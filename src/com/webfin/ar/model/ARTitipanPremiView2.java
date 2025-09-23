/***********************************************************************
 * Module:  com.webfin.ar.model.ARInvoiceView
 * Author:  Denny Mahendra
 * Created: Oct 11, 2005 10:05:46 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.*;
import com.crux.pool.DTOPool;
import com.crux.lang.LanguageManager;
import com.webfin.entity.model.EntityView;
import com.webfin.gl.model.AccountView;
import com.webfin.gl.util.GLUtil;
import com.webfin.FinCodec;
import com.webfin.insurance.model.*;

import java.math.BigDecimal;
import java.util.Date;

public class ARTitipanPremiView2 extends DTO implements RecordAudit {

   public static boolean exludeComission = false;

   /*
CREATE TABLE ar_titipan_premi
(
  ar_titipan_premi_id bigint NOT NULL,
  trx_no character varying(130),
  account_id bigint,
  account_no character varying(32),
  description character varying(128),
  cc_code character varying(8),
  ar_titipan_premi_date timestamp without time zone,
  premi_total numeric,
  create_date timestamp without time zone,
  create_who character varying(32),
  change_date timestamp without time zone,
  change_who character varying(32),
  CONSTRAINT ar_titipan_premi_pk PRIMARY KEY (ar_titipan_premi_id)
)
WITH OIDS;
ALTER TABLE ar_titipan_premi OWNER TO postgres;




)
   */

   public static String tableName = "ar_titipan_premi";

   public static String fieldMap[][] = {
      {"stARTitipanPremiID","ar_titipan_premi_id*pk"},
      {"stTransactionNo","trx_no"},
      {"stAccountID","account_id"},
      {"stAccountNo","account_no"},
      {"stDescription","description"},
      {"stCostCenterCode","cc_code"},
      {"dtARTitipanPremi","ar_titipan_premi_date"},
      {"dbPremiTotal","premi_total"},
   };

   public ARTitipanPremiView2() {
   }

   private String stCommitFlag="Y";
   private String stApprovedFlag;
   private String stPolicyID;

   private String stPremiFlag;
   
   private DTOList details;
   private DTOList list;
   private DTOList list2;
   
   public DTOList objects;
   private Class clObjectClass;
   
   	private String stARTitipanPremiID;
	private String stTransactionNo;
	private String stAccountID;
	private String stAccountNo;
	private String stDescription;
	private String stCostCenterCode;
	
	private Date dtARTitipanPremi;
	
	private BigDecimal dbPremiTotal;
   
   public String getStARTitipanPremiID() {
      return stARTitipanPremiID;
   }

   public void setStARTitipanPremiID(String stARTitipanPremiID) {
      this.stARTitipanPremiID = stARTitipanPremiID;
   }
   
   public String getStTransactionNo() {
      return stTransactionNo;
   }

   public void setStTransactionNo(String stTransactionNo) {
      this.stTransactionNo = stTransactionNo;
   }
   
   public String getStAccountID() {
      return stAccountID;
   }

   public void setStAccountID(String stAccountID) {
      this.stAccountID = stAccountID;
   }
   
   public String getStAccountNo() {
      return stAccountNo;
   }

   public void setStAccountNo(String stAccountNo) {
      this.stAccountNo = stAccountNo;
   }
   
   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }
   
   public String getStCostCenterCode() {
      return stCostCenterCode;
   }

   public void setStCostCenterCode(String stCostCenterCode) {
      this.stCostCenterCode = stCostCenterCode;
   }
   
   public Date getDtARTitipanPremi() {
      return dtARTitipanPremi;
   }

   public void setDtARTitipanPremi(Date dtARTitipanPremi) {
      this.dtARTitipanPremi = dtARTitipanPremi;
   }
   
   public BigDecimal getDbPremiTotal() {
      return dbPremiTotal;
   }

   public void setDbPremiTotal(BigDecimal dbPremiTotal) {
      this.dbPremiTotal = dbPremiTotal;
   }


   public DTOList getDetails() {
      if (details==null) {
         if (!isNew()) {
            try {
               details = ListUtil.getDTOListFromQuery(
                       "select * from ar_titipan_premi_details where ar_titipan_premi_id = ? order by ar_titipan_premi_det_id",
                       new Object[] {stARTitipanPremiID},
                       ARTitipanPremiView2.class
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
   
   public DTOList getList() {
      if (list==null) {
         if (!isNew()) {
            try {
               list = ListUtil.getDTOListFromQuery(
                       "select * from ar_titipan_premi where ar_titipan_premi_id = ?",
                       new Object[] {stARTitipanPremiID},
                       ARTitipanPremiView2.class
               );
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }
      return list;
   }

   public void setList(DTOList list) {
      this.list = list;
   }

  
/*
   public void validate() {

      if (stGLARAccountID==null && stARTransactionTypeID==null) throw new RuntimeException("AR/AP TRX not defined");

      if (stCostCenterCode==null) throw new RuntimeException("You must supply cost center for invoice !");

      if (stCurrencyCode==null) throw new RuntimeException("You must supply currency code for invoice !");

      if (dbCurrencyRate==null) throw new RuntimeException("You must supply currency rate for invoice !");

      for (int i = 0; i < details.size(); i++) {
         ARInvoiceDetailView det = (ARInvoiceDetailView) details.get(i);

         if (det.isComission()) {
            if (det.getStEntityID()==null) throw new RuntimeException("Comission should be assigned to an agent entity");
         }
      }
   }*/
   
   public void recalculate(){
 		if(details!=null){
	 		BigDecimal totalPremi = null;
	   		for(int i = 0; i< details.size(); i++){
	   			ARTitipanPremiDetailView detilTitipan = (ARTitipanPremiDetailView) details.get(i);
	   			
	   			totalPremi = BDUtil.add(totalPremi,detilTitipan.getDbPremiAmount());
	   		}
	   		
	   		setDbPremiTotal(totalPremi);
 	  }
   }

   private void negateDetails() {

      for (int i = 0; i < details.size(); i++) {
         ARInvoiceDetailView det = (ARInvoiceDetailView) details.get(i);

         det.setDbEnteredAmount(BDUtil.negate(det.getDbEnteredAmount()));
      }
   }
   
   public void generateTrxNo() throws Exception {

      if (stTransactionNo!=null) return;

      final String ccCode = Tools.getDigitRightJustified(getStCostCenterCode(),2);
      //final String methodCode = Tools.getDigitRightJustified(getReceiptClass().getStMethodCode(),1);
      String stBankCode = "00000";
      final String bankCode = Tools.getDigitRightJustified(stBankCode,5);
	  //final String bankCode = "";
      String counterKey = "";
              //DateUtil.getMonth2Digit(getDtARTitipanPremi())+
              //DateUtil.getYear2Digit(getDtARTitipanPremi());

      String rn = String.valueOf(IDFactory.createNumericID("RCPNO"+counterKey));

      rn = Tools.getDigitRightJustified(rn,3);
      
      //122100100300 10
      //012345678901234
      
      //A 01 10 1010 00000 00 001
      stTransactionNo =
              "H" +
              counterKey +
              ccCode +
              ccCode +
              bankCode +
              "00"+
              rn;
   }
  
}
