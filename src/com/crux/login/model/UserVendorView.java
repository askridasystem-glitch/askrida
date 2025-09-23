/***********************************************************************
 * Module:  com.crux.login.model.UserOutletView
 * Author:  Denny Mahendra
 * Created: Jul 16, 2004 9:31:12 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.login.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

public class UserVendorView extends DTO implements RecordAudit {
   private String stUserID;
   private String stVendorID;
   private String stVendorName;

   public static String comboFields[] = {"vendor_id", "vendor_name"};

   public static String tableName = "s_user_vendor";

   public static String fieldMap[][] = {
      {"stUserID","user_id*pk*nd"},
      {"stVendorID","vendor_id*pk"},
      {"stVendorName","vendor_name*n"},
   };

   public String getStUserID() {
      return stUserID;
   }

   public void setStUserID(String stUserID) {
      this.stUserID = stUserID;
   }

   public String getStVendorID() {
      return stVendorID;
   }

   public void setStVendorID(String stVendorID) {
      this.stVendorID = stVendorID;
   }

   public String getStVendorName() {
      return stVendorName;
   }

   public void setStVendorName(String stVendorName) {
      this.stVendorName = stVendorName;
   }
}
