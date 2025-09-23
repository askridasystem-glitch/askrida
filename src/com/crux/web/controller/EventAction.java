/***********************************************************************
 * Module:  com.crux.web.controller.EventAction
 * Author:  Denny Mahendra
 * Created: Mar 5, 2004 4:13:57 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.web.controller;

import com.crux.util.LogUtil;

public class EventAction {
   private String stActionID;

   private String stPageURL;
   private String stEventID;

   public String getStActionID() {
      return stActionID;
   }

   public void setStActionID(String stActionID) {
      this.stActionID = stActionID;
   }

   public String getStEventID() {
      return stEventID;
   }

   public void setStEventID(String stEventID) {
      this.stEventID = stEventID;
   }

   public String getStPageURL() {
      if (stEventID != null) {
         return "ctl.ctl?EVENT="+stEventID;
      }
      return stPageURL;
   }

   public void setStPageURL(String stPageURL) {
      this.stPageURL = stPageURL;
   }

   public String toString() {
      return LogUtil.logGetterMethods(this);
   }
}
