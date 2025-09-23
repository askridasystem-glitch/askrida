<%@ page import="com.webfin.gl.util.GLUtil,
         com.webfin.gl.ejb.GLReportEngine2,
         com.crux.util.JSPUtil,
         com.webfin.gl.report2.form.FinReportForm,
         com.crux.util.DateUtil,
         java.util.Date,
         com.crux.util.BDUtil,
         com.crux.util.Tools,
         com.crux.util.*,
         com.crux.util.fop.FOPUtil,
         com.crux.web.controller.SessionManager,
         com.crux.common.parameter.Parameter,
         java.math.BigDecimal"%><?xml version="1.0" ?>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines the layout master -->
    <fo:layout-master-set>
        <fo:simple-page-master master-name="first"
                               page-height="31cm"
                               page-width="21cm"
                               margin-top="2.5cm"
                               margin-bottom="0.5cm"
                               margin-left="1.5cm"
                               margin-right="2cm">
            <fo:region-body margin-top="3cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="1.5cm"/>
            <fo:region-after extent="1.5cm"/>
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

                    BigDecimal kewajiban2 = BDUtil.add(jumlah_kewajiban, subordinasi_ekuitas);

                    BigDecimal selisih = BDUtil.sub(aktiva, BDUtil.negate(kewajiban2));//3
                    BigDecimal selisih_nilai = null;

                    if (BDUtil.lesserThanZero(selisih)) {
                        selisih_nilai = BDUtil.mul(selisih, new BigDecimal(-1));
                    } else {
                        selisih_nilai = selisih;
                    }

                    BigDecimal kewajiban = null;
                    BigDecimal utang_lain2 = null;
                    BigDecimal jumlah_kewajiban2 = null;
                    BigDecimal kewajiban_nilai = null;

                    if (Tools.compare(selisih_nilai, new BigDecimal(50)) < 0) {
                        if (BDUtil.lesserThanZero(selisih)) {
                            utang_lain2 = BDUtil.add(utang_lain, selisih_nilai);
                            jumlah_kewajiban2 = BDUtil.add(jumlah_kewajiban, selisih_nilai);
                            kewajiban_nilai = BDUtil.add(kewajiban2, selisih_nilai);
                        } else {
                            utang_lain2 = BDUtil.sub(utang_lain, selisih_nilai);
                            jumlah_kewajiban2 = BDUtil.sub(jumlah_kewajiban, selisih_nilai);
                            kewajiban_nilai = BDUtil.sub(kewajiban2, selisih_nilai);
                        }
                    } else if (Tools.compare(selisih_nilai, new BigDecimal(50)) > 0) {
                        utang_lain2 = utang_lain;
                        jumlah_kewajiban2 = jumlah_kewajiban;
                        kewajiban_nilai = kewajiban2;
                    }

                    if (BDUtil.lesserThanZero(kewajiban_nilai)) {
                        kewajiban = BDUtil.negate(kewajiban_nilai);
                    } else {
                        kewajiban = kewajiban_nilai;
                    }

                    //BigDecimal rekeningKantor   = form.updateRekeningKantor(BDUtil.negate(balance),"Y");

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
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" >NERACA KONVENSIONAL</fo:block>
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
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>
                        <fo:table-row  >
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" number-columns-spanned="2" ><fo:block line-height="5mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">AKTIVA</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" number-columns-spanned="2" border-left-style="solid"><fo:block line-height="5mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">KEWAJIBAN DAN EKUITAS</fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell   padding="2pt"><fo:block font-style="italic">Deposito</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(deposito, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block font-style="italic">Utang Klaim</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" text-align="end"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(utang_klaim), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic">Sertifikat Deposito</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(ser_depodito, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block font-style="italic">Estimasi Klaim Retensi Sendiri</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(estimasi_klaim), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic">Surat Berharga</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(surat_bhrga, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block font-style="italic">Premi yg belum merupakan pendapatan</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(premi_belum_pndptn), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic">Penyertaan</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(penyertaan, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block font-style="italic">Utang Reasuransi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(utang_reas), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic">Properti</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(properti, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block font-style="italic">Utang Komisi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(utang_komisi), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic">Properti pinjaman hipotek</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(properti_pinjaman, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block font-style="italic">Utang Pajak</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(utang_pajak), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic">Investasi Lain</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(investasi, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block font-style="italic">Biaya yg masih harus dibayar</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(biaya_dibayar), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell  padding="2pt"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block font-style="italic">Utang Sewa Guna Usaha</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(utang_sewa), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell  padding="2pt"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block font-style="italic">Utang Lain-lain</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(BDUtil.negate(utang_lain2), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row height="5mm">
                            <fo:table-cell  padding="2pt"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block>JUMLAH INVESTASI</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(jumlah_aktiva, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>JUMLAH KEWAJIBAN</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(jumlah_kewajiban2), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row height="5mm">
                            <fo:table-cell  padding="2pt"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Kas dan Bank</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(kas_bank, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block font-style="italic">Utang Jangka Panjang</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(utang_subordinasi), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Piutang Premi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(piut_premi, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Piutang Reasuransi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(BDUtil.sub(piut_reas, aset_reas), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Aset Reasuransi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(aset_reas, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block font-weight="bold">EKUITAS</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Piutang Hasil Investasi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(piut_hsl_inves, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid">
                                <fo:block font-style="italic">Modal Dasar 3000 Lbr @ RP.10.000.000</fo:block>
                                <fo:block font-style="italic" start-indent="3mm">Modal Ditempatkan dan Disetor</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(modal_disetor), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Piutang Lain</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(piut_lain, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block font-style="italic">Titipan Modal Disetor</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(titipan_disetor), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Biaya Dibayar Dimuka</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(biaya_byr_muka, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block font-style="italic">Agio Saham</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(agio_saham), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Aktiva Tetap</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(aktiva_tetap, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block font-style="italic">Cadangan umum</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(cadangan_umum), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Aktiva Lain-lain</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(aktiva_lain, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid">
                                <fo:block font-style="italic">Keuntungan (Kerugian) blm direal. </fo:block>
                                <fo:block font-style="italic" start-indent="3mm">atas Penurunan Nilai Wajib Efek Siap Jual</fo:block>
                            </fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(kerugian_penurunan), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Aktiva Pajak Tangguhan</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(aktiva_pajak, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block font-style="italic">Cadangan Khusus</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(cadangan_khusus), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <% if (form.getBranch() != null) {%>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Rekening Antar Kantor</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(rek_kantor, 0)%></fo:block></fo:table-cell>
                            <% } else if (form.getBranch()
                                     == null) {
                                 if (Tools.isEqual(rek_kantor, BDUtil.zero)) {%>
                            <fo:table-cell  padding="2pt"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Rekening Antar Kantor</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(rek_kantor, 0)%></fo:block></fo:table-cell>
                            <% }
                             }
                            %>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block font-style="italic">Laba (Rugi) ditahan</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(laba_rugi_ditahan), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block font-style="italic">Saldo Laba (Rugi) Tahun Lalu</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(laba_rugi_tahun_lalu), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block font-style="italic">Saldo Laba (Rugi) Tahun Berjalan</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(laba_rugi_berjalan), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block font-weight="bold" text-align="center">Jumlah Ekuitas</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" ><fo:inline text-decoration="overline"><%=JSPUtil.printX(BDUtil.negate(jumlah_ekuitas), 0)%></fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-top-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" font-weight="bold"  >JUMLAH AKTIVA</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="10mm"  font-weight="bold" background-color="#C0C0C0"  ><%=JSPUtil.printX(aktiva, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid" border-top-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0"  font-weight="bold">JUMLAH KEWAJIBAN &amp; EKUITAS</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="10mm" font-weight="bold" background-color="#C0C0C0"  ><%=JSPUtil.printX(kewajiban, 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>

                <fo:block font-size="6pt"
                          font-family="sans-serif"
                          line-height="10pt"
                          text-align="left" >
                    Print Stamp : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "hhmmssyyyyMMdd "))%>
                </fo:block>


                <%if (form.getBranch() != null) {%>
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
                                            message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(aktiva, 0)%>" orientation="0">
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
                                        <fo:table-row>
                                            <fo:table-cell>
                                                <fo:block text-align="center">S.E. &amp; O.</fo:block>
                                                <fo:block text-align="center"><%=JSPUtil.printX(form.getCostCenter().getStDescription().toUpperCase())%>, <%=DateUtil.getDateStr(form.getDtPrintDate(), "d ^^ yyyy")%></fo:block>
                                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell></fo:table-cell>
                                        </fo:table-row>

                                        <fo:table-row height="15mm">
                                        </fo:table-row>

                                        <fo:table-row>
                                            <%if (!form.getBranch().equalsIgnoreCase("00")) {%>
                                            <fo:table-cell>
                                                <fo:block text-align="center" text-decoration="underline"><%= Parameter.readString("BRANCH_SIGN_" + form.getBranch())%></fo:block>
                                                <fo:block text-align="center"><%=SessionManager.getInstance().getUser(Parameter.readString("BRANCH_" + form.getBranch())).getStJobPosition()%></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell></fo:table-cell>
                                            <%} else {%>
                                            <fo:table-cell>
                                                <fo:block text-align="center" text-decoration="underline"><%=JSPUtil.printX(form.getPerson1Name())%></fo:block>
                                                <fo:block text-align="center"><%=JSPUtil.printX(form.getPerson1Title())%></fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell>
                                                <fo:block text-align="center" text-decoration="underline"><%=JSPUtil.printX(form.getPerson2Name())%></fo:block>
                                                <fo:block text-align="center"><%=JSPUtil.printX(form.getPerson2Title())%></fo:block>
                                            </fo:table-cell>
                                            <%}%>
                                        </fo:table-row>

                                    </fo:table-body>
                                </fo:table>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>

                <% } else {%>

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
                                            message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(aktiva, 0)%>" orientation="0">
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
                                        <fo:table-row>
                                            <fo:table-cell number-columns-spanned="2" >
                                                <fo:block text-align="center">S.E. &amp; O.</fo:block>
                                                <fo:block text-align="center">JAKARTA, <%=DateUtil.getDateStr(form.getDtPrintDate(), "d ^^ yyyy")%></fo:block>
                                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                                <fo:block text-align="center">DIREKSI</fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>

                                        <fo:table-row height="15mm">
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
                <%}%>

            </fo:block>            
        </fo:flow>
    </fo:page-sequence>
</fo:root>