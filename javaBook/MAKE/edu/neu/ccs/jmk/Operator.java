// $Id: Operator.java,v 1.1.1.1 2000/09/26 11:20:34 ramsdell Exp $

// The operator interface for commands.

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
 * The operator interface.
 * Classes that implement this interface can be used as
 * command operators.
 * @see edu.neu.ccs.jmk.Command
 * @version November 1997
 * @author John D. Ramsdell
 */
public interface Operator
{
  /**
   * Get the name of an operator.
   * @return the name for printing
   */
  String getName();

  /**
   * Execute the operation specified by the operator.
   * @param args parameters to the operation
   * @param out place to write messages
   * @exception CommandFailedException if operation failed
   */
  void exec(String[] args, java.io.PrintWriter out)
       throws CommandFailedException;

}
