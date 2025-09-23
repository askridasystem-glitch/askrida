/***********************************************************************
 * Module:  com.webfin.insurance.helper.InsurancePolicyHelper
 * Author:  Denny Mahendra
 * Created: Oct 9, 2005 9:57:28 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.helper;

import com.crux.common.controller.FOPServlet;
import com.crux.common.controller.Helper;
import com.crux.common.model.DTO;
import com.crux.common.parameter.Parameter;
import com.crux.file.FileView;
import com.crux.lang.LanguageManager;
import com.crux.lov.LOVManager;
import com.crux.pool.DTOPool;
import com.crux.util.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.Rectangle;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.model.*;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.CurrencyManager;
import com.webfin.entity.filter.EntityFilter;
import com.webfin.entity.model.EntityView;
import com.webfin.entity.model.EntityAddressView;
import com.webfin.FinCodec;
import com.webfin.insurance.filter.ZoneFilter;
import com.webfin.insurance.form.PolicyForm;
import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletOutputStream;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class InsurancePolicyHelper extends Helper{
   private final static transient LogManager logger = LogManager.getInstance(InsurancePolicyHelper.class);

   private String ref1;
   private HashMap refPropMap;
   
   private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB",InsuranceHome.class.getName()))
            .create();
   }

   private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
      return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB",GeneralLedgerHome.class.getName()))
            .create();
   }

   public void listPolicies(HttpServletRequest rq)  throws Exception {
      final DTOList list = getRemoteInsurance().listPolicies(null);

      rq.setAttribute("LIST",list);
   }

   public void create(HttpServletRequest rq)  throws Exception {
      final InsurancePolicyView pol = new InsurancePolicyView();

      final String polType = getString(rq.getParameter("poltype"));
      final String polSubType = getString(rq.getParameter("polsubtype"));

      pol.setStPolicyTypeID(polType);
      pol.setStPolicySubTypeID(polSubType);

      pol.setDetails(new DTOList());

      //pol.setClObjectClass(InsurancePolicyVehicleView.class);
      final DTOList clausules = getRemoteInsurance().getClausules(pol.getStPolicyTypeID(), pol.getStPolicySubTypeID());

      for (int i = 0; i < clausules.size(); i++) {
         InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);

         icl.markNew();
      }

      pol.setClausules(clausules);

      final DTOList objects = new DTOList();

      /*final InsurancePolicyObjectView obj = pol.createObject();

      objects.add(obj);
      objects.select((DTO) obj);*/
      pol.setObjects(objects);

      final EntityView owner = new EntityView();

      owner.markNew();

      owner.createPrimaryAddress();

      final InsurancePolicyEntityView ownerPolEntity = new InsurancePolicyEntityView();

      ownerPolEntity.markNew();

      ownerPolEntity.setStRelationType(FinCodec.PolicyEntityRelation.OWNER);

      ownerPolEntity.setEntity(owner);

      pol.markNew();

      pol.setEntities(new DTOList());

      pol.getEntities().add(ownerPolEntity);

      pol.setDtPolicyDate(new Date());
      pol.setStCurrencyCode(CurrencyManager.getInstance().getMasterCurrency());
      pol.setDbCurrencyRate(BDUtil.one);

      put(rq,"POL",pol);

      populate(rq);
   }

   private InsurancePolicyView populate(HttpServletRequest rq) throws Exception {
      final InsurancePolicyView pol = (InsurancePolicyView)get(rq,"POL");

      pol.recalculate();

      final LOV ccyList = getRemoteGeneralLedger().getCurrencyCodeLOV();

      final DTOList insItemsList = getRemoteInsurance().getInsItemsList();


      rq.setAttribute("POLICY",pol);
      rq.setAttribute("CCY_LIST",ccyList);
      rq.setAttribute("ITEM_LIST",insItemsList);
      rq.setAttribute("BUSINESS_SOURCE",getRemoteInsurance().getBusinessSourceLOV());
      rq.setAttribute("REGION",getRemoteInsurance().getRegionLOV());
      return pol;
   }

   public void selectPolicy(HttpServletRequest rq)  throws Exception {
      final String polType = getString(rq.getParameter("poltype"));
      final LOV insTypesList = getRemoteInsurance().getInsuranceTypesLOV();
      insTypesList.setLOValue(polType);
      rq.setAttribute("INS_TYPE_LIST",insTypesList);


      if (polType!=null) {
         final LOV insSubTypesList = getRemoteInsurance().getInsuranceSubTypesLOV(polType);
         rq.setAttribute("INS_SUBTYPE_LIST",insSubTypesList);
      }
   }

   public void edit(HttpServletRequest rq)  throws Exception {
      final InsurancePolicyView pol = view(rq);

      if (!Tools.isYes(pol.getStPostedFlag()))
         pol.markUpdate();
   }

   public InsurancePolicyView view(HttpServletRequest rq)  throws Exception {
      final String policyID = getString(rq.getParameter("polid"));

      final InsurancePolicyView pol = getRemoteInsurance().getInsurancePolicy(policyID);

      pol.loadObjects();
      pol.loadClausules();
      pol.loadEntities();
      pol.loadDetails();

      //logger.logDebug("view: entities = "+pol.getEntities());

      put(rq,"POL",pol);

      return populate(rq);
   }

   public void save(HttpServletRequest rq)  throws Exception {
      final InsurancePolicyView pol = retrieve(rq);

      getRemoteInsurance().save(pol, "?",false);
   }

   public void recalculate(HttpServletRequest rq)  throws Exception {
      final InsurancePolicyView pol = retrieve(rq);

      populate(rq);
   }

   public void deleteItem(HttpServletRequest rq)  throws Exception {
      final InsurancePolicyView pol = retrieve(rq);

      final int delIndex = Integer.parseInt(rq.getParameter("delindex"));

      pol.getDetails().delete(delIndex);

      populate(rq);
   }

   public void newItem(HttpServletRequest rq)  throws Exception {
      final InsurancePolicyView pol = retrieve(rq);

      final InsurancePolicyItemsView ip = new InsurancePolicyItemsView();

      ip.setStInsItemID(getString(rq.getParameter("i_item_n")));

      ip.markNew();

      pol.getDetails().add(ip);

      populate(rq);
   }

   private InsurancePolicyView retrieve(HttpServletRequest rq) throws Exception {
      final InsurancePolicyView pol = (InsurancePolicyView)get(rq,"POL");

      pol.touch();

      pol.setStPolicyNo(getString(rq.getParameter("polno")));
      pol.setDtPolicyDate(getDate(rq.getParameter("poldate")));
      pol.setStDescription(getString(rq.getParameter("desc")));
      pol.setStCurrencyCode(getString(rq.getParameter("ccy")));
      pol.setDbCurrencyRate(getNum(rq.getParameter("ccyrate")));
      pol.setDbPremiBase(getNum(rq.getParameter("premibase")));
      pol.setDbPremiRate(getNum(rq.getParameter("premi_rate")));
      pol.setDtPeriodStart(getDate(rq.getParameter("perstart")));
      pol.setDtPeriodEnd(getDate(rq.getParameter("perend")));

      pol.setStBusinessSourceCode(getString(rq.getParameter("bussrc")));
      pol.setStRegionID(getString(rq.getParameter("reg")));
      pol.setStCaptiveFlag(getFlag(rq.getParameter("captive")));
      pol.setStInwardFlag(getFlag(rq.getParameter("inward")));
      //pol.setDbPremiTotal(getNum(rq.getParameter("premitot")));
      if (Tools.isNo(pol.getStPostedFlag()))
         pol.setStPostedFlag(getFlag(rq.getParameter("posted")));

      final DTOList details = pol.getDetails();

      if (pol.getTab().isActive(InsurancePolicyView.TAB_PREMI)) {

         for (int i = 0; i < details.size(); i++) {
            InsurancePolicyItemsView ip = (InsurancePolicyItemsView) details.get(i);

            ip.touch();

            ip.setStInsItemID(getString(rq.getParameter("i_item"+i)));
            ip.setStDescription(getString(rq.getParameter("i_desc"+i)));
            ip.setDbAmount(getNum(rq.getParameter("i_amount"+i)));
            ip.setStEntityID(getString(rq.getParameter("i_ent_id"+i)));
            //ip.setStEntityName(getString(rq.getParameter("i_ent_name"+i)));
         }
      }

      if (pol.getTab().isActive(InsurancePolicyView.TAB_CLAUSULES)) {
         final DTOList clausules = pol.getClausules();

         for (int i = 0; i < clausules.size(); i++) {
            InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);

            if (getChecked(rq.getParameter("clselect"+i))) icl.select(); else icl.deSelect();

            icl.touch();

            icl.setDbRate(getNum(rq.getParameter("clrate"+i)));
            icl.setDbAmount(getNum(rq.getParameter("clrateamount"+i)));
         }
      }

      final DTOList objects = pol.getObjects();
       /*
      final Long vehIndex = getLong(rq.getParameter("vehindex"));
      if (vehIndex!=null) {
         final InsurancePolicyVehicleView veh = (InsurancePolicyVehicleView)objects.get(vehIndex.intValue());

         veh.touch();

         veh.setStVehicleTypeDesc(getString(rq.getParameter("vehtype")));
         veh.setStPoliceRegNo(getString(rq.getParameter("vregno")));
         veh.setLgYearProduction(getLong(rq.getParameter("vyear")));
         veh.setStChassisNo(getString(rq.getParameter("vchassno")));
         veh.setStEngineNo(getString(rq.getParameter("vengno")));
         veh.setLgSeatNumber(getLong(rq.getParameter("vseatnum")));
         veh.setDbInsuredAmount(getNum(rq.getParameter("vinsamount")));
         veh.setDbPremiRate(getNum(rq.getParameter("vpremirate")));
         veh.setDbPremiAmount(getNum(rq.getParameter("vpremiamount")));
         veh.setDbPremiAmount(getNum(rq.getParameter("vpremiamount")));
      }*/

      final Long oclIndex = getLong(rq.getParameter("oclindex"));
      if(oclIndex!=null) {
         final InsurancePolicyObjectView obj = (InsurancePolicyObjectView)objects.get(oclIndex.intValue());

         final DTOList clausules = obj.getClausules();

         for (int i = 0; i < clausules.size(); i++) {
            InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);
            icl.touch();

            icl.setDbRate(getNum(rq.getParameter("oclrate"+i)));
            icl.setDbAmount(getNum(rq.getParameter("oclrateamount"+i)));
         }
      }

      if (rq.getParameter("ownerinfo")!=null) {

         pol.getOwner().touch();

         final EntityView owner = pol.getOwner().getEntity();

         if (owner.isUnModified()) owner.markUpdate();

         //owner.setStEntityName(getString(rq.getParameter("oxcustno")));
         owner.setStEntityName(getString(rq.getParameter("oxfullname")));
         owner.setStFirstName(getString(rq.getParameter("oxfirstname")));
         owner.setStMiddleName(getString(rq.getParameter("oxmidname")));
         owner.setStLastName(getString(rq.getParameter("oxlastname")));
         owner.setStTitle(getString(rq.getParameter("oxtitle")));
         owner.setStSexID(getString(rq.getParameter("oxsex_id")));
         owner.setDtBirthDate(getDate(rq.getParameter("oxbirth_date")));

         final DTOList addresses = owner.getAddresses();

         if (addresses!=null)
            for (int i = 0; i < addresses.size(); i++) {
               EntityAddressView adr = (EntityAddressView) addresses.get(i);

               if (adr.isUnModified()) adr.markUpdate();

               adr.setStAddress(getString(rq.getParameter("axaddress")));
            }
      }


      pol.recalculate();

      return pol;
   }

   public void selectAgent(HttpServletRequest rq)  throws Exception {
      final String key = getString(rq.getParameter("key"));  
	  
      HashMap lovParam = new HashMap();

      if (key!=null) {
         EntityFilter f = new EntityFilter();

         f.stKey = key;

         final DTOList list = getRemoteInsurance().searchAgents(f);

         rq.setAttribute("LIST",list);
      }
   }

   public void chgTab(HttpServletRequest rq)  throws Exception {
      final InsurancePolicyView pol = retrieve(rq);

      pol.getTab().setActiveTab(getString(rq.getParameter("ctab_changetab")));

      populate(rq);
   }

   public void chgObject(HttpServletRequest rq)  throws Exception {
      final InsurancePolicyView pol = retrieve(rq);
      final String objSel = getString(rq.getParameter("objsel"));

      final DTOList objects = pol.getObjects();

      if(objSel!=null) {

         final int n = Integer.parseInt(objSel);

         if (n>=0)
            objects.select((DTO)objects.get(n));

      }

      populate(rq);
   }

   public void addObject(HttpServletRequest rq)  throws Exception {
      final InsurancePolicyView pol = retrieve(rq);

      final InsurancePolicyObjectView obj = pol.createObject();

      final DTOList objects = pol.getObjects();

      objects.add(obj);

      objects.select(obj);

      populate(rq);
   }

   public void delObject(HttpServletRequest rq)  throws Exception {
      final InsurancePolicyView pol = retrieve(rq);

      final DTOList objects = pol.getObjects();

      objects.delete(objects.indexOf(objects.getSelected()));

      objects.select(null);

      populate(rq);
   }

   public void addOwnerAddress(HttpServletRequest rq)  throws Exception {
   }

   private static HashSet formList = null;

   public void printPolicy(HttpServletRequest rq, HttpServletResponse rp) throws Exception {

        loadFormList(rq);

        final String policyid = rq.getParameter("policyid");

        final String fontsize = rq.getParameter("fontsize");

        final String attached = rq.getParameter("attached");

        final String authorized = rq.getParameter("authorized");

        String alter = getString(rq.getParameter("alter"));

        final String xlang = rq.getParameter("xlang");

        final InsurancePolicyView policy = getRemoteInsurance().getInsurancePolicyForPrinting(policyid, alter);

        String vs = getString(rq.getParameter("vs"));

        ref1 = LOVManager.getInstance().getRef1("POL_PRINTING_" + vs, alter);

        refPropMap = Tools.getPropMap(ref1);

        String isNotLock = (String) refPropMap.get("NO_LOCK_PDF");

        rq.setAttribute("POLICY", policy);
        rq.setAttribute("FONTSIZE", fontsize);
        rq.setAttribute("attached", attached);
        rq.setAttribute("authorized", authorized);
        rq.setAttribute("xlang", xlang);

        if (Tools.isYes(isNotLock)) {
            rq.setAttribute("LOCK_PDF", "N");
        }

        if (alter.equals("STANDARD") || alter.equals("PRINTALL")) {
            rq.setAttribute("SAVE_TO_FILE", "Y");
            rq.setAttribute("FILE_NAME", policy.getStPolicyNo());
        }

        final ArrayList plist = new ArrayList();

        String nom = getString(rq.getParameter("nom"));


        String poltypeOverride = getString(rq.getParameter("poltype"));


        if (alter == null) {
            throw new RuntimeException("ALT code not specified");
        }

        if (alter == null) {
            alter = "";
        } else {
            alter = "_alt" + alter;
        }

        if (poltypeOverride != null) {
            plist.add(poltypeOverride + "_" + policy.getStStatus() + alter);
            plist.add(poltypeOverride + alter);
        }

        plist.add(policy.getStPolicyTypeID() + "_" + policy.getStStatus() + alter + "_" + policy.getStCostCenterCode() + "_" + attached);
        plist.add(policy.getStPolicyTypeID() + "_" + policy.getStStatus() + alter + "_" + policy.getStCostCenterCode());
        plist.add(policy.getStPolicyTypeID() + "_" + policy.getStStatus() + alter);
        plist.add("0" + "_" + policy.getStStatus() + alter + "_" + policy.getStCostCenterCode());
        plist.add("0" + "_" + policy.getStStatus() + alter);

        plist.add(policy.getStPolicyTypeID() + alter + "_" + policy.getStCostCenterCode() + "_" + attached);
        plist.add(policy.getStPolicyTypeID() + alter + "_" + policy.getStCostCenterCode());
        plist.add(policy.getStPolicyTypeID() + alter + "_" + attached);
        plist.add(policy.getStPolicyTypeID() + alter);
        plist.add("0" + alter + "_" + policy.getStCostCenterCode() + "_" + attached);
        plist.add("0" + alter + "_" + policy.getStCostCenterCode());
        plist.add("0" + alter + "_" + attached);
        plist.add("0" + alter);


        //plist.add("0"+"_"+policy.getStStatus());
        //plist.add("0");

        String urx = null;

        logger.logDebug("printPolicy: scanlist:" + plist);

        for (int i = 0; i < plist.size(); i++) {
            String s = (String) plist.get(i);

            if (formList.contains("pol" + s + ".fop.jsp")) {
                urx = "/pages/insurance/report/pol" + s + ".fop";
                break;
            }
        }

        if (urx == null) {
            throw new RuntimeException("Unable to find suitable print form");
        }

        if (nom != null) {
            getRemoteInsurance().registerPrintSerial(policy, nom, urx);
        }


        logger.logDebug("printPolicy: forwarding to ########## " + urx);

        rq.getRequestDispatcher(urx).forward(rq, rp);
    }

   private void loadFormList(HttpServletRequest rq) {
      if (formList==null || true) {
         final String[] filez = new File(rq.getSession().getServletContext().getRealPath("/pages/insurance/report")).list();

         formList = new HashSet();

         for (int i = 0; i < filez.length; i++) {
            String s = filez[i];

            formList.add(s);
         }
      }
   }

   public void getNomerator(HttpServletRequest rq)  throws Exception {
      
   }
   
   
  public void selectZone(HttpServletRequest rq)  throws Exception {
      final String key = getString(rq.getParameter("key"));

      if (key!=null) {
         ZoneFilter f = new ZoneFilter();

         f.stKey = key;

         final DTOList list = getRemoteInsurance().searchZone(f);

         rq.setAttribute("LIST",list);
      }
   }
   
  public void searchPolicy(HttpServletRequest rq)  throws Exception {
      final String key = getString(rq.getParameter("key"));

      final SQLAssembler sqa = new SQLAssembler();
      
      String query = "select  distinct a.pol_id as pol_id,a.pol_no as pol_no,a.description as description,"+
					"a.ccy as ccy,a.posted_flag as posted_flag,a.create_date as create_date,a.create_who as create_who,"+
					"a.change_date as change_date,a.change_who as change_who,a.pol_type_id as pol_type_id,a.amount as amount,"+
					"a.period_start as period_start,a.period_end as period_end,a.pol_subtype_id as pol_subtype_id,"+
					"a.premi_base as premi_base,a.premi_total as premi_total,a.premi_rate as premi_rate,"+
					"a.insured_amount as insured_amount,a.policy_date as policy_date,a.bus_source as bus_source,"+
					"a.region_id as region_id,a.captive_flag as captive_flag,a.inward_flag as inward_flag,"+
					"a.premi_netto as premi_netto,a.ccy_rate as ccy_rate,a.cc_code as cc_code,a.entity_id as entity_id,"+
					"a.condition_id as condition_id,a.risk_category_id as risk_category_id,a.cover_type_code as cover_type_code,"+
					"a.f_prodmode as f_prodmode,a.cust_name as cust_name,a.cust_address as cust_address,"+
					"a.master_policy_id as master_policy_id,a.prod_name as prod_name,a.prod_address as prod_address,"+
					"a.prod_id as prod_id,a.ins_policy_type_grp_id as ins_policy_type_grp_id,a.premi_total_adisc as premi_total_adisc,"+
					"a.total_due as total_due,a.ins_period_id as ins_period_id,a.inst_period_id as inst_period_id,"+
					"a.inst_periods as inst_periods,a.period_rate as period_rate,a.ref1 as ref1,a.ref2 as ref2,a.ref3 as ref3,"+
					"a.ref4 as ref4,a.ref5 as ref5,a.ref6 as ref6,a.ref7 as ref7,a.ref8 as ref8,a.ref9 as ref9,a.ref10 as ref10,"+
					"a.ref11 as ref11,a.ref12 as ref12,a.refd1 as refd1,a.refd2 as refd2,a.refd3 as refd3,a.refd4 as refd4,"+
					"a.refd5 as refd5,a.refn1 as refn1,a.refn2 as refn2,a.refn3 as refn3,a.refn4 as refn4,a.refn5 as refn5,"+
					"a.parent_id as parent_id,a.status as status,a.active_flag as active_flag,a.sppa_no as sppa_no,"+
					"a.claim_no as claim_no,a.claim_date as claim_date,a.claim_cause as claim_cause,"+
					"a.claim_cause_desc as claim_cause_desc,a.event_location as event_location,a.claim_person_id as claim_person_id,"+
					"a.claim_person_name as claim_person_name,a.claim_person_address as claim_person_address,"+
					"a.claim_person_status as claim_person_status,a.claim_amount_est as claim_amount_est,"+
					"a.claim_currency as claim_currency,a.claim_loss_status as claim_loss_status,a.claim_benefit as claim_benefit,"+
					"a.claim_documents as claim_documents,a.endorse_date as endorse_date,a.effective_flag as effective_flag,"+
					"a.claim_status as claim_status,a.endorse_notes as endorse_notes,a.print_code as print_code,a.root_id as root_id,"+
					"a.insured_amount_e as insured_amount_e,a.ins_period_base_id as ins_period_base_id,"+
					"a.period_rate_before as period_rate_before,a.ins_period_base_b4 as ins_period_base_b4,"+
					"a.ins_premium_factor_id as ins_premium_factor_id,a.dla_no as dla_no,a.ins_treaty_id as ins_treaty_id,"+
					"a.total_fee as total_fee,a.nd_comm1 as nd_comm1,a.nd_comm2 as nd_comm2,a.nd_comm3 as nd_comm3,"+
					"a.nd_comm4 as nd_comm4,a.nd_brok1 as nd_brok1,a.nd_brok2 as nd_brok2,a.nd_hfee as nd_hfee,"+
					"a.nd_sfee as nd_sfee,a.nd_pcost as nd_pcost,a.nd_update as nd_update,a.nd_brok1pct as nd_brok1pct,"+
					"a.nd_brok2pct as nd_brok2pct,a.nd_hfeepct as nd_hfeepct,a.nd_disc1 as nd_disc1,a.nd_disc2 as nd_disc2,"+
					"a.nd_disc1pct as nd_disc1pct,a.nd_disc2pct as nd_disc2pct,a.odescription as odescription,"+
					"a.pfx_clauses as pfx_clauses,a.pfx_interest as pfx_interest,a.pfx_coverage as pfx_coverage,"+
					"a.pfx_deductible as pfx_deductible,a.claim_ded_amount as claim_ded_amount,a.premi_pay_date as premi_pay_date,"+
					"a.claim_amount as claim_amount,a.claim_amount_approved as claim_amount_approved,a.pla_no as pla_no,"+
					"a.dla_remark as dla_remark,a.claim_cust_amount as claim_cust_amount,"+
					"a.claim_cust_ded_amount as claim_cust_ded_amount,a.claim_ri_amount as claim_ri_amount,"+
					"a.claim_object_id as claim_object_id,"+
					" (coalesce ((select sum(amount_settled) from"+
					" ar_invoice where ar_invoice.refid0='PREMI/'||a.pol_id and commit_flag='Y'),0))as premi_paid,"+
					" (coalesce((select sum(amount) from ar_pol2 where ins_item_cat='COMM' and pol_id=a.pol_id),0)) as ap_comis,"+
					" (coalesce((select sum(amount_settled) from ar_pol2 where ins_item_cat='COMM' and pol_id=a.pol_id),0)) as ap_comis_p,"+
					" (coalesce((select sum(amount) from ar_pol2 where ins_item_cat='BROKR' and pol_id=a.pol_id ),0)) as ap_brok,"+
					" (coalesce((select sum(amount_settled) from ar_pol2 where"+
					" ins_item_cat='BROKR' and pol_id=a.pol_id),0)) as ap_brok_p    from"+        
					" ins_policy a         left join ent_master c on c.ent_id = a.entity_id  where a.status"+
					" in ('POLICY','ENDORSE','RENEWAL') and a.effective_flag = 'Y'";
         
		 

         //String gquery = sqa.getSQL();
        
         //sqa=new SQLAssembler();

         sqa.addQuery(" from ("+query+") x");
         sqa.addSelect("x.*");
         sqa.addClause(" premi_paid+ap_comis_p+ap_brok_p < premi_netto+ap_comis+ap_brok");


      //sqa.addSelect("*");

      //sqa.addQuery("from ar_invoice");

      if (key!=null){
         sqa.addClause("upper(invoice_no) like ?");
         sqa.addPar("%"+key.toUpperCase()+"%");
      }

      /*if (cust!=null) {
         sqa.addClause("ent_id = ?");
         sqa.addPar(cust);
      }*/

      final DTOList l = sqa.getList(InsurancePolicyView.class);

      rq.setAttribute("LIST",l);
   }
  
  public void previewPolicy(HttpServletRequest rq,HttpServletResponse rp)  throws Exception {

      loadFormList(rq);

      final String policyid = rq.getParameter("policyid");
      
      final String fontsize = rq.getParameter("fontsize");
      
      final String attached = rq.getParameter("attached");
      
      final String authorized = rq.getParameter("authorized");
      
      final String preview = rq.getParameter("preview");

      final String xlang = rq.getParameter("xlang");

      final InsurancePolicyView policy = getRemoteInsurance().getInsurancePolicyForPreview(policyid);

      rq.setAttribute("POLICY",policy);
      rq.setAttribute("FONTSIZE",fontsize);
      rq.setAttribute("attached",attached);
      rq.setAttribute("authorized",authorized);
      rq.setAttribute("preview",preview);
      rq.setAttribute("xlang", xlang);

      final ArrayList plist = new ArrayList();

      String nom = getString(rq.getParameter("nom"));


      String poltypeOverride = getString(rq.getParameter("poltype"));
      String alter = getString(rq.getParameter("alter"));

      if (alter==null) throw new RuntimeException("ALT code not specified");

      if (alter==null) alter=""; else alter="_alt"+alter;

        plist.add(policy.getStPolicyTypeID() + "_" + policy.getStStatus() + alter + "_" + policy.getStCostCenterCode() + "_" + attached);
        plist.add(policy.getStPolicyTypeID() + "_" + policy.getStStatus() + alter + "_" + policy.getStCostCenterCode());
        plist.add(policy.getStPolicyTypeID() + "_" + policy.getStStatus() + alter);
        plist.add("0" + "_" + policy.getStStatus() + alter+ "_" + policy.getStCostCenterCode());
        plist.add("0" + "_" + policy.getStStatus() + alter);

        plist.add(policy.getStPolicyTypeID() + alter + "_" + policy.getStCostCenterCode() + "_" + attached);
        plist.add(policy.getStPolicyTypeID() + alter + "_" + policy.getStCostCenterCode());
        plist.add(policy.getStPolicyTypeID() + alter + "_" + attached);
        plist.add(policy.getStPolicyTypeID() + alter);
        plist.add("0" + alter + "_" + policy.getStCostCenterCode() + "_" + attached);
        plist.add("0" + alter + "_" + policy.getStCostCenterCode());
        plist.add("0" + alter + "_" + attached);
        plist.add("0" + alter);


      //plist.add("0"+"_"+policy.getStStatus());
      //plist.add("0");

      String urx=null;

      //logger.logDebug("printPolicy: scanlist:"+plist);

      for (int i = 0; i < plist.size(); i++) {
         String s = (String) plist.get(i);

         if (formList.contains("pol"+s+".fop.jsp")) {
            urx = "/pages/insurance/report/pol"+s+".fop";
            break;
         }
      }

      if (urx==null) throw new RuntimeException("Unable to find suitable print form");

      //if (nom!=null)
         //getRemoteInsurance().registerPrintSerial(policy, nom, urx);


      logger.logDebug("printPolicy: forwarding to ########## "+urx);

      rq.getRequestDispatcher(urx).forward(rq,rp);
   }
  
  public void searchPolicyNew(HttpServletRequest rq)  throws Exception {
      final String key = getString(rq.getParameter("key"));
      final String lks = getString(rq.getParameter("lks"));
      final String costcenter = getString(rq.getParameter("costcenter"));

      final SQLAssembler sqa = new SQLAssembler();
      
      sqa.addSelect("a.pol_id,a.status,a.pol_no, premi_total");   
      sqa.addQuery("from ins_policy a"+
                    " inner join ar_invoice b on a.pol_id = b.attr_pol_id");

      sqa.addClause("b.amount - coalesce(b.amount_settled,0) > 0");
      sqa.addClause("a.effective_flag = 'Y'");
      sqa.addClause("a.active_flag = 'Y'");
      sqa.addClause("status in ('POLICY','RENEWAL','ENDORSE')");
      sqa.addClause("a.claim_adv_payment_amount is null");
      
      if(costcenter!=null){
          sqa.addClause("a.cc_code = ?");
          sqa.addPar(costcenter);
      }

      if (key!=null){
         sqa.addClause("upper(a.pol_no) like ?");
         sqa.addPar("%"+key.toUpperCase()+"%");
         
      }

      sqa.setLimit(30);

      final DTOList l = sqa.getList(InsurancePolicyView.class);

      rq.setAttribute("LIST",l);
   }

  public void searchKlaimNew(HttpServletRequest rq)  throws Exception {
      final String key = getString(rq.getParameter("key"));
      final String lks = getString(rq.getParameter("lks"));
      final String costcenter = getString(rq.getParameter("costcenter"));
      final String arsid = getString(rq.getParameter("arsid"));

      final SQLAssembler sqa = new SQLAssembler();

      //case when a.claim_status = 'DLA' then b.amount else

      sqa.addSelect("a.pol_id,ar_invoice_id as parent_id,a.claim_status,a.pol_no,case when a.claim_status = 'DLA' then a.dla_no else a.pla_no end as dla_no,"+
                    " case when ar_invoice_id is not null then (select sum(case when x.negative_flag='Y' then amount*-1 else amount end) from ar_invoice_details x where x.ar_invoice_id = b.ar_invoice_id) else a.claim_amount end as claim_amount_approved,"+
                    " a.approved_date,b.used_flag as active_flag, b.receipt_date as premi_pay_date,b.receipt_no,a.claim_payment_used_f,a.claim_adv_payment_amount");
      sqa.addQuery("from ins_policy a left join ar_invoice b on a.pol_id = b.attr_pol_id and b.ar_trx_type_id in (12,26) and coalesce(b.cancel_flag,'') <> 'Y' ");
      
      //sqa.addClause("a.effective_flag = 'Y'");
      sqa.addClause("a.active_flag = 'Y'");

      if(arsid.equalsIgnoreCase("10"))
            sqa.addClause("a.status in ('CLAIM','CLAIM ENDORSE','CLAIM INWARD','ENDORSE CLAIM INWARD') ");
      else if(arsid.equalsIgnoreCase("47"))
            sqa.addClause("a.status in ('CLAIM INWARD','ENDORSE CLAIM INWARD') ");
      //sqa.addClause("a.claim_adv_payment_amount is null");
      //sqa.addClause("a.claim_payment_used_f is null");
      //sqa.addClause("coalesce(b.cancel_flag,'') <> 'Y'");
      
      if(costcenter!=null){
          if(!costcenter.equalsIgnoreCase("00")){
                sqa.addClause(" a.cc_code in (?,'80')");
                sqa.addPar(costcenter);
          }
      }
      
      if (key!=null){
         sqa.addClause("upper(a.pol_no) like ?");
         sqa.addPar("%"+key.toUpperCase()+"%");
      }

      if (lks!=null){
         sqa.addClause("(upper(a.pla_no) like ? or upper(a.dla_no) like ?)");

         String lksKey = lks.toUpperCase().replaceAll("LKP", "");
         lksKey = lks.toUpperCase().replaceAll("LKS", "");
         
         sqa.addPar("%"+lksKey.toUpperCase()+"%");
         sqa.addPar("%"+lksKey.toUpperCase()+"%");
         
         if(lks.toUpperCase().startsWith("LKS")) sqa.addClause("a.claim_status = 'PLA'");
         if(lks.toUpperCase().startsWith("LKP")) sqa.addClause("a.claim_status = 'DLA'");
      }

      sqa.addOrder("pol_id desc");

      if(key==null && lks==null)
          sqa.setLimit(0);
      else
          sqa.setLimit(30);

      final DTOList l = sqa.getList(InsurancePolicyView.class);

      rq.setAttribute("LIST",l);
   }

   public void searchPolicyOnly(HttpServletRequest rq)  throws Exception {
      final String key = getString(rq.getParameter("key"));
      final String lks = getString(rq.getParameter("lks"));
      final String costcenter = getString(rq.getParameter("costcenter"));

      final SQLAssembler sqa = new SQLAssembler();

      sqa.addSelect("*");
      sqa.addQuery("from ins_policy a");

      sqa.addClause("a.active_flag = 'Y'");
      sqa.addClause("status in ('POLICY','RENEWAL','ENDORSE')");

      if(costcenter!=null){
          sqa.addClause("a.cc_code = ?");
          sqa.addPar(costcenter);
      }

      if (key!=null){
         sqa.addClause("upper(a.pol_no) like ?");
         sqa.addPar("%"+key.toUpperCase()+"%");

      }

      sqa.setLimit(50);

      final DTOList l = sqa.getList(InsurancePolicyView.class);

      rq.setAttribute("LIST",l);
   }

   public void printPolicyWithDigital(HttpServletRequest rq,HttpServletResponse rp)  throws Exception {

      loadFormList(rq);

      final String policyid = rq.getParameter("policyid");

      final String fontsize = rq.getParameter("fontsize");

      final String attached = rq.getParameter("attached");

      final String authorized = rq.getParameter("authorized");

      String alter = getString(rq.getParameter("alter"));

      String digitalSign = rq.getParameter("digitalsign");

      final String xlang = rq.getParameter("xlang");

      final InsurancePolicyView policy = getRemoteInsurance().getInsurancePolicyForPrintingWithDigitalSign(policyid, alter);

      String vs = getString(rq.getParameter("vs"));

      ref1 = LOVManager.getInstance().getRef1("POL_PRINTING_"+vs, alter);

      refPropMap = Tools.getPropMap(ref1);

      String isNotLock = (String)refPropMap.get("NO_LOCK_PDF");

      rq.setAttribute("POLICY",policy);
      rq.setAttribute("FONTSIZE",fontsize);
      rq.setAttribute("attached",attached);
      rq.setAttribute("authorized",authorized);
      rq.setAttribute("digitalsign",digitalSign);
      rq.setAttribute("xlang", xlang);

      if(Tools.isYes(isNotLock)) rq.setAttribute("LOCK_PDF","N");

      final ArrayList plist = new ArrayList();

      String nom = getString(rq.getParameter("nom"));

      String poltypeOverride = getString(rq.getParameter("poltype"));

      if (alter==null) throw new RuntimeException("ALT code not specified");

      if (alter==null) alter=""; else alter="_alt"+alter;

      if (poltypeOverride!=null) {
         plist.add(poltypeOverride+"_"+policy.getStStatus()+alter);
         plist.add(poltypeOverride+alter);
      }

        plist.add(policy.getStPolicyTypeID() + "_" + policy.getStStatus() + alter + "_" + policy.getStCostCenterCode() + "_" + attached);
        plist.add(policy.getStPolicyTypeID() + "_" + policy.getStStatus() + alter + "_" + policy.getStCostCenterCode());
        plist.add(policy.getStPolicyTypeID() + "_" + policy.getStStatus() + alter);
        plist.add("0" + "_" + policy.getStStatus() + alter+ "_" + policy.getStCostCenterCode());
        plist.add("0" + "_" + policy.getStStatus() + alter);

        plist.add(policy.getStPolicyTypeID() + alter + "_" + policy.getStCostCenterCode() + "_" + attached);
        plist.add(policy.getStPolicyTypeID() + alter + "_" + policy.getStCostCenterCode());
        plist.add(policy.getStPolicyTypeID() + alter + "_" + attached);
        plist.add(policy.getStPolicyTypeID() + alter);
        plist.add("0" + alter + "_" + policy.getStCostCenterCode() + "_" + attached);
        plist.add("0" + alter + "_" + policy.getStCostCenterCode());
        plist.add("0" + alter + "_" + attached);
        plist.add("0" + alter);


      //plist.add("0"+"_"+policy.getStStatus());
      //plist.add("0");

      String urx=null;

      //logger.logDebug("printPolicy: scanlist:"+plist);

      for (int i = 0; i < plist.size(); i++) {
         String s = (String) plist.get(i);

         if (formList.contains("pol"+s+".fop.jsp")) {
            urx = "/pages/insurance/report/pol"+s+".fop";
            break;
         }
      }

      if (urx==null) throw new RuntimeException("file format pencetakan tidak ditemukan");

      if (nom!=null)
         getRemoteInsurance().registerPrintSerial(policy, nom, urx);


      logger.logDebug("printPolicy: forwarding to ########## "+urx);

      rq.getRequestDispatcher(urx).forward(rq,rp);
   }

   public void printDigitalPolicy(HttpServletRequest rq,HttpServletResponse rp)  throws Exception {

          loadFormList2(rq);

          final String policyid = rq.getParameter("policyid");

          final String fontsize = rq.getParameter("fontsize");

          final String attached = rq.getParameter("attached");

          final String authorized = rq.getParameter("authorized");

          String alter = getString(rq.getParameter("alter"));

          String digitalSign = rq.getParameter("digitalsign");

          final String xlang = rq.getParameter("xlang");

          final InsurancePolicyView policy = getRemoteInsurance().getInsurancePolicyForPrintingWithDigitalSign(policyid, alter);

          String vs = getString(rq.getParameter("vs"));

          ref1 = LOVManager.getInstance().getRef1("POL_PRINTING_"+vs, alter);

          refPropMap = Tools.getPropMap(ref1);

          String isNotLock = (String)refPropMap.get("NO_LOCK_PDF");

          rq.setAttribute("POLICY",policy);
          rq.setAttribute("FONTSIZE",fontsize);
          rq.setAttribute("attached",attached);
          rq.setAttribute("authorized",authorized);
          rq.setAttribute("digitalsign",digitalSign);
          rq.setAttribute("xlang", xlang);

          if(Tools.isYes(isNotLock)) rq.setAttribute("LOCK_PDF","N");

          if(alter.equals("STANDARD") || alter.equals("PRINTALL")){
                rq.setAttribute("SAVE_TO_FILE","Y");
                rq.setAttribute("FILE_NAME", policy.getStPolicyNo());
          }

          String docType = alter;

          final ArrayList plist = new ArrayList();

          String nom = getString(rq.getParameter("nom"));

          String poltypeOverride = getString(rq.getParameter("poltype"));

          if (alter==null) throw new RuntimeException("ALT code not specified");

          if (alter==null) alter=""; else alter="_alt"+alter;

          if (poltypeOverride!=null) {
             plist.add(poltypeOverride+"_"+policy.getStStatus()+alter);
             plist.add(poltypeOverride+alter);
          }

            plist.add(policy.getStPolicyTypeID() + "_" + policy.getStStatus() + alter + "_" + policy.getStCostCenterCode() + "_" + attached);
            plist.add(policy.getStPolicyTypeID() + "_" + policy.getStStatus() + alter + "_" + policy.getStCostCenterCode());
            plist.add(policy.getStPolicyTypeID() + "_" + policy.getStStatus() + alter);
            plist.add("0" + "_" + policy.getStStatus() + alter+ "_" + policy.getStCostCenterCode());
            plist.add("0" + "_" + policy.getStStatus() + alter);

            plist.add(policy.getStPolicyTypeID() + alter + "_" + policy.getStCostCenterCode() + "_" + attached);
            plist.add(policy.getStPolicyTypeID() + alter + "_" + policy.getStCostCenterCode());
            plist.add(policy.getStPolicyTypeID() + alter + "_" + attached);
            plist.add(policy.getStPolicyTypeID() + alter);
            plist.add("0" + alter + "_" + policy.getStCostCenterCode() + "_" + attached);
            plist.add("0" + alter + "_" + policy.getStCostCenterCode());
            plist.add("0" + alter + "_" + attached);
            plist.add("0" + alter);


          //plist.add("0"+"_"+policy.getStStatus());
          //plist.add("0");

          String urx = null;
          String urxSave = null;

          //logger.logDebug("printPolicy: scanlist:"+plist);

          for (int i = 0; i < plist.size(); i++) {
             String s = (String) plist.get(i);

             if (formList.contains("pol"+s+".fop.jsp")) {
                urx = "/pages/insurance/report2/pol"+s+".fop";
                urxSave = "/pages/insurance/report2/pol"+s+"_SAVE.fop";
                break;
             }
          }

          if (urx==null) throw new RuntimeException("file format pencetakan tidak ditemukan");

          if (nom!=null)
             getRemoteInsurance().registerPrintSerial(policy, nom, urx);
 
          logger.logDebug("print polis digital : forwarding to ########## "+urx);

          if(docType.equals("STANDARD") || docType.equals("PRINTALL")){
              FOPServlet srv = new FOPServlet();
              srv.saveFOP(rq,rp, urxSave);
          }

           rq.getRequestDispatcher(urx).forward(rq,rp);
   }

   private void loadFormList2(HttpServletRequest rq) {
      if (formList==null || true) {
         final String[] filez = new File(rq.getSession().getServletContext().getRealPath("/pages/insurance/report2")).list();

         formList = new HashSet();

         for (int i = 0; i < filez.length; i++) {
            String s = filez[i];

            formList.add(s);
         }
      }
   }

   public void searchKlaimNewVld(HttpServletRequest rq) throws Exception {
        final String key = getString(rq.getParameter("key"));
        final String lks = getString(rq.getParameter("lks"));
        final String costcenter = getString(rq.getParameter("costcenter"));
        final String jenpol = getString(rq.getParameter("jenpol"));
        final String dateFrom = getString(rq.getParameter("dateFrom"));
        final String dateTo = getString(rq.getParameter("dateTo"));

        final SQLAssembler sqa = new SQLAssembler();

        //case when a.claim_status = 'DLA' then b.amount else

        sqa.addSelect("a.pol_id,ar_invoice_id as parent_id,a.claim_status,a.pol_no,case when a.claim_status = 'DLA' then a.dla_no else a.pla_no end as dla_no,a.f_validate_claim,"
                + " case when ar_invoice_id is not null then b.amount else a.claim_amount end as claim_amount_approved, a.approved_date,a.active_flag, b.receipt_date as premi_pay_date,b.receipt_no");

        sqa.addQuery("from ins_policy a left join ar_invoice b on a.pol_id = b.attr_pol_id and b.ar_trx_type_id = 12 ");

        //sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause("a.active_flag = 'Y'");
        sqa.addClause("a.status in ('CLAIM','CLAIM ENDORSE') ");
        sqa.addClause("a.claim_adv_payment_amount is null");
        sqa.addClause("coalesce(b.cancel_flag,'') <> 'Y'");
        //sqa.addClause("a.f_validate_claim is null");

        if (costcenter != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(costcenter);
        }

        if (jenpol != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(jenpol);
        }

        if (key != null) {
            sqa.addClause("upper(a.pol_no) like ?");
            sqa.addPar("%" + key.toUpperCase() + "%");
        }

        if (lks != null) {
            sqa.addClause("(upper(a.pla_no) like ? or upper(a.dla_no) like ?)");

            String lksKey = lks.toUpperCase().replaceAll("LKP", "");
            lksKey = lks.toUpperCase().replaceAll("LKS", "");

            sqa.addPar("%" + lksKey.toUpperCase() + "%");
            sqa.addPar("%" + lksKey.toUpperCase() + "%");

            if (lks.toUpperCase().startsWith("LKS")) {
                sqa.addClause("a.claim_status = 'PLA'");
            }
            if (lks.toUpperCase().startsWith("LKP")) {
                sqa.addClause("a.claim_status = 'DLA'");
            }
        }

        if (dateFrom != null) {
            sqa.addClause("date_trunc('day',coalesce(a.dla_date,a.pla_date)) >= ?");
            sqa.addPar(DateUtil.getDate(dateFrom));
        }

        if (dateTo != null) {
            sqa.addClause("date_trunc('day',coalesce(a.dla_date,a.pla_date)) <= ?");
            sqa.addPar(DateUtil.getDate(dateTo));
        }

        sqa.addOrder("pol_id desc");

        //sqa.setLimit(30);

        final DTOList l = sqa.getList(InsurancePolicyView.class);

        rq.setAttribute("LIST", l);
    }

   public void printClosing(HttpServletRequest rq, HttpServletResponse rp) throws Exception {

        loadFormList(rq);

        final String closingid = rq.getParameter("closingid");
        String alter = getString(rq.getParameter("alter"));

        final InsuranceClosingView closing = getRemoteInsurance().getClosingForPrinting(closingid);

        rq.setAttribute("CLOSING", closing);

        final ArrayList plist = new ArrayList();

        if (alter == null) {
            throw new RuntimeException("ALT code not specified");
        }

        if (alter == null) {
            alter = "";
        } else {
            alter = "_alt" + alter;
        }

        plist.add(alter);

        String urx = null;

        logger.logDebug("printPolicy: scanlist:" + plist);

        for (int i = 0; i < plist.size(); i++) {
            String s = (String) plist.get(i);

            if (formList.contains("clo" + s + ".fop.jsp")) {
                urx = "/pages/insurance/report/clo" + s + ".fop";
                break;
            }
        }

        if (urx == null) {
            throw new RuntimeException("Unable to find suitable print form");
        }

        logger.logDebug("printPolicy: forwarding to ########## " + urx);

        rq.getRequestDispatcher(urx).forward(rq, rp);
    }

   public void printProposal(HttpServletRequest rq, HttpServletResponse rp) throws Exception {

        loadFormList(rq);

        final String receiptid = rq.getParameter("receiptid");

        final uploadProposalCommView receipt = getRemoteInsurance().getProposalForPrinting(receiptid);

        rq.setAttribute("PROPOSAL", receipt);

//        final ArrayList plist = new ArrayList();
//
//        plist.add(receipt.getStInsuranceUploadID());

        String urx = null;

//        for (int i = 0; i < plist.size(); i++) {
//            String s = (String) plist.get(i);
//
//            if (formList.contains("rcp" + s + ".fop.jsp")) {
//                urx = "/pages/ar/report/prop_comm_" + s + ".fop";
//                break;
//            }
//        }

        urx = "/pages/insurance/report/prop_comm.fop";

        if (urx == null) {
            throw new RuntimeException("Unable to find suitable print form");
        }

        logger.logDebug("print: forwarding to ########## " + urx);

        rq.getRequestDispatcher(urx).forward(rq, rp);
    }

    public void printProposalExcel(HttpServletRequest rq, HttpServletResponse rp) throws Exception {

        final String receiptid = rq.getParameter("receiptid");

        getRemoteInsurance().getProposalForPrintingExcel(receiptid);

    }

    

    public void printPiutangExcel(HttpServletRequest rq, HttpServletResponse rp) throws Exception {

        final String receiptid = rq.getParameter("receiptid");

        getRemoteInsurance().getPiutangPremiExcel(receiptid);

    }

    public void printPiutang(HttpServletRequest rq, HttpServletResponse rp) throws Exception {

        loadFormList(rq);

        final String receiptid = rq.getParameter("receiptid");

        final uploadPiutangPremiView receipt = getRemoteInsurance().getPiutangForPrinting(receiptid);

        rq.setAttribute("PROPOSAL", receipt);

//        final ArrayList plist = new ArrayList();
//
//        plist.add(receipt.getStInsuranceUploadID());

        String urx = null;

//        for (int i = 0; i < plist.size(); i++) {
//            String s = (String) plist.get(i);
//
//            if (formList.contains("rcp" + s + ".fop.jsp")) {
//                urx = "/pages/ar/report/prop_comm_" + s + ".fop";
//                break;
//            }
//        }

        urx = "/pages/insurance/report/piutangpremi.fop";

        if (urx == null) {
            throw new RuntimeException("Unable to find suitable print form");
        }

        logger.logDebug("print: forwarding to ########## " + urx);

        rq.getRequestDispatcher(urx).forward(rq, rp);
    }

   public void printByPemasaran(HttpServletRequest rq, HttpServletResponse rp) throws Exception {

        loadFormList(rq);

        final String receiptid = rq.getParameter("receiptid");

        final BiayaPemasaranView receipt = getRemoteInsurance().getPemasaranForPrinting(receiptid);

        rq.setAttribute("PEMASARAN", receipt);

        String urx = null;

        urx = "/pages/insurance/report/biaya_pemasaran.fop";

        if (urx == null) {
            throw new RuntimeException("Unable to find suitable print form");
        }

        logger.logDebug("print: forwarding to ########## " + urx);

        rq.getRequestDispatcher(urx).forward(rq, rp);
    }

    public void printByPemasaranExcel(HttpServletRequest rq, HttpServletResponse rp) throws Exception {

        final String receiptid = rq.getParameter("receiptid");

        getRemoteInsurance().getPemasaranForPrintingExcel(receiptid);

    }

    public void printByPemasaranDoc(HttpServletRequest rq, HttpServletResponse rp) throws Exception {

        final String receiptid = rq.getParameter("receiptid");
        final BiayaPemasaranView receipt = getRemoteInsurance().getPemasaranForPrintingMix(receiptid);

        if (Tools.isNo(receipt.getStStatus4())) {
            throw new RuntimeException("Tidak bisa dicetak karena belum setujui Kadiv");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String sf = sdf.format(receipt.getDtEntryDate());

        // reads input file from an absolute path
        String filePath1 = "D:/fin-repository/report_temp/" + sf + "/proposal_biaya" + receiptid + ".pdf";
        String filePath2 = "D:/fin-repository/report_temp/" + sf + "/approval_biaya" + receiptid + ".pdf";

        List<InputStream> list = new ArrayList<InputStream>();
        list.add(new FileInputStream(new File(filePath1)));
        list.add(new FileInputStream(new File(filePath2)));

        String pathFinal = "D:/fin-repository/report/mix_biaya" + receiptid + ".pdf";
        File downloadFile = new File(pathFinal);

        // Resulting pdf
        OutputStream out2 = new FileOutputStream(new File(pathFinal));

        //GABUNGIN POLIS & WORDING
        PDFMerge merge = new PDFMerge();
        merge.doMerge(list, out2);

        rp.setContentType("application/pdf");
        rp.addHeader("Content-Disposition", "attachment; filename=mix_biaya" + receiptid + ".pdf");
        rp.setHeader("Pragma", "token");
        rp.setContentLength((int) downloadFile.length());

        FileInputStream fileInputStream = new FileInputStream(downloadFile);
        OutputStream responseOutputStream = rp.getOutputStream();
        int bytes;
        while ((bytes = fileInputStream.read()) != -1) {
            responseOutputStream.write(bytes);
        }
    }

    public void printProposalDoc(HttpServletRequest rq, HttpServletResponse rp) throws Exception {

        final String receiptid = rq.getParameter("receiptid");
        final uploadProposalCommView receipt = getRemoteInsurance().getProposalForPrintingMix(receiptid);

        if (Tools.isNo(receipt.getStStatus4())) {
            throw new RuntimeException("Tidak bisa dicetak karena belum setujui Kadiv");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String sf = sdf.format(receipt.getDtCreateDate());

        // reads input file from an absolute path
        String filePath1 = "D:/fin-repository/report_temp/" + sf + "/proposal_komisi" + receiptid + ".pdf";
        String filePath2 = "D:/fin-repository/report_temp/" + sf + "/approval_komisi" + receiptid + ".pdf";

        List<InputStream> list = new ArrayList<InputStream>();
        list.add(new FileInputStream(new File(filePath1)));
        list.add(new FileInputStream(new File(filePath2)));

        String pathFinal = "D:/fin-repository/report/mix_komisi" + receiptid + ".pdf";
        File downloadFile = new File(pathFinal);

        // Resulting pdf
        OutputStream out2 = new FileOutputStream(new File(pathFinal));

        //GABUNGIN POLIS & WORDING
        PDFMerge merge = new PDFMerge();
        merge.doMerge(list, out2);

        rp.setContentType("application/pdf");
        rp.addHeader("Content-Disposition", "attachment; filename=mix_komisi" + receiptid + ".pdf");
        rp.setHeader("Pragma", "token");
        rp.setContentLength((int) downloadFile.length());

        FileInputStream fileInputStream = new FileInputStream(downloadFile);
        OutputStream responseOutputStream = rp.getOutputStream();
        int bytes;
        while ((bytes = fileInputStream.read()) != -1) {
            responseOutputStream.write(bytes);
        }
    }

    public void printByPemasaranDoc_Dynamic(HttpServletRequest rq, HttpServletResponse rp) throws Exception {

        final String receiptid = rq.getParameter("receiptid");
        final BiayaPemasaranView pemasaran = getRemoteInsurance().getPemasaranForPrintingMix(receiptid);

        if (Tools.isNo(pemasaran.getStStatus4())) {
            throw new RuntimeException("Tidak bisa dicetak karena belum setujui Kadiv");
        }

        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {

            final Statement S = conn.createStatement();

            /* Define the SQL query */
            ResultSet query_set = S.executeQuery(
                    " select a.pms_id,status1,status2,status3,status4,reverse_flag,cc_code,"
                    + "no_spp,ket,jumlah_data,total_biaya,ket,round(sum(b.excess_amount),2) as pajak  "
                    + "from biaya_pemasaran a "
                    + "inner join biaya_pemasaran_detail b on b.pms_id = a.pms_id "
                    + " where a.pms_id = " + receiptid
                    + " group by a.pms_id,status1,status2,status3,status4,reverse_flag,cc_code,"
                    + " no_spp,ket,jumlah_data,total_biaya,ket order by a.pms_id ");

            String fileName = "proposal_biaya" + receiptid;
            File fo = new File("C:/");

            String fileFOlder = Parameter.readString("SYS_FILES_FOLDER");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

            String sf = sdf.format(new Date());
            String tempPath = fileFOlder + File.separator + "report_temp" + File.separator + sf;
            String path1 = fileFOlder + File.separator + "report_temp" + File.separator;
            String pathTemp = tempPath + File.separator + fileName + ".pdf";

            try {
                new File(path1).mkdir();
                new File(tempPath).mkdir();
            } catch (Exception e) {
            }

            fo = new File(pathTemp);

            FileOutputStream fop = new FileOutputStream(fo);

            /* Step-2: Initialize PDF documents - logical objects */
            /* Step-2: Initialize PDF documents - logical objects */
            Document my_pdf_report = new Document();
            PdfWriter writer = PdfWriter.getInstance(my_pdf_report, fop);
            //PdfWriter.getInstance(my_pdf_report, fop);
            my_pdf_report.open();

            //we have five columns in our table
            PdfPTable my_report_logo = new PdfPTable(5);
            my_report_logo.setWidthPercentage(100);

            PdfPTable my_report_header = new PdfPTable(2);
            my_report_header.setWidthPercentage(100);

            PdfPTable my_report_table = new PdfPTable(4);
            my_report_table.setWidthPercentage(100);

            PdfPTable my_report_footer = new PdfPTable(5);
            my_report_footer.setWidthPercentage(100);
            //create a cell object

            Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12),
                    smallbold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD),
                    smallc = new Font(Font.FontFamily.TIMES_ROMAN, 11),
                    small10 = new Font(Font.FontFamily.TIMES_ROMAN, 10),
                    small10bold = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD),
                    small6 = new Font(Font.FontFamily.TIMES_ROMAN, 6);

            //insert heading
            String bulan = DateUtil.getMonth2Digit(pemasaran.getDtEntryDate());
            String tahun = DateUtil.getYear(pemasaran.getDtEntryDate());
            final String no_surat[] = pemasaran.getStNoSPP().split("[\\/]");
            final String norut = no_surat[0] + "/" + Parameter.readString("BRANCH_SHORT_" + pemasaran.getStCostCenterCode()).toUpperCase() + "-BIAYA/" + bulan + "/" + tahun;

            Image askridaLogoPath = Image.getInstance(Parameter.readString("DIGITAL_POLIS_LOGO_PIC"));

            PdfPCell logo = null;
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            logo.setColspan(2);
            my_report_logo.addCell(logo);
            logo = new PdfPCell(askridaLogoPath);
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_logo.addCell(logo);
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            logo.setColspan(2);
            my_report_logo.addCell(logo);
            my_report_logo.completeRow();

            PdfPCell headerJudul = null;
            headerJudul = new PdfPCell(new Phrase("SURAT IZIN PENGELUARAN DANA", smallbold));
            headerJudul.setBorder(Rectangle.NO_BORDER);
            headerJudul.setColspan(2);
            headerJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_header.addCell(headerJudul);
            my_report_header.completeRow();

            PdfPCell headerNo = null;
            headerNo = new PdfPCell(new Phrase("No.", small));
            headerNo.setBorder(Rectangle.NO_BORDER);
            headerNo.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerNo);
            headerNo = new PdfPCell(new Phrase(": " + norut, small));
            headerNo.setBorder(Rectangle.NO_BORDER);
            headerNo.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerNo);

            PdfPCell headerName = null;
            headerName = new PdfPCell(new Phrase("Kepada Yth.", small));
            headerName.setBorder(Rectangle.NO_BORDER);
            headerName.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerName);
            headerName = new PdfPCell(new Phrase(": Kadiv. Keuangan", small));
            headerName.setBorder(Rectangle.NO_BORDER);
            headerName.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerName);

            PdfPCell headerTanggal = null;
            headerTanggal = new PdfPCell(new Phrase("Tanggal", small));
            headerTanggal.setBorder(Rectangle.NO_BORDER);
            headerTanggal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerTanggal);
            headerTanggal = new PdfPCell(new Phrase(": " + LanguageManager.getInstance().translate(DateUtil.getDateStr(pemasaran.getDtEntryDate(), "dd ^^ yyyy")), small));
            headerTanggal.setBorder(Rectangle.NO_BORDER);
            headerTanggal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerTanggal);

            PdfPCell headerDaerah = null;
            headerDaerah = new PdfPCell(new Phrase("Daerah", small));
            headerDaerah.setBorder(Rectangle.NO_BORDER);
            headerDaerah.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerDaerah);
            headerDaerah = new PdfPCell(new Phrase(": Cabang " + pemasaran.getCostCenter(pemasaran.getStCostCenterCode()).getStDescription(), small));
            headerDaerah.setBorder(Rectangle.NO_BORDER);
            headerDaerah.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerDaerah);

            String perihal = null;
            String keterangan = null;
            String taxType = null;
            String taxTypeDesc = null;
            BigDecimal dbPajak = new BigDecimal(0);
            BigDecimal dbPajakdet = new BigDecimal(0);
            boolean isrevFlag = false;
            while (query_set.next()) {
                isrevFlag = Tools.isYes(query_set.getString("reverse_flag"));
                dbPajak = query_set.getBigDecimal("pajak");
                keterangan = query_set.getString("ket");
            }

            if (isrevFlag) {
                perihal = ": Pengeluaran Biaya Pemasaran No. SBP " + pemasaran.getStNoSPP() + " (Revisi)";
            } else {
                perihal = ": Pengeluaran Biaya Pemasaran No. SBP " + pemasaran.getStNoSPP();
            }

            PdfPCell headerHal = null;
            headerHal = new PdfPCell(new Phrase("Perihal", small));
            headerHal.setBorder(Rectangle.NO_BORDER);
            headerHal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerHal);
            headerHal = new PdfPCell(new Phrase(perihal, small));
            headerHal.setBorder(Rectangle.NO_BORDER);
            headerHal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_header.addCell(headerHal);
            my_report_header.completeRow();

            //insert an empty row
            PdfPCell emptyRow = new PdfPCell(new Phrase(" ", small));
            emptyRow.setBorder(Rectangle.NO_BORDER);
            emptyRow.setColspan(2);
            emptyRow.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_header.addCell(emptyRow);
            my_report_header.completeRow();

            PdfPCell kolomJudul = null;
            kolomJudul = new PdfPCell(new Phrase("No", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(kolomJudul);
            kolomJudul = new PdfPCell(new Phrase("Uraian Pengeluaran", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(kolomJudul);
            kolomJudul = new PdfPCell(new Phrase("Nominal", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(kolomJudul);
            kolomJudul = new PdfPCell(new Phrase("Keterangan", smallbold));
            kolomJudul.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(kolomJudul);

            PdfPCell headerIsi = null;
            headerIsi = new PdfPCell(new Phrase("1", small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(headerIsi);
            headerIsi = new PdfPCell(new Phrase("Biaya Pemasaran bulan " + LanguageManager.getInstance().translate(DateUtil.getDateStr(pemasaran.getDtEntryDate(), "^^ yyyy")) + " (Rincian Terlampir)", small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(headerIsi);
            headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(BDUtil.sub(pemasaran.getDbTotalBiaya(), dbPajak), 2), small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table.addCell(headerIsi);
            headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(keterangan), small));
            headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(headerIsi);

            ResultSet query_tax = S.executeQuery(
                    " select row_number() over(order by a.pms_id,b.tax_type)+1 as no,b.tax_type,round(sum(b.excess_amount),2) as pajak  "
                    + "from biaya_pemasaran a inner join biaya_pemasaran_detail b on b.pms_id = a.pms_id "
                    + "where a.pms_id = " + receiptid
                    + " and b.excess_amount <> 0 group by a.pms_id,b.tax_type order by b.tax_type ");

            while (query_tax.next()) {
                taxType = query_tax.getString("tax_type");

                if (taxType.equalsIgnoreCase("1")) {
                    taxTypeDesc = "Pph 21";
                } else {
                    taxTypeDesc = "Pph 23";
                }

                BigDecimal no = query_tax.getBigDecimal("no");
                headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(no, 0), small));
                headerIsi.setHorizontalAlignment(Element.ALIGN_CENTER);
                my_report_table.addCell(headerIsi);
                headerIsi = new PdfPCell(new Phrase(taxTypeDesc, small));
                headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
                my_report_table.addCell(headerIsi);
                dbPajakdet = query_tax.getBigDecimal("pajak");
                headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(dbPajakdet, 2), small));
                headerIsi.setHorizontalAlignment(Element.ALIGN_RIGHT);
                my_report_table.addCell(headerIsi);
                headerIsi = new PdfPCell(new Phrase(" ", small));
                headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
                my_report_table.addCell(headerIsi);
            }

//            if (Tools.compare(dbPajak, BDUtil.zero) > 0) {
//                headerIsi = new PdfPCell(new Phrase("2", small));
//                headerIsi.setHorizontalAlignment(Element.ALIGN_CENTER);
//                my_report_table.addCell(headerIsi);
//                headerIsi = new PdfPCell(new Phrase(taxTypeDesc, small));
//                headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
//                my_report_table.addCell(headerIsi);
//                headerIsi = new PdfPCell(new Phrase(JSPUtil.printX(dbPajakdet, 2), small));
//                headerIsi.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                my_report_table.addCell(headerIsi);
//                headerIsi = new PdfPCell(new Phrase(" ", small));
//                headerIsi.setHorizontalAlignment(Element.ALIGN_LEFT);
//                my_report_table.addCell(headerIsi);
//            }

            PdfPCell headerTotal = null;
            headerTotal = new PdfPCell(new Phrase("TOTAL", small));
            headerTotal.setColspan(2);
            headerTotal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(headerTotal);
            headerTotal = new PdfPCell(new Phrase(JSPUtil.printX(pemasaran.getDbTotalBiaya(), 2), small));
            headerTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table.addCell(headerTotal);
            headerTotal = new PdfPCell(new Phrase(" ", small));
            headerTotal.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(headerTotal);

            //insert footer
            String created3 = "Hormat kami,\n"
                    + "PT. ASURANSI BANGUN ASKRIDA \n"
                    + "Cabang " + pemasaran.getCostCenter(pemasaran.getStCostCenterCode()).getStDescription();
            PdfPCell footer1 = new PdfPCell(new Phrase(created3, small));
            footer1.setBorder(Rectangle.NO_BORDER);
            footer1.setColspan(5);
            footer1.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_footer.addCell(footer1);
            my_report_footer.completeRow();

            Image img = Image.getInstance(pemasaran.getUser(Parameter.readString("BRANCH_" + pemasaran.getStCostCenterCode())).getFile().getStFilePath());
            img.scaleToFit(100f, 100f);
            //insert column data
            PdfPCell footer2 = null;
            footer2 = new PdfPCell(new Phrase(" ", small));
            footer2.setColspan(2);
            footer2.setHorizontalAlignment(Element.ALIGN_LEFT);
            footer2.setBorder(Rectangle.NO_BORDER);
            my_report_footer.addCell(footer2);
            footer2 = new PdfPCell(img);
            footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
            footer2.setBorder(Rectangle.NO_BORDER);
            my_report_footer.addCell(footer2);
            footer2 = new PdfPCell(new Phrase(" ", small));
            footer2.setColspan(2);
            footer2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            footer2.setBorder(Rectangle.NO_BORDER);
            my_report_footer.addCell(footer2);

            String created4 = Parameter.readString("BRANCH_SIGN_" + pemasaran.getStCostCenterCode()) + "\n"
                    + pemasaran.getUser(Parameter.readString("BRANCH_" + pemasaran.getStCostCenterCode())).getStJobPosition().toUpperCase();
            PdfPCell footer3 = new PdfPCell(new Phrase(created4, small));
            footer3.setBorder(Rectangle.NO_BORDER);
            footer3.setColspan(5);
            footer3.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_footer.addCell(footer3);
            my_report_footer.completeRow();

            //Create QR Code by using BarcodeQRCode Class
            BarcodeQRCode my_code = new BarcodeQRCode(DateUtil.getDateStr(new Date(), "dd-MMM-yyyy HH:mm:ss") + "_" + pemasaran.getStNoSPP() + "_" + JSPUtil.printX(pemasaran.getDbSaldoBiaya(), 0), 3, 3, null);
            //Get Image corresponding to the input string
            Image qr_image = my_code.getImage();

            PdfPCell barcode = new PdfPCell(qr_image);
            barcode.setBorder(Rectangle.NO_BORDER);
            barcode.setColspan(5);
            barcode.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_footer.addCell(barcode);
            my_report_footer.completeRow();

            my_report_logo.setWidths(new int[]{20, 20, 20, 20, 20});
            my_report_header.setWidths(new int[]{15, 85});
            my_report_table.setWidths(new int[]{5, 30, 20, 45});
            my_report_footer.setWidths(new int[]{20, 20, 20, 20, 20});

            /* Attach report table to PDF */
            my_pdf_report.add(my_report_logo);
            my_pdf_report.add(my_report_header);
            my_pdf_report.add(my_report_table);
            my_pdf_report.add(my_report_footer);
            my_pdf_report.close();

            /* Define the SQL query */
            ResultSet query_set2 = S.executeQuery(
                    " select a.pms_id,status1,status2,status3,status4,reverse_flag,cc_code,no_surat,"
                    + "no_spp,ket,jumlah_data,total_biaya,kabag_approved,ket,round(sum(b.excess_amount),2) as pajak  "
                    + "from biaya_pemasaran a "
                    + "inner join biaya_pemasaran_detail b on b.pms_id = a.pms_id "
                    + " where a.pms_id = " + receiptid
                    + " group by a.pms_id,status1,status2,status3,status4,reverse_flag,cc_code,no_surat,"
                    + " no_spp,ket,jumlah_data,total_biaya,ket order by a.pms_id ");

            String fileName2 = "approval_biaya" + pemasaran.getStPemasaranID();

            String approvedKabag = null;
            String keterangan_app = null;
            String noSurat = null;
            BigDecimal dbPajak_app = new BigDecimal(0);
            while (query_set2.next()) {
                approvedKabag = query_set2.getString("kabag_approved");
                dbPajak_app = query_set2.getBigDecimal("pajak");
                keterangan_app = query_set2.getString("ket");
                noSurat = query_set2.getString("no_surat");
            }

            File fo2 = new File("C:/");
            String pathTemp2 = tempPath + File.separator + fileName2 + ".pdf";

            try {
                new File(path1).mkdir();
                new File(tempPath).mkdir();
            } catch (Exception e) {
            }

            fo2 = new File(pathTemp2);

            FileOutputStream fop2 = new FileOutputStream(fo2);

            /* Step-2: Initialize PDF documents - logical objects */
            Document my_pdf_report2 = new Document();
            PdfWriter writer2 = PdfWriter.getInstance(my_pdf_report2, fop2);
            //PdfWriter.getInstance(my_pdf_report, fop);
            my_pdf_report2.open();

            //we have four columns in our table
            PdfPTable my_report_table_app = new PdfPTable(5);
            my_report_table_app.setWidthPercentage(100);
            PdfPTable my_report_table_app2 = new PdfPTable(4);
            my_report_table_app2.setWidthPercentage(100);
            PdfPTable my_report_table_app4 = new PdfPTable(4);
            my_report_table_app4.setWidthPercentage(100);
            PdfPTable my_report_table_app3 = new PdfPTable(5);
            my_report_table_app3.setWidthPercentage(100);
            //create a cell object

            String requestNo = null;

            if (noSurat != null) {
                requestNo = noSurat;
            } else {
                String counterKey = DateUtil.getYear(pemasaran.getDtEntryDate());
                String rn = String.valueOf(IDFactory.createNumericID("REQBIAYAPMSNO" + counterKey, 1));
                rn = StringTools.leftPad(rn, '0', 3);

                requestNo = rn + "/KEU-BIAYA/" + DateUtil.getMonth2Digit(pemasaran.getDtEntryDate()) + "/" + DateUtil.getYear(pemasaran.getDtEntryDate());

                final SQLUtil SQ = new SQLUtil();
                PreparedStatement PS = SQ.setQuery("update biaya_pemasaran set no_surat = ? where pms_id = ?");

                PS.setObject(1, requestNo);
                PS.setObject(2, pemasaran.getStPemasaranID());

                int p = PS.executeUpdate();
                SQ.release();

            }

            //insert heading
            String Tanggal = "Jakarta, " + LanguageManager.getInstance().translate(DateUtil.getDateStr(pemasaran.getDtEntryDate(), "dd ^^ yyyy"));
            final String norutCab = no_surat[0] + "/" + Parameter.readString("BRANCH_SHORT_" + pemasaran.getStCostCenterCode()).toUpperCase() + "-BIAYA/" + DateUtil.getMonth2Digit(pemasaran.getDtEntryDate()) + "/" + DateUtil.getYear(pemasaran.getDtEntryDate());
            final String norutKP = requestNo;

            PdfPCell logo_app = null;
            logo_app = new PdfPCell(new Phrase(" ", small));
            logo_app.setBorder(Rectangle.NO_BORDER);
            logo_app.setColspan(2);
            my_report_table_app.addCell(logo_app);
            logo_app = new PdfPCell(askridaLogoPath);
            logo_app.setBorder(Rectangle.NO_BORDER);
            logo_app.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table_app.addCell(logo_app);
            logo_app = new PdfPCell(new Phrase(" ", small));
            logo_app.setBorder(Rectangle.NO_BORDER);
            logo_app.setColspan(2);
            my_report_table_app.addCell(logo_app);

            PdfPCell shkTanggal = null;
            shkTanggal = new PdfPCell(new Phrase("No. : " + norutKP, small));
            shkTanggal.setBorder(Rectangle.NO_BORDER);
            shkTanggal.setHorizontalAlignment(Element.ALIGN_LEFT);
            shkTanggal.setColspan(2);
            my_report_table_app.addCell(shkTanggal);
            shkTanggal = new PdfPCell(new Phrase(Tanggal, small));
            shkTanggal.setBorder(Rectangle.NO_BORDER);
            shkTanggal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            shkTanggal.setColspan(3);
            my_report_table_app.addCell(shkTanggal);

            //insert heading
            String created1 = "Kepada Yth.\n"
                    + "Kepala Cabang " + pemasaran.getCostCenter(pemasaran.getStCostCenterCode()).getStDescription() + "\n"
                    + pemasaran.getCostCenter(pemasaran.getStCostCenterCode()).getStAddress() + "\n\n\n";
            PdfPCell heading = new PdfPCell(new Phrase(created1, small));
            heading.setBorder(Rectangle.NO_BORDER);
            heading.setColspan(2);
            heading.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table_app.addCell(heading);
            heading = new PdfPCell(new Phrase(" ", small));
            heading.setBorder(Rectangle.NO_BORDER);
            heading.setHorizontalAlignment(Element.ALIGN_CENTER);
            heading.setColspan(3);
            my_report_table_app.addCell(heading);

            //insert judul
            PdfPCell Title = new PdfPCell(new Phrase("Perihal : Persetujuan Pengeluaran Dana",
                    smallbold));
            Title.setBorder(Rectangle.NO_BORDER);
            Title.setColspan(5);
            Title.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table_app.addCell(Title);

            //insert an empty row
            PdfPCell emptyRow_app = new PdfPCell(new Phrase(" ", small));
            emptyRow_app.setBorder(Rectangle.NO_BORDER);
            emptyRow_app.setColspan(5);
            emptyRow_app.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table_app.addCell(emptyRow_app);

            //insert isi
//            String created2 = "Menindaklanjuti surat dari Kantor Cabang " + pemasaran.getCostCenter(pemasaran.getStCostCenterCode()).getStDescription() + " No. " + norutCab + " perihal izin pengeluaran dana sebesar Rp. " + JSPUtil.printX(pemasaran.getDbTotalBiaya(), 2) + ",- "
//                    + "dengan ini Direksi menyetujui pengeluaran dana tersebut dan bukti pendukung transaksi agar diadministrasikan dengan tertib. "
//                    + "Untuk selanjutnya setiap penarikan dana dari bank agar sesuai dengan surat Direksi No. 444/DIR/VIII/2014.\n\n"
//                    + "Demikian kami sampaikan, atas perhatiannya kami ucapkan terima kasih.\n\n"
//                    + "Hormat kami,";
//            PdfPCell bySistem = new PdfPCell(new Phrase(created2, small));
//            bySistem.setBorder(Rectangle.NO_BORDER);
//            bySistem.setColspan(5);
//            bySistem.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
//            my_report_table_app.addCell(bySistem);

            String created2 = "Menindaklanjuti surat dari Kantor Cabang " + pemasaran.getCostCenter(pemasaran.getStCostCenterCode()).getStDescription() + " No. " + norutCab + " perihal izin pengeluaran dana, "
                    + "dengan ini Direksi menyetujui pengeluaran dana sebagai berikut: ";
            PdfPCell bySistem = new PdfPCell(new Phrase(created2, small));
            bySistem.setBorder(Rectangle.NO_BORDER);
            bySistem.setColspan(4);
            bySistem.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            my_report_table_app2.addCell(bySistem);

            PdfPCell kolomJudul_app = null;
            kolomJudul_app = new PdfPCell(new Phrase("No", smallbold));
            kolomJudul_app.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table_app2.addCell(kolomJudul_app);
            kolomJudul_app = new PdfPCell(new Phrase("Uraian Pengeluaran", smallbold));
            kolomJudul_app.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table_app2.addCell(kolomJudul_app);
            kolomJudul_app = new PdfPCell(new Phrase("Nominal", smallbold));
            kolomJudul_app.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table_app2.addCell(kolomJudul_app);
            kolomJudul_app = new PdfPCell(new Phrase("Keterangan", smallbold));
            kolomJudul_app.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table_app2.addCell(kolomJudul_app);

            PdfPCell headerIsi_app = null;
            headerIsi_app = new PdfPCell(new Phrase("1", small));
            headerIsi_app.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table_app2.addCell(headerIsi_app);
            headerIsi_app = new PdfPCell(new Phrase("Biaya Pemasaran bulan " + LanguageManager.getInstance().translate(DateUtil.getDateStr(pemasaran.getDtEntryDate(), "^^ yyyy")) + " (Rincian Terlampir)", small));
            headerIsi_app.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table_app2.addCell(headerIsi_app);
            headerIsi_app = new PdfPCell(new Phrase(JSPUtil.printX(BDUtil.sub(pemasaran.getDbTotalBiaya(), dbPajak_app), 2), small));
            headerIsi_app.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table_app2.addCell(headerIsi_app);
            headerIsi_app = new PdfPCell(new Phrase(JSPUtil.printX(keterangan_app), small));
            headerIsi_app.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table_app2.addCell(headerIsi_app);

            ResultSet query_tax2 = S.executeQuery(
                    " select row_number() over(order by a.pms_id,b.tax_type)+1 as no,b.tax_type,round(sum(b.excess_amount),2) as pajak  "
                    + "from biaya_pemasaran a inner join biaya_pemasaran_detail b on b.pms_id = a.pms_id "
                    + "where a.pms_id = " + receiptid
                    + " and b.excess_amount <> 0 group by a.pms_id,b.tax_type order by b.tax_type ");

            String taxType2 = null;
            String taxTypeDesc2 = null;
            BigDecimal dbPajakdet2 = new BigDecimal(0);
            while (query_tax2.next()) {
                taxType2 = query_tax2.getString("tax_type");

                if (taxType2.equalsIgnoreCase("1")) {
                    taxTypeDesc2 = "Pph 21";
                } else {
                    taxTypeDesc2 = "Pph 23";
                }

                BigDecimal no = query_tax2.getBigDecimal("no");
                headerIsi_app = new PdfPCell(new Phrase(JSPUtil.printX(no, 0), small));
                headerIsi_app.setHorizontalAlignment(Element.ALIGN_CENTER);
                my_report_table_app2.addCell(headerIsi_app);
                headerIsi_app = new PdfPCell(new Phrase(taxTypeDesc2, small));
                headerIsi_app.setHorizontalAlignment(Element.ALIGN_LEFT);
                my_report_table_app2.addCell(headerIsi_app);
                dbPajakdet2 = query_tax2.getBigDecimal("pajak");
                headerIsi_app = new PdfPCell(new Phrase(JSPUtil.printX(dbPajakdet2, 2), small));
                headerIsi_app.setHorizontalAlignment(Element.ALIGN_RIGHT);
                my_report_table_app2.addCell(headerIsi_app);
                headerIsi_app = new PdfPCell(new Phrase("Pembayaran dilakukan bulan berikutnya pada kas negara", small));
                headerIsi_app.setHorizontalAlignment(Element.ALIGN_LEFT);
                my_report_table_app2.addCell(headerIsi_app);
            }

//            if (Tools.compare(dbPajak, BDUtil.zero) > 0) {
//                headerIsi_app = new PdfPCell(new Phrase("2", small));
//                headerIsi_app.setHorizontalAlignment(Element.ALIGN_CENTER);
//                my_report_table_app2.addCell(headerIsi_app);
//                headerIsi_app = new PdfPCell(new Phrase(taxTypeDesc, small));
//                headerIsi_app.setHorizontalAlignment(Element.ALIGN_LEFT);
//                my_report_table_app2.addCell(headerIsi_app);
//                headerIsi_app = new PdfPCell(new Phrase(JSPUtil.printX(dbPajak, 2), small));
//                headerIsi_app.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                my_report_table_app2.addCell(headerIsi_app);
//                headerIsi_app = new PdfPCell(new Phrase("Pembayaran dilakukan bulan berikutnya pada kas negara", small));
//                headerIsi_app.setHorizontalAlignment(Element.ALIGN_LEFT);
//                my_report_table_app2.addCell(headerIsi_app);
//            }

            PdfPCell headerTotal_app = null;
            headerTotal_app = new PdfPCell(new Phrase("TOTAL", small));
            headerTotal_app.setColspan(2);
            headerTotal_app.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table_app2.addCell(headerTotal_app);
            headerTotal_app = new PdfPCell(new Phrase(JSPUtil.printX(pemasaran.getDbTotalBiaya(), 2), small));
            headerTotal_app.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table_app2.addCell(headerTotal_app);
            headerTotal_app = new PdfPCell(new Phrase(" ", small));
            headerTotal_app.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table_app2.addCell(headerTotal_app);

            String created = "Bukti pendukung transaksi agar diadministrasikan dengan tertib. "
                    + "Untuk selanjutnya setiap penarikan dana dari bank agar sesuai dengan surat Direksi No. 444/DIR/VIII/2014.\n\n"
                    + "Demikian kami sampaikan, atas perhatiannya kami ucapkan terima kasih.\n\n"
                    + "Hormat kami,";
            PdfPCell bySistem2 = new PdfPCell(new Phrase(created, small));
            bySistem2.setBorder(Rectangle.NO_BORDER);
            bySistem2.setColspan(4);
            bySistem2.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            my_report_table_app2.addCell(bySistem2);

            //insert footer
//            String created3 = "PT. ASURANSI BANGUN ASKRIDA\n"
//                    + "DIVISI KEUANGAN";
            String created3_app = "PT. ASURANSI BANGUN ASKRIDA";
            PdfPCell footer1_app = new PdfPCell(new Phrase(created3_app, smallbold));
            footer1_app.setBorder(Rectangle.NO_BORDER);
            footer1_app.setColspan(4);
            footer1_app.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table_app2.addCell(footer1_app);

            Image imgKeuKabag = Image.getInstance(Parameter.readString("PROPOSAL_KEU_" + approvedKabag));
            Image imgKeuKasie = Image.getInstance(Parameter.readString("PROPOSAL_KASIE_KEU"));
            PdfPCell footer4 = new PdfPCell(imgKeuKabag);
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table_app4.addCell(footer4);
            footer4 = new PdfPCell(new Phrase("DIVISI KEUANGAN", smallbold));
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table_app4.addCell(footer4);
            footer4 = new PdfPCell(imgKeuKasie);
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table_app4.addCell(footer4);
            footer4 = new PdfPCell(new Phrase(" ", small));
            footer4.setBorder(Rectangle.NO_BORDER);
            footer4.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table_app4.addCell(footer4);

            Image img_app = Image.getInstance(pemasaran.getUser(Parameter.readString("FINANCE_DIVISION_HEAD")).getFile().getStFilePath());
            //insert column data
            PdfPCell footer2_app = new PdfPCell(img_app);
            footer2_app.setBorder(Rectangle.NO_BORDER);
            footer2_app.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table_app3.addCell(footer2_app);
            footer2_app = new PdfPCell(new Phrase(" ", small));
            footer2_app.setBorder(Rectangle.NO_BORDER);
            footer2_app.setHorizontalAlignment(Element.ALIGN_CENTER);
            footer2_app.setColspan(4);
            my_report_table_app3.addCell(footer2_app);

            String created4_app = pemasaran.getUser(Parameter.readString("FINANCE_DIVISION_HEAD")).getStUserName().toUpperCase() + "\n"
                    + "Kepala Divisi";
            PdfPCell footer3_app = new PdfPCell(new Phrase(created4_app, smallbold));
            footer3_app.setBorder(Rectangle.NO_BORDER);
            footer3_app.setHorizontalAlignment(Element.ALIGN_LEFT);
            footer3_app.setColspan(5);
            my_report_table_app3.addCell(footer3_app);

            //Create QR Code by using BarcodeQRCode Class
            BarcodeQRCode my_code2 = new BarcodeQRCode(DateUtil.getDateStr(pemasaran.getDtEntryDate(), "dd-MMM-yyyy HH:mm:ss") + "_" + pemasaran.getStNoSPP() + "_" + JSPUtil.printX(pemasaran.getDbSaldoBiaya(), 0), 3, 3, null);
            //Get Image corresponding to the input string
            Image qr_image2 = my_code2.getImage();

            PdfPCell barcode_app = new PdfPCell(qr_image2);
            barcode_app.setBorder(Rectangle.NO_BORDER);
            barcode_app.setColspan(5);
            barcode_app.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table_app3.addCell(barcode_app);

            my_report_table_app.setWidths(new int[]{20, 20, 20, 20, 20});
            my_report_table_app2.setWidths(new int[]{5, 30, 20, 45});
            my_report_table_app3.setWidths(new int[]{20, 20, 20, 20, 20});
            my_report_table_app4.setWidths(new int[]{3, 23, 3, 71});

            /* Attach report table to PDF */
            my_pdf_report2.add(my_report_table_app);
            my_pdf_report2.add(my_report_table_app2);
            my_pdf_report2.add(my_report_table_app4);
            my_pdf_report2.add(my_report_table_app3);
            my_pdf_report2.close();

            /* Close all DB related objects */
            query_set.close();
            query_tax.close();
            query_set2.close();
            query_tax2.close();
            S.close();

            List<InputStream> list = new ArrayList<InputStream>();
            list.add(new FileInputStream(fo));
            list.add(new FileInputStream(fo2));

            String pathFinal = "D:/fin-repository/report/mix_biaya" + receiptid + ".pdf";
            File downloadFile = new File(pathFinal);

            // Resulting pdf
            OutputStream out2 = new FileOutputStream(new File(pathFinal));

            //GABUNGIN POLIS & WORDING
            PDFMerge merge = new PDFMerge();
            merge.doMerge(list, out2);

            rp.setContentType("application/pdf");
            rp.addHeader("Content-Disposition", "attachment; filename=mix_biaya" + receiptid + ".pdf");
            rp.setHeader("Pragma", "token");
            rp.setContentLength((int) downloadFile.length());

            FileInputStream fileInputStream = new FileInputStream(downloadFile);
            OutputStream responseOutputStream = rp.getOutputStream();
            int bytes;
            while ((bytes = fileInputStream.read()) != -1) {
                responseOutputStream.write(bytes);
            }

            //HAPUS FILE TEXT DI LOCAL SERVER CORE
            fo = new File(fileName + ".pdf");
            boolean del = fo.delete();
            if (del) {
                logger.logWarning("delete file di core server " + fo.getName() + "...");
            } else {
                logger.logWarning("gagal delete file di core server " + fo.getName() + "...");
            }

            fo2 = new File(fileName2 + ".pdf");
            boolean del2 = fo2.delete();
            if (del2) {
                logger.logWarning("delete file di core server " + fo2.getName() + "...");
            } else {
                logger.logWarning("gagal delete file di core server " + fo2.getName() + "...");
            }

            boolean del3 = downloadFile.delete();
            if (del3) {
                logger.logWarning("delete file di core server " + downloadFile.getName() + "...");
            } else {
                logger.logWarning("gagal delete file di core server " + downloadFile.getName() + "...");
            }


        } finally {
            conn.close();
        }
    }

    public void searchKlaimCashflow(HttpServletRequest rq)  throws Exception {
      final String key = getString(rq.getParameter("key"));
      final String lks = getString(rq.getParameter("lks"));
      final String costcenter = getString(rq.getParameter("costcenter"));

      final SQLAssembler sqa = new SQLAssembler();

      //case when a.claim_status = 'DLA' then b.amount else

      sqa.addSelect("a.pol_id,ar_invoice_id as parent_id,a.claim_status,a.pol_no,case when a.claim_status = 'DLA' then a.dla_no else a.pla_no end as dla_no,"+
                    " case when ar_invoice_id is not null then (select sum(case when x.negative_flag='Y' then amount*-1 else amount end) from ar_invoice_details x where x.ar_invoice_id = b.ar_invoice_id) else a.claim_amount end as claim_amount_approved,"+
                    " a.approved_date,b.used_flag as active_flag, b.receipt_date as premi_pay_date,b.receipt_no,a.claim_payment_used_f,a.claim_adv_payment_amount");
      sqa.addQuery("from ins_policy a left join ar_invoice b on a.pol_id = b.attr_pol_id and b.ar_trx_type_id in (12,26) and coalesce(b.cancel_flag,'') <> 'Y' ");

      sqa.addClause("a.claim_status  = 'DLA'");
      sqa.addClause("a.effective_flag = 'Y'");
      sqa.addClause("a.active_flag = 'Y'");
      sqa.addClause("a.status in ('CLAIM') ");
      //sqa.addClause("a.claim_adv_payment_amount is null");
      //sqa.addClause("a.claim_payment_used_f is null");
      //sqa.addClause("coalesce(b.cancel_flag,'') <> 'Y'");

      if(costcenter!=null){
          if(!costcenter.equalsIgnoreCase("00")){
                sqa.addClause("a.cc_code = ?");
                sqa.addPar(costcenter);
          }
      }

      if (key!=null){
         sqa.addClause("upper(a.pol_no) like ?");
         sqa.addPar("%"+key.toUpperCase()+"%");
      }

      if (lks!=null){
         sqa.addClause("(upper(a.pla_no) like ? or upper(a.dla_no) like ?)");

         String lksKey = lks.toUpperCase().replaceAll("LKP", "");
         lksKey = lks.toUpperCase().replaceAll("LKS", "");

         sqa.addPar("%"+lksKey.toUpperCase()+"%");
         sqa.addPar("%"+lksKey.toUpperCase()+"%");

         if(lks.toUpperCase().startsWith("LKS")) sqa.addClause("a.claim_status = 'PLA'");
         if(lks.toUpperCase().startsWith("LKP")) sqa.addClause("a.claim_status = 'DLA'");
      }

      sqa.addOrder("pol_id desc");

      sqa.setLimit(30);

      final DTOList l = sqa.getList(InsurancePolicyView.class);

      rq.setAttribute("LIST",l);
   }

    public void downloadPolisJiwa(HttpServletRequest rq, HttpServletResponse rp)  throws Exception {

      final String nopolis = rq.getParameter("nopolis");
      final String norek = rq.getParameter("norek");
      final String entid = rq.getParameter("entid");
      final String policyid = rq.getParameter("policyid");

      //logger.logDebug("############# no polis jiwa = "+ nopolis);
      //logger.logDebug("############# norek = "+ norek);
      //logger.logDebug("############# entid = "+ entid);
      logger.logDebug("############# policyid = "+ policyid);

      final InsurancePolicyView policy = getRemoteInsurance().getInsurancePolicy(policyid);
      
      //String fn = "//webapps.askrida.co.id/fin-repository/POLIS-PAJ/"+nopolis+".pdf";
      String fn = "//webapps.askrida.co.id/fin-repository/POLIS-PAJ/"+ policy.getStPolicyNo() +".pdf";

      //CEK DULU UDAH ADA BELUM FILE POLIS NYA, JIKA SUDAH MAKA AMBIL YG ADA
      File file = new File(fn);

      if(file.exists() && file.canRead()){

          logger.logDebug("############# FILE E-POLIS SUDAH ADA SEBELUMNYA = "+ policy.getStPolicyNo());

          enableCache(rp);

          rp.setContentType("application/pdf");
          rp.setHeader("Content-Disposition","attachment; filename="+ policy.getStPolicyNo() +".pdf;");
          
          FileInputStream fi=null;
          ServletOutputStream os=null;

          try {
             fi = new FileInputStream(fn);

             os = rp.getOutputStream();

             byte[] buf = new byte [4096];

             while (fi.available()>0) {
                int n = fi.read(buf);
                os.write(buf,0,n);
             }

          } finally {
             if (fi!=null) fi.close();
             //if (os!=null) os.close();
          }
      }else{

          //KALAU TIDAK ADA FILE, REQUEST KE API JIWA
          logger.logDebug("############# FILE E-POLIS BELUM ADA, REQUEST KE API JIWA = "+ policy.getStPolicyNo());

          //EntityView ent = getEntity(entid);
          EntityView ent = getEntity(policy.getStEntityID());

          //Request polis ke PAJ
          //requestPolisToPAJ(norek, nopolis, ent.getCompany().getStVSCode(), ent.getStEntityID());

          requestPolisMergeToPAJ(ent.getCompany().getStVSCode(), ent.getStEntityID(), policyid, policy);

          enableCache(rp);

          rp.setContentType("application/pdf");
          rp.setHeader("Content-Disposition","attachment; filename="+ policy.getStPolicyNo() +".pdf;");
          //rp.setContentLength((int) file.getDbOriginalSize().longValue());

          FileInputStream fi=null;
          ServletOutputStream os=null;

          String fn2 = "\\\\webapps.askrida.co.id\\fin-repository\\POLIS-PAJ\\"+ policy.getStPolicyNo() +".pdf";

          File fileCek = new File(fn2);

          if(!fileCek.exists())
              throw new RuntimeException("Gagal dapat polis asuransi Jiwa");

          try {
             fi = new FileInputStream(fn2);

             os = rp.getOutputStream();

             byte[] buf = new byte [4096];

             while (fi.available()>0) {
                int n = fi.read(buf);
                os.write(buf,0,n);
             }

          } catch (IOException e) {
              System.out.println(e);
            }finally {
             if (fi!=null) fi.close();
             //if (os!=null) os.close();
          }
      }

      
   }

    public static void enableCache(HttpServletResponse httpServletResponse) {
      // Set to expire far in the past.
      httpServletResponse.setHeader("Expires", null);

      // Set standard HTTP/1.1 no-cache headers.
      httpServletResponse.setHeader("Cache-Control", null);

      // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
      httpServletResponse.addHeader("Cache-Control", null);

      // Set standard HTTP/1.0 no-cache header.
      httpServletResponse.setHeader("Pragma", null);
   }

    public EntityView getEntity(String stEntID) {

        EntityView entity = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntID);

        return entity;
    }

    public void requestPolisToPAJ(String nomorLoan, String noPolis, String kodeBank, String namaBank)  throws Exception{
        try {

                        logger.logDebug("############### HIT API DOWNLOAD POLIS PAJ INTERNAL ####################### ");

                        URL url = new URL("http://192.168.250.62:8100/AsJiwaWS/downloadPolisPAJ");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoOutput(true);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json");

                        /*
                        {
                            "kode_bank": "BJB1",
                            "nomor_loan": "REK/2/2024",
                            "nomor_polis": "POLICY-QS-241107-N873NZCY"
                        }*/

                        String jsonRequest = "{"+
                                       "\"kode_bank\": \""+ kodeBank +"\","+
                                       "\"kode_bank_cabang\": \""+ namaBank +"\","+
                                       "\"nomor_loan\": \""+ nomorLoan +"\","+
                                       "\"nomor_polis\": \""+ noPolis +"\""+
                                       "}";

                        logger.logDebug("############## JSON REQUEST ASKRIDA = " + jsonRequest);

                        // For POST only - START
                            conn.setDoOutput(true);
                            OutputStream os = conn.getOutputStream();
                            os.write(jsonRequest.getBytes());
                            os.flush();
                            os.close();
                            // For POST only - END

                            int responseCode = conn.getResponseCode();
                            logger.logDebug("POST Response Code :: " + responseCode);

                            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                    String inputLine;
                                    StringBuffer response = new StringBuffer();

                                    while ((inputLine = in.readLine()) != null) {
                                            response.append(inputLine);
                                    }
                                    in.close();

                                    // print result
                                    logger.logDebug("############### BALIKAN ####################### ");
                                    logger.logDebug("############### RESPONSE  = "+ response.toString());

                                    JSONParser parser = new JSONParser();
                                    JSONObject json = (JSONObject) parser.parse(response.toString());
                                    logger.logDebug("code = "+ json.get("code"));
                                    logger.logDebug("status = "+ json.get("status"));
                                    logger.logDebug("keterangan = "+ json.get("message"));

                                    if(!json.get("code").toString().equalsIgnoreCase("00")){
                                        throw new RuntimeException("Gagal download e-polis dari Asuransi Jiwa ");
                                    }


                            } else {
                                    System.out.println("POST request did not work.");
                            }

                        if (conn.getResponseCode() != 200) {
                            throw new RuntimeException("Failed : HTTP Error code : "
                                    + conn.getResponseCode());
                        }

                        conn.disconnect();

                    }catch (MalformedURLException e) {
                        e.printStackTrace();
                    } 
    }
 
    public void requestPolisMergeToPAJ(String kodeBank, String namaBank, String policyid, InsurancePolicyView pol)  throws Exception{
        try {

                        logger.logDebug("############### HIT API DOWNLOAD POLIS PAJ INTERNAL ####################### ");

                        URL url = new URL("http://192.168.250.62:8100/AsJiwaWS/downloadMergePolisPAJ");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoOutput(true);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json");

                        /*
                        {
                            "kode_bank": "BJB1",
                            "nomor_loan": "REK/2/2024",
                            "nomor_polis": "POLICY-QS-241107-N873NZCY"
                        }*/

                        JSONObject obj = new JSONObject();
                        obj.put("no_polis", pol.getStPolicyNo());

                        JSONArray list = new JSONArray();

                        final DTOList objects = pol.getObjects();

                        for (int i = 0; i < objects.size(); i++) {
                             InsurancePolicyObjectView object = (InsurancePolicyObjectView) objects.get(i);

                             InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) object;


                            JSONObject obj2 = new JSONObject();

                            obj2.put("kode_bank", kodeBank);
                            obj2.put("kode_bank_cabang", namaBank);
                            obj2.put("nomor_loan", objx.getStReference16());
                            obj2.put("nomor_polis", objx.getStReference30());

                            list.add(obj2);

                        }

                        obj.put("data", list);

                        logger.logDebug("############## JSON REQUEST ASKRIDA = " + obj.toJSONString());

                        // For POST only - START
                            conn.setDoOutput(true);
                            OutputStream os = conn.getOutputStream();
                            os.write(obj.toJSONString().getBytes());
                            os.flush();
                            os.close();
                            // For POST only - END

                            int responseCode = conn.getResponseCode();
                            logger.logDebug("POST Response Code :: " + responseCode);

                            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                    String inputLine;
                                    StringBuffer response = new StringBuffer();

                                    while ((inputLine = in.readLine()) != null) {
                                            response.append(inputLine);
                                    }
                                    in.close();

                                    // print result
                                    logger.logDebug("############### BALIKAN ####################### ");
                                    logger.logDebug("############### RESPONSE  = "+ response.toString());

                                    /*
                                    JSONParser parser = new JSONParser();
                                    JSONObject json = (JSONObject) parser.parse(response.toString());
                                    logger.logDebug("code = "+ json.get("code"));
                                    logger.logDebug("status = "+ json.get("status"));
                                    logger.logDebug("keterangan = "+ json.get("message"));

                                    if(!json.get("code").toString().equalsIgnoreCase("00")){
                                        throw new RuntimeException("Gagal download e-polis dari Asuransi Jiwa ");
                                    }*/


                            } else {
                                    System.out.println("POST request did not work.");
                            }

                        if (conn.getResponseCode() != 200) {
                            throw new RuntimeException("Failed : HTTP Error code : "
                                    + conn.getResponseCode());
                        }

                        conn.disconnect();

                    }catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
    }

    public void downloadPolisJiwaSatuan(HttpServletRequest rq, HttpServletResponse rp)  throws Exception {

      final String nopolis = rq.getParameter("nopolis");
      final String norek = rq.getParameter("norek");
      final String entid = rq.getParameter("entid");

      logger.logDebug("############# no polis jiwa = "+ nopolis);
      logger.logDebug("############# norek = "+ norek);
      logger.logDebug("############# entid = "+ entid);


      String fn = "//webapps.askrida.co.id/fin-repository/POLIS-PAJ/"+nopolis+".pdf";

      //CEK DULU UDAH ADA BELUM FILE POLIS NYA, JIKA SUDAH MAKA AMBIL YG ADA
      File file = new File(fn);

      if(file.exists() && file.canRead()){

          logger.logDebug("############# FILE E-POLIS SUDAH ADA SEBELUMNYA = "+ nopolis);

          enableCache(rp);

          rp.setContentType("application/pdf");
          rp.setHeader("Content-Disposition","attachment; filename="+ nopolis +".pdf;");

          FileInputStream fi=null;
          ServletOutputStream os=null;

          try {
             fi = new FileInputStream(fn);

             os = rp.getOutputStream();

             byte[] buf = new byte [4096];

             while (fi.available()>0) {
                int n = fi.read(buf);
                os.write(buf,0,n);
             }

          } finally {
             if (fi!=null) fi.close();
             //if (os!=null) os.close();
          }
      }else{

          //KALAU TIDAK ADA FILE, REQUEST KE API JIWA
          logger.logDebug("############# FILE E-POLIS BELUM ADA, REQUEST KE API JIWA = "+ nopolis);

          //EntityView ent = getEntity(entid);
          EntityView ent = getEntity(entid);

          //Request polis ke PAJ
          requestPolisToPAJ(norek, nopolis, ent.getCompany().getStVSCode(), ent.getStEntityID());

          //requestPolisMergeToPAJ(ent.getCompany().getStVSCode(), ent.getStEntityID(), policyid, policy);

          enableCache(rp);

          rp.setContentType("application/pdf");
          rp.setHeader("Content-Disposition","attachment; filename="+ nopolis +".pdf;");
          //rp.setContentLength((int) file.getDbOriginalSize().longValue());

          FileInputStream fi=null;
          ServletOutputStream os=null;

          File fileCek = new File(fn);

          if(!fileCek.exists())
              throw new RuntimeException("Gagal dapat polis asuransi Jiwa");

          try {
             fi = new FileInputStream(fn);

             os = rp.getOutputStream();

             byte[] buf = new byte [4096];

             while (fi.available()>0) {
                int n = fi.read(buf);
                os.write(buf,0,n);
             }

          } finally {
             if (fi!=null) fi.close();
             //if (os!=null) os.close();
          }
      }


   }

}
