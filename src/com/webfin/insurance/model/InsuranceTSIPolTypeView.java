/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceTSIPolTypeView
 * Author:  Denny Mahendra
 * Created: Oct 1, 2006 1:43:17 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

public class InsuranceTSIPolTypeView extends DTO implements RecordAudit {

   /*CREATE TABLE ins_tsicat_poltype
(
  ins_tcpt_id int8 NOT NULL,
  ins_tsi_cat_id int8 NOT NULL,
  pol_type_id int8 NOT NULL,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  default_tsi_flag varchar(1),
  manual_tsi_flag varchar(1),
  manual_tsi_lock_flag varchar(1),
  CONSTRAINT ins_tsicat_poltype_pk PRIMARY KEY (ins_tcpt_id)
)
WITHOUT OIDS;
ALTER TABLE ins_tsicat_poltype OWNER TO postgres;
*/

   public static String tableName = "ins_tsicat_poltype";

   public static String fieldMap[][] = {
      {"stInsuranceTSIPolTypeID","ins_tcpt_id*pk"},
      {"stInsuranceTSICategoryID","ins_tsi_cat_id"},
      {"stPolicyTypeID","pol_type_id"},
      {"stDefaultTSIFlag","default_tsi_flag"},
      {"stManualTSIFlag","manual_tsi_flag"},
      {"stManualTSILockFlag","manual_tsi_lock_flag"},
      {"stDefaultTSIFlag2","default_tsi_flag2"},
      {"stManualTSIFlag2","manual_tsi_flag2"},
      {"stManualTSILockFlag2","manual_tsi_lock_flag2"},
      {"stDescription","description*n"},
      {"stActiveFlag","active_flag"},
   };

   public static String comboFields[] = {"ins_tcpt_id","description"};

   private String stInsuranceTSIPolTypeID;
   private String stInsuranceTSICategoryID;
   private String stPolicyTypeID;
   private String stDefaultTSIFlag;
   private String stManualTSIFlag;
   private String stManualTSILockFlag;
   private String stDescription;
   private String stDefaultTSIFlag2;
   private String stManualTSIFlag2;
   private String stManualTSILockFlag2;
   private String stActiveFlag;

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public String getStInsuranceTSIPolTypeID() {
      return stInsuranceTSIPolTypeID;
   }

   public void setStInsuranceTSIPolTypeID(String stInsuranceTSIPolTypeID) {
      this.stInsuranceTSIPolTypeID = stInsuranceTSIPolTypeID;
   }

   public String getStInsuranceTSICategoryID() {
      return stInsuranceTSICategoryID;
   }

   public void setStInsuranceTSICategoryID(String stInsuranceTSICategoryID) {
      this.stInsuranceTSICategoryID = stInsuranceTSICategoryID;
   }

   public String getStPolicyTypeID() {
      return stPolicyTypeID;
   }

   public void setStPolicyTypeID(String stPolicyTypeID) {
      this.stPolicyTypeID = stPolicyTypeID;
   }

   public String getStDefaultTSIFlag() {
      return stDefaultTSIFlag;
   }

   public void setStDefaultTSIFlag(String stDefaultTSIFlag) {
      this.stDefaultTSIFlag = stDefaultTSIFlag;
   }

   public String getStManualTSIFlag() {
      return stManualTSIFlag;
   }

   public void setStManualTSIFlag(String stManualTSIFlag) {
      this.stManualTSIFlag = stManualTSIFlag;
   }

   public String getStManualTSILockFlag() {
      return stManualTSILockFlag;
   }

   public void setStManualTSILockFlag(String stManualTSILockFlag) {
      this.stManualTSILockFlag = stManualTSILockFlag;
   }

    public String getStDefaultTSIFlag2() {
        return stDefaultTSIFlag2;
    }

    public void setStDefaultTSIFlag2(String stDefaultTSIFlag2) {
        this.stDefaultTSIFlag2 = stDefaultTSIFlag2;
    }

    public String getStManualTSIFlag2() {
        return stManualTSIFlag2;
    }

    public void setStManualTSIFlag2(String stManualTSIFlag2) {
        this.stManualTSIFlag2 = stManualTSIFlag2;
    }

    public String getStManualTSILockFlag2() {
        return stManualTSILockFlag2;
    }

    public void setStManualTSILockFlag2(String stManualTSILockFlag2) {
        this.stManualTSILockFlag2 = stManualTSILockFlag2;
    }

    /**
     * @return the stActiveFlag
     */
    public String getStActiveFlag() {
        return stActiveFlag;
    }

    /**
     * @param stActiveFlag the stActiveFlag to set
     */
    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }
}
