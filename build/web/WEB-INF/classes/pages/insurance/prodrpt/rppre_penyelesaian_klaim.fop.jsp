<%@ page import="com.webfin.insurance.model.*,
com.crux.util.*, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal, 
com.crux.web.form.FormManager,
com.crux.util.DTOList,
com.crux.web.controller.SessionManager, 
com.webfin.insurance.form.ProductionReinsuranceReportForm,
com.crux.common.model.HashDTO,
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 


<%

final DTOList l = (DTOList)request.getAttribute("RPT");

//final ProductionClaimReportForm form = (ProductionClaimReportForm)SessionManager.getInstance().getCurrentForm();

 //HashDTO h = (HashDTO) list.get(i);
%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-height="37cm"
                               page-width="42cm"
                               margin-top="2cm"
                               margin-bottom="1cm"
                               margin-left="2cm"
                               margin-right="2cm">
            <fo:region-body margin-top="1cm" margin-bottom="1cm"/>
            <fo:region-before extent="1cm"/>
            <fo:region-after extent="1cm"/>
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
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="45mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-header>
                        
                        <!-- judul laporan -->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="17">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="14pt" text-align="center">             
                                    PENYELESAIAN KLAIM
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="17">
                                <fo:block font-family="tahoma" font-weight="bold" font-size="10pt" text-align="center">
                                   

                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="17"><fo:block space-after.optimum="20pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                       
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block font-weight="bold">Cabang Asuransi :
                                 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="17"><fo:block text-align="end" space-before.optimum="10pt">(dalam Rupiah)</fo:block></fo:table-cell>
                        </fo:table-row>
                        <!-- garis horizontal -->
                        
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16" >
                                <fo:block border-width="0.5pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <!-- garisnye sampe sini -->
        
                        
                        
                        <!-- sampe sini neh -->
                        
                        <!-- yang ini bikin kolom" header -->



                         <fo:table-row>

                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  ><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  ><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid"><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid"><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid"><fo:block text-align="center">Klaim yang Harus</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid" ><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid" ><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid" ><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid" ><fo:block text-align="center"></fo:block></fo:table-cell>

                        </fo:table-row>




                        <fo:table-row>

                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  ><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  ><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid"><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid"><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid"><fo:block text-align="center">Diselesaikan</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid" ><fo:block text-align="center">Klaim yang Disetujui</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid" ><fo:block text-align="center">Klaim Yang Disetujui</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid" ><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid" ><fo:block text-align="center"></fo:block></fo:table-cell>

                        </fo:table-row>


                           <!-- ini bikin row yg ke dua -->
                        <fo:table-row>

                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  ><fo:block text-align="center">NO</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  ><fo:block text-align="center">Cabang Asuransi</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid"><fo:block text-align="center">Klaim Dalam Proses</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid"><fo:block text-align="center">Klaim yang Diajukan</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid"><fo:block text-align="center">Tahun Berjalan</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid" ><fo:block text-align="center">Tahun Berjalan</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid" ><fo:block text-align="center">Tahun Berjalan</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid" ><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid" ><fo:block text-align="center">Klaim Dalam Proses</fo:block></fo:table-cell>

                        </fo:table-row>

                       <fo:table-row>

                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  ><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  ><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"   border-right-style="solid"><fo:block text-align="center">Akhir Tahun Lalu</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"   border-right-style="solid"><fo:block text-align="center">Tahun Berjalan</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"   border-right-style="solid"><fo:block text-align="center">(Klaim yang Harus Diproses)</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid" ><fo:block text-align="center">Dan Telah Dibayar</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid" ><fo:block text-align="center">Tapi Belum Dibayar</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid" ><fo:block text-align="center">Klaim Ditolak</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid" ><fo:block text-align="center">Tahun Berjalan</fo:block></fo:table-cell>

                        </fo:table-row>



                            <fo:table-row>

                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">Jml Polis</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">Rp. Juta</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">Jml Polis</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">Rp. Juta</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">Jml Polis</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">Rp. Juta</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">Jml Polis</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">Rp. Juta</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">Jml Polis</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">Rp. Juta</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">Jml Polis</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">Rp. Juta</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">Jml Polis)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">Rp. Juta</fo:block></fo:table-cell>

                        </fo:table-row>

                        
                             <!-- ini bikin row yg ke EMPAT -->
                        <fo:table-row>

                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">(1)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">(2)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">(3)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">(4)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">(5)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">(6)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">(7)=(3)+(5)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">(8)=(4)+(6)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">(9)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">(10)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">(11)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">(12)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">(13)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">(14)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">(15)=(7)-(9)-(11)-(13)</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-right-style="solid" border-top-style="solid" border-bottom-style="solid"><fo:block text-align="center">(16)=(8)-(10)-(12)-(14)</fo:block></fo:table-cell>

                        </fo:table-row>
                    </fo:table-header>
                    <fo:table-body>
