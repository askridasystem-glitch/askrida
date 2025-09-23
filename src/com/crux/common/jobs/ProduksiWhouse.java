/***********************************************************************
 * Module:  com.crux.common.jobs.ProduksiWhouse
 * Author:  Denny Mahendra
 * Created: Jul 12, 2004 12:24:48 PM
 * Purpose:
 ***********************************************************************/
package com.crux.common.jobs;

import com.crux.common.parameter.Parameter;
import com.crux.jobs.util.JobUtil;
import org.quartz.StatefulJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.crux.util.LogManager;
import com.crux.util.ConnectionCache;
import com.crux.util.DTOList;
import com.crux.util.DateUtil;
import com.crux.util.ListUtil;
import com.crux.util.MailUtil2;
import com.crux.util.SQLUtil;
import com.webfin.ar.model.ARInvoiceView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Date;

public class ProduksiWhouse implements StatefulJob {

    private final static transient LogManager logger = LogManager.getInstance(ProduksiWhouse.class);

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

            final DTOList file = ListUtil.getDTOListFromQueryDS(
                    " select a.pol_id,a.tsi_obj_riil,b.tsi from ( "
                    + "select pol_id,';'||pol_no as pol_no,sum(tsi_obj_riil) as tsi_obj_riil,sum(premi_obj_riil) as premi_obj_riil,"
                    + "sum(tsi_obj_rev) as tsi_obj_rev,sum(premi_obj_rev) as premi_obj_rev,sum(disc_obj) as disc_obj,sum(tsiko_obj) as tsiko_obj,"
                    + "sum(premiko_obj) as premiko_obj,sum(diskonko_obj) as diskonko_obj,sum(komisi_obj) as komisi_obj,sum(bfee_obj) as bfee_obj,"
                    + "sum(hfee_obj) as hfee_obj,sum(fbase_obj) as fbase_obj,sum(ppn_obj) as ppn_obj,sum(commko_obj) as commko_obj,sum(bfeeko_obj) as bfeeko_obj,"
                    + "sum(hfeeko_obj) as hfeeko_obj from data_debitur a "
                    + "where date_trunc('day',a.policy_date) >= '2023-06-01 00:00:00' "
                    + "and date_trunc('day', a.policy_date) <= '2023-06-30 00:00:00' "
                    + "group by pol_id,pol_no order by pol_no ) a left join ( "
                    + "select pol_id,';'||pol_no as pol_no,a.tsi,a.premi_total,a.diskon,a.tsi_ko,a.premi_ko,a.diskon_ko,"
                    + "a.komisi,a.bfee,a.hfee,a.fbase,a.ppn,a.komisi_ko,a.bfee_ko,a.hfee_ko from data_polis a "
                    + "where date_trunc('day',a.policy_date) >= '2023-06-01 00:00:00' "
                    + "and date_trunc('day', a.policy_date) <= '2023-06-30 00:00:00' "
                    + "group by pol_id,pol_no order by pol_no ) b on b.pol_id = a.pol_id "
                    + "where a.tsi_obj_riil <> b.tsi order by a.pol_id ",
                    ARInvoiceView.class, "WHouseDS");

            logger.logDebug("################### : " + file.size());

            for (int i = 0; i < file.size(); i++) {
                ARInvoiceView inv = (ARInvoiceView) file.get(i);

                String receiver = Parameter.readString("FINANCE_EMAIL");
//                String receiver = "prasetyo@askrida.co.id";
                String subject = "Pembayaran Premi Polis Facultatif";
                String text = "Dengan hormat,\n\n"
                        + "Bersama ini kami infokan bahwa polis " + inv.getStInvoiceNo() + " telah dibayar premi Nobuk " + inv.getStReceiptNo() + " dan polis terdapat Facultatif.\n"
                        + "Atas perhatian dan kerjasamanya, kami ucapkan terima kasih.\n\n\n"
                        + "Hormat kami,\n"
                        + "Administrator";

                MailUtil2 mail = new MailUtil2();
                mail.sendEmail(receiver, subject, text);

                updateFlagPosting(inv);

            }

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 2 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }
    }

    public void updateFlagPosting(ARInvoiceView inv) throws Exception {

        final SQLUtil S = new SQLUtil();
        try {

            PreparedStatement PS = S.setQuery("update ar_invoice set notif_flag = 'Y' where ar_invoice_id = ? ");

            PS.setObject(1, inv.getStARInvoiceID());

            int hasil = PS.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            S.release();
        }

    }
}
