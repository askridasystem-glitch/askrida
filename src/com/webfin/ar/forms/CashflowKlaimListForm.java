/***********************************************************************
 * Module:  com.webfin.ar.forms.ReceiptListForm
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
import com.webfin.ar.filter.ARReceiptFilter;
import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.ar.ejb.AccountReceivableHome;
import com.webfin.ar.model.ARInvoiceView;
import com.webfin.ar.model.ARReceiptLinesView;
import com.webfin.ar.model.ARReceiptView;
import com.webfin.ar.model.ARAPSettlementView;
import com.webfin.ar.model.ARCashflowView;
import com.webfin.ar.model.ARInvoiceDetailView;
import java.math.BigDecimal;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.util.Date;

public class CashflowKlaimListForm extends Form {
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
   
   private String stLang;
   public String goPrint="N";
   private boolean enableSuperEdit = SessionManager.getInstance().getSession().hasResource("FINANCE_REVERSE");
   private boolean enableApprove = SessionManager.getInstance().getSession().hasResource("SETTLR_APRV");

   private ARAPSettlementView arapSettlementView;
   private String stNotApproved="N";
   
   private String printFlag="N";

   
   
   private final static transient LogManager logger = LogManager.getInstance(ReceiptListForm.class);

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
              "   ar_cashflow a"+
              " left join ent_master b on a.account_entity_id = b.ent_id"+
              " left join s_users c on a.create_who = c.user_id");
	  
	  
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

      list = sqa.getList(ARCashflowView.class);

      return list;
   }
   
   public SQLAssembler getSQA() {
      final SQLAssembler sqa = new SQLAssembler();

      sqa.addSelect("a.*, b.ent_name as entity_name");

      sqa.addQuery("   from " +
              " from " +
              "   ar_cashflow a" +
              "      inner join ent_master b on b.ent_id = a.entity_id");
	  
	  if (stSettlementID==null){
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

   public CashflowKlaimListForm() {
      filter = new ARReceiptFilter();
      filter.activate();
   }

   public void clickCreate() throws Exception {
      final CashflowKlaimForm form = (CashflowKlaimForm)newForm("cashflowclaim_form", this);

      form.createNew();

      form.show();

   };
   
   public void clickCreate2() throws Exception {
      final ReceiptForm form = (ReceiptForm)newForm("policy_pl", this);

      form.createNew2(stSettlementID);

      form.show();

   };
   
   public void clickCreateTitipan() throws Exception {
      final ReceiptForm form = (ReceiptForm)newForm("receipttitipanform", this);

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

      final CashflowKlaimForm form = (CashflowKlaimForm)newForm("cashflowclaim_form", this);

      form.edit(receiptID);

      form.show();

   };
   
   public void clickEditPajak() throws Exception {

      final ReceiptForm form = (ReceiptForm)newForm("receiptform3", this);

      form.edit(receiptID);

      form.show();

   };
   
   public void clickEditTitipan() throws Exception {

      final ReceiptForm form = (ReceiptForm)newForm("receipttitipanform", this);

      form.editTitipan(receiptID);

      form.show();

   };
   
   public void clickSuperEdit() throws Exception {

      final ReceiptForm form = (ReceiptForm)newForm("receiptform", this);

      form.superEdit(receiptID);

      form.show();

   };
   

   public void clickView() throws Exception {

      final CashflowKlaimForm form = (CashflowKlaimForm)newForm("cashflowclaim_form", this);

      form.view(receiptID);

      form.show();
   };

   public void clickApprove() throws Exception {
      final CashflowKlaimForm form = (CashflowKlaimForm)newForm("cashflowclaim_form", this);

      form.approve(receiptID);

      form.show();
   }

   public void clickVoid() throws Exception {
      final ReceiptForm form = (ReceiptForm)newForm("receiptform", this);

      form.voids(receiptID);

      form.show();
   }

   public void initialize() {
      super.initialize();    //To change body of overridden methods use File | Settings | File Templates.

      stSettlementID = (String)getAttribute("arsid");

      //final ARAPSettlementView settlement = getSettlement();
      setTitle("Cashflow Pembayaran Klaim");

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
      final ReceiptForm form = (ReceiptForm)newForm("policy_receipt", this);

      form.createNewReceipt(stSettlementID);

      form.show();

   };
   
   public void clickCreateClaim() throws Exception {
      final ReceiptForm form = (ReceiptForm)newForm("policy_claim", this);

      form.createNew2(stSettlementID);

      form.show();

   };
   
   public void clickCreateLetter() throws Exception {
      final ReceiptForm form = (ReceiptForm)newForm("policy_letter", this);

      form.createNew2(stSettlementID);

      form.show();

   };

    public String getStNotApproved() {
        return stNotApproved;
    }

    public void setStNotApproved(String stNotApproved) {
        this.stNotApproved = stNotApproved;
    }

    public void clickCreatePembayaranDataFoxpro() throws Exception {
        
        final SQLUtil S = new SQLUtil();
         
        DTOList listBayar = null;
        listBayar = ListUtil.getDTOListFromQuery(
                " select * from data_bayar_aba "+
                " where proses_flag is null and pol_id is not null and kodeko = '00'"+
                " ORDER BY NOMOR "+
                " limit 90000",
                HashDTO.class);
        
        for (int i = 0; i < listBayar.size(); i++) {
            HashDTO dto = (HashDTO) listBayar.get(i);
 
            String invoice_id = dto.getFieldValueByFieldNameBD("POL_ID").toString();

            logger.logDebug("+++++++++++++ PROSES +++++++++++++++");
            logger.logDebug("pol_id : "+ invoice_id);
            
            ReceiptForm form = new ReceiptForm();
            
            form.createNewTes("1");

            form.getReceipt().setStReceiptClassID("3");
            if(dto.getFieldValueByFieldNameST("ent_id_gl")==null) continue;
            form.getReceipt().setStAccountEntityID(dto.getFieldValueByFieldNameST("ent_id_gl"));
            if(form.getReceipt().getStAccountEntityID().equalsIgnoreCase("TIDAK ADA"))
                form.getReceipt().setStAccountEntityID(dto.getFieldValueByFieldNameBD("entity_id").toString());
            
            form.getReceipt().setStCostCenterCode(dto.getFieldValueByFieldNameST("nopol").substring(4,6));
            form.getReceipt().setStCurrencyCode("IDR");
            form.getReceipt().setDtReceiptDate(dto.getFieldValueByFieldNameDT("tglbay"));
            logger.logDebug("nopol : "+ dto.getFieldValueByFieldNameST("nopol"));
            logger.logDebug("cost center : "+ dto.getFieldValueByFieldNameST("nopol").substring(4,6));
            ARInvoiceView inv = getRemoteAccountReceivable().getARInvoiceUsingPolID(invoice_id);
            
            if(inv==null) continue;
            
            form.onNewInvoiceByInvoiceID(inv.getStARInvoiceID());
            
            BigDecimal komisi_a = BDUtil.sub(dto.getFieldValueByFieldNameBD("komisia"),dto.getFieldValueByFieldNameBD("pajaka"));
            BigDecimal bayar_komisi_a = dto.getFieldValueByFieldNameBD("bayarkoma");
            BigDecimal komisi_b = BDUtil.sub(dto.getFieldValueByFieldNameBD("komisib"),dto.getFieldValueByFieldNameBD("pajakb"));
            BigDecimal bayar_komisi_b = dto.getFieldValueByFieldNameBD("bayarkomb");
            BigDecimal komisi_c = BDUtil.sub(dto.getFieldValueByFieldNameBD("komisic"),dto.getFieldValueByFieldNameBD("pajakc"));
            BigDecimal bayar_komisi_c = dto.getFieldValueByFieldNameBD("bayarkomc");
            
            String nobukti_bay = dto.getFieldValueByFieldNameST("nobukbay")!=null?dto.getFieldValueByFieldNameST("nobukbay"):null;
            String nobukti_kom_a = dto.getFieldValueByFieldNameST("nobukkoma")!=null?dto.getFieldValueByFieldNameST("nobukkoma"):null;
            String nobukti_kom_b = dto.getFieldValueByFieldNameST("nobukkomb")!=null?dto.getFieldValueByFieldNameST("nobukkomb"):null;
            String nobukti_kom_c = dto.getFieldValueByFieldNameST("nobukkomc")!=null?dto.getFieldValueByFieldNameST("nobukkomc"):null;
            
            DTOList det = form.getReceipt().getDetails();
            for(int k=0; k < det.size(); k++){
                ARReceiptLinesView line = (ARReceiptLinesView) det.get(k);
                
                line.setStCommitFlag("Y");
                
                DTOList lineDetail = line.getDetails();
                for(int j=0; j < lineDetail.size(); j++){
                    ARReceiptLinesView lineDet = (ARReceiptLinesView) lineDetail.get(j);
                    
                    lineDet.setStCommitFlag("Y");
                    
                    if(!lineDet.getInvoiceDetail().isCommission2()&&
                        !lineDet.getInvoiceDetail().isBrokerage2()&&
                            !lineDet.getInvoiceDetail().isHandlingFee2()) continue;
                    
                    lineDet.setStCheck(null);
                    
                    if(nobukti_kom_a!=null)
                        if(nobukti_bay.equalsIgnoreCase(nobukti_kom_a))
                            if(BDUtil.isEqual(komisi_a, lineDet.getDbAmount(), 0))
                                lineDet.setStCheck("Y");
                    
                    if(nobukti_kom_b!=null)
                        if(nobukti_bay.equalsIgnoreCase(nobukti_kom_b))
                            if(BDUtil.isEqual(komisi_b, lineDet.getDbAmount(), 0))
                                lineDet.setStCheck("Y");
                    
                    if(nobukti_kom_c!=null)
                        if(nobukti_bay.equalsIgnoreCase(nobukti_kom_c))
                            if(BDUtil.isEqual(komisi_c, lineDet.getDbAmount(), 0))
                                lineDet.setStCheck("Y");
                }
            }
              form.getReceipt().recalculate();
             
              logger.logDebug("amount applied : "+ form.getReceipt().getDbAmountApplied());
              form.getReceipt().setDbEnteredAmount(form.getReceipt().getDbAmountApplied());

              form.getReceipt().setStPostedFlag("Y");

              form.getReceipt().recalculate();
              
              
              
              form.getReceipt().generateReceiptNo();
              form.getReceipt().setStFoxproReceiptNo(dto.getFieldValueByFieldNameST("nobukbay"));
              
              getRemoteAccountReceivable().savePembayaranPremi(form.getReceipt());
            

            PreparedStatement PS = S.setQuery("update data_bayar_aba set proses_flag ='Y' where pol_id = ?");
            
            PS.setObject(1,invoice_id);

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ invoice_id +" ++++++++++++++++++");
            
            
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

    public boolean isEnableApprove()
    {
        return enableApprove;
    }

    public void setEnableApprove(boolean enableApprove)
    {
        this.enableApprove = enableApprove;
    }
    
    public void clickCreatePembayaranDataFoxpro2() throws Exception {
        
        final SQLUtil S = new SQLUtil();
         
        DTOList listBayar = null;
        listBayar = ListUtil.getDTOListFromQuery(
                " select * from data_bayar_aba2012 "+
                " where proses_flag is null and pol_id is not null and kodeko = '00'"+
                " ORDER BY NOMOR "+
                " ",
                HashDTO.class);
        
        for (int i = 0; i < listBayar.size(); i++) {
            HashDTO dto = (HashDTO) listBayar.get(i);
 
            String invoice_id = dto.getFieldValueByFieldNameBD("POL_ID").toString();

            logger.logDebug("+++++++++++++ PROSES +++++++++++++++");
            logger.logDebug("pol_id : "+ invoice_id);
            
            ReceiptForm form = new ReceiptForm();
            
            form.createNewTes("1");

            form.getReceipt().setStReceiptClassID("3");
            if(dto.getFieldValueByFieldNameST("ent_id_gl")==null) continue;
            form.getReceipt().setStAccountEntityID(dto.getFieldValueByFieldNameST("ent_id_gl"));
            if(form.getReceipt().getStAccountEntityID().equalsIgnoreCase("TIDAK ADA"))
                form.getReceipt().setStAccountEntityID(dto.getFieldValueByFieldNameBD("entity_id").toString());
            
            form.getReceipt().setStCostCenterCode(dto.getFieldValueByFieldNameST("nopol").substring(4,6));
            form.getReceipt().setStCurrencyCode("IDR");
            form.getReceipt().setDtReceiptDate(dto.getFieldValueByFieldNameDT("tglbay"));
            logger.logDebug("nopol : "+ dto.getFieldValueByFieldNameST("nopol"));
            logger.logDebug("cost center : "+ dto.getFieldValueByFieldNameST("nopol").substring(4,6));
            ARInvoiceView inv = getRemoteAccountReceivable().getARInvoiceUsingPolID(invoice_id);
            
            if(inv==null) continue;
            
            form.onNewInvoiceByInvoiceID(inv.getStARInvoiceID());
            
            BigDecimal komisi_a = BDUtil.sub(dto.getFieldValueByFieldNameBD("komisia"),dto.getFieldValueByFieldNameBD("pajaka"));
            BigDecimal bayar_komisi_a = dto.getFieldValueByFieldNameBD("bayarkoma");
            BigDecimal komisi_b = BDUtil.sub(dto.getFieldValueByFieldNameBD("komisib"),dto.getFieldValueByFieldNameBD("pajakb"));
            BigDecimal bayar_komisi_b = dto.getFieldValueByFieldNameBD("bayarkomb");
            BigDecimal komisi_c = BDUtil.sub(dto.getFieldValueByFieldNameBD("komisic"),dto.getFieldValueByFieldNameBD("pajakc"));
            BigDecimal bayar_komisi_c = dto.getFieldValueByFieldNameBD("bayarkomc");
            BigDecimal komisi_d = BDUtil.sub(dto.getFieldValueByFieldNameBD("komisid"),dto.getFieldValueByFieldNameBD("pajakd"));
            BigDecimal bayar_komisi_d = dto.getFieldValueByFieldNameBD("bayarkomd");
            
            String nobukti_bay = dto.getFieldValueByFieldNameST("nobukbay")!=null?dto.getFieldValueByFieldNameST("nobukbay"):null;
            String nobukti_kom_a = dto.getFieldValueByFieldNameST("nobukkoma")!=null?dto.getFieldValueByFieldNameST("nobukkoma"):null;
            String nobukti_kom_b = dto.getFieldValueByFieldNameST("nobukkomb")!=null?dto.getFieldValueByFieldNameST("nobukkomb"):null;
            String nobukti_kom_c = dto.getFieldValueByFieldNameST("nobukkomc")!=null?dto.getFieldValueByFieldNameST("nobukkomc"):null;
            String nobukti_kom_d = dto.getFieldValueByFieldNameST("nobukkomd")!=null?dto.getFieldValueByFieldNameST("nobukkomd"):null;
            
            String komisiASamaPremi = "komisia_sama_premi = null";
            String komisiBSamaPremi = "komisib_sama_premi = null";
            String komisiCSamaPremi = "komisic_sama_premi = null";
            String komisiDSamaPremi = "komisid_sama_premi = null";
            
            DTOList det = form.getReceipt().getDetails();
            for(int k=0; k < det.size(); k++){
                ARReceiptLinesView line = (ARReceiptLinesView) det.get(k);
                
                line.setStCommitFlag("Y");
                
                DTOList lineDetail = line.getDetails();
                for(int j=0; j < lineDetail.size(); j++){
                    ARReceiptLinesView lineDet = (ARReceiptLinesView) lineDetail.get(j);
                    
                    lineDet.setStCommitFlag("Y");
                    
                    if(!lineDet.getInvoiceDetail().isCommission2()&&
                        !lineDet.getInvoiceDetail().isBrokerage2()&&
                            !lineDet.getInvoiceDetail().isHandlingFee2()) continue;
                    
                    lineDet.setStCheck(null);

                    if(nobukti_kom_a!=null)
                        if(nobukti_bay.equalsIgnoreCase(nobukti_kom_a)){
                            if(BDUtil.isEqual(komisi_a, lineDet.getDbAmount(), 0)){
                                lineDet.setStCheck("Y");
                                komisiASamaPremi = "komisia_sama_premi = 'Y'";
                            }
                        }else{
                            komisiASamaPremi = "komisia_sama_premi = null";
                        }
                    
                    if(nobukti_kom_b!=null)
                        if(nobukti_bay.equalsIgnoreCase(nobukti_kom_b)){
                            if(BDUtil.isEqual(komisi_b, lineDet.getDbAmount(), 0)){
                                lineDet.setStCheck("Y");
                                komisiBSamaPremi = "komisib_sama_premi = 'Y'";
                            }
                        }else{
                            komisiBSamaPremi = "komisib_sama_premi = null";
                        }
                    
                    if(nobukti_kom_c!=null)
                        if(nobukti_bay.equalsIgnoreCase(nobukti_kom_c)){
                            if(BDUtil.isEqual(komisi_c, lineDet.getDbAmount(), 0)){
                                lineDet.setStCheck("Y");
                                komisiCSamaPremi = "komisic_sama_premi = 'Y'";
                            }
                        }else{
                            komisiCSamaPremi = "komisic_sama_premi = null";
                        }
                    
                    if(nobukti_kom_d!=null)
                        if(nobukti_bay.equalsIgnoreCase(nobukti_kom_d)){
                            if(BDUtil.isEqual(komisi_d, lineDet.getDbAmount(), 0)){
                                lineDet.setStCheck("Y");
                                komisiDSamaPremi = "komisid_sama_premi = 'Y'";
                            }
                        }else{
                            komisiDSamaPremi = "komisid_sama_premi = null";
                        }

                }
            }
            
          form.getReceipt().recalculate();

          logger.logDebug("amount applied : "+ form.getReceipt().getDbAmountApplied());
          form.getReceipt().setDbEnteredAmount(form.getReceipt().getDbAmountApplied());

          form.getReceipt().setStPostedFlag("Y");

          form.getReceipt().recalculate();



          form.getReceipt().generateReceiptNo();
          form.getReceipt().setStFoxproReceiptNo(dto.getFieldValueByFieldNameST("nobukbay"));

          getRemoteAccountReceivable().savePembayaranPremi(form.getReceipt());
            

          String sql = "update data_bayar_aba2012 set proses_flag ='Y'";
          sql = sql + "," + komisiASamaPremi;
          sql = sql + "," + komisiBSamaPremi;
          sql = sql + "," + komisiCSamaPremi;
          sql = sql + "," + komisiDSamaPremi;
          sql = sql + " where pol_id = ? ";
          
        PreparedStatement PS = S.setQuery(sql);

        PS.setObject(1,invoice_id);

        int j = PS.executeUpdate();

        if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ invoice_id +" ++++++++++++++++++");
            
            
        }
        
        S.release();
        
    }
     
    public void clickCreatePembayaranKomisiFoxpro2() throws Exception {
        
        final SQLUtil S = new SQLUtil();
         
        DTOList listBayar = null;
        listBayar = ListUtil.getDTOListFromQuery(
                " select pol_id,nopol,nobukbay,nobukkomd,komisid,bayarkomd,tglbaykomd,pajakd,"+
                " komisid-pajakd as utang_komisid,* "+
                "      from data_bayar_aba    "+
                 "     where proses_flag = 'Y'  "+
                 "     and nobukbay <> nobukkomd  and proses4 is null and kodeko = '00'"+
                 " ORDER BY NOMOR limit 10000",
                HashDTO.class);
        
        for (int i = 0; i < listBayar.size(); i++) {
            HashDTO dto = (HashDTO) listBayar.get(i);
 
            String invoice_id = dto.getFieldValueByFieldNameBD("POL_ID").toString();

            logger.logWarning("+++++++++++++ PROSES +++++++++++++++");
            logger.logWarning("pol_id : "+ invoice_id);
            
            ReceiptForm form = new ReceiptForm();
            
            form.createNewTes("2");

            form.getReceipt().setStReceiptClassID("3");
            if(dto.getFieldValueByFieldNameST("ent_id_gl")==null) continue;
            form.getReceipt().setStAccountEntityID(dto.getFieldValueByFieldNameST("ent_id_gl"));
            if(form.getReceipt().getStAccountEntityID().equalsIgnoreCase("TIDAK ADA"))
                form.getReceipt().setStAccountEntityID(dto.getFieldValueByFieldNameBD("entity_id").toString());
            
            form.getReceipt().setStCostCenterCode(dto.getFieldValueByFieldNameST("nopol").substring(4,6));
            form.getReceipt().setStCurrencyCode("IDR");
            form.getReceipt().setDtReceiptDate(dto.getFieldValueByFieldNameDT("tglbay"));
            logger.logDebug("nopol : "+ dto.getFieldValueByFieldNameST("nopol"));
            logger.logDebug("cost center : "+ dto.getFieldValueByFieldNameST("nopol").substring(4,6));
            
            //GET KOMISI POLIS
            
            
            ARInvoiceView inv = getARInvoiceUsingPolID(invoice_id,dto.getFieldValueByFieldNameBD("utang_komisid"));
            
            if(inv==null) continue;
            
            form.onNewInvoicePembayaranKomisiFoxpro(inv.getStARInvoiceID());
            
            /*
            BigDecimal komisi_a = BDUtil.sub(dto.getFieldValueByFieldNameBD("komisia"),dto.getFieldValueByFieldNameBD("pajaka"));
            BigDecimal bayar_komisi_a = dto.getFieldValueByFieldNameBD("bayarkoma");
            BigDecimal komisi_b = BDUtil.sub(dto.getFieldValueByFieldNameBD("komisib"),dto.getFieldValueByFieldNameBD("pajakb"));
            BigDecimal bayar_komisi_b = dto.getFieldValueByFieldNameBD("bayarkomb");
            BigDecimal komisi_c = BDUtil.sub(dto.getFieldValueByFieldNameBD("komisic"),dto.getFieldValueByFieldNameBD("pajakc"));
            BigDecimal bayar_komisi_c = dto.getFieldValueByFieldNameBD("bayarkomc");
            
            String nobukti_bay = dto.getFieldValueByFieldNameST("nobukbay")!=null?dto.getFieldValueByFieldNameST("nobukbay"):null;
            String nobukti_kom_a = dto.getFieldValueByFieldNameST("nobukkoma")!=null?dto.getFieldValueByFieldNameST("nobukkoma"):null;
            String nobukti_kom_b = dto.getFieldValueByFieldNameST("nobukkomb")!=null?dto.getFieldValueByFieldNameST("nobukkomb"):null;
            String nobukti_kom_c = dto.getFieldValueByFieldNameST("nobukkomc")!=null?dto.getFieldValueByFieldNameST("nobukkomc"):null;
            */
            DTOList det = form.getReceipt().getDetails();
            for(int k=0; k < det.size(); k++){
                ARReceiptLinesView line = (ARReceiptLinesView) det.get(k);
                
                line.setStCommitFlag("Y");
                
                DTOList lineDetail = line.getDetails();
                for(int j=0; j < lineDetail.size(); j++){
                    ARReceiptLinesView lineDet = (ARReceiptLinesView) lineDetail.get(j);
                    
                    lineDet.setStCommitFlag("Y");
                    
                } 
            }
              form.getReceipt().recalculatePembayaranKomisi();
             
              logger.logDebug("amount applied : "+ form.getReceipt().getDbAmountApplied());
              form.getReceipt().setDbEnteredAmount(form.getReceipt().getDbAmountApplied());

              form.getReceipt().setStPostedFlag("Y");

              form.getReceipt().recalculatePembayaranKomisi();
              
              form.getReceipt().generateReceiptNo();
              form.getReceipt().setStFoxproReceiptNo(dto.getFieldValueByFieldNameST("nobukkomd"));
              
              getRemoteAccountReceivable().savePembayaranKomisi(form.getReceipt());
            

            PreparedStatement PS = S.setQuery("update data_bayar_aba set proses4 ='KOM_D' where pol_id = ? and kodeko = '00'");
            
            PS.setObject(1,invoice_id);

            int j = PS.executeUpdate();

            if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ invoice_id +" ++++++++++++++++++");
        }
        
        S.release();
        
    }
    
    public ARInvoiceView getARInvoiceUsingPolID(String attrpolid, BigDecimal komisi) throws Exception {
        final ARInvoiceView iv = (ARInvoiceView) ListUtil.getDTOListFromQuery(
                "select * from ar_invoice where attr_pol_id = ? and ar_trx_type_id = 11 and amount = ?",
                new Object [] {attrpolid,komisi},
                ARInvoiceView.class
                ).getDTO();
        
        if(iv!=null) {
            iv.setDetails(
                    ListUtil.getDTOListFromQuery(
                    "select a.* from ar_invoice_details a,ar_invoice b where a.ar_invoice_id = b.ar_invoice_id and b.attr_pol_id = ? and b.amount = ? and b.ar_trx_type_id = 11",
                    new Object [] {attrpolid,komisi},
                    ARInvoiceDetailView.class
                    )
                    
                    );
        }
        
        return iv;
    }

    public String getStLang() {
        return stLang;
    }

    public void setStLang(String stLang) {
        this.stLang = stLang;
    }

    public void clickReverse() throws Exception {

      final CashflowKlaimForm form = (CashflowKlaimForm)newForm("cashflowclaim_form", this);

      form.view(receiptID);

      if (!form.getCashflow().isPosted()) throw new RuntimeException("Pembayaran belum disetujui, tidak bisa di reverse");

      form.setReverseMode(true);

      form.show();
   }

}
