// $Id: Loader.java,v 1.1.1.1 2000/09/26 11:20:34 ramsdell Exp $

// The loader class for reading and parsing makefiles

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

import java.io.*;
import java.util.Vector;

/**
 * The loader class for reading and parsing makefiles.
 * This class is rather large because it contains the 
 * complete makefile parser and most of an expression evaluator.
 * @see edu.neu.ccs.jmk.Make
 * @version May 1999
 * @author John D. Ramsdell
 */
final class Loader
{
  private Make make;
  private File file;	// For error messages
  private LineNumberReader in;
  private GlobalTable table;// Identifiers are looked up here.
  private PrintWriter out;
  private boolean verbose;

  Loader(Make make, Reader in) {
    this.make = make;
    out = make.getOut();
    verbose = make.isVerbose();
    file = make.getFile();
    this.in = new LineNumberReader(in);
    table = new GlobalTable();
  }

  private Loader(Make make,	// Used for processing an
		 File file,	// include directive
		 Reader in,
		 GlobalTable table) {
    this.make = make;
    out = make.getOut();
    verbose = make.isVerbose();
    this.file = file;
    this.in = new LineNumberReader(in);
    this.table = table;
  }

  // This flag is used to implement conditionals.
  private boolean conditionTrue = true;

  /*
   * Loads a makefile from a reader.
   * @param in the source of the makefile
   * @exception ParseError if an error occurs during loading
   */
  void load()
       throws ParseError
  {
    try {
      int token = scan();
      if (token == EOF_TOKEN) {
	String msg = "No input found";
	throw new ParseError(file, in.getLineNumber(), msg);
      }
      do {
	decl(token);
	token = scan();
      }
      while (token != EOF_TOKEN);
    }
    finally {			// Tidy up.
      try {
	in.close();
      }
      catch (IOException ex) {
      }
    }
  }
   
  // A declaration
  private void decl(int token) 
       throws ParseError
  {
    switch (token) {
    case IDENTIFIER_TOKEN:	// Could be a rule or an assignment
      Global var = table.get(string);
      int saw = peek();
      if (saw == '=') {
	drop();			// Clear out token
	assign(var);
      }
      else
	rule(append(var, null, null));
      break;
    case INCLUDE_TOKEN:
      include();
      break;
    case IF_TOKEN:
      conditional();
      break;
    default:
      if (tokenStartsItem(token))
	rule(list(token, null, null));
      else {
	String msg = "Expecting an item, "
	  + "an include, or a conditional, but found ";
	throw new ParseError(file, in.getLineNumber(),
			     msg + token2string(token));
      }
    }
  }

  // An assignment
  private void assign(Global var)
       throws ParseError
  {
    int token = scan();
    Expression exp;
    if (token == ';')
      exp = new Nil();
    else if (token == FUNCTION_TOKEN) {
      String name = var.getIdentifier();
      CompEnv env = new CompEnv(null, new String[] { name });
      exp = new Letrec(funct(name, env), new Lexical(name, 0, 0));
      token = scan();
    }
    else {
      exp = list(token, var.getIdentifier(), null);
      token = scan();
    }
    if (token != ';') 
      expecting(';', token);
    if (conditionTrue) {
      Value val;
      try {
	val = exp.eval(null, null);
      }
      catch (Exception ex) {
	String msg = "Exception: " + ex.getMessage();
	throw new ParseError(file, in.getLineNumber(), msg);
      }
      var.setValue(val);
      if (verbose) {
	out.print(var.getIdentifier() + " =");
  	showValue(val);
	out.println(";");
      }
    }
  }

  // Used to generate debugging output
  private void showValue(Value v) {
    if (StringList.isStringList(v))
      for (StringList sl = (StringList)v; sl != null; sl = sl.getRest()) {
	String term = unscanString(sl.getString());
	out.print(" " + term);
      }
    else if (v instanceof Function) {
      out.print(" [function");
      String name = ((Function)v).getName();
      if (name != null)
	out.print(" " + name);
      out.print("]");
    }
    else
      out.print(" " + v);
  }

  // Routines for parsing sequences of items

