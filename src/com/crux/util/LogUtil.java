/***********************************************************************
 * Module:  com.crux.util.LogUtil
 * Author:  Denny Mahendra
 * Created: Mar 5, 2004 4:44:31 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.io.InputStream;
import java.io.IOException;

public class LogUtil {
   public static String logParameters(HttpServletRequest q) {

      Enumeration e = q.getParameterNames();

      StringBuffer b = new StringBuffer();

      while (e.hasMoreElements()) {
         String s = (String) e.nextElement();
         b.append(s + " = " + q.getParameter(s) + ";\n");
      }

      return b.toString();

   }

   public static String logAttributes(HttpServletRequest q) {

      Enumeration e = q.getAttributeNames();

      StringBuffer b = new StringBuffer();

      while (e.hasMoreElements()) {
         String s = (String) e.nextElement();
         b.append(s + " = " + q.getAttribute(s) + "\n");
      }

      return b.toString();

   }

   public static String logLimitLength(String stLogMessage, int iCount) {

      if (stLogMessage.length() > iCount)
         return stLogMessage.substring(0, iCount) + "...";
      else
         return stLogMessage;

   }

   public static String getLogStr(HashMap m) {
      Iterator k = m.keySet().iterator();

      StringBuffer sz = new StringBuffer();

      while (k.hasNext()) {
         String s = (String) k.next();

         sz.append(s).append(" = ").append(m.get(s)).append("\n");
      }

      return sz.toString();
   }

   public static void logStackTrace() {
      try {
         throw new Exception();
      }
      catch (Throwable e) {
         e.printStackTrace();  //To change body of catch statement use Options | File Templates.
      }
   }

   public static String logFields(Object p) {
      if (p == null) return null;

      try {
         final Field[] fields = p.getClass().getFields();

         final StringBuffer sz = new StringBuffer(p.getClass().getName()).append("fields :\n");

         for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];

            sz.append(field.getName()).append(" = ").append(String.valueOf(field.get(p))).append("\n");
         }

         return sz.toString();
      }
      catch (Exception e) {
         e.printStackTrace();  //To change body of catch statement use Options | File Templates.
         return e.toString();
      }
   }

   public static String logGetterMethods(Object o) {
      if (o == null) {
         return null;
      }

      try {
         Object x = o;

         final Method[] methods = x.getClass().getMethods();

         final StringBuffer sz = new StringBuffer();

         for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];

            final String name = method.getName();

            if ((
                  (name.indexOf("getSt") == 0) ||
                  (name.indexOf("getLg") == 0) ||
                  (name.indexOf("getDb") == 0) ||
                  (name.indexOf("getBd") == 0) ||
                  (name.indexOf("getDt") == 0)
                  ) && (method.getParameterTypes().length == 0)) {
               try {
                  sz.append(name).append(":").append(method.invoke(x,null)).append(";\n");
               }
               catch (InvocationTargetException e) {
                  sz.append(name).append(":(").append(e.getTargetException().toString()).append(");\n");
               }
            }
            //else if ((name.indexOf("get") == 0) && (method.getParameterTypes().length == 0))
            //   sz.append(name+":"+method.invoke(x,null)+";\n");
         }

         return "[" + sz.toString() + "]";
      }
      catch (Exception e) {
         e.printStackTrace();
         return e.toString();
      }
   }

   public void logStream(InputStream inp) throws IOException {
      byte [] buf = new byte[1024];
      int n;

      while ((n=inp.read(buf,0,buf.length))>0) {
         System.out.println(new String(buf,0,n));
      }
   }

   public static Object traverseError(Object e) {
      if (e instanceof Throwable) {
         Throwable t = (Throwable)e;

         try {
            while (true) {
               if (t.getClass().getName().equalsIgnoreCase("org.apache.jasper.JasperException")) {
                  Throwable p = (Throwable) t.getClass().getMethod("getRootCause",null).invoke(t,null);
                  if (p!=null) t=p; else break;
               }
               else if (t.getCause()!=null) {
                  t=t.getCause();
               }
               else break;
            }
         } catch (Exception e1) {
            throw new RuntimeException(e1);
         }
         e=t;
      }
      return e;
   }

   /*public static Object traverseError2(Object e) {
      if (e instanceof Throwable) {
         Throwable t = (Throwable)e;

         try {
            while (true) {
               if (t.getClass().getName().equalsIgnoreCase("org.apache.axis.AxisFault")) {
                  //String s = (String)t.getClass().getMethod("getFaultString",null).invoke(t,null);

                  AxisFault ax = (AxisFault)t;

                  //String s=ax.getMessage()+"\n"+JSPUtil.xmlEscape(XMLUtils.getInnerXMLString(ax.getFaultDetails()[0]));
                  //String s=ax.getMessage()+"\n"+AxisUtil.translateWebMethodException(ax.getFaultDetails());

                  String message = ax.getMessage();

                  String faultString = ax.getFaultString();

                  String faultXML = XMLUtils.getInnerXMLString(ax.getFaultDetails()[0]);

                  if (message==null) message="";

                  if (faultXML==null) faultXML="";


                  RuntimeException r = new RuntimeException("AXIS Fault: "+message+"\n"+"XML:"+faultXML);

                  r.setStackTrace(t.getStackTrace());

                  t=r;
                  break;
               } else if (t.getClass().getName().equalsIgnoreCase("org.apache.jasper.JasperException")) {
                  Throwable p = (Throwable) t.getClass().getMethod("getRootCause",null).invoke(t,null);
                  if (p!=null) t=p; else break;
               }
               else if (t.getCause()!=null) {
                  t=t.getCause();
               }
               else break;
            }
         } catch (Exception e1) {
            throw new RuntimeException(e1);
         }
         e=t;
      }
      return e;
   }*/

   public static String getStackTraceString(Throwable t) {

      StringBuffer sz = new StringBuffer();

      sz.append("Error Description : "+t.toString());
      sz.append("\n");


      final StackTraceElement[] stackTrace = t.getStackTrace();

      int l=0;

      for (int i = 0; i < stackTrace.length; i++) {
         StackTraceElement stackTraceElement = stackTrace[i];

         final String cls = stackTraceElement.getClassName();

         if (
                 (cls.indexOf("mandiri")>=0) ||
                 (cls.indexOf("crux")>=0) ||
                 (cls.indexOf("webfin")>=0) ||
                 (cls.indexOf(".jsp")>=0)
         ) {
            l++;
            sz.append(l+". ");
            sz.append(stackTraceElement.getFileName()+":"+stackTraceElement.getLineNumber());
            sz.append("\n");

            //if (l>0) break;
         }


      }

      return sz.toString();
   }
}
