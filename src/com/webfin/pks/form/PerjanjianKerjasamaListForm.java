/***********************************************************************
 * Module:  com.webfin.insurance.form.PolicyListForm
 * Author:  Denny Mahendra
 * Created: Jan 19, 2006 1:03:25 AM
 * Purpose: 
 ***********************************************************************/
package com.webfin.pks.form;

import com.crux.common.model.Filter;
import com.crux.util.*;
import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;
import com.crux.pool.DTOPool;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.pks.model.PerjanjianKerjasamaView;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Logger;
import org.joda.time.DateTime;

public class PerjanjianKerjasamaListForm extends Form {

    private String policyid;
    private String stPrintForm;
    private String stLang;
    private String stOutstandingFlag = "Y";
    private DTOList list;
    private boolean enableCreateProduction;
    private boolean enableCreateProposal;
    private boolean enableEdit = false;
    private boolean enableView = true;
    private boolean enablePrint = false;
    private String printingLOV = "";
    private String stBranch = SessionManager.getInstance().getSession().getStBranch();
    private Date dtFilterPolicyDateFrom;
    private Date dtFilterPolicyDateTo;
//    private Date dtFilterPolicyExpireFrom;
//    private Date dtFilterPolicyExpireTo;
    private Date dtFilterPolicyExpireFrom = new Date();
    private Date dtFilterPolicyExpireTo = calcDays();
    private HashSet editFilter = new HashSet();
    private boolean canNavigateBranch = hasResource("POL_PROP_NAVBR");
    private boolean canEdit;
    private boolean canCreate;
    private boolean canApprove;
    private boolean listEffective;
    private String stFontSize;
    private String stAttached;
    private String stShowAll;
    private String stPksAwal;
    private String stPolicyGroup;
    private String stPolicyTypeID;
    private final static transient LogManager logger = LogManager.getInstance(PerjanjianKerjasamaListForm.class);

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

    public boolean isEnableEdit() {
        return enableEdit;
    }

    public boolean isEnableView() {
        return enableView;
    }

    public boolean isEnablePrint() {
        return enablePrint;
    }
    private ArrayList statusFilter;

