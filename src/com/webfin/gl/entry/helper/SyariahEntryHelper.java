/***********************************************************************
 * Module:  com.webfin.gl.entry.helper.GLEntryHelper
 * Author:  Denny Mahendra
 * Created: Jul 22, 2005 1:21:28 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.entry.helper;

import com.crux.common.controller.Helper;
import com.crux.ff.model.FlexTableView;
import com.crux.util.*;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.ejb.CurrencyManager;
import com.webfin.gl.ejb.CostCenterManager;
import com.webfin.gl.model.JournalHeaderView;
import com.webfin.gl.model.JournalSyariahView;
import com.webfin.ar.model.ARReceiptClassView;
import com.webfin.gl.model.AccountView;
import com.webfin.gl.model.JournalView;
import com.webfin.gl.model.RKAPGroupView;

import javax.servlet.http.HttpServletRequest;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.util.Date;

public class SyariahEntryHelper extends Helper {

   private final static transient LogManager logger = LogManager.getInstance(SyariahEntryHelper.class);
   private boolean edit = false;
   private DTOList akunSyariah;
   private DTOList reportRKAP;

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

      jh.setStCurrencyCode(CurrencyManager.getInstance().getMasterCurrency());
      jh.setDbCurrencyRate(BDUtil.one);

      jh.markNew();

      jh.setStRefTrxType("NERACA");
      jh.setDtCreateDate(new Date());

      final DTOList syariah = getAkunSyariah("NS");
      
      for(int i=0; i<syariah.size(); i++){
          AccountView jurnal = (AccountView) syariah.get(i);
          
          final JournalSyariahView jv = new JournalSyariahView();

          jv.markNew();

          details.add(jv);
          
          jv.setLgAccountID(jurnal.getLgAccountID());
          jv.setStAccountNo(jurnal.getStAccountNo());
          jv.setStDescription(jurnal.getStDescription());
          jv.setDbEnteredDebit(BDUtil.zero);
          jv.setDbEnteredCredit(BDUtil.zero);
          jv.setDbCredit(BDUtil.zero);
          jv.setDbDebit(BDUtil.zero);
          jv.setStRefTrxType("NERACA");
      }
      
      jh.setDetails(details);

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
      
      //final DTOList method = getRemoteGeneralLedger().getMethodCodeLOV();
      //javax.swing.JOptionPane.showMessageDialog(null,"tes = "+ gl_acct_id,"Error",javax.swing.JOptionPane.CLOSED_OPTION);
      
      rq.setAttribute("CBJT", journalTypesCombo);
      rq.setAttribute("CCYCB", ccy);
      rq.setAttribute("JH",jh);
      rq.setAttribute("CC",costcenter);
      rq.setAttribute("METHOD2",method2);
      rq.setAttribute("GL_ACCT_ID",gl_acct_id);
   
      
   }

   public void edit(HttpServletRequest rq)  throws Exception {
      setEdit(true);
      
      final JournalHeaderView jh = view(rq);

      jh.markUpdateO();

      final DTOList details = jh.getDetails();

      for (int i = 0; i < details.size(); i++) {
         JournalSyariahView jv = (JournalSyariahView) details.get(i);
         
         final AccountView account = getRemoteGeneralLedger().getAccountByAccountID(String.valueOf(jv.getLgAccountID()));
         
         jv.setStKet(account.getStDescription());
         
         jv.setReadOnly(true);
         
         jv.markUpdate();
      }
      
      logger.logDebug("++++++++++ EDIT ++++++++++++++");
      

   }

   public JournalHeaderView view(HttpServletRequest rq)  throws Exception {
      final String trxhdrid = getString(rq.getParameter("trxhdrid"));

      final DTOList details = getRemoteGeneralLedger().getSyariahJournalEntry(trxhdrid);
      

      if (details.size()>0) {
         final JournalHeaderView jh = new JournalHeaderView();

         final JournalSyariahView jv = (JournalSyariahView)details.get(0);
         
         if(edit)
         	if(jv.getStApproved()!=null)
         		if(jv.getStApproved().equalsIgnoreCase("Y"))
         			throw new RuntimeException("Can't Edit Approved Data");
         
         jh.setStTransactionNo(jv.getStTransactionNo());
         jh.setDtCreateDate(jv.getDtCreateDate());
         jh.setDtApplyDate(jv.getDtApplyDate());
         jh.setStCurrencyCode(jv.getStCurrencyCode());
         jh.setDbCurrencyRate(jv.getDbCurrencyRate());
         jh.setStJournalCode(jv.getStJournalCode());
         jh.setStCostCenter(jv.getStTransactionNo().substring(5,7));
         jh.setStMethodCode(jv.getStTransactionNo().substring(0,1));
	 jh.setReadOnly(true);
         jh.setLgFiscalYear(jv.getLgFiscalYear());
         jh.setLgPeriodNo(jv.getLgPeriodNo());
         jh.setStRefTrxType(jv.getStRefTrxType());
		          
         jh.setDetails(details);
         
         final DTOList details2 = jh.getDetails();
         
         for (int i = 0; i < details2.size(); i++) {
         JournalSyariahView jv2 = (JournalSyariahView) details2.get(i);
         
         final AccountView account = getRemoteGeneralLedger().getAccountByAccountID(String.valueOf(jv2.getLgAccountID()));
         
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
      
      getRemoteGeneralLedger().saveJournalSyariah(jh,details);
      //getRemoteGeneralLedger().saveJournalEntry3(jh,details);
   }
   
   public void approve(HttpServletRequest rq)  throws Exception {
      final JournalHeaderView jh = retrieve(rq);

      jh.markUpdateO();
      
      jh.setStApproved("Y");

      final DTOList details = jh.getDetails();
      
      for(int i=0; i< details.size();i++){
          JournalSyariahView det = (JournalSyariahView) details.get(i);
          
          det.markUpdate();
          
          det.setStApproved("Y");
      }
      
      getRemoteGeneralLedger().saveJournalSyariah(jh,details);
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

      final JournalSyariahView jv = new JournalSyariahView();

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

      //jh.setStTransactionNo(getString(rq.getParameter("trxno")));

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
      jh.setDtApplyDate(getDate(rq.getParameter("applydate")));
      jh.setLgFiscalYear(getLong(rq.getParameter("fiscal")));
      jh.setLgPeriodNo(getLong(rq.getParameter("periodnum")));

      final DTOList details = jh.getDetails();

      for (int i = 0; i < details.size(); i++) {
         JournalSyariahView jv = (JournalSyariahView) details.get(i);

         jv.setStTransactionNo(jh.getStTransactionNo());
         jv.setDtApplyDate(jh.getDtApplyDate());
         jv.setStCurrencyCode(jh.getStCurrencyCode());
         jv.setDbCurrencyRate(jh.getDbCurrencyRate());
         jv.setStJournalCode(jh.getStJournalCode());
         jv.setStKet(getString(rq.getParameter("keterangan"+i)));
         jv.setLgFiscalYear(jh.getLgFiscalYear());
         jv.setLgPeriodNo(jh.getLgPeriodNo());

         //if(rq.getParameter("acid"+i)!=null) 
             
         //jv.setLgAccountID(getLong(rq.getParameter("acid"+i)));
         
         jv.setStAccountNo(getString(rq.getParameter("acno"+i)));
         jv.setStDescription(getString(rq.getParameter("desc"+i)));
         jv.setDbEnteredCredit(getNum(rq.getParameter("credit"+i)));
         jv.setDbEnteredDebit(getNum(rq.getParameter("debit"+i)));
         jv.setDtApplyDate(getDate(rq.getParameter("trxdate"+i)));
         jv.setStPolicyID(getString(rq.getParameter("polid"+i)));
         jv.setStPolicyNo(getString(rq.getParameter("polno"+i)));

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

      final JournalSyariahView jv = new JournalSyariahView();

      jv.markNew();

      details.add(jv);

      jh.setDetails(details);

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
         
      rq.setAttribute("CBJT", journalTypesCombo);
      rq.setAttribute("CCYCB", ccy);
      rq.setAttribute("JH",jh);
      rq.setAttribute("CC",costcenter);
      rq.setAttribute("METHOD2",method2);
      rq.setAttribute("GL_ACCT_ID",gl_acct_id);
   
      
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

      final DTOList details = jh.getDetails();

      for (int i = 0; i < details.size(); i++) {
         JournalSyariahView jv = (JournalSyariahView) details.get(i);

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

         jv.reCalculate();
      }

      //jh.reCalculate();

      return jh;
   }
   
    public void addlineCashBank(HttpServletRequest rq)  throws Exception {
      final JournalHeaderView jh = retrieveCashBank(rq);

      final DTOList details = jh.getDetails();

      final JournalSyariahView jv = new JournalSyariahView();

      jv.setDbEnteredDebit(BDUtil.zero);
      jv.setDbEnteredCredit(BDUtil.zero);
      jv.setDbCredit(BDUtil.zero);
      jv.setDbDebit(BDUtil.zero);
      
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

      final String codeMethod = "";

      getRemoteGeneralLedger().saveEditCashBank(jh,details);
   }
    
    public JournalHeaderView clickApprove(HttpServletRequest rq)  throws Exception {
      final String trxhdrid = getString(rq.getParameter("trxhdrid"));

      final DTOList details = getRemoteGeneralLedger().getSyariahJournalEntry(trxhdrid);
      
      if (details.size()>0) {
         final JournalHeaderView jh = new JournalHeaderView();

         final JournalSyariahView jv = (JournalSyariahView)details.get(0);

         if(jv.getStApproved()!=null)
                if(jv.getStApproved().equalsIgnoreCase("Y"))
                        throw new RuntimeException("Can't Edit Approved Data");
         
         jh.setApprovedMode(true);
         
         jh.setStTransactionNo(jv.getStTransactionNo());
         jh.setDtCreateDate(jv.getDtCreateDate());
         jh.setDtApplyDate(jv.getDtApplyDate());
         jh.setStCurrencyCode(jv.getStCurrencyCode());
         jh.setDbCurrencyRate(jv.getDbCurrencyRate());
         jh.setStJournalCode(jv.getStJournalCode());
         jh.setStCostCenter(jv.getStTransactionNo().substring(5,7));
         jh.setStMethodCode(jv.getStTransactionNo().substring(0,1));
         jh.setLgFiscalYear(jv.getLgFiscalYear());
         jh.setLgPeriodNo(jv.getLgPeriodNo());
         jh.setStRefTrxType(jv.getStRefTrxType());
         
	 jh.setReadOnly(true);
		          
         jh.setDetails(details);

         populate(rq,jh);

         return jh;
      } else
         throw new Exception("transaction number not found");
   }

    public DTOList getAkunSyariah(String type)
    {
        loadAkunSyariah(type);
        return akunSyariah;
    }

    public void setAkunSyariah(DTOList akunSyariah)
    {
        this.akunSyariah = akunSyariah;
    }
    
    private void loadAkunSyariah(String type) {
        try {
            if (akunSyariah == null)
                akunSyariah = ListUtil.getDTOListFromQuery(
                        "select * from gl_accounts where enabled = 'N' and cc_code = '00' and acctype = '"+ type +"' order by accountno",
                        AccountView.class
                        );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }
    
    public void addLabaRugi(HttpServletRequest rq)  throws Exception {
      final JournalHeaderView jh = new JournalHeaderView();

      final DTOList details = new DTOList();

      jh.setStCurrencyCode(CurrencyManager.getInstance().getMasterCurrency());
      jh.setDbCurrencyRate(BDUtil.one);

      jh.markNew();

      jh.setStRefTrxType("LABARUGI");
      jh.setDtCreateDate(new Date());

      final DTOList syariah = getAkunSyariah("LRS");
      
      for(int i=0; i<syariah.size(); i++){
          AccountView jurnal = (AccountView) syariah.get(i);
          
          final JournalSyariahView jv = new JournalSyariahView();

          jv.markNew();

          details.add(jv);
          
          jv.setLgAccountID(jurnal.getLgAccountID());
          jv.setStAccountNo(jurnal.getStAccountNo());
          jv.setStDescription(jurnal.getStDescription());
          jv.setDbEnteredDebit(BDUtil.zero);
          jv.setDbEnteredCredit(BDUtil.zero);
          jv.setDbCredit(BDUtil.zero);
          jv.setDbDebit(BDUtil.zero);
          jv.setStRefTrxType("LABARUGI");
      }
      
      jh.setDetails(details);

      populate(rq,jh);
   }

    public void reverse(HttpServletRequest rq) throws Exception {

        setEdit(false);

        rq.setAttribute("REVERSE", "Y");

        final JournalHeaderView jh = view(rq);

        jh.markUpdateO();

        jh.setStApproved("N");

        final DTOList details = jh.getDetails();

        for (int i = 0; i < details.size(); i++) {
            JournalSyariahView jv = (JournalSyariahView) details.get(i);

            final AccountView account = getRemoteGeneralLedger().getAccountByAccountID(String.valueOf(jv.getLgAccountID()));

            jv.setStKet(account.getStDescription());

            jv.setReadOnly(true);

            jv.markUpdate();

            jv.setStApproved("N");
        }


    }

    public void doReverse(HttpServletRequest rq) throws Exception {

        final JournalHeaderView jh = retrieve(rq);

        //final DTOList details = jh.getDetails();

        getRemoteGeneralLedger().reverseJournalSyariah(jh.getStTransactionNo());

    }

    public void addRKAP(HttpServletRequest rq)  throws Exception {
      final JournalHeaderView jh = new JournalHeaderView();

      final DTOList details = new DTOList();

      jh.setStCurrencyCode(CurrencyManager.getInstance().getMasterCurrency());
      jh.setDbCurrencyRate(BDUtil.one);

      jh.markNew();

      jh.setStRefTrxType("RKAPSYA");
      jh.setDtCreateDate(new Date());

      final DTOList syariah = getAkunSyariah("RKAPS");

      for(int i=0; i<syariah.size(); i++){
          AccountView jurnal = (AccountView) syariah.get(i);

          final JournalSyariahView jv = new JournalSyariahView();

          jv.markNew();

          details.add(jv);

          jv.setLgAccountID(jurnal.getLgAccountID());
          jv.setStAccountNo(jurnal.getStAccountNo());
          jv.setStDescription(jurnal.getStDescription());
          jv.setDbEnteredDebit(BDUtil.zero);
          jv.setDbEnteredCredit(BDUtil.zero);
          jv.setDbCredit(BDUtil.zero);
          jv.setDbDebit(BDUtil.zero);
          jv.setStRefTrxType("RKAPSYA");
      }

      jh.setDetails(details);

      populate(rq,jh);
   }

    

    public void addReportRKAP(HttpServletRequest rq) throws Exception {
        final JournalHeaderView jh = new JournalHeaderView();

        final DTOList details = new DTOList();

        jh.markNew();

        //jh.setStRefTrxType("REPORTRKAP");

        final DTOList syariah = getReportRKAP();

        for (int i = 0; i < syariah.size(); i++) {
            FlexTableView jurnal = (FlexTableView) syariah.get(i);

            final RKAPGroupView jv = new RKAPGroupView();

            jv.markNew();

            details.add(jv);

            jv.setStRKAPGroupID(jurnal.getStReference9());
            jv.setStRKAPDescription(jurnal.getStReference3());
            jv.setDbKonvensional(BDUtil.zero);
            jv.setDbSyariah(BDUtil.zero);
        }

        jh.setDetails(details);

        populate(rq, jh);
    }

    public void editReportRKAP(HttpServletRequest rq) throws Exception {
        setEdit(true);

        final JournalHeaderView jh = viewReportRKAP(rq);

        jh.markUpdateO();

        final DTOList details = jh.getDetails();

        for (int i = 0; i < details.size(); i++) {
            RKAPGroupView jv = (RKAPGroupView) details.get(i);

            //final AccountView account = getRemoteGeneralLedger().getAccountByAccountID(String.valueOf(jv.getLgAccountID()));

            //jv.setStKet(account.getStDescription());

            //jv.setReadOnly(true);

            jv.markUpdate();
        }

        logger.logDebug("++++++++++ EDIT ++++++++++++++");


    }

    public JournalHeaderView viewReportRKAP(HttpServletRequest rq) throws Exception {
        final String trxhdrid = getString(rq.getParameter("trxhdrid"));

        final DTOList details = getRemoteGeneralLedger().getRKAPEntry(trxhdrid);

        if (details.size() > 0) {
            final JournalHeaderView jh = new JournalHeaderView();

            final RKAPGroupView jv = (RKAPGroupView) details.get(0);

            jh.setStTransactionNo(jv.getStRKAPTransactionNo());
            jh.setStYears(jv.getStYears());
            jh.setReadOnly(true);

            jh.setDetails(details);

            populate(rq, jh);

            return jh;
        } else {
            throw new Exception("transaction number not found");
        }
    }

    public DTOList getReportRKAP() {
        loadReportRKAP();
        return reportRKAP;
    }

    public void setReportRKAP(DTOList reportRKAP) {
        this.reportRKAP = reportRKAP;
    }

    private void loadReportRKAP() {
        try {
            if (reportRKAP == null) {
                reportRKAP = ListUtil.getDTOListFromQuery(
                        "select * from ff_table where active_flag = 'Y' and REF9 is not null and fft_group_id like 'RKAP%' order by ref9,orderseq ",
                        FlexTableView.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void saveReportRKAP(HttpServletRequest rq) throws Exception {
        final JournalHeaderView jh = retrieveRKAP(rq);

        final DTOList details = jh.getDetails();

        //logger.logDebug("save: "+details.size());

        getRemoteGeneralLedger().saveReportRKAP(jh, details);
        //getRemoteGeneralLedger().saveJournalEntry3(jh,details);
    }

    public void editrefreshReportRKAP(HttpServletRequest rq) throws Exception {
        populate(rq, null);
    }

    private JournalHeaderView retrieveRKAP(HttpServletRequest rq) throws Exception {
        final JournalHeaderView jh = (JournalHeaderView) get(rq, "JH");

        jh.setStYears(rq.getParameter("fiscal"));

        final DTOList details = jh.getDetails();

        for (int i = 0; i < details.size(); i++) {
            RKAPGroupView jv = (RKAPGroupView) details.get(i);

            jv.setStRKAPTransactionNo(jh.getStTransactionNo());
            jv.setStYears(jh.getStYears());

            jv.setStRKAPGroupID(getString(rq.getParameter("acno" + i)));
            jv.setStRKAPDescription(getString(rq.getParameter("desc" + i)));
            jv.setDbKonvensional(getNum(rq.getParameter("konvensional" + i)));
            jv.setDbSyariah(getNum(rq.getParameter("syariah" + i)));
            //jv.reCalculate();
        }

        //jh.reCalculate();

        return jh;
    }
    
}
