/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyCoverView
 * Author:  Denny Mahendra
 * Created: Jan 19, 2006 1:41:22 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;
import com.crux.util.Tools;
import com.crux.util.BDUtil;
import com.webfin.FinCodec;

import java.math.BigDecimal;

public class InsurancePolicyZoneView extends DTO implements RecordAudit {
   /*

CREATE TABLE ins_pol_zone
(
  ins_pol_zone_id bigint NOT NULL,
  zone_id character varying(15),
  description character varying(128),
  amount numeric
)
WITH OIDS;
ALTER TABLE ins_pol_zone OWNER TO postgres;
   */

   public static String tableName = "ins_pol_zone";

   public static String fieldMap[][] = {
      {"stInsurancePolicyZoneID", "ins_pol_zone_id*pk"},
      {"stZoneID", "zone_id"},
      {"stDescription", "description"},
      {"dbAmount", "amount"},
   };

   /*default_cover_flag*/
   
   private String stInsurancePolicyZoneID;
   private String stZoneID;
   private String stDescription;
   private BigDecimal dbAmount;

   public String getStInsurancePolicyZoneID() {
      return stInsurancePolicyZoneID;
   }

   public void setStInsurancePolicyZoneID(String stInsurancePolicyZoneID) {
      this.stInsurancePolicyZoneID = stInsurancePolicyZoneID;
   }
   
   public String getStZoneID() {
      return stZoneID;
   }

   public void setStZoneID(String stZoneID) {
      this.stZoneID = stZoneID;
   }
   
   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }
   
   public BigDecimal getDbAmount() {
      return dbAmount;
   }

   public void setDbAmount(BigDecimal dbAmount) {
      this.dbAmount = dbAmount;
   }

}
