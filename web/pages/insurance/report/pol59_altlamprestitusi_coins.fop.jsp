<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         java.util.Date,
         com.crux.util.fop.FOPUtil,
         com.webfin.entity.model.EntityView,
         com.crux.common.parameter.Parameter,
         java.math.BigDecimal"%><?xml version="1.0" encoding="utf-8"?>
<%

            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");
            final String attached = (String) request.getAttribute("attached");
            boolean tanpaTanggal = attached.equalsIgnoreCase("3") ? false : true;

            String no_polis = pol.getStPolicyNo();

            String no_polis_cetak = no_polis.substring(0, 4) + "-" + no_polis.substring(4, 8) + "-" + no_polis.substring(8, 12) + "-" + no_polis.substring(12, 16) + "-" + no_polis.substring(16, 18);

            boolean effective = pol.isEffective();

%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-height="21.5cm"
                               page-width="34cm"
                               margin-top="2.5cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">

        <!-- usage of page layout -->
        <!-- header -->
        <fo:static-content flow-name="xsl-region-before">
            <fo:block text-align="end"
                      font-size="6pt"
                      font-family="serif"
                      line-height="12pt" >
                PT. Asuransi Bangun Askrida Page:<fo:page-number/>
            </fo:block>
        </fo:static-content>

        <fo:static-content flow-name="xsl-region-after">
            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="0pt" space-after.optimum="5pt"></fo:block>
        </fo:static-content>

        <fo:flow flow-name="xsl-region-body">

            <%
                        DTOList coins = pol.getCoins2();
                        String member = null;
                        BigDecimal rate = new BigDecimal(0);
            %>    
            <!-- defines text title level 1-->
            <% if (!effective) {%>
            <fo:block font-size="16pt" font-family="TAHOMA"
                      line-height="16pt" space-after.optimum="0pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>

            <fo:block font-size="16pt" font-family="TAHOMA" line-height="5pt" space-after.optimum="5pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                ENDORSEMENT
            </fo:block>

            <fo:block font-size="16pt" font-family="TAHOMA" line-height="5pt" space-after.optimum="5pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                No Polis : <%= no_polis_cetak%>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="5pt" space-after.optimum="5pt"
                      color="black"
                      text-align="left"
                      padding-top="10pt">
            </fo:block>

            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-after.optimum="10pt"></fo:block>

            <%
                        String bw = "0.5pt";
                        int pt = 11;
            %> 
            <!-- Normal text -->

            <!-- defines text title level 1-->
            <fo:block font-size="10pt" font-family="TAHOMA">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="42mm"/>
                    <fo:table-column column-width="4mm"/>
                    <fo:table-column />
                    <fo:table-body>         

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG On Behalf of-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>  <%=JSPUtil.xmlEscape(pol.getStCustomerName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>  <%=JSPUtil.xmlEscape(pol.getStCustomerAddress())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>Objek Pertanggungan</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block> Asuransi PA Kreasi</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>Total Nilai Pertanggungan</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block> Rp.   <%=JSPUtil.print(pol.getDbInsuredAmount(), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="10pt" font-family="TAHOMA" line-height="5pt" space-after.optimum="5pt"
                      color="black"
                      text-align="left"
                      padding-top="10pt">
                Dengan ini dicatat dan disetujui bahwa : 
            </fo:block>

            <fo:block font-size="10pt" font-family="TAHOMA" line-height="5pt" space-after.optimum="5pt"
                      color="black"
                      text-align="left"
                      padding-top="10pt">
                Sesuai permintaan Tertanggung, terhitung mulai tanggal pelunasan, Debitur atas Nama tersebut dibatalkan 
            </fo:block>

            <fo:block font-size="10pt" font-family="TAHOMA" line-height="5pt" space-after.optimum="15pt"
                      color="black"
                      text-align="left"
                      padding-top="10pt">
                Maka kepada Tertanggung dikembalikan Premi dengan perhitungan dibawah ini :
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="8mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="16mm"/>
                    <fo:table-column column-width="8mm"/>
                    <fo:table-column column-width="37mm"/>
                    <fo:table-column column-width="16mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="21mm"/>
                    <fo:table-column column-width="21mm"/>
                    <fo:table-column column-width="21mm"/>
                    <%
                                for (int k = 0; k < coins.size(); k++) {
                                    InsurancePolicyCoinsView coi = (InsurancePolicyCoinsView) coins.get(k);
                    %>
                    <fo:table-column column-width="21mm"/>
                    <% }%>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="<%=pt + coins.size()%>">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5t" space-after.optimum="0pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >{L-ENG No. -L}{L-INA No. -L}</fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >{L-ENG Name of Member -L}{L-INA Nama Peserta -L}</fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >{L-ENG Birth Date -L}{L-INA Tanggal Lahir -L}</fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >{L-ENG Year -L}{L-INA Usia -L}</fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >{L-ENG Time Length-L}{L-INA Jangka Waktu -L}</fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >Tanggal Pelunasan</fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >Sisa Kontrak</fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >{L-ENG Time -L}{L-INA Lama (Bulan) -L}</fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >{L-ENG Sum Insured -L}{L-INA Harga Pertanggungan -L}</fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >{L-ENG Premium -L}{L-INA Premi -L}</fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >{L-ENG Premium -L}{L-INA Pengembalian Premi -L}</fo:block></fo:table-cell>
                            <%
                                        for (int k = 0; k < coins.size(); k++) {
                                            InsurancePolicyCoinsView coi = (InsurancePolicyCoinsView) coins.get(k);
                            %>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid" border-right-style="solid"><fo:block text-align="center" line-height="5mm" ><%=JSPUtil.printX(coi.getEntity().getStShortName().toUpperCase())%></fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="<%=pt + coins.size()%>">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                    DTOList objects = pol.getObjects();
                                    BigDecimal premiTotal = null;

                                    BigDecimal[] t = new BigDecimal[coins.size()];

                                    for (int i = 0; i < objects.size(); i++) {
                                        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

                                        if (BDUtil.isZeroOrNull(obj.getDbObjectPremiTotalBeforeCoinsuranceAmount())) {
                                            continue;
                                        }

                                        premiTotal = BDUtil.add(premiTotal, obj.getDbObjectPremiTotalBeforeCoinsuranceAmount());

                        %>

                        <fo:table-row>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" ><%=JSPUtil.printX(String.valueOf(i + 1))%></fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block line-height="5mm" ><%=JSPUtil.printX(obj.getStReference1())%></fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" ><%=JSPUtil.printX(obj.getDtReference1())%></fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" ><%=JSPUtil.printX(obj.getStReference2())%></fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" ><%=JSPUtil.printX(obj.getDtReference2())%> s/d <%=JSPUtil.printX(obj.getDtReference3())%></fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" ><%=JSPUtil.printX(obj.getDtReference5())%></fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" ><%=JSPUtil.printX(obj.getStReference9())%> </fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" ><%=JSPUtil.printX(obj.getStReference5())%></fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="end" line-height="5mm" ><%=JSPUtil.printX(BDUtil.negate(obj.getDbReference2()), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="end" line-height="5mm" ><%=JSPUtil.printX(obj.getDbReference1(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="end" line-height="5mm" ><%=JSPUtil.printX(BDUtil.negate(obj.getDbObjectPremiTotalBeforeCoinsuranceAmount()), 0)%></fo:block></fo:table-cell>
                            <%
                                                                    BigDecimal coinsAmount = new BigDecimal(0);
                                                                    for (int k = 0; k < coins.size(); k++) {
                                                                        InsurancePolicyCoinsView coi = (InsurancePolicyCoinsView) coins.get(k);

                                                                        coinsAmount = BDUtil.mul(obj.getDbObjectPremiTotalBeforeCoinsuranceAmount(), BDUtil.getRateFromPct(coi.getDbSharePct()));
                                                                        //coinsAmount = coi.getDbPremiAmount();
                                                                        //rate = BDUtil.getRateFromPct(coi.getDbSharePct());
                            %>
                            <%--<fo:table-cell  border-width="<%=bw%>" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm" ><%=JSPUtil.printX(BDUtil.mul(BDUtil.negate(obj.getDbObjectPremiTotalBeforeCoinsuranceAmount()), rate), 0)%></fo:block></fo:table-cell>--%>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm" ><%=JSPUtil.printX(BDUtil.negate(coinsAmount), 0)%></fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>

                        <%
                                    }
                        %>
                        <%--
                                                <fo:table-row>
                                                    <fo:table-cell number-columns-spanned="<%=pt + coins.size()%>">
                                                        <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">TOTAL SELURUH</fo:block></fo:table-cell>
                            <fo:table-cell  border-width="0.5pt" ><fo:block text-align="end"  line-height="5mm"></fo:block></fo:table-cell>
                            <fo:table-cell  border-width="0.5pt" ><fo:block text-align="end"  line-height="5mm"></fo:block></fo:table-cell>
                            <fo:table-cell  border-width="0.5pt" ><fo:block text-align="end"  line-height="5mm"></fo:block></fo:table-cell>
                            <fo:table-cell  border-width="0.5pt" ><fo:block text-align="end"  line-height="5mm"></fo:block></fo:table-cell>
                            <fo:table-cell  border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  line-height="5mm"><%=JSPUtil.printX(BDUtil.negate(premiTotal), 0)%></fo:block></fo:table-cell>
                            <%
                                        for (int k = 0; k < coins.size(); k++) {
                                            InsurancePolicyCoinsView coi = (InsurancePolicyCoinsView) coins.get(k);

                                            //rate = BDUtil.getRateFromPct(coi.getDbSharePct());
                            %>
                            <fo:table-cell  border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  line-height="5mm"><%=JSPUtil.printX(BDUtil.negate(t[k++]), 0)%></fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>
                        --%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="<%=pt + coins.size()%>">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
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

            <fo:block font-size="<%=fontsize%>" space-before.optimum="20pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="150mm"/>
                    <fo:table-column column-width="100mm"/>
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L} 
                                    <%= JSPUtil.printX(pol.getStCostCenterDesc())%><%if (tanpaTanggal) {%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%></fo:block>
                                    <% } else {%>
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L} 
                                    <%= JSPUtil.printX(pol.getCostCenter(pol.getCostCenter(pol.getStCostCenterCode()).getStSubCostCenterCode()).getStDescription())%><%if (tanpaTanggal) {%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%></fo:block>
                                    <% }%>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>

                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" space-before.optimum="45pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="150mm"/>
                    <fo:table-column column-width="100mm"/>
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <% if (Parameter.readString("BRANCH_SIGN_" + pol.getStCostCenterCode()) != null) {%>
                                <fo:block text-align="center"><%= Parameter.readString("BRANCH_SIGN_" + pol.getStCostCenterCode())%></fo:block>
                                <%} else {%>
                                <fo:block text-align="center"></fo:block>
                                <%}%>
                                <fo:block text-align="center"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block>

        </fo:flow>
    </fo:page-sequence>

</fo:root>
