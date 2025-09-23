/***********************************************************************
 * Module:  com.crux.util.ServletUtil
 * Author:  Denny Mahendra
 * Created: May 31, 2006 12:57:57 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import com.crux.web.controller.CruxController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;

public class ServletUtil {
   public static boolean isSupportGZIP(HttpServletRequest request) {
      String encoding = request.getHeader("accept-encoding");

      if (encoding!=null && encoding.indexOf("gzip")>=0)  return true;

      String userAgent = request.getHeader("user-agent");

      if (userAgent!=null && userAgent.indexOf("MSIE 6")>=0) return true;
      if (userAgent!=null && userAgent.indexOf("Mozilla/5")>=0) return true;

      return false;
   }

   public static String printHeaders(HttpServletRequest rq) {

      Enumeration headerNames = rq.getHeaderNames();

      HashMap m = new HashMap();

      while (headerNames.hasMoreElements()) {
         String nm = (String) headerNames.nextElement();

         m.put(nm, rq.getHeader(nm));
      }

      return m.toString();
   }
}
