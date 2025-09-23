<%@ page import="com.crux.util.SQLUtil,
                 java.sql.PreparedStatement,
                 java.sql.ResultSet,
                 com.crux.util.ResultSetIterator,
                 com.crux.login.model.UserSessionView,
                 com.crux.util.ListUtil,
                 com.crux.login.model.FunctionsView,
                 com.crux.util.DTOList,
                 javax.sql.DataSource"%>
<%

   final SQLUtil S1 = new SQLUtil();

   final PreparedStatement PS = S1.setQuery("select * from s_functions");

   final ResultSet RS = PS.executeQuery();

   final ResultSetIterator rsi = new ResultSetIterator(RS,FunctionsView.class,0,-1,true);

   System.out.println("PS handle = "+PS);
   System.out.println("RS handle = "+RS);

   rsi.hasNext(); rsi.next();
   rsi.hasNext(); rsi.next();

   final SQLUtil S2 = new SQLUtil();

   S2.getConnection();

   final PreparedStatement PS2 = S2.setQuery("select * from s_users");

   final ResultSet RS2 = PS2.executeQuery();

   System.out.println("PS2 handle = "+PS2);
   System.out.println("RS2 handle = "+RS2);

   final DTOList l = ListUtil.getDTOListFromResultSet(RS2,UserSessionView.class, 0,-1);

   //S2.release();


   System.out.println("col[1] of RS1="+RS.getMetaData().getColumnName(1));
   System.out.println("col[1] of RS2="+RS2.getMetaData().getColumnName(1));

   rsi.hasNext();System.out.println(rsi.next());
   rsi.hasNext();System.out.println(rsi.next());
   rsi.hasNext();System.out.println(rsi.next());

%>