<%@ page import="com.webfin.gl.util.GLUtil,
         com.webfin.gl.ejb.GLReportEngine2,
         com.webfin.gl.model.AccountView,
         com.crux.util.JSPUtil,
         com.webfin.gl.report2.form.FinReportForm,
         com.crux.util.DateUtil,
         java.util.Date,
         com.crux.util.BDUtil,
         com.crux.util.SQLAssembler,
         java.math.BigDecimal"%><?xml version="1.0" ?>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines the layout master -->
    <fo:layout-master-set>
        <fo:simple-page-master master-name="first"
                               page-height="32cm"
                               page-width="21cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="2.5cm"
                               margin-right="2.5cm">
            <fo:region-body margin-top="2cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="1cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>
    </fo:layout-master-set>

    <!-- starts actual layout -->
    <fo:page-sequence master-reference="first">

        <%
                    GLReportEngine2 glr = new GLReportEngine2();

                    FinReportForm form = (FinReportForm) request.getAttribute("FORM");

                    long lPeriodFrom = form.getLPeriodFrom();
                    long lPeriodTo = form.getLPeriodTo();
                    long lYearFrom = form.getLYearFrom();
                    long lYearTo = form.getLYearFrom();

                    glr.setBranch(form.getBranch());
                    glr.setStFlag("Y");
                    BigDecimal kali = new BigDecimal(-1);

                    //KONVEN
                    BigDecimal premi_bruto1 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal premi_reas1 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal premi_kenaikan1 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal jumlahPendapatanPremi1 = BDUtil.add(premi_bruto1, BDUtil.add(premi_reas1, premi_kenaikan1));

                    BigDecimal klaim_bruto1 = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal klaim_reas1 = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal kenaikan_klaim1 = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal jumlahBebanKlaim1 = BDUtil.add(klaim_bruto1, klaim_reas1);
                    jumlahBebanKlaim1 = BDUtil.add(jumlahBebanKlaim1, kenaikan_klaim1);

                    BigDecimal beban_komisi_netto1 = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal beban_und_lain1 = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal jumlahBebanUnderwriting1 = BDUtil.add(jumlahBebanKlaim1, beban_komisi_netto1);
                    jumlahBebanUnderwriting1 = BDUtil.add(jumlahBebanUnderwriting1, beban_und_lain1);

                    BigDecimal investasi1 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal beban_usaha1 = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

                    BigDecimal pajakPenghasilan1 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal pajakPenghasilanTangguhan1 = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

                    BigDecimal hasilUnderWriting1 = BDUtil.add(jumlahPendapatanPremi1, jumlahBebanUnderwriting1);
                    BigDecimal laba_usaha1 = BDUtil.add(hasilUnderWriting1, investasi1);
                    laba_usaha1 = BDUtil.add(laba_usaha1, beban_usaha1);
                    BigDecimal penghasilanBeban691 = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
                    BigDecimal penghasilanBeban891 = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearFrom));
                    BigDecimal penghasilanBeban1 = BDUtil.add(penghasilanBeban691, penghasilanBeban891);

                    BigDecimal labaSebelumPajak1 = BDUtil.negate(BDUtil.add(laba_usaha1, penghasilanBeban1));

                    BigDecimal pajakPenghasilanTotal1 = BDUtil.sub(labaSebelumPajak1, pajakPenghasilan1);
                    pajakPenghasilanTotal1 = BDUtil.add(pajakPenghasilanTotal1, BDUtil.negate(pajakPenghasilanTangguhan1));

                    BigDecimal laba_bersih1 = BDUtil.roundUp(glr.getSummaryRangedOnePeriod("BAL|NEG=5", "51611", "51611", lPeriodTo, lYearFrom, lYearTo));
                    //BigDecimal selisih_nilai = BDUtil.sub(laba_bersih, BDUtil.add(laba_usaha, pajakPenghasilanTotal));
                    BigDecimal selisih_nilai1 = BDUtil.add(laba_bersih1, pajakPenghasilanTotal1);

                    penghasilanBeban1 = BDUtil.add(penghasilanBeban1, selisih_nilai1);
                    labaSebelumPajak1 = BDUtil.sub(labaSebelumPajak1, selisih_nilai1);

                    //System.out.print("##################### : "+penghasilanBeban1);

                    //SYARIAH
                    BigDecimal premi_bruto_kon = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal premi_bruto_sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal premi_bruto = BDUtil.sub(premi_bruto_kon, premi_bruto_sya);
                    BigDecimal premi_reas_kon = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal premi_reas_sya = BDUtil.roundUp(glr.getSummaryRangedExcludedSyariah("BAL|ADD=0", "63", "63", "638", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal premi_reas = BDUtil.add(premi_reas_kon, premi_reas_sya);
                    BigDecimal premi_kenaikan_kon = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal premi_kenaikan_sya1 = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "664", "664", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal premi_kenaikan_sya2 = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=0", "665", "665", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal premi_kenaikan_sya = BDUtil.sub(premi_kenaikan_sya1, premi_kenaikan_sya2);
                    BigDecimal premi_kenaikan = BDUtil.add(premi_kenaikan_kon, premi_kenaikan_sya);
                    BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

                    BigDecimal klaim_bruto_kon = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal klaim_bruto_sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal klaim_bruto = BDUtil.add(klaim_bruto_kon, klaim_bruto_sya);
                    BigDecimal klaim_reas_kon = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal klaim_reas_sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal klaim_reas = BDUtil.sub(klaim_reas_kon, klaim_reas_sya);
                    BigDecimal kenaikan_klaim_kon = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal kenaikan_klaim_sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|NEG|ADD=1,BBU", "751", "751", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal kenaikan_klaim_sya2 = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|NEG|ADD=1,BBU", "752", "752", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal kenaikan_klaim = BDUtil.add(kenaikan_klaim_kon, BDUtil.sub(kenaikan_klaim_sya, kenaikan_klaim_sya2));
                    BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, BDUtil.add(klaim_reas, kenaikan_klaim));

                    String retensiklaim = null;
                    if (BDUtil.isNegative(kenaikan_klaim)) {
                        retensiklaim = "(" + JSPUtil.printX(BDUtil.negate(kenaikan_klaim),0) + ")";
                    } else {
                        retensiklaim = JSPUtil.printX(kenaikan_klaim,0);
                    }

                    BigDecimal beban_komisi_netto_kon = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal beban_komisi_netto_sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|NEG|ADD=BBU", "771", "771", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal ujroh_sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|NEG|ADD=BBU", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal surplus_sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|NEG|ADD=BBU", "650", "650", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    beban_komisi_netto_sya = BDUtil.sub(beban_komisi_netto_sya, BDUtil.add(ujroh_sya, surplus_sya));
                    BigDecimal beban_komisi_netto = BDUtil.add(beban_komisi_netto_kon, beban_komisi_netto_sya);

                    BigDecimal beban_uw_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
                    jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_uw_lain);

                    BigDecimal investasi_kon = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal investasi_sya1 = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=HI", "651", "651", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal investasi_sya2 = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=HI", "655", "655", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal investasi_sya3 = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=HI", "658", "658", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal investasi_sya = BDUtil.add(investasi_sya1, investasi_sya2);
                    investasi_sya = BDUtil.add(investasi_sya, investasi_sya3);
                    BigDecimal investasi = BDUtil.sub(investasi_kon, investasi_sya);
                    BigDecimal beban_usaha_kon = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal beban_usaha_sya = BDUtil.roundUp(glr.getSummaryRangedExcludedSyariah("BAL|NEG|ADD=BU", "81", "83", "8290", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal beban_usaha = BDUtil.add(beban_usaha_kon, beban_usaha_sya);

                    BigDecimal pajakPenghasilan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal pajakZakat = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal pajakPenghasilanTotal = BDUtil.sub(pajakPenghasilan, pajakZakat);

                    BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
                    BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
                    laba_usaha = BDUtil.add(laba_usaha, beban_usaha);

                    BigDecimal penghasilanBeban_kon = BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)));
                    BigDecimal hasil_beban_sya = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|NEG|ADD=3", "8290", "8290", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal alokasi_sya1 = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|NEG|ADD=3", "6380", "6380", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    BigDecimal alokasi_sya2 = BDUtil.roundUp(glr.getSummaryRangedSyariah("BAL|NEG|ADD=3", "6381", "6381", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                    //BigDecimal penghasilanBeban = BDUtil.add(BDUtil.mul(penghasilanBeban_kon, kali), hasil_beban_sya);
                    //penghasilanBeban = BDUtil.sub(penghasilanBeban, alokasi_sya1);
                    //penghasilanBeban = BDUtil.sub(penghasilanBeban, alokasi_sya2);

                    BigDecimal penghasilanBeban2 = BDUtil.add(BDUtil.mul(penghasilanBeban1, kali), hasil_beban_sya);
                    penghasilanBeban2 = BDUtil.sub(penghasilanBeban2, alokasi_sya1);
                    penghasilanBeban2 = BDUtil.sub(penghasilanBeban2, alokasi_sya2);

                    BigDecimal laba_bersih_kon = glr.getSummaryRangedOnePeriod("BAL|NEG=5", "51611", "51611", lPeriodTo, lYearFrom, lYearTo);
                    BigDecimal laba_bersih_sya = glr.getSummaryRangedOnePeriodSyariah("BAL|NEG=5", null, "51611", "51611", lPeriodTo, lYearFrom, lYearTo);
                    BigDecimal laba_bersih = BDUtil.sub(laba_bersih_kon, laba_bersih_sya);

                    BigDecimal selisih_nilai = BDUtil.sub(laba_bersih, BDUtil.sub(laba_usaha, penghasilanBeban2));
                    selisih_nilai = BDUtil.add(selisih_nilai, pajakZakat);

                    //penghasilanBeban = BDUtil.add(penghasilanBeban, selisih_nilai);

        %>

        <fo:flow flow-name="xsl-region-body">

            <%
                        String bw = "0.5pt";
            %>

            <%if (form.getBranch() != null) {
                            if (!form.isPosted()) {%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" color="red">DRAFT</fo:block>
            <% }
                        }%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" >PT. ASURANSI BANGUN ASKRIDA</fo:block>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" >LABA RUGI GABUNGAN</fo:block>
            <%--<fo:block font-family="Helvetica" font-weight="bold" text-align="center" >KONSOLIDASI</fo:block>--%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" ><%=JSPUtil.printX(form.getPeriodTitleDescription())%></fo:block>
            <% if (form.getStKeterangan() != null) {%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" >(<%=JSPUtil.printX(form.getStKeteranganDesc())%>)</fo:block>
            <% }%>
            <% if (form.getBranch() != null) {%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" ><%=JSPUtil.printX(form.getCostCenter().getStDescription())%></fo:block>
            <% }%>
            <fo:block space-after.optimum="14pt"/>


            <fo:block font-family="Helvetica" font-size="10pt" display-align="center" border-width="0.1pt">
                <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>">
                    <fo:table-column column-width="130mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"  ><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">KETERANGAN</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">NILAI</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block font-weight="bold" font-style="italic">Pendapatan Premi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Premi Bruto</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(BDUtil.negate(premi_bruto), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Premi Reasuransi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(premi_reas, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Kenaikan (Penurunan) Premi yang belum merupakan pendapatan</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(premi_kenaikan, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-top-style="solid" border-bottom-style="solid"><fo:block font-weight="bold" font-style="italic">Jumlah Pendapatan Premi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(BDUtil.negate(jumlahPendapatanPremi), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row height="3mm">
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block font-weight="bold" font-style="italic">Beban Underwriting</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block font-weight="bold" font-style="italic">Beban Klaim</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Klaim Bruto</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(klaim_bruto, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Klaim Reasuransi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(BDUtil.negate(klaim_reas), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Kenaikan (penurunan) estimasi klaim retensi sendiri</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(retensiklaim)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-top-style="solid" border-bottom-style="solid"><fo:block font-weight="bold" font-style="italic">Jumlah Beban Klaim</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(jumlahBebanKlaim, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row height="3mm">
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Beban Komisi Netto</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(beban_komisi_netto, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Beban Underwriting Lain Netto</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(beban_uw_lain, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-top-style="solid" border-bottom-style="solid"><fo:block font-weight="bold" font-style="italic">Jumlah Beban Underwriting</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(jumlahBebanUnderwriting, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row height="3mm">
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Hasil Underwriting</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(BDUtil.negate(hasilUnderWriting), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Hasil Investasi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(BDUtil.negate(investasi), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Beban Usaha</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(beban_usaha, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Laba Usaha</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(BDUtil.negate(laba_usaha), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Penghasilan (Beban) Lain-lain</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" >(<%=JSPUtil.printX(BDUtil.negate(penghasilanBeban2), 0)%>)</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Laba Sebelum Zakat</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(BDUtil.negate(BDUtil.sub(laba_usaha, penghasilanBeban2)), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Zakat</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(pajakZakat, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Laba Sebelum Pajak</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(BDUtil.negate(laba_bersih), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Pajak Penghasilan</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(BDUtil.negate(pajakPenghasilan), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"  ><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">LABA BERSIH</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(BDUtil.negate(laba_bersih), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>

                <fo:block font-size="6pt"
                          font-family="sans-serif"
                          line-height="10pt"
                          space-after.optimum="15pt"
                          text-align="left" >
                    Print Stamp : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "hhmmssyyyyMMdd "))%>
                </fo:block>

                <fo:table table-layout="fixed">
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <fo:table-row>

                            <fo:table-cell>
                                <fo:block text-align="start">
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(BDUtil.negate(laba_bersih), 0)%>" orientation="0">
                                            <barcode:datamatrix>
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>
                                </fo:block>
                            </fo:table-cell>

                            <fo:table-cell>
                                <fo:table table-layout="fixed">
                                    <fo:table-column column-width="70mm"/>
                                    <fo:table-column />
                                    <fo:table-body>
                                        <fo:table-row height="10mm"/>

                                        <fo:table-row>
                                            <fo:table-cell number-columns-spanned="2" >
                                                <fo:block text-align="center">S.E. &amp; O.</fo:block>
                                                <fo:block text-align="center">JAKARTA, <%=DateUtil.getDateStr(form.getDtPrintDate(), "d ^^ yyyy")%></fo:block>
                                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                                <fo:block text-align="center">DIREKSI</fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>

                                        <fo:table-row height="20mm">
                                        </fo:table-row>

                                        <fo:table-row>
                                            <fo:table-cell>
                                                <fo:block text-align="center" text-decoration="underline"><%=JSPUtil.printX(form.getPerson1Name())%></fo:block>
                                                <fo:block text-align="center"><%=JSPUtil.printX(form.getPerson1Title())%></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell>
                                                <fo:block text-align="center" text-decoration="underline"><%=JSPUtil.printX(form.getPerson2Name())%></fo:block>
                                                <fo:block text-align="center"><%=JSPUtil.printX(form.getPerson2Title())%></fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>

                                    </fo:table-body>
                                </fo:table>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>

            </fo:block>
        </fo:flow>
    </fo:page-sequence>
</fo:root>