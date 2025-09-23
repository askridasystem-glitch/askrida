/***********************************************************************
 * Module:  com.crux.common.jobs.AutoClosingProduksi
 * Author:  Denny Mahendra
 * Created: Jul 12, 2004 12:24:48 PM
 * Purpose:
 ***********************************************************************/
package com.crux.common.jobs;

import com.crux.common.controller.Helper;
import com.crux.jobs.util.JobUtil;
import org.quartz.StatefulJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.crux.util.LogManager;
import com.crux.util.ConnectionCache;
import com.crux.util.DTOList;
import com.crux.util.DateUtil;
import com.crux.util.IDFactory;
import com.crux.util.ListUtil;
import com.crux.util.Tools;
import com.webfin.gl.ejb.PeriodManager;
import com.webfin.gl.model.GLPostingView;
import com.webfin.gl.model.PeriodDetailView;
import com.webfin.insurance.model.InsurancePostingView;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;
import java.util.Calendar;

public class ClosingProduksi extends Helper implements StatefulJob {

    private final static transient LogManager logger = LogManager.getInstance(ClosingProduksi.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {

            if (JobUtil.isServerProduction()) {
                Calendar now = Calendar.getInstance();
                int date = now.get(Calendar.DAY_OF_MONTH);
                int dateAuto = 1;
                int hour = now.get(Calendar.HOUR_OF_DAY);
                int hourAuto = 6;

                if (dateAuto == date) {
                    if (hourAuto == hour) {
//                execute1();
//                execute2();
//                execute3();
//                execute4();

                        executePremi();
                        executeKlaim();
                    }
                }
            }
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    public void execute1() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {

            final Statement S = conn.createStatement();
            logger.logInfo("execute: performing proses delete konversi file 2");

            long t = System.currentTimeMillis();

            String bulan = null;
            String tahun = null;

            int tahunCodeLast;
            int bulanCode = DateUtil.getMonthDigit(new Date());

            if (bulanCode == 1) {
                tahunCodeLast = Integer.parseInt(DateUtil.getYear(new Date())) - 1;

                bulan = "12";
                tahun = Integer.toString(tahunCodeLast);

            } else if (bulanCode > 1) {
                int bulanCodeNow = DateUtil.getMonthDigit(new Date()) - 1;

                PeriodDetailView pd = PeriodManager.getInstance().getPeriod(String.valueOf(bulanCodeNow), DateUtil.getYear(new Date()));
                Date dateStart = pd.getDtStartDate();
                Date dateEnd = pd.getDtEndDate();

                bulan = DateUtil.getMonth2Digit(dateStart);
                tahun = DateUtil.getYear(dateStart);
            }

            final String cekproses = " select * from ins_closing where years =  '" + tahun + "' and months = '" + bulan + "'";

            final DTOList file = ListUtil.getDTOListFromQuery(
                    cekproses,
                    InsurancePostingView.class);

            logger.logDebug("################### : " + file.size());

            if (file.size() < 1) {
                String noUrut = String.valueOf(IDFactory.createNumericID("INSCLOSINGPRODUKSIID"));

//                    final String proses1 = " INSERT INTO ins_closing( "
//                            + "gl_post_id, months, years, create_who, "
//                            + "create_date, posted_flag ) "
//                            + " values (" + noUrut + ",'" + bulan + "','" + tahun + "','admin',now(),'Y') ";

                final String proses1 = " INSERT INTO ins_closing( "
                        + "gl_post_id, months, years, create_who,create_date) "
                        + " values (" + noUrut + ",'" + bulan + "','" + tahun + "','admin',now()) ";

                final boolean b = S.execute(proses1);
                logger.logDebug("####################### " + proses1);

                S.close();

                t = System.currentTimeMillis() - t;

                logger.logInfo("proses 2 selesai dalam " + t + " ms");
            }
        } finally {
            conn.close();
        }
    }

    public void execute2() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            final Statement S = conn.createStatement();
            logger.logInfo("execute: performing proses delete konversi file 2");

            long t = System.currentTimeMillis();

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

                    final String proses1 = " DELETE from ins_rekap_produksi where tahun =  '" + posting.getStYears() + "' and bulan = '" + posting.getStMonths() + "'";

                    final boolean b = S.execute(proses1);
                    logger.logDebug("####################### " + proses1);

                    final String proses2 = " INSERT INTO ins_rekap_produksi(tahun,bulan,cc_code,region_id,cc_code_source,region_id_source,ins_policy_type_grp_id,pol_type_id,sumbis,premi,tsi,preto,diskon,komisi,bfee,hfee,feebase,ppn) "
                            + "select tahun,bulan,cc_code,region_id,cc_code_source,region_id_source,ins_policy_type_grp_id,pol_type_id,sumbis,sum(premi-diskon) as premi,sum(tsi) as tsi,sum(premi) as preto,"
                            + "sum(diskon) as diskon,sum(comm1) as komisi,sum(bfee) as bfee,sum(hfee) as hfee,sum(feebase) as feebase,sum(ppn) as ppn "
                            + "from ( select substr(a.approved_date::text,1,4) as tahun, substr(a.approved_date::text,6,2) as bulan,"
                            + "a.cc_code,a.region_id,a.cc_code_source,a.region_id_source,a.ins_policy_type_grp_id,a.pol_type_id,e.category1 as sumbis,"
                            + "sum(getpremiend(b.entity_id,coalesce(a.insured_amount*a.ccy_rate,0),coalesce(b.amount*a.ccy_rate,0)*-1)) as tsi, "
                            + "sum(getpremiend(b.entity_id,coalesce(a.premi_total*a.ccy_rate,0),coalesce(b.premi_amt*a.ccy_rate,0)*-1)) as premi,"
                            + "sum(getpremiend(b.entity_id,coalesce(a.nd_disc1*a.ccy_rate,0),(coalesce(b.disc_amount*a.ccy_rate,0))*-1)) as diskon,"
                            + "sum(getpremiend(b.entity_id,coalesce(a.nd_comm1*a.ccy_rate,0),(coalesce(b.comm_amount*a.ccy_rate,0))*-1)) as comm1, "
                            + "sum(getpremiend(b.entity_id,coalesce(a.nd_brok1*a.ccy_rate,0),(coalesce(b.broker_amount*a.ccy_rate,0))*-1)) as bfee,"
                            + "sum(getpremiend(b.entity_id,coalesce(a.nd_hfee*a.ccy_rate,0),coalesce(b.hfee_amount*a.ccy_rate,0)*-1)) as hfee,  "
                            + "sum(getpremiend(b.entity_id,coalesce(a.nd_feebase1*a.ccy_rate,0),0)) as feebase,"
                            + "sum(getpremiend(b.entity_id,coalesce(a.nd_ppn*a.ccy_rate,0),0)) as ppn "
                            + "from ins_policies a "
                            + "inner join ins_pol_coins b on b.policy_id = a.pol_id "
                            + "left join ent_master e on e.ent_id = a.entity_id "
                            + "where (b.entity_id <> 1 or b.coins_type <> 'COINS_COVER') and a.status in ('POLICY','RENEWAL','ENDORSE') "
                            + "and a.active_flag='Y' and a.effective_flag='Y' "
                            + "and substr(a.approved_date::text,1,4) = '" + posting.getStYears() + "' "
                            + "and substr(a.approved_date::text,6,2) = '" + posting.getStMonths() + "' "
                            + "group by 1,2,3,4,5,6,7,8,9 order by 1,2,3,5,6,7,8 ) a group by 1,2,3,4,5,6,7,8,9 order by 1,2,3,5,7 ";

                    final boolean c = S.execute(proses2);
                    logger.logDebug("####################### " + proses2);

                }
            }

            S.close();

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 2 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }
    }

    public void execute3() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            final Statement S = conn.createStatement();
            logger.logInfo("execute: performing proses delete konversi file 2");

            long t = System.currentTimeMillis();

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

                if (Tools.isNo(posting.getStPostedFlag())) {

                    final String proses1 = " DELETE from ins_rekap_klaim where tahun =  '" + posting.getStYears() + "' and bulan = '" + posting.getStMonths() + "'";

                    final boolean b = S.execute(proses1);
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

                    final boolean c = S.execute(proses2);
                    logger.logDebug("####################### " + proses2);

                }
            }

            S.close();

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 2 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }
    }

    public void execute4() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            final Statement S = conn.createStatement();
            logger.logInfo("execute: performing proses delete konversi file 2");

            long t = System.currentTimeMillis();

            final DTOList file = ListUtil.getDTOListFromQuery(
                    " select * from ins_closing order by gl_post_id desc limit 1 ",
                    InsurancePostingView.class);

            logger.logDebug("################### : " + file.size());

            for (int i = 0; i < file.size(); i++) {
                InsurancePostingView posting = (InsurancePostingView) file.get(i);

                if (Tools.isYes(posting.getStPostedFlag())) {
                    continue;
                }

                if (Tools.isNo(posting.getStPostedFlag())) {

                    final String proses1 = " DELETE from ins_rekap_produksi_ri where tahun =  '" + posting.getStYears() + "' and bulan = '" + posting.getStMonths() + "'";

                    final boolean b = S.execute(proses1);
                    logger.logDebug("####################### " + proses1);

                    final String proses2 = " INSERT INTO ins_rekap_produksi_ri(tahun,bulan,cc_code,region_id,ins_policy_type_grp_id,pol_type_id,effective_flag,"
                            + "category1,approved_date,status,pol_id,pol_no,ins_pol_obj_id,cust_name,ccy,ccy_rate,entity_id,prod_id,cover_type_code,description,"
                            + "ref1,tgl_lahir,period_start,period_end,insured_amount_obj,premi_total_obj,treaty_type,ins_pol_ri_id,member_ent_id,tsi_amount,"
                            + "premi_amount,ricomm_amt,endorse_notes,total_komisi_pct,total_komisi) "
                            + "select substr(a.approved_date::text,1,4) as tahun, substr(a.approved_date::text,6,2) as bulan,"
                            + "a.cc_code,a.region_id,a.ins_policy_type_grp_id,a.pol_type_id,a.effective_flag,c.category1,"
                            + "a.approved_date,a.status,a.pol_id,a.pol_no,b.ins_pol_obj_id,"
                            + "a.cust_name,a.ccy,a.ccy_rate,a.entity_id,a.prod_id,a.cover_type_code,b.description,b.ref1,"
                            + "(getperiod(a.pol_type_id in (4,21,59),b.refd1,null)) as tgl_lahir,"
                            + "(getperiod(a.pol_type_id in (4,21,59),b.refd2,getperiod(a.pol_type_id in (1,3,24,81),b.refd1,a.period_start))) as period_start,"
                            + "(getperiod(a.pol_type_id in (4,21,59),b.refd3,getperiod(a.pol_type_id in (1,3,24,81),b.refd2,a.period_end))) as period_end, "
                            + "b.insured_amount as insured_amount_obj,b.premi_total as premi_total_obj,"
                            + "j.treaty_type,i.ins_pol_ri_id,i.member_ent_id,i.tsi_amount,i.premi_amount,i.ricomm_amt,a.endorse_notes, "
                            + "coalesce((SELECT SUM(rate)  FROM INS_POL_ITEMS y  WHERE y.pol_id = a.pol_id "
                            + "and y.ins_item_id in (select ins_item_id from ins_items where item_type in ('COMIS'))),0) as total_komisi_pct, "
                            + "coalesce((SELECT SUM(amount)  FROM INS_POL_ITEMS y  WHERE y.pol_id = a.pol_id "
                            + "and y.ins_item_id in (select ins_item_id from ins_items where item_type in ('COMIS'))),0) as total_komisi "
                            + "from ins_policy a "
                            + "inner join ins_pol_obj b on b.pol_id = a.pol_id "
                            + "inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                            + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                            + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                            + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = i.ins_treaty_detail_id "
                            + "where a.active_flag = 'Y' and a.effective_flag = 'Y' "
                            + "and a.status IN ('POLICY','RENEWAL','ENDORSE') "
                            + "and substr(a.approved_date::text,1,4) = '" + posting.getStYears() + "' "
                            + "and substr(a.approved_date::text,6,2) = '" + posting.getStMonths() + "' "
                            //                                + "and substr(a.approved_date::text,1,4) = '2017' "
                            //                                + "and substr(a.approved_date::text,6,2) = '12' "
                            + "and (b.insured_amount <> 0 or b.premi_total <> 0) "
                            + "order by a.approved_date,a.pol_no,b.ins_pol_obj_id,h.ins_pol_tre_det_id,i.member_ent_id ";

                    final boolean c = S.execute(proses2);
                    logger.logDebug("####################### " + proses2);

                }
            }

            S.close();

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 2 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }
    }

    public void executePremi() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            final Statement S = conn.createStatement();
            logger.logInfo("execute: performing proses delete konversi file 2");

            long t = System.currentTimeMillis();

            final DTOList file = ListUtil.getDTOListFromQuery(
                    " select tahun as years,bulan as months from ins_rekap_produksi "
                    + "group by 1,2 order by 1 desc,2 desc limit 1",
                    InsurancePostingView.class);

            logger.logDebug("################### : " + file.size());

            for (int i = 0; i < file.size(); i++) {
                InsurancePostingView posting = (InsurancePostingView) file.get(i);

                int bulanNow = Integer.parseInt(posting.getStMonths()); //8
                int tahunNow = Integer.parseInt(posting.getStYears()); //2022
                String bulan = null;
                String tahun = null;

                if (bulanNow == 12) {
                    bulan = "01";
                    tahun = String.valueOf(tahunNow + 1);
                } else if (bulanNow < 12) {
                    bulanNow = bulanNow + 1;
                    if (bulanNow < 10) {
                        bulan = "0" + bulanNow;
                    } else {
                        bulan = String.valueOf(bulanNow);
                    }
                    tahun = String.valueOf(tahunNow);
                }

                final String proses = " INSERT INTO ins_rekap_produksi(tahun,bulan,cc_code,region_id,cc_code_source,region_id_source,ins_policy_type_grp_id,pol_type_id,sumbis,premi,tsi,preto,diskon,komisi,bfee,hfee,feebase,ppn) "
                        + "select tahun,bulan,cc_code,region_id,cc_code_source,region_id_source,ins_policy_type_grp_id,pol_type_id,sumbis,"
                        + "sum(premi-diskon) as premi,sum(tsi) as tsi,sum(premi) as preto,sum(diskon) as diskon,sum(comm1) as komisi,"
                        + "sum(bfee) as bfee,sum(hfee) as hfee,sum(feebase) as feebase,sum(ppn) as ppn "
                        + "from ( select substr(a.approved_date::text,1,4) as tahun, "
                        + "substr(a.approved_date::text,6,2) as bulan,"
                        + "a.cc_code,a.region_id,a.cc_code_source,a.region_id_source,a.ins_policy_type_grp_id,a.pol_type_id,e.category1 as sumbis,"
                        + "sum(getpremiend(b.entity_id,coalesce(a.insured_amount*a.ccy_rate,0),coalesce(b.amount*a.ccy_rate,0)*-1)) as tsi, "
                        + "sum(getpremiend(b.entity_id,coalesce(a.premi_total*a.ccy_rate,0),coalesce(b.premi_amt*a.ccy_rate,0)*-1)) as premi,"
                        + "sum(getpremiend(b.entity_id,coalesce(a.nd_disc1*a.ccy_rate,0),(coalesce(b.disc_amount*a.ccy_rate,0))*-1)) as diskon,"
                        + "sum(getpremiend(b.entity_id,coalesce(a.nd_comm1*a.ccy_rate,0),(coalesce(b.comm_amount*a.ccy_rate,0))*-1)) as comm1, "
                        + "sum(getpremiend(b.entity_id,coalesce(a.nd_brok1*a.ccy_rate,0),(coalesce(b.broker_amount*a.ccy_rate,0))*-1)) as bfee,"
                        + "sum(getpremiend(b.entity_id,coalesce(a.nd_hfee*a.ccy_rate,0),coalesce(b.hfee_amount*a.ccy_rate,0)*-1)) as hfee,  "
                        + "sum(getpremiend(b.entity_id,coalesce(a.nd_feebase1*a.ccy_rate,0),0)) as feebase,"
                        + "sum(getpremiend(b.entity_id,coalesce(a.nd_ppn*a.ccy_rate,0),0)) as ppn "
                        + "from ins_policies a "
                        + "inner join ins_pol_coins b on b.policy_id = a.pol_id "
                        + "left join ent_master e on e.ent_id = a.entity_id "
                        + "where (b.entity_id <> 1 or b.coins_type <> 'COINS_COVER') and a.status in ('POLICY','RENEWAL','ENDORSE') "
                        + "and a.active_flag='Y' and a.effective_flag='Y' "
                        + "and substr(a.approved_date::text,1,4) = '" + tahun + "' "
                        + "and substr(a.approved_date::text,6,2) = '" + bulan + "' "
                        + "group by 1,2,3,4,5,6,7,8,9 order by 1,2,3,5,6 ) a group by 1,2,3,4,5,6,7,8,9 order by 1,2,3,5,6 ";

                final boolean c = S.execute(proses);
                logger.logDebug("####################### " + proses);
            }

            S.close();

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 2 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }
    }

    public void executeKlaim() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            final Statement S = conn.createStatement();
            logger.logInfo("execute: performing proses delete konversi file 2");

            long t = System.currentTimeMillis();

            final DTOList file = ListUtil.getDTOListFromQuery(
                    " select tahun as years,bulan as months from ins_rekap_klaim "
                    + "group by 1,2 order by 1 desc,2 desc limit 1",
                    InsurancePostingView.class);

            logger.logDebug("################### : " + file.size());

            for (int i = 0; i < file.size(); i++) {
                InsurancePostingView posting = (InsurancePostingView) file.get(i);

                int bulanNow = Integer.parseInt(posting.getStMonths()); //12
                int tahunNow = Integer.parseInt(posting.getStYears()); //2022
                String bulan = null;
                String tahun = null;

                if (bulanNow == 12) {
                    bulan = "01";
                    tahun = String.valueOf(tahunNow + 1);
                } else if (bulanNow < 12) {
                    bulanNow = bulanNow + 1;
                    if (bulanNow < 10) {
                        bulan = "0" + bulanNow;
                    } else {
                        bulan = String.valueOf(bulanNow);
                    }
                    tahun = String.valueOf(tahunNow);
                }

                final String proses2 = " INSERT INTO ins_rekap_klaim(tahun,bulan,cc_code,region_id,cc_code_source,region_id_source,ins_policy_type_grp_id,pol_type_id,sumbis,klaim,klaim_bruto,subrogasi,feerecovery) "
                        + "select tahun,bulan,cc_code,region_id,cc_code_source,region_id_source,ins_policy_type_grp_id,pol_type_id,sumbis,sum(klaim) as klaim,sum(Klaimbruto) as Klaimbruto,sum(Subrogasi) as Subrogasi,sum(FeeRecovery) as FeeRecovery "
                        + "from ( select substr(a.claim_approved_date::text,1,4) as tahun,substr(a.claim_approved_date::text,6,2) as bulan,a.dla_no,a.cc_code,a.region_id,a.cc_code_source,a.region_id_source,a.ins_policy_type_grp_id,a.pol_type_id,"
                        + "e.category1 as sumbis,(a.claim_amount*a.ccy_rate_claim) as klaim,sum(checkreas(c.ins_item_id in (46),(c.amount*a.ccy_rate_claim))) as Klaimbruto,"
                        + "sum(checkreas(c.ins_item_id in (48),(c.amount*a.ccy_rate_claim))) as Subrogasi,sum(checkreas(c.ins_item_id in (73),(c.amount*a.ccy_rate_claim))) as FeeRecovery "
                        + "from ins_policy a inner join ins_pol_obj b on b.ins_pol_obj_id = a.claim_object_id "
                        + "inner join ins_pol_items c on c.pol_id = a.pol_id and c.item_class = 'CLM' "
                        + "left join ent_master e on e.ent_id = a.entity_id "
                        + "where a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA' "
                        + "and a.active_flag='Y' and a.effective_flag='Y' "
                        + "and substr(a.claim_approved_date::text,1,4) = '" + tahun + "' "
                        + "and substr(a.claim_approved_date::text,6,2) = '" + bulan + "' "
                        + "group by 1,2,3,4,5,6,7,8,9,10,11 order by 1,2,3,5,6 "
                        + ") a group by 1,2,3,4,5,6,7,8,9 order by 1,2,3,5,6 ";

                final boolean c = S.execute(proses2);
                logger.logDebug("####################### " + proses2);
            }

            S.close();

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 2 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }
    }
}
