package com.webfin.gl.ejb;

import com.crux.util.DTOList;
import com.crux.util.LOV;
import com.webfin.ar.model.ARInvestmentBungaView;
import com.webfin.ar.model.ARInvestmentDepositoView;
import com.webfin.ar.model.ARInvestmentIzinBungaView;
import com.webfin.ar.model.ARInvestmentIzinDepositoView;
import com.webfin.ar.model.ARInvestmentIzinPencairanView;
import com.webfin.ar.model.ARInvestmentPencairanView;
import com.webfin.ar.model.ARRequestFee;
import com.webfin.gl.model.*;
import java.math.BigDecimal;
import com.webfin.gl.accounts.filter.AccountFilter;
import com.webfin.ar.model.BiayaPemasaranView;
import com.webfin.insurance.model.InsuranceClosingReportView;
import com.webfin.insurance.model.InsuranceClosingView;
import com.webfin.insurance.model.InsurancePolicySOAView;
import com.webfin.insurance.model.InsurancePostingView;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: Opah
 * Date: Jul 18, 2005
 * Time: 5:43:04 AM
 * To change this template use File | Settings | File Templates.
 */

public interface GeneralLedger extends EJBObject {
   DTOList listAccountTypes() throws Exception, RemoteException;

   DTOList listJournalMaster() throws Exception, RemoteException;

   JournalMasterView getJournalMaster(String stJournalMasterID) throws Exception, RemoteException;

   void saveJournalMaster(JournalMasterView jm) throws Exception, RemoteException;

   DTOList listDepartments() throws Exception, RemoteException;

   void saveDepartment(GLCostCenterView dept) throws Exception, RemoteException;

   GLCostCenterView getDepartment(String stDeptID) throws Exception, RemoteException;

   DTOList listAccounts(AccountFilter accountFilter) throws Exception, RemoteException;

   AccountView getAccount(String stAccountID) throws Exception, RemoteException;

   void saveAccount(AccountView account) throws Exception, RemoteException;

   DTOList getAccountTypesCombo() throws Exception, RemoteException;

   void savePeriod(PeriodHeaderView period) throws Exception, RemoteException;

   PeriodHeaderView getPeriod(Long lgPeriodID) throws Exception, RemoteException;

   DTOList listGLPeriods() throws Exception, RemoteException;

   DTOList listJournalEntry() throws Exception, RemoteException;

   DTOList getJournalEntry(String stTrxNo) throws Exception, RemoteException;

   void saveJournalEntry(DTOList l) throws Exception, RemoteException;
   
   void saveJournalEntryTes(DTOList l) throws Exception, RemoteException;
   
   void saveJournalEntry2(JournalHeaderView header, DTOList l) throws Exception, RemoteException;

   void saveJournalEntry3(JournalHeaderView header, DTOList l) throws Exception, RemoteException;

   void saveJournalEntry4(JournalHeaderView header, DTOList l) throws Exception, RemoteException;

   void saveJournalEntry5(JournalHeaderView header, DTOList l) throws Exception, RemoteException;

   DTOList getJournalTypesCombo() throws Exception, RemoteException;

   DTOList searchAccounts(String key, String costCenter) throws Exception, RemoteException;

   DTOList listReports()  throws Exception, RemoteException;

   LOV getCurrencyCodeLOV()  throws Exception, RemoteException;
   
   LOV getCostCenterCodeLOV()  throws Exception, RemoteException;
   
   LOV getMethodCodeLOV()  throws Exception, RemoteException;
   
   LOV getMethodPaymentLOV() throws Exception, RemoteException;

   GLReportView getReportDefinition(String glReportID)  throws Exception, RemoteException;

   GLReportView fillReportData(GLReportView glv) throws Exception, RemoteException;

   void closePeriod(PeriodDetailView pdd) throws Exception, RemoteException;

   void openPeriod(PeriodDetailView pdd) throws Exception, RemoteException;

   AccountView getAccountByAccountNo(String stAccountNo) throws Exception, RemoteException;

