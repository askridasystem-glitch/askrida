/***********************************************************************
 * Module:  com.crux.common.jobs.WarningPiutangPremi
 * Author:  Denny Mahendra
 * Created: Jul 12, 2004 12:24:48 PM
 * Purpose:
 ***********************************************************************/
package com.crux.common.jobs;

import com.crux.common.controller.Helper;
import com.crux.common.model.HashDTO;
import com.crux.common.parameter.Parameter;
import com.crux.jobs.util.JobUtil;
import com.crux.lang.LanguageManager;
import com.crux.pool.DTOPool;
import com.crux.util.BDUtil;
import org.quartz.StatefulJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.crux.util.LogManager;
import com.crux.util.ConnectionCache;
import com.crux.util.DTOList;
import com.crux.util.DateUtil;
import com.crux.util.IDFactory;
import com.crux.util.JSPUtil;
import com.crux.util.ListUtil;
import com.crux.util.MailUtil2;
import com.crux.util.SQLUtil;
import com.crux.util.StringTools;
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
import com.webfin.gl.model.*;
import com.webfin.insurance.model.UploadHeaderPiutangPremi30View;
import com.webfin.insurance.model.UploadHeaderPiutangPremi75View;
import com.webfin.insurance.model.uploadPiutangPremi30View;
import com.webfin.insurance.model.uploadPiutangPremi75View;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WarningPiutangPremiNew extends Helper implements StatefulJob {

    private final static transient LogManager logger = LogManager.getInstance(WarningPiutangPremiNew.class);
    private UploadHeaderPiutangPremi30View headerPiutang30;
    private UploadHeaderPiutangPremi75View headerPiutang75;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {

            if (JobUtil.isServerProduction()) {
                validasicabang30();
                validasicabang75();
            }

        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    public GLCostCenterView getCostCenter(String koda) {
        final GLCostCenterView costcenter = (GLCostCenterView) DTOPool.getInstance().getDTO(GLCostCenterView.class, koda);

        return costcenter;
    }

    public void validasicabang30() throws Exception {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int hourAuto = 5;

//        if (hourAuto == hour) {

            String query = "select a.cc_code,a.attr_pol_no as pol_no,a.mutation_date,date_part('day','" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd 00:00:00") + "' -a.mutation_date) as hari,"
                    + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1 else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total,"
                    + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_desc = 'Komisi', c.amount))*-1 else sum(getpremi2(d.item_desc = 'Komisi', c.amount)) end as nd_comm1,"
                    + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'HFEE', c.amount))*-1 else sum(getpremi2(d.category = 'HFEE', c.amount)) end as nd_hfee,"
                    + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category= 'BROKERAGE' and d.item_desc <> 'PPN', c.amount))*-1 else "
                    + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) end as nd_brok1,"
                    + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn,"
                    + "sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,"
                    + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'DISC', c.amount))*-1 else sum(getpremi2(d.category = 'DISC', c.amount)) end as nd_disc1 "
                    + "from ar_invoice a inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                    + "left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                    + "inner join ent_master b on b.ent_id = a.ent_id "
                    + "where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' "
                    + "and " + Parameter.readString("WARNING_PIUTANG_BPD") + " "
                    + "and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7) "
                    + "and substr(a.attr_pol_no,1,16) in ( select pol_no from ( "
                    + "select substr(a.attr_pol_no,1,16) as pol_no,"
                    + "sum(getpremi(a.ar_trx_type_id = 10,getpremi2(d.item_class in ('PREMIG'), c.amount)*-1,getpremi2(d.item_class in ('PREMIG'), c.amount))) as premi_total,"
                    + "sum(case when a.ar_trx_type_id = 10 then (getpremi2(d.item_desc = 'Komisi', c.amount)*-1) else (getpremi2(d.item_desc = 'Komisi', c.amount)) end) as nd_comm1,"
                    + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
                    + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn,sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,"
                    + "sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1 "
                    + "from ar_invoice a inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                    + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                    + "inner join ent_master b on b.ent_id = a.ent_id "
                    + "where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' "
                    + "and " + Parameter.readString("WARNING_PIUTANG_BPD") + " "
                    + "and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) "
                    + "and a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021' "
                    + "and date_part('day','" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd 00:00:00") + "' - a.mutation_date) = '30' "
                    + "and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd 00:00:00") + "' ) "
                    + "group by substr(a.attr_pol_no,1,16) ) a where (premi_total <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn <> 0 or nd_disc1 <> 0) ) "
                    + "and date_part('day','" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd 00:00:00") + "' - a.mutation_date) = '30' "
                    + "and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd 00:00:00") + "' ) "
                    + "group by a.cc_code,a.mutation_date,a.ar_trx_type_id,a.attr_pol_no order by a.cc_code,a.attr_pol_no ";

            DTOList cabang = null;
            cabang = ListUtil.getDTOListFromQuery(
                    "select a.cc_code from ( "
                    + query + " ) a group by a.cc_code order by a.cc_code ",
                    GLCostCenterView.class);

            for (int i = 0; i < cabang.size(); i++) {
                GLCostCenterView cab = (GLCostCenterView) cabang.get(i);

                executeCab30(cab.getStCostCenterCode());

            }
