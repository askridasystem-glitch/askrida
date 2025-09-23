package com.webfin.ar.ejb;

import com.webfin.ar.model.ARInvoiceView;
import com.webfin.ar.model.ARTitipanView;
import com.webfin.ar.model.ARReceiptView;
import com.webfin.gl.model.ARTitipanPremiDetailsView;
import com.webfin.ar.forms.ReceiptForm;
import com.webfin.ar.filter.ARReceiptFilter;
import com.webfin.ar.filter.ARInvoiceFilter;
import com.crux.util.DTOList;
import com.crux.util.LOV;
import com.webfin.ar.model.ARCashflowView;
import com.webfin.ar.model.ARRequestFee;
import com.webfin.ar.model.ARTitipanPremiView2;
import com.webfin.gl.model.TitipanPremiView;
import com.webfin.insurance.model.InsuranceClosingView;
import com.webfin.insurance.model.InsurancePolicyInwardView;
import com.webfin.insurance.model.InsurancePolicyObjectView;
import com.webfin.insurance.model.InsurancePolicyView;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: Opah
 * Date: Oct 11, 2005
 * Time: 10:09:24 PM
 * To change this template use File | Settings | File Templates.
 */ 

public interface AccountReceivable extends EJBObject {
   String save(ARInvoiceView invoice)  throws Exception, RemoteException;
   
   String saveInvoiceCoas(ARInvoiceView invoice)  throws Exception, RemoteException;

   DTOList listARReceipts(ARReceiptFilter f)  throws Exception, RemoteException;

   LOV getLOVReceiptClass()  throws Exception, RemoteException;

   LOV getLOVPaymentMethod(String stReceiptClassID)  throws Exception, RemoteException;

   ARReceiptView getARReceipt(String rcid) throws Exception, RemoteException;

   ARInvoiceView getARInvoice(String invoice) throws Exception, RemoteException;

   DTOList searchInvoice(ARInvoiceFilter f) throws Exception, RemoteException;

   void save(ARReceiptView rcp) throws Exception, RemoteException;
   
   DTOList listInvoices(ARInvoiceFilter f) throws Exception, RemoteException;

   void updateARTrxMenu()  throws Exception, RemoteException;
   
   void saveSuratHutang(ARInvoiceView invoice,DTOList list)  throws Exception, RemoteException;
   
   ARInvoiceView getSuratHutang(String nosurathutang) throws Exception, RemoteException;
   
   ARInvoiceView getARInvoiceByNoSuratHutang(String nosurathutang) throws Exception, RemoteException;
   
   String saveClaimEndorse(ARInvoiceView invoice)  throws Exception, RemoteException;
   
   ARTitipanView getARTitipan(String rcid) throws Exception, RemoteException;
   
   void save(ARTitipanView titipan,ARTitipanPremiDetailsView titipanDetil) throws Exception, RemoteException;
 
   DTOList getARInvoiceByAttrPolId(String invoice) throws Exception, RemoteException;
   
   DTOList getReceiptByARInvoiceId(String invoice) throws Exception, RemoteException;

   void savePembayaranKomisi(ARReceiptView rcp) throws Exception, RemoteException;
   
   String saveSaldoAwal(ARInvoiceView invoice)  throws Exception, RemoteException;
   
   void saveReceiptTitipan(ARReceiptView rcp, ReceiptForm form) throws Exception, RemoteException;

   void saveTitipanPremi(ARTitipanPremiView2 titipan) throws Exception, RemoteException;
   
   ARInvoiceView getARInvoiceByClaimNo(String claimno) throws Exception, RemoteException;
   
   void savePembayaranReasuransi(ARReceiptView rcp) throws Exception, RemoteException;
   
   void savePembayaranPremi(ARReceiptView rcp)  throws Exception, RemoteException;
   
   void savePembayaranPajak(ARReceiptView rcp)  throws Exception, RemoteException;
   
   String saveInvoiceClaimCoas(ARInvoiceView invoice)  throws Exception, RemoteException;
   
   void savePembayaranCoas(ARReceiptView rcp)  throws Exception, RemoteException;
   
   void savePembayaranPremiPolisSementara(ARReceiptView rcp)  throws Exception, RemoteException;
   
   void savePembayaranPremiRealisasiTitipan(ARReceiptView rcp)  throws Exception, RemoteException;
   
   String saveInvoiceReins(ARInvoiceView invoice)  throws Exception, RemoteException;
   
   void savePembayaranKlaim(ARReceiptView rcp) throws Exception, RemoteException;
   
   InsurancePolicyInwardView getARInvoiceInwardTreaty(String invoice) throws Exception, RemoteException;
   
   String saveInwardTreaty(InsurancePolicyInwardView invoice)  throws Exception, RemoteException;
   
   void saveSuratHutangInward(InsurancePolicyInwardView invoice,DTOList list)  throws Exception, RemoteException;
   
   InsurancePolicyInwardView getSuratHutangInward(String nosurathutang) throws Exception, RemoteException;
   
   InsurancePolicyInwardView getARInvoiceInward(String invoice) throws Exception, RemoteException;
   
   ARInvoiceView getARInvoiceUsingPolID(String attrpolid) throws Exception, RemoteException;
    
