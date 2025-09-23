/***********************************************************************
 * Module:  com.webfin.gl.model.GLCurrencyHistoryView
 * Author:  Denny Mahendra
 * Created: Oct 31, 2005 10:32:39 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

import java.math.BigDecimal;
import java.util.Date;

public class GLCurrencyHistoryView extends DTO implements RecordAudit {
   /*

   CREATE TABLE gl_ccy_history
(
  ccy_hist_id int8 NOT NULL,
  ccy_code  varchar(3),
  ccy_date timestamp,
  ccy_rate numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT gl_ccy_history_pk PRIMARY KEY (ccy_hist_id)
);
   */

   public static String tableName = "gl_ccy_history";

   public static String fieldMap[][] = {
      {"stCurrencyHistID","ccy_hist_id*pk"},
      {"stCurrencyCode","ccy_code"},
      {"stCurrencyMaster","ccy_master"},
      {"dtCurrencyDate","ccy_date"},
      {"dbRate","ccy_rate"},
      {"dbRateTreaty","ccy_rate_treaty"},
      {"stCurrencyDesc","ccy_desc"},
      {"stActiveFlag","active_flag"},
      {"dtPeriodStart","period_start"},
      {"dtPeriodEnd","period_end"},
   };


   private String stCurrencyHistID;
   private String stCurrencyCode;
   private String stCurrencyMaster;
   private Date dtCurrencyDate;
   private BigDecimal dbRate;
   private BigDecimal dbRateTreaty;
   private String stCurrencyDesc;
   private String stActiveFlag;
   private Date dtPeriodStart;
   private Date dtPeriodEnd;

   public String getStCurrencyHistID() {
      return stCurrencyHistID;
   }

   public void setStCurrencyHistID(String stCurrencyHistID) {
      this.stCurrencyHistID = stCurrencyHistID;
   }

   public String getStCurrencyCode() {
      return stCurrencyCode;
   }

   public void setStCurrencyCode(String stCurrencyCode) {
      this.stCurrencyCode = stCurrencyCode;
   }

   public Date getDtCurrencyDate() {
      return dtCurrencyDate;
   }

   public void setDtCurrencyDate(Date dtCurrencyDate) {
      this.dtCurrencyDate = dtCurrencyDate;
   }

   public BigDecimal getDbRate() {
      return dbRate;
   }

   public void setDbRate(BigDecimal dbRate) {
      this.dbRate = dbRate;
   }

   public String getStCurrencyMaster() {
      return stCurrencyMaster;
   }

   public void setStCurrencyMaster(String stCurrencyMaster) {
      this.stCurrencyMaster = stCurrencyMaster;
   }
   
   public BigDecimal getDbRateTreaty() {
      return dbRateTreaty;
   }

   public void setDbRateTreaty(BigDecimal dbRateTreaty) {
      this.dbRateTreaty = dbRateTreaty;
   }

    public String getStCurrencyDesc() {
        return stCurrencyDesc;
    }

    public void setStCurrencyDesc(String stCurrencyDesc) {
        this.stCurrencyDesc = stCurrencyDesc;
    }

    public String getStActiveFlag() {
        return stActiveFlag;
    }

    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }

    /**
     * @return the dtPeriodStart
     */
    public Date getDtPeriodStart() {
        return dtPeriodStart;
    }

    /**
     * @param dtPeriodStart the dtPeriodStart to set
     */
    public void setDtPeriodStart(Date dtPeriodStart) {
        this.dtPeriodStart = dtPeriodStart;
    }

    /**
     * @return the dtPeriodEnd
     */
    public Date getDtPeriodEnd() {
        return dtPeriodEnd;
    }

    /**
     * @param dtPeriodEnd the dtPeriodEnd to set
     */
    public void setDtPeriodEnd(Date dtPeriodEnd) {
        this.dtPeriodEnd = dtPeriodEnd;
    }
}
