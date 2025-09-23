/***********************************************************************
 * Module:  com.webfin.gl.accounts.model.SelectAccountForm
 * Author:  Denny Mahendra
 * Created: Nov 20, 2005 10:07:56 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.entity.model;

import com.crux.common.model.DTO;

public class SelectEntityForm extends DTO {
   private String stKey;
   private String stAccountNo;
   private String stCostCenter;
   private String stMethod;

   public String getStKey() {
      return stKey;
   }

   public void setStKey(String stKey) {
      this.stKey = stKey;
   }

   public String getStAccountNo() {
      return stAccountNo;
   }

   public void setStAccountNo(String stAccountNo) {
      this.stAccountNo = stAccountNo;
   }

   public String getStCostCenter() {
      return stCostCenter;
   }

   public void setStCostCenter(String stCostCenter) {
      this.stCostCenter = stCostCenter;
   }
   
   public String getStMethod() {
      return stMethod;
   }

   public void setStMethod(String stMethod) {
      this.stMethod = stMethod;
   }
}
