// $Id: Javac.java,v 1.1.1.1 2000/09/26 11:20:35 ramsdell Exp $

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

import sun.tools.javac.*; 
import edu.neu.ccs.jmk.Operator;
import edu.neu.ccs.jmk.CommandFailedException;
import java.io.*;

/**
 * Javac as an Operator.
 * <pre>
 * javac = "edu.neu.ccs.jmk.sun.Javac";
 *
 * "all":;
 * {
 *   forname javac "-O" "source.java";
 * }
 * </pre>
 */
public final class Javac
implements Operator, Runnable
{
    // if the static compiler is not used during 30s
    // in a row, we clean the reference and call the GC
    private static final long CLEANER_DELAY=1000*30;
    private static  Thread cleaner_=null;
    private static Main ji_=null;
    private static long launchdate_=0;

    public String getName()
    {
        return("Javac");
    }

    public void run()
    {
        while ( System.currentTimeMillis()-launchdate_<CLEANER_DELAY )
        {
            try
            {
                Thread.sleep(CLEANER_DELAY+1000);
            }
            catch ( Exception e )
            {
                System.err.println(e.toString());
            }
        }

        // clean up resources
        synchronized(this)
        {
            ji_=null;
            cleaner_=null;
            System.gc();
        }
    }

    public void exec(String[] _args, PrintWriter _out)
    throws CommandFailedException
    {
        if ( _args.length == 0 )
            throw new CommandFailedException("No args to " + getName());
        try
        {
            // make sure the cleaning thread doesn't destroy the resource
            // while we use it.
            synchronized(this)
            {
                if ( ji_==null )
                {
                    ji_ = new Main(new OutputStreamToPrintWriter(_out), "javac");
                }

                launchdate_=System.currentTimeMillis();
                if ( cleaner_==null )
                {
                    cleaner_=new Thread(this);
                    cleaner_.start();
                }

                if ( ji_.compile(_args) == false )
                    throw new CommandFailedException(getName() + " failed");
            }
        }
        catch ( CommandFailedException ex1 )
        {
            throw ex1;
        }
        catch ( Exception ex2 )
        {
            throw new CommandFailedException(getName() + " threw an exception -> "+ex2.toString());
        }
    }
}
