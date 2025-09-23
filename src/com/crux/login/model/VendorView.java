/***********************************************************************
 * Module:  com.crux.login.model.RoleView
 * Author:  Denny Mahendra
 * Created: Apr 27, 2004 1:39:58 PM
 * Purpose:
 ***********************************************************************/

package com.crux.login.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.common.codedecode.Codec;
import com.crux.util.DTOList;

import java.util.Date;

public class VendorView extends DTO implements RecordAudit {
   private String stVendorID;
   private String stVendorName;

   public static String comboFields[] = {"vendor_id", "vendor_name"};

   public static String tableName = "v_vendors";

   public static String fieldMap[][] = {
      {"stVendorID", "vendor_id*pk"},
      {"stVendorName", "vendor_name"}
   };

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