  // Parse list until end token is found
  private Expression listEnd(int token, String name, 
			     CompEnv env, int end) 
       throws ParseError
  {
    if (token == end)
      return new Nil();
    else {
      Expression exp = list(token, name, env);
      token = scan();
      if (token == end)
	return exp;
      else
	return expecting(end, token);
    }
  }

  // Parse list starting with token
  private Expression list(int token, String name, CompEnv env) 
       throws ParseError
  {
    return append(item(token, name, env), name, env);
  }

  // Parse list starting with an expression
  private Expression append(Expression exp, String name, CompEnv env) 
       throws ParseError
  {
    int token = peek();
    if (!tokenStartsItem(token))
      return exp;
    else {
      Vector v = new Vector();
      v.addElement(exp);
      do {
	drop();
	v.addElement(item(token, name, env));
	token = peek();
      }
      while (tokenStartsItem(token));
      Expression[] exps = new Expression[v.size()];
      v.copyInto(exps);
      return new Append(exps);
    }
  }    

  // Item parsing

  // Classify item
  private Expression item(int token, String name, CompEnv env) 
       throws ParseError
  {
    switch(token) {
    case STRING_TOKEN:
      return new Constant(string);
    case IDENTIFIER_TOKEN:
      return ident(string, env);
    case '(':
      return call(name, env);
    case '{':
      Expression exp = block(name, env);
      token = scan();
      if (token == '}')
	return exp;
      else
	return expecting('}', token);
    case IF_TOKEN:
      return cond(name, env);
    case FUNCTION_TOKEN:
      return funct(name, env);
    case DO_TOKEN:
      return loop(name, env);
    default:
      String msg = "Expecting an item, but found " + token2string(token);
      throw new ParseError(file, in.getLineNumber(), msg);
    }
  }  

  private boolean tokenStartsItem(int token) {
    switch(token) {
    case STRING_TOKEN:
    case IDENTIFIER_TOKEN:
    case '(':
    case '{':
    case IF_TOKEN:
    case FUNCTION_TOKEN:
    case DO_TOKEN:
      return true;
    default:
      return false;
    }
  }

  // Classify identifiers as global or lexical
  private Expression ident(String identifier, CompEnv env) {
    int up = 0;
    for (; env != null; env = env.parent) {
      for (int offset = 0; offset < env.rib.length; offset++)
	if (identifier == env.rib[offset])
	  return new Lexical(identifier, up, offset);
      up++;
    }
    return table.get(identifier);
  }

  // A function application
  private Expression call(String name, CompEnv env) 
       throws ParseError
  {
    Expression op = item(scan(), name, env);
    Vector v = new Vector();
    for (;;) {
      int token = scan();
      if (tokenStartsItem(token)) {
	v.addElement(list(token, name, env));
	token = scan();
      }
      else
	v.addElement(new Nil());
      if (token == ')') {
	Expression exps[] = new Expression[v.size()];
	v.copyInto(exps);
	return new Call(op, exps);
      }
      else if (token != ',') 
	return expecting(',', token);
    }
  }

  // A sequence of bindings followed by a list
  private Expression block(String name, CompEnv env) 
       throws ParseError
  {
    int token = peek();
    if (!tokenStartsItem(token))
      return new Nil();
    drop();
    if (token == IDENTIFIER_TOKEN) {
      String identifier = string;
      token = peek();
      if (token == '=') {
	drop();
	token = scan();
	CompEnv newenv = new CompEnv(env, new String[] { identifier });
	if (token == ';')
	  return new Let(new Nil(), block(name, newenv));
	else if (token == FUNCTION_TOKEN) {
	  Expression exp = funct(identifier, newenv);
	  token = scan();
	  if (token != ';') 
	    return expecting(';', token);
	  else
	    return new Letrec(exp, block(name, newenv));
	}
	else {
	  Expression exp = list(token, identifier, env);
	  token = scan();
	  if (token != ';') 
	    return expecting(';', token);
	  else
	    return new Let(exp, block(name, newenv));
	}
      }
      else
	return append(ident(identifier, env), name, env);
    }
    else
      return list(token, name, env);
  }
 
