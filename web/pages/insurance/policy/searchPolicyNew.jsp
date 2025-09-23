<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.validation.FieldValidator,
                 com.crux.util.DTOList,
                 com.webfin.insurance.model.InsurancePolicyView"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
<%
   final String key = request.getParameter("key");
   
   final String lks = request.getParameter("lks");
   
   final String costcenter = request.getParameter("costcenter");
   
   //final String arsid = request.getParameter("arsid");

   final DTOList list = (DTOList) request.getAttribute("LIST");
%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="INS_POLICY_SEARCH">
         <input type=hidden name=costcenter value="<%=costcenter%>">
         
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader("SEARCH POLICY")%>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr>
                        <td>Nomor Polis</td>
                        <td>:</td>
                        <td><%=jspUtil.getInputText("key",new FieldValidator("Search Key","string",50),key,200)%></td>
                     </tr>
                     <tr>
                         <td colspan="3"><%=jspUtil.getButtonSubmit("bs","Search")%></td>
                     </tr>
                      
                  </table>
               </td>
            </tr>

            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td></td>
                        <td>POL ID</td>
                        <td>Status</td>
                        <td>Nomor Polis</td>
                        <td>Premi</td>
                     </tr>
<%
   for (int i = 0; i < list.size(); i++) {
      InsurancePolicyView pol = (InsurancePolicyView) list.get(i);

%>
                     
                     <tr class=row<%=i%2%>>
                          <td><input type=radio name=x ondblclick="sele(<%=i%>)"></td>
                        <td id=polid<%=i%>><%=jspUtil.print(pol.getStPolicyID())%></td>
                        <td><%=jspUtil.print(pol.getStStatus())%></td>
                        <td id=polno<%=i%>><%=jspUtil.print(pol.getStPolicyNo())%></td>
                        <td ><%=jspUtil.printX(pol.getDbPremiTotal(),2)%></td>
                     </tr>
<% } %>
                  </table>
               </td>
            </tr>

         </table>
      </form>
   </body>
</html>
<script>
   function selectInvoice(o) {
      dialogReturn({POL_ID:o});
      window.close();
   }
   
   function sele(x) {
      var polidx =document.getElementById('polid'+x).innerText;
      if (polidx=='') {
         //f.polno.value = document.getElementById('polno'+x).innerText;
      }
      
      dialogReturn(
         {
          polid:polidx,
          polno:document.getElementById('polno'+x).innerText
         }
      );
      
      window.close();
   }
</script>