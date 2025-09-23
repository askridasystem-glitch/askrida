<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         java.util.Date,
         com.crux.util.Tools,
         com.crux.util.fop.FOPUtil,
         com.webfin.entity.model.EntityView,
         com.crux.common.parameter.Parameter,
         java.math.BigDecimal"%><?xml version="1.0" encoding="utf-8"?>
<%

            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");
            final String attached = (String) request.getAttribute("attached");

            String param[] = attached.split("[\\|]");

            boolean isAttached = param[0].equalsIgnoreCase("2") ? true : false;
            boolean tanpaTanggal = param[0].equalsIgnoreCase("3") ? true : false;
            boolean tanpaNama = param[0].equalsIgnoreCase("5") ? true : false;
            boolean tanpaNamaTanggal = param[0].equalsIgnoreCase("6") ? true : false;
            boolean tanpaRate = param[0].equalsIgnoreCase("7") ? true : false;
            boolean klausulaTerlampir = param[0].equalsIgnoreCase("8") ? true : false;

            if (attached.length() > 1) {
                if (param[1] != null) {
                    isAttached = !isAttached ? param[1].equalsIgnoreCase("2") ? true : false : isAttached;
                    tanpaTanggal = !tanpaTanggal ? param[1].equalsIgnoreCase("3") ? true : false : tanpaTanggal;
                    tanpaRate = !tanpaRate ? param[1].equalsIgnoreCase("7") ? true : false : tanpaRate;
                }
            }

            final String preview = (String) request.getAttribute("preview");
            boolean isPreview = false;
            if (preview != null) {
                if (preview.equalsIgnoreCase("Preview")) {
                    isPreview = true;
                }
            }

            boolean effective = pol.isEffective();

            final String digitalsign = (String) request.getAttribute("digitalsign");
            boolean usingDigitalSign = false;
            if (digitalsign != null) {
                if (digitalsign.equalsIgnoreCase("Y")) {
                    usingDigitalSign = true;
                }
            }

            boolean isUsingBarcode = Parameter.readString("REPORT_SIGN_TYPE").equalsIgnoreCase("BARCODE");
            /*
            String originalWatermarkPath = Parameter.readString("DIGITAL_NOTA_ORI_PIC");
            String duplicateWatermarkPath = Parameter.readString("DIGITAL_NOTA_DUPLICATE_PIC");
            String copyWatermarkPath = Parameter.readString("DIGITAL_NOTA_COPY_PIC");
            String askridaLogoPath = Parameter.readString("DIGITAL_POLIS_LOGO_PIC");
            String askridaLogoBlackWhitePath = Parameter.readString("DIGITAL_POLIS_LOGOBW_PIC");
            String alamatLogoPath = Parameter.readString("DIGITAL_NOTA_ALAMAT_PIC");
            String alamatLogoBlackWhitePath = Parameter.readString("DIGITAL_NOTA_ALAMATBW_PIC");
            */

            String originalWatermarkPath = Parameter.readString("DIGITAL_POLIS_ORI_PIC2");
            String duplicateWatermarkPath = Parameter.readString("DIGITAL_POLIS_DUPLICATE_PIC2");
            String copyWatermarkPath = Parameter.readString("DIGITAL_POLIS_COPY_PIC2");
            String askridaLogoPath = Parameter.readString("DIGITAL_POLIS_LOGO_PIC2");
            String askridaLogoBlackWhitePath = Parameter.readString("DIGITAL_POLIS_LOGOBW_PIC2");
            String alamatLogoPath = Parameter.readString("DIGITAL_POLIS_ALAMAT_PIC2");
            String alamatLogoBlackWhitePath = Parameter.readString("DIGITAL_POLIS_ALAMATBW_PIC2");

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:fox="http://xmlgraphics.apache.org/fop/extensions">

    <%
                String watermarkPath = null;
                String logoAskridaPath = null;
                String alamatAskridaPath = null;

                for (int x = 0; x < 3; x++) {

                    if (x == 0) {
                        watermarkPath = originalWatermarkPath;
                        logoAskridaPath = askridaLogoPath;
                        alamatAskridaPath = alamatLogoPath;
                    } else if (x == 1) {
                        watermarkPath = copyWatermarkPath;
                        logoAskridaPath = askridaLogoBlackWhitePath;
                        alamatAskridaPath = alamatLogoBlackWhitePath;
                    } else if (x == 2) {
                        watermarkPath = duplicateWatermarkPath;
                        logoAskridaPath = askridaLogoBlackWhitePath;
                        alamatAskridaPath = alamatLogoBlackWhitePath;
                    }



    %>

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <!-- komisi cuma 13.5cm -->

        <fo:simple-page-master master-name="first"
                               page-height="30cm"
                               page-width="21cm"
                               margin-top="0.3cm"
                               margin-left="1cm"
                               margin-right="1cm">

            <fo:region-body margin-top="3cm" margin-bottom="3cm" background-image="file:///<%=watermarkPath%>"/>
            <fo:region-before extent="3cm" background-image="file:///<%=logoAskridaPath%>"/>
            <fo:region-after extent="3cm" background-image="file:///<%=alamatAskridaPath%>"/>
        </fo:simple-page-master>

    </fo:layout-master-set>

    <fo:page-sequence master-reference="first">


        <fo:flow flow-name="xsl-region-body">

            <fo:block text-align="center"
                      font-size="16pt"
                      font-family="TAHOMA"
                      line-height="12pt"
                      font-weight="bold"
                      space-before.optimum="7pt">
                {L-ENG INVOICE-L}{L-INA INVOICE-L}
            </fo:block>

            <fo:block space-after.optimum="25pt"></fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="150mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block><fo:inline text-decoration="underline">{L-ENG Policy No-L}{L-INA Nomor Polis-L}</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">Policy No</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block><fo:inline text-decoration="underline">{L-ENG Policy Date-L}{L-INA Tanggal Polis-L}</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">Policy Date</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"><fo:inline text-decoration="underline">{L-ENG Ref. No-L}{L-INA Ref. No-L}</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"><%=JSPUtil.xmlEscape(pol.getStParentPolicy().getStDescription())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block><fo:inline text-decoration="underline">{L-ENG Parent Policy No-L}{L-INA Nomor Polis Induk-L}</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(pol.getStReference2Desc())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">Policy Number (Parent)</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block><fo:inline text-decoration="underline">{L-ENG To-L}{L-INA Nama Tertanggung-L}</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(pol.getStCustomerName())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">The Insured</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block><fo:inline text-decoration="underline">{L-ENG Address-L}{L-INA Alamat-L}</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(pol.getStCustomerAddress())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">Address</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block><fo:inline text-decoration="underline">{L-ENG Insurance Type-L}{L-INA Jenis Asuransi-L}</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(pol.getPolicyType().getStDescription())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">Type Of Insurance</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block><fo:inline text-decoration="underline">{L-ENG Notes-L}{L-INA Catatan-L}/Notes</fo:inline> </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Jumlah premi sebesar tersebut merupakan Asuransi Kredit <%=pol.getObjects().size()%> debitur <%=JSPUtil.xmlEscape(pol.getStCustomerName())%>, dan dapat disetorkan pada rekening giro kami Nomor 120.00000.51099 di Bank Mandiri KCP Cempaka Mas atas nama PT. Asuransi Bangun Askrida</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            

            <% if (isPreview) {%>
            <fo:block font-size="20pt"
                      line-height="16pt"
                      color="blue"
                      text-align="center"
                      padding-top="10pt">
                PREVIEW MODE
            </fo:block>
            <% }%>

            <% if (!effective) {%>
            <fo:block font-size="16pt" font-family="TAHOMA"
                      line-height="16pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>

            

            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="5pt"></fo:block>

            <!-- ROW -->

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="37mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">{L-ENG Details-L}{L-INA Rincian-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">{L-ENG Gross Premium-L}{L-INA Premi Bruto-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end" space-after.optimum="5pt"><%=JSPUtil.printX(pol.getDbPremiTotal(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                                            final DTOList details = pol.getDetails();
                                            BigDecimal disc = new BigDecimal(0);
                                            BigDecimal item = new BigDecimal(0);

                                            for (int i = 0; i < details.size(); i++) {
                                                InsurancePolicyItemsView dtu = (InsurancePolicyItemsView) details.get(i);

                                                if (!dtu.isDiscount()) {
                                                    continue;
                                                }

                                                String rate = "";

                                                if (dtu.isEntryByRate()) {
                                                    rate = JSPUtil.printX(dtu.getDbRate(), 2) + "%";
                                                }

                                                disc = BDUtil.add(disc, dtu.getDbAmount());
                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">{L-ENG Discount-L}{L-INA Diskon-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"><%=rate%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end" space-after.optimum="5pt"><%=JSPUtil.printX(dtu.getDbAmount(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>
                        <!-- GARIS DALAM KOLOM -->

                        <%
                                            for (int i = 0; i < details.size(); i++) {
                                                InsurancePolicyItemsView dti = (InsurancePolicyItemsView) details.get(i);

                                                if (!dti.isFee() || dti.isPPN()) {
                                                    continue;
                                                }
                                                /*
                                                String rate = "";

                                                rate = JSPUtil.printX(det.getDbRate(),2) + "%";
                                                 */
                                                item = BDUtil.add(item, dti.getDbAmount());
                        %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"><%=JSPUtil.printX(dti.getStDescription2())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end" space-after.optimum="5pt"><%=JSPUtil.printX(dti.getDbAmount(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" ><fo:block border-width="0.35pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">{L-ENG Due To Us-L}{L-INA Total Tagihan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end" space-after.optimum="5pt"><%=JSPUtil.printX(BDUtil.sub(BDUtil.add(pol.getDbPremiTotal(), item), disc), 2)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <!-- GARIS DALAM KOLOM -->



                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" space-after.optimum="3pt" line-height="0.15pt"></fo:block>

            <fo:block font-size="<%=fontsize%>" font-weight="bold">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="180mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="left">{L-ENG SAY :-L}{L-INA TERBILANG :-L} <%=NumberSpell.readNumber(JSPUtil.printX(BDUtil.sub(BDUtil.add(pol.getDbPremiTotal(), item), disc), 2), pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <!-- CICILAN -->

            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>" font-family="TAHOMA"
                      line-height="10pt" space-after.optimum="2pt"
                      text-align="justify" > </fo:block>

            <% //if(pol.getStInstallmentPeriods()>=2)
                                {

                                    DTOList installment = pol.getInstallment();

                                    if (installment.size() > 1) {

            %>
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>

                        <!-- INTEREST START -->

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Tanggal</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Jumlah</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="4"><fo:block font-size="<%=fontsize%>" space-after.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%

                                                                for (int i = 0; i < installment.size(); i++) {
                                                                    InsurancePolicyInstallmentView ins = (InsurancePolicyInstallmentView) installment.get(i);

                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>Installment <%=(i + 1)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(ins.getDtDueDate())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(ins.getDbAmount(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>
                    </fo:table-body>
                </fo:table>
            </fo:block>
            <% }%>
            <% }%>
            <!-- ROW -->

            <% if (usingDigitalSign) {%>
            <fo:block font-size="6pt"
                      space-after.optimum="3pt"
                      text-align="justify" >
                Dokumen ini dicetak secara sistem dan dinyatakan sah.
            </fo:block>
            <% }%>

            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="3pt"></fo:block>

            <fo:block font-size="6pt" space-after.optimum="5pt">
                <%if (!tanpaTanggal) {%>
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "dd/MM/yyyy HH:mm:ss"))%>
                <% }%>
                <% if (effective) {%>
                PrintStamp:<%=pol.getStPrintCode()%> PrintCode:<%=pol.getStPrintStamp()%> Page:<fo:page-number/> Cetakan : <%=pol.getStPrintCounter()%>
                <% }%>
                <% if (usingDigitalSign) {%>
                <%if (!isUsingBarcode) {%>  [<%=JSPUtil.print(pol.getStSignCode())%>] <%}%>
                <% }%>
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="100mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L}
                                    <%= JSPUtil.printX(pol.getCostCenter(pol.getCostCenter(pol.getStCostCenterCode()).getStSubCostCenterCode()).getStDescription())%><%if (!tanpaTanggal) {%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <%if (!isUsingBarcode) {%><fo:block text-align="center">{L-ENG Authorized-L}{L-INA Tanda Tangan Yang Berwenang-L}</fo:block><%}%>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

             


            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="70mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-body>

                        <% if (usingDigitalSign) {%>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="KANTOR_ASKRIDA_<%=pol.getStCostCenterCode()%>_<%=pol.getStPolicyNo()%>" orientation="0">
                                            <barcode:datamatrix>
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>24x24</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>

                                </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(<%=pol.getUserApproved().getFile().getStFilePath()%>)"  />
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%= pol.getUserApproved().getStUserName().toUpperCase()%></fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center" ><%=JSPUtil.printX(pol.getUserApproved().getStJobPosition()).toUpperCase()%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>

                    </fo:table-body>
                </fo:table>
            </fo:block>


            
            <%--
                        <!-- ALAMAT ASKRIDA -->
                        <fo:block-container height="1.7cm" width="17cm" top="11cm" left="3cm" position="absolute">
                            <fo:block text-align = "center">
                                <fo:external-graphic
                                    scaling="non-uniform" src="url(file:///<%=alamatAskridaPath%>)"  />
                            </fo:block>
                        </fo:block-container>
            --%>
        </fo:flow>
    </fo:page-sequence>
    <%-- END DESIGN HALAMAN 1 (ORIGINAL)--%>

    <%
                }
    %>

</fo:root>