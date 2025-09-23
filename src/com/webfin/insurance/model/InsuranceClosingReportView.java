/***********************************************************************
 * Module:  com.webfin.gl.model.InsuranceClosingView
 * Author:  Ahmad Rhodoni
 * Created: 10 Oktober 2012
 * Purpose: 
 ***********************************************************************/
package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;
import com.crux.util.LogManager;
import com.webfin.entity.model.EntityView;
import java.util.Date;

public class InsuranceClosingReportView extends DTO implements RecordAudit {
    /*    
    -- Table: ins_gl_closing_report

    -- DROP TABLE ins_gl_closing_report;

    CREATE TABLE ins_gl_closing_report
    (
    closing_id bigint NOT NULL,
    policy_date_start timestamp without time zone,
    policy_date_end timestamp without time zone,
    period_start_start timestamp without time zone,
    period_start_end timestamp without time zone,
    reasuradur bigint,
    reasuradur_name character varying(32),
    create_date timestamp without time zone NOT NULL,
    create_who character varying(32) NOT NULL,
    change_date timestamp without time zone,
    change_who character varying(32),
    ri_slip_no character varying(32),
    description text,
    pol_no character varying(32),
    CONSTRAINT ins_gl_closing_report_pkey PRIMARY KEY (closing_id)
    )
    WITH (
    OIDS=FALSE
    );
    ALTER TABLE ins_gl_closing_report
    OWNER TO postgres;
    
     */

    private final static transient LogManager logger = LogManager.getInstance(InsuranceClosingReportView.class);
    public static String tableName = "ins_gl_closing_report";
    public static String comboFields[] = {"ri_slip_no", "no_surat_hutang", "pol_no", "description"};
    public static String fieldMap[][] = {
        {"stClosingID", "closing_id*pk"},
        {"dtPolicyDateStart", "policy_date_start"},
        {"dtPolicyDateEnd", "policy_date_end"},
        {"dtPeriodStartStart", "period_start_start"},
        {"dtPeriodStartEnd", "period_start_end"},
        {"stReasuradurID", "reasuradur_id"},
        {"stRISlipNo", "ri_slip_no"},
        {"stDescription", "description"},
        {"stPolicyNo", "pol_no"},
        {"stNoSuratHutang", "no_surat_hutang"},
        {"stFilePhysic", "file_physic"},
        {"stReasuradurName", "reasuradur_name"},
        {"stPolicyTypeID", "pol_type_id"},
        {"stMonths", "months"},
        {"stYears", "years"},};
    private String stClosingID;
    private String stReasuradurID;
    private String stNoSuratHutang;
    private String stRISlipNo;
    private String stDescription;
    private String stPolicyNo;
    private Date dtPolicyDateStart;
    private Date dtPolicyDateEnd;
    private Date dtPeriodStartStart;
    private Date dtPeriodStartEnd;
    private String stReasuradurName;
    private String stFilePhysic;
    private String stPolicyTypeID;
    private String stMonths;
    private String stYears;

    /**
     * @return the stClosingID
     */
    public String getStClosingID() {
        return stClosingID;
    }

    /**
     * @param stClosingID the stClosingID to set
     */
    public void setStClosingID(String stClosingID) {
        this.stClosingID = stClosingID;
    }

    /**
     * @return the stReasuradur
     */
    public String getStReasuradurID() {
        return stReasuradurID;
    }

    /**
     * @param stReasuradur the stReasuradur to set
     */
    public void setStReasuradurID(String stReasuradurID) {
        this.stReasuradurID = stReasuradurID;
    }

    /**
     * @return the stRISlipNo
     */
    public String getStRISlipNo() {
        return stRISlipNo;
    }

