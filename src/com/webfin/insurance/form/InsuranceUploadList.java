/***********************************************************************
 * Module:  com.webfin.gl.form.GLJournalList
 * Author:  Denny Mahendra
 * Created: Jan 17, 2006 4:00:42 AM
 * Purpose:
 ***********************************************************************/
package com.webfin.insurance.form;

import com.crux.common.model.Filter;
import com.crux.common.model.HashDTO;
import com.crux.pool.DTOPool;
import com.crux.web.form.Form;
import com.crux.util.*;
import com.crux.web.controller.SessionManager;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.model.JournalView;
import com.webfin.insurance.ejb.Insurance;
import com.webfin.insurance.ejb.InsuranceHome;
import com.webfin.insurance.model.BiayaPemasaranView;
import com.webfin.insurance.model.InsurancePolicyObjDefaultView;
import com.webfin.insurance.model.InsurancePolicyReinsView;
import com.webfin.insurance.model.InsurancePolicyTreatyDetailView;
import com.webfin.insurance.model.InsurancePolicyView;
import com.webfin.insurance.model.uploadEndorsemenView;
import com.webfin.insurance.model.uploadPiutangPremi30View;
import com.webfin.insurance.model.uploadPiutangPremi75View;
import com.webfin.insurance.model.uploadPiutangPremiView;
import com.webfin.insurance.model.uploadProposalCommView;
import com.webfin.insurance.model.uploadReinsuranceSpreadingView;
import java.math.BigDecimal;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.util.Date;
import javax.servlet.ServletOutputStream;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class InsuranceUploadList extends Form {

    private DTOList list;
    private JournalView journal;
    private String memorialID;
    private String description;
    private String branch;
    private DTOList listRI;
    private DTOList listOS;
    private uploadPiutangPremiView piutangPremi;
    private String stNotApproved;

    private Date dtFilterEntryDateFrom;
    private Date dtFilterEntryDateTo;
    private String stNotRealized;


    private DTOList listComm;
    private uploadProposalCommView proposal;
    private boolean canCreate = SessionManager.getInstance().getSession().hasResource("PROP_CREATE");
    private boolean approvalCab = SessionManager.getInstance().getSession().hasResource("PROP_APPROVAL_CAB");
    private boolean approvalSie = SessionManager.getInstance().getSession().hasResource("PROP_APPROVAL_SIE");
    private boolean approvalBag = SessionManager.getInstance().getSession().hasResource("PROP_APPROVAL_BAG");
    private boolean approvalDiv = SessionManager.getInstance().getSession().hasResource("PROP_APPROVAL_DIV");

    private String stShowAll;

    private DTOList listPemasaran;
    private BiayaPemasaranView pemasaran;
    private String period;
    private String year;
    private BigDecimal dbAmount;
    private BigDecimal dbAmountShare;

    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName())).create();
    }

    private final static transient LogManager logger = LogManager.getInstance(InsuranceUploadList.class);

    public DTOList getList() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (list == null) {
            list = new DTOList();
            final Filter filter = new Filter();
            //filter.orderKey="root_id desc, create_date desc";
            list.setFilter(filter.activate());
        }
 
        sqa.addSelect(" ins_upload_id,status,recap_no,count(ins_upload_dtl_id) as data_amount,sum(premi_koas) as premi_koas_total,sum(komisi_koas) as komisi_koas_total,create_who ");

        sqa.addQuery(" from ins_upload_detail");

        sqa.addFilter(list.getFilter());

        sqa.addOrder("ins_upload_id desc");
        
        sqa.addGroup("ins_upload_id,status,recap_no,create_who");

       // sqa.setLimit(300);

        list = sqa.getList(uploadEndorsemenView.class);

        return list;
    }

    public void setList(DTOList list) {
        this.list = list;
    }

    public void clickRefresh() {
    }

    public void btnSearch() throws Exception {
        getList().getFilter().setCurrentPage(0);
    }

   public void clickUploadExcel() throws Exception {
      final InsuranceUploadForm form = (InsuranceUploadForm)newForm("endorse_upload_form", this);

      form.createNewUpload();

      form.show();

   }

   public void clickEdit() throws Exception {
      final InsuranceUploadForm form = (InsuranceUploadForm)newForm("endorse_upload_form", this);

      form.Edit(memorialID);

      form.show();

   }

   public void clickView() throws Exception {
      final InsuranceUploadForm form = (InsuranceUploadForm)newForm("endorse_upload_form", this);

      form.Edit(memorialID);

      form.setReadOnly(true);

      form.show();

   }

    /**
     * @return the memorialID
     */
    public String getMemorialID() {
        return memorialID;
    }

    /**
     * @param memorialID the memorialID to set
     */
    public void setMemorialID(String memorialID) {
        this.memorialID = memorialID;
    }

    /**
     * @return the branch
     */
    public String getBranch() {
        return branch;
    }

    /**
     * @param branch the branch to set
     */
    public void setBranch(String branch) {
        this.branch = branch;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the journal
     */
    public JournalView getJournal() {
        journal = (JournalView) DTOPool.getInstance().getDTO(JournalView.class, memorialID);
        return journal;
    }

    /**
     * @param journal the journal to set
     */
    public void setJournal(JournalView journal) {
        this.journal = journal;
    }

    public void createEndorsemenByNoRecap() throws Exception{
        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;
        listPolicy = ListUtil.getDTOListFromQuery(
                "select pol_no "+
                " from ins_upload_detail "+
                " where coalesce(status,'') <> 'Y' and ins_upload_id = ? "+
                " group by pol_no "+
                " order by pol_no",
                new Object[]{memorialID},
                uploadEndorsemenView.class);

        for (int i = 0; i < listPolicy.size(); i++) {
                uploadEndorsemenView pol2 = (uploadEndorsemenView) listPolicy.get(i);

                PolicyForm form = new PolicyForm();

                DTOList listObject = null;
                listObject = ListUtil.getDTOListFromQuery(
                " SELECT * "+
                " from ins_upload_detail "+
                " where coalesce(status,'') <> 'Y' "+
                " and pol_no = ? "+
                " ORDER BY POL_NO",
                new Object[]{pol2.getStPolicyNo()},
                uploadEndorsemenView.class);

                //CARI ENDORSEMEN TERAKHIR DARI POLIS TSB
                DTOList listEndorsemen = null;
                listEndorsemen = ListUtil.getDTOListFromQuery(
                " SELECT * "+
                " FROM INS_POLICY "+
                " WHERE STATUS IN ('POLICY','RENEWAL','ENDORSE') "+
                " AND SUBSTR(POL_NO,1,16) = ? "+
                " ORDER BY POL_ID DESC LIMIT 1",
                new Object[]{pol2.getStPolicyNo().substring(0, 16)},
                InsurancePolicyView.class);

                InsurancePolicyView polis = (InsurancePolicyView)  listEndorsemen.get(0);

                form.editCreateUploadEndorse(polis.getStPolicyID(), listObject);

                form.btnSaveUpload();

                PreparedStatement PS = S.setQuery("update ins_upload_detail set status = 'Y' where pol_no = ?");

                PS.setObject(1,pol2.getStPolicyNo());

                int j = PS.executeUpdate();

                if (j!=0) logger.logInfo("+++++++ UPDATE STATUS POLIS : "+ pol2.getStPolicyNo() +" ++++++++++++++++++");


        }

        S.release();

    }

    public DTOList getListRI() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (listRI == null) {
            listRI = new DTOList();
            final Filter filter = new Filter();
            //filter.orderKey="root_id desc, create_date desc";
            listRI.setFilter(filter.activate());
        }

        sqa.addSelect(" ins_upload_id,status,recap_no,count(ins_upload_dtl_id) as data_amount,sum(tsi) as tsi_total,sum(premi) as premi_total,create_who ");

        sqa.addQuery(" from (select * from ins_upload_reins  order by ins_upload_id desc limit 120000) x");

        sqa.addFilter(listRI.getFilter());

        sqa.addOrder("ins_upload_id desc");

        sqa.addGroup("ins_upload_id,status,recap_no,create_who");

         sqa.setLimit(40);

        listRI = sqa.getList(uploadReinsuranceSpreadingView.class);

        return listRI;
    }

    /**
     * @param listRI the listRI to set
     */
    public void setListRI(DTOList listRI) {
        this.listRI = listRI;
    }

    public void clickUploadExcelRI() throws Exception {
        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("reas_upload_form", this);

        form.createNewUploadRI();

        form.show();

    }

    public void clickEditRI() throws Exception {
        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("reas_upload_form", this);

        form.EditRI(memorialID);

        form.show();

    }

    public void clickViewRI() throws Exception {
        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("reas_upload_form", this);

        form.EditRI(memorialID);

        form.setReadOnly(true);

        form.show();

    }

    public void createSpreadingReinsByNoRecap() throws Exception {

        if (Tools.isYes(memorialID)) {
            throw new RuntimeException("Data sudah di spreading");
        }

        final SQLUtil S = new SQLUtil();

        DTOList listPolicy = null;

        listPolicy = ListUtil.getDTOListFromQuery(
                " select * "
                + " from ins_policy  "
                + " where status in ('POLICY','RENEWAL','ENDORSE','ENDORSE RI') " //AND effective_flag = 'Y' "
                + " and pol_id in "
                + " ( select ins_pol_id from ins_upload_reins "
                + " where status is null and ins_upload_id = ? "
                + " group by ins_pol_id "
                + " ) order by pol_id",
                new Object[]{memorialID},
                InsurancePolicyView.class);

        for (int i = 0; i < listPolicy.size(); i++) {
            InsurancePolicyView policy = (InsurancePolicyView) listPolicy.get(i);

            final InsurancePolicyView pol = (InsurancePolicyView) DTOPool.getInstance().getDTO(InsurancePolicyView.class, policy.getStPolicyID());

            //dapetin objeknya

            /*
            DTOList listObject = ListUtil.getDTOListFromQuery(
                " select * "+
                "  from ins_upload_reins "+
                "  where status is null and ins_upload_id = ? and pol_no = ?",
                new Object[]{memorialID, pol.getStPolicyNo()},
                uploadReinsuranceSpreadingView.class);
            */

            DTOList objects = pol.getObjects();
            //DTOList objects = listObject;

            for (int j = 0; j < objects.size(); j++) {
                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(j);

                //DTOList objByID = pol.getObjectsByID(objDet.getStPolicyObjectID());

                //InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objByID.get(0);
                if(policy.getStPolicyTypeID().equalsIgnoreCase("41")){
                    //obj.setDbReference1(null);
                    //obj.setDbReference2(null);
                    //obj.setDbReference3(null);
                    //obj.setDbReference4(null);
                }

                DTOList reins = obj.getSpreading(memorialID);

                for (int m = 0; m < reins.size(); m++) {
                    uploadReinsuranceSpreadingView rei = (uploadReinsuranceSpreadingView) reins.get(m);

                    String treatyApply = rei.getStTreaty();

                    logger.logDebug("@@@@@@@@@@@@@@@@@@@@@ proses objek ke : "+rei.getStInsuranceUploadDetailID() + " treaty applied = "+ treatyApply);

                    obj.getTreaties().markAllUpdate();

                    final DTOList treatyDetails = obj.getTreatyDetails();
                    treatyDetails.markAllUpdate();

                    for (int k = 0; k < treatyDetails.size(); k++) {
                        InsurancePolicyTreatyDetailView trdi = (InsurancePolicyTreatyDetailView) treatyDetails.get(k);

                        final DTOList share = trdi.getShares();
                        trdi.getShares().markAllUpdate();

                        if (treatyApply.equalsIgnoreCase("COMM")) {

                            if (trdi.getTreatyDetail().isQS()) {
                                trdi.setDbComissionRate(new BigDecimal(32.5));
                            } else if (trdi.getTreatyDetail().isOR()) {
                                trdi.setDbComissionRate(null);
                                trdi.setDbComission(null);
                            } else if (trdi.getTreatyDetail().isBPDAN()) {
                                trdi.setDbComissionRate(new BigDecimal(30));
                            }

                            for (int l = 0; l < share.size(); l++) {
                                InsurancePolicyReinsView ri = (InsurancePolicyReinsView) share.get(l);

                                if (trdi.getTreatyDetail().isQS()) {
                                    ri.setDbRICommRate(new BigDecimal(32.5));
                                } else if (trdi.getTreatyDetail().isOR()) {
                                    ri.setDbRICommRate(null);
                                    ri.setDbRICommAmount(null);
                                } else if (trdi.getTreatyDetail().isBPDAN()) {
                                    ri.setDbRICommRate(new BigDecimal(30));
                                }
                            }

                        }

                        if (!treatyApply.equalsIgnoreCase("COMM")) {
                            obj.setStInsuranceTreatyID(null);
                            obj.getTreaties().deleteAll();

                            obj.setStInsuranceTreatyID(treatyApply);
                        }
                    }

                    PreparedStatement PS = S.setQuery("update ins_upload_reins set status = 'Y' where ins_upload_dtl_id = ?");

                    PS.setObject(1, rei.getStInsuranceUploadDetailID());

                    int p = PS.executeUpdate();

                    if (p != 0) {
                        logger.logInfo("+++++++ UPDATE STATUS UPLOAD : " + rei.getStInsuranceUploadDetailID() + " ++++++++++++++++++");
                    }

                }
            }

            pol.recalculateSpreading();
            pol.recalculateTreatyUploadSpreading();
            //pol.recalculateTreaty();

            //RESET TREATY
            if (pol.isStatusEndorse()||pol.isStatusEndorseRI()) {
                pol.recalculateTreatyInitialize();
            }

            getRemoteInsurance().saveReinsuranceOnly(pol, pol.getStNextStatus(), false);

        }
    }

    private Insurance getRemoteInsurance() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((InsuranceHome) JNDIUtil.getInstance().lookup("InsuranceEJB", InsuranceHome.class.getName())).create();
    }

    public void clickUploadExcelKalkulator() throws Exception {
        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("kalkulator_premi_form", this);

        form.createNewKalkulator();

        form.show();

    }

    public DTOList getListComm() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (listComm == null) {
            listComm = new DTOList();
            final Filter filter = new Filter();
            //filter.orderKey="root_id desc, create_date desc";
            listComm.setFilter(filter.activate());
        }

