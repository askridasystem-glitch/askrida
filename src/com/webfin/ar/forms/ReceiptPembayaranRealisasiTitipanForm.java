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
import com.webfin.FinCodec;
import com.webfin.ar.model.*;
import com.crux.util.LogManager;
import com.webfin.ar.ejb.AccountReceivableHome;
import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.gl.ejb.CurrencyManager;
import com.webfin.gl.model.AccountView;
import com.webfin.gl.model.TitipanPremiView;

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
import com.webfin.gl.ejb.PeriodManager;
import com.webfin.gl.model.ClosingDetailView;
import com.webfin.gl.model.TitipanPremiExtracomptableView;
import com.webfin.gl.model.TitipanPremiReinsuranceView;
import com.webfin.gl.util.GLUtil;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import org.apache.poi.hssf.usermodel.*;
import javax.servlet.ServletOutputStream;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class ReceiptPembayaranRealisasiTitipanForm extends Form {

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
   
   private boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("SETTLR_NAVBR");

   private final static transient LogManager logger = LogManager.getInstance(ReceiptPembayaranRealisasiTitipanForm.class);

   final boolean headOfficeUser = SessionManager.getInstance().getSession().isHeadOfficeUser();

   private Date shpstart;
    private Date shpend;
    private String stRekeningNo;
    private String ccy;
    private Date periodstart;
    private Date periodend;

    public Date getPeriodend() {
        return periodend;
    }

    public void setPeriodend(Date periodend) {
        this.periodend = periodend;
    }

    public Date getPeriodstart() {
        return periodstart;
    }

    public void setPeriodstart(Date periodstart) {
        this.periodstart = periodstart;
    }


    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public Date getShpend() {
        return shpend;
    }

    public void setShpend(Date shpend) {
        this.shpend = shpend;
    }

    public Date getShpstart() {
        return shpstart;
    }

    public void setShpstart(Date shpstart) {
        this.shpstart = shpstart;
    }

    public String getStRekeningNo() {
        return stRekeningNo;
    }

    public void setStRekeningNo(String stRekeningNo) {
        this.stRekeningNo = stRekeningNo;
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
        //final DTOList l = receipt.getDetails();
        
        //SessionManager.getInstance().getRequest().setAttribute("RPT",l);
        
        //getRemoteInsurance().updateMonitoring(l, this);
        
        //super.redirect("/pages/ar/report/"+stReportType+".fop");
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

   public ReceiptPembayaranRealisasiTitipanForm() {
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
      ARReceiptLinesView currentLine = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

      getListInvoices().delete(Integer.parseInt(invoicesindex));

      if(receipt.getDetails().size()>0){
          receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " Polis");
          receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " Polis");
      }

      currentLine.getDetails().deleteAll();
      currentLine.getListTitipan().deleteAll();

      recalculate();
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
         if(!isTax) rcl.setStCheck("Y");

         // ksg detail REMARK KSG
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

                 onNewInvoiceByInvoiceID(invPPN.getStARInvoiceID());
             }

         }
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

         rcl.getListTitipan().markAllUpdate();
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
      
       receipt.setStIDRFlag("Y");
       
      if(receipt.getSettlement().checkProperty("CALC_COMM","Y")){
          recalculatePembayaranKomisi();

          if(receipt.getStJournalType()!=null){
                if(receipt.isJournalOffset()) getRemoteAccountReceivable().savePembayaranKomisiSentralisasi(receipt);
                else getRemoteAccountReceivable().savePembayaranKomisi(receipt);
          }else {
              getRemoteAccountReceivable().savePembayaranKomisi(receipt);
          }

      }else{
          validateRealisasi();
          receipt.recalculate();

          if(receipt.getStJournalType()!=null){
              if(receipt.isJournalOffset()) getRemoteAccountReceivable().savePembayaranPremiSentralisasi(receipt);
              else getRemoteAccountReceivable().savePembayaranPremi(receipt);
          }else {
              getRemoteAccountReceivable().savePembayaranPremi(receipt);
          }
          
      }
      
      super.close();
   }
   
   public void doSave3() throws Exception {
      receipt.generateReceiptNo();
      final String trxNO = receipt.getStReceiptNo();
      receipt.recalculate();
      receipt.setStIDRFlag("Y");
      getRemoteAccountReceivable().savePembayaranPajak(receipt);
      
      super.close();
   }
   
   public void doSaveTitipan() throws Exception {
      receipt.generateReceiptNo();
      final String trxNO = receipt.getStReceiptNo();
      receipt.recalculate();
      //getRemoteAccountReceivable().saveReceiptTitipan(receipt,this);
      super.close();
   }

   public void doRecalculate() throws Exception{
        if(receipt.getStARSettlementID()!=null){
              if(receipt.getSettlement().checkProperty("CALC_COMM","Y")) recalculatePembayaranKomisi();
              else if(receipt.getSettlement().checkProperty("CALC_TAX","Y")) recalculatePembayaranPajak();
              //else if(receipt.getSettlement().checkProperty("CALC_TITIP","Y")) receipt.recalculateLKS();
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
              //else if(receipt.getSettlement().checkProperty("CALC_TITIP","Y")) receipt.recalculateLKS();
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

   public void addGLOld() throws Exception {
      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();
      
      ARReceiptLinesView rclTitip = (ARReceiptLinesView) getListInvoices().get(Integer.parseInt(invoicesindex));
      final ARSettlementExcessView arSettlementExcessView = (ARSettlementExcessView) DTOPool.getInstance().getDTO(ARSettlementExcessView.class, stGLSelect);

      rcl.setStInvoiceID(rclTitip.getStInvoiceID());
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
   
   public void addGL() throws Exception {
      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();

      //final ARSettlementExcessView arSettlementExcessView = (ARSettlementExcessView) DTOPool.getInstance().getDTO(ARSettlementExcessView.class, stGLSelect);
      
      ARReceiptLinesView rclTitip = (ARReceiptLinesView) getListInvoices().get(Integer.parseInt(invoicesindex));
      ARInvoiceView invTitip = rclTitip.getInvoice();
      
      rcl.setStInvoiceID(rclTitip.getStInvoiceID());
      rcl.setStInvoiceNo(rclTitip.getStInvoiceNo());

      if(invTitip!=null){
          rcl.setStPolicyID(invTitip.getStPolicyID());
          rcl.setStDescription("Titipan Premi "+ invTitip.getStAttrPolicyNo());
      }
        
      
      
      rcl.setStCurrencyCode(receipt.getStCurrencyCode());
      rcl.setDbCurrencyRate(receipt.getDbCurrencyRate());
      rcl.setDbInvoiceAmount(null);
      rcl.setDbAmount(null);
      //rcl.setStARSettlementExcessID(arSettlementExcessView.getStARSettlementExcessID());

      rcl.setStLineType(FinCodec.ARReceiptLineType.TITIPAN);
      rcl.markCommit();

      //receipt.getGLs().add(rcl);
      rclTitip.getListTitipan().add(rcl);

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
      rcl.setStPolicyID(invoice.getStPolicyID());
       
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
      rcl.setStPolicyID(invoice.getStPolicyID());
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

          if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+nosurathutang);
          if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+nosurathutang);


          receipt.getDetails().add(rcl);

          invoicesindex=String.valueOf(receipt.getDetails().size()-1);
      }
      
      onExpandInvoiceItemNoSuratHutang(dto);
   }
   
   public void onNewSuratHutang() throws Exception {
      ARInvoiceView invoice = new ARInvoiceView();
      invoice.setStNoSuratHutang(nosurathutang);
      final DTOList listInv = invoice.getList2();
      
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
      
      //receipt.recalculate();
   }   

   public void changeCostCenter()  throws Exception{
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

      super.redirect("/pages/ar/report/receipt.fop");
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
   	//  final DTOList l = receipt.getDetails();

      //SessionManager.getInstance().getRequest().setAttribute("RPT",l);
        
      //getRemoteInsurance().updateClaimRecap(l, this);

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
      rcl.setStPolicyID(invoice.getStPolicyID());
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
      receipt.generateReceiptNo();
      final String trxNO = receipt.getStReceiptNo();
      receipt.recalculate();
      getRemoteAccountReceivable().savePembayaranReasuransi(receipt);
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
                " a.ar_invoice_id,a.attr_pol_no,substr(b.pol_no,1,16)||''||get_koasur(substr(b.pol_no,17,2)) as pol_no,a.cc_code,b.dla_no,a.claim_no," +
                " a.claim_name,a.claim_coins_id,a.claim_coins_name,d.ref1 as nama,b.claim_amount_approved,b.parent_id,b.pol_id,b.claim_date, "+
                " (select x.ref3 from ins_policy x where x.pol_id = b.parent_id) as ref3 "
                );
        
        sqa.addQuery(" from ar_invoice a "+
                " inner join ins_policy b on b.pol_id = a.attr_pol_id "+
                " inner join ins_pol_obj d on d.ins_pol_obj_id = b.claim_object_id "
                );
        
        sqa.addClause("a.claim_no = ? ");
        sqa.addPar(stReceiptNo);
        
        final String sql = "select x.pol_id,x.dla_no,x.attr_pol_no,x.nama,claim_date,x.claim_amount_approved,coalesce(c.norek,ref3) as norek,claim_no "+
                " from ( "+sqa.getSQL()+" ) x "+
                " left join aba_koasur c on c.nopol = x.pol_no ";
        
        //sql = sql + " order by claim_no,x.cc_code,x.dla_no order by a.cc_code, b.dla_no ";
        
        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class
                );
        
        SessionManager.getInstance().getRequest().setAttribute("RPT", l);
        
        return l;
        
    }
    
    public void EXPORT_CLAIM()  throws Exception {
        
        HSSFWorkbook wb = new HSSFWorkbook();
        
        //bikin sheet
        HSSFSheet sheet = wb.createSheet("new sheet");
        
        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");
        
        for (int i=0;i< list.size() ; i++){
            HashDTO h = (HashDTO) list.get(i);
            
            //bikin header
            HSSFRow row0 = sheet.createRow((short)0);
            row0.createCell((short)0).setCellValue("Pengajuan Klaim No : " + h.getFieldValueByFieldNameST("claim_no"));
            
            HSSFRow row1 = sheet.createRow((short)2);
            row1.createCell((short)0).setCellValue("ID");
            row1.createCell((short)1).setCellValue("No. LKP");
            row1.createCell((short)2).setCellValue("No. Polis");
            row1.createCell((short)3).setCellValue("Nama");
            row1.createCell((short)4).setCellValue("Tanggal Klaim");
            row1.createCell((short)5).setCellValue("Nilai Klaim");
            row1.createCell((short)6).setCellValue("No. Reg");
            
            //bikin isi cell
            HSSFRow row = sheet.createRow((short)i+3);
            row.createCell((short)0).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
            row.createCell((short)1).setCellValue(h.getFieldValueByFieldNameST("dla_no"));
            row.createCell((short)2).setCellValue(h.getFieldValueByFieldNameST("attr_pol_no"));
            row.createCell((short)3).setCellValue(h.getFieldValueByFieldNameST("nama"));
            row.createCell((short)4).setCellValue(h.getFieldValueByFieldNameDT("claim_date"));
            row.createCell((short)5).setCellValue(h.getFieldValueByFieldNameBD("claim_amount_approved").doubleValue());
            row.createCell((short)6).setCellValue(h.getFieldValueByFieldNameST("norek"));
        }
        
        SessionManager.getInstance().getResponse().setContentType("application/vnd.ms-excel");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition","attachment; filename="+ getStFileName()+".xls;");
        SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();
        
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
              
              if(rl.getInvoice()!=null)
                    if(rl.getInvoice().getStARInvoiceID().equalsIgnoreCase(arInvoiceID))
                        throw new RuntimeException("Tagihan "+ rl.getInvoice().getStAttrPolicyNo() +" Sudah Dipilih sebelumnya");
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
                onNewInvoicePembayaranPajak();
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
                onNewInvoicePembayaranPajak();
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
   
   public void doSavePembayaranKomisi() throws Exception {
      //receipt.generateReceiptNo();
      //final String trxNO = receipt.getStReceiptNo();
      receipt.recalculatePembayaranKomisi();
     receipt.setStIDRFlag("Y");
      getRemoteAccountReceivable().savePembayaranKomisi(receipt);
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
   
   public void doSavePembayaranPajak() throws Exception {
      receipt.generateReceiptNo();
      final String trxNO = receipt.getStReceiptNo();
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
      rcl.setStPolicyID(invoice.getStPolicyID());
       
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
          receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " Polis");
          receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " Polis");
      }
      recalculatePembayaranPajak();
   }
    
    public void onChgPeriod() throws Exception {
        //logger.logDebug("++++++++++++ : "+receipt.getStMonths());
        //logger.logDebug("############ : "+receipt.getStYears());
        //logger.logDebug("%%%%%%%%%%%% : "+DateUtil.getMonth2Digit(receipt.getDtReceiptDate()));
        //logger.logDebug("$$$$$$$$$$$$ : "+DateUtil.getYear(receipt.getDtReceiptDate()));
        
        if (!Tools.isEqual(DateUtil.getMonth2Digit(receipt.getDtReceiptDate()),receipt.getStMonths())) throw new Exception("Bulan tidak sama dengan Tanggal Pembayaran");
        if (!Tools.isEqual(DateUtil.getYear(receipt.getDtReceiptDate()),receipt.getStYears())) throw new Exception("Tahun tidak sama dengan Tanggal Pembayaran");
    }
    
    public void onDeleteInvoiceTitipanItem() throws Exception {
      final ARReceiptLinesView iv = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

      iv.getListTitipan().delete(Integer.parseInt(invoicecomissionindex));

      recalculate();
   }
    
    public void onNewTitipanPremi() throws Exception {

      final TitipanPremiView titipan = getRemoteAccountReceivable().getTitipanPremi(artitipanid);

      validateInvoiceAlreadyIn2(titipan.getStPolicyNo());

      final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoiceUsingPolNo(titipan.getStPolicyNo());

      if(invoice==null)
          throw new RuntimeException("Tagihan premi tidak ditemukan, polis belum disetujui");
      
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
      
      addGLAuto(titipan);
   }
    
    public void addGLAuto(TitipanPremiView titipan) throws Exception {
      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();

      //final ARSettlementExcessView arSettlementExcessView = (ARSettlementExcessView) DTOPool.getInstance().getDTO(ARSettlementExcessView.class, stGLSelect);
      
      ARReceiptLinesView rclTitip = (ARReceiptLinesView) getListInvoices().get(Integer.parseInt(invoicesindex));
      ARInvoiceView invTitip = rclTitip.getInvoice();
      
      rcl.setStInvoiceID(rclTitip.getStInvoiceID());
      rcl.setStInvoiceNo(rclTitip.getStInvoiceNo());
      rcl.setStPolicyID(invTitip.getStPolicyID());
      rcl.setStDescription("Titipan Premi "+ invTitip.getStAttrPolicyNo());
      rcl.setStCurrencyCode(invTitip.getStCurrencyCode());
      rcl.setDbCurrencyRate(receipt.getDbCurrencyRate());
      rcl.setDbInvoiceAmount(null);
      rcl.setDbAmount(null);
      //rcl.setStARSettlementExcessID(arSettlementExcessView.getStARSettlementExcessID());
      rcl.setStTitipanPremiID(titipan.getStTransactionID());
      rcl.setDbTitipanPremiAmount(titipan.getDbAmount());
      rcl.setStDescription(titipan.getStDescription());

      rcl.setStLineType(FinCodec.ARReceiptLineType.TITIPAN);
      rcl.markCommit();

      //receipt.getGLs().add(rcl);
      rclTitip.getListTitipan().add(rcl);

      receipt.recalculate();
   }
    
     public void onNewLawanTitipan() throws Exception {
       
      //validateInvoiceAlreadyIn(parinvoiceid);
       
      //final ARInvoiceView invoice = getRemoteAccountReceivable().get(parinvoiceid);
      //final InsurancePolicyView policy = getRemoteInsurance().getInsurancePolicy(parinvoiceid);

      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();
      
      //String invoice_no = policy.isStatusClaimDLA()?policy.getStDLANo():policy.getStPLANo();
      //BigDecimal amt = policy.getDbClaimCustAmount();//policy.isStatusClaimDLA()?policy.getDbClaimAmountApproved():policy.getDbClaimAmountEstimate();
      //Date dt = policy.isStatusClaimDLA()?policy.getDtDLADate():policy.getDtPLADate();
      
      //rcl.setStInvoiceID(invoice.getStARInvoiceID());
      //rcl.setStInvoiceNo(policy.getStPolicyNo());
      //rcl.setStArInvoiceClaim(invoice_no);
      rcl.setStCurrencyCode(receipt.getStCurrencyCode());
      rcl.setDbCurrencyRate(receipt.getDbCurrencyRate());
      //rcl.setDbInvoiceAmount(amt);
      //rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
      rcl.setDtReceiptDate(receipt.getDtReceiptDate());
      //rcl.setStPolicyID(policy.getStPolicyID());
      //rcl.setStAdvancePaymentFlag("Y");

      rcl.markAsInvoice();

      //receipt.setStEntityID(policy.getStEntityID());
     
      receipt.getDetails().add(rcl);

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
         rcl.setStDescription("REALISASI TITIPAN");
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
    
    public void selectLawanTitipan()throws Exception{
        final ARReceiptLinesView currentLine = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(artitipanid));
             
        final AccountView acc = (AccountView) ListUtil.getDTOListFromQuery("select * from gl_accounts where account_id=?",
                new Object [] {currentLine.getStAccountID()},AccountView.class).getDTO();
        
        final DTOList detRCL = currentLine.getDetails();
        
        for (int j = 0; j < detRCL.size(); j++) {
            ARReceiptLinesView rclSub = (ARReceiptLinesView) detRCL.get(j);
            
            rclSub.setStDescription(acc.getStDescription());
        }

        
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

      if(receipt.getDetails().size()>0){
          receipt.setStDescription(null);
          receipt.setStShortDescription(null);
      }
   }
    
    private boolean reverseMode;

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

    public boolean lockCheck(ARReceiptLinesView rclSub){
        return rclSub.getInvoiceDetail().isCommission2()||rclSub.getInvoiceDetail().isBrokerage2()
                    ||rclSub.getInvoiceDetail().isHandlingFee2();
    }

    public void onNewTitipanPremiKomisi() throws Exception {

      //validateInvoiceAlreadyIn(parinvoiceid);

      final TitipanPremiView titipan = getRemoteAccountReceivable().getTitipanPremi(artitipanid);

      final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoiceKomisiUsingPolNo(titipan.getStPolicyNo());

      if(invoice==null)
          throw new RuntimeException("Hutang komisi tidak ditemukan, polis belum dibayar");

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

      if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());
      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());

      receipt.getDetails().add(rcl);

      if(receipt.getDetails().size()>1){
          receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " Polis");
          receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} "+receipt.getDetails().size()+ " Polis");
      }

      receipt.recalculatePembayaranKomisi();

      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandInvoiceItem();

      addGLAuto(titipan);
   }

   public void validateTitipanAlreadyIn() throws Exception{
        final DTOList detail = receipt.getDetails();

        boolean cekTitipan = true;

         for (int i = 0; i < detail.size(); i++) {
              ARReceiptLinesView rl = (ARReceiptLinesView) detail.get(i);

              DTOList titipan = rl.getListTitipan();

              //if(titipan.size()<1) cekTitipan = false;
              //if(detail.size()==1 && titipan.size() == 1) cekTitipan = false;

              for (int j = 0; j < titipan.size(); j++) {
                 ARReceiptLinesView titip = (ARReceiptLinesView) titipan.get(j);

                 TitipanPremiView titipanAsli = titip.getTitipanPremi(artitipanid);

                 if(Integer.parseInt(invoicesindex) == i && Integer.parseInt(invoicecomissionindex) == j) continue;

                 /*
                 logger.logWarning("############# yuhuuu ##########");
                 logger.logWarning("############# cek titipan polis "+ rl.getInvoice().getStAttrPolicyNo() +" titipan "+ titipanAsli.getStTransactionNo() +" ["+ titipanAsli.getStCounter() +"]: "+cekTitipan);
                 logger.logWarning("############# titip.getStTitipanPremiID() : "+titip.getStTitipanPremiID());
                 logger.logWarning("############# artitipanid : "+ artitipanid);
                 logger.logWarning("############# end yuhuuu ##########");
                 */

                 if(cekTitipan){
                     if(titip.getStTitipanPremiID().equalsIgnoreCase(artitipanid)){
                         final ARReceiptLinesView iv = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

                         iv.getListTitipan().delete(Integer.parseInt(invoicecomissionindex));

                         String counter = titipanAsli.getStCounter()!=null?titipanAsli.getStCounter():"";

                         throw new RuntimeException("Titipan Premi "+ titipanAsli.getStTransactionNo() +" Counter : "+ counter +" Sudah Dipilih");
                     }
                 }
             }  
         }
    }

   public void setAccountExcess() throws Exception{
        DTOList receiptLines = receipt.getDetails();


        //for (int i = 0; i < receiptLines.size(); i++) {
            ARReceiptLinesView rl = (ARReceiptLinesView) receiptLines.get(Integer.parseInt(invoicesindex));

            if(rl.getStARSettlementExcessID()!=null){
                rl.setArSettlementExcess(null);

                ARSettlementExcessView arSettlementExcess = rl.getARSettlementExcess();

                GLUtil.Applicator gla = new GLUtil.Applicator();

                gla.setCode('B',receipt.getStCostCenterCode());

                rl.setStExcessAccountID(gla.getAccountID(arSettlementExcess.getStGLAccount()));
            }
        //}
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

   public void uploadExcel() throws Exception {
        if (receipt.getStARSettlementID().equalsIgnoreCase("33") || receipt.getStARSettlementID().equalsIgnoreCase("45")) {
            uploadExcelKomisi();
        } else if (receipt.getStARSettlementID().equalsIgnoreCase("25") || receipt.getStARSettlementID().equalsIgnoreCase("44")) {
            uploadExcelPremiNew();
        } else if (receipt.getStARSettlementID().equalsIgnoreCase("48")) {
            uploadExcelPolisKhusus();
        }else if (receipt.getStARSettlementID().equalsIgnoreCase("49")) {
            uploadExcelRealisasiTPReinsurance();
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


//            ARInvoiceView inv = getARInvoiceUsingPolNo(cellNomorPolis.getStringCellValue(), stl.getStParameter1());

//            DTOList invoiceList = getARInvoiceByPolId(cellNomorPolis.getStringCellValue(), stl.getStParameter1());
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


            //logger.logDebug("+++++++++++++ LOOPING POLIS : "+ r);

        }

        HSSFSheet sheetTitipan = wb.getSheet("TITIPAN");

        int rows2 = sheetTitipan.getPhysicalNumberOfRows();

        String alasanTitipan = "";
        boolean cekGagalTitipan = false;

        for (int r = 5; r <= rows2; r++) {
            HSSFRow row = sheetTitipan.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellNomorBukti = row.getCell(2);//no bukti
            HSSFCell cellCounter = row.getCell(3);// counter
            HSSFCell cellJumlahTerpakai = row.getCell(5);// counter

            String counter = cellCounter.getCellType() == cellCounter.CELL_TYPE_STRING ? cellCounter.getStringCellValue() : new BigDecimal(cellCounter.getNumericCellValue()).toString();

            TitipanPremiView titipan = getARTitipanPremiUsingTrxNoAndCounter(cellNomorBukti.getStringCellValue(), counter);

            if (titipan == null) {
                alasanTitipan = alasanTitipan + "<br>Titipan " + cellNomorBukti.getStringCellValue() + " [" + counter + "] tidak ditemukan.";
                cekGagalTitipan = true;
                continue;
            }

            final ARReceiptLinesView rcl = new ARReceiptLinesView();
            rcl.markNew();

            //ARReceiptLinesView rclTitip = (ARReceiptLinesView) getListInvoices().get(Integer.parseInt(invoicesindex));

            int lastRLC = getListInvoices().size() - 1;

            ARReceiptLinesView rclTitip = (ARReceiptLinesView) getListInvoices().get(lastRLC);

            ARInvoiceView invTitip = rclTitip.getInvoice();

            rcl.setStInvoiceID(rclTitip.getStInvoiceID());
            rcl.setStInvoiceNo(rclTitip.getStInvoiceNo());
            if (invTitip != null) {
                rcl.setStPolicyID(invTitip.getStPolicyID());
                rcl.setStDescription("Titipan Premi " + invTitip.getStAttrPolicyNo());
            }

            rcl.setStCurrencyCode(receipt.getStCurrencyCode());
            rcl.setDbCurrencyRate(receipt.getDbCurrencyRate());
            rcl.setDbInvoiceAmount(null);
            rcl.setDbAmount(null);


            rcl.setStLineType(FinCodec.ARReceiptLineType.TITIPAN);
            rcl.markCommit();

            rcl.setStTitipanPremiID(titipan.getStTransactionID());
            rcl.setStDescription(titipan.getStDescription());
            rcl.setDbTitipanPremiAmount(titipan.getDbBalance());
            rcl.setDbTitipanPremiUsedAmount(new BigDecimal(cellJumlahTerpakai.getNumericCellValue()));

            rclTitip.getListTitipan().add(rcl);

            //logger.logDebug("+++++++++++++ LOOPING TITIPAN : "+ r);
        }

        getReceipt().recalculate();

        //if(cekGagal){
        //    throw new RuntimeException("Polis gagal konversi : "+ alasan);
        //}

        if (cekGagalTitipan) {
            throw new RuntimeException("Titipan gagal konversi : " + alasanTitipan);
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

            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, getReceipt().getStARSettlementID());

            DTOList invoiceList = getARInvoiceByPolId(cellNomorPolis.getStringCellValue(), stl.getStParameter1(), null);

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

        HSSFSheet sheetTitipan = wb.getSheet("TITIPAN");

        int rows2 = sheetTitipan.getPhysicalNumberOfRows();

        String alasanTitipan = "";
        boolean cekGagalTitipan = false;

        for (int r = 5; r <= rows2; r++) {
            HSSFRow row = sheetTitipan.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellNomorBukti = row.getCell(2);//no bukti
            HSSFCell cellCounter = row.getCell(3);// counter
            HSSFCell cellJumlahTerpakai = row.getCell(5);// counter

            String counter = cellCounter.getCellType() == cellCounter.CELL_TYPE_STRING ? cellCounter.getStringCellValue() : new BigDecimal(cellCounter.getNumericCellValue()).toString();

            TitipanPremiView titipan = getARTitipanPremiUsingTrxNoAndCounter(cellNomorBukti.getStringCellValue(), counter);

            if (titipan == null) {
                alasanTitipan = alasanTitipan + "<br>Titipan " + cellNomorBukti.getStringCellValue() + " [" + counter + "] tidak ditemukan.";
                cekGagalTitipan = true;
                continue;
            }

            final ARReceiptLinesView rcl = new ARReceiptLinesView();
            rcl.markNew();

            //ARReceiptLinesView rclTitip = (ARReceiptLinesView) getListInvoices().get(Integer.parseInt(invoicesindex));

            int lastRLC = getListInvoices().size() - 1;

            ARReceiptLinesView rclTitip = (ARReceiptLinesView) getListInvoices().get(lastRLC);

            ARInvoiceView invTitip = rclTitip.getInvoice();

            rcl.setStInvoiceID(rclTitip.getStInvoiceID());
            rcl.setStInvoiceNo(rclTitip.getStInvoiceNo());
            if (invTitip != null) {
                rcl.setStPolicyID(invTitip.getStPolicyID());
                rcl.setStDescription("Titipan Premi " + invTitip.getStAttrPolicyNo());
            }

            rcl.setStCurrencyCode(receipt.getStCurrencyCode());
            rcl.setDbCurrencyRate(receipt.getDbCurrencyRate());
            rcl.setDbInvoiceAmount(null);
            rcl.setDbAmount(null);


            rcl.setStLineType(FinCodec.ARReceiptLineType.TITIPAN);
            rcl.markCommit();

            rcl.setStTitipanPremiID(titipan.getStTransactionID());
            rcl.setStDescription(titipan.getStDescription());
            rcl.setDbTitipanPremiAmount(titipan.getDbBalance());
            rcl.setDbTitipanPremiUsedAmount(new BigDecimal(cellJumlahTerpakai.getNumericCellValue()));

            rclTitip.getListTitipan().add(rcl);

            //logger.logDebug("+++++++++++++ LOOPING TITIPAN : "+ r);
        }

        getReceipt().recalculate();

        //if(cekGagal){
        //    throw new RuntimeException("Polis gagal konversi : "+ alasan);
        //}

        if (cekGagalTitipan) {
            throw new RuntimeException("Titipan gagal konversi : " + alasanTitipan);
        }

    }

   public ARInvoiceView getARInvoiceUsingPolNo(String attrpolid,String param) throws Exception {

       String sql = "select * from ar_invoice a where a.cc_code = ? and a.attr_pol_no = ?";

       sql = sql + " and " + param;

       sql = sql + " and a.amount_settled is null and coalesce(a.cancel_flag,'') <> 'Y'";

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

   private TitipanPremiView getARTitipanPremiUsingTrxNoAndCounter(String trx_no, String counter) throws Exception {
        final TitipanPremiView titipan = (TitipanPremiView) ListUtil.getDTOListFromQuery(
                "select *, trim(to_char(coalesce((amount - realisasi_used),0),'999G999G999G999G999G999D99'))::character varying as jumlah"+
                " from ("+
                "         select *,"+
                "         (select coalesce(SUM(z.titipan_premi_used_amount),0) as pemakaian "+
                "                from ar_receipt y "+
                "                inner join ar_receipt_lines z on y.ar_receipt_id = z.receipt_id "+
                "                where y.status = 'POST' AND z.titipan_premi_id = a.trx_id) as realisasi_used"+
                "         from ar_titipan_premi a"+
                "         where cc_code = ? and trx_no = ? and counter = ? "+
                "         and coalesce(active_flag,'') <> 'N' and approved = 'Y' and balance <> 0"+
                " ) zz "+
                " where (amount - realisasi_used) <> 0",
                //"select * from ar_titipan_premi where cc_code = ? and trx_no = ? and counter = ? and coalesce(active_flag,'') <> 'N' and approved = 'Y' and balance <> 0",
                new Object [] {receipt.getStCostCenterCode(), trx_no, counter},
                TitipanPremiView.class
                ).getDTO();

        return titipan;
    }

   private TitipanPremiView getARTitipanPremiUsingTrxNoAndCounterCepet(String trx_no, String counter) throws Exception {
        final TitipanPremiView titipan = (TitipanPremiView) ListUtil.getDTOListFromQuery(
                " select * "+
                 " from ar_titipan_premi a "+
                  " where cc_code = ? and trx_no = ? and counter = ? "+
                  " and coalesce(active_flag,'') <> 'N' and approved = 'Y' and balance <> 0",
                //"select * from ar_titipan_premi where cc_code = ? and trx_no = ? and counter = ? and coalesce(active_flag,'') <> 'N' and approved = 'Y' and balance <> 0",
                new Object [] {receipt.getStCostCenterCode(), trx_no, counter},
                TitipanPremiView.class
                ).getDTO();

        return titipan;
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
      rcl.setStPolicyID(invoice.getStPolicyID());

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

         if(d.isFeeBase3()){
             DTOList invoicePPN = getARInvoicePPNFeeBase(invoice.getStAttrPolicyNo());

             if(invoicePPN.size()>0){
                 ARInvoiceView invPPN = (ARInvoiceView) invoicePPN.get(0);

                 onNewInvoicePembayaranKomisiByArInvoiceID(invPPN.getStARInvoiceID());
             }

         }
      }

   }

   public void validateTitipanAlreadyInUpload(String noBukti,String counter) throws Exception{
        final DTOList detail = receipt.getDetails();

         for (int i = 0; i < detail.size(); i++) {
              ARReceiptLinesView rl = (ARReceiptLinesView) detail.get(i);

              DTOList titipan = rl.getListTitipan();

              for (int j = 0; j < titipan.size(); j++) {
                 ARReceiptLinesView titip = (ARReceiptLinesView) titipan.get(j);

                 TitipanPremiView titipanAsli = titip.getTitipanPremi(titip.getStTitipanPremiID());

                 if(titipanAsli.getStTransactionNo().equalsIgnoreCase(noBukti) && titipanAsli.getStCounter().equalsIgnoreCase(counter))
                     throw new RuntimeException("Titipan no bukti : "+ noBukti +" counter : "+ counter +" sudah di entry sebelumnya");

              }
         }
    }

   public void validateInvoiceAlreadyIn2(String stPolicyNo) throws Exception{
        final DTOList detail = receipt.getDetails();

         for (int i = 0; i < detail.size(); i++) {
              ARReceiptLinesView rl = (ARReceiptLinesView) detail.get(i);

              if(rl.getInvoice()!=null)
                    if(rl.getInvoice().getStAttrPolicyNo().equalsIgnoreCase(stPolicyNo))
                        throw new RuntimeException("Tagihan "+ rl.getInvoice().getStAttrPolicyNo() +" Sudah Dipilih sebelumnya");
         }
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
            HSSFCell cellInstall = row.getCell(6);//installment

            String cicilan = null;
            if (cellInstall != null) {
                cicilan = ConvertUtil.removeTrailing(String.valueOf(cellInstall.getNumericCellValue()));
            }

            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, getReceipt().getStARSettlementID());

            DTOList invoiceList = getARInvoiceByPolId(cellNomorPolis.getStringCellValue(), stl.getStParameter1(), cicilan);

            String jenisKomisi = cellJenis.getStringCellValue();

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
//                    onNewInvoiceByInvoiceIDWithoutRecalculate(inv.getStARInvoiceID(), inv);
                    onNewInvoiceByInvoiceIDChoice(inv.getStARInvoiceID(), inv, jenisKomisi, null, null,null,null,null,null);
                }
            }

            //ARInvoiceView inv = getARInvoiceUsingPolNo(cellNomorPolis.getStringCellValue(), stl.getStParameter1());

        }

        HSSFSheet sheetTitipan = wb.getSheet("TITIPAN");

        int rows2 = sheetTitipan.getPhysicalNumberOfRows();

        String alasanTitipan = "";
        boolean cekGagalTitipan = false;

        for (int r = 5; r <= rows2; r++) {
            HSSFRow row = sheetTitipan.getRow(r);

            HSSFCell cellControl = row.getCell(0);
            if (cellControl == null) {
                break;
            }

            if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                break;
            }

            HSSFCell cellNomorBukti = row.getCell(2);//no bukti
            HSSFCell cellCounter = row.getCell(3);// counter
            HSSFCell cellJumlahTerpakai = row.getCell(5);// counter

            String counter = cellCounter.getCellType() == cellCounter.CELL_TYPE_STRING ? cellCounter.getStringCellValue() : new BigDecimal(cellCounter.getNumericCellValue()).toString();

            TitipanPremiView titipan = getARTitipanPremiUsingTrxNoAndCounter(cellNomorBukti.getStringCellValue(), counter);

            if (titipan == null) {
                alasanTitipan = alasanTitipan + "<br>Titipan " + cellNomorBukti.getStringCellValue() + " [" + counter + "] tidak ditemukan.";
                cekGagalTitipan = true;
                continue;
            }

            final ARReceiptLinesView rcl = new ARReceiptLinesView();
            rcl.markNew();

            //ARReceiptLinesView rclTitip = (ARReceiptLinesView) getListInvoices().get(Integer.parseInt(invoicesindex));

            int lastRLC = getListInvoices().size() - 1;

            ARReceiptLinesView rclTitip = (ARReceiptLinesView) getListInvoices().get(lastRLC);

            ARInvoiceView invTitip = rclTitip.getInvoice();

            rcl.setStInvoiceID(rclTitip.getStInvoiceID());
            rcl.setStInvoiceNo(rclTitip.getStInvoiceNo());
            if (invTitip != null) {
                rcl.setStPolicyID(invTitip.getStPolicyID());
                rcl.setStDescription("Titipan Premi " + invTitip.getStAttrPolicyNo());
            }

            rcl.setStCurrencyCode(receipt.getStCurrencyCode());
            rcl.setDbCurrencyRate(receipt.getDbCurrencyRate());
            rcl.setDbInvoiceAmount(null);
            rcl.setDbAmount(null);


            rcl.setStLineType(FinCodec.ARReceiptLineType.TITIPAN);
            rcl.markCommit();

            rcl.setStTitipanPremiID(titipan.getStTransactionID());
            rcl.setStDescription(titipan.getStDescription());
            rcl.setDbTitipanPremiAmount(titipan.getDbBalance());
            rcl.setDbTitipanPremiUsedAmount(new BigDecimal(cellJumlahTerpakai.getNumericCellValue()));

            rclTitip.getListTitipan().add(rcl);

            //logger.logDebug("+++++++++++++ LOOPING TITIPAN : "+ r);
        }

        getReceipt().recalculate();

        //if(cekGagal){
        //    throw new RuntimeException("Polis gagal konversi : "+ alasan);
        //}

        if (cekGagalTitipan) {
            throw new RuntimeException("Titipan gagal konversi : " + alasanTitipan);
        }

    }

   public void uploadExcelPremiNew() throws Exception {

        String fileID = getReceipt().getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheetPolis = wb.getSheet("POLIS");
        HSSFSheet sheetTitipan = wb.getSheet("TITIPAN");

        int rows = sheetPolis.getPhysicalNumberOfRows();

        int rows2 = sheetTitipan.getPhysicalNumberOfRows();

        String alasan = "";
        boolean cekGagal = false;

        boolean adaPolis = false;
        for (int r = 5; r <= rows; r++) {
                HSSFRow row = sheetPolis.getRow(r);

                HSSFCell cellControl = row.getCell(1);
                //if(cellControl==null) break;

                if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                    break;
                }

                HSSFCell cellNomorPolis = row.getCell(3);//nomor polis

                if(cellNomorPolis.getStringCellValue()!=null)
                    adaPolis = true;
        }

        //Jika ada rincian polis

        if(adaPolis){

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
                HSSFCell cellInstall = row.getCell(6);//installment
                HSSFCell cellMetode = row.getCell(7);//metode
                HSSFCell cellPolis2 = row.getCell(8); //polis 2
                HSSFCell cellPolis3 = row.getCell(9); //polis 3
                HSSFCell cellPolis4 = row.getCell(10); //polis 4
                HSSFCell cellPolis5 = row.getCell(11); //polis 5
                HSSFCell cellPolis6 = row.getCell(12); //polis 6\

                String polis2 = cellPolis2!=null?cellPolis2.getStringCellValue():"";
                String polis3 = cellPolis3!=null?cellPolis3.getStringCellValue():"";
                String polis4 = cellPolis4!=null?cellPolis4.getStringCellValue():"";
                String polis5 = cellPolis5!=null?cellPolis5.getStringCellValue():"";
                String polis6 = cellPolis6!=null?cellPolis6.getStringCellValue():"";

                String cicilan = null;
                if (cellInstall != null) {
                    if(!BDUtil.isZeroOrNull(new BigDecimal(cellInstall.getNumericCellValue())))
                        cicilan = ConvertUtil.removeTrailing(String.valueOf(cellInstall.getNumericCellValue()));
                }

                ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, getReceipt().getStARSettlementID());

                DTOList invoiceList;

                if(receipt.getStARSettlementID().equalsIgnoreCase("44")){
                    invoiceList = getARInvoiceByPolIdSentralisasi(cellNomorPolis.getStringCellValue(), stl.getStParameter1(), cicilan);
                }else{
                    invoiceList = getARInvoiceByPolId(cellNomorPolis.getStringCellValue(), stl.getStParameter1(), cicilan);
                }

                String jenisKomisi = cellJenis.getStringCellValue();
                String metode = cellMetode.getStringCellValue();

                for (int i = 0; i < invoiceList.size(); i++) {
                    ARInvoiceView inv = (ARInvoiceView) invoiceList.get(i);

                    if (inv == null) {
                        alasan = alasan + "<br>Tagihan Polis " + cellNomorPolis.getStringCellValue() + " tidak ditemukan.";
                        cekGagal = true;
                        continue;
                    }

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
                        onNewInvoiceByInvoiceIDChoice(inv.getStARInvoiceID(), inv, jenisKomisi, metode, polis2,
                            polis3,polis4,polis5,
                            polis6);
                        //XYZ
                    }
                }

                //tambahin titipan by nomor polis

                String alasanTitipan = "";
                boolean cekGagalTitipan = false;

                for (int r2 = 5; r2 <= rows2; r2++) {
                    HSSFRow row2 = sheetTitipan.getRow(r2);

                    HSSFCell cellControl2 = row2.getCell(0);
                    if (cellControl2 == null) {
                        break;
                    }

                    if (cellControl2.getStringCellValue().equalsIgnoreCase("END")) {
                        break;
                    }

                    HSSFCell cellNomorPolisTitipan = row2.getCell(2);//no bukti
                    HSSFCell cellNomorBukti = row2.getCell(3);//no bukti
                    HSSFCell cellCounter = row2.getCell(4);// counter
                    HSSFCell cellJumlahTerpakai = row2.getCell(6);// counter
                    HSSFCell cellInstallment = row2.getCell(7);// installment

                    String cicilanPolis = "";
                    if (cellInstall != null)
                        if(!BDUtil.isZeroOrNull(new BigDecimal(cellInstall.getNumericCellValue())))
                            cicilanPolis = ConvertUtil.removeTrailing(String.valueOf(cellInstall.getNumericCellValue()));

                    String cicilanTitipan = "";
                    if (cellInstallment != null)
                        if(!BDUtil.isZeroOrNull(new BigDecimal(cellInstallment.getNumericCellValue())))
                            cicilanTitipan = ConvertUtil.removeTrailing(String.valueOf(cellInstallment.getNumericCellValue()));

 
                    //cek jika bukan titipan punya nomor polis tsb, skip
                    if(!cellNomorPolis.getStringCellValue().equalsIgnoreCase(cellNomorPolisTitipan.getStringCellValue()))
                        continue;

                    if(cellNomorPolis.getStringCellValue().equalsIgnoreCase(cellNomorPolisTitipan.getStringCellValue())
                       && !cicilanPolis.equalsIgnoreCase(cicilanTitipan))
                        continue;

                    String counter = cellCounter.getCellType() == cellCounter.CELL_TYPE_STRING ? cellCounter.getStringCellValue() : new BigDecimal(cellCounter.getNumericCellValue()).toString();

                    validateTitipanAlreadyUploaded(cellNomorBukti.getStringCellValue(), counter);

                    TitipanPremiView titipan = getARTitipanPremiUsingTrxNoAndCounter(cellNomorBukti.getStringCellValue(), counter);

                    if (titipan == null) {
                        alasanTitipan = alasanTitipan + "<br>Titipan " + cellNomorBukti.getStringCellValue() + " [" + counter + "] tidak ditemukan/Sudah habis direalisasi.";
                        cekGagalTitipan = true;
                        continue;
                    }

                    final ARReceiptLinesView rcl = new ARReceiptLinesView();
                    rcl.markNew();

                    ARReceiptLinesView rclTitip = (ARReceiptLinesView) getListInvoices().get(Integer.parseInt(invoicesindex));

                    //int lastRLC = getListInvoices().size() - 1;

                    //ARReceiptLinesView rclTitip = (ARReceiptLinesView) getListInvoices().get(lastRLC);

                    ARInvoiceView invTitip = rclTitip.getInvoice();

                    rcl.setStInvoiceID(rclTitip.getStInvoiceID());
                    rcl.setStInvoiceNo(rclTitip.getStInvoiceNo());
                    if (invTitip != null) {
                        rcl.setStPolicyID(invTitip.getStPolicyID());
                        rcl.setStDescription("Titipan Premi " + invTitip.getStAttrPolicyNo());
                    }

                    rcl.setStCurrencyCode(receipt.getStCurrencyCode());
                    rcl.setDbCurrencyRate(receipt.getDbCurrencyRate());
                    rcl.setDbInvoiceAmount(null);
                    rcl.setDbAmount(null);


                    rcl.setStLineType(FinCodec.ARReceiptLineType.TITIPAN);
                    rcl.markCommit();

                    rcl.setStTitipanPremiID(titipan.getStTransactionID());
                    rcl.setStDescription(titipan.getStDescription());
                    rcl.setDbTitipanPremiAmount(titipan.getDbBalance());
                    rcl.setDbTitipanPremiUsedAmount(new BigDecimal(cellJumlahTerpakai.getNumericCellValue()));

                    rclTitip.getListTitipan().add(rcl);

                }

                if(cekGagal){
                    throw new RuntimeException("Polis gagal konversi : "+ alasan);
                }

                if (cekGagalTitipan) {
                    throw new RuntimeException("Titipan gagal konversi : " + alasanTitipan);
                }

            }
        }else if(!adaPolis){

            String alasanTitipan = "";
            boolean cekGagalTitipan = false;

            for (int r = 5; r <= rows2; r++) {
                HSSFRow row = sheetTitipan.getRow(r);

                HSSFCell cellControl = row.getCell(0);
                if (cellControl == null) {
                    break;
                }

                if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                    break;
                }

                HSSFCell cellNomorBukti = row.getCell(3);//no bukti
                HSSFCell cellCounter = row.getCell(4);// counter
                HSSFCell cellJumlahTerpakai = row.getCell(6);// counter

                String counter = cellCounter.getCellType() == cellCounter.CELL_TYPE_STRING ? cellCounter.getStringCellValue() : new BigDecimal(cellCounter.getNumericCellValue()).toString();

                validateTitipanAlreadyUploaded(cellNomorBukti.getStringCellValue(), counter);
                
                TitipanPremiView titipan = getARTitipanPremiUsingTrxNoAndCounter(cellNomorBukti.getStringCellValue(), counter);

                if (titipan == null) {
                    alasanTitipan = alasanTitipan + "<br>Titipan " + cellNomorBukti.getStringCellValue() + " [" + counter + "] tidak ditemukan.";
                    cekGagalTitipan = true;
                    continue;
                }

                final ARReceiptLinesView rcl = new ARReceiptLinesView();
                rcl.markNew();

                //ARReceiptLinesView rclTitip = (ARReceiptLinesView) getListInvoices().get(Integer.parseInt(invoicesindex));

                int lastRLC = getListInvoices().size() - 1;

                ARReceiptLinesView rclTitip = (ARReceiptLinesView) getListInvoices().get(lastRLC);

                ARInvoiceView invTitip = rclTitip.getInvoice(); 

                rcl.setStInvoiceID(rclTitip.getStInvoiceID());
                rcl.setStInvoiceNo(rclTitip.getStInvoiceNo());
                if (invTitip != null) {
                    rcl.setStPolicyID(invTitip.getStPolicyID());
                    rcl.setStDescription("Titipan Premi " + invTitip.getStAttrPolicyNo());
                }

                rcl.setStCurrencyCode(receipt.getStCurrencyCode());
                rcl.setDbCurrencyRate(receipt.getDbCurrencyRate());
                rcl.setDbInvoiceAmount(null);
                rcl.setDbAmount(null);


                rcl.setStLineType(FinCodec.ARReceiptLineType.TITIPAN);
                rcl.markCommit();

                rcl.setStTitipanPremiID(titipan.getStTransactionID());
                rcl.setStDescription(titipan.getStDescription());
                rcl.setDbTitipanPremiAmount(titipan.getDbBalance());
                rcl.setDbTitipanPremiUsedAmount(new BigDecimal(cellJumlahTerpakai.getNumericCellValue()));

                rclTitip.getListTitipan().add(rcl);

                //logger.logDebug("+++++++++++++ LOOPING TITIPAN : "+ r);
            }
        }

        

        getReceipt().recalculate();

    }

   
    public void onNewInvoiceByInvoiceIDChoice(String invoice_id, ARInvoiceView invoiceParam, String jenis, String metode, String polis2, String polis3, String polis4, String polis5, String polis6) throws Exception {

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
//        rcl.setDtReceiptDate(receipt.getDtReceiptDate());
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

        if (receipt.getStDescription() == null) {
            receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: " + rcl.getStInvoiceNo());
        }
        if (receipt.getStShortDescription() == null) {
            receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: " + rcl.getStInvoiceNo());
        }

        receipt.getDetails().add(rcl);

        if (receipt.getDetails().size() > 1) {
            receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} " + receipt.getDetails().size() + " Polis");
            receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L} " + receipt.getDetails().size() + " Polis");
        }

        invoicesindex = String.valueOf(receipt.getDetails().size() - 1);

        onExpandInvoiceItemChoice(jenis, polis2, polis3, polis4, polis5, polis6);
    }

    public void onExpandInvoiceItemChoice(String jenis, String polis2, String polis3, String polis4, String polis5, String polis6) throws Exception {
        final ARReceiptLinesView rl = (ARReceiptLinesView) getListInvoices().get(Integer.parseInt(invoicesindex));

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
             

            boolean isTax = d.isTaxComm() || d.isTaxBrok() || d.isTaxHFee() || d.isTaxHFee();

            if ((d.isComission() || d.isPPNFeebase() || d.isPPNKomisi() || d.isPPN()) && !isTax) {
                if (jenis.equalsIgnoreCase("FEEBASE")) {
                    if (d.isFeeBase3()) {
                        rcl.setStCheck("Y");
                    }
                }

                if (jenis.equalsIgnoreCase("KOMISI")) {
                    if (d.isKomisi()) {
                        rcl.setStCheck("Y");
                    }
                }

                if (jenis.equalsIgnoreCase("ALL")) {
                    rcl.setStCheck("Y");
                }

                if (jenis.equalsIgnoreCase("NONE")) {
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
             

            } else {
                if (!isTax) {
                    rcl.setStCheck("Y");
                }

                //test ksg detail
                /* REMARK KSG
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

            if (lockCheck(rcl)) {
                rcl.setStLock(false);
            }

            if (d.isComission()) {
                rcl.markAsComission();
            }

            rl.getDetails().add(rcl);
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

    public void validateRealisasi() throws Exception{
        final DTOList detail = receipt.getDetails();

        boolean adaTitipan = false;
        boolean realisasiLainnya = false;

         for (int i = 0; i < detail.size(); i++) {
              ARReceiptLinesView rl = (ARReceiptLinesView) detail.get(i);

              DTOList titipan = rl.getListTitipan();

              if(titipan.size()>0) adaTitipan = true;


              if(rl.getStInvoiceID()==null) realisasiLainnya = true;
         }

        if(detail.size()>0)
            if(!adaTitipan)
                  throw new RuntimeException("Realisasi titipan premi tidak boleh tidak ada titipan premi nya");

        if(realisasiLainnya)
            if(receipt.isPosted())
                if(!headOfficeUser)
                    throw new RuntimeException("Realisasi titipan premi lainnya hanya boleh disetujui kantor pusat");

    }

    public DTOList getARInvoiceByPolIdSentralisasi(String attrpolid, String param, String install) throws Exception {

        String sql = "select * from ar_invoice a where attr_pol_no = ?";

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
                new Object[]{attrpolid},
                ARInvoiceView.class);
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
        }
    }

    public void validateTitipanAlreadyUploaded(String noBukti,String counter) throws Exception{
        final DTOList detail = receipt.getDetails();

         for (int i = 0; i < detail.size(); i++) {
              ARReceiptLinesView rl = (ARReceiptLinesView) detail.get(i);

              DTOList titipan = rl.getListTitipan();

              for (int j = 0; j < titipan.size(); j++) {
                 ARReceiptLinesView titip = (ARReceiptLinesView) titipan.get(j);

                 TitipanPremiView titipanAsli = titip.getTitipanPremi(titip.getStTitipanPremiID());

                 if(titipanAsli.getStTransactionNo().equalsIgnoreCase(noBukti) && titipanAsli.getStCounter().equalsIgnoreCase(counter))
                     throw new RuntimeException("Titipan <b>no bukti : "+ noBukti +" Counter : "+ counter +"</b> sudah pernah dipilih");

              }
         }
    }

    public void doSavePolisKhusus() throws Exception {

       receipt.setStIDRFlag("Y");

       validateRealisasi();
       receipt.recalculate();
       getRemoteAccountReceivable().savePembayaranPremiPolisKhusus(receipt);

      super.close();
   }

    public void doApprovePolisKhusus() throws Exception {
      //receipt.setStPostedFlag("Y");
      doSavePolisKhusus();
   }

    public void editPolis(String receiptID) throws Exception {
      masterEdit(receiptID);

      editMode = true;
   }

    public void uploadExcelPolisKhusus() throws Exception {

        String fileID = getReceipt().getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheetPolis = wb.getSheet("POLIS");
        HSSFSheet sheetTitipan = wb.getSheet("TITIPAN");

        int rows = sheetPolis.getPhysicalNumberOfRows();

        int rows2 = sheetTitipan.getPhysicalNumberOfRows();

        String alasan = "";
        boolean cekGagal = false;

        boolean adaPolis = false;
        
        /*
        for (int r = 5; r <= rows; r++) {
                HSSFRow row = sheetPolis.getRow(r);

                HSSFCell cellControl = row.getCell(1);
                //if(cellControl==null) break;

                if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                    break;
                }

                HSSFCell cellNomorPolis = row.getCell(3);//nomor polis

                if(cellNomorPolis.getStringCellValue()!=null)
                    adaPolis = true;
        }
        */


        onNewLawanTitipan();

        if(adaPolis){

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
                HSSFCell cellInstall = row.getCell(6);//installment

                String cicilan = null;
                if (cellInstall != null) {
                    if(!BDUtil.isZeroOrNull(new BigDecimal(cellInstall.getNumericCellValue())))
                        cicilan = ConvertUtil.removeTrailing(String.valueOf(cellInstall.getNumericCellValue()));
                }

                ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, getReceipt().getStARSettlementID());

                DTOList invoiceList;

                if(receipt.getStARSettlementID().equalsIgnoreCase("44")){
                    invoiceList = getARInvoiceByPolIdSentralisasi(cellNomorPolis.getStringCellValue(), stl.getStParameter1(), cicilan);
                }else{
                    invoiceList = getARInvoiceByPolId(cellNomorPolis.getStringCellValue(), stl.getStParameter1(), cicilan);
                }

                String jenisKomisi = cellJenis.getStringCellValue();

                for (int i = 0; i < invoiceList.size(); i++) {
                    ARInvoiceView inv = (ARInvoiceView) invoiceList.get(i);

                    if (inv == null) {
                        alasan = alasan + "<br>Tagihan Polis " + cellNomorPolis.getStringCellValue() + " tidak ditemukan.";
                        cekGagal = true;
                        continue;
                    }

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
                        onNewInvoiceByInvoiceIDChoice(inv.getStARInvoiceID(), inv, jenisKomisi, null,null,null,null,null,null);
                    }
                }

                //tambahin titipan by nomor polis

                String alasanTitipan = "";
                boolean cekGagalTitipan = false;

                for (int r2 = 5; r2 <= rows2; r2++) {
                    HSSFRow row2 = sheetTitipan.getRow(r2);

                    HSSFCell cellControl2 = row2.getCell(0);
                    if (cellControl2 == null) {
                        break;
                    }

                    if (cellControl2.getStringCellValue().equalsIgnoreCase("END")) {
                        break;
                    }

                    HSSFCell cellNomorPolisTitipan = row2.getCell(2);//no bukti
                    HSSFCell cellNomorBukti = row2.getCell(3);//no bukti
                    HSSFCell cellCounter = row2.getCell(4);// counter
                    HSSFCell cellJumlahTerpakai = row2.getCell(6);// counter
                    HSSFCell cellInstallment = row2.getCell(7);// installment

                    String cicilanPolis = "";
                    if (cellInstall != null)
                        if(!BDUtil.isZeroOrNull(new BigDecimal(cellInstall.getNumericCellValue())))
                            cicilanPolis = ConvertUtil.removeTrailing(String.valueOf(cellInstall.getNumericCellValue()));

                    String cicilanTitipan = "";
                    if (cellInstallment != null)
                        if(!BDUtil.isZeroOrNull(new BigDecimal(cellInstallment.getNumericCellValue())))
                            cicilanTitipan = ConvertUtil.removeTrailing(String.valueOf(cellInstallment.getNumericCellValue()));


                    //cek jika bukan titipan punya nomor polis tsb, skip
                    if(!cellNomorPolis.getStringCellValue().equalsIgnoreCase(cellNomorPolisTitipan.getStringCellValue()))
                        continue;

                    if(cellNomorPolis.getStringCellValue().equalsIgnoreCase(cellNomorPolisTitipan.getStringCellValue())
                       && !cicilanPolis.equalsIgnoreCase(cicilanTitipan))
                        continue;

                    String counter = cellCounter.getCellType() == cellCounter.CELL_TYPE_STRING ? cellCounter.getStringCellValue() : new BigDecimal(cellCounter.getNumericCellValue()).toString();

                    validateTitipanPolisKhususAlreadyUploaded(cellNomorBukti.getStringCellValue(), counter);

                    TitipanPremiView titipan = getARTitipanPremiPolisKhususUsingTrxNoAndCounter(cellNomorBukti.getStringCellValue(), counter);

                    if (titipan == null) {
                        alasanTitipan = alasanTitipan + "<br>Titipan " + cellNomorBukti.getStringCellValue() + " [" + counter + "] tidak ditemukan/Sudah habis direalisasi.";
                        cekGagalTitipan = true;
                        continue;
                    }

                    final ARReceiptLinesView rcl = new ARReceiptLinesView();
                    rcl.markNew();

                    ARReceiptLinesView rclTitip = (ARReceiptLinesView) getListInvoices().get(Integer.parseInt(invoicesindex));

                    //int lastRLC = getListInvoices().size() - 1;

                    //ARReceiptLinesView rclTitip = (ARReceiptLinesView) getListInvoices().get(lastRLC);

                    ARInvoiceView invTitip = rclTitip.getInvoice();

                    rcl.setStInvoiceID(rclTitip.getStInvoiceID());
                    rcl.setStInvoiceNo(rclTitip.getStInvoiceNo());
                    if (invTitip != null) {
                        rcl.setStPolicyID(invTitip.getStPolicyID());
                        rcl.setStDescription("Titipan Premi " + invTitip.getStAttrPolicyNo());
                    }

                    rcl.setStCurrencyCode(receipt.getStCurrencyCode());
                    rcl.setDbCurrencyRate(receipt.getDbCurrencyRate());
                    rcl.setDbInvoiceAmount(null);
                    rcl.setDbAmount(null);


                    rcl.setStLineType(FinCodec.ARReceiptLineType.TITIPAN);
                    rcl.markCommit();

                    rcl.setStTitipanPremiID(titipan.getStTransactionID());
                    rcl.setStDescription(titipan.getStDescription());
                    rcl.setDbTitipanPremiAmount(titipan.getDbBalance());
                    rcl.setDbTitipanPremiUsedAmount(new BigDecimal(cellJumlahTerpakai.getNumericCellValue()));

                    rclTitip.getListTitipan().add(rcl);

                }

                if(cekGagal){
                    throw new RuntimeException("Polis gagal konversi : "+ alasan);
                }

                if (cekGagalTitipan) {
                    throw new RuntimeException("Titipan gagal konversi : " + alasanTitipan);
                }

            }
        }else if(!adaPolis){

            String alasanTitipan = "";
            boolean cekGagalTitipan = false;

            for (int r = 5; r <= rows2; r++) {
                HSSFRow row = sheetTitipan.getRow(r);

                HSSFCell cellControl = row.getCell(0);
                if (cellControl == null) {
                    break;
                }

                if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                    break;
                }

                HSSFCell cellNomorBukti = row.getCell(3);//no bukti
                HSSFCell cellCounter = row.getCell(4);// counter
                HSSFCell cellJumlahTerpakai = row.getCell(6);// counter

                String counter = cellCounter.getCellType() == cellCounter.CELL_TYPE_STRING ? cellCounter.getStringCellValue() : new BigDecimal(cellCounter.getNumericCellValue()).toString();

                validateTitipanPolisKhususAlreadyUploaded(cellNomorBukti.getStringCellValue(), counter);

                TitipanPremiView titipan = getARTitipanPremiPolisKhususUsingTrxNoAndCounter(cellNomorBukti.getStringCellValue(), counter);

                if (titipan == null) {
                    alasanTitipan = alasanTitipan + "<br>Titipan " + cellNomorBukti.getStringCellValue() + " [" + counter + "] tidak ditemukan.";
                    cekGagalTitipan = true;
                    continue;
                }

                final ARReceiptLinesView rcl = new ARReceiptLinesView();
                rcl.markNew();

                //ARReceiptLinesView rclTitip = (ARReceiptLinesView) getListInvoices().get(Integer.parseInt(invoicesindex));

                int lastRLC = getListInvoices().size() - 1;

                ARReceiptLinesView rclTitip = (ARReceiptLinesView) getListInvoices().get(lastRLC);

                ARInvoiceView invTitip = rclTitip.getInvoice();

                rcl.setStInvoiceID(rclTitip.getStInvoiceID());
                rcl.setStInvoiceNo(rclTitip.getStInvoiceNo());
                if (invTitip != null) {
                    rcl.setStPolicyID(invTitip.getStPolicyID());
                    rcl.setStDescription("Titipan Premi " + invTitip.getStAttrPolicyNo());
                }

                rcl.setStCurrencyCode(receipt.getStCurrencyCode());
                rcl.setDbCurrencyRate(receipt.getDbCurrencyRate());
                rcl.setDbInvoiceAmount(null);
                rcl.setDbAmount(null);


                rcl.setStLineType(FinCodec.ARReceiptLineType.TITIPAN);
                rcl.markCommit();

                rcl.setStTitipanPremiID(titipan.getStTransactionID());
                rcl.setStDescription(titipan.getStDescription());
                rcl.setDbTitipanPremiAmount(titipan.getDbBalance());
                rcl.setDbTitipanPremiUsedAmount(new BigDecimal(cellJumlahTerpakai.getNumericCellValue()));

                rclTitip.getListTitipan().add(rcl);

                //logger.logDebug("+++++++++++++ LOOPING TITIPAN : "+ r);
            }
        }



        getReceipt().recalculate();

    }

    private TitipanPremiView getARTitipanPremiPolisKhususUsingTrxNoAndCounter(String trx_no, String counter) throws Exception {
        final TitipanPremiView titipan = (TitipanPremiView) ListUtil.getDTOListFromQuery(
                "select *, trim(to_char(coalesce((amount - realisasi_used),0),'999G999G999G999G999G999D99'))::character varying as jumlah"+
                " from ("+
                "         select *,"+
                "         (select coalesce(SUM(z.titipan_premi_used_amount),0) as pemakaian "+
                "                from ar_receipt y "+
                "                inner join ar_receipt_lines z on y.ar_receipt_id = z.receipt_id "+
                "                where y.status = 'POST' AND z.titipan_premi_id = a.trx_id) as realisasi_used"+
                "         from ar_titipan_premi_extracomptable a"+
                "         where cc_code = ? and trx_no = ? and counter = ? "+
                "         and coalesce(active_flag,'') <> 'N' and approved = 'Y' and balance <> 0"+
                " ) zz "+
                " where (amount - realisasi_used) <> 0",
                //"select * from ar_titipan_premi where cc_code = ? and trx_no = ? and counter = ? and coalesce(active_flag,'') <> 'N' and approved = 'Y' and balance <> 0",
                new Object [] {receipt.getStCostCenterCode(), trx_no, counter},
                TitipanPremiView.class
                ).getDTO();

        return titipan;
    }

    public void validateTitipanPolisKhususAlreadyUploaded(String noBukti,String counter) throws Exception{
        final DTOList detail = receipt.getDetails();

         for (int i = 0; i < detail.size(); i++) {
              ARReceiptLinesView rl = (ARReceiptLinesView) detail.get(i);

              DTOList titipan = rl.getListTitipan();

              for (int j = 0; j < titipan.size(); j++) {
                 ARReceiptLinesView titip = (ARReceiptLinesView) titipan.get(j);

                 TitipanPremiExtracomptableView titipanAsli = titip.getTitipanPremiPolisKhusus(titip.getStTitipanPremiID());

                 if(titipanAsli.getStTransactionNo().equalsIgnoreCase(noBukti) && titipanAsli.getStCounter().equalsIgnoreCase(counter))
                     throw new RuntimeException("Titipan <b>no bukti : "+ noBukti +" Counter : "+ counter +"</b> sudah pernah dipilih");

              }
         }
    }

    public void reversePolisKhusus() throws Exception{

        if(isPosted()){
            String tahun = receipt.getStYears();
            throw new RuntimeException("Transaksi bulan "+ receipt.getStMonths() +" dan tahun "+ tahun +" tsb sudah diposting");
        }

        getRemoteAccountReceivable().reversePembayaranPolisKhusus(receipt);

        super.close();
    }

    public void doSaveTPReinsurance() throws Exception {

       receipt.setStIDRFlag("Y");

       validateRealisasi();

       receipt.recalculate();

       getRemoteAccountReceivable().saveRealisasiTitipanPremiReinsurance(receipt);

      super.close();
   }

    public void doApproveTPReinsurance() throws Exception {
      //receipt.setStPostedFlag("Y");
      doSaveTPReinsurance();
   }

    public void reverseTPReinsurance() throws Exception{

        if(isPosted()){
            String tahun = receipt.getStYears();
            throw new RuntimeException("Transaksi bulan "+ receipt.getStMonths() +" dan tahun "+ tahun +" tsb sudah diposting");
        }

        getRemoteAccountReceivable().reversePembayaranTPReinsurance(receipt);

        super.close();
    }

    public void uploadExcelRealisasiTPReinsurance() throws Exception {

        String fileID = getReceipt().getStFilePhysic();

        FileView file = FileManager.getInstance().getFile(fileID);

        FileInputStream fis = new FileInputStream(file.getStFilePath());

        POIFSFileSystem fs = new POIFSFileSystem(fis);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheetPolis = wb.getSheet("POLIS");
        HSSFSheet sheetTitipan = wb.getSheet("TITIPAN");

        int rows = sheetPolis.getPhysicalNumberOfRows();

        int rows2 = sheetTitipan.getPhysicalNumberOfRows();

        String alasan = "";
        boolean cekGagal = false;

        boolean adaPolis = false;

        for (int r = 5; r <= rows; r++) {
                HSSFRow row = sheetPolis.getRow(r);

                HSSFCell cellControl = row.getCell(1);
                //if(cellControl==null) break;

                if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                    break;
                }

                HSSFCell cellNomorPolis = row.getCell(3);//nomor polis

                HSSFCell cellNomorTrx = row.getCell(5);//nomor trx

                if(cellNomorPolis.getStringCellValue()!=null)
                    adaPolis = true;

                if(cellNomorTrx.getStringCellValue()!=null)
                    adaPolis = true;
        }

        //Jika ada rincian polis

        if(adaPolis){

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
                HSSFCell cellInstall = row.getCell(6);//installment
                HSSFCell cellNomorTrx = row.getCell(5);//nomor trx
                HSSFCell cellCcy = row.getCell(6);//nomor trx

                String cicilan = null;

                ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, getReceipt().getStARSettlementID());

                DTOList invoiceList;

                if(receipt.getStARSettlementID().equalsIgnoreCase("44")){
                    invoiceList = getARInvoiceByPolIdSentralisasi(cellNomorPolis.getStringCellValue(), stl.getStParameter1(), cicilan);
                }else{
                    if(cellNomorTrx!=null)
                        invoiceList = getARInvoiceByTrxNo(cellNomorTrx.getStringCellValue(), cellCcy.getStringCellValue());
                    else
                        invoiceList = getARInvoiceByPolId(cellNomorPolis.getStringCellValue(), stl.getStParameter1(), cicilan);
                }

                String jenisKomisi = cellJenis.getStringCellValue();

                for (int i = 0; i < invoiceList.size(); i++) {
                    ARInvoiceView inv = (ARInvoiceView) invoiceList.get(i);

                    if (inv == null) {
                        alasan = alasan + "<br>Tagihan Polis " + cellNomorPolis.getStringCellValue() + " tidak ditemukan.";
                        cekGagal = true;
                        continue;
                    }

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
                        onNewInvoiceByInvoiceIDChoice(inv.getStARInvoiceID(), inv, jenisKomisi, null, null,null,null,null,null);
                    }
                }

                //tambahin titipan by nomor polis

                String alasanTitipan = "";
                boolean cekGagalTitipan = false;

                for (int r2 = 5; r2 <= rows2; r2++) {
                    HSSFRow row2 = sheetTitipan.getRow(r2);

                    HSSFCell cellControl2 = row2.getCell(0);
                    if (cellControl2 == null) {
                        break;
                    }

                    if (cellControl2.getStringCellValue().equalsIgnoreCase("END")) {
                        break;
                    }

                    HSSFCell cellNomorPolisTitipan = row2.getCell(2);//no bukti
                    HSSFCell cellNomorBukti = row2.getCell(3);//no bukti
                    HSSFCell cellCounter = row2.getCell(4);// counter
                    HSSFCell cellJumlahTerpakai = row2.getCell(6);// counter
                    HSSFCell cellInstallment = row2.getCell(7);// installment
                    HSSFCell cellTrx = row2.getCell(7);//nomor trx

                    //cek jika bukan titipan punya nomor polis tsb, skip
                    //if(!cellNomorPolis.getStringCellValue().equalsIgnoreCase(cellNomorPolisTitipan.getStringCellValue()))
                        //continue;

                    if(cellNomorPolis==null) continue;

                    //if(cellNomorPolisTitipan==null) continue;

                    if(cellNomorPolis.getStringCellValue().equalsIgnoreCase(cellNomorPolisTitipan.getStringCellValue()) ||
                            cellNomorTrx.getStringCellValue().equalsIgnoreCase(cellTrx.getStringCellValue())){

                        String counter = cellCounter.getCellType() == cellCounter.CELL_TYPE_STRING ? cellCounter.getStringCellValue() : new BigDecimal(cellCounter.getNumericCellValue()).toString();

                        validateTitipanReinsuranceAlreadyUploaded(cellNomorBukti.getStringCellValue(), counter);

                        TitipanPremiReinsuranceView titipan = getARTitipanPremiReinsuranceUsingTrxNoAndCounter(cellNomorBukti.getStringCellValue(), counter);

                        if (titipan == null) {
                            alasanTitipan = alasanTitipan + "<br>Titipan " + cellNomorBukti.getStringCellValue() + " [" + counter + "] tidak ditemukan/Sudah habis direalisasi.";
                            cekGagalTitipan = true;
                            continue;
                        }

                        final ARReceiptLinesView rcl = new ARReceiptLinesView();
                        rcl.markNew();

                        ARReceiptLinesView rclTitip = (ARReceiptLinesView) getListInvoices().get(Integer.parseInt(invoicesindex));

                        //int lastRLC = getListInvoices().size() - 1;

                        //ARReceiptLinesView rclTitip = (ARReceiptLinesView) getListInvoices().get(lastRLC);

                        ARInvoiceView invTitip = rclTitip.getInvoice();

                        rcl.setStInvoiceID(rclTitip.getStInvoiceID());
                        rcl.setStInvoiceNo(rclTitip.getStInvoiceNo());
                        if (invTitip != null) {
                            rcl.setStPolicyID(invTitip.getStPolicyID());
                            rcl.setStDescription("Titipan Premi " + invTitip.getStAttrPolicyNo());
                        }

                        rcl.setStCurrencyCode(receipt.getStCurrencyCode());
                        rcl.setDbCurrencyRate(receipt.getDbCurrencyRate());
                        rcl.setDbInvoiceAmount(null);
                        rcl.setDbAmount(null);


                        rcl.setStLineType(FinCodec.ARReceiptLineType.TITIPAN);
                        rcl.markCommit();

                        rcl.setStTitipanPremiID(titipan.getStTransactionID());
                        rcl.setStDescription(titipan.getStDescription());
                        rcl.setDbTitipanPremiAmount(titipan.getDbBalance());
                        rcl.setDbTitipanPremiUsedAmount(new BigDecimal(cellJumlahTerpakai.getNumericCellValue()));

                        rclTitip.getListTitipan().add(rcl);
                    }

                    

                }

                if(cekGagal){
                    throw new RuntimeException("Polis gagal konversi : "+ alasan);
                }

                if (cekGagalTitipan) {
                    throw new RuntimeException("Titipan gagal konversi : " + alasanTitipan);
                }

            }
        }else if(!adaPolis){

            String alasanTitipan = "";
            boolean cekGagalTitipan = false;

            for (int r = 5; r <= rows2; r++) {
                HSSFRow row = sheetTitipan.getRow(r);

                HSSFCell cellControl = row.getCell(0);
                if (cellControl == null) {
                    break;
                }

                if (cellControl.getStringCellValue().equalsIgnoreCase("END")) {
                    break;
                }

                HSSFCell cellNomorBukti = row.getCell(3);//no bukti
                HSSFCell cellCounter = row.getCell(4);// counter
                HSSFCell cellJumlahTerpakai = row.getCell(6);// counter

                String counter = cellCounter.getCellType() == cellCounter.CELL_TYPE_STRING ? cellCounter.getStringCellValue() : new BigDecimal(cellCounter.getNumericCellValue()).toString();

                validateTitipanReinsuranceAlreadyUploaded(cellNomorBukti.getStringCellValue(), counter);

                TitipanPremiReinsuranceView titipan = getARTitipanPremiReinsuranceUsingTrxNoAndCounter(cellNomorBukti.getStringCellValue(), counter);

                if (titipan == null) {
                    alasanTitipan = alasanTitipan + "<br>Titipan " + cellNomorBukti.getStringCellValue() + " [" + counter + "] tidak ditemukan.";
                    cekGagalTitipan = true;
                    continue;
                }

                final ARReceiptLinesView rcl = new ARReceiptLinesView();
                rcl.markNew();

                //ARReceiptLinesView rclTitip = (ARReceiptLinesView) getListInvoices().get(Integer.parseInt(invoicesindex));

                int lastRLC = getListInvoices().size() - 1;

                ARReceiptLinesView rclTitip = (ARReceiptLinesView) getListInvoices().get(lastRLC);

                ARInvoiceView invTitip = rclTitip.getInvoice();

                rcl.setStInvoiceID(rclTitip.getStInvoiceID());
                rcl.setStInvoiceNo(rclTitip.getStInvoiceNo());
                if (invTitip != null) {
                    rcl.setStPolicyID(invTitip.getStPolicyID());
                    rcl.setStDescription("Titipan Premi " + invTitip.getStAttrPolicyNo());
                }

                rcl.setStCurrencyCode(receipt.getStCurrencyCode());
                rcl.setDbCurrencyRate(receipt.getDbCurrencyRate());
                rcl.setDbInvoiceAmount(null);
                rcl.setDbAmount(null);


                rcl.setStLineType(FinCodec.ARReceiptLineType.TITIPAN);
                rcl.markCommit();

                rcl.setStTitipanPremiID(titipan.getStTransactionID());
                rcl.setStDescription(titipan.getStDescription());
                rcl.setDbTitipanPremiAmount(titipan.getDbBalance());
                rcl.setDbTitipanPremiUsedAmount(new BigDecimal(cellJumlahTerpakai.getNumericCellValue()));

                rclTitip.getListTitipan().add(rcl);

                //logger.logDebug("+++++++++++++ LOOPING TITIPAN : "+ r);
            }
        }



        getReceipt().recalculate();

    }

    private TitipanPremiReinsuranceView getARTitipanPremiReinsuranceUsingTrxNoAndCounter(String trx_no, String counter) throws Exception {
        final TitipanPremiReinsuranceView titipan = (TitipanPremiReinsuranceView) ListUtil.getDTOListFromQuery(
                "select *, trim(to_char(coalesce((amount - realisasi_used),0),'999G999G999G999G999G999D99'))::character varying as jumlah"+
                " from ("+
                "         select *,"+
                "         (select coalesce(SUM(z.titipan_premi_used_amount),0) as pemakaian "+
                "                from ar_receipt y "+
                "                inner join ar_receipt_lines z on y.ar_receipt_id = z.receipt_id "+
                "                where y.status = 'POST' AND z.titipan_premi_id = a.trx_id) as realisasi_used"+
                "         from ar_titipan_premi_reinsurance a"+
                "         where cc_code = ? and trx_no = ? and counter = ? "+
                "         and coalesce(active_flag,'') <> 'N' and approved = 'Y' and balance <> 0"+
                " ) zz "+
                " where (amount - realisasi_used) <> 0",
                //"select * from ar_titipan_premi where cc_code = ? and trx_no = ? and counter = ? and coalesce(active_flag,'') <> 'N' and approved = 'Y' and balance <> 0",
                new Object [] {receipt.getStCostCenterCode(), trx_no, counter},
                TitipanPremiReinsuranceView.class
                ).getDTO();

        return titipan;
    }

    public void validateTitipanReinsuranceAlreadyUploaded(String noBukti,String counter) throws Exception{
        final DTOList detail = receipt.getDetails();

         for (int i = 0; i < detail.size(); i++) {
              ARReceiptLinesView rl = (ARReceiptLinesView) detail.get(i);

              DTOList titipan = rl.getListTitipan();

              for (int j = 0; j < titipan.size(); j++) {
                 ARReceiptLinesView titip = (ARReceiptLinesView) titipan.get(j);

                 TitipanPremiReinsuranceView titipanAsli = titip.getTitipanPremiReinsurance(titip.getStTitipanPremiID());

                 if(titipanAsli.getStTransactionNo().equalsIgnoreCase(noBukti) && titipanAsli.getStCounter().equalsIgnoreCase(counter))
                     throw new RuntimeException("Titipan <b>no bukti : "+ noBukti +" Counter : "+ counter +"</b> sudah pernah dipilih");

              }
         }
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

        String descLong = "";

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

    public DTOList getARInvoiceByTrxNo(String trxNo, String ccy) throws Exception {

        String sql = "select * from ar_invoice a where ";

        sql = sql + " a.reference_no = ? ";

        sql = sql + " and a.ccy = ? ";

        sql = sql + " and amount_settled is null and coalesce(a.cancel_flag,'') <> 'Y' ";

        if (receipt.getStARSettlementID().equalsIgnoreCase("13") || receipt.getStARSettlementID().equalsIgnoreCase("14")) {
            sql = sql + " and a.ent_id not in (select refid1 from ent_master_coas)";
        }

        //sql = sql + " limit 1";

        return ListUtil.getDTOListFromQuery(
                sql,
                new Object[]{trxNo, ccy},
                ARInvoiceView.class);
    }

    public void addTitipanAutoLunas(TitipanPremiView titipan) throws Exception {
      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();

      //final ARSettlementExcessView arSettlementExcessView = (ARSettlementExcessView) DTOPool.getInstance().getDTO(ARSettlementExcessView.class, stGLSelect);

      ARReceiptLinesView rclTitip = (ARReceiptLinesView) getListInvoices().get(Integer.parseInt(invoicesindex));
      ARInvoiceView invTitip = rclTitip.getInvoice();

      rcl.setStInvoiceID(rclTitip.getStInvoiceID());
      rcl.setStInvoiceNo(rclTitip.getStInvoiceNo());
      rcl.setStPolicyID(invTitip.getStPolicyID());
      rcl.setStDescription("Titipan Premi "+ invTitip.getStAttrPolicyNo());
      rcl.setStCurrencyCode(invTitip.getStCurrencyCode());
      rcl.setDbCurrencyRate(receipt.getDbCurrencyRate());
      rcl.setDbInvoiceAmount(null);
      rcl.setDbAmount(null);
      //rcl.setStARSettlementExcessID(arSettlementExcessView.getStARSettlementExcessID());
      rcl.setStTitipanPremiID(titipan.getStTransactionID());
      rcl.setDbTitipanPremiAmount(titipan.getDbAmount());
      rcl.setStDescription(titipan.getStDescription());
      rcl.setDbTitipanPremiUsedAmount(titipan.getDbAmount());

      rcl.setStLineType(FinCodec.ARReceiptLineType.TITIPAN);
      rcl.markCommit();

      //receipt.getGLs().add(rcl);
      rclTitip.getListTitipan().add(rcl);

      receipt.recalculate();
   }

    public void doSaveAuto() throws Exception {

       receipt.setStIDRFlag("Y");

      if(receipt.getSettlement().checkProperty("CALC_COMM","Y")){
          recalculatePembayaranKomisi();

          if(receipt.getStJournalType()!=null){
                if(receipt.isJournalOffset()) getRemoteAccountReceivable().savePembayaranKomisiSentralisasi(receipt);
                else getRemoteAccountReceivable().savePembayaranKomisi(receipt);
          }else {
              getRemoteAccountReceivable().savePembayaranKomisi(receipt);
          }

      }else{
          validateRealisasi();
          receipt.recalculate();

          if(receipt.getStJournalType()!=null){
              if(receipt.isJournalOffset()) getRemoteAccountReceivable().savePembayaranPremiSentralisasi(receipt);
              else getRemoteAccountReceivable().savePembayaranPremi(receipt);
          }else {
              getRemoteAccountReceivable().savePembayaranPremi(receipt);
          }

      }

   }

    public void onNewInvoiceAuto(DTOList titipan) throws Exception {

      validateInvoiceAlreadyIn(parinvoiceid);

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
      rcl.setStPolicyID(invoice.getStPolicyID());

      //rcl.setStNegativeFlag(d.getStNegativeFlag());
      rcl.markAsInvoice();
      rcl.markCommit();

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

      onExpandInvoiceItemAuto(titipan);
   }

    public void createAutoRealisasiTitipanPremi(InsurancePolicyView pol, List<String> policyList) throws Exception{

            //set header nya
            createNew("25");
            getReceipt().setStCostCenterCode(pol.getStCostCenterCode());
            getReceipt().setStReceiptClassID(pol.getEntity().isBPD()?"3":"4");
            getReceipt().setDtReceiptDate(new Date());
            getReceipt().setStMonths(DateUtil.getMonth2Digit(new Date()));
            getReceipt().setStYears(DateUtil.getYear(new Date()));
            getReceipt().setStAccountEntityID(pol.getStEntityID());
            getReceipt().setStCurrencyCode(pol.getStCurrencyCode());

            //cari detail polis nya, add ke pembayaran
            for (int i = 0; i < policyList.size(); i++) {
                String polID = policyList.get(i);

                logger.logDebug("################## pol id polis perpanjangan = "+ polID);

                //tambah detail polis by invoice id
                final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoiceByAttrPolID(polID);

                //dapetin polis nya
                InsurancePolicyView polBayar = getRemoteInsurance().getInsurancePolicy(polID);

                if(invoice!=null){

                    final DTOList titipanList = getRemoteAccountReceivable().getTitipanPremiSerbaguna(polBayar.getStPolicyNo());

                    //tambah invoice polis
                    setParinvoiceid(invoice.getStARInvoiceID());

                    onNewInvoiceAuto(titipanList);

                    //tambah titipan
                    //cari titipan premi
                    /*
                    final TitipanPremiView titipan = getRemoteAccountReceivable().getTitipanPremiByPolNo(polBayar.getStPolicyNo());

                    getReceipt().setStAccountEntityID(titipan.getStRefEntID());

                    addTitipanAutoLunas(titipan);
                     
                     */

                    //test ksg new

                    for (int j = 0; j < titipanList.size(); j++) {
                        TitipanPremiView titip = (TitipanPremiView) titipanList.get(j);

                        getReceipt().setStAccountEntityID(titip.getStRefEntID());

                        addTitipanAutoLunas(titip);
                    }
                }
            }

            doRecalculate();

            getReceipt().setDbEnteredAmount(getReceipt().getDbAmountApplied());

            doRecalculate();

            //simpan & setujui
            getReceipt().setStPostedFlag("Y");
            approveMode = true;
            doSaveAuto();
    }

    public void onExpandInvoiceItemAuto(DTOList titipan) throws Exception {
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

         //boolean isTax = d.isTaxComm() || d.isTaxBrok() || d.isTaxHFee();
         //if(!isTax) rcl.setStCheck("Y");

         boolean isPremiBruto = d.isPremiGross2();

         if(isPremiBruto)
             rcl.setStCheck("Y");

         //cek titipan yg sama item nya, centang
          for (int j = 0; j < titipan.size(); j++) {
              TitipanPremiView titip = (TitipanPremiView) titipan.get(j);

              if(titip.getStARTrxLineID()==null) continue;

              if(rcl.getInvoiceDetail().getStARTrxLineID().equalsIgnoreCase(titip.getStARTrxLineID()))
                  rcl.setStCheck("Y");
          }

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

                 onNewInvoiceByInvoiceID(invPPN.getStARInvoiceID());
             }

         }
      }

      receipt.recalculate();
   }

}
