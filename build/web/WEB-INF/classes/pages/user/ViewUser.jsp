<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 java.util.Date,
                 com.crux.login.model.UserSessionView,
                 com.crux.login.model.UserRoleView,
                 com.crux.login.model.UserVendorView"%>
<html>
<% final JSPUtil jspUtil = new JSPUtil(request, response); %>
<LINK href="<%=jspUtil.getStyleSheetPath()%>" type=text/css rel=STYLESHEET>
<head>
<%
   UserSessionView usr = (UserSessionView) request.getAttribute("USER");
  /*
   final DTOList branchcombo = (DTOList) request.getAttribute("BRANCH");
   final DTOList otcombo = (DTOList) request.getAttribute("OUTLET");
   final DTOList whcombo = (DTOList) request.getAttribute("WAREHOUSE");
   final DTOList dpcombo = (DTOList) request.getAttribute("DEPO");
   final DTOList rlcombo = (DTOList) request.getAttribute("ROLES");
   */

   final DTOList userrole = usr.getUserroles();
   //final DTOList usrVendor  = usr.getVendors();
   //System.out.println("userrole : "+ userrole);
   String id = "";
   if (usr == null) usr = new UserSessionView();
   else id = usr.getStUserID();
   String sTitle = (String) request.getAttribute("ACTION");
   System.out.println("Action = " + sTitle);
   String stUserType="";


%>
</head><body>
<form name=form1 action="user.ctl" method=POST>
<input type=hidden name=usrid value="<%=id%>">
<input type=hidden name=ACTION value="<%=sTitle%>">
<script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
<script language="javascript">

</script>
<table cellpadding=1 cellspacing=1>
<%=jspUtil.getHeader("VIEW USER PROFILE")%>
<tr>
	<td width="30%" <br></td>
</tr>
<tr>
	<td class="row0">
		User ID
	</td>
	<td >
    <%=jspUtil.print(usr.getStUserID())%>
 	</td>
</tr>
<!--tr>
	<td class="row1">
		User Type
	</td>
	<td >
    <!--%=jspUtil.print(stUserType)%>
 	</td>
</tr-->
<tr>
	<td class="row0">
		User Name
	</td>
	<td >
    <%=jspUtil.print(usr.getStUserName())%>
 	</td>
</tr>
<!--tr>
	<td class="row1">
		Branch
	</td>
	<td >

 	</td>
</tr-->
<tr>
	<td class="row0">
        Division
	</td>
	<td >
    <%=jspUtil.print(usr.getStDivision())%>
 	</td>
</tr>
<tr>
	<td class="row1">
        Department
	</td>
	<td ><%=jspUtil.print(usr.getStDepartment())%>
 	</td>
</tr>
<tr>
	<td class="row0">
        Email Address
	</td>
	<td >
    <%=jspUtil.print(usr.getStEmail())%>
 	</td>
</tr>
<tr>
	<td class="row1">
        Phone
	</td>
	<td >
    <%=jspUtil.print(usr.getStPhone())%>
 	</td>
</tr>
<tr>
	<td class="row0">
        Contact Number
	</td>
	<td >
    <%=jspUtil.print(usr.getStContactNum())%>
 	</td>
</tr>
<tr>
	<td class="row0">
		Active Date
	</td>
	<td >
    <%=jspUtil.print(usr.getDtActiveDate())%>
 	</td>
</tr>
<tr>
	<td class="row1">
		InActive Date
	</td>
	<td >
    <%=jspUtil.print(usr.getDtInActiveDate())%>
 	</td>
</tr>

<%
    if (userrole!=null) {
       int intRoles = userrole.size();
        for (int i=0; i<intRoles; i++){
          UserRoleView urv = (UserRoleView) userrole.get(i);
          if (i == 0) {%>
             <tr><td class="row1">Roles</td>
             <td>
             <%=jspUtil.print(urv.getStRoleName())%>
             </tr>
<%        } else { %>
             <tr><td class="row<%=i+1%>"></td>
	         <td>
             <%=jspUtil.print(urv.getStRoleName())%>
             </td>
           </tr>
<%        }
      }
      if (intRoles == 0) { %>
         <tr>
	     <td class="row1">Roles</td>
	     <td></td>
         </tr>
<%    }
    } else {
%>

<tr>
	<td class="row0">Roles</td>
	<td></td>
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
      <input type=hidden name=EVENT value=USER_LIST_DISPLAY>
      <input class=button type=submit value="  Close  ">
	</td>
</tr>
</table>
</form>
</body>
</html>
