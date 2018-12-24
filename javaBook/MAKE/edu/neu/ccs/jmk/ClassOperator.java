// $Id: ClassOperator.java,v 1.1.1.1 2000/09/26 11:20:34 ramsdell Exp $

// Applies an operator loaded from the first argument to the remander of 
// the operands.

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

import java.io.PrintWriter;

/**
 * This operator is used to implement class commands.
 * It invokes an arbitrary operation which is specified in a makefile.
 * The first argument is treated as the name of a class and loaded.
 * An instance of the class is created which must implement Operator.
 * The instance is then applied to the remaining operands.
 * @version November 1997
 * @author John D. Ramsdell
 */
final class ClassOperator
implements Operator
{
  private final String name = "forname";

  /**
   * Get the name of this operator.
   * @return the name of this operator
   */
  public String getName() {
    return name;
  }

  /**
   * Applies the operation specified by the first argument
   * to the remaining arguments.
   * @param args operands from the command
   * @param out place to write messages
   * @exception CommandFailedException if operation failed
   */
  public void exec(String[] args, PrintWriter out)
       throws CommandFailedException
  {
    String msg = null;		// For error messages
    try {
      if (args.length > 0) {
	Class cls = Class.forName(args[0]);
	Operator operator = (Operator)cls.newInstance();
	String[] params = new String[args.length - 1];
	for (int i = 0; i < params.length; i++)
	  params[i] = args[i + 1];
	operator.exec(params, out);
      }
      else
	msg = "Missing class name";
    }
    catch (CommandFailedException ex) {	// From exec
      throw ex;
    }
    catch (ClassNotFoundException ex) {	// From forName
      msg = args[0] + " class not found";
    }
    catch (ClassCastException ex) { // From (Operator)newInstance()
      msg = args[0] + " does not implement Operator";
    }
    catch (IllegalAccessException ex) {
      msg = "Cannot create an instance of " + args[0] +
	" due to access restrictions";
    }
    catch (InstantiationException ex) {
      msg = "Cannot create an instance of " + args[0];
    }
    if (msg != null)
      throw new CommandFailedException(msg);
  }

  /**
   * An entry point for testing class commands.
   * It simply passes the arguments to the exec operation.
   * @param args operands for the command
   */
  public static void main(String[] args) {
    PrintWriter out = new PrintWriter(System.out, true);
    try {
      new ClassOperator().exec(args, out);
    }
    catch (Throwable t) {
      System.err.println("Internal error: " + t.getMessage());
      t.printStackTrace();
    }
    out.println("Class command completed");
  }
}
