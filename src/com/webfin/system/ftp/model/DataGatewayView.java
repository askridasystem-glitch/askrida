/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceTSIView
 * Author:  DONI
 * Created: 15-10-2014
 * Purpose: 
 ***********************************************************************/

package com.webfin.system.ftp.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;

public class DataGatewayView extends DTO implements RecordAudit {

    /*
            CREATE TABLE s_data_gateway
        (
          gateway_id bigint NOT NULL,
          description character varying(255),
          ftp_address character varying(128),
          ftp_user_id character varying(255),
          ftp_password character varying(255),
          data_directory character varying(255),
          active_flag character varying(1),
          ftp_port bigint,
          CONSTRAINT s_data_gateway_pkey PRIMARY KEY (gateway_id)
        )
        WITH (
          OIDS=FALSE
        );
        ALTER TABLE s_data_gateway
          OWNER TO postgres;
    */

   public static String tableName = "s_data_gateway";

   public static String fieldMap[][] = {
      {"stGatewayID","gateway_id*pk"},
      {"stDescription","description"},
      {"stFTPAddress","ftp_address"},
      {"stFTPUserID","ftp_user_id"},
      {"stFTPPassword","ftp_password"},
      {"stDataDirectory","data_directory"},
      {"stActiveFlag","active_flag"},
      {"stFTPPort","ftp_port"},
      {"stHistoryDirectory","history_directory"},
      {"stDataClaimDirectory","data_claim_directory"},
      {"stCostCenterCode","cc_code"},
      {"stRegionID","region_id"},
      {"stPolisDetailFlag","polis_detail_f"},
      {"stAutomaticApprovalFlag","automatic_approval_f"},
   };

   private String stGatewayID;
   private String stDescription;
   private String stFTPAddress;
   private String stFTPUserID;
   private String stFTPPassword;
   private String stDataDirectory;
   private String stActiveFlag;
   private String stFTPPort;
   private String stHistoryDirectory;
   private String stDataClaimDirectory;
   private String stCostCenterCode;
   private String stRegionID;
   private String stPolisDetailFlag;
   private String stAutomaticApprovalFlag;

    /**
     * @return the stGatewayID
     */
    public String getStGatewayID() {
        return stGatewayID;
    }

    /**
     * @param stGatewayID the stGatewayID to set
     */
    public void setStGatewayID(String stGatewayID) {
        this.stGatewayID = stGatewayID;
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
     * @return the stFTPAddress
     */
    public String getStFTPAddress() {
        return stFTPAddress;
    }

    /**
     * @param stFTPAddress the stFTPAddress to set
     */
    public void setStFTPAddress(String stFTPAddress) {
        this.stFTPAddress = stFTPAddress;
    }

    /**
     * @return the stFTPUserID
     */
    public String getStFTPUserID() {
        return stFTPUserID;
    }

    /**
     * @param stFTPUserID the stFTPUserID to set
     */
    public void setStFTPUserID(String stFTPUserID) {
        this.stFTPUserID = stFTPUserID;
    }

    /**
     * @return the stFTPPassword
     */
    public String getStFTPPassword() {
        return stFTPPassword;
    }

    /**
     * @param stFTPPassword the stFTPPassword to set
     */
    public void setStFTPPassword(String stFTPPassword) {
        this.stFTPPassword = stFTPPassword;
    }

    /**
     * @return the stDataDirectory
     */
    public String getStDataDirectory() {
        return stDataDirectory;
    }

    /**
     * @param stDataDirectory the stDataDirectory to set
     */
    public void setStDataDirectory(String stDataDirectory) {
        this.stDataDirectory = stDataDirectory;
    }

    /**
     * @return the stActiveFlag
     */
    public String getStActiveFlag() {
        return stActiveFlag;
    }

    /**
     * @param stActiveFlag the stActiveFlag to set
     */
    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }

    /**
     * @return the stFTPPort
     */
    public String getStFTPPort() {
        return stFTPPort;
    }

    /**
     * @param stFTPPort the stFTPPort to set
     */
    public void setStFTPPort(String stFTPPort) {
        this.stFTPPort = stFTPPort;
    }

    /**
     * @return the stHistoryDirectory
     */
    public String getStHistoryDirectory() {
        return stHistoryDirectory;
    }

    /**
     * @param stHistoryDirectory the stHistoryDirectory to set
     */
    public void setStHistoryDirectory(String stHistoryDirectory) {
        this.stHistoryDirectory = stHistoryDirectory;
    }

    /**
     * @return the stDataClaimDirectory
     */
    public String getStDataClaimDirectory() {
        return stDataClaimDirectory;
    }

    /**
     * @param stDataClaimDirectory the stDataClaimDirectory to set
     */
    public void setStDataClaimDirectory(String stDataClaimDirectory) {
        this.stDataClaimDirectory = stDataClaimDirectory;
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
     * @return the stPolisDetailFlag
     */
    public String getStPolisDetailFlag() {
        return stPolisDetailFlag;
    }

    /**
     * @param stPolisDetailFlag the stPolisDetailFlag to set
     */
    public void setStPolisDetailFlag(String stPolisDetailFlag) {
        this.stPolisDetailFlag = stPolisDetailFlag;
    }

    public boolean isPolisDetails(){
        return Tools.isYes(stPolisDetailFlag);
    }

    /**
     * @return the stAutomaticApprovalFlag
     */
    public String getStAutomaticApprovalFlag() {
        return stAutomaticApprovalFlag;
    }

    /**
     * @param stAutomaticApprovalFlag the stAutomaticApprovalFlag to set
     */
    public void setStAutomaticApprovalFlag(String stAutomaticApprovalFlag) {
        this.stAutomaticApprovalFlag = stAutomaticApprovalFlag;
    }

    public boolean isAutomaticApproval(){
        return Tools.isYes(stAutomaticApprovalFlag);
    }
   
}
