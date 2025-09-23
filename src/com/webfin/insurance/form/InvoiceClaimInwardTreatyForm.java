/***********************************************************************
 * Module:  com.webfin.ar.forms.InvoiceForm
 * Author:  Denny Mahendra
 * Created: Jan 8, 2006 11:02:38 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.form;

import com.crux.web.form.Form;
import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.LOV;
import com.crux.util.ListUtil;
import com.webfin.FinCodec;
//import com.webfin.ar.model.InsurancePolicyInwardView;
//import com.webfin.ar.model.InsurancePolicyInwardDetailView;
import com.webfin.ar.model.ARTransactionLineView;
import com.webfin.ar.model.ARTransactionTypeView;
import com.webfin.ar.ejb.AccountReceivableHome;
import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.entity.model.EntityView;
import com.webfin.gl.ejb.CurrencyManager;
import com.crux.util.SQLAssembler;
import com.crux.pool.DTOPool;
import com.crux.util.LogManager;

import com.webfin.insurance.model.InsurancePolicyInwardView;
import com.webfin.insurance.model.InsurancePolicyInwardDetailView;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.math.BigDecimal;

public class InvoiceClaimInwardTreatyForm extends Form {
   private InsurancePolicyInwardView invoice;
   private String parentTrxID;
   private DTOList list;
   private String stEntityID;
   private boolean cek = false;
   private boolean approvedMode;
   private boolean isClose = false;
   private String detailindex;
   
   private final static transient LogManager logger = LogManager.getInstance(InvoiceForm.class);

   private AccountReceivable getRemoteAccountReceivable() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((AccountReceivableHome) JNDIUtil.getInstance().lookup("AccountReceivableEJB",AccountReceivableHome.class.getName()))
            .create();
   }

   public LOV getLovARTrxType() throws Exception {
      return ListUtil.getDTOListFromQuery(
              "select ar_trx_type_id,description from ar_trx_type where parent_trx_id = ?",
              new Object [] {parentTrxID},
              ARTransactionTypeView.class
      );
   }

   public DTOList getDetails() {
      return invoice.getDetails();
   }

   public InsurancePolicyInwardView getInvoice() {
      return invoice;
   }

   public void setInvoice(InsurancePolicyInwardView invoice) {
      this.invoice = invoice;
   }

   public InvoiceClaimInwardTreatyForm() {
   }

   public void clickCancel() {
      setIsClose(true);
      close();
   }
   
   public void changeEntity() throws Exception{
   		getList();
   }

   public void clickSave() throws Exception {
        invoice.validate2();

        getRemoteAccountReceivable().saveSaldoAwalInward(invoice);
        close();
    }
   
   public void clickSaveSuratHutang() throws Exception {
   	  invoice.setList(list);
   	  //invoice.markUpdate();
      getRemoteAccountReceivable().saveSuratHutangInward(invoice,list);
      close();
   }

   public void create() throws Exception {
      invoice = new InsurancePolicyInwardView();
      invoice.setDetails(new DTOList());
      invoice.markNew();

      invoice.setStCurrencyCode(CurrencyManager.getInstance().getMasterCurrency());
      invoice.setDbCurrencyRate(new BigDecimal(1));
   }
   
   public DTOList getList() throws Exception {
	  //getDetails();
      if (list==null) {
         list=new DTOList();

         list.getFilter().activate();
      }

      final SQLAssembler sqa = new SQLAssembler();
      
      /*
       *select *
from 
ar_invoice a,ent_master b,ins_policy c where b.ent_id = a.ent_id and a.attr_pol_no = c.pol_no
 and refid0 like 'REINS%' and upper(b.ent_id) like '200' and attr_pol_per_0>= and attr_pol_per_1<=
order by a.create_date desc
      */

      sqa.addSelect("*");
      sqa.addQuery(
              "   from " +
              "      ar_invoice a," +
              "         ent_master b,ins_policy c");
      sqa.addClause(" b.ent_id = a.ent_id and a.attr_pol_no = c.pol_no and refid0 like 'REINS%'");
      sqa.addOrder("a.create_date desc");

	  if (invoice.getStEntityID()!=null) {
         sqa.addClause("upper(b.ent_id) like ?");
         sqa.addPar(invoice.getStEntityID().toUpperCase());
      }
      
      if(invoice.getStNoSuratHutang()!=null){
      	  sqa.addClause("a.no_surat_hutang = ?");
          sqa.addPar(invoice.getStNoSuratHutang().toUpperCase());
      }
      
