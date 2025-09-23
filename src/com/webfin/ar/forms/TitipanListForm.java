/***********************************************************************
 * Module:  com.webfin.ar.forms.ReceiptListForm
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:42 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.forms;

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
import com.webfin.ar.model.ARTitipanView;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.util.Date;

public class TitipanListForm extends Form {
   private DTOList list;
   private ARReceiptFilter filter;
   private String titipanID;
   private String stSettlementID;
   private Date rcpDateFrom;
   private Date rcpDateTo;
   private String receiptNo;
   private String description;
   private String branch;
   private String entity;
   private String printingLOV = "";
   private String printLang;

   private ARAPSettlementView arapSettlementView;



   public String getTitipanID() {
      return titipanID;
   }

   public void chgBranch() {
   }



   public void setTitipanID(String titipanID) {
      this.titipanID = titipanID;
   }

   private AccountReceivable getRemoteAccountReceivable() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((AccountReceivableHome) JNDIUtil.getInstance().lookup("AccountReceivableEJB",AccountReceivableHome.class.getName()))
            .create();
   }



   public DTOList getList() throws Exception {
      final SQLAssembler sqa = new SQLAssembler();

      sqa.addSelect("* ");

      sqa.addQuery(
              " from " +
              "   ar_titipan ");
      if(description!=null){
         sqa.addClause("upper(description) like ?");
         sqa.addLike(description);
      }
      
      if(branch!=null) {
         sqa.addClause("account_no like ?");
         sqa.addPar("% "+branch);
      }

      list = sqa.getList(ARTitipanView.class);

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

   public TitipanListForm() {
      filter = new ARReceiptFilter();
      filter.activate();
   }

   public void clickCreate() throws Exception {
      final TitipanForm form = (TitipanForm)newForm("titipanform", this);

      form.createNew();

      form.show();

   };

   public void clickRefresh() {
   }
   
   public void btnSearch() throws Exception {
      getList().getFilter().setCurrentPage(0);
   }

   public void clickEdit() throws Exception {

      final TitipanForm form = (TitipanForm)newForm("titipanform", this);

      form.edit(titipanID);

      form.show();

   };
   
   public void clickSuperEdit() throws Exception {

      //final ReceiptForm form = (ReceiptForm)newForm("receiptform", this);

      //form.superEdit(titipanID);

      //form.show();

   };
   

   public void clickView() throws Exception {

      //final ReceiptForm form = (ReceiptForm)newForm("receiptform", this);

      //form.view(titipanID);

      //form.show();
   };

   public void clickApprove() throws Exception {
      //final ReceiptForm form = (ReceiptForm)newForm("receiptform", this);

      //form.approve(titipanID);

      //form.show();
   }

   public void clickVoid() throws Exception {
      //final ReceiptForm form = (ReceiptForm)newForm("receiptform", this);

      //form.voids(titipanID);

      //form.show();
   }

   public void initialize() {
      super.initialize();    //To change body of overridden methods use File | Settings | File Templates.

      //stSettlementID = (String)getAttribute("arsid");

      //final ARAPSettlementView settlement = getSettlement();
      //setTitle(settlement==null?"Settlement":settlement.getStDescription());

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
}
