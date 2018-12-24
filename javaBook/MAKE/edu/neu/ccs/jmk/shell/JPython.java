// $Id: JPython.java,v 1.1.1.1 2000/09/26 11:20:35 ramsdell Exp $

// The implementation of the JPython operator.

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

package edu.neu.ccs.jmk.shell;

import org.python.util.PythonInterpreter; 
import org.python.core.PyException; 
import edu.neu.ccs.jmk.Operator;
import edu.neu.ccs.jmk.CommandFailedException;

/**
 * JPython as an Operator.
 * <pre>
 * python = "edu.neu.ccs.jmk.shell.JPython";
 *
 * "all":;
 * {
 *   forname python "1 + 4";
 * }
 * </pre>
 */
public class JPython 
implements Operator
{
  public String getName() {
    return "JPython";
  }

  /**
   * Execute the operation by concatenating the arguments
   * and executing the string using JPython.
   * @param args parameters to the operation
   * @param out place to write messages
   * @exception CommandFailedException if operation failed
   */
  public void exec(String[] args, java.io.PrintWriter out)
       throws CommandFailedException
  {
    if (args.length == 0)
      throw new CommandFailedException("No args to " + getName());
    StringBuffer sb = new StringBuffer(args[0]);
    for (int i = 1; i < args.length; i++)
      sb.append(args[i]);
    PythonInterpreter pi = new PythonInterpreter();
    try {
      pi.exec(sb.toString());
    }
    catch (PyException pex) {
      out.println(pex);
      throw new CommandFailedException(getName() + " threw an exception");
    }
  }
}
