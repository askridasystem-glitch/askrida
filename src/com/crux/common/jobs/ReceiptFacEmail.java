/***********************************************************************
 * Module:  com.crux.common.jobs.ReceiptFacEmail
 * Author:  Denny Mahendra
 * Created: Jul 12, 2004 12:24:48 PM
 * Purpose:
 ***********************************************************************/
package com.crux.common.jobs;

import com.crux.common.parameter.Parameter;
import com.crux.jobs.util.JobUtil;
import com.crux.pool.DTOPool;
import org.quartz.StatefulJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.crux.util.LogManager;
import com.crux.util.ConnectionCache;
import com.crux.util.DTOList;
import com.crux.util.DateUtil;
import com.crux.util.JSPUtil;
import com.crux.util.ListUtil;
import com.crux.util.MailUtil2;
import com.crux.util.SQLUtil;
import com.webfin.ar.model.ARInvoiceView;
import com.webfin.gl.ejb.PeriodManager;
import com.webfin.gl.model.GLCostCenterView;
import com.webfin.gl.model.PeriodDetailView;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import jxl.biff.ContinueRecord;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ReceiptFacEmail implements StatefulJob {

    private final static transient LogManager logger = LogManager.getInstance(ReceiptFacEmail.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {

            if (JobUtil.isServerProduction()) {
                Calendar now = Calendar.getInstance();
                int dow = now.get(Calendar.DAY_OF_WEEK);
                int hour = now.get(Calendar.HOUR_OF_DAY);
                int hourAuto = 9;

                if (dow == Calendar.MONDAY) {
                    if (hourAuto == hour) {

                        exe_Validate_Inward();
                        exe_PremiDirectPaid();
                        exe_Validate_PremiFacCabang();

                    }
                }
            }
//            exe_PremiDirectOs();
//            exe_Validate_PremiFacCabang();
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    public void exe_PremiDirectPaid() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            logger.logInfo("execute: performing proses delete konversi file 2");

            long t = System.currentTimeMillis();

            final Statement S = conn.createStatement();

            final DTOList file = ListUtil.getDTOListFromQuery(
                    " SELECT A.AR_INVOICE_ID,A.INVOICE_NO,A.ATTR_POL_ID,A.ATTR_POL_NO,A.AMOUNT,"
                    + "A.AMOUNT_SETTLED,A.receipt_date,A.receipt_no,A.notif_flag "
                    + "FROM AR_INVOICE a "
                    + "WHERE A.AR_TRX_TYPE_ID in (5,6,7) AND A.AMOUNT_SETTLED is not null "
                    + "AND COALESCE(A.CANCEL_FLAG,'') <> 'Y' and A.notif_flag is null "
                    //                    + "and date_trunc('day',receipt_date) >= '" + DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-01 00:00:00'"
                    + "and date_trunc('day',receipt_date) >= '2020-01-01 00:00:00'"
                    + "and date_trunc('day',receipt_date) <= '" + new Date() + "' "
                    + "AND ATTR_POL_no IN (SELECT A.ATTR_POL_NO FROM AR_INVOICE a "
                    + "WHERE A.AR_TRX_TYPE_ID = 13 and a.refd1 = 'FAC' AND COALESCE(A.CANCEL_FLAG,'') <> 'Y' "
                    + "and date_trunc('day',a.mutation_date) >= '2020-01-01 00:00:00' "
                    + "and date_trunc('day',a.mutation_date) <= '" + new Date() + "' "
                    + "group by A.ATTR_POL_NO) ORDER BY a.receipt_date,a.invoice_no ",
                    ARInvoiceView.class);

            logger.logDebug("################### : " + file.size());

            String receiver = Parameter.readString("FINANCE_EMAIL");
//            String receiver = "prasetyo.dwi@askrida.com";
//            String receiverCc = "prasetyo@askrida.co.id,bisyaprasdepe@gmail.com";
            String subject = "Pembayaran Premi Polis Facultatif";
            String text = "Dengan hormat,\n\n"
                    + "Bersama ini kami infokan bahwa polis-polis dibawah ini Premi Direct telah dibayar dan terdapat Facultatif.\n\n";

            for (int i = 0; i < file.size(); i++) {
                ARInvoiceView inv = (ARInvoiceView) file.get(i);

                text = text + "Nopolis " + inv.getStInvoiceNo() + " No Bukti " + inv.getStReceiptNo() + ".\n";

                updateFlagPosting(inv);

            }

            text = text + "\nAtas perhatian dan kerjasamanya, kami ucapkan terima kasih.\n\n\n"
                    + "Hormat kami,\n"
                    + "Administrator";

            MailUtil2 mail = new MailUtil2();
            if (file.size() > 0) {
                mail.sendEmail(receiver, subject, text);
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

    public void exe_PremiDirectOs() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            logger.logInfo("execute: performing proses delete konversi file 2");

            long t = System.currentTimeMillis();

            final Statement S = conn.createStatement();

            int bulanCodeNext;
            int tahunCodeNext;
            int bulanCode = DateUtil.getMonthDigit(new Date());
            int tahunCode = Integer.parseInt(DateUtil.getYear(new Date()));
//            int bulanCode = 12;
//            int tahunCode = 2023;

            Date dateStart = null;
            Date dateEnd = null;
            if (bulanCode == 12) {
                tahunCodeNext = tahunCode + 1;

                dateStart = DateUtil.getDate("01/01/" + tahunCodeNext);
                dateEnd = DateUtil.getDate("31/01/" + tahunCodeNext);
            } else if (bulanCode < 12) {
                bulanCodeNext = bulanCode + 1;

                PeriodDetailView pd = PeriodManager.getInstance().getPeriod(String.valueOf(bulanCodeNext), DateUtil.getYear(new Date()));
                dateStart = pd.getDtStartDate();
                dateEnd = pd.getDtEndDate();
            }

            final DTOList file = ListUtil.getDTOListFromQuery(
                    "SELECT a.mutation_date,A.ATTR_POL_ID,A.ATTR_POL_NO,b.short_name as attr_pol_name,A.AMOUNT "
                    + "FROM AR_INVOICE a inner join ent_master b on b.ent_id = a.ent_id "
                    + "WHERE A.AR_TRX_TYPE_ID in (5,6,7) AND COALESCE(A.CANCEL_FLAG,'') <> 'Y' AND A.AMOUNT_SETTLED is null "
                    + "AND COALESCE(A.CANCEL_FLAG,'') <> 'Y' and a.refid0 like 'PREMI%' "
                    + "and date_trunc('day',mutation_date) >= '2020-01-01 00:00:00' "
                    + "and date_trunc('day',mutation_date) <= '" + dateEnd + "' "
                    + "AND ATTR_POL_no IN (SELECT A.ATTR_POL_NO FROM AR_INVOICE a "
                    + "WHERE A.AR_TRX_TYPE_ID = 13 and a.refd1 = 'FAC' AND COALESCE(A.CANCEL_FLAG,'') <> 'Y' "
                    + "group by A.ATTR_POL_NO) group by a.mutation_date,A.ATTR_POL_ID,A.ATTR_POL_NO,b.short_name,A.AMOUNT "
                    + "ORDER BY a.mutation_date,A.ATTR_POL_NO ",
                    ARInvoiceView.class);

            logger.logDebug("################### : " + file.size());

            String receiver = "inkaso@askrida.com";
            String receiverCc = "underwriting.dept@askrida.com,akseptasiumum.askrida@gmail.com";
//            String receiver = "prasetyo.dwi@askrida.com,prasetyo@askrida.co.id";
            String subject = "Laporan OS Premi Direct (Facultatif)";
            String text = "Dengan hormat,\n"
                    + "Bersama ini kami informasikan bahwa polis-polis Direct yang masih Outstanding per " + DateUtil.getDateStr(dateEnd, "dd-MMM-yyyy") + ", yaitu: \n\n";

            int no;
            for (int i = 0; i < file.size(); i++) {
                ARInvoiceView inv = (ARInvoiceView) file.get(i);

                no = i + 1;
                text = text + no + ". No Polis. " + inv.getStAttrPolicyNo() + " " + inv.getStAttrPolicyName() + " Premi " + JSPUtil.print(inv.getDbAmount(), 2) + ", tanggal polis " + DateUtil.getDateStr(inv.getDtMutationDate(), "dd-MMM-yyyy") + ".\n";

            }

            text = text + "\n Mohon untuk dibayarkan segera.\n"
                    + "Atas perhatian dan kerjasamanya, kami ucapkan terima kasih.\n\n\n"
                    + "Hormat kami,\n"
                    + "Administrator";

            MailUtil2 mail = new MailUtil2();
            mail.sendEmailCc(receiver, receiverCc, subject, text);

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 2 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }
    }

    public void exe_Validate_PremiFacCabang() throws Exception {

        int bulanCodeNext;
        int tahunCodeNext;
        int bulanCode = DateUtil.getMonthDigit(new Date());
        int tahunCode = Integer.parseInt(DateUtil.getYear(new Date()));

        Date dateStart = null;
        Date dateEnd = null;
        if (bulanCode == 12) {
            tahunCodeNext = tahunCode + 1;

            dateStart = DateUtil.getDate("01/01/" + tahunCodeNext);
            dateEnd = DateUtil.getDate("31/01/" + tahunCodeNext);
        } else if (bulanCode < 12) {
            bulanCodeNext = bulanCode + 1;

            PeriodDetailView pd = PeriodManager.getInstance().getPeriod(String.valueOf(bulanCodeNext), DateUtil.getYear(new Date()));
            dateStart = pd.getDtStartDate();
            dateEnd = pd.getDtEndDate();
        }

        String query = "select a.cc_code,a.cc_code_source as sub_code from ( "
                + "select a.cc_code,b.cc_code_source,sum(a.amount) as amount FROM AR_INVOICE a "
                + "inner join ins_policy b on b.pol_id = a.ATTR_POL_ID "
                + "WHERE A.AR_TRX_TYPE_ID in (5,6,7) AND COALESCE(A.CANCEL_FLAG,'') <> 'Y' AND A.AMOUNT_SETTLED is null "
                + "AND COALESCE(A.CANCEL_FLAG,'') <> 'Y' and a.refid0 like 'PREMI%' "
                //                + "and date_trunc('day',mutation_date) >= '2020-01-01 00:00:00' "
                //                + "and date_trunc('day',mutation_date) <= '" + dateEnd + "' "
                + "AND ATTR_POL_no IN (SELECT A.ATTR_POL_NO FROM AR_INVOICE a "
                + "WHERE A.AR_TRX_TYPE_ID = 13 and a.refd1 = 'FAC' AND COALESCE(A.CANCEL_FLAG,'') <> 'Y' "
                + "and date_trunc('day',a.mutation_date) >= '2020-01-01 00:00:00' "
                + "and date_trunc('day',a.mutation_date) <= '" + dateEnd + "' "
                + "group by A.ATTR_POL_NO) group BY a.cc_code,b.cc_code_source ORDER BY a.cc_code "
                + ") a where amount <> 0 group BY a.cc_code,a.cc_code_source ORDER BY a.cc_code ";

        logger.logDebug("$$$$$$$$$$exe_Validate_PremiFacCabang " + query);

        DTOList cabang = ListUtil.getDTOListFromQuery(
                query,
                GLCostCenterView.class);

        String koda = null;
        for (int i = 0; i < cabang.size(); i++) {
            GLCostCenterView cab = (GLCostCenterView) cabang.get(i);

            if (cab.getStCostCenterCode().equalsIgnoreCase("80")) {
                koda = cab.getStSubCostCenterCode();
            } else {
                koda = cab.getStCostCenterCode();
            }

            exe_PremiFacCabang(koda);

        }
    }

    public void exe_PremiFacCabang(String koda) throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            logger.logInfo("execute: performing proses delete konversi file 2");

            long t = System.currentTimeMillis();

            final Statement S = conn.createStatement();

            int bulanCodeNext;
            int tahunCodeNext;
            int bulanCode = DateUtil.getMonthDigit(new Date());
            int tahunCode = Integer.parseInt(DateUtil.getYear(new Date()));
//            int bulanCode = 12;
//            int tahunCode = 2023;

            Date dateStart = null;
            Date dateEnd = null;
            if (bulanCode == 12) {
                tahunCodeNext = tahunCode + 1;

                dateStart = DateUtil.getDate("01/01/" + tahunCodeNext);
                dateEnd = DateUtil.getDate("31/01/" + tahunCodeNext);
            } else if (bulanCode < 12) {
                bulanCodeNext = bulanCode + 1;

                PeriodDetailView pd = PeriodManager.getInstance().getPeriod(String.valueOf(bulanCodeNext), DateUtil.getYear(new Date()));
                dateStart = pd.getDtStartDate();
                dateEnd = pd.getDtEndDate();
            }

//            final DTOList file = ListUtil.getDTOListFromQuery(
//                    "select *,DATE_PART('day', invoice_date - now()) as refnum1 from ( "
//                    + "SELECT a.cc_code,c.cc_code_source,a.attr_pol_type_id,A.AR_INVOICE_ID,A.INVOICE_NO,a.mutation_date,A.ATTR_POL_ID,A.ATTR_POL_NO,a.attr_pol_name,b.short_name as attr_quartal,"
//                    + "c.insured_amount as attr_pol_tsi_total,c.premi_total as attr_pol_tsi,A.AMOUNT,coalesce(a.refdate1,a.mutation_date) as invoice_date,a.no_surat_hutang "
//                    + "FROM AR_INVOICE a inner join ent_master b on b.ent_id = a.ent_id "
//                    + "inner join ins_policy c on c.pol_id = a.ATTR_POL_ID "
//                    + "WHERE A.AR_TRX_TYPE_ID = 13 and a.refd1 = 'FAC' AND COALESCE(A.CANCEL_FLAG,'') <> 'Y' AND A.AMOUNT_SETTLED is null "
//                    + "AND COALESCE(A.CANCEL_FLAG,'') <> 'Y' and A.notif_flag is null "
//                    + "AND ATTR_POL_no in ( SELECT A.ATTR_POL_NO FROM AR_INVOICE a "
//                    + "WHERE A.AR_TRX_TYPE_ID in (5,6,7) AND COALESCE(A.CANCEL_FLAG,'') <> 'Y' AND A.AMOUNT_SETTLED is null "
//                    + "AND COALESCE(A.CANCEL_FLAG,'') <> 'Y' and a.refid0 like 'PREMI%' AND ATTR_POL_no IN ("
//                    + "SELECT A.ATTR_POL_NO FROM AR_INVOICE a WHERE A.AR_TRX_TYPE_ID = 13 and a.refd1 = 'FAC' AND COALESCE(A.CANCEL_FLAG,'') <> 'Y' "
//                    + "group by A.ATTR_POL_NO ) group by A.ATTR_POL_NO ) "
//                    + "ORDER BY a.cc_code,a.attr_pol_type_id,a.mutation_date,A.ATTR_POL_NO,a.invoice_no,a.AR_INVOICE_ID "
//                    + " ) a where date_trunc('day',a.invoice_date) >= '2020-01-01 00:00:00' "
//                    + "and date_trunc('day',a.invoice_date) <= '" + dateEnd + "' "
//                    + "and ((a.cc_code = '" + koda + "') or (a.cc_code = '80' and a.cc_code_source = '" + koda + "'))"
//                    + "order by invoice_date desc,a.cc_code ",
//                    ARInvoiceView.class);
//
//            logger.logDebug("################### : " + file.size());
//            int no;
//            for (int i = 0; i < file.size(); i++) {
//                ARInvoiceView inv = (ARInvoiceView) file.get(i);
//
//                no = i + 1;
//                text = text + no + ". No Polis. " + inv.getStAttrPolicyNo() + " " + inv.getStAttrPolicyName() + " Premi " + JSPUtil.print(inv.getDbAmount(), 2) + ", jatuh tempo " + DateUtil.getDateStr(inv.getDtInvoiceDate(), "dd-MMM-yyyy") + ".\n";
//
//            }

            String fileName = "OS_Facultatif" + DateUtil.getDateStr(new Date(), "yyyyMMdd");
            File fo = new File("C:/");

            String fileFOlder = Parameter.readString("SYS_FILES_FOLDER");
//            String fileFOlder = "//fin-repository";
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
                    "select *,DATE_PART('day', wpc_date - now()) as sisa from ( "
                    + "SELECT a.cc_code,c.cc_code_source,a.attr_pol_type_id,A.AR_INVOICE_ID,A.INVOICE_NO,a.mutation_date,A.ATTR_POL_ID,"
                    + "A.ATTR_POL_NO,a.attr_pol_name,b.short_name as attr_quartal,c.member_ent_id,c.reins_ent_id,c.objek,"
                    + "c.insured_amount as attr_pol_tsi_total,c.premi_total as attr_pol_tsi,c.premi_fac,A.AMOUNT,a.no_surat_hutang,c.binding_date,"
                    + "coalesce(c.wpc_date,a.mutation_date) as wpc_date,c.notes,c.ri_slip_no FROM AR_INVOICE a "
                    + "inner join ( select a.pol_id,a.cc_code,a.cc_code_source,a.insured_amount,a.premi_total,b.description as objek,"
                    + "i.tsi_amount as tsi_fac,i.premi_amount as premi_fac,i.ricomm_amt as comm_fac,i.installment_count as wpc_count,i.binding_date,"
                    + "i.binding_date+ interval '1' day * i.installment_count as wpc_date,i.notes,i.ri_slip_no,i.member_ent_id,i.reins_ent_id "
                    + "from ins_policy a inner join ins_pol_obj b on b.pol_id=a.pol_id inner join ins_pol_treaty g on g.ins_pol_obj_id = b.ins_pol_obj_id "
                    + "inner join ins_pol_treaty_detail h on h.ins_pol_treaty_id = g.ins_pol_treaty_id "
                    + "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "
                    + "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id "
                    + "where a.pol_id in ( SELECT A.ATTR_POL_ID FROM AR_INVOICE a WHERE A.AR_TRX_TYPE_ID = 13 and a.refd1 = 'FAC' AND COALESCE(A.CANCEL_FLAG,'') <> 'Y' "
                    + "and date_trunc('day',a.mutation_date) >= '2020-01-01 00:00:00' "
                    + "and date_trunc('day',a.mutation_date) <= '" + dateEnd + "' "
                    + "group by A.ATTR_POL_ID ) and j.treaty_type = 'FAC' and i.premi_amount <> 0  ) c on c.pol_id = a.ATTR_POL_ID "
                    + "inner join ent_master b on b.ent_id = c.reins_ent_id "
                    + "WHERE A.AR_TRX_TYPE_ID = 13 and a.refd1 = 'FAC' AND COALESCE(A.CANCEL_FLAG,'') <> 'Y' AND A.AMOUNT_SETTLED is null "
                    + "AND COALESCE(A.CANCEL_FLAG,'') <> 'Y' and A.notif_flag is null "
                    + "AND ATTR_POL_no in ( SELECT A.ATTR_POL_NO FROM AR_INVOICE a "
                    + "WHERE A.AR_TRX_TYPE_ID in (5,6,7) AND COALESCE(A.CANCEL_FLAG,'') <> 'Y' AND A.AMOUNT_SETTLED is null "
                    + "AND COALESCE(A.CANCEL_FLAG,'') <> 'Y' and a.refid0 like 'PREMI%' AND ATTR_POL_no IN ("
                    + "SELECT A.ATTR_POL_NO FROM AR_INVOICE a WHERE A.AR_TRX_TYPE_ID = 13 and a.refd1 = 'FAC' AND COALESCE(A.CANCEL_FLAG,'') <> 'Y' "
                    + "and date_trunc('day',a.mutation_date) >= '2020-01-01 00:00:00' "
                    + "and date_trunc('day',a.mutation_date) <= '" + dateEnd + "' "
                    + "group by A.ATTR_POL_NO ) group by A.ATTR_POL_NO ) "
                    + "ORDER BY a.cc_code,a.attr_pol_type_id,a.mutation_date,A.ATTR_POL_NO,a.invoice_no,a.AR_INVOICE_ID "
                    + " ) a where date_trunc('day',a.wpc_date) >= '2020-01-01 00:00:00' "
                    + "and date_trunc('day',a.wpc_date) <= '" + dateEnd + "' "
                    + "and ((a.cc_code = '" + koda + "') or (a.cc_code = '80' and a.cc_code_source = '" + koda + "'))"
                    + "order by wpc_date desc,a.cc_code ");

            logger.logDebug("$$$$$$$$$$exe_PremiFacCabang " + query_set1);

            HSSFWorkbook wb = new HSSFWorkbook();

            //bikin sheet
            HSSFSheet sheet = wb.createSheet("OS_Facultatif");

            //bikin header
            HSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("No Polis");
            row1.createCell(1).setCellValue("Tertanggung");
            row1.createCell(2).setCellValue("Reasuradur");
            row1.createCell(3).setCellValue("Nilai Pertanggungan");
            row1.createCell(4).setCellValue("Premi Bruto");
            row1.createCell(5).setCellValue("Premi RI");
            row1.createCell(6).setCellValue("Usia");
            row1.createCell(7).setCellValue("Tanggal Konfirm");
            row1.createCell(8).setCellValue("Tanggal Jatuh Tempo");

            int i = 1;
            while (query_set1.next()) {
                HSSFRow row = sheet.createRow(i + 0);
                row.createCell(0).setCellValue(query_set1.getString("ATTR_POL_NO"));
                row.createCell(1).setCellValue(query_set1.getString("attr_pol_name"));
                row.createCell(2).setCellValue(query_set1.getString("attr_quartal"));
                row.createCell(3).setCellValue(query_set1.getDouble("attr_pol_tsi_total"));
                row.createCell(4).setCellValue(query_set1.getDouble("attr_pol_tsi"));
                row.createCell(5).setCellValue(query_set1.getDouble("AMOUNT"));
                row.createCell(6).setCellValue(query_set1.getString("sisa"));
                row.createCell(7).setCellValue(query_set1.getDate("binding_date"));
                row.createCell(8).setCellValue(query_set1.getDate("wpc_date"));
                i++;
            }

            /* Close all DB related objects */
            query_set1.close();

            FileOutputStream fop = new FileOutputStream(fo);
            wb.write(fop);
            fop.close();

            S.close();


            String receiver = getCostCenter(koda).getStEmail() + ",inkaso@askrida.com";
            String receiverCc = "underwriting.dept@askrida.com,akseptasiumum.askrida@gmail.com";
//            String receiver = "prasetyo.dwi@askrida.com";
//            String receiverCc = "prasetyo@askrida.co.id,bisyaprasdepe@gmail.com";

//              String text = "Dengan hormat,\n"
//                    + "Bersama ini kami informasikan bahwa polis-polis Fakultatif yang masih Outstanding per " + DateUtil.getDateStr(dateEnd, "dd-MMM-yyyy") + ", yaitu: \n\n";
            String subject = "Tagihan Atas Polis yang Memiliki Back Up Reasuransi Facultatif";
            String text = "Kepada Yth,\n"
                    + "Kepala Cabang Askrida " + getCostCenter(koda).getStDescription() + "\n"
                    + "di \n"
                    + "Tempat \n\n"
                    + "Dengan hormat,\n"
                    + "Sehubungan dengan terdapatnya outstanding premi atas beberapa polis yang memiliki kewajiban reasuransi fakultatif, "
                    + "serta mengingat jatuh tempo pembayaran premi reasuransi yang semakin dekat, bersama ini kami sampaikan agar memberikan "
                    + "konfirmasi pembayaran atas polis-polis tersebut serta direalisasi sebelum jatuh tempo.\n\n"
                    + "Atas hal tersebut kami menunggu konfirmasi penyelesaian sampai dengan 15 hari setelah surat ini kami sampaikan. "
                    + "Apabila dalam 15 hari belum terdapat pembayaran dari Tertanggung mohon untuk dapat menyampaikan permohonan penundaan "
                    + "pembayaran premi sampai dengan Tertanggung dapat menyelesaikan pembayaran premi tersebut.\n"
                    + "Demikian kami sampaikan, atas perhatian dan kerja samanya kami ucapkan terima kasih.\n\n\n"
                    + "Hormat kami,\n"
                    + "Bagian Inkaso Reasuransi\n"
                    + "Divisi Keuangan & Investasi";

            MailUtil2 mail = new MailUtil2();
//            mail.sendEmailCc(receiver, receiverCc, subject, text);
            mail.sendEmailWithTypeCc(receiver, receiverCc, subject, text, pathTemp, fileName, ".xls");

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 2 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }
    }

    public GLCostCenterView getCostCenter(String koda) {
        final GLCostCenterView costcenter = (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, koda);

        return costcenter;
    }

    public void exe_Validate_Inward() throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            logger.logInfo("execute: performing proses delete konversi file 2");

            long t = System.currentTimeMillis();

            final Statement S = conn.createStatement();

            int bulanCodeNext;
            int tahunCodeNext;
            int bulanCode = DateUtil.getMonthDigit(new Date());
            int tahunCode = Integer.parseInt(DateUtil.getYear(new Date()));
