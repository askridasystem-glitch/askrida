<%@ page import="com.crux.util.JSPUtil"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response,"NOMERATOR"); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <br>
                  Enter Nomerator : <%=jspUtil.getInputText("nom|Nomerator|string", null,null,100)%>
                  <%=jspUtil.getButtonSubmit("b","OK")%>
                  <%=jspUtil.getButtonNormal("b","Cancel","bcancel();")%>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>
<script>
   f.onsubmit=function() {
      dialogReturn(f.nom.value);
      window.close();
   }

   function bcancel() {
      window.close();
   }
</script>