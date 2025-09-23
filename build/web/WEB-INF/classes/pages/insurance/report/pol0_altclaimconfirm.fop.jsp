<%@ page import="com.webfin.insurance.model.*,
         com.webfin.entity.model.EntityAddressView,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         com.crux.util.fop.FOPUtil,
         java.util.Date,
         com.crux.web.controller.SessionManager,
         com.crux.common.parameter.Parameter,
         java.math.BigDecimal,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%

            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");
            final String attached = (String) request.getAttribute("attached");
            final String otorized = (String) request.getAttribute("authorized");
            boolean isAttached = attached.equalsIgnoreCase("1") ? false : true;

            boolean effective = pol.isEffective();

            String nopol = pol.getStPolicyNo();

            String jenis_pol = pol.getStPolicyTypeDesc2().toUpperCase();

            String objectName = null;
            String objectNoPolisi = null;
            String itemID = null;
            String itemName = new String("Teknis");
            String itemNameOnly = new String("Teknis");

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
                    || pol.getStPolicyTypeID().equalsIgnoreCase("74")
                    || pol.getStPolicyTypeID().equalsIgnoreCase("60")
                    || pol.getStPolicyTypeID().equalsIgnoreCase("80")) {
                isSuretyKBG = true;
            } else if (pol.getStPolicyTypeID().equalsIgnoreCase("21")
                    || pol.getStPolicyTypeID().equalsIgnoreCase("59")) {
                isKreditKreasi = true;
            } else {
                isNonSuretyKredit = true;
            }

            final String preview = (String) request.getAttribute("preview");
            boolean isPreview = false;
            if (preview != null) {
                if (preview.equalsIgnoreCase("Preview")) {
                    isPreview = true;
                }
            }

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
                               margin-left="2cm"
                               margin-right="2cm">

            <fo:region-body margin-top="1.5cm" margin-bottom="1.5cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="1.5cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">

        <!-- usage of page layout -->
        <!-- HEADER -->

        <fo:flow flow-name="xsl-region-body">

            <% if (SessionManager.getInstance().getSession().getStBranch() == null) {%>

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
                      line-height="16pt" space-after.optimum="10pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="90mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <% if (!effective) {%>
                            <fo:table-cell></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block>Nomor : 
                                    <%if (pol.getStClaimLetter() != null) {%>
                                    <%= JSPUtil.printX(pol.getStClaimLetter())%>
                                    <% }%>
                                </fo:block></fo:table-cell>
                                <% }%>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Jakarta, 
                                    <%if (pol.getDtConfirmDate() != null) {%>
                                    <%=DateUtil.getDateStr(pol.getDtConfirmDate(), "d ^^ yyyy")%>
                                    <% }%>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" line-height="10pt" space-after.optimum="20pt" text-align="justify"> </fo:block>

            <!-- defines text title level 1-->

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="100mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG To -L}{L-INA Kepada Yth. -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block><fo:inline font-weight="bold">PT. ASURANSI BANGUN ASKRIDA</fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block><%= pol.getStCostCenterDesc()%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block><%=pol.getStCostCenterAddress()%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <!-- Normal text -->

            <!-- DYNAMIC HEADER WORDING -->

            <fo:block font-size="<%=fontsize%>" line-height="10pt" space-after.optimum="20pt" text-align="justify"> </fo:block>


            <!-- GARIS  -->

            <!-- Normal text -->

            <fo:block font-size="<%=fontsize%>" line-height="12pt" space-after.optimum="10pt">
                Dengan hormat,
            </fo:block>

            <%
                 BigDecimal claimAmount = new BigDecimal(0);
                 BigDecimal tax3 = new BigDecimal(0);

                 final DTOList items = pol.getClaimItems();
                 for (int j = 0; j < items.size(); j++) {
                     InsurancePolicyItemsView item = (InsurancePolicyItemsView) items.get(j);

                     itemID = item.getStInsItemID();

                     if (item.isGratia()) {
                         itemName = item.getStDescription2();
                         itemNameOnly = item.getInsuranceItem().getStDescription();

                     } else {
                         //itemName = new String("Teknis");
                         //itemNameOnly = new String("teknis");
                     }

                     final boolean negative = item.getInsItem().getARTrxLine().isNegative();

                     /*if (item.getStInsItemID().equalsIgnoreCase("50")
                     || item.getStInsItemID().equalsIgnoreCase("76")
                     || item.getStInsItemID().equalsIgnoreCase("77")
                     || item.getStInsItemID().equalsIgnoreCase("78")
                     || item.getStInsItemID().equalsIgnoreCase("81")
                     || item.getStInsItemID().equalsIgnoreCase("82")
                     || item.getStInsItemID().equalsIgnoreCase("83")
                     || item.getStInsItemID().equalsIgnoreCase("84")) {
                     continue;
                     }*/

                     if (item.getInsuranceItem().getStItemType() != null) {
                         if (item.getInsuranceItem().getStItemType().equalsIgnoreCase("ADVPAYMENT")) {
                             continue;
                         }
                     }

                     System.out.print("@@@@@@@@@@@@@@@ " + item.getInsItem().getStDescription() + " : " + item.getDbAmount());

                     if (negative) {
                         claimAmount = BDUtil.sub(claimAmount, item.getDbAmount());
                     } else {
                         claimAmount = BDUtil.add(claimAmount, item.getDbAmount());
                     }
                     System.out.print("%%%%%%%%%%%%%%%%%%%% : " + claimAmount);

                     if (pol.getStCoverTypeCode().equalsIgnoreCase("COINSOUT") || pol.getStCoverTypeCode().equalsIgnoreCase("COINSOUTSELF")) {
                         final DTOList coins = pol.getCoins2();
                         for (int f = 0; f < coins.size(); f++) {
                             InsurancePolicyCoinsView coi = (InsurancePolicyCoinsView) coins.get(f);

                             if (!coi.getStEntityID().equalsIgnoreCase("1")) {
                                 continue;
                             }

                             claimAmount = coi.getDbClaimAmount();
                             System.out.print("####################### : " + claimAmount);
                         }
                     }

                     if (item.getStInsItemID().equalsIgnoreCase("70")) {
                         tax3 = item.getDbTaxAmount();
                     }

                 }

                 System.out.print("@@@@@@@@@@@@@@@@@@@@ : " + claimAmount);
            %>      

            <fo:block font-size="<%=fontsize%>" line-height="12pt" text-align="center" space-after.optimum="10pt">
                <fo:inline font-weight="bold">Hal : Konfirmasi <%= itemName%> Klaim <%=JSPUtil.printX(pol.getStPolicyTypeDesc2())%></fo:inline>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" line-height="12pt" space-after.optimum="20pt">
                Bersama ini kami sampaikan persetujuan <%=itemName%> atas klaim tersebut dengan perincian sbb:
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="40mm"/>
                    <% if (pol.getStPolicyTypeID().equalsIgnoreCase("3")) {%>
                    <fo:table-column column-width="20mm"/>
                    <% }%>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>

                        <%
                             final DTOList objects2 = pol.getObjectsClaim();

                             for (int j = 0; j < objects2.size(); j++) {
                                 InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects2.get(j);

                                 if (pol.getStPolicyTypeID().equalsIgnoreCase("3")) {
                                     if (obj.getStReference9() != null) {
                                         objectName = obj.getStReference9();
                                     } else {
                                         objectName = "--";
                                     }
                                 } else {
                                     objectName = obj.getStReference1();
                                 }

                                 objectNoPolisi = obj.getStReference1();

                             }

                        %>

                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="center">Nomor LKP</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Nomor Polis</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Tertanggung</fo:block></fo:table-cell>
                            <% if (pol.getStPolicyTypeID().equalsIgnoreCase("3")) {%>
                            <fo:table-cell><fo:block text-align="center">No Polisi</fo:block></fo:table-cell>
                            <% }%>
                            <fo:table-cell><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Klaim</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="center"><%= pol.getStDLANo()%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%= pol.getStPolicyNo()%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.xmlEscape(objectName)%></fo:block></fo:table-cell>
                            <% if (pol.getStPolicyTypeID().equalsIgnoreCase("3")) {%>
                            <fo:table-cell><fo:block text-align="center"><%= objectNoPolisi%></fo:block></fo:table-cell>
                            <% }%>
                            <fo:table-cell><fo:block text-align="end"><%= pol.getStClaimCurrency()%></fo:block></fo:table-cell>
                            <%if (pol.getStCoverTypeCode().equalsIgnoreCase("DIRECT")) {%>
                            <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(BDUtil.sub(claimAmount, tax3), 2)%></fo:block></fo:table-cell>
                            <%} else {%>
                            <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(claimAmount, 2)%></fo:block></fo:table-cell>
                            <%}%>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block> 

            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>" line-height="10pt" space-before.optimum="20pt" space-after.optimum="10pt" text-align="justify" > </fo:block>

            <fo:block font-size="<%=fontsize%>" line-height="12pt" text-align="justify" space-after.optimum="10pt">
                Berkenaan dengan persetujuan tersebut, mohon disampaikan kepada tertanggung bahwa apabila dikemudian hari terdapat indikasi kerugian tidak dijamin oleh kondisi polis dan bertentangan dengan ketentuan hukum yang berlaku, maka Askrida akan menarik kembali ganti rugi yang telah dibayarkan.
            </fo:block>

            <fo:block font-size="<%=fontsize%>" line-height="12pt" text-align="justify" space-after.optimum="10pt">
                Demikian disampaikan. Atas perhatian dan kerjasamanya, kami ucapkan terima kasih.
            </fo:block>

            <fo:block font-size="<%=fontsize%>" line-height="12pt" text-align="justify" space-after.optimum="10pt" font-weight="bold">
                Catatan Klaim : <%=JSPUtil.xmlEscape(pol.getStDLARemark())%>
            </fo:block>

            <%
                 boolean isSubrogasi = false;

                 String namaKasie = null;
                 String namaKasie2 = null;
                 String namaKasie3 = null;
                 String jabatanKasie = null;
                 String jabatanKasie2 = null;
                 String jabatanKasie3 = null;

                 String namaKabag = null;
                 String jabatanKabag = null;

                 String divisiKlaim = null;

                 final DTOList claimItems = pol.getClaimItems();
                 for (int i = 0; i < claimItems.size(); i++) {
                     InsurancePolicyItemsView ci = (InsurancePolicyItemsView) claimItems.get(i);

                     if (ci.getInsItem().getStInsuranceItemID().equalsIgnoreCase("48") || ci.getInsItem().getStInsuranceItemID().equalsIgnoreCase("73")) {
                         isSubrogasi = true;
                     }

                     //31okt2018
                     /*
                     if (pol.getStEntityID().equalsIgnoreCase("1752")
                     || pol.getStEntityID().equalsIgnoreCase("1020501")
                     || pol.getStEntityID().equalsIgnoreCase("1020502")
                     || pol.getStEntityID().equalsIgnoreCase("1020503")) {
                     namaKabag = "ADE IRWAN MUNTACO";
                     jabatanKabag = "Pimpinan";
                     divisiKabag = "HEAD OFFICE";
                     } else {*/
                     if (isSubrogasi) {
                         namaKasie = Parameter.readString("KASIE_SUBROGRASI");
                         jabatanKasie = "Kasie. Subrogasi";

                         namaKabag = Parameter.readString("KABAG_SUBROGRASI");
                         jabatanKabag = "Kabag. Klaim Umum dan Recovery";

                         divisiKlaim = "BAGIAN SUBROGASI";
                     } else {
                         if (isKreditKreasi) {
                             namaKasie = Parameter.readString("KASIE_KLAIM1");
                             namaKasie2 = Parameter.readString("KASIE_KLAIM4");
                             namaKasie3 = Parameter.readString("KASIE_KLAIM5");
                             namaKabag = Parameter.readString("KABAG_KLAIM");
                             jabatanKasie = "Kasie. I Klaim Konsumtif";
                             jabatanKasie2 = "Kasie. II Klaim Konsumtif";
                             jabatanKasie3 = "Kasie. III Klaim Konsumtif";
                         } else if (isSuretyKBG) {
                             namaKasie = Parameter.readString("KASIE_KLAIM2");
                             namaKabag = Parameter.readString("KABAG_KLAIM");
                             jabatanKasie = "Kasie. Klaim Surety";
                         } else if (isNonSuretyKredit) {
                             namaKasie = Parameter.readString("KASIE_KLAIM3");
                             namaKabag = Parameter.readString("KABAG_KLAIM2");
                             jabatanKasie = "Kasie. Klaim Asuransi Umum";
                         }
                         jabatanKabag = "Kabag. Klaim";
                     }
                     divisiKlaim = "BAGIAN KLAIM";
                     //}
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
                                            message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(claimAmount, 2)%>" orientation="0">
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
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block text-align="center">MENYETUJUI,</fo:block>
                                <fo:block text-align="center"><%=JSPUtil.printX(divisiKlaim)%>,</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-display="2">
                                <fo:block text-align="start">
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(claimAmount, 2)%>" orientation="0">
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

            <% if (otorized.equalsIgnoreCase("kasie2")) {%>

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
                                <fo:block text-align="center"><%=JSPUtil.printX(divisiKlaim)%>,</fo:block>
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
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= namaKasie2%></fo:inline></fo:block>
                                <fo:block text-align="center"><%= jabatanKasie2%></fo:block>
                                <%--<% }%>--%>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% }%>

            <% if (otorized.equalsIgnoreCase("kasie3")) {%>

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
                                <fo:block text-align="center"><%=JSPUtil.printX(divisiKlaim)%>,</fo:block>
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
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= namaKasie3%></fo:inline></fo:block>
                                <fo:block text-align="center"><%= jabatanKasie3%></fo:block>
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
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block text-align="center"><%=JSPUtil.printX(divisiKlaim)%>,</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-display="2">
                                <fo:block text-align="start">
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(claimAmount, 2)%>" orientation="0">
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
            <%--
                        <fo:block font-size="<%=fontsize%>">
                            <fo:table table-layout="fixed">
                                <fo:table-column column-width="30mm"/>
                                <fo:table-column column-width="70mm"/>
                                <fo:table-column column-width="70mm"/>
                                <fo:table-body>

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
                                            message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(claimAmount, 2)%>" orientation="0">
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
            --%>
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block text-align="center"><%=JSPUtil.printX(divisiKlaim)%>,</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-display="2">
                                <fo:block text-align="start">
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(claimAmount, 2)%>" orientation="0">
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
                    <fo:table-column column-width="55mm"/>
                    <fo:table-column column-width="55mm"/>
                    <fo:table-column column-width="70mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(divisiKlaim)%>,</fo:block></fo:table-cell>
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
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("KADIV_KLAIM")%> </fo:inline></fo:block>
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
                        message="<%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(claimAmount, 2)%>" orientation="0">
                        <barcode:datamatrix>
                            <barcode:height>40pt</barcode:height>
                            <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                        </barcode:datamatrix>
                    </barcode:barcode>
                </fo:instream-foreign-object>
            </fo:block>

            <% }%>

            <% }%>

            <fo:block font-size="8pt" space-before.optimum="40pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(), "d-MMM-yyyy hh:mm:ss")%>
            </fo:block>

            <% }%> 

        </fo:flow>
    </fo:page-sequence>
</fo:root>