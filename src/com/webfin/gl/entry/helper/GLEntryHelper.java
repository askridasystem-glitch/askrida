/***********************************************************************
 * Module:  com.webfin.gl.entry.helper.GLEntryHelper
 * Author:  Denny Mahendra
 * Created: Jul 22, 2005 1:21:28 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.entry.helper;

import com.crux.common.controller.Helper;
import com.crux.util.*;
import com.crux.web.controller.SessionManager;
import com.webfin.FinCodec;
import com.webfin.WebFinLOVRegistry;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.ejb.CurrencyManager;
import com.webfin.gl.model.AccountDivisionView;
import com.webfin.gl.model.JournalHeaderView;
import com.webfin.gl.model.JournalView;
import com.webfin.gl.model.AccountView;
import com.webfin.insurance.ejb.UserManager;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class GLEntryHelper extends Helper {

   private final static transient LogManager logger = LogManager.getInstance(GLEntryHelper.class);
   private boolean edit = false;


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
      final JournalHeaderView jh = new JournalHeaderView();

      final DTOList details = new DTOList();

      final JournalView jv = new JournalView();

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

      populate(rq,jh);
   }

   public void editrefresh(HttpServletRequest rq)  throws Exception {
      populate(rq,null);
   }

   private void populate(HttpServletRequest rq, JournalHeaderView jh) throws Exception {
      if (jh==null) jh=(JournalHeaderView) get(rq,"JH");
      put(rq,"JH",jh);

      final DTOList journalTypesCombo = getRemoteGeneralLedger().getJournalTypesCombo();

      final LOV ccy = getRemoteGeneralLedger().getCurrencyCodeLOV();
      
      final LOV costcenter = getRemoteGeneralLedger().getCostCenterCodeLOV();
      
      final LOV method2 = getRemoteGeneralLedger().getMethodCodeLOV();
      
      final LOV gl_acct_id = getRemoteGeneralLedger().getMethodPaymentLOV();
      
      final LOV month = FinCodec.MonthPeriods.getLookUp();
      
      final LOV year = WebFinLOVRegistry.getInstance().LOV_GL_Years();

      final LOV owner = WebFinLOVRegistry.getInstance().LOV_OwnerDivision();

      final LOV user = WebFinLOVRegistry.getInstance().LOV_UserDivision();

      final LOV bisnis = WebFinLOVRegistry.getInstance().LOV_BusinessType();
      
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
      rq.setAttribute("OWNER",owner);
      rq.setAttribute("USER",user);
      rq.setAttribute("BISNIS",bisnis);
        
   }

   public void edit(HttpServletRequest rq)  throws Exception {
      setEdit(true);

      final JournalHeaderView jh = view(rq);

      //if (!isEditByHeadOffice())
      //{
          if(isPosted(jh.getStMonths(), jh.getStYears(), jh.getStCostCenter())){

              setEdit(false);

              rq.setAttribute("REVERSE","Y");

              throw new RuntimeException("Transaksi bulan "+ jh.getStMonths() +" Tahun "+ jh.getStYears()+ " Sudah diposting");
          }
      //}
      

      jh.markUpdateO();

      final DTOList details = jh.getDetails();

      for (int i = 0; i < details.size(); i++) {
         JournalView jv = (JournalView) details.get(i);
         
         final AccountView account = getRemoteGeneralLedger().getAccountByAccountID(String.valueOf(jv.getLgAccountID()));
         
         jv.setStKet(account.getStDescription());
         
         jv.setReadOnly(true);
         
         jv.markUpdate();
      }

   }

   public JournalHeaderView view(HttpServletRequest rq)  throws Exception {
      final String trxhdrid = getString(rq.getParameter("trxhdrid"));

      final DTOList details = getRemoteGeneralLedger().getJournalEntry(trxhdrid);
      

      if (details.size()>0) {
         final JournalHeaderView jh = new JournalHeaderView();

         final JournalView jv = (JournalView)details.get(0);
         
         if(edit)
         	if(jv.getStApproved()!=null)
         		if(jv.getStApproved().equalsIgnoreCase("Y"))
         			throw new RuntimeException("Can't Edit Approved Data");

         jh.setStMonths(jv.getStMonths());
         jh.setStYears(jv.getStYears());

         if(jh.getStMonths()==null){
             jh.setStMonths(DateUtil.getMonth2Digit(jv.getDtApplyDate()));
         }

         if(jh.getStYears()==null){
             jh.setStYears(DateUtil.getYear(jv.getDtApplyDate()));
         }

         jh.setStCostCenter(jv.getStTransactionNo().substring(5,7));

         if(edit){
             //if (!isEditByHeadOffice())
             {
                 if(isPosted(jh.getStMonths(), jh.getStYears(), jh.getStCostCenter())){
                         throw new RuntimeException("Transaksi bulan "+ jh.getStMonths() +" Tahun "+ jh.getStYears()+ " Sudah diposting");
                 }
             }
         }             
             
         jh.setStTransactionNo(jv.getStTransactionNo());
         jh.setDtCreateDate(jv.getDtCreateDate());
         jh.setDtApplyDate(jv.getDtApplyDate());
         jh.setStCurrencyCode(jv.getStCurrencyCode());
         jh.setDbCurrencyRate(jv.getDbCurrencyRate());
         jh.setStJournalCode(jv.getStJournalCode());
         
         jh.setStMethodCode(jv.getStTransactionNo().substring(0,1));
         
	 jh.setReadOnly(true);
         jh.setStPostedFlag(jv.getStPostedFlag());
		          
         jh.setDetails(details);
         
         final DTOList details2 = jh.getDetails();
         
         for (int i = 0; i < details2.size(); i++) {
             JournalView jv2 = (JournalView) details2.get(i);

             final AccountView account = getRemoteGeneralLedger().getAccountByAccountID(String.valueOf(jv2.getLgAccountID()));

             if(account==null)
                 throw new RuntimeException("Kode account jurnal : "+ jv2.getStDescription() + " tidak ditemukan, cek tabel account");

             jv2.setStKet(account.getStDescription());
         }
         
         jh.setDetails(details2);

         populate(rq,jh);

         return jh;
      } else
         throw new Exception("transaction number not found");
   }

   public void save(HttpServletRequest rq)  throws Exception {
      final JournalHeaderView jh = retrieve(rq);

      final DTOList details = jh.getDetails();
      
      //logger.logDebug("save: "+details);
      
      getRemoteGeneralLedger().saveJournalEntry4(jh,details);
      //getRemoteGeneralLedger().saveJournalEntry3(jh,details);
   }
   
   public void approve(HttpServletRequest rq)  throws Exception {
      final JournalHeaderView jh = retrieve(rq);

      final DTOList details = jh.getDetails();
      
      //logger.logDebug("save: "+details);
      
      getRemoteGeneralLedger().saveJournalEntry5(jh,details);
      //getRemoteGeneralLedger().saveJournalEntry3(jh,details);
   }
   
   public void tesGenNo(HttpServletRequest rq)  throws Exception {
      final JournalHeaderView jh = retrieve(rq);

      final DTOList details = jh.getDetails();
      
      //logger.logDebug("tes: "+details);

      getRemoteGeneralLedger().tesGenerateTransNo(jh,details);
      
      //getRemoteGeneralLedger().saveJournalEntry3(jh,details);
   }
   
   public void saveCBGL(HttpServletRequest rq)  throws Exception {
      final JournalHeaderView jh = retrieveCashBank(rq);

      final DTOList details = jh.getDetails();

      validateAccounts(details);

      final String codeMethod = "";

      getRemoteGeneralLedger().saveJournalEntry3(jh,details);
   }

   public void detailedit(HttpServletRequest rq)  throws Exception {
   }

   public void detailsave(HttpServletRequest rq)  throws Exception {
   }

   public void addline(HttpServletRequest rq)  throws Exception {
      final JournalHeaderView jh = retrieve(rq);

      final DTOList details = jh.getDetails();

      final JournalView jv = new JournalView();

      jv.setDbEnteredDebit(BDUtil.zero);
      jv.setDbEnteredCredit(BDUtil.zero);
      jv.setDbCredit(BDUtil.zero);
      jv.setDbDebit(BDUtil.zero);
      jv.markNew();

      details.add(jv);

      populate(rq,jh);
      
   }
   

   public void delline(HttpServletRequest rq)  throws Exception {
      final JournalHeaderView jh = retrieve(rq);

      jh.getDetails().delete(getLong(rq.getParameter("rn")).intValue());

      populate(rq,jh);
   }

   private JournalHeaderView retrieve(HttpServletRequest rq) throws Exception {
      final JournalHeaderView jh = (JournalHeaderView)get(rq,"JH");

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
      
      jh.setLgAccountIDMaster(getLong(rq.getParameter("acidmaster")));
      jh.setStAccountNoMaster(getString(rq.getParameter("acnomaster")));
      jh.setStDescriptionMaster(getString(rq.getParameter("descmaster")));
      jh.setStKetMaster(getString(rq.getParameter("keterangan")));
      jh.setStMonths(getString(rq.getParameter("month")));
      jh.setStYears(getString(rq.getParameter("year")));

      final DTOList details = jh.getDetails();

      for (int i = 0; i < details.size(); i++) {
         JournalView jv = (JournalView) details.get(i);

         jv.setStTransactionNo(jh.getStTransactionNo());
         jv.setDtApplyDate(jh.getDtApplyDate());
         jv.setStCurrencyCode(jh.getStCurrencyCode());
         jv.setDbCurrencyRate(jh.getDbCurrencyRate());
         jv.setStJournalCode(jh.getStJournalCode());
         jv.setStKet(getString(rq.getParameter("keterangan"+i)));

         jv.setLgAccountID(getLong(rq.getParameter("acid"+i)));
         jv.setStAccountNo(getString(rq.getParameter("acno"+i)));
         jv.setStDescription(getString(rq.getParameter("desc"+i)));
         jv.setDbEnteredCredit(getNum(rq.getParameter("credit"+i)));
         jv.setDbEnteredDebit(getNum(rq.getParameter("debit"+i)));
         jv.setDtApplyDate(getDate(rq.getParameter("trxdate"+i)));
         jv.setStPolicyID(getString(rq.getParameter("polid"+i)));
         jv.setStPolicyNo(getString(rq.getParameter("polno"+i)));
         jv.setStMonths(jh.getStMonths());
         jv.setStYears(jh.getStYears());

         jv.reCalculate();
      }

      //jh.reCalculate();

      return jh;
   }

   public void changeCCy(HttpServletRequest rq)  throws Exception {
      final JournalHeaderView jh = retrieve(rq);

      populate(rq,jh);
   }
   
   /*
   public void changeMethod(HttpServletRequest rq)  throws Exception {
      final JournalHeaderView jh = retrieve(rq);

	  
	  
      populate(rq,jh);
   }*/
   
    public void addCashBank(HttpServletRequest rq)  throws Exception {
      final JournalHeaderView jh = new JournalHeaderView();

      final DTOList details = new DTOList();

      final JournalView jv = new JournalView();

      jv.markNew();

      details.add(jv);

      jh.setDetails(details);

      jh.setStCostCenter(UserManager.getInstance().getUser().getStBranch());
      jh.setStCurrencyCode(CurrencyManager.getInstance().getMasterCurrency());
      jh.setDbCurrencyRate(BDUtil.one);
      jh.setDbEnteredDebitMaster(BDUtil.zero);
      jh.setDbEnteredCreditMaster(BDUtil.zero);
      jh.setDbCreditMaster(BDUtil.zero);
      jh.setDbDebitMaster(BDUtil.zero);
      
      jh.markNew();

      jh.setDtCreateDate(new Date());
      
      jv.setDbEnteredDebit(BDUtil.zero);
      jv.setDbEnteredCredit(BDUtil.zero);
      jv.setDbCredit(BDUtil.zero);
      jv.setDbDebit(BDUtil.zero);

      if(UserManager.getInstance().getUser().getStBranch()!=null || UserManager.getInstance().getUser().getStRegion()!=null){
          String userCode = getUserCode(UserManager.getInstance().getUser().getStBranch(), UserManager.getInstance().getUser().getStRegion());

          if(userCode!=null)
            jv.setStUserCode(userCode);
      }
      

      populateCashBank(rq,jh);
   }
    
    private void populateCashBank(HttpServletRequest rq, JournalHeaderView jh) throws Exception {
      if (jh==null) jh=(JournalHeaderView) get(rq,"JH");
      put(rq,"JH",jh);

      final DTOList journalTypesCombo = getRemoteGeneralLedger().getJournalTypesCombo();

      final LOV ccy = getRemoteGeneralLedger().getCurrencyCodeLOV();
      
      final LOV costcenter = getRemoteGeneralLedger().getCostCenterCodeLOV();
      
      final LOV method2 = getRemoteGeneralLedger().getMethodCodeLOVCashBank();
      
      final LOV gl_acct_id = getRemoteGeneralLedger().getMethodPaymentLOV();
      
      final LOV month = FinCodec.MonthPeriods.getLookUp();
      
      final LOV year = WebFinLOVRegistry.getInstance().LOV_GL_Years();

      final LOV owner = WebFinLOVRegistry.getInstance().LOV_OwnerDivision();

      final LOV user = WebFinLOVRegistry.getInstance().LOV_UserDivision();

      final LOV bisnis = WebFinLOVRegistry.getInstance().LOV_BusinessType();
         
      rq.setAttribute("CBJT", journalTypesCombo);
      rq.setAttribute("CCYCB", ccy);
      rq.setAttribute("JH",jh);
      rq.setAttribute("CC",costcenter);
      rq.setAttribute("METHOD2",method2);
      rq.setAttribute("GL_ACCT_ID",gl_acct_id);
      rq.setAttribute("MONTH",month);
      rq.setAttribute("YEAR",year);
      rq.setAttribute("OWNER",owner);
      rq.setAttribute("USER",user);
      rq.setAttribute("BISNIS",bisnis);
   
      
   }
    
    private JournalHeaderView retrieveCashBank(HttpServletRequest rq) throws Exception {
      final JournalHeaderView jh = (JournalHeaderView)get(rq,"JH");

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
      
      jh.setLgAccountIDMaster(getLong(rq.getParameter("acidmaster")));
      jh.setStAccountNoMaster(getString(rq.getParameter("acnomaster")));
      jh.setStDescriptionMaster(getString(rq.getParameter("descmaster")));
      jh.setStKetMaster(getString(rq.getParameter("keterangan"))); 
      jh.setDbEnteredCreditMaster(getNum(rq.getParameter("creditmaster")));
      jh.setDbEnteredDebitMaster(getNum(rq.getParameter("debitmaster")));
      jh.setStMonths(getString(rq.getParameter("month")));
      jh.setStYears(getString(rq.getParameter("year")));

      final DTOList details = jh.getDetails();

      for (int i = 0; i < details.size(); i++) {
         JournalView jv = (JournalView) details.get(i);

         jv.setStTransactionNo(jh.getStTransactionNo());
         jv.setDtApplyDate(jh.getDtApplyDate());
         jv.setStCurrencyCode(jh.getStCurrencyCode());
         jv.setDbCurrencyRate(jh.getDbCurrencyRate());
         jv.setStJournalCode(jh.getStJournalCode());
         jv.setStKet(getString(rq.getParameter("keterangan"+i)));

         jv.setLgAccountID(getLong(rq.getParameter("acid"+i)));
         jv.setStAccountNo(getString(rq.getParameter("acno"+i)));
         jv.setStDescription(getString(rq.getParameter("desc"+i)));
         jv.setDbEnteredCredit(getNum(rq.getParameter("credit"+i)));
         jv.setDbEnteredDebit(getNum(rq.getParameter("debit"+i)));
         jv.setDtApplyDate(getDate(rq.getParameter("trxdate"+i)));
         jv.setStPolicyID(getString(rq.getParameter("polid"+i)));
         jv.setStPolicyNo(getString(rq.getParameter("polno"+i)));
         jv.setStReference1(getString(rq.getParameter("ref1"+i)));
         jv.setStMonths(jh.getStMonths());
         jv.setStYears(jh.getStYears());

         jv.setStOwnerCode(getString(rq.getParameter("owner"+i)));
         jv.setStUserCode(getString(rq.getParameter("user"+i)));
         jv.setStBusinessTypeID(getString(rq.getParameter("bisnis"+i)));

         jv.reCalculate();
      }

      //jh.reCalculate();

      return jh;
   }
   
    public void addlineCashBank(HttpServletRequest rq)  throws Exception {
      final JournalHeaderView jh = retrieveCashBank(rq);

      final DTOList details = jh.getDetails();

      final JournalView jv = new JournalView();

      jv.setDbEnteredDebit(BDUtil.zero);
      jv.setDbEnteredCredit(BDUtil.zero);
      jv.setDbCredit(BDUtil.zero);
      jv.setDbDebit(BDUtil.zero);

      if(UserManager.getInstance().getUser().getStBranch()!=null || UserManager.getInstance().getUser().getStRegion()!=null){
          String userCode = getUserCode(UserManager.getInstance().getUser().getStBranch(), UserManager.getInstance().getUser().getStRegion());

          if(userCode!=null)
            jv.setStUserCode(userCode);
      }
      
      jv.markNew();

      details.add(jv);

      populateCashBank(rq,jh);
      
   }
    
    public void dellineCashBank(HttpServletRequest rq)  throws Exception {
      final JournalHeaderView jh = retrieveCashBank(rq);

      jh.getDetails().delete(getLong(rq.getParameter("rn")).intValue());

      populateCashBank(rq,jh);
   }
    
    public void saveUangMuka(HttpServletRequest rq)  throws Exception {
      final JournalHeaderView jh = retrieveCashBank(rq);

      final DTOList details = jh.getDetails();

      final String codeMethod = "";
      
      getRemoteGeneralLedger().saveJournalUangMuka(jh,details);
   }
    
    public void saveUangMukaPremi(HttpServletRequest rq)  throws Exception {
      final JournalHeaderView jh = retrieveCashBank(rq);

      final DTOList details = jh.getDetails();

      final String codeMethod = "";
      
      getRemoteGeneralLedger().saveJournalUangMukaPremi(jh,details);
   }
    
    public void saveUangMukaKomisi(HttpServletRequest rq)  throws Exception {
      final JournalHeaderView jh = retrieveCashBank(rq);

      final DTOList details = jh.getDetails();

      final String codeMethod = "";
      
      getRemoteGeneralLedger().saveJournalUangMukaKomisi(jh,details);
   }
    
    public void editrefreshCashBank(HttpServletRequest rq)  throws Exception {
      populateCashBank(rq,null);
   }
     
    public void saveEditCBGL(HttpServletRequest rq)  throws Exception {
      final JournalHeaderView jh = retrieveCashBank(rq);

      final DTOList details = jh.getDetails();

      //validateDoubleInput(details);

      final String codeMethod = "";

      getRemoteGeneralLedger().saveEditCashBank(jh,details);
   }

    public void reverse(HttpServletRequest rq)  throws Exception {

      setEdit(false);

      rq.setAttribute("REVERSE","Y");

      final JournalHeaderView jh = view(rq);

      jh.markUpdateO();

      jh.setStApproved("N");

      final DTOList details = jh.getDetails();

      for (int i = 0; i < details.size(); i++) {
         JournalView jv = (JournalView) details.get(i);

         final AccountView account = getRemoteGeneralLedger().getAccountByAccountID(String.valueOf(jv.getLgAccountID()));

         jv.setStKet(account.getStDescription());

         jv.setReadOnly(true);

         jv.markUpdate();

         jv.setStApproved("N");
      }


   }

    public void doReverse(HttpServletRequest rq)  throws Exception {

      final JournalHeaderView jh = retrieve(rq);

      //final DTOList details = jh.getDetails();

      getRemoteGeneralLedger().reverseJournal(jh.getStTransactionNo());

   }

    public void delete(HttpServletRequest rq)  throws Exception {

      setEdit(true);
      
      final JournalHeaderView jh = view(rq);

      if(isPosted(jh.getStMonths(), jh.getStYears(), jh.getStCostCenter())){
                    throw new RuntimeException("Data sudah diposting, tidak bisa dihapus");
      }

      jh.markDelete();

      final DTOList details = jh.getDetails();

      for (int i = 0; i < details.size(); i++) {
         JournalView jv = (JournalView) details.get(i);

         final AccountView account = getRemoteGeneralLedger().getAccountByAccountID(String.valueOf(jv.getLgAccountID()));

         jv.setStKet(account.getStDescription());

         jv.setReadOnly(true);

         jv.markDelete();
      }

   }
    
    public void changeYear(HttpServletRequest rq) throws Exception{
        final JournalHeaderView jh = retrieveCashBank(rq);

        if(isPosted(jh.getStMonths(), jh.getStYears(), jh.getStCostCenter()))
            throw new Exception("Transaksi bulan "+ jh.getStMonths() +" Tahun "+ jh.getStYears()+ " Sudah diposting");

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

    public void saveMemorial(HttpServletRequest rq)  throws Exception {
      final JournalHeaderView jh = retrieve(rq);

      final DTOList details = jh.getDetails();

      logger.logDebug("save memorial: "+details);

      getRemoteGeneralLedger().saveJournalMemorial(jh,details);

   }

    public void addMemorial(HttpServletRequest rq)  throws Exception {
      final JournalHeaderView jh = new JournalHeaderView();

      final DTOList details = new DTOList();

      final JournalView jv = new JournalView();

      jv.markNew();

      details.add(jv);

      jh.setDetails(details);

      jh.setStCurrencyCode(CurrencyManager.getInstance().getMasterCurrency());
      jh.setDbCurrencyRate(BDUtil.one);
      jh.setStMethodCode("F");
      jh.setStJournalCode("AOG");

      jh.markNew();

      jh.setDtCreateDate(new Date());
      jv.setDbEnteredDebit(BDUtil.zero);
      jv.setDbEnteredCredit(BDUtil.zero);
      jv.setDbCredit(BDUtil.zero);
      jv.setDbDebit(BDUtil.zero);
      jv.setStMethodCode("F");
      jv.setStJournalCode("AOG");

      populate(rq,jh);
   }

    public void memorialChangeYear(HttpServletRequest rq) throws Exception{
        final JournalHeaderView jh = retrieveCashBank(rq);

        if(isPosted(jh.getStMonths(), jh.getStYears(), jh.getStCostCenter()))
            throw new Exception("Transaksi bulan "+ jh.getStMonths() +" Tahun "+ jh.getStYears()+ " Sudah diposting");

        populateCashBank(rq,null);

    }

    private boolean editByHeadOffice = SessionManager.getInstance().getSession().hasResource("EDIT_BY_KP");

    /**
     * @return the editByHeadOffice
     */
    public boolean isEditByHeadOffice() {
        return editByHeadOffice;
    }

    /**
     * @param editByHeadOffice the editByHeadOffice to set
     */
    public void setEditByHeadOffice(boolean editByHeadOffice) {
        this.editByHeadOffice = editByHeadOffice;
    }

    public String getUserCode(String cc_code, String regionID)throws Exception{
        final SQLUtil S = new SQLUtil();
        String userCode = null;
        try {

            String sql = "select code from s_division";

            if(cc_code!=null){
                sql = sql + " where cc_code = ? ";
            }

            if(regionID!=null){
                sql = sql + " and region_id = ? ";
            }

            final PreparedStatement PS = S.setQuery(sql);

            if(cc_code!=null){
               PS.setString(1,cc_code);
            }

             if(regionID!=null){
                 PS.setString(2,regionID);
             }

            final ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                userCode = RS.getString("code");
            }

            return userCode;

        }finally{
            S.release();
        }


    }

    public void validateAccounts(DTOList journal) throws Exception{
        final DTOList details = journal;

        for (int i = 0; i < details.size(); i++) {
            JournalView det = (JournalView) details.get(i);

            //cek jika kode akun tidak sesuai dengan list akun pemilik
            if(det.getStOwnerCode()!=null){

                final DTOList accountDiv = getAccountDivision(det.getStOwnerCode(), det.getStAccountNo());

                if(accountDiv.size()<1){
                    throw new RuntimeException(" Account "+ det.getStAccountNo() +" tidak ada pada daftar account Pemilik : "+det.getDivision().getStDescription());
                }
            }

            //validateDoubleInputPerJournal(det);

            //cek jika akun beban namun tidak isi pemilik
            /*final DTOList accountCek = getAccountByAccountNo(det.getStAccountNo());

            if(accountCek.size()>0){
                if(det.getStOwnerCode()==null){
                    throw new RuntimeException(" Account "+ det.getStAccountNo() +" harus mengisi Pemilik Anggaran");
                }
            }*/
        }
    }

    public void validateDoubleInput(DTOList journal) throws Exception{
        final DTOList details = journal;

        for (int i = 0; i < details.size(); i++) {
            JournalView det = (JournalView) details.get(i);

            validateDoubleInputPerJournal(det);
        }
    }

    private void validateDoubleInputPerJournal(JournalView journal) throws Exception{

        final SQLUtil S = new SQLUtil();

        try {
                String query = "select * "+
                                " from gl_je_detail "+
                                " where accountid = ? "+
                                " and date_trunc('day',applydate) = ? "+
                                " and upper(description) like ? ";

                if(!BDUtil.isZeroOrNull(journal.getDbDebit()))
                        query = query + " and debit = ?";
                else if(!BDUtil.isZeroOrNull(journal.getDbCredit()))
                        query = query + " and credit = ?";


                final PreparedStatement PS = S.setQuery(query);

                PS.setObject(1, journal.getLgAccountID());
                PS.setObject(2, journal.getDtApplyDate());
                PS.setString(3, "%"+ journal.getStDescription().trim().toUpperCase()+ "%");
                PS.setObject(4, journal.getDbJournalAmount());

                final ResultSet RS = PS.executeQuery();

                if (RS.next()) {
                    String msg = "JURNAL : "+ journal.getStDescription().trim().toUpperCase()+ " | ACCOUNT : "+ journal.getStAccountNo() +
                            " | AMOUNT : "+ journal.getDbJournalAmount()+" | SUDAH PERNAH diinput pada No Bukti : "+ RS.getString("trx_no");

                    throw new RuntimeException(msg);
                }

            } finally {
                S.release();
            }
    }
    
    private DTOList account;
    private DTOList accountCek;

    public DTOList getAccountDivision(String divisionCode, String accountHeader) {
        loadAccountDivision(divisionCode, accountHeader);
        return account;
    }

    public void loadAccountDivision(String divisionCode, String accountNo) {
        try {
            account = null;
            
            if (account == null) {
                account = ListUtil.getDTOListFromQuery(
                        "select * "+
                        " from gl_accounts_division "+
                        " where division_code = ? and account_header like ?",
                        new Object[]{divisionCode, accountNo.substring(0, 5)+"%"},
                        AccountDivisionView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DTOList getAccountByAccountNo(String accountHeader) {
        loadAccountByAccountNo(accountHeader);
        return accountCek;
    }

    public void loadAccountByAccountNo(String accountNo) {
        try {
            accountCek = null;

            if (accountCek == null) {
                accountCek = ListUtil.getDTOListFromQuery(
                        "select * "+
                        " from gl_accounts_division "+
                        " where account_header like ?",
                        new Object[]{accountNo.substring(0, 5)+"%"},
                        AccountDivisionView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void refresh(HttpServletRequest rq)  throws Exception {
        final JournalHeaderView jh = retrieveCashBank(rq);

        populateCashBank(rq,jh);
   }


}
