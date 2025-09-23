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
import com.webfin.insurance.model.InsurancePolicyTypeView;
import com.webfin.gl.model.ARTitipanPremiDetailsView;

import java.math.BigDecimal;
import java.util.Date;

public class ARTitipanView extends DTO implements RecordAudit {

   public static boolean exludeComission = false;
   
    private final static transient LogManager logger = LogManager.getInstance(ARTitipanView.class);
  

   /*
   CREATE TABLE ar_invoice
(
  ar_invoice_id int8 NOT NULL,
  invoice_no varchar(64),
  amount numeric,
  invoice_date timestamp,
  due_date timestamp,
  amount_settled numeric,
  ccy varchar(3),
  ar_cust_id int8,
  posted_flag varchar(1),
  cancel_flag varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
ALTER TABLE ar_invoice ADD COLUMN ent_id int8;

  CONSTRAINT ar_invoice_pk PRIMARY KEY (ar_invoice_id)


ALTER TABLE ar_invoice ADD COLUMN attr_pol_no varchar(32);
ALTER TABLE ar_invoice ADD COLUMN mutation_date timestamp;
ALTER TABLE ar_invoice ADD COLUMN attr_pol_name varchar(255);
ALTER TABLE ar_invoice ADD COLUMN attr_pol_per_0 timestamp;
ALTER TABLE ar_invoice ADD COLUMN attr_pol_per_1 timestamp;
ALTER TABLE ar_invoice ADD COLUMN attr_pol_address varchar(255);
ALTER TABLE ar_invoice ADD COLUMN attr_pol_tsi numeric;
ALTER TABLE ar_invoice ADD COLUMN attr_pol_tsi_total numeric;
ALTER TABLE ar_invoice ADD COLUMN attr_pol_type_id int8;
ALTER TABLE ar_invoice ADD COLUMN attr_quartal varchar(32);
ALTER TABLE ar_invoice ADD COLUMN attr_underwrit varchar(32);
ALTER TABLE ar_invoice ADD COLUMN attr_pol_id int8;

ALTER TABLE ar_invoice ADD COLUMN commit_flag varchar(1);
ALTER TABLE ar_invoice ADD COLUMN approved_flag varchar(1);

ALTER TABLE ar_invoice ADD COLUMN refx0 varchar(8);
ALTER TABLE ar_invoice ADD COLUMN refx1 varchar(64);
ALTER TABLE ar_invoice ADD COLUMN refy0 varchar(8);
ALTER TABLE ar_invoice ADD COLUMN refy1 varchar(64);
ALTER TABLE ar_invoice ADD COLUMN refz0 varchar(8);
ALTER TABLE ar_invoice ADD COLUMN refz1 varchar(64);
ALTER TABLE ar_invoice ADD COLUMN refa0 varchar(8);
ALTER TABLE ar_invoice ADD COLUMN refa1 varchar(64);
ALTER TABLE ar_invoice ADD COLUMN refc0 varchar(8);
ALTER TABLE ar_invoice ADD COLUMN refc1 varchar(64);
ALTER TABLE ar_invoice ADD COLUMN refd0 varchar(8);
ALTER TABLE ar_invoice ADD COLUMN refd1 varchar(64);
ALTER TABLE ar_invoice ADD COLUMN refe0 varchar(8);
ALTER TABLE ar_invoice ADD COLUMN refe1 varchar(64);




)

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
  balance numeric,
  CONSTRAINT ar_titipan_pkey PRIMARY KEY (ar_titipan_id)
)
WITH OIDS;
ALTER TABLE ar_titipan OWNER TO postgres;
   */

   public static String tableName = "ar_titipan";

