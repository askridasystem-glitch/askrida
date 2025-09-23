<%@ page import="com.crux.util.JSPUtil,
                 com.crux.lang.LanguageManager,
                 com.crux.util.DTOList,
                 com.crux.lang.LanguageView,
                 com.crux.util.Tools,
                 com.crux.common.parameter.Parameter"%>
<% final JSPUtil jspUtil = new JSPUtil(request, response); %>
<%
   final boolean firstTime = !"Y".equalsIgnoreCase((String) request.getSession().getAttribute("FIRST_TIME"));

   final DTOList languages = LanguageManager.getInstance().getLanguages();

    final boolean showLogo = Parameter.readBoolean("GEN_SHOW_LOGO");
    
    final boolean blockUser = Parameter.readBoolean("BLOCK_USER");

   if (firstTime && false) {
      request.getSession().setAttribute("FIRST_TIME","Y");
      %>


<html>
<script language="JavaScript">

   window.open('index.jsp','','toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no,width=800,height=400');
   window.close();
</script>
</html>
 <%
   } else {
%>

<html>
<head>
<title></title>
<link rel="stylesheet" href="<%=jspUtil.getStyleSheetPath()%>" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>


<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" MarginWidth="0" MarginHeight="0">
<form name=loginform method=POST action="login.ctl">
<input type=hidden name=EVENT value=LOGIN_MAIN>
<table height="100%" width="100%" cellpadding=2 cellspacing=1>
   <tr height="450">
      <td align=center valign=bottom>
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td valign=bottom>
               <% if(blockUser) {%>
               <%--  <img src="images/worker copy.png">--%>
               		<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0" width="300" height="300" id="sub" align="middle">
					<param name="allowScriptAccess" value="sameDomain" />
					<param name="movie" value="<%=jspUtil.getImagePath()%>/under maintenance.swf" />
					<param name="quality" value="high" />
					<param name="bgcolor" value="FFFFFF" />
					<embed src="<%=jspUtil.getImagePath()%>/under maintenance.swf" quality="high" bgcolor="#FFFFFF" width="300" height="300" name="sub" align="middle" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
				  	</object>
               <% } else{%>
               		<% if (showLogo) {%>               
			        <img src="images/LOGO_HOME.jpg">
			        <%--<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0" width="900" height="200" id="sub" align="middle">
					<param name="allowScriptAccess" value="sameDomain" />
					<param name="movie" value="<%=jspUtil.getImagePath()%>/ASKRIDA_LOGO.swf" />
					<param name="quality" value="high" />
					<param name="bgcolor" value="FFFFFF" />
					<embed src="<%=jspUtil.getImagePath()%>/ASKRIDA_LOGO.swf" quality="high" bgcolor="#FFFFFF" width="900" height="200" name="sub" align="middle" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
				  	</object>--%>
					<% } %>
               <% }%>

               </td>
            </tr>
            <tr>
               <td align=center>
                  <table cellpadding=2 cellspacing=1>
                     <tr>
                        <td>
                           <table cellpadding=2 cellspacing=1>
                              <tr><td align=right>{L-ENGUser ID-L}{L-INAUser ID-L} </td><td>:</td><td><input type="text" name="userid" id="userid" value=""  autocomplete="off"></td></tr>
                              <tr><td>{L-ENGPassword-L}{L-INAPassword-L} </td><td>:</td><td><input type="password" name="password" autocomplete="off"></td></tr>
                              <tr><td><td></td></td><td align=left><input type=image alt="Submit" name="submit" src="images/bt_login_off.gif" width="54" height="21"></td></tr>
                           </table>
                        </td>
                        <td valign=top>

                           <table cellpadding=2 cellspacing=1>
<%

      final String activeLang = LanguageManager.getInstance().getActiveLang();

      for (int i = 0; i < languages.size(); i++) {
         LanguageView lv = (LanguageView) languages.get(i);
         final String imgname = lv.getStLanguageID().toLowerCase();

         final boolean active = Tools.isEqual(lv.getStLanguageID(), activeLang);

         final String bor = active?"border:solid 2px blue":"border:solid 1px black";

         %><tr><td align=center><img style="<%=bor%>; cursor:hand" alt="<%=lv.getStLanguageName()%>" src="images/flag_<%=imgname%>.gif" onclick="document.location='<%=request.getContextPath()%>/?clangx=<%=lv.getStLanguageID()%>'"></td></tr><%
      }
%>
                           </table>
                        </td>
                     </tr>
                  </table>
               </td>
            </tr>
            <tr>
            <td></td>
            </tr>
         </table>
      </td>
   <% if (showLogo) {%>    
   </tr>
   <tr align="CENTER">
   <td>
   
   </td>
   </tr>
   <tr align="CENTER">
   <td>

   </td>
   </tr>
   <% } %>
</table>
<script>
   document.getElementById('userid').focus();
</script>

</form>

</body>


</html>
<% } %>