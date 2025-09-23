/***********************************************************************
 * Module:  com.webfin.gl.entry.helper.GLEntryHelper
 * Author:  Denny Mahendra
 * Created: Jul 22, 2005 1:21:28 PM
 * Purpose:
 ***********************************************************************/

package com.webfin.gl.entry.helper;

import com.crux.common.controller.Helper;
import com.crux.util.*;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.ejb.CurrencyManager;
import com.webfin.gl.model.JournalHeaderView;
import com.webfin.gl.model.JournalView;
import com.webfin.gl.model.AccountView;
import com.webfin.insurance.ejb.UserManager;

import javax.servlet.http.HttpServletRequest;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.util.Date;

public class PBEntryHelper extends Helper
{
    
    private final static transient LogManager logger = LogManager.getInstance(PBEntryHelper.class);
    private boolean edit = false;
    
    
    public boolean getEdit()
    {
        return edit;
    }
    
    public void setEdit(boolean edit)
    {
        this.edit = edit;
    }
    
    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException
    {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB",GeneralLedgerHome.class.getName()))
        .create();
    }
    
    public void list(HttpServletRequest rq)  throws Exception
    {
        final DTOList list = getRemoteGeneralLedger().listJournalEntry();
        
        rq.setAttribute("LIST",list);
    }
    
