/***********************************************************************
 * Module:  com.webfin.gl.model.UploadHeaderView
 * Author:  Denny Mahendra
 * Created: Jul 24, 2005 8:48:57 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.util.DTOList;

public class UploadHeaderView extends uploadEndorsemenView {
   private DTOList details;
   private boolean approvedMode;

   private String stFilePhysic;

   public DTOList getDetails() {
      return details;
   }

   public void setDetails(DTOList details) {
      this.details = details;
   }

   public void reCalculate() {

   }

    public boolean isApprovedMode()
    {
        return approvedMode;
    }

    public void setApprovedMode(boolean approvedMode)
    {
        this.approvedMode = approvedMode;
    }

    public String getStFilePhysic() {
        return stFilePhysic;
    }

    /**
     * @param stFilePhysic the stFilePhysic to set
     */
    public void setStFilePhysic(String stFilePhysic) {
        this.stFilePhysic = stFilePhysic;
    }
}
