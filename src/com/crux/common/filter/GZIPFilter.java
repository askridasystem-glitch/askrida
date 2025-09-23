/***********************************************************************
 * Module:  com.crux.common.filter.GZIPFilter
 * Author:  Denny Mahendra
 * Created: Jul 23, 2004 10:28:50 AM
 * Purpose:
 ***********************************************************************/

package com.crux.common.filter;

import com.crux.common.parameter.Parameter;
import com.crux.util.LogManager;
import com.crux.util.ServletUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GZIPFilter implements Filter {

   private final static transient LogManager logger = LogManager.getInstance(GZIPFilter.class);

   public void init(FilterConfig filterConfig) throws ServletException {
   }

   public void doFilter(ServletRequest req, ServletResponse res,
                        FilterChain chain) throws IOException, ServletException {
      if (req instanceof HttpServletRequest) {
         //logger.logDebug("doFilter: in");

         HttpServletRequest request = (HttpServletRequest) req;
         HttpServletResponse response = (HttpServletResponse) res;

         //logger.logDebug("gzipfilter in : "+request.getRequestURI()); mark logger

         //final boolean dontCompress = "GENERATE".equalsIgnoreCase(request.getParameter("EVENT"));
         //final boolean dontCompress = "DISABLED".equalsIgnoreCase((String)request.getAttribute("COMPRESSION"));
         final boolean dontCompress = "Y".equals(req.getAttribute("COMPRESSED")) || !Parameter.readBoolean("SYS_HTTP_COMPRESSION",true);


         //if (dontCompress) logger.logInfo("skipping compression"); mark logger
         if (!dontCompress) {
            if (ServletUtil.isSupportGZIP(request)) {
               //System.out.println("GZIP supported, compressing.");
               req.setAttribute("COMPRESSED","Y");
               GZIPResponseWrapper wrappedResponse =
                     new GZIPResponseWrapper(request,response);
               chain.doFilter(req, wrappedResponse);
               wrappedResponse.finishResponse();

               //logger.logDebug("doFilter: out");

               return;
            }
         }

         //logger.logDebug("doFilter: out");
      };

      chain.doFilter(req, res);
   }

   public void destroy() {
   }
}