//        }
    }

    public void executeCab30(String koda) throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {

            long t = System.currentTimeMillis();

            final Statement S = conn.createStatement();

            String queryPiutangPremi = " select row_number() over(order by a.cc_code,a.pol_no) as no,a.cc_code,a.pol_id::text,a.pol_no,a.tertanggung,a.mutation_date as policy_date,a.hari,a.premibru as amount,"
                    + "(a.premibru-a.nd_comm1-a.nd_hfee-a.nd_brok1-a.nd_ppn-a.nd_feebase1-a.nd_disc1) as preminett "
                    + "from ( select a.cc_code,a.pol_id,a.pol_no,a.tertanggung,a.mutation_date,a.hari,(a.premi_total+a.nd_sfee+a.nd_pcost) as premibru,"
                    + "a.nd_comm1,a.nd_hfee,a.nd_brok1,a.nd_ppn,a.nd_feebase1,a.nd_disc1 "
                    + "from ( select a.cc_code,a.attr_pol_id as pol_id,a.attr_pol_no as pol_no,a.mutation_date,date_part('day','" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd 00:00:00") + "' - a.mutation_date) as hari,a.attr_pol_name as tertanggung,"
                    + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1 else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total,"
                    + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                    + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                    + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_desc = 'Komisi', c.amount))*-1 else sum(getpremi2(d.item_desc = 'Komisi', c.amount)) end as nd_comm1,"
                    + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'HFEE', c.amount))*-1 else sum(getpremi2(d.category = 'HFEE', c.amount)) end as nd_hfee,"
                    + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category= 'BROKERAGE' and d.item_desc <> 'PPN', c.amount))*-1 else "
                    + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) end as nd_brok1,"
                    + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn,"
                    + "sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,"
                    + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'DISC', c.amount))*-1 else sum(getpremi2(d.category = 'DISC', c.amount)) end as nd_disc1 "
                    + "from ar_invoice a inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                    + "left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                    + "inner join ent_master b on b.ent_id = a.ent_id "
                    + "where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' "
                    + "and " + Parameter.readString("WARNING_PIUTANG_BPD") + " "
                    + "and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7) and a.cc_code = '" + koda + "' "
                    + "and substr(a.attr_pol_no,1,16) in ( select pol_no from ( "
                    + "select substr(a.attr_pol_no,1,16) as pol_no,"
                    + "sum(getpremi(a.ar_trx_type_id = 10,getpremi2(d.item_class in ('PREMIG'), c.amount)*-1,getpremi2(d.item_class in ('PREMIG'), c.amount))) as premi_total,"
                    + "sum(case when a.ar_trx_type_id = 10 then (getpremi2(d.item_desc = 'Komisi', c.amount)*-1) else (getpremi2(d.item_desc = 'Komisi', c.amount)) end) as nd_comm1,"
                    + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
                    + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn,sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,"
                    + "sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1 "
                    + "from ar_invoice a inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                    + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                    + "inner join ent_master b on b.ent_id = a.ent_id "
                    + "where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' "
                    + "and " + Parameter.readString("WARNING_PIUTANG_BPD") + " "
                    + "and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) and a.cc_code = '" + koda + "'"
                    + "and a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021' "
                    + "and date_part('day','" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd 00:00:00") + "' - a.mutation_date) = '30' "
                    + "and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd 00:00:00") + "' ) "
                    + "group by substr(a.attr_pol_no,1,16) ) a where (premi_total <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn <> 0 or nd_disc1 <> 0) ) "
                    + "and date_part('day','" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd 00:00:00") + "' - a.mutation_date) = '30' "
                    + "and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd 00:00:00") + "' ) "
                    + "group by a.cc_code,a.mutation_date,a.ar_trx_type_id,a.attr_pol_id,a.attr_pol_no,a.attr_pol_name order by a.cc_code,a.attr_pol_no "
                    + ") a ) a where a.premibru <> 0 order by a.cc_code,a.pol_no ";

            /* Define the SQL query */
            ResultSet query_set2 = S.executeQuery(queryPiutangPremi);

            logger.logInfo("!!!! jalankan : proses send report");

            String fileName = "konfirmasiOSpremi_" + Parameter.readString("BRANCH_SHORT_" + koda).replace("-", "") + DateUtil.getDateStr(new Date(), "yyyyMMdd");
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
            PdfPTable my_report_table = new PdfPTable(5);
            my_report_table.setWidthPercentage(100);
            //create a cell object

            Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12),
                    smallb = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD),
                    smallc = new Font(Font.FontFamily.TIMES_ROMAN, 11),
                    small10 = new Font(Font.FontFamily.TIMES_ROMAN, 10),
                    small10bold = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD),
                    small6 = new Font(Font.FontFamily.TIMES_ROMAN, 6);
            //create a cell object

            String requestNo = null;
            String counterKey = DateUtil.getYear2Digit(new Date()) + DateUtil.getMonth2Digit(new Date());
//            String counterKey = DateUtil.getYear(new Date());
            String rn = String.valueOf(IDFactory.createNumericID("REQOSPREMI30" + counterKey, 1));
            rn = StringTools.leftPad(rn, '0', 3);

            requestNo = rn + "/KEU-OS30/" + Parameter.readString("BRANCH_SHORT_" + koda) + "/" + DateUtil.getMonth2Digit(new Date()) + "/" + DateUtil.getYear(new Date());

            //insert heading
            String hariIni = LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "dd ^^ yyyy"));
            String Tanggal = "Jakarta, " + hariIni;
            Image askridaLogoPath = Image.getInstance(Parameter.readString("DIGITAL_POLIS_LOGO_PIC"));

            PdfPCell logo = null;
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setColspan(2);
            my_report_table.addCell(logo);
            logo = new PdfPCell(askridaLogoPath);
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(logo);
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setColspan(2);
            my_report_table.addCell(logo);

            PdfPCell shkTanggal = null;
            shkTanggal = new PdfPCell(new Phrase("No. : " + requestNo, small));
            shkTanggal.setBorder(Rectangle.NO_BORDER);
            shkTanggal.setHorizontalAlignment(Element.ALIGN_LEFT);
            shkTanggal.setColspan(2);
            my_report_table.addCell(shkTanggal);
            shkTanggal = new PdfPCell(new Phrase(Tanggal, small));
            shkTanggal.setBorder(Rectangle.NO_BORDER);
            shkTanggal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            shkTanggal.setColspan(3);
            my_report_table.addCell(shkTanggal);

            //insert heading
            String created1 = "Kepada Yth.\n"
                    + "Kepala Cabang " + getCostCenter(koda).getStDescription() + "\n"
                    + getCostCenter(koda).getStAddress() + "\n\n\n";
            PdfPCell heading = new PdfPCell(new Phrase(created1, small));
            heading.setBorder(Rectangle.NO_BORDER);
            heading.setColspan(2);
            heading.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(heading);
            heading = new PdfPCell(new Phrase(" ", small));
            heading.setBorder(Rectangle.NO_BORDER);
            heading.setHorizontalAlignment(Element.ALIGN_CENTER);
            heading.setColspan(3);
            my_report_table.addCell(heading);

            //insert judul
            PdfPCell Title = new PdfPCell(new Phrase("Hal : Konfirmasi Outstanding Premi Umur 30 Hari",
                    smallb));
            Title.setBorder(Rectangle.NO_BORDER);
            Title.setColspan(5);
            Title.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(Title);

            //insert an empty row
            PdfPCell emptyRow = new PdfPCell(new Phrase(" ", small));
            emptyRow.setBorder(Rectangle.NO_BORDER);
            emptyRow.setColspan(5);
            emptyRow.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(emptyRow);

            //insert isi
            BigDecimal premiNet = new BigDecimal(0);

            while (query_set2.next()) {
                BigDecimal premi = query_set2.getBigDecimal("preminett");
                premiNet = BDUtil.add(premiNet, premi);
            }

            String created2 = "Diberitahukan kepada Saudara/i bahwa per tanggal " + hariIni + ", polis yang tercatat sebagai "
                    + "Outstanding premi umur 30 hari adalah terlampir dengan total premi netto Rp. " + JSPUtil.printX(premiNet, 2) + ".\n\n"
                    + "Menunjuk pada SK Direksi No: SK.013/DIR/2016 Bab XI pasal 20 maka Outstanding premi tersebut agar dapat ditindaklanjuti.\n\n"
                    + "Surat pemberitahuan ini adalah secara otomatis dikonfirmasi oleh sistem dan tidak perlu membalas email ini.\n\n\n"
                    + "Konfirmasi via Sistem";
            PdfPCell bySistem = new PdfPCell(new Phrase(created2, small));
            bySistem.setBorder(Rectangle.NO_BORDER);
            bySistem.setColspan(5);
            bySistem.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            my_report_table.addCell(bySistem);

            //Create QR Code by using BarcodeQRCode Class
            BarcodeQRCode my_code = new BarcodeQRCode(DateUtil.getDateStr(new Date(), "dd-MMM-yyyy HH:mm:ss") + "_" + getCostCenter(koda).getStDescription(), 3, 3, null);
            //Get Image corresponding to the input string
            Image qr_image = my_code.getImage();

            PdfPCell barcode = new PdfPCell(qr_image);
            barcode.setBorder(Rectangle.NO_BORDER);
            barcode.setColspan(5);
            barcode.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(barcode);

            my_report_table.setWidths(new int[]{20, 20, 20, 20, 20});

            /* Attach report table to PDF */
            my_pdf_report.add(my_report_table);
            my_pdf_report.close();

            //batas lampiran OS polis
            ResultSet query_set = S.executeQuery(queryPiutangPremi);
            String fileName2 = "lampiranpolis_" + Parameter.readString("BRANCH_SHORT_" + koda).replace("-", "") + DateUtil.getDateStr(new Date(), "yyyyMMdd");

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

