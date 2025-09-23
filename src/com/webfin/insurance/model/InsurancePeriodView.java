/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePeriodView
 * Author:  Denny Mahendra
 * Created: Apr 23, 2006 1:28:48 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;
import com.crux.util.BDUtil;
import com.webfin.FinCodec;

import java.util.Date;
import java.util.Calendar;
import java.math.BigDecimal;

public class InsurancePeriodView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ins_period
(
  ins_period_id int8 NOT NULL,
  description  varchar(255),
  period_length int8,
  period_unit  varchar(16),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_period_pk PRIMARY KEY (ins_period_id)
) without oids;
   */

   public static String tableName = "ins_period";

   public static String fieldMap[][] = {
      {"stInsurancePeriodID","ins_period_id*pk"},
      {"stDescription","description"},
      {"lgPeriodLength","period_length"},
      {"stPeriodUnit","period_unit"},
      {"stOrderSeq","order_seq"},
   };

   private String stOrderSeq;
   private String stInsurancePeriodID;
   private String stDescription;
   private Long lgPeriodLength;
   private String stPeriodUnit;

   public String getStOrderSeq() {
      return stOrderSeq;
   }

   public void setStOrderSeq(String stOrderSeq) {
      this.stOrderSeq = stOrderSeq;
   }

   public String getStInsurancePeriodID() {
      return stInsurancePeriodID;
   }

   public void setStInsurancePeriodID(String stInsurancePeriodID) {
      this.stInsurancePeriodID = stInsurancePeriodID;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public Long getLgPeriodLength() {
      return lgPeriodLength;
   }

   public void setLgPeriodLength(Long lgPeriodLength) {
      this.lgPeriodLength = lgPeriodLength;
   }

   public String getStPeriodUnit() {
      return stPeriodUnit;
   }

   public void setStPeriodUnit(String stPeriodUnit) {
      this.stPeriodUnit = stPeriodUnit;
   }

   public Date advance(Date perDate) {

      final int perlen = lgPeriodLength.intValue();

      final Calendar cld = Calendar.getInstance();

      cld.setTime(perDate);

      if (isDays()) cld.add(Calendar.DATE, perlen);
      else if (isMonths()) cld.add(Calendar.MONTH, perlen);
      else if (isYear()) cld.add(Calendar.YEAR, perlen);

      return cld.getTime();

   }

   public boolean isDays() {return FinCodec.PeriodUnit.DAY.equalsIgnoreCase(stPeriodUnit);}
   public boolean isMonths() {return FinCodec.PeriodUnit.MONTH.equalsIgnoreCase(stPeriodUnit);}
   public boolean isYear() {return FinCodec.PeriodUnit.YEAR.equalsIgnoreCase(stPeriodUnit);}

   public BigDecimal getDbYearlyRate() {
      final int perlen = lgPeriodLength.intValue();

      if (isDays()) return new BigDecimal(perlen).divide(new BigDecimal(366), 2, BigDecimal.ROUND_HALF_DOWN);
      if (isMonths()) return new BigDecimal(perlen).divide(new BigDecimal(12), 2, BigDecimal.ROUND_HALF_DOWN);
      if (isYear()) return new BigDecimal(perlen);

      return null;
   }

   public BigDecimal getInstallmentAmount(int periodNo, String lgPeriodCount, BigDecimal amt) {

      final BigDecimal perlen = new BigDecimal(lgPeriodCount);

      final BigDecimal periodAmount = BDUtil.div(amt, perlen);

      if (periodNo==0) {
         final BigDecimal roundingErr = BDUtil.sub(amt, BDUtil.mul(periodAmount, perlen));
         return BDUtil.add(periodAmount, roundingErr);
      } else
         return periodAmount;
   }
}


