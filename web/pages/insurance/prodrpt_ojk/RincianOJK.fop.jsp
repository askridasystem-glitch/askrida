<%@ page import="com.webfin.insurance.model.*,
         com.crux.util.*,
         com.crux.util.fop.FOPUtil,
         java.math.BigDecimal,
         com.crux.web.form.FormManager,
         com.crux.web.controller.SessionManager,
         com.webfin.insurance.form.ProductionFinanceOJKReportForm,
         java.text.SimpleDateFormat,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>


<%
            final DTOList l = (DTOList) request.getAttribute("RPT");

            final ProductionFinanceOJKReportForm form = (ProductionFinanceOJKReportForm) SessionManager.getInstance().getCurrentForm();

            String report = null;
            InsurancePolicyView policy = (InsurancePolicyView) l.get(0);
            report = policy.getStReference1();
%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-height="31cm"
                               page-width="21cm"
                               margin-top="0.5cm"
                               margin-bottom="0.5cm"                                                                                                                                                                           
                               margin-left="1cm"
                               margin-right="0.5cm">
            <fo:region-body margin-top="2cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">

        <!-- usage of page layout -->
        <fo:static-content flow-name="xsl-region-before"> 
            <fo:block-container height="1cm" width="15cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="message"
                                        retrieve-boundary="page"
                                        retrieve-position="first-starting-within-page"/>
                </fo:block>
            </fo:block-container>
            <fo:block-container height="1cm" width="15cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="term"
                                        retrieve-boundary="page"
                                        retrieve-position="last-ending-within-page"/>
                </fo:block>
            </fo:block-container>


            <fo:block text-align="end"
                      font-size="8pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>



        <fo:flow flow-name="xsl-region-body">

            <!-- defines text title level 1-->
            <fo:block font-size="7pt" line-height="8pt"></fo:block>


            <!-- Normal text -->

            <!-- bikin kolom headernye -->
            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-header>

                        <!-- judul laporan -->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="14pt" text-align="center">             
                                    PT. ASURANSI BANGUN ASKRIDA
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="14pt" text-align="center">
                                    PERUSAHAAN ASURANSI UMUM KONVENSIONAL
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="14pt" text-align="center">
                                    <%=JSPUtil.printX(form.getStProdOJKDesc())%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="14pt" text-align="center">
                                    Triwulan <%=JSPUtil.printX(form.getStTriwulan())%> Tahun <%=JSPUtil.printX(new SimpleDateFormat("yyyy").format(new Date()))%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7"><fo:block text-align="end" space-before.optimum="10pt">(dalam jutaan rupiah)</fo:block></fo:table-cell>
                        </fo:table-row>
                        <!-- garis horizontal -->


                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <!-- garisnye sampe sini -->


                        <!-- bikin row pertama -->
                        <fo:table-row>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">NO</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">Nama Bank</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">Kategori</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">Saldo Buku Besar</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">AYD</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">Saldo Buku Besar Lancar (Kurang dari satu tahun)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center" line-height="5mm">Keterangan</fo:block></fo:table-cell>
                        </fo:table-row>

                        <!-- ini bikin row yg ke dua -->

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" ></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <!-- bikin lagi row yang ke tiga -->

                        <fo:table-row>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">(1)</fo:block></fo:table-cell> 
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">(2)</fo:block></fo:table-cell> 
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">(4)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">(5)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">(6)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center">(7)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-right-style="solid" border-left-style="solid"><fo:block text-align="center">(8)</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" ></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <!-- sampe sini -->

                    </fo:table-header>
                    <fo:table-body>
                        <%
                                    BigDecimal[] t = new BigDecimal[1];
                                    BigDecimal amount = new BigDecimal(0);

                                    for (int i = 0; i < l.size(); i++) {
                                        InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                        amount = BDUtil.div(pol.getDbPremiTotal(),new BigDecimal(1000000));

                                        int n = 0;
                                        t[n] = BDUtil.add(t[n++], amount);
                        %>

                        <fo:table-row>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block space-before.optimum="5pt" text-align="center"><%=JSPUtil.printX(String.valueOf(i + 1))%></fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block space-before.optimum="5pt" text-align="left"><%=JSPUtil.printX(pol.getStDescription())%></fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block space-before.optimum="5pt" text-align="left"></fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block space-before.optimum="5pt" text-align="left"></fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block space-before.optimum="5pt" text-align="left"></fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"><fo:block space-before.optimum="5pt" text-align="right"><%=JSPUtil.printX(amount, 2)%> <fo:inline color="white">.</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-right-style="solid" border-left-style="solid"><fo:block space-before.optimum="5pt" text-align="center"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid" border-bottom-style="solid"><fo:block space-before.optimum="5pt" text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block space-before.optimum="5pt" text-align="left">Total</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block space-before.optimum="5pt" text-align="right"></fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block space-before.optimum="5pt" text-align="right"></fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block space-before.optimum="5pt" text-align="right"></fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block space-before.optimum="5pt" text-align="right"><%=JSPUtil.printX(t[0], 2)%> <fo:inline color="white">.</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-right-style="solid" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block space-before.optimum="5pt" text-align="right"></fo:block></fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>

                </fo:table>
            </fo:block>    

            <fo:block font-size="6pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>
            </fo:block> 

            <fo:block 
                font-size="6pt"
                font-family="TAHOMA"
                line-height="12pt"
                font-style="bold">
                <%=JSPUtil.printX(report)%> - PT. Asuransi Bangun Askrida
            </fo:block>


            <fo:block id="end-of-document"><fo:marker
                    marker-class-name="term">
                    <fo:instream-foreign-object>
                        <!--                        <svg xmlns="http://www.w3.org/2000/svg" width="15cm" height="1cm" xml:space="preserve">
                                                    <rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
                                                </svg>-->
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block> 


        </fo:flow>
    </fo:page-sequence>
</fo:root>