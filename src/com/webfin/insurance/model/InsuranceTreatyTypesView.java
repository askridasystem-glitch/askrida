/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceTreatyTypesView
 * Author:  Denny Mahendra
 * Created: Jul 2, 2006 1:55:22 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;

import java.math.BigDecimal;

public class InsuranceTreatyTypesView extends DTO implements RecordAudit {

   /*

   CREATE TABLE ins_treaty_types
(
  ins_treaty_type_id  varchar(5) NOT NULL,
  treaty_type_name  varchar(128),
  treaty_type_level numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_treaty_types_pk PRIMARY KEY (ins_treaty_type_id)
) without oids;


ALTER TABLE ins_treaty_types ADD COLUMN or_share_flag varchar(1);
ALTER TABLE ins_treaty_types ADD COLUMN free_members_flag varchar(1);

   */

   public static String tableName = "ins_treaty_types";


   public static String fieldMap[][] = {
      {"stInsuranceTreatyTypeID","ins_treaty_type_id*pk"},
      {"stInsuranceTreatyTypeName","treaty_type_name"},
      {"dbTreatyTypeLevel","treaty_type_level"},
      {"stORShareFlag","or_share_flag"},
      {"stFreeMembersFlag","free_members_flag"},
      {"stNonProportionalFlag","non_proportional_flag"},
      {"stFreeTSIFlag","free_tsi_flag"},
      {"stUseRateFlag","use_rate_flag"},
      {"stTreatyTypeGLCode","treaty_type_gl_code"},
      {"stTreatyTypeGLCode2","treaty_type_gl_code2"},
      {"stTreatyTypeGLCode3","treaty_type_gl_code3"},
      {"stTreatyTypeGLCode4","treaty_type_gl_code4"},
      {"stTreatyTypeGLCode5","treaty_type_gl_code5"},
      {"stTreatyTypeGLCode6","treaty_type_gl_code6"},
      {"stTreatyClass","treaty_class"},
      {"stJournalFlag","journal_flag"},
      {"stReinsuranceSlipFlag","ri_slip_flag"},
      {"stTransactionNoHeader","trx_no_header"},
      {"stInsuranceTreatyTypeReference","ins_treaty_type_reference"},
      {"stInsuranceTreatyTypeReferenceClaim","ins_treaty_type_ref_claim"},
   };

   private String stInsuranceTreatyTypeID;
   private String stInsuranceTreatyTypeName;
   private String stORShareFlag;
   private String stNonProportionalFlag;
   private String stFreeMembersFlag;
   private String stFreeTSIFlag;
   private String stUseRateFlag;
   private String stTreatyTypeGLCode;
   private String stTreatyTypeGLCode2;
   private String stTreatyTypeGLCode3;
   private String stTreatyTypeGLCode4;
   private String stTreatyTypeGLCode5;
   private String stTreatyClass;
   private BigDecimal dbTreatyTypeLevel;
   private String stJournalFlag;
   private String stReinsuranceSlipFlag;
   private String stTransactionNoHeader;
   private String stTreatyTypeGLCode6;
   private String stInsuranceTreatyTypeReference;
   private String stInsuranceTreatyTypeReferenceClaim;

    public String getStInsuranceTreatyTypeReferenceClaim() {
        return stInsuranceTreatyTypeReferenceClaim;
    }

    public void setStInsuranceTreatyTypeReferenceClaim(String stInsuranceTreatyTypeReferenceClaim) {
        this.stInsuranceTreatyTypeReferenceClaim = stInsuranceTreatyTypeReferenceClaim;
    }

    public String getStInsuranceTreatyTypeReference() {
        return stInsuranceTreatyTypeReference;
    }

    public void setStInsuranceTreatyTypeReference(String stInsuranceTreatyTypeReference) {
        this.stInsuranceTreatyTypeReference = stInsuranceTreatyTypeReference;
    }

   public String getStTreatyClass() {
      return stTreatyClass;
   }

   public void setStTreatyClass(String stTreatyClass) {
      this.stTreatyClass = stTreatyClass;
   }

   public String getStTreatyTypeGLCode4() {
      return stTreatyTypeGLCode4;
   }

   public void setStTreatyTypeGLCode4(String stTreatyTypeGLCode4) {
      this.stTreatyTypeGLCode4 = stTreatyTypeGLCode4;
   }

   public String getStTreatyTypeGLCode5() {
      return stTreatyTypeGLCode5;
   }

   public void setStTreatyTypeGLCode5(String stTreatyTypeGLCode5) {
      this.stTreatyTypeGLCode5 = stTreatyTypeGLCode5;
   }

