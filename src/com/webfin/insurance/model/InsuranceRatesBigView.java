/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceRatesBigView
 * Author:  Denny Mahendra
 * Created: Oct 18, 2007 12:22:54 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import java.math.BigDecimal;
import java.util.Date;

public class InsuranceRatesBigView extends DTO implements RecordAudit {
   /*
-- Table: ins_rates_big

-- DROP TABLE ins_rates_big;

CREATE TABLE ins_rates_big
(
  ins_rates_id int8,
  ref1 varchar(5),
  ref2 varchar(5),
  ref3 varchar(5),
  rate0 numeric,
  rate1 numeric,
  rate2 numeric,
  rate3 numeric,
  rate4 numeric,
  rate5 numeric,
  rate6 numeric,
  rate7 numeric,
  rate8 numeric,
  rate9 numeric,
  rate10 numeric,
  rate11 numeric,
  rate12 numeric,
  rate13 numeric,
  rate14 numeric,
  rate15 numeric,
  rate16 numeric,
  rate17 numeric,
  rate18 numeric,
  rate19 numeric,
  rate20 numeric,
  rate21 numeric,
  rate22 numeric,
  rate23 numeric,
  rate24 numeric,
  rate25 numeric,
  rate26 numeric,
  rate27 numeric,
  rate28 numeric,
  rate29 numeric,
  rate30 numeric,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  refid1 int8,
  ins_rates_hdr_id int8,
  rate_class varchar(20),
  period_start timestamp,
  period_end timestamp
)
WITH OIDS;
ALTER TABLE ins_rates_big OWNER TO postgres;

   */

   public static String tableName = "ins_rates_big";

   public static String fieldMap[][] = {
      {"stInsuranceRatesID","ins_rates_id*pk"},
      {"stRateClass","rate_class"},
      {"stDescription","description"},
      {"dtPeriodStart","period_start"},
      {"dtPeriodEnd","period_end"},
      {"stReference1","ref1"},
      {"stReference2","ref2"},
      {"stReference3","ref3"},
      {"stReference4","ref4"},
      {"stReference5","ref5"},
      {"stReference6","ref6"},
      {"stNotes","notes"},
      {"dbRate0","rate0"},
      {"dbRate1","rate1"},
      {"dbRate2","rate2"},
      {"dbRate3","rate3"},
      {"dbRate4","rate4"},
      {"dbRate5","rate5"},
      {"dbRate6","rate6"},
      {"dbRate7","rate7"},
      {"dbRate8","rate8"},
      {"dbRate9","rate9"},
      {"dbRate10","rate10"},
      {"dbRate11","rate11"},
      {"dbRate12","rate12"},
      {"dbRate13","rate13"},
      {"dbRate14","rate14"},
      {"dbRate15","rate15"},
      {"dbRate16","rate16"},
      {"dbRate17","rate17"},
      {"dbRate18","rate18"},
      {"dbRate19","rate19"},
      {"dbRate20","rate20"},
      {"dbRate21","rate21"},
      {"dbRate22","rate22"},
      {"dbRate23","rate23"},
      {"dbRate24","rate24"},
      {"dbRate25","rate25"},
      {"dbRate26","rate26"},
      {"dbRate27","rate27"},
      {"dbRate28","rate28"},
      {"dbRate29","rate29"},
      {"dbRate30","rate30"},
      {"dbRate31","rate31"},
      {"dbRate32","rate32"},
      {"dbRate33","rate33"},
      {"dbRate34","rate34"},
      {"dbRate35","rate35"},
      {"dbRate36","rate36"},
      {"dbRate37","rate37"},
      {"dbRate38","rate38"},
      {"dbRate39","rate39"},
      {"dbRate40","rate40"},
      {"stRefID1","refid1"},
      {"stActiveFlag","active_flag"},
      {"stFileID","file_id"},
   };

    private String stInsuranceRatesID;
    private String stRateClass;
    private String stDescription;
    private Date dtPeriodStart;
    private Date dtPeriodEnd;
    private String stReference1;
    private String stReference2;
    private String stReference3;
    private String stReference4;
    private String stReference5;
    private String stReference6;
    private String stRefID1;
    private String stNotes;
    private String stActiveFlag;

