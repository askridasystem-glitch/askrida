/***********************************************************************
 * Module:  com.crux.common.filter.FOPFilter
 * Author:  Denny Mahendra
 * Created: Apr 9, 2006 11:28:27 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.filter;

import com.crux.util.LogManager;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FOPFilter implements Filter {

   private final static transient LogManager logger = LogManager.getInstance(FOPFilter.class);

   public void init(FilterConfig filterConfig) throws ServletException {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
      if (req instanceof HttpServletRequest) {
         //logger.logDebug("doFilter: in");

         logger.logDebug("doFilter: processing FOP ");

         HttpServletRequest request = (HttpServletRequest) req;
         HttpServletResponse response = (HttpServletResponse) res;

         final FOPResponseWrapper resp = new FOPResponseWrapper(response, request);

         String x = request.getRequestURI()+".jsp";

         x=x.substring(request.getContextPath().length(),x.length());

         logger.logDebug("doFilter: using resource : "+x);

         request.getRequestDispatcher(x).forward(request,resp);

         //chain.doFilter(req, resp);

         resp.finish();

         return;
      }
   }

   public void destroy() {
      //To change body of implemented methods use File | Settings | File Templates.
   }
}
