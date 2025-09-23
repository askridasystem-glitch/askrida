<%@ page import="com.crux.util.JSPUtil"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
<script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
<script>
   dialogReturn('auto');
   window.close();
</script>