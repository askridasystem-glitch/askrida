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
import com.webfin.insurance.model.InsurancePolicyTypeView;
import com.webfin.insurance.model.InsurancePolicyObjDefaultView;
import com.webfin.FinCodec;
import com.crux.ff.model.FlexFieldDetailView;
import com.crux.ff.model.FlexFieldHeaderView;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import javax.servlet.ServletOutputStream;

public class PolicySearchForm extends Form {
   private String policyID;
   private String stPrintForm;
   private String stLang;
   private String stFontSize;
   private String stAttached;
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
   private String stPolicyTypeGroupID;
   private String stPolicyTypeID;
   private String stPolicyHeaderID;
   private InsurancePolicyObjDefaultView object;
   private String stFilter1;
   private String stKeyword1;
   private String stFilter2;
   private String stKeyword2;
   private String stFilter3;
   private Date dtKeyword3;
   private String stFilter4;
   private String dbKeyword4;
   private String stEntityID;

    private String stPolicy;
   
   public String getStPolicyHeaderID() {
      return stPolicyHeaderID;
   }

   public void setStPolicyHeaderID(String stPolicyHeaderID) {
      this.stPolicyHeaderID = stPolicyHeaderID;
   }
   
   public String getStPolicyTypeID() {
      return stPolicyTypeID;
   }

   public void setStPolicyTypeID(String stPolicyTypeID) {
      this.stPolicyTypeID = stPolicyTypeID;
   }
   
   public String getStPolicyTypeGroupID() {
      return stPolicyTypeGroupID;
   }

   public void setStPolicyTypeGroupID(String stPolicyTypeGroupID) {
      this.stPolicyTypeGroupID = stPolicyTypeGroupID;
   }
   
   public void onChangePolicyType() throws Exception {
   	
       setStPolicyHeaderID(getPolicyType(getStPolicyTypeID()).getStPolicyTypeCode());
   	     
   }
   
   public InsurancePolicyObjDefaultView getPolicyObj() {
      return object;
   }

   public void setPolicyObj(InsurancePolicyObjDefaultView object) {
      this.object = object;
   }
   
   public void onChangePolicyTypeGroup() {
      
   }
   
   public FlexFieldHeaderView getFF(String ffid) {
      return (FlexFieldHeaderView) DTOPool.getInstance().getDTO(FlexFieldHeaderView.class, ffid);
   }
   
   public InsurancePolicyTypeView getPolicyType(String stPolicyTypeID) {
      return (InsurancePolicyTypeView) DTOPool.getInstance().getDTO(InsurancePolicyTypeView.class, stPolicyTypeID);
   }
   
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
      
      sqa.addClause("(a.cover_note_flag='N')");
      

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

