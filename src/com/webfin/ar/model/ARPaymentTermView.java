/***********************************************************************
 * Module:  com.webfin.ar.model.ARPaymentTermView
 * Author:  Denny Mahendra
 * Created: Dec 31, 2005 9:50:37 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

public class ARPaymentTermView extends DTO implements RecordAudit {
   private String stPaymentTermID;
   private String stDescription;
   private String stActiveFlag;
   private Long lgDueDays;

   public static String tableName = "payment_term";

   public static String fieldMap[][] = {
      {"stPaymentTermID","payment_term_id*pk"},
      {"stDescription","description"},
      {"stActiveFlag","active_flag"},
      {"lgDueDays","due_days"},
   };

   public static String comboFields[] = {"payment_term_id","description"};

   public String getStPaymentTermID() {
      return stPaymentTermID;
   }

   public void setStPaymentTermID(String stPaymentTermID) {
      this.stPaymentTermID = stPaymentTermID;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public String getStActiveFlag() {
      return stActiveFlag;
   }

   public void setStActiveFlag(String stActiveFlag) {
      this.stActiveFlag = stActiveFlag;
   }

   public Long getLgDueDays() {
      return lgDueDays;
   }

   public void setLgDueDays(Long lgDueDays) {
      this.lgDueDays = lgDueDays;
   }
}
