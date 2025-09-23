/***********************************************************************
 * Module:  com.webfin.gl.model.UploadHeaderView
 * Author:  Denny Mahendra
 * Created: Jul 24, 2005 8:48:57 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.DateUtil;
import com.crux.util.IDFactory;
import com.crux.util.StringTools;
import com.crux.util.Tools;
import com.webfin.system.region.model.RegionView;
import java.math.BigDecimal;
import java.util.Date;

public class PolicyApprovalHeaderView  extends DataTeksMasukView{

    /*
     *
     * CREATE TABLE ins_policy_approval_upload_header
(
  ins_policy_approval_upload_id bigint NOT NULL,
  file_id character varying(255),
  recap_no character varying(255),
  tsi_total numeric,
  premi_total numeric,
  data_amount bigint,
  cc_code character varying(32),
  file_physic bigint,
  create_date timestamp without time zone,
  create_who character varying(32),
  change_date timestamp without time zone,
  change_who character varying(32),
  effective_flag character varying(1),
  region_id bigint,
  description text,
  posted_flag character varying(1),
  status character varying(64),
  CONSTRAINT ins_policy_approval_upload_header_pkey PRIMARY KEY (ins_policy_approval_upload_id)
)
     */

    public static String tableName = "ins_policy_approval_upload_header";

    public transient static String fieldMap[][] = {
        {"stInsurancePolicyApprovalUploadID", "ins_policy_approval_upload_id*pk*nd"},
        {"stCostCenterCode", "cc_code"},
        {"stRegionID", "region_id"},
        {"stFilePhysic", "file_physic"},
        {"stEffectiveFlag", "effective_flag"},
        {"dbTSITotal", "tsi_total"},
        {"dbPremiTotal", "premi_total"},
        {"stRecapNo", "recap_no"},
        {"stCreateName", "create_name*n"},
        {"stJumlahData", "jumlah_data*n"},
        {"stJumlahDataProses", "jumlah_data_proses*n"},
        {"stDescription", "description"},
        {"stPostedFlag", "posted_flag"},
        {"stStatus", "status"},
        {"dtApproveDate", "approved_date"},
        {"stApprovedWho", "approved_who"},
        {"stApprovedName", "approved_name*n"},


    };

   private DTOList details;
   private boolean approvedMode;
   private String stInsurancePolicyApprovalUploadID;
   private String stCostCenterCode;
   private String stEffectiveFlag;
   private String stRegionID;
   private BigDecimal dbTSITotal;
   private BigDecimal dbPremiTotal;
   private String stRecapNo;
   private String stCreateName;
   private String stJumlahData;
   private String stJumlahDataProses;

   private String stFilePhysic;
   private String stDescription;
   private String stPostedFlag;
   private String stStatus;
   private Date dtApproveDate;
   private String stApprovedWho;
   private String stApprovedName;

    public String getStApprovedName() {
        return stApprovedName;
    }

    public void setStApprovedName(String stApprovedName) {
        this.stApprovedName = stApprovedName;
    }

    public Date getDtApproveDate() {
        return dtApproveDate;
    }

    public void setDtApproveDate(Date dtApproveDate) {
        this.dtApproveDate = dtApproveDate;
    }

    public String getStApprovedWho() {
        return stApprovedWho;
    }

    public void setStApprovedWho(String stApprovedWho) {
        this.stApprovedWho = stApprovedWho;
    }

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

    public String getStInsurancePolicyApprovalUploadID() {
        return stInsurancePolicyApprovalUploadID;
    }

    public void setStInsurancePolicyApprovalUploadID(String stInsurancePolicyApprovalUploadID) {
        this.stInsurancePolicyApprovalUploadID = stInsurancePolicyApprovalUploadID;
    }

    /**
     * @return the stCostCenterCode
     */
    public String getStCostCenterCode() {
        return stCostCenterCode;
    }

    /**
     * @param stCostCenterCode the stCostCenterCode to set
     */
    public void setStCostCenterCode(String stCostCenterCode) {
        this.stCostCenterCode = stCostCenterCode;
    }

    /**
     * @return the stEffectiveFlag
     */
    public String getStEffectiveFlag() {
        return stEffectiveFlag;
    }

    /**
     * @param stEffectiveFlag the stEffectiveFlag to set
     */
    public void setStEffectiveFlag(String stEffectiveFlag) {
        this.stEffectiveFlag = stEffectiveFlag;
    }

    /**
     * @return the stRegionID
     */
    public String getStRegionID() {
        return stRegionID;
    }

    /**
     * @param stRegionID the stRegionID to set
     */
    public void setStRegionID(String stRegionID) {
        this.stRegionID = stRegionID;
    }

    /**
     * @return the dbTSITotal
     */
    public BigDecimal getDbTSITotal() {
        return dbTSITotal;
    }

    /**
     * @param dbTSITotal the dbTSITotal to set
     */
    public void setDbTSITotal(BigDecimal dbTSITotal) {
        this.dbTSITotal = dbTSITotal;
    }

    /**
     * @return the dbPremiTotal
     */
    public BigDecimal getDbPremiTotal() {
        return dbPremiTotal;
    }

    /**
     * @param dbPremiTotal the dbPremiTotal to set
     */
    public void setDbPremiTotal(BigDecimal dbPremiTotal) {
        this.dbPremiTotal = dbPremiTotal;
    }

    /**
     * @return the stRecapNo
     */
    public String getStRecapNo() {
        return stRecapNo;
    }

    /**
     * @param stRecapNo the stRecapNo to set
     */
    public void setStRecapNo(String stRecapNo) {
        this.stRecapNo = stRecapNo;
    }

    public void generateRecapNo()throws Exception {

        //uploadBayarDetailView data = (uploadBayarDetailView) details.get(0);

        //final String policyType2Digit = StringTools.leftPad(data.getStPolicyTypeID(), '0', 2);

        String year2Digit = DateUtil.getYear2Digit(new Date());
        String month2Digit = DateUtil.getMonth2Digit(new Date());
        String counterKey = month2Digit + year2Digit;

        final RegionView reg = getRegion();

        if (reg == null) throw new RuntimeException("Unable to get region (code=" + getStRegionID() + ")");

        final String ccCode = getDigit(reg.getStCostCenterCode(), 2);
        final String regCode = getDigit(reg.getStRegionCode(), 2);

       String orderNo = StringTools.leftPad(String.valueOf(IDFactory.createNumericID("UPLAPRV"  + month2Digit + year2Digit + ccCode, 1)), '0', 4);


        stRecapNo =
                "APRV-" + // C
                ccCode + // D
                regCode + // E
                counterKey + // Fg
                orderNo
                ;
    }

    public RegionView getRegion() {
        final RegionView reg = (RegionView) DTOPool.getInstance().getDTO(RegionView.class, stRegionID);

        return reg;
    }

    private String getDigit(String code, int i) {
        if ((code == null) || (code.length() < 1)) code = "";

        code = code + "000000000000000000";

        code = code.substring(0, i);

        return code;
    }

    public boolean isEffective() {
        return Tools.isYes(stEffectiveFlag);
    }

    /**
     * @return the stCreateName
     */
    public String getStCreateName() {
        return stCreateName;
    }

    /**
     * @param stCreateName the stCreateName to set
     */
    public void setStCreateName(String stCreateName) {
        this.stCreateName = stCreateName;
    }

    /**
     * @return the stJumlahData
     */
    public String getStJumlahData() {
        return stJumlahData;
    }

    /**
     * @param stJumlahData the stJumlahData to set
     */
    public void setStJumlahData(String stJumlahData) {
        this.stJumlahData = stJumlahData;
    }

    /**
     * @return the stJumlahDataProses
     */
    public String getStJumlahDataProses() {
        return stJumlahDataProses;
    }

    /**
     * @param stJumlahDataProses the stJumlahDataProses to set
     */
    public void setStJumlahDataProses(String stJumlahDataProses) {
        this.stJumlahDataProses = stJumlahDataProses;
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

    public boolean isPosted() {
        return Tools.isYes(stPostedFlag);
    }

    /**
     * @return the stPostedFlag
     */
    public String getStPostedFlag() {
        return stPostedFlag;
    }

    /**
     * @param stPostedFlag the stPostedFlag to set
     */
    public void setStPostedFlag(String stPostedFlag) {
        this.stPostedFlag = stPostedFlag;
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

}