//        sqa.addSelect(" ins_upload_id,status1,status2,status3,status4,cc_code,"
//                + "periode_awal,periode_akhir,no_surat_hutang,pol_type_grp_id,pol_type_id,"
//                + "count(ins_upload_dtl_id) as data_amount,sum(amount) as amount_total,create_who ");
//
//        sqa.addQuery(" from ins_proposal_komisi a ");
//
//        if (SessionManager.getInstance().getSession().getStBranch() != null) {
//            sqa.addClause(" cc_code = ? ");
//            sqa.addPar(SessionManager.getInstance().getSession().getStBranch());
//        }
//
//        sqa.addOrder("ins_upload_id desc");
//
//        sqa.addGroup("a.ins_upload_id,a.status1,a.status2,a.status3,a.status4,a.cc_code,"
//                + "a.periode_awal,a.periode_akhir,a.no_surat_hutang,a.pol_type_grp_id,a.pol_type_id,a.create_who");

        // sqa.setLimit(300);

//        String select = " a.*,(select string_agg(b.receipt_no,',')) as reinsurer_note ";
//
//        String sql = "from ( select ins_upload_id,status1,status2,status3,status4,cc_code,"
//                + " periode_awal,periode_akhir,no_surat_hutang,pol_type_grp_id,pol_type_id,"
//                + " count(ins_upload_dtl_id) as data_amount,sum(amount) as amount_total,create_who "
//                + " from ins_proposal_komisi a ";
//
//        if (getStBranch() != null) {
//            sql = sql + " where a.cc_code = '" + getStBranch() + "'";
//        }
//
//        sql = sql + " group by a.ins_upload_id,a.status1,a.status2,a.status3,a.status4,a.cc_code,"
//                + " a.periode_awal,a.periode_akhir,a.no_surat_hutang,a.pol_type_grp_id,a.pol_type_id,a.create_who "
//                + " order by ins_upload_id desc "
//                + ") a left join ( select no_surat_hutang as no_shk,receipt_no from ar_invoice "
//                + "where ar_trx_type_id = 11 and approved_flag = 'Y' ";
//
//        if (getStBranch() != null) {
//            sql = sql + " and cc_code = '" + getStBranch() + "'";
//        }
//
//        sql = sql + "and amount_settled is not null and no_surat_hutang is not null "
//                + "and date_trunc('day',receipt_date) >= '2018-01-01 00:00:00' "
//                + "group by no_surat_hutang,receipt_no order by no_surat_hutang,receipt_no "
//                + ") b on b.no_shk = a.no_surat_hutang ";
//
//        sqa.addSelect(select);
//        sqa.addQuery(sql);
//
//        if (Tools.isYes(stNotApproved)) {
//            sqa.addClause("b.receipt_no is null");
//        }
//
//        sqa.addGroup(" a.ins_upload_id,a.status1,a.status2,a.status3,a.status4,a.cc_code,a.data_amount,"
//                + "a.amount_total,a.periode_awal,a.periode_akhir,a.no_surat_hutang,a.pol_type_grp_id,a.pol_type_id,"
//                + "a.create_who ");
//        sqa.addOrder(" a.ins_upload_id desc");
//
//        sqa.setLimit(100);

        sqa.addSelect("a.ins_upload_id,a.status1,a.status2,a.status3,a.status4,a.cc_code,"
                + "a.periode_awal,a.periode_akhir,a.no_surat_hutang,a.pol_type_grp_id,a.pol_type_id,"
                + "sum(a.data_amount) as data_amount,sum(a.amount_total) as amount_total,a.create_who,"
                + "(select string_agg(a.receipt_no,',')) as reinsurer_note ");

        String sql = "from ( "
                + "select a.ins_upload_id,a.status1,a.status2,a.status3,a.status4,a.cc_code,"
                + "a.periode_awal,a.periode_akhir,a.no_surat_hutang,a.pol_type_grp_id,a.pol_type_id,"
                + "count(a.ins_upload_dtl_id) as data_amount,sum(a.amount) as amount_total,a.create_who,a.receipt_no "
                + "from ( select a.ins_upload_id,a.status1,a.status2,a.status3,a.status4,a.cc_code,"
                + "a.periode_awal,a.periode_akhir,a.no_surat_hutang,a.pol_type_grp_id,a.pol_type_id,"
                + "a.ins_upload_dtl_id,a.amount,a.create_who,b.receipt_no "
                + "from ins_proposal_komisi a "
                + "inner join ar_invoice b on b.ar_invoice_id = a.ins_ar_invoice_id "
                + "where a.no_surat_hutang is not null ";

        if (getStBranch() != null) {
            sql = sql + " and a.cc_code = '" + getStBranch() + "'";

            if (Tools.isYes(getStDataFinal())) {
                sql = sql + "and (a.status1 = 'Y' and a.status2 = 'Y' and a.status3 = 'Y' and a.status4 = 'Y') ";
            }
//            else {
//                sql = sql + "and (coalesce(a.status1,'N') <> 'Y' and coalesce(a.status2,'N') <> 'Y' and coalesce(a.status3,'N') <> 'Y' and coalesce(a.status4,'N') <> 'Y') ";
//            }

            if (dtFilterEntryDateFrom != null || dtFilterEntryDateTo != null) {
                sql = sql + "and date_trunc('day',a.create_date) >= '" + dtFilterEntryDateFrom + "' ";
                sql = sql + "and date_trunc('day',a.create_date) <= '" + dtFilterEntryDateTo + "' ";
            }
            if (dtFilterEntryDateFrom == null || dtFilterEntryDateTo == null) {
                sql = sql + "and date_trunc('day',a.create_date) >= '" + DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-01 00:00:00' ";
                sql = sql + "and date_trunc('day',a.create_date) <= '" + new Date() + "' ";
            }
        }

        if (getStBranch() == null) {
            sql = sql + "and (coalesce(a.status2,'N') <> 'Y' or coalesce(a.status3,'N') <> 'Y' or coalesce(a.status4,'N') <> 'Y') ";

            if (dtFilterEntryDateFrom != null || dtFilterEntryDateTo != null) {
                sql = sql + "and date_trunc('day',a.create_date) >= '" + dtFilterEntryDateFrom + "' ";
                sql = sql + "and date_trunc('day',a.create_date) <= '" + dtFilterEntryDateTo + "' ";
            }
            if (dtFilterEntryDateFrom == null || dtFilterEntryDateTo == null) {
                sql = sql + "and date_trunc('day',a.create_date) >= '" + DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-01 00:00:00' ";
                sql = sql + "and date_trunc('day',a.create_date) <= '" + new Date() + "' ";
            }
        }

        if (Tools.isYes(getStNotRealized())) {
            sql = sql + " and b.receipt_no is null ";
        }

        sql = sql + " order by a.ins_upload_id desc ";