//            int bulanCode = 12;
//            int tahunCode = 2023;

            Date dateStart = null;
            Date dateEnd = null;
            if (bulanCode == 12) {
                tahunCodeNext = tahunCode + 1;

                dateStart = DateUtil.getDate("01/01/" + tahunCodeNext);
                dateEnd = DateUtil.getDate("31/01/" + tahunCodeNext);
            } else if (bulanCode < 12) {
                bulanCodeNext = bulanCode + 1;

                PeriodDetailView pd = PeriodManager.getInstance().getPeriod(String.valueOf(bulanCodeNext), DateUtil.getYear(new Date()));
                dateStart = pd.getDtStartDate();
                dateEnd = pd.getDtEndDate();
            }

            String fileName = "Inward_" + DateUtil.getDateStr(new Date(), "yyyyMMdd");
            File fo = new File("C:/");

            String fileFOlder = Parameter.readString("SYS_FILES_FOLDER");
//            String fileFOlder = "//fin-repository";
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
                    "  select * from ( select a.ar_invoice_id as invid,a.attr_pol_name as akun,a.invoice_no as nobukti,a.attr_pol_per_0 as tglawal,a.attr_pol_per_1 as tglakhir,"
                    + "a.attr_pol_tsi as askrida_share,a.ccy as curr,a.ccy_rate as rate,a.amount as tagihan,b.premi_bruto as tag_berjalan,"
                    + "b.inst_date as due_date,ROW_NUMBER() OVER(PARTITION BY a.ar_invoice_id ORDER BY a.ar_invoice_id,b.ins_pol_inward_inst_id)||'/'||(select count(x.ar_invoice_id) from ins_pol_inward_installment x where x.ar_invoice_id = a.ar_invoice_id) as inst,"
                    + "b.ins_pol_inward_inst_id as invdetid "
                    + "from ins_pol_inward a "
                    + "inner join ins_pol_inward_installment b on b.ar_invoice_id = a.ar_invoice_id "
                    + "where a.ar_trx_type_id in (1,2,3) and coalesce(a.cancel_flag,'') <> 'Y' "
                    + "and date_trunc('day',a.mutation_date) >= '2020-01-01 00:00:00' "
                    + "and date_trunc('day',a.mutation_date) <= '" + dateEnd + "' "
                    + "ORDER BY a.ar_invoice_id,b.ins_pol_inward_inst_id "
                    + ") a where date_trunc('day',CURRENT_DATE) between a.due_date - INTERVAL '14 days' and a.due_date + INTERVAL '5 days' "
                    + "ORDER BY a.invid,a.invdetid ");

            logger.logDebug("$$$$$$$$$$exe_Validate_Inward " + query_set1);

            HSSFWorkbook wb = new HSSFWorkbook();

            //bikin sheet
            HSSFSheet sheet = wb.createSheet("inward");

            //bikin header
            HSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("NAMA");
            row1.createCell(1).setCellValue("NOBUKTI");
            row1.createCell(2).setCellValue("POI START");
            row1.createCell(3).setCellValue("POI END");
            row1.createCell(5).setCellValue("TSI ASKRIDA SHARE");
            row1.createCell(6).setCellValue("CURR");
            row1.createCell(7).setCellValue("RATE");
            row1.createCell(8).setCellValue("JUMLAH TAGIHAN");
            row1.createCell(9).setCellValue("TAGIHAN BERJALAN");
            row1.createCell(10).setCellValue("INST");
            row1.createCell(11).setCellValue("DUE DATE");

            int i = 1;
            while (query_set1.next()) {
                HSSFRow row = sheet.createRow(i + 0);
                row.createCell(0).setCellValue(query_set1.getString("akun"));
                row.createCell(1).setCellValue(query_set1.getString("nobukti"));
                row.createCell(3).setCellValue(query_set1.getDate("tglawal"));
                row.createCell(4).setCellValue(query_set1.getDate("tglakhir"));
                row.createCell(5).setCellValue(query_set1.getDouble("askrida_share"));
                row.createCell(6).setCellValue(query_set1.getString("curr"));
                row.createCell(7).setCellValue(query_set1.getDouble("rate"));
                row.createCell(8).setCellValue(query_set1.getDouble("tagihan"));
                row.createCell(9).setCellValue(query_set1.getDouble("tag_berjalan"));
                row.createCell(10).setCellValue(query_set1.getString("inst"));
                row.createCell(11).setCellValue(query_set1.getDate("due_date"));
                i++;
            }

            /* Close all DB related objects */
            query_set1.close();

            FileOutputStream fop = new FileOutputStream(fo);
            wb.write(fop);
            fop.close();

            S.close();


            String receiver = "reas@askrida.com";
            String receiverCc = "divisi.keuangan@askrida.com,akuntansi@askrida.com,divisi.klaim@askrida.com";
//            String receiver = "prasetyo.dwi@askrida.com";
//            String receiverCc = "prasetyo@askrida.co.id,bisyaprasdepe@gmail.com";

            String subject = "Produksi Inward H-14 sebelum Jatuh Tempo";
            String text = "Kepada Yth,\n"
                    + "Divisi Keuangan \n\n"
                    + "Dengan hormat,\n"
                    + "Terlampir adalah produksi inward yang akan Jatuh Tempo.\n\n"
                    + "Demikian kami sampaikan, atas perhatian dan kerja samanya kami ucapkan terima kasih.\n\n\n"
                    + "Hormat kami,\n"
                    + "Divisi Akseptasi & Reasuransi";

            MailUtil2 mail = new MailUtil2();
//            mail.sendEmailCc(receiver, receiverCc, subject, text);
            mail.sendEmailWithTypeCc(receiver, receiverCc, subject, text, pathTemp, fileName, ".xls");

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 2 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }
    }
}
