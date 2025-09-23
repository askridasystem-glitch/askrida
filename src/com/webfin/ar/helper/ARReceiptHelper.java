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
import com.webfin.ar.model.ARPaymentMethodView;
import com.crux.pool.DTOPool;
import com.webfin.ar.model.ARAPSettlementView;
import com.webfin.gl.model.TitipanPremiView;
import com.webfin.insurance.model.InsurancePolicyView;
import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class ARReceiptHelper extends Helper {
    
    private static HashSet formList = null;
    
    private boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("SETTLR_NAVBR");
     
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
    
    public void create(HttpServletRequest rq)  throws Exception {
        final ARReceiptView rcp = new ARReceiptView();
        
        rcp.setDetails(new DTOList());
        
        rcp.markNew();
        
        put(rq,"RCP",rcp);
        
        populate(rq);
    }
    
    private void populate(HttpServletRequest rq) throws Exception {
        
        final ARReceiptView rcp = (ARReceiptView)get(rq,"RCP");
        
        final LOV lovrc = getRemoteAccountReceivable().getLOVReceiptClass();
        
        final LOV lovpmt = getRemoteAccountReceivable().getLOVPaymentMethod(rcp.getStReceiptClassID());
        
        final LOV lovccy = getRemoteGeneralLedger().getCurrencyCodeLOV();
        
        rq.setAttribute("RCP",rcp);
        rq.setAttribute("RC",lovrc);
        rq.setAttribute("PMT",lovpmt);
        rq.setAttribute("CCY",lovccy);
    }
    
    public void edit(HttpServletRequest rq)  throws Exception {
        final ARReceiptView rcp = view(rq);
        
        rcp.markUpdate();
    }
    
    public ARReceiptView view(HttpServletRequest rq)  throws Exception {
        final String rcid = getString(rq.getParameter("rcid"));
        
        final ARReceiptView rcp = getRemoteAccountReceivable().getARReceipt(rcid);
        
        put(rq,"RCP",rcp);
        
        populate(rq);
        
        return rcp;
    }
    
    public void save(HttpServletRequest rq)  throws Exception {
        final ARReceiptView rcp = retrieve(rq);
        
        rcp.validate();
        
        try {
            getRemoteAccountReceivable().save(rcp);
        } catch (Exception e) {
            populate(rq);
            throw e;
        }
    }
    
    public void chgCurrency(HttpServletRequest rq)  throws Exception {
        retrieve(rq);
        populate(rq);
    }
    
    private ARReceiptView retrieve(HttpServletRequest rq) throws Exception {
        final ARReceiptView rcp = (ARReceiptView)get(rq,"RCP");
        
        rcp.setStReceiptNo(getString(rq.getParameter("receiptno")));
        
        final String ccy = getString(rq.getParameter("ccy"));
        final boolean ccyChanged = !Tools.isEqual(ccy,rcp.getStCurrencyCode());
        rcp.setStCurrencyCode(ccy);
        
        rcp.setDbAmount(getNum(rq.getParameter("amount")));
        final String rc = getString(rq.getParameter("rc"));
        final boolean rcChanged = !Tools.isEqual(rc,rcp.getStReceiptClassID());
        rcp.setStReceiptClassID(rc);
        final String method = getString(rq.getParameter("method"));
        final boolean methodChanged = !Tools.isEqual(method,rcp.getStPaymentMethodID());
        rcp.setStPaymentMethodID(method);
        rcp.setStPostedFlag(getFlag(rq.getParameter("posted")));
        rcp.setDtReceiptDate(getDate(rq.getParameter("rcptdate")));
        rcp.setStCostCenterCode(getString(rq.getParameter("costcenter")));
        rcp.setDbCurrencyRate(getNum(rq.getParameter("rate")));
        
        if (ccyChanged) {
            rcp.setDbCurrencyRate(CurrencyManager.getInstance().getRate(rcp.getStCurrencyCode(), rcp.getDtReceiptDate()));
        }
        
        if (rcp.getReceiptClass()!=null) {
            //rcp.setStInvoiceType(rcp.getReceiptClass().getStInvoiceType());
        }
        
        final DTOList details = rcp.getDetails();
        
        for (int i = 0; i < details.size(); i++) {
            
            ARReceiptLinesView rl = (ARReceiptLinesView) details.get(i);
            
            if (rl.isUnModified())
                rl.markUpdate();
            
            rl.setDbAmount(getNum(rq.getParameter("i_amount"+i)));
        }
        
        final DTOList notes = rcp.getNotes();
        
        for (int i = 0; i < notes.size(); i++) {
            
            ARReceiptLinesView rl = (ARReceiptLinesView) notes.get(i);
            
            if (rl.isUnModified())
                rl.markUpdate();
            
            rl.setDbAmount(getNum(rq.getParameter("n_amount"+i)));
        }
        
        rcp.recalculate();
        
        
        
        return rcp;
    }
    
    public void recalculate(HttpServletRequest rq)  throws Exception {
        retrieve(rq);
        
        populate(rq);
        
    }
    
    public void searchInvoice(HttpServletRequest rq)  throws Exception {
        final String key = getString(rq.getParameter("key"));
        final String type = getString(rq.getParameter("type"));
        final String ccy = getString(rq.getParameter("ccy"));
        final String cust = getString(rq.getParameter("cust"));
        final String arsid = getString(rq.getParameter("arsid"));
        final String cc_code = getString(rq.getParameter("cc_code"));
        //final Date rcpdate = getDate(rq.getParameter("rcpdate"));
        final String journaltype = getString(rq.getParameter("journaltype"));
        boolean useCcCode = true;
        boolean isJournalOffset = false;
        boolean menuReas = arsid.equalsIgnoreCase("9") || arsid.equalsIgnoreCase("11");
        boolean menuKomisi = arsid.equalsIgnoreCase("2")
                || arsid.equalsIgnoreCase("33");
        
        if(journaltype!=null)
            if(journaltype.equalsIgnoreCase("OFFSET"))
                isJournalOffset = true;
        
        if(arsid.equalsIgnoreCase("13") && canNavigateBranch)
            useCcCode = false;
        
        if(arsid.equalsIgnoreCase("14") && canNavigateBranch)
            useCcCode = false;
        
        final ARInvoiceFilter f = new ARInvoiceFilter();
        
        f.key = key;
        
        final SQLAssembler sqa = new SQLAssembler();
        
        sqa.addSelect("a.*");
        
        sqa.addQuery("from ar_invoice a ");

        if(!menuReas){
            if(!isJournalOffset){

                if(key!=null){
                    if(key.contains("8080")){
                        sqa.addClause(" a.cc_code in (?,'80')");
                        sqa.addPar(cc_code);
                    }else{
                        //sqa.addClause(" a.cc_code = ?");
                        sqa.addClause(" a.cc_code in (?,'80')");
                        sqa.addPar(cc_code);
                    }
                }else{
                    sqa.addClause(" a.cc_code = ?");
                    sqa.addPar(cc_code);
                }
                
            }
        }
        
 
        if (key!=null){

            if(arsid!=null){
                ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, arsid);

                if(stl.getStSearchKey()!=null){
                    if(stl.getStSearchKey().trim().equalsIgnoreCase("attr_pol_no = ?")){
                        if(key.length()==16){
                            sqa.addClause("substr(attr_pol_no,1,16) = ? ");
                            sqa.addPar(key.toUpperCase());
                        }else if(key.length()==18){
                            sqa.addClause("attr_pol_no = ? ");
                            sqa.addPar(key.toUpperCase());
                        }else{
                            sqa.addClause("attr_pol_no = ?");
                            sqa.addPar(key.toUpperCase());
                        }
                    }else{
                        sqa.addClause(stl.getStSearchKey());
                        sqa.addPar(((String) key).toUpperCase());
                    }
                }else{
                    sqa.addClause("(a.attr_pol_no like ? or upper(a.refid2) like ? or upper(refa1) like ? or upper(invoice_no) like ?)");
                    final String key1 = "%"+((String) key).toUpperCase()+"%";
                    sqa.addPar(key1);
                    sqa.addPar(key1);
                    sqa.addPar(key1);
                    sqa.addPar(key1);
                }
            }

        }
        
        if (cust!=null) {
            if(!cust.equalsIgnoreCase("null")){
                sqa.addClause("a.ent_id = ?");
                sqa.addPar(cust);
            }
        }
        
        if(cc_code!=null){
            if(!cc_code.equalsIgnoreCase("00")){
                if(arsid.equalsIgnoreCase("13") || arsid.equalsIgnoreCase("14")){
                    sqa.addClause(" a.attr_pol_type_id <> ALL (VALUES (31),(32),(33))");
                    //sqa.addClause(" a.ent_id <> ALL (VALUES (1),(94),(96),(111),(2000),(2001),(2002))");
                    sqa.addClause(" case when a.approved_flag is null then a.ent_id <> ALL (VALUES (1),(94),(96),(111),(2000),(2001),(2002)) else a.ent_id = a.ent_id end ");
                }    
            }  
        }

        //sqa.addClause("a.posted_flag = 'Y'");

//        if (arsid.equalsIgnoreCase("2")) {
//            sqa.addClause("upper(a.invoice_no) not like '%KOMISI%'");
//        }
//
//        if (arsid.equalsIgnoreCase("33")) {
//            sqa.addClause("(upper(a.invoice_no) not like '%KOMISI%' or date_trunc('day',a.mutation_date) < '2018-01-01 00:00:00')");
//        }

        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        
        if(arsid!=null){
            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, arsid);
            
            if(stl.getStParameter1()!=null)
                sqa.addClause(stl.getStParameter1());

            if(stl.getStSearchCondition()!=null)
                sqa.addQuery(stl.getStSearchCondition());
        }

        if (key!=null){
            sqa.setLimit(50);
        }else{
            sqa.setLimit(0);
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
        //final String cc_code = getString(rq.getParameter("cc_code"));
        final String cc_code = SessionManager.getInstance().getSession().getStBranch();
        
        final ARInvoiceFilter f = new ARInvoiceFilter();
        
        f.key = key;
        
        final SQLAssembler sqa = new SQLAssembler();
        
        sqa.addSelect("*");
        
        sqa.addQuery("from ar_invoice");
        
        if(cc_code!=null){
            if(!cc_code.equalsIgnoreCase("00")){
                sqa.addClause(" cc_code = ?");
                sqa.addPar(cc_code);
            }
        }
        
        if (key!=null){
            if(key.length()>15 && key.length()<18){
                sqa.addClause("substr(attr_pol_no,1,16) = ? ");
                sqa.addPar(key.toUpperCase().substring(0, 16));
            }else if(key.length()==18){
                sqa.addClause("attr_pol_no = ? ");
                sqa.addPar(key.toUpperCase());
            }else{
                sqa.addClause("attr_pol_no like ?");
                sqa.addPar("%"+key.toUpperCase()+"%");
            }
        }
        
        if (type!=null) {
            sqa.addClause("invoice_type = ?");
            sqa.addPar(type);
        }
        
        if (cust!=null) {
            sqa.addClause("ent_id = ?");
            sqa.addPar(cust);
        }

        //sqa.addClause("posted_flag = 'Y'");
        sqa.addClause("coalesce(cancel_flag,'') <> 'Y'");
        
        sqa.addClause("substr(refid0,1,5) = 'PREMI'");
        sqa.addClause("amount_settled is null");
        
        sqa.setLimit(10);
        
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
        final String tax_code = getString(rq.getParameter("tax_code"));
        Date start_date = getDate(rq.getParameter("start_date"));
        Date end_date = getDate(rq.getParameter("end_date"));

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date now = cal.getTime();

        String month = DateUtil.getMonth2Digit(now);
        String year = DateUtil.getYear(now);
        
        final ARInvoiceFilter f = new ARInvoiceFilter();
        
        f.key = key;
        f.start_date = start_date;
        
      /*return
         ListUtil.getDTOListFromQuery(
                 "select * from ar_invoice where invoice_no like ?",
                 new Object [] { "%"+f.key+"%"},
                 ARInvoiceView.class
         );
       
       
      final DTOList l = getRemoteAccountReceivable().searchInvoice(f);*/
        
        String date1 = "null";
        String date2 = "null";
        if(start_date!=null){
            if(!start_date.toString().equalsIgnoreCase("null"))
                date1 = "'"+ start_date.toString() +"'";
        }

        if(end_date!=null){
            if(!end_date.toString().equalsIgnoreCase("null"))
                date2 = "'"+ end_date.toString() +"'";
        }
        
        final SQLAssembler sqa = new SQLAssembler();
        
        sqa.addSelect(" distinct a.no_surat_hutang,b.ent_name as refa0,count(a.no_surat_hutang) as refa1, round(sum(a.amount),2) as amount , "+
                      " " + date1 +"::timestamp without time zone as attr_pol_per_0,"+ date2 +"::timestamp without time zone as attr_pol_per_1");
        sqa.addQuery(
                "   from " +
                "      ar_invoice a" +
                "         left join ent_master b on b.ent_id = a.ent_id");
        sqa.addClause(" a.no_surat_hutang is not null");

        //sqa.addOrder(" a.no_surat_hutang asc");
        sqa.setLimit(20);
        
        if(cc_code!=null){
            if(!cc_code.equalsIgnoreCase("00")){
                //sqa.addClause(" a.cc_code = ?");
                //sqa.addPar(cc_code);

                sqa.addClause(" a.cc_code in (?,'80')");
                sqa.addPar(cc_code);

                sqa.addClause("a.no_surat_hutang like '%/"+ cc_code +"/%'");
            }  
        }

        /*
        if (key==null){
            sqa.addClause("a.no_surat_hutang <> null");
        }

        if (key!=null){
            if(key.contains("%")){
                sqa.addClause("upper(a.no_surat_hutang) like ?");
                sqa.addPar("%"+f.key.replaceAll("%", "").toUpperCase()+"%");
            }else{
                sqa.addClause("a.no_surat_hutang = ?");
                sqa.addPar(f.key.toUpperCase());
            }
            
        }*/

        if (key!=null){
                sqa.addClause("a.no_surat_hutang like ?");
                sqa.addPar("%"+f.key.toUpperCase()+"%");
        }
        
        if (cust!=null) {
            if(!cust.equalsIgnoreCase("null")){
                sqa.addClause("a.ent_id = ?");
                sqa.addPar(cust);
            }
        }

        if(start_date!=null){
            sqa.addClause("date_trunc('day',a.invoice_date) >= ?");
            sqa.addPar(start_date);
        }

        if(end_date!=null){
            sqa.addClause("date_trunc('day',a.invoice_date) <= ?");
            sqa.addPar(end_date);
        }
        
        if(tax_code!=null && !tax_code.equalsIgnoreCase("null")){
            sqa.addQuery(" inner join ar_invoice_details c on a.ar_invoice_id = c.ar_invoice_id");

            if (type!=null) {
                sqa.addClause("a.invoice_type = ?");
                sqa.addPar(type);
            }

            if(tax_code.equalsIgnoreCase("PPH21")){
                sqa.addClause("c.ar_trx_line_id in (14,17,20,30,33,36,46,49,52)");
            }else if(tax_code.equalsIgnoreCase("PPH23")){
                sqa.addClause("c.ar_trx_line_id in (15,18,19,21,31,34,35,37,47,50,51,53,96,120)");
            }
        }
        
        
        //sqa.addClause("case when a.amount > 0 then a.amount else (a.amount*-1) end - coalesce(a.amount_settled,0) > 0");
        sqa.addClause("a.amount_settled is null");
        sqa.addClause("a.approved_flag = 'Y'");
        //sqa.addClause("a.posted_flag = 'Y'");
        sqa.addClause("a.used_flag is null");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");

        if(start_date==null && key==null){
            sqa.addClause("right(no_surat_hutang::text, 4) = '"+ year +"'");
            sqa.addClause("date_trunc('day'::text, mutation_date) >= '"+ year +"-01-01 00:00:00'");
        }
                      

        if(arsid!=null){
            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, arsid);
            
            if(stl.getStParameter1()!=null)
                sqa.addClause(stl.getStParameter1());
        }
        
        /*
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
                sqa.addQuery(" inner join ar_invoice_details c on a.ar_invoice_id = c.ar_invoice_id");
                
                if(tax_code.equalsIgnoreCase("PPH21")){
                    sqa.addClause("c.ar_trx_line_id in (14,17,20,30,33,36,46,49,52)");
                }else if(tax_code.equalsIgnoreCase("PPH23")){
                    sqa.addClause("c.ar_trx_line_id in (15,18,21,31,34,37,47,50,53)");
                }
                
                sqa.addClause(" ar_trx_type_id = ? ");
                sqa.addPar("11");
            }else if(arsid.equalsIgnoreCase("13")){
                sqa.addClause(" ar_trx_type_id = ? ");
                sqa.addPar("10");
            }else if(arsid.equalsIgnoreCase("14")){
                sqa.addClause(" ar_trx_type_id = ? ");
                sqa.addPar("16");
            }else if(arsid.equalsIgnoreCase("9")){
                sqa.addClause(" ar_trx_type_id = ? ");
                sqa.addPar("13");
            }
            
        }*/
        
        sqa.addGroup("a.no_surat_hutang,b.ent_name ");
        
        final DTOList l = sqa.getList(ARInvoiceView.class);
        
        rq.setAttribute("LIST",l);
        //rq.setAttribute("CC_CODE",cc_code);
    }

    public void searchTitipan(HttpServletRequest rq)  throws Exception {
        final String key = getString(rq.getParameter("key"));
        final String type = getString(rq.getParameter("type"));
        final String ccy = getString(rq.getParameter("ccy"));
        final String cust = getString(rq.getParameter("cust"));
        final String arsid = getString(rq.getParameter("arsid"));
        final String cc_code = getString(rq.getParameter("cc_code"));
        final String paymentmethodid = getString(rq.getParameter("paymentmethodid"));
        //String cc_code = SessionManager.getInstance().getSession().getStBranch();

        final ARPaymentMethodView payment= getPaymentMethod(paymentmethodid);
        //final String account_id = payment.getStGLAccountID();

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

        sqa.addSelect(" a.* ");
        sqa.addQuery(
                "   from " +
                "      ar_titipan_premi a inner join ar_invoice b on a.pol_no = b.attr_pol_no");

        sqa.addClause("coalesce(a.active_flag,'') <> 'N' and a.approved = 'Y' and a.balance <> 0");
        sqa.addClause(" a.pol_no is not null");
        sqa.addClause("b.ar_trx_type_id in (5,6,7)");
        sqa.addClause("b.amount_settled is null and coalesce(b.used_flag,'N') <> 'Y'");

        if(cc_code!=null){
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(cc_code);
        }

        sqa.setLimit(100);

        if (key!=null){
            sqa.addClause("(upper(a.trx_no) like ? or upper(a.description) like ? or upper(a.pol_no) like ?)");
            sqa.addPar("%"+f.key.toUpperCase()+"%");
            sqa.addPar("%"+f.key.toUpperCase()+"%");
            sqa.addPar("%"+f.key.toUpperCase()+"%");
        }


        final DTOList l = sqa.getList(TitipanPremiView.class);

        rq.setAttribute("LIST",l);
        
    }
    
    public void addInvoice(HttpServletRequest rq)  throws Exception {
        final ARReceiptView rc = retrieve(rq);
        
        final String invoiceID = getString(rq.getParameter("addinvoiceid"));
        
        final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(invoiceID);
        
        final ARReceiptLinesView rcl = new ARReceiptLinesView();
        
        rcl.setStInvoiceID(invoice.getStARInvoiceID());
        rcl.setStInvoiceNo(invoice.getStInvoiceNo());
        rcl.setDbInvoiceAmount(invoice.getDbOutstandingAmount());
        rcl.setDbAmount(rcl.getDbInvoiceAmount());
        rcl.markAsInvoice();
        
        rcl.markNew();
        
        rc.getDetails().add(rcl);
        
        rc.recalculate();
        
        populate(rq);
    }
    
    public void addNote(HttpServletRequest rq)  throws Exception {
        final ARReceiptView rc = retrieve(rq);
        
        final String invoiceID = getString(rq.getParameter("addnoteid"));
        
        final ARInvoiceView invoice = getRemoteAccountReceivable().getARInvoice(invoiceID);
        
        final ARReceiptLinesView rcl = new ARReceiptLinesView();
        
        rcl.setStInvoiceID(invoice.getStARInvoiceID());
        rcl.setStInvoiceNo(invoice.getStInvoiceNo());
        rcl.setDbInvoiceAmount(invoice.getDbOutstandingAmount());
        rcl.setDbAmount(rcl.getDbInvoiceAmount());
        rcl.markAsNote();
        
        rcl.markNew();
        
        rc.getNotes().add(rcl);
        
        rc.recalculate();
        
        populate(rq);
    }
    
    
    public void delNote(HttpServletRequest rq)  throws Exception {
        final ARReceiptView rc = retrieve(rq);
        
        rc.getNotes().delete(getLong(rq.getParameter("delindex")).intValue());
        
        rc.recalculate();
        
        populate(rq);
        
    }
    
    public void delInvoice(HttpServletRequest rq)  throws Exception {
        
        final ARReceiptView rc = retrieve(rq);
        
        rc.getDetails().delete(getLong(rq.getParameter("delindex")).intValue());
        
        rc.recalculate();
        
        populate(rq);
        
    }
    
    private ARPaymentMethodView getPaymentMethod(String stPaymentMethodID) {
        if (stPaymentMethodID==null) return null;
        return (ARPaymentMethodView) DTOPool.getInstance().getDTO(ARPaymentMethodView.class, stPaymentMethodID);
    }
    
    // private static HashSet formList = null;
    
    public void printReceipt(HttpServletRequest rq,HttpServletResponse rp)  throws Exception {
        
        loadFormList(rq);
        
        final String receiptid = rq.getParameter("receiptid");
        String alter = getString(rq.getParameter("alter"));
        
        final ARReceiptView receipt = getRemoteAccountReceivable().getARReceiptForPrinting(receiptid);
        
        rq.setAttribute("RECEIPT",receipt);
        
        final ArrayList plist = new ArrayList();
        
        if (alter==null) throw new RuntimeException("ALT code not specified");
        
        if (alter==null) alter=""; else alter="_"+alter;
        
        plist.add(receipt.getStARSettlementID()+alter);
        
        String urx=null;
        
        //logger.logDebug("printPolicy: scanlist:"+plist);
        
        
        for (int i = 0; i < plist.size(); i++) {
            String s = (String) plist.get(i);
            
            if (formList.contains("rcp"+s+".fop.jsp")) {
                urx = "/pages/ar/report/rcp"+s+".fop";
                break;
            }
        }
        
        //urx = "/pages/ar/report/penerimaanPremi.fop";
        
        if (urx==null) throw new RuntimeException("Unable to find suitable print form");
        
        //if (nom!=null)
        //getRemoteInsurance().registerPrintSerial(policy, nom, urx);
        
        logger.logDebug("print: forwarding to ########## "+urx);
        
        rq.getRequestDispatcher(urx).forward(rq,rp);
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
    
   public void searchClaim(HttpServletRequest rq) throws Exception {
        final String key = getString(rq.getParameter("key"));
        final String type = getString(rq.getParameter("type"));
        final String ccy = getString(rq.getParameter("ccy"));
        final String cust = getString(rq.getParameter("cust"));
        final String arsid = getString(rq.getParameter("arsid"));
        final String cc_code = SessionManager.getInstance().getSession().getStBranch();

        final ARInvoiceFilter f = new ARInvoiceFilter();

        f.key = key;

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" b.ref3,b.ref4,b.dla_no,a.* ");

        sqa.addQuery("from ar_invoice a "
                + " inner join ins_policy b on b.pol_id = a.attr_pol_id");

        if (cc_code != null) {
            if (!cc_code.equalsIgnoreCase("00")) {
                sqa.addClause(" a.cc_code in ('" + cc_code + "','80')");
//                sqa.addPar(cc_code);
            }
        }

        if (key != null) {
            sqa.addClause("(a.attr_pol_no like ? or b.dla_no like ?)");
            final String key1 = "%" + ((String) key).toUpperCase() + "%";
            sqa.addPar(key1);

            final String key2 = "%" + ((String) key).toUpperCase() + "%";
            sqa.addPar(key2);
        }

        sqa.addClause("a.ar_trx_type_id = 16");
        sqa.addClause("a.attr_pol_type_id = 21");
        sqa.addClause("a.ent_id in (94,96,2000,2001,2002)");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");



        sqa.setLimit(10);

        final DTOList l = sqa.getList(ARInvoiceView.class);

        rq.setAttribute("LIST", l);
    }

    public void searchClaimNo(HttpServletRequest rq) throws Exception {
        final String type = getString(rq.getParameter("type"));
        final String ccy = getString(rq.getParameter("ccy"));
        final String cust = getString(rq.getParameter("cust"));
        final String arsid = getString(rq.getParameter("arsid"));
        final String norek = getString(rq.getParameter("norek"));
        final String cc_code = SessionManager.getInstance().getSession().getStBranch();

        final ARInvoiceFilter f = new ARInvoiceFilter();

        f.norek = norek;

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("claim_no,claim_name,claim_coins_name,claim_coins_id,claim_coins_address,count(attr_pol_id) as attr_pol_id, sum(amount) as amount ");

        sqa.addQuery("from ar_invoice");

        if (cc_code != null) {
            if (!cc_code.equalsIgnoreCase("00")) {
                sqa.addClause(" cc_code in ('" + cc_code + "','80')");
//                sqa.addPar(cc_code);
            }
        }

        if (norek != null) {
            sqa.addClause("upper(claim_no) like ?");
            final String key1 = "%" + ((String) norek).toUpperCase() + "%";
            sqa.addPar(key1);
        }
        sqa.addClause("ar_trx_type_id = 16 ");
        sqa.addClause("claim_no is not null");
        sqa.addClause("coalesce(cancel_flag,'') <> 'Y'");

        sqa.addGroup("claim_no,claim_name,claim_coins_name,claim_coins_address,claim_coins_id");

        sqa.setLimit(10);

        final DTOList l = sqa.getList(ARInvoiceView.class);

        rq.setAttribute("LIST", l);
    }

    public void searchPLNo(HttpServletRequest rq) throws Exception {
        final String type = getString(rq.getParameter("type"));
        final String ccy = getString(rq.getParameter("ccy"));
        final String cust = getString(rq.getParameter("cust"));
        final String arsid = getString(rq.getParameter("arsid"));
        final String norek = getString(rq.getParameter("norek"));
        final String cc_code = SessionManager.getInstance().getSession().getStBranch();

        final ARInvoiceFilter f = new ARInvoiceFilter();

        f.norek = norek;

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("claim_no,claim_name,count(attr_pol_id) as attr_pol_id ");

        sqa.addQuery("from ar_invoice");

        if (cc_code != null) {
            if (!cc_code.equalsIgnoreCase("00")) {
                sqa.addClause(" cc_code in ('" + cc_code + "','80')");
//                sqa.addPar(cc_code);
            }
        }

        if (norek != null) {
            sqa.addClause("upper(claim_no) like ?");
            final String key1 = "%" + ((String) norek).toUpperCase() + "%";
            sqa.addPar(key1);
        }

        sqa.addClause("claim_no is not null");
        sqa.addClause("substr(refid0,1,5) = 'PREMI'");

        sqa.addGroup("claim_no,claim_name");
        //sqa.addClause("posted_flag = 'Y'");
        sqa.addClause("coalesce(cancel_flag,'') <> 'Y'");

        sqa.setLimit(10);

        final DTOList l = sqa.getList(ARInvoiceView.class);

        rq.setAttribute("LIST", l);
    }

    public void searchLKS(HttpServletRequest rq) throws Exception {
        final String key = getString(rq.getParameter("key"));
        final String type = getString(rq.getParameter("type"));
        final String ccy = getString(rq.getParameter("ccy"));
        final String cust = getString(rq.getParameter("cust"));
        final String arsid = getString(rq.getParameter("arsid"));
        final String cc_code = getString(rq.getParameter("cc_code"));

        //final ARInvoiceFilter f = new ARInvoiceFilter();

        //f.key = key;

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("*");

        sqa.addQuery("from ins_policy a"
                + " inner join ins_pol_items b on a.pol_id = b.pol_id ");

        sqa.addClause("claim_status ='PLA'");
        sqa.addClause("effective_flag ='Y'");
        sqa.addClause("item_class='CLM'");

        if (cc_code != null) {
            sqa.addClause(" a.cc_code in ('" + cc_code + "','80')");
//            sqa.addPar(cc_code);
        }

        if (key != null) {
            sqa.addClause("upper(pla_no) like ?");
            final String key1 = "%" + ((String) key).toUpperCase() + "%";
            sqa.addPar(key1);
        }

        sqa.setLimit(10);

        final DTOList l = sqa.getList(InsurancePolicyView.class);

        rq.setAttribute("LIST", l);
    }
    
   public void searchKwitansi(HttpServletRequest rq) throws Exception {
        final String key = getString(rq.getParameter("key"));
        final String type = getString(rq.getParameter("type"));
        final String ccy = getString(rq.getParameter("ccy"));
        final String cust = getString(rq.getParameter("cust"));
        final String arsid = getString(rq.getParameter("arsid"));
        //final String cc_code = getString(rq.getParameter("cc_code"));
        final String cc_code = SessionManager.getInstance().getSession().getStBranch();

        final ARInvoiceFilter f = new ARInvoiceFilter();

        f.key = key;

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("*");

        sqa.addQuery("from ar_invoice");

        if (cc_code != null) {
            if (!cc_code.equalsIgnoreCase("00")) {
                sqa.addClause(" cc_code in ('" + cc_code + "','80')");
//                sqa.addPar(cc_code);
            }
        }

//        if (key!=null){
//            sqa.addClause("attr_pol_no like ?");
//            sqa.addPar("%"+key.toUpperCase()+"%");
//        }

        if (key != null) {
            sqa.addClause("attr_pol_no = ?");
            sqa.addPar(key.toUpperCase());
        }

        if (type != null) {
            sqa.addClause("invoice_type = ?");
            sqa.addPar(type);
        }

        if (cust != null) {
            sqa.addClause("ent_id = ?");
            sqa.addPar(cust);
        }

        //sqa.addClause("posted_flag = 'Y'");
        sqa.addClause("(coalesce(cancel_flag,'') <> 'Y' and coalesce(posted_flag,'Y') = 'Y') ");

        //sqa.addClause("substr(refid0,1,5) = 'PREMI'");
        sqa.addClause("ar_trx_type_id in (5,6,7,10)");

        sqa.setLimit(10);

        final DTOList l = sqa.getList(ARInvoiceView.class);

        rq.setAttribute("LIST", l);
    }

    public void searchTitipanMinus(HttpServletRequest rq)  throws Exception {
        final String key = getString(rq.getParameter("key"));
        final String type = getString(rq.getParameter("type"));
        final String ccy = getString(rq.getParameter("ccy"));
        final String cust = getString(rq.getParameter("cust"));
        final String arsid = getString(rq.getParameter("arsid"));
        final String cc_code = getString(rq.getParameter("cc_code"));
        final String paymentmethodid = getString(rq.getParameter("paymentmethodid"));
        //String cc_code = SessionManager.getInstance().getSession().getStBranch();

        final ARPaymentMethodView payment= getPaymentMethod(paymentmethodid);
        //final String account_id = payment.getStGLAccountID();

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

        sqa.addSelect(" a.* ");
        sqa.addQuery(
                "   from " +
                "      ar_titipan_premi a inner join ar_invoice b on a.pol_no = b.attr_pol_no");

        sqa.addClause("coalesce(a.active_flag,'') <> 'N' and a.approved = 'Y' and a.balance <> 0");
        sqa.addClause(" a.pol_no is not null");
        sqa.addClause("a.balance < 0");
        sqa.addClause("b.ar_trx_type_id = 11");
        sqa.addClause("b.amount_settled is null and coalesce(b.used_flag,'N') <> 'Y'");

        if(cc_code!=null){
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(cc_code);
        }

        sqa.setLimit(100);

        if (key!=null){
            sqa.addClause("(upper(a.trx_no) like ? or upper(a.description) like ? or upper(a.pol_no) like ?)");
            sqa.addPar("%"+f.key.toUpperCase()+"%");
            sqa.addPar("%"+f.key.toUpperCase()+"%");
            sqa.addPar("%"+f.key.toUpperCase()+"%");
        }


        final DTOList l = sqa.getList(TitipanPremiView.class);

        rq.setAttribute("LIST",l);
        
    }

    public void printExcel(HttpServletRequest rq, HttpServletResponse rp) throws Exception {

        final String receiptid = rq.getParameter("receiptid");
        final String settlement = rq.getParameter("settlement");

        if (settlement.equalsIgnoreCase("14")) {
            getRemoteAccountReceivable().getARReceiptForPrintingExcelKlaimCo(receiptid, settlement);
        }else if(settlement.equalsIgnoreCase("48")){
            getRemoteAccountReceivable().getARReceiptForPrintingExcelPremiKhusus(receiptid, settlement);
        }
        else {
            getRemoteAccountReceivable().getARReceiptForPrintingExcelPremi(receiptid, settlement);
        }
    }


    public void printExcelKlaim(HttpServletRequest rq,HttpServletResponse rp)  throws Exception {

        final String receiptid = rq.getParameter("receiptid");
        final String settlement = rq.getParameter("settlement");

        getRemoteAccountReceivable().getARReceiptForPrintingExcelKlaim(receiptid,settlement);

    }

    public void searchVldClaim(HttpServletRequest rq) throws Exception {
        final String key = getString(rq.getParameter("key"));
        final String type = getString(rq.getParameter("type"));
        final String arsid = getString(rq.getParameter("arsid"));
        final String cc_code = getString(rq.getParameter("costcenter"));
        final String dateFrom = getString(rq.getParameter("dateFrom"));
        final String dateTo = getString(rq.getParameter("dateTo"));

        final ARInvoiceFilter f = new ARInvoiceFilter();

        f.key = key;

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" b.ref3,b.ref4,b.dla_no,a.* ");

        sqa.addQuery("from ar_invoice a "
                + " inner join ins_policy b on b.pol_id = a.attr_pol_id");

        if (cc_code != null) {
            sqa.addClause(" b.cc_code = ?");
            sqa.addPar(cc_code);
        }

        if (dateFrom != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(DateUtil.getDate(dateFrom));
        }

        if (dateTo != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(DateUtil.getDate(dateTo));
        }

        if (key != null) {
            sqa.addClause("(a.attr_pol_no like ? or b.dla_no like ?)");
            final String key1 = "%" + ((String) key).toUpperCase() + "%";
            sqa.addPar(key1);

            final String key2 = "%" + ((String) key).toUpperCase() + "%";
            sqa.addPar(key2);
        }

        sqa.addClause("b.f_validate_claim is null");
        sqa.addClause("a.ar_trx_type_id = 12");
        sqa.addClause("a.attr_pol_type_id in (21,59) ");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");

        sqa.setLimit(500);

        final DTOList l = sqa.getList(ARInvoiceView.class);

        rq.setAttribute("LIST", l);
    }

    public void searchInvoiceRI(HttpServletRequest rq) throws Exception {
        final String key = getString(rq.getParameter("key"));
        final String type = getString(rq.getParameter("type"));
        final String ccy = getString(rq.getParameter("ccy"));
        final String cust = getString(rq.getParameter("cust"));
        final String arsid = getString(rq.getParameter("arsid"));
        final String cc_code = getString(rq.getParameter("cc_code"));
        //final Date rcpdate = getDate(rq.getParameter("rcpdate"));
        final String journaltype = getString(rq.getParameter("journaltype"));
        final String slip = getString(rq.getParameter("key2"));
        boolean useCcCode = true;
        boolean isJournalOffset = false;
        
        boolean menuReas = arsid.equalsIgnoreCase("9") //pemb.reas
                || arsid.equalsIgnoreCase("11")//pemb.klaim reas
                || arsid.equalsIgnoreCase("29")//pemb.inwFac
                || arsid.equalsIgnoreCase("30")//pemb.inwTreaty
                || arsid.equalsIgnoreCase("31")//pemb.inwXol
                || arsid.equalsIgnoreCase("32")//pemb.profitcomm
                || arsid.equalsIgnoreCase("35")//pemb.klaimInwFac
                || arsid.equalsIgnoreCase("36")//pemb.klaimInwTreaty
                || arsid.equalsIgnoreCase("37")//pemb.klaimInwXol
                || arsid.equalsIgnoreCase("42")//pemb.Exgratia
                || arsid.equalsIgnoreCase("46")//pemb.klaim inward
                || arsid.equalsIgnoreCase("49");//realisasi tp reas

        if (journaltype != null) {
            if (journaltype.equalsIgnoreCase("OFFSET")) {
                isJournalOffset = true;
            }
        }

        if (arsid.equalsIgnoreCase("13") && canNavigateBranch) {
            useCcCode = false;
        }

        if (arsid.equalsIgnoreCase("14") && canNavigateBranch) {
            useCcCode = false;
        }

        final ARInvoiceFilter f = new ARInvoiceFilter();

        f.key = key;

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.*");

        sqa.addQuery("from ar_invoice a ");

        /*
        if(!arsid.equalsIgnoreCase("9"))
        if(cc_code!=null){
        sqa.addClause(" a.cc_code = ?");
        sqa.addPar(cc_code);
        }
         */

        if (!menuReas) {
            if (!isJournalOffset) {
                sqa.addClause(" a.cc_code = ?");
                sqa.addPar(cc_code);
            }
        }

        if (key == null && slip == null) {
            sqa.addClause("a.attr_pol_no <> null ");
        }

//        logger.logDebug("@@@@@@@@@@@@@@@@@@ "+ arsid);

        if (key != null) {
//            sqa.addClause("(a.attr_pol_no = ? or upper(invoice_no) = ?)");
//            final String key1 = ((String) key);
//            sqa.addPar(key1);
//
//            final String key2 = ((String) key).toUpperCase();
//            sqa.addPar(key2);

            if (arsid.equalsIgnoreCase("9") || arsid.equalsIgnoreCase("11")) {

                sqa.addClause("a.attr_pol_no = ? ");
                final String key1 = ((String) key);
                sqa.addPar(key1);

            } else if (arsid.equalsIgnoreCase("29") || arsid.equalsIgnoreCase("30")
                    || arsid.equalsIgnoreCase("31") || arsid.equalsIgnoreCase("43")
                    || arsid.equalsIgnoreCase("46") || arsid.equalsIgnoreCase("49")) {

                /*
                sqa.addClause("(a.attr_pol_no = ? or upper(invoice_no) = ?)");
                final String key1 = ((String) key);
                sqa.addPar(key1);
                */

                sqa.addClause("(a.attr_pol_no = ? or invoice_no = ?)");
                final String key1 = ((String) key);
                sqa.addPar(key1);

                final String key2 = ((String) key);
                sqa.addPar(key2);

            }
        }

        if (slip != null) {
            if (arsid.equalsIgnoreCase("9")) {

                sqa.addClause("refa1 = ? ");
                final String key1 = ((String) slip);
                sqa.addPar(key1);

            } else if (arsid.equalsIgnoreCase("29") || arsid.equalsIgnoreCase("30")
                    || arsid.equalsIgnoreCase("31") || arsid.equalsIgnoreCase("11")
                    || arsid.equalsIgnoreCase("43") || arsid.equalsIgnoreCase("46") || arsid.equalsIgnoreCase("49")) {

                sqa.addClause("(upper(refa1) = ? or upper(refid2) = ? or upper(reference_no) = ?)");
                final String key1 = ((String) slip).toUpperCase();
                sqa.addPar(key1);

                final String key2 = ((String) slip).toUpperCase();
                sqa.addPar(key2);

                final String key3 = ((String) slip).toUpperCase();
                sqa.addPar(key3);
            }
        }

        if (cust != null) {
            if (!cust.equalsIgnoreCase("null")) {
                sqa.addClause("a.ent_id = ?");
                sqa.addPar(cust);
            }
        }

        if (cc_code != null) {
            if (!cc_code.equalsIgnoreCase("00")) {
                if (arsid.equalsIgnoreCase("13") || arsid.equalsIgnoreCase("14")) {
                    sqa.addClause(" a.attr_pol_type_id not in ('31','32','33')");
                    sqa.addClause(" a.ent_id not in (select refid1 from ent_master_coas)");
                }
            }
        }

        //sqa.addClause("a.posted_flag = 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");

        if (arsid != null) {
            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, arsid);

            if (stl.getStParameter1() != null) {
                sqa.addClause(stl.getStParameter1());
            }

            if (stl.getStSearchCondition() != null) {
                sqa.addQuery(stl.getStSearchCondition());
            }
        }

        /*
        sqa.addClause("case when a.amount < 0 then (a.amount*-1) - coalesce(a.amount_settled,0) > 0"+
        " else a.amount - coalesce(a.amount_settled,0) > 0 end");*/

        //sqa.addClause("a.used_flag is null");

        if (key == null && slip == null) {
            sqa.setLimit(10);
        }

        final DTOList l = sqa.getList(ARInvoiceView.class);

        rq.setAttribute("LIST", l);
    }

    public void searchSuratHutangReas(HttpServletRequest rq) throws Exception {
        final String key = getString(rq.getParameter("key"));
        final String type = getString(rq.getParameter("type"));
        final String ccy = getString(rq.getParameter("ccy"));
        final String cust = getString(rq.getParameter("cust"));
        final String arsid = getString(rq.getParameter("arsid"));
        final String cc_code = getString(rq.getParameter("cc_code"));
//        final String tax_code = getString(rq.getParameter("tax_code"));
        final String jenas = getString(rq.getParameter("jenas"));
//        final String treaty = getString(rq.getParameter("treaty"));
        Date start_date = getDate(rq.getParameter("start_date"));
        Date end_date = getDate(rq.getParameter("end_date"));
        Date start_per_date = getDate(rq.getParameter("start_per_date"));
        Date end_per_date = getDate(rq.getParameter("end_per_date"));

        final ARInvoiceFilter f = new ARInvoiceFilter();

        f.key = key;
        f.start_date = start_date;

        /*return
        ListUtil.getDTOListFromQuery(
        "select * from ar_invoice where invoice_no like ?",
        new Object [] { "%"+f.key+"%"},
        ARInvoiceView.class
        );


        final DTOList l = getRemoteAccountReceivable().searchInvoice(f);*/

        String date1 = "null";
        String date2 = "null";
        String perdate1 = "null";
        String perdate2 = "null";
        if (start_date != null) {
            if (!start_date.toString().equalsIgnoreCase("null")) {
                date1 = "'" + start_date.toString() + "'";
            }
        }

        if (end_date != null) {
            if (!end_date.toString().equalsIgnoreCase("null")) {
                date2 = "'" + end_date.toString() + "'";
            }
        }
        if (start_per_date != null) {
            if (!start_per_date.toString().equalsIgnoreCase("null")) {
                perdate1 = "'" + start_per_date.toString() + "'";
            }
        }

        if (end_per_date != null) {
            if (!end_per_date.toString().equalsIgnoreCase("null")) {
                perdate2 = "'" + end_per_date.toString() + "'";
            }
        }

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" distinct a.no_surat_hutang,b.ent_name as refa0,count(a.no_surat_hutang) as refa1, round(sum(a.amount),2) as amount, "
                + " " + date1 + "::timestamp without time zone as surat_hutang_period_from," + date2 + "::timestamp without time zone as surat_hutang_period_to, "
                + " " + perdate1 + "::timestamp without time zone as attr_pol_per_0," + perdate2 + "::timestamp without time zone as attr_pol_per_1 ");
        sqa.addQuery(
                " from ar_invoice a "
                + " left join ent_master b on b.ent_id = a.ent_id ");
        //sqa.addClause(" a.no_surat_hutang is not null");
        sqa.setLimit(20);

        if (cc_code != null) {
            if (!cc_code.equalsIgnoreCase("00")) {
                sqa.addClause(" a.cc_code = ?");
                sqa.addPar(cc_code);
            }
        }

        if (key == null) {
            if (start_date != null || end_date != null || start_per_date != null || end_per_date != null) {
                sqa.addClause(" a.no_surat_hutang is not null");
            } else {
                sqa.addClause("a.no_surat_hutang <> null");
            }
        }

        if (key != null) {
            sqa.addClause("a.no_surat_hutang = ?");
            sqa.addPar(f.key);
//            sqa.addPar(f.key.toUpperCase());
        }

