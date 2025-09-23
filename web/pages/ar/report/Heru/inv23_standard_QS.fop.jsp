<%@ page import="com.webfin.insurance.model.*,
com.crux.util.*,  
java.util.Date,               
java.math.BigDecimal, 
com.crux.util.fop.FOPUtil,
com.crux.lang.LanguageManager,
com.crux.web.controller.SessionManager, 
com.crux.common.parameter.Parameter,
com.webfin.entity.model.EntityView"%><?xml version="1.0" encoding="utf-8"?>
<%

final InsurancePolicyInwardView pol = (InsurancePolicyInwardView)request.getAttribute("INVOICE");

boolean effective = pol.isEffective();

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->

        <fo:simple-page-master master-name="only"
                               page-height="30cm"
                               page-width="21cm"
                               margin-top="3cm"
                               margin-bottom="1cm"
                               margin-left="2cm"
                               margin-right="2cm">
            
            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>
        
    </fo:layout-master-set>
    
    <fo:page-sequence master-reference="only" initial-page-number="1">
        
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
            
            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
            
            <fo:block font-size="16pt" line-height="16pt" color="black" text-align="center" space-after.optimum="5pt">
                DEFINITE INWARD CLAIM
            </fo:block>
            
            <fo:block font-size="10pt" line-height="16pt" color="black" text-align="center"> NO. <%=pol.getStPLANo()%></fo:block>
            
            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="10pt"></fo:block>
            
            <fo:block font-size="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">Type of Treaty</fo:block></fo:table-cell>             
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Quota Share</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">Year of Treaty</fo:block></fo:table-cell>             
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStAttrUnderwriting()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">Type of Insurance</fo:block></fo:table-cell>             
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getPolicyType().getStDescription()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">Policy Number</fo:block></fo:table-cell>             
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStAttrPolicyNo()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">THE INSURER</fo:block></fo:table-cell>             
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getEntity().getStEntityName().toUpperCase()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">THE INSURED</fo:block></fo:table-cell>             
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(pol.getStAttrPolicyName()).toUpperCase()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">Total Sum Insured (TSI)</fo:block></fo:table-cell>             
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStCurrencyCode()%> <%=JSPUtil.printX(pol.getDbAttrPolicyTSITotal(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">Date of Loss</fo:block></fo:table-cell>             
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtReference2(),"d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">Cause of Loss</fo:block></fo:table-cell>             
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(pol.getStReferenceX1())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">100% Claim of QS</fo:block></fo:table-cell>             
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStCurrencyCode()%> <%=JSPUtil.printX(pol.getDbClaimAmountTotal(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                        BigDecimal amount = null;
                        final DTOList details = pol.getDetails();
                        for (int i = 0; i < details.size(); i++) {
                            InsurancePolicyInwardDetailView io = (InsurancePolicyInwardDetailView) details.get(i);
                            
                            amount = io.getDbAmount();                            
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">Share of ASKRIDA</fo:block></fo:table-cell>             
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStCurrencyCode()%> <%=JSPUtil.printX(io.getDbEnteredAmount(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <% } %>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">Remarks</fo:block></fo:table-cell>             
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block wrap-option="yes-wrap" linefeed-treatment="preserve"
                                                          white-space-treatment="preserve" white-space-collapse="false"
                            ><%=JSPUtil.xmlEscape(pol.getStDescription())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>
            
            <fo:block font-size="6pt" space-after.optimum="10pt" text-align="end">
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(),"dd/MM/yyyy HH:mm:ss"))%> 
            </fo:block>
            
            <fo:block font-size="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block>Jakarta, <%=DateUtil.getDateStr(new Date(),"d ^^ yyyy")%></fo:block>
                                <fo:block font-weight="bold">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block>BAGIAN KLAIM</fo:block>
                            </fo:table-cell>             
                            <fo:table-cell ></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="start">
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="<%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(amount,0)%>" orientation="0">
                                            <barcode:datamatrix> 
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>                                    
                                </fo:block>
                            </fo:table-cell>             
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block font-weight="bold"><fo:inline text-decoration="underline">JATMIKO</fo:inline></fo:block>
                                <fo:block>Kepala Bagian</fo:block>
                            </fo:table-cell>             
                            <fo:table-cell ></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>