   AccountView autoCreateAccount(String acno, String costcenter) throws Exception, RemoteException;

   void reverse(String stRefTRX)  throws Exception, RemoteException;

   String generateAccount(String ac, String cc) throws Exception, RemoteException;

   DTOList getCharts() throws Exception, RemoteException;

   void saveReport(GLReportView rpt) throws Exception, RemoteException;
   
   void tesGenerateTransNo(JournalHeaderView header, DTOList l) throws Exception, RemoteException;
   
   String generateTransactionNo(JournalHeaderView header, DTOList l,String transNo) throws Exception, RemoteException;

   AccountView getAccountByAccountID(String stAccountID) throws Exception, RemoteException;

   void updateBalance(Long lgAccountID, Long lgPeriodYear, Long lgPeriodNo, BigDecimal am) throws Exception, RemoteException;

   BigDecimal getBalance(Long lgAccountID, Long lgPeriodYear, Long lgPeriodNo, BigDecimal am) throws Exception, RemoteException;

    void saveJournalSaldoAwal(DTOList l) throws Exception, RemoteException;

    String saveJournalEntryAutoPayment(DTOList l) throws Exception, RemoteException;
    
    LOV getMethodCodeLOVCashBank() throws Exception, RemoteException;
    
    void save(AccountView account) throws Exception, RemoteException;
    
    void saveJournalUangMuka(JournalHeaderView header, DTOList l) throws Exception, RemoteException;
    
    void saveJournalUangMukaPremi(JournalHeaderView header, DTOList l) throws Exception, RemoteException;
    
    void saveJournalUangMukaKomisi(JournalHeaderView header, DTOList l) throws Exception, RemoteException;
    
    void saveTitipanPremi(TitipanPremiHeaderView header, DTOList l) throws Exception, RemoteException;
    
    DTOList getTitipanPremi(String trxhdrid) throws Exception, RemoteException; 
    
    void saveEditTitipanPremi(TitipanPremiHeaderView header,DTOList l) throws Exception, RemoteException;  
    
     void saveEditCashBank(JournalHeaderView header,DTOList l) throws Exception, RemoteException;
     
     void saveJournalEntryPengembanganBisnis(JournalHeaderView header, DTOList l) throws Exception, RemoteException;
     
     void saveEditPengembanganBisnis(JournalHeaderView header,DTOList l) throws Exception, RemoteException;
    
     void saveJournalSyariah(JournalHeaderView header,DTOList l) throws Exception, RemoteException;
     
     DTOList getSyariahJournalEntry(String trxhdrid) throws Exception, RemoteException;
     
     
     void save2(AccountView2 account) throws Exception, RemoteException;
    
    void saveCurrency(GLCurrencyHistoryView currency) throws Exception, RemoteException;
    
    String save(ARInvestmentDepositoView deposito)  throws Exception, RemoteException;
    
    String savePencairan(ARInvestmentPencairanView pencairan)  throws Exception, RemoteException;
    
    void saveBunga(ARInvestmentBungaView bunga)  throws Exception, RemoteException;

    ARInvestmentDepositoView loadDeposito(String ardepoid) throws Exception, RemoteException;

    ARInvestmentPencairanView loadPencairan(String arcairid) throws Exception, RemoteException;

    ARInvestmentBungaView loadBunga(String arbungaid) throws Exception, RemoteException;
    
    void approve(ARInvestmentDepositoView deposito, DTOList l)  throws Exception, RemoteException;
    
    void approve(ARInvestmentPencairanView pencairan, DTOList l)  throws Exception, RemoteException;
    
    void approve(ARInvestmentBungaView bunga, DTOList l)  throws Exception, RemoteException;
    
    String saveRenewal(ARInvestmentDepositoView deposito, String stNextStatus, boolean approvalMode)  throws Exception, RemoteException;
    
    void savePostingGL(GLPostingView posting, boolean reOpenMode) throws Exception, RemoteException;

    void saveJournalDirectTitipanPremi(DTOList l) throws Exception, RemoteException;

    void reverseTitipan(String trx_no) throws Exception , RemoteException;

