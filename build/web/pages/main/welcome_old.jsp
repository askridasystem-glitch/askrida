<%@ page import="com.crux.util.JSPUtil,
                 com.crux.common.controller.Helper,
                 com.crux.login.model.UserSessionView"%><% final JSPUtil jspUtil = new JSPUtil(request, response,"WELCOME"); %><html>
<%

   final UserSessionView uv = (UserSessionView)Helper.getUserSession(request);

   final String encoding = request.getHeader("accept-encoding");

   final boolean compressionEnabled = (encoding != null) && (encoding.indexOf("gzip")>=0);

%>

<table cellpadding=2 cellspacing=1 width="80%">
   <tr>
        <td>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <strong>Hi <%=uv.getStUserName()%>, </strong>
        <br>
        <br>
        <br>
        <br>
        <br>
         {L-ENG Welcome To-L}{L-INA Selamat Datang Di-L}<br>
        <br>
        <br>
        <br>
        {L-ENG Insurance & Finance Web Application-L}{L-INA Aplikasi Web Finance & Asuransi-L}
      </td> 
      <td>
      <%-- 
      <object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0" width="900" height="600" id="sub" align="middle">
		<param name="allowScriptAccess" value="sameDomain" />
		<param name="movie" value="<%=jspUtil.getImagePath()%>/sub.swf" />
		<param name="quality" value="high" />
		<param name="bgcolor" value="white" />
		<embed src="<%=jspUtil.getImagePath()%>/sub.swf" quality="high" bgcolor="#000000" width="900" height="600" name="sub" align="middle" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
	  </object>--%>
    </td>
   </tr>
</table>



<%=jspUtil.release()%>