//        if (getStBranch() == null) {
//            sql = sql + "limit 10000 ";
//        }

        sql = sql + " ) a "
                + "group by a.ins_upload_id,a.status1,a.status2,a.status3,a.status4,a.cc_code,a.receipt_no,"
                + "a.periode_awal,a.periode_akhir,a.no_surat_hutang,a.pol_type_grp_id,a.pol_type_id,a.create_who ) a ";

        sqa.addQuery(sql);

        if (Tools.isYes(stNotApproved)) {
            sqa.addClause(" (a.status1 = 'Y' and coalesce(a.status2,'N') <> 'Y' and coalesce(a.status3,'N') <> 'Y' and coalesce(a.status4,'N') <> 'Y') ");
        }

        sqa.addGroup(" a.ins_upload_id,a.status1,a.status2,a.status3,a.status4,a.cc_code,"
                + "a.periode_awal,a.periode_akhir,a.no_surat_hutang,a.pol_type_grp_id,a.pol_type_id,a.create_who ");

        sqa.addOrder(" a.ins_upload_id desc");

        sqa.setLimit(100);

        sqa.addFilter(listComm.getFilter());

        listComm = sqa.getList(uploadProposalCommView.class);

//        listComm = ListUtil.getDTOListFromQuery(
//                sql,
//                sqa.getPar(),
//                uploadProposalCommView.class);

        return listComm;
    }

    /**
     * @param listComm the listComm to set
     */
    public void setListComm(DTOList listComm) {
        this.listComm = listComm;
    }

    public void clickCreateProposalComm() throws Exception {

        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("proposalcomm_form", this);

        form.createProposalComm();

        form.show();

    }

    public void clickEditProposalComm() throws Exception {
        validateDepoID();

        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("proposalcomm_form", this);

        form.editProposalComm(memorialID);

        form.setEditMode(true);

        form.show();

    }

    public void clickViewProposalComm() throws Exception {
        validateDepoID();

        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("proposalcomm_form", this);

        form.viewProposalComm(memorialID);

        //form.setReadOnly(true);

        form.setViewMode(true);

        form.show();

    }

    /**
     * @return the approvalCab
     */
    public boolean isApprovalCab() {
        return approvalCab;
    }

    /**
     * @param approvalCab the approvalCab to set
     */
    public void setApprovalCab(boolean approvalCab) {
        this.approvalCab = approvalCab;
    }

    /**
     * @return the approvalSie
     */
    public boolean isApprovalSie() {
        return approvalSie;
    }

    /**
     * @param approvalSie the approvalSie to set
     */
    public void setApprovalSie(boolean approvalSie) {
        this.approvalSie = approvalSie;
    }

    /**
     * @return the approvalBag
     */
    public boolean isApprovalBag() {
        return approvalBag;
    }

    /**
     * @param approvalBag the approvalBag to set
     */
    public void setApprovalBag(boolean approvalBag) {
        this.approvalBag = approvalBag;
    }

    /**
     * @return the approvalDiv
     */
    public boolean isApprovalDiv() {
        return approvalDiv;
    }

    /**
     * @param approvalDiv the approvalDiv to set
     */
    public void setApprovalDiv(boolean approvalDiv) {
        this.approvalDiv = approvalDiv;
    }

    public void clickApprovalProposalComm1() throws Exception {
        validateDepoID();

        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("proposalcomm_form", this);

        form.approvalProposalComm1(memorialID);

        form.setApprovalCab(true);

        form.show();
    }

    public void clickApprovalProposalComm2() throws Exception {
        validateDepoID();

        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("proposalcomm_form", this);

        form.approvalProposalComm2(memorialID);

        form.setApprovalSie(true);

        form.show();
    }

    public void clickApprovalProposalComm3() throws Exception {
        validateDepoID();

        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("proposalcomm_form", this);

        form.approvalProposalComm3(memorialID);

        form.setApprovalBag(true);

        form.show();
    }

    public void clickApprovalProposalComm4() throws Exception {
        validateDepoID();

        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("proposalcomm_form", this);

        form.approvalProposalComm4(memorialID);

        form.setApprovalDiv(true);

        form.show();
    }

    private void validateDepoID() {
        if (memorialID == null) {
            throw new RuntimeException("Pilih dulu data yang ingin diproses");
        }
    }

    /**
     * @return the canCreate
     */
    public boolean isCanCreate() {
        return canCreate;
    }

    /**
     * @param canCreate the canCreate to set
     */
    public void setCanCreate(boolean canCreate) {
        this.canCreate = canCreate;
    }

    private String stBranch = SessionManager.getInstance().getSession().getStBranch();

    /**
     * @return the stBranch
     */
    public String getStBranch() {
        return stBranch;
    }

    /**
     * @param stBranch the stBranch to set
     */
    public void setStBranch(String stBranch) {
        this.stBranch = stBranch;
    }

    public void refresh() {
    }

     /**
     * @return the listOS
     */
    public DTOList getListOS() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (listOS == null) {
            listOS = new DTOList();
            final Filter filter = new Filter();
            listOS.setFilter(filter.activate());
        }

        String query = "select a.pol_id,a.preminetto from ins_warning_piutang a "
                + "inner join ar_invoice b on b.attr_pol_id = a.pol_id::bigint "
                + "where b.ar_trx_type_id in (5,6,7) and b.amount_settled is not null AND COALESCE(b.CANCEL_FLAG,'') <> 'Y' ";

        if (stBranch != null) {
            query = query + "and a.cc_code = '" + stBranch + "' ";

            if (dtFilterEntryDateFrom != null || dtFilterEntryDateTo != null) {
                query = query + "and date_trunc('day',a.create_date) >= '" + dtFilterEntryDateFrom + "' ";
                query = query + "and date_trunc('day',a.create_date) <= '" + dtFilterEntryDateTo + "' ";
            }
            if (dtFilterEntryDateFrom == null || dtFilterEntryDateTo == null) {
                query = query + "and date_trunc('day',a.create_date) >= '" + DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-01 00:00:00' ";
                query = query + "and date_trunc('day',a.create_date) <= '" + new Date() + "' ";
            }
        }

        if (getStBranch() == null) {

            if (dtFilterEntryDateFrom != null || dtFilterEntryDateTo != null) {
                query = query + "and date_trunc('day',a.create_date) >= '" + dtFilterEntryDateFrom + "' ";
                query = query + "and date_trunc('day',a.create_date) <= '" + dtFilterEntryDateTo + "' ";
            }
            if (dtFilterEntryDateFrom == null || dtFilterEntryDateTo == null) {
                query = query + "and date_trunc('day',a.create_date) >= '" + DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-01 00:00:00' ";
                query = query + "and date_trunc('day',a.create_date) <= '" + new Date() + "' ";
            }
        }

        sqa.addSelect(" a.ins_piutang_id,a.cc_code,date_trunc('day',a.create_date) as create_date,"
                + "a.no_surat_hutang,a.amount_total,sum(a.preminetto) as preminetto,a.data_amount, "
                + "sum(b.preminetto) as settled,count(b.pol_id) as data_settled ");

        sqa.addQuery("from ins_warning_piutang a "
                + "left join ( " + query + " ) b on b.pol_id = a.pol_id ");

        if (stBranch != null) {
            sqa.addClause(" a.cc_code = ? ");
            sqa.addPar(stBranch);
        }

        if (dtFilterEntryDateFrom != null) {
            sqa.addClause("date_trunc('day',a.create_date) >= ?");
            sqa.addPar(dtFilterEntryDateFrom);
        }

        if (dtFilterEntryDateTo != null) {
            sqa.addClause("date_trunc('day',a.create_date) <= ?");
            sqa.addPar(dtFilterEntryDateTo);
        }

        sqa.addGroup(" a.ins_piutang_id,a.cc_code,date_trunc('day',a.create_date),a.no_surat_hutang,a.amount_total,a.data_amount ");
        sqa.addOrder(" a.ins_piutang_id desc");

        sqa.setLimit(100);

        sqa.addFilter(listOS.getFilter());

        listOS = sqa.getList(uploadPiutangPremiView.class);

        return listOS;
    }


    /**
     * @param listOS the listOS to set
     */
    public void setListOS(DTOList listOS) {
        this.listOS = listOS;
    }

    /**
     * @return the piutangPremi
     */
    public uploadPiutangPremiView getPiutangPremi() {
        return piutangPremi;
    }

    /**
     * @param piutangPremi the piutangPremi to set
     */
    public void setPiutangPremi(uploadPiutangPremiView piutangPremi) {
        this.piutangPremi = piutangPremi;
    }

    public void clickEditPiutangPremi() throws Exception {
        validateDepoID();

        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("piutangpremi_form", this);

        form.editPiutangPremi(memorialID);

        form.setEditMode(true);

        form.show();

    }

    public void clickViewPiutangPremi() throws Exception {
        validateDepoID();

        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("piutangpremi_form", this);

        form.viewPiutangPremi(memorialID);

        form.setViewMode(true);

        form.show();

    }



    /**
     * @return the stNotApproved
     */
    public String getStNotApproved() {
        return stNotApproved;
    }

    /**
     * @param stNotApproved the stNotApproved to set
     */
    public void setStNotApproved(String stNotApproved) {
        this.stNotApproved = stNotApproved;
    }


    public void clickReverseProposalCommCab() throws Exception {
        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("proposalcomm_form", this);

        form.reverseProposalCommCab(memorialID);

        form.setReadOnly(true);

        form.setReverseMode(true);

        form.show();

    }

    public void clickReverseProposalCommSie() throws Exception {
        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("proposalcomm_form", this);

        form.reverseProposalCommSie(memorialID);

        form.setReadOnly(true);

        form.setReverseMode(true);

        form.show();

    }

    /**
     * @return the dtFilterEntryDateFrom
     */
    public Date getDtFilterEntryDateFrom() {
        return dtFilterEntryDateFrom;
    }

    /**
     * @param dtFilterEntryDateFrom the dtFilterEntryDateFrom to set
     */
    public void setDtFilterEntryDateFrom(Date dtFilterEntryDateFrom) {
        this.dtFilterEntryDateFrom = dtFilterEntryDateFrom;
    }

    /**
     * @return the dtFilterEntryDateTo
     */
    public Date getDtFilterEntryDateTo() {
        return dtFilterEntryDateTo;
    }

    /**
     * @param dtFilterEntryDateTo the dtFilterEntryDateTo to set
     */
    public void setDtFilterEntryDateTo(Date dtFilterEntryDateTo) {
        this.dtFilterEntryDateTo = dtFilterEntryDateTo;
    }

    public void clickPrintExcel() throws Exception {

        final DTOList l = EXCEL_WARNING();

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        EXPORT_WARNING();
    }

    public DTOList EXCEL_WARNING() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

               sqa.addSelect("a.ins_piutang_id,a.cc_code,a.policy_date,a.pol_id,a.pol_no,a.tertanggung,a.amount,a.preminetto,"
                + "(select string_agg(b.receipt_no,'|')) as ket, a.amount_total,a.data_amount,a.no_surat_hutang ");

        sqa.addQuery(" from ins_warning_piutang a "
                + "inner join ar_invoice b on b.attr_pol_id = a.pol_id::bigint and b.ar_trx_type_id in (5,6,7) ");

        if (stBranch != null) {
            sqa.addClause(" a.cc_code = ? ");
            sqa.addPar(stBranch);
        }

        if(dtFilterEntryDateFrom!=null){
            sqa.addClause("date_trunc('day',a.create_date) >= ?");
            sqa.addPar(dtFilterEntryDateFrom);
        }

        if(dtFilterEntryDateTo!=null){
            sqa.addClause("date_trunc('day',a.create_date) <= ?");
            sqa.addPar(dtFilterEntryDateTo);
        }

        String sql = sqa.getSQL() + " group by a.ins_piutang_id,a.cc_code,a.policy_date,a.pol_id,"
                + "a.pol_no,a.tertanggung,a.amount,a.preminetto,a.amount_total,a.data_amount,a.no_surat_hutang,a.ins_piutang_dtl_id "
                + "order by a.no_surat_hutang,a.ins_piutang_dtl_id ";


        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                HashDTO.class);

        SessionManager.getInstance().getRequest().setAttribute("RPT", l);

        return l;

        /*
        SQLUtil S = new SQLUtil();

        String nama_file = "rincian_titipan_" + System.currentTimeMillis() + ".csv";

        sql2 = "Copy ("
        + sql2
        + " ) To 'D://exportdb/csv/" + nama_file + "' With CSV HEADER;";

        final PreparedStatement ps = S.setQuery(sql2);

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
         */
    }

    public void EXPORT_WARNING() throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();

        //bikin sheet
        XSSFSheet sheet = wb.createSheet("new sheet");

        final DTOList list = (DTOList) SessionManager.getInstance().getRequest().getAttribute("RPT");

        for (int i = 0; i < list.size(); i++) {
            HashDTO h = (HashDTO) list.get(i);

            //bikin header
            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("no polis");
            row0.createCell(1).setCellValue("tertanggung");
            row0.createCell(2).setCellValue("tanggal polis");
            row0.createCell(3).setCellValue("premi bruto");
            row0.createCell(4).setCellValue("premi netto");
            row0.createCell(5).setCellValue("nobuk");
            row0.createCell(6).setCellValue("no.konfirmasi");

            XSSFRow row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
            row.createCell(1).setCellValue(h.getFieldValueByFieldNameST("tertanggung"));
            row.createCell(2).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
            row.createCell(3).setCellValue(h.getFieldValueByFieldNameBD("amount").doubleValue());
            row.createCell(4).setCellValue(h.getFieldValueByFieldNameBD("preminetto").doubleValue());
            row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("ket"));
            row.createCell(6).setCellValue(h.getFieldValueByFieldNameST("no_surat_hutang"));

        }

        SessionManager.getInstance().getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        SessionManager.getInstance().getResponse().setHeader("Content-Disposition", "attachment; filename=warningpremi" + "_" + System.currentTimeMillis() + ".xlsx;");
        SessionManager.getInstance().getResponse().setHeader("Pragma", "token");
        ServletOutputStream sosStream = SessionManager.getInstance().getResponse().getOutputStream();

        wb.write(sosStream);
        sosStream.flush();
        sosStream.close();
    }

    /**
     * @return the stNotRealized
     */
    public String getStNotRealized() {
        return stNotRealized;
    }

    /**
     * @param stNotRealized the stNotRealized to set
     */
    public void setStNotRealized(String stNotRealized) {
        this.stNotRealized = stNotRealized;
    }

    /**
     * @return the stShowAll
     */
    public String getStShowAll() {
        return stShowAll;
    }

    /**
     * @param stShowAll the stShowAll to set
     */
    public void setStShowAll(String stShowAll) {
        this.stShowAll = stShowAll;
    }

    /**
     * @return the proposal
     */
    public uploadProposalCommView getProposal() {
        return proposal;
    }

    /**
     * @param proposal the proposal to set
     */
    public void setProposal(uploadProposalCommView proposal) {
        this.proposal = proposal;
    }

   /**
     * @return the listPemasaran
     */
     public DTOList getListPemasaran() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        if (listPemasaran == null) {
            listPemasaran = new DTOList();
            final Filter filter = new Filter();
            //filter.orderKey="root_id desc, create_date desc";
            listPemasaran.setFilter(filter.activate());
        }

        sqa.addSelect(" a.*,b.vs_description as transaksi ");

        sqa.addQuery(" from biaya_pemasaran a "
                + "left join s_valueset b on b.vs_code = a.kd_input::text and b.vs_group = 'TRANSAKSI' ");

        if (getStBranch() != null) {
            sqa.addClause(" a.cc_code = ? ");
            sqa.addPar(stBranch);
        }

        if (getTransaksi() != null) {
            sqa.addClause(" a.kd_input = ? ");
            sqa.addPar(transaksi);
        }

        if (dtFilterEntryDateFrom != null) {
            sqa.addClause("date_trunc('day',a.create_date) >= ?");
            sqa.addPar(dtFilterEntryDateFrom);
        }

        if (dtFilterEntryDateTo != null) {
            sqa.addClause("date_trunc('day',a.create_date) <= ?");
            sqa.addPar(dtFilterEntryDateTo);
        }

        if (Tools.isYes(stNotApproved) && Tools.isNo(stNotRealized)) {
            sqa.addClause(" (coalesce(a.status2,'Y') <> 'Y' or coalesce(a.status3,'Y') <> 'Y' or coalesce(a.status4,'N') <> 'Y') ");
        }

        if (Tools.isYes(stNotRealized) && Tools.isNo(stNotApproved)) {
            sqa.addClause(" a.no_bukti_bayar is null and a.status1 = 'Y' and a.status2 = 'Y' and a.status3 = 'Y' and a.status4 = 'Y' ");
        }

        sqa.addOrder(" pms_id desc ");

        sqa.setLimit(100);

        sqa.addFilter(listPemasaran.getFilter());

        listPemasaran = sqa.getList(BiayaPemasaranView.class);

