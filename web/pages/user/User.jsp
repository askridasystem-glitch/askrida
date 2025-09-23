<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.crux.util.LookUpUtil,
                 java.util.Date,
                 com.crux.login.model.UserSessionView,
                 com.crux.login.validation.UserValidator,
                 com.crux.login.model.UserRoleView,
                 com.crux.login.model.UserVendorView,
                 com.crux.util.LOV,
                 com.crux.lov.LOVManager"%>
<html>
<% final JSPUtil jspUtil = new JSPUtil(request, response); %>
<LINK href="<%=jspUtil.getStyleSheetPath()%>" type=text/css rel=STYLESHEET>
<head>
<%
   UserSessionView usr = (UserSessionView) request.getAttribute("USER");
   //final LookUpUtil usrTyp = (LookUpUtil) request.getAttribute("USERTYPE");
   final DTOList vcombo = (DTOList) request.getAttribute("VENDOR");
   final DTOList rlcombo = (DTOList) request.getAttribute("ROLE");
   final Boolean isVendor = (Boolean)request.getAttribute("isVendor");
   final DTOList userrole = usr.getUserroles();
   String id = "";
   if (usr == null) usr = new UserSessionView();
   else id = usr.getStUserID();
   String sTitle = (String) usr.getAttribute("ACTION");
   String ut = usr.getStUserType();
   System.out.println("Action = " + sTitle);
%>
</head>
<script language="javascript">
 function addRole(){

    if (form1.roleid.value != "") {

       if (form1.ACTION.value == "CREATE" && form1.userid.value == "") {
          alert("User ID should not be empty");
       } else {
          form1.EVENT.value = 'USER_ADD_ROLE';
          form1.submit();
	   }
	} else {
	   alert("Role should be selected");
	}

  }
  function delRole(roleid){

    form1.EVENT.value = 'USER_DEL_ROLE';
    form1.roleselect.value = roleid;
    form1.submit();
  }

function selectVendorCallBack(o) {
      if (o!=null) {
         form1.vendorDesc.value = o.DESC;
         form1.vendorid.value = o.VENDORID;
      }
   }

   function selectVendor() {
      openDialog('so.ctl?EVENT=VENDOR_SEARCH', 300,400,selectVendorCallBack);
   }


 /* function cekcbbranch() {
	 form1.branchcode.value = form1.branchid.value;
    form1.EVENT.value = 'USER_SEL_BRANCH';
	 form1.submit();
  }*/

  function cekpassword() {
     if (form1.password.value != form1.repassword.value) {
        return "Retype password not valid";
	 } else { return true; }
  }
</script>
<body>
<form name=form1 action="user.ctl" method=POST onsubmit="return validate();">
<input type=hidden name=EVENT value="USER_SAVE">
<input type=hidden name=usrid value="<%=id%>">
<input type=hidden name=roleselect>
<input type=hidden name=branchcode>
<input type=hidden name=ACTION value="<%=sTitle%>">
<script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>

<table cellpadding=1 cellspacing=1>
<%=jspUtil.getHeader(sTitle+" USER PROFILE")%>
<tr>
	<td width="30%"><br></td>
</tr>
<tr>
	<td class="row0">
		User ID
	</td>
	<td > <%if (sTitle.equals("EDIT")) {
    %>
    <%=jspUtil.print(usr.getStUserID())%>
    <%} else {%>
    <%=jspUtil.getInputText("userid", UserValidator.vfUserID, usr.getStUserID(), 100, JSPUtil.MANDATORY)%>
    <%}%>
 	</td>
</tr>
<!--tr>
	<td class="row1">
		User Type
	</td>
	<td >
    <%
      //out.println(jspUtil.getInputSelect("usertype", UserValidator.vfUserType, usrTyp.getComboContent(usr.getStUserType()), 200,JSPUtil.MANDATORY));
  %>
 	</td>
</tr-->
<tr>
	<td class="row0">
		User Name
	</td>
	<td >
    <%=jspUtil.getInputText("username", UserValidator.vfUserName, usr.getStUserName(), 200,JSPUtil.MANDATORY) %>
 	</td>
</tr>
<tr>
	<td class="row0">
        Division
	</td>
	<td >
    <%=jspUtil.getInputText("division", UserValidator.vfDivision, usr.getStDivision(), 200)%>
 	</td>
</tr>
<tr>
	<td class="row1">
        Department
	</td>
	<td >
    <%=jspUtil.getInputText("department", UserValidator.vfDepartment, usr.getStDepartment(), 200)%>
 	</td>
</tr>
<tr>
	<td class="row0">
        Email Address
	</td>
	<td >
    <%=jspUtil.getInputText("email", UserValidator.vfEmail, usr.getStEmail(), 200)%>
 	</td>
