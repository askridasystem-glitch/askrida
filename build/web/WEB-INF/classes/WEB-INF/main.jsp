<%@ page import="com.crux.util.JSPUtil"%><html>
<% final JSPUtil jspUtil = new JSPUtil(request, response); %><head>
	<title></title>
</head>

<frameset id="mainFrame" cols="*" frameborder="NO" border="1" framespacing="0">
	<frameset rows="85,*,1" cols="*" frameborder="NO" border="1" framespacing="0">
	  <frame name="topFrame" target="mainFrame" scrolling="NO" noresize src="<%=jspUtil.getPageURL("FRAME_TOP")%>">
	  <frameset id="frmMenu" cols="185,*" frameborder="NO" border="1" framespacing="0">
		<frame name="leftFrame" target="basefrm" scrolling="yes" src="<%=jspUtil.getPageURL("FRAME_LEFT")%>">
	    <frame name="basefrm" src="<%=jspUtil.getPageURL("MAIN_WELCOME")%>">
	  </frameset>
	  <frame name="bottomFrame" scrolling="NO" noresize src="blank.ctl">
	</frameset>
</frameset>
<script language=JavaScript1.2>
   window.title = "";
   
</script>

</html>
