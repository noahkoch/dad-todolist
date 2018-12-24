// $Id: ReverseOperator.java,v 1.1.1.1 2000/09/26 11:20:34 ramsdell Exp $

// An example of an operator: the reverse command operator.

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

/**
 * An example of an operator: the reverse command operator.
 * This class is used only for testing and as an example.
 * @version November 1997
 * @author John D. Ramsdell
 */
public class ReverseOperator
implements Operator
{
  /**
   * Get the name of an operator.
   * @return the name for printing
   */
  public String getName() {
    return "reverse";
  }

  /**
   * Execute the reverse operation.  The operator simply prints
   * the arguments in reverse order.
   * @param args parameters to the operation
   * @param out place to write messages
   * @exception CommandFailedException if no args provided
   */
  public void exec(String[] args, java.io.PrintWriter out)
       throws CommandFailedException
  {
    if (args.length > 0) {
      out.print(args[args.length - 1]);
      for (int i = args.length - 2; i >= 0; i--)
	out.print(" " + args[i]);
      out.println();
    }
    else
      throw new CommandFailedException("Too few args to " + getName());
  }
}
