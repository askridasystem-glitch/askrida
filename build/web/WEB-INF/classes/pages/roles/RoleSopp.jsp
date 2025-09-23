<%@ page import="com.crux.login.model.RoleView,
                 com.crux.login.model.FunctionsView,
                 com.crux.util.DTOList,
                 com.crux.login.model.FuncRoleView,
                 com.mandiri.sopp.so.form.SOForm,
                 com.mandiri.sopp.so.model.SalesOrderView,
                 com.crux.login.form.RoleForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="ROLE">
<%
   RoleForm form = (RoleForm)frame.getForm();

   RoleView rl = form.getRole();

   //RoleView rl = (RoleView) request.getAttribute("ROLE");

   String id = "";
   int nRoot = 0;
   int nSub1 = 0;
   int nSub2 = 0;
   int nSub3 = 0;
   if (rl == null) rl = new RoleView();
   else id = rl.getStRoleID();
   String sTitle = rl.isNew()?"NEW":(rl.isUpdate()?"EDIT":"VIEW");//(String) request.getAttribute("ACTION");
   String sDisable = "";
   if (sTitle.equals("VIEW")) { sDisable = "disabled";}
   System.out.println("Action = " + sTitle);
%>
<input type=hidden name=EVENT value="ROLE_SAVE">
<input type=hidden name=ACTION value="<%=sTitle%>">
<script language="javascript">
function cekall(nu) {
    var size;

	if (f.sub1 == null) size = 0; else size = f.sub1.length;
	for(var i=0; i<size; i++){
       if ( ((f.sub1[i].value).substring(0,2)) == ((f.root[nu].value).substring(0,2)) ) {
	 	   f.sub1[i].checked = f.root[nu].checked;
		}
	}

	if (f.sub2 == null) size = 0; else size = f.sub2.length;
	for(var i=0; i<size; i++){
	    if (((f.sub2[i].value).substring(0,2)) == ((f.root[nu].value).substring(0,2))) {
		   f.sub2[i].checked = f.root[nu].checked;
		}
	}

	if (f.sub3 == null) size = 0; else size = f.sub3.length;
	for(var i=0; i<size; i++){
	    if (((f.sub3[i].value).substring(0,2)) == ((f.root[nu].value).substring(0,2))) {
		   f.sub3[i].checked = f.root[nu].checked;
		}
	}

}

function cekall1(nu) {

	var size;
	var size1;
	if (f.sub2 == null) size = 0; else size = f.sub2.length;
	for(var i=0; i<size; i++){
	    if (((f.sub2[i].value).substring(0,5)) == ((f.sub1[nu].value).substring(0,5))) {
		   f.sub2[i].checked = f.sub1[nu].checked;
		}
	}

	if (f.sub3 == null) size = 0; else size = f.sub3.length;
	for(var i=0; i<size; i++){
	    if (((f.sub3[i].value).substring(0,5)) == ((f.sub1[nu].value).substring(0,5))) {
		   f.sub3[i].checked = f.sub1[nu].checked;
		}
	}

   return;


	var size = f.root.length;

	for(var i=0; i<size; i++){
	   if (((f.root[i].value).substring(0,2)) == ((f.sub1[nu].value).substring(0,2))) {
	   if (f.sub1 == null) size1 = 0; else size1 = f.sub1.length;
	   var isChecked = false;
	   for(var j=0; j<size1; j++){
		   if (((f.root[i].value).substring(0,2)) == ((f.sub1[j].value).substring(0,2)) && f.sub1[j].checked) {
		      isChecked = true;
			  break;
		   }

	   }
	  /* if (f.sub1[nu].checked) {
	       f.root[i].checked = f.sub1[nu].checked;
	   } else {
	   if (isChecked == false ) {
	       f.root[i].checked = f.sub1[nu].checked;
	   }

	   }*/

       f.root[i].checked = isChecked;
	   }
	}
}

