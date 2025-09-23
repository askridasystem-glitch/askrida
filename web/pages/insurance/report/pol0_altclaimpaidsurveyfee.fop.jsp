<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         java.util.Date,
         com.crux.web.controller.SessionManager,
         com.crux.common.parameter.Parameter,
         com.crux.util.fop.FOPUtil,
         java.math.BigDecimal"%><?xml version="1.0" encoding="utf-8"?>
<%
            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");
            final String otorized = (String) request.getAttribute("authorized");
            String objectName = null;
            String objectID = null;

            boolean effective = pol.isEffective();

            boolean isSuretyKBG = false;
            boolean isKreditKreasi = false;
            boolean isNonSuretyKredit = false;

            if (pol.getStPolicyTypeID().equalsIgnoreCase("45")
                    || pol.getStPolicyTypeID().equalsIgnoreCase("46")
                    || pol.getStPolicyTypeID().equalsIgnoreCase("47")
                    || pol.getStPolicyTypeID().equalsIgnoreCase("48")
                    || pol.getStPolicyTypeID().equalsIgnoreCase("51")
                    || pol.getStPolicyTypeID().equalsIgnoreCase("52")
                    || pol.getStPolicyTypeID().equalsIgnoreCase("53")
                    || pol.getStPolicyTypeID().equalsIgnoreCase("54")
                    || pol.getStPolicyTypeID().equalsIgnoreCase("55")
                    || pol.getStPolicyTypeID().equalsIgnoreCase("56")
                    || pol.getStPolicyTypeID().equalsIgnoreCase("57")
                    || pol.getStPolicyTypeID().equalsIgnoreCase("58")
                    || pol.getStPolicyTypeID().equalsIgnoreCase("73")
                    || pol.getStPolicyTypeID().equalsIgnoreCase("60")) {
                isSuretyKBG = true;
            } else if (pol.getStPolicyTypeID().equalsIgnoreCase("21")
                    || pol.getStPolicyTypeID().equalsIgnoreCase("59")
                    || pol.getStPolicyTypeID().equalsIgnoreCase("80")) {
                isKreditKreasi = true;
            } else {
                isNonSuretyKredit = true;
            }

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->

        <fo:simple-page-master master-name="only"
                               page-height="30cm"
                               page-width="21cm"
                               margin-top="5.5cm"
                               margin-bottom="0.5cm"
                               margin-left="2.5cm"
                               margin-right="2.5cm">

            <fo:region-body margin-top="1cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="1.5cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">

        <!-- usage of page layout -->
        <!-- HEADER -->

        <fo:static-content flow-name="xsl-region-before">
            <fo:block text-align="end"
                      font-size="6pt"
                      font-family="TAHOMA"
                      line-height="12pt" >
                Claim Document PrintCode:<%=pol.getStPrintCode()%> Page:<fo:page-number/>
            </fo:block>
        </fo:static-content>

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

            <!-- GARIS  -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="10pt"></fo:block>

            <!-- defines text title level 1-->

            <fo:block font-size="16pt" font-family="TAHOMA" line-height="16pt" color="black" text-align="center" space-after.optimum="10pt">
                {L-INA Surat Permintaan Pembayaran-L}{L-ENG Payment Request-L} Biaya Survey
            </fo:block>


            <!-- Normal text -->

            <!-- DYNAMIC HEADER WORDING -->


            <!-- GARIS -->
            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>

            <!-- Normal text -->

            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Date-L}{L-INA Tanggal -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%=DateUtil.getDateStr(pol.getDtDLADate(), "d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Number-L}{L-INA Nomor-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%=pol.getStDLANo()%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG To-L}{L-INA Kepada -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG FINANCE DEPARTMENT-L}{L-INA BAGIAN KEUANGAN -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <!-- GARIS  -->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.50pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="162mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG We, herewith request to pay -L}{L-INA Dengan ini kami minta agar Saudara membayarkan -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <%



            %>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <%
                                    final DTOList objects2 = pol.getObjectsClaim();
                                    for (int j = 0; j < objects2.size(); j++) {
                                        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects2.get(j);

                                        if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("10")) {
                                            objectName = pol.getStClaimClientName();
                                        } else {
                                            objectName = obj.getStReference1();
                                        }

                                        objectID = obj.getStPolicyObjectID();
                                    }

                                    String adjuster = null;
                                    String adjusterDesc = null;
                                    BigDecimal dbAdjuster = new BigDecimal(0);
                                    BigDecimal dbTaxAdjuster = new BigDecimal(0);
                                    final DTOList det = pol.getClaimItems();

                                    for (int l = 0; l < det.size(); l++) {
                                        InsurancePolicyItemsView dt = (InsurancePolicyItemsView) det.get(l);

                                        if (dt.getInsItem().isSurveyFee()) {
                                            if (dt.getStDescription() != null) {
                                                adjuster = dt.getStDescription();
                                            } else {
                                                adjuster = dt.getEntity().getStEntityName();
                                            }

                                            adjusterDesc = dt.getInsItem().getStDescription();

                                            dbAdjuster = BDUtil.add(dbAdjuster, dt.getDbAmount());
                                            dbTaxAdjuster = BDUtil.add(dbTaxAdjuster, dt.getDbTaxAmount());
                                        }

                                        
                                    }

                                    System.out.print("@@@@@@@@@@@@@@@@@@@@@@@ : " + dbAdjuster);
                                    System.out.print("####################### : " + dbTaxAdjuster);
                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG To-L}{L-INA Kepada-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">
                                    <%= JSPUtil.printX(adjuster)%>
                                </fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Amount of-L}{L-INA Sebesar-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%= JSPUtil.printX(pol.getStClaimCurrency())%>. <%= JSPUtil.printX(BDUtil.sub(dbAdjuster, dbTaxAdjuster), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Say :-L}{L-INA Terbilang :-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%=NumberSpell.readNumber(JSPUtil.printX(BDUtil.sub(dbAdjuster, dbTaxAdjuster), 2), pol.getStClaimCurrency())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Remark-L}{L-INA Keterangan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">Pembayaran <%=adjusterDesc%> a/n <%=JSPUtil.xmlEscape(objectName)%> </fo:block></fo:table-cell>
                        </fo:table-row>

                        <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("10")) {%>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">No. Rekening : <%=pol.getStClaimAccountNo()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">Tanggal Kejadian : <%=JSPUtil.printX(pol.getDtClaimDate())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Policy No.-L}{L-INA No. Polis-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%= JSPUtil.printX(pol.getStPolicyNo())%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="10pt" space-after.optimum="2pt" text-align="justify" > </fo:block>


            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-body>
                                            <%

                                                        final DTOList details = pol.getDetails();

                                            %>


                                            <%

                                                        for (int i = 0; i < details.size(); i++) {
                                                            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);
                                                            if (!item.isDiscount()) {
                                                                continue;
                                                            }

                                                            String desc = item.getStDescription2();

                                                            if (item.isEntryByRate()) {
                                                                desc += JSPUtil.printX(item.getDbRate() + " %");
                                                            }

                                            %>
                                            <% }%>



                                            <%

                                                        for (int i = 0; i < details.size(); i++) {
                                                            InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);

                                                            if (!item.isFee()) {
                                                                continue;
                                                            }
                                            %>
                                            <% }%>


                                        </fo:table-body>
                                    </fo:table>
                                </fo:block></fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" space-after.optimum="15pt"></fo:block>

            <%
                        boolean isSubrogasi = false;

                 String namaKasie = null;
                 String jabatanKasie = null;
                 String divisiKasie = null;

                 String namaKabag = null;
                 String jabatanKabag = null;
                 String divisiKabag = null;

                        final DTOList claimItems = pol.getClaimItems();
                        for (int i = 0; i < claimItems.size(); i++) {
                            InsurancePolicyItemsView ci = (InsurancePolicyItemsView) claimItems.get(i);

                            if (ci.getInsItem().getStInsuranceItemID().equalsIgnoreCase("48") || ci.getInsItem().getStInsuranceItemID().equalsIgnoreCase("73")) {
                                isSubrogasi = true;
                            }

                            if (isSubrogasi) {
                         namaKasie = Parameter.readString("KASIE_SUBROGRASI");
                         jabatanKasie = "Kasie. Subrogasi";

                         namaKabag = Parameter.readString("KABAG_SUBROGRASI");
                         jabatanKabag = "Kabag. Klaim Umum dan Recovery";
                     } else {
                         if (isKreditKreasi) {
                             namaKasie = Parameter.readString("KASIE_KLAIM1");
                             namaKabag = Parameter.readString("KABAG_KLAIM");
                         } else if (isSuretyKBG) {
                             namaKasie = Parameter.readString("KASIE_KLAIM2");
                             namaKabag = Parameter.readString("KABAG_KLAIM");
                         } else if (isNonSuretyKredit) {
                             namaKasie = Parameter.readString("KASIE_KLAIM3");
                             namaKabag = Parameter.readString("KABAG_KLAIM2");
                         }
                         jabatanKasie = "Kasie. Klaim";
                         jabatanKabag = "Kabag. Klaim";
                     }
                     divisiKasie = "BAGIAN KLAIM";
                     divisiKabag = "BAGIAN KLAIM";
                        }
            %>

            <%
                        if (SessionManager.getInstance().getSession().getStBranch() != null) {
            %>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>

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

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <%
                                    } else {
            %>


            <% if (otorized.equalsIgnoreCase("kasie")) {%>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(pol.getDtDLADate(), "d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block text-align="center">MENYETUJUI,</fo:block>
                                <fo:block text-align="center"><%=JSPUtil.printX(divisiKasie)%>,</fo:block>
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
                                <%--<%if (pol.getStEntityID().equalsIgnoreCase("1752")
                                    || pol.getStEntityID().equalsIgnoreCase("1020501")
                                    || pol.getStEntityID().equalsIgnoreCase("1020502")
                                    || pol.getStEntityID().equalsIgnoreCase("1020503")) {%>
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%=namaKabag%></fo:inline></fo:block>
                                <fo:block text-align="center"><%=jabatanKabag%></fo:block>
                                <% } else {%>--%>
                                <%--<%if (isKreditKreasi) {%>
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("KASIE_KLAIM1")%></fo:inline></fo:block>
                                <% } else if (isSuretyKBG) {%>
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("KASIE_KLAIM2")%></fo:inline></fo:block>
                                <% } else if (isNonSuretyKredit) {%>
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("KASIE_KLAIM3")%></fo:inline></fo:block>
                                <% }%>--%>
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= namaKasie%></fo:inline></fo:block>
                                <fo:block text-align="center"><%= jabatanKasie%></fo:block>
                                <%--<% }%>--%>
                            </fo:table-cell>
                        </fo:table-row>

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

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(pol.getDtDLADate(), "d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block text-align="center"><%=JSPUtil.printX(divisiKabag)%>,</fo:block>
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
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%=JSPUtil.printX(namaKabag)%></fo:inline></fo:block>
                                <fo:block text-align="center"><%=JSPUtil.printX(jabatanKabag)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

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
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(pol.getDtDLADate(), "d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block text-align="center">BAGIAN <%=JSPUtil.printX(divisiKabag)%>,</fo:block>
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
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("KADIV_KLAIM")%></fo:inline></fo:block>
                                <fo:block text-align="center">Kadiv. Klaim</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% }%>

            <% if (otorized.equalsIgnoreCase("dirtek")) {%>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(pol.getDtDLADate(), "d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block text-align="center">BAGIAN <%=JSPUtil.printX(divisiKabag)%>,</fo:block>
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
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("DIRTEK")%></fo:inline></fo:block>
                                <fo:block text-align="center">Direktur Teknik</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% }%>

            <% }%>

            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>
            </fo:block>   

        </fo:flow>
    </fo:page-sequence>
</fo:root>