/***********************************************************************
 * Module:  com.crux.common.filter.ProfilingFilter
 * Author:  Denny Mahendra
 * Created: Oct 6, 2004 5:40:05 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.filter;

import com.crux.common.model.UserSession;
import com.crux.util.LogManager;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ProfilingFilter implements Filter {

   private final static transient LogManager logger = LogManager.getInstance(ProfilingFilter.class);

   public static long cpc = 0;

   public void init(FilterConfig filterConfig) throws ServletException {
   }

   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
      //logger.logDebug("doFilter: in");

      adjustCPC(1);

      try {
         String event = servletRequest.getParameter("EVENT");
         final HttpServletRequest rq = (HttpServletRequest)servletRequest;
         long t = System.currentTimeMillis();
         if (event==null) event = ((HttpServletRequest)servletRequest).getRequestURI();
         UserSession uv = (UserSession) rq.getSession().getAttribute("USER_SESSION");
         if (uv!=null) event=uv.getStUserID()+":"+event;
         filterChain.doFilter(servletRequest, servletResponse);
         t = System.currentTimeMillis()-t;
         final Long invoketime = (Long)rq.getAttribute("INVOKE_TIME");
         logger.logDebug("access time = "+t+(invoketime==null?"":","+invoketime)+" ms ["+event+"] ");
      } finally {
         adjustCPC(-1);
      }


      //logger.logDebug("doFilter: out");
   }

   private static synchronized void adjustCPC(int i) {

      cpc = cpc + i;

   }

   public void destroy() {
   }
}

