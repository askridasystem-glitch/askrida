/***********************************************************************
 * Module:  com.crux.login.model.UserLogView
 * Author:  Denny Mahendra
 * Created: Oct 24, 2006 4:56:30 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.login.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

public class UserLogView extends DTO implements RecordAudit{
   //USER_LOG_ID, USER_ACTION, REFERENCE1

   private String stUserLogId;
   private String stUserAction;
   private String stReference1;
   private String stUserId;

   public static String tableName = "s_user_log";

   public static String fieldMap[][] = {
      {"stUserLogId", "user_log_id*pk"},
      {"stUserAction", "user_action"},
      {"stReference1", "reference1"},
      {"stUserId", "user_id"},
   };

   public String getStUserId() {
      return stUserId;
   }

   public void setStUserId(String stUserId) {
      this.stUserId = stUserId;
   }

   public String getStUserLogId() {
      return stUserLogId;
   }

   public void setStUserLogId(String stUserLogId) {
      this.stUserLogId = stUserLogId;
   }

   public String getStUserAction() {
      return stUserAction;
   }

   public void setStUserAction(String stUserAction) {
      this.stUserAction = stUserAction;
   }

   public String getStReference1() {
      return stReference1;
   }

   public void setStReference1(String stReference1) {
      this.stReference1 = stReference1;
   }
}
