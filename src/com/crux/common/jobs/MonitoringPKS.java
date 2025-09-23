/***********************************************************************
 * Module:  com.crux.common.jobs.AutoReporting
 * Author:  Denny Mahendra
 * Created: Jul 12, 2004 12:24:48 PM
 * Purpose:
 ***********************************************************************/
package com.crux.common.jobs;

import com.crux.common.controller.Helper;
import com.crux.common.parameter.Parameter;
import com.crux.jobs.util.JobUtil;
import com.crux.lang.LanguageManager;
import com.crux.pool.DTOPool;
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
import com.webfin.gl.ejb.PeriodManager;
import com.webfin.gl.model.GLCostCenterView;
import com.webfin.gl.model.PeriodDetailView;
import com.webfin.pks.model.PerjanjianKerjasamaView;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.joda.time.DateTime;

public class MonitoringPKS extends Helper implements StatefulJob {

    private final static transient LogManager logger = LogManager.getInstance(MonitoringPKS.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {

            if (JobUtil.isServerProduction()) {
                Calendar now = Calendar.getInstance();
                int hour = now.get(Calendar.HOUR_OF_DAY);
                int hourAuto = 2;

                if (hourAuto == hour) {
                    execute1();
                    executeKaOps();
                }
            }

//            execute5();
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

            String policyDateStart = null;
            String policyDateEnd = null;

            int bulanCode = DateUtil.getMonthDigit(new Date());

            if (bulanCode == 11 || bulanCode == 12) {
                int tahunCode = Integer.parseInt(DateUtil.getYear(new Date())) + 1;

                if (bulanCode == 11) {
                    policyDateStart = DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-01 00:00:00";
                    policyDateEnd = tahunCode + "-01-31 00:00:00";
                } else if (bulanCode == 12) {
                    policyDateStart = DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-01 00:00:00";
                    policyDateEnd = tahunCode + "-02-28 00:00:00";
                }
            } else {
                int bulanCodeNow = bulanCode + 2;

                PeriodDetailView pd = PeriodManager.getInstance().getPeriod(String.valueOf(bulanCodeNow), DateUtil.getYear(new Date()));

                Date dateEnd = pd.getDtEndDate();

                policyDateStart = DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-01 00:00:00";
                policyDateEnd = dateEnd.toString();
            }

            final DTOList file = ListUtil.getDTOListFromQuery(
                    " select a.pks_id,a.pol_no,a.bank_no,a.policy_date,a.receive_date,"
                    + "a.period_start,a.period_end,a.cust_name,a.cust_address,a.description "
                    + "from perjanjian_kerjasama a "
                    + "where date_trunc('day',a.period_end) >= ? "
                    + "and date_trunc('day',a.period_end) <= ? "
                    + "and a.posted_flag is null "
                    + "order by root_id desc, create_date desc ",
                    new Object[]{policyDateStart, policyDateEnd},
                    PerjanjianKerjasamaView.class);

            logger.logDebug("################### : " + file.size());

            String fileName = "reminderPKS_" + DateUtil.getDateStr(new Date(), "yyyyMMdd");
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
                    " select a.pks_id,a.pol_no,a.bank_no,a.policy_date,a.receive_date,"
                    + "a.period_start,a.period_end,a.cust_name,a.cust_address,a.description "
                    + "from perjanjian_kerjasama a "
                    + "where date_trunc('day',a.period_end) >= '" + policyDateStart + "' "
                    + "and date_trunc('day',a.period_end) <= '" + policyDateEnd + "' "
                    + "and a.posted_flag is null "
                    + "order by root_id desc, create_date desc ");

            HSSFWorkbook wb = new HSSFWorkbook();

            //bikin sheet
            HSSFSheet sheet = wb.createSheet("reminderPKS");

            //bikin header
            HSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("ID");
            row1.createCell(1).setCellValue("PKS ASKRIDA");
            row1.createCell(2).setCellValue("PKS BANK");
            row1.createCell(3).setCellValue("PERIODE MULAI");
            row1.createCell(4).setCellValue("PERIODE AKHIR");
            row1.createCell(5).setCellValue("TERTANGGUNG");
            row1.createCell(6).setCellValue("KETERANGAN");

            int i = 1;
            while (query_set1.next()) {
                HSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(query_set1.getString("pks_id"));
                row.createCell(1).setCellValue(query_set1.getString("pol_no"));
                row.createCell(2).setCellValue(query_set1.getString("bank_no"));
                row.createCell(3).setCellValue(query_set1.getDate("period_start"));
                row.createCell(4).setCellValue(query_set1.getDate("period_end"));
                row.createCell(5).setCellValue(query_set1.getString("cust_name"));
                row.createCell(6).setCellValue(query_set1.getString("description"));
                i++;
            }

            /* Close all DB related objects */
            query_set1.close();

            FileOutputStream fop = new FileOutputStream(fo);
            wb.write(fop);
            fop.close();

            S.close();

            String receiver = Parameter.readString("EMAIL_PKS");
//            String receiver = "prasetyo@askrida.co.id";
            String subject = "Reminder PKS bulan " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "^^ yyyy"));
            String text = "Kepada Yth.\n"
                    + "Divisi Sekretariat Perusahaan\n"
                    + "Di Tempat\n\n\n"
                    + "Dengan hormat,\n\n"
                    + "Bersama ini kami infokan, PKS yang akan berakhir dalam 3 bulan ke depan yang diproses secara otomatis oleh Sistem.\n"
                    + "Demikian disampaikan dan semoga laporan tersebut di atas dapat menambah informasi, terima kasih.\n\n\n"
                    + "Hormat kami,\n"
                    + "Administrator";

            MailUtil2 mail = new MailUtil2();
            mail.sendEmailWithType(pathTemp, fileName, receiver, subject, text, ".xls");

            for (int j = 0; j < file.size(); j++) {
                PerjanjianKerjasamaView rcl = (PerjanjianKerjasamaView) file.get(j);

                updatePKS(rcl);

            }

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 2 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }
    }

    public void executeKaOps() throws Exception {

        String policyDateStart = null;
        String policyDateEnd = null;

        int bulanCode = DateUtil.getMonthDigit(new Date());

        if (bulanCode == 11 || bulanCode == 12) {
            int tahunCode = Integer.parseInt(DateUtil.getYear(new Date())) + 1;

            if (bulanCode == 11) {
                policyDateStart = DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-01 00:00:00";
                policyDateEnd = tahunCode + "-01-31 00:00:00";
            } else if (bulanCode == 12) {
                policyDateStart = DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-01 00:00:00";
                policyDateEnd = tahunCode + "-02-28 00:00:00";
            }
        } else {
            int bulanCodeNow = bulanCode + 2;

            PeriodDetailView pd = PeriodManager.getInstance().getPeriod(String.valueOf(bulanCodeNow), DateUtil.getYear(new Date()));

            Date dateEnd = pd.getDtEndDate();

            policyDateStart = DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-01 00:00:00";
            policyDateEnd = dateEnd.toString();
        }

        String query =
                " select a.pks_id,a.pol_no,a.cc_code,a.bank_no,a.policy_date,a.receive_date,"
                + "a.period_start,a.period_end,a.cust_name,a.cust_address,a.description "
                + "from perjanjian_kerjasama a "
                + "where date_trunc('day',a.period_end) >= '" + policyDateStart + "' "
                + "and date_trunc('day',a.period_end) <= '" + policyDateEnd + "' "
                + "and a.posted_flag is null "
                + "order by root_id desc, create_date desc ";

        DTOList cabang = null;
        cabang = ListUtil.getDTOListFromQuery(
                "select a.cc_code from ( "
                + query + " ) a group by a.cc_code order by a.cc_code ",
                GLCostCenterView.class);

        for (int i = 0; i < cabang.size(); i++) {
            GLCostCenterView cab = (GLCostCenterView) cabang.get(i);

            execute2(cab.getStCostCenterCode());

        }
    }

    public void execute2(String koda) throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {
            logger.logInfo("execute: performing proses delete konversi file 2");

            long t = System.currentTimeMillis();

            final Statement S = conn.createStatement();

            String policyDateStart = null;
            String policyDateEnd = null;

            int bulanCode = DateUtil.getMonthDigit(new Date());

            if (bulanCode == 11 || bulanCode == 12) {
                int tahunCode = Integer.parseInt(DateUtil.getYear(new Date())) + 1;

                if (bulanCode == 11) {
                    policyDateStart = DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-01 00:00:00";
                    policyDateEnd = tahunCode + "-01-31 00:00:00";
                } else if (bulanCode == 12) {
                    policyDateStart = DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-01 00:00:00";
                    policyDateEnd = tahunCode + "-02-28 00:00:00";
                }
            } else {
                int bulanCodeNow = bulanCode + 2;

                PeriodDetailView pd = PeriodManager.getInstance().getPeriod(String.valueOf(bulanCodeNow), DateUtil.getYear(new Date()));

                Date dateEnd = pd.getDtEndDate();

                policyDateStart = DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-01 00:00:00";
                policyDateEnd = dateEnd.toString();
            }

            final DTOList file = ListUtil.getDTOListFromQuery(
                    " select a.pks_id,a.pol_no,a.cc_code,a.bank_no,a.policy_date,a.receive_date,"
                    + "a.period_start,a.period_end,a.cust_name,a.cust_address,a.description "
                    + "from perjanjian_kerjasama a "
                    + "where date_trunc('day',a.period_end) >= ? "
                    + "and date_trunc('day',a.period_end) <= ? "
                    + "and a.posted_flag is null and a.cc_code = '" + koda + "' "
                    + "order by root_id desc, create_date desc ",
                    new Object[]{policyDateStart, policyDateEnd},
                    PerjanjianKerjasamaView.class);

            logger.logDebug("################### : " + file.size());

            String fileName = "reminderPKS_" + DateUtil.getDateStr(new Date(), "yyyyMMdd");
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
                    " select a.pks_id,a.pol_no,a.bank_no,a.policy_date,a.receive_date,"
                    + "a.period_start,a.period_end,a.cust_name,a.cust_address,a.description "
                    + "from perjanjian_kerjasama a "
                    + "where date_trunc('day',a.period_end) >= '" + policyDateStart + "' "
                    + "and date_trunc('day',a.period_end) <= '" + policyDateEnd + "' "
                    + "and a.posted_flag is null and a.cc_code = '" + koda + "' "
                    + "order by root_id desc, create_date desc ");

            HSSFWorkbook wb = new HSSFWorkbook();

            //bikin sheet
            HSSFSheet sheet = wb.createSheet("reminderPKS");

            //bikin header
            HSSFRow row1 = sheet.createRow(0);
            row1.createCell(0).setCellValue("ID");
            row1.createCell(1).setCellValue("PKS ASKRIDA");
            row1.createCell(2).setCellValue("PKS BANK");
            row1.createCell(3).setCellValue("PERIODE MULAI");
            row1.createCell(4).setCellValue("PERIODE AKHIR");
            row1.createCell(5).setCellValue("TERTANGGUNG");
            row1.createCell(6).setCellValue("KETERANGAN");

            int i = 1;
            while (query_set1.next()) {
                HSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(query_set1.getString("pks_id"));
                row.createCell(1).setCellValue(query_set1.getString("pol_no"));
                row.createCell(2).setCellValue(query_set1.getString("bank_no"));
                row.createCell(3).setCellValue(query_set1.getDate("period_start"));
                row.createCell(4).setCellValue(query_set1.getDate("period_end"));
                row.createCell(5).setCellValue(query_set1.getString("cust_name"));
                row.createCell(6).setCellValue(query_set1.getString("description"));
                i++;
            }

            /* Close all DB related objects */
            query_set1.close();

            FileOutputStream fop = new FileOutputStream(fo);
            wb.write(fop);
            fop.close();

            S.close();

