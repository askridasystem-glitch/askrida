<%@ page import="javax.naming.InitialContext,
                 javax.sql.DataSource,
                 java.sql.Connection,
                 java.sql.PreparedStatement,
                 java.sql.ResultSet"%>
<%

   final InitialContext ic = new InitialContext();

   final DataSource ds = (DataSource)ic.lookup("java:/TelkomselDS");

   final Connection con = ds.getConnection();

   final PreparedStatement PS1 = con.prepareStatement("select * from s_users");

   final ResultSet RS1 = PS1.executeQuery();

   RS1.next();

   System.out.println("RS1[1]="+RS1.getString(1));

   final PreparedStatement PS2 = con.prepareStatement("select * from s_functions");

   final ResultSet RS2 = PS2.executeQuery();

   while (RS2.next())
      System.out.println("RS2[1]="+RS2.getString(1));

   RS1.next();

   System.out.println("RS1[1]="+RS1.getString(1));

   con.close();
%>