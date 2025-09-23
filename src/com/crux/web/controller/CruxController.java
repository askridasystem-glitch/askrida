/***********************************************************************
 * Module:  com.crux.web.controller.CruxController
 * Author:  Denny Mahendra
 * Created: May 20, 2005 1:52:32 PM
 * Purpose:
 ***********************************************************************/

package com.crux.web.controller;

import com.crux.common.config.Config;
import com.crux.initializer.ejb.wds.SchemaInitializer;
import com.crux.lov.LOVServer;
import com.crux.session.Session;
import com.crux.session.SessionImpl;
import com.crux.util.LogManager;
import com.crux.util.LogUtil;
import com.crux.util.SQLUtil;
import com.crux.web.application.Application;
import com.crux.web.form.Form;
import com.crux.web.form.FormManager;
import com.crux.web.resource.Resource;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

public class CruxController extends HttpServlet implements Filter {
   private ServletContext servletContext;
   private FormManager formManager = FormManager.getInstance();

   private final static transient LogManager logger = LogManager.getInstance(CruxController.class);
   private boolean initialized = false;
   private Application application;
   private HashMap resourceMap;
   private String appclass;
   private ServletContext context;
   private boolean hideLogger = false;

   protected void doGet(HttpServletRequest rq, HttpServletResponse rp) throws ServletException, IOException {
      if (rq.getAttribute("REQ_HANDLED")==null) {
         rq.setAttribute("REQ_HANDLED","1");
         final String uri = rq.getRequestURI();
         final String rsrc = uri.substring(uri.lastIndexOf('/'),uri.length());
         System.out.println(uri);
         final String rsrctype = rsrc.substring(rsrc.lastIndexOf('.'),rsrc.length());
         if ("crux".equalsIgnoreCase(rsrctype)) {
         }
         rq.getRequestDispatcher("/login.jsp").forward(rq,rp);
      }
   }