//            Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12),
//                    smallb = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD),
//                    smallc = new Font(Font.FontFamily.TIMES_ROMAN, 11);

            //insert headings
            PdfPCell Title2 = new PdfPCell(new Phrase("Lampiran Outstanding Polis",
                    smallb));
            Title2.setBorder(Rectangle.NO_BORDER);
            Title2.setColspan(6);
            Title2.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(Title2);
            my_report_table2.completeRow();

            //insert an empty row
            my_report_table2.completeRow();

            //insert column headings
            Phrase p[] = {new Phrase("No", smallb),
                new Phrase("No. Polis", smallb),
                new Phrase("Tertanggung", smallb),
                new Phrase("Tanggal Polis", smallb),
                new Phrase("Premi Bruto", smallb),
                new Phrase("Premi Netto", smallb)};

            PdfPCell judno = new PdfPCell(p[0]),
                    judpol = new PdfPCell(p[1]),
                    judname = new PdfPCell(p[2]),
                    judtanggal = new PdfPCell(p[3]),
                    judkomisi = new PdfPCell(p[4]),
                    judnobuk = new PdfPCell(p[5]);

            //insert column headings
            judno.setHorizontalAlignment(judno.ALIGN_CENTER);
            judno.setVerticalAlignment(judno.ALIGN_MIDDLE);
            judno.setGrayFill(0.7f);
            judpol.setHorizontalAlignment(judpol.ALIGN_CENTER);
            judpol.setVerticalAlignment(judpol.ALIGN_MIDDLE);
            judpol.setGrayFill(0.7f);
            judname.setHorizontalAlignment(judname.ALIGN_CENTER);
            judname.setVerticalAlignment(judname.ALIGN_MIDDLE);
            judname.setGrayFill(0.7f);
            judtanggal.setHorizontalAlignment(judtanggal.ALIGN_CENTER);
            judtanggal.setVerticalAlignment(judtanggal.ALIGN_MIDDLE);
            judtanggal.setGrayFill(0.7f);
            judkomisi.setHorizontalAlignment(judkomisi.ALIGN_CENTER);
            judkomisi.setVerticalAlignment(judkomisi.ALIGN_MIDDLE);
            judkomisi.setGrayFill(0.7f);
            judnobuk.setHorizontalAlignment(judnobuk.ALIGN_CENTER);
            judnobuk.setVerticalAlignment(judnobuk.ALIGN_MIDDLE);
            judnobuk.setGrayFill(0.7f);

            my_report_table2.addCell(judno);
            my_report_table2.addCell(judpol);
            my_report_table2.addCell(judname);
            my_report_table2.addCell(judtanggal);
            my_report_table2.addCell(judkomisi);
            my_report_table2.addCell(judnobuk);

            //insert column data
            PdfPCell table_cell2 = null;
            BigDecimal premiTotal = new BigDecimal(0);
            BigDecimal premiNetTotal = new BigDecimal(0);

            while (query_set.next()) {
                BigDecimal no = query_set.getBigDecimal("no");
                table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(no, 0), small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                my_report_table2.addCell(table_cell2);

                String pol_no = query_set.getString("pol_no");
                table_cell2 = new PdfPCell(new Phrase(pol_no, small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                my_report_table2.addCell(table_cell2);

                String custname = query_set.getString("tertanggung");
                if (custname.length() > 30) {
                    custname = custname.substring(0, 30);
                } else {
                    custname = custname.substring(0, custname.length());
                }
                table_cell2 = new PdfPCell(new Phrase(custname, small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                my_report_table2.addCell(table_cell2);

                String tanggal = DateUtil.getDateStr(query_set.getDate("policy_date"), "dd/MM/yyyy");
                table_cell2 = new PdfPCell(new Phrase(tanggal, small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                my_report_table2.addCell(table_cell2);

                BigDecimal premi = query_set.getBigDecimal("amount");
                premiTotal = BDUtil.add(premiTotal, premi);
                table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(premi, 2), small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                my_report_table2.addCell(table_cell2);

                BigDecimal preminet = query_set.getBigDecimal("preminett");
                premiNetTotal = BDUtil.add(premiNetTotal, preminet);
                table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(preminet, 2), small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                my_report_table2.addCell(table_cell2);
            }

            table_cell2 = new PdfPCell(new Phrase("SUBTOTAL", smallb));
            table_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell2.setColspan(4);
            my_report_table2.addCell(table_cell2);
            table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(premiTotal, 2), small10bold));
            table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table2.addCell(table_cell2);
            table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(premiNetTotal, 2), small10bold));
            table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table2.addCell(table_cell2);

            String created = "Created by System " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "dd ^^ yyyy HH:mm:ss"));
            PdfPCell bySistem2 = new PdfPCell(new Phrase(created, small6));
            bySistem2.setBorder(Rectangle.NO_BORDER);
            bySistem2.setColspan(6);
            bySistem2.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(bySistem2);
            my_report_table2.completeRow();

            //Create QR Code by using BarcodeQRCode Class
            BarcodeQRCode my_code2 = new BarcodeQRCode(DateUtil.getDateStr(new Date(), "dd-MMM-yyyy HH:mm:ss") + "_" + JSPUtil.printX(premiTotal, 2), 3, 3, null);
            //Get Image corresponding to the input string
            Image qr_image2 = my_code2.getImage();

            PdfPCell barcode2 = new PdfPCell(qr_image2);
            barcode2.setBorder(Rectangle.NO_BORDER);
            barcode2.setColspan(6);
            barcode2.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(barcode2);
            my_report_table2.completeRow();

            my_report_table2.setWidths(new int[]{5, 20, 35, 10, 15, 15});

            /* Attach report table to PDF */
            my_pdf_report2.add(my_report_table2);
            my_pdf_report2.close();

            /* Close all DB related objects */
            query_set.close();
            query_set2.close();
            S.close();

            String receiver = getCostCenter(koda).getStEmail() + "," + getCostCenter(koda).getStEmail2();
            String Cc = "internal.audit@askrida.co.id";
            String subject = " (NO-REPLY) Laporan OS Premi (30 Hari) Per Hari Ini " + hariIni;
            String text = "Dengan hormat,\n\n"
                    + "Bersama ini kami lampirkan “Laporan OS Premi (30 Hari)” yang dikirim otomatis oleh sistem.\n"
                    + "Atas perhatian dan kerjasamanya, kami ucapkan terima kasih.\n\n\n"
                    + "Hormat kami,\n"
                    + "Administrator";

            MailUtil2 mail = new MailUtil2();
            mail.sendEmailMultiFile(pathTemp, fileName, pathTemp2, fileName2, null, null, null, null, null, null, receiver, Cc, subject, text);
