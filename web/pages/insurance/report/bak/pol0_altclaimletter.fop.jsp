<%@ page import="com.webfin.insurance.model.*,
com.webfin.entity.model.EntityAddressView,
com.crux.ff.model.FlexFieldHeaderView,
com.crux.ff.model.FlexFieldDetailView,
com.crux.util.*,
com.crux.util.fop.FOPUtil,
java.util.Date,
com.crux.web.controller.SessionManager, 
com.crux.common.parameter.Parameter,
java.math.BigDecimal,
java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%

final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
final String fontsize = (String) request.getAttribute("FONTSIZE");
final String attached = (String) request.getAttribute("attached");
final String otorized = (String) request.getAttribute("authorized");
boolean isAttached = attached.equalsIgnoreCase("1")?false:true;

boolean effective = pol.isEffective();

String nopol = pol.getStPolicyNo();

String jenis_pol = pol.getStPolicyTypeDesc2().toUpperCase();

String objectName = null;

final DTOList objects2 = pol.getObjectsClaim();

for(int j = 0;j < objects2.size(); j++){
    InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects2.get(j);
    
    objectName = obj.getStReference1();
}

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->

        <fo:simple-page-master master-name="only"
                               page-height="30cm"
                               page-width="21cm"
                               margin-top="5cm"
                               margin-bottom="1cm"
                               margin-left="2.5cm"
                               margin-right="2.5cm">
            
            <fo:region-body margin-top="1.5cm" margin-bottom="1.5cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="1.5cm"/>
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
            
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" text-align="justify" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
            
            <fo:block font-size="16pt" text-align="center">
                <fo:inline font-weight="bold">LETTER OF DISCHARGE</fo:inline>
            </fo:block>
            
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" text-align="justify" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="10pt">
                I/We, <fo:inline font-weight="bold"><%=JSPUtil.printX(pol.getStCustomerName().toUpperCase())%></fo:inline>, hereby acknowledge that I/We have received the payment of total IDR <fo:inline font-weight="bold"><%= JSPUtil.printX(pol.getDbClaimCustAmount(),2)%></fo:inline>,
                full and final paid by <fo:inline font-weight="bold">PT. Asuransi Bangun Askrida</fo:inline>, for indemnity payment regarding claim of <fo:inline font-weight="bold"><%= JSPUtil.printX(pol.getStPolicyTypeDesc().toUpperCase())%></fo:inline>,
                date of loss <fo:inline font-weight="bold"><%=DateUtil.getDateStr(pol.getDtClaimDate(),"dd ^^ yyyy")%></fo:inline> under policy No. <fo:inline font-weight="bold"><%=JSPUtil.printX(pol.getStPolicyNo())%></fo:inline>.
            </fo:block>   
            
            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="10pt">
                By issuing this letter, I/We shall release the Insurer from any further actions, suits, claims, and any further expanses related to the claim.    
            </fo:block>  
            
            <fo:block font-size="<%=fontsize%>" space-after.optimum="40pt"></fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="100mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(new Date(),"dd ^^ yyyy")%></fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>           
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block number-column-display="2" space-after.optimum="30pt"></fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block number-column-display="2" space-after.optimum="30pt" text-align="center" font-size="6pt">Signature &#x26; Stamp (if any)</fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline" font-weight="bold"><%=JSPUtil.printX(pol.getStCustomerName())%></fo:inline></fo:block>
                                <fo:block text-align="center"><fo:inline font-weight="bold">THE INSURED</fo:inline></fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="8pt" space-before.optimum="40pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
            </fo:block>   
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>




