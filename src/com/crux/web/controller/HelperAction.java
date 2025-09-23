/***********************************************************************
 * Module:  com.crux.web.controller.HelperAction
 * Author:  Denny Mahendra
 * Created: Mar 9, 2004 9:11:47 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.web.controller;

public class HelperAction {
   private String stForwardID;
   private int action;

   public final static transient int FORWARD = 1;

   protected HelperAction() {
   }

   public static HelperAction forward(String stForwardID) {
      final HelperAction ha = new HelperAction();

      ha.stForwardID = stForwardID;
      ha.action = FORWARD;

      return ha;
   }

   public int getAction() {
      return action;
   }

   public String getStForwardID() {
      if (action != FORWARD) return null;
      return stForwardID;
   }

   public String toString() {
      return "{Forward action : "+stForwardID+"}";
   }
}
