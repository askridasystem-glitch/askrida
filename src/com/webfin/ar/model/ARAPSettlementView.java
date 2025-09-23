/***********************************************************************
 * Module:  com.webfin.ar.model.ARAPSettlementView
 * Author:  Denny Mahendra
 * Created: Nov 19, 2006 8:41:49 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;

import java.util.HashMap;

public class ARAPSettlementView extends DTO implements RecordAudit {

   public static String tableName = "ar_settlement";

   public static String fieldMap[][] = {
      {"stARSettlementID","ar_settlement_id*pk"},
      {"stDescription","description"},
      {"stARAPAccount","arap_account"},
      {"stARAPAccountNeg","arap_account_neg"},
      {"stExcessAccount","excess_account"},
      {"stExcessAccountNeg","excess_account_neg"},
      {"stRateDiffAccount","rate_diff_account"},
      {"stRateDiffAccountNeg","rate_diff_account_neg"},
      {"stARAPTrxTypeID","arap_trx_type_id"},
      {"stControlFlags","control_flags"},
      {"stTrxType","trx_type"},
      {"stExcessDescription","excess_description"},
      {"stMenuID","menu_id"},
      {"stParameter1","params1"},
      {"stSearchCondition","search_condition"},
      {"stSearchKey","search_key"},

   };

   private String stARSettlementID;
   private String stDescription;
   private String stARAPAccount;
   private String stARAPAccountNeg;
   private String stExcessAccount;
   private String stExcessAccountNeg;
   private String stRateDiffAccount;
   private String stRateDiffAccountNeg;
   private String stARAPTrxTypeID;
   private String stControlFlags;
   private String stTrxType;
   private String stExcessDescription;
   private HashMap propMap;
   private String stMenuID;
   private String stParameter1;
   private String stSearchCondition;
   private String stSearchKey;
   
   public String getStMenuID() {
      return stMenuID;
   }

   public void setStMenuID(String stMenuID) {
      this.stMenuID = stMenuID;
   }

   public String getStExcessDescription() {
      return stExcessDescription;
   }

   public void setStExcessDescription(String stExcessDescription) {
      this.stExcessDescription = stExcessDescription;
   }

   public String getStTrxType() {
      return stTrxType;
   }

   public void setStTrxType(String stTrxType) {
      this.stTrxType = stTrxType;
   }

   public String getStControlFlags() {
      return stControlFlags;
   }

   public void setStControlFlags(String stControlFlags) {
      this.stControlFlags = stControlFlags;
   }

   public String getStARSettlementID() {
      return stARSettlementID;
   }

   public void setStARSettlementID(String stARSettlementID) {
      this.stARSettlementID = stARSettlementID;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public String getStARAPAccount() {
      return stARAPAccount;
   }

   public void setStARAPAccount(String stARAPAccount) {
      this.stARAPAccount = stARAPAccount;
   }

   public String getStARAPAccountNeg() {
      return stARAPAccountNeg;
   }

   public void setStARAPAccountNeg(String stARAPAccountNeg) {
      this.stARAPAccountNeg = stARAPAccountNeg;
   }

   public String getStExcessAccount() {
      return stExcessAccount;
   }

   public void setStExcessAccount(String stExcessAccount) {
      this.stExcessAccount = stExcessAccount;
   }

   public String getStExcessAccountNeg() {
      return stExcessAccountNeg;
   }

   public void setStExcessAccountNeg(String stExcessAccountNeg) {
      this.stExcessAccountNeg = stExcessAccountNeg;
   }

   public String getStRateDiffAccount() {
      return stRateDiffAccount;
   }

   public void setStRateDiffAccount(String stRateDiffAccount) {
      this.stRateDiffAccount = stRateDiffAccount;
   }

   public String getStRateDiffAccountNeg() {
      return stRateDiffAccountNeg;
   }

   public void setStRateDiffAccountNeg(String stRateDiffAccountNeg) {
      this.stRateDiffAccountNeg = stRateDiffAccountNeg;
   }

   public String getStARAPTrxTypeID() {
      return stARAPTrxTypeID;
   }

   public void setStARAPTrxTypeID(String stARAPTrxTypeID) {
      this.stARAPTrxTypeID = stARAPTrxTypeID;
   }

   public HashMap getPropMap() {
      if (propMap==null)
         propMap = Tools.getPropMap(stControlFlags);
      return propMap;
   }

   public boolean checkProperty(String key, String value) {
      return Tools.isEqual(value,(Comparable) getPropMap().get(key));
   }

   public boolean isNote() {
      return checkProperty("EN_NOTE","Y");
   }

   public boolean isAR() {
      return "AR".equalsIgnoreCase(getStTrxType());
   }

   public boolean isAP() {
      return "AP".equalsIgnoreCase(getStTrxType());
   }

    public String getStParameter1()
    {
        return stParameter1;
    }

    public void setStParameter1(String stParameter1)
    {
        this.stParameter1 = stParameter1;
    }

    /**
     * @return the stSearchCondition
     */
    public String getStSearchCondition() {
        return stSearchCondition;
    }

    /**
     * @param stSearchCondition the stSearchCondition to set
     */
    public void setStSearchCondition(String stSearchCondition) {
        this.stSearchCondition = stSearchCondition;
    }

    /**
     * @return the stSearchKey
     */
    public String getStSearchKey() {
        return stSearchKey;
    }

    /**
     * @param stSearchKey the stSearchKey to set
     */
    public void setStSearchKey(String stSearchKey) {
        this.stSearchKey = stSearchKey;
    }
}