    void reverseJournal(String trx_no) throws Exception , RemoteException;

    void reverseDeposito(ARInvestmentDepositoView deposito)throws Exception,RemoteException;

    void reversePencairan(ARInvestmentPencairanView pencairan)throws Exception,RemoteException;

    void reverseBunga(ARInvestmentBungaView bunga)throws Exception,RemoteException;

    String saveWithoutJurnal(ARInvestmentDepositoView deposito)  throws Exception, RemoteException;

    String saveWithoutJurnalCair(ARInvestmentPencairanView pencairan)  throws Exception, RemoteException;

    void save3(AccountInsuranceView account) throws Exception, RemoteException;

    void AccountRekeningKantor(String am, long periodTo, long yearFrom, BigDecimal balance, String flag) throws Exception, RemoteException;

    void AccountLabaBersih(String am, long periodTo, long yearFrom, BigDecimal labaBersih, String flag) throws Exception, RemoteException;

    DTOList getBungaDeposito(String trxhdrid) throws Exception, RemoteException;

    void approveBungaDeposito(BungaDepositoHeaderView header, DTOList l) throws Exception, RemoteException;

    void saveBungaDeposito(BungaDepositoHeaderView header, DTOList l) throws Exception, RemoteException;

    void saveJournalMemorial(JournalHeaderView header, DTOList l) throws Exception, RemoteException;

    void saveClosing(InsuranceClosingView closing) throws Exception, RemoteException;

    void reverseJournalSyariah(String trx_no) throws Exception, RemoteException;

    void reverseBungaDeposito(BungaDepositoHeaderView header, DTOList l) throws Exception, RemoteException;

    void deleteBungaDeposito(String trx_no, DTOList l) throws Exception, RemoteException;

    void saveInputBilyetDeposito(BungaDepositoHeaderView header, DTOList l) throws Exception, RemoteException;

    void reverseClosing(InsuranceClosingView closing) throws Exception , RemoteException;

    DTOList getRKAPEntry(String trxhdrid) throws Exception, RemoteException;

    void saveReportRKAP(JournalHeaderView header, DTOList l) throws Exception, RemoteException;

    void savePostingGLFinalisasi(GLPostingView posting, boolean reOpenMode) throws Exception, RemoteException;

    void saveAccounts(JournalHeaderView header, DTOList l) throws Exception, RemoteException;

    void saveNoper(JournalHeaderView header, DTOList l) throws Exception, RemoteException;

    void reverseClosingClaimInward(InsuranceClosingView closing) throws Exception , RemoteException;

    void AccountNeracaItem(String am, long periodTo, long yearFrom, BigDecimal neracaItem, String flag) throws Exception, RemoteException;

    void AccountNeracaItemPerBulan(String am, long periodTo, long yearFrom, BigDecimal neracaItem, String flag) throws Exception, RemoteException;

    void saveClosingSetting(ClosingHeaderView closing) throws Exception, RemoteException;

    String saveBunga2(ARInvestmentDepositoView deposito, boolean bungaMode) throws Exception, RemoteException;

    ARRequestFee loadRequest(String arreqid) throws Exception, RemoteException;

    void saveRequest(ARRequestFee arrequest, String stNextStatus, boolean approvalMode) throws Exception, RemoteException;

    void approveCashier(ARRequestFee arrequest) throws Exception, RemoteException;

    void saveJournalCashBank(JournalHeaderView header, DTOList l) throws Exception, RemoteException;

    void saveClosingTax(InsuranceClosingView closing) throws Exception, RemoteException;

    void reverseClosingTax(InsuranceClosingView closing) throws Exception, RemoteException;

    void reverseRequest(ARRequestFee req) throws Exception, RemoteException;

    DTOList getInsuranceProposalComm(String stSHK) throws Exception, RemoteException;

    void saveClosingReport(InsuranceClosingReportView closing) throws Exception, RemoteException;

    void saveMKT(AccountMarketingView account) throws Exception, RemoteException;

    void saveClosingP(InsurancePostingView posting, boolean reOpenMode) throws Exception, RemoteException;

