// $Id: Global.java,v 1.1.1.1 2000/09/26 11:20:34 ramsdell Exp $

// Globals

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
 * Global cells which are used to bind values to identifiers.
 * As an expression, a global is a reference of a global identifier.
 * @version May 1999
 * @author John D. Ramsdell
 */
final class Global
extends Expression
{
  private /* final */ String identifier;
  private Value value = null;

  Global (String identifier) {
    this.identifier = identifier;
  }

  String getIdentifier() {
    return identifier;
  }

  Value eval(Environment env, StringList list)
       throws StringListCastException
  {
    if (list == null) 
      return value;
    else if (StringList.isStringList(value))
      return StringList.append((StringList)value, list);
    else {
      String msg = "Append error: the value in " + identifier + 
	" is not a string list";
      throw new StringListCastException(msg);
    }
  }

  void setValue(Value value) {
    this.value = value;
  }
}
