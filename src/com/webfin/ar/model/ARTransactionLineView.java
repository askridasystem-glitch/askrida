/***********************************************************************
 * Module:  com.webfin.ar.model.ARTransactionLineView
 * Author:  Denny Mahendra
 * Created: Jan 13, 2006 11:18:10 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;
import com.crux.util.Tools;

public class ARTransactionLineView extends DTO implements RecordAudit {

   /*

CREATE TABLE ar_trx_line
(
  ar_trx_line_id int8 NOT NULL,
  ar_trx_type_id int8 NOT NULL,
  item_desc varchar(128) NOT NULL,
  gl_account varchar(32) NOT NULL,
  positive_flag varchar(1) NOT NULL,
  enabled_flag varchar(1) NOT NULL,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ar_trx_line_pk PRIMARY KEY (ar_trx_line_id)
  comission_flag
)
   */

   public static String tableName = "ar_trx_line"; 

   public static String fieldMap[][] = {
      {"stARTrxLineID","ar_trx_line_id*pk"},
      {"stARTrxTypeID","ar_trx_type_id"},
      {"stItemDesc","item_desc"},
      {"stGLAccount","gl_account"},
      {"stNegativeFlag","negative_flag"},
      {"stEnabledFlag","enabled_flag"},
      {"stComissionFlag","comission_flag"},
      {"stTaxCode","tax_code"},
      {"stItemClass","item_class"},
      {"stGLArAccount","gl_ar_account"},
      {"stGLExcessAccount","gl_excess_account"},
      {"stCategory","category"},
      {"stGLApAccount","gl_ap_account"},
      {"stGLApAccount2","gl_ap_account2"},
      {"stGLAccountWithoutPremi","gl_account_without_premi"},
      {"stGLAccount2","gl_account2"},

   };

   private String stARTrxLineID;
   private String stARTrxTypeID;
   private String stItemDesc;
   private String stGLAccount;
   private String stNegativeFlag;
   private String stEnabledFlag;
   private String stComissionFlag;
   private String stTaxCode;
   private String stItemClass;
   private String stGLArAccount;
   private String stGLExcessAccount;
   private String stCategory;
   private String stGLApAccount;
   private String stGLApAccount2;
   private String stGLAccountWithoutPremi;
   private String stGLAccount2;

    public String getStGLAccount2() {
        return stGLAccount2;
    }

    public void setStGLAccount2(String stGLAccount2) {
        this.stGLAccount2 = stGLAccount2;
    }
   
   public String getStCategory(){
      return stCategory;
   }

   public void setStCategory(String stCategory) {
      this.stCategory = stCategory;
   }

   public String getStNegativeFlag() {
      return stNegativeFlag;
   }

   public void setStNegativeFlag(String stNegativeFlag) {
      this.stNegativeFlag = stNegativeFlag;
   }

   public String getStARTrxLineID() {
      return stARTrxLineID;
   }

   public void setStARTrxLineID(String stARTrxLineID) {
      this.stARTrxLineID = stARTrxLineID;
   }

   public String getStARTrxTypeID() {
      return stARTrxTypeID;
   }

   public void setStARTrxTypeID(String stARTrxTypeID) {
      this.stARTrxTypeID = stARTrxTypeID;
   }

   public String getStItemDesc() {
      return stItemDesc;
   }

   public void setStItemDesc(String stItemDesc) {
      this.stItemDesc = stItemDesc;
   }

   public String getStGLAccount() {
      return stGLAccount;
   }

   public void setStGLAccount(String stGLAccount) {
      this.stGLAccount = stGLAccount;
   }

   public String getStEnabledFlag() {
      return stEnabledFlag;
   }

   public void setStEnabledFlag(String stEnabledFlag) {
      this.stEnabledFlag = stEnabledFlag;
   }

   public String getStComissionFlag() {
      return stComissionFlag;
   }

   public void setStComissionFlag(String stComissionFlag) {
      this.stComissionFlag = stComissionFlag;
   }

   public String getStTaxCode() {
      return stTaxCode;
   }

   public void setStTaxCode(String stTaxCode) {
      this.stTaxCode = stTaxCode;
   }

   public boolean isTaxed() {
      return null!=stTaxCode;
   }

   public ARTaxView getTax() {
      return (ARTaxView) DTOPool.getInstance().getDTO(ARTaxView.class, stTaxCode);
   }

   public boolean isNegative() {
      return Tools.isYes(stNegativeFlag);
   }

   public String getStItemClass() {
      return stItemClass;
   }

   public void setStItemClass(String stItemClass) {
      this.stItemClass = stItemClass;
   }
   
   public String getStGLArAccount2(boolean isCheck) {
   	  String code[] = getStGLArAccount().split("[\\|]");
   	  String account = "";
   	  if(!isCheck)	account = code[0];
      else if(isCheck)	account = code[1];
       
      return account;
   }
   
   public String getStGLArAccount() {
      return stGLArAccount;
   }

   public void setStGLArAccount(String stGLArAccount) {
      this.stGLArAccount = stGLArAccount;
   }
   
   public String getStGLExcessAccount() {
      return stGLExcessAccount;
   }

   public void setStGLExcessAccount(String stGLExcessAccount) {
      this.stGLExcessAccount = stGLExcessAccount;
   }
   
   public String getStGLArAccountByType(String type) {
   	  String code[] = getStGLArAccount().split("[\\|]");
   	  String account = null;
   	  if("AR".equalsIgnoreCase(type)) account = code[0];
          else if("AP".equalsIgnoreCase(type))	account = code[1];
      
      return account;
   }

    public String getStGLApAccount()
    {
        return stGLApAccount;
    }

    public void setStGLApAccount(String stGLApAccount)
    {
        this.stGLApAccount = stGLApAccount;
    }

    public String getStGLApAccount2() {
        return stGLApAccount2;
    }

    public void setStGLApAccount2(String stGLApAccount2) {
        this.stGLApAccount2 = stGLApAccount2;
    }

    public ARTransactionTypeView getTransactionType() {
      return (ARTransactionTypeView) DTOPool.getInstance().getDTO(ARTransactionTypeView.class, stARTrxTypeID);
   }

    /**
     * @return the stGLAccountWithoutPremi
     */
    public String getStGLAccountWithoutPremi() {
        return stGLAccountWithoutPremi;
    }

    /**
     * @param stGLAccountWithoutPremi the stGLAccountWithoutPremi to set
     */
    public void setStGLAccountWithoutPremi(String stGLAccountWithoutPremi) {
        this.stGLAccountWithoutPremi = stGLAccountWithoutPremi;
    }

}
