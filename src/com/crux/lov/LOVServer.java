/***********************************************************************
 * Module:  com.crux.lov.LOVServer
 * Author:  Denny Mahendra
 * Created: Jan 1, 2006 1:53:48 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.lov;

import com.crux.util.JSPUtil;
import com.crux.util.LOV;
import com.crux.util.LogManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

public class LOVServer {
   private static LOVServer staticinstance;

   private final static transient LogManager logger = LogManager.getInstance(LOVServer.class);

   public static LOVServer getInstance() {
      if (staticinstance == null) staticinstance = new LOVServer();
      return staticinstance;
   }

   private LOVServer() {
   }

   public void serve() {

   }

   public void serve(String rsrcname, HttpServletRequest rq, HttpServletResponse rp) throws Exception {
      /*final Iterator keyIts = rq.getParameterMap().keySet().iterator();

      final HashMap pars = new HashMap();

      while (keyIts.hasNext()) {
         String k = (String) keyIts.next();
         par.put(k,rp.g)
      }
*/

      final Map par = rq.getParameterMap();

      final Object ctlName = JSPUtil.getParameter(par,"ctl");
      final String callback = (String) JSPUtil.getParameter(par,"callback");

      final LOV lov = LOVManager.getInstance().getLOV(rsrcname,par);

      final PrintWriter writer = rp.getWriter();

      final String[] an = lov.getAttributeNames();

      writer.print("<script>");
      writer.print("lovContent = ");
      writer.print(lov.getJSObject());
      writer.println(';');
      writer.println("var c = window.parent.document.getElementById('"+ctlName+"');");
      writer.println("c.options.length=0;");
      writer.println("for (var i=0;i<lovContent.length;i++) {");
      writer.println("   var o = document.createElement('OPTION');");
      writer.println("   o.text=lovContent[i].text;");
      writer.println("   o.value=lovContent[i].value;");
      for (int i = 0; i < an.length; i++) {
         String s = an[i];
         writer.println("   o."+s+"=lovContent[i]."+s+";");
      }
      writer.println("   c.options.add(o);");
      writer.println("}");
      writer.println("");
      if (callback!=null) {
         writer.println("window.parent."+callback+"();");
         writer.println("window.parent."+callback+"=null;");
      }
      writer.println("</script>");
   }
}