    /**
     * @param stRISlipNo the stRISlipNo to set
     */
    public void setStRISlipNo(String stRISlipNo) {
        this.stRISlipNo = stRISlipNo;
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
     * @return the stPolicyNo
     */
    public String getStPolicyNo() {
        return stPolicyNo;
    }

    /**
     * @param stPolicyNo the stPolicyNo to set
     */
    public void setStPolicyNo(String stPolicyNo) {
        this.stPolicyNo = stPolicyNo;
    }

    /**
     * @return the dtPolicyDateStart
     */
    public Date getDtPolicyDateStart() {
        return dtPolicyDateStart;
    }

    /**
     * @param dtPolicyDateStart the dtPolicyDateStart to set
     */
    public void setDtPolicyDateStart(Date dtPolicyDateStart) {
        this.dtPolicyDateStart = dtPolicyDateStart;
    }

    /**
     * @return the dtPolicyDateEnd
     */
    public Date getDtPolicyDateEnd() {
        return dtPolicyDateEnd;
    }

    /**
     * @param dtPolicyDateEnd the dtPolicyDateEnd to set
     */
    public void setDtPolicyDateEnd(Date dtPolicyDateEnd) {
        this.dtPolicyDateEnd = dtPolicyDateEnd;
    }

    /**
     * @return the dtPeriodStartStart
     */
    public Date getDtPeriodStartStart() {
        return dtPeriodStartStart;
    }

    /**
     * @param dtPeriodStartStart the dtPeriodStartStart to set
     */
    public void setDtPeriodStartStart(Date dtPeriodStartStart) {
        this.dtPeriodStartStart = dtPeriodStartStart;
    }

    /**
     * @return the dtPeriodStartEnd
     */
    public Date getDtPeriodStartEnd() {
        return dtPeriodStartEnd;
    }

    /**
     * @param dtPeriodStartEnd the dtPeriodStartEnd to set
     */
    public void setDtPeriodStartEnd(Date dtPeriodStartEnd) {
        this.dtPeriodStartEnd = dtPeriodStartEnd;
    }

    /**
     * @return the stReasuradurName
     */
    public String getStReasuradurName() {
        return stReasuradurName;
    }

    /**
     * @param stReasuradurName the stReasuradurName to set
     */
    public void setStReasuradurName(String stReasuradurName) {
        this.stReasuradurName = stReasuradurName;
    }

    /**
     * @return the stNoSuratHutang
     */
    public String getStNoSuratHutang() {
        return stNoSuratHutang;
    }

    /**
     * @param stNoSuratHutang the stNoSuratHutang to set
     */
    public void setStNoSuratHutang(String stNoSuratHutang) {
        this.stNoSuratHutang = stNoSuratHutang;
    }

    /**
     * @return the stFilePhysic
     */
    public String getStFilePhysic() {
        return stFilePhysic;
    }

    /**
     * @param stFilePhysic the stFilePhysic to set
     */
    public void setStFilePhysic(String stFilePhysic) {
        this.stFilePhysic = stFilePhysic;
    }

    public EntityView getEntity() {
        return (EntityView) DTOPool.getInstance().getDTORO(EntityView.class, stReasuradurID);
    }

    public InsurancePolicyTypeView getPolicyType() {
        return (InsurancePolicyTypeView) DTOPool.getInstance().getDTORO(InsurancePolicyTypeView.class, stPolicyTypeID);
    }

    /**
     * @return the stPolicyTypeID
     */
    public String getStPolicyTypeID() {
        return stPolicyTypeID;
    }

    /**
     * @param stPolicyTypeID the stPolicyTypeID to set
     */
    public void setStPolicyTypeID(String stPolicyTypeID) {
        this.stPolicyTypeID = stPolicyTypeID;
    }

    /**
     * @return the stMonths
     */
    public String getStMonths() {
        return stMonths;
    }

    /**
     * @param stMonths the stMonths to set
     */
    public void setStMonths(String stMonths) {
        this.stMonths = stMonths;
    }

    /**
     * @return the stYears
     */
    public String getStYears() {
        return stYears;
    }

    /**
     * @param stYears the stYears to set
     */
    public void setStYears(String stYears) {
        this.stYears = stYears;
    }
}
