<%@ page import="com.webfin.ar.model.*,  
         com.webfin.ar.forms.DepositoReportForm,
         com.crux.util.*,
         java.math.BigDecimal,
         com.crux.web.controller.SessionManager,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%

            final DTOList l = (DTOList) request.getAttribute("RPT");

            final DepositoReportForm form = (DepositoReportForm) SessionManager.getInstance().getCurrentForm();

            String cabang = null;

            if (SessionManager.getInstance().getSession().getStBranch() != null) {
                cabang = form.getStBranchName();
            } else {
                cabang = form.getStBranchDesc();
            }

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 

    <!-- defines page layout --> 
    <fo:layout-master-set> 

        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="first" 
                               page-height="21cm"
                               page-width="33cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">

            <fo:region-body margin-top="0cm" margin-bottom="0cm"/> 
            <fo:region-before extent="0cm"/> 
            <fo:region-after extent="0.5cm"/> 
        </fo:simple-page-master> 

    </fo:layout-master-set> 
    <!-- end: defines page layout --> 

    <!-- actual layout --> 
    <fo:page-sequence master-reference="first"> 

        <!-- usage of page layout --> 
        <fo:flow flow-name="xsl-region-body"> 

            <fo:block font-size="8pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="10mm"/><!-- No -->
                    <fo:table-column column-width="70mm"/><!-- Entry Date -->
                    <fo:table-column column-width="20mm"/><!-- Entry Date -->
                    <fo:table-column column-width="20mm"/><!-- Policy Date--> 
                    <fo:table-column column-width="20mm"/><!-- Policy Date--> 
                    <fo:table-column column-width="20mm"/><!-- Policy Date--> 
                    <fo:table-column column-width="30mm"/><!-- Policy No -->
                    <fo:table-column column-width="70mm"/><!-- The Insured -->
                    <fo:table-column column-width="20mm"/><!-- Premium --> 
                    <fo:table-column /><!-- Biaya Polis --> 
                    <fo:table-header>          

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold" text-align="center" font-size="12pt">             
                                    LAPORAN DEPOSITO AKTIF
                                </fo:block>
                                <fo:block font-weight="bold" text-align="center" font-size="10pt">
                                    sampai dengan <%=DateUtil.getDateStr(form.getDtTglAwalFrom(), "dd ^^ yyyy")%>
                                </fo:block>                                
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold" font-size="10pt">
                                    <% if (cabang != null) {%>
                                    Daerah : <%=JSPUtil.printX(cabang.toUpperCase())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold" font-size="10pt">
                                    <% if (form.getStCompTypeID() != null) {%>
                                    Jenis : <%=JSPUtil.printX(form.getStCompTypeName().toUpperCase())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block font-weight="bold" font-size="10pt">
                                    <% if (form.getStEntityName() != null) {%>
                                    Bank : <%=JSPUtil.printX(form.getStDescription().toUpperCase())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10" >  
                                <fo:block border-width="0.25pt" border-style="solid" line-height="0.25pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  

                        <fo:table-row>   
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Bilyet -L}{L-INA Bilyet -L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Time -L}{L-INA Waktu -L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Start Date -L}{L-INA Tanggal Awal -L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG End Date -L}{L-INA Tanggal Akhir -L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Debet Date -L}{L-INA Tanggal Pendebetan -L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Amount -L}{L-INA Nominal -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Bank -L}{L-INA Bank -L}</fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Rate -L}{L-INA Rate Bunga -L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Description -L}{L-INA Keterangan -L}</fo:block></fo:table-cell>  
                        </fo:table-row>    

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10" >  
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 

                    </fo:table-header>   

                    <fo:table-body>   

                        <%
                                    BigDecimal nominal = new BigDecimal(0);

                                    BigDecimal[] t = new BigDecimal[1];

                                    String keterangan = null;

                                    int counter = 30;
                                    for (int i = 0; i < l.size(); i++) {
                                        ARInvestmentDepositoView depo = (ARInvestmentDepositoView) l.get(i);

                                        int n = 0;
                                        t[n] = BDUtil.add(t[n++], depo.getDbNominal());

                                        if (depo.getStStatus().equalsIgnoreCase("DEPOSITO")) {
                                            keterangan = "Pembentukan";
                                        } else {
                                            keterangan = "Perpanjangan";
                                        }

                                        if (i == counter) {
                                            counter = counter + 30;
                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm" font-weight="bold">SUB-TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(nominal, 2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3" border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center" line-height="5mm"></fo:block></fo:table-cell>
                        </fo:table-row>  

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        <%
                                                        nominal = null;

                                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>   
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=String.valueOf(i + 1)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(depo.getStNodefo())%></fo:block></fo:table-cell> 
                            <% if (depo.getStKodedepo().equalsIgnoreCase("1")) {%>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(depo.getStHari())%> Hari</fo:block></fo:table-cell> 
                            <% } else if (depo.getStKodedepo().equalsIgnoreCase("2")) {%>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(depo.getStBulan())%> Bulan</fo:block></fo:table-cell> 
                            <% }%>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(depo.getDtTglawal())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(depo.getDtTglakhir())%></fo:block></fo:table-cell>  
                            <% if (depo.getDtTgldepo() != null) {%>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(depo.getDtTgldepo())%></fo:block></fo:table-cell> 
                            <% } else {%>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"></fo:block></fo:table-cell> 
                            <% }%>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(depo.getDbNominal(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(depo.getStBankName())%></fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(depo.getDbBunga())%></fo:block></fo:table-cell> 
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center" line-height="5mm"><%=JSPUtil.printX(keterangan)%></fo:block></fo:table-cell> 
                        </fo:table-row>			

                        <%
                                        nominal = BDUtil.add(nominal, depo.getDbNominal());
                                    }%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm" font-weight="bold">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[0], 2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3" border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="center" line-height="5mm"></fo:block></fo:table-cell>
                        </fo:table-row>  

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="10">  
                                <fo:block border-width="0.25pt" border-style="solid" line-height="0.25pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  

                    </fo:table-body>
                </fo:table>   
            </fo:block> 

            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>
            </fo:block>       

            <fo:block text-align="start">
                <fo:instream-foreign-object>
                    <barcode:barcode
                        xmlns:barcode="http://barcode4j.krysalis.org/ns"
                        message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(t[0], 0)%>" orientation="0">
                        <barcode:datamatrix> 
                            <barcode:height>40pt</barcode:height>
                            <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                        </barcode:datamatrix>
                    </barcode:barcode>
                </fo:instream-foreign-object>                                    
            </fo:block>

        </fo:flow> 
    </fo:page-sequence>   
</fo:root>   