//      sqa.addClause("c.policy_date>=?");
//      sqa.addPar(DateUtil.dateBracketLow(invoice.getDtSuratHutangPeriodFrom()));
//      
//      sqa.addClause("c.policy_date<=?");
//      sqa.addPar(DateUtil.dateBracketLow(invoice.getDtSuratHutangPeriodTo()));


      sqa.addFilter(list.getFilter());

      list = sqa.getList(InsurancePolicyInwardView.class);

      return list;
   }
   
   public void setList(DTOList list) {
      this.list = list;
   }
   
   public void createSuratHutang() throws Exception {
      invoice = new InsurancePolicyInwardView();
      invoice.setDetails(new DTOList());
      invoice.markNew();

      //invoice.setStCurrencyCode(CurrencyManager.getInstance().getMasterCurrency());
      //invoice.setDbCurrencyRate(new BigDecimal(1));
   }

   public void edit(String invoiceid) throws Exception {
      view(invoiceid);

      invoice.markUpdate();

      super.setReadOnly(false);

      for (int i = 0; i < invoice.getDetails().size(); i++) {
         InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) invoice.getDetails().get(i);

         ivd.markUpdate();
      }
   }
   
   public void editSuratHutang(String nosurathutang) throws Exception {
      viewSuratHutang(nosurathutang);

	  cek = false;
	  
      invoice.markUpdate();

      super.setReadOnly(false);

   }
   
   public void viewSuratHutang(String nosurathutang) throws Exception {
      invoice = getRemoteAccountReceivable().getSuratHutangInward(nosurathutang);

      super.setReadOnly(true);
      
      cek = true;

      //recalculate();
   }

   public void view(String invoiceid) throws Exception {
      invoice = getRemoteAccountReceivable().getARInvoiceInward(invoiceid);

      super.setReadOnly(true);

      recalculate();
   }

   public void onNewItem() {
       
       final ARTransactionTypeView trx = invoice.getARTrxType();

      if (trx==null) return;

      final DTOList artrxitems = trx.getItems();

      for (int i = 0; i < artrxitems.size(); i++) {
         ARTransactionLineView lt = (ARTransactionLineView) artrxitems.get(i);

         final InsurancePolicyInwardDetailView ivd = new InsurancePolicyInwardDetailView();

         ivd.setStARTrxLineID(lt.getStARTrxLineID());

         ivd.setStDescription(lt.getStItemDesc());
         ivd.setStNegativeFlag(lt.getStNegativeFlag());
         ivd.setStComissionFlag(lt.getStComissionFlag());

         ivd.markNew();

         getDetails().add(ivd);
      }
      
   }

   private String itemIndex;

   public String getItemIndex() {
      return itemIndex;
   }

   public void setItemIndex(String itemIndex) {
      this.itemIndex = itemIndex;
   }

   public void onDeleteItem() {
      getDetails().delete(Integer.parseInt(itemIndex));
   }

   public void onChangeTRXType() {
      final ARTransactionTypeView trx = invoice.getARTrxType();

      if (trx==null) return;

      if (trx.isSuperType()) {
         parentTrxID = trx.getStARTrxTypeID();
         return;
      }

      final DTOList artrxitems = trx.getItems();

      //invoice.setStGLARAccountCode(trx.getStGLARAccount());
      //invoice.setStGLAPAccountCode(trx.getStGLAPAccount());
      invoice.setStInvoiceType(trx.getStInvoiceType());

      for (int i = 0; i < artrxitems.size(); i++) {
         ARTransactionLineView lt = (ARTransactionLineView) artrxitems.get(i);

         final InsurancePolicyInwardDetailView ivd = new InsurancePolicyInwardDetailView();

         //ivd.setStGLAccountCode(lt.getStGLAccount());
         ivd.setStARTrxLineID(lt.getStARTrxLineID());

         ivd.setStDescription(lt.getStItemDesc());
         ivd.setStNegativeFlag(lt.getStNegativeFlag());
         ivd.setStComissionFlag(lt.getStComissionFlag());

         ivd.markNew();

         invoice.getDetails().add(ivd);
      }
      
      boolean isClaim = trx.getStARTrxTypeID().equalsIgnoreCase("17")||trx.getStARTrxTypeID().equalsIgnoreCase("18")
                        ||trx.getStARTrxTypeID().equalsIgnoreCase("19");
      if(isClaim) invoice.setStClaimStatus(FinCodec.ClaimStatus.PLA);
          
   }

   public LOV getLOV_Entity() throws Exception {
      return ListUtil.getLookUpFromQuery(
              "select ent_id, ent_name from ent_master where ins_inward_flag = 'Y'"
      );
   }

   public void recalculate() throws Exception {
      invoice.recalculateClaimInwardTreaty();
   }

   public void onChangeCurrency() throws Exception {
      invoice.setDbCurrencyRate(CurrencyManager.getInstance().getRate(invoice.getStCurrencyCode(), invoice.getDtInvoiceDate()));

      invoice.recalculateSaldoAwal();
   }

   public void afterUpdateForm() {
      if(invoice.getStARTransactionTypeID()!=null){
      	//invoice.recalculateSaldoAwal();
          //if(!isClose)
            //invoice.recalculateOld();
      }
      
   }
   
   public void setStEntityID(String stEntityID) {
      this.stEntityID = stEntityID;
   }

   public String getStEntityID() {
      return stEntityID;
   }
   
   public void setCek(boolean cek) {
      this.cek = cek;
   }

   public boolean getCek() {
      return cek;
   }
   
   public String getStEntityName() {
      final EntityView entity = getEntity();

      if (entity!=null) return entity.getStEntityName();

      return null;
   }

   private EntityView getEntity() {
      if (stEntityID==null) return null;
      return (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntityID);
   }

   public boolean trxEnablePolType() {return invoice.getARTrxType()!=null && invoice.getARTrxType().trxEnablePolType();}
   public boolean trxEnablePolis() {return invoice.getARTrxType()!=null && invoice.getARTrxType().trxEnablePolis();}
   public boolean trxEnableUWrit() {return invoice.getARTrxType()!=null && invoice.getARTrxType().trxEnableUWrit();}
   public boolean trxEnableFixedItem() {return invoice.getARTrxType()!=null && invoice.getARTrxType().trxEnableFixedItem();}
   public boolean trxDisableDetail() {return invoice.getARTrxType()!=null && invoice.getARTrxType().trxDisableDetail();}
   public boolean trxEnableReins() {return invoice.getARTrxType()!=null && invoice.getARTrxType().trxEnableReins();}
   
   public void approve(String invoiceid) throws Exception {
        invoice = getRemoteAccountReceivable().getARInvoiceInward(invoiceid);

        super.setReadOnly(true);

        invoice.setStPostedFlag("Y");
        invoice.setStApprovedFlag("Y");
        invoice.setStActiveFlag("Y");
        invoice.setStEffectiveFlag("Y");

        invoice.markUpdate();

        for (int i = 0; i < invoice.getDetails().size(); i++) {
            InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) invoice.getDetails().get(i);

            ivd.markUpdate();
        }

        recalculate();
    }

    public boolean isApprovedMode() {
        return approvedMode;
    }

    public void setApprovedMode(boolean approvedMode) {
        this.approvedMode = approvedMode;
    }

    public boolean isIsClose() {
        return isClose;
    }

    public void setIsClose(boolean isClose) {
        this.isClose = isClose;
    }
    
    public void editCreateDLA(String invoiceid) throws Exception {
      view(invoiceid);
      
      if(!invoice.getStClaimStatus().equalsIgnoreCase(FinCodec.ClaimStatus.PLA))
          throw new RuntimeException("DLA hanya bisa dibuat dari PLA");

      invoice.markNew();

      super.setReadOnly(false);

      for (int i = 0; i < invoice.getDetails().size(); i++) {
         InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) invoice.getDetails().get(i);

         ivd.markNew();
      }
      
      invoice.setStClaimStatus(FinCodec.ClaimStatus.DLA);
      invoice.setStPostedFlag(null);
      invoice.setStApprovedFlag(null);
          
   }
    
    public void recalculateInwardTreaty() throws Exception {
      invoice.recalculateInwardTreaty();
   }
    
    public void clickSaveInwardTreaty() throws Exception {
      recalculateInwardTreaty();
      //getRemoteAccountReceivable().saveInwardTreaty(invoice);
      close();
   }
    
    public void onChangeTRXTypeInwardTreaty() {
      final ARTransactionTypeView trx = invoice.getARTrxType();

      if (trx==null) return;

      if (trx.isSuperType()) {
         parentTrxID = trx.getStARTrxTypeID();
         return;
      }

      final DTOList artrxitems = trx.getItems();
     
      invoice.setStInvoiceType(trx.getStInvoiceType());
      
      final InsurancePolicyInwardDetailView ivdHeader = new InsurancePolicyInwardDetailView();
      
      ivdHeader.markNew();

      invoice.getDetails().add(ivdHeader);
      
      final InsurancePolicyInwardDetailView dtl = (InsurancePolicyInwardDetailView) invoice.getDetails().get(Integer.parseInt(itemIndex));

      for (int i = 0; i < artrxitems.size(); i++) {
         ARTransactionLineView lt = (ARTransactionLineView) artrxitems.get(i);

         final InsurancePolicyInwardDetailView ivd = new InsurancePolicyInwardDetailView();

         ivd.setStARTrxLineID(lt.getStARTrxLineID());

         ivd.setStDescription(lt.getStItemDesc());
         ivd.setStNegativeFlag(lt.getStNegativeFlag());
         ivd.setStComissionFlag(lt.getStComissionFlag());

         ivd.markNew();

         invoice.getDetails().add(ivd);
      }
      
      boolean isClaim = trx.getStARTrxTypeID().equalsIgnoreCase("17")||trx.getStARTrxTypeID().equalsIgnoreCase("18")
                        ||trx.getStARTrxTypeID().equalsIgnoreCase("19");
      if(isClaim) invoice.setStClaimStatus(FinCodec.ClaimStatus.PLA);
          
   }
    
    public void setHeader(){
        final ARTransactionTypeView trx = invoice.getARTrxType();

        if (trx==null) return;
      
        invoice.setStInvoiceType(trx.getStInvoiceType());
    }
    
    public void onNewDetails() {
      
      final InsurancePolicyInwardDetailView ivdHeader = new InsurancePolicyInwardDetailView();
      
      ivdHeader.markNew();

      invoice.getDetails().add(ivdHeader);
      
      itemIndex = String.valueOf(invoice.getDetails().size()-1);
      
      onExpandDetails();
   }
    
    public void onExpandDetails() {
      
      final InsurancePolicyInwardDetailView dtl = (InsurancePolicyInwardDetailView) invoice.getDetails().get(Integer.parseInt(itemIndex));

      final ARTransactionTypeView trx = invoice.getARTrxType();
      final DTOList artrxitems = trx.getItems();
      
      for (int i = 0; i < artrxitems.size(); i++) {
         ARTransactionLineView lt = (ARTransactionLineView) artrxitems.get(i);

         final InsurancePolicyInwardDetailView ivd = new InsurancePolicyInwardDetailView();

         ivd.setStARTrxLineID(lt.getStARTrxLineID());

         ivd.setStDescription(lt.getStItemDesc());
         ivd.setStNegativeFlag(lt.getStNegativeFlag());
         ivd.setStComissionFlag(lt.getStComissionFlag());

         ivd.markNew();

         dtl.getDetails().add(ivd);
      }
          
   }

    public String getDetailindex() {
        return detailindex;
    }

    public void setDetailindex(String detailindex) {
        this.detailindex = detailindex;
    }
    
    public void editInwardTreaty(String invoiceid) throws Exception {
      viewInwardTreaty(invoiceid);

      invoice.markUpdate();

      super.setReadOnly(false);

      for (int i = 0; i < invoice.getDetailsInwardTreaty().size(); i++) {
         InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) invoice.getDetailsInwardTreaty().get(i);

         ivd.markUpdate();
         
         final DTOList det = ivd.getDetails();
         
         for (int j = 0; j < det.size(); j++) {
            InsurancePolicyInwardDetailView detail = (InsurancePolicyInwardDetailView) det.get(j);
            
            detail.markUpdate();
         }
         
         ivd.getDetails().markAllUpdate();
         
      }
   }
    
    public void viewInwardTreaty(String invoiceid) throws Exception {
      //invoice = getRemoteAccountReceivable().getARInvoiceInwardTreaty(invoiceid);

      super.setReadOnly(true);

      recalculateInwardTreaty();
   }
    
    public void editCreateFromInward(String invoiceid) throws Exception {
      view(invoiceid);
      
      //if(!invoice.getStClaimStatus().equalsIgnoreCase(FinCodec.ClaimStatus.PLA))
          //throw new RuntimeException("DLA hanya bisa dibuat dari PLA");

      invoice.markNew();

      super.setReadOnly(false);

      invoice.setDetails(null);
      invoice.setDetails(new DTOList());

      invoice.setStCurrencyCode(CurrencyManager.getInstance().getMasterCurrency());
      invoice.setDbCurrencyRate(new BigDecimal(1));
      
      invoice.setStClaimStatus(FinCodec.ClaimStatus.PLA);
      invoice.setStPostedFlag(null);
      invoice.setStApprovedFlag(null);
          
   }

    public void clickReverse() throws Exception {
        if (!invoice.isApproved()) {
            throw new Exception("Tidak bisa di Reverse karena belum disetujui");
        }

        invoice.validate3();

        //boolean canReverse = DateUtil.getDateStr(invoice.getDtMutationDate(), "yyyyMM").equalsIgnoreCase(DateUtil.getDateStr(new Date(), "yyyyMM"));

        //if (!canReverse) throw new Exception("Tidak bisa di Reverse sudah tutup produksi");

        getRemoteAccountReceivable().reverse(invoice);
        close();
    }
    
   
}