  // Conditional expressions
  private Expression cond(String name, CompEnv env) 
       throws ParseError
  {
    Expression exp = listEnd(scan(), name, env, THEN_TOKEN);
    Expression conseq = block(name, env);
    int token = scan();
    if (token != ELSE_TOKEN)
      return expecting(ELSE_TOKEN, token);
    Expression altern = block(name, env);
    token = scan();
    if (token == END_TOKEN)
      return new If(exp, conseq, altern);
    else
      return expecting(END_TOKEN, token);
  }

  // function constants
  private Expression funct(String name, CompEnv env) 
       throws ParseError
  {
    int token = scan();
    if (token != '(')
      return expecting('(', token);
    token = scan();
    if (token != IDENTIFIER_TOKEN)
      return expecting(IDENTIFIER_TOKEN, token);
    Vector v = new Vector();
    v.addElement(string);
    for (;;) {
      token = scan();
      if (token == ')') {
	String ids[] = new String[v.size()];
	v.copyInto(ids);
	CompEnv newenv = new CompEnv(env, ids);
	Expression body = block(name, newenv);
	token = scan();
	if (token != END_TOKEN)
	  return expecting(END_TOKEN, token);
	else
	  return new Lambda(name, ids.length, body);
      }
      else if (token != ',') 
	return expecting(',', token);
      token = scan();
      if (token != IDENTIFIER_TOKEN)
	return expecting(IDENTIFIER_TOKEN, token);
      v.addElement(string);
    }
  }
 
  // The DO loop
  private Expression loop(String name, CompEnv env) 
       throws ParseError
  {
    int token = scan();
    if (token != IDENTIFIER_TOKEN)
      return expecting(IDENTIFIER_TOKEN, token);
    String label = string;
    CompEnv callEnv = new CompEnv(env, new String[] { label });
    token = scan();
    if (token != '(')
      return expecting('(', token);
    Vector str = new Vector();
    Vector inits = new Vector();
    token = scan();
    if (token != IDENTIFIER_TOKEN)
      return expecting(IDENTIFIER_TOKEN, token);
    str.addElement(string);
    token = scan();
    if (token != '=')
      return expecting('=', token);
    token = scan();
    if (token == ')' || token == ',')
      inits.addElement(new Nil());
    else {
      inits.addElement(list(token, name, callEnv));
      token = scan();
    }
    for (;;) {
      if (token == ')') {
	String ids[] = new String[str.size()];
	str.copyInto(ids);
	CompEnv newenv = new CompEnv(callEnv, ids);
	Expression body = block(name, newenv);
	token = scan();
	if (token != END_TOKEN)
	  return expecting(END_TOKEN, token);
	Expression exp = new Lambda(label, ids.length, body);
	Expression exps[] = new Expression[inits.size()];
	inits.copyInto(exps);
	return new Letrec(exp, new Call(new Lexical(name, 0, 0), exps));
      }
      else if (token != ',') 
	return expecting(',', token);
      token = scan();
      if (token != IDENTIFIER_TOKEN)
	return expecting(IDENTIFIER_TOKEN, token);
      str.addElement(string);
      token = scan();
      if (token != '=')
	return expecting('=', token);
      token = scan();
      if (token == ')' || token == ',')
	inits.addElement(new Nil());
      else {
	inits.addElement(list(token, name, callEnv));
	token = scan();
      }
    }
  }

  private Expression expecting(int wanted, int got) 
       throws ParseError
  {
    String msg = "Expecting " + token2string(wanted) 
      + ", but found " + token2string(got);
    throw new ParseError(file, in.getLineNumber(), msg);
  }

  private StringList evalStringList(Expression exp) 
       throws ParseError
  {
    Value value;
    try {
      value = exp.eval(null, null);
    }
    catch (Exception ex) {
      String msg = "Exception: " + ex.getMessage();
      throw new ParseError(file, in.getLineNumber(), msg);
    }
    if (StringList.isStringList(value))
      return (StringList)value;
    else {
      String msg = "Expression did not evaluate to a string list";
      throw new ParseError(file, in.getLineNumber(), msg);
    }
  }

