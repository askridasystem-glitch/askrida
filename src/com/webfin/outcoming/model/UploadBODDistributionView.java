/***********************************************************************
 * Module:  com.webfin.outcoming.model.UploadBODDistributionView
 * Author:  Denny Mahendra
 * Created: Nov 8, 2005 2:03:05 PM
 * Purpose: 
 ***********************************************************************/
package com.webfin.outcoming.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.login.model.UserSessionView;
import com.crux.pool.DTOPool;

public class UploadBODDistributionView extends DTO implements RecordAudit {
    
    public static String tableName = "uploadbod_dist";
    public static String fieldMap[][] = {
        {"stOutID", "out_id"},
        {"stIdDist", "id_dist*pk"},
        {"stReceiver", "receiver"},
        {"stDeletedFlag", "delete_flag"},
        {"stApprovalType", "approval_type"},
        {"stApprovalFlag", "approval_flag"},
        {"stReplyNote", "replynote"},
        {"stInid", "in_id"},};
    public static String comboFields[] = {"index", "receiver"};
    private String stOutID;
    private String stIdDist;
    private String stReceiver;
    private String stDeletedFlag;
    private String stApprovalType;
    private String stApprovalFlag;
    private String stReplyNote;
    private String stInid;

    public String getStOutID() {
        return stOutID;
    }

    public void setStOutID(String stOutID) {
        this.stOutID = stOutID;
    }

    public String getStIdDist() {
        return stIdDist;
    }

    public void setStIdDist(String stIdDist) {
        this.stIdDist = stIdDist;
    }

    public String getStReceiver() {
        return stReceiver;
    }

    public void setStReceiver(String stReceiver) {
        this.stReceiver = stReceiver;
    }

    public UserSessionView getUser(String stUserID) {
        return (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, stUserID);
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
     * @return the stDeletedFlag
     */
    public String getStDeletedFlag() {
        return stDeletedFlag;
    }

    /**
     * @param stDeletedFlag the stDeletedFlag to set
     */
    public void setStDeletedFlag(String stDeletedFlag) {
        this.stDeletedFlag = stDeletedFlag;
    }

    /**
     * @return the stApprovalFlag
     */
    public String getStApprovalFlag() {
        return stApprovalFlag;
    }

    /**
     * @param stApprovalFlag the stApprovalFlag to set
     */
    public void setStApprovalFlag(String stApprovalFlag) {
        this.stApprovalFlag = stApprovalFlag;
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

    /**
     * @return the stInid
     */
    public String getStInid() {
        return stInid;
    }

    /**
     * @param stInid the stInid to set
     */
    public void setStInid(String stInid) {
        this.stInid = stInid;
    }
}