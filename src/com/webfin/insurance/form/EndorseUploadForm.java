/***********************************************************************
 * Module:  com.webfin.gl.form.InsuranceUploadForm
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:34 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.insurance.form;

import com.crux.file.FileManager;
import com.crux.file.FileView;
import com.crux.pool.DTOPool;
import com.crux.util.BDUtil;
import com.crux.util.ConvertUtil;
import com.crux.web.form.Form;
import com.crux.util.DTOList;
import com.crux.util.DateUtil;
import com.crux.util.JNDIUtil;
import com.crux.util.ListUtil;
import com.crux.util.LogManager;
import com.crux.util.SQLUtil;
import com.crux.web.controller.SessionManager;
import com.webfin.ar.ejb.AccountReceivableHome;
import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.entity.model.EntityView;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.model.JournalView;

import com.webfin.insurance.ejb.*;
import com.webfin.insurance.model.DataTeksMasukView;
import com.webfin.insurance.model.InsurancePolicyObjDefaultView;
import com.webfin.insurance.model.InsurancePolicyObjectView;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.insurance.model.UploadEndorseFireHeaderView;
import com.webfin.insurance.model.UploadEndorseHeaderView;
import com.webfin.insurance.model.UploadHeaderView;
import com.webfin.insurance.model.uploadEndorseDetailView;
import com.webfin.insurance.model.uploadEndorseFireDetailView;
import com.webfin.insurance.model.uploadEndorsemenView;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.joda.time.DateTime;
import org.joda.time.Months;

public class EndorseUploadForm extends Form {

    //private final static transient LogManager logger = LogManager.getInstance(JournalView.class);
    private final static transient LogManager logger = LogManager.getInstance(InsuranceUploadForm.class);
    private UploadEndorseHeaderView titipan;
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

    private UploadEndorseFireHeaderView titipanFire;

    public UploadEndorseHeaderView getTitipan() {
        return titipan;
    }

    public void setReceipt(UploadEndorseHeaderView titipan) {
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

    public EndorseUploadForm() {
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

        titipan = new UploadEndorseHeaderView();

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

        final DTOList details = getRemoteInsurance().getUploadEndorseDetail(titipanID);

        titipan = (UploadEndorseHeaderView) DTOPool.getInstance().getDTO(UploadEndorseHeaderView.class, titipanID);

        titipan.setDetails(details);

        super.setReadOnly(true);

        viewMode = true;
    }

    public void doSave() throws Exception {

        recalculate();

        getRemoteInsurance().saveUploadEndorsePolis(titipan, titipan.getDetails());

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
        Edit(titipanID);

        titipan.setStEffectiveFlag("Y");

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
        titipan = new UploadEndorseHeaderView();

        titipan.markNew();

        final DTOList details = new DTOList();

        titipan.setDetails(details);

        titipan.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());

        setTitle("Upload Data Endorse Polis");

        editMode = true;
    }

    public void uploadExcel() throws Exception {


        String fileID = titipan.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("UPLOAD_ENDORSE");

        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");

        if(sheet==null)
            throw new RuntimeException("Sheet UPLOAD_ENDORSE tidak Ditemukan, download format excel pada menu Dokumen");

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

            String control = cellControl.getCellType() == cellControl.CELL_TYPE_STRING ? cellControl.getStringCellValue() : new BigDecimal(cellControl.getNumericCellValue()).toString();
            if (control.equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellNoPolis = row.getCell(1);
            HSSFCell cellFeebase = row.getCell(2);
            HSSFCell cellKomisi = row.getCell(3);
            HSSFCell cellBrokerFee = row.getCell(4);
            HSSFCell cellBiayaPolis = row.getCell(5);
            HSSFCell cellMaterai = row.getCell(6);
            HSSFCell cellApproved = row.getCell(7);
            HSSFCell cellKeterangan = row.getCell(8);

            HSSFCell cellJenisEndorse = row.getCell(9);
            HSSFCell cellNoUrut = row.getCell(10);
            HSSFCell cellNama = row.getCell(11);

            HSSFCell cellTSIAwal = row.getCell(12);
            HSSFCell cellPremiAwal = row.getCell(13);
            HSSFCell cellTanggalLahir = row.getCell(14);
            HSSFCell cellPeriodeAwal = row.getCell(15);
            HSSFCell cellPeriodeAkhir = row.getCell(16);

            HSSFCell cellTSI = row.getCell(17);
            HSSFCell cellPremi = row.getCell(18);
            HSSFCell cellTglLunas = row.getCell(19);
            HSSFCell cellSisaJangkaWaktu = row.getCell(20);
            HSSFCell cellPctRestitusi = row.getCell(21);
            HSSFCell cellNoReferensi = row.getCell(22);

            uploadEndorseDetailView data = new uploadEndorseDetailView();
            data.markNew();

            data.setStPolicyNo(cellNoPolis.getStringCellValue());

            if(cellFeebase!=null)
                    data.setDbFeeBase(new BigDecimal(cellFeebase.getNumericCellValue()));

            if(cellKomisi!=null)
                    data.setDbKomisi(new BigDecimal(cellKomisi.getNumericCellValue()));

            if(cellBrokerFee!=null)
                    data.setDbBrokerFee(new BigDecimal(cellBrokerFee.getNumericCellValue()));

            if(cellBiayaPolis!=null)
                    data.setDbBiayaPolis(new BigDecimal(cellBiayaPolis.getNumericCellValue()));

            if(cellMaterai!=null)
                    data.setDbMaterai(new BigDecimal(cellMaterai.getNumericCellValue()));

            if(cellJenisEndorse==null)
                throw new RuntimeException("Kolom jenis endorse tidak boleh kosong");

            if(cellJenisEndorse!=null)
                data.setStJenisEndorse(cellJenisEndorse.getStringCellValue());


            if(cellApproved!=null)
                data.setStApprovedWho(cellApproved.getStringCellValue());

            //String keterangan
            String keterangan = cellKeterangan.getStringCellValue();
            keterangan = keterangan.replaceAll("<br>", "\n");
            data.setStEndorseNotes(keterangan);
            //data.setStEndorseNotes("ENDORSE \n"+" NIH");


            if (cellNoUrut.getCellType() == cellNoUrut.CELL_TYPE_STRING) {
                data.setStOrderNo(cellNoUrut.getStringCellValue()); //usia
            } else {
                data.setStOrderNo(ConvertUtil.removeTrailing(String.valueOf(cellNoUrut.getNumericCellValue()))); //usia
            }

            if(cellNama!=null)
                data.setStNama(cellNama.getStringCellValue());

            if(cellTSIAwal!=null)
                data.setDbTSIAwal(new BigDecimal(cellTSIAwal.getNumericCellValue()));
            
            if(cellPremiAwal!=null)
                data.setDbPremiAwal(new BigDecimal(cellPremiAwal.getNumericCellValue()));
            
            if(cellTanggalLahir!=null)
                if(!cellTanggalLahir.getStringCellValue().equalsIgnoreCase(""))
                    data.setDtTanggalLahir(df2.parse(cellTanggalLahir.getStringCellValue()));
            
            if(cellPeriodeAwal!=null)
                if(!cellPeriodeAwal.getStringCellValue().equalsIgnoreCase(""))
                    data.setDtPeriodeAwal(df2.parse(cellPeriodeAwal.getStringCellValue()));
            
            if(cellPeriodeAkhir!=null)
                if(!cellPeriodeAkhir.getStringCellValue().equalsIgnoreCase(""))
                    data.setDtPeriodeAkhir(df2.parse(cellPeriodeAkhir.getStringCellValue()));

            if(cellTSI!=null)
                data.setDbTSI(new BigDecimal(cellTSI.getNumericCellValue()));

            if(cellPremi!=null)
                data.setDbPremi(new BigDecimal(cellPremi.getNumericCellValue()));

            if(cellTglLunas!=null)
                if(!cellTglLunas.getStringCellValue().equalsIgnoreCase(""))
                    data.setDtRestitutionDate(df2.parse(cellTglLunas.getStringCellValue()));

            if(data.getStJenisEndorse()!=null){
                if(!data.isEndorseJangkaWaktu()){
                    if(cellSisaJangkaWaktu!=null)
                    {
                        if (cellSisaJangkaWaktu.getCellType() == cellNoUrut.CELL_TYPE_STRING) {
                             data.setStSisaJangkaWaktu(cellSisaJangkaWaktu.getStringCellValue()); //usia
                        } else {
                             data.setStSisaJangkaWaktu(ConvertUtil.removeTrailing(String.valueOf(cellSisaJangkaWaktu.getNumericCellValue()))); //usia
                        }
                    }
                }
            }
            

            if(cellPctRestitusi!=null)
                if(cellPctRestitusi.getNumericCellValue()!=0)
                    data.setDbRestitutionPct(new BigDecimal(cellPctRestitusi.getNumericCellValue()));

            if(cellNoReferensi!=null)
                data.setStReferenceNo(cellNoReferensi.getStringCellValue());

            details.add(data);

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

        final DTOList details = getRemoteInsurance().getUploadEndorseDetail(titipanID);

        titipan = (UploadEndorseHeaderView) DTOPool.getInstance().getDTO(UploadEndorseHeaderView.class, titipanID);

        if(titipan.isEffective())
            throw new Exception("Data tidak bisa diubah karena sudah disetujui");

        titipan.markUpdate();

        titipan.setDetails(details);

        titipan.getDetails().markAllUpdate();

    }

    public void recalculate() throws Exception{

        cekData();

    }

    public void onChangeBranch(){

    }

    public String getStatusKerja(String cabang, String pekerjaan) throws Exception {
        final SQLUtil S = new SQLUtil();

        try {
            S.setQuery(
                    "   select " +
                    "     ref2 " +
                    "   from " +
                    "         s_code_mapping " +
                    "   where" +
                    "      vs_group = 'WORKING_TYPE' and ref1 = ? and vs_code = ?");

            S.setParam(1,cabang);
            S.setParam(2,pekerjaan);

            final ResultSet RS = S.executeQuery();

            if (RS.next()){
                return RS.getString(1);
            }

            return null;
        }finally{
            S.release();
        }
    }

    public void delAll() throws Exception {
        titipan.getDetails().deleteAll();
    }

    public void validate() throws Exception{
        DTOList details = titipan.getDetails();

    }

    public void clickPrint() throws Exception {

        SessionManager.getInstance().getRequest().setAttribute("DATATEXT", titipan.getDetails());

        String urx="/pages/insurance/prodrpt/upload.fop";

        if (urx==null) throw new RuntimeException("Unable to find suitable print form");

        super.redirect(urx);

    }

    public void cekData() throws Exception{

        BigDecimal totalTSI = null;
        BigDecimal totalPremi = null;

        DTOList details = titipan.getDetails();

        boolean dataIsValid = true;
        
        String pesan = "";
        boolean cek = true;

        if(details.size()>500)
            throw new RuntimeException("Jumlah data maksimal 500 baris");

        for (int i = 0; i < details.size(); i++) {
            uploadEndorseDetailView det = (uploadEndorseDetailView) details.get(i);

            if(det.isBypassValidasi()) continue;

            //CARI OBJEK ENDORSEMEN TERAKHIR DARI POLIS TSB
                DTOList listObjek = ListUtil.getDTOListFromQuery(
                " SELECT b.*, "+
                " (select insured_amount from ins_pol_tsi x where x.ins_pol_obj_id = b.ins_pol_obj_id) as total_tsi,"+
                " ( select "+
                " a.amount_settled::character varying||a.receipt_date::character varying"+
                " from "+
                " ar_invoice   a"+
                " where "+
                " coalesce(a.cancel_flag,'N') <> 'Y' and posted_flag = 'Y' and ar_trx_type_id in (5,6,7) and attr_pol_no = ? limit 1) as payment"+
                " FROM INS_POLICY a "+
                " inner join ins_pol_obj b on a.pol_id = b.pol_id "+
                " WHERE STATUS IN ('POLICY','RENEWAL','ENDORSE')  "+
                " AND ACTIVE_FLAG = 'Y' AND SUBSTR(POL_NO,0,17) = ? and cc_code = ?"+
                " and order_no = ? and btrim(upper(b.ref1)) = ? "+
                " ORDER BY POL_ID DESC LIMIT 1",
                new Object[]{det.getStPolicyNo().substring(0, 16)+"00", det.getStPolicyNo().substring(0, 16), titipan.getStCostCenterCode(), det.getStOrderNo(), det.getStNama().trim().toUpperCase()},
                InsurancePolicyObjDefaultView.class);

                InsurancePolicyObjectView obj = (InsurancePolicyObjectView) listObjek.get(0);

                InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) obj;

                //compare data upload dengan data real

                String msg = "";
                cek = true;

                if(objx==null){
                    dataIsValid = false;
                    cek = false;
                    msg = msg + " [Data Tidak Ada]";
                }

                if(objx!=null){

                    //jika endorse restitusi
                    if(!det.isEndorseJangkaWaktu()){
                        boolean sudahBayar = false;

                        if(objx.getStPayment()!=null){
                            sudahBayar = true;
                        }

                        if(!sudahBayar){
                            dataIsValid = false;
                            cek = false;
                            msg = msg + " [Belum Bayar]";
                        }

                        if(objx.getDtReference5()!=null){
                            dataIsValid = false;
                            cek = false;
                            msg = msg + " [Sudah Restitusi]";
                        }

                        if(!BDUtil.isZero(det.getDbTSI())){
                            dataIsValid = false;
                            cek = false;
                            msg = msg + " [TSI Tidak Nol]";
                        }

                        if(objx.isVoid()){
                            dataIsValid = false;
                            cek = false;
                            msg = msg + " [Sudah Klaim]";
                        }

                        //compare nama
                        if(!det.getStNama().trim().toUpperCase().equalsIgnoreCase(objx.getStReference1().trim().toUpperCase())){
                            dataIsValid = false;
                            cek = false;
                            msg = msg + " [Nama]";
                        }

                        //compare tgl lahir
                        if(!det.getDtTanggalLahir().equals(objx.getDtReference1())){
                            dataIsValid = false;
                            cek = false;
                            msg = msg + " [Tanggal Lahir]";
                        }

                        //compare tgl awal
                        if(!det.getDtPeriodeAwal().equals(objx.getDtReference2())){
                            dataIsValid = false;
                            cek = false;
                            msg = msg + " [Tanggal Awal]";
                        }

                        //compare tgl akhir
                        if(!det.getDtPeriodeAkhir().equals(objx.getDtReference3())){
                            dataIsValid = false;
                            cek = false;
                            msg = msg + " [Tanggal Akhir]";
                        }

                        //compare tsi
                        if(!BDUtil.isEqual(det.getDbTSIAwal(), objx.getDbTotalTSI(), 2)){
                            dataIsValid = false;
                            cek = false;
                            msg = msg + " [TSI]";
                        }
                    }

                    if(det.isEndorseJangkaWaktu()){
                        boolean sudahBayar = false;

                        if(objx.getStPayment()!=null){
                            sudahBayar = true;
                        }

                        if(!sudahBayar){
                            dataIsValid = false;
                            cek = false;
                            msg = msg + " [Belum Bayar]";
                        }

                        if(objx.getDtReference5()!=null){
                            dataIsValid = false;
                            cek = false;
                            msg = msg + " [Sudah Restitusi]";
                        }

                        if(objx.isVoid()){
                            dataIsValid = false;
                            cek = false;
                            msg = msg + " [Sudah Klaim]";
                        }

                        //compare nama
                        if(!det.getStNama().trim().toUpperCase().equalsIgnoreCase(objx.getStReference1().trim().toUpperCase())){
                            dataIsValid = false;
                            cek = false;
                            msg = msg + " [Nama]";
                        }

                        //compare tgl lahir
                        if(!det.getDtTanggalLahir().equals(objx.getDtReference1())){
                            dataIsValid = false;
                            cek = false;
                            msg = msg + " [Tanggal Lahir]";
                        }

                        //compare tsi
                        if(!BDUtil.isEqual(det.getDbTSIAwal(), objx.getDbTotalTSI(), 2)){
                            dataIsValid = false;
                            cek = false;
                            msg = msg + " [TSI]";
                        }
                    }
                    
                }

                //CEK KLAIM
                //CARI OBJEK KLAIM DARI POLIS TSB
                DTOList listObjekKlaim = ListUtil.getDTOListFromQuery(
                " SELECT pol_no,void_flag,pla_no,a.dla_no as dla_no2,a.claim_status,b.*, "+
                " (select insured_amount from ins_pol_tsi x where x.ins_pol_obj_id = b.ins_pol_obj_id) as total_tsi,"+
                " ( select "+
                " a.amount_settled::character varying||a.receipt_date::character varying"+
                " from "+
                " ar_invoice   a"+
                " where "+
                " coalesce(a.cancel_flag,'N') <> 'Y' and posted_flag = 'Y' and ar_trx_type_id in (5,6,7) and attr_pol_no = ? limit 1) as payment"+
                " FROM INS_POLICY a "+
                " inner join ins_pol_obj b on a.pol_id = b.pol_id "+
                " WHERE STATUS IN ('CLAIM','CLAIM ENDORSE')  "+
                " AND ACTIVE_FLAG = 'Y' AND SUBSTR(POL_NO,0,17) = ? and cc_code = ?"+
                " and order_no = ? and btrim(upper(b.ref1)) = ? "+
                " ORDER BY POL_ID DESC LIMIT 1",
                new Object[]{det.getStPolicyNo().substring(0, 16)+"00", det.getStPolicyNo().substring(0, 16), titipan.getStCostCenterCode(), det.getStOrderNo(), det.getStNama().trim().toUpperCase()},
                InsurancePolicyObjDefaultView.class);

                InsurancePolicyObjectView objKlaim = (InsurancePolicyObjectView) listObjekKlaim.get(0);

                InsurancePolicyObjDefaultView objxKlaim = (InsurancePolicyObjDefaultView) objKlaim;

                if(objxKlaim!=null){

                    if(objxKlaim.getStPLANo()!=null || objxKlaim.getStDLANo2()!=null){

                            String noKlaim = objxKlaim.getStClaimStatus().equalsIgnoreCase("DLA")?objxKlaim.getStDLANo2():objxKlaim.getStPLANo();

                            dataIsValid = false;
                            cek = false;
                            msg = msg + " [Sudah Klaim "+ noKlaim +"]";
                    }
                }

                
                if(!cek){
                    pesan =  pesan+ "- "+ det.getStPolicyNo()+" ["+det.getStOrderNo()+" "+ det.getStNama() +"] Data tidak sama : " + msg;
                }

                totalTSI = BDUtil.add(totalTSI, det.getDbTSI());
                totalPremi = BDUtil.add(totalPremi, det.getDbPremi());

                if(!pesan.equalsIgnoreCase(""))
                    pesan = pesan +"<br>";

        }

        titipan.setDbTSITotal(totalTSI);
        titipan.setDbPremiTotal(totalPremi);

        if(!dataIsValid){
            throw new RuntimeException(pesan);
        }
    }

    public void createNewFire() throws Exception {
        titipanFire = new UploadEndorseFireHeaderView();

        titipanFire.markNew();

        final DTOList details = new DTOList();

        titipanFire.setDetails(details);

        titipanFire.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());

        setTitle("Upload Data Endorse Polis Fire");

        editMode = true;
    }

    public UploadEndorseFireHeaderView getTitipanFire() {
        return titipanFire;
    }

    public DTOList getDetailsFire() throws Exception {
        return titipanFire.getDetails();
    }

    public void delAllFire() throws Exception {
        titipanFire.getDetails().deleteAll();
    }

    public void uploadExcelFire() throws Exception {


        String fileID = titipanFire.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("ENDORSE");

        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");

        if(sheet==null)
            throw new RuntimeException("Sheet ENDORSE tidak Ditemukan, download format excel terbaru pada menu Dokumen");

        final DTOList details = new DTOList();

        titipanFire.setDetails(details);

        int rows = sheet.getPhysicalNumberOfRows();

        //for (int r = 12; r < rows; r++) {
        for (int r = 4; r < rows; r++) {
            HSSFRow row = sheet.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            String control = cellControl.getCellType() == cellControl.CELL_TYPE_STRING ? cellControl.getStringCellValue() : new BigDecimal(cellControl.getNumericCellValue()).toString();
            if (control.equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellNoPolis = row.getCell(1);
            HSSFCell cellFeebasePct = row.getCell(2);
            HSSFCell cellFeebase = row.getCell(3);
            HSSFCell cellKomisiPct = row.getCell(4);
            HSSFCell cellKomisi = row.getCell(5);
            HSSFCell cellBrokerFeePct = row.getCell(6);
            HSSFCell cellBrokerFee = row.getCell(7);
            HSSFCell cellBiayaPolis = row.getCell(8);
            HSSFCell cellMaterai = row.getCell(9);
            HSSFCell cellKeterangan = row.getCell(10);

            HSSFCell cellJenisEndorse = row.getCell(11);
            HSSFCell cellNoUrut = row.getCell(12);
            HSSFCell cellPenggunaan = row.getCell(13);

            HSSFCell cellTSIAwal = row.getCell(14);
            HSSFCell cellPremiAwal = row.getCell(15);
            HSSFCell cellPeriodeAwal = row.getCell(16);
            HSSFCell cellPeriodeAkhir = row.getCell(17);

            HSSFCell cellTSI = row.getCell(18);
            HSSFCell cellPremi = row.getCell(19);
            HSSFCell cellAlamat = row.getCell(20);

            uploadEndorseFireDetailView data = new uploadEndorseFireDetailView();
            data.markNew();

            data.setStPolicyNo(cellNoPolis.getStringCellValue());

            if(cellFeebasePct!=null)
                    data.setDbFeeBasePct(new BigDecimal(cellFeebasePct.getNumericCellValue()));

            if(cellFeebase!=null)
                    data.setDbFeeBase(new BigDecimal(cellFeebase.getNumericCellValue()));

            if(cellKomisiPct!=null)
                    data.setDbKomisiPct(new BigDecimal(cellKomisiPct.getNumericCellValue()));

            if(cellKomisi!=null)
                    data.setDbKomisi(new BigDecimal(cellKomisi.getNumericCellValue()));

            if(cellBrokerFeePct!=null)
                    data.setDbBrokerFeePct(new BigDecimal(cellBrokerFeePct.getNumericCellValue()));

            if(cellBrokerFee!=null)
                    data.setDbBrokerFee(new BigDecimal(cellBrokerFee.getNumericCellValue()));

            if(cellBiayaPolis!=null)
                    data.setDbBiayaPolis(new BigDecimal(cellBiayaPolis.getNumericCellValue()));

            if(cellMaterai!=null)
                    data.setDbMaterai(new BigDecimal(cellMaterai.getNumericCellValue()));

            if(cellJenisEndorse==null)
                throw new RuntimeException("Kolom jenis endorse tidak boleh kosong");

            if(cellJenisEndorse!=null)
                data.setStJenisEndorse(cellJenisEndorse.getStringCellValue());

            if(cellAlamat!=null)
                data.setStAlamatRisiko(cellAlamat.getStringCellValue());


            //if(cellApproved!=null)
                //data.setStApprovedWho(cellApproved.getStringCellValue());

            //String keterangan
            String keterangan = cellKeterangan.getStringCellValue();
            keterangan = keterangan.replaceAll("<br>", "\n");
            data.setStEndorseNotes(keterangan);
            //data.setStEndorseNotes("ENDORSE \n"+" NIH");


            if (cellNoUrut.getCellType() == cellNoUrut.CELL_TYPE_STRING) {
                data.setStOrderNo(cellNoUrut.getStringCellValue()); //usia
            } else {
                data.setStOrderNo(ConvertUtil.removeTrailing(String.valueOf(cellNoUrut.getNumericCellValue()))); //usia
            }

            if(cellPenggunaan!=null)
                data.setStNama(cellPenggunaan.getStringCellValue());

            if(cellTSIAwal!=null)
                data.setDbTSIAwal(new BigDecimal(cellTSIAwal.getNumericCellValue()));

            if(cellPremiAwal!=null)
                data.setDbPremiAwal(new BigDecimal(cellPremiAwal.getNumericCellValue()));

            if(cellPeriodeAwal!=null)
                if(!cellPeriodeAwal.getStringCellValue().equalsIgnoreCase(""))
                    data.setDtPeriodeAwal(df2.parse(cellPeriodeAwal.getStringCellValue()));

            if(cellPeriodeAkhir!=null)
                if(!cellPeriodeAkhir.getStringCellValue().equalsIgnoreCase(""))
                    data.setDtPeriodeAkhir(df2.parse(cellPeriodeAkhir.getStringCellValue()));

            if(cellTSI!=null)
                data.setDbTSI(new BigDecimal(cellTSI.getNumericCellValue()));

            if(cellPremi!=null)
                data.setDbPremi(new BigDecimal(cellPremi.getNumericCellValue()));


            details.add(data);

        }

        titipanFire.setDetails(details);

    }

    public void recalculateFire() throws Exception{

        BigDecimal totalTSI = null;
        BigDecimal totalPremi = null;

        DTOList details = titipanFire.getDetails();

        boolean dataIsValid = true;

        String pesan = "";
        boolean cek = true;

        if(details.size()>500)
            throw new RuntimeException("Jumlah data maksimal 500 baris");

        for (int i = 0; i < details.size(); i++) {
            uploadEndorseFireDetailView det = (uploadEndorseFireDetailView) details.get(i);

            if(det.isBypassValidasi()) continue;



                totalTSI = BDUtil.add(totalTSI, det.getDbTSI());
                totalPremi = BDUtil.add(totalPremi, det.getDbPremi());

                if(!pesan.equalsIgnoreCase(""))
                    pesan = pesan +"<br>";

        }

        titipanFire.setDbTSITotal(totalTSI);
        titipanFire.setDbPremiTotal(totalPremi);

        if(!dataIsValid){
            throw new RuntimeException(pesan);
        }
    }

    public void doSaveFire() throws Exception {

        recalculateFire();

        //validate();

        getRemoteInsurance().saveUploadEndorsePolisFire(titipanFire, titipanFire.getDetails());

        super.close();

    }

    public void EditFire(String titipanID) throws Exception {

        final DTOList details = getRemoteInsurance().getUploadEndorseFireDetail(titipanID);

        titipanFire = (UploadEndorseFireHeaderView) DTOPool.getInstance().getDTO(UploadEndorseFireHeaderView.class, titipanID);

        if(titipanFire.isEffective())
            throw new Exception("Data tidak bisa diubah karena sudah disetujui");

        titipanFire.markUpdate();

        titipanFire.setDetails(details);

        titipanFire.getDetails().markAllUpdate();

    }

    public void viewFire(String titipanID) throws Exception {

        final DTOList details = getRemoteInsurance().getUploadEndorseFireDetail(titipanID);

        titipanFire = (UploadEndorseFireHeaderView) DTOPool.getInstance().getDTO(UploadEndorseFireHeaderView.class, titipanID);

        titipanFire.setDetails(details);

        super.setReadOnly(true);

        viewMode = true;
    }

    public void approveFire(String titipanID) throws Exception {
        EditFire(titipanID);

        titipanFire.setStEffectiveFlag("Y");

        super.setReadOnly(true);
        approveMode = true;
    }

    public void doApproveFire() throws Exception {
        //titipan.setStPostedFlag("Y");
        doSaveFire();
    }

    public void delLineFire() throws Exception {
        titipanFire.getDetails().delete(Integer.parseInt(notesindex));
    }


}
