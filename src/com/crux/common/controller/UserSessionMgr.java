/***********************************************************************
 * Module:  com.crux.common.controller.UserSessionMgr
 * Author:  Denny Mahendra
 * Created: Jul 17, 2005 8:41:48 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.controller;

import com.crux.common.model.UserSession;
import com.crux.login.model.UserSessionView;

public class UserSessionMgr {
   private static ThreadLocal us = new ThreadLocal() {
      protected synchronized Object initialValue() {
         return null;
      }
   };

   private static UserSessionMgr staticinstance;

   public static UserSessionMgr getInstance() {
      if (staticinstance == null) staticinstance = new UserSessionMgr();
      return staticinstance;
   }

   private UserSessionMgr() {
   }

   public UserSessionView getUserSession() {
      return (UserSessionView) us.get();
   }

   public UserSession setUserSession(UserSession usv) {
      us.set(usv);
      return usv;
   }
}
