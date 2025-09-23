/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceCoverView
 * Author:  Denny Mahendra
 * Created: Jan 28, 2006 5:07:58 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.webfin.FinCodec;

public class InsuranceCoverView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ins_cover
(
  ins_cover_id int8 NOT NULL,
  description varchar(128),
  cover_category varchar(16),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_cover_pk PRIMARY KEY (ins_cover_id)
)
   */

   public static String tableName = "ins_cover";

   public static String fieldMap[][] = {
      {"stInsuranceCoverageID","ins_cover_id*pk"},
      {"stDescription","description"},
      {"stCoverCategory","cover_category"},
      {"stShortDesc","short_desc"},
      {"stFlag","flag"},
   };

   public static String comboFields[] = {"ins_cover_id","description"};


   private String stInsuranceCoverageID;
   private String stDescription;
   private String stCoverCategory;
   private String stShortDesc;
   private String stFlag;

   public String getStInsuranceCoverageID() {
      return stInsuranceCoverageID;
   }

   public void setStInsuranceCoverageID(String stInsuranceCoverageID) {
      this.stInsuranceCoverageID = stInsuranceCoverageID;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public String getStCoverCategory() {
      return stCoverCategory;
   }

   public void setStCoverCategory(String stCoverCategory) {
      this.stCoverCategory = stCoverCategory;
   }

   public boolean isMainCover() {
      return FinCodec.CoverCategory.MAIN.equalsIgnoreCase(stCoverCategory);
   }
   
   public String getStShortDesc() {
      return stShortDesc;
   }

   public void setStShortDesc(String stShortDesc) {
      this.stShortDesc = stShortDesc;
   }
   
   public String getStFlag() {
      return stFlag;
   }

   public void setStFlag(String stFlag) {
      this.stFlag = stFlag;
   }
}