  // A rule
  private void rule(Expression exp)
       throws ParseError
  {
    int lineNumber = in.getLineNumber();
    StringList targets = evalStringList(exp);
    int token = scan();
    if (token != ':')
      expecting(':', token);
    String[] prerequisites = prerequisiteList();
    Command[] commands = commandList();

    if (conditionTrue)
      for (; targets != null; targets = targets.getRest())
	make.addRule(targets.getString(),
		     prerequisites,
		     commands,
		     lineNumber);
  }

  // Prerequisites
  private String[] prerequisiteList() 
       throws ParseError
  {
    Expression exp = listEnd(scan(), null, null, ';');
    StringList val = evalStringList(exp);
    return StringList.list2array(val);
  }    

  // Commands
  private Command[] commandList()
       throws ParseError
  {
    int token = peek();
    if (token != '{')
      return new Command[0];
    drop();
    Vector v = new Vector();	// A vector of commands
    for (;;) {
      token = scan();
      switch (token) {
      case '}':			// End of commands 
	Command[] cmds = new Command[v.size()];
	v.copyInto(cmds);
	return cmds;
      case IDENTIFIER_TOKEN:	// Next command
	v.addElement(commandItem(string));
	break;
      case '-':
	token = scan();
	if (token == IDENTIFIER_TOKEN) {
	  Command cmd = commandItem(string);
	  cmd.setIgnore(true);
	  v.addElement(cmd);
	  break;
	}
	// fall thru okay
      default:
	String msg = "Expecting an identifier, ";
	throw new ParseError(file, in.getLineNumber(),
			     msg + token2string(token));
      }
    }
  }

  // Various operators available for selection.

  private final static ExecOperator exec = new ExecOperator();

  private final static FileOperator delete = new FileOperator() {
    boolean exec(File arg, PrintWriter out) {
      return arg.delete();
    }
    public String getName() {
      return "delete";
    }
  };

  // recursive delete
  private final static FileOperator delall = new FileOperator() {
    boolean exec(File arg, PrintWriter out) {
      if (arg.isDirectory()) {
	String[] file = arg.list();
	for (int i = 0; i < file.length; i++) {
	  File f = new File(arg, file[i]);
	  if (!exec(f, out))
	    return false;
	}
      }
      return arg.delete();
    }
    public String getName() {
      return "delall";
    }
  };

  private final static FileOperator mkdir = new FileOperator() {
    boolean exec(File arg, PrintWriter out) {
      return arg.mkdir();
    }
    public String getName() {
      return "mkdir";
    }
  };

  private final static FileOperator mkdirs = new FileOperator() {
    boolean exec(File arg, PrintWriter out) {
      return arg.mkdirs();
    }
    public String getName() {
      return "mkdirs";
    }
  };

  private final static BinaryFileOperator rename = new BinaryFileOperator() {
    boolean exec(File arg1, File arg2, PrintWriter out) {
      if (arg1.equals(arg2))
	return false;
      return arg1.renameTo(arg2);
    }
    public String getName() {
      return "rename";
    }
  };

  private final static BinaryFileOperator copy = new BinaryFileOperator() {
    boolean exec(File arg1, File arg2, PrintWriter out) {
      if (arg1.equals(arg2))
	return false;
      try {
	int bufferSize = 8192;
	byte[] buffer = new byte[bufferSize];
	FileInputStream src = new FileInputStream(arg1);
	FileOutputStream dst = new FileOutputStream(arg2);
	for (;;) {
	  int n = src.read(buffer);
	  if (n == -1) {
	    src.close();
	    dst.close();
	    return true;
	  }
	  dst.write(buffer, 0, n);
	}
      }
      catch (FileNotFoundException ex) {
	out.println("Cannot open " + ex.getMessage());
	return false;
      }
      catch (IOException ex) {
	out.println(ex.toString());
	return false;
      }
    }
    public String getName() {
      return "copy";
    }
  };

  private final static CreateOperator create = new CreateOperator();

  private final static NoteOperator note = new NoteOperator();

  private final static ClassOperator classOperator = new ClassOperator();

