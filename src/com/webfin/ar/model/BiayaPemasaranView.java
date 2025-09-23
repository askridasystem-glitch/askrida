/***********************************************************************
 * Module: com.webfin.ar.model.BiayaPemasaranView
 * Author:  Denny Mahendra
 * Created: Mar 11, 2004 9:41:08 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.file.FileView;
import com.crux.login.model.UserSessionView;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.DateUtil;
import com.crux.util.IDFactory;
import com.crux.util.ListUtil;
import com.crux.util.SQLAssembler;
import com.crux.util.SQLUtil;
import com.crux.util.StringTools;
import com.crux.util.Tools;
import com.webfin.ar.model.ARReceiptClassView;
import com.webfin.gl.model.GLCostCenterView;
import com.webfin.insurance.model.InsurancePolicyDocumentView;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import com.webfin.FinCodec;

public class BiayaPemasaranView extends DTO implements RecordAudit {

    private HashMap attributes = new HashMap();

    public void setAttribute(String x, Object o) {
        attributes.put(x, o);
    }

    public Object getAttribute(String x) {
        return attributes.get(x);
    }

    /*
    -- Table: biaya_pemasaran

    -- DROP TABLE biaya_pemasaran;

    CREATE TABLE biaya_pemasaran
    (
    pms_id bigint NOT NULL,
    no_spp character varying(32),
    create_date timestamp without time zone NOT NULL,
    create_who character varying(32) NOT NULL,
    change_date timestamp without time zone,
    change_who character varying(32),
    ccy character varying(5),
    ccy_rate numeric,
    biaya numeric,
    saldo_biaya numeric,
    status1 character varying(1),
    status2 character varying(1),
    status3 character varying(1),
    status4 character varying(1),
    cc_code character varying(2),
    file_id bigint,
    years character varying(4),
    months character varying(10),
    CONSTRAINT biaya_pemasaran_pkey PRIMARY KEY (pms_id)
    )
    WITH (
    OIDS=FALSE
    );
    ALTER TABLE biaya_pemasaran
    OWNER TO postgres;

    -- Index: biaya_pemasaran_idx

    -- DROP INDEX biaya_pemasaran_idx;

    CREATE INDEX biaya_pemasaran_idx
    ON biaya_pemasaran
    USING btree
    (pms_id);

    -- Index: biaya_pemasaran_spp

    -- DROP INDEX biaya_pemasaran_spp;

    CREATE INDEX biaya_pemasaran_spp
    ON biaya_pemasaran
    USING btree
    (no_spp COLLATE pg_catalog."default");
     */
    public static String tableName = "biaya_pemasaran";
    public static String comboFields[] = {"pms_id", "no_spp", "stJumlahData", "dbTotalBiaya"};
    public transient static String fieldMap[][] = {
        {"stPemasaranID", "pms_id*pk*nd"},
        {"stNoSPP", "no_spp"},
        {"stCurrency", "ccy"},
        {"dbCurr", "ccy_rate"},
        {"dbBiaya", "biaya"},
        {"dbSaldoBiaya", "saldo_biaya"},
        {"stStatus1", "status1"},
        {"stStatus2", "status2"},
        {"stStatus3", "status3"},
        {"stStatus4", "status4"},
        {"stCostCenterCode", "cc_code"},
        {"stFilePhysic", "file_id"},
        {"stYears", "years"},
        {"stMonths", "months"},
        {"stJumlahData", "jumlah_data"},
        {"dbTotalBiaya", "total_biaya"},
        {"stReceiptClassID", "rc_id"},
        {"stAccountID", "accountid"},
        {"stAccountNo", "accountno"},
        {"stAccountDesc", "accountdesc"},
        {"dtEntryDate", "entry_date"},
        {"stNoBukti", "no_bukti"},
        {"stKabagApproved", "kabag_approved"},
        {"stKeterangan", "ket"},
        {"stNoBuktiBayar", "no_bukti_bayar"},
        {"dtTanggalBayar", "tgl_bayar"},
        {"stKodeInput", "kd_input"},
        {"stTransaksi", "transaksi*n"},
        {"stNoSurat", "no_surat"},
        {"stValidasiF", "validasi_f"},
        {"stFileNota", "filenota"},
        {"stFileApp", "fileapp"},
        {"stNotaF", "nota_f"},
        {"dtApprovedDate", "approved_date"},
        {"stKadivApproved", "kadiv_approved"},
    };
    private String stPemasaranID;
    private String stNoSPP;
    private String stCurrency;
    private BigDecimal dbCurr;
    private BigDecimal dbBiaya;
    private BigDecimal dbSaldoBiaya;
    private String stStatus1;
    private String stStatus2;
    private String stStatus3;
    private String stStatus4;
    private String stCostCenterCode;
    private String stFilePhysic;
    private String stYears;
    private String stMonths;
    private String stJumlahData;
    private BigDecimal dbTotalBiaya;
    private String stReceiptClassID;
    private String stAccountID;
    private String stAccountNo;
    private String stAccountDesc;
    private Date dtEntryDate;
    private String stNoBukti;
    private String stKabagApproved;
    private String stKeterangan;
    private String stNoBuktiBayar;
    private Date dtTanggalBayar;
    private String stKodeInput;
    private String stTransaksi;
    private String stNoSurat;
    private String stValidasiF;
    private String stFileNota;
    private String stFileApp;
    private String stNotaF;

    private Date dtApprovedDate;
    private String stKadivApproved;

    public FileView getFile() {

        final FileView file = (FileView) DTOPool.getInstance().getDTO(FileView.class, getStFilePhysic());

        return file;

    }

    /**
     * @return the stStatus1
     */
    public String getStStatus1() {
        return stStatus1;
    }

    /**
     * @param stStatus1 the stStatus1 to set
     */
    public void setStStatus1(String stStatus1) {
        this.stStatus1 = stStatus1;
    }

    /**
     * @return the stStatus2
     */
    public String getStStatus2() {
        return stStatus2;
    }

    /**
     * @param stStatus2 the stStatus2 to set
     */
    public void setStStatus2(String stStatus2) {
        this.stStatus2 = stStatus2;
    }

    /**
     * @return the stStatus3
     */
    public String getStStatus3() {
        return stStatus3;
    }

    /**
     * @param stStatus3 the stStatus3 to set
     */
    public void setStStatus3(String stStatus3) {
        this.stStatus3 = stStatus3;
    }

    /**
     * @return the stStatus4
     */
    public String getStStatus4() {
        return stStatus4;
    }

    /**
     * @param stStatus4 the stStatus4 to set
     */
    public void setStStatus4(String stStatus4) {
        this.stStatus4 = stStatus4;
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

    public boolean isStatus1Flag() {
        return Tools.isYes(stStatus1);
    }

    public boolean isStatus2Flag() {
        return Tools.isYes(stStatus2);
    }

    public boolean isStatus3Flag() {
        return Tools.isYes(stStatus3);
    }

    public boolean isStatus4Flag() {
        return Tools.isYes(stStatus4);
    }

    public GLCostCenterView getCostCenter(String stCostCenterCode) {
        final GLCostCenterView costcenter = (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, stCostCenterCode);

        return costcenter;
    }

    public UserSessionView getUser(String stUserID) {
        return (UserSessionView) DTOPool.getInstance().getDTO(UserSessionView.class, stUserID);
    }

    public SQLAssembler getSQAComm(String receiptid) throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

//        sqa.addSelect(" a.* ");
        sqa.addSelect(" a.pol_no,a.tertanggung,a.amount,a.no_surat_hutang,a.periode_awal,a.periode_akhir,a.cc_code,"
                + " (select x.receipt_no from ins_policy x where x.pol_id = a.ins_pol_id) as reinsurer_note ");

        sqa.addQuery(" from ins_proposal_komisi a ");

        sqa.addClause("a.ins_upload_id = ? ");
        sqa.addPar(receiptid);

        sqa.addOrder("a.ins_upload_dtl_id ");

        return sqa;
    }

    /**
     * @return the stPemasaranID
     */
    public String getStPemasaranID() {
        return stPemasaranID;
    }

    /**
     * @param stPemasaranID the stPemasaranID to set
     */
    public void setStPemasaranID(String stPemasaranID) {
        this.stPemasaranID = stPemasaranID;
    }

    /**
     * @return the stNoSPP
     */
    public String getStNoSPP() {
        return stNoSPP;
    }

    /**
     * @param stNoSPP the stNoSPP to set
     */
    public void setStNoSPP(String stNoSPP) {
        this.stNoSPP = stNoSPP;
    }

    /**
     * @return the stCurrency
     */
    public String getStCurrency() {
        return stCurrency;
    }

    /**
     * @param stCurrency the stCurrency to set
     */
    public void setStCurrency(String stCurrency) {
        this.stCurrency = stCurrency;
    }

    /**
     * @return the dbCurr
     */
    public BigDecimal getDbCurr() {
        return dbCurr;
    }

    /**
     * @param dbCurr the dbCurr to set
     */
    public void setDbCurr(BigDecimal dbCurr) {
        this.dbCurr = dbCurr;
    }

    /**
     * @return the dbBiaya
     */
    public BigDecimal getDbBiaya() {
        return dbBiaya;
    }

    /**
     * @param dbBiaya the dbBiaya to set
     */
    public void setDbBiaya(BigDecimal dbBiaya) {
        this.dbBiaya = dbBiaya;
    }

    /**
     * @return the dbSaldoBiaya
     */
    public BigDecimal getDbSaldoBiaya() {
        return dbSaldoBiaya;
    }

    /**
     * @param dbSaldoBiaya the dbSaldoBiaya to set
     */
    public void setDbSaldoBiaya(BigDecimal dbSaldoBiaya) {
        this.dbSaldoBiaya = dbSaldoBiaya;
    }

    /**
     * @return the stFileID
     */
    public String getStFilePhysic() {
        return stFilePhysic;
    }

    /**
     * @param stFileID the stFileID to set
     */
    public void setStFilePhysic(String stFilePhysic) {
        this.stFilePhysic = stFilePhysic;
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
    private DTOList pmsDetail;

    /**
     * @return the pmsDetail
     */
    public DTOList getPmsDetail() {
        loadPmsDetail();
        return pmsDetail;
    }

    /**
     * @param pmsDetail the pmsDetail to set
     */
    public void setPmsDetail(DTOList pmsDetail) {
        this.pmsDetail = pmsDetail;
    }

    public void loadPmsDetail() {
        try {
            if (pmsDetail == null) {
                pmsDetail = ListUtil.getDTOListFromQuery(
                        "select * from biaya_pemasaran_detail where pms_id = ? and delete_flag is null order by pms_det_id ",
                        new Object[]{stPemasaranID},
                        BiayaPemasaranDetailView.class);

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
     * @return the stAccountID
     */
    public String getStAccountID() {
        return stAccountID;
    }

    /**
     * @param stAccountID the stAccountID to set
     */
    public void setStAccountID(String stAccountID) {
        this.stAccountID = stAccountID;
    }

    /**
     * @return the stAccountNo
     */
    public String getStAccountNo() {
        return stAccountNo;
    }

    /**
     * @param stAccountNo the stAccountNo to set
     */
    public void setStAccountNo(String stAccountNo) {
        this.stAccountNo = stAccountNo;
    }

    /**
     * @return the stAccountDesc
     */
    public String getStAccountDesc() {
        return stAccountDesc;
    }

    /**
     * @param stAccountDesc the stAccountDesc to set
     */
    public void setStAccountDesc(String stAccountDesc) {
        this.stAccountDesc = stAccountDesc;
    }

    /**
     * @return the dtEntryDate
     */
    public Date getDtEntryDate() {
        return dtEntryDate;
    }

    /**
     * @param dtEntryDate the dtEntryDate to set
     */
    public void setDtEntryDate(Date dtEntryDate) {
        this.dtEntryDate = dtEntryDate;
    }

    public boolean isPosted() throws Exception {

        SQLUtil S = new SQLUtil();

        boolean isPosted = false;

        try {
            String cek = "select gl_post_id from gl_posting where months = ? and years = ? and posted_flag = 'Y'";

            if (getStCostCenterCode() != null) {
                cek = cek + " and cc_code = ?";
            }

            PreparedStatement PS = S.setQuery(cek);
            PS.setString(1, getStMonths());
            PS.setString(2, getStYears());

            if (getStCostCenterCode() != null) {
                PS.setString(3, getStCostCenterCode());
            }

            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                isPosted = true;
            }

        } finally {
            S.release();
        }

        return isPosted;
    }

    /**
     * @return the stJumlahData
     */
    public String getStJumlahData() {
        return stJumlahData;
    }

    /**
     * @param stJumlahData the stJumlahData to set
     */
    public void setStJumlahData(String stJumlahData) {
        this.stJumlahData = stJumlahData;
    }

    /**
     * @return the dbTotalBiaya
     */
    public BigDecimal getDbTotalBiaya() {
        return dbTotalBiaya;
    }

    /**
     * @param dbTotalBiaya the dbTotalBiaya to set
     */
    public void setDbTotalBiaya(BigDecimal dbTotalBiaya) {
        this.dbTotalBiaya = dbTotalBiaya;
    }

    public ARReceiptClassView getReceiptclass() {
        return (ARReceiptClassView) (stReceiptClassID == null ? null : DTOPool.getInstance().getDTO(ARReceiptClassView.class, stReceiptClassID));
    }

    public void generateNoBukti() throws Exception {

        final String ccCode = Tools.getDigitRightJustified(getStCostCenterCode(), 2);
        String bankCode = null;
        if (getStAccountID() != null) {
            bankCode = getStAccountNo().substring(5, 10);
        }

        String counterKey = DateUtil.getYear2Digit(getDtEntryDate()) + DateUtil.getMonth2Digit(getDtEntryDate());

        String rn = String.valueOf(IDFactory.createNumericID("RCPNO" + counterKey + ccCode, 1));

        //StringTools.leftPad(String.valueOf(IDFactory.createNumericID("PP" + policyType2Digit + year2Digit + ccCode, 1)), '0', 4) + //H

        rn = StringTools.leftPad(rn, '0', 5);

        stNoBukti =
                getStReceiptClassID()
                + counterKey
                + ccCode
                + ccCode
                + bankCode
                + rn;

        //setStBuktiB(stNodefo);
        //setStRegister(stRegister);
    }

    /**
     * @return the stNoBukti
     */
    public String getStNoBukti() {
        return stNoBukti;
    }

    /**
     * @param stNoBukti the stNoBukti to set
     */
    public void setStNoBukti(String stNoBukti) {
        this.stNoBukti = stNoBukti;
    }

    /**
     * @return the stKabagApproved
     */
    public String getStKabagApproved() {
        return stKabagApproved;
    }

    /**
     * @param stKabagApproved the stKabagApproved to set
     */
    public void setStKabagApproved(String stKabagApproved) {
        this.stKabagApproved = stKabagApproved;
    }

    /**
     * @return the stKeterangan
     */
    public String getStKeterangan() {
        return stKeterangan;
    }

    /**
     * @param stKeterangan the stKeterangan to set
     */
    public void setStKeterangan(String stKeterangan) {
        this.stKeterangan = stKeterangan;
    }

    /**
     * @return the stNoBuktiBayar
     */
    public String getStNoBuktiBayar() {
        return stNoBuktiBayar;
    }

    /**
     * @param stNoBuktiBayar the stNoBuktiBayar to set
     */
    public void setStNoBuktiBayar(String stNoBuktiBayar) {
        this.stNoBuktiBayar = stNoBuktiBayar;
    }

    public void generateNoBuktiBayar(Date dtEntryBayar) throws Exception {

        final String ccCode = Tools.getDigitRightJustified(getStCostCenterCode(), 2);
        String bankCode = null;
        if (getStAccountID() != null) {
            bankCode = getStAccountNo().substring(5, 10);
        }

//        String counterKey = DateUtil.getYear2Digit(new Date()) + DateUtil.getMonth2Digit(new Date());
        String counterKey = DateUtil.getYear2Digit(dtEntryBayar) + DateUtil.getMonth2Digit(dtEntryBayar);

        String rn = String.valueOf(IDFactory.createNumericID("RCPNO" + counterKey + ccCode, 1));

        //StringTools.leftPad(String.valueOf(IDFactory.createNumericID("PP" + policyType2Digit + year2Digit + ccCode, 1)), '0', 4) + //H

        rn = StringTools.leftPad(rn, '0', 5);

        stNoBuktiBayar =
                getStReceiptClassID()
                + counterKey
                + ccCode
                + ccCode
                + bankCode
                + rn;

        //setStBuktiB(stNodefo);
        //setStRegister(stRegister);
    }
    private DTOList documents;

    public DTOList getDocuments() {
        loadDocuments();
        return documents;
    }

    private void loadDocuments() {
        try {
            if (documents == null) {
                documents = ListUtil.getDTOListFromQuery(
                        "select * from biaya_pemasaran_doc where pms_id = ? and delete_flag is null order by doc_pms_id ",
                        new Object[]{stPemasaranID},
                        BiayaPemasaranDocumentsView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setDocuments(DTOList documents) {
        this.documents = documents;
    }

    /**
     * @return the dtTanggalBayar
     */
    public Date getDtTanggalBayar() {
        return dtTanggalBayar;
    }

    /**
     * @param dtTanggalBayar the dtTanggalBayar to set
     */
    public void setDtTanggalBayar(Date dtTanggalBayar) {
        this.dtTanggalBayar = dtTanggalBayar;
    }

    /**
     * @return the stKodeInput
     */
    public String getStKodeInput() {
        return stKodeInput;
    }

    /**
     * @param stKodeInput the stKodeInput to set
     */
    public void setStKodeInput(String stKodeInput) {
        this.stKodeInput = stKodeInput;
    }

    public boolean isTunai() {
        return getStKodeInput().equalsIgnoreCase("1");
    }

    public boolean isNonTunai() {
        return getStKodeInput().equalsIgnoreCase("2");
    }

    /**
     * @return the stTransaksi
     */
    public String getStTransaksi() {
        return stTransaksi;
    }

    /**
     * @param stTransaksi the stTransaksi to set
     */
    public void setStTransaksi(String stTransaksi) {
        this.stTransaksi = stTransaksi;
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
     * @return the stValidasiF
     */
    public String getStValidasiF() {
        return stValidasiF;
    }

    /**
     * @param stValidasiF the stValidasiF to set
     */
    public void setStValidasiF(String stValidasiF) {
        this.stValidasiF = stValidasiF;
    }

    public boolean isValidasiFlag() {
        return Tools.isYes(stValidasiF);
    }

    /**
     * @return the stFileNota
     */
    public String getStFileNota() {
        return stFileNota;
    }

    /**
     * @param stFileNota the stFileNota to set
     */
    public void setStFileNota(String stFileNota) {
        this.stFileNota = stFileNota;
    }

    /**
     * @return the stFileApp
     */
    public String getStFileApp() {
        return stFileApp;
    }

    /**
     * @param stFileApp the stFileApp to set
     */
    public void setStFileApp(String stFileApp) {
        this.stFileApp = stFileApp;
    }

    public boolean isExOnePersen() {
        return getStFileApp() != null;
    }

    /**
     * @return the stNotaF
     */
    public String getStNotaF() {
        return stNotaF;
    }

    /**
     * @param stNotaF the stNotaF to set
     */
    public void setStNotaF(String stNotaF) {
        this.stNotaF = stNotaF;
    }

    public String getMonths() {
        return (String) FinCodec.MonthPeriods.getLookUp().getValue(stMonths);
    }

    public Date getDtApprovedDate() {
        return dtApprovedDate;
    }

    /**
     * @param dtApprovedDate the dtApprovedDate to set
     */
    public void setDtApprovedDate(Date dtApprovedDate) {
        this.dtApprovedDate = dtApprovedDate;
    }

    /**
     * @return the stKadivApproved
     */
    public String getStKadivApproved() {
        return stKadivApproved;
    }

    /**
     * @param stKadivApproved the stKadivApproved to set
     */
    public void setStKadivApproved(String stKadivApproved) {
        this.stKadivApproved = stKadivApproved;
    }
    
}
