<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.webfin.gl.model.AccountView,
                 com.crux.util.validation.FieldValidator,
                 com.webfin.entity.model.EntityView"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="INS_AGENT_SELECT">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader2("SELECT ENTITIES")%>
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
                        <td>ENT ID</td>
                        <td>NAME</td>
                        <td>NPWP</td>
                        <td>Tax Code</td>
                     </tr>
<%
   final DTOList list = (DTOList) request.getAttribute("LIST");

   if (list!=null) {
      for (int i = 0; i < list.size(); i++) {
         EntityView ent = (EntityView) list.get(i);

%>                     <tr class=row<%=i%2%>>
                        <td><input type=radio name=x ondblclick="sele(<%=i%>)"></td>
                        <td id=entid<%=i%>><%=jspUtil.print(ent.getStEntityID())%></td>
                        <td id=entname<%=i%>><%=jspUtil.print(ent.getStEntityName())%></td>
                        <td id=npwp<%=i%>><%=jspUtil.print(ent.getStTaxFile())%></td>
                        <td id=taxcode<%=i%>><%=jspUtil.print(ent.getStTaxCode())%></td>
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
          entid:document.getElementById('entid'+x).innerText,
          entname:document.getElementById('entname'+x).innerText,
          npwp:document.getElementById('npwp'+x).innerText,
          taxcode:document.getElementById('taxcode'+x).innerText
         }
      );
      window.close();
   }
</script>