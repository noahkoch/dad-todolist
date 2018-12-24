// $Id: Rule.java,v 1.1.1.1 2000/09/26 11:20:34 ramsdell Exp $

// Rules defined by a makefile.

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

import java.io.File;
import java.io.PrintWriter;
import java.util.Vector;

/**
 * This class implements rules.
 * @version November 1997
 * @author John D. Ramsdell
 */
final class Rule
{
  private boolean verbose = false;

  // This method is used to recursively make prerequisites of a rule.
  private static boolean makeGoals(Rule[] rules, PrintWriter out)
       throws CommandFailedException
  {
    boolean made = false;
    for (int i = 0; i < rules.length; i++) {
      Rule rule = rules[i];
      if (rule.isUpdating())
	out.println("Circular dependency " + rule + " dropped");
      else if (rule.make())
	made = true;
    }
    return made;
  }

  // This method is used to recursively reset prerequisites of a rule.
  private static void resetGoals(Rule[] rules) {
    for (int i = 0; i < rules.length; i++)
      rules[i].reset();
  }

  private Make make;		// Used the test the verbose flag
  private String target;	// and try patterns
  private File file;
  private Rule[] prerequisites = new Rule[0];
  private Command[] commands = new Command[0];
  private int lineNumber = -1;
  // match is non-null if a pattern was use to define the rule
  private String match = null;	

  /**
   * Create a rule.
   * @param make the make object associated with the rule
   * @param target the target of the rule
   */
  Rule(Make make, String target) {
    this.make = make;
    this.target = target;
    file = new File(StringUtils.localizePaths(target));
  }

  /**
   * Merges prerequisites and commands into a rule.
   * Prerequisites are added to the end of the current list of prerequisites.
   * The commands are added if the rule has no other commands, otherwise
   * they are ignored.
   * @param addedPrerequisites prereqs to be added
   * @param commands new commands for consideration
   * @param lineNumber the position of the information in the makefile
   */
  void merge(Rule[] addedPrerequisites,
	     Command[] commands,
	     int lineNumber)
  {
    if (prerequisites.length == 0)
      prerequisites = addedPrerequisites;
    else if (addedPrerequisites.length > 0) {
      Rule[] old = prerequisites;
      int len = old.length;
      prerequisites = new Rule[len + addedPrerequisites.length];
      for (int i = 0; i < len; i++)
	prerequisites[i] = old[i];
      for (int i = 0; i < addedPrerequisites.length; i++)
	prerequisites[len + i] = addedPrerequisites[i];
    }
    if (this.commands.length == 0)
      this.commands = commands;
    if (this.lineNumber == -1)
      this.lineNumber = lineNumber;
  }

  /**
   * Get the target of the rule.
   */
  String getTarget() {
    return target;
  }

  /**
   * Get the target after localization of separators.
   * @see edu.neu.ccs.jmk.StringUtils#localizePaths(String)
   */
  String getLocalizedTarget() {
    return file.getPath();
  }

  /**
   * Set the match in the case in which a pattern defines this rule.
   */
  private void setMatch(String match) {
    this.match = StringUtils.localizePaths(match);
  }

  /**
   * This method checks the patterns to see if one
   * is applicable to this rule.  A rule that has commands
   * will not check the list of patterns for matches.
   */
  private void checkPatterns() {
    if (commands.length > 0)
      return;
    make.tryPatterns(this);
  }

  /**
   * Called by make.tryPatterns when a matching pattern is found.
   */
  boolean tryPattern(String match, String[] dependencies,
		     Command[] commands, int lineNumber) {
    setMatch(match);
    // Add new prerequisites to the FRONT of the list.
    Rule[] addedPrerequisites = prerequisites;
    prerequisites = new Rule[dependencies.length];
    for (int i = 0; i < dependencies.length; i++) // Replace names
      prerequisites[i] = make.get(dependencies[i]); // by rules
    merge(addedPrerequisites, commands, lineNumber);
    return true;
  }

