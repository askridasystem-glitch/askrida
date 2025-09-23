<%@ page import="com.crux.util.JSPUtil,
                 com.ots.notification.model.NotificationView,
                 com.ots.codec.OTSCodec,
                 com.crux.util.LookUpUtil,
                 com.ots.notification.validator.NotificationValidator"%><html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
      <script language="JavaScript" src="<%=jspUtil.getScriptURL("validator.js")%>"></script>
   </head>
<%
   NotificationView not = (NotificationView)request.getAttribute("NOT");
   LookUpUtil luNotMode = OTSCodec.NotificationMode.getLookUp();
   LookUpUtil luNotEvents = OTSCodec.NotificationEvents.getLookUp();

   luNotMode.setLOValue(not.getStModa());
   luNotEvents.setLOValue(not.getStEvent());

   int ov=0;
   boolean readOnly = not.isUnModified();
   if (readOnly) ov |= JSPUtil.READONLY;
%>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="">
         <table cellpadding=2 cellspacing=1>
            <%=jspUtil.getHeader("NOTIFICATION")%>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr><td>Moda</td><td>:</td><td><%=jspUtil.getInputSelect("moda",NotificationValidator.vfModa,luNotMode,200,JSPUtil.MANDATORY|ov)%></td></tr>
                     <tr><td>Event</td><td>:</td><td><%=jspUtil.getInputSelect("eventx",NotificationValidator.vfEvent,luNotEvents,200,JSPUtil.MANDATORY|ov)%></td></tr>
                     <tr><td>Active</td><td>:</td><td><input <%=readOnly?"disabled":""%> type=checkbox name=activeflag <%=not.isActive()?"checked":""%>></td></tr>
                     <tr><td valign=top>Template</td><td valign=top>:</td><td><%=jspUtil.getInputTextArea("template",NotificationValidator.vfTemplate,not.getStTemplate(),20,100,400,JSPUtil.MANDATORY|ov)%></td></tr>
                  </table>
               </td>
            </tr>
            <tr>
               <td>
                  <%=jspUtil.getButtonSubmit("bsave","Save","document.f.EVENT.value='SAVE_NOTIFICATIONS'")%>
                  <%=jspUtil.getButtonNormal("bcancel","Cancel","document.f.EVENT.value='LIST_NOTIFICATIONS';f.submit()")%>
               </td>
            </tr>
         </table>
      </form>
   </body>
</html>