//        listComm = ListUtil.getDTOListFromQuery(
//                sql,
//                sqa.getPar(),
//                uploadProposalCommView.class);

        return listPemasaran;
    }


    /**
     * @param listPemasaran the listPemasaran to set
     */
    public void setListPemasaran(DTOList listPemasaran) {
        this.listPemasaran = listPemasaran;
    }

    /**
     * @return the pemasaran
     */
    public BiayaPemasaranView getPemasaran() {
        return pemasaran;
    }

    /**
     * @param pemasaran the pemasaran to set
     */
    public void setPemasaran(BiayaPemasaranView pemasaran) {
        this.pemasaran = pemasaran;
    }

    public void clickCreatePemasaran() throws Exception {

        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("pemasaran_form", this);

        form.createBiayaPemasaran();

        form.show();

    }

    public void clickEditPemasaran() throws Exception {
        validateDepoID();

        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("pemasaran_form", this);

        form.editPemasaran(memorialID);

        form.setEditMode(true);

        form.show();

    }

    public void clickViewPemasaran() throws Exception {
        validateDepoID();

        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("pemasaran_form", this);

        form.viewPemasaran(memorialID);

        //form.setReadOnly(true);

        form.setViewMode(true);

        form.show();

    }

    /**
     * @return the period
     */
    public String getPeriod() {
        return period;
    }

    /**
     * @param period the period to set
     */
    public void setPeriod(String period) {
        this.period = period;
    }

    /**
     * @return the year
     */
    public String getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * @return the dbAmount
     */
    public BigDecimal getDbAmount() {
        return dbAmount;
    }

    /**
     * @param dbAmount the dbAmount to set
     */
    public void setDbAmount(BigDecimal dbAmount) {
        this.dbAmount = dbAmount;
    }

    /**
     * @return the dbAmountShare
     */
    public BigDecimal getDbAmountShare() {
        return dbAmountShare;
    }

    /**
     * @param dbAmountShare the dbAmountShare to set
     */
    public void setDbAmountShare(BigDecimal dbAmountShare) {
        this.dbAmountShare = dbAmountShare;
    }

    public void onChangeAmount() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.cc_code,sum(getpremiend(d.entity_id,coalesce(a.premi_total*a.ccy_rate,0),d.premi_amt*a.ccy_rate*-1)) as premi, "
                + "sum(getpremiend(d.entity_id,coalesce(a.nd_disc1*a.ccy_rate,0),((coalesce(d.disc_amount,0))*a.ccy_rate)*-1)) as diskon ");

        sqa.addQuery(" from ins_policies a "
                + "inner join ins_pol_coins d on d.policy_id = a.pol_id ");

        sqa.addClause("a.status in ('POLICY','ENDORSE','RENEWAL')");
        sqa.addClause("a.active_flag = 'Y'");
        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause("(d.entity_id <> 1 or d.coins_type <> 'COINS_COVER')");
        sqa.addClause("a.pol_type_id in (21,59)");

        if (stBranch != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(stBranch);
        }

        if (period != null) {
            sqa.addClause("substr(a.policy_date::text,6,2) = ?");
            sqa.addPar(period);
        }

        if (year != null) {
            sqa.addClause("substr(a.policy_date::text,1,4) = ?");
            sqa.addPar(year);
        }

        String sql = "select a.cc_code,(a.premi-a.diskon) as biaya "
                + "from ( " + sqa.getSQL() + " group by a.cc_code  ) a ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                BiayaPemasaranView.class);

        BiayaPemasaranView close = (BiayaPemasaranView) l.get(0);

        setDbAmount(close.getDbBiaya());
        setDbAmountShare(BDUtil.mul(getDbAmount(), new BigDecimal(0.01)));
    }

    public void clickApprovalPms1() throws Exception {
        validateDepoID();

        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("pemasaran_form", this);

        form.approvalPms1(memorialID);

        form.setApprovalCab(true);

        form.show();
    }

    public void clickApprovalPms2() throws Exception {
        validateDepoID();

        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("pemasaran_form", this);

        form.approvalPms2(memorialID);

        form.setApprovalSie(true);

        form.show();
    }

    public void clickApprovalPms3() throws Exception {
        validateDepoID();

        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("pemasaran_form", this);

        form.approvalPms3(memorialID);

        form.setApprovalBag(true);

        form.show();
    }

    public void clickApprovalPms4() throws Exception {
        validateDepoID();

        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("pemasaran_form", this);

        form.approvalPms4(memorialID);

        form.setApprovalDiv(true);

        form.show();
    }

    public void clickReversePmsCab() throws Exception {
        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("pemasaran_form", this);

        form.reversePmsCab(memorialID);

        form.setReadOnly(true);

        form.setReverseMode(true);

        form.show();

    }

    public void clickReversePmsSie() throws Exception {
        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("pemasaran_form", this);

        form.reversePmsSie(memorialID);

        form.setReadOnly(true);

        form.setReverseMode(true);

        form.show();

    }

    public void clickReceiptPemasaran() throws Exception {
        validateDepoID();

        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("pemasaran_bayar_form", this);

        form.receiptPemasaran(memorialID);

        form.setReceiptMode(true);

        form.show();
    }

    private String transaksi;

    /**
     * @return the transaksi
     */
    public String getTransaksi() {
        return transaksi;
    }

    /**
     * @param transaksi the transaksi to set
     */
    public void setTransaksi(String transaksi) {
        this.transaksi = transaksi;
    }

    private DTOList listOS30;
    private DTOList listOS75;

    public DTOList getListOS30() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (listOS30 == null) {
            listOS30 = new DTOList();
            final Filter filter = new Filter();
            listOS30.setFilter(filter.activate());
        }

        String query = "select distinct a.pol_id,a.preminetto from ins_warning_piutang30 a "
                + "inner join ar_invoice b on b.attr_pol_id = a.pol_id::bigint "
                + "where b.ar_trx_type_id in (5,6,7) and b.amount_settled is not null ";

        if (stBranch != null) {
            query = query + "and a.cc_code = '" + stBranch + "' ";

            if (dtFilterEntryDateFrom != null || dtFilterEntryDateTo != null) {
                query = query + "and date_trunc('day',a.create_date) >= '" + dtFilterEntryDateFrom + "' ";
                query = query + "and date_trunc('day',a.create_date) <= '" + dtFilterEntryDateTo + "' ";
            }
            if (dtFilterEntryDateFrom == null || dtFilterEntryDateTo == null) {
                query = query + "and date_trunc('day',a.create_date) >= '" + DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-01 00:00:00' ";
                query = query + "and date_trunc('day',a.create_date) <= '" + new Date() + "' ";
            }
        }

        if (getStBranch() == null) {

            if (dtFilterEntryDateFrom != null || dtFilterEntryDateTo != null) {
                query = query + "and date_trunc('day',a.create_date) >= '" + dtFilterEntryDateFrom + "' ";
                query = query + "and date_trunc('day',a.create_date) <= '" + dtFilterEntryDateTo + "' ";
            }
            if (dtFilterEntryDateFrom == null || dtFilterEntryDateTo == null) {
                query = query + "and date_trunc('day',a.create_date) >= '" + DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-01 00:00:00' ";
                query = query + "and date_trunc('day',a.create_date) <= '" + new Date() + "' ";
            }
        }

        sqa.addSelect(" a.ins_piutang_id,a.cc_code,date_trunc('day',a.create_date) as create_date,"
                + "a.no_surat_hutang,a.amount_total,sum(a.preminetto) as preminetto,a.data_amount, "
                + "sum(b.preminetto) as settled,count(b.pol_id) as data_settled ");

        sqa.addQuery("from ins_warning_piutang30 a "
                + "left join ( " + query + " ) b on b.pol_id = a.pol_id ");

        if (stBranch != null) {
            sqa.addClause(" a.cc_code = ? ");
            sqa.addPar(stBranch);
        }

        if (dtFilterEntryDateFrom != null) {
            sqa.addClause("date_trunc('day',a.create_date) >= ?");
            sqa.addPar(dtFilterEntryDateFrom);
        }

        if (dtFilterEntryDateTo != null) {
            sqa.addClause("date_trunc('day',a.create_date) <= ?");
            sqa.addPar(dtFilterEntryDateTo);
        }

        sqa.addGroup(" a.ins_piutang_id,a.cc_code,date_trunc('day',a.create_date),a.no_surat_hutang,a.amount_total,a.data_amount ");
        sqa.addOrder(" a.ins_piutang_id desc");

        sqa.setLimit(100);

        sqa.addFilter(listOS30.getFilter());

        listOS30 = sqa.getList(uploadPiutangPremi30View.class);

        return listOS30;
    }

    /**
     * @param listOS the listOS to set
     */
    public void setListOS30(DTOList listOS30) {
        this.listOS30 = listOS30;
    }

    public DTOList getListOS75() throws Exception {
        final SQLAssembler sqa = new SQLAssembler();

        if (listOS75 == null) {
            listOS75 = new DTOList();
            final Filter filter = new Filter();
            listOS75.setFilter(filter.activate());
        }

        String query = "select distinct a.pol_id,a.preminetto from ins_warning_piutang75 a "
                + "inner join ar_invoice b on b.attr_pol_id = a.pol_id::bigint "
                + "where b.ar_trx_type_id in (5,6,7) and b.amount_settled is not null ";

        if (stBranch != null) {
            query = query + "and a.cc_code = '" + stBranch + "' ";

            if (dtFilterEntryDateFrom != null || dtFilterEntryDateTo != null) {
                query = query + "and date_trunc('day',a.create_date) >= '" + dtFilterEntryDateFrom + "' ";
                query = query + "and date_trunc('day',a.create_date) <= '" + dtFilterEntryDateTo + "' ";
            }
            if (dtFilterEntryDateFrom == null || dtFilterEntryDateTo == null) {
                query = query + "and date_trunc('day',a.create_date) >= '" + DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-01 00:00:00' ";
                query = query + "and date_trunc('day',a.create_date) <= '" + new Date() + "' ";
            }
        }

        if (getStBranch() == null) {

            if (dtFilterEntryDateFrom != null || dtFilterEntryDateTo != null) {
                query = query + "and date_trunc('day',a.create_date) >= '" + dtFilterEntryDateFrom + "' ";
                query = query + "and date_trunc('day',a.create_date) <= '" + dtFilterEntryDateTo + "' ";
            }
            if (dtFilterEntryDateFrom == null || dtFilterEntryDateTo == null) {
                query = query + "and date_trunc('day',a.create_date) >= '" + DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-01 00:00:00' ";
                query = query + "and date_trunc('day',a.create_date) <= '" + new Date() + "' ";
            }
        }

        sqa.addSelect(" a.ins_piutang_id,a.cc_code,date_trunc('day',a.create_date) as create_date,"
                + "a.no_surat_hutang,a.amount_total,sum(a.preminetto) as preminetto,a.data_amount, "
                + "sum(b.preminetto) as settled,count(b.pol_id) as data_settled ");

        sqa.addQuery("from ins_warning_piutang75 a "
                + "left join ( " + query + " ) b on b.pol_id = a.pol_id ");

        if (stBranch != null) {
            sqa.addClause(" a.cc_code = ? ");
            sqa.addPar(stBranch);
        }

        if (dtFilterEntryDateFrom != null) {
            sqa.addClause("date_trunc('day',a.create_date) >= ?");
            sqa.addPar(dtFilterEntryDateFrom);
        }

        if (dtFilterEntryDateTo != null) {
            sqa.addClause("date_trunc('day',a.create_date) <= ?");
            sqa.addPar(dtFilterEntryDateTo);
        }

        sqa.addGroup(" a.ins_piutang_id,a.cc_code,date_trunc('day',a.create_date),a.no_surat_hutang,a.amount_total,a.data_amount ");
        sqa.addOrder(" a.ins_piutang_id desc");

        sqa.setLimit(100);

        sqa.addFilter(listOS75.getFilter());

        listOS75 = sqa.getList(uploadPiutangPremi75View.class);

        return listOS75;
    }

    /**
     * @param listOS the listOS to set
     */
    public void setListOS75(DTOList listOS75) {
        this.listOS75 = listOS75;
    }

    private String stDataFinal;

    /**
     * @return the stDataFinal
     */
    public String getStDataFinal() {
        return stDataFinal;
    }

    /**
     * @param stDataFinal the stDataFinal to set
     */
    public void setStDataFinal(String stDataFinal) {
        this.stDataFinal = stDataFinal;
    }

    public void clickPrintReload() throws Exception {
        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("proposalcomm_form", this);

        form.viewProposalComm(memorialID);

        form.setViewMode(true);

        form.show();
    }

    public void clickUploadExcelRIManual() throws Exception {
        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("ri_upload_manual_form", this);

        form.createUploadRIManual();

        form.show();

    }

    public void clickCreateRenewal() throws Exception {
        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("policy_renewal", this);

        form.createPrintRenewal();

        form.show();
    }

    public void clickPolicyFop() throws Exception {
        final InsuranceUploadForm form = (InsuranceUploadForm) newForm("policy_fop", this);

        form.createPolicyFop();

        form.show();
    }
    
}
