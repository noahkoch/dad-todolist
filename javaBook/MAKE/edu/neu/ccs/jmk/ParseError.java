// $Id: ParseError.java,v 1.1.1.1 2000/09/26 11:20:34 ramsdell Exp $

// The exception raised by parse errors.

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

/**
 * The parse error exception.
 * @version November 1997
 * @author John D. Ramsdell
 */
public class ParseError
extends Exception
{
  private File file;
  private int lineNumber;

  ParseError(File file, int lineNumber, String msg) {
    super(msg);
    this.file = file;
    // LineNumberReader starts a 0 and counts line terminators
    this.lineNumber = lineNumber + 1;
  }

  /**
   * Returns the line number at which the parse error occured.
   */
  public int getLineNumber() {
    return lineNumber;
  }

  /**
   * Returns the file name in which the parse error occured.
   */
  public String getFileName() {
    return file == null ? "" : file.getPath();
  }
}
