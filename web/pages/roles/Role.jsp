<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.crux.login.model.RoleView,
                 com.crux.login.validation.RoleValidator,
                 com.crux.login.model.FunctionsView,
                 com.crux.login.model.FuncRoleView,
                 java.util.Date"%>
<html>
<% final JSPUtil jspUtil = new JSPUtil(request, response); %>
<LINK href="<%=jspUtil.getStyleSheetPath()%>" type=text/css rel=STYLESHEET>
<head>
<%
   RoleView rl = (RoleView) request.getAttribute("ROLE");

   String id = "";
   int nRoot = 0;
   int nSub1 = 0;
   int nSub2 = 0;
   int nSub3 = 0;
   if (rl == null) rl = new RoleView();
   else id = rl.getStRoleID();
   String sTitle = (String) request.getAttribute("ACTION");
   String sDisable = "";
   System.out.println("Action = " + sTitle);
%>
</head><body>
<form name=form1 action="role.ctl" method=POST>
<input type=hidden name=EVENT value="ROLE_SAVE">
<%--<input type=hidden name=roleid value="<%=id%>">--%>
<input type=hidden name=ACTION value="<%=sTitle%>">
<script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
<script language="javascript">
function cekall(nu) {
    var size;

	if (form1.sub1 == null) size = 0; else size = form1.sub1.length;
	for(var i=0; i<size; i++){
	    if (((form1.sub1[i].value).substring(0,2)) == ((form1.root[nu].value).substring(0,2))) {
	 	   form1.sub1[i].checked = form1.root[nu].checked;
		}
	}

	if (form1.sub2 == null) size = 0; else size = form1.sub2.length;
	for(var i=0; i<size; i++){
	    if (((form1.sub2[i].value).substring(0,2)) == ((form1.root[nu].value).substring(0,2))) {
		   form1.sub2[i].checked = form1.root[nu].checked;
		}
	}

	if (form1.sub3 == null) size = 0; else size = form1.sub3.length;
	for(var i=0; i<size; i++){
	    if (((form1.sub3[i].value).substring(0,2)) == ((form1.root[nu].value).substring(0,2))) {
		   form1.sub3[i].checked = form1.root[nu].checked;
		}
	}

}

function cekall1(nu) {

	var size;
	var size1;
	if (form1.sub2 == null) size = 0; else size = form1.sub2.length;
	for(var i=0; i<size; i++){
	    if (((form1.sub2[i].value).substring(0,5)) == ((form1.sub1[nu].value).substring(0,5))) {
		   form1.sub2[i].checked = form1.sub1[nu].checked;
		}
	}

	if (form1.sub3 == null) size = 0; else size = form1.sub3.length;
	for(var i=0; i<size; i++){
	    if (((form1.sub3[i].value).substring(0,5)) == ((form1.sub1[nu].value).substring(0,5))) {
		   form1.sub3[i].checked = form1.sub1[nu].checked;
		}
	}

   return;


	var size = form1.root.length;

	for(var i=0; i<size; i++){
	   if (((form1.root[i].value).substring(0,2)) == ((form1.sub1[nu].value).substring(0,2))) {
	   if (form1.sub1 == null) size1 = 0; else size1 = form1.sub1.length;
	   var isChecked = false;
	   for(var j=0; j<size1; j++){
		   if (((form1.root[i].value).substring(0,2)) == ((form1.sub1[j].value).substring(0,2)) && form1.sub1[j].checked) {
		      isChecked = true;
			  break;
		   }

	   }
	  /* if (form1.sub1[nu].checked) {
	       form1.root[i].checked = form1.sub1[nu].checked;
	   } else {
	   if (isChecked == false ) {
	       form1.root[i].checked = form1.sub1[nu].checked;
	   }

	   }*/

       form1.root[i].checked = isChecked;
	   }
	}
}

