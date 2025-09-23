/***********************************************************************
 * Module:  com.webfin.ar.model.ARTransactionTypeView
 * Author:  Denny Mahendra
 * Created: Jan 13, 2006 11:15:56 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.LogManager;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.util.Tools;

import java.util.HashMap;

public class ARTransactionTypeView extends DTO implements RecordAudit {

   private final static transient LogManager logger = LogManager.getInstance(ARTransactionTypeView.class);

   /*

   CREATE TABLE ar_trx_type
(
  ar_trx_type_id int8 NOT NULL,
  positive_flag varchar(1),
  description varchar(128),
  gl_account_id int8,
  active_flag varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  ar_account varchar(32),
  ap_account varchar(32),
  CONSTRAINT ar_trx_type_pk PRIMARY KEY (ar_trx_type_id)

ALTER TABLE ar_trx_type ADD COLUMN parent_trx_id int8;
ALTER TABLE ar_trx_type ADD COLUMN super_type_flag varchar(1);

)
WITH OIDS;
   */

   public static String tableName = "ar_trx_type";

   public static String fieldMap[][] = {
      {"stARTrxTypeID","ar_trx_type_id*pk"},
      {"stPositiveFlag","positive_flag"},
      {"stDescription","description"},
      {"stGLAccountID","gl_account_id"},
      {"stInvoiceType","invoice_type"},
      {"stGLARAccount","ar_account"},
      {"stGLAPAccount","ap_account"},
      {"stActiveFlag","active_flag"},
      {"stTrxCodes","ar_trx_codes"},
      {"stResourceID","resource_id"},
      {"stParentTrxID","parent_trx_id"},
      {"stSuperTypeFlag","super_type_flag"},
      {"stRAFlag","ra_flag"},
      {"stMenuID","menu_id"},
   };

   public static String comboFields[] = {"ar_trx_type_id","description"};

   private String stARTrxTypeID;
   private String stPositiveFlag;
   private String stInvoiceType;
   private String stActiveFlag;
   private String stDescription;
   private String stGLAccountID;
   private String stGLARAccount;
   private String stGLAPAccount;
   private String stTrxCodes;
   private String stResourceID;
   private String stParentTrxID;
   private String stSuperTypeFlag;
   private String stRAFlag;
   public HashMap codeMap;
   private DTOList items;
   private String TaxCode;
   private String stMenuID;

   public DTOList getItems() {
      try {
         if (items==null) items=
                 ListUtil.getDTOListFromQuery(
                         "select * from ar_trx_line where ar_trx_type_id = ? order by order_no",
                         new Object [] {stARTrxTypeID},
                         ARTransactionLineView.class
                 );
         return items;
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void setItems(DTOList items) {
      this.items = items;
   }

   public String getStARTrxTypeID() {
      return stARTrxTypeID;
   }

   public void setStARTrxTypeID(String stARTrxTypeID) {
      this.stARTrxTypeID = stARTrxTypeID;
   }

   public String getStPositiveFlag() {
      return stPositiveFlag;
   }

   public void setStPositiveFlag(String stPositiveFlag) {
      this.stPositiveFlag = stPositiveFlag;
   }

   public String getStActiveFlag() {
      return stActiveFlag;
   }

   public void setStActiveFlag(String stActiveFlag) {
      this.stActiveFlag = stActiveFlag;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public String getStGLAccountID() {
      return stGLAccountID;
   }

   public void setStGLAccountID(String stGLAccountID) {
      this.stGLAccountID = stGLAccountID;
   }

   public String getStGLARAccount() {
      return stGLARAccount;
   }

   public void setStGLARAccount(String stGLARAccount) {
      this.stGLARAccount = stGLARAccount;
   }

   public String getStGLAPAccount() {
      return stGLAPAccount;
   }

   public void setStGLAPAccount(String stGLAPAccount) {
      this.stGLAPAccount = stGLAPAccount;
   }

   public String getStTrxCodes() {
      return stTrxCodes;
   }

   public void setStTrxCodes(String stTrxCodes) {
      this.stTrxCodes = stTrxCodes;
      processCodes();
   }

   private void processCodes() {
      codeMap = new HashMap();

      if (stTrxCodes==null) return;

      final String[] codes = stTrxCodes.split("[\\|]");

      for (int i = 0; i < codes.length; i++) {
         String code = codes[i];

         codeMap.put(code,code);
      }

      logger.logDebug("processCodes: codeMap = "+codeMap);
   }

   public boolean hasFlag(String s) {

      if (codeMap==null) return false;

      return codeMap.containsKey(s);
   }

   public void setStInvoiceType(String stInvoiceType) {
      this.stInvoiceType = stInvoiceType;
   }

   public String getStInvoiceType() {
      return stInvoiceType;
   }

   public String getStResourceID() {
      return stResourceID;
   }

   public void setStResourceID(String stResourceID) {
      this.stResourceID = stResourceID;
   }

   public String getStParentTrxID() {
      return stParentTrxID;
   }

   public void setStParentTrxID(String stParentTrxID) {
      this.stParentTrxID = stParentTrxID;
   }

   public String getStSuperTypeFlag() {
      return stSuperTypeFlag;
   }

   public void setStSuperTypeFlag(String stSuperTypeFlag) {
      this.stSuperTypeFlag = stSuperTypeFlag;
   }

   public boolean isSuperType() {
      return Tools.isYes(getStSuperTypeFlag());
   }

   public boolean trxEnablePolType() {return hasFlag("ENABLE_POLTYPE");}
   public boolean trxEnablePolis() {return hasFlag("ENABLE_POLIS");}
   public boolean trxEnableUWrit() {return hasFlag("ENABLE_UWRIT");}
   public boolean trxEnableFixedItem() {return hasFlag("FIXED_ITEM");}
   public boolean trxDisableDetail() {return hasFlag("NO_DETAIL");}
   public boolean trxCustIns() {return hasFlag("CUST_INS");}
   public boolean trxEnableReins() {return hasFlag("ENABLE_REINS");}

   public String getStRAFlag() {
      return stRAFlag;
   }

   public void setStRAFlag(String stRAFlag) {
      this.stRAFlag = stRAFlag;
   }
   
   public void getCommOrTax(){
   		String GL[] = getStGLAPAccount().split("[\\|]");
   		String Comm = GL[0];
   		String Tax 	= GL[1];
   		setStTaxCode(Tax);
   }
   
   public void setStTaxCode(String TaxCode){
   		this.TaxCode = TaxCode;
   }
   
   public String getStTaxCode(){
   		return TaxCode;
   }

    public String getStMenuID() {
        return stMenuID;
    }

    public void setStMenuID(String stMenuID) {
        this.stMenuID = stMenuID;
    }
    
    public boolean trxEnableAttachment() {return hasFlag("ENABLE_ATTACHMENT");}
    
    public boolean trxEnableXOL() {return hasFlag("ENABLE_XOL");}

    public boolean trxEnableReinsured() {
        return hasFlag("ENABLE_REINSURED");
    }

}
