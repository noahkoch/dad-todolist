// $Id: Expression.java,v 1.1.1.1 2000/09/26 11:20:34 ramsdell Exp $

// Expressions

/*
 * Copyright 1999 by John D. Ramsdell
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
 * Expressions.
 * This is the base class of all classes that 
 * implement expression evaluation.
 * @version May 1999
 * @author John D. Ramsdell
 */
abstract class Expression 
{
  /*
   * Evaluate expression.
   * @param env run time environment
   * @param a string list
   * @return result of invocation appended to the list
   * (if list is non-null, result must be a string list)
   * @exception Exception if evaluation failed
   */
  abstract Value eval(Environment env, StringList list)
       throws Exception;
}
