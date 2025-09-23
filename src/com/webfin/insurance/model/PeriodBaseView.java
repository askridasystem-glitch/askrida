/***********************************************************************
 * Module:  com.webfin.insurance.model.PeriodBaseView
 * Author:  Denny Mahendra
 * Created: May 20, 2006 10:00:07 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

import java.math.BigDecimal;

public class PeriodBaseView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ins_period_base
(
  ins_period_base_id int8 NOT NULL,
  base_unit numeric,
  description varchar(128),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_period_base_pk PRIMARY KEY (ins_period_base_id)
)
   */

   public static String tableName = "ins_period_base";

   public static String fieldMap[][] = {
      {"stPeriodBaseID","ins_period_base_id*pk"},
      {"dbBaseUnit","base_unit"},
      {"stDescription","description"},
   };

   private String stPeriodBaseID;
   private BigDecimal dbBaseUnit;
   private String stDescription;

   public String getStPeriodBaseID() {
      return stPeriodBaseID;
   }

   public void setStPeriodBaseID(String stPeriodBaseID) {
      this.stPeriodBaseID = stPeriodBaseID;
   }

   public BigDecimal getDbBaseUnit() {
      return dbBaseUnit;
   }

   public void setDbBaseUnit(BigDecimal dbBaseUnit) {
      this.dbBaseUnit = dbBaseUnit;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }
}
