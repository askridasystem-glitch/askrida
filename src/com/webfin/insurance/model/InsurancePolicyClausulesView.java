/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyClausulesView
 * Author:  Denny Mahendra
 * Created: Nov 2, 2005 8:39:44 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.common.codedecode.Codec;
import com.crux.pool.DTOPool;
import com.crux.util.Tools;
import com.webfin.FinCodec;

import java.math.BigDecimal;

public class InsurancePolicyClausulesView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ins_pol_clausules
(
  ins_pol_claus_id int8 NOT NULL,
  ins_clause_id int8,
  pol_id int8,
  rate numeric,
  rate_type varchar(5),
  amount numeric,
  ins_pol_obj_id int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_pol_clausules_pk PRIMARY KEY (ins_pol_claus_id)
)
   */

   public static String tableName = "ins_pol_clausules";

   public static String fieldMap[][] = {
      {"stPolicyClauseID","ins_pol_claus_id*pk*nd"},
      {"stClauseID","ins_clause_id"},
      {"stClauseID2","ins_clause_id2*n"},
      {"stPolicyID","pol_id"},
      {"dbRate","rate"},
      //{"stDescription2","description2"},
      {"dbRateDefault","ratedef*n"},
      {"stRateType","rate_type"},
      {"stRateType2","rate_type2*n"},
      {"dbAmount","amount"},
      {"stPolicyObjectID","ins_pol_obj_id"},

      {"stDescription","shortdesc*n"},
      {"stWording","description*n"},
      {"stActiveFlag","active_flag*n"},
      {"stLevel","clause_level*n"},
      {"stDefaultFlag","f_default*n"},
      {"stWordingNew","description_new*n"},
      {"stReference1","ref1*n"},
      {"stKeterangan","keterangan"},
      {"stChildClausules","child_clausules*n"},
      {"stParentID","parent_id*n"},
   };

   private String stDefaultFlag;
   private String stSelectedFlag;
   private String stWording;
   private String stDescription;
   private String stActiveFlag;
   private String stLevel;

   private String stPolicyClauseID;
   private String stClauseID;
   private String stClauseID2;
   private String stPolicyID;
   private BigDecimal dbRate;
   private BigDecimal dbRateDefault;
   private String stRateType;
   private String stRateType2;
   private BigDecimal dbAmount;
   private String stPolicyObjectID;
   private String stDescription2;
   private String stWordingNew;
   private String stReference1;
   private String stKeterangan;
   private String stChildClausules;
   private String stParentID;

   public String getStPolicyClauseID() {
      return stPolicyClauseID;
   }

   public void setStPolicyClauseID(String stPolicyClauseID) {
      this.stPolicyClauseID = stPolicyClauseID;
   }

   public String getStClauseID() {
      return stClauseID;
   }

   public void setStClauseID(String stClauseID) {
      this.stClauseID = stClauseID;
   }

   public String getStPolicyID() {
      return stPolicyID;
   }

   public void setStPolicyID(String stPolicyID) {
      this.stPolicyID = stPolicyID;
   }

   public BigDecimal getDbRate() {
      return dbRate;
   }

   public void setDbRate(BigDecimal dbRate) {
      this.dbRate = dbRate;
   }

   public String getStRateType() {
      return stRateType;
   }

   public void setStRateType(String stRateType) {
      this.stRateType = stRateType;
   }

   public BigDecimal getDbAmount() {
      return dbAmount;
   }

   public void setDbAmount(BigDecimal dbAmount) {
      this.dbAmount = dbAmount;
   }

   public String getStPolicyObjectID() {
      return stPolicyObjectID;
   }

   public void setStPolicyObjectID(String stPolicyObjectID) {
      this.stPolicyObjectID = stPolicyObjectID;
   }

   public String getStWording() {
      return stWording;
   }

   public void setStWording(String stWording) {
      this.stWording = stWording;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }
   
   /*
   public String getStDescription2() {
      return stDescription2;
   }

   public void setStDescription2(String stDescription2) {
      this.stDescription2 = stDescription2;
   }*/

   public String getStActiveFlag() {
      return stActiveFlag;
   }

   public void setStActiveFlag(String stActiveFlag) {
      this.stActiveFlag = stActiveFlag;
   }


   public void recalculate() {
   }

   public boolean isStandard() {
      return FinCodec.ClauseRateType.STANDARD.equalsIgnoreCase(stRateType);
   }

   public void setStLevel(String stLevel) {
      this.stLevel = stLevel;
   }

   public String getStLevel() {
      return stLevel;
   }

   public boolean isObjectClause() {
      return Tools.isEqual(stLevel, FinCodec.ClauseLevel.OBJECT);

   }

   public void setStClauseID2(String stClauseID2) {
      this.stClauseID = stClauseID2;
      this.stClauseID2 = stClauseID2;
   }

   public String getStClauseID2() {
      return stClauseID2;
   }

   public void setStRateType2(String stRateType2) {
      this.stRateType = stRateType2;
      this.stRateType2 = stRateType2;
   }

   public String getStRateType2() {
      return stRateType2;
   }

   public void setDbRateDefault(BigDecimal dbRateDefault) {
      this.dbRateDefault = dbRateDefault;
   }

   public BigDecimal getDbRateDefault() {
      return dbRateDefault;
   }

   public void setStSelectedFlag(String stSelectedFlag) {
      this.stSelectedFlag = stSelectedFlag;
      if (Tools.isYes(stSelectedFlag)) select(); else deSelect();
   }

   public String getStSelectedFlag() {
      return stSelectedFlag;
   }

   public String getStDefaultFlag() {
      return stDefaultFlag;
   }

   public void setStDefaultFlag(String stDefaultFlag) {
      this.stDefaultFlag = stDefaultFlag;
   }

    public String getStWordingNew() {
        return stWordingNew;
    }

    public void setStWordingNew(String stWordingNew) {
        this.stWordingNew = stWordingNew;
    }

    public String getStReference1() {
        return stReference1;
    }

    public void setStReference1(String stReference1) {
        this.stReference1 = stReference1;
    }

    /**
     * @return the stKeterangan
     */
    public String getStKeterangan() {
        return stKeterangan;
    }

    /**
     * @param stKeterangan the stKeterangan to set
     */
    public void setStKeterangan(String stKeterangan) {
        this.stKeterangan = stKeterangan;
    }

    /**
     * @return the stChildClausules
     */
    public String getStChildClausules() {
        return stChildClausules;
    }

    /**
     * @param stChildClausules the stChildClausules to set
     */
    public void setStChildClausules(String stChildClausules) {
        this.stChildClausules = stChildClausules;
    }

    /**
     * @return the stParentID
     */
    public String getStParentID() {
        return stParentID;
    }

    /**
     * @param stParentID the stParentID to set
     */
    public void setStParentID(String stParentID) {
        this.stParentID = stParentID;
    }

    public String getStClausulesDesc() {

      final InsuranceClausulesView cc = getClausules();

      if (cc==null) return "";

      return cc.getStShortDescription();
   }

    public InsuranceClausulesView getClausules() {
      return (InsuranceClausulesView) DTOPool.getInstance().getDTO(InsuranceClausulesView.class, stClauseID);
   }

}
