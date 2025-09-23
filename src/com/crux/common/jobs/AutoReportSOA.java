/***********************************************************************
 * Module:  com.crux.common.jobs.AutoReporting
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
import com.crux.util.DateUtil;
import com.webfin.gl.ejb.PeriodManager;
import com.webfin.gl.model.PeriodDetailView;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;
import java.util.Calendar;

public class AutoReportSOA extends Helper implements StatefulJob {

    private final static transient LogManager logger = LogManager.getInstance(AutoReportSOA.class);

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

        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int hourAuto = 21;

        if (hourAuto == hour) {
            try {
                logger.logInfo("execute: performing proses delete konversi file 2");

                long t = System.currentTimeMillis();

                String policyDateStart = null;
                String policyDateEnd = null;

                int tahunCodeLast;
                int bulanCode = DateUtil.getMonthDigit(new Date());

                if (bulanCode == 1) {
                    tahunCodeLast = Integer.parseInt(DateUtil.getYear(new Date())) - 1;

                    policyDateStart = tahunCodeLast + "-12-01 00:00:00";
                    policyDateEnd = tahunCodeLast + "-12-31 00:00:00";
                } else if (bulanCode > 1) {
                    int bulanCodeNow = DateUtil.getMonthDigit(new Date()) - 1;

                    PeriodDetailView pd = PeriodManager.getInstance().getPeriod(String.valueOf(bulanCodeNow), DateUtil.getYear(new Date()));
                    Date dateStart = pd.getDtStartDate();
                    Date dateEnd = pd.getDtEndDate();

                    policyDateStart = dateStart.toString();
                    policyDateEnd = dateEnd.toString();
                }

                final Statement S = conn.createStatement();

                final String proses1 = " INSERT INTO s_report_for_soa( "
                        + "bulan, tahun, ins_policy_type_grp_id, pol_type_id, "
                        + "ccy, ccy_rate, ccy_rate_treaty, pol_no, ins_pol_obj_id, "
                        + "description, treaty_type, ent_id, period_start, period_end, "
                        + "premi_netto, nd_comm1, claim_amount, "
                        + "desc_soa, premi_rate_soa, comm_rate_soa ) "
                        + "select a.*,b.desc_soa,b.premi_rate_soa,b.comm_rate_soa "
                        + "from ( "
                        + "select substr(getperiod(a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI'),a.policy_date,a.claim_approved_date)::text,6,2) as bulan,"
                        + "substr(getperiod(a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI'),a.policy_date,a.claim_approved_date)::text,1,4) as tahun,"
                        + "a.ins_policy_type_grp_id,a.pol_type_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,a.pol_no,c.ins_pol_obj_id,c.description,j.treaty_type,k.ent_id,"
                        + "(getperiod(a.pol_type_id in (4,21,59),c.refd2,getperiod(a.pol_type_id in (1,3,24,81),c.refd1,a.period_start))) as period_start,"
                        + "(getperiod(a.pol_type_id in (4,21,59),c.refd3,getperiod(a.pol_type_id in (1,3,24,81),c.refd2,a.period_end))) as period_end,  "
                        + "sum(getpremi2(a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI') "
                        + "and date_trunc('day',a.policy_date) >= '" + policyDateStart + "' "
                        + "and date_trunc('day',a.policy_date) <= '" + policyDateEnd + "' ,(i.premi_amount*a.ccy_rate))) as premi_netto,"
                        + "sum(getpremi2(a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI') "
                        + "and date_trunc('day',a.policy_date) >= '" + policyDateStart + "' "
                        + "and date_trunc('day',a.policy_date) <= '" + policyDateEnd + "' ,(i.ricomm_amt*a.ccy_rate))) as nd_comm1,"
                        + "sum(getpremi2(a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA' "
                        + "and date_trunc('day',a.claim_approved_date) >= '" + policyDateStart + "' "
                        + "and date_trunc('day',a.claim_approved_date) <= '" + policyDateEnd + "' ,(i.claim_amount*a.ccy_rate_claim))) as claim_amount "
                        + "from ins_policy a "
                        + "inner join ins_pol_obj c on c.pol_id = a.pol_id "
                        + "inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
                        + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
                        + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                        + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                        + "inner join ent_master k on k.ent_id = i.member_ent_id "
                        + "where ((a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI') "
                        + "and date_trunc('day',a.policy_date) >= '" + policyDateStart + "' "
                        + "and date_trunc('day',a.policy_date) <= '" + policyDateEnd + "' ) or "
                        + "(a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA' "
                        + "and date_trunc('day',a.claim_approved_date) >= '" + policyDateStart + "' "
                        + "and date_trunc('day',a.claim_approved_date) <= '" + policyDateEnd + "')) "
                        + "and j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5') "
                        + "and a.active_flag='Y' and a.effective_flag='Y' "
                        + "group by j.treaty_type,a.ins_policy_type_grp_id,a.pol_type_id,a.ccy,a.ccy_rate,a.ccy_rate_treaty,k.short_name,"
                        + "c.refd1,c.refd2,c.refd3,a.period_start,a.period_end,k.ent_id,a.pol_no,c.ins_pol_obj_id,c.description,a.status,"
                        + "a.policy_date,a.claim_approved_date order by a.ins_policy_type_grp_id,a.pol_type_id,a.pol_no,j.treaty_type,k.ent_id "
                        + ") a inner join s_poltype_soa b on a.pol_type_id = b.pol_type_id and b.years = substr(a.period_start::text,1,4) "
                        + "where (a.premi_netto <> 0 or a.nd_comm1 <> 0 or a.claim_amount <> 0) " //and b.desc_soa <> 'UNCLASSED' "
                        + "order by a.ins_policy_type_grp_id,a.pol_type_id,a.pol_no,a.treaty_type,a.ent_id ";

//            final String proses1 = " INSERT INTO s_report_for_soa ("
//                    + "bulan, tahun, ins_policy_type_grp_id, pol_type_id, desc_soa, "
//                    + "premi_rate_soa, comm_rate_soa, ccy, ccy_rate, ccy_rate_treaty, "
//                    + "pol_no, ins_pol_obj_id, description, treaty_type, period_start, "
//                    + "period_end, ent_id, premi_netto, nd_comm1, claim_amount) "
//                    + "select a.* from ( "
//                    + "select substr(getperiod(a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI'),a.policy_date,a.claim_approved_date)::text,6,2) as bulan,"
//                    + "substr(getperiod(a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI'),a.policy_date,a.claim_approved_date)::text,1,4) as tahun,"
//                    + "f.ins_policy_type_grp_id,a.pol_type_id,f.desc_soa,f.premi_rate_soa,f.comm_rate_soa,a.ccy,a.ccy_rate,a.ccy_rate_treaty,"
//                    + "a.pol_no,c.ins_pol_obj_id,c.description,j.treaty_type,"
//                    + "(getperiod(a.pol_type_id in (4,21,59),c.refd2,getperiod(a.pol_type_id in (1,3,24,81),c.refd1,a.period_start))) as period_start,"
//                    + "(getperiod(a.pol_type_id in (4,21,59),c.refd3,getperiod(a.pol_type_id in (1,3,24,81),c.refd2,a.period_end))) as period_end, "
//                    + "k.ent_id,sum(getpremi2(a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI') "
//                    + "and date_trunc('day',a.policy_date) >= '" + policyDateStart + "' "
//                    + "and date_trunc('day',a.policy_date) <= '" + policyDateEnd + "' ,(i.premi_amount*a.ccy_rate))) as premi_netto,"
//                    + "sum(getpremi2(a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI') "
//                    + "and date_trunc('day',a.policy_date) >= '" + policyDateStart + "' "
//                    + "and date_trunc('day',a.policy_date) <= '" + policyDateEnd + "' ,(i.ricomm_amt*a.ccy_rate))) as nd_comm1,"
//                    + "sum(getpremi2(a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA' "
//                    + "and date_trunc('day',a.claim_approved_date) >= '" + policyDateStart + "' "
//                    + "and date_trunc('day',a.claim_approved_date) <= '" + policyDateEnd + "' ,(i.claim_amount*a.ccy_rate_claim))) as claim_amount "
//                    + "from ins_policy a "
//                    + "inner join ins_pol_obj c on c.pol_id = a.pol_id "
//                    + "inner join ins_policy_types f on a.pol_type_id = f.pol_type_id "
//                    + "inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "
//                    + "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "
//                    + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
//                    + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
//                    + "inner join ent_master k on k.ent_id = i.member_ent_id "
//                    + "where ((a.status IN ('POLICY','ENDORSE','RENEWAL','ENDORSE RI') "
//                    + "and date_trunc('day',a.policy_date) >= '" + policyDateStart + "' "
//                    + "and date_trunc('day',a.policy_date) <= '" + policyDateEnd + "' ) or "
//                    + "(a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA' "
//                    + "and date_trunc('day',a.claim_approved_date) >= '" + policyDateStart + "' "
//                    + "and date_trunc('day',a.claim_approved_date) <= '" + policyDateEnd + "')) "
//                    + "and date_trunc('day',a.policy_date) >= '" + policyDateStart + "' "
//                    + "and date_trunc('day',a.policy_date) <= '" + policyDateEnd + "' "
//                    + "and j.treaty_type not in ('OR','XOL1','XOL2','XOL3','XOL4','XOL5') "
//                    + "and a.active_flag='Y' and a.effective_flag='Y' and f.desc_soa <> 'UNCLASSED' "
//                    + "group by j.treaty_type,f.ins_policy_type_grp_id,a.pol_type_id,f.desc_soa,a.ccy,a.ccy_rate,a.ccy_rate_treaty,"
//                    + "k.short_name,c.refd1,c.refd2,c.refd3,a.period_start,a.period_end,k.ent_id,a.pol_no,f.premi_rate_soa,f.comm_rate_soa,"
//                    + "c.ins_pol_obj_id,c.description,a.status,a.policy_date,a.claim_approved_date "
//                    + "order by f.ins_policy_type_grp_id,a.pol_type_id,j.treaty_type,k.ent_id "
//                    + ") a where (a.premi_netto <> 0 or a.nd_comm1 <> 0 or claim_amount <> 0) "
//                    + "order by a.ins_policy_type_grp_id,a.pol_type_id,a.pol_no,a.treaty_type,a.ent_id; ";

                final boolean b = S.execute(proses1);

                logger.logDebug("####################### " + proses1);

                S.close();

                t = System.currentTimeMillis() - t;

                logger.logInfo("proses 2 selesai dalam " + t + " ms");

            } finally {
                conn.close();
            }
        }
    }
}
