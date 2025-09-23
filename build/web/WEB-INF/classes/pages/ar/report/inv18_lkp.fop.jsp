<%@ page import="com.webfin.ar.forms.InvoiceForm,
com.crux.web.controller.SessionManager,
java.util.ArrayList,
com.crux.util.*,
com.crux.util.fop.FOPUtil,
com.webfin.insurance.model.*,
java.math.BigDecimal,
com.crux.common.parameter.Parameter,
com.crux.lang.LanguageManager,
java.util.Date"%><?xml version="1.0" ?>

<%
final InsurancePolicyInwardView pol = (InsurancePolicyInwardView)request.getAttribute("INVOICE");
final String fontsize = (String) request.getAttribute("FONTSIZE");

boolean effective = pol.isApproved();

String LKP = new String("DLA");
BigDecimal deduct = BDUtil.mul(pol.getDbAttrPolicyTSITotal(), BDUtil.getRateFromPct(pol.getDbSharePct()));
%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->

        <fo:simple-page-master master-name="only"
                               page-height="30cm"
                               page-width="21cm"
                               margin-top="1cm"
                               margin-bottom="0.5cm"
                               margin-left="1.5cm"
                               margin-right="1.5cm">
            
            <fo:region-body margin-top="3cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>
        
        
    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">
        
        <!-- usage of page layout -->
        <!-- HEADER -->

        <fo:flow flow-name="xsl-region-body">
            
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
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="10pt"></fo:block>
            
            <!-- defines text title level 1-->

            <fo:block font-size="16pt" line-height="16pt" color="black" text-align="center">
                {L-INA Klaim Pasti Asuransi-L}<%=pol.getStPolicyTypeDesc().getStDescription()%>{L-ENG Definitive Loss Advice-L}
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" line-height="10pt" space-after.optimum="10pt" color="black" text-align="center"> NO. <%=pol.getStInvoiceNo()%></fo:block>
            
            <!-- Normal text -->
            
            <!-- DYNAMIC HEADER WORDING -->


            <!-- GARIS -->
            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
            
            <fo:block font-size="<%=fontsize%>" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>
            
            <% if (LKP.equalsIgnoreCase(pol.getStClaimStatus())) { %>
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Policy Number-L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= pol.getStAttrPolicyNo()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Type of Insurance-L}{L-INA Jenis Asuransi-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStPolicyTypeDesc().getStDescription()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Claim Person Name-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStAttrPolicyName()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Event of Date -L}{L-INA Tanggal Event-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtAttrPolicyPeriodStart(), "d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Sum Insured-L}{L-INA Harga Pertanggungan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStCurrencyCode()%> <%=JSPUtil.printX(pol.getDbAttrPolicyTSITotal(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Deductible-L}{L-INA Resiko Sendiri-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbSharePct(),2)%> %</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Askrida Share-L}{L-INA Askrida Share-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>IDR <%=JSPUtil.printX(pol.getDbPerHole(),2)%>/hole</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Claim Object-L}{L-INA Objek Klaim -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= pol.getStPolicyTypeDesc().getStDescription()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Claim of Date -L}{L-INA Tanggal Klaim-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtAttrPolicyPeriodStart(), "d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Claim Cause-L}{L-INA Penyebab Klaim-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStDescription()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Name of Insured-L}{L-INA Nama Pengaju Klaim-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getEntity().getStEntityName()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Address of Insured-L}{L-INA Alamat Pengaju Klaim-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getEntity().getStAddress()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Currency -L}{L-INA Mata Uang-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStCurrencyCode()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Claim Payable-L}{L-INA Pembayaran Klaim-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Claim Document-L}{L-INA Dokumen Klaim-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Claim (100%)-L}{L-INA Claim (100%)-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start"><%=pol.getStCurrencyCode()%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbAttrPolicyTSITotal(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Deductible-L}{L-INA Resiko Sendiri-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start"><%=pol.getStCurrencyCode()%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(deduct,2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4">
                                <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="2pt" space-after.optimum="2pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>  
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Total-L}{L-INA Jumlah-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start"><%=pol.getStCurrencyCode()%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(pol.getDbAttrPolicyTSITotal(), deduct),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Askrida Share-L}{L-INA Askrida Share-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start"><%=pol.getStCurrencyCode()%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPerHole(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">x</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(pol.getDbAttrPolicyTSITotal(), deduct),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"  space-before.optimum="2pt" space-after.optimum="2pt"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"></fo:table-cell>
                        </fo:table-row>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start"><%=pol.getStCurrencyCode()%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbAttrPolicyTSITotal(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6">
                                <fo:block font-size="<%=fontsize%>" line-height="10pt" space-after.optimum="10pt" text-align="justify" space-before.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start"><%=pol.getStCurrencyCode()%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbEnteredAmount(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <% } %>
            
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="10pt"></fo:block>
            
            <fo:block font-size="6pt"
                      font-family="sans-serif"
                      line-height="10pt"
                      space-after.optimum="10pt"
                      text-align="left" >
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(),"dd/MM/yyyy HH:mm:ss"))%>  Page:<fo:page-number/> Terlampir : [<%=JSPUtil.print(request.getAttribute("DOCUMENT_NAME"))%>]
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="100mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(new Date(),"d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <% if (pol.getStAttrPolicyTypeID().equalsIgnoreCase("51")||pol.getStAttrPolicyTypeID().equalsIgnoreCase("52")||pol.getStAttrPolicyTypeID().equalsIgnoreCase("53")||pol.getStAttrPolicyTypeID().equalsIgnoreCase("54")
                                ||pol.getStAttrPolicyTypeID().equalsIgnoreCase("55")||pol.getStAttrPolicyTypeID().equalsIgnoreCase("56")||pol.getStAttrPolicyTypeID().equalsIgnoreCase("57")||pol.getStAttrPolicyTypeID().equalsIgnoreCase("58")) { %>
                                <fo:block text-align="center">BAGIAN BONDING,</fo:block>
                                <% } else { %>
                                <fo:block text-align="center">BAGIAN KLAIM,</fo:block>
                                <% } %>   
                            </fo:table-cell>
                        </fo:table-row>           
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block number-column-display="2" space-after.optimum="60pt"></fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <% if (pol.getStAttrPolicyTypeID().equalsIgnoreCase("51")||pol.getStAttrPolicyTypeID().equalsIgnoreCase("52")||pol.getStAttrPolicyTypeID().equalsIgnoreCase("53")||pol.getStAttrPolicyTypeID().equalsIgnoreCase("54")
                                ||pol.getStAttrPolicyTypeID().equalsIgnoreCase("55")||pol.getStAttrPolicyTypeID().equalsIgnoreCase("56")||pol.getStAttrPolicyTypeID().equalsIgnoreCase("57")||pol.getStAttrPolicyTypeID().equalsIgnoreCase("58")) { %>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">TEPANUS HUTABARAT</fo:inline></fo:block>
                                <fo:block text-align="center">Kabag. Bonding</fo:block> 
                                <% } else { %>
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("KABAG_KLAIM2")%></fo:inline></fo:block>
                                <fo:block text-align="center">Kabag. Klaim</fo:block> 
                                <% } %>
                            </fo:table-cell>
                        </fo:table-row> 
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>            
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>