  // Status flags

  private boolean made = false;

  private boolean isMade() {	// Is target made?
    return made;
  }

  private boolean used = false;	// Has this rule been used?  See reset()

  private boolean updating = false;

  boolean isUpdating() {	// Is target updating?
    return updating;
  }

  private boolean phony = false; // Is target phony?  See exists()

  void setPhony(boolean phony) { 
    this.phony = phony;
  }

  private boolean patternsChecked = false; // Have patterns been checked yet?

  boolean make() 
       throws CommandFailedException
  {
    if (made)			// If made, nothing to do
      return made;
    if (!patternsChecked) {	// Just check patterns once
      checkPatterns();
      patternsChecked = true;
    }
    verbose = make.isVerbose();
    PrintWriter out = make.getOut();
    if (verbose) {
      if (prerequisites.length > 0 || commands.length > 0)
	out.println("Making " + getTarget());
    }
    used = true;
    try {			// Updating is true while
      updating = true;		// this context is active
      Rule[] remakeRules;
      made = makeGoals(prerequisites, out);
      boolean targetExists = exists();
      if (targetExists) {
	remakeRules = checkDates(); // The prerequisites excluded from
	if (!made && remakeRules.length == 0) { // this list are up to date
	  if (verbose) 
	    out.println(getTarget() + " is up to date");
	  return made;
	}
      }
      else {
	remakeRules = prerequisites; // All prerequisites are out of date
	if (remakeRules.length == 0 && commands.length == 0) {
	  String msg = "No way to make " + getTarget();
	  throw new CommandFailedException(msg);
	}
      }
      made = runCommands(remakeRules, out) || made;
      // reset cache so created files get the correct last modified time
      cachedLastModified = 0L;
      if (targetExists && !made) {
	// Target exists but no update occurred
	String msg = "Make of " + getTarget() + " failed";
	throw new CommandFailedException(msg);
      }
      if (verbose) {
	if (made)
	  out.println("Made " + getTarget());
	else if (prerequisites.length > 0 || commands.length > 0)
	  out.println("Nothing to do for " + getTarget());
      }			     
      return made;
    }
    finally {
      updating = false;
    }
  }

  private boolean exists() {
    if (phony)			// Phony targets are not associated
      return false;		// with a file
    else
      return file.exists();
  }
  
  private long cachedLastModified = 0L;

  // This routine is only called when the target exists.
  // file.lastModified() returns 0L only when the file does not exist.
  private long lastModified() {
    if (cachedLastModified == 0L) 
      cachedLastModified = file.lastModified();
    return cachedLastModified;
  }
   
  // Returns the list of prerequisites that are out of date with respect
  // to the target.
  private Rule[] checkDates() {
    Vector v = new Vector();
    long whenModified = lastModified();	// Not zero by assumption
    for (int i = 0; i < prerequisites.length; i++) {
      long dependModified = prerequisites[i].lastModified();
      if (dependModified == 0L || whenModified < dependModified)
	v.addElement(prerequisites[i]);
    }
    Rule[] rules = new Rule[v.size()];
    v.copyInto(rules);
    return rules;
  }

  /**
   * Runs the commands.
   * @return true if at least one command ran
   */
  private boolean runCommands(Rule[] newer, PrintWriter out)
       throws CommandFailedException
  {
    Rule first = null;
    if (prerequisites.length > 0)
      first = prerequisites[0];
    for (int i = 0; i < commands.length; i++) {
      if (make.isInterruptEnabled()) // Poll interrupt status
	throw new CommandFailedException("make interrupted");
      commands[i].run(this, first, newer, match,
		      make.isJustPrinting(), out);
    }
    return commands.length > 0;
  }

  /**
   * Reset a rule.
   * This recursively calls reset on its prerequisites if it is used.
   */
  void reset() {
    cachedLastModified = 0L;
    if (used) {
      used = false;
      made = false;
      updating = false;
      resetGoals(prerequisites);
    }
  }
}
