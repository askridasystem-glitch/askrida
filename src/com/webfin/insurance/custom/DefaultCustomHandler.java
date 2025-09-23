/***********************************************************************
 * Module:  com.webfin.insurance.custom.DefaultCustomHandler
 * Author:  Denny Mahendra
 * Created: Sep 29, 2006 12:19:50 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.custom;

import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.insurance.model.InsurancePolicyObjectView;
import com.webfin.insurance.model.InsurancePolicyObjDefaultView;
import com.webfin.insurance.model.InsurancePolicyCoverView;
import com.webfin.insurance.model.InsuranceRiskCategoryView;
import com.crux.util.DTOList;
import com.crux.util.BDUtil;

import java.math.BigDecimal;

public class DefaultCustomHandler implements CustomHandler {

   private static DefaultCustomHandler staticinstance;

   public static DefaultCustomHandler getInstance() {
      if (staticinstance == null) staticinstance = new DefaultCustomHandler();
      return staticinstance;
   }

   public DefaultCustomHandler() {
   }

   public void onCalculate(InsurancePolicyView policy, InsurancePolicyObjectView obj, boolean validate) {
      try {
         InsuranceRiskCategoryView rc = obj.getRiskCategory();
         BigDecimal rate = rc==null?null:rc.getRate(obj.getStRiskClass());

         DTOList covers = obj.getCoverage();

         for (int i = 0; i < covers.size(); i++) {
            InsurancePolicyCoverView cv = (InsurancePolicyCoverView) covers.get(i);

            if (cv.isAutoRate()) cv.setDbRate(rate);
         }
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public boolean isLockTSI() {
      return false;
   }

   public void onNewObject(InsurancePolicyView policy, InsurancePolicyObjectView o) {
   }
   
   public void onCalculateTreaty(InsurancePolicyView policy, InsurancePolicyObjectView o) {
   	
   }
   
   public void customCriteria(InsurancePolicyView policy, InsurancePolicyObjectView obj){
       
   }

   public void onCalculatePolicy(InsurancePolicyView policy, InsurancePolicyObjectView obj, boolean validate) {

   }

   public void onCalculateSpreading(InsurancePolicyView policy, InsurancePolicyObjectView obj, boolean validate){
       
   }

}
