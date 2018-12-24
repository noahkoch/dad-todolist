// $Id: NoteOperator.java,v 1.1.1.1 2000/09/26 11:20:34 ramsdell Exp $

// The null operator used when printing notes.

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
 * The null operator used when printing notes.
 * @version January 1998
 * @author John D. Ramsdell
 */
public final class NoteOperator
implements Operator
{
  /**
   * Get the name of an operator.
   * @return the name for printing
   */
  public String getName() {
    return "note";
  }

  /**
   * This operation does nothing.
   * @param args parameters to the operation
   * @param out place to write messages
   */
  public void exec(String[] args, java.io.PrintWriter out) {
  }
}
