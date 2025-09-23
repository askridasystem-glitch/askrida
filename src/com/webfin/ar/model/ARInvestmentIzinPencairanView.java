/***********************************************************************
 * Module:  com.webfin.ar.model.ARInvestmentIzinPencairanView
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
import java.math.BigDecimal;

import java.util.Date;

public class ARInvestmentIzinPencairanView extends DTO implements RecordAudit {

    private final static transient LogManager logger = LogManager.getInstance(ARInvestmentIzinPencairanView.class);
    public static String tableName = "ar_izin_pencairan";
    //public static String comboFields[] = {"ar_depo_id","nodefo","register","bukti_b","accountno","description"};
    /*
    -- Table: ar_izin_pencairan
    
    -- DROP TABLE ar_izin_pencairan;
    
    CREATE TABLE ar_izin_pencairan
    (
    ar_izincair_id bigint NOT NULL,
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
    mutation_date timestamp without time zone,
    CONSTRAINT ar_inv_izin_pencairan_pkey PRIMARY KEY (ar_izincair_id)
    )
    WITH (
    OIDS=FALSE
    );
    ALTER TABLE ar_izin_pencairan
    OWNER TO postgres;
    
    )*/
    public static String fieldMap[][] = {
        {"stARIzinCairID", "ar_izincair_id*pk"},
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
        {"dtMutationDate", "mutation_date"},
        {"stFilePhysic", "file_physic"},
        {"dbTotalBilyet", "totalbilyet"},
        {"dbTotalRealisasi", "totalrealisasi"},};
    private String stARIzinCairID;
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
    private Date dtMutationDate;
    private String stFilePhysic;
    private BigDecimal dbTotalBilyet;
    private BigDecimal dbTotalRealisasi;

    public boolean isEffectiveCab() {
        return Tools.isYes(stApprovedCabFlag);
    }

    public boolean isEffectivePus() {
        return Tools.isYes(stApprovedPusFlag);
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

    private DTOList pencairandet;

    /**
     * @return the pencairandet
     */
//    public DTOList getPencairandet() {
//        loadPencairandet();
//        return pencairandet;
//    }

    public DTOList getPencairandet() throws Exception {

        if (pencairandet != null) return pencairandet;
        if (pencairandet == null) pencairandet = loadPencairandet("INVOICE");
        if (pencairandet == null) pencairandet = new DTOList();

        return pencairandet;
    }

    /**
     * @param pencairandet the pencairandet to set
     */
    public void setPencairandet(DTOList pencairandet) {
        this.pencairandet = pencairandet;
    }

//    public void loadPencairandet() {
//        try {
//            if (pencairandet == null) {
//                pencairandet = ListUtil.getDTOListFromQuery(
//                        "select * from ar_izin_pencairan_detail where ar_izincair_id = ? and active_flag = 'Y' order by ar_izincairdet_id ",
//                        new Object[]{stARIzinCairID},
//                        ARInvestmentIzinPencairanDetView.class);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    private DTOList loadPencairandet(String receiptLineType) throws Exception {
        if (stARIzinCairID != null) {
            return ListUtil.getDTOListFromQuery(
                    "select * from  ar_izin_pencairan_detail where ar_izincair_id = ? and line_type = ? and delete_flag is null order by ar_izincairdet_id",
                    new Object[]{stARIzinCairID, receiptLineType},
                    ARInvestmentIzinPencairanDetView.class);
        }

        return null;
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

    public GLCostCenterView3 getCostCenter3() {
        final GLCostCenterView3 costcenter = (GLCostCenterView3) DTOPool.getInstance().getDTO(GLCostCenterView3.class, stCostCenterCode);

        return costcenter;
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

    /**
     * @return the stARIzinCairID
     */
    public String getStARIzinCairID() {
        return stARIzinCairID;
    }

    /**
     * @param stARIzinCairID the stARIzinCairID to set
     */
    public void setStARIzinCairID(String stARIzinCairID) {
        this.stARIzinCairID = stARIzinCairID;
    }
    private DTOList pencairan;

    /**
     * @return the pencairan
     */
    public DTOList getPencairan() {
        loadPencairan();
        return pencairan;
    }

    /**
     * @param pencairan the pencairan to set
     */
    public void setPencairan(DTOList pencairan) {
        this.pencairan = pencairan;
    }

    public void loadPencairan() {
        try {
            if (pencairan == null) {
                pencairan = ListUtil.getDTOListFromQuery(
                        "select * from ar_inv_pencairan where ar_izincair_id = ? and active_flag = 'Y' and deleted is null order by ar_cair_id ",
                        new Object[]{stARIzinCairID},
                        ARInvestmentPencairanView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    /**
     * @return the dbTotalBilyet
     */
    public BigDecimal getDbTotalBilyet() {
        return dbTotalBilyet;
    }

    /**
     * @param dbTotalBilyet the dbTotalBilyet to set
     */
    public void setDbTotalBilyet(BigDecimal dbTotalBilyet) {
        this.dbTotalBilyet = dbTotalBilyet;
    }

    /**
     * @return the dbTotalRealisasi
     */
    public BigDecimal getDbTotalRealisasi() {
        return dbTotalRealisasi;
    }

    /**
     * @param dbTotalRealisasi the dbTotalRealisasi to set
     */
    public void setDbTotalRealisasi(BigDecimal dbTotalRealisasi) {
        this.dbTotalRealisasi = dbTotalRealisasi;
    }
}
