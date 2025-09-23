/***********************************************************************
 * Module:  com.crux.common.jobs.ValidatePolicy
 * Author:  Denny Mahendra
 * Created: Jul 12, 2004 12:24:48 PM
 * Purpose:
 ***********************************************************************/
package com.crux.common.jobs;

import com.crux.common.controller.Helper;
import com.crux.common.parameter.Parameter;
import com.crux.jobs.util.JobUtil;
import com.crux.lang.LanguageManager;
import org.quartz.StatefulJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.crux.util.LogManager;
import com.crux.util.ConnectionCache;
import com.crux.util.DateUtil;
import com.crux.util.MailUtil2;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ValidatePolicy extends Helper implements StatefulJob {

    private final static transient LogManager logger = LogManager.getInstance(ValidatePolicy.class);

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
        int dow = now.get(Calendar.DAY_OF_WEEK);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int hourAuto = 1;

        if (dow == Calendar.SUNDAY) {
            if (hourAuto == hour) {

                try {
                    logger.logInfo("execute: performing proses delete konversi file 2");

                    long t = System.currentTimeMillis();

                    final Statement S = conn.createStatement();

                    String fileName = "validasi_" + DateUtil.getDateStr(new Date(), "yyyyMMdd");
                    File fo = new File("C:/");

                    String fileFOlder = Parameter.readString("SYS_FILES_FOLDER");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

                    String sf = sdf.format(new Date());
                    String tempPath = fileFOlder + File.separator + "report_temp" + File.separator + sf;
                    String path1 = fileFOlder + File.separator + "report_temp" + File.separator;
                    String pathTemp = tempPath + File.separator + fileName + ".xls";

                    try {
                        new File(path1).mkdir();
                        new File(tempPath).mkdir();
                    } catch (Exception e) {
                    }

                    fo = new File(pathTemp);

                    /* Define the SQL query */
                    ResultSet query_set1 = S.executeQuery(
                            " select substr(trx_no,6,2) as cabang,* from ( "
                            + "select a.trx_no,a.months,a.years,sum(a.amount) as total_titipan, "
                            + "(select sum(case when z.debit <> 0 then round(z.debit,2) * -1 else round(z.credit,2) end) "
                            + "from gl_je_detail z "
                            + "inner join gl_accounts y on z.accountid = y.account_id "
                            + "where y.accountno like '489%' and z.trx_no = a.trx_no )as total_jurnal "
                            + "from ar_titipan_premi a where  coalesce(approved, 'N') = 'Y' "
                            //+ "and date_trunc('day',a.applydate) >= '2017-07-01 00:00:00' and date_trunc('day',a.applydate) <= '2017-07-31 00:00:00' "
                            + "and date_trunc('day', a.applydate) >= '" + DateUtil.getYear(new Date()) + "-01-01 00:00:00' "
                            + "and date_trunc('day', a.applydate) <= '" + new Date() + "'"
                            + "group by a.trx_no,a.months,a.years "
                            + ") x where total_titipan <> total_jurnal order by years::bigint,months::bigint ");

                    HSSFWorkbook wb = new HSSFWorkbook();

                    //bikin sheet
                    HSSFSheet sheet = wb.createSheet("vld_titipan_dgn_jurnal");

                    //bikin header
                    HSSFRow row1 = sheet.createRow(0);
                    row1.createCell(0).setCellValue("CABANG");
                    row1.createCell(1).setCellValue("NOMOR BUKTI");
                    row1.createCell(2).setCellValue("BULAN");
                    row1.createCell(3).setCellValue("TAHUN");
                    row1.createCell(4).setCellValue("TOTAL TITIPAN");
                    row1.createCell(5).setCellValue("TOTAL JURNAL");

                    int i = 1;
                    while (query_set1.next()) {
                        HSSFRow row = sheet.createRow(i + 1);
                        row.createCell(0).setCellValue(query_set1.getString("cabang"));
                        row.createCell(1).setCellValue(query_set1.getString("trx_no"));
                        row.createCell(2).setCellValue(query_set1.getString("months"));
                        row.createCell(3).setCellValue(query_set1.getString("years"));
                        row.createCell(4).setCellValue(query_set1.getBigDecimal("total_titipan").doubleValue());
                        row.createCell(5).setCellValue(query_set1.getBigDecimal("total_jurnal").doubleValue());
                        i++;
                    }

                    /* Close all DB related objects */
                    query_set1.close();

                    ResultSet query_set2 = S.executeQuery(
                            "  select substr(receipt_no2,6,2) as cabang,* from ( "
                            + "select a.receipt_no2,a.months,a.years,sum(b.titipan_premi_used_amount) as titipan_premi_terpakai, "
                            + "(select sum(case when z.debit <> 0 then round(z.debit,2) else round(z.credit,2) * -1 end) "
                            + "from gl_je_detail z "
                            + "inner join gl_accounts y on z.accountid = y.account_id "
                            + "where y.accountno like '489%' and z.trx_no = a.receipt_no2 )as total_jurnal "
                            + "from ar_receipt a "
                            + "inner join ar_receipt_lines b on a.ar_receipt_id = b.receipt_id "
                            + "where  a.ar_settlement_id in (25,33)  and  coalesce(a.posted_flag, 'N') = 'Y' and b.titipan_premi_id is not null "
                            //+ "and date_trunc('day',a.receipt_date) >= '2017-08-01 00:00:00' and date_trunc('day',a.receipt_date) <= '2017-08-15 00:00:00' "
                            + "and date_trunc('day', a.receipt_date) >= '" + DateUtil.getYear(new Date()) + "-01-01 00:00:00' "
                            + "and date_trunc('day', a.receipt_date) <= '" + new Date() + "'"
                            + "group by a.receipt_no2,a.months,a.years "
                            + ")  zz where titipan_premi_terpakai <> total_jurnal order by years::bigint,months::bigint ");

                    //bikin sheet
                    HSSFSheet sheet2 = wb.createSheet("vld_realisasi_dgn_jurnal");

                    //bikin header
                    HSSFRow row2 = sheet2.createRow(0);
                    row2.createCell(0).setCellValue("CABANG");
                    row2.createCell(1).setCellValue("NOMOR BUKTI");
                    row2.createCell(2).setCellValue("BULAN");
                    row2.createCell(3).setCellValue("TAHUN");
                    row2.createCell(4).setCellValue("TOTAL REALISASI");
                    row2.createCell(5).setCellValue("TOTAL JURNAL");

                    int j = 1;
                    while (query_set2.next()) {
                        HSSFRow row = sheet2.createRow(j + 1);
                        row.createCell(0).setCellValue(query_set2.getString("cabang"));
                        row.createCell(1).setCellValue(query_set2.getString("receipt_no2"));
                        row.createCell(2).setCellValue(query_set2.getString("months"));
                        row.createCell(3).setCellValue(query_set2.getString("years"));
                        row.createCell(4).setCellValue(query_set2.getBigDecimal("titipan_premi_terpakai").doubleValue());
                        row.createCell(5).setCellValue(query_set2.getBigDecimal("total_jurnal").doubleValue());
                        j++;
                    }

                    /* Close all DB related objects */
                    query_set2.close();

                    ResultSet query_set3 = S.executeQuery(
                            " select *  from (  select koda,pol_id,pol_no,round(premi_bruto,2) as premi_bruto, "
                            + "coalesce((select sum(case when z.debit <> 0 then round(z.debit,2) * -1 else round(z.credit,2)  end) "
                            + "from gl_je_detail z inner join gl_accounts y on z.accountid = y.account_id "
                            + "where (case when a.ref_ent_id = '00' then (y.accountno like '6131%' or y.accountno like '6132%') else y.accountno like '6133%' || gl_code|| '%' end) "
                            + "and z.pol_no = a.nopol ),0)as total_jurnal  from ( "
                            + "select  a.cc_code as koda,a.create_date as tglentry,a.policy_date as tglpolis,a.period_start as periode_awal,a.period_end as periode_akhir, "
                            + "a.pol_id,';'||a.pol_no as pol_no,a.cust_name as tertanggung,a.entity_id,';'||c.ref_ent_id as kodeko,';'||a.ref1 as no_pp,a.ref5 as nama_pp,"
                            + "a.coins_pol_no as no_polis_rujukan,a.ccy,a.ccy_rate as kurs,getpremiend(b.entity_id,coalesce(a.premi_total*a.ccy_rate,0),coalesce(b.premi_amt*a.ccy_rate,0)*-1) as premi_bruto, "
                            + "c.ref_ent_id,a.pol_no as nopol,b.coins_type,c.ent_name as koas,c.gl_code "
                            + "from ins_policy a "
                            + "inner join ins_pol_coins b on b.policy_id = a.pol_id "
                            + "left join ent_master c on c.ent_id = b.entity_id "
                            + "left join ent_master d on d.ent_id = a.entity_id "
                            + "left join ent_master e on e.ent_id = a.prod_id "
                            + "where  a.status in ('POLICY','ENDORSE','RENEWAL') and (b.entity_id <> 1 or b.coins_type <> 'COINS_COVER') "
                            + "and a.active_flag='Y' and a.effective_flag='Y'  and c.ref_ent_id = '00' "
                            //+ "and date_trunc('day',a.policy_date) >= '2017-08-01 00:00:00' and date_trunc('day',a.policy_date) <= '2017-08-31 00:00:00' "
                            + "and date_trunc('day', a.policy_date) >= '" + DateUtil.getYear(new Date()) + "-01-01 00:00:00' "
                            + "and date_trunc('day', a.policy_date) <= '" + new Date() + "'"
                            + " ) a ) zz where round(premi_bruto,0) <> round(total_jurnal,0) ");

                    //bikin sheet
                    HSSFSheet sheet3 = wb.createSheet("vld_produksi_dgn_jurnal");

                    //bikin header
                    HSSFRow row3 = sheet3.createRow(0);
                    row3.createCell(0).setCellValue("CABANG");
                    row3.createCell(1).setCellValue("NOMOR POLIS");
                    row3.createCell(2).setCellValue("PREMI");
                    row3.createCell(3).setCellValue("JURNAL");

                    int k = 1;
                    while (query_set3.next()) {
                        HSSFRow row = sheet3.createRow(k + 1);
                        row.createCell(0).setCellValue(query_set3.getString("koda"));
                        row.createCell(1).setCellValue(query_set3.getString("pol_no"));
                        row.createCell(2).setCellValue(query_set3.getBigDecimal("premi_bruto").doubleValue());
                        row.createCell(3).setCellValue(query_set3.getBigDecimal("total_jurnal").doubleValue());
                        k++;
                    }

                    query_set3.close();

                    ResultSet query_set4 = S.executeQuery(
                            " select a.cc_code,a.policy_date,a.pol_id,b.ar_invoice_id,a.pol_no,round(a.premi_netto * a.ccy_rate,0) as premi_netto, "
                            + "(select round(sum(amount),0) from ar_invoice x where x.ar_trx_type_id in (5,6,7) "
                            + "and coalesce(x.cancel_flag,'N') <> 'Y' and coalesce(x.posted_flag,'Y') = 'Y' "
                            + "and x.attr_pol_id = a.pol_id) as total_invoice,   b.receipt_no,b.receipt_date   "
                            + "from ins_policy a   "
                            + "inner join ar_invoice b on a.pol_id = b.attr_pol_id   "
                            + "where ar_trx_type_id in (5,6,7) "
                            + "and coalesce(b.cancel_flag,'N') <> 'Y' and coalesce(b.posted_flag,'Y') = 'Y' "
                            + "and a.pol_id in ( select m.pol_id from (  select  a.pol_id,pol_no,"
                            + "round(a.premi_netto * a.ccy_rate,0) as premi_netto,  "
                            + "(select round(sum(amount),0) from ar_invoice x where x.ar_trx_type_id in (5,6,7) and coalesce(x.cancel_flag,'N') <> 'Y' "
                            + "and coalesce(x.posted_flag,'Y') = 'Y' and x.attr_pol_id = a.pol_id) as total_invoice  "
                            + "from ins_policy a  where  a.status in ('POLICY','ENDORSE','RENEWAL')  and effective_flag = 'Y' and active_flag = 'Y'  "
                            //+ "and date_trunc('day',a.policy_date) >= '2017-08-01 00:00:00' and date_trunc('day',a.policy_date) <= '2017-08-31 00:00:00' "
                            + "and date_trunc('day', a.policy_date) >= '" + DateUtil.getYear(new Date()) + "-01-01 00:00:00' "
                            + "and date_trunc('day', a.policy_date) <= '" + new Date() + "'"
                            + "group by a.pol_id,pol_no ) m inner join ins_policy n on m.pol_id = n.pol_id "
                            + "where m.premi_netto <> 0 and m.premi_netto <> m.total_invoice and (m.premi_netto - m.total_invoice) not in (1,-1) ) order by pol_id,b.ar_invoice_id ");

                    //bikin sheet
                    HSSFSheet sheet4 = wb.createSheet("vld_produksi_dgn_piutang");

                    //bikin header
                    HSSFRow row4 = sheet4.createRow(0);
                    row4.createCell(0).setCellValue("CABANG");
                    row4.createCell(1).setCellValue("NOMOR POLIS");
                    row4.createCell(2).setCellValue("TANGGAL POLIS");
                    row4.createCell(3).setCellValue("PREMI NETTO");
                    row4.createCell(4).setCellValue("TAGIHAN NETTO");
                    row4.createCell(5).setCellValue("NO BUKTI BAYAR");
                    row4.createCell(6).setCellValue("TANGGAL BAYAR");

                    int l = 1;
                    while (query_set4.next()) {
                        HSSFRow row = sheet4.createRow(l + 1);
                        row.createCell(0).setCellValue(query_set4.getString("cc_code"));
                        row.createCell(1).setCellValue(query_set4.getString("pol_no"));
                        row.createCell(2).setCellValue(query_set4.getDate("policy_date"));
                        row.createCell(3).setCellValue(query_set4.getBigDecimal("premi_netto").doubleValue());
                        row.createCell(4).setCellValue(query_set4.getBigDecimal("total_invoice").doubleValue());
                        if (query_set4.getString("receipt_no") != null) {
                            row.createCell(5).setCellValue(query_set4.getString("receipt_no"));
                        }
                        if (query_set4.getDate("receipt_date") != null) {
                            row.createCell(6).setCellValue(query_set4.getDate("receipt_date"));
                        }
                        l++;
                    }

                    query_set4.close();

                    ResultSet query_set5 = S.executeQuery(
                            " select substr(receipt_no,5,2) as cabang,*,  (select premi_netto from ins_policy x where x.pol_id = xx.attr_pol_id) as premi_netto_produk "
                            + "from  (  select  a.ar_receipt_id,upper(f.description) as description,a.receipt_no,e.attr_pol_no,e.attr_pol_id,a.months,a.years,  "
                            + "sum(case when c.negative_flag = 'Y' and tax_flag is null then c.amount * -1 when c.negative_flag <> 'Y' and tax_flag is null then c.amount end) as tagihan_netto_per_invoice,"
                            + "sum(case when c.negative_flag = 'Y' then c.amount * -1 when c.negative_flag <> 'Y' and tax_flag is null then c.amount end) as tagihan_netto_per_invoice2, "
                            + "(select sum(case when z.debit <> 0 then round(z.debit,2) * -1 else round(z.credit,2) end) from gl_je_detail z inner join gl_accounts y on z.accountid= y.account_id "
                            + "where y.accountno like '133%' and z.trx_no = a.receipt_no and z.pol_no = e.attr_pol_no)as total_jurnal  "
                            + "from ar_receipt a  "
                            + "inner join ar_receipt_lines b on a.ar_receipt_id = b.receipt_id  "
                            + "inner join ar_invoice_details c on b.ar_invoice_dtl_id = c.ar_invoice_dtl_id  "
                            + "inner join ar_invoice e on b.ar_invoice_id = e.ar_invoice_id  "
                            + "left join ar_settlement f on f.ar_settlement_id = a.ar_settlement_id  "
                            + "where  a.ar_settlement_id in (1,25,38,41) and coalesce(a.posted_flag, 'N') = 'Y'  "
                            //+ "and date_trunc('day',a.receipt_date) >= '2017-08-01 00:00:00'  and date_trunc('day',a.receipt_date) <= '2017-08-31 00:00:00' "
                            + "and date_trunc('day', a.receipt_date) >= '" + DateUtil.getYear(new Date()) + "-01-01 00:00:00' "
                            + "and date_trunc('day', a.receipt_date) <= '" + new Date() + "'"
                            + "group by a.ar_receipt_id,f.description,a.receipt_no,e.attr_pol_no,e.attr_pol_id,a.months,a.years "
                            + ") xx  where (tagihan_netto_per_invoice <> total_jurnal) and (tagihan_netto_per_invoice2 <> total_jurnal)  order by ar_receipt_id ");

                    //bikin sheet
                    HSSFSheet sheet5 = wb.createSheet("vld_pembayaran_dgn_jurnal");

                    //bikin header
                    HSSFRow row5 = sheet5.createRow(0);
                    row5.createCell(0).setCellValue("CABANG");
                    row5.createCell(1).setCellValue("NOMOR BUKTI");
                    row5.createCell(2).setCellValue("NOMOR POLIS");
                    row5.createCell(3).setCellValue("BULAN");
                    row5.createCell(4).setCellValue("TAHUN");
                    row5.createCell(5).setCellValue("PEMBAYARAN NETTO");
                    row5.createCell(6).setCellValue("TOTAL JURNAL");
                    row5.createCell(7).setCellValue("TAGIHAN NETTO POLIS");

                    int m = 1;
                    while (query_set5.next()) {
                        HSSFRow row = sheet5.createRow(m + 1);
                        row.createCell(0).setCellValue(query_set5.getString("cabang"));
                        row.createCell(1).setCellValue(query_set5.getString("receipt_no"));
                        row.createCell(2).setCellValue(query_set5.getString("attr_pol_no"));
                        row.createCell(3).setCellValue(query_set5.getString("months"));
                        row.createCell(4).setCellValue(query_set5.getString("years"));
                        row.createCell(5).setCellValue(query_set5.getBigDecimal("tagihan_netto_per_invoice").doubleValue());
                        row.createCell(6).setCellValue(query_set5.getBigDecimal("total_jurnal").doubleValue());
                        row.createCell(7).setCellValue(query_set5.getBigDecimal("premi_netto_produk").doubleValue());
                        m++;
                    }

                    query_set5.close();

                    ResultSet query_set6 = S.executeQuery(
                            " select * from ( select a.*,b.oscomm from (  select  b.pol_id::text,';'||a.pol_no as pol_no,b.ent_id::text,(b.amount-b.tax_amount) as amount,"
                            + "';'||(b.pol_id||''||b.ent_id||''||round(b.amount-b.tax_amount,0)) as zzz,count(b.pol_id||''||b.ent_id||''||b.amount-b.tax_amount) as comm  "
                            + "from ins_policy a "
                            + "inner join ins_pol_items b on b.pol_id = a.pol_id "
                            + "inner join ins_items c on c.ins_item_id = b.ins_item_id  "
                            + "where  status in ('POLICY','RENEWAL','ENDORSE') and a.active_flag = 'Y' and a.effective_flag = 'Y' "
                            + "and c.item_type = 'COMIS' and c.item_group <> 'FEEBASE'  "
                            + "group by b.pol_id,a.pol_no,b.ent_id,b.amount,b.tax_amount order by a.pol_no) a "
                            + "left join ( select attr_pol_id,';'||attr_pol_no as attr_pol_no,ent_id,amount,';'||(attr_pol_id||''||ent_id||''||round(amount,0)) as zzz,"
                            + "count(attr_pol_id||''||ent_id||''||amount) as oscomm from ar_invoice a "
                            + "where ar_trx_type_id = 11 and a.no_surat_hutang is null and coalesce(a.cancel_flag,'') <> 'Y' and a.amount_settled is null "
                            + "group by invoice_no,attr_pol_id,attr_pol_no,ent_id,amount order by attr_pol_no "
                            + ") b on b.zzz = a.zzz order by a.pol_no ) a where comm < oscomm ");

                    //bikin sheet
                    HSSFSheet sheet6 = wb.createSheet("vld_komisi_dgn_hutkom");

                    //bikin header
                    HSSFRow row6 = sheet6.createRow(0);
                    row6.createCell(0).setCellValue("polid");
                    row6.createCell(1).setCellValue("polis");
                    row6.createCell(2).setCellValue("entid");
                    row6.createCell(3).setCellValue("nilai");
                    row6.createCell(4).setCellValue("jumlah komisi produksi");
                    row6.createCell(5).setCellValue("jumlah komisi os");

                    int n = 1;
                    while (query_set6.next()) {
                        HSSFRow row = sheet6.createRow(n + 1);
                        row.createCell(0).setCellValue(query_set6.getString("pol_id"));
                        row.createCell(1).setCellValue(query_set6.getString("pol_no"));
                        row.createCell(2).setCellValue(query_set6.getString("ent_id"));
                        row.createCell(3).setCellValue(query_set6.getBigDecimal("amount").doubleValue());
                        row.createCell(4).setCellValue(query_set6.getBigDecimal("comm").doubleValue());
                        row.createCell(5).setCellValue(query_set6.getBigDecimal("oscomm").doubleValue());
                        n++;
                    }

                    query_set6.close();

                    ResultSet query_set7 = S.executeQuery(
                            " SELECT substr(receipt_no2,6,2) as cabang,* FROM (  "
                            + "select  a.receipt_no2,a.months,a.years,sum(b.titipan_premi_used_amount) as titipan_premi_terpakai,"
                            + "(select sum(case when z.debit <> 0 then round(z.debit,2) else round(z.credit,2) * -1 end)  "
                            + "from gl_je_detail z inner join gl_accounts y on z.accountid = y.account_id "
                            + "where y.accountno like '489%' and z.trx_no = a.receipt_no2 )as total_jurnal   "
                            + "from ar_receipt a inner join ar_receipt_lines b on a.ar_receipt_id = b.receipt_id  "
                            + "where  a.ar_settlement_id in (2,33,39)  and  coalesce(a.posted_flag, 'N') = 'Y' and b.titipan_premi_id is not null "
                            //+ "and date_trunc('day',a.receipt_date) >= '2017-08-01 00:00:00'  and date_trunc('day',a.receipt_date) <= '2017-08-31 00:00:00' "
                            + "and date_trunc('day', a.receipt_date) >= '" + DateUtil.getYear(new Date()) + "-01-01 00:00:00' "
                            + "and date_trunc('day', a.receipt_date) <= '" + new Date() + "'"
                            + "group by a.receipt_no2,a.months,a.years ) zz where titipan_premi_terpakai <> total_jurnal  order by years::bigint,months::bigint ");

                    //bikin sheet
                    HSSFSheet sheet7 = wb.createSheet("vld_pemb.komisi_dgn_jurnal");

                    //bikin header
                    HSSFRow row7 = sheet7.createRow(0);
                    row7.createCell(0).setCellValue("CABANG");
                    row7.createCell(1).setCellValue("NOMOR BUKTI");
                    row7.createCell(2).setCellValue("BULAN");
                    row7.createCell(3).setCellValue("TAHUN");
                    row7.createCell(4).setCellValue("TOTAL TITIPAN");
                    row7.createCell(5).setCellValue("TOTAL JURNAL");

                    int o = 1;
                    while (query_set7.next()) {
                        HSSFRow row = sheet7.createRow(o + 1);
                        row.createCell(0).setCellValue(query_set7.getString("cabang"));
                        row.createCell(1).setCellValue(query_set7.getString("receipt_no2"));
                        row.createCell(2).setCellValue(query_set7.getString("months"));
                        row.createCell(3).setCellValue(query_set7.getString("years"));
                        row.createCell(4).setCellValue(query_set7.getBigDecimal("titipan_premi_terpakai").doubleValue());
                        row.createCell(5).setCellValue(query_set7.getBigDecimal("total_jurnal").doubleValue());
                        o++;
                    }

                    query_set7.close();

                    ResultSet query_set8 = S.executeQuery(
                            " select a.cc_code,a.policy_date,a.dla_date,a.pol_id,b.ar_invoice_id,a.pol_no,a.dla_no,"
                            //                        + "round(a.claim_amount*a.ccy_rate_claim,0) as claim_amount,   "
                            + "round((select sum(getpremi(c.use_tax = 'Y',b.amount-b.tax_amount,b.amount*(getpremi(c.negative_flag = 'Y',-1,1)))) "
                            + "from ins_policy x inner join ins_pol_items b on b.pol_id = x.pol_id and b.item_class = 'CLM' "
                            + "inner join ins_items c on c.ins_item_id = b.ins_item_id and c.ins_item_id <> 61 "
                            + "where x.pol_id = a.pol_id)*a.ccy_rate_claim,0) as claim_amount,   "
                            + "( select coalesce(round(sum(y.amount*(getpremi((x.ar_trx_type_id = 11 and y.negative_flag = 'N' and y.tax_flag = 'Y') "
                            + "or (x.ar_trx_type_id = 12 and y.negative_flag = 'Y'),-1,1))),0),0) "
                            + "from ar_invoice x inner join ar_invoice_details y on y.ar_invoice_id = x.ar_invoice_id "
                            + "where x.ar_trx_type_id in (12,26,11) and coalesce(x.cancel_flag,'N') <> 'Y' and (y.ar_trx_line_id <> 96 or y.negative_flag <> 'Y') "
                            + "and coalesce(x.posted_flag,'Y') = 'Y' and x.attr_pol_id = a.pol_id ) as total_invoice, "
                            + "( select round(sum(credit-debit),0) "
                            + "from gl_je_detail j "
                            + "inner join gl_accounts k on j.accountid = k.account_id "
                            + "where (k.accountno like '3371%' or k.accountno like '48930%') and substr(j.trx_no,1,1) = 'K' "
                            + "and j.pol_no = a.pol_no and trim(substring(TRIM(right(j.description, 24)), position('LKP' in TRIM(right(j.description, 24))))) = a.dla_no "
                            + "and date_trunc('month',j.applydate) = date_trunc('month',a.dla_date)) as total_jurnal,"
                            + "( select round(sum(debit-credit),0) "
                            + "from gl_je_detail j inner join gl_accounts k on j.accountid = k.account_id "
                            + "where (k.accountno like '14910%') and substr(j.trx_no,1,1) = 'L' and j.pol_no = a.pol_no "
                            + "and trim(substring(TRIM(right(j.description, 24)), position('LKP' in TRIM(right(j.description, 24))))) = a.dla_no "
                            + "and date_trunc('month',j.applydate) = date_trunc('month',a.dla_date)) as total_jurnal_koas,b.receipt_no,b.receipt_date "
                            + "from ins_policy a inner join ar_invoice b on a.pol_id = b.attr_pol_id "
                            + "where ar_trx_type_id in (12,26) and coalesce(b.cancel_flag,'N') <> 'Y' and coalesce(b.posted_flag,'Y') = 'Y' and coalesce(a.admin_notes,'') <> 'OK' "
                            + "and a.pol_id in (select m.pol_id from ( "
                            + "select  a.pol_id,pol_no,a.dla_no,"
                            //                        + "round(a.claim_amount * a.ccy_rate_claim,0) as claim_amount,   "
                            + "round((select sum(getpremi(c.use_tax = 'Y',b.amount-b.tax_amount,b.amount*(getpremi(c.negative_flag = 'Y',-1,1)))) "
                            + "from ins_policy x inner join ins_pol_items b on b.pol_id = x.pol_id and b.item_class = 'CLM' "
                            + "inner join ins_items c on c.ins_item_id = b.ins_item_id and c.ins_item_id <> 61 where x.pol_id = a.pol_id)*a.ccy_rate_claim,0) as claim_amount, "
                            + "( select coalesce(round(sum(y.amount*(getpremi((x.ar_trx_type_id = 11 and y.negative_flag = 'N' and y.tax_flag = 'Y') "
                            + "or (x.ar_trx_type_id = 12 and y.negative_flag = 'Y'),-1,1))),0),0) "
                            + "from ar_invoice x inner join ar_invoice_details y on y.ar_invoice_id = x.ar_invoice_id "
                            + "where x.ar_trx_type_id in (12,26,11) and coalesce(x.cancel_flag,'N') <> 'Y' and (y.ar_trx_line_id <> 96 or y.negative_flag <> 'Y') "
                            + "and coalesce(x.posted_flag,'Y') = 'Y' and x.attr_pol_id = a.pol_id ) as total_invoice,  "
                            + "( select round(sum(credit-debit),0) "
                            + "from gl_je_detail j inner join gl_accounts k on j.accountid = k.account_id "
                            + "where (k.accountno like '3371%' or k.accountno like '48930%') and substr(j.trx_no,1,1) = 'K' and j.pol_no = a.pol_no "
                            + "and trim(substring(TRIM(right(j.description, 24)), position('LKP' in TRIM(right(j.description, 24))))) = a.dla_no "
                            + "and date_trunc('month',j.applydate) = date_trunc('month',a.dla_date)) as total_jurnal,"
                            + "( select round(sum(debit-credit),0) "
                            + "from gl_je_detail j inner join gl_accounts k on j.accountid = k.account_id "
                            + "where (k.accountno like '14910%') and substr(j.trx_no,1,1) = 'L' and j.pol_no = a.pol_no "
                            + "and trim(substring(TRIM(right(j.description, 24)), position('LKP' in TRIM(right(j.description, 24))))) = a.dla_no "
                            + "and date_trunc('month',j.applydate) = date_trunc('month',a.dla_date)) as total_jurnal_koas   "
                            + "from ins_policy a  where  a.status in ('CLAIM','CLAIM ENDORSE') and a.claim_status = 'DLA' and effective_flag = 'Y' and active_flag = 'Y'  "
                            //+ "and date_trunc('day',a.claim_approved_date) >= '2017-08-01 00:00:00' and date_trunc('day',a.claim_approved_date) <= '2017-08-31 00:00:00' "
                            + "and date_trunc('day', a.claim_approved_date) >= '" + DateUtil.getYear(new Date()) + "-01-01 00:00:00' "
                            + "and date_trunc('day', a.claim_approved_date) <= '" + new Date() + "'"
                            + ") m inner join ins_policy n on m.pol_id = n.pol_id where coalesce(m.claim_amount,0) <> coalesce(m.total_invoice,0) or coalesce(m.claim_amount,0) <> coalesce(m.total_jurnal,0) "
                            + "or coalesce(m.total_invoice,0) <> coalesce(m.total_jurnal,0)) order by pol_id,b.ar_invoice_id ");

                    //bikin sheet
                    HSSFSheet sheet8 = wb.createSheet("vld_klaim_dgn_jurnal");

                    //bikin header
                    HSSFRow row8 = sheet8.createRow(0);
                    row8.createCell(0).setCellValue("CABANG");
                    row8.createCell(1).setCellValue("NOMOR POLIS");
                    row8.createCell(2).setCellValue("TANGGAL POLIS");
                    row8.createCell(3).setCellValue("KLAIM NETTO");
                    row8.createCell(4).setCellValue("UTANG KLAIM NETTO");
                    row8.createCell(5).setCellValue("TOTAL JURNAL");
                    row8.createCell(6).setCellValue("NO BUKTI BAYAR");
                    row8.createCell(7).setCellValue("TANGGAL BAYAR");

                    int p = 1;
                    while (query_set8.next()) {
                        HSSFRow row = sheet8.createRow(p + 1);
                        row.createCell(0).setCellValue(query_set8.getString("cc_code"));
                        row.createCell(1).setCellValue(query_set8.getString("pol_no"));
                        row.createCell(2).setCellValue(query_set8.getDate("policy_date"));
                        row.createCell(3).setCellValue(query_set8.getBigDecimal("claim_amount").doubleValue());
                        row.createCell(4).setCellValue(query_set8.getBigDecimal("total_invoice").doubleValue());
                        if (query_set8.getBigDecimal("total_jurnal") != null) {
                            row.createCell(5).setCellValue(query_set8.getBigDecimal("total_jurnal").doubleValue());
                        }
                        if (query_set8.getString("receipt_no") != null) {
                            row.createCell(6).setCellValue(query_set8.getString("receipt_no"));
                        }
                        if (query_set8.getDate("receipt_date") != null) {
                            row.createCell(7).setCellValue(query_set8.getDate("receipt_date"));
                        }
                        p++;
                    }

                    query_set8.close();

                    ResultSet query_set8a = S.executeQuery(
                            " select * from ( select  cc_code,attr_pol_no,attr_pol_id::character varying,a.refid2,a.mutation_date,"
                            + "round(sum(case when b.negative_flag = 'Y' then b.amount*-1 else b.amount end),0) as amount,"
                            + "coalesce((select round(coalesce(claim_amount*ccy_rate_claim),0) "
                            + "from ins_policy x where x.pol_id = a.pol_id),0) as total_prod_claim "
                            + "from ar_invoice a  inner join ar_invoice_details b on a.ar_invoice_id = b.ar_invoice_id "
                            + "where  a.ar_trx_type_id in (12,26) and coalesce(a.cancel_flag,'N') <> 'Y' "
                            + "and coalesce(a.claim_status,'') <> 'OK' and coalesce(a.posted_flag,'Y') = 'Y' "
                            //+ "and date_trunc('day', a.mutation_date) >= '2017-08-01 00:00:00' and date_trunc('day', a.mutation_date) <= '2017-08-31 00:00:00' "
                            + "and date_trunc('day', a.mutation_date) >= '" + DateUtil.getYear(new Date()) + "-01-01 00:00:00' "
                            + "and date_trunc('day', a.mutation_date) <= '" + new Date() + "'"
                            + "group by cc_code,attr_pol_no,attr_pol_id,a.refid2,a.mutation_date,pol_id "
                            + ") m  where m.amount <> m.total_prod_claim ");

                    //bikin sheet
                    HSSFSheet sheet8a = wb.createSheet("vld_hutklaim_dgn_produksi");

                    //bikin header
                    HSSFRow row8a = sheet8a.createRow(0);
                    row8a.createCell(0).setCellValue("CABANG");
                    row8a.createCell(1).setCellValue("NOMOR POLIS");
                    row8a.createCell(2).setCellValue("POLIS ID");
                    row8a.createCell(3).setCellValue("NOMOR LKP");
                    row8a.createCell(4).setCellValue("TANGGAL LKP");
                    row8a.createCell(5).setCellValue("JUMLAH HUTANG");
                    row8a.createCell(6).setCellValue("JUMLAH PROD KLAIM");

                    int p2 = 1;
                    while (query_set8a.next()) {
                        HSSFRow row = sheet8a.createRow(p2 + 1);
                        row.createCell(0).setCellValue(query_set8a.getString("cc_code"));
                        row.createCell(1).setCellValue(query_set8a.getString("attr_pol_no"));
                        row.createCell(2).setCellValue(query_set8a.getString("attr_pol_id"));
                        row.createCell(3).setCellValue(query_set8a.getString("refid2"));
                        row.createCell(4).setCellValue(query_set8a.getDate("mutation_date"));
                        row.createCell(5).setCellValue(query_set8a.getBigDecimal("amount").doubleValue());
                        row.createCell(6).setCellValue(query_set8a.getBigDecimal("total_prod_claim").doubleValue());
                        p2++;
                    }

                    query_set8a.close();

                    ResultSet query_set9 = S.executeQuery(
                            " select  pol_no,trim(substring(TRIM(right(a.description, 24)), position('LKP' in TRIM(right(a.description, 24))))) as no_lkp, "
                            + "applydate,credit-debit as amount,  (SELECT approved_date FROM INS_POLICY where status in ('CLAIM','CLAIM ENDORSE') and claim_status = 'DLA' and effective_flag = 'Y' "
                            + "and dla_no = trim(substring(TRIM(right(a.description, 24)), position('LKP' in TRIM(right(a.description, 24)))))) as tgl_approved   "
                            + "from gl_je_detail a  inner join gl_accounts b on a.accountid = b.account_id  "
                            + "where  b.accountno like '3371%' and substr(a.trx_no,1,1) = 'K'  "
                            //+ "and date_trunc('day',a.applydate) >= '2017-08-01 00:00:00' and date_trunc('day',a.applydate) <= '2017-08-31 00:00:00' "
                            + "and date_trunc('day', a.applydate) >= '" + DateUtil.getYear(new Date()) + "-01-01 00:00:00' "
                            + "and date_trunc('day', a.applydate) <= '" + new Date() + "'"
                            + "and trim(substring(TRIM(right(a.description, 24)), position('LKP' in TRIM(right(a.description, 24))))) not in ( "
                            + "select dla_no   from ins_policy where status in ('CLAIM','CLAIM ENDORSE') and claim_status = 'DLA' and effective_flag = 'Y' "
                            //                        + "and date_trunc('day',approved_date) >= '2017-08-01 00:00:00'  and date_trunc('day',approved_date) <= '2017-08-31 00:00:00' "
                            + "and date_trunc('day', approved_date) >= '" + DateUtil.getYear(new Date()) + "-01-01 00:00:00' "
                            + "and date_trunc('day', approved_date) <= '" + new Date() + "'"
                            + " ) ");

                    //bikin sheet
                    HSSFSheet sheet9 = wb.createSheet("vld_jurnalklaim_dgn_produksi");

                    //bikin header
                    HSSFRow row9 = sheet9.createRow(0);
                    row9.createCell(0).setCellValue("NOMOR POLIS");
                    row9.createCell(1).setCellValue("NOMOR LKP");
                    row9.createCell(2).setCellValue("TANGGAL JURNAL");
                    row9.createCell(3).setCellValue("UTANG KLAIM NETTO");
                    row9.createCell(4).setCellValue("TANGGAL APPROVED");

                    int q = 1;
                    while (query_set9.next()) {
                        HSSFRow row = sheet9.createRow(q + 1);
                        row.createCell(0).setCellValue(query_set9.getString("pol_no"));
                        row.createCell(1).setCellValue(query_set9.getString("no_lkp"));
                        row.createCell(2).setCellValue(query_set9.getDate("applydate"));
                        row.createCell(3).setCellValue(query_set9.getBigDecimal("amount").doubleValue());
                        if (query_set9.getDate("tgl_approved") != null) {
                            row.createCell(4).setCellValue(query_set9.getDate("tgl_approved"));
                        }
                        q++;
                    }

                    query_set9.close();

                    ResultSet query_set10 = S.executeQuery(
                            " select * from ( "
                            + "select ';'||a.pol_no as pol_no,count(a.pol_no) as jumlah "
                            + "from ins_policy a "
                            + "where a.status in ('POLICY','ENDORSE','RENEWAL') and a.active_flag='Y' and a.effective_flag = 'Y' "
                            + "and date_trunc('day', a.approved_date) >= '2018-01-01 00:00:00' "
                            //                        + "and date_trunc('day', a.approved_date) <= '" + new Date() + "'"
                            + "group by a.pol_no ) a where jumlah > 1 order by a.pol_no ");

                    //bikin sheet
                    HSSFSheet sheet10 = wb.createSheet("vld_polis_dobel");

                    //bikin header
                    HSSFRow row10 = sheet10.createRow(0);
                    row10.createCell(0).setCellValue("NOMOR POLIS");
                    row10.createCell(1).setCellValue("JUMLAH");

                    int w = 1;
                    while (query_set10.next()) {
                        HSSFRow row = sheet10.createRow(w + 1);
                        row.createCell(0).setCellValue(query_set10.getString("pol_no"));
                        row.createCell(1).setCellValue(query_set10.getBigDecimal("jumlah").doubleValue());
                        w++;
                    }

                    query_set10.close();

                    ResultSet query_set11 = S.executeQuery(
                            " select * from ( select a.pol_no,b.entity_id,count(b.entity_id) as jumlah "
                            + "from ( select a.policy_date,a.pol_id,';'||a.pol_no as pol_no "
                            + "from ins_policy a where a.status in ('POLICY','ENDORSE','RENEWAL') "
                            + "and a.active_flag='Y' and a.effective_flag = 'Y' and a.cover_type_code <> 'DIRECT' "
                            + "and date_trunc('day', a.approved_date) >= '2018-01-01 00:00:00' "
                            //                        + "and date_trunc('day', a.approved_date) <= '" + new Date() + "'"
                            + ") a inner join ins_pol_coins b on b.policy_id = a.pol_id where b.entity_id <> 1 "
                            + "group by a.pol_no,b.entity_id order by a.pol_no,b.entity_id asc "
                            + ") a where a.jumlah > 1 order by a.pol_no ");

                    //bikin sheet
                    HSSFSheet sheet11 = wb.createSheet("vld_polis_koas_dobel");

                    //bikin header
                    HSSFRow row11 = sheet11.createRow(0);
                    row11.createCell(0).setCellValue("NOMOR POLIS");
                    row11.createCell(1).setCellValue("ENTID");
                    row11.createCell(2).setCellValue("JUMLAH");

                    int r = 1;
                    while (query_set11.next()) {
                        HSSFRow row = sheet11.createRow(r + 1);
                        row.createCell(0).setCellValue(query_set11.getString("pol_no"));
                        row.createCell(1).setCellValue(query_set11.getBigDecimal("entity_id").doubleValue());
                        row.createCell(2).setCellValue(query_set11.getBigDecimal("jumlah").doubleValue());
                        r++;
                    }

                    query_set11.close();

                    FileOutputStream fop = new FileOutputStream(fo);
                    wb.write(fop);
                    fop.close();

                    S.close();

//            String receiver = Parameter.readString("FINANCE_EMAIL");
                    String receiver = "prasetyo@askrida.co.id";
                    String subject = "Validasi Polis Per Hari Ini " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "dd ^^ yyyy"));
                    String text = "Kepada Yth.\n"
                            + "Divisi Keuangan\n"
                            + "Di Tempat\n\n\n"
                            + "Dengan hormat,\n\n"
                            + "Bersama ini kami lampirkan validasi yang diproses secara otomatis harian oleh Sistem.\n"
                            + "Demikian disampaikan dan semoga laporan tersebut di atas dapat menambah informasi, terima kasih.\n\n\n"
                            + "Hormat kami,\n"
                            + "Administrator";

                    MailUtil2 mail = new MailUtil2();
                    mail.sendEmailWithType(pathTemp, fileName, receiver, subject, text, ".xls");

                    t = System.currentTimeMillis() - t;

                    logger.logInfo("proses 2 selesai dalam " + t + " ms");

                } finally {
                    conn.close();
                }
            }
        }
    }
}
