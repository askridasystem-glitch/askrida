<%@ page import="com.webfin.gl.util.GLUtil,
com.webfin.gl.ejb.GLReportEngine2,
com.crux.util.JSPUtil,
com.webfin.gl.report2.form.FinReportForm,
com.crux.util.DateUtil,
java.util.Date,
com.crux.util.BDUtil,
com.crux.util.*,
com.crux.util.fop.FOPUtil,
java.math.BigDecimal"%><?xml version="1.0" ?>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines the layout master -->
    <fo:layout-master-set>
        <fo:simple-page-master master-name="first"
                               page-height="31cm"
                               page-width="21cm"
                               margin-top="1cm"
                               margin-bottom="2cm"
                               margin-left="2.5cm"
                               margin-right="2.5cm">
            <fo:region-body margin-top="1cm" margin-bottom="0.5cm"/>
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
        long lYearTo = form.getLYearTo();
        
        glr.setBranch(form.getBranch());
        glr.setStFlag(form.getStFlag());
        
        BigDecimal deposito             = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0","1111","1112",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal ser_depodito         = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0","1113","1113",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal surat_bhrga          = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0","112","114",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal penyertaan           = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0","115","115",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal properti             = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0","116","116",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal properti_pinjaman    = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0","117","117",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal investasi            = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0","119","119",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal jumlah_aktiva        = BDUtil.add(deposito, ser_depodito);
        jumlah_aktiva = BDUtil.add(jumlah_aktiva, surat_bhrga);
        jumlah_aktiva = BDUtil.add(jumlah_aktiva, penyertaan);
        jumlah_aktiva = BDUtil.add(jumlah_aktiva, properti);
        jumlah_aktiva = BDUtil.add(jumlah_aktiva, properti_pinjaman);
        jumlah_aktiva = BDUtil.add(jumlah_aktiva, investasi);
        
        BigDecimal utang_klaim          = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=1","33","33",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal estimasi_klaim       = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=1","32","32",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal premi_belum_pndptn   = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=1","34","34",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal utang_reas           = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=1","42","42",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal utang_komisi         = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=1","43","43",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal utang_pajak          = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=1","44","44",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal biaya_dibayar        = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=1","46","46",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal utang_sewa           = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=1","47","47",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal utang_lain           = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=1","48","48",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal jumlah_kewajiban     = BDUtil.add(utang_klaim, estimasi_klaim);
        jumlah_kewajiban     = BDUtil.add(jumlah_kewajiban, premi_belum_pndptn);
        jumlah_kewajiban     = BDUtil.add(jumlah_kewajiban, utang_reas);
        jumlah_kewajiban     = BDUtil.add(jumlah_kewajiban, utang_komisi);
        jumlah_kewajiban     = BDUtil.add(jumlah_kewajiban, utang_pajak);
        jumlah_kewajiban     = BDUtil.add(jumlah_kewajiban, biaya_dibayar);
        jumlah_kewajiban     = BDUtil.add(jumlah_kewajiban, utang_sewa);
        jumlah_kewajiban     = BDUtil.add(jumlah_kewajiban, utang_lain);
        
        BigDecimal kas_bank         = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0","120","123",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal piut_premi       = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0","13","13",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal piut_reas        = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0","14","14",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal piut_hsl_inves   = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0","15","15",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal piut_lain        = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0","16","16",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        piut_lain                   = BDUtil.add(piut_lain, BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0","124","124",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo)));
        BigDecimal biaya_byr_muka   = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0","17","17",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal aktiva_tetap     = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0","18","18",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal aktiva_lain      = BDUtil.roundUp(glr.getSummaryRangedExcluded("BAL|ADD=0","19","19","19144",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal aktiva_pajak     = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0","19144","19144",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal jumlah_aktiva2   = BDUtil.add(kas_bank, piut_premi);
        jumlah_aktiva2     = BDUtil.add(jumlah_aktiva2, piut_reas);
        jumlah_aktiva2     = BDUtil.add(jumlah_aktiva2, piut_hsl_inves);
        jumlah_aktiva2     = BDUtil.add(jumlah_aktiva2, piut_lain);
        jumlah_aktiva2     = BDUtil.add(jumlah_aktiva2, biaya_byr_muka);
        jumlah_aktiva2     = BDUtil.add(jumlah_aktiva2, aktiva_tetap);
        jumlah_aktiva2     = BDUtil.add(jumlah_aktiva2, aktiva_lain);
        jumlah_aktiva2     = BDUtil.add(jumlah_aktiva2, aktiva_pajak);
        
        BigDecimal utang_subordinasi    = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2","49","49",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal modal_dasar          = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2","5111","5111",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal modal_disetor        = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2","512","512",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal titipan_disetor      = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2","5113","5113",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal agio_saham           = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2","513","513",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal cadangan_umum        = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2","51620","51621",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal kerugian_penurunan   = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2","514","514",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal cadangan_khusus      = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2","51622","51622",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal laba_rugi_ditahan    = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2","51623","51623",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal selisih_kembali      = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2","515","515",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal laba_rugi_tahun_lalu = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2","51610","51610",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal laba_rugi_berjalan   = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=2","51611","51611",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal jumlah_ekuitas       = BDUtil.add(utang_subordinasi, modal_dasar);
        jumlah_ekuitas     = BDUtil.add(jumlah_ekuitas, modal_disetor);
        jumlah_ekuitas     = BDUtil.add(jumlah_ekuitas, titipan_disetor);
        jumlah_ekuitas     = BDUtil.add(jumlah_ekuitas, cadangan_umum);
        jumlah_ekuitas     = BDUtil.add(jumlah_ekuitas, kerugian_penurunan);
        jumlah_ekuitas     = BDUtil.add(jumlah_ekuitas, cadangan_khusus);
        jumlah_ekuitas     = BDUtil.add(jumlah_ekuitas, laba_rugi_ditahan);
        jumlah_ekuitas     = BDUtil.add(jumlah_ekuitas, selisih_kembali);
        jumlah_ekuitas     = BDUtil.add(jumlah_ekuitas, laba_rugi_tahun_lalu);
        jumlah_ekuitas     = BDUtil.add(jumlah_ekuitas, laba_rugi_berjalan);
        
        %>       
        
        <fo:flow flow-name="xsl-region-body">
            
            <%
            String bw = "0.5pt";
            %>
            
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="20mm">NERACA</fo:block>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" ><%=JSPUtil.printX(form.getPeriodTitleDescription())%></fo:block>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" ><%=JSPUtil.printX(form.getBranchDesc())%></fo:block>
            <fo:block space-after.optimum="14pt"/>
            
            <fo:block font-family="Helvetica" font-size="8pt" display-align="center" border-width="0.1pt">
                <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>">
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>
                        <fo:table-row  >
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" number-columns-spanned="2" ><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">AKTIVA</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" number-columns-spanned="2" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">KEWAJIBAN DAN EKUITAS</fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell   padding="2pt"><fo:block>DEPOSITO</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(deposito,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block>UTANG KLAIM</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" text-align="end"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(utang_klaim),0)%></fo:block></fo:table-cell>
                        </fo:table-row>                        
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block>SERTIFIKAT DEPOSITO</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(ser_depodito,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>ESTIMASI KLAIM RETENSI SENDIRI   </fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(estimasi_klaim),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block>SURAT BERHARGA REKSADANA</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(surat_bhrga,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>PREMI YG BELUM MERUPAKAN PENDAPATAN   </fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(premi_belum_pndptn),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block>PENYERTAAN</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(penyertaan,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>UTANG REASURANSI   </fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(utang_reas),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block>PROPERTI</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(properti,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>UTANG KOMISI   </fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(utang_komisi),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block>PROPERTI PINJAMAN HIPOTEK</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(properti_pinjaman,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>UTANG PAJAK   </fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(utang_pajak),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block>INVESTASI LAIN</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(investasi,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>BIAYA YG MASIH HARUS DIBAYAR</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(biaya_dibayar),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>UTANG SEWA GUNA USAHA</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(utang_sewa),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>UTANG LAIN-LAIN</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(BDUtil.negate(utang_lain),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row height="5mm">
                            <fo:table-cell  padding="2pt"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block>JUMLAH INVESTASI</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(jumlah_aktiva,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>JUMLAH KEWAJIBAN</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(jumlah_kewajiban),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row height="5mm">
                            <fo:table-cell  padding="2pt"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Kas dan Bank</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(kas_bank,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>UTANG SUBORDINASI</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(utang_subordinasi),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Piutang Premi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(piut_premi,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Piutang Reasuransi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(piut_reas,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block font-weight="bold">EKUITAS</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Piutang Hasil Investasi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(piut_hsl_inves,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>MODAL DASAR 3000 LBR @ RP.10.000.000</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(modal_dasar),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Piutang Lain</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(piut_lain,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>MODAL DITEMPATKAN DAN DISETOR</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(modal_disetor),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Biaya Dibayar Dimuka</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(biaya_byr_muka,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>TITIPAN MODAL DISETOR</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(titipan_disetor),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Aktiva Tetap</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(aktiva_tetap,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>AGIO SAHAM</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(agio_saham),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Aktiva Lain-lain</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(aktiva_lain,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>CADANGAN UMUM</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(cadangan_umum),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block font-style="italic" start-indent="3mm">Aktiva Pajak Tangguhan</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ><%=JSPUtil.printX(aktiva_pajak,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>Kerugian (Keuntungan) blm direal. Atas Penurunan Nilai Wajib Efek Siap Jual</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(kerugian_penurunan),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>CADANGAN KHUSUS</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(cadangan_khusus),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>Laba (Rugi) ditahan</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(laba_rugi_ditahan),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>Selisih Penilaian Kembali</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(selisih_kembali),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>SALDO LABA(RUGI) TAHUN LALU</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(laba_rugi_tahun_lalu),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block>SALDO LABA(RUGI) TAHUN BERJALAN</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" > <%=JSPUtil.printX(BDUtil.negate(laba_rugi_berjalan),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell  padding="2pt"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" ></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block font-weight="bold" text-align="center">Jumlah Ekuitas</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid"><fo:block text-align="end" ><fo:inline text-decoration="overline"><%=JSPUtil.printX(BDUtil.negate(jumlah_ekuitas),0)%></fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-top-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" font-weight="bold"  >JUMLAH AKTIVA</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="10mm"  font-weight="bold" background-color="#C0C0C0"  ><%=JSPUtil.printX(BDUtil.add(jumlah_aktiva, jumlah_aktiva2),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid" border-top-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0"  font-weight="bold">JUMLAH KEWAJIBAN &amp; EKUITAS</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>"  padding="2pt" border-left-style="solid" border-top-style="solid"><fo:block text-align="end" line-height="10mm" font-weight="bold" background-color="#C0C0C0"  ><%=JSPUtil.printX(BDUtil.negate(BDUtil.add(jumlah_kewajiban, jumlah_ekuitas)),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
                
                <fo:block font-size="6pt"
                          font-family="sans-serif"
                          line-height="10pt"
                          space-after.optimum="15pt"
                          text-align="left" >
                    Print Stamp : <%=JSPUtil.print(DateUtil.getDateStr(new Date(),"hhmmssyyyyMMdd "))%>                    
                </fo:block>
                
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell/>
                            <fo:table-cell>
                                
                                <fo:table table-layout="fixed">
                                    <fo:table-column column-width="50mm"/>
                                    <fo:table-column column-width="50mm"/>
                                    <fo:table-body>
                                        <fo:table-row height="10mm"/>
                                        <fo:table-row>
                                            <fo:table-cell number-columns-spanned="2" >
                                                <fo:block text-align="center">S.E. &amp; O.</fo:block>
                                                <fo:block text-align="center">JAKARTA, <%=DateUtil.getDateStr(new Date(),"d ^^ yyyy")%></fo:block>
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

