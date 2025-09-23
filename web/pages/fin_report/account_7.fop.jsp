<%@ page import="com.webfin.gl.util.GLUtil,
         com.webfin.gl.ejb.GLReportEngine2,
         com.webfin.gl.report2.form.FinReportForm,
         com.crux.ff.FlexTableManager,
         com.crux.ff.model.FlexTableView,
         com.webfin.gl.model.GLInfoView,
         com.crux.util.DateUtil,
         java.util.Date,
         java.math.BigDecimal,
         com.crux.util.JSPUtil,
         com.crux.util.BDUtil,
         java.util.HashMap,
         com.crux.util.*,
         java.util.ArrayList,
         com.crux.util.fop.FOPUtil"%><?xml version="1.0" ?>

<%
            GLReportEngine2 glr = new GLReportEngine2();

            FinReportForm form = (FinReportForm) request.getAttribute("FORM");

            long lPeriodFrom = form.getLPeriodFrom();
            long lPeriodTo = form.getLPeriodTo();
            long lYearFrom = form.getLYearFrom();
            long lYearTo = form.getLYearFrom();

            glr.setBranch(form.getBranch());
            glr.setStFlag("Y");

            String bw = "0.5pt";
%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines the layout master -->
    <fo:layout-master-set>
        <fo:simple-page-master master-name="first"
                               page-height="32cm"
                               page-width="21cm"
                               margin-top="1cm"
                               margin-bottom="2cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            <fo:region-body margin-top="1cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="1.5cm"/>
            <fo:region-after extent="1.5cm"/>
        </fo:simple-page-master>
    </fo:layout-master-set>

    <!-- starts actual layout -->
    <fo:page-sequence master-reference="first">

        <% {
                        BigDecimal deposito = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "1111", "1112", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal ser_depodito = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "1113", "1113", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal surat_bhrga = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "112", "114", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal penyertaan = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "115", "115", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal properti = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "116", "116", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal properti_pinjaman = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "117", "117", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal investasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "119", "119", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal jumlah_aktiva = BDUtil.add(deposito, ser_depodito);
                        jumlah_aktiva = BDUtil.add(jumlah_aktiva, surat_bhrga);
                        jumlah_aktiva = BDUtil.add(jumlah_aktiva, penyertaan);
                        jumlah_aktiva = BDUtil.add(jumlah_aktiva, properti);
                        jumlah_aktiva = BDUtil.add(jumlah_aktiva, properti_pinjaman);
                        jumlah_aktiva = BDUtil.add(jumlah_aktiva, investasi);

                        BigDecimal kas_bank = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "120", "123", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal piut_premi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "13", "13", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal piut_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "14", "14", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal aset_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "1492", "1493", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal piut_hsl_inves = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "15", "15", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal piut_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "16", "16", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        piut_lain = BDUtil.add(piut_lain, BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "124", "124", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)));
                        BigDecimal biaya_byr_muka = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "17", "17", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal aktiva_tetap = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "18", "18", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal aktiva_lain = BDUtil.roundUp(glr.getSummaryRangedExcluded("BAL|ADD=0", "19", "19", "19144", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal aktiva_pajak = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "19144", "19144", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal rek_kantor = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0", "21", "21", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal jumlah_aktiva2 = BDUtil.add(kas_bank, piut_premi);
                        jumlah_aktiva2 = BDUtil.add(jumlah_aktiva2, piut_reas);
                        jumlah_aktiva2 = BDUtil.add(jumlah_aktiva2, piut_hsl_inves);
                        jumlah_aktiva2 = BDUtil.add(jumlah_aktiva2, piut_lain);
                        jumlah_aktiva2 = BDUtil.add(jumlah_aktiva2, biaya_byr_muka);
                        jumlah_aktiva2 = BDUtil.add(jumlah_aktiva2, aktiva_tetap);
                        jumlah_aktiva2 = BDUtil.add(jumlah_aktiva2, aktiva_lain);
                        jumlah_aktiva2 = BDUtil.add(jumlah_aktiva2, aktiva_pajak);
                        jumlah_aktiva2 = BDUtil.add(jumlah_aktiva2, rek_kantor);

                        BigDecimal aktiva = BDUtil.add(jumlah_aktiva, jumlah_aktiva2);

                        BigDecimal utang_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=1", "33", "33", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal estimasi_klaim = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=1", "32", "32", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal premi_belum_pndptn = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=1", "34", "34", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal utang_reas = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=1", "42", "42", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal utang_komisi = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=1", "43", "43", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal utang_pajak = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=1", "44", "44", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal biaya_dibayar = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=1", "46", "46", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal utang_sewa = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=1", "47", "47", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal utang_lain = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=1", "48", "48", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal jumlah_kewajiban = BDUtil.add(utang_klaim, estimasi_klaim);
                        jumlah_kewajiban = BDUtil.add(jumlah_kewajiban, premi_belum_pndptn);
                        jumlah_kewajiban = BDUtil.add(jumlah_kewajiban, utang_reas);
                        jumlah_kewajiban = BDUtil.add(jumlah_kewajiban, utang_komisi);
                        jumlah_kewajiban = BDUtil.add(jumlah_kewajiban, utang_pajak);
                        jumlah_kewajiban = BDUtil.add(jumlah_kewajiban, biaya_dibayar);
                        jumlah_kewajiban = BDUtil.add(jumlah_kewajiban, utang_sewa);
                        jumlah_kewajiban = BDUtil.add(jumlah_kewajiban, utang_lain);

                        BigDecimal utang_subordinasi = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2", "49", "49", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal modal_dasar = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2", "512", "512", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal modal_disetor = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2", "5111", "5111", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal titipan_disetor = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2", "5113", "5113", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal agio_saham = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2", "513", "513", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal cadangan_umum = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2", "51620", "51621", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal kerugian_penurunan = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2", "514", "514", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal cadangan_khusus = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2", "51622", "51622", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal laba_rugi_ditahan = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2", "51623", "51623", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        //BigDecimal selisih_kembali      = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2","515","515",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
                        BigDecimal laba_rugi_tahun_lalu = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2", "51610", "51610", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                        BigDecimal laba_rugi_berjalan = BDUtil.roundUp(glr.getSummaryRangedOnePeriod("BAL|NEG|ADD=2", "51611", "51611", lPeriodTo, lYearFrom, lYearTo));

                        BigDecimal jumlah_ekuitas = BDUtil.add(modal_dasar, modal_disetor);
                        jumlah_ekuitas = BDUtil.add(jumlah_ekuitas, titipan_disetor);
                        jumlah_ekuitas = BDUtil.add(jumlah_ekuitas, cadangan_umum);
                        jumlah_ekuitas = BDUtil.add(jumlah_ekuitas, kerugian_penurunan);
                        jumlah_ekuitas = BDUtil.add(jumlah_ekuitas, cadangan_khusus);
                        jumlah_ekuitas = BDUtil.add(jumlah_ekuitas, laba_rugi_ditahan);
                        //jumlah_ekuitas     = BDUtil.add(jumlah_ekuitas, selisih_kembali);
                        jumlah_ekuitas = BDUtil.add(jumlah_ekuitas, laba_rugi_tahun_lalu);
                        jumlah_ekuitas = BDUtil.add(jumlah_ekuitas, laba_rugi_berjalan);

                        BigDecimal subordinasi_ekuitas = BDUtil.add(jumlah_ekuitas, utang_subordinasi);

                        BigDecimal selisih_nilai = BDUtil.add(aktiva, BDUtil.add(jumlah_kewajiban, subordinasi_ekuitas));//-2

                        utang_lain = BDUtil.add(utang_lain, selisih_nilai);

                        BigDecimal kewajiban = BDUtil.add(jumlah_kewajiban, subordinasi_ekuitas);
                        kewajiban = BDUtil.sub(kewajiban, selisih_nilai);

                        //BigDecimal rekeningKantor   = form.updateRekeningKantor(BDUtil.negate(balance),"Y");

        %>

        <fo:flow flow-name="xsl-region-body">

            <%if (form.getBranch() != null) {
                            if (!form.isPosted()) {%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" color="red">DRAFT</fo:block>
            <% }
                        }%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="20mm">NERACA</fo:block>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" ><%=JSPUtil.printX(form.getPeriodTitleDescription())%></fo:block>
            <% if (form.getStKeterangan() != null) {%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" >(<%=JSPUtil.printX(form.getStKeteranganDesc())%>)</fo:block>
            <% }%>
            <% if (form.getBranch() != null) {%>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" ><%=JSPUtil.printX(form.getCostCenter().getStDescription())%></fo:block>
            <% }%>
            <fo:block space-after.optimum="14pt"/>

            <fo:block font-family="Helvetica" font-size="8pt" display-align="center" border-width="0.1pt">
                <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>">
                    <fo:table-column column-width="70mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="70mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" number-columns-spanned="2" ><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">AKTIVA</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" number-columns-spanned="2" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">KEWAJIBAN DAN EKUITAS</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="2pt"><fo:block font-style="italic">Deposito</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(deposito, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block font-style="italic">Utang Klaim</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" text-align="end" padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(utang_klaim), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="2pt"><fo:block font-style="italic">Sertifikat Deposito</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(ser_depodito, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block font-style="italic">Estimasi Klaim Retensi Sendiri</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(estimasi_klaim), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="2pt"><fo:block font-style="italic">Surat Berharga Reksadana</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(surat_bhrga, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block font-style="italic">Premi yg belum merupakan pendapatan</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(premi_belum_pndptn), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="2pt"><fo:block font-style="italic">Penyertaan</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(penyertaan, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block font-style="italic">Utang Reasuransi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(utang_reas), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="2pt"><fo:block font-style="italic">Properti</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(properti, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block font-style="italic">Utang Komisi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(utang_komisi), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="2pt"><fo:block font-style="italic">Properti pinjaman hipotek</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(properti_pinjaman, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block font-style="italic">Utang Pajak</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(utang_pajak), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="2pt"><fo:block font-style="italic">Investasi Lain</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(investasi, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block font-style="italic">Biaya yg masih harus dibayar</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(biaya_dibayar), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="2pt"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block font-style="italic">Utang Sewa Guna Usaha</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(utang_sewa), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="2pt"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block font-style="italic">Utang Lain-lain</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(BDUtil.negate(utang_lain), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row height="5mm">
                            <fo:table-cell padding="2pt"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="2pt"><fo:block>JUMLAH INVESTASI</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(jumlah_aktiva, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block>JUMLAH KEWAJIBAN</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(jumlah_kewajiban), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row height="5mm">
                            <fo:table-cell padding="2pt"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="2pt"><fo:block font-style="italic" start-indent="3mm">Kas dan Bank</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(kas_bank, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block font-style="italic">Utang Jangka Panjang</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(utang_subordinasi), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="2pt"><fo:block font-style="italic" start-indent="3mm">Piutang Premi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(piut_premi, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="2pt"><fo:block font-style="italic" start-indent="3mm">Piutang Reasuransi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(BDUtil.sub(piut_reas, aset_reas), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="2pt"><fo:block font-style="italic" start-indent="3mm">Aset Reasuransi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(aset_reas, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block font-weight="bold">EKUITAS</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="2pt"><fo:block font-style="italic" start-indent="3mm">Piutang Hasil Investasi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(piut_hsl_inves, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid">
                                <fo:block font-style="italic">Modal Dasar 3000 Lbr @ RP.10.000.000</fo:block>
                                <fo:block font-style="italic" start-indent="3mm">Modal Ditempatkan dan Disetor</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(modal_disetor), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="2pt"><fo:block font-style="italic" start-indent="3mm">Piutang Lain</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(piut_lain, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block font-style="italic">Titipan Modal Disetor</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(titipan_disetor), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="2pt"><fo:block font-style="italic" start-indent="3mm">Biaya Dibayar Dimuka</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(biaya_byr_muka, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block font-style="italic">Agio Saham</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(agio_saham), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="2pt"><fo:block font-style="italic" start-indent="3mm">Aktiva Tetap</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(aktiva_tetap, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block font-style="italic">Cadangan umum</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(cadangan_umum), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="2pt"><fo:block font-style="italic" start-indent="3mm">Aktiva Lain-lain</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(aktiva_lain, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid">
                                <fo:block font-style="italic">Kerugian (Keuntungan) blm direal. </fo:block>
                                <fo:block font-style="italic" start-indent="3mm">Atas Penurunan Nilai Wajib Efek Siap Jual</fo:block>
                            </fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(kerugian_penurunan), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="2pt"><fo:block font-style="italic" start-indent="3mm">Aktiva Pajak Tangguhan</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(aktiva_pajak, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block font-style="italic">Cadangan Khusus</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(cadangan_khusus), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <%if (form.getBranch() != null) {%>
                            <fo:table-cell padding="2pt"><fo:block font-style="italic" start-indent="3mm">Rekening Antar Kantor</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(rek_kantor, 0)%></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell padding="2pt"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ></fo:block></fo:table-cell>
                            <% }%>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block font-style="italic">Laba (Rugi) ditahan</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(laba_rugi_ditahan), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="2pt"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block font-style="italic">Saldo Laba (Rugi) Tahun Lalu</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(laba_rugi_tahun_lalu), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="2pt"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block font-style="italic">Saldo Laba (Rugi) Tahun Berjalan</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(laba_rugi_berjalan), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="2pt"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block font-weight="bold" text-align="center">Jumlah Ekuitas</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><fo:inline text-decoration="overline"><%=JSPUtil.printX(BDUtil.negate(jumlah_ekuitas), 0)%></fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-top-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" font-weight="bold"  >JUMLAH AKTIVA</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="10mm"  font-weight="bold" background-color="#C0C0C0"  ><%=JSPUtil.printX(aktiva, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-top-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0"  font-weight="bold">JUMLAH KEWAJIBAN &amp; EKUITAS</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="10mm" font-weight="bold" background-color="#C0C0C0"  ><%=JSPUtil.printX(BDUtil.negate(kewajiban), 0)%></fo:block></fo:table-cell>
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
                                            message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(BDUtil.negate(kewajiban), 0)%>" orientation="0">
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
                                                <fo:block text-align="center">JAKARTA, <%=DateUtil.getDateStr(new Date(), "d ^^ yyyy")%></fo:block>
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
        <% }%>
    </fo:page-sequence>

    <fo:page-sequence master-reference="first">

        <% {
                        BigDecimal kali = new BigDecimal(-1);

                        BigDecimal premi_bruto = glr.getSummaryRanged("BAL|ADD=0", "61", "61", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo);
                        BigDecimal premi_reas = glr.getSummaryRanged("BAL|ADD=0", "63", "63", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo);
                        BigDecimal premi_kenaikan = glr.getSummaryRanged("BAL|ADD=0", "64", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo);
                        BigDecimal jumlahPendapatanPremi = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));

                        BigDecimal klaim_bruto = glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "71", "71", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo);
                        BigDecimal klaim_reas = glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "72", "72", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo);
                        BigDecimal kenaikan_klaim = glr.getSummaryRanged("BAL|NEG|ADD=1,BBU", "75", "75", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo);
                        BigDecimal jumlahBebanKlaim = BDUtil.add(klaim_bruto, klaim_reas);
                        jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);

                        BigDecimal beban_komisi_netto = glr.getSummaryRanged("BAL|NEG|ADD=BBU", "77", "77", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo);
                        BigDecimal beban_und_lain = glr.getSummaryRanged("BAL|NEG|ADD=BBU", "79", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo);
                        BigDecimal jumlahBebanUnderwriting = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
                        jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);

                        BigDecimal investasi = glr.getSummaryRanged("BAL|ADD=HI", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo);
                        BigDecimal beban_usaha = glr.getSummaryRanged("BAL|NEG|ADD=BU", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo);

                        BigDecimal pajakPenghasilan = glr.getSummaryRanged("BAL|ADD=4", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo);
                        BigDecimal pajakPenghasilanTangguhan = glr.getSummaryRanged("BAL|ADD=4", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo);
                        BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);

                        BigDecimal hasilUnderWriting = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
                        BigDecimal laba_usaha = BDUtil.add(hasilUnderWriting, investasi);
                        laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
                        BigDecimal penghasilanBeban = BDUtil.add(glr.getSummaryRanged("BAL|NEG|ADD=3", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo), glr.getSummaryRanged("BAL|NEG|ADD=3", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));

                        //BigDecimal laba_bersih = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);
                        BigDecimal laba_bersih = glr.getSummaryRangedOnePeriod("BAL|NEG=5", "51611", "51611", lPeriodTo, lYearFrom, lYearTo);
                        BigDecimal selisih_nilai = BDUtil.sub(laba_bersih, BDUtil.add(laba_usaha, penghasilanBeban));

                        penghasilanBeban = BDUtil.add(penghasilanBeban, selisih_nilai);
                        //BigDecimal laba_bersih_final        = form.updateLabaBersih(BDUtil.roundUp(laba_bersih),"Y");
        %>

        <fo:flow flow-name="xsl-region-body">

            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="20mm">LABA RUGI</fo:block>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" ><%=JSPUtil.printX(form.getPeriodTitleDescription())%></fo:block>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" ><%=JSPUtil.printX(form.getCostCenter().getStDescription())%></fo:block>
            <fo:block space-after.optimum="14pt"/>

            <fo:block font-family="Helvetica" font-size="10pt" display-align="center" border-width="0.1pt">
                <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>">
                    <fo:table-column column-width="150mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-body>
                        <fo:table-row  >
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"  ><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">KETERANGAN</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">NILAI</fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block font-weight="bold" font-style="italic">Pendapatan Premi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Premi Bruto</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(BDUtil.negate(premi_bruto), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Premi Reasuransi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(premi_reas, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Kenaikan (Penurunan) Premi yang belum merupakan pendapatan</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(premi_kenaikan, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row >
                            <fo:table-cell  border-width="<%=bw%>" padding="2pt" border-top-style="solid" border-bottom-style="solid"><fo:block font-weight="bold" font-style="italic">Jumlah Pendapatan Premi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(BDUtil.negate(jumlahPendapatanPremi), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row height="3mm">
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block font-weight="bold" font-style="italic">Beban Underwriting</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block font-weight="bold" font-style="italic">Beban Klaim</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Klaim Bruto</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(klaim_bruto, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Klaim Reasuransi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(BDUtil.negate(klaim_reas), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Kenaikan (penurunan) estimasi klaim retensi sendiri</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(kenaikan_klaim, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row >
                            <fo:table-cell  border-width="<%=bw%>" padding="2pt" border-top-style="solid" border-bottom-style="solid"><fo:block font-weight="bold" font-style="italic">Jumlah Beban Klaim</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(jumlahBebanKlaim, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row height="3mm">
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Beban Komisi Netto</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(beban_komisi_netto, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Beban Underwriting Lain Netto</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(beban_und_lain, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row >
                            <fo:table-cell  border-width="<%=bw%>" padding="2pt" border-top-style="solid" border-bottom-style="solid"><fo:block font-weight="bold" font-style="italic">Jumlah Beban Underwriting</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(jumlahBebanUnderwriting, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row height="3mm">
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Hasil Underwriting</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(BDUtil.negate(hasilUnderWriting), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Hasil Investasi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(BDUtil.negate(investasi), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Beban Usaha</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(beban_usaha, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Laba Usaha</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(BDUtil.negate(laba_usaha), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Penghasilan (Beban) Lain-lain</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(BDUtil.negate(penghasilanBeban), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Laba Sebelum Pajak</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(BDUtil.negate(BDUtil.add(laba_usaha, penghasilanBeban)), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Pajak Penghasilan</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(BDUtil.negate(pajakPenghasilan), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Pajak Penghasilan (Beban) Tangguhan</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(BDUtil.negate(pajakPenghasilanTangguhan), 0)%></fo:block></fo:table-cell>
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
                                            message="<%=DateUtil.getDateStr(new Date(), "hhmmssyyyyMMdd")%>" orientation="0">
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
                                                <fo:block text-align="center">JAKARTA, <%=DateUtil.getDateStr(new Date(), "d ^^ yyyy")%></fo:block>
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
        <% }%>
    </fo:page-sequence>

    <fo:page-sequence master-reference="first">

        <fo:static-content flow-name="xsl-region-after">
            <fo:block text-align="end"
                      font-size="8pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>

        <%
                    {
                        HashMap refmap = form.getRptRef();

                        String ftGroup = (String) refmap.get("FT");

                        DTOList ftl = FlexTableManager.getInstance().getFlexTable("RL1");

                        ArrayList cmap = new ArrayList();

                        cmap.add(new Integer(5));
                        cmap.add(new Integer(90));
                        cmap.add(new Integer(35));
                        cmap.add(new Integer(35));
                        cmap.add(new Integer(35));

                        ArrayList cw = FOPUtil.computeColumnWidth(cmap, 20, 2, "cm");

        %>

        <fo:flow flow-name="xsl-region-body">

            <%
                                    for (int i = 0; i < ftl.size(); i++) {
                                        FlexTableView ft = (FlexTableView) ftl.get(i);

                                        String opCode = ft.getStReference2();
                                        String desc = ft.getStReference3();

                                        boolean isTITLE = "TITLE".equalsIgnoreCase(opCode);

                                        if (isTITLE) {
            %>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="20mm"><%=desc%></fo:block>
            <%
                                        }

                                    }
            %>

            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" ><%=JSPUtil.printX(form.getPeriodTitleDescription())%></fo:block>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" ><%=JSPUtil.printX(form.getCostCenter().getStDescription())%></fo:block>
            <fo:block space-after.optimum="14pt"/>

            <fo:block font-family="Helvetica" font-size="6pt" >
                <fo:table table-layout="fixed">
                    <%
                                            for (int i = 0; i < cw.size(); i++) {
                                                String cwx = (String) cw.get(i);
                    %>
                    <fo:table-column column-width="<%=cwx%>"/>
                    <%
                                            }
                    %>

                    <fo:table-header>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block font-size="8pt" text-align="end" font-style="italic">(Dalam Rupiah)</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">NO</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">KETERANGAN</fo:block></fo:table-cell>
                            <% if (form.getLPeriodTo() != 1) {%>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" ><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">SALDO <%=JSPUtil.printX(form.getPeriodBeforeTitleDescription().toUpperCase())%></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" ><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">SALDO PER 31 JANUARI <%=JSPUtil.printX(form.getYearFrom())%></fo:block></fo:table-cell>
                            <% }%>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" ><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">MUTASI BLN <%=JSPUtil.printX(form.getMonthTitleDescription().toUpperCase())%></fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" border-right-style="solid"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">SALDO <%=JSPUtil.printX(form.getPeriodTitleDescription().toUpperCase())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-header>
                    <fo:table-body>

                        <%
                                                String[] indents = null;

                                                BigDecimal atot1 = new BigDecimal(0);
                                                BigDecimal atot2 = new BigDecimal(0);
                                                BigDecimal atot3 = new BigDecimal(0);
                                                BigDecimal atot4 = new BigDecimal(0);
                                                BigDecimal atot5 = new BigDecimal(0);
                                                BigDecimal atot6 = new BigDecimal(0);
                                                BigDecimal atot7 = new BigDecimal(0);
                                                BigDecimal atot8 = new BigDecimal(0);
                                                BigDecimal atot9 = new BigDecimal(0);
                                                BigDecimal atot10 = new BigDecimal(0);
                                                BigDecimal atot11 = new BigDecimal(0);
                                                BigDecimal atot12 = new BigDecimal(0);
                                                BigDecimal atot13 = new BigDecimal(0);
                                                BigDecimal atot14 = new BigDecimal(0);
                                                BigDecimal atot15 = new BigDecimal(0);
                                                BigDecimal akun69A = new BigDecimal(0);
                                                BigDecimal akun69B = new BigDecimal(0);
                                                BigDecimal akun69C = new BigDecimal(0);
                                                BigDecimal akun8911A = new BigDecimal(0);
                                                BigDecimal akun8911B = new BigDecimal(0);
                                                BigDecimal akun8911C = new BigDecimal(0);
                                                BigDecimal akun8921A = new BigDecimal(0);
                                                BigDecimal akun8921B = new BigDecimal(0);
                                                BigDecimal akun8921C = new BigDecimal(0);
                                                BigDecimal akun65A = new BigDecimal(0);
                                                BigDecimal akun65B = new BigDecimal(0);
                                                BigDecimal akun65C = new BigDecimal(0);
                                                BigDecimal akun8183A = new BigDecimal(0);
                                                BigDecimal akun8183B = new BigDecimal(0);
                                                BigDecimal akun8183C = new BigDecimal(0);
                                                BigDecimal akun811A = new BigDecimal(0);
                                                BigDecimal akun811B = new BigDecimal(0);
                                                BigDecimal akun811C = new BigDecimal(0);
                                                BigDecimal akun646A = new BigDecimal(0);
                                                BigDecimal akun646B = new BigDecimal(0);
                                                BigDecimal akun646C = new BigDecimal(0);
                                                BigDecimal akun647A = new BigDecimal(0);
                                                BigDecimal akun647B = new BigDecimal(0);
                                                BigDecimal akun647C = new BigDecimal(0);
                                                BigDecimal akun753A = new BigDecimal(0);
                                                BigDecimal akun753B = new BigDecimal(0);
                                                BigDecimal akun753C = new BigDecimal(0);
                                                BigDecimal akun754A = new BigDecimal(0);
                                                BigDecimal akun754B = new BigDecimal(0);
                                                BigDecimal akun754C = new BigDecimal(0);

                                                for (int i = 0; i < ftl.size(); i++) {
                                                    FlexTableView ft = (FlexTableView) ftl.get(i);

                                                    String style = ft.getStReference1();
                                                    String opCode = ft.getStReference2();
                                                    String desc = ft.getStReference3();
                                                    String acctFrom = ft.getStReference4();
                                                    String acctTo = ft.getStReference5();
                                                    int iindent = ft.getStReference6() == null ? 0 : Integer.parseInt(ft.getStReference6());
                                                    String groupType = ft.getStReferenceID1();
                                                    String acctTo2 = ft.getStReference7();
                                                    BigDecimal kali = ft.getDbReference1();

                                                    String indent = (indents != null && indents.length > iindent) ? indents[iindent] : null;

                                                    if (style == null) {
                                                        style = "";
                                                    }

                                                    String[] styles = style.split("[\\|]");

                                                    style = styles[0];
                                                    String style1 = styles.length > 1 ? styles[1] : "";

                                                    if (indent != null) {
                                                        style += " start-indent=\"" + indent + "\"";
                                                    }

                                                    if (opCode == null) {
                                                        continue;
                                                    }

                                                    boolean isINDENT = "INDENT".equalsIgnoreCase(opCode);
                                                    boolean isDESC = "DESC".equalsIgnoreCase(opCode);
                                                    boolean isDESC1 = "DESC1".equalsIgnoreCase(opCode);
                                                    boolean isACCT = "ACCT".equalsIgnoreCase(opCode);
                                                    boolean isACTT = "ACTT".equalsIgnoreCase(opCode);
                                                    boolean isAAAT = "AAAT".equalsIgnoreCase(opCode);
                                                    boolean isAATT = "AATT".equalsIgnoreCase(opCode);
                                                    boolean isACCC = "ACCC".equalsIgnoreCase(opCode);
                                                    boolean isATTT = "ATTT".equalsIgnoreCase(opCode);
                                                    boolean isNL = "NL".equalsIgnoreCase(opCode);
                                                    boolean isPAGE = "PAGE".equalsIgnoreCase(opCode);
                                                    boolean isTOT = "TOT".equalsIgnoreCase(opCode);
                                                    boolean isATOT1 = "ATOT1".equalsIgnoreCase(opCode);
                                                    boolean isATOT2 = "ATOT2".equalsIgnoreCase(opCode);
                                                    boolean isATOT3 = "ATOT3".equalsIgnoreCase(opCode);
                                                    boolean isATOT4 = "ATOT4".equalsIgnoreCase(opCode);
                                                    boolean isATOT5 = "ATOT5".equalsIgnoreCase(opCode);
                                                    boolean isATOT6 = "ATOT6".equalsIgnoreCase(opCode);
                                                    boolean isATOT7 = "ATOT7".equalsIgnoreCase(opCode);
                                                    boolean isATOT8 = "ATOT8".equalsIgnoreCase(opCode);
                                                    boolean isATOT = opCode.indexOf("ATOT") == 0;

                                                    if (isATOT) {
                                                        atot1 = BDUtil.add(glr.getSummaryRanged("BAL|ADD=0", "61", "64", lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo), glr.getSummaryRanged("BAL|ADD=0", "71", "79", lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo));
                                                        atot2 = BDUtil.add(glr.getSummaryRangedOnePeriod("BAL|ADD=0", "61", "64", lPeriodTo, lYearFrom, lYearTo), glr.getSummaryRangedOnePeriod("BAL|ADD=0", "71", "79", lPeriodTo, lYearFrom, lYearTo));
                                                        atot3 = BDUtil.add(glr.getSummaryRanged("BAL|ADD=0", "61", "64", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo), glr.getSummaryRanged("BAL|ADD=0", "71", "79", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                                        atot4 = glr.getSummaryRanged("BAL|ADD=0", "65", "65", lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo);
                                                        atot5 = glr.getSummaryRangedOnePeriod("BAL|ADD=0", "65", "65", lPeriodTo, lYearFrom, lYearTo);
                                                        atot6 = glr.getSummaryRanged("BAL|ADD=0", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo);
                                                        atot7 = glr.getSummaryRanged("BAL|ADD=0", "81", "83", lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo);
                                                        atot8 = glr.getSummaryRangedOnePeriod("BAL|ADD=0", "81", "83", lPeriodTo, lYearFrom, lYearTo);
                                                        atot9 = glr.getSummaryRanged("BAL|ADD=0", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo);
                                                        atot10 = BDUtil.add(glr.getSummaryRanged("BAL|ADD=1", "69", "69", lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo), glr.getSummaryRanged("BAL|ADD=1", "89", "89", lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo));
                                                        atot11 = BDUtil.add(glr.getSummaryRangedOnePeriod("BAL|ADD=1", "69", "69", lPeriodTo, lYearFrom, lYearTo), glr.getSummaryRangedOnePeriod("BAL|ADD=1", "89", "89", lPeriodTo, lYearFrom, lYearTo));
                                                        atot12 = BDUtil.add(glr.getSummaryRanged("BAL|ADD=1", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo), glr.getSummaryRanged("BAL|ADD=1", "89", "89", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                                        atot13 = BDUtil.add(glr.getSummaryRanged("BAL|ADD=0", "90", "90", lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo), glr.getSummaryRanged("BAL|ADD=0", "91", "91", lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo));
                                                        atot14 = BDUtil.add(glr.getSummaryRangedOnePeriod("BAL|ADD=0", "90", "90", lPeriodTo, lYearFrom, lYearTo), glr.getSummaryRangedOnePeriod("BAL|ADD=0", "91", "91", lPeriodTo, lYearFrom, lYearTo));
                                                        atot15 = BDUtil.add(glr.getSummaryRanged("BAL|ADD=0", "90", "90", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo), glr.getSummaryRanged("BAL|ADD=0", "91", "91", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                                        akun69A = glr.getSummaryRanged("BAL|ADD=0", "69", "69", lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo);
                                                        akun69B = glr.getSummaryRangedOnePeriod("BAL|ADD=0", "69", "69", lPeriodTo, lYearFrom, lYearTo);
                                                        akun69C = glr.getSummaryRanged("BAL|ADD=0", "69", "69", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo);
                                                        akun8911A = glr.getSummaryRanged("BAL|ADD=0", "8911", "8911", lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo);
                                                        akun8911B = glr.getSummaryRangedOnePeriod("BAL|ADD=0", "8911", "8911", lPeriodTo, lYearFrom, lYearTo);
                                                        akun8911C = glr.getSummaryRanged("BAL|ADD=0", "8911", "8911", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo);
                                                        akun8921A = glr.getSummaryRanged("BAL|ADD=0", "8921", "8921", lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo);
                                                        akun8921B = glr.getSummaryRangedOnePeriod("BAL|ADD=0", "8921", "8921", lPeriodTo, lYearFrom, lYearTo);
                                                        akun8921C = glr.getSummaryRanged("BAL|ADD=0", "8921", "8921", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo);
                                                        akun65A = BDUtil.sub(glr.getSummaryRanged("BAL|ADD=0", "65", "65", lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo), glr.getSummaryRanged("BAL|ADD=0", "65111", "65112", lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo));
                                                        akun65B = BDUtil.sub(glr.getSummaryRangedOnePeriod("BAL|ADD=0", "65", "65", lPeriodTo, lYearFrom, lYearTo), glr.getSummaryRangedOnePeriod("BAL|ADD=0", "65111", "65112", lPeriodTo, lYearFrom, lYearTo));
                                                        akun65C = BDUtil.sub(glr.getSummaryRanged("BAL|ADD=0", "65", "65", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo), glr.getSummaryRanged("BAL|ADD=0", "65111", "65112", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                                        akun8183A = glr.getSummaryRanged("BAL|ADD=0", "81", "83", lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo);
                                                        akun8183B = glr.getSummaryRangedOnePeriod("BAL|ADD=0", "81", "83", lPeriodTo, lYearFrom, lYearTo);
                                                        akun8183C = glr.getSummaryRanged("BAL|ADD=0", "81", "83", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo);
                                                        akun811A = BDUtil.add(glr.getSummaryRanged("BAL|ADD=0", "811", "829", lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo), glr.getSummaryRanged("BAL|ADD=0", "833", "834", lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo));
                                                        akun811B = BDUtil.add(glr.getSummaryRangedOnePeriod("BAL|ADD=0", "811", "829", lPeriodTo, lYearFrom, lYearTo), glr.getSummaryRangedOnePeriod("BAL|ADD=0", "833", "834", lPeriodTo, lYearFrom, lYearTo));
                                                        akun811C = BDUtil.add(glr.getSummaryRanged("BAL|ADD=0", "811", "829", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo), glr.getSummaryRanged("BAL|ADD=0", "833", "834", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo));
                                                        akun646A = glr.getSummaryRanged("BAL|ADD=0", "646", "646", lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo);
                                                        akun646B = glr.getSummaryRangedOnePeriod("BAL|ADD=0", "646", "646", lPeriodTo, lYearFrom, lYearTo);
                                                        akun646C = glr.getSummaryRanged("BAL|ADD=0", "646", "646", lPeriodFrom, lPeriodTo, lYearFrom, lYearTo);
                                                    }

                                                    if (isINDENT) {
                                                        indents = desc.split("[\\|]");
                                                    }

                                                    if (isACCT) {

                                                        if (acctFrom == null) {
                                                            continue;
                                                        }
                                                        if (acctTo == null) {
                                                            acctTo = acctFrom;
                                                        }

                        %>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>>- <%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRangedWithPolType("BAL|ADD=0", acctFrom, acctTo, groupType, lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo), kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRangedWithPolTypeOnePeriod("BAL|ADD=0", acctFrom, acctTo, groupType, lPeriodTo, lYearFrom, lYearTo), kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRangedWithPolType("BAL|ADD=0", acctFrom, acctTo, groupType, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo), kali), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                            }

                                                                            if (isACTT) {

                                                                                if (acctFrom == null) {
                                                                                    continue;
                                                                                }
                                                                                if (acctTo == null) {
                                                                                    acctTo = acctFrom;
                                                                                }
                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo), kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRangedOnePeriod("BAL|ADD=0", acctFrom, acctTo, lPeriodTo, lYearFrom, lYearTo), kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo), kali), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                                                            }

                                                                            if (isAAAT) {

                                                                                if (acctFrom == null) {
                                                                                    continue;
                                                                                }
                                                                                if (acctTo == null) {
                                                                                    continue;
                                                                                }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(BDUtil.add(glr.getSummaryRangedWithPolType("BAL|ADD=0", acctFrom, acctFrom, groupType, lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo), glr.getSummaryRangedWithPolType("BAL|ADD=0", acctTo, acctTo, groupType, lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo)), kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(BDUtil.add(glr.getSummaryRangedWithPolTypeOnePeriod("BAL|ADD=0", acctFrom, acctFrom, groupType, lPeriodTo, lYearFrom, lYearTo), glr.getSummaryRangedWithPolTypeOnePeriod("BAL|ADD=0", acctTo, acctTo, groupType, lPeriodTo, lYearFrom, lYearTo)), kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(BDUtil.add(glr.getSummaryRangedWithPolType("BAL|ADD=0", acctFrom, acctFrom, groupType, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo), glr.getSummaryRangedWithPolType("BAL|ADD=0", acctTo, acctTo, groupType, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                            }

                                                                            if (isAATT) {

                                                                                if (acctFrom == null) {
                                                                                    continue;
                                                                                }
                                                                                if (acctTo == null) {
                                                                                    continue;
                                                                                }
                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(BDUtil.add(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctFrom, lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo), glr.getSummaryRanged("BAL|ADD=0", acctTo, acctTo, lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo)), kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(BDUtil.add(glr.getSummaryRangedOnePeriod("BAL|ADD=0", acctFrom, acctFrom, lPeriodTo, lYearFrom, lYearTo), glr.getSummaryRangedOnePeriod("BAL|ADD=0", acctTo, acctTo, lPeriodTo, lYearFrom, lYearTo)), kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(BDUtil.add(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctFrom, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo), glr.getSummaryRanged("BAL|ADD=0", acctTo, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo)), kali), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                                                            }

                                                                            if (isACCC) {

                                                                                if (acctFrom == null) {
                                                                                    continue;
                                                                                }
                                                                                if (acctTo == null) {
                                                                                    continue;
                                                                                }
                                                                                if (acctTo2 == null) {
                                                                                    continue;
                                                                                }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(BDUtil.add(glr.getSummaryRangedWithPolType("BAL|ADD=0", acctFrom, acctFrom, groupType, lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo), BDUtil.add(glr.getSummaryRangedWithPolType("BAL|ADD=0", acctTo, acctTo, groupType, lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo), glr.getSummaryRangedWithPolType("BAL|ADD=0", acctTo2, acctTo2, groupType, lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo))), kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(BDUtil.add(glr.getSummaryRangedWithPolTypeOnePeriod("BAL|ADD=0", acctFrom, acctFrom, groupType, lPeriodTo, lYearFrom, lYearTo), BDUtil.add(glr.getSummaryRangedWithPolTypeOnePeriod("BAL|ADD=0", acctTo, acctTo, groupType, lPeriodTo, lYearFrom, lYearTo), glr.getSummaryRangedWithPolTypeOnePeriod("BAL|ADD=0", acctTo2, acctTo2, groupType, lPeriodTo, lYearFrom, lYearTo))), kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(BDUtil.add(glr.getSummaryRangedWithPolType("BAL|ADD=0", acctFrom, acctFrom, groupType, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo), BDUtil.add(glr.getSummaryRangedWithPolType("BAL|ADD=0", acctTo, acctTo, groupType, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo), glr.getSummaryRangedWithPolType("BAL|ADD=0", acctTo2, acctTo2, groupType, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo))), kali), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                            }

                                                                            if (isATTT) {

                                                                                if (acctFrom == null) {
                                                                                    continue;
                                                                                }
                                                                                if (acctTo == null) {
                                                                                    continue;
                                                                                }
                                                                                if (acctTo2 == null) {
                                                                                    continue;
                                                                                }
                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(BDUtil.add(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctFrom, lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo), BDUtil.add(glr.getSummaryRanged("BAL|ADD=0", acctTo, acctTo, lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo), glr.getSummaryRanged("BAL|ADD=0", acctTo2, acctTo2, lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo))), kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(BDUtil.add(glr.getSummaryRangedOnePeriod("BAL|ADD=0", acctFrom, acctFrom, lPeriodTo, lYearFrom, lYearTo), BDUtil.add(glr.getSummaryRangedOnePeriod("BAL|ADD=0", acctTo, acctTo, lPeriodTo, lYearFrom, lYearTo), glr.getSummaryRangedOnePeriod("BAL|ADD=0", acctTo2, acctTo2, lPeriodTo, lYearFrom, lYearTo))), kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(BDUtil.add(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctFrom, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo), BDUtil.add(glr.getSummaryRanged("BAL|ADD=0", acctTo, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo), glr.getSummaryRanged("BAL|ADD=0", acctTo2, acctTo2, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo))), kali), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%

                                                                            }

                                                                            if (isDESC) {
                        %>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX(form.getLineNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%

                                                                            }

                                                                            if (isDESC1) {
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                            }

                                                                            if (isPAGE) {
                        %>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block font-size="6pt" text-align="left" >
                                    Print Stamp : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "hhmmssyyyyMMdd "))%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row <%=style%>>
                            <fo:table-cell />
                        </fo:table-row>

                        <%
                                                                            }

                                                                            if (isNL) {
                        %>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                            }

                                                                            if (isTOT) {

                                                                                if (acctFrom == null) {
                                                                                    continue;
                                                                                }
                                                                                if (acctTo == null) {
                                                                                    acctTo = acctFrom;
                                                                                }
                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo), kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRangedOnePeriod("BAL|ADD=0", acctFrom, acctTo, lPeriodTo, lYearFrom, lYearTo), kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo), kali), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                                                            }

                                                                            if (isATOT1) {

                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atot1, kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atot2, kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(atot3, kali), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                                                            }

                                                                            if (isATOT2) {

                                                                                BigDecimal atotA = akun65A;
                                                                                BigDecimal atotB = akun65B;
                                                                                BigDecimal atotC = akun65C;

                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>>- <%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotA, kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotB, kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotC, kali), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                                                            }

                                                                            if (isATOT3) {

                                                                                BigDecimal atotA = BDUtil.sub(akun8183A, akun811A);
                                                                                BigDecimal atotB = BDUtil.sub(akun8183B, akun811B);
                                                                                BigDecimal atotC = BDUtil.sub(akun8183C, akun811C);

                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>>- <%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotA, kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotB, kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotC, kali), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                                                            }

                                                                            if (isATOT4) {

                                                                                BigDecimal atotA = BDUtil.add(BDUtil.add(atot1, atot4), atot7);
                                                                                BigDecimal atotB = BDUtil.add(BDUtil.add(atot2, atot5), atot8);
                                                                                BigDecimal atotC = BDUtil.add(BDUtil.add(atot3, atot6), atot9);

                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotA, kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotB, kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotC, kali), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                                                            }

                                                                            if (isATOT5) {

                                                                                BigDecimal atotA = BDUtil.add(akun69A, BDUtil.add(akun8911A, akun8921A));
                                                                                atotA = BDUtil.sub(atotA, atot10);
                                                                                BigDecimal atotB = BDUtil.add(akun69B, BDUtil.add(akun8911B, akun8921B));
                                                                                atotB = BDUtil.sub(atotB, atot11);
                                                                                BigDecimal atotC = BDUtil.add(akun69C, BDUtil.add(akun8911C, akun8921C));
                                                                                atotC = BDUtil.sub(atotC, atot12);

                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>>- <%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotA, kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotB, kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotC, kali), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                            }

                                                                            if (isATOT6) {

                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atot10, kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atot11, kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(atot12, kali), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                                                            }

                                                                            if (isATOT7) {

                                                                                BigDecimal atotA = BDUtil.add(atot1, BDUtil.add(atot4, atot7));
                                                                                atotA = BDUtil.add(atotA, atot10);
                                                                                BigDecimal atotB = BDUtil.add(atot2, BDUtil.add(atot5, atot8));
                                                                                atotB = BDUtil.add(atotB, atot11);
                                                                                BigDecimal atotC = BDUtil.add(atot3, BDUtil.add(atot6, atot9));
                                                                                atotC = BDUtil.add(atotC, atot12);

                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotA, kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotB, kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotC, kali), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                                                            }

                                                                            if (isATOT8) {

                                                                                BigDecimal atotA = BDUtil.add(atot1, BDUtil.add(atot4, atot7));
                                                                                atotA = BDUtil.add(atotA, atot10);
                                                                                atotA = BDUtil.add(atotA, atot13);
                                                                                BigDecimal atotB = BDUtil.add(atot2, BDUtil.add(atot5, atot8));
                                                                                atotB = BDUtil.add(atotB, atot11);
                                                                                atotB = BDUtil.add(atotB, atot14);
                                                                                BigDecimal atotC = BDUtil.add(atot3, BDUtil.add(atot6, atot9));
                                                                                atotC = BDUtil.add(atotC, atot12);
                                                                                atotC = BDUtil.add(atotC, atot15);

                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotA, kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotB, kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(atotC, kali), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                                    }

                                                }
                        %>

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
                                            message="<%=DateUtil.getDateStr(new Date(), "hhmmssyyyyMMdd")%>" orientation="0">
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
                                                <fo:block text-align="center">JAKARTA, <%=DateUtil.getDateStr(new Date(), "d ^^ yyyy")%></fo:block>
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
        <% }%>
    </fo:page-sequence>

    <fo:page-sequence master-reference="first">

        <fo:static-content flow-name="xsl-region-after">
            <fo:block text-align="end"
                      font-size="8pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>

        <%
                    {

                        HashMap refmap = form.getRptRef();

                        String ftGroup = (String) refmap.get("FT");

                        DTOList ftl = FlexTableManager.getInstance().getFlexTable("BO1");

                        ArrayList cmap = new ArrayList();

                        cmap.add(new Integer(5));
                        cmap.add(new Integer(90));
                        cmap.add(new Integer(35));
                        cmap.add(new Integer(35));
                        cmap.add(new Integer(35));

                        ArrayList cw = FOPUtil.computeColumnWidth(cmap, 20, 2, "cm");

        %>

        <fo:flow flow-name="xsl-region-body">

            <%
                                    for (int i = 0; i < ftl.size(); i++) {
                                        FlexTableView ft = (FlexTableView) ftl.get(i);

                                        String opCode = ft.getStReference2();
                                        String desc = ft.getStReference3();

                                        boolean isTITLE = "TITLE".equalsIgnoreCase(opCode);

                                        if (isTITLE) {
            %>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center"><%=desc%></fo:block>
            <%
                                        }

                                    }
            %>

            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" ><%=JSPUtil.printX(form.getPeriodTitleDescription())%></fo:block>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" ><%=JSPUtil.printX(form.getCostCenter().getStDescription())%></fo:block>
            <fo:block space-after.optimum="14pt"/>

            <fo:block font-family="Helvetica" font-size="6pt" >
                <fo:table table-layout="fixed">
                    <%
                                            for (int i = 0; i < cw.size(); i++) {
                                                String cwx = (String) cw.get(i);
                    %>
                    <fo:table-column column-width="<%=cwx%>"/>
                    <%
                                            }
                    %>

                    <fo:table-header>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block font-size="8pt" text-align="end" font-style="italic">(Dalam Rupiah)</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">NO</fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" display-align="center"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">KETERANGAN</fo:block></fo:table-cell>
                            <% if (form.getLPeriodTo() != 1) {%>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" ><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">SALDO <%=JSPUtil.printX(form.getPeriodBeforeTitleDescription().toUpperCase())%></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" ><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">SALDO PER 31 JANUARI <%=JSPUtil.printX(form.getYearFrom())%></fo:block></fo:table-cell>
                            <% }%>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" ><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">MUTASI BLN <%=JSPUtil.printX(form.getMonthTitleDescription().toUpperCase())%></fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt" border-width="<%=bw%>" border-left-style="solid" border-right-style="solid"><fo:block line-height="10mm" text-align="center" font-size="10pt" font-weight="bold">SALDO <%=JSPUtil.printX(form.getPeriodTitleDescription().toUpperCase())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-header>
                    <fo:table-body>

                        <%
                                                String[] indents = null;

                                                for (int i = 0; i < ftl.size(); i++) {
                                                    FlexTableView ft = (FlexTableView) ftl.get(i);

                                                    String style = ft.getStReference1();
                                                    String opCode = ft.getStReference2();
                                                    String desc = ft.getStReference3();
                                                    String acctFrom = ft.getStReference4();
                                                    String acctTo = ft.getStReference5();
                                                    int iindent = ft.getStReference6() == null ? 0 : Integer.parseInt(ft.getStReference6());
                                                    String groupType = ft.getStReferenceID1();
                                                    String flags = ft.getStReference7();
                                                    BigDecimal kali = ft.getDbReference1();

                                                    String indent = (indents != null && indents.length > iindent) ? indents[iindent] : null;

                                                    if (style == null) {
                                                        style = "";
                                                    }

                                                    String[] styles = style.split("[\\|]");

                                                    style = styles[0];
                                                    String style1 = styles.length > 1 ? styles[1] : "";

                                                    if (indent != null) {
                                                        style += " start-indent=\"" + indent + "\"";
                                                    }

                                                    if (opCode == null) {
                                                        continue;
                                                    }

                                                    boolean isINDENT = "INDENT".equalsIgnoreCase(opCode);
                                                    boolean isDESCRIP = "DESCRIP".equalsIgnoreCase(opCode);
                                                    boolean isDESC1 = "DESC1".equalsIgnoreCase(opCode);
                                                    boolean isACCT = "ACCT".equalsIgnoreCase(opCode);
                                                    boolean isACTT = "ACTT".equalsIgnoreCase(opCode);
                                                    boolean isNL = "NL".equalsIgnoreCase(opCode);
                                                    boolean isPAGE = "PAGE".equalsIgnoreCase(opCode);
                                                    boolean isTOT = "TOT".equalsIgnoreCase(opCode);

                                                    if (isINDENT) {
                                                        indents = desc.split("[\\|]");
                                                    }

                                                    if (isACCT) {

                                                        if (acctFrom == null) {
                                                            continue;
                                                        }
                                                        if (acctTo == null) {
                                                            acctTo = acctFrom;
                                                        }

                        %>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>>- <%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo), kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRangedOnePeriod("BAL|ADD=0", acctFrom, acctTo, lPeriodTo, lYearFrom, lYearTo), kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo), kali), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                            }

                                                                            if (isACTT) {

                                                                                if (acctFrom == null) {
                                                                                    continue;
                                                                                }
                                                                                if (acctTo == null) {
                                                                                    acctTo = acctFrom;
                                                                                }
                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo), kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRangedOnePeriod("BAL|ADD=0", acctFrom, acctTo, lPeriodTo, lYearFrom, lYearTo), kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo), kali), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%

                                                                            }

                                                                            if (isDESCRIP) {
                        %>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX(form.getLineNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%

                                                                            }

                                                                            if (isDESC1) {
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                            }

                                                                            if (isPAGE) {
                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block font-size="6pt" text-align="left" >
                                    Print Stamp : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "hhmmssyyyyMMdd "))%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row <%=style%>>
                            <fo:table-cell />
                        </fo:table-row>

                        <%
                                                                            }

                                                                            if (isNL) {
                        %>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                            }

                                                                            if (isTOT) {

                                                                                if (acctFrom == null) {
                                                                                    continue;
                                                                                }
                                                                                if (acctTo == null) {
                                                                                    acctTo = acctFrom;
                                                                                }
                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="center" <%=style%>><%=JSPUtil.printX("")%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="start" <%=style%>><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo - 1, lYearFrom, lYearTo), kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRangedOnePeriod("BAL|ADD=0", acctFrom, acctTo, lPeriodTo, lYearFrom, lYearTo), kali), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  <%=style%>><%=JSPUtil.printX(BDUtil.mul(glr.getSummaryRanged("BAL|ADD=0", acctFrom, acctTo, lPeriodFrom, lPeriodTo, lYearFrom, lYearTo), kali), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                                    }

                                                }
                        %>

                    </fo:table-body>
                </fo:table>

                <fo:block font-size="6pt"
                          font-family="sans-serif"
                          line-height="10pt"
                          space-after.optimum="5pt"
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
                                            message="<%=DateUtil.getDateStr(new Date(), "hhmmssyyyyMMdd")%>" orientation="0">
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
                                                <fo:block text-align="center">JAKARTA, <%=DateUtil.getDateStr(new Date(), "d ^^ yyyy")%></fo:block>
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
        <% }%>
    </fo:page-sequence>
</fo:root>