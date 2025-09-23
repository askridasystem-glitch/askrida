/***********************************************************************
 * Module:  com.webfin.ar.forms.titipanForm
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:34 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.forms;

import com.crux.file.FileManager;
import com.crux.file.FileView;
import com.crux.pool.DTOPool;
import com.crux.util.BDUtil;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.util.DTOList;
import com.crux.util.DateUtil;
import com.crux.util.JNDIUtil;
import com.crux.util.ListUtil;
import com.crux.util.LogManager;
import com.crux.util.SQLUtil;
import com.crux.util.Tools;
import com.webfin.ar.ejb.AccountReceivableHome;
import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.model.TitipanPremiHeaderView;
import com.webfin.gl.model.TitipanPremiReinsuranceHeaderView;
import com.webfin.gl.model.TitipanPremiReinsuranceView;
import com.webfin.gl.model.TitipanPremiView;

import com.webfin.insurance.ejb.*;
import com.webfin.insurance.model.InsurancePolicyTreatyView;
import com.webfin.insurance.model.InsurancePolicyView;
import java.io.FileInputStream;
import java.math.BigDecimal;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ARTitipanPremiForm extends Form {

   private TitipanPremiHeaderView titipan;
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

   private TitipanPremiReinsuranceHeaderView titipanReinsurance;

    public TitipanPremiReinsuranceHeaderView getTitipanReinsurance() {
        return titipanReinsurance;
    }

    public void setTitipanReinsurance(TitipanPremiReinsuranceHeaderView titipanReinsurance) {
        this.titipanReinsurance = titipanReinsurance;
    }

   
   
   private final static transient LogManager logger = LogManager.getInstance(ARTitipanPremiForm.class);

   public TitipanPremiHeaderView getTitipan() {
      return titipan;
   }

   public void setReceipt(TitipanPremiHeaderView titipan) {
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
   
   public void btnPrint()throws Exception{
   	  
   }
   
   public String getStReportType() {
      return stReportType;
   }

   public void setStReportType(String stReportType) {
      this.stReportType = stReportType;
   }
   
   public void tes()throws Exception{
   	   logger.logDebug("line= ");
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
      return ((AccountReceivableHome) JNDIUtil.getInstance().lookup("AccountReceivableEJB",AccountReceivableHome.class.getName()))
            .create();
   }
   
   private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB",InsuranceHome.class.getName()))
            .create();
   }

   private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB",GeneralLedgerHome.class.getName()))
        .create();
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

   public ARTitipanPremiForm() {
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
   
   public void createNew() throws Exception {
      titipan = new TitipanPremiHeaderView();
      titipan.markNew();

      final DTOList details = new DTOList();

      final TitipanPremiView  jv = new TitipanPremiView ();

      jv.markNew();

      details.add(jv);

      titipan.setDetails(details);

      titipan.setStCostCenter(SessionManager.getInstance().getSession().getStBranch());

      setTitle("Input Titipan Premi");

      editMode=true;
   }

   public void edit(String titipanID) throws Exception {
       logger.logDebug("++++++++++++++ titipanID : "+titipanID);
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



   public void masterEdit(String titipanID) throws Exception {


       final DTOList details = getRemoteGeneralLedger().getTitipanPremi(getTitipanPremi(titipanID).getStTransactionHeaderID());

       titipan = new TitipanPremiHeaderView ();

       titipan.markUpdate();

      titipan.setDetails(details);

      titipan.getDetails().markAllUpdate();

      TitipanPremiView titip = (TitipanPremiView) details.get(0);

      titipan.setStTransactionNo(titip.getStTransactionNo());
      titipan.setStCostCenter(titip.getStTransactionNo().substring(5,7));
      titipan.setStMonths(titip.getStMonths());
      titipan.setStYears(titip.getStYears());
      titipan.setStMethodCode(titip.getStTransactionNo().substring(0,1));
      titipan.setStHeaderAccountID(titip.getStHeaderAccountID());
      titipan.setStAccountNoMaster(titip.getStHeaderAccountNo());
      titipan.setStDescriptionMaster(titip.getStDescriptionMaster());
      

   }

   public void view(String titipanID) throws Exception {

      super.setReadOnly(true);

      viewMode = true;
   }

   public void doSave() throws Exception {

      validate(titipan, titipan.getDetails());

      getRemoteGeneralLedger().saveTitipanPremi(titipan, titipan.getDetails());

      super.close();

   }

   public void doRecalculate() throws Exception {
	 //titipan.recalculate();

       BigDecimal total = BDUtil.zero;

       final DTOList details = getDetails();
       for (int i = 0; i < details.size(); i++) {
           TitipanPremiView titip = (TitipanPremiView) details.get(i);

           titip.setStAccountIDMaster(titipan.getStAccountIDMaster());
           titip.setStAccountNoMaster(titipan.getStAccountNoMaster());
           titip.setStMonths(titipan.getStMonths());
           titip.setStCostCenter(titipan.getStCostCenter());

           total = BDUtil.add(total, titip.getDbAmount());
           
       }

       dbTotalTitipan = total;

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
   
   public void addLine(){
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

      final TitipanPremiView  jv = new TitipanPremiView ();

      jv.markNew();

      details.add(jv);

      notesindex=String.valueOf(titipan.getDetails().size()-1);


      
   }

   private DTOList detailsTitipan;

   public DTOList getDetailsTitipan(String titipanID) {
       logger.logWarning("++++++++++++++ titipan id : "+ titipanID);
        loadTreaties(titipanID);
        return detailsTitipan;
    }

   
    private void loadTreaties(String titipanID) {
        try {
            if (detailsTitipan == null) {

                detailsTitipan = ListUtil.getDTOListFromQuery(
                        "select * from ar_titipan_premi where trx_no = ?",
                        new Object[]{getTitipanPremi(titipanID).getStTransactionNo()},
                        TitipanPremiView.class
                        );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TitipanPremiView getTitipanPremi(String stArTitipanID) {
        if (stArTitipanID==null) return null;
        return (TitipanPremiView) DTOPool.getInstance().getDTO(TitipanPremiView.class, stArTitipanID);
    }

    public void createNewUpload() throws Exception {
      titipan = new TitipanPremiHeaderView();
      titipan.markNew();

      final DTOList details = new DTOList();

      //final TitipanPremiView  jv = new TitipanPremiView ();

      //jv.markNew();

      //details.add(jv);

      titipan.setDetails(details);

      titipan.setStCostCenter(SessionManager.getInstance().getSession().getStBranch());

      setTitle("Upload Titipan Premi");

      editMode=true;
   }

    public void uploadExcel() throws Exception{
        String fileID = titipan.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("titipan");

        final DTOList details = new DTOList();

        titipan.setDetails(details);

        int rows  = sheet.getPhysicalNumberOfRows();

        int counter = 1;

        for (int r = 12; r < rows; r++){
            HSSFRow row   = sheet.getRow(r);

            HSSFCell cellControl  = row.getCell(0);
            if(cellControl==null) break;

            if(cellControl.getStringCellValue().equalsIgnoreCase("END")) break;
 
            HSSFCell cellCabang  = row.getCell(1);//cabang
            HSSFCell cellMetode  = row.getCell(2);//cabang
            HSSFCell cellBulan  = row.getCell(3);//cabang
            HSSFCell cellTahun  = row.getCell(4);//cabang
            HSSFCell cellAccountHeaderID  = row.getCell(5);//cabang
            HSSFCell cellAccountHeaderNo  = row.getCell(6);//cabang
            HSSFCell cellAccountHeaderName  = row.getCell(7);//cabang
            HSSFCell cellAccountID  = row.getCell(8);//cabang
            HSSFCell cellAccountNo  = row.getCell(9);//cabang
            HSSFCell cellAccountName  = row.getCell(10);//cabang
            //HSSFCell cellCounter  = row.getCell(11);//cabang
            HSSFCell cellDescription  = row.getCell(11);//cabang
            //HSSFCell cellCause  = row.getCell(13);//cabang
            HSSFCell cellTanggalTitipan  = row.getCell(12);//cabang
            HSSFCell cellJumlahTitipan  = row.getCell(13);//cabang

            HSSFCell cellNoPolis  = row.getCell(14);//no polis
            HSSFCell cellNoReferensi  = row.getCell(15);//no ref

            TitipanPremiView  jv = new TitipanPremiView ();
            jv.markNew();

            if(r==12){
                  titipan = getTitipan();

                  titipan.setStCostCenter(cellCabang.getCellType()==cellCabang.CELL_TYPE_STRING?cellCabang.getStringCellValue():new BigDecimal(cellCabang.getNumericCellValue()).toString());
                  titipan.setStMonths(cellBulan.getCellType()==cellBulan.CELL_TYPE_STRING?cellBulan.getStringCellValue():new BigDecimal(cellBulan.getNumericCellValue()).toString());
                  titipan.setStYears(cellTahun.getCellType()==cellTahun.CELL_TYPE_STRING?cellTahun.getStringCellValue():new BigDecimal(cellTahun.getNumericCellValue()).toString());
                  titipan.setStMethodCode(cellMetode.getCellType()==cellMetode.CELL_TYPE_STRING?cellMetode.getStringCellValue():new BigDecimal(cellMetode.getNumericCellValue()).toString());
                  titipan.setStHeaderAccountID(cellAccountHeaderID.getCellType()==cellAccountHeaderID.CELL_TYPE_STRING?cellAccountHeaderID.getStringCellValue():new BigDecimal(cellAccountHeaderID.getNumericCellValue()).toString());
                  titipan.setStAccountIDMaster(cellAccountHeaderID.getCellType()==cellAccountHeaderID.CELL_TYPE_STRING?cellAccountHeaderID.getStringCellValue():new BigDecimal(cellAccountHeaderID.getNumericCellValue()).toString());
                  titipan.setStAccountNoMaster(cellAccountHeaderNo.getCellType()==cellAccountHeaderNo.CELL_TYPE_STRING?cellAccountHeaderNo.getStringCellValue():new BigDecimal(cellAccountHeaderNo.getNumericCellValue()).toString());
                  titipan.setStDescriptionMaster(cellAccountHeaderName.getCellType()==cellAccountHeaderName.CELL_TYPE_STRING?cellAccountHeaderName.getStringCellValue():new BigDecimal(cellAccountHeaderName.getNumericCellValue()).toString());

            }

            jv.setStActiveFlag("Y");
            jv.setStCostCenter(cellCabang.getCellType()==cellCabang.CELL_TYPE_STRING?cellCabang.getStringCellValue():new BigDecimal(cellCabang.getNumericCellValue()).toString());
            jv.setStMethodCode(cellMetode.getCellType()==cellMetode.CELL_TYPE_STRING?cellMetode.getStringCellValue():new BigDecimal(cellMetode.getNumericCellValue()).toString());
            jv.setStMonths(cellBulan.getCellType()==cellBulan.CELL_TYPE_STRING?cellBulan.getStringCellValue():new BigDecimal(cellBulan.getNumericCellValue()).toString());
            jv.setStYears(cellTahun.getCellType()==cellTahun.CELL_TYPE_STRING?cellTahun.getStringCellValue():new BigDecimal(cellTahun.getNumericCellValue()).toString());
            jv.setStAccountIDMaster(cellAccountHeaderID.getCellType()==cellAccountHeaderID.CELL_TYPE_STRING?cellAccountHeaderID.getStringCellValue():new BigDecimal(cellAccountHeaderID.getNumericCellValue()).toString());
            jv.setStAccountNoMaster(cellAccountHeaderNo.getCellType()==cellAccountHeaderNo.CELL_TYPE_STRING?cellAccountHeaderNo.getStringCellValue():new BigDecimal(cellAccountHeaderNo.getNumericCellValue()).toString());
            jv.setStDescriptionMaster(cellAccountHeaderName.getCellType()==cellAccountHeaderName.CELL_TYPE_STRING?cellAccountHeaderName.getStringCellValue():new BigDecimal(cellAccountHeaderName.getNumericCellValue()).toString());
            jv.setStAccountID(cellAccountID.getCellType()==cellAccountID.CELL_TYPE_STRING?cellAccountID.getStringCellValue():new BigDecimal(cellAccountID.getNumericCellValue()).toString());
            jv.setStAccountNo(cellAccountNo.getCellType()==cellAccountNo.CELL_TYPE_STRING?cellAccountNo.getStringCellValue():new BigDecimal(cellAccountNo.getNumericCellValue()).toString());
            jv.setStDescription(cellDescription.getCellType()==cellDescription.CELL_TYPE_STRING?cellDescription.getStringCellValue():new BigDecimal(cellDescription.getNumericCellValue()).toString());
            jv.setStCause("3");
            jv.setStCounter(String.valueOf(counter++));
            jv.setDtApplyDate(cellTanggalTitipan.getDateCellValue());
            jv.setDbAmount(new BigDecimal(cellJumlahTitipan.getNumericCellValue()));
            jv.setDbBalance(jv.getDbAmount());

            if(cellNoPolis!=null){
                if(!cellNoPolis.getStringCellValue().equalsIgnoreCase("")){
                    jv.setStPolicyNo(cellNoPolis.getStringCellValue());
                }
            }

            if(cellNoPolis!=null){
                if(!cellNoPolis.getStringCellValue().equalsIgnoreCase("")){
                    if(cellNoPolis.getStringCellValue().substring(2, 4).equalsIgnoreCase("87")||cellNoPolis.getStringCellValue().substring(2, 4).equalsIgnoreCase("88")){
                        //CARI POLIS TSB
                        DTOList listPolis = null;
                        listPolis = ListUtil.getDTOListFromQuery(
                        " SELECT * "+
                        " FROM INS_POLICY "+
                        " WHERE STATUS IN ('POLICY','RENEWAL','ENDORSE') "+
                        " AND pol_no = ? "+
                        " ORDER BY POL_ID DESC LIMIT 1",
                        new Object[]{cellNoPolis.getStringCellValue().substring(0, 16)+"100"},
                        InsurancePolicyView.class);

                        InsurancePolicyView polis = (InsurancePolicyView)  listPolis.get(0);

                        if(polis!=null){
                            jv.setStPolicyID(polis.getStPolicyID());
                        }
                    }
                }
                
            }
            

            if(cellNoReferensi!=null){
                if(!cellNoReferensi.getStringCellValue().equalsIgnoreCase("")){
                    jv.setStReferenceNo(cellNoReferensi.getStringCellValue());
                }
            }
                

            details.add(jv);

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
    
    public void selectYear() throws Exception{
        if(isPosted()){
            String tahun = titipan.getStYears();
            titipan.setStYears(null);
            throw new RuntimeException("Transaksi bulan "+ titipan.getStMonths() +" dan tahun "+ tahun +" tsb sudah diposting");
        }
            
    }

    public boolean isPosted() throws Exception {

            SQLUtil S = new SQLUtil();

            boolean isPosted = false;

            try {
                String cek = "select gl_post_id from gl_posting where months = ? and years = ? and posted_flag = 'Y' and cc_code is null"+
                            " union "+
                            " select gl_post_id from gl_posting where months = ? and years = ? and posted_flag = 'Y'";

                if(titipan.getStCostCenter()!=null)
                    cek = cek + " and cc_code = ?";

                PreparedStatement PS = S.setQuery(cek);
                PS.setString(1, titipan.getStMonths());
                PS.setString(2, titipan.getStYears());

                PS.setString(3, titipan.getStMonths());
                PS.setString(4, titipan.getStYears());

                if(titipan.getStCostCenter()!=null)
                      PS.setString(5, titipan.getStCostCenter());

                ResultSet RS = PS.executeQuery();

                if (RS.next()){
                    isPosted = true;
                }

            } finally {
                S.release();
            }

        return isPosted;
    }

    public void cekData()throws Exception{
        validate(titipan, titipan.getDetails());
    }

    public void validate(TitipanPremiHeaderView jh, DTOList details) throws Exception{

        String glCode = jh.getStAccountNoMaster().substring(5,10);

          for (int i = 0; i < details.size(); i++) {
             TitipanPremiView  jv = (TitipanPremiView ) details.get(i);

             String glCodeDetail = jv.getAccount().getStAccountNo().substring(5,10);

             if (!Tools.isEqual(DateUtil.getMonth2Digit(jv.getDtApplyDate()),jh.getStMonths())) throw new Exception("Bulan tidak sama dengan Bulan Entry pd counter "+jv.getStCounter());
             if (!Tools.isEqual(DateUtil.getYear(jv.getDtApplyDate()),jh.getStYears())) throw new Exception("Tahun tidak sama dengan Tahun Entry pd counter "+jv.getStCounter());

             if(jv.getStPolicyNo()!=null)
                 if(jv.getStPolicyNo().length()<18)
                     throw new RuntimeException("Nomor Polis harus minimal 18 digit");

             if(!glCode.equalsIgnoreCase(glCodeDetail))
                 throw new RuntimeException("Kode akun bank ("+ jh.getStAccountNoMaster() +") dengan kode akun titipan ("+ jv.getAccount().getStAccountNo() +") tidak sama");

          }

          if(isPosted()){
              throw new Exception("Transaksi bulan "+ jh.getStMonths() +" Tahun "+ jh.getStYears()+ " Sudah diposting");
          }

    }

    public void createNewUploadReinsurance() throws Exception {
      titipanReinsurance = new TitipanPremiReinsuranceHeaderView();
      titipanReinsurance.markNew();

      final DTOList details = new DTOList();

      //final TitipanPremiView  jv = new TitipanPremiView ();

      //jv.markNew();

      //details.add(jv);

      titipanReinsurance.setDetails(details);

      titipanReinsurance.setStCostCenter(SessionManager.getInstance().getSession().getStBranch());

      setTitle("Upload Titipan Premi Reasuransi");

      editMode=true;
   }

    public void doRecalculateReinsurance() throws Exception {
	 //titipan.recalculate();

       BigDecimal total = BDUtil.zero;

       final DTOList details = getDetailsReinsurance();

       for (int i = 0; i < details.size(); i++) {
           TitipanPremiReinsuranceView titip = (TitipanPremiReinsuranceView) details.get(i);

           titip.setStAccountIDMaster(titipanReinsurance.getStAccountIDMaster());
           titip.setStAccountNoMaster(titipanReinsurance.getStAccountNoMaster());
           titip.setStMonths(titipanReinsurance.getStMonths());
           titip.setStCostCenter(titipanReinsurance.getStCostCenter());

           total = BDUtil.add(total, titip.getDbAmount());

       }

       dbTotalTitipan = total;

   }

    public void uploadExcelReinsurance() throws Exception{
        String fileID = titipanReinsurance.getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheet("titipan");

        final DTOList details = new DTOList();

        titipanReinsurance.setDetails(details);

        int rows  = sheet.getPhysicalNumberOfRows();

        int counter = 1;

        for (int r = 12; r < rows; r++){
            HSSFRow row   = sheet.getRow(r);

            HSSFCell cellControl  = row.getCell(0);
            if(cellControl==null) break;

            if(cellControl.getStringCellValue().equalsIgnoreCase("END")) break;

            HSSFCell cellCabang  = row.getCell(1);//cabang
            HSSFCell cellMetode  = row.getCell(2);//cabang
            HSSFCell cellBulan  = row.getCell(3);//cabang
            HSSFCell cellTahun  = row.getCell(4);//cabang
            HSSFCell cellAccountHeaderID  = row.getCell(5);//cabang
            HSSFCell cellAccountHeaderNo  = row.getCell(6);//cabang
            HSSFCell cellAccountHeaderName  = row.getCell(7);//cabang
            HSSFCell cellAccountID  = row.getCell(8);//cabang
            HSSFCell cellAccountNo  = row.getCell(9);//cabang
            HSSFCell cellAccountName  = row.getCell(10);//cabang
            //HSSFCell cellCounter  = row.getCell(11);//cabang
            HSSFCell cellDescription  = row.getCell(11);//cabang
            //HSSFCell cellCause  = row.getCell(13);//cabang
            HSSFCell cellTanggalTitipan  = row.getCell(12);//cabang
            HSSFCell cellJumlahTitipan  = row.getCell(13);//cabang

            TitipanPremiReinsuranceView  jv = new TitipanPremiReinsuranceView ();
            jv.markNew();

            if(r==12){
                  titipanReinsurance = getTitipanReinsurance();

                  titipanReinsurance.setStCostCenter(cellCabang.getCellType()==cellCabang.CELL_TYPE_STRING?cellCabang.getStringCellValue():new BigDecimal(cellCabang.getNumericCellValue()).toString());
                  titipanReinsurance.setStMonths(cellBulan.getCellType()==cellBulan.CELL_TYPE_STRING?cellBulan.getStringCellValue():new BigDecimal(cellBulan.getNumericCellValue()).toString());
                  titipanReinsurance.setStYears(cellTahun.getCellType()==cellTahun.CELL_TYPE_STRING?cellTahun.getStringCellValue():new BigDecimal(cellTahun.getNumericCellValue()).toString());
                  titipanReinsurance.setStMethodCode(cellMetode.getCellType()==cellMetode.CELL_TYPE_STRING?cellMetode.getStringCellValue():new BigDecimal(cellMetode.getNumericCellValue()).toString());
                  titipanReinsurance.setStHeaderAccountID(cellAccountHeaderID.getCellType()==cellAccountHeaderID.CELL_TYPE_STRING?cellAccountHeaderID.getStringCellValue():new BigDecimal(cellAccountHeaderID.getNumericCellValue()).toString());
                  titipanReinsurance.setStAccountIDMaster(cellAccountHeaderID.getCellType()==cellAccountHeaderID.CELL_TYPE_STRING?cellAccountHeaderID.getStringCellValue():new BigDecimal(cellAccountHeaderID.getNumericCellValue()).toString());
                  titipanReinsurance.setStAccountNoMaster(cellAccountHeaderNo.getCellType()==cellAccountHeaderNo.CELL_TYPE_STRING?cellAccountHeaderNo.getStringCellValue():new BigDecimal(cellAccountHeaderNo.getNumericCellValue()).toString());
                  titipanReinsurance.setStDescriptionMaster(cellAccountHeaderName.getCellType()==cellAccountHeaderName.CELL_TYPE_STRING?cellAccountHeaderName.getStringCellValue():new BigDecimal(cellAccountHeaderName.getNumericCellValue()).toString());

            }

            jv.setStActiveFlag("Y");
            jv.setStCostCenter(cellCabang.getCellType()==cellCabang.CELL_TYPE_STRING?cellCabang.getStringCellValue():new BigDecimal(cellCabang.getNumericCellValue()).toString());
            jv.setStMethodCode(cellMetode.getCellType()==cellMetode.CELL_TYPE_STRING?cellMetode.getStringCellValue():new BigDecimal(cellMetode.getNumericCellValue()).toString());
            jv.setStMonths(cellBulan.getCellType()==cellBulan.CELL_TYPE_STRING?cellBulan.getStringCellValue():new BigDecimal(cellBulan.getNumericCellValue()).toString());
            jv.setStYears(cellTahun.getCellType()==cellTahun.CELL_TYPE_STRING?cellTahun.getStringCellValue():new BigDecimal(cellTahun.getNumericCellValue()).toString());
            jv.setStAccountIDMaster(cellAccountHeaderID.getCellType()==cellAccountHeaderID.CELL_TYPE_STRING?cellAccountHeaderID.getStringCellValue():new BigDecimal(cellAccountHeaderID.getNumericCellValue()).toString());
            jv.setStAccountNoMaster(cellAccountHeaderNo.getCellType()==cellAccountHeaderNo.CELL_TYPE_STRING?cellAccountHeaderNo.getStringCellValue():new BigDecimal(cellAccountHeaderNo.getNumericCellValue()).toString());
            jv.setStDescriptionMaster(cellAccountHeaderName.getCellType()==cellAccountHeaderName.CELL_TYPE_STRING?cellAccountHeaderName.getStringCellValue():new BigDecimal(cellAccountHeaderName.getNumericCellValue()).toString());
            jv.setStAccountID(cellAccountID.getCellType()==cellAccountID.CELL_TYPE_STRING?cellAccountID.getStringCellValue():new BigDecimal(cellAccountID.getNumericCellValue()).toString());
            jv.setStAccountNo(cellAccountNo.getCellType()==cellAccountNo.CELL_TYPE_STRING?cellAccountNo.getStringCellValue():new BigDecimal(cellAccountNo.getNumericCellValue()).toString());
            jv.setStDescription(cellDescription.getCellType()==cellDescription.CELL_TYPE_STRING?cellDescription.getStringCellValue():new BigDecimal(cellDescription.getNumericCellValue()).toString());
            jv.setStCause("3");
            jv.setStCounter(String.valueOf(counter++));
            jv.setDtApplyDate(cellTanggalTitipan.getDateCellValue());
            jv.setDbAmount(new BigDecimal(cellJumlahTitipan.getNumericCellValue()));
            jv.setDbBalance(jv.getDbAmount());

            details.add(jv);

        }

        titipanReinsurance.setDetails(details);

    }

    public void doSaveReinsurance() throws Exception {

      validateReinsurance(titipanReinsurance, titipanReinsurance.getDetails());

      getRemoteGeneralLedger().saveTitipanPremiReinsurance(titipanReinsurance, titipanReinsurance.getDetails());

      super.close();

   }

    public void validateReinsurance(TitipanPremiReinsuranceHeaderView jh, DTOList details) throws Exception{

        String glCode = jh.getStAccountNoMaster().substring(5,10);

          for (int i = 0; i < details.size(); i++) {
             TitipanPremiReinsuranceView  jv = (TitipanPremiReinsuranceView ) details.get(i);

             String glCodeDetail = jv.getAccount().getStAccountNo().substring(5,10);

             if (!Tools.isEqual(DateUtil.getMonth2Digit(jv.getDtApplyDate()),jh.getStMonths())) throw new Exception("Bulan tidak sama dengan Bulan Entry pd counter "+jv.getStCounter());
             if (!Tools.isEqual(DateUtil.getYear(jv.getDtApplyDate()),jh.getStYears())) throw new Exception("Tahun tidak sama dengan Tahun Entry pd counter "+jv.getStCounter());

             if(jv.getStPolicyNo()!=null)
                 if(jv.getStPolicyNo().length()<18)
                     throw new RuntimeException("Nomor Polis harus minimal 18 digit");

             if(!glCode.equalsIgnoreCase(glCodeDetail))
                 throw new RuntimeException("Kode akun bank ("+ jh.getStAccountNoMaster() +") dengan kode akun titipan ("+ jv.getAccount().getStAccountNo() +")tidak sama");

          }

          if(isPostedReinsurance()){
              throw new Exception("Transaksi bulan "+ jh.getStMonths() +" Tahun "+ jh.getStYears()+ " Sudah diposting");
          }

    }


   public DTOList getDetailsReinsurance() throws Exception {
      return titipanReinsurance.getDetails();
   }

    public void selectYearReinsurance() throws Exception{
        if(isPostedReinsurance()){
            String tahun = titipanReinsurance.getStYears();
            titipanReinsurance.setStYears(null);
            throw new RuntimeException("Transaksi bulan "+ titipanReinsurance.getStMonths() +" dan tahun "+ tahun +" tsb sudah diposting");
        }

    }

    public boolean isPostedReinsurance() throws Exception {

            SQLUtil S = new SQLUtil();

            boolean isPosted = false;

            try {
                String cek = "select gl_post_id from gl_posting where months = ? and years = ? and posted_flag = 'Y' and cc_code is null"+
                            " union "+
                            " select gl_post_id from gl_posting where months = ? and years = ? and posted_flag = 'Y'";

                if(titipanReinsurance.getStCostCenter()!=null)
                    cek = cek + " and cc_code = ?";

                PreparedStatement PS = S.setQuery(cek);
                PS.setString(1, titipanReinsurance.getStMonths());
                PS.setString(2, titipanReinsurance.getStYears());

                PS.setString(3, titipanReinsurance.getStMonths());
                PS.setString(4, titipanReinsurance.getStYears());

                if(titipanReinsurance.getStCostCenter()!=null)
                      PS.setString(5, titipanReinsurance.getStCostCenter());

                ResultSet RS = PS.executeQuery();

                if (RS.next()){
                    isPosted = true;
                }

            } finally {
                S.release();
            }

        return isPosted;
    }

}