    void AccountNeracaItemJenas(String am, long periodTo, long yearFrom, BigDecimal neracaItem, String flag, String poltype) throws Exception, RemoteException;

    void postSOA(DTOList reinsAll, InsurancePolicySOAView soa) throws Exception, RemoteException;

    ARInvestmentIzinDepositoView loadIzinDeposito(String arizinid) throws Exception, RemoteException;

    String saveIzin(ARInvestmentIzinDepositoView izindeposito, boolean approvalMode) throws Exception, RemoteException;

    String saveIzinBentuk(ARInvestmentDepositoView deposito) throws Exception, RemoteException;

    ARInvestmentIzinPencairanView loadIzinPencairan(String arizincairid) throws Exception, RemoteException;

    String saveIzinCair(ARInvestmentIzinPencairanView izinpencairan, boolean approvalMode) throws Exception, RemoteException;

    ARInvestmentIzinBungaView loadIzinBunga(String arizincairid) throws Exception, RemoteException;

    String saveIzinBunga(ARInvestmentIzinBungaView izinbunga, boolean approvalMode) throws Exception, RemoteException;

    void saveBungaBng(ARInvestmentBungaView bunga) throws Exception, RemoteException;

    void AccountNeracaItemCabang(String am, long periodTo, long yearFrom, BigDecimal neracaItem, String flag, String cabang) throws Exception, RemoteException;

    String saveUpload(ARInvestmentDepositoView deposito) throws Exception, RemoteException;

    void reverseIzinCair(ARInvestmentIzinPencairanView izinpencairan) throws Exception, RemoteException;

    String saveUlang(ARInvestmentPencairanView pencairan) throws Exception, RemoteException;

    String savePemasaran(BiayaPemasaranView pemasaran) throws Exception, RemoteException;

    void reversePms(BiayaPemasaranView pemasaran) throws Exception, RemoteException;

    BiayaPemasaranView loadPemasaran(String arpmsid) throws Exception, RemoteException;

    BiayaPemasaranView getPemasaranForPrinting(String rcid) throws Exception, RemoteException;

    void getPemasaranForPrintingExcel(String rcid) throws Exception, RemoteException;

    BiayaPemasaranView getPemasaranForPrintingMix(String rcid) throws Exception, RemoteException;

    void approvePmsNew(BiayaPemasaranView pemasaran, DTOList l) throws Exception, RemoteException;

    void approvePmsAcrual(BiayaPemasaranView pemasaran, DTOList l) throws Exception, RemoteException;

    void approvePmsAcrualBayar(BiayaPemasaranView pemasaran, DTOList l) throws Exception, RemoteException;

    void approvePmsBayar(BiayaPemasaranView pemasaran, DTOList l) throws Exception, RemoteException;

    void reverseIzinBentuk(ARInvestmentIzinDepositoView izindeposito) throws Exception, RemoteException;

    void reverseIzinBunga(ARInvestmentIzinBungaView izinbunga) throws Exception, RemoteException;

    void saveEditTitipanPremiReinsurance(TitipanPremiReinsuranceHeaderView header, DTOList l) throws Exception, RemoteException;

    void saveTitipanPremiReinsurance(TitipanPremiReinsuranceHeaderView header, DTOList l) throws Exception, RemoteException;

    DTOList getTitipanPremiReinsurance(String trxhdrid) throws Exception, RemoteException;

    void reverseTitipanReinsurance(String trx_no) throws Exception, RemoteException;

    String saveCair(ARInvestmentDepositoView deposito, boolean cairMode) throws Exception, RemoteException;

    DTOList getTitipanPremiExtracomptable(String trxhdrid) throws Exception, RemoteException;

    void saveTitipanPremiExtracomptable(TitipanPremiExtracomptableHeaderView header, DTOList l) throws Exception, RemoteException;

    void saveEditBilyet(ARInvestmentDepositoView deposito) throws Exception, RemoteException;

    void saveJournalClosing(InsuranceClosingView closing) throws Exception, RemoteException;

}
