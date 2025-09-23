/***********************************************************************
 * Module:  com.webfin.ar.forms.ReceiptPembayaranRealisasiTitipanListForm
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:42 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.forms;

import com.crux.common.model.Filter;
import com.crux.common.model.HashDTO;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.util.*;
import com.crux.pool.DTOPool;
import com.webfin.FinCodec;
import com.webfin.ar.filter.ARReceiptFilter;
import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.ar.ejb.AccountReceivableHome;
import com.webfin.ar.model.ARInvoiceView;
import com.webfin.ar.model.ARReceiptView;
import com.webfin.ar.model.ARAPSettlementView;
import com.webfin.ar.model.ARReceiptLinesView;
import com.webfin.gl.model.TitipanPremiView;
import java.math.BigDecimal;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.util.Date;

public class ReceiptPembayaranRealisasiTitipanListForm extends Form {
   private DTOList list;
   private ARReceiptFilter filter;
   private String receiptID;
   private String stSettlementID;
   private Date rcpDateFrom;
   private Date rcpDateTo;
   private String receiptNo;
   private String description;
   private String branch;
   private String entity;
   private String printingLOV = "";
   private String printLang;
   public String goPrint="N";
   private boolean enableSuperEdit = SessionManager.getInstance().getSession().hasResource("FINANCE_REVERSE");

   private ARAPSettlementView arapSettlementView;
   private String stNotApproved="N";
   private String printFlag="N";
   private String stLang;
   
   private final static transient LogManager logger = LogManager.getInstance(ReceiptPembayaranRealisasiTitipanListForm.class);

   private boolean enableApprovePolisKhusus = SessionManager.getInstance().getSession().hasResource("SETTLR_APPROVE_POLIS_KHUSUS");

    public boolean isEnableApprovePolisKhusus() {
        return enableApprovePolisKhusus;
    }

    public void setEnableApprovePolisKhusus(boolean enableApprovePolisKhusus) {
        this.enableApprovePolisKhusus = enableApprovePolisKhusus;
    }

   public String getReceiptID() {
      return receiptID;
   }

   public void chgBranch() {
   }

   public void setReceiptID(String receiptID) {
      this.receiptID = receiptID;
   }

   private AccountReceivable getRemoteAccountReceivable() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((AccountReceivableHome) JNDIUtil.getInstance().lookup("AccountReceivableEJB",AccountReceivableHome.class.getName()))
            .create();
   }

   public DTOList getList() throws Exception {
      final SQLAssembler sqa = new SQLAssembler();
      
      if (list==null) {
            list=new DTOList();
            final Filter filter = new Filter();
            //filter.orderKey="root_id desc, create_date desc";
            list.setFilter(filter.activate());
        }

      sqa.addSelect("a.*,b.ent_name as entity_name,c.user_name");

      sqa.addQuery(
              " from " +
              "   ar_receipt a"+
              " left join ent_master b on a.account_entity_id = b.ent_id"+
              " left join s_users c on a.create_who = c.user_id");
	  
	  if (stSettlementID==null){
         sqa.addClause("ar_settlement_id = ?");
         sqa.addPar("1");
      }
	  
      if (stSettlementID!=null){
         sqa.addClause("ar_settlement_id = ?");
         sqa.addPar(stSettlementID);
      }

      if (rcpDateFrom!=null){
         sqa.addClause("date_trunc('day',receipt_date)>=?");
         sqa.addPar(rcpDateFrom);
      }

      if (rcpDateTo!=null){
         sqa.addClause("date_trunc('day',receipt_date)<=?");
         sqa.addPar(rcpDateTo);
      }

      if(receiptNo!=null) {
         sqa.addClause("upper(receipt_no) like ?");
         sqa.addLike(receiptNo);
      }

      if(description!=null) {
         sqa.addClause("upper(description) like ?");
         sqa.addLike(description);
      }

      if(branch!=null) {
         sqa.addClause("a.cc_code = ?");
         sqa.addPar(branch);
      }

      if(entity!=null) {
         sqa.addClause("upper(b.ent_name) like ?");
         sqa.addLike(entity);
      }
      
      if(Tools.isYes(stNotApproved)){
          sqa.addClause("coalesce(posted_flag,'') <> 'Y'");
      }

        if(Tools.isYes(getPrintFlag())){
            sqa.addClause("a.print_flag = 'Y'");
        }

      sqa.addClause("a.idr_flag = 'Y'");
        
        //if(Tools.isNo(getPrintFlag())){
           // sqa.addClause("a.print_flag is null");
        //}
      
      sqa.addFilter(list.getFilter());
      
      sqa.addOrder("a.create_date desc");

      sqa.setLimit(200);

      list = sqa.getList(ARReceiptView.class);

      return list;
   }
   
   public SQLAssembler getSQA() {
      final SQLAssembler sqa = new SQLAssembler();

      sqa.addSelect("a.*, b.ent_name as entity_name");

      sqa.addQuery("   from " +
              " from " +
              "   ar_receipt a" +
              "      inner join ent_master b on b.ent_id = a.entity_id");
	  
	  if (stSettlementID==null){
         sqa.addClause("ar_settlement_id = ?");
         sqa.addPar("1  ");
      }
	  
      if (stSettlementID!=null){
         sqa.addClause("ar_settlement_id = ?");
         sqa.addPar(stSettlementID);
      }

      if (rcpDateFrom!=null){
         sqa.addClause("receipt_date>=?");
         sqa.addPar(DateUtil.dateBracketLow(rcpDateFrom));
      }

      if (rcpDateTo!=null){
         sqa.addClause("receipt_date<?");
         sqa.addPar(DateUtil.dateBracketHigh(rcpDateTo));
      }

      if(receiptNo!=null) {
         sqa.addClause("upper(receipt_no) like ?");
         sqa.addLike(receiptNo);
      }

      if(description!=null) {
         sqa.addClause("upper(description) like ?");
         sqa.addLike(description);
      }

      if(branch!=null) {
         sqa.addClause("a.cc_code = ?");
         sqa.addPar(branch);
      }

      if(entity!=null) {
         sqa.addClause("upper(b.ent_name) like ?");
         sqa.addLike(entity);
      }
      
      return sqa;
   }

   public void setList(DTOList list) {
      this.list = list;
   }

   public ARReceiptFilter getFilter() {
      return filter;
   }

   public void setFilter(ARReceiptFilter filter) {
      this.filter = filter;
   }

   public ReceiptPembayaranRealisasiTitipanListForm() {
      filter = new ARReceiptFilter();
      filter.activate();
   }

   public void clickCreate() throws Exception {
      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptpembayaranrealisasititipanform", this);

      form.createNew(stSettlementID);

      form.show();

   };
   
   public void clickCreatePajak() throws Exception {
      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("ReceiptPembayaranRealisasiTitipanForm3", this);

      form.createNew(stSettlementID);

      form.show();

   };
   
   public void clickCreate2() throws Exception {
      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("policy_pl", this);

      form.createNew2(stSettlementID);

      form.show();

   };
   
   public void clickCreateTitipan() throws Exception {
      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receipttitipanform", this);

      form.createNew(stSettlementID);

      form.show();

   };

   public void clickRefresh() {
   }
   
   public void btnSearch() throws Exception {
      getList().getFilter().setCurrentPage(0);
   }
   
   public void btnPrint() throws Exception{
   	   goPrint = "Y";
   }

   public void clickEdit() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptpembayaranrealisasititipanform", this);

      form.edit(receiptID);

      form.show();

   };
   
   public void clickEditPajak() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("ReceiptPembayaranRealisasiTitipanForm3", this);

      form.edit(receiptID);

      form.show();

   };
   
   public void clickEditTitipan() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receipttitipanform", this);

      form.editTitipan(receiptID);

      form.show();

   };
   
   public void clickSuperEdit() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("ReceiptPembayaranRealisasiTitipanForm", this);

      form.superEdit(receiptID);

      form.show();

   };
   
   public void clickSuperEditPajak() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("ReceiptPembayaranRealisasiTitipanForm3", this);

      form.superEdit(receiptID);

      form.show();

   };

   public void clickView() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptpembayaranrealisasititipanform", this);

      form.view(receiptID);

      form.show();
   };
   
   public void clickViewPajak() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("ReceiptPembayaranRealisasiTitipanForm3", this);

      form.view(receiptID);

      form.show();
   };

   public void clickApprove() throws Exception {
      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptpembayaranrealisasititipanform", this);

      form.approve(receiptID);

      form.show();
   }
   
   public void clickApprovePajak() throws Exception {
      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("ReceiptPembayaranRealisasiTitipanForm3", this);

      form.approve(receiptID);

      form.show();
   }

   public void clickVoid() throws Exception {
      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("ReceiptPembayaranRealisasiTitipanForm", this);

      form.voids(receiptID);

      form.show();
   }

   public void initialize() {
      super.initialize();    //To change body of overridden methods use File | Settings | File Templates.

      stSettlementID = (String)getAttribute("arsid");

      final ARAPSettlementView settlement = getSettlement();
      setTitle(settlement==null?"Settlement":settlement.getStDescription());

      branch = SessionManager.getInstance().getSession().getStBranch();
   }

   private ARAPSettlementView getSettlement() {
      if (arapSettlementView==null)
      arapSettlementView = (ARAPSettlementView) DTOPool.getInstance().getDTO(ARAPSettlementView.class, stSettlementID);

      return arapSettlementView;
   }

   public String getStSettlementID() {
      return stSettlementID;
   }

   public void setStSettlementID(String stSettlementID) {
      this.stSettlementID = stSettlementID;
   }

   public Date getRcpDateFrom() {
      return rcpDateFrom;
   }

   public void setRcpDateFrom(Date rcpDateFrom) {
      this.rcpDateFrom = rcpDateFrom;
   }

   public Date getRcpDateTo() {
      return rcpDateTo;
   }

   public void setRcpDateTo(Date rcpDateTo) {
      this.rcpDateTo = rcpDateTo;
   }

   public String getReceiptNo() {
      return receiptNo;
   }

   public void setReceiptNo(String receiptNo) {
      this.receiptNo = receiptNo;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getBranch() {
      return branch;
   }

   public void setBranch(String branch) {
      this.branch = branch;
   }

   public String getEntity() {
      return entity;
   }

   public void setEntity(String entity) {
      this.entity = entity;
   }
   
   public String getPrintingLOV() {
      return printingLOV;
   }
   
   public String getPrintLang() {
      return printLang;
   }

   public void setPrintLang(String printLang) {
      this.printLang = printLang;
   }
   
   public boolean isEnableSuperEdit() {
      return enableSuperEdit;
   }
   
   public void clickCreateReceipt() throws Exception {
      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("policy_receipt", this);

      form.createNewReceipt(stSettlementID);

      form.show();

   };
   
   public void clickCreateClaim() throws Exception {
      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("policy_claim", this);

      form.createNew2(stSettlementID);

      form.show();

   };
   
   public void clickCreateLetter() throws Exception {
      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("policy_letter", this);

      form.createNew2(stSettlementID);

      form.show();

   };
   
   public void clickCreateReas() throws Exception {
      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptpembayaranreasform", this);

      form.createNew(stSettlementID);

      form.show();

   };
   
   public void clickApproveReas() throws Exception {
      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptpembayaranreasform", this);

      form.approve(receiptID);

      form.show();
   }
   
   public void clickEditReas() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptpembayaranreasform", this);

      form.edit(receiptID);

      form.show();

   };
   
   public void clickViewReas() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptpembayaranreasform", this);

      form.view(receiptID);

      form.show();
   };
   
   public void clickSuperEditReas() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptpembayaranreasform", this);

      form.superEdit(receiptID);

      form.show();

   };
   
   public void clickCreatePembayaranKomisi() throws Exception {
      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptpembayarankomisiform", this);

      form.createNew(stSettlementID);

      form.show();

   };
   
   public void clickEditPembayaranKomisi() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptpembayarankomisiform", this);

      form.edit(receiptID);

      form.show();

   };
   
   public void clickViewPembayaranKomisi() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptpembayarankomisiform", this);

      form.view(receiptID);

      form.show();
   };
   
    public void clickApprovePembayaranKomisi() throws Exception {
      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptpembayarankomisiform", this);

      form.approve(receiptID);

      form.show();
   }
    
    public void clickCreatePembayaranPajak() throws Exception {
      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptpembayaranpajakform", this);

      form.createNew(stSettlementID);

      form.show();

   };
   
   public void clickEditPembayaranPajak() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptpembayaranpajakform", this);

      form.edit(receiptID);

      form.show();

   };
   
   public void clickViewPembayaranPajak() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptpembayaranpajakform", this);

      form.view(receiptID);

      form.show();
   };
   
    public void clickApprovePembayaranPajak() throws Exception {
      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptpembayaranpajakform", this);

      form.approve(receiptID);

      form.show();
   }

    public String getStNotApproved() {
        return stNotApproved;
    }

    public void setStNotApproved(String stNotApproved) {
        this.stNotApproved = stNotApproved;
    }

    public void clickCreatePembayaranDataFoxpro() throws Exception {
        
        final SQLUtil S = new SQLUtil();
        
        DTOList listPolicy = null;
        listPolicy = ListUtil.getDTOListFromQuery(
                " select * from data_bayar_aba "+
                " WHERE length(NOPOL) = 16 "+
                " ORDER BY NOMOR "+
                " limit 1",
                HashDTO.class);
        
        for (int i = 0; i < listPolicy.size(); i++) {
            HashDTO dto = (HashDTO) listPolicy.get(i);
            
            String invoice_id = dto.getFieldValueByFieldNameBD("POL_ID").toString();
            
            logger.logDebug("+++++++++++++ PROSES +++++++++++++++");
            logger.logDebug("pol_id : "+ invoice_id);
            
            ReceiptPembayaranRealisasiTitipanForm form = new ReceiptPembayaranRealisasiTitipanForm();
            
            form.createNewTes("1");

            form.getReceipt().setStReceiptClassID("3");
            form.getReceipt().setStAccountEntityID("397");
            form.getReceipt().setStCostCenterCode(dto.getFieldValueByFieldNameST("nopol").substring(4,6));
            form.getReceipt().setStCurrencyCode("IDR");
            logger.logDebug("nopol : "+ dto.getFieldValueByFieldNameST("nopol"));
            logger.logDebug("cost center : "+ dto.getFieldValueByFieldNameST("nopol").substring(4,6));
            ARInvoiceView inv = getRemoteAccountReceivable().getARInvoiceUsingPolID(invoice_id);
            form.onNewInvoiceByInvoiceID(inv.getStARInvoiceID());
            
             logger.logDebug("amount applied : "+ form.getReceipt().getDbAmountApplied());
              form.getReceipt().setDbEnteredAmount(form.getReceipt().getDbAmountApplied());
            
              form.getReceipt().generateReceiptNo();
              final String trxNO = form.getReceipt().getStReceiptNo();
              form.getReceipt().recalculate();
              

              getRemoteAccountReceivable().savePembayaranPremi(form.getReceipt());
            /*
            getRemoteInsurance().save(polis, polis.getStNextStatus(), true);
            
            PreparedStatement PS = S.setQuery("update proses_koas_jiwa_sementara set ref7='Y' where pol_id = ?");
            
            PS.setObject(1,policy.getStPolicyID());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ policy.getStPolicyNo() +" ++++++++++++++++++");
            */
            
        }
        
        S.release();
        
    }
   
    public String getPrintFlag() {
        return printFlag;
    }

    public void setPrintFlag(String printFlag) {
        this.printFlag = printFlag;
    }

    public void refresh() {
        
    }
    
    public void clickCreatePembayaranUangMukaKlaim() throws Exception {
      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("claimform", this);

      form.createNew(stSettlementID);

      form.show();

   };
   
   public void clickEditPembayaranUangMukaKlaim() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("claimform", this);

      form.edit(receiptID);

      form.show();

   };
   
   public void clickViewPembayaranUangMukaKlaim() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("claimform", this);

      form.view(receiptID);

      form.show();
   };
   
   public void clickApprovePembayaranUangMukaKlaim() throws Exception {
      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("claimform", this);

      form.approve(receiptID);

      form.show();
   }
   
   public void clickSuperEditPembayaranUangMukaKlaim() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("claimform", this);

      form.superEdit(receiptID);

      form.show();

   };
   
   public String getStLang() {
        return stLang;
    }

    public void setStLang(String stLang) {
        this.stLang = stLang;
    }

    public void clickReverse() throws Exception {

      final ReceiptForm form = (ReceiptForm)newForm("receiptform", this);

      form.view(receiptID);

      form.cekClosingStatus("REVERSE");

      if (!form.getReceipt().isPosted()) throw new RuntimeException("Pembayaran belum disetujui, tidak bisa di reverse");

      form.setReverseMode(true);

      form.show();
   };
   
   public void clickCreateRealisasiTitipanLainnya() throws Exception {

        final SQLUtil S = new SQLUtil();

        DTOList listBayar = null;
        listBayar = ListUtil.getDTOListFromQuery(
                " select kode_kelompok,proses_flag "+
                " from proses_inject_realisasi "+
                " where proses_flag is null "+
                " group by kode_kelompok,proses_flag "+ 
                " order by kode_kelompok",
                HashDTO.class);

        for (int i = 0; i < listBayar.size(); i++) {
            HashDTO dto = (HashDTO) listBayar.get(i);

            logger.logWarning("+++++++++++++ PROSES +++++++++++++++");

            //LOOPING UNTUK KELOMPOK YANG SAMA
            DTOList kelompok = null;
           kelompok = ListUtil.getDTOListFromQuery(
                " SELECT * "
                + " FROM proses_inject_realisasi "
                + " WHERE kode_kelompok = ? ",
                new Object [] {dto.getFieldValueByFieldNameBD("kode_kelompok").toString()},
                HashDTO.class);

           HashDTO dtoKelompok1 = (HashDTO) kelompok.get(0);

            ReceiptPembayaranRealisasiTitipanForm form = new ReceiptPembayaranRealisasiTitipanForm();

            form.createNewTes("25");
            form.getReceipt().setStReceiptClassID(dtoKelompok1.getFieldValueByFieldNameBD("metode").toString());
            form.getReceipt().setStAccountEntityID(dtoKelompok1.getFieldValueByFieldNameBD("entity_id").toString());
            form.getReceipt().setStCostCenterCode(dtoKelompok1.getFieldValueByFieldNameST("cabang"));
            form.getReceipt().setStCurrencyCode("IDR");
            form.getReceipt().setStMonths(dtoKelompok1.getFieldValueByFieldNameST("bulan"));
            form.getReceipt().setStYears(dtoKelompok1.getFieldValueByFieldNameST("tahun"));

            form.setDate();
            form.onNewLawanTitipan();

            for (int j = 0; j < kelompok.size(); j++) {
                HashDTO dtoKelompok = (HashDTO) kelompok.get(j);

                //GET TITIPAN PREMI
                DTOList titipan = null;
                    titipan = ListUtil.getDTOListFromQuery(
                            "   select *"
                            + " from ar_titipan_premi"
                            + " where trx_no = ? and counter = ?",
                            new Object [] {dtoKelompok.getFieldValueByFieldNameST("no_bukti_titipan"), dtoKelompok.getFieldValueByFieldNameBD("counter").toString()},
                            TitipanPremiView.class);

                TitipanPremiView titip = (TitipanPremiView) titipan.get(0);
                //END GET

                //ADD TITIPAN DETAILS

                  final ARReceiptLinesView rcl = new ARReceiptLinesView();

                  rcl.markNew();

                  ARReceiptLinesView rclTitip = (ARReceiptLinesView) form.getListInvoices().get(Integer.parseInt(form.getInvoicesindex()));
                  ARInvoiceView invTitip = rclTitip.getInvoice();

                  rcl.setStInvoiceID(rclTitip.getStInvoiceID());
                  rcl.setStInvoiceNo(rclTitip.getStInvoiceNo());
                  if(invTitip!=null){
                      rcl.setStPolicyID(invTitip.getStPolicyID());
                      rcl.setStDescription("Titipan Premi "+ invTitip.getStAttrPolicyNo());
                  }

                  rcl.setStCurrencyCode(form.getReceipt().getStCurrencyCode());
                  rcl.setDbCurrencyRate(form.getReceipt().getDbCurrencyRate());
                  rcl.setDbInvoiceAmount(null);
                  rcl.setDbAmount(null);

                  rcl.setStLineType(FinCodec.ARReceiptLineType.TITIPAN);
                  rcl.markCommit();

                  rcl.setStTitipanPremiID(titip.getStTransactionID());
                  rcl.setStDescription(titip.getStDescription());
                  rcl.setDbTitipanPremiAmount(titip.getDbAmount());
                  rcl.setDbTitipanPremiUsedAmount(dtoKelompok.getFieldValueByFieldNameBD("terpakai"));

                  form.getReceipt().setStDescription("REALISASI "+ kelompok.size() +" TITIPAN PREMI");
                  form.getReceipt().setStShortDescription("REALISASI "+ kelompok.size() +" TITIPAN PREMI");

                  rclTitip.getListTitipan().add(rcl);
                //END
            }
            //END LOOPING

              form.getReceipt().recalculateInject();

              logger.logWarning("++++++++++++++++++ amount applied : "+ form.getReceipt().getDbAmountApplied());
              form.getReceipt().setDbEnteredAmount(form.getReceipt().getDbAmountApplied());
              form.getReceipt().setStPostedFlag("Y");

              form.getReceipt().recalculateInject();

              getRemoteAccountReceivable().savePembayaranPremi(form.getReceipt());


            PreparedStatement PS = S.setQuery("update proses_inject_realisasi set proses_flag ='Y' where kode_kelompok = ?");

            PS.setObject(1,dto.getFieldValueByFieldNameBD("kode_kelompok").toString());

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS KELOMPOK : "+ dto.getFieldValueByFieldNameBD("kode_kelompok").toString() +" ++++++++++++++++++");
        }

        S.release();

    }

   public void clickCreatePolisKhusus() throws Exception {
      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptpembayaran_poliskhususform", this);

      form.createNew(stSettlementID);

      form.show();

   };

   public void clickEditPolisKhusus() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptpembayaran_poliskhususform", this);

      form.edit(receiptID);

      form.show();

   };

   public void clickViewPolisKhusus() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptpembayaran_poliskhususform", this);

      form.view(receiptID);

      form.show();
   };

   public void clickApprovePolisKhusus() throws Exception {
      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptpembayaran_poliskhususform", this);

      form.approve(receiptID);

      form.show();
   }

   public void clickReversePolisKhusus() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptpembayaran_poliskhususform", this);

      form.view(receiptID);

      form.cekClosingStatus("REVERSE");

      if (!form.getReceipt().isPosted()) throw new RuntimeException("Pembayaran belum disetujui, tidak bisa di reverse");

      if (form.getReceipt().getStPolicyID()!=null) throw new RuntimeException("Polis Endorse sudah disetujui di Akseptasi, tidak bisa di reverse");

      form.setReverseMode(true);

      form.show();
   };

   public void clickEditNoPolisKhusus() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptpembayaran_poliskhususform", this);

      form.editPolis(receiptID);

      form.show();

   };

   public void clickCreateTPReinsurance() throws Exception {
      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptrealisasi_tpreinsurancesform", this);

      form.createNew(stSettlementID);

      form.show();

   };

   public void clickEditTPReinsurance() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptrealisasi_tpreinsurancesform", this);

      form.edit(receiptID);

      form.show();

   };

   public void clickViewTPReinsurance() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptrealisasi_tpreinsurancesform", this);

      form.view(receiptID);

      form.show();
   };

   public void clickApproveTPReinsurance() throws Exception {
      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptrealisasi_tpreinsurancesform", this);

      form.approve(receiptID);

      form.show();
   }

   public void clickReverseTPReinsurance() throws Exception {

      final ReceiptPembayaranRealisasiTitipanForm form = (ReceiptPembayaranRealisasiTitipanForm)newForm("receiptrealisasi_tpreinsurancesform", this);

      form.view(receiptID);

      form.cekClosingStatus("REVERSE");

      if (!form.getReceipt().isPosted()) throw new RuntimeException("Pembayaran belum disetujui, tidak bisa di reverse");

      form.setReverseMode(true);

      form.show();
   };

}