  // A single command
  private Command commandItem(String operatorName)
       throws ParseError
  {
    Operator operator;
    if (operatorName.equals(exec.getName()))
      operator = exec;
    else if (operatorName.equals(delete.getName()))
      operator = delete;
    else if (operatorName.equals(delall.getName()))
      operator = delall;
    else if (operatorName.equals(mkdir.getName()))
      operator = mkdir;
    else if (operatorName.equals(mkdirs.getName()))
      operator = mkdirs;
    else if (operatorName.equals(rename.getName()))
      operator = rename;
    else if (operatorName.equals(copy.getName()))
      operator = copy;
    else if (operatorName.equals(create.getName()))
      operator = create;
    else if (operatorName.equals(note.getName()))
      operator = note;
    else if (operatorName.equals(classOperator.getName()))
      operator = classOperator;
    else {
      String msg = "Unrecognized command operator: " + operatorName;
      throw new ParseError(file, in.getLineNumber(), msg);
    }
    // Construct function that produces the operands
    CompEnv commandEnv = new CompEnv(null, Command.commandArgs);
    Expression exp = listEnd(scan(), null, commandEnv, ';');
    Function fun = new Closure(null, Command.commandArgs.length, exp, null);
    return new Command(operator, fun);
  }

  // Inclusion of the makefiles given by a list of strings.
  private void include()
       throws ParseError
  {
    Expression exp = listEnd(scan(), null, null, ';');
    StringList val = evalStringList(exp);
    if (conditionTrue) {
      if (verbose) {
	out.print(INCLUDE_WORD);
	showValue(val);
	out.println(";");
      }
      String parent = file.getParent();	// Used for relative file names
      for (; val != null; val = val.getRest()) {
	String incName = StringUtils.localizePaths(val.getString());
	File incFile = new File(incName);
	String incDir = null;
	if (parent != null && !incFile.isAbsolute()) {
	  incDir = parent;
	  incFile = new File(incDir, incName);
	}
	try {
	  FileReader incIn = new FileReader(incFile);
	  new Loader(make, incFile, incIn, table).load();
	}
	catch (FileNotFoundException ex) {
	  String msg ="Cannot find include file " + incName;
	  if (incDir != null)
	    msg += " in directory " + incDir;
	  throw new ParseError(file, in.getLineNumber(), msg);
	}
      }
    }
  }

  // Conditional makefile reading.
  // This sets the instance variable conditionTrue.
  private void conditional()
       throws ParseError
  {
    boolean changeSense = conditionTrue;
    boolean elseSeen = false;
    Expression exp = listEnd(scan(), null, null, THEN_TOKEN);
    StringList val = evalStringList(exp);
    String msg;
    if (verbose && changeSense) {
      out.print(IF_WORD);
      showValue(val);
      out.println(" " + THEN_WORD);
    }
    if (changeSense)
      conditionTrue = val != null; // Make the test
    for (;;) { 
      int token = scan();
      switch (token) {
      case EOF_TOKEN:
	if (elseSeen)
	  msg = "Expecting " + END_WORD + ", ";
	else
	  msg = "Expecting " + END_WORD + " or " + ELSE_WORD + ", ";
	throw new ParseError(file, in.getLineNumber(),
			     msg + token2string(token));
      case END_TOKEN:		// Found conditional closer
	conditionTrue = changeSense;
	if (verbose && changeSense)
	  out.println(END_WORD);
	return;
      case ELSE_TOKEN:		// Found alternative
	if (elseSeen) {
	  msg = "Expecting " + END_WORD + ", ";
	  throw new ParseError(file, in.getLineNumber(),
			       msg + token2string(token));
	}
	elseSeen = true;
	if (verbose && changeSense)
	  out.println(ELSE_WORD);
	if (changeSense)
	  conditionTrue = !conditionTrue;
	break;
      default:
	decl(token);		// Found content
      }
    }
  }

  // For error messages
  private String token2string(int token) {
    switch (token) {
    case EOF_TOKEN:
      return "[EOF]";
    case STRING_TOKEN:
      return "string " + unscanString(string);
    case IDENTIFIER_TOKEN:
      return "identifier " + string;
    case INCLUDE_TOKEN:
      return "the reserved word " + INCLUDE_WORD;
    case IF_TOKEN:
      return "the reserved word " + IF_WORD;
    case THEN_TOKEN:
      return "the reserved word " + THEN_WORD;
    case ELSE_TOKEN:
      return "the reserved word " + ELSE_WORD;
    case END_TOKEN:
      return "the reserved word " + END_WORD;
    case FUNCTION_TOKEN:
      return "the reserved word " + FUNCTION_WORD;
    case DO_TOKEN:
      return "the reserved word " + DO_WORD;
    default:			// must be a character
      return "'" + (char)token + "'";
    }
  }