//        if (key != null) {
//            if (key.contains("%")) {
//                sqa.addClause("upper(a.no_surat_hutang) like ?");
//                sqa.addPar("%" + f.key.replaceAll("%", "").toUpperCase() + "%");
//            } else {
//                sqa.addClause("upper(a.no_surat_hutang) = ?");
//                sqa.addPar(f.key.toUpperCase());
//            }
//        }

        if (cust != null) {
            if (!cust.equalsIgnoreCase("null")) {
                sqa.addClause("a.ent_id = ?");
                sqa.addPar(cust);
            }
        }

        if (jenas != null) {
            if (!jenas.equalsIgnoreCase("null")) {
                sqa.addClause("a.attr_pol_type_id = ?");
                sqa.addPar(jenas);
            }
        }

//        if (treaty != null) {
//            if (!treaty.equalsIgnoreCase("null")) {
//                if (treaty.equalsIgnoreCase("Quota Share") || treaty.equalsIgnoreCase("Surplus")) {
//                    sqa.addClause("a.refz1 in ('Quota Share','Surplus')");
//                } else {
//                    sqa.addClause("a.refz1 = ?");
//                    sqa.addPar(treaty);
//                }
//            }
//        }

        if (start_date != null) {
            sqa.addClause("date_trunc('day',a.invoice_date) >= ?");
            sqa.addPar(start_date);
        }

        if (end_date != null) {
            sqa.addClause("date_trunc('day',a.invoice_date) <= ?");
            sqa.addPar(end_date);
        }

        if (start_per_date != null) {
            sqa.addClause("date_trunc('day',a.attr_pol_per_0) >= ?");
            sqa.addPar(start_per_date);
        }

        if (end_per_date != null) {
            sqa.addClause("date_trunc('day',a.attr_pol_per_0) <= ?");
            sqa.addPar(end_per_date);
        }

        if (type != null) {
            //sqa.addClause("a.invoice_type = ?");
            //sqa.addPar(type);
        }


        //sqa.addClause("case when a.amount > 0 then a.amount else (a.amount*-1) end - coalesce(a.amount_settled,0) > 0");
        sqa.addClause("a.amount_settled is null");
        //sqa.addClause("a.posted_flag = 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");

        if (arsid != null) {
            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, arsid);

            if (stl.getStParameter1() != null) {
                sqa.addClause(stl.getStParameter1());
            }
        }

        sqa.addGroup("a.no_surat_hutang,b.ent_name ");
        sqa.addOrder("a.no_surat_hutang,b.ent_name ");

        final DTOList l = sqa.getList(ARInvoiceView.class);

        rq.setAttribute("LIST", l);
        //rq.setAttribute("CC_CODE",cc_code);
    }

    public void searchSuratHutangComm(HttpServletRequest rq) throws Exception {
        final String key = getString(rq.getParameter("key"));
        final String type = getString(rq.getParameter("type"));
        final String ccy = getString(rq.getParameter("ccy"));
        final String cust = getString(rq.getParameter("cust"));
        final String arsid = getString(rq.getParameter("arsid"));
        final String cc_code = getString(rq.getParameter("cc_code"));

        final ARInvoiceFilter f = new ARInvoiceFilter();
        logger.logDebug("@@@@@@@@@@@@@@@@ " + type);
        logger.logDebug("################ " + cust);
        logger.logDebug("@@@@@@@@@@@@@@@@ " + arsid);
        logger.logDebug("################ " + cc_code);

        f.key = key;

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" distinct a.no_surat_hutang,count(a.no_surat_hutang) as refa1, round(sum(a.amount),2) as amount ");

        sqa.addQuery(" from ar_invoice a "
                + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id and c.ar_trx_line_id in (8,24,40) ");

        sqa.addClause("a.no_surat_hutang is not null");
        sqa.addClause("a.approved_flag = 'Y'");
        sqa.addClause("a.invoice_type = 'AP'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.amount_settled is null");
        sqa.setLimit(20);

        if (cc_code != null) {
            if (!cc_code.equalsIgnoreCase("00")) {
                sqa.addClause(" a.cc_code in ('" + cc_code + "','80')");
//                sqa.addPar(cc_code);
            }
        }

        if (key == null) {
            sqa.addClause("a.no_surat_hutang <> null");
        }

        if (key != null) {
            sqa.addClause(" a.no_surat_hutang = ? ");
//            sqa.addPar("%" + f.key.toUpperCase() + "%");
            sqa.addPar(f.key.toUpperCase());
        }

        if (cust != null) {
            if (!cust.equalsIgnoreCase("null")) {
                sqa.addClause("a.ent_id = ?");
                sqa.addPar(cust);
            }
        }

        if (arsid != null) {
            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, arsid);

            if (stl.getStParameter1() != null) {
                sqa.addClause(stl.getStParameter1());
            }
        }

        sqa.addGroup("a.no_surat_hutang ");

        final DTOList l = sqa.getList(ARInvoiceView.class);

        rq.setAttribute("LIST", l);
    }

    public void searchSuratHutangReasRekap(HttpServletRequest rq) throws Exception {
        final String key = getString(rq.getParameter("key"));
        final String cust = getString(rq.getParameter("cust"));

        final ARInvoiceFilter f = new ARInvoiceFilter();

        f.key = key;

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" no_surat_hutang,description ");
        sqa.addQuery(" from ins_gl_closing_report a ");

        sqa.setLimit(20);

        if (key != null) {
//            if (key.contains("%")) {
//                sqa.addClause("a.ri_slip_no = ?");
//                sqa.addPar(f.key.replaceAll("%", "").toUpperCase());
//            } else {
            sqa.addClause("a.ri_slip_no = ?");
            sqa.addPar(f.key);
//            }
        }