//            mail.sendEmailMultiFileNoReceiver(pathTemp, fileName, pathTemp2, fileName2, null, null, null, null, null, null, subject, text);

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 2 selesai dalam " + t + " ms");

            //HAPUS FILE TEXT DI LOCAL SERVER CORE
            boolean del = fo.delete();
            if (del) {
                logger.logWarning("delete file di core server " + fo.getName() + "...");
            } else {
                logger.logWarning("gagal delete file di core server " + fo.getName() + "...");
            }

            boolean del2 = fo2.delete();
            if (del2) {
                logger.logWarning("delete file di core server " + fo2.getName() + "...");
            } else {
                logger.logWarning("gagal delete file di core server " + fo2.getName() + "...");
            }

            retrievePiutang30(queryPiutangPremi, requestNo);

        } finally {
            conn.close();
        }
    }

    public void retrievePiutang30(String queryPiutangPremi, String requestNo) throws Exception {
        headerPiutang30 = new UploadHeaderPiutangPremi30View();
        headerPiutang30.markNew();

        final DTOList details = new DTOList();
        headerPiutang30.setDetails(details);

        DTOList l = ListUtil.getDTOListFromQuery(
                queryPiutangPremi,
                HashDTO.class);

        for (int i = 0; i < l.size(); i++) {
            HashDTO h = (HashDTO) l.get(i);

            uploadPiutangPremi30View piutang = new uploadPiutangPremi30View();
            piutang.markNew();

            piutang.setStPolicyNo(h.getFieldValueByFieldNameST("pol_no"));
            piutang.setStTertanggung(h.getFieldValueByFieldNameST("tertanggung"));
            piutang.setDtPolicyDate(h.getFieldValueByFieldNameDT("policy_date"));
            piutang.setStCostCenterCode(h.getFieldValueByFieldNameST("cc_code"));
            piutang.setStPolicyID(h.getFieldValueByFieldNameST("pol_id"));
            piutang.setDbAmount(new BigDecimal(h.getFieldValueByFieldNameBD("amount").doubleValue()));
            piutang.setDbPremiNetto(new BigDecimal(h.getFieldValueByFieldNameBD("preminett").doubleValue()));

            details.add(piutang);
        }

        headerPiutang30.setDetails(details);

        BigDecimal totalAmount = null;
        for (int i = 0; i < details.size(); i++) {
            uploadPiutangPremi30View object = (uploadPiutangPremi30View) details.get(i);

            totalAmount = BDUtil.add(totalAmount, object.getDbAmount());

        }

        for (int i = 0; i < details.size(); i++) {
            uploadPiutangPremi30View object = (uploadPiutangPremi30View) details.get(i);

            object.setStDataAmount(String.valueOf(details.size()));
            object.setDbAmountTotal(totalAmount);

        }

        headerPiutang30.setStDataAmount(String.valueOf(details.size()));
        headerPiutang30.setDbAmountTotal(totalAmount);
        headerPiutang30.setStNoSuratHutang(requestNo);

        saveWarningPiutang30(headerPiutang30, headerPiutang30.getDetails());
    }

    public void saveWarningPiutang30(UploadHeaderPiutangPremi30View header, DTOList l) throws Exception {
        logger.logDebug("saveUploadSpreading: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        if (header.isNew() && header.getStInsuranceUploadID() == null) {
            header.setStInsuranceUploadID(String.valueOf(IDFactory.createNumericID("INSPIUTANGPREMI30ID")));
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                uploadPiutangPremi30View j = (uploadPiutangPremi30View) l.get(i);

                if (header.getStInsuranceUploadID() != null) {
                    j.setStInsuranceUploadID(header.getStInsuranceUploadID());
                    j.setStNoSuratHutang(header.getStNoSuratHutang());

                    j.setStCreateWho("admin");
                    j.setDtCreateDate(new Date());
                }

                if (j.isNew()) {
                    j.setStInsuranceUploadDetailID(String.valueOf(IDFactory.createNumericID("INSPIUTANGPREMIDTL30ID")));
                }

            }

            S.store(l);

        } catch (Exception e) {
            throw e;
        } finally {
            S.release();
        }
    }

    public void validasicabang75() throws Exception {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int hourAuto = 5;

//        if (hourAuto == hour) {

            String query = "select a.cc_code,a.attr_pol_no as pol_no,a.mutation_date,date_part('day','" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd 00:00:00") + "' -a.mutation_date) as hari,"
                    + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1 else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total,"
                    + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_desc = 'Komisi', c.amount))*-1 else sum(getpremi2(d.item_desc = 'Komisi', c.amount)) end as nd_comm1,"
                    + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'HFEE', c.amount))*-1 else sum(getpremi2(d.category = 'HFEE', c.amount)) end as nd_hfee,"
                    + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category= 'BROKERAGE' and d.item_desc <> 'PPN', c.amount))*-1 else "
                    + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) end as nd_brok1,"
                    + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn,"
                    + "sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,"
                    + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'DISC', c.amount))*-1 else sum(getpremi2(d.category = 'DISC', c.amount)) end as nd_disc1 "
                    + "from ar_invoice a inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                    + "left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                    + "inner join ent_master b on b.ent_id = a.ent_id "
                    + "where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' "
                    + "and " + Parameter.readString("WARNING_PIUTANG_BPD") + " "
                    + "and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7) "
                    + "and substr(a.attr_pol_no,1,16) in ( select pol_no from ( "
                    + "select substr(a.attr_pol_no,1,16) as pol_no,"
                    + "sum(getpremi(a.ar_trx_type_id = 10,getpremi2(d.item_class in ('PREMIG'), c.amount)*-1,getpremi2(d.item_class in ('PREMIG'), c.amount))) as premi_total,"
                    + "sum(case when a.ar_trx_type_id = 10 then (getpremi2(d.item_desc = 'Komisi', c.amount)*-1) else (getpremi2(d.item_desc = 'Komisi', c.amount)) end) as nd_comm1,"
                    + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
                    + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn,sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,"
                    + "sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1 "
                    + "from ar_invoice a inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                    + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                    + "inner join ent_master b on b.ent_id = a.ent_id "
                    + "where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' "
                    + "and " + Parameter.readString("WARNING_PIUTANG_BPD") + " "
                    + "and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) "
                    + "and a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021' "
                    + "and date_part('day','" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd 00:00:00") + "' - a.mutation_date) = '75' "
                    + "and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd 00:00:00") + "' ) "
                    + "group by substr(a.attr_pol_no,1,16) ) a where (premi_total <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn <> 0 or nd_disc1 <> 0) ) "
                    + "and date_part('day','" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd 00:00:00") + "' - a.mutation_date) = '75' "
                    + "and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd 00:00:00") + "' ) "
                    + "group by a.cc_code,a.mutation_date,a.ar_trx_type_id,a.attr_pol_no order by a.cc_code,a.attr_pol_no ";

            DTOList cabang = null;
            cabang = ListUtil.getDTOListFromQuery(
                    "select a.cc_code from ( "
                    + query + " ) a group by a.cc_code order by a.cc_code ",
                    GLCostCenterView.class);

            for (int i = 0; i < cabang.size(); i++) {
                GLCostCenterView cab = (GLCostCenterView) cabang.get(i);

                executeCab75(cab.getStCostCenterCode());

            }
