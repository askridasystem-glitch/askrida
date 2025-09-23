/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyTypeGroupView
 * Author:  Denny Mahendra
 * Created: Mar 10, 2006 5:18:26 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

public class InsurancePolicyTypeGroupView extends DTO implements RecordAudit {

   /*
   CREATE TABLE ins_policy_type_grp
(
  ins_policy_type_grp_id int8 NOT NULL,
  group_name  varchar(128),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_polic_type_grp_pk PRIMARY KEY (ins_policy_type_grp_id)
) without oids;
   */

   public static String tableName = "ins_policy_type_grp";

   public static String fieldMap[][] = {
      {"stInsurancePolicyTypeGroupID","ins_policy_type_grp_id*pk"},
      {"stGroupName","group_name"},
      {"stCategory","cat"},
   };

   private String stInsurancePolicyTypeGroupID;
   private String stGroupName;
   
   private String stCategory;

   public String getStInsurancePolicyTypeGroupID() {
      return stInsurancePolicyTypeGroupID;
   }

   public void setStInsurancePolicyTypeGroupID(String stInsurancePolicyTypeGroupID) {
      this.stInsurancePolicyTypeGroupID = stInsurancePolicyTypeGroupID;
   }

   public String getStGroupName() {
      return stGroupName;
   }

   public void setStGroupName(String stGroupName) {
      this.stGroupName = stGroupName;
   }
   
    public String getStCategory() {
        return stCategory;
    }

    public void setStCategory(String stCategory) {
        this.stCategory = stCategory;
    }
}


