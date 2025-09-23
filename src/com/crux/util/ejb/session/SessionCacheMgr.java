/***********************************************************************
 * Module:  com.crux.util.ejb.session.SessionCacheMgr
 * Author:  Denny Mahendra
 * Created: Mar 9, 2004 2:09:45 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util.ejb.session;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.ejb.EJBHome;
import java.util.HashMap;

public class SessionCacheMgr {
   private static SessionCacheMgr staticinstance;
   private InitialContext ctx;

   private HashMap sessions = new HashMap();

   public static SessionCacheMgr getInstance() {
      if (staticinstance == null) staticinstance = new SessionCacheMgr();
      return staticinstance;
   }

   private SessionCacheMgr() {
   }

   public void loadSession(String stRemoteClassName) throws Exception {
      final Class remote = Class.forName(stRemoteClassName);
      final SessionCache sc = new SessionCache();

      sc.setRemoteClass(remote);

      String stHomeName = stRemoteClassName + "Home";

      if (ctx == null)
         ctx = new InitialContext();

      final int i = stRemoteClassName.lastIndexOf('.');

      final String ejbName = stRemoteClassName.substring(i+1,stRemoteClassName.length());

      final Object obj = ctx.lookup(ejbName+"EJB");

      final Class homeClass = Class.forName(stHomeName);

      final EJBHome home = (EJBHome)PortableRemoteObject.narrow(obj, homeClass);

      sc.setCreateMethod(homeClass.getMethod("create", null));

      sc.setHome(home);
      sc.setStEJBName(ejbName);

      sessions.put(stRemoteClassName, sc);
   }

   public Object getSession(String stRemoteClassName) throws Exception {

      SessionCache sessionCache = (SessionCache)sessions.get(stRemoteClassName);

      if (sessionCache == null) {
         loadSession(stRemoteClassName);
         sessionCache = (SessionCache)sessions.get(stRemoteClassName);
      }

      return sessionCache.getRemote();
   }
}
