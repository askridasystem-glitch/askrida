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
import com.crux.util.BDUtil;
import org.quartz.StatefulJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.crux.util.LogManager;
import com.crux.util.ConnectionCache;
import com.crux.util.DateUtil;
import com.crux.util.JSPUtil;
import com.crux.util.MailUtil2;
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
import com.webfin.gl.ejb.PeriodManager;
import com.webfin.gl.model.PeriodDetailView;
import com.webfin.gl.util.GLUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AutoReporting extends Helper implements StatefulJob {

    private final static transient LogManager logger = LogManager.getInstance(AutoReporting.class);

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
        int hourAuto = 4;

        if (dow == Calendar.MONDAY) {
            if (hourAuto == hour) {

                try {
                    logger.logInfo("execute: performing proses delete konversi file 2");

                    long t = System.currentTimeMillis();

                    final Statement S = conn.createStatement();

                    /* Define the SQL query */
                    ResultSet query_set = S.executeQuery(
                            " select a.cc_code,a.cabang,a.umum,a.pemda,a.perusda,a.bpd,a.jumlah,coalesce(a.inward,0) as inward,sum(b.amount) as target "
                            + " from ( select e.cc_code,e.description as cabang, "
                            + " round(sum(getkoas(a.bus_source='1',(a.premi-a.diskon))),2) as umum,"
                            + " round(sum(getkoas(a.bus_source='2',(a.premi-a.diskon))),2) as pemda,"
                            + " round(sum(getkoas(a.bus_source='3',(a.premi-a.diskon))),2) as perusda,"
                            + " round(sum(getkoas(a.bus_source='4',(a.premi-a.diskon))),2) as bpd,"
                            + " round(coalesce(sum(a.premi-a.diskon),0),2) as jumlah,a.inward "
                            + " from gl_cost_center e left join ( "
                            + " select b.category1 as bus_source,a.cc_code,"
                            + " sum(getpremiend(d.entity_id,coalesce(a.premi_total*a.ccy_rate,0),d.premi_amt*a.ccy_rate*-1)) as premi,"
                            + " sum(getpremiend(d.entity_id,coalesce(a.nd_disc1*a.ccy_rate,0),((coalesce(d.disc_amount,0))*a.ccy_rate)*-1)) as diskon, "
                            + " ( select sum(checkreas(d.ar_trx_line_id in ('1','4','77','79'),d.amount)) "
                            + " from ins_pol_inward c inner join ins_pol_inward_details d on d.ar_invoice_id = c.ar_invoice_id "
                            + " where c.approved_flag = 'Y' and c.ar_trx_type_id in (1,2,3,20) "
                            + " and date_trunc('day',c.mutation_date) >= '" + DateUtil.getYear(new Date()) + "-01-01 00:00:00'"
                            + " and date_trunc('day',c.mutation_date) <= '" + new Date() + "'"
                            //                    + " and date_trunc('day',c.mutation_date) >= '2016-01-01 00:00:00'"
                            //                    + " and date_trunc('day',c.mutation_date) <= '2016-12-31 00:00:00'"
                            + " ) as inward "
                            + " from ins_policies a  "
                            + " left join ent_master b on b.ent_id = a.entity_id "
                            + " inner join ins_pol_coins d on d.policy_id = a.pol_id "
                            + " left join ent_master f on f.ent_id = a.prod_id "
                            + " where a.active_flag='Y' and a.effective_flag='Y' and (d.entity_id <> 1 or d.coins_type <>'COINS_COVER') and a.status in ('POLICY','ENDORSE','RENEWAL') "
                            + " and date_trunc('day', a.policy_date) >= '" + DateUtil.getYear(new Date()) + "-01-01 00:00:00' "
                            + " and date_trunc('day', a.policy_date) <= '" + new Date() + "' "
                            //                    + " and date_trunc('day', a.policy_date) >= '2016-01-01 00:00:00'"
                            //                    + " and date_trunc('day', a.policy_date) <= '2016-12-31 00:00:00'"
                            + " group by b.category1,a.cc_code "
                            + " ) a on a.cc_code = e.cc_code "
                            + " where e.cc_code not in ('01') "
                            + " group by e.cc_code,e.description,a.inward order by e.cc_code"
                            + " ) a left join ins_statistic_product_cab b on a.cc_code = b.cc_code where b.years = '" + DateUtil.getYear(new Date()) + "' "
                            + " group by a.cc_code,a.cabang,a.umum,a.pemda,a.perusda,a.bpd,a.jumlah,a.inward order by a.cc_code ");

                    String fileName = "rpprekapcabang" + DateUtil.getDateStr(new Date(), "yyyyMMdd");
//            String fileName = "rpprekapcabang20161231";
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
                    PdfPTable my_report_table = new PdfPTable(8);
                    my_report_table.setWidthPercentage(100);
                    //create a cell object

                    Font large = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD),
                            medium = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD),
                            small = new Font(Font.FontFamily.TIMES_ROMAN, 8),
                            smallb = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);

                    //insert headings
                    PdfPCell Title = new PdfPCell(new Phrase("REKAPITULASI PRODUKSI PREMI",
                            large));
                    Title.setBorder(Rectangle.NO_BORDER);
                    Title.setColspan(8);
                    Title.setHorizontalAlignment(Element.ALIGN_CENTER);
                    my_report_table.addCell(Title);
                    my_report_table.completeRow();

                    PdfPCell pertanggal = new PdfPCell(new Phrase("Tanggal Polis : 01/01/" + DateUtil.getYear(new Date()) + " s/d " + DateUtil.getDateStr(new Date(), "dd/MM/yyyy"),
                            medium));
//            PdfPCell pertanggal = new PdfPCell(new Phrase("Tanggal Polis : 01/01/2016 s/d 31/12/2016",
//                    medium));
                    pertanggal.setBorder(Rectangle.NO_BORDER);
                    pertanggal.setColspan(8);
                    pertanggal.setHorizontalAlignment(Element.ALIGN_CENTER);
                    my_report_table.addCell(pertanggal);
                    my_report_table.completeRow();

                    //insert an empty row
                    PdfPCell emptyRow = new PdfPCell(new Phrase(" ", large));
                    emptyRow.setBorder(Rectangle.NO_BORDER);
                    emptyRow.setColspan(8);
                    emptyRow.setHorizontalAlignment(Element.ALIGN_CENTER);
                    my_report_table.addCell(emptyRow);
                    my_report_table.completeRow();

                    //insert column headings
                    Phrase p[] = {new Phrase("CABANG", medium),
                        new Phrase("UMUM", medium),
                        new Phrase("PEMDA", medium),
                        new Phrase("PERUSDA", medium),
                        new Phrase("BPD", medium),
                        new Phrase("JUMLAH", medium),
                        new Phrase("TARGET", medium),
                        new Phrase("%", medium)};

                    PdfPCell judcab = new PdfPCell(p[0]),
                            judumum = new PdfPCell(p[1]),
                            judpemda = new PdfPCell(p[2]),
                            judperusda = new PdfPCell(p[3]),
                            judbpd = new PdfPCell(p[4]),
                            judjumlah = new PdfPCell(p[5]),
                            judtarget = new PdfPCell(p[6]),
                            judpersen = new PdfPCell(p[7]);

                    judcab.setHorizontalAlignment(judcab.ALIGN_CENTER);
                    judcab.setVerticalAlignment(judcab.ALIGN_MIDDLE);
                    judcab.setGrayFill(0.7f);
                    judumum.setHorizontalAlignment(judumum.ALIGN_CENTER);
                    judumum.setVerticalAlignment(judumum.ALIGN_MIDDLE);
                    judumum.setGrayFill(0.7f);
                    judpemda.setHorizontalAlignment(judpemda.ALIGN_CENTER);
                    judpemda.setVerticalAlignment(judpemda.ALIGN_MIDDLE);
                    judpemda.setGrayFill(0.7f);
                    judperusda.setHorizontalAlignment(judperusda.ALIGN_CENTER);
                    judperusda.setVerticalAlignment(judperusda.ALIGN_MIDDLE);
                    judperusda.setGrayFill(0.7f);
                    judbpd.setHorizontalAlignment(judbpd.ALIGN_CENTER);
                    judbpd.setVerticalAlignment(judbpd.ALIGN_MIDDLE);
                    judbpd.setGrayFill(0.7f);
                    judjumlah.setHorizontalAlignment(judjumlah.ALIGN_CENTER);
                    judjumlah.setVerticalAlignment(judjumlah.ALIGN_MIDDLE);
                    judjumlah.setGrayFill(0.7f);
                    judtarget.setHorizontalAlignment(judtarget.ALIGN_CENTER);
                    judtarget.setVerticalAlignment(judtarget.ALIGN_MIDDLE);
                    judtarget.setGrayFill(0.7f);
                    judpersen.setHorizontalAlignment(judpersen.ALIGN_CENTER);
                    judpersen.setVerticalAlignment(judpersen.ALIGN_MIDDLE);
                    judpersen.setGrayFill(0.7f);

                    my_report_table.addCell(judcab);
                    my_report_table.addCell(judumum);
                    my_report_table.addCell(judpemda);
                    my_report_table.addCell(judperusda);
                    my_report_table.addCell(judbpd);
                    my_report_table.addCell(judjumlah);
                    my_report_table.addCell(judtarget);
                    my_report_table.addCell(judpersen);

                    //insert column data
                    PdfPCell table_cell = null;
                    BigDecimal umumTotal = new BigDecimal(0);
                    BigDecimal pemdaTotal = new BigDecimal(0);
                    BigDecimal perusdaTotal = new BigDecimal(0);
                    BigDecimal bpdTotal = new BigDecimal(0);
                    BigDecimal jumlahTotal = new BigDecimal(0);
                    BigDecimal targetTotal = new BigDecimal(0);

                    BigDecimal inward = new BigDecimal(0);

                    while (query_set.next()) {
                        String desc = query_set.getString("cabang");
                        table_cell = new PdfPCell(new Phrase(desc, small));
                        table_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        my_report_table.addCell(table_cell);

                        BigDecimal umum = query_set.getBigDecimal("umum");
                        umumTotal = BDUtil.add(umumTotal, umum);
                        table_cell = new PdfPCell(new Phrase(JSPUtil.printX(umum, 0), small));
                        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        my_report_table.addCell(table_cell);

                        BigDecimal pemda = query_set.getBigDecimal("pemda");
                        pemdaTotal = BDUtil.add(pemdaTotal, pemda);
                        table_cell = new PdfPCell(new Phrase(JSPUtil.printX(pemda, 0), small));
                        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        my_report_table.addCell(table_cell);

                        BigDecimal perusda = query_set.getBigDecimal("perusda");
                        perusdaTotal = BDUtil.add(perusdaTotal, perusda);
                        table_cell = new PdfPCell(new Phrase(JSPUtil.printX(perusda, 0), small));
                        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        my_report_table.addCell(table_cell);

                        BigDecimal bpd = query_set.getBigDecimal("bpd");
                        bpdTotal = BDUtil.add(bpdTotal, bpd);
                        table_cell = new PdfPCell(new Phrase(JSPUtil.printX(bpd, 0), small));
                        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        my_report_table.addCell(table_cell);

                        BigDecimal jumlah = query_set.getBigDecimal("jumlah");
                        jumlahTotal = BDUtil.add(jumlahTotal, jumlah);
                        table_cell = new PdfPCell(new Phrase(JSPUtil.printX(jumlah, 0), small));
                        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        my_report_table.addCell(table_cell);

                        BigDecimal target = query_set.getBigDecimal("target");
                        targetTotal = BDUtil.add(targetTotal, target);
                        table_cell = new PdfPCell(new Phrase(JSPUtil.printX(target, 0), small));
                        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        my_report_table.addCell(table_cell);

                        BigDecimal persen = BDUtil.mul(BDUtil.div(jumlah, target, 5), new BigDecimal(100));
                        table_cell = new PdfPCell(new Phrase(JSPUtil.printX(persen, 2), small));
                        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        my_report_table.addCell(table_cell);

                        inward = query_set.getBigDecimal("inward");
                    }

                    table_cell = new PdfPCell(new Phrase("SUBTOTAL", smallb));
                    table_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    my_report_table.addCell(table_cell);
                    table_cell = new PdfPCell(new Phrase(JSPUtil.printX(umumTotal, 0), smallb));
                    table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table.addCell(table_cell);
                    table_cell = new PdfPCell(new Phrase(JSPUtil.printX(pemdaTotal, 0), smallb));
                    table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table.addCell(table_cell);
                    table_cell = new PdfPCell(new Phrase(JSPUtil.printX(perusdaTotal, 0), smallb));
                    table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table.addCell(table_cell);
                    table_cell = new PdfPCell(new Phrase(JSPUtil.printX(bpdTotal, 0), smallb));
                    table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table.addCell(table_cell);
                    table_cell = new PdfPCell(new Phrase(JSPUtil.printX(jumlahTotal, 0), smallb));
                    table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table.addCell(table_cell);
                    table_cell = new PdfPCell(new Phrase(JSPUtil.printX(targetTotal, 0), smallb));
                    table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table.addCell(table_cell);
                    BigDecimal persenTotal = BDUtil.mul(BDUtil.div(jumlahTotal, targetTotal, 5), new BigDecimal(100));
                    table_cell = new PdfPCell(new Phrase(JSPUtil.printX(persenTotal, 2), smallb));
                    table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table.addCell(table_cell);

                    table_cell = new PdfPCell(new Phrase("INWARD", smallb));
                    table_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    my_report_table.addCell(table_cell);
                    //BigDecimal inward = query_set.getBigDecimal("inward");
                    table_cell = new PdfPCell(new Phrase(JSPUtil.printX(inward, 0), smallb));
                    table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table.addCell(table_cell);
                    table_cell = new PdfPCell(new Phrase("0", smallb));
                    table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table.addCell(table_cell);
                    table_cell = new PdfPCell(new Phrase("0", smallb));
                    table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table.addCell(table_cell);
                    table_cell = new PdfPCell(new Phrase("0", smallb));
                    table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table.addCell(table_cell);
                    table_cell = new PdfPCell(new Phrase(JSPUtil.printX(inward, 0), smallb));
                    table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table.addCell(table_cell);
                    table_cell = new PdfPCell(new Phrase(null, smallb));
                    table_cell.setColspan(2);
                    my_report_table.addCell(table_cell);

                    table_cell = new PdfPCell(new Phrase("TOTAL", smallb));
                    table_cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    my_report_table.addCell(table_cell);
                    BigDecimal totalumum = BDUtil.add(inward, umumTotal);
                    table_cell = new PdfPCell(new Phrase(JSPUtil.printX(totalumum, 0), smallb));
                    table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table.addCell(table_cell);
                    table_cell = new PdfPCell(new Phrase(JSPUtil.printX(pemdaTotal, 0), smallb));
                    table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table.addCell(table_cell);
                    table_cell = new PdfPCell(new Phrase(JSPUtil.printX(perusdaTotal, 0), smallb));
                    table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table.addCell(table_cell);
                    table_cell = new PdfPCell(new Phrase(JSPUtil.printX(bpdTotal, 0), smallb));
                    table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table.addCell(table_cell);
                    BigDecimal total = BDUtil.add(inward, jumlahTotal);
                    table_cell = new PdfPCell(new Phrase(JSPUtil.printX(total, 0), smallb));
                    table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table.addCell(table_cell);
                    table_cell = new PdfPCell(new Phrase(null, smallb));
                    table_cell.setColspan(2);
                    my_report_table.addCell(table_cell);
                    //my_report_table.completeRow();

                    String created = "Created by System " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "dd ^^ yyyy HH:mm:ss"));
                    PdfPCell bySistem = new PdfPCell(new Phrase(created, small));
                    bySistem.setBorder(Rectangle.NO_BORDER);
                    bySistem.setColspan(8);
                    bySistem.setHorizontalAlignment(Element.ALIGN_LEFT);
                    my_report_table.addCell(bySistem);
                    my_report_table.completeRow();

                    //Create QR Code by using BarcodeQRCode Class
                    BarcodeQRCode my_code = new BarcodeQRCode(DateUtil.getDateStr(new Date(), "dd-MMM-yyyy HH:mm:ss") + "_" + JSPUtil.printX(jumlahTotal, 0), 3, 3, null);
                    //Get Image corresponding to the input string
                    Image qr_image = my_code.getImage();

                    PdfPCell barcode = new PdfPCell(qr_image);
                    barcode.setBorder(Rectangle.NO_BORDER);
                    barcode.setColspan(8);
                    barcode.setHorizontalAlignment(Element.ALIGN_LEFT);
                    my_report_table.addCell(barcode);
                    my_report_table.completeRow();

                    my_report_table.setWidths(new int[]{30, 30, 30, 30, 30, 30, 30, 10});

                    /* Attach report table to PDF */
                    my_pdf_report.add(my_report_table);
                    my_pdf_report.close();

                    //BATAS LAPORAN\\

                    ResultSet query_set2 = S.executeQuery(
                            " select e.description as cabang, "
                            + " round(sum(getkoas(bus_source='1',(premi-diskon))),2) as umum,"
                            + " round(sum(getkoas(bus_source='2',(premi-diskon))),2) as pemda,"
                            + " round(sum(getkoas(bus_source='3',(premi-diskon))),2) as perusda,"
                            + " round(sum(getkoas(bus_source='4',(premi-diskon))),2) as bpd,"
                            + " round(coalesce(sum(premi-diskon),0),2) as jumlah,coalesce(inward,0) as inward "
                            + " from gl_cost_center e left join ( "
                            + " select b.category1 as bus_source,a.cc_code,"
                            + " sum(getpremiend(d.entity_id,coalesce(a.premi_total*a.ccy_rate,0),d.premi_amt*a.ccy_rate*-1)) as premi,"
                            + " sum(getpremiend(d.entity_id,coalesce(a.nd_disc1*a.ccy_rate,0),((coalesce(d.disc_amount,0))*a.ccy_rate)*-1)) as diskon, "
                            + " ( select sum(checkreas(d.ar_trx_line_id in ('1','4','77','79'),d.amount)) "
                            + " from ins_pol_inward c inner join ins_pol_inward_details d on d.ar_invoice_id = c.ar_invoice_id "
                            + " where c.approved_flag = 'Y' and c.ar_trx_type_id in (1,2,3,20) "
                            + " and date_trunc('day',c.mutation_date) >= '" + DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-01 00:00:00'"
                            + " and date_trunc('day',c.mutation_date) <= '" + new Date() + "'"
                            //                    + " and date_trunc('day',c.mutation_date) >= '2016-12-01 00:00:00'"
                            //                    + " and date_trunc('day',c.mutation_date) <= '2016-12-31 00:00:00'"
                            + " ) as inward "
                            + " from ins_policies a  "
                            + " left join ent_master b on b.ent_id = a.entity_id "
                            + " inner join ins_pol_coins d on d.policy_id = a.pol_id "
                            + " left join ent_master f on f.ent_id = a.prod_id "
                            + " where a.active_flag='Y' and a.effective_flag='Y' and (d.entity_id <> 1 or d.coins_type <>'COINS_COVER') and a.status in ('POLICY','ENDORSE','RENEWAL') "
                            + " and date_trunc('day', a.policy_date) >= '" + DateUtil.getYear(new Date()) + "-" + DateUtil.getMonth2Digit(new Date()) + "-01 00:00:00' "
                            + " and date_trunc('day', a.policy_date) <= '" + new Date() + "' "
                            //                    + " and date_trunc('day', a.policy_date) >= '2016-12-01 00:00:00'"
                            //                    + " and date_trunc('day', a.policy_date) <= '2016-12-31 00:00:00'"
                            + " group by b.category1,a.cc_code "
                            + " ) z on z.cc_code = e.cc_code "
                            + " where e.cc_code not in ('01') "
                            + " group by e.cc_code,e.description,inward order by e.cc_code");

                    String fileName2 = "rpprekapcabangperbulan" + DateUtil.getDateStr(new Date(), "yyyyMMdd");
