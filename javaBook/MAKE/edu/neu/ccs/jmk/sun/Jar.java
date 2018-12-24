// $Id: Jar.java,v 1.1.1.1 2000/09/26 11:20:35 ramsdell Exp $

/*
 * Copyright 1999 by Olivier Refalo
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

package edu.neu.ccs.jmk.sun;

import sun.tools.jar.*; 
import edu.neu.ccs.jmk.Operator;
import edu.neu.ccs.jmk.CommandFailedException;
import java.io.*;

/**
 * Jar as an Operator.
 * <pre>
 * jar = "edu.neu.ccs.jmk.sun.Jar";
 *
 * "all":;
 * {
 *   forname jar "cvf" "arch.jar" (glob "*.class");
 * }
 * </pre>
 */
public final class Jar
implements Operator
{
    public String getName()
    {
        return("Jar");
    }


    public void exec(String[] _args, PrintWriter _out)
    throws CommandFailedException
    {
        Main ji;

        if ( _args.length == 0 )
            throw new CommandFailedException("No args to " + getName());
        try
        {
            OutputStream os=new OutputStreamToPrintWriter(_out);
            // I know, this code is deprecated but Jar needs PrintStreams !
            PrintStream ps=new PrintStream(os);
            ji = new Main(ps, ps, "jar");

            if ( ji.run(_args) == false )
                throw new CommandFailedException(getName() + " failed");

        } catch ( CommandFailedException ex1 )
        {
            throw ex1;
        } catch ( Exception ex2 )
        {
            throw new CommandFailedException(getName() + " threw an exception ->"+ex2.toString());
        } finally
        {
            ji=null;
            // clean up VM allocations
            System.gc();
        }
    }

}
