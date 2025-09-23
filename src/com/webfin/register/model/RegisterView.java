/***********************************************************************
 * Module:  com.webfin.entity.model.EntityView
 * Author:  Denny Mahendra
 * Created: Oct 29, 2005 7:15:29 AM
 * Purpose:
 ***********************************************************************/

package com.webfin.register.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.login.model.UserSessionView;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.web.controller.SessionManager;
import com.webfin.FinCodec;
import com.webfin.insurance.model.InsuranceEntityView;

import java.util.Date;

public class RegisterView extends DTO implements RecordAudit {
    
    /*
     *-- Table: ins_pol_register
     
-- DROP TABLE ins_pol_register;
     
CREATE TABLE ins_pol_register
(
  reg_id bigint NOT NULL,
  ref_no character varying(32) NOT NULL,
  sender character varying(100),
  letter_date timestamp without time zone,
  subject character varying(80),
  create_who character varying(32) NOT NULL,
  create_date timestamp without time zone NOT NULL,
  change_who character varying(32),
  change_date timestamp without time zone,
  note character varying,
  receiver character varying(50),
  file_physic bigint,
  delete_flag character varying(1)
     * deadline_date timestamp without time zone,
  receive_date timestamp without time zone,
  division character varying(128),
  status character varying(64),
)
WITH (
  OIDS=TRUE
);
ALTER TABLE ins_pol_register
  OWNER TO postgres;
     
     */
    
    public static String tableName = "ins_pol_register";
    
    public static String comboFields[] = {"reg_id","sender"};
    
    public static String fieldMap[][] = {
        {"stRegID","reg_id*pk"},
        {"stRefNo","ref_no"},
        {"stSender","sender"},
        {"dtLetterDate","letter_date"},
        {"stSubject","subject"},
        {"stNote","note"},
        {"stReceiver","receiver"},
        {"stDeleteFlag","delete_flag"},
        {"stFilePhysic","file_physic"},
        {"stPolicyTypeID", "pol_type_id"},
        {"stPolicyTypeGroupID", "ins_policy_type_grp_id"},
        {"stCostCenterCode", "cc_code"},
        {"stRegionID", "region_id"},
        {"stPolicyTypeDesc", "policy_type_desc*n"},
        {"dtDeadlineDate","deadline_date"},
        {"dtReceiveDate","receive_date"},
        {"stDivision", "division"},
        {"stStatus", "status"},
        {"stUserID", "user_id"},
        {"stUserName", "user_name*n"},
        
    };
    
    private String stRefNo;
    private String stSender;
    private String stSubject;
    private String stNote;
    private Date dtLetterDate;
    private String stReceiver;
    private String stRegID;
    private String stFilePhysic;
    private String stDeleteFlag;
    private String stPolicyTypeID;
    private String stPolicyTypeGroupID;
    private String stCostCenterCode;
    private String stRegionID;
    private String stPolicyTypeDesc;
    private Date dtDeadlineDate;
    private Date dtReceiveDate;
    private String stDivision;
    private String  stStatus;
    private String stUserID;
    private String stUserName;

    private DTOList distributions;
    
    public String getStDeleteFlag() {
        return stDeleteFlag;
    }
    
    public void setStDeleteFlag(String stDeleteFlag) {
        this.stDeleteFlag = stDeleteFlag;
    }
    
    public String getStFilePhysic() {
        return stFilePhysic;
    }
    
    public void setStFilePhysic(String stFilePhysic) {
        this.stFilePhysic = stFilePhysic;
    }
    
    public String getStRefNo() {
        return stRefNo;
    }
    
    public void setStRefNo(String stRefNo) {
        this.stRefNo = stRefNo;
    }
    
    public String getStReceiver() {
        return stReceiver;
    }
    
    public void setStReceiver(String stReceiver) {
        this.stReceiver = stReceiver;
    }
    
    public String getStNote() {
        return stNote;
    }
    
    public void setStNote(String stNote) {
        this.stNote = stNote;
    }
    
    public String getStSender() {
        return stSender;
    }
    
    public void setStSender(String stSender) {
        this.stSender = stSender;
    }
    
    public String getStSubject() {
        return stSubject;
    }
    
    public void setStSubject(String stSubject) {
        this.stSubject = stSubject;
    }
    
    public Date getDtLetterDate() {
        return dtLetterDate;
    }
    
    public void setDtLetterDate(Date dtLetterDate) {
        this.dtLetterDate = dtLetterDate;
    }
    
//   public String getStShortName() {
//      return stShortName;
//   }
//
//   public void setStShortName(String stShortName) {
//      this.stShortName = stShortName;
//   }
//
//   public String getStContactPerson() {
//	      return stContactPerson;
//	   }
//
//   public void setStContactPerson(String stContactPerson) {
//	      this.stContactPerson = stContactPerson;
//	   }
//
//   public String getStComment() {
//	      return stComment;
//	   }
//
//   public void setStComment(String stComment) {
//	      this.stComment = stComment;
//	   }
    
   
    
    public void setDistributions(DTOList distributions) {
        this.distributions = distributions;
    }
    
//   public String getStCostCenterCode() {
//      return stCostCenterCode;
//   }
//
//   public void setStCostCenterCode(String stCostCenterCode) {
//      this.stCostCenterCode = stCostCenterCode;
//   }

    
    public String getStRegID() {
        return stRegID;
    }
    
    public void setStRegID(String stRegID) {
        this.stRegID = stRegID;
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

    public String getStCostCenterCode() {
        return stCostCenterCode;
    }

    public void setStCostCenterCode(String stCostCenterCode) {
        this.stCostCenterCode = stCostCenterCode;
    }

    public String getStRegionID() {
        return stRegionID;
    }

    public void setStRegionID(String stRegionID) {
        this.stRegionID = stRegionID;
    }

    public String getStPolicyTypeDesc() {
        return stPolicyTypeDesc;
    }

    public void setStPolicyTypeDesc(String stPolicyTypeDesc) {
        this.stPolicyTypeDesc = stPolicyTypeDesc;
    }

    /**
     * @return the dtDeadlineDate
     */
    public Date getDtDeadlineDate() {
        return dtDeadlineDate;
    }

    /**
     * @param dtDeadlineDate the dtDeadlineDate to set
     */
    public void setDtDeadlineDate(Date dtDeadlineDate) {
        this.dtDeadlineDate = dtDeadlineDate;
    }

    /**
     * @return the dtReceiveDate
     */
    public Date getDtReceiveDate() {
        return dtReceiveDate;
    }

    /**
     * @param dtReceiveDate the dtReceiveDate to set
     */
    public void setDtReceiveDate(Date dtReceiveDate) {
        this.dtReceiveDate = dtReceiveDate;
    }

    /**
     * @return the stStatus
     */
    public String getStStatus() {
        return stStatus;
    }

    /**
     * @param stStatus the stStatus to set
     */
    public void setStStatus(String stStatus) {
        this.stStatus = stStatus;
    }

    public String getStDivision() {
        return stDivision;
    }

    public void setStDivision(String stDivision) {
        this.stDivision = stDivision;
    }

    public String getStUserID() {
        return stUserID;
    }

    public void setStUserID(String stUserID) {
        this.stUserID = stUserID;
    }

    public String getStUserName() {
        return stUserName;
    }

    public void setStUserName(String stUserName) {
        this.stUserName = stUserName;
    }
}
