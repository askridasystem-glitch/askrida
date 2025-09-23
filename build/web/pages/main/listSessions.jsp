<%@ page import="com.crux.util.JSPUtil,
                 com.crux.common.controller.SessionKeeper,
                 com.crux.common.controller.Helper,
                 com.crux.login.model.UserSessionView,
                 java.sql.Date,
                 com.crux.common.controller.Event,
                 com.crux.util.DateUtil,
                 java.util.Iterator,
                 com.crux.common.controller.ControllerServlet,
                 java.util.Collection,
                 com.crux.web.controller.SessionManager,
                 java.util.*"%>
                 <%@ taglib prefix="c" uri="crux" %>
                 <html><% final JSPUtil jspUtil = new JSPUtil(request, response); %>
   <head>
      <LINK href="<%=jspUtil.getStyleSheetPath()%>" type="text/css" rel=STYLESHEET>
   </head>
   <%

      final ArrayList sessions = new ArrayList(SessionKeeper.getInstance().getSessions());

      boolean blockUsers=false;
      boolean showRole = false;
      final ControllerServlet controller = ControllerServlet.getInstance();
      if (controller != null){
          blockUsers = controller.isBlockUsers();
          showRole = controller.isShowRole();
      }
         
      boolean isAdmin = false;
      if(SessionManager.getInstance().getSession().isAdministrator(SessionManager.getInstance().getSession().getStUserID()))
          isAdmin = true;

      boolean enableSuperEdit = SessionManager.getInstance().getSession().hasResource("POL_SUPER_EDIT");
      
   %>
   <body>
      <form name=f method=POST action="ctl.ctl">
         <input type=hidden name=EVENT value="">
         <input type=hidden name=sid value="">
         <table cellpadding=2 cellspacing=1>
            <%=jspUtil.getHeader("USER SESSIONS")%>
            <tr>
               <td colspan="2">
                  <table cellpadding=2 cellspacing=1>                    
                     <tr class=header>
                        <td>No</td>
                        <td>User ID</td>
                        <%if(showRole){%><td width="500">Role</td><%}%>
                        <td>Nama</td>
                        <td>Cabang</td>
                        <td>Cabang Penerbit</td>
                        <td>Idle</td>
                        <td>Current</td>
                        <%--  <td>Last Login</td>--%>
                        <td align=center>GZ</td>
                        <td>IP Address</td>
                        <%if(isAdmin){%><td></td><%}%>
                        
                     </tr>
<%
   Collections.sort(
         sessions,
         new Comparator() {
            public int compare(Object o1, Object o2) {
               final long t1 = ((HttpSession) o1).getLastAccessedTime();
               final long t2 = ((HttpSession) o2).getLastAccessedTime();
               if (t1==t2) return 0;
               if (t1>t2) return -1;
               return 1;
            }
         }
   );

   final Iterator it = sessions.iterator();

   final long cms = System.currentTimeMillis();

   final UserSessionView blankuv = new UserSessionView();

   int i=0;

   while (it.hasNext()) {
      HttpSession s = (HttpSession) it.next();

      UserSessionView uv = (UserSessionView) s.getAttribute("USER_SESSION");
      if (uv==null) continue;
      if (uv==null) uv = blankuv; 

      i++;

      final long idlems = cms-s.getLastAccessedTime();
      final Date idle = new Date(idlems);

      final boolean compression = "Y".equalsIgnoreCase((String)s.getAttribute("COMPRESSION"));

      String idlex=null;

      if (idlems<60*1000)
         idlex = String.valueOf(idlems/1000)+"s";
      else
         idlex = String.valueOf(idlems/(1000*60))+"m";
         
         
      String ipAddress="";
      ipAddress = (String) s.getAttribute("HOST");

      final Event event = (Event)s.getAttribute("CURRENT_EVENT");

      if(!enableSuperEdit)
            if(SessionManager.getInstance().getSession().getStBranch() != null)
                  if(!SessionManager.getInstance().getSession().getStBranch().equalsIgnoreCase(uv.getStBranch())){
                        i--;
                        continue;
                  }

      String cabang = uv.getStBranchName();

      if(cabang==null) cabang = "Head Office";
              
      
      
      
%>
                     <tr class=row0>
                        <td><%=i%></td>
                        <td><%=jspUtil.print(uv.getStUserID())%></td>
                        <%if(showRole){%><td width="500"><%=jspUtil.print(uv.getStRolesName(uv.getStUserID()))%></td><%}%>
                        <td><%=jspUtil.print(uv.getStUserName())%></td>
                        <td><%=jspUtil.print(cabang)%></td>
                        <td><%=jspUtil.print(uv.getStBranchSourceName())%></td>
                        <td><%=idlex%></td>
                        <td><%=jspUtil.print(event.getStEventID())%></td>
                        <%--  <td><%=(uv.getDtLastLogin())%></td>--%>
                        <td><%=(compression?"Y":"N")%></td>
                        <td><%=ipAddress%></td>
                        <%--
                        <%if(isAdmin){%><td>[<a href="<%=jspUtil.print(jspUtil.getControllerURL("SESSION_KILL")+"&sid="+s.getId())%>">kill</a>]</td><%}%>
                        --%>
                        <%if(isAdmin){%>
                     <td>
                        <button class=button id="kill" onclick="if (confirm('Yakin ingin di kill?')) {f.EVENT.value='SESSION_KILL';f.sid.value='<%=jspUtil.print(s.getId())%>';f.submit();}else{}">kill</button>  
                    </td>
                    <%}%>
                       
                    </tr>
<%
   }
%>
                  </table>
               </td>
            </tr>
            <tr>
                <td>
                    <%if(!showRole){%><%=jspUtil.getButtonSubmit("bshow","Show Role","f.EVENT.value='SHOW_ROLE'")%><%}%>
                    <%if(showRole){%><%=jspUtil.getButtonSubmit("bhide","Hide Role","f.EVENT.value='HIDE_ROLE'")%><%}%>
                    <%if(isAdmin){%><%=jspUtil.getButtonSubmit("bblock","BLOCK/UNBLOCK USERS","f.EVENT.value='CTL_BLOCK_USERS'")%><%}%>
                </td>
            </tr>
            <tr>
               <td>
                  <br>
                  <br>
                  <table cellpadding=2 cellspacing=1>
                     <%if(isAdmin){%>
                      <tr>
                        <td>Block Users</td>
                        <td>:</td>
                        <td><%=blockUsers%></td>
                     </tr>
                     <%}%>
                  </table>
               </td>
            </tr>         
         </table>
      </form>
   </body>
</html>