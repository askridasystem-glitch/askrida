<%@ page import="com.crux.util.JSPUtil,
                 com.webfin.entity.model.EntityView"%>
<%@ taglib prefix="c" uri="crux.tld"%>
<% final JSPUtil jspUtil = new JSPUtil(request, response,"");

   final EntityView entity = (EntityView) request.getAttribute("FORM");


%>
<table cellpadding=2 cellspacing=1>
   <tr><td>Entity ID</td><td>:</td><td><c:field name="" type="" value="" flags=""/></td></tr>
   <tr><td>Customer Type</td><td>:</td><td></td></tr>
   <tr><td>Customer Code</td><td>:</td><td></td></tr>
   <tr><td>Customer Status</td><td>:</td><td></td></tr>
</table>
<%=jspUtil.release()%>