    public void add(HttpServletRequest rq)  throws Exception
    {
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
    
    public void editrefresh(HttpServletRequest rq)  throws Exception
    {
        populate(rq,null);
    }
    
    private void populate(HttpServletRequest rq, JournalHeaderView jh) throws Exception
    {
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
    
    public void edit(HttpServletRequest rq)  throws Exception
    {
        setEdit(true);
        final JournalHeaderView jh = view(rq);
        
        jh.markUpdateO();
        
        final DTOList details = jh.getDetails();
        
        for (int i = 0; i < details.size(); i++)
        {
            JournalView jv = (JournalView) details.get(i);
            
            final AccountView account = getRemoteGeneralLedger().getAccountByAccountID(String.valueOf(jv.getLgAccountID()));
            
            jv.setStKet(account.getStDescription());
            
            jv.setReadOnly(true);
            
            jv.markUpdate();
        }
        
        logger.logDebug("++++++++++ EDIT ++++++++++++++");
        
        
    }
    
    public JournalHeaderView view(HttpServletRequest rq)  throws Exception
    {
        final String trxhdrid = getString(rq.getParameter("trxhdrid"));
        
        final DTOList details = getRemoteGeneralLedger().getJournalEntry(trxhdrid);
        
        
        if (details.size()>0)
        {
            final JournalHeaderView jh = new JournalHeaderView();
            
            final JournalView jv = (JournalView)details.get(0);
            
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
            
            jh.setDetails(details);
            
            final DTOList details2 = jh.getDetails();
            
            for (int i = 0; i < details2.size(); i++)
            {
                JournalView jv2 = (JournalView) details2.get(i);
                
                final AccountView account = getRemoteGeneralLedger().getAccountByAccountID(String.valueOf(jv2.getLgAccountID()));
                
                jv2.setStKet(account.getStDescription());
            }
            
            jh.setDetails(details2);
            
            populate(rq,jh);
            
            return jh;
        }
        else
            throw new Exception("transaction number not found");
    }
    
    public void save(HttpServletRequest rq)  throws Exception
    {
        final JournalHeaderView jh = retrieve(rq);
        
        final DTOList details = jh.getDetails();
        
        //logger.logDebug("save: "+details);
        
        getRemoteGeneralLedger().saveJournalEntry4(jh,details);
        //getRemoteGeneralLedger().saveJournalEntry3(jh,details);
    }
    
    public void approve(HttpServletRequest rq)  throws Exception
    {
        final JournalHeaderView jh = retrieve(rq);
        
        final DTOList details = jh.getDetails();
        
        //logger.logDebug("save: "+details);
        
        getRemoteGeneralLedger().saveJournalEntry5(jh,details);
        //getRemoteGeneralLedger().saveJournalEntry3(jh,details);
    }

    public void saveCBGL(HttpServletRequest rq)  throws Exception
    {
        final JournalHeaderView jh = retrieveCashBank(rq);
        
        final DTOList details = jh.getDetails();
        
        final String codeMethod = "";
        
        getRemoteGeneralLedger().saveJournalEntryPengembanganBisnis(jh,details);
    }
    
    public void detailedit(HttpServletRequest rq)  throws Exception
    {
    }
    
    public void detailsave(HttpServletRequest rq)  throws Exception
    {
    }
    
    public void addline(HttpServletRequest rq)  throws Exception
    {
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
    
    
    public void delline(HttpServletRequest rq)  throws Exception
    {
        final JournalHeaderView jh = retrieve(rq);
        
        jh.getDetails().delete(getLong(rq.getParameter("rn")).intValue());
        
        populate(rq,jh);
    }
    
    private JournalHeaderView retrieve(HttpServletRequest rq) throws Exception
    {
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
        
        final DTOList details = jh.getDetails();
        
        for (int i = 0; i < details.size(); i++)
        {
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
            
            jv.reCalculate();
        }
        
        //jh.reCalculate();
        
        return jh;
    }
    
    public void changeCCy(HttpServletRequest rq)  throws Exception
    {
        final JournalHeaderView jh = retrieve(rq);
        
        populate(rq,jh);
    }
    
   /*
   public void changeMethod(HttpServletRequest rq)  throws Exception {
      final JournalHeaderView jh = retrieve(rq);
    
    
    
      populate(rq,jh);
   }*/
    
    public void addCashBank(HttpServletRequest rq)  throws Exception
    {
        final JournalHeaderView jh = new JournalHeaderView();
        
        final DTOList details = new DTOList();
        
        final JournalView jv = new JournalView();
        
        jv.markNew();
        
        details.add(jv);
        
        jh.setDetails(details);
        
        jh.setStCostCenter(UserManager.getInstance().getUser().getStBranch());
        jh.setStMethodCode("C");
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
    
    private void populateCashBank(HttpServletRequest rq, JournalHeaderView jh) throws Exception
    {
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
    
    private JournalHeaderView retrieveCashBank(HttpServletRequest rq) throws Exception
    {
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
        
        for (int i = 0; i < details.size(); i++)
        {
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
            
            jv.reCalculate();
        }
        
        //jh.reCalculate();
        
        return jh;
    }
    
    public void addlineCashBank(HttpServletRequest rq)  throws Exception
    {
        final JournalHeaderView jh = retrieveCashBank(rq);
        
        final DTOList details = jh.getDetails();
        
        final JournalView jv = new JournalView();
        
        jv.setDbEnteredDebit(BDUtil.zero);
        jv.setDbEnteredCredit(BDUtil.zero);
        jv.setDbCredit(BDUtil.zero);
        jv.setDbDebit(BDUtil.zero);
        
        jv.markNew();
        
        details.add(jv);
        
        populateCashBank(rq,jh);
        
    }
    
    public void dellineCashBank(HttpServletRequest rq)  throws Exception
    {
        final JournalHeaderView jh = retrieveCashBank(rq);
        
        jh.getDetails().delete(getLong(rq.getParameter("rn")).intValue());
        
        populateCashBank(rq,jh);
    }
    
    
    public void editrefreshCashBank(HttpServletRequest rq)  throws Exception
    {
        populateCashBank(rq,null);
    }
    
    public void saveEditCBGL(HttpServletRequest rq)  throws Exception
    {
        final JournalHeaderView jh = retrieveCashBank(rq);
        
        final DTOList details = jh.getDetails();
        
        final String codeMethod = "";
        
        getRemoteGeneralLedger().saveEditPengembanganBisnis(jh,details);
    }
}
