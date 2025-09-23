/***********************************************************************
 * Module:  com.crux.web.controller.ControllerServlet
 * Author:  Denny Mahendra
 * Created: Mar 4, 2004 2:53:12 PM
 * Purpose:
 ***********************************************************************/

package com.crux.web.controller;



import com.crux.util.LogManager;
import com.crux.util.SQLUtil;
import com.crux.util.ThreadContext;
import com.crux.util.IDFactory;
import com.crux.common.exception.LoginTimeOutException;
import com.crux.login.model.UserSessionView;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;

public class ControllerServlet extends HttpServlet {
   ServletContext context;
   com.crux.web.controller.Events ev;

   private static transient ControllerServlet instance = null;

   private boolean blockUsers = false;
   private String stBlockReason = "Server is currently being blocked";
   private long lastInvalidate;
   private ArrayList listeners;
   private boolean hideLogger = false;

   public interface Listener {
      public void onInvoke(Class helperClass);
   }

   public void addListener(Listener x) {
      if (listeners == null)
         listeners = new ArrayList();

      listeners.add(x);
   }

   int requestid = 1;

   public com.crux.web.controller.Events getEvents() {
      return ev;
   }

   public void setEvents(com.crux.web.controller.Events ev) {
      this.ev = ev;
   }

   public boolean isBlockUsers() {
      return blockUsers;
   }

   public void setBlockUsers(boolean blockUsers) {
      this.blockUsers = blockUsers;
   }

   public String getStBlockReason() {
      return stBlockReason;
   }

   public void setStBlockReason(String stBlockReason) {
      this.stBlockReason = stBlockReason;
   }

   public static ControllerServlet getInstance() {
      return instance;
   }

   private final static transient LogManager logger = LogManager.getInstance(ControllerServlet.class);

   /*public static void main(String [] args) throws Exception {
      //final File file = new File("http://localhost:8080/trikomsel");
      final URL u = new URL("http://localhost:8080/trikomsel");

      final Socket sock = new Socket("localhost",8080);

      final InputStream is = sock.getInputStream();

      final PrintWriter os = new PrintWriter(sock.getOutputStream());

      os.print("GET /\n");

      byte [] buffer = new byte [512];

      while (is.read(buffer)>0) {
         System.out.write(buffer);
      }

   }*/

   public void init(ServletConfig servletConfig) throws ServletException {
      super.init(servletConfig);

      logger.logInfo("FLOW GUARD is "+(Helper.useFlowGuard?"on":"off"));
      logger.logInfo("Concurrency Protection is "+(SQLUtil.useLockProtect?"on":"off"));

      context = servletConfig.getServletContext();
      try {
         ev = new Events(context);
         context.setAttribute("EVENTCOLLECTION",ev);

         if (instance != null) throw new IllegalStateException("Me cannot be instantiated twice");
         instance = this;
      }
      catch (Exception e) {
         e.printStackTrace();
         throw new ServletException(e.toString());
      }
   }

   protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
      try {
         ThreadContext.getInstance().clear();
         ThreadContext.getInstance().put("HTTP_REQUEST", httpServletRequest);
         doPostx(httpServletRequest, httpServletResponse);
      } finally {
         ThreadContext.getInstance().remove("HTTP_REQUEST");
      }
   }

   protected void doPostx(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
      try {
         //disableCache(httpServletResponse);

         if (System.currentTimeMillis()-lastInvalidate>10000) { // invalidates every 10 seconds
            lastInvalidate = System.currentTimeMillis();
            SessionKeeper.getInstance().invalidate();
         }

         UserSessionView us = (UserSessionView) httpServletRequest.getSession().getAttribute("USER_SESSION");

         //if (blockUsers) {
            //if ((us!=null) && (!"admin".equalsIgnoreCase(us.getStUserID())))
         //   if (us==null)
         //      throw new Exception(stBlockReason);
         //}

         final String stEventName = httpServletRequest.getParameter("EVENT");

         if (us==null)
            if (stEventName != null)
               if (stEventName.indexOf("LOG")!=0) throw new LoginTimeOutException("Login Timeout");

//         logger.logDebug("ControllerServlet.doPost Parameters ["+stEventName+"]: \n"+LogUtil.logParameters(httpServletRequest));

         logger.logDebug("doPost: processing event: "+stEventName);

         Helper.processFlowCard(httpServletRequest);

         final Event event = ev.getEvent(stEventName);

         ThreadContext.getInstance().put("EVENT",stEventName);

         httpServletRequest.setAttribute("CURRENT_EVENT", event);
         httpServletRequest.setAttribute("REQUEST_TIME", new Date());
         httpServletRequest.setAttribute("REQUEST_ID", new Long(requestid++));
         httpServletRequest.setAttribute("REQUEST_THREAD", Thread.currentThread());

         if (event == null) throw new IllegalArgumentException("Invalid Event ID");

         final HttpSession session = httpServletRequest.getSession();

         session.setAttribute("CURRENT_EVENT", event);

         Object retVal = null;

         boolean forceForward=false;

         if (event.getClHelperClass() != null) {
            final Helper hp = event.getHelperInstance();
            if (event.getMtHelperMethod() == null) throw new Exception("Error retrieving helper method");
            if(!hideLogger) logger.logDebug("doPost: Invoking "+event.getClHelperClass()+"."+event.getMtHelperMethod().getName());
            try {
               long t = System.currentTimeMillis();

               if (listeners!=null) {
                  for (int i = 0; i < listeners.size(); i++) {
                     Listener listener = (Listener) listeners.get(i);
                     listener.onInvoke(event.getClHelperClass());
                  }
               }

               if (event.isResponseEnabled())
                  retVal = event.getMtHelperMethod().invoke(hp, new Object[] {httpServletRequest, httpServletResponse});
               else
                  retVal = event.getMtHelperMethod().invoke(hp, new Object[] {httpServletRequest});
               t = System.currentTimeMillis() - t;
               httpServletRequest.setAttribute("INVOKE_TIME",new Long(t));
               //logger.logDebug("doPost: ["+stEventName+"] invoke time = "+t+" ms");

               if (event.isResponseEnabled()) return;
            }
            catch (LoginTimeOutException e) {
               throw e;
            }
            catch (IllegalAccessException e) {
               e.printStackTrace();  //To change body of catch statement use Options | File Templates.
            }
            catch (InvocationTargetException e) {
               IDFactory.cancelCache();
               final Throwable tgt = e.getTargetException();
               tgt.printStackTrace();
               if (tgt instanceof  LoginTimeOutException) throw tgt;
               httpServletRequest.setAttribute("ERROR_MESSAGE", tgt);
               retVal = HelperAction.forward("ERROR");
               forceForward = true;
            }
            catch (Exception e) {
               IDFactory.cancelCache();
               httpServletRequest.setAttribute("ERROR_MESSAGE", e);
               retVal = HelperAction.forward("ERROR");
               forceForward = true;
            }
         }

         if (retVal != null) logger.logDebug("doPost: retval = "+retVal);

         if (!event.isResponseEnabled() || forceForward) {
            if (retVal instanceof HelperAction) {
               final HelperAction ha = (HelperAction) retVal;
               if (ha.getStForwardID() != null) {
                  final EventAction act = (EventAction) event.getHmActions().get(ha.getStForwardID());
                  if (act != null) {
                     final String forwardURL = act.getStPageURL();
                     logger.logDebug("doPost: forwarding :  "+ha.getStForwardID()+" ["+forwardURL+"] ");
                     httpServletRequest.getRequestDispatcher(forwardURL).forward(httpServletRequest, httpServletResponse);
                  } else if ("ERROR".equalsIgnoreCase(ha.getStForwardID())) {
                     //final Throwable t = (Throwable) httpServletRequest.getAttribute("ERROR_MESSAGE");
                     //httpServletResponse.getWriter().print("ERROR : "+t.toString());
                     httpServletRequest.getRequestDispatcher("/pages/main/Error.jsp").forward(httpServletRequest, httpServletResponse);
                  } else if (act==null) {
                     httpServletRequest.setAttribute("ERROR_MESSAGE", new Exception("Unknown event ID"));
                     httpServletRequest.getRequestDispatcher("/pages/main/Error.jsp").forward(httpServletRequest, httpServletResponse);
                  }
               }
            } else {
               final String forwarURL = event.getDefaultAction().getStPageURL();
               if(!hideLogger) logger.logDebug("doPost: [noretval] forwarding : "+forwarURL);
               httpServletRequest.getRequestDispatcher(forwarURL).forward(httpServletRequest, httpServletResponse);
            }
         } else {
            final String forwarURL = event.getDefaultAction().getStPageURL();
            logger.logDebug("doPost: forwarding : "+forwarURL);
            httpServletRequest.getRequestDispatcher(forwarURL).forward(httpServletRequest, httpServletResponse);
         }

      }
      catch (LoginTimeOutException e) {
         e.printStackTrace();
         logger.logDebug("doPost: catching login timeout");
         httpServletRequest.getSession().invalidate();
         httpServletRequest.setAttribute("ERROR_MESSAGE", e);
         final String forwardURL = (ev.getEvent("LOGIN_TIMEOUT")).getDefaultAction().getStPageURL();
         httpServletRequest.getRequestDispatcher(forwardURL).forward(httpServletRequest, httpServletResponse);
         return;
      }
      catch (Throwable e) {
         if (e instanceof InvocationTargetException) e = (Throwable) ((InvocationTargetException) e).getTargetException();
         logger.logError("ERROR = "+e);
         e.printStackTrace();
         //throw new ServletException(e.toString());
      }
   }

   public static void disableCache(HttpServletResponse httpServletResponse) {
      /*// Set to expire far in the past.
      httpServletResponse.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");

      // Set standard HTTP/1.1 no-cache headers.
      httpServletResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

      // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
      httpServletResponse.addHeader("Cache-Control", "post-check=0, pre-check=0");

      // Set standard HTTP/1.0 no-cache header.
      httpServletResponse.setHeader("Pragma", "no-cache");*/
   }

   protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
      doPost(httpServletRequest,httpServletResponse);
   }
}