    private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB", InsuranceHome.class.getName())).create();
    }

    public DTOList getList() throws Exception {
        /*DTOList list = ListUtil.getDTOListFromQuery("select * from ins_policy where active_flag = 'Y' order by pol_no",
        InsurancePolicyView.class,filter);

        list.setFilter(filter);*/

        if (list == null) {
            list = new DTOList();
            final Filter filter = new Filter();
            //filter.orderKey="root_id desc, create_date desc";
            list.setFilter(filter.activate());
        }

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.pks_id,a.parent_id,a.active_flag,a.effective_flag,a.status,a.pol_type_id,a.pol_no,a.bank_no,a.cust_name,"
                + "b.description as policy_type_desc,c.description as cc_code,d.vs_description as ref1 ");

        sqa.addQuery(" from perjanjian_kerjasama a "
                + " left join ins_policy_types b on a.pol_type_id = b.pol_type_id "
                + " inner join gl_cost_center c on c.cc_code = a.cc_code "
                + " inner join s_valueset d on d.vs_code = a.ref1 and d.vs_group = 'PERJANJIAN' ");

        sqa.addFilter(list.getFilter());

        sqa.addClause("a.active_flag='Y'");

        if (statusFilter != null) {
            sqa.addClause(SQLUtil.exprIN("status", statusFilter));

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

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (dtFilterPolicyDateFrom != null) {
            sqa.addClause("a.policy_date >= ?");
            sqa.addPar(dtFilterPolicyDateFrom);
        }

        if (dtFilterPolicyDateTo != null) {
            sqa.addClause("a.policy_date <= ?");
            sqa.addPar(dtFilterPolicyDateTo);
        }

        if (dtFilterPolicyExpireFrom != null) {
            sqa.addClause("date_trunc('day',a.period_end) >= ?");
            sqa.addPar(dtFilterPolicyExpireFrom);
        }

        if (dtFilterPolicyExpireTo != null) {
            sqa.addClause("date_trunc('day',a.period_end) <= ?");
            sqa.addPar(dtFilterPolicyExpireTo);
        }

        if (stPolicyGroup != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(stPolicyGroup);
        }

        if (stPolicyTypeID != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(stPolicyTypeID);
        }

        if (stPksAwal != null) {
            sqa.addClause("a.ref2 like ?");
            sqa.addPar("%" + stPksAwal + "%");
        }

        sqa.addOrder("a.root_id desc, a.create_date desc");
        //	javax.swing.JOptionPane.showMessageDialog(null,sqa.getSQL(),"Claim",javax.swing.JOptionPane.CLOSED_OPTION );

        list = sqa.getList(PerjanjianKerjasamaView.class);
        //javax.swing.JOptionPane.showMessageDialog(null,sqa.getSQL(),"Claim",javax.swing.JOptionPane.CLOSED_OPTION );


        return list;
    }

    public void refresh() {
    }

    public void setList(DTOList list) {
        this.list = list;
    }

    public PerjanjianKerjasamaListForm() {
    }

    public void clickCreate() throws Exception {
        final PerjanjianKerjasamaForm form = (PerjanjianKerjasamaForm) newForm("pks_edit", this);

        form.createNew();

        form.show();
    }

    private void validatePolicyID() {
        if (policyid == null) {
            throw new RuntimeException("Data belum dipilih");
        }
    }

    public void clickEdit() throws Exception {
        validatePolicyID();

        final PerjanjianKerjasamaView pol = (PerjanjianKerjasamaView) DTOPool.getInstance().getDTO(PerjanjianKerjasamaView.class, policyid);

        if (pol == null) {
            throw new RuntimeException("Document not found ??");
        }

        String pksForm;
        if (pol.isPKSInduk()) {
            pksForm = "pks_edit";
        } else {
            pksForm = "pks_editadd";
        }

        final PerjanjianKerjasamaForm form = (PerjanjianKerjasamaForm) newForm(pksForm, this);

        form.edit(policyid);

        form.show();
    }

    public void clickView() throws Exception {
        validatePolicyID();

        final PerjanjianKerjasamaView pol = (PerjanjianKerjasamaView) DTOPool.getInstance().getDTO(PerjanjianKerjasamaView.class, policyid);

        if (pol == null) {
            throw new RuntimeException("Document not found ??");
        }

        String pksForm;
        if (pol.isPKSInduk()) {
            pksForm = "pks_edit";
        } else {
            pksForm = "pks_editadd";
        }

        final PerjanjianKerjasamaForm form = (PerjanjianKerjasamaForm) newForm(pksForm, this);

        form.view(policyid);

        form.show();
    }

    public void clickApproval() throws Exception {
        validatePolicyID();

        final PerjanjianKerjasamaView pol = (PerjanjianKerjasamaView) DTOPool.getInstance().getDTO(PerjanjianKerjasamaView.class, policyid);

        if (pol == null) {
            throw new RuntimeException("Document not found ??");
        }

        String pksForm;
        if (pol.isPKSInduk()) {
            pksForm = "pks_edit";
        } else {
            pksForm = "pks_editadd";
        }

        final PerjanjianKerjasamaForm form = (PerjanjianKerjasamaForm) newForm(pksForm, this);

        form.approval(policyid);

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

    public Date calcDays() {

        DateTime startDate = new DateTime(new Date());
        DateTime endDate = new DateTime();

        endDate = startDate.plusMonths(Integer.parseInt("2"));

        return endDate.toDate();

    }

    public String getStShowAll() {
        return stShowAll;
    }

    public void setStShowAll(String stShowAll) {
        this.stShowAll = stShowAll;
    }

    public void refreshAll() {
        if (Tools.isYes(stShowAll)) {
            setDtFilterPolicyExpireFrom(null);
            setDtFilterPolicyExpireTo(null);
            refresh();
        } else {
            setDtFilterPolicyExpireFrom(new Date());
            setDtFilterPolicyExpireTo(calcDays());
            refresh();
        }
    }

    public void clickCreateAdd() throws Exception {
        validatePolicyID();

        final PerjanjianKerjasamaForm form = (PerjanjianKerjasamaForm) newForm("pks_editadd", this);

        form.editCreateAdd(policyid);

        form.show();
    }

    /**
     * @return the policyid
     */
    public String getPolicyid() {
        return policyid;
    }

    /**
     * @param policyid the policyid to set
     */
    public void setPolicyid(String policyid) {
        this.policyid = policyid;
    }

    /**
     * @return the stPksAwal
     */
    public String getStPksAwal() {
        return stPksAwal;
    }

    /**
     * @param stPksAwal the stPksAwal to set
     */
    public void setStPksAwal(String stPksAwal) {
        this.stPksAwal = stPksAwal;
    }

    /**
     * @return the stPolicyGroup
     */
    public String getStPolicyGroup() {
        return stPolicyGroup;
    }

    /**
     * @param stPolicyGroup the stPolicyGroup to set
     */
    public void setStPolicyGroup(String stPolicyGroup) {
        this.stPolicyGroup = stPolicyGroup;
    }

    /**
     * @return the stPolicyTypeID
     */
    public String getStPolicyTypeID() {
        return stPolicyTypeID;
    }

    /**
     * @param stPolicyTypeID the stPolicyTypeID to set
     */
    public void setStPolicyTypeID(String stPolicyTypeID) {
        this.stPolicyTypeID = stPolicyTypeID;
    }

    public void clickClose() throws Exception {
        validatePolicyID();

        final PerjanjianKerjasamaView pol = (PerjanjianKerjasamaView) DTOPool.getInstance().getDTO(PerjanjianKerjasamaView.class, policyid);

        if (pol == null) {
            throw new RuntimeException("Document not found ??");
        }

        String pksForm;
        if (pol.isPKSInduk()) {
            pksForm = "pks_edit";
        } else {
            pksForm = "pks_editadd";
        }

        final PerjanjianKerjasamaForm form = (PerjanjianKerjasamaForm) newForm(pksForm, this);

        form.close(policyid);

        form.show();
    }
}
