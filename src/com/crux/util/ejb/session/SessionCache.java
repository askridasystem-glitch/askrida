/***********************************************************************
 * Module:  com.crux.util.ejb.session.SessionCache
 * Author:  Denny Mahendra
 * Created: Mar 9, 2004 2:09:22 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util.ejb.session;

import javax.ejb.EJBHome;
import java.lang.reflect.Method;

public class SessionCache {
   private EJBHome home;
   private Method createMethod;
   private String stEJBName;
   private Class remoteClass;

   public Method getCreateMethod() {
      return createMethod;
   }

   public void setCreateMethod(Method createMethod) {
      this.createMethod = createMethod;
   }

   public EJBHome getHome() {
      return home;
   }

   public void setHome(EJBHome home) {
      this.home = home;
   }

   public Class getRemoteClass() {
      return remoteClass;
   }

   public void setRemoteClass(Class remoteClass) {
      this.remoteClass = remoteClass;
   }

   public String getStEJBName() {
      return stEJBName;
   }

   public void setStEJBName(String stEJBName) {
      this.stEJBName = stEJBName;
   }

   public Object getRemote() throws Exception {
      return createMethod.invoke(home,null);
   }
}
