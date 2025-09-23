/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyEntityView
 * Author:  Denny Mahendra
 * Created: Nov 8, 2005 1:57:37 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;
import com.crux.pool.DTOPool;
import com.webfin.FinCodec;
import com.webfin.entity.model.EntityView;

public class InsurancePolicyEntityView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ins_pol_entity
(
  ins_pe_id int8 NOT NULL,
  pol_id int8,
  ent_id int8,
  rel_type varchar(5),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  gl_comission varchar(32),
  CONSTRAINT ins_pol_entity_pk PRIMARY KEY (ins_pe_id)
)
   */

   public static String tableName = "ins_pol_entity";

   public static String fieldMap[][] = {
      {"stPolicyEntityID","ins_pe_id*pk"},
      {"stPolicyID","pol_id"},
      {"stEntityID","ent_id"},
      {"stRelationType","rel_type"},
   };

   private String stPolicyEntityID;
   private String stPolicyID;
   private String stEntityID;
   private String stRelationType;

   private EntityView entity;

   public EntityView getEntity() {
      if (entity==null) return (EntityView) DTOPool.getInstance().getDTO(EntityView.class,stEntityID);
      return entity;
   }

   public void setEntity(EntityView entity) {
      this.entity = entity;
   }

   public String getStPolicyEntityID() {
      return stPolicyEntityID;
   }

   public void setStPolicyEntityID(String stPolicyEntityID) {
      this.stPolicyEntityID = stPolicyEntityID;
   }

   public String getStPolicyID() {
      return stPolicyID;
   }

   public void setStPolicyID(String stPolicyID) {
      this.stPolicyID = stPolicyID;
   }

   public String getStEntityID() {
      return stEntityID;
   }

   public void setStEntityID(String stEntityID) {
      this.stEntityID = stEntityID;
   }

   public String getStRelationType() {
      return stRelationType;
   }

   public void setStRelationType(String stRelationType) {
      this.stRelationType = stRelationType;
   }

   public boolean isPolicyOwner() {
      return Tools.isEqual(stRelationType, FinCodec.PolicyEntityRelation.OWNER);
   }
}
