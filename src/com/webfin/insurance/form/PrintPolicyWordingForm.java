/***********************************************************************
 * Module:  com.webfin.insurance.form.PrintPolicyWordingForm
 * Author:  Denny Mahendra
 * Created: Sep 13, 2006 8:46:03 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.form;

import com.crux.web.form.Form;
import com.crux.lov.LOVManager;
import com.webfin.insurance.ejb.UserManager;

public class PrintPolicyWordingForm extends Form {
   private String stPolicyTypeGroupID;
   private String stPolicyTypeID;
   private String stFileID;

   public void initialize() {
      setTitle("POLICY DOCUMENTS");
   }

   public String getStFileID() {
      return stFileID;
   }

   public void setStFileID(String stFileID) {
      this.stFileID = stFileID;
   }

   public String getStPolicyTypeID() {
      return stPolicyTypeID;
   }

   public void setStPolicyTypeID(String stPolicyTypeID) {
      this.stPolicyTypeID = stPolicyTypeID;
   }

   public String getStPolicyTypeGroupID() {
      return stPolicyTypeGroupID;
   }

   public void setStPolicyTypeGroupID(String stPolicyTypeGroupID) {
      this.stPolicyTypeGroupID = stPolicyTypeGroupID;
   }

   public void onChangePolicyTypeGroup() {

   }

   public void onChangePolicyType() {

   }

   public void btnPrint() {
//      LOVManager.getInstance().getLOV(getDocLOVName(), null).getComboDesc(stFileID);
      if (stFileID!=null)
         super.redirect("/pages/insurance/pword/docs/"+stFileID);
   }

   public String getDocLOVName() {
       /*
       String cabang = UserManager.getInstance().getUser().getStBranch();
       String pattern = "VS_PWORD_"+getStPolicyTypeID();

       if(cabang!=null) pattern = pattern +"_"+cabang;
       
       String result = "";

       System.out.println("+++++++++++++ lov : " + LOVManager.getInstance().getLOV(pattern, null).getLOValue());
       if(LOVManager.getInstance().getLOV(pattern, null)==null)
            result = "VS_PWORD_"+getStPolicyTypeID();
       else
           result = pattern;

       return result;
       */

       return "VS_PWORD_"+getStPolicyTypeID();
   }
}
