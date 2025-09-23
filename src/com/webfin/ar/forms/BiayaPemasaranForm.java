/***********************************************************************
 * Module:  com.webfin.ar.forms.BiayaPemasaranForm
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:34 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.ar.forms;

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
import com.crux.util.NumberSpell;
import com.crux.util.SQLAssembler;
import com.crux.util.SQLUtil;
import com.crux.util.StringTools;
import com.crux.util.Tools;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.Rectangle;
import com.crux.web.controller.SessionManager;
import com.webfin.ar.ejb.AccountReceivableHome;
import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.ar.model.ARInvoiceView;
import com.webfin.gl.ejb.CurrencyManager;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.ejb.PeriodManager;
import com.webfin.gl.model.JournalView;
import com.webfin.gl.model.PeriodDetailView;

import com.webfin.ar.ejb.*;
import com.webfin.ar.model.BiayaPemasaranDetailView;
import com.webfin.ar.model.BiayaPemasaranDocumentsView;
import com.webfin.ar.model.BiayaPemasaranView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.joda.time.DateTime;

public class BiayaPemasaranForm extends Form {

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
    private boolean canCreate = SessionManager.getInstance().getSession().hasResource("PROP_CREATE");
    private boolean approvalCab = SessionManager.getInstance().getSession().hasResource("PROP_APPROVAL_CAB");
    private boolean approvalSie = SessionManager.getInstance().getSession().hasResource("PROP_APPROVAL_SIE");
    private boolean approvalBag = SessionManager.getInstance().getSession().hasResource("PROP_APPROVAL_BAG");
    private boolean approvalDiv = SessionManager.getInstance().getSession().hasResource("PROP_APPROVAL_DIV");
    private final static transient LogManager logger = LogManager.getInstance(BiayaPemasaranForm.class);
    private boolean reverseMode;
    private BiayaPemasaranView pemasaran;
    private String pemasaranID;
    private boolean receiptMode;
    private FormTab tabs;
    private boolean validasiMode;
    private boolean notaMode;

    public boolean isDrillMode() {
        return drillMode;
    }

    public void setDrillMode(boolean drillMode) {
        this.drillMode = drillMode;
    }

    public void drillToggle() {
        drillMode = !drillMode;
    }

//    public void btnPrint() throws Exception {
//    }
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

    public BiayaPemasaranForm() {
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
    public void view(String titipanID) throws Exception {

        super.setReadOnly(true);

        viewMode = true;
    }

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

    public boolean isApproveMode() {
        return approveMode;
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

    public void onChangeBranchGroup() {
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

    public void onChangePolicyTypeGroup() {
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
        det.setDbExcRatePajak(BDUtil.zero);

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
        sqa.addClause("a.pol_type_id in (59,87,88)");

        String cabang = null;
        if (pemasaran.getStCostCenterCode().equalsIgnoreCase("15") || pemasaran.getStCostCenterCode().equalsIgnoreCase("18")) {
            cabang = "'15','18'";
        } else if (pemasaran.getStCostCenterCode().equalsIgnoreCase("13") || pemasaran.getStCostCenterCode().equalsIgnoreCase("19")) {
            cabang = "'13','19'";
        } else if (pemasaran.getStCostCenterCode().equalsIgnoreCase("40") || pemasaran.getStCostCenterCode().equalsIgnoreCase("45")) {
            cabang = "'40','45'";
        } else if (pemasaran.getStCostCenterCode().equalsIgnoreCase("60") || pemasaran.getStCostCenterCode().equalsIgnoreCase("61")) {
            cabang = "'60','61'";
        } else {
            cabang = "'" + pemasaran.getStCostCenterCode() + "'";
        }

        if (pemasaran.getStCostCenterCode() != null) {
            sqa.addClause("a.cc_code in (" + cabang + ")");
//            sqa.addClause("a.cc_code =?");
//            sqa.addPar(pemasaran.getStCostCenterCode());
        }

        String bulan = null;
        int tanggal = DateUtil.getMonthDigit(pemasaran.getDtEntryDate()) - 1;
        if (tanggal < 10) {
            bulan = "0" + tanggal;
        } else {
            bulan = String.valueOf(tanggal);
        }

        if (pemasaran.getStMonths() != null) {
            sqa.addClause("substr(a.policy_date::text,6,2) = ?");
            sqa.addPar(bulan);
//            sqa.addPar(pemasaran.getStMonths());
        }

        if (pemasaran.getStYears() != null) {
            sqa.addClause("substr(a.policy_date::text,1,4) = ?");
            sqa.addPar(pemasaran.getStYears());
        }

        String sql = "select sum(a.premi-a.diskon) as biaya "
                + "from ( " + sqa.getSQL() + " group by a.cc_code ) a ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                BiayaPemasaranView.class);

        BiayaPemasaranView close = (BiayaPemasaranView) l.get(0);
        setDbProdAskred(close.getDbBiaya());

    }

    public void recalculatePms() throws Exception {
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

    public void recalculatePmsNew() throws Exception {
        /*REVISI 2*/

        onChgPeriod();

        if (pemasaran.getDbBiaya() == null) {
//            onChangeAmount();
            BigDecimal prodAskredCabPms = getTotalAskredCabPms(pemasaran.getStMonths(), pemasaran.getStYears(), pemasaran.getStCostCenterCode());
            BigDecimal prodAskredCabang = getProdAskredCabang(pemasaran.getStMonths(), pemasaran.getStYears(), pemasaran.getStCostCenterCode());

            BigDecimal prodGsAskred = getAnggaranGoalset(pemasaran.getStMonths(), pemasaran.getStYears(), pemasaran.getStCostCenterCode());
            if (prodGsAskred == null) {
                prodGsAskred = BDUtil.zero;
            }
            setDbProduksiGs(prodGsAskred);

            logger.logDebug("@@@@@@@@@@@@@@@@ Prod.GS Askred : " + dbProduksiGs);
            logger.logDebug("@@@@@@@@@@@@@@@@ Prod.AskredCabPms : " + prodAskredCabPms);
            logger.logDebug("@@@@@@@@@@@@@@@@ Prod.AskredCabang : " + prodAskredCabang);

            BigDecimal persen = BDUtil.getPctFromRate(BDUtil.div(prodAskredCabPms, dbProduksiGs, 4));
            logger.logDebug("@@@@@@@@@@@@@@@@ persen : " + persen);
            logger.logDebug("@@@@@@@@@@@@@@@@ persen : " + BDUtil.getRateFromPct(persen));
            BigDecimal saldoBiaya = new BigDecimal(0);
            BigDecimal rateBiaya = new BigDecimal(Parameter.readString("RATE_BIAYAPMS"));

            if (Tools.compare(prodAskredCabPms, BDUtil.zero) > 0) {
                if (Tools.compare(prodAskredCabPms, dbProduksiGs) <= 0) {
                    saldoBiaya = BDUtil.mul(prodAskredCabang, BDUtil.getRateFromPct(rateBiaya));
//                saldoBiaya = BDUtil.mul(prodAskredCabang, BDUtil.getRateFromPct(BDUtil.one));
//                logger.logDebug("@@@@@@@@@@@@@@@@ Biaya 1% : " + saldoBiaya);
                    saldoBiaya = BDUtil.mul(saldoBiaya, BDUtil.getRateFromPct(persen));
//                logger.logDebug("@@@@@@@@@@@@@@@@ persen2 : " + BDUtil.getRateFromPct(persen));
//                logger.logDebug("@@@@@@@@@@@@@@@@ Biaya prorata : " + saldoBiaya);
                }
                if (Tools.compare(prodAskredCabPms, dbProduksiGs) > 0) {
                    saldoBiaya = BDUtil.mul(prodAskredCabang, BDUtil.getRateFromPct(rateBiaya));
//                saldoBiaya = BDUtil.mul(prodAskredCabang, BDUtil.getRateFromPct(BDUtil.one));
//                logger.logDebug("@@@@@@@@@@@@@@@@ Biaya 1% : " + saldoBiaya);
                }
            } else {
                saldoBiaya = BDUtil.zero;
            }

            pemasaran.setDbBiaya(saldoBiaya);
            logger.logDebug("@@@@@@@@@@@@@@@@ Biaya 0.5% : " + pemasaran.getDbBiaya());

        }

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

            if (!pemasaran.isExOnePersen()) {
                if (Tools.compare(object.getDbNilai(), new BigDecimal(25000000)) > 0) {
                    throw new Exception("Transaksi " + object.getStDescription() + " melebihi limit Rp 25.000.000 (Dua Puluh Lima Juta Rupiah)");
                }
            }

            if (Tools.compare(object.getDbExcRatePajak(), BDUtil.zero) > 0) {
                if (object.getStTaxType() == null) {
                    throw new Exception("Description " + object.getStDescription() + " dengan nilai " + JSPUtil.print(object.getDbNilai(), 2) + ", Tipe Pajak belum dipilih");
                }
            }
        }

        pemasaran.setStJumlahData(String.valueOf(details.size()));
        pemasaran.setDbTotalBiaya(totalAmount);
        logger.logDebug("@@@@@@@@@@@@@@@@ totalbiaya : " + pemasaran.getDbTotalBiaya());

        BigDecimal totalRealisasi = getTotalRealisasi(pemasaran.getStMonths(), pemasaran.getStYears(), pemasaran.getStCostCenterCode());
        logger.logDebug("@@@@@@@@@@@@@@@@ totalrealsblm : " + totalRealisasi);

        pemasaran.setDbSaldoBiaya(totalRealisasi);

        BigDecimal totalReal = new BigDecimal(0);
        BigDecimal akhirBiaya = new BigDecimal(0);
        totalReal = BDUtil.add(totalAmount, totalRealisasi);
        akhirBiaya = BDUtil.sub(pemasaran.getDbBiaya(), totalReal);
        logger.logDebug("@@@@@@@@@@@@@@@@ totalReal : " + totalReal);
        logger.logDebug("@@@@@@@@@@@@@@@@ akhirbiaya : " + akhirBiaya);

        if (!pemasaran.isExOnePersen()) {
            if (Tools.compare(akhirBiaya, BDUtil.zero) < 0) {
                throw new Exception("Penggunaan Biaya sudah melebihi limit Biaya Pemasaran!<br>"
                        + "(" + JSPUtil.print(pemasaran.getDbBiaya(), 2) + " < " + JSPUtil.print(totalReal, 2) + ")");
            }
        }

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

