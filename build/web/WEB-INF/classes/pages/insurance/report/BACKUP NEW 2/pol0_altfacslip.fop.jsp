<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         java.util.Date,
         java.math.BigDecimal,
         com.crux.util.fop.FOPUtil,
         com.crux.lang.LanguageManager,
         com.webfin.entity.model.EntityView"%><?xml version="1.0" encoding="utf-8"?>
<%

            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");

            boolean effective = pol.isEffective();


%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->

        <fo:simple-page-master master-name="only"
                               page-height="33cm"
                               page-width="21cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="1.5cm"
                               margin-right="1.5cm">

            <fo:region-body margin-top="3.5cm" margin-bottom="1.5cm"/>
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

        <fo:static-content flow-name="xsl-region-after"> 
            <fo:block text-align="end"
                      font-size="6pt"

                      line-height="12pt" 
                      font-style="bold">
                FACRE - PT. Asuransi Bangun Askrida
            </fo:block>
            <fo:block text-align="end"
                      font-size="6pt" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>

        </fo:static-content>

        <fo:flow flow-name="xsl-region-body">


            <%
                        int pn = 0;

                        final DTOList objects = pol.getObjects();

                        for (int i = 0; i < objects.size(); i++) {
                            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

                            final DTOList treatyDetails = obj.getTreatyDetails();

                            for (int j = 0; j < treatyDetails.size(); j++) {
                                InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

                                final boolean nonProportional = trd.getTreatyDetail().getTreatyType().isNonProportional();

                                if (!nonProportional) {
                                    continue;
                                }

                                final DTOList shares = trd.getShares();

                                for (int k = 0; k < shares.size(); k++) {
                                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);

                                    final EntityView reasuradur = ri.getEntity();

                                    pn++;

            %>
            <%if (pn > 1) {%>
            <fo:block break-after="page"></fo:block>
            <% }%>

            <!-- defines text title level 1-->
            <% if (!effective) {%>
            <fo:block font-size="16pt" line-height="16pt" color="red" text-align="center">
                {L-ENG FACULTATIVE REINSURANCE OFFER-L}{L-INA PENAWARAN REASURANSI FAKULTATIF-L}
            </fo:block>
            <% } else {%>
            <fo:block font-size="16pt" line-height="16pt" color="black" text-align="center">
                {L-ENG FACULTATIVE REINSURANCE SLIP-L}{L-INA SLIP REASURANSI FAKULTATIF-L}
            </fo:block>
            <% }%>

            <fo:block font-size="<%=fontsize%>" line-height="16pt" space-after.optimum="10pt" color="black" text-align="center"> NO. <%=JSPUtil.xmlEscape(ri.getStRISlipNo())%>
            </fo:block>

            <!-- Normal text -->

            <!-- DYNAMIC HEADER WORDING -->


            <!-- Normal text -->

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
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%= JSPUtil.xmlEscape(pol.getHoldingCompany().getStEntityName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG The Reinsurer -L}{L-INA Reasuradur -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= JSPUtil.xmlEscape(reasuradur.getStEntityName())%></fo:block></fo:table-cell>
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
                            <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(pol.getStCustomerName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Address -L}{L-INA Alamat -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(pol.getStCustomerAddress())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Period -L}{L-INA Jangka Waktu -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodStart(), "d ^^ yyyy") + " {L-ENG Up To-L}{L-INA Sampai-L} " + DateUtil.getDateStr(pol.getDtPeriodEnd(), "d ^^ yyyy")%></fo:block></fo:table-cell>
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
                                                                            out.print(JSPUtil.xmlEscape(cov.getStInsuranceCoverDesc()));
                                    %>
                                    <% }%>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <% if (pol.getStCoverTypeCode().equalsIgnoreCase("COINSIN")) {%>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Co-Insurance -L}{L-INA Ko-Asuransi -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>

                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="70mm"/>
                                        <fo:table-column column-width="2mm"/>
                                        <fo:table-column />
                                        <fo:table-body>

                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>PT ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block><%=JSPUtil.print(pol.getDbSharePct(), 2)%> %</fo:block></fo:table-cell>
                                            </fo:table-row>

                                        </fo:table-body>
                                    </fo:table>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <% 	final DTOList suminsureds2 = obj.getSuminsureds();
                             if (suminsureds2.size() > 1) {
                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Total Sum Insured -L}{L-INA Jumlah Pertanggungan -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="10mm"/>
                                        <fo:table-column column-width="30mm"/>
                                        <fo:table-column column-width="5mm"/>
                                        <fo:table-column column-width="20mm"/>
                                        <fo:table-column column-width="10mm"/>                                        
                                        <fo:table-column column-width="30mm"/>
                                        <fo:table-column />
                                        <fo:table-body>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(pol.getStCurrencyCode())%> </fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>{L-ENG part of -L}{L-INA bagian dari -L}</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%> </fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.div(pol.getDbInsuredAmount(), BDUtil.getRateFromPct(pol.getDbSharePct())), 2)%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell>
                                            </fo:table-row>
                                        </fo:table-body>
                                    </fo:table>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>

                        <% } else {%>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Co-Insurance -L}{L-INA Ko-Asuransi -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>

                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="70mm"/>
                                        <fo:table-column column-width="2mm"/>
                                        <fo:table-column />
                                        <fo:table-body>
                                            <%
                                                 final DTOList coins = pol.getCoins2();
                                                 for (int m = 0; m < coins.size(); m++) {
                                                     InsurancePolicyCoinsView co = (InsurancePolicyCoinsView) coins.get(m);

                                            %>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(co.getStEntityName())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block><%=JSPUtil.printX(co.getDbSharePct(), 2)%> %</fo:block></fo:table-cell>
                                            </fo:table-row>
                                            <% }%>
                                        </fo:table-body>
                                    </fo:table>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <% 	final DTOList suminsureds = obj.getSuminsureds();
                             if (suminsureds.size() > 1) {
                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Total Sum Insured -L}{L-INA Jumlah Pertanggungan -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="10mm"/>
                                        <fo:table-column column-width="30mm"/>
                                        <fo:table-column column-width="10mm"/>
                                        <fo:table-column column-width="25mm"/>
                                        <fo:table-column />
                                        <fo:table-body>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%> </fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                        </fo:table-body>
                                    </fo:table>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>

                        <% }%>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Sum Insured -L}{L-INA Harga Pertanggungan -L} </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="55mm"/>
                                        <fo:table-column column-width="8mm"/>
                                        <fo:table-column column-width="30mm"/>
                                        <fo:table-column />
                                        <fo:table-body>
                                            <% final DTOList suminsureds3 = obj.getSuminsureds();
                                                                                for (int l = 0; l < suminsureds3.size(); l++) {
                                                                                    InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds3.get(l);



                                            %>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block space-after.optimum="10pt"><%=JSPUtil.xmlEscape(tsi.getStDescriptionAuto())%></fo:block></fo:table-cell>
                                                <%if (Tools.compare(tsi.getDbInsuredAmount(), BDUtil.zero) == 0 || tsi.getDbInsuredAmount() == null) {%>
                                                <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                                                <% } else {%>
                                                <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(tsi.getDbInsuredAmount(), 2)%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                                                <% }%>
                                            </fo:table-row>
                                            <% }%>
                                        </fo:table-body>
                                    </fo:table>

                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                            final FlexFieldHeaderView objectMap = pol.getPolicyType().getObjectMap();

                                                            final DTOList objectMapDetails = objectMap == null ? null : objectMap.getReportHidden();

                                                            final InsurancePolicyObjDefaultView io = obj;

                        %>
                        <%

                                                            if (objectMapDetails != null)
                                                                for (int o = 0; o < objectMapDetails.size(); o++) {
                                                                    FlexFieldDetailView iomd = (FlexFieldDetailView) objectMapDetails.get(o);

                                                                    final Object desc = iomd.getDesc(io);



                        %>          

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%=JSPUtil.xmlEscape(iomd.getStFieldDesc())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                                                                                        }
                        %>



                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Reinsurances Share -L}{L-INA Saham Reasuradur -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="35mm"/>
                                        <fo:table-column column-width="2mm"/>
                                        <fo:table-column column-width="10mm"/>
                                        <fo:table-column column-width="25mm"/>
                                        <fo:table-column />
                                        <fo:table-body>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(ri.getDbTSIAmount(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                        </fo:table-body>
                                    </fo:table>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG Rate of Premium -L}{L-INA Suku Premi -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <%
                                                                        if (shares.size() >= 1) {
                                    %>

                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="70mm"/>
                                        <fo:table-column column-width="2mm"/>
                                        <fo:table-column />
                                        <fo:table-body>
                                            <%--  <%
   for (int z = 0; z < shares.size(); z++) {
      InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(z);

%>--%>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(reasuradur.getStEntityName())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block><%=JSPUtil.printX(ri.getDbPremiRate(), 5)%> %</fo:block></fo:table-cell>
                                            </fo:table-row>
                                            <%-- <% } %> --%>
                                        </fo:table-body>
                                    </fo:table>
                                    <% } else {%>
                                    NONE
                                    <% }%>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <% if (Tools.compare(ri.getDbRICommRate(), new BigDecimal(0)) > 0) {%>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG RI Commission -L}{L-INA Komisi Reasuransi -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(ri.getDbRICommRate(), 2)%> %</fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>

                        <% if (Tools.isEqual(ri.getDbRICommRate(), new BigDecimal(0))) {%>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG RI Commission -L}{L-INA Komisi Reasuransi -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>NONE</fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>

                        <% if (ri.getDbRICommRate() == null) {%>
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG RI Commission -L}{L-INA Komisi Reasuransi -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>NONE</fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG R e m a r k -L}{L-INA Keterangan -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(ri.getStNotes())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Reinsurance Premium -L}{L-INA Premi Reasuransi -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="35mm"/>
                                        <fo:table-column column-width="2mm"/>
                                        <fo:table-column column-width="10mm"/>
                                        <fo:table-column column-width="25mm"/>

                                        <fo:table-column />
                                        <fo:table-body>

                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Premium-L}{L-INA Premi-L}</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(ri.getDbPremiAmount(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>

                                            <% if (Tools.compare(ri.getDbRICommRate(), new BigDecimal(0)) > 0) {%>

                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Commission-L}{L-INA Komisi-L} <%=JSPUtil.print(ri.getDbRICommRate(), 2)%>%</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(ri.getDbRICommAmount(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>

                                            <% } else {%>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Commission-L}{L-INA Komisi-L}</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end">--</fo:block></fo:table-cell>
                                            </fo:table-row>
                                            <% }%>                    

                                            <fo:table-row>
                                                <fo:table-cell ></fo:table-cell>
                                                <fo:table-cell ></fo:table-cell>
                                                <fo:table-cell number-columns-spanned="2" >
                                                    <fo:block border-width="0.50pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="0.75pt"></fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>

                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Nett Premium-L}{L-INA Premi Netto-L}</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>

                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(ri.getDbPremiNet(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>

                                            <fo:table-row>
                                                <fo:table-cell ></fo:table-cell>
                                                <fo:table-cell ></fo:table-cell>
                                                <fo:table-cell number-columns-spanned="2" >
                                                    <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="0.75pt"></fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>


                                        </fo:table-body>
                                    </fo:table>

                                </fo:block></fo:table-cell>
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

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="150mm"/>
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center" space-before.optimum="20pt">JAKARTA, <%=DateUtil.getDateStr(new Date(), "d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>

                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block>


            <%
                                }
                            }

                        }

            %>

            <fo:block id="end-of-document"><fo:marker
                    marker-class-name="term">
                    <fo:instream-foreign-object>
                        <svg xmlns="http://www.w3.org/2000/svg" width="15cm" height="1cm" xml:space="preserve">     
                            <rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>

        </fo:flow>
    </fo:page-sequence>
</fo:root>



