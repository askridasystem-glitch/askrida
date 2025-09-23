package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.util.LogManager;
import com.crux.util.Tools;
import java.math.BigDecimal;

public class InsurancePostingView
  extends DTO
{
  private static final transient LogManager logger = LogManager.getInstance(InsurancePostingView.class);
  public static String tableName = "ins_closing";
  public static String[][] fieldMap = { { "stGLPostingID", "gl_post_id*pk" }, { "stMonths", "months" }, { "stYears", "years" }, { "stPostedFlag", "posted_flag" }, { "stUserName", "user_name*n" }, { "stUserNameEdited", "user_name_e*n" }, { "stLastChanged", "last_changed*n" }, { "stStatus", "status" }, { "stNotes", "notes" }, { "stFinalFlag", "final_flag" }, { "stConfigFlag", "config_flag" }, { "dbProduksi", "produksi*n" }, { "dbKlaim", "klaim*n" } };
  private String stGLPostingID;
  private String stMonths;
  private String stYears;
  private String stPostedFlag;
  private String stUserName;
  private String stUserNameEdited;
  private String stLastChanged;
  private String stStatus;
  private String stNotes;
  private String stFinalFlag;
  private String stConfigFlag;
  private BigDecimal dbProduksi;
  private BigDecimal dbKlaim;
  
  public String getStGLPostingID()
  {
    return this.stGLPostingID;
  }
  
  public void setStGLPostingID(String stGLPostingID)
  {
    this.stGLPostingID = stGLPostingID;
  }
  
  public String getStMonths()
  {
    return this.stMonths;
  }
  
  public void setStMonths(String stMonths)
  {
    this.stMonths = stMonths;
  }
  
  public String getStYears()
  {
    return this.stYears;
  }
  
  public void setStYears(String stYears)
  {
    this.stYears = stYears;
  }
  
  public String getStPostedFlag()
  {
    return this.stPostedFlag;
  }
  
  public void setStPostedFlag(String stPostedFlag)
  {
    this.stPostedFlag = stPostedFlag;
  }
  
  public String getStUserName()
  {
    return this.stUserName;
  }
  
  public void setStUserName(String stUserName)
  {
    this.stUserName = stUserName;
  }
  
  public String getStUserNameEdited()
  {
    return this.stUserNameEdited;
  }
  
  public void setStUserNameEdited(String stUserNameEdited)
  {
    this.stUserNameEdited = stUserNameEdited;
  }
  
  public String getStLastChanged()
  {
    return this.stLastChanged;
  }
  
  public void setStLastChanged(String stLastChanged)
  {
    this.stLastChanged = stLastChanged;
  }
  
  public String getStStatus()
  {
    return this.stStatus;
  }
  
  public void setStStatus(String stStatus)
  {
    this.stStatus = stStatus;
  }
  
  public String getStNotes()
  {
    return this.stNotes;
  }
  
  public void setStNotes(String stNotes)
  {
    this.stNotes = stNotes;
  }
  
  public String getStFinalFlag()
  {
    return this.stFinalFlag;
  }
  
  public void setStFinalFlag(String stFinalFlag)
  {
    this.stFinalFlag = stFinalFlag;
  }
  
  public String getStConfigFlag()
  {
    return this.stConfigFlag;
  }
  
  public void setStConfigFlag(String stConfigFlag)
  {
    this.stConfigFlag = stConfigFlag;
  }
  
  public BigDecimal getDbProduksi()
  {
    return this.dbProduksi;
  }
  
  public void setDbProduksi(BigDecimal dbProduksi)
  {
    this.dbProduksi = dbProduksi;
  }
  
  public BigDecimal getDbKlaim()
  {
    return this.dbKlaim;
  }
  
  public void setDbKlaim(BigDecimal dbKlaim)
  {
    this.dbKlaim = dbKlaim;
  }
  
  public boolean isPostedFlag()
  {
    return Tools.isYes(this.stPostedFlag);
  }
}


/* Location:              F:\DONI\decompiler\src\webapps\com.ear\!\webfin\insurance\model\InsurancePostingView.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */