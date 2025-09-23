/***********************************************************************
 * Module:  com.crux.web.controller.Events
 * Author:  Denny Mahendra
 * Created: Mar 5, 2004 4:10:05 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.web.controller;

import com.crux.util.FileEnumerator;
import com.crux.util.LogUtil;
import com.crux.util.LogManager;
import com.crux.util.Tools;
import org.w3c.dom.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;
import java.util.HashMap;

public class Events {
   private HashMap events = new HashMap();

   private final static transient LogManager logger = LogManager.getInstance(Events.class);

   public Event getEvent(String stEventName) {
      return (Event) events.get(stEventName);
   }

   public boolean isEventCallingClass(String stEventID, Class x) {
      final Event ev = (Event)events.get(stEventID);

      if (ev==null) return false;

      if (ev.getClHelperClass() == null) return false;

      return ev.getClHelperClass().equals(x);
   }

   public Events(ServletContext context) throws Exception {
      final String file = context.getResource("/xml/control/control.xml").toString();

      final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setValidating(false);
      dbf.setNamespaceAware(true);
      final DocumentBuilder db = dbf.newDocumentBuilder();
      final Document doc = db.parse(file);

      final Element root = doc.getDocumentElement();

      final NodeList nodes = root.getElementsByTagName("control-file");

      for (int i = 0; i < nodes.getLength(); i++) {
         final Node node = nodes.item(i);

         final String ctlFile = getNodeValue(node,"name");

         if (ctlFile == null) throw new IllegalArgumentException("Control file name must be provided");

         final URL rsrc = context.getResource("/xml/control/"+ctlFile);

         if (rsrc == null) throw new IllegalArgumentException("Cannot find control file : "+ctlFile);

         loadFile(rsrc.toString());
      }
   }

   public Events(String stControlConfigPath) throws Exception {
      final FileEnumerator fe = new FileEnumerator(stControlConfigPath);

      while (fe.hasMoreElements()) {
         String f = (String) fe.nextElement();

         if (f.endsWith(".xml"))
            loadFile(stControlConfigPath+File.separator+f);
      }



      //System.out.println(events);
   }

   public void loadFile(String stFileName) throws Exception {
      final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setValidating(false);
      dbf.setNamespaceAware(true);
      final DocumentBuilder db = dbf.newDocumentBuilder();
      final Document doc = db.parse(stFileName);

      final Element root = doc.getDocumentElement();

      final NodeList nodes = root.getChildNodes();

      for (int i=0;i<nodes.getLength();i++) {
         final Node node = nodes.item(i);

         //System.out.println(node.getNodeName()+"="+node.getNodeValue());

         if (node.getNodeName().equalsIgnoreCase("event")) {
            final Event event = new Event();

            event.setStEventID(getNodeValue(node,"name"));

            if (event.getStEventID() == null) throw new IllegalArgumentException("Event ID should not be null ["+stFileName+"] !");

            events.put(event.getStEventID(), event);

            //System.out.println(event.getStEventID());

            final NodeList eventchild = node.getChildNodes();

            for (int j=0;j<eventchild.getLength();j++) {
               final Node n1 = eventchild.item(j);

               final String nname = n1.getNodeName();

               if ("helper".equalsIgnoreCase(nname)) {

                  final String stClassName = n1.getAttributes().getNamedItem("class").getNodeValue();
                  final String stMethodName = n1.getAttributes().getNamedItem("method").getNodeValue();
                  try {
                     final Class helperClass = Class.forName(stClassName);
                     event.setClHelperClass(helperClass);
                     try {
                        event.setMtHelperMethod(
                              helperClass.getMethod(
                                    stMethodName,
                                    new Class[] {HttpServletRequest.class}
                              )
                        );
                     }
                     catch (NoSuchMethodException e) {
                        event.setMtHelperMethod(
                              helperClass.getMethod(
                                    n1.getAttributes().getNamedItem("method").getNodeValue(),
                                    new Class[] {HttpServletRequest.class, HttpServletResponse.class}
                              )
                        );
                        event.setResponseEnabled(true);
                     }
                  }
                  catch (ClassNotFoundException e) {
                     logger.logError("Class Not Found : "+stClassName);
                  }
                  catch (NoSuchMethodException e) {
                     logger.logError("Method Not Found : "+stClassName+"."+stMethodName);
                  }
                  catch (Exception e) {
                     e.printStackTrace();  //To change body of catch statement use Options | File Templates.
                  }

               } else if ("forward".equalsIgnoreCase(nname)) {
                  final EventAction act = new EventAction();

                  act.setStActionID(getNodeValue(n1, "id"));
                  act.setStEventID(getNodeValue(n1, "event"));
                  act.setStPageURL(getNodeValue(n1, "page"));

                  event.getHmActions().put(act.getStActionID(), act);
               }
            }
         }
      }
   }

   public String getNodeValue(Node node, String stNodeId) {
      final Node attr = node.getAttributes().getNamedItem(stNodeId);
      return (attr==null) ? null : attr.getNodeValue();
   }

   public String toString() {
      return LogUtil.logGetterMethods(this);
   }
}