    private BigDecimal dbRate0;
    private BigDecimal dbRate1;
    private BigDecimal dbRate2;
    private BigDecimal dbRate3;
    private BigDecimal dbRate4;
    private BigDecimal dbRate5;
    private BigDecimal dbRate6;
    private BigDecimal dbRate7;
    private BigDecimal dbRate8;
    private BigDecimal dbRate9;
    private BigDecimal dbRate10;
    private BigDecimal dbRate11;
    private BigDecimal dbRate12;
    private BigDecimal dbRate13;
    private BigDecimal dbRate14;
    private BigDecimal dbRate15;
    private BigDecimal dbRate16;
    private BigDecimal dbRate17;
    private BigDecimal dbRate18;
    private BigDecimal dbRate19;
    private BigDecimal dbRate20;
    private BigDecimal dbRate21;
    private BigDecimal dbRate22;
    private BigDecimal dbRate23;
    private BigDecimal dbRate24;
    private BigDecimal dbRate25;
    private BigDecimal dbRate26;
    private BigDecimal dbRate27;
    private BigDecimal dbRate28;
    private BigDecimal dbRate29;
    private BigDecimal dbRate30;
    private BigDecimal dbRate31;
    private BigDecimal dbRate32;
    private BigDecimal dbRate33;
    private BigDecimal dbRate34;
    private BigDecimal dbRate35;
    private BigDecimal dbRate36;
    private BigDecimal dbRate37;

    private String stFileID;

    public String getStFileID() {
        return stFileID;
    }

    public void setStFileID(String stFileID) {
        this.stFileID = stFileID;
    }

    public BigDecimal getDbRate31() {
        return dbRate31;
    }

    public void setDbRate31(BigDecimal dbRate31) {
        this.dbRate31 = dbRate31;
    }

    public BigDecimal getDbRate32() {
        return dbRate32;
    }

    public void setDbRate32(BigDecimal dbRate32) {
        this.dbRate32 = dbRate32;
    }

    public BigDecimal getDbRate33() {
        return dbRate33;
    }

    public void setDbRate33(BigDecimal dbRate33) {
        this.dbRate33 = dbRate33;
    }

    public BigDecimal getDbRate34() {
        return dbRate34;
    }

    public void setDbRate34(BigDecimal dbRate34) {
        this.dbRate34 = dbRate34;
    }

    public BigDecimal getDbRate35() {
        return dbRate35;
    }

    public void setDbRate35(BigDecimal dbRate35) {
        this.dbRate35 = dbRate35;
    }

    public BigDecimal getDbRate36() {
        return dbRate36;
    }

    public void setDbRate36(BigDecimal dbRate36) {
        this.dbRate36 = dbRate36;
    }

    public BigDecimal getDbRate37() {
        return dbRate37;
    }

    public void setDbRate37(BigDecimal dbRate37) {
        this.dbRate37 = dbRate37;
    }

    public BigDecimal getDbRate38() {
        return dbRate38;
    }

    public void setDbRate38(BigDecimal dbRate38) {
        this.dbRate38 = dbRate38;
    }

    public BigDecimal getDbRate39() {
        return dbRate39;
    }

    public void setDbRate39(BigDecimal dbRate39) {
        this.dbRate39 = dbRate39;
    }

    public BigDecimal getDbRate40() {
        return dbRate40;
    }

    public void setDbRate40(BigDecimal dbRate40) {
        this.dbRate40 = dbRate40;
    }
    private BigDecimal dbRate38;
    private BigDecimal dbRate39;
    private BigDecimal dbRate40;

    /**
     * @return the stInsuranceRatesID
     */
    public String getStInsuranceRatesID() {
        return stInsuranceRatesID;
    }

    /**
     * @param stInsuranceRatesID the stInsuranceRatesID to set
     */
    public void setStInsuranceRatesID(String stInsuranceRatesID) {
        this.stInsuranceRatesID = stInsuranceRatesID;
    }

