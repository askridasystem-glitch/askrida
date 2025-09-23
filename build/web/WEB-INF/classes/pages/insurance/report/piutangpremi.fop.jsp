<%@ page import="com.webfin.insurance.form.InsuranceUploadForm,
         com.crux.web.controller.SessionManager,
         java.util.ArrayList,
         com.crux.util.*,
         com.crux.util.fop.FOPUtil,
         com.webfin.insurance.model.uploadPiutangPremiView,
         java.math.BigDecimal,
         com.crux.common.parameter.Parameter,
         com.crux.lang.LanguageManager,
         java.util.Date"%><?xml version="1.0" ?>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines the layout master -->
    <fo:layout-master-set>
        <fo:simple-page-master master-name="first"
                               page-width="21cm"
                               page-height="30cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="1cm"
                               margin-right="1cm">
            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>
    </fo:layout-master-set>

    <fo:page-sequence master-reference="first">

        <%
                    uploadPiutangPremiView receipt = (uploadPiutangPremiView) request.getAttribute("PROPOSAL");

                    ArrayList colW = new ArrayList();

                    colW.add(new Integer(5));
                    colW.add(new Integer(20));
                    colW.add(new Integer(30));
                    colW.add(new Integer(10));
                    colW.add(new Integer(15));
                    colW.add(new Integer(15));
                    colW.add(new Integer(20));

                    SQLAssembler sqa = receipt.getSQAComm(receipt.getStInsuranceUploadID());
                    DTOList list = sqa.getList(uploadPiutangPremiView.class);
        %>


        <fo:flow flow-name="xsl-region-body">

            <fo:block font-size="12pt">
                <fo:table table-layout="fixed">
                    <fo:table-header>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block text-align="left">No. : <%=JSPUtil.print(receipt.getStNoSuratHutang())%></fo:block>
                                <fo:block text-align="left">Kepada Yth.</fo:block>
                                <fo:block text-align="left">Kepala Cabang <%=receipt.getCostCenter(receipt.getStCostCenterCode()).getStDescription()%></fo:block>
                                <fo:block text-align="left"><%=receipt.getCostCenter(receipt.getStCostCenterCode()).getStAddress()%></fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" ></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" ><fo:block text-align="right">Jakarta, <%=LanguageManager.getInstance().translate(DateUtil.getDateStr(receipt.getDtCreateDate(), "dd ^^ yyyy"))%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-header>

                    <%=FOPUtil.printColumnWidth(colW, 19, 1, "cm")%>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block text-align="center" font-weight="bold" space-before.optimum="10pt" space-after.optimum="10pt">Hal : Konfirmasi Outstanding Premi Umur 61 Hari</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                    BigDecimal[] q = new BigDecimal[1];
                                    for (int j = 0; j < list.size(); j++) {
                                        uploadPiutangPremiView view = (uploadPiutangPremiView) list.get(j);

                                        int n = 0;
                                        q[n] = BDUtil.add(q[n++], view.getDbPremiNetto());
                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block text-align="justify" space-before.optimum="2pt" space-after.optimum="2pt">Diberitahukan kepada Saudara/i bahwa per tanggal <%=LanguageManager.getInstance().translate(DateUtil.getDateStr(receipt.getDtCreateDate(), "dd ^^ yyyy"))%>, polis yang tercatat sebagai Outstanding premi umur 61 hari adalah terlampir dengan total premi netto Rp. <%=JSPUtil.printX(q[0], 2)%>.</fo:block>
                                <fo:block text-align="justify" space-before.optimum="2pt" space-after.optimum="2pt">Menunjuk pada SK Direksi No: SK.013/DIR/2016 Bab XI pasal 20 maka Outstanding premi tersebut agar dapat ditindaklanjuti.</fo:block>
                                <fo:block text-align="justify" space-before.optimum="2pt" space-after.optimum="2pt">Surat pemberitahuan ini adalah secara otomatis dikonfirmasi oleh sistem dan tidak perlu membalas email ini.</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block text-align="start" space-before.optimum="10pt" space-after.optimum="2pt">Konfirmasi via Sistem</fo:block>

            <fo:block text-align="start">
                <fo:instream-foreign-object>
                    <barcode:barcode
                        xmlns:barcode="http://barcode4j.krysalis.org/ns"
                        message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(receipt.getDbAmountTotal(), 0)%>" orientation="0">
                        <barcode:datamatrix>
                            <barcode:height>40pt</barcode:height>
                            <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                        </barcode:datamatrix>
                    </barcode:barcode>
                </fo:instream-foreign-object>
            </fo:block>

        </fo:flow>
    </fo:page-sequence>

    <!-- starts actual layout -->
    <fo:page-sequence master-reference="first">

        <fo:flow flow-name="xsl-region-body">
            <%
                        String bw = "0.2pt";
            %>

            <fo:block font-family="Helvetica" font-weight="bold" font-size="8pt" line-height="5mm">No. : <%=JSPUtil.print(receipt.getStNoSuratHutang())%></fo:block>
            <fo:block font-family="Helvetica" font-size="6pt" display-align="center" border-width="0.1pt">

                <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>" >
                    <fo:table-header>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >No</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >No Polis</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Tertanggung</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Tgl Polis</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Premi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Premi Nett</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Pelunasan</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-header>

                    <%=FOPUtil.printColumnWidth(colW, 19, 1, "cm")%>
                    <fo:table-body>

                        <%
                                    BigDecimal[] t = new BigDecimal[1];
                                    for (int j = 0; j < list.size(); j++) {
                                        uploadPiutangPremiView view = (uploadPiutangPremiView) list.get(j);

                                        int n = 0;
                                        t[n] = BDUtil.add(t[n++], view.getDbPremiNetto());
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.print(String.valueOf(j + 1))%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.print(view.getStPolicyNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="start" font-size="8pt"><%=JSPUtil.xmlEscape(view.getStTertanggung())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="start" font-size="8pt"><%=JSPUtil.print(view.getDtPolicyDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(view.getDbAmount(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(view.getDbPremiNetto(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(view.getStKeterangan())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="2pt" space-after.optimum="2pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" number-columns-spanned="4"><fo:block text-align="center" font-size="8pt" >SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(receipt.getDbAmountTotal(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" ><%=JSPUtil.printX(t[0], 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="6pt" >
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "dd/MM/yyyy HH:mm:ss"))%>
            </fo:block>

        </fo:flow>
    </fo:page-sequence>
</fo:root>