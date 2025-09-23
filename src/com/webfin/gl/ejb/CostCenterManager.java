/***********************************************************************
 * Module:  com.webfin.gl.ejb.CostCenterManager
 * Author:  Denny Mahendra
 * Created: Nov 20, 2005 9:32:56 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.ejb;

import com.crux.util.ThreadContext;
import com.crux.util.LOV;
import com.crux.util.ListUtil;
import com.webfin.gl.model.GLCostCenterView;

public class CostCenterManager {
   public static CostCenterManager getInstance() {
      CostCenterManager ccm = (CostCenterManager)ThreadContext.getInstance().get("CostCenterManager");

      if (ccm==null) {
         ccm = new CostCenterManager();
         ThreadContext.getInstance().put("CostCenterManager",ccm);
      };

      return ccm;
   }

   private CostCenterManager() {
   }

   private String currentCostCenter;
   private LOV costCenterLOV;

   public String getCurrentCostCenter() {
      return currentCostCenter;
   }

   public void setCurrentCostCenter(String currentCostCenter) {
      this.currentCostCenter = currentCostCenter;
   }

   public LOV getCostCenterLOV() throws Exception {
      loadCostCenterLOV();

      return costCenterLOV.setLOValue(currentCostCenter);
   }

//   CREATE TABLE gl_cost_center
//(
//  cc_code varchar(8) NOT NULL,
//  description varchar(128) NOT NULL,
//  create_date timestamp NOT NULL,
//  create_who varchar(32) NOT NULL,
//  change_date timestamp,
//  change_who varchar(32),
//  CONSTRAINT dept_pk PRIMARY KEY (cc_code)
//)

   private void loadCostCenterLOV() throws Exception {
      if (costCenterLOV==null)
         costCenterLOV = ListUtil.getDTOListFromQuery(
                 "select cc_code,description from gl_cost_center order by description",
                 GLCostCenterView.class
         );
   }
}