  private static String unscanString(String s) {
    if (s.indexOf('\\') < 0 && s.indexOf('"') < 0)
      return "\"" + s + "\"";
    // This adds in the escape character.
    StringBuffer sb = new StringBuffer();
    sb.append('"');
    for (int i = 0; i < s.length(); i++) {
      char ch = s.charAt(i);
      switch (ch) {
      case '\\':
      case '"':
	sb.append('\\');
	// fall thru okay
      default:
	sb.append(ch);
      }
    }
    sb.append('"');
    return sb.toString();
  }

  /* * * * * *
   * The Makefile lexical analyzer
   * * * * * */

  // This uses a common implementation method for lexical analyzers
  // written in C like languages.  Tokens are ints, and you can
  // guess rest.

  // Character tokens are the character values.
  private final static int EOF_TOKEN = -1;
  private final static int STRING_TOKEN = -2;
  private final static int IDENTIFIER_TOKEN = -3;
  private final static int INCLUDE_TOKEN = -4;
  private final static int IF_TOKEN = -5;
  private final static int THEN_TOKEN = -6;
  private final static int ELSE_TOKEN = -7;
  private final static int END_TOKEN = -8;
  private final static int FUNCTION_TOKEN = -9;
  private final static int DO_TOKEN = -10;

  // Reserved words
  private final static String INCLUDE_WORD = "include";
  private final static String IF_WORD = "if";
  private final static String THEN_WORD = "then";
  private final static String ELSE_WORD = "else";
  private final static String END_WORD = "end";
  private final static String FUNCTION_WORD = "function";
  private final static String DO_WORD = "do";

  // Token values are strings
  // When string is an identifier, it must be interned!
  private String string;

  private int savedToken;
  private boolean isTokenSaved = false;

  /**
   * Returns a token without removing it from the input stream of tokens.
   * @return first token
   * @exception ParseError if scan throws one
   */
  private int peek() 
       throws ParseError
  {
    if (!isTokenSaved) {
      savedToken = scan();
      isTokenSaved = true;
    }
    return savedToken;
  }

  /**
   * Drops a saved token.
   */
  private void drop() {
    isTokenSaved = false;
  }

  // Characters can be pushed back into the input stream of chars.
  // This happens after reading an identifier.
  private boolean isCharPushedBack = false;
  private int pushedBackChar;	

  /**
   * Returns a token after removing it from the input stream of tokens.
   * @return first token
   * @exception ParseError if syntax error occurs
   */
  private int scan()
       throws ParseError
  {
    try {
      if (isTokenSaved) {	// Token was saved
	isTokenSaved = false;	// because someone 
	return savedToken;	// was peeking.
      }      
      
      int ch;
      if (isCharPushedBack) {	// Char pushed back after
	ch = pushedBackChar;	// reading an identifier.
	isCharPushedBack = false; // Use it instead of reading.
      }
      else
	ch = in.read();
      
      for (;;) {		// Skip leading spaces
	if (ch == -1)
	  return EOF_TOKEN;
	if (!Character.isWhitespace((char)ch))
	  break;
	ch = in.read();
      }

      switch (ch) {
      case ',':
      case ';':
      case ':':
      case '-':
      case '=':
      case '(':
      case ')':
      case '{':
      case '}':
	return ch;
      case '@':
	string = "@";
	return IDENTIFIER_TOKEN;
      case '<':
	string = "<";
	return IDENTIFIER_TOKEN;
      case '?':
	string = "?";
	return IDENTIFIER_TOKEN;
      case '%':
	string = "%";
	return IDENTIFIER_TOKEN;
      case '#':			// Found comment character
	if (in.readLine() == null) // Ignore rest of line
	  return EOF_TOKEN;
	else 
	  return scan();	// Try again...
      case '"':			// Found a string
	return readString();
      default:			// It must be an identifier.
	if (!Character.isJavaIdentifierStart((char)ch))
	  throw new ParseError(file, in.getLineNumber(),
			       "Unrecognized character " + (char)ch);
	return readIdentifier(ch);
      }
    }
    catch (IOException ex) {
      String msg = "I/O exception: " + ex.getMessage();
      throw new ParseError(file, in.getLineNumber(), msg);
    }
  }

