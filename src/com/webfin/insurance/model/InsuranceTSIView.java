/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceTSIView
 * Author:  Denny Mahendra
 * Created: Jan 27, 2006 5:33:06 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

public class InsuranceTSIView extends DTO implements RecordAudit {
/*
CREATE TABLE ins_tsi_cat
(
  ins_tsi_cat_id int8 NOT NULL,
  description varchar(128),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_tsi_cat_pk PRIMARY KEY (ins_tsi_cat_id)
)
*/

   public static String tableName = "ins_tsi_cat";

   public static String fieldMap[][] = {
      {"stInsuranseTSIID","ins_tsi_cat_id*pk"},
      {"stDescription","description"},
      {"stExcTSIFlag","exc_tsi_flag"},
   };

   public static String comboFields[] = {"ins_tsi_cat_id","description"};


   private String stInsuranseTSIID;
   private String stDescription;
   private String stExcTSIFlag;
   private String stExcluded;

   public String getStInsuranseTSIID() {
      return stInsuranseTSIID;
   }

   public void setStInsuranseTSIID(String stInsuranseTSIID) {
      this.stInsuranseTSIID = stInsuranseTSIID;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }
   
   public String getStExcTSIFlag() {
      return stExcTSIFlag;
   }

   public void setStExcTSIFlag(String stExcTSIFlag) {
      this.stExcTSIFlag = stExcTSIFlag;
   }
   
   /*public boolean isExcluded(){
   	   return Tools.isYes(stExcluded);
   }
   
   public void setStExcluded(String stExcluded) {
      this.stExcluded = stExcluded;
   }*/
}