//        int tahunCodeLast;
//        int bulanCode = Integer.parseInt(pemasaran.getStMonths());

//        if (bulanCode == 1) {
//            tahunCodeLast = Integer.parseInt(pemasaran.getStYears()) - 1;
//
//            dateStart = DateUtil.getDate("01/12/" + tahunCodeLast);
//            dateEnd = DateUtil.getDate("31/12/" + tahunCodeLast);
//        } else if (bulanCode > 1) {
//            int bulanCodeNow = Integer.parseInt(pemasaran.getStMonths());
//
//            PeriodDetailView pd = PeriodManager.getInstance().getPeriod(String.valueOf(bulanCodeNow), DateUtil.getYear(pemasaran.getDtEntryDate()));
//            dateStart = pd.getDtStartDate();
//            dateEnd = DateUtil.getDate(DateUtil.getDateStr(pd.getDtEndDate(), "dd/MM/yyyy"));
//        }
//
//        logger.logDebug("@@@@@@@@@@@@@@@@@date1 " + dateStart);
//        logger.logDebug("@@@@@@@@@@@@@@@@@date2 " + dateEnd);
//
//        if (!Tools.isEqual(DateUtil.getMonth2Digit(new Date()), pemasaran.getStMonths())) {
//            pemasaran.setDtEntryDate(dateEnd);
//        } else {
//            pemasaran.setDtEntryDate(new Date());
//        }

        Date dateEnd = null;
        PeriodDetailView pd = PeriodManager.getInstance().getPeriod(pemasaran.getStMonths(), pemasaran.getStYears());
        dateEnd = DateUtil.getDate(DateUtil.getDateStr(pd.getDtEndDate(), "dd/MM/yyyy"));

