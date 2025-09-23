/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceCoverSourceView
 * Author:  Denny Mahendra
 * Created: Mar 19, 2006 8:49:30 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;

public class InsuranceCoverSourceView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ins_cover_source
(
  ins_cover_source_id int8 NOT NULL,
  description  varchar(128),
  active_flag  varchar(1),
  ar_trx_type_id int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_cover_source_pk PRIMARY KEY (ins_cover_source_id)
) without oids;
ALTER TABLE ins_cover_source ADD COLUMN claim_trx_type_id int8;

   */

   public static String tableName = "ins_cover_source";

   public static String fieldMap[][] = {
      {"stInsuranceCoverSourceID","ins_cover_source_id*pk"},
      {"stDescription","description"},
      {"stActiveFlag","active_flag"},
      {"stARTransactionTypeID","ar_trx_type_id"},
      {"stShareMode","share_mode"},
      {"stAPTransactionTypeID","ap_trx_type_id"},
      {"stInwardFlag","inward_flag"},
      {"stCoinsFlag","coins_flag"},
      {"stClaimTrxTypeID","claim_trx_type_id"},
   };

   private String stInsuranceCoverSourceID;
   private String stDescription;
   private String stActiveFlag;
   private String stARTransactionTypeID;
   private String stAPTransactionTypeID;
   private String stShareMode;
   private String stInwardFlag;
   private String stCoinsFlag;
   private String stClaimTrxTypeID;

   public String getStClaimTrxTypeID() {
      return stClaimTrxTypeID;
   }

   public void setStClaimTrxTypeID(String stClaimTrxTypeID) {
      this.stClaimTrxTypeID = stClaimTrxTypeID;
   }

   public String getStInwardFlag() {
      return stInwardFlag;
   }

   public void setStInwardFlag(String stInwardFlag) {
      this.stInwardFlag = stInwardFlag;
   }

   public String getStCoinsFlag() {
      return stCoinsFlag;
   }

   public void setStCoinsFlag(String stCoinsFlag) {
      this.stCoinsFlag = stCoinsFlag;
   }

   public String getStInsuranceCoverSourceID() {
      return stInsuranceCoverSourceID;
   }

   public void setStInsuranceCoverSourceID(String stInsuranceCoverSourceID) {
      this.stInsuranceCoverSourceID = stInsuranceCoverSourceID;
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

   public String getStARTransactionTypeID() {
      return stARTransactionTypeID;
   }

   public void setStARTransactionTypeID(String stARTransactionTypeID) {
      this.stARTransactionTypeID = stARTransactionTypeID;
   }

   public String getStShareMode() {
      return stShareMode;
   }

   public void setStShareMode(String stShareMode) {
      this.stShareMode = stShareMode;
   }

   public boolean isLeader() {
      return Tools.isEqual(stShareMode,"LEADER");
   }
   
   public boolean isMember() {
      return Tools.isEqual(stShareMode,"MEMBER");
   }

   public String getStAPTransactionTypeID() {
      return stAPTransactionTypeID;
   }

   public void setStAPTransactionTypeID(String stAPTransactionTypeID) {
      this.stAPTransactionTypeID = stAPTransactionTypeID;
   }

   public boolean isInward() {
      return "Y".equalsIgnoreCase(stInwardFlag);
   }

   public boolean isCoins() {
      return "Y".equalsIgnoreCase(stCoinsFlag);
   }
   
   public boolean isCoinsSelf() {
      return Tools.isEqual(stInsuranceCoverSourceID,"COINSOUTSELF");
   }
}
