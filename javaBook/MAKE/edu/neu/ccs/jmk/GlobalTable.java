// $Id: GlobalTable.java,v 1.1.1.1 2000/09/26 11:20:34 ramsdell Exp $

// A hash table used to look up global identifiers.

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

import java.util.Hashtable;

/**
 * A hash table used to look up global identifiers.
 * @version May 1999
 * @author John D. Ramsdell
 */
final class GlobalTable
{
  final Hashtable ht = new Hashtable();

  /**
   * Get a global associated with an identifier.
   */
  Global get(String identifier) {
    Global var = (Global)ht.get(identifier);
    if (var == null) {
      var = new Global(identifier);
      ht.put(identifier, var);
    }
    return var;
  }

  /**
   * Construct table and load predefined identifiers.
   */
  GlobalTable() {
    put(new PatSubst());
    put(new Subst());
    put(new Cat());
    put(new Glob());
    put(new Dirs());
    put(new GetProp());
    put(new Join());
    put(new Equal());
    put(new First());
    put(new Rest());
    put(new Load());
  }

  private void put(Function fun) {
    String name = fun.getName();
    Global var = get(name);
    var.setValue(fun);
  }

  private static boolean isOneString(Value v) {
    if (StringList.isStringList(v)) {
      StringList sl = (StringList)v;
      return sl != null && sl.getRest() == null;
    }
    else
      return false;
  }

  /*
   * Implementations as functions of various string utilities.
   */
  
  private static class PatSubst
  implements Function
  {
    private final static int nargs = 3;

    public String getName() {
      return "patsubst";
    }

    public Value invoke(Value[] args, StringList list) 
	 throws Exception
    {
      if (args.length == nargs) {
	if (isOneString(args[0])) {
	  StringList sl0 = (StringList)args[0];
	  if (isOneString(args[1])) {
	    StringList sl1 = (StringList)args[1];
	    if (StringList.isStringList(args[2])) {
	      StringList sl2 = (StringList)args[2];
	      return StringUtils.patsubst(sl0.getString(),
					  sl1.getString(),
					  sl2, list);
	    }
	    else {
	      String msg = getName() 
		+ ": the third argument is not a string list";
	      throw new StringListCastException(msg);
	    }
	  }
	  else {
	    String msg = getName() 
	      + ": the second argument is not a string";
	    throw new StringListCastException(msg);
	  }
	}
	else {
	  String msg = getName() 
	    + ": the first argument is not a string";
	  throw new StringListCastException(msg);
	}
      }
      else {
	String msg = "Arg count error: " + getName() + " expecting " + nargs +
	  " but got " + args.length + " arguments";
	throw new WrongArgCountException(msg);
      }
    }
  }

  private static class Subst
  implements Function
  {
    private final static int nargs = 3;

    public String getName() {
      return "subst";
    }

    public Value invoke(Value[] args, StringList list) 
	 throws Exception
    {
      if (args.length == nargs) {
	if (isOneString(args[0])) {
	  StringList sl0 = (StringList)args[0];
	  if (isOneString(args[1])) {
	    StringList sl1 = (StringList)args[1];
	    if (StringList.isStringList(args[2])) {
	      StringList sl2 = (StringList)args[2];
	      return StringUtils.subst(sl0.getString(),
				       sl1.getString(),
				       sl2, list);
	    }
	    else {
	      String msg = getName() 
		+ ": the third argument is not a string list";
	      throw new StringListCastException(msg);
	    }
	  }
	  else {
	    String msg = getName() 
	      + ": the second argument is not a string";
	    throw new StringListCastException(msg);
	  }
	}
	else {
	  String msg = getName() 
	    + ": the first argument is not a string";
	  throw new StringListCastException(msg);
	}
      }
      else {
	String msg = "Arg count error: " + getName() + " expecting " + nargs +
	  " but got " + args.length + " arguments";
	throw new WrongArgCountException(msg);
      }
    }
  }

  private static class Cat
  implements Function
  {
    private final static int nargs = 1;

    public String getName() {
      return "cat";
    }

    public Value invoke(Value[] args, StringList list) 
	 throws Exception
    {
      if (args.length == nargs) {
	if (StringList.isStringList(args[0])) {
	  return StringUtils.cat((StringList)args[0], list);
	}
	else {
	  String msg = getName() 
	    + ": the argument is not a string list";
	  throw new StringListCastException(msg);
	}
      }
      else {
	String msg = "Arg count error: " + getName() + " expecting " + nargs +
	  " but got " + args.length + " arguments";
	throw new WrongArgCountException(msg);
      }
    }
  }

  private static class Glob
  implements Function
  {
    private final static int nargs = 1;

    public String getName() {
      return "glob";
    }

    public Value invoke(Value[] args, StringList list) 
	 throws Exception
    {
      if (args.length == nargs) {
	if (StringList.isStringList(args[0])) {
	  return StringUtils.glob((StringList)args[0], list);
	}
	else {
	  String msg = getName() 
	    + ": the argument is not a string list";
	  throw new StringListCastException(msg);
	}
      }
      else {
	String msg = "Arg count error: " + getName() + " expecting " + nargs +
	  " but got " + args.length + " arguments";
	throw new WrongArgCountException(msg);
      }
    }
  }

