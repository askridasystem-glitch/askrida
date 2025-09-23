<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.crux.util.LOV,
                 com.webfin.gl.model.AccountView,
                 com.crux.util.validation.FieldValidator,
                 com.webfin.entity.model.EntityView"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
   <%
   
   final LOV grupcompany = (LOV) request.getAttribute("grupcompany");

%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="INS_ENTITY_SELECT">
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
                     <tr>
                        <td>Grup</td>
                        <td>:</td>
                        <td colspan="2"><%=jspUtil.getInputSelect("grup|Perusahaan|string",null,grupcompany, 200, JSPUtil.MANDATORY|JSPUtil.NOTEXTMODE)%>
                        </td>
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
          npwp:document.getElementById('npwp'+x).innerText
         }
      );
      window.close();
   }
</script>