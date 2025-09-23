/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceCoinsScaleView
 * Author:  Achmad Rhodoni
 * Created: Aug 3, 2009 5:30:36 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.webfin.FinCodec;
import java.math.BigDecimal;
import java.util.Date;

public class InsuranceCoinsScaleView extends DTO implements RecordAudit {
   /*
-- Table: ins_co_scale

-- DROP TABLE ins_co_scale;

CREATE TABLE ins_co_scale
(
  ins_co_scale_id bigint NOT NULL,
  ins_co_scale_desc character varying(255),
  scale_lower numeric,
  scale_upper numeric,
  scale_period_start timestamp without time zone,
  scale_period_end timestamp without time zone,
  session_pct numeric,
  pol_type_id bigint,
  CONSTRAINT ins_co_scale_pkey PRIMARY KEY (ins_co_scale_id)
)
WITHOUT OIDS;
ALTER TABLE ins_co_scale OWNER TO postgres;
   */

   private String stInsuranceCoverPolTypeID;
   private String stInsuranceCoverID;
   private String stPolicyTypeID;
   private String stCoverCategory;
   private String stPolicySubTypeID;
   private String stDescription;

   public static String tableName = "ins_co_scale";

   public static String fieldMap[][] = {
      {"stInsuranceCoinsScaleID","ins_co_scale_id*pk"},
      {"stInsuranceCoinsScaleDesc","ins_co_scale_desc"},
      {"dbScaleLower","scale_lower"},
      {"dbScaleUpper","scale_upper"},
      {"dtScalePeriodStart","scale_period_start"},
      {"dtScalePeriodEnd","scale_period_end"},
      {"dbSessionPct","session_pct"},
      {"stInsurancePolTypeID","pol_type_id"},
   };

   private String stInsuranceCoinsScaleID;
   private String stInsuranceCoinsScaleDesc;
   private BigDecimal dbScaleLower;
   private BigDecimal dbScaleUpper;
   private Date dtScalePeriodStart;
   private Date dtScalePeriodEnd;
   private BigDecimal dbSessionPct;
   private String stInsurancePolTypeID;

   //public static String comboFields[] = {"ins_cvpt_id","description"};

   public String getStInsuranceCoinsScaleID() {
      return stInsuranceCoinsScaleID;
   }

   public void setStInsuranceCoinsScaleID(String stInsuranceCoinsScaleID) {
      this.stInsuranceCoinsScaleID = stInsuranceCoinsScaleID;
   }
   
   public String getStInsuranceCoinsScaleDesc() {
      return stInsuranceCoinsScaleDesc;
   }

   public void setStInsuranceCoinsScaleDesc(String stInsuranceCoinsScaleDesc) {
      this.stInsuranceCoinsScaleDesc = stInsuranceCoinsScaleDesc;
   }
   
   public String getStInsurancePolTypeID() {
      return stInsurancePolTypeID;
   }

   public void setStInsurancePolTypeID(String stInsurancePolTypeID) {
      this.stInsurancePolTypeID = stInsurancePolTypeID;
   }
   
   public BigDecimal getDbScaleLower() {
      return dbScaleLower;
   }

   public void setDbScaleLower(BigDecimal dbScaleLower) {
      this.dbScaleLower = dbScaleLower;
   }
   
   public BigDecimal getDbScaleUpper() {
      return dbScaleUpper;
   }

   public void setDbScaleUpper(BigDecimal dbScaleUpper) {
      this.dbScaleUpper = dbScaleUpper;
   }
   
   
   public BigDecimal getDbSessionPct() {
      return dbSessionPct;
   }

   public void setDbSessionPct(BigDecimal dbSessionPct) {
      this.dbSessionPct = dbSessionPct;
   }


}
