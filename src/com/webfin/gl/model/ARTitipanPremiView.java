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
import com.crux.util.DTOList;
import com.crux.util.ListUtil;

import java.math.BigDecimal;
import java.util.Date;

public class ARTitipanPremiView extends DTO implements RecordAudit {
   /*
CREATE TABLE ar_titipan
(
  ar_titipan_id bigint NOT NULL,
  trx_no character varying(20),
  accountid bigint,
  account_no character varying(32) NOT NULL,
  description character varying(255),
  apply_date timestamp without time zone,
  create_date timestamp without time zone,
  create_who character varying(20),
  change_date timestamp without time zone,
  change_who character varying(20),
  trx_id bigint,
  ccy_code character varying(3),  
  ccy_rate numeric,
  trx_hdr_id bigint,
  debit numeric,
  credit numeric,
  CONSTRAINT ar_titipan_pkey PRIMARY KEY (ar_titipan_id)
)
WITH OIDS;
ALTER TABLE ar_titipan OWNER TO postgres;
)
   */

   public static String tableName = "ar_titipan";
   
   public static String fieldMap[][] = {
      {"stARTitipanID","ar_titipan_id*pk"},
      {"stTransactionNo","trx_no"},
      {"lgAccountID","accountid"},
      {"stAccountNo","account_no"},
      {"stDescription","description"},
      {"dtApplyDate","apply_date"},
      {"stTransactionID","trx_id"},
      {"stCurrencyCode","ccy_code"},
      {"dbCurrencyRate","ccy_rate"},
      {"stTransactionHeaderID","trx_hdr_id"},
      {"dbBalance","balance"},
      {"stCostCenterCode","cc_code"},
   };

   /*
ALTER TABLE gl_je_detail ADD COLUMN ref_trx_type varchar(8);
ALTER TABLE gl_je_detail ADD COLUMN ref_trx_no varchar(32);

   */
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
   private BigDecimal dbBalance;
   private DTOList details;
   private DTOList titipan;
   
   private String stCostCenterCode;
   
   public String getStCostCenterCode(){
   		return stCostCenterCode;
   }
   
   public void setStCostCenterCode(String stCostCenterCode){
   		this.stCostCenterCode = stCostCenterCode;
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

   public BigDecimal getDbBalance() {
      if (dbBalance==null) dbBalance = new BigDecimal(0);
      return dbBalance;
   }

   public void setDbBalance(BigDecimal dbBalance) {
      this.dbBalance = dbBalance;
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

   /*
   public String toString() {
      return "["+getStMarks()+">"+lgAccountID+":"+BDUtil.sub(dbCredit,dbDebit)+"]\n";
   }*/

   public void loadAccountNo() {
      AccountView acc = getAccount();

      if (acc!=null)
         setStAccountNo(acc.getStAccountNo());
   }

   public AccountView getAccount() {
      if (lgAccountID==null) return null;
      return (AccountView) DTOPool.getInstance().getDTORO(AccountView.class, String.valueOf(getLgAccountID()));
   }

   
   public void setStAccountIDNotNull(String stAccountID, String errMsg) {
      if (stAccountID==null) throw new RuntimeException(errMsg);
      setStAccountID(stAccountID);
      loadAccountNo();
      if (stAccountNo==null) throw new RuntimeException(errMsg);
   }
   
   public void setStAccountID(String ac) {
      lgAccountID = ac==null?null:new Long(ac);
   }
   
   public DTOList getDetails() {
      if (details==null) {
         if (!isNew()) {
            try {
               details = ListUtil.getDTOListFromQuery(
                       "select * from ar_titipan_details where ar_titipan_id = ?",
                       new Object[] {stARTitipanID},
                       ARTitipanPremiDetailsView.class
               );
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }
      return details;
   }

   public void setDetails(DTOList details) {
      this.details = details;
   }
   
   
   public DTOList getTitipan() {
      if (titipan==null) {
           try {
               details = ListUtil.getDTOListFromQuery(
                       "select * from ar_titipan where ar_titipan_id = ?",
                       new Object[] {stARTitipanID},
                       ARTitipanPremiView.class
               );
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
      }
      return titipan;
   }

   public void setTitipan(DTOList titipan) {
      this.titipan = titipan;
   }
   
}