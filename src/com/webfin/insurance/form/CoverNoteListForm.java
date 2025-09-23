/***********************************************************************
 * Module:  com.webfin.insurance.form.PolicyListForm
 * Author:  Denny Mahendra
 * Created: Jan 19, 2006 1:03:25 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.form;

import com.crux.common.model.Filter;
import com.crux.common.controller.UserSessionMgr;
import com.crux.util.*;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.pool.DTOPool;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.insurance.model.CoverNoteView;
import com.webfin.FinCodec;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class CoverNoteListForm extends Form {
   private String policyID;
   private String stPrintForm;
   private String stLang;
   private String stOutstandingFlag="Y";
   private DTOList list;
   private boolean enableCreateSPPA;
   private boolean enableCreatePolis;
   private boolean enableCreateProduction;
   private boolean enableCreateProposal;
   private boolean enableEdit = false;
   private boolean enableRenewal;
   private boolean enableSuperEdit = SessionManager.getInstance().getSession().hasResource("POL_SUPER_EDIT");
   private boolean enableEndorsement;
   private boolean enableClaim;
   private boolean enableView = true;
   private boolean enablePrint = false;
   private String printingLOV = "";
   private String stBranch = SessionManager.getInstance().getSession().getStBranch();

   private Date dtFilterPolicyDateFrom;
   private Date dtFilterPolicyDateTo;
   private Date dtFilterPolicyExpireFrom;
   private Date dtFilterPolicyExpireTo;
   private HashSet editFilter = new HashSet();
   private boolean reas;
   private boolean canNavigateBranch;
   private boolean canEdit;
   private boolean canCreate;
   private boolean canApprove;
   private boolean listEffective;
   private boolean claim;
   
   public boolean isClaim() {
      return claim;
   }

   public void setClaim(boolean claim) {
      this.claim = claim;
   }

   public boolean isReas() {
      return reas;
   }

   public void setReas(boolean reas) {
      this.reas = reas;
   }

   public String getStOutstandingFlag() {
      return stOutstandingFlag;
   }

   public void setStOutstandingFlag(String stOutstandingFlag) {
      this.stOutstandingFlag = stOutstandingFlag;
   }

   public boolean isCanNavigateBranch() {
      return canNavigateBranch;
   }

   public void setCanNavigateBranch(boolean canNavigateBranch) {
      this.canNavigateBranch = canNavigateBranch;
   }

   public String getStBranch() {
      return stBranch;
   }

   public void setStBranch(String stBranch) {
      this.stBranch = stBranch;
   }

   public Date getDtFilterPolicyDateFrom() {
      return dtFilterPolicyDateFrom;
   }

   public void setDtFilterPolicyDateFrom(Date dtFilterPolicyDateFrom) {
      this.dtFilterPolicyDateFrom = dtFilterPolicyDateFrom;
   }

   public Date getDtFilterPolicyDateTo() {
      return dtFilterPolicyDateTo;
   }

   public void setDtFilterPolicyDateTo(Date dtFilterPolicyDateTo) {
      this.dtFilterPolicyDateTo = dtFilterPolicyDateTo;
   }

   public Date getDtFilterPolicyExpireFrom() {
      return dtFilterPolicyExpireFrom;
   }

   public void setDtFilterPolicyExpireFrom(Date dtFilterPolicyExpireFrom) {
      this.dtFilterPolicyExpireFrom = dtFilterPolicyExpireFrom;
   }

   public Date getDtFilterPolicyExpireTo() {
      return dtFilterPolicyExpireTo;
   }

   public void setDtFilterPolicyExpireTo(Date dtFilterPolicyExpireTo) {
      this.dtFilterPolicyExpireTo = dtFilterPolicyExpireTo;
   }

   public String getPrintingLOV() {
      return printingLOV;
   }

   public boolean isEnableCreateProduction() {
      return enableCreateProduction;
   }

   public void setEnableCreateProduction(boolean enableCreateProduction) {
      this.enableCreateProduction = enableCreateProduction;
   }

   public boolean isEnableCreateSPPA() {
      return enableCreateSPPA;
   }

   public boolean isEnableCreatePolis() {
      return enableCreatePolis;
   }

   public boolean isEnableCreateProposal() {
      return enableCreateProposal;
   }

   public boolean isEnableEdit() {
      return enableEdit;
   }

   public boolean isEnableSuperEdit() {
      return enableSuperEdit;
   }

   public boolean isEnableEndorsement() {
      return enableEndorsement;
   }

   public boolean isEnableClaim() {
      return enableClaim;
   }
   
      public boolean isEnableClaimPLA() {
      return enableClaim;
   }
   
      public boolean isEnableClaimDLA() {
      return enableClaim;
   }

   public boolean isEnableView() {
      return enableView;
   }

   public boolean isEnablePrint() {
      return enablePrint;
   }
   
    public boolean isEnableRenewal() {
      return enableRenewal;
   }


   private ArrayList statusFilter;

   private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB",InsuranceHome.class.getName()))
            .create();
   }

   public String getPolicyID() {
      return policyID;
   }

   public void setPolicyID(String policyID) {
      this.policyID = policyID;
   }

   public DTOList getList() throws Exception {
      /*DTOList list = ListUtil.getDTOListFromQuery("select * from ins_policy where active_flag = 'Y' order by pol_no",
                    InsurancePolicyView.class,filter);

      list.setFilter(filter);*/

      if (list==null) {
         list=new DTOList();
         final Filter filter = new Filter();
         //filter.orderKey="root_id desc, create_date desc";
         list.setFilter(filter.activate());
      }

      final SQLAssembler sqa = new SQLAssembler();

      sqa.addQuery(
              "   from " +
              "      (" +
              "         select " +
              "            a.*, b.description as policy_type_desc " +
              "         from " +
              "            ins_policy a" +
              "            left join ins_policy_types b on b.pol_type_id = a.pol_type_id" +
              "      ) a");

      sqa.addSelect("*");

      sqa.addFilter(list.getFilter());
      
      sqa.addClause("(a.cover_note_flag='Y')");

      if (Tools.isYes(stOutstandingFlag)) {
         sqa.addClause("(a.effective_flag='N' or a.effective_flag is null or a.active_flag='Y')");
      }     

      if (statusFilter!=null) {
         sqa.addClause(SQLUtil.exprIN("status",statusFilter));

         for (int i = 0; i < statusFilter.size(); i++) {
            String s = (String) statusFilter.get(i);

            sqa.addPar(s);
         }
      }
      
      /*
      if(listEffective==true){
      	sqa.addQuery(" except "+
      		  "	   (" +
              "         select " +
              "            a.*, b.description as policy_type_desc " +
              "         from " +
              "            ins_policy a" +
              "            left join ins_policy_types b on b.pol_type_id = a.pol_type_id" +
              "      where status in ('POLICY') and effective_flag ='N')");
      }*/
      
      

      if (stBranch!=null) {
         sqa.addClause(
                 "a.cc_code = ?"
         );
         sqa.addPar(stBranch);
      }

      if (dtFilterPolicyDateFrom!=null) {
         sqa.addClause(
                 "a.policy_date >= ?"
         );
         sqa.addPar(dtFilterPolicyDateFrom);
      }

      if (dtFilterPolicyDateTo!=null) {
         sqa.addClause(
                 "a.policy_date <= ?"
         );
         sqa.addPar(dtFilterPolicyDateTo);
      }

      if (dtFilterPolicyExpireFrom!=null) {
         sqa.addClause(
                 "a.period_end >= ?"
         );
         sqa.addPar(dtFilterPolicyExpireFrom);
      }

      if (dtFilterPolicyExpireTo!=null) {
         sqa.addClause(
                 "a.period_end <= ?"
         );
         sqa.addPar(dtFilterPolicyExpireTo);
      }

      sqa.addOrder("root_id desc, create_date desc");
	//	javax.swing.JOptionPane.showMessageDialog(null,sqa.getSQL(),"Claim",javax.swing.JOptionPane.CLOSED_OPTION );

      list=sqa.getList(CoverNoteView.class);
      //javax.swing.JOptionPane.showMessageDialog(null,sqa.getSQL(),"Claim",javax.swing.JOptionPane.CLOSED_OPTION );

	  
      return list;
   }

   public void refresh() {

   }

   public void setList(DTOList list) {
      this.list = list;
   }

   public CoverNoteListForm() {
   }

   public void clickCreate() throws Exception {
      final CoverNoteForm form = (CoverNoteForm)newForm("cover_note_edit",this);

      form.createNew();

      form.show();
   }

   public void clickCreateProd() throws Exception {
      final PolicyForm form = (PolicyForm)newForm("policy_edit",this);

      form.createNewProductionOnly();

      form.show();
   }

   public void clickCreatePolis() throws Exception {
      //validatePolicyID();

      final PolicyForm form = (PolicyForm)newForm("policy_edit",this);

      form.editCreatePolis(policyID);

      form.show();
   }

   public void clickCreateSPPA() throws Exception {
      validatePolicyID();

      final PolicyForm form = (PolicyForm)newForm("policy_edit",this);

      form.editCreateSPPA(policyID);

      form.show();
   }

   private void validatePolicyID() {
      if (policyID==null) throw new RuntimeException("Please select a policy first");
   }

   public void clickCreateEndorse() throws Exception {
      validatePolicyID();

      final PolicyForm form = (PolicyForm)newForm("policy_edit",this);

      form.editCreateEndorse(policyID);

      form.show();
   }

   public void clickCreateClaim() throws Exception {
      validatePolicyID();

      final PolicyForm form = (PolicyForm)newForm("policy_edit",this);

      form.editCreateClaim(policyID);

      form.show();
   }
   
   
   public void clickCreateClaimEndorse() throws Exception {
      validatePolicyID();

      final PolicyForm form = (PolicyForm)newForm("policy_edit",this);

      form.editCreateClaimEndorse(policyID);

      form.show();
   }


   public void clickEdit() throws Exception {
      final CoverNoteForm form = (CoverNoteForm)newForm("cover_note_edit",this);

      final CoverNoteView pol = (CoverNoteView) DTOPool.getInstance().getDTO(CoverNoteView.class, policyID);

      if (pol==null) throw new RuntimeException("Document not found ??");

      if (!editFilter.contains(pol.getStStatus())) throw new RuntimeException("This document can't be edited");

      //form.setReasMode(reas);

      form.edit(policyID);

      form.show();
   }
   
   public void clickCreateRenewal() throws Exception {
      validatePolicyID();

      final CoverNoteForm form = (CoverNoteForm)newForm("cover_note_edit",this);

      //form.setReasMode(reas);
      
      form.editCreateRenewal(policyID);
      

      form.show();
   }
   
   public void clickCreateClaimPLA() throws Exception {
      validatePolicyID();

      final CoverNoteForm form = (CoverNoteForm)newForm("cover_note_edit",this);

      form.editCreateClaimPLA(policyID);
      
      form.show();
   }
   
      public void clickCreateClaimDLA() throws Exception {
      validatePolicyID();

      final CoverNoteForm form = (CoverNoteForm)newForm("cover_note_edit",this);

      form.editCreateClaimDLA(policyID);

      form.show();
   }

   public void clickSuperEdit() throws Exception {
      final CoverNoteForm form = (CoverNoteForm)newForm("cover_note_edit",this);

      form.setReasMode(reas);

      form.superEdit(policyID);

      form.show();
   }

   public void clickView() throws Exception {
      final CoverNoteForm form = (CoverNoteForm)newForm("cover_note_edit", this);

      //form.setReasMode(reas);

      form.view(policyID);

      form.show();
   }

   public String getStPrintForm() {
      return stPrintForm;
   }

   public void setStPrintForm(String stPrintForm) {
      this.stPrintForm = stPrintForm;
   }

   public String getStLang() {
      return stLang;
   }

   public void setStLang(String stLang) {
      this.stLang = stLang;
   }

   public void proposal() {
      setTitle("PROPOSAL");
      printingLOV = "PROPOSAL";

      canEdit = hasResource("POL_PROP_EDIT");
      canCreate = hasResource("POL_PROP_CREATE");
      canApprove = hasResource("POL_PROP_APRV");
      enablePrint = hasResource("POL_PROP_PRINT");

      statusFilter = array2list(new String [] {
         FinCodec.PolicyStatus.SPPA,
         FinCodec.PolicyStatus.DRAFT,
      });

      enableCreateSPPA=canCreate;
      enableCreateProposal=canCreate;

      if (canEdit) {
         editFilter.add(FinCodec.PolicyStatus.SPPA);
         editFilter.add(FinCodec.PolicyStatus.DRAFT);
      }

      canNavigateBranch = hasResource("POL_PROP_NAVBR");
      enableEdit = canEdit;

   }

   public boolean isCanApprove() {
      return canApprove;
   }

   public void setCanApprove(boolean canApprove) {
      this.canApprove = canApprove;
   }

   public boolean isCanEdit() {
      return canEdit;
   }

   public void setCanEdit(boolean canEdit) {
      this.canEdit = canEdit;
   }

   public boolean isCanCreate() {
      return canCreate;
   }

   public void setCanCreate(boolean canCreate) {
      this.canCreate = canCreate;
   }
   
   public boolean isListEffective() {
      return listEffective;
   }

   public void setListEffective(boolean listEffective) {
      this.listEffective = listEffective;
   }

   private boolean hasResource(String rsrc) {
      return SessionManager.getInstance().getSession().hasResource(rsrc);
   }

   private ArrayList array2list(String[] strings) {

      final ArrayList l = new ArrayList();

      for (int i = 0; i < strings.length; i++) {
         String string = strings[i];

         l.add(string);
      }

      return l;
   }

   public void underwrit() {
      setTitle("UNDERWRITING");
      printingLOV = "UWRIT";

      canEdit = hasResource("POL_UWRIT_EDIT");
      canCreate = hasResource("POL_UWRIT_CREATE");
      canApprove = hasResource("POL_UWRIT_APRV");
      enablePrint = hasResource("POL_UWRIT_PRINT");

      statusFilter = array2list(new String [] {
         FinCodec.PolicyStatus.SPPA,
         FinCodec.PolicyStatus.POLICY,
         FinCodec.PolicyStatus.ENDORSE,
         FinCodec.PolicyStatus.RENEWAL,
      });

      //enableCreateSPPA=canCreate;
      enableCreatePolis=canCreate;
      enableEndorsement=canCreate;
      enableRenewal=canCreate;

      editFilter.add(FinCodec.PolicyStatus.SPPA);
      editFilter.add(FinCodec.PolicyStatus.POLICY);
      editFilter.add(FinCodec.PolicyStatus.ENDORSE);
      editFilter.add(FinCodec.PolicyStatus.RENEWAL);

      canNavigateBranch = hasResource("POL_UWRIT_NAVBR");

      enableEdit = canEdit;
   }

   public void claim() {
      printingLOV = "CLAIM";

      setTitle("CLAIM");

      canEdit = hasResource("POL_CLAIM_EDIT");
      canCreate = hasResource("POL_CLAIM_CREATE");
      canApprove = hasResource("POL_CLAIM_APRV");
      enablePrint = hasResource("POL_CLAIM_PRINT");
      canNavigateBranch = hasResource("POL_CLAIM_NAVBR");

      statusFilter = array2list(new String [] {
         FinCodec.PolicyStatus.CLAIM,
         FinCodec.PolicyStatus.POLICY,
         FinCodec.PolicyStatus.ENDORSE,
         FinCodec.PolicyStatus.ENDORSECLAIM
      });
      enableClaim=true;
      enableEdit = canEdit;
      listEffective = true;
      claim = true;
                  
      editFilter.add(FinCodec.PolicyStatus.CLAIM);
      editFilter.add(FinCodec.PolicyStatus.ENDORSECLAIM);
  
   }

   public void renewal() {
      printingLOV = "RENEWAL";

      setTitle("RENEWAL NOTICES");

      statusFilter = array2list(new String [] {
         FinCodec.PolicyStatus.POLICY,
         FinCodec.PolicyStatus.ENDORSE,
      });
   }

   public void reas() {
      setTitle("REINSURANCE");

      printingLOV = "REAS";

      canEdit = hasResource("POL_RI_EDIT");
      canCreate = hasResource("POL_RI_CREATE");
      canApprove = hasResource("POL_RI_APRV");
      enablePrint = hasResource("POL_RI_PRINT");
      canNavigateBranch = hasResource("POL_RI_NAVBR");

      statusFilter = array2list(new String [] {
         FinCodec.PolicyStatus.SPPA,
         FinCodec.PolicyStatus.POLICY,
         FinCodec.PolicyStatus.ENDORSE,
         FinCodec.PolicyStatus.RENEWAL,
      });

      reas = true;
      enableEdit = canEdit;

      editFilter.add(FinCodec.PolicyStatus.SPPA);
      editFilter.add(FinCodec.PolicyStatus.POLICY);
      editFilter.add(FinCodec.PolicyStatus.ENDORSE);
      editFilter.add(FinCodec.PolicyStatus.RENEWAL);   }
      
      public void titipan() {
      setTitle("COVER NOTE");
      printingLOV = "UWRIT";

      canEdit = hasResource("POL_UWRIT_EDIT");
      canCreate = hasResource("POL_UWRIT_CREATE");
      canApprove = hasResource("POL_UWRIT_APRV");
      enablePrint = hasResource("POL_UWRIT_PRINT");

      statusFilter = array2list(new String [] {
         FinCodec.PolicyStatus.SPPA,
         FinCodec.PolicyStatus.POLICY,
         FinCodec.PolicyStatus.ENDORSE,
         FinCodec.PolicyStatus.RENEWAL,
      });

      //enableCreateSPPA=canCreate;
      enableCreatePolis=canCreate;
      enableEndorsement=canCreate;
      enableRenewal=canCreate;

      editFilter.add(FinCodec.PolicyStatus.SPPA);
      editFilter.add(FinCodec.PolicyStatus.POLICY);
      editFilter.add(FinCodec.PolicyStatus.ENDORSE);
      editFilter.add(FinCodec.PolicyStatus.RENEWAL);

      canNavigateBranch = hasResource("POL_UWRIT_NAVBR");

      enableEdit = canEdit;
   }

   public void clickApproval() throws Exception {
      final PolicyForm form = (PolicyForm)newForm("policy_edit", this);

      form.approval(policyID);

      form.show();
   }
   
   public void clickApprovalReins() throws Exception {
      final PolicyForm form = (PolicyForm)newForm("policy_edit", this);

      form.approvalReins(policyID);

      form.show();
   }
   
   
}



