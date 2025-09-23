/***********************************************************************
 * Module:  com.webfin.gl.form.InsuranceUploadForm
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:34 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.insurance.form;

import com.crux.common.controller.FormTab;
import com.crux.common.model.HashDTO;
import com.crux.common.parameter.Parameter;
import com.crux.file.FileManager;
import com.crux.file.FileView;
import com.crux.lang.LanguageManager;
import com.crux.pool.DTOPool;
import com.crux.util.BDUtil;
import com.crux.util.ConnectionCache;
import com.crux.web.form.Form;
import com.crux.util.DTOList;
import com.crux.util.DateUtil;
import com.crux.util.IDFactory;
import com.crux.util.JNDIUtil;
import com.crux.util.JSPUtil;
import com.crux.util.ListUtil;
import com.crux.util.LogManager;
import com.crux.util.MailUtil2;
import com.crux.util.SQLAssembler;
import com.crux.util.SQLUtil;
import com.crux.util.StringTools;
import com.crux.util.Tools;
import com.crux.web.controller.SessionManager;
import com.lowagie.text.Rectangle;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.webfin.ar.ejb.AccountReceivableHome;
import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.ar.model.ARInvoiceDetailView;
import com.webfin.ar.model.ARInvoiceView;
import com.webfin.gl.ejb.CurrencyManager;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.model.JournalView;

import com.webfin.insurance.ejb.*;
import com.webfin.insurance.model.BiayaPemasaranDetailView;
import com.webfin.insurance.model.BiayaPemasaranDocumentsView;
import com.webfin.insurance.model.BiayaPemasaranView;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.insurance.model.UploadHeaderPiutangPremiView;
import com.webfin.insurance.model.UploadHeaderProposalCommView;
import com.webfin.insurance.model.UploadHeaderReinsuranceView;
import com.webfin.insurance.model.UploadHeaderView;
import com.webfin.insurance.model.kalkulatorPremiHeaderView;
import com.webfin.insurance.model.kalkulatorPremiView;
import com.webfin.insurance.model.uploadEndorsemenView;
import com.webfin.insurance.model.uploadPiutangPremiView;
import com.webfin.insurance.model.uploadProposalCommView;
import com.webfin.insurance.model.uploadReinsuranceSpreadingView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import javax.servlet.ServletOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class InsuranceUploadForm extends Form {

    //private final static transient LogManager logger = LogManager.getInstance(JournalView.class);
    private UploadHeaderView titipan;
    private String notesindex;
    private String glindex;
    private String stGLSelect;
    private String invoicesindex;
    private String invoicecomissionindex;
    private String parinvoiceid;
    public boolean approveMode;
    public boolean voidMode;
    public boolean editMode;
    public boolean viewMode;
    private String nosurathutang;
    private String cc_code;
    private String artitipanid;
    private String stReportType;
    public DTOList objects;
    public String objectindex;
    private boolean drillMode = false;
    private BigDecimal dbTotalTitipan;
    private UploadHeaderReinsuranceView headerReins;
    private kalkulatorPremiHeaderView kalkulatorReins;
    private UploadHeaderPiutangPremiView headerPiutang;
    private UploadHeaderProposalCommView headerProposal;
    private boolean canCreate = SessionManager.getInstance().getSession().hasResource("PROP_CREATE");
    private boolean approvalCab = SessionManager.getInstance().getSession().hasResource("PROP_APPROVAL_CAB");
    private boolean approvalSie = SessionManager.getInstance().getSession().hasResource("PROP_APPROVAL_SIE");
    private boolean approvalBag = SessionManager.getInstance().getSession().hasResource("PROP_APPROVAL_BAG");
    private boolean approvalDiv = SessionManager.getInstance().getSession().hasResource("PROP_APPROVAL_DIV");
    private boolean reverseMode;
    private BiayaPemasaranView pemasaran;
    private BiayaPemasaranDetailView pmsdetail;
    private String pemasaranID;
    private boolean receiptMode;
    private FormTab tabs;
    private DTOList document;
     private final static transient LogManager logger = LogManager.getInstance(InsuranceUploadForm.class);

     private String stPolicyTypeGroupID;
    private String stPolicyTypeGroupDesc;
    private String stPolicyTypeID;
    private String stPolicyTypeDesc;
    private String stBranch;

    public UploadHeaderView getTitipan() {
        return titipan;
    }

    public void setReceipt(UploadHeaderView titipan) {
        this.titipan = titipan;
    }

    public boolean isDrillMode() {
        return drillMode;
    }

    public void setDrillMode(boolean drillMode) {
        this.drillMode = drillMode;
    }

    public void drillToggle() {
        drillMode = !drillMode;
    }

    public void btnPrint() throws Exception {
    }

    public String getStReportType() {
        return stReportType;
    }

    public void setStReportType(String stReportType) {
        this.stReportType = stReportType;
    }

    public String getStGLSelect() {
        return stGLSelect;
    }

    public void setStGLSelect(String stGLSelect) {
        this.stGLSelect = stGLSelect;
    }

    public String getGlindex() {
        return glindex;
    }

    public void setGlindex(String glindex) {
        this.glindex = glindex;
    }

    public String getInvoicecomissionindex() {
        return invoicecomissionindex;
    }

    public void setInvoicecomissionindex(String invoicecomissionindex) {
        this.invoicecomissionindex = invoicecomissionindex;
    }

    private AccountReceivable getRemoteAccountReceivable() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((AccountReceivableHome) JNDIUtil.getInstance().lookup("AccountReceivableEJB", AccountReceivableHome.class.getName())).create();
    }

    private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB", InsuranceHome.class.getName())).create();
    }

    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName())).create();
    }

    public String getParinvoiceid() {
        return parinvoiceid;
    }

    public void setParinvoiceid(String parinvoiceid) {
        this.parinvoiceid = parinvoiceid;
    }

    public String getArtitipanid() {
        return artitipanid;
    }

    public void setArtitipanid(String artitipanid) {
        this.artitipanid = artitipanid;
    }

    public String getNosurathutang() {
        return nosurathutang;
    }

    public void setNosurathutang(String nosurathutang) {
        this.nosurathutang = nosurathutang;
    }

    public String getCcCode() {
        return cc_code;
    }

    public void setCcCode(String cc_code) {
        this.cc_code = cc_code;
    }

    public String getNotesindex() {
        return notesindex;
    }

    public void setNotesindex(String notesindex) {
        this.notesindex = notesindex;
    }

    public String getInvoicesindex() {
        return invoicesindex;
    }

    public void setInvoicesindex(String invoicesindex) {
        this.invoicesindex = invoicesindex;
    }

    public DTOList getDetails() throws Exception {
        return titipan.getDetails();
    }

    public InsuranceUploadForm() {
    }

    public void onNewNoteItem() {
    }
    /*
    public void onDeleteNoteItem() throws Exception {
    getListNotes().delete(Integer.parseInt(notesindex));

    recalculate();
    }

    private void recalculate() throws Exception {
    titipan.recalculate();

    titipan.setStARTitipanID("01");
    }

    public void RecalculatePajak() throws Exception {
    titipan.recalculate2();

    titipan.setStARTitipanID("01");
    }

    public void onDeleteGLItem() throws Exception {
    getListGLs().delete(Integer.parseInt(glindex));
    recalculate();
    }

    public void onNewInvoiceItem() {

    }

    public void onDeleteInvoiceItem() throws Exception {
    getListInvoices().delete(Integer.parseInt(invoicesindex));
    recalculate();
    }

    public void onDeleteInvoiceComissionItem() throws Exception {
    final ARtitipanLinesView iv = (ARtitipanLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

    iv.getDetails().delete(Integer.parseInt(invoicecomissionindex));

    recalculate();
    }

    public void onShrinkInvoiceItem() {


    }

    public void onChangeCust() {

    }

    public void onExpandInvoiceItemSuratHutang(String nosurathutang) throws Exception {
    final ARtitipanLinesView rl = (ARtitipanLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

    rl.setStNosurathutang(nosurathutang);

    final ARInvoiceView invoice = rl.getInvoiceBySuratHutang();

    rl.setStExpandedFlag("Y");

    final DTOList details = invoice.getDetails();

    for (int i = 0; i < details.size(); i++) {
    ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);

    if (!d.isComission()) continue;

    final ARtitipanLinesView rcl = new ARtitipanLinesView();
    rcl.markNew();

    rcl.setStInvoiceID(invoice.getStARInvoiceID());
    rcl.setStInvoiceNo(invoice.getStInvoiceNo());
    rcl.setStInvoiceDetailID(d.getStARInvoiceDetailID());
    rcl.setStDescription(d.getStDescription());
    rcl.setStCurrencyCode(invoice.getStCurrencyCode());
    rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());

    rcl.setDbInvoiceAmount(d.getDbEnteredAmount());
    //rcl.setDbAmount(d.getDbOustandingAmount());
    rcl.setDbEnteredAmount(d.getDbOustandingAmount());
    //rcl.setDbAmountSettled(d.getDbAmountSettled());
    rcl.markAsComission();
    rcl.markCommit();

    rl.getDetails().add(rcl);
    }
    titipan.recalculate();
    }
     */
    /*
    public void onExpandInvoiceItem() throws Exception {
    final ARtitipanLinesView rl = (ARtitipanLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

    final ARInvoiceView invoice = rl.getInvoice();

    rl.setStExpandedFlag("Y");

    final DTOList details = invoice.getDetails();

    /* buat munculin detail premi bruto
    for (int i = 0; i < details.size(); i++) {
    ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);
    if(d.getStRefID0()!=null){
    if (!d.getStRefID0().startsWith("PREMIG")) continue;

    final ARtitipanLinesView rcl = new ARtitipanLinesView();
    rcl.markNew();

    rcl.setStInvoiceID(invoice.getStARInvoiceID());
    rcl.setStInvoiceNo(invoice.getStInvoiceNo());
    rcl.setStInvoiceDetailID(d.getStARInvoiceDetailID());
    rcl.setStDescription(d.getStDescription());
    rcl.setStCurrencyCode(invoice.getStCurrencyCode());
    rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());

    rcl.setDbInvoiceAmount(d.getDbEnteredAmount());
    //rcl.setDbAmount(d.getDbOustandingAmount());
    rcl.setDbEnteredAmount(d.getDbOustandingAmount());
    //rcl.setDbAmountSettled(d.getDbAmountSettled());
    //rcl.markAsComission();
    rcl.markCommit();

    rl.getDetails().add(rcl);
    }


    }*/

    /*
    for (int i = 0; i < details.size(); i++) {
    ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);

    //if (!d.isComission()) continue; tampilin semua

    final ARtitipanLinesView rcl = new ARtitipanLinesView();
    rcl.markNew();

    rcl.setStInvoiceID(invoice.getStARInvoiceID());
    rcl.setStInvoiceNo(invoice.getStInvoiceNo());
    rcl.setStInvoiceDetailID(d.getStARInvoiceDetailID());
    rcl.setStDescription(d.getStDescription());
    rcl.setStCurrencyCode(invoice.getStCurrencyCode());
    rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
    rcl.setStARTrxLineID(d.getStARTrxLineID());


    rcl.setDbInvoiceAmount(d.getDbEnteredAmount());
    //rcl.setDbAmount(d.getDbOustandingAmount());
    rcl.setDbEnteredAmount(d.getDbOustandingAmount());
    //rcl.setDbAmountSettled(d.getDbAmountSettled());
    rcl.markAsComission();
    rcl.markCommit();


    rl.getDetails().add(rcl);
    }


    titipan.recalculate();
    }


    public void onExpandInvoiceItem() throws Exception {
    final ARtitipanLinesView rl = (ARtitipanLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

    final ARInvoiceView invoice = rl.getInvoice();

    rl.setStExpandedFlag("Y");

    final DTOList details = invoice.getDetails();

    for (int i = 0; i < details.size(); i++) {
    ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);

    final ARtitipanLinesView rcl = new ARtitipanLinesView();
    rcl.markNew();

    rcl.setStInvoiceID(invoice.getStARInvoiceID());
    rcl.setStInvoiceNo(invoice.getStInvoiceNo());
    rcl.setStInvoiceDetailID(d.getStARInvoiceDetailID());
    rcl.setStDescription(d.getStDescription());
    rcl.setStCurrencyCode(invoice.getStCurrencyCode());
    rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());

    rcl.setDbInvoiceAmount(d.getDbEnteredAmount());
    rcl.setDbAmount(d.getDbOustandingAmount());
    rcl.setDbEnteredAmount(d.getDbOustandingAmount());

    if(d.isPremiGross2()||d.isCommission2()||d.isBrokerage2()
    ||d.isHandlingFee2()){
    rcl.setStLock(false);
    }

    if (d.isComission()) rcl.markAsComission();
    rcl.markCommit();

    rl.getDetails().add(rcl);
    }
    titipan.recalculate();
    }

    public void onExpandInvoiceItemTitipan() throws Exception {
    final ARtitipanLinesView rl = (ARtitipanLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

    final ARInvoiceView invoice = rl.getInvoice();

    rl.setStExpandedFlag("Y");

    final DTOList details = invoice.getDetails();

    for (int i = 0; i < details.size(); i++) {
    ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);

    final ARtitipanLinesView rcl = new ARtitipanLinesView();
    rcl.markNew();

    rcl.setStInvoiceID(invoice.getStARInvoiceID());
    rcl.setStInvoiceNo(invoice.getStInvoiceNo());
    rcl.setStInvoiceDetailID(d.getStARInvoiceDetailID());
    rcl.setStDescription(d.getStDescription());
    rcl.setStCurrencyCode(invoice.getStCurrencyCode());
    rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());

    rcl.setDbInvoiceAmount(d.getDbEnteredAmount());
    rcl.setDbAmount(d.getDbOustandingAmount());
    rcl.setDbEnteredAmount(d.getDbOustandingAmount());

    if(d.isPremiGross2()||d.isCommission2()||d.isBrokerage2()
    ||d.isHandlingFee2()){
    rcl.setStLock(false);
    }

    if (d.isComission()) rcl.markAsComission();
    rcl.markCommit();

    rl.getDetails().add(rcl);
    }

    DTOList objectsPolicy = invoice.getPolicyObjects();


    for(int i=0;i<objectsPolicy.size();i++){
    InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objectsPolicy.get(i);

    obj.setDbObjectPremiSettled(obj.getDbObjectPremiTotalAmount());
    obj.setStPremiSettledFlag("Y");
    }
    objectsPolicy.markAllUpdate();
    rl.setObjects(objectsPolicy);

    titipan.recalculate();
    }

     */
    /*
    public void createNew() throws Exception {
    titipan = new JournalMemorialHeaderView();
    titipan.markNew();

    final DTOList details = new DTOList();

    final TitipanPremiView jv = new TitipanPremiView();

    jv.markNew();

    details.add(jv);

    titipan.setDetails(details);

    titipan.setStCostCenter(SessionManager.getInstance().getSession().getStBranch());

    setTitle("Input Titipan Premi");

    editMode = true;
    }

    public void edit(String titipanID) throws Exception {
    logger.logDebug("++++++++++++++ titipanID : " + titipanID);
    masterEdit(titipanID);

    //if (titipan.isPosted()) throw new RuntimeException("titipan not editable (Posted)");
    //if (titipan.isCancel()) throw new RuntimeException("titipan not editable (VOID)");

    editMode = true;
    }

    public void superEdit(String titipanID) throws Exception {
    artitipanid = titipanID;

    masterEdit(titipanID);

    editMode = true;
    }
     */
    public void masterEdit(String titipanID) throws Exception {


        final DTOList details = getRemoteGeneralLedger().getJournalEntry(getTitipanPremi(titipanID).getStTransactionHeaderID());

        titipan = new UploadHeaderView();

        //titipan.markUpdate();

        titipan.setDetails(details);

        titipan.getDetails().markAllUpdate();

        JournalView titip = (JournalView) details.get(0);

//        titipan.setStTransactionNo(titip.getStTransactionNo());
//        titipan.setStCostCenter(titip.getStTransactionNo().substring(5, 7));
//        titipan.setStMonths(titip.getStMonths());
//        titipan.setStYears(titip.getStYears());
//        titipan.setStMethodCode("F");
//        titipan.setStAccountID(titip.getLgAccountID().toString());



    }

    public void view(String titipanID) throws Exception {

        super.setReadOnly(true);

        viewMode = true;
    }

    public void doSave() throws Exception {

        recalculate();

        getRemoteInsurance().saveUploadEndorsemen(titipan, titipan.getDetails());

        super.close();

    }

    /*
    public void doRecalculate() throws Exception {
    //titipan.recalculate();

    BigDecimal total = BDUtil.zero;

    final DTOList details = getDetails();
    for (int i = 0; i < details.size(); i++) {
    JournalView titip = (JournalView) details.get(i);

    titip.setStAccountIDMaster(titipan.getStAccountIDMaster());
    titip.setStAccountNoMaster(titipan.getStAccountNoMaster());
    titip.setStMonths(titipan.getStMonths());
    titip.setStCostCenter(titipan.getStCostCenter());

    total = BDUtil.add(total, titip.getDbAmount());

    }
    dbTotalTitipan = total;
    }
     */
    public void doCancel() {
        super.close();
    }

    public void doClose() {
        super.close();
    }

    public void afterUpdateForm() {
        try {
            //titipan.recalculate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void chgCurrency() throws Exception {
        //titipan.setDbCurrencyRate(CurrencyManager.getInstance().getRate(titipan.getStCurrencyCode(), titipan.getDttitipanDate()));
    }

    public void changeBankType() {
        //titipan.setStInvoiceType(titipan.gettitipanClass().getStInvoiceType());
    }

    public void changeCostCenter() {
    }

    public void changemethod() {
    }

    public void generatRNo() throws Exception {
        //titipan.generatetitipanNo();
    }

    public void approve(String titipanID) throws Exception {
        masterEdit(titipanID);

        //if (!titipan.isPosted() && !titipan.isCancel()); else throw new RuntimeException("Invalid State");
        //titipan.setStPostedFlag("Y");

        super.setReadOnly(true);
        approveMode = true;
    }

    public boolean isApproveMode() {
        return approveMode;
    }

    public void doApprove() throws Exception {
        //titipan.setStPostedFlag("Y");
        doSave();
    }

    public void setApproveMode(boolean approveMode) {
        this.approveMode = approveMode;
    }

    public boolean isVoidMode() {
        return voidMode;
    }

    public void setVoidMode(boolean voidMode) {
        this.voidMode = voidMode;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean isViewMode() {
        return viewMode;
    }

    public void setViewMode(boolean viewMode) {
        this.viewMode = viewMode;
    }

    /*
    public DTOList getObjects() throws Exception{
    ARtitipanLinesView activetitipanLine = getActivetitipanLine();

    objects = activetitipanLine.getObjects();

    return objects;
    }*/
    public void setObjects(DTOList objects) {
        this.objects = objects;
    }

    public String getObjectindex() {
        return objectindex;
    }

    public void setObjectindex(String objectindex) {
        this.objectindex = objectindex;
    }

    public void addLine() {
        /*
        final DTOList details = titipan.getDetails();
        final ARTitipanPremiDetailView titipanDetails = new ARTitipanPremiDetailView();
        details.add(titipanDetails);
        invoicesindex=String.valueOf(titipan.getDetails().size()-1);*/
    }

    public void delLine() throws Exception {
        titipan.getDetails().delete(Integer.parseInt(notesindex));
    }

    public void doNewTitipan() throws Exception {

        final DTOList details = titipan.getDetails();

        final JournalView jv = new JournalView();

        jv.markNew();

        details.add(jv);

        notesindex = String.valueOf(titipan.getDetails().size() - 1);



    }
    private DTOList detailsTitipan;

    public DTOList getDetailsTitipan(String titipanID) {
        //logger.logWarning("++++++++++++++ titipan id : " + titipanID);
        loadTreaties(titipanID);
        return detailsTitipan;
    }

    private void loadTreaties(String titipanID) {
        try {
            if (detailsTitipan == null) {

                detailsTitipan = ListUtil.getDTOListFromQuery(
                        "select * from gl_je_detail where trx_no = ?",
                        new Object[]{getTitipanPremi(titipanID).getStTransactionNo()},
                        JournalView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JournalView getTitipanPremi(String stArTitipanID) {
        if (stArTitipanID == null) {
            return null;
        }
        return (JournalView) DTOPool.getInstance().getDTO(JournalView.class, stArTitipanID);
    }

    public void createNewUpload() throws Exception {
        titipan = new UploadHeaderView();
        titipan.markNew();

        final DTOList details = new DTOList();

        titipan.setDetails(details);

        //titipan.setStCostCenter(SessionManager.getInstance().getSession().getStBranch());

        setTitle("Upload Endorse PA Kreasi");

        editMode = true;
    }

    public void uploadExcel() throws Exception {

        if (titipan.getDtPolicyDate() == null) {
            throw new RuntimeException("Tanggal polis/approval harus diisi");
        }

        String fileID = titipan.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Endorse");

        final DTOList details = new DTOList();

        titipan.setDetails(details);

        int rows = sheet.getPhysicalNumberOfRows();

        //for (int r = 12; r < rows; r++) {
        for (int r = 4; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellRekap = row.getCell(1);
            HSSFCell cellNoPolis = row.getCell(2);
            HSSFCell cellNomorUrut = row.getCell(3);
            HSSFCell cellNama = row.getCell(4);
            HSSFCell cellTSI = row.getCell(5);
            HSSFCell cellRateJual = row.getCell(6);
            HSSFCell cellPremi = row.getCell(7);
            HSSFCell cellKoas = row.getCell(8);
            HSSFCell cellKoasMenjadi = row.getCell(9);
            HSSFCell cellRateKoas = row.getCell(10);
            HSSFCell cellPremiKoas = row.getCell(11);
            HSSFCell cellRateKomisiKoas = row.getCell(12);
            HSSFCell cellKomisiKoas = row.getCell(13);
            HSSFCell cellNoRekapObjek = row.getCell(14);
            HSSFCell cellKetEndorseObjek = row.getCell(15);
            HSSFCell cellAutoApproveObjek = row.getCell(16);
            HSSFCell cellApprovedWho = row.getCell(17);

            uploadEndorsemenView endorse = new uploadEndorsemenView();
            endorse.markNew();

            titipan.setStRecapNo(cellRekap.getStringCellValue());

            endorse.setStRecapNo(cellRekap.getStringCellValue());
            endorse.setStPolicyNo(cellNoPolis.getStringCellValue());

            endorse.setStOrderNo(cellNomorUrut.getCellType() == cellNomorUrut.CELL_TYPE_STRING ? cellNomorUrut.getStringCellValue() : new BigDecimal(cellNomorUrut.getNumericCellValue()).toString());

            endorse.setStNama(cellNama.getStringCellValue());
            endorse.setDbTSI(new BigDecimal(cellTSI.getNumericCellValue()));
            endorse.setDbRateJual(new BigDecimal(cellRateJual.getNumericCellValue()));
            endorse.setDbPremi(new BigDecimal(cellPremi.getNumericCellValue()));

            endorse.setStKoasuransi(cellKoas.getCellType() == cellKoas.CELL_TYPE_STRING ? cellKoas.getStringCellValue() : new BigDecimal(cellKoas.getNumericCellValue()).toString());
            endorse.setStKoasuransiMenjadi(cellKoasMenjadi.getCellType() == cellKoasMenjadi.CELL_TYPE_STRING ? cellKoasMenjadi.getStringCellValue() : new BigDecimal(cellKoasMenjadi.getNumericCellValue()).toString());

            endorse.setDbRateKoas(new BigDecimal(cellRateKoas.getNumericCellValue()));
            endorse.setDbPremiKoas(new BigDecimal(cellPremiKoas.getNumericCellValue()));
            endorse.setDbRateKomisiKoas(new BigDecimal(cellRateKomisiKoas.getNumericCellValue()));
            endorse.setDbKomisiKoas(new BigDecimal(cellKomisiKoas.getNumericCellValue()));
            endorse.setStRecapNoObject(cellNoRekapObjek.getStringCellValue());
            endorse.setStEndorseNoteObject(cellKetEndorseObjek.getStringCellValue());
            endorse.setStAutoApproveFlag(cellAutoApproveObjek.getStringCellValue());
            endorse.setStApprovedWho(cellApprovedWho.getCellType() == cellApprovedWho.CELL_TYPE_STRING ? cellApprovedWho.getStringCellValue() : new BigDecimal(cellApprovedWho.getNumericCellValue()).toString());

            if (titipan.getDtPolicyDate() != null) {
                endorse.setDtPolicyDate(titipan.getDtPolicyDate());
            }

            details.add(endorse);

        }

        titipan.setDetails(details);

    }

    /**
     * @return the dbTotalTitipan
     */
    public BigDecimal getDbTotalTitipan() {
        return dbTotalTitipan;
    }

    /**
     * @param dbTotalTitipan the dbTotalTitipan to set
     */
    public void setDbTotalTitipan(BigDecimal dbTotalTitipan) {
        this.dbTotalTitipan = dbTotalTitipan;
    }

    public void Edit(String titipanID) throws Exception {

        final DTOList details = getRemoteInsurance().getInsuranceUploadDetail(titipanID);

        uploadEndorsemenView titip = (uploadEndorsemenView) details.get(0);

        titipan = new UploadHeaderView();

        // titipan.getDetails().markAllUpdate();

        titipan.setDetails(details);

        titipan.getDetails().markAllUpdate();

        titipan.setStRecapNo(titip.getStRecapNo());
        titipan.setStFilePhysic(titip.getStFilePhysic());
        titipan.setStDataAmount(titip.getStDataAmount());
        titipan.setDbTSITotal(titip.getDbTSITotal());
        titipan.setDbPremiTotal(titip.getDbPremiTotal());
        titipan.setDbPremiKoasTotal(titip.getDbPremiKoasTotal());
        titipan.setDbKomisiKoasTotal(titip.getDbKomisiKoasTotal());
        titipan.setStEndorseNote(titip.getStEndorseNote());
        titipan.setStInsuranceUploadID(titip.getStInsuranceUploadID());
        titipan.setDtPolicyDate(titip.getDtPolicyDate());

    }

    public void recalculate() throws Exception {
        DTOList details = titipan.getDetails();

        BigDecimal totalTSI = null;
        BigDecimal totalPremi = null;
        BigDecimal totalPremiKoas = null;
        BigDecimal totalKomisiKoas = null;

        for (int i = 0; i < details.size(); i++) {
            uploadEndorsemenView object = (uploadEndorsemenView) details.get(i);

            totalTSI = BDUtil.add(totalTSI, object.getDbTSI());
            totalPremi = BDUtil.add(totalPremi, object.getDbPremi());
            totalPremiKoas = BDUtil.add(totalPremiKoas, object.getDbPremiKoas());
            totalKomisiKoas = BDUtil.add(totalKomisiKoas, object.getDbKomisiKoas());

        }

        for (int i = 0; i < details.size(); i++) {
            uploadEndorsemenView object = (uploadEndorsemenView) details.get(i);

            object.setStDataAmount(String.valueOf(details.size()));
            object.setDbTSITotal(totalTSI);
            object.setDbPremiTotal(totalPremi);
            object.setDbPremiKoasTotal(totalPremiKoas);
            object.setDbKomisiKoasTotal(totalKomisiKoas);
            object.setStEndorseNote(titipan.getStEndorseNote());

        }

        titipan.setStDataAmount(String.valueOf(details.size()));
        titipan.setDbTSITotal(totalTSI);
        titipan.setDbPremiTotal(totalPremi);
        titipan.setDbPremiKoasTotal(totalPremiKoas);
        titipan.setDbKomisiKoasTotal(totalKomisiKoas);
    }

    public void createNewUploadRI() throws Exception {
        headerReins = new UploadHeaderReinsuranceView();
        headerReins.markNew();

        final DTOList details = new DTOList();

        headerReins.setDetails(details);

        //titipan.setStCostCenter(SessionManager.getInstance().getSession().getStBranch());

        setTitle("Upload Spreading Kreasi/Kredit");

        editMode = true;
    }

    public void uploadExcelRI() throws Exception {

        String fileID = headerReins.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Reins");

        final DTOList details = new DTOList();

        headerReins.setDetails(details);

        int rows = sheet.getPhysicalNumberOfRows();

        //for (int r = 12; r < rows; r++) {
        for (int r = 4; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellRekap = row.getCell(1);
            HSSFCell cellPolID = row.getCell(2);
            HSSFCell cellNoPolis = row.getCell(3);
            HSSFCell cellNomorUrut = row.getCell(4);
            HSSFCell cellObjectID = row.getCell(5);
            HSSFCell cellNama = row.getCell(6);
            HSSFCell cellUsia = row.getCell(7);
            HSSFCell cellTglLahir = row.getCell(8);
            HSSFCell cellTglAwal = row.getCell(9);
            HSSFCell cellTglAkhir = row.getCell(10);
            HSSFCell cellTSI = row.getCell(11);
            HSSFCell cellPremi = row.getCell(12);
            HSSFCell cellTreaty = row.getCell(13);
            HSSFCell cellKetSpreadingReins = row.getCell(14);

            uploadReinsuranceSpreadingView reinsurer = new uploadReinsuranceSpreadingView();
            reinsurer.markNew();

            headerReins.setStRecapNo(cellRekap.getStringCellValue());
            headerReins.setStReinsurerNote(cellKetSpreadingReins.getStringCellValue());

            reinsurer.setStRecapNo(cellRekap.getStringCellValue());
            reinsurer.setStInsurancePolicyID(cellPolID.getCellType() == cellPolID.CELL_TYPE_STRING ? cellPolID.getStringCellValue() : new BigDecimal(cellPolID.getNumericCellValue()).toString());
            reinsurer.setStPolicyNo(cellNoPolis.getStringCellValue());
            reinsurer.setStOrderNo(cellNomorUrut.getCellType() == cellNomorUrut.CELL_TYPE_STRING ? cellNomorUrut.getStringCellValue() : new BigDecimal(cellNomorUrut.getNumericCellValue()).toString());
            reinsurer.setStInsurancePolicyObjectID(cellObjectID.getCellType() == cellObjectID.CELL_TYPE_STRING ? cellObjectID.getStringCellValue() : new BigDecimal(cellObjectID.getNumericCellValue()).toString());

            reinsurer.setStNama(cellNama.getStringCellValue());
            reinsurer.setStUsia(cellUsia.getCellType() == cellUsia.CELL_TYPE_STRING ? cellUsia.getStringCellValue() : new BigDecimal(cellUsia.getNumericCellValue()).toString());
            reinsurer.setDtTanggalLahir(cellTglLahir.getDateCellValue());
            reinsurer.setDtPeriodeAwal(cellTglAwal.getDateCellValue());
            reinsurer.setDtPeriodeAkhir(cellTglAkhir.getDateCellValue());
            reinsurer.setDbTSI(new BigDecimal(cellTSI.getNumericCellValue()));
            reinsurer.setDbPremi(new BigDecimal(cellPremi.getNumericCellValue()));

            reinsurer.setStTreaty(cellTreaty.getCellType() == cellTreaty.CELL_TYPE_STRING ? cellTreaty.getStringCellValue() : new BigDecimal(cellTreaty.getNumericCellValue()).toString());

            reinsurer.setStReinsurerNoteObject(cellKetSpreadingReins.getStringCellValue());

            details.add(reinsurer);

        }

        headerReins.setDetails(details);

    }

    /**
     * @return the headerReins
     */
    public UploadHeaderReinsuranceView getHeaderReins() {
        return headerReins;
    }

    /**
     * @param headerReins the headerReins to set
     */
    public void setHeaderReins(UploadHeaderReinsuranceView headerReins) {
        this.headerReins = headerReins;
    }

    public DTOList getDetailsreins() throws Exception {
        return headerReins.getDetails();
    }

    public void delLine2() throws Exception {
        headerReins.getDetails().delete(Integer.parseInt(notesindex));
    }

    public void recalculateRI() throws Exception {
        DTOList details = headerReins.getDetails();

        BigDecimal totalTSI = null;
        BigDecimal totalPremi = null;

        for (int i = 0; i < details.size(); i++) {
            uploadReinsuranceSpreadingView object = (uploadReinsuranceSpreadingView) details.get(i);

            totalTSI = BDUtil.add(totalTSI, object.getDbTSI());
            totalPremi = BDUtil.add(totalPremi, object.getDbPremi());

        }

        for (int i = 0; i < details.size(); i++) {
            uploadReinsuranceSpreadingView object = (uploadReinsuranceSpreadingView) details.get(i);

            object.setStDataAmount(String.valueOf(details.size()));
            object.setDbTSITotal(totalTSI);
            object.setDbPremiTotal(totalPremi);
            object.setStReinsurerNote(headerReins.getStReinsurerNote());

        }

        headerReins.setStDataAmount(String.valueOf(details.size()));
        headerReins.setDbTSITotal(totalTSI);
        headerReins.setDbPremiTotal(totalPremi);
    }

    public void doSaveRI() throws Exception {

        recalculateRI();

        getRemoteInsurance().saveUploadSpreading(headerReins, headerReins.getDetails());

        super.close();

    }

    public void EditRI(String titipanID) throws Exception {

        final DTOList details = getRemoteInsurance().getInsuranceUploadReins(titipanID);

        uploadReinsuranceSpreadingView titip = (uploadReinsuranceSpreadingView) details.get(0);

        headerReins = new UploadHeaderReinsuranceView();

        // titipan.getDetails().markAllUpdate();

        headerReins.setDetails(details);

        headerReins.getDetails().markAllUpdate();

        headerReins.setStRecapNo(titip.getStRecapNo());
        headerReins.setStFilePhysic(titip.getStFilePhysic());
        headerReins.setStDataAmount(titip.getStDataAmount());
        headerReins.setDbTSITotal(titip.getDbTSITotal());
        headerReins.setDbPremiTotal(titip.getDbPremiTotal());
        headerReins.setStReinsurerNote(titip.getStReinsurerNote());
        headerReins.setStInsuranceUploadID(titip.getStInsuranceUploadID());

    }

    public kalkulatorPremiHeaderView getKalkulatorReins() {
        return kalkulatorReins;
    }

    /**
     * @param headerReins the headerReins to set
     */
    public void setHeaderReins(kalkulatorPremiHeaderView kalkulatorReins) {
        this.kalkulatorReins = kalkulatorReins;
    }

    public DTOList getDetails2() throws Exception {
        return kalkulatorReins.getDetails();
    }

    public void createNewKalkulator() throws Exception {
        kalkulatorReins = new kalkulatorPremiHeaderView();
        kalkulatorReins.markNew();

        final DTOList details = new DTOList();

        kalkulatorReins.setDetails(details);

        setTitle("Kalkulator Premi");

        editMode = true;
    }

    public void uploadExcelKalkulator() throws Exception {

        String fileID = kalkulatorReins.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Original");

        final DTOList details = new DTOList();

        kalkulatorReins.setDetails(details);

        int rows = sheet.getPhysicalNumberOfRows();

        int no = 0;
        //for (int r = 12; r < rows; r++) {
        for (int r = 6; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(1);
            if (cellControl == null) {
                break;
            }

            if (cellControl.getNumericCellValue() == 0) {
                break;
            }

            HSSFCell cellNama = row.getCell(2);
            HSSFCell cellTglLahir = row.getCell(3);
            HSSFCell cellTglAwal = row.getCell(4);
            HSSFCell cellTglAkhir = row.getCell(5);
            HSSFCell cellUsia = row.getCell(6);
            HSSFCell cellBulan = row.getCell(7);
            HSSFCell cellLama = row.getCell(8);
            HSSFCell cellKategori = row.getCell(9);
            HSSFCell cellPekerjaan = row.getCell(10);
            HSSFCell cellRate = row.getCell(11);
            HSSFCell cellTSI = row.getCell(12);
            HSSFCell cellPremi = row.getCell(13);
            HSSFCell cellPerluasan = row.getCell(14);
            HSSFCell cellTotalPremi = row.getCell(15);

            kalkulatorPremiView reinsurer = new kalkulatorPremiView();
            reinsurer.markNew();

            no++;

            reinsurer.setStOrderNo(String.valueOf(no));
            reinsurer.setStNama(cellNama.getStringCellValue());
            reinsurer.setDtTanggalLahir(cellTglLahir.getDateCellValue());
            reinsurer.setDtPeriodeAwal(cellTglAwal.getDateCellValue());
            reinsurer.setDtPeriodeAkhir(cellTglAkhir.getDateCellValue());
            reinsurer.setStUsia(cellUsia.getCellType() == cellUsia.CELL_TYPE_STRING ? cellUsia.getStringCellValue() : new BigDecimal(cellUsia.getNumericCellValue()).toString());
            reinsurer.setStBulan(cellBulan.getCellType() == cellBulan.CELL_TYPE_STRING ? cellBulan.getStringCellValue() : new BigDecimal(cellBulan.getNumericCellValue()).toString());
            reinsurer.setStLama(cellLama.getCellType() == cellLama.CELL_TYPE_STRING ? cellLama.getStringCellValue() : new BigDecimal(cellLama.getNumericCellValue()).toString());
            reinsurer.setStKategori(getKategori(cellKategori.getStringCellValue()));
            reinsurer.setStPekerjaan(getKategori(cellPekerjaan.getStringCellValue()));
            //reinsurer.setDbRate(new BigDecimal(cellRate.getNumericCellValue()));
            reinsurer.setDbTSI(new BigDecimal(cellTSI.getNumericCellValue()));
            //reinsurer.setDbPremi(new BigDecimal(cellPremi.getNumericCellValue()));
            reinsurer.setStPerluasan(cellPerluasan.getStringCellValue());
            //reinsurer.setDbPremiTotal(new BigDecimal(cellTotalPremi.getNumericCellValue()));

            details.add(reinsurer);

        }

        kalkulatorReins.setDetails(details);

    }

    public void exportToExcelKalkulator() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("data");

        final DTOList list = kalkulatorReins.getDetails();


        for (int i = 0; i < list.size(); i++) {
            kalkulatorPremiView obj = (kalkulatorPremiView) list.get(i);


            XSSFRow row0 = sheet.createRow(2);
            row0.createCell(0).setCellValue("No Urut");
            row0.createCell(1).setCellValue("Nama");
            row0.createCell(2).setCellValue("Tanggal Lahir");
            row0.createCell(3).setCellValue("Tanggal Mulai");
            row0.createCell(4).setCellValue("Tanggal Akhir");
            row0.createCell(5).setCellValue("Usia");
            row0.createCell(6).setCellValue("Bulan");
            row0.createCell(7).setCellValue("Lama");
            row0.createCell(8).setCellValue("KATEGORI");
            row0.createCell(9).setCellValue("Rate");
            row0.createCell(10).setCellValue("Harga Pertanggungan");
            row0.createCell(11).setCellValue("Premi");
            row0.createCell(12).setCellValue("Perluasan");
            row0.createCell(13).setCellValue("Premi + PHK/PAW");


            XSSFRow row = sheet.createRow(i + 3);

            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(obj.getStNama());
            row.createCell(2).setCellValue(obj.getDtTanggalLahir());
            row.createCell(3).setCellValue(obj.getDtPeriodeAwal());
            row.createCell(4).setCellValue(obj.getDtPeriodeAkhir());
            row.createCell(5).setCellValue(obj.getStUsia());
            row.createCell(6).setCellValue(obj.getStBulan());
            row.createCell(7).setCellValue(obj.getStLama());
            row.createCell(8).setCellValue(obj.getStKategori());
            row.createCell(9).setCellValue(obj.getDbRate().doubleValue());
            row.createCell(10).setCellValue(obj.getDbTSI().doubleValue());
            row.createCell(11).setCellValue(obj.getDbPremi().doubleValue());
            row.createCell(12).setCellValue(obj.getStPerluasan());
            row.createCell(13).setCellValue(obj.getDbPremiTotal().doubleValue());

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=kalkulator_premi" + "_" + System.currentTimeMillis() + ".xlsx;");
        SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public String getKategori(String stKategori) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select "
                    + "     vs_code "
                    + "   from "
                    + "         s_valueset "
                    + "   where"
                    + "      vs_group = 'KATEGORI_KREDIT' and vs_description = ?");

            S.setParam(1, stKategori);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

            return null;
        } finally {
            S.release();
        }
    }

    public String getPekerjaan(String stKategori) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select "
                    + "     vs_code "
                    + "   from "
                    + "         s_valueset "
                    + "   where"
                    + "      vs_group = 'KATEGORI_KREDIT' and vs_description = ?");

            S.setParam(1, stKategori);

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getString(1);
            }

            return null;
        } finally {
            S.release();
        }
    }

    public void recalculatePremi() throws Exception {

        final DTOList list = kalkulatorReins.getDetails();

        for (int i = 0; i < list.size(); i++) {
            kalkulatorPremiView obj = (kalkulatorPremiView) list.get(i);

            final SQLUtil S = new SQLUtil();
            
            try {

                String sql = "select rate" + obj.getStLama()
                        + " from ins_rates_big where REF1 = ? AND REF2 = '23' AND REF3 = ?"
                        + " and period_start <= ? and period_end >= ?"
                        + " and rate_class = 'KONSUMTIF_RATE'";

                final PreparedStatement PS = S.setQuery(sql);

                int n = 1;

                PS.setString(n++, obj.getStKategori());
                PS.setString(n++, obj.getStUsia());

                S.setParam(n++, obj.getDtPeriodeAwal()); //period start
                S.setParam(n++, obj.getDtPeriodeAwal()); //period end

                final ResultSet RS = PS.executeQuery();

                if (RS.next()) {
                    obj.setDbRate(RS.getBigDecimal(1));

                    obj.setDbPremi(BDUtil.mul(obj.getDbTSI(), BDUtil.getRateFromMile(obj.getDbRate())));

                    recalculatePerluasanPremi(obj);
                }

            } finally {
                S.release();
            }
        }

    }

    public void recalculatePerluasanPremi(kalkulatorPremiView obj) throws Exception {

        final SQLUtil S = new SQLUtil();
        try {

            String sql = "select rate1,rate0"
                    + " from ins_rates_big where REF1 = ? AND REF2 = '23' AND REF3 = ?"
                    + " and period_start <= ? and period_end >= ?"
                    + " and rate_class = 'PERLUASAN_RATE' and rate0 <= ?";

            final PreparedStatement PS = S.setQuery(sql);

            int n = 1;

            PS.setString(n++, obj.getStKategori());
            PS.setString(n++, obj.getStPerluasan());

            S.setParam(n++, obj.getDtPeriodeAwal()); //period start
            S.setParam(n++, obj.getDtPeriodeAwal()); //period end

            PS.setBigDecimal(n++, obj.getDbTSI());

            final ResultSet RS = PS.executeQuery();

            BigDecimal premiPerluasan = BDUtil.zero;

            if (RS.next()) {
                BigDecimal perluasanRate = RS.getBigDecimal(1);

                //( Uang Pertanggungan – Rp.30.000.000 )  X  Rate Premi PHK  X  Jangka Waktu
                premiPerluasan = BDUtil.sub(obj.getDbTSI(), RS.getBigDecimal(2));
                premiPerluasan = BDUtil.mul(premiPerluasan, BDUtil.getRateFromMile(perluasanRate));
                premiPerluasan = BDUtil.mul(premiPerluasan, new BigDecimal(obj.getStLama()));

            }
            obj.setDbPremiPerluasan(premiPerluasan);

            obj.setDbPremiTotal(BDUtil.add(obj.getDbPremi(), premiPerluasan));

        } finally {
            S.release();
        }

    }

    public void downloadFormat() throws Exception {
        String urx = "/pages/manual/docs/KALKULATOR PREMI 59.xls";
        SessionManager.getInstance().getResponse().setContentType("application/vnd.ms-excel");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=KALKULATOR PREMI 59.xls;");
        SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
        SessionManager.getInstance().getRequest().getRequestDispatcher(urx).forward(SessionManager.getInstance().getRequest(), SessionManager.getInstance().getResponse());
    }

    public void createProposalComm() throws Exception {
        headerProposal = new UploadHeaderProposalCommView();

        headerProposal.markNew();

        headerProposal.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());
        headerProposal.setDtPeriodeAwal(DateUtil.getDate("01/01/2018"));
        headerProposal.setDtPeriodeAkhir(new Date());

        final DTOList details = new DTOList();

        headerProposal.setDetails(details);

        //titipan.setStCostCenter(SessionManager.getInstance().getSession().getStBranch());

        setTitle("Proposal Komisi");

        editMode = false;
    }

    public void dataProposalComm(String titipanID) throws Exception {

        final DTOList details = getRemoteInsurance().getInsuranceProposalComm(titipanID);

        uploadProposalCommView titip = (uploadProposalCommView) details.get(0);

//        if (titip.isStatus1Flag()
//                && titip.isStatus2Flag()
//                && titip.isStatus3Flag()
//                && titip.isStatus4Flag()) {
//            throw new Exception("SHK Sudah disetujui Kadiv");
//        } else if (titip.isStatus1Flag()
//                && titip.isStatus2Flag()
//                && titip.isStatus3Flag()
//                && !titip.isStatus4Flag()) {
//            throw new Exception("SHK Sudah disetujui Kabag");
//        } else if (titip.isStatus1Flag()
//                && titip.isStatus2Flag()
//                && !titip.isStatus3Flag()
//                && !titip.isStatus4Flag()) {
//            throw new Exception("SHK Sudah disetujui Kasie");
//        } else if (titip.isStatus1Flag()
//                && !titip.isStatus2Flag()
//                && !titip.isStatus3Flag()
//                && !titip.isStatus4Flag()) {
//            throw new Exception("SHK Sudah disetujui Cabang");
//        }

        headerProposal = new UploadHeaderProposalCommView();

        // titipan.getDetails().markAllUpdate();

        headerProposal.setDetails(details);

        headerProposal.getDetails().markAllUpdate();

        headerProposal.setStCostCenterCode(titip.getStCostCenterCode());
        headerProposal.setDtPeriodeAwal(titip.getDtPeriodeAwal());
        headerProposal.setDtPeriodeAkhir(titip.getDtPeriodeAkhir());
        headerProposal.setStNoSuratHutang(titip.getStNoSuratHutang());
        headerProposal.setStFilePhysic(titip.getStFilePhysic());
        headerProposal.setStDataAmount(titip.getStDataAmount());
        headerProposal.setDbAmountTotal(titip.getDbAmountTotal());
        headerProposal.setStReinsurerNote(titip.getStReinsurerNote());
        headerProposal.setStInsuranceUploadID(titip.getStInsuranceUploadID());
        headerProposal.setStPolicyTypeGroupID(titip.getStPolicyTypeGroupID());
        headerProposal.setStPolicyTypeID(titip.getStPolicyTypeID());
        headerProposal.setStKeterangan(titip.getStKeterangan());

        editMode = false;

    }

    /**
     * @return the headerProposal
     */
    public UploadHeaderProposalCommView getHeaderProposal() {
        return headerProposal;
    }

    /**
     * @param headerProposal the headerProposal to set
     */
    public void setHeaderProposal(UploadHeaderProposalCommView headerProposal) {
        this.headerProposal = headerProposal;
    }

    public DTOList getDetailsproposal() throws Exception {
        return headerProposal.getDetails();
    }

    public void delLine3() throws Exception {
        headerProposal.getDetails().delete(Integer.parseInt(notesindex));
    }

    public void retrieveProposalComm() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.ar_invoice_id as ins_ar_invoice_id,a.attr_pol_id as ins_pol_id,"
                + "a.attr_pol_no as pol_no,a.attr_pol_name as tertanggung,a.amount ");

        sqa.addQuery(" from ar_invoice a "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id and d.tax_code is null and d.ar_trx_line_id in (8,24,40) ");

        sqa.addClause("a.no_surat_hutang is null");//PROPOSAL
        sqa.addClause("a.approved_flag is null");//PROPOSAL
        //sqa.addClause("a.surat_hutang_period_from is null");//PROPOSAL
        sqa.addClause("a.invoice_type = 'AP'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.description is null");
        sqa.addClause("a.ar_trx_type_id = 11");
        sqa.addClause("a.amount_settled is null");

        if (headerProposal.getStCostCenterCode() != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(headerProposal.getStCostCenterCode());
        }

        if (headerProposal.getStPolicyTypeID() != null) {
            sqa.addClause("a.attr_pol_type_id = ?");
            sqa.addPar(headerProposal.getStPolicyTypeID());
        }

        String premiPayment = " select g.pol_id "
                + "from ar_receipt f "
                + "inner join ar_receipt_lines g on g.receipt_id = f.ar_receipt_id "
                + "inner join ins_policy a on a.pol_id = g.pol_id "
                + "where f.status = 'POST' and f.ar_settlement_id in (1,25,38,41) and g.pol_id is not null ";

        if (headerProposal.getStCostCenterCode() != null) {
            premiPayment = premiPayment + " and f.cc_code = '" + headerProposal.getStCostCenterCode() + "'";
        }

        if (headerProposal.getStPolicyTypeID() != null) {
            premiPayment = premiPayment + " and a.pol_type_id = '" + headerProposal.getStPolicyTypeID() + "'";
        }

//        if (headerProposal.getDtPeriodeAwal() != null) {
//            premiPayment = premiPayment + " and date_trunc('day',f.receipt_date) >= '" + headerProposal.getDtPeriodeAwal() + "'";
//        }
        premiPayment = premiPayment + " and date_trunc('day',f.receipt_date) >= '2018-01-01 00:00:00'";

//        if (headerProposal.getDtPeriodeAkhir() != null) {
//            premiPayment = premiPayment + " and date_trunc('day',f.receipt_date) <= '" + headerProposal.getDtPeriodeAkhir() + "'";
//        }
        premiPayment = premiPayment + " and date_trunc('day',f.receipt_date) <= '" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd") + "'";

        premiPayment = premiPayment + " group by g.pol_id ";

        sqa.addClause("a.attr_pol_id in ( " + premiPayment + " ) ");

        String sql = sqa.getSQL() + " order by 1 ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        final DTOList details = new DTOList();

        headerProposal.setDetails(details);

        for (int i = 0; i < l.size(); i++) {
            HashDTO h = (HashDTO) l.get(i);

            uploadProposalCommView reinsurer = new uploadProposalCommView();
            reinsurer.markNew();

            reinsurer.setStInsurancePolicyID(h.getFieldValueByFieldNameBD("ins_pol_id").toString());
            reinsurer.setStPolicyNo(h.getFieldValueByFieldNameST("pol_no"));
            reinsurer.setStARInvoiceID(h.getFieldValueByFieldNameBD("ins_ar_invoice_id").toString());

            reinsurer.setStTertanggung(h.getFieldValueByFieldNameST("tertanggung"));
            reinsurer.setDtPeriodeAwal(headerProposal.getDtPeriodeAwal());
            reinsurer.setDtPeriodeAkhir(headerProposal.getDtPeriodeAkhir());
            reinsurer.setDbAmount(new BigDecimal(h.getFieldValueByFieldNameBD("amount").doubleValue()));

            details.add(reinsurer);

        }

        headerProposal.setDetails(details);
    }

    public void recalculateProposalComm() throws Exception {

        DTOList details = headerProposal.getDetails();

        BigDecimal totalAmount = null;

        for (int i = 0; i < details.size(); i++) {
            uploadProposalCommView object = (uploadProposalCommView) details.get(i);

            totalAmount = BDUtil.add(totalAmount, object.getDbAmount());

//            DTOList proposal = validatePolisAlreadyIn2(headerProposal.getStCostCenterCode());
//            for (int k = 0; k < proposal.size(); k++) {
//                uploadProposalCommView prop = (uploadProposalCommView) proposal.get(k);
//
////                UNTUK PENGECEKAN NOPOLIS
//                if (object.getStPolicyNo().equalsIgnoreCase(prop.getStPolicyNo())) {
//                    throw new RuntimeException("Polis " + prop.getStPolicyNo() + " Sudah Terinput di SHK " + prop.getStNoSuratHutang());
//                }
//            }
        }

        for (int i = 0; i < details.size(); i++) {
            uploadProposalCommView object = (uploadProposalCommView) details.get(i);

            object.setStDataAmount(String.valueOf(details.size()));
            object.setDbAmountTotal(totalAmount);
            object.setStReinsurerNote(headerProposal.getStReinsurerNote());

        }

        headerProposal.setStDataAmount(String.valueOf(details.size()));
        headerProposal.setDbAmountTotal(totalAmount);

        if (SessionManager.getInstance().getSession().getStBranch() != null) {
            if (Tools.compare(headerProposal.getDbAmountTotal(), new BigDecimal(500000001)) >= 0) {
                throw new Exception("Proposal Komisi tidak boleh melebihi 500 juta");
            }
        }
    }

    public void onChangeBranchGroup() {
    }

    public void validateInvoiceAlreadyIn(String arInvoiceID) throws Exception {
        final DTOList detail = headerProposal.getDetails();

        for (int i = 0; i < detail.size(); i++) {
            uploadProposalCommView rl = (uploadProposalCommView) detail.get(i);

            if (rl.getInvoice().getStARInvoiceID().equalsIgnoreCase(arInvoiceID)) {
                throw new RuntimeException("Invoice " + arInvoiceID + " Sudah Dipilih sebelumnya");
            }
        }
    }

    public void onNewInvoiceProposalComm() throws Exception {

        //logger.logDebug("########################## : " + parinvoiceid);

        validateInvoiceAlreadyIn(parinvoiceid);

        final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(parinvoiceid);

        final DTOList installment = getARInvoiceByAttrPolId(invoice.getStARInvoiceID());

//        for (int i = 0; i < installment.size(); i++) {
//            ARInvoiceView inv = (ARInvoiceView) installment.get(i);
//
//            final uploadProposalCommView rcl = new uploadProposalCommView();
//            rcl.markNew();
//
//            rcl.setStInsurancePolicyID(inv.getStAttrPolicyID());
//            rcl.setStPolicyNo(inv.getStInvoiceNo());
//            rcl.setStARInvoiceID(inv.getStARInvoiceID());
//
//            rcl.setStTertanggung(inv.getStAttrPolicyName());
//            rcl.setDtPeriodeAwal(headerProposal.getDtPeriodeAwal());
//            rcl.setDtPeriodeAkhir(headerProposal.getDtPeriodeAkhir());
//            rcl.setDbAmount(inv.getDbAmount());
//
//            notesindex = String.valueOf(headerProposal.getDetails().size() - 1);
//
//            onExpandInvoiceItem();
//        }

        final DTOList details = new DTOList();

        headerProposal.setDetails(details);

        for (int i = 0; i < installment.size(); i++) {
            ARInvoiceView inv = (ARInvoiceView) installment.get(i);

            uploadProposalCommView reinsurer = new uploadProposalCommView();
            reinsurer.markNew();

            reinsurer.setStInsurancePolicyID(inv.getStAttrPolicyID());
            reinsurer.setStPolicyNo(inv.getStAttrPolicyNo());
            reinsurer.setStARInvoiceID(inv.getStARInvoiceID());

            reinsurer.setStTertanggung(inv.getStAttrPolicyName());
            reinsurer.setDtPeriodeAwal(headerProposal.getDtPeriodeAwal());
            reinsurer.setDtPeriodeAkhir(headerProposal.getDtPeriodeAkhir());
            reinsurer.setDbAmount(inv.getDbAmount());

            details.add(reinsurer);

        }

        headerProposal.setDetails(details);

    }

    public void onExpandInvoiceItem() throws Exception {
        final UploadHeaderProposalCommView rl = (UploadHeaderProposalCommView) getListInvoices().get(Integer.parseInt(notesindex));

        final ARInvoiceView invoice = rl.getInvoice();

        final DTOList details = invoice.getDetails();

        for (int i = 0; i < details.size(); i++) {
            ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);

            //if (!d.isComission()) continue;

            final uploadProposalCommView rcl = new uploadProposalCommView();
            rcl.markNew();

            rcl.setStInsurancePolicyID(invoice.getStAttrPolicyID());
            rcl.setStPolicyNo(invoice.getStAttrPolicyNo());
            rcl.setStARInvoiceID(invoice.getStARInvoiceID());

            rcl.setStTertanggung(d.getStDescription());
            rcl.setDtPeriodeAwal(headerProposal.getDtPeriodeAwal());
            rcl.setDtPeriodeAkhir(headerProposal.getDtPeriodeAkhir());
            rcl.setDbAmount(invoice.getDbAmount());

            rl.getDetails().add(rcl);
        }