//        if (key != null) {
//            if (key.contains("%")) {
//                sqa.addClause("a.ri_slip_no = ?");
//                sqa.addPar(f.key.replaceAll("%", "").toUpperCase());
//            } else {
//            sqa.addClause("a.ri_slip_no = ?");
//            sqa.addPar(f.key.toUpperCase());
//            }
//        }

        if (cust != null) {
            sqa.addClause("a.reasuradur_id = ?");
            sqa.addPar(cust);
        }

        final DTOList l = sqa.getList(ARInvoiceView.class);

        rq.setAttribute("LIST", l);
    }

    public boolean isProposalKomisi(String polno) throws Exception {

        SQLUtil S = new SQLUtil();

        boolean isPosted = false;

        try {
            String cek = " select * from ins_proposal_komisi where pol_no = ? ";

            PreparedStatement PS = S.setQuery(cek);
            PS.setString(1, polno);

            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                isPosted = true; 
            }

        } finally {
            S.release();
        }

        return isPosted;
    }


    public void searchNomorRekap(HttpServletRequest rq)  throws Exception {
        final String key = getString(rq.getParameter("key"));
        final String type = getString(rq.getParameter("type"));
        final String ccy = getString(rq.getParameter("ccy"));
        final String cust = getString(rq.getParameter("cust"));
        final String arsid = getString(rq.getParameter("arsid"));
        final String cc_code = getString(rq.getParameter("cc_code"));
        Date start_date = getDate(rq.getParameter("start_date"));
        Date end_date = getDate(rq.getParameter("end_date"));

        final ARInvoiceFilter f = new ARInvoiceFilter();

        f.key = key;
        f.start_date = start_date;


        String date1 = "null";
        String date2 = "null";
        if(start_date!=null){
            if(!start_date.toString().equalsIgnoreCase("null"))
                date1 = "'"+ start_date.toString() +"'";
        }

        if(end_date!=null){
            if(!end_date.toString().equalsIgnoreCase("null"))
                date2 = "'"+ end_date.toString() +"'";
        }

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" distinct a.no_surat_hutang,count(a.no_surat_hutang) as refa1, round(sum(a.amount),2) as amount , "+
                      " round(sum(a.amount_settled),2) as amount_settled, "+
                      "(select sum(y.amount) "+
                      " from ar_invoice x inner join ar_invoice_details y on x.ar_invoice_id = y.ar_invoice_id "+
                      " where x.no_surat_hutang = a.no_surat_hutang and y.ar_trx_line_id in (1,4,7,23,39,55,79)) as entered_amount,"+
                      " " + date1 +"::timestamp without time zone as attr_pol_per_0,"+ date2 +"::timestamp without time zone as attr_pol_per_1");
        sqa.addQuery(
                "   from " +
                "      ar_invoice a" +
                "         left join ent_master b on b.ent_id = a.ent_id");
        sqa.addClause(" a.no_surat_hutang is not null");

        if (key==null && start_date==null && end_date==null) sqa.setLimit(0);

        if(cc_code!=null){
            if(!cc_code.equalsIgnoreCase("00")){
                sqa.addClause(" a.cc_code = ?");
                sqa.addPar(cc_code);
            }
        }

        if (key!=null){
            if(key.length()<20){
                sqa.addClause("a.no_surat_hutang like ?");
                sqa.addPar("%"+f.key.toUpperCase()+"%");
            }else if(key.length()>20){
                sqa.addClause("a.no_surat_hutang = ?");
                sqa.addPar(f.key.toUpperCase());
            }
                

                sqa.setLimit(10);
        }

        if (cust!=null) {
            if(!cust.equalsIgnoreCase("null")){
                sqa.addClause("a.ent_id = ?");
                sqa.addPar(cust);
            }
        }

        if(start_date!=null){
            sqa.addClause("date_trunc('day',a.surat_hutang_period_from) >= ?");
            sqa.addPar(start_date);
        }

        if(end_date!=null){
            sqa.addClause("date_trunc('day',a.surat_hutang_period_from) <= ?");
            sqa.addPar(end_date);
        }

        //sqa.addClause("case when a.amount > 0 then a.amount else (a.amount*-1) end - coalesce(a.amount_settled,0) > 0");
        //sqa.addClause("a.amount_settled is null");
        //sqa.addClause("a.posted_flag = 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");

        if(arsid!=null){
            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, arsid);

            if(stl.getStParameter1()!=null)
                sqa.addClause(stl.getStParameter1());
        }

        sqa.addGroup("a.no_surat_hutang ");

        final DTOList l = sqa.getList(ARInvoiceView.class);

        rq.setAttribute("LIST",l);

    }

    public void searchInvoiceByReferenceNo(HttpServletRequest rq)  throws Exception {
        final String key = getString(rq.getParameter("key"));
        final String type = getString(rq.getParameter("type"));
        final String ccy = getString(rq.getParameter("ccy"));
        final String cust = getString(rq.getParameter("cust"));
        final String arsid = getString(rq.getParameter("arsid"));
        final String cc_code = getString(rq.getParameter("cc_code"));
        final String journaltype = getString(rq.getParameter("journaltype"));
        boolean useCcCode = true;
        boolean isJournalOffset = false;

        if(journaltype!=null)
            if(journaltype.equalsIgnoreCase("OFFSET"))
                isJournalOffset = true;

        if(arsid.equalsIgnoreCase("13") && canNavigateBranch)
            useCcCode = false;

        if(arsid.equalsIgnoreCase("14") && canNavigateBranch)
            useCcCode = false;

        final ARInvoiceFilter f = new ARInvoiceFilter();

        f.key = key;

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.*");

        sqa.addQuery("from ar_invoice a ");


        if(!isJournalOffset){
            sqa.addClause(" a.cc_code = ?");
            sqa.addPar(cc_code);
        }
        

        if (key!=null){
            sqa.addClause("reference_no = ? ");
            sqa.addPar(key.toUpperCase());
        }

        if (cust!=null) {
            if(!cust.equalsIgnoreCase("null")){
                sqa.addClause("a.ent_id = ?");
                sqa.addPar(cust);
            }
        }

        if(cc_code!=null){
            if(!cc_code.equalsIgnoreCase("00")){
                if(arsid.equalsIgnoreCase("13") || arsid.equalsIgnoreCase("14")){
                    sqa.addClause(" a.attr_pol_type_id <> ALL (VALUES (31),(32),(33))");
                    sqa.addClause(" a.ent_id <> ALL (VALUES (1),(94),(96),(111),(2000),(2001),(2002))");
                }
            }
        }

        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");

        if(arsid!=null){
            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, arsid);

            if(stl.getStParameter1()!=null)
                sqa.addClause(stl.getStParameter1());

            if(stl.getStSearchCondition()!=null)
                sqa.addQuery(stl.getStSearchCondition());
        }

        if (key!=null){
            sqa.setLimit(50);
        }else{
            sqa.setLimit(0);
        }


        final DTOList l = sqa.getList(ARInvoiceView.class);

        rq.setAttribute("LIST",l);
    }

    public void searchSuratHutangClaim(HttpServletRequest rq) throws Exception {
        final String key = getString(rq.getParameter("key"));
        final String type = getString(rq.getParameter("type"));
        final String ccy = getString(rq.getParameter("ccy"));
        final String cust = getString(rq.getParameter("cust"));
        final String arsid = getString(rq.getParameter("arsid"));
        final String cc_code = getString(rq.getParameter("cc_code"));

        final ARInvoiceFilter f = new ARInvoiceFilter();
        logger.logDebug("@@@@@@@@@@@@@@@@ " + type);
        logger.logDebug("################ " + cust);
        logger.logDebug("@@@@@@@@@@@@@@@@ " + arsid);
        logger.logDebug("################ " + cc_code);

        f.key = key;

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" distinct a.no_surat_hutang,count(a.no_surat_hutang) as refa1, round(sum(a.amount),2) as amount ");

        sqa.addQuery(" from ar_invoice a "
                + " inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id and c.ar_trx_line_id = 57 ");

        sqa.addClause("a.no_surat_hutang is not null");
        sqa.addClause("a.approved_flag = 'Y'");
        sqa.addClause("a.invoice_type = 'AP'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
//        sqa.addClause("a.amount_settled is null");
        sqa.setLimit(20);

        if (cc_code != null) {
            if (!cc_code.equalsIgnoreCase("00")) {
                sqa.addClause(" a.cc_code = ?");
                sqa.addPar(cc_code);
            }
        }

        if (key == null) {
            sqa.addClause("a.no_surat_hutang is not null");
        }

        if (key != null) {
            sqa.addClause(" a.no_surat_hutang = ? ");
//            sqa.addPar("%" + f.key.toUpperCase() + "%");
            sqa.addPar(f.key.toUpperCase());
        }

        if (cust != null) {
            if (!cust.equalsIgnoreCase("null")) {
                sqa.addClause("a.ent_id = ?");
                sqa.addPar(cust);
            }
        }

        if (arsid != null) {
            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, arsid);

            if (stl.getStParameter1() != null) {
                sqa.addClause(stl.getStParameter1());
            }
        }

        sqa.addGroup("a.no_surat_hutang ");

        final DTOList l = sqa.getList(ARInvoiceView.class);

        rq.setAttribute("LIST", l);
    }

    public void searchNomorTransaksi(HttpServletRequest rq) throws Exception {
        final String key = getString(rq.getParameter("key"));
        final String type = getString(rq.getParameter("type"));
        final String ccy = getString(rq.getParameter("ccy"));
        final String cust = getString(rq.getParameter("cust"));
        final String arsid = getString(rq.getParameter("arsid"));
        final String cc_code = getString(rq.getParameter("cc_code"));
//        final String tax_code = getString(rq.getParameter("tax_code"));
        final String jenas = getString(rq.getParameter("jenas"));
//        final String treaty = getString(rq.getParameter("treaty"));
        Date start_date = getDate(rq.getParameter("start_date"));
        Date end_date = getDate(rq.getParameter("end_date"));
        Date start_per_date = getDate(rq.getParameter("start_per_date"));
        Date end_per_date = getDate(rq.getParameter("end_per_date"));

        final ARInvoiceFilter f = new ARInvoiceFilter();

        f.key = key;
        f.start_date = start_date;

        /*return
        ListUtil.getDTOListFromQuery(
        "select * from ar_invoice where invoice_no like ?",
        new Object [] { "%"+f.key+"%"},
        ARInvoiceView.class
        );


        final DTOList l = getRemoteAccountReceivable().searchInvoice(f);*/

        String date1 = "null";
        String date2 = "null";
        String perdate1 = "null";
        String perdate2 = "null";
        if (start_date != null) {
            if (!start_date.toString().equalsIgnoreCase("null")) {
                date1 = "'" + start_date.toString() + "'";
            }
        }

        if (end_date != null) {
            if (!end_date.toString().equalsIgnoreCase("null")) {
                date2 = "'" + end_date.toString() + "'";
            }
        }
        if (start_per_date != null) {
            if (!start_per_date.toString().equalsIgnoreCase("null")) {
                perdate1 = "'" + start_per_date.toString() + "'";
            }
        }

        if (end_per_date != null) {
            if (!end_per_date.toString().equalsIgnoreCase("null")) {
                perdate2 = "'" + end_per_date.toString() + "'";
            }
        }

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" distinct a.reference_no,a.ccy,b.ent_name as refa0,count(a.reference_no) as refa1, round(sum(a.amount),2) as amount, "
                + " " + date1 + "::timestamp without time zone as surat_hutang_period_from," + date2 + "::timestamp without time zone as surat_hutang_period_to, "
                + " " + perdate1 + "::timestamp without time zone as attr_pol_per_0," + perdate2 + "::timestamp without time zone as attr_pol_per_1 ");
        sqa.addQuery(
                " from ar_invoice a "
                + " left join ent_master b on b.ent_id = a.ent_id ");
        //sqa.addClause(" a.no_surat_hutang is not null");
        sqa.setLimit(20);

        if (cc_code != null) {
            if (!cc_code.equalsIgnoreCase("00")) {
                sqa.addClause(" a.cc_code = ?");
                sqa.addPar(cc_code);
            }
        }

        if (key == null) {
            if (start_date != null || end_date != null || start_per_date != null || end_per_date != null) {
                sqa.addClause(" a.reference_no is not null");
            } else {
                sqa.addClause("a.reference_no <> null");
            }
        }

        if (key != null) {
            sqa.addClause("a.reference_no = ?");
            sqa.addPar(f.key);
//            sqa.addPar(f.key.toUpperCase());
        }