  private int readString() 
       throws IOException, ParseError
  {
    StringBuffer sb = new StringBuffer();
    for (;;) {
      int ch = in.read();
      switch (ch) {
      case -1:
	throw new ParseError(file, in.getLineNumber(), 
			     "EOF in string");
      case '"':
	string = sb.toString();
	return STRING_TOKEN;
      case '\n':
	throw new ParseError(file, in.getLineNumber(),
			     "newline in string");
				 
      case '\\':		// Escaped string character
	ch = in.read();	// Only " and \ can be escaped.
	switch (ch) {
	case '"':
	  break;		// Add to string
	case '\\':
	  break;
	case -1:		// Error cases follow
	  throw new ParseError(file, in.getLineNumber(),
			       "EOF in string");
	case '\n':
	  throw new ParseError(file, in.getLineNumber(),
			       "newline in string");
	default:
	  throw new ParseError(file, in.getLineNumber(),
			       "Cannot escape " + (char)ch);
	}
	// Fall thru okay
      default:
	sb.append((char)ch);
      }
    }
  }

  private int readIdentifier(int ch) 
       throws IOException
  {
    StringBuffer ib = new StringBuffer();
    ib.append((char)ch);
    for (;;) {
      ch = in.read();
      if (ch != -1 && Character.isJavaIdentifierPart((char)ch))
	ib.append((char)ch);
      else {
	isCharPushedBack = true;
	pushedBackChar = ch; // Intern identifiers
	string = ib.toString().intern();
	if (string == INCLUDE_WORD) // Identify reserved words
	  return INCLUDE_TOKEN;
	else if (string == IF_WORD)
	  return IF_TOKEN;
	else if (string == THEN_WORD)
	  return THEN_TOKEN;
	else if (string == ELSE_WORD)
	  return ELSE_TOKEN;
	else if (string == END_WORD)
	  return END_TOKEN;
	else if (string == FUNCTION_WORD)
	  return FUNCTION_TOKEN;
	else if (string == DO_WORD)
	  return DO_TOKEN;
	else
	  return IDENTIFIER_TOKEN;
      }
    } 
  }

  /* * * * * *
   * The Makefile expression evaluator
   * * * * * */

  // This implementation is much more efficient on Java VMs
  // that are tail recursive.
  
  // Compile time environments 
  // Used only during parsing
  private static class CompEnv
  {
    final CompEnv parent;
    final String[] rib;

    CompEnv(CompEnv parent, String[] rib) {
      this.parent = parent;
      this.rib = rib;
    }
  }

  /**
   * Expressions other than Globals.
   */
    
  // A string constant
  private static class Constant
  extends Expression
  {
    private final String string;

    Constant(String string) {
      this.string = string;
    }

    Value eval(Environment env, StringList list) {
      return new StringList(string, list);
    }
  }

