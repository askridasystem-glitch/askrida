<%@ page import="com.webfin.ar.forms.ReceiptForm,
         com.crux.web.controller.SessionManager,
         com.webfin.ar.model.ARInvoiceView,
         com.webfin.ar.model.ARInvoiceDetailView,
         com.webfin.ar.model.ARReceiptView,
         com.webfin.ar.model.ARReceiptLinesView,
         com.crux.util.*,
         java.math.BigDecimal,
         com.crux.common.parameter.Parameter,
         com.webfin.insurance.model.*,
         java.util.Date"%><?xml version="1.0" ?>

<%
            ReceiptForm form = (ReceiptForm) request.getAttribute("FORM");

            DTOList line = (DTOList) request.getAttribute("RPT");
%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines the layout master -->
    <fo:layout-master-set>

        <fo:simple-page-master master-name="first"
                               page-height="14cm"
                               page-width="21.5cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="1cm"
                               margin-right="1cm">

            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>


    </fo:layout-master-set>

    <%
                for (int j = 0; j < line.size(); j++) {
                    ARReceiptLinesView view = (ARReceiptLinesView) line.get(j);
    %>    

    <!-- starts actual layout -->
    <fo:page-sequence master-reference="first" initial-page-number="1">

        <fo:flow flow-name="xsl-region-body">

            <%
                    BigDecimal premiGrossTotal = null;
                    BigDecimal diskonTotal = null;
                    BigDecimal pcostTotal = null;
                    BigDecimal stampdutyTotal = null;
                    BigDecimal tagihBruto = null;
                    BigDecimal tagihBruto2 = null;

                    String nopol = "";
                    String installment = "";
                    String custName = "";
                    String custName1 = "";
                    String custName2 = "";
                    String addressName = "";
                    String addressName1 = "";
                    String addressName2 = "";
                    String ccy = "";
                    String polType = "";
                    String policyType = "";
                    String branch = "";
                    String koda = "";
                    String ttd = "";
                    String pejabat = "";
                    String jabatan = "";
                    Date tglpol = null;

                    //for(int j=0;j<line.size();j++) {
                    //    ARReceiptLinesView view = (ARReceiptLinesView) line.get(j);

                    nopol = view.getInvoice().getStAttrPolicyNo();

                    installment = view.getStInvoiceNo();

                    final DTOList arInvoiceDetail = view.getARInvoiceDetails();

                    for (int k = 0; k < arInvoiceDetail.size(); k++) {
                        ARInvoiceDetailView detil = (ARInvoiceDetailView) arInvoiceDetail.get(k);

                        if (detil.isPremiGross2()) {
                            premiGrossTotal = BDUtil.add(premiGrossTotal, detil.getDbEnteredAmount());
                        }

                        if (detil.isDiscount2()) {
                            diskonTotal = BDUtil.add(diskonTotal, detil.getDbEnteredAmount());
                        }

                        if (detil.isPolicyCost2()) {
                            pcostTotal = BDUtil.add(pcostTotal, detil.getDbEnteredAmount());
                        }

                        if (detil.isStampDuty2()) {
                            stampdutyTotal = BDUtil.add(stampdutyTotal, detil.getDbEnteredAmount());
                        }

                        tagihBruto = BDUtil.add(BDUtil.add(premiGrossTotal, pcostTotal), stampdutyTotal);
                        tagihBruto2 = BDUtil.sub(tagihBruto, diskonTotal);
                    }
                    //}

                    //for(int i=0;i<line.size();i++){
                    //    ARReceiptLinesView rcl = (ARReceiptLinesView) line.get(i);

                    final DTOList arInvoice = view.getARInvoice();
                    for (int m = 0; m < arInvoice.size(); m++) {
                        ARInvoiceView invoc = (ARInvoiceView) arInvoice.get(m);

                        final DTOList insPolicyView = invoc.getInsPolicy();
                        for (int d = 0; d < insPolicyView.size(); d++) {
                            InsurancePolicyView insPol = (InsurancePolicyView) insPolicyView.get(d);

                            custName1 = insPol.getStCustomerName();

                            addressName1 = insPol.getStCustomerAddress();

                            custName2 = insPol.getStReference5();

                            ccy = insPol.getStCurrencyCode();

                            polType = insPol.getStPolicyTypeGroupID();

                            policyType = insPol.getStPolicyTypeDesc2();

                            branch = insPol.getStCostCenterDesc();

                            tglpol = insPol.getDtPolicyDate();

                            koda = insPol.getCostCenter(insPol.getStCostCenterCode()).getStType();

                            if (koda.equalsIgnoreCase("1")) {

                                ttd = insPol.getUser(insPol.getUserIDSign()).getFile().getStFilePath();

                                pejabat = insPol.getUserIDSignName();

                                jabatan = insPol.getUser(insPol.getUserIDSign()).getStJobPosition();

                            }

                            final DTOList insPolObjView = insPol.getObjects();
                            for (int e = 0; e < insPolObjView.size(); e++) {
                                InsurancePolicyObjDefaultView insPolObj = (InsurancePolicyObjDefaultView) insPolObjView.get(e);

                                addressName2 = insPolObj.getStReference3();

                            }
                        }
                    }
                    //}

                    if (polType.equalsIgnoreCase("7") || polType.equalsIgnoreCase("8")) {
                        custName = custName2;
                        addressName = addressName2;
                    } else {
                        custName = custName1;
                        addressName = addressName1;
                    }
            %>

            <fo:block-container height="3cm" width="3cm" top="0cm" left="0cm" position="absolute">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="3cm"/>
                    <fo:table-body font-size="8pt">

                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block text-align="center">
                                    <fo:external-graphic <%--content-width="scale-to-fit"--%>
                                        content-height="100%"
                                        width="100%"
                                        scaling="uniform" src="url(D:\jboss-fin\server\default\deploy\fin.ear\fin.war\pages\main\img\burung aja.jpg)"/>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block-container>

            <fo:block-container height="1cm" width="1cm" top="3.1cm" left="0cm" position="absolute">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="1cm"/>
                    <fo:table-body font-size="8pt">

                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block text-align="start"
                                          font-size="16pt"
                                          font-family="TAHOMA"
                                          line-height="12pt"
                                          font-weight="bold">
                                    {L-ENG RECEIPT-L}{L-INA KWITANSI-L}
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block-container>

            <%--<fo:block space-after.optimum="20pt"></fo:block>--%>

            <fo:block-container height="7cm" width="2cm" top="3.1cm" left="13cm" position="absolute">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="3cm"/>
                    <fo:table-column column-width="0.3cm"/>
                    <fo:table-column column-width="5cm"/>
                    <fo:table-body font-size="8pt">

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Receipt No.-L}{L-INA No. Kwitansi-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= JSPUtil.printX(form.getStReceiptNo())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Policy Type-L}{L-INA Jenis Pertanggungan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= JSPUtil.printX(policyType)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Branch-L}{L-INA Cabang-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= JSPUtil.printX(branch)%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block-container>

            <%--<fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.25pt" space-before.optimum="10pt" space-after.optimum="3pt"></fo:block>--%>

            <fo:block-container height="6cm" width="19.5cm" top="4cm" left="0cm" position="absolute">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="3.5cm"/>
                    <fo:table-column column-width="0.2cm"/>
                    <fo:table-column column-width="4.5cm"/>
                    <fo:table-column column-width="3.0cm"/>
                    <fo:table-column column-width="3.0cm"/>
                    <fo:table-column column-width="0.7cm"/>
                    <fo:table-column />
                    <fo:table-body font-size="10pt">

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7"><fo:block space-before.optimum="5pt" space-after.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Received from-L}{L-INA Sudah di terima dari-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="5"><fo:block><%=JSPUtil.printX(custName)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7"><fo:block space-before.optimum="5pt" space-after.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Address-L}{L-INA Alamat -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="5"><fo:block><%=JSPUtil.printX(addressName)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7"><fo:block space-before.optimum="5pt" space-after.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG The sum of-L}{L-INA Jumlah uang sebesar -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="5"><fo:block font-weight="bold"><%=JSPUtil.printX(ccy)%> <%=JSPUtil.printX(tagihBruto2, 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7"><fo:block space-before.optimum="5pt" space-after.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Say -L}{L-INA Terbilang -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="5"><fo:block font-weight="bold"># <%=JSPUtil.printX(ccy)%>  <%=NumberSpell.readNumber(JSPUtil.printX(tagihBruto2, 2))%> #</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7"><fo:block space-before.optimum="5pt" space-after.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block>{L-ENG Being payment of insurance premium as per out Debit Note No.-L}{L-INA Untuk pembayaran premi sesuai dengan Debet No.-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block font-weight="bold">F<%=JSPUtil.printX(nopol.substring(2, 4) + "" + nopol.substring(10, 12) + "" + nopol.substring(0, 2) + "" + nopol.substring(4, 6) + "" + nopol.substring(12, 16))%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block>No. Rekening <fo:inline font-weight="bold"><%= JSPUtil.printX(form.getStRekeningNo())%></fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7"><fo:block space-before.optimum="5pt" space-after.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Description-L}{L-INA Keterangan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="5"><fo:block>Pembayaran Polis No. <%=JSPUtil.printX(nopol.substring(0, 4) + "-" + nopol.substring(4, 8) + "-" + nopol.substring(8, 12) + "-" + nopol.substring(12, 16) + "-" + nopol.substring(16, 18))%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7"><fo:block space-before.optimum="5pt" space-after.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block-container>

            <%

                    //for(int a = 0; a < line.size(); a++){
                    //ARReceiptLinesView receipt = (ARReceiptLinesView) line.get(a);

                    final DTOList arInvoiceView = view.getARInvoice();
                    for (int s = 0; s < arInvoiceView.size(); s++) {
                        ARInvoiceView invoicView = (ARInvoiceView) arInvoiceView.get(s);

                        final DTOList insPolicyView = invoicView.getInsPolicy();
                        for (int d = 0; d < insPolicyView.size(); d++) {
                            InsurancePolicyView insPol = (InsurancePolicyView) insPolicyView.get(d);

                            final DTOList insPolicyInstal = insPol.getInstallment();
                            for (int e = 0; e < insPolicyInstal.size(); e++) {
                                InsurancePolicyInstallmentView insPolInstal = (InsurancePolicyInstallmentView) insPolicyInstal.get(e);
            %>         

            <% if (insPolicyInstal.size() > 1) {%>

            <fo:block-container height="1cm" width="14cm" top="9.7cm" left="0cm" padding="1mm" position="absolute">
                <fo:block text-align="start" space-after.optimum="10pt" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    Angsuran Ke 
                    <% if (installment.length() > 21) {%>
                    <%=JSPUtil.printX(installment.substring(31, 32))%>
                    <% } else {%>
                    <%=JSPUtil.printX(installment.substring(20, 21))%>
                    <% }%>
                </fo:block>
            </fo:block-container>

            <% }%>

            <%
                            }
                        }
                    }
                    //}
%>          

            <fo:block-container height="7cm" width="6cm" top="8cm" left="13cm" padding="1mm" position="absolute">
                <fo:block text-align="center" space-after.optimum="10pt" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    {L-ENG Date : -L}{L-INA Tgl : -L}<%=DateUtil.getDateStr(tglpol, "dd ^^ yyyy")%>
                </fo:block>
            </fo:block-container>

            <fo:block-container height="7cm" width="6cm" top="8.5cm" left="13cm" padding="1mm" position="absolute">
                <fo:block text-align="center" line-height="14pt" font-size="8pt">
                    <%= JSPUtil.printX(branch)%>, <%=DateUtil.getDateStr(form.getDtReceipt(), "dd ^^ yyyy")%>
                </fo:block>
                <fo:block text-align="center" line-height="14pt" font-size="8pt">
                    PT. ASURANSI BANGUN ASKRIDA
                </fo:block>
                <% if (koda.equalsIgnoreCase("1")) {%>
                <fo:block text-align="center" line-height="14pt">
                    <fo:external-graphic
                        content-height="scale-to-fit"
                        height="0.75in" content-width="0.75in"
                        scaling="non-uniform" src="url(<%=ttd%>)"  />
                </fo:block>                 
                <fo:block text-align="center" line-height="14pt" font-size="8pt">
                    <fo:inline text-decoration="underline"><%=pejabat%></fo:inline>
                </fo:block>
                <fo:block text-align="center" line-height="14pt" font-size="8pt">
                    <%=JSPUtil.printX(jabatan).toUpperCase()%>
                </fo:block>
                <% }%>
            </fo:block-container>

            <% if (Tools.compare(pcostTotal, BDUtil.zero) > 0 && Tools.compare(stampdutyTotal, BDUtil.zero) > 0) {%>

            <fo:block-container height="3cm" width="6.5cm" top="10.1cm" left="0cm" position="absolute">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="3cm"/>
                    <fo:table-column column-width="1cm"/>
                    <fo:table-column column-width="2.5cm"/>
                    <fo:table-body font-size="10pt">

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block space-before.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>     

                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Premium Bruto-L}{L-INA Premi Bruto-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block ><%=JSPUtil.printX(ccy)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(premiGrossTotal, 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% if (diskonTotal != null) {%>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Discount-L}{L-INA Diskon-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block ><%=JSPUtil.printX(ccy)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(diskonTotal, 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Policy Cost-L}{L-INA Biaya Polis-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block ><%=JSPUtil.printX(ccy)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pcostTotal, 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Stamp Cost-L}{L-INA Biaya Materai-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block ><%=JSPUtil.printX(ccy)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(stampdutyTotal, 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% } else {%>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Policy Cost-L}{L-INA Biaya Polis-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block ><%=JSPUtil.printX(ccy)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pcostTotal, 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Stamp Cost-L}{L-INA Biaya Materai-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block ><%=JSPUtil.printX(ccy)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(stampdutyTotal, 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>

                    </fo:table-body>
                </fo:table>
            </fo:block-container>

            <% }%> 

            <fo:block-container height="2cm" width="10cm" top="12cm" left="0cm" padding="1mm" position="absolute">
                <fo:block text-align="start" line-height="14pt" font-size="6pt">
                    {L-ENG his receipt is only valid, after the cheque or transfer is realized -L}{L-INA Jika pembayaran dilakukan dengan cheque, giro atau pemindah-bukuan, maka kwitansi ini baru berlaku setelah cheque, giro atau pemindah-bukuan tersebut direalisir-L}
                </fo:block>
            </fo:block-container> 

        </fo:flow>
    </fo:page-sequence>

    <% }%>

</fo:root>

