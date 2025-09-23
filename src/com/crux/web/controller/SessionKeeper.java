/***********************************************************************
 * Module:  com.crux.web.controller.SessionKeeper
 * Author:  Denny Mahendra
 * Created: Jul 16, 2004 2:39:54 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.web.controller;

import com.crux.util.LogManager;

import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

public class SessionKeeper implements HttpSessionListener {
   private HashMap sessions = new HashMap();
   private final static transient LogManager logger = LogManager.getInstance(SessionKeeper.class);

   private static SessionKeeper staticinstance;

   public static SessionKeeper getInstance() {
      return staticinstance;
   }

   public SessionKeeper() {
      staticinstance = this;
   }

   public void sessionCreated(HttpSessionEvent event) {
      final HttpSession session = event.getSession();
      final String id = session.getId();
      sessions.put(id, session);
      logger.logDebug("sessionCreated: "+id);
   }

   public void sessionDestroyed(HttpSessionEvent event) {
      final HttpSession session = event.getSession();
      final String id = session.getId();
      sessions.remove(id);
      logger.logDebug("sessionDestroyed: "+id);
   }

   public Collection getSessions() {
      return sessions.values();
   }

   public void invalidate() {
      final Iterator keys = sessions.keySet().iterator();

      final ArrayList del = new ArrayList();

      final long t = System.currentTimeMillis();

      while (keys.hasNext()) {
         String stSessionID = (String) keys.next();

         final HttpSession s = (HttpSession)sessions.get(stSessionID);

         if (t-s.getLastAccessedTime()>20*60*1000) { // timeout in 20 mins
            del.add(s);
         }
      }

      for (int i = 0; i < del.size(); i++) {
         HttpSession session = (HttpSession) del.get(i);
         session.invalidate();
      }
   }
}
