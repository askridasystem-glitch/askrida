/***********************************************************************
 * Module:  com.crux.common.jobs.NeracaTotalReport
 * Author:  Denny Mahendra
 * Created: Jul 12, 2004 12:24:48 PM
 * Purpose:
 ***********************************************************************/
package com.crux.common.jobs;

import com.crux.jobs.util.JobUtil;
import com.crux.util.BDUtil;
import org.quartz.StatefulJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.crux.util.LogManager;
import com.crux.util.ConnectionCache;
import com.crux.util.DTOList;
import com.crux.util.JNDIUtil;
import com.crux.util.ListUtil;
import com.crux.util.SQLUtil;
import com.crux.util.Tools;
import com.webfin.gl.ejb.GLReportEngine2;
import com.webfin.gl.ejb.GeneralLedger;
import com.webfin.gl.ejb.GeneralLedgerHome;
import com.webfin.gl.model.AccountView2;
import com.webfin.gl.model.GLCostCenterView;
import com.webfin.gl.model.GLNeracaTotalView;
import com.webfin.gl.model.GLPostingView;
import com.webfin.gl.model.PeriodDetailView;
import com.webfin.insurance.model.InsurancePolicyTypeView;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Calendar;
import javax.ejb.CreateException;
import javax.naming.NamingException;

public class NeracaTotalReport implements StatefulJob {

    private final static transient LogManager logger = LogManager.getInstance(NeracaTotalReport.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {

            if (JobUtil.isServerProduction()) {
                execute1();
            }
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    public void execute1() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            logger.logInfo("execute: performing proses delete konversi file 2");

            long t = System.currentTimeMillis();

            final Statement S = conn.createStatement();

            final DTOList file = ListUtil.getDTOListFromQuery(
                    " select a.* from gl_posting a where a.cc_code is null "
                    //+ " and years = '2018' and months = '07' "
                    + " order by years::bigint desc,months::int desc,gl_post_id desc "
                    + "limit 1 ",
                    GLPostingView.class);

            logger.logDebug("################### : " + file.size());

            for (int i = 0; i < file.size(); i++) {
                GLPostingView posting = (GLPostingView) file.get(i);

                if (Tools.isYes(posting.getStConfigFlag())) {
                    continue;
                }

                if (Tools.isYes(posting.getStFinalFlag())) {
                    clickNeracaTotal(posting);
                    clickNeracaTotalPerBulan(posting);
                    clickNeracaTotalJenas(posting);
                    clickNeracaTotalCabang(posting);

//                    clickRekapProduksi(posting);
//                    clickRekapKlaim(posting);

                    updateFlagPosting(posting);
                }

            }

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 2 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }
    }

    public void clickNeracaTotal(GLPostingView posting) throws Exception {

        long lPeriodFrom = Integer.parseInt(posting.getStMonths());
        long lPeriodTo = Integer.parseInt(posting.getStMonths());
        long lYearFrom = Integer.parseInt(posting.getStYears());
        //String postingid = posting.getStGLPostingID();

        AccountView2 account = new AccountView2();

        final DTOList neraca = account.getGLNeraca();

        for (int i = 0; i < neraca.size(); i++) {
            GLNeracaTotalView nrc = (GLNeracaTotalView) neraca.get(i);

            GLReportEngine2 glr = new GLReportEngine2();
            String accountid = nrc.getStGLNeracaID();

            String sheet[] = nrc.getStReference1().split("[\\|]");
            if (sheet.length == 1) {

                String akun1 = nrc.getStReference1();
                String akun2 = null;
                if (nrc.getStReference2() != null) {
                    akun2 = nrc.getStReference2();
                } else {
                    akun2 = nrc.getStReference1();
                }

                BigDecimal neracaItem = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", akun1, akun2, lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

                getRemoteGeneralLedger().AccountNeracaItem(accountid, lPeriodFrom, lYearFrom, neracaItem, "Y");

            } else if (sheet.length > 1) {

                BigDecimal[] t = new BigDecimal[1];
                for (int k = 0; k < sheet.length; k++) {

                    logger.logInfo("@@@@@@@@@@@@@@@@@@@2 : " + sheet[k]);

                    BigDecimal neracaItem = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", sheet[k], sheet[k], lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

                    int n = 0;
                    t[n] = BDUtil.add(t[n++], neracaItem);

                    logger.logInfo("################### : " + neracaItem);

                }

                logger.logInfo("$$$$$$$$$$$$$$$$$$$ : " + t[0]);

                getRemoteGeneralLedger().AccountNeracaItem(accountid, lPeriodFrom, lYearFrom, t[0], "Y");

            }

        }
    }

