/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crux.util;

import com.crux.file.FileView;
import com.crux.pool.DTOPool;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pras
 */
public class PDFAdding {

    private static String DescriptionToPrint = "Hai...Saya Hasanul";
    private static int AlignmentofDescription = 3;
    private static float XofDescription = 100;
    private static float YofDescription = 100;
    private static float XofDescription2 = 200;
    private static float YofDescription2 = 100;
    private static float RotationofDescription = 0;

    public static void main(String[] args) {
        List<InputStream> list = new ArrayList<InputStream>();
        try {
            // Source pdfs
            list.add(new FileInputStream(new File("D:/fin-repository/report/041431311115000100.pdf")));
            list.add(new FileInputStream(new File("D:/fin-repository/report/041421211115000100.pdf")));

            // Resulting pdf
            OutputStream out = new FileOutputStream(new File("D:/fin-repository/report/result.pdf"));

            //doMerge(list, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPDF(String nama, String doc, String note, String status) {

        try {
            //PdfReader pdfReader = new PdfReader("D:/data/HelloWorld.pdf");
            PdfReader pdfReader = new PdfReader(getFiles(doc).getStFilePath());

            PdfStamper pdfStamper = new PdfStamper(pdfReader,
                    new FileOutputStream("D:/jboss-fin/server/default/deploy/fin.ear/fin.war/pages/incoming/report/preview.pdf"));

            //Image image = Image.getInstance("D:/data/agam indo.jpg");
            //image.scaleAbsolute(100, 50);
            //image.setAbsolutePosition(100f, 500f);

            BaseFont bf = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.WINANSI, false);

            for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {

                PdfContentByte content = pdfStamper.getUnderContent(i);
                //content.addImage(image);
                content.setFontAndSize(bf, 26);
                content.beginText();
                content.showTextAligned(content.ALIGN_CENTER, nama, XofDescription, YofDescription, RotationofDescription);
                content.showTextAligned(content.ALIGN_CENTER, note, XofDescription2, YofDescription2, RotationofDescription);
                content.endText();
            }

            pdfStamper.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addPDF2(String readerURL, String stamperURL, String text) {
        HttpURLConnection connection = null;
        try {

            URL url = new URL(readerURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);

            PdfReader pdfReader = new PdfReader("D:/data/HelloWorld.pdf");

            PdfStamper pdfStamper = new PdfStamper(pdfReader,
                    new FileOutputStream("D:/data/HelloWorld-Stamped.pdf"));

            BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, false);

            for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {

                PdfContentByte content = pdfStamper.getUnderContent(i);
                //content.addImage(image);
                content.setFontAndSize(bf, 26);
                content.beginText();
                content.showTextAligned(content.ALIGN_CENTER, DescriptionToPrint.toString(), XofDescription, YofDescription, RotationofDescription);
                content.endText();

            }

            pdfStamper.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public FileView getFiles(String stFilesID) {
        return (FileView) DTOPool.getInstance().getDTO(FileView.class, stFilesID);
    }
}
