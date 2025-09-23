/***********************************************************************
 * Module:  com.crux.web.controller.SessionManager
 * Author:  Denny Mahendra
 * Created: May 25, 2005 1:04:44 PM
 * Purpose:
 ***********************************************************************/

package com.crux.web.controller;

import com.crux.session.Session;
import com.crux.util.LogManager;
import com.crux.web.form.Form;
import com.crux.login.model.UserSessionView;
import com.crux.pool.DTOPool;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class SessionManager {
   private final static transient LogManager logger = LogManager.getInstance(SessionManager.class);

   private static ThreadLocal staticinstance = new ThreadLocal(){
      protected Object initialValue() {
         return new SessionManager();
      }
   };

   private static HashMap forms=new HashMap();
   private Session session;
   private Form currentForm;
   private String redirect;
   private HttpServletRequest request;
   private HttpServletResponse response;
   private ServletContext servletContext;
   //private long formExpire = 15l*60l*1000l; // 2 mins this is default
   private long formExpire = 30l*60l*1000l; //  
   //private long formExpire = 3*1000l; // 3 secs

   public String getRedirect() {
      return redirect;
   }

   public void setRedirect(String redirect) {
      this.redirect = redirect;
   }

   public static SessionManager getInstance() {
      return (SessionManager) staticinstance.get();
   }

   private SessionManager() {
   }

   public void add(Form f) {
      f.touch();
      forms.put(f.getFormID(),f);
      collectGarbage();
      logger.logDebug("add: forms in repository = "+forms.size());
   }

   private void collectGarbage() {

      Iterator fv = forms.values().iterator();

      long oldest = System.currentTimeMillis()-formExpire;

      int n=0;

      while (fv.hasNext()) {
         Form form = (Form) fv.next();

         //logger.logDebug("form.getStamp(): "+form.getStamp());
         if (form.getStamp()<oldest) {
            form.expire();
            fv.remove();
            n++;
         }
      }

      logger.logDebug("collectGarbage: trashes "+n+" forms");
      //logger.logDebug("oldest: "+oldest);

   }

   public UserSessionView getSession() {
      if (session!=null)
         session.setDtTransactionDate(new Date());
      return (UserSessionView) session;
   }

   public void setSession(Session session) {
      this.session = session;
   }

   public Form getForm(String formid) {
      if (forms==null) return null;
      Form f = (Form) forms.get(formid);
      if (f!=null)
         f.touch();
      return f;
   }

   public void show(Form form) {
      //logger.logDebug("show: "+form);
      currentForm = form;
   }

   public Form getCurrentForm() {
      //logger.logDebug("getCurrentForm: currentForm = "+currentForm);
      wakeUp(currentForm);
      return currentForm;
   }

   public void close(Form form) {
      /*final Iterator iterator = forms.getList().iterator();

      Form f=null;

      while (iterator.hasNext()) {
         f = (Form) iterator.next();
         if (f==form) iterator.remove();
      }*/

      //final ArrayList l = forms.getList();

      //l.remove(form);

      forms.remove(form.getFormID());

      if (form.getOpener()!=null) {
         currentForm = form.getOpener();
         wakeUp(currentForm);
      }
      else {
         currentForm=null;
         final String ret = form.getFormMeta().getStReturn();
         if (ret!=null) {
            redirect = "ctl.ctl?EVENT="+ret;
         } else {
            redirect = "ctl.ctl?EVENT=MAIN_WELCOME";
         }
      }
   }

   private void wakeUp(Form form) {
      if (form==null) return;

      form.touch();

      if (!forms.containsKey(form)) {
         forms.put(form.getFormID(), form);
      }
   }

   public void setServlet(ServletRequest srq, ServletResponse srp, ServletContext servletContext) {
      request = (HttpServletRequest) srq;
      response = (HttpServletResponse) srp;
      this.servletContext = servletContext;
   }

   public ServletContext getServletContext() {
      return servletContext;
   }

   public HttpServletResponse getResponse() {
      return response;
   }

   public HttpServletRequest getRequest() {
      return request;
   }

   public boolean hasResource(String resourceid) {
      return ((UserSessionView) session).hasResource(resourceid);
   }

   public String getUserID() {
      return session==null?"?":session.getStUserID();
   }

   public int getFormCount() {
      return forms.size();
   }
   
   public String getShortName() {
      return session==null?"?":session.getStShortName();
   }
   
   public UserSessionView getCreateUser() {
        return (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, session.getStUserID());
    }

   public UserSessionView getUser(String stUserID) {
        return (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, stUserID);
    }

}