//            String fileName2 = "rpprekapcabangperbulan20161231";
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
                    PdfPTable my_report_table2 = new PdfPTable(6);
                    my_report_table2.setWidthPercentage(100);
                    //create a cell object

                    //insert headings
                    PdfPCell Title2 = new PdfPCell(new Phrase("REKAPITULASI PRODUKSI PREMI",
                            large));
                    Title2.setBorder(Rectangle.NO_BORDER);
                    Title2.setColspan(6);
                    Title2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    my_report_table2.addCell(Title2);
                    my_report_table2.completeRow();

                    PdfPCell pertanggal2 = new PdfPCell(new Phrase("Tanggal Polis : 01/" + DateUtil.getMonth2Digit(new Date()) + "/" + DateUtil.getYear(new Date()) + " s/d " + DateUtil.getDateStr(new Date(), "dd/MM/yyyy"),
                            medium));
//            PdfPCell pertanggal2 = new PdfPCell(new Phrase("Tanggal Polis : 01/12/2016 s/d 31/12/2016",
//                    medium));
                    pertanggal2.setBorder(Rectangle.NO_BORDER);
                    pertanggal2.setColspan(6);
                    pertanggal2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    my_report_table2.addCell(pertanggal2);
                    my_report_table2.completeRow();

                    //insert an empty row
                    my_report_table2.addCell(emptyRow);
                    my_report_table2.completeRow();

                    //insert column headings
                    judcab.setHorizontalAlignment(judcab.ALIGN_CENTER);
                    judcab.setVerticalAlignment(judcab.ALIGN_MIDDLE);
                    judcab.setGrayFill(0.7f);
                    judumum.setHorizontalAlignment(judumum.ALIGN_CENTER);
                    judumum.setVerticalAlignment(judumum.ALIGN_MIDDLE);
                    judumum.setGrayFill(0.7f);
                    judpemda.setHorizontalAlignment(judpemda.ALIGN_CENTER);
                    judpemda.setVerticalAlignment(judpemda.ALIGN_MIDDLE);
                    judpemda.setGrayFill(0.7f);
                    judperusda.setHorizontalAlignment(judperusda.ALIGN_CENTER);
                    judperusda.setVerticalAlignment(judperusda.ALIGN_MIDDLE);
                    judperusda.setGrayFill(0.7f);
                    judbpd.setHorizontalAlignment(judbpd.ALIGN_CENTER);
                    judbpd.setVerticalAlignment(judbpd.ALIGN_MIDDLE);
                    judbpd.setGrayFill(0.7f);
                    judjumlah.setHorizontalAlignment(judjumlah.ALIGN_CENTER);
                    judjumlah.setVerticalAlignment(judjumlah.ALIGN_MIDDLE);
                    judjumlah.setGrayFill(0.7f);
                    my_report_table2.addCell(judcab);
                    my_report_table2.addCell(judumum);
                    my_report_table2.addCell(judpemda);
                    my_report_table2.addCell(judperusda);
                    my_report_table2.addCell(judbpd);
                    my_report_table2.addCell(judjumlah);

                    //insert column data
                    PdfPCell table_cell2 = null;
                    BigDecimal umumTotal2 = new BigDecimal(0);
                    BigDecimal pemdaTotal2 = new BigDecimal(0);
                    BigDecimal perusdaTotal2 = new BigDecimal(0);
                    BigDecimal bpdTotal2 = new BigDecimal(0);
                    BigDecimal jumlahTotal2 = new BigDecimal(0);

                    BigDecimal inward2 = new BigDecimal(0);

                    while (query_set2.next()) {
                        String desc = query_set2.getString("cabang");
                        table_cell2 = new PdfPCell(new Phrase(desc, small));
                        table_cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                        my_report_table2.addCell(table_cell2);
                        BigDecimal umum = query_set2.getBigDecimal("umum");
                        umumTotal2 = BDUtil.add(umumTotal2, umum);
                        table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(umum, 0), small));
                        table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        my_report_table2.addCell(table_cell2);
                        BigDecimal pemda = query_set2.getBigDecimal("pemda");
                        pemdaTotal2 = BDUtil.add(pemdaTotal2, pemda);
                        table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(pemda, 0), small));
                        table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        my_report_table2.addCell(table_cell2);
                        BigDecimal perusda = query_set2.getBigDecimal("perusda");
                        perusdaTotal2 = BDUtil.add(perusdaTotal2, perusda);
                        table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(perusda, 0), small));
                        table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        my_report_table2.addCell(table_cell2);
                        BigDecimal bpd = query_set2.getBigDecimal("bpd");
                        bpdTotal2 = BDUtil.add(bpdTotal2, bpd);
                        table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(bpd, 0), small));
                        table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        my_report_table2.addCell(table_cell2);
                        BigDecimal jumlah = query_set2.getBigDecimal("jumlah");
                        jumlahTotal2 = BDUtil.add(jumlahTotal2, jumlah);
                        table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(jumlah, 0), small));
                        table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        my_report_table2.addCell(table_cell2);

                        inward2 = query_set2.getBigDecimal("inward");
                    }

                    table_cell2 = new PdfPCell(new Phrase("SUBTOTAL", smallb));
                    table_cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                    my_report_table2.addCell(table_cell2);
                    table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(umumTotal2, 0), smallb));
                    table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table2.addCell(table_cell2);
                    table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(pemdaTotal2, 0), smallb));
                    table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table2.addCell(table_cell2);
                    table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(perusdaTotal2, 0), smallb));
                    table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table2.addCell(table_cell2);
                    table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(bpdTotal2, 0), smallb));
                    table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table2.addCell(table_cell2);
                    table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(jumlahTotal2, 0), smallb));
                    table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table2.addCell(table_cell2);

                    table_cell2 = new PdfPCell(new Phrase("INWARD", smallb));
                    table_cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                    my_report_table2.addCell(table_cell2);
                    //BigDecimal inward2 = query_set2.getBigDecimal("inward");
                    table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(inward2, 0), smallb));
                    table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table2.addCell(table_cell2);
                    table_cell2 = new PdfPCell(new Phrase("0", smallb));
                    table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table2.addCell(table_cell2);
                    table_cell2 = new PdfPCell(new Phrase("0", smallb));
                    table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table2.addCell(table_cell2);
                    table_cell2 = new PdfPCell(new Phrase("0", smallb));
                    table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table2.addCell(table_cell2);
                    table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(inward2, 0), smallb));
                    table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table2.addCell(table_cell2);

                    table_cell2 = new PdfPCell(new Phrase("TOTAL", smallb));
                    table_cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                    my_report_table2.addCell(table_cell2);
                    BigDecimal totalumum2 = BDUtil.add(inward2, umumTotal2);
                    table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(totalumum2, 0), smallb));
                    table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table2.addCell(table_cell2);
                    table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(pemdaTotal2, 0), smallb));
                    table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table2.addCell(table_cell2);
                    table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(perusdaTotal2, 0), smallb));
                    table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table2.addCell(table_cell2);
                    table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(bpdTotal2, 0), smallb));
                    table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table2.addCell(table_cell2);
                    BigDecimal total2 = BDUtil.add(inward2, jumlahTotal2);
                    table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(total2, 0), smallb));
                    table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table2.addCell(table_cell2);

                    String created2 = "Created by System " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "dd ^^ yyyy HH:mm:ss"));
                    PdfPCell bySistem2 = new PdfPCell(new Phrase(created2, small));
                    bySistem2.setBorder(Rectangle.NO_BORDER);
                    bySistem2.setColspan(6);
                    bySistem2.setHorizontalAlignment(Element.ALIGN_LEFT);
                    my_report_table2.addCell(bySistem2);
                    my_report_table2.completeRow();

                    //Create QR Code by using BarcodeQRCode Class
                    BarcodeQRCode my_code2 = new BarcodeQRCode(DateUtil.getDateStr(new Date(), "dd-MMM-yyyy HH:mm:ss") + "_" + JSPUtil.printX(jumlahTotal2, 0), 3, 3, null);
                    //Get Image corresponding to the input string
                    Image qr_image2 = my_code2.getImage();

                    PdfPCell barcode2 = new PdfPCell(qr_image2);
                    barcode2.setBorder(Rectangle.NO_BORDER);
                    barcode2.setColspan(6);
                    barcode2.setHorizontalAlignment(Element.ALIGN_LEFT);
                    my_report_table2.addCell(barcode2);
                    my_report_table2.completeRow();

                    my_report_table2.setWidths(new int[]{50, 30, 30, 30, 30, 30});

                    /* Attach report table to PDF */
                    my_pdf_report2.add(my_report_table2);
                    my_pdf_report2.close();

                    //BATAS LAPORAN\\

                    String policyDateStart = null;
                    String policyDateEnd = null;
                    Date dateStart = null;
                    Date dateEnd = null;

                    int tahunCodeLast;
                    int bulanCode = DateUtil.getMonthDigit(new Date());
                    int tanggalCode = Integer.parseInt(DateUtil.getDays(new Date()));

                    if (bulanCode == 1) {
                        tahunCodeLast = Integer.parseInt(DateUtil.getYear(new Date())) - 1;

                        policyDateStart = tahunCodeLast + "-12-01 00:00:00";
                        policyDateEnd = tahunCodeLast + "-12-31 00:00:00";

                        dateStart = DateUtil.getDate("01/12/" + tahunCodeLast);
                        dateEnd = DateUtil.getDate("31/12/" + tahunCodeLast);
                    } else if (bulanCode > 1) {
                        int bulanCodeNow = DateUtil.getMonthDigit(new Date()) - 1;

                        PeriodDetailView pd = PeriodManager.getInstance().getPeriod(String.valueOf(bulanCodeNow), DateUtil.getYear(new Date()));
                        dateStart = pd.getDtStartDate();
                        dateEnd = pd.getDtEndDate();

                        policyDateStart = dateStart.toString();
                        policyDateEnd = dateEnd.toString();
                    }

                    ResultSet query_set3 = S.executeQuery(
                            " select e.description as cabang, "
                            + " round(sum(getkoas(bus_source='1',(premi-diskon))),2) as umum,"
                            + " round(sum(getkoas(bus_source='2',(premi-diskon))),2) as pemda,"
                            + " round(sum(getkoas(bus_source='3',(premi-diskon))),2) as perusda,"
                            + " round(sum(getkoas(bus_source='4',(premi-diskon))),2) as bpd,"
                            + " round(coalesce(sum(premi-diskon),0),2) as jumlah,coalesce(inward,0) as inward "
                            + " from gl_cost_center e left join ( "
                            + " select b.category1 as bus_source,a.cc_code,"
                            + " sum(getpremiend(d.entity_id,coalesce(a.premi_total*a.ccy_rate,0),d.premi_amt*a.ccy_rate*-1)) as premi,"
                            + " sum(getpremiend(d.entity_id,coalesce(a.nd_disc1*a.ccy_rate,0),((coalesce(d.disc_amount,0))*a.ccy_rate)*-1)) as diskon, "
                            + " ( select sum(checkreas(d.ar_trx_line_id in ('1','4','77','79'),d.amount)) "
                            + " from ins_pol_inward c inner join ins_pol_inward_details d on d.ar_invoice_id = c.ar_invoice_id "
                            + " where c.approved_flag = 'Y' and c.ar_trx_type_id in (1,2,3,20) "
                            + " and date_trunc('day',c.mutation_date) >= '" + policyDateStart + "'"
                            + " and date_trunc('day',c.mutation_date) <= '" + policyDateEnd + "'"
                            //                    + " and date_trunc('day',c.mutation_date) >= '2016-12-01 00:00:00'"
                            //                    + " and date_trunc('day',c.mutation_date) <= '2016-12-31 00:00:00'"
                            + " ) as inward "
                            + " from ins_policies a  "
                            + " left join ent_master b on b.ent_id = a.entity_id "
                            + " inner join ins_pol_coins d on d.policy_id = a.pol_id "
                            + " left join ent_master f on f.ent_id = a.prod_id "
                            + " where a.active_flag='Y' and a.effective_flag='Y' and (d.entity_id <> 1 or d.coins_type <>'COINS_COVER') and a.status in ('POLICY','ENDORSE','RENEWAL') "
                            + " and date_trunc('day', a.policy_date) >= '" + policyDateStart + "' "
                            + " and date_trunc('day', a.policy_date) <= '" + policyDateEnd + "' "
                            //                    + " and date_trunc('day', a.policy_date) >= '2016-12-01 00:00:00'"
                            //                    + " and date_trunc('day', a.policy_date) <= '2016-12-31 00:00:00'"
                            + " group by b.category1,a.cc_code "
                            + " ) z on z.cc_code = e.cc_code "
                            + " where e.cc_code not in ('01') "
                            + " group by e.cc_code,e.description,inward order by e.cc_code");

                    String fileName3 = "rpprekapcabangperbulan" + DateUtil.getDateStr(dateEnd, "MMyyyy");