   void savePengembalianPremi(ARReceiptView rcp) throws Exception, RemoteException;

   ARReceiptView getARReceiptForPrinting(String rcid) throws Exception, RemoteException;

   void savePembayaranKlaimLKS(ARReceiptView rcp) throws Exception, RemoteException;
   
   ARInvoiceView getARInvoiceByAttrPolID(String attrpolid) throws Exception, RemoteException;
   
   void savePembayaranRealisasiTitipan(ARReceiptView rcp) throws Exception, RemoteException;
   
   InsurancePolicyInwardView getARInvoiceForPrinting(String rcid) throws Exception, RemoteException;
   
   TitipanPremiView getTitipanPremi(String stArTitipanID) throws Exception, RemoteException;
   
   ARInvoiceView getARInvoiceUsingPolNo(String attrpolid) throws Exception, RemoteException;
   
   void savePembayaranKlaimKoas(ARReceiptView rcp) throws Exception, RemoteException;
   
   void reversePembayaran(ARReceiptView rcp)throws Exception,RemoteException;

   ARInvoiceView getARInvoiceKomisiUsingPolNo(String attrpolid) throws Exception, RemoteException;

   DTOList getARInvoiceByAttrPolIdCoasOnly(String invoice) throws Exception, RemoteException;

   void getARReceiptForPrintingExcelPremi(String rcid, String settlement) throws Exception, RemoteException;

   void getARReceiptForPrintingExcelKlaim(String rcid, String settlement) throws Exception, RemoteException;

   InsurancePolicyInwardView getARInvoiceInwardTreatyOnly(String invoice) throws Exception, RemoteException;

    void reverse(InsurancePolicyInwardView inward) throws Exception, RemoteException;

    String saveInvoiceClaim(ARInvoiceView invoice) throws Exception, RemoteException;

    void savePembayaranRealisasiUangMukaKlaim(ARReceiptView rcp) throws Exception, RemoteException;

    String saveInvoiceClaimReins(ARInvoiceView invoice) throws Exception, RemoteException;

    void reverse(ARReceiptView rcp, String receipt_no) throws Exception, RemoteException;

    String saveClaimInward(InsurancePolicyInwardView invoice, String stNextStatus) throws Exception, RemoteException;

    String saveClaimInwardClosing(InsurancePolicyInwardView invoice, InsuranceClosingView closing, String stNextStatus) throws Exception, RemoteException;

    String saveSaldoAwalInward(InsurancePolicyInwardView invoice) throws Exception, RemoteException;

    String saveSaldoAwalInwardClosing(InsurancePolicyInwardView invoice, InsuranceClosingView closing) throws Exception, RemoteException;

    InsurancePolicyInwardView getARInvoiceInwardByPolNo(String stPolicyNo) throws Exception, RemoteException;

    ARRequestFee getARRequestForPrinting(String arreqid, String alter) throws Exception, RemoteException;

    void savePembayaranPremiSentralisasi(ARReceiptView rcp) throws Exception, RemoteException;

    void savePembayaranKomisiSentralisasi(ARReceiptView rcp) throws Exception, RemoteException;

    void getARReceiptForPrintingExcelKlaimCo(String rcid, String settlement) throws Exception, RemoteException;

    void savePembayaranKlaimSentralisasi(ARReceiptView rcp) throws Exception, RemoteException;

    void savePembayaranInward(ARReceiptView rcp) throws Exception, RemoteException;

    void savePembayaranInwardNew(ARReceiptView rcp) throws Exception, RemoteException;

    ARInvoiceView getARInvoiceWithoutOrder(String invoice) throws Exception, RemoteException;

    void saveCashflowKlaim(ARCashflowView rcp) throws Exception, RemoteException;

    ARCashflowView getARCashflow(String rcid) throws Exception, RemoteException;

    void reverseCashflow(ARCashflowView rcp) throws Exception, RemoteException;

    String saveSaldoAwalInwardUpload(InsurancePolicyInwardView invoice) throws Exception, RemoteException;

    void savePenerimaanKlaimOutward(ARReceiptView rcp) throws Exception, RemoteException;

    String saveSaldoAwalInwardPajakAcrual(InsurancePolicyInwardView invoice) throws Exception, RemoteException;

    void savePembayaranPremiPolisKhusus(ARReceiptView rcp) throws Exception, RemoteException;

    void reversePembayaranPolisKhusus(ARReceiptView rcp) throws Exception, RemoteException;

    void saveRealisasiTitipanPremiReinsurance(ARReceiptView rcp) throws Exception, RemoteException;

    void reversePembayaranTPReinsurance(ARReceiptView rcp) throws Exception, RemoteException;

    void getARReceiptForPrintingExcelPremiKhusus(String rcid, String settlement) throws Exception, RemoteException;

    void createTitipanPremi(InsurancePolicyView pol, InsurancePolicyObjectView object, ARReceiptView receipt) throws Exception, RemoteException;

    TitipanPremiView getTitipanPremiByPolNo(String policyNo) throws Exception, RemoteException;

    DTOList getTitipanPremiSerbaguna(String policyNo) throws Exception, RemoteException;

}

