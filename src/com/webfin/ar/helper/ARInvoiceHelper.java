/***********************************************************************
 * Module:  com.webfin.ar.helper.ARInvoiceHelper
 * Author:  Denny Mahendra
 * Created: Oct 23, 2005 2:53:38 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.helper;

import com.crux.common.controller.Helper;
import com.crux.common.filter.FOPResponseWrapper;
import com.crux.common.filter.LanguageFilter;
import com.crux.lov.LOVManager;
import com.crux.util.JNDIUtil;
import com.crux.util.DTOList;
import com.crux.util.Tools;
import com.crux.util.LOV;
import com.crux.util.LogManager;
import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.ar.ejb.AccountReceivableHome;
import com.webfin.ar.filter.ARInvoiceFilter;
import com.webfin.ar.model.ARInvestmentIzinDepositoView;
import com.webfin.ar.model.ARInvestmentIzinPencairanView;
import com.webfin.ar.model.ARInvoiceView;
import com.webfin.ar.model.ARInvoiceDetailView;
import com.webfin.ar.model.ARRequestFee;
import com.webfin.gl.ejb.CurrencyManager;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.insurance.model.InsurancePolicyInwardView;
import java.io.File;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletResponse;

public class ARInvoiceHelper extends Helper {
    
    private final static transient LogManager logger = LogManager.getInstance(ARInvoiceHelper.class);


    private String ref1;
    private HashMap refPropMap;

   private AccountReceivable getRemoteAccountReceivable() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((AccountReceivableHome) JNDIUtil.getInstance().lookup("AccountReceivableEJB",AccountReceivableHome.class.getName()))
            .create();
   }

   private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB",GeneralLedgerHome.class.getName()))
            .create();
   }

   public void list(HttpServletRequest rq)  throws Exception {
      final ARInvoiceFilter f = new ARInvoiceFilter();
      final DTOList list = getRemoteAccountReceivable().listInvoices(f);

      rq.setAttribute("LIST",list);
   }

   public void create(HttpServletRequest rq)  throws Exception {
      final ARInvoiceView invoice = new ARInvoiceView();
      invoice.setDetails(new DTOList());
      invoice.markNew();
      put(rq,"INVOICE",invoice);

      populate(rq);
   }

   public void edit(HttpServletRequest rq)  throws Exception {
      final ARInvoiceView invoice = view(rq);

      invoice.markUpdate();
   }

   private void populate(HttpServletRequest rq) throws Exception {
      final ARInvoiceView iv = (ARInvoiceView)get(rq,"INVOICE");


      rq.setAttribute("INVOICE",iv);

      final LOV lovccy = getRemoteGeneralLedger().getCurrencyCodeLOV();

      rq.setAttribute("CCY",lovccy);

   }

   public ARInvoiceView view(HttpServletRequest rq)  throws Exception {
      final String invoiceID = getString(rq.getParameter("invoiceid"));
      final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(invoiceID);

      put(rq,"INVOICE",invoice);

      populate(rq);

      return invoice;
   }

   public void save(HttpServletRequest rq)  throws Exception {
      final ARInvoiceView iv = retrieve(rq);

      populate(rq);

      getRemoteAccountReceivable().save(iv);
   }


   public void delItem(HttpServletRequest rq)  throws Exception {
      final ARInvoiceView iv = retrieve(rq);

      final DTOList details = iv.getDetails();

      final int delIndex = getNum(rq.getParameter("delindex")).intValue();

      details.delete(delIndex);

      populate(rq);
   }

   public void addItem(HttpServletRequest rq)  throws Exception {
      final ARInvoiceView iv = retrieve(rq);

      final DTOList details = iv.getDetails();

      final ARInvoiceDetailView det = new ARInvoiceDetailView();
      det.markNew();

      details.add(det);

      populate(rq);
   }

   private ARInvoiceView retrieve(HttpServletRequest rq) throws Exception {
      final ARInvoiceView iv = (ARInvoiceView)get(rq,"INVOICE");

      iv.setStInvoiceNo(getString(rq.getParameter("invoiceno")));
      iv.setStInvoiceType(getString(rq.getParameter("invoicetype")));
      iv.setDtInvoiceDate(getDate(rq.getParameter("invoicedate")));
      iv.setDtDueDate(getDate(rq.getParameter("duedate")));
      final String ccy = getString(rq.getParameter("ccy"));
      final boolean ccyChanged = !Tools.isEqual(ccy,iv.getStCurrencyCode());
      iv.setStCurrencyCode(ccy);
      iv.setDbCurrencyRate(getNum(rq.getParameter("rate")));
      iv.setDbAmount(getNum(rq.getParameter("amount")));
      iv.setStGLARAccountID(getString(rq.getParameter("acid")));
      iv.setStEntityID(getString(rq.getParameter("custname")));
      iv.setStDescription(getString(rq.getParameter("description")));
      iv.setStPostedFlag(getFlag(rq.getParameter("posted")));

      if (ccyChanged) {
         iv.setDbCurrencyRate(CurrencyManager.getInstance().getRate(iv.getStCurrencyCode(), iv.getDtInvoiceDate()));
      }

      final DTOList details = iv.getDetails();

      for (int i = 0; i < details.size(); i++) {
         ARInvoiceDetailView det = (ARInvoiceDetailView) details.get(i);

         det.setStDescription(getString(rq.getParameter("idesc"+i)));
         det.setDbAmount(getNum(rq.getParameter("iamt"+i)));
         det.setStGLAccountID(getString(rq.getParameter("iacid"+i)));
      }

      iv.recalculate();

      return iv;
   }

   public void refresh(HttpServletRequest rq)  throws Exception {
      retrieve(rq);
      populate(rq);
   }
   
   private static HashSet formList = null;
    
    public void printInvoice(HttpServletRequest rq, HttpServletResponse rp) throws Exception {

        loadFormList(rq);

        final String invoiceid = rq.getParameter("invoiceid");
        final String fontsize = rq.getParameter("fontsize");
        final String authorized = rq.getParameter("authorized");
        String alter = getString(rq.getParameter("alter"));

        final InsurancePolicyInwardView invoice = getRemoteAccountReceivable().getARInvoiceForPrinting(invoiceid);

        rq.setAttribute("INVOICE", invoice);
        rq.setAttribute("FONTSIZE", fontsize);
        rq.setAttribute("AUTHORIZED", authorized);

        final ArrayList plist = new ArrayList();

        if (alter == null) {
            throw new RuntimeException("ALT code not specified");
        }

        if (alter == null) {
            alter = "";
        } else {
            alter = "_" + alter;
        }

        plist.add(invoice.getStARTransactionTypeID() + alter + "_" + invoice.getStClaimStatus() + "_" + invoice.getStRefID0());
        plist.add(invoice.getStARTransactionTypeID() + alter + "_" + invoice.getStRefID0());
        plist.add(invoice.getStARTransactionTypeID() + alter);

        String urx = null;

        logger.logDebug("printClaimInward: scanlist:" + plist);


        for (int i = 0; i < plist.size(); i++) {
            String s = (String) plist.get(i);

            if (formList.contains("inv" + s + ".fop.jsp")) {
                urx = "/pages/ar/report/inv" + s + ".fop";
                break;
            }
        }

        if (urx == null) {
            throw new RuntimeException("Unable to find suitable print form");
        }

        logger.logDebug("print: forwarding to ########## " + urx);

        rq.getRequestDispatcher(urx).forward(rq, rp);
    }
    
    private void loadFormList(HttpServletRequest rq) {
        if (formList==null || true) {
            final String[] filez = new File(rq.getSession().getServletContext().getRealPath("/pages/ar/report")).list();
            
            formList = new HashSet();
            
            for (int i = 0; i < filez.length; i++) {
                String s = filez[i];
                
                formList.add(s);
            }
        }
    }

    public void printRequest(HttpServletRequest rq, HttpServletResponse rp) throws Exception {

        loadFormList(rq);

        final String arreqid = rq.getParameter("arreqid");
        String alter = getString(rq.getParameter("alter"));

        final ARRequestFee request = getRemoteAccountReceivable().getARRequestForPrinting(arreqid, alter);

        ref1 = LOVManager.getInstance().getRef1("REQUEST_PRINTING", alter);

        refPropMap = Tools.getPropMap(ref1);

        String isNotLock = (String) getRefPropMap().get("NO_LOCK_PDF");

        rq.setAttribute("REQUEST", request);

        if (alter.equals("standard")) {
            rq.setAttribute("SAVE_TO_FILE", "Y");
            rq.setAttribute("FILE_NAME", request.getStTransactionNo());
        }

        if (Tools.isYes(isNotLock)) {
            rq.setAttribute("LOCK_PDF", "N");
        }

        final ArrayList plist = new ArrayList();

        if (alter == null) {
            throw new RuntimeException("ALT code not specified");
        }

        if (alter == null) {
            alter = "";
        } else {
            alter = "_" + alter;
        }

        plist.add(alter);

        logger.logDebug("printRequest: scanlist:" + plist);

        String urx = null;

        for (int i = 0; i < plist.size(); i++) {
            String s = (String) plist.get(i);

            if (formList.contains("request" + s + ".fop.jsp")) {
                urx = "/pages/ar/report/request" + s + ".fop";
                break;
            }
        }
        //logger.logDebug("printPolicy: scanlist:"+plist);

        if (urx == null) {
            throw new RuntimeException("Unable to find suitable print form");
        }

        logger.logDebug("print: forwarding to ########## " + urx);

//        FOPResponseWrapper res1 = new FOPResponseWrapper(rp, rq);
//        final LanguageFilter.LanguageFilterResponseWrapper respx = new LanguageFilter.LanguageFilterResponseWrapper(res1);
//
//        respx.setStripHeaders(true);
//        String x = urx + ".jsp";
//
//        RequestDispatcher view = rq.getRequestDispatcher(x);
//
//        view.forward(rq, respx);
//
//        respx.finishResponse();
//
//        res1.save3();

        rq.getRequestDispatcher(urx).forward(rq, rp);
    }



    /**
     * @return the ref1
     */
    public String getRef1() {
        return ref1;
    }

    /**
     * @param ref1 the ref1 to set
     */
    public void setRef1(String ref1) {
        this.ref1 = ref1;
    }

    /**
     * @return the refPropMap
     */
    public HashMap getRefPropMap() {
        return refPropMap;
    }

    /**
     * @param refPropMap the refPropMap to set
     */
    public void setRefPropMap(HashMap refPropMap) {
        this.refPropMap = refPropMap;
    }

    /*
    public void printCashFlow(HttpServletRequest rq, HttpServletResponse rp) throws Exception {

        loadFormList(rq);

        final String arreqid = rq.getParameter("arreqid");
        String alter = getString(rq.getParameter("alter"));

        final ARCashFlowView cashflow = getRemoteAccountReceivable().getARCashFlowForPrinting(arreqid, alter);

        ref1 = LOVManager.getInstance().getRef1("CASHFLOW_PRINTING", alter);

        refPropMap = Tools.getPropMap(ref1);

        rq.setAttribute("CASHFLOW", cashflow);

        String isNotLock = (String) getRefPropMap().get("NO_LOCK_PDF");

        if (alter.equals("standard")) {
            rq.setAttribute("SAVE_TO_FILE", "Y");
            rq.setAttribute("FILE_NAME", null);
        }

        if (Tools.isYes(isNotLock)) {
            rq.setAttribute("LOCK_PDF", "N");
        }

        final ArrayList plist = new ArrayList();

        if (alter == null) {
            throw new RuntimeException("ALT code not specified");
        }

        if (alter == null) {
            alter = "";
        } else {
            alter = "_" + alter;
        }

        plist.add(alter);

        logger.logDebug("printRequest: scanlist:" + plist);

        String urx = null;

        for (int i = 0; i < plist.size(); i++) {
            String s = (String) plist.get(i);

            if (formList.contains("cashflow" + s + ".fop.jsp")) {
                urx = "/pages/ar/report/cashflow" + s + ".fop";
                break;
            }
        }
        //logger.logDebug("printPolicy: scanlist:"+plist);

        if (urx == null) {
            throw new RuntimeException("Unable to find suitable print form");
        }

        logger.logDebug("print: forwarding to ########## " + urx);

        rq.getRequestDispatcher(urx).forward(rq, rp);
    }
    */

    public void printDeposito(HttpServletRequest rq, HttpServletResponse rp) throws Exception {

        loadFormList(rq);

        final String arizinid = rq.getParameter("arizinid");

        String alter = getString(rq.getParameter("alter"));

        final ARInvestmentIzinDepositoView depo = getRemoteGeneralLedger().loadIzinDeposito(arizinid);

        rq.setAttribute("IZINDEPO", depo);

        String docType = alter;

        final ArrayList plist = new ArrayList();

        if (alter == null) {
            throw new RuntimeException("ALT code not specified");
        }

        if (alter == null) {
            alter = "";
        } else {
            alter = "_alt" + alter;
        }

        plist.add(alter);

        String urx = null;

        logger.logDebug("printPolicy: scanlist:" + plist);

        for (int i = 0; i < plist.size(); i++) {
            String s = (String) plist.get(i);

            if (formList.contains("izin" + s + ".fop.jsp")) {
                urx = "/pages/ar/report/izin" + s + ".fop";
                break;
            }
        }

        logger.logDebug("printPolicy: forwarding to ########## " + urx);

        if (urx == null) {
            throw new RuntimeException("Unable to find suitable print form");
        }

//        rq.setAttribute("SAVE_TO_FILE", "Y");
//        rq.setAttribute("FILE_NAME", "SPPD_" + umum.getStSPPDID());
//        if (docType.equals("sppd")) {
//            FOPServlet srv = new FOPServlet();
//            srv.saveFOP(rq, rp, urx);
//        }

        rq.getRequestDispatcher(urx).forward(rq, rp);
    }

    public void printPencairan(HttpServletRequest rq, HttpServletResponse rp) throws Exception {

        loadFormList(rq);

        final String arizincairid = rq.getParameter("arizincairid");

        String alter = getString(rq.getParameter("alter"));

        final ARInvestmentIzinPencairanView cair = getRemoteGeneralLedger().loadIzinPencairan(arizincairid);

        rq.setAttribute("IZINCAIR", cair);

        String docType = alter;

        final ArrayList plist = new ArrayList();

        if (alter == null) {
            throw new RuntimeException("ALT code not specified");
        }

        if (alter == null) {
            alter = "";
        } else {
            alter = "_alt" + alter;
        }

        plist.add(alter);

        String urx = null;

        logger.logDebug("printPolicy: scanlist:" + plist);

        for (int i = 0; i < plist.size(); i++) {
            String s = (String) plist.get(i);

            if (formList.contains("izin" + s + ".fop.jsp")) {
                urx = "/pages/ar/report/izin" + s + ".fop";
                break;
            }
        }

        logger.logDebug("printPolicy: forwarding to ########## " + urx);

        if (urx == null) {
            throw new RuntimeException("Unable to find suitable print form");
        }

//        rq.setAttribute("SAVE_TO_FILE", "Y");
//        rq.setAttribute("FILE_NAME", "SPPD_" + umum.getStSPPDID());
//        if (docType.equals("sppd")) {
//            FOPServlet srv = new FOPServlet();
//            srv.saveFOP(rq, rp, urx);
//        }

        rq.getRequestDispatcher(urx).forward(rq, rp);
    }

}
