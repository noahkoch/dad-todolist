// $Id: OutputStreamToPrintWriter.java,v 1.1.1.1 2000/09/26 11:20:35 ramsdell Exp $

/*
* Copyright 1998 by olivier Refalo
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

import java.io.*;

public final
class OutputStreamToPrintWriter
extends OutputStream
{

    private PrintWriter pw_=null;

    public OutputStreamToPrintWriter(PrintWriter _pw)
    {
        pw_=_pw;
    }

    public void write(int b)
    throws IOException {
        pw_.print((char)b);
    }
}