   public String getStTreatyTypeGLCode3() {
      return stTreatyTypeGLCode3;
   }

   public void setStTreatyTypeGLCode3(String stTreatyTypeGLCode3) {
      this.stTreatyTypeGLCode3 = stTreatyTypeGLCode3;
   }

   public String getStTreatyTypeGLCode2() {
      return stTreatyTypeGLCode2;
   }

   public void setStTreatyTypeGLCode2(String stTreatyTypeGLCode2) {
      this.stTreatyTypeGLCode2 = stTreatyTypeGLCode2;
   }

   public String getStTreatyTypeGLCode() {
      return stTreatyTypeGLCode;
   }

   public void setStTreatyTypeGLCode(String stTreatyTypeGLCode) {
      this.stTreatyTypeGLCode = stTreatyTypeGLCode;
   }

   public String getStNonProportionalFlag() {
      return stNonProportionalFlag;
   }

   public void setStNonProportionalFlag(String stNonProportionalFlag) {
      this.stNonProportionalFlag = stNonProportionalFlag;
   }

   public String getStORShareFlag() {
      return stORShareFlag;
   }

   public void setStORShareFlag(String stORShareFlag) {
      this.stORShareFlag = stORShareFlag;
   }

   public String getStFreeMembersFlag() {
      return stFreeMembersFlag;
   }

   public void setStFreeMembersFlag(String stFreeMembersFlag) {
      this.stFreeMembersFlag = stFreeMembersFlag;
   }

   public String getStInsuranceTreatyTypeID() {
      return stInsuranceTreatyTypeID;
   }

   public void setStInsuranceTreatyTypeID(String stInsuranceTreatyTypeID) {
      this.stInsuranceTreatyTypeID = stInsuranceTreatyTypeID;
   }

   public String getStInsuranceTreatyTypeName() {
      return stInsuranceTreatyTypeName;
   }

   public void setStInsuranceTreatyTypeName(String stInsuranceTreatyTypeName) {
      this.stInsuranceTreatyTypeName = stInsuranceTreatyTypeName;
   }

   public BigDecimal getDbTreatyTypeLevel() {
      return dbTreatyTypeLevel;
   }

   public void setDbTreatyTypeLevel(BigDecimal dbTreatyTypeLevel) {
      this.dbTreatyTypeLevel = dbTreatyTypeLevel;
   }

   public boolean isNonProportional() {
      return Tools.isYes(stNonProportionalFlag);
   }
   
   public boolean isNonXOL() {
      return Tools.isYes(stORShareFlag);
   }

   public String getStFreeTSIFlag() {
      return stFreeTSIFlag;
   }

   public void setStFreeTSIFlag(String stFreeTSIFlag) {
      this.stFreeTSIFlag = stFreeTSIFlag;
   }

   public boolean isFreeTSI() {
      return Tools.isYes(stFreeTSIFlag);
   }

   public String getStUseRateFlag() {
      return stUseRateFlag;
   }

   public void setStUseRateFlag(String stUseRateFlag) {
      this.stUseRateFlag = stUseRateFlag;
   }

    public String getStJournalFlag() {
        return stJournalFlag;
    }

    public void setStJournalFlag(String stJournalFlag) {
        this.stJournalFlag = stJournalFlag;
    }
    
    public boolean isJournal() {
      return Tools.isYes(stJournalFlag);
   }

    public String getStReinsuranceSlipFlag() {
        return stReinsuranceSlipFlag;
    }

    public void setStReinsuranceSlipFlag(String stReinsuranceSlipFlag) {
        this.stReinsuranceSlipFlag = stReinsuranceSlipFlag;
    }

    /**
     * @return the stTransactionNoHeader
     */
    public String getStTransactionNoHeader() {
        return stTransactionNoHeader;
    }

    /**
     * @param stTransactionNoHeader the stTransactionNoHeader to set
     */
    public void setStTransactionNoHeader(String stTransactionNoHeader) {
        this.stTransactionNoHeader = stTransactionNoHeader;
    }

    /**
     * @return the stTreatyTypeGLCode6
     */
    public String getStTreatyTypeGLCode6() {
        return stTreatyTypeGLCode6;
    }

    /**
     * @param stTreatyTypeGLCode6 the stTreatyTypeGLCode6 to set
     */
    public void setStTreatyTypeGLCode6(String stTreatyTypeGLCode6) {
        this.stTreatyTypeGLCode6 = stTreatyTypeGLCode6;
    }
}
