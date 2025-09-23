/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyCoinsView
 * Author:  Doni
 * Created: Jan 19, 2024 2:09:31 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

import java.math.BigDecimal;

public class InsurancePolicyTitipanPremiView extends DTO implements RecordAudit {

   /*
   CREATE TABLE ins_pol_titipan_premi
(
  ins_pol_titipan_premi_id bigint NOT NULL,
  policy_id bigint,
  create_date timestamp without time zone NOT NULL,
  create_who character varying(32) NOT NULL,
  change_date timestamp without time zone,
  change_who character varying(32),
  trx_no character varying(32),
  counter bigint,
  description character varying(255),
  realisasi_amount numeric,
  CONSTRAINT ins_pol_titipan_premi_pkey PRIMARY KEY (ins_pol_titipan_premi_id)
)
   */

   public static String tableName = "ins_pol_titipan_premi";

   public static String fieldMap[][] = {
      {"stInsurancePolicyTitipanPremiID","ins_pol_titipan_premi_id*pk"},
      {"stPolicyID","policy_id"},
      {"stTransactionID","trx_id"},
      {"stTransactionNo","trx_no"},
      {"stCounter","counter"},
      {"stDescription","description"},
      {"dbRealisasiAmount","realisasi_amount"},
   };

   /*
ALTER TABLE ins_pol_coins ADD COLUMN claim_amt numeric;

ALTER TABLE ins_pol_coins ADD COLUMN hfee_rate numeric;
ALTER TABLE ins_pol_coins ADD COLUMN hfee_amount numeric;
   */

   private String stInsurancePolicyTitipanPremiID;
   private String stPolicyID;
   private String stTransactionID;
   private String stTransactionNo;
   private String stCounter;
   private String stDescription;
   private BigDecimal dbRealisasiAmount;

    public String getStTransactionID() {
        return stTransactionID;
    }

    public void setStTransactionID(String stTransactionID) {
        this.stTransactionID = stTransactionID;
    }

    public BigDecimal getDbRealisasiAmount() {
        return dbRealisasiAmount;
    }

    public void setDbRealisasiAmount(BigDecimal dbRealisasiAmount) {
        this.dbRealisasiAmount = dbRealisasiAmount;
    }

    public String getStCounter() {
        return stCounter;
    }

    public void setStCounter(String stCounter) {
        this.stCounter = stCounter;
    }

    public String getStDescription() {
        return stDescription;
    }

    public void setStDescription(String stDescription) {
        this.stDescription = stDescription;
    }

    public String getStInsurancePolicyTitipanPremiID() {
        return stInsurancePolicyTitipanPremiID;
    }

    public void setStInsurancePolicyTitipanPremiID(String stInsurancePolicyTitipanPremiID) {
        this.stInsurancePolicyTitipanPremiID = stInsurancePolicyTitipanPremiID;
    }

    public String getStPolicyID() {
        return stPolicyID;
    }

    public void setStPolicyID(String stPolicyID) {
        this.stPolicyID = stPolicyID;
    }

    public String getStTransactionNo() {
        return stTransactionNo;
    }

    public void setStTransactionNo(String stTransactionNo) {
        this.stTransactionNo = stTransactionNo;
    }

}
