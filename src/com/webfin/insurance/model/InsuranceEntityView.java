/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceEntityView
 * Author:  Denny Mahendra
 * Created: Oct 25, 2005 9:57:11 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

public class InsuranceEntityView extends DTO implements RecordAudit {
    /*
    CREATE TABLE ins_entity
(
  ins_entity_id int8 NOT NULL,
  ins_entity_type varchar(5),
  ent_id int8,
  active_flag varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  gl_comission varchar(32),
  CONSTRAINT ins_entity_pk PRIMARY KEY (ins_entity_id)
)
    */

    public static String tableName = "ins_entity";

    public static String fieldMap[][] = {
       {"stInsuranceEntityID","ins_entity_id*pk"},
       {"stEntityType","ins_entity_type"},
       {"stEntityID","ent_id"},
       {"stActiveFlag","active_flag"},
       {"stGLAP","gl_ap"},
    };

    private String stInsuranceEntityID;
    private String stEntityType;
    private String stEntityID;
    private String stActiveFlag;
    private String stGLAP;

    public String getStInsuranceEntityID() {
        return stInsuranceEntityID;
    }

    public void setStInsuranceEntityID(String stInsuranceEntityID) {
        this.stInsuranceEntityID = stInsuranceEntityID;
    }

    public String getStEntityType() {
        return stEntityType;
    }

    public void setStEntityType(String stEntityType) {
        this.stEntityType = stEntityType;
    }

    public String getStEntityID() {
        return stEntityID;
    }

    public void setStEntityID(String stEntityID) {
        this.stEntityID = stEntityID;
    }

    public String getStActiveFlag() {
        return stActiveFlag;
    }

    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }

    public String getStGLAP() {
        return stGLAP;
    }

    public void setStGLAP(String stGLAP) {
        this.stGLAP = stGLAP;
    }
}
