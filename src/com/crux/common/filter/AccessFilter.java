/***********************************************************************
 * Module:  com.crux.common.filter.AccessFilter
 * Author:  Denny Mahendra
 * Created: Aug 25, 2004 9:50:17 AM
 * Purpose:
 ***********************************************************************/

package com.crux.common.filter;

import com.crux.util.LogManager;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;

public class AccessFilter implements Filter {
   private final static transient LogManager logger = LogManager.getInstance(AccessFilter.class);

   private HashSet allowedPath = null;
   //private boolean secureRedirect = Parameter.readBoolean("SECURE_REDIRECT");
   private boolean secureRedirect = true;

   public void init(FilterConfig filterConfig) throws ServletException {
   }

   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

      //logger.logDebug("doFilter: in");

      final HttpServletRequest rq = (HttpServletRequest) servletRequest;
      final HttpSession session = rq.getSession();
      HttpServletResponse rp = (HttpServletResponse) servletResponse;

      if (secureRedirect)
         if (rq.getRequestURL().indexOf("https")<0) {
            URL url = new URL(rq.getRequestURL().toString());

            String red = "https://"+url.getHost()+rq.getContextPath();

            rp.getWriter().println("<html>");
            rp.getWriter().println("You're being redirected to a secure port");
            rp.getWriter().println("</html>");
            rp.getWriter().println("<script>");
            rp.getWriter().println("function gonext() {document.location='"+red+"';};");
            rp.getWriter().println("window.setTimeout('gonext();',1000);");
            rp.getWriter().println("</script>");

            //rp.sendRedirect(red);
            return;
         }

      final String uri = rq.getRequestURI().toLowerCase();

      try {
         //logger.logDebug("doFilter: uri = "+uri);

         final char lastchar = uri.charAt(uri.length()-1);

         if (allowedPath==null) {
            allowedPath = new HashSet();
            allowedPath.add((rq.getContextPath()).toLowerCase());
            allowedPath.add((rq.getContextPath()).toLowerCase()+'/');
            allowedPath.add((rq.getContextPath()).toLowerCase()+"/index.jsp");
            allowedPath.add((rq.getContextPath()).toLowerCase()+"/crashit.jsp");
            allowedPath.add((rq.getContextPath()).toLowerCase()+"/syncserverservlet");
            allowedPath.add((rq.getContextPath()).toLowerCase()+'\\');

            //logger.logDebug("doFilter: allowedPath = "+allowedPath);
         }

         final boolean allow = (allowedPath.contains(uri));

         if (!allow) {
            if (
                  //(uri.indexOf(".jsp")<0) &&
                  (uri.indexOf(".js")<0) &&
                  (uri.indexOf(".gif")<0) &&
                  (uri.indexOf(".jpg")<0) &&
                  (uri.indexOf(".css")<0) &&
                  (uri.indexOf(".ctl")<0) &&
                  (uri.indexOf(".pdf")<0) &&
                  (uri.indexOf(".crux")<0) &&
                  (uri.indexOf(".fop")<0) &&
                  (uri.indexOf(".xls")<0) &&
                  (uri.indexOf(".chm")<0) &&
                  (uri.indexOf(".swf")<0)
            ) {
               throw new Exception("Invalid resource type");
            }

            switch (lastchar) {
               case '\\':
               case '/':
                  throw new Exception("Directory listing not allowed");
            }

            if (uri.indexOf(".jsp")>=0) throw new Exception("Directory jsp not allowed");
         }

         //if ("Y".equals(session.getAttribute("INPROCESS"))) throw new Exception("Your process still being processed");

         //session.setAttribute("INPROCESS","Y");

         filterChain.doFilter(servletRequest, servletResponse);

         //logger.logDebug("doFilter: out");

         //session.removeAttribute("INPROCESS");
      }
      catch (Exception e) {
         e.printStackTrace();

         logger.logWarning("Access denied : "+uri);

         servletRequest.setAttribute("MESSAGE",e.getMessage());

         servletRequest.getRequestDispatcher("/pages/main/accessdenied.jsp").forward(servletRequest, servletResponse);
      }
   }

   public void destroy() {
   }
}
