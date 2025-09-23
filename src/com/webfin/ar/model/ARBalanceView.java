/***********************************************************************
 * Module:  com.webfin.ar.model.ARBalanceView
 * Author:  Denny Mahendra
 * Created: Mar 14, 2006 1:00:00 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.util.BDUtil;
import com.crux.util.IDFactory;

import java.math.BigDecimal;

public class ARBalanceView extends DTO {

   /*
   CREATE TABLE ar_bal
(
  ent_id int8 NOT NULL,
  bal_ar numeric,
  bal_ap numeric,
  CONSTRAINT ar_bal_pk PRIMARY KEY (ent_id)
)
   */

   public void beforeInsert() {
   }

   public static String tableName = "ar_bal";
   //public static String tableName = "ar_bal_temp";

   public static String fieldMap[][] = {
      {"stEntityID","ent_id*pk"},
      {"dbBalAP","bal_ar"},
      {"dbBalAR","bal_ap"},
   };

   private String stEntityID;
   private BigDecimal dbBalAP;
   private BigDecimal dbBalAR;

   public String getStEntityID() {
      return stEntityID;
   }

   public void setStEntityID(String stEntityID) {
      this.stEntityID = stEntityID;
   }

   public BigDecimal getDbBalAP() {
      return dbBalAP;
   }

   public void setDbBalAP(BigDecimal dbBalAP) {
      this.dbBalAP = dbBalAP;
   }

   public BigDecimal getDbBalAR() {
      return dbBalAR;
   }

   public void setDbBalAR(BigDecimal dbBalAR) {
      this.dbBalAR = dbBalAR;
   }

   public void applyReverse(ARInvoiceView inv) {
      if (inv.isAP())
         dbBalAP = BDUtil.sub(dbBalAP, inv.getDbAmount());
      else
         dbBalAR = BDUtil.sub(dbBalAR, inv.getDbAmount());
   }

   public void apply(ARInvoiceView inv) {
      if (inv.isAP())
         dbBalAP = BDUtil.add(dbBalAP, inv.getDbAmount());
      else
         dbBalAR = BDUtil.add(dbBalAR, inv.getDbAmount());
   }
}
