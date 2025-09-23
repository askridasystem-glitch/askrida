<%@ page import="com.crux.util.JSPUtil"%><html>
<% final JSPUtil jspUtil = new JSPUtil(request, response); %><head>
	<title></title>
<%
   final String url = request.getParameter("url");
%>
</head>
	<frameset rows="1,*" cols="*" frameborder="NO" border="1" framespacing="0">
	  <frame name="topFrame" target="mainFrame" scrolling="NO" noresize>
     <frame name="basefrm" src="<%=jspUtil.print(url)%>">
	</frameset>
<script>
   window.title = "";
</script>
</html>
