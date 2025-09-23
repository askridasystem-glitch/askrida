/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicySubTypeView
 * Author:  Denny Mahendra
 * Created: Nov 3, 2005 11:35:06 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

import java.math.BigDecimal;

public class InsurancePolicySubTypeView extends DTO implements RecordAudit {
   /*

   CREATE TABLE ins_policy_subtype
(
  pol_type_id int8 NOT NULL,
  pol_subtype_id int8 NOT NULL,
  description varchar(128),
  active_flag varchar(1),
  premi_rate numeric,
  gl_production varchar(32),
   varchar(32),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_policy_subtype_pk PRIMARY KEY (pol_subtype_id)
)
*/

   public static String tableName = "ins_policy_subtype";

   public static String fieldMap[][] = {
      {"stPolicyTypeID","pol_type_id"},
      {"stPolicySubTypeID","pol_subtype_id*pk"},
      {"stDescription","description"},
      {"stActiveFlag","active_flag"},
      {"dbPremiRate","premi_rate"},
      {"stGLProduction","gl_production"},
      {"stGLCost","gl_cost"},
      {"stGLCode","gl_code"},
   };

   public static String comboFields[] = {"pol_subtype_id","description"};

   private String stPolicyTypeID;
   private String stPolicySubTypeID;
   private String stDescription;
   private String stActiveFlag;
   private BigDecimal dbPremiRate;
   private String stGLProduction;
   private String stGLCost;
   private String stGLCode;

   public String getStGLCode() {
      return stGLCode;
   }

   public void setStGLCode(String stGLCode) {
      this.stGLCode = stGLCode;
   }

   public String getStPolicyTypeID() {
      return stPolicyTypeID;
   }

   public void setStPolicyTypeID(String stPolicyTypeID) {
      this.stPolicyTypeID = stPolicyTypeID;
   }

   public String getStPolicySubTypeID() {
      return stPolicySubTypeID;
   }

   public void setStPolicySubTypeID(String stPolicySubTypeID) {
      this.stPolicySubTypeID = stPolicySubTypeID;
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

   public BigDecimal getDbPremiRate() {
      return dbPremiRate;
   }

   public void setDbPremiRate(BigDecimal dbPremiRate) {
      this.dbPremiRate = dbPremiRate;
   }

   public String getStGLProduction() {
      return stGLProduction;
   }

   public void setStGLProduction(String stGLProduction) {
      this.stGLProduction = stGLProduction;
   }

   public String getStGLCost() {
      return stGLCost;
   }

   public void setStGLCost(String stGLCost) {
      this.stGLCost = stGLCost;
   }
}
