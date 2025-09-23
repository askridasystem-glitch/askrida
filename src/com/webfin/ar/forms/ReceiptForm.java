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
import com.crux.util.ConvertUtil;
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
import com.webfin.entity.model.EntityView;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.ejb.PeriodManager;
import com.webfin.gl.model.AccountView;
import com.webfin.gl.model.ClosingDetailView;
import com.webfin.gl.model.GLCostCenterView;
import com.webfin.gl.model.TitipanPremiView;
import com.webfin.gl.util.GLUtil;
import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import javax.servlet.ServletOutputStream;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class ReceiptForm extends Form {

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
   
   private boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("SETTLR_NAVBR");
   private boolean reverseMode;
   
    private String stFontSize;
    private String stCurrency;
    private Date periodstart;
    private Date periodend;

    private Date dtApplyDateFrom;
    private Date dtApplyDateTo;
    private String stBranch;
    private String stBranchDesc;
    private String stInsuranceTreatyTypeDesc;

    private String nosurathutangrekap;

    private Date shpstart;
    private Date shpend;
    private String stRekeningNo;
    private String ccy;

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }
   
   
   private final static transient LogManager logger = LogManager.getInstance(ReceiptForm.class);

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

        getRemoteInsurance().updateMonitoring(l, this);

        loadFormList();

        final ArrayList plist = new ArrayList();

        plist.add(stReportType+"_"+getStCurrency());

        plist.add(stReportType);

        String urx=null;

        logger.logDebug("##################: scanlist:"+plist);

        for (int i = 0; i < plist.size(); i++) {
            String s = (String) plist.get(i);

            if (formList.contains(s+".fop.jsp")) {
                urx = "/pages/ar/report/"+s+".fop";
                break;
            }
        }

        if (urx==null) throw new RuntimeException("Unable to find suitable print form");

        super.redirect(urx);

        //super.redirect("/pages/ar/report/claim.fop");
    }

    private void loadFormList() {
        if (formList==null || true) {
            final String[] filez = new File(SessionManager.getInstance().getRequest().getSession().getServletContext().getRealPath("/pages/ar/report")).list();

            formList = new HashSet();

            for (int i = 0; i < filez.length; i++) {
                String s = filez[i];

                formList.add(s);
            }
        }
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
       return receipt.getDetails();   
   }

   public ReceiptForm() {
   }

   public void onNewNoteItem() {

   }

   public void onDeleteNoteItem() throws Exception {
      getListNotes().delete(Integer.parseInt(notesindex));

      recalculate();
   }

   private void recalculate() throws Exception {
      receipt.recalculate();
      //setDate();
      
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
      //final ARReceiptLinesView iv = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

      ARReceiptLinesView currentLine = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

      getListInvoices().delete(Integer.parseInt(invoicesindex));

      if(receipt.getDetails().size()>0){
          receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " " + getWording());
          receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " " + getWording());
      }

      currentLine.getDetails().deleteAll();
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

         if(rl.getDtReceiptDate()!=null)
             rcl.setDtReceiptDate(rl.getDtReceiptDate());
          else
             rcl.setDtReceiptDate(receipt.getDtReceiptDate());
         
         rcl.setStPolicyID(invoice.getStAttrPolicyID());

         rcl.setDbInvoiceAmount(d.getDbEnteredAmount());
         rcl.setDbAmount(d.getDbOustandingAmount());
         rcl.setDbEnteredAmount(d.getDbOustandingAmount());

         //KSG detail simulasi REMARK KSG
         /*
         if(invoice.isPolicyKSG()){
             rcl.setDbAmount100(d.getDbAmount100());
             rcl.setDbAmount1(d.getDbAmount1());
             rcl.setDbAmount2(d.getDbAmount2());
             rcl.setDbAmount3(d.getDbAmount3());
             rcl.setDbAmount4(d.getDbAmount4());
             rcl.setDbAmount5(d.getDbAmount5());
             rcl.setDbAmount6(d.getDbAmount6());

             rcl.setDbTaxAmount100(d.getDbTaxAmount100());
             rcl.setDbTaxAmount1(d.getDbTaxAmount1());
             rcl.setDbTaxAmount2(d.getDbTaxAmount2());
             rcl.setDbTaxAmount3(d.getDbTaxAmount3());
             rcl.setDbTaxAmount4(d.getDbTaxAmount4());
             rcl.setDbTaxAmount5(d.getDbTaxAmount5());
             rcl.setDbTaxAmount6(d.getDbTaxAmount6());
         }*/


         //test new
         /*
         final DTOList items = d.getItems();

         InsurancePolicyItemsView it = (InsurancePolicyItemsView) items.get(0);
         
         if(items!=null){

             if(it!=null){
                 rcl.setDbAmount100(it.getDbAmount100());
                 rcl.setDbAmount1(it.getDbAmount1());
                 rcl.setDbAmount2(it.getDbAmount2());
                 rcl.setDbAmount3(it.getDbAmount3());
                 rcl.setDbAmount4(it.getDbAmount4());
                 rcl.setDbAmount5(it.getDbAmount5());
                 rcl.setDbAmount6(it.getDbAmount6());

                 rcl.setDbTaxAmount100(it.getDbTaxAmount100());
                 rcl.setDbTaxAmount1(it.getDbTaxAmount1());
                 rcl.setDbTaxAmount2(it.getDbTaxAmount2());
                 rcl.setDbTaxAmount3(it.getDbTaxAmount3());
                 rcl.setDbTaxAmount4(it.getDbTaxAmount4());
                 rcl.setDbTaxAmount5(it.getDbTaxAmount5());
                 rcl.setDbTaxAmount6(it.getDbTaxAmount6());
             }
         }*/
         

         boolean isTax = d.isTaxComm() || d.isTaxBrok() || d.isTaxHFee();
         if(!isTax) rcl.setStCheck("Y");

         //test ksg detail REMARK KSG
         /*
         if(!isTax){
             if(invoice.isPolicyKSG()){
                 if(!BDUtil.isZeroOrNull(d.getDbAmount2())) rcl.setStCheck2("Y");
                 if(!BDUtil.isZeroOrNull(d.getDbAmount3())) rcl.setStCheck3("Y");
                 if(!BDUtil.isZeroOrNull(d.getDbAmount4())) rcl.setStCheck4("Y");
                 if(!BDUtil.isZeroOrNull(d.getDbAmount5())) rcl.setStCheck5("Y");
                 if(!BDUtil.isZeroOrNull(d.getDbAmount6())) rcl.setStCheck6("Y");
             }
             /*
             if(it!=null){
                 if(!BDUtil.isZeroOrNull(it.getDbAmount2())) rcl.setStCheck2("Y");
                 if(!BDUtil.isZeroOrNull(it.getDbAmount3())) rcl.setStCheck3("Y");
                 if(!BDUtil.isZeroOrNull(it.getDbAmount4())) rcl.setStCheck4("Y");
                 if(!BDUtil.isZeroOrNull(it.getDbAmount5())) rcl.setStCheck5("Y");
                 if(!BDUtil.isZeroOrNull(it.getDbAmount6())) rcl.setStCheck6("Y");
             }*/
              
         //}

         rcl.setStLock(true);
         
         if(lockCheck(rcl))
             rcl.setStLock(false);

         if (d.isComission()) rcl.markAsComission();

         rl.getDetails().add(rcl);

      }

      receipt.recalculate();
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
   
   public void createNew(String stSettlementID) throws Exception {

      receipt = new ARReceiptView();
      receipt.markNew();

      receipt.setNotes(new DTOList());
      receipt.setDetails(new DTOList());

      receipt.setStARSettlementID(stSettlementID);

      receipt.setStInvoiceType(receipt.getSettlement().getStTrxType());

      //receipt.setDtReceiptDate(new Date());

      if(!canNavigateBranch)
            receipt.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());

      final String settlementRC = (String)receipt.getSettlement().getPropMap().get("RC");

      receipt.setStReceiptClassID(settlementRC);

      setTitle(receipt.getSettlement().getStDescription());

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

      if (receipt.isPosted()) throw new RuntimeException("Pembayaran tidak bisa diubah (Posted)");
      if (receipt.isCancel()) throw new RuntimeException("Receipt not editable (VOID)");

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
         
         final DTOList subDetails = rcl.getDetails();
         
         for (int j = 0; j < subDetails.size(); j++) {
            ARReceiptLinesView rclSub = (ARReceiptLinesView) subDetails.get(j);

            rclSub.setStLock(true);

            if(rclSub.getInvoiceDetail()!=null)
            if(lockCheck(rclSub)){
                    rclSub.setStLock(false);
            }
         }

         rcl.getDetails().markAllUpdate();
      }

      setTitle(receipt.getSettlement().getStDescription());

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
      receipt = getRemoteAccountReceivable().getARReceipt(receiptID);
      
      if("3".equalsIgnoreCase(receipt.getStReceiptClassID())||
      	"4".equalsIgnoreCase(receipt.getStReceiptClassID())||
      	"8".equalsIgnoreCase(receipt.getStReceiptClassID())||
      	"9".equalsIgnoreCase(receipt.getStReceiptClassID())||
      	"10".equalsIgnoreCase(receipt.getStReceiptClassID()))
      		receipt.setUsingEntityID(true);

      super.setReadOnly(true);

      viewMode = true;
   }

   public void doSave() throws Exception {
       onChgPeriod();
      //receipt.generateReceiptNo();
      //final String trxNO = receipt.getStReceiptNo();
      receipt.recalculate();
      receipt.setStIDRFlag("Y");
      
      if("16".equalsIgnoreCase(receipt.getStARSettlementID())) getRemoteAccountReceivable().savePembayaranPremiPolisSementara(receipt);
      else if("17".equalsIgnoreCase(receipt.getStARSettlementID())) getRemoteAccountReceivable().savePembayaranPremiRealisasiTitipan(receipt);
      else if("13".equalsIgnoreCase(receipt.getStARSettlementID())) getRemoteAccountReceivable().savePembayaranCoas(receipt);
      else if("2".equalsIgnoreCase(receipt.getStARSettlementID())) getRemoteAccountReceivable().savePembayaranKomisi(receipt);
      else if("10".equalsIgnoreCase(receipt.getStARSettlementID())) getRemoteAccountReceivable().savePembayaranKlaim(receipt);
      else if("18".equalsIgnoreCase(receipt.getStARSettlementID())) getRemoteAccountReceivable().savePengembalianPremi(receipt);
      else if("22".equalsIgnoreCase(receipt.getStARSettlementID())) getRemoteAccountReceivable().savePembayaranKlaim(receipt);
      else if("24".equalsIgnoreCase(receipt.getStARSettlementID())) getRemoteAccountReceivable().savePembayaranCoas(receipt);
      else if("25".equalsIgnoreCase(receipt.getStARSettlementID())) getRemoteAccountReceivable().savePembayaranKlaimLKS(receipt);
      else if("14".equalsIgnoreCase(receipt.getStARSettlementID())) getRemoteAccountReceivable().savePembayaranCoas(receipt);
      else if("38".equalsIgnoreCase(receipt.getStARSettlementID())) getRemoteAccountReceivable().savePembayaranPremiSentralisasi(receipt);
      else getRemoteAccountReceivable().savePembayaranPremi(receipt);
      super.close();
   }
   
   public void doSave3() throws Exception {
       onChgPeriod();
      //receipt.generateReceiptNo();
      //final String trxNO = receipt.getStReceiptNo();
      receipt.recalculate();
      receipt.setStIDRFlag("Y");

      getRemoteAccountReceivable().savePembayaranKlaimKoas(receipt);
      
      super.close();
   }
   
   public void doSaveTitipan() throws Exception {
      receipt.generateReceiptNo();
      final String trxNO = receipt.getStReceiptNo();
      receipt.recalculate();
      getRemoteAccountReceivable().saveReceiptTitipan(receipt,this);
      super.close();
   }

   public void doRecalculate() throws Exception {
        if(receipt.getStARSettlementID()!=null){
              if(receipt.getSettlement().checkProperty("CALC_COMM","Y")) recalculatePembayaranKomisi();
              else if(receipt.getSettlement().checkProperty("CALC_TAX","Y")) recalculatePembayaranPajak();
              else if(receipt.getSettlement().checkProperty("CALC_TITIP","Y")) receipt.recalculateLKS();
              else receipt.recalculate();
          }
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
      receipt.setDbCurrencyRate(CurrencyManager.getInstance().getRate(receipt.getStCurrencyCode(), receipt.getDtReceiptDate()));
   }

   public void changeReceiptClass() {
      //receipt.setStInvoiceType(receipt.getReceiptClass().getStInvoiceType());
      if("3".equalsIgnoreCase(receipt.getStReceiptClassID())||
      	"4".equalsIgnoreCase(receipt.getStReceiptClassID())||
      	"8".equalsIgnoreCase(receipt.getStReceiptClassID())||
      	"9".equalsIgnoreCase(receipt.getStReceiptClassID())||
      	"10".equalsIgnoreCase(receipt.getStReceiptClassID()))
      	receipt.setUsingEntityID(true);
      
      //setDate();
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
      validateInvoiceAlreadyIn2(parinvoiceid);
       
      final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(parinvoiceid);

      validatePembayaranPolisInduk(invoice);

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
          receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " " + getWording());
          receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " " + getWording());
      }

      receipt.recalculate();

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandInvoiceItem();

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
      invoice.setDtSuratHutangPeriodFrom(periodstart);
      invoice.setDtSuratHutangPeriodTo(periodend);
      DTOList listInv = invoice.getList2();
      
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

                onNewInvoiceByNoSuratHutang(dto,samedata);
	  }
    
      receipt.setStDescription("Pembayaran " + descLong);
      receipt.setStShortDescription(receipt.getStDescription());
   }   

   public void changeCostCenter()throws Exception {
        cekClosingStatus("INPUT");
   }

   public void changemethod() {

   }

   public void generatRNo() throws Exception {
      receipt.generateReceiptNo();
   }

   public void approve(String receiptID) throws Exception {
      masterEditApprove(receiptID);

      cekClosingStatus("APPROVE");

      if(isPosted()){
            String tahun = receipt.getStYears();
            throw new RuntimeException("Transaksi bulan "+ receipt.getStMonths() +" dan tahun "+ tahun +" tsb sudah diposting");
      }

      if (receipt.isPosted()) throw new RuntimeException("Receipt not editable (Posted)");
      if (receipt.isCancel()) throw new RuntimeException("Receipt not editable (VOID)");

      receipt.setStPostedFlag("Y");
      super.setReadOnly(true);
      approveMode = true;
   }

   public boolean isApproveMode() {
      return approveMode;
   }

   public void doApprove() throws Exception {
      //receipt.setStPostedFlag("Y");
       if(isPosted()){
            String tahun = receipt.getStYears();
            throw new RuntimeException("Transaksi bulan "+ receipt.getStMonths() +" dan tahun "+ tahun +" tsb sudah diposting");
      }
       
      doSave();
   }
   
   public void doApprovePajak() throws Exception {
      //receipt.setStPostedFlag("Y");
      doSave3();
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

        loadFormList();

        final ArrayList plist = new ArrayList();

        plist.add(stReceiptType+"_"+getStCurrency());

        plist.add(stReceiptType);

        String urx=null;

        logger.logDebug("##################: scanlist:"+plist);

        for (int i = 0; i < plist.size(); i++) {
            String s = (String) plist.get(i);

            if (formList.contains(s+".fop.jsp")) {
                urx = "/pages/ar/report/"+s+".fop";
                break;
            }
        }

        if (urx==null) throw new RuntimeException("Unable to find suitable print form");

        super.redirect(urx);

        //super.redirect("/pages/ar/report/"+stReceiptType+".fop");
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
   
   private static HashSet formList = null;


    public void clickPrintClaim()throws Exception{
        final DTOList l = receipt.getDetails();

        SessionManager.getInstance().getRequest().setAttribute("RPT",l);
        
        getRemoteInsurance().updateClaimRecap(l, this);

        loadFormList();

        final ArrayList plist = new ArrayList();

        plist.add("claim"+stEntityID);

        plist.add("claim");

        String urx=null;

        logger.logDebug("##################: scanlist:"+plist);

        for (int i = 0; i < plist.size(); i++) {
            String s = (String) plist.get(i);

            if (formList.contains(s+".fop.jsp")) {
                urx = "/pages/ar/report/"+s+".fop";
                break;
            }
        }

        if (urx==null) throw new RuntimeException("Unable to find suitable print form");

        super.redirect(urx);

        //super.redirect("/pages/ar/report/claim.fop");
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
   
   public void doApproveReinsurance() throws Exception {
      doSaveReinsurance();
   }
   
   public void doSaveReinsurance() throws Exception {
        onChgPeriod();
        //receipt.generateReceiptNo();
        //final String trxNO = receipt.getStReceiptNo();
        receipt.recalculate();
        receipt.setStIDRFlag("Y");

        if("30".equalsIgnoreCase(receipt.getStARSettlementID()) || "46".equalsIgnoreCase(receipt.getStARSettlementID())) getRemoteAccountReceivable().savePembayaranInwardNew(receipt);
        else if("11".equalsIgnoreCase(receipt.getStARSettlementID())) getRemoteAccountReceivable().savePenerimaanKlaimOutward(receipt);
        else getRemoteAccountReceivable().savePembayaranReasuransi(receipt);

        super.close();
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
    
   public DTOList EXCEL_CLAIM() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                " a.ar_invoice_id,a.attr_pol_no,c.ref1 as nama,substr(b.pol_no,1,16)||''||get_koasur(substr(b.pol_no,17,2)) as pol_no,"
                + " a.cc_code,b.dla_no,a.claim_no,a.claim_name,a.receipt_no,a.receipt_date,a.amount_settled,a.claim_coins_id,a.claim_coins_name,"
                + " a.amount,b.parent_id,b.pol_id,b.claim_date,b.approved_date as claim_approved,e.ref2,e.division,e.vs_description,"
                + " c.refd1 as tgllahir,c.refd2 as tglawal,c.refd3 as tglakhir,"
                + " c.ref8,c.refn4,c.refn1,c.refn2,c.refn9,c.rekap_kreasi,"
                + " (select x.approved_date from ins_policy x where x.pol_no = b.pol_no and status in ('POLICY','ENDORSE','RENEWAL') and effective_flag = 'Y') as approved_polis_date,"
                + " (select x.ref3 from ins_policy x where x.pol_id = b.parent_id) as ref3,"
                + " (select x.ref4 from ins_policy x where x.pol_id = b.parent_id) as ref4 ");

        sqa.addQuery(" from ar_invoice a "
                + " inner join ins_policy b on b.pol_id = a.attr_pol_id "
                + " inner join ins_pol_obj c on c.pol_id = b.pol_id "
                + " left join s_valueset e on e.vs_code = b.kreasi_type_id and e.vs_group = 'INSOBJ_KREASI_KREDIT' ");

        sqa.addClause("a.claim_no = ? ");
        sqa.addPar(stReceiptNo);

        final String sql = " select a.pol_id,a.dla_no,a.attr_pol_no,a.receipt_no,a.receipt_date,a.amount_settled,a.nama,a.tgllahir,a.tglawal,a.tglakhir,"
                + " a.claim_date,a.claim_approved,a.approved_polis_date,a.amount,a.ref8,a.refn4,a.refn1,a.refn2,a.refn9,a.rekap_kreasi,"
                + " a.claim_no,a.ref3,a.ref4,a.ref2,a.division,a.vs_description,c.norek  from ( " + sqa.getSQL() + " ) a "
                + " left join aba_koasur c on c.nopol = a.pol_no "
                + " group by a.pol_id,a.dla_no,a.attr_pol_no,a.nama,a.receipt_no,a.receipt_date,a.amount_settled,a.claim_date,a.claim_approved,a.approved_polis_date,"
                + " a.amount,a.ref8,a.refn4,a.refn1,a.refn2,a.refn9,a.rekap_kreasi,a.claim_no,a.ref3,a.ref4,a.ref2,a.division,a.vs_description,"
                + " a.tgllahir,a.tglawal,a.tglakhir,c.norek "
                + " order by a.claim_no,a.dla_no";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

    }

    public void EXPORT_CLAIM() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        String coinsurer = null;
        String noRekap = null;
        String noRekap1 = null;
        String noRekap2 = null;
        String noRekap3 = null;

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("Pengajuan Klaim No : " + h.getFieldValueByFieldNameST("claim_no"));

            XSSFRow row1 = sheet.createRow(2);
            row1.createCell(0).setCellValue("ID");
            row1.createCell(1).setCellValue("No. LKP");
            row1.createCell(2).setCellValue("No. Polis");
            row1.createCell(3).setCellValue("Nama");
            row1.createCell(4).setCellValue("Tanggal Klaim");
            row1.createCell(5).setCellValue("Nilai Klaim");
            row1.createCell(6).setCellValue("Insured");
            row1.createCell(7).setCellValue("Rate Koas");
            row1.createCell(8).setCellValue("Premi Koas");
            row1.createCell(9).setCellValue("Komisi Koas");
            row1.createCell(10).setCellValue("Tanggal Lahir");
            row1.createCell(11).setCellValue("Tanggal Awal");
            row1.createCell(12).setCellValue("Tanggal Akhir");
            row1.createCell(13).setCellValue("No. Reg");
            row1.createCell(14).setCellValue("Kredit");
            row1.createCell(15).setCellValue("Jenis");
            row1.createCell(16).setCellValue("Tipe");
            row1.createCell(17).setCellValue("No Bukti");
            row1.createCell(18).setCellValue("Tgl Bayar");
            row1.createCell(19).setCellValue("Jml Bayar");
            row1.createCell(20).setCellValue("Keterangan");
            row1.createCell(21).setCellValue("Tanggal Setujui Klaim");
            row1.createCell(22).setCellValue("Tanggal Setujui Polis");

            coinsurer = h.getFieldValueByFieldNameST("ref8");
            noRekap1 = h.getFieldValueByFieldNameST("ref3") != null ? h.getFieldValueByFieldNameST("ref3") : h.getFieldValueByFieldNameST("ref4");
            noRekap2 = h.getFieldValueByFieldNameST("norek");
            noRekap3 = h.getFieldValueByFieldNameST("rekap_kreasi");

            if (coinsurer != null) {
                if (noRekap3 != null) {
                    noRekap = noRekap3;
                } else if (noRekap3 == null) {
                    noRekap = noRekap1;
                } else if (noRekap3 == null && noRekap2 != null) {
                    noRekap = noRekap2;
                } else if (noRekap3 == null && noRekap2 == null) {
                    noRekap = noRekap2;
                }
            } else if (coinsurer == null) {
                if (noRekap1 != null) {
                    noRekap = noRekap1;
                } else if (noRekap1 == null && noRekap2 != null) {
                    noRekap = noRekap2;
                } else if (noRekap1 == null && noRekap2 == null) {
                    noRekap = noRekap2;
                }
            }

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("attr_pol_no"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameST("nama"));
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("claim_date"));
            if (h.getFieldValueByFieldNameBD("amount") != null) {
                row.createCell(5).setCellValue(h.getFieldValueByFieldNameBD("amount").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("refn4") != null) {
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("refn4").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("refn1") != null) {
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("refn1").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("refn2") != null) {
                row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("refn2").doubleValue());
            }
            if (h.getFieldValueByFieldNameBD("refn9") != null) {
                row.createCell(9).setCellValue(h.getFieldValueByFieldNameBD("refn9").doubleValue());
            }
            if (h.getFieldValueByFieldNameDT("tgllahir") != null) {
                row.createCell(10).setCellValue(h.getFieldValueByFieldNameDT("tgllahir"));
            }
            if (h.getFieldValueByFieldNameDT("tglawal") != null) {
                row.createCell(11).setCellValue(h.getFieldValueByFieldNameDT("tglawal"));
            }
            if (h.getFieldValueByFieldNameDT("tglakhir") != null) {
                row.createCell(12).setCellValue(h.getFieldValueByFieldNameDT("tglakhir"));
            }
            row.createCell(13).setCellValue(noRekap);
            row.createCell(14).setCellValue(h.getFieldValueByFieldNameST("vs_description"));
            row.createCell(15).setCellValue(h.getFieldValueByFieldNameST("ref2"));
            row.createCell(16).setCellValue(h.getFieldValueByFieldNameST("division"));
            if (h.getFieldValueByFieldNameST("receipt_no") != null) {
                row.createCell(17).setCellValue(h.getFieldValueByFieldNameST("receipt_no"));
            }
            if (h.getFieldValueByFieldNameDT("receipt_date") != null) {
                row.createCell(18).setCellValue(h.getFieldValueByFieldNameDT("receipt_date"));
            }
            if (h.getFieldValueByFieldNameBD("amount_settled") != null) {
                row.createCell(19).setCellValue(h.getFieldValueByFieldNameBD("amount_settled").doubleValue());
            }

            if (h.getFieldValueByFieldNameDT("claim_approved") != null)
                row.createCell(21).setCellValue(h.getFieldValueByFieldNameDT("claim_approved"));

            if (h.getFieldValueByFieldNameDT("approved_polis_date") != null)
                row.createCell(22).setCellValue(h.getFieldValueByFieldNameDT("approved_polis_date"));
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=pengajuan_klaim.xlsx;");
        SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }
    
    public void checkBayarFlag(){
        
    }
    
    public void validateInvoiceAlreadyIn(String arInvoiceID) throws Exception{
        final DTOList detail = receipt.getDetails();
        
         for (int i = 0; i < detail.size(); i++) {
              ARReceiptLinesView rl = (ARReceiptLinesView) detail.get(i);
              
              if(rl.getInvoice().getStARInvoiceID().equalsIgnoreCase(arInvoiceID))
                  throw new RuntimeException("Invoice "+arInvoiceID +" Sudah Dipilih sebelumnya");
         }
    }
    
    public void onNewSuratHutangPajak21() throws Exception {
      //final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoiceByNoSuratHutang(nosurathutang);
      ARInvoiceView invoice = new ARInvoiceView();
      invoice.setStNoSuratHutang(nosurathutang);
      final DTOList listInv = invoice.getListDetailsSuratHutangPph21();
      
	  for (int i = 0; i < listInv.size(); i++) {
                ARInvoiceView invoiceView = (ARInvoiceView) listInv.get(i);
                parinvoiceid = invoiceView.getStARInvoiceID();
                onNewInvoicePembayaranPajak(invoiceView);
	  }
	   
   }
    
    public void onNewSuratHutangPajak23() throws Exception {
      //final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoiceByNoSuratHutang(nosurathutang);
      ARInvoiceView invoice = new ARInvoiceView();
      invoice.setStNoSuratHutang(nosurathutang);
      final DTOList listInv = invoice.getListDetailsSuratHutangPph23();
      
	  for (int i = 0; i < listInv.size(); i++) {
                ARInvoiceView invoiceView = (ARInvoiceView) listInv.get(i);
                parinvoiceid = invoiceView.getStARInvoiceID();
                onNewInvoicePembayaranPajak(invoiceView);
	  }
	   
   }
    
    public void onNewInvoicePajakByNoSuratHutang() throws Exception {
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
      rcl.markAsInvoice();
      rcl.markCommit();

      receipt.setStEntityID(invoice.getStEntityID());

      if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());
      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());

      receipt.getDetails().add(rcl);

      receipt.recalculatePajak();

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);
      
   }
    
   public void masterEditApprove(String receiptID) throws Exception {
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
         
         final DTOList subDetails = rcl.getDetails();
         
         for (int j = 0; j < subDetails.size(); j++) {
            ARReceiptLinesView rclSub = (ARReceiptLinesView) subDetails.get(j);
            
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

      setTitle(receipt.getSettlement().getStDescription());

   }
   
   private void recalculatePembayaranKomisi() throws Exception {
      receipt.recalculatePembayaranKomisi();
   }
   
   public void onNewInvoicePembayaranKomisi() throws Exception {
       
      ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(parinvoiceid);

      validateInvoiceAlreadyIn(invoice.getStARInvoiceID());

      ARReceiptLinesView rcl = new ARReceiptLinesView();
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
         
         if(!isTax){
             rcl.setStCheck("Y");
         }

         rcl.setStLock(true);

         if(isTax){
            //rcl.setStLock(false);
         }
         
         /*
         if(d.isTaxComm()||d.isTaxBrok()||d.isTaxHFee()){
            rcl.setDbAmount(BDUtil.zero);
            rcl.setDbEnteredAmount(BDUtil.zero);
         }*/
         	
         
         if (d.isComission()) rcl.markAsComission();
         //rcl.markCommit();

         rl.getDetails().add(rcl);

         if(d.isFeeBase3()){
             DTOList invoicePPN = getARInvoicePPNFeeBase(invoice.getStAttrPolicyNo());

             if(invoicePPN.size()>0){
                 ARInvoiceView invPPN = (ARInvoiceView) invoicePPN.get(0);

                 onNewInvoicePembayaranKomisiByArInvoiceID(invPPN.getStARInvoiceID());
             }

         }
      }
      receipt.recalculatePembayaranKomisi();
   }
   
   public void doSavePembayaranKomisi() throws Exception {
         onChgPeriod();
         receipt.generateReceiptNo();
         final String trxNO = receipt.getStReceiptNo();
         receipt.recalculatePembayaranKomisi();
         receipt.setStIDRFlag("Y");

         if("39".equalsIgnoreCase(receipt.getStARSettlementID())) getRemoteAccountReceivable().savePembayaranKomisiSentralisasi(receipt);
         else getRemoteAccountReceivable().savePembayaranKomisi(receipt);

         super.close();
   }
   
    public void doApprovePembayaranKomisi() throws Exception {
      //receipt.setStPostedFlag("Y");
      doSavePembayaranKomisi();
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

      //receipt.recalculatePembayaranPajak();

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandInvoiceItemPembayaranPajak();
   }
   
   public void onExpandInvoiceItemPembayaranPajak() throws Exception {
      final ARReceiptLinesView rl = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));
      
      final ARInvoiceView invoice = rl.getInvoice();

      rl.setStExpandedFlag("Y");

      final DTOList details = invoice.getDetailsWithoutOrder();

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
      //receipt.recalculatePembayaranPajak();
   }
   
   public void doSavePembayaranPajak() throws Exception {
       onChgPeriod();
      //receipt.generateReceiptNo();
      //final String trxNO = receipt.getStReceiptNo();
      receipt.recalculatePembayaranPajak();
      
      receipt.setStIDRFlag("Y");
     
      getRemoteAccountReceivable().savePembayaranPajak(receipt);
      super.close();
   }
   
    public void doApprovePembayaranPajak() throws Exception {
      //receipt.setStPostedFlag("Y");
      doSavePembayaranPajak();
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
    
    public void createNewTes(String stSettlementID) throws Exception {

      receipt = new ARReceiptView();
      receipt.markNew();

      receipt.setNotes(new DTOList());
      receipt.setDetails(new DTOList());

      receipt.setStARSettlementID(stSettlementID);

      receipt.setStInvoiceType(receipt.getSettlement().getStTrxType());

      receipt.setDtReceiptDate(new Date());

      final String settlementRC = (String)receipt.getSettlement().getPropMap().get("RC");

      setTitle(receipt.getSettlement().getStDescription());

      chgCurrency();

      editMode=true;
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
    
    public void doSavePembayaranLKS() throws Exception {
        onChgPeriod();
      receipt.generateReceiptNo();
      final String trxNO = receipt.getStReceiptNo();
      receipt.recalculateLKS();
      receipt.setStIDRFlag("Y");
      getRemoteAccountReceivable().savePembayaranKlaimLKS(receipt);

      super.close();
   }
    
    public void doApprovePembayaranLKS() throws Exception {
      //receipt.setStPostedFlag("Y");
      doSavePembayaranLKS();
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
      if(receipt.getStARSettlementID()!=null){
          switch (Integer.parseInt(receipt.getStARSettlementID())) {
            case 1: return "Polis";
            case 2: return "Komisi";
            case 8: return "Pajak";
            case 9: return "Reas";
            case 10: return "Klaim";
            case 13: return "Koasuransi";
            default: return "";
        } 
      }
      return "";
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
        
        if (!Tools.isEqual(DateUtil.getMonth2Digit(receipt.getDtReceiptDate()),receipt.getStMonths())) throw new Exception("Bulan tidak sama dengan Tanggal Pembayaran");
        if (!Tools.isEqual(DateUtil.getYear(receipt.getDtReceiptDate()),receipt.getStYears())) throw new Exception("Tahun tidak sama dengan Tanggal Pembayaran");
    }
    
    public String getClaimno() {
        return claimno;
    }

    public void setClaimno(String claimno) {
        this.claimno = claimno;
    }
    
    public void setDate() throws Exception{

         if(isPosted()){
            String tahun = receipt.getStYears();
            receipt.setStYears(null);
            throw new RuntimeException("Transaksi bulan "+ receipt.getStMonths() +" dan tahun "+ tahun +" tsb sudah diposting");
        }
            

        String date = "01/"+ receipt.getStMonths()+"/"+receipt.getStYears();
        
        receipt.setDtReceiptDate(DateUtil.getDate(date));
                
    }
    
    public void selectMonth(){
        
    }
    
    public void onDeleteAll() throws Exception {
      getListInvoices().deleteAll();
      //getListInvoices().delete(Integer.parseInt(invoicesindex));
      if(receipt.getDetails().size()>0){
          receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " " + getWording());
          receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " " + getWording());
      }
   }

    public boolean isReverseMode() {
        return reverseMode;
    }

    public void setReverseMode(boolean reverseMode) {
        this.reverseMode = reverseMode;
    }
    
    public void reverse() throws Exception{

        if(isPosted()){
            String tahun = receipt.getStYears();
            throw new RuntimeException("Transaksi bulan "+ receipt.getStMonths() +" dan tahun "+ tahun +" tsb sudah diposting");
        } 

        getRemoteAccountReceivable().reversePembayaran(receipt);

        super.close();
    }

    public String getStFontSize() {
        return stFontSize;
    }

    public void setStFontSize(String stFontSize) {
        this.stFontSize = stFontSize;
    }

    public boolean lockCheck(ARReceiptLinesView rclSub){
        return rclSub.getInvoiceDetail().isCommission2()||rclSub.getInvoiceDetail().isBrokerage2()
                    ||rclSub.getInvoiceDetail().isHandlingFee2();

                    //||rclSub.getInvoiceDetail().isTaxBrok()
                    //||rclSub.getInvoiceDetail().isTaxComm()||rclSub.getInvoiceDetail().isTaxHFee();
    }

     public void onExpandInvoiceItemPembayaranKomisiAll(ARReceiptLinesView rl) throws Exception {
      //final ARReceiptLinesView rl = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

      ARInvoiceView invoice = rl.getInvoice();

      rl.setStExpandedFlag("Y");

      DTOList details = invoice.getDetails();

      for (int i = 0; i < details.size(); i++) {
         ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);

         //if (!d.isComission()) continue;

         ARReceiptLinesView rcl = new ARReceiptLinesView();
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

         if(!isTax){
             rcl.setStCheck("Y");
         }

         rcl.setStLock(true);

         if(isTax){
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

    public void setAccountExcess() throws Exception{
        DTOList receiptLines = receipt.getDetails();

        ARReceiptLinesView rl = (ARReceiptLinesView) receiptLines.get(Integer.parseInt(invoicesindex));

        if(rl.getStARSettlementExcessID()!=null){
            rl.setArSettlementExcess(null);

            ARSettlementExcessView arSettlementExcess = rl.getARSettlementExcess();

            GLUtil.Applicator gla = new GLUtil.Applicator();

            gla.setCode('B',receipt.getStCostCenterCode());

            rl.setStExcessAccountID(gla.getAccountID(arSettlementExcess.getStGLAccount()));
        }
    }

    public void onNewInvoicePembayaranPajak(ARInvoiceView inv) throws Exception {

      validateInvoiceAlreadyIn(parinvoiceid);

      final ARInvoiceView invoice = inv;

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

      //receipt.recalculatePembayaranPajak();

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandInvoiceItemPembayaranPajak();
   }

    /**
     * @return the periodstart
     */
    public Date getPeriodstart() {
        return periodstart;
    }

    /**
     * @param periodstart the periodstart to set
     */
    public void setPeriodstart(Date periodstart) {
        this.periodstart = periodstart;
    }

    /**
     * @return the periodend
     */
    public Date getPeriodend() {
        return periodend;
    }

    /**
     * @param periodend the periodend to set
     */
    public void setPeriodend(Date periodend) {
        this.periodend = periodend;
    }

    public void onNewSuratHutangPph21New() throws Exception {
      ARInvoiceView invoice = new ARInvoiceView();
      invoice.setStNoSuratHutang(nosurathutang);
      invoice.setDtSuratHutangPeriodFrom(periodstart);
      invoice.setDtSuratHutangPeriodTo(periodend);
      DTOList listInv = invoice.getListDetailsSuratHutangPph21New();

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
      receipt.setStShortDescription(receipt.getStDescription());
   }

    public void onNewSuratHutangPph23New() throws Exception {
      ARInvoiceView invoice = new ARInvoiceView();
      invoice.setStNoSuratHutang(nosurathutang);
      invoice.setDtSuratHutangPeriodFrom(periodstart);
      invoice.setDtSuratHutangPeriodTo(periodend);
      DTOList listInv = invoice.getListDetailsSuratHutangPph23New();

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
      receipt.setStShortDescription(receipt.getStDescription());
   }

    public boolean isPosted() throws Exception {

            SQLUtil S = new SQLUtil();

            boolean isPosted = false;

            try {
                String cek = "select gl_post_id from gl_posting where months = ? and years = ? and posted_flag = 'Y' and cc_code is null"+
                            " union "+
                            " select gl_post_id from gl_posting where months = ? and years = ? and posted_flag = 'Y'";

                if(receipt.getStCostCenterCode()!=null)
                    cek = cek + " and cc_code = ?";

                PreparedStatement PS = S.setQuery(cek);
                PS.setString(1, receipt.getStMonths());
                PS.setString(2, receipt.getStYears());

                PS.setString(3, receipt.getStMonths());
                PS.setString(4, receipt.getStYears());

                if(receipt.getStCostCenterCode()!=null)
                      PS.setString(5, receipt.getStCostCenterCode());

                ResultSet RS = PS.executeQuery();

                if (RS.next()){
                    isPosted = true;
                }

            } finally {
                S.release();
            }

        return isPosted;
    }

    public void uploadExcelOthers() throws Exception {

        String fileID = getReceipt().getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheetPolis = wb.getSheet("POLIS");

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
            HSSFCell cellReceiptDate = row.getCell(4);//nomor polis
            HSSFCell cellEntityID = row.getCell(5);//nomor polis

            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, getReceipt().getStARSettlementID());

            DTOList invoiceList = getARInvoiceByPolId(cellNomorPolis.getStringCellValue(), stl.getStParameter1(),null);

            if (cellEntityID != null) {
                String entID = cellEntityID.getCellType() == cellEntityID.CELL_TYPE_STRING ? cellEntityID.getStringCellValue() : new BigDecimal(cellEntityID.getNumericCellValue()).toString();
                if (!entID.equalsIgnoreCase("0")) {
                    invoiceList = null;

                    invoiceList = getARInvoiceByPolIdAndEntityID(cellNomorPolis.getStringCellValue(), stl.getStParameter1(), entID, null);
                }

            }

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
                    if (inv.getStUsedFlag().equalsIgnoreCase("Y") && inv.getDtReceipt() == null) {
                        bisaBayar = false;
                        cekGagal = true;
                        alasan = alasan + "<br>Polis " + cellNomorPolis.getStringCellValue() + " Sudah di entry di no bukti lain.";
                    }

                }

                if (bisaBayar) {
                    onNewInvoiceByInvoiceIDWithoutRecalculate(inv.getStARInvoiceID(), inv);
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

            DTOList invoiceList = getARInvoiceByPolId(cellNomorPolis.getStringCellValue(), stl.getStParameter1(),null);

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

    public void uploadExcel() throws Exception {
        if (receipt.getStARSettlementID().equalsIgnoreCase("14")) {
            uploadExcelKlaimKoas();
        } else if (receipt.getStARSettlementID().equalsIgnoreCase("2") || receipt.getStARSettlementID().equalsIgnoreCase("39")) {
            uploadExcelKomisi();
        } else if (receipt.getStARSettlementID().equalsIgnoreCase("38")) {
            uploadExcelSentralisasi();
        } else if (receipt.getStARSettlementID().equalsIgnoreCase("1")) {
            uploadExcelPremi();
        } else if (receipt.getStARSettlementID().equalsIgnoreCase("13")) {
            uploadExcelPremiKoas();
        }else if (receipt.getStARSettlementID().equalsIgnoreCase("8")) {
            uploadExcelPajak();
        }else if (receipt.getStARSettlementID().equalsIgnoreCase("11")) {
            uploadExcelKlaimOutward();
        }else if (receipt.getStARSettlementID().equalsIgnoreCase("9")) {
            uploadExcelPremiReasOutward();
        }
    }

    public void uploadExcelKomisi() throws Exception {

        String fileID = getReceipt().getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheetPolis = wb.getSheet("POLIS");

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
            HSSFCell cellReceiptDate = row.getCell(4);//nomor polis
            HSSFCell cellJenis = row.getCell(5);//nomor polis

            if (cellJenis == null) {
                throw new RuntimeException("Download ulang format konversi terbaru");
            }

            String jenisKomisi = cellJenis.getStringCellValue();
            String arTrxLineID = null;

            if (jenisKomisi.equalsIgnoreCase("ALL")) {
                arTrxLineID = " b.ar_trx_line_id in (8,24,40,9,25,41,10,26,42,82,83,84,92,93,94,129,130,131)";
            }

            if (jenisKomisi.equalsIgnoreCase("KOMISI")) {
                arTrxLineID = " b.ar_trx_line_id in (8,24,40)";
            }

            if (jenisKomisi.equalsIgnoreCase("BROKERAGE")) {
                arTrxLineID = " b.ar_trx_line_id in (9,25,41)";
            }

            if (jenisKomisi.equalsIgnoreCase("HANDLINGFEE")) {
                arTrxLineID = " b.ar_trx_line_id in (10,26,42)";
            }

            if (jenisKomisi.equalsIgnoreCase("PPN")) {
                arTrxLineID = " b.ar_trx_line_id in (82,83,84)";
            }

            if (jenisKomisi.equals("FEEBASE")) {
                arTrxLineID = " b.ar_trx_line_id in (92,93,94)";
            }

            if (jenisKomisi.equals("PPN FEEBASE")) {
                arTrxLineID = " b.ar_trx_line_id in (129,130,131)";
            }

            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, getReceipt().getStARSettlementID());

            DTOList invoiceList = getARInvoiceDetailsByPolId(cellNomorPolis.getStringCellValue(), stl.getStParameter1(), arTrxLineID);

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
                    if (inv.getStUsedFlag().equalsIgnoreCase("Y") && inv.getDtReceipt() == null) {
                        bisaBayar = false;
                        cekGagal = true;
                        alasan = alasan + "<br>Polis " + cellNomorPolis.getStringCellValue() + " Sudah di entry di no bukti lain.";
                    }

                }

                if (bisaBayar) {
                    onNewInvoiceByInvoiceIDWithoutRecalculate(inv.getStARInvoiceID(), inv);
                }
            }

            //ARInvoiceView inv = getARInvoiceUsingPolNo(cellNomorPolis.getStringCellValue(), stl.getStParameter1());

        }

        getReceipt().recalculate();

        if (cekGagal) {
            throw new RuntimeException("Polis gagal konversi : " + alasan);
        }

    }

   public ARInvoiceView getARInvoiceUsingPolNo(String attrpolid, String param) throws Exception {

       String sql = "select * from ar_invoice a where cc_code = ? and attr_pol_no = ?";

       sql = sql + " and " + param;

       sql = sql + " and amount_settled is null and coalesce(a.cancel_flag,'') <> 'Y'";

       if(receipt.getStARSettlementID().equalsIgnoreCase("13") || receipt.getStARSettlementID().equalsIgnoreCase("14")){
           sql = sql + " and a.ent_id not in (select refid1 from ent_master_coas)";
       }

       sql = sql + " limit 1";
       
       final ARInvoiceView iv = (ARInvoiceView) ListUtil.getDTOListFromQuery(
                sql,
                new Object [] {receipt.getStCostCenterCode(), attrpolid},
                ARInvoiceView.class
                ).getDTO();

        if(iv!=null) {
            iv.setDetails(
                    ListUtil.getDTOListFromQuery(
                    "select a.* from ar_invoice_details a,ar_invoice b where a.ar_invoice_id = b.ar_invoice_id and a.ar_invoice_id = ? and b.ar_trx_type_id in (5,6,7)",
                    new Object [] {iv.getStARInvoiceID()},
                    ARInvoiceDetailView.class
                    )

                    );
        }

        return iv;
    }

   private TitipanPremiView getARTitipanPremiUsingTrxNoAndCounter(String trx_no, String counter) throws Exception {
        final TitipanPremiView titipan = (TitipanPremiView) ListUtil.getDTOListFromQuery(
                "select * from ar_titipan_premi where cc_code = ? and trx_no = ? and counter = ? and coalesce(active_flag,'') <> 'N' and approved = 'Y' and balance <> 0",
                new Object [] {receipt.getStCostCenterCode(), trx_no, counter},
                TitipanPremiView.class
                ).getDTO();

        return titipan;
    }
   
   public DTOList getARInvoiceByAttrPolId(String invoice) throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("*");
        sqa.addQuery(" from ar_invoice");
        sqa.addClause(" attr_pol_id = ? and ar_trx_type_id in (5,6,7)");
        sqa.addClause(" (coalesce(cancel_flag,'') <> 'Y' or coalesce(posted_flag,'Y') = 'Y') ");
        sqa.addPar(invoice);
        sqa.addOrder(" ar_invoice_id");

        final DTOList l = sqa.getList(ARInvoiceView.class);

        return l;
    }

    public DTOList getARInvoiceByPolId(String attrpolid, String param, String install) throws Exception {

        String sql = "select * from ar_invoice a where ";

        if(attrpolid.contains("8080")){

            sql = sql + " a.cc_code in (?,'80')";

        }else{

            sql = sql + " a.cc_code  = ? ";
        }

        sql = sql + " and attr_pol_no = ? ";

        sql = sql + " and " + param;

        if (install != null) {
            sql = sql + " and a.invoice_no like '%-" + install + "'";
        }

        sql = sql + " and amount_settled is null and coalesce(a.cancel_flag,'') <> 'Y'";

        if (receipt.getStARSettlementID().equalsIgnoreCase("13") || receipt.getStARSettlementID().equalsIgnoreCase("14")) {
            sql = sql + " and a.ent_id not in (select refid1 from ent_master_coas)";
        }

        //sql = sql + " limit 1";

        return ListUtil.getDTOListFromQuery(
                sql,
                new Object[]{receipt.getStCostCenterCode(), attrpolid},
                ARInvoiceView.class);
    }

    public DTOList getARInvoiceByPolIdAndEntityID(String attrpolid, String param,String entityID, String noLKP) throws Exception {

       String sql = "select * from ar_invoice a where ent_id = ? and attr_pol_no = ?";

       sql = sql + " and " + param;

       sql = sql + " and amount_settled is null and coalesce(a.cancel_flag,'') <> 'Y'";

       if(noLKP!=null){
           sql = sql + " and refid2 = '"+ noLKP.trim().toUpperCase() +"'";
       }

       sql = sql + " and coalesce(cancel_flag,'N') <> 'Y' ";

       return ListUtil.getDTOListFromQuery(
                    sql ,
                    new Object [] {entityID, attrpolid},
                    ARInvoiceView.class
                    );
    }

   public void onNewInvoiceAllInstallment() throws Exception {

        validateInvoiceAlreadyIn(parinvoiceid);

        final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(parinvoiceid);

        //final DTOList installment = getARInvoiceByAttrPolId(invoice.getStAttrPolicyID());
        final DTOList installment = getARInvoiceByARInvoiceId(invoice.getStARInvoiceID());

        for (int i = 0; i < installment.size(); i++) {
            ARInvoiceView inv = (ARInvoiceView) installment.get(i);

            final ARReceiptLinesView rcl = new ARReceiptLinesView();
            rcl.markNew();

            rcl.setStInvoiceID(inv.getStARInvoiceID());
            rcl.setStInvoiceNo(inv.getStInvoiceNo());
            rcl.setStCurrencyCode(inv.getStCurrencyCode());
            rcl.setDbCurrencyRate(inv.getDbCurrencyRate());
            rcl.setDbInvoiceAmount(inv.getDbOutstandingAmount());
            rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
            rcl.setDtReceiptDate(receipt.getDtReceiptDate());
            rcl.setStPolicyID(inv.getStAttrPolicyID());

            //rcl.setStNegativeFlag(d.getStNegativeFlag());
            rcl.markAsInvoice();
            //rcl.markCommit();

            receipt.setStEntityID(inv.getStEntityID());

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

            onExpandInvoiceItem();

        }

    }

    public void clickPrintForAccounting() throws Exception {

        final DTOList l = clickPrintClaimFinance();

        SessionManager.getInstance().getRequest().setAttribute("RPT",l);

        super.redirect("/pages/ar/report/claimfinance.fop");
    }

    public DTOList clickPrintClaimFinance() throws Exception {
        /*final DTOList l = receipt.getDetails();

        SessionManager.getInstance().getRequest().setAttribute("RPT",l);

        //getRemoteInsurance().updateClaimRecap(l, this);

        super.redirect("/pages/ar/report/claimfinance.fop");
         */

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(
                " a.ar_invoice_id,a.cc_code,a.attr_pol_id,a.attr_pol_no,d.ref1 as attr_pol_name,a.refid2," +
                "a.amount,b.claim_date,a.receipt_no,a.receipt_date,a.amount_settled,b.claim_approved_date as mutation_date," +
                "c.claim_approved_date as due_date,d.rekap_kreasi as refx0," +
                "(SELECT SUM(coalesce(b.claim_amt*a.ccy_rate,0)) FROM ins_pol_coins b WHERE b.policy_id = a.ATTR_pol_id and b.entity_id <> 1 and b.coins_type = 'COINS') as entered_amount "
                );

        sqa.addQuery(" from ar_invoice a " +
                "inner join ins_policy b on b.pol_id = a.attr_pol_id " +
                "inner join ins_pol_obj d on d.ins_pol_obj_id = b.claim_object_id "
                );

        String sql = sqa.getSQL() + " left join ( select a.pla_no,a.claim_approved_date " +
                "from ins_policy a " +
                "inner join ins_pol_coins b on b.policy_id = a.pol_id and b.coins_type = 'COINS_COVER' " +
                "and b.entity_id = 1 and b.amount <> 0 " +
                "where a.claim_status = 'DLA' and a.status = 'CLAIM ENDORSE' " +
                "and a.pla_no in ( select a.refid1 from ar_invoice a" ;

        sql = sql + " where a.claim_no = ? ";
        sqa.addPar(stReceiptNo);

        sql = sql + " )) c on c.pla_no = a.refid1 where a.claim_no = ? ";
        sqa.addPar(stReceiptNo);

        sql = sql + " order by b.cc_code,b.pol_no ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                ARInvoiceView.class
                );

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;
    }

    public void onNewInvoiceByInvoiceIDWithoutRecalculate(String invoice_id, ARInvoiceView invoiceParam) throws Exception {

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
   }

   public void onExpandInvoiceItemWithoutRecalculate() throws Exception {
      final ARReceiptLinesView rl = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

      final ARInvoiceView invoice = rl.getInvoice();

      rl.setStExpandedFlag("Y");

      final DTOList details = invoice.getDetailsWithoutOrder();

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

         if(d.isFeeBase3()){
             DTOList invoicePPN = getARInvoicePPNFeeBase(invoice.getStAttrPolicyNo());

             if(invoicePPN.size()>0){
                 ARInvoiceView invPPN = (ARInvoiceView) invoicePPN.get(0);

                 onNewInvoicePembayaranKomisiByArInvoiceIDWithoutOrder(invPPN.getStARInvoiceID());
             }

         }
      }

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

    public void clickValidate() throws Exception {
        final DTOList l = receipt.getDetails();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        getRemoteInsurance().updateValidateClaim(l);

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

    public void clickCancel() throws Exception {
        final DTOList l = receipt.getDetails();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        getRemoteInsurance().updateCancelClaimRecap(l, this);

        onDeleteAll();

    }

    public void validateInvoiceAlreadyIn2(String arInvoiceID) throws Exception {
        final DTOList detail = receipt.getReceiptDetails(arInvoiceID);

        for (int i = 0; i < detail.size(); i++) {
            ARReceiptLinesView rl = (ARReceiptLinesView) detail.get(i);

            if (rl.getInvoice().getStARInvoiceID().equalsIgnoreCase(arInvoiceID)) {
                throw new RuntimeException("Invoice " + arInvoiceID + " sudah pernah dibayar sebelumnya pada Nobuk " + rl.getStReceiptNo());
            } 
        }
    }

    public void clickPrintExcelMonitoring() throws Exception {
        final DTOList l = receipt.getDetails();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        EXPORT_MONITORING();

        if (stReceiptNo != null) {
            getRemoteInsurance().updateMonitoring(l, this);
        }

    }

    public void EXPORT_MONITORING() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            //HashDTO h = (HashDTO) list.get(i);
            ARReceiptLinesView view = (ARReceiptLinesView) list.get(i);

            BigDecimal premiGrossTotal = null;
            BigDecimal commissionTotal = null;
            BigDecimal commissionFeeBaseTotal = null;
            BigDecimal brokerageTotal = null;
            BigDecimal hfeeTotal = null;
            BigDecimal PPNTotal = null;
            BigDecimal pcostTotal = null;
            BigDecimal stampdutyTotal = null;
            BigDecimal taxCommTotal = null;
            BigDecimal tagihBruto = null;
            BigDecimal tagihNetto = null;
            BigDecimal diskonTotal = null;
            BigDecimal taxBrokTotal = null;
            BigDecimal taxFeeTotal = null;
            BigDecimal commissionPlusTaxTotal = null;
            BigDecimal brokeragePlusTaxTotal = null;
            BigDecimal handlingPlusTaxTotal = null;
            BigDecimal CommBrokHfeeTotal = null;
            BigDecimal TaxCommBrokHfeeTotal = null;
            BigDecimal PpnBfee = null;
            BigDecimal PpnFbase = null;
            BigDecimal PpnKomisi = null;

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            if (getStReceiptNo() != null) {
                row0.createCell(0).setCellValue("No Rekap Monitoring: " + getStReceiptNo() + " - " + getStName());
            }

            XSSFRow row1 = sheet.createRow(2);
            row1.createCell(0).setCellValue("Tanggal");
            row1.createCell(1).setCellValue("No. Polis");
            row1.createCell(2).setCellValue("Premi Bruto");
            row1.createCell(3).setCellValue("Diskon");
            row1.createCell(4).setCellValue("Biaya Polis");
            row1.createCell(5).setCellValue("Biaya Materai");
            row1.createCell(6).setCellValue("Tagihan Bruto");
            row1.createCell(7).setCellValue("Komisi");
            row1.createCell(8).setCellValue("Pajak Komisi");
            row1.createCell(9).setCellValue("Broker Fee");
            row1.createCell(10).setCellValue("Pajak Broker");
            row1.createCell(11).setCellValue("Handling Fee");
            row1.createCell(12).setCellValue("Fee Base");
//            row1.createCell(13).setCellValue("PPN");
            row1.createCell(13).setCellValue("Ppn Bfee");
            row1.createCell(14).setCellValue("Ppn Fbase");
            row1.createCell(15).setCellValue("Ppn Komisi");
            row1.createCell(16).setCellValue("Tagihan Netto");
            row1.createCell(17).setCellValue("Kurs");
            row1.createCell(18).setCellValue("Curr");
            row1.createCell(19).setCellValue("No Bukti Keseluruhan");
//            row1.createCell(16).setCellValue("Tanggal Bayar");

            final DTOList arInvoiceDetail = view.getARInvoiceDetails();
            for (int k = 0; k < arInvoiceDetail.size(); k++) {
                ARInvoiceDetailView detil = (ARInvoiceDetailView) arInvoiceDetail.get(k);

                BigDecimal amount = detil.getDbAmount();
                BigDecimal enteredAmount = detil.getDbEnteredAmount();

                if (detil.isPremiGross2()) {
                    premiGrossTotal = BDUtil.add(premiGrossTotal, enteredAmount);
                }

                if (detil.isCommission2() && !detil.isFeeBase3() && !detil.isPPNFeebase()) {
                    commissionTotal = BDUtil.add(commissionTotal, enteredAmount);
                }

                if (detil.isCommission2() && detil.isFeeBase3()) {
                    commissionFeeBaseTotal = BDUtil.add(commissionFeeBaseTotal, enteredAmount);
                }

                if (detil.isBrokerage2() && !detil.isPPN()) {
                    brokerageTotal = BDUtil.add(brokerageTotal, enteredAmount);
                }

                if (detil.isHandlingFee2()) {
                    hfeeTotal = BDUtil.add(hfeeTotal, enteredAmount);
                }

                if (detil.isPolicyCost2()) {
                    pcostTotal = BDUtil.add(pcostTotal, enteredAmount);
                }

                if (detil.isStampDuty2()) {
                    stampdutyTotal = BDUtil.add(stampdutyTotal, enteredAmount);
                }

                if (detil.isTaxComm()) {
                    taxCommTotal = BDUtil.add(taxCommTotal, enteredAmount);
                }

                if (detil.isDiscount2()) {
                    diskonTotal = BDUtil.add(diskonTotal, enteredAmount);
                }

                if (detil.isTaxBrok()) {
                    taxBrokTotal = BDUtil.add(taxBrokTotal, enteredAmount);
                }

                if (detil.isTaxHFee()) {
                    taxFeeTotal = BDUtil.add(taxFeeTotal, enteredAmount);
                }

                if (detil.isPPN() || detil.isPPNFeebase()) {
                    PPNTotal = BDUtil.add(PPNTotal, enteredAmount);
                }

                if (detil.isPPN()) {
                    PpnBfee = BDUtil.add(PpnBfee, amount);
                }

                if (detil.isPPNFeebase()) {
                    PpnFbase = BDUtil.add(PpnFbase, amount);
                }

                if (detil.isPPNKomisi()) {
                    PpnKomisi = BDUtil.add(PpnKomisi, amount);
                }

                commissionPlusTaxTotal = BDUtil.add(commissionTotal, taxCommTotal);
                brokeragePlusTaxTotal = BDUtil.add(brokerageTotal, taxBrokTotal);
                handlingPlusTaxTotal = BDUtil.add(hfeeTotal, taxFeeTotal);

                logger.logDebug("@@@@@@@@@@@@@@@@@@@1" + commissionTotal);
                logger.logDebug("@@@@@@@@@@@@@@@@@@@2" + taxCommTotal);

                CommBrokHfeeTotal = BDUtil.add(commissionPlusTaxTotal, brokeragePlusTaxTotal);
                CommBrokHfeeTotal = BDUtil.add(CommBrokHfeeTotal, handlingPlusTaxTotal);
//                CommBrokHfeeTotal = BDUtil.add(CommBrokHfeeTotal, PPNTotal);
                CommBrokHfeeTotal = BDUtil.add(CommBrokHfeeTotal, PpnBfee);
                CommBrokHfeeTotal = BDUtil.add(CommBrokHfeeTotal, PpnFbase);
                CommBrokHfeeTotal = BDUtil.add(CommBrokHfeeTotal, PpnKomisi);

                TaxCommBrokHfeeTotal = BDUtil.add(taxCommTotal, taxBrokTotal);
                TaxCommBrokHfeeTotal = BDUtil.add(TaxCommBrokHfeeTotal, taxFeeTotal);

                tagihBruto = BDUtil.add(BDUtil.add(premiGrossTotal, pcostTotal), stampdutyTotal);

                tagihBruto = BDUtil.sub(tagihBruto, diskonTotal);

                tagihNetto = BDUtil.sub(tagihBruto, BDUtil.sub(CommBrokHfeeTotal, TaxCommBrokHfeeTotal));
                tagihNetto = BDUtil.sub(tagihNetto, commissionFeeBaseTotal);
            }

            //bikin isi cell
            XSSFRow row = sheet.createRow(i + 3);
            row.createCell(0).setCellValue(view.getInvoice().getDtMutationDate());
            row.createCell(1).setCellValue(view.getInvoice().getStInvoiceNo()); //.substring(0, 4) + "-" + view.getInvoice().getStAttrPolicyNo().substring(4, 8) + "-" + view.getInvoice().getStAttrPolicyNo().substring(8, 12) + "-" + view.getInvoice().getStAttrPolicyNo().substring(12, 16) + "-" + view.getInvoice().getStAttrPolicyNo().substring(16, 18));
            if (premiGrossTotal != null) {
                row.createCell(2).setCellValue(premiGrossTotal.doubleValue());
            }
            if (diskonTotal != null) {
                row.createCell(3).setCellValue(diskonTotal.doubleValue());
            }
            if (pcostTotal != null) {
                row.createCell(4).setCellValue(pcostTotal.doubleValue());
            }
            if (stampdutyTotal != null) {
                row.createCell(5).setCellValue(stampdutyTotal.doubleValue());
            }
            if (tagihBruto != null) {
                row.createCell(6).setCellValue(tagihBruto.doubleValue());
            }
            if (commissionPlusTaxTotal != null) {
                row.createCell(7).setCellValue(commissionPlusTaxTotal.doubleValue());
            }
            if (taxCommTotal != null) {
                row.createCell(8).setCellValue(taxCommTotal.doubleValue());
            }
            if (brokeragePlusTaxTotal != null) {
                row.createCell(9).setCellValue(brokeragePlusTaxTotal.doubleValue());
            }
            if (taxBrokTotal != null) {
                row.createCell(10).setCellValue(taxBrokTotal.doubleValue());
            }
            if (handlingPlusTaxTotal != null) {
                row.createCell(11).setCellValue(handlingPlusTaxTotal.doubleValue());
            }
            if (commissionFeeBaseTotal != null) {
                row.createCell(12).setCellValue(commissionFeeBaseTotal.doubleValue());
            }
//            if (PPNTotal != null) {
//                row.createCell(13).setCellValue(PPNTotal.doubleValue());
//            }
            if (PpnBfee != null) {
                row.createCell(13).setCellValue(PpnBfee.doubleValue());
            }
            if (PpnFbase != null) {
                row.createCell(14).setCellValue(PpnFbase.doubleValue());
            }
            if (PpnKomisi != null) {
                row.createCell(15).setCellValue(PpnKomisi.doubleValue());
            }
            if (tagihNetto != null) {
                row.createCell(16).setCellValue(tagihNetto.doubleValue());
            }
//            if (view.getInvoice().getStReceiptNo() != null) {
//                row.createCell(15).setCellValue(view.getInvoice().getStReceiptNo());
//            }
//            if (view.getInvoice().getDtReceipt() != null) {
//                row.createCell(16).setCellValue(view.getInvoice().getDtReceipt());
//            }

            row.createCell(17).setCellValue(view.getInvoice().getStCurrencyCode());
            row.createCell(18).setCellValue(view.getInvoice().getDbCurrencyRate().doubleValue());

            if (view.getHistoryTrx() != null) {
                String param = view.getHistoryTrx();
                String[] param2 = param.split("[\\,]");

                for (int k = 0; k < param2.length; k++) {
                    row.createCell(19 + k).setCellValue(param2[k]);
                }

            }
        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=pengajuan_klaim.xlsx;");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    public DTOList getARInvoiceByPolId2(String attrpolno) throws Exception {

        String sql = " select * from ar_invoice a where attr_pol_no = ? "
                + " and a.ar_trx_type_id in (5,6,7) and coalesce(a.cancel_flag,'N') <> 'Y' and coalesce(a.posted_flag,'Y') = 'Y' ";

        return ListUtil.getDTOListFromQuery(
                sql,
                new Object[]{attrpolno},
                ARInvoiceView.class);
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


    public DTOList getARInvoiceDetailsByPolId(String attrpolid, String param, String trxLineID) throws Exception {

        String sql = "select a.* from ar_invoice a inner join ar_invoice_details b on a.ar_invoice_id = b.ar_invoice_id where a.attr_pol_no = ?";

        sql = sql + " and " + param;

        sql = sql + " and a.amount_settled is null and coalesce(a.cancel_flag,'') <> 'Y'";

        if (trxLineID != null) {
            sql = sql + " and " + trxLineID;
        }

        if (receipt.getStARSettlementID().equalsIgnoreCase("13") || receipt.getStARSettlementID().equalsIgnoreCase("14")) {
            sql = sql + " and a.ent_id not in (select refid1 from ent_master_coas)";
        }

        //sql = sql + " limit 1";

        return ListUtil.getDTOListFromQuery(
                sql,
                new Object[]{attrpolid},
                ARInvoiceView.class);
    }


    public void reverseReas() throws Exception{

        if(isPosted()){
            String tahun = receipt.getStYears();
            throw new RuntimeException("Transaksi bulan "+ receipt.getStMonths() +" dan tahun "+ tahun +" tsb sudah diposting");
        }

        getRemoteAccountReceivable().reverse(receipt, receipt.getStReceiptNo());

        super.close();
    }

    public void onNewSuratHutangReas() throws Exception {

        ARInvoiceView invoice = new ARInvoiceView();
        invoice.setStNoSuratHutang(nosurathutang);
        invoice.setDtSuratHutangPeriodFrom(shpstart);
        invoice.setDtSuratHutangPeriodTo(shpend);
        invoice.setDtAttrPolicyPeriodStart(periodstart);
        invoice.setDtAttrPolicyPeriodEnd(periodend);
        invoice.setStAttrPolicyTypeID(receipt.getStPolicyTypeID());
        invoice.setStEntityID(receipt.getStEntityID());

        final DTOList listInv = invoice.getList4();

        descLong = descLong + "\n" + nosurathutang;

        for (int i = 0; i < listInv.size(); i++) {
            //ARInvoiceView invoiceView = (ARInvoiceView) listInv.get(i);
            final HashDTO dto = (HashDTO) listInv.get(i);

            boolean samedata = false;

            if (i > 0) {
                final HashDTO dto2 = (HashDTO) listInv.get(i - 1);
                String invoice_id = dto2.getFieldValueByFieldNameBD("ar_invoice_id").toString();

                if (dto.getFieldValueByFieldNameBD("ar_invoice_id").toString().equalsIgnoreCase(invoice_id)) {
                    samedata = true;
                }
            }

            //parinvoiceid = invoiceView.getStARInvoiceID();

            onNewInvoiceByNoSuratHutang(dto, samedata);
        }

        receipt.setStDescription("Pembayaran " + descLong);
        receipt.setStShortDescription(receipt.getStDescription());
    }

    public void uploadExcelSentralisasi() throws Exception {

        String fileID = getReceipt().getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheetPolis = wb.getSheet("POLIS");

        HSSFSheet sheetReferensi = wb.getSheet("REFERENSI_BAYAR");

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
            HSSFCell cellReceiptDate = row.getCell(4);//nomor polis
            HSSFCell cellJenis = row.getCell(5);//jenis komisi
            HSSFCell cellMetode = row.getCell(6);//metode

            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, getReceipt().getStARSettlementID());

            DTOList invoiceList = getARInvoice(cellNomorPolis.getStringCellValue(), stl.getStParameter1());

            String jenisKomisi = cellJenis.getStringCellValue();
            String metode = cellMetode.getStringCellValue();

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
                    if (inv.getStUsedFlag().equalsIgnoreCase("Y") && inv.getDtReceipt() == null) {
                        bisaBayar = false;
                        cekGagal = true;
                        alasan = alasan + "<br>Polis " + cellNomorPolis.getStringCellValue() + " Sudah di entry di no bukti lain.";
                    }

                }

                if (bisaBayar) {
                    onNewInvoiceByInvoiceIDSentralisasi(inv.getStARInvoiceID(), inv, jenisKomisi, metode, null, null,null,null,null);
                }
            }

        }


        //cek sheet by referensi bayar --STEP 2
        
        if(sheetReferensi!=null){
            int rowsReferensi = sheetReferensi.getPhysicalNumberOfRows();
        
            for (int s = 5; s <= rowsReferensi; s++) {
                HSSFRow row = sheetReferensi.getRow(s);

                HSSFCell cellControl = row.getCell(1);
                //if(cellControl==null) break;

                if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                    break;
                }

                HSSFCell cellNomorReferensi = row.getCell(3);//nomor polis
                HSSFCell cellReceiptDate = row.getCell(4);//nomor polis
                HSSFCell cellJenis = row.getCell(5);//jenis komisi
                HSSFCell cellJumlahBayar = row.getCell(6);//jumlah bayar
                HSSFCell cellMetode = row.getCell(7);//metode

                String deskripsiReferensi = cellNomorReferensi.getStringCellValue();

                if(!deskripsiReferensi.trim().toUpperCase().contains("FFFFFF")) continue;

                String [] tempReferensi = deskripsiReferensi.split("FFFFFF");

                String nomorReferensi =  tempReferensi[1].substring(0, 11) +" - "+ tempReferensi[1].substring(11,16);

                ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, getReceipt().getStARSettlementID());

                DTOList invoiceList = getARInvoiceFlexible("reference_no", stl.getStParameter1(), nomorReferensi);

                String jenisKomisi = cellJenis.getStringCellValue();
                String metode = cellMetode.getStringCellValue();

                for (int i = 0; i < invoiceList.size(); i++) {
                    ARInvoiceView inv = (ARInvoiceView) invoiceList.get(i);

                    if (inv == null) {
                        alasan = alasan + "<br>Tagihan No Referensi " + cellNomorReferensi.getStringCellValue() + " tidak ditemukan.";
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
                        alasan = alasan + "<br>Tanggal Persetujuan Polis " + polis.getStPolicyNo() + " > Tanggal Pembukuan.";
                    }

                    if (inv.getDtReceipt() != null) {
                        bisaBayar = false;
                        cekGagal = true;
                        alasan = alasan + "<br>Polis " + polis.getStPolicyNo() + " Sudah dibayar.";

                    }

                    if (inv.getStUsedFlag() != null) {
                        if (inv.getStUsedFlag().equalsIgnoreCase("Y") && inv.getDtReceipt() == null) {
                            bisaBayar = false;
                            cekGagal = true;
                            alasan = alasan + "<br>Polis " + polis.getStPolicyNo() + " Sudah di entry di no bukti lain.";
                        }
                    }

                    if (bisaBayar) {
                        onNewInvoiceByInvoiceIDSentralisasi(inv.getStARInvoiceID(), inv, jenisKomisi, metode, null, null,null,null,null);
                    }
                }

            }
        }


        getReceipt().recalculate();

        if (cekGagal) {
            throw new RuntimeException("Polis gagal konversi : " + alasan);
        }

    }

    public void onNewInvoiceByInvoiceIDSentralisasi(String invoice_id, ARInvoiceView invoiceParam, String jenis, String metode, String polis2, String polis3, String polis4, String polis5, String polis6) throws Exception {

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

      if(metode!=null){

          String trxMethod = "";

          if(metode.equalsIgnoreCase("BROKER")) trxMethod = "1";
          else if(metode.equalsIgnoreCase("TRANSFER")) trxMethod = "2";
          else if(metode.equalsIgnoreCase("TUNAI")) trxMethod = "3";

          rcl.setStTransactionMethod(trxMethod);
      }


      rcl.markAsInvoice();

      receipt.setStEntityID(invoice.getStEntityID());

      if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());
      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());

      receipt.getDetails().add(rcl);

      if(receipt.getDetails().size()>1){
          receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " Polis");
          receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " Polis");
      }

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandInvoiceItemSentralisasi(jenis, polis2, polis3, polis4, polis5, polis6);

   }

   public void onExpandInvoiceItemSentralisasi(String jenis, String polis2, String polis3, String polis4, String polis5, String polis6) throws Exception {
      final ARReceiptLinesView rl = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

      final ARInvoiceView invoice = rl.getInvoice();

      rl.setStExpandedFlag("Y");

      final DTOList details = invoice.getDetails();

      for (int i = 0; i < details.size(); i++) {
         ARInvoiceDetailView d = (ARInvoiceDetailView) details.get(i);

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

          //ksg detail REMARK KSG
         /*
         if(invoice.isPolicyKSG()){
             rcl.setDbAmount100(d.getDbAmount100());
             rcl.setDbAmount1(d.getDbAmount1());
             rcl.setDbAmount2(d.getDbAmount2());
             rcl.setDbAmount3(d.getDbAmount3());
             rcl.setDbAmount4(d.getDbAmount4());
             rcl.setDbAmount5(d.getDbAmount5());
             rcl.setDbAmount6(d.getDbAmount6());

             rcl.setDbTaxAmount100(d.getDbTaxAmount100());
             rcl.setDbTaxAmount1(d.getDbTaxAmount1());
             rcl.setDbTaxAmount2(d.getDbTaxAmount2());
             rcl.setDbTaxAmount3(d.getDbTaxAmount3());
             rcl.setDbTaxAmount4(d.getDbTaxAmount4());
             rcl.setDbTaxAmount5(d.getDbTaxAmount5());
             rcl.setDbTaxAmount6(d.getDbTaxAmount6());
         }*/
         

         boolean isTax = d.isTaxComm() || d.isTaxBrok() || d.isTaxHFee();

         if ((d.isComission() || d.isPPNFeebase() || d.isPPN() || d.isPPNKomisi()) && !isTax){
             if(jenis.equalsIgnoreCase("FEEBASE")){
                if(d.isFeeBase3()){
                    rcl.setStCheck("Y");
                }
             }

             if(jenis.equalsIgnoreCase("KOMISI")){
                    if(d.isKomisi2())
                         rcl.setStCheck("Y");
             }

             if(jenis.equalsIgnoreCase("ALL")){
                 rcl.setStCheck("Y");
             }

             if(jenis.equalsIgnoreCase("NONE")){
                rcl.setStCheck("N");
             }

             if(jenis.equalsIgnoreCase("BROKERFEE")){
                    if(d.isBrokerage2())
                         rcl.setStCheck("Y");
             }

             //KSG REMARK KSG
             /*
             if(invoice.isPolicyKSG()){
                 //BROKERFEE
                 if(polis2.equalsIgnoreCase("BROKERFEE")){
                     if(d.isBrokerage2())
                         if(!BDUtil.isZeroOrNull(rcl.getDbAmount2())) rcl.setStCheck2("Y");
                 }

                 if(polis3.equalsIgnoreCase("BROKERFEE")){
                     if(d.isBrokerage2())
                         if(!BDUtil.isZeroOrNull(rcl.getDbAmount3())) rcl.setStCheck3("Y");
                 }

                 if(polis4.equalsIgnoreCase("BROKERFEE")){
                     if(d.isBrokerage2())
                         if(!BDUtil.isZeroOrNull(rcl.getDbAmount4())) rcl.setStCheck4("Y");
                 }

                 if(polis5.equalsIgnoreCase("BROKERFEE")){
                     if(d.isBrokerage2())
                         if(!BDUtil.isZeroOrNull(rcl.getDbAmount5())) rcl.setStCheck5("Y");
                 }

                 if(polis6.equalsIgnoreCase("BROKERFEE")){
                     if(d.isBrokerage2())
                         if(!BDUtil.isZeroOrNull(rcl.getDbAmount6())) rcl.setStCheck6("Y");
                 }

                 //FEEBASE
                 if(polis2.equalsIgnoreCase("FEEBASE")){
                     if(d.isFeeBase3())
                         if(!BDUtil.isZeroOrNull(rcl.getDbAmount2())) rcl.setStCheck2("Y");
                 }

                 if(polis3.equalsIgnoreCase("FEEBASE")){
                     if(d.isFeeBase3())
                         if(!BDUtil.isZeroOrNull(rcl.getDbAmount3())) rcl.setStCheck3("Y");
                 }

                 if(polis4.equalsIgnoreCase("FEEBASE")){
                     if(d.isFeeBase3())
                         if(!BDUtil.isZeroOrNull(rcl.getDbAmount4())) rcl.setStCheck4("Y");
                 }

                 if(polis5.equalsIgnoreCase("FEEBASE")){
                     if(d.isFeeBase3())
                         if(!BDUtil.isZeroOrNull(rcl.getDbAmount5())) rcl.setStCheck5("Y");
                 }

                 if(polis6.equalsIgnoreCase("FEEBASE")){
                     if(d.isFeeBase3())
                         if(!BDUtil.isZeroOrNull(rcl.getDbAmount6())) rcl.setStCheck6("Y");
                 }

                 //KOMISI
                 if(polis2.equalsIgnoreCase("KOMISI")){
                     if(d.isKomisi2())
                         if(!BDUtil.isZeroOrNull(rcl.getDbAmount2())) rcl.setStCheck2("Y");
                 }

                 if(polis3.equalsIgnoreCase("KOMISI")){
                     if(d.isKomisi2())
                         if(!BDUtil.isZeroOrNull(rcl.getDbAmount3())) rcl.setStCheck3("Y");
                 }

                 if(polis4.equalsIgnoreCase("KOMISI")){
                     if(d.isKomisi2())
                         if(!BDUtil.isZeroOrNull(rcl.getDbAmount4())) rcl.setStCheck4("Y");
                 }

                 if(polis5.equalsIgnoreCase("KOMISI")){
                     if(d.isKomisi2())
                         if(!BDUtil.isZeroOrNull(rcl.getDbAmount5())) rcl.setStCheck5("Y");
                 }

                 if(polis6.equalsIgnoreCase("KOMISI")){
                     if(d.isKomisi2())
                         if(!BDUtil.isZeroOrNull(rcl.getDbAmount6())) rcl.setStCheck6("Y");
                 }

                 //ALL
                 if(polis2.equalsIgnoreCase("ALL")){
                     if(!BDUtil.isZeroOrNull(rcl.getDbAmount2())) rcl.setStCheck2("Y");
                 }

                 if(polis3.equalsIgnoreCase("ALL")){
                     if(!BDUtil.isZeroOrNull(rcl.getDbAmount3())) rcl.setStCheck3("Y");
                 }

                 if(polis4.equalsIgnoreCase("ALL")){
                     if(!BDUtil.isZeroOrNull(rcl.getDbAmount4())) rcl.setStCheck4("Y");
                 }

                 if(polis5.equalsIgnoreCase("ALL")){
                     if(!BDUtil.isZeroOrNull(rcl.getDbAmount5())) rcl.setStCheck5("Y");
                 }

                 if(polis6.equalsIgnoreCase("ALL")){
                     if(!BDUtil.isZeroOrNull(rcl.getDbAmount6())) rcl.setStCheck6("Y");
                 }

                 //NONE
                 //ALL
                 if(polis2.equalsIgnoreCase("NONE")){
                     rcl.setStCheck2("N");
                 }

                 if(polis3.equalsIgnoreCase("NONE")){
                     rcl.setStCheck3("N");
                 }

                 if(polis4.equalsIgnoreCase("NONE")){
                     rcl.setStCheck4("N");
                 }

                 if(polis5.equalsIgnoreCase("NONE")){
                     rcl.setStCheck5("N");
                 }

                 if(polis6.equalsIgnoreCase("NONE")){
                     rcl.setStCheck6("N");
                 }
             }*/
             


         }else{
            if(!isTax){
                rcl.setStCheck("Y");
            }

            //ksg detail REMARK KSG
            /*
             if(!isTax){
                 if(invoice.isPolicyKSG()){
                     if(!BDUtil.isZeroOrNull(d.getDbAmount2())) rcl.setStCheck2("Y");
                     if(!BDUtil.isZeroOrNull(d.getDbAmount3())) rcl.setStCheck3("Y");
                     if(!BDUtil.isZeroOrNull(d.getDbAmount4())) rcl.setStCheck4("Y");
                     if(!BDUtil.isZeroOrNull(d.getDbAmount5())) rcl.setStCheck5("Y");
                     if(!BDUtil.isZeroOrNull(d.getDbAmount6())) rcl.setStCheck6("Y");
                 }
             }*/
         }

         rcl.setStLock(true);

         if(lockCheck(rcl)){
         	 rcl.setStLock(false);
         }

         if (d.isComission()) rcl.markAsComission();

         rl.getDetails().add(rcl);
      }

   }

   public DTOList getARInvoice(String attrpolid, String param) throws Exception {

       String sql = "select * from ar_invoice a where attr_pol_no = ?";

       sql = sql + " and " + param;

       sql = sql + " and amount_settled is null and coalesce(a.cancel_flag,'') <> 'Y'";

       if(receipt.getStARSettlementID().equalsIgnoreCase("13") || receipt.getStARSettlementID().equalsIgnoreCase("14")){
           sql = sql + " and a.ent_id not in (select refid1 from ent_master_coas)";
       }

        return ListUtil.getDTOListFromQuery(
                    sql ,
                    new Object [] {attrpolid},
                    ARInvoiceView.class
                    );
    }

   public DTOList getARInvoiceFlexible(String fieldName, String param, String noReferensi) throws Exception {

       String sql = "select * from ar_invoice a where " + fieldName + " = ?";

       sql = sql + " and " + param;

       sql = sql + " and amount_settled is null and coalesce(a.cancel_flag,'') <> 'Y'";

       if(receipt.getStARSettlementID().equalsIgnoreCase("13") || receipt.getStARSettlementID().equalsIgnoreCase("14")){
           sql = sql + " and a.ent_id not in (select refid1 from ent_master_coas)";
       }

        return ListUtil.getDTOListFromQuery(
                    sql ,
                    new Object [] {noReferensi},
                    ARInvoiceView.class
                    );
    }



    /**
     * @return the stInsuranceTreatyTypeDesc
     */
    public String getStInsuranceTreatyTypeDesc() {
        return stInsuranceTreatyTypeDesc;
    }

    /**
     * @param stInsuranceTreatyTypeDesc the stInsuranceTreatyTypeDesc to set
     */
    public void setStInsuranceTreatyTypeDesc(String stInsuranceTreatyTypeDesc) {
        this.stInsuranceTreatyTypeDesc = stInsuranceTreatyTypeDesc;
    }

    public void onNewSuratHutangComm() throws Exception {
        final ARInvoiceView invoice = getRemoteAccountReceivable().getSuratHutang(nosurathutang);

        invoice.setStNoSuratHutang(nosurathutang);

        descLong = descLong + "\n" + nosurathutang;

        final DTOList listInv = invoice.getListDetailsSuratHutangComm();

        for (int i = 0; i < listInv.size(); i++) {
            ARInvoiceView invoiceView = (ARInvoiceView) listInv.get(i);
            parinvoiceid = invoiceView.getStARInvoiceID();
            onNewInvoiceBySuratHutangComm(nosurathutang);
        }

        receipt.setStDescription("Pembayaran " + descLong);
        receipt.setStShortDescription(receipt.getStDescription());

    }

    public void onNewInvoiceBySuratHutangComm(String nosurathutang) throws Exception {
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

        onExpandInvoiceItemPembayaranKomisi();
    }

    /**
     * @return the nosurathutangrekap
     */
    public String getNosurathutangrekap() {
        return nosurathutangrekap;
    }

    /**
     * @param nosurathutangrekap the nosurathutangrekap to set
     */
    public void setNosurathutangrekap(String nosurathutangrekap) {
        this.nosurathutangrekap = nosurathutangrekap;
    }

    public void onNewSuratHutangReasRekap() throws Exception {
        ARInvoiceView invoice = new ARInvoiceView();
        invoice.setStNoSuratHutang(nosurathutangrekap);
        invoice.setStInvoiceType(receipt.getStReinsTypePayment());
        invoice.setStAttrPolicyTypeID(receipt.getStPolicyTypeID());
        invoice.setStEntityID(receipt.getStEntityID());

        DTOList listInv = invoice.getList5();

        descLong = descLong + "\n" + nosurathutangrekap;
        for (int i = 0; i < listInv.size(); i++) {
            //ARInvoiceView invoiceView = (ARInvoiceView) listInv.get(i);
            HashDTO dto = (HashDTO) listInv.get(i);

            boolean samedata = false;

            if (i > 0) {
                HashDTO dto2 = (HashDTO) listInv.get(i - 1);
                String invoice_id = dto2.getFieldValueByFieldNameBD("ar_invoice_id").toString();

                if (dto.getFieldValueByFieldNameBD("ar_invoice_id").toString().equalsIgnoreCase(invoice_id)) {
                    samedata = true;
                }
            }

            //parinvoiceid = invoiceView.getStARInvoiceID();

            onNewInvoiceByNoSuratHutang(dto, samedata);
        }

        receipt.setStDescription("Pembayaran " + descLong);
        receipt.setStShortDescription(receipt.getStDescription());
    }

    /**
     * @return the shpstart
     */
    public Date getShpstart() {
        return shpstart;
    }

    /**
     * @param shpstart the shpstart to set
     */
    public void setShpstart(Date shpstart) {
        this.shpstart = shpstart;
    }

    /**
     * @return the shpend
     */
    public Date getShpend() {
        return shpend;
    }

    /**
     * @param shpend the shpend to set
     */
    public void setShpend(Date shpend) {
        this.shpend = shpend;
    }

    public DTOList getARInvoiceByARInvoiceId(String invoice) throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("*");
        sqa.addQuery(" from ar_invoice");
        sqa.addClause(" ar_invoice_id = ? and ar_trx_type_id in (5,6,7,10)");
        sqa.addClause(" (coalesce(cancel_flag,'') <> 'Y' and coalesce(posted_flag,'Y') = 'Y') ");
        sqa.addPar(invoice);
        sqa.addOrder(" ar_invoice_id");

        final DTOList l = sqa.getList(ARInvoiceView.class);

        return l;
    }

    public void uploadExcelKlaimKoas() throws Exception {

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
            HSSFCell cellEntityID = row.getCell(6);//entity id

            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, getReceipt().getStARSettlementID());

            DTOList invoiceList = getARInvoiceByPolId(cellNomorPolis.getStringCellValue(), stl.getStParameter1(), null);

            if (cellEntityID != null) {
                String entID = cellEntityID.getCellType() == cellEntityID.CELL_TYPE_STRING ? cellEntityID.getStringCellValue() : new BigDecimal(cellEntityID.getNumericCellValue()).toString();
                if (!entID.equalsIgnoreCase("0")) {
                    invoiceList = null;

                    invoiceList = getARInvoiceByPolIdAndEntityID(cellNomorPolis.getStringCellValue(), stl.getStParameter1(), entID, cellNoLKP.getStringCellValue());
                }
            }


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
                    onNewInvoiceByInvoiceIDWithoutRecalculate(inv.getStARInvoiceID(), inv);
                }
            }

            //ARInvoiceView inv = getARInvoiceUsingPolNo(cellNomorPolis.getStringCellValue(), stl.getStParameter1());

        }

        getReceipt().recalculate();

        if (cekGagal) {
            throw new RuntimeException("Polis gagal konversi : " + alasan);
        }

    }

     public void uploadExcelPremi() throws Exception {

        String fileID = getReceipt().getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheetPolis = wb.getSheet("POLIS");

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
            HSSFCell cellReceiptDate = row.getCell(4);//nomor polis
            HSSFCell cellJenis = row.getCell(5);//jenis komisi
//            HSSFCell cellEntityID = row.getCell(6);//nomor polis
            HSSFCell cellInstall = row.getCell(6);//installment
            HSSFCell cellMetode = row.getCell(7);//metode

            HSSFCell cellPolis2 = row.getCell(8); //polis 2
            HSSFCell cellPolis3 = row.getCell(9); //polis 3
            HSSFCell cellPolis4 = row.getCell(10); //polis 4
            HSSFCell cellPolis5 = row.getCell(11); //polis 5
            HSSFCell cellPolis6 = row.getCell(12); //polis 6

            String polis2 = cellPolis2!=null?cellPolis2.getStringCellValue():"";
            String polis3 = cellPolis3!=null?cellPolis3.getStringCellValue():"";
            String polis4 = cellPolis4!=null?cellPolis4.getStringCellValue():"";
            String polis5 = cellPolis5!=null?cellPolis5.getStringCellValue():"";
            String polis6 = cellPolis6!=null?cellPolis6.getStringCellValue():"";

            String cicilan = null;
            if (cellInstall != null) {
                if (!BDUtil.isZeroOrNull(new BigDecimal(cellInstall.getNumericCellValue()))) {
                    cicilan = ConvertUtil.removeTrailing(String.valueOf(cellInstall.getNumericCellValue()));
                }
            }

            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, getReceipt().getStARSettlementID());

            DTOList invoiceList = getARInvoiceByPolId(cellNomorPolis.getStringCellValue(), stl.getStParameter1(), cicilan);

            String jenisKomisi = cellJenis.getStringCellValue();
            String metode = cellMetode.getStringCellValue();

            for (int i = 0; i < invoiceList.size(); i++) {
                ARInvoiceView inv = (ARInvoiceView) invoiceList.get(i);

                if (inv == null) {
                    alasan = alasan + "<br>Tagihan Polis " + cellNomorPolis.getStringCellValue() + " tidak ditemukan.";
                    cekGagal = true;
                    continue;
                }

                //cek pembayaran polis induk
                validatePembayaranPolisInduk(inv);

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
                    if (inv.getStUsedFlag().equalsIgnoreCase("Y") && inv.getDtReceipt() == null) {
                        bisaBayar = false;
                        cekGagal = true;
                        alasan = alasan + "<br>Polis " + cellNomorPolis.getStringCellValue() + " Sudah di entry di no bukti lain.";
                    }

                }

                if (bisaBayar) {
//                    onNewInvoiceByInvoiceIDWithoutRecalculate(inv.getStARInvoiceID(), inv);
                    onNewInvoiceByInvoiceIDSentralisasi(inv.getStARInvoiceID(), inv, jenisKomisi, metode,
                            polis2,polis3,polis4,polis5,polis6);
                    //xxx
                }
            }

            //ARInvoiceView inv = getARInvoiceUsingPolNo(cellNomorPolis.getStringCellValue(), stl.getStParameter1());

        }

        getReceipt().recalculate();

        if (cekGagal) {
            throw new RuntimeException("Polis gagal konversi : " + alasan);
        }

    }

    public void uploadExcelPremiKoas() throws Exception {

        String fileID = getReceipt().getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheetPolis = wb.getSheet("POLIS");

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
            HSSFCell cellReceiptDate = row.getCell(4);//nomor polis
            HSSFCell cellEntityID = row.getCell(5);//nomor polis

            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, getReceipt().getStARSettlementID());

            DTOList invoiceList = getARInvoiceByPolId(cellNomorPolis.getStringCellValue(), stl.getStParameter1(), null);

            if (cellEntityID != null) {
                String entID = cellEntityID.getCellType() == cellEntityID.CELL_TYPE_STRING ? cellEntityID.getStringCellValue() : new BigDecimal(cellEntityID.getNumericCellValue()).toString();
                if (!entID.equalsIgnoreCase("0")) {
                    invoiceList = null;

                    invoiceList = getARInvoiceByPolIdAndEntityID(cellNomorPolis.getStringCellValue(), stl.getStParameter1(), entID, null);
                }

            }

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
                    if (inv.getStUsedFlag().equalsIgnoreCase("Y") && inv.getDtReceipt() == null) {
                        bisaBayar = false;
                        cekGagal = true;
                        alasan = alasan + "<br>Polis " + cellNomorPolis.getStringCellValue() + " Sudah di entry di no bukti lain.";
                    }

                }

                if (bisaBayar) {
                    onNewInvoiceByInvoiceIDWithoutRecalculate(inv.getStARInvoiceID(), inv);
                }
            }

            //ARInvoiceView inv = getARInvoiceUsingPolNo(cellNomorPolis.getStringCellValue(), stl.getStParameter1());

        }

        getReceipt().recalculate();

        if (cekGagal) {
            throw new RuntimeException("Polis gagal konversi : " + alasan);
        }

    }

    public DTOList getARInvoicePPNFeeBase(String attrpolno) throws Exception {

       String sql = "select a.* from ar_invoice a inner join ar_invoice_details b on a.ar_invoice_id = b.ar_invoice_id where a.attr_pol_no = ?";

       sql = sql + " and a.ar_trx_type_id = 11 and a.amount_settled is null and coalesce(a.cancel_flag,'') <> 'Y'";

       sql = sql + " and b.ar_trx_line_id in (129,130,131)";

        return ListUtil.getDTOListFromQuery(
                    sql ,
                    new Object [] {attrpolno},
                    ARInvoiceView.class
                    );
    }

    public void onNewInvoicePembayaranKomisiByArInvoiceID(String arInvoiceID) throws Exception {

      ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(arInvoiceID);

      validateInvoiceAlreadyIn(invoice.getStARInvoiceID());

      ARReceiptLinesView rcl = new ARReceiptLinesView();
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

     public void onNewInvoiceByInvoiceIDMonitoring(String invoice_id, ARInvoiceView invoiceParam) throws Exception {

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

      onExpandInvoiceItemMonitoring();
   }

   public void onExpandInvoiceItemMonitoring() throws Exception {
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

   public void onNewNomorRekap() throws Exception {
      ARInvoiceView invoice = new ARInvoiceView();
      invoice.setStNoSuratHutang(nosurathutang);
      invoice.setDtSuratHutangPeriodFrom(periodstart);
      invoice.setDtSuratHutangPeriodTo(periodend);
      invoice.setStEntityID(receipt.getStEntityID());
      DTOList listInv = invoice.getListNomorRekap();

      invoice.setDtSuratHutangPeriodFrom(null);
      invoice.setDtSuratHutangPeriodTo(null);
      periodstart = null;
      periodend = null;
 
      descLong = descLong + "\n" + nosurathutang;
	  for (int i = 0; i < listInv.size(); i++) {
                //ARInvoiceView invoiceView = (ARInvoiceView) listInv.get(i);
                HashDTO dto = (HashDTO) listInv.get(i);

                boolean samedata = false;

                if(i>0){
                    HashDTO dto2 = (HashDTO) listInv.get(i-1);
                    String invoice_id = dto2.getFieldValueByFieldNameBD("ar_invoice_id").toString();

                    if(dto.getFieldValueByFieldNameBD("ar_invoice_id").toString().equalsIgnoreCase(invoice_id)){
                        samedata = true;
                    }
                        
                }

                onNewInvoiceByNomorRekap(dto,samedata);
	  }

      receipt.setStDescription("Pembayaran " + descLong);
      receipt.setStShortDescription(receipt.getStDescription());
   }

   public void onNewInvoiceByNomorRekap(HashDTO dto, boolean samedata) throws Exception {


      if(!samedata){

          validateInvoiceAlreadyIn(dto.getFieldValueByFieldNameBD("ar_invoice_id").toString());

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

      onExpandInvoiceItemNomorRekap(dto);
   }

   public void onExpandInvoiceItemNomorRekap(HashDTO dto) throws Exception {
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

         if (!rcl.getStDescription().equalsIgnoreCase("Premi Bruto")){
                rcl.setStCheck("N");
         }
         
         ARInvoiceDetailView d = (ARInvoiceDetailView) rcl.getInvoiceDetail();

         if (d.isComission()) rcl.markAsComission();

         //if(lockCheck(rcl))
             //rcl.setStLock(false);

         rl.getDetails().add(rcl);
   }


   public void rekonExcelSentralisasi() throws Exception {

        String fileID = getReceipt().getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);

        HSSFSheet sheetReferensi = wb.getSheet("REFERENSI_BAYAR");

        int rowsReferensi = sheetReferensi.getPhysicalNumberOfRows();

        String alasan = "";
        boolean cekGagal = false;

        XSSFWorkbook wbC = new XSSFWorkbook();
        CreationHelper createHelper = wbC.getCreationHelper();

        //bikin sheet
        XSSFSheet sheet = wbC.createSheet("CEK DATA");

        //bikin header
        XSSFRow row1 = sheet.createRow(0);
        row1.createCell(0).setCellValue("NO");
        row1.createCell(1).setCellValue("NOMOR REFERENSI SOURCE");
        row1.createCell(2).setCellValue("NOMOR REFERENSI");
        row1.createCell(3).setCellValue("TANGGAL BAYAR");
        row1.createCell(4).setCellValue("NOMOR POLIS");
        row1.createCell(5).setCellValue("TSI");
        row1.createCell(6).setCellValue("PREMI");
        row1.createCell(7).setCellValue("JUMLAH BAYAR");
        row1.createCell(8).setCellValue("STATUS");

        //cek sheet by referensi bayar --STEP 2
        int ctr = 0;
        for (int s = 5; s <= rowsReferensi; s++) {
            HSSFRow row = sheetReferensi.getRow(s);

            HSSFCell cellControl = row.getCell(1);
            //if(cellControl==null) break;

            if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                break;
            }

            ctr= ctr+1;

            HSSFCell cellNomorReferensi = row.getCell(3);//nomor polis
            HSSFCell cellReceiptDate = row.getCell(4);//nomor polis
            HSSFCell cellJenis = row.getCell(5);//jenis komisi
            HSSFCell cellJumlahBayar = row.getCell(6);//jumlah bayar

            String nomorPolis = "--";
            String status = "OK";
            BigDecimal tsi = BDUtil.zero;
            BigDecimal premi = BDUtil.zero;

            String deskripsiReferensi = cellNomorReferensi.getStringCellValue();
            String nomorReferensi = "";

            if(deskripsiReferensi.trim().toUpperCase().contains("FFFFFF")){
                String [] tempReferensi = deskripsiReferensi.split("FFFFFF");

                //nomorReferensi =  tempReferensi[1].substring(0, 11) +" - "+ tempReferensi[1].substring(11,16);

                nomorReferensi =  tempReferensi[1].substring(0, 11) +" - "+ (tempReferensi[1].length()>17?tempReferensi[1].substring(11,16):tempReferensi[1].substring(11));
            }
            

            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, getReceipt().getStARSettlementID());

            DTOList invoiceList = new DTOList();

            if(!nomorReferensi.equalsIgnoreCase("")){
                invoiceList = getARInvoiceFlexible("reference_no", stl.getStParameter1(), nomorReferensi);
            }

            if(!deskripsiReferensi.trim().toUpperCase().contains("FFFFFF")){
                status = "Format Nomor Referensi tidak sesuai";
                tsi = BDUtil.zero;
                premi = BDUtil.zero;

            }

            if(invoiceList.size()==0){
                status = "Polis tidak ditemukan, cek nomor referensi";
            }
            
            for (int i = 0; i < invoiceList.size(); i++) {
                ARInvoiceView inv = (ARInvoiceView) invoiceList.get(i);

                if (inv == null) {
                    alasan = alasan + "<br>Tagihan No Referensi " + cellNomorReferensi.getStringCellValue() + " tidak ditemukan.";
                    cekGagal = true;
                    //continue;
                    status = "Polis tidak ditemukan, cek nomor referensi";
                }

                inv.setDtTransDate(cellReceiptDate.getDateCellValue());

                Date tglPembukuan = receipt.getDtReceiptDate();
                DateTime tglApproved = new DateTime();
                DateTime tglBayar = new DateTime();
                DateTime tglAkhir = new DateTime();

                InsurancePolicyView polis = inv.getPolicy();

                nomorPolis = polis.getStPolicyNo();
                tsi = polis.getDbInsuredAmount();
                premi = polis.getDbPremiTotal();

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
                    alasan = alasan + "<br>Tanggal Persetujuan Polis " + polis.getStPolicyNo() + " > Tanggal Pembukuan.";
                    status = "Tanggal Persetujuan Polis " + polis.getStPolicyNo() + " > Tanggal Pembukuan.";
                }

                if (inv.getDtReceipt() != null) {
                    bisaBayar = false;
                    cekGagal = true;
                    alasan = alasan + "<br>Polis " + polis.getStPolicyNo() + " Sudah dibayar.";
                    status = "Polis " + polis.getStPolicyNo() + " Sudah dibayar.";
                }

                if (inv.getStUsedFlag() != null) {
                    if (inv.getStUsedFlag().equalsIgnoreCase("Y") && inv.getDtReceipt() == null) {
                        bisaBayar = false;
                        cekGagal = true;
                        alasan = alasan + "<br>Polis " + polis.getStPolicyNo() + " Sudah di entry di no bukti lain.";
                        status = "Polis " + polis.getStPolicyNo() + " Sudah di entry di no bukti lain.";
                    }
                }

                if(!BDUtil.isEqual(premi, new BigDecimal(cellJumlahBayar.getNumericCellValue()), 0))
                    status = "Selisih Premi";

                

            }

                //bikin isi cell
                XSSFRow row2 = sheet.createRow(ctr);
                row2.createCell(0).setCellValue(ctr);
                row2.createCell(1).setCellValue(cellNomorReferensi.getStringCellValue());
                row2.createCell(2).setCellValue(nomorReferensi);
                row2.createCell(3).setCellValue(cellReceiptDate.getDateCellValue());
                row2.createCell(4).setCellValue(nomorPolis);
                row2.createCell(5).setCellValue(tsi.doubleValue());
                row2.createCell(6).setCellValue(premi.doubleValue());
                row2.createCell(7).setCellValue(cellJumlahBayar.getNumericCellValue());
                row2.createCell(8).setCellValue(status);

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=rekonsiliasi_"+ file.getStOriginalName()+".xlsx;");
        SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wbC.write(sosStream);
        sosStream.flush();
        sosStream.close();


    }

   /**
     * @return the stRekeningNo
     */
    public String getStRekeningNo() {
        return stRekeningNo;
    }

    /**
     * @param stRekeningNo the stRekeningNo to set
     */
    public void setStRekeningNo(String stRekeningNo) {
        this.stRekeningNo = stRekeningNo;
    }

    public void doApproveReinsuranceInward() throws Exception {
        onChgPeriod();
        //receipt.generateReceiptNo();
        //final String trxNO = receipt.getStReceiptNo();
        receipt.recalculate();
        receipt.setStIDRFlag("Y");
        getRemoteAccountReceivable().savePembayaranInward(receipt);
        super.close();
    }

    public void createNewRCP(String stSettlementID) throws Exception {

        receipt = new ARReceiptView();
        receipt.markNew();

        receipt.setNotes(new DTOList());
        receipt.setDetails(new DTOList());

        receipt.setStARSettlementID(stSettlementID);

        //receipt.setDtReceiptDate(new Date());

        receipt.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());

        //setTitle("Cetak Monitoring Pembayaran");

        editMode = true;
    }

    public void clickTagihan() throws Exception {
        if (stBranch == null) {
            throw new RuntimeException("Cabang belum diinput");
        }
        if (stEntityID == null) {
            throw new RuntimeException("Nama Bank belum diinput");
        }
        if (stName == null) {
            throw new RuntimeException("Nomor belum diinput");
        }

        final DTOList l = receipt.getDetails();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        //getRemoteInsurance().updateClaimRecap(l, this);

        super.redirect("/pages/ar/report/tagihan.fop");
    }

    public GLCostCenterView getCostCenter() {
        final GLCostCenterView cccode = (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, stBranch);

        return cccode;
    }

    public EntityView getEntity() {
        final EntityView entity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntityID);

        return entity;
    }

    public void onNewNomorTransaksi() throws Exception {
        ARInvoiceView invoice = new ARInvoiceView();
        invoice.setStNoSuratHutang(nosurathutang);
        invoice.setDtSuratHutangPeriodFrom(shpstart);
        invoice.setDtSuratHutangPeriodTo(shpend);
        invoice.setDtAttrPolicyPeriodStart(periodstart);
        invoice.setDtAttrPolicyPeriodEnd(periodend);
        invoice.setStAttrPolicyTypeID(receipt.getStPolicyTypeID());
        invoice.setStEntityID(receipt.getStEntityID());

        DTOList listInv = invoice.getList6(getCcy());

        descLong = descLong + "\n" + nosurathutang;
        for (int i = 0; i < listInv.size(); i++) {
            //ARInvoiceView invoiceView = (ARInvoiceView) listInv.get(i);
            HashDTO dto = (HashDTO) listInv.get(i);

            boolean samedata = false;

            if (i > 0) {
                HashDTO dto2 = (HashDTO) listInv.get(i - 1);
                String invoice_id = dto2.getFieldValueByFieldNameBD("ar_invoice_id").toString();

                if (dto.getFieldValueByFieldNameBD("ar_invoice_id").toString().equalsIgnoreCase(invoice_id)) {
                    samedata = true;
                }
            }

            //parinvoiceid = invoiceView.getStARInvoiceID();

            onNewInvoiceByNoSuratHutang(dto, samedata);
        }

        receipt.setStDescription("Pembayaran " + descLong);
        receipt.setStShortDescription(receipt.getStDescription());
    }

    public void onNewInvoicePembayaranKomisiByArInvoiceIDWithoutOrder(String arInvoiceID) throws Exception {

      ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(arInvoiceID);

      validateInvoiceAlreadyIn(invoice.getStARInvoiceID());

      ARReceiptLinesView rcl = new ARReceiptLinesView();
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

      onExpandInvoiceItemPembayaranKomisiWithoutOrder();


   }

    public void onExpandInvoiceItemPembayaranKomisiWithoutOrder() throws Exception {
      final ARReceiptLinesView rl = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

      final ARInvoiceView invoice = rl.getInvoice();

      rl.setStExpandedFlag("Y");

      final DTOList details = invoice.getDetailsWithoutOrder();

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

         if(!isTax){
             rcl.setStCheck("Y");
         }

         rcl.setStLock(true);

         if(isTax){
            //rcl.setStLock(false);
         }

         /*
         if(d.isTaxComm()||d.isTaxBrok()||d.isTaxHFee()){
            rcl.setDbAmount(BDUtil.zero);
            rcl.setDbEnteredAmount(BDUtil.zero);
         }*/


         if (d.isComission()) rcl.markAsComission();
         //rcl.markCommit();

         rl.getDetails().add(rcl);

         if(d.isFeeBase3()){
             DTOList invoicePPN = getARInvoicePPNFeeBase(invoice.getStAttrPolicyNo());

             if(invoicePPN.size()>0){
                 ARInvoiceView invPPN = (ARInvoiceView) invoicePPN.get(0);

                 onNewInvoicePembayaranKomisiByArInvoiceID(invPPN.getStARInvoiceID());
             }

         }
      }
      receipt.recalculatePembayaranKomisi();
   }

    public void onNewSuratHutangCommNew() throws Exception {
        ARInvoiceView invoice = new ARInvoiceView();
        invoice.setStNoSuratHutang(nosurathutang);
        invoice.setDtSuratHutangPeriodFrom(periodstart);
        invoice.setDtSuratHutangPeriodTo(periodend);
        DTOList listInv = invoice.getListDetailsSuratHutangCommNew();

        descLong = descLong + "\n" + nosurathutang;
        for (int i = 0; i < listInv.size(); i++) {
            //ARInvoiceView invoiceView = (ARInvoiceView) listInv.get(i);
            HashDTO dto = (HashDTO) listInv.get(i);

            boolean samedata = false;

            if (i > 0) {
                HashDTO dto2 = (HashDTO) listInv.get(i - 1);
                String invoice_id = dto2.getFieldValueByFieldNameBD("ar_invoice_id").toString();

                if (dto.getFieldValueByFieldNameBD("ar_invoice_id").toString().equalsIgnoreCase(invoice_id)) {
                    samedata = true;
                }
            }

            //parinvoiceid = invoiceView.getStARInvoiceID();

            onNewInvoiceByNoSuratHutang(dto, samedata);
        }

        receipt.setStDescription("Pembayaran " + descLong);
        receipt.setStShortDescription(receipt.getStDescription());
    }

    public void uploadExcelMonitoring() throws Exception {

        String fileID = getReceipt().getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheetPolis = wb.getSheet("POLIS");

        final DTOList details = new DTOList();

        getReceipt().setDetails(details);

        int rows = sheetPolis.getPhysicalNumberOfRows();

        for (int r = 5; r <= rows; r++) {
            HSSFRow row = sheetPolis.getRow(r);

            HSSFCell cellControl = row.getCell(1);
            if (cellControl == null) {
                break;
            }

            if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellNomorPolis = row.getCell(3);//nomor polis

            //ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, getReceipt().getStARSettlementID());

            DTOList invoiceList = getARInvoiceByPolId2(cellNomorPolis.getStringCellValue());

            //ARInvoiceView inv = getARInvoiceUsingPolNo(cellNomorPolis.getStringCellValue(), stl.getStParameter1());

            for (int i = 0; i < invoiceList.size(); i++) {
                ARInvoiceView inv = (ARInvoiceView) invoiceList.get(i);

                onNewInvoiceByInvoiceIDWithoutRecalculate(inv.getStARInvoiceID(), inv);
            }
        }

        getReceipt().recalculate();

    }

    public DTOList getARInvoiceByPolId3(String attrpolno) throws Exception {

        String sql = "select a.*,b.ar_invoice_dtl_id,b.description as ket,a.amount as utang from ar_invoice a "
                + " inner join ar_invoice_details b on a.ar_invoice_id = b.ar_invoice_id"
                + " where a.attr_pol_no in (" + attrpolno + ") "
                + " and b.ar_trx_line_id in (7,23,39) and coalesce(a.cancel_flag,'') <> 'Y' and coalesce(a.posted_flag,'Y') = 'Y'";

        return ListUtil.getDTOListFromQuery(
                sql,
                HashDTO.class);
    }

    public void onNewInvoiceByMonitoring(HashDTO dto, boolean samedata) throws Exception {

        if (!samedata) {
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

            invoicesindex = String.valueOf(receipt.getDetails().size() - 1);
        }

        onExpandInvoiceItemNoSuratHutang(dto);
    }



    public void onNewSuratHutangIzinCairRefund() throws Exception {
        ARInvoiceView invoice = new ARInvoiceView();
        invoice.setStNoIzinCair(noizincair);
//        invoice.setDtSuratHutangPeriodFrom(periodstart);
//        invoice.setDtSuratHutangPeriodTo(periodend);
        DTOList listInv = invoice.getListDetailsSHKRefundIzinCair();

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

    public void onNewSuratHutangIzinCairComm() throws Exception {
        ARInvoiceView invoice = new ARInvoiceView();
        invoice.setStNoIzinCair(noizincair);
//        invoice.setDtSuratHutangPeriodFrom(periodstart);
//        invoice.setDtSuratHutangPeriodTo(periodend);
        DTOList listInv = invoice.getListDetailsSHKKomisiIzinCair();

        descLong = descLong + "\n" + noizincair;
        for (int i = 0; i < listInv.size(); i++) {
            //ARInvoiceView invoiceView = (ARInvoiceView) listInv.get(i);
            HashDTO dto = (HashDTO) listInv.get(i);

            boolean samedata = false;

            if (i > 0) {
                HashDTO dto2 = (HashDTO) listInv.get(i - 1);
                String invoice_id = dto2.getFieldValueByFieldNameBD("ar_invoice_id").toString();

                if (dto.getFieldValueByFieldNameBD("ar_invoice_id").toString().equalsIgnoreCase(invoice_id)) {
                    samedata = true;
                }
            }

            //parinvoiceid = invoiceView.getStARInvoiceID();

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

    public void validatePembayaranPolisInduk(ARInvoiceView invoice)throws Exception{

        //Jika menu pembayaran premi & realisasi titipan premi
        if(receipt.getStARSettlementID().equalsIgnoreCase("1") || receipt.getStARSettlementID().equalsIgnoreCase("25")){

            InsurancePolicyView polisInvoice = invoice.getPolicy();

            logger.logDebug("########### validasi pembayaran polis induk "+ polisInvoice.getStPolicyNo());

            String noPolisParam = polisInvoice.getStPolicyNo().substring(0, 16);

            if(polisInvoice.getStPolicyNo().length()>18){
                noPolisParam = polisInvoice.getStPolicyNo().substring(0, 17);
            }

            if(polisInvoice.isStatusEndorse()){

                //cari polis induk dari invoice tsb
                DTOList listPolis = null;
                listPolis = ListUtil.getDTOListFromQuery(
                " select * "+
                " from ins_policy "+
                " where status in ('POLICY','RENEWAL') "+
                " and active_flag = 'Y' and effective_flag = 'Y' and pol_no = ? ",
                new Object[]{noPolisParam + "00"},
                InsurancePolicyView.class);

                logger.logDebug("########### cari polis induk nya "+ noPolisParam + "00");

                InsurancePolicyView polisInduk = (InsurancePolicyView)  listPolis.get(0);

                if(!polisInduk.isPremiPaidInSystem2()){
                    throw new RuntimeException("Polis "+ polisInvoice.getStPolicyNo() +", Polis Induk nya ("+ polisInduk.getStPolicyNo() +") belum dilakukan pembayaran premi. <br> Silakan lakukan pembayaran polis induk terlebih dulu.");
                }
            }

            //JIKA POLIS SERBAGUNA, CEK POLIS SEBELUM-SEBELUMNYA
            if(polisInvoice.isStatusEndorse()){

                if(polisInvoice.getStPolicyTypeID().equalsIgnoreCase("87") || polisInvoice.getStPolicyTypeID().equalsIgnoreCase("88")){

                    noPolisParam = polisInvoice.getStPolicyNo().substring(0, 16);

                    //cari polis induk dari invoice tsb
                    DTOList listPolis = null;
                    listPolis = ListUtil.getDTOListFromQuery(
                    " select * "+
                    " from ins_policy "+
                    " where status in ('POLICY','RENEWAL') "+
                    " and active_flag = 'Y' and effective_flag = 'Y' and substr(pol_no,0,17) = ? and pol_id < ?",
                    new Object[]{noPolisParam, polisInvoice.getStPolicyID()},
                    InsurancePolicyView.class);

                    logger.logDebug("########### cari polis induk nya "+ noPolisParam);

                    for (int i = 0; i < listPolis.size(); i++) {
                        InsurancePolicyView polisInduk = (InsurancePolicyView)  listPolis.get(0);

                        if(!polisInduk.isPremiPaidInSystem2()){
                            throw new RuntimeException("Polis "+ polisInvoice.getStPolicyNo() +", Polis Induk nya ("+ polisInduk.getStPolicyNo() +") belum dilakukan pembayaran premi. <br> Silakan lakukan pembayaran polis induk terlebih dulu.");
                        }

                    }
                    
                }


            }
        }
    }

    public void onNewInvoiceCoins() throws Exception {

        validateInvoiceAlreadyIn(parinvoiceid);
        validateInvoiceAlreadyIn2(parinvoiceid);

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

        //rcl.setStNegativeFlag(d.getStNegativeFlag());
        rcl.markAsInvoice();
        //rcl.markCommit();

        receipt.setStEntityID(invoice.getStEntityID());

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

        onExpandInvoiceItem();
    }

    public void uploadExcelPajak() throws Exception {

        String fileID = getReceipt().getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheetPolis = wb.getSheet("POLIS");

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
            HSSFCell cellReceiptDate = row.getCell(4);//nomor polis
            HSSFCell cellJenis = row.getCell(5);//nomor polis

            if (cellJenis == null) {
                throw new RuntimeException("Download ulang format konversi terbaru");
            }

            String jenisPajak = cellJenis.getStringCellValue();
            String arTrxLineID = null;

            if (jenisPajak.equalsIgnoreCase("PPH21")) {
                arTrxLineID = " b.ar_trx_line_id in (14,17,20,30,33,36,46,49,52)";
            }

            if (jenisPajak.equalsIgnoreCase("PPH23")) {
                arTrxLineID = " b.ar_trx_line_id in (15,18,19,21,31,34,35,37,47,50,51,53,96,120)";
            }

            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, getReceipt().getStARSettlementID());

            DTOList invoiceList = getARInvoiceDetailsByPolId(cellNomorPolis.getStringCellValue(), stl.getStParameter1(), arTrxLineID);

            for (int i = 0; i < invoiceList.size(); i++) {
                ARInvoiceView inv = (ARInvoiceView) invoiceList.get(i);

                if (inv == null) {
                    alasan = alasan + "<br>Hutang Pajak Polis " + cellNomorPolis.getStringCellValue() + " tidak ditemukan.";
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
                    if (inv.getStUsedFlag().equalsIgnoreCase("Y") && inv.getDtReceipt() == null) {
                        bisaBayar = false;
                        cekGagal = true;
                        alasan = alasan + "<br>Polis " + cellNomorPolis.getStringCellValue() + " Sudah di entry di no bukti lain.";
                    }

                }

                if (bisaBayar) {
                    onNewInvoiceByInvoiceIDWithoutRecalculate(inv.getStARInvoiceID(), inv);
                }
            }

            //ARInvoiceView inv = getARInvoiceUsingPolNo(cellNomorPolis.getStringCellValue(), stl.getStParameter1());

        }

        recalculatePembayaranPajak();

        if (cekGagal) {
            throw new RuntimeException("Polis gagal konversi : " + alasan);
        }

    }

    public void uploadExcelKlaimOutward() throws Exception {

        String fileID = getReceipt().getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheetPolis = wb.getSheet("POLIS");

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
            HSSFCell cellReceiptDate = row.getCell(4);//nomor polis
            HSSFCell cellEntityID = row.getCell(5);//nomor polis
            HSSFCell cellNoLKP = row.getCell(6);//nomor lkp

            String entityID = "";

            if(cellEntityID!=null){
                entityID = cellEntityID.getCellType() == cellEntityID.CELL_TYPE_STRING ? cellEntityID.getStringCellValue() : new BigDecimal(cellEntityID.getNumericCellValue()).toString();
            }

            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, getReceipt().getStARSettlementID());

            DTOList invoiceList = getARInvoiceDetailsByEntityIDAndLKP(cellNomorPolis.getStringCellValue(), stl.getStParameter1(), entityID, cellNoLKP.getStringCellValue());

            for (int i = 0; i < invoiceList.size(); i++) {
                ARInvoiceView inv = (ARInvoiceView) invoiceList.get(i);

                if (inv == null) {
                    alasan = alasan + "<br>Piutang Polis " + cellNomorPolis.getStringCellValue() + " tidak ditemukan.";
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
                    if (inv.getStUsedFlag().equalsIgnoreCase("Y") && inv.getDtReceipt() == null) {
                        bisaBayar = false;
                        cekGagal = true;
                        alasan = alasan + "<br>Polis " + cellNomorPolis.getStringCellValue() + " Sudah di entry di no bukti lain.";
                    }

                }

                if (bisaBayar) {
                    onNewInvoiceByInvoiceIDWithoutRecalculate(inv.getStARInvoiceID(), inv);
                }
            }

            //ARInvoiceView inv = getARInvoiceUsingPolNo(cellNomorPolis.getStringCellValue(), stl.getStParameter1());

        }

        RecalculatePajak();

        if (cekGagal) {
            throw new RuntimeException("Polis gagal konversi : " + alasan);
        }

    }

    public DTOList getARInvoiceDetailsByEntityID(String attrpolid, String param, String entityID) throws Exception {

        String sql = "select a.* from ar_invoice a inner join ar_invoice_details b on a.ar_invoice_id = b.ar_invoice_id where a.attr_pol_no = ?";

        sql = sql + " and " + param;

        sql = sql + " and a.amount_settled is null and coalesce(a.cancel_flag,'') <> 'Y'";

        if (!entityID.equalsIgnoreCase("")) {
            sql = sql + " and a.ent_id = " + entityID;
        }

        //sql = sql + " limit 1";

        return ListUtil.getDTOListFromQuery(
                sql,
                new Object[]{attrpolid},
                ARInvoiceView.class);
    }

    public void uploadExcelPremiReasOutward() throws Exception {

        String fileID = getReceipt().getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheetPolis = wb.getSheet("POLIS");

        int rows = sheetPolis.getPhysicalNumberOfRows();

        String alasan = "";
        boolean cekGagal = false;

        for (int r = 5; r <= rows; r++) {
            HSSFRow row = sheetPolis.getRow(r);

            HSSFCell cellControl = row.getCell(1);

            if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellNoSuratHutang = row.getCell(3);//
            HSSFCell cellNomorPolis = row.getCell(4);//nomor polis
            HSSFCell cellEntID = row.getCell(5);//nomor polis
            HSSFCell cellReceiptDate = row.getCell(6);//nomor polis
            HSSFCell cellInstall = row.getCell(7);//installment
            HSSFCell cellRiSlip = row.getCell(8);// r/i slip

            String cicilan = null;

            if (cellInstall != null) {
                if (!BDUtil.isZeroOrNull(new BigDecimal(cellInstall.getNumericCellValue()))) {
                    cicilan = ConvertUtil.removeTrailing(String.valueOf(cellInstall.getNumericCellValue()));
                }
            }

            String entID = null;

            if (cellEntID != null) {
                if (!BDUtil.isZeroOrNull(new BigDecimal(cellEntID.getNumericCellValue()))) {
                    entID = ConvertUtil.removeTrailing(String.valueOf(cellEntID.getNumericCellValue()));
                }
            }
            
            String riSlip = null;

            if (cellRiSlip != null) {
                    riSlip = cellRiSlip.getStringCellValue();
            }



            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, getReceipt().getStARSettlementID());

            DTOList invoiceList = getARInvoiceByPolNo(cellNoSuratHutang.getStringCellValue(), cellNomorPolis.getStringCellValue(), stl.getStParameter1(), cicilan, entID, riSlip);

            for (int i = 0; i < invoiceList.size(); i++) {
                ARInvoiceView inv = (ARInvoiceView) invoiceList.get(i);

                if (inv == null) {
                    alasan = alasan + "<br>Tagihan Polis " + cellNomorPolis.getStringCellValue() + " tidak ditemukan.";
                    cekGagal = true;
                    continue;
                }

                //cek pembayaran polis induk
                //validatePembayaranPolisInduk(inv);

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
                    if (inv.getStUsedFlag().equalsIgnoreCase("Y") && inv.getDtReceipt() == null) {
                        bisaBayar = false;
                        cekGagal = true;
                        alasan = alasan + "<br>Polis " + cellNomorPolis.getStringCellValue() + " Sudah di entry di no bukti lain.";
                    }

                }

                if (bisaBayar) {
//                    onNewInvoiceByInvoiceIDWithoutRecalculate(inv.getStARInvoiceID(), inv);
                    onNewInvoiceByInvoiceID(inv.getStARInvoiceID());
                }
            }

            //ARInvoiceView inv = getARInvoiceUsingPolNo(cellNomorPolis.getStringCellValue(), stl.getStParameter1());

        }

        getReceipt().recalculate();

        if (cekGagal) {
            throw new RuntimeException("Polis gagal konversi : " + alasan);
        }

    }

    public DTOList getARInvoiceByPolNo(String noSuratHutang, String polNo, String param, String install, String entID) throws Exception {

        String sql = "select * from ar_invoice a where ";

        sql = sql + " attr_pol_no = ? ";

        sql = sql + " and " + param;

        if (install != null) {
            sql = sql + " and a.invoice_no like '%-" + install + "'";
        }

        if (noSuratHutang != null) {
            sql = sql + " and a.no_surat_hutang = '" + noSuratHutang.trim() + "'";
        }

        if (entID != null) {
            sql = sql + " and a.ent_id = '" + entID + "'";
        }

        sql = sql + " and amount_settled is null and coalesce(a.cancel_flag,'') <> 'Y' ";

        if (receipt.getStARSettlementID().equalsIgnoreCase("13") || receipt.getStARSettlementID().equalsIgnoreCase("14")) {
            sql = sql + " and a.ent_id not in (select refid1 from ent_master_coas)";
        }

        return ListUtil.getDTOListFromQuery(
                sql,
                new Object[]{ polNo},
                ARInvoiceView.class);
    }

    public DTOList getARInvoiceDetailsByEntityIDAndLKP(String attrpolid, String param, String entityID, String noLKP) throws Exception {

        String sql = "select a.* from ar_invoice a inner join ar_invoice_details b on a.ar_invoice_id = b.ar_invoice_id where a.attr_pol_no = ?";

        sql = sql + " and " + param;

        sql = sql + " and a.amount_settled is null and coalesce(a.cancel_flag,'') <> 'Y'";

        if (!entityID.equalsIgnoreCase("")) {
            sql = sql + " and a.ent_id = " + entityID;
        }

        if (!noLKP.equalsIgnoreCase("")) {
            sql = sql + " and a.refid2 = '" + noLKP.trim()+"' ";
        }

        //sql = sql + " limit 1";

        return ListUtil.getDTOListFromQuery(
                sql,
                new Object[]{attrpolid},
                ARInvoiceView.class);
    }

    public DTOList getARInvoiceByPolNo(String noSuratHutang, String polNo, String param, String install, String entID, String riSlip) throws Exception {

        String sql = "select * from ar_invoice a where ";

        sql = sql + " attr_pol_no = ? ";

        sql = sql + " and " + param;

        if (install != null) {
            sql = sql + " and a.invoice_no like '%-" + install + "'";
        }

        if (noSuratHutang != null) {
            sql = sql + " and a.no_surat_hutang = '" + noSuratHutang.trim() + "'";
        }

        if (entID != null) {
            sql = sql + " and a.ent_id = '" + entID + "'";
        }

        if (riSlip != null) {
            sql = sql + " and a.refa1 = '" + riSlip + "'";
        }

        sql = sql + " and amount_settled is null and coalesce(a.cancel_flag,'') <> 'Y' ";

        if (receipt.getStARSettlementID().equalsIgnoreCase("13") || receipt.getStARSettlementID().equalsIgnoreCase("14")) {
            sql = sql + " and a.ent_id not in (select refid1 from ent_master_coas)";
        }

        return ListUtil.getDTOListFromQuery(
                sql,
                new Object[]{ polNo},
                ARInvoiceView.class);
    }

}