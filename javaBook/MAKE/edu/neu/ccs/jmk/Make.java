// $Id: Make.java,v 1.1.1.1 2000/09/26 11:20:34 ramsdell Exp $

// The make class

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

import java.io.*;
import java.util.Hashtable;
import java.util.Vector;

/**
 * The make class.
 * After loading a makefile, instances of this class contain a set of
 * rules and patterns that maintain the consistency of a set of files.
 * The rules are invoked by supplying a list of targets to the make method.
 * <p>
 * The most important part of the make algorithm is coded in the make
 * method of a rule.
 * @see edu.neu.ccs.jmk.Rule#make()
 * @version November 1997
 * @author John D. Ramsdell
 */
public class Make
implements Operator
{
  public final static String version = "Make for Java version 1.4";

  private final static String defaultMakefileName = "makefile.jmk";

  /* * * * * *
   * Make rules and patterns
   * * * * * */

  // The rule directory
  // It maps a target as a string to a rule.
  private Hashtable dir = new Hashtable();

  Rule get(String name) {
    Rule rule = (Rule)dir.get(name);
    if (rule == null) {
      rule = new Rule(this, name);
      put(rule);
    }
    return rule;
  }

  private Rule put(Rule rule) {
    return (Rule)dir.put(rule.getTarget(), rule);
  }

  // The pattern list
  // The list is search by a rule which lacks commands.
  private Vector patterns = new Vector();

  /**
   * This method checks the patterns in a reverse order to see if one
   * is applicable to the given rule.
   * @return true if a pattern matched
   */
  boolean tryPatterns(Rule rule) {
    for (int i = patterns.size() - 1; i >= 0; i--) {
      Pattern pattern = (Pattern)patterns.elementAt(i);
      if (pattern.merge(rule))
	return true;
    }
    return false;
  }

  /**
   * Adds a pattern if either the target or a prerequisite contains
   * the pattern wild card, the percent sign.
   */
  private boolean maybeAddPattern(String target, String[] prerequisites,
				  Command[] commands, int lineNumber) {
    if (isPattern(target, prerequisites)) {
      patterns.addElement(new Pattern(target, prerequisites,
				      commands, lineNumber));
      return true;
    }
    else
      return false;
  }

  /**
   * Do rule parts suggest a pattern?
   */
  private static boolean isPattern(String target, String[] prerequisites) {
    if (Matcher.isPattern(target))
      return true;
    for (int i = 0; i < prerequisites.length; i++)
      if (Matcher.isPattern(prerequisites[i]))
	return true;
    return false;
  }

  /**
   * Is the target a special target?
   * The only special target currently supported is .PHONY.
   */
  private boolean maybeAddSpecialTarget(String target,
					String[] prerequisites) {
    if (target.equals(".PHONY")) {
      for (int i = 0; i < prerequisites.length; i++)
	get(prerequisites[i]).setPhony(true);
      return true;
    }
    else
      return false;
  }

  // The Default Target for a make without arguments.
  // It is set by addRule.
  private String defaultTarget = "default";

  private boolean hasSeenARule;

  /**
   * Add something that parses as a rule.  The means one could be adding
   * a pattern, a special target, or a makefile rule.
   */
  void addRule(String target, String[] prerequisites,
	       Command[] commands, int lineNumber) {
    if (maybeAddPattern(target, prerequisites, commands, lineNumber))
      return;
    if (maybeAddSpecialTarget(target, prerequisites))
      return;
    if (!hasSeenARule) {	// A real rule has been found
      defaultTarget = target;	// Set default target if not set
      hasSeenARule = true;
    }
    Rule rule = get(target);	// The get will create a rule if none exists
    Rule[] dependencies = new Rule[prerequisites.length];
    for (int i = 0; i < prerequisites.length; i++) // Replace names
      dependencies[i] = get(prerequisites[i]); // by rules
    rule.merge(dependencies, commands, lineNumber);
  }

  /* Various make system options are get and set here. */
     
  private PrintWriter out = new PrintWriter(System.out, true);

  /**
   * Get the print writer used for the make system output.
   * @return the make system print writer
   */
  public PrintWriter getOut() {
    return out;
  }

  /**
   * Set the print writer used for the make system output.
   * @param out the new make system print writer
   */
  public void setOut(PrintWriter out) {
    this.out = out;
  }

  private File file = new File(defaultMakefileName);

  /**
   * Get the file associated with this make object.
   * The makefile loader uses this file when called with no arguments.
   * @return the file
   */
  public File getFile() {
    return file;
  }

  /**
   * Store a file with the make object.
   * @param file the file, if null, store the default file
   */
  public void setFile(File file) {
    if (file == null)
      this.file = new File(defaultMakefileName);
    else
      this.file = file;
  }

  private boolean verbose = false;

  /**
   * Is verbose mode enabled?
   * In verbose mode, rules print more information when interpreted and
   * assignments are displayed.
   * @return true if verbose mode is enabled
   */
  public boolean isVerbose() {
    return verbose;
  }

  /**
   * Set verbose mode.
   */
  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }

  private boolean justPrinting = false;

  /**
   * Is just printing mode enabled?
   * In just printing mode, commands are printed but not run.
   * @return true if just printing mode is enabled
   */
  public boolean isJustPrinting() {
    return justPrinting;
  }

  /**
   * Set just printing mode.
   */
  public void setJustPrinting(boolean justPrinting) {
    this.justPrinting = justPrinting;
  }

  /**
   * Code for interrupting a make run.
   * The variable is volatile so that multithreaded applications need not
   * acquire a lock to use the shared variable.  The variables which hold
   * the state of the other modes need not be volatile as their changes
   * can be delayed until the object's lock status changes.
   *
   * The variable is static for recursive makes.
   */
  private static volatile boolean interrupt = false;

  /**
   * Should a make in progress be interrupted?
   * @return true if a make in progress should be interrupted
   */
  public boolean isInterruptEnabled() {
    return interrupt;
  }

  /**
   * Set the interrupt.
   * When true, a make in progress will be interrupted as soon as
   * it polls the interrupt status.
   * The interrupt is disabled when a make run starts and ends.
   */
  public void setInterruptEnabled(boolean interrupt) {
    this.interrupt = interrupt;
  }

  /**
   * This is the main entry point for starting a make run.
   * It invokes the make method of the appropriate rules, and then
   * resets any rule used during the make run.
   * @param targets a list of targets to be made
   * @exception CommandFailedException if make failed
   */
  public synchronized boolean make(String[] targets)
       throws CommandFailedException
  {
    interrupt = false;
    if (targets.length == 0)
      targets = new String[] { defaultTarget };
    try {
      boolean made = false;
      for (int i = 0; i < targets.length; i++)
	if (get(targets[i]).make())
	  made = true;
      return made;
    }
    finally {			// Make sure rules get reset even when
      for (int i = 0; i < targets.length; i++) // there is a failure
	get(targets[i]).reset();
      interrupt = false;
    }
  }

  /* * * * * *
   * The makefile loader and its associated parsing methods
   * are in the Loader class.
   * * * * * */

  /**
   * This method loads a makefile.  In addition to parsing the input, 
   * the value associated with each identifier through an assignment 
   * is substituted for the identifer, and all string functions are
   * executed.  The result is a make object that contains only rules
   * and patterns.
   *
   * <p>The makefile loader expects input to conform to the grammar
   * given below. Comments start with the sharp sign character
   * (<tt>#</tt>) and continue to the end of the line.  In the
   * grammar, syntactic categories begin with an uppercase letter.
   * The pattern Thing... indicates zero or more Things, and vertical
   * bar indicates alternatives.  In the grammar, The pattern
   * Thing... indicates zero or more Things, Thing,,, indicates one or
   * Things separated by commas, and vertical bar indicates
   * alternatives.
   *
   * <blockquote>
   * Makefile <tt>-&gt;</tt> Statement Statement...
   * <br>
   * Statement <tt>-&gt;</tt> Assignment | Rule | Inclusion | Conditional
   * <br>
   * Assignment <tt>-&gt;</tt> Identifier = List ;
   * <br>
   * List <tt>-&gt;</tt> Item...
   * <br>
   * Item <tt>-&gt;</tt> String | Identifier | (Item List,,,)
   * | { Block } 
   * | <tt>if</tt> List <tt>then</tt> Block <tt>else</tt> Block <tt>end</tt>
   * | <tt>function</tt> (Identifier,,,) Block <tt>end</tt>
   * | <tt>do</tt> Identifier (Initializer,,,) Block <tt>end</tt>
   * <br>
   * Block <tt>-&gt;</tt> Assignment... List
   * <br>
   * Initializer <tt>-&gt;</tt> Identifier = List
   * <br>
   * Rule <tt>-&gt;</tt> Item List : List ; Commands
   * <br>
   * Commands <tt>-&gt;</tt> | { Command... }
   * <br>
   * Command <tt>-&gt;</tt> Ignore Operator List ;
   * <br>
   * Ignore <tt>-&gt;</tt> | -
   * <br>
   * Operator <tt>-&gt;</tt> <tt>exec</tt> | <tt>delete</tt> | 
   * <tt>delall</tt> | <tt>mkdir</tt> | 
   * <tt>mkdirs</tt> | <tt>copy</tt> | <tt>rename</tt>
   * | <tt>create</tt> | <tt>note</tt> | <tt>forname</tt>
   * <br>
   * Inclusion <tt>-&gt;</tt> <tt>include</tt> List ;
   * <br>
   * Conditional <tt>-&gt;</tt> <tt>if</tt> List <tt>then</tt>
   * Statement... Alternative <tt>end</tt>
   * <br>
   * Alternative <tt>-&gt;</tt> | <tt>else</tt> Statement...
   * <br>
   * String <tt>-&gt;</tt> <tt>"</tt> Characters... <tt>"</tt>
   * <br>
   * Identifier <tt>-&gt;</tt> JavaIdentifier | <tt>@</tt> | <tt>&lt;</tt> | 
   * <tt>?</tt> | <tt>%</tt>
   * </blockquote>
   *
   * <p>The JavaIdentifiers that are reserved and not available as
   * Identifiers are <tt>include</tt>, <tt>if</tt>, <tt>then</tt>,
   * <tt>else</tt>, <tt>end</tt>, and <tt>function</tt>.
   *
   * @exception FileNotFoundException if an error occurs will opening the file
   * @exception ParseError if an error occurs during loading */
  public synchronized void load()
       throws FileNotFoundException, ParseError
  {
    load(new FileReader(getFile()));
  }

  /*
   * Loads a makefile from a reader.
   * @param in the source of the makefile
   * @exception ParseError if an error occurs during loading
   */
  public synchronized void load(Reader in)
       throws ParseError
  {
    dir = new Hashtable();
    patterns = new Vector();
    hasSeenARule = false;
    new Loader(this, in).load();
  }

  /* * * * * *
   * Make as an operator
   * * * * * */

  public String getName() {
    return "make";
  }

  /**
   * Execute the operation by running make after parsing the arguments.
   * @param args parameters to the operation
   * @param out place to write messages
   * @exception CommandFailedException if operation failed
   */
  public void exec(String[] args, PrintWriter out)
       throws CommandFailedException
  {
    boolean useWindow = false;
    boolean useSwing = false;
    setOut(out);
    // Process the switches
    if (args.length > 0 && args[0].startsWith("-")) {
      int i = 0;
    switches:			// Target for break when "--" found.
      for (; i < args.length; i++) {
	if (!args[i].startsWith("-")) // Not a switch
	  break;		// leave for loop
	if (args[i].length() != 2) {
	  out.println("Unrecognized switch: " + args[i]);
	  usage();
	  return;
	}
	switch (args[i].charAt(1)) {
	case 'v':
	  out.println(version);
	  return;
	case 'f':
	  i++;
	  if (i < args.length)
	    setFile(new File(args[i]));
	  else {
	    out.println("No makefile for -f option");
	    usage();
	    return;
	  }
	  break;
	case 'd':
	  setVerbose(true);
	  break;
	case 'n':
	  setJustPrinting(true);
	  break;
	case 'w':
	  useWindow = true;
	  break;
	case 's':
	  useSwing = true;
	  break;
	case '-':
	  i++;
	  break switches;
	default:
	  out.println("Unrecognized switch: " + args[i]);
	  usage();
	  return;
	}
      }
      Vector v = new Vector();	// Copy non-switch args into v
      for (; i < args.length; i++)
	v.addElement(args[i]);
      args = new String[v.size()];
      v.copyInto(args);		// then back to args
    }
    readAndMake(args, useWindow, useSwing);
  }

  private void usage()
       throws CommandFailedException
  {
    out.println("Usage: java " + Make.class.getName() +
		" [-f filename] [-d] [-n] [-v] [-w] [-s] filename ...");
    out.println("  -f use filename for makefile" +
		" (default is " + defaultMakefileName + ")");
    out.println("  -d print debugging information");
    out.println("  -n print but don't run commands");
    out.println("  -w use AWT window for make output");
    out.println("  -s use Swing window for make output");
    out.println("  -v print the version number");
    fail();
  }

  private void fail()
       throws CommandFailedException
  {
    throw new CommandFailedException("Make failed");
  }

  /**
   * Invoke make after command line processing has completed.
   * @param targets the list of targets for a make run
   * @param useWindow request make window when true
   * @param useSwing request make Swing window when true
   */
  public void readAndMake(String[] targets, 
			  boolean useWindow,
			  boolean useSwing)
       throws CommandFailedException
  {
    if (useWindow) {
      edu.neu.ccs.jmk.awt.MakeWindow.createMakeWindow(this, targets);
    }
    else if (useSwing) {
      try {			// Try using swing if possible
	edu.neu.ccs.jmk.swing.JMakeWindow.createMakeWindow(this, targets);
      }
      catch (LinkageError le) {
	edu.neu.ccs.jmk.awt.MakeWindow.createMakeWindow(this, targets);
      }
    }
    else {
      File file = getFile();
      Reader in;			// Open input to makefile
    
      if (file.getPath().equals("-"))
	in = new InputStreamReader(System.in);
      else {
	try {
	  in = new FileReader(file);
	}
	catch (FileNotFoundException ex) {
	  out.println("Cannot find makefile " + file.getPath());
	  fail();
	  return;
	}
      }

      try {
	load(in);	// Load makefile
      }
      catch (ParseError pe) {
	out.println(pe.getFileName() + ":" + pe.getLineNumber()
		    + ": Parse error: " + pe.getMessage());
	fail();
	return;
      }

      try {
	if (!make(targets)) // Run make
	  out.println("Nothing to make");
      }
      catch (CommandFailedException ex) {
	out.println("Command failed: " + ex.getMessage());
	fail();
	return;
      }
    }
  }

  /* * * * * *
   * The main class method
   * * * * * */

  /**
   * The main entry point for running Make.
   * Invoke Make with "-h" to see the program's usage message.
   */
  public static void main(String[] args) {
    PrintWriter out = new PrintWriter(System.out, true);
    Make make = new Make();
    try {
      make.exec(args, out);
    }
    catch (CommandFailedException ex) {
      System.exit(1);
    }
    catch (Throwable t) {
      System.err.println("Internal error: " + t.getMessage());
      t.printStackTrace();
      System.exit(1);
    }
  }
}
