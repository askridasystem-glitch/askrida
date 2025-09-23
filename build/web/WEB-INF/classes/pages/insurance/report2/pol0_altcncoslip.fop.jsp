<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.webfin.ar.model.ARTaxView,
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

            BigDecimal PremiNett = new BigDecimal(0);
            BigDecimal instPremiNett = new BigDecimal(0);
            BigDecimal instCoins = new BigDecimal(pol.getStInstallmentPeriods());

            BigDecimal CommMember = new BigDecimal(0);
            BigDecimal BfeeMember = new BigDecimal(0);
            BigDecimal HfeeMember = new BigDecimal(0);

            final String digitalsign = (String) request.getAttribute("digitalsign");
            boolean usingDigitalSign = false;
            if (digitalsign != null) {
                if (digitalsign.equalsIgnoreCase("Y")) {
                    usingDigitalSign = true;
                }
            }

            boolean isUsingBarcode = Parameter.readString("REPORT_SIGN_TYPE").equalsIgnoreCase("BARCODE");
            String originalWatermarkPath = Parameter.readString("DIGITAL_NOTA_ORI_PIC");
            String duplicateWatermarkPath = Parameter.readString("DIGITAL_NOTA_DUPLICATE_PIC");
            String copyWatermarkPath = Parameter.readString("DIGITAL_NOTA_COPY_PIC");
            String askridaLogoPath = Parameter.readString("DIGITAL_POLIS_LOGO_PIC");
            String askridaLogoBlackWhitePath = Parameter.readString("DIGITAL_POLIS_LOGOBW_PIC");
            String alamatLogoPath = Parameter.readString("DIGITAL_NOTA_ALAMAT_PIC");
            String alamatLogoBlackWhitePath = Parameter.readString("DIGITAL_NOTA_ALAMATBW_PIC");

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
                               page-height="16cm"
                               page-width="21.5cm"
                               margin-top="1cm"
                               margin-left="1cm"
                               margin-right="1cm">

            <fo:region-body margin-top="0cm" margin-bottom="0cm" background-image="file:///<%=watermarkPath%>"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>

    <fo:page-sequence master-reference="first">

        <fo:static-content flow-name="xsl-region-after">
            <fo:block text-align="end"
                      font-size="6pt"

                      line-height="12pt" >
                CNCOSLIP - PT. Asuransi Bangun Askrida
            </fo:block>

            <fo:block text-align="end"
                      font-size="6pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>


        <fo:flow flow-name="xsl-region-body">

            <%
                                int pn = 0;
                                String tax = "";
                                BigDecimal tax_amount = new BigDecimal(0);

                                final DTOList det = pol.getDetails();
                                for (int i = 0; i < det.size(); i++) {
                                    InsurancePolicyItemsView dt = (InsurancePolicyItemsView) det.get(i);

                                    final ARTaxView tx = dt.getTax();
                                    if (tx == null) {
                                        continue;
                                    }

                                    tax = tx.getStDescription();
                                    tax_amount = dt.getDbTaxRate();

                                    /*
                                    if (tx == null) {
                                    tax = "";
                                    tax_amount = BDUtil.zero;
                                    } else if (tx != null) {
                                    tax = tx.getStDescription();
                                    tax_amount = dt.getDbTaxRate();
                                    }
                                     */

                                    //System.out.println("@@@@@@@@@@@@@@@@@@@@ : " + tax_amount);
                                    //System.out.println("#################### : " + dt.getStTaxCode());
                                }

                                DTOList coinz = pol.getCoins2();

                                for (int j = 0; j < coinz.size(); j++) {
                                    InsurancePolicyCoinsView ci = (InsurancePolicyCoinsView) coinz.get(j);

                                    if (ci.isHoldingCompany()) {
                                        continue;
                                    }

                                    EntityView reasuradur = ci.getEntity();

                                    String ciSlipNo = pol.getStPolicyNo() + j;

                                    PremiNett = BDUtil.sub(ci.getDbPremiAmount(), ci.getDbCommissionAmount());
                                    PremiNett = BDUtil.sub(PremiNett, ci.getDbDiscountAmount());
                                    PremiNett = BDUtil.sub(PremiNett, ci.getDbHandlingFeeAmount());
                                    PremiNett = BDUtil.sub(PremiNett, ci.getDbBrokerageAmount());
                                    instPremiNett = BDUtil.div(PremiNett, instCoins);

                                    CommMember = BDUtil.mul(ci.getDbCommissionAmount(), tax_amount);
                                    BfeeMember = BDUtil.mul(ci.getDbBrokerageAmount(), tax_amount);
                                    HfeeMember = BDUtil.mul(ci.getDbHandlingFeeAmount(), tax_amount);

                                    pn++;

            %>

            <%if (pn > 1) {%>
            <fo:block break-after="page"></fo:block>
            <% }%>
            <!-- ROW -->

            <!-- LOGO ASKRIDA -->
            <fo:block text-align = "center">
                <fo:external-graphic
                    content-height="scale-to-fit"
                    height="1.00in" content-width="1.00in"
                    scaling="non-uniform" src="url(file:///<%=logoAskridaPath%>)"  />
            </fo:block>

            <fo:block font-size="16pt" line-height="12pt" space-after.optimum="10pt">
                {L-ENG CREDIT NOTE-L}{L-INA NOTA KREDIT-L}
            </fo:block>

            <fo:block font-size="16pt" line-height="12pt" space-after.optimum="10pt" >
                {L-ENG CO-INSURANCE-L}{L-INA KO-ASURANSI-L}
            </fo:block>

            <% if (!effective) {%>
            <fo:block font-size="14pt" 
                      line-height="16pt" space-after.optimum="10pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>

            <fo:block font-size="<%=fontsize%>" >
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column />
                    <fo:table-body>


                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG TO-L}{L-INA KEPADA-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= JSPUtil.xmlEscape(reasuradur.getStEntityName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG ADDRESS-L}{L-INA ALAMAT-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"><%= JSPUtil.xmlEscape(reasuradur.getStAddress())%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" >
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column />
                    <fo:table-body>


                        <!-- GARIS DALAM KOLOM -->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Type of Insurance-L}{L-INA Jenis Asuransi-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStPolicyTypeDesc2()%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Policy Number-L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Address-L}{L-INA Alamat-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <% if (pol.getStCustomerAddress() != null) {%><%=JSPUtil.printX(pol.getStCustomerAddress())%><% } else {%><% }%></fo:block></fo:table-cell>
                        </fo:table-row>


                        <%
                                                            final FlexFieldHeaderView objectMap = pol.getPolicyType().getObjectMap();

                                                            final DTOList objectMapDetails = objectMap == null ? null : objectMap.getDetails();

                                                            //final InsurancePolicyObjDefaultView io = obj;

                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Period-L}{L-INA Jangka Waktu-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(DateUtil.getDateStr(pol.getDtPeriodStart(), "d ^^ yyyy") + " {L-ENG Up To-L}{L-INA Sampai-L} ") + DateUtil.getDateStr(pol.getDtPeriodEnd(), "d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Total Sum Insured-L}{L-INA Jumlah Pertanggungan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="40mm"/>
                                        <fo:table-column column-width="5mm"/>
                                        <fo:table-column column-width="10mm"/>
                                        <fo:table-column column-width="40mm"/>
                                        <fo:table-column column-width="64mm"/>


                                        <fo:table-column />
                                        <fo:table-body>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                        </fo:table-body>
                                    </fo:table>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Your Share-L}{L-INA Saham Saudara-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="40mm"/>
                                        <fo:table-column column-width="5mm"/>
                                        <fo:table-column column-width="10mm"/>
                                        <fo:table-column column-width="40mm"/>
                                        <fo:table-column column-width="64mm"/>
                                        <fo:table-column />
                                        <fo:table-body>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(ci.getDbAmount(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                        </fo:table-body>
                                    </fo:table>
                                </fo:block></fo:table-cell>
                        </fo:table-row>
                        <%-- 
 <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Conditions-L}{L-INA Kondisi-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>

<%
	final DTOList objects = pol.getObjects();

      for (int i = 0; i < objects.size(); i++) {
         InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

   final DTOList covers = obj.getCoverage();

   for (int m = 0; m < covers.size(); m++) {
      InsurancePolicyCoverView cov = (InsurancePolicyCoverView) covers.get(m);

      if (m>0) out.print("; ");
      //out.print(JSPUtil.print(cov.getStInsCoverDesc()));
      	if (cov.getDbRatePct()!=null) {
      		out.print(JSPUtil.printX(cov.getStInsCoverDesc())+": "+JSPUtil.printX(cov.getDbRatePct(),4)+" %");
      	} else {
      		out.print(JSPUtil.printX(cov.getStInsCoverDesc()));
      	}
      %>

<% 		} 
	}
%>

             </fo:block></fo:table-cell>
           </fo:table-row>           

           <fo:table-row>
             <fo:table-cell ><fo:block >{L-ENGHandling Fee-L}{L-INA Handling Fee-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.print(ci.getDbHandlingFeeRate(),2)%>%</fo:block></fo:table-cell>
           </fo:table-row>

                   <fo:table-row>
                      <fo:table-cell number-columns-spanned="3" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="5pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>
                        --%>
                    </fo:table-body>
                </fo:table>
            </fo:block>

            <!-- defines text title level 1-->


            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="153mm"/>
                    <fo:table-column />
                    <fo:table-body>


                        <!-- GARIS DALAM KOLOM -->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="40mm"/>
                                        <fo:table-column column-width="2mm"/>
                                        <fo:table-column column-width="10mm"/>
                                        <fo:table-column column-width="30mm"/>


                                        <fo:table-column />
                                        <fo:table-body>

                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Premium-L}{L-INA Premi-L}</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(ci.getDbPremiAmount(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>

                                            <%if (!BDUtil.isZeroOrNull(ci.getDbDiscountAmount())) {%>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Disc Fee-L}{L-INA Diskon Fee-L} <%=JSPUtil.print(ci.getDbDiscountRate(), 2)%>%</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(ci.getDbDiscountAmount(), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                            <% }%>

                                            <%if (!BDUtil.isZeroOrNull(ci.getDbCommissionAmount())) {%>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Comm Fee-L}{L-INA Komisi Fee-L} <%=JSPUtil.printX(ci.getDbCommissionRate(), 2)%>%</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(BDUtil.sub(ci.getDbCommissionAmount(), CommMember), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>

                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Tax Fee-L}{L-INA Pajak-L} <%=JSPUtil.print(tax)%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(CommMember, 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                            <% }%>

                                            <%if (!BDUtil.isZeroOrNull(ci.getDbBrokerageAmount())) {%>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Brokerage Fee-L}{L-INA Brokerage Fee-L} <%=JSPUtil.printX(ci.getDbBrokerageRate(), 2)%>%</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(BDUtil.sub(ci.getDbBrokerageAmount(), BfeeMember), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>

                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Tax Fee-L}{L-INA Pajak-L} <%=JSPUtil.print(tax)%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(BfeeMember, 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                            <% }%>

                                            <%if (!BDUtil.isZeroOrNull(ci.getDbHandlingFeeAmount())) {%>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Handling Fee-L}{L-INA Handling Fee-L} <%=JSPUtil.print(ci.getDbHandlingFeeRate(), 2)%>%</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(BDUtil.sub(ci.getDbHandlingFeeAmount(), HfeeMember), 2)%></fo:block></fo:table-cell>
                                            </fo:table-row>

                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Tax Fee-L}{L-INA Pajak-L} <%=JSPUtil.print(tax)%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(HfeeMember, 2)%></fo:block></fo:table-cell>
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
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(PremiNett, 2)%></fo:block></fo:table-cell>
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

            <fo:block space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>

            <%
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

                                                                            for (int k = 0; k < installment.size(); k++) {
                                                                                InsurancePolicyInstallmentView ins = (InsurancePolicyInstallmentView) installment.get(k);

                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>Installment <%=(k + 1)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(ins.getDtDueDate())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(instPremiNett, 2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>
                    </fo:table-body>
                </fo:table>
            </fo:block>
            <% }%>

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

            <fo:block-container height="3cm" width="3cm" top="10cm" left="0cm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="3cm"/>
                    <fo:table-body>

                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="1pt"><fo:block>
                                    <% if (usingDigitalSign) {%>
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="KANTOR_ASKRIDA_<%=pol.getStCostCenterCode()%>_<%=pol.getStPolicyNo()%>" orientation="0">
                                            <barcode:datamatrix>
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>24x24</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                                <%--<human-readable>none</human-readable>--%>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>
                                    <% }%>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block-container>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="100mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <% if (usingDigitalSign) {%>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <%--<fo:table-cell ><fo:block>
                                    <% if (usingDigitalSign) { %>
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="KANTOR_ASKRIDA_<%=pol.getCostCenter(pol.getStCostCenterCode()).getTypeDescription().toUpperCase()%>_<%=pol.getCostCenter(pol.getStCostCenterCode()).getStDescription().toUpperCase()%>_<%=pol.getStPolicyNo()%>" orientation="0">
                                            <barcode:datamatrix>
                                                <barcode:height>24pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>
                                    <%}%>
                            </fo:block></fo:table-cell>
                            --%>
                            <fo:table-cell>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="0.75in" content-width="0.75in"
                                        scaling="non-uniform" src="url(<%=pol.getUser(pol.getUserIDSign()).getFile().getStFilePath()%>)"  />
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%= pol.getUserIDSignName()%></fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center" ><%=JSPUtil.printX(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>

                    </fo:table-body>
                </fo:table>
            </fo:block>


            <%

                                }

            %>

        </fo:flow>
    </fo:page-sequence>
    <%-- END DESIGN HALAMAN 1 (ORIGINAL)--%>

    <%
                }
    %>

</fo:root>