<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.webfin.gl.model.AccountView,
                 com.crux.util.validation.FieldValidator,
                 com.webfin.gl.ejb.CostCenterManager,
                 com.webfin.gl.model.JournalHeaderView,
                 com.webfin.gl.accounts.model.SelectAccountForm"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
<%
   final SelectAccountForm form = (SelectAccountForm) request.getAttribute("FORM");
   
%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="GL_ACCOUNTS_SELECTCRT">
         <input type=hidden name=acno value="GL_ACCOUNTS_SELECTCRT">
         <input type=hidden name=method value=<%=form.getStMethod()%>>
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeader("SELECT ACCOUNTS NOT CREATE")%>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr>
                        <td>Branch</td>
                        <td>:</td>
                        <%--  
                        <td><%=jspUtil.getInputSelect("costcenter|Branch|string",null,CostCenterManager.getInstance().getCostCenterLOV().setLOValue(form.getStCostCenter()),200,JSPUtil.MANDATORY)%></td>
                     	--%>
                     	
                     	<td><%=jspUtil.getInputSelect("costcenter|Branch|string",null,CostCenterManager.getInstance().getCostCenterLOV().setLOValue(form.getStCostCenter()),200,JSPUtil.READONLY)%></td>

                     </tr>
                     <tr>
                        <td>Search</td>
                        <td>:</td>
                        <td><%=jspUtil.getInputText("key",new FieldValidator("key","Search Key","string",50),form.getStKey(), 200, JSPUtil.MANDATORY)%></td>
                     </tr>
                     
                  </table>
                  <table cellpadding=2 cellspacing=1>
                     <tr>
                        <td>
                           <%=jspUtil.getButtonSubmit("bs","Search")%>
                        </td> 
                  </table>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td></td>
                        <td>ACCOUNT ID</td>
                        <td>ACCOUNT NO</td>
                        <td>ACCOUNT TYPE</td>
                        <td>DESC</td>
                     </tr>
<%
   final DTOList list = (DTOList) request.getAttribute("LIST2");

   if (list!=null) {
      for (int i = 0; i < list.size(); i++) {
         AccountView ac = (AccountView) list.get(i);
         
         boolean enabled = false;
         
         if(ac.getStEnabled()!=null){
         	enabled = true;
         }
         		

%>                     <tr class=row<%=i%2%>>
                        <%--  <td><%if(!enabled){%><input type=radio name=x <%if(enabled){%>disabled<%}%> ondblclick="sele(<%=i%>)"><%}%></td>
                        --%><td><input type=radio name=x ondblclick="select2(<%=i%>)"></td>

                        <td id=acid<%=i%>><%=jspUtil.print(ac.getLgAccountID())%></td>
                        <td id=acno<%=i%>><%=jspUtil.print(ac.getStAccountNo())%></td>
                        <td id=actype<%=i%>><%=jspUtil.print(ac.getStAccountType())%></td>
                        <td id=desc<%=i%>><%=jspUtil.print(ac.getStDescription())%></td>
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
   function select2(x) {
     // var acidx =document.getElementById('acidd'+x).innerText;
      //if (acidx=='') {
       //  alert('cumi auto generate account');
       //  f.acno.value = document.getElementById('acno'+x).innerText;
       //  f.submit();
      //   return;
      //}
      
      //costcenter = document.getElementById('costcenter2');
      
      dialogReturn(
         {
          //acid:acidx,
          acno:document.getElementById('acno'+x).innerText,
          desc:document.getElementById('desc'+x).innerText,
          actype:document.getElementById('actype'+x).innerText,
          acdesc:(document.getElementById('acno'+x).innerText+'/'+document.getElementById('desc'+x).innerText)
         }
      );
      
      window.close();
   }
</script>
<script>
//var costcenter = document.getElementById('costcenter').value;
//alert(costcenter);
</script>