//        headerProposal.recalculate();
    }

    public DTOList getListInvoices() throws Exception {
        return headerProposal.getDetails();
    }

    public DTOList getARInvoiceByAttrPolId(String invoice) throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.*");

        sqa.addQuery(" from ar_invoice a "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id and d.tax_code is null and d.ar_trx_line_id in (8,24,40) ");

        String premiPayment = " select g.pol_id "
                + "from ar_receipt f "
                + "inner join ar_receipt_lines g on g.receipt_id = f.ar_receipt_id "
                + "where f.status = 'POST' and f.ar_settlement_id in (1,25) and g.pol_id is not null ";

        if (headerProposal.getDtPeriodeAwal() != null) {
            premiPayment = premiPayment + " and date_trunc('day',f.receipt_date) >= '" + headerProposal.getDtPeriodeAwal() + "'";
        }

        if (headerProposal.getDtPeriodeAkhir() != null) {
            premiPayment = premiPayment + " and date_trunc('day',f.receipt_date) <= '" + headerProposal.getDtPeriodeAkhir() + "'";
        }

        premiPayment = premiPayment + " group by g.pol_id ";

        sqa.addClause("a.attr_pol_id in ( " + premiPayment + " ) ");

        sqa.addClause("a.no_surat_hutang is null");
        sqa.addClause("a.approved_flag is null");
        sqa.addClause("a.invoice_type = 'AP'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.ar_trx_type_id = 11");
        sqa.addClause("a.amount_settled is null");
        sqa.addClause(" ar_invoice_id = ? ");
        sqa.addPar(invoice);

        sqa.addOrder(" ar_invoice_id");

        final DTOList l = sqa.getList(ARInvoiceView.class);

        return l;
    }

    public void doSaveProposalComm() throws Exception {

        recalculateProposalComm();

        getRemoteInsurance().saveUploadProposal(headerProposal, headerProposal.getDetails());

        super.close();

    }

    public void approvalProposalComm1(String titipanID) throws Exception {
        dataProposalComm(titipanID);

        final DTOList details = getRemoteInsurance().getInsuranceProposalComm(titipanID);

        uploadProposalCommView titip = (uploadProposalCommView) details.get(0);

        if (titip.isStatus1Flag()) {
            throw new Exception("Data Sudah disetujui Cabang");
        }

        super.setReadOnly(true);

        approvalCab = true;

    }

    public void approvalProposalComm2(String titipanID) throws Exception {
        dataProposalComm(titipanID);

        final DTOList details = getRemoteInsurance().getInsuranceProposalComm(titipanID);

        uploadProposalCommView titip = (uploadProposalCommView) details.get(0);

        if (!titip.isStatus1Flag()) {
            throw new Exception("Data Belum disetujui Cabang");
        }

        if (titip.isStatus2Flag()) {
            throw new Exception("Data Sudah disetujui Kasie");
        }

        super.setReadOnly(true);

        approvalSie = true;

    }

    public void approvalProposalComm3(String titipanID) throws Exception {
        dataProposalComm(titipanID);

        final DTOList details = getRemoteInsurance().getInsuranceProposalComm(titipanID);

        uploadProposalCommView titip = (uploadProposalCommView) details.get(0);

        if (!titip.isStatus1Flag()) {
            throw new Exception("Data Belum disetujui Cabang");
        }

        if (!titip.isStatus2Flag()) {
            throw new Exception("Data Belum disetujui Kasie");
        }

        if (titip.isStatus3Flag()) {
            throw new Exception("Data Sudah disetujui Kabag");
        }

        super.setReadOnly(true);

        approvalBag = true;

    }

    public void approvalProposalComm4(String titipanID) throws Exception {
        dataProposalComm(titipanID);

        final DTOList details = getRemoteInsurance().getInsuranceProposalComm(titipanID);

        uploadProposalCommView titip = (uploadProposalCommView) details.get(0);

        if (!titip.isStatus1Flag()) {
            throw new Exception("Data Belum disetujui Cabang");
        }

        if (!titip.isStatus2Flag()) {
            throw new Exception("Data Belum disetujui Kasie");
        }

        if (!titip.isStatus3Flag()) {
            throw new Exception("Data Belum disetujui Kabag");
        }

        if (titip.isStatus4Flag()) {
            throw new Exception("Data Sudah disetujui Kadiv");
        }

        super.setReadOnly(true);

        approvalDiv = true;

    }

    public void approvedProposalComm() throws Exception {

        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;

        listPolicy = ListUtil.getDTOListFromQuery(
                " select a.* from ins_proposal_komisi a "
                + "inner join ar_invoice b on b.ar_invoice_id = a.ins_ar_invoice_id "
                + " where a.ins_upload_id = ? "
                + " order by a.ins_upload_dtl_id ",
                new Object[]{headerProposal.getStInsuranceUploadID()},
                uploadProposalCommView.class);

        if (listPolicy.size() < 1) {
            throw new Exception("Data tidak ada, Hubungi KP");
        }

        for (int i = 0; i < listPolicy.size(); i++) {
            uploadProposalCommView pol = (uploadProposalCommView) listPolicy.get(i);

            if (isApprovalCab()) {

                try {
                    PreparedStatement PS = S.setQuery("update ar_invoice set no_surat_hutang = ? where ar_invoice_id = ?");

                    PS.setObject(1, pol.getStNoSuratHutang());
                    PS.setObject(2, pol.getStARInvoiceID());

                    int p = PS.executeUpdate();

                    PreparedStatement PS2 = S.setQuery("update ins_proposal_komisi set status1 = 'Y' where ins_upload_dtl_id = ?");

                    PS2.setObject(1, pol.getStInsuranceUploadDetailID());

                    int p2 = PS2.executeUpdate();

                } finally {
                    S.release();
                }

                if (isSendEmailParam()) {
                    sendEmailProposal();
                }
            }

            if (isApprovalSie()) {

                if (!isValidateJumlahSHKBeda()) {
                    throw new Exception("Jumlah Polis dalam SHK tidak sama, Hubungi KP");
                }

                boolean isValid = false;

                String cek = "select a.ar_invoice_id "
                        + "from ar_invoice a where a.ar_invoice_id = ? "
                        + "and a.amount_settled is not null and a.no_surat_hutang is not null "
                        + "and a.approved_flag is null ";

                try {
                    PreparedStatement validPS = S.setQuery(cek);
                    validPS.setString(1, pol.getStARInvoiceID());

                    ResultSet RS = validPS.executeQuery();

                    if (RS.next()) {
                        isValid = true;
                    }

                    if (isValid) {
                        String polisValid = getARInvoiceBySHK(pol.getStARInvoiceID()).getStPolicyNo();
                        throw new Exception("Polis " + polisValid + " Tidak valid/Sudah Dibayar");
                    }

                    PreparedStatement PS = S.setQuery("update ins_proposal_komisi set status2 = 'Y' where ins_upload_dtl_id = ?");

                    PS.setObject(1, pol.getStInsuranceUploadDetailID());

                    int p = PS.executeUpdate();

                } finally {
                    S.release();
                }
            }

            if (isApprovalBag()) {

                if (!isValidateJumlahSHKBeda()) {
                    throw new Exception("Jumlah Polis dalam SHK tidak sama, Hubungi KP");
                }

                boolean isValid = false;

                String cek = "select a.ar_invoice_id "
                        + "from ar_invoice a where a.ar_invoice_id = ? "
                        + "and a.amount_settled is not null and a.no_surat_hutang is not null "
                        + "and a.approved_flag is null ";

                try {
                    PreparedStatement validPS = S.setQuery(cek);
                    validPS.setString(1, pol.getStARInvoiceID());

                    ResultSet RS = validPS.executeQuery();

                    if (RS.next()) {
                        isValid = true;
                    }

                    if (isValid) {
                        String polisValid = getARInvoiceBySHK(pol.getStARInvoiceID()).getStPolicyNo();
                        throw new Exception("Polis " + polisValid + " Tidak valid/Sudah Dibayar");
                    }

                    PreparedStatement PS = S.setQuery("update ins_proposal_komisi set status3 = 'Y',kabag_approved = ? where ins_upload_dtl_id = ?");

                    PS.setObject(1, SessionManager.getInstance().getSession().getStUserID());
                    PS.setObject(2, pol.getStInsuranceUploadDetailID());

                    int p = PS.executeUpdate();

                } finally {
                    S.release();
                }
            }

            if (isApprovalDiv()) {

                if (!isValidateJumlahSHKBeda()) {
                    throw new Exception("Jumlah Polis dalam SHK tidak sama, Hubungi KP");
                }

                boolean isValid = false;

                try {
                    String cek = "select a.ar_invoice_id "
                            + "from ar_invoice a where a.ar_invoice_id = ? "
                            + "and a.amount_settled is not null and a.no_surat_hutang is not null "
                            + "and a.approved_flag is null ";

                    PreparedStatement validPS = S.setQuery(cek);
                    validPS.setString(1, pol.getStARInvoiceID());

                    ResultSet RS = validPS.executeQuery();

                    if (RS.next()) {
                        isValid = true;
                    }

                    if (isValid) {
                        String polisValid = getARInvoiceBySHK(pol.getStARInvoiceID()).getStPolicyNo();
                        throw new Exception("Polis " + polisValid + " Tidak valid/Sudah Dibayar");
                    }

                    PreparedStatement PS = S.setQuery("update ar_invoice set approved_flag = 'Y' where ar_invoice_id = ?");

                    PS.setObject(1, pol.getStARInvoiceID());

                    int p = PS.executeUpdate();

                    PreparedStatement PS2 = S.setQuery("update ins_proposal_komisi set status4 = 'Y' where ins_upload_dtl_id = ?");

                    PS2.setObject(1, pol.getStInsuranceUploadDetailID());

                    int p2 = PS2.executeUpdate();

                } finally {
                    S.release();
                }

                if (isSendEmailParam()) {
                    sendEmailApproval();
                }
            }
        }

        if (isApprovalCab()) {
        }

        if (isApprovalDiv()) {
        }

        super.close();
    }

    /**
     * @return the approvalCab
     */
    public boolean isApprovalCab() {
        return approvalCab;
    }

    /**
     * @param approvalCab the approvalCab to set
     */
    public void setApprovalCab(boolean approvalCab) {
        this.approvalCab = approvalCab;
    }

    /**
     * @return the approvalSie
     */
    public boolean isApprovalSie() {
        return approvalSie;
    }

    /**
     * @param approvalSie the approvalSie to set
     */
    public void setApprovalSie(boolean approvalSie) {
        this.approvalSie = approvalSie;
    }

    /**
     * @return the approvalBag
     */
    public boolean isApprovalBag() {
        return approvalBag;
    }

    /**
     * @param approvalBag the approvalBag to set
     */
    public void setApprovalBag(boolean approvalBag) {
        this.approvalBag = approvalBag;
    }

    /**
     * @return the approvalDiv
     */
    public boolean isApprovalDiv() {
        return approvalDiv;
    }

    /**
     * @param approvalDiv the approvalDiv to set
     */
    public void setApprovalDiv(boolean approvalDiv) {
        this.approvalDiv = approvalDiv;
    }

    /**
     * @return the canCreate
     */
    public boolean isCanCreate() {
        return canCreate;
    }

    /**
     * @param canCreate the canCreate to set
     */
    public void setCanCreate(boolean canCreate) {
        this.canCreate = canCreate;
    }

    public void editProposalComm(String titipanID) throws Exception {
        dataProposalComm(titipanID);

        final DTOList details = getRemoteInsurance().getInsuranceProposalComm(titipanID);

        uploadProposalCommView titip = (uploadProposalCommView) details.get(0);

        if (titip.isStatus1Flag()) {
            throw new Exception("Data Sudah disetujui Cabang");
        }

        //canCreate = true;
        editMode = true;

    }

    public void viewProposalComm(String titipanID) throws Exception {
        dataProposalComm(titipanID);

        final DTOList details = getRemoteInsurance().getInsuranceProposalComm(titipanID);

        uploadProposalCommView titip = (uploadProposalCommView) details.get(0);

        super.setReadOnly(true);

        viewMode = true;

    }

    public void sendEmailProposal() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {

            final Statement S = conn.createStatement();

            /* Define the SQL query */
            ResultSet query_set = S.executeQuery(
                    " select ins_upload_id,status1,status2,status3,status4,reverse_flag,cc_code,"
                    + " periode_awal,periode_akhir,no_surat_hutang,ket,"
                    + " count(ins_upload_dtl_id) as data_amount,sum(amount) as amount_total "
                    + " from ins_proposal_komisi where ins_upload_id = " + headerProposal.getStInsuranceUploadID()
                    + " group by ins_upload_id,status1,status2,status3,status4,reverse_flag,cc_code,periode_awal,periode_akhir,no_surat_hutang,ket ");

            String fileName = "proposal_komisi" + headerProposal.getStInsuranceUploadID();
            File fo = new File("C:/");

            String fileFOlder = Parameter.readString("SYS_FILES_FOLDER");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

            String sf = sdf.format(new Date());
            String tempPath = fileFOlder + File.separator + "report_temp" + File.separator + sf;
            String path1 = fileFOlder + File.separator + "report_temp" + File.separator;
            String pathTemp = tempPath + File.separator + fileName + ".pdf";

            try {
                new File(path1).mkdir();
                new File(tempPath).mkdir();
            } catch (Exception e) {
            }

            fo = new File(pathTemp);

            FileOutputStream fop = new FileOutputStream(fo);

            /* Step-2: Initialize PDF documents - logical objects */
            Document my_pdf_report = new Document();
            PdfWriter writer = PdfWriter.getInstance(my_pdf_report, fop);
            //PdfWriter.getInstance(my_pdf_report, fop);
            my_pdf_report.open();

            //we have five columns in our table
            PdfPTable my_report_logo = new PdfPTable(5);
            my_report_logo.setWidthPercentage(100);

            PdfPTable my_report_header = new PdfPTable(2);
            my_report_header.setWidthPercentage(100);

            PdfPTable my_report_table = new PdfPTable(4);
            my_report_table.setWidthPercentage(100);

            PdfPTable my_report_footer = new PdfPTable(5);
            my_report_footer.setWidthPercentage(100);
            //create a cell object

            Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12),
                    smallbold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD),
                    smallc = new Font(Font.FontFamily.TIMES_ROMAN, 11),
                    small10 = new Font(Font.FontFamily.TIMES_ROMAN, 10),
                    small10bold = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD),
                    small6 = new Font(Font.FontFamily.TIMES_ROMAN, 6);

            //insert heading
            final String no_surat[] = headerProposal.getStNoSuratHutang().split("[\\/]");
            final String norut = no_surat[0] + "/" + Parameter.readString("BRANCH_SHORT_" + headerProposal.getStCostCenterCode()).toUpperCase() + "-COMM/" + DateUtil.getMonth2Digit(new Date()) + "/" + DateUtil.getYear(new Date());

            Image askridaLogoPath = Image.getInstance(Parameter.readString("DIGITAL_POLIS_LOGO_PIC"));

            PdfPCell logo = null;
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            logo.setColspan(2);
            my_report_logo.addCell(logo);
            logo = new PdfPCell(askridaLogoPath);
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_logo.addCell(logo);
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            logo.setColspan(2);
            my_report_logo.addCell(logo);
            my_report_logo.completeRow();

            PdfPCell headerJudul = null;
            headerJudul = new PdfPCell(new Phrase("SURAT IZIN PENGELUARAN DANA", smallbold));
            headerJudul.setBorder(Rectangle.NO_BORDER);
            headerJudul.setColspan(2);
            headerJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_header.addCell(headerJudul);
            my_report_header.completeRow();

            PdfPCell headerNo = null;
            headerNo = new PdfPCell(new Phrase("No.", small));
            headerNo.setBorder(Rectangle.NO_BORDER);
            headerNo.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerNo);
            headerNo = new PdfPCell(new Phrase(": " + norut, small));
            headerNo.setBorder(Rectangle.NO_BORDER);
            headerNo.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerNo);

            PdfPCell headerName = null;
            headerName = new PdfPCell(new Phrase("Kepada Yth.", small));
            headerName.setBorder(Rectangle.NO_BORDER);
            headerName.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerName);
            headerName = new PdfPCell(new Phrase(": Kadiv. Keuangan", small));
            headerName.setBorder(Rectangle.NO_BORDER);
            headerName.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerName);

            PdfPCell headerTanggal = null;
            headerTanggal = new PdfPCell(new Phrase("Tanggal", small));
            headerTanggal.setBorder(Rectangle.NO_BORDER);
            headerTanggal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerTanggal);
