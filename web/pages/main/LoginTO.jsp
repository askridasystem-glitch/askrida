<%@ page import="com.crux.util.JSPUtil"%>
<%
   final Throwable e = (Throwable)request.getAttribute("ERROR_MESSAGE");
%>
<script>
   alert('<%=JSPUtil.jsEscape(JSPUtil.translateMessage(e))%>');
   window.parent.location='<%=request.getContextPath()%>/';
</script>