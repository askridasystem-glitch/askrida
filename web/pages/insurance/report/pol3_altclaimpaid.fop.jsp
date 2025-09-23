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
//  boolean isOtorized = otorized.equalsIgnoreCase("kasie");
            String objectName = null;
            String objectID = null;
            BigDecimal amount = null;

            boolean effective = pol.isEffective();
            boolean itemID = false;

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
                    || pol.getStPolicyTypeID().equalsIgnoreCase("59")) {
                isKreditKreasi = true;
            } else {
                isNonSuretyKredit = true;
            }

//if (true) throw new NullPointerException();

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
                {L-INA Surat Permintaan Pembayaran-L}{L-ENG Payment Request-L}
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

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <%
                                    final DTOList objects = pol.getObjectsClaim();
                                    for (int j = 0; j < objects.size(); j++) {
                                        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(j);

                                        if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("10")) {
                                            objectName = pol.getStClaimClientName();
                                        } else {
                                            objectName = obj.getStReference1();
                                        }

                                        objectID = obj.getStPolicyObjectID();
                                    }

                                    final DTOList items = pol.getClaimItems();
                                    for (int j = 0; j < items.size(); j++) {
                                        InsurancePolicyItemsView item = (InsurancePolicyItemsView) items.get(j);

                                        if (item.isAFee()) {
                                            amount = pol.getDbClaimCustAmount();
                                        } else {
                                            amount = pol.getDbClaimAmount();
                                        }
                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG To-L}{L-INA Kepada-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%= JSPUtil.printX(pol.getStClaimPersonName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Remark-L}{L-INA Keterangan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">Pembayaran Klaim Asuransi a/n <%=JSPUtil.xmlEscape(objectName)%> </fo:block></fo:table-cell>
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
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">Bengkel : <%=JSPUtil.printX(pol.getStClaimClientName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Policy No.-L}{L-INA No. Polis-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%= JSPUtil.printX(pol.getStPolicyNo())%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <%

                                    final DTOList claimItems = pol.getClaimItems();
                                    BigDecimal amt3 = new BigDecimal(0);
                                    for (int i = 0; i < claimItems.size(); i++) {
                                        InsurancePolicyItemsView ci = (InsurancePolicyItemsView) claimItems.get(i);

                                        final boolean negative = ci.getInsItem().getARTrxLine().isNegative();

                                        if (ci.getStInsItemID().equalsIgnoreCase("61")) {
                                            continue;
                                        }

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
                            <fo:table-cell ><fo:block text-align="end"><%=amt%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                                    }
                        %>

                        <%

                                    BigDecimal tax3 = new BigDecimal(0);
                                    for (int i = 0; i < claimItems.size(); i++) {
                                        InsurancePolicyItemsView ci = (InsurancePolicyItemsView) claimItems.get(i);

                                        if (!ci.getStInsItemID().equalsIgnoreCase("70")) {
                                            continue;
                                        }

                                        tax3 = ci.getDbTaxAmount();

                        %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(ci.getTax().getStDescription())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end">(<%=JSPUtil.printX(tax3, 2)%>)</fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                                    }
                        %>

                        <%

                                    BigDecimal tax4 = new BigDecimal(0);
                                    for (int i = 0; i < claimItems.size(); i++) {
                                        InsurancePolicyItemsView ci = (InsurancePolicyItemsView) claimItems.get(i);

                                        if (!ci.getStInsItemID().equalsIgnoreCase("50")) {
                                            continue;
                                        }

                                        tax4 = ci.getDbTaxAmount();

                        %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>PPh 23 - Adjuster Fee </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end">(<%=JSPUtil.printX(tax4, 2)%>)</fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                                    }
                        %>

                        <%
                                    BigDecimal share = new BigDecimal(0);
                                    String reasuradur = null;

                                    final DTOList objects2 = pol.getObjectsClaim();
                                    for (int j = 0; j < objects2.size(); j++) {
                                        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects2.get(j);

                                        final DTOList treatyDetails = obj.getTreatyDetails();

                                        for (int m = 0; m < treatyDetails.size(); m++) {
                                            InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treatyDetails.get(m);

                                            if (trd.getStTreatyType().equalsIgnoreCase("JP") || trd.getStTreatyType().equalsIgnoreCase("FAC")) {
                                                final DTOList shares = trd.getShares();

                                                for (int n = 0; n < shares.size(); n++) {
                                                    InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(n);

                                                    if (BDUtil.isZeroOrNull(ri.getDbClaimAmount())) {
                                                        continue;
                                                    }

                                                    if (!ri.getEntity().getStShortName().equalsIgnoreCase("JASINDO")) {
                                                        continue;
                                                    }

                                                    share = BDUtil.add(share, ri.getDbClaimAmount());
                                                    reasuradur = ri.getEntity().getStShortName();


                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>Share <%=JSPUtil.printX(reasuradur)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end">(<%=JSPUtil.printX(share, 2)%>)</fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                }
                                            }
                                        }
                                    }
                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="1pt"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Total-L}{L-INA Jumlah-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt" text-align="end"><%=JSPUtil.printX(BDUtil.sub(pol.getDbClaimAmount(), share), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Say :-L}{L-INA Terbilang :-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%=NumberSpell.readNumber(JSPUtil.printX(BDUtil.sub(pol.getDbClaimAmount(), share), 2), pol.getStClaimCurrency())%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>" space-after.optimum="15pt" text-align="start" ></fo:block>

            <%
                        boolean isSubrogasi = false;
                        String namaKabag = null;
                        String jabatanKabag = null;
                        String divisiKabag = null;
                        for (int i = 0; i < claimItems.size(); i++) {
                            InsurancePolicyItemsView ci = (InsurancePolicyItemsView) claimItems.get(i);

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
                                <fo:block text-align="center">BAGIAN KLAIM,</fo:block>
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
                                <%if (isKreditKreasi) {%>
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("KASIE_KLAIM1")%></fo:inline></fo:block>
                                <% } else if (isSuretyKBG) {%>
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("KASIE_KLAIM2")%></fo:inline></fo:block>
                                <% } else {%>
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("KASIE_KLAIM3")%></fo:inline></fo:block>
                                <% }%>
                                <fo:block text-align="center">Kasie. Klaim</fo:block>
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
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="70mm"/>
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
                            <fo:table-cell ><fo:block text-align="center">MENYETUJUI,</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">BAGIAN <%=JSPUtil.printX(divisiKabag)%>,</fo:block></fo:table-cell>
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
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("KADIV_KLAIM")%></fo:inline></fo:block>
                                <fo:block text-align="center">Kadiv. Klaim</fo:block>
                            </fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%=JSPUtil.printX(namaKabag)%></fo:inline></fo:block>
                                <fo:block text-align="center"><%=JSPUtil.printX(jabatanKabag)%></fo:block>
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
                            <fo:table-cell number-column-spanned="3"><fo:block space-after.optimum="60pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("KADIV_KLAIM")%></fo:inline></fo:block>
                                <fo:block text-align="center">Kadiv. Klaim</fo:block>
                            </fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("DIRTEK")%></fo:inline></fo:block>
                                <fo:block text-align="center">Direktur Teknik</fo:block>
                            </fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%=JSPUtil.printX(namaKabag)%></fo:inline></fo:block>
                                <fo:block text-align="center"><%=JSPUtil.printX(jabatanKabag)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

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

            <% }%>

            <% }%>

            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>
            </fo:block>   

        </fo:flow>
    </fo:page-sequence>
</fo:root>