//            String receiver = Parameter.readString("EMAIL_PKS");
//                String receiver = "prasetyo@askrida.co.id";
            String receiver = getCostCenter(koda).getStEmail() + "," + getCostCenter(koda).getStEmail2();
            String subject = "Reminder PKS bulan " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "^^ yyyy"));
            String text = "Kepada Yth.\n"
                    + "Divisi Sekretariat Perusahaan Cabang " + getCostCenter(koda).getStDescription() + " \n"
                    + "Di Tempat\n\n\n"
                    + "Dengan hormat,\n\n"
                    + "Bersama ini kami infokan, PKS yang akan berakhir dalam 3 bulan ke depan yang diproses secara otomatis oleh Sistem.\n"
                    + "Demikian disampaikan dan semoga laporan tersebut di atas dapat menambah informasi, terima kasih.\n\n\n"
                    + "Hormat kami,\n"
                    + "Administrator";

            MailUtil2 mail = new MailUtil2();
            mail.sendEmailWithType(pathTemp, fileName, receiver, subject, text, ".xls");

            for (int j = 0; j < file.size(); j++) {
                PerjanjianKerjasamaView rcl = (PerjanjianKerjasamaView) file.get(j);

                updatePKS(rcl);

            }

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 2 selesai dalam " + t + " ms");

        } finally {
            conn.close();
        }
    }

    public void updatePKS(PerjanjianKerjasamaView rcl) throws Exception {

        final SQLUtil S = new SQLUtil();
        try {

            PreparedStatement P = S.setQuery("update perjanjian_kerjasama set posted_flag = 'Y' where pks_id = ? ");

            P.setObject(1, rcl.getStPolicyID());

            int hasil = P.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            S.release();
        }
    }

    public void execute5() throws Exception {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int hourAuto = 7;

//        if (hourAuto == hour) {

        logger.logInfo("execute: performing proses delete konversi file 2");

        long t = System.currentTimeMillis();

        String policyDateStart = null;
        String policyDateEnd = null;

        int bulanCode = DateUtil.getMonthDigit(new Date());

        if (bulanCode == 12) {
            int tahunCode = Integer.parseInt(DateUtil.getYear(new Date())) + 1;

            policyDateStart = tahunCode + "-01-01 00:00:00";
            policyDateEnd = tahunCode + "-01-31 00:00:00";
        } else {
            int bulanCodeNow = bulanCode;

            PeriodDetailView pd = PeriodManager.getInstance().getPeriod(String.valueOf(bulanCodeNow), DateUtil.getYear(new Date()));

            Date dateStart = pd.getDtStartDate();
            Date dateEnd = pd.getDtEndDate();

//            policyDateStart = DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(dateStart) + "-01 00:00:00";
            policyDateStart = dateStart.toString();
            policyDateEnd = dateEnd.toString();
        }

        final DTOList file = ListUtil.getDTOListFromQuery(
                " select a.* from ar_invoice a "
                + "where date_trunc('day',a.due_date) >= ? "
                + "and date_trunc('day',a.due_date) <= ? "
                + "and a.ar_trx_type_id in (1,2,3,20,21,22) "
                + "order by ar_invoice_id  ",
                new Object[]{policyDateStart, policyDateEnd},
                ARInvoiceView.class);

        String receiver = Parameter.readString("EMAIL_PKS");
//        String receiver = "prasetyo@askrida.co.id";
        String subject = "Reminder OS Reas bulan " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "^^ yyyy"));
        String text = "Kepada Yth.\n"
                + "Ibu. Vika \n\n\n"
                + "Dengan hormat,\n\n"
                + "Bersama ini kami infokan, pembayaran reas outward dan inward yang masih Outstanding pada bulan ini.\n\n";

        for (int j = 0; j < file.size(); j++) {
            ARInvoiceView inv = (ARInvoiceView) file.get(j);

            text = text + DateUtil.getDateStr(inv.getDtDueDate(), "dd ^^ yyyy") + " - " + inv.getStInvoiceNo() + " - " + inv.getDbAmount() + "\n";

        }

        text = text + "\nDemikian disampaikan dan semoga laporan tersebut di atas dapat menambah informasi, terima kasih.\n\n\n"
                + "Hormat kami,\n"
                + "Administrator";

        MailUtil2 mail = new MailUtil2();
        mail.sendEmail(receiver, subject, text);

        t = System.currentTimeMillis() - t;

        logger.logInfo("proses 2 selesai dalam " + t + " ms");

//        }
    }

    public GLCostCenterView getCostCenter(String koda) {
        final GLCostCenterView costcenter = (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, koda);

        return costcenter;
    }
}
