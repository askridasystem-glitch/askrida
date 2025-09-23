/***********************************************************************
 * Module:  com.webfin.gl.entry.helper.GLEntryHelper
 * Author:  Denny Mahendra
 * Created: Jul 22, 2005 1:21:28 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.entry.helper;

import com.crux.common.controller.Helper;
import com.crux.common.parameter.Parameter;
import com.crux.util.*;
import com.crux.web.controller.SessionManager;
import com.webfin.FinCodec;
import com.webfin.WebFinLOVRegistry;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.ejb.CurrencyManager;
import com.webfin.gl.ejb.PeriodManager;
import com.webfin.gl.model.TitipanPremiReinsuranceHeaderView ;
import com.webfin.gl.model.TitipanPremiReinsuranceView ;
import com.webfin.gl.model.AccountView;
import com.webfin.gl.model.ClosingDetailView;
import com.webfin.gl.model.JournalHeaderView;
import com.webfin.gl.util.GLUtil;

import javax.servlet.http.HttpServletRequest;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class TitipanPremiReinsuranceEntryHelper extends Helper {

   private final static transient LogManager logger = LogManager.getInstance(GLEntryHelper.class);
   private boolean edit = false;
   private boolean reverse = false;


   public boolean getEdit(){
   		return edit;
   }
   
   public void setEdit(boolean edit){
   		this.edit = edit;
   }

   private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB",GeneralLedgerHome.class.getName()))
            .create();
   }

   public void list(HttpServletRequest rq)  throws Exception {
      final DTOList list = getRemoteGeneralLedger().listJournalEntry();

      rq.setAttribute("LIST",list);
   }

   public void add(HttpServletRequest rq)  throws Exception {
      final TitipanPremiReinsuranceHeaderView  jh = new TitipanPremiReinsuranceHeaderView ();

      final DTOList details = new DTOList();

      final TitipanPremiReinsuranceView  jv = new TitipanPremiReinsuranceView ();

      jv.markNew();

      details.add(jv);

      jh.setDetails(details);

      jh.setStCurrencyCode(CurrencyManager.getInstance().getMasterCurrency());
      jh.setDbCurrencyRate(BDUtil.one);

      jh.markNew();

      jh.setDtCreateDate(new Date());
      jv.setDbEnteredDebit(BDUtil.zero);
      jv.setDbEnteredCredit(BDUtil.zero);
      jv.setDbCredit(BDUtil.zero);
      jv.setDbDebit(BDUtil.zero);
      jv.setStCounter(String.valueOf(BDUtil.one));

      populate(rq,jh);
   }

   public void editrefresh(HttpServletRequest rq)  throws Exception {
      populate(rq,null);
   }

   private void populate(HttpServletRequest rq, TitipanPremiReinsuranceHeaderView  jh) throws Exception {
      if (jh==null) jh=(TitipanPremiReinsuranceHeaderView ) get(rq,"JH");
      put(rq,"JH",jh);

      final DTOList journalTypesCombo = getRemoteGeneralLedger().getJournalTypesCombo();

      final LOV ccy = getRemoteGeneralLedger().getCurrencyCodeLOV();
      
      final LOV costcenter = getRemoteGeneralLedger().getCostCenterCodeLOV();
      
      final LOV method2 = getRemoteGeneralLedger().getMethodCodeLOV();
      
      final LOV gl_acct_id = getRemoteGeneralLedger().getMethodPaymentLOV();
      
      final LOV month = FinCodec.MonthPeriods.getLookUp();
      
      final LOV year = WebFinLOVRegistry.getInstance().LOV_GL_Years();
      
      final LOV titipanCause = FinCodec.TitipanCause.getLookUp();


      
      //final DTOList method = getRemoteGeneralLedger().getMethodCodeLOV();
      //javax.swing.JOptionPane.showMessageDialog(null,"tes = "+ gl_acct_id,"Error",javax.swing.JOptionPane.CLOSED_OPTION);
      
      rq.setAttribute("CBJT", journalTypesCombo);
      rq.setAttribute("CCYCB", ccy);
      rq.setAttribute("JH",jh);
      rq.setAttribute("CC",costcenter);
      rq.setAttribute("METHOD2",method2);
      rq.setAttribute("GL_ACCT_ID",gl_acct_id);
      rq.setAttribute("MONTH",month);
      rq.setAttribute("YEAR",year);
      rq.setAttribute("CAUSE",titipanCause);
   
      
   }

   public void edit(HttpServletRequest rq)  throws Exception {
   
      setEdit(true);
      
      final TitipanPremiReinsuranceHeaderView  jh = view(rq);

      jh.markUpdateO();

      final DTOList details = jh.getDetails();

      for (int i = 0; i < details.size(); i++) {
         TitipanPremiReinsuranceView  jv = (TitipanPremiReinsuranceView ) details.get(i);
         
         final AccountView account = getRemoteGeneralLedger().getAccountByAccountID(jv.getStAccountID());
         
         jv.setStKet(account.getStDescription());
         
         jv.setReadOnly(true);
         
         jv.markUpdate();
      }
      

   }

   public TitipanPremiReinsuranceHeaderView  view(HttpServletRequest rq)  throws Exception {
      final String trxhdrid = getString(rq.getParameter("trxhdrid"));

      final DTOList details = getRemoteGeneralLedger().getTitipanPremiReinsurance(trxhdrid);
      

      if (details.size()>0) {
         final TitipanPremiReinsuranceHeaderView  jh = new TitipanPremiReinsuranceHeaderView ();

         final TitipanPremiReinsuranceView  jv = (TitipanPremiReinsuranceView )details.get(0);
         
         if(edit)
         	if(jv.getStApproved()!=null)
         		if(jv.getStApproved().equalsIgnoreCase("Y"))
         			throw new RuntimeException("Data sudah disetujui");
         
         jh.setStTransactionNo(jv.getStTransactionNo());
         jh.setDtCreateDate(jv.getDtCreateDate());
         jh.setDtApplyDate(jv.getDtApplyDate());
         jh.setStCurrencyCode(jv.getStCurrencyCode());
         jh.setDbCurrencyRate(jv.getDbCurrencyRate());
         jh.setStJournalCode(jv.getStJournalCode());
         jh.setStCostCenter(jv.getStTransactionNo().substring(5,7));
         jh.setStMethodCode(jv.getStTransactionNo().substring(0,1));
	 jh.setReadOnly(true);
         jh.setStAccountIDMaster(jv.getStAccountIDMaster());
         jh.setStDescriptionMaster(jv.getStDescriptionMaster());
         jh.setStMonths(jv.getStMonths());
         jh.setStYears(jv.getStYears());
         //jh.setStCause(jv.getStCause());
         
         final AccountView account = getRemoteGeneralLedger().getAccountByAccountID(String.valueOf(jh.getStAccountIDMaster()));
         jh.setStAccountNoMaster(account.getStAccountNo());
         
         jh.setDetails(details);
         
         final DTOList details2 = jh.getDetails();
         
         for (int i = 0; i < details2.size(); i++) {
             TitipanPremiReinsuranceView  jv2 = (TitipanPremiReinsuranceView ) details2.get(i);

             final AccountView account2 = getRemoteGeneralLedger().getAccountByAccountID(String.valueOf(jv2.getStAccountID()));

             jv2.setStKet(account2.getStDescription());
         }
         
         jh.setDetails(details2);

         populate(rq,jh);

         return jh;
      } else
         throw new Exception("transaction number not found");
   }

   public void saveTP(HttpServletRequest rq)  throws Exception {
      final TitipanPremiReinsuranceHeaderView  jh = retrieveCashBank(rq);

      final DTOList details = jh.getDetails();
      
      validate(jh, details);

      getRemoteGeneralLedger().saveTitipanPremiReinsurance(jh,details);
   }

   public void detailedit(HttpServletRequest rq)  throws Exception {
   }

   public void detailsave(HttpServletRequest rq)  throws Exception {
   }

   public void addline(HttpServletRequest rq)  throws Exception {
      final TitipanPremiReinsuranceHeaderView  jh = retrieve(rq);

      final DTOList details = jh.getDetails();

      final TitipanPremiReinsuranceView  jv = new TitipanPremiReinsuranceView ();

      int counter = details.size()+1;
      
      jv.setDbEnteredDebit(BDUtil.zero);
      jv.setDbEnteredCredit(BDUtil.zero);
      jv.setDbCredit(BDUtil.zero);
      jv.setDbDebit(BDUtil.zero);
      jv.setStCounter(String.valueOf(counter));
      jv.markNew();
 
      details.add(jv);

      populate(rq,jh);
      
   }
   

   public void delline(HttpServletRequest rq)  throws Exception {
      final TitipanPremiReinsuranceHeaderView  jh = retrieve(rq);

      jh.getDetails().delete(getLong(rq.getParameter("rn")).intValue());

      populate(rq,jh);
   }

   private TitipanPremiReinsuranceHeaderView  retrieve(HttpServletRequest rq) throws Exception {
      final TitipanPremiReinsuranceHeaderView  jh = (TitipanPremiReinsuranceHeaderView )get(rq,"JH");

      jh.setStTransactionNo(getString(rq.getParameter("trxno")));

      final Date createdate = getDate(rq.getParameter("createdate"));
      final boolean createdateChanged = !Tools.isEqual(createdate,jh.getDtCreateDate());
      jh.setDtCreateDate(createdate);

      final String ccy = getString(rq.getParameter("ccy"));
      final boolean ccyChanged = !Tools.isEqual(ccy,jh.getStCurrencyCode());
      jh.setStCurrencyCode(ccy);

      jh.setDbCurrencyRate(getNum(rq.getParameter("rate")));

      if (createdateChanged || ccyChanged)
         jh.setDbCurrencyRate(CurrencyManager.getInstance().getRate(jh.getStCurrencyCode(), jh.getDtApplyDate()));

      jh.setStJournalCode(getString(rq.getParameter("jt")));
      
      jh.setStCostCenter(getString(rq.getParameter("costcenter")));
      jh.setStMethodCode(getString(rq.getParameter("method2")));
      jh.setStGlAccountID(getString(rq.getParameter("gl_acct_id")));
      jh.setStMonths(getString(rq.getParameter("month")));
      jh.setStYears(getString(rq.getParameter("year")));
      //jh.setStCause(getString(rq.getParameter("cause")));
      
      jh.setStAccountIDMaster(getString(rq.getParameter("acidmaster")));
      jh.setStAccountNoMaster(getString(rq.getParameter("acnomaster")));
      jh.setStDescriptionMaster(getString(rq.getParameter("descmaster")));
      jh.setStKetMaster(getString(rq.getParameter("keterangan")));  
      jh.setStIDRFlag(getString(rq.getParameter("idrflag")));

      final DTOList details = jh.getDetails();

      for (int i = 0; i < details.size(); i++) {
         TitipanPremiReinsuranceView  jv = (TitipanPremiReinsuranceView ) details.get(i);

         jv.setStTransactionNo(jh.getStTransactionNo());
         jv.setDtApplyDate(jh.getDtApplyDate());
         jv.setStCurrencyCode(jh.getStCurrencyCode());
         jv.setDbCurrencyRate(jh.getDbCurrencyRate());
         jv.setStJournalCode(jh.getStJournalCode());
         jv.setStIDRFlag(jh.getStIDRFlag());
         jv.setStKet(getString(rq.getParameter("keterangan"+i)));

         jv.setStAccountID(getString(rq.getParameter("acid"+i)));
         jv.setStAccountNo(getString(rq.getParameter("acno"+i)));
         jv.setStDescription(getString(rq.getParameter("desc"+i)));
         jv.setDbEnteredCredit(getNum(rq.getParameter("credit"+i)));
         jv.setDbEnteredDebit(getNum(rq.getParameter("debit"+i)));
         jv.setDtApplyDate(getDate(rq.getParameter("trxdate"+i)));
         jv.setStPolicyID(getString(rq.getParameter("polid"+i)));
         jv.setStPolicyNo(getString(rq.getParameter("polno"+i)));
         jv.setDbEnteredDebit(getNum(rq.getParameter("debit"+i)));
         jv.setDbExcessAmount(getNum(rq.getParameter("excess"+i)));
         
         jv.setStMonths(jh.getStMonths());
         jv.setStYears(jh.getStYears());
         jv.setStCause(getString(rq.getParameter("cause"+i)));
         jv.setStCounter(String.valueOf(i+1));


         jv.reCalculate();
      }

      //jh.reCalculate();

      return jh;
   }

   public void changeCCy(HttpServletRequest rq)  throws Exception {
      final TitipanPremiReinsuranceHeaderView  jh = retrieve(rq);

      populate(rq,jh);
   }
   
   /*
   public void changeMethod(HttpServletRequest rq)  throws Exception {
      final TitipanPremiReinsuranceHeaderView  jh = retrieve(rq);

	  
	  
      populate(rq,jh);
   }*/
   
    public void addCashBank(HttpServletRequest rq)  throws Exception {
      final TitipanPremiReinsuranceHeaderView  jh = new TitipanPremiReinsuranceHeaderView ();

      final DTOList details = new DTOList();

      final TitipanPremiReinsuranceView  jv = new TitipanPremiReinsuranceView ();

      jv.markNew();

      details.add(jv);

      jh.setDetails(details);

      jh.setStCurrencyCode(CurrencyManager.getInstance().getMasterCurrency());
      jh.setDbCurrencyRate(BDUtil.one);
      jh.setDbEnteredDebitMaster(BDUtil.zero);
      jh.setDbEnteredCreditMaster(BDUtil.zero);
      jh.setDbCreditMaster(BDUtil.zero);
      jh.setDbDebitMaster(BDUtil.zero);
      jh.setStCostCenter(SessionManager.getInstance().getSession().getStBranch());
      
      jh.markNew();

      //jh.setDtCreateDate(new Date());
      
      jv.setDbEnteredDebit(BDUtil.zero);
      jv.setDbEnteredCredit(BDUtil.zero);
      jv.setDbCredit(BDUtil.zero);
      jv.setDbDebit(BDUtil.zero);
      jv.setStActiveFlag("Y");
      jv.setStCounter(String.valueOf(BDUtil.one));

      populateCashBank(rq,jh);
   }
    
    private void populateCashBank(HttpServletRequest rq, TitipanPremiReinsuranceHeaderView  jh) throws Exception {
      if (jh==null) jh=(TitipanPremiReinsuranceHeaderView ) get(rq,"JH");
      put(rq,"JH",jh);

      final DTOList journalTypesCombo = getRemoteGeneralLedger().getJournalTypesCombo();

      final LOV ccy = getRemoteGeneralLedger().getCurrencyCodeLOV();
      
      final LOV costcenter = getRemoteGeneralLedger().getCostCenterCodeLOV();
      
      final LOV method2 = getRemoteGeneralLedger().getMethodCodeLOVCashBank();
      
      final LOV gl_acct_id = getRemoteGeneralLedger().getMethodPaymentLOV();
      
      final LOV month = FinCodec.MonthPeriods.getLookUp();
      
      final LOV year = WebFinLOVRegistry.getInstance().LOV_GL_Years();
      
      final LOV titipanCause = FinCodec.TitipanCause.getLookUp();
         
      rq.setAttribute("CBJT", journalTypesCombo);
      rq.setAttribute("CCYCB", ccy);
      rq.setAttribute("JH",jh);
      rq.setAttribute("CC",costcenter);
      rq.setAttribute("METHOD2",method2);
      rq.setAttribute("GL_ACCT_ID",gl_acct_id);
      rq.setAttribute("MONTH",month);
      rq.setAttribute("YEAR",year);
      rq.setAttribute("CAUSE",titipanCause);
   
      
   }
    
    private TitipanPremiReinsuranceHeaderView  retrieveCashBank(HttpServletRequest rq) throws Exception {
      final TitipanPremiReinsuranceHeaderView  jh = (TitipanPremiReinsuranceHeaderView )get(rq,"JH");

      jh.setStTransactionNo(getString(rq.getParameter("trxno")));

      final Date createdate = getDate(rq.getParameter("createdate"));
      final boolean createdateChanged = !Tools.isEqual(createdate,jh.getDtCreateDate());
      jh.setDtCreateDate(createdate);

      final String ccy = getString(rq.getParameter("ccy"));
      final boolean ccyChanged = !Tools.isEqual(ccy,jh.getStCurrencyCode());
      jh.setStCurrencyCode(ccy);

      jh.setDbCurrencyRate(getNum(rq.getParameter("rate")));
      

      if (createdateChanged || ccyChanged)
         jh.setDbCurrencyRate(CurrencyManager.getInstance().getRate(jh.getStCurrencyCode(), jh.getDtApplyDate()));

      jh.setStJournalCode(getString(rq.getParameter("jt")));
      
      jh.setStCostCenter(getString(rq.getParameter("costcenter")));
      jh.setStMethodCode(getString(rq.getParameter("method2")));
      jh.setStGlAccountID(getString(rq.getParameter("gl_acct_id")));
      
      jh.setStAccountIDMaster(getString(rq.getParameter("acidmaster")));
      jh.setStAccountNoMaster(getString(rq.getParameter("acnomaster")));
      jh.setStDescriptionMaster(getString(rq.getParameter("descmaster")));
      jh.setStKetMaster(getString(rq.getParameter("keterangan"))); 
      jh.setDbEnteredCreditMaster(getNum(rq.getParameter("creditmaster")));
      jh.setDbEnteredDebitMaster(getNum(rq.getParameter("debitmaster")));
      
      jh.setStAccountIDMaster(getString(rq.getParameter("acidmaster")));
      jh.setStDescriptionMaster(getString(rq.getParameter("descmaster")));
      jh.setStMonths(getString(rq.getParameter("month")));
      jh.setStYears(getString(rq.getParameter("year")));
      //jh.setStCause(getString(rq.getParameter("cause")));
      jh.setStIDRFlag(getString(rq.getParameter("idrflag")));
      

      final GLUtil.Applicator applicator = new GLUtil.Applicator();

      final DTOList details = jh.getDetails();

      for (int i = 0; i < details.size(); i++) {
         TitipanPremiReinsuranceView  jv = (TitipanPremiReinsuranceView ) details.get(i);

         jv.setStTransactionNo(jh.getStTransactionNo());
         jv.setDtApplyDate(jh.getDtApplyDate());
         jv.setStCurrencyCode(jh.getStCurrencyCode());
         jv.setDbCurrencyRate(jh.getDbCurrencyRate());
         jv.setStJournalCode(jh.getStJournalCode());
         jv.setStIDRFlag(jh.getStIDRFlag());
         jv.setStKet(getString(rq.getParameter("keterangan"+i)));

         jv.setStAccountID(getString(rq.getParameter("acid"+i)));
         jv.setStAccountNo(getString(rq.getParameter("acno"+i)));
         jv.setStDescription(getString(rq.getParameter("desc"+i)));
         jv.setDbEnteredCredit(getNum(rq.getParameter("credit"+i)));
         jv.setDbEnteredDebit(getNum(rq.getParameter("debit"+i)));
         jv.setDtApplyDate(getDate(rq.getParameter("trxdate"+i)));
         jv.setStPolicyID(getString(rq.getParameter("polid"+i)));
         jv.setStPolicyNo(getString(rq.getParameter("polno"+i)));
         jv.setStReference1(getString(rq.getParameter("ref1"+i)));
         jv.setStAccountIDMaster(getString(rq.getParameter("acidmaster")));
         jv.setStDescriptionMaster(getString(rq.getParameter("descmaster")));
         jv.setDbBalance(getNum(rq.getParameter("balance"+i)));
         jv.setStCostCenter(getString(rq.getParameter("costcenter")));
         jv.setDbAmount(getNum(rq.getParameter("amount"+i)));

         if(jv.getStTransactionID()==null)
                jv.setDbBalance(jv.getDbAmount());

         jv.setStActiveFlag("Y");
         jv.setStMonths(jh.getStMonths());
         jv.setStYears(jh.getStYears());
         //jv.setStCause(jh.getStCause());
         jv.setStCause(getString(rq.getParameter("cause"+i)));
         jv.setStCounter(String.valueOf(i+1));
         jv.setDbExcessAmount(getNum(rq.getParameter("excess"+i)));

         //48921 20035 00 20
         //12210 41007 00 41
         //01234 56789 0123456
         //jv.setStAccountID(Long.valueOf(applicator.getAccountID("48920"+ jh.getStAccountNoMaster().substring(5, 10)+ "00")));

         jv.reCalculate();
      }

      //jh.reCalculate();

      return jh;
   }
   
    public void addlineCashBank(HttpServletRequest rq)  throws Exception {
      final TitipanPremiReinsuranceHeaderView  jh = retrieveCashBank(rq);

      final DTOList details = jh.getDetails();

      final TitipanPremiReinsuranceView  jv = new TitipanPremiReinsuranceView ();

      if(details.size()>0){
          TitipanPremiReinsuranceView titipanAwal = (TitipanPremiReinsuranceView) details.get(0);

          jv.setStAccountID(titipanAwal.getStAccountID());
          jv.setStAccountNo(titipanAwal.getStAccountNo());
      }

      jv.setDbEnteredDebit(BDUtil.zero);
      jv.setDbEnteredCredit(BDUtil.zero);
      jv.setDbCredit(BDUtil.zero);
      jv.setDbDebit(BDUtil.zero);
      jv.setStCounter(String.valueOf(details.size()+1));
      
      
      jv.markNew();

      details.add(jv);

      populateCashBank(rq,jh);
      
   }
    
    public void dellineCashBank(HttpServletRequest rq)  throws Exception {
      final TitipanPremiReinsuranceHeaderView  jh = retrieveCashBank(rq);

      //int size = jh.getDetails().size();

      if(jh.getDetails().size()>1){
          jh.getDetails().delete(getLong(rq.getParameter("rn")).intValue());
      }


      //retrieveCashBank(rq);
/*
      if(size == 1){
          final DTOList details = jh.getDetails();

          TitipanPremiReinsuranceView titip = (TitipanPremiReinsuranceView) details.get(0);
            titip.setStCounter(null);
            titip.setStAccountID(null);
            titip.setStPolicyID(null);
            titip.setStPolicyNo(null);
            titip.setDbAmount(null);
            titip.setDbBalance(null);
            titip.setStDescription(null);
            titip.setStCause(null);
            titip.setDtApplyDate(null);
            titip.setStAccountID(null);
            titip.setStAccountNo(null);
      }*/

      populateCashBank(rq,jh);
   }
    
    public void editrefreshCashBank(HttpServletRequest rq)  throws Exception {
      populateCashBank(rq,null);
   }
    
    public void save(HttpServletRequest rq)  throws Exception {
      final TitipanPremiReinsuranceHeaderView jh = retrieve(rq);

      final DTOList details = jh.getDetails();
      
      //logger.logDebug("save: "+details);
      
      getRemoteGeneralLedger().saveEditTitipanPremiReinsurance(jh,details);
   }
    
    public void approve(HttpServletRequest rq)  throws Exception {

      setEdit(true);
      
      final TitipanPremiReinsuranceHeaderView  jh = view(rq);

      cekClosingStatus("APPROVE", jh.getStCostCenter());
      
      jh.setReadOnly(true);

      jh.markUpdateO();

      jh.setStApproved("Y");
      
      final DTOList details = jh.getDetails();

      for (int i = 0; i < details.size(); i++) {
         TitipanPremiReinsuranceView  jv = (TitipanPremiReinsuranceView ) details.get(i);
         
         final AccountView account = getRemoteGeneralLedger().getAccountByAccountID(String.valueOf(jv.getStAccountID()));
         
         jv.setStKet(account.getStDescription());
         
         jv.setReadOnly(true);
         
         jv.markUpdate();
         
         jv.setStApproved("Y");
      }
      

   }
    
    private void validate(TitipanPremiReinsuranceHeaderView jh, DTOList details) throws Exception{
        //if (!Tools.isEqual(DateUtil.getMonth2Digit(jh.getDtCreateDate()),jh.getStMonths())) throw new Exception("Bulan tidak sama dengan Tanggal Entry");
        //if (!Tools.isEqual(DateUtil.getYear(jh.getDtCreateDate()),jh.getStYears())) throw new Exception("Tahun tidak sama dengan Tanggal Entry");

        //if(details.size() == 1) return;
        //12210 13086 00 13
        //01234 56789 01234
        String glCode = jh.getStAccountNoMaster().substring(5,10);

          for (int i = 0; i < details.size(); i++) {
             TitipanPremiReinsuranceView  jv = (TitipanPremiReinsuranceView ) details.get(i);

             String glCodeDetail = jv.getAccount().getStAccountNo().substring(5,10);

             if (!Tools.isEqual(DateUtil.getMonth2Digit(jv.getDtApplyDate()),jh.getStMonths())) throw new Exception("Bulan tidak sama dengan Bulan Entry");
             if (!Tools.isEqual(DateUtil.getYear(jv.getDtApplyDate()),jh.getStYears())) throw new Exception("Tahun tidak sama dengan Tahun Entry");
             
             if(jv.getStPolicyNo()!=null)
                 if(jv.getStPolicyNo().length()!=18)
                     throw new RuntimeException("Nomor Polis harus 18 digit");

             if(!glCode.equalsIgnoreCase(glCodeDetail))
                 throw new RuntimeException("Kode akun bank ("+ jh.getStAccountNoMaster() +") dengan kode akun titipan ("+ jv.getAccount().getStAccountNo() +")tidak sama");

          }

          if(isPosted(jh.getStMonths(), jh.getStYears(), jh.getStCostCenter())){
              throw new Exception("Transaksi bulan "+ jh.getStMonths() +" Tahun "+ jh.getStYears()+ " Sudah diposting");
          }

    }

    public void reverse(HttpServletRequest rq)  throws Exception {

      setEdit(false);

      setReverse(true);

      rq.setAttribute("REVERSE","Y");

      final TitipanPremiReinsuranceHeaderView  jh = view(rq);

      cekClosingStatus("REVERSE", jh.getStCostCenter());

      //getRemoteGeneralLedger().reverseTitipan(jh.getStTransactionNo());
      cekRealisasiTitipanPremi(jh.getStTransactionNo());

      jh.setReadOnly(true);

      jh.markUpdateO();

      final DTOList details = jh.getDetails();

      jh.setStApproved("N");

      for (int i = 0; i < details.size(); i++) {
         TitipanPremiReinsuranceView  jv = (TitipanPremiReinsuranceView ) details.get(i);

         final AccountView account = getRemoteGeneralLedger().getAccountByAccountID(String.valueOf(jv.getStAccountID()));

         jv.setStKet(account.getStDescription());

         jv.setReadOnly(true);

         jv.markUpdate();

         jv.setStApproved("N");
      }


   }

    /**
     * @return the reverse
     */
    public boolean isReverse() {
        return reverse;
    }

    /**
     * @param reverse the reverse to set
     */
    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public void doReverse(HttpServletRequest rq)  throws Exception {
      final TitipanPremiReinsuranceHeaderView  jh = retrieveCashBank(rq);

      if(isPosted(jh.getStMonths(), jh.getStYears(), jh.getStCostCenter())){
          setEdit(false);

          setReverse(true);

          rq.setAttribute("REVERSE","Y");
          
          throw new Exception("Transaksi bulan "+ jh.getStMonths() +" Tahun "+ jh.getStYears()+ " Sudah diposting");
      }

      cekClosingStatus("REVERSE", jh.getStCostCenter());

      final DTOList details = jh.getDetails();

      validate(jh, details);

      getRemoteGeneralLedger().reverseTitipanReinsurance(jh.getStTransactionNo());

   }

    public void setDate(HttpServletRequest rq)  throws Exception{
        final TitipanPremiReinsuranceHeaderView  jh = retrieveCashBank(rq);

        String date = "01/"+ jh.getStMonths()+"/"+jh.getStYears();

        logger.logDebug("date = "+ date);
        final DTOList details = jh.getDetails();
        for (int i = 0; i < details.size(); i++) {
            TitipanPremiReinsuranceView titip = (TitipanPremiReinsuranceView) details.get(i);

            titip.setDtApplyDate(DateUtil.getDate(date));
        }


    }

    public void changeYear(HttpServletRequest rq) throws Exception{
        final TitipanPremiReinsuranceHeaderView jh = retrieveCashBank(rq);

        if(isPosted(jh.getStMonths(), jh.getStYears(), jh.getStCostCenter()))
            throw new Exception("Transaksi bulan "+ jh.getStMonths() +" Tahun "+ jh.getStYears()+ " Sudah diposting");

        cekClosingStatus("INPUT", jh.getStCostCenter());

        populateCashBank(rq,null);

    }

    public boolean isPosted(String bulan, String tahun, String cabang) throws Exception {

            SQLUtil S = new SQLUtil();

            boolean isPosted = false;

            try {
                String cek = "select gl_post_id from gl_posting where months = ? and years = ? and posted_flag = 'Y'";

                if(cabang!=null)
                    cek = cek + " and cc_code = ?";

                PreparedStatement PS = S.setQuery(cek);
                PS.setString(1, bulan);
                PS.setString(2, tahun);

                if(cabang!=null)
                      PS.setString(3, cabang);

                ResultSet RS = PS.executeQuery();

                if (RS.next()){
                    isPosted = true;
                }

            } finally {
                S.release();
            }

        return isPosted;
    }

    public void delete(HttpServletRequest rq)  throws Exception {

      setEdit(true);

      final TitipanPremiReinsuranceHeaderView jh = view(rq);

      if(jh.getStApproved()!=null)
          if(Tools.isYes(jh.getStApproved()))
              throw new RuntimeException("Data sudah di setujui, tidak bisa dihapus");

      jh.markDelete();

      final DTOList details = jh.getDetails();

      for (int i = 0; i < details.size(); i++) {
         TitipanPremiReinsuranceView jv = (TitipanPremiReinsuranceView) details.get(i);

         final AccountView account = getRemoteGeneralLedger().getAccountByAccountID(jv.getStAccountID());
         
         jv.setStKet(account.getStDescription());

         jv.setReadOnly(true);

         jv.markDelete();
      }
      

   }

   private void cekRealisasiTitipanPremi(String trxNo) throws Exception{

       final SQLUtil S = new SQLUtil();

       try{
            String cekTitipan = " select a.receipt_no "+
                            " from ar_receipt a "+
                            " inner join ar_receipt_lines b on a.ar_receipt_id = b.receipt_id "+
                            " inner join ar_titipan_premi_reinsurance c on b.titipan_premi_id = c.trx_id "+
                            " where a.status = 'POST' and c.trx_no = ? "+
                            " group by a.receipt_no ";

            boolean sudahRealisasi = false;

            PreparedStatement PSCekTitipan = S.setQuery(cekTitipan);

            PSCekTitipan.setObject(1, trxNo);

            ResultSet res = PSCekTitipan.executeQuery();

            String noBuktiRealisasi = "";

            if (res.next()) {
                sudahRealisasi = true;

                res.beforeFirst();

                while (res.next()) {
                    noBuktiRealisasi = noBuktiRealisasi + ", <br>" + res.getString(1);
                }

                if(sudahRealisasi)
                        throw new RuntimeException("Titipan premi sudah direalisasi, perbaiki dulu data realisasi pada nomor bukti : " + noBuktiRealisasi);
                
            }

       } finally {
            S.release();
        }

   }

   public void cekClosingStatus(String status, String cabang) throws Exception{

        final boolean blockClosingEndOfDay = Parameter.readBoolean("FINANCE_CLOSING_END_OF_DAY");

        if(blockClosingEndOfDay){

                final ClosingDetailView cls = PeriodManager.getInstance().getClosing(DateUtil.getYear(DateUtil.getNewDate()), cabang);

                if(cls!=null){
                    
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
                            if(dtBatasInput.isBefore(now))
                                throw new RuntimeException("Tidak bisa input data karena sudah lewat batas jam input "+ DateUtil.getDateTimeStr2(dtBatasInput.toDate()));

                        if(status.equalsIgnoreCase("APPROVE"))
                            if(dtBatasReverseSetujui.isBefore(now))
                                throw new RuntimeException("Tidak bisa setujui data karena sudah lewat batas jam validasi "+ DateUtil.getDateTimeStr2(dtBatasReverseSetujui.toDate()));

                        if(status.equalsIgnoreCase("REVERSE"))
                            if(dtBatasReverseSetujui.isBefore(now))
                                throw new RuntimeException("Tidak bisa reverse data karena sudah lewat batas jam validasi "+ DateUtil.getDateTimeStr2(dtBatasReverseSetujui.toDate()));
                }
                

        }


    }


}
