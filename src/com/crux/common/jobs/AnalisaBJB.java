/***********************************************************************
 * Module:  com.crux.common.jobs.AnalisaBJB
 * Author:  Denny Mahendra
 * Created: Jul 12, 2004 12:24:48 PM
 * Purpose:
 ***********************************************************************/
package com.crux.common.jobs;

import com.crux.common.model.HashDTO;
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
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AnalisaBJB implements StatefulJob {

    private final static transient LogManager logger = LogManager.getInstance(AnalisaBJB.class);

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

            String fileName = "analisaBJB_" + DateUtil.getDateStr(new Date(), "yyyyMMdd");
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

            final DTOList file = ListUtil.getDTOListFromQueryDS(
                    " select distinct a.* from ("
                    + "select a.cust_name,a.ins_pol_obj_id,a.description,a.period_start,a.period_end,"
                    + "a.marketer,a.tsi_obj,a.premi_obj,a.pol_id,a.pol_no,a.cob,a.policy_date,a.status,"
                    + "b.policy_date||'|'||b.description||'|'||b.premi_obj_rev||'|'||b.status as endorse, "
                    + "c.tgl_klaim||'|'||c.cause_desc||'|'||c.dla_date||'|'||c.claim_amount as klaim "
                    + "from data_bjb_polis a "
                    + "left join data_endorse b on b.sub_polno||b.description = substr(a.pol_no,2,16)||a.description "
                    + "left join data_bjb_klaim c on substr(c.pol_no,2,16)||c.description = substr(a.pol_no,2,16)||a.description "
                    + "where a.pol_type_id in (1,81,3,24) "
                    + "and date_trunc('day',a.policy_date) >= date_trunc('month', current_date - interval '1' month) "
                    + "and date_trunc('day',a.policy_date) < date_trunc('month', current_date) "
                    + "order by a.policy_date,a.pol_id,a.ins_pol_obj_id ) a  "
                    + "order by a.policy_date,a.pol_id,a.ins_pol_obj_id ",
                    HashDTO.class, "WHouseDS");

            logger.logDebug("################### : " + file.size());

            XSSFWorkbook wb = new XSSFWorkbook();

            //bikin sheet
            XSSFSheet sheet = wb.createSheet("new sheet");

            for (int i = 0; i < file.size(); i++) {
                HashDTO h = (HashDTO) file.get(i);

                //bikin header
                XSSFRow row0 = sheet.createRow(0);
                row0.createCell(0).setCellValue("cust_name");
                row0.createCell(1).setCellValue("ins_pol_obj_id");
                row0.createCell(2).setCellValue("description");
                row0.createCell(3).setCellValue("period_start");
                row0.createCell(4).setCellValue("period_end");
                row0.createCell(5).setCellValue("marketer");
                row0.createCell(6).setCellValue("tsi_obj");
                row0.createCell(7).setCellValue("premi_obj");
                row0.createCell(8).setCellValue("pol_id");
                row0.createCell(9).setCellValue("pol_no");
                row0.createCell(10).setCellValue("cob");
                row0.createCell(11).setCellValue("policy_date");
                row0.createCell(12).setCellValue("status");
                row0.createCell(13).setCellValue("endorse");
                row0.createCell(14).setCellValue("klaim");

                //bikin isi cell
                XSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(h.getFieldValueByFieldNameST("cust_name"));
                row.createCell(1).setCellValue(h.getFieldValueByFieldNameBD("ins_pol_obj_id").doubleValue());
                row.createCell(2).setCellValue(h.getFieldValueByFieldNameST("description"));
                row.createCell(3).setCellValue(h.getFieldValueByFieldNameDT("period_start"));
                row.createCell(4).setCellValue(h.getFieldValueByFieldNameDT("period_end"));
                row.createCell(5).setCellValue(h.getFieldValueByFieldNameST("marketer"));
                row.createCell(6).setCellValue(h.getFieldValueByFieldNameBD("tsi_obj").doubleValue());
                row.createCell(7).setCellValue(h.getFieldValueByFieldNameBD("premi_obj").doubleValue());
                row.createCell(8).setCellValue(h.getFieldValueByFieldNameBD("pol_id").doubleValue());
                row.createCell(9).setCellValue(h.getFieldValueByFieldNameST("pol_no"));
                row.createCell(10).setCellValue(h.getFieldValueByFieldNameST("cob"));
                row.createCell(11).setCellValue(h.getFieldValueByFieldNameDT("policy_date"));
                row.createCell(12).setCellValue(h.getFieldValueByFieldNameST("status"));
                if (h.getFieldValueByFieldNameST("endorse") != null) {
                    row.createCell(13).setCellValue(h.getFieldValueByFieldNameST("endorse"));
                }
                if (h.getFieldValueByFieldNameST("klaim") != null) {
                    row.createCell(14).setCellValue(h.getFieldValueByFieldNameST("klaim"));
                }
            }

            FileOutputStream fop = new FileOutputStream(fo);
            wb.write(fop);
            fop.close();

            S.close();

//                String receiver = Parameter.readString("FINANCE_EMAIL");
            String receiver = "prasetyo@askrida.co.id";
            String cc = "prasetyo@askrida.com";
            String subject = "Analisa BJB";
            String text = "Dengan hormat,\n\n"
                    + "Bersama ini kami infokan bahwa analisa BJB bulan xxx.\n"
                    + "Atas perhatian dan kerjasamanya, kami ucapkan terima kasih.\n\n\n"
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