      list=sqa.getList(InsurancePolicyView.class);
      //javax.swing.JOptionPane.showMessageDialog(null,sqa.getSQL(),"Claim",javax.swing.JOptionPane.CLOSED_OPTION );

	  
      return list;
   }

   public void refresh() {

   }

   public void setList(DTOList list) {
      this.list = list;
   }
   
   public DTOList getObject() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();
        sqa.addSelect("*");
        sqa.addQuery("from ins_policy a "
                + " inner join ins_pol_obj b on a.pol_id = b.pol_id");

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = ?");
            sqa.addPar(stEntityID);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stFilter1 != null && stKeyword1 != null) {
            if(stFilter1.equalsIgnoreCase("ref1")){
                sqa.addClause("to_tsvector('english', b.ref1) @@ to_tsquery('english', ?) ");
                sqa.addPar(stKeyword1.toUpperCase().replaceAll(" ", "&").replaceAll("&&", "&"));
            }else{
                sqa.addClause("upper(b." + stFilter1 + ") like ?");
                sqa.addPar("%" + stKeyword1.toUpperCase() + "%");   
            }
        }

        if (stFilter2 != null && stKeyword2 != null) {
            //sqa.addClause("upper(b." + stFilter2 + ") like ?");
            //sqa.addPar("%" + stKeyword2.toUpperCase() + "%");
            if(stFilter2.equalsIgnoreCase("ref1")){
                sqa.addClause("to_tsvector('english', b.ref1) @@ to_tsquery('english', ?) ");
                sqa.addPar(stKeyword2.toUpperCase().replaceAll(" ", "&").replaceAll("&&", "&"));
            }else{
                sqa.addClause("upper(b." + stFilter2 + ") like ?");
                sqa.addPar("%" + stKeyword2.toUpperCase() + "%");
            }
        }

        if (stFilter3 != null && dtKeyword3 != null) {
            sqa.addClause("date_trunc('day',b." + stFilter3 + ") = ?");
            sqa.addPar(dtKeyword3);
        }

        if (stFilter4 != null && dbKeyword4 != null) {
            sqa.addClause("b." + stFilter4 + " = ?");
            sqa.addPar(dbKeyword4);
        }

        if (stPolicy != null) {
            sqa.addClause("a.pol_no = ? ");
            sqa.addPar(stPolicy);
        }

        sqa.addClause("a.status in ('POLICY','RENEWAL','ENDORSE') and coalesce(a.active_flag,'') = 'Y'");

        sqa.addOrder("a.pol_id desc");
        sqa.setLimit(100);

        return sqa.getList(InsurancePolicyObjDefaultView.class);

    }

   public PolicySearchForm() {
   }

   public void clickCreate() throws Exception {
      final PolicyForm form = (PolicyForm)newForm("policy_edit",this);

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
      final PolicyForm form = (PolicyForm)newForm("policy_edit",this);

      final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policyID);

      if (pol==null) throw new RuntimeException("Document not found ??");

      if (!editFilter.contains(pol.getStStatus())) throw new RuntimeException("This document can't be edited");

      form.setReasMode(reas);

      form.edit(policyID);

      form.show();
   }
   
   public void clickCreateRenewal() throws Exception {
      validatePolicyID();

      final PolicyForm form = (PolicyForm)newForm("policy_edit",this);

      //form.setReasMode(reas);
      
      form.editCreateRenewal(policyID);
      

      form.show();
   }
   
   public void clickCreateClaimPLA() throws Exception {
      validatePolicyID();

      final PolicyForm form = (PolicyForm)newForm("policy_edit",this);

      form.editCreateClaimPLA(policyID);
      
      form.show();
   }
   
      public void clickCreateClaimDLA() throws Exception {
      validatePolicyID();

      final PolicyForm form = (PolicyForm)newForm("policy_edit",this);

      form.editCreateClaimDLA(policyID);

      form.show();
   }

   public void clickSuperEdit() throws Exception {
      final PolicyForm form = (PolicyForm)newForm("policy_edit",this);

      form.setReasMode(reas);

      form.superEdit(policyID);

      form.show();
   }

   public void clickView() throws Exception {
      final PolicyForm form = (PolicyForm)newForm("policy_edit", this);

      form.setReasMode(reas);

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
   
   public String getStFontSize() {
      return stFontSize;
   }

   public void setStFontSize(String stFontSize) {
      this.stFontSize = stFontSize;
   }
   
   public String getStAttached() {
      return stAttached;
   }

   public void setStAttached(String stAttached) {
      this.stAttached = stAttached;
   }

    public String getStFilter1()
    {
        return stFilter1;
    }

    public void setStFilter1(String stFilter1)
    {
        this.stFilter1 = stFilter1;
    }

    public String getStKeyword1()
    {
        return stKeyword1;
    }

    public void setStKeyword1(String stKeyword1)
    {
        this.stKeyword1 = stKeyword1;
    }

    public String getStFilter2()
    {
        return stFilter2;
    }

    public void setStFilter2(String stFilter2)
    {
        this.stFilter2 = stFilter2;
    }

    public String getStKeyword2()
    {
        return stKeyword2;
    }

    public void setStKeyword2(String stKeyword2)
    {
        this.stKeyword2 = stKeyword2;
    }

    public String getStFilter3()
    {
        return stFilter3;
    }

    public void setStFilter3(String stFilter3)
    {
        this.stFilter3 = stFilter3;
    }

    public Date getDtKeyword3()
    {
        return dtKeyword3;
    }

    public void setDtKeyword3(Date dtKeyword3)
    {
        this.dtKeyword3 = dtKeyword3;
    }

    public String getStFilter4()
    {
        return stFilter4;
    }

    public void setStFilter4(String stFilter4)
    {
        this.stFilter4 = stFilter4;
    }

    public String getDbKeyword4()
    {
        return dbKeyword4;
    }

    public void setDbKeyword4(String dbKeyword4)
    {
        this.dbKeyword4 = dbKeyword4;
    }

    /**
     * @return the stEntityID
     */
    public String getStEntityID() {
        return stEntityID;
    }

    /**
     * @param stEntityID the stEntityID to set
     */
    public void setStEntityID(String stEntityID) {
        this.stEntityID = stEntityID;
    }

    public void EXCEL_SEARCHING() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" ';'||a.pol_no as nopol,b.ref1,b.ref2,b.ref3,b.ref4,b.ref5,b.ref6,b.ref7,b.ref8,b.ref9,b.ref10, "
                + "b.ref11,b.ref12,b.ref13,b.ref14,b.ref15,b.ref16,b.ref17,b.ref18,b.ref19,b.ref20,b.refd1,b.refd2,b.refd3,"
                + "b.risk_class,b.ins_risk_cat_code1,b.ins_risk_cat_code2,b.ins_risk_cat_code3,"
                //+ "b.ref1 as penggunaan,b.risk_class as konstruksi,"
                //+ "b.ref3 as kode_resiko,b.ref4 as penerangan,b.ref5 as alamat,b.ref7 as kode_akumulasi,"
                //+ "b.ref9 as kodepos,b.ref11 as nama,b.ref12 as longitude,b.ref13 as latitude,"
                //+ "b.refd1 as periode_awal,b.refd2 as periode_akhir,b.ref14 as nota_debet,b.ref15 as no_kpak,"
                //+ "b.ins_risk_cat_code1 as kode_resiko1,b.ins_risk_cat_code2 as kode_resiko4,b.ins_risk_cat_code3 as kode_resiko3,"
                + "b.insured_amount as tsi,b.premi_total as premi ");

        sqa.addQuery("from ins_policy a "
                + " inner join ins_pol_obj b on a.pol_id = b.pol_id");

        sqa.addClause("a.status in ('POLICY','RENEWAL','ENDORSE')");

        if (stEntityID != null) {
            sqa.addClause("a.entity_id = '"+stEntityID+"'");
            //sqa.addPar(stEntityID);
        }

        if (stBranch != null) {
            sqa.addClause("a.cc_code = '"+stBranch+"'");
            //sqa.addPar(stBranch);
        }

        if (stPolicyTypeGroupID != null) {
            sqa.addClause("a.ins_policy_type_grp_id = '"+stPolicyTypeGroupID+"'");
            //sqa.addPar(stPolicyTypeGroupID);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = '"+stPolicyTypeID+"'");
            //sqa.addPar(stPolicyTypeID);
        }

        if (stFilter1 != null && stKeyword1 != null) {

            //sqa.addPar("%" + stKeyword1.toUpperCase() + "%");

            if(stFilter1.equalsIgnoreCase("ref1")){
                sqa.addClause("to_tsvector('english', b.ref1) @@ to_tsquery('english', "+ stKeyword1.toUpperCase().replaceAll(" ", "&").replaceAll("&&", "&") +") ");
                //sqa.addPar();
            }else{
                sqa.addClause("upper(b." + stFilter1 + ") like '%"+stKeyword1.toUpperCase()+"%'");
            }

        }

        if (stFilter2 != null && stKeyword2 != null) {
            //sqa.addClause("upper(b." + stFilter2 + ") like '%"+stKeyword2.toUpperCase()+"%'");
            //sqa.addPar("%" + stKeyword2.toUpperCase() + "%");
            if(stFilter2.equalsIgnoreCase("ref1")){
                sqa.addClause("to_tsvector('english', b.ref1) @@ to_tsquery('english', "+ stKeyword2.toUpperCase().replaceAll(" ", "&").replaceAll("&&", "&") +") ");
                //sqa.addPar();
            }else{
                sqa.addClause("upper(b." + stFilter2 + ") like '%"+stKeyword2.toUpperCase()+"%'");
            }

        }

        if (stFilter3 != null && dtKeyword3 != null) {
            sqa.addClause("date_trunc('day',b." + stFilter3 + ") = '"+dtKeyword3+"'");
            //sqa.addPar(dtKeyword3);
        }

        if (stFilter4 != null && dbKeyword4 != null) {
            sqa.addClause("b." + stFilter4 + " = '"+dbKeyword4+"'");
            //sqa.addPar(dbKeyword4);
        }

        if (stPolicy != null) {
            sqa.addClause("a.pol_no like '%"+stPolicy+"%'");
            //sqa.addPar(stBranch);
        }

        String sql = sqa.getSQL() + " order by a.pol_no";

        SQLUtil S = new SQLUtil();

        String nama_file = "searchinglist_" + System.currentTimeMillis() + ".csv";

        sql = "Copy ("
                + sql
                + " ) To 'D://exportdb/csv/" + nama_file + "' With CSV HEADER;";

        final PreparedStatement ps = S.setQuery(sql);

        boolean tes = ps.execute();

        S.release();

        File file = new File("//db03.askrida.co.id//exportdb/csv/" + nama_file);
        int length = 0;
        ServletOutputStream outStream = SessionManager.getInstance().getResponse().getOutputStream();

        SessionManager.getInstance().getResponse().setContentType("text/csv");
        SessionManager.getInstance().getResponse().setContentLength((int) file.length());

        // sets HTTP header
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + nama_file + "\"");

        int BUFSIZE = 4096;
        byte[] byteBuffer = new byte[BUFSIZE];
        DataInputStream in = new DataInputStream(new FileInputStream(file));

        // reads the file's bytes and writes them to the response stream
        while ((in != null) && ((length = in.read(byteBuffer)) != -1)) {
            outStream.write(byteBuffer, 0, length);
        }

        in.close();
        outStream.close();

        file.delete();
    }

    /**
     * @return the stPolicy
     */
    public String getStPolicy() {
        return stPolicy;
    }

    /**
     * @param stPolicy the stPolicy to set
     */
    public void setStPolicy(String stPolicy) {
        this.stPolicy = stPolicy;
    }
   
}



