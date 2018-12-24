// $Id: MakeWindow.java,v 1.1.1.1 2000/09/26 11:20:35 ramsdell Exp $

// The make window class

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

package edu.neu.ccs.jmk.awt;

import java.io.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.util.*;
import edu.neu.ccs.jmk.*;

/**
 * A window for displaying the results of running Make.
 * @version November 1997
 * @author John D. Ramsdell
 */
public class MakeWindow
extends TextArea
implements ActionListener, ClipboardOwner
{
  private final static String JMK_PROPERTIES = "jmk";
  private final static String DEFAULT_FONT = "monoSpaced";
  private final static int DEFAULT_COLUMNS = 72;
  private final static int DEFAULT_ROWS = 22;

  private final static int MAX_TEXT_AREA = 16384;
  private final static int TRUNCATED_TEXT_AREA = MAX_TEXT_AREA / 2;
  private final String nl = System.getProperty("line.separator");
  // private final String nl = "\n";  // Which is right?

  private Make make;
  private String[] targets;
  private final RunQueue tasks = new RunQueue();

  /**
   * Create a make window.
   * @param make the make object
   * @param targets the initial set of targets for make
   * @param rows the number of rows
   * @param columns the number of columns
   */
  MakeWindow(Make make, String[] targets,
	     int rows, int columns) {
    this.make = make;
    this.targets = targets;
    setRows(rows);
    setColumns(columns);
    // Create make writer
    make.setOut(new PrintWriter(new MakeWriter(), true));
    new Thread(tasks).start();
  }

  /**
   * This routine appends a line to the panel without breaking long lines.
   * This routine should only be called by appendLongLine.
   * @param line the line to be appended
   */
  void appendLine(String s) {
    append(s + nl);
    setCaretPosition(Integer.MAX_VALUE);
    String area = getText();
    int n = area.length();	// Truncate saved text if needed
    if (n > MAX_TEXT_AREA) {
      int truncSize = area.indexOf('\n', n - TRUNCATED_TEXT_AREA);
      area = area.substring(truncSize);
      setText(area);
    }
  }

  /**
   * This routine breaks long lines as they are added to the panel.
   * This routine should only be called by a MakeWriter.
   * @param line the line to be broken
   */
  void appendLongLine(String line) {
    int columns = getColumns() - 5;
    String prefix = "";		// String to add at start of line
    int cc = 0;			// Current column
    int ls = 0;			// Index of the last space
    int s = 0;			// Start index
    int n = line.length();	// End index
    for (int i = 0; i < n; i++) {
      if (line.charAt(i) == '\n') {
	cc = 0;
	ls = i + 1;		// Advance ls to this line
      }
      else if (Character.isSpaceChar(line.charAt(i))) {
	ls = i;
	cc++;
      }
      else if (cc >= columns && Character.isSpaceChar(line.charAt(ls))) {
	appendLine(prefix + line.substring(s, ls) + " \\");
	cc = 0;			// Break a line
	i = ls;			// Reset line index here!
	ls = i + 1;		// Advance ls to this line
	s = ls;			// New start
	prefix = "  ";		// Set continuation prefix
      }
      else
	cc++;
    }
    if (s < n) {		// Insert the remainder
      appendLine(prefix + line.substring(s, n));
    }
  }

  class MakeWriter 
  extends Writer
  {
    StringBuffer buffer = new StringBuffer(DEFAULT_COLUMNS);
    boolean eol = false;
    boolean closed = false;
    
    public void write (char cbuf[], int off, int len) 
	 throws IOException
    {
      if (closed)
	throw new IOException("Write of a closed writer");
      for (int i = off; i < len; i++) {
	char ch = cbuf[i];
	if (eol) {
	  eol = false;
	  if (ch == '\n')
	    continue;
	}
	if (ch == '\n' || ch == '\r') {
	  if (ch == '\r') 
	    eol = true;
	  appendLongLine(buffer.toString());
	  buffer.setLength(0);
	}
	else {
	  buffer.append(ch);
	}
      }
    }

    public void flush() 
	 throws IOException
    {
      if (closed)
	throw new IOException("Flush of a closed writer");
    }

    public void close() 
	 throws IOException
    {
      if (!closed) {
	flush();
	closed = true;
      }
    }
  }

  private String cachedTargetString;

  /**
   * Handle input from the input window.
   * Sets the target list and then starts a make.
   * @see edu.neu.ccs.jmk.awt.MakeInput
   */
  public void actionPerformed(ActionEvent e) {
    String targetString = e.getActionCommand().trim();
    if (!cachedTargetString.equals(targetString)) {
      cachedTargetString = targetString;
      targets = string2String_array(targetString);
    }
    make();
  }

  /**
   * Break a string into an array of strings.
   * A space character marks a breaking point.
   */
  private static String[] string2String_array(String str) {
    Vector v = new Vector();
    int s = 0;                    // s is the start index

    while (s < str.length()) {
      if (str.charAt(s) == ' ')
	s++;
      else {
	int e = str.indexOf(' ', s); // e is the end index
	if (e < 0) {
	  v.addElement(str.substring(s));
	  break;
	}
	v.addElement(str.substring(s, e));
	s = e + 1;
      }
    }
      
    String[] str_array = new String[v.size()];
    v.copyInto(str_array);
    return str_array;
  }

  /**
   * Get the current target list as a string.
   */
  private String getTargetString() {
    if (cachedTargetString == null) {
      if (targets.length > 0) {
	cachedTargetString = targets[0];
	for (int i = 1; i < targets.length; i++)
	  cachedTargetString += " " + targets[i];
      }
      else 
	cachedTargetString = "";
    }
    return cachedTargetString;
  }

  /**
   * Run make and print any error messages.
   */
  private void makeTask(String[] args, String targetString) {
    try {
      if (!make.make(args))
	make.getOut().println("Nothing to make");
    }
    catch (CommandFailedException ex) {
      make.getOut().println("Command failed: " + ex.getMessage());
    }
    if (targetString.equals(""))
      targetString = "Make";
    else
      targetString = "Made " + targetString;
    make.getOut().println(targetString + " at " + now());
  }

  private DateFormat formatter;

  private String now() {
    if (formatter == null) {
      formatter = DateFormat.getTimeInstance();
      formatter.setTimeZone(TimeZone.getDefault());
    }
    return formatter.format(new Date());
  }

  /**
   * Enqueue a make task.
   */
  private void make() {
    final String targetString = getTargetString();
    final String[] args = targets;
    tasks.add(new Runnable() {
      public void run() {
	makeTask(args, targetString);
      }
    });
  }

  /**
   * Load the makefile given in the make object
   * and print any error messages.
   */
  private void loadTask() {
    String fileName = make.getFile().getPath();
    try {
      make.load();
      make.getOut().println("Loaded " + fileName + " at " + now());
    }
    catch (FileNotFoundException ex) {
      make.getOut().println("Cannot find makefile " + fileName);
    }
    catch (ParseError pe) {
      make.getOut().println(pe.getFileName() + ":" + pe.getLineNumber()
			    + ": Parse error: " + pe.getMessage());
    }
  }

  /**
   * Enqueue a load task.
   */
  private void load() {
    tasks.add(new Runnable() {
      public void run() {
	loadTask();
      }
    });
  }

  /**
   * Cancel queued tasks.
   */
  private void cancel() {
    tasks.removeAll();
    make.setInterruptEnabled(true);
    make.getOut().println("Cancel requested");
  }

  /**
   * Creates the make frame and all the windows that it contains.
   */
  public static void createMakeWindow(Make make, String[] targets) {
    loadResources();
    Frame f = new Frame(Make.version);
    f.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
	System.exit(0);
      }
    });
    f.setFont(Font.decode(getFontResource()));

    Label label = new Label(makefileLabel(make));
    f.add(label, "North");
    MakeWindow mw = new MakeWindow(make, targets,
				   getRowsResource(),
				   getColumnsResource());
    mw.setEditable(false);
    f.add(mw, "Center");
    f.setMenuBar(createMenuBar(mw, make, f, label));
    Panel panel = new Panel(new BorderLayout());
    Label targetLabel = new Label(" Targets:");
    MakeInput field = new MakeInput(mw.getTargetString());
    field.addActionListener(mw);
    panel.add(targetLabel, "West");
    panel.add(field, "Center");
    f.add(panel, "South");
    label.addFocusListener(field);
    mw.addFocusListener(field);
    panel.addFocusListener(field);
    targetLabel.addFocusListener(field);

    f.pack();
    f.show();
    field.requestFocus();
    mw.load();
  }

  private static String makefileLabel(Make make) {
    return " Makefile: " + make.getFile().getPath();
  }

  private static MenuBar createMenuBar(final MakeWindow mw,
				       final Make make,
				       final Frame frame,
				       final Label label) {
    MenuBar mb = new MenuBar();
    Menu m = new Menu("File");
    mb.add(m);
    MenuItem mi;
    mi = new MenuItem("Reload", new MenuShortcut(KeyEvent.VK_R));
    mi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	mw.load();
      }
    });
    m.add(mi);
    mi = new MenuItem("File load", new MenuShortcut(KeyEvent.VK_F));
    mi.addActionListener(makefileDialog(mw, make, frame, label));
    m.add(mi);
    mi = new MenuItem("Exit", new MenuShortcut(KeyEvent.VK_X));
    mi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	System.exit(0);
      }
    });
    m.add(mi);
    m = new Menu("Commands");
    mb.add(m);
    // Changed the short cut from VK_M to VK_A because
    // VK_M invoked make twice.
    mi = new MenuItem("Make", new MenuShortcut(KeyEvent.VK_A));
    mi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	mw.make();
      }
    });
    m.add(mi);
    mi = new MenuItem("Cancel", new MenuShortcut(KeyEvent.VK_C));
    mi.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	mw.cancel();
      }
    });
    m.add(mi);
    mi = new MenuItem("To Clipboard", new MenuShortcut(KeyEvent.VK_T));
    mi.addActionListener(copyTranscriptAction(mw));
    m.add(mi);
    m = new Menu("Options");
    mb.add(m);
    CheckboxMenuItem cmi = new CheckboxMenuItem("Verbose", make.isVerbose());
    cmi.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
	make.setVerbose(e.getStateChange() == ItemEvent.SELECTED);
      }
    });
    m.add(cmi);
    cmi = new CheckboxMenuItem("Just Print", make.isJustPrinting());
    cmi.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
	make.setJustPrinting(e.getStateChange() == ItemEvent.SELECTED);
      }
    });
    m.add(cmi);
    return mb;
  }

  private static ActionListener makefileDialog(final MakeWindow mw,
					       final Make make,
					       final Frame frame,
					       final Label label) {
    return new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	FileDialog fd = new FileDialog(frame, "Select makefile name");
	fd.setFile(make.getFile().getPath());
	fd.show();
	String makefile = fd.getFile();
	if (makefile != null) {
	  makefile = fd.getDirectory() + makefile;
	  make.setFile(new File(makefile));
	  label.setText(makefileLabel(make));
	  mw.load();
	}
      }
    };
  }

  private static ActionListener copyTranscriptAction(final MakeWindow mw) {
    final Clipboard clipboard = mw.getToolkit().getSystemClipboard();
    return new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	String srcData;
	if (mw.getSelectionStart() < mw.getSelectionEnd())
	  srcData = mw.getSelectedText();
	else {
	  mw.selectAll();
	  srcData = mw.getText();
	}
	if (srcData != null) {
	  StringSelection contents = new StringSelection(srcData);
	  clipboard.setContents(contents, mw);
	}
      }
    };
  }

  /**
   * Action on loss of clipboard.
   */
  public void lostOwnership(Clipboard clipboard, Transferable contents) {
    select(0, 0);		// Deselect all text
  }

 /**
   * Resource functions.
   */

  private static ResourceBundle resources;

  /**
   * load resources
   */
  private static void loadResources() {
    if (resources == null) {
      try {
	resources = ResourceBundle.getBundle(JMK_PROPERTIES);
      }
      catch (MissingResourceException mre) {
	// Ignore errors.  Defaults will be used for missing properties.
      }
    }
  }

  /**
   * Get a string from a ResourceBundle.
   * @param nm name of key
   * @return resource as a string or defaultValue on error
   */
  private static String getResourceString(String nm, String defaultValue) {
    if (resources == null)
      return defaultValue;
    try {
      return resources.getString(nm);
    }
    catch (MissingResourceException mre) {
      return defaultValue;
    }
  }

  /**
   * Get an integer from a ResourceBundle.
   * @param nm name of key
   * @param defaultValue value on error
   * @return resource as an integer or default value on error
   */
  private static int getResourceInt(String nm, int defaultValue) {
    if (resources == null)
      return defaultValue;
    String str;
    try {
      str = resources.getString(nm);
    }
    catch (MissingResourceException mre) {
      return defaultValue;
    }
    if (str == null)
      return defaultValue;
    try {
      return Integer.parseInt(str);
    }
    catch (NumberFormatException ex) {
      return defaultValue;
    }
  }

  private static String getFontResource() {
    return getResourceString("jmk.font", DEFAULT_FONT);
  }

  private static int getColumnsResource() {
    return getResourceInt("jmk.columns", DEFAULT_COLUMNS);
  }

  private static int getRowsResource() {
    return getResourceInt("jmk.rows", DEFAULT_ROWS);
  }

  /**
   * A main entry point for running Make in a window.
   * The first arg is the name of the makefile
   * and the remaining args are the initial targets.
   */
  public static void main(String[] args) {
    try {
      Make make = new Make();
      if (args.length > 0) {
	String[] targets = new String[args.length - 1];
	for (int i = 0; i < targets.length; i++)
	  targets[i] = args[i + 1];
	make.setFile(new File(args[0]));
	MakeWindow.createMakeWindow(make, targets);
      }
      else {
	System.out.println("Usage: java " + MakeWindow.class.getName() +
			   " makefile [ target ]*");
	System.exit(1);
      }
    }
    catch (Throwable t) {
      System.err.println("Internal error: " + t.getMessage());
      t.printStackTrace();
      System.exit(1);
    }
  }
}