//        if (key != null) {
//            if (key.contains("%")) {
//                sqa.addClause("upper(a.no_surat_hutang) like ?");
//                sqa.addPar("%" + f.key.replaceAll("%", "").toUpperCase() + "%");
//            } else {
//                sqa.addClause("upper(a.no_surat_hutang) = ?");
//                sqa.addPar(f.key.toUpperCase());
//            }
//        }

        if (cust != null) {
            if (!cust.equalsIgnoreCase("null")) {
                sqa.addClause("a.ent_id = ?");
                sqa.addPar(cust);
            }
        }

        if (jenas != null) {
            if (!jenas.equalsIgnoreCase("null")) {
                sqa.addClause("a.attr_pol_type_id = ?");
                sqa.addPar(jenas);
            }
        }

//        if (treaty != null) {
//            if (!treaty.equalsIgnoreCase("null")) {
//                if (treaty.equalsIgnoreCase("Quota Share") || treaty.equalsIgnoreCase("Surplus")) {
//                    sqa.addClause("a.refz1 in ('Quota Share','Surplus')");
//                } else {
//                    sqa.addClause("a.refz1 = ?");
//                    sqa.addPar(treaty);
//                }
//            }
//        }

        if (start_date != null) {
            sqa.addClause("date_trunc('day',a.invoice_date) >= ?");
            sqa.addPar(start_date);
        }

        if (end_date != null) {
            sqa.addClause("date_trunc('day',a.invoice_date) <= ?");
            sqa.addPar(end_date);
        }

        if (start_per_date != null) {
            sqa.addClause("date_trunc('day',a.attr_pol_per_0) >= ?");
            sqa.addPar(start_per_date);
        }

        if (end_per_date != null) {
            sqa.addClause("date_trunc('day',a.attr_pol_per_0) <= ?");
            sqa.addPar(end_per_date);
        }

        if (type != null) {
            //sqa.addClause("a.invoice_type = ?");
            //sqa.addPar(type);
        }


        //sqa.addClause("case when a.amount > 0 then a.amount else (a.amount*-1) end - coalesce(a.amount_settled,0) > 0");
        sqa.addClause("a.amount_settled is null");
        //sqa.addClause("a.posted_flag = 'Y'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");

        if (arsid != null) {
            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, arsid);

            if (stl.getStParameter1() != null) {
                sqa.addClause(stl.getStParameter1());
            }
        }

        sqa.addGroup("a.reference_no,a.ccy,b.ent_name ");
        sqa.addOrder("a.reference_no,a.ccy, b.ent_name ");

        final DTOList l = sqa.getList(ARInvoiceView.class);

        rq.setAttribute("LIST", l);
        //rq.setAttribute("CC_CODE",cc_code);
    }

    public void searchSuratHutangIzinCair(HttpServletRequest rq) throws Exception {
        final String key = getString(rq.getParameter("key"));
        final String type = getString(rq.getParameter("type"));
        final String ccy = getString(rq.getParameter("ccy"));
        final String cust = getString(rq.getParameter("cust"));
        final String arsid = getString(rq.getParameter("arsid"));
        final String cc_code = getString(rq.getParameter("cc_code"));

        final ARInvoiceFilter f = new ARInvoiceFilter();
        logger.logDebug("@@@@@@@@@@@@@@@@ " + type);
        logger.logDebug("################ " + cust);
        logger.logDebug("@@@@@@@@@@@@@@@@ " + arsid);
        logger.logDebug("################ " + cc_code);

        f.key = key;

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" distinct a.no_izin_pencairan as no_surat_hutang,count(a.no_izin_pencairan) as refa1, round(sum(a.amount),2) as amount ");
        sqa.addQuery(" from ar_invoice a ");

        sqa.addClause("a.no_izin_pencairan is not null");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
//        sqa.addClause("a.amount_settled is null");
//        sqa.addClause("a.approved_flag = 'Y'");
//        sqa.addClause("a.invoice_type = 'AP'");
        sqa.setLimit(20);

        if (cc_code != null) {
            if (!cc_code.equalsIgnoreCase("00")) {
                sqa.addClause(" a.cc_code in ('" + cc_code + "','80')");
//                sqa.addPar(cc_code);
            }
        }

        if (key == null) {
            sqa.addClause("a.no_izin_pencairan is not null");
        }

        if (key != null) {
            sqa.addClause(" a.no_izin_pencairan = ? ");
//            sqa.addPar("%" + f.key.toUpperCase() + "%");
            sqa.addPar(f.key.toUpperCase());
        }

        if (cust != null) {
            if (!cust.equalsIgnoreCase("null")) {
                sqa.addClause("a.ent_id = ?");
                sqa.addPar(cust);
            }
        }

        if (arsid != null) {
            ARAPSettlementView stl = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, arsid);

            if (stl.getStParameter1() != null) {
                sqa.addClause(stl.getStParameter1());
            }
        }

        sqa.addGroup("a.no_izin_pencairan ");

        final DTOList l = sqa.getList(ARInvoiceView.class);

        rq.setAttribute("LIST", l);
    }
    
}
