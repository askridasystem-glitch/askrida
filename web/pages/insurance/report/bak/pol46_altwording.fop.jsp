<%@ page import="com.webfin.insurance.model.*,
com.crux.ff.model.FlexFieldHeaderView,
com.crux.ff.model.FlexFieldDetailView,
com.crux.util.*,
java.util.Date,      
java.math.BigDecimal, 
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
            <% }%>    
            
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

            <!-- defines text title level 1-->

            <fo:block font-size="12pt" line-height="0pt" space-after.optimum="10pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                UNCONDITIONAL
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
                            <fo:table-cell number-columns-spanned="2"><fo:block>{L-ENG xxx -L}{L-INA Yang bertanda tangan dibawah ini : -L}<fo:inline font-weight="bold"><%=pol.getUserApproved().getStUserName().toUpperCase()%></fo:inline> {L-ENG xxx -L}{L-INA dalam jabatan selaku -L} <fo:inline font-weight="bold"><%=pol.getUserApproved().getStRolesName(pol.getStApprovedWho())%></fo:inline> 
                                    Dalam hal ini bertindak untuk dan atas nama <fo:inline font-weight="bold"><%=pol.getStProducerName()%></fo:inline> berkedudukan di <fo:inline font-weight="bold"><%=pol.getStProducerAddress()%></fo:inline>
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
                            <fo:table-cell ><fo:block>: <fo:inline font-weight="bold"><%=pol.getStCustomerName()%></fo:inline></fo:block></fo:table-cell>
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
                                    sebagai jaminan Sanggahan Banding dalam bentuk garansi bank, apabila :
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
                                    Ternyata sampai batas waktu yang ditentukan, namun tidak melebihi tanggal batas waktu berlakunya Garansi Bank ini, sanggahan banding yang diajukan oleh YANG DIJAMIN dinyatakan tidak benar.
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align = "justify">                
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="175mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG 1. -L}{L-INA 1. -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Garansi Bank sebagai Jaminan Sanggahan Banding berlaku selama <% if (pol.getStPeriodLength().equalsIgnoreCase("0")) { %> 1 <% } else { %> <%=JSPUtil.printX(pol.getStPeriodLength())%> <% } %> (<% if (pol.getStPeriodLength().equalsIgnoreCase("0")) { %> Satu <% } else { %> <%=NumberSpell.readNumber(JSPUtil.printX(periodLength,2))%> <% } %>)
                                    hari kalender, dari tanggal <fo:inline font-weight="bold"><%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d ^^ yyyy")%></fo:inline> sampai dengan tanggal <fo:inline font-weight="bold"><%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"d ^^ yyyy")%></fo:inline>.
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG 2. -L}{L-INA 2. -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Tuntutan pencairan atau klaim dapat diajukan secara tertulis dengan melampirkan Surat Jawaban Sanggahan Banding yang menyatakan bahwa Sanggahan Banding tidak benar dari <fo:inline font-weight="bold"><%=obj.getStReference17()%></fo:inline> [Menteri/Pimpinan Lembaga/Kepala Daerah/Pimpinan Institusi Lain] paling lambat 14 (empat belas) hari kalender setelah tanggal jatuh tempo Garansi Bank sebagai Jaminan Sanggahan  Banding sebagai mana tercantum dalam butir 1.
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG 3. -L}{L-INA 3. -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Penjamin akan membayar kepada Penerima Jaminan sejumlah  nilai jaminan tersebut diatas dalam waktu paling lambat 14 (empat belas) hari kerja tanpa syarat setelah menerima tuntutan pencairan dari Penerima Jaminan berdasar Sanggahan Banding yang menyatakan bahwa Sanggahan Banding tidak benar.
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG 4. -L}{L-INA 4. -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Penjamin melepaskan hak-hak istimewanya untuk menuntut supaya benda-benda yang diikat sebagai jaminan lebih dahulu disita dan dijual untuk melunasi hutangnya sebagaimana dimaksud dalam pasal 1831 kitab Undang-Undamg Hukum Perdata.
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG 5. -L}{L-INA 5. -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Garansi bank sebagai Jaminan Sanggahan Banding ini tidak dapat dipindahtangankan atau dijadikan  jaminan kepada pihak lain.
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG 6. -L}{L-INA 6. -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Mengenai segala hal yang mungkin timbul sebagai akibat dari Garansi Bank Sebagai Jaminan Sanggahan Banding ini, masing-masing  pihak  memilih domisili hukum yang umum dan tetap di Kantor Pengadilan Negeri <fo:inline font-weight="bold"><%=obj.getStReference17()%></fo:inline>.
                            </fo:block></fo:table-cell>
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
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <% } %>   
            
            <fo:block font-size="<%=fontsize%>" line-height="0pt" space-after.optimum="5pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align = "justify">                
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="120mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <%if(pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")){%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=pol.getStProducerName().toUpperCase()%></fo:block></fo:table-cell>
                            <% }else{ %>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                            <% } %>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>  
                        
                        <% if (pol.getUserApproved().getStSign()!=null) { %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align = "center">  	
                                    <fo:external-graphic <%--content-width="scale-to-fit"--%>
                                                         content-height="70%"
                                                         width="70%"
                                                         scaling="non-uniform" src="url(<%=pol.getUserApproved().getFile().getStFilePath()%>)" />
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>                           
                        <% } else { %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align = "center">  	
                                    <fo:external-graphic <%--content-width="scale-to-fit"--%>
                                                         content-height="70%"
                                                         width="70%"
                                                         scaling="non-uniform" src="url(D:\jboss-3.2.5\server\default\deploy\fin.ear\fin.war\pages\main\img\untitled.jpg)"/>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>  
                        <% } %>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=pol.getUserApproved().getStUserName().toUpperCase()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="1" >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0.2" line-height="0.15pt" space-after.optimum="0.5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=pol.getUserApproved().getStRolesName(pol.getStApprovedWho())%></fo:block></fo:table-cell>
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