//        }
    }

    public void executeCab75(String koda) throws Exception {
        final Connection conn = ConnectionCache.getInstance().getConnection();

        try {

            long t = System.currentTimeMillis();

            final Statement S = conn.createStatement();

            String queryPiutangPremi = " select row_number() over(order by a.cc_code,a.pol_no) as no,a.cc_code,a.pol_id::text,a.pol_no,a.tertanggung,a.mutation_date as policy_date,a.hari,a.premibru as amount,"
                    + "(a.premibru-a.nd_comm1-a.nd_hfee-a.nd_brok1-a.nd_ppn-a.nd_feebase1-a.nd_disc1) as preminett "
                    + "from ( select a.cc_code,a.pol_id,a.pol_no,a.tertanggung,a.mutation_date,a.hari,(a.premi_total+a.nd_sfee+a.nd_pcost) as premibru,"
                    + "a.nd_comm1,a.nd_hfee,a.nd_brok1,a.nd_ppn,a.nd_feebase1,a.nd_disc1 "
                    + "from ( select a.cc_code,a.attr_pol_id as pol_id,a.attr_pol_no as pol_no,a.mutation_date,date_part('day','" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd 00:00:00") + "' - a.mutation_date) as hari,a.attr_pol_name as tertanggung,"
                    + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_class = 'PREMIG', c.amount))*-1 else sum(getpremi2(d.item_class = 'PREMIG', c.amount)) end as premi_total,"
                    + "sum(getpremi2(d.category = 'STAMPDUTY', c.amount)) as nd_sfee,"
                    + "sum(getpremi2(d.category = 'PCOST', c.amount)) as nd_pcost,"
                    + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.item_desc = 'Komisi', c.amount))*-1 else sum(getpremi2(d.item_desc = 'Komisi', c.amount)) end as nd_comm1,"
                    + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'HFEE', c.amount))*-1 else sum(getpremi2(d.category = 'HFEE', c.amount)) end as nd_hfee,"
                    + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category= 'BROKERAGE' and d.item_desc <> 'PPN', c.amount))*-1 else "
                    + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) end as nd_brok1,"
                    + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn,"
                    + "sum(getpremi2(d.category = 'TAXBROK', c.amount)) as nd_taxbrok1,sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,"
                    + "case when a.ar_trx_type_id = 10 then sum(getpremi2(d.category = 'DISC', c.amount))*-1 else sum(getpremi2(d.category = 'DISC', c.amount)) end as nd_disc1 "
                    + "from ar_invoice a inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                    + "left join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                    + "inner join ent_master b on b.ent_id = a.ent_id "
                    + "where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' "
                    + "and " + Parameter.readString("WARNING_PIUTANG_BPD") + " "
                    + "and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7) and a.cc_code = '" + koda + "' "
                    + "and substr(a.attr_pol_no,1,16) in ( select pol_no from ( "
                    + "select substr(a.attr_pol_no,1,16) as pol_no,"
                    + "sum(getpremi(a.ar_trx_type_id = 10,getpremi2(d.item_class in ('PREMIG'), c.amount)*-1,getpremi2(d.item_class in ('PREMIG'), c.amount))) as premi_total,"
                    + "sum(case when a.ar_trx_type_id = 10 then (getpremi2(d.item_desc = 'Komisi', c.amount)*-1) else (getpremi2(d.item_desc = 'Komisi', c.amount)) end) as nd_comm1,"
                    + "sum(getpremi2(d.category = 'HFEE', c.amount)) as nd_hfee,sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc <> 'PPN', c.amount)) as nd_brok1,"
                    + "sum(getpremi2(d.category = 'BROKERAGE' and d.item_desc = 'PPN', c.amount)) as nd_ppn,sum(getpremi2(d.item_desc = 'Fee Base', c.amount)) as nd_feebase1,"
                    + "sum(getpremi2(d.category = 'DISC', c.amount)) as nd_disc1 "
                    + "from ar_invoice a inner join ar_invoice_details c on c.ar_invoice_id = a.ar_invoice_id "
                    + "inner join ar_trx_line d on d.ar_trx_line_id = c.ar_trx_line_id "
                    + "inner join ent_master b on b.ent_id = a.ent_id "
                    + "where a.posted_flag = 'Y' and coalesce(a.cancel_flag,'') <> 'Y' "
                    + "and " + Parameter.readString("WARNING_PIUTANG_BPD") + " "
                    + "and a.invoice_type in ('AR','AP') and a.ar_trx_type_id in (5,6,7,10) and a.cc_code = '" + koda + "'"
                    + "and a.ar_trx_type_id||''||a.attr_pol_type_id <> '1021' "
                    + "and date_part('day','" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd 00:00:00") + "' - a.mutation_date) = '75' "
                    + "and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd 00:00:00") + "' ) "
                    + "group by substr(a.attr_pol_no,1,16) ) a where (premi_total <> 0 or nd_hfee <> 0 or nd_brok1 <> 0 or nd_comm1 <> 0 or nd_feebase1 <> 0 or nd_ppn <> 0 or nd_disc1 <> 0) ) "
                    + "and date_part('day','" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd 00:00:00") + "' - a.mutation_date) = '75' "
                    + "and (a.receipt_date is null or date_trunc('day',a.receipt_date) > '" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd 00:00:00") + "' ) "
                    + "group by a.cc_code,a.mutation_date,a.ar_trx_type_id,a.attr_pol_id,a.attr_pol_no,a.attr_pol_name order by a.cc_code,a.attr_pol_no "
                    + ") a ) a where a.premibru <> 0 order by a.cc_code,a.pol_no ";

            /* Define the SQL query */
            ResultSet query_set2 = S.executeQuery(queryPiutangPremi);

            logger.logInfo("!!!! jalankan : proses send report");

            String fileName = "konfirmasiOSpremi_" + Parameter.readString("BRANCH_SHORT_" + koda).replace("-", "") + DateUtil.getDateStr(new Date(), "yyyyMMdd");
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
            PdfPTable my_report_table = new PdfPTable(5);
            my_report_table.setWidthPercentage(100);
            //create a cell object

            Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12),
                    smallb = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD),
                    smallc = new Font(Font.FontFamily.TIMES_ROMAN, 11),
                    small10 = new Font(Font.FontFamily.TIMES_ROMAN, 10),
                    small10bold = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD),
                    small6 = new Font(Font.FontFamily.TIMES_ROMAN, 6);
            //create a cell object

            String requestNo = null;
            String counterKey = DateUtil.getYear2Digit(new Date()) + DateUtil.getMonth2Digit(new Date());
