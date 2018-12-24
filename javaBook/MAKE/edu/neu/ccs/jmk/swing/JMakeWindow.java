// $Id: JMakeWindow.java,v 1.1.1.1 2000/09/26 11:20:35 ramsdell Exp $

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

import edu.neu.ccs.jmk.*;
import edu.neu.ccs.jmk.awt.RunQueue;

import java.io.*;
import java.util.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public final
class JMakeWindow 
extends JEditorPane
{
    private final static String NL = System.getProperty("line.separator");

    static private Make make;
    static private String[] targets=null;
    static private final RunQueue tasks = new RunQueue();

    private static boolean autoscroll_=true;
    private static boolean autoclear_=true;
    private static boolean automake_=false;
    private static String LnF_=null;

    private Icon TrafficNone = null;
    private Icon TrafficGreen = null;
    private Icon TrafficYellow = null;
    private Icon TrafficRed = null;

    private int trafficStatus_=0;


    /** Initializes the Form */
    private JMakeWindow(Make make, String[] targets)
    {
        this.make = make;
        this.targets = targets;
        setEditable(false);
        // Create make writer
        make.setOut(new JmkPrintWriter(new MyWriter(), true));
        new Thread(tasks).start();
    }


    final class MyWriter 
    extends JmkWriter
    {
        private boolean closed_ = false;
        private MutableAttributeSet blackcolor_=null;
        private MutableAttributeSet bluecolor_=null;
        private MutableAttributeSet redcolor_=null;

        public MyWriter()
        {
            super();

            blackcolor_ = new SimpleAttributeSet();
            StyleConstants.setForeground(blackcolor_, Color.black);
            bluecolor_ = new SimpleAttributeSet();
            StyleConstants.setForeground(bluecolor_, Color.blue);
            redcolor_ = new SimpleAttributeSet();
            StyleConstants.setForeground(redcolor_, Color.red);
            StyleConstants.setBold(redcolor_, true);
        }

        private void appendLine(String _s,AttributeSet _attr)
        throws BadLocationException
        {
            Document document=mw.getDocument();
            document.insertString(document.getEndPosition().getOffset()-1,_s+NL,_attr);
        }

        public void jmk_println(String _s)
        {
            try
            {
                appendLine(_s, bluecolor_);

            } catch ( BadLocationException e )
            {
                System.err.println(e.toString());
            }
        }

        public void jmkerr_println(String _s)
        {
            try
            {
                appendLine(_s, redcolor_);

            } catch ( BadLocationException e )
            {
                System.err.println(e.toString());
            }
            setRed();
        }

        private StringBuffer buffer_ = new StringBuffer(80);
        private boolean eol=false;

        public void write (char cbuf[], int off, int len) 
        throws IOException
        {
            if ( closed_ )
                throw new IOException("Write of a closed writer");

            try
            {
                for ( int i = off; i < len; i++ )
                {
                    char ch = cbuf[i];
                    if ( eol )
                    {
                        eol = false;
                        if ( ch == '\n' )
                            continue;
                    }
                    if ( ch == '\n' || ch == '\r' )
                    {
                        if ( ch == '\r' )
                            eol = true;
                        appendLine(buffer_.toString(),blackcolor_);
                        buffer_.setLength(0);
                    } else
                    {
                        buffer_.append(ch);
                    }
                }

            } catch ( BadLocationException e )
            {
                System.err.println(e.toString());
            }

            // scroll the bar down.
            if ( autoscroll_ )
            {
                JScrollBar sb=logSP.getVerticalScrollBar();
                sb.setValue(sb.getMaximum());
            }

        }


        public void flush() 
        throws IOException
        {

            if ( closed_ )
                throw new IOException("Flush of a closed writer");

        }

        public void close() 
        throws IOException
        {
            if ( !closed_ )
            {
                flush();
                closed_ = true;
            }
        }

    }

    private static String makefileLabel(Make make)
    {
        return(" Makefile: " + make.getFile().getPath());
    }

    public static void createMakeWindow(Make make, String [] targets)
    {

        JmkProperties jp=JmkProperties.getInstance();
        jp.load();

        LnF_=jp.getString("jmk.lookandfeel",  UIManager.getSystemLookAndFeelClassName());
        autoscroll_=jp.getBoolean("jmk.autoscroll",true);
        autoclear_=jp.getBoolean("jmk.autoclear",true);
        automake_=jp.getBoolean("jmk.automake",false);


        // Set default Look&Feel
        try
        {
            UIManager.setLookAndFeel(LnF_ );
        } catch ( Exception e )
        {
            System.err.println(e.toString());
        }

        f = new JFrame("Jmk");
        // set frame icon
        f.setIconImage((new ImageIcon (f.getClass().getResource ("/edu/neu/ccs/jmk/swing/gfx/jmkicon.gif"))).getImage());


        f.addWindowListener (new java.awt.event.WindowAdapter ()
                             {
                                 public void windowClosing (java.awt.event.WindowEvent evt)
                                 {
                                     exitForm (evt);
                                 }
                             });
        f.getContentPane ().setLayout (new java.awt.BorderLayout ());

        // Size & Center window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = new Dimension(screenSize.width/2, screenSize.height/2);
        f.setBounds( (screenSize.width-windowSize.width)/4,
                     (screenSize.height-windowSize.height)/4, windowSize.width, windowSize.height);


        mainMB = new JMenuBar ();
        fileMN = new JMenu ();
        fileMN.setText ("File");
        fileMN.setMnemonic('F');
        openMI = new JMenuItem ();
        openMI.setText ("Open");
        openMI.addActionListener (new java.awt.event.ActionListener ()
                                  {
                                      public void actionPerformed (java.awt.event.ActionEvent evt)
                                      {
                                          openMIActionPerformed (evt);
                                      }
                                  });
        openMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));

        fileMN.add(openMI);

        reloadMI = new JMenuItem ();
        reloadMI.setText ("Reload");
        reloadMI.addActionListener (new java.awt.event.ActionListener ()
                                    {
                                        public void actionPerformed (java.awt.event.ActionEvent evt)
                                        {
                                            reloadMIActionPerformed (evt);
                                        }
                                    });
        reloadMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));

        fileMN.add(reloadMI);

        fileMN.addSeparator();

        exitMI = new JMenuItem ();
        exitMI.setText ("Exit");
        exitMI.addActionListener (new java.awt.event.ActionListener ()
                                  {
                                      public void actionPerformed (java.awt.event.ActionEvent evt)
                                      {
                                          exitMIActionPerformed (evt);
                                      }
                                  });
        exitMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        fileMN.add(exitMI);

        mainMB.add(fileMN);

        commandMN = new JMenu ();
        commandMN.setText ("Command");
        commandMN.setMnemonic('C');
        makeMI = new JMenuItem ();
        makeMI.setText ("Make");
        makeMI.addActionListener (new ActionListener ()
                                  {
                                      public void actionPerformed (java.awt.event.ActionEvent evt)
                                      {
                                          makeMIActionPerformed (evt);
                                      }
                                  });
        makeMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        commandMN.add(makeMI);

        cancelMI = new JMenuItem ();
        cancelMI.setText ("Cancel");
        cancelMI.addActionListener (new ActionListener ()
                                    {
                                        public void actionPerformed (java.awt.event.ActionEvent evt)
                                        {
                                            cancelMIActionPerformed (evt);
                                        }
                                    });
        cancelMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        commandMN.add(cancelMI);

        mainMB.add(commandMN);

        optionsMN = new JMenu ();
        optionsMN.setText ("Options");
        optionsMN.setMnemonic('O');
        prefMI = new JMenuItem ();
        prefMI.setText ("Preferences");
        prefMI.setMnemonic('P');
        prefMI.addActionListener (new ActionListener ()
                                  {
                                      public void actionPerformed (java.awt.event.ActionEvent evt)
                                      {
                                          prefMIActionPerformed (evt);
                                      }
                                  });
        optionsMN.add(prefMI);

        optionsMN.addSeparator();

        verboseMI = new JCheckBoxMenuItem ();
        verboseMI.setState (make.isVerbose());
        verboseMI.setText ("Verbose");
        verboseMI.setMnemonic('V');
        verboseMI.addActionListener (new java.awt.event.ActionListener ()
                                     {
                                         public void actionPerformed (java.awt.event.ActionEvent evt)
                                         {
                                             verboseMIActionPerformed (evt);
                                         }
                                     });
        optionsMN.add(verboseMI);

        justprintMI = new JCheckBoxMenuItem ();
        justprintMI.setState (make.isJustPrinting());
        justprintMI.setText ("Just Print");
        justprintMI.setMnemonic('J');
        justprintMI.addActionListener (new java.awt.event.ActionListener ()
                                       {
                                           public void actionPerformed (java.awt.event.ActionEvent evt)
                                           {
                                               justprintMIActionPerformed (evt);
                                           }
                                       });
        optionsMN.add(justprintMI);

        mainMB.add(optionsMN);

        helpMN = new JMenu ();
        helpMN.setText ("Help");
        helpMN.setMnemonic('H');
        aboutMI = new JMenuItem ();
        aboutMI.setText ("About...");
        aboutMI.setMnemonic('A');
        aboutMI.addActionListener (new java.awt.event.ActionListener ()
                                   {
                                       public void actionPerformed (java.awt.event.ActionEvent evt)
                                       {
                                           aboutMIActionPerformed (evt);
                                       }
                                   });
        helpMN.add(aboutMI);

        mainMB.add(helpMN);

        f.setJMenuBar(mainMB);

        logSP = new JScrollPane ();
        logSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mw = new JMakeWindow(make, targets);
        mw.setContentType ("text/rtf");

        mw.addKeyListener (new java.awt.event.KeyAdapter ()
                           {
                               public void keyPressed (java.awt.event.KeyEvent evt)
                               {
                                   if ( evt.getKeyCode()==java.awt.event.KeyEvent.VK_ENTER )
                                       mw.makeTarget();
                               }
                           }
                          );



        logSP.setViewportView (mw);
        f.getContentPane ().add (logSP, "Center");

        mainTB = new JToolBar ();
        mainTB.setFloatable (false);
        mainTB.setLayout (new java.awt.FlowLayout (0, 5, 5));

        openBT = new JButton ();
        openBT.setIcon (new ImageIcon (f.getClass ().getResource ("/edu/neu/ccs/jmk/swing/gfx/VCROpen.gif")));
        openBT.setToolTipText ("Open a new makefile");
        openBT.addActionListener (new java.awt.event.ActionListener ()
                                  {
                                      public void actionPerformed (java.awt.event.ActionEvent evt)
                                      {
                                          openBTActionPerformed (evt);
                                      }
                                  });
        mainTB.add (openBT);

        playBT = new JButton ();
        playBT.setIcon (new ImageIcon (f.getClass ().getResource ("/edu/neu/ccs/jmk/swing/gfx/VCRPlay.gif")));
        playBT.setToolTipText ("Run makefile");
        playBT.addActionListener (new java.awt.event.ActionListener ()
                                  {
                                      public void actionPerformed (java.awt.event.ActionEvent evt)
                                      {
                                          playBTActionPerformed (evt);
                                      }
                                  });
        mainTB.add (playBT);

        stopBT = new JButton ();
        stopBT.setIcon (new ImageIcon (f.getClass ().getResource ("/edu/neu/ccs/jmk/swing/gfx/VCRStop.gif")));
        stopBT.setSelectedIcon (new ImageIcon (f.getClass ().getResource ("/edu/neu/ccs/jmk/swing/gfx/VCRStop.gif")));
        stopBT.setToolTipText ("Stop makefile processing");
        stopBT.addActionListener (new java.awt.event.ActionListener ()
                                  {
                                      public void actionPerformed (java.awt.event.ActionEvent evt)
                                      {
                                          stopBTActionPerformed (evt);
                                      }
                                  });
        mainTB.add (stopBT);

        targetCB = new JComboBox ();
        targetCB.setToolTipText ("Enter your target");
        targetCB.setEditable (true);
        targetCB.addActionListener (new java.awt.event.ActionListener ()
                                    {
                                        public void actionPerformed (java.awt.event.ActionEvent evt)
                                        {
                                            targetCBActionPerformed (evt);
                                        }
                                    });

        targetCB.addItem(getTargetString());
        targetCB.setSelectedIndex(0);

        mainTB.add (targetCB);

        f.getContentPane ().add (mainTB, "North");

        filenameLB = new JLabel (makefileLabel(make));
        mw.setNone();
        filenameLB.setDisabledIcon ((Icon) null);
        f.getContentPane ().add (filenameLB, "South");

        f.show();

        // load the makefile
        mw.load();
    }

    /**
     * Run make and print any error messages.
     */
    private void makeTask(String[] args, String targetString)
    {
        boolean failed=false;

        try
        {
            if ( !make.make(args) )
                ((JmkPrintWriter)make.getOut()).jmk_println("Nothing to make");
        } catch ( CommandFailedException ex )
        {
            failed=true;
            ((JmkPrintWriter)make.getOut()).jmkerr_println("Command failed: " + ex.getMessage());
        }
        if ( targetString.equals("") )
            targetString = "Make";
        else
            targetString = "Made " + targetString;
        ((JmkPrintWriter)make.getOut()).jmk_println(targetString + " at " + now());
        if ( !failed ) setGreen();
    }

    /**
     * Enqueue a make task.
     */
    private void make()
    {
        final String targetString = getTargetString();
        final String[] args = targets;
        tasks.add(new Runnable()
                  {
                      public void run()
                      {
                          makeTask(args, targetString);
                      }
                  });
    }

    /**
     * Load the makefile given in the make object
     * and print any error messages.
     */
    private void loadTask()
    {
        String fileName = make.getFile().getPath();
        try
        {
            make.load();
            ((JmkPrintWriter)make.getOut()).jmk_println("Loaded " + fileName + " at " + now());
        } catch ( FileNotFoundException ex )
        {
            ((JmkPrintWriter)make.getOut()).jmkerr_println("Cannot find makefile " + fileName);
        } catch ( ParseError pe )
        {
            ((JmkPrintWriter)make.getOut()).jmkerr_println(pe.getFileName() + ":" + pe.getLineNumber()
                                         + ": Parse error: " + pe.getMessage());
        }
    }

    /**
     * Enqueue a load task.
     */
    private void load()
    {
        tasks.add(new Runnable()
                  {
                      public void run()
                      {
                          loadTask();
                      }
                  });
    }

    /**
     * Cancel queued tasks.
     */
    private void cancel()
    {
        tasks.removeAll();
        make.setInterruptEnabled(true);
        ((JmkPrintWriter)make.getOut()).jmk_println("Cancel requested");
    }

    private DateFormat formatter_;

    private String now()
    {
        if ( formatter_ == null )
        {
            formatter_ = DateFormat.getTimeInstance();
            formatter_.setTimeZone(TimeZone.getDefault());
        }
        return(formatter_.format(new Date()));
    }

    static private String cachedTargetString;

    /**
     * Break a string into an array of strings.
     * A space character marks a breaking point.
     */
    private static String[] string2String_array(String str)
    {
        Vector v = new Vector();
        int s = 0;    // s is the start index

        while ( s < str.length() )
        {
            if ( str.charAt(s) == ' ' )
                s++;
            else
            {
                int e = str.indexOf(' ', s);    // e is the end index
                if ( e < 0 )
                {
                    v.addElement(str.substring(s));
                    break;
                }
                v.addElement(str.substring(s, e));
                s = e + 1;
            }
        }

        String[] str_array = new String[v.size()];
        v.copyInto(str_array);
        return(str_array);
    }

    /**
     * Get the current target list as a string.
     */
    static private String getTargetString()
    {
        if ( cachedTargetString == null )
        {
            if ( targets.length > 0 )
            {
                cachedTargetString = targets[0];
                for ( int i = 1; i < targets.length; i++ )
                    cachedTargetString += " " + targets[i];
            } else
                cachedTargetString = "";
        }
        return(cachedTargetString);
    }

    static private void aboutMIActionPerformed (java.awt.event.ActionEvent evt)
    {
        new AboutDG (f, true).show ();
    }

    static private void justprintMIActionPerformed (java.awt.event.ActionEvent evt)
    {
        make.setJustPrinting(!make.isJustPrinting());
    }

    static private void verboseMIActionPerformed (java.awt.event.ActionEvent evt)
    {
        make.setVerbose(!make.isVerbose());
    }

    static private void makeMIActionPerformed (java.awt.event.ActionEvent evt)
    {
        makeTarget();
    }

    static private void makeTarget()
    {
        mw.setNone();
        if ( autoclear_ )
            mw.setText("");
        mw.make();
    }


    static private void prefMIActionPerformed (java.awt.event.ActionEvent evt)
    {
        PreferenceDG pref=new PreferenceDG(f, true);

        pref.setAutoscroll(autoscroll_);
        pref.setAutoclear(autoclear_);
        pref.setAutomake(automake_);
        pref.setLnF(LnF_);

        pref.show ();

        if ( pref.getExitStatus()!=0 )
        {
            autoscroll_=pref.getAutoscroll();
            autoclear_=pref.getAutoclear();
            automake_=pref.getAutomake();

            // Look and Feel
            String lf=pref.getSelectedLnF();
            if ( LnF_.compareTo(lf)!=0 )
            {
                LnF_=lf;
                try
                {
                    UIManager.setLookAndFeel(lf);
                    SwingUtilities.updateComponentTreeUI(f);
                } catch ( Exception e )
                {
                    System.err.println(e.toString());
                }
            }

        }
    }

    static private void cancelMIActionPerformed (java.awt.event.ActionEvent evt)
    {
        mw.setYellow();
        mw.cancel();
    }

    static private void exitMIActionPerformed (java.awt.event.ActionEvent evt)
    {
        System.exit (0);
    }

    static private void reloadMIActionPerformed (java.awt.event.ActionEvent evt)
    {
        mw.load();
    }

    private static JFileChooser fc_=null;

    static private void openMIActionPerformed (java.awt.event.ActionEvent evt)
    {

        if ( fc_==null )
        {
            fc_=new JFileChooser();

            javax.swing.filechooser.FileFilter fileFilter=new JmkFilter();
            fc_.addChoosableFileFilter(fileFilter);
            fc_.setFileFilter(fileFilter);

        }

        int ret=fc_.showOpenDialog(f);
        if ( ret==JFileChooser.APPROVE_OPTION )
        {
            mw.setGreen();
            File file=fc_.getSelectedFile();
            setMakefilename(file);
            make.setFile(file);
            mw.load();
        }
    }

    static private void targetCBActionPerformed (java.awt.event.ActionEvent evt)
    {

        String s=(String)targetCB.getSelectedItem();

        boolean found=false;
        // make sure item is not already in the combobox
        for ( int i=0; found==false &&  i<targetCB.getItemCount();i++ )
        {
            String v=(String)targetCB.getItemAt(i);
            if ( v.compareTo(s)==0 )
                found=true;
        }
        if ( found==false )
            targetCB.addItem(s);

        if ( !cachedTargetString.equals(s) )
        {
            cachedTargetString = s;
            targets = string2String_array(s);
        }

        if ( automake_ )
            makeMIActionPerformed(evt);
    }

    static private void stopBTActionPerformed (java.awt.event.ActionEvent evt)
    {
        cancelMIActionPerformed(evt);
    }

    static private void playBTActionPerformed (java.awt.event.ActionEvent evt)
    {
        makeTarget();
    }

    static private void openBTActionPerformed (java.awt.event.ActionEvent evt)
    {
        openMIActionPerformed(evt);
    }

    static private void setMakefilename(File _file)
    {
        filenameLB.setText("Makefile: "+_file.getPath());
        f.setTitle("Jmk - "+_file.getName());
    }


    private void setNone()
    {
        // need to check if object is created because function called before label created!
        if ( filenameLB!=null )
        {
            if ( TrafficNone==null )
                TrafficNone = new ImageIcon (f.getClass ().getResource ("/edu/neu/ccs/jmk/swing/gfx/TrafficOff.gif"));

            filenameLB.setIcon (TrafficNone);
            trafficStatus_=0;
        }
    }

    private void setGreen()
    {
        if ( trafficStatus_!=1 )
        {
            if ( TrafficGreen==null )
                TrafficGreen = new ImageIcon (getClass ().getResource ("/edu/neu/ccs/jmk/swing/gfx/TrafficGreen.gif"));
            filenameLB.setIcon (TrafficGreen);
            trafficStatus_=1;
        }
    }

    private void setRed()
    {
        if ( trafficStatus_!=2 )
        {
            if ( TrafficRed==null )
                TrafficRed = new ImageIcon (getClass ().getResource ("/edu/neu/ccs/jmk/swing/gfx/TrafficRed.gif"));
            filenameLB.setIcon (TrafficRed);
            trafficStatus_=2;
        }
    }

    private void setYellow()
    {
        if ( trafficStatus_!=3 )
        {
            if ( TrafficYellow==null )
                TrafficYellow = new ImageIcon (getClass ().getResource ("/edu/neu/ccs/jmk/swing/gfx/TrafficYellow.gif"));
            filenameLB.setIcon (TrafficYellow);
            trafficStatus_=3;
        }
    }

    /** Exit the Application */
    private static void exitForm(java.awt.event.WindowEvent evt)
    {
        System.exit (0);
    }

    static private JFrame f=null;
    static private JMenuBar mainMB;
    static private JMenu fileMN;
    static private JMenuItem openMI;
    static private JScrollPane logSP;
    static private JMakeWindow mw;
    static private JToolBar mainTB;
    static private JButton openBT;
    static private JButton playBT;
    static private JButton stopBT;
    static private JComboBox targetCB;
    static private JLabel filenameLB=null;
    static private JMenuItem reloadMI;
    static private JMenuItem exitMI;
    static private JMenu commandMN;
    static private JMenuItem makeMI;
    static private JMenuItem cancelMI;
    static private JMenu optionsMN;
    static private JMenuItem prefMI;
    static private JCheckBoxMenuItem verboseMI;
    static private JCheckBoxMenuItem justprintMI;
    static private JMenu helpMN;
    static private JMenuItem aboutMI;

}
