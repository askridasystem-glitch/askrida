<%@ page import="com.crux.util.JSPUtil,
                 com.webfin.gl.model.AccountView,
                 com.crux.util.validation.FieldValidator,
                 com.webfin.gl.codes.GLCodes,
                 com.crux.util.DTOList"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
<%
   final AccountView ac = (AccountView) request.getAttribute("ACCOUNT");
   final char cf = (ac.isModified()?0:JSPUtil.READONLY);
   final DTOList cbAccountType = (DTOList)request.getAttribute("CBAT");

   cbAccountType.setLOValue(ac.getStAccountType());
%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="GL_ACCOUNTS_SAVE">
         <table cellpadding=2 cellspacing=1>
            <%=jspUtil.getHeader("ACCOUNTS")%>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr><td>Account No</td><td>:</td><td><%=jspUtil.getInputText("accountno",new FieldValidator("","Account No","string",32),ac.getStAccountNo(), 200, JSPUtil.MANDATORY|cf)%></td></tr>
                     <tr><td>Description</td><td>:</td><td><%=jspUtil.getInputText("desc",new FieldValidator("","Description","string",128),ac.getStDescription(), 200, JSPUtil.MANDATORY|cf)%></td></tr>
                     <tr><td>Account Type</td><td>:</td><td><%=jspUtil.getInputSelect("actype",new FieldValidator("","Account Type","string",5),cbAccountType, 200, JSPUtil.MANDATORY|cf)%></td></tr>
                     <tr><td>Allocated</td><td>:</td><td><%=jspUtil.getInputCheck("allocflag",ac.getStAllocatedFlag(), "",JSPUtil.MANDATORY|cf)%></td></tr>
                     <tr><td>Opening Balance</td><td>:</td><td><%=jspUtil.getInputText("ob",new FieldValidator("","Opening Balance","money16.2",-1),ac.getDbBalanceOpen(), 200, JSPUtil.READONLY|cf)%></td></tr>
                  </table>
               </td>
            </tr>
            <tr>
               <td>
                  <%=jspUtil.getButtonSubmit("bsave","SAVE","dialogReturn('yay');")%>
                  <%=jspUtil.getButtonNormal("bcancel","CANCEL","window.close();")%>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>