<!--                    row kosong  -->
                              <%
                             

                              BigDecimal jmlpol3 = BigDecimal.ZERO;
                              BigDecimal jmlRp4 = BigDecimal.ZERO;
                              BigDecimal jmlpol5 = BigDecimal.ZERO;
                              BigDecimal jmlRp6 = BigDecimal.ZERO;
                              BigDecimal jmlpol7 = BigDecimal.ZERO;
                              BigDecimal jmlRp8 = BigDecimal.ZERO;

                              BigDecimal jmlPol9 = BigDecimal.ZERO;
                              BigDecimal jmlRp10 = BigDecimal.ZERO;

                              BigDecimal jmlPol11 = BigDecimal.ZERO;
                              BigDecimal jmlRp12 = BigDecimal.ZERO;

                              BigDecimal jmlPol15 = BigDecimal.ZERO;
                              BigDecimal jmlRp16 = BigDecimal.ZERO;



                                for (int i=0;i< l.size() ; i++){
                              BigDecimal klaimHrsProses = BigDecimal.ZERO;

                                       HashDTO h = (HashDTO) l.get(i);
                                       klaimHrsProses = klaimHrsProses.add(h.getFieldValueByFieldNameBD("JumlahKlaimLks").add(h.getFieldValueByFieldNameBD("JumlahKlaimLkp")));
                                     BigDecimal jml7 =   h.getFieldValueByFieldNameBD("jumlahLks").add(h.getFieldValueByFieldNameBD("jumlahLkp"));
                                     BigDecimal jml15 =   jml7.subtract(h.getFieldValueByFieldNameBD("jmlx")).subtract(h.getFieldValueByFieldNameBD("jmlxs"));
                                     BigDecimal jml16 = klaimHrsProses.subtract(h.getFieldValueByFieldNameBD("jmlrn")).subtract(h.getFieldValueByFieldNameBD("jmln"));

                           jmlpol3 = jmlpol3.add(h.getFieldValueByFieldNameBD("jumlahLks"));
                           jmlRp4 = jmlRp4.add(h.getFieldValueByFieldNameBD("JumlahKlaimLks"));

                           jmlpol5 = jmlpol5.add(h.getFieldValueByFieldNameBD("jumlahLkp"));
                           jmlRp6 = jmlRp6.add(h.getFieldValueByFieldNameBD("JumlahKlaimLkp"));

                           jmlpol7 = jmlpol7.add(jml7);
                           jmlRp8 = jmlRp8.add(klaimHrsProses);

                           jmlPol9 = jmlPol9.add(h.getFieldValueByFieldNameBD("jmlx"));
                           jmlRp10 = jmlRp10.add(h.getFieldValueByFieldNameBD("jmlrn"));


                            jmlPol11 = jmlPol11.add(h.getFieldValueByFieldNameBD("jmlxs"));
                           jmlRp12 = jmlRp12.add(h.getFieldValueByFieldNameBD("jmln"));

                           jmlPol15 = jmlPol15.add(jml15);
                           jmlRp16 = jmlRp16.add(jml16);
                            %>

                         <fo:table-row>

                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid"><fo:block text-align="left"><%=JSPUtil.printX(i+1)%> </fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(h.getFieldValueByFieldNameST("group_name"))%> </fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid"><fo:block text-align="center"> <%=JSPUtil.printX(h.getFieldValueByFieldName("jumlahLks"))%>  </fo:block> </fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid"><fo:block text-align="right"> <%=JSPUtil.printX(h.getFieldValueByFieldNameBD("JumlahKlaimLks"))%> </fo:block> </fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid"><fo:block text-align="center"> <%=JSPUtil.printX(h.getFieldValueByFieldName("jumlahLkp"))%> </fo:block> </fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid"><fo:block text-align="right"> <%=JSPUtil.printX(h.getFieldValueByFieldNameBD("JumlahKlaimLkp"))%> </fo:block> </fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid"><fo:block text-align="center"> <%=JSPUtil.printX(jml7)%> </fo:block> </fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid"><fo:block text-align="right"> <%=JSPUtil.printX(klaimHrsProses)%> </fo:block> </fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid"><fo:block text-align="center"> <%=JSPUtil.printX(h.getFieldValueByFieldNameBD("jmlx"))%> </fo:block> </fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid" border-right-style="solid" border-top-style="solid"><fo:block text-align="right"> <%=JSPUtil.printX(h.getFieldValueByFieldNameBD("jmlrn"))%></fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(h.getFieldValueByFieldNameBD("jmlxs"))%> </fo:block>  </fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid"><fo:block text-align="right"> <%=JSPUtil.printX(h.getFieldValueByFieldNameBD("jmln"))%> </fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid"><fo:block text-align="center"> - </fo:block> </fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid"><fo:block text-align="right"> -  </fo:block> </fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid"><fo:block text-align="center"> <%=JSPUtil.printX(jml15)%>  </fo:block> </fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid" border-right-style="solid" border-top-style="solid"> <fo:block text-align="right"> <%=JSPUtil.printX(jml16)%> </fo:block></fo:table-cell>

                        </fo:table-row>

                            <%
                            }
                             %>

                        <fo:table-row>

                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid" ><fo:block text-align="left"></fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid"  border-top-style="solid" border-bottom-style="solid" ><fo:block text-align="center"> Total</fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid" ><fo:block text-align="center"><%=JSPUtil.printX(jmlpol3)%>  </fo:block> </fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid" ><fo:block text-align="right"><%=JSPUtil.printX(jmlRp4)%> </fo:block> </fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid" ><fo:block text-align="center"><%=JSPUtil.printX(jmlpol5)%></fo:block> </fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid" ><fo:block text-align="right"> <%=JSPUtil.printX(jmlRp6)%></fo:block> </fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid" ><fo:block text-align="center"> <%=JSPUtil.printX(jmlpol7)%></fo:block> </fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid" ><fo:block text-align="right"><%=JSPUtil.printX(jmlRp8)%> </fo:block> </fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid" ><fo:block text-align="center"><%=JSPUtil.printX(jmlPol9)%> </fo:block> </fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid" border-right-style="solid" border-bottom-style="solid" border-top-style="solid"><%=JSPUtil.printX(jmlRp10)%><fo:block text-align="right"></fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid"><fo:block text-align="center"><%=JSPUtil.printX(jmlPol11)%>  </fo:block>  </fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid" ><fo:block text-align="right"><%=JSPUtil.printX(jmlRp12)%> </fo:block></fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid" ><fo:block text-align="center"> - </fo:block> </fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid" ><fo:block text-align="right"> - </fo:block> </fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-top-style="solid" border-left-style="solid" border-bottom-style="solid" ><fo:block text-align="center"><%=JSPUtil.printX(jmlPol15)%></fo:block> </fo:table-cell>
                            <fo:table-cell display-align="center" border-width="0.5pt" border-left-style="solid" border-right-style="solid" border-bottom-style="solid" border-top-style="solid"> <fo:block text-align="right"> <%=JSPUtil.printX(jmlRp16)%></fo:block></fo:table-cell>

                        </fo:table-row>
                    </fo:table-body>


                </fo:table>
            </fo:block>    
            
            <fo:block font-size="6pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
            </fo:block> 

            <fo:block 
                      font-size="6pt"
                      font-family="TAHOMA"
                      line-height="12pt"
                      font-style="bold">
                rppre_phitungan_surplus_underwriting.fop - PT. Asuransi Bangun Askrida
            </fo:block>

            
            <fo:block id="end-of-document"><fo:marker
                    marker-class-name="term">
                    <fo:instream-foreign-object>
                        <svg xmlns="http://www.w3.org/2000/svg" width="15cm" height="1cm" xml:space="preserve">     
                            <rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block> 
            
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>