  private static class Dirs
  implements Function
  {
    private final static int nargs = 1;

    public String getName() {
      return "dirs";
    }

    public Value invoke(Value[] args, StringList list) 
	 throws Exception
    {
      if (args.length == nargs) {
	if (StringList.isStringList(args[0])) {
	  return StringUtils.dirs((StringList)args[0], list);
	}
	else {
	  String msg = getName() 
	    + ": the argument is not a string list";
	  throw new StringListCastException(msg);
	}
      }
      else {
	String msg = "Arg count error: " + getName() + " expecting " + nargs +
	  " but got " + args.length + " arguments";
	throw new WrongArgCountException(msg);
      }
    }
  }

  private static class GetProp
  implements Function
  {
    private final static int nargs = 1;

    public String getName() {
      return "getprop";
    }

    public Value invoke(Value[] args, StringList list) 
	 throws Exception
    {
      if (args.length == nargs) {
	if (StringList.isStringList(args[0])) {
	  return StringUtils.getprop((StringList)args[0], list);
	}
	else {
	  String msg = getName() 
	    + ": the argument is not a string list";
	  throw new StringListCastException(msg);
	}
      }
      else {
	String msg = "Arg count error: " + getName() + " expecting " + nargs +
	  " but got " + args.length + " arguments";
	throw new WrongArgCountException(msg);
      }
    }
  }

  private static class Join
  implements Function
  {
    private final static int nargs = 2;

    public String getName() {
      return "join";
    }

    public Value invoke(Value[] args, StringList list) 
	 throws Exception
    {
      if (args.length == nargs) {
	if (StringList.isStringList(args[0])) {
	  if (StringList.isStringList(args[1])) {
	    return StringUtils.join((StringList)args[0],
				    (StringList)args[1],
				    list);
	  }
	  else {
	    String msg = getName() 
	      + ": the second argument is not a string list";
	    throw new StringListCastException(msg);
	  }
	}
	else {
	  String msg = getName() 
	    + ": the first argument is not a string list";
	  throw new StringListCastException(msg);
	}
      }
      else {
	String msg = "Arg count error: " + getName() + " expecting " + nargs +
	  " but got " + args.length + " arguments";
	throw new WrongArgCountException(msg);
      }
    }
  }

  private static class Equal
  implements Function
  {
    private final static int nargs = 2;

    public String getName() {
      return "equal";
    }

    public Value invoke(Value[] args, StringList list) 
	 throws Exception
    {
      if (args.length == nargs) {
	if (StringList.isStringList(args[0])) {
	  if (StringList.isStringList(args[1])) {
	    return StringUtils.equal((StringList)args[0],
				     (StringList)args[1],
				     list);
	  }
	  else {
	    String msg = getName() 
	      + ": the second argument is not a string list";
	    throw new StringListCastException(msg);
	  }
	}
	else {
	  String msg = getName() 
	    + ": the first argument is not a string list";
	  throw new StringListCastException(msg);
	}
      }
      else {
	String msg = "Arg count error: " + getName() + " expecting " + nargs +
	  " but got " + args.length + " arguments";
	throw new WrongArgCountException(msg);
      }
    }
  }

  private static class First
  implements Function
  {
    private final static int nargs = 1;

    public String getName() {
      return "first";
    }

    public Value invoke(Value[] args, StringList list) 
	 throws Exception
    {
      if (args.length == nargs) {
	if (StringList.isStringList(args[0])) {
	  StringList sl = (StringList)args[0];
	  if (sl == null)
	    return list;
	  else
	    return new StringList(sl.getString(), list);
	}
	else {
	  String msg = getName() 
	    + ": the argument is not a string list";
	  throw new StringListCastException(msg);
	}
      }
      else {
	String msg = "Arg count error: " + getName() + " expecting " + nargs +
	  " but got " + args.length + " arguments";
	throw new WrongArgCountException(msg);
      }
    }
  }  

  private static class Rest
  implements Function
  {
    private final static int nargs = 1;

    public String getName() {
      return "rest";
    }

    public Value invoke(Value[] args, StringList list) 
	 throws Exception
    {
      if (args.length == nargs) {
	if (StringList.isStringList(args[0])) {
	  StringList sl = (StringList)args[0];
	  if (sl == null)
	    return list;
	  else
	    return StringList.append(sl.getRest(), list);
	}
	else {
	  String msg = getName() 
	    + ": the argument is not a string list";
	  throw new StringListCastException(msg);
	}
      }
      else {
	String msg = "Arg count error: " + getName() + " expecting " + nargs +
	  " but got " + args.length + " arguments";
	throw new WrongArgCountException(msg);
      }
    }
  }

  private static class Load
  implements Function
  {
    private final static int nargs = 1;

    public String getName() {
      return "load";
    }

    public Value invoke(Value[] args, StringList list) 
	 throws Exception
    {
      if (args.length == nargs) {
	if (list == null) {
	  if (isOneString(args[0])) {
	    String clsName = ((StringList)args[0]).getString();
	    Class cls = Class.forName(clsName);
	    return (Function)cls.newInstance();
	  }
	  else {
	    String msg = getName() 
	      + ": the argument is not a string";
	    throw new StringListCastException(msg);
	  }
	}
	else {
	  String msg = getName() 
	    + ": loaded value is not a string";
	  throw new StringListCastException(msg);
	}
      }
      else {
	String msg = "Arg count error: " + getName() + " expecting " + nargs +
	  " but got " + args.length + " arguments";
	throw new WrongArgCountException(msg);
      }
    }
  }
}
