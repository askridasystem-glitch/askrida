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

            final InsurancePolicyInwardView pol = (InsurancePolicyInwardView) request.getAttribute("INVOICE");

            boolean effective = pol.isEffective();

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
                               margin-left="1.5cm"
                               margin-right="1.5cm">

            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>

    <fo:page-sequence master-reference="only" initial-page-number="1">

        <fo:flow flow-name="xsl-region-body">                 

            <% if (!effective) {%>
            <fo:block font-size="16pt"
                      line-height="16pt" space-after.optimum="10pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>

            <fo:block font-size="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="70mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="start">Nomor : <%=JSPUtil.printX(pol.getStClaimNo())%></fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end">Jakarta, <%=DateUtil.getDateStr(new Date(), "d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="10pt" line-height="10pt" space-after.optimum="10pt" text-align="justify"> </fo:block>

            <fo:block font-size="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG To -L}{L-INA Kepada Yth. -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block><fo:inline font-weight="bold"><%=JSPUtil.xmlEscape(pol.getEntity().getStEntityName().toUpperCase())%></fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block><%=JSPUtil.xmlEscape(pol.getEntity().getStAddress().toUpperCase())%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>            

            <fo:block font-size="10pt" line-height="10pt" space-before.optimum="10pt" space-after.optimum="10pt">
                Dengan hormat,
            </fo:block>

            <fo:block font-size="10pt" line-height="10pt" text-align="center" space-after.optimum="10pt">
                <fo:inline font-weight="bold">Perihal : Konfirmasi Klaim <%=pol.getPolicyType().getStDescription()%> - Quota Share</fo:inline>
            </fo:block>

            <fo:block font-size="10pt" line-height="10pt" text-align="justify" space-after.optimum="10pt">
                Bersama ini kami sampaikan persetujuan teknis atas klaim tersebut dengan perincian sbb:
            </fo:block>

            <fo:block font-size="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="65mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="5" >  
                                <fo:block border-width="0.15pt" border-style="solid" space-before.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  

                        <fo:table-row>
                            <fo:table-cell  border-left-style="solid" padding="2pt"><fo:block text-align="center">No. LKP</fo:block></fo:table-cell>
                            <fo:table-cell  border-left-style="solid" padding="2pt"><fo:block text-align="center">Tgl. Klaim</fo:block></fo:table-cell>
                            <fo:table-cell  border-left-style="solid" padding="2pt"><fo:block text-align="center">Tertanggung</fo:block></fo:table-cell>
                            <fo:table-cell  border-left-style="solid" padding="2pt"><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell  border-left-style="solid" border-right-style="solid" padding="2pt"><fo:block text-align="center">Share Askrida</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="5" >  
                                <fo:block border-width="0.15pt" border-style="solid"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 

                        <%
                                    BigDecimal nilai = new BigDecimal(0);
                                    BigDecimal netto = new BigDecimal(0);
                                    final DTOList details = pol.getDetails();
                                    for (int i = 0; i < details.size(); i++) {
                                        InsurancePolicyInwardDetailView io = (InsurancePolicyInwardDetailView) details.get(i);

                                        if (io.getStARTrxLineID().equalsIgnoreCase("108")) {
                                            nilai = io.getDbEnteredAmount();
                                        } else if (io.getStARTrxLineID().equalsIgnoreCase("135")) {
                                            nilai = BDUtil.negate(io.getDbEnteredAmount());
                                        }

                                        netto = BDUtil.add(netto, nilai);
                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell border-left-style="solid" padding="2pt"><fo:block text-align="center"><%if (pol.isDLA()) {%><%=pol.getStDLANo()%> <%} else {%> <%=pol.getStPLANo()%> <%}%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt"><fo:block text-align="center"><%= JSPUtil.printX(pol.getDtReference2())%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt"><fo:block text-align="center" font-size="8pt"><%= JSPUtil.printX(pol.getStAttrPolicyName().toUpperCase())%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt"><fo:block text-align="start"><%= JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell border-right-style="solid" padding="2pt"><fo:block text-align="end"><%= JSPUtil.printX(netto, 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="5" >  
                                <fo:block border-width="0.15pt" border-style="solid" space-after.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>                            

                    </fo:table-body>
                </fo:table>
            </fo:block> 

            <fo:block font-size="10pt" line-height="10pt" text-align="justify" space-before.optimum="10pt" space-after.optimum="10pt">
                Berkenaan dengan persetujuan tersebut, perlu kami tegaskan bahwa apabila dikemudian hari terdapat indikasi bahwa 
                kerugian tidak dijamin oleh kondisi polis dan/atau perjanjian reasuransi dan/atau bertentangan 
                dengan ketentuan hukum yang berlaku, maka kami akan menarik kembali/membatalkan persetujuan tersebut.
            </fo:block>

            <fo:block font-size="10pt" line-height="10pt" text-align="justify" space-after.optimum="10pt">
                Demikian kami sampaikan. Atas perhatian dan kerjasamanya, kami ucapkan terima kasih.
            </fo:block>   

            <fo:block font-size="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block>Hormat kami,</fo:block>
                                <fo:block font-weight="bold">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                            </fo:table-cell>             
                            <fo:table-cell ></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2">
                                <fo:block space-before.optimum="30pt" space-after.optimum="30pt"></fo:block>
                            </fo:table-cell>             
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block font-weight="bold"><fo:inline text-decoration="underline"><%= Parameter.readString("KABAG_KLAIM2")%></fo:inline></fo:block>
                                <fo:block>Kabag. Klaim</fo:block>
                            </fo:table-cell>             
                            <fo:table-cell ></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

        </fo:flow> 
    </fo:page-sequence>   
</fo:root>   