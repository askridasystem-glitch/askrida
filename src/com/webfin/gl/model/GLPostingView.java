/***********************************************************************
 * Module:  com.webfin.gl.model.GLPostingView
 * Author:  Ahmad Rhodoni
 * Created: 10 Oktober 2012
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.LogManager;
import com.crux.util.Tools;


public class GLPostingView extends DTO implements RecordAudit {
   /*
   
CREATE TABLE gl_posting
(
  gl_post_id bigint,
  months character varying(10),
  years character varying(4),
  cc_code character varying(2)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE gl_posting
  OWNER TO postgres;

   */
   private final static transient LogManager logger = LogManager.getInstance(GLPostingView.class);

   public static String tableName = "gl_posting";
   
   public static String fieldMap[][] = {
      {"stGLPostingID","gl_post_id*pk"},
      {"stMonths","months"},
      {"stYears","years"},
      {"stCostCenterCode","cc_code"},
      {"stPostedFlag","posted_flag"},
      {"stCostCenter","cost_center*n"},
      {"stUserName","user_name*n"},
      {"stUserNameEdited","user_name_e*n"},
      {"stLastChanged","last_changed*n"},
      {"stStatus","status"},
      {"stNotes","notes"},
      {"stFinalFlag","final_flag"},
      {"stConfigFlag","config_flag"},
      
   };
   
   private String stGLPostingID;
   private String stMonths;
   private String stYears;
   private String stCostCenterCode;
   private String stPostedFlag;
   private String stCostCenter;
   private String stUserName;
   private String stUserNameEdited;
   private String stLastChanged;
   private String stStatus;
   private String stNotes;
   private String stFinalFlag;
   private String stConfigFlag;


    public String getStGLPostingID() {
        return stGLPostingID;
    }

    public void setStGLPostingID(String stGLPostingID) {
        this.stGLPostingID = stGLPostingID;
    }

    public String getStMonths() {
        return stMonths;
    }

    public void setStMonths(String stMonths) {
        this.stMonths = stMonths;
    }

    public String getStYears() {
        return stYears;
    }

    public void setStYears(String stYears) {
        this.stYears = stYears;
    }

    public String getStCostCenterCode() {
        return stCostCenterCode;
    }

    public void setStCostCenterCode(String stCostCenterCode) {
        this.stCostCenterCode = stCostCenterCode;
    }

    public String getStPostedFlag() {
        return stPostedFlag;
    }

    public void setStPostedFlag(String stPostedFlag) {
        this.stPostedFlag = stPostedFlag;
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

    /**
     * @return the stUserName
     */
    public String getStUserName() {
        return stUserName;
    }

    /**
     * @param stUserName the stUserName to set
     */
    public void setStUserName(String stUserName) {
        this.stUserName = stUserName;
    }

    /**
     * @return the stUserNameEdited
     */
    public String getStUserNameEdited() {
        return stUserNameEdited;
    }

    /**
     * @param stUserNameEdited the stUserNameEdited to set
     */
    public void setStUserNameEdited(String stUserNameEdited) {
        this.stUserNameEdited = stUserNameEdited;
    }

    /**
     * @return the stLastChanged
     */
    public String getStLastChanged() {
        return stLastChanged;
    }

    /**
     * @param stLastChanged the stLastChanged to set
     */
    public void setStLastChanged(String stLastChanged) {
        this.stLastChanged = stLastChanged;
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

    /**
     * @return the stNotes
     */
    public String getStNotes() {
        return stNotes;
    }

    /**
     * @param stNotes the stNotes to set
     */
    public void setStNotes(String stNotes) {
        this.stNotes = stNotes;
    }

    /**
     * @return the stFinalFlag
     */
    public String getStFinalFlag() {
        return stFinalFlag;
    }

    /**
     * @param stFinalFlag the stFinalFlag to set
     */
    public void setStFinalFlag(String stFinalFlag) {
        this.stFinalFlag = stFinalFlag;
    }

    public boolean isFinal() {
        return Tools.isYes(stFinalFlag);
    }

    /**
     * @return the stConfigFlag
     */
    public String getStConfigFlag() {
        return stConfigFlag;
    }

    /**
     * @param stConfigFlag the stConfigFlag to set
     */
    public void setStConfigFlag(String stConfigFlag) {
        this.stConfigFlag = stConfigFlag;
    }
    
}
