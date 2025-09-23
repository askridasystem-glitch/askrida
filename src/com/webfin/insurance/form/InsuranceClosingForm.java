/***********************************************************************
 * Module:  com.webfin.gl.currencys.forms.CurrencyForm
 * Author:  Denny Mahendra
 * Created: Apr 19, 2007 8:37:50 PM
 * Purpose:
 ***********************************************************************/
package com.webfin.insurance.form;

import com.crux.common.parameter.Parameter;
import com.crux.lang.LanguageManager;
import com.crux.web.form.Form;
import com.crux.pool.DTOPool;
import com.crux.util.BDUtil;
import com.crux.util.ConnectionCache;
import com.crux.util.DTOList;
import com.crux.util.DateUtil;
import com.crux.util.JNDIUtil;
import com.crux.util.JSPUtil;
import com.crux.util.ListUtil;
import com.crux.util.MailUtil2;
import com.crux.util.NumberSpell;
import com.crux.util.SQLAssembler;
import com.crux.util.SQLUtil;
import com.crux.util.Tools;
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
import com.webfin.gl.ejb.*;
import com.webfin.insurance.model.InsuranceClosingReportView;
import com.webfin.insurance.model.InsuranceClosingView;
import com.webfin.insurance.model.InsurancePolicySOAView;
import com.webfin.insurance.model.InsurancePostingView;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class InsuranceClosingForm extends Form {

    private InsuranceClosingView closing;
    private boolean noValidation = false;
    private boolean reverseMode = false;
    private boolean editMode = false;
    private InsurancePostingView posting;
    private boolean reOpenMode = false;
    private boolean finalMode = false;
    private InsurancePolicySOAView soa;
    private InsuranceClosingView closingTask;

    class ProsesClosingReinsurance implements Callable<Integer> {

        private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
            return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName())).create();
        }

      public Integer call () throws Exception {
        // do something that takes really long...
         System.out.println("####################### coba callable ");

        //bikin auto close keuangan juga
        if(closingTask.isReinsuranceFlag()){
            if(closingTask.getDtPolicyDateEnd()!=null){

                if(closing.getDtInvoiceDate()==null){
                    closingTask.setDtInvoiceDate(closingTask.getDtPolicyDateEnd());
                    closingTask.setStFinanceClosingStatus("Y");
                }else{
                    closing.setDtInvoiceDate(closing.getDtInvoiceDate());
                    closing.setStFinanceClosingStatus("Y");
                }
            }

            getRemoteGeneralLedger().saveJournalClosing(closingTask);
        }

        return 1;
      }
    }

    public InsurancePolicySOAView getSoa() {
        return soa;
    }

    public void setSoa(InsurancePolicySOAView soa) {
        this.soa = soa;
    }

    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName())).create();
    }

    public void createNew() {
        setPosting(new InsuranceClosingView());

        getClosing().markNew();

        //getPosting().setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());

        setTitle("CLOSING REASURANSI");

    }

    public void edit() {
        final String glpostingid = (String) getAttribute("glpostingid");

        closing = (InsuranceClosingView) DTOPool.getInstance().getDTO(InsuranceClosingView.class, glpostingid);

        if (Tools.isYes(closing.getStFinanceClosingStatus())) {
            throw new RuntimeException("Data sudah di closing");
        }

        closing.markUpdate();

        setTitle("UBAH CLOSING");
    }

    public void view() {
        final String glpostingid = (String) getAttribute("glpostingid");

        closing = (InsuranceClosingView) DTOPool.getInstance().getDTO(InsuranceClosingView.class, glpostingid);

        setReadOnly(true);

        setTitle("LIHAT CLOSING");

    }

    public void save() throws Exception {

        if(closing.isReinsuranceFlag()){
            if(closing.getDtPolicyDateEnd()!=null){
                if(closing.getDtInvoiceDate()==null){
                    closing.setDtInvoiceDate(closing.getDtPolicyDateEnd());
                    closing.setStFinanceClosingStatus("Y");
                }else{
                    closing.setDtInvoiceDate(closing.getDtInvoiceDate());
                    closing.setStFinanceClosingStatus("Y");
                }
                
            }
        }

        closingTask = closing;

        closing.validate();
        
        getRemoteGeneralLedger().saveClosing(closing);

        close();

        //POSTING JURNAL ASYNCRONOUSLY
        ExecutorService es = Executors.newSingleThreadExecutor ();
        Future<Integer> task = es.submit(new ProsesClosingReinsurance());

        //task.get();

    }

    public void close() {
        super.close();
    }

    public InsuranceClosingView getClosing() {
        return closing;
    }

    public void setPosting(InsuranceClosingView closing) {
        this.closing = closing;
    }

    public boolean isPosted() throws Exception {

        SQLUtil S = new SQLUtil();

        boolean isPosted = false;

        try {
            String cek = "select gl_post_id from gl_posting where months = ? and years = ? ";


            PreparedStatement PS = S.setQuery(cek);

            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                isPosted = true;
            }

        } finally {
            S.release();
        }


        return isPosted;
    }

    public void openPosting() {
        final String glpostingid = (String) getAttribute("glpostingid");

        closing = (InsuranceClosingView) DTOPool.getInstance().getDTO(InsuranceClosingView.class, glpostingid);

        //posting.setStPostedFlag("N");
        closing.markUpdate();

        noValidation = true;

        setTitle("BUKA CLOSING");
    }

    public void onChangePolicyTypeGroup() {
    }

    public void retrieveDataTotalold() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.pol_no,a.pol_id,f.ins_treaty_detail_id,ins_treaty_shares_id,e.member_ent_id, sum(round(e.tsi_amount,2) * ccy_rate) as tsi_amount, sum(round(e.tsi_amount_e,2) * ccy_rate) as tsi_amount_e, "
                + " sum(round(e.premi_amount,2) * ccy_rate) as premi_amount, sum(round(e.premi_amount_e,2) * ccy_rate) as premi_amount_e, sum(round(e.ricomm_amt,2) * ccy_rate) as ricomm_amt, sum(round(e.ricomm_amt_e,2) * ccy_rate) ricomm_amt_e, e.ri_slip_no ");

        sqa.addQuery(" from ins_policy a "
                + " inner join ins_pol_obj b on a.pol_id = b.pol_id "
                + "  inner join ins_pol_treaty c on b.ins_pol_obj_id = c.ins_pol_obj_id  "
                + "  inner join ins_pol_treaty_detail d on c.ins_pol_treaty_id = d.ins_pol_treaty_id  "
                + "  inner join ins_treaty_detail f on f.ins_treaty_detail_id = d.ins_treaty_detail_id  "
                + "  inner join ins_pol_ri e on d.ins_pol_tre_det_id = e.ins_pol_tre_det_id");

        sqa.addClause("a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause("(coalesce(e.premi_amount,0) <> 0 or coalesce(e.ricomm_amt,0) <> 0) ");
        sqa.addClause(" f.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5') and a.active_flag='Y'");

        if (getClosing().getDtPolicyDateStart() != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(getClosing().getDtPolicyDateStart());
        }

        if (getClosing().getDtPolicyDateEnd() != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(getClosing().getDtPolicyDateEnd());
        }

        if (getClosing().getDtPeriodStartStart() != null) {
            sqa.addClause("date_trunc('day',a.period_start) >= ?");
            sqa.addPar(getClosing().getDtPeriodStartStart());
        }

        if (getClosing().getDtPeriodStartEnd() != null) {
            sqa.addClause("date_trunc('day',a.period_start) <= ?");
            sqa.addPar(getClosing().getDtPeriodStartEnd());
        }

        /*
        if(getClosing().getStTreatyType()!=null){
        sqa.addClause("f.treaty_type = ?");
        sqa.addPar(getClosing().getStTreatyType());
        }*/

        if (getClosing().getStTreatyType() != null) {
            if (getClosing().getStTreatyType().equalsIgnoreCase("SPL") || getClosing().getStTreatyType().equalsIgnoreCase("QS")) {
                sqa.addClause("f.treaty_type in ('SPL','QS')");
            } else {
                sqa.addClause("f.treaty_type = ?");
                sqa.addPar(getClosing().getStTreatyType());
            }
        }

        if (getClosing().getStPolicyTypeID() != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(getClosing().getStPolicyTypeID());
        }

        if (getClosing().getStPolicyTypeGroupID() != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(getClosing().getStPolicyTypeGroupID());
        }

        sqa.addGroup("a.pol_no,a.pol_id,e.ins_treaty_shares_id,f.ins_treaty_detail_id,e.member_ent_id,e.ri_slip_no");
        //sqa.addOrder("pol_id,ins_treaty_detail_id");

        final String sql = "select sum(tsi_amount) as tsi_total,sum(premi_amount) as premi_reins_total, sum(ricomm_amt) as comm_reins_total, count(pol_id) as data_amount"
                + "   from "
                + "(" + sqa.getSQL() + " order by pol_id,ins_treaty_detail_id ) x ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsuranceClosingView.class);

        InsuranceClosingView close = (InsuranceClosingView) l.get(0);

        getClosing().setDbTSITotal(close.getDbTSITotal());
        getClosing().setDbPremiReinsuranceTotal(close.getDbPremiReinsuranceTotal());
        getClosing().setDbComissionReinsuranceTotal(close.getDbComissionReinsuranceTotal());
        getClosing().setStDataAmount(close.getStDataAmount());


    }

    public void retrieveDataTotal() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        String sqlSelect = "a.pol_no,a.pol_id,j.ins_treaty_detail_id,i.ins_treaty_shares_id,i.member_ent_id,k.ent_name as member,i.ri_slip_no,";

        String amountSelect = " sum(round(i.tsi_amount,2)*a.ccy_rate) as tsi_amount, sum(round(i.tsi_amount_e,2)*a.ccy_rate) as tsi_amount_e,"
                + " sum(round(i.premi_amount,2)*a.ccy_rate) as premi_amount,sum(round(i.premi_amount_e,2)*a.ccy_rate) as premi_amount_e,"
                + " sum(round(i.ricomm_amt,2)*a.ccy_rate) as ricomm_amt,sum(round(i.ricomm_amt_e,2)*a.ccy_rate) as ricomm_amt_e,"
                + " installment_f,l.installment_no,sum(round(l.premi_amount,2)) as premi_inst,sum(round(l.ricomm_amt,2)) as ricomm_inst,l.inst_date,i.binding_date, coalesce(i.installment_count,1) as  installment_count ";

        /*
        if (getClosing().getStTreatyType() != null) {
        if (getClosing().getStTreatyType().equalsIgnoreCase("BPDAN"))
        amountSelect = " sum(i.tsi_amount) as tsi_amount, sum(round(i.tsi_amount_e,2)*a.ccy_rate) as tsi_amount_e,"+
        " sum(round(i.premi_amount_e,2)*a.ccy_rate) as premi_amount_e,"+
        " sum(round(i.ricomm_amt_e,2)*a.ccy_rate) as ricomm_amt_e,"+
        " sum(i.premi_amount) as premi_amount,sum(i.ricomm_amt) as ricomm_amt";
        }*/

        sqlSelect = sqlSelect + amountSelect;

        sqa.addSelect(sqlSelect);

        String clauseObj = "";
        if (getClosing().getDtPolicyDateStart() != null) {
            clauseObj = " and c.ins_pol_obj_id > 9999 and a.policy_date >= '" + getClosing().getDtPolicyDateStart() + "' ";
        }

        sqa.addQuery(" from ins_policy a "
                + "  inner join ins_pol_obj c on c.pol_id = a.pol_id " + clauseObj
                + "    inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + "    inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + "    inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "    inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "    inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "    inner join ent_master k on k.ent_id = i.member_ent_id  "
                + "    left join ins_pol_ri_installment_test l on i.ins_pol_ri_id = l.ins_pol_ri_id  ");

        if (getClosing().isClosingRIOutward()) {
            sqa.addClause("a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
        } else if (getClosing().isClosingRIInwardToOutward()) {
            sqa.addClause("a.status = 'INWARD'");
        }

        sqa.addClause("a.active_flag = 'Y'");
        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause("(coalesce(i.premi_amount,0) <> 0 or coalesce(i.ricomm_amt,0) <> 0) ");
        sqa.addClause(" j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5')");

        if (getClosing().getDtPolicyDateStart() != null) {
            sqa.addClause("date_trunc('day',a.policy_date) >= ?");
            sqa.addPar(getClosing().getDtPolicyDateStart());
        }

        if (getClosing().getDtPolicyDateEnd() != null) {
            sqa.addClause("date_trunc('day',a.policy_date) <= ?");
            sqa.addPar(getClosing().getDtPolicyDateEnd());
        }

        if (getClosing().getStParameter1() != null) {
            if (getClosing().getStParameter1().equalsIgnoreCase("1")) {
                sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80,87,88)");
            } else if (getClosing().getStParameter1().equalsIgnoreCase("2")) {
                sqa.addClause("a.pol_type_id in (59,80,87,88)");
            }
        }

        /*
        if(getClosing().getStTreatyType()!=null){
        sqa.addClause("f.treaty_type = ?");
        sqa.addPar(getClosing().getStTreatyType());
        }*/

        if (getClosing().getStTreatyType() != null) {
            if (getClosing().getStTreatyType().equalsIgnoreCase("SPL") || getClosing().getStTreatyType().equalsIgnoreCase("QS")) {
                sqa.addClause("j.treaty_type in ('SPL','QS')");
            } else {
                sqa.addClause("j.treaty_type = ?");
                sqa.addPar(getClosing().getStTreatyType());
            }
        }

        if (getClosing().getStPolicyTypeID() != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(getClosing().getStPolicyTypeID());
        }

        if (getClosing().getStPolicyTypeGroupID() != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(getClosing().getStPolicyTypeGroupID());
        }

        if (getClosing().getStPolicyNo() != null) {

            String noPolis = "";
            String noPolisGabungan = getClosing().getStPolicyNo().replace("\n", "").replace("\r", "");
            String noPolisArray[] = noPolisGabungan.split("[\\,]");

            for (int k = 0; k < noPolisArray.length; k++) {

                if (k > 0) {
                    noPolis = noPolis + ",";
                }

                noPolis = noPolis + "'" + noPolisArray[k] + "'";
            }

            sqa.addClause("a.pol_no in (" + noPolis + ")");
        }

        if (getClosing().getStPolicyNoExclude() != null) {
            String noPolis = "";
            String noPolisGabungan = getClosing().getStPolicyNoExclude().replace("\n", "").replace("\r", "");
            String noPolisArray[] = noPolisGabungan.split("[\\,]");

            for (int k = 0; k < noPolisArray.length; k++) {

                if (k > 0) {
                    noPolis = noPolis + ",";
                }

                noPolis = noPolis + "'" + noPolisArray[k] + "'";
            }

            sqa.addClause("a.pol_no not in (" + noPolis + ")");
        }

        if (getClosing().getDtPeriodStartStart() != null) {
            if (getClosing().getStTreatyType() != null) {
                if (getClosing().getStTreatyType().equalsIgnoreCase("BPDAN")) {
                    sqa.addClause(" date_trunc('day',i.valid_ri_date) >= ?");
                    sqa.addPar(getClosing().getDtPeriodStartStart());
                } else {
                    sqa.addClause(" case when a.pol_type_id in (21,59,87,88) then date_trunc('day',c.refd2) >= ? "
                            + " when a.pol_type_id in (1,3,24,81) then date_trunc('day',c.refd1) >= ?"
                            + " else date_trunc('day',a.period_start) >= ? end");
                    sqa.addPar(getClosing().getDtPeriodStartStart());
                    sqa.addPar(getClosing().getDtPeriodStartStart());
                    sqa.addPar(getClosing().getDtPeriodStartStart());
                }
            }
        }


        if (getClosing().getDtPeriodStartEnd() != null) {
            if (getClosing().getStTreatyType() != null) {
                if (getClosing().getStTreatyType().equalsIgnoreCase("BPDAN")) {
                    sqa.addClause(" date_trunc('day',i.valid_ri_date) <= ?");
                    sqa.addPar(getClosing().getDtPeriodStartEnd());
                } else {
                    sqa.addClause(" case when a.pol_type_id in (21,59,87,88) then date_trunc('day',c.refd2) <= ? "
                            + " when a.pol_type_id in (1,3,24,81) then date_trunc('day',c.refd1) <= ?"
                            + " else date_trunc('day',a.period_start) <= ? end");
                    sqa.addPar(getClosing().getDtPeriodStartEnd());
                    sqa.addPar(getClosing().getDtPeriodStartEnd());
                    sqa.addPar(getClosing().getDtPeriodStartEnd());
                }
            }

        }

