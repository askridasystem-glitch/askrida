/***********************************************************************
 * Module:  com.crux.jobs.model.JobConfig
 * Author:  Denny Mahendra
 * Created: Jun 15, 2004 5:57:00 PM
 * Purpose:
 ***********************************************************************/

package com.crux.jobs.model;

import com.crux.common.jobs.*;
import com.crux.common.model.DTO;
import com.crux.util.DateUtil;
import com.crux.util.LookUpUtil;

import java.util.Date;

public class JobConfigView extends DTO {
    public static final class Jobs {
        public final static transient String CODEGROUP = "CODE";

        private static LookUpUtil lookUp = null;

        public static final String SHELL_EXEC = "SHELL_EXEC";
        public static final String SHELL_EXEC_2 = "SHELL_EXEC_2";
        public static final String SHELL_EXEC_3 = "SHELL_EXEC_3";
        public static final String SHELL_EXEC_4 = "SHELL_EXEC_4";
        public static final String SHELL_EXEC_5 = "SHELL_EXEC_5";
        public static final String SHELL_EXEC_6 = "SHELL_EXEC_6";
        public static final String SHELL_EXEC_7 = "SHELL_EXEC_7";
        public static final String SHELL_EXEC_8 = "SHELL_EXEC_8";
        public static final String SHELL_EXEC_9 = "SHELL_EXEC_9";
        public static final String SHELL_EXEC_10 = "SHELL_EXEC_10";
        public static final String SHELL_EXEC_11 = "SHELL_EXEC_11";
        public static final String SHELL_EXEC_12 = "SHELL_EXEC_12";
        public static final String SHELL_EXEC_13 = "SHELL_EXEC_13";
        public static final String SHELL_EXEC_16 = "SHELL_EXEC_16";
        public static final String SHELL_EXEC_17 = "SHELL_EXEC_17";
        public static final String SHELL_EXEC_18 = "SHELL_EXEC_18";
        public static final String SHELL_EXEC_19 = "SHELL_EXEC_19";
        public static final String SHELL_EXEC_20 = "SHELL_EXEC_20";
        public static final String SHELL_EXEC_21 = "SHELL_EXEC_21";
        public static final String SHELL_EXEC_22 = "SHELL_EXEC_22";
        public static final String SHELL_EXEC_23 = "SHELL_EXEC_23";
        public static final String SHELL_EXEC_24 = "SHELL_EXEC_24";
        public static final String SHELL_EXEC_25 = "SHELL_EXEC_25";
        public static final String SHELL_EXEC_26 = "SHELL_EXEC_26";


        public static LookUpUtil getHOJobs() {
            return getLookUp().getSubSetOf(new String[]{
                       SHELL_EXEC_26,SHELL_EXEC_25,SHELL_EXEC_24,SHELL_EXEC_23,SHELL_EXEC_22,SHELL_EXEC_21,SHELL_EXEC_20,SHELL_EXEC_19,SHELL_EXEC_18,SHELL_EXEC_17,SHELL_EXEC_16,SHELL_EXEC_13,SHELL_EXEC_12,SHELL_EXEC_11,SHELL_EXEC_10,SHELL_EXEC_4,SHELL_EXEC_3,SHELL_EXEC_2,SHELL_EXEC,
            });
        }

