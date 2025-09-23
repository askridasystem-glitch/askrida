<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.webfin.insurance.model.InsurancePolicyView"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="">
         <input type=hidden name=polid value="">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader("INSURANCE POLICIES")%>
               </td>
            </tr>
            <tr>
               <td>
                 <table cellpadding=2 cellspacing=1>
                    <tr class=header>
                       <td></td>
                       <td>POLICY NO</td>
                       <td>CURRENCY</td>
                       <td>AMOUNT</td>
                    </tr>
<%
   final DTOList list = (DTOList)request.getAttribute("LIST");

   for (int i = 0; i < list.size(); i++) {
      InsurancePolicyView ip = (InsurancePolicyView) list.get(i);

%>
                     <tr class=row<%=i%2%>>
                       <td><input type=radio name=x onclick="f.polid.value='<%=jspUtil.print(ip.getStPolicyID())%>'"></td>
                       <td><%=jspUtil.print(ip.getStPolicyNo())%></td>
                       <td><%=jspUtil.print(ip.getStCurrencyCode())%></td>
                       <td></td>
                    </tr>
<% } %>
                 </table>
               </td>
            </tr>
            <tr>
               <td>
                  <%=jspUtil.getButtonSubmit("bcr","Create","f.EVENT.value='INS_POL_CREATE'")%>
                  <%=jspUtil.getButtonSubmit("bed","Edit","f.EVENT.value='INS_POL_EDIT'")%>
                  <%=jspUtil.getButtonSubmit("bvw","View","f.EVENT.value='INS_POL_VIEW'")%>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>