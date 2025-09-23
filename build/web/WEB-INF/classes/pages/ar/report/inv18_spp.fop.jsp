<%@ page import="com.webfin.ar.forms.InvoiceForm,
com.crux.web.controller.SessionManager,
java.util.ArrayList,
com.crux.util.*,
com.crux.util.fop.FOPUtil,
com.webfin.insurance.model.*,
java.math.BigDecimal,
com.crux.lang.LanguageManager,
java.util.Date"%><?xml version="1.0" ?>
<%

final InsurancePolicyInwardView pol = (InsurancePolicyInwardView)request.getAttribute("INVOICE");
final String fontsize = (String) request.getAttribute("FONTSIZE");

boolean effective = pol.isApproved();

String LKP = new String("DLA");

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
            <fo:block font-size="16pt" font-family="TAHOMA"
                      line-height="16pt" space-after.optimum="10pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>
            
            <!-- GARIS  -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="10pt"></fo:block>
            
            <!-- defines text title level 1-->

            <fo:block font-size="16pt" font-family="TAHOMA" line-height="16pt" color="black" text-align="center" space-after.optimum="10pt">
                {L-INA Surat Permintaan Pembayaran-L}{L-ENG Payment Request-L}
            </fo:block>
            
            
            <!-- Normal text -->
            
            <!-- DYNAMIC HEADER WORDING -->


            <!-- GARIS -->
            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
            
            <!-- Normal text -->
            
            <% if (LKP.equalsIgnoreCase(pol.getStClaimStatus())) { %>
            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Date-L}{L-INA Tanggal -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%=DateUtil.getDateStr(new Date(),"d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Number-L}{L-INA Nomor-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%=pol.getStInvoiceNo()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG To-L}{L-INA Kepada -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG FINANCE DEPARTMENT-L}{L-INA BAGIAN KEUANGAN -L}</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <!-- GARIS  -->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.50pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="162mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG We, herewith request to pay -L}{L-INA Dengan ini kami minta agar Saudara membayarkan -L}</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG To-L}{L-INA Kepada-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%=pol.getEntity().getStEntityName()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Amount of-L}{L-INA Sebesar-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%= JSPUtil.printX(pol.getStCurrencyCode()) %> <%=JSPUtil.printX(pol.getDbEnteredAmount(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Say :-L}{L-INA Terbilang :-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbEnteredAmount(),2), pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Remark-L}{L-INA Keterangan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block space-after.optimum="2pt">Pembayaran Klaim Inward <%=pol.getStPolicyTypeDesc().getStDescription()%> </fo:block>
                                <fo:block space-after.optimum="10pt">DOL: <%=DateUtil.getDateStr(pol.getDtAttrPolicyPeriodStart(),"d ^^ yyyy")%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Policy No.-L}{L-INA No. Polis-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%= JSPUtil.printX(pol.getStAttrPolicyNo())%></fo:block></fo:table-cell>
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



