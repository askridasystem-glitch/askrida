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

   final String month = request.getParameter("month");
   final String year = request.getParameter("year");
   
%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="GL_ACCOUNTS_SELECT_NOKASBANK">
         <input type=hidden name=acno value="GL_ACCOUNTS_SELECT_NOKASBANK">
         <input type=hidden name=acccode value=<%=form.getStMethod()%>>
         <input type=hidden name=month value=<%=month%>>
         <input type=hidden name=year value=<%=year%>>
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <%=jspUtil.getHeaderOnly("PILIH REKENING")%>
               </td>
            </tr>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr>
                        <td>Cabang</td>
                        <td>:</td>
                     	<td><%=jspUtil.getInputSelect("costcenter|Branch|string",null,CostCenterManager.getInstance().getCostCenterLOV().setLOValue(form.getStCostCenter()),200,JSPUtil.READONLY)%></td>

                     </tr>
                     <tr>
                        <td>Kode Akun</td>
                        <td>:</td>
                        <td><%=jspUtil.getInputText("accountno",new FieldValidator("accountno","Search Key","string",50),form.getStAccountNo(), 200)%></td>
                     </tr>
                     <tr>
                        <td>Nama Akun</td>
                        <td>:</td>
                        
                        <td><%=jspUtil.getInputText("key",new FieldValidator("key","Search Key","string",50),form.getStKey(), 200)%></td>
                     </tr>
                     
                  </table>
                  <table cellpadding=2 cellspacing=1>
                     <tr>
                        <td>
                           <%=jspUtil.getButtonSubmit("bs","Cari")%>
                        </td>
                     <td>
                           <%=jspUtil.getButtonEvent("Buat Baru","CREATE_ACCOUNTCODE")%>
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
                        <td>NO REK.</td>
                        <td>DESC</td>
                        <td>SALDO</td>
                        <td>DIVISI</td>
                     </tr>
<%
   final DTOList list = (DTOList) request.getAttribute("LIST");

   if (list!=null) {
      for (int i = 0; i < list.size(); i++) {
         AccountView ac = (AccountView) list.get(i);
         
         boolean enabled = true;

                                     if(ac.getStEnabled()!=null){
                                         if(ac.getStEnabled().equalsIgnoreCase("N"))
                                            enabled = false;
                                     }
         		

%>                     <tr class=row<%=i%2%>>
                          <td><%if(enabled){%><input type=radio name=x ondblclick="sele(<%=i%>)"><%}%></td>
                        <%--<td><input type=radio name=x ondblclick="sele(<%=i%>)"></td>
						--%>
                        <td id=acid<%=i%>><%=jspUtil.print(ac.getLgAccountID())%></td>
                        <td id=acno<%=i%>><%=jspUtil.print(ac.getStAccountNo())%></td>
                        <td id=rekno<%=i%>><%=jspUtil.print(ac.getStRekeningNo())%></td>
                        <td id=desc<%=i%>><%=jspUtil.print(ac.getStDescription())%></td>
                        <td id=desc<%=i%>><%=jspUtil.print(ac.getDbBalanceOpen(),2)%></td>
                        <td id=div<%=i%>><%=jspUtil.print(ac.getStDivisionCode())%></td>
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
      var acidx =document.getElementById('acid'+x).innerText;
      if (acidx=='') {
         //alert('invoking auto generate account');
         f.acno.value = document.getElementById('acno'+x).innerText;
         //f.EVENT.value='GL_ACCOUNTS_AUTOCRT';
         //f.submit();
         //return;
      }
      
      //costcenter = document.getElementById('costcenter2');
      
      dialogReturn(
         {
          acid:acidx,
          acno:document.getElementById('acno'+x).innerText,
          desc:document.getElementById('desc'+x).innerText,
          div:document.getElementById('div'+x).innerText,
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