    public void clickNeracaTotalPerBulan(GLPostingView posting) throws Exception {

        long lPeriodFrom = Integer.parseInt(posting.getStMonths());
        long lPeriodTo = Integer.parseInt(posting.getStMonths());
        long lYearFrom = Integer.parseInt(posting.getStYears());
        //String postingid = posting.getStGLPostingID();

        AccountView2 account = new AccountView2();

        final DTOList neraca = account.getGLNeraca();

        for (int i = 0; i < neraca.size(); i++) {
            GLNeracaTotalView nrc = (GLNeracaTotalView) neraca.get(i);

            GLReportEngine2 glr = new GLReportEngine2();
            String accountid = nrc.getStGLNeracaID();

            String sheet[] = nrc.getStReference1().split("[\\|]");
            if (sheet.length == 1) {

                String akun1 = nrc.getStReference1();
                String akun2 = null;
                if (nrc.getStReference2() != null) {
                    akun2 = nrc.getStReference2();
                } else {
                    akun2 = nrc.getStReference1();
                }

                BigDecimal neracaItem = BDUtil.roundUp(glr.getSummaryRangedOnePeriod("BAL|ADD=0", akun1, akun2, lPeriodTo, lYearFrom, lYearFrom));

                getRemoteGeneralLedger().AccountNeracaItemPerBulan(accountid, lPeriodFrom, lYearFrom, neracaItem, "Y");

            } else if (sheet.length > 1) {

                BigDecimal[] t = new BigDecimal[1];
                for (int k = 0; k < sheet.length; k++) {

                    logger.logInfo("@@@@@@@@@@@@@@@@@@@ : " + sheet[k]);

                    BigDecimal neracaItem = BDUtil.roundUp(glr.getSummaryRangedOnePeriod("BAL|ADD=0", sheet[k], sheet[k], lPeriodTo, lYearFrom, lYearFrom));

                    int n = 0;
                    t[n] = BDUtil.add(t[n++], neracaItem);

                    logger.logInfo("################### : " + neracaItem);

                }


                logger.logInfo("$$$$$$$$$$$$$$$$$$$ : " + t[0]);

                getRemoteGeneralLedger().AccountNeracaItemPerBulan(accountid, lPeriodFrom, lYearFrom, t[0], "Y");

            }

        }
    }

    public void updateFlagPosting(GLPostingView posted) throws Exception {

        final SQLUtil S = new SQLUtil();
        try {

            PreparedStatement PS = S.setQuery("update gl_posting set config_flag = 'Y' where gl_post_id = ? ");

            PS.setObject(1, posted.getStGLPostingID());

            int hasil = PS.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            S.release();
        }

    }

    private GeneralLedger getRemoteGeneralLedger() throws NamingException, ClassNotFoundException, CreateException, RemoteException {
        return ((GeneralLedgerHome) JNDIUtil.getInstance().lookup("GeneralLedgerEJB", GeneralLedgerHome.class.getName())).create();
    }

