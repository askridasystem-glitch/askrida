/***********************************************************************
 * Module:  com.webfin.insurance.custom.CustomHandler
 * Author:  Denny Mahendra
 * Created: Sep 21, 2006 12:58:14 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.custom;

import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.insurance.model.InsurancePolicyObjectView;
import com.webfin.insurance.model.InsuranceSplitPolicyObjectView;
import com.webfin.insurance.model.InsuranceSplitPolicyView;

import java.math.BigDecimal;

public interface CustomHandler {
   public void onCalculate(InsurancePolicyView policy, InsurancePolicyObjectView obj, boolean validate);

   public boolean isLockTSI();

   void onNewObject(InsurancePolicyView policy, InsurancePolicyObjectView o);
   
   public void onCalculateTreaty(InsurancePolicyView policy, InsurancePolicyObjectView obj);
   
   public void customCriteria(InsurancePolicyView policy, InsurancePolicyObjectView obj);

   public void onCalculatePolicy(InsurancePolicyView policy, InsurancePolicyObjectView obj, boolean validate);

   public void onCalculateSpreading(InsurancePolicyView policy, InsurancePolicyObjectView obj, boolean validate);

  // public void onCalculateCoinsurance(InsurancePolicyView policy);

}
