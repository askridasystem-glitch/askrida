<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         com.crux.util.Tools,
         com.crux.util.fop.FOPUtil,
         com.crux.util.BDUtil,
         java.math.BigDecimal,
         com.crux.common.parameter.Parameter,
         com.crux.web.form.FormManager,
         com.crux.web.controller.SessionManager,
         com.webfin.insurance.form.ProductionMarketingReportForm,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%

            final DTOList l = (DTOList) request.getAttribute("RPT");

            final ProductionMarketingReportForm form = (ProductionMarketingReportForm) SessionManager.getInstance().getCurrentForm();

            InsurancePolicyView policy = (InsurancePolicyView) l.get(0);

            String rekening = policy.getEntity2(policy.getStEntityID()).getStRcNo();

            boolean isBPR = policy.getEntity2(policy.getStEntityID()).getStRef1().equalsIgnoreCase("93") || policy.getEntity2(policy.getStProducerID()).getStRef1().equalsIgnoreCase("93");

%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <fo:simple-page-master master-name="first"
                               page-height="29.7cm"
                               page-width="21cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">

            <fo:region-body margin-top="1cm" margin-bottom="2cm"/>
            <fo:region-before extent="1cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="first">

        <!-- usage of page layout -->
        <!-- header -->
        <fo:static-content flow-name="xsl-region-before">

            <fo:block-container height="0cm" width="0cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="message"
                                        retrieve-boundary="page"
                                        retrieve-position="first-starting-within-page"/>
                </fo:block>
            </fo:block-container>
            <fo:block-container height="0cm" width="0cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="term"
                                        retrieve-boundary="page"
                                        retrieve-position="last-ending-within-page"/>
                </fo:block>
            </fo:block-container>
        </fo:static-content>

        <fo:static-content flow-name="xsl-region-after">

            <fo:block text-align="end"
                      font-size="8pt" font-family="serif" line-height="1em + 0.15pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>


        <fo:flow flow-name="xsl-region-body">

            <%if (!isBPR) {%>
            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/><!-- No -->
                    <fo:table-column column-width="40mm"/><!-- Policy No -->
                    <fo:table-column column-width="40mm"/><!-- The Insured -->
                    <fo:table-column column-width="40mm"/><!-- The Insured -->
                    <fo:table-column column-width="30mm"/><!-- Premium -->
                    <fo:table-column column-width="10mm"/><!-- Premium -->
                    <fo:table-column column-width="30mm"/><!-- Feebase -->
                    <fo:table-header>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"></fo:table-cell>
                            <fo:table-cell >
                                <fo:block >
                                    <fo:external-graphic <%--content-width="scale-to-fit"--%>
                                        content-height="100%"
                                        width="100%"
                                        scaling="uniform" src="url(D:\jboss-fin\server\default\deploy\fin.ear\fin.war\pages\main\img\burung aja.jpg)"/>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4">
                                <fo:block text-align="start">
                                    Surat Pengantar No. ...................
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="3">
                                <fo:block text-align="end">
                                    <% if (form.getStBranch() != null) {%>
                                    <%=JSPUtil.printX(form.getStBranchDesc())%>, <%=DateUtil.getDateStr(new Date(), "dd ^^ yyyy")%>
                                    <%}%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2">
                                <fo:block text-align="start">
                                    Kepada Yth  :
                                </fo:block>
                                <fo:block text-align="start">
                                    <% if (form.getStEntityID() != null) {%>
                                    <%=JSPUtil.printX(form.getStEntityName())%>
                                    <% }%>
                                    <% if (form.getStMarketerID() != null) {%>
                                    <%=JSPUtil.printX(form.getStMarketerName())%>
                                    <% }%>
                                </fo:block>
                                <fo:block text-align="start">
                                    <% if (form.getStEntityID() != null) {%>
                                    <%=JSPUtil.printX(form.getEntity().getStAddress())%>
                                    <% }%>
                                    <% if (form.getStMarketerID() != null) {%>
                                    <%=JSPUtil.printX(form.getMarketer().getStAddress())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="5"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block space-before.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block text-align="justify">Dengan Hormat,</fo:block>
                                <fo:block text-align="justify">Bersama ini kami sampaikan kepada Saudara Polis-polis / Lampiran Nota Kwitansi yang tersebut dibawah ini : </fo:block>
                                <%if (form.getPolicyDateFrom() != null) {%>
                                <fo:block text-align="justify">Periode Polis <%=JSPUtil.printX(form.getPolicyDateFrom())%> s/d <%=JSPUtil.printX(form.getPolicyDateTo())%> </fo:block>
                                <%}%>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="0.15pt" border-left-style="solid"><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>
                            <fo:table-cell padding="0.15pt" border-left-style="solid"><fo:block text-align="center">{L-ENG Policy Number -L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" padding="0.15pt" border-left-style="solid"><fo:block text-align="center">{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell>
                            <fo:table-cell padding="0.15pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center">{L-ENG Bruto Premium-L}{L-INA Premi Bruto-L}</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" padding="0.15pt" border-left-style="solid"><fo:block text-align="center">{L-ENG Fee Base-L}{L-INA Fee Base -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-header>

                    <fo:table-body>

                        <%

                            BigDecimal bruto = new BigDecimal(0);
                            BigDecimal[] t = new BigDecimal[3];

                            for (int i = 0; i < l.size(); i++) {
                                InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                int n = 0;
                                t[n] = BDUtil.add(t[n++], pol.getDbNDFeeBase1());
                                t[n] = BDUtil.add(t[n++], pol.getDbPremiTotal());

                                bruto = BDUtil.add(pol.getDbPremiTotal(), pol.getDbNDPCost());
                                bruto = BDUtil.add(bruto, pol.getDbNDSFee());
                                t[n] = BDUtil.add(t[n++], bruto);

                                String custName = "";
                                if (pol.getStCustomerName().length() > 40) {
                                    custName = pol.getStCustomerName().substring(0, 40);
                                } else {
                                    custName = pol.getStCustomerName().substring(0, pol.getStCustomerName().length());
                                }

                                String no_polis = pol.getStPolicyNo();
                                String no_polis_cetak = no_polis.substring(0, 4) + "-" + no_polis.substring(4, 8) + "-" + no_polis.substring(8, 12) + "-" + no_polis.substring(12, 16) + "-" + no_polis.substring(16, 18);
                        %>

                        <fo:table-row>
                            <fo:table-cell padding="0.15pt" border-left-style="solid"><fo:block text-align="center"><%=String.valueOf(i + 1)%></fo:block></fo:table-cell>    <!-- No --><!-- No -->
                            <fo:table-cell padding="0.15pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(no_polis_cetak)%></fo:block></fo:table-cell>    <!-- No --><!-- Policy No -->
                            <fo:table-cell number-columns-spanned="2" padding="0.15pt" border-left-style="solid"><fo:block><%=JSPUtil.printX(custName)%></fo:block></fo:table-cell>    <!-- No --><!-- The Insured -->
                            <fo:table-cell padding="0.15pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(bruto, 0)%></fo:block></fo:table-cell>    <!-- No --><!-- Premium -->
                            <fo:table-cell padding="0.15pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(pol.getDbReference1(), 0)%>%</fo:block></fo:table-cell>    <!-- No --><!-- Total Comm -->
                            <fo:table-cell padding="0.15pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDFeeBase1(), 0)%></fo:block></fo:table-cell>    <!-- No --><!-- Total Comm -->
                        </fo:table-row>

                        <%
                            }
                        %>

                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" padding="0.15pt" border-left-style="solid"><fo:block> TOTAL : </fo:block></fo:table-cell>
                            <fo:table-cell padding="0.15pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(t[2], 0)%></fo:block></fo:table-cell><!-- Total Premi -->
                            <fo:table-cell padding="0.15pt" border-left-style="solid"></fo:table-cell><!-- Total Premi -->
                            <fo:table-cell padding="0.15pt" border-right-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(t[0], 0)%></fo:block></fo:table-cell><!-- Total Premi -->
                        </fo:table-row>

                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block space-before.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >*</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3">
                                <fo:block text-align="justify">Bersama ini pula kami sampaikan persetujuan kepada Bapak/Ibu untuk mendebet ke rekening kami sebesar 10% untuk keuntungan/pendapatan perusahaan Bapak/Ibu atas pembayaran tersebut. </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >*</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3">
                                <fo:block text-align="justify">Setiap pembayaran premi asuransi harap mencantumkan nomor polisnya Nota pelunasan dari Bank dikirim ke <fo:inline font-weight="bold">PT. Asuransi Bangun Askrida Cabang <%=JSPUtil.printX(form.getStBranchDesc())%></fo:inline></fo:block>
                                <fo:block text-align="justify"><%=JSPUtil.printX(form.getCostCenter().getStAddress())%></fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >*</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3">
                                <fo:block text-align="justify">Jumlah tagihan sebesar <fo:inline font-weight="bold">Rp. <%=JSPUtil.printX(t[2], 0)%></fo:inline> dapat Saudara pindah bukukan ke dalam Rek. Giro kami pada No. Rekening <fo:inline font-weight="bold"><%=JSPUtil.printX(rekening)%></fo:inline> untuk mana kami ucapkan terima kasih.</fo:block>
                                <fo:block text-align="justify">Penyerahan Polis / Lamp. Nota Kwitansi tersebut belum berarti pembayaran telah diterima.</fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block space-before.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block font-size="6pt">Catatan : </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block font-size="6pt" text-align="justify">Sebagai tanda terima, harap tindasannya ditandatangani dan dikembalikan kepada kami.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2">
                                <fo:block text-align="center">Hormat kami,</fo:block>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(<%=form.getUser(Parameter.readString("BRANCH_" + form.getStBranch())).getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <fo:block text-align = "center"><fo:inline text-decoration="underline"><%=form.getUser(Parameter.readString("BRANCH_" + form.getStBranch())).getStUserName().toUpperCase()%></fo:inline></fo:block>
                                <fo:block text-align = "center"><%=form.getUser(Parameter.readString("BRANCH_" + form.getStBranch())).getStJobPosition().toUpperCase()%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="6pt" space-before.optimum="10pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>
            </fo:block>

            <fo:block text-align="start">
                <fo:instream-foreign-object>
                    <barcode:barcode
                        xmlns:barcode="http://barcode4j.krysalis.org/ns"
                        message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(t[2], 0)%>" orientation="0">
                        <barcode:datamatrix>
                            <barcode:height>40pt</barcode:height>
                            <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                        </barcode:datamatrix>
                    </barcode:barcode>
                </fo:instream-foreign-object>
            </fo:block>
            <%}%>

            <%if (isBPR) {%>
            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/><!-- No -->
                    <fo:table-column column-width="40mm"/><!-- Policy No -->
                    <fo:table-column column-width="40mm"/><!-- The Insured -->
                    <fo:table-column column-width="80mm"/><!-- The Insured -->
                    <fo:table-column column-width="30mm"/><!-- Premium -->
                    <fo:table-header>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"></fo:table-cell>
                            <fo:table-cell >
                                <fo:block >
                                    <fo:external-graphic <%--content-width="scale-to-fit"--%>
                                        content-height="100%"
                                        width="100%"
                                        scaling="uniform" src="url(D:\jboss-fin\server\default\deploy\fin.ear\fin.war\pages\main\img\burung aja.jpg)"/>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3">
                                <fo:block text-align="start">
                                    Surat Pengantar No. ...................
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="2">
                                <fo:block text-align="end">
                                    <% if (form.getStBranch() != null) {%>
                                    <%=JSPUtil.printX(form.getStBranchDesc())%>, <%=DateUtil.getDateStr(new Date(), "dd ^^ yyyy")%>
                                    <%}%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2">
                                <fo:block text-align="start">
                                    Kepada Yth  :
                                </fo:block>
                                <fo:block text-align="start">
                                    <% if (form.getStEntityID() != null) {%>
                                    <%=JSPUtil.printX(form.getStEntityName())%>
                                    <% }%>
                                    <% if (form.getStMarketerID() != null) {%>
                                    <%=JSPUtil.printX(form.getStMarketerName())%>
                                    <% }%>
                                </fo:block>
                                <fo:block text-align="start">
                                    <% if (form.getStEntityID() != null) {%>
                                    <%=JSPUtil.printX(form.getEntity().getStAddress())%>
                                    <% }%>
                                    <% if (form.getStMarketerID() != null) {%>
                                    <%=JSPUtil.printX(form.getMarketer().getStAddress())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5">
                                <fo:block space-before.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5">
                                <fo:block text-align="justify">Dengan Hormat,</fo:block>
                                <fo:block text-align="justify">Bersama ini kami sampaikan kepada Saudara Polis-polis / Lampiran Nota Kwitansi yang tersebut dibawah ini : </fo:block>
                                <%if (form.getPolicyDateFrom() != null) {%>
                                <fo:block text-align="justify">Periode Polis <%=JSPUtil.printX(form.getPolicyDateFrom())%> s/d <%=JSPUtil.printX(form.getPolicyDateTo())%> </fo:block>
                                <%}%>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="0.15pt" border-left-style="solid"><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>
                            <fo:table-cell padding="0.15pt" border-left-style="solid"><fo:block text-align="center">{L-ENG Policy Number -L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" padding="0.15pt" border-left-style="solid"><fo:block text-align="center">{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell>
                            <fo:table-cell padding="0.15pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center">{L-ENG Bruto Premium-L}{L-INA Premi Bruto-L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-header>

                    <fo:table-body>

                        <%

                            BigDecimal bruto = new BigDecimal(0);
                            BigDecimal[] t = new BigDecimal[3];

                            for (int i = 0; i < l.size(); i++) {
                                InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                                int n = 0;
                                t[n] = BDUtil.add(t[n++], pol.getDbNDFeeBase1());
                                t[n] = BDUtil.add(t[n++], pol.getDbPremiTotal());

                                bruto = BDUtil.add(pol.getDbPremiTotal(), pol.getDbNDPCost());
                                bruto = BDUtil.add(bruto, pol.getDbNDSFee());
                                t[n] = BDUtil.add(t[n++], bruto);

                                String no_polis = pol.getStPolicyNo();
                                String no_polis_cetak = no_polis.substring(0, 4) + "-" + no_polis.substring(4, 8) + "-" + no_polis.substring(8, 12) + "-" + no_polis.substring(12, 16) + "-" + no_polis.substring(16, 18);
                        %>

                        <fo:table-row>
                            <fo:table-cell padding="0.15pt" border-left-style="solid"><fo:block text-align="center"><%=String.valueOf(i + 1)%></fo:block></fo:table-cell>    <!-- No --><!-- No -->
                            <fo:table-cell padding="0.15pt" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(no_polis_cetak)%></fo:block></fo:table-cell>    <!-- No --><!-- Policy No -->
                            <fo:table-cell number-columns-spanned="2" padding="0.15pt" border-left-style="solid"><fo:block><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>    <!-- No --><!-- The Insured -->
                            <fo:table-cell padding="0.15pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(bruto, 0)%></fo:block></fo:table-cell>    <!-- No --><!-- Premium -->
                        </fo:table-row>

                        <%
                            }
                        %>

                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" padding="0.15pt" border-left-style="solid"><fo:block> TOTAL : </fo:block></fo:table-cell>
                            <fo:table-cell padding="0.15pt" border-left-style="solid"><fo:block text-align="end"><%=JSPUtil.printX(t[2], 0)%></fo:block></fo:table-cell><!-- Total Premi -->
                        </fo:table-row>

                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5">
                                <fo:block space-before.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >*</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3">
                                <fo:block text-align="justify">Bersama ini pula kami sampaikan persetujuan kepada Bapak/Ibu untuk mendebet ke rekening kami sebesar 10% untuk keuntungan/pendapatan perusahaan Bapak/Ibu atas pembayaran tersebut. </fo:block>
                            </fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >*</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3">
                                <fo:block text-align="justify">Setiap pembayaran premi asuransi harap mencantumkan nomor polisnya Nota pelunasan dari Bank dikirim ke <fo:inline font-weight="bold">PT. Asuransi Bangun Askrida Cabang <%=JSPUtil.printX(form.getStBranchDesc())%></fo:inline></fo:block>
                                <fo:block text-align="justify"><%=JSPUtil.printX(form.getCostCenter().getStAddress())%></fo:block>
                            </fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >*</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3">
                                <fo:block text-align="justify">Jumlah tagihan sebesar <fo:inline font-weight="bold">Rp. <%=JSPUtil.printX(t[2], 0)%></fo:inline> dapat Saudara pindah bukukan ke dalam Rek. Giro kami pada No. Rekening <fo:inline font-weight="bold"><%=JSPUtil.printX(rekening)%></fo:inline> untuk mana kami ucapkan terima kasih.</fo:block>
                                <fo:block text-align="justify">Penyerahan Polis / Lamp. Nota Kwitansi tersebut belum berarti pembayaran telah diterima.</fo:block>
                            </fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5">
                                <fo:block space-before.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block font-size="6pt">Catatan : </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block font-size="6pt" text-align="justify">Sebagai tanda terima, harap tindasannya ditandatangani dan dikembalikan kepada kami.</fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2">
                                <fo:block text-align="center">Hormat kami,</fo:block>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(<%=form.getUser(Parameter.readString("BRANCH_" + form.getStBranch())).getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <fo:block text-align = "center"><fo:inline text-decoration="underline"><%=form.getUser(Parameter.readString("BRANCH_" + form.getStBranch())).getStUserName().toUpperCase()%></fo:inline></fo:block>
                                <fo:block text-align = "center"><%=form.getUser(Parameter.readString("BRANCH_" + form.getStBranch())).getStJobPosition().toUpperCase()%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="6pt" space-before.optimum="10pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>
            </fo:block>

            <fo:block text-align="start">
                <fo:instream-foreign-object>
                    <barcode:barcode
                        xmlns:barcode="http://barcode4j.krysalis.org/ns"
                        message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(t[2], 0)%>" orientation="0">
                        <barcode:datamatrix>
                            <barcode:height>40pt</barcode:height>
                            <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                        </barcode:datamatrix>
                    </barcode:barcode>
                </fo:instream-foreign-object>
            </fo:block>

            <%}%>

            <fo:block id="end-of-document"><fo:marker
                    marker-class-name="term">
                    <fo:instream-foreign-object>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>

        </fo:flow>
    </fo:page-sequence>
</fo:root>