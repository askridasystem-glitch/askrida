/***********************************************************************
 * Module:  com.crux.login.model.uploadReinsuranceSpreadingView
 * Author:  Denny Mahendra
 * Created: Mar 11, 2004 9:41:08 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.file.FileView;
import com.crux.login.model.UserSessionView;
import com.crux.pool.DTOPool;
import com.crux.util.SQLAssembler;
import com.crux.util.Tools;
import com.webfin.ar.model.ARInvoiceDetailView;
import com.webfin.ar.model.ARInvoiceView;
import com.webfin.gl.model.GLCostCenterView;
import java.math.BigDecimal;

import java.util.Date;
import java.util.HashMap;

public class uploadProposalCommView extends DTO implements RecordAudit {

    private HashMap attributes = new HashMap();

    public void setAttribute(String x, Object o) {
        attributes.put(x, o);
    }

    public Object getAttribute(String x) {
        return attributes.get(x);
    }

    /*
    -- Table: ins_proposal_komisi

    -- DROP TABLE ins_proposal_komisi;

    CREATE TABLE ins_proposal_komisi
    (
    ins_upload_dtl_id bigint NOT NULL,
    ins_upload_id bigint,
    ins_pol_id bigint,
    ins_ar_invoice_id bigint,
    pol_no character varying(32),
    order_no bigint,
    tertanggung character varying(255),
    periode_awal timestamp without time zone,
    periode_akhir timestamp without time zone,
    amount numeric,
    status character varying(32),
    file_id character varying(255),
    no_surat_hutang character varying(255),
    amount_total numeric,
    data_amount bigint,
    file_physic bigint,
    create_date timestamp without time zone,
    create_who character varying(32),
    change_date timestamp without time zone,
    change_who character varying(32),
    reinsurer_note text,
    reinsurer_note_objek character varying(255),
    CONSTRAINT ins_proposal_komisi_pkey PRIMARY KEY (ins_upload_dtl_id)
    )
    WITH (
    OIDS=FALSE
    );
    ALTER TABLE ins_proposal_komisi
    OWNER TO postgres;

    -- Index: ins_proposal_komisi_ins_ar_invoice_id_idx

    -- DROP INDEX ins_proposal_komisi_ins_ar_invoice_id_idx;

    CREATE INDEX ins_proposal_komisi_ins_ar_invoice_id_idx
    ON ins_proposal_komisi
    USING btree
    (ins_ar_invoice_id);

    -- Index: ins_proposal_komisi_ins_upload_id_idx

    -- DROP INDEX ins_proposal_komisi_ins_upload_id_idx;

    CREATE INDEX ins_proposal_komisi_ins_upload_id_idx
    ON ins_proposal_komisi
    USING btree
    (ins_upload_id);

    -- Index: ins_proposal_komisi_pol_no_idx

    -- DROP INDEX ins_proposal_komisi_pol_no_idx;

    CREATE INDEX ins_proposal_komisi_pol_no_idx
    ON ins_proposal_komisi
    USING btree
    (pol_no COLLATE pg_catalog."default");

     */
    public static String tableName = "ins_proposal_komisi";
    public static String comboFields[] = {"ins_upload_id", "no_surat_hutang", "data_amount", "jumlah"};
    public transient static String fieldMap[][] = {
        {"stInsuranceUploadDetailID", "ins_upload_dtl_id*pk*nd"},
        {"stInsuranceUploadID", "ins_upload_id"},
        {"stInsurancePolicyID", "ins_pol_id"},
        {"stARInvoiceID", "ins_ar_invoice_id"},
        {"stPolicyNo", "pol_no"},
        {"stTertanggung", "tertanggung"},
        {"dtPeriodeAwal", "periode_awal"},
        {"dtPeriodeAkhir", "periode_akhir"},
        {"dbAmount", "amount"},
        {"stFilePhysic", "file_physic"},
        {"stNoSuratHutang", "no_surat_hutang"},
        {"dbAmountTotal", "amount_total"},
        {"stDataAmount", "data_amount"},
        {"stReinsurerNote", "reinsurer_note"},
        {"stReinsurerNoteObject", "reinsurer_note_objek"},
        {"stStatus1", "status1"},
        {"stStatus2", "status2"},
        {"stStatus3", "status3"},
        {"stStatus4", "status4"},
        {"stCostCenterCode", "cc_code"},
        {"stPolicyTypeGroupID", "pol_type_grp_id"},
        {"stPolicyTypeID", "pol_type_id"},
        {"stJumlah", "jumlah*n"},
        {"stKeterangan", "ket"},
        {"stKabagApproved", "kabag_approved"},
        {"stInvoiceNo", "invoice_no"},
         {"stARInvoiceRefID", "ar_invoice_ref_id"},
        {"stCostCenterCodeSource", "cc_code_source"},
         {"dtApprovedDate", "approved_date"},
        {"stKadivApproved", "kadiv_approved"},
    };

    private String stInsuranceUploadDetailID;
    private String stInsuranceUploadID;
    private String stInsurancePolicyID;
    private String stARInvoiceID;
    private String stPolicyNo;
    private String stTertanggung;
    private Date dtPeriodeAwal;
    private Date dtPeriodeAkhir;
    private BigDecimal dbAmount;
    private String stFilePhysic;
    private String stNoSuratHutang;
    private BigDecimal dbAmountTotal;
    private String stDataAmount;
    private String stReinsurerNote;
    private String stReinsurerNoteObject;
    private String stStatus1;
    private String stStatus2;
    private String stStatus3;
    private String stStatus4;
    private String stCostCenterCode;
    private String stPolicyTypeGroupID;
    private String stPolicyTypeID;
    private String stJumlah;
    private String stKeterangan;
    private String stKabagApproved;
    private String stInvoiceNo;
    private String stARInvoiceRefID;
    private String stCostCenterCodeSource;
    private Date dtApprovedDate;
    private String stKadivApproved;


    public FileView getFile() {

        final FileView file = (FileView) DTOPool.getInstance().getDTO(FileView.class, getStFilePhysic());

        return file;

    }

    /**
     * @return the stInsuranceUploadDetailID
     */
    public String getStInsuranceUploadDetailID() {
        return stInsuranceUploadDetailID;
    }

    /**
     * @param stInsuranceUploadDetailID the stInsuranceUploadDetailID to set
     */
    public void setStInsuranceUploadDetailID(String stInsuranceUploadDetailID) {
        this.stInsuranceUploadDetailID = stInsuranceUploadDetailID;
    }

    /**
     * @return the stInsuranceUploadID
     */
    public String getStInsuranceUploadID() {
        return stInsuranceUploadID;
    }

    /**
     * @param stInsuranceUploadID the stInsuranceUploadID to set
     */
    public void setStInsuranceUploadID(String stInsuranceUploadID) {
        this.stInsuranceUploadID = stInsuranceUploadID;
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
     * @return the dtPeriodeAwal
     */
    public Date getDtPeriodeAwal() {
        return dtPeriodeAwal;
    }

    /**
     * @param dtPeriodeAwal the dtPeriodeAwal to set
     */
    public void setDtPeriodeAwal(Date dtPeriodeAwal) {
        this.dtPeriodeAwal = dtPeriodeAwal;
    }

    /**
     * @return the dtPeriodeAkhir
     */
    public Date getDtPeriodeAkhir() {
        return dtPeriodeAkhir;
    }

    /**
     * @param dtPeriodeAkhir the dtPeriodeAkhir to set
     */
    public void setDtPeriodeAkhir(Date dtPeriodeAkhir) {
        this.dtPeriodeAkhir = dtPeriodeAkhir;
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
     * @return the stDataAmount
     */
    public String getStDataAmount() {
        return stDataAmount;
    }

    /**
     * @param stDataAmount the stDataAmount to set
     */
    public void setStDataAmount(String stDataAmount) {
        this.stDataAmount = stDataAmount;
    }

    /**
     * @return the stReinsurerNote
     */
    public String getStReinsurerNote() {
        return stReinsurerNote;
    }

    /**
     * @param stReinsurerNote the stReinsurerNote to set
     */
    public void setStReinsurerNote(String stReinsurerNote) {
        this.stReinsurerNote = stReinsurerNote;
    }

    /**
     * @return the stReinsurerNoteObject
     */
    public String getStReinsurerNoteObject() {
        return stReinsurerNoteObject;
    }

    /**
     * @param stReinsurerNoteObject the stReinsurerNoteObject to set
     */
    public void setStReinsurerNoteObject(String stReinsurerNoteObject) {
        this.stReinsurerNoteObject = stReinsurerNoteObject;
    }

    /**
     * @return the stInsurancePolicyID
     */
    public String getStInsurancePolicyID() {
        return stInsurancePolicyID;
    }

    /**
     * @param stInsurancePolicyID the stInsurancePolicyID to set
     */
    public void setStInsurancePolicyID(String stInsurancePolicyID) {
        this.stInsurancePolicyID = stInsurancePolicyID;
    }

    /**
     * @return the stARInvoiceID
     */
    public String getStARInvoiceID() {
        return stARInvoiceID;
    }

    /**
     * @param stARInvoiceID the stARInvoiceID to set
     */
    public void setStARInvoiceID(String stARInvoiceID) {
        this.stARInvoiceID = stARInvoiceID;
    }

    /**
     * @return the stTertanggung
     */
    public String getStTertanggung() {
        return stTertanggung;
    }

    /**
     * @param stTertanggung the stTertanggung to set
     */
    public void setStTertanggung(String stTertanggung) {
        this.stTertanggung = stTertanggung;
    }

    /**
     * @return the dbAmount
     */
    public BigDecimal getDbAmount() {
        return dbAmount;
    }

    /**
     * @param dbAmount the dbAmount to set
     */
    public void setDbAmount(BigDecimal dbAmount) {
        this.dbAmount = dbAmount;
    }

    /**
     * @return the dbAmountTotal
     */
    public BigDecimal getDbAmountTotal() {
        return dbAmountTotal;
    }

    /**
     * @param dbAmountTotal the dbAmountTotal to set
     */
    public void setDbAmountTotal(BigDecimal dbAmountTotal) {
        this.dbAmountTotal = dbAmountTotal;
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

    public ARInvoiceView getInvoice() {
        return (ARInvoiceView) DTOPool.getInstance().getDTO(ARInvoiceView.class, getStARInvoiceID());
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

    /**
     * @return the stPolicyTypeGroupID
     */
    public String getStPolicyTypeGroupID() {
        return stPolicyTypeGroupID;
    }

    /**
     * @param stPolicyTypeGroupID the stPolicyTypeGroupID to set
     */
    public void setStPolicyTypeGroupID(String stPolicyTypeGroupID) {
        this.stPolicyTypeGroupID = stPolicyTypeGroupID;
    }

    /**
     * @return the stJumlah
     */
    public String getStJumlah() {
        return stJumlah;
    }

    /**
     * @param stJumlah the stJumlah to set
     */
    public void setStJumlah(String stJumlah) {
        this.stJumlah = stJumlah;
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

    public String getStInvoiceNo() {
        return stInvoiceNo;
    }

    /**
     * @param stInvoiceNo the stInvoiceNo to set
     */
    public void setStInvoiceNo(String stInvoiceNo) {
        this.stInvoiceNo = stInvoiceNo;
    }
    
    
    /**
     * @return the stARInvoiceRefID
     */
    public String getStARInvoiceRefID() {
        return stARInvoiceRefID;
    }

    /**
     * @param stARInvoiceRefID the stARInvoiceRefID to set
     */
    public void setStARInvoiceRefID(String stARInvoiceRefID) {
        this.stARInvoiceRefID = stARInvoiceRefID;
    }

    public ARInvoiceDetailView getInvoiceDet(String stARInvRefID) {
        return (ARInvoiceDetailView) DTOPool.getInstance().getDTO(ARInvoiceDetailView.class, stARInvRefID);
    }

    public String getStCostCenterCodeSource() {
        return stCostCenterCodeSource;
    }

    /**
     * @param stCostCenterCodeSource the stCostCenterCodeSource to set
     */
    public void setStCostCenterCodeSource(String stCostCenterCodeSource) {
        this.stCostCenterCodeSource = stCostCenterCodeSource;
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
