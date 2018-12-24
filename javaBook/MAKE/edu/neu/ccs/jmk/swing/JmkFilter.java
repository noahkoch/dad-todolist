// $Id: JmkFilter.java,v 1.1.1.1 2000/09/26 11:20:35 ramsdell Exp $

/*
 * Copyright 1997 by Olivier Refalo
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
 *
 * @author John D. Ramsdell
 * @author olivier refalo
 *
 */

package edu.neu.ccs.jmk.swing;

import java.io.*;

// this is the filter for the file chooser
public final
class JmkFilter
extends javax.swing.filechooser.FileFilter {

    private final String jmk="jmk";
    // accept jmk files only.
    public boolean accept(File f) {

        if (f.isDirectory()) {
            return(true);
        }

        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            String extension = s.substring(i+1).toLowerCase();
            return(jmk.equals(extension));
        }
        return(false);
    }

    // The description of this filter
    public String getDescription() {
        return("Jmk makefiles");
    }
}



