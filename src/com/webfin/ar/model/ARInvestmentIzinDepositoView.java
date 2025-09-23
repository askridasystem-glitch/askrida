/***********************************************************************
 * Module:  com.webfin.ar.model.ARInvestmentIzinDepositoView
 * Author:  Denny Mahendra
 * Created: Oct 11, 2005 10:34:51 PM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.common.parameter.Parameter;
import com.crux.login.model.UserSessionView;
import com.crux.pool.DTOPool;
import com.crux.util.*;
import com.webfin.gl.model.GLCostCenterView3;

import java.util.Date;

public class ARInvestmentIzinDepositoView extends DTO implements RecordAudit {

    private final static transient LogManager logger = LogManager.getInstance(ARInvestmentIzinDepositoView.class);
    public static String tableName = "ar_izin_deposito";
//    public static String comboFields[] = {"ar_depo_id","bukti_b","nodefo","register","accountno","description"};
    /*
    -- Table: ar_izin_deposito

    -- DROP TABLE ar_izin_deposito;

    CREATE TABLE ar_izin_deposito
    (
    ar_izin_id bigint NOT NULL,
    years character varying(4),
    months character varying(2),
    no_surat character varying(32),
    create_date timestamp without time zone NOT NULL,
    create_who character varying(32) NOT NULL,
    change_date timestamp without time zone,
    change_who character varying(32),
    active_flag character varying(1),
    print_flag character varying(1),
    cc_code character varying(2),
    approvedcab_flag character varying(1),
    approvedcab_who character varying(10),
    approvedcab_date timestamp without time zone,
    approvedpus_flag character varying(1),
    approvedpus_who character varying(10),
    approvedpus_date timestamp without time zone,
    rc_id bigint,
    comp_type character varying(2),
    mutation_date timestamp without time zone,
    nodefo character varying(32),
    CONSTRAINT ar_izin_deposito PRIMARY KEY (ar_izin_id)
    )
    WITH (
    OIDS=FALSE
    );
    ALTER TABLE ar_izin_deposito
    OWNER TO postgres;

    )*/
    public static String fieldMap[][] = {
        {"stARIzinID", "ar_izin_id*pk"},
        {"StYears", "years"},
        {"StMonths", "months"},
        {"stNoSurat", "no_surat"},
        {"stActiveFlag", "active_flag"},
        {"stPrintFlag", "print_flag"},
        {"stCostCenterCode", "cc_code"},
        {"stApprovedCabFlag", "approvedcab_flag"},
        {"stApprovedCabWho", "approvedcab_who"},
        {"dtApprovedCabDate", "approvedcab_date"},
        {"stApprovedPusFlag", "approvedpus_flag"},
        {"stApprovedPusWho", "approvedpus_who"},
        {"dtApprovedPusDate", "approvedpus_date"},
        {"stReceiptClassID", "rc_id"},
        {"stCompanyType", "comp_type"},
        {"dtMutationDate", "mutation_date"},
        {"stNodefoSementara", "nodefo_sementara"},};
    private String stARIzinID;
    private String StYears;
    private String StMonths;
    private String stNoSurat;
    private String stActiveFlag;
    private String stPrintFlag;
    private String stCostCenterCode;
    private String stApprovedCabFlag;
    private String stApprovedCabWho;
    private Date dtApprovedCabDate;
    private String stApprovedPusFlag;
    private String stApprovedPusWho;
    private Date dtApprovedPusDate;
    private String stReceiptClassID;
    private String stCompanyType;
    private Date dtMutationDate;
    private String stNodefoSementara;

    public boolean isEffectiveCab() {
        return Tools.isYes(stApprovedCabFlag);
    }

    public boolean isEffectivePus() {
        return Tools.isYes(stApprovedPusFlag);
    }

    /**
     * @return the stARIzinID
     */
    public String getStARIzinID() {
        return stARIzinID;
    }

    /**
     * @param stARIzinID the stARIzinID to set
     */
    public void setStARIzinID(String stARIzinID) {
        this.stARIzinID = stARIzinID;
    }

    /**
     * @return the StYears
     */
    public String getStYears() {
        return StYears;
    }

    /**
     * @param StYears the StYears to set
     */
    public void setStYears(String StYears) {
        this.StYears = StYears;
    }

    /**
     * @return the StMonths
     */
    public String getStMonths() {
        return StMonths;
    }

    /**
     * @param StMonths the StMonths to set
     */
    public void setStMonths(String StMonths) {
        this.StMonths = StMonths;
    }

    /**
     * @return the stNoSurat
     */
    public String getStNoSurat() {
        return stNoSurat;
    }

    /**
     * @param stNoSurat the stNoSurat to set
     */
    public void setStNoSurat(String stNoSurat) {
        this.stNoSurat = stNoSurat;
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
     * @return the stApprovedCabFlag
     */
    public String getStApprovedCabFlag() {
        return stApprovedCabFlag;
    }

    /**
     * @param stApprovedCabFlag the stApprovedCabFlag to set
     */
    public void setStApprovedCabFlag(String stApprovedCabFlag) {
        this.stApprovedCabFlag = stApprovedCabFlag;
    }

    /**
     * @return the stApprovedCabWho
     */
    public String getStApprovedCabWho() {
        return stApprovedCabWho;
    }

    /**
     * @param stApprovedCabWho the stApprovedCabWho to set
     */
    public void setStApprovedCabWho(String stApprovedCabWho) {
        this.stApprovedCabWho = stApprovedCabWho;
    }

    /**
     * @return the stApprovedPusFlag
     */
    public String getStApprovedPusFlag() {
        return stApprovedPusFlag;
    }

    /**
     * @param stApprovedPusFlag the stApprovedPusFlag to set
     */
    public void setStApprovedPusFlag(String stApprovedPusFlag) {
        this.stApprovedPusFlag = stApprovedPusFlag;
    }

    /**
     * @return the stApprovedPusWho
     */
    public String getStApprovedPusWho() {
        return stApprovedPusWho;
    }

    /**
     * @param stApprovedPusWho the stApprovedPusWho to set
     */
    public void setStApprovedPusWho(String stApprovedPusWho) {
        this.stApprovedPusWho = stApprovedPusWho;
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
    private DTOList deposito;

    /**
     * @return the sppdPegawai
     */
    public DTOList getDeposito() {
        loadDeposito();
        return deposito;
    }

    /**
     * @param sppdPegawai the sppdPegawai to set
     */
    public void setDeposito(DTOList deposito) {
        this.deposito = deposito;
    }

    public void loadDeposito() {
        try {
            if (deposito == null) {
                deposito = ListUtil.getDTOListFromQuery(
                        "select * from ar_inv_deposito where ar_izin_id = ? and active_flag = 'Y' order by ar_depo_id ",
                        new Object[]{stARIzinID},
                        ARInvestmentDepositoIndexView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the stReceiptClassID
     */
    public String getStReceiptClassID() {
        return stReceiptClassID;
    }

    /**
     * @param stReceiptClassID the stReceiptClassID to set
     */
    public void setStReceiptClassID(String stReceiptClassID) {
        this.stReceiptClassID = stReceiptClassID;
    }

    /**
     * @return the stCompanyType
     */
    public String getStCompanyType() {
        return stCompanyType;
    }

    /**
     * @param stCompanyType the stCompanyType to set
     */
    public void setStCompanyType(String stCompanyType) {
        this.stCompanyType = stCompanyType;
    }

    /**
     * @return the dtApprovedCabDate
     */
    public Date getDtApprovedCabDate() {
        return dtApprovedCabDate;
    }

    /**
     * @param dtApprovedCabDate the dtApprovedCabDate to set
     */
    public void setDtApprovedCabDate(Date dtApprovedCabDate) {
        this.dtApprovedCabDate = dtApprovedCabDate;
    }

    /**
     * @return the dtApprovedPusDate
     */
    public Date getDtApprovedPusDate() {
        return dtApprovedPusDate;
    }

    /**
     * @param dtApprovedPusDate the dtApprovedPusDate to set
     */
    public void setDtApprovedPusDate(Date dtApprovedPusDate) {
        this.dtApprovedPusDate = dtApprovedPusDate;
    }

    /**
     * @return the dtMutationDate
     */
    public Date getDtMutationDate() {
        return dtMutationDate;
    }

    /**
     * @param dtMutationDate the dtMutationDate to set
     */
    public void setDtMutationDate(Date dtMutationDate) {
        this.dtMutationDate = dtMutationDate;
    }

    public GLCostCenterView3 getCostCenter3() {
        final GLCostCenterView3 costcenter = (GLCostCenterView3) DTOPool.getInstance().getDTO(GLCostCenterView3.class, stCostCenterCode);

        return costcenter;
    }

    /**
     * @return the stNodefoSementara
     */
    public String getStNodefoSementara() {
        return stNodefoSementara;
    }

    /**
     * @param stNodefoSementara the stNodefoSementara to set
     */
    public void setStNodefoSementara(String stNodefoSementara) {
        this.stNodefoSementara = stNodefoSementara;
    }

    private DTOList depodetail;

    public void loadDepodetail() {
        try {
            if (depodetail == null) {
                depodetail = ListUtil.getDTOListFromQuery(
                        "select * from ar_izin_deposito_detail where ar_izin_id = ? and active_flag = 'Y' and delete_flag is null order by ar_depo_id ",
                        new Object[]{stARIzinID},
                        ARInvestmentIzinDepositoDetailView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the depodetail
     */
    public DTOList getDepodetail() {
        loadDepodetail();
        return depodetail;
    }

    /**
     * @param depodetail the depodetail to set
     */
    public void setDepodetail(DTOList depodetail) {
        this.depodetail = depodetail;
    }

    public String getUserIDSignName() {

        String NIPPimpinan = Parameter.readString("BRANCH_" + getStCostCenterCode());

//        String NIPPimpinan = getStApprovedWho();

        if (stApprovedCabWho == null) {
            stApprovedCabWho = NIPPimpinan;
        }

        UserSessionView user = getUser(stApprovedCabWho);
        UserSessionView userPimpinan = getUser(NIPPimpinan);

        String useridSignName = userPimpinan.getStUserName();


        if (!NIPPimpinan.equalsIgnoreCase(stApprovedCabWho)) {
            if (user.getStBranch() != null) {
                if (user.getStBranch().equalsIgnoreCase(getUser(NIPPimpinan).getStBranch())) {
                    if (user.hasSignAuthority()) {
                        useridSignName = user.getStUserName();
                    }
                }
            }
        }

        return useridSignName.toUpperCase();
    }

    public UserSessionView getUser(String stUserID) {
        return (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, stUserID);
    }
}
