/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceClausulesView
 * Author:  Denny Mahendra
 * Created: Nov 2, 2005 8:35:28 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.webfin.gl.model.GLCostCenterView;

import java.math.BigDecimal;
import java.util.Date;

public class InsuranceClausulesView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ins_clausules
(
  ins_clause_id int8 NOT NULL,
  description varchar(128),
  active_flag varchar(1),
  rate numeric,
  rate_type varchar(5),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_clausules_pk PRIMARY KEY (ins_clause_id)
)
   */

   public static String tableName = "ins_clausules";

   public static String comboFields[] = {"ins_clause_id","shortdesc"};

   public static String fieldMap[][] = {
      {"stInsuranceClauseID","ins_clause_id*pk"},
      {"stDescription","description"},
      {"stActiveFlag","active_flag"},
      {"dbRate","rate"},
      {"stRateType","rate_type"},
      {"stLevel","clause_level"},
      {"stExclusionFlag","exclusion_flag"},
      {"stDefaultFlag","f_default"},
      {"stShortDescription","shortdesc"},
      {"stPolicyTypeID","pol_type_id"},
      {"stDescriptionNew","description_new"},
      {"stCostCenterCode","cc_code"},
      {"stReference1","ref1"},
      {"stChildClausules","child_clausules"},
      {"stCostCenter","cost_center*n"},
      {"dtCreateDate","create_date*n"},
      {"stCreateWho","create_who*n"},
      {"stEntryUserName","entry_user_name*n"},
      
   };

   private String stInsuranceClauseID;
   private String stDescription;
   private String stActiveFlag;
   private BigDecimal dbRate;
   private String stRateType;
   private String stLevel;
   private String stExclusionFlag;
   private String stDefaultFlag;
   private String stShortDescription;
   private String stPolicyTypeID;
   private String stDescriptionNew;
   private String stCostCenterCode;
   private String stReference1;
   private String stChildClausules;
   private String stCostCenter;
   private Date dtCreateDate;
   private String stCreateWho;
   private String stEntryUserName;


   @Override
    public String getStCreateWho() {
        return stCreateWho;
    }

    @Override
    public void setStCreateWho(String stCreateWho) {
        this.stCreateWho = stCreateWho;
    }

    @Override
    public Date getDtCreateDate() {
        return dtCreateDate;
    }

    @Override
    public void setDtCreateDate(Date dtCreateDate) {
        this.dtCreateDate = dtCreateDate;
    }
   

    public String getStEntryUserName() {
        return stEntryUserName;
    }

    public void setStEntryUserName(String stEntryUserName) {
        this.stEntryUserName = stEntryUserName;
    }

   public String getStInsuranceClauseID() {
      return stInsuranceClauseID;
   }

   public void setStInsuranceClauseID(String stInsuranceClauseID) {
      this.stInsuranceClauseID = stInsuranceClauseID;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public String getStActiveFlag() {
      return stActiveFlag;
   }

   public void setStActiveFlag(String stActiveFlag) {
      this.stActiveFlag = stActiveFlag;
   }

   public BigDecimal getDbRate() {
      return dbRate;
   }

   public void setDbRate(BigDecimal dbRate) {
      this.dbRate = dbRate;
   }

   public String getStRateType() {
      return stRateType;
   }

   public void setStRateType(String stRateType) {
      this.stRateType = stRateType;
   }

   public void setStLevel(String stLevel) {
      this.stLevel = stLevel;
   }

   public String getStLevel() {
      return stLevel;
   }

   public String getStExclusionFlag() {
      return stExclusionFlag;
   }

   public void setStExclusionFlag(String stExclusionFlag) {
      this.stExclusionFlag = stExclusionFlag;
   }

   public String getStDefaultFlag() {
      return stDefaultFlag;
   }

   public void setStDefaultFlag(String stDefaultFlag) {
      this.stDefaultFlag = stDefaultFlag;
   }

    public String getStShortDescription()
    {
        return stShortDescription;
    }

    public void setStShortDescription(String stShortDescription)
    {
        this.stShortDescription = stShortDescription;
    }

    public String getStPolicyTypeID()
    {
        return stPolicyTypeID;
    }

    public void setStPolicyTypeID(String stPolicyTypeID)
    {
        this.stPolicyTypeID = stPolicyTypeID;
    }
    
    public String getStDescriptionNew() {
        return stDescriptionNew;
    }

    public void setStDescriptionNew(String stDescriptionNew) {
        this.stDescriptionNew = stDescriptionNew;
    }
    
    public String getStCostCenterCode() {
        return stCostCenterCode;
    }

    public void setStCostCenterCode(String stCostCenterCode) {
        this.stCostCenterCode = stCostCenterCode;
    }

    public String getStReference1() {
        return stReference1;
    }

    public void setStReference1(String stReference1) {
        this.stReference1 = stReference1;
    }

    /**
     * @return the stChildClausules
     */
    public String getStChildClausules() {
        return stChildClausules;
    }

    /**
     * @param stChildClausules the stChildClausules to set
     */
    public void setStChildClausules(String stChildClausules) {
        this.stChildClausules = stChildClausules;
    }

    public DTOList getCabang() {
        try {
                return ListUtil.getDTOListFromQuery(
                        "select * from gl_cost_center order by cc_code",
                        GLCostCenterView.class
                        );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    private GLCostCenterView getCostCenter() {
         return (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, stCostCenterCode);
    }

    public InsurancePolicyTypeView getPolicyType() {
        return (InsurancePolicyTypeView) DTOPool.getInstance().getDTO(InsurancePolicyTypeView.class, stPolicyTypeID);
    }

    /**
     * @return the stCostCenter
     */
    public String getStCostCenter() {
        return stCostCenter;
    }

    /**
     * @param stCostCenter the stCostCenter to set
     */
    public void setStCostCenter(String stCostCenter) {
        this.stCostCenter = stCostCenter;
    }
    
}
