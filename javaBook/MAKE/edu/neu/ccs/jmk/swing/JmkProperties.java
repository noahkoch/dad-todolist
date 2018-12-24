// $Id: JmkProperties.java,v 1.1.1.1 2000/09/26 11:20:35 ramsdell Exp $

package edu.neu.ccs.jmk.swing;

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
 */


import  java.util.*;
import  java.io.*;

public final
class JmkProperties
{

    private final static String JMK_PROPERTIES = "jmk.properties";

    // This is a singleton
    private Properties              properties_ = null ;
    private static JmkProperties    instance_ = null;

    private JmkProperties()
    {
        properties_=new Properties() ;
    }
    public static JmkProperties getInstance()
    {
        if (instance_==null)
            instance_ = new JmkProperties();
        return(instance_);
    }

    // Accessors for resources.

    public String getString(String _key, String _defaultValue)
    {
        String value = properties_.getProperty(_key);
        if (value == null)
        {
            return(_defaultValue);
        }
        else
        {
            value=value.trim();
            return(value);
        }
    }


    public boolean getBoolean(String _key, boolean _defaultValue)
    {
        String value = properties_.getProperty(_key);
        if (value == null)
        {
            return(_defaultValue);
        }
        else
        {
            value=value.trim();
            if (value.toUpperCase().compareTo("TRUE")==0)
                return(true);
            else if (value.toUpperCase().compareTo("FALSE")==0)
                return(false);
            else
            {
                System.err.println("Invalid boolean property for key <"+_key+">");
                return (_defaultValue);
            }
        }
    }

    public int getInteger(String _key, int _defaultValue)
    {

        String value = properties_.getProperty(_key);
        if (value == null)
        {
            return(_defaultValue);
        }
        else
        {
            value=value.trim();
            try
            {
                int v=Integer.parseInt(value);
                return(v);
            }
            catch (NumberFormatException e)
            {
                System.err.println("Type mismatch for key <"+_key+">, defaulting to <"+_defaultValue+">");
                return(_defaultValue);
            }
        }
    }

    public String put(String _key, String _value)
    {
        return(String)properties_.put(_key, _value);
    }

    // load the property file
    public void load()
    {
        try
        {
            InputStream fis = new FileInputStream(JMK_PROPERTIES);
            properties_.load(fis);
            fis.close();
        }
        catch (Exception e)
        {
            // property file not found
        }
    }

    // save the property file
    public void save()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(JMK_PROPERTIES);
            properties_.save(fos, "Jmk property file");
            fos.close();
            properties_.clear();
        }
        catch (IOException e)
        {
            System.err.println(e);
        }
    }

}