//            String counterKey = DateUtil.getYear(new Date());
            String rn = String.valueOf(IDFactory.createNumericID("REQOSPREMI75" + counterKey, 1));
            rn = StringTools.leftPad(rn, '0', 3);

            requestNo = rn + "/KEU-OS75/" + Parameter.readString("BRANCH_SHORT_" + koda) + "/" + DateUtil.getMonth2Digit(new Date()) + "/" + DateUtil.getYear(new Date());

            //insert heading
            String hariIni = LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "dd ^^ yyyy"));
            String Tanggal = "Jakarta, " + hariIni;
            Image askridaLogoPath = Image.getInstance(Parameter.readString("DIGITAL_POLIS_LOGO_PIC"));

            PdfPCell logo = null;
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setColspan(2);
            my_report_table.addCell(logo);
            logo = new PdfPCell(askridaLogoPath);
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(logo);
            logo = new PdfPCell(new Phrase(" ", small));
            logo.setBorder(Rectangle.NO_BORDER);
            logo.setColspan(2);
            my_report_table.addCell(logo);

            PdfPCell shkTanggal = null;
            shkTanggal = new PdfPCell(new Phrase("No. : " + requestNo, small));
            shkTanggal.setBorder(Rectangle.NO_BORDER);
            shkTanggal.setHorizontalAlignment(Element.ALIGN_LEFT);
            shkTanggal.setColspan(2);
            my_report_table.addCell(shkTanggal);
            shkTanggal = new PdfPCell(new Phrase(Tanggal, small));
            shkTanggal.setBorder(Rectangle.NO_BORDER);
            shkTanggal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            shkTanggal.setColspan(3);
            my_report_table.addCell(shkTanggal);

            //insert heading
            String created1 = "Kepada Yth.\n"
                    + "Kepala Cabang " + getCostCenter(koda).getStDescription() + "\n"
                    + getCostCenter(koda).getStAddress() + "\n\n\n";
            PdfPCell heading = new PdfPCell(new Phrase(created1, small));
            heading.setBorder(Rectangle.NO_BORDER);
            heading.setColspan(2);
            heading.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(heading);
            heading = new PdfPCell(new Phrase(" ", small));
            heading.setBorder(Rectangle.NO_BORDER);
            heading.setHorizontalAlignment(Element.ALIGN_CENTER);
            heading.setColspan(3);
            my_report_table.addCell(heading);

            //insert judul
            PdfPCell Title = new PdfPCell(new Phrase("Hal : Konfirmasi Outstanding Premi Umur 75 Hari",
                    smallb));
            Title.setBorder(Rectangle.NO_BORDER);
            Title.setColspan(5);
            Title.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(Title);

            //insert an empty row
            PdfPCell emptyRow = new PdfPCell(new Phrase(" ", small));
            emptyRow.setBorder(Rectangle.NO_BORDER);
            emptyRow.setColspan(5);
            emptyRow.setHorizontalAlignment(Element.ALIGN_CENTER);
            my_report_table.addCell(emptyRow);

            //insert isi
            BigDecimal premiNet = new BigDecimal(0);

            while (query_set2.next()) {
                BigDecimal premi = query_set2.getBigDecimal("preminett");
                premiNet = BDUtil.add(premiNet, premi);
            }

            String created2 = "Diberitahukan kepada Saudara/i bahwa per tanggal " + hariIni + ", polis yang tercatat sebagai "
                    + "Outstanding premi umur 75 hari adalah terlampir dengan total premi netto Rp. " + JSPUtil.printX(premiNet, 2) + ".\n\n"
                    + "Menunjuk pada SK Direksi No: SK.013/DIR/2016 Bab XI pasal 20 maka Outstanding premi tersebut agar dapat ditindaklanjuti.\n\n"
                    + "Surat pemberitahuan ini adalah secara otomatis dikonfirmasi oleh sistem dan tidak perlu membalas email ini.\n\n\n"
                    + "Konfirmasi via Sistem";
            PdfPCell bySistem = new PdfPCell(new Phrase(created2, small));
            bySistem.setBorder(Rectangle.NO_BORDER);
            bySistem.setColspan(5);
            bySistem.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            my_report_table.addCell(bySistem);

            //Create QR Code by using BarcodeQRCode Class
            BarcodeQRCode my_code = new BarcodeQRCode(DateUtil.getDateStr(new Date(), "dd-MMM-yyyy HH:mm:ss") + "_" + getCostCenter(koda).getStDescription(), 3, 3, null);
            //Get Image corresponding to the input string
            Image qr_image = my_code.getImage();

            PdfPCell barcode = new PdfPCell(qr_image);
            barcode.setBorder(Rectangle.NO_BORDER);
            barcode.setColspan(5);
            barcode.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table.addCell(barcode);

            my_report_table.setWidths(new int[]{20, 20, 20, 20, 20});

            /* Attach report table to PDF */
            my_pdf_report.add(my_report_table);
            my_pdf_report.close();

            //batas lampiran OS polis
            ResultSet query_set = S.executeQuery(queryPiutangPremi);
            String fileName2 = "lampiranpolis_" + Parameter.readString("BRANCH_SHORT_" + koda).replace("-", "") + DateUtil.getDateStr(new Date(), "yyyyMMdd");

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

