<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         java.math.BigDecimal,
         com.crux.common.parameter.Parameter,
         java.util.Date,
         com.crux.util.fop.FOPUtil"%><?xml version="1.0" encoding="utf-8"?>
<%

            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");
            final String attached = (String) request.getAttribute("attached");
            boolean tanpaTanggal = attached.equalsIgnoreCase("3") ? false : true;

            boolean effective = pol.isEffective();

            final String digitalsign = (String) request.getAttribute("digitalsign");
            boolean usingDigitalSign = false;
            if (digitalsign != null) {
                if (digitalsign.equalsIgnoreCase("Y")) {
                    usingDigitalSign = true;
                }
            }

            boolean isUsingBarcode = Parameter.readString("REPORT_SIGN_TYPE").equalsIgnoreCase("BARCODE");
            String askridaLogoPath = Parameter.readString("DIGITAL_POLIS_LOGO_PIC");

//if (true) throw new NullPointerException();

%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-height="30cm"
                               page-width="21cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            <fo:region-body margin-top="1cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="1.5cm"/>
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
                PT. Asuransi Bangun Askrida
            </fo:block>

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
                      font-size="6pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>

        <fo:flow flow-name="xsl-region-body">
            <%--
                        <fo:block text-align = "center">
                            <fo:external-graphic
                                content-height="scale-to-fit"
                                height="1.00in" content-width="1.00in"
                                scaling="non-uniform" src="url(file:///<%=askridaLogoPath%>)"  />
                        </fo:block>
            --%>
            <%
                        String bw = "0.5pt";
            %> 

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-header>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(file:///<%=askridaLogoPath%>)"  />
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block font-size="16pt" text-align="center" space-after.optimum="10pt">
                                    {L-ENG List of Attachement Credit Insurance-L}{L-INA Lampiran Polis Asuransi Kredit-L}
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block font-size="12pt" text-align="center" space-after.optimum="10pt">
                                    {L-ENG On Behalf of : -L}{L-INA Atas Nama : -L}<%=JSPUtil.xmlEscape(pol.getStCustomerName())%>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block font-size="14pt" text-align="center" space-after.optimum="10pt">
                                    {L-ENG Policy Number-L}{L-INA Nomor Polis -L} : <%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >{L-ENG No. -L}{L-INA No. -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >{L-ENG Member-L}{L-INA Peserta-L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >{L-ENG Birth of Date-L}{L-INA Tanggal Lahir-L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >{L-ENG ID No.-L}{L-INA No. KTP-L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" font-size="7pt">{L-ENG Work-L}{L-INA Pekerjaan-L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >{L-ENG Periode-L}{L-INA Jangka Waktu-L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >{L-ENG Credit No.-L}{L-INA No. PK-L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="end" line-height="5mm" >{L-ENG Sum Insured -L}{L-INA H. Pertanggungan -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm" >{L-ENG Premium -L}{L-INA Premi -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-header> 
                    <fo:table-body>

                        <%

                                    int counter = 30;

                                    BigDecimal subTotalInsured = new BigDecimal(0);
                                    BigDecimal subTotalPremi = new BigDecimal(0);

                                    BigDecimal TotalInsured = new BigDecimal(0);
                                    BigDecimal TotalPremi = new BigDecimal(0);

                                    DTOList objects = pol.getObjects();

                                    for (int i = 0; i < objects.size(); i++) {
                                        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

                                        if (i == counter) {
                                            counter = counter + 30;


                        %>    

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row space-before.optimum="5pt">
                            <fo:table-cell number-columns-spanned="7" border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="end" line-height="5mm" ><%=JSPUtil.printX(subTotalInsured, 2)%><fo:inline color="white">.</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm" ><%=JSPUtil.printX(subTotalPremi, 2)%><fo:inline color="white">.</fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt"></fo:block>
                            </fo:table-cell>                            
                        </fo:table-row>

                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>

                        <%
                                                                    subTotalInsured = null;
                                                                    subTotalPremi = null;
                                                                }%>

                        <fo:table-row>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" ><%=JSPUtil.printX(String.valueOf(i + 1))%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="start" line-height="5mm" font-size="6pt"><fo:inline color="white">.</fo:inline><%=JSPUtil.printX(obj.getStReference1())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" ><%=JSPUtil.printX(obj.getDtReference1())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >--</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >--</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" font-size="7pt"><%=JSPUtil.printX(obj.getDtReference2())%> - <%=JSPUtil.printX(obj.getDtReference3())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >--</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="end" line-height="5mm" ><%=JSPUtil.printX(obj.getDbObjectInsuredAmount(), 2)%><fo:inline color="white">.</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm" ><%=JSPUtil.printX(BDUtil.add(obj.getDbObjectPremiTotalAmount(), obj.getDbReference7()), 2)%><fo:inline color="white">.</fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                        subTotalInsured = BDUtil.add(subTotalInsured, obj.getDbObjectInsuredAmount());
                                        subTotalPremi = BDUtil.add(subTotalPremi, BDUtil.add(obj.getDbObjectPremiTotalAmount(), obj.getDbReference7()));

                                    }%>


                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7" border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="end" line-height="5mm" ><%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%><fo:inline color="white">.</fo:inline></fo:block> </fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm" ><%=JSPUtil.printX(pol.getDbPremiTotal(), 2)%><fo:inline color="white">.</fo:inline></fo:block> </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>                    
                </fo:table>
            </fo:block>

            <% if (usingDigitalSign) {%>
            <fo:block font-size="6pt"
                      space-before.optimum="3pt"
                      space-after.optimum="3pt"
                      text-align="justify" >
                Dokumen ini dicetak secara sistem dan dinyatakan sah.
            </fo:block>
            <% }%>

            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="3pt"></fo:block>


            <fo:block font-size="6pt" space-after.optimum="10pt">
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "dd/MM/yyyy HH:mm:ss"))%>

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
                    <fo:table-column column-width="100mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L}
                                    <%= JSPUtil.printX(pol.getStCostCenterDesc())%><%if (!tanpaTanggal) {%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%></fo:block>
                                    <% } else {%>
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L}
                                    <%= JSPUtil.printX(pol.getCostCenter(pol.getCostCenter(pol.getStCostCenterCode()).getStSubCostCenterCode()).getStDescription())%><%if (!tanpaTanggal) {%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%></fo:block>
                                    <% }%>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <%if (!isUsingBarcode) {%><fo:block text-align="center">{L-ENG Authorized-L}{L-INA Tanda Tangan Yang Berwenang-L}</fo:block><%}%>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="90mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="90mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-body>

                        <% if (usingDigitalSign) {%>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="KANTOR_ASKRIDA_<%=pol.getCostCenter(pol.getStCostCenterCode()).getTypeDescription().toUpperCase()%>_<%=pol.getStCostCenterCode()%>_<%=pol.getStPolicyNo()%>" orientation="0">
                                            <barcode:datamatrix>
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>24x24</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                                <%--<human-readable>none</human-readable>--%>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>

                                </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <%--<% if (pol.getUserApproved().getFile().getStFilePath()!=null) { %>--%>
                            <fo:table-cell>
                                <%if (!isUsingBarcode) {%>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(<%=pol.getUser(pol.getUserIDSign()).getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <% }%>

                                <%if (isUsingBarcode) {%>
                                <fo:block text-align = "center" space-before.optimum="5pt">
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="<%=pol.getEncryptedApprovedWho()%>" orientation="0">
                                            <barcode:datamatrix>
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>24x24</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                                <%--<human-readable>none</human-readable>--%>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>

                                </fo:block>
                                <fo:block font-size="6pt"
                                          font-family="sans-serif"
                                          line-height="10pt"
                                          space-after.optimum="10pt"
                                          text-align="center">
                                    <%=pol.getStSignCode()%>
                                </fo:block>
                                <%}%>

                            </fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%= pol.getUserIDSignName()%></fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center" ><%if (pol.getParaf() != null) {%><fo:leader leader-length="10mm"/><%}%><%=JSPUtil.printX(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%>                                     <%if (pol.getParaf() != null) {%>                                     <fo:external-graphic                                         content-height="scale-to-fit" height="0.3in"  content-width="0.3in" scaling="non-uniform"                                         overflow="hidden" src="url(<%=pol.getUser(pol.getParaf()).getParafFile().getStFilePath()%>)"  />                                     <%}%>                             </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>

                    </fo:table-body>
                </fo:table>
            </fo:block>

        </fo:flow>
    </fo:page-sequence>
</fo:root>