  // A reference to a lexical variable
  private static class Lexical
  extends Expression
  {
    private final String identifier;
    private final int up;
    private final int offset;

    Lexical (String identifier, int up, int offset) {
      this.identifier = identifier;
      this.up = up;
      this.offset = offset;
    }

    Value eval(Environment env, StringList list)
	 throws StringListCastException
    {
      Value value = Environment.lookup(env, up, offset);
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
  }

  // A function application
  private static class Call
  extends Expression
  {
    private final Expression operator;
    private final Expression[] operands;

    Call(Expression operator, Expression[] operands) {
      this.operator = operator;
      this.operands = operands;
    }

    Value eval(Environment env, StringList list)
	 throws Exception
    {
      int nargs = operands.length;
      Value[] args = new Value[nargs];
      for (int i = 0; i < nargs; i++) 
	args[i] = operands[i].eval(env, null);
      Value op = operator.eval(env, null);
      if (op instanceof Function) 
	return ((Function)op).invoke(args, list);
      else {
	String msg = "Call error: operator not a function";
	throw new FunctionCastException(msg);
      }
    }
  }    

  // A local binding of a variable
  private static class Let
  extends Expression
  {
    private final Expression exp;
    private final Expression body;

    Let(Expression exp, Expression body) {
      this.exp = exp;
      this.body = body;
    }

    Value eval(Environment env, StringList list)
	 throws Exception
    {
      Value value = exp.eval(env, null);
      Value[] rib = new Value[] { value };
      Environment newenv = new Environment(env, rib);
      return body.eval(newenv, list);
    }
  }

  // A recursive binding of a variable
  private static class Letrec
  extends Expression
  {
    private final Expression exp;
    private final Expression body;

    Letrec(Expression exp, Expression body) {
      this.exp = exp;
      this.body = body;
    }

    Value eval(Environment env, StringList list)
	 throws Exception
    {
      Value[] rib = new Value[1];
      Environment newenv = new Environment(env, rib);
      rib[0] = exp.eval(newenv, null);
      return body.eval(newenv, list);
    }
  }

  // A conditional
  private static class If
  extends Expression
  {
    private final Expression condition;
    private final Expression consequent;
    private final Expression alternative;

    If(Expression condition, Expression consequent, Expression alternative) {
      this.condition = condition;
      this.consequent = consequent;
      this.alternative = alternative;
    }

    Value eval(Environment env, StringList list)
	 throws Exception
    {
      Value cond = condition.eval(env, null);
      if (StringList.isStringList(cond)) {
	if (cond != null)
	  return consequent.eval(env, list);
	else
	  return alternative.eval(env, list);
      }
      else {
	String msg = "If error: the condition is not a string list";
	throw new StringListCastException(msg);
      }
    }
  }

  // An expression that produces a function
  private static class Lambda
  extends Expression
  {
    private final String name;
    private final int nargs;
    private final Expression body;

    Lambda(String name, int nargs, Expression body) {
      this.name = name;
      this.nargs = nargs;
      this.body = body;
    }

    Value eval(Environment env, StringList list)
	 throws StringListCastException
    {
      if (list == null) 
	return new Closure(name, nargs, body, env);
      else {
	String msg = 
	  "Append error: a function cannot be appended to a string list";
	throw new StringListCastException(msg);
      }
    }
  }

  // The function produced by a Lambda expression
  private static class Closure
  implements Function
  {
    private final String name;
    private final int nargs;
    private final Expression body;
    private final Environment env;

    Closure(String name, int nargs, Expression body, Environment env) {
      this.name = name;
      this.nargs = nargs;
      this.body = body;
      this.env = env;
    }

    public String getName() {
      return name;
    }

    public Value invoke(Value[] args, StringList list)
	 throws Exception
    {
      if (nargs == args.length) {
	Environment newenv = new Environment(env, args);
	return body.eval(newenv, list);
      }
      else {
	String msg = "Arg count error: expecting " + nargs +
	  " but got " + args.length + " arguments";
	throw new WrongArgCountException(msg);
      }
    }
  }

  // The expression for the empty list
  private static class Nil
  extends Expression
  {
    Nil() {
    }

    Value eval(Environment env, StringList list)
    {
      return list;
    }
  }

  // The expression that appends the values produced by multiple expressions
  private static class Append
  extends Expression
  {
    private final Expression[] exps;

    Append(Expression[] exps) {
      this.exps = exps;
    }

    Value eval(Environment env, StringList list)
	 throws Exception
    {
      int nexps = exps.length;
      switch (nexps) {
      case 0:
	return list;
      case 1:
	return exps[0].eval(env, list);
      default:			// Must check every result
	for (nexps--; nexps >= 0; nexps--) {
	  Value value = exps[nexps].eval(env, list);
	  if (StringList.isStringList(value)) 
	    list = (StringList)value;
	  else {
	    String msg = "Append error: a value in a list was not a string";
	    throw new StringListCastException(msg);
	  }
	}
	return list;
      }
    }
  }
}
