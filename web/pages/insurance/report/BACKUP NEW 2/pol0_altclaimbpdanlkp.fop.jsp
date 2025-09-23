<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         java.util.Date,
         java.math.BigDecimal,
         com.crux.util.fop.FOPUtil,
         com.crux.lang.LanguageManager,
         com.crux.web.controller.SessionManager,
         com.crux.common.parameter.Parameter,
         com.webfin.entity.model.EntityView"%><?xml version="1.0" encoding="utf-8"?>
<%

            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");
            final String otorized = (String) request.getAttribute("authorized");

            boolean effective = pol.isEffective();


%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->

        <fo:simple-page-master master-name="only"
                               page-height="30cm"
                               page-width="21cm"
                               margin-top="4cm"
                               margin-bottom="1cm"
                               margin-left="1cm"
                               margin-right="1cm">

            <fo:region-body margin-top="3cm" margin-bottom="0cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="1cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">

        <!-- usage of page layout -->
        <!-- HEADER -->

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
        </fo:static-content>

        <fo:flow flow-name="xsl-region-body">      


            <%
                        int pn = 0;

                        final DTOList objects = pol.getObjectsClaim();

                        for (int i = 0; i < objects.size(); i++) {
                            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

                            final DTOList treatyDetails = obj.getTreatyDetails();

                            for (int j = 0; j < treatyDetails.size(); j++) {
                                InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                                String nonProportional = trd.getTreatyDetail().getTreatyType().getStInsuranceTreatyTypeName();

                                if (!nonProportional.equalsIgnoreCase("BPPDAN")) {
                                    continue;
                                }

                                pn++;

            %>            


            <%if (pn > 1) {%>
            <fo:block break-after="page"></fo:block>
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

            <!-- defines text title level 1-->
            <fo:block font-size="16pt" line-height="16pt" color="black" text-align="center">
                {L-ENG DEFINITE LOSS ADVICE -L}{L-INA LAPORAN KERUGIAN PASTI-L}
            </fo:block> 

            <fo:block font-size="<%=fontsize%>" line-height="16pt" space-after.optimum="10pt" color="black" text-align="center"> NO. <%=JSPUtil.printX(pol.getStDLANo())%>
            </fo:block>

            <!-- Normal text -->

            <!-- DYNAMIC HEADER WORDING -->


            <!-- Normal text -->
            <% if (pol.isStatusClaimDLA()) {%>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <!-- GARIS DALAM KOLOM -->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG The Insurer -L}{L-INA Penanggung -L}</fo:block></fo:table-cell>             
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%= pol.getHoldingCompany().getStEntityName()%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG The Reinsurer -L}{L-INA Reasuradur -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>BPPDAN</fo:block></fo:table-cell>
                        </fo:table-row>

                        <!-- GARIS DALAM KOLOM -->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Policy Number-L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Type of Insurance -L}{L-INA Jenis Asuransi -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStPolicyTypeDesc2()%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Name of Insured -L}{L-INA Nama Tertanggung -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Address -L}{L-INA Alamat -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerAddress())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Period -L}{L-INA Jangka Waktu -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(DateUtil.getDateStr(pol.getDtPeriodStart(), "d ^^ yyyy") + " {L-ENG Up To-L}{L-INA Sampai-L} ") + DateUtil.getDateStr(pol.getDtPeriodEnd(), "d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Coverage -L}{L-INA Penutupan -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <%
                                         final DTOList covers = obj.getCoverage();

                                         for (int m = 0; m < covers.size(); m++) {
                                             InsurancePolicyCoverView cov = (InsurancePolicyCoverView) covers.get(m);

                                             if (m > 0) {
                                                 out.print("; ");
                                             }
                                             out.print(JSPUtil.print(cov.getStInsuranceCoverDesc()));
                                    %>
                                    <% }%>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Sum Insured -L}{L-INA Harga Pertanggungan -L} </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="35mm"/>
                                        <fo:table-column column-width="2mm"/>
                                        <fo:table-column column-width="10mm"/>
                                        <fo:table-column column-width="35mm"/>
                                        <fo:table-body>

                                            <% final DTOList suminsureds3 = obj.getSuminsureds();
                                                 for (int l = 0; l < suminsureds3.size(); l++) {
                                                     InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds3.get(l);
                                            %>

                                            <fo:table-row>
                                                <fo:table-cell ><fo:block space-after.optimum="10pt"><%=JSPUtil.printX(tsi.getStDescriptionAuto())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(tsi.getDbInsuredAmount(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                            <% }%>
                                        </fo:table-body>
                                    </fo:table>

                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <%--
                        <%
                        final FlexFieldHeaderView objectMap = pol.getPolicyType().getObjectMap();

                        final DTOList objectMapDetails = objectMap==null?null:objectMap.getReportHidden();

                        final InsurancePolicyObjDefaultView io = obj;

                        %>
                        <%

                        if (objectMapDetails!=null)
                            for (int o = 0; o < objectMapDetails.size(); o++) {
                            FlexFieldDetailView iomd = (FlexFieldDetailView) objectMapDetails.get(o);

                            final Object desc = iomd.getDesc(io);

                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%=JSPUtil.printX(iomd.getStFieldDesc())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                            }
                        %>
                        --%>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Occupation-L}{L-INA Penggunaan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(obj.getStReference1())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Class Construction-L}{L-INA Kelas Konstruksi-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getStRiskClass())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Electricity-L}{L-INA Penerangan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=LanguageManager.getInstance().translate(obj.getStReference4Desc())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Risk Location-L}{L-INA Alamat Risiko-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getStReference5())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Claim Date-L}{L-INA Tanggal Klaim-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(DateUtil.getDateStr(pol.getDtClaimDate(), "d ^^ yyyy"))%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Claim Event Location-L}{L-INA Lokasi Terjadi Klaim-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimEventLocation())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% DTOList cause = pol.getClaimCause();
                             for (int b = 0; b < cause.size(); b++) {
                                 InsuranceClaimCauseView cau = (InsuranceClaimCauseView) cause.get(b);
                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Claim Cause-L}{L-INA Penyebab Klaim-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(cau.getStDescription())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Settled Claim Amount -L}{L-INA Jumlah Kerugian Pasti-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="35mm"/>
                                        <fo:table-column column-width="2mm"/>
                                        <fo:table-column column-width="10mm"/>
                                        <fo:table-column column-width="35mm"/>
                                        <fo:table-body>
                                            <%

                                                 final DTOList claimItems = pol.getClaimItems();

                                                 BigDecimal amt3 = new BigDecimal(0);
                                                 for (int z = 0; z < claimItems.size(); z++) {
                                                     InsurancePolicyItemsView ci = (InsurancePolicyItemsView) claimItems.get(z);

                                                     if (ci.getStInsItemID().equalsIgnoreCase("61")) {
                                                         continue;
                                                     }

                                                     final boolean negative = ci.getInsItem().getARTrxLine().isNegative();

                                                     if (negative) {
                                                         amt3 = BDUtil.sub(amt3, ci.getDbAmount());
                                                     } else {
                                                         amt3 = BDUtil.add(amt3, ci.getDbAmount());
                                                     }

                                                     String amt = JSPUtil.printX(ci.getDbAmount(), 2);

                                                     if (negative) {
                                                         amt = "(" + amt + ")";
                                                     }

                                            %>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block><%=JSPUtil.printX(ci.getStDescription2())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimCurrency())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=amt%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                            <%
                                                 }
                                            %>

                                            <fo:table-row>
                                                <fo:table-cell number-columns-spanned="2"></fo:table-cell>		
                                                <fo:table-cell number-columns-spanned="2">
                                                    <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="1pt"></fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>

                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Nett Claim-L}{L-INA Netto-L}</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimCurrency())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(amt3, 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>                                            

                                        </fo:table-body>
                                    </fo:table>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt" space-after.optimum="10pt">{L-ENG Your Share in Claim -L}{L-INA Saham Saudara dalam Klaim-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-before.optimum="10pt" space-after.optimum="10pt">:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-before.optimum="10pt" space-after.optimum="10pt">
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="35mm"/>
                                        <fo:table-column column-width="2mm"/>
                                        <fo:table-column column-width="10mm"/>
                                        <fo:table-column column-width="35mm"/>
                                        <fo:table-body>
                                            <fo:table-row>
                                                <fo:table-cell number-columns-spanned="2" ><fo:block ></fo:block></fo:table-cell>                           
                                                <fo:table-cell ><fo:block ><%=JSPUtil.printX(pol.getStClaimCurrency())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(trd.getDbClaimAmount(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                        </fo:table-body>
                                    </fo:table>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG R e m a r k -L}{L-INA Keterangan -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="6pt"
                      font-family="sans-serif"
                      line-height="10pt"
                      space-after.optimum="10pt"
                      text-align="left" >
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "dd/MM/yyyy HH:mm:ss"))%> PrintStamp:<%=pol.getStPrintCode()%> PrintCode:<%=pol.getStPrintStamp()%> Page:<fo:page-number/> Terlampir : [<%=JSPUtil.print(request.getAttribute("DOCUMENT_NAME"))%>]
            </fo:block>

            <%
                 boolean isSubrogasi = false;
                 String namaKabag = null;
                 String jabatanKabag = null;
                 String divisiKabag = null;
                 for (int k = 0; k < claimItems.size(); k++) {
                     InsurancePolicyItemsView ci = (InsurancePolicyItemsView) claimItems.get(k);

                     if (ci.getInsItem().getStInsuranceItemID().equalsIgnoreCase("48")) {
                         isSubrogasi = true;
                     }

                     if (isSubrogasi) {
                         namaKabag = Parameter.readString("KABAG_SUBROGRASI");
                         jabatanKabag = "Kabag. Subrogasi";
                         divisiKabag = "SUBROGASI";
                     } else {
                         namaKabag = Parameter.readString("KABAG_KLAIM");
                         jabatanKabag = "Kabag. Klaim";
                         divisiKabag = "KLAIM";
                     }
                 }
            %>

            <% if (otorized.equalsIgnoreCase("kasie")) {%> 

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>

                        <%
                             if (SessionManager.getInstance().getSession().getStBranch() != null) {
                        %>     

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><%= JSPUtil.printX(pol.getStCostCenterDesc())%>, <%=DateUtil.getDateStr(pol.getDtDLADate(), "d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                            </fo:table-cell>
                        </fo:table-row>           

                        <fo:table-row>
                            <fo:table-cell number-column-display="2">           
                                <fo:block text-align="start">
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(pol.getDbClaimAmount(), 2)%>" orientation="0">
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
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("BRANCH_SIGN_" + SessionManager.getInstance().getSession().getStBranch())%></fo:inline></fo:block>
                                <fo:block text-align="center">Pimpinan</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                                } else {
                        %>     

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(pol.getDtDLADate(), "d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block text-align="center">MENYETUJUI,</fo:block>    
                                <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7") || pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) {%>
                                <fo:block text-align="center">BAGIAN BONDING,</fo:block>
                                <% } else {%>
                                <fo:block text-align="center">BAGIAN KLAIM,</fo:block>
                                <% }%>    
                            </fo:table-cell>
                        </fo:table-row>          

                        <fo:table-row>
                            <fo:table-cell number-column-display="2">           
                                <fo:block text-align="start">
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(pol.getDbClaimAmount(), 2)%>" orientation="0">
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
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("KASIE_KLAIM1")%></fo:inline></fo:block>
                                <fo:block text-align="center">Kasie. Klaim</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <% }%>

                    </fo:table-body>
                </fo:table>
            </fo:block>
            <% }%>

            <% if (otorized.equalsIgnoreCase("kabag")) {%> 

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>

                        <%
                             if (SessionManager.getInstance().getSession().getStBranch() != null) {
                        %>     

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><%= JSPUtil.printX(pol.getStCostCenterDesc())%>, <%=DateUtil.getDateStr(pol.getDtDLADate(), "d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                            </fo:table-cell>
                        </fo:table-row>           

                        <fo:table-row>
                            <fo:table-cell number-column-display="2">           
                                <fo:block text-align="start">
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(pol.getDbClaimAmount(), 2)%>" orientation="0">
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
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("BRANCH_SIGN_" + SessionManager.getInstance().getSession().getStBranch())%></fo:inline></fo:block>
                                <fo:block text-align="center">Pimpinan</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                                } else {
                        %>     

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(pol.getDtDLADate(), "d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7") || pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) {%>
                                <fo:block text-align="center">BAGIAN BONDING,</fo:block>
                                <% } else {%>
                                <fo:block text-align="center">BAGIAN KLAIM,</fo:block>
                                <% }%>    
                            </fo:table-cell>
                        </fo:table-row>             

                        <fo:table-row>
                            <fo:table-cell number-column-display="2">           
                                <fo:block text-align="start">
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(pol.getDbClaimAmount(), 2)%>" orientation="0">
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
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7") || pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) {%>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">TEPANUS HUTABARAT</fo:inline></fo:block>
                                <fo:block text-align="center">Kabag. Bonding</fo:block> 
                                <% } else {%>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">JATMIKO</fo:inline></fo:block>
                                <fo:block text-align="center">Kabag. Klaim</fo:block> 
                                <% }%>
                            </fo:table-cell>
                        </fo:table-row>

                        <% }%>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% }%>

            <% if (otorized.equalsIgnoreCase("kadiv")) {%> 

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(pol.getDtDLADate(), "d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">MENYETUJUI,</fo:block></fo:table-cell>             
                            <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7") || pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) {%>
                            <fo:table-cell ><fo:block text-align="center">BAGIAN BONDING,</fo:block></fo:table-cell> 
                            <% } else {%>
                            <fo:table-cell ><fo:block text-align="center">BAGIAN KLAIM,</fo:block></fo:table-cell> 
                            <% }%>    
                        </fo:table-row>         

                        <fo:table-row>
                            <fo:table-cell number-column-display="3">           
                                <fo:block text-align="start">
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(pol.getDbClaimAmount(), 2)%>" orientation="0">
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
                                <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7") || pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) {%>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">BUDI PRANOTO</fo:inline></fo:block>
                                <fo:block text-align="center">Kadiv. UnderWriting dan Reas Facultatif</fo:block>
                                <% } else {%>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">HENRY ANANDA SIREGAR</fo:inline></fo:block>
                                <fo:block text-align="center">Kadiv. Klaim</fo:block>
                                <% }%>
                            </fo:table-cell>             
                            <fo:table-cell >
                                <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7") || pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) {%>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">TEPANUS HUTABARAT</fo:inline></fo:block>
                                <fo:block text-align="center">Kabag. Bonding</fo:block> 
                                <% } else {%>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">JATMIKO</fo:inline></fo:block>
                                <fo:block text-align="center">Kabag. Klaim</fo:block> 
                                <% }%>
                            </fo:table-cell>             
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% }%>

            <% if (otorized.equalsIgnoreCase("dirtek")) {%> 

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="55mm"/>
                    <fo:table-column column-width="55mm"/>
                    <fo:table-column column-width="70mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(pol.getDtDLADate(), "d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">BAGIAN <%=JSPUtil.printX(divisiKabag)%>,</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="center">MENYETUJUI,</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">MENYETUJUI,</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">MENYETUJUI,</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-display="3">           
                                <fo:block text-align="start">
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(pol.getDbClaimAmount(), 2)%>" orientation="0">
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
                                <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7") || pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) {%>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">BUDI PRANOTO</fo:inline></fo:block>
                                <fo:block text-align="center">Kadiv. UnderWriting dan Reas Facultatif</fo:block>
                                <% } else {%>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">HENRY ANANDA SIREGAR</fo:inline></fo:block>
                                <fo:block text-align="center">Kadiv. Klaim</fo:block>
                                <% }%>
                            </fo:table-cell>    
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("DIRTEK")%></fo:inline></fo:block>
                                <fo:block text-align="center">Direktur Teknik</fo:block>
                            </fo:table-cell>        
                            <fo:table-cell >
                                <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7") || pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) {%>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">TEPANUS HUTABARAT</fo:inline></fo:block>
                                <fo:block text-align="center">Kabag. Bonding</fo:block> 
                                <% } else {%>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">JATMIKO</fo:inline></fo:block>
                                <fo:block text-align="center">Kabag. Klaim</fo:block> 
                                <% }%>
                            </fo:table-cell>             
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% }%>

            <% }%>

            <%

                            }

                        }

            %>


        </fo:flow>
    </fo:page-sequence>
</fo:root>



