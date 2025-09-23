<%@ page import="com.crux.util.JSPUtil,
                 com.crux.util.DTOList,
                 com.ots.notification.model.NotificationView,
                 com.ots.codec.OTSCodec"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
   <script>
      var notid='';
   </script>
<%
   DTOList list = (DTOList)request.getAttribute("LIST");

   boolean hasCreate = jspUtil.hasResource(OTSCodec.Resource.Notification.NOT_CREATE);
   boolean hasEdit = jspUtil.hasResource(OTSCodec.Resource.Notification.NOT_EDIT);
   boolean hasView = jspUtil.hasResource(OTSCodec.Resource.Notification.NOT_VIEW);

%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="">
         <table cellpadding=2 cellspacing=1>
            <%=jspUtil.getHeader("NOTIFICATIONS")%>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td></td>
                        <td>TRANSPORT</td>
                        <td>EVENT</td>
                        <td>ACTIVE</td>
                     </tr>
<%
   for (int i = 0; i < list.size(); i++) {
      NotificationView not = (NotificationView) list.get(i);
%>
                     <tr class=row0>
                        <td><input type=radio name=select onclick="notid='<%=jspUtil.print(not.getLgNotificationID())%>'"></td>
                        <td><%=jspUtil.print(not.getStModa())%></td>
                        <td><%=jspUtil.print(not.getStEvent())%></td>
                        <td><input type=checkbox disabled <%=not.isActive()?"checked":""%>></td>
                     </tr>
<% }%>
                  </table>
               </td>
            </tr>
            <tr>
               <td>
                  <br>
                  <br>
                  <%=!hasCreate?"":jspUtil.getButtonNormal("bcreate","Create","window.location='not.ctl?EVENT=CREATE_NOTIFICATIONS'")%>
                  <%=!hasEdit?"":jspUtil.getButtonNormal("bEdit","Edit","if (notid!='') window.location='not.ctl?EVENT=EDIT_NOTIFICATIONS&notid='+notid")%>
                  <%=!hasView?"":jspUtil.getButtonNormal("bView","View","if (notid!='') window.location='not.ctl?EVENT=VIEW_NOTIFICATIONS&notid='+notid")%>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>