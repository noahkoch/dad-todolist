// $Id: AboutDG.java,v 1.1.1.1 2000/09/26 11:20:35 ramsdell Exp $

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
package edu.neu.ccs.jmk.swing;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.*;

import edu.neu.ccs.jmk.Make;


final public
class AboutDG
extends JDialog {

    /** Initializes the Form */
    public AboutDG(java.awt.Frame parent, boolean modal) {
        super (parent, modal);
        initComponents ();
        pack ();

        // Center window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = getSize();
        setBounds( (screenSize.width-windowSize.width)/2,
                   (screenSize.height-windowSize.height)/2, windowSize.width, windowSize.height);


    }

    private void initComponents () {

        setTitle ("About Jmk");
        setBackground (java.awt.Color.white);
        addWindowListener (new WindowAdapter () {
                               public void windowClosing (WindowEvent evt) {
                                   closeDialog (evt);
                               }
                           }
                          );
        getContentPane ().setLayout (new java.awt.BorderLayout ());

        bottomPL = new JPanel ();
        bottomPL.setBackground (java.awt.Color.white);
        bottomPL.setLayout (new java.awt.FlowLayout ());

        okBT = new JButton ();
        okBT.setText ("Ok");
        okBT.addActionListener (new ActionListener () {
                                    public void actionPerformed (ActionEvent evt) {
                                        okBTActionPerformed (evt);
                                    }
                                }
                               );
        bottomPL.add (okBT);

        getContentPane ().add (bottomPL, "South");

        textSP = new JScrollPane ();
        textSP.setBackground (java.awt.Color.white);
        textSP.setBorder (new EmptyBorder (new java.awt.Insets(5, 5, 5, 5)));

        textTP = new JTextPane ();
        textTP.setText (Make.version+"\n\nCopyright 1998-1999 by John D. Ramsdell\nSwing GUI by Olivier Refalo\n\nThis program is free software; you can redistribute it and/or\nmodify it under the terms of the GNU Lesser General Public License\nas published by the Free Software Foundation; either version 2\nof the License, or (at your option) any later version.\n\nThis program is distributed in the hope that it will be useful,\nbut WITHOUT ANY WARRANTY; without even the implied warranty of\nMERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\nGNU Lesser General Public License for more details.\n\nYou should have received a copy of the GNU Lesser General Public License\nalong with this program; if not, write to the Free Software\nFoundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.\n");
        textTP.setEditable (false);
        textSP.add (textTP);

        textSP.setViewportView (textTP);
        getContentPane ().add (textSP, "Center");

        leftPL = new JPanel ();
        leftPL.setBackground (java.awt.Color.white);
        leftPL.setLayout (new java.awt.FlowLayout ());

        logoLB = new JLabel ();
        logoLB.setIcon (new ImageIcon (getClass ().getResource ("/edu/neu/ccs/jmk/swing/gfx/jmklogo.gif")));
        logoLB.setDisabledIcon ((Icon) null);
        leftPL.add (logoLB);

        getContentPane ().add (leftPL, "West");

    }

    private void okBTActionPerformed (ActionEvent evt) {
        setVisible (false);
        dispose ();
    }

    /** Closes the dialog */
    private void closeDialog(WindowEvent evt) {
        setVisible (false);
        dispose ();
    }


    private JPanel bottomPL;
    private JScrollPane textSP;
    private JPanel leftPL;
    private JButton okBT;
    private JTextPane textTP;
    private JLabel logoLB;

}
