//: c15:jdbc:Lookup2.java
// From 'Thinking in Java, 2nd ed.' by Bruce Eckel
// www.BruceEckel.com. See copyright notice in CopyRight.txt.
// Looks up email addresses in a 
// local database using JDBC.

import java.sql.*;
import java.io.*;

public class Lookup2 {
  public static void main(String[] args) 
  throws SQLException, ClassNotFoundException {

    // Load the Hypersonic SQL JDBC driver
    Class.forName("org.hsql.jdbcDriver");

    // Connect to the database
    // It will be create automatically if it does not yet exist
    // 'testfiles' in the URL is the name of the database
    // "sa" is the user name and "" is the (empty) password

    Connection c = DriverManager.getConnection(
        "jdbc:HypersonicSQL:http://1.1.1.4:/db_eng/hypersonicsql/demo/test","sa",""
    );


    Statement s = c.createStatement();
System.out.println("I am here!");
    // SQL code:
    ResultSet r = s.executeQuery("SELECT * FROM ADDRESS");
    while(r.next()) {
      // Capitalization doesn't matter:
      System.out.println(
        r.getString("ID") + ", "
        + r.getString("FIRSTNAME") + ", " 
        + r.getString("LASTNAME")
        + r.getString("STREET")
        + ": " + r.getString("CITY") );
    }
    s.close(); // Also closes ResultSet
  }
} ///:~