//            Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12),
//                    smallb = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD),
//                    smallc = new Font(Font.FontFamily.TIMES_ROMAN, 11);

            //insert headings
            PdfPCell Title2 = new PdfPCell(new Phrase("Lampiran Outstanding Polis",
                    smallb));
            Title2.setBorder(Rectangle.NO_BORDER);
            Title2.setColspan(6);
            Title2.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(Title2);
            my_report_table2.completeRow();

            //insert an empty row
            my_report_table2.completeRow();

            //insert column headings
            Phrase p[] = {new Phrase("No", smallb),
                new Phrase("No. Polis", smallb),
                new Phrase("Tertanggung", smallb),
                new Phrase("Tanggal Polis", smallb),
                new Phrase("Premi Bruto", smallb),
                new Phrase("Premi Netto", smallb)};

            PdfPCell judno = new PdfPCell(p[0]),
                    judpol = new PdfPCell(p[1]),
                    judname = new PdfPCell(p[2]),
                    judtanggal = new PdfPCell(p[3]),
                    judkomisi = new PdfPCell(p[4]),
                    judnobuk = new PdfPCell(p[5]);

            //insert column headings
            judno.setHorizontalAlignment(judno.ALIGN_CENTER);
            judno.setVerticalAlignment(judno.ALIGN_MIDDLE);
            judno.setGrayFill(0.7f);
            judpol.setHorizontalAlignment(judpol.ALIGN_CENTER);
            judpol.setVerticalAlignment(judpol.ALIGN_MIDDLE);
            judpol.setGrayFill(0.7f);
            judname.setHorizontalAlignment(judname.ALIGN_CENTER);
            judname.setVerticalAlignment(judname.ALIGN_MIDDLE);
            judname.setGrayFill(0.7f);
            judtanggal.setHorizontalAlignment(judtanggal.ALIGN_CENTER);
            judtanggal.setVerticalAlignment(judtanggal.ALIGN_MIDDLE);
            judtanggal.setGrayFill(0.7f);
            judkomisi.setHorizontalAlignment(judkomisi.ALIGN_CENTER);
            judkomisi.setVerticalAlignment(judkomisi.ALIGN_MIDDLE);
            judkomisi.setGrayFill(0.7f);
            judnobuk.setHorizontalAlignment(judnobuk.ALIGN_CENTER);
            judnobuk.setVerticalAlignment(judnobuk.ALIGN_MIDDLE);
            judnobuk.setGrayFill(0.7f);

            my_report_table2.addCell(judno);
            my_report_table2.addCell(judpol);
            my_report_table2.addCell(judname);
            my_report_table2.addCell(judtanggal);
            my_report_table2.addCell(judkomisi);
            my_report_table2.addCell(judnobuk);

            //insert column data
            PdfPCell table_cell2 = null;
            BigDecimal premiTotal = new BigDecimal(0);
            BigDecimal premiNetTotal = new BigDecimal(0);

            while (query_set.next()) {
                BigDecimal no = query_set.getBigDecimal("no");
                table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(no, 0), small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                my_report_table2.addCell(table_cell2);

                String pol_no = query_set.getString("pol_no");
                table_cell2 = new PdfPCell(new Phrase(pol_no, small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                my_report_table2.addCell(table_cell2);

                String custname = query_set.getString("tertanggung");
                if (custname.length() > 30) {
                    custname = custname.substring(0, 30);
                } else {
                    custname = custname.substring(0, custname.length());
                }
                table_cell2 = new PdfPCell(new Phrase(custname, small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                my_report_table2.addCell(table_cell2);

                String tanggal = DateUtil.getDateStr(query_set.getDate("policy_date"), "dd/MM/yyyy");
                table_cell2 = new PdfPCell(new Phrase(tanggal, small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                my_report_table2.addCell(table_cell2);

                BigDecimal premi = query_set.getBigDecimal("amount");
                premiTotal = BDUtil.add(premiTotal, premi);
                table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(premi, 2), small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                my_report_table2.addCell(table_cell2);

                BigDecimal preminet = query_set.getBigDecimal("preminett");
                premiNetTotal = BDUtil.add(premiNetTotal, preminet);
                table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(preminet, 2), small10));
                table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                my_report_table2.addCell(table_cell2);
            }

            table_cell2 = new PdfPCell(new Phrase("SUBTOTAL", smallb));
            table_cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            table_cell2.setColspan(4);
            my_report_table2.addCell(table_cell2);
            table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(premiTotal, 2), small10bold));
            table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table2.addCell(table_cell2);
            table_cell2 = new PdfPCell(new Phrase(JSPUtil.printX(premiNetTotal, 2), small10bold));
            table_cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            my_report_table2.addCell(table_cell2);

            String created = "Created by System " + LanguageManager.getInstance().translate(DateUtil.getDateStr(new Date(), "dd ^^ yyyy HH:mm:ss"));
            PdfPCell bySistem2 = new PdfPCell(new Phrase(created, small6));
            bySistem2.setBorder(Rectangle.NO_BORDER);
            bySistem2.setColspan(6);
            bySistem2.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(bySistem2);
            my_report_table2.completeRow();

            //Create QR Code by using BarcodeQRCode Class
            BarcodeQRCode my_code2 = new BarcodeQRCode(DateUtil.getDateStr(new Date(), "dd-MMM-yyyy HH:mm:ss") + "_" + JSPUtil.printX(premiTotal, 2), 3, 3, null);
            //Get Image corresponding to the input string
            Image qr_image2 = my_code2.getImage();

            PdfPCell barcode2 = new PdfPCell(qr_image2);
            barcode2.setBorder(Rectangle.NO_BORDER);
            barcode2.setColspan(6);
            barcode2.setHorizontalAlignment(Element.ALIGN_LEFT);
            my_report_table2.addCell(barcode2);
            my_report_table2.completeRow();

            my_report_table2.setWidths(new int[]{5, 20, 35, 10, 15, 15});

            /* Attach report table to PDF */
            my_pdf_report2.add(my_report_table2);
            my_pdf_report2.close();

            /* Close all DB related objects */
            query_set.close();
            query_set2.close();
            S.close();

            String receiver = getCostCenter(koda).getStEmail() + "," + getCostCenter(koda).getStEmail2();
            String Cc = "internal.audit@askrida.co.id";
            String subject = " (NO-REPLY) Laporan OS Premi (75 Hari) Per Hari Ini " + hariIni;
            String text = "Dengan hormat,\n\n"
                    + "Bersama ini kami lampirkan “Laporan OS Premi (75 Hari)” yang dikirim otomatis oleh sistem.\n"
                    + "Atas perhatian dan kerjasamanya, kami ucapkan terima kasih.\n\n\n"
                    + "Hormat kami,\n"
                    + "Administrator";

            MailUtil2 mail = new MailUtil2();
            mail.sendEmailMultiFile(pathTemp, fileName, pathTemp2, fileName2, null, null, null, null, null, null, receiver, Cc, subject, text);
//            mail.sendEmailMultiFileNoReceiver(pathTemp, fileName, pathTemp2, fileName2, null, null, null, null, null, null, subject, text);

            t = System.currentTimeMillis() - t;

            logger.logInfo("proses 2 selesai dalam " + t + " ms");

            //HAPUS FILE TEXT DI LOCAL SERVER CORE
            boolean del = fo.delete();
            if (del) {
                logger.logWarning("delete file di core server " + fo.getName() + "...");
            } else {
                logger.logWarning("gagal delete file di core server " + fo.getName() + "...");
            }

            boolean del2 = fo2.delete();
            if (del2) {
                logger.logWarning("delete file di core server " + fo2.getName() + "...");
            } else {
                logger.logWarning("gagal delete file di core server " + fo2.getName() + "...");
            }

            retrievePiutang75(queryPiutangPremi, requestNo);

        } finally {
            conn.close();
        }
    }

    public void retrievePiutang75(String queryPiutangPremi, String requestNo) throws Exception {
        headerPiutang75 = new UploadHeaderPiutangPremi75View();
        headerPiutang75.markNew();

        final DTOList details = new DTOList();
        headerPiutang75.setDetails(details);

        DTOList l = ListUtil.getDTOListFromQuery(
                queryPiutangPremi,
                HashDTO.class);

        for (int i = 0; i < l.size(); i++) {
            HashDTO h = (HashDTO) l.get(i);

            uploadPiutangPremi75View piutang = new uploadPiutangPremi75View();
            piutang.markNew();

            piutang.setStPolicyNo(h.getFieldValueByFieldNameST("pol_no"));
            piutang.setStTertanggung(h.getFieldValueByFieldNameST("tertanggung"));
            piutang.setDtPolicyDate(h.getFieldValueByFieldNameDT("policy_date"));
            piutang.setStCostCenterCode(h.getFieldValueByFieldNameST("cc_code"));
            piutang.setStPolicyID(h.getFieldValueByFieldNameST("pol_id"));
            piutang.setDbAmount(new BigDecimal(h.getFieldValueByFieldNameBD("amount").doubleValue()));
            piutang.setDbPremiNetto(new BigDecimal(h.getFieldValueByFieldNameBD("preminett").doubleValue()));

            details.add(piutang);
        }

        headerPiutang75.setDetails(details);

        BigDecimal totalAmount = null;
        for (int i = 0; i < details.size(); i++) {
            uploadPiutangPremi75View object = (uploadPiutangPremi75View) details.get(i);

            totalAmount = BDUtil.add(totalAmount, object.getDbAmount());

        }

        for (int i = 0; i < details.size(); i++) {
            uploadPiutangPremi75View object = (uploadPiutangPremi75View) details.get(i);

            object.setStDataAmount(String.valueOf(details.size()));
            object.setDbAmountTotal(totalAmount);

        }

        headerPiutang75.setStDataAmount(String.valueOf(details.size()));
        headerPiutang75.setDbAmountTotal(totalAmount);
        headerPiutang75.setStNoSuratHutang(requestNo);

        saveWarningPiutang75(headerPiutang75, headerPiutang75.getDetails());
    }

    public void saveWarningPiutang75(UploadHeaderPiutangPremi75View header, DTOList l) throws Exception {
        logger.logDebug("saveUploadSpreading: " + l);

        if (l.size() < 1) {
            return;
        }

        final SQLUtil S = new SQLUtil();

        if (header.isNew() && header.getStInsuranceUploadID() == null) {
            header.setStInsuranceUploadID(String.valueOf(IDFactory.createNumericID("INSPIUTANGPREMI75ID")));
        }

        try {
            for (int i = 0; i < l.size(); i++) {
                uploadPiutangPremi75View j = (uploadPiutangPremi75View) l.get(i);

                if (header.getStInsuranceUploadID() != null) {
                    j.setStInsuranceUploadID(header.getStInsuranceUploadID());
                    j.setStNoSuratHutang(header.getStNoSuratHutang());

                    j.setStCreateWho("admin");
                    j.setDtCreateDate(new Date());
                }

                if (j.isNew()) {
                    j.setStInsuranceUploadDetailID(String.valueOf(IDFactory.createNumericID("INSPIUTANGPREMIDTL75ID")));
                }

            }

            S.store(l);

        } catch (Exception e) {
            throw e;
        } finally {
            S.release();
        }
    }
}
