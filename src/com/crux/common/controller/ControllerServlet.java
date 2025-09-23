/***********************************************************************
 * Module:  com.crux.common.controller.ControllerServlet
 * Author:  Denny Mahendra
 * Created: Mar 4, 2004 2:53:12 PM
 * Purpose:
 ***********************************************************************/

package com.crux.common.controller;

import com.crux.common.exception.LoginTimeOutException;
import com.crux.common.exception.RTException;
import com.crux.common.parameter.Parameter;
import com.crux.login.model.UserSessionView;
import com.crux.util.LogManager;
import com.crux.util.LogUtil;
import com.crux.util.SQLUtil;
import com.crux.initializer.ejb.wds.SchemaInitializer;
import com.crux.web.controller.CruxController;
import com.crux.file.FileView;
import com.crux.web.controller.SessionManager;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.Part;
import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.ParamPart;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.URL;
import java.util.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class ControllerServlet extends HttpServlet {
   ServletContext context;
   Events ev;

   private static transient ControllerServlet instance = null;

   private boolean blockUsers = false;
   private String stBlockReason = "Server is currently being blocked";
   private long lastInvalidate;
   private String fileFOlder;
   private boolean makeAnnouncement = false;
   private String stAnnouncement = "Pengumuman";
   private String stColor;
   private String stUserID;
   private boolean showRole = false;
   private boolean hideLogger = false;
   
   
   public String getStUserID() {
      return stUserID;
   }

   public void setStUserID(String stUserID) {
      this.stUserID = stUserID;
   }
   
   public String getStColor() {
      return stColor;
   }

   public void setStColor(String stColor) {
      this.stColor = stColor;
   }

   public Events getEvents() {
      return ev;
   }

   public void setEvents(Events ev) {
      this.ev = ev;
   }

   public boolean isBlockUsers() {
      return blockUsers;
   }

   public void setBlockUsers(boolean blockUsers) {
      this.blockUsers = blockUsers;
   }
   
   public boolean isShowRole() {
      return showRole;
   }

   public void setShowRole(boolean showRole) {
      this.showRole = showRole;
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
   
   public boolean isAnnouncement() {
      return makeAnnouncement;
   }

   public void setAnnouncement(boolean makeAnnouncement) {
      this.makeAnnouncement = makeAnnouncement;
   }

   public String getStAnnouncement() {
      return stAnnouncement;
   }

   public void setStAnnouncement(String stAnnouncement) {
      this.stAnnouncement = stAnnouncement;
   }

   private final static transient LogManager logger = LogManager.getInstance(ControllerServlet.class);

   public static void main(String [] args) throws Exception {
      //final File file = new File("http://localhost:8080/ots");
      final URL u = new URL("http://localhost:8080/ots");

      final Socket sock = new Socket("localhost",8080);

      final InputStream is = sock.getInputStream();

      final PrintWriter os = new PrintWriter(sock.getOutputStream());

      os.print("GET /\n");

      byte [] buffer = new byte [512];

      while (is.read(buffer)>0) {
         System.out.write(buffer);
      }

   }

   public void init(ServletConfig servletConfig) throws ServletException {
      super.init(servletConfig);

      context = servletConfig.getServletContext();
      SchemaInitializer.setScript(context.getRealPath("/xml/initdump.sql"));

      logger.logInfo("FLOW GUARD is "+(Helper.useFlowGuard?"on":"off"));
      logger.logInfo("Concurrency Protection is "+(SQLUtil.useLockProtect?"on":"off"));

      fileFOlder = Parameter.readString("SYS_FILES_FOLDER");

      new File(fileFOlder).mkdirs();

      try {
         new File(fileFOlder).mkdir();
      } catch (Exception e) {
      }

      try {
         ev = new Events(context);
         context.setAttribute("EVENTCOLLECTION",ev);

         if (instance != null) throw new IllegalStateException("Me cannot be instantiated twice");
         instance = this;
      }
      catch (Throwable e) {
         logger.log(e);
         throw new RTException(e);
      }
   }

   protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
      UserSessionMgr.getInstance().setUserSession((UserSessionView) httpServletRequest.getSession().getAttribute("USER_SESSION"));
      doPost2(httpServletRequest, httpServletResponse);
      UserSessionMgr.getInstance().setUserSession(null);
   }

   protected void doPost2(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
      /*httpServletRequest.getRequestDispatcher("/main.jsp").forward(httpServletRequest, httpServletResponse);
      if (true) return;*/

      try {
         if (System.currentTimeMillis()-lastInvalidate>10000) { // invalidates every 10 seconds
            lastInvalidate = System.currentTimeMillis();
            SessionKeeper.getInstance().invalidate();
         }

         String contentType = httpServletRequest.getContentType();
         logger.logDebug("doPost: httpServletRequest.getContentType() = "+contentType);

         if (contentType!=null)
            if (contentType.indexOf("multipart")>=0) {
             
               String cc_code = SessionManager.getInstance().getSession().getStBranch()!=null?SessionManager.getInstance().getSession().getStBranch():"00";
             
               logger.logDebug("doPost: decoding multipart request ...");

               SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

               String sf = sdf.format(new Date());

               String tempPath = fileFOlder+File.separator+sf;
               String path1 = fileFOlder+File.separator;

               try {
                  new File(path1).mkdir();
                  new File(tempPath).mkdir();
               } catch (Exception e) {
               }

               MultipartParser parser =
                       new MultipartParser(httpServletRequest, 40*1024*1024, true, true, null);

               Hashtable parameters = new Hashtable();

               // Some people like to fetch query string parameters from
               // MultipartRequest, so here we make that possible.  Thanks to
               // Ben Johnson, ben.johnson@merrillcorp.com, for the idea.
               if (httpServletRequest.getQueryString() != null) {
                  // Let HttpUtils create a name->String[] structure
                  Hashtable queryParameters =
                          HttpUtils.parseQueryString(httpServletRequest.getQueryString());
                  // For our own use, name it a name->Vector structure
                  Enumeration queryParameterNames = queryParameters.keys();
                  while (queryParameterNames.hasMoreElements()) {
                     Object paramName = queryParameterNames.nextElement();
                     String[] values = (String[])queryParameters.get(paramName);
                     Vector newValues = new Vector();
                     for (int i = 0; i < values.length; i++) {
                        newValues.add(values[i]);
                     }
                     parameters.put(paramName, newValues);
                  }
               }

               Part part;
               while ((part = parser.readNextPart()) != null) {
                  if (!part.isFile()) {
                     ParamPart PRAT = (ParamPart) part;

                     parameters.put(part.getName(), PRAT.getStringValue());

                     continue;
                  }

                  FilePart filePart = (FilePart) part;

                  if (filePart.getFileName()==null) continue;

                  File fo = new File(tempPath+File.separator+System.currentTimeMillis());

                  FileOutputStream fos = new FileOutputStream(fo.getCanonicalPath());

                  try {
                     filePart.writeTo(fos);

                     fos.close();
                  } catch (Exception e) {

                     fos.close();
                     fo.delete();

                     throw e;
                  }

                  FileView fv = new FileView();

                  fv.markNew();

                  fv.setStOriginalName(filePart.getFileName());
                  fv.setStMimeType(filePart.getContentType());
                  fv.setDbOriginalSize(new BigDecimal(fo.length()));
                  fv.setDbFileSize(fv.getDbOriginalSize());
                  fv.setDtFileDate(new Date(fo.lastModified()));
                  fv.setStFilePath(fo.getCanonicalPath());

                  fv.determineFileType();

                  httpServletRequest.setAttribute(part.getName()+"_file", fv);
                  
                  logger.logDebug("doPost: httpServletRequest.getContentType() = "+contentType);
               }

               httpServletRequest = new HttpServletRequestWrapper(httpServletRequest,parameters);

               /*MultipartRequest mrq =
                       new MultipartRequest(
                               httpServletRequest,
                               "tempupload"
                       );
               httpServletRequest = new HttpServletRequestWrapper(httpServletRequest,mrq);*/
            }


         UserSessionView us = (UserSessionView) httpServletRequest.getSession().getAttribute("USER_SESSION");

         if (blockUsers) {
            if ((us!=null) && (!"admin".equalsIgnoreCase(us.getStUserID())))
               throw new Exception(stBlockReason);
         }

         final String stEventName = httpServletRequest.getParameter("EVENT");

         if (us==null)
            if (stEventName != null)
               if (stEventName.indexOf("LOG")!=0) throw new LoginTimeOutException("Login Timeout");

         logger.logDebug("ControllerServlet.doPost Parameters ["+stEventName+"]: \n"+LogUtil.logParameters(httpServletRequest));

         logger.logDebug("doPost: processing event: "+stEventName);

         String uid = us==null?"?":us.getStUserID();

         logger.logInfo("[H2HC] "+uid+" : doPost: processing event : "+stEventName);

         Helper.processFlowCard(httpServletRequest);

         final Event event = ev.getEvent(stEventName);

         if (event!=null)if (!event.isCacheable()) disableCache(httpServletResponse);

         httpServletRequest.setAttribute("CURRENT_EVENT", event);

         if (event!=null)
            httpServletRequest.getSession().setAttribute("ACTIVITY", event.getStEventID());

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
               logger.logError("[H2HC] "+SessionManager.getInstance().getUserID()+" : "+e);

               throw e;
            }
            catch (IllegalAccessException e) {
               logger.logError("[H2HC] "+SessionManager.getInstance().getUserID()+" : "+e);

               logger.log(e);
            }
            catch (InvocationTargetException e) {
               final Throwable tgt = e.getTargetException();
               logger.logError("[H2HC] "+SessionManager.getInstance().getUserID()+" : "+tgt);
               logger.log(tgt);
               if (tgt instanceof  LoginTimeOutException) throw tgt;
               httpServletRequest.setAttribute("ERROR_MESSAGE", tgt);
               retVal = HelperAction.forward("ERROR");
               forceForward = true;
            }
            catch (Throwable e) {
               logger.logError("[H2HC] "+SessionManager.getInstance().getUserID()+" : "+e);

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
                     return;
                  } else if ("ERROR".equalsIgnoreCase(ha.getStForwardID())) {
                     //final Throwable t = (Throwable) httpServletRequest.getAttribute("ERROR_MESSAGE");
                     //httpServletResponse.getWriter().print("ERROR : "+t.toString());
                     //httpServletRequest.getRequestDispatcher("/pages/main/Error.jsp").forward(httpServletRequest, httpServletResponse);

                     final Object e = httpServletRequest.getAttribute("ERROR_MESSAGE");
                     final String ticket = CruxController.putError(e);
                     httpServletResponse.sendRedirect("error.crux?ticket="+ticket);
                     return;

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
            httpServletRequest.getRequestDispatcher(forwarURL).include(httpServletRequest, httpServletResponse);
         }

      }
      catch (LoginTimeOutException e) {
         //e.printStackTrace();
         logger.logDebug("doPost: catching login timeout");
         httpServletRequest.getSession().invalidate();
         httpServletRequest.setAttribute("ERROR_MESSAGE", e);
         final String forwardURL = (ev.getEvent("LOGIN_TIMEOUT")).getDefaultAction().getStPageURL();
         httpServletRequest.getRequestDispatcher(forwardURL).forward(httpServletRequest, httpServletResponse);

         logger.logError("[H2HC] "+SessionManager.getInstance().getUserID()+" : "+e);

         return;
      }
      catch (Throwable e) {
         if (e instanceof InvocationTargetException) e = ((InvocationTargetException) e).getTargetException();
         //logger.logError("ERROR = "+e);
         logger.log(e);
         //e.printStackTrace();
         //throw new ServletException(e.toString());
      }
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

   protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
      doPost(httpServletRequest,httpServletResponse);
   }

}


