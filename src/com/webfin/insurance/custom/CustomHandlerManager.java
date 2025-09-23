/***********************************************************************
 * Module:  com.webfin.insurance.custom.CustomHandlerManager
 * Author:  Denny Mahendra
 * Created: Sep 21, 2006 1:00:06 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.custom;

import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.insurance.model.InsurancePolicyObjectView;
import com.webfin.insurance.model.InsuranceSplitPolicyObjectView;
import com.webfin.insurance.model.InsuranceSplitPolicyView;

import java.util.HashMap;
import java.math.BigDecimal;

public class CustomHandlerManager {
   private static CustomHandlerManager staticinstance;
   private HashMap customhandlerPool = new HashMap();

   public static CustomHandlerManager getInstance() {
      if (staticinstance == null) staticinstance = new CustomHandlerManager();
      return staticinstance;
   }

   private CustomHandlerManager() {
   }

   public void onCalculate(InsurancePolicyView policy, InsurancePolicyObjectView obj, boolean validate) {

      final String customhandler = policy.getPolicyType().getStCustomHandler();

      //if (customhandler == null) return;

      final CustomHandler ch = getCustomHandler(customhandler);

      ch.onCalculate(policy,obj, validate);
   }

   public CustomHandler getCustomHandler(String customhandler) {
      if (customhandler==null) return DefaultCustomHandler.getInstance();

      CustomHandler ch = (CustomHandler)customhandlerPool.get(customhandler);

      if (ch==null) {
         try {
            ch = (CustomHandler) Class.forName(customhandler).newInstance();
            customhandlerPool.put(customhandler, ch);
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
      }

      return ch;
   }
   
   public void onCalculateTreaty(InsurancePolicyView policy, InsurancePolicyObjectView obj) {

      final String customhandler = policy.getPolicyType().getStCustomHandler();

      //if (customhandler == null) return;

      final CustomHandler ch = getCustomHandler(customhandler);

      ch.onCalculateTreaty(policy,obj);
   }
   
   public void customCriteria(InsurancePolicyView policy, InsurancePolicyObjectView obj) {

      final String customhandler = policy.getPolicyType().getStCustomHandler();

      //if (customhandler == null) return;

      final CustomHandler ch = getCustomHandler(customhandler);

      ch.customCriteria(policy,obj);
   }

   public void onCalculatePolicy(InsurancePolicyView policy, InsurancePolicyObjectView obj, boolean validate) {

      final String customhandler = policy.getPolicyType().getStCustomHandler();

      //if (customhandler == null) return;

      final CustomHandler ch = getCustomHandler(customhandler);

      ch.onCalculatePolicy(policy,obj, validate);
   }
   /*
   public void onCalculateCoinsurance(InsurancePolicyView policy) {

      final String customhandler = policy.getPolicyType().getStCustomHandler();

      //if (customhandler == null) return;

      final CustomHandler ch = getCustomHandler(customhandler);

      ch.onCalculateCoinsurance(policy);
   }*/

   public void onCalculateSpreading(InsurancePolicyView policy, InsurancePolicyObjectView obj, boolean validate) {

        final String customhandler = policy.getPolicyType().getStCustomHandler();

        //if (customhandler == null) return;

        final CustomHandler ch = getCustomHandler(customhandler);

        ch.onCalculateSpreading(policy, obj, validate);
    }


}
