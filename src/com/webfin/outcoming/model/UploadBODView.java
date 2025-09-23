/***********************************************************************
 * Module:  com.webfin.outcoming.model.UploadBODView
 * Author:  Denny Mahendra
 * Created: Oct 29, 2005 7:15:29 AM
 * Purpose: 
 ***********************************************************************/
package com.webfin.outcoming.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.login.model.UserSessionView;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.webfin.incoming.model.ApprovalBODView;
import com.webfin.insurance.model.InsurancePolicyView;

import java.util.Date;

public class UploadBODView extends DTO implements RecordAudit {

    public static String tableName = "uploadbod_letter";
    public static String comboFields[] = {"out_id", "sender", "receiver"};
    public static String fieldMap[][] = {
        {"stOutID", "out_id*pk"},
        {"stRefNo", "ref_no"},
        {"stSender", "sender"},
        {"dtLetterDate", "letter_date"},
        {"stSubject", "subject"},
        {"stNote", "note"},
        {"stReceiver", "receiver"},
        {"stDeleteFlag", "delete_flag"},
        {"stFilePhysic", "file_physic"},
        {"stJam", "jam*n"},
        {"stPolicyID", "pol_id"},
        {"stLetterNo", "letter_no"},
        {"stPrintFlag", "print_flag"},
        {"stStatusFlag", "status_flag"},};

    private String stRefNo;
    private String stSender;
    private String stSubject;
    private String stNote;
    private Date dtLetterDate;
    private String stReceiver;
    private String stOutID;
    private String stFilePhysic;
    private String stDeleteFlag;
    private String stJam;
    private OutcomingDistributionView primaryAddress;
    private DTOList distributions;
    private DTOList documents;
    private DTOList approvalbod;
    private String stPolicyID;
    private String stLetterNo;
    private String stPrintFlag;
    private String stStatusFlag;

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

    public String getStOutID() {
        return stOutID;
    }

    public void setStOutID(String stOutID) {
        this.stOutID = stOutID;
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

    public DTOList getDistributions() {
        loadDistributions();
        return distributions;
    }

    private void loadDistributions() {
        try {
            if (distributions == null) {
                distributions = ListUtil.getDTOListFromQuery(
                        "select * from uploadbod_dist where out_id = ? and delete_flag is null",
                        new Object[]{stOutID},
                        UploadBODDistributionView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setDistributions(DTOList distributions) {
        this.distributions = distributions;
    }

    public DTOList getDocuments() {
        loadDocuments();
        return documents;
    }

    private void loadDocuments() {
        try {
            if (documents == null) {
                documents = ListUtil.getDTOListFromQuery(
                        "select * from uploadbod_documents where out_id = ?",
                        new Object[]{stOutID},
                        UploadBODDocumentsView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setDocuments(DTOList documents) {
        this.documents = documents;
    }

//   public String getStCostCenterCode() {
//      return stCostCenterCode;
//   }
//
//   public void setStCostCenterCode(String stCostCenterCode) {
//      this.stCostCenterCode = stCostCenterCode;
//   }
    public OutcomingDistributionView getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(OutcomingDistributionView primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    /**
     * @return the stJam
     */
    public String getStJam() {
        return stJam;
    }

    /**
     * @param stJam the stJam to set
     */
    public void setStJam(String stJam) {
        this.stJam = stJam;
    }

    public String getStPolicyID() {
        return stPolicyID;
    }

    public void setStPolicyID(String stPolicyID) {
        this.stPolicyID = stPolicyID;
    }

    /**
     * @return the stLetterNo
     */
    public String getStLetterNo() {
        return stLetterNo;
    }

    /**
     * @param stLetterNo the stLetterNo to set
     */
    public void setStLetterNo(String stLetterNo) {
        this.stLetterNo = stLetterNo;
    }

    public UserSessionView getUser(String stUserID) {
        return (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, stUserID);
    }

    public InsurancePolicyView getPolicy(String stPolicyID) {
        return (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, stPolicyID);
    }

    /**
     * @return the stPrintFlag
     */
    public String getStPrintFlag() {
        return stPrintFlag;
    }

    /**
     * @param stPrintFlag the stPrintFlag to set
     */
    public void setStPrintFlag(String stPrintFlag) {
        this.stPrintFlag = stPrintFlag;
    }

    /**
     * @return the stStatusFlag
     */
    public String getStStatusFlag() {
        return stStatusFlag;
    }

    /**
     * @param stStatusFlag the stStatusFlag to set
     */
    public void setStStatusFlag(String stStatusFlag) {
        this.stStatusFlag = stStatusFlag;
    }
}