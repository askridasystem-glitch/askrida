<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.crux.util.validation.FieldValidator,
                 com.webfin.gl.accounts.forms.AccountMasterForm,
                 com.webfin.gl.model.AccountView"%>
<html>
<% final JSPUtil jspUtil = new JSPUtil(request, response); %>
<LINK href="<%=jspUtil.getStyleSheetPath()%>" type=text/css rel=STYLESHEET>
<head>
<%

	final AccountView jh = (AccountView)request.getAttribute("ACCOUNT");
	
	final String costcenter = (String) request.getAttribute("costcenter");
	
%>
</head><body>
<form name=f action="ctl.ctl" method=POST>
    <input type=hidden name=EVENT value="GL_ACCOUNT_SAVE">
<script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>

<table cellpadding=1 cellspacing=1>
<%=jspUtil.getHeader("{L-ENGCREATE ACCOUNT CODE-L}{L-INAINPUT KODE AKUN-L}")%>
<tr>
	<td width="30%"> <br></td>
</tr>

<tr><td colspan=2>
<table cellpadding=1 cellspacing=1>
     
   <input type=hidden name=costcenter value=<%=jspUtil.print(costcenter)%>>
   <input type=hidden name=desc value="">  
   <tr><td>{L-ENGMaster Code-L}{L-INAAkun Header-L}</td><td>:</td><td><%=jspUtil.getInputText("accountno",new FieldValidator("","Post Code","string",5),jh.getStAccountNo(), 80, JSPUtil.MANDATORY)%>
   </td>
   </tr>
  <%--<tr><td>{L-ENGAccount Type-L}{L-INATipe Akun-L}</td><td>:</td><td><%=jspUtil.getInputText("accounttype",new FieldValidator("","Region Name","string",128),jh.getStAccountType(),200, JSPUtil.MANDATORY|JSPUtil.READONLY|JSPUtil.NOTEXTMODE)%></td></tr>
 --%>
    <tr><td>{L-ENGGLCode-L}{L-INAGLCode/Bank-L}</td><td>:</td><td><%=jspUtil.getInputText("entityid",new FieldValidator("","City Name","string",5),jh.getStEntityID(), 80, JSPUtil.MANDATORY)%>
    <%=jspUtil.getButtonNormal("bx","...","selectEntity();")%></td></tr>
    <tr><td>{L-ENGEntity-L}{L-INABank-L}</td><td>:</td><td><%=jspUtil.getInputText("entityname",new FieldValidator("","Region Name","string",128),jh.getStEntityName(),200)%></td></tr>
    <tr><td>{L-ENGNo Urut/Jenis-L}{L-INANo Urut/Jenis-L}</td><td>:</td><td><%=jspUtil.getInputText("orderno",new FieldValidator("","No Urut","string",2),null,80)%></td></tr>
   
    <TR><td></td><td></td><td><%=jspUtil.getButtonNormal("bc","Preview Akun Baru","createNoAccount();") %></td></TR>
    <tr><td>{L-ENGNew Account-L}{L-INAAkun Baru-L}</td><td>:</td><td><%=jspUtil.getInputText("accountnew",new FieldValidator("","Region Name","string",128),jh.getStAccountNo2(),200, JSPUtil.MANDATORY)%></td></tr>
   <tr><td>{L-ENGDescription-L}{L-INANama Akun-L}</td><td>:</td><td><%=jspUtil.getInputTextArea("description",new FieldValidator("","Region Name","string",128),jh.getStDescription(),3,3,200, JSPUtil.MANDATORY)%></td></tr>
   <tr><td colspan="3"></td></tr>
   <tr><td></td><td></td>
		
   	<td>
        <%=jspUtil.getButtonSubmit("bsave","Simpan","f.EVENT.value='GL_ACCOUNT_SAVE';window.close()")%>
      	<%=jspUtil.getButtonNormal("bc","Batal","window.close()") %>
      	
	</td>

    </tr>
          
</table>
</td></tr>
</table>

</form>
</body>
<script>
   function selectAccountByBranch(o){
   		//alert(document.getElementById('costcenter').value);
   	    //alert(document.getElementById('method2').value);
   	    //var cabang = document.getElementById('costcenter').value;
   	    //var method = document.getElementById('method2').value;
   		openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECTCRT', 400,400,selectAccount);
   }
   
   function selectAccount(o) {
      if (o==null) return;
      //document.getElementById('acid').value=o.acid;
      document.getElementById('accountno').value=o.acno;
      document.getElementById('accounttype').value=o.actype;
      document.getElementById('desc').value=o.desc;
   }
   
   function selectEntity(o){
   		//alert(document.getElementById('costcenter').value);
   	    //alert(document.getElementById('method2').value);
   	    //var cabang = document.getElementById('costcenter').value;
   	    //var method = document.getElementById('method2').value;
   		openDialog('so.ctl?EVENT=SELECT_ENT', 400,400,selectEnt);
   }
   
   function selectEnt(o) {
      if (o==null) return;
      //document.getElementById('acid').value=o.acid;
      document.getElementById('entityname').value=o.acno;
      document.getElementById('entityid').value=o.desc;
      //document.getElementById('desc').value=o.desc;
   }
   
   function createNoAccount(o){
      var glcode = document.getElementById('entityid').value;
      var accountnew = document.getElementById('entityid').value;
      var nourut = document.getElementById('orderno').value;
      
      if(nourut==''){
            nourut = '00';
      }
      
      if(accountnew==''){
         alert('Belum pilih Account!');
         return;
      }
      
      document.getElementById('accountnew').value = document.getElementById('accountno').value.substring(0,5) + '' + document.getElementById('entityid').value + nourut +' '+document.getElementById('costcenter').value;
      document.getElementById('description').value = document.getElementById('desc').value +' '+document.getElementById('entityname').value

      if(glcode==''){
          document.getElementById('accountnew').value = document.getElementById('accountno').value+' '+document.getElementById('costcenter').value;;
      }
   }
</script>

</html>

