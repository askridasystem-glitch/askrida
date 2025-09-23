/***********************************************************************
 * Module:  com.crux.login.helper.LoginHelper
 * Author:  Denny Mahendra
 * Created: Mar 8, 2004 2:48:07 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.login.helper;

import com.crux.common.controller.Helper;
import com.crux.common.controller.HelperAction;
import com.crux.common.controller.ControllerServlet;
import com.crux.common.controller.SessionKeeper;
import com.crux.common.model.UserSession;
import com.crux.common.codedecode.Codec;
import com.crux.util.LogManager;
import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.Tools;
import com.crux.util.ejb.session.SessionCacheMgr;
import com.crux.login.ejb.LoginModule;
import com.crux.login.ejb.*;
import com.crux.login.model.UserSessionView;
import com.crux.common.parameter.Parameter;
import com.crux.util.VerifyRecaptcha;
import com.crux.web.controller.SessionManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;

public class LoginHelper extends Helper {

   private final static transient LogManager logger = LogManager.getInstance(LoginHelper.class);

   private LoginModule getRemoteLoginModule() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((LoginModuleHome) JNDIUtil.getInstance().lookup("LoginModuleEJB",LoginModuleHome.class.getName()))
            .create();
   }
   
   private UserMaintenance getRemoteUserMaintenance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((UserMaintenanceHome) JNDIUtil.getInstance().lookup("UserMaintenanceEJB",UserMaintenanceHome.class.getName()))
            .create();
   }

   public HelperAction processLogin(HttpServletRequest rq) throws Exception {

      if (rq.getSession().getAttribute("USER_SESSION")!=null){
          rq.getSession().setAttribute("USER_SESSION",null);
          logger.logDebug("user already login");
      }

      String stUserID = getString(rq.getParameter("userid"));
      String stPassword = getString(rq.getParameter("password"));

      // throw new Exception("Server is currently not available for normal operations");

      final String encoding = rq.getHeader("accept-encoding");

      final boolean compressionEnabled = (encoding != null) && (encoding.indexOf("gzip")>=0);

      if (compressionEnabled) rq.getSession().setAttribute("COMPRESSION","Y");

      final ControllerServlet ctl = ControllerServlet.getInstance();

      if (ctl.isBlockUsers())
         if (!"admin".equalsIgnoreCase(stUserID))
            throw new Exception("Server is currently not available for normal operations");
      
      final boolean blockUser2 = Parameter.readBoolean("BLOCK_USER");

      final boolean multipleLogin = Parameter.readBoolean("MULTIPLE_LOGIN");

      if(blockUser2)
         if(!"admin".equalsIgnoreCase(stUserID))
         	if(!"doni".equalsIgnoreCase(stUserID))
			throw new Exception(" Maintenance Sistem/Server, Akan Segera Beroperasi Kembali");
      

      final UserSessionView us = (UserSessionView) getRemoteLoginModule().getUser(stUserID,stPassword);

      Collection sessions = SessionKeeper.getInstance().getSessions();

      Iterator it = sessions.iterator();

      while (it.hasNext()) {
         HttpSession s = (HttpSession) it.next();

         UserSessionView usx = (UserSessionView) s.getAttribute("USER_SESSION");

         if (usx==null) continue;

         boolean sameUser = (Tools.isEqual(usx.getStUserID(), stUserID));

         boolean sameIP = Tools.isEqual((String)s.getAttribute("HOST"), rq.getRemoteHost());

         if(!multipleLogin){
             if (sameUser && !sameIP){
                  //throw new RuntimeException("User sedang login di komputer lain");
                 logger.logInfo("+++++ auto kill user : "+ usx.getStUserID());
                 s.invalidate();
                 break;
             }
         }
          
      }

      rq.getSession().setAttribute("AGENT", rq.getHeader("User-Agent"));
      rq.getSession().setAttribute("HOST", rq.getRemoteHost());
      rq.getSession().setAttribute("USER_SESSION", us);
      rq.getSession().setAttribute("USER_RESOURCES", us.getResources());

      if(stUserID.equalsIgnoreCase("underwriting")) return HelperAction.forward("success_login_uw");
      else if(stUserID.equalsIgnoreCase("claim")) return HelperAction.forward("success_login_claim");
      else if(stUserID.equalsIgnoreCase("reinsurance")) return HelperAction.forward("success_login_reas");
      else if(stUserID.equalsIgnoreCase("policycheckguest")) return HelperAction.forward("success_login_policycheckguest");
      else return HelperAction.forward("success_login");
   }

   public void loadMenu(HttpServletRequest rq)  throws Exception {
      final LoginModule loginModule = (LoginModule) SessionCacheMgr.getInstance().getSession(LoginModule.class.getName());

      final UserSession us = getUserSession(rq);

      final DTOList l = loginModule.getMenu(us.getStUserID());

      rq.setAttribute("MENU",l);
   }

   public void logOut(HttpServletRequest rq)  throws Exception {
      final UserSession us = getUserSession(rq);
      final UserSessionView uv = getRemoteUserMaintenance().getUserView(us.getStUserID());
      getRemoteLoginModule().logActivity(uv,"LOGOUT",null);
      
      rq.getSession().invalidate();
   }

   public void blockUsers(HttpServletRequest rq)  throws Exception {
      final ControllerServlet ctl = ControllerServlet.getInstance();
      ctl.setBlockUsers(!ctl.isBlockUsers());
      ctl.setStBlockReason("Server will be down immediately for maintenance");

      final HttpSession ms = rq.getSession();

      if (ctl.isBlockUsers()) {
         final Iterator sessions = SessionKeeper.getInstance().getSessions().iterator();

         final ArrayList l = new ArrayList();

         while (sessions.hasNext()) {
            l.add(sessions.next());
         }

         /*for (int i = 0; i < l.size(); i++) {
            HttpSession session = (HttpSession) l.get(i);
            if (session.getId().equalsIgnoreCase(ms.getId())) {
               logger.logDebug("blockUsers: skipping destroy session : "+session.getId());
               continue;
            }
            logger.logDebug("blockUsers: destroying "+session.getId());
            session.invalidate();
         }*/
      }
   }
   
   public void makeAnnouncement(HttpServletRequest rq)  throws Exception {
      final ControllerServlet ctl = ControllerServlet.getInstance();
      ctl.setAnnouncement(!ctl.isAnnouncement());
      ctl.setStAnnouncement("TES PENGUMUMAN");

      final HttpSession ms = rq.getSession();
   }

   public void killSession(HttpServletRequest rq)  throws Exception {
      final String sid = getString(rq.getParameter("sid"));
      final Iterator sessions = SessionKeeper.getInstance().getSessions().iterator();
      while (sessions.hasNext()) {
         HttpSession s = (HttpSession) sessions.next();
         if (s.getId().equals(sid)) {
            s.invalidate();
            break;
         }
      }
   }
   
   public void showRole(HttpServletRequest rq)  throws Exception {
      final ControllerServlet ctl = ControllerServlet.getInstance();
      ctl.setShowRole(true);
   }
   
   public void hideRole(HttpServletRequest rq)  throws Exception {
      final ControllerServlet ctl = ControllerServlet.getInstance();
      ctl.setShowRole(false);
   }
}