//            String fileName2 = "rpprekapcabangperbulan20161231";
                    File fo3 = new File("C:/");

                    String pathTemp3 = tempPath + File.separator + fileName3 + ".pdf";

                    try {
                        new File(path1).mkdir();
                        new File(tempPath).mkdir();
                    } catch (Exception e) {
                    }

                    fo3 = new File(pathTemp3);

                    FileOutputStream fop3 = new FileOutputStream(fo3);

                    /* Step-2: Initialize PDF documents - logical objects */
                    Document my_pdf_report3 = new Document();
                    PdfWriter writer3 = PdfWriter.getInstance(my_pdf_report3, fop3);
                    //PdfWriter.getInstance(my_pdf_report, fop);
                    my_pdf_report3.open();

                    //we have four columns in our table
                    PdfPTable my_report_table3 = new PdfPTable(6);
                    my_report_table3.setWidthPercentage(100);
                    //create a cell object

                    //insert headings
                    PdfPCell Title3 = new PdfPCell(new Phrase("REKAPITULASI PRODUKSI PREMI",
                            large));
                    Title3.setBorder(Rectangle.NO_BORDER);
                    Title3.setColspan(6);
                    Title3.setHorizontalAlignment(Element.ALIGN_CENTER);
                    my_report_table3.addCell(Title3);
                    my_report_table3.completeRow();

                    PdfPCell pertanggal3 = new PdfPCell(new Phrase("Tanggal Polis : " + DateUtil.getDateStr(dateStart, "dd/MM/yyyy") + " s/d " + DateUtil.getDateStr(dateEnd, "dd/MM/yyyy"),
                            medium));
