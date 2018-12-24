// $Id: ReverseFunction.java,v 1.1.1.1 2000/09/26 11:20:35 ramsdell Exp $

// An example of a function: the reverse string list function.

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
 * An example of a function: the reverse string list function.
 * This class is used only for testing and as an example.
 * @version July 1999
 * @author John D. Ramsdell
 */
public class ReverseFunction
implements Function
{
  private final static int nargs = 1;

  /**
   * Get the name of an function.
   * @return the name for printing
   */
  public String getName() {
    return "reverse";
  }

  /**
   * Invoke the reverse function.
   * @param args parameters to the function
   * @param list a string list 
   * @return result of invocation appended to the list
   * (if list is non-null, result must be a string list)
   * @exception Exception if invocation failed
   */
  public Value invoke(Value[] args, StringList list)
       throws Exception
  {
    if (args.length == nargs) {
      if (StringList.isStringList(args[0])) {
	StringList sl = (StringList)args[0];
	for (; sl != null; sl = sl.getRest())
	  list = new StringList(sl.getString(), list);
	return list;
      }
      else {
	String msg = getName() 
	  + ": the argument is not a string list";
	throw new StringListCastException(msg);
      }
    }
    else {
      String msg = "Arg count error: " + getName() + " expecting " + nargs +
	" but got " + args.length + " arguments";
      throw new WrongArgCountException(msg);
    }
  }
}
