// $Id: MakeInput.java,v 1.1.1.1 2000/09/26 11:20:35 ramsdell Exp $

// A text field for the make window.

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

import java.awt.*;
import java.awt.event.*;

/** 
 * A text field for the make window with methods that grab the focus.
 * @version November 1997
 * @author John D. Ramsdell
 */ 
final class MakeInput
extends TextField
implements FocusListener
{
  MakeInput(String text) {
    super(text);
  }
  
  /**
   * Make this object a focus listener for components that should not
   * grab the focus.  If another component gets the focus, this method
   * grabs it back.
   */
  public void focusGained(FocusEvent e) {
    requestFocus();
  }

  /**
   * This method does nothing.
   */
  public void focusLost(FocusEvent e) {}

}
