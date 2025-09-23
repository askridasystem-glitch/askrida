/***********************************************************************
 * Module:  com.crux.common.jobs.ValidateReceipt
 * Author:  Denny Mahendra
 * Created: Jul 12, 2004 12:24:48 PM
 * Purpose:
 ***********************************************************************/
package com.crux.common.jobs;

import com.crux.jobs.util.JobUtil;
import org.quartz.StatefulJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.crux.util.LogManager;
import com.crux.util.ConnectionCache;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.util.SQLUtil;
import com.webfin.ar.model.ARReceiptLinesView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class ValidateReceipt implements StatefulJob {

    private final static transient LogManager logger = LogManager.getInstance(ValidateReceipt.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {

            if (JobUtil.isServerProduction()) {
                execute1();
            }

        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    /*   --CARI DATA POLIS YANG BELUM DI INDEX, SIMPAN KE TEMP TABLE
    select pol_id into temp proses_flag_kreasi
    from ins_policy
    where pol_type_id = 21
    and status in ('POLICY','ENDORSE','RENEWAL')
    and effective_flag = 'Y' and checking_flag is null limit 50;

    --UPDATE DATA INDEX CHECKING DEBITUR
    update ins_pol_obj
    set checking = case when substr(ref1,1,1) in ('1','2','3','4','5','6','7','8','9')
    then ltrim(replace(ref1, split_part(ref1, ' ', 1), ''))
    else ref1 end ||
    coalesce(refd1,refd2::timestamp without time zone - (ref2||' years')::interval)::date || refd2::date
    where pol_id in
    (
    select pol_id
    from proses_flag_kreasi
    );

    --UPDATE FLAG INS_POLICY
    update ins_policy set checking_flag = 'Y'
    where pol_id in
    ( select pol_id
    from proses_flag_kreasi
    );

    --HAPUS TABLE TEMP
    drop table proses_flag_kreasi;

    select 1;
     */
    public void execute1() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            logger.logInfo("execute: performing proses delete konversi file 2");

            long t = System.currentTimeMillis();

            final Statement S = conn.createStatement();

            final DTOList file = ListUtil.getDTOListFromQuery(
                    "select b.ar_invoice_id,b.receipt_no,b.receipt_date "
                    + "from ar_invoice a "
                    + "inner join ar_receipt_lines b on b.ar_invoice_id = a.ar_invoice_id and b.line_type = ? "
                    + "inner join ar_receipt c on c.ar_receipt_id = b.receipt_id "
                    + "where (coalesce(a.cancel_flag,'') <> 'Y' or coalesce(a.posted_flag,'Y') = 'Y') and c.posted_flag = 'Y' "
                    + "and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7) and amount_settled is null ",
                    //+ "AND a.attr_pol_no = '015914141115000300' ",
                    new Object[]{"INVOC"},
                    ARReceiptLinesView.class);

            logger.logDebug("################### : " + file.size());

            for (int i = 0; i < file.size(); i++) {
                ARReceiptLinesView rcl = (ARReceiptLinesView) file.get(i);

                updateInvoice(rcl);

            }

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 2 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }
    }

    public void updateInvoice(ARReceiptLinesView rcl) throws Exception {

        final SQLUtil S = new SQLUtil();
        try {

            PreparedStatement P6 = S.setQuery("update ar_invoice set receipt_no = ?, receipt_date = ?, used_flag = 'Y', amount_settled = amount where ar_invoice_id = ? ");

            P6.setObject(1, rcl.getStReceiptNo());
            P6.setObject(2, rcl.getDtReceiptDate());
            P6.setObject(3, rcl.getStInvoiceID());

            int hasil = P6.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            S.release();
        }

    }
}
