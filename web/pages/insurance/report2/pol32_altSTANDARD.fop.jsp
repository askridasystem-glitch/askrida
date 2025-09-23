<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         java.util.Date,
         com.crux.web.controller.SessionManager,
         com.crux.common.parameter.Parameter,
         com.crux.util.fop.FOPUtil"%><?xml version="1.0" encoding="utf-8"?>
<%

            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");
            final String attached = (String) request.getAttribute("attached");
            boolean tanpaTanggal = attached.equalsIgnoreCase("3") ? false : true;

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
    <%-- DESIGN POLIS--%>
    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->

        <fo:simple-page-master master-name="only"
                               page-height="30cm"
                               page-width="21cm"
                               margin-left="2cm"
                               margin-right="2cm">

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

            <!-- LOGO ASKRIDA -->
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
            <fo:block font-size="16pt" font-family="TAHOMA"
                      line-height="16pt" space-after.optimum="0pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>

            <!-- GARIS  -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>

            <!-- defines text title level 1-->

            <fo:block font-size="12pt" font-family="TAHOMA" line-height="0pt" space-after.optimum="10pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                POLIS ASURANSI KREASI
            </fo:block>

            <!-- Normal text -->

            <!-- defines text title level 1-->

            <fo:block font-size="12pt" font-family="TAHOMA" line-height="0pt" space-after.optimum="10pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                PROGRAM <%=JSPUtil.printX(pol.getStPolicyTypeDesc2().toUpperCase())%>
            </fo:block>

            <!-- Normal text -->

            <!-- GARIS  -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>


            <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>


            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="70mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="end" font-weight="bold">{L-ENG No. :  -L}{L-INA No. :   -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-weight="bold"><%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end" font-weight="bold">{L-ENG Reg. No.   -L}{L-INA Reg. No.   -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-weight="bold"><%= pol.getStPolicyNo().substring(12, 16)%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify">
                Perseroan Terbatas ASURANSI BANGUN ASKRIDA, selanjutnya disebut ASKRIDA, menanggung atas dasar syarat-syarat seperti dalam polis ini :
            </fo:block>

            <fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG NAME -L}{L-INA NAMA -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-weight="bold"><%= JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG ADDRESS -L}{L-INA ALAMAT -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-weight="bold"><%= JSPUtil.printX(pol.getStCustomerAddress())%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify">
                seterusnya dinyatakan sebagai Penanda Tangan, untuk pembayaran jumlah-jumlah uang pertanggungan seperti tersebut dibawah ini dalam hal : 
            </fo:block>

            <fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG Died -L}{L-INA Meninggal Dunia -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >Sesuai PKS Terlampir</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG if -L}{L-INA jika -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >Sesuai PKS Terlampir</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG job -L}{L-INA pekerjaan -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >Sesuai PKS Terlampir</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG born -L}{L-INA tempat tanggal lahir -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >Sesuai PKS Terlampir</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG address -L}{L-INA alamat -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >Sesuai PKS Terlampir</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify">
                selanjutnya dinyatakan sebagai Tertanggung, mengalami resiko kematian yang disebabkan oleh kecelakaan atau sebab-sebab lain, kecuali kematian karena :
            </fo:block>

            <fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block >-</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >bunuh diri atau dihukum mati oleh Pengadilan / Pejabat yang berwenang.</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block >-</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >karena terlibat dalam perkelahian, kecuali sebagai seseorang yang mempertahankan diri.</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block >-</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >karena akibat kecelakaan pesawat penumpang pesawat udara yang tidak diselenggarakan oleh perusahaan dengan jadwal tetap dan teratur.</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block >-</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >karena akibat perang, perang saudara, perbuatan kekerasan dalam pemberontakan, huru-hara, pengacauan dan perbuatan teror lainnya.</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block >-</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >sebagai akibat perbuatan kejahatan yang dilakukan dengan sengaja ataupun kekhilafan besar oleh mereka yang berkepentingan dalam polis dan ini / atau ahli waris lainnya.</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify">
                Pembayaran ganti kerugian akan dilakukan kepada : 
            </fo:block>

            <fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="center">
                <fo:inline font-weight="bold"><%= JSPUtil.printX(pol.getStProducerName())%></fo:inline></fo:block>

            <fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="center">
                QQ. NASABAH DAN / ATAU AHLI WARIS SAH YANG DITUNJUK 
            </fo:block>

            <fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>">
                Jangka waktu pertanggungan : <fo:inline font-weight="bold"><%=DateUtil.getDateStr(pol.getDtPeriodStart(), "d ^^ yyyy")%></fo:inline> sampai dengan <fo:inline font-weight="bold"><%=DateUtil.getDateStr(pol.getDtPeriodEnd(), "d ^^ yyyy")%></fo:inline></fo:block>

            <fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify">
                Setelah berakhirnya jangka waktu tersebut, maka pertanggungan ini akan diperpanjang untuk jangka waktu yang sama, kecuali jika salah satu pihak sedikit-sedikitnya tiga bulan (3 bulan) sebelum berakhirnya tiap-tiap tanggal jangka waktu,
                membatalkan pertanggungan ini dengan pemberitahuan tertulis kepada pihak yang bersangkutan.
            </fo:block>

            <fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify">
                Pertanggungan ini harus sudah berakhir pada hari terakhir dari jangka waktu yang bersangkutan jika Tertanggung telah mencapai usia 65 tahun.
            </fo:block>

            <fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="7mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG Premium -L}{L-INA Premi -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>Rp.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(pol.getDbPremiTotal(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG Policy Cost -L}{L-INA Ongkos Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>Rp.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(pol.getDbNDPCost(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG Materai -L}{L-INA Materai -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>Rp.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(pol.getDbNDSFee(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell>
                                <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG Total -L}{L-INA Jumlah -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>Rp.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end" font-weight="bold"><%= JSPUtil.printX(pol.getDbTotalDue(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
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
                                <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L} 
                                    <%= JSPUtil.printX(pol.getStCostCenterDesc())%><%if (!tanpaTanggal) {%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%></fo:block>
                                    <% } else {%>
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L} 
                                    <%= JSPUtil.printX(pol.getCostCenter(pol.getCostCenter(pol.getStCostCenterCode()).getStSubCostCenterCode()).getStDescription())%><%if (!tanpaTanggal) {%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%></fo:block>
                                    <% }%>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <%if (!isUsingBarcode) {%><fo:block text-align="center">{L-ENG Authorized-L}{L-INA Tanda Tangan Yang Berwenang-L}</fo:block><%}%>
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
                                            message="KANTOR_ASKRIDA_<%=pol.getCostCenter(pol.getStCostCenterCode()).getTypeDescription().toUpperCase()%>_<%=pol.getCostCenter(pol.getStCostCenterCode()).getStDescription().toUpperCase()%>_<%=pol.getStPolicyNo()%>" orientation="0">
                                            <barcode:datamatrix> 
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>24x24</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                                <%--<human-readable>none</human-readable>--%>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>

                                </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <%--<% if (pol.getUserApproved().getFile().getStFilePath()!=null) { %>--%>
                            <fo:table-cell>
                                <%if (!isUsingBarcode) {%>
                                <%if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(<%=pol.getUser(pol.getUserIDSign()).getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <% } else {%>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(<%=pol.getUserApproved().getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <% }%>
                                <% }%>

                                <%if (isUsingBarcode) {%>
                                <fo:block text-align = "center" space-before.optimum="5pt">
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="<%=pol.getEncryptedApprovedWho()%>" orientation="0">
                                            <barcode:datamatrix>
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>24x24</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                                <%--<human-readable>none</human-readable>--%>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>

                                </fo:block>
                                <fo:block font-size="6pt"
                                          font-family="sans-serif"
                                          line-height="10pt"
                                          space-after.optimum="10pt"
                                          text-align="center">
                                    <%=pol.getStSignCode()%>
                                </fo:block>
                                <%}%>

                            </fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <%--
                            <% } else { %>
                            <fo:table-cell>
                                <fo:block text-align = "center" space-after.optimum="65pt"></fo:block>
                            </fo:table-cell>
                            <% } %>
                            --%>
                        </fo:table-row>                         

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <%--<fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=pol.getUserApproved().getStUserName().toUpperCase()%></fo:block></fo:table-cell>--%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%= pol.getUserIDSignName()%></fo:inline></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%=JSPUtil.printX(pol.getUserApproved().getStUserName()).toUpperCase()%></fo:inline></fo:block></fo:table-cell>
                            <% }%>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center" ><%if (pol.getParaf() != null) {%><fo:leader leader-length="10mm"/><%}%><%=JSPUtil.printX(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%>                                     <%if (pol.getParaf() != null) {%>                                     <fo:external-graphic                                         content-height="scale-to-fit" height="0.3in"  content-width="0.3in" scaling="non-uniform"                                         overflow="hidden" src="url(<%=pol.getUser(pol.getParaf()).getParafFile().getStFilePath()%>)"  />                                     <%}%>                             </fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
                            <% }%>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%> 

                    </fo:table-body>
                </fo:table> 
            </fo:block>
<%--
            <!-- ALAMAT ASKRIDA -->
            <fo:block-container height="3.2cm" width="17cm" top="25.7cm" left="0cm" position="absolute">
                <fo:block text-align = "center">
                    <fo:external-graphic
                        scaling="non-uniform" src="url(file:///<%=alamatAskridaPath%>)"  />
                </fo:block>
            </fo:block-container>
--%>
        </fo:flow>
    </fo:page-sequence>
    <%-- END DESIGN HALAMAN 1 (ORIGINAL)--%>

    <%
                }
    %>

</fo:root>