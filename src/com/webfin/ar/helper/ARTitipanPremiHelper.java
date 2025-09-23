/***********************************************************************
 * Module:  com.webfin.ar.helper.ARReceiptHelper
 * Author:  Denny Mahendra
 * Created: Oct 11, 2005 10:30:00 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.helper;

import com.crux.common.controller.Helper;
import com.crux.util.*;
import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.ar.ejb.AccountReceivableHome;
import com.webfin.ar.filter.ARReceiptFilter;
import com.webfin.ar.filter.ARInvoiceFilter;
import com.webfin.ar.model.ARReceiptView;
import com.webfin.ar.model.ARReceiptLinesView;
import com.webfin.ar.model.ARInvoiceView;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.ejb.CurrencyManager;
import com.crux.web.controller.SessionManager;
import com.webfin.ar.model.*;
import com.webfin.ar.model.ARPaymentMethodView;
import com.crux.pool.DTOPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

public class ARTitipanPremiHelper extends Helper {
	private final static transient LogManager logger = LogManager.getInstance(ARReceiptHelper.class);

   private AccountReceivable getRemoteAccountReceivable() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((AccountReceivableHome) JNDIUtil.getInstance().lookup("AccountReceivableEJB",AccountReceivableHome.class.getName()))
            .create();
   }
 
   private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB",GeneralLedgerHome.class.getName()))
            .create();
   }

   public void list(HttpServletRequest rq)  throws Exception {
      final ARReceiptFilter f = new ARReceiptFilter();

      final DTOList l = getRemoteAccountReceivable().listARReceipts(f);

      rq.setAttribute("LIST",l);
   }

   private void populate(HttpServletRequest rq, ARTitipanPremiView2 titipan) throws Exception {
      if (titipan==null) titipan=(ARTitipanPremiView2) get(rq,"TITIPAN");
      put(rq,"TITIPAN",titipan);
      rq.setAttribute("TITIPAN",titipan);
   }

   private ARTitipanPremiView2 retrieve(HttpServletRequest rq) throws Exception {
      final ARTitipanPremiView2 titipan = (ARTitipanPremiView2)get(rq,"FORM");

      final DTOList details = titipan.getDetails();

      for (int i = 0; i < details.size(); i++) {

         ARTitipanPremiDetailView rl = (ARTitipanPremiDetailView) details.get(i);

		 rl.setStDescription(getString(rq.getParameter("desc"+i)));
		 rl.setStPolicyNo(getString(rq.getParameter("polno"+i)));
		 rl.setDtARTitipanPremiDetail(getDate(rq.getParameter("trxdate"+i)));
		 rl.setDbPremiAmount(getNum(rq.getParameter("premi"+i)));
      }
		
      return titipan;
   }

   public void searchInvoice(HttpServletRequest rq)  throws Exception {
      final String key = getString(rq.getParameter("key"));
      final String type = getString(rq.getParameter("type"));
      final String ccy = getString(rq.getParameter("ccy"));
      final String cust = getString(rq.getParameter("cust"));
      final String arsid = getString(rq.getParameter("arsid"));
      final String cc_code = getString(rq.getParameter("cc_code"));
      //String cc_code = SessionManager.getInstance().getSession().getStBranch();

      final ARInvoiceFilter f = new ARInvoiceFilter();

      f.key = key;

      /*return
         ListUtil.getDTOListFromQuery(
                 "select * from ar_invoice where invoice_no like ?",
                 new Object [] { "%"+f.key+"%"},
                 ARInvoiceView.class
         );


      final DTOList l = getRemoteAccountReceivable().searchInvoice(f);*/


      final SQLAssembler sqa = new SQLAssembler();

      sqa.addSelect("*");

      sqa.addQuery("from ar_invoice");
      
      if(cc_code!=null){
      		sqa.addClause(" cc_code like ?");
      		sqa.addPar("%"+cc_code+"%");
      }
      

      if (key!=null){
         sqa.addClause("upper(invoice_no) like ?");
         sqa.addPar("%"+key.toUpperCase()+"%");
      }

      if (type!=null) {
         sqa.addClause("invoice_type = ?");
         sqa.addPar(type);
      }

      if (cust!=null) {
         sqa.addClause("ent_id = ?");
         sqa.addPar(cust);
      }
      
      if(arsid!=null){
	      if(arsid.equalsIgnoreCase("1")){
		  		sqa.addClause(" ar_trx_type_id between ? and ?");
	            sqa.addPar("5");
	            sqa.addPar("7");
		  }else if(arsid.equalsIgnoreCase("2")){
		  		sqa.addClause(" ar_trx_type_id = ? and refid3 is null");
	            sqa.addPar("11");
	            //sqa.addPar("%POL%");
		  }else if(arsid.equalsIgnoreCase("3")){
		  		sqa.addClause(" ar_trx_type_id = ? ");
	            sqa.addPar("11");
		  }else if(arsid.equalsIgnoreCase("5")){
		  	    sqa.addClause(" ar_trx_type_id between ? and ?");
	            sqa.addPar("5");
	            sqa.addPar("7");
		  }else if(arsid.equalsIgnoreCase("8")){
		  		sqa.addClause(" ar_trx_type_id = ? and refid2 like ?");
	            sqa.addPar("11");
	            sqa.addPar("TAX%");
		  }
		  else if(arsid.equalsIgnoreCase("10")){
		  		sqa.addClause(" ar_trx_type_id = ?");
	            sqa.addPar("12");
		  }
		  else if(arsid.equalsIgnoreCase("11")){
		  		sqa.addClause(" ar_trx_type_id between ? and ? and invoice_type = 'AP'");
	            sqa.addPar("5");
	            sqa.addPar("7");
		  }
		  else if(arsid.equalsIgnoreCase("13")){
		  		sqa.addClause(" ar_trx_type_id = ?");
	            sqa.addPar("10");
		  }
      }
      

      final DTOList l = sqa.getList(ARInvoiceView.class);

      rq.setAttribute("LIST",l);
   }
   
   public void searchPL(HttpServletRequest rq)  throws Exception {
      final String key = getString(rq.getParameter("key"));
      final String type = getString(rq.getParameter("type"));
      final String ccy = getString(rq.getParameter("ccy"));
      final String cust = getString(rq.getParameter("cust"));
      final String arsid = getString(rq.getParameter("arsid"));
      final String cc_code = getString(rq.getParameter("cc_code"));
      //String cc_code = SessionManager.getInstance().getSession().getStBranch();

      final ARInvoiceFilter f = new ARInvoiceFilter();

      f.key = key;

      /*return
         ListUtil.getDTOListFromQuery(
                 "select * from ar_invoice where invoice_no like ?",
                 new Object [] { "%"+f.key+"%"},
                 ARInvoiceView.class
         );


      final DTOList l = getRemoteAccountReceivable().searchInvoice(f);*/


      final SQLAssembler sqa = new SQLAssembler();

      sqa.addSelect("*");

      sqa.addQuery("from ar_invoice");
      
      if(cc_code!=null){
      		sqa.addClause(" cc_code like ?");
      		sqa.addPar("%"+cc_code+"%");
      }
      

      if (key!=null){
         sqa.addClause("upper(invoice_no) like ?");
         sqa.addPar("%"+key.toUpperCase()+"%");
      }

      if (type!=null) {
         sqa.addClause("invoice_type = ?");
         sqa.addPar(type);
      }

      if (cust!=null) {
         sqa.addClause("ent_id = ?");
         sqa.addPar(cust);
      }
      
      sqa.addClause("refd0 is null");
      sqa.addClause("refid0 like 'PREMI%'");

      final DTOList l = sqa.getList(ARInvoiceView.class);

      rq.setAttribute("LIST",l);
   }
   
   public void searchSuratHutang(HttpServletRequest rq)  throws Exception {
      final String key = getString(rq.getParameter("key"));
      final String type = getString(rq.getParameter("type"));
      final String ccy = getString(rq.getParameter("ccy"));
      final String cust = getString(rq.getParameter("cust"));
      final String arsid = getString(rq.getParameter("arsid"));
      final String cc_code = getString(rq.getParameter("cc_code"));
      //String cc_code = SessionManager.getInstance().getSession().getStBranch();

      final ARInvoiceFilter f = new ARInvoiceFilter();

      f.key = key;

      /*return
         ListUtil.getDTOListFromQuery(
                 "select * from ar_invoice where invoice_no like ?",
                 new Object [] { "%"+f.key+"%"},
                 ARInvoiceView.class
         );


      final DTOList l = getRemoteAccountReceivable().searchInvoice(f);*/


      final SQLAssembler sqa = new SQLAssembler();

      sqa.addSelect(" distinct a.no_surat_hutang,b.* ");
      sqa.addQuery(
              "   from " +
              "      ar_invoice a" +
              "         left join ent_master b on b.ent_id = a.ent_id");
      sqa.addClause(" a.no_surat_hutang is not null");
      sqa.addOrder(" a.no_surat_hutang asc");
      
      if(cc_code!=null){
      		sqa.addClause(" a.cc_code like ?");
      		sqa.addPar("%"+cc_code+"%");
      }
      

      if (key!=null){
         sqa.addClause("upper(a.no_surat_hutang) like ?");
         sqa.addPar("%"+f.key.toUpperCase()+"%");
      }

      if (type!=null) {
         sqa.addClause("a.invoice_type = ?");
         sqa.addPar(type);
      }

      if (cust!=null) {
         sqa.addClause("a.ent_id = ?");
         sqa.addPar(cust);
      }
      
      if(arsid!=null){
	      		if(arsid.equalsIgnoreCase("1")){
		  		sqa.addClause(" ar_trx_type_id between ? and ?");
	            sqa.addPar("5");
	            sqa.addPar("7");
		  }else if(arsid.equalsIgnoreCase("2")){
		  		sqa.addClause(" ar_trx_type_id = ? ");
	            sqa.addPar("11");
		  }else if(arsid.equalsIgnoreCase("3")){
		  		sqa.addClause(" ar_trx_type_id = ? ");
	            sqa.addPar("11");
		  }else if(arsid.equalsIgnoreCase("5")){
		  	    sqa.addClause(" ar_trx_type_id between ? and ?");
	            sqa.addPar("5");
	            sqa.addPar("7");
		  }else if(arsid.equalsIgnoreCase("8")){
		  		sqa.addClause(" ar_trx_type_id = ? ");
	            sqa.addPar("11");
		  }
      }
      

      final DTOList l = sqa.getList(ARInvoiceView.class);

      rq.setAttribute("LIST",l);
      //rq.setAttribute("CC_CODE",cc_code);
   }
   
  
   public void addline(HttpServletRequest rq)  throws Exception {
      final ARTitipanPremiView2 titipan = retrieve(rq);

      final DTOList details = titipan.getDetails();

      final ARTitipanPremiDetailView det = new ARTitipanPremiDetailView();

      det.markNew();

      details.add(det);

      populate(rq,titipan);
      
      throw new RuntimeException("cumi");
      
   }
   

   
  // private static HashSet formList = null;

   public void printReceipt(HttpServletRequest rq,HttpServletResponse rp)  throws Exception {

      //loadFormList(rq);

      final String receiptid = rq.getParameter("receiptid");
      
      final ARReceiptView receipt = getRemoteAccountReceivable().getARReceipt(receiptid);

      rq.setAttribute("RECEIPT",receipt);

      //final ArrayList plist = new ArrayList();

      String urx=null;

      //logger.logDebug("printPolicy: scanlist:"+plist);

	  /*
      for (int i = 0; i < plist.size(); i++) {
         String s = (String) plist.get(i);

         if (formList.contains("pol"+s+".fop.jsp")) {
            urx = "/pages/ar/report/pol"+s+".fop";
            break;
         }
      }*/
      
      urx = "/pages/ar/report/penerimaanPremi.fop";

      if (urx==null) throw new RuntimeException("Unable to find suitable print form");

      //if (nom!=null)
         //getRemoteInsurance().registerPrintSerial(policy, nom, urx);


      logger.logDebug("printPolicy: forwarding to ########## "+urx);

      rq.getRequestDispatcher(urx).forward(rq,rp);
   }

/*
   private void loadFormList(HttpServletRequest rq) {
      if (formList==null || true) {
         final String[] filez = new File(rq.getSession().getServletContext().getRealPath("/pages/ar/report")).list();

         formList = new HashSet();

         for (int i = 0; i < filez.length; i++) {
            String s = filez[i];

            formList.add(s);
         }
      }
   }*/
   
   public void add(HttpServletRequest rq)  throws Exception {
      final ARTitipanPremiView2 titipan = new ARTitipanPremiView2();

      final DTOList details = new DTOList();

      final ARTitipanPremiDetailView titipanDetails = new ARTitipanPremiDetailView();

      titipanDetails.markNew();

      details.add(titipanDetails);

      titipan.setDetails(details);

      titipan.markNew();

      //titipan.setDtARTitipanPremi(new Date());

      populate(rq,titipan);
   }

}