//            headerTanggal = new PdfPCell(new Phrase(": " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "dd ^^ yyyy")), small));
            headerTanggal = new PdfPCell(new Phrase(": " + LanguageManager.getInstance().translate(DateUtil.getDateStr(headerProposal.getDtPeriodeAkhir(), "dd ^^ yyyy")), small));
            headerTanggal.setBorder(Rectangle.NO_BORDER);
            headerTanggal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerTanggal);

            PdfPCell headerDaerah = null;
            headerDaerah = new PdfPCell(new Phrase("Daerah", small));
            headerDaerah.setBorder(Rectangle.NO_BORDER);
            headerDaerah.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerDaerah);
            headerDaerah = new PdfPCell(new Phrase(": Cabang " + headerProposal.getCostCenter(headerProposal.getStCostCenterCode()).getStDescription(), small));
            headerDaerah.setBorder(Rectangle.NO_BORDER);
            headerDaerah.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerDaerah);

            String perihal = null;
            boolean isrevFlag = false;
            while (query_set.next()) {
                isrevFlag = Tools.isYes(query_set.getString("reverse_flag"));
            }

            if (isrevFlag) {
                perihal = ": Pembayaran Komisi No. SHK " + headerProposal.getStNoSuratHutang() + " (Revisi)";
            } else {
                perihal = ": Pembayaran Komisi No. SHK " + headerProposal.getStNoSuratHutang();
            }

            PdfPCell headerHal = null;
            headerHal = new PdfPCell(new Phrase("Perihal", small));
            headerHal.setBorder(Rectangle.NO_BORDER);
            headerHal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerHal);
            headerHal = new PdfPCell(new Phrase(perihal, small));
            headerHal.setBorder(Rectangle.NO_BORDER);
            headerHal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerHal);
            my_report_header.completeRow();

            //insert an empty row
            PdfPCell emptyRow = new PdfPCell(new Phrase(" ", small));
            emptyRow.setBorder(Rectangle.NO_BORDER);
            emptyRow.setColspan(2);
            emptyRow.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_header.addCell(emptyRow);
            my_report_header.completeRow();

            PdfPCell kolomJudul = null;
            kolomJudul = new PdfPCell(new Phrase("No", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(kolomJudul);
            kolomJudul = new PdfPCell(new Phrase("Uraian Pengeluaran", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(kolomJudul);
            kolomJudul = new PdfPCell(new Phrase("Nominal", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(kolomJudul);
            kolomJudul = new PdfPCell(new Phrase("Keterangan", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(kolomJudul);

            PdfPCell headerIsi = null;
            headerIsi = new PdfPCell(new Phrase("1", small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(headerIsi);
            headerIsi = new PdfPCell(new Phrase("Pembayaran Komisi bulan " + LanguageManager.getInstance().translate(DateUtil.getDateStr(headerProposal.getDtPeriodeAkhir(), "^^ yyyy")) + " (Polis Terlampir)", small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(headerIsi);
            headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(headerProposal.getDbAmountTotal(), 2), small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table.addCell(headerIsi);
            headerIsi = new PdfPCell(new Phrase(headerProposal.getStKeterangan(), small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(headerIsi);

            //insert footer
            String created3 = "Hormat kami,\n"
                    + "PT. ASURANSI BANGUN ASKRIDA \n"
                    + "Cabang " + headerProposal.getCostCenter(headerProposal.getStCostCenterCode()).getStDescription();
            PdfPCell footer1 = new PdfPCell(new Phrase(created3, small));
            footer1.setBorder(Rectangle.NO_BORDER);
            footer1.setColspan(5);
            footer1.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_footer.addCell(footer1);
            my_report_footer.completeRow();

            Image img = Image.getInstance(headerProposal.getUser(Parameter.readString("BRANCH_" + headerProposal.getStCostCenterCode())).getFile().getStFilePath());
            img.scaleToFit(100f, 100f);
            //insert column data
            PdfPCell footer2 = null;
            footer2 = new PdfPCell(new Phrase(" ", small));
            footer2.setColspan(2);
            footer2.setHorizontalAlignment(Element.ALIGN_LEFT);
            footer2.setBorder(Rectangle.NO_BORDER);
            my_report_footer.addCell(footer2);
            footer2 = new PdfPCell(img);
            footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
            footer2.setBorder(Rectangle.NO_BORDER);
            my_report_footer.addCell(footer2);
            footer2 = new PdfPCell(new Phrase(" ", small));
            footer2.setColspan(2);
            footer2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            footer2.setBorder(Rectangle.NO_BORDER);
            my_report_footer.addCell(footer2);

            String created4 = Parameter.readString("BRANCH_SIGN_" + headerProposal.getStCostCenterCode()) + "\n"
                    + headerProposal.getUser(Parameter.readString("BRANCH_" + headerProposal.getStCostCenterCode())).getStJobPosition().toUpperCase();
            PdfPCell footer3 = new PdfPCell(new Phrase(created4, small));
            footer3.setBorder(Rectangle.NO_BORDER);
            footer3.setColspan(5);
            footer3.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_footer.addCell(footer3);
            my_report_footer.completeRow();

            //Create QR Code by using BarcodeQRCode Class
            BarcodeQRCode my_code = new BarcodeQRCode(DateUtil.getDateStr(headerProposal.getDtPeriodeAkhir(), "dd-MMM-yyyy HH:mm:ss") + "_" + headerProposal.getStNoSuratHutang() + "_" + JSPUtil.printX(headerProposal.getDbAmountTotal(), 0), 3, 3, null);
            //Get Image corresponding to the input string
            Image qr_image = my_code.getImage();

            PdfPCell barcode = new PdfPCell(qr_image);
            barcode.setBorder(Rectangle.NO_BORDER);
            barcode.setColspan(5);
            barcode.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_footer.addCell(barcode);
            my_report_footer.completeRow();

            my_report_logo.setWidths(new int[]{20, 20, 20, 20, 20});
            my_report_header.setWidths(new int[]{15, 85});
            my_report_table.setWidths(new int[]{5, 30, 20, 45});
            my_report_footer.setWidths(new int[]{20, 20, 20, 20, 20});

            /* Attach report table to PDF */
            my_pdf_report.add(my_report_logo);
            my_pdf_report.add(my_report_header);
            my_pdf_report.add(my_report_table);
            my_pdf_report.add(my_report_footer);
            my_pdf_report.close();

            /* Define the SQL query */
            ResultSet query_set2 = S.executeQuery(
                    " select row_number() over(order by a.ar_invoice_id) as no,"
                    + " a.attr_pol_no,a.attr_pol_name,a.amount,"
                    + " (select x.receipt_no from ins_policy x where x.pol_id = a.attr_pol_id) as receipt_no "
                    + " from ar_invoice a "
                    + " where a.no_surat_hutang = '" + headerProposal.getStNoSuratHutang() + "' "
                    + " order by 1 ");

            String fileName2 = "lampiran_polis_komisi" + headerProposal.getStInsuranceUploadID();

            File fo2 = new File("C:/");

            String pathTemp2 = tempPath + File.separator + fileName2 + ".pdf";

            try {
                new File(path1).mkdir();
                new File(tempPath).mkdir();
            } catch (Exception e) {
            }

            fo2 = new File(pathTemp2);

            FileOutputStream fop2 = new FileOutputStream(fo2);

            /* Step-2: Initialize PDF documents - logical objects */
            Document my_pdf_report2 = new Document();
            PdfWriter writer2 = PdfWriter.getInstance(my_pdf_report2, fop2);
            //PdfWriter.getInstance(my_pdf_report, fop);
            my_pdf_report2.open();

            //we have four columns in our table
            PdfPTable my_report_table2 = new PdfPTable(5);
            my_report_table2.setWidthPercentage(100);

            //insert headings
            PdfPCell Title2 = new PdfPCell(new Phrase("Lampiran Surat Izin Pengeluaran Dana",
                    smallbold));
            Title2.setBorder(Rectangle.NO_BORDER);
            Title2.setColspan(5);
            Title2.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(Title2);
            my_report_table2.completeRow();

            //insert headings
            PdfPCell Title3 = new PdfPCell(new Phrase("No. : " + norut,
                    smallbold));
            Title3.setBorder(Rectangle.NO_BORDER);
            Title3.setColspan(5);
            Title3.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(Title3);
            my_report_table2.completeRow();

            //insert headings
            PdfPCell Title4 = new PdfPCell(new Phrase("No. SHK : " + headerProposal.getStNoSuratHutang(),
                    smallbold));
            Title4.setBorder(Rectangle.NO_BORDER);
            Title4.setColspan(5);
            Title4.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(Title4);
            my_report_table2.completeRow();

            PdfPCell pertanggal2 = new PdfPCell(new Phrase("Tanggal : "
                    + LanguageManager.getInstance().translate(DateUtil.getDateStr(headerProposal.getDtPeriodeAwal(), "dd ^^ yyyy"))
                    + " s/d "
                    + LanguageManager.getInstance().translate(DateUtil.getDateStr(headerProposal.getDtPeriodeAkhir(), "dd ^^ yyyy")),
                    small));
            pertanggal2.setBorder(Rectangle.NO_BORDER);
            pertanggal2.setColspan(5);
            pertanggal2.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(pertanggal2);
            my_report_table2.completeRow();

            //insert an empty row
            my_report_table2.completeRow();

            //insert column headings
            Phrase p[] = {new Phrase("No", smallbold),
                new Phrase("No. Polis", smallbold),
                new Phrase("Tertanggung", smallbold),
                new Phrase("Komisi", smallbold),
                new Phrase("Pelunasan", smallbold)};

            PdfPCell judno = new PdfPCell(p[0]),
                    judpol = new PdfPCell(p[1]),
                    judname = new PdfPCell(p[2]),
                    judkomisi = new PdfPCell(p[3]),
                    judnobuk = new PdfPCell(p[4]);

            //insert column headings
            judno.setHorizontalAlignment(judno.ALIGN_CENTER);
            judno.setVerticalAlignment(judno.ALIGN_MIDDLE);
            judno.setGrayFill(0.7f);
            judpol.setHorizontalAlignment(judpol.ALIGN_CENTER);
            judpol.setVerticalAlignment(judpol.ALIGN_MIDDLE);
            judpol.setGrayFill(0.7f);
            judname.setHorizontalAlignment(judname.ALIGN_CENTER);
            judname.setVerticalAlignment(judname.ALIGN_MIDDLE);
            judname.setGrayFill(0.7f);
            judkomisi.setHorizontalAlignment(judkomisi.ALIGN_CENTER);
            judkomisi.setVerticalAlignment(judkomisi.ALIGN_MIDDLE);
            judkomisi.setGrayFill(0.7f);
            judnobuk.setHorizontalAlignment(judnobuk.ALIGN_CENTER);
            judnobuk.setVerticalAlignment(judnobuk.ALIGN_MIDDLE);
            judnobuk.setGrayFill(0.7f);

            my_report_table2.addCell(judno);
            my_report_table2.addCell(judpol);
            my_report_table2.addCell(judname);
            my_report_table2.addCell(judkomisi);
            my_report_table2.addCell(judnobuk);

            //insert column data
            PdfPCell table_cell2 = null;
            BigDecimal komisiTotal = new BigDecimal(0);

            while (query_set2.next()) {
                BigDecimal no = query_set2.getBigDecimal("no");
                table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(no, 0), small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                my_report_table2.addCell(table_cell2);

                String pol_no = query_set2.getString("attr_pol_no");
                table_cell2 = new PdfPCell(new Phrase(pol_no, small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                my_report_table2.addCell(table_cell2);

                String custname = query_set2.getString("attr_pol_name");
                if (custname.length() > 32) {
                    custname = custname.substring(0, 32);
                } else {
                    custname = custname.substring(0, custname.length());
                }
                table_cell2 = new PdfPCell(new Phrase(custname, small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                my_report_table2.addCell(table_cell2);

                BigDecimal komisi = query_set2.getBigDecimal("amount");
                komisiTotal = BDUtil.add(komisiTotal, komisi);
                table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(komisi, 2), small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                my_report_table2.addCell(table_cell2);

                String receipt_no = query_set2.getString("receipt_no");
                table_cell2 = new PdfPCell(new Phrase(receipt_no, small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                my_report_table2.addCell(table_cell2);
            }

            table_cell2 = new PdfPCell(new Phrase("SUBTOTAL", smallbold));
            table_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell2.setColspan(3);
            my_report_table2.addCell(table_cell2);
            table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(komisiTotal, 2), small10bold));
            table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table2.addCell(table_cell2);
            table_cell2 = new PdfPCell(new Phrase(" ", small10bold));
            my_report_table2.addCell(table_cell2);

            String created = "Created by System " + LanguageManager.getInstance().translate(DateUtil.getDateStr(headerProposal.getDtPeriodeAkhir(), "dd ^^ yyyy HH:mm:ss"));
            PdfPCell bySistem2 = new PdfPCell(new Phrase(created, small6));
            bySistem2.setBorder(Rectangle.NO_BORDER);
            bySistem2.setColspan(5);
            bySistem2.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(bySistem2);
            my_report_table2.completeRow();

            //Create QR Code by using BarcodeQRCode Class
            BarcodeQRCode my_code2 = new BarcodeQRCode(DateUtil.getDateStr(headerProposal.getDtPeriodeAkhir(), "dd-MMM-yyyy HH:mm:ss") + "_" + JSPUtil.printX(komisiTotal, 2), 3, 3, null);
            //Get Image corresponding to the input string
            Image qr_image2 = my_code2.getImage();

            PdfPCell barcode2 = new PdfPCell(qr_image2);
            barcode2.setBorder(Rectangle.NO_BORDER);
            barcode2.setColspan(5);
            barcode2.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(barcode2);
            my_report_table2.completeRow();

            my_report_table2.setWidths(new int[]{5, 20, 40, 15, 20});

            /* Attach report table to PDF */
            my_pdf_report2.add(my_report_table2);
            my_pdf_report2.close();

            /* Close all DB related objects */
            query_set.close();
            query_set2.close();
            S.close();

            String receiver = Parameter.readString("FINANCE_EMAIL");
//            String receiver = "prasetyo.dwi@askrida.com";
            String subject = "Permohonan Penarikan Dana";
            String text = "Dengan hormat,\n\n"
                    + "Bersama ini kami lampirkan “" + subject + "” yang dikirim otomatis oleh sistem.\n"
                    + "Atas perhatian dan kerjasamanya, kami ucapkan terima kasih.\n\n\n"
                    + "Hormat kami,\n"
                    + "Administrator";

            if (autoEmail) {
                MailUtil2 mail = new MailUtil2();
                mail.sendEmailMultiFile(pathTemp, fileName, pathTemp2, fileName2, null, null, null, null, null, null, receiver, null, subject, text);
            }

        } finally {
            conn.close();
        }
    }

    public void sendEmailApproval() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {

            final Statement S = conn.createStatement();

            /* Define the SQL query */
            ResultSet query_set = S.executeQuery(
                    " select ins_upload_id,status1,status2,status3,status4,cc_code,"
                    + " periode_awal,periode_akhir,no_surat_hutang,kabag_approved,kadiv_approved,no_surat,"
                    + " count(ins_upload_dtl_id) as data_amount,sum(amount) as amount_total "
                    + " from ins_proposal_komisi where ins_upload_id = " + headerProposal.getStInsuranceUploadID()
                    + " group by ins_upload_id,status1,status2,status3,status4,cc_code,periode_awal,periode_akhir,no_surat_hutang,kabag_approved,kadiv_approved,no_surat ");

            String fileName = "approval_komisi" + headerProposal.getStInsuranceUploadID();
            File fo = new File("C:/");

            String fileFOlder = Parameter.readString("SYS_FILES_FOLDER");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

            Date period_akhir = null;
            String approvedKabag = null;
            String approvedKadiv = null;
            String noSurat = null;
            while (query_set.next()) {
                period_akhir = query_set.getDate("periode_akhir");
                approvedKabag = query_set.getString("kabag_approved");
                approvedKadiv = query_set.getString("kadiv_approved");
                noSurat = query_set.getString("no_surat");
            }

            String sf = sdf.format(period_akhir);
            String tempPath = fileFOlder + File.separator + "report_temp" + File.separator + sf;
            String path1 = fileFOlder + File.separator + "report_temp" + File.separator;
            String pathTemp = tempPath + File.separator + fileName + ".pdf";

            try {
                new File(path1).mkdir();
                new File(tempPath).mkdir();
            } catch (Exception e) {
            }

            fo = new File(pathTemp);

            FileOutputStream fop = new FileOutputStream(fo);

            /* Step-2: Initialize PDF documents - logical objects */
            Document my_pdf_report = new Document();
            PdfWriter writer = PdfWriter.getInstance(my_pdf_report, fop);
            //PdfWriter.getInstance(my_pdf_report, fop);
            my_pdf_report.open();

            //we have four columns in our table
            PdfPTable my_report_table = new PdfPTable(5);
            my_report_table.setWidthPercentage(100);
            PdfPTable my_report_table2 = new PdfPTable(4);
            my_report_table2.setWidthPercentage(100);
            PdfPTable my_report_table3 = new PdfPTable(5);
            my_report_table3.setWidthPercentage(100);
            //create a cell object

            Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12),
                    smallb = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD),
                    smallc = new Font(Font.FontFamily.TIMES_ROMAN, 11);

            String requestNo = null;
//            String counterKey = DateUtil.getYear2Digit(new Date()) + DateUtil.getMonth2Digit(new Date());
            String counterKey = DateUtil.getYear(new Date());
            String rn = String.valueOf(IDFactory.createNumericID("REQPROPNO" + counterKey, 1));
            rn = StringTools.leftPad(rn, '0', 3);

            requestNo = rn + "/KEU-COMM/" + DateUtil.getMonth2Digit(new Date()) + "/" + DateUtil.getYear(new Date());

            //insert heading
            String Tanggal = "Jakarta, " + LanguageManager.getInstance().translate(DateUtil.getDateStr(headerProposal.getDtPeriodeAkhir(), "dd ^^ yyyy"));
            Image askridaLogoPath = Image.getInstance(Parameter.readString("DIGITAL_POLIS_LOGO_PIC"));
            final String no_surat[] = headerProposal.getStNoSuratHutang().split("[\\/]");
            final String norutCab = no_surat[0] + "/" + Parameter.readString("BRANCH_SHORT_" + headerProposal.getStCostCenterCode()).toUpperCase() + "-COMM/" + DateUtil.getMonth2Digit(new Date()) + "/" + DateUtil.getYear(new Date());
            final String norutKP = requestNo;

            final SQLUtil SQ = new SQLUtil();
            try {
                PreparedStatement PS = SQ.setQuery("update ins_proposal_komisi set no_surat = ? where ins_upload_id = ?");

                PS.setObject(1, norutKP);
                PS.setObject(2, headerProposal.getStInsuranceUploadID());

                int p = PS.executeUpdate();
            } finally {
                SQ.release();
            }

            PdfPCell logo = null;
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setColspan(2);
            my_report_table.addCell(logo);
            logo = new PdfPCell(askridaLogoPath);
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(logo);
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setColspan(2);
            my_report_table.addCell(logo);

            PdfPCell shkTanggal = null;
            shkTanggal = new PdfPCell(new Phrase("No. : " + norutKP, small));
            shkTanggal.setBorder(Rectangle.NO_BORDER);
            shkTanggal.setHorizontalAlignment(Element.ALIGN_LEFT);
            shkTanggal.setColspan(2);
            my_report_table.addCell(shkTanggal);
            shkTanggal = new PdfPCell(new Phrase(Tanggal, small));
            shkTanggal.setBorder(Rectangle.NO_BORDER);
            shkTanggal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            shkTanggal.setColspan(3);
            my_report_table.addCell(shkTanggal);

            //insert heading
            String created1 = "Kepada Yth.\n"
                    + "Kepala Cabang " + headerProposal.getCostCenter(headerProposal.getStCostCenterCode()).getStDescription() + "\n"
                    + headerProposal.getCostCenter(headerProposal.getStCostCenterCode()).getStAddress() + "\n\n\n";
            PdfPCell heading = new PdfPCell(new Phrase(created1, small));
            heading.setBorder(Rectangle.NO_BORDER);
            heading.setColspan(2);
            heading.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(heading);
            heading = new PdfPCell(new Phrase(" ", small));
            heading.setBorder(Rectangle.NO_BORDER);
            heading.setHorizontalAlignment(Element.ALIGN_CENTER);
            heading.setColspan(3);
            my_report_table.addCell(heading);

            //insert judul
            PdfPCell Title = new PdfPCell(new Phrase("Perihal : Persetujuan Penarikan Dana",
                    smallb));
            Title.setBorder(Rectangle.NO_BORDER);
            Title.setColspan(5);
            Title.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(Title);

            //insert an empty row
            PdfPCell emptyRow = new PdfPCell(new Phrase(" ", small));
            emptyRow.setBorder(Rectangle.NO_BORDER);
            emptyRow.setColspan(5);
            emptyRow.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(emptyRow);

            //insert isi
            String created2 = "Menindaklanjuti surat dari Kantor Cabang " + headerProposal.getCostCenter(headerProposal.getStCostCenterCode()).getStDescription() + " No. " + norutCab + " perihal permohonan penarikan dana sebesar Rp. " + JSPUtil.printX(headerProposal.getDbAmountTotal(), 2) + ",- "
                    + "setelah dilakukan verifikasi atas data Kantor Cabang " + headerProposal.getCostCenter(headerProposal.getStCostCenterCode()).getStDescription() + " dengan data di Kantor Pusat, "
                    + "dengan ini Direksi menyetujui penarikan dana tersebut, dengan catatan premi atas polis-polis tersebut sudah masuk ke rekening perusahaan. "
                    + "Untuk selanjutnya setiap penarikan dana dari bank agar sesuai dengan surat Direksi No. 444/DIR/VIII/2014.\n\n"
                    + "Demikian kami sampaikan, atas perhatiannya kami ucapkan terima kasih.\n\n"
                    + "Hormat kami,";
            PdfPCell bySistem = new PdfPCell(new Phrase(created2, small));
            bySistem.setBorder(Rectangle.NO_BORDER);
            bySistem.setColspan(5);
            bySistem.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            my_report_table.addCell(bySistem);

            //insert footer
//            String created3 = "PT. ASURANSI BANGUN ASKRIDA\n"
//                    + "DIVISI KEUANGAN";
            String created3 = "PT. ASURANSI BANGUN ASKRIDA";
            PdfPCell footer1 = new PdfPCell(new Phrase(created3, smallb));
            footer1.setBorder(Rectangle.NO_BORDER);
            footer1.setColspan(5);
            footer1.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(footer1);

//            String approvedKabag = null;
//            while (query_set.next()) {
//                approvedKabag = query_set.getString("kabag_approved");
//            }

            Image imgKeuKabag = Image.getInstance(Parameter.readString("PROPOSAL_KEU_" + approvedKabag));
            Image imgKeuKasie = Image.getInstance(Parameter.readString("PROPOSAL_KASIE_KEU"));
            PdfPCell footer4 = new PdfPCell(imgKeuKabag);
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table2.addCell(footer4);
            footer4 = new PdfPCell(new Phrase("DIVISI KEUANGAN", smallb));
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table2.addCell(footer4);
            footer4 = new PdfPCell(imgKeuKasie);
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(footer4);
            footer4 = new PdfPCell(new Phrase(" ", small));
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table2.addCell(footer4);

            //Image imgKeuKadiv = Image.getInstance(Parameter.readString("PROPOSAL_KEU_" + approvedKadiv));
            //Image img = Image.getInstance(headerProposal.getUser(Parameter.readString("FINANCE_DIVISION_HEAD")).getFile().getStFilePath());
            Image img = null;
            if (approvedKadiv != null) {
                img = Image.getInstance(headerProposal.getUser(approvedKadiv).getFile().getStFilePath());
            } else {
                img = Image.getInstance(headerProposal.getUser(Parameter.readString("PROPOSAL_KEU_KADIV")).getFile().getStFilePath());
            }

            //insert column data
            PdfPCell footer2 = new PdfPCell(img);
            footer2.setBorder(Rectangle.NO_BORDER);
            footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table3.addCell(footer2);
            footer2 = new PdfPCell(new Phrase(" ", small));
            footer2.setBorder(Rectangle.NO_BORDER);
            footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
            footer2.setColspan(4);
            my_report_table3.addCell(footer2);

            String kadiv = null;
            if (approvedKadiv != null) {
                kadiv = approvedKadiv;
            } else {
                kadiv = Parameter.readString("PROPOSAL_KEU_KADIV");
            }

            String created4 = headerProposal.getUser(kadiv).getStUserName().toUpperCase() + "\n"
                    + "Kepala Divisi";
            PdfPCell footer3 = new PdfPCell(new Phrase(created4, smallb));
            footer3.setBorder(Rectangle.NO_BORDER);
            footer3.setHorizontalAlignment(Element.ALIGN_LEFT);
            footer3.setColspan(5);
            my_report_table3.addCell(footer3);

            //Create QR Code by using BarcodeQRCode Class
            BarcodeQRCode my_code = new BarcodeQRCode(DateUtil.getDateStr(headerProposal.getDtPeriodeAkhir(), "dd-MMM-yyyy HH:mm:ss") + "_" + headerProposal.getStNoSuratHutang() + "_" + JSPUtil.printX(headerProposal.getDbAmountTotal(), 0), 3, 3, null);
            //Get Image corresponding to the input string
            Image qr_image = my_code.getImage();

            PdfPCell barcode = new PdfPCell(qr_image);
            barcode.setBorder(Rectangle.NO_BORDER);
            barcode.setColspan(5);
            barcode.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table3.addCell(barcode);

            my_report_table.setWidths(new int[]{20, 20, 20, 20, 20});
            my_report_table2.setWidths(new int[]{3, 23, 3, 71});
            my_report_table3.setWidths(new int[]{20, 20, 20, 20, 20});

            /* Attach report table to PDF */
            my_pdf_report.add(my_report_table);
            my_pdf_report.add(my_report_table2);
            my_pdf_report.add(my_report_table3);
            my_pdf_report.close();

            /* Close all DB related objects */
            query_set.close();
            S.close();

//            String receiver = "prasetyo.dwi@askrida.com";
            String receiver = headerProposal.getCostCenter(headerProposal.getStCostCenterCode()).getStEmail();
            String subject = "Persetujuan Penarikan Dana";
            String text = "Dengan hormat,\n\n"
                    + "Bersama ini kami lampirkan “" + subject + "” yang dikirim otomatis oleh sistem.\n"
                    + "Atas perhatian dan kerjasamanya, kami ucapkan terima kasih.\n\n\n"
                    + "Hormat kami,\n"
                    + "Administrator";

            if (autoEmail) {
                MailUtil2 mail = new MailUtil2();
                mail.sendEmailMultiFile(pathTemp, fileName, null, null, null, null, null, null, null, null, receiver, null, subject, text);
            }

        } finally {
            conn.close();
        }
    }

    public void onChangePolicyTypeGroup() {
    }

   public void uploadExcelProposalComm() throws Exception {

        String fileID = headerProposal.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheetPolis = wb.getSheet("PROPOSAL");

        final DTOList details = new DTOList();

        headerProposal.setDetails(details);

        int rows = sheetPolis.getPhysicalNumberOfRows();

        String polno1 = "";
        String polno = "";
        HSSFRow row1 = sheetPolis.getRow(5);

        HSSFCell cellNorut = row1.getCell(2);//nomor polis
        HSSFCell cellNoPol1 = row1.getCell(3);//nomor polis

        polno1 = "'" + cellNoPol1.getStringCellValue() + "'";

        for (int r = 6; r <= rows; r++) {
            HSSFRow row = sheetPolis.getRow(r);

            HSSFCell cellControl = row.getCell(1);
            if (cellControl == null) {
                break;
            }

            if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellNoPol = row.getCell(3);//nomor polis

            polno = polno + ",'" + cellNoPol.getStringCellValue() + "'";
        }

        polno = polno1 + polno;
        logger.logDebug("@@@@@@@@@@@@@@@polno " + polno);

        DTOList proposal = validatePolisAlreadyInPolno(polno);
        for (int i = 0; i < proposal.size(); i++) {
            uploadProposalCommView dto = (uploadProposalCommView) proposal.get(0);

            if (proposal.size() >= 1) {
                throw new RuntimeException("Polis " + dto.getStPolicyNo() + " Sudah Terinput di SHK " + dto.getStNoSuratHutang());
            }
        }

        DTOList invoiceList = getARInvoiceByPolIdSet(polno);
        for (int i = 0; i < invoiceList.size(); i++) {
            HashDTO dto = (HashDTO) invoiceList.get(i);

            boolean samedata = false;

            if (i > 0) {
                HashDTO dto2 = (HashDTO) invoiceList.get(i - 1);
                String attr_pol_no = dto2.getFieldValueByFieldNameST("check");
                logger.logDebug("@@@@@@@@@@@@@@@polno1 " + dto.getFieldValueByFieldNameST("attr_pol_no"));
                logger.logDebug("@@@@@@@@@@@@@@@polno2 " + attr_pol_no);

                if (dto.getFieldValueByFieldNameST("attr_pol_no").equalsIgnoreCase(attr_pol_no)) {
                    samedata = true;
                }
            }

            if (samedata) {
                throw new RuntimeException("Polis " + dto.getFieldValueByFieldNameST("attr_pol_no") + " dobel ");
            }

            uploadProposalCommView reinsurer = new uploadProposalCommView();
            reinsurer.markNew();

            reinsurer.setStPolicyNo(dto.getFieldValueByFieldNameST("attr_pol_no"));
            reinsurer.setStInsurancePolicyID(dto.getFieldValueByFieldNameBD("attr_pol_id").toString());
            reinsurer.setStTertanggung(dto.getFieldValueByFieldNameST("attr_pol_name"));
            reinsurer.setStARInvoiceID(dto.getFieldValueByFieldNameBD("ar_invoice_id").toString());
            reinsurer.setDbAmount(dto.getFieldValueByFieldNameBD("amount"));
            reinsurer.setStFilePhysic(fileID);
            reinsurer.setStInvoiceNo(dto.getFieldValueByFieldNameST("invoice_no"));
            reinsurer.setStARInvoiceRefID(reinsurer.getInvoiceDet(dto.getFieldValueByFieldNameBD("root_id").toString()).getStARInvoiceID());

            details.add(reinsurer);
        }
    }

    public DTOList getARInvoiceByPolIdSet(String attrpolno) throws Exception {
        String ccCode = "'" + headerProposal.getStCostCenterCode() + "','80'";
//        String polno = "select pol_no from ins_policy a where ((a.cc_code = '" + headerProposal.getStCostCenterCode() + "') "
//                + " or (a.cc_code = '80' and a.cc_code_source = '" + headerProposal.getStCostCenterCode() + "')) "
//                + " and a.pol_no in (" + attrpolno + ")";

        String polno = "select pol_no from ins_policy a where ";
        if (headerProposal.getStCostCenterCodeSource() != null) {
            if (headerProposal.getStCostCenterCodeSource().equalsIgnoreCase("1")) {
                if (headerProposal.getStCostCenterCode() != null) {
                    polno = polno + "a.cc_code = '80' and a.cc_code_source = '" + headerProposal.getStCostCenterCode() + "'";
                }
            } else if (headerProposal.getStCostCenterCodeSource().equalsIgnoreCase("2")) {
                if (headerProposal.getStCostCenterCode() != null) {
                    polno = polno + "a.cc_code = '" + headerProposal.getStCostCenterCode() + "'";
                }
            }
        } else {
            if (headerProposal.getStCostCenterCode() != null) {
                polno = polno + "((a.cc_code = '" + headerProposal.getStCostCenterCode() + "') or (a.cc_code = '80' and a.cc_code_source = '" + headerProposal.getStCostCenterCode() + "'))";
            }
        }
        polno = polno + " and a.pol_no in (" + attrpolno + ")";

        String sql = "select a.ar_invoice_id,a.attr_pol_id,a.attr_pol_no,a.attr_pol_name,a.invoice_no,a.amount,b.root_id,  "
                + "(a.attr_pol_no||a.amount::text) as check "
                + "from ar_invoice a "
                + "inner join ar_invoice_details b on b.ar_invoice_id = a.ar_invoice_id and b.ar_trx_line_id in (8,24,40) "
                //                + "where a.cc_code = ? and a.attr_pol_no in (" + attrpolno + ")";
                + "where a.cc_code in (" + ccCode + ") and a.attr_pol_no in (" + polno + ")";

        sql = sql + " and a.no_surat_hutang is null";
        sql = sql + " and a.approved_flag is null";
        sql = sql + " and a.invoice_type = 'AP'";
        sql = sql + " and coalesce(a.cancel_flag,'') <> 'Y'";
        sql = sql + " and a.description is null";
        sql = sql + " and a.ar_trx_type_id = 11";
        sql = sql + " and a.amount_settled is null";
        sql = sql + " order by a.invoice_no ";

        return ListUtil.getDTOListFromQuery(
                sql,
                //                new Object[]{headerProposal.getStCostCenterCode()},
                HashDTO.class);
    }

    public DTOList validatePolisAlreadyInPolno(String attrpolno) throws Exception {
        String ccCode = "'" + headerProposal.getStCostCenterCode() + "','80'";
//        String polno = "select pol_no from ins_policy a where ((a.cc_code = '" + headerProposal.getStCostCenterCode() + "') "
//                + " or (a.cc_code = '80' and a.cc_code_source = '" + headerProposal.getStCostCenterCode() + "')) "
//                + " and a.pol_no in (" + attrpolno + ")";

        String polno = "select pol_no from ins_policy a where ";
        if (headerProposal.getStCostCenterCodeSource() != null) {
            if (headerProposal.getStCostCenterCodeSource().equalsIgnoreCase("1")) {
                if (headerProposal.getStCostCenterCode() != null) {
                    polno = polno + "a.cc_code = '80' and a.cc_code_source = '" + headerProposal.getStCostCenterCode() + "'";
                }
            } else if (headerProposal.getStCostCenterCodeSource().equalsIgnoreCase("2")) {
                if (headerProposal.getStCostCenterCode() != null) {
                    polno = polno + "a.cc_code = '" + headerProposal.getStCostCenterCode() + "'";
                }
            }
        } else {
            if (headerProposal.getStCostCenterCode() != null) {
                polno = polno + "((a.cc_code = '" + headerProposal.getStCostCenterCode() + "') or (a.cc_code = '80' and a.cc_code_source = '" + headerProposal.getStCostCenterCode() + "'))";
            }
        }
        polno = polno + " and a.pol_no in (" + attrpolno + ")";

        String sql = "select a.ar_invoice_id "
                + "from ar_invoice a "
                + "inner join ar_invoice_details b on b.ar_invoice_id = a.ar_invoice_id and b.ar_trx_line_id in (8,24,40) "
                //                + "where a.cc_code = ? and a.attr_pol_no in (" + attrpolno + ")";
                + "where a.cc_code in (" + ccCode + ") and a.attr_pol_no in (" + polno + ")";

        sql = sql + " and a.no_surat_hutang is null";
        sql = sql + " and a.approved_flag is null";
        sql = sql + " and a.invoice_type = 'AP'";
        sql = sql + " and coalesce(a.cancel_flag,'') <> 'Y'";
        sql = sql + " and a.description is null";
        sql = sql + " and a.ar_trx_type_id = 11";
        sql = sql + " and a.amount_settled is null";
        sql = sql + " order by a.invoice_no ";

        String querysql = "select * from ins_proposal_komisi where ins_ar_invoice_id in (" + sql + ") ";

        return ListUtil.getDTOListFromQuery(querysql,
                //                new Object[]{headerProposal.getStCostCenterCode()},
                uploadProposalCommView.class);
    }

    public DTOList getARInvoiceByPolId(String attrpolno) throws Exception {

        String sql = "select a.* from ar_invoice a "
                + "inner join ar_invoice_details b on b.ar_invoice_id = a.ar_invoice_id and b.ar_trx_line_id in (8,24,40) "
                + "where a.cc_code = ? and a.attr_pol_no = '" + attrpolno + "'";

        sql = sql + " and a.no_surat_hutang is null";
        sql = sql + " and a.approved_flag is null";
        sql = sql + " and a.invoice_type = 'AP'";
        sql = sql + " and coalesce(a.cancel_flag,'') <> 'Y'";
        sql = sql + " and a.description is null";
        sql = sql + " and a.ar_trx_type_id = 11";
        sql = sql + " and a.amount_settled is null";

//        String premiPayment = " select g.pol_id "
//                + "from ar_receipt f "
//                + "inner join ar_receipt_lines g on g.receipt_id = f.ar_receipt_id "
//                + "inner join ins_policy a on a.pol_id = g.pol_id "
//                + "where f.status = 'POST' and f.ar_settlement_id in (1,25,38,41) "
//                + "and g.pol_id is not null and a.pol_no = '" + attrpolno + "'  ";
//
//        if (headerProposal.getStCostCenterCode() != null) {
//            premiPayment = premiPayment + " and f.cc_code = '" + headerProposal.getStCostCenterCode() + "'";
//        }
//
//        if (headerProposal.getStPolicyTypeID() != null) {
//            premiPayment = premiPayment + " and a.pol_type_id = '" + headerProposal.getStPolicyTypeID() + "'";
//        }
//
//        premiPayment = premiPayment + " and date_trunc('day',f.receipt_date) >= '" + headerProposal.getDtPeriodeAwal() + "'";
//
//        premiPayment = premiPayment + " and date_trunc('day',f.receipt_date) <= '" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd") + " 00:00:00'";
//
//        premiPayment = premiPayment + " group by g.pol_id ";
//
//        sql = sql + " and a.attr_pol_id = ( " + premiPayment + " ) ";

        return ListUtil.getDTOListFromQuery(
                sql,
                new Object[]{headerProposal.getStCostCenterCode()}, //, attrpolno
                ARInvoiceView.class);
    }

    private ARInvoiceView getInvPolicyNo(String attrpolno) throws Exception {

        String sql = "select a.* from ar_invoice a where a.attr_pol_no = ? ";

        sql = sql + " and a.no_surat_hutang is null";
        sql = sql + " and a.approved_flag is null";
        sql = sql + " and a.invoice_type = 'AP'";
        sql = sql + " and coalesce(a.cancel_flag,'') <> 'Y'";
        sql = sql + " and a.ar_trx_type_id = 11";
        sql = sql + " and a.amount_settled is null";

        final ARInvoiceView titipan = (ARInvoiceView) ListUtil.getDTOListFromQuery(
                sql,
                new Object[]{attrpolno},
                ARInvoiceView.class).getDTO();

        return titipan;
    }

    private uploadProposalCommView getARInvoiceBySHK(String attrpolno) throws Exception {

        String sql = "select a.* "
                + "from ins_proposal_komisi a where a.ins_ar_invoice_id = ? ";
//                + "and a.ins_ar_invoice_id in ( "
//                + "select a.ar_invoice_id from ar_invoice a where "
//                + "a.ar_invoice_id = ? and a.amount_settled is not null "
//                + "and a.no_surat_hutang is not null and a.approved_flag is null) ";

        final uploadProposalCommView titipan = (uploadProposalCommView) ListUtil.getDTOListFromQuery(
                sql,
                new Object[]{attrpolno},
                uploadProposalCommView.class).getDTO();

        return titipan;
    }

    public void validatePolisAlreadyIn(String polno, BigDecimal amount) throws Exception {

        DTOList details = headerProposal.getDetails();

        for (int i = 0; i < details.size(); i++) {
            uploadProposalCommView object = (uploadProposalCommView) details.get(i);

            if (object.getStPolicyNo().equalsIgnoreCase(polno)) {
                if (Tools.isEqual(object.getDbAmount(), amount)) {
                    throw new RuntimeException("Polis " + polno + " Sudah Dipilih Sebelumnya (Double Input)");
                }
            }
        }
    }

    public DTOList validatePolisAlreadyIn2(String cc_code) throws Exception {
        return ListUtil.getDTOListFromQuery(
                "select * from ins_proposal_komisi where cc_code = ?",
                new Object[]{cc_code},
                uploadProposalCommView.class);
    }

    public boolean isValidateJumlahSHKBeda() throws Exception {

        SQLUtil S = new SQLUtil();

        boolean isValidate = false;

        try {
            String cek = " select a.no_surat_hutang,"
                    + "count(a.no_surat_hutang) as reinsurer_note,"
                    + "count(b.no_surat_hutang) as reinsurer_note_objek "
                    + "from ins_proposal_komisi a "
                    + "left join ar_invoice b on b.ar_invoice_id = a.ins_ar_invoice_id "
                    + "where a.no_surat_hutang = ? "
                    + "group by a.no_surat_hutang order by a.no_surat_hutang ";

            PreparedStatement validPS = S.setQuery(cek);
            validPS.setString(1, headerProposal.getStNoSuratHutang());

            ResultSet RS = validPS.executeQuery();

            if (RS.next()) {
                String reins1 = RS.getString("reinsurer_note");
                String reins2 = RS.getString("reinsurer_note_objek");

                if (Tools.isEqual(reins1, reins2)) {
                    isValidate = true;
                }
            }

        } finally {
            S.release();
        }

        return isValidate;
    }

    /**
     * @return the headerPiutang
     */
    public UploadHeaderPiutangPremiView getHeaderPiutang() {
        return headerPiutang;
    }

    /**
     * @param headerPiutang the headerPiutang to set
     */
    public void setHeaderPiutang(UploadHeaderPiutangPremiView headerPiutang) {
        this.headerPiutang = headerPiutang;
    }

    public DTOList getDetailspiutang() throws Exception {
        return headerPiutang.getDetails();
    }

    public void delLine4() throws Exception {
        headerPiutang.getDetails().delete(Integer.parseInt(notesindex));
    }

    public void recalculatePiutang() throws Exception {

        DTOList details = headerPiutang.getDetails();


        BigDecimal totalAmount = null;

        for (int i = 0; i < details.size(); i++) {
            uploadPiutangPremiView object = (uploadPiutangPremiView) details.get(i);

            totalAmount = BDUtil.add(totalAmount, object.getDbAmount());

        }

        for (int i = 0; i < details.size(); i++) {
            uploadPiutangPremiView object = (uploadPiutangPremiView) details.get(i);

            object.setStDataAmount(String.valueOf(details.size()));
            object.setDbAmountTotal(totalAmount);

        }

        headerPiutang.setStDataAmount(String.valueOf(details.size()));
        headerPiutang.setDbAmountTotal(totalAmount);
    }

    public void doSavePiutang() throws Exception {
        recalculatePiutang();

        getRemoteInsurance().saveWarningPiutang(headerPiutang, headerPiutang.getDetails());

        super.close();

    }

    public void editPiutangPremi(String titipanID) throws Exception {
        dataPiutangPremi(titipanID);

        final DTOList details = getRemoteInsurance().getInsurancePiutangPremi(titipanID);

        uploadPiutangPremiView titip = (uploadPiutangPremiView) details.get(0);

        editMode = true;

    }

    public void viewPiutangPremi(String titipanID) throws Exception {
        dataPiutangPremi(titipanID);

        final DTOList details = getRemoteInsurance().getInsurancePiutangPremi(titipanID);

        uploadPiutangPremiView titip = (uploadPiutangPremiView) details.get(0);

        super.setReadOnly(true);

        viewMode = true;

    }

    public void dataPiutangPremi(String titipanID) throws Exception {

        final DTOList details = getRemoteInsurance().getInsurancePiutangPremi(titipanID);

        uploadPiutangPremiView titip = (uploadPiutangPremiView) details.get(0);

        headerPiutang = new UploadHeaderPiutangPremiView();

        headerPiutang.setDetails(details);

        headerPiutang.getDetails().markAllUpdate();

        headerPiutang.setStCostCenterCode(titip.getStCostCenterCode());
        headerPiutang.setStNoSuratHutang(titip.getStNoSuratHutang());
        headerPiutang.setStFilePhysic(titip.getStFilePhysic());
        headerPiutang.setStDataAmount(titip.getStDataAmount());
        headerPiutang.setDbAmountTotal(titip.getDbAmountTotal());
        headerPiutang.setStInsuranceUploadID(titip.getStInsuranceUploadID());
        headerPiutang.setStKeterangan(titip.getStKeterangan());

        editMode = false;

    }

    public void validatePolisAlreadyIn3(String arinvocieid) throws Exception {

        DTOList details = headerProposal.getDetails();

        for (int i = 0; i < details.size(); i++) {
            uploadProposalCommView object = (uploadProposalCommView) details.get(i);

            if (object.getStARInvoiceID().equalsIgnoreCase(arinvocieid)) {
                throw new RuntimeException("ID " + arinvocieid + " Sudah Dipilih sebelumnya");
            }
        }
    }

    public void reverseProposalCommCab(String titipanID) throws Exception {
        dataProposalComm(titipanID);

        final DTOList details = getRemoteInsurance().getInsuranceProposalComm(titipanID);

        uploadProposalCommView titip = (uploadProposalCommView) details.get(0);

        if (!titip.isStatus1Flag()) {
            throw new Exception("Data Belum Disetujui Cabang");
        }

        if (titip.isStatus2Flag()) {
            throw new Exception("Data Sudah Disetujui Kantor Pusat");
        }

//        titip.setStStatus1("N");
//        titip.markUpdate();

        super.setReadOnly(true);

        editMode = true;

        approvalCab = true;

    }

    public void reverseProposalCommSie(String titipanID) throws Exception {
        dataProposalComm(titipanID);

        final DTOList details = getRemoteInsurance().getInsuranceProposalComm(titipanID);

        uploadProposalCommView titip = (uploadProposalCommView) details.get(0);

        if (!titip.isStatus1Flag()) {
            throw new Exception("Data Belum Disetujui Cabang");
        }

        if (!titip.isStatus2Flag()) {
            throw new Exception("Data Belum Disetujui Kantor Pusat");
        }

        titip.setStStatus1("N");
        titip.markUpdate();

        super.setReadOnly(true);

        editMode = true;

        approvalSie = true;

    }

   public void doReverseProposalComm() throws Exception {

        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;

        listPolicy = ListUtil.getDTOListFromQuery(
                " select a.* from ins_proposal_komisi a "
                + "inner join ar_invoice b on b.ar_invoice_id = a.ins_ar_invoice_id "
                + " where a.ins_upload_id = ? "
                + " order by a.ins_upload_dtl_id ",
                new Object[]{headerProposal.getStInsuranceUploadID()},
                uploadProposalCommView.class);

        if (listPolicy.size() < 1) {
            throw new Exception("Data tidak ada, Hubungi KP");
        }

        for (int i = 0; i < listPolicy.size(); i++) {
            uploadProposalCommView pol = (uploadProposalCommView) listPolicy.get(i);

            if (isApprovalCab()) {
                try {
                    PreparedStatement PS = S.setQuery("update ins_proposal_komisi set status1 = 'N', reverse_flag = 'Y' where ins_upload_id = ?");

                    PS.setObject(1, pol.getStInsuranceUploadID());

                    int p = PS.executeUpdate();

                    PreparedStatement PS2 = S.setQuery("update ar_invoice set no_surat_hutang = null, used_flag = 'Y' where ar_invoice_id = ?");

                    PS2.setObject(1, pol.getStARInvoiceID());

                    int p2 = PS2.executeUpdate();

                } finally {
                    S.release();
                }
            }

            if (isApprovalSie()) {

//                //CEK APAKAH KOMISI POLIS SUDAH DIBAYAR
//                String cekComm = "select b.pol_id,b.ar_invoice_no,b.receipt_no "
//                        + "from ar_receipt a "
//                        + "INNER JOIN AR_RECEIPT_LINES B ON A.AR_RECEIPT_ID = B.RECEIPT_ID and a.ar_settlement_id in (2,33,39) and a.posted_flag = 'Y' "
//                        + "where b.pol_id in ( select a.ins_pol_id from ins_proposal_komisi a "
//                        + "where a.ins_upload_id = ? ) group by b.pol_id,b.ar_invoice_no,b.receipt_no ";

                String cekComm = "select b.pol_id,b.ar_invoice_no,b.receipt_no "
                        + "from ar_receipt a "
                        + "INNER JOIN AR_RECEIPT_LINES B ON A.AR_RECEIPT_ID = B.RECEIPT_ID and a.ar_settlement_id in (2,33,39,45) and a.posted_flag = 'Y' "
                        + "where b.ar_invoice_id in ( select a.ins_ar_invoice_id from ins_proposal_komisi a "
                        + "where a.ins_upload_id = ? ) group by b.ar_invoice_no,b.pol_id,b.receipt_no ";

                PreparedStatement P = S.setQuery(cekComm);

                P.setObject(1, pol.getStInsuranceUploadID());
                ResultSet RS = P.executeQuery();

                boolean sudahBayarKomisi = false;
                String polno = null;
                String nobuk = null;
                if (RS.next()) {
                    sudahBayarKomisi = true;
                    polno = RS.getString("ar_invoice_no");
                    nobuk = RS.getString("receipt_no");
                }

                logger.logDebug("+++++++++++++ SUDAH BAYAR KOMISI : " + sudahBayarKomisi);

                if (sudahBayarKomisi) {
                    //reverse(rcp,receipt_no);
                    throw new RuntimeException("Komisi polis " + polno.replace("HUTANG Komisi : G", "") + " sudah diinput pembayaran komisi, reverse dulu Pembayaran Komisi : " + nobuk);
                }
                try {
                    PreparedStatement PS = S.setQuery("update ins_proposal_komisi set status2 = 'N', status3 = 'N', status4 = 'N' where ins_upload_id = ?");

                    PS.setObject(1, pol.getStInsuranceUploadID());

                    int p = PS.executeUpdate();

                    PreparedStatement PS2 = S.setQuery("update ar_invoice set approved_flag = null where ar_invoice_id = ?");

                    PS2.setObject(1, pol.getStARInvoiceID());

                    int p2 = PS2.executeUpdate();

                } finally {
                    S.release();
                }
            }
        }

        super.close();
    }

    public void onDeleteAll() throws Exception {
        headerProposal.getDetails().deleteAll();
    }

    /**
     * @return the reverseMode
     */
    public boolean isReverseMode() {
        return reverseMode;
    }

    /**
     * @param reverseMode the reverseMode to set
     */
    public void setReverseMode(boolean reverseMode) {
        this.reverseMode = reverseMode;
    }

    /**
     * @return the pemasaran
     */
    public BiayaPemasaranView getPemasaran() {
        return pemasaran;
    }

    /**
     * @param pemasaran the pemasaran to set
     */
    public void setPemasaran(BiayaPemasaranView pemasaran) {
        this.pemasaran = pemasaran;
    }

    public void createBiayaPemasaran() throws Exception {
        pemasaran = new BiayaPemasaranView();

        pemasaran.markNew();

        pemasaran.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());

        pemasaran.setStCurrency(CurrencyManager.getInstance().getMasterCurrency());
        pemasaran.setDbCurr(BDUtil.one);

//        pemasaran.setStYears(DateUtil.getYear(new Date()));

        pemasaran.setPmsDetail(new DTOList());
        setPemasaranID("rkappem");

        setTitle("CREATE BIAYA PEMASARAN");
    }

    public DTOList getPmsDetail() {
        return pemasaran.getPmsDetail();
    }

    public void onNewDetail() throws Exception {
        final BiayaPemasaranDetailView det = new BiayaPemasaranDetailView();

        det.markNew();

        getPmsDetail().add(det);

        String n = String.valueOf(getPmsDetail().size());
    }

    public void onDeleteDetail() throws Exception {
        getPmsDetail().delete(Integer.parseInt(notesindex));

        String n = String.valueOf(getPmsDetail().size());
    }

    public void refresh() throws Exception {
    }

    public void onChangeAmount() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.cc_code,sum(getpremiend(d.entity_id,coalesce(a.premi_total*a.ccy_rate,0),d.premi_amt*a.ccy_rate*-1)) as premi, "
                + "sum(getpremiend(d.entity_id,coalesce(a.nd_disc1*a.ccy_rate,0),((coalesce(d.disc_amount,0))*a.ccy_rate)*-1)) as diskon ");

        sqa.addQuery(" from ins_policies a "
                + "inner join ins_pol_coins d on d.policy_id = a.pol_id ");

        sqa.addClause("a.status in ('POLICY','ENDORSE','RENEWAL')");
        sqa.addClause("a.active_flag = 'Y'");
        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause("(d.entity_id <> 1 or d.coins_type <> 'COINS_COVER')");
        sqa.addClause("a.pol_type_id in (21,59)");

        if (pemasaran.getStCostCenterCode() != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(pemasaran.getStCostCenterCode());
        }

        if (pemasaran.getStMonths() != null) {
            sqa.addClause("substr(a.policy_date::text,6,2) = ?");
            sqa.addPar(pemasaran.getStMonths());
        }

        if (pemasaran.getStYears() != null) {
            sqa.addClause("substr(a.policy_date::text,1,4) = ?");
            sqa.addPar(pemasaran.getStYears());
        }

        String sql = "select a.cc_code,(a.premi-a.diskon) as biaya "
                + "from ( " + sqa.getSQL() + " group by a.cc_code  ) a ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                BiayaPemasaranView.class);

        final SQLAssembler sqa2 = new SQLAssembler();

        sqa2.addSelect("round(biaya,2) as biaya,sum(round(total_biaya,2)) as saldo ");

        sqa2.addQuery(" from biaya_pemasaran ");

        sqa2.addClause("status1 = 'Y' and status2 = 'Y' and status3 = 'Y' and status4 = 'Y'");

        if (pemasaran.getStCostCenterCode() != null) {
            sqa2.addClause("cc_code = ?");
            sqa2.addPar(pemasaran.getStCostCenterCode());
        }

        if (pemasaran.getStMonths() != null) {
            sqa2.addClause("months = ?");
            sqa2.addPar(pemasaran.getStMonths());
        }

        if (pemasaran.getStYears() != null) {
            sqa2.addClause("years = ?");
            sqa2.addPar(pemasaran.getStYears());
        }

        String sql2 = "select biaya,biaya - saldo as saldo_biaya "
                + "from ( " + sqa2.getSQL() + " group by 1) a";

        final DTOList l2 = ListUtil.getDTOListFromQuery(
                sql2,
                sqa2.getPar(),
                BiayaPemasaranView.class);

        BiayaPemasaranView close = (BiayaPemasaranView) l.get(0);
        pemasaran.setDbBiaya(BDUtil.mul(close.getDbBiaya(), new BigDecimal(0.01)));

        BiayaPemasaranView close2 = (BiayaPemasaranView) l2.get(0);
        if (l2.size() < 1) {
            pemasaran.setDbSaldoBiaya(pemasaran.getDbBiaya());
        } else {
            pemasaran.setDbSaldoBiaya(close2.getDbSaldoBiaya());
        }
    }

    public void recalculatePemasaran() throws Exception {
        /*REVISI 2*/
//        onChangeAmount();

        DTOList details = pemasaran.getPmsDetail();
        BigDecimal totalAmount = null;

        for (int i = 0; i < details.size(); i++) {
            BiayaPemasaranDetailView object = (BiayaPemasaranDetailView) details.get(i);

            totalAmount = BDUtil.add(totalAmount, object.getDbNilai());

            object.setDbExcAmount(BDUtil.mul(object.getDbNilai(), BDUtil.getRateFromPct(object.getDbExcRatePajak())));

            /*REVISI 2*/
//            if (!Tools.isEqual(DateUtil.getMonth2Digit(object.getDtApplyDate()), pemasaran.getStMonths())) {
//                throw new Exception("Tanggal pada Akun " + object.getStAccountNo() + " - " + object.getStDescription().toUpperCase() + " tidak sama dengan Tanggal Biaya");
//            }
//            if (!Tools.isEqual(DateUtil.getYear(object.getDtApplyDate()), pemasaran.getStYears())) {
//                throw new Exception("Tanggal pada Akun " + object.getStAccountNo() + " - " + object.getStDescription().toUpperCase() + " tidak sama dengan Tanggal Biaya");
//            }

            if (Tools.compare(object.getDbExcRatePajak(), BDUtil.zero) > 0) {
                if (object.getStTaxType() == null) {
                    throw new Exception("Description " + object.getStDescription() + " dengan nilai " + JSPUtil.print(object.getDbNilai(), 2) + ", Tipe Pajak belum dipilih");
                }
            }
        }

        pemasaran.setStJumlahData(String.valueOf(details.size()));
        pemasaran.setDbTotalBiaya(totalAmount);

        /*REVISI 1*/
//        if (Tools.compare(pemasaran.getDbTotalBiaya(), pemasaran.getDbSaldoBiaya()) >= 0) {
//            throw new RuntimeException("Total Biaya melebihi Saldo Biaya (" + JSPUtil.print(pemasaran.getDbTotalBiaya(), 2) + " > " + JSPUtil.print(pemasaran.getDbSaldoBiaya(), 2) + ")");
//        }

        /*REVISI 2*/
//        if (BDUtil.isNegative(BDUtil.sub(pemasaran.getDbSaldoBiaya(), totalAmount))) {
//            throw new RuntimeException("Total Biaya bulan ini melebihi Saldo Biaya " + JSPUtil.print(BDUtil.sub(pemasaran.getDbSaldoBiaya(), totalAmount), 2));
//        }
    }

    /**
     * @return the pemasaranID
     */
    public String getPemasaranID() {
        return pemasaranID;
    }

    /**
     * @param pemasaranID the pemasaranID to set
     */
    public void setPemasaranID(String pemasaranID) {
        this.pemasaranID = pemasaranID;
    }

    public void chgCurrencyPms() throws Exception {
        pemasaran.setDbCurr(CurrencyManager.getInstance().getRate(pemasaran.getStCurrency(), pemasaran.getDtEntryDate()));
    }

    public void setDate() throws Exception {

        if (pemasaran.isPosted()) {
            String tahun = pemasaran.getStYears();
            pemasaran.setStYears(null);
            throw new RuntimeException("Transaksi bulan " + pemasaran.getStMonths() + " dan tahun " + tahun + " tsb sudah diposting");
        }


        String date = DateUtil.getDays2Digit(new Date()) + "/" + pemasaran.getStMonths() + "/" + pemasaran.getStYears();

//        pemasaran.setDtEntryDate(DateUtil.getDate(date));
        pemasaran.setDtEntryDate(new Date());

    }

    public void onChgPeriod() throws Exception {

        if (!Tools.isEqual(DateUtil.getMonth2Digit(pemasaran.getDtEntryDate()), pemasaran.getStMonths())) {
            throw new Exception("Bulan pada Tanggal Biaya tidak sama dengan Bulan Transaksi");
        }
        if (!Tools.isEqual(DateUtil.getYear(pemasaran.getDtEntryDate()), pemasaran.getStYears())) {
            throw new Exception("Tahun pada Tanggal Biaya tidak sama dengan Tahun Transaksi");
        }
    }

    public void doSavePms() throws Exception {

        recalculatePemasaran();

        onChgPeriod();

        if (getPmsDetail().size() == 0) {
            throw new RuntimeException("Anggaran belum diinput");
        }

        if (getDocuments().size() == 0) {
            throw new RuntimeException("Lampiran belum diinput");
        }

        boolean adaAkunKosong = false;
        String listAkunKosong = "";
        final DTOList documents = pemasaran.getDocuments();
        for (int j = 0; j < documents.size(); j++) {
            BiayaPemasaranDocumentsView doc = (BiayaPemasaranDocumentsView) documents.get(j);

            if (doc.getStFilePhysic() == null) {
                throw new RuntimeException("File Lampiran Belum di Upload");
            }

            /*REVISI 2*/
//            if (j > 0) {
//                BiayaPemasaranDocumentsView doc2 = (BiayaPemasaranDocumentsView) documents.get(j - 1);
//                String nopol = doc2.getStKeterangan();
//                String nopol2 = doc.getStKeterangan();
//                if (nopol.equalsIgnoreCase(nopol2)) {
//                    throw new RuntimeException("No. Surat sama, tidak bisa disimpan");
//                }
//            }
//
//            if (validateNoSuratPemasaran(doc.getStKeterangan(), doc.getStPemasaranID()) != null) {
//                adaAkunKosong = true;
//                listAkunKosong = listAkunKosong + "<br>" + doc.getStKeterangan();
//            }
//
//            if (adaAkunKosong) {
//                throw new RuntimeException("No. Surat " + listAkunKosong + " Sudah Terinput. ");
//            }
        }

//        pemasaran.setStKeterangan(getStNoLampiranDesc());

        if (validateNoSuratPemasaran(pemasaran.getStKeterangan(), pemasaran.getStPemasaranID()) != null) {
            adaAkunKosong = true;
            listAkunKosong = listAkunKosong + "<br>" + pemasaran.getStKeterangan();
        }

        //logger.logDebug("@@@@@@@@@@@@@@@ " + adaAkunKosong);

        if (adaAkunKosong) {
            throw new RuntimeException("No. Surat " + listAkunKosong + " Sudah Terinput. ");
        }

        getRemoteInsurance().savePemasaran(pemasaran);

        super.close();

    }

    public void viewPemasaran(String ardepoid) throws Exception {
        setPemasaranID("rkappem");

        pemasaran = getRemoteInsurance().loadPemasaran(ardepoid);

        if (pemasaran == null) {
            throw new RuntimeException("Pemasaran not found !");
        }

        setReadOnly(true);

        setTitle("VIEW BIAYA PEMASARAN");
    }

    public void editPemasaran(String ardepoid) throws Exception {
        viewPemasaran(ardepoid);

        if (pemasaran.isStatus1Flag()) {
            throw new Exception("Data Sudah Disetujui Cabang");
        }

        setReadOnly(false);

        pemasaran.markUpdate();
        pemasaran.getPmsDetail().markAllUpdate();
        pemasaran.getDocuments().markAllUpdate();

        setTitle("EDIT BIAYA PEMASARAN");
    }

    public void approvalPms1(String ardepoid) throws Exception {
//        dataProposalComm(titipanID);
        setPemasaranID("rkappem");

        pemasaran = getRemoteInsurance().loadPemasaran(ardepoid);

        if (pemasaran.isStatus1Flag()) {
            throw new Exception("Data Sudah disetujui Cabang");
        }

        super.setReadOnly(true);

        approvalCab = true;

    }

    public void approvalPms2(String ardepoid) throws Exception {
//        dataProposalComm(titipanID);
        setPemasaranID("rkappem");

        pemasaran = getRemoteInsurance().loadPemasaran(ardepoid);

        if (!pemasaran.isStatus1Flag()) {
            throw new Exception("Data Belum disetujui Cabang");
        }

        if (pemasaran.isStatus2Flag()) {
            throw new Exception("Data Sudah disetujui Kasie");
        }

        super.setReadOnly(true);

        approvalSie = true;

    }

    public void approvalPms3(String ardepoid) throws Exception {
//        dataProposalComm(titipanID);
        setPemasaranID("rkappem");

        pemasaran = getRemoteInsurance().loadPemasaran(ardepoid);

        if (!pemasaran.isStatus1Flag()) {
            throw new Exception("Data Belum disetujui Cabang");
        }

        if (!pemasaran.isStatus2Flag()) {
            throw new Exception("Data Belum disetujui Kasie");
        }

        if (pemasaran.isStatus3Flag()) {
            throw new Exception("Data Sudah disetujui Kabag");
        }

        super.setReadOnly(true);

        approvalBag = true;

    }

    public void approvalPms4(String ardepoid) throws Exception {
//        dataProposalComm(titipanID);
        setPemasaranID("rkappem");

        pemasaran = getRemoteInsurance().loadPemasaran(ardepoid);

        if (!pemasaran.isStatus1Flag()) {
            throw new Exception("Data Belum disetujui Cabang");
        }

        if (!pemasaran.isStatus2Flag()) {
            throw new Exception("Data Belum disetujui Kasie");
        }

        if (!pemasaran.isStatus3Flag()) {
            throw new Exception("Data Belum disetujui Kabag");
        }

        if (pemasaran.isStatus4Flag()) {
            throw new Exception("Data Sudah disetujui Kadiv");
        }

        super.setReadOnly(true);

        approvalDiv = true;

    }

    public void doApprovedPms() throws Exception {
        final SQLUtil S = new SQLUtil();

//        if (pemasaran.isPosted()) {
//            throw new RuntimeException("Transaksi bulan " + pemasaran.getStMonths() + " dan tahun " + pemasaran.getStYears() + " tsb sudah diposting");
//        }

        if (isApprovalCab()) {

            try {
                PreparedStatement PS = S.setQuery("update biaya_pemasaran set status1 = 'Y',kacab_approved = ? where pms_id = ?");

                PS.setObject(1, SessionManager.getInstance().getSession().getStUserID());
                PS.setObject(2, pemasaran.getStPemasaranID());

                int p = PS.executeUpdate();

            } finally {
                S.release();
            }
            if (autoEmail) {
                sendEmailProposalBiaya();
            }
        }

        if (isApprovalSie()) {

            try {
                PreparedStatement PS = S.setQuery("update biaya_pemasaran set status2 = 'Y' where pms_id = ?");

                PS.setObject(1, pemasaran.getStPemasaranID());

                int p = PS.executeUpdate();

            } finally {
                S.release();
            }
        }

        if (isApprovalBag()) {

            try {
                PreparedStatement PS = S.setQuery("update biaya_pemasaran set status3 = 'Y',kabag_approved = ? where pms_id = ?");

                PS.setObject(1, SessionManager.getInstance().getSession().getStUserID());
                PS.setObject(2, pemasaran.getStPemasaranID());

                int p = PS.executeUpdate();

            } finally {
                S.release();
            }
        }

        if (isApprovalDiv()) {

            try {
                PreparedStatement PS = S.setQuery("update biaya_pemasaran set status4 = 'Y',kadiv_approved = ?,approved_date = ? where pms_id = ?");

                PS.setObject(1, SessionManager.getInstance().getSession().getStUserID());
                PS.setObject(2, new Date());
                PS.setObject(3, pemasaran.getStPemasaranID());

                int p = PS.executeUpdate();

            } finally {
                S.release();
            }

//            final DTOList details = pemasaran.getPmsDetail();
//            getRemoteGeneralLedger().approve(pemasaran, details);

            if (autoEmail) {
                sendEmailApprovalBiaya();
            }
        }

        close();
    }

    
    public void onDeleteAllPms() throws Exception {
        pemasaran.getPmsDetail().deleteAll();
    }

    public void doReversePms() throws Exception {
        final SQLUtil S = new SQLUtil();

        if (pemasaran.isPosted()) {
            throw new RuntimeException("Transaksi bulan " + pemasaran.getStMonths() + " dan tahun " + pemasaran.getStYears() + " tsb sudah diposting");
        }

        if (isApprovalCab()) {

            PreparedStatement PS = S.setQuery("update biaya_pemasaran set status1 = 'N', reverse_flag = 'Y' where pms_id = ?");

            PS.setObject(1, pemasaran.getStPemasaranID());

            int p = PS.executeUpdate();
        }

        if (isApprovalSie()) {

//            getRemoteGeneralLedger().reverse(pemasaran);

            PreparedStatement PS = S.setQuery("update biaya_pemasaran set status2 = 'N', status3 = 'N', status4 = 'N', no_bukti_bayar = null, tgl_bayar = null where pms_id = ?");

            PS.setObject(1, pemasaran.getStPemasaranID());

            int p = PS.executeUpdate();
        }

        close();
    }

    public void reversePmsCab(String ardepoid) throws Exception {
        setPemasaranID("rkappem");

        pemasaran = getRemoteInsurance().loadPemasaran(ardepoid);

        if (!pemasaran.isStatus1Flag()) {
            throw new Exception("Data Belum Disetujui Cabang");
        }

        if (pemasaran.isStatus2Flag()) {
            throw new Exception("Data Sudah Disetujui Kantor Pusat");
        }

//        titip.setStStatus1("N");
//        titip.markUpdate();

        super.setReadOnly(true);

        editMode = true;

        approvalCab = true;

    }

    public void reversePmsSie(String ardepoid) throws Exception {
        setPemasaranID("rkappem");

        pemasaran = getRemoteInsurance().loadPemasaran(ardepoid);

        if (!pemasaran.isStatus1Flag()) {
            throw new Exception("Data Belum Disetujui Cabang");
        }

        if (!pemasaran.isStatus2Flag()) {
            throw new Exception("Data Belum Disetujui Kantor Pusat");
        }

        pemasaran.setStStatus1("N");
        pemasaran.markUpdate();

        super.setReadOnly(true);

        editMode = true;

        approvalSie = true;

    }

    public void sendEmailProposalBiaya() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {

            final Statement S = conn.createStatement();

            /* Define the SQL query */
            ResultSet query_set = S.executeQuery(
                    " select a.pms_id,status1,status2,status3,status4,reverse_flag,cc_code,"
                    + "no_spp,ket,jumlah_data,total_biaya,ket,round(sum(b.excess_amount),2) as pajak  "
                    + "from biaya_pemasaran a "
                    + "inner join biaya_pemasaran_detail b on b.pms_id = a.pms_id "
                    + " where a.pms_id = " + pemasaran.getStPemasaranID()
                    + " group by a.pms_id,status1,status2,status3,status4,reverse_flag,cc_code,"
                    + " no_spp,ket,jumlah_data,total_biaya,ket order by a.pms_id ");

            String fileName = "proposal_biaya" + pemasaran.getStPemasaranID();
            File fo = new File("C:/");

            String fileFOlder = Parameter.readString("SYS_FILES_FOLDER");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

            String sf = sdf.format(pemasaran.getDtEntryDate());
            String tempPath = fileFOlder + File.separator + "report_temp" + File.separator + sf;
            String path1 = fileFOlder + File.separator + "report_temp" + File.separator;
            String pathTemp = tempPath + File.separator + fileName + ".pdf";

            try {
                new File(path1).mkdir();
                new File(tempPath).mkdir();
            } catch (Exception e) {
            }

            fo = new File(pathTemp);

            FileOutputStream fop = new FileOutputStream(fo);

            /* Step-2: Initialize PDF documents - logical objects */
            Document my_pdf_report = new Document();
            PdfWriter writer = PdfWriter.getInstance(my_pdf_report, fop);
            //PdfWriter.getInstance(my_pdf_report, fop);
            my_pdf_report.open();

            //we have five columns in our table
            PdfPTable my_report_logo = new PdfPTable(5);
            my_report_logo.setWidthPercentage(100);

            PdfPTable my_report_header = new PdfPTable(2);
            my_report_header.setWidthPercentage(100);

            PdfPTable my_report_table = new PdfPTable(4);
            my_report_table.setWidthPercentage(100);

            PdfPTable my_report_footer = new PdfPTable(5);
            my_report_footer.setWidthPercentage(100);
            //create a cell object

            Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12),
                    smallbold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD),
                    smallc = new Font(Font.FontFamily.TIMES_ROMAN, 11),
                    small10 = new Font(Font.FontFamily.TIMES_ROMAN, 10),
                    small10bold = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD),
                    small6 = new Font(Font.FontFamily.TIMES_ROMAN, 6);

            //insert heading
            final String no_surat[] = pemasaran.getStNoSPP().split("[\\/]");
            final String norut = no_surat[0] + "/" + Parameter.readString("BRANCH_SHORT_" + pemasaran.getStCostCenterCode()).toUpperCase() + "-BIAYA/" + DateUtil.getMonth2Digit(new Date()) + "/" + DateUtil.getYear(new Date());

            Image askridaLogoPath = Image.getInstance(Parameter.readString("DIGITAL_POLIS_LOGO_PIC"));

            PdfPCell logo = null;
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            logo.setColspan(2);
            my_report_logo.addCell(logo);
            logo = new PdfPCell(askridaLogoPath);
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_logo.addCell(logo);
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            logo.setColspan(2);
            my_report_logo.addCell(logo);
            my_report_logo.completeRow();

            PdfPCell headerJudul = null;
            headerJudul = new PdfPCell(new Phrase("SURAT IZIN PENGELUARAN DANA", smallbold));
            headerJudul.setBorder(Rectangle.NO_BORDER);
            headerJudul.setColspan(2);
            headerJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_header.addCell(headerJudul);
            my_report_header.completeRow();

            PdfPCell headerNo = null;
            headerNo = new PdfPCell(new Phrase("No.", small));
            headerNo.setBorder(Rectangle.NO_BORDER);
            headerNo.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerNo);
            headerNo = new PdfPCell(new Phrase(": " + norut, small));
            headerNo.setBorder(Rectangle.NO_BORDER);
            headerNo.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerNo);

            PdfPCell headerName = null;
            headerName = new PdfPCell(new Phrase("Kepada Yth.", small));
            headerName.setBorder(Rectangle.NO_BORDER);
            headerName.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerName);
            headerName = new PdfPCell(new Phrase(": Kadiv. Keuangan", small));
            headerName.setBorder(Rectangle.NO_BORDER);
            headerName.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerName);

            PdfPCell headerTanggal = null;
            headerTanggal = new PdfPCell(new Phrase("Tanggal", small));
            headerTanggal.setBorder(Rectangle.NO_BORDER);
            headerTanggal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerTanggal);
            headerTanggal = new PdfPCell(new Phrase(": " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "dd ^^ yyyy")), small));
            headerTanggal.setBorder(Rectangle.NO_BORDER);
            headerTanggal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerTanggal);

            PdfPCell headerDaerah = null;
            headerDaerah = new PdfPCell(new Phrase("Daerah", small));
            headerDaerah.setBorder(Rectangle.NO_BORDER);
            headerDaerah.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerDaerah);
            headerDaerah = new PdfPCell(new Phrase(": Cabang " + pemasaran.getCostCenter(pemasaran.getStCostCenterCode()).getStDescription(), small));
            headerDaerah.setBorder(Rectangle.NO_BORDER);
            headerDaerah.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerDaerah);

            String perihal = null;
            String keterangan = null;
            String taxType = null;
            String taxTypeDesc = null;
            BigDecimal dbPajak = new BigDecimal(0);
            BigDecimal dbPajakdet = new BigDecimal(0);
            boolean isrevFlag = false;
            while (query_set.next()) {
                isrevFlag = Tools.isYes(query_set.getString("reverse_flag"));
                dbPajak = query_set.getBigDecimal("pajak");
                keterangan = query_set.getString("ket");
            }

            if (isrevFlag) {
                perihal = ": Pengeluaran Biaya Pemasaran No. SBP " + pemasaran.getStNoSPP() + " (Revisi)";
            } else {
                perihal = ": Pengeluaran Biaya Pemasaran No. SBP " + pemasaran.getStNoSPP();
            }

            PdfPCell headerHal = null;
            headerHal = new PdfPCell(new Phrase("Perihal", small));
            headerHal.setBorder(Rectangle.NO_BORDER);
            headerHal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerHal);
            headerHal = new PdfPCell(new Phrase(perihal, small));
            headerHal.setBorder(Rectangle.NO_BORDER);
            headerHal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerHal);
            my_report_header.completeRow();

            //insert an empty row
            PdfPCell emptyRow = new PdfPCell(new Phrase(" ", small));
            emptyRow.setBorder(Rectangle.NO_BORDER);
            emptyRow.setColspan(2);
            emptyRow.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_header.addCell(emptyRow);
            my_report_header.completeRow();

            PdfPCell kolomJudul = null;
            kolomJudul = new PdfPCell(new Phrase("No", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(kolomJudul);
            kolomJudul = new PdfPCell(new Phrase("Uraian Pengeluaran", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(kolomJudul);
            kolomJudul = new PdfPCell(new Phrase("Nominal", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(kolomJudul);
            kolomJudul = new PdfPCell(new Phrase("Keterangan", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(kolomJudul);

            PdfPCell headerIsi = null;
            headerIsi = new PdfPCell(new Phrase("1", small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(headerIsi);
            headerIsi = new PdfPCell(new Phrase("Biaya Pemasaran bulan " + LanguageManager.getInstance().translate(DateUtil.getDateStr(pemasaran.getDtEntryDate(), "^^ yyyy")) + " (Rincian Terlampir)", small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(headerIsi);
            headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(BDUtil.sub(pemasaran.getDbTotalBiaya(), dbPajak), 2), small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table.addCell(headerIsi);
            headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(keterangan), small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(headerIsi);

            ResultSet query_tax = S.executeQuery(
                    " select row_number() over(order by a.pms_id,b.tax_type)+1 as no,b.tax_type,round(sum(b.excess_amount),2) as pajak  "
                    + "from biaya_pemasaran a inner join biaya_pemasaran_detail b on b.pms_id = a.pms_id "
                    + "where a.pms_id = " + pemasaran.getStPemasaranID()
                    + " and b.excess_amount <> 0 group by a.pms_id,b.tax_type order by b.tax_type ");

            while (query_tax.next()) {
                taxType = query_tax.getString("tax_type");

                if (taxType.equalsIgnoreCase("1")) {
                    taxTypeDesc = "Pph 21";
                } else {
                    taxTypeDesc = "Pph 23";
                }

                BigDecimal no = query_tax.getBigDecimal("no");
                headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(no, 0), small));
                headerIsi.setHorizontalAlignment(Element.ALIGN_CENTER);
                my_report_table.addCell(headerIsi);
                headerIsi = new PdfPCell(new Phrase(taxTypeDesc, small));
                headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
                my_report_table.addCell(headerIsi);
                dbPajakdet = query_tax.getBigDecimal("pajak");
                headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(dbPajakdet, 2), small));
                headerIsi.setHorizontalAlignment(Element.ALIGN_RIGHT);
                my_report_table.addCell(headerIsi);
                headerIsi = new PdfPCell(new Phrase(" ", small));
                headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
                my_report_table.addCell(headerIsi);
            }

//            if (Tools.compare(dbPajak, BDUtil.zero) > 0) {
//                headerIsi = new PdfPCell(new Phrase("2", small));
//                headerIsi.setHorizontalAlignment(Element.ALIGN_CENTER);
//                my_report_table.addCell(headerIsi);
//                headerIsi = new PdfPCell(new Phrase(taxTypeDesc, small));
//                headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
//                my_report_table.addCell(headerIsi);
//                headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(dbPajakdet, 2), small));
//                headerIsi.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                my_report_table.addCell(headerIsi);
//                headerIsi = new PdfPCell(new Phrase(" ", small));
//                headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
//                my_report_table.addCell(headerIsi);
//            }

            PdfPCell headerTotal = null;
            headerTotal = new PdfPCell(new Phrase("TOTAL", small));
            headerTotal.setColspan(2);
            headerTotal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(headerTotal);
            headerTotal = new PdfPCell(new Phrase(JSPUtil.printX(pemasaran.getDbTotalBiaya(), 2), small));
            headerTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table.addCell(headerTotal);
            headerTotal = new PdfPCell(new Phrase(" ", small));
            headerTotal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(headerTotal);

            //insert footer
            String created3 = "Hormat kami,\n"
                    + "PT. ASURANSI BANGUN ASKRIDA \n"
                    + "Cabang " + pemasaran.getCostCenter(pemasaran.getStCostCenterCode()).getStDescription();
            PdfPCell footer1 = new PdfPCell(new Phrase(created3, small));
            footer1.setBorder(Rectangle.NO_BORDER);
            footer1.setColspan(5);
            footer1.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_footer.addCell(footer1);
            my_report_footer.completeRow();

            Image img = Image.getInstance(pemasaran.getUser(Parameter.readString("BRANCH_" + pemasaran.getStCostCenterCode())).getFile().getStFilePath());
            img.scaleToFit(100f, 100f);
            //insert column data
            PdfPCell footer2 = null;
            footer2 = new PdfPCell(new Phrase(" ", small));
            footer2.setColspan(2);
            footer2.setHorizontalAlignment(Element.ALIGN_LEFT);
            footer2.setBorder(Rectangle.NO_BORDER);
            my_report_footer.addCell(footer2);
            footer2 = new PdfPCell(img);
            footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
            footer2.setBorder(Rectangle.NO_BORDER);
            my_report_footer.addCell(footer2);
            footer2 = new PdfPCell(new Phrase(" ", small));
            footer2.setColspan(2);
            footer2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            footer2.setBorder(Rectangle.NO_BORDER);
            my_report_footer.addCell(footer2);

            String created4 = Parameter.readString("BRANCH_SIGN_" + pemasaran.getStCostCenterCode()) + "\n"
                    + pemasaran.getUser(Parameter.readString("BRANCH_" + pemasaran.getStCostCenterCode())).getStJobPosition().toUpperCase();
            PdfPCell footer3 = new PdfPCell(new Phrase(created4, small));
            footer3.setBorder(Rectangle.NO_BORDER);
            footer3.setColspan(5);
            footer3.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_footer.addCell(footer3);
            my_report_footer.completeRow();

            //Create QR Code by using BarcodeQRCode Class
            BarcodeQRCode my_code = new BarcodeQRCode(DateUtil.getDateStr(new Date(), "dd-MMM-yyyy HH:mm:ss") + "_" + pemasaran.getStNoSPP() + "_" + JSPUtil.printX(pemasaran.getDbSaldoBiaya(), 0), 3, 3, null);
            //Get Image corresponding to the input string
            Image qr_image = my_code.getImage();

            PdfPCell barcode = new PdfPCell(qr_image);
            barcode.setBorder(Rectangle.NO_BORDER);
            barcode.setColspan(5);
            barcode.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_footer.addCell(barcode);
            my_report_footer.completeRow();

            my_report_logo.setWidths(new int[]{20, 20, 20, 20, 20});
            my_report_header.setWidths(new int[]{15, 85});
            my_report_table.setWidths(new int[]{5, 30, 20, 45});
            my_report_footer.setWidths(new int[]{20, 20, 20, 20, 20});

            /* Attach report table to PDF */
            my_pdf_report.add(my_report_logo);
            my_pdf_report.add(my_report_header);
            my_pdf_report.add(my_report_table);
            my_pdf_report.add(my_report_footer);
            my_pdf_report.close();

            /* Define the SQL query */
            ResultSet query_set2 = S.executeQuery(
                    " select row_number() over(order by b.pms_det_id) as no,"
                    + "b.accountno,b.description,b.nilai,b.applydate,a.no_bukti "
                    + "from biaya_pemasaran a "
                    + "inner join biaya_pemasaran_detail b on b.pms_id = a.pms_id "
                    + "where a.no_spp = '" + pemasaran.getStNoSPP() + "'"
                    + "order by 1 ");

            String fileName2 = "lampiran_biaya" + pemasaran.getStPemasaranID();

            File fo2 = new File("C:/");

            String pathTemp2 = tempPath + File.separator + fileName2 + ".pdf";

            try {
                new File(path1).mkdir();
                new File(tempPath).mkdir();
            } catch (Exception e) {
            }

            fo2 = new File(pathTemp2);

            FileOutputStream fop2 = new FileOutputStream(fo2);

            /* Step-2: Initialize PDF documents - logical objects */
            Document my_pdf_report2 = new Document();
            PdfWriter writer2 = PdfWriter.getInstance(my_pdf_report2, fop2);
            //PdfWriter.getInstance(my_pdf_report, fop);
            my_pdf_report2.open();

            //we have four columns in our table
            PdfPTable my_report_table2 = new PdfPTable(4);
            my_report_table2.setWidthPercentage(100);

            //insert headings
            PdfPCell Title2 = new PdfPCell(new Phrase("Lampiran Surat Izin Pengeluaran Dana",
                    smallbold));
            Title2.setBorder(Rectangle.NO_BORDER);
            Title2.setColspan(4);
            Title2.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(Title2);
            my_report_table2.completeRow();

            //insert headings
            PdfPCell Title3 = new PdfPCell(new Phrase("No. : " + norut,
                    smallbold));
            Title3.setBorder(Rectangle.NO_BORDER);
            Title3.setColspan(4);
            Title3.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(Title3);
            my_report_table2.completeRow();

            //insert headings
            PdfPCell Title4 = new PdfPCell(new Phrase("No. SBP : " + pemasaran.getStNoSPP(),
                    smallbold));
            Title4.setBorder(Rectangle.NO_BORDER);
            Title4.setColspan(4);
            Title4.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(Title4);
            my_report_table2.completeRow();

//            PdfPCell pertanggal2 = new PdfPCell(new Phrase("Tanggal : "
//                    + LanguageManager.getInstance().translate(DateUtil.getDateStr(pemasaran.getDtPeriodeAwal(), "dd ^^ yyyy"))
//                    + " s/d "
//                    + LanguageManager.getInstance().translate(DateUtil.getDateStr(pemasaran.getDtPeriodeAkhir(), "dd ^^ yyyy")),
//                    small));
//            pertanggal2.setBorder(Rectangle.NO_BORDER);
//            pertanggal2.setColspan(5);
//            pertanggal2.setHorizontalAlignment(Element.ALIGN_LEFT);
//            my_report_table2.addCell(pertanggal2);
//            my_report_table2.completeRow();

            //insert an empty row
            my_report_table2.completeRow();

            //insert column headings
            Phrase p[] = {new Phrase("No", smallbold),
                new Phrase("No. Akun", smallbold),
                new Phrase("Keterangan", smallbold),
                new Phrase("Nilai", smallbold) //,
            //                new Phrase("Tanggal", smallbold),
            //                new Phrase("No. Bukti", smallbold)
            };

            PdfPCell judno = new PdfPCell(p[0]),
                    judpol = new PdfPCell(p[1]),
                    judname = new PdfPCell(p[2]),
                    judkomisi = new PdfPCell(p[3]) //,
                    //                    judtanggal = new PdfPCell(p[4]),
                    //                    judnobuk = new PdfPCell(p[5])
                    ;

            //insert column headings
            judno.setHorizontalAlignment(judno.ALIGN_CENTER);
            judno.setVerticalAlignment(judno.ALIGN_MIDDLE);
            judno.setGrayFill(0.7f);
            judpol.setHorizontalAlignment(judpol.ALIGN_CENTER);
            judpol.setVerticalAlignment(judpol.ALIGN_MIDDLE);
            judpol.setGrayFill(0.7f);
            judname.setHorizontalAlignment(judname.ALIGN_CENTER);
            judname.setVerticalAlignment(judname.ALIGN_MIDDLE);
            judname.setGrayFill(0.7f);
            judkomisi.setHorizontalAlignment(judkomisi.ALIGN_CENTER);
            judkomisi.setVerticalAlignment(judkomisi.ALIGN_MIDDLE);
            judkomisi.setGrayFill(0.7f);
//            judtanggal.setHorizontalAlignment(judtanggal.ALIGN_CENTER);
//            judtanggal.setVerticalAlignment(judtanggal.ALIGN_MIDDLE);
//            judtanggal.setGrayFill(0.7f);
//            judnobuk.setHorizontalAlignment(judnobuk.ALIGN_CENTER);
//            judnobuk.setVerticalAlignment(judnobuk.ALIGN_MIDDLE);
//            judnobuk.setGrayFill(0.7f);

            my_report_table2.addCell(judno);
            my_report_table2.addCell(judpol);
            my_report_table2.addCell(judname);
            my_report_table2.addCell(judkomisi);
//            my_report_table2.addCell(judtanggal);
//            my_report_table2.addCell(judnobuk);

            //insert column data
            PdfPCell table_cell2 = null;
            BigDecimal komisiTotal = new BigDecimal(0);

            while (query_set2.next()) {
                BigDecimal no = query_set2.getBigDecimal("no");
                table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(no, 0), small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                my_report_table2.addCell(table_cell2);

                String pol_no = query_set2.getString("accountno");
                table_cell2 = new PdfPCell(new Phrase(pol_no, small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                my_report_table2.addCell(table_cell2);

                String custname = query_set2.getString("description");
                if (custname.length() > 32) {
                    custname = custname.substring(0, 32);
                } else {
                    custname = custname.substring(0, custname.length());
                }
                table_cell2 = new PdfPCell(new Phrase(custname, small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                my_report_table2.addCell(table_cell2);

                BigDecimal komisi = query_set2.getBigDecimal("nilai");
                komisiTotal = BDUtil.add(komisiTotal, komisi);
                table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(komisi, 2), small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                my_report_table2.addCell(table_cell2);

//                Date tanggal = query_set2.getDate("applydate");
//                table_cell2 = new PdfPCell(new Phrase(LanguageManager.getInstance().translate(DateUtil.getDateStr(tanggal, "dd ^^ yyyy")), small10));
//                table_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
//                my_report_table2.addCell(table_cell2);
//
//                String receipt_no = query_set2.getString("no_bukti");
//                table_cell2 = new PdfPCell(new Phrase(receipt_no, small10));
//                table_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
//                my_report_table2.addCell(table_cell2);
            }

            table_cell2 = new PdfPCell(new Phrase("SUBTOTAL", smallbold));
            table_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell2.setColspan(3);
            my_report_table2.addCell(table_cell2);
            table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(komisiTotal, 2), small10bold));
            table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table2.addCell(table_cell2);
//            table_cell2 = new PdfPCell(new Phrase(" ", small10bold));
//            table_cell2.setColspan(2);
//            my_report_table2.addCell(table_cell2);

            String created = "Created by System " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "dd ^^ yyyy HH:mm:ss"));
            PdfPCell bySistem2 = new PdfPCell(new Phrase(created, small6));
            bySistem2.setBorder(Rectangle.NO_BORDER);
            bySistem2.setColspan(4);
            bySistem2.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(bySistem2);
            my_report_table2.completeRow();

            //Create QR Code by using BarcodeQRCode Class
            BarcodeQRCode my_code2 = new BarcodeQRCode(DateUtil.getDateStr(new Date(), "dd-MMM-yyyy HH:mm:ss") + "_" + JSPUtil.printX(komisiTotal, 2), 3, 3, null);
            //Get Image corresponding to the input string
            Image qr_image2 = my_code2.getImage();

            PdfPCell barcode2 = new PdfPCell(qr_image2);
            barcode2.setBorder(Rectangle.NO_BORDER);
            barcode2.setColspan(4);
            barcode2.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(barcode2);
            my_report_table2.completeRow();

//            my_report_table2.setWidths(new int[]{5, 20, 25, 15, 15, 20});
            my_report_table2.setWidths(new int[]{5, 20, 25, 15});

            /* Attach report table to PDF */
            my_pdf_report2.add(my_report_table2);
            my_pdf_report2.close();

            /* Close all DB related objects */
            query_set.close();
            query_tax.close();
            query_set2.close();
            S.close();

            String receiver = Parameter.readString("FINANCE_EMAIL");
//            String receiver = "dwi.puspita@askrida.com";
//            String receiver = "prasetyo@askrida.co.id";
            String subject = "Permohonan Penggunaan Biaya Pemasaran";
            String text = "Dengan hormat,\n\n"
                    + "Bersama ini kami lampirkan “" + subject + "” yang dikirim otomatis oleh sistem.\n"
                    + "Atas perhatian dan kerjasamanya, kami ucapkan terima kasih.\n\n\n"
                    + "Hormat kami,\n"
                    + "Administrator";

            if (autoEmail) {
                MailUtil2 mail = new MailUtil2();
                mail.sendEmailMultiFile(pathTemp, fileName, pathTemp2, fileName2, null, null, null, null, null, null, receiver, null, subject, text);
            }

        } finally {
            conn.close();
        }
    }

    public void sendEmailApprovalBiaya() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {

            final Statement S = conn.createStatement();

            /* Define the SQL query */
//            ResultSet query_set = S.executeQuery(
//                    " select pms_id,status1,status2,status3,status4,reverse_flag,cc_code,"
//                    + " no_spp,ket,jumlah_data,total_biaya,kabag_approved "
//                    + " from biaya_pemasaran where pms_id = " + pemasaran.getStPemasaranID()
//                    + " order by pms_id ");

            ResultSet query_set = S.executeQuery(
                    " select a.pms_id,status1,status2,status3,status4,reverse_flag,cc_code,"
                    + "no_spp,ket,jumlah_data,total_biaya,kabag_approved,ket,round(sum(b.excess_amount),2) as pajak  "
                    + "from biaya_pemasaran a "
                    + "inner join biaya_pemasaran_detail b on b.pms_id = a.pms_id "
                    + " where a.pms_id = " + pemasaran.getStPemasaranID()
                    + " group by a.pms_id,status1,status2,status3,status4,reverse_flag,cc_code,"
                    + " no_spp,ket,jumlah_data,total_biaya,ket order by a.pms_id ");

            String fileName = "approval_biaya" + pemasaran.getStPemasaranID();
            File fo = new File("C:/");

            String fileFOlder = Parameter.readString("SYS_FILES_FOLDER");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

            String sf = sdf.format(pemasaran.getDtEntryDate());
            String tempPath = fileFOlder + File.separator + "report_temp" + File.separator + sf;
            String path1 = fileFOlder + File.separator + "report_temp" + File.separator;
            String pathTemp = tempPath + File.separator + fileName + ".pdf";

            try {
                new File(path1).mkdir();
                new File(tempPath).mkdir();
            } catch (Exception e) {
            }

            fo = new File(pathTemp);

            FileOutputStream fop = new FileOutputStream(fo);

            /* Step-2: Initialize PDF documents - logical objects */
            Document my_pdf_report = new Document();
            PdfWriter writer = PdfWriter.getInstance(my_pdf_report, fop);
            //PdfWriter.getInstance(my_pdf_report, fop);
            my_pdf_report.open();

            //we have four columns in our table
            PdfPTable my_report_table = new PdfPTable(5);
            my_report_table.setWidthPercentage(100);
            PdfPTable my_report_table2 = new PdfPTable(4);
            my_report_table2.setWidthPercentage(100);
            PdfPTable my_report_table4 = new PdfPTable(4);
            my_report_table4.setWidthPercentage(100);
            PdfPTable my_report_table3 = new PdfPTable(5);
            my_report_table3.setWidthPercentage(100);
            //create a cell object

            Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12),
                    smallb = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD),
                    smallc = new Font(Font.FontFamily.TIMES_ROMAN, 11),
                    smallbold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

            String requestNo = null;
//            String counterKey = DateUtil.getYear2Digit(new Date()) + DateUtil.getMonth2Digit(new Date());
            String counterKey = DateUtil.getYear(new Date());
            String rn = String.valueOf(IDFactory.createNumericID("REQBIAYAPMSNO" + counterKey, 1));
            rn = StringTools.leftPad(rn, '0', 3);

            requestNo = rn + "/KEU-BIAYA/" + DateUtil.getMonth2Digit(new Date()) + "/" + DateUtil.getYear(new Date());

            //insert heading
            String Tanggal = "Jakarta, " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "dd ^^ yyyy"));
            Image askridaLogoPath = Image.getInstance(Parameter.readString("DIGITAL_POLIS_LOGO_PIC"));
            final String no_surat[] = pemasaran.getStNoSPP().split("[\\/]");
            final String norutCab = no_surat[0] + "/" + Parameter.readString("BRANCH_SHORT_" + pemasaran.getStCostCenterCode()).toUpperCase() + "-BIAYA/" + DateUtil.getMonth2Digit(new Date()) + "/" + DateUtil.getYear(new Date());
            final String norutKP = requestNo;

            final SQLUtil SQ = new SQLUtil();
            try {
                PreparedStatement PS = SQ.setQuery("update biaya_pemasaran set no_bukti = ? where pms_id = ?");

                PS.setObject(1, norutKP);
                PS.setObject(2, pemasaran.getStPemasaranID());

                int p = PS.executeUpdate();
            } finally {
                SQ.release();
            }

            PdfPCell logo = null;
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setColspan(2);
            my_report_table.addCell(logo);
            logo = new PdfPCell(askridaLogoPath);
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(logo);
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setColspan(2);
            my_report_table.addCell(logo);

            PdfPCell shkTanggal = null;
            shkTanggal = new PdfPCell(new Phrase("No. : " + norutKP, small));
            shkTanggal.setBorder(Rectangle.NO_BORDER);
            shkTanggal.setHorizontalAlignment(Element.ALIGN_LEFT);
            shkTanggal.setColspan(2);
            my_report_table.addCell(shkTanggal);
            shkTanggal = new PdfPCell(new Phrase(Tanggal, small));
            shkTanggal.setBorder(Rectangle.NO_BORDER);
            shkTanggal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            shkTanggal.setColspan(3);
            my_report_table.addCell(shkTanggal);

            //insert heading
            String created1 = "Kepada Yth.\n"
                    + "Kepala Cabang " + pemasaran.getCostCenter(pemasaran.getStCostCenterCode()).getStDescription() + "\n"
                    + pemasaran.getCostCenter(pemasaran.getStCostCenterCode()).getStAddress() + "\n\n\n";
            PdfPCell heading = new PdfPCell(new Phrase(created1, small));
            heading.setBorder(Rectangle.NO_BORDER);
            heading.setColspan(2);
            heading.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(heading);
            heading = new PdfPCell(new Phrase(" ", small));
            heading.setBorder(Rectangle.NO_BORDER);
            heading.setHorizontalAlignment(Element.ALIGN_CENTER);
            heading.setColspan(3);
            my_report_table.addCell(heading);

            //insert judul
            PdfPCell Title = new PdfPCell(new Phrase("Perihal : Persetujuan Pengeluaran Dana",
                    smallb));
            Title.setBorder(Rectangle.NO_BORDER);
            Title.setColspan(5);
            Title.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(Title);

            //insert an empty row
            PdfPCell emptyRow = new PdfPCell(new Phrase(" ", small));
            emptyRow.setBorder(Rectangle.NO_BORDER);
            emptyRow.setColspan(5);
            emptyRow.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(emptyRow);

            //insert isi
//            String created2 = "Menindaklanjuti surat dari Kantor Cabang " + pemasaran.getCostCenter(pemasaran.getStCostCenterCode()).getStDescription() + " No. " + norutCab + " perihal izin pengeluaran dana sebesar Rp. " + JSPUtil.printX(pemasaran.getDbTotalBiaya(), 2) + ",- "
//                    + "dengan ini Direksi menyetujui pengeluaran dana tersebut dan bukti pendukung transaksi agar diadministrasikan dengan tertib. "
//                    + "Untuk selanjutnya setiap penarikan dana dari bank agar sesuai dengan surat Direksi No. 444/DIR/VIII/2014.\n\n"
//                    + "Demikian kami sampaikan, atas perhatiannya kami ucapkan terima kasih.\n\n"
//                    + "Hormat kami,";
//            PdfPCell bySistem = new PdfPCell(new Phrase(created2, small));
//            bySistem.setBorder(Rectangle.NO_BORDER);
//            bySistem.setColspan(5);
//            bySistem.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
//            my_report_table.addCell(bySistem);

            String approvedKabag = null;
            String keterangan = null;
            String taxType = null;
            String taxTypeDesc = null;
            BigDecimal dbPajak = new BigDecimal(0);
            BigDecimal dbPajakdet = new BigDecimal(0);
            while (query_set.next()) {
                approvedKabag = query_set.getString("kabag_approved");
                dbPajak = query_set.getBigDecimal("pajak");
                keterangan = query_set.getString("ket");
            }

            String created2 = "Menindaklanjuti surat dari Kantor Cabang " + pemasaran.getCostCenter(pemasaran.getStCostCenterCode()).getStDescription() + " No. " + norutCab + " perihal izin pengeluaran dana, "
                    + "dengan ini Direksi menyetujui pengeluaran dana sebagai berikut: ";
            PdfPCell bySistem = new PdfPCell(new Phrase(created2, small));
            bySistem.setBorder(Rectangle.NO_BORDER);
            bySistem.setColspan(4);
            bySistem.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            my_report_table2.addCell(bySistem);

            PdfPCell kolomJudul = null;
            kolomJudul = new PdfPCell(new Phrase("No", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table2.addCell(kolomJudul);
            kolomJudul = new PdfPCell(new Phrase("Uraian Pengeluaran", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table2.addCell(kolomJudul);
            kolomJudul = new PdfPCell(new Phrase("Nominal", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table2.addCell(kolomJudul);
            kolomJudul = new PdfPCell(new Phrase("Keterangan", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table2.addCell(kolomJudul);

            PdfPCell headerIsi = null;
            headerIsi = new PdfPCell(new Phrase("1", small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table2.addCell(headerIsi);
            headerIsi = new PdfPCell(new Phrase("Biaya Pemasaran bulan " + LanguageManager.getInstance().translate(DateUtil.getDateStr(pemasaran.getDtEntryDate(), "^^ yyyy")) + " (Rincian Terlampir)", small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(headerIsi);
            headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(BDUtil.sub(pemasaran.getDbTotalBiaya(), dbPajak), 2), small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table2.addCell(headerIsi);
            headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(keterangan), small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(headerIsi);

            ResultSet query_tax = S.executeQuery(
                    " select row_number() over(order by a.pms_id,b.tax_type)+1 as no,b.tax_type,round(sum(b.excess_amount),2) as pajak  "
                    + "from biaya_pemasaran a inner join biaya_pemasaran_detail b on b.pms_id = a.pms_id "
                    + "where a.pms_id = " + pemasaran.getStPemasaranID()
                    + " and b.excess_amount <> 0 group by a.pms_id,b.tax_type order by b.tax_type ");

            while (query_tax.next()) {
                taxType = query_tax.getString("tax_type");

                if (taxType.equalsIgnoreCase("1")) {
                    taxTypeDesc = "Pph 21";
                } else {
                    taxTypeDesc = "Pph 23";
                }

                BigDecimal no = query_tax.getBigDecimal("no");
                headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(no, 0), small));
                headerIsi.setHorizontalAlignment(Element.ALIGN_CENTER);
                my_report_table2.addCell(headerIsi);
                headerIsi = new PdfPCell(new Phrase(taxTypeDesc, small));
                headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
                my_report_table2.addCell(headerIsi);
                dbPajakdet = query_tax.getBigDecimal("pajak");
                headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(dbPajakdet, 2), small));
                headerIsi.setHorizontalAlignment(Element.ALIGN_RIGHT);
                my_report_table2.addCell(headerIsi);
                headerIsi = new PdfPCell(new Phrase("Pembayaran dilakukan bulan berikutnya pada kas negara", small));
                headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
                my_report_table2.addCell(headerIsi);
            }

//            if (Tools.compare(dbPajak, BDUtil.zero) > 0) {
//                headerIsi = new PdfPCell(new Phrase("2", small));
//                headerIsi.setHorizontalAlignment(Element.ALIGN_CENTER);
//                my_report_table2.addCell(headerIsi);
//                headerIsi = new PdfPCell(new Phrase(taxTypeDesc, small));
//                headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
//                my_report_table2.addCell(headerIsi);
//                headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(dbPajak, 2), small));
//                headerIsi.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                my_report_table2.addCell(headerIsi);
//                headerIsi = new PdfPCell(new Phrase("Pembayaran dilakukan bulan berikutnya pada kas negara", small));
//                headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
//                my_report_table2.addCell(headerIsi);
//            }

            PdfPCell headerTotal = null;
            headerTotal = new PdfPCell(new Phrase("TOTAL", small));
            headerTotal.setColspan(2);
            headerTotal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(headerTotal);
            headerTotal = new PdfPCell(new Phrase(JSPUtil.printX(pemasaran.getDbTotalBiaya(), 2), small));
            headerTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table2.addCell(headerTotal);
            headerTotal = new PdfPCell(new Phrase(" ", small));
            headerTotal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(headerTotal);

            String created = "Bukti pendukung transaksi agar diadministrasikan dengan tertib. "
                    + "Untuk selanjutnya setiap penarikan dana dari bank agar sesuai dengan surat Direksi No. 444/DIR/VIII/2014.\n\n"
                    + "Demikian kami sampaikan, atas perhatiannya kami ucapkan terima kasih.\n\n"
                    + "Hormat kami,";
            PdfPCell bySistem2 = new PdfPCell(new Phrase(created, small));
            bySistem2.setBorder(Rectangle.NO_BORDER);
            bySistem2.setColspan(4);
            bySistem2.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            my_report_table2.addCell(bySistem2);

            //insert footer
//            String created3 = "PT. ASURANSI BANGUN ASKRIDA\n"
//                    + "DIVISI KEUANGAN";
            String created3 = "PT. ASURANSI BANGUN ASKRIDA";
            PdfPCell footer1 = new PdfPCell(new Phrase(created3, smallb));
            footer1.setBorder(Rectangle.NO_BORDER);
            footer1.setColspan(4);
            footer1.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(footer1);

            Image imgKeuKabag = Image.getInstance(Parameter.readString("PROPOSAL_KEU_" + approvedKabag));
            Image imgKeuKasie = Image.getInstance(Parameter.readString("PROPOSAL_KASIE_KEU"));
            PdfPCell footer4 = new PdfPCell(imgKeuKabag);
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table4.addCell(footer4);
            footer4 = new PdfPCell(new Phrase("DIVISI KEUANGAN", smallb));
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table4.addCell(footer4);
            footer4 = new PdfPCell(imgKeuKasie);
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table4.addCell(footer4);
            footer4 = new PdfPCell(new Phrase(" ", small));
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table4.addCell(footer4);

            Image img = Image.getInstance(pemasaran.getUser(Parameter.readString("FINANCE_DIVISION_HEAD")).getFile().getStFilePath());
            //insert column data
            PdfPCell footer2 = new PdfPCell(img);
            footer2.setBorder(Rectangle.NO_BORDER);
            footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table3.addCell(footer2);
            footer2 = new PdfPCell(new Phrase(" ", small));
            footer2.setBorder(Rectangle.NO_BORDER);
            footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
            footer2.setColspan(4);
            my_report_table3.addCell(footer2);

            String created4 = pemasaran.getUser(Parameter.readString("FINANCE_DIVISION_HEAD")).getStUserName().toUpperCase() + "\n"
                    + "Kepala Divisi";
            PdfPCell footer3 = new PdfPCell(new Phrase(created4, smallb));
            footer3.setBorder(Rectangle.NO_BORDER);
            footer3.setHorizontalAlignment(Element.ALIGN_LEFT);
            footer3.setColspan(5);
            my_report_table3.addCell(footer3);

            //Create QR Code by using BarcodeQRCode Class
            BarcodeQRCode my_code = new BarcodeQRCode(DateUtil.getDateStr(new Date(), "dd-MMM-yyyy HH:mm:ss") + "_" + pemasaran.getStNoSPP() + "_" + JSPUtil.printX(pemasaran.getDbSaldoBiaya(), 0), 3, 3, null);
            //Get Image corresponding to the input string
            Image qr_image = my_code.getImage();

            PdfPCell barcode = new PdfPCell(qr_image);
            barcode.setBorder(Rectangle.NO_BORDER);
            barcode.setColspan(5);
            barcode.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table3.addCell(barcode);

            my_report_table.setWidths(new int[]{20, 20, 20, 20, 20});
            my_report_table2.setWidths(new int[]{5, 30, 20, 45});
            my_report_table3.setWidths(new int[]{20, 20, 20, 20, 20});
            my_report_table4.setWidths(new int[]{3, 23, 3, 71});

            /* Attach report table to PDF */
            my_pdf_report.add(my_report_table);
            my_pdf_report.add(my_report_table2);
            my_pdf_report.add(my_report_table4);
            my_pdf_report.add(my_report_table3);
            my_pdf_report.close();

            /* Close all DB related objects */
            query_set.close();
            S.close();

//            String receiver = "dwi.puspita@askrida.com";
//            String receiver = "prasetyo@askrida.co.id";
            String receiver = pemasaran.getCostCenter(pemasaran.getStCostCenterCode()).getStEmail();
            String subject = "Persetujuan Penggunaan Biaya Pemasaran";
            String text = "Dengan hormat,\n\n"
                    + "Bersama ini kami lampirkan “" + subject + "” yang dikirim otomatis oleh sistem.\n"
                    + "Atas perhatian dan kerjasamanya, kami ucapkan terima kasih.\n\n\n"
                    + "Hormat kami,\n"
                    + "Administrator";

            if (autoEmail) {
                MailUtil2 mail = new MailUtil2();
                mail.sendEmailWithType(pathTemp, fileName, receiver, subject, text, ".pdf");
            }

        } finally {
            conn.close();
        }
    }
    
    public void receiptPemasaran(String ardepoid) throws Exception {
        setPemasaranID("rkappem");

        pemasaran = getRemoteInsurance().loadPemasaran(ardepoid);

        if (pemasaran == null) {
            throw new RuntimeException("Pemasaran not found !");
        }

        if (pemasaran.getStNoBuktiBayar() != null) {
            throw new Exception("Biaya sudah direalisasi");
        }

        if (!pemasaran.isStatus1Flag()) {
            throw new Exception("Data Belum disetujui Cabang");
        }

        if (!pemasaran.isStatus2Flag()) {
            throw new Exception("Data Belum disetujui Kasie");
        }

        if (!pemasaran.isStatus3Flag()) {
            throw new Exception("Data Belum disetujui Kabag");
        }

        if (!pemasaran.isStatus4Flag()) {
            throw new Exception("Data Belum disetujui Kadiv");
        }

        setReadOnly(true);

        receiptMode = true;

        setTitle("VIEW BIAYA PEMASARAN");
    }

    /**
     * @return the receiptMode
     */
    public boolean isReceiptMode() {
        return receiptMode;
    }

    /**
     * @param receiptMode the receiptMode to set
     */
    public void setReceiptMode(boolean receiptMode) {
        this.receiptMode = receiptMode;
    }

    public void doBayarPms() throws Exception {
//        final SQLUtil S = new SQLUtil();
//
//        if (pemasaran.isPosted()) {
//            throw new RuntimeException("Transaksi bulan " + pemasaran.getStMonths() + " dan tahun " + pemasaran.getStYears() + " tsb sudah diposting");
//        }

        pemasaran.setDtTanggalBayar(new Date());

//        final DTOList details = pemasaran.getPmsDetail();
//        getRemoteGeneralLedger().approveBayar(pemasaran, details);

//        final DTOList details = pemasaran.getPmsDetail();
//        getRemoteGeneralLedger().approvePaidPms(pemasaran, details);

        close();
    }

    /**
     * @param tabs the tabs to set
     */
    public void setTabs(FormTab tabs) {
        this.tabs = tabs;
    }

    public FormTab getTabs() {
        if (tabs == null) {
            tabs = new FormTab();

            tabs.add(new FormTab.TabBean("TAB1", "BIAYA", true));
            tabs.add(new FormTab.TabBean("TAB2", "LAMPIRAN", true));

            tabs.setActiveTab("TAB1");

        }
        return tabs;
    }

    /**
     * @return the document
     */
    public DTOList getDocuments() throws Exception {
        return pemasaran.getDocuments();
    }

    public void doNewDocument() {
        final BiayaPemasaranDocumentsView adr = new BiayaPemasaranDocumentsView();

        adr.markNew();

        pemasaran.getDocuments().add(adr);

        //stSelectedDocument = String.valueOf(entity.getDocuments().size()-1);

    }

    public void doDeleteDocument() {

        //int n = Integer.parseInt(stSelectedDocument);

        pemasaran.getDocuments().delete(Integer.parseInt(instIndex));

        //stSelectedDocument=null;

    }
    private String instIndex;

    /**
     * @return the instIndex
     */
    public String getInstIndex() {
        return instIndex;
    }

    /**
     * @param instIndex the instIndex to set
     */
    public void setInstIndex(String instIndex) {
        this.instIndex = instIndex;
    }

    public String getStNoLampiranDesc() throws Exception {
        final StringBuffer sz = new StringBuffer();

        final DTOList document = pemasaran.getDocuments();
        for (int i = 0; i < document.size(); i++) {
            BiayaPemasaranDocumentsView doc = (BiayaPemasaranDocumentsView) document.get(i);

            if (i > 0) {
                sz.append(", ");
            }

            sz.append(doc.getStKeterangan());
        }

        return sz.toString();
    }

    public BiayaPemasaranView validateNoSuratPemasaran(String keterangan, String pmsid) throws Exception {

        String sql = "select * from biaya_pemasaran where ket = ? ";
        if (pmsid != null) {
            sql = sql + " and pms_id <> " + pmsid;
        }

        BiayaPemasaranView ac = (BiayaPemasaranView) ListUtil.getDTOListFromQuery(
                sql,
                new Object[]{keterangan},
                BiayaPemasaranView.class).getDTO();

        return ac;
    }

    public void printReload() throws Exception {
        sendEmailProposalNoEmail();
        sendEmailApprovalNoEmail();
        super.close();
    }

    public void sendEmailProposalNoEmail() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {

            final Statement S = conn.createStatement();

            /* Define the SQL query */
            ResultSet query_set = S.executeQuery(
                    " select ins_upload_id,status1,status2,status3,status4,reverse_flag,cc_code,"
                    + " periode_awal,periode_akhir,no_surat_hutang,ket,"
                    + " count(ins_upload_dtl_id) as data_amount,sum(amount) as amount_total "
                    + " from ins_proposal_komisi where ins_upload_id = " + headerProposal.getStInsuranceUploadID()
                    + " group by ins_upload_id,status1,status2,status3,status4,reverse_flag,cc_code,periode_awal,periode_akhir,no_surat_hutang,ket ");

            String fileName = "proposal_komisi" + headerProposal.getStInsuranceUploadID();
            File fo = new File("C:/");

            String fileFOlder = Parameter.readString("SYS_FILES_FOLDER");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

            Date period_akhir = null;
            while (query_set.next()) {
                period_akhir = query_set.getDate("periode_akhir");
            }

            String sf = sdf.format(period_akhir);
            String tempPath = fileFOlder + File.separator + "report_temp" + File.separator + sf;
            String path1 = fileFOlder + File.separator + "report_temp" + File.separator;
            String pathTemp = tempPath + File.separator + fileName + ".pdf";

            try {
                new File(path1).mkdir();
                new File(tempPath).mkdir();
            } catch (Exception e) {
            }

            fo = new File(pathTemp);

            FileOutputStream fop = new FileOutputStream(fo);

            /* Step-2: Initialize PDF documents - logical objects */
            Document my_pdf_report = new Document();
            PdfWriter writer = PdfWriter.getInstance(my_pdf_report, fop);
            //PdfWriter.getInstance(my_pdf_report, fop);
            my_pdf_report.open();

            //we have five columns in our table
            PdfPTable my_report_logo = new PdfPTable(5);
            my_report_logo.setWidthPercentage(100);

            PdfPTable my_report_header = new PdfPTable(2);
            my_report_header.setWidthPercentage(100);

            PdfPTable my_report_table = new PdfPTable(4);
            my_report_table.setWidthPercentage(100);

            PdfPTable my_report_footer = new PdfPTable(5);
            my_report_footer.setWidthPercentage(100);
            //create a cell object

            Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12),
                    smallbold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD),
                    smallc = new Font(Font.FontFamily.TIMES_ROMAN, 11),
                    small10 = new Font(Font.FontFamily.TIMES_ROMAN, 10),
                    small10bold = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD),
                    small6 = new Font(Font.FontFamily.TIMES_ROMAN, 6);

            //insert heading
            final String no_surat[] = headerProposal.getStNoSuratHutang().split("[\\/]");
            final String norut = no_surat[0] + "/" + Parameter.readString("BRANCH_SHORT_" + headerProposal.getStCostCenterCode()).toUpperCase() + "-COMM/" + DateUtil.getMonth2Digit(period_akhir) + "/" + DateUtil.getYear(period_akhir);

            Image askridaLogoPath = Image.getInstance(Parameter.readString("DIGITAL_POLIS_LOGO_PIC"));

            PdfPCell logo = null;
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            logo.setColspan(2);
            my_report_logo.addCell(logo);
            logo = new PdfPCell(askridaLogoPath);
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_logo.addCell(logo);
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            logo.setColspan(2);
            my_report_logo.addCell(logo);
            my_report_logo.completeRow();

            PdfPCell headerJudul = null;
            headerJudul = new PdfPCell(new Phrase("SURAT IZIN PENGELUARAN DANA", smallbold));
            headerJudul.setBorder(Rectangle.NO_BORDER);
            headerJudul.setColspan(2);
            headerJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_header.addCell(headerJudul);
            my_report_header.completeRow();

            PdfPCell headerNo = null;
            headerNo = new PdfPCell(new Phrase("No.", small));
            headerNo.setBorder(Rectangle.NO_BORDER);
            headerNo.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerNo);
            headerNo = new PdfPCell(new Phrase(": " + norut, small));
            headerNo.setBorder(Rectangle.NO_BORDER);
            headerNo.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerNo);

            PdfPCell headerName = null;
            headerName = new PdfPCell(new Phrase("Kepada Yth.", small));
            headerName.setBorder(Rectangle.NO_BORDER);
            headerName.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerName);
            headerName = new PdfPCell(new Phrase(": Kadiv. Keuangan", small));
            headerName.setBorder(Rectangle.NO_BORDER);
            headerName.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerName);

            PdfPCell headerTanggal = null;
            headerTanggal = new PdfPCell(new Phrase("Tanggal", small));
            headerTanggal.setBorder(Rectangle.NO_BORDER);
            headerTanggal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerTanggal);
            headerTanggal = new PdfPCell(new Phrase(": " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "dd ^^ yyyy")), small));
            headerTanggal.setBorder(Rectangle.NO_BORDER);
            headerTanggal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerTanggal);

            PdfPCell headerDaerah = null;
            headerDaerah = new PdfPCell(new Phrase("Daerah", small));
            headerDaerah.setBorder(Rectangle.NO_BORDER);
            headerDaerah.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerDaerah);
            headerDaerah = new PdfPCell(new Phrase(": Cabang " + headerProposal.getCostCenter(headerProposal.getStCostCenterCode()).getStDescription(), small));
            headerDaerah.setBorder(Rectangle.NO_BORDER);
            headerDaerah.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerDaerah);

            String perihal = null;
            boolean isrevFlag = false;
            while (query_set.next()) {
                isrevFlag = Tools.isYes(query_set.getString("reverse_flag"));
            }

            if (isrevFlag) {
                perihal = ": Pembayaran Komisi No. SHK " + headerProposal.getStNoSuratHutang() + " (Revisi)";
            } else {
                perihal = ": Pembayaran Komisi No. SHK " + headerProposal.getStNoSuratHutang();
            }

            PdfPCell headerHal = null;
            headerHal = new PdfPCell(new Phrase("Perihal", small));
            headerHal.setBorder(Rectangle.NO_BORDER);
            headerHal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerHal);
            headerHal = new PdfPCell(new Phrase(perihal, small));
            headerHal.setBorder(Rectangle.NO_BORDER);
            headerHal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerHal);
            my_report_header.completeRow();

            //insert an empty row
            PdfPCell emptyRow = new PdfPCell(new Phrase(" ", small));
            emptyRow.setBorder(Rectangle.NO_BORDER);
            emptyRow.setColspan(2);
            emptyRow.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_header.addCell(emptyRow);
            my_report_header.completeRow();

            PdfPCell kolomJudul = null;
            kolomJudul = new PdfPCell(new Phrase("No", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(kolomJudul);
            kolomJudul = new PdfPCell(new Phrase("Uraian Pengeluaran", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(kolomJudul);
            kolomJudul = new PdfPCell(new Phrase("Nominal", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(kolomJudul);
            kolomJudul = new PdfPCell(new Phrase("Keterangan", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(kolomJudul);

            PdfPCell headerIsi = null;
            headerIsi = new PdfPCell(new Phrase("1", small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(headerIsi);
            headerIsi = new PdfPCell(new Phrase("Pembayaran Komisi bulan " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "^^ yyyy")) + " (Polis Terlampir)", small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(headerIsi);
            headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(headerProposal.getDbAmountTotal(), 2), small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table.addCell(headerIsi);
            headerIsi = new PdfPCell(new Phrase(headerProposal.getStKeterangan(), small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(headerIsi);

            //insert footer
            String created3 = "Hormat kami,\n"
                    + "PT. ASURANSI BANGUN ASKRIDA \n"
                    + "Cabang " + headerProposal.getCostCenter(headerProposal.getStCostCenterCode()).getStDescription();
            PdfPCell footer1 = new PdfPCell(new Phrase(created3, small));
            footer1.setBorder(Rectangle.NO_BORDER);
            footer1.setColspan(5);
            footer1.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_footer.addCell(footer1);
            my_report_footer.completeRow();

            Image img = Image.getInstance(headerProposal.getUser(Parameter.readString("BRANCH_" + headerProposal.getStCostCenterCode())).getFile().getStFilePath());
            img.scaleToFit(100f, 100f);
            //insert column data
            PdfPCell footer2 = null;
            footer2 = new PdfPCell(new Phrase(" ", small));
            footer2.setColspan(2);
            footer2.setHorizontalAlignment(Element.ALIGN_LEFT);
            footer2.setBorder(Rectangle.NO_BORDER);
            my_report_footer.addCell(footer2);
            footer2 = new PdfPCell(img);
            footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
            footer2.setBorder(Rectangle.NO_BORDER);
            my_report_footer.addCell(footer2);
            footer2 = new PdfPCell(new Phrase(" ", small));
            footer2.setColspan(2);
            footer2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            footer2.setBorder(Rectangle.NO_BORDER);
            my_report_footer.addCell(footer2);

            String created4 = Parameter.readString("BRANCH_SIGN_" + headerProposal.getStCostCenterCode()) + "\n"
                    + headerProposal.getUser(Parameter.readString("BRANCH_" + headerProposal.getStCostCenterCode())).getStJobPosition().toUpperCase();
            PdfPCell footer3 = new PdfPCell(new Phrase(created4, small));
            footer3.setBorder(Rectangle.NO_BORDER);
            footer3.setColspan(5);
            footer3.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_footer.addCell(footer3);
            my_report_footer.completeRow();

            //Create QR Code by using BarcodeQRCode Class
            BarcodeQRCode my_code = new BarcodeQRCode(DateUtil.getDateStr(new Date(), "dd-MMM-yyyy HH:mm:ss") + "_" + headerProposal.getStNoSuratHutang() + "_" + JSPUtil.printX(headerProposal.getDbAmountTotal(), 0), 3, 3, null);
            //Get Image corresponding to the input string
            Image qr_image = my_code.getImage();

            PdfPCell barcode = new PdfPCell(qr_image);
            barcode.setBorder(Rectangle.NO_BORDER);
            barcode.setColspan(5);
            barcode.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_footer.addCell(barcode);
            my_report_footer.completeRow();

            my_report_logo.setWidths(new int[]{20, 20, 20, 20, 20});
            my_report_header.setWidths(new int[]{15, 85});
            my_report_table.setWidths(new int[]{5, 30, 20, 45});
            my_report_footer.setWidths(new int[]{20, 20, 20, 20, 20});

            /* Attach report table to PDF */
            my_pdf_report.add(my_report_logo);
            my_pdf_report.add(my_report_header);
            my_pdf_report.add(my_report_table);
            my_pdf_report.add(my_report_footer);
            my_pdf_report.close();

            /* Define the SQL query */
            ResultSet query_set2 = S.executeQuery(
                    " select row_number() over(order by a.ar_invoice_id) as no,"
                    + " a.attr_pol_no,a.attr_pol_name,a.amount,"
                    + " (select x.receipt_no from ins_policy x where x.pol_id = a.attr_pol_id) as receipt_no "
                    + " from ar_invoice a "
                    + " where a.no_surat_hutang = '" + headerProposal.getStNoSuratHutang() + "' "
                    + " order by 1 ");

            String fileName2 = "lampiran_polis_komisi" + headerProposal.getStInsuranceUploadID();

            File fo2 = new File("C:/");

            String pathTemp2 = tempPath + File.separator + fileName2 + ".pdf";

            try {
                new File(path1).mkdir();
                new File(tempPath).mkdir();
            } catch (Exception e) {
            }

            fo2 = new File(pathTemp2);

            FileOutputStream fop2 = new FileOutputStream(fo2);

            /* Step-2: Initialize PDF documents - logical objects */
            Document my_pdf_report2 = new Document();
            PdfWriter writer2 = PdfWriter.getInstance(my_pdf_report2, fop2);
            //PdfWriter.getInstance(my_pdf_report, fop);
            my_pdf_report2.open();

            //we have four columns in our table
            PdfPTable my_report_table2 = new PdfPTable(5);
            my_report_table2.setWidthPercentage(100);

            //insert headings
            PdfPCell Title2 = new PdfPCell(new Phrase("Lampiran Surat Izin Pengeluaran Dana",
                    smallbold));
            Title2.setBorder(Rectangle.NO_BORDER);
            Title2.setColspan(5);
            Title2.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(Title2);
            my_report_table2.completeRow();

            //insert headings
            PdfPCell Title3 = new PdfPCell(new Phrase("No. : " + norut,
                    smallbold));
            Title3.setBorder(Rectangle.NO_BORDER);
            Title3.setColspan(5);
            Title3.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(Title3);
            my_report_table2.completeRow();

            //insert headings
            PdfPCell Title4 = new PdfPCell(new Phrase("No. SHK : " + headerProposal.getStNoSuratHutang(),
                    smallbold));
            Title4.setBorder(Rectangle.NO_BORDER);
            Title4.setColspan(5);
            Title4.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(Title4);
            my_report_table2.completeRow();

            PdfPCell pertanggal2 = new PdfPCell(new Phrase("Tanggal : "
                    + LanguageManager.getInstance().translate(DateUtil.getDateStr(headerProposal.getDtPeriodeAwal(), "dd ^^ yyyy"))
                    + " s/d "
                    + LanguageManager.getInstance().translate(DateUtil.getDateStr(headerProposal.getDtPeriodeAkhir(), "dd ^^ yyyy")),
                    small));
            pertanggal2.setBorder(Rectangle.NO_BORDER);
            pertanggal2.setColspan(5);
            pertanggal2.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(pertanggal2);
            my_report_table2.completeRow();

            //insert an empty row
            my_report_table2.completeRow();

            //insert column headings
            Phrase p[] = {new Phrase("No", smallbold),
                new Phrase("No. Polis", smallbold),
                new Phrase("Tertanggung", smallbold),
                new Phrase("Komisi", smallbold),
                new Phrase("Pelunasan", smallbold)};

            PdfPCell judno = new PdfPCell(p[0]),
                    judpol = new PdfPCell(p[1]),
                    judname = new PdfPCell(p[2]),
                    judkomisi = new PdfPCell(p[3]),
                    judnobuk = new PdfPCell(p[4]);

            //insert column headings
            judno.setHorizontalAlignment(judno.ALIGN_CENTER);
            judno.setVerticalAlignment(judno.ALIGN_MIDDLE);
            judno.setGrayFill(0.7f);
            judpol.setHorizontalAlignment(judpol.ALIGN_CENTER);
            judpol.setVerticalAlignment(judpol.ALIGN_MIDDLE);
            judpol.setGrayFill(0.7f);
            judname.setHorizontalAlignment(judname.ALIGN_CENTER);
            judname.setVerticalAlignment(judname.ALIGN_MIDDLE);
            judname.setGrayFill(0.7f);
            judkomisi.setHorizontalAlignment(judkomisi.ALIGN_CENTER);
            judkomisi.setVerticalAlignment(judkomisi.ALIGN_MIDDLE);
            judkomisi.setGrayFill(0.7f);
            judnobuk.setHorizontalAlignment(judnobuk.ALIGN_CENTER);
            judnobuk.setVerticalAlignment(judnobuk.ALIGN_MIDDLE);
            judnobuk.setGrayFill(0.7f);

            my_report_table2.addCell(judno);
            my_report_table2.addCell(judpol);
            my_report_table2.addCell(judname);
            my_report_table2.addCell(judkomisi);
            my_report_table2.addCell(judnobuk);

            //insert column data
            PdfPCell table_cell2 = null;
            BigDecimal komisiTotal = new BigDecimal(0);

            while (query_set2.next()) {
                BigDecimal no = query_set2.getBigDecimal("no");
                table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(no, 0), small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                my_report_table2.addCell(table_cell2);

                String pol_no = query_set2.getString("attr_pol_no");
                table_cell2 = new PdfPCell(new Phrase(pol_no, small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                my_report_table2.addCell(table_cell2);

                String custname = query_set2.getString("attr_pol_name");
                if (custname.length() > 32) {
                    custname = custname.substring(0, 32);
                } else {
                    custname = custname.substring(0, custname.length());
                }
                table_cell2 = new PdfPCell(new Phrase(custname, small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                my_report_table2.addCell(table_cell2);

                BigDecimal komisi = query_set2.getBigDecimal("amount");
                komisiTotal = BDUtil.add(komisiTotal, komisi);
                table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(komisi, 2), small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                my_report_table2.addCell(table_cell2);

                String receipt_no = query_set2.getString("receipt_no");
                table_cell2 = new PdfPCell(new Phrase(receipt_no, small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                my_report_table2.addCell(table_cell2);
            }

            table_cell2 = new PdfPCell(new Phrase("SUBTOTAL", smallbold));
            table_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell2.setColspan(3);
            my_report_table2.addCell(table_cell2);
            table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(komisiTotal, 2), small10bold));
            table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table2.addCell(table_cell2);
            table_cell2 = new PdfPCell(new Phrase(" ", small10bold));
            my_report_table2.addCell(table_cell2);

            String created = "Created by System " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "dd ^^ yyyy HH:mm:ss"));
            PdfPCell bySistem2 = new PdfPCell(new Phrase(created, small6));
            bySistem2.setBorder(Rectangle.NO_BORDER);
            bySistem2.setColspan(5);
            bySistem2.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(bySistem2);
            my_report_table2.completeRow();

            //Create QR Code by using BarcodeQRCode Class
            BarcodeQRCode my_code2 = new BarcodeQRCode(DateUtil.getDateStr(new Date(), "dd-MMM-yyyy HH:mm:ss") + "_" + JSPUtil.printX(komisiTotal, 2), 3, 3, null);
            //Get Image corresponding to the input string
            Image qr_image2 = my_code2.getImage();

            PdfPCell barcode2 = new PdfPCell(qr_image2);
            barcode2.setBorder(Rectangle.NO_BORDER);
            barcode2.setColspan(5);
            barcode2.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(barcode2);
            my_report_table2.completeRow();

            my_report_table2.setWidths(new int[]{5, 20, 40, 15, 20});

            /* Attach report table to PDF */
            my_pdf_report2.add(my_report_table2);
            my_pdf_report2.close();

            /* Close all DB related objects */
            query_set.close();
            query_set2.close();
            S.close();

//            String receiver = Parameter.readString("FINANCE_EMAIL");
////            String receiver = "prasetyo@askrida.co.id";
//            String subject = "Permohonan Penarikan Dana";
//            String text = "Dengan hormat,\n\n"
//                    + "Bersama ini kami lampirkan “" + subject + "” yang dikirim otomatis oleh sistem.\n"
//                    + "Atas perhatian dan kerjasamanya, kami ucapkan terima kasih.\n\n\n"
//                    + "Hormat kami,\n"
//                    + "Administrator";
//
//            MailUtil2 mail = new MailUtil2();
//            mail.sendEmailMultiFile(pathTemp, fileName, pathTemp2, fileName2, null, null, null, null, null, null, receiver, null, subject, text);

        } finally {
            conn.close();
        }
    }

    public void sendEmailApprovalNoEmail() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {

            final Statement S = conn.createStatement();

            /* Define the SQL query */
            ResultSet query_set = S.executeQuery(
                    " select ins_upload_id,status1,status2,status3,status4,cc_code,"
                    + " periode_awal,periode_akhir,no_surat_hutang,kabag_approved,no_surat,"
                    + " count(ins_upload_dtl_id) as data_amount,sum(amount) as amount_total "
                    + " from ins_proposal_komisi where ins_upload_id = " + headerProposal.getStInsuranceUploadID()
                    + " group by ins_upload_id,status1,status2,status3,status4,cc_code,periode_awal,periode_akhir,no_surat_hutang,kabag_approved,no_surat ");

            String fileName = "approval_komisi" + headerProposal.getStInsuranceUploadID();
            File fo = new File("C:/");

            String fileFOlder = Parameter.readString("SYS_FILES_FOLDER");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

            Date period_akhir = null;
            String approvedKabag = null;
            String noSurat = null;
            while (query_set.next()) {
                period_akhir = query_set.getDate("periode_akhir");
                approvedKabag = query_set.getString("kabag_approved");
                noSurat = query_set.getString("no_surat");
            }

            String sf = sdf.format(period_akhir);
            String tempPath = fileFOlder + File.separator + "report_temp" + File.separator + sf;
            String path1 = fileFOlder + File.separator + "report_temp" + File.separator;
            String pathTemp = tempPath + File.separator + fileName + ".pdf";

            try {
                new File(path1).mkdir();
                new File(tempPath).mkdir();
            } catch (Exception e) {
            }

            fo = new File(pathTemp);

            FileOutputStream fop = new FileOutputStream(fo);

            /* Step-2: Initialize PDF documents - logical objects */
            Document my_pdf_report = new Document();
            PdfWriter writer = PdfWriter.getInstance(my_pdf_report, fop);
            //PdfWriter.getInstance(my_pdf_report, fop);
            my_pdf_report.open();

            //we have four columns in our table
            PdfPTable my_report_table = new PdfPTable(5);
            my_report_table.setWidthPercentage(100);
            PdfPTable my_report_table2 = new PdfPTable(4);
            my_report_table2.setWidthPercentage(100);
            PdfPTable my_report_table3 = new PdfPTable(5);
            my_report_table3.setWidthPercentage(100);
            //create a cell object

            Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12),
                    smallb = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD),
                    smallc = new Font(Font.FontFamily.TIMES_ROMAN, 11);

            String requestNo = null;

            if (noSurat != null) {
                requestNo = noSurat;
            } else {
                String counterKey = DateUtil.getYear(new Date());
                String rn = String.valueOf(IDFactory.createNumericID("REQPROPNO" + counterKey, 1));
                rn = StringTools.leftPad(rn, '0', 3);

                requestNo = rn + "/KEU-COMM/" + DateUtil.getMonth2Digit(period_akhir) + "/" + DateUtil.getYear(period_akhir);

                final SQLUtil SQ = new SQLUtil();
                PreparedStatement PS = SQ.setQuery("update ins_proposal_komisi set no_surat = ? where ins_upload_id = ?");

                PS.setObject(1, requestNo);
                PS.setObject(2, headerProposal.getStInsuranceUploadID());

                int p = PS.executeUpdate();
            }

            //insert heading
            String Tanggal = "Jakarta, " + LanguageManager.getInstance().translate(DateUtil.getDateStr(period_akhir, "dd ^^ yyyy"));
            Image askridaLogoPath = Image.getInstance(Parameter.readString("DIGITAL_POLIS_LOGO_PIC"));
            final String no_surat[] = headerProposal.getStNoSuratHutang().split("[\\/]");
            final String norutCab = no_surat[0] + "/" + Parameter.readString("BRANCH_SHORT_" + headerProposal.getStCostCenterCode()).toUpperCase() + "-COMM/" + DateUtil.getMonth2Digit(period_akhir) + "/" + DateUtil.getYear(period_akhir);
            final String norutKP = requestNo;

            PdfPCell logo = null;
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setColspan(2);
            my_report_table.addCell(logo);
            logo = new PdfPCell(askridaLogoPath);
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(logo);
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setColspan(2);
            my_report_table.addCell(logo);

            PdfPCell shkTanggal = null;
            shkTanggal = new PdfPCell(new Phrase("No. : " + norutKP, small));
            shkTanggal.setBorder(Rectangle.NO_BORDER);
            shkTanggal.setHorizontalAlignment(Element.ALIGN_LEFT);
            shkTanggal.setColspan(2);
            my_report_table.addCell(shkTanggal);
            shkTanggal = new PdfPCell(new Phrase(Tanggal, small));
            shkTanggal.setBorder(Rectangle.NO_BORDER);
            shkTanggal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            shkTanggal.setColspan(3);
            my_report_table.addCell(shkTanggal);

            //insert heading
            String created1 = "Kepada Yth.\n"
                    + "Kepala Cabang " + headerProposal.getCostCenter(headerProposal.getStCostCenterCode()).getStDescription() + "\n"
                    + headerProposal.getCostCenter(headerProposal.getStCostCenterCode()).getStAddress() + "\n\n\n";
            PdfPCell heading = new PdfPCell(new Phrase(created1, small));
            heading.setBorder(Rectangle.NO_BORDER);
            heading.setColspan(2);
            heading.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(heading);
            heading = new PdfPCell(new Phrase(" ", small));
            heading.setBorder(Rectangle.NO_BORDER);
            heading.setHorizontalAlignment(Element.ALIGN_CENTER);
            heading.setColspan(3);
            my_report_table.addCell(heading);

            //insert judul
            PdfPCell Title = new PdfPCell(new Phrase("Perihal : Persetujuan Penarikan Dana",
                    smallb));
            Title.setBorder(Rectangle.NO_BORDER);
            Title.setColspan(5);
            Title.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(Title);

            //insert an empty row
            PdfPCell emptyRow = new PdfPCell(new Phrase(" ", small));
            emptyRow.setBorder(Rectangle.NO_BORDER);
            emptyRow.setColspan(5);
            emptyRow.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(emptyRow);

            //insert isi
            String created2 = "Menindaklanjuti surat dari Kantor Cabang " + headerProposal.getCostCenter(headerProposal.getStCostCenterCode()).getStDescription() + " No. " + norutCab + " perihal permohonan penarikan dana sebesar Rp. " + JSPUtil.printX(headerProposal.getDbAmountTotal(), 2) + ",- "
                    + "setelah dilakukan verifikasi atas data Kantor Cabang " + headerProposal.getCostCenter(headerProposal.getStCostCenterCode()).getStDescription() + " dengan data di Kantor Pusat, "
                    + "dengan ini Direksi menyetujui penarikan dana tersebut, dengan catatan premi atas polis-polis tersebut sudah masuk ke rekening perusahaan. "
                    + "Untuk selanjutnya setiap penarikan dana dari bank agar sesuai dengan surat Direksi No. 444/DIR/VIII/2014.\n\n"
                    + "Demikian kami sampaikan, atas perhatiannya kami ucapkan terima kasih.\n\n"
                    + "Hormat kami,";
            PdfPCell bySistem = new PdfPCell(new Phrase(created2, small));
            bySistem.setBorder(Rectangle.NO_BORDER);
            bySistem.setColspan(5);
            bySistem.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            my_report_table.addCell(bySistem);

            //insert footer
//            String created3 = "PT. ASURANSI BANGUN ASKRIDA\n"
//                    + "DIVISI KEUANGAN";
            String created3 = "PT. ASURANSI BANGUN ASKRIDA";
            PdfPCell footer1 = new PdfPCell(new Phrase(created3, smallb));
            footer1.setBorder(Rectangle.NO_BORDER);
            footer1.setColspan(5);
            footer1.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(footer1);

//            String approvedKabag = null;
//            while (query_set.next()) {
//                approvedKabag = query_set.getString("kabag_approved");
//            }

            Image imgKeuKabag = Image.getInstance(Parameter.readString("PROPOSAL_KEU_" + approvedKabag));
            Image imgKeuKasie = Image.getInstance(Parameter.readString("PROPOSAL_KEU_05920834"));
            PdfPCell footer4 = new PdfPCell(imgKeuKabag);
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table2.addCell(footer4);
            footer4 = new PdfPCell(new Phrase("DIVISI KEUANGAN", smallb));
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table2.addCell(footer4);
            footer4 = new PdfPCell(imgKeuKasie);
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(footer4);
            footer4 = new PdfPCell(new Phrase(" ", small));
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table2.addCell(footer4);

            Image img = Image.getInstance(headerProposal.getUser(Parameter.readString("FINANCE_DIVISION_HEAD")).getFile().getStFilePath());
            //insert column data
            PdfPCell footer2 = new PdfPCell(img);
            footer2.setBorder(Rectangle.NO_BORDER);
            footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table3.addCell(footer2);
            footer2 = new PdfPCell(new Phrase(" ", small));
            footer2.setBorder(Rectangle.NO_BORDER);
            footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
            footer2.setColspan(4);
            my_report_table3.addCell(footer2);

            String created4 = headerProposal.getUser(Parameter.readString("FINANCE_DIVISION_HEAD")).getStUserName().toUpperCase() + "\n"
                    + "Kepala Divisi";
            PdfPCell footer3 = new PdfPCell(new Phrase(created4, smallb));
            footer3.setBorder(Rectangle.NO_BORDER);
            footer3.setHorizontalAlignment(Element.ALIGN_LEFT);
            footer3.setColspan(5);
            my_report_table3.addCell(footer3);

            //Create QR Code by using BarcodeQRCode Class
            BarcodeQRCode my_code = new BarcodeQRCode(DateUtil.getDateStr(new Date(), "dd-MMM-yyyy HH:mm:ss") + "_" + headerProposal.getStNoSuratHutang() + "_" + JSPUtil.printX(headerProposal.getDbAmountTotal(), 0), 3, 3, null);
            //Get Image corresponding to the input string
            Image qr_image = my_code.getImage();

            PdfPCell barcode = new PdfPCell(qr_image);
            barcode.setBorder(Rectangle.NO_BORDER);
            barcode.setColspan(5);
            barcode.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table3.addCell(barcode);

            my_report_table.setWidths(new int[]{20, 20, 20, 20, 20});
            my_report_table2.setWidths(new int[]{3, 23, 3, 71});
            my_report_table3.setWidths(new int[]{20, 20, 20, 20, 20});

            /* Attach report table to PDF */
            my_pdf_report.add(my_report_table);
            my_pdf_report.add(my_report_table2);
            my_pdf_report.add(my_report_table3);
            my_pdf_report.close();

            /* Close all DB related objects */
            query_set.close();
            S.close();

////            String receiver = "prasetyo@askrida.co.id";
//            String receiver = headerProposal.getCostCenter(headerProposal.getStCostCenterCode()).getStEmail();
//            String subject = "Persetujuan Penarikan Dana";
//            String text = "Dengan hormat,\n\n"
//                    + "Bersama ini kami lampirkan “" + subject + "” yang dikirim otomatis oleh sistem.\n"
//                    + "Atas perhatian dan kerjasamanya, kami ucapkan terima kasih.\n\n\n"
//                    + "Hormat kami,\n"
//                    + "Administrator";
//
//            MailUtil2 mail = new MailUtil2();
//            mail.sendEmailWithType(pathTemp, fileName, receiver, subject, text, ".pdf");

        } finally {
            conn.close();
        }
    }

    private String sendEmailParam = Parameter.readString("SYS_FILES_FOLDER");

    public boolean isSendEmailParam() {
        return sendEmailParam.equalsIgnoreCase("1");
    }

    public void sendEmailApproval(String uploadID) throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {

            final Statement S = conn.createStatement();

            /* Define the SQL query */
            ResultSet query_set = S.executeQuery(
                    " select ins_upload_id,status1,status2,status3,status4,cc_code,"
                    + " periode_awal,periode_akhir,no_surat_hutang,kabag_approved,"
                    + " count(ins_upload_dtl_id) as data_amount,sum(amount) as amount_total "
                    + " from ins_proposal_komisi where ins_upload_id = " + headerProposal.getStInsuranceUploadID()
                    + " group by ins_upload_id,status1,status2,status3,status4,cc_code,periode_awal,periode_akhir,no_surat_hutang,kabag_approved ");

            String fileName = "approval_komisi" + headerProposal.getStInsuranceUploadID();
            File fo = new File("C:/");

            String fileFOlder = Parameter.readString("SYS_FILES_FOLDER");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

            String sf = sdf.format(new Date());
            String tempPath = fileFOlder + File.separator + "report_temp" + File.separator + sf;
            String path1 = fileFOlder + File.separator + "report_temp" + File.separator;
            String pathTemp = tempPath + File.separator + fileName + ".pdf";

            try {
                new File(path1).mkdir();
                new File(tempPath).mkdir();
            } catch (Exception e) {
            }

            fo = new File(pathTemp);

            FileOutputStream fop = new FileOutputStream(fo);

            /* Step-2: Initialize PDF documents - logical objects */
            Document my_pdf_report = new Document();
            PdfWriter writer = PdfWriter.getInstance(my_pdf_report, fop);
            //PdfWriter.getInstance(my_pdf_report, fop);
            my_pdf_report.open();

            //we have four columns in our table
            PdfPTable my_report_table = new PdfPTable(5);
            my_report_table.setWidthPercentage(100);
            PdfPTable my_report_table2 = new PdfPTable(4);
            my_report_table2.setWidthPercentage(100);
            PdfPTable my_report_table3 = new PdfPTable(5);
            my_report_table3.setWidthPercentage(100);
            //create a cell object

            Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12),
                    smallb = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD),
                    smallc = new Font(Font.FontFamily.TIMES_ROMAN, 11);

            String requestNo = null;
//            String counterKey = DateUtil.getYear2Digit(new Date()) + DateUtil.getMonth2Digit(new Date());
            String counterKey = DateUtil.getYear(new Date());
            String rn = String.valueOf(IDFactory.createNumericID("REQPROPNO" + counterKey, 1));
            rn = StringTools.leftPad(rn, '0', 3);

            requestNo = rn + "/KEU-COMM/" + DateUtil.getMonth2Digit(new Date()) + "/" + DateUtil.getYear(new Date());

            //insert heading
            String Tanggal = "Jakarta, " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "dd ^^ yyyy"));
            Image askridaLogoPath = Image.getInstance(Parameter.readString("DIGITAL_POLIS_LOGO_PIC"));
            final String no_surat[] = headerProposal.getStNoSuratHutang().split("[\\/]");
            final String norutCab = no_surat[0] + "/" + Parameter.readString("BRANCH_SHORT_" + headerProposal.getStCostCenterCode()).toUpperCase() + "-COMM/" + DateUtil.getMonth2Digit(new Date()) + "/" + DateUtil.getYear(new Date());
            final String norutKP = requestNo;

            final SQLUtil SQ = new SQLUtil();
            PreparedStatement PS = SQ.setQuery("update ins_proposal_komisi set no_surat = ? where ins_upload_id = ?");

            PS.setObject(1, norutKP);
            PS.setObject(2, uploadID);

            int p = PS.executeUpdate();

            SQ.release();

            PdfPCell logo = null;
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setColspan(2);
            my_report_table.addCell(logo);
            logo = new PdfPCell(askridaLogoPath);
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(logo);
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setColspan(2);
            my_report_table.addCell(logo);

            PdfPCell shkTanggal = null;
            shkTanggal = new PdfPCell(new Phrase("No. : " + norutKP, small));
            shkTanggal.setBorder(Rectangle.NO_BORDER);
            shkTanggal.setHorizontalAlignment(Element.ALIGN_LEFT);
            shkTanggal.setColspan(2);
            my_report_table.addCell(shkTanggal);
            shkTanggal = new PdfPCell(new Phrase(Tanggal, small));
            shkTanggal.setBorder(Rectangle.NO_BORDER);
            shkTanggal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            shkTanggal.setColspan(3);
            my_report_table.addCell(shkTanggal);

            //insert heading
            String created1 = "Kepada Yth.\n"
                    + "Kepala Cabang " + headerProposal.getCostCenter(headerProposal.getStCostCenterCode()).getStDescription() + "\n"
                    + headerProposal.getCostCenter(headerProposal.getStCostCenterCode()).getStAddress() + "\n\n\n";
            PdfPCell heading = new PdfPCell(new Phrase(created1, small));
            heading.setBorder(Rectangle.NO_BORDER);
            heading.setColspan(2);
            heading.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(heading);
            heading = new PdfPCell(new Phrase(" ", small));
            heading.setBorder(Rectangle.NO_BORDER);
            heading.setHorizontalAlignment(Element.ALIGN_CENTER);
            heading.setColspan(3);
            my_report_table.addCell(heading);

            //insert judul
            PdfPCell Title = new PdfPCell(new Phrase("Perihal : Persetujuan Penarikan Dana",
                    smallb));
            Title.setBorder(Rectangle.NO_BORDER);
            Title.setColspan(5);
            Title.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(Title);

            //insert an empty row
            PdfPCell emptyRow = new PdfPCell(new Phrase(" ", small));
            emptyRow.setBorder(Rectangle.NO_BORDER);
            emptyRow.setColspan(5);
            emptyRow.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(emptyRow);

            //insert isi
            String created2 = "Menindaklanjuti surat dari Kantor Cabang " + headerProposal.getCostCenter(headerProposal.getStCostCenterCode()).getStDescription() + " No. " + norutCab + " perihal permohonan penarikan dana sebesar Rp. " + JSPUtil.printX(headerProposal.getDbAmountTotal(), 2) + ",- "
                    + "setelah dilakukan verifikasi atas data Kantor Cabang " + headerProposal.getCostCenter(headerProposal.getStCostCenterCode()).getStDescription() + " dengan data di Kantor Pusat, "
                    + "dengan ini Direksi menyetujui penarikan dana tersebut, dengan catatan premi atas polis-polis tersebut sudah masuk ke rekening perusahaan. "
                    + "Untuk selanjutnya setiap penarikan dana dari bank agar sesuai dengan surat Direksi No. 444/DIR/VIII/2014.\n\n"
                    + "Demikian kami sampaikan, atas perhatiannya kami ucapkan terima kasih.\n\n"
                    + "Hormat kami,";
            PdfPCell bySistem = new PdfPCell(new Phrase(created2, small));
            bySistem.setBorder(Rectangle.NO_BORDER);
            bySistem.setColspan(5);
            bySistem.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            my_report_table.addCell(bySistem);

            //insert footer
//            String created3 = "PT. ASURANSI BANGUN ASKRIDA\n"
//                    + "DIVISI KEUANGAN";
            String created3 = "PT. ASURANSI BANGUN ASKRIDA";
            PdfPCell footer1 = new PdfPCell(new Phrase(created3, smallb));
            footer1.setBorder(Rectangle.NO_BORDER);
            footer1.setColspan(5);
            footer1.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(footer1);

            String approvedKabag = null;
            while (query_set.next()) {
                approvedKabag = query_set.getString("kabag_approved");
            }

            Image imgKeuKabag = Image.getInstance(Parameter.readString("PROPOSAL_KEU_" + approvedKabag));
            Image imgKeuKasie = Image.getInstance(Parameter.readString("PROPOSAL_KEU_05690254"));
            PdfPCell footer4 = new PdfPCell(imgKeuKabag);
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table2.addCell(footer4);
            footer4 = new PdfPCell(new Phrase("DIVISI KEUANGAN", smallb));
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table2.addCell(footer4);
            footer4 = new PdfPCell(imgKeuKasie);
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(footer4);
            footer4 = new PdfPCell(new Phrase(" ", small));
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table2.addCell(footer4);

            Image img = Image.getInstance(headerProposal.getUser(Parameter.readString("FINANCE_DIVISION_HEAD")).getFile().getStFilePath());
            //insert column data
            PdfPCell footer2 = new PdfPCell(img);
            footer2.setBorder(Rectangle.NO_BORDER);
            footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table3.addCell(footer2);
            footer2 = new PdfPCell(new Phrase(" ", small));
            footer2.setBorder(Rectangle.NO_BORDER);
            footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
            footer2.setColspan(4);
            my_report_table3.addCell(footer2);

            String created4 = headerProposal.getUser(Parameter.readString("FINANCE_DIVISION_HEAD")).getStUserName().toUpperCase() + "\n"
                    + "Kepala Divisi";
            PdfPCell footer3 = new PdfPCell(new Phrase(created4, smallb));
            footer3.setBorder(Rectangle.NO_BORDER);
            footer3.setHorizontalAlignment(Element.ALIGN_LEFT);
            footer3.setColspan(5);
            my_report_table3.addCell(footer3);

            //Create QR Code by using BarcodeQRCode Class
            BarcodeQRCode my_code = new BarcodeQRCode(DateUtil.getDateStr(new Date(), "dd-MMM-yyyy HH:mm:ss") + "_" + headerProposal.getStNoSuratHutang() + "_" + JSPUtil.printX(headerProposal.getDbAmountTotal(), 0), 3, 3, null);
            //Get Image corresponding to the input string
            Image qr_image = my_code.getImage();

            PdfPCell barcode = new PdfPCell(qr_image);
            barcode.setBorder(Rectangle.NO_BORDER);
            barcode.setColspan(5);
            barcode.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table3.addCell(barcode);

            my_report_table.setWidths(new int[]{20, 20, 20, 20, 20});
            my_report_table2.setWidths(new int[]{3, 23, 3, 71});
            my_report_table3.setWidths(new int[]{20, 20, 20, 20, 20});

            /* Attach report table to PDF */
            my_pdf_report.add(my_report_table);
            my_pdf_report.add(my_report_table2);
            my_pdf_report.add(my_report_table3);
            my_pdf_report.close();

            /* Close all DB related objects */
            query_set.close();
            S.close();

//            String receiver = "prasetyo@askrida.co.id";
            String receiver = headerProposal.getCostCenter(headerProposal.getStCostCenterCode()).getStEmail();
            String subject = "Persetujuan Penarikan Dana";
            String text = "Dengan hormat,\n\n"
                    + "Bersama ini kami lampirkan “" + subject + "” yang dikirim otomatis oleh sistem.\n"
                    + "Atas perhatian dan kerjasamanya, kami ucapkan terima kasih.\n\n\n"
                    + "Hormat kami,\n"
                    + "Administrator";

            if (autoEmail) {
                MailUtil2 mail = new MailUtil2();
                mail.sendEmailWithType(pathTemp, fileName, receiver, subject, text, ".pdf");
            }

        } finally {
            conn.close();
        }
    }

    public void sendEmailApprovalBiaya(String uploadID) throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {

            final Statement S = conn.createStatement();

            /* Define the SQL query */
//            ResultSet query_set = S.executeQuery(
//                    " select pms_id,status1,status2,status3,status4,reverse_flag,cc_code,"
//                    + " no_spp,ket,jumlah_data,saldo_biaya,kabag_approved "
//                    + " from biaya_pemasaran where pms_id = " + pemasaran.getStPemasaranID()
//                    + " order by pms_id ");

            ResultSet query_set = S.executeQuery(
                    " select a.pms_id,status1,status2,status3,status4,reverse_flag,cc_code,"
                    + "no_spp,ket,jumlah_data,saldo_biaya,kabag_approved,ket,sum(b.excess_amount) as pajak "
                    + "from biaya_pemasaran a "
                    + "inner join biaya_pemasaran_detail b on b.pms_id = a.pms_id "
                    + " where a.pms_id = " + pemasaran.getStPemasaranID()
                    + " group by a.pms_id,status1,status2,status3,status4,reverse_flag,cc_code,"
                    + " no_spp,ket,jumlah_data,saldo_biaya,ket order by a.pms_id ");

            String fileName = "approval_biaya" + pemasaran.getStPemasaranID();
            File fo = new File("C:/");

            String fileFOlder = Parameter.readString("SYS_FILES_FOLDER");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

            String sf = sdf.format(pemasaran.getDtEntryDate());
            String tempPath = fileFOlder + File.separator + "report_temp" + File.separator + sf;
            String path1 = fileFOlder + File.separator + "report_temp" + File.separator;
            String pathTemp = tempPath + File.separator + fileName + ".pdf";

            try {
                new File(path1).mkdir();
                new File(tempPath).mkdir();
            } catch (Exception e) {
            }

            fo = new File(pathTemp);

            FileOutputStream fop = new FileOutputStream(fo);

            /* Step-2: Initialize PDF documents - logical objects */
            Document my_pdf_report = new Document();
            PdfWriter writer = PdfWriter.getInstance(my_pdf_report, fop);
            //PdfWriter.getInstance(my_pdf_report, fop);
            my_pdf_report.open();

            //we have four columns in our table
            PdfPTable my_report_table = new PdfPTable(5);
            my_report_table.setWidthPercentage(100);
            PdfPTable my_report_table2 = new PdfPTable(4);
            my_report_table2.setWidthPercentage(100);
            PdfPTable my_report_table4 = new PdfPTable(4);
            my_report_table4.setWidthPercentage(100);
            PdfPTable my_report_table3 = new PdfPTable(5);
            my_report_table3.setWidthPercentage(100);
            //create a cell object

            Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12),
                    smallb = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD),
                    smallc = new Font(Font.FontFamily.TIMES_ROMAN, 11),
                    smallbold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

            String requestNo = null;
//            String counterKey = DateUtil.getYear2Digit(new Date()) + DateUtil.getMonth2Digit(new Date());
            String counterKey = DateUtil.getYear(new Date());
            String rn = String.valueOf(IDFactory.createNumericID("REQBIAYAPMSNO" + counterKey, 1));
            rn = StringTools.leftPad(rn, '0', 3);

            requestNo = rn + "/KEU-BIAYA/" + DateUtil.getMonth2Digit(new Date()) + "/" + DateUtil.getYear(new Date());

            //insert heading
            String Tanggal = "Jakarta, " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "dd ^^ yyyy"));
            Image askridaLogoPath = Image.getInstance(Parameter.readString("DIGITAL_POLIS_LOGO_PIC"));
            final String no_surat[] = pemasaran.getStNoSPP().split("[\\/]");
            final String norutCab = no_surat[0] + "/" + Parameter.readString("BRANCH_SHORT_" + pemasaran.getStCostCenterCode()).toUpperCase() + "-BIAYA/" + DateUtil.getMonth2Digit(new Date()) + "/" + DateUtil.getYear(new Date());
            final String norutKP = requestNo;

            final SQLUtil SQ = new SQLUtil();
            PreparedStatement PS = SQ.setQuery("update biaya_pemasaran set no_bukti = ? where pms_id = ?");

            PS.setObject(1, norutKP);
            PS.setObject(2, uploadID);

            int p = PS.executeUpdate();

            SQ.release();

            PdfPCell logo = null;
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setColspan(2);
            my_report_table.addCell(logo);
            logo = new PdfPCell(askridaLogoPath);
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(logo);
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setColspan(2);
            my_report_table.addCell(logo);

            PdfPCell shkTanggal = null;
            shkTanggal = new PdfPCell(new Phrase("No. : " + norutKP, small));
            shkTanggal.setBorder(Rectangle.NO_BORDER);
            shkTanggal.setHorizontalAlignment(Element.ALIGN_LEFT);
            shkTanggal.setColspan(2);
            my_report_table.addCell(shkTanggal);
            shkTanggal = new PdfPCell(new Phrase(Tanggal, small));
            shkTanggal.setBorder(Rectangle.NO_BORDER);
            shkTanggal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            shkTanggal.setColspan(3);
            my_report_table.addCell(shkTanggal);

            //insert heading
            String created1 = "Kepada Yth.\n"
                    + "Kepala Cabang " + pemasaran.getCostCenter(pemasaran.getStCostCenterCode()).getStDescription() + "\n"
                    + pemasaran.getCostCenter(pemasaran.getStCostCenterCode()).getStAddress() + "\n\n\n";
            PdfPCell heading = new PdfPCell(new Phrase(created1, small));
            heading.setBorder(Rectangle.NO_BORDER);
            heading.setColspan(2);
            heading.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(heading);
            heading = new PdfPCell(new Phrase(" ", small));
            heading.setBorder(Rectangle.NO_BORDER);
            heading.setHorizontalAlignment(Element.ALIGN_CENTER);
            heading.setColspan(3);
            my_report_table.addCell(heading);

            //insert judul
            PdfPCell Title = new PdfPCell(new Phrase("Perihal : Persetujuan Pengeluaran Dana",
                    smallb));
            Title.setBorder(Rectangle.NO_BORDER);
            Title.setColspan(5);
            Title.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(Title);

            //insert an empty row
            PdfPCell emptyRow = new PdfPCell(new Phrase(" ", small));
            emptyRow.setBorder(Rectangle.NO_BORDER);
            emptyRow.setColspan(5);
            emptyRow.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(emptyRow);

            //insert isi
//            String created2 = "Menindaklanjuti surat dari Kantor Cabang " + pemasaran.getCostCenter(pemasaran.getStCostCenterCode()).getStDescription() + " No. " + norutCab + " perihal izin pengeluaran dana sebesar Rp. " + JSPUtil.printX(pemasaran.getDbTotalBiaya(), 2) + ",- "
//                    + "dengan ini Direksi menyetujui pengeluaran dana tersebut dan bukti pendukung transaksi agar diadministrasikan dengan tertib. "
//                    + "Untuk selanjutnya setiap penarikan dana dari bank agar sesuai dengan surat Direksi No. 444/DIR/VIII/2014.\n\n"
//                    + "Demikian kami sampaikan, atas perhatiannya kami ucapkan terima kasih.\n\n"
//                    + "Hormat kami,";
//            PdfPCell bySistem = new PdfPCell(new Phrase(created2, small));
//            bySistem.setBorder(Rectangle.NO_BORDER);
//            bySistem.setColspan(5);
//            bySistem.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
//            my_report_table.addCell(bySistem);

            String approvedKabag = null;
            String keterangan = null;
            String taxType = null;
            String taxTypeDesc = null;
            BigDecimal dbPajak = new BigDecimal(0);
            BigDecimal dbPajakdet = new BigDecimal(0);
            while (query_set.next()) {
                approvedKabag = query_set.getString("kabag_approved");
                dbPajak = query_set.getBigDecimal("pajak");
                keterangan = query_set.getString("ket");
            }

            String created2 = "Menindaklanjuti surat dari Kantor Cabang " + pemasaran.getCostCenter(pemasaran.getStCostCenterCode()).getStDescription() + " No. " + norutCab + " perihal izin pengeluaran dana, "
                    + "dengan ini Direksi menyetujui pengeluaran dana sebagai berikut: ";
            PdfPCell bySistem = new PdfPCell(new Phrase(created2, small));
            bySistem.setBorder(Rectangle.NO_BORDER);
            bySistem.setColspan(4);
            bySistem.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            my_report_table2.addCell(bySistem);

            PdfPCell kolomJudul = null;
            kolomJudul = new PdfPCell(new Phrase("No", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table2.addCell(kolomJudul);
            kolomJudul = new PdfPCell(new Phrase("Uraian Pengeluaran", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table2.addCell(kolomJudul);
            kolomJudul = new PdfPCell(new Phrase("Nominal", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table2.addCell(kolomJudul);
            kolomJudul = new PdfPCell(new Phrase("Keterangan", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table2.addCell(kolomJudul);

            PdfPCell headerIsi = null;
            headerIsi = new PdfPCell(new Phrase("1", small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table2.addCell(headerIsi);
            headerIsi = new PdfPCell(new Phrase("Biaya Pemasaran bulan " + LanguageManager.getInstance().translate(DateUtil.getDateStr(pemasaran.getDtEntryDate(), "^^ yyyy")) + " (Rincian Terlampir)", small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(headerIsi);
            headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(BDUtil.sub(pemasaran.getDbTotalBiaya(), dbPajak), 2), small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table2.addCell(headerIsi);
            headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(keterangan), small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(headerIsi);

            ResultSet query_tax = S.executeQuery(
                    " select row_number() over(order by a.pms_id,b.tax_type)+1 as no,b.tax_type,sum(b.excess_amount) as pajak "
                    + "from biaya_pemasaran a inner join biaya_pemasaran_detail b on b.pms_id = a.pms_id "
                    + "where a.pms_id = " + pemasaran.getStPemasaranID()
                    + " and b.excess_amount <> 0 group by a.pms_id,b.tax_type order by b.tax_type ");

            while (query_tax.next()) {
                taxType = query_tax.getString("tax_type");

                if (taxType.equalsIgnoreCase("1")) {
                    taxTypeDesc = "Pph 21";
                } else {
                    taxTypeDesc = "Pph 23";
                }

                BigDecimal no = query_tax.getBigDecimal("no");
                headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(no, 0), small));
                headerIsi.setHorizontalAlignment(Element.ALIGN_CENTER);
                my_report_table2.addCell(headerIsi);
                headerIsi = new PdfPCell(new Phrase(taxTypeDesc, small));
                headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
                my_report_table2.addCell(headerIsi);
                dbPajakdet = query_tax.getBigDecimal("pajak");
                headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(dbPajakdet, 2), small));
                headerIsi.setHorizontalAlignment(Element.ALIGN_RIGHT);
                my_report_table2.addCell(headerIsi);
                headerIsi = new PdfPCell(new Phrase("Pembayaran dilakukan bulan berikutnya pada kas negara", small));
                headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
                my_report_table2.addCell(headerIsi);
            }

//            if (Tools.compare(dbPajak, BDUtil.zero) > 0) {
//                headerIsi = new PdfPCell(new Phrase("2", small));
//                headerIsi.setHorizontalAlignment(Element.ALIGN_CENTER);
//                my_report_table2.addCell(headerIsi);
//                headerIsi = new PdfPCell(new Phrase(taxTypeDesc, small));
//                headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
//                my_report_table2.addCell(headerIsi);
//                headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(dbPajak, 2), small));
//                headerIsi.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                my_report_table2.addCell(headerIsi);
//                headerIsi = new PdfPCell(new Phrase("Pembayaran dilakukan bulan berikutnya pada kas negara", small));
//                headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
//                my_report_table2.addCell(headerIsi);
//            }

            PdfPCell headerTotal = null;
            headerTotal = new PdfPCell(new Phrase("TOTAL", small));
            headerTotal.setColspan(2);
            headerTotal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(headerTotal);
            headerTotal = new PdfPCell(new Phrase(JSPUtil.printX(pemasaran.getDbTotalBiaya(), 2), small));
            headerTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table2.addCell(headerTotal);
            headerTotal = new PdfPCell(new Phrase(" ", small));
            headerTotal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(headerTotal);

            String created = "Bukti pendukung transaksi agar diadministrasikan dengan tertib. "
                    + "Untuk selanjutnya setiap penarikan dana dari bank agar sesuai dengan surat Direksi No. 444/DIR/VIII/2014.\n\n"
                    + "Demikian kami sampaikan, atas perhatiannya kami ucapkan terima kasih.\n\n"
                    + "Hormat kami,";
            PdfPCell bySistem2 = new PdfPCell(new Phrase(created, small));
            bySistem2.setBorder(Rectangle.NO_BORDER);
            bySistem2.setColspan(4);
            bySistem2.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            my_report_table2.addCell(bySistem2);

            //insert footer
//            String created3 = "PT. ASURANSI BANGUN ASKRIDA\n"
//                    + "DIVISI KEUANGAN";
            String created3 = "PT. ASURANSI BANGUN ASKRIDA";
            PdfPCell footer1 = new PdfPCell(new Phrase(created3, smallb));
            footer1.setBorder(Rectangle.NO_BORDER);
            footer1.setColspan(4);
            footer1.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(footer1);

            Image imgKeuKabag = Image.getInstance(Parameter.readString("PROPOSAL_KEU_" + approvedKabag));
            Image imgKeuKasie = Image.getInstance(Parameter.readString("PROPOSAL_KEU_05690254"));
            PdfPCell footer4 = new PdfPCell(imgKeuKabag);
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table4.addCell(footer4);
            footer4 = new PdfPCell(new Phrase("DIVISI KEUANGAN", smallb));
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table4.addCell(footer4);
            footer4 = new PdfPCell(imgKeuKasie);
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table4.addCell(footer4);
            footer4 = new PdfPCell(new Phrase(" ", small));
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table4.addCell(footer4);

            Image img = Image.getInstance(pemasaran.getUser(Parameter.readString("FINANCE_DIVISION_HEAD")).getFile().getStFilePath());
            //insert column data
            PdfPCell footer2 = new PdfPCell(img);
            footer2.setBorder(Rectangle.NO_BORDER);
            footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table3.addCell(footer2);
            footer2 = new PdfPCell(new Phrase(" ", small));
            footer2.setBorder(Rectangle.NO_BORDER);
            footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
            footer2.setColspan(4);
            my_report_table3.addCell(footer2);

            String created4 = pemasaran.getUser(Parameter.readString("FINANCE_DIVISION_HEAD")).getStUserName().toUpperCase() + "\n"
                    + "Kepala Divisi";
            PdfPCell footer3 = new PdfPCell(new Phrase(created4, smallb));
            footer3.setBorder(Rectangle.NO_BORDER);
            footer3.setHorizontalAlignment(Element.ALIGN_LEFT);
            footer3.setColspan(5);
            my_report_table3.addCell(footer3);

            //Create QR Code by using BarcodeQRCode Class
            BarcodeQRCode my_code = new BarcodeQRCode(DateUtil.getDateStr(new Date(), "dd-MMM-yyyy HH:mm:ss") + "_" + pemasaran.getStNoSPP() + "_" + JSPUtil.printX(pemasaran.getDbSaldoBiaya(), 0), 3, 3, null);
            //Get Image corresponding to the input string
            Image qr_image = my_code.getImage();

            PdfPCell barcode = new PdfPCell(qr_image);
            barcode.setBorder(Rectangle.NO_BORDER);
            barcode.setColspan(5);
            barcode.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table3.addCell(barcode);

            my_report_table.setWidths(new int[]{20, 20, 20, 20, 20});
            my_report_table2.setWidths(new int[]{5, 30, 20, 45});
            my_report_table3.setWidths(new int[]{20, 20, 20, 20, 20});
            my_report_table4.setWidths(new int[]{3, 23, 3, 71});

            /* Attach report table to PDF */
            my_pdf_report.add(my_report_table);
            my_pdf_report.add(my_report_table2);
            my_pdf_report.add(my_report_table4);
            my_pdf_report.add(my_report_table3);
            my_pdf_report.close();

            /* Close all DB related objects */
            query_set.close();
            S.close();

//            String receiver = "dwi.puspita@askrida.com";
//            String receiver = "prasetyo@askrida.co.id";
            String receiver = pemasaran.getCostCenter(pemasaran.getStCostCenterCode()).getStEmail();
            String subject = "Persetujuan Penggunaan Biaya Pemasaran";
            String text = "Dengan hormat,\n\n"
                    + "Bersama ini kami lampirkan “" + subject + "” yang dikirim otomatis oleh sistem.\n"
                    + "Atas perhatian dan kerjasamanya, kami ucapkan terima kasih.\n\n\n"
                    + "Hormat kami,\n"
                    + "Administrator";

            if (autoEmail) {
                MailUtil2 mail = new MailUtil2();
                mail.sendEmailWithType(pathTemp, fileName, receiver, subject, text, ".pdf");
            }

        } finally {
            conn.close();
        }
    }

    public void sendEmailApprovalBiayaNoEmail() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {

            final Statement S = conn.createStatement();

            /* Define the SQL query */
//            ResultSet query_set = S.executeQuery(
//                    " select pms_id,status1,status2,status3,status4,reverse_flag,cc_code,"
//                    + " no_spp,ket,jumlah_data,total_biaya,kabag_approved "
//                    + " from biaya_pemasaran where pms_id = " + pemasaran.getStPemasaranID()
//                    + " order by pms_id ");

            ResultSet query_set = S.executeQuery(
                    " select a.pms_id,status1,status2,status3,status4,reverse_flag,cc_code,no_bukti,"
                    + "no_spp,ket,jumlah_data,total_biaya,kabag_approved,ket,sum(b.excess_amount) as pajak "
                    + "from biaya_pemasaran a "
                    + "inner join biaya_pemasaran_detail b on b.pms_id = a.pms_id "
                    + " where a.pms_id = " + pemasaran.getStPemasaranID()
                    + " group by a.pms_id,status1,status2,status3,status4,reverse_flag,cc_code,no_bukti,"
                    + " no_spp,ket,jumlah_data,total_biaya,ket order by a.pms_id ");

            String fileName = "approval_biaya" + pemasaran.getStPemasaranID();
            File fo = new File("C:/");

            String fileFOlder = Parameter.readString("SYS_FILES_FOLDER");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

            String approvedKabag = null;
            String keterangan = null;
            String noSurat = null;
            BigDecimal dbPajak = new BigDecimal(0);
            while (query_set.next()) {
                approvedKabag = query_set.getString("kabag_approved");
                dbPajak = query_set.getBigDecimal("pajak");
                keterangan = query_set.getString("ket");
                noSurat = query_set.getString("no_bukti");
            }

            String sf = sdf.format(pemasaran.getDtEntryDate());
            String tempPath = fileFOlder + File.separator + "report_temp" + File.separator + sf;
            String path1 = fileFOlder + File.separator + "report_temp" + File.separator;
            String pathTemp = tempPath + File.separator + fileName + ".pdf";

            try {
                new File(path1).mkdir();
                new File(tempPath).mkdir();
            } catch (Exception e) {
            }

            fo = new File(pathTemp);

            FileOutputStream fop = new FileOutputStream(fo);

            /* Step-2: Initialize PDF documents - logical objects */
            Document my_pdf_report = new Document();
            PdfWriter writer = PdfWriter.getInstance(my_pdf_report, fop);
            //PdfWriter.getInstance(my_pdf_report, fop);
            my_pdf_report.open();

            //we have four columns in our table
            PdfPTable my_report_table = new PdfPTable(5);
            my_report_table.setWidthPercentage(100);
            PdfPTable my_report_table2 = new PdfPTable(4);
            my_report_table2.setWidthPercentage(100);
            PdfPTable my_report_table4 = new PdfPTable(4);
            my_report_table4.setWidthPercentage(100);
            PdfPTable my_report_table3 = new PdfPTable(5);
            my_report_table3.setWidthPercentage(100);
            //create a cell object

            Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12),
                    smallb = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD),
                    smallc = new Font(Font.FontFamily.TIMES_ROMAN, 11),
                    smallbold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

            String requestNo = null;

            if (noSurat != null) {
                requestNo = noSurat;
            } else {
                String counterKey = DateUtil.getYear(pemasaran.getDtEntryDate());
                String rn = String.valueOf(IDFactory.createNumericID("REQBIAYAPMSNO" + counterKey, 1));
                rn = StringTools.leftPad(rn, '0', 3);

                requestNo = rn + "/KEU-BIAYA/" + DateUtil.getMonth2Digit(pemasaran.getDtEntryDate()) + "/" + DateUtil.getYear(pemasaran.getDtEntryDate());

                final SQLUtil SQ = new SQLUtil();
                PreparedStatement PS = SQ.setQuery("update biaya_pemasaran set no_bukti = ? where pms_id = ?");

                PS.setObject(1, requestNo);
                PS.setObject(2, pemasaran.getStPemasaranID());

                int p = PS.executeUpdate();

                SQ.release();

            }

            //insert heading
            String Tanggal = "Jakarta, " + LanguageManager.getInstance().translate(DateUtil.getDateStr(pemasaran.getDtEntryDate(), "dd ^^ yyyy"));
            Image askridaLogoPath = Image.getInstance(Parameter.readString("DIGITAL_POLIS_LOGO_PIC"));
            final String no_surat[] = pemasaran.getStNoSPP().split("[\\/]");
            final String norutCab = no_surat[0] + "/" + Parameter.readString("BRANCH_SHORT_" + pemasaran.getStCostCenterCode()).toUpperCase() + "-BIAYA/" + DateUtil.getMonth2Digit(pemasaran.getDtEntryDate()) + "/" + DateUtil.getYear(pemasaran.getDtEntryDate());
            final String norutKP = requestNo;

            PdfPCell logo = null;
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setColspan(2);
            my_report_table.addCell(logo);
            logo = new PdfPCell(askridaLogoPath);
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(logo);
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setColspan(2);
            my_report_table.addCell(logo);

            PdfPCell shkTanggal = null;
            shkTanggal = new PdfPCell(new Phrase("No. : " + norutKP, small));
            shkTanggal.setBorder(Rectangle.NO_BORDER);
            shkTanggal.setHorizontalAlignment(Element.ALIGN_LEFT);
            shkTanggal.setColspan(2);
            my_report_table.addCell(shkTanggal);
            shkTanggal = new PdfPCell(new Phrase(Tanggal, small));
            shkTanggal.setBorder(Rectangle.NO_BORDER);
            shkTanggal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            shkTanggal.setColspan(3);
            my_report_table.addCell(shkTanggal);

            //insert heading
            String created1 = "Kepada Yth.\n"
                    + "Kepala Cabang " + pemasaran.getCostCenter(pemasaran.getStCostCenterCode()).getStDescription() + "\n"
                    + pemasaran.getCostCenter(pemasaran.getStCostCenterCode()).getStAddress() + "\n\n\n";
            PdfPCell heading = new PdfPCell(new Phrase(created1, small));
            heading.setBorder(Rectangle.NO_BORDER);
            heading.setColspan(2);
            heading.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(heading);
            heading = new PdfPCell(new Phrase(" ", small));
            heading.setBorder(Rectangle.NO_BORDER);
            heading.setHorizontalAlignment(Element.ALIGN_CENTER);
            heading.setColspan(3);
            my_report_table.addCell(heading);

            //insert judul
            PdfPCell Title = new PdfPCell(new Phrase("Perihal : Persetujuan Pengeluaran Dana",
                    smallb));
            Title.setBorder(Rectangle.NO_BORDER);
            Title.setColspan(5);
            Title.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(Title);

            //insert an empty row
            PdfPCell emptyRow = new PdfPCell(new Phrase(" ", small));
            emptyRow.setBorder(Rectangle.NO_BORDER);
            emptyRow.setColspan(5);
            emptyRow.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(emptyRow);

            //insert isi
//            String created2 = "Menindaklanjuti surat dari Kantor Cabang " + pemasaran.getCostCenter(pemasaran.getStCostCenterCode()).getStDescription() + " No. " + norutCab + " perihal izin pengeluaran dana sebesar Rp. " + JSPUtil.printX(pemasaran.getDbTotalBiaya(), 2) + ",- "
//                    + "dengan ini Direksi menyetujui pengeluaran dana tersebut dan bukti pendukung transaksi agar diadministrasikan dengan tertib. "
//                    + "Untuk selanjutnya setiap penarikan dana dari bank agar sesuai dengan surat Direksi No. 444/DIR/VIII/2014.\n\n"
//                    + "Demikian kami sampaikan, atas perhatiannya kami ucapkan terima kasih.\n\n"
//                    + "Hormat kami,";
//            PdfPCell bySistem = new PdfPCell(new Phrase(created2, small));
//            bySistem.setBorder(Rectangle.NO_BORDER);
//            bySistem.setColspan(5);
//            bySistem.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
//            my_report_table.addCell(bySistem);

            String created2 = "Menindaklanjuti surat dari Kantor Cabang " + pemasaran.getCostCenter(pemasaran.getStCostCenterCode()).getStDescription() + " No. " + norutCab + " perihal izin pengeluaran dana, "
                    + "dengan ini Direksi menyetujui pengeluaran dana sebagai berikut: ";
            PdfPCell bySistem = new PdfPCell(new Phrase(created2, small));
            bySistem.setBorder(Rectangle.NO_BORDER);
            bySistem.setColspan(4);
            bySistem.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            my_report_table2.addCell(bySistem);

            PdfPCell kolomJudul = null;
            kolomJudul = new PdfPCell(new Phrase("No", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table2.addCell(kolomJudul);
            kolomJudul = new PdfPCell(new Phrase("Uraian Pengeluaran", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table2.addCell(kolomJudul);
            kolomJudul = new PdfPCell(new Phrase("Nominal", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table2.addCell(kolomJudul);
            kolomJudul = new PdfPCell(new Phrase("Keterangan", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table2.addCell(kolomJudul);

            PdfPCell headerIsi = null;
            headerIsi = new PdfPCell(new Phrase("1", small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table2.addCell(headerIsi);
            headerIsi = new PdfPCell(new Phrase("Biaya Pemasaran bulan " + LanguageManager.getInstance().translate(DateUtil.getDateStr(pemasaran.getDtEntryDate(), "^^ yyyy")) + " (Rincian Terlampir)", small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(headerIsi);
            headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(BDUtil.sub(pemasaran.getDbTotalBiaya(), dbPajak), 2), small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table2.addCell(headerIsi);
            headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(keterangan), small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(headerIsi);

            ResultSet query_tax = S.executeQuery(
                    " select row_number() over(order by a.pms_id,b.tax_type)+1 as no,b.tax_type,sum(b.excess_amount) as pajak "
                    + "from biaya_pemasaran a inner join biaya_pemasaran_detail b on b.pms_id = a.pms_id "
                    + "where a.pms_id = " + pemasaran.getStPemasaranID()
                    + " and b.excess_amount <> 0 group by a.pms_id,b.tax_type order by b.tax_type ");

            String taxType = null;
            String taxTypeDesc = null;
            BigDecimal dbPajakdet = new BigDecimal(0);
            while (query_tax.next()) {
                taxType = query_tax.getString("tax_type");

                if (taxType.equalsIgnoreCase("1")) {
                    taxTypeDesc = "Pph 21";
                } else {
                    taxTypeDesc = "Pph 23";
                }

                BigDecimal no = query_tax.getBigDecimal("no");
                headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(no, 0), small));
                headerIsi.setHorizontalAlignment(Element.ALIGN_CENTER);
                my_report_table2.addCell(headerIsi);
                headerIsi = new PdfPCell(new Phrase(taxTypeDesc, small));
                headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
                my_report_table2.addCell(headerIsi);
                dbPajakdet = query_tax.getBigDecimal("pajak");
                headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(dbPajakdet, 2), small));
                headerIsi.setHorizontalAlignment(Element.ALIGN_RIGHT);
                my_report_table2.addCell(headerIsi);
                headerIsi = new PdfPCell(new Phrase("Pembayaran dilakukan bulan berikutnya pada kas negara", small));
                headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
                my_report_table2.addCell(headerIsi);
            }

//            if (Tools.compare(dbPajak, BDUtil.zero) > 0) {
//                headerIsi = new PdfPCell(new Phrase("2", small));
//                headerIsi.setHorizontalAlignment(Element.ALIGN_CENTER);
//                my_report_table2.addCell(headerIsi);
//                headerIsi = new PdfPCell(new Phrase(taxTypeDesc, small));
//                headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
//                my_report_table2.addCell(headerIsi);
//                headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(dbPajak, 2), small));
//                headerIsi.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                my_report_table2.addCell(headerIsi);
//                headerIsi = new PdfPCell(new Phrase("Pembayaran dilakukan bulan berikutnya pada kas negara", small));
//                headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
//                my_report_table2.addCell(headerIsi);
//            }

            PdfPCell headerTotal = null;
            headerTotal = new PdfPCell(new Phrase("TOTAL", small));
            headerTotal.setColspan(2);
            headerTotal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(headerTotal);
            headerTotal = new PdfPCell(new Phrase(JSPUtil.printX(pemasaran.getDbTotalBiaya(), 2), small));
            headerTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table2.addCell(headerTotal);
            headerTotal = new PdfPCell(new Phrase(" ", small));
            headerTotal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(headerTotal);

            String created = "Bukti pendukung transaksi agar diadministrasikan dengan tertib. "
                    + "Untuk selanjutnya setiap penarikan dana dari bank agar sesuai dengan surat Direksi No. 444/DIR/VIII/2014.\n\n"
                    + "Demikian kami sampaikan, atas perhatiannya kami ucapkan terima kasih.\n\n"
                    + "Hormat kami,";
            PdfPCell bySistem2 = new PdfPCell(new Phrase(created, small));
            bySistem2.setBorder(Rectangle.NO_BORDER);
            bySistem2.setColspan(4);
            bySistem2.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            my_report_table2.addCell(bySistem2);

            //insert footer
//            String created3 = "PT. ASURANSI BANGUN ASKRIDA\n"
//                    + "DIVISI KEUANGAN";
            String created3 = "PT. ASURANSI BANGUN ASKRIDA";
            PdfPCell footer1 = new PdfPCell(new Phrase(created3, smallb));
            footer1.setBorder(Rectangle.NO_BORDER);
            footer1.setColspan(4);
            footer1.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(footer1);

            Image imgKeuKabag = Image.getInstance(Parameter.readString("PROPOSAL_KEU_" + approvedKabag));
            Image imgKeuKasie = Image.getInstance(Parameter.readString("PROPOSAL_KEU_05920834"));
            PdfPCell footer4 = new PdfPCell(imgKeuKabag);
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table4.addCell(footer4);
            footer4 = new PdfPCell(new Phrase("DIVISI KEUANGAN", smallb));
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table4.addCell(footer4);
            footer4 = new PdfPCell(imgKeuKasie);
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table4.addCell(footer4);
            footer4 = new PdfPCell(new Phrase(" ", small));
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table4.addCell(footer4);

            Image img = Image.getInstance(pemasaran.getUser(Parameter.readString("FINANCE_DIVISION_HEAD")).getFile().getStFilePath());
            //insert column data
            PdfPCell footer2 = new PdfPCell(img);
            footer2.setBorder(Rectangle.NO_BORDER);
            footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table3.addCell(footer2);
            footer2 = new PdfPCell(new Phrase(" ", small));
            footer2.setBorder(Rectangle.NO_BORDER);
            footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
            footer2.setColspan(4);
            my_report_table3.addCell(footer2);

            String created4 = pemasaran.getUser(Parameter.readString("FINANCE_DIVISION_HEAD")).getStUserName().toUpperCase() + "\n"
                    + "Kepala Divisi";
            PdfPCell footer3 = new PdfPCell(new Phrase(created4, smallb));
            footer3.setBorder(Rectangle.NO_BORDER);
            footer3.setHorizontalAlignment(Element.ALIGN_LEFT);
            footer3.setColspan(5);
            my_report_table3.addCell(footer3);

            //Create QR Code by using BarcodeQRCode Class
            BarcodeQRCode my_code = new BarcodeQRCode(DateUtil.getDateStr(pemasaran.getDtEntryDate(), "dd-MMM-yyyy HH:mm:ss") + "_" + pemasaran.getStNoSPP() + "_" + JSPUtil.printX(pemasaran.getDbSaldoBiaya(), 0), 3, 3, null);
            //Get Image corresponding to the input string
            Image qr_image = my_code.getImage();

            PdfPCell barcode = new PdfPCell(qr_image);
            barcode.setBorder(Rectangle.NO_BORDER);
            barcode.setColspan(5);
            barcode.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table3.addCell(barcode);

            my_report_table.setWidths(new int[]{20, 20, 20, 20, 20});
            my_report_table2.setWidths(new int[]{5, 30, 20, 45});
            my_report_table3.setWidths(new int[]{20, 20, 20, 20, 20});
            my_report_table4.setWidths(new int[]{3, 23, 3, 71});

            /* Attach report table to PDF */
            my_pdf_report.add(my_report_table);
            my_pdf_report.add(my_report_table2);
            my_pdf_report.add(my_report_table4);
            my_pdf_report.add(my_report_table3);
            my_pdf_report.close();

            /* Close all DB related objects */
            query_set.close();
            query_tax.close();
            S.close();

////            String receiver = "dwi.puspita@askrida.com";
//            String receiver = "prasetyo@askrida.co.id";
////            String receiver = pemasaran.getCostCenter(pemasaran.getStCostCenterCode()).getStEmail();
//            String subject = "Persetujuan Penggunaan Biaya Pemasaran";
//            String text = "Dengan hormat,\n\n"
//                    + "Bersama ini kami lampirkan “" + subject + "” yang dikirim otomatis oleh sistem.\n"
//                    + "Atas perhatian dan kerjasamanya, kami ucapkan terima kasih.\n\n\n"
//                    + "Hormat kami,\n"
//                    + "Administrator";
//
//            MailUtil2 mail = new MailUtil2();
//            mail.sendEmailWithType(pathTemp, fileName, receiver, subject, text, ".pdf");

        } finally {
            conn.close();
        }
    }

    //final boolean autoEmail = Parameter.readBoolean("SYS_SEND_EMAIL", 1, false);
    final boolean autoEmail = Parameter.readBoolean("SYS_SEND_EMAIL");

    public void createUploadRIManual() throws Exception {
        headerReins = new UploadHeaderReinsuranceView();
        headerReins.markNew();

        final DTOList details = new DTOList();

        headerReins.setDetails(details);

        //titipan.setStCostCenter(SessionManager.getInstance().getSession().getStBranch());

        setTitle("Upload Spreading RI Manual");

        headerReins.setStStatus("Belum proses data");

        editMode = true;
    }

    public void prosesRISpreadingManual() throws Exception {

        final SQLUtil S = new SQLUtil();

        String fileID = headerReins.getStFilePhysic();

        if(fileID==null){
            throw new RuntimeException("File excel belum diupload");
        }

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheetDetail = wb.getSheet("detil");

        int rows = sheetDetail.getPhysicalNumberOfRows();

        //looping update ins pol treaty detail
        //for (int r = 12; r < rows; r++) {
        int counterDetail = 0;

        for (int r = 1; r < rows; r++) {
            XSSFRow row = sheetDetail.getRow(r);

            // update ins_pol_treaty_detail set tsi_amount=372875000,premi_amount=906459.12,comm_rate=27.5,comm_amt=249276.258,premi_rate=null where ins_pol_tre_det_id=160420812;

            headerReins.setStStatus("sedang proses data detil ke "+ r +" ...");

            XSSFCell cellInsPolTreDetID = row.getCell(8);
            XSSFCell cellTSI = row.getCell(11);
            XSSFCell cellPremi = row.getCell(12);
            XSSFCell cellCommRate = row.getCell(13);
            XSSFCell cellCommAmt = row.getCell(14);
            XSSFCell cellPremiRate = row.getCell(19);

            BigDecimal detilTSI = new BigDecimal(cellTSI.getNumericCellValue());
            BigDecimal detilPremi = new BigDecimal(cellPremi.getNumericCellValue());
            BigDecimal detilCommRate = cellCommRate!=null?new BigDecimal(cellCommRate.getNumericCellValue()):BDUtil.zero;
            BigDecimal detilCommAmt = cellCommAmt!=null?new BigDecimal(cellCommAmt.getNumericCellValue()):BDUtil.zero;
            //BigDecimal detilPremiRate = new BigDecimal(cellPremiRate.getNumericCellValue());
            String insPolTreDetID = cellInsPolTreDetID.getCellType() == cellInsPolTreDetID.CELL_TYPE_STRING ? cellInsPolTreDetID.getStringCellValue() : new BigDecimal(cellInsPolTreDetID.getNumericCellValue()).toString();

            String updateDetil = "update ins_pol_treaty_detail set "
                    + "tsi_amount="+ detilTSI +","
                    + "premi_amount="+ detilPremi +","
                    + "comm_rate="+ detilCommRate +","
                    + "comm_amt="+ detilCommAmt +","
                    + "premi_rate=null "
                    + "where ins_pol_tre_det_id= ?";

            //updateDetil = updateDetil.replaceAll("=0", "=null");

            PreparedStatement PS = S.setQuery(updateDetil);

            PS.setObject(1,insPolTreDetID);

            int j = PS.executeUpdate();

            if(j!=0){
                counterDetail++;
            }

            S.release();

        }

        //sheet member
        XSSFSheet sheetMember = wb.getSheet("member");

        int rows2 = sheetMember.getPhysicalNumberOfRows();

        //looping update ins pol treaty detail
        //for (int r = 12; r < rows; r++) {

        int counterMember = 0;

        for (int r2 = 1; r2 < rows2; r2++) {
            XSSFRow row = sheetMember.getRow(r2);

            // update ins_pol_ri set premi_amount=,tsi_amount=37287500,premi_rate=null,ricomm_rate=30,ricomm_amt=0,ri_slip_no='' where ins_pol_ri_id=258466251;

            headerReins.setStStatus("sedang proses data member ke "+ r2 +" ...");

            XSSFCell cellInsPolRIId = row.getCell(9);
            XSSFCell cellTSI = row.getCell(18);
            XSSFCell cellPremi = row.getCell(16);
            XSSFCell cellCommRate = row.getCell(22);
            XSSFCell cellCommAmt = row.getCell(23);
            XSSFCell cellPremiRate = row.getCell(19);
            XSSFCell cellRISlip = row.getCell(24);

            BigDecimal detilTSI = new BigDecimal(cellTSI.getNumericCellValue());
            BigDecimal detilPremi = new BigDecimal(cellPremi.getNumericCellValue());
            BigDecimal detilCommRate = cellCommRate!=null?new BigDecimal(cellCommRate.getNumericCellValue()):BDUtil.zero;
            BigDecimal detilCommAmt = cellCommAmt!=null?new BigDecimal(cellCommAmt.getNumericCellValue()):BDUtil.zero;
            //BigDecimal detilPremiRate = new BigDecimal(cellPremiRate.getNumericCellValue());
            String InsPolRIId = cellInsPolRIId.getCellType() == cellInsPolRIId.CELL_TYPE_STRING ? cellInsPolRIId.getStringCellValue() : new BigDecimal(cellInsPolRIId.getNumericCellValue()).toString();
            String riSlipNo = "";

            if(cellRISlip!=null){
                riSlipNo = cellRISlip.getStringCellValue();
            }

            String updateDetil = "update ins_pol_ri set "
                    + "tsi_amount="+ detilTSI +","
                    + "premi_amount="+ detilPremi +","
                    + "ricomm_rate="+ detilCommRate +","
                    + "ricomm_amt="+ detilCommAmt +","
                    + "ri_slip_no='"+ riSlipNo +"'"
                    + " where ins_pol_ri_id= ?";

            //updateDetil = updateDetil.replaceAll("=0", "=null");

            PreparedStatement PS = S.setQuery(updateDetil);

            PS.setObject(1,InsPolRIId);

            int j = PS.executeUpdate();

            if(j!=0){
                counterMember++;
            }

            S.release();

        }

        //headerReins.setStFilePhysic(null);
        headerReins.setStStatus("Proses selesai, data detail "+ counterDetail + " rows dan data member "+ counterMember + " rows terproses");


        S.release();
    }

    public void createPrintRenewal() throws Exception {
        titipan = new UploadHeaderView();
        titipan.markNew();

        final DTOList details = new DTOList();

        titipan.setDetails(details);

        //titipan.setStCostCenter(SessionManager.getInstance().getSession().getStBranch());

        setTitle("Renewal Notice");

        editMode = true;
    }

    public void uploadExcelRenewal() throws Exception {

        String fileID = titipan.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("Renewal");

        final DTOList details = new DTOList();

        titipan.setDetails(details);

        int rows = sheet.getPhysicalNumberOfRows();

        //for (int r = 12; r < rows; r++) {
        for (int r = 4; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(1);
            if (cellControl == null) {
                break;
            }

            if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellNoPolis = row.getCell(3);
            HSSFCell cellPeriodEnd = row.getCell(4);

            uploadEndorsemenView endorse = new uploadEndorsemenView();
            endorse.markNew();

            endorse.setStPolicyNo(cellNoPolis.getStringCellValue());
            endorse.setDtPeriodeAkhir(cellPeriodEnd.getDateCellValue());

            details.add(endorse);

        }

        titipan.setDetails(details);

    }

    public void clickPrintRenewal() throws Exception {
        final DTOList l = titipan.getDetails();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);
        SessionManager.getInstance().getRequest().setAttribute("fontsize", stFontSize);

        loadFormList();

        String urx = "/pages/insurance/prodrpt/renewalnotice.fop";

        if (urx == null) {
            throw new RuntimeException("Unable to find suitable print form");
        }

        super.redirect(urx);

    }

    private static HashSet formList = null;

    private void loadFormList() {
        if (formList == null || true) {
            final String[] filez = new File(SessionManager.getInstance().getRequest().getSession().getServletContext().getRealPath("/pages/insurance/prodrpt")).list();

            formList = new HashSet();

            for (int i = 0; i < filez.length; i++) {
                String s = filez[i];

                formList.add(s);
            }
        }
    }

    public void createPolicyFop() throws Exception {
        titipan = new UploadHeaderView();
        titipan.markNew();

        final DTOList details = new DTOList();

        titipan.setDetails(details);

        //titipan.setStCostCenter(SessionManager.getInstance().getSession().getStBranch());

        setTitle("Policy FOP");

        editMode = true;
    }

    public void uploadExcelFop() throws Exception {

        String fileID = titipan.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("fop");

        final DTOList details = new DTOList();

        titipan.setDetails(details);

        int rows = sheet.getPhysicalNumberOfRows();

        String polno1 = "";
        String polno = "";
        HSSFRow row1 = sheet.getRow(4);

        HSSFCell cellNoPolis1 = row1.getCell(3);

        polno1 = "'" + cellNoPolis1.getStringCellValue() + "'";

        for (int r = 5; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(1);
            if (cellControl == null) {
                break;
            }

            if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellNoPolis = row.getCell(3);

            polno = polno + ",'" + cellNoPolis.getStringCellValue() + "'";
        }

        polno = polno1 + polno;
        logger.logDebug("@@@@@@@@@@@@@@@polno " + polno);

        DTOList proposal = validatePolnoJenis(polno, stBranch, stPolicyTypeID);
        for (int i = 0; i < proposal.size(); i++) {
            InsurancePolicyView dto = (InsurancePolicyView) proposal.get(i);

            if (dto.getStSignCode() != null) {
                throw new RuntimeException("Ikhtisar Polis " + dto.getStPolicyNo() + " sudah pernah dicetak ");
            }

            uploadEndorsemenView endorse = new uploadEndorsemenView();
            endorse.markNew();

            endorse.setStPolicyID(dto.getStPolicyID());
            endorse.setStPolicyNo(dto.getStPolicyNo());

            details.add(endorse);
        }

        titipan.setDetails(details);

    }

    public DTOList validatePolnoJenis(String attrpolno, String koda, String jenid) throws Exception {
        /*String ccCode = "'" + headerProposal.getStCostCenterCode() + "','80'";

        String polno = "select pol_no from ins_policy a where ";
        if (headerProposal.getStCostCenterCodeSource() != null) {
        if (headerProposal.getStCostCenterCodeSource().equalsIgnoreCase("1")) {
        if (headerProposal.getStCostCenterCode() != null) {
        polno = polno + "a.cc_code = '80' and a.cc_code_source = '" + headerProposal.getStCostCenterCode() + "'";
        }
        } else if (headerProposal.getStCostCenterCodeSource().equalsIgnoreCase("2")) {
        if (headerProposal.getStCostCenterCode() != null) {
        polno = polno + "a.cc_code = '" + headerProposal.getStCostCenterCode() + "'";
        }
        }
        } else {
        if (headerProposal.getStCostCenterCode() != null) {
        polno = polno + "((a.cc_code = '" + headerProposal.getStCostCenterCode() + "') or (a.cc_code = '80' and a.cc_code_source = '" + headerProposal.getStCostCenterCode() + "'))";
        }
        }
        polno = polno + " and a.pol_no in (" + attrpolno + ")";*/

        String sql = "select a.pol_id,a.pol_no,a.sign_code,a.print_counter,a.document_print_flag "
                + "from ins_policy a "
                + "where a.status in ('POLICY','RENEWAL','ENDORSE') and a.active_flag = 'Y' and a.effective_flag = 'Y' ";

        sql = sql + " and ((a.cc_code = '" + koda + "') or (a.cc_code = '80' and a.cc_code_source = '" + koda + "'))";
        sql = sql + " and a.pol_type_id = " + jenid + "";
        sql = sql + " and a.pol_no in (" + attrpolno + ")";
        sql = sql + " order by a.sign_code desc, a.pol_no ";

        return ListUtil.getDTOListFromQuery(sql, InsurancePolicyView.class);
    }

    public void clickPrintFop() throws Exception {
        final DTOList l = titipan.getDetails();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        loadFormList();

        final ArrayList plist = new ArrayList();

        plist.add("printfopall_" + stPolicyTypeID);
        plist.add("printfopall");
        logger.logDebug("printPolicy: scanlist:" + plist);

        String urx = null;
        for (int i = 0; i < plist.size(); i++) {
            String s = (String) plist.get(i);

            if (formList.contains(s + ".fop.jsp")) {
                urx = "/pages/insurance/prodrpt/" + s + ".fop";
                break;
            }
        }
        logger.logDebug("printPolicy:" + urx);

        if (urx == null) {
            throw new RuntimeException("Unable to find suitable print form");
        }

        for (int i = 0; i < l.size(); i++) {
            uploadEndorsemenView dto = (uploadEndorsemenView) l.get(i);

            final InsurancePolicyView policy = getRemoteInsurance().getInsurancePolicyForPrintingWithDigitalSign(dto.getStPolicyID(), "STANDARD");
        }

        super.redirect(urx);

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
     * @return the stPolicyTypeGroupDesc
     */
    public String getStPolicyTypeGroupDesc() {
        return stPolicyTypeGroupDesc;
    }

    /**
     * @param stPolicyTypeGroupDesc the stPolicyTypeGroupDesc to set
     */
    public void setStPolicyTypeGroupDesc(String stPolicyTypeGroupDesc) {
        this.stPolicyTypeGroupDesc = stPolicyTypeGroupDesc;
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
     * @return the stPolicyTypeDesc
     */
    public String getStPolicyTypeDesc() {
        return stPolicyTypeDesc;
    }

    /**
     * @param stPolicyTypeDesc the stPolicyTypeDesc to set
     */
    public void setStPolicyTypeDesc(String stPolicyTypeDesc) {
        this.stPolicyTypeDesc = stPolicyTypeDesc;
    }

    /**
     * @return the stBranch
     */
    public String getStBranch() {
        return stBranch;
    }

    /**
     * @param stBranch the stBranch to set
     */
    public void setStBranch(String stBranch) {
        this.stBranch = stBranch;
    }

    private String stFontSize;

    /**
     * @return the stFontSize
     */
    public String getStFontSize() {
        return stFontSize;
    }

    /**
     * @param stFontSize the stFontSize to set
     */
    public void setStFontSize(String stFontSize) {
        this.stFontSize = stFontSize;
    }

    public void doApprovedProposalComm() throws Exception {

        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;

        listPolicy = ListUtil.getDTOListFromQuery(
                " select a.* from ins_proposal_komisi a "
                + "inner join ar_invoice b on b.ar_invoice_id = a.ins_ar_invoice_id "
                + " where a.ins_upload_id = ? "
                + " order by a.ins_upload_dtl_id ",
                new Object[]{headerProposal.getStInsuranceUploadID()},
                uploadProposalCommView.class);

        if (listPolicy.size() < 1) {
            throw new Exception("Data tidak ada, Hubungi KP");
        }

        for (int i = 0; i < listPolicy.size(); i++) {
            uploadProposalCommView pol = (uploadProposalCommView) listPolicy.get(i);

            if (isApprovalCab()) {

                try {
                    PreparedStatement PS = S.setQuery("update ar_invoice set no_surat_hutang = ? where ar_invoice_id = ?");

                    PS.setObject(1, pol.getStNoSuratHutang());
                    PS.setObject(2, pol.getStARInvoiceID());

                    int p = PS.executeUpdate();

                    PreparedStatement PS2 = S.setQuery("update ins_proposal_komisi set status1 = 'Y',kacab_approved = ? where ins_upload_dtl_id = ?");

                    PS2.setObject(1, SessionManager.getInstance().getSession().getStUserID());
                    PS2.setObject(2, pol.getStInsuranceUploadDetailID());

                    int p2 = PS2.executeUpdate();

                } finally {
                    S.release();
                }
            }

            if (isApprovalSie()) {

                if (!isValidateJumlahSHKBeda()) {
                    throw new Exception("Jumlah Polis dalam SHK tidak sama, Hubungi KP");
                }

                boolean isValid = false;

                String cek = "select a.ar_invoice_id "
                        + "from ar_invoice a where a.ar_invoice_id = ? "
                        + "and a.amount_settled is not null and a.no_surat_hutang is not null "
                        + "and a.approved_flag is null ";

                try {
                    PreparedStatement validPS = S.setQuery(cek);
                    validPS.setString(1, pol.getStARInvoiceID());

                    ResultSet RS = validPS.executeQuery();

                    if (RS.next()) {
                        isValid = true;
                    }

                    if (isValid) {
                        String polisValid = getARInvoiceBySHK(pol.getStARInvoiceID()).getStPolicyNo();
                        throw new Exception("Polis " + polisValid + " Tidak valid/Sudah Dibayar");
                    }

                    PreparedStatement PS = S.setQuery("update ins_proposal_komisi set status2 = 'Y' where ins_upload_dtl_id = ?");

                    PS.setObject(1, pol.getStInsuranceUploadDetailID());

                    int p = PS.executeUpdate();

                } finally {
                    S.release();
                }
            }

            if (isApprovalBag()) {

                if (!isValidateJumlahSHKBeda()) {
                    throw new Exception("Jumlah Polis dalam SHK tidak sama, Hubungi KP");
                }

                boolean isValid = false;

                String cek = "select a.ar_invoice_id "
                        + "from ar_invoice a where a.ar_invoice_id = ? "
                        + "and a.amount_settled is not null and a.no_surat_hutang is not null "
                        + "and a.approved_flag is null ";

                try {
                    PreparedStatement validPS = S.setQuery(cek);
                    validPS.setString(1, pol.getStARInvoiceID());

                    ResultSet RS = validPS.executeQuery();

                    if (RS.next()) {
                        isValid = true;
                    }

                    if (isValid) {
                        String polisValid = getARInvoiceBySHK(pol.getStARInvoiceID()).getStPolicyNo();
                        throw new Exception("Polis " + polisValid + " Tidak valid/Sudah Dibayar");
                    }

                    PreparedStatement PS = S.setQuery("update ins_proposal_komisi set status3 = 'Y',kabag_approved = ? where ins_upload_dtl_id = ?");

                    PS.setObject(1, SessionManager.getInstance().getSession().getStUserID());
                    PS.setObject(2, pol.getStInsuranceUploadDetailID());

                    int p = PS.executeUpdate();

                } finally {
                    S.release();
                }
            }

            if (isApprovalDiv()) {

                if (!isValidateJumlahSHKBeda()) {
                    throw new Exception("Jumlah Polis dalam SHK tidak sama, Hubungi KP");
                }

                boolean isValid = false;

                try {
                    String cek = "select a.ar_invoice_id "
                            + "from ar_invoice a where a.ar_invoice_id = ? "
                            + "and a.amount_settled is not null and a.no_surat_hutang is not null "
                            + "and a.approved_flag is null ";

                    PreparedStatement validPS = S.setQuery(cek);
                    validPS.setString(1, pol.getStARInvoiceID());

                    ResultSet RS = validPS.executeQuery();

                    if (RS.next()) {
                        isValid = true;
                    }

                    if (isValid) {
                        String polisValid = getARInvoiceBySHK(pol.getStARInvoiceID()).getStPolicyNo();
                        throw new Exception("Polis " + polisValid + " Tidak valid/Sudah Dibayar");
                    }

                    PreparedStatement PS = S.setQuery("update ar_invoice set approved_flag = 'Y' where ar_invoice_id = ?");

                    PS.setObject(1, pol.getStARInvoiceID());

                    int p = PS.executeUpdate();

                    PreparedStatement PS2 = S.setQuery("update ins_proposal_komisi set status4 = 'Y',kadiv_approved = ?,approved_date = ? where ins_upload_dtl_id = ?");

                    PS2.setObject(1, SessionManager.getInstance().getSession().getStUserID());
                    PS2.setObject(2, new Date());
                    PS2.setObject(3, pol.getStInsuranceUploadDetailID());

                    int p2 = PS2.executeUpdate();

                } finally {
                    S.release();
                }
            }
        }

        if (isApprovalCab()) {
            if (autoEmail) {
                sendEmailProposal();
            }
        }

        if (isApprovalDiv()) {
            if (autoEmail) {
                sendEmailApproval();
            }
        }

        super.close();
    }


}