function cekall2(nu) {

	var size;
	var size1;
	if (form1.sub3 == null) size = 0; else size = form1.sub3.length;
	for(var i=0; i<size; i++){
	    if (((form1.sub3[i].value).substring(0,8)) == ((form1.sub2[nu].value).substring(0,8))) {
		   form1.sub3[i].checked = form1.sub2[nu].checked;
		}
	}

	if (form1.sub1 == null) size = 0; else size = form1.sub1.length;
	for(var i=0; i<size; i++){
	   if (((form1.sub1[i].value).substring(0,5)) == ((form1.sub2[nu].value).substring(0,5))) {
	   if (form1.sub2 == null) size1 = 0; else size1 = form1.sub2.length;
	   var isChecked = false;
	   for(var j=0; j<size1; j++){
		   if (((form1.sub1[i].value).substring(0,5)) == ((form1.sub2[j].value).substring(0,5)) && form1.sub2[j].checked) {
		      isChecked = true;
			  break;
		   }

	   }
	  /* if (form1.sub2[nu].checked) {
	       form1.sub1[i].checked = form1.sub2[nu].checked;
	   } else {
	   if (isChecked == false ) {
	       form1.sub1[i].checked = form1.sub2[nu].checked;
	   }
	   }*/
       form1.sub1[i].checked = isChecked;
	   }
	}

   return;

	var size = form1.root.length;
	for(var i=0; i<size; i++){
	   if (((form1.root[i].value).substring(0,2)) == ((form1.sub2[nu].value).substring(0,2))) {
	   if (form1.sub1 == null) size1 = 0; else size1 = form1.sub1.length;
	   var isChecked1 = false;
	   for(var j=0; j<size1; j++){
		   if (((form1.root[i].value).substring(0,2)) == ((form1.sub1[j].value).substring(0,2)) && form1.sub1[j].checked) {
		      isChecked1 = true;
			  break;
		   }

	   }
	   /*if (form1.sub2[nu].checked) {
	       form1.root[i].checked = form1.sub2[nu].checked;
	   } else {
	   if (isChecked == false ) {
	       form1.root[i].checked = form1.sub2[nu].checked;
	   }
	   } */
        form1.root[i].checked = isChecked1;
	   }
	}
}

function cekall3(nu) {

    var size;
	var size1;
	if (form1.sub2 == null) size = 0; else size = form1.sub2.length;
	for(var i=0; i<size; i++){
	   if (((form1.sub2[i].value).substring(0,8)) == ((form1.sub3[nu].value).substring(0,8))) {
	   if (form1.sub3 == null) size1 = 0; else size1 = form1.sub3.length;
	   var isChecked = false;
	   for(var j=0; j<size1; j++){
		   if (((form1.sub2[i].value).substring(0,8)) == ((form1.sub3[j].value).substring(0,8)) && form1.sub3[j].checked) {
		      isChecked = true;
			  break;
		   }

	   }

      return;

	   /*if (form1.sub3[nu].checked) {
	       form1.sub2[i].checked = form1.sub3[nu].checked;
	   } else {
	   if (isChecked == false ) {
	       form1.sub2[i].checked = form1.sub3[nu].checked;
	   }
	   } */
       form1.sub2[i].checked = isChecked;
	   }
	}

	var size = form1.sub1.length;
	if (form1.sub1 == null) size = 0; else size = form1.sub1.length;
	for(var i=0; i<size; i++){
	   if (((form1.sub1[i].value).substring(0,5)) == ((form1.sub2[nu].value).substring(0,5))) {
	   if (form1.sub2 == null) size1 = 0; else size1 = form1.sub2.length;
	   var isChecked1 = false;
	   for(var j=0; j<size1; j++){
		   if (((form1.sub1[i].value).substring(0,5)) == ((form1.sub2[j].value).substring(0,5)) && form1.sub2[j].checked) {
		      isChecked1 = true;
			  break;
		   }

	   }
	   /*if (form1.sub2[nu].checked) {
	       form1.sub1[i].checked = form1.sub2[nu].checked;
	   } else {
	   if (isChecked == false ) {
	       form1.sub1[i].checked = form1.sub2[nu].checked;
	   }
	   } */
       form1.sub1[i].checked = isChecked1;
	   }
	}

	var size = form1.root.length;
	for(var i=0; i<size; i++){
	   if (((form1.root[i].value).substring(0,2)) == ((form1.sub2[nu].value).substring(0,2))) {
	   if (form1.sub1 == null) size1 = 0; else size1 = form1.sub1.length;
	   var isChecked2 = false;
	   for(var j=0; j<size1; j++){
		   if (((form1.root[i].value).substring(0,2)) == ((form1.sub1[j].value).substring(0,2)) && form1.sub1[j].checked) {
		      isChecked2 = true;
			  break;
		   }

	   }
	   /*if (form1.sub2[nu].checked) {
	       form1.root[i].checked = form1.sub2[nu].checked;
	   } else {
	   if (isChecked == false ) {
	       form1.root[i].checked = form1.sub2[nu].checked;
	   }
	   } */
       form1.root[i].checked = isChecked2;
	   }
	}
}
</script>
<table cellpadding=1 cellspacing=1>
<%=jspUtil.getHeader(sTitle+" ROLE ")%>
<tr>
	<td width="30%"> <br></td>
