// $Id: ExecOperator.java,v 1.1.1.1 2000/09/26 11:20:34 ramsdell Exp $

// Executes a command in a separate process.

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

/**
 * This operator is used to implement exec commands.
 * It executes a command in a separate process using
 * the runtime's exec method.
 * @version November 1997
 * @author John D. Ramsdell
 */
final class ExecOperator
implements Operator
{

  /**
   * Get the name of this operator.
   * @return the name of this operator
   */
  public String getName() {
    return "exec";
  }

  /**
   * Executes a command in a separate process using
   * the runtime's exec method.
   * @see java.lang.Runtime#exec(String[])
   * @param args operands given to exec method of the runtime
   * @param out place to write messages
   * @exception CommandFailedException if operation failed
   */
  public void exec(String[] args, PrintWriter out)
       throws CommandFailedException
  {
    String msg = null;		// For error messages
    try {
      // Create the child process.
      Process p = Runtime.getRuntime().exec(args);
      // Start a thread which copies the input stream to out.
      Thread t = new Thread(new BufferedCopy(p.getInputStream(), out));
      t.start();
      // Copy the error stream.
      new BufferedCopy(p.getErrorStream(), out).run();
      int exitCode = p.waitFor(); // Wait for the termination of the child
      t.join();			// Wait for the end of the input stream
      if (exitCode != 0) {
	if (args.length > 0) {	// Copy command when an error occurs
	  msg = args[0];
	  for (int i = 1; i < args.length; i++)
	    msg += " " + args[i];
	}
	else
	  msg = "No args to exec";
      }
    }
    catch (IOException ioEx) {	// from exec
      msg = "In exec: " + ioEx.toString();
    }
    catch (InterruptedException iEx) { // from waitFor
      msg = "In waitFor: " + iEx.toString();
    }
    if (msg != null)
      throw new CommandFailedException(msg);
  }

  /**
   * An object which copies the input stream to out.
   * The copy can be run by a thread.
   */
  private static class BufferedCopy
  implements Runnable
  {
    private InputStream is;
    private PrintWriter out;

    BufferedCopy(InputStream is, PrintWriter out) {
      this.is = is;
      this.out = out;
    }
      
    public void run() {
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      try {
	for (;;) {
	  String line = br.readLine();
	  if (line == null)
	    break;
	  out.println(line);
	}
	br.close();
      }
      catch (IOException ex) { }
    }
  }

  /**
   * An entry point for testing exec commands.
   * It simply passes the arguments to the exec operation.
   * @param args operands for the command
   */
  public static void main(String[] args) {
    PrintWriter out = new PrintWriter(System.out, true);
    try {
      new ExecOperator().exec(args, out);
    }
    catch (Throwable t) {
      System.err.println("Internal error: " + t.getMessage());
      t.printStackTrace();
    }
    out.println("Exec command completed");
  }
}