   public static String fieldMap[][] = {
      {"stARTitipanID","ar_titipan_id*pk"},
      {"stTransactionNo","trx_no"},
      {"stAccountID","accountid"},
      {"stAccountNo","account_no"},
      {"stDescription","description"},
      {"dtApplyDate","apply_date"},
      {"stTransactionID","trx_id"},
      {"stCurrencyCode","ccy_code"},
      {"dbCurrencyRate","ccy_rate"},
      {"stTransactionHeaderID","trx_hdr_id"},
      {"dbBalance","balance"},
      {"stCostCenterCode","cc_code"},
      {"stAccountIDVs","accountid_vs"},
      {"stAccountNoVs","account_no_vs"},
      {"stDescriptionVs","description_vs"},
   };

   public ARTitipanView() {
   }

/*
ALTER TABLE ar_invoice ADD COLUMN f_hide varchar(1);
ALTER TABLE ar_invoice ADD COLUMN f_postable varchar(1);

*/


   private DTOList details;
   private DTOList list;
   private DTOList list2;
   
   private String stARTitipanID;
   private String stTransactionNo;
   private String stAccountID;
   private String stAccountNo;
   private String stDescription;
   private Date dtApplyDate;
   private String stTransactionID;
   private String stCurrencyCode;
   private BigDecimal dbCurrencyRate;
   private String stTransactionHeaderID;
   private BigDecimal dbBalance;
   private String stCostCenterCode;
   private String stAccountIDVs;
   private String stAccountNoVs;
   private String stDescriptionVs;
   
   public String getStAccountIDVs(){
   		return stAccountIDVs;
   }
   
   public void setStAccountIDVs(String stAccountIDVs){
   		this.stAccountIDVs = stAccountIDVs;
   }
   
   public String getStAccountNoVs(){
   		return stAccountNoVs;
   }
   
   public void setStAccountNoVs(String stAccountNoVs){
   		this.stAccountNoVs = stAccountNoVs;
   }
   
   public String getStDescriptionVs(){
   		return stDescriptionVs;
   }
   
   public void setStDescriptionVs(String stDescriptionVs){
   		this.stDescriptionVs = stDescriptionVs;
   }
   
   public String getStCostCenterCode(){
   		return stCostCenterCode;
   }
   
   public void setStCostCenterCode(String stCostCenterCode){
   		this.stCostCenterCode = stCostCenterCode;
   }
   
   public String getStARTitipanID(){
   		return stARTitipanID;
   }
   
   public void setStARTitipanID(String stARTitipanID){
   		this.stARTitipanID = stARTitipanID;
   }
   
   public String getStTransactionNo(){
   		return stTransactionNo;
   }
   
   public void setStTransactionNo(String stTransactionNo){
   		this.stTransactionNo = stTransactionNo;
   }
   
   public String getStAccountID(){
   		return stAccountID;
   }
   
   public void setStAccountID(String stAccountID){
   		this.stAccountID = stAccountID;
   }
   
   public String getStAccountNo(){
   		return stAccountNo;
   }
   
   public void setStAccountNo(String stAccountNo){
   		this.stAccountNo = stAccountNo;
   }
   
   public String getStDescription(){
   		return stDescription;
   }
   
   public void setStDescription(String stDescription){
   		this.stDescription = stDescription;
   }

   public Date getDtApplyDate() {
      return dtApplyDate;
   }

   public void setDtApplyDate(Date dtApplyDate) {
      this.dtApplyDate = dtApplyDate;
   }

   public String getStTransactionID(){
   		return stTransactionID;
   }
   
   public void setStTransactionID(String stTransactionID){
   		this.stTransactionID = stTransactionID;
   }
   
   public String getStCurrencyCode(){
   		return stCurrencyCode;
   }
   
   public void setStCurrencyCode(String stCurrencyCode){
   		this.stCurrencyCode = stCurrencyCode;
   }
   
   public BigDecimal getDbCurrencyRate(){
   		return dbCurrencyRate;
   }
   
   public void setDbCurrencyRate(BigDecimal dbCurrencyRate){
   		this.dbCurrencyRate = dbCurrencyRate;
   }
   
