// $Id: Matcher.java,v 1.1.1.1 2000/09/26 11:20:34 ramsdell Exp $

// A string matcher.

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
 * A string matcher.
 * This class implements pattern matches of the form:
 * pattern = prefix + "%" + suffix.  If the pattern does contain '%',
 * the percent sign is added to the beginning of the pattern.  
 * A pattern with more than one percent sign is erroneous.
 * @version November 1997
 * @author John D. Ramsdell
 */

final class Matcher
{
  // The wild card for string patterns.
  private final static char wildCard = '%';

  private String prefix = "";
  private String suffix = null;
  private int prefixLength;
  private int suffixLength;

  /**
   * Create a matcher associated with a given pattern.
   * When given a bad pattern, a matcher that matches nothing is created.
   */
  Matcher(String pattern) {
    if (isPatternOkay(pattern)) {
      suffix = pattern;
      prefixLength = pattern.indexOf(wildCard);
      if (prefixLength >= 0) {
	prefix = pattern.substring(0, prefixLength);
	suffix = pattern.substring(prefixLength + 1);
      }
      else
	prefixLength = 0;
      suffixLength = suffix.length();
    }
  }

  /**
   * Match a string against the pattern.
   * @return null if there is no match, otherwise a substitution
   *         that makes the pattern equal to the string 
   */
  String match(String string) {
    if (suffix != null
	&& string.length() >= prefixLength + suffixLength
	&& string.startsWith(prefix)
	&& string.endsWith(suffix)) {
      string = string.substring(0, string.length() - suffixLength);
      string = string.substring(prefixLength);
      return string;
    }
    else
      return null;
  }

  /**
   * Substitute the wild cards in the replacement with the match.
   */
  static String subst(String match, String replacement) {
    if (match == null)
      return replacement;
    int i = replacement.indexOf(wildCard);
    if (i < 0)
      return replacement;
    StringBuffer sb = new StringBuffer();
    do {
      sb.append(replacement.substring(0, i));
      sb.append(match);
      replacement = replacement.substring(i + 1);
      i = replacement.indexOf(wildCard);
    }
    while (i >= 0);
    sb.append(replacement);
    return sb.toString();
  }

  /**
   * Test if a string has at least one wild card.
   */
  static boolean isPattern(String string) {
    return string.indexOf(wildCard) >= 0;
  }

  /**
   * Test if a pattern has at most one wild card.
   */
  static boolean isPatternOkay(String pattern) {
    int wildCards = 0;
    for (int i = 0; i < pattern.length(); i++)
      if (pattern.charAt(i) == wildCard)
	wildCards++;
    return wildCards <= 1;
  }
}
