/***********************************************************************
 * Module:  com.webfin.gl.form.InsuranceUploadForm
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:34 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.reinsurance.form;

import com.crux.file.FileManager;
import com.crux.file.FileView;
import com.crux.pool.DTOPool;
import com.crux.web.form.Form;
import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.ListUtil;
import com.crux.util.LogManager;
import com.crux.util.SQLUtil;
import com.crux.web.controller.SessionManager;
import com.webfin.ar.ejb.AccountReceivableHome;
import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.model.JournalView;

import com.webfin.insurance.ejb.*;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.reinsurance.model.ReinsuranceValidationDetailView;
import com.webfin.reinsurance.model.ReinsuranceValidationHeaderView;
import java.io.FileInputStream;
import java.math.BigDecimal;

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

public class ReinsuranceValidationUploadForm extends Form {

    //private final static transient LogManager logger = LogManager.getInstance(JournalView.class);
    private final static transient LogManager logger = LogManager.getInstance(ReinsuranceValidationUploadForm.class);
    private ReinsuranceValidationHeaderView titipan;
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

    public ReinsuranceValidationHeaderView getTitipan() {
        return titipan;
    }

    public void setReceipt(ReinsuranceValidationHeaderView titipan) {
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

    public ReinsuranceValidationUploadForm() {
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

        titipan = new ReinsuranceValidationHeaderView();

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

        final DTOList details = getRemoteInsurance().getReinsuranceValidationDetail(titipanID);

        titipan = (ReinsuranceValidationHeaderView) DTOPool.getInstance().getDTO(ReinsuranceValidationHeaderView.class, titipanID);

        titipan.setDetails(details);

        super.setReadOnly(true);

        viewMode = true;
    }

    public void doSave() throws Exception {

        recalculate();

        //validate();

        getRemoteInsurance().saveReinsuranceValidation(titipan, titipan.getDetails());

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
        titipan.setDtApproveDate(new Date());
        titipan.setStApprovedWho(UserManager.getInstance().getUser().getStUserID());

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
        titipan = new ReinsuranceValidationHeaderView();

        titipan.markNew();

        final DTOList details = new DTOList();

        titipan.setDetails(details);

        titipan.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());

        setTitle("Upload Validasi Reasuransi");

        editMode = true;
    }

    public void uploadExcel() throws Exception {


        String fileID = titipan.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("UPLOAD_VALIDASI");

        SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");

        if(sheet==null)
            throw new RuntimeException("Sheet UPLOAD_VALIDASI tidak Ditemukan, download format excel pada menu Dokumen");

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
            HSSFCell cellStatus = row.getCell(2);
            
            String noPolis = cellNoPolis.getStringCellValue().trim();

            //cari polis nya
            DTOList listPolicy = null;

            listPolicy = ListUtil.getDTOListFromQuery(
                " SELECT * FROM ins_policy where status = '"+ cellStatus +"' and pol_no = '"+ noPolis +"'"+
                " order by POL_ID",
                InsurancePolicyView.class);

            if(listPolicy==null)
                throw new RuntimeException("No polis "+ noPolis +" tidak ditemukan");

            InsurancePolicyView polis = (InsurancePolicyView) listPolicy.get(0);

            //cek jika sudah disetujui
            if(polis.isEffective())
                throw new RuntimeException("No polis "+ noPolis +" sudah disetujui");

            //cek jika belum offrisk
            /*
            DateTime periodEnd = new DateTime(polis.getDtPeriodEnd());
            DateTime tanggalProduksi = new DateTime(new Date());

            if(periodEnd.isAfter(tanggalProduksi)){
                throw new RuntimeException("No polis "+ noPolis +" belum offrisk");
            }
            */

            ReinsuranceValidationDetailView data = new ReinsuranceValidationDetailView();
            data.markNew();

            data.setStPolicyNo(cellNoPolis.getStringCellValue());

            if(cellStatus!=null)
                data.setStDescription(cellStatus.getStringCellValue());


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

        final DTOList details = getRemoteInsurance().getReinsuranceValidationDetail(titipanID);

        titipan = (ReinsuranceValidationHeaderView) DTOPool.getInstance().getDTO(ReinsuranceValidationHeaderView.class, titipanID);

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

        if(details.size()>100)
            throw new RuntimeException("Jumlah data maksimal 100 baris");

        /*
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

                
                if(!cek){
                    pesan =  pesan+ "- "+ det.getStPolicyNo()+" ["+det.getStOrderNo()+" "+ det.getStNama() +"] Data tidak sama : " + msg;
                }

                totalTSI = BDUtil.add(totalTSI, det.getDbTSI());
                totalPremi = BDUtil.add(totalPremi, det.getDbPremi());

                if(!pesan.equalsIgnoreCase(""))
                    pesan = pesan +"<br>";

        }

        */

        titipan.setDbTSITotal(totalTSI);
        titipan.setDbPremiTotal(totalPremi);

        if(!dataIsValid){
            throw new RuntimeException(pesan);
        }
    }


}