//            PdfPCell pertanggal2 = new PdfPCell(new Phrase("Tanggal Polis : 01/12/2016 s/d 31/12/2016",
//                    medium));
                    pertanggal3.setBorder(Rectangle.NO_BORDER);
                    pertanggal3.setColspan(6);
                    pertanggal3.setHorizontalAlignment(Element.ALIGN_CENTER);
                    my_report_table3.addCell(pertanggal3);
                    my_report_table3.completeRow();

                    //insert an empty row
                    my_report_table3.addCell(emptyRow);
                    my_report_table3.completeRow();

                    //insert column headings
                    judcab.setHorizontalAlignment(judcab.ALIGN_CENTER);
                    judcab.setVerticalAlignment(judcab.ALIGN_MIDDLE);
                    judcab.setGrayFill(0.7f);
                    judumum.setHorizontalAlignment(judumum.ALIGN_CENTER);
                    judumum.setVerticalAlignment(judumum.ALIGN_MIDDLE);
                    judumum.setGrayFill(0.7f);
                    judpemda.setHorizontalAlignment(judpemda.ALIGN_CENTER);
                    judpemda.setVerticalAlignment(judpemda.ALIGN_MIDDLE);
                    judpemda.setGrayFill(0.7f);
                    judperusda.setHorizontalAlignment(judperusda.ALIGN_CENTER);
                    judperusda.setVerticalAlignment(judperusda.ALIGN_MIDDLE);
                    judperusda.setGrayFill(0.7f);
                    judbpd.setHorizontalAlignment(judbpd.ALIGN_CENTER);
                    judbpd.setVerticalAlignment(judbpd.ALIGN_MIDDLE);
                    judbpd.setGrayFill(0.7f);
                    judjumlah.setHorizontalAlignment(judjumlah.ALIGN_CENTER);
                    judjumlah.setVerticalAlignment(judjumlah.ALIGN_MIDDLE);
                    judjumlah.setGrayFill(0.7f);
                    my_report_table3.addCell(judcab);
                    my_report_table3.addCell(judumum);
                    my_report_table3.addCell(judpemda);
                    my_report_table3.addCell(judperusda);
                    my_report_table3.addCell(judbpd);
                    my_report_table3.addCell(judjumlah);

                    //insert column data
                    PdfPCell table_cell3 = null;
                    BigDecimal umumTotal3 = new BigDecimal(0);
                    BigDecimal pemdaTotal3 = new BigDecimal(0);
                    BigDecimal perusdaTotal3 = new BigDecimal(0);
                    BigDecimal bpdTotal3 = new BigDecimal(0);
                    BigDecimal jumlahTotal3 = new BigDecimal(0);

                    BigDecimal inward3 = new BigDecimal(0);

                    while (query_set3.next()) {
                        String desc = query_set3.getString("cabang");
                        table_cell3 = new PdfPCell(new Phrase(desc, small));
                        table_cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
                        my_report_table3.addCell(table_cell3);
                        BigDecimal umum = query_set3.getBigDecimal("umum");
                        umumTotal3 = BDUtil.add(umumTotal3, umum);
                        table_cell3 = new PdfPCell(new Phrase(JSPUtil.printX(umum, 0), small));
                        table_cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        my_report_table3.addCell(table_cell3);
                        BigDecimal pemda = query_set3.getBigDecimal("pemda");
                        pemdaTotal3 = BDUtil.add(pemdaTotal3, pemda);
                        table_cell3 = new PdfPCell(new Phrase(JSPUtil.printX(pemda, 0), small));
                        table_cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        my_report_table3.addCell(table_cell3);
                        BigDecimal perusda = query_set3.getBigDecimal("perusda");
                        perusdaTotal3 = BDUtil.add(perusdaTotal3, perusda);
                        table_cell3 = new PdfPCell(new Phrase(JSPUtil.printX(perusda, 0), small));
                        table_cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        my_report_table3.addCell(table_cell3);
                        BigDecimal bpd = query_set3.getBigDecimal("bpd");
                        bpdTotal3 = BDUtil.add(bpdTotal3, bpd);
                        table_cell3 = new PdfPCell(new Phrase(JSPUtil.printX(bpd, 0), small));
                        table_cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        my_report_table3.addCell(table_cell3);
                        BigDecimal jumlah = query_set3.getBigDecimal("jumlah");
                        jumlahTotal3 = BDUtil.add(jumlahTotal3, jumlah);
                        table_cell3 = new PdfPCell(new Phrase(JSPUtil.printX(jumlah, 0), small));
                        table_cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        my_report_table3.addCell(table_cell3);

                        inward3 = query_set3.getBigDecimal("inward");
                    }

                    table_cell3 = new PdfPCell(new Phrase("SUBTOTAL", smallb));
                    table_cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
                    my_report_table3.addCell(table_cell3);
                    table_cell3 = new PdfPCell(new Phrase(JSPUtil.printX(umumTotal3, 0), smallb));
                    table_cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table3.addCell(table_cell3);
                    table_cell3 = new PdfPCell(new Phrase(JSPUtil.printX(pemdaTotal3, 0), smallb));
                    table_cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table3.addCell(table_cell3);
                    table_cell3 = new PdfPCell(new Phrase(JSPUtil.printX(perusdaTotal3, 0), smallb));
                    table_cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table3.addCell(table_cell3);
                    table_cell3 = new PdfPCell(new Phrase(JSPUtil.printX(bpdTotal3, 0), smallb));
                    table_cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table3.addCell(table_cell3);
                    table_cell3 = new PdfPCell(new Phrase(JSPUtil.printX(jumlahTotal3, 0), smallb));
                    table_cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table3.addCell(table_cell3);

                    table_cell3 = new PdfPCell(new Phrase("INWARD", smallb));
                    table_cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
                    my_report_table3.addCell(table_cell3);
                    //BigDecimal inward3 = query_set3.getBigDecimal("inward");
                    table_cell3 = new PdfPCell(new Phrase(JSPUtil.printX(inward3, 0), smallb));
                    table_cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table3.addCell(table_cell3);
                    table_cell3 = new PdfPCell(new Phrase("0", smallb));
                    table_cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table3.addCell(table_cell3);
                    table_cell3 = new PdfPCell(new Phrase("0", smallb));
                    table_cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table3.addCell(table_cell3);
                    table_cell3 = new PdfPCell(new Phrase("0", smallb));
                    table_cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table3.addCell(table_cell3);
                    table_cell3 = new PdfPCell(new Phrase(JSPUtil.printX(inward3, 0), smallb));
                    table_cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table3.addCell(table_cell3);

                    table_cell3 = new PdfPCell(new Phrase("TOTAL", smallb));
                    table_cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
                    my_report_table3.addCell(table_cell3);
                    BigDecimal totalumum3 = BDUtil.add(inward3, umumTotal3);
                    table_cell3 = new PdfPCell(new Phrase(JSPUtil.printX(totalumum3, 0), smallb));
                    table_cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table3.addCell(table_cell3);
                    table_cell3 = new PdfPCell(new Phrase(JSPUtil.printX(pemdaTotal3, 0), smallb));
                    table_cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table3.addCell(table_cell3);
                    table_cell3 = new PdfPCell(new Phrase(JSPUtil.printX(perusdaTotal3, 0), smallb));
                    table_cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table3.addCell(table_cell3);
                    table_cell3 = new PdfPCell(new Phrase(JSPUtil.printX(bpdTotal3, 0), smallb));
                    table_cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table3.addCell(table_cell3);
                    BigDecimal total3 = BDUtil.add(inward3, jumlahTotal3);
                    table_cell3 = new PdfPCell(new Phrase(JSPUtil.printX(total3, 0), smallb));
                    table_cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    my_report_table3.addCell(table_cell3);

                    String created3 = "Created by System " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "dd ^^ yyyy HH:mm:ss"));
                    PdfPCell bySistem3 = new PdfPCell(new Phrase(created3, small));
                    bySistem3.setBorder(Rectangle.NO_BORDER);
                    bySistem3.setColspan(6);
                    bySistem3.setHorizontalAlignment(Element.ALIGN_LEFT);
                    my_report_table3.addCell(bySistem3);
                    my_report_table3.completeRow();

                    //Create QR Code by using BarcodeQRCode Class
                    BarcodeQRCode my_code3 = new BarcodeQRCode(DateUtil.getDateStr(new Date(), "dd-MMM-yyyy HH:mm:ss") + "_" + JSPUtil.printX(jumlahTotal3, 0), 3, 3, null);
                    //Get Image corresponding to the input string
                    Image qr_image3 = my_code3.getImage();

                    PdfPCell barcode3 = new PdfPCell(qr_image3);
                    barcode3.setBorder(Rectangle.NO_BORDER);
                    barcode3.setColspan(6);
                    barcode3.setHorizontalAlignment(Element.ALIGN_LEFT);
                    my_report_table3.addCell(barcode3);
                    my_report_table3.completeRow();

                    my_report_table3.setWidths(new int[]{50, 30, 30, 30, 30, 30});

                    /* Attach report table to PDF */
                    my_pdf_report3.add(my_report_table3);
                    my_pdf_report3.close();

                    /* Close all DB related objects */
                    query_set.close();
                    query_set2.close();
                    query_set3.close();
                    S.close();

                    String receiver = Parameter.readString("SEND_EMAIL");
                    String subject = "Laporan Produksi Per Hari Ini " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "dd ^^ yyyy"));
                    String text = "Kepada Yth.\n"
                            + "-  Bapak Direksi\n"
                            + "-  Bapak Kepala Divisi\n"
                            + "Di Tempat\n\n\n"
                            + "Dengan hormat,\n\n"
                            + "Bersama ini kami lampirkan “Rekapitulasi Produksi Premi Per Cabang” yang diproses secara otomatis harian oleh Sistem.\n"
                            + "Demikian disampaikan dan semoga laporan tersebut di atas dapat menambah informasi, terima kasih.\n\n\n"
                            + "Hormat kami,\n"
                            + "Administrator";

                    MailUtil2 mail = new MailUtil2();
//                mail.sendEmailMultiFileNoReceiver(pathTemp, fileName, pathTemp2, fileName2, pathTemp3, fileName3, null, null, null, null, subject, text);

                    if (tanggalCode <= 7) {
                        mail.sendEmailMultiFile(pathTemp, fileName, pathTemp2, fileName2, pathTemp3, fileName3, null, null, null, null, receiver, null, subject, text);
                        mail.sendEmailMultiFileNoReceiver(pathTemp, fileName, pathTemp2, fileName2, pathTemp3, fileName3, null, null, null, null, subject, text);
                    } else {
                        mail.sendEmailMultiFile(pathTemp, fileName, pathTemp2, fileName2, null, null, null, null, null, null, receiver, null, subject, text);
                        mail.sendEmailMultiFileNoReceiver(pathTemp, fileName, pathTemp2, fileName2, null, null, null, null, null, null, subject, text);
                    }

                    t = System.currentTimeMillis() - t;

                    logger.logInfo("proses 2 selesai dalam " + t + " ms");

                } finally {
                    conn.close();
                }
            }
        }
    }
}
