/***********************************************************************
 * Module:  com.webfin.ar.forms.InvoiceListForm
 * Author:  Denny Mahendra
 * Created: Jan 8, 2006 11:03:51 PM
 * Purpose:
 ***********************************************************************/

package com.webfin.insurance.form;

import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.ar.ejb.AccountReceivableHome;
import com.webfin.ar.model.ARTransactionTypeView;
//import com.webfin.ar.model.ARInvoiceView;
import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.SQLAssembler;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.pool.DTOPool;
import com.crux.util.ListUtil;
import com.crux.util.LogManager;
import com.crux.util.SQLUtil;
import com.webfin.gl.ejb.CurrencyManager;
import com.webfin.gl.model.GLCurrencyHistoryView;
import com.webfin.insurance.model.InsurancePolicyInwardDetailView;
import com.webfin.insurance.model.InsurancePolicyInwardView;
import java.util.Date;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

public class InvoiceInwardTreatyListForm extends Form {
    private DTOList list;
    private String invoiceid;
    private String trxId;
    private String customer;
    private String trxno;
    private String branch;
    private ARTransactionTypeView artrxtype;
    private DTOList list2;
    private String nosurathutang;
    private String nosurathutang2;
    private String treatytype;
    private boolean canApproved = SessionManager.getInstance().getSession().hasResource("AR_TRX_1_APPROVE");
    private String invoiceno;
    private String stEntityID;
    private String stEntityName;
    private Date transdatefrom;
    private Date transdateto;
    
    private final static transient LogManager logger = LogManager.getInstance(InvoiceInwardTreatyListForm.class);
    
    public String getTreatytype() {
        return treatytype;
    }
    
    public void setTreatytype(String treatytype) {
        this.treatytype = treatytype;
    }
    
    public String getBranch() {
        return branch;
    }
    
    public void setBranch(String branch) {
        this.branch = branch;
    }
    
    public String getCustomer() {
        return customer;
    }
    
    public void setCustomer(String customer) {
        this.customer = customer;
    }
    
    public String getTrxno() {
        return trxno;
    }
    
    public void setTrxno(String trxno) {
        this.trxno = trxno;
    }
    
    public void btnSearch() {
        
    }
    