function cekall2(nu) {

	var size;
	var size1;
	if (f.sub3 == null) size = 0; else size = f.sub3.length;
	for(var i=0; i<size; i++){
	    if (((f.sub3[i].value).substring(0,8)) == ((f.sub2[nu].value).substring(0,8))) {
		   f.sub3[i].checked = f.sub2[nu].checked;
		}
	}

	if (f.sub1 == null) size = 0; else size = f.sub1.length;
	for(var i=0; i<size; i++){
	   if (((f.sub1[i].value).substring(0,5)) == ((f.sub2[nu].value).substring(0,5))) {
	   if (f.sub2 == null) size1 = 0; else size1 = f.sub2.length;
	   var isChecked = false;
	   for(var j=0; j<size1; j++){
		   if (((f.sub1[i].value).substring(0,5)) == ((f.sub2[j].value).substring(0,5)) && f.sub2[j].checked) {
		      isChecked = true;
			  break;
		   }

	   }
	  /* if (f.sub2[nu].checked) {
	       f.sub1[i].checked = f.sub2[nu].checked;
	   } else {
	   if (isChecked == false ) {
	       f.sub1[i].checked = f.sub2[nu].checked;
	   }
	   }*/
       //f.sub1[i].checked = isChecked;
	   }
	}

   return;

	var size = f.root.length;
	for(var i=0; i<size; i++){
	   if (((f.root[i].value).substring(0,2)) == ((f.sub2[nu].value).substring(0,2))) {
	   if (f.sub1 == null) size1 = 0; else size1 = f.sub1.length;
	   var isChecked1 = false;
	   for(var j=0; j<size1; j++){
		   if (((f.root[i].value).substring(0,2)) == ((f.sub1[j].value).substring(0,2)) && f.sub1[j].checked) {
		      isChecked1 = true;
			  break;
		   }

	   }
	   /*if (f.sub2[nu].checked) {
	       f.root[i].checked = f.sub2[nu].checked;
	   } else {
	   if (isChecked == false ) {
	       f.root[i].checked = f.sub2[nu].checked;
	   }
	   } */
        //f.root[i].checked = isChecked1;
	   }
	}
}

function cekall3(nu) {

    var size;
	var size1;
	if (f.sub2 == null) size = 0; else size = f.sub2.length;
	for(var i=0; i<size; i++){
	   if (((f.sub2[i].value).substring(0,8)) == ((f.sub3[nu].value).substring(0,8))) {
	   if (f.sub3 == null) size1 = 0; else size1 = f.sub3.length;
	   var isChecked = false;
	   for(var j=0; j<size1; j++){
		   if (((f.sub2[i].value).substring(0,8)) == ((f.sub3[j].value).substring(0,8)) && f.sub3[j].checked) {
		      isChecked = true;
			  break;
		   }

	   }
	   /*if (f.sub3[nu].checked) {
	       f.sub2[i].checked = f.sub3[nu].checked;
	   } else {
	   if (isChecked == false ) {
	       f.sub2[i].checked = f.sub3[nu].checked;
	   }
	   } */
       //f.sub2[i].checked = isChecked;
	   }
	}

   return;

	var size = f.sub1.length;
	if (f.sub1 == null) size = 0; else size = f.sub1.length;
	for(var i=0; i<size; i++){
	   if (((f.sub1[i].value).substring(0,5)) == ((f.sub2[nu].value).substring(0,5))) {
	   if (f.sub2 == null) size1 = 0; else size1 = f.sub2.length;
	   var isChecked1 = false;
	   for(var j=0; j<size1; j++){
		   if (((f.sub1[i].value).substring(0,5)) == ((f.sub2[j].value).substring(0,5)) && f.sub2[j].checked) {
		      isChecked1 = true;
			  break;
		   }

	   }
	   /*if (f.sub2[nu].checked) {
	       f.sub1[i].checked = f.sub2[nu].checked;
	   } else {
	   if (isChecked == false ) {
	       f.sub1[i].checked = f.sub2[nu].checked;
	   }
	   } */
       f.sub1[i].checked = isChecked1;
	   }
	}

	var size = f.root.length;
	for(var i=0; i<size; i++){
	   if (((f.root[i].value).substring(0,2)) == ((f.sub2[nu].value).substring(0,2))) {
	   if (f.sub1 == null) size1 = 0; else size1 = f.sub1.length;
	   var isChecked2 = false;
	   for(var j=0; j<size1; j++){
		   if (((f.root[i].value).substring(0,2)) == ((f.sub1[j].value).substring(0,2)) && f.sub1[j].checked) {
		      isChecked2 = true;
			  break;
		   }

	   }
	   /*if (f.sub2[nu].checked) {
	       f.root[i].checked = f.sub2[nu].checked;
	   } else {
	   if (isChecked == false ) {
	       f.root[i].checked = f.sub2[nu].checked;
	   }
	   } */
       f.root[i].checked = isChecked2;
	   }
	}
}

