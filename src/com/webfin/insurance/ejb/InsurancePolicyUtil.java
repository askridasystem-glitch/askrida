/***********************************************************************
 * Module:  com.webfin.insurance.ejb.InsurancePolicyUtil
 * Author:  Denny Mahendra
 * Created: Jun 20, 2006 12:51:37 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.ejb;

import com.crux.util.SQLUtil;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InsurancePolicyUtil {
   private static InsurancePolicyUtil staticinstance;

   public static InsurancePolicyUtil getInstance() {
      if (staticinstance == null) staticinstance = new InsurancePolicyUtil();
      return staticinstance;
   }

   private InsurancePolicyUtil() {
   }

   public String findPremiumFactor(BigDecimal periodBase) {

      try {
         final SQLUtil S = new SQLUtil();

         try {

            final PreparedStatement PS = S.setQuery(
                    "   select " +
                    "      ins_premium_factor_id " +
                    "   from " +
                    "      ins_premium_factor " +
                    "   where " +
                    "      period_rate_factor>=? " +
                    "   order by " +
                    "      period_rate_factor " +
                    "   limit 1");

            PS.setBigDecimal(1,periodBase);

            final ResultSet RS = PS.executeQuery();

            if (!RS.next()) return null;

            return RS.getString(1);

         } finally {

            S.release();
         }

      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
}
