<%@ page import="com.webfin.insurance.model.*,
com.crux.ff.model.FlexFieldHeaderView,
com.crux.ff.model.FlexFieldDetailView,
com.crux.util.*,
com.crux.util.fop.FOPUtil,
org.joda.time.Days,
org.joda.time.DateTime,
com.crux.common.parameter.Parameter,
java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%

final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
final String fontsize = (String) request.getAttribute("FONTSIZE");

boolean effective = pol.isEffective();

DateTime startDate = new DateTime(pol.getDtPeriodStart());
DateTime endDate = new DateTime(pol.getDtPeriodEnd());
Days d = Days.daysBetween(startDate, endDate);
int day = d.getDays();

//if (true) throw new NullPointerException();

%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-height="30.7cm"
                               page-width="21cm"
                               margin-top="2cm"
                               margin-bottom="0.5cm"
                               margin-left="2.5cm"
                               margin-right="2.5cm">
            <fo:region-body margin-top="1cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>
        
    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">
        
        <!-- usage of page layout -->
        <!-- header -->
    

        <fo:flow flow-name="xsl-region-body">
            
            <!-- defines text title level 1-->
      
            <% if (!effective) {%>
            <fo:block font-size="16pt"
                      line-height="16pt" space-after.optimum="0pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>
            
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-after.optimum="5pt"></fo:block>
            
            <fo:block font-size="14pt" line-height="0pt" space-after.optimum="10pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                SERTIFIKAT KONTRA BANK GARANSI
            </fo:block>
            
            <fo:block font-size="14pt" line-height="0pt" space-after.optimum="5pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                <%=JSPUtil.printX(pol.getStPolicyTypeDesc2())%>
            </fo:block>
            
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-before.optimum="5pt" space-after.optimum="10pt"></fo:block>
            
            <!-- defines text title level 1-->

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="70mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block>Nomor : <%= pol.getStPolicyNo() %></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>Nilai   </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%= pol.getStCurrencyCode() %> <%= JSPUtil.printX(pol.getDbInsuredAmount(),0) %></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>Premi   </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%= pol.getStCurrencyCode() %> <%= JSPUtil.printX(pol.getDbPremiTotal(),0) %></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align="center" font-weight="bold" space-after.optimum="5pt"><fo:inline font-weight="bold"><%=pol.getStProducerName().toUpperCase()%></fo:inline></fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="10pt"><fo:inline font-weight="bold"><%=pol.getStProducerAddress()%></fo:inline></fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="5pt">Dengan ini menyatakan akan membayar sejumlah uang dengan melepaskan hak-hak utamanya yang oleh Undang-Undang diberikan kepada seorang penjamin sesuai bunyi Pasal 1832 KUHP, setinggi-tingginya sebesar :</fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align="center" font-weight="bold" space-after.optimum="5pt"><fo:inline font-weight="bold"><%= pol.getStCurrencyCode() %> <%= JSPUtil.printX(pol.getDbInsuredAmount(),0) %></fo:inline></fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="10pt">Terbilang : <fo:inline font-weight="bold">## <%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbInsuredAmount(), 0),pol.getStCurrencyCode())%> ##</fo:inline></fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="10pt">Kepada : <fo:inline font-weight="bold"><%= pol.getStCustomerName() %></fo:inline></fo:block>
            
            <%
            DTOList objects = pol.getObjects();
            
            for (int i = 0; i < objects.size(); i++) {
                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
            
            %>
            
            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="10pt">Sehubungan dengan Bank Garansi sebesar <fo:inline font-weight="bold"><%= pol.getStCurrencyCode() %> <%= JSPUtil.printX(pol.getDbInsuredAmount(),0) %></fo:inline> yang dikeluarkan oleh <fo:inline font-weight="bold"><%= pol.getStCustomerName() %></fo:inline> untuk kepentingan <fo:inline font-weight="bold"><%= obj.getStReference1() %>, <%= obj.getStReference3() %></fo:inline> berdasarkan kontrak No. <fo:inline font-weight="bold"><%= obj.getStReference11() %></fo:inline>, tanggal <fo:inline font-weight="bold"><%=DateUtil.getDateStr(obj.getDtReference3(),"d ^^ yyyy")%></fo:inline>.</fo:block>
            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="10pt">Dengan Beneficiary : <fo:inline font-weight="bold"><%= obj.getStReference5() %></fo:inline></fo:block>
            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="10pt">Untuk Proyek Pekerjaan : <fo:inline font-weight="bold"><%= obj.getStReference6() %></fo:inline></fo:block>
            <fo:block font-size="<%=fontsize%>" text-align="justify" >GARANSI INI DIKELUARKAN DENGAN KETENTUAN SEBAGAI BERIKUT : </fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align = "justify" space-after.optimum="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block >1. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >Kontra Garansi ini berlaku sejak tanggal <fo:inline font-weight="bold"><%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d ^^ yyyy")%></fo:inline> dan berakhir (jatuh tempo) tanggal <fo:inline font-weight="bold"><%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"d ^^ yyyy")%></fo:inline>.</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block >2. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >Batas waktu pengajuan klaim adalah 30 (tiga puluh) hari sejak jatuh tempo dan pembayaran akan dilakukan 15 (lima belas) hari sejak klaim diterima.</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block >3. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >Klaim dibayarkan setelah memenuhi dalam kontrak.</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block >4. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >Kontra Garansi ini tidak berlaku lagi apabila Principal telah menyelesaikan pekerjaannya sesuai kontrak atau jangka waktu telah melampau masa berlaku jaminan.</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="10pt">Demikian Kontra Garansi ini dikeluarkan untuk dapat dipergunakan oleh yang berkepentingan. Ditandatangani serta dibubuhi cap dan materai di <fo:inline font-weight="bold"><%=pol.getStCostCenterDesc()%></fo:inline>, tanggal <fo:inline font-weight="bold"><%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d ^^ yyyy")%></fo:inline>.</fo:block>
            
            
            <fo:block font-size="<%=fontsize%>" text-align = "justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block space-before.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">PENYEDIA JASA ({L-ENG PRINCIPAL -L}{L-INA PRINCIPAL -L})</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">PENJAMIN ({L-ENG SURETY -L}{L-INA SURETY -L})</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference1())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(pol.getStProducerName())%></fo:block></fo:table-cell>
                        </fo:table-row>   
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block space-before.optimum="60pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="1pt"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0.2" line-height="0.15pt" space-after.optimum="0.5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference17())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%= pol.getUserIDSignName()%></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">Direktur</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">Pimpinan Cabang</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <% } %>	
            
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>
            
            
            <fo:block font-size="6pt"
                      font-family="sans-serif"
                      line-height="10pt"
                      space-after.optimum="10pt"
                      text-align="left" >
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(),"dd/MM/yyyy HH:mm:ss"))%> PrintCode:<%=pol.getStPrintCode()%> PrintStamp:<%=pol.getStPrintStamp()%> Page:<fo:page-number/> Terlampir : [<%=JSPUtil.print(request.getAttribute("DOCUMENT_NAME"))%>]
                
            </fo:block>
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>


