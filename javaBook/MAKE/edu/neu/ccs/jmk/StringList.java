// $Id: StringList.java,v 1.1.1.1 2000/09/26 11:20:35 ramsdell Exp $

// Implements a string list as a value.

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
 * Implements a string list as a value.
 * @version May 1999
 * @author John D. Ramsdell
 */
public final class StringList
implements Value
{
  private /* final */ String string;
  private StringList rest;

  /**
   * Construct a string list.
   * @param string a non-null string
   * @param rest tail of the list
   * @exception NullPointerException when string is null
   */
  public StringList(String string, StringList rest) {
    if (string == null)
      throw new NullPointerException();
    this.string = string;
    this.rest = rest;
  }

  /**
   * Construct a one element string list.
   */
  public StringList(String string) {
    this(string, null);
  }

  /**
   * Get string associated with this cell.
   */
  public String getString() {
    return string;
  }

  /**
   * Get string list following this cell.
   */
  public StringList getRest() {
    return rest;
  }

  /**
   * Set string list following this cell.
   * Used only in this package because unrestricted use
   * could cause problems with the evaluator.
   */
  void setRest(StringList rest) { // NOT public
    this.rest = rest;
  }

  /**
   * Determines which values are string lists.
   */
  public static boolean isStringList(Value v) {
    return v == null || v instanceof StringList;
  }

  /**
   * Number of strings in a string list.
   */
  public static int length(StringList sl) {
    int len = 0;
    for (; sl != null; sl = sl.rest)
      len++;
    return len;
  }

  /**
   * Constructs a string array from a string list.
   */
  public static String[] list2array(StringList sl) {
    int len = length(sl);
    String[] result = new String[len];
    for (int i = 0; sl != null; sl = sl.rest)
      result[i++] = sl.string;
    return result;
  }

  /**
   * Constructs a string list from a string array.
   */
  public static StringList array2list(String[] sa) {
    StringList result = null;
    if (sa != null)
      for (int i = sa.length - 1; i >= 0; i--)
	result = new StringList(sa[i], result);
    return result;
  }

  /**
   * Appends two string lists.
   */
  public static StringList append(StringList sl1, StringList sl2) {
    if (sl2 == null)
      return sl1;
    else if (sl1 == null)
      return sl2;
    else {
      StringList sl0 = new StringList(sl1.getString());
      StringList last = sl0;
      for (sl1 = sl1.getRest(); sl1 != null; sl1 = sl1.getRest()) {
	StringList temp = new StringList(sl1.getString());
	last.rest = temp;
	last = temp;
      }
      last.setRest(sl2);
      return sl0;
    }
  }
}