//        pemasaran.setDtEntryDate(dateEnd);
//        logger.logDebug("@@@@@@@@@@@@@@@@@date3 " + pemasaran.getDtEntryDate());

        logger.logDebug("@@@@@@@@@@@@@@@@@1 " + DateUtil.getMonth2Digit(new Date()));
        logger.logDebug("@@@@@@@@@@@@@@@@@2 " + pemasaran.getStMonths());
        logger.logDebug("@@@@@@@@@@@@@@@@@3 " + dateEnd);

        if (!Tools.isEqual(DateUtil.getMonth2Digit(new Date()), pemasaran.getStMonths())) {
            pemasaran.setDtEntryDate(dateEnd);
        } else {
            pemasaran.setDtEntryDate(new Date());
        }
        logger.logDebug("@@@@@@@@@@@@@@@@@date3 " + pemasaran.getDtEntryDate());

        DateTime currentDateLastDay = new DateTime(pemasaran.getDtEntryDate());
        currentDateLastDay = currentDateLastDay.dayOfMonth().withMaximumValue();
        int batasMaxHari = 5;
        DateTime maximumBackDate = new DateTime();
        maximumBackDate = currentDateLastDay.plusDays(batasMaxHari);

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();
        Date todayWithZeroTime = formatter.parse(formatter.format(today));
        DateTime currentDate = new DateTime(todayWithZeroTime);

        logger.logDebug("@@@@@@@@@@@@@@@@@1 " + currentDateLastDay);
        logger.logDebug("@@@@@@@@@@@@@@@@@2 " + currentDate);
        logger.logDebug("@@@@@@@@@@@@@@@@@3 " + maximumBackDate);

//        if (SessionManager.getInstance().getSession().getStBranch() != null) {
        if (currentDate.isAfter(maximumBackDate)) {
            throw new RuntimeException("Tanggal Biaya tidak bisa bulan sebelumnya, dikarenakan sudah \n"
                    + "melebihi waktu 5 hari dari Tanggal bulan berjalan ");
        } else {
            if (!Tools.isEqual(DateUtil.getMonth2Digit(pemasaran.getDtEntryDate()), pemasaran.getStMonths())) {
                throw new Exception("Bulan pada Tanggal Biaya tidak sama dengan Bulan Transaksi");
            }

            if (!Tools.isEqual(DateUtil.getYear(pemasaran.getDtEntryDate()), pemasaran.getStYears())) {
                throw new Exception("Tahun pada Tanggal Biaya tidak sama dengan Tahun Transaksi");
            }
        }
