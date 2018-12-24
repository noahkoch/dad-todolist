// $Id: Pattern.java,v 1.1.1.1 2000/09/26 11:20:34 ramsdell Exp $

// Pattern rules defined by a makefile.

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
 * Pattern rules defined by a makefile.
 * A pattern rule defines commands that can be used by a rule defined
 * without commands as long as the rule's target matches the target 
 * of the pattern rule.
 * @version November 1997
 * @author John D. Ramsdell
 */
final class Pattern
{
  private Matcher matcher;
  private String[] prerequisites;
  private Command[] commands;
  private int lineNumber;

  /**
   * Create a pattern rule.
   */
  Pattern(String target, String[] prerequisites,
	  Command[] commands, int lineNumber) {
    matcher = new Matcher(target);
    this.prerequisites = prerequisites;
    this.commands = commands;
    this.lineNumber = lineNumber;
  }

  /**
   * Merge a pattern into a rule.
   * @return true if the targets match and the rule uses the pattern.
   */
  boolean merge(Rule rule) {
    String target = rule.getTarget();
    String match = matcher.match(target);
    if (match == null)
      return false;
    String[] dependencies = new String[prerequisites.length];
    for (int i = 0; i < prerequisites.length; i++)
      dependencies[i] = Matcher.subst(match, prerequisites[i]);
    return rule.tryPattern(match, dependencies, commands, lineNumber);
  }
}