</tr>
<tr>
	<td class="row0">
		Role ID
	</td>
	<td > <%if (sTitle.equals("VIEW")) { sDisable = "disabled";
    %>
    <%=jspUtil.print(rl.getStRoleName())%>
    <%} else {%>
    <%=jspUtil.getInputText("roleid", RoleValidator.vfRoleID, rl.getStRoleID(), 200, JSPUtil.MANDATORY|(!rl.isNew()?JSPUtil.READONLY:0))%>
    <%}%>
 	</td>
</tr>
<tr>
	<td class="row0">
		Role Name
	</td>
	<td > <%if (sTitle.equals("VIEW")) { sDisable = "disabled";
    %>
    <%=jspUtil.print(rl.getStRoleName())%>
    <%} else {%>
    <%=jspUtil.getInputText("rolename", RoleValidator.vfRoleName, rl.getStRoleName(), 200, JSPUtil.MANDATORY)%>
    <%}%>
 	</td>
</tr>
<tr>
	<td class="row1">
		Active Date
	</td>
	<td > <%if (sTitle.equals("CREATE")) {%>
    <%=jspUtil.getDisplayText("activedate", RoleValidator.vfActiveDate, new Date(), 100)%>
    <%} else if (sTitle.equals("VIEW")) {%>
    <%=jspUtil.print(rl.getDtActiveDate())%>
    <%} else {%>
    <%=jspUtil.getDisplayText("activedate", RoleValidator.vfActiveDate, rl.getDtActiveDate(), 100)%>
    <%}%>
 	</td>
</tr>
<tr>
	<td class="row0">
		InActive Date
	</td>
	<td > <%if (sTitle.equals("VIEW")) {%>
    <%=jspUtil.print(rl.getDtInActiveDate())%>
    <%} else {%>
    <%=jspUtil.getInputText("inactivedate", RoleValidator.vfInActiveDate, rl.getDtInActiveDate(), 100)%>
    <%}%>
 	</td>
</tr>
<tr>
	<td class="row0">
		Transaction Limit
	</td>
	<td > <%if (sTitle.equals("VIEW")) {%>
    <%=jspUtil.print(rl.getDbTransactionLimit(),2)%>
    <%} else {%>
    <%=jspUtil.getInputText("translimit|Transaction Limit|money16.2", null, rl.getDbTransactionLimit(), 100,JSPUtil.MANDATORY)%>
    <%}%>
 	</td>
</tr>
<br>
<tr><td colspan=4>
<table cellpadding=1 cellspacing=1>
<tr>
   <td width=500></td>
</tr>
<tr>
   <td class="header">Resource</td>
</tr>
<%   System.out.println(rl.getStRoleName());
   final DTOList func = (DTOList)request.getAttribute("FUNC_LIST");



   if  (func == null) {
       System.out.println("no record");
   } else {
   for (int i = 0; i < func.size(); i++) {
       FunctionsView fv = (FunctionsView) func.get(i);
       String kode = fv.getStFunctionID();
       String name = fv.getStFunctionName();
       String selected = "";
        if (sTitle.equalsIgnoreCase("EDIT") || sTitle.equalsIgnoreCase("VIEW")) {
          final DTOList funcrole = (DTOList)request.getAttribute("FUNCROLE_LIST");
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
          <td class="row0"><input type="checkbox" name="root" <%=sDisable%> <%=selected%> value='<%=kode%>' onclick="cekall(<%=nRoot -1%>)"><%=name%>
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
</td></tr>
</table>
<table cellspacing=1 cellpadding=1>
<tr>
   <td colspan=3 width=600></td>
</tr>
<tr>
<%  if (sTitle.equals("VIEW")) { %>
    <td colspan=2>
		<br><br>
      <%=jspUtil.getButtonNormal("bclose","Close","window.location='"+jspUtil.getControllerURL("ROLE_LIST")+"'")%>
	</td>
<% } else { %>
	<td colspan=2>
		<br><br>
      <%=jspUtil.getButtonSubmit("bsave","Submit") %>
      <%=jspUtil.getButtonNormal("bcancel","Cancel","window.location='"+jspUtil.getControllerURL("ROLE_LIST")+"'")%>
	</td>
<%  }%>
</tr>
</table>
</form>
</body>
</html>
