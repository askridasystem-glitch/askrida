/***********************************************************************
 * Module:  com.webfin.gl.model.GLBalanceView
 * Author:  Denny Mahendra
 * Created: Oct 7, 2005 10:35:12 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.common.codedecode.Codec;

import java.math.BigDecimal;

public class GLBalanceView extends DTO implements RecordAudit {
   /*
CREATE TABLE gl_acct_bal
(
  account_id int8 NOT NULL,
  period_year int8 NOT NULL,
  period_no int4 NOT NULL,
  bal numeric,
  CONSTRAINT gl_acct_bal_pk PRIMARY KEY (account_id, period_year, period_no)
) 
   */

   private Long lgAccountID;
   private String stAccountNo;
   private String stDescription;
   private Long lgPeriondNo;
   private Long lgPeriodYear;
   private BigDecimal dbBalance;
   private BigDecimal dbEffectiveBalance;
   private BigDecimal [] debit;
   private BigDecimal [] credit;
   private BigDecimal [] balance;

   public BigDecimal[] getDebit() {
      return debit;
   }

   public BigDecimal[] getCredit() {
      return credit;
   }

   public BigDecimal[] getBalance() {
      return balance;
   }

   public Object setFieldValueByFieldName(String stFieldName, Object value) {
      if (stFieldName.indexOf("bal")==0) {
         final int idx = Integer.parseInt(stFieldName.substring(3,stFieldName.length()));
         balance=setAr(balance,idx,value);
      }

      else if (stFieldName.indexOf("db")==0) {
         final int idx = Integer.parseInt(stFieldName.substring(2,stFieldName.length()));
         debit=setAr(debit,idx,value);
      }

      else if (stFieldName.indexOf("cr")==0) {
         final int idx = Integer.parseInt(stFieldName.substring(2,stFieldName.length()));
         credit=setAr(credit,idx,value);
      }
      else
         return super.setFieldValueByFieldName(stFieldName, value);    //To change body of overridden methods use File | Settings | File Templates.

      return value;
   }

   private BigDecimal[] setAr(BigDecimal[] balance, int idx, Object value) {

      if (balance==null) balance = new BigDecimal[14];

      if (value==null);
      else if (!(value instanceof BigDecimal)) value = new BigDecimal(value.toString());

      balance[idx] = (BigDecimal)value;

      return balance;
   }

   public BigDecimal getDbEffectiveBalance() {
      return dbEffectiveBalance;
   }

   public void setDbEffectiveBalance(BigDecimal dbEffectiveBalance) {
      this.dbEffectiveBalance = dbEffectiveBalance;
   }

   public static String tableName = "gl_acct_bal";
   //MARK BUAT BERSIH2X 
   //public static String tableName = "gl_acct_bal_temp";

   public static String fieldMap[][] = {
      {"lgAccountID","account_id*pk"},
      {"lgPeriondNo","period_no*pk"},
      {"lgPeriodYear","period_year*pk"},
      {"dbBalance","bal"},
      {"stAccountNo","accountno*n"},
      {"stDescription","description*n"},
   };

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public Long getLgAccountID() {
      return lgAccountID;
   }

   public void setLgAccountID(Long lgAccountID) {
      this.lgAccountID = lgAccountID;
   }

   public Long getLgPeriondNo() {
      return lgPeriondNo;
   }

   public void setLgPeriondNo(Long lgPeriondNo) {
      this.lgPeriondNo = lgPeriondNo;
   }

   public BigDecimal getDbBalance() {
      return dbBalance;
   }

   public void setDbBalance(BigDecimal dbBalance) {
      this.dbBalance = dbBalance;
   }

   public void setStAccountNo(String stAccountNo) {
      this.stAccountNo = stAccountNo;
   }

   public String getStAccountNo() {
      return stAccountNo;
   }

   public void setLgPeriodYear(Long lgPeriodYear) {
      this.lgPeriodYear = lgPeriodYear;
   }

   public Long getLgPeriodYear() {
      return lgPeriodYear;
   }
}
