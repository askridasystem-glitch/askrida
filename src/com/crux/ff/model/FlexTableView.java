/***********************************************************************
 * Module:  com.crux.ff.model.FlexTableView
 * Author:  Denny Mahendra
 * Created: Jun 2, 2007 10:38:16 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.ff.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.login.model.RoleView;
import com.crux.pool.DTOPool;
import com.crux.util.Tools;
import com.webfin.insurance.model.InsurancePolicyTypeView;

import java.math.BigDecimal;
import java.util.Date;

public class FlexTableView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ff_table
(
  fft_id int8 NOT NULL,
  ref1 varchar(255),
  ref2 varchar(255),
  ref3 varchar(255),
  ref4 varchar(255),
  ref5 varchar(255),
  refid1 int8,
  refid2 int8,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  fft_group_id varchar(32),
  CONSTRAINT ff_table_pk PRIMARY KEY (fft_id)
) */

   public static String tableName = "ff_table";

   public static String fieldMap[][] = {
      {"stFFTID","fft_id*pk*nd"},
      {"stReference1","ref1"},
      {"stReference2","ref2"},
      {"stReference2","ref2"},
      {"stReference3","ref3"},
      {"stReference4","ref4"},
      {"stReference5","ref5"},
      {"stReference6","ref6"},
      {"stReference7","ref7"},
      {"dbReference1","refn1"},
      {"dbReference2","refn2"},
      {"dbReference3","refn3"},
      {"dbReference4","refn4"},
      {"dbReference5","refn5"},
      {"stReference1Desc","ref1desc*n"},
      {"stReference2Desc","ref2desc*n"},
      {"stReference3Desc","ref3desc*n"},
      {"stReference4Desc","ref4desc*n"},
      {"stReference5Desc","ref5desc*n"},
      {"stReference6Desc","ref6desc*n"},
      {"stReference7Desc","ref7desc*n"},
      {"stReferenceID1","refid1"},
      {"stReferenceID2","refid2"},
      {"stFFTGroupID","fft_group_id"},
      {"dtPeriodStart","period_start"},
      {"dtPeriodEnd","period_end"},
      {"stApprovedFlag1","approved_flag1"},
      {"stApprovedFlag2","approved_flag2"},
      {"stApprovedFlag3","approved_flag3"},
      {"stApprovedFlag4","approved_flag4"},
      {"stActiveFlag","active_flag"},
      {"stAddFlag","add_flag"},
      {"dtActiveDate","active_date"},
      {"stReference8","ref8"},
      {"stReference9","ref9"},
      {"stDescriptionNew", "description_new*n"},
      {"stFileID","file_id"},
      {"stApprovedWho1","approved_who1"},
      {"dtApprovedDate1","approved_date1"},
      {"stApprovedWho2","approved_who2"},
      {"dtApprovedDate2","approved_date2"},
      {"stApprovedWho3","approved_who3"},
      {"dtApprovedDate3","approved_date3"},
      {"stApprovedWho4","approved_who4"},
      {"dtApprovedDate4","approved_date4"},
  
   };

   private String stFFTID;
   private BigDecimal dbReference1;
   private BigDecimal dbReference2;
   private BigDecimal dbReference3;
   private BigDecimal dbReference4;
   private BigDecimal dbReference5;
   private String stReference1Desc;
   private String stReference2Desc;
   private String stReference3Desc;
   private String stReference4Desc;
   private String stReference5Desc;
   private String stReference6Desc;
   private String stReference7Desc;
   private String stReference1;
   private String stReference2;
   private String stReference3;
   private String stReference4;
   private String stReference5;
   private String stReference6;
   private String stReference7;
   private String stReferenceID1;
   private String stReferenceID2;
   private String stFFTGroupID;
   private Date dtPeriodStart;
   private Date dtPeriodEnd;
   private String stApprovedFlag1;
   private String stApprovedFlag2;
   private String stApprovedFlag3;
   private String stApprovedFlag4;
   private String stActiveFlag;
   private String stAddFlag;
   private Date dtActiveDate;

   private String stReference8;
   private String stReference9;
   private String stDescriptionNew;
   private String stFileID;

   private String stApprovedWho1;
   private Date dtApprovedDate1;
   private String stApprovedWho2;
   private Date dtApprovedDate2;
   private String stApprovedWho3;
   private Date dtApprovedDate3;
   private String stApprovedWho4;
   private Date dtApprovedDate4;

   public BigDecimal getDbReference2() {
      return dbReference2;
   }

   public void setDbReference2(BigDecimal dbReference2) {
      this.dbReference2 = dbReference2;
   }

   public BigDecimal getDbReference3() {
      return dbReference3;
   }

   public void setDbReference3(BigDecimal dbReference3) {
      this.dbReference3 = dbReference3;
   }

   public BigDecimal getDbReference1() {
      return dbReference1;
   }

   public void setDbReference1(BigDecimal dbReference1) {
      this.dbReference1 = dbReference1;
   }
   
   public BigDecimal getDbReference4() {
      return dbReference4;
   }

   public void setDbReference4(BigDecimal dbReference4) {
      this.dbReference4 = dbReference4;
   }
   
   public BigDecimal getDbReference5() {
      return dbReference5;
   }

   public void setDbReference5(BigDecimal dbReference5) {
      this.dbReference5 = dbReference5;
   }

   public String getStReference6() {
      return stReference6;
   }

   public void setStReference6(String stReference6) {
      this.stReference6 = stReference6;
   }

   public String getStReference7() {
      return stReference7;
   }

   public void setStReference7(String stReference7) {
      this.stReference7 = stReference7;
   }

   public String getStFFTGroupID() {
      return stFFTGroupID;
   }

   public void setStFFTGroupID(String stFFTGroupID) {
      this.stFFTGroupID = stFFTGroupID;
   }

   public String getStFFTID() {
      return stFFTID;
   }

   public void setStFFTID(String stFFTID) {
      this.stFFTID = stFFTID;
   }

   public String getStReference1() {
      return stReference1;
   }

   public void setStReference1(String stReference1) {
      this.stReference1 = stReference1;
   }

   public String getStReference2() {
      return stReference2;
   }

   public void setStReference2(String stReference2) {
      this.stReference2 = stReference2;
   }

   public String getStReference3() {
      return stReference3;
   }

   public void setStReference3(String stReference3) {
      this.stReference3 = stReference3;
   }

   public String getStReference4() {
      return stReference4;
   }

   public void setStReference4(String stReference4) {
      this.stReference4 = stReference4;
   }

   public String getStReference5() {
      return stReference5;
   }

   public void setStReference5(String stReference5) {
      this.stReference5 = stReference5;
   }

   public String getStReferenceID1() {
      return stReferenceID1;
   }

   public void setStReferenceID1(String stReferenceID1) {
      this.stReferenceID1 = stReferenceID1;
   }

   public String getStReferenceID2() {
      return stReferenceID2;
   }

   public void setStReferenceID2(String stReferenceID2) {
      this.stReferenceID2 = stReferenceID2;
   }

   public String getStReference1Desc() {
      return stReference1Desc;
   }

   public void setStReference1Desc(String stReference1Desc) {
      this.stReference1Desc = stReference1Desc;
   }

   public String getStReference2Desc() {
      return stReference2Desc;
   }

   public void setStReference2Desc(String stReference2Desc) {
      this.stReference2Desc = stReference2Desc;
   }

   public String getStReference3Desc() {
      return stReference3Desc;
   }

   public void setStReference3Desc(String stReference3Desc) {
      this.stReference3Desc = stReference3Desc;
   }

   public String getStReference4Desc() {
      return stReference4Desc;
   }

   public void setStReference4Desc(String stReference4Desc) {
      this.stReference4Desc = stReference4Desc;
   }

   public String getStReference5Desc() {
      return stReference5Desc;
   }

   public void setStReference5Desc(String stReference5Desc) {
      this.stReference5Desc = stReference5Desc;
   }

   public String getStReference6Desc() {
      return stReference6Desc;
   }

   public void setStReference6Desc(String stReference6Desc) {
      this.stReference6Desc = stReference6Desc;
   }

   public String getStReference7Desc() {
      return stReference7Desc;
   }

   public void setStReference7Desc(String stReference7Desc) {
      this.stReference7Desc = stReference7Desc;
   }

    public Date getDtPeriodStart() {
        return dtPeriodStart;
    }

    public void setDtPeriodStart(Date dtPeriodStart) {
        this.dtPeriodStart = dtPeriodStart;
    }

    public Date getDtPeriodEnd() {
        return dtPeriodEnd;
    }

    public void setDtPeriodEnd(Date dtPeriodEnd) {
        this.dtPeriodEnd = dtPeriodEnd;
    }

    public String getStApprovedFlag1() {
        return stApprovedFlag1;
    }

    public void setStApprovedFlag1(String stApprovedFlag1) {
        this.stApprovedFlag1 = stApprovedFlag1;
    }

    public String getStApprovedFlag2() {
        return stApprovedFlag2;
    }

    public void setStApprovedFlag2(String stApprovedFlag2) {
        this.stApprovedFlag2 = stApprovedFlag2;
    }

    public String getStApprovedFlag3() {
        return stApprovedFlag3;
    }

    public void setStApprovedFlag3(String stApprovedFlag3) {
        this.stApprovedFlag3 = stApprovedFlag3;
    }

    public String getStApprovedFlag4() {
        return stApprovedFlag4;
    }

    public void setStApprovedFlag4(String stApprovedFlag4) {
        this.stApprovedFlag4 = stApprovedFlag4;
    }

    public String getStActiveFlag() {
        return stActiveFlag;
    }

    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }

    public String getStAddFlag() {
        return stAddFlag;
    }

    public void setStAddFlag(String stAddFlag) {
        this.stAddFlag = stAddFlag;
    }
    
    public boolean isAddFlag() {
        return Tools.isYes(stAddFlag);
    }

    /**
     * @return the dtActiveDate
     */
    public Date getDtActiveDate() {
        return dtActiveDate;
    }

    /**
     * @param dtActiveDate the dtActiveDate to set
     */
    public void setDtActiveDate(Date dtActiveDate) {
        this.dtActiveDate = dtActiveDate;
    }

    public String getStReference8() {
        return stReference8;
    }

    /**
     * @param stReference8 the stReference8 to set
     */
    public void setStReference8(String stReference8) {
        this.stReference8 = stReference8;
    }

    /**
     * @return the stReference9
     */
    public String getStReference9() {
        return stReference9;
    }

    /**
     * @param stReference9 the stReference9 to set
     */
    public void setStReference9(String stReference9) {
        this.stReference9 = stReference9;
    }

    public boolean isMarked() {
        return Tools.isYes(stAddFlag);
    }

    /**
     * @return the stDescription
     */
    public String getStDescriptionNew() {
        return stDescriptionNew;
    }

    /**
     * @param stDescription the stDescription to set
     */
    public void setStDescriptionNew(String stDescriptionNew) {
        this.stDescriptionNew = stDescriptionNew;
    }

    public RoleView getRoleAccept() {
        final RoleView reg = (RoleView) DTOPool.getInstance().getDTO(RoleView.class, stReference1);

        return reg;
    }

    public RoleView getRoleComm() {
        final RoleView reg = (RoleView) DTOPool.getInstance().getDTO(RoleView.class, stReference2);

        return reg;
    }

    /**
     * @return the stFileID
     */
    public String getStFileID() {
        return stFileID;
    }

    /**
     * @param stFileID the stFileID to set
     */
    public void setStFileID(String stFileID) {
        this.stFileID = stFileID;
    }

    public InsurancePolicyTypeView getPolicyType() {
        return (InsurancePolicyTypeView) DTOPool.getInstance().getDTO(InsurancePolicyTypeView.class, stReference3);
    }

    /**
     * @return the stApprovedWho1
     */
    public String getStApprovedWho1() {
        return stApprovedWho1;
    }

    /**
     * @param stApprovedWho1 the stApprovedWho1 to set
     */
    public void setStApprovedWho1(String stApprovedWho1) {
        this.stApprovedWho1 = stApprovedWho1;
    }

    /**
     * @return the dtApprovedDate1
     */
    public Date getDtApprovedDate1() {
        return dtApprovedDate1;
    }

    /**
     * @param dtApprovedDate1 the dtApprovedDate1 to set
     */
    public void setDtApprovedDate1(Date dtApprovedDate1) {
        this.dtApprovedDate1 = dtApprovedDate1;
    }

    /**
     * @return the stApprovedWho2
     */
    public String getStApprovedWho2() {
        return stApprovedWho2;
    }

    /**
     * @param stApprovedWho2 the stApprovedWho2 to set
     */
    public void setStApprovedWho2(String stApprovedWho2) {
        this.stApprovedWho2 = stApprovedWho2;
    }

    /**
     * @return the dtApprovedDate2
     */
    public Date getDtApprovedDate2() {
        return dtApprovedDate2;
    }

    /**
     * @param dtApprovedDate2 the dtApprovedDate2 to set
     */
    public void setDtApprovedDate2(Date dtApprovedDate2) {
        this.dtApprovedDate2 = dtApprovedDate2;
    }

    /**
     * @return the stApprovedWho3
     */
    public String getStApprovedWho3() {
        return stApprovedWho3;
    }

    /**
     * @param stApprovedWho3 the stApprovedWho3 to set
     */
    public void setStApprovedWho3(String stApprovedWho3) {
        this.stApprovedWho3 = stApprovedWho3;
    }

    /**
     * @return the dtApprovedDate3
     */
    public Date getDtApprovedDate3() {
        return dtApprovedDate3;
    }

    /**
     * @param dtApprovedDate3 the dtApprovedDate3 to set
     */
    public void setDtApprovedDate3(Date dtApprovedDate3) {
        this.dtApprovedDate3 = dtApprovedDate3;
    }

    /**
     * @return the stApprovedWho4
     */
    public String getStApprovedWho4() {
        return stApprovedWho4;
    }

    /**
     * @param stApprovedWho4 the stApprovedWho4 to set
     */
    public void setStApprovedWho4(String stApprovedWho4) {
        this.stApprovedWho4 = stApprovedWho4;
    }

    /**
     * @return the dtApprovedDate4
     */
    public Date getDtApprovedDate4() {
        return dtApprovedDate4;
    }

    /**
     * @param dtApprovedDate4 the dtApprovedDate4 to set
     */
    public void setDtApprovedDate4(Date dtApprovedDate4) {
        this.dtApprovedDate4 = dtApprovedDate4;
    }

    public boolean isActive() {
        return Tools.isYes(stActiveFlag);
    }

}