    private AccountReceivable getRemoteAccountReceivable() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((AccountReceivableHome) JNDIUtil.getInstance().lookup("AccountReceivableEJB",AccountReceivableHome.class.getName()))
        .create();
    }
    
    public DTOList getList() throws Exception {
        
        if (list==null) {
            list=new DTOList();
            
            list.getFilter().activate();
        }
        
        final SQLAssembler sqa = new SQLAssembler();
        
        sqa.addSelect("a.*,b.*, a.create_who as user_input, "+
                       "(select user_name "+
                        " from s_users "+
                        " where user_id = a.create_who limit 1) as create_name  ");
        sqa.addQuery(
                "   from " +
                "      ins_pol_inward a" +
                "         left join ent_master b on b.ent_id = a.ent_id");
        sqa.addOrder("a.create_date desc");
        
        if (trxId!=null) {
            sqa.addClause("(ar_trx_type_id = ? or ar_trx_type_id in (select ar_trx_type_id from ar_trx_type where parent_trx_id = ?))");
            sqa.addPar(trxId);
            sqa.addPar(trxId);
        }
        
        if (customer!=null) {
            sqa.addClause("upper(b.ent_name) like ?");
            sqa.addPar("%"+customer.toUpperCase()+"%");
        }
        
        if (trxno!=null) {
            sqa.addClause("upper(a.invoice_no) like ?");
            sqa.addPar("%"+trxno.toUpperCase()+"%");
        }
        
        if (invoiceno!=null) {
            sqa.addClause("upper(a.trx_no_reference) like ?");
            sqa.addPar("%"+invoiceno.toUpperCase()+"%");
        }
        
/*
      if (branch!=null) {
         sqa.addClause("a.cc_code=?");
         sqa.addPar(branch);
      }
 */
        
        if (stEntityID!=null) {
            sqa.addClause("a.ent_id = ? ");
            sqa.addPar(stEntityID);
        }
        
        if (transdatefrom!=null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(transdatefrom);
        }
        
        if (transdateto!=null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(transdateto);
        }
        
        sqa.addFilter(list.getFilter());

        sqa.setLimit(800);
        
        list = sqa.getList(InsurancePolicyInwardView.class);
        
        
        return list;
    }
    
    public void setList(DTOList list) {
        this.list = list;
    }
    
    public DTOList getList2() throws Exception {
        
        if (list2==null) {
            list2=new DTOList();
            
            list2.getFilter().activate();
        }
        
        final SQLAssembler sqa = new SQLAssembler();
        
      /*
      select distinct a.no_surat_hutang,sum(a.amount),b.ent_id,b.ent_name
from
ar_invoice a
left join ent_master b on b.ent_id = a.ent_id
where a.no_surat_hutang is not null
group  by a.no_surat_hutang,b.ent_id,b.ent_name
order by a.no_surat_hutang asc*/
        
        sqa.addSelect(" distinct a.no_surat_hutang,sum(a.amount) as tagihan_amount,b.ent_id,b.ent_name ");
        sqa.addQuery(
                "   from " +
                "      ar_invoice a" +
                "         left join ent_master b on b.ent_id = a.ent_id");
        sqa.addClause(" a.no_surat_hutang is not null");
        sqa.addGroup(" a.no_surat_hutang,b.ent_id,b.ent_name order by a.no_surat_hutang asc");
        
//      if (trxId!=null) {
//         sqa.addClause("(ar_trx_type_id = ? or ar_trx_type_id in (select ar_trx_type_id from ar_trx_type where parent_trx_id = ?))");
//         sqa.addPar(trxId);
//         sqa.addPar(trxId);
//      }
        
        if (customer!=null) {
            sqa.addClause("upper(b.ent_name) like ?");
            sqa.addPar("%"+customer.toUpperCase()+"%");
        }
        
        if (nosurathutang2!=null) {
            sqa.addClause("upper(a.no_surat_hutang) like ?");
            sqa.addPar("%"+nosurathutang2.toUpperCase()+"%");
        }
        
        if (branch!=null) {
            sqa.addClause("a.cc_code=?");
            sqa.addPar(branch);
        }
        
        if(treatytype!=null){
            sqa.addClause("a.no_surat_hutang like ?");
            sqa.addPar("%"+treatytype.toUpperCase()+"%");
        }
        
        sqa.addFilter(list2.getFilter());
        
        list2 = sqa.getList(InsurancePolicyInwardView.class);
        
        
        return list2;
    }
    
    public void setList2(DTOList list2) {
        this.list2 = list2;
    }
    
    public String getInvoiceid() {
        return invoiceid;
    }
    
    public void setInvoiceid(String invoiceid) {
        this.invoiceid = invoiceid;
    }
    
    public String getNosurathutang() {
        return nosurathutang;
    }
    
    public void setNosurathutang(String nosurathutang) {
        this.nosurathutang = nosurathutang;
    }
    
    public String getNosurathutang2() {
        return nosurathutang2;
    }
    
    public void setNosurathutang2(String nosurathutang2) {
        this.nosurathutang2 = nosurathutang2;
    }
    
    public InvoiceInwardTreatyListForm() {
    }
    
    public void initialize() {
        trxId = (String)getAttribute("trx");
        
        if(!trxId.equalsIgnoreCase("50")){
            artrxtype = (ARTransactionTypeView) DTOPool.getInstance().getDTO(ARTransactionTypeView.class, trxId);
        }
        
        if (artrxtype==null){
            if(trxId.equalsIgnoreCase("50"))
                setTitle("SURAT HUTANG");
            else
                setTitle("TRANSACTION");
        }else{
            setTitle(artrxtype.getStDescription());
        }
        
        
        branch = SessionManager.getInstance().getSession().getStBranch();
    }
    
    public String getTrxId() {
        return trxId;
    }
    
    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }
    
    public void clickCreate() throws Exception {
        
        final InvoiceForm iv = (InvoiceForm) newForm("invoiceform", this);
        
        iv.create();
        
        iv.getInvoice().setStARTransactionTypeID(trxId);
        
        iv.onChangeTRXType();
        
        iv.show();
    }
    
    public void clickCreateSuratHutang() throws Exception {
        
        final InvoiceForm iv = (InvoiceForm) newForm("surathutangform", this);
        
        iv.createSuratHutang();
        
        //iv.getInvoice().setStARTransactionTypeID(trxId);
        
        //iv.onChangeTRXType();
        
        iv.show();
    }
    
    public void clickEditSuratHutang() throws Exception {
        final InvoiceForm iv = (InvoiceForm) newForm("surathutangform", this);
        
        iv.editSuratHutang(nosurathutang);
        
        iv.show();
        
    }
    
    public void clickViewSuratHutang() throws Exception {
        final InvoiceForm iv = (InvoiceForm) newForm("surathutangform", this);
        
        iv.viewSuratHutang(nosurathutang);
        
        iv.show();
    }
    
    public void clickEdit() throws Exception {
        final InvoiceForm iv = (InvoiceForm) newForm("invoiceform", this);
        
        iv.edit(invoiceid);
        
        iv.show();
        
    }
    
    public void clickView() throws Exception {
        final InvoiceForm iv = (InvoiceForm) newForm("invoiceform", this);
        
        iv.view(invoiceid);
        
        iv.show();
    }
    
    public void clickApprove() throws Exception {
        final InvoiceForm iv = (InvoiceForm) newForm("invoiceform", this);
        
        iv.approve(invoiceid);
        
        iv.setApprovedMode(true);
        
        iv.show();
    }
    
    public boolean isCanApproved() {
        return canApproved;
    }
    
    public void setCanApproved(boolean canApproved) {
        this.canApproved = canApproved;
    }
    
    public void clickCreateDLA() throws Exception {
        final InvoiceForm iv = (InvoiceForm) newForm("invoiceform", this);
        
        iv.editCreateDLA(invoiceid);
        
        iv.show();
        
    }
    
    public void clickCreateInwardTreaty() throws Exception {
        
        final InvoiceInwardTreatyForm iv = (InvoiceInwardTreatyForm) newForm("invoiceinwardtreatyform", this);
        
        iv.create();
        
        iv.getInvoice().setStARTransactionTypeID(trxId);
        
        iv.setHeader();
        
        iv.onNewDetails();
        
        iv.show();
    }
    
    public void clickEditInwardTreaty() throws Exception {
        final InvoiceInwardTreatyForm iv = (InvoiceInwardTreatyForm) newForm("invoiceinwardtreatyform", this);
        
        iv.editInwardTreaty(invoiceid);
        
        iv.show();
        
    }
    
    public void clickViewInwardTreaty() throws Exception {
        final InvoiceInwardTreatyForm iv = (InvoiceInwardTreatyForm) newForm("invoiceinwardtreatyform", this);
        
        iv.viewInwardTreaty(invoiceid);
        
        iv.show();
    }
    
    public void clickApproveInwardTreaty() throws Exception {
        final InvoiceInwardTreatyForm iv = (InvoiceInwardTreatyForm) newForm("invoiceinwardtreatyform", this);
        
        iv.approve(invoiceid);
        
        iv.setApprovedMode(true);
        
        iv.show();
    }
    
    public String getStEntityID() {
        return stEntityID;
    }
    
    public void setStEntityID(String stEntityID) {
        this.stEntityID = stEntityID;
    }
    
    public String getStEntityName() {
        return stEntityName;
    }
    
    public void setStEntityName(String stEntityName) {
        this.stEntityName = stEntityName;
    }
    
    public Date getTransdatefrom() {
        return transdatefrom;
    }
    
    public void setTransdatefrom(Date transdatefrom) {
        this.transdatefrom = transdatefrom;
    }
    
    public Date getTransdateto() {
        return transdateto;
    }
    
    public void setTransdateto(Date transdateto) {
        this.transdateto = transdateto;
    }

    public String getInvoiceno() {
        return invoiceno;
    }

    public void setInvoiceno(String invoiceno) {
        this.invoiceno = invoiceno;
    }
    
    public void clickReApproval() throws Exception {
        
        final SQLUtil S = new SQLUtil();
        
        DTOList listPolicy = null;
        
        listPolicy = ListUtil.getDTOListFromQuery(
                " select * "+
                " from ins_pol_inward a "+
                " where a.ar_trx_type_id = 2 " +
                //" and a.invoice_no in ('KU/R/589/VI/2013') " +
                " and a.ar_invoice_id in (13641,13648,13717,13717,13718,13718,13888,13889,13569,13569,"
                + "13570,13570,13642,13644,13647,13649,13651,13687,13693,13704,13704,13643,13645,13646,"
                + "13650,13690,13697,13531)"+
                //" and date_trunc('day',a.mutation_date) >= '2014-01-01 00:00:00' " +
                //" and date_trunc('day',a.mutation_date) <= '2014-01-31 00:00:00' "+
                " order by ar_invoice_id ",
                InsurancePolicyInwardView.class);
        
        for (int i = 0; i < listPolicy.size(); i++) {
            InsurancePolicyInwardView policy = (InsurancePolicyInwardView) listPolicy.get(i);
            
            final InsurancePolicyInwardView pol = (InsurancePolicyInwardView) DTOPool.getInstance().getDTO(InsurancePolicyInwardView.class, policy.getStARInvoiceID());
            
            final InsurancePolicyInwardView polis = (InsurancePolicyInwardView) pol;
            getRemoteAccountReceivable().reverse(pol);
            
            polis.markUpdate();
            polis.recalculate();
            
            polis.setStPostedFlag("Y");
            polis.setStEffectiveFlag("Y");
            getRemoteAccountReceivable().saveInwardTreaty(polis);
            //logger.logDebug("+++++++ UPDATE STATUS POLIS : "+ policy.getStARInvoiceID() +" ++++++++++++++++++");
            
        }
        
    }

    public void clickUpdateKursBulananTreaty() throws Exception {

        if(transdatefrom==null) throw new RuntimeException("Tanggal awal mutasi tidak boleh kosong");

        if(transdateto==null) throw new RuntimeException("Tanggal akhir mutasi tidak boleh kosong");
        
        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;

        listPolicy = ListUtil.getDTOListFromQuery(
                " select * "+
                " from ins_pol_inward a "+
                " where a.ar_trx_type_id = 2 " +
                " and date_trunc('day',a.mutation_date) >= ? " +
                " and date_trunc('day',a.mutation_date) <= ? "+
                " and coalesce(posted_flag,'N') <> 'Y' and ccy <> 'IDR' "+
                " order by ar_invoice_id ",
                new Object[]{transdatefrom, transdateto},
                InsurancePolicyInwardView.class);

        for (int i = 0; i < listPolicy.size(); i++) {
            InsurancePolicyInwardView policy = (InsurancePolicyInwardView) listPolicy.get(i);

            final InsurancePolicyInwardView polis = getRemoteAccountReceivable().getARInvoiceInwardTreaty(policy.getStARInvoiceID());

            polis.markUpdate();

            for (int j = 0; j < polis.getDetailsInwardTreaty().size(); j++) {
                InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) polis.getDetailsInwardTreaty().get(j);

                ivd.markUpdate();

                final DTOList det = ivd.getDetails();

                for (int k = 0; k < det.size(); k++) {
                    InsurancePolicyInwardDetailView detail = (InsurancePolicyInwardDetailView) det.get(k);

                    detail.markUpdate();
                }

                ivd.getDetails().markAllUpdate();

            }

            DTOList cekKurs = null;

            cekKurs = ListUtil.getDTOListFromQuery(
                      "select ccy_rate from gl_ccy_history where ccy_code=? and period_start <= ? and period_end >= ? order by ccy_hist_id desc limit 1",
                      new Object [] {polis.getStCurrencyCode(), polis.getDtMutationDate(), polis.getDtMutationDate()},
                      GLCurrencyHistoryView.class);


            if(cekKurs.size() == 0)
                 throw new RuntimeException("No Bukti : "+ policy.getStInvoiceNo() +" Kurs "+ polis.getStCurrencyCode()+" tidak ditemukan, konfirmasi ke keuangan");


            //if(CurrencyManager.getInstance().getRate(polis.getStCurrencyCode(), polis.getDtMutationDate())==null)
                //throw new RuntimeException("Kurs "+ polis.getStCurrencyCode()+" tidak ditemukan, konfirmasi ke keuangan");

            
            polis.setDbCurrencyRate(CurrencyManager.getInstance().getRate(polis.getStCurrencyCode(), polis.getDtMutationDate()));

            polis.recalculateInwardTreaty();

            //polis.setStPostedFlag("Y");
            //polis.setStEffectiveFlag("Y");
            getRemoteAccountReceivable().saveInwardTreaty(polis);
            
            

        }

    }

    public void clickReverseKursBulananTreaty() throws Exception {

        if(transdatefrom==null) throw new RuntimeException("Tanggal awal mutasi tidak boleh kosong");

        if(transdateto==null) throw new RuntimeException("Tanggal akhir mutasi tidak boleh kosong");

        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;

        listPolicy = ListUtil.getDTOListFromQuery(
                " select * "+
                " from ins_pol_inward a "+
                " where a.ar_trx_type_id = 2 " +
                " and date_trunc('day',a.mutation_date) >= ? " +
                " and date_trunc('day',a.mutation_date) <= ? "+
                " and coalesce(posted_flag,'N') = 'Y' and ccy <> 'IDR' "+
                " order by ar_invoice_id ",
                new Object[]{transdatefrom, transdateto},
                InsurancePolicyInwardView.class);

        for (int i = 0; i < listPolicy.size(); i++) {
            InsurancePolicyInwardView policy = (InsurancePolicyInwardView) listPolicy.get(i);

            final InsurancePolicyInwardView polis = getRemoteAccountReceivable().getARInvoiceInwardTreaty(policy.getStARInvoiceID());

            getRemoteAccountReceivable().reverse(polis);

        }

    }

    public void clickCopyInwardTreaty() throws Exception {
        final InvoiceInwardTreatyForm iv = (InvoiceInwardTreatyForm) newForm("invoiceinwardtreatyform", this);

        iv.copyInwardTreaty(invoiceid);

        iv.show();

    }

    public void clickEndorseInwardTreaty() throws Exception {
        final InvoiceInwardTreatyForm iv = (InvoiceInwardTreatyForm) newForm("invoiceinwardtreatyform", this);

        iv.endorseInwardTreaty(invoiceid);

        iv.show();

    }

    public void clickCreateInwardTreatyUpload() throws Exception {

        final InvoiceInwardTreatyForm iv = (InvoiceInwardTreatyForm) newForm("inwardtreatyuploadform", this);

        iv.create();

        iv.getInvoice().setStARTransactionTypeID(trxId);

        iv.setHeader();

        //iv.onNewDetails();

        iv.show();
    }

}
