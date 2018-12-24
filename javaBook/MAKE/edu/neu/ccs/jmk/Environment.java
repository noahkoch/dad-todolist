// $Id: Environment.java,v 1.1.1.1 2000/09/26 11:20:34 ramsdell Exp $

// Environments

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
 * Environments used during expression evaluation.
 * An environment is a list of arrays of values.
 * @version May 1999
 * @author John D. Ramsdell
 */
final class Environment
{
  private /* final */ Environment parent;
  private /* final */ Value[] rib;

  Environment (Environment parent, Value[] rib) {
    this.parent = parent;
    this.rib = rib;
  }

  static Value lookup(Environment env, int up, int offset) {
    for(; up > 0; up--)
      env = env.parent;
    return env.rib[offset];
  }
}
