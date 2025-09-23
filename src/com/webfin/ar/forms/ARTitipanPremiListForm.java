/***********************************************************************
 * Module:  com.webfin.ar.forms.ReceiptListForm
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:42 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.forms;

import com.crux.common.model.Filter;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.util.*;
import com.crux.ff.model.FlexFieldHeaderView;
import com.crux.pool.DTOPool;
import com.webfin.ar.filter.ARReceiptFilter;
import com.webfin.ar.ejb.AccountReceivable;
import com.webfin.ar.ejb.AccountReceivableHome;
import com.webfin.ar.model.ARReceiptView;
import com.webfin.ar.model.ARAPSettlementView;
import com.webfin.gl.model.TitipanPremiReinsuranceView;
import com.webfin.gl.model.TitipanPremiView;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.util.Date;

public class ARTitipanPremiListForm extends Form {

   private DTOList list;
   private ARReceiptFilter filter;
   private String titipanPremiID;
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
   private boolean enableSuperEdit = SessionManager.getInstance().getSession().hasResource("POL_SUPER_EDIT");


   private ARAPSettlementView arapSettlementView;

   private DTOList listReinsurance;

   public String getTitipanPremiID() {
      return titipanPremiID;
   }

   public void chgBranch() {
   }



   public void setTitipanPremiID(String titipanPremiID) {
      this.titipanPremiID = titipanPremiID;
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

      sqa.addSelect("*");

      sqa.addQuery(
              " from " +
              "   ar_titipan_premi");

      sqa.addFilter(list.getFilter());
      
      sqa.addOrder("trx_id desc");

      sqa.setLimit(300);

      list = sqa.getList(TitipanPremiView.class);

      return list;
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

   public ARTitipanPremiListForm() {
      filter = new ARReceiptFilter();
      filter.activate();
   }

   public void clickCreate() throws Exception {
      final ARTitipanPremiForm form = (ARTitipanPremiForm)newForm("titipanpremiform", this);

      form.createNew();

      form.show();

   };
   
   public void clickCreatePajak() throws Exception {
      final ReceiptForm form = (ReceiptForm)newForm("receiptform3", this);

      form.createNew(stSettlementID);

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

      final ARTitipanPremiForm form = (ARTitipanPremiForm)newForm("titipanpremiform", this);

      form.edit(titipanPremiID);

      form.show();


   };
   
   public void clickEditPajak() throws Exception {

      final ReceiptForm form = (ReceiptForm)newForm("receiptform3", this);

      form.edit(titipanPremiID);

      form.show();

   };
   
   public void clickEditTitipan() throws Exception {

      final ReceiptForm form = (ReceiptForm)newForm("receipttitipanform", this);

      form.editTitipan(titipanPremiID);

      form.show();

   };
   
   public void clickSuperEdit() throws Exception {

      final ReceiptForm form = (ReceiptForm)newForm("receiptform", this);

      form.superEdit(titipanPremiID);

      form.show();

   };
   

   public void clickView() throws Exception {

      final ReceiptForm form = (ReceiptForm)newForm("receiptform", this);

      form.view(titipanPremiID);

      form.show();
   };

   public void initialize() {
      super.initialize();    //To change body of overridden methods use File | Settings | File Templates.

      stSettlementID = (String)getAttribute("arsid");

      final ARAPSettlementView settlement = getSettlement();
      setTitle(settlement==null?"UPLOAD TITIPAN PREMI":settlement.getStDescription());

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

   public void clickUploadExcel() throws Exception {
      final ARTitipanPremiForm form = (ARTitipanPremiForm)newForm("titipanpremiform", this);

      form.createNewUpload();

      form.show();

   };

     public DTOList getListReinsurance() throws Exception {
      final SQLAssembler sqa = new SQLAssembler();

      if (listReinsurance==null) {
            listReinsurance=new DTOList();
            final Filter filter = new Filter();
            //filter.orderKey="root_id desc, create_date desc";
            listReinsurance.setFilter(filter.activate());
        }

      sqa.addSelect("*");

      sqa.addQuery(
              " from " +
              "   ar_titipan_premi_reinsurance");

      sqa.addFilter(listReinsurance.getFilter());

      sqa.addOrder("trx_id desc");

      sqa.setLimit(300);

      listReinsurance = sqa.getList(TitipanPremiReinsuranceView.class);

      return listReinsurance;
   }

   public void setListReinsurance(DTOList listReinsurance) {
      this.listReinsurance = listReinsurance;
   }

   public void clickUploadExcelReinsurance() throws Exception {
      final ARTitipanPremiForm form = (ARTitipanPremiForm)newForm("titipanpremireinsuranceform", this);

      form.createNewUploadReinsurance();

      form.show();

   };

}
