// $Id: CreateOperator.java,v 1.1.1.1 2000/09/26 11:20:34 ramsdell Exp $

// The create operator for creating a file and its contents.

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
 * The create operator for creating a file and its contents.
 * @version April 1998
 * @author John D. Ramsdell
 */
public final class CreateOperator
implements Operator
{
  /**
   * Get the name of an operator.
   * @return the name for printing
   */
  public String getName() {
    return "create";
  }

  /**
   * The create operator creates a file given by its first operand
   * and than writes any remaining operands into the file as 
   * separate lines of text.
   * @param args parameters to the operation
   * @param out place to write messages
   */
  public void exec(String[] args, java.io.PrintWriter out)
       throws CommandFailedException
  {
    if (args.length == 0) {
      String msg = getName() + " received no operands";
      throw new CommandFailedException(msg);
    }
    try {
      FileWriter fw = new FileWriter(args[0]);
      if (args.length == 1)
	fw.close();
      else {
	BufferedWriter bw = new BufferedWriter(fw);
	PrintWriter pw = new PrintWriter(bw);
	for (int i = 1; i < args.length; i++)
	  pw.println(args[i]);
	pw.close();
      }
      return;
    }
    catch (FileNotFoundException ex) {
      out.println("Cannot open " + ex.getMessage());
    }
    catch (IOException ex) {
      out.println(ex.toString());
    }
    String msg = getName() + " " + args[0] + " operation failed";
    throw new CommandFailedException(msg);
  }

  /**
   * An entry point for testing create commands.
   * It simply passes the arguments to the create operation.
   * @param args operands for the command
   */
  public static void main(String[] args) {
    PrintWriter out = new PrintWriter(System.out, true);
    try {
      new CreateOperator().exec(args, out);
    }
    catch (Throwable t) {
      System.err.println("Internal error: " + t.getMessage());
      t.printStackTrace();
    }
    out.println("Create command completed");
  }
}
