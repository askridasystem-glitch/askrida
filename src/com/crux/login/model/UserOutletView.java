/***********************************************************************
 * Module:  com.crux.login.model.UserOutletView
 * Author:  Denny Mahendra
 * Created: Jul 16, 2004 9:31:12 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.login.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

public class UserOutletView extends DTO implements RecordAudit {
   private String stUserID;
   private String stOutletID;
   private String stOutletDesc;

   public static String tableName = "s_user_outlet";

   public static String fieldMap[][] = {
      {"stUserID","user_id*pk*nd"},
      {"stOutletID","outletid*pk"},
      {"stOutletDesc","outletname*n"},
   };

   public String getStUserID() {
      return stUserID;
   }

   public void setStUserID(String stUserID) {
      this.stUserID = stUserID;
   }

   public String getStOutletID() {
      return stOutletID;
   }

   public void setStOutletID(String stOutletID) {
      this.stOutletID = stOutletID;
   }

   public String getStOutletDesc() {
      return stOutletDesc;
   }

   public void setStOutletDesc(String stOutletDesc) {
      this.stOutletDesc = stOutletDesc;
   }
}
