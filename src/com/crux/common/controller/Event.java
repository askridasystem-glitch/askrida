/***********************************************************************
 * Module:  com.crux.common.controller.Event
 * Author:  Denny Mahendra
 * Created: Mar 5, 2004 4:11:17 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.controller;

import com.crux.util.LogUtil;

import java.lang.reflect.Method;
import java.util.HashMap;

public class Event {
   private String stEventID;
   private String stEventName;
   private String stFlags;
   private boolean cacheable;
   private Class clHelperClass;
   private Method mtHelperMethod;
   private HashMap hmActions;
   private Helper helperInstance;
   private boolean responseEnabled;

   public boolean isCacheable() {
      return cacheable;
   }

   public void setCacheable(boolean cacheable) {
      this.cacheable = cacheable;
   }

   public Helper getHelperInstance() throws Exception {
      if (helperInstance == null)
         helperInstance = (Helper) clHelperClass.newInstance();
      return helperInstance;
   }

   public EventAction getDefaultAction() {
      return (EventAction) hmActions.get(null);
   }

   public HashMap getHmActions() {
      if (hmActions == null) hmActions = new HashMap();
      return hmActions;
   }

   public Class getClHelperClass() {
      return clHelperClass;
   }

   public void setClHelperClass(Class clHelperClass) {
      this.clHelperClass = clHelperClass;
   }

   public Method getMtHelperMethod() {
      return mtHelperMethod;
   }

   public void setMtHelperMethod(Method mtHelperMethod) {
      this.mtHelperMethod = mtHelperMethod;
   }

   public String getStEventID() {
      return stEventID;
   }

   public void setStEventID(String stEventID) {
      this.stEventID = stEventID;
   }

   public String getStEventName() {
      return stEventName;
   }

   public void setStEventName(String stEventName) {
      this.stEventName = stEventName;
   }

   public String toString() {
      return LogUtil.logGetterMethods(this);
   }

   public boolean isResponseEnabled() {
      return responseEnabled;
   }

   public void setResponseEnabled(boolean responseEnabled) {
      this.responseEnabled = responseEnabled;
   }

   public String getStFlags() {
      return stFlags;
   }

   public void setStFlags(String stFlags) {
      if (stFlags!=null) {
         String[] flags = stFlags.split(",");
         for (int i = 0; i < flags.length; i++) {
            String flag = flags[i];

            if ("cacheable".equalsIgnoreCase(flag)) setCacheable(true);
         }
      }
      this.stFlags = stFlags;
   }
}