//        }
    }

    public void doSavePms() throws Exception {

        recalculatePms();

        if (getPmsDetail().size() == 0) {
            throw new RuntimeException("Anggaran belum diinput");
        }

        if (getDocuments().size() == 0) {
            throw new RuntimeException("Lampiran belum diinput");
        }

        /*Format Lama*/
//        final DTOList documents = pemasaran.getDocuments();
//        for (int j = 0; j < documents.size(); j++) {
//            BiayaPemasaranDocumentsView doc = (BiayaPemasaranDocumentsView) documents.get(j);
//
//            if (doc.getStFilePhysic() == null) {
//                throw new RuntimeException("File Lampiran Belum di Upload");
//            }
//
//            /*REVISI 2*/
////            if (j > 0) {
////                BiayaPemasaranDocumentsView doc2 = (BiayaPemasaranDocumentsView) documents.get(j - 1);
////                String nopol = doc2.getStKeterangan();
////                String nopol2 = doc.getStKeterangan();
////                if (nopol.equalsIgnoreCase(nopol2)) {
////                    throw new RuntimeException("No. Surat sama, tidak bisa disimpan");
////                }
////            }
////
////            if (validateNoSuratPemasaran(doc.getStKeterangan(), doc.getStPemasaranID()) != null) {
////                adaAkunKosong = true;
////                listAkunKosong = listAkunKosong + "<br>" + doc.getStKeterangan();
////            }
////
////            if (adaAkunKosong) {
////                throw new RuntimeException("No. Surat " + listAkunKosong + " Sudah Terinput. ");
////            }
//        }
//        pemasaran.setStKeterangan(getStNoLampiranDesc());

        boolean adaAkunKosong = false;
        String listAkunKosong = "";
//        if (validateNoSuratPemasaran(pemasaran.getStKeterangan(), pemasaran.getStPemasaranID()) != null) {
        if (validateNoSuratPemasaran(pemasaran) != null) {
            adaAkunKosong = true;
            listAkunKosong = listAkunKosong + "<br>" + pemasaran.getStKeterangan();
        }

        logger.logDebug("@@@@@@@@@@@@@@@ " + adaAkunKosong);

        if (adaAkunKosong) {
            throw new RuntimeException("No. Surat " + listAkunKosong + " Sudah Terinput. ");
        }

        getRemoteGeneralLedger().savePemasaran(pemasaran);

        super.close();

    }

    public void viewPemasaran(String ardepoid) throws Exception {
        setPemasaranID("rkappem");

        pemasaran = getRemoteGeneralLedger().loadPemasaran(ardepoid);

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

        pemasaran = getRemoteGeneralLedger().loadPemasaran(ardepoid);

        if (pemasaran.isStatus1Flag()) {
            throw new Exception("Data Sudah disetujui Cabang");
        }

        super.setReadOnly(true);

        approvalCab = true;

    }

    public void approvalPms2(String ardepoid) throws Exception {
//        dataProposalComm(titipanID);
        setPemasaranID("rkappem");

        pemasaran = getRemoteGeneralLedger().loadPemasaran(ardepoid);

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

        pemasaran = getRemoteGeneralLedger().loadPemasaran(ardepoid);

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

        pemasaran = getRemoteGeneralLedger().loadPemasaran(ardepoid);

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
                PreparedStatement PS = S.setQuery("update biaya_pemasaran set status1 = 'Y' where pms_id = ?");

                PS.setObject(1, pemasaran.getStPemasaranID());

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

            getRemoteGeneralLedger().reversePms(pemasaran);

            PreparedStatement PS = S.setQuery("update biaya_pemasaran set status2 = 'N', status3 = 'N', status4 = 'N', no_bukti_bayar = null, tgl_bayar = null where pms_id = ?");

            PS.setObject(1, pemasaran.getStPemasaranID());

            int p = PS.executeUpdate();
        }

        close();
    }

    public void reversePmsCab(String ardepoid) throws Exception {
        setPemasaranID("rkappem");

        pemasaran = getRemoteGeneralLedger().loadPemasaran(ardepoid);

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

        pemasaran = getRemoteGeneralLedger().loadPemasaran(ardepoid);

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
            headerIsi = new PdfPCell(new Phrase("Biaya Pemasaran bulan " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "^^ yyyy")) + " (Rincian Terlampir)", small));
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
                    + "no_spp,ket,jumlah_data,total_biaya,kabag_approved,kadiv_approved,ket,round(sum(b.excess_amount),2) as pajak  "
                    + "from biaya_pemasaran a "
                    + "inner join biaya_pemasaran_detail b on b.pms_id = a.pms_id "
                    + " where a.pms_id = " + pemasaran.getStPemasaranID()
                    + " group by a.pms_id,status1,status2,status3,status4,reverse_flag,cc_code,"
                    + " no_spp,ket,jumlah_data,total_biaya,ket,kabag_approved,kadiv_approved order by a.pms_id ");

            String fileName = "approval_biaya" + pemasaran.getStPemasaranID();
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
//            PreparedStatement PS = SQ.setQuery("update biaya_pemasaran set no_bukti = ? where pms_id = ?");
            PreparedStatement PS = SQ.setQuery("update biaya_pemasaran set no_surat = ? where pms_id = ?");

            PS.setObject(1, norutKP);
            PS.setObject(2, pemasaran.getStPemasaranID());

            int p = PS.executeUpdate();

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
            String approvedKadiv = null;
            String keterangan = null;
            String taxType = null;
            String taxTypeDesc = null;
            BigDecimal dbPajak = new BigDecimal(0);
            BigDecimal dbPajakdet = new BigDecimal(0);
            while (query_set.next()) {
                approvedKabag = query_set.getString("kabag_approved");
                approvedKadiv = query_set.getString("kadiv_approved");
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
            headerIsi = new PdfPCell(new Phrase("Biaya Pemasaran bulan " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "^^ yyyy")) + " (Rincian Terlampir)", small));
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

            //Image imgKeuKadiv = Image.getInstance(Parameter.readString("PROPOSAL_KEU_" + approvedKadiv));
            //Image img = Image.getInstance(pemasaran.getUser(Parameter.readString("FINANCE_DIVISION_HEAD")).getFile().getStFilePath());
            Image img = null;
            if (approvedKadiv != null) {
                img = Image.getInstance(pemasaran.getUser(approvedKadiv).getFile().getStFilePath());
            } else {
                img = Image.getInstance(pemasaran.getUser(Parameter.readString("PROPOSAL_KEU_KADIV")).getFile().getStFilePath());
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

            String created4 = pemasaran.getUser(kadiv).getStUserName().toUpperCase() + "\n"
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

        pemasaran = getRemoteGeneralLedger().loadPemasaran(ardepoid);

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

        final DTOList details = pemasaran.getPmsDetail();
        getRemoteGeneralLedger().approvePmsBayar(pemasaran, details);

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
//            tabs.add(new FormTab.TabBean("TAB3", "LAMPIRAN", true));

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

//    public BiayaPemasaranView validateNoSuratPemasaran(String keterangan, String pmsid) throws Exception {
    public BiayaPemasaranView validateNoSuratPemasaran(BiayaPemasaranView pms) throws Exception {

        String sql = "select * from biaya_pemasaran where ket = ? and cc_code = ? ";
        if (pms.getStPemasaranID() != null) {
            sql = sql + " and pms_id <> " + pms.getStPemasaranID();
        }

        BiayaPemasaranView ac = (BiayaPemasaranView) ListUtil.getDTOListFromQuery(
                sql,
                new Object[]{pms.getStKeterangan(), pms.getStCostCenterCode()},
                BiayaPemasaranView.class).getDTO();

        return ac;
    }

    public void printReloadPms() throws Exception {
        sendEmailProposalBiayaNoEmail();
        sendEmailApprovalBiayaNoEmail();
        super.close();
    }

    public void sendEmailProposalBiayaNoEmail() throws Exception {
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
            final String norut = no_surat[0] + "/" + Parameter.readString("BRANCH_SHORT_" + pemasaran.getStCostCenterCode()).toUpperCase() + "-BIAYA/" + DateUtil.getMonth2Digit(pemasaran.getDtEntryDate()) + "/" + DateUtil.getYear(pemasaran.getDtEntryDate());

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
            headerTanggal = new PdfPCell(new Phrase(": " + LanguageManager.getInstance().translate(DateUtil.getDateStr(pemasaran.getDtEntryDate(), "dd ^^ yyyy")), small));
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
            BarcodeQRCode my_code = new BarcodeQRCode(DateUtil.getDateStr(pemasaran.getDtEntryDate(), "dd-MMM-yyyy HH:mm:ss") + "_" + pemasaran.getStNoSPP() + "_" + JSPUtil.printX(pemasaran.getDbSaldoBiaya(), 0), 3, 3, null);
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

            String created = "Created by System " + LanguageManager.getInstance().translate(DateUtil.getDateStr(pemasaran.getDtEntryDate(), "dd ^^ yyyy HH:mm:ss"));
            PdfPCell bySistem2 = new PdfPCell(new Phrase(created, small6));
            bySistem2.setBorder(Rectangle.NO_BORDER);
            bySistem2.setColspan(4);
            bySistem2.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(bySistem2);
            my_report_table2.completeRow();

            //Create QR Code by using BarcodeQRCode Class
            BarcodeQRCode my_code2 = new BarcodeQRCode(DateUtil.getDateStr(pemasaran.getDtEntryDate(), "dd-MMM-yyyy HH:mm:ss") + "_" + JSPUtil.printX(komisiTotal, 2), 3, 3, null);
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
                    " select a.pms_id,status1,status2,status3,status4,reverse_flag,cc_code,no_surat,a.entry_date,"
                    + "no_spp,ket,jumlah_data,total_biaya,kabag_approved,kadiv_approved,approved_date,ket,round(sum(b.excess_amount),2) as pajak  "
                    + "from biaya_pemasaran a "
                    + "inner join biaya_pemasaran_detail b on b.pms_id = a.pms_id "
                    + " where a.pms_id = " + pemasaran.getStPemasaranID()
                    + " group by a.pms_id,status1,status2,status3,status4,reverse_flag,cc_code,no_surat,a.entry_date,"
                    + " no_spp,ket,jumlah_data,total_biaya,ket,kabag_approved,kadiv_approved,approved_date order by a.pms_id ");

            String fileName = "approval_biaya" + pemasaran.getStPemasaranID();
            File fo = new File("C:/");

            String fileFOlder = Parameter.readString("SYS_FILES_FOLDER");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

            Date entry = null;
            Date approved = null;
            String approvedKabag = null;
            String approvedKadiv = null;
            String keterangan = null;
            String noSurat = null;
            BigDecimal dbPajak = new BigDecimal(0);
            while (query_set.next()) {
                entry = query_set.getDate("entry_date");
                approved = query_set.getDate("approved_date");
                approvedKabag = query_set.getString("kabag_approved");
                approvedKadiv = query_set.getString("kadiv_approved");
                dbPajak = query_set.getBigDecimal("pajak");
                keterangan = query_set.getString("ket");
                noSurat = query_set.getString("no_surat");
            }

            Date currentdate = null;
            if(approved!=null){
                currentdate = approved;
            }else {
                currentdate = entry;
            }

            String sf = sdf.format(currentdate);
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
                String counterKey = DateUtil.getYear(currentdate);
                String rn = String.valueOf(IDFactory.createNumericID("REQBIAYAPMSNO" + counterKey, 1));
                rn = StringTools.leftPad(rn, '0', 3);

                requestNo = rn + "/KEU-BIAYA/" + DateUtil.getMonth2Digit(currentdate) + "/" + DateUtil.getYear(currentdate);

                final SQLUtil SQ = new SQLUtil();
                PreparedStatement PS = SQ.setQuery("update biaya_pemasaran set no_surat = ? where pms_id = ?");

                PS.setObject(1, requestNo);
                PS.setObject(2, pemasaran.getStPemasaranID());

                int p = PS.executeUpdate();

            }

            //insert heading
            String Tanggal = "Jakarta, " + LanguageManager.getInstance().translate(DateUtil.getDateStr(currentdate, "dd ^^ yyyy"));
            Image askridaLogoPath = Image.getInstance(Parameter.readString("DIGITAL_POLIS_LOGO_PIC"));
            final String no_surat[] = pemasaran.getStNoSPP().split("[\\/]");
            final String norutCab = no_surat[0] + "/" + Parameter.readString("BRANCH_SHORT_" + pemasaran.getStCostCenterCode()).toUpperCase() + "-BIAYA/" + DateUtil.getMonth2Digit(currentdate) + "/" + DateUtil.getYear(currentdate);
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
            headerIsi = new PdfPCell(new Phrase("Biaya Pemasaran bulan " + LanguageManager.getInstance().translate(DateUtil.getDateStr(currentdate, "^^ yyyy")) + " (Rincian Terlampir)", small));
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

            //Image imgKeuKadiv = Image.getInstance(Parameter.readString("PROPOSAL_KEU_" + approvedKadiv));
            //Image img = Image.getInstance(pemasaran.getUser(Parameter.readString("FINANCE_DIVISION_HEAD")).getFile().getStFilePath());
            Image img = null;
            if (approvedKadiv != null) {
                img = Image.getInstance(pemasaran.getUser(approvedKadiv).getFile().getStFilePath());
            } else {
                img = Image.getInstance(pemasaran.getUser(Parameter.readString("PROPOSAL_KEU_KADIV")).getFile().getStFilePath());
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

            String created4 = pemasaran.getUser(kadiv).getStUserName().toUpperCase() + "\n"
                    + "Kepala Divisi";
            PdfPCell footer3 = new PdfPCell(new Phrase(created4, smallb));
            footer3.setBorder(Rectangle.NO_BORDER);
            footer3.setHorizontalAlignment(Element.ALIGN_LEFT);
            footer3.setColspan(5);
            my_report_table3.addCell(footer3);

            //Create QR Code by using BarcodeQRCode Class
            BarcodeQRCode my_code = new BarcodeQRCode(DateUtil.getDateStr(currentdate, "dd-MMM-yyyy HH:mm:ss") + "_" + pemasaran.getStNoSPP() + "_" + JSPUtil.printX(pemasaran.getDbSaldoBiaya(), 0), 3, 3, null);
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

        } finally {
            conn.close();
        }
    }

    final boolean autoEmail = Parameter.readBoolean("SYS_SEND_EMAIL", 1, false);

    public void doApprovedPmsNew() throws Exception {
        final SQLUtil S = new SQLUtil();

//        if (pemasaran.isPosted()) {
//            throw new RuntimeException("Transaksi bulan " + pemasaran.getStMonths() + " dan tahun " + pemasaran.getStYears() + " tsb sudah diposting");
//        }
        String keuKabagParams = Parameter.readString("PROPOSAL_KEU_KABAG");
        String keuKadivParams = Parameter.readString("PROPOSAL_KEU_KADIV");

        if (isApprovalCab()) {
            String sql = null;
            if (pemasaran.isNonTunai()) {
                sql = "update biaya_pemasaran set status1 = 'Y',status2 = 'Y',status3 = 'Y',status4 = 'Y',kabag_approved = '" + keuKabagParams + "',kadiv_approved = '" + keuKadivParams + "',approved_date = '" + new Date() + "' where pms_id = ?";
            } else {
                sql = "update biaya_pemasaran set status1 = 'Y' where pms_id = ?";
            }

            try {
                PreparedStatement PS = S.setQuery(sql);

                PS.setObject(1, pemasaran.getStPemasaranID());

                int p = PS.executeUpdate();

            } finally {
                S.release();
            }

            if (pemasaran.isNonTunai()) {
                if (!Tools.isEqual(DateUtil.getMonth2Digit(new Date()), pemasaran.getStMonths())) {
                    final DTOList details = pemasaran.getPmsDetail();
                    getRemoteGeneralLedger().approvePmsAcrual(pemasaran, details);
                }
            }

            if (autoEmail) {
                if (pemasaran.isNonTunai()) {
                    sendEmailProposalBiaya();
                    sendEmailApprovalBiaya();
                } else {
                    sendEmailProposalBiaya();
                }
            }
        }

        if (isApprovalSie()) {
            String keuKasieParams = Parameter.readString("PROPOSAL_KEU_KASIE");
            String keuKasie = SessionManager.getInstance().getSession().getStUserID();

            if (!keuKasieParams.equalsIgnoreCase(keuKasie)) {
                throw new RuntimeException("ID Login tidak sesuai NIRP Kasie Berwenang");
            }

            try {
                PreparedStatement PS = S.setQuery("update biaya_pemasaran set status2 = 'Y' where pms_id = ?");

                PS.setObject(1, pemasaran.getStPemasaranID());

                int p = PS.executeUpdate();

            } finally {
                S.release();
            }
        }

        if (isApprovalBag()) {

//            String keuKabagParams = Parameter.readString("PROPOSAL_KEU_KABAG");
            String keuKabag = SessionManager.getInstance().getSession().getStUserID();

            if (!keuKabagParams.equalsIgnoreCase(keuKabag)) {
                throw new RuntimeException("ID Login tidak sesuai NIRP Kabag Berwenang");
            }

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

//            String keuKadivParams = Parameter.readString("PROPOSAL_KEU_KADIV");
            String keuKadiv = SessionManager.getInstance().getSession().getStUserID();

            if (!keuKadivParams.equalsIgnoreCase(keuKadiv)) {
                throw new RuntimeException("ID Login tidak sesuai NIRP Kadiv Berwenang");
            }

            try {
                PreparedStatement PS = S.setQuery("update biaya_pemasaran set status4 = 'Y',kadiv_approved = ?,approved_date = ? where pms_id = ?");

                PS.setObject(1, SessionManager.getInstance().getSession().getStUserID());
                PS.setObject(2, new Date());
                PS.setObject(3, pemasaran.getStPemasaranID());

                int p = PS.executeUpdate();

            } finally {
                S.release();
            }

            if (!Tools.isEqual(DateUtil.getMonth2Digit(pemasaran.getDtCreateDate()), DateUtil.getMonth2Digit(pemasaran.getDtEntryDate()))) {
                final DTOList details = pemasaran.getPmsDetail();
                getRemoteGeneralLedger().approvePmsAcrual(pemasaran, details);
            }

            if (autoEmail) {
                sendEmailApprovalBiaya();
//                sendEmailApprovalBiayaNoEmail();
            }
        }

        close();
    }

    public void doBayarPmsNew() throws Exception {

        DateTime currentDateLastDay = new DateTime(pemasaran.getDtEntryDate());
        currentDateLastDay = currentDateLastDay.dayOfMonth().withMaximumValue();
        int batasMaxHari = 5;
        DateTime maximumBackDate = new DateTime();
        maximumBackDate = currentDateLastDay.plusDays(batasMaxHari);

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();
        Date todayWithZeroTime = formatter.parse(formatter.format(today));
        DateTime currentDate = new DateTime(todayWithZeroTime);

        logger.logDebug("@@@@@@@@@@@@@@@@@1 " + currentDateLastDay);
        logger.logDebug("@@@@@@@@@@@@@@@@@2 " + currentDate);
        logger.logDebug("@@@@@@@@@@@@@@@@@3 " + maximumBackDate);

        boolean isHO = SessionManager.getInstance().getSession().getStBranch() == null;
        if (!isHO) {
            if (!Tools.isEqual(DateUtil.getMonth2Digit(pemasaran.getDtEntryDate()), DateUtil.getMonth2Digit(new Date()))) {
                if (currentDate.isAfter(maximumBackDate)) {
                    throw new RuntimeException("Tidak bisa klik Bayar, sudah melebihi batas waktu 5 hari ");
                }
            }
        }

        pemasaran.setDtTanggalBayar(new Date());

        final DTOList details = pemasaran.getPmsDetail();
        if (Tools.isEqual(DateUtil.getMonth2Digit(pemasaran.getDtCreateDate()), DateUtil.getMonth2Digit(pemasaran.getDtEntryDate()))) {
            if (!isHO) {
                if (!Tools.isEqual(DateUtil.getMonth2Digit(pemasaran.getDtEntryDate()), DateUtil.getMonth2Digit(new Date()))) {
                    throw new RuntimeException("Tidak bisa klik Bayar, melebihi bulan transaksi ");
                }
            }
            getRemoteGeneralLedger().approvePmsBayar(pemasaran, details);
        } else {
            getRemoteGeneralLedger().approvePmsAcrualBayar(pemasaran, details);
        }

        close();
    }

    public BigDecimal getAnggaranGoalset(String bulan, String tahun, String koda) throws Exception {

        int bulanNow = Integer.parseInt(bulan);
        int tahunNow = Integer.parseInt(tahun);
        int bulanCode = 0;
        int tahunCode = 0;
        String beforeMonth = null;

        if (bulanNow == 1) {
//            beforeMonth = "12";
            bulanCode = 12;
            tahunCode = tahunNow - 1;
        } else if (bulanNow > 1) {
            bulanCode = bulanNow - 1;
//            if (bulanCode < 10) {
//                beforeMonth = "0" + bulanCode;
//            } else {
//                beforeMonth = String.valueOf(bulanCode);
//            }

            beforeMonth = String.valueOf(bulanCode);
            tahunCode = tahunNow;
        }

        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "select bln" + bulanCode
                    + " from ins_statistic_target_perbulan a "
                    + " where a.jenis = 'kredit' and a.cc_code = ? and a.years = ? ");

            S.setParam(1, koda);
            S.setParam(2, Integer.toString(tahunCode));

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }
    private BigDecimal dbProduksiGs;
    private BigDecimal dbProdAskred;

    /**
     * @return the dbProduksiGs
     */
    public BigDecimal getDbProduksiGs() {
        return dbProduksiGs;
    }

    /**
     * @param dbProduksiGs the dbProduksiGs to set
     */
    public void setDbProduksiGs(BigDecimal dbProduksiGs) {
        this.dbProduksiGs = dbProduksiGs;
    }

    public void doSavePmsNew() throws Exception {

        recalculatePmsNew();

        if (getPmsDetail().size() == 0) {
            throw new RuntimeException("Anggaran belum diinput");
        }

        if (pemasaran.getStFileNota() == null) {
            throw new RuntimeException("File Nota Belum di Upload");
        }

//        boolean adaAkunKosong = false;
//        String listAkunKosong = "";
////        if (validateNoSuratPemasaran(pemasaran.getStKeterangan(), pemasaran.getStPemasaranID()) != null) {
//        if (validateNoSuratPemasaran(pemasaran) != null) {
//            adaAkunKosong = true;
//            listAkunKosong = listAkunKosong + "<br>" + pemasaran.getStKeterangan();
//        }
//
//        logger.logDebug("@@@@@@@@@@@@@@@ " + adaAkunKosong);
//
//        if (adaAkunKosong) {
//            throw new RuntimeException("No. Surat " + listAkunKosong + " Sudah Terinput. ");
//        }

        getRemoteGeneralLedger().savePemasaran(pemasaran);

        super.close();

    }

    public void approvalPms1New(String ardepoid) throws Exception {
//        dataProposalComm(titipanID);
        setPemasaranID("rkappem");

        pemasaran = getRemoteGeneralLedger().loadPemasaran(ardepoid);

        if (!pemasaran.isValidasiFlag()) {
            throw new Exception("Data Belum divalidasi Marketing");
        }

        if (pemasaran.isStatus1Flag()) {
            throw new Exception("Data Sudah disetujui Cabang");
        }

        super.setReadOnly(true);

        approvalCab = true;

    }

    /**
     * @return the dbProdAskred
     */
    public BigDecimal getDbProdAskred() {
        return dbProdAskred;
    }

    /**
     * @param dbProdAskred the dbProdAskred to set
     */
    public void setDbProdAskred(BigDecimal dbProdAskred) {
        this.dbProdAskred = dbProdAskred;
    }

    public BigDecimal getTotalRealisasi(String bulan, String tahun, String koda) throws Exception {
        final SQLUtil S = new SQLUtil();

        //khusus 1%
        String sql = "select coalesce(sum(total_biaya),0) "
                + " from biaya_pemasaran a "
                + "where a.fileapp is null and a.delete_f is null and a.months = ? and a.years = ? and a.cc_code = ? ";

        if (pemasaran.getStPemasaranID() != null) {
            sql = sql + " and a.pms_id <> ? ";
        }

        try {
            S.setQuery(sql);

            S.setParam(1, bulan);
            S.setParam(2, tahun);
            S.setParam(3, koda);

            if (pemasaran.getStPemasaranID() != null) {
                S.setParam(4, pemasaran.getStPemasaranID());
            }

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    /**
     * @return the validasiMode
     */
    public boolean isValidasiMode() {
        return validasiMode;
    }

    /**
     * @param validasiMode the validasiMode to set
     */
    public void setValidasiMode(boolean validasiMode) {
        this.validasiMode = validasiMode;
    }

    public void doValidasiNew() throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            PreparedStatement PS = S.setQuery("update biaya_pemasaran set validasi_f = 'Y' where pms_id = ?");

            PS.setObject(1, pemasaran.getStPemasaranID());

            int p = PS.executeUpdate();

        } finally {
            S.release();
        }

        close();
    }

    public void setDate2() throws Exception {

        if (pemasaran.isPosted()) {
            String tahun = pemasaran.getStYears();
            pemasaran.setStYears(null);
            throw new RuntimeException("Transaksi bulan " + pemasaran.getStMonths() + " dan tahun " + tahun + " tsb sudah diposting");
        }

        recalculatePmsNew();
    }

    public BigDecimal getTotalAskredCabPms(String bulan, String tahun, String koda) throws Exception {

        int bulanNow = Integer.parseInt(bulan);
        int tahunNow = Integer.parseInt(tahun);
        int bulanCode = 0;
        int tahunCode = 0;
        String beforeMonth = null;

        if (bulanNow == 1) {
            beforeMonth = "12";
            tahunCode = tahunNow - 1;
        } else if (bulanNow > 1) {
            bulanCode = bulanNow - 1;
            if (bulanCode < 10) {
                beforeMonth = "0" + bulanCode;
            } else {
                beforeMonth = String.valueOf(bulanCode);
            }

            tahunCode = tahunNow;
        }

        final SQLUtil S = new SQLUtil();

        String cabang = null;
        if (koda.equalsIgnoreCase("15") || koda.equalsIgnoreCase("18")) {
            cabang = "'15','18'";
        } else if (koda.equalsIgnoreCase("13") || koda.equalsIgnoreCase("19")) {
            cabang = "'13','19'";
        } else if (koda.equalsIgnoreCase("40") || koda.equalsIgnoreCase("45")) {
            cabang = "'40','45'";
        } else if (koda.equalsIgnoreCase("60") || koda.equalsIgnoreCase("61")) {
            cabang = "'60','61'";
        } else {
            cabang = "'" + koda + "'";
        }

        try {
            /*S.setQuery(" select sum(a.premi-a.diskon) as biaya from (  select a.cc_code,"
                    + "sum(getpremiend(d.entity_id,coalesce(a.premi_total*a.ccy_rate,0),d.premi_amt*a.ccy_rate*-1)) as premi, "
                    + "sum(getpremiend(d.entity_id,coalesce(a.nd_disc1*a.ccy_rate,0),((coalesce(d.disc_amount,0))*a.ccy_rate)*-1)) as diskon   "
                    + "from ins_policies a "
                    + "inner join ins_pol_coins d on d.policy_id = a.pol_id "
                    + "inner join s_region c on c.region_id = a.region_id "
                    + "where a.status in ('POLICY','ENDORSE','RENEWAL') and a.active_flag = 'Y' and a.effective_flag = 'Y' "
                    + "and (d.entity_id <> 1 or d.coins_type <> 'COINS_COVER') and a.pol_type_id in (59,87,88) "
                    //                    + "and a.cc_code in (" + cabang + ") and c.region_code <> '99' "
                    + "and a.cc_code in (" + cabang + ") "
                    + "and substr(a.policy_date::text,6,2) = '" + beforeMonth + "' "
                    + "and substr(a.policy_date::text,1,4) = '" + Integer.toString(tahunCode) + "' "
                    + "group by a.cc_code ) a  ");*/

            S.setQuery(" select sum(premi) from ("
                    + "select cc_code,premi from ins_rekap_produksi_det "
                    + "where pol_type_id in (59,87,88) and tahun = '" + Integer.toString(tahunCode) + "' and bulan = '" + beforeMonth + "' and cc_code in (" + cabang + ")"
                    + ") a");

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public BigDecimal getProdAskredCabang(String bulan, String tahun, String koda) throws Exception {

        int bulanNow = Integer.parseInt(bulan);
        int tahunNow = Integer.parseInt(tahun);
        int bulanCode = 0;
        int tahunCode = 0;
        String beforeMonth = null;

        if (bulanNow == 1) {
            beforeMonth = "12";
            tahunCode = tahunNow - 1;
        } else if (bulanNow > 1) {
            bulanCode = bulanNow - 1;
            if (bulanCode < 10) {
                beforeMonth = "0" + bulanCode;
            } else {
                beforeMonth = String.valueOf(bulanCode);
            }

            tahunCode = tahunNow;
        }

        final SQLUtil S = new SQLUtil();

        try {
            /*S.setQuery(" select sum(a.premi-a.diskon) as biaya from (  select a.cc_code,"
                    + "sum(getpremiend(d.entity_id,coalesce(a.premi_total*a.ccy_rate,0),d.premi_amt*a.ccy_rate*-1)) as premi, "
                    + "sum(getpremiend(d.entity_id,coalesce(a.nd_disc1*a.ccy_rate,0),((coalesce(d.disc_amount,0))*a.ccy_rate)*-1)) as diskon   "
                    + "from ins_policies a "
                    + "inner join ins_pol_coins d on d.policy_id = a.pol_id  "
                    + "inner join s_region c on c.region_id = a.region_id "
                    + "where a.status in ('POLICY','ENDORSE','RENEWAL') and a.active_flag = 'Y' and a.effective_flag = 'Y' "
                    + "and (d.entity_id <> 1 or d.coins_type <> 'COINS_COVER') and a.pol_type_id in (59,87,88) "
                    + "and substr(a.policy_date::text,6,2) = '" + beforeMonth + "' "
                    + "and substr(a.policy_date::text,1,4) = '" + Integer.toString(tahunCode) + "' "
                    //                    + "and a.cc_code = '" + koda + "' and c.region_code <> '99' "
                    + "and a.cc_code = '" + koda + "' "
                    + "group by a.cc_code ) a  ");*/

            S.setQuery(" select sum(premi) from ("
                    + "select cc_code,premi from ins_rekap_produksi_det "
                    + "where pol_type_id in (59,87,88) and tahun = '" + Integer.toString(tahunCode) + "' and bulan = '" + beforeMonth + "' and cc_code in ('" + koda + "')"
                    + ") a");

            final ResultSet RS = S.executeQuery();

            if (RS.next()) {
                return RS.getBigDecimal(1);
            }

            return null;
        } finally {

            S.release();
        }
    }

    public void editPemasaranNota(String ardepoid) throws Exception {
        viewPemasaran(ardepoid);

        setReadOnly(false);

        pemasaran.markUpdate();
        pemasaran.getDocuments().markAllUpdate();

        setTitle("EDIT BIAYA PEMASARAN");
    }

    /**
     * @return the notaMode
     */
    public boolean isNotaMode() {
        return notaMode;
    }

    /**
     * @param notaMode the notaMode to set
     */
    public void setNotaMode(boolean notaMode) {
        this.notaMode = notaMode;
    }

    public void doSaveNota() throws Exception {

        getRemoteGeneralLedger().savePemasaran(pemasaran);

        super.close();

    }
}