    public void clickNeracaTotalJenas(GLPostingView posting) throws Exception {

        long lPeriodFrom = Integer.parseInt(posting.getStMonths());
        long lPeriodTo = Integer.parseInt(posting.getStMonths());
        long lYearFrom = Integer.parseInt(posting.getStYears());
        //String postingid = posting.getStGLPostingID();

        AccountView2 account = new AccountView2();

        final DTOList neraca = account.getGLNeraca();

        for (int i = 0; i < neraca.size(); i++) {
            GLNeracaTotalView nrc = (GLNeracaTotalView) neraca.get(i);

            GLReportEngine2 glr = new GLReportEngine2();
            String accountid = nrc.getStGLNeracaID();
//                String akun1 = nrc.getStReference1();
            boolean isJenas = Tools.isYes(nrc.getStKeterangan());

            String sheet[] = nrc.getStReference1().split("[\\|]");
            if (sheet.length == 1) {

                BigDecimal neracaItem = new BigDecimal(0);

                if (isJenas) {
                    final DTOList jenas = getPolicyType();
                    for (int j = 0; j < jenas.size(); j++) {
                        InsurancePolicyTypeView pol = (InsurancePolicyTypeView) jenas.get(j);

                        neracaItem = BDUtil.roundUp(glr.getSummaryRangedWithPolType("BAL|ADD=0", sheet[0], sheet[0], pol.getStPolicyTypeID(), lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
                        getRemoteGeneralLedger().AccountNeracaItemJenas(accountid, lPeriodFrom, lYearFrom, neracaItem, "Y", pol.getStPolicyTypeID());
                    }
                } else {
                    neracaItem = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", sheet[0], sheet[0], lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
                    getRemoteGeneralLedger().AccountNeracaItemJenas(accountid, lPeriodFrom, lYearFrom, neracaItem, "Y", null);
                }

            } else if (sheet.length > 1) {
                BigDecimal neracaItem = new BigDecimal(0);

                if (isJenas) {
                    final DTOList jenas = getPolicyType();
                    for (int j = 0; j < jenas.size(); j++) {
                        InsurancePolicyTypeView pol = (InsurancePolicyTypeView) jenas.get(j);

                        BigDecimal[] t = new BigDecimal[1];
                        for (int k = 0; k < sheet.length; k++) {
                            neracaItem = BDUtil.roundUp(glr.getSummaryRangedWithPolType("BAL|ADD=0", sheet[k], sheet[k], pol.getStPolicyTypeID(), lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
                            logger.logDebug("!!!!!!!!!!!!!!!!!!! : " + sheet[k] + " - " + neracaItem + " - " + pol.getStPolicyTypeID());

                            int n = 0;
                            t[n] = BDUtil.add(t[n++], neracaItem);
                        }
                        logger.logDebug("@@@@@@@@@@@@@@@@@@@ : " + pol.getStPolicyTypeID() + " - " + t[0]);
                        getRemoteGeneralLedger().AccountNeracaItemJenas(accountid, lPeriodFrom, lYearFrom, t[0], "Y", pol.getStPolicyTypeID());
                    }
                } else {
                    BigDecimal[] t = new BigDecimal[1];
                    for (int k = 0; k < sheet.length; k++) {

                        neracaItem = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", sheet[k], sheet[k], lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));

                        int n = 0;
                        t[n] = BDUtil.add(t[n++], neracaItem);
                    }
                    getRemoteGeneralLedger().AccountNeracaItemJenas(accountid, lPeriodFrom, lYearFrom, t[0], "Y", null);
                }


//                BigDecimal[] t = new BigDecimal[1];
//                for (int k = 0; k < sheet.length; k++) {
//
//                    logger.logInfo("@@@@@@@@@@@@@@@@@@@2 : " + sheet[k]);
//
//                    BigDecimal neracaItem = new BigDecimal(0);
//
//                    if (isJenas) {
//                        final DTOList jenas = getPolicyType();
//                        for (int j = 0; j < jenas.size(); j++) {
//                            InsurancePolicyTypeView poltype = (InsurancePolicyTypeView) jenas.get(j);
//
//                            neracaItem = BDUtil.roundUp(glr.getSummaryRangedWithPolType("BAL|ADD=0", sheet[k], sheet[k], poltype.getStPolicyTypeID(), lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
//                        }
//                    } else {
//                        neracaItem = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", sheet[k], sheet[k], lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
//                    }
//
//                    int n = 0;
//                    t[n] = BDUtil.add(t[n++], neracaItem);
//
//                    logger.logInfo("################### : " + neracaItem);
//
//                }
//
//                getRemoteGeneralLedger().AccountNeracaItemJenas(accountid, lPeriodFrom, lYearFrom, t[0], "Y", null);
//                logger.logInfo("$$$$$$$$$$$$$$$$$$$ : " + t[0]);

            }
        }
    }

    public DTOList getPolicyType() {
        try {
            return ListUtil.getDTOListFromQuery(
                    "select * from ins_policy_types order by pol_type_id",
                    InsurancePolicyTypeView.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    public void execute1(String bulan, String tahun) throws Exception {
//        final Connection conn = ConnectionCache.getInstance().getConnection();
//
//        try {
//            logger.logInfo("execute: performing proses delete konversi file 2");
//
//            long t = System.currentTimeMillis();
//
//            final Statement S = conn.createStatement();
//
//            final DTOList file = ListUtil.getDTOListFromQuery(
//                    " select a.* from gl_posting a where a.cc_code is null "
//                    + "and years = '" + tahun + "' and months = '" + bulan + "' "
//                    + "order by years::bigint desc,months::int desc,gl_post_id desc ",
//                    GLPostingView.class);
//
//            logger.logDebug("################### : " + file.size());
//
//            for (int i = 0; i < file.size(); i++) {
//                GLPostingView posting = (GLPostingView) file.get(i);
//
//                clickNeracaTotalJenas(posting);
//            }
//
//            t = System.currentTimeMillis() - t;
//
//            logger.logInfo("proses 2 selesai dalam " + t + " ms");
//
//        } finally {
//            conn.close();
//        }
//    }
    public void clickNeracaTotalCabang(GLPostingView posting) throws Exception {

        long lPeriodFrom = Integer.parseInt(posting.getStMonths());
        long lPeriodTo = Integer.parseInt(posting.getStMonths());
        long lYearFrom = Integer.parseInt(posting.getStYears());
        //String postingid = posting.getStGLPostingID();

        AccountView2 account = new AccountView2();

        final DTOList neraca = account.getGLNeraca();

        for (int i = 0; i < neraca.size(); i++) {
            GLNeracaTotalView nrc = (GLNeracaTotalView) neraca.get(i);

            GLReportEngine2 glr = new GLReportEngine2();
            String accountid = nrc.getStGLNeracaID();
//            boolean isJenas = Tools.isYes(nrc.getStKeterangan());

            String sheet[] = nrc.getStReference1().split("[\\|]");
            if (sheet.length == 1) {

                BigDecimal neracaItem = new BigDecimal(0);

//                if (isJenas) {
                final DTOList jenas = getCostCenterCode();
                for (int j = 0; j < jenas.size(); j++) {
                    GLCostCenterView pol = (GLCostCenterView) jenas.get(j);

                    neracaItem = BDUtil.roundUp(glr.getSummaryRangedBranch2("BAL|ADD=0", sheet[0], sheet[0], lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom, pol.getStCostCenterCode()));
                    getRemoteGeneralLedger().AccountNeracaItemCabang(accountid, lPeriodFrom, lYearFrom, neracaItem, "Y", pol.getStCostCenterCode());
                }
//                } else {
//                    neracaItem = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", sheet[0], sheet[0], lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
//                    getRemoteGeneralLedger().AccountNeracaItemJenas(accountid, lPeriodFrom, lYearFrom, neracaItem, "Y", null);
//                }

            } else if (sheet.length > 1) {
                BigDecimal neracaItem = new BigDecimal(0);

//                if (isJenas) {
                final DTOList jenas = getCostCenterCode();
                for (int j = 0; j < jenas.size(); j++) {
                    GLCostCenterView pol = (GLCostCenterView) jenas.get(j);

                    BigDecimal[] t = new BigDecimal[1];
                    for (int k = 0; k < sheet.length; k++) {
                        neracaItem = BDUtil.roundUp(glr.getSummaryRangedBranch2("BAL|ADD=0", sheet[k], sheet[k], lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom, pol.getStCostCenterCode()));
                        logger.logDebug("!!!!!!!!!!!!!!!!!!! : " + sheet[k] + " - " + neracaItem + " - " + pol.getStCostCenterCode());

                        int n = 0;
                        t[n] = BDUtil.add(t[n++], neracaItem);
                    }
                    logger.logDebug("@@@@@@@@@@@@@@@@@@@ : " + pol.getStCostCenterCode() + " - " + t[0]);
                    getRemoteGeneralLedger().AccountNeracaItemCabang(accountid, lPeriodFrom, lYearFrom, t[0], "Y", pol.getStCostCenterCode());
                }
//                } else {
//                    BigDecimal[] t = new BigDecimal[1];
//                    for (int k = 0; k < sheet.length; k++) {
//
//                        neracaItem = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", sheet[k], sheet[k], lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
//
//                        int n = 0;
//                        t[n] = BDUtil.add(t[n++], neracaItem);
//                    }
//                    getRemoteGeneralLedger().AccountNeracaItemJenas(accountid, lPeriodFrom, lYearFrom, t[0], "Y", null);
//                }
            }
        }
    }

    public DTOList getCostCenterCode() {
        try {
            return ListUtil.getDTOListFromQuery(
                    "select * from gl_cost_center order by cc_code",
                    GLCostCenterView.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void clickRekapProduksi(GLPostingView posting) throws Exception {

        final SQLUtil S = new SQLUtil();

        try {

            final String proses1 = " DELETE from ins_rekap_produksi where tahun =  '" + posting.getStYears() + "' and bulan = '" + posting.getStMonths() + "'";

            PreparedStatement PS1 = S.setQuery(proses1);
            logger.logDebug("####################### " + proses1);

            final String proses2 = " INSERT INTO ins_rekap_produksi(tahun,bulan,cc_code,region_id,ins_policy_type_grp_id,pol_type_id,sumbis,premi) "
                    + "select tahun,bulan,cc_code,region_id,ins_policy_type_grp_id,pol_type_id,sumbis,(premi-diskon) as premi "
                    + "from ( select substr(a.approved_date::text,1,4) as tahun, "
                    + "substr(a.approved_date::text,6,2) as bulan,"
                    + "a.cc_code,a.region_id,a.ins_policy_type_grp_id,a.pol_type_id,e.category1 as sumbis,"
                    + "sum(getpremiend(b.entity_id,coalesce(a.premi_total*a.ccy_rate,0),coalesce(b.premi_amt*a.ccy_rate,0)*-1)) as premi,"
                    + "sum(getpremiend(b.entity_id,coalesce(a.nd_disc1*a.ccy_rate,0),(coalesce(b.disc_amount*a.ccy_rate,0))*-1)) as diskon "
                    + "from ins_policies a "
                    + "inner join ins_pol_coins b on b.policy_id = a.pol_id "
                    + "left join ent_master e on e.ent_id = a.entity_id "
                    + "where (b.entity_id <> 1 or b.coins_type <> 'COINS_COVER') and a.status in ('POLICY','RENEWAL','ENDORSE') "
                    + "and a.active_flag='Y' and a.effective_flag='Y' "
                    + "and substr(a.approved_date::text,1,4) = '" + posting.getStYears() + "' "
                    + "and substr(a.approved_date::text,6,2) = '" + posting.getStMonths() + "' "
                    + "group by 1,2,3,4,5,6,7 order by 1,2,3,5,6 ) a order by 1,2,3,5,6 ";

            PreparedStatement PS = S.setQuery(proses2);

            int i = PS.executeUpdate();
        } finally {
            S.release();
        }
    }

    public void clickRekapKlaim(GLPostingView posting) throws Exception {

        final SQLUtil S = new SQLUtil();

        try {

            final String proses1 = " DELETE from ins_rekap_klaim where tahun =  '" + posting.getStYears() + "' and bulan = '" + posting.getStMonths() + "'";

            PreparedStatement PS1 = S.setQuery(proses1);
            logger.logDebug("####################### " + proses1);

            final String proses2 = " INSERT INTO ins_rekap_klaim(tahun,bulan,cc_code,region_id,ins_policy_type_grp_id,pol_type_id,sumbis,klaim,klaim_bruto,subrogasi,feerecovery) "
                    + "select tahun,bulan,cc_code,region_id,ins_policy_type_grp_id,pol_type_id,sumbis,sum(klaim) as klaim,sum(Klaimbruto) as Klaimbruto,sum(Subrogasi) as Subrogasi,sum(FeeRecovery) as FeeRecovery "
                    + "from ( select substr(a.claim_approved_date::text,1,4) as tahun,substr(a.claim_approved_date::text,6,2) as bulan,a.dla_no,a.cc_code,a.region_id,a.ins_policy_type_grp_id,a.pol_type_id,"
                    + "e.category1 as sumbis,(a.claim_amount*a.ccy_rate_claim) as klaim,sum(checkreas(c.ins_item_id in (46),(c.amount*a.ccy_rate_claim))) as Klaimbruto,"
                    + "sum(checkreas(c.ins_item_id in (48),(c.amount*a.ccy_rate_claim))) as Subrogasi,sum(checkreas(c.ins_item_id in (73),(c.amount*a.ccy_rate_claim))) as FeeRecovery "
                    + "from ins_policy a inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id "
                    + "inner join ins_pol_items c on c.pol_id = a.pol_id and c.item_class = 'CLM' "
                    + "left join ent_master e on e.ent_id = a.entity_id "
                    + "where a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA' "
                    + "and a.active_flag='Y' and a.effective_flag='Y' "
                    + "and substr(a.claim_approved_date::text,1,4) = '" + posting.getStYears() + "' "
                    + "and substr(a.claim_approved_date::text,6,2) = '" + posting.getStMonths() + "' "
                    + "group by 1,2,3,4,5,6,7,8,9 order by 1,2,3,5,6 "
                    + ") a group by 1,2,3,4,5,6,7 order by 1,2,3,5,6 ";

//                                + "select tahun,bulan,cc_code,region_id,ins_policy_type_grp_id,pol_type_id,sumbis,claim_amount "
//                                + "from ( select substr(a.claim_approved_date::text,1,4) as tahun, "
//                                + "substr(a.claim_approved_date::text,6,2) as bulan,"
//                                + "a.cc_code,a.region_id,a.ins_policy_type_grp_id,a.pol_type_id,e.category1 as sumbis,"
//                                + "sum(coalesce((a.claim_amount*a.ccy_rate_claim),0)) as claim_amount "
//                                + "from ins_policy a "
//                                + "left join ent_master e on e.ent_id = a.entity_id "
//                                + "where a.status in ('CLAIM','CLAIM ENDORSE')  and  a.claim_status = 'DLA' "
//                                + "and a.active_flag='Y' and a.effective_flag='Y' "
//                                + "and substr(a.claim_approved_date::text,1,4) = '" + posting.getStYears() + "' "
//                                + "and substr(a.claim_approved_date::text,6,2) = '" + posting.getStMonths() + "' "
//                                + "group by 1,2,3,4,5,6,7 order by 1,2,3,5,6 ) a order by 1,2,3,5,6 ";

            PreparedStatement PS = S.setQuery(proses2);
            logger.logDebug("####################### " + proses2);

            int i = PS.executeUpdate();
        } finally {
            S.release();
        }
    }
}
