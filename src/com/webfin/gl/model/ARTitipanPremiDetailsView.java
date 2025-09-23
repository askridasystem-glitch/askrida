/***********************************************************************
 * Module:  com.webfin.gl.model.JournalView
 * Author:  Denny Mahendra
 * Created: Jul 24, 2005 6:29:56 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.BDUtil;
import com.crux.util.ObjectCloner;
import com.crux.util.Tools;
import com.crux.pool.DTOPool;

import java.math.BigDecimal;
import java.util.Date;

public class ARTitipanPremiDetailsView extends DTO implements RecordAudit {
   /*
CREATE TABLE ar_titipan_details
(
  ar_titipan_details_id bigint,
  ar_titipan_id bigint,
  account_no character varying(32) NOT NULL,
  description character varying(255),
  create_date timestamp without time zone,
  create_who character varying(20),
  change_date timestamp without time zone,
  change_who character varying(20),
  debit numeric,
  credit numeric
)
WITH OIDS;
ALTER TABLE ar_titipan_details OWNER TO postgres;
   */

   public static String tableName = "ar_titipan_details";
   
   public static String fieldMap[][] = {
   	  {"stARTitipanDetailsID","ar_titipan_details_id*pk"},
      {"stARTitipanID","ar_titipan_id"},
      {"stAccountNo","account_no"},
      {"stDescription","description"},
      {"dbDebit","debit"},
      {"dbCredit","credit"},
      //{"dtApplyDate","applydate"},
      
   };

   /*
ALTER TABLE gl_je_detail ADD COLUMN ref_trx_type varchar(8);
ALTER TABLE gl_je_detail ADD COLUMN ref_trx_no varchar(32);

   */
   private String stARTitipanDetailsID;
   private String stARTitipanID;
   private String stTransactionNo;
   private Long lgAccountID;
   private String stAccountNo;
   private String stDescription;
   private Date dtApplyDate;
   private String stTransactionID;
   private String stCurrencyCode;
   private BigDecimal dbCurrencyRate;
   private String stTransactionHeaderID;
   private BigDecimal dbDebit;
   private BigDecimal dbCredit;

   public String getStARTitipanDetailsID() {
      return stARTitipanDetailsID;
   }

   public void setStARTitipanDetailsID(String stARTitipanDetailsID) {
      this.stARTitipanDetailsID = stARTitipanDetailsID;
   }

   public String getStARTitipanID() {
      return stARTitipanID;
   }

   public void setStARTitipanID(String stARTitipanID) {
      this.stARTitipanID = stARTitipanID;
   }
   
   public String getStTransactionNo() {
      return stTransactionNo;
   }

   public void setStTransactionNo(String stTransactionNo) {
      this.stTransactionNo = stTransactionNo;
   }

   public BigDecimal getDbDebit() {
      if (dbDebit==null) dbDebit = new BigDecimal(0);
      return dbDebit;
   }

   public void setDbDebit(BigDecimal dbDebit) {
      this.dbDebit = dbDebit;
   }

   public BigDecimal getDbCredit() {
      if (dbCredit==null) dbCredit = new BigDecimal(0);
      return dbCredit;
   }

   public void setDbCredit(BigDecimal dbCredit) {
      this.dbCredit = dbCredit;
   }

   public Long getLgAccountID() {
      return lgAccountID;
   }

   public void setLgAccountID(Long lgAccountID) {
      this.lgAccountID = lgAccountID;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public Date getDtApplyDate() {
      return dtApplyDate;
   }

   public void setDtApplyDate(Date dtApplyDate) {
      this.dtApplyDate = dtApplyDate;
   }
   
   public String getStAccountNo() {
      return stAccountNo;
   }

   public void setStAccountNo(String stAccountNo) {
      this.stAccountNo = stAccountNo;
   }
   
   public String getStTransactionHeaderID() {
      return stTransactionHeaderID;
   }

   public void setStTransactionHeaderID(String stTransactionHeaderID) {
      this.stTransactionHeaderID = stTransactionHeaderID;
   }

   public String getStTransactionID() {
      return stTransactionID;
   }

   public void setStTransactionID(String stTransactionID) {
      this.stTransactionID = stTransactionID;
   }
   
   public String getStCurrencyCode() {
      return stCurrencyCode;
   }

   public void setStCurrencyCode(String stCurrencyCode) {
      this.stCurrencyCode = stCurrencyCode;
   }

   public BigDecimal getDbCurrencyRate() {
      return dbCurrencyRate;
   }

   public void setDbCurrencyRate(BigDecimal dbCurrencyRate) {
      this.dbCurrencyRate = dbCurrencyRate;
   }
   
   //lama
   
   
   public JournalView copy() {
      return (JournalView) ObjectCloner.deepCopy(this);
   }

   public String toString() {
      return "["+getStMarks()+">"+lgAccountID+":"+BDUtil.sub(dbCredit,dbDebit)+"]\n";
   }

   public void loadAccountNo() {
      AccountView acc = getAccount();

      if (acc!=null)
         setStAccountNo(acc.getStAccountNo());
   }

   public AccountView getAccount() {
      if (lgAccountID==null) return null;
      return (AccountView) DTOPool.getInstance().getDTORO(AccountView.class, String.valueOf(getLgAccountID()));
   }

   /*
   public void setStAccountIDNotNull(String stAccountID, String errMsg) {
      if (stAccountID==null) throw new RuntimeException(errMsg);
      setStAccountID(stAccountID);
      loadAccountNo();
      if (stAccountNo==null) throw new RuntimeException(errMsg);
   }*/
   
}