   public void init(FilterConfig filterConfig) throws ServletException {
      try {

         context = filterConfig.getServletContext();

         SchemaInitializer.setScript(context.getRealPath("/xml/initdump.sql"));

         new SQLUtil();

         appclass = filterConfig.getInitParameter("application");

         application = (Application)Class.forName(appclass).getConstructor(null).newInstance(null);
         application.setBasePath(filterConfig.getServletContext().getRealPath("\\"));
         application.initialize();

         resourceMap = new HashMap();

         /*resourceMap.put("crux.css",new Resource("org/crux/web/css/crux.css","text/plain"));
         resourceMap.put("crux.js",new Resource("org/crux/web/js/crux.js","text/plain"));
         resourceMap.put("ezloader.js",new Resource("org/crux/web/js/ezloader.js","text/plain"));
         resourceMap.put("ezmenuie.js",new Resource("org/crux/web/js/ezmenuie.js","text/plain"));
         resourceMap.put("ezmenuns.js",new Resource("org/crux/web/js/ezmenuns.js","text/plain"));

         resourceMap.put("3d_divider.gif",new Resource("org/crux/web/images/ez/3d_divider.gif","image/gif"));
         resourceMap.put("arrow.gif",new Resource("org/crux/web/images/ez/arrow.gif","image/gif"));
         resourceMap.put("arrowhi.gif",new Resource("org/crux/web/images/ez/arrowhi.gif","image/gif"));
         resourceMap.put("bgimg.gif",new Resource("org/crux/web/images/ez/bgimg.gif","image/gif"));
         resourceMap.put("bullet.gif",new Resource("org/crux/web/images/ez/bullet.gif","image/gif"));
         resourceMap.put("button.gif",new Resource("org/crux/web/images/ez/button.gif","image/gif"));
         resourceMap.put("cascade.gif",new Resource("org/crux/web/images/ez/cascade.gif","image/gif"));
         resourceMap.put("circle.gif",new Resource("org/crux/web/images/ez/circle.gif","image/gif"));
         resourceMap.put("divider.gif",new Resource("org/crux/web/images/ez/divider.gif","image/gif"));
         resourceMap.put("dot.gif",new Resource("org/crux/web/images/ez/dot.gif","image/gif"));
         resourceMap.put("gradient.gif",new Resource("org/crux/web/images/ez/gradient.gif","image/gif"));
         resourceMap.put("line.gif",new Resource("org/crux/web/images/ez/line.gif","image/gif"));
         resourceMap.put("mouse.gif",new Resource("org/crux/web/images/ez/mouse.gif","image/gif"));
         resourceMap.put("panels.gif",new Resource("org/crux/web/images/ez/panels.gif","image/gif"));
         resourceMap.put("picdn.gif",new Resource("org/crux/web/images/ez/picdn.gif","image/gif"));
         resourceMap.put("picup.gif",new Resource("org/crux/web/images/ez/picup.gif","image/gif"));
         resourceMap.put("winxp.gif",new Resource("org/crux/web/images/ez/winxp.gif","image/gif"));*/

      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void doFilter(ServletRequest srq, ServletResponse srp, FilterChain chain) throws IOException, ServletException {
      //chain.doFilter(srq,srp);
      //if (true) return ;

      final HttpServletRequest rq = (HttpServletRequest) srq;
      final HttpServletResponse rp = (HttpServletResponse) srp;

      try {

         SessionManager.getInstance().setServlet(srq,srp,null);

         Session session = (Session)rq.getSession().getAttribute("USER_SESSION");
         if (session==null) {
            session = new SessionImpl();
            session.setStUserID("anon");
            session.setDtTransactionDate(new Date());
            rq.getSession().setAttribute("SESSION",session);

            //srq.setAttribute("ERROR_MESSAGE", new LoginTimeOutException("Login Timeout"));
            //final String forwardURL = (ev.getEvent("LOGIN_TIMEOUT")).getDefaultAction().getStPageURL();
            //rq.getRequestDispatcher("pages/main/LoginTO.jsp").forward(rq,rp);
            //return;

            chain.doFilter(srq,srp);
            return;
         }

         try {
            SessionManager.getInstance().setSession(session);
            SessionManager.getInstance().setServlet(srq,srp, servletContext);

            //logger.logDebug("doFilter: session = "+SessionManager.getInstance().getSession());

            servletContext = rq.getSession().getServletContext();



            /*System.out.println("filter in : ");
            System.out.println("filter in : "+servletContext);
            System.out.println("filter in : "+((servletContext!=null)?servletContext.getRealPath("\\"):null));

            if (application.getBasePath()==null)
               application.setBasePath(servletContext.getRealPath("\\"));*/

            if (servletContext!=null)
               formManager.addFormsPath(servletContext.getRealPath(File.separator));

            if (rq.getAttribute("REQ_HANDLED")==null) {
               rq.setAttribute("REQ_HANDLED","1");

               String uri = rq.getRequestURI().toLowerCase();


               //System.out.println("uri = "+uri);

               final String event = rq.getParameter("EVENT");

               if (event!=null)
                  if (event.indexOf(".crux")>=0) uri=event;

               final int m = uri.lastIndexOf('/');

               final String rsrckey = uri.substring(m+1,uri.length());

               logger.logDebug("doFilter: rsrckey = "+rsrckey);

               final Resource resx = (Resource)resourceMap.get(rsrckey);

               if (resx!=null) {
                  rp.setContentType(resx.getContentType());
                  //final String exp = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z").format(new Date(System.currentTimeMillis()+3*24*60*60*1000));
                  //logger.logDebug("doFilter: exp = "+exp);
                  rp.setDateHeader("Expires", System.currentTimeMillis()+(long)(3l*24l*60l*60l*1000l));
                  rp.setHeader("Content-Disposition","inline; filename=" +rsrckey+';');

                  logger.logDebug("doFilter: Expires : "+System.currentTimeMillis()+(long)(3l*24l*60l*60l*1000l));
                  logger.logDebug("doFilter: Content-Disposition : "+"inline; filename=" +rsrckey+';');
                  rp.getOutputStream().write(resx.getBytes());
                  rp.getOutputStream().flush();
                  return;
               }

               com.crux.common.controller.Helper.processFlowCard(rq);

               //System.out.println("rq.getContextPath() = "+rq.getContextPath());

               String rsrc;
               if (uri.indexOf(rq.getContextPath())==0)
                  rsrc = uri.substring(rq.getContextPath().length(),uri.length()).trim();
               else
                  rsrc = uri;

               if (rsrc.length()>0 && rsrc.charAt(0)=='/') rsrc = rsrc.substring(1,rsrc.length());

               if (rsrc==null || rsrc.length()<1) {
                  //rp.sendRedirect(rq.getContextPath()+"/start.crux");
                  chain.doFilter(srq,srp);
                  return;
                  //rsrc = "start.crux";
               }

               //System.out.println("rsrc = "+rsrc);

               final int n = rsrc.lastIndexOf('.');

               if (n>=0) {
                  final String rsrctype = rsrc.substring(n+1,rsrc.length());
                  String rsrcname = rsrc.substring(0,n);
                  //System.out.println("rsrc type="+rsrctype+",name="+rsrcname);

                  String action=null;
                  final int dotIndex = rsrcname.indexOf('.');

                  if (dotIndex>=0) {
                     action = rsrcname.substring(dotIndex+1,rsrcname.length());
                     rsrcname = rsrcname.substring(0,dotIndex);
                  }

                  if ("crux".equalsIgnoreCase(rsrctype)) {

                     String activity=rsrcname;
                     if (action!=null) activity+=":"+action;
                     rq.getSession().setAttribute("ACTIVITY", activity);

                     String uid = session==null?"?":session.getStUserID();

                     //logger.logInfo("[H2HC] "+uid+" : "+activity);

                     disableCache(rp);

                     final boolean newForm = SessionManager.getInstance().getForm(rq.getParameter("formid"))==null;

                     Form form = FormManager.getInstance().getForm(rsrcname,rq.getParameter("formid"));

                     rq.setAttribute("FORM",form);

                     try {
                        updateFormProperties(form,rq);
                     } catch (Exception e) {
                        final String ticket = CruxController.putError(e);
                        form.setStErrorTicket(ticket);
                     }

                     if (form.getStErrorTicket()==null) {
                        if (newForm)
                           form.initialize();



                        if (action!=null) {
                           if(!hideLogger) logger.logDebug("doFilter: action = "+action); 

                           form.setStActionEvent(action);
                           form.executeActionEvent();

                           if (form.getStErrorTicket()!=null){
                              rp.sendRedirect("error.crux?ticket="+form.getStErrorTicket());
                              return;
                           }
                           //form.setStActionEvent(null);
                        }
                     }



                     form = SessionManager.getInstance().getCurrentForm();

                     if (form==null) {
                        final String redirect = SessionManager.getInstance().getRedirect();
                        rq.getRequestDispatcher(redirect).forward(rq,rp);
                        return;
                     }

                     rq.setAttribute("FORM",form);
                     String stPresentation = form.getPresentation();
                     stPresentation = stPresentation.replace('\\','/');
                     if(!hideLogger) logger.logDebug("doFilter: stPresentation = "+stPresentation);
                     rq.getRequestDispatcher(stPresentation).forward(rq,rp);

                     return;
                  }
                  else if ("lov".equalsIgnoreCase(rsrctype)) {
                     LOVServer.getInstance().serve(rsrcname, rq,rp);
                     return;
                  }
               }

               //logger.logError("unable to handle request");

               //rq.getRequestDispatcher("/login.jsp").forward(rq,rp);

               // srp.getWriter().println(rsrc);
            }

            //logger.logDebug("doFilter: chaining "+rq.getRequestURI()+" ...");

            chain.doFilter(srq,srp);
         } finally {
            //SessionManager.getInstance().setSession(null);
         }
      } catch (Throwable e) {
         e.printStackTrace();

         final String ticket = putError(e);
         rp.sendRedirect("error.crux?ticket="+ticket);
         //throw new RuntimeException(e);
      }
   }

   public static HashMap errorTickets = new HashMap();
   public static long ticketCounter = 0;

   public static Object getError(String ticket) {
      return errorTickets.get(ticket);
   }

   public static String putError(Object e) {

      if (e instanceof Throwable) {
         ((Throwable)e).printStackTrace();
      }

      final String ticket = String.valueOf(ticketCounter++);

      e = LogUtil.traverseError(e);

      logger.logError("[H2HC] "+SessionManager.getInstance().getUserID()+" : "+e);

      errorTickets.put(ticket, e);
      return ticket;
   }

   private void updateFormProperties(Form form, HttpServletRequest rq) throws Exception {
      final Enumeration names = rq.getParameterNames();

      form.beforeUpdateForm();

      final Iterator keys = form.getPropMap().keySet().iterator();

      while (keys.hasNext()) {
         String k = (String) keys.next();

         //final String pn = (String) form.getPropMap().get(k);
         form.setProperty(k,rq.getParameter(k));
      }

      while (names.hasMoreElements()) {
         String pn = (String) names.nextElement();

         if (form.getPropMap().containsKey(pn)) continue;


         String[] v = rq.getParameterValues(pn);
         if (v!=null && v.length>1) {
            form.setProperty(pn,v);
         } else {
            form.setProperty(pn,rq.getParameter(pn));
         }
      }

      form.afterUpdateForm();

      SessionManager.getInstance().show(form);

      if (form.getStActionEvent()!=null) {
         String activity = (String)rq.getSession().getAttribute("ACTIVITY");

         activity+=":"+form.getStActionEvent();

         rq.getSession().setAttribute("ACTIVITY",activity);

         //logger.logInfo("[H2HC] "+SessionManager.getInstance().getUserID()+" : "+activity);

      }

      form.executeActionEvent();
   }

   public void destroy() {

   }

   public static void disableCache(HttpServletResponse httpServletResponse) {
      // Set to expire far in the past.
      httpServletResponse.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");

      // Set standard HTTP/1.1 no-cache headers.
      httpServletResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

      // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
      httpServletResponse.addHeader("Cache-Control", "post-check=0, pre-check=0");

      // Set standard HTTP/1.0 no-cache header.
      httpServletResponse.setHeader("Pragma", "no-cache");
   }
}
