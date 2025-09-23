<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.webfin.entity.model.EntityAddressView,
         com.crux.util.*,
         java.util.Date,
         com.crux.common.parameter.Parameter,
         com.crux.util.fop.FOPUtil"%><?xml version="1.0" encoding="utf-8"?>
<%

            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");
            final String attached = (String) request.getAttribute("attached");
            String param[] = attached.split("[\\|]");

            boolean isAttached = param[0].equalsIgnoreCase("2") ? true : false;
            boolean tanpaTanggal = param[0].equalsIgnoreCase("3") ? true : false;
            boolean tanpaRate = param[0].equalsIgnoreCase("7") ? true : false;
            //boolean klausulaTerlampir = param[0].equalsIgnoreCase("8") ? true : false;

            if (attached.length() > 1) {
                if (param[1] != null) {
                    isAttached = !isAttached ? param[1].equalsIgnoreCase("2") ? true : false : isAttached;
                    tanpaTanggal = !tanpaTanggal ? param[1].equalsIgnoreCase("3") ? true : false : tanpaTanggal;
                    tanpaRate = !tanpaRate ? param[1].equalsIgnoreCase("7") ? true : false : tanpaRate;
                }
            }

            final String preview = (String) request.getAttribute("preview");
            boolean isPreview = false;
            if (preview != null) {
                if (preview.equalsIgnoreCase("Preview")) {
                    isPreview = true;
                }
            }

            boolean effective = pol.isEffective();

            final String digitalsign = (String) request.getAttribute("digitalsign");
            boolean usingDigitalSign = false;
            if (digitalsign != null) {
                if (digitalsign.equalsIgnoreCase("Y")) {
                    usingDigitalSign = true;
                }
            }

            boolean isUsingBarcode = Parameter.readString("REPORT_SIGN_TYPE").equalsIgnoreCase("BARCODE");
            String originalWatermarkPath = Parameter.readString("DIGITAL_POLIS_ORI_PIC");
            String duplicateWatermarkPath = Parameter.readString("DIGITAL_POLIS_DUPLICATE_PIC");
            String copyWatermarkPath = Parameter.readString("DIGITAL_POLIS_COPY_PIC");
            String askridaLogoPath = Parameter.readString("DIGITAL_POLIS_LOGO_PIC");
            String askridaLogoBlackWhitePath = Parameter.readString("DIGITAL_POLIS_LOGOBW_PIC");
            String alamatLogoPath = Parameter.readString("DIGITAL_POLIS_ALAMAT_PIC");
            String alamatLogoBlackWhitePath = Parameter.readString("DIGITAL_POLIS_ALAMATBW_PIC");

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fox="http://xmlgraphics.apache.org/fop/extensions">

    <%
                String watermarkPath = null;
                String logoAskridaPath = null;
                String alamatAskridaPath = null;

                for (int x = 0; x < 3; x++) {

                    if (x == 0) {
                        watermarkPath = originalWatermarkPath;
                        logoAskridaPath = askridaLogoPath;
                        alamatAskridaPath = alamatLogoPath;
                    } else if (x == 1) {
                        watermarkPath = copyWatermarkPath;
                        logoAskridaPath = askridaLogoBlackWhitePath;
                        alamatAskridaPath = alamatLogoBlackWhitePath;
                    } else if (x == 2) {
                        watermarkPath = duplicateWatermarkPath;
                        logoAskridaPath = askridaLogoBlackWhitePath;
                        alamatAskridaPath = alamatLogoBlackWhitePath;
                    }

    %>

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-height="30cm"
                               page-width="21cm"
                               margin-left="2.5cm"
                               margin-right="2.5cm">

            <!-- WATERMARK BACKGROUND -->
            <fo:region-body margin-top="0cm" margin-bottom="0cm" background-image="file:///<%=watermarkPath%>"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1" force-page-count="no-force">

        <fo:flow flow-name="xsl-region-body">

            <fo:block text-align = "center">
                <fo:external-graphic
                    content-height="scale-to-fit"
                    height="1.00in" content-width="1.00in"
                    scaling="non-uniform" src="url(file:///<%=logoAskridaPath%>)"  />
            </fo:block>

            <% if (isPreview) {%>
            <fo:block font-size="20pt"
                      line-height="16pt" space-after.optimum="0pt"
                      color="blue"
                      text-align="center"
                      padding-top="10pt">
                PREVIEW MODE
            </fo:block>
            <% }%>

            <% if (!effective) {%>
            <fo:block font-size="16pt"
                      line-height="16pt" space-after.optimum="0pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>

            <!-- GARIS  -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-before.optimum="10pt" space-after.optimum="1pt"></fo:block>

            <!-- defines text title level 1-->

            <fo:block font-size="16pt" line-height="0pt" space-before.optimum="10pt" space-after.optimum="5pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                {L-INA IKHTISAR PERTANGGUNGAN -L}{L-ENG POLICY SCHEDULE -L}
            </fo:block>

            <fo:block font-size="16pt" line-height="0pt" space-before.optimum="5pt" space-after.optimum="10pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                {L-INA ASURANSI -L}<%=JSPUtil.print(pol.getStPolicyTypeDesc2().toUpperCase())%>{L-ENG INSURANCE -L}
            </fo:block>

            <!-- Normal text -->

            <!-- GARIS  -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-before.optimum="1pt" space-after.optimum="10pt" text-align="justify"></fo:block>

            <!-- Normal text -->

            <%

                        String nama = "";
                        String alamat = "";
                        String noPK = "";
                        String noSKP = "";
                        String noSPPA = "";
                        Date tanggalSKP = null;
                        String penggunaan = "";
                        String jenkred = "";

                        DTOList objects = pol.getObjects();
                        for (int j = 0; j < objects.size(); j++) {
                            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(j);

                            if (objects.size() == 1) {
                                nama = obj.getStReference1();

                                alamat = obj.getStReference6();

                                noPK = obj.getStReference4();

                                noSKP = obj.getStReference22();

                                noSPPA = obj.getStReference24();

                                penggunaan = obj.getStReference23();

                                jenkred = obj.getStReference19();
                            } else {
                                nama = "(Terlampir)";

                                alamat = "(Terlampir)";

                                noPK = "(Terlampir)";

                                noSKP = "(Terlampir)";

                                noSPPA = "(Terlampir)";

                                penggunaan = "(Terlampir)";

                                jenkred = "(Terlampir)";
                            }

                            tanggalSKP = obj.getDtReference7();


                        }

            %>

            <fo:block font-size="<%=fontsize%>" text-align="center">
                {L-ENG Policy Number :  -L}{L-INA Nomor Polis :   -L}<%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" text-align = "justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block text-align="justified">PT. ASURANSI BANGUN ASKRIDA, berkedudukan di ASKRIDA TOWER, Jl. Pramuka Raya Kav. 151, Jakarta Timur yang selanjutnya disebut sebagai PENANGGUNG, dengan ini menanggung :</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block space-before.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Kepada Tertanggung</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block><%=JSPUtil.xmlEscape(pol.getStEntityName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Alamat</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block><%=JSPUtil.xmlEscape(pol.getEntity().getStAddress())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block text-align="justified">1. Beradasarkan Surat Permohonan Penutupan Asuransi <%=JSPUtil.print(pol.getStPolicyTypeDesc2())%> No. <%=JSPUtil.xmlEscape(noSPPA)%> untuk dan atas nama pertanggungan kredit DEBITUR :</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block space-before.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Nama</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block><%=JSPUtil.xmlEscape(nama)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Alamat</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block><%=JSPUtil.xmlEscape(alamat)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block text-align="justified">2. Sesuai Surat Konfirmasi Persetujuan dari PENANGGUNG Nomor, <%=JSPUtil.xmlEscape(noSKP)%>
                                    Tanggal, <% if (objects.size() == 1) {%>
                                    <%=DateUtil.getDateStr(tanggalSKP, "d ^^ yyyy")%>
                                    <% } else {%>
                                    (Terlampir)
                                    <%}%>
                                    dengan data sebagai berikut :</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block space-before.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>a.	Nomor Perjanjian Kredit</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block><%=JSPUtil.xmlEscape(noPK)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>b.	Plafond Kredit/Nilai Pertanggungan</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block><%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>c.	Jenis Kredit</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block><%=JSPUtil.xmlEscape(jenkred)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>d.	Jangka Waktu Pertanggungan</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block><%=JSPUtil.printX(pol.getDtPeriodStart())%> - <%=JSPUtil.printX(pol.getDtPeriodEnd())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>e.	Penggunaan Plafon</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block><%=JSPUtil.xmlEscape(penggunaan)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block text-align="justified">3. Perhitungan Biaya Pertanggungan :</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block space-before.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>a.	Premi Pertanggungan</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>b.	Bea Meterai</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDSFee(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>c.	Biaya Administrasi</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDPCost(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt"></fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Total</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(pol.getDbPremiTotal(), BDUtil.add(pol.getDbNDSFee(), pol.getDbNDPCost())), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block text-align="justified">4. Ruang Lingkup Jaminan sebagaimana tersebut dalam Polis Asuransi <%=JSPUtil.print(pol.getStPolicyTypeDesc2())%> terlampir.</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% if (usingDigitalSign) {%>
            <fo:block font-size="6pt"
                      space-after.optimum="3pt"
                      text-align="justify" >
                Dokumen ini dicetak secara sistem dan dinyatakan sah.
            </fo:block>
            <% }%>

            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="3pt"></fo:block>


            <fo:block font-size="6pt" space-after.optimum="10pt">
                <%if (!tanpaTanggal) {%>
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "dd/MM/yyyy HH:mm:ss"))%>
                <% }%>
                <% if (effective) {%>
                PrintStamp:<%=pol.getStPrintCode()%> PrintCode:<%=pol.getStPrintStamp()%> Page:<fo:page-number/> Cetakan : <%=pol.getStPrintCounter()%>
                <% }%>
                <% if (usingDigitalSign) {%>
                <%if (!isUsingBarcode) {%>  [<%=JSPUtil.print(pol.getStSignCode())%>] <%}%>
                <% }%>
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="90mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L}
                                    <%= JSPUtil.printX(pol.getCostCenter(pol.getCostCenter(pol.getStCostCenterCode()).getStSubCostCenterCode()).getStDescription())%><%if (!tanpaTanggal) {%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <%if (!isUsingBarcode) {%><fo:block text-align="center">
                                    <%if (pol.getParaf() != null) {%><fo:external-graphic content-height="scale-to-fit" height="0.3in"  content-width="0.3in" scaling="non-uniform" overflow="hidden" src="url(<%=pol.getUser(pol.getParaf()).getParafFile().getStFilePath()%>)"/><%}%>
                                    {L-ENG Authorized-L}{L-INA Tanda Tangan Yang Berwenang-L}</fo:block><%}%>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="70mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-body>

                        <% if (usingDigitalSign) {%>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="KANTOR_ASKRIDA_<%=pol.getStCostCenterCode()%>_<%=pol.getStPolicyNo()%>" orientation="0">
                                            <barcode:datamatrix>
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>24x24</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>
                                </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(<%=pol.getUser(pol.getUserIDSign()).getFile().getStFilePath()%>)"  />
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%= pol.getUserIDSignName()%></fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center" ><%=JSPUtil.printX(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <%--
<fo:block-container height="2cm" width="18cm" top="26cm" left="0cm" padding="1mm" position="absolute">
    <fo:block text-align="center" line-height="14pt" font-family="sans-serif" font-size="8pt">
        PT. ASURANSI BANGUN ASKRIDA <%=pol.getCostCenter3().getTypeDescription()%> <%=pol.getCostCenter3().getStDescription().toUpperCase()%>
    </fo:block>
</fo:block-container>

            <fo:block-container height="2cm" width="18cm" top="26.3cm" left="0cm" padding="1mm" position="absolute">
                <fo:block text-align="center" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    Telp. <%=pol.getCostCenter3().getStPhoneCode()%>, Fax. <%=pol.getCostCenter3().getStFaxCode()%>
                </fo:block>
            </fo:block-container>
            --%>

        </fo:flow>
    </fo:page-sequence>
    <%
                }
    %>
</fo:root>