/***********************************************************************
 * Module:  com.webfin.ar.forms.ReceiptForm
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:34 AM
 * Purpose:  
 ***********************************************************************/

package com.webfin.ar.forms;

import com.crux.common.model.HashDTO;
import com.crux.common.parameter.Parameter;
import com.crux.file.FileManager;
import com.crux.file.FileView;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.util.DTOList;
import com.crux.util.BDUtil;
import com.crux.util.JNDIUtil;
import com.crux.pool.DTOPool;
import com.crux.util.DateUtil;
import com.crux.util.ListUtil;
import com.webfin.ar.model.*;
import com.crux.util.LogManager;
import com.webfin.ar.ejb.AccountReceivableHome;
import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.gl.ejb.CurrencyManager;

import com.webfin.insurance.model.*;
import com.webfin.insurance.ejb.*;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.Date;
import java.math.BigDecimal;
import com.crux.util.SQLAssembler;
import com.crux.util.SQLUtil;
import com.crux.util.Tools;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.ejb.PeriodManager;
import com.webfin.gl.model.AccountView;
import com.webfin.gl.model.ClosingDetailView;
import com.webfin.gl.util.GLUtil;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class CashflowKlaimForm extends Form {

   private ARReceiptView receipt;
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
   
   private String stReceiptNo;
   private Date dtReceipt;
   
   private String stName;
   private String stEntityID;
   private String stEntityName;
   private String claimno;
   
   private String stAddress;
   
   private String stUserID;
   private String stUserName;
   private String stDivision;
   
   private String stFileName;
   private String stReceiptType;
   public String invoiceid;
   private String descLong = "";
   private String stMonths;
   private String stYears;

    private Date dtApplyDateFrom;
    private Date dtApplyDateTo;
    private String stBranch;
    private String stBranchDesc;
   
   private boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("SETTLR_NAVBR");

   private final static transient LogManager logger = LogManager.getInstance(ReceiptForm.class);

    private String stPolicyTypeID;
    private String stPolicyTypeDesc;

    private boolean canCreateOthers = SessionManager.getInstance().getSession().hasResource("FINANCE_CLM_OTHER");

    private ARCashflowView cashflow;
    private boolean reverseMode;

    public boolean isReverseMode() {
        return reverseMode;
    }

    public void setReverseMode(boolean reverseMode) {
        this.reverseMode = reverseMode;
    }

    public ARCashflowView getCashflow() {
        return cashflow;
    }

    public void setCashflow(ARCashflowView cashflow) {
        this.cashflow = cashflow;
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
        final DTOList l = receipt.getDetails();
        
        SessionManager.getInstance().getRequest().setAttribute("RPT",l);
        
        //getRemoteInsurance().updateMonitoring(l, this);
        
        super.redirect("/pages/ar/report/"+stReportType+".fop");
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

   public ARReceiptView getReceipt() {
      return receipt;
   }

   public void setReceipt(ARReceiptView receipt) {
      this.receipt = receipt;
   }

   public DTOList getListNotes() throws Exception {
      return receipt.getNotes();
   }

   public DTOList getListGLs() throws Exception {
      return receipt.getGLs();
   }

   public DTOList getListInvoices() throws Exception {
       return cashflow.getDetails();
   }

   public CashflowKlaimForm() {
   }

   public void onNewNoteItem() {

   }

   public void onDeleteNoteItem() throws Exception {
      getListNotes().delete(Integer.parseInt(notesindex));

      recalculate();
   }

   private void recalculate() throws Exception {
      receipt.recalculate();
      
      receipt.setStARTitipanID("01");
   }
   
   public void RecalculatePajak() throws Exception {
      receipt.recalculate();
      
      //receipt.setStARTitipanID("01");
   }

   public void onDeleteGLItem() throws Exception {
      getListGLs().delete(Integer.parseInt(glindex));
      recalculate();
   }

   public void onNewInvoiceItem() {

   }

   public void onDeleteInvoiceItem() throws Exception {
       
      ARCashflowDetailsView currentLine = (ARCashflowDetailsView)getListInvoices().get(Integer.parseInt(invoicesindex));

      getListInvoices().delete(Integer.parseInt(invoicesindex));

      if(cashflow.getDetails().size()>0){
          cashflow.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+cashflow.getDetails().size()+ " " + getWording());
          cashflow.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+cashflow.getDetails().size()+ " " + getWording());
      }

      currentLine.getDetails().deleteAll();
      //recalculate();
   }

   public void onDeleteInvoiceComissionItem() throws Exception {
      final ARReceiptLinesView iv = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

      iv.getDetails().delete(Integer.parseInt(invoicecomissionindex));

      recalculate();
   }

   public void onShrinkInvoiceItem() {


   }

   public void onChangeCust() {

   }

   public void onExpandInvoiceItemSuratHutang(String nosurathutang) throws Exception {
      final ARReceiptLinesView rl = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));
	  
	  rl.setStNosurathutang(nosurathutang);
	  
      final ARInvoiceView invoice = rl.getInvoiceBySuratHutang();
      
      rl.setStExpandedFlag("Y");

      final DTOList details = invoice.getDetails();

      for (int i = 0; i < details.size(); i++) {
         ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);

         if (!d.isComission()) continue;

         final ARReceiptLinesView rcl = new ARReceiptLinesView();
         rcl.markNew();

         rcl.setStInvoiceID(invoice.getStARInvoiceID());
         rcl.setStInvoiceNo(invoice.getStInvoiceNo());
         rcl.setStInvoiceDetailID(d.getStARInvoiceDetailID());
         rcl.setStDescription(d.getStDescription());
         rcl.setStCurrencyCode(invoice.getStCurrencyCode());
         rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
         rcl.setDtReceiptDate(receipt.getDtReceiptDate());
         rcl.setStPolicyID(invoice.getStPolicyID());

         rcl.setDbInvoiceAmount(d.getDbEnteredAmount());
         //rcl.setDbAmount(d.getDbOustandingAmount());
         rcl.setDbEnteredAmount(d.getDbOustandingAmount());
         //rcl.setDbAmountSettled(d.getDbAmountSettled());
         rcl.markAsComission();
         //rcl.markCommit();

         rl.getDetails().add(rcl);
      }
      receipt.recalculate();
   }
   
   /*
   public void onExpandInvoiceItem() throws Exception {
      final ARReceiptLinesView rl = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

      final ARInvoiceView invoice = rl.getInvoice();

      rl.setStExpandedFlag("Y");

      final DTOList details = invoice.getDetails();
      
      /* buat munculin detail premi bruto
      for (int i = 0; i < details.size(); i++) {
         ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);
		if(d.getStRefID0()!=null){
			if (!d.getStRefID0().startsWith("PREMIG")) continue;		
		
	         final ARReceiptLinesView rcl = new ARReceiptLinesView();
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

         final ARReceiptLinesView rcl = new ARReceiptLinesView();
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
      
      
      receipt.recalculate();
   }
   */
   
   public void onExpandInvoiceItemBackup() throws Exception {
      final ARReceiptLinesView rl = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));
      
      final ARInvoiceView invoice = rl.getInvoice();

      rl.setStExpandedFlag("Y");

      final DTOList details = invoice.getDetails();

      for (int i = 0; i < details.size(); i++) {
         ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);

         //if (!d.isComission()) continue;

         final ARReceiptLinesView rcl = new ARReceiptLinesView();
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
         
         //if(d.getStRefID0().startsWith("TAX")) rcl.setStCheck("Y");
         //rcl.setDbAmountSettled(d.getDbAmountSettled());
         //rcl.setStNegativeFlag(d.getStNegativeFlag());
         //rcl.markAsComission();
         
         if(d.isCommission2()||d.isBrokerage2()
         	||d.isHandlingFee2()){
         	 rcl.setStLock(false);
         }
         
         
         if(d.isTaxComm()||d.isTaxBrok()||d.isTaxHFee()){
            rcl.setDbAmount(BDUtil.zero);
            rcl.setDbEnteredAmount(BDUtil.zero);
         }
         	
         
         if (d.isComission()) rcl.markAsComission();
         rcl.markCommit();

         rl.getDetails().add(rcl);
      }
      receipt.recalculate();
   }
   
   public void onExpandInvoiceItem() throws Exception {
      final ARCashflowDetailsView rl = (ARCashflowDetailsView)getListInvoices().get(Integer.parseInt(invoicesindex));
      
      final ARInvoiceView invoice = rl.getInvoice();

      rl.setStExpandedFlag("Y");

      final DTOList details = invoice.getDetails();

      for (int i = 0; i < details.size(); i++) {
         ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);

         //if (!d.isComission()) continue;

         final ARCashflowDetailsView rcl = new ARCashflowDetailsView();
         rcl.markNew();

         rcl.setStInvoiceID(invoice.getStARInvoiceID());
         rcl.setStInvoiceNo(invoice.getStInvoiceNo());
         rcl.setStInvoiceDetailID(d.getStARInvoiceDetailID());
         rcl.setStDescription(d.getStDescription());
         rcl.setStCurrencyCode(invoice.getStCurrencyCode());
         rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
         rcl.setDtReceiptDate(cashflow.getDtReceiptDate());
         rcl.setStPolicyID(invoice.getStAttrPolicyID());

         rcl.setDbInvoiceAmount(d.getDbEnteredAmount());
         rcl.setDbAmount(d.getDbOustandingAmount());
         rcl.setDbEnteredAmount(d.getDbOustandingAmount());

         boolean isTax = d.isTaxComm() || d.isTaxBrok() || d.isTaxHFee();
         if(!isTax) rcl.setStCheck("Y");
         
         if(d.isCommission2()||d.isBrokerage2()||d.isHandlingFee2()){
         	 rcl.setStLock(false);
         }

         if (d.isComission()) rcl.markAsComission();

         rl.getDetails().add(rcl);
      }

      cashflow.recalculate();
   }
   
   public void onExpandInvoiceItemTitipan() throws Exception {
      final ARReceiptLinesView rl = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));
      
      final ARInvoiceView invoice = rl.getInvoice();

      rl.setStExpandedFlag("Y");

      final DTOList details = invoice.getDetails();

      for (int i = 0; i < details.size(); i++) {
         ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);

         //if (!d.isComission()) continue;

         final ARReceiptLinesView rcl = new ARReceiptLinesView();
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
         rcl.setDtReceiptDate(receipt.getDtReceiptDate());
      rcl.setStPolicyID(invoice.getStPolicyID());
         
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
      
      receipt.recalculate();
   }
   
   public void onExpandInvoiceItem3() throws Exception {
      final ARReceiptLinesView rl = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

      final ARInvoiceView invoice = rl.getInvoice();

      rl.setStExpandedFlag("Y");

      final DTOList details = invoice.getDetails();

      for (int i = 0; i < details.size(); i++) {
         ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);

         //if (!d.isComission()) continue;

         final ARReceiptLinesView rcl = new ARReceiptLinesView();
         rcl.markNew();

         rcl.setStInvoiceID(invoice.getStARInvoiceID());
         rcl.setStInvoiceNo(invoice.getStInvoiceNo());
         rcl.setStInvoiceDetailID(d.getStARInvoiceDetailID());
         rcl.setStDescription(d.getStDescription());
         rcl.setStCurrencyCode(invoice.getStCurrencyCode());
         rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
         rcl.setDtReceiptDate(receipt.getDtReceiptDate());
         rcl.setStPolicyID(invoice.getStPolicyID());

         rcl.setDbInvoiceAmount(d.getDbEnteredAmount());
         rcl.setDbAmount(d.getDbOustandingAmount());
         rcl.setDbEnteredAmount(d.getDbOustandingAmount());
         //rcl.setDbAmountSettled(d.getDbAmountSettled());
         //rcl.markAsComission();
         //rcl.markCommit();
         boolean isTax = d.isTaxComm() || d.isTaxBrok() || d.isTaxHFee();
         if(!isTax) rcl.setStCheck("Y");

         rl.getDetails().add(rcl);
      }
      
      receipt.recalculate();
   }
   
   public void onExpandInvoiceItem2(String parinvoiceid) throws Exception {
      //final ARReceiptLinesView rl = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

      final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(parinvoiceid);

	  final ARReceiptLinesView rl = new ARReceiptLinesView();
	  rl.markAsInvoice();
      //rl.markNew();
      
      rl.setStExpandedFlag("Y");

      final DTOList details = invoice.getDetails();

      for (int i = 0; i < details.size(); i++) {
         ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);

         //if (!d.isComission()) continue;

         final ARReceiptLinesView rcl = new ARReceiptLinesView();
         rcl.markNew();

         rcl.setStInvoiceID(invoice.getStARInvoiceID());
         rcl.setStInvoiceNo(invoice.getStInvoiceNo());
         rcl.setStInvoiceDetailID(d.getStARInvoiceDetailID());
         rcl.setStDescription(d.getStDescription());
         rcl.setStCurrencyCode(invoice.getStCurrencyCode());
         rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
         rcl.setDtReceiptDate(receipt.getDtReceiptDate());
         rcl.setStPolicyID(invoice.getStPolicyID());

         rcl.setDbInvoiceAmount(d.getDbEnteredAmount());
         rcl.setDbAmount(d.getDbOustandingAmount());
         rcl.setDbEnteredAmount(d.getDbOustandingAmount());
         
         //if(d.getStRefID0().startsWith("TAX")) rcl.setStCheck("Y");
         //rcl.setDbAmountSettled(d.getDbAmountSettled());
         //rcl.setStNegativeFlag(d.getStNegativeFlag());
         //rcl.markAsComission();
         //rcl.markCommit();
         

         rl.getDetails().add(rcl);
      }
      //logger.logDebug("Expand new: "+ rl.getDetails().size());
      receipt.recalculate();
   }
   
   public void createNew() throws Exception {

      cashflow = new ARCashflowView();
      cashflow.markNew();

      cashflow.setNotes(new DTOList());
      cashflow.setDetails(new DTOList());

      cashflow.setStARSettlementID("10");

      cashflow.setStInvoiceType("AP");

      //cashflow.setDtReceiptDate(new Date());

      if(!canNavigateBranch)
            cashflow.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());
      
      //final String settlementRC = (String)receipt.getSettlement().getPropMap().get("RC");

      //cashflow.setStReceiptClassID("3");

      setTitle("Cashflow Pembayaran Klaim");

      cashflow.setDtReceiptDate(new Date());

      chgCurrency();

      editMode=true;
   }
   
   public void createNew2(String stSettlementID) throws Exception {

      receipt = new ARReceiptView();
      receipt.markNew();

      receipt.setNotes(new DTOList());
      receipt.setDetails(new DTOList());

      receipt.setStARSettlementID(stSettlementID);

      //receipt.setDtReceiptDate(new Date());

      receipt.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());

      //setTitle("Cetak Monitoring Pembayaran");

      editMode=true;
   }

   public void edit(String receiptID) throws Exception {
      masterEdit(receiptID);

      if (cashflow.isPosted()) throw new RuntimeException("Data tidak bisa diubah (Sudah disetujui)");
      if (cashflow.isCancel()) throw new RuntimeException("Receipt not editable (VOID)");

      editMode = true;
   }
   
   public void editTitipan(String receiptID) throws Exception {
      masterEditTitipan(receiptID);

      if (receipt.isPosted()) throw new RuntimeException("Receipt not editable (Posted)");
      if (receipt.isCancel()) throw new RuntimeException("Receipt not editable (VOID)");

      editMode = true;
   }
   
   
   public void superEdit(String receiptID) throws Exception {
      masterEdit(receiptID);

      editMode = true;
   }

   public void masterEdit(String receiptID) throws Exception {
      cashflow = getRemoteAccountReceivable().getARCashflow(receiptID);
      
      cashflow.markUpdateO();

      cashflow.getDetails().markAllUpdate();
      cashflow.getNotes().markAllUpdate();
      cashflow.getGLs().markAllUpdate();

      final DTOList details = cashflow.getDetails();

      for (int i = 0; i < details.size(); i++) {
         ARCashflowDetailsView rcl = (ARCashflowDetailsView) details.get(i);
         
         final DTOList subDetails = rcl.getDetails();
         
         for (int j = 0; j < subDetails.size(); j++) {
            ARCashflowDetailsView rclSub = (ARCashflowDetailsView) subDetails.get(j);
            
            if(rclSub.getInvoiceDetail()!=null)
            if(rclSub.getInvoiceDetail().isCommission2()||rclSub.getInvoiceDetail().isBrokerage2()
                    ||rclSub.getInvoiceDetail().isHandlingFee2()){
                    rclSub.setStLock(false);
            }
         }

         rcl.getDetails().markAllUpdate();
      }

      setTitle("Cashflow Pembayaran Klaim");

   }
   
   public void masterEditTitipan(String receiptID) throws Exception {
      receipt = getRemoteAccountReceivable().getARReceipt(receiptID);
      if("3".equalsIgnoreCase(receipt.getStReceiptClassID())||
      	"4".equalsIgnoreCase(receipt.getStReceiptClassID())||
      	"8".equalsIgnoreCase(receipt.getStReceiptClassID())||
      	"9".equalsIgnoreCase(receipt.getStReceiptClassID())||
      	"10".equalsIgnoreCase(receipt.getStReceiptClassID()))
      		receipt.setUsingEntityID(true);
      
      receipt.markUpdateO();

      receipt.getDetails().markAllUpdate();
      receipt.getNotes().markAllUpdate();
      receipt.getGLs().markAllUpdate();

      final DTOList details = receipt.getDetails();

      for (int i = 0; i < details.size(); i++) {
         ARReceiptLinesView rcl = (ARReceiptLinesView) details.get(i);

         rcl.getDetails().markAllUpdate();
         rcl.getObjects().markAllUpdate();
      }

      setTitle(receipt.getSettlement().getStDescription());

   }

   public void view(String receiptID) throws Exception {
      cashflow = getRemoteAccountReceivable().getARCashflow(receiptID);

      super.setReadOnly(true);

      viewMode = true;

      setTitle("Cashflow Pembayaran Klaim");
   }

   public void doSave() throws Exception {
       onChgPeriod();

      cashflow.recalculate();
      cashflow.setStIDRFlag("Y");

      getRemoteAccountReceivable().saveCashflowKlaim(cashflow);

      super.close();
   }

   public void doRecalculate()throws Exception {
        
       cashflow.recalculate();
        
   }

   public void doCancel() {
      super.close();
   }

   public void doClose() {
      super.close();
   }

   public void afterUpdateForm() {
      try {
          /*
          if(receipt.getStARSettlementID()!=null){
              if(receipt.getSettlement().checkProperty("CALC_COMM","Y")) recalculatePembayaranKomisi();
              else if(receipt.getSettlement().checkProperty("CALC_TAX","Y")) recalculatePembayaranPajak();
              else if(receipt.getSettlement().checkProperty("CALC_TITIP","Y")) receipt.recalculateLKS();
              else receipt.recalculate();
          }
          */
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void chgCurrency() throws Exception {
      cashflow.setDbCurrencyRate(CurrencyManager.getInstance().getRate(cashflow.getStCurrencyCode(), cashflow.getDtReceiptDate()));
   }

   public void changeReceiptClass() {
      //receipt.setStInvoiceType(receipt.getReceiptClass().getStInvoiceType());
      if("3".equalsIgnoreCase(cashflow.getStReceiptClassID())||
      	"4".equalsIgnoreCase(cashflow.getStReceiptClassID())||
      	"8".equalsIgnoreCase(cashflow.getStReceiptClassID())||
      	"9".equalsIgnoreCase(cashflow.getStReceiptClassID())||
      	"10".equalsIgnoreCase(cashflow.getStReceiptClassID()))
      	cashflow.setUsingEntityID(true);
   } 
   
   public void changeBankType() {
      //receipt.setStInvoiceType(receipt.getReceiptClass().getStInvoiceType());
   }

   public void onNewNote() throws Exception {
      final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(parinvoiceid);

      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();

      rcl.setStInvoiceID(invoice.getStARInvoiceID());
      rcl.setStInvoiceNo(invoice.getStInvoiceNo());
      rcl.setStCurrencyCode(invoice.getStCurrencyCode());
      rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
      rcl.setDbInvoiceAmount(invoice.getDbOutstandingAmount());
      rcl.setDbAmount(rcl.getDbInvoiceAmount());
      rcl.markAsNote();
      rcl.markCommit();

      receipt.getNotes().add(rcl);

      receipt.recalculate();
   }

   public void addGL() throws Exception {
      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();

      final ARSettlementExcessView arSettlementExcessView = (ARSettlementExcessView) DTOPool.getInstance().getDTO(ARSettlementExcessView.class, stGLSelect);

      rcl.setStInvoiceID(null);
      rcl.setStInvoiceNo(arSettlementExcessView.getStDescription());
      rcl.setStCurrencyCode(null);
      rcl.setDbCurrencyRate(null);
      rcl.setDbInvoiceAmount(null);
      rcl.setDbAmount(null);
      rcl.setStARSettlementExcessID(arSettlementExcessView.getStARSettlementExcessID());

      rcl.markAsGL();
      rcl.markCommit();

      receipt.getGLs().add(rcl);

      receipt.recalculate();
   }

   /*
   public void onNewInvoice() throws Exception {
      final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(parinvoiceid);

	  BigDecimal dbInvoiceAmt0 = null;
	  BigDecimal dbEnteredAmt0 = null;
		
      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();
      
      final DTOList invoiceDetail = invoice.getDetails();
      
      for (int i = 0; i < invoiceDetail.size(); i++) {
	  		ARInvoiceDetailView invDetail = (ARInvoiceDetailView) invoiceDetail.get(i);

				if (invDetail.isComission()) continue;
				dbInvoiceAmt0 = BDUtil.add(dbInvoiceAmt0,invDetail.getDbAmount());
				dbEnteredAmt0 = BDUtil.add(dbEnteredAmt0,invDetail.getDbEnteredAmount());
      }
		
      rcl.setStInvoiceID(invoice.getStARInvoiceID());
      rcl.setStInvoiceNo(invoice.getStInvoiceNo());
      rcl.setStCurrencyCode(invoice.getStCurrencyCode());
      rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
      //rcl.setDbInvoiceAmount(invoice.getDbOutstandingAmount());
      rcl.setDbInvoiceAmount(dbInvoiceAmt0);
	  rcl.setDbEnteredAmount(dbEnteredAmt0);
      rcl.markAsInvoice();
      rcl.markCommit();

      receipt.setStEntityID(invoice.getStEntityID());

      if (receipt.getStDescription()==null) receipt.setStDescription("Payment for "+rcl.getStInvoiceNo());
      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("Payment for "+rcl.getStInvoiceNo());

      receipt.getDetails().add(rcl);

      receipt.recalculate();

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandInvoiceItem();
   }
   */
   
   public void onNewInvoiceBackup() throws Exception {
      final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(parinvoiceid);

      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();

      rcl.setStInvoiceID(invoice.getStARInvoiceID());
      rcl.setStInvoiceNo(invoice.getStInvoiceNo());
      rcl.setStCurrencyCode(invoice.getStCurrencyCode());
      rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
      rcl.setDbInvoiceAmount(invoice.getDbOutstandingAmount());
      rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
      //rcl.setStNegativeFlag(d.getStNegativeFlag());
      rcl.markAsInvoice();
      rcl.markCommit();

      receipt.setStEntityID(invoice.getStEntityID());

      if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());
      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());

      receipt.getDetails().add(rcl);

      receipt.recalculate();

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandInvoiceItem();
   }
   
   public void onNewInvoice() throws Exception {
       
      validateInvoiceAlreadyIn(parinvoiceid);
       
      final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(parinvoiceid);

      final ARCashflowDetailsView rcl = new ARCashflowDetailsView();
      rcl.markNew();

      rcl.setStInvoiceID(invoice.getStARInvoiceID());
      rcl.setStInvoiceNo(invoice.getStInvoiceNo());
      rcl.setStCurrencyCode(invoice.getStCurrencyCode());
      rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
      rcl.setDbInvoiceAmount(invoice.getDbOutstandingAmount());
      rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
      rcl.setDtReceiptDate(cashflow.getDtReceiptDate());
      rcl.setStPolicyID(invoice.getStAttrPolicyID());
      rcl.setStArInvoiceClaim(invoice.getStRefID2());
       
      //rcl.setStNegativeFlag(d.getStNegativeFlag());
      rcl.markAsInvoice();
      //rcl.markCommit();

      cashflow.setStEntityID(invoice.getStEntityID());

      if (cashflow.getStDescription()==null) cashflow.setStDescription("Cashflow : "+rcl.getStInvoiceNo());
      if (cashflow.getStShortDescription()==null) cashflow.setStShortDescription("Cashflow : "+rcl.getStInvoiceNo());
      
      cashflow.getDetails().add(rcl);
      
      if(cashflow.getDetails().size()>1){
          cashflow.setStDescription("Cashflow "+cashflow.getDetails().size()+ " " + getWording());
          cashflow.setStShortDescription("Cashflow "+cashflow.getDetails().size()+ " " + getWording());
      }

      cashflow.recalculate();

      invoicesindex=String.valueOf(cashflow.getDetails().size()-1);

      onExpandInvoiceItem();

      //tambahUangMukaKlaim();
   }
   
   public void onNewInvoiceTitipan() throws Exception {
      final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(parinvoiceid);

      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();

      rcl.setStInvoiceID(invoice.getStARInvoiceID());
      rcl.setStInvoiceNo(invoice.getStInvoiceNo());
      rcl.setStCurrencyCode(invoice.getStCurrencyCode());
      rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
      rcl.setDbInvoiceAmount(invoice.getDbOutstandingAmount());
      rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
      rcl.setDtReceiptDate(receipt.getDtReceiptDate());
      rcl.setStPolicyID(invoice.getStPolicyID());
      //rcl.setStNegativeFlag(d.getStNegativeFlag());
      rcl.markAsInvoice();
      rcl.markCommit();

      receipt.setStEntityID(invoice.getStEntityID());

      if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());
      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());

      receipt.getDetails().add(rcl);

      receipt.recalculate();

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandInvoiceItemTitipan();
   }
   
   public void onNewInvoice3() throws Exception {
      validateInvoiceAlreadyIn(parinvoiceid);
       
      final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(parinvoiceid);

      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();

      rcl.setStInvoiceID(invoice.getStARInvoiceID());
      rcl.setStInvoiceNo(invoice.getStInvoiceNo());
      rcl.setStCurrencyCode(invoice.getStCurrencyCode());
      rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
      rcl.setDbInvoiceAmount(invoice.getDbOutstandingAmount());
      rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
      rcl.setDtReceiptDate(receipt.getDtReceiptDate());
      rcl.setStPolicyID(invoice.getStAttrPolicyID());
      
      rcl.markAsInvoice();
      //rcl.markCommit();

      receipt.setStEntityID(invoice.getStEntityID());

      if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());
      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());

      receipt.getDetails().add(rcl);
      
      if(receipt.getDetails().size()>1){
          receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " Polis");
          receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " Polis");
      }

      receipt.recalculate();

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandInvoiceItem3();
   }
   
   public void onNewPolicySearch() throws Exception {
      final InsurancePolicyView policy = getRemoteInsurance().getInsurancePolicy(parinvoiceid);

      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();

      /*
      rcl.setStInvoiceID(invoice.getStARInvoiceID());
      rcl.setStInvoiceNo(invoice.getStInvoiceNo());
      rcl.setStCurrencyCode(invoice.getStCurrencyCode());
      rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
      rcl.setDbInvoiceAmount(invoice.getDbOutstandingAmount());
      rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());*/
      //rcl.setStNegativeFlag(d.getStNegativeFlag());
      rcl.markAsInvoice();
      rcl.markCommit();

     // receipt.setStEntityID(invoice.getStEntityID());

      if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());
      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());

      receipt.getDetails().add(rcl);

      receipt.recalculate();

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

   }
   
   public void onNewInvoice2() throws Exception {
       
      validateInvoiceAlreadyIn(parinvoiceid);
       
      final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(parinvoiceid);

      /*final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();

      rcl.setStInvoiceID(invoice.getStARInvoiceID());
      rcl.setStInvoiceNo(invoice.getStInvoiceNo());
      rcl.setStCurrencyCode(invoice.getStCurrencyCode());
      rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
      rcl.setDbInvoiceAmount(invoice.getDbOutstandingAmount());
      rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
      //rcl.setStNegativeFlag(d.getStNegativeFlag());
      rcl.markAsInvoice();
      rcl.markCommit();

      receipt.setStEntityID(invoice.getStEntityID());

      if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());
      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());

      receipt.getDetails().add(rcl);

      receipt.recalculate();*/

      //invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandInvoiceItem2(parinvoiceid);
   }
   
   public void onNewInvoiceByNoSuratHutang2(String nosurathutang) throws Exception {
      final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(parinvoiceid);
	  
	  BigDecimal dbInvoiceAmt0 = null;
	  BigDecimal dbEnteredAmt0 = null;
	  
      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();
      
      final DTOList invoiceDetail = invoice.getDetails();
      
       for (int i = 0; i < invoiceDetail.size(); i++) {
	  		ARInvoiceDetailView invDetail = (ARInvoiceDetailView) invoiceDetail.get(i);
	  		
				if (invDetail.isComission()) continue;
				dbInvoiceAmt0 = BDUtil.add(dbInvoiceAmt0,invDetail.getDbAmount());
				dbEnteredAmt0 = BDUtil.add(dbEnteredAmt0,invDetail.getDbEnteredAmount());
      }

      rcl.setStInvoiceID(invoice.getStARInvoiceID());
      rcl.setStInvoiceNo(invoice.getStInvoiceNo());
      rcl.setStCurrencyCode(invoice.getStCurrencyCode());
      rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
      //rcl.setDbInvoiceAmount(invoice.getDbOutstandingAmount());
      //rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
      rcl.setDbInvoiceAmount(dbInvoiceAmt0);
	  rcl.setDbEnteredAmount(dbEnteredAmt0);
          rcl.setDtReceiptDate(receipt.getDtReceiptDate());
      rcl.setStPolicyID(invoice.getStPolicyID());
      rcl.markAsInvoice();
      //rcl.markCommit();

      receipt.setStEntityID(invoice.getStEntityID());

      if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+nosurathutang);
      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+nosurathutang);

      receipt.getDetails().add(rcl);

      receipt.recalculate();

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandInvoiceItem();
   }
   
   public void onNewInvoiceByNoSuratHutang(HashDTO dto, boolean samedata) throws Exception {
      /*	  
      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();
      
      rcl.setStInvoiceID(invoice.getStARInvoiceID());
      rcl.setStInvoiceNo(invoice.getStInvoiceNo());
      rcl.setStCurrencyCode(invoice.getStCurrencyCode());
      rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
      rcl.setDbInvoiceAmount(invoice.getDbOutstandingAmount());
      rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
      rcl.setDtReceiptDate(receipt.getDtReceiptDate());
      rcl.setStPolicyID(invoice.getStPolicyID());
      rcl.markAsInvoice();

      receipt.setStEntityID(invoice.getStEntityID());

      if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+nosurathutang);
      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+nosurathutang);

      receipt.getDetails().add(rcl);

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandInvoiceItemNoSuratHutang();*/
      
      if(!samedata){
          final ARReceiptLinesView rcl = new ARReceiptLinesView();
          rcl.markNew();

          rcl.setStInvoiceID(dto.getFieldValueByFieldNameBD("ar_invoice_id").toString());
          rcl.setStInvoiceNo(dto.getFieldValueByFieldNameST("invoice_no"));
          rcl.setStCurrencyCode(dto.getFieldValueByFieldNameST("ccy"));
          rcl.setDbCurrencyRate(dto.getFieldValueByFieldNameBD("ccy_rate"));
          rcl.setDbInvoiceAmount(dto.getFieldValueByFieldNameBD("amount"));
          rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
          rcl.setDtReceiptDate(receipt.getDtReceiptDate());
          rcl.setStPolicyID(dto.getFieldValueByFieldNameBD("attr_pol_id").toString());
          rcl.markAsInvoice();

          receipt.setStEntityID(dto.getFieldValueByFieldNameBD("ent_id").toString());

          receipt.getDetails().add(rcl);

          invoicesindex=String.valueOf(receipt.getDetails().size()-1);
      }
      
      onExpandInvoiceItemNoSuratHutang(dto);
   }
   
   public void onNewSuratHutang() throws Exception {
      ARInvoiceView invoice = new ARInvoiceView();
      invoice.setStNoSuratHutang(nosurathutang);
      final DTOList listInv = invoice.getList2();
      
      descLong = descLong + "\n" + nosurathutang;
	  for (int i = 0; i < listInv.size(); i++) {
                //ARInvoiceView invoiceView = (ARInvoiceView) listInv.get(i);
                HashDTO dto = (HashDTO) listInv.get(i);
                
                boolean samedata = false;
                
                if(i>0){
                    HashDTO dto2 = (HashDTO) listInv.get(i-1);
                    String invoice_id = dto2.getFieldValueByFieldNameBD("ar_invoice_id").toString();
                
                    if(dto.getFieldValueByFieldNameBD("ar_invoice_id").toString().equalsIgnoreCase(invoice_id))
                        samedata = true;
                }
                
                //parinvoiceid = invoiceView.getStARInvoiceID();
                
                onNewInvoiceByNoSuratHutang(dto,samedata);
	  }
    
      receipt.setStDescription("Pembayaran " + descLong);
      //receipt.setStDescription(receipt.getStDescription().replaceAll("Pembayaran \n", "Pembayaran "));
      receipt.setStShortDescription(receipt.getStDescription());
      //receipt.recalculate();
   }   

   public void changeCostCenter() throws Exception{
        //cekClosingStatus("INPUT");
   }

   public void changemethod() {

   }

   public void generatRNo() throws Exception {
      receipt.generateReceiptNo();
   }

   public void approve(String receiptID) throws Exception {
      masterEditApprove(receiptID);

      if(isPosted()){
            String tahun = cashflow.getStYears();
            throw new RuntimeException("Transaksi bulan "+ cashflow.getStMonths() +" dan tahun "+ tahun +" tsb sudah diposting");
      }
      
      if (cashflow.isPosted()) throw new RuntimeException("Receipt not editable (Posted)");
      if (cashflow.isCancel()) throw new RuntimeException("Receipt not editable (VOID)");

      cashflow.setStPostedFlag("Y");
      super.setReadOnly(true);
      approveMode = true;
   }

   public boolean isApproveMode() {
      return approveMode;
   }

   public void doApprove() throws Exception {
      //receipt.setStPostedFlag("Y");
      doSave();
   }
   
   

   public void doVoid() throws Exception {
      receipt.setStCancelFlag("Y");
      receipt.setStPostedFlag("N");

      DTOList details = receipt.getDetails();

      for (int i = 0; i < details.size(); i++) {
         ARReceiptLinesView lines = (ARReceiptLinesView) details.get(i);

         lines.setStCommitFlag("N");
      }

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

   public void voids(String receiptID) throws Exception {
      masterEdit(receiptID);

      if (!receipt.isCancel()); else throw new RuntimeException("Invalid State");

      super.setReadOnly(true);
      voidMode = true;
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
   
   public DTOList getObjects() throws Exception{
      ARReceiptLinesView activeReceiptLine = getActiveReceiptLine();

      objects = activeReceiptLine.getObjects();

      return objects;
   }

   public ARReceiptLinesView getActiveReceiptLine() throws Exception {
   	  Integer tes = Integer.valueOf(invoicesindex);
   	  int tes2 = tes.intValue();
      final ARReceiptLinesView rl = (ARReceiptLinesView) receipt.getDetails().get(tes2);
      return rl;
   }

   public void setObjects(DTOList objects) {
      this.objects = objects;
   }
   
   public String getObjectindex() {
      return objectindex;
   }

   public void setObjectindex(String objectindex) {
      this.objectindex = objectindex;
   }
   
   public void clickPrintReceipt()throws Exception{
        final DTOList l = receipt.getDetails();
        
        SessionManager.getInstance().getRequest().setAttribute("RPT",l);
        
        super.redirect("/pages/ar/report/"+stReceiptType+".fop");
   }
   
   public void createNewReceipt(String stSettlementID) throws Exception {

      receipt = new ARReceiptView();
      receipt.markNew();

      receipt.setNotes(new DTOList());
      receipt.setDetails(new DTOList());

      receipt.setStARSettlementID(stSettlementID);

      //receipt.setDtReceiptDate(new Date());

      receipt.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());

     // setTitle("Cetak Monitoring Pembayaran");

      editMode=true;
   }
   
   public String getStReceiptNo() {
      return stReceiptNo;
   }

   public void setStReceiptNo(String stReceiptNo) {
      this.stReceiptNo = stReceiptNo;
   }
   
   public Date getDtReceipt() {
      return dtReceipt;
   }

   public void setDtReceipt(Date dtReceipt) {
      this.dtReceipt = dtReceipt;
   }
   
   public String getStName() {
      return stName;
   }

   public void setStName(String stName) {
      this.stName = stName;
   }
   
   public String getStEntityID() {
      return stEntityID;
   }

   public void setStEntityID(String stEntityID) {
      this.stEntityID = stEntityID;
   }
   
   public String getStEntityName() {
      return stEntityName;
   }

   public void setStEntityName(String stEntityName) {
      this.stEntityName = stEntityName;
   }
   
   public void clickPrintClaim()throws Exception{
   	  final DTOList l = receipt.getDetails();

      SessionManager.getInstance().getRequest().setAttribute("RPT",l);
        
//      getRemoteInsurance().updateClaimRecap(l, this);

      super.redirect("/pages/ar/report/claim.fop");
   }
   
   public String getClaimNo() {
      return claimno;
   }

   public void setClaimNo(String claimno) {
      this.claimno = claimno;
   }
   
   public void onNewClaimNo() throws Exception {
      final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoiceByClaimNo(claimno);
      
      invoice.setStClaimNo(claimno);
      
      
      final DTOList listInv = invoice.getList3();
      
	  for (int i = 0; i < listInv.size(); i++) {
	  		ARInvoiceView invoiceView = (ARInvoiceView) listInv.get(i);
	  		parinvoiceid = invoiceView.getStARInvoiceID();
			onNewInvoiceByClaim(claimno);
	  }
	   
   }
   
   public void onNewInvoiceByClaim(String claimno) throws Exception {
      final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(parinvoiceid);
	  
      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();
      
      rcl.setStInvoiceID(invoice.getStARInvoiceID());
      rcl.setStInvoiceNo(invoice.getStInvoiceNo());
      rcl.setStArInvoiceClaim(invoice.getStClaimNo());
      rcl.setStCurrencyCode(invoice.getStCurrencyCode());
      rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
      rcl.setDbInvoiceAmount(invoice.getDbOutstandingAmount());
      rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
      rcl.setDtReceiptDate(receipt.getDtReceiptDate());
      rcl.setStPolicyID(invoice.getStAttrPolicyID());
      rcl.markAsInvoice();
      rcl.markCommit();
/*
      receipt.setStEntityID(invoice.getStEntityID());

      if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+claimno);
      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+claimno);
*/
      receipt.getDetails().add(rcl);

      receipt.recalculate();

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandInvoiceItem();
   }
   
   public String getStAddress() {
      return stAddress;
   }

   public void setStAddress(String stAddress) {
      this.stAddress = stAddress;
   }
   
   public String getStUserID() {
      return stUserID;
   }

   public void setStUserID(String stUserID) {
      this.stUserID = stUserID;
   }
   
   public String getStUserName() {
      return stUserName;
   }

   public void setStUserName(String stUserName) {
      this.stUserName = stUserName;
   }
   
   public String getStDivision() {
      return stDivision;
   }

   public void setStDivision(String stDivision) {
      this.stDivision = stDivision;
   }
   
   public void clickPrintLetter()throws Exception{
   	  final DTOList l = receipt.getDetails();

      SessionManager.getInstance().getRequest().setAttribute("RPT",l);
        
      //getRemoteInsurance().updateClaimRecap(l, this);

      super.redirect("/pages/ar/report/letter.fop");
   }
 
    public void clickPrintExcel() throws Exception {
        
        final DTOList l = EXCEL_CLAIM();
        
        SessionManager.getInstance().getRequest().setAttribute("RPT",l);
        
        EXPORT_CLAIM();
    }

    public String getStFileName() {
        return stFileName;
    }
    
    public void setStFileName(String stFileName) {
        this.stFileName = stFileName;
    }

    public void checkBayarFlag(){
        
    }
    
    public void validateInvoiceAlreadyIn(String arInvoiceID) throws Exception{
        final DTOList detail = cashflow.getDetails();
        
         for (int i = 0; i < detail.size(); i++) {
              ARCashflowDetailsView rl = (ARCashflowDetailsView) detail.get(i);
              
              if(rl.getInvoice()!=null){
                  if(rl.getInvoice().getStARInvoiceID().equalsIgnoreCase(arInvoiceID))
                        throw new RuntimeException("Invoice "+arInvoiceID +" Sudah Dipilih sebelumnya");
              }
         }
    }
    
   public void masterEditApprove(String receiptID) throws Exception {
      cashflow = getRemoteAccountReceivable().getARCashflow(receiptID);
      
      cashflow.markUpdateO();

      cashflow.getDetails().markAllUpdate();
      cashflow.getNotes().markAllUpdate();
      cashflow.getGLs().markAllUpdate();

      final DTOList details = cashflow.getDetails();

      for (int i = 0; i < details.size(); i++) {
         ARCashflowDetailsView rcl = (ARCashflowDetailsView) details.get(i);
         
         final DTOList subDetails = rcl.getDetails();
         
         for (int j = 0; j < subDetails.size(); j++) {
            ARCashflowDetailsView rclSub = (ARCashflowDetailsView) subDetails.get(j);
            
            if(rclSub.getInvoiceDetail()!=null)
            if(rclSub.getInvoiceDetail().isCommission2()||rclSub.getInvoiceDetail().isBrokerage2()
                    ||rclSub.getInvoiceDetail().isHandlingFee2()){
                    rclSub.setStLock(false);
            }
            rclSub.markCommit();
         }
         
         rcl.markCommit();
         rcl.getDetails().markAllUpdate();
      }

      setTitle("Ubah Cashflow Pembayaran Klaim");

   }
   
   private void recalculatePembayaranKomisi() throws Exception {
      receipt.recalculatePembayaranKomisi();
   }
   
   public void onNewInvoicePembayaranKomisi() throws Exception {
       
      validateInvoiceAlreadyIn(parinvoiceid);
       
      final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(parinvoiceid);

      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();

      rcl.setStInvoiceID(invoice.getStARInvoiceID());
      rcl.setStInvoiceNo(invoice.getStInvoiceNo());
      rcl.setStCurrencyCode(invoice.getStCurrencyCode());
      rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
      rcl.setDbInvoiceAmount(invoice.getDbOutstandingAmount());
      rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
      rcl.setDtReceiptDate(receipt.getDtReceiptDate());
      rcl.setStPolicyID(invoice.getStPolicyID());
      
      //rcl.setStNegativeFlag(d.getStNegativeFlag());
      rcl.markAsInvoice();
      //rcl.markCommit();

      receipt.setStEntityID(invoice.getStEntityID());

      if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());
      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());
      
      receipt.getDetails().add(rcl);
      
      if(receipt.getDetails().size()>1){
          receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} Komisi "+receipt.getDetails().size()+ " Polis");
          receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} Komisi "+receipt.getDetails().size()+ " Polis");
      }

      receipt.recalculatePembayaranKomisi();

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandInvoiceItemPembayaranKomisi();
   }
   
   public void onExpandInvoiceItemPembayaranKomisi() throws Exception {
      final ARReceiptLinesView rl = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));
      
      final ARInvoiceView invoice = rl.getInvoice();

      rl.setStExpandedFlag("Y");

      final DTOList details = invoice.getDetails();

      for (int i = 0; i < details.size(); i++) {
         ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);

         //if (!d.isComission()) continue;

         final ARReceiptLinesView rcl = new ARReceiptLinesView();
         rcl.markNew();

         rcl.setStInvoiceID(invoice.getStARInvoiceID());
         rcl.setStInvoiceNo(invoice.getStInvoiceNo());
         rcl.setStInvoiceDetailID(d.getStARInvoiceDetailID());
         rcl.setStDescription(d.getStDescription());
         rcl.setStCurrencyCode(invoice.getStCurrencyCode());
         rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
         rcl.setDtReceiptDate(receipt.getDtReceiptDate());
         rcl.setStPolicyID(invoice.getStPolicyID());

         rcl.setDbInvoiceAmount(d.getDbEnteredAmount());
         rcl.setDbAmount(d.getDbOustandingAmount());
         rcl.setDbEnteredAmount(d.getDbOustandingAmount());
         
         //if(d.getStRefID0().startsWith("TAX")) rcl.setStCheck("Y");
         //rcl.setDbAmountSettled(d.getDbAmountSettled());
         //rcl.setStNegativeFlag(d.getStNegativeFlag());
         //rcl.markAsComission();
         boolean isTax = d.isTaxComm() || d.isTaxBrok() || d.isTaxHFee();
         if(!isTax) rcl.setStCheck("Y");
         
         if(d.isCommission2()||d.isBrokerage2()
         	||d.isHandlingFee2()){
         	 rcl.setStLock(false);
         }
         
         /*
         if(d.isTaxComm()||d.isTaxBrok()||d.isTaxHFee()){
            rcl.setDbAmount(BDUtil.zero);
            rcl.setDbEnteredAmount(BDUtil.zero);
         }*/
         	
         
         if (d.isComission()) rcl.markAsComission();
         //rcl.markCommit();

         rl.getDetails().add(rcl);
      }
      receipt.recalculatePembayaranKomisi();
   }
    
   public void onNewInvoicePembayaranPajak() throws Exception {
       
      validateInvoiceAlreadyIn(parinvoiceid);
       
      final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(parinvoiceid);
      
      

      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();

      rcl.setStInvoiceID(invoice.getStARInvoiceID());
      rcl.setStInvoiceNo(invoice.getStInvoiceNo());
      rcl.setStCurrencyCode(invoice.getStCurrencyCode());
      rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
      rcl.setDbInvoiceAmount(invoice.getDbOutstandingAmount());
      rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
      rcl.setDtReceiptDate(receipt.getDtReceiptDate());
      rcl.setStPolicyID(invoice.getStPolicyID());
      
      //rcl.setStNegativeFlag(d.getStNegativeFlag());
      rcl.markAsInvoice();
      //rcl.markCommit();

      receipt.setStEntityID(invoice.getStEntityID());

      if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());
      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());
      
      receipt.getDetails().add(rcl);
      
      if(receipt.getDetails().size()>1){
          receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+" Pajak");
          receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+" Pajak");
      }

      receipt.recalculatePembayaranKomisi();

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandInvoiceItemPembayaranPajak();
   }
   
   public void onExpandInvoiceItemPembayaranPajak() throws Exception {
      final ARReceiptLinesView rl = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));
      
      final ARInvoiceView invoice = rl.getInvoice();

      rl.setStExpandedFlag("Y");

      final DTOList details = invoice.getDetails();

      for (int i = 0; i < details.size(); i++) {
         ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);

         //if (!d.isComission()) continue;

         final ARReceiptLinesView rcl = new ARReceiptLinesView();
         rcl.markNew();

         rcl.setStInvoiceID(invoice.getStARInvoiceID());
         rcl.setStInvoiceNo(invoice.getStInvoiceNo());
         rcl.setStInvoiceDetailID(d.getStARInvoiceDetailID());
         rcl.setStDescription(d.getStDescription());
         rcl.setStCurrencyCode(invoice.getStCurrencyCode());
         rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
         rcl.setDtReceiptDate(receipt.getDtReceiptDate());
         rcl.setStPolicyID(invoice.getStPolicyID());

         rcl.setDbInvoiceAmount(d.getDbEnteredAmount());
         rcl.setDbAmount(d.getDbOustandingAmount());
         rcl.setDbEnteredAmount(d.getDbOustandingAmount());
         
         //if(d.getStRefID0().startsWith("TAX")) rcl.setStCheck("Y");
         //rcl.setDbAmountSettled(d.getDbAmountSettled());
         //rcl.setStNegativeFlag(d.getStNegativeFlag());
         //rcl.markAsComission();
         boolean isTax = d.isTaxComm() || d.isTaxBrok() || d.isTaxHFee();
         if(!isTax) rcl.setStCheck("Y");
         
         if(d.isCommission2()||d.isBrokerage2()
         	||d.isHandlingFee2()){
         	 rcl.setStLock(false);
         }
         
         /*
         if(d.isTaxComm()||d.isTaxBrok()||d.isTaxHFee()){
            rcl.setDbAmount(BDUtil.zero);
            rcl.setDbEnteredAmount(BDUtil.zero);
         }*/
         	
         
         if (d.isComission()) rcl.markAsComission();
         //rcl.markCommit();

         rl.getDetails().add(rcl);
      }
      receipt.recalculatePembayaranPajak();
   }
    
    
    private void recalculatePembayaranPajak() throws Exception {
      receipt.recalculatePembayaranPajak();
   }
    
    public void onNewInvoiceByInvoiceID(String invoice_id) throws Exception {
       
      validateInvoiceAlreadyIn(invoice_id);
       
      final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(invoice_id);

      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();

      rcl.setStInvoiceID(invoice.getStARInvoiceID());
      rcl.setStInvoiceNo(invoice.getStInvoiceNo());
      rcl.setStCurrencyCode(invoice.getStCurrencyCode());
      rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
      rcl.setDbInvoiceAmount(invoice.getDbOutstandingAmount());
      rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
      rcl.setDtReceiptDate(receipt.getDtReceiptDate());
      rcl.setStPolicyID(invoice.getStAttrPolicyID());
       
      //rcl.setStNegativeFlag(d.getStNegativeFlag());
      rcl.markAsInvoice();
      //rcl.markCommit();

      receipt.setStEntityID(invoice.getStEntityID());

      if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());
      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());
      
      receipt.getDetails().add(rcl);
      
      if(receipt.getDetails().size()>1){
          receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " Polis");
          receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " Polis");
      }

      receipt.recalculate();

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandInvoiceItem();
   }

    public boolean isCanNavigateBranch()
    {
        return canNavigateBranch;
    }

    public void setCanNavigateBranch(boolean canNavigateBranch)
    {
        this.canNavigateBranch = canNavigateBranch;
    }
    
    public void onNewLKS() throws Exception {
       
      //validateInvoiceAlreadyIn(parinvoiceid);
       
      final InsurancePolicyView policy = getRemoteInsurance().getInsurancePolicy(parinvoiceid);

      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();

      rcl.setStInvoiceID(policy.getStPolicyID());
      rcl.setStInvoiceNo(policy.getStPLANo());
      rcl.setStCurrencyCode(policy.getStCurrencyCode());
      rcl.setDbCurrencyRate(policy.getDbCurrencyRate());
      rcl.setDbInvoiceAmount(policy.getDbClaimAmountEstimate());
      rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
      rcl.setDtReceiptDate(receipt.getDtReceiptDate());
      rcl.setStPolicyID(policy.getStPolicyID());
       
      //rcl.setStNegativeFlag(d.getStNegativeFlag());
      rcl.markAsInvoice();
      //rcl.markCommit();

      receipt.setStEntityID(policy.getStEntityID());

      if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());
      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());
      
      receipt.getDetails().add(rcl);
      
      if(receipt.getDetails().size()>1){
          receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " Polis");
          receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " Polis");
      }

      receipt.recalculateLKS();

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandLKSItem(policy);
   }
    
    public void onExpandLKSItem(InsurancePolicyView policy) throws Exception {
      final ARReceiptLinesView rl = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));
      
      //final ARInvoiceView invoice = rl.getInvoice();

      rl.setStExpandedFlag("Y");

      final DTOList details = policy.getClaimItems();
      
      logger.logDebug("++++++++++++++ EXPAND DETAILS +++++++++++++++++++");
      logger.logDebug("pol id : "+ policy.getStPolicyID());
      logger.logDebug("size : "+ details.size());
      logger.logDebug("++++++++++++++ EXPAND DETAILS +++++++++++++++++++");

      for (int i = 0; i < details.size(); i++) {
         InsurancePolicyItemsView d = (InsurancePolicyItemsView) details.get(i);

         //if (!d.isComission()) continue;

         final ARReceiptLinesView rcl = new ARReceiptLinesView();
         rcl.markNew();

         rcl.setStInvoiceID(policy.getStPolicyID());
         rcl.setStInvoiceNo(policy.getStPolicyNo());
         rcl.setStInvoiceDetailID(d.getStPolicyItemID());
         rcl.setStDescription(d.getInsuranceItem().getStDescription());
         rcl.setStCurrencyCode(policy.getStCurrencyCode());
         rcl.setDbCurrencyRate(policy.getDbCurrencyRate());
         rcl.setDtReceiptDate(receipt.getDtReceiptDate());
         rcl.setStPolicyID(policy.getStPolicyID());

         rcl.setDbInvoiceAmount(d.getDbAmount());
         rcl.setDbAmount(d.getDbAmount());
         rcl.setDbEnteredAmount(d.getDbAmount());
         
         rcl.setStCheck("Y");

         rl.getDetails().add(rcl);
      }
      receipt.recalculateLKS();
   }
    
    public void recalculateLKS(){
        
    }
    
    public void onDeleteLKSItem() throws Exception {
      getListInvoices().delete(Integer.parseInt(invoicesindex));
      recalculateLKS();
   }
    
    public void onExpandInvoiceItemNoSuratHutang(HashDTO dto) throws Exception {
         final ARReceiptLinesView rl = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

         rl.setStExpandedFlag("Y");

         final ARReceiptLinesView rcl = new ARReceiptLinesView();
         rcl.markNew();

         rcl.setStInvoiceID(dto.getFieldValueByFieldNameBD("ar_invoice_id").toString());
         rcl.setStInvoiceNo(dto.getFieldValueByFieldNameST("invoice_no"));
         rcl.setStInvoiceDetailID(dto.getFieldValueByFieldNameBD("ar_invoice_dtl_id").toString());
         rcl.setStDescription(dto.getFieldValueByFieldNameST("ket"));
         rcl.setStCurrencyCode(dto.getFieldValueByFieldNameST("ccy"));
         rcl.setDbCurrencyRate(dto.getFieldValueByFieldNameBD("ccy_rate"));
         rcl.setDtReceiptDate(receipt.getDtReceiptDate());
         rcl.setStPolicyID(dto.getFieldValueByFieldNameBD("attr_pol_id").toString());
         rcl.setDbInvoiceAmount(dto.getFieldValueByFieldNameBD("utang"));
         rcl.setDbAmount(rcl.getDbInvoiceAmount());
         rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
         rcl.setStCheck("Y");

         rl.getDetails().add(rcl);
   }
    
    public void onDeleteInvoiceItemPajak() throws Exception {
      getListInvoices().delete(Integer.parseInt(invoicesindex));
      if(receipt.getDetails().size()>0){
          receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " " + getWording());
          receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " " + getWording());
      }
      recalculatePembayaranPajak();
   }
    
    public String getWording(){
      
      return "Klaim";
    }
    
    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB",GeneralLedgerHome.class.getName()))
        .create();
    }
    
    public void createAccount()throws Exception{
        ARReceiptLinesView xc = (ARReceiptLinesView) receipt.getGLs().get(Integer.parseInt(glindex));
        ARSettlementExcessView excess = (ARSettlementExcessView) xc.getARSettlementExcess();
        ARReceiptLinesView rl = (ARReceiptLinesView) receipt.getDetails().get(0);
        final AccountView account = getRemoteGeneralLedger().getAccountByAccountID(xc.getStExcessAccountID());
        String account_header = account.getStAccountNo().substring(0,5);
        
        
        final GLUtil.Applicator gla = new GLUtil.Applicator();
        
        gla.setCode('B',receipt.getStCostCenterCode());
        gla.setCode('X',rl.getInvoice().getPolicyType().getStPolicyTypeID());
        gla.setCode('Y',"00000");
        gla.setCode('H',account_header);
        gla.setDesc("H",account.getStDescription());
        gla.setDesc("X",rl.getInvoice().getPolicyType().getStShortDescription());
        gla.setDesc("Y","");
        
        if(receipt.getPaymentEntity()!=null){
            gla.setCode('Y',receipt.getPaymentEntity().getStGLCode());
            gla.setDesc("Y",receipt.getPaymentEntity().getStShortName());
            
            //48923 12014 21 12
            //0123456789012345
            xc.setStExcessAccountID(gla.getAccountID(excess.getStGLAccount()));
            xc.setStExcessDescription(gla.getStGLDesc());
        }
        
    }
            
    public String getStReceiptType() {
        return stReceiptType;
    }

    public void setStReceiptType(String stReceiptType) {
        this.stReceiptType = stReceiptType;
    }
    
    public void onNewInvoicePembayaranKomisiFoxpro(String invoiceid) throws Exception {
       
      validateInvoiceAlreadyIn(invoiceid);
       
      final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(invoiceid);

      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();

      rcl.setStInvoiceID(invoice.getStARInvoiceID());
      rcl.setStInvoiceNo(invoice.getStInvoiceNo());
      rcl.setStCurrencyCode(invoice.getStCurrencyCode());
      rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
      rcl.setDbInvoiceAmount(invoice.getDbOutstandingAmount());
      rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
      rcl.setDtReceiptDate(receipt.getDtReceiptDate());
      rcl.setStPolicyID(invoice.getStPolicyID());
      
      //rcl.setStNegativeFlag(d.getStNegativeFlag());
      rcl.markAsInvoice();
      //rcl.markCommit();

      receipt.setStEntityID(invoice.getStEntityID());

      if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());
      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());
      
      receipt.getDetails().add(rcl);
      
      if(receipt.getDetails().size()>1){
          receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} Komisi "+receipt.getDetails().size()+ " Polis");
          receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} Komisi "+receipt.getDetails().size()+ " Polis");
      }

      receipt.recalculatePembayaranKomisi();

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandInvoiceItemPembayaranKomisi();
   }
    
    public void addGLPerLine() throws Exception {
      //logger.logDebug("+++++++++++ INVOICE ID : "+ getInvoiceid());
      final ARReceiptLinesView rclExists = (ARReceiptLinesView) receipt.getDetails().get(Integer.parseInt(getInvoiceid()));

      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();

      final ARSettlementExcessView arSettlementExcessView = (ARSettlementExcessView) DTOPool.getInstance().getDTO(ARSettlementExcessView.class, stGLSelect);

      rcl.setStInvoiceID(rclExists.getStInvoiceID());
      rcl.setStInvoiceNo(arSettlementExcessView.getStDescription());
      rcl.setStCurrencyCode(null);
      rcl.setDbCurrencyRate(null);
      rcl.setDbInvoiceAmount(null);
      rcl.setDbAmount(null);
      rcl.setStARSettlementExcessID(arSettlementExcessView.getStARSettlementExcessID());

      rcl.markAsGL();
      rcl.markCommit();

      rclExists.getGLs().add(rcl);
      //receipt.getDetails().getGLs().add(rcl);

      receipt.recalculate();
   }

    public String getInvoiceid() {
        return invoiceid;
    }

    public void setInvoiceid(String invoiceid) {
        this.invoiceid = invoiceid;
    }
    
    public void changeTaxMode(){
        
    }

    public void onChgPeriod() throws Exception {
        //logger.logDebug("++++++++++++ : "+receipt.getStMonths());
        //logger.logDebug("############ : "+receipt.getStYears());
        //logger.logDebug("%%%%%%%%%%%% : "+DateUtil.getMonth2Digit(receipt.getDtReceiptDate()));
        //logger.logDebug("$$$$$$$$$$$$ : "+DateUtil.getYear(receipt.getDtReceiptDate()));
        
        if (!Tools.isEqual(DateUtil.getMonth2Digit(cashflow.getDtReceiptDate()),cashflow.getStMonths())) throw new Exception("Bulan tidak sama dengan Tanggal Pembayaran");
        if (!Tools.isEqual(DateUtil.getYear(cashflow.getDtReceiptDate()),cashflow.getStYears())) throw new Exception("Tahun tidak sama dengan Tanggal Pembayaran");
    }
    
    public String getClaimno() {
        return claimno;
    }

    public void setClaimno(String claimno) {
        this.claimno = claimno;
    }
    
    public void onNewUangMukaKlaim(BigDecimal amount) throws Exception {
       
      //validateInvoiceAlreadyIn(parinvoiceid);
       
      //final ARInvoiceView invoice = getRemoteAccountReceivable().get(parinvoiceid);
      final InsurancePolicyView policy = getRemoteInsurance().getInsurancePolicy(parinvoiceid);

      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();
      
      String invoice_no = policy.isStatusClaimDLA()?policy.getStDLANo():policy.getStPLANo();
      BigDecimal amt = BDUtil.mul(policy.getDbClaimAmount(), policy.getDbCurrencyRate());//policy.isStatusClaimDLA()?policy.getDbClaimAmountApproved():policy.getDbClaimAmountEstimate();
      //Date dt = policy.isStatusClaimDLA()?policy.getDtDLADate():policy.getDtPLADate();

      if(!BDUtil.isZeroOrNull(amount)) amt = amount;
      
      //rcl.setStInvoiceID(invoice.getStARInvoiceID());
      rcl.setStInvoiceNo(policy.getStPolicyNo());
      rcl.setStArInvoiceClaim(invoice_no);
      rcl.setStCurrencyCode(policy.getStCurrencyCode());
      rcl.setDbCurrencyRate(policy.getDbCurrencyRate());
      rcl.setDbInvoiceAmount(amt);
      rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
      rcl.setDtReceiptDate(receipt.getDtReceiptDate());
      rcl.setStPolicyID(policy.getStPolicyID());
      rcl.setStAdvancePaymentFlag("Y");

      rcl.markAsInvoice();

      receipt.setStEntityID(policy.getStEntityID());

      if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());
      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());

      receipt.getDetails().add(rcl);
      
      if(receipt.getDetails().size()>1){
          receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " " + getWording());
          receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " " + getWording());
      }

      receipt.recalculate();

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandUangMuka();
   }
    
    public void onExpandUangMuka() throws Exception {
      final ARReceiptLinesView rl = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));
      
      //final ARInvoiceView invoice = rl.getInvoice();

      rl.setStExpandedFlag("Y");
      
      final ARReceiptLinesView rcl = new ARReceiptLinesView();
         rcl.markNew();

         //rcl.setStInvoiceID(invoice.getStARInvoiceID());
         rcl.setStInvoiceNo(rl.getStInvoiceNo());
         rcl.setStArInvoiceClaim(rl.getStArInvoiceClaim());
         //rcl.setStInvoiceDetailID(d.getStARInvoiceDetailID());
         rcl.setStDescription("Uang Muka Klaim");
         rcl.setStCurrencyCode(rl.getStCurrencyCode());
         rcl.setDbCurrencyRate(rl.getDbCurrencyRate());
         rcl.setDtReceiptDate(rl.getDtReceiptDate());
         rcl.setStPolicyID(rl.getStPolicyID());

         rcl.setDbInvoiceAmount(rl.getDbInvoiceAmount());
         rcl.setDbAmount(rl.getDbInvoiceAmount());
         rcl.setDbEnteredAmount(rl.getDbInvoiceAmount());
         rcl.setStCheck("Y");
         rcl.setStAdvancePaymentFlag("Y");
         rcl.setStLock(true);

         rl.getDetails().add(rcl);

      receipt.recalculate();
   }
    
    public void setDate() throws Exception{
                
    }
    
    public void selectMonth(){
        
    }
    
    public void onDeleteAll() throws Exception {
      getListInvoices().deleteAll();
      //getListInvoices().delete(Integer.parseInt(invoicesindex));
      if(cashflow.getDetails().size()>0){
          cashflow.setStDescription("Cashflow "+ cashflow.getDetails().size()+ " " + getWording());
          cashflow.setStShortDescription("Cashflow "+ cashflow.getDetails().size()+ " " + getWording());
      }
   }

    public void reverse() throws Exception{
        
        getRemoteAccountReceivable().reverseCashflow(cashflow);

        super.close();
    }

    public void onNewKlaimByPolID() throws Exception{


        validatePolIDAlreadyIn(parinvoiceid);

        if(cashflow.getDtReceiptDate()==null)
            throw new RuntimeException("Tanggal bayar klaim belum diisi");

        //final ARInvoiceView invoice = getARInvoiceByAttrPolIDAndTrxTypeID(invoiceid,"12");
        final ARInvoiceView invoice = getARInvoiceByInvoiceIDAndTrxTypeID(invoiceid);
        final ARInvoiceView invoice2 = getARInvoiceByInvoiceIDAndTrxTypeIDNotPanjar(invoiceid);


        final InsurancePolicyView policy = getRemoteInsurance().getInsurancePolicy(parinvoiceid);

        DateTime tglApproved = new DateTime();
        DateTime tglBayar = new DateTime();
        DateTime tglAkhir = new DateTime();
        tglBayar = new DateTime(cashflow.getDtReceiptDate());
        //tglAkhir = tglBayar.dayOfMonth().withMaximumValue();
        tglAkhir = tglBayar;

        tglApproved = new DateTime(policy.getDtApprovedDate());
        tglApproved = tglApproved.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);

        boolean bolehBayarHutang = true;

        if (tglApproved.isAfter(tglAkhir)) {
            bolehBayarHutang = false;
        }

        if(invoice!=null){
            if(invoice.getDtReceipt()!=null)
                throw new RuntimeException("Data klaim sudah pernah dibayar");

            if(invoice.getStARInvoiceID()!=null){
                    parinvoiceid = invoice.getStARInvoiceID();
                    onNewInvoice();
                }

        }
    }

    private ARInvoiceView getARInvoiceByAttrPolIDAndTrxTypeID(String invoiceid, String trxtype) throws Exception {
        final ARInvoiceView iv = (ARInvoiceView) ListUtil.getDTOListFromQuery(
                "select * from ar_invoice where ar_invoice_id = ? and ar_trx_type_id = ? and coalesce(refid0,'') <> 'PANJAR'",
                new Object [] {invoiceid,trxtype},
                ARInvoiceView.class
                ).getDTO();

        if(iv!=null) {
            iv.setDetails(
                    ListUtil.getDTOListFromQuery(
                    "select a.* from ar_invoice_details a,ar_invoice b where a.ar_invoice_id = b.ar_invoice_id and b.ar_invoice_id = ? and b.ar_trx_type_id = ? and coalesce(b.refid0,'') <> 'PANJAR'",
                    new Object [] {invoiceid,trxtype},
                    ARInvoiceDetailView.class
                    )

                    );
        }

        return iv;
    }

    private ARInvoiceView getARInvoiceByInvoiceIDAndTrxTypeID(String invoiceid) throws Exception {
        final ARInvoiceView iv = (ARInvoiceView) ListUtil.getDTOListFromQuery(
                "select * from ar_invoice where ar_invoice_id = ? and coalesce(refid0,'') <> 'PANJAR'",
                new Object [] {invoiceid},
                ARInvoiceView.class
                ).getDTO();

        if(iv!=null) {
            iv.setDetails(
                    ListUtil.getDTOListFromQuery(
                    "select a.* from ar_invoice_details a,ar_invoice b where a.ar_invoice_id = b.ar_invoice_id and b.ar_invoice_id = ? and coalesce(b.refid0,'') <> 'PANJAR'",
                    new Object [] {invoiceid},
                    ARInvoiceDetailView.class
                    )

                    );
        }

        return iv;
    }

    private ARInvoiceView getARInvoiceByInvoiceIDAndTrxTypeIDNotPanjar(String invoiceid) throws Exception {
        final ARInvoiceView iv = (ARInvoiceView) ListUtil.getDTOListFromQuery(
                "select * from ar_invoice where ar_invoice_id = ?",
                new Object [] {invoiceid},
                ARInvoiceView.class
                ).getDTO();

        if(iv!=null) {
            iv.setDetails(
                    ListUtil.getDTOListFromQuery(
                    "select a.* from ar_invoice_details a,ar_invoice b where a.ar_invoice_id = b.ar_invoice_id and b.ar_invoice_id = ? ",
                    new Object [] {invoiceid},
                    ARInvoiceDetailView.class
                    )

                    );
        }

        return iv;
    }

     public boolean isPosted() throws Exception {

            SQLUtil S = new SQLUtil();

            boolean isPosted = false;

            try {
                String cek = "select gl_post_id from gl_posting where months = ? and years = ? and posted_flag = 'Y' and cc_code is null"+
                            " union "+
                            " select gl_post_id from gl_posting where months = ? and years = ? and posted_flag = 'Y'";

                if(cashflow.getStCostCenterCode()!=null)
                    cek = cek + " and cc_code = ?";

                PreparedStatement PS = S.setQuery(cek);
                PS.setString(1, cashflow.getStMonths());
                PS.setString(2, cashflow.getStYears());

                PS.setString(3, cashflow.getStMonths());
                PS.setString(4, cashflow.getStYears());

                if(cashflow.getStCostCenterCode()!=null)
                      PS.setString(5, cashflow.getStCostCenterCode());

                ResultSet RS = PS.executeQuery();

                if (RS.next()){
                    isPosted = true;
                }

            } finally {
                S.release();
            }

        return isPosted;
    }

     public Date getDtApplyDateFrom() {
        return dtApplyDateFrom;
    }

    /**
     * @param dtApplyDateFrom the dtApplyDateFrom to set
     */
    public void setDtApplyDateFrom(Date dtApplyDateFrom) {
        this.dtApplyDateFrom = dtApplyDateFrom;
    }

    /**
     * @return the dtApplyDateTo
     */
    public Date getDtApplyDateTo() {
        return dtApplyDateTo;
    }

    /**
     * @param dtApplyDateTo the dtApplyDateTo to set
     */
    public void setDtApplyDateTo(Date dtApplyDateTo) {
        this.dtApplyDateTo = dtApplyDateTo;
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

    /**
     * @return the stBranchDesc
     */
    public String getStBranchDesc() {
        return stBranchDesc;
    }

    /**
     * @param stBranchDesc the stBranchDesc to set
     */
    public void setStBranchDesc(String stBranchDesc) {
        this.stBranchDesc = stBranchDesc;
    }

    public void clickValidate() throws Exception {
        final DTOList l = receipt.getDetails();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        getRemoteInsurance().updateValidateClaim(l);

        onDeleteAll();

    }

    public void validatePolIDAlreadyIn(String arInvoiceID) throws Exception {
        final DTOList detail = cashflow.getDetails();

        String stARInvoiceID = "";
        for (int i = 0; i < detail.size(); i++) {
            ARCashflowDetailsView rl = (ARCashflowDetailsView) detail.get(i);

            stARInvoiceID = rl.getStInvoiceID();

            if (rl.getPolicy() != null) {
                InsurancePolicyView pol = rl.getPolicy();
                if (pol.getStPolicyID().equalsIgnoreCase(arInvoiceID)) {

                    String notif = pol.isStatusClaimDLA()?"LKP : "+pol.getStDLANo():"LKS : "+ pol.getStPLANo();

                    throw new RuntimeException("Policy ID " + arInvoiceID + " "+ notif +" Sudah Dipilih sebelumnya pada transaksi ini");
                }
            }
        }
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

    public void uploadClaim() throws Exception {

        final DTOList l = EXCEL_CLAIM();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        EXPORT_CLAIM();
    }

    public DTOList EXCEL_CLAIM() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.pol_id::text,a.pol_no,a.cc_code,a.claim_status,case when a.claim_status = 'DLA' then a.dla_no else a.pla_no end as dla_no,"
                + " a.claim_amount as claim_amount_approved,a.effective_flag,a.claim_effective_flag,a.active_flag,a.f_validate_claim ");

        sqa.addQuery(" from ins_policy a ");

        sqa.addClause("a.active_flag = 'Y'");
        sqa.addClause("a.status in ('CLAIM','CLAIM ENDORSE') ");
        sqa.addClause("a.claim_status in ('PLA','DLA') ");

        if (dtApplyDateFrom != null) {
            sqa.addClause("date_trunc('day',coalesce(a.dla_date,a.pla_date)) >= ?");
            sqa.addPar(dtApplyDateFrom);
        }

        if (dtApplyDateTo != null) {
            sqa.addClause("date_trunc('day',coalesce(a.dla_date,a.pla_date)) <= ?");
            sqa.addPar(dtApplyDateTo);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        final String sql = sqa.getSQL() + " order by a.pla_no,a.dla_no asc";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void EXPORT_CLAIM() throws Exception {

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int r = 0; r < list.size(); r++) {
            HashDTO h = (HashDTO) list.get(r);

            onNewKlaimByPolID2(h.getFieldValueByFieldNameST("pol_id"));

        }

        getReceipt().recalculate();

    }

    public void onNewKlaimByPolID2(String pol_id) throws Exception {


        validatePolIDAlreadyIn(pol_id);

        final InsurancePolicyView policy = getRemoteInsurance().getInsurancePolicy(pol_id);

        DateTime tglApproved = new DateTime();

        tglApproved = new DateTime(policy.getDtApprovedDate());
        tglApproved = tglApproved.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);

        parinvoiceid = policy.getStPolicyID();
        onNewUangMukaKlaim(null);

    }

    public void clickCancel() throws Exception {
        final DTOList l = receipt.getDetails();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        getRemoteInsurance().updateCancelValidateClaim(l);

        onDeleteAll();

    }

    public void uploadKonversi() throws Exception {
        
            uploadExcel();
    }


    public void uploadExcel() throws Exception {

        String fileID = getCashflow().getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheetPolis = wb.getSheet("KLAIM");

        int rows = sheetPolis.getPhysicalNumberOfRows();

        String alasan = "";
        boolean cekGagal = false;

        for (int r = 5; r <= rows; r++) {
            HSSFRow row = sheetPolis.getRow(r);

            HSSFCell cellControl = row.getCell(1);
            //if(cellControl==null) break;

            if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellNomorPolis = row.getCell(3);//nomor polis
            HSSFCell cellNoLKP = row.getCell(4);//entity id
            HSSFCell cellReceiptDate = row.getCell(5);//nomor polis

            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, getCashflow().getStARSettlementID());

            DTOList invoiceList = getARInvoiceByPolIdAndEntityID(cellNomorPolis.getStringCellValue(), stl.getStParameter1(), null, cellNoLKP.getStringCellValue());

            for (int i = 0; i < invoiceList.size(); i++) {
                ARInvoiceView inv = (ARInvoiceView) invoiceList.get(i);

                if (inv == null) {
                    alasan = alasan + "<br>Tagihan Polis " + cellNomorPolis.getStringCellValue() + " tidak ditemukan.";
                    cekGagal = true;
                    continue;
                }

                inv.setDtTransDate(cashflow.getDtReceiptDate());

                Date tglPembukuan = cashflow.getDtReceiptDate();
                DateTime tglApproved = new DateTime();
                DateTime tglBayar = new DateTime();
                DateTime tglAkhir = new DateTime();

                InsurancePolicyView polis = inv.getPolicy();

                if (polis != null) {
                    tglApproved = new DateTime(polis.getDtApprovedDate());
                } else {
                    tglApproved = new DateTime(inv.getDtDueDate());
                }

                tglBayar = new DateTime(tglPembukuan);
                tglAkhir = tglBayar.dayOfMonth().withMaximumValue();

                tglApproved = tglApproved.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);

                boolean bisaBayar = true;

                if (tglApproved.isAfter(tglAkhir)) {
                    //bisaBayar = false;
                    //cekGagal = true;
                    //alasan = alasan + "<br>Tanggal Persetujuan Polis " + cellNomorPolis.getStringCellValue() + " > Tanggal Pembukuan.";
                }

                if (inv.getDtReceipt() != null) {
                    bisaBayar = false;
                    cekGagal = true;
                    alasan = alasan + "<br>Polis " + cellNomorPolis.getStringCellValue() + " Sudah dibayar.";
                }

                if (inv.getStUsedFlag() != null) {
                    if (inv.getStUsedFlag().equalsIgnoreCase("Y")) {
                        bisaBayar = false;
                        cekGagal = true;
                        alasan = alasan + "<br>Polis " + cellNomorPolis.getStringCellValue() + " Sudah dibayar.";
                    }

                }

                if (bisaBayar) {
                    onNewInvoiceByInvoiceIDWithoutRecalculate(inv.getStARInvoiceID(), inv);
                }
            }

            //ARInvoiceView inv = getARInvoiceUsingPolNo(cellNomorPolis.getStringCellValue(), stl.getStParameter1());

        }

        getCashflow().recalculate();

        if (cekGagal) {
            throw new RuntimeException("Polis gagal konversi : " + alasan);
        }
    }

    public void uploadExcelRealisasi() throws Exception {

        String fileID = getReceipt().getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheetPolis = wb.getSheet("KLAIM");

        int rows = sheetPolis.getPhysicalNumberOfRows();

        String alasan = "";
        boolean cekGagal = false;

        for (int r = 5; r <= rows; r++) {
            HSSFRow row = sheetPolis.getRow(r);

            HSSFCell cellControl = row.getCell(1);
            //if(cellControl==null) break;

            if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellNomorPolis = row.getCell(3);//nomor polis
            HSSFCell cellNoLKP = row.getCell(4);//entity id
            HSSFCell cellReceiptDate = row.getCell(5);//nomor polis

            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, getReceipt().getStARSettlementID());

            DTOList invoiceList = getARInvoiceByPolIdAndEntityID(cellNomorPolis.getStringCellValue(), stl.getStParameter1(), null, cellNoLKP.getStringCellValue());

            for (int i = 0; i < invoiceList.size(); i++) {
                ARInvoiceView inv = (ARInvoiceView) invoiceList.get(i);

                if (inv == null) {
                    alasan = alasan + "<br>Tagihan Polis " + cellNomorPolis.getStringCellValue() + " tidak ditemukan.";
                    cekGagal = true;
                    continue;
                }

                inv.setDtTransDate(cellReceiptDate.getDateCellValue());

                Date tglPembukuan = receipt.getDtReceiptDate();
                DateTime tglApproved = new DateTime();
                DateTime tglBayar = new DateTime();
                DateTime tglAkhir = new DateTime();

                InsurancePolicyView polis = inv.getPolicy();

                if (polis != null) {
                    tglApproved = new DateTime(polis.getDtApprovedDate());
                } else {
                    tglApproved = new DateTime(inv.getDtDueDate());
                }

                tglBayar = new DateTime(tglPembukuan);
                tglAkhir = tglBayar.dayOfMonth().withMaximumValue();

                tglApproved = tglApproved.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);

                boolean bisaBayar = true;

                if (tglApproved.isAfter(tglAkhir)) {
                    bisaBayar = false;
                    cekGagal = true;
                    alasan = alasan + "<br>Tanggal Persetujuan Polis " + cellNomorPolis.getStringCellValue() + " > Tanggal Pembukuan.";
                }

                if (inv.getDtReceipt() != null) {
                    bisaBayar = false;
                    cekGagal = true;
                    alasan = alasan + "<br>Polis " + cellNomorPolis.getStringCellValue() + " Sudah dibayar.";
                }

                if (inv.getStUsedFlag() != null) {
                    if (inv.getStUsedFlag().equalsIgnoreCase("Y")) {
                        bisaBayar = false;
                        cekGagal = true;
                        alasan = alasan + "<br>Polis " + cellNomorPolis.getStringCellValue() + " Sudah dibayar.";
                    }

                }

                if (bisaBayar) {
                    onNewInvoiceByInvoiceIDWithoutRecalculateRealisasi(inv.getStARInvoiceID(), inv);
                }
            }

            //ARInvoiceView inv = getARInvoiceUsingPolNo(cellNomorPolis.getStringCellValue(), stl.getStParameter1());

        }

        getReceipt().recalculate();

        if (cekGagal) {
            throw new RuntimeException("Polis gagal konversi : " + alasan);
        }
    }

    public void uploadExcelKlaim() throws Exception{

        String fileID = getReceipt().getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheetPolis = wb.getSheet("KLAIM");

        int rows  = sheetPolis.getPhysicalNumberOfRows();

        String alasan = "";
        boolean cekGagal = false;

        for (int r = 5; r <= rows; r++){
            HSSFRow row   = sheetPolis.getRow(r);

            HSSFCell cellControl  = row.getCell(1);
            //if(cellControl==null) break;

            if(cellControl.getStringCellValue().equalsIgnoreCase("END")) break;

            HSSFCell cellNomorPolis  = row.getCell(3);//nomor polis
            HSSFCell cellNoLKP = row.getCell(4);//entity id
            HSSFCell cellEntityID = row.getCell(5);//entity id

            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, getReceipt().getStARSettlementID());

            DTOList invoiceList = getARInvoiceByPolId(cellNomorPolis.getStringCellValue(), stl.getStParameter1());

            if(cellEntityID!=null){
                String entID = cellEntityID.getCellType()==cellEntityID.CELL_TYPE_STRING?cellEntityID.getStringCellValue():new BigDecimal(cellEntityID.getNumericCellValue()).toString();
                if(!entID.equalsIgnoreCase("0")){
                    invoiceList = null;

                    invoiceList = getARInvoiceByPolIdAndEntityID(cellNomorPolis.getStringCellValue(), stl.getStParameter1(), entID, cellNoLKP.getStringCellValue());
                }
            }


            for (int i = 0; i < invoiceList.size(); i++) {
                ARInvoiceView inv = (ARInvoiceView) invoiceList.get(i);

                if(inv==null){
                    alasan = alasan + "<br>Tagihan Polis "+ cellNomorPolis.getStringCellValue() +" tidak ditemukan.";
                    cekGagal = true;
                    continue;
                }

                Date tglPembukuan = receipt.getDtReceiptDate();
                DateTime tglApproved = new DateTime();
                DateTime tglBayar = new DateTime();
                DateTime tglAkhir = new DateTime();

                InsurancePolicyView polis = inv.getPolicy();

                if(polis!=null)
                    tglApproved = new DateTime(polis.getDtApprovedDate());
                else
                    tglApproved = new DateTime(inv.getDtDueDate());

                tglBayar = new DateTime(tglPembukuan);
                tglAkhir = tglBayar.dayOfMonth().withMaximumValue();

                tglApproved = tglApproved.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);

                boolean bisaBayar = true;

                if (tglApproved.isAfter(tglAkhir)) {
                    bisaBayar = false;
                    cekGagal = true;
                    alasan = alasan + "<br>Tanggal Persetujuan Polis "+ cellNomorPolis.getStringCellValue() +" > Tanggal Pembukuan.";
                }

                if (inv.getDtReceipt() != null) {
                    bisaBayar = false;
                    cekGagal = true;
                    alasan = alasan + "<br>Polis "+ cellNomorPolis.getStringCellValue() +" Sudah dibayar.";
                }

                if(inv.getStUsedFlag()!=null){
                    if(inv.getStUsedFlag().equalsIgnoreCase("Y")){
                        bisaBayar = false;
                        cekGagal = true;
                        alasan = alasan + "<br>Polis "+ cellNomorPolis.getStringCellValue() +" Sudah dibayar.";
                    }

                }

                if(bisaBayar){
                    onNewInvoiceByInvoiceIDWithoutRecalculate(inv.getStARInvoiceID(), inv);
                }
            }

            //ARInvoiceView inv = getARInvoiceUsingPolNo(cellNomorPolis.getStringCellValue(), stl.getStParameter1());

        }

        getReceipt().recalculate();

        if(cekGagal){
            throw new RuntimeException("Polis gagal konversi : "+ alasan);
        }

    }

    public void uploadExcelOthers() throws Exception{

        String fileID = getReceipt().getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheetPolis = wb.getSheet("POLIS");

        int rows  = sheetPolis.getPhysicalNumberOfRows();

        String alasan = "";
        boolean cekGagal = false;

        for (int r = 5; r <= rows; r++){
            HSSFRow row   = sheetPolis.getRow(r);

            HSSFCell cellControl  = row.getCell(1);
            //if(cellControl==null) break;

            if(cellControl.getStringCellValue().equalsIgnoreCase("END")) break;

            HSSFCell cellNomorPolis  = row.getCell(3);//nomor polis
            HSSFCell cellEntityID = row.getCell(4);//nomor polis

            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, getReceipt().getStARSettlementID());

            DTOList invoiceList = getARInvoiceByPolId(cellNomorPolis.getStringCellValue(), stl.getStParameter1());

            if(cellEntityID!=null){
                String entID = cellEntityID.getCellType()==cellEntityID.CELL_TYPE_STRING?cellEntityID.getStringCellValue():new BigDecimal(cellEntityID.getNumericCellValue()).toString();
                if(!entID.equalsIgnoreCase("0")){
                    invoiceList = null;

                    invoiceList = getARInvoiceByPolIdAndEntityID(cellNomorPolis.getStringCellValue(), stl.getStParameter1(), entID, null);
                }

            }


            for (int i = 0; i < invoiceList.size(); i++) {
                ARInvoiceView inv = (ARInvoiceView) invoiceList.get(i);

                if(inv==null){
                    alasan = alasan + "<br>Tagihan Polis "+ cellNomorPolis.getStringCellValue() +" tidak ditemukan.";
                    cekGagal = true;
                    continue;
                }

                Date tglPembukuan = receipt.getDtReceiptDate();
                DateTime tglApproved = new DateTime();
                DateTime tglBayar = new DateTime();
                DateTime tglAkhir = new DateTime();

                InsurancePolicyView polis = inv.getPolicy();

                if(polis!=null)
                    tglApproved = new DateTime(polis.getDtApprovedDate());
                else
                    tglApproved = new DateTime(inv.getDtDueDate());

                tglBayar = new DateTime(tglPembukuan);
                tglAkhir = tglBayar.dayOfMonth().withMaximumValue();

                tglApproved = tglApproved.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);

                boolean bisaBayar = true;

                if (tglApproved.isAfter(tglAkhir)) {
                    bisaBayar = false;
                    cekGagal = true;
                    alasan = alasan + "<br>Tanggal Persetujuan Polis "+ cellNomorPolis.getStringCellValue() +" > Tanggal Pembukuan.";
                }

                if (inv.getDtReceipt() != null) {
                    bisaBayar = false;
                    cekGagal = true;
                    alasan = alasan + "<br>Polis "+ cellNomorPolis.getStringCellValue() +" Sudah dibayar.";

                }

                if(inv.getStUsedFlag()!=null){
                    if(inv.getStUsedFlag().equalsIgnoreCase("Y") && inv.getDtReceipt() == null){
                        bisaBayar = false;
                        cekGagal = true;
                        alasan = alasan + "<br>Polis "+ cellNomorPolis.getStringCellValue() +" Sudah di entry di no bukti lain.";
                    }

                }

                if(bisaBayar){
                    onNewInvoiceByInvoiceIDWithoutRecalculate(inv.getStARInvoiceID(), inv);
                }
            }

            //ARInvoiceView inv = getARInvoiceUsingPolNo(cellNomorPolis.getStringCellValue(), stl.getStParameter1());

        }

        getReceipt().recalculate();

        if(cekGagal){
            throw new RuntimeException("Polis gagal konversi : "+ alasan);
        }

    }

    public DTOList getARInvoiceByPolId(String attrpolid, String param) throws Exception {

       String sql = "select * from ar_invoice a where cc_code = ? and attr_pol_no = ?";

       sql = sql + " and " + param;

       sql = sql + " and amount_settled is null ";

       if(receipt.getStARSettlementID().equalsIgnoreCase("13") || receipt.getStARSettlementID().equalsIgnoreCase("14")){
           sql = sql + " and a.ent_id not in (select refid1 from ent_master_coas)";
       }

       //sql = sql + " limit 1";

        return ListUtil.getDTOListFromQuery(
                    sql ,
                    new Object [] {receipt.getStCostCenterCode(), attrpolid},
                    ARInvoiceView.class
                    );
    }

    public DTOList getARInvoiceByPolIdAndEntityID(String attrpolid, String param,String entityID, String noLKP) throws Exception {

       String sql = "select * from ar_invoice a where attr_pol_no = ?";

       sql = sql + " and " + param;

       sql = sql + " and amount is not null and amount_settled is null ";

       if(entityID!=null){
           sql = sql + " and ent_id = "+ entityID +"";
       }

       if(noLKP!=null){
           sql = sql + " and refid2 = '"+ noLKP.trim().toUpperCase() +"'";
       }

       return ListUtil.getDTOListFromQuery(
                    sql ,
                    new Object [] {attrpolid},
                    ARInvoiceView.class
                    );
    }

    public void onNewInvoiceByInvoiceIDWithoutRecalculate(String invoice_id, ARInvoiceView invoiceParam) throws Exception {

      validateInvoiceAlreadyIn(invoice_id);

      final ARInvoiceView invoice = invoiceParam;

      final ARCashflowDetailsView rcl = new ARCashflowDetailsView();
      rcl.markNew();

      rcl.setStInvoiceID(invoice.getStARInvoiceID());
      rcl.setStInvoiceNo(invoice.getStInvoiceNo());
      rcl.setStCurrencyCode(invoice.getStCurrencyCode());
      rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
      rcl.setDbInvoiceAmount(invoice.getDbOutstandingAmount());
      rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
      rcl.setDtReceiptDate(invoice.getDtTransDate());
      rcl.setStPolicyID(invoice.getStAttrPolicyID());
      
      if(invoice.getStRefID2()!=null)
            rcl.setStArInvoiceClaim(invoice.getStRefID2());


      //rcl.setStNegativeFlag(d.getStNegativeFlag());
      rcl.markAsInvoice();
      //rcl.markCommit();

      cashflow.setStEntityID(invoice.getStEntityID());

      if (cashflow.getStDescription()==null) cashflow.setStDescription("Cashflow : "+rcl.getStInvoiceNo());
      if (cashflow.getStShortDescription()==null) cashflow.setStShortDescription("Cashflow : "+rcl.getStInvoiceNo());

      cashflow.getDetails().add(rcl);

      if(cashflow.getDetails().size()>1){
          cashflow.setStDescription("Cashflow "+cashflow.getDetails().size()+ " Klaim");
          cashflow.setStShortDescription("Cashflow "+cashflow.getDetails().size()+ " Klaim");
      }

      invoicesindex=String.valueOf(cashflow.getDetails().size()-1);

      onExpandInvoiceItem();

   }

    public void onNewInvoiceByInvoiceIDWithoutRecalculateRealisasi(String invoice_id, ARInvoiceView invoiceParam) throws Exception {

      validateInvoiceAlreadyIn(invoice_id);

      final ARInvoiceView invoice = invoiceParam;

      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();

      rcl.setStInvoiceID(invoice.getStARInvoiceID());
      rcl.setStInvoiceNo(invoice.getStInvoiceNo());
      rcl.setStCurrencyCode(invoice.getStCurrencyCode());
      rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
      rcl.setDbInvoiceAmount(invoice.getDbOutstandingAmount());
      rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
      rcl.setDtReceiptDate(invoice.getDtTransDate());
      rcl.setStPolicyID(invoice.getStAttrPolicyID());


      //rcl.setStNegativeFlag(d.getStNegativeFlag());
      rcl.markAsInvoice();
      //rcl.markCommit();

      receipt.setStEntityID(invoice.getStEntityID());

      if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());
      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());

      receipt.getDetails().add(rcl);

      if(receipt.getDetails().size()>1){
          receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " Polis");
          receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " Polis");
      }

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandInvoiceItemWithoutRecalculate();

      tambahUangMukaKlaim();
      
   }

    public void onExpandInvoiceItemWithoutRecalculate() throws Exception {
      final ARReceiptLinesView rl = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

      final ARInvoiceView invoice = rl.getInvoice();

      rl.setStExpandedFlag("Y");

      final DTOList details = invoice.getDetails();

      for (int i = 0; i < details.size(); i++) {
         ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);

         //if (!d.isComission()) continue;

         final ARReceiptLinesView rcl = new ARReceiptLinesView();
         rcl.markNew();

         rcl.setStInvoiceID(invoice.getStARInvoiceID());
         rcl.setStInvoiceNo(invoice.getStInvoiceNo());
         rcl.setStInvoiceDetailID(d.getStARInvoiceDetailID());
         rcl.setStDescription(d.getStDescription());
         rcl.setStCurrencyCode(invoice.getStCurrencyCode());
         rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
         rcl.setDtReceiptDate(receipt.getDtReceiptDate());
         rcl.setStPolicyID(invoice.getStPolicyID());

         rcl.setDbInvoiceAmount(d.getDbEnteredAmount());
         rcl.setDbAmount(d.getDbOustandingAmount());
         rcl.setDbEnteredAmount(d.getDbOustandingAmount());

         boolean isTax = d.isTaxComm() || d.isTaxBrok() || d.isTaxHFee();
         if(!isTax) rcl.setStCheck("Y");

         rcl.setStLock(true);

         if(lockCheck(rcl)){
         	 rcl.setStLock(false);
         }

         if (d.isComission()) rcl.markAsComission();

         rl.getDetails().add(rcl);
      }

   }

    public boolean lockCheck(ARReceiptLinesView rclSub){
        return rclSub.getInvoiceDetail().isCommission2()||rclSub.getInvoiceDetail().isBrokerage2()
                    ||rclSub.getInvoiceDetail().isHandlingFee2();

                    //||rclSub.getInvoiceDetail().isTaxBrok()
                    //||rclSub.getInvoiceDetail().isTaxComm()||rclSub.getInvoiceDetail().isTaxHFee();
    }

    public void uploadExcelUangMukaKlaim() throws Exception{

        String fileID = getReceipt().getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheetPolis = wb.getSheet("KLAIM");

        int rows  = sheetPolis.getPhysicalNumberOfRows();

        String alasan = "";
        boolean cekGagal = false;

        for (int r = 5; r <= rows; r++){
            HSSFRow row   = sheetPolis.getRow(r);

            HSSFCell cellControl  = row.getCell(1);
            //if(cellControl==null) break;

            if(cellControl.getStringCellValue().equalsIgnoreCase("END")) break;

            HSSFCell cellNomorPolis  = row.getCell(3);//nomor polis
            HSSFCell cellNoLKP = row.getCell(4);//entity id
            HSSFCell cellEntityID = row.getCell(5);//entity id

            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, getReceipt().getStARSettlementID());

            DTOList invoiceList = getARInvoiceByPolIdAndEntityID(cellNomorPolis.getStringCellValue(), stl.getStParameter1(), null, cellNoLKP.getStringCellValue());

            for (int i = 0; i < invoiceList.size(); i++) {
                ARInvoiceView inv = (ARInvoiceView) invoiceList.get(i);

                if(inv==null){
                    alasan = alasan + "<br>Tagihan Polis "+ cellNomorPolis.getStringCellValue() +" tidak ditemukan.";
                    cekGagal = true;
                    continue;
                }

                Date tglPembukuan = receipt.getDtReceiptDate();
                DateTime tglApproved = new DateTime();
                DateTime tglBayar = new DateTime();
                DateTime tglAkhir = new DateTime();

                InsurancePolicyView polis = inv.getPolicy();

                if(polis!=null)
                    tglApproved = new DateTime(polis.getDtApprovedDate());
                else
                    tglApproved = new DateTime(inv.getDtDueDate());

                tglBayar = new DateTime(tglPembukuan);
                tglAkhir = tglBayar.dayOfMonth().withMaximumValue();

                tglApproved = tglApproved.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);

                boolean bisaBayar = true;

                if (tglApproved.isAfter(tglAkhir)) {
                    bisaBayar = false;
                    cekGagal = true;
                    alasan = alasan + "<br>Tanggal Persetujuan Polis "+ cellNomorPolis.getStringCellValue() +" > Tanggal Pembukuan.";
                }

                if (inv.getDtReceipt() != null) {
                    bisaBayar = false;
                    cekGagal = true;
                    alasan = alasan + "<br>Polis "+ cellNomorPolis.getStringCellValue() +" Sudah dibayar.";
                }

                if(inv.getStUsedFlag()!=null){
                    if(inv.getStUsedFlag().equalsIgnoreCase("Y")){
                        bisaBayar = false;
                        cekGagal = true;
                        alasan = alasan + "<br>Polis "+ cellNomorPolis.getStringCellValue() +" Sudah dibayar.";
                    }

                }

                if(bisaBayar){
                    onNewInvoiceByInvoiceIDWithoutRecalculate(inv.getStARInvoiceID(), inv);
                }
            }

            //ARInvoiceView inv = getARInvoiceUsingPolNo(cellNomorPolis.getStringCellValue(), stl.getStParameter1());

        }

        getReceipt().recalculate();

        if(cekGagal){
            throw new RuntimeException("Polis gagal konversi : "+ alasan);
        }

    }

    public void onNewUangMukaKlaimNew() throws Exception {

      //validateInvoiceAlreadyIn(parinvoiceid);

      //final ARInvoiceView invoice = getRemoteAccountReceivable().get(parinvoiceid);
      final InsurancePolicyView policy = getRemoteInsurance().getInsurancePolicy(parinvoiceid);

      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();

      String invoice_no = policy.isStatusClaimDLA()?policy.getStDLANo():policy.getStPLANo();
      BigDecimal amt = BDUtil.mul(policy.getDbClaimAdvancePaymentAmount(), policy.getDbCurrencyRate());//policy.isStatusClaimDLA()?policy.getDbClaimAmountApproved():policy.getDbClaimAmountEstimate();
      //Date dt = policy.isStatusClaimDLA()?policy.getDtDLADate():policy.getDtPLADate();

      //rcl.setStInvoiceID(invoice.getStARInvoiceID());
      rcl.setStInvoiceNo(policy.getStPolicyNo());
      rcl.setStArInvoiceClaim(invoice_no);
      rcl.setStCurrencyCode(policy.getStCurrencyCode());
      rcl.setDbCurrencyRate(policy.getDbCurrencyRate());
      rcl.setDbInvoiceAmount(amt);
      rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
      rcl.setDtReceiptDate(receipt.getDtReceiptDate());
      rcl.setStPolicyID(policy.getStPolicyID());
      rcl.setStAdvancePaymentFlag("Y");

      rcl.markAsInvoice();

      receipt.setStEntityID(policy.getStEntityID());

      if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());
      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());

      receipt.getDetails().add(rcl);

      if(receipt.getDetails().size()>1){
          receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " " + getWording());
          receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " " + getWording());
      }

      receipt.recalculate();

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandUangMuka();
   }

    public void tambahUangMukaKlaim() throws Exception {
      final ARReceiptLinesView rl = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

      //final ARInvoiceView invoice = rl.getInvoice();

      rl.setStExpandedFlag("Y");

      final ARReceiptLinesView rcl = new ARReceiptLinesView();
         rcl.markNew();

         //rcl.setStInvoiceID(invoice.getStARInvoiceID());
         rcl.setStInvoiceNo(rl.getStInvoiceNo());
         rcl.setStArInvoiceClaim(rl.getStArInvoiceClaim());
         //rcl.setStInvoiceDetailID(d.getStARInvoiceDetailID());
         rcl.setStDescription("Uang Muka Klaim");
         rcl.setStCurrencyCode(rl.getStCurrencyCode());
         rcl.setDbCurrencyRate(rl.getDbCurrencyRate());
         rcl.setDtReceiptDate(rl.getDtReceiptDate());
         rcl.setStPolicyID(rl.getStPolicyID());

         //BigDecimal jumlahPanjar = BDUtil.mul(rl.getPolicy().getDbClaimAdvancePaymentAmount(), rl.getPolicy().getDbCurrencyRate());
         BigDecimal jumlahPanjar = rl.getPolicy().getDbClaimAdvancePaymentAmount();

         rcl.setDbInvoiceAmount(jumlahPanjar);
         rcl.setDbAmount(rcl.getDbInvoiceAmount());
         rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
         rcl.setStCheck("Y");
         rcl.setStAdvancePaymentFlag("Y");
         rcl.setStLock(true);
         rcl.setStNegativeFlag("Y");

         rl.getDetails().add(rcl);

      receipt.recalculate();
   }

    public void onNewInvoiceUangMukaKlaim() throws Exception {

      validateInvoiceAlreadyIn(parinvoiceid);

      final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(parinvoiceid);

      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();

      rcl.setStInvoiceID(invoice.getStARInvoiceID());
      rcl.setStInvoiceNo(invoice.getStInvoiceNo());
      rcl.setStCurrencyCode(invoice.getStCurrencyCode());
      rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
      rcl.setDbInvoiceAmount(invoice.getDbOutstandingAmount());
      rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
      rcl.setDtReceiptDate(receipt.getDtReceiptDate());
      rcl.setStPolicyID(invoice.getStAttrPolicyID());
      rcl.setStArInvoiceClaim(invoice.getStRefID2());

      //rcl.setStNegativeFlag(d.getStNegativeFlag());
      rcl.markAsInvoice();
      //rcl.markCommit();

      receipt.setStEntityID(invoice.getStEntityID());

      if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());
      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());

      receipt.getDetails().add(rcl);

      if(receipt.getDetails().size()>1){
          receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " " + getWording());
          receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " " + getWording());
      }

      receipt.recalculate();

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandInvoiceItem();

      tambahUangMukaKlaim();
   }

    public void onNewKlaimValidateByPolID() throws Exception {

        validatePolIDAlreadyIn(parinvoiceid);

        final ARInvoiceView invoice = getARInvoiceByAttrPolIDAndTrxTypeID(invoiceid, "12");
        final InsurancePolicyView policy = getRemoteInsurance().getInsurancePolicy(parinvoiceid);

        DateTime tglApproved = new DateTime();
        DateTime tglBayar = new DateTime();
        DateTime tglAkhir = new DateTime();
        tglBayar = new DateTime(receipt.getDtReceiptDate());
        tglAkhir = tglBayar.dayOfMonth().withMaximumValue();

        tglApproved = new DateTime(policy.getDtApprovedDate());
        tglApproved = tglApproved.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);

        boolean bolehBayarHutang = true;

        if (tglApproved.isAfter(tglAkhir)) {
            bolehBayarHutang = false;
        }

        if (invoice != null) {
            if (bolehBayarHutang) {
                if (invoice.getStARInvoiceID() != null) {
                    parinvoiceid = invoice.getStARInvoiceID();
                    onNewInvoice();
                }
            } else {
                parinvoiceid = policy.getStPolicyID();
                onNewUangMukaKlaim(null);
            }
        } else {
            parinvoiceid = policy.getStPolicyID();
            onNewUangMukaKlaim(null);
        }

    }


    private ARInvoiceView getARInvoiceByAttrPolIDAndTrxTypeIDClaim(String invoiceid, String trxtype) throws Exception {
        final ARInvoiceView iv = (ARInvoiceView) ListUtil.getDTOListFromQuery(
                "select * from ar_invoice where ar_invoice_id = ? and ar_trx_type_id in (12,26) and coalesce(refid0,'') <> 'PANJAR'",
                new Object [] {invoiceid},
                ARInvoiceView.class
                ).getDTO();

        if(iv!=null) {
            iv.setDetails(
                    ListUtil.getDTOListFromQuery(
                    "select a.* from ar_invoice_details a,ar_invoice b where a.ar_invoice_id = b.ar_invoice_id and b.ar_invoice_id = ? and b.ar_trx_type_id in (12,26) and coalesce(b.refid0,'') <> 'PANJAR'",
                    new Object [] {invoiceid},
                    ARInvoiceDetailView.class
                    )

                    );
        }

        return iv;
    }

    public void cekClosingStatus(String status) throws Exception{

        final boolean blockClosingEndOfDay = Parameter.readBoolean("FINANCE_CLOSING_END_OF_DAY");

        if(blockClosingEndOfDay){

                final ClosingDetailView cls = PeriodManager.getInstance().getClosing(DateUtil.getYear(DateUtil.getNewDate()), receipt.getStCostCenterCode());

                if(cls!=null){

                        if(status.equalsIgnoreCase("PROPOSAL"))
                            if(cls==null){
                                //policy.setStCostCenterCode(null);
                                throw new RuntimeException("Tabel setting closing belum diisi");
                            }

                        if(cls==null)
                            throw new RuntimeException("Tabel setting closing belum diisi");

                        DateTimeZone timeZoneWIB = DateTimeZone.forID( "Asia/Bangkok" );

                        String batasJamInput [] = cls.getStEditEndTime().split("[\\:]");
                        int jam = Integer.parseInt(batasJamInput[0]);
                        int menit = Integer.parseInt(batasJamInput[1]);

                        String batasJamSetujui [] = cls.getStReverseEndTime().split("[\\:]");
                        int jam2 = Integer.parseInt(batasJamSetujui[0]);
                        int menit2 = Integer.parseInt(batasJamSetujui[1]);

                        DateTime dtBatasInput = new DateTime().withZone(timeZoneWIB).withTime(jam, menit, 0, 0);
                        DateTime dtBatasReverseSetujui = new DateTime().withZone(timeZoneWIB).withTime(jam2, menit2, 0, 0);
                        DateTime now = new DateTime().withZone(timeZoneWIB);

                        logger.logDebug("########### new date : "+ DateUtil.getNewDate());
                        logger.logDebug("########### batas input : "+ dtBatasInput);
                        logger.logDebug("########### batas reverse : "+ dtBatasReverseSetujui);
                        logger.logDebug("########### now : "+ now);

                        if(status.equalsIgnoreCase("INPUT"))
                            if(dtBatasInput.isBefore(now)){
                                receipt.setStCostCenterCode(null);
                                throw new RuntimeException("Tidak bisa input data karena sudah lewat batas jam input "+ DateUtil.getDateTimeStr2(dtBatasInput.toDate()));
                            }

                        if(status.equalsIgnoreCase("APPROVE"))
                            if(dtBatasReverseSetujui.isBefore(now))
                                throw new RuntimeException("Tidak bisa setujui data karena sudah lewat batas jam validasi "+ DateUtil.getDateTimeStr2(dtBatasReverseSetujui.toDate()));

                        if(status.equalsIgnoreCase("REVERSE"))
                            if(dtBatasReverseSetujui.isBefore(now))
                                throw new RuntimeException("Tidak bisa reverse data karena sudah lewat batas jam validasi "+ DateUtil.getDateTimeStr2(dtBatasReverseSetujui.toDate()));
                }

                

        }


    }

    public void onNewLainnya() throws Exception {

      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();

      rcl.setStCurrencyCode(receipt.getStCurrencyCode());
      rcl.setDbCurrencyRate(receipt.getDbCurrencyRate());
      rcl.setDtReceiptDate(receipt.getDtReceiptDate());

      rcl.markAsInvoice();

      if (receipt.getStDescription()==null) receipt.setStDescription("Realisasi Uang Muka Klaim Lainnya");
      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("Realisasi Uang Muka Klaim Lainnya");


      receipt.getDetails().add(rcl);

      receipt.recalculate();

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandLainnya();
   }

    public void onExpandLainnya() throws Exception {
      final ARReceiptLinesView rl = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

      rl.setStExpandedFlag("Y");

      final ARReceiptLinesView rcl = new ARReceiptLinesView();
         rcl.markNew();

         rcl.setStInvoiceNo(rl.getStInvoiceNo());
         rcl.setStArInvoiceClaim(rl.getStArInvoiceClaim());
         rcl.setStDescription("Realisasi Panjar Klaim Lainnya");
         rcl.setStCurrencyCode(rl.getStCurrencyCode());
         rcl.setDbCurrencyRate(rl.getDbCurrencyRate());
         rcl.setDtReceiptDate(rl.getDtReceiptDate());
         rcl.setStPolicyID(rl.getStPolicyID());

         rcl.setDbInvoiceAmount(rl.getDbInvoiceAmount());
         rcl.setDbAmount(rl.getDbInvoiceAmount());
         rcl.setDbEnteredAmount(rl.getDbInvoiceAmount());
         rcl.setStCheck("Y");
         rcl.setStOtherFlag("Y");
         rcl.setStLock(true);

         rl.getDetails().add(rcl);

      receipt.recalculate();
   }

    /**
     * @return the canCreateOthers
     */
    public boolean isCanCreateOthers() {
        return canCreateOthers;
    }

    /**
     * @param canCreateOthers the canCreateOthers to set
     */
    public void setCanCreateOthers(boolean canCreateOthers) {
        this.canCreateOthers = canCreateOthers;
    }

    public void onNewKlaimByPolID3(String parinvoiceid, String invoiceid) throws Exception {

        validatePolIDAlreadyIn(parinvoiceid);

        if (receipt.getDtReceiptDate() == null) {
            throw new RuntimeException("Tanggal bayar klaim belum diisi");

            //final ARInvoiceView invoice = getARInvoiceByAttrPolIDAndTrxTypeID(invoiceid,"12");

        }
        final ARInvoiceView invoice = getARInvoiceByInvoiceIDAndTrxTypeID(invoiceid);
        final ARInvoiceView invoice2 = getARInvoiceByInvoiceIDAndTrxTypeIDNotPanjar(invoiceid);

        final InsurancePolicyView policy = getRemoteInsurance().getInsurancePolicy(parinvoiceid);

        DateTime tglApproved = new DateTime();
        DateTime tglBayar = new DateTime();
        DateTime tglAkhir = new DateTime();
        tglBayar = new DateTime(receipt.getDtReceiptDate());
        //tglAkhir = tglBayar.dayOfMonth().withMaximumValue();
        tglAkhir = tglBayar;

        tglApproved = new DateTime(policy.getDtApprovedDate());
        tglApproved = tglApproved.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);

        boolean bolehBayarHutang = true;

        if (tglApproved.isAfter(tglAkhir)) {
            bolehBayarHutang = false;
        }

        if (invoice != null) {
            if (invoice.getDtReceipt() != null) {
                throw new RuntimeException("Data klaim sudah pernah dibayar");
            }
            if (bolehBayarHutang) {
                if (invoice.getStARInvoiceID() != null) {
                    parinvoiceid = invoice.getStARInvoiceID();
                    onNewInvoice();
                }
            } else {
                BigDecimal amount = invoice.getTagihanNetto();
                parinvoiceid = policy.getStPolicyID();
                onNewUangMukaKlaim2(parinvoiceid, amount);
            }
        } else {
            parinvoiceid = policy.getStPolicyID();

            if (policy.getDbClaimAdvancePaymentAmount() != null) {
                throw new RuntimeException("Data klaim " + policy.getStPLANo() + " sudah pernah di panjar sebelumnya");
            }
            if (invoice2 != null) {
                BigDecimal amount2 = invoice2.getTagihanNetto();
                onNewUangMukaKlaim2(parinvoiceid, amount2);
            } else {
                onNewUangMukaKlaim2(parinvoiceid, null);
            }
        }
    }

    public void onNewUangMukaKlaim2(String parinvoiceid, BigDecimal amount) throws Exception {

        //validateInvoiceAlreadyIn(parinvoiceid);

        //final ARInvoiceView invoice = getRemoteAccountReceivable().get(parinvoiceid);
        final InsurancePolicyView policy = getRemoteInsurance().getInsurancePolicy(parinvoiceid);

        final ARReceiptLinesView rcl = new ARReceiptLinesView();
        rcl.markNew();

        String invoice_no = policy.isStatusClaimDLA() ? policy.getStDLANo() : policy.getStPLANo();
        BigDecimal amt = BDUtil.mul(policy.getDbClaimAmount(), policy.getDbCurrencyRate());//policy.isStatusClaimDLA()?policy.getDbClaimAmountApproved():policy.getDbClaimAmountEstimate();
        //Date dt = policy.isStatusClaimDLA()?policy.getDtDLADate():policy.getDtPLADate();

        if (!BDUtil.isZeroOrNull(amount)) {
            amt = amount;

            //rcl.setStInvoiceID(invoice.getStARInvoiceID());

        }
        rcl.setStInvoiceNo(policy.getStPolicyNo());
        rcl.setStArInvoiceClaim(invoice_no);
        rcl.setStCurrencyCode(policy.getStCurrencyCode());
        rcl.setDbCurrencyRate(policy.getDbCurrencyRate());
        rcl.setDbInvoiceAmount(amt);
        rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
        rcl.setDtReceiptDate(receipt.getDtReceiptDate());
        rcl.setStPolicyID(policy.getStPolicyID());
        rcl.setStAdvancePaymentFlag("Y");

        rcl.markAsInvoice();

        receipt.setStEntityID(policy.getStEntityID());

        if (receipt.getStDescription() == null) {
            receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: " + rcl.getStInvoiceNo());

        }
        if (receipt.getStShortDescription() == null) {
            receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: " + rcl.getStInvoiceNo());


        }
        receipt.getDetails().add(rcl);

        if (receipt.getDetails().size() > 1) {
            receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} " + receipt.getDetails().size() + " " + getWording());
            receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} " + receipt.getDetails().size() + " " + getWording());
        }

        receipt.recalculate();

        invoicesindex = String.valueOf(receipt.getDetails().size() - 1);

        onExpandUangMuka();
    }


      public void onNewInvoiceBySuratHutangClaim(String nosurathutang) throws Exception {
        final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(parinvoiceid);

        final ARReceiptLinesView rcl = new ARReceiptLinesView();
        rcl.markNew();

        rcl.setStInvoiceID(invoice.getStARInvoiceID());
        rcl.setStInvoiceNo(invoice.getStInvoiceNo());
        rcl.setStCurrencyCode(invoice.getStCurrencyCode());
        rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
        rcl.setDbInvoiceAmount(invoice.getDbOutstandingAmount());
        rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
        rcl.setDtReceiptDate(receipt.getDtReceiptDate());
        rcl.setStPolicyID(invoice.getStAttrPolicyID());
        rcl.markAsInvoice();
        rcl.markCommit();
        /*
        receipt.setStEntityID(invoice.getStEntityID());

        if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+claimno);
        if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+claimno);
         */
        receipt.getDetails().add(rcl);

        receipt.recalculate();

        invoicesindex = String.valueOf(receipt.getDetails().size() - 1);

        onExpandInvoiceItemPembayaranClaim();
    }

      public void onExpandInvoiceItemPembayaranClaim() throws Exception {
        final ARReceiptLinesView rl = (ARReceiptLinesView) getListInvoices().get(Integer.parseInt(invoicesindex));

        final ARInvoiceView invoice = rl.getInvoice();

        rl.setStExpandedFlag("Y");

        final DTOList details = invoice.getDetails();

        for (int i = 0; i < details.size(); i++) {
            ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);

            //if (!d.isComission()) continue;

            final ARReceiptLinesView rcl = new ARReceiptLinesView();
            rcl.markNew();

            rcl.setStInvoiceID(invoice.getStARInvoiceID());
            rcl.setStInvoiceNo(invoice.getStInvoiceNo());
            rcl.setStInvoiceDetailID(d.getStARInvoiceDetailID());
            rcl.setStDescription(d.getStDescription());
            rcl.setStCurrencyCode(invoice.getStCurrencyCode());
            rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
            rcl.setDtReceiptDate(receipt.getDtReceiptDate());
            rcl.setStPolicyID(invoice.getStPolicyID());

            rcl.setDbInvoiceAmount(d.getDbEnteredAmount());
            rcl.setDbAmount(d.getDbOustandingAmount());
            rcl.setDbEnteredAmount(d.getDbOustandingAmount());

            rl.getDetails().add(rcl);
        }
        receipt.recalculateKlaim();
    }

      public void onNewKlaimByPolID4(String parinvoiceid, String invoiceid) throws Exception {

        validatePolIDAlreadyIn(parinvoiceid);

        if (receipt.getDtReceiptDate() == null) {
            throw new RuntimeException("Tanggal bayar klaim belum diisi");

            //final ARInvoiceView invoice = getARInvoiceByAttrPolIDAndTrxTypeID(invoiceid,"12");

        }
        final ARInvoiceView invoice = getARInvoiceByInvoiceIDAndTrxTypeID(invoiceid);
        final ARInvoiceView invoice2 = getARInvoiceByInvoiceIDAndTrxTypeIDNotPanjar(invoiceid);

        final InsurancePolicyView policy = getRemoteInsurance().getInsurancePolicy(parinvoiceid);

        DateTime tglApproved = new DateTime();
        DateTime tglBayar = new DateTime();
        DateTime tglAkhir = new DateTime();
        tglBayar = new DateTime(receipt.getDtReceiptDate());
        //tglAkhir = tglBayar.dayOfMonth().withMaximumValue();
        tglAkhir = tglBayar;

        tglApproved = new DateTime(policy.getDtApprovedDate());
        tglApproved = tglApproved.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);

        boolean bolehBayarHutang = true;

        if (tglApproved.isAfter(tglAkhir)) {
            bolehBayarHutang = false;
        }

        if (invoice != null) {
            if (invoice.getDtReceipt() != null) {
                throw new RuntimeException("Data klaim sudah pernah dibayar");
            }
            if (bolehBayarHutang) {
                if (invoice.getStARInvoiceID() != null) {
                    parinvoiceid = invoice.getStARInvoiceID();
                    onNewInvoice();
                }
            } else {
                BigDecimal amount = invoice.getTagihanNetto();
                parinvoiceid = policy.getStPolicyID();
                onNewUangMukaKlaim3(parinvoiceid, amount);
            }
        } else {
            parinvoiceid = policy.getStPolicyID();

            if (policy.getDbClaimAdvancePaymentAmount() != null) {
                throw new RuntimeException("Data klaim " + policy.getStPLANo() + " sudah pernah di panjar sebelumnya");
            }
            if (invoice2 != null) {
                BigDecimal amount2 = invoice2.getTagihanNetto();
                onNewUangMukaKlaim3(parinvoiceid, amount2);
            } else {
                onNewUangMukaKlaim3(parinvoiceid, null);
            }
        }
    }

    public void onNewUangMukaKlaim3(String parinvoiceid, BigDecimal amount) throws Exception {

        //validateInvoiceAlreadyIn(parinvoiceid);

        //final ARInvoiceView invoice = getRemoteAccountReceivable().get(parinvoiceid);
        final InsurancePolicyView policy = getRemoteInsurance().getInsurancePolicy(parinvoiceid);

        final ARReceiptLinesView rcl = new ARReceiptLinesView();
        rcl.markNew();

        String invoice_no = policy.isStatusClaimDLA() ? policy.getStDLANo() : policy.getStPLANo();
        BigDecimal amt = BDUtil.mul(policy.getDbClaimAmount(), policy.getDbCurrencyRate());//policy.isStatusClaimDLA()?policy.getDbClaimAmountApproved():policy.getDbClaimAmountEstimate();
        //Date dt = policy.isStatusClaimDLA()?policy.getDtDLADate():policy.getDtPLADate();

        if (!BDUtil.isZeroOrNull(amount)) {
            amt = amount;

            //rcl.setStInvoiceID(invoice.getStARInvoiceID());

        }
        rcl.setStInvoiceNo(policy.getStPolicyNo());
        rcl.setStArInvoiceClaim(invoice_no);
        rcl.setStCurrencyCode(policy.getStCurrencyCode());
        rcl.setDbCurrencyRate(policy.getDbCurrencyRate());
        rcl.setDbInvoiceAmount(amt);
        rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
        rcl.setDtReceiptDate(receipt.getDtReceiptDate());
        rcl.setStPolicyID(policy.getStPolicyID());
        rcl.setStAdvancePaymentFlag("Y");

        rcl.markAsInvoice();

        receipt.setStEntityID(policy.getStEntityID());

        if (receipt.getStDescription() == null) {
            receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: " + rcl.getStInvoiceNo());

        }
        if (receipt.getStShortDescription() == null) {
            receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: " + rcl.getStInvoiceNo());


        }
        receipt.getDetails().add(rcl);

        if (receipt.getDetails().size() > 1) {
            receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} " + receipt.getDetails().size() + " " + getWording());
            receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} " + receipt.getDetails().size() + " " + getWording());
        }

        receipt.recalculate();

        invoicesindex = String.valueOf(receipt.getDetails().size() - 1);

        onExpandUangMuka();
    }

    public void onNewSuratHutangClaim() throws Exception {
        final ARInvoiceView invoice = getRemoteAccountReceivable().getSuratHutang(nosurathutang);

        invoice.setStNoSuratHutang(nosurathutang);

        descLong = descLong + "\n" + nosurathutang;

        final DTOList listInv = invoice.getListDetailsSuratHutangComm();

        for (int i = 0; i < listInv.size(); i++) {
            ARInvoiceView invoiceView = (ARInvoiceView) listInv.get(i);
            parinvoiceid = invoiceView.getStARInvoiceID();
//            onNewInvoiceBySuratHutangClaim(nosurathutang);
            onNewKlaimByPolID4(invoiceView.getStAttrPolicyID(), invoiceView.getStARInvoiceID());
        }

        receipt.setStDescription("Pembayaran " + descLong);
        receipt.setStShortDescription(receipt.getStDescription());

    }



    public void onNewSuratHutangIzinCair() throws Exception {
        ARInvoiceView invoice = new ARInvoiceView();
        invoice.setStNoIzinCair(noizincair);
//        invoice.setDtSuratHutangPeriodFrom(periodstart);
//        invoice.setDtSuratHutangPeriodTo(periodend);
        DTOList listInv = invoice.getListDetailsSHKClaimIzinCair();

        descLong = descLong + "\n" + noizincair;
        for (int i = 0; i < listInv.size(); i++) {
            HashDTO dto = (HashDTO) listInv.get(i);

            boolean samedata = false;

            if (i > 0) {
                HashDTO dto2 = (HashDTO) listInv.get(i - 1);
                String invoice_id = dto2.getFieldValueByFieldNameBD("ar_invoice_id").toString();

                if (dto.getFieldValueByFieldNameBD("ar_invoice_id").toString().equalsIgnoreCase(invoice_id)) {
                    samedata = true;
                }
            }

            onNewInvoiceByNoSuratHutang(dto, samedata);
        }

        receipt.setStDescription("Pembayaran " + descLong);
        receipt.setStShortDescription(receipt.getStDescription());
    }

    private String noizincair;

    /**
     * @return the noizincair
     */
    public String getNoizincair() {
        return noizincair;
    }

    /**
     * @param noizincair the noizincair to set
     */
    public void setNoizincair(String noizincair) {
        this.noizincair = noizincair;
    }

    
}
