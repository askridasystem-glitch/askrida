/***********************************************************************
 * Module:  com.webfin.ar.model.ARInvoiceDetailView
 * Author:  Denny Mahendra
 * Created: Oct 25, 2005 10:38:43 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;
import com.crux.util.Tools;
import com.crux.util.BDUtil;
import com.webfin.gl.model.AccountView;

import java.math.BigDecimal;
import java.util.Date;

public class ARTitipanPremiDetailView extends DTO implements RecordAudit {

   /*
CREATE TABLE ar_titipan_premi_details
(
  ar_titipan_premi_det_id bigint NOT NULL,
  ar_titipan_premi_id bigint,
  description character varying(128),
  pol_no character varying(32),
  ar_titipan_premi_det_date timestamp without time zone,
  premi_amount numeric,
  create_date timestamp without time zone,
  create_who character varying(32),
  change_date timestamp without time zone,
  change_who character varying(32),
  CONSTRAINT ar_titipan_premi_details_pk PRIMARY KEY (ar_titipan_premi_det_id)
)
WITH OIDS;
ALTER TABLE ar_titipan_premi_details OWNER TO postgres;

   */

   public ARTitipanPremiDetailView() {
   }

   public static String tableName = "ar_titipan_premi_details";

   public static String fieldMap[][] = {
      {"stARTitipanPremiDetailID","ar_titipan_premi_det_id*pk"},
      {"stARTitipanPremiID","ar_titipan_premi_id"},
      {"stDescription","description"},
      {"stPolicyNo","pol_no"},
      {"dtARTitipanPremiDetail","ar_titipan_premi_det_date"},
      {"dbPremiAmount","premi_amount"},
   };
   
    private String stARTitipanPremiDetailID;
    private String stARTitipanPremiID;
	private String stTransactionNo;
	private String stDescription;
	private String stPolicyNo;
	
	private Date dtARTitipanPremiDetail;
	
	private BigDecimal dbPremiAmount;

   private ARTransactionLineView trxLine;
   private ARInvoiceDetailView ref;
   
   public String getStARTitipanPremiDetailID() {
      return stARTitipanPremiDetailID;
   }

   public void setStARTitipanPremiDetailID(String stARTitipanPremiDetailID) {
      this.stARTitipanPremiDetailID = stARTitipanPremiDetailID;
   }
   
   public String getStARTitipanPremiID() {
      return stARTitipanPremiID;
   }

   public void setStARTitipanPremiID(String stARTitipanPremiID) {
      this.stARTitipanPremiID = stARTitipanPremiID;
   }
   
   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }
   
   public String getStPolicyNo() {
      return stPolicyNo;
   }

   public void setStPolicyNo(String stPolicyNo) {
      this.stPolicyNo = stPolicyNo;
   }
   
   public Date getDtARTitipanPremiDetail() {
      return dtARTitipanPremiDetail;
   }

   public void setDtARTitipanPremiDetail(Date dtARTitipanPremiDetail) {
      this.dtARTitipanPremiDetail = dtARTitipanPremiDetail;
   }
   
   public BigDecimal getDbPremiAmount() {
      return dbPremiAmount;
   }

   public void setDbPremiAmount(BigDecimal dbPremiAmount) {
      this.dbPremiAmount = dbPremiAmount;
   }
   //end here

  
}