   public String getStTransactionHeaderID(){
   		return stTransactionHeaderID;
   }
   
   public void setStTransactionHeaderID(String stTransactionHeaderID){
   		this.stTransactionHeaderID = stTransactionHeaderID;
   }
   
   public BigDecimal getDbBalance(){
   		return dbBalance;
   }
   
   public void setDbBalance(BigDecimal dbBalance){
   		this.dbBalance = dbBalance;
   }

   
   public DTOList getDetails() {
      if (details==null) {
         //if (!isNew()) {
            try {
               details = ListUtil.getDTOListFromQuery(
                       "select * from ar_titipan_details where ar_titipan_id = ?",
                       new Object[] {stARTitipanID},
                       ARTitipanPremiDetailsView.class
               );
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         //}
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
                       "select * from ar_titipan where ar_titipan_id = ?",
                       new Object[] {stARTitipanID},
                       ARTitipanView.class
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
   public DTOList getList2() {
      if (list2==null) {
         if (!isNew()) {
            try {
               list2 = ListUtil.getDTOListFromQuery(
                       "select * from ar_invoice where no_surat_hutang = ?",
                       new Object[] {stNoSuratHutang},
                       ARInvoiceView.class
               );
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         }
      }
      return list2;
   }*/

   public void setList2(DTOList list2) {
      this.list2 = list2;
   }

  


   /*
   public void validate() {

      //if (stGLARAccountID==null) throw new RuntimeException("AR/AP Account not defined");

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
   
   public String generateTransactionNo() throws Exception {

      //if (stReceiptNo!=null) return;
	  String transactionNo="";
      final String ccCode = Tools.getDigitRightJustified(getStCostCenterCode(),2);
      //final String methodCode = Tools.getDigitRightJustified(header.getStMethodCode(),1);
      //String stBankCode = getPaymentMethod()==null?"0000":getPaymentMethod().getStBankCode();
      //final String bankCode = Tools.getDigitRightJustified(stBankCode,4);

      String counterKey =
              DateUtil.getYear2Digit(getDtApplyDate())+
              DateUtil.getMonth2Digit(getDtApplyDate());

      String rn = String.valueOf(IDFactory.createNumericID("RCPNO"+counterKey));

      rn = Tools.getDigitRightJustified(rn,3);
      
      String accountcode="";
      if(getStAccountNo()!=null)
      		accountcode = getStAccountNo().substring(5,10);
      //else
      //		accountcode = transNo.substring(5,10);
      
      //110002700400
      //012345678901
      //no
      //  A0901171000000
      //  01234567890123
      //    A0910202700002

      transactionNo =
              "H" +
              counterKey +
              ccCode +
              accountcode +
              rn;
      
      setStTransactionNo(transactionNo);
              
      return transactionNo;

   }
   
   public void recalculate()throws Exception{
   	  try{
   	  	final DTOList detil = getDetails();
   	  
   	  	if(detil==null){
   	  		ARTitipanPremiDetailsView titipan = (ARTitipanPremiDetailsView) detil.get(0);
       		
       		titipan.setStAccountNo(getStAccountNo());
	  		titipan.setStDescription(getStDescription());
	  		titipan.setDbDebit(BDUtil.zero);
	  		titipan.setDbCredit(getDbBalance());
	  		
	  		getDetails().add(titipan);
   	  	}
       	
       	logger.logDebug("detil titipan= "+detil);
       	for(int i=0;i<details.size();i++){
       		ARTitipanPremiDetailsView titipan = (ARTitipanPremiDetailsView) detil.get(i);
       		
       		titipan.setStAccountNo(getStAccountNo());
	  		titipan.setStDescription(getStDescription());
	  		titipan.setDbDebit(BDUtil.zero);
	  		titipan.setDbCredit(getDbBalance());
       	}
   	  }catch(Exception e){
   	  	 
   	  }
       	
       	
       	
	 	
   }
   



}