</tr>
<%if (sTitle.equals("CREATE")) {%>
<tr>
	<td class="row1">
        Password
	</td>
	<td >
    <%=jspUtil.getInputText("password", UserValidator.vfPassword, usr.getStTempPassword(), 200,JSPUtil.MANDATORY)%>
 	</td>
</tr>
<tr>
	<td class="row0">
        Retype Password
	</td>
	<td >
    <%=jspUtil.getInputText("repassword", UserValidator.vfRePassword, usr.getStTempPassword(), 200,JSPUtil.MANDATORY)%>
 	</td>
</tr>
<%}%>
<tr>
	<td class="row1">
        Phone
	</td>
	<td ><%=jspUtil.getInputText("phone", UserValidator.vfPhone, usr.getStPhone(), 200)%>
 	</td>
</tr><tr>
	<td class="row1">
        Mobile
	</td>
	<td ><%=jspUtil.getInputText("mobile", UserValidator.vfPhone, usr.getStMobileNumber(), 200)%>
 	</td>
</tr>
<tr>
	<td class="row0">
        Contact Number
	</td>
	<td ><%=jspUtil.getInputText("contactnum", UserValidator.vfContact, usr.getStContactNum(), 200)%>
 	</td>
</tr>
<tr>
	<td class="row0">
		Active Date
	</td>
	<td >
    <%if (sTitle.equalsIgnoreCase("CREATE")){
      out.println(jspUtil.getInputText("activedate", UserValidator.vfActiveDate, new Date(), 100));
    }else{
    out.println(jspUtil.getDisplayText("activedate", UserValidator.vfActiveDate, usr.getDtActiveDate(), 100));
    }%>

 	</td>
</tr>
<tr>
	<td class="row1">
		InActive Date
	</td>
	<td >
    <%=jspUtil.getInputText("inactivedate", UserValidator.vfInActiveDate, usr.getDtInActiveDate(), 100)%>
 	</td>
</tr>
<tr>
	<td class="row1">
		Branch
	</td>
	<td >
    <%=jspUtil.getInputSelect("branch|Branch|string", null, LOVManager.getInstance().getLOV("LOV_Branch2",null).setLOValue(usr.getStBranch()), 100)%>
 	</td>
</tr>
<tr>
	<td class="row1">
		Last Login
	</td>
	<td >
    <%=jspUtil.print(usr.getDtLastLogin())%>
 	</td>
</tr>
<%
    if (userrole!=null) {
       int intRoles = userrole.size();
       System.out.println("========================="+intRoles);
        for (int i=0; i<intRoles; i++){
          UserRoleView urv = (UserRoleView) userrole.get(i);

          if (i == 0) {%>
             <tr><td class="row0">Roles</td>
             <td><%=jspUtil.getDisplayText("roles", null, urv.getStRoleName(), 200)%>
             <%=jspUtil.getButtonNormal("bdel","Delete","delRole('"+i+"')")%></td>
             </tr>

<%        } else { %>
             <tr><td class="row<%=i+1%>"></td>
	         <td><%=jspUtil.getDisplayText("roles", null, urv.getStRoleName(), 200)%>
             <%=jspUtil.getButtonNormal("bdel","Delete","delRole('"+i+"')")%></td>
           </tr>
<%        } %>
          <input type=hidden name=roles<%=i%> value="<%=urv.getStRoleID()%>">
<%      }%>

      <input type=hidden name=jmlrole value=<%=intRoles+1%>>
	<tr>
	<td class="row<%=intRoles+1%>"></td>
	<td><%=jspUtil.getInputSelect("roleid", UserValidator.vfRoles, rlcombo.getComboContent(null), 200)%>
    <%=jspUtil.getButtonNormal("badd","Add Role","addRole()")%></td>
    </tr>
<%
    } else {
%>
<input type=hidden name=jmlrole value=0>
<tr>
	<td class="row0">Roles</td>
	<td><%=jspUtil.getInputSelect("roleid", UserValidator.vfRoles, rlcombo.getComboContent(null), 200)%>
    <%=jspUtil.getButtonNormal("badd","Add Role","addRole()")%></td>
</tr>

<%
    }
%>


<br>

</table>
<table cellspacing=1 cellpadding=1>
<tr>
   <td colspan=3 width=600></td>
</tr>
<tr>
	<td colspan=2>
		<br><br>
      <input type=hidden name=deleteindex>
      <%=jspUtil.getButtonSubmit("bsave","Submit") %>
      <%=jspUtil.getButtonNormal("bcancel","Cancel","window.location='"+jspUtil.getControllerURL("USER_LIST_DISPLAY")+"'")%>
	</td>
</tr>
</table>
</form>
<script>

var f= form1;

var cekst="<%=sTitle%>";

if (cekst.toUpperCase()=="CREATE"){
 form1.repassword.validate=cekpassword
}
//form1.usertype.VE_onchange=cekcbbranch;
//form1.branchid.VE_onchange=cekcbbranch;

function addVendor() {
   f.EVENT.value='USER_ADD_VENDOR'
   f.submit();
}

function deleteVendor(n) {
   f.deleteindex.value = n;
   f.EVENT.value='USER_DEL_VENDOR'
   f.submit();
}

function validate() {
   if (f.vendorscount)
      if (f.vendorscount.value<1) {
         alert('You must assign vendors for this user');
         return false;
      }

   return true;
}

</script>
</body>
</html>