        public static LookUpUtil getBranchJobs() {
            return getLookUp().getSubSetOf(new String[]{
                        SHELL_EXEC_26,SHELL_EXEC_25,SHELL_EXEC_24,SHELL_EXEC_23,SHELL_EXEC_22,SHELL_EXEC_21,SHELL_EXEC_20,SHELL_EXEC_19,SHELL_EXEC_18,SHELL_EXEC_17,SHELL_EXEC_16,SHELL_EXEC_13,SHELL_EXEC_12,SHELL_EXEC_11,SHELL_EXEC_10,SHELL_EXEC_9,SHELL_EXEC_8,SHELL_EXEC_7,SHELL_EXEC_6,SHELL_EXEC_5,SHELL_EXEC_4,SHELL_EXEC_3,SHELL_EXEC_2,SHELL_EXEC,
            });
        }

        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                        .add(SHELL_EXEC_26, new Object[]{"PostgreSQL - Kill Idle Connection", PostgresKillIdleConnection.class})
                        .add(SHELL_EXEC_25, new Object[]{"SPPA Online - Pengajuan Polis", ProsesSPPAOnlineToCore.class})
                        .add(SHELL_EXEC_24, new Object[]{"Pengajuan Proposal Interkoneksi", ProsesPengajuanProposalInterkoneksi.class})
                        .add(SHELL_EXEC_23, new Object[]{"Proses Klaim H2H Auto Approve", ProsesPengajuanKlaimAutoApprove.class})
                        .add(SHELL_EXEC_22, new Object[]{"Pengajuan Subrogasi", ProsesPengajuanSubrogasi.class})
                        .add(SHELL_EXEC_21, new Object[]{"Email Otomatis FAC", ReceiptFacEmail.class})
                        .add(SHELL_EXEC_20, new Object[]{"Warning Piutang Premi New", WarningPiutangPremiNew.class})
                        .add(SHELL_EXEC_19, new Object[]{"Closing Produksi", ClosingProduksi.class})
                        .add(SHELL_EXEC_18, new Object[]{"Warning Piutang Premi", WarningPiutangPremi.class})
                        .add(SHELL_EXEC_17, new Object[]{"Pengajuan Polis Refund", ProsesPolisRefund.class})
                        .add(SHELL_EXEC_16, new Object[]{"Reminder PKS", MonitoringPKS.class})
                        .add(SHELL_EXEC_13, new Object[]{"Validate Policy", ValidatePolicy.class})
                        .add(SHELL_EXEC_12, new Object[]{"Auto Report SOA", AutoReportSOA.class})
                        .add(SHELL_EXEC_11, new Object[]{"New Deposito", NewDeposito.class})
                        .add(SHELL_EXEC_10, new Object[]{"Auto Report", AutoReporting.class})
                        .add(SHELL_EXEC_9, new Object[]{"Neraca Android", NeracaTotalReport.class})
                        .add(SHELL_EXEC_8, new Object[]{"Validasi Outstanding", ValidateReceipt.class})
                        .add(SHELL_EXEC_7, new Object[]{"Proses File Teks Pada FTP", ProsesTeksFileFromFTP.class})
                        .add(SHELL_EXEC_6, new Object[]{"Pengajuan Endorse H2H & Interkoneksi", ProsesPengajuanEndorse.class})
                        .add(SHELL_EXEC_5, new Object[]{"Pengajuan Klaim H2H & Interkoneksi", ProsesPengajuanKlaim.class})
                        .add(SHELL_EXEC_4, new Object[]{"Pengajuan Polis H2H", ProsesPengajuanPolis.class})
                        .add(SHELL_EXEC_3, new Object[]{"Perpanjangan Deposito", RenewalDeposito.class})
                        .add(SHELL_EXEC_2, new Object[]{"Delete Konversi File", DeleteKonversiJob.class})
                        .add(SHELL_EXEC, new Object[]{"Vacuum Database", PostgresVacuumJob.class})
                        ;
            }
            return lookUp;
        }
    }

    public static final class TriggerMode {
        public final static transient String CODEGROUP = "CODE";

        private static LookUpUtil lookUp = null;

        public final static transient String SIMPLE_TRIGGER = "SIMPL";
        public final static transient String CRON_TRIGGER = "CRON";
        public final static transient String STARTUP = "START";
        public final static transient String SHUTDOWN = "STOP";

        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                        .add(SIMPLE_TRIGGER, "Interval")
                        .add(CRON_TRIGGER, "Cron")
                        //.add(STARTUP, "Startup")
                        //.add(SHUTDOWN, "Shutdown")
                        ;
            }

            return lookUp;
        }
    }


    public static final class Enabled {
        public final static transient String CODEGROUP = "CODE";

        private static LookUpUtil lookUp = null;

        public final static transient String ENABLED = "Y";
        public final static transient String DISABLED = "N";

        public static LookUpUtil getLookUp() {
            if (lookUp == null) {
                lookUp = new LookUpUtil()
                        .add(ENABLED, "Enabled")
                        .add(DISABLED, "Disabled")
                        ;
            }

            return lookUp;
        }
    }


    private String stJobID;
    private String stJobEvent;
    private String stEnabled;
    private String stTriggerMode;
    private Date dtStart;
    private Date dtEnd;
    private Long lgRepeatNum;
    private Long lgInterval;
    private String stCRONExpression;
    private String stParam1;
    private String stParam2;
    private String stParam3;
    private String stParam4;
    private String stParam5;
    private Integer itRetry;
    private String stStarterJobID;
    private String stJobLastStatus;
    private Integer itStarterJobInterval;
    private Integer itRetryInterval;

    public static String tableName = "job_config";

    public static String fieldMap[][] = {
        {"stJobID", "job_id*pk"},
        {"stJobEvent", "job_event"},
        {"stEnabled", "enabled_flag"},
        {"stTriggerMode", "trigger_mode"},
        {"dtStart", "startjob"},
        {"dtEnd", "stopjob"},
        {"lgRepeatNum", "repeat_num"},
        {"lgInterval", "interval"},
        {"stCRONExpression", "cronx"},
        {"itRetry", "retry_count"},
        {"stStarterJobID", "job_starter"},
        {"itRetryInterval", "retry_interval"},
        {"itStarterJobInterval", "job_starter_interval"},
        {"stParam1", "job_param1"},
        {"stParam2", "job_param2"},
        {"stParam3", "job_param3"},
        {"stParam4", "job_param4"},
        {"stParam5", "job_param5"},
        {"stJobLastStatus", "job_last_status*n"},
    };

    public String getStJobID() {
        return stJobID;
    }

    public void setStJobID(String stJobID) {
        this.stJobID = stJobID;
    }

    public String getStEnabled() {
        return stEnabled;
    }

    public void setStEnabled(String stEnabled) {
        this.stEnabled = stEnabled;
    }

    public String getStTriggerMode() {
        return stTriggerMode;
    }

    public void setStTriggerMode(String stTriggerMode) {
        this.stTriggerMode = stTriggerMode;
    }

    public Date getDtStart() {
        return dtStart;
    }

    public void setDtStart(Date dtStart) {
        this.dtStart = dtStart;
    }

    public Date getDtEnd() {
        return dtEnd;
    }

    public void setDtEnd(Date dtEnd) {
        this.dtEnd = dtEnd;
    }

    public Long getLgRepeatNum() {
        return lgRepeatNum;
    }

    public void setLgRepeatNum(Long lgRepeatNum) {
        this.lgRepeatNum = lgRepeatNum;
    }

    public Long getLgInterval() {
        return lgInterval;
    }

    public void setLgInterval(Long lgInterval) {
        this.lgInterval = lgInterval;
    }

    public String getStJobEvent() {
        return stJobEvent;
    }

    public void setStJobEvent(String stJobEvent) {
        this.stJobEvent = stJobEvent;
    }

    public String getStCRONExpression() {
        return stCRONExpression;
    }

    public void setStCRONExpression(String stCRONExpression) {
        this.stCRONExpression = stCRONExpression;
    }

    public String getStStarterJobID() {
        return stStarterJobID;
    }

    public void setStStarterJobID(String stStarterJobID) {
        this.stStarterJobID = stStarterJobID;
    }

    public Integer getItRetry() {
        return itRetry;
    }

    public void setItRetry(Integer itRetry) {
        this.itRetry = itRetry;
    }

    public Integer getItStarterJobInterval() {
        return itStarterJobInterval;
    }

    public void setItStarterJobInterval(Integer itStarterJobInterval) {
        this.itStarterJobInterval = itStarterJobInterval;
    }

    public Integer getItRetryInterval() {
        return itRetryInterval;
    }

    public void setItRetryInterval(Integer itRetryInterval) {
        this.itRetryInterval = itRetryInterval;
    }

    public String getStSchedulingMode() {
        if (TriggerMode.CRON_TRIGGER.equalsIgnoreCase(stTriggerMode)) {
            return "CRON:" + stCRONExpression;
        } else if (TriggerMode.SIMPLE_TRIGGER.equalsIgnoreCase(stTriggerMode)) {
            final StringBuffer sz = new StringBuffer();
            sz.append("SIMPLE");
            if (dtStart != null) sz.append(":start=").append(DateUtil.getDateStr(dtStart));

            return sz.toString();
        }

        return null;
    }

    public String getStParam1() {
        return stParam1;
    }

    public void setStParam1(String stParam1) {
        this.stParam1 = stParam1;
    }

    public String getStParam2() {
        return stParam2;
    }

    public void setStParam2(String stParam2) {
        this.stParam2 = stParam2;
    }

    public String getStParam3() {
        return stParam3;
    }

    public void setStParam3(String stParam3) {
        this.stParam3 = stParam3;
    }

    public String getStParam4() {
        return stParam4;
    }

    public void setStParam4(String stParam4) {
        this.stParam4 = stParam4;
    }

    public String getStParam5() {
        return stParam5;
    }

    public void setStParam5(String stParam5) {
        this.stParam5 = stParam5;
    }

    public String getStJobLastStatus() {
        return stJobLastStatus;
    }

    public void setStJobLastStatus(String stJobLastStatus) {
        this.stJobLastStatus = stJobLastStatus;
    }
}
