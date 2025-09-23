<%@ page import="com.crux.util.JSPUtil,
                 com.webfin.gl.model.AccountView,
                 com.crux.util.validation.FieldValidator,
                 com.webfin.gl.codes.GLCodes,
                 com.crux.util.DTOList,
                 com.webfin.gl.model.PeriodHeaderView,
                 com.webfin.gl.model.PeriodDetailView,
                 com.crux.util.LookUpUtil"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
<%
   final PeriodHeaderView ph = (PeriodHeaderView) request.getAttribute("PERIOD");
   final char cf = (ph.isModified()?0:JSPUtil.READONLY);

   final LookUpUtil luyears = GLCodes.Years.getLookUp();
   luyears.setLOValue(String.valueOf(ph.getStFiscalYear()));
   final LookUpUtil luper = GLCodes.GLPeriods.getLookUp();
   luper.setLOValue(String.valueOf(ph.getLgPeriodNum()));
%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="GL_PERIODS_SAVE">
         <table cellpadding=2 cellspacing=1>
            <%=jspUtil.getHeader("PERIOD HEADER")%>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr><td>Fiscal Year</td><td>:</td><td><%=jspUtil.getInputSelect("fiscal",new FieldValidator("","Fiscal Year","integer",5),luyears, 100, JSPUtil.MANDATORY|cf)%></td></tr>
                     <tr><td>Period Num</td><td>:</td><td><%=jspUtil.getInputSelect("periodnum",new FieldValidator("","Period No","integer",2).setRange(new Integer(1),new Integer(13)),luper, 100, JSPUtil.MANDATORY|cf)%></td></tr>
                  </table>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td>PERIOD</td>
                        <td>START DATE</td>
                        <td>END DATE</td>
                     </tr>
<%
   final DTOList details = ph.getDetails();

   final long pernum = ph.getLgPeriodNum().longValue();

   for (int i = 0; i < pernum; i++) {
      PeriodDetailView pd = (PeriodDetailView) details.get(i);
      final boolean islast = i==pernum-1;
%>
                     <tr class=row<%=i%2%>>
                        <td><%=i+1%></td>
                        <td><%=jspUtil.getInputText("pstart"+i,new FieldValidator("","Start Date","date",-1),pd.getDtStartDate(),100,cf)%></td>
                        <td><%=!islast?"":jspUtil.getInputText("pend"+i,new FieldValidator("","End Date","date",-1),pd.getDtEndDate(),100,cf)%></td>
                     </tr>
<% } %>
                  </table>
               </td>
            </tr>
            <tr>
               <td>
                  <%=jspUtil.getButtonSubmit("bsave","SAVE","")%>
                  <%=jspUtil.getButtonNormal("bcancel","CANCEL","window.close();")%>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>