// $Id: FileOperator.java,v 1.1.1.1 2000/09/26 11:20:34 ramsdell Exp $

// Executes a file operation on file operands.

/*
 * Copyright 1997 by John D. Ramsdell
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package edu.neu.ccs.jmk;

import java.io.*;
import java.util.Vector;

/**
 * This operator is used to implement file commands.
 * To be useful, subclasses must implement
 * <code>getName(String)</code> and <code>exec(File, PrintWriter)</code>.
 * @version November 1997
 * @author John D. Ramsdell
 */
abstract class FileOperator
implements Operator
{
  private final static char wildCard = '*';

  /**
   * The actual method implemented by a particular file operator
   * is given by defining this method.
   * @return true if the file operation completed successfully; 
   *         false otherwise
   */
  abstract boolean exec(File arg, PrintWriter out);

  /**
   * Applies the file operator to each argument.
   * If an argument contains the wild card character '*',
   * the argument is expanded, and the file operator is applied
   * to each element in the expansion.
   * @param args operands given to the file operator
   * @param out place to write messages
   * @exception CommandFailedException if operation failed
   */
  public void exec(String[] args, PrintWriter out)
       throws CommandFailedException
  {
    StringBuffer sb = new StringBuffer(); // For error messages
    exec(args, out, sb);
    if (sb.length() != 0) {	// Errors occurred
      sb.insert(0, getName());
      throw new CommandFailedException(sb.toString());
    }
  }

  private void exec(String[] args, PrintWriter out, StringBuffer sb) {
    for (int i = 0; i < args.length; i++) {
      String fileName = args[i];
      if (fileName.indexOf(wildCard) < 0) { // If no wild card exec
	if (!exec(new File(fileName), out)) {
	  sb.append(' ');
	  sb.append(fileName);
	}
      }
      else {			// else glob and then exec
	try {
	  exec(glob(fileName), out, sb);
	}
	catch (CommandFailedException ex) {
	  sb.append(' ');
	  sb.append(fileName);
	}
      }
    }
  }

  /**
   * This method expands a localized file name that may contain wild
   * cards to an array of file names without the wild cards.
   * All file separators in the file name must preceed any wild card.
   * @exception CommandFailedException if wild card is misused
   */
  static String[] glob(String fileName)
       throws CommandFailedException
  {
    int wildCardIndex = fileName.indexOf(wildCard);
    if (wildCardIndex < 0)	// No wild card
      return new String[] { fileName };
    else {
      int separatorIndex = fileName.lastIndexOf(File.separatorChar);
      if (separatorIndex > wildCardIndex) {
	String msg = "Cannot expand " + fileName;
	throw new CommandFailedException(msg);
      }
      String pattern;
      String dirName;
      File dir;
      if (separatorIndex >= 0) {
	pattern = fileName.substring(separatorIndex + 1);
	dirName = fileName.substring(0, separatorIndex + 1);
	dir = new File(dirName);
      }
      else {
	pattern = fileName;
	dirName = "";
	dir = new File(System.getProperty("user.dir"));
      }
      String[] list = dir.list(new WildCardFilter(pattern, wildCard));
      if (list == null)
	return new String[0];
      Vector v = new Vector();
      for (int i = 0; i < list.length; i++)
	list[i] = dirName + list[i];
      return list;
    }
  }

  /**
   * This method expands an array of localized file names that may
   * contain wild cards to an array of file names without wild cards.
   * Wild cards must conform to the rules given above.
   */
  static String[] glob(String[] args)
       throws CommandFailedException
  {
    for (int i = 0; i < args.length; i++) {
      if (args[i].indexOf(wildCard) >= 0) { // If wild card
	Vector v = new Vector();
	for (int j = 0; j < i; j++) // Add previous strings
	  v.addElement(args[j]);
	addStrings(v, glob(args[i])); // Add this arg
	for (int j = i + 1; j < args.length; j++) // Add remaining args
	  if (args[j].indexOf(wildCard) >= 0)
	    addStrings(v, glob(args[j]));
	  else
	    v.addElement(args[j]);
	String[] result = new String[v.size()];
	v.copyInto(result);
	return result;		// Wild cards found
      }
    }
    return args;		// No wild cards found
  }

  private static void addStrings(Vector v, String[] s) {
    for (int i = 0; i < s.length; i++)
      v.addElement(s[i]);
  }

  /**
   * This method expands a localized file name into a list of all 
   * directories in that file.
   */
  static String[] dirs(String fileName) {
    Vector v = new Vector();
    findDirs(new File(fileName), v);
    String[] result = new String[v.size()];
    v.copyInto(result);
    return result;
  }
    
  private static void findDirs(File f, Vector v) {
    if (f.isDirectory()) {
      v.addElement(f.getPath());
      String[] l = f.list();
      for (int i = 0; i < l.length; i++)
	findDirs(new File(f, l[i]), v);
    }
  }

  /**
   * An entry point for testing exec commands.
   * It simply passes the arguments to the exec operation.
   * @param args operands for the command
   */
  public static void main(String[] args) {
    PrintWriter out = new PrintWriter(System.out, true);
    FileOperator operator = new FileOperator() {
      boolean exec(File arg, PrintWriter out) {
	return arg.delete();
      }
      public String getName() {
	return "delete";
      }
    };
    try {
      operator.exec(args, out);
    }
    catch (Throwable t) {
      System.err.println("Internal error: " + t.getMessage());
      t.printStackTrace();
    }
    out.println("File command " + operator.getName() + " completed");
  }
}
