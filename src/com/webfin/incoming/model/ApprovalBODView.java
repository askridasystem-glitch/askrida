/***********************************************************************
 * Module:  com.webfin.entity.model.EntityView
 * Author:  Denny Mahendra
 * Created: Oct 29, 2005 7:15:29 AM
 * Purpose: 
 ***********************************************************************/
package com.webfin.incoming.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.login.model.UserSessionView;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.outcoming.model.UploadBODDistributionView;

import java.util.Date;

public class ApprovalBODView extends DTO implements RecordAudit {


    /*
    CREATE TABLE incoming_letter
    (
    ref_no character varying(32) NOT NULL,
    sender character varying(100),
    letter_date timestamp without time zone,
    subject character varying(80),
    create_who character varying(32) NOT NULL,
    create_date timestamp without time zone NOT NULL,
    change_who character varying(32),
    change_date timestamp without time zone,
    note character varying(200),
    CONSTRAINT pk_incoming_letter PRIMARY KEY (ref_no)
    )

     */
    public static String tableName = "approvalbod_letter";
    public static String comboFields[] = {"in_id", "sender", "receiver"};
    public static String fieldMap[][] = {
        {"stInID", "in_id*pk"},
        {"stRefNo", "ref_no"},
        {"stSender", "sender"},
        {"dtLetterDate", "letter_date"},
        {"stSubject", "subject"},
        {"stNote", "note"},
        {"stReceiver", "receiver"},
        {"stFilePhysic", "file_physic"},
        {"stReadFlag", "read_flag"},
        {"stDeleteFlag", "delete_flag"},
        {"stSenderName", "sender_name"},
        {"stCC", "cc"},
        {"stUnreadMail", "unread_mail*n"},
        {"stTotalMail", "total_mail*n"},
        {"stJam", "jam*n"},
        {"stOutID", "out_id"},
        {"stApprovalType", "approval_type"},
        {"stReplyNote", "replynote"},
        {"stPolicyID", "pol_id"},
        {"stApproveFlag", "approve_flag"},
        {"stLetterNo", "letter_no"},
        {"stCCID", "cc_id"},
        {"stReceiverName", "receiver_name"},
        {"stPrintFlag", "print_flag"},};

    private String stRefNo;
    private String stSender;
    private String stSubject;
    private String stNote;
    private Date dtLetterDate;
    private String stReceiver;
    private String stInID;
    private String stFilePhysic;
    private String stReadFlag;
    private String stUnreadMail;
    private String stDeleteFlag;
    private String stSenderName;
    private String stTotalMail;
    private String stCC;
    private String stJam;
    //private DTOList distributions;
    private ApprovalBODDistributionView primaryAddress;
    private DTOList documents;
    private String stOutID;
    private String stApprovalType;
    private String stReplyNote;
    private String stPolicyID;
    private String stApproveFlag;
    private String stLetterNo;
    private String stCCID;
    private String stReceiverName;
    private DTOList uploadDist;
    private String stPrintFlag;

    public String getStTotalMail() {
        return stTotalMail;
    }

    public void setStTotalMail(String stTotalMail) {
        this.stTotalMail = stTotalMail;
    }

    public String getStSenderName() {
        return stSenderName;
    }

    public void setStSenderName(String stSenderName) {
        this.stSenderName = stSenderName;
    }

    public String getStUnreadMail() {
        return stUnreadMail;
    }

    public void setStUnreadMail(String stUnreadMail) {
        this.stUnreadMail = stUnreadMail;
    }

    public String getStFilePhysic() {
        return stFilePhysic;
    }

    public void setStFilePhysic(String stFilePhysic) {
        this.stFilePhysic = stFilePhysic;
    }

    public String getStReadFlag() {
        return stReadFlag;
    }

    public void setStReadFlag(String stReadFlag) {
        this.stReadFlag = stReadFlag;
    }

    public String getStDeleteFlag() {
        return stDeleteFlag;
    }

    public void setStDeleteFlag(String stDeleteFlag) {
        this.stDeleteFlag = stDeleteFlag;
    }

    public String getStInID() {
        return stInID;
    }

    public void setStInID(String stInID) {
        this.stInID = stInID;
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

//    public DTOList getDistributions() {
//        loadDistributions();
//        return distributions;
//    }
//
//    private void loadDistributions() {
//        try {
//            if (distributions == null) {
//                distributions = ListUtil.getDTOListFromQuery(
//                        "select * from approvalbod_dist where in_id = ? and delete_flag is null",
//                        new Object[]{stInID},
//                        ApprovalBODDistributionView.class);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void setDistributions(DTOList distributions) {
//        this.distributions = distributions;
//    }

    public ApprovalBODDistributionView getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(ApprovalBODDistributionView primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public DTOList getDocuments() {
        loadDocuments();
        return documents;
    }

    private void loadDocuments() {
        try {
            if (documents == null) {
                documents = ListUtil.getDTOListFromQuery(
                        "select * from approvalbod_documents where in_id = ?",
                        new Object[]{stInID},
                        ApprovalBODDocumentsView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setDocuments(DTOList documents) {
        this.documents = documents;
    }

    public String getStCC() {
        return stCC;
    }

    public void setStCC(String stCC) {
        this.stCC = stCC;
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

    /**
     * @return the stOutID
     */
    public String getStOutID() {
        return stOutID;
    }

    /**
     * @param stOutID the stOutID to set
     */
    public void setStOutID(String stOutID) {
        this.stOutID = stOutID;
    }

    /**
     * @return the stApprovalType
     */
    public String getStApprovalType() {
        return stApprovalType;
    }

    /**
     * @param stApprovalType the stApprovalType to set
     */
    public void setStApprovalType(String stApprovalType) {
        this.stApprovalType = stApprovalType;
    }

    /**
     * @return the stReplyNote
     */
    public String getStReplyNote() {
        return stReplyNote;
    }

    /**
     * @param stReplyNote the stReplyNote to set
     */
    public void setStReplyNote(String stReplyNote) {
        this.stReplyNote = stReplyNote;
    }

    public String getStPolicyID() {
        return stPolicyID;
    }

    public void setStPolicyID(String stPolicyID) {
        this.stPolicyID = stPolicyID;
    }

    /**
     * @return the stApproveFlag
     */
    public String getStApproveFlag() {
        return stApproveFlag;
    }

    /**
     * @param stApproveFlag the stApproveFlag to set
     */
    public void setStApproveFlag(String stApproveFlag) {
        this.stApproveFlag = stApproveFlag;
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
     * @return the stCCID
     */
    public String getStCCID() {
        return stCCID;
    }

    /**
     * @param stCCID the stCCID to set
     */
    public void setStCCID(String stCCID) {
        this.stCCID = stCCID;
    }

    /**
     * @return the stReceiverName
     */
    public String getStReceiverName() {
        return stReceiverName;
    }

    /**
     * @param stReceiverName the stReceiverName to set
     */
    public void setStReceiverName(String stReceiverName) {
        this.stReceiverName = stReceiverName;
    }

    /**
     * @return the uploadDist
     */
    public DTOList getUploadDist() {
        loadUploadDist();
        return uploadDist;
    }

    private void loadUploadDist() {
        try {
            if (uploadDist == null) {
                uploadDist = ListUtil.getDTOListFromQuery(
                        "select * from uploadbod_dist where out_id = ? and delete_flag is null",
                        new Object[]{stOutID},
                        UploadBODDistributionView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param uploadDist the uploadDist to set
     */
    public void setUploadDist(DTOList uploadDist) {
        this.uploadDist = uploadDist;
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
}