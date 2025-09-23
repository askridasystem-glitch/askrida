/***********************************************************************
 * Module:  com.webfin.gl.ejb.CurrencyManager
 * Author:  Denny Mahendra
 * Created: Nov 1, 2005 10:45:37 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.ejb;

import com.crux.common.parameter.Parameter;
import com.crux.util.ListUtil;
import com.crux.util.Tools;
import com.webfin.gl.model.GLCurrencyHistoryView;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

public class CurrencyManager {
   private static CurrencyManager staticinstance;
   private BigDecimal one = new BigDecimal(1);
   public String mainCcy;

   public static CurrencyManager getInstance() {
      if (staticinstance == null) staticinstance = new CurrencyManager();
      return staticinstance;
   }

   private CurrencyManager() {
   }

   HashMap ccyHistMap = new HashMap();

   public String getMasterCurrency() {
      if (mainCcy==null)
         mainCcy = Parameter.readString("GL_CURRENCY");

      return mainCcy;
   }

   public BigDecimal getRate(String ccyCode, Date trxDate) throws Exception {
      if (ccyCode==null) return one;
      if (Tools.isEqual(ccyCode,getMasterCurrency())) return one;
      if (trxDate==null) trxDate=new Date();
      return (
              (GLCurrencyHistoryView)
              ListUtil.getDTOListFromQuery(
                      "select ccy_rate from gl_ccy_history where ccy_code=? and period_start <= ? and period_end >= ? order by ccy_hist_id desc limit 1",
                      new Object [] {ccyCode, trxDate, trxDate},
                      GLCurrencyHistoryView.class
              ).getDTO()
              ).getDbRate();
   }
   
   public BigDecimal getRateTreaty(String ccyCode, Date trxDate) throws Exception {
      if (ccyCode==null) return one;
      if (Tools.isEqual(ccyCode,getMasterCurrency())) return one;
      if (trxDate==null) trxDate=new Date();
      return (
              (GLCurrencyHistoryView)
              ListUtil.getDTOListFromQuery(
                      "select ccy_rate_treaty from gl_ccy_treaty_history where ccy_code=? and ccy_date<=? order by ccy_date desc limit 1",
                      new Object [] {ccyCode, trxDate},
                      GLCurrencyHistoryView.class
              ).getDTO()
              ).getDbRateTreaty();
   }

   public boolean isMasterCurrency(String stCurrencyCode) {
      return Tools.isEqual(stCurrencyCode,getMasterCurrency());
   }

   public BigDecimal getLatestRate(String ccyCode) throws Exception {
      if (ccyCode==null) return one;

      if (Tools.isEqual(ccyCode,getMasterCurrency())) return one;

      return (
              (GLCurrencyHistoryView)
              ListUtil.getDTOListFromQuery(
                      "select ccy_rate from gl_ccy_history where ccy_code=? order by ccy_hist_id desc limit 1",
                      new Object [] {ccyCode},
                      GLCurrencyHistoryView.class
              ).getDTO()
              ).getDbRate();
   }
}