//        if (getClosing().getStTreatyType() != null)
//            if (!getClosing().getStTreatyType().equalsIgnoreCase("BPDAN"))
//                sqa.addClause("a.policy_date > '2016-01-01 00:00:00'");


        sqa.addGroup("a.pol_no,a.pol_id,j.ins_treaty_detail_id,i.ins_treaty_shares_id,i.member_ent_id,k.ent_name,i.ri_slip_no,"
                + " installment_f,l.installment_no,l.inst_date,binding_date,installment_count");

        //String sql = "select sum(tsi_amount) as tsi_total,sum(premi_amount) as premi_reins_total, sum(ricomm_amt) as comm_reins_total, count(pol_id) as data_amount "
                //+ " from (select * from ( " + sqa.getSQL() + ")a ";

        String sql = "select sum(tsi_amount) as tsi_total,sum(premi_amount) as premi_reins_total, sum(ricomm_amt) as comm_reins_total, sum(installment_count) as data_amount "
                + " from (select * from ( " + sqa.getSQL() + ")a ";
        /*
        if(getClosing().getDtPeriodStartStart()!=null){
        sql = sql + " where date_trunc('day',a.period_start) >= ?";
        sqa.addPar(getClosing().getDtPeriodStartStart());
        }

        if(getClosing().getDtPeriodStartEnd()!=null){
        sql = sql + " and date_trunc('day',a.period_start) <= ?";
        sqa.addPar(getClosing().getDtPeriodStartEnd());
        }
         */

        sql = sql + " ) x";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsuranceClosingView.class);

        InsuranceClosingView close = (InsuranceClosingView) l.get(0);

        getClosing().setDbTSITotal(close.getDbTSITotal());
        getClosing().setDbPremiReinsuranceTotal(close.getDbPremiReinsuranceTotal());
        getClosing().setDbComissionReinsuranceTotal(close.getDbComissionReinsuranceTotal());
        getClosing().setStDataAmount(close.getStDataAmount());


    }

    public void selectClosingType() {
    }

    public void reverse() {
        final String glpostingid = (String) getAttribute("glpostingid");

        closing = (InsuranceClosingView) DTOPool.getInstance().getDTO(InsuranceClosingView.class, glpostingid);

        if (Tools.isNo(closing.getStFinanceClosingStatus())) {
            throw new RuntimeException("Data belum di closing");
        }

        closing.setStFinanceClosingStatus("N");
        closing.setStDataProses(null);

        closing.markUpdate();
        setReverseMode(true);

        setTitle("REVERSE CLOSING");
    }

    public void doReverse() throws Exception {

        closing.validateReverse();

        getRemoteGeneralLedger().reverseClosing(closing);

        close();
    }

    /**
     * @return the reverseMode
     */
    public boolean isReverseMode() {
        return reverseMode;
    }

    /**
     * @param reverseMode the reverseMode to set
     */
    public void setReverseMode(boolean reverseMode) {
        this.reverseMode = reverseMode;
    }

    public void retrieveDataTotalClaim() throws Exception {

        if (closing.getStClosingType().equalsIgnoreCase("CLAIM_RI_OUTWARD")) {


            if (!closing.isClosingReins()) {
                String cek = "Jenis Treaty " + closing.getStTreatyType() + "<br><br>"
                        + " Klaim Disetujui : bulan " + DateUtil.getMonth2Digit(closing.getDtPolicyDateStart())
                        + " tahun " + DateUtil.getYear(closing.getDtPolicyDateEnd()) + "<br>";
                if (closing.getDtPeriodStartStart() != null || closing.getDtPeriodStartEnd() != null) {
                    cek = cek + " Klaim Periode : dari " + DateUtil.getDateStr(closing.getDtPeriodStartStart(), "dd ^^ yyyy")
                            + " sd " + DateUtil.getDateStr(closing.getDtPeriodStartEnd(), "dd ^^ yyyy") + "<br>";
                }
                if (closing.getStPolicyTypeGroupID() != null) {
                    cek = cek + " Jenis Asuransi : " + closing.getPolicyTypeGroup().getStGroupName()
                            + " - " + closing.getPolicyType().getStDescription() + "<br>";
                }
                cek = cek + " belum di-Closing Reasuransi ";

                throw new RuntimeException(cek);

            }

            retrieveDataTotalClaimOutward();
        } else if (closing.getStClosingType().equalsIgnoreCase("CLAIM_RI_INWARD")) {
            retrieveDataTotalClaimInward();
        }else if (closing.getStClosingType().equalsIgnoreCase("CLAIM_RI_INWARD_TO_OUTWARD")) {
            retrieveDataTotalClaimInwardToOutward();
        }
    }

    public void retrieveDataTotalClaimOutward() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        String sqlSelect = "a.pol_no,a.pol_id,j.ins_treaty_detail_id,i.ins_treaty_shares_id,i.member_ent_id,k.ent_name as member,i.ri_slip_no,";

        String amountSelect = " sum(round(a.claim_amount,2)*a.ccy_rate_claim) as tsi_amount, sum(round(i.claim_amount,2)*a.ccy_rate_claim) as claim_amount ";

        /*
        if (getClosing().getStTreatyType() != null) {
        if (getClosing().getStTreatyType().equalsIgnoreCase("BPDAN"))
        amountSelect = " sum(i.tsi_amount) as tsi_amount, sum(round(i.tsi_amount_e,2)*a.ccy_rate) as tsi_amount_e,"+
        " sum(round(i.premi_amount_e,2)*a.ccy_rate) as premi_amount_e,"+
        " sum(round(i.ricomm_amt_e,2)*a.ccy_rate) as ricomm_amt_e,"+
        " sum(i.premi_amount) as premi_amount,sum(i.ricomm_amt) as ricomm_amt";
        }*/

        sqlSelect = sqlSelect + amountSelect;

        sqa.addSelect(sqlSelect);

        sqa.addQuery(" from ins_policy a "
                + "  inner join ins_pol_obj c on c.pol_id = a.pol_id "
                + "    inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + "    inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + "    inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "    inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "    inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "    inner join ent_master k on k.ent_id = i.member_ent_id  ");

        sqa.addClause("a.status in ('CLAIM','CLAIM ENDORSE')");
        sqa.addClause("a.active_flag = 'Y'");
        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause("a.claim_status = 'DLA'");
        sqa.addClause("coalesce(i.claim_amount,0) <> 0 ");
        sqa.addClause(" j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5')");

        if (getClosing().getDtPolicyDateStart() != null) {
            sqa.addClause("date_trunc('day',a.claim_approved_date) >= ?");
            sqa.addPar(getClosing().getDtPolicyDateStart());
        }

        if (getClosing().getDtPolicyDateEnd() != null) {
            sqa.addClause("date_trunc('day',a.claim_approved_date) <= ?");
            sqa.addPar(getClosing().getDtPolicyDateEnd());
        }

        if (getClosing().getStParameter1() != null) {
            sqa.addClause("a.pol_type_id not in (21,59,31,32,33,80)");
        }

        /*
        if(getClosing().getStTreatyType()!=null){
        sqa.addClause("f.treaty_type = ?");
        sqa.addPar(getClosing().getStTreatyType());
        }*/

        if (getClosing().getStTreatyType() != null) {
            sqa.addClause("j.treaty_type = ?");
            sqa.addPar(getClosing().getStTreatyType());
        }

        if (getClosing().getStPolicyTypeID() != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(getClosing().getStPolicyTypeID());
        }

        if (getClosing().getStPolicyTypeGroupID() != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(getClosing().getStPolicyTypeGroupID());
        }

        if (getClosing().getDtPeriodStartStart() != null) {
            if (getClosing().getStTreatyType() != null) {
                if (getClosing().getStTreatyType().equalsIgnoreCase("BPDAN")) {
                    sqa.addClause(" date_trunc('day',i.valid_ri_date) >= ?");
                    sqa.addPar(getClosing().getDtPeriodStartStart());
                } else {
                    sqa.addClause(" case when a.pol_type_id in (21,59) then date_trunc('day',c.refd2) >= ? "
                            + " when a.pol_type_id in (1,3,24,81) then date_trunc('day',c.refd1) >= ?"
                            + " else date_trunc('day',a.period_start) >= ? end");
                    sqa.addPar(getClosing().getDtPeriodStartStart());
                    sqa.addPar(getClosing().getDtPeriodStartStart());
                    sqa.addPar(getClosing().getDtPeriodStartStart());
                }
            }
        }


        if (getClosing().getDtPeriodStartEnd() != null) {
            if (getClosing().getStTreatyType() != null) {
                if (getClosing().getStTreatyType().equalsIgnoreCase("BPDAN")) {
                    sqa.addClause(" date_trunc('day',i.valid_ri_date) <= ?");
                    sqa.addPar(getClosing().getDtPeriodStartEnd());
                } else {
                    sqa.addClause(" case when a.pol_type_id in (21,59) then date_trunc('day',c.refd2) <= ? "
                            + " when a.pol_type_id in (1,3,24,81) then date_trunc('day',c.refd1) <= ?"
                            + " else date_trunc('day',a.period_start) <= ? end");
                    sqa.addPar(getClosing().getDtPeriodStartEnd());
                    sqa.addPar(getClosing().getDtPeriodStartEnd());
                    sqa.addPar(getClosing().getDtPeriodStartEnd());
                }
            }

        }

        if (getClosing().getStDLANo() != null) {

            String noLKP = "";
            String noLKPGabungan = getClosing().getStDLANo().replace("\n", "").replace("\r", "");
            String noLKPArray[] = noLKPGabungan.split("[\\,]");

            for (int k = 0; k < noLKPArray.length; k++) {

                if (k > 0) {
                    noLKP = noLKP + ",";
                }

                noLKP = noLKP + "'" + noLKPArray[k] + "'";
            }

            sqa.addClause("a.dla_no in (" + noLKP + ")");
        }

        sqa.addGroup("a.pol_no,a.pol_id,j.ins_treaty_detail_id,i.ins_treaty_shares_id,i.member_ent_id,k.ent_name,i.ri_slip_no");

        String sql = "select sum(tsi_amount) as tsi_total,sum(claim_amount) as claim_reins_total, count(pol_id) as data_amount "
                + " from (select * from ( " + sqa.getSQL() + ")a ";
        /*
        if(getClosing().getDtPeriodStartStart()!=null){
        sql = sql + " where date_trunc('day',a.period_start) >= ?";
        sqa.addPar(getClosing().getDtPeriodStartStart());
        }

        if(getClosing().getDtPeriodStartEnd()!=null){
        sql = sql + " and date_trunc('day',a.period_start) <= ?";
        sqa.addPar(getClosing().getDtPeriodStartEnd());
        }
         */

        sql = sql + " order by pol_id,ins_treaty_detail_id ) x";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsuranceClosingView.class);

        InsuranceClosingView close = (InsuranceClosingView) l.get(0);

        getClosing().setDbTSITotal(close.getDbTSITotal());
        getClosing().setDbClaimReinsuranceTotal(close.getDbClaimReinsuranceTotal());
        getClosing().setStDataAmount(close.getStDataAmount());


    }

    public void retrieveDataTotalClaimInward() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.ar_invoice_id,c.description as description,a.invoice_date as approved_date,coalesce(d.refid0,a.refid0) as treaty,sum(a.attr_pol_tsi) as attr_pol_tsi,"
                + "';'||a.invoice_no as nobukti,';'||coalesce(a.trx_no_reference,a.attr_pol_no) as nopolis,';'||a.pla_no as pla_no,';'||a.dla_no as dla_no,"
                + "coalesce(b.ent_name,a.attr_pol_name) as nama,b.short_name,coalesce(a.attr_pol_type_id,d.attr_pol_type_id) as pol_type_id,a.ccy,a.ccy_rate,"
                + "sum(checkreas(d.ar_trx_line_id in ('74','75','76','108','109'),d.amount)) as klaim_idr,"
                + "sum(checkreas(d.ar_trx_line_id in ('101'),d.amount)) as adjfee_idr,"
                + "sum(checkreas(d.ar_trx_line_id in ('74','75','76','108','109'),d.entered_amount)) as klaim_ori,"
                + "sum(checkreas(d.ar_trx_line_id in ('101'),d.entered_amount)) as adjfee_ori ");

        sqa.addQuery(" from ins_pol_inward a "
                + "left join ent_master b on b.ent_id = a.ent_id "
                + "left join ar_trx_type c on c.ar_trx_type_id = a.ar_trx_type_id "
                + "inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id ");

        sqa.addClause("a.claim_status = 'DLA'");
        sqa.addClause("a.approved_flag = 'Y'");
        sqa.addClause("a.ar_trx_type_id in (17,18,19,25,23,24) ");

        if (getClosing().getDtPolicyDateStart() != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(getClosing().getDtPolicyDateStart());
        }

        if (getClosing().getDtPolicyDateEnd() != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(getClosing().getDtPolicyDateEnd());
        }

        if (getClosing().getDtPeriodStartStart() != null) {
            sqa.addClause("date_trunc('day',a.attr_pol_per_0) >= ?");
            sqa.addPar(getClosing().getDtPeriodStartStart());
        }

        if (getClosing().getDtPeriodStartEnd() != null) {
            sqa.addClause("date_trunc('day',a.attr_pol_per_0) <= ?");
            sqa.addPar(getClosing().getDtPeriodStartEnd());
        }

        if (getClosing().getStParameter1() != null) {
            sqa.addClause("a.attr_pol_type_id not in (21,59,31,32,33)");
        }

        if (getClosing().getStTreatyType() != null) {
            sqa.addClause("a.refid0 = ?");
            sqa.addPar(getClosing().getStTreatyType());
        }

        if (getClosing().getStPolicyTypeID() != null) {
            sqa.addClause("a.attr_pol_type_id = ?");
            sqa.addPar(getClosing().getStPolicyTypeID());
        }

        String sql = "select count(ar_invoice_id) as data_amount,sum(klaim_idr) as claim_reins_total,sum(attr_pol_tsi) as tsi_total from ( "
                + sqa.getSQL() + " group by c.description,a.ar_trx_type_id,a.attr_pol_type_id,a.invoice_date,"
                + "a.refid0,d.refid0,d.attr_pol_type_id,a.ar_invoice_id,a.invoice_no,b.ent_id,b.ent_name,b.reas_ent_id,b.short_name,a.ccy,a.ccy_rate "
                + "order by a.ar_trx_type_id,a.attr_pol_type_id ) a ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsuranceClosingView.class);

        InsuranceClosingView close = (InsuranceClosingView) l.get(0);

        getClosing().setDbTSITotal(close.getDbTSITotal());
        getClosing().setDbClaimReinsuranceTotal(close.getDbClaimReinsuranceTotal());
        getClosing().setStDataAmount(close.getStDataAmount());

    }

    public void doReverseClaim() throws Exception {

        closing.validateReverse();

        getRemoteGeneralLedger().reverseClosingClaimInward(closing);

        close();
    }

    public void retrieveDataTotalPajak() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect("a.cc_code,"
                + "sum(getpremi2(d.ar_trx_line_id in (14,17,20,30,33,36,46,49,52),a.amount)) as inv_tax21,"
                + "sum(getpremi2(d.ar_trx_line_id in (15,18,19,31,34,35,47,50,51,107),a.amount)) as inv_tax23");

        sqa.addQuery(" from ar_invoice a "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id and d.tax_code is not null ");

        sqa.addClause("a.no_surat_hutang is not null");
        sqa.addClause("a.invoice_type = 'AP'");
        //sqa.addClause("substr(a.no_surat_hutang,25,7) = '" + DateUtil.getMonth2Digit(getClosing().getDtPolicyDateStart()) + "/" + DateUtil.getYear(getClosing().getDtPolicyDateStart()) + "'");
        sqa.addClause("a.no_surat_hutang like '%" + DateUtil.getMonth2Digit(getClosing().getDtPolicyDateStart()) + "/" + DateUtil.getYear(getClosing().getDtPolicyDateStart()) + "'");

        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.ar_trx_type_id = 11");
        sqa.addClause("substr(a.refid2,0,4) = 'TAX'");

        if (getClosing().getDtPolicyDateStart() != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(getClosing().getDtPolicyDateStart());
        }

        if (getClosing().getDtPolicyDateEnd() != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(getClosing().getDtPolicyDateEnd());
        }

        //String sql = "select a.cc_code,"
        String sql = "select sum(prod_tax21) as tsi_total,"
                + "sum(inv_tax21) as premi_reins_total,"
                + "sum(prod_tax23) as comm_reins_total,"
                + "sum(inv_tax23) as claim_reins_total from ( " + sqa.getSQL()
                + "group by 1 order by 1 ) a "
                + "inner join ( select a.cc_code,"
                + "sum(getpremi2(b.ins_item_id in (11,18,25,32,12,19,26,33,88,89,90,13,20,27,34,100,105,106,107,108) and tax_code in (1,4,9),round((b.tax_amount*a.ccy_rate),2))) as prod_tax21,"
                + "sum(getpremi2(b.ins_item_id in (11,18,25,32,12,19,26,33,88,89,90,13,20,27,34,100,105,106,107,108) and tax_code in (2,5,6),round((b.tax_amount*a.ccy_rate),2))) as prod_tax23 "
                + "from ins_policies a "
                + "inner join ins_pol_items b on b.pol_id = a.pol_id "
                + "where a.status in ('POLICY','ENDORSE','RENEWAL') and a.active_flag='Y' and a.effective_flag='Y' ";

        if (getClosing().getDtPolicyDateStart() != null) {
            sql = sql + "and date_trunc('day',a.policy_date) >= '" + getClosing().getDtPolicyDateStart() + "'";
        }

        if (getClosing().getDtPolicyDateEnd() != null) {
            sql = sql + "and date_trunc('day',a.policy_date) <= '" + getClosing().getDtPolicyDateEnd() + "'";
        }

        sql = sql + " group by 1 order by 1 "
                + " ) b on b.cc_code = a.cc_code ";
        //+ "group by 1 order by 1 ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsuranceClosingView.class);

        InsuranceClosingView close = (InsuranceClosingView) l.get(0);

        getClosing().setDbTSITotal(close.getDbTSITotal());
        getClosing().setDbPremiReinsuranceTotal(close.getDbPremiReinsuranceTotal());
        getClosing().setDbComissionReinsuranceTotal(close.getDbComissionReinsuranceTotal());
        getClosing().setDbClaimReinsuranceTotal(close.getDbClaimReinsuranceTotal());

    }

    public void saveTax() throws Exception {

        closing.validate();

        getRemoteGeneralLedger().saveClosingTax(closing);

        close();
    }

    public void doReversePajak() throws Exception {

        closing.validate();

        getRemoteGeneralLedger().reverseClosingTax(closing);

        close();
    }
    private InsuranceClosingReportView closingReport;

    public InsuranceClosingReportView getClosingReport() {
        return closingReport;
    }

    public void setClosingReport(InsuranceClosingReportView closingReport) {
        this.closingReport = closingReport;
    }

    public void createNewReport() {
        closingReport = new InsuranceClosingReportView();

        closingReport.markNew();

        //getPosting().setStCostCenterCode(SessionManager.getInstance().getSession().getStBranch());

        setTitle("CLOSING REPORT REASURANSI");

    }

    public void editReport() throws Exception {
        final String glpostingid = (String) getAttribute("glpostingid");

        setEditMode(false);

        closingReport = (InsuranceClosingReportView) DTOPool.getInstance().getDTO(InsuranceClosingReportView.class, glpostingid);

        closingReport.markUpdate();

        setTitle("UBAH CLOSING");
    }

    public void viewReport() throws Exception {
        final String glpostingid = (String) getAttribute("glpostingid");

        closingReport = (InsuranceClosingReportView) DTOPool.getInstance().getDTO(InsuranceClosingReportView.class, glpostingid);

        setReadOnly(true);

        setTitle("LIHAT CLOSING");

    }

    public void saveReport() throws Exception {

//        closingReport.validate();

        getRemoteGeneralLedger().saveClosingReport(closingReport);

        close();
    }

    public void updateRISlip() throws Exception {

        String entid = closingReport.getEntity().getStEntityID();
        String jenid = closingReport.getStPolicyTypeID();
        String jenas = closingReport.getPolicyType().getStDescription();
        String bulan = closingReport.getStMonths();
        String tahun = closingReport.getStYears();

        closingReport.setStRISlipNo("SHR/" + entid + "/" + jenid + "/" + bulan + "/" + tahun);
//        closingReport.setStDescription(jenas + " "
//                + DateUtil.getMonth(closingReport.getDtPolicyDateEnd()) + " "
//                + DateUtil.getYear(closingReport.getDtPolicyDateEnd()));

    }

    /**
     * @return the editMode
     */
    public boolean isEditMode() {
        return editMode;
    }

    /**
     * @param editMode the editMode to set
     */
    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public void retrieveDataTotalKomisi() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.attr_pol_id,sum(a.amount) as comm ");

        sqa.addQuery(" from ar_invoice a "
                + "inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id and d.tax_code is null and d.ar_trx_line_id in (8,24,40)   ");

        sqa.addClause("a.no_surat_hutang is null");//PROPOSAL
        sqa.addClause("a.approved_flag is null");//PROPOSAL
        //sqa.addClause("a.surat_hutang_period_from is null");//PROPOSAL
        sqa.addClause("a.invoice_type = 'AP'");
        sqa.addClause("coalesce(a.cancel_flag,'') <> 'Y'");
        sqa.addClause("a.ar_trx_type_id = 11");
        sqa.addClause("a.amount_settled is null");

        if (getClosing().getStCostCenterCode() != null) {
            sqa.addClause("a.cc_code = ?");
            sqa.addPar(getClosing().getStCostCenterCode());
        }

        String premiPayment = " select g.pol_id "
                + "from ar_receipt f "
                + "inner join ar_receipt_lines g on g.receipt_id = f.ar_receipt_id "
                + "where f.status = 'POST' and f.ar_settlement_id in (1,25,38,41) and g.pol_id is not null ";

        if (getClosing().getDtPolicyDateStart() != null) {
            premiPayment = premiPayment + " and date_trunc('day',f.receipt_date) >= '" + getClosing().getDtPolicyDateStart() + "'";
        }

        if (getClosing().getDtPolicyDateEnd() != null) {
            premiPayment = premiPayment + " and date_trunc('day',f.receipt_date) <= '" + getClosing().getDtPolicyDateEnd() + "'";
        }

        premiPayment = premiPayment + " group by g.pol_id ";

        sqa.addClause("a.attr_pol_id in ( " + premiPayment + " ) ");

        String sql = "select count(a.attr_pol_id) as data_amount,"
                + "sum(comm) as comm_reins_total from ( " + sqa.getSQL()
                + "group by 1 order by 1 ) a where a.comm <> 0 ";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsuranceClosingView.class);

        InsuranceClosingView close = (InsuranceClosingView) l.get(0);

        getClosing().setDbComissionReinsuranceTotal(close.getDbComissionReinsuranceTotal());
        getClosing().setStDataAmount(close.getStDataAmount());

    }

    public void sendEmailProposal() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {

            final Statement S = conn.createStatement();

            /* Define the SQL query */
            ResultSet query_set = S.executeQuery(
                    " select a.* from ins_gl_closing a "
                    + "where a.closing_type IN ('COMM') and a.closing_id = " + closing.getStClosingID());

            String fileName = "proposal_komisi" + closing.getStClosingID();
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
            Document my_pdf_report = new Document();
            PdfWriter writer = PdfWriter.getInstance(my_pdf_report, fop);
            //PdfWriter.getInstance(my_pdf_report, fop);
            my_pdf_report.open();

            //we have four columns in our table
            PdfPTable my_report_table = new PdfPTable(5);
            my_report_table.setWidthPercentage(100);
            //create a cell object

            Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12),
                    smallbold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD),
                    smallc = new Font(Font.FontFamily.TIMES_ROMAN, 11);

            //insert heading
            String shk = "NO. SHK : " + closing.getStNoSuratHutang() + "\n\n";
            String Tanggal = closing.getCostCenter(closing.getStCostCenterCode()).getStDescription() + ", " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "dd ^^ yyyy"));
            Image askridaLogoPath = Image.getInstance(Parameter.readString("DIGITAL_POLIS_LOGO_PIC"));

            PdfPCell logo = null;
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            logo.setColspan(2);
            my_report_table.addCell(logo);
            logo = new PdfPCell(askridaLogoPath);
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(logo);
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            logo.setColspan(2);
            my_report_table.addCell(logo);

            PdfPCell shkTanggal = null;
            shkTanggal = new PdfPCell(new Phrase(shk, smallc));
            shkTanggal.setBorder(Rectangle.NO_BORDER);
            shkTanggal.setHorizontalAlignment(Element.ALIGN_LEFT);
            shkTanggal.setColspan(2);
            my_report_table.addCell(shkTanggal);
            shkTanggal = new PdfPCell(new Phrase(" ", small));
            shkTanggal.setBorder(Rectangle.NO_BORDER);
            shkTanggal.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(shkTanggal);
            shkTanggal = new PdfPCell(new Phrase(Tanggal, small));
            shkTanggal.setBorder(Rectangle.NO_BORDER);
            shkTanggal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            shkTanggal.setColspan(2);
            my_report_table.addCell(shkTanggal);

            //insert heading
            String created1 = "Kepada Yth.\n"
                    + "Bapak Kepala Divisi Keuangan \n"
                    + "PT. Asuransi Bangun Askrida \n"
                    + "Askrida Tower \n"
                    + "Jl. Pramuka Raya Kav. 151 \n"
                    + "Jakarta Timur 13120 \n\n"
                    + "(UP. Bpk. Nonot Haryoto, Ak, AAAIK) \n\n\n"
                    + "Dengan hormat, \n";
            PdfPCell heading = new PdfPCell(new Phrase(created1, small));
            heading.setBorder(Rectangle.NO_BORDER);
            heading.setColspan(5);
            heading.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(heading);
            my_report_table.completeRow();

            //insert judul
            PdfPCell Title = new PdfPCell(new Phrase("Perihal : Permohonan Penarikan Dana Tunai",
                    smallbold));
            Title.setBorder(Rectangle.NO_BORDER);
            Title.setColspan(5);
            Title.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(Title);
            my_report_table.completeRow();

            //insert an empty row
            PdfPCell emptyRow = new PdfPCell(new Phrase(" ", small));
            emptyRow.setBorder(Rectangle.NO_BORDER);
            emptyRow.setColspan(5);
            emptyRow.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(emptyRow);
            my_report_table.completeRow();

            //insert isi
            String created2 = "Dengan ini kami mohon persetujuan Bapak atas penarikan dana tunai sebesar Rp. " + JSPUtil.printX(closing.getDbComissionReinsuranceTotal(), 2) + " (" + LanguageManager.getInstance().translate(NumberSpell.readNumber(JSPUtil.printX(closing.getDbComissionReinsuranceTotal(), 2), "IDR")) + "), "
                    + "dengan perincian sebagai berikut (Data Terlampir).\n\n"
                    + "Demikian permohonan ini kami sampaikan dan atas perhatian serta kebijaksanaan Bapak kami menghaturkan terima kasih.";
            PdfPCell bySistem = new PdfPCell(new Phrase(created2, small));
            bySistem.setBorder(Rectangle.NO_BORDER);
            bySistem.setColspan(5);
            bySistem.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            my_report_table.addCell(bySistem);
            my_report_table.completeRow();

            //insert footer
            String created3 = "Hormat kami,\n"
                    + "PT. ASURANSI BANGUN ASKRIDA \n"
                    + "Cabang " + closing.getCostCenter(closing.getStCostCenterCode()).getStDescription();
            PdfPCell footer1 = new PdfPCell(new Phrase(created3, small));
            footer1.setBorder(Rectangle.NO_BORDER);
            footer1.setColspan(5);
            footer1.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(footer1);
            my_report_table.completeRow();

            Image img = Image.getInstance(closing.getUser(Parameter.readString("BRANCH_" + closing.getStCostCenterCode())).getFile().getStFilePath());
            //insert column data
            PdfPCell footer2 = null;
            footer2 = new PdfPCell(new Phrase(" ", small));
            footer2.setColspan(2);
            footer2.setHorizontalAlignment(Element.ALIGN_LEFT);
            footer2.setBorder(Rectangle.NO_BORDER);
            my_report_table.addCell(footer2);
            footer2 = new PdfPCell(img);
            footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
            footer2.setBorder(Rectangle.NO_BORDER);
            my_report_table.addCell(footer2);
            footer2 = new PdfPCell(new Phrase(" ", small));
            footer2.setColspan(2);
            footer2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            footer2.setBorder(Rectangle.NO_BORDER);
            my_report_table.addCell(footer2);

            String created4 = Parameter.readString("BRANCH_SIGN_" + closing.getStCostCenterCode()) + "\n"
                    + closing.getUser(Parameter.readString("BRANCH_" + closing.getStCostCenterCode())).getStJobPosition().toUpperCase();
            PdfPCell footer3 = new PdfPCell(new Phrase(created4, small));
            footer3.setBorder(Rectangle.NO_BORDER);
            footer3.setColspan(5);
            footer3.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(footer3);
            my_report_table.completeRow();

            //Create QR Code by using BarcodeQRCode Class
            BarcodeQRCode my_code = new BarcodeQRCode(DateUtil.getDateStr(new Date(), "dd-MMM-yyyy HH:mm:ss") + "_" + closing.getStNoSuratHutang() + "_" + JSPUtil.printX(closing.getDbComissionReinsuranceTotal(), 0), 3, 3, null);
            //Get Image corresponding to the input string
            Image qr_image = my_code.getImage();

            PdfPCell barcode = new PdfPCell(qr_image);
            barcode.setBorder(Rectangle.NO_BORDER);
            barcode.setColspan(5);
            barcode.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(barcode);
            my_report_table.completeRow();

            my_report_table.setWidths(new int[]{20, 20, 20, 20, 20});

            /* Attach report table to PDF */
            my_pdf_report.add(my_report_table);
            my_pdf_report.close();

            /* Define the SQL query */
            ResultSet query_set2 = S.executeQuery(
                    " select row_number() over(order by a.ar_invoice_id) as no,a.* "
                    + " from ar_invoice a "
                    + " where a.no_surat_hutang = '" + closing.getStNoSuratHutang() + "' "
                    + " order by 1 ");

            String fileName2 = "lampiran_polis_komisi" + closing.getStClosingID();

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
            PdfPTable my_report_table2 = new PdfPTable(4);
            my_report_table2.setWidthPercentage(100);

            //insert headings
            PdfPCell Title2 = new PdfPCell(new Phrase("LAPORAN PRODUKSI KOMISI",
                    smallbold));
            Title2.setBorder(Rectangle.NO_BORDER);
            Title2.setColspan(4);
            Title2.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(Title2);
            my_report_table2.completeRow();

            //insert headings
            PdfPCell Title3 = new PdfPCell(new Phrase("NO. SHK : " + closing.getStNoSuratHutang(),
                    smallbold));
            Title3.setBorder(Rectangle.NO_BORDER);
            Title3.setColspan(4);
            Title3.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(Title3);
            my_report_table2.completeRow();

            PdfPCell pertanggal2 = new PdfPCell(new Phrase("Tanggal : "
                    + LanguageManager.getInstance().translate(DateUtil.getDateStr(closing.getDtPolicyDateStart(), "dd ^^ yyyy"))
                    + " s/d "
                    + LanguageManager.getInstance().translate(DateUtil.getDateStr(closing.getDtPolicyDateEnd(), "dd ^^ yyyy")),
                    small));
            pertanggal2.setBorder(Rectangle.NO_BORDER);
            pertanggal2.setColspan(4);
            pertanggal2.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(pertanggal2);
            my_report_table2.completeRow();

            //insert an empty row
            my_report_table2.addCell(emptyRow);
            my_report_table2.completeRow();

            //insert column headings
            Phrase p[] = {new Phrase("No", smallbold),
                new Phrase("No. Polis", smallbold),
                new Phrase("Tertanggung", smallbold),
                new Phrase("Komisi", smallbold)};

            PdfPCell judno = new PdfPCell(p[0]),
                    judpol = new PdfPCell(p[1]),
                    judname = new PdfPCell(p[2]),
                    judkomisi = new PdfPCell(p[3]);

            //insert column headings
            judno.setHorizontalAlignment(judno.ALIGN_CENTER);
            judno.setVerticalAlignment(judno.ALIGN_MIDDLE);
            judno.setGrayFill(0.7f);
            judpol.setHorizontalAlignment(judpol.ALIGN_CENTER);
            judpol.setVerticalAlignment(judpol.ALIGN_MIDDLE);
            judpol.setGrayFill(0.7f);
            judname.setHorizontalAlignment(judname.ALIGN_CENTER);
            judname.setVerticalAlignment(judname.ALIGN_MIDDLE);
            judname.setGrayFill(0.7f);
            judkomisi.setHorizontalAlignment(judkomisi.ALIGN_CENTER);
            judkomisi.setVerticalAlignment(judkomisi.ALIGN_MIDDLE);
            judkomisi.setGrayFill(0.7f);

            my_report_table2.addCell(judno);
            my_report_table2.addCell(judpol);
            my_report_table2.addCell(judname);
            my_report_table2.addCell(judkomisi);

            //insert column data
            PdfPCell table_cell2 = null;
            BigDecimal komisiTotal = new BigDecimal(0);

            while (query_set2.next()) {
                BigDecimal no = query_set2.getBigDecimal("no");
                table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(no, 0), small));
                table_cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                my_report_table2.addCell(table_cell2);

                String pol_no = query_set2.getString("attr_pol_no");
                table_cell2 = new PdfPCell(new Phrase(pol_no, small));
                table_cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                my_report_table2.addCell(table_cell2);

                String custname = query_set2.getString("attr_pol_name");
                if (custname.length() > 34) {
                    custname = custname.substring(0, 34);
                } else {
                    custname = custname.substring(0, custname.length());
                }
                table_cell2 = new PdfPCell(new Phrase(custname, small));
                table_cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                my_report_table2.addCell(table_cell2);

                BigDecimal komisi = query_set2.getBigDecimal("amount");
                komisiTotal = BDUtil.add(komisiTotal, komisi);
                table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(komisi, 2), small));
                table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                my_report_table2.addCell(table_cell2);
            }

            table_cell2 = new PdfPCell(new Phrase("SUBTOTAL", smallbold));
            table_cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
            table_cell2.setColspan(3);
            my_report_table2.addCell(table_cell2);
            table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(komisiTotal, 2), smallbold));
            table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table2.addCell(table_cell2);

            String created = "Created by System " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "dd ^^ yyyy HH:mm:ss"));
            PdfPCell bySistem2 = new PdfPCell(new Phrase(created, small));
            bySistem2.setBorder(Rectangle.NO_BORDER);
            bySistem2.setColspan(4);
            bySistem2.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(bySistem2);
            my_report_table2.completeRow();

            //Create QR Code by using BarcodeQRCode Class
            BarcodeQRCode my_code2 = new BarcodeQRCode(DateUtil.getDateStr(new Date(), "dd-MMM-yyyy HH:mm:ss") + "_" + JSPUtil.printX(komisiTotal, 2), 3, 3, null);
            //Get Image corresponding to the input string
            Image qr_image2 = my_code2.getImage();

            PdfPCell barcode2 = new PdfPCell(qr_image2);
            barcode2.setBorder(Rectangle.NO_BORDER);
            barcode2.setColspan(4);
            barcode2.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(barcode2);
            my_report_table2.completeRow();

            my_report_table2.setWidths(new int[]{5, 25, 50, 20});

            /* Attach report table to PDF */
            my_pdf_report2.add(my_report_table2);
            my_pdf_report2.close();

            /* Close all DB related objects */
            query_set.close();
            query_set2.close();
            S.close();

            String receiver = "prasetyo@askrida.co.id";
            String subject = "Permohonan Penarikan Dana Tunai";
            String text = "Dengan hormat,\n\n"
                    + "Bersama ini kami lampirkan " + subject + " yang dikirim otomatis oleh sistem.\n"
                    + "Atas perhatian dan kerjasamanya, kami ucapkan terima kasih.\n\n\n"
                    + "Hormat kami,\n"
                    + "Administrator";

            MailUtil2 mail = new MailUtil2();
            mail.sendEmailMultiFile(pathTemp, fileName, pathTemp2, fileName2, null, null, null, null, null, null, receiver, null, subject, text);

        } finally {
            conn.close();
        }
    }

    public void sendEmailApproval() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {

            final Statement S = conn.createStatement();

            /* Define the SQL query */
            ResultSet query_set = S.executeQuery(
                    " select a.* from ins_gl_closing a "
                    + "where a.closing_type IN ('COMM') and a.closing_id = " + closing.getStClosingID());

            String fileName = "approval_komisi" + closing.getStClosingID();
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
            Document my_pdf_report = new Document();
            PdfWriter writer = PdfWriter.getInstance(my_pdf_report, fop);
            //PdfWriter.getInstance(my_pdf_report, fop);
            my_pdf_report.open();

            //we have four columns in our table
            PdfPTable my_report_table = new PdfPTable(5);
            my_report_table.setWidthPercentage(100);
            //create a cell object

            Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12),
                    smallb = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD),
                    smallc = new Font(Font.FontFamily.TIMES_ROMAN, 11);

            //insert heading
            String Tanggal = closing.getCostCenter(closing.getStCostCenterCode()).getStDescription() + ", " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "dd ^^ yyyy"));
            Image askridaLogoPath = Image.getInstance(Parameter.readString("DIGITAL_POLIS_LOGO_PIC"));

            PdfPCell logo = null;
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            logo.setColspan(2);
            my_report_table.addCell(logo);
            logo = new PdfPCell(askridaLogoPath);
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(logo);
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            logo.setColspan(2);
            my_report_table.addCell(logo);

            PdfPCell shkTanggal = null;
            shkTanggal = new PdfPCell(new Phrase(" ", smallc));
            shkTanggal.setBorder(Rectangle.NO_BORDER);
            shkTanggal.setHorizontalAlignment(Element.ALIGN_LEFT);
            shkTanggal.setColspan(3);
            my_report_table.addCell(shkTanggal);
            shkTanggal = new PdfPCell(new Phrase(Tanggal, small));
            shkTanggal.setBorder(Rectangle.NO_BORDER);
            shkTanggal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            shkTanggal.setColspan(2);
            my_report_table.addCell(shkTanggal);

            //insert heading
            String created1 = "Kepada Yth.\n"
                    + "Kepala Cabang " + closing.getCostCenter(closing.getStCostCenterCode()).getStDescription() + "\n"
                    + "UP. " + Parameter.readString("BRANCH_SIGN_" + closing.getStCostCenterCode()) + "\n\n\n";
            PdfPCell heading = new PdfPCell(new Phrase(created1, small));
            heading.setBorder(Rectangle.NO_BORDER);
            heading.setColspan(5);
            heading.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(heading);
            my_report_table.completeRow();

            //insert judul
            PdfPCell Title = new PdfPCell(new Phrase("Perihal : Persetujuan Penarikan Dana Tunai",
                    smallb));
            Title.setBorder(Rectangle.NO_BORDER);
            Title.setColspan(5);
            Title.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(Title);
            my_report_table.completeRow();

            //insert an empty row
            PdfPCell emptyRow = new PdfPCell(new Phrase(" ", small));
            emptyRow.setBorder(Rectangle.NO_BORDER);
            emptyRow.setColspan(5);
            emptyRow.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(emptyRow);
            my_report_table.completeRow();

            //insert isi
            String created2 = "Sehubungan dengan surat Saudara/i No. SHK " + closing.getStNoSuratHutang() + " perihal permohonan penarikan dana tunai sebesar sebesar Rp. " + JSPUtil.printX(closing.getDbComissionReinsuranceTotal(), 2) + ",- "
                    + "setelah dilakukan verifikasi atas data Kantor Cabang Surabaya dengan data di Kantor Pusat, dengan ini Direksi menyetujui penarikan dana tunai sebesar Rp. " + JSPUtil.printX(closing.getDbComissionReinsuranceTotal(), 2) + ",- "
                    + "(setelah pajak), dengan catatan premi untuk setiap polis-polis tersebut sudah masuk ke rekening perusahaan. Untuk selanjutnya setiap penarikan dana dari bank agar sesuai dengan surat Direksi No. 444/DIR/VIII/2014.\n\n"
                    + "Demikian kami sampaikan, atas perhatian Saudara/i kami ucapkan terima kasih.";
            PdfPCell bySistem = new PdfPCell(new Phrase(created2, small));
            bySistem.setBorder(Rectangle.NO_BORDER);
            bySistem.setColspan(5);
            bySistem.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            my_report_table.addCell(bySistem);
            my_report_table.completeRow();

            //insert footer
            PdfPCell footer1 = new PdfPCell(new Phrase("DIVISI KEUANGAN", smallb));
            footer1.setBorder(Rectangle.NO_BORDER);
            footer1.setColspan(5);
            footer1.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(footer1);
            my_report_table.completeRow();

            Image img = Image.getInstance(closing.getUser("01010101").getFile().getStFilePath());
            //insert column data
            PdfPCell footer2 = new PdfPCell(img);
            footer2.setBorder(Rectangle.NO_BORDER);
            footer2.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(footer2);
            footer2 = new PdfPCell(new Phrase(" ", small));
            footer2.setBorder(Rectangle.NO_BORDER);
            footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
            footer2.setColspan(4);
            my_report_table.addCell(footer2);

            String created4 = "NONOT HARYOTO\n"
                    + "Kepala Divisi";
            PdfPCell footer3 = new PdfPCell(new Phrase(created4, smallb));
            footer3.setBorder(Rectangle.NO_BORDER);
            footer3.setColspan(5);
            footer3.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(footer3);
            my_report_table.completeRow();

            //Create QR Code by using BarcodeQRCode Class
            BarcodeQRCode my_code = new BarcodeQRCode(DateUtil.getDateStr(new Date(), "dd-MMM-yyyy HH:mm:ss") + "_" + closing.getStNoSuratHutang() + "_" + JSPUtil.printX(closing.getDbComissionReinsuranceTotal(), 0), 3, 3, null);
            //Get Image corresponding to the input string
            Image qr_image = my_code.getImage();

            PdfPCell barcode = new PdfPCell(qr_image);
            barcode.setBorder(Rectangle.NO_BORDER);
            barcode.setColspan(5);
            barcode.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(barcode);
            my_report_table.completeRow();

            my_report_table.setWidths(new int[]{20, 20, 20, 20, 20});

            /* Attach report table to PDF */
            my_pdf_report.add(my_report_table);
            my_pdf_report.close();

            /* Close all DB related objects */
            query_set.close();
            S.close();

            String receiver = "prasetyo@askrida.co.id";
            String subject = "Persetujuan Penarikan Dana Tunai";
            String text = "Dengan hormat,\n\n"
                    + "Bersama ini kami lampirkan " + subject + " yang dikirim otomatis oleh sistem.\n"
                    + "Atas perhatian dan kerjasamanya, kami ucapkan terima kasih.\n\n\n"
                    + "Hormat kami,\n"
                    + "Administrator";

            MailUtil2 mail = new MailUtil2();
            mail.sendEmailWithType(pathTemp, fileName, receiver, subject, text, ".pdf");

        } finally {
            conn.close();
        }
    }

    public void createNewP() {
        setPosting(new InsurancePostingView());

        getPosting().markNew();

        setTitle("CLOSING PRODUKSI");

    }

    public void editP() {
        final String glpostingid = (String) getAttribute("glpostingid");

        posting = (InsurancePostingView) DTOPool.getInstance().getDTO(InsurancePostingView.class, glpostingid);

        if (Tools.isYes(posting.getStPostedFlag())) {
            throw new RuntimeException("Data sudah di posting");
        }

        noValidation = true;

        posting.markUpdate();

        setTitle("UBAH POSTING");
    }

    public void viewP() {
        final String glpostingid = (String) getAttribute("glpostingid");

        posting = (InsurancePostingView) DTOPool.getInstance().getDTO(InsurancePostingView.class, glpostingid);

        setReadOnly(true);

        setTitle("LIHAT POSTING");
    }

    public void openPostingP() {
        final String glpostingid = (String) getAttribute("glpostingid");

        posting = (InsurancePostingView) DTOPool.getInstance().getDTO(InsurancePostingView.class, glpostingid);

        //posting.setStPostedFlag("N");
        posting.markUpdate();

        noValidation = true;

        setTitle("BUKA CLOSING");
    }

    public void saveP() throws Exception {

        String errorMsg = "Data bulan dan tahun tsb sudah pernah di posting.<br> Cek jika status belum effective maka ubah, centang posting flag, lalu simpan untuk posting ulang. <br>Jika status sudah effective maka hub. Akuntansi Kantor Pusat untuk membukakan posting";

        if (!noValidation) {
            if (isPosted()) {
                throw new RuntimeException(errorMsg);
            }
        }

        getRemoteGeneralLedger().saveClosingP(posting, reOpenMode);

//        if (Tools.isYes(posting.getStPostedFlag())) {
//            if (posting.getStCostCenterCode() != null) {
//                calculateLabaBersih(posting.getStCostCenterCode());
//            } else {
//                clickNeraca();
//            }
//        }

        close();
    }

    /**
     * @return the posting
     */
    public InsurancePostingView getPosting() {
        return posting;
    }

    /**
     * @param posting the posting to set
     */
    public void setPosting(InsurancePostingView posting) {
        this.posting = posting;
    }

    /**
     * @return the finalMode
     */
    public boolean isFinalMode() {
        return finalMode;
    }

    /**
     * @param finalMode the finalMode to set
     */
    public void setFinalMode(boolean finalMode) {
        this.finalMode = finalMode;
    }

    /**
     * @return the reOpenMode
     */
    public boolean isReOpenMode() {
        return reOpenMode;
    }

    /**
     * @param reOpenMode the reOpenMode to set
     */
    public void setReOpenMode(boolean reOpenMode) {
        this.reOpenMode = reOpenMode;
    }

    public boolean isClosed() throws Exception {

        SQLUtil S = new SQLUtil();

        boolean isClosed = false;

        try {
            String cek = "select gl_post_id from ins_closing where months = ? and years = ? and posted_flag = 'Y' ";

            PreparedStatement PS = S.setQuery(cek);
            PS.setString(1, posting.getStMonths());
            PS.setString(2, posting.getStYears());

            ResultSet RS = PS.executeQuery();

            if (RS.next()) {
                isClosed = true;
            }

        } finally {
            S.release();
        }

        return isClosed;
    }

    public void retrieveDataTotalOutwardXOL()
            throws Exception {
        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.ar_invoice_id,c.description as description,a.invoice_date as approved_date,coalesce(d.refid0,a.refid0) as treaty,sum(a.attr_pol_tsi) as attr_pol_tsi,';'||a.invoice_no as nobukti,';'||coalesce(a.trx_no_reference,a.attr_pol_no) as nopolis,';'||a.pla_no as pla_no,';'||a.dla_no as dla_no,coalesce(b.ent_name,a.attr_pol_name) as nama,b.short_name,coalesce(a.attr_pol_type_id,d.attr_pol_type_id) as pol_type_id,a.ccy,a.ccy_rate,sum(checkreas(d.ar_trx_line_id in ('102'),d.amount)) as premi_idr,sum(checkreas(d.ar_trx_line_id in ('102'),d.entered_amount)) as premi_ori ");

        sqa.addQuery(" from ins_pol_inward a left join ent_master b on b.ent_id = a.ent_id left join ar_trx_type c on c.ar_trx_type_id = a.ar_trx_type_id inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id ");

        sqa.addClause("a.approved_flag = 'Y'");
        sqa.addClause("a.ar_trx_type_id in (22) ");
        if (getClosing().getDtPolicyDateStart() != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(getClosing().getDtPolicyDateStart());
        }
        if (getClosing().getDtPolicyDateEnd() != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(getClosing().getDtPolicyDateEnd());
        }
        if (getClosing().getDtPeriodStartStart() != null) {
            sqa.addClause("date_trunc('day',a.attr_pol_per_0) >= ?");
            sqa.addPar(getClosing().getDtPeriodStartStart());
        }
        if (getClosing().getDtPeriodStartEnd() != null) {
            sqa.addClause("date_trunc('day',a.attr_pol_per_0) <= ?");
            sqa.addPar(getClosing().getDtPeriodStartEnd());
        }
        if (getClosing().getStParameter1() != null) {
            sqa.addClause("a.attr_pol_type_id not in (21,59,31,32,33,87,88)");
        }
        if (getClosing().getStTreatyType() != null) {
            sqa.addClause("a.refid0 = ?");
            sqa.addPar(getClosing().getStTreatyType());
        }
        if (getClosing().getStPolicyTypeID() != null) {
            sqa.addClause("a.attr_pol_type_id = ?");
            sqa.addPar(getClosing().getStPolicyTypeID());
        }
        String sql = "select count(ar_invoice_id) as data_amount,sum(premi_idr) as premi_reins_total,sum(attr_pol_tsi) as tsi_total from ( " + sqa.getSQL() + " group by c.description,a.ar_trx_type_id,a.attr_pol_type_id,a.invoice_date," + "a.refid0,d.refid0,d.attr_pol_type_id,a.ar_invoice_id,a.invoice_no,b.ent_id,b.ent_name,b.reas_ent_id,b.short_name,a.ccy,a.ccy_rate " + "order by a.ar_trx_type_id,a.attr_pol_type_id ) a ";

        DTOList l = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), InsuranceClosingView.class);

        InsuranceClosingView close = (InsuranceClosingView) l.get(0);

        getClosing().setDbTSITotal(close.getDbTSITotal());
        getClosing().setDbPremiReinsuranceTotal(close.getDbPremiReinsuranceTotal());
        getClosing().setStDataAmount(close.getStDataAmount());
    }

    public void retrieveDataTotalInwardFac()
            throws Exception {
        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.ar_invoice_id,c.description as description,a.invoice_date as approved_date,coalesce(d.refid0,a.refid0) as treaty,sum(a.attr_pol_tsi) as attr_pol_tsi,';'||a.invoice_no as nobukti,';'||coalesce(a.trx_no_reference,a.attr_pol_no) as nopolis,';'||a.pla_no as pla_no,';'||a.dla_no as dla_no,coalesce(b.ent_name,a.attr_pol_name) as nama,b.short_name,coalesce(a.attr_pol_type_id,d.attr_pol_type_id) as pol_type_id,a.ccy,a.ccy_rate,sum(checkreas(d.ar_trx_line_id in ('1'),d.amount)) as premi_idr,sum(checkreas(d.ar_trx_line_id in ('1'),d.entered_amount)) as premi_ori, sum(checkreas(d.ar_trx_line_id in ('2'),d.amount)) as komisi_idr,sum(checkreas(d.ar_trx_line_id in ('2'),d.entered_amount)) as komisi_ori");

        sqa.addQuery(" from ins_pol_inward a left join ent_master b on b.ent_id = a.ent_id left join ar_trx_type c on c.ar_trx_type_id = a.ar_trx_type_id inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id ");

        sqa.addClause("a.approved_flag = 'Y'");
        sqa.addClause("a.ar_trx_type_id in (1) ");
        if (getClosing().getDtPolicyDateStart() != null) {
            sqa.addClause("date_trunc('day',a.due_date) >= ?");
            sqa.addPar(getClosing().getDtPolicyDateStart());
        }
        if (getClosing().getDtPolicyDateEnd() != null) {
            sqa.addClause("date_trunc('day',a.due_date) <= ?");
            sqa.addPar(getClosing().getDtPolicyDateEnd());
        }
        if (getClosing().getDtPeriodStartStart() != null) {
            sqa.addClause("date_trunc('day',a.attr_pol_per_0) >= ?");
            sqa.addPar(getClosing().getDtPeriodStartStart());
        }
        if (getClosing().getDtPeriodStartEnd() != null) {
            sqa.addClause("date_trunc('day',a.attr_pol_per_0) <= ?");
            sqa.addPar(getClosing().getDtPeriodStartEnd());
        }
        if (getClosing().getStParameter1() != null) {
            sqa.addClause("a.attr_pol_type_id not in (21,59,31,32,33,87,88)");
        }
        if (getClosing().getStTreatyType() != null) {
            sqa.addClause("a.refid0 = ?");
            sqa.addPar(getClosing().getStTreatyType());
        }
        if (getClosing().getStPolicyTypeID() != null) {
            sqa.addClause("a.attr_pol_type_id = ?");
            sqa.addPar(getClosing().getStPolicyTypeID());
        }
        String sql = "select count(ar_invoice_id) as data_amount,sum(premi_idr) as premi_reins_total,sum(komisi_idr) as comm_reins_total,sum(attr_pol_tsi) as tsi_total from ( " + sqa.getSQL() + " group by c.description,a.ar_trx_type_id,a.attr_pol_type_id,a.invoice_date," + "a.refid0,d.refid0,d.attr_pol_type_id,a.ar_invoice_id,a.invoice_no,b.ent_id,b.ent_name,b.reas_ent_id,b.short_name,a.ccy,a.ccy_rate " + "order by a.ar_trx_type_id,a.attr_pol_type_id ) a ";

        DTOList l = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), InsuranceClosingView.class);

        InsuranceClosingView close = (InsuranceClosingView) l.get(0);

        getClosing().setDbTSITotal(close.getDbTSITotal());
        getClosing().setDbPremiReinsuranceTotal(close.getDbPremiReinsuranceTotal());
        getClosing().setDbComissionReinsuranceTotal(close.getDbComissionReinsuranceTotal());
        getClosing().setStDataAmount(close.getStDataAmount());

    }

    

    public void viewSOA() {
        final String glpostingid = (String) getAttribute("glpostingid");

        soa = (InsurancePolicySOAView) DTOPool.getInstance().getDTO(InsurancePolicySOAView.class, glpostingid);

        setReadOnly(true);

        setTitle("LIHAT SOA");

    }

    public void editSOA() {
        final String glpostingid = (String) getAttribute("glpostingid");

        soa = (InsurancePolicySOAView) DTOPool.getInstance().getDTO(InsurancePolicySOAView.class, glpostingid);

        if (Tools.isYes(soa.getStStatus())) {
            throw new RuntimeException("Data sudah di posting");
        }

        closing.markUpdate();

        setTitle("UBAH SOA");
    }

    public void processSOA() throws Exception {

        final String glpostingid = (String) getAttribute("glpostingid");

        soa = (InsurancePolicySOAView) DTOPool.getInstance().getDTO(InsurancePolicySOAView.class, glpostingid);

        getRemoteGeneralLedger().postSOA(new DTOList(),soa);

    }
 
    public void retrieveData()  throws Exception{
        if (closing.getStClosingType().equalsIgnoreCase("PREMIUM_RI_OUTWARD_XOL")){
            retrieveDataTotalOutwardXOL();
        }else if (closing.getStClosingType().equalsIgnoreCase("PREMIUM_RI_INWARD_FAC")){
            retrieveDataTotalInwardFac();
        }else if (closing.getStClosingType().equalsIgnoreCase("PROFIT_COMMISION_INWARD")){
            retrieveDataTotalProfitCommision();
        }else{
            retrieveDataTotal();
        }


    }

    public void retrieveDataTotalProfitCommision()
            throws Exception {
        SQLAssembler sqa = new SQLAssembler();

        sqa.addSelect(" a.ar_invoice_id,c.description as description,a.invoice_date as approved_date,coalesce(d.refid0,a.refid0) as treaty,sum(a.attr_pol_tsi) as attr_pol_tsi,';'||a.invoice_no as nobukti,';'||coalesce(a.trx_no_reference,a.attr_pol_no) as nopolis,';'||a.pla_no as pla_no,';'||a.dla_no as dla_no,coalesce(b.ent_name,a.attr_pol_name) as nama,b.short_name,coalesce(a.attr_pol_type_id,d.attr_pol_type_id) as pol_type_id,a.ccy,a.ccy_rate,sum(checkreas(d.ar_trx_line_id in ('79','97'),d.amount)) as premi_idr,sum(checkreas(d.ar_trx_line_id in ('79','97'),d.entered_amount)) as premi_ori, sum(checkreas(d.ar_trx_line_id in ('80','100'),d.amount)) as komisi_idr,sum(checkreas(d.ar_trx_line_id in ('80','100'),d.entered_amount)) as komisi_ori");

        sqa.addQuery(" from ins_pol_inward a left join ent_master b on b.ent_id = a.ent_id left join ar_trx_type c on c.ar_trx_type_id = a.ar_trx_type_id inner join ins_pol_inward_details d on d.ar_invoice_id = a.ar_invoice_id ");

        sqa.addClause("a.approved_flag = 'Y'");
        sqa.addClause("a.ar_trx_type_id in (20,21) ");

        if (getClosing().getDtPolicyDateStart() != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) >= ?");
            sqa.addPar(getClosing().getDtPolicyDateStart());
        }
        if (getClosing().getDtPolicyDateEnd() != null) {
            sqa.addClause("date_trunc('day',a.mutation_date) <= ?");
            sqa.addPar(getClosing().getDtPolicyDateEnd());
        }
        if (getClosing().getDtPeriodStartStart() != null) {
            sqa.addClause("date_trunc('day',a.attr_pol_per_0) >= ?");
            sqa.addPar(getClosing().getDtPeriodStartStart());
        }
        if (getClosing().getDtPeriodStartEnd() != null) {
            sqa.addClause("date_trunc('day',a.attr_pol_per_0) <= ?");
            sqa.addPar(getClosing().getDtPeriodStartEnd());
        }
        if (getClosing().getStParameter1() != null) {
            sqa.addClause("a.attr_pol_type_id not in (21,59,31,32,33)");
        }
        if (getClosing().getStTreatyType() != null) {
            sqa.addClause("a.refid0 = ?");
            sqa.addPar(getClosing().getStTreatyType());
        }
        if (getClosing().getStPolicyTypeID() != null) {
            sqa.addClause("a.attr_pol_type_id = ?");
            sqa.addPar(getClosing().getStPolicyTypeID());
        }
        String sql = "select count(ar_invoice_id) as data_amount,sum(premi_idr) as premi_reins_total,sum(komisi_idr) as comm_reins_total,sum(attr_pol_tsi) as tsi_total from ( " + sqa.getSQL() + " group by c.description,a.ar_trx_type_id,a.attr_pol_type_id,a.invoice_date," + "a.refid0,d.refid0,d.attr_pol_type_id,a.ar_invoice_id,a.invoice_no,b.ent_id,b.ent_name,b.reas_ent_id,b.short_name,a.ccy,a.ccy_rate " + "order by a.ar_trx_type_id,a.attr_pol_type_id ) a ";

        DTOList l = ListUtil.getDTOListFromQuery(sql, sqa.getPar(), InsuranceClosingView.class);

        InsuranceClosingView close = (InsuranceClosingView) l.get(0);

        getClosing().setDbTSITotal(close.getDbTSITotal());
        getClosing().setDbPremiReinsuranceTotal(close.getDbPremiReinsuranceTotal());
        getClosing().setDbComissionReinsuranceTotal(close.getDbComissionReinsuranceTotal());
        getClosing().setStDataAmount(close.getStDataAmount());

    }

    public void retrieveDataTotalClaimInwardToOutward() throws Exception {

        final SQLAssembler sqa = new SQLAssembler();

        String sqlSelect = "a.pol_no,a.pol_id,j.ins_treaty_detail_id,i.ins_treaty_shares_id,i.member_ent_id,k.ent_name as member,i.ri_slip_no,";

        String amountSelect = " sum(round(a.claim_amount,2)*a.ccy_rate_claim) as tsi_amount, sum(round(i.claim_amount,2)*a.ccy_rate_claim) as claim_amount ";

        /*
        if (getClosing().getStTreatyType() != null) {
        if (getClosing().getStTreatyType().equalsIgnoreCase("BPDAN"))
        amountSelect = " sum(i.tsi_amount) as tsi_amount, sum(round(i.tsi_amount_e,2)*a.ccy_rate) as tsi_amount_e,"+
        " sum(round(i.premi_amount_e,2)*a.ccy_rate) as premi_amount_e,"+
        " sum(round(i.ricomm_amt_e,2)*a.ccy_rate) as ricomm_amt_e,"+
        " sum(i.premi_amount) as premi_amount,sum(i.ricomm_amt) as ricomm_amt";
        }*/

        sqlSelect = sqlSelect + amountSelect;

        sqa.addSelect(sqlSelect);

        sqa.addQuery(" from ins_policy a "
                + "  inner join ins_pol_obj c on c.pol_id = a.pol_id "
                + "    inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
                + "    inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                + "    inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                + "    inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                + "    inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                + "    inner join ent_master k on k.ent_id = i.member_ent_id  ");

        sqa.addClause("a.status in ('CLAIM INWARD','ENDORSE CLAIM INWARD')");
        sqa.addClause("a.active_flag = 'Y'");
        sqa.addClause("a.effective_flag = 'Y'");
        sqa.addClause("a.claim_status = 'DLA'");
        sqa.addClause("coalesce(i.claim_amount,0) <> 0 ");
        sqa.addClause(" j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5')");

        if (getClosing().getDtPolicyDateStart() != null) {
            sqa.addClause("date_trunc('day',a.approved_date) >= ?");
            sqa.addPar(getClosing().getDtPolicyDateStart());
        }

        if (getClosing().getDtPolicyDateEnd() != null) {
            sqa.addClause("date_trunc('day',a.approved_date) <= ?");
            sqa.addPar(getClosing().getDtPolicyDateEnd());
        }

        if (getClosing().getStParameter1() != null) {
            sqa.addClause("a.pol_type_id not in (21,59,31,32,33)");
        }

        /*
        if(getClosing().getStTreatyType()!=null){
        sqa.addClause("f.treaty_type = ?");
        sqa.addPar(getClosing().getStTreatyType());
        }*/

        if (getClosing().getStTreatyType() != null) {
            sqa.addClause("j.treaty_type = ?");
            sqa.addPar(getClosing().getStTreatyType());
        }

        if (getClosing().getStPolicyTypeID() != null) {
            sqa.addClause("a.pol_type_id = ?");
            sqa.addPar(getClosing().getStPolicyTypeID());
        }

        if (getClosing().getStPolicyTypeGroupID() != null) {
            sqa.addClause("a.ins_policy_type_grp_id = ?");
            sqa.addPar(getClosing().getStPolicyTypeGroupID());
        }

        if (getClosing().getDtPeriodStartStart() != null) {
            if (getClosing().getStTreatyType() != null) {
                if (getClosing().getStTreatyType().equalsIgnoreCase("BPDAN")) {
                    sqa.addClause(" date_trunc('day',i.valid_ri_date) >= ?");
                    sqa.addPar(getClosing().getDtPeriodStartStart());
                } else {
                    sqa.addClause(" case when a.pol_type_id in (21,59) then date_trunc('day',c.refd2) >= ? "
                            + " when a.pol_type_id in (1,3,24,81) then date_trunc('day',c.refd1) >= ?"
                            + " else date_trunc('day',a.period_start) >= ? end");
                    sqa.addPar(getClosing().getDtPeriodStartStart());
                    sqa.addPar(getClosing().getDtPeriodStartStart());
                    sqa.addPar(getClosing().getDtPeriodStartStart());
                }
            }
        }


        if (getClosing().getDtPeriodStartEnd() != null) {
            if (getClosing().getStTreatyType() != null) {
                if (getClosing().getStTreatyType().equalsIgnoreCase("BPDAN")) {
                    sqa.addClause(" date_trunc('day',i.valid_ri_date) <= ?");
                    sqa.addPar(getClosing().getDtPeriodStartEnd());
                } else {
                    sqa.addClause(" case when a.pol_type_id in (21,59) then date_trunc('day',c.refd2) <= ? "
                            + " when a.pol_type_id in (1,3,24,81) then date_trunc('day',c.refd1) <= ?"
                            + " else date_trunc('day',a.period_start) <= ? end");
                    sqa.addPar(getClosing().getDtPeriodStartEnd());
                    sqa.addPar(getClosing().getDtPeriodStartEnd());
                    sqa.addPar(getClosing().getDtPeriodStartEnd());
                }
            }

        }

        if (getClosing().getStDLANo() != null) {

            String noLKP = "";
            String noLKPGabungan = getClosing().getStDLANo().replace("\n", "").replace("\r", "");
            String noLKPArray[] = noLKPGabungan.split("[\\,]");

            for (int k = 0; k < noLKPArray.length; k++) {

                if (k > 0) {
                    noLKP = noLKP + ",";
                }

                noLKP = noLKP + "'" + noLKPArray[k] + "'";
            }

            sqa.addClause("a.dla_no in (" + noLKP + ")");
        }

        sqa.addGroup("a.pol_no,a.pol_id,j.ins_treaty_detail_id,i.ins_treaty_shares_id,i.member_ent_id,k.ent_name,i.ri_slip_no");

        String sql = "select sum(tsi_amount) as tsi_total,sum(claim_amount) as claim_reins_total, count(pol_id) as data_amount "
                + " from (select * from ( " + sqa.getSQL() + ")a ";
        /*
        if(getClosing().getDtPeriodStartStart()!=null){
        sql = sql + " where date_trunc('day',a.period_start) >= ?";
        sqa.addPar(getClosing().getDtPeriodStartStart());
        }

        if(getClosing().getDtPeriodStartEnd()!=null){
        sql = sql + " and date_trunc('day',a.period_start) <= ?";
        sqa.addPar(getClosing().getDtPeriodStartEnd());
        }
         */

        sql = sql + " order by pol_id,ins_treaty_detail_id ) x";

        final DTOList l = ListUtil.getDTOListFromQuery(
                sql,
                sqa.getPar(),
                InsuranceClosingView.class);

        InsuranceClosingView close = (InsuranceClosingView) l.get(0);

        getClosing().setDbTSITotal(close.getDbTSITotal());
        getClosing().setDbClaimReinsuranceTotal(close.getDbClaimReinsuranceTotal());
        getClosing().setStDataAmount(close.getStDataAmount());


    }


}
