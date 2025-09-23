/***********************************************************************
 * Module:  com.webfin.insurance.model.InsurancePolicyTypeView
 * Author:  Denny Mahendra
 * Created: Oct 25, 2005 9:54:40 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;
import com.crux.ff.model.FlexFieldHeaderView;
import com.crux.util.Tools;
import com.webfin.FinCodec;
import com.webfin.insurance.custom.CustomHandler;
import com.webfin.insurance.custom.CustomHandlerManager;
import java.util.Date;
import java.util.HashMap;

public class InsurancePolicyTypeView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ins_policy_types
(
 pol_type_id int8 NOT NULL,
 description varchar(255),
 create_date timestamp NOT NULL,
 create_who varchar(32) NOT NULL,
 change_date timestamp,
 change_who varchar(32),
 gl_comission varchar(32),
 gl_comission2 varchar(32),
 gl_ar varchar(32),
 gl_ar2 varchar(32),
 gl_rev varchar(32),
 gl_rev2 varchar(32),
 CONSTRAINT ins_policy_types_pk PRIMARY KEY (pol_type_id)
)
   */

   public static String tableName = "ins_policy_types";

   public static String comboFields[] = {"pol_type_id","description"};

   public static String fieldMap[][] = {
      {"stPolicyTypeID", "pol_type_id*pk"},
      {"stDescription", "description"},
      {"stGLComission", "gl_comission"},
      {"stGLComission2", "gl_comission2"},
      {"stGLAR", "gl_ar"},
      {"stGLAR2", "gl_ar2"},
      {"stGLRevenue", "gl_rev"},
      {"stGLRevenue2", "gl_rev2"},
      {"stGLAP", "gl_ap"},
      {"stPolicyTypeCode", "poltype_code"},
      {"stGLCode", "gl_code"},
      {"stGroupID", "ins_policy_type_grp_id"},
      {"stWording1", "wording1"},
      {"stCustomHandler", "custom_handler"},
      {"stShortDescription", "short_desc"},
      {"stInsuranceCoverSourceID", "ins_cover_source_id"},
      {"stRateMethod", "rate_method"},
      {"stOldPolicyTypeID", "old_pol_type_id"},
      {"stGroupID2", "ins_policy_type_grp_id2"},
      {"stEntityMasterLabel", "ent_master_label"},
      {"stEntityMasterDescriptionLabel","ent_master_desc_label"},
      {"stDefaultPeriodFactor", "default_period_factor"},
      {"stDescription2", "description2"},
      {"stControlFlags", "control_flags"},
      {"stWordingPath", "wording_path"},
      {"dtReference1", "refd1"},
      {"stUploadFlag", "upload_flag"},
      {"stEndorseRIFlag", "endorse_ri_block"},
      {"stBusinessTypeID","business_type_id"},
      {"stMultiYearsBlockFlag", "multiyears_block_f"},
      {"stGroupName", "group_name*n"},


   };

   private String stPolicyTypeCode;
   private String stPolicyTypeID;
   private String stDescription;
   private String stShortDescription;
   private String stGLComission;
   private String stGLComission2;
   private String stGLAR;
   private String stGLAR2;
   private String stGLRevenue;
   private String stGLRevenue2;
   private String stGLAP;
   private String stGLCode;
   private String stGroupID;
   private String stWording1;
   private String stCustomHandler;
   private String stInsuranceCoverSourceID;
   private String stRateMethod;
   private String stOldPolicyTypeID;
   private String stEntityMasterLabel;
   private String stEntityMasterDescriptionLabel;
   private String stDefaultPeriodFactor;
   
   private String stGroupID2;

   private String stDescription2;
   private String stControlFlags;
   private String stWordingPath;
   private Date dtReference1;
   private String stUploadFlag;
   private String stEndorseRIFlag;
   private String stBusinessTypeID;
   private String stMultiYearsBlockFlag;
   private String stGroupName;

    public String getStGroupName() {
        return stGroupName;
    }

    public void setStGroupName(String stGroupName) {
        this.stGroupName = stGroupName;
    }

    public String getStMultiYearsBlockFlag() {
        return stMultiYearsBlockFlag;
    }

    public void setStMultiYearsBlockFlag(String stMultiYearsBlockFlag) {
        this.stMultiYearsBlockFlag = stMultiYearsBlockFlag;
    }

    public String getStBusinessTypeID() {
        return stBusinessTypeID;
    }

    public void setStBusinessTypeID(String stBusinessTypeID) {
        this.stBusinessTypeID = stBusinessTypeID;
    }

   public String getStInsuranceCoverSourceID() {
      return stInsuranceCoverSourceID;
   }

   public void setStInsuranceCoverSourceID(String stInsuranceCoverSourceID) {
      this.stInsuranceCoverSourceID = stInsuranceCoverSourceID;
   }

   public String getStShortDescription() {
      return stShortDescription;
   }

   public void setStShortDescription(String stShortDescription) {
      this.stShortDescription = stShortDescription;
   }

   public CustomHandler getHandler(){
      return CustomHandlerManager.getInstance().getCustomHandler(stCustomHandler);
   }

   public String getStCustomHandler() {
      return stCustomHandler;
   }

   public void setStCustomHandler(String stCustomHandler) {
      this.stCustomHandler = stCustomHandler;
   }

   public String getStGLAP() {
      return stGLAP;
   }

   public void setStGLAP(String stGLAP) {
      this.stGLAP = stGLAP;
   }

   public String getStPolicyTypeID() {
      return stPolicyTypeID;
   }

   public void setStPolicyTypeID(String stPolicyTypeID) {
      this.stPolicyTypeID = stPolicyTypeID;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public String getStGLComission() {
      return stGLComission;
   }

   public void setStGLComission(String stGLComission) {
      this.stGLComission = stGLComission;
   }

   public String getStGLComission2() {
      return stGLComission2;
   }

   public void setStGLComission2(String stGLComission2) {
      this.stGLComission2 = stGLComission2;
   }

   public String getStGLAR() {
      return stGLAR;
   }

   public void setStGLAR(String stGLAR) {
      this.stGLAR = stGLAR;
   }

   public String getStGLAR2() {
      return stGLAR2;
   }

   public void setStGLAR2(String stGLAR2) {
      this.stGLAR2 = stGLAR2;
   }

   public String getStGLRevenue() {
      return stGLRevenue;
   }

   public void setStGLRevenue(String stGLRevenue) {
      this.stGLRevenue = stGLRevenue;
   }

   public String getStGLRevenue2() {
      return stGLRevenue2;
   }

   public void setStGLRevenue2(String stGLRevenue2) {
      this.stGLRevenue2 = stGLRevenue2;
   }


   public void setStPolicyTypeCode(String stPolicyTypeCode) {
      this.stPolicyTypeCode = stPolicyTypeCode;
   }

   public String getStPolicyTypeCode() {
      return stPolicyTypeCode;
   }

   public void setStGLCode(String stGLCode) {
      this.stGLCode = stGLCode;
   }

   public String getStGLCode() {
      return stGLCode;
   }

   public String getStGroupID() {
      return stGroupID;
   }

   public void setStGroupID(String stGroupID) {
      this.stGroupID = stGroupID;
   }

   public Class getClObjectClass() {

      final boolean isObjectMap =
              getStPolicyTypeCode() != null &&
              getStPolicyTypeCode().indexOf("OM_") == 0;

      if (isObjectMap)
         return InsurancePolicyObjDefaultView.class;

      return (Class) FinCodec.PolicyTypeCodeMap.getLookUp().getValue(getStPolicyTypeCode());
   }

   public FlexFieldHeaderView getObjectMap() {

      final String stObjectMapID = getStObjectMapID();

      if (stObjectMapID != null)
         return (FlexFieldHeaderView) DTOPool.getInstance().getDTO(FlexFieldHeaderView.class, stObjectMapID);

      return null;
   }

   public String getStObjectMapID() {

      if (stPolicyTypeCode != null)
         if (stPolicyTypeCode.indexOf("OM_") == 0)
            return stPolicyTypeCode;

      return null;
   }

   public String getStWording1() {
      return stWording1;
   }

   public void setStWording1(String stWording1) {
      this.stWording1 = stWording1;
   }
   
   public String getStRateMethod() {
      return stRateMethod;
   }

   public void setStRateMethod(String stRateMethod) {
      this.stRateMethod = stRateMethod;
   }
   
    public String getStOldPolicyTypeID() {
      return stOldPolicyTypeID;
   }

   public void setStOldPolicyTypeID(String stOldPolicyTypeID) {
      this.stOldPolicyTypeID = stOldPolicyTypeID;
   }

    public String getStGroupID2() {
        return stGroupID2;
    }

    public void setStGroupID2(String stGroupID2) {
        this.stGroupID2 = stGroupID2;
    } 
    
    public InsurancePolicyTypeGroupView getPolicyTypeGroup() {

        final InsurancePolicyTypeGroupView group = (InsurancePolicyTypeGroupView) DTOPool.getInstance().getDTO(InsurancePolicyTypeGroupView.class, stGroupID);

        return group;

    }

    public String getStEntityMasterLabel() {
        //return stEntityMasterLabel!=null?stEntityMasterLabel:"{L-ENGBussiness Source-L}{L-INASumber Bisnis-L}";
        return stEntityMasterLabel!=null?stEntityMasterLabel:"{L-ENGBussiness Source-L}{L-INATertanggung-L}";
    }

    public void setStEntityMasterLabel(String stEntityMasterLabel) {
        this.stEntityMasterLabel = stEntityMasterLabel;
    }

    public String getStEntityMasterDescriptionLabel() {
        return stEntityMasterDescriptionLabel!=null?stEntityMasterDescriptionLabel:"{L-ENGBussiness Source-L}{L-INATertanggung-L}";
    }

    public void setStEntityMasterDescriptionLabel(String stEntityMasterDescriptionLabel) {
        this.stEntityMasterDescriptionLabel = stEntityMasterDescriptionLabel;
    }

    public String getStDefaultPeriodFactor() {
        return stDefaultPeriodFactor;
    }

    public void setStDefaultPeriodFactor(String stDefaultPeriodFactor) {
        this.stDefaultPeriodFactor = stDefaultPeriodFactor;
    }
    
    public boolean isDefaultPeriod(){
        return Tools.isYes(stDefaultPeriodFactor);
    }

    public String getStDescription2() {
        return stDescription2;
    }

    public void setStDescription2(String stDescription2) {
        this.stDescription2 = stDescription2;
    }

    /**
     * @return the stControlFlags
     */
    public String getStControlFlags() {
        return stControlFlags;
    }

    /**
     * @param stControlFlags the stControlFlags to set
     */
    public void setStControlFlags(String stControlFlags) {
        this.stControlFlags = stControlFlags;
    }

    private HashMap propMap;

    public HashMap getPropMap() throws Exception{
      if (propMap==null)
         propMap = Tools.getPropMap(stControlFlags);
      return propMap;
   }

   public boolean checkProperty(String key, String value) throws Exception{
      return Tools.isEqual(value,(Comparable) getPropMap().get(key));
   }

    /**
     * @return the stWordingPath
     */
    public String getStWordingPath() {
        return stWordingPath;
    }

    /**
     * @param stWordingPath the stWordingPath to set
     */
    public void setStWordingPath(String stWordingPath) {
        this.stWordingPath = stWordingPath;
    }

    /**
     * @return the dtReference1
     */
    public Date getDtReference1() {
        return dtReference1;
    }

    /**
     * @param dtReference1 the dtReference1 to set
     */
    public void setDtReference1(Date dtReference1) {
        this.dtReference1 = dtReference1;
    }

    /**
     * @return the stUploadFlag
     */
    public String getStUploadFlag() {
        return stUploadFlag;
    }

    /**
     * @param stUploadFlag the stUploadFlag to set
     */
    public void setStUploadFlag(String stUploadFlag) {
        this.stUploadFlag = stUploadFlag;
    }

    public boolean canUpload(){
        return Tools.isYes(stUploadFlag);
    }

    /**
     * @return the stEndorseRIFlag
     */
    public String getStEndorseRIFlag() {
        return stEndorseRIFlag;
    }

    /**
     * @param stEndorseRIFlag the stEndorseRIFlag to set
     */
    public void setStEndorseRIFlag(String stEndorseRIFlag) {
        this.stEndorseRIFlag = stEndorseRIFlag;
    }

    public boolean isRIBlockEndorse(){
        return Tools.isYes(stEndorseRIFlag);
    }

    public Class getSplitClObjectClass() {

      final boolean isObjectMap =
              getStPolicyTypeCode() != null &&
              getStPolicyTypeCode().indexOf("OM_") == 0;

      if (isObjectMap)
         return InsuranceSplitPolicyObjDefaultView.class;

      return (Class) FinCodec.PolicyTypeCodeMap.getLookUp().getValue(getStPolicyTypeCode());
   }

    public boolean isValidateDeductible() throws Exception {

        boolean validateDeduct = false;

        if(getStControlFlags()!=null)
               validateDeduct = checkProperty("VAL_DEDUCT","Y");

        return validateDeduct;

    }

    public boolean isMultiyearsBlock(){
        return Tools.isYes(stMultiYearsBlockFlag);
    }
      
}
