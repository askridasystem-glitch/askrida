					/***********************************************************************
 * Module:  com.webfin.ar.forms.ReceiptForm
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:34 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.forms;

import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.util.DTOList;
import com.crux.util.BDUtil;
import com.crux.util.JNDIUtil;
import com.crux.pool.DTOPool;
import com.webfin.ar.model.*;
import com.crux.util.*;
import com.webfin.ar.ejb.AccountReceivableHome;
import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.gl.ejb.CurrencyManager;
import com.webfin.gl.model.ARTitipanPremiDetailsView;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.Date;
import java.math.BigDecimal;

public class TitipanForm extends Form {

   private ARTitipanView receipt;
   private ARTitipanPremiDetailsView titipan;
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
   
   private final static transient LogManager logger = LogManager.getInstance(TitipanForm.class);
  

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

   public ARTitipanView getReceipt() {
      return receipt;
   }

   public void setReceipt(ARTitipanView receipt) {
      this.receipt = receipt;
   }

   public DTOList getListNotes() throws Exception {
      return null;
   }

   public DTOList getListGLs() throws Exception {
      return null;
   }

   public DTOList getListInvoices() throws Exception {
       return null;   
   }

   public TitipanForm() {
   }

   public void onNewNoteItem() {

   }



   public void onDeleteNoteItem() throws Exception {
      getListNotes().delete(Integer.parseInt(notesindex));

      recalculate();
   }

   private void recalculate() throws Exception {
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
      getListInvoices().delete(Integer.parseInt(invoicesindex));
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

         rcl.setDbInvoiceAmount(d.getDbEnteredAmount());
         //rcl.setDbAmount(d.getDbOustandingAmount());
         rcl.setDbEnteredAmount(d.getDbOustandingAmount());
         //rcl.setDbAmountSettled(d.getDbAmountSettled());
         rcl.markAsComission();
         rcl.markCommit();

         rl.getDetails().add(rcl);
      }
//      receipt.recalculate();
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
   
   public void onExpandInvoiceItem() throws Exception {
      final ARReceiptLinesView rl = (ARReceiptLinesView)getListInvoices().get(Integer.parseInt(invoicesindex));

      final ARInvoiceView invoice = rl.getInvoice();

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

         rcl.setDbInvoiceAmount(d.getDbEnteredAmount());
         rcl.setDbAmount(d.getDbOustandingAmount());
         rcl.setDbEnteredAmount(d.getDbOustandingAmount());
         
         //if(d.getStRefID0().startsWith("TAX")) rcl.setStCheck("Y");
         //rcl.setDbAmountSettled(d.getDbAmountSettled());
         //rcl.setStNegativeFlag(d.getStNegativeFlag());
         rcl.markAsComission();
         rcl.markCommit();

         rl.getDetails().add(rcl);
      }
//      receipt.recalculate();
   }
   
   public void createNew() throws Exception {

      receipt = new ARTitipanView();
      
      
      receipt.setStCurrencyCode(CurrencyManager.getInstance().getMasterCurrency());
      receipt.setDbCurrencyRate(BDUtil.one);
      
      titipan = new ARTitipanPremiDetailsView();
      titipan.markNew();

//      receipt.setNotes(new DTOList());
//      receipt.setDetails(new DTOList());
       receipt.markNew();

//      receipt.setStARSettlementID(stSettlementID);

//      receipt.setStInvoiceType(receipt.getSettlement().getStTrxType());

//      receipt.setDtReceiptDate(new Date());

      //receipt.setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());

//      final String settlementRC = (String)receipt.getSettlement().getPropMap().get("RC");

//      receipt.setStReceiptClassID(settlementRC);

//      setTitle(receipt.getSettlement().getStDescription());

      //chgCurrency();

      editMode=true;
   }

   public void edit(String receiptID) throws Exception {
      masterEdit(receiptID);

      //if (receipt.isPosted()) throw new RuntimeException("Receipt not editable (Posted)");
      //if (receipt.isCancel()) throw new RuntimeException("Receipt not editable (VOID)");

      editMode = true;
   }
   
   
   public void superEdit(String receiptID) throws Exception {
      masterEdit(receiptID);

      editMode = true;
   }



   public void masterEdit(String receiptID) throws Exception {
      receipt = getRemoteAccountReceivable().getARTitipan(receiptID);

      receipt.markUpdateO();
	
	  
      receipt.getDetails().markAllUpdate();
      /*
      receipt.getNotes().markAllUpdate();
      receipt.getGLs().markAllUpdate();

      final DTOList details = receipt.getDetails();

      for (int i = 0; i < details.size(); i++) {
         ARReceiptLinesView rcl = (ARReceiptLinesView) details.get(i);

         rcl.getDetails().markAllUpdate();
      }

      setTitle(receipt.getSettlement().getStDescription());*/

   }

   public void view(String receiptID) throws Exception {
//      receipt = getRemoteAccountReceivable().getARReceipt(receiptID);

      super.setReadOnly(true);

      viewMode = true;
   }

   public void doSave() throws Exception {
   	  if(receipt.isNew())
      		receipt.generateTransactionNo();
//      final String trxNO = receipt.getStReceiptNo();
      //receipt.recalculate();
      getRemoteAccountReceivable().save(receipt,titipan);
      super.close();
   }

   public void doRecalculate() throws Exception {
      receipt.recalculate();
   }

   public void doCancel() {
      super.close();
   }

   public void doClose() {
      super.close();
   }

   public void afterUpdateForm() {
      try {
         //receipt.recalculate();
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void chgCurrency() throws Exception {
//      receipt.setDbCurrencyRate(CurrencyManager.getInstance().getRate(receipt.getStCurrencyCode(), receipt.getDtReceiptDate()));
   }

   public void changeReceiptClass() {
      //receipt.setStInvoiceType(receipt.getReceiptClass().getStInvoiceType());
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

//      receipt.getNotes().add(rcl);

//      receipt.recalculate();
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

//      receipt.getGLs().add(rcl);

//      receipt.recalculate();
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
   
   public void onNewInvoice() throws Exception {
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

//      receipt.setStEntityID(invoice.getStEntityID());

//      if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());
//      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+rcl.getStInvoiceNo());

//      receipt.getDetails().add(rcl);

//      receipt.recalculate();

//      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandInvoiceItem();
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
      rcl.markAsInvoice();
      rcl.markCommit();

//      receipt.setStEntityID(invoice.getStEntityID());

//      if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+nosurathutang);
//      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+nosurathutang);

//      receipt.getDetails().add(rcl);

//      receipt.recalculate();

//      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandInvoiceItem();
   }
   
   public void onNewInvoiceByNoSuratHutang(String nosurathutang) throws Exception {
      final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(parinvoiceid);
	  
      final ARReceiptLinesView rcl = new ARReceiptLinesView();
      rcl.markNew();
      
      rcl.setStInvoiceID(invoice.getStARInvoiceID());
      rcl.setStInvoiceNo(invoice.getStInvoiceNo());
      rcl.setStCurrencyCode(invoice.getStCurrencyCode());
      rcl.setDbCurrencyRate(invoice.getDbCurrencyRate());
      //rcl.setDbInvoiceAmount(invoice.getDbOutstandingAmount());
      //rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
      rcl.setDbInvoiceAmount(invoice.getDbOutstandingAmount());
      rcl.setDbEnteredAmount(rcl.getDbInvoiceAmount());
      rcl.markAsInvoice();
      rcl.markCommit();

//      receipt.setStEntityID(invoice.getStEntityID());

//      if (receipt.getStDescription()==null) receipt.setStDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+nosurathutang);
//      if (receipt.getStShortDescription()==null) receipt.setStShortDescription("{L-Eng Payment For-L}{L-INA Pembayaran-L}: "+nosurathutang);

//      receipt.getDetails().add(rcl);

//      receipt.recalculate();

//      invoicesindex=String.valueOf(receipt.getDetails().size()-1);

      onExpandInvoiceItem();
   }
   
   
   public void onNewSuratHutang() throws Exception {
      final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoiceByNoSuratHutang(nosurathutang);
      
      invoice.setStNoSuratHutang(nosurathutang);
      final DTOList listInv = invoice.getList2();
      
	  for (int i = 0; i < listInv.size(); i++) {
	  		ARInvoiceView invoiceView = (ARInvoiceView) listInv.get(i);
	  		parinvoiceid = invoiceView.getStARInvoiceID();
			onNewInvoiceByNoSuratHutang(nosurathutang);
	  }
	   
   }
   

   public void changeCostCenter() {

   }

   public void changemethod() {

   }

   public void generatRNo() throws Exception {
//      receipt.generateReceiptNo();
   }

   public void approve(String receiptID) throws Exception {
      masterEdit(receiptID);

      //if (!receipt.isPosted() && !receipt.isCancel()); else throw new RuntimeException("Invalid State");
//	  receipt.setStPostedFlag("Y");

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

//      if (!receipt.isCancel()); else throw new RuntimeException("Invalid State");

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
   
   public void onChgCurrency() throws Exception {
      receipt.setDbCurrencyRate(
              CurrencyManager.getInstance().getRate(
                      receipt.getStCurrencyCode(),
                      receipt.getDtApplyDate()
              )
      );
   }
}
