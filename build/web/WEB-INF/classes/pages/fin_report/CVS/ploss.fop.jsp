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
                               margin-bottom="2cm"
                               margin-left="2.5cm"
                               margin-right="2.5cm">
            <fo:region-body margin-top="1cm" margin-bottom="1.5cm"/>
            <fo:region-before extent="3cm"/>
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
        
        BigDecimal kali = new BigDecimal(-1);
        
        BigDecimal premi_bruto              = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0","61","61",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal premi_reas               = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0","63","63",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal premi_kenaikan           = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=0","64","64",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal jumlahPendapatanPremi    = BDUtil.add(premi_bruto, BDUtil.add(premi_reas, premi_kenaikan));
        
        BigDecimal klaim_bruto              = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU","71","71",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal klaim_reas               = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU","72","72",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal kenaikan_klaim           = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=1,BBU","75","75",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal jumlahBebanKlaim         = BDUtil.add(klaim_bruto, klaim_reas);
        jumlahBebanKlaim = BDUtil.add(jumlahBebanKlaim, kenaikan_klaim);
        
        BigDecimal beban_komisi_netto       = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU","77","77",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal beban_und_lain           = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BBU","79","79",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal jumlahBebanUnderwriting  = BDUtil.add(jumlahBebanKlaim, beban_komisi_netto);
        jumlahBebanUnderwriting = BDUtil.add(jumlahBebanUnderwriting, beban_und_lain);
        
        BigDecimal investasi                = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=HI","65","65",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal beban_usaha              = BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=BU","81","83",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        
        BigDecimal pajakPenghasilan         = BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4","90","90",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal pajakPenghasilanTangguhan= BDUtil.roundUp(glr.getSummaryRanged("BAL|ADD=4","91","91",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo));
        BigDecimal pajakPenghasilanTotal = BDUtil.add(pajakPenghasilan, pajakPenghasilanTangguhan);
        
        BigDecimal hasilUnderWriting        = BDUtil.add(jumlahPendapatanPremi, jumlahBebanUnderwriting);
        BigDecimal laba_usaha               = BDUtil.add(hasilUnderWriting, investasi);
        laba_usaha = BDUtil.add(laba_usaha, beban_usaha);
        BigDecimal penghasilanBeban =  BDUtil.add(BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3","69","69",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo)), BDUtil.roundUp(glr.getSummaryRanged("BAL|NEG|ADD=3","89","89",lPeriodFrom,lPeriodTo,lYearFrom,lYearTo)));
        
        BigDecimal laba_bersih              = BDUtil.sub(BDUtil.add(laba_usaha, penghasilanBeban), pajakPenghasilanTotal);
        BigDecimal laba_bersih_final        = form.updateLabaBersih(BDUtil.roundUp(laba_bersih),form.getStFlag());
        %>
        
        <fo:flow flow-name="xsl-region-body">
            
            <%
            String bw = "0.5pt";
            %>
            
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="20mm">LABA RUGI</fo:block>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" ><%=JSPUtil.printX(form.getPeriodTitleDescription())%></fo:block>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="center" ><%=JSPUtil.printX(form.getBranchDesc())%></fo:block>
            <fo:block space-after.optimum="14pt"/>
            
            
            <fo:block font-family="Helvetica" font-size="10pt" display-align="center" border-width="0.1pt">
                <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>">
                    <fo:table-column column-width="130mm"/>
                    <fo:table-column column-width="30mm"/>
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
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(BDUtil.negate(premi_bruto),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Premi Reasuransi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(premi_reas,0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Kenaikan (Penurunan) Premi yang belum merupakan pendapatan</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(premi_kenaikan,0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row >
                            <fo:table-cell  border-width="<%=bw%>" padding="2pt" border-top-style="solid" border-bottom-style="solid"><fo:block font-weight="bold" font-style="italic">Jumlah Pendapatan Premi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(BDUtil.negate(jumlahPendapatanPremi),0)%></fo:block></fo:table-cell>
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
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(klaim_bruto,0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Klaim Reasuransi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(BDUtil.negate(klaim_reas),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Kenaikan (penurunan) estimasi klaim retensi sendiri</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(kenaikan_klaim,0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row >
                            <fo:table-cell  border-width="<%=bw%>" padding="2pt" border-top-style="solid" border-bottom-style="solid"><fo:block font-weight="bold" font-style="italic">Jumlah Beban Klaim</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(jumlahBebanKlaim,0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row height="3mm">
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Beban Komisi Netto</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(beban_komisi_netto,0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Beban Underwriting Lain Netto</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(beban_und_lain,0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row >
                            <fo:table-cell  border-width="<%=bw%>" padding="2pt" border-top-style="solid" border-bottom-style="solid"><fo:block font-weight="bold" font-style="italic">Jumlah Beban Underwriting</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(jumlahBebanUnderwriting,0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row height="3mm">
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Hasil Underwriting</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(BDUtil.negate(hasilUnderWriting),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Hasil Investasi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(BDUtil.negate(investasi),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Beban Usaha</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(beban_usaha,0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Laba Usaha</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(BDUtil.negate(laba_usaha),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Penghasilan (Beban) Lain-lain</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(BDUtil.negate(penghasilanBeban),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Laba Sebelum Pajak</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(BDUtil.negate(BDUtil.add(laba_usaha, penghasilanBeban)),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Pajak Penghasilan</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(BDUtil.negate(pajakPenghasilan),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block start-indent="5mm" >Pajak Penghasilan (Beban) Tangguhan</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end"  ><%=JSPUtil.printX(BDUtil.negate(pajakPenghasilanTangguhan),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"  ><fo:block line-height="10mm"  background-color="#C0C0C0"  text-align="center" font-size="10pt" font-weight="bold">LABA BERSIH</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt" border-left-style="solid"><fo:block line-height="10mm"  background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(BDUtil.negate(laba_bersih),0)%></fo:block></fo:table-cell>
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
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell/>
                            <fo:table-cell>
                                
                                <fo:table table-layout="fixed">
                                    <fo:table-column column-width="70mm"/>
                                    <fo:table-column />
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

