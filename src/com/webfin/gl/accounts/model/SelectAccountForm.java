/***********************************************************************
 * Module:  com.webfin.gl.accounts.model.SelectAccountForm
 * Author:  Denny Mahendra
 * Created: Nov 20, 2005 10:07:56 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.accounts.model;

import com.crux.common.model.DTO;

public class SelectAccountForm extends DTO {
   private String stKey;
   private String stAccountNo;
   private String stCostCenter;
   private String stMethod;
   private String stMonth;
   private String stYear;
   private String stUnit;
   private String stType;

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

    /**
     * @return the stMonth
     */
    public String getStMonth() {
        return stMonth;
    }

    /**
     * @param stMonth the stMonth to set
     */
    public void setStMonth(String stMonth) {
        this.stMonth = stMonth;
    }

    /**
     * @return the stYear
     */
    public String getStYear() {
        return stYear;
    }

    /**
     * @param stYear the stYear to set
     */
    public void setStYear(String stYear) {
        this.stYear = stYear;
    }

    public String getStUnit() {
        return stUnit;
    }

    /**
     * @param stUnit the stUnit to set
     */
    public void setStUnit(String stUnit) {
        this.stUnit = stUnit;
    }

    /**
     * @return the stType
     */
    public String getStType() {
        return stType;
    }

    /**
     * @param stType the stType to set
     */
    public void setStType(String stType) {
        this.stType = stType;
    }
}
