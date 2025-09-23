<%@ page import="com.webfin.insurance.model.*,
com.crux.ff.model.FlexFieldHeaderView,
com.crux.ff.model.FlexFieldDetailView,
com.crux.util.*,
java.util.Date,      
java.math.BigDecimal, 
org.joda.time.DateTime,
com.crux.util.fop.FOPUtil"%><?xml version="1.0" encoding="utf-8"?>
<%

final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
final String fontsize = (String) request.getAttribute("FONTSIZE");
final String attached = (String) request.getAttribute("attached");
boolean tanpaTanggal = attached.equalsIgnoreCase("3")?false:true;

final String preview = (String) request.getAttribute("preview");
boolean isPreview = false;
if (preview!=null)
    if (preview.equalsIgnoreCase("Preview")) isPreview = true;

boolean effective = pol.isEffective();

BigDecimal periodLength = new BigDecimal(pol.getStPeriodLength());

DateTime startDate = new DateTime(pol.getDtPeriodEnd());
DateTime endDate = new DateTime();

int k = 30;
endDate = startDate.plusDays(k);

//if (true) throw new NullPointerException();

%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-height="30cm"
                               page-width="21cm"
                               margin-top="0.80cm"
                               margin-bottom="0.5cm"
                               margin-left="1.5cm"
                               margin-right="1.5cm">
            <fo:region-body margin-top="0.5cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>
        
    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">
        
        <fo:flow flow-name="xsl-region-body">
            
            <% if (isPreview) { %>
            <fo:block font-size="20pt"
                      line-height="16pt" space-after.optimum="0pt"
                      color="blue"
                      text-align="center"
                      padding-top="10pt">
                PREVIEW MODE
            </fo:block>
            <% } %>    
            
            <% if (!effective) {%>
            <fo:block font-size="16pt"
                      line-height="16pt" space-after.optimum="0pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>
            
            <!-- GARIS  -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-before.optimum="50pt" space-after.optimum="1pt"></fo:block>
            
            <!-- defines text title level 1-->

            <fo:block font-size="16pt" line-height="0pt" space-after.optimum="10pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                <%=pol.getStPolicyTypeDesc2()%>
            </fo:block>
            
            <!-- Normal text -->

            <!-- GARIS  -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-before.optimum="1pt" space-after.optimum="10pt" text-align="justify"></fo:block>
            
            <!-- Normal text -->
           
            <%
            DTOList objects = pol.getObjects();
            
            for (int i = 0; i < objects.size(); i++) {
                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
            
            %>
            
            <fo:block font-size="<%=fontsize%>" text-align = "justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="95mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG Bond No. : -L}{L-INA No. Bond : -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG Value : -L}{L-INA Nilai : -L}<%=JSPUtil.print(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align = "end"><%=JSPUtil.printX(pol.getDbInsuredAmount(),0)%> </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align = "justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="150mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block >
                                    <fo:inline font-weight="bold">PT. ASURANSI BANGUN ASKRIDA</fo:inline> berkedudukan di <fo:inline font-weight="bold">Pusat Niaga Cempaka Mas M1/36 Jl. Letjen Soeprapto, Jakarta Pusat 10640</fo:inline>.
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block >
                                    Untuk selanjutnya disebut : PENJAMIN, 
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block >
                                    Dengan ini menyatakan akan membayar sejumlah uang dengan melepaskan hak utamanya yang oleh Undang-Undang diberikan kepada seorang Penjamin sesuai dengan bunyi Pasal 1832 Kitab Undang-Undang Hukum Perdata, kepada :
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Name -L}{L-INA Nama -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>: <fo:inline font-weight="bold"><%=JSPUtil.xmlEscape(pol.getStCustomerName())%></fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Address -L}{L-INA Alamat -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>: <fo:inline font-weight="bold"><%=pol.getStCustomerAddress()%></fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block >
                                    Selanjutnya disebut : PENERIMA JAMINAN, 
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block >
                                    Sejumlah uang <fo:inline font-weight="bold"><%=pol.getStCurrencyCode()%> <%=JSPUtil.printX(pol.getDbInsuredAmount(),0)%></fo:inline> terbilang (<%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbInsuredAmount(),2))%> <%=JSPUtil.printX(pol.getCurrency().getStCcyDescription())%>)
                                    atas dasar tuntutan/klaim yang diajukan secara tertulis dimana berisikan pernyataan bahwa YANG DIJAMIN wanprestasi oleh <fo:inline font-weight="bold"><%=JSPUtil.xmlEscape(obj.getStReference5())%></fo:inline> selaku PEMEGANG JAMINAN atau yang diberikan kuasa dalam jangka waktu pengajuan tuntutan yang ditetapkan dalam Jaminan Kontra Bank Garansi ini, apabila :
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Name -L}{L-INA Nama -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>: <fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference1())%></fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Address -L}{L-INA Alamat -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>: <fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference3())%></fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block >
                                    Selanjutnya disebut : YANG DIJAMIN, 
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block >
                                    Ternyata sampai batas waktu yang ditentukan, namun tidak melebihi tanggal batas waktu berlakunya Jaminan Kontra Bank Garansi ini, lalai tidak memenuhi kewajiban kepada PEMEGANG JAMINAN atas pelaksanaan pekerjaan <fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference6())%></fo:inline> sesuai dengan Kontrak 
                                    No.<fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference11())%></fo:inline> tanggal <fo:inline font-weight="bold"><%=DateUtil.getDateStr(obj.getDtReference3(),"d ^^ yyyy")%></fo:inline>.
                                    
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block >
                                    Jaminan Kontra Bank Garansi ini berlaku terhitung mulai tanggal <fo:inline font-weight="bold"><%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d ^^ yyyy")%></fo:inline> sampai dengan tanggal <fo:inline font-weight="bold"><%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"d ^^ yyyy")%></fo:inline>.
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block >
                                    Tuntutan/klaim harus diajukan secara tertulis segera setelah timbul Wanprestasi atau kelalaian yang dilakukan oleh YANG DIJAMIN dengan menyerahkan Warkat Jaminan Kontra Bank Garansi.
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block >
                                    Penerima Jaminan mengajukan tuntutan/klaim penagihan kepada Penjamin paling lambat 30 (tigapuluh) hari kalender setelah berakhirnya Jaminan Kontra Bank Garansi atau paling lambat tanggal <fo:inline font-weight="bold"><%=DateUtil.getDateStr(endDate.toDate(),"d ^^ yyyy")%></fo:inline> apabila tuntutan/klaim dilakukan oleh Penerima Jaminan.
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block >
                                    Apabila dalam dan/atau sampai habis batas waktu pengajuan tuntutan/klaim tersebut diatas, PEMEGANG JAMINAN atau yang diberi kuasa tidak mengajukan tuntutan/klaim, maka Jaminan Kontra Bank Garansi ini tidak mengikat lagi terhadap PENJAMIN.
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block >
                                    Sertifikat Jaminan Kontra Bank Garansi ini merupakan bagian tidak terpisahkan dari Persetujuan Prinsip Jaminan Kontra Bank Garansi <fo:inline font-weight="bold">PT. ASURANSI BANGUN ASKRIDA</fo:inline> pada <fo:inline font-weight="bold"><%=JSPUtil.xmlEscape(pol.getStCustomerName())%></fo:inline>
                                    No. <fo:inline font-weight="bold"><%=JSPUtil.xmlEscape(pol.getStReference1())%></fo:inline> Tentang Penjaminan Bank Garansi No. <fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference12())%></fo:inline> tanggal <fo:inline font-weight="bold"><%=DateUtil.getDateStr(obj.getDtReference4(),"d ^^ yyyy")%></fo:inline>
                                    dan tidak dapat dipindahtangankan atau dijadikan jaminan kepada pihak lain serta untuk keperluan pemberian jaminan ini beserta segenap akibat yang timbul padanya.
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block >
                                    Untuk keperluan pemberian Jaminan Kontra Bank Garansi ini beserta akibat yang timbul dari padanya, <fo:inline font-weight="bold">PT. ASURANSI BANGUN ASKRIDA</fo:inline> memilih domisili yang umum dan tetap pada Kantor Panitera Pengadilan Negeri <fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference15())%></fo:inline>.
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2">
                                <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) { %>
                                <fo:block >{L-ENG XXX -L}{L-INA Ditanda tangani serta dibubuhkan materai di -L} 
                                <%= JSPUtil.printX(pol.getStCostCenterDesc())%><%if(tanpaTanggal){%>, {L-ENG XXX -L}{L-INA pada tanggal -L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%><%}%>.</fo:block>
                                <% } else { %>  
                                <fo:block >{L-ENG XXX -L}{L-INA Ditanda tangani serta dibubuhkan materai di -L}
                                Jakarta<%if(tanpaTanggal){%>, {L-ENG XXX -L}{L-INA pada tanggal -L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%><%}%>.</fo:block>
                                <% } %>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <% } %>   
            
            <fo:block font-size="<%=fontsize%>" text-align = "justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Jasa Jaminan -L} </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Biaya Administrasi -L} </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDPCost(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Bea Materai -L} </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDSFee(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Jumlah -L} </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTotalDue(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" line-height="0pt" space-after.optimum="5pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align = "justify">                
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="100mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="70pt"></fo:block></fo:table-cell>
                        </fo:table-row>	
                        
                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>	                       
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>
            
            
            <fo:block font-size="6pt"
                      font-family="sans-serif"
                      line-height="10pt"
                      space-after.optimum="10pt"
                      text-align="left" >
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(),"dd/MM/yyyy HH:mm:ss"))%> 
                
                <% if (effective) {%>
                PrintStamp:<%=pol.getStPrintCode()%> PrintCode:<%=pol.getStPrintStamp()%> Page:<fo:page-number/> Terlampir : [<%=JSPUtil.print(request.getAttribute("DOCUMENT_NAME"))%>]
                <% } %>   
            </fo:block>
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>


