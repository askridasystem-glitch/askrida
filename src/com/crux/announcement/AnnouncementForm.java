/***********************************************************************
 * Module:  com.crux.sqi.SQIForm
 * Author:  Denny Mahendra
 * Created: Jan 21, 2008 7:36:30 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.announcement;

import com.crux.web.form.Form;
import com.crux.common.parameter.Parameter;
import com.crux.util.stringutil.StringUtil;
import com.crux.util.StringTools;
import com.crux.util.SQLUtil;
import com.crux.util.LogUtil;
import com.crux.common.controller.*;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.crux.common.controller.Helper;
import com.crux.common.controller.HelperAction;
import com.crux.common.controller.ControllerServlet;
import com.crux.common.controller.SessionKeeper;
import com.crux.common.model.UserSession;
import com.crux.common.codedecode.Codec;
import com.crux.util.LogManager;
import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.JSPUtil;
import com.crux.util.Tools;
import com.crux.util.ejb.session.SessionCacheMgr;
import com.crux.login.ejb.LoginModule;
import com.crux.login.ejb.LoginModuleHome;
import com.crux.login.model.UserSessionView;
import com.crux.common.controller.HelperAction;
import com.crux.common.controller.EventAction;
import com.crux.common.parameter.Parameter;

import java.io.BufferedReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.ArrayList;

public class AnnouncementForm extends Form {
   private String stAnnouncement;
   private String rpt;
   private String stColor;
   private String stUserID;

   public String getStAnnouncement() {
      return stAnnouncement;
   }

   public void setStAnnouncement(String stAnnouncement) {
      this.stAnnouncement = stAnnouncement;
   }
   
   public String getStColor() {
      return stColor;
   }

   public void setStColor(String stColor) {
      this.stColor = stColor;
   }
   
   public String getStUserID() {
      return stUserID;
   }

   public void setStUserID(String stUserID) {
      this.stUserID = stUserID;
   }

   public void execute() throws Throwable {
      try {
         final ControllerServlet ctl = ControllerServlet.getInstance();
         ctl.setAnnouncement(!ctl.isAnnouncement());
         ctl.setStAnnouncement(getStAnnouncement());
         ctl.setStColor(getStColor());
         if(getStUserID()!=null) ctl.setStUserID(getStUserID());
         else if(getStUserID()==null) ctl.setStUserID("ALL");
         
         //final HttpSession ms = rq.getSession();
		  /*
	      if (ctl.isAnnouncement()) {
	         final Iterator sessions = SessionKeeper.getInstance().getSessions().iterator();
	
	         final ArrayList l = new ArrayList();
	
	         while (sessions.hasNext()) {
	            l.add(sessions.next());
	         }
	      */
	         /*for (int i = 0; i < l.size(); i++) {
	            HttpSession session = (HttpSession) l.get(i);
	            if (session.getId().equalsIgnoreCase(ms.getId())) {
	               logger.logDebug("blockUsers: skipping destroy session : "+session.getId());
	               continue;
	            }
	            logger.logDebug("blockUsers: destroying "+session.getId());
	            session.invalidate();
	         }*/
	      //}

         //final HttpSession ms = rq.getSession();
      } catch(Exception e) {
         throw new RuntimeException("Error");
      }

      //stAnnouncement="";
   }
   
   public void clear() throws Throwable {
      try {
      	
      	 //if(getStUserID()!=null)
      	 
         final ControllerServlet ctl = ControllerServlet.getInstance();
         ctl.setAnnouncement(!ctl.isAnnouncement());
         ctl.setStAnnouncement("");
         
         //final HttpSession ms = rq.getSession();

	      if (ctl.isAnnouncement()) {
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

         //final HttpSession ms = rq.getSession();
      } catch(Exception e) {
         throw new RuntimeException("Error");
      }

      //stAnnouncement="";
   }
   


}

