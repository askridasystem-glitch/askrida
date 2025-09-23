<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.webfin.gl.model.AccountView,
                 com.crux.util.validation.FieldValidator,
                 com.webfin.insurance.model.InsuranceZoneLimitView,
                 com.webfin.entity.model.EntityView"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="ZONE_SELECT">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader("SELECT ZONE")%>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr>
                        <td>Search</td>
                        <td>:</td>
                        <td><%=jspUtil.getInputText("key",new FieldValidator("key","Search Key","string",50),null, 200, JSPUtil.MANDATORY)%></td>
                        <td><%=jspUtil.getButtonSubmit("bs","Search")%></td>
                     </tr>
                  </table>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td></td>
                        <td>ZONE ID</td>
                        <td>DESCRIPTION</td>
                        <td>LIMIT</td>
                     </tr>
<%
   final DTOList list = (DTOList) request.getAttribute("LIST");

   if (list!=null) {
      for (int i = 0; i < list.size(); i++) {
         InsuranceZoneLimitView ent = (InsuranceZoneLimitView) list.get(i);

%>                     <tr class=row<%=i%2%>>
                        <td><input type=radio name=x ondblclick="sele(<%=i%>)"></td>
                        <td id=zoneid<%=i%>><%=jspUtil.print(ent.getStZoneID())%></td>
                        <td id=desc<%=i%>><%=jspUtil.print(ent.getStDescription())%></td>
                        <td id=limit<%=i%>><%=jspUtil.print(ent.getDbLimit1())%></td>
                     </tr>
<% }}%>
                  </table>
               </td>
            </tr>
            <tr>
               <td>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>
<script>
   function sele(x) {
      dialogReturn(
         {
          zoneid:document.getElementById('zoneid'+x).innerText,
          desc:document.getElementById('desc'+x).innerText,
          limit:document.getElementById('limit'+x).innerText
         }
      );
      window.close();
   }
</script>