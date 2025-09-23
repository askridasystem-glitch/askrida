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

public class InsuranceZoneLimitView extends DTO implements RecordAudit {
   /*

CREATE TABLE ins_zone_limit
CREATE TABLE ins_zone_limit
(
  zone_id character varying(20),
  description character varying(128),
  limit1 numeric
)
WITH OIDS;
ALTER TABLE ins_zone_limit OWNER TO postgres;
   */

   public static String tableName = "ins_zone_limit";

   public static String fieldMap[][] = {
      {"stZoneID", "zone_id*pk"},
      {"stZoneCode", "zone_code"},
      {"stDescription", "description"},
      {"dbLimit1", "limit1"},
   };
   
   private String stZoneID;
   private String stDescription;
   private BigDecimal dbLimit1;
   private String stZoneCode;
   
   public String getStZoneID() {
      return stZoneID;
   }

   public void setStZoneID(String stZoneID) {
      this.stZoneID = stZoneID;
   }
   
   public String getStZoneCode() {
      return stZoneCode;
   }

   public void setStZoneCode(String stZoneCode) {
      this.stZoneCode = stZoneCode;
   }
   
   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }
   
   public BigDecimal getDbLimit1() {
      return dbLimit1;
   }

   public void setDbLimit1(BigDecimal dbLimit1) {
      this.dbLimit1 = dbLimit1;
   }

}