    /**
     * @return the stRateClass
     */
    public String getStRateClass() {
        return stRateClass;
    }

    /**
     * @param stRateClass the stRateClass to set
     */
    public void setStRateClass(String stRateClass) {
        this.stRateClass = stRateClass;
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
     * @return the dtPeriodStart
     */
    public Date getDtPeriodStart() {
        return dtPeriodStart;
    }

    /**
     * @param dtPeriodStart the dtPeriodStart to set
     */
    public void setDtPeriodStart(Date dtPeriodStart) {
        this.dtPeriodStart = dtPeriodStart;
    }

    /**
     * @return the dtPeriodEnd
     */
    public Date getDtPeriodEnd() {
        return dtPeriodEnd;
    }

    /**
     * @param dtPeriodEnd the dtPeriodEnd to set
     */
    public void setDtPeriodEnd(Date dtPeriodEnd) {
        this.dtPeriodEnd = dtPeriodEnd;
    }

    /**
     * @return the stReference1
     */
    public String getStReference1() {
        return stReference1;
    }

    /**
     * @param stReference1 the stReference1 to set
     */
    public void setStReference1(String stReference1) {
        this.stReference1 = stReference1;
    }

    /**
     * @return the stReference2
     */
    public String getStReference2() {
        return stReference2;
    }

    /**
     * @param stReference2 the stReference2 to set
     */
    public void setStReference2(String stReference2) {
        this.stReference2 = stReference2;
    }

    /**
     * @return the stReference3
     */
    public String getStReference3() {
        return stReference3;
    }

    /**
     * @param stReference3 the stReference3 to set
     */
    public void setStReference3(String stReference3) {
        this.stReference3 = stReference3;
    }

    /**
     * @return the stReference4
     */
    public String getStReference4() {
        return stReference4;
    }

    /**
     * @param stReference4 the stReference4 to set
     */
    public void setStReference4(String stReference4) {
        this.stReference4 = stReference4;
    }

    /**
     * @return the stReference5
     */
    public String getStReference5() {
        return stReference5;
    }

    /**
     * @param stReference5 the stReference5 to set
     */
    public void setStReference5(String stReference5) {
        this.stReference5 = stReference5;
    }

    /**
     * @return the dbRate0
     */
    public BigDecimal getDbRate0() {
        return dbRate0;
    }

    /**
     * @param dbRate0 the dbRate0 to set
     */
    public void setDbRate0(BigDecimal dbRate0) {
        this.dbRate0 = dbRate0;
    }

    /**
     * @return the dbRate1
     */
    public BigDecimal getDbRate1() {
        return dbRate1;
    }

    /**
     * @param dbRate1 the dbRate1 to set
     */
    public void setDbRate1(BigDecimal dbRate1) {
        this.dbRate1 = dbRate1;
    }

    /**
     * @return the dbRate2
     */
    public BigDecimal getDbRate2() {
        return dbRate2;
    }

    /**
     * @param dbRate2 the dbRate2 to set
     */
    public void setDbRate2(BigDecimal dbRate2) {
        this.dbRate2 = dbRate2;
    }

    /**
     * @return the dbRate3
     */
    public BigDecimal getDbRate3() {
        return dbRate3;
    }

    /**
     * @param dbRate3 the dbRate3 to set
     */
    public void setDbRate3(BigDecimal dbRate3) {
        this.dbRate3 = dbRate3;
    }

    /**
     * @return the dbRate4
     */
    public BigDecimal getDbRate4() {
        return dbRate4;
    }

    /**
     * @param dbRate4 the dbRate4 to set
     */
    public void setDbRate4(BigDecimal dbRate4) {
        this.dbRate4 = dbRate4;
    }

    /**
     * @return the dbRate5
     */
    public BigDecimal getDbRate5() {
        return dbRate5;
    }

    /**
     * @param dbRate5 the dbRate5 to set
     */
    public void setDbRate5(BigDecimal dbRate5) {
        this.dbRate5 = dbRate5;
    }

    /**
     * @return the dbRate6
     */
    public BigDecimal getDbRate6() {
        return dbRate6;
    }

    /**
     * @param dbRate6 the dbRate6 to set
     */
    public void setDbRate6(BigDecimal dbRate6) {
        this.dbRate6 = dbRate6;
    }

    /**
     * @return the dbRate7
     */
    public BigDecimal getDbRate7() {
        return dbRate7;
    }

    /**
     * @param dbRate7 the dbRate7 to set
     */
    public void setDbRate7(BigDecimal dbRate7) {
        this.dbRate7 = dbRate7;
    }

    /**
     * @return the dbRate8
     */
    public BigDecimal getDbRate8() {
        return dbRate8;
    }

    /**
     * @param dbRate8 the dbRate8 to set
     */
    public void setDbRate8(BigDecimal dbRate8) {
        this.dbRate8 = dbRate8;
    }

    /**
     * @return the dbRate9
     */
    public BigDecimal getDbRate9() {
        return dbRate9;
    }

    /**
     * @param dbRate9 the dbRate9 to set
     */
    public void setDbRate9(BigDecimal dbRate9) {
        this.dbRate9 = dbRate9;
    }

    /**
     * @return the dbRate10
     */
    public BigDecimal getDbRate10() {
        return dbRate10;
    }

    /**
     * @param dbRate10 the dbRate10 to set
     */
    public void setDbRate10(BigDecimal dbRate10) {
        this.dbRate10 = dbRate10;
    }

    /**
     * @return the dbRate11
     */
    public BigDecimal getDbRate11() {
        return dbRate11;
    }

    /**
     * @param dbRate11 the dbRate11 to set
     */
    public void setDbRate11(BigDecimal dbRate11) {
        this.dbRate11 = dbRate11;
    }

    /**
     * @return the dbRate12
     */
    public BigDecimal getDbRate12() {
        return dbRate12;
    }

    /**
     * @param dbRate12 the dbRate12 to set
     */
    public void setDbRate12(BigDecimal dbRate12) {
        this.dbRate12 = dbRate12;
    }

    /**
     * @return the dbRate13
     */
    public BigDecimal getDbRate13() {
        return dbRate13;
    }

    /**
     * @param dbRate13 the dbRate13 to set
     */
    public void setDbRate13(BigDecimal dbRate13) {
        this.dbRate13 = dbRate13;
    }

    /**
     * @return the dbRate14
     */
    public BigDecimal getDbRate14() {
        return dbRate14;
    }

    /**
     * @param dbRate14 the dbRate14 to set
     */
    public void setDbRate14(BigDecimal dbRate14) {
        this.dbRate14 = dbRate14;
    }

    /**
     * @return the dbRate15
     */
    public BigDecimal getDbRate15() {
        return dbRate15;
    }

    /**
     * @param dbRate15 the dbRate15 to set
     */
    public void setDbRate15(BigDecimal dbRate15) {
        this.dbRate15 = dbRate15;
    }

    /**
     * @return the dbRate16
     */
    public BigDecimal getDbRate16() {
        return dbRate16;
    }

    /**
     * @param dbRate16 the dbRate16 to set
     */
    public void setDbRate16(BigDecimal dbRate16) {
        this.dbRate16 = dbRate16;
    }

    /**
     * @return the dbRate17
     */
    public BigDecimal getDbRate17() {
        return dbRate17;
    }

    /**
     * @param dbRate17 the dbRate17 to set
     */
    public void setDbRate17(BigDecimal dbRate17) {
        this.dbRate17 = dbRate17;
    }

    /**
     * @return the dbRate18
     */
    public BigDecimal getDbRate18() {
        return dbRate18;
    }

    /**
     * @param dbRate18 the dbRate18 to set
     */
    public void setDbRate18(BigDecimal dbRate18) {
        this.dbRate18 = dbRate18;
    }

    /**
     * @return the dbRate19
     */
    public BigDecimal getDbRate19() {
        return dbRate19;
    }

    /**
     * @param dbRate19 the dbRate19 to set
     */
    public void setDbRate19(BigDecimal dbRate19) {
        this.dbRate19 = dbRate19;
    }

    /**
     * @return the dbRate20
     */
    public BigDecimal getDbRate20() {
        return dbRate20;
    }

    /**
     * @param dbRate20 the dbRate20 to set
     */
    public void setDbRate20(BigDecimal dbRate20) {
        this.dbRate20 = dbRate20;
    }

    /**
     * @return the dbRate21
     */
    public BigDecimal getDbRate21() {
        return dbRate21;
    }

    /**
     * @param dbRate21 the dbRate21 to set
     */
    public void setDbRate21(BigDecimal dbRate21) {
        this.dbRate21 = dbRate21;
    }

    /**
     * @return the dbRate22
     */
    public BigDecimal getDbRate22() {
        return dbRate22;
    }

    /**
     * @param dbRate22 the dbRate22 to set
     */
    public void setDbRate22(BigDecimal dbRate22) {
        this.dbRate22 = dbRate22;
    }

    /**
     * @return the dbRate23
     */
    public BigDecimal getDbRate23() {
        return dbRate23;
    }

    /**
     * @param dbRate23 the dbRate23 to set
     */
    public void setDbRate23(BigDecimal dbRate23) {
        this.dbRate23 = dbRate23;
    }

    /**
     * @return the dbRate24
     */
    public BigDecimal getDbRate24() {
        return dbRate24;
    }

    /**
     * @param dbRate24 the dbRate24 to set
     */
    public void setDbRate24(BigDecimal dbRate24) {
        this.dbRate24 = dbRate24;
    }

    /**
     * @return the dbRate25
     */
    public BigDecimal getDbRate25() {
        return dbRate25;
    }

    /**
     * @param dbRate25 the dbRate25 to set
     */
    public void setDbRate25(BigDecimal dbRate25) {
        this.dbRate25 = dbRate25;
    }

    /**
     * @return the dbRate26
     */
    public BigDecimal getDbRate26() {
        return dbRate26;
    }

    /**
     * @param dbRate26 the dbRate26 to set
     */
    public void setDbRate26(BigDecimal dbRate26) {
        this.dbRate26 = dbRate26;
    }

    /**
     * @return the dbRate27
     */
    public BigDecimal getDbRate27() {
        return dbRate27;
    }

    /**
     * @param dbRate27 the dbRate27 to set
     */
    public void setDbRate27(BigDecimal dbRate27) {
        this.dbRate27 = dbRate27;
    }

    /**
     * @return the dbRate28
     */
    public BigDecimal getDbRate28() {
        return dbRate28;
    }

    /**
     * @param dbRate28 the dbRate28 to set
     */
    public void setDbRate28(BigDecimal dbRate28) {
        this.dbRate28 = dbRate28;
    }

    /**
     * @return the dbRate29
     */
    public BigDecimal getDbRate29() {
        return dbRate29;
    }

    /**
     * @param dbRate29 the dbRate29 to set
     */
    public void setDbRate29(BigDecimal dbRate29) {
        this.dbRate29 = dbRate29;
    }

    /**
     * @return the dbRate30
     */
    public BigDecimal getDbRate30() {
        return dbRate30;
    }

    /**
     * @param dbRate30 the dbRate30 to set
     */
    public void setDbRate30(BigDecimal dbRate30) {
        this.dbRate30 = dbRate30;
    }

    /**
     * @return the stRefID1
     */
    public String getStRefID1() {
        return stRefID1;
    }

    /**
     * @param stRefID1 the stRefID1 to set
     */
    public void setStRefID1(String stRefID1) {
        this.stRefID1 = stRefID1;
    }

    public String getStNotes() {
        return stNotes;
    }

    public void setStNotes(String stNotes) {
        this.stNotes = stNotes;
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
     * @return the stReference6
     */
    public String getStReference6() {
        return stReference6;
    }

    /**
     * @param stReference6 the stReference6 to set
     */
    public void setStReference6(String stReference6) {
        this.stReference6 = stReference6;
    }



}
