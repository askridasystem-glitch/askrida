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
                               margin-left="1.5cm"
                               margin-right="1.5cm">

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

            <fo:block font-size="16pt" line-height="0pt" space-after.optimum="10pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                <%=pol.getStPolicyTypeDesc2()%>
            </fo:block>

            <!-- Normal text -->

            <!-- GARIS  -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-before.optimum="1pt" space-after.optimum="10pt" text-align="justify"></fo:block>

            <!-- Normal text -->

            <%
                                DTOList objects = pol.getObjects();
                                String dtTanggal = null;

                                for (int i = 0; i < objects.size(); i++) {
                                    InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

                                    dtTanggal = "{L-ENG dated -L}{L-INA pada tanggal -L}" + DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy");

            %>

            <fo:block font-size="<%=fontsize%>" text-align = "justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG Bond No. : -L}{L-INA No. Bond : -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align = "end">{L-ENG Amount : -L}{L-INA Nilai : -L}<%=JSPUtil.print(pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.print(pol.getStCurrencyCode())%><%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%> </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" text-align = "justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block text-align="start">Yang bertanda tangan dibawah ini, kami :</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Nama</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference8())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Jabatan</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference9())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block text-align="start">Dalam hal ini bertindak dan atas nama :</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Nama Perusahaan</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.xmlEscape(obj.getStReference12())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Berkedudukan di</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getCostCenter3().getStDescription().toUpperCase())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Alamat</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.xmlEscape(obj.getEntity(obj.getStReference6()).getStAddress())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                            String suretyPhone = null;
                                                            String suretyFax = null;
                                                            String suretyEmail = null;

                                                            DTOList addressSurety = obj.getEntity(obj.getStReference6()).getAddresses();
                                                            for (int j = 0; j < addressSurety.size(); j++) {
                                                                EntityAddressView addSurety = (EntityAddressView) addressSurety.get(j);

                                                                suretyPhone = addSurety.getStPhone();
                                                                suretyFax = addSurety.getStPhoneFax();
                                                                suretyEmail = addSurety.getStEmail();

                                                            }
                        %>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Telepon</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>
                                    <%if (suretyPhone != null) {%>
                                    <%=JSPUtil.printX(suretyPhone)%>
                                    <% }%>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Faksimili dan Email</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>
                                    <%if (suretyFax != null) {%>
                                    <%=JSPUtil.printX(suretyFax)%>
                                    <%}%>
                                    <%if (suretyEmail != null) {%>
                                    <%=JSPUtil.printX(suretyEmail)%>
                                    <%}%>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block text-align="start">yang selanjutnya disebut <fo:inline font-weight="bold" font-style="italic">Surety</fo:inline>,</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block text-align="start">berjanji dan menjamin :</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Nama</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.xmlEscape(obj.getStReference1())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Alamat</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>
                                    <%if (obj.getEntity(obj.getStReference2()).getStAddress() != null) {%>
                                    <%=JSPUtil.xmlEscape(obj.getEntity(obj.getStReference2()).getStAddress())%>
                                    <% }%>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                            String principalPhone = null;
                                                            String principalFax = null;
                                                            String principalEmail = null;

                                                            DTOList addressPrincipal = obj.getEntity(obj.getStReference2()).getAddresses();

                                                            for (int j = 0; j < addressPrincipal.size(); j++) {
                                                                EntityAddressView addPrincipal = (EntityAddressView) addressPrincipal.get(j);

                                                                principalPhone = addPrincipal.getStPhone();
                                                                principalFax = addPrincipal.getStPhoneFax();
                                                                principalEmail = addPrincipal.getStEmail();
                                                            }
                        %>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>NPWP</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>
                                    <%if (obj.getEntity(obj.getStReference2()).getStTaxFile() != null) {%>
                                    <%=JSPUtil.xmlEscape(obj.getEntity(obj.getStReference2()).getStTaxFile())%>
                                    <% }%>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>NIPER</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>
                                    <%if (obj.getEntity(obj.getStReference2()).getStIdentificationNumber() != null) {%>
                                    <%=JSPUtil.xmlEscape(obj.getEntity(obj.getStReference2()).getStIdentificationNumber())%>
                                    <% }%>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Telepon</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>
                                    <%if (principalPhone != null) {%>
                                    <%=JSPUtil.printX(principalPhone)%>
                                    <% }%>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Faksimili dan Email</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>
                                    <%if (principalFax != null) {%>
                                    <%=JSPUtil.printX(principalFax)%>
                                    <% }%>
                                    <%if (principalEmail != null) {%>
                                    <%=JSPUtil.printX(principalEmail)%>
                                    <% }%>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block text-align="start">yang selanjutnya disebut <fo:inline font-weight="bold" font-style="italic">Principal</fo:inline>,</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block text-align="justified">dengan melepaskan hak istimewa untuk menuntut supaya barang-barang <fo:inline font-style="italic">Principal</fo:inline>
                                    lebih dahulu disita dan dijual untuk melunasi hutang-hutangnya yang oleh undang-undang diberikan kepada <fo:inline font-style="italic">Surety</fo:inline> sesuai dengan Pasal 1832 Kitab
                                    Undang-Undang Hukum Perdata, termasuk juga haknya untuk terlebih dahulu mendapat pembayaran piutang, akan membayar segera dan sekaligus kepada (yang selanjutnya disebut <fo:inline font-style="italic">Obligee</fo:inline>)
                                    uang paling banyak sebesar <%=JSPUtil.print(pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%>, apabila <fo:inline font-style="italic">Principal</fo:inline>
                                    tidak dapat memenuhi kewajiban pabean atas :</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>kegiatan kepabeanan</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference13())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>dokumen sumber</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference14())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4">
                                <fo:block text-align="justified">Klaim atas <fo:inline font-style="italic">Customs Bonds</fo:inline> ini harus telah selesai diajukan oleh <fo:inline font-style="italic">Obligee</fo:inline>
                                    dan diterima oleh <fo:inline font-style="italic">Surety</fo:inline> dalam jangka waktu paling lama 30 (tiga puluh) hari sejak tanggal jatuh tempo <fo:inline font-style="italic">Customs Bonds</fo:inline> ini dengan menggunakan Surat Pencairan Jaminan.</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4">
                                <fo:block text-align="justified">Pembayaran atas klaim <fo:inline font-style="italic">Customs Bonds</fo:inline> ini dilakukan paling lama 6 (enam) hari kerja sejak tanggal diterimanya Surat Pencairan Jaminan dengan ketentuan :</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3">
                                <fo:block>a. disetorkan ke Kas Negara sejumlah yang tertera Surat Pencairan Jaminan; dan </fo:block>
                                <fo:block>b. apabila terdapat sisa dari penyetoran tersebut pada huruf a, dikembalikan kepada <fo:inline font-style="italic">Principal</fo:inline>.</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4">
                                <fo:block text-align="justified">Apabila sampai dengan tanggal jatuh tempo klaim <fo:inline font-style="italic">Customs Bonds</fo:inline>, <fo:inline font-style="italic">Surety</fo:inline>
                                    tidak menerima Surat Pencairan Jaminan dari <fo:inline font-style="italic">Obligee</fo:inline>, <fo:inline font-style="italic">Surety</fo:inline> tidak bertanggung jawab lagi atas pembayaran dimaksud
                                    (batal demi hukum tanpa menghilangkan tagihan negara kepada <fo:inline font-style="italic">Principal</fo:inline>).</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4">
                                <fo:block text-align="justified">Penyesuaian Jaminan hanya dapat dilakukan setelah mendapat persetujuan Kepala Kantor Pabean.</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4">
                                <fo:block text-align="justified"><fo:inline font-style="italic">Customs Bonds</fo:inline> ini berlaku terhitung mulai tanggal <%=DateUtil.getDateStr(pol.getDtPeriodStart(), "d ^^ yyyy")%> sampai dengan tanggal <%=DateUtil.getDateStr(pol.getDtPeriodEnd(), "d ^^ yyyy")%> (jatuh tempo <fo:inline font-style="italic">Customs Bonds</fo:inline>).</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% }%>

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

        </fo:flow>
    </fo:page-sequence>
    <%
                }
    %>
</fo:root>