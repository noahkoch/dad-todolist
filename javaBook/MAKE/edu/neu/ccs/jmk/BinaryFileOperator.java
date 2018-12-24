// $Id: BinaryFileOperator.java,v 1.1.1.1 2000/09/26 11:20:34 ramsdell Exp $

// Executes a binary file operation on file operands.

/*
 * Copyright 1998 by John D. Ramsdell
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

/**
 * This operator is used to implement binary file commands.
 * To be useful, subclasses must implement
 * <code>getName(String)</code> and <code>exec(File, File, PrintWriter)</code>.
 * @version April 1998
 * @author John D. Ramsdell
 */
abstract class BinaryFileOperator
implements Operator
{

  /**
   * The actual method implemented by a particular file operator
   * is given by defining this method.
   * @return true if the file operation completed successfully; 
   *         false otherwise
   */
  abstract boolean exec(File arg1, File arg2, PrintWriter out);

  /**
   * Applies the binary file operator to the arguments.
   * If an argument contains the wild card character '*',
   * the argument is expanded.
   * @param arg operands given to the file operator
   * @param out place to write messages
   * @exception CommandFailedException if operation failed
   */
  public void exec(String[] args, PrintWriter out)
       throws CommandFailedException
  {
    args = FileOperator.glob(args);
    if (args.length != 2) {
      String msg = getName() + " expects 2 operands, but got " + args.length;
      throw new CommandFailedException(msg);
    }
    if (!exec(new File(args[0]), new File(args[1]), out)) {
      String msg = getName() + " " + args[0] + " " + args[1] +
	" operation failed";
      throw new CommandFailedException(msg);
    }
  }

  /**
   * An entry point for testing exec commands.
   * It simply passes the arguments to the exec operation.
   * @param args operands for the command
   */
  public static void main(String[] args) {
    PrintWriter out = new PrintWriter(System.out, true);
    BinaryFileOperator operator = new BinaryFileOperator() {
      boolean exec(File arg1, File arg2, PrintWriter out) {
	return arg1.renameTo(arg2);
      }
      public String getName() {
	return "rename";
      }
    };

    try {
      operator.exec(args, out);
    }
    catch (Throwable t) {
      System.err.println("Internal error: " + t.getMessage());
      t.printStackTrace();
    }
    out.println("Binary file command " + operator.getName() + " completed");
  }
}
