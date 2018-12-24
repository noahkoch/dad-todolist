// $Id: WildCardFilter.java,v 1.1.1.1 2000/09/26 11:20:35 ramsdell Exp $

// Wild card file name filter

/*
 * Copyright 1999 by John D. Ramsdell and Olivier Refalo
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

import java.io.*;

public final class WildCardFilter
implements FilenameFilter
{
  private /* final */ String pattern;
  private /* final */ char wildCard;
  private /* final */ int[] wildIndex; // Contains the index of each occurrence
  private /* final */ String prefix;	// of the wild card in the pattern
  private /* final */ String suffix;

  public WildCardFilter(String pattern, char wildCard) {
    this.pattern = pattern;
    this.wildCard = wildCard;
    int wilds = 0;
    for (int i = 0; i < pattern.length(); i++)
      if (wildCard == pattern.charAt(i))
	wilds++;
    wildIndex = new int[wilds];
    int j = 0;
    for (int i = 0; j < wilds; i++) 
     if (wildCard == pattern.charAt(i))
       wildIndex[j++] = i;
    if (wilds == 0) {
      prefix = null;
      suffix = null;
    }
    else {
      prefix = pattern.substring(0, wildIndex[0]);
      suffix = pattern.substring(wildIndex[wilds - 1] + 1);
    }
  }

  public boolean accept(File dir, String name) {
    if (wildIndex.length == 0)
      return pattern.equals(name);
    else if (!name.startsWith(prefix)
	     || !name.endsWith(suffix))
      return false;
    else if (wildIndex.length == 1)
      return true;
    else {
      int flen = name.length() - suffix.length();
      int j = wildIndex[0];
      int f = j;		// index into file
      for (int i = 1; i < wildIndex.length; i++) {
	/* i is the index into wildIndex */
	/* j is wildIndex[i - 1] at loop start */
	/* pattern matched is pattern.substring(j + 1, wildIndex[i]) */
	int pstart = j + 1;
	j = wildIndex[i];
	int plen = j - pstart;
	for (;;) {		// Find pattern in rest of name
	  if (plen + f > flen)
	    return false;
	  /* Is inlining regionMatches is faster that calling it? */
	  else if (name.regionMatches(f, pattern, pstart, plen))
	    break;
	  else
	    f++;
	}
	f += plen;
      }
      return true;
    }
  }

  //Filter tests
  static private void test(String pattern, String file) {
    WildCardFilter f = new WildCardFilter(pattern, '*');
    String match = f.accept(null, file) ? "matches" : "does not match";
    System.out.println("String \"" + file + "\" " + match +
		       " pattern \"" + pattern + "\".");
  }

  public static void main(String args[]) {
    if (args.length == 2)
      test(args[0], args[1]);
    else {
      test("*", "toto");
      test("toto.java", "tutu.java");
      test("12345", "1234");
      test("1234", "12345");
      test("*f", "");
      test("***", "toto");
      test("*.java", "toto.");
      test("*.java", "toto.jav");
      test("*.java", "toto.java");
      test("abc*", "");
      test("a*c", "abbbbbccccc");
      test("abc*xyz", "abcxxxyz");
      test("*xyz", "abcxxxyz");
      test("abc**xyz", "abcxxxyz");
      test("abc**x", "abcxxx");
      test("*a*b*c**x", "aaabcxxx");
      test("abc*x*yz", "abcxxxyz");
      test("abc*x*yz*", "abcxxxyz");
      test("a*b*c*x*yf*z*", "aabbccxxxeeyffz");
      test("a*b*c*x*yf*zze", "aabbccxxxeeyffz");
      test("a*b*c*x*yf*ze", "aabbccxxxeeyffz");
      test("a*b*c*x*yf*ze", "aabbccxxxeeyfze");
      test("*LogServerInterface*.java", "_LogServerInterfaceImpl.java");
      test("abc*xyz", "abcxyxyz");
    }
  }
}
