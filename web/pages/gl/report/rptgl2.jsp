<%@ page import="com.crux.util.JSPUtil,
                 com.webfin.gl.model.GLReportView,
                 com.crux.util.DTOList,
                 com.webfin.gl.model.GLReportLineView,
                 com.webfin.gl.model.GLReportColumnView,
                 com.crux.util.BDUtil,
                 java.util.ArrayList,
                 com.crux.util.CharRenderer,
                 java.math.BigDecimal"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
<%
   final GLReportView glrpt = (GLReportView) request.getAttribute("RPT");

   final DTOList columns = glrpt.getColumns();

   final CharRenderer charrend = new CharRenderer();

   for (int i = 0; i < columns.size(); i++) {
      GLReportColumnView col = (GLReportColumnView) columns.get(i);

      charrend.print(col.getLgColumnPosition(), col.getStColumnHeader());
   }

   charrend.newLine();

   final ArrayList lines = glrpt.getResult();

   for (int i = 0; i < lines.size(); i++) {
      Object[] line = (Object []) lines.get(i);

      for (int j = 0; j < columns.size(); j++) {
         GLReportColumnView col = (GLReportColumnView) columns.get(j);

         Object val = j>=line.length?null:line[j];

         if (val==null) val="";

         if (val instanceof BigDecimal) {
            //val = col.format(val);
            if (col.getStColumnFormat()!=null) {
               val = col.format(val);
            }
         }

         charrend.print(col.getLgColumnPosition(), String.valueOf(val));
      }

      charrend.newLine();
   }

%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader("RPT")%>
               </td>
            </tr>
            <tr>
               <td>
                  <pre>
<%
   final int lc = charrend.getLineCount();

   for (int i=0;i<lc;i++) {
      final String ln = charrend.getLine(i);
      out.println(ln);
   }

%>
                  </pre>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>