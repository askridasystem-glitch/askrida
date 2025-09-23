/***********************************************************************
 * Module:  com.webfin.insurance.model.EntityDocumentView
 * Author:  Ahmad Rhodoni
 * Created: 
 * Purpose: 
 ***********************************************************************/

package com.webfin.entity.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;

public class EntityDocumentView extends DTO implements RecordAudit {

   /*

CREATE TABLE ent_documents
(
 ent_document_id bigint NOT NULL,
  ent_document_type_id bigint,
  document_class character varying(8),
  file_physic bigint,
  create_date timestamp without time zone NOT NULL,
  create_who character varying(32) NOT NULL,
  change_date timestamp without time zone,
  change_who character varying(32),
  ent_id bigint,
  CONSTRAINT ent_documents_pkey PRIMARY KEY (ent_document_id)
)

ALTER TABLE ins_pol_documents ADD COLUMN policy_id int8;
ALTER TABLE ins_pol_documents ADD COLUMN ins_pol_obj_id int8;

   */

   public static String tableName = "ent_documents";

   public static String fieldMap[][] = {
      {"stEntityDocumentID","ent_document_id*pk*nd"},
      {"stInsuranceDocumentTypeID","ins_document_type_id"},
      {"stDocumentClass","document_class"},
      {"stFilePhysic","file_physic"},
      {"stEntityID","ent_id"},
      {"stDescription","description*n"},
   };

   private String stEntityDocumentID;
   private String stInsuranceDocumentTypeID;
   private String stDocumentClass;
   private String stFilePhysic;
   private String stEntityID;
   private String stDescription;
   private String stSelectedFlag;

   public String getStSelectedFlag() {
      return stSelectedFlag;
   }

   public void setStSelectedFlag(String stSelectedFlag) {
      this.stSelectedFlag = stSelectedFlag;
   }

   public boolean isMarked() {
      return Tools.isYes(stSelectedFlag);
   }

    /**
     * @return the stEntityDocumentID
     */
    public String getStEntityDocumentID() {
        return stEntityDocumentID;
    }

    /**
     * @param stEntityDocumentID the stEntityDocumentID to set
     */
    public void setStEntityDocumentID(String stEntityDocumentID) {
        this.stEntityDocumentID = stEntityDocumentID;
    }


    /**
     * @return the stDocumentClass
     */
    public String getStDocumentClass() {
        return stDocumentClass;
    }

    /**
     * @param stDocumentClass the stDocumentClass to set
     */
    public void setStDocumentClass(String stDocumentClass) {
        this.stDocumentClass = stDocumentClass;
    }

    /**
     * @return the stFilePhysic
     */
    public String getStFilePhysic() {
        return stFilePhysic;
    }

    /**
     * @param stFilePhysic the stFilePhysic to set
     */
    public void setStFilePhysic(String stFilePhysic) {
        this.stFilePhysic = stFilePhysic;
    }

    /**
     * @return the stEntityID
     */
    public String getStEntityID() {
        return stEntityID;
    }

    /**
     * @param stEntityID the stEntityID to set
     */
    public void setStEntityID(String stEntityID) {
        this.stEntityID = stEntityID;
    }

    /**
     * @return the stDescription
     */
    public String getStDescription() {
        return stDescription;
    }

    /**
     * @param stDescription the stDescription to set
     */
    public void setStDescription(String stDescription) {
        this.stDescription = stDescription;
    }

    /**
     * @return the stInsuranceDocumentTypeID
     */
    public String getStInsuranceDocumentTypeID() {
        return stInsuranceDocumentTypeID;
    }

    /**
     * @param stInsuranceDocumentTypeID the stInsuranceDocumentTypeID to set
     */
    public void setStInsuranceDocumentTypeID(String stInsuranceDocumentTypeID) {
        this.stInsuranceDocumentTypeID = stInsuranceDocumentTypeID;
    }
}