function test(){
   alert('test ');
}
</script>
<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <table cellpadding=2 cellspacing=1>
            <c:field width="200" name="role.stRoleID" mandatory="true" caption="Role Id" type="string" presentation="standard" />
            <c:field width="200" name="role.stRoleName" mandatory="true" caption="Role Name" type="string" presentation="standard" />
            <c:field width="200" name="role.dtActiveDate" caption="Active Date" type="date" presentation="standard"  />
            <c:field width="200" name="role.dtInActiveDate" caption="Inactive Date" type="date" presentation="standard"/>
            <c:field width="200" name="role.dbTransactionLimit" mandatory="true" caption="Transaction Limit" type ="money19.0" presentation="standard" />
         </table>
      </td>
   </tr>
   <tr>
      <td>
     <table cellpadding=1 cellspacing=1>
<tr>
   <td width=500></td>
</tr>
<tr>
   <td class="header">Resource</td>
</tr>
<%   System.out.println(rl.getStRoleName());
   final DTOList func = form.getListFunction();

   if  (func == null) {
       System.out.println("no record");
   } else {
   for (int i = 0; i < func.size(); i++) {
       FunctionsView fv = (FunctionsView) func.get(i);
       String kode = fv.getStFunctionID();
       String name = fv.getStFunctionName();
       String selected = "";
        if (sTitle.equalsIgnoreCase("EDIT") || sTitle.equalsIgnoreCase("VIEW")) {
          final DTOList funcrole = form.getListFunctionRole();
          if (funcrole != null) {
              System.out.println("Function Role : "+funcrole.size());
              for (int j = 0; j < funcrole.size(); j++) {
                  FuncRoleView frv = (FuncRoleView) funcrole.get(j);
                  if (frv.getStFuncID().equals(kode)) {
                     selected = "checked";
                     break;
                  }
              }
          }
       }

       System.out.println("Kode => " + kode);
       System.out.println(kode.substring(3));
       if (kode.substring(3).equals("00.00.00")) { nRoot++;

%>
       <tr>
          <td class="row0"><input type="checkbox" name="root" <%=sDisable%> <%=selected%> value='<%=kode%>' onclick="cekall('<%=nRoot -1%>')"><%=name%>
          </td>
       </tr>
<%
       } else if (kode.substring(6).equals("00.00")) { nSub1++; %>
       <tr>
          <td class="row1">&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="sub1" <%=sDisable%> <%=selected%> value='<%=kode%>' onclick="cekall1(<%=nSub1-1%>)"><%=name%>
          </td>
       </tr>
<%
       } else if (kode.substring(9).equals("00")) { nSub2++; %>
       <tr>
          <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="sub2" <%=sDisable%> <%=selected%> value='<%=kode%>' onclick="cekall2(<%=nSub2-1%>)"><%=name%>
          </td>
       </tr>
<%
       } else if (!(kode.substring(9).equals("00"))) { nSub3++; %>
       <tr>
          <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="sub3" <%=sDisable%> <%=selected%> value='<%=kode%>' onclick="cekall3(<%=nSub3%>)"><%=name%>
          </td>
       </tr>
<%
       }

   } // end For
   } // end if
%>
<input type=hidden name=check1 value="<%=nRoot%>">
<input type=hidden name=check2 value="<%=nSub1%>">
<input type=hidden name=check3 value="<%=nSub2%>">
<input type=hidden name=check4 value="<%=nSub3%>">
</table>
      </td>
   </tr>
   <tr>

      <td align=center>
         <c:button text="Submit"  event="doSave" show="<%=!form.isReadOnly()%>"/>
         <c:button text="Cancel"  event="doCancel" show="<%=!form.isReadOnly()%>"/>
         <c:button text="Back"  event="doCancel" show="<%=form.isReadOnly()%>"/>
      </td>
   </tr>
</table>




</c:frame>
