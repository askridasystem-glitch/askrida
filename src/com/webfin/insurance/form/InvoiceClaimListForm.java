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
import com.webfin.ar.model.ARInvoiceView;
import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.SQLAssembler;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.pool.DTOPool;
import com.webfin.insurance.model.InsurancePolicyInwardView;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.util.Date;

public class InvoiceClaimListForm extends Form {
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
   private String stFontSize;
   private boolean canApproved = SessionManager.getInstance().getSession().hasResource("AR_TRX_1_APPROVE");

   private String stLang;
   private String stPrintForm;
   private Date transdatefrom;
   private String stEntityID;
   private InsurancePolicyInwardView inward;

    public String getStEntityID() {
        return stEntityID;
    }

    public void setStEntityID(String stEntityID) {
        this.stEntityID = stEntityID;
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
   private Date transdateto;
   
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

        if (list == null) {
            list = new DTOList();

            list.getFilter().activate();
        }

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.*,b.ent_name");
        sqa.addQuery(
                "   from "
                + "      ins_pol_inward a"
                + "         left join ent_master b on b.ent_id = a.ent_id");
        //sqa.addClause("a.active_flag = 'Y'");
        sqa.addOrder("a.create_date desc");

        if (trxId != null) {
            sqa.addClause("(ar_trx_type_id = ? or ar_trx_type_id in (select ar_trx_type_id from ar_trx_type where parent_trx_id = ?))");
            sqa.addPar(trxId);
            sqa.addPar(trxId);
        }

        if (customer != null) {
            sqa.addClause("upper(b.ent_name) like ?");
            sqa.addPar("%" + customer.toUpperCase() + "%");
        }

        if (stEntityID != null) {
            sqa.addClause("b.ent_id = ?");
            sqa.addPar(stEntityID);
        }

        if (trxno != null) {
            sqa.addClause("upper(a.invoice_no) like ?");
            sqa.addPar("%" + trxno.toUpperCase() + "%");
        }

        if (branch != null) {
            sqa.addClause("a.cc_code=?");
            sqa.addPar(branch);
        }

        if (transdatefrom!=null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(transdatefrom);
        }

        if (transdateto!=null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(transdateto);
        }

        sqa.addClause("coalesce(a.active_flag, 'Y') <> 'N'");

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

      list2 = sqa.getList(ARInvoiceView.class);


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

   public InvoiceClaimListForm() {
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

      final InvoiceClaimForm iv = (InvoiceClaimForm) newForm("invoiceclaimform", this);

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
      final InvoiceClaimForm iv = (InvoiceClaimForm) newForm("invoiceclaimform", this);

      iv.edit(invoiceid);

      iv.show();

   }

   public void clickView() throws Exception {
      final InvoiceClaimForm iv = (InvoiceClaimForm) newForm("invoiceclaimform", this);

      iv.view(invoiceid);

      iv.show();
   }
   
   public void clickApprove() throws Exception {
      final InvoiceClaimForm iv = (InvoiceClaimForm) newForm("invoiceclaimform", this);

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
      final InvoiceClaimForm iv = (InvoiceClaimForm) newForm("invoiceclaimform", this);

      iv.editCreateDLA(invoiceid);

      iv.show();

   }
    
    public void clickCreateInwardTreaty() throws Exception {

      final InvoiceForm iv = (InvoiceForm) newForm("invoiceinwardtreatyform", this);

      iv.create();

      iv.getInvoice().setStARTransactionTypeID(trxId);

      iv.setHeader();
      
      iv.onNewDetails();

      iv.show();
   }
    
     public void clickEditInwardTreaty() throws Exception {
      final InvoiceForm iv = (InvoiceForm) newForm("invoiceinwardtreatyform", this);

      iv.editInwardTreaty(invoiceid);

      iv.show();

   }

   public void clickViewInwardTreaty() throws Exception {
      final InvoiceForm iv = (InvoiceForm) newForm("invoiceinwardtreatyform", this);

      iv.viewInwardTreaty(invoiceid);

      iv.show();
   }
   
   public void clickApproveInwardTreaty() throws Exception {
      final InvoiceForm iv = (InvoiceForm) newForm("invoiceinwardtreatyform", this);

      iv.approve(invoiceid);
      
      iv.setApprovedMode(true);

      iv.show();
   }
   
   public String getStFontSize() {
        return stFontSize;
    }

    public void setStFontSize(String stFontSize) {
        this.stFontSize = stFontSize;
    }

    public void clickCreatePLAFromReas() throws Exception {
      final InvoiceClaimForm iv = (InvoiceClaimForm) newForm("invoiceclaimform", this);

      iv.createPLAFromReins(invoiceid);

      iv.getInvoice().setStARTransactionTypeID(trxId);
      //iv.getInvoice().setStRefID0("FAC");
      
      iv.onChangeTRXType();

      iv.show();

   }

    public String getStLang() {
        return stLang;
    }

    public void setStLang(String stLang) {
        this.stLang = stLang;
    }

    public String getStPrintForm() {
        return stPrintForm;
    }

    public void setStPrintForm(String stPrintForm) {
        this.stPrintForm = stPrintForm;
    }

    public void clickCreateLKS() throws Exception {

      final InvoiceClaimForm iv = (InvoiceClaimForm) newForm("invoiceclaimform", this);

      if(invoiceid!=null){

          iv.createPLAFromReins(invoiceid);

          iv.getInvoice().setStARTransactionTypeID(trxId);

          iv.onChangeTRXType();

          iv.show();
      }else{

          iv.create();

          iv.getInvoice().setStARTransactionTypeID(trxId);

          iv.onChangeTRXType();

          iv.show();
      }
    }

    public void clickReverse() throws Exception {
      final InvoiceClaimForm iv = (InvoiceClaimForm) newForm("invoiceclaimform", this);

      iv.view(invoiceid);
      iv.setReverseMode(true);

      iv.show();
   }

    public void clickCreateEndorse() throws Exception {
        final InvoiceClaimForm iv = (InvoiceClaimForm) newForm("invoiceclaimform", this);

        iv.endorse(invoiceid);

        iv.show();

    }

    private InsurancePolicyInwardView getInward() {
        inward = (InsurancePolicyInwardView) DTOPool.getInstance().getDTO(InsurancePolicyInwardView.class, invoiceid);
        return inward;
    }

    public void clickCreateGratia() throws Exception {

        final InvoiceClaimForm iv = (InvoiceClaimForm) newForm("invoiceclaimexgratiaform", this);

        iv.create();

        iv.getInvoice().setStARTransactionTypeID(trxId);

        iv.onChangeTRXType();

        iv.show();
    }

    public void clickEditGratia() throws Exception {
        final InvoiceClaimForm iv = (InvoiceClaimForm) newForm("invoiceclaimexgratiaform", this);

        iv.edit(invoiceid);

        iv.show();

    }

    public void clickViewGratia() throws Exception {
        final InvoiceClaimForm iv = (InvoiceClaimForm) newForm("invoiceclaimexgratiaform", this);

        iv.view(invoiceid);

        iv.show();
    }

    public void clickApproveGratia() throws Exception {

        if (getInward().isEffective()) {
            throw new Exception("Data Sudah Disetujui");
        }

        final InvoiceClaimForm iv = (InvoiceClaimForm) newForm("invoiceclaimexgratiaform", this);

        iv.approve(invoiceid);

        iv.setApprovedMode(true);

        iv.show();
    }

    public void clickCreateSubro() throws Exception {

        final InvoiceClaimForm iv = (InvoiceClaimForm) newForm("invoiceclaimsubroform", this);

        iv.create();

        iv.getInvoice().setStARTransactionTypeID(trxId);

        iv.onChangeTRXType();

        iv.show();
    }

    public void clickEditSubro() throws Exception {
        final InvoiceClaimForm iv = (InvoiceClaimForm) newForm("invoiceclaimsubroform", this);

        iv.edit(invoiceid);

        iv.show();

    }

    public void clickViewSubro() throws Exception {
        final InvoiceClaimForm iv = (InvoiceClaimForm) newForm("invoiceclaimsubroform", this);

        iv.view(invoiceid);

        iv.show();
    }

    public void clickApproveSubro() throws Exception {

        if (getInward().isEffective()) {
            throw new Exception("Data Sudah Disetujui");
        }

        final InvoiceClaimForm iv = (InvoiceClaimForm) newForm("invoiceclaimsubroform", this);

        iv.approve(invoiceid);

        iv.setApprovedMode(true);

        iv.show();
    }

    public void clickEndorseSubro() throws Exception {
        final InvoiceClaimForm iv = (InvoiceClaimForm) newForm("invoiceclaimsubroform", this);

        iv.endorse(invoiceid);

        iv.show();

    }

    private String stAuthorized;

    /**
     * @return the stAuthorized
     */
    public String getStAuthorized() {
        return stAuthorized;
    }

    /**
     * @param stAuthorized the stAuthorized to set
     */
    public void setStAuthorized(String stAuthorized) {
        this.stAuthorized = stAuthorized;
    }

    public void clickEditNoSurat() throws Exception {
      final InvoiceClaimForm iv = (InvoiceClaimForm) newForm("invoiceclaimform", this);

      iv.editNoSurat(invoiceid);

      iv.show();

   }

}
