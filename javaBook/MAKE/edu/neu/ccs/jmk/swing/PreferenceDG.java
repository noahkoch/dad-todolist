// $Id: PreferenceDG.java,v 1.1.1.1 2000/09/26 11:20:35 ramsdell Exp $

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

/*
  Add a save button!

*/

package edu.neu.ccs.jmk.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

final public
class PreferenceDG
extends JDialog
{
    static final String metalClassname="javax.swing.plaf.metal.MetalLookAndFeel";
    static final String motifClassname="com.sun.java.swing.plaf.motif.MotifLookAndFeel";
    static final String windowsClassname="com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    static final String macClassname="com.sun.java.swing.plaf.mac.MacLookAndFeel";

    // The part is called by the main window to set/retreive preferences

    private String landf_=null;
    private boolean autoscroll_=true;
    private boolean autoclear_=true;
    private boolean automake_=true;
    private int exitstatus_=0;    //0-CANCEL 1-OK


    private JTabbedPane mainTP;
    private JPanel bottomPL;
    private JPanel lfPL;
    private JPanel optionPL;
    private JPanel line1PL;
    private JPanel line2PL;
    private JLabel widthLB;
    private JTextField widthTF;
    private JLabel heightLB;
    private JTextField heightTF;
    private JRadioButton winRB;
    private JRadioButton metalRB;
    private JRadioButton motifRB;
    private JRadioButton macRB;
    private JCheckBox autoclearCB;
    private JCheckBox autoscrollCB;
    private JCheckBox automakeCB;
    private JButton saveBT;
    private JButton okBT;
    private JButton cancelBT;


    public PreferenceDG(java.awt.Frame parent, boolean modal)
    {
        super (parent, modal);
        initComponents ();
        pack ();

        // Center window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = getSize();
        setBounds( (screenSize.width-windowSize.width)/2,
                   (screenSize.height-windowSize.height)/2, windowSize.width, windowSize.height);
    }


    // Sets combo boxes to the right L&F
    public void setLnF(String _lnf)
    {
        metalRB.setSelected(false);
        motifRB.setSelected(false);
        winRB.setSelected(false);
        macRB.setSelected(false);

        if ( _lnf.indexOf("Metal") >= 0 ) metalRB.setSelected(true);
        else if ( _lnf.indexOf("Motif") >= 0 ) motifRB.setSelected(true);
        else if ( _lnf.indexOf("Windows") >= 0 ) winRB.setSelected(true);
        else if ( _lnf.indexOf("Mac") >= 0 ) macRB.setSelected(true);
        else
        {
            System.err.println("Unknown look and Feel:"+_lnf);
            metalRB.setSelected(true);
        }
    }


    // returns the selected look and feel
    private void initComponents ()
    {
        setTitle ("Jmk Preferences");
        addWindowListener (new WindowAdapter ()
                           {
                               public void windowClosing (WindowEvent evt)
                               {
                                   closeDialog (evt);
                               }
                           }
                          );
        getContentPane ().setLayout (new java.awt.BorderLayout ());

        mainTP = new JTabbedPane ();

        lfPL = new JPanel ();
        lfPL.setBorder (new EmptyBorder (new java.awt.Insets(5, 40, 5, 1)));
        lfPL.setLayout (new BoxLayout (lfPL, 1));

        winRB = new JRadioButton ();
        winRB.setText ("Windows");
        lfPL.add (winRB);

        metalRB = new JRadioButton ();
        metalRB.setText ("Metal");
        lfPL.add (metalRB);

        motifRB = new JRadioButton ();
        motifRB.setText ("Motif");
        lfPL.add (motifRB);

        macRB = new JRadioButton ();
        macRB.setText ("Mac");
        lfPL.add (macRB);

        mainTP.addTab ("Look&Feel", lfPL);

        optionPL = new JPanel ();
        optionPL.setBorder (new EmptyBorder (new java.awt.Insets(5, 40, 5, 1)));
        optionPL.setLayout (new BoxLayout (optionPL, 1));

        autoclearCB = new JCheckBox ();
        autoclearCB.setText ("Autoclear log");
        optionPL.add (autoclearCB);

        autoscrollCB = new JCheckBox ();
        autoscrollCB.setText ("Autoscroll log");
        optionPL.add (autoscrollCB);

        automakeCB = new JCheckBox ();
        automakeCB.setText ("Automake on target combo selection");
        optionPL.add (automakeCB);

        mainTP.addTab ("Options", optionPL);

        getContentPane ().add (mainTP, "Center");

        bottomPL = new JPanel ();
        bottomPL.setLayout (new java.awt.FlowLayout ());

        saveBT = new JButton ();
        saveBT.setText ("Save");
        saveBT.addActionListener (new ActionListener ()
                                  {
                                      public void actionPerformed (ActionEvent evt)
                                      {
                                          setBooleans();
                                          JmkProperties jp=JmkProperties.getInstance();
                                          jp.put("jmk.lookandfeel", getSelectedLnF());
                                          jp.put("jmk.autoscroll", ""+autoscroll_);
                                          jp.put("jmk.autoclear", ""+autoclear_);
                                          jp.put("jmk.automake", ""+automake_);
                                          jp.save();
                                          exitstatus_=1;
                                          setVisible(false);
                                          dispose();

                                      }
                                  });
        bottomPL.add (saveBT);

        okBT = new JButton ();
        okBT.setText ("Apply");
        okBT.addActionListener (new ActionListener ()
                                {
                                    public void actionPerformed (ActionEvent evt)
                                    {
                                        setBooleans ();
                                        exitstatus_=1;
                                        setVisible(false);
                                        dispose();

                                    }
                                });
        bottomPL.add (okBT);

        cancelBT = new JButton ();
        cancelBT.setText ("Cancel");
        cancelBT.addActionListener (new ActionListener ()
                                    {
                                        public void actionPerformed (ActionEvent evt)
                                        {
                                            cancelBTActionPerformed (evt);
                                        }
                                    });
        bottomPL.add (cancelBT);

        getContentPane ().add (bottomPL, "South");

        // Group Radiobuttons
        ButtonGroup group=new ButtonGroup();
        group.add(winRB);
        group.add(metalRB);
        group.add(motifRB);
        group.add(macRB);


    }

    private void cancelBTActionPerformed (ActionEvent evt)
    {
        setVisible(false);
        dispose();
        exitstatus_=0;
    }


    public int getExitStatus()
    {
        return(exitstatus_);
    }

    public void setAutoscroll(boolean _b)
    {
        autoscrollCB.setSelected(_b);
        autoscroll_=_b;
    }
    public boolean getAutoscroll()
    {
        return(autoscroll_);
    }


    public void setAutoclear(boolean _b)
    {
        autoclearCB.setSelected(_b);
        autoclear_=_b;
    }
    public boolean getAutoclear()
    {
        return(autoclear_);
    }

    public void setAutomake(boolean _b)
    {
        automakeCB.setSelected(_b);
        automake_=_b;
    }
    public boolean getAutomake()
    {
        return(automake_);
    }


    public String getSelectedLnF()
    {

        if ( motifRB.isSelected() ) return(motifClassname);
        if ( winRB.isSelected() ) return(windowsClassname);
        if ( macRB.isSelected() ) return(macClassname);
        return(metalClassname);
    }


    private void setBooleans()
    {
        autoclear_=autoclearCB.isSelected();
        autoscroll_=autoscrollCB.isSelected();
        automake_=automakeCB.isSelected();
    }


    private void closeDialog(WindowEvent evt)
    {
        setVisible (false);
        dispose ();
        exitstatus_=0;
    }


}
