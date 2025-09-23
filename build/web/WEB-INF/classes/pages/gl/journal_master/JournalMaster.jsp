<%@ page import="com.crux.util.JSPUtil,
                 com.webfin.gl.model.JournalMasterView,
                 com.crux.util.validation.FieldValidator,
                 com.webfin.gl.codes.GLCodes,
                 com.crux.util.LOV"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
<%
   final JournalMasterView jm = (JournalMasterView )request.getAttribute("JM");

   final char jflags = jm.isModified()?0:JSPUtil.READONLY;

   final LOV luJType = GLCodes.JournalType.getLookUp().setLOValue(jm.getStJournalType());
   final LOV luJF = GLCodes.JournalFreq.getLookUp().setLOValue(jm.getStJournalFreq());

%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="GL_J_MASTER_SAVE">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader("JOURNAL MASTER")%>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr><td>Journal Code</td><td>:</td><td><%=jspUtil.getInputText("journalcode",new FieldValidator("","Journal Code","string",5),jm.getStJournalCode(),80,JSPUtil.MANDATORY|jflags)%></td></tr>
                     <tr><td>Description</td><td>:</td><td><%=jspUtil.getInputText("desc",new FieldValidator("","Description","string",128),jm.getStDescription(),200,JSPUtil.MANDATORY|jflags)%></td></tr>
                     <tr><td>Journal Type</td><td>:</td><td><%=jspUtil.getInputSelect("jt",new FieldValidator("","Journal Type","string",5),luJType,200,JSPUtil.MANDATORY|jflags)%></td></tr>
                     <tr><td>Journal Freq</td><td>:</td><td><%=jspUtil.getInputSelect("jf",new FieldValidator("","Journal Freq","string",5),luJF,200,jflags)%></td></tr>
                     <tr><td>Journal End Date</td><td>:</td><td><%=jspUtil.getInputText("enddate",new FieldValidator("","End Date","date",-1),jm.getDtEndDate(),100,jflags)%></td></tr>
                     <tr><td>Last Posted</td><td>:</td><td><%=jspUtil.getInputText("lastposted",new FieldValidator("","Last Posted","date",-1),jm.getDtLastPosted(),100,jflags)%></td></tr>
                  </table>
               </td>
            </tr>

            <tr>
               <td>
                  <%=jm.isModified()?jspUtil.getButtonSubmit("bsave","Save"):""%>
                  <%=jspUtil.getButtonNormal("bcancel",jm.isModified()?"Cancel":"Back","submitEvent('GL_J_MASTER')")%>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>