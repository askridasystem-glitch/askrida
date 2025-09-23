/***********************************************************************
 * Module:  com.webfin.gl.model.GLInfoView
 * Author:  Denny Mahendra
 * Created: Jun 3, 2007 9:34:43 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.common.model.DTO;

import java.math.BigDecimal;
import java.util.Date;

public class GLInfoView extends DTO {
   public static String fieldMap[][] = {
      {"stAccountNo","account_no"},
      {"stAccountDesc","account_name"},
      {"dbAmount","bx"},
      {"dtTrxDate","trx_date"},
   };

   private String stAccountNo;
   private String stAccountDesc;
   private BigDecimal dbAmount;
   private Date dtTrxDate;

   public Date getDtTrxDate() {
      return dtTrxDate;
   }

   public void setDtTrxDate(Date dtTrxDate) {
      this.dtTrxDate = dtTrxDate;
   }

   public String getStAccountDesc() {
      return stAccountDesc;
   }

   public void setStAccountDesc(String stAccountDesc) {
      this.stAccountDesc = stAccountDesc;
   }

   public String getStAccountNo() {
      return stAccountNo;
   }

   public void setStAccountNo(String stAccountNo) {
      this.stAccountNo = stAccountNo;
   }

   public BigDecimal getDbAmount() {
      return dbAmount;
   }

   public void setDbAmount(BigDecimal dbAmount) {
      this.dbAmount = dbAmount;
   }
}
