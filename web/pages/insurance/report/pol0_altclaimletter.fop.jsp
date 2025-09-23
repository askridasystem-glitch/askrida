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

            final DTOList objects2 = pol.getObjectsClaim();

            for (int j = 0; j < objects2.size(); j++) {
                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects2.get(j);

                objectName = obj.getStReference1();
            }

            BigDecimal claimBruto = new BigDecimal(0);
            BigDecimal claimDeduct = new BigDecimal(0);
            final DTOList claimItems = pol.getClaimItems();
            BigDecimal amt3 = new BigDecimal(0);
            for (int i = 0; i < claimItems.size(); i++) {
                InsurancePolicyItemsView ci = (InsurancePolicyItemsView) claimItems.get(i);

                if (ci.getStInsItemID().equalsIgnoreCase("46")) {
                    claimBruto = ci.getDbAmount();
                }
                if (ci.getStInsItemID().equalsIgnoreCase("47")) {
                    claimDeduct = ci.getDbAmount();
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
                               margin-left="2.5cm"
                               margin-right="2.5cm">

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

            <% if (!effective) {%>
            <fo:block font-size="16pt" font-family="TAHOMA"
                      line-height="16pt" space-after.optimum="10pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>

            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" text-align="justify" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>

            <fo:block font-size="16pt" text-align="center">
                <fo:inline font-weight="bold">{L-INA SURAT PERNYATAAN-L}{L-ENG LETTER OF DISCHARGE-L}</fo:inline>
            </fo:block>

            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" text-align="justify" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="10pt">
                {L-INA Saya/Kami yang bertanda tangan dibawah ini,-L}{L-ENG I/We, the undersigned,-L} <fo:inline font-weight="bold"><%=JSPUtil.printX(pol.getStCustomerName().toUpperCase())%></fo:inline>, {L-INA (untuk selanjutnya disebut Tertanggung)-L}{L-ENG (hereinafter referred to as the Insured)-L}, {L-INA menyatakan setuju untuk menerima dari-L}{L-ENG hereby agree to accept from-L} <fo:inline font-weight="bold">PT. ASURANSI BANGUN ASKRIDA </fo:inline>
                {L-INA (untuk selanjutnya disebut Penanggung)-L}{L-ENG (hereinafter referred as the Insurer)-L} {L-INA sejumlah -L}{L-ENG the sum of -L}<fo:inline font-weight="bold"><%= JSPUtil.printX(pol.getDbClaimAmount(), 2)%> <%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbClaimAmount(), 2), pol.getStCurrencyCode())%></fo:inline>, {L-INA sebagai pembayaran ganti rugi terhadap klaim-L}{L-ENG as indemnity payment of-L}<fo:inline font-weight="bold"><%= JSPUtil.printX(pol.getStPolicyTypeDesc().toUpperCase())%></fo:inline>
                {L-INA yang terjadi pada tanggal-L}{L-ENG Claim arising from the loss/accident dated-L} <fo:inline font-weight="bold"><%=DateUtil.getDateStr(pol.getDtClaimDate(), "dd ^^ yyyy")%></fo:inline> {L-INA dan mengakibatkan kerugian terhadap objek yang dipertanggungakan dibawah polis nomor-L}{L-ENG under policy number-L} <fo:inline font-weight="bold"><%=JSPUtil.printX(pol.getStPolicyNo())%></fo:inline>.
            </fo:block>
            <%--
            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="10pt">
                {L-INA Saya/Kami yang bertanda tangan dibawah ini, <fo:inline font-weight="bold"><%=JSPUtil.printX(pol.getStCustomerName().toUpperCase())%></fo:inline>, (untuk selanjutnya disebut Tertanggung), menyatakan setuju untuk menerima dari  <fo:inline font-weight="bold">PT. ASURANSI BANGUN ASKRIDA </fo:inline>
                (untuk selanjutnya disebut Penanggung) sejumlah <fo:inline font-weight="bold"><%= JSPUtil.printX(pol.getDbClaimAmount(), 2)%> <%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbClaimAmount(), 2), pol.getStCurrencyCode())%></fo:inline>, sebagai pembayaran ganti rugi terhadap klaim <fo:inline font-weight="bold"><%= JSPUtil.printX(pol.getStPolicyTypeDesc().toUpperCase())%></fo:inline>
                yang terjadi pada tanggal <fo:inline font-weight="bold"><%=DateUtil.getDateStr(pol.getDtClaimDate(), "dd/MM/yyyy")%></fo:inline> dan mengakibatkan kerugian terhadap objek yang dipertanggungakan dibawah polis nomor <fo:inline font-weight="bold"><%=JSPUtil.printX(pol.getStPolicyNo())%></fo:inline>.-L}
            </fo:block> 

            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="10pt">
                {L-ENG I/We, the undersigned, <fo:inline font-weight="bold"><%=JSPUtil.printX(pol.getStCustomerName().toUpperCase())%></fo:inline>, (hereinafter referred to as the Insured), hereby agree to accept the sum of <fo:inline font-weight="bold"><%= JSPUtil.printX(pol.getDbClaimAmount(), 2)%> <%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbClaimAmount(), 2), pol.getStCurrencyCode())%></fo:inline>,
                from <fo:inline font-weight="bold">PT. ASURANSI BANGUN ASKRIDA </fo:inline> (hereinafter referred as the Insurer), as indemnity payment of <fo:inline font-weight="bold"><%= JSPUtil.printX(pol.getStPolicyTypeDesc().toUpperCase())%></fo:inline> Claim arising from the loss/accident dated 
                <fo:inline font-weight="bold"><%=DateUtil.getDateStr(pol.getDtClaimDate(), "dd/MM/yyyy")%></fo:inline> under policy number <fo:inline font-weight="bold"><%=JSPUtil.printX(pol.getStPolicyNo())%></fo:inline>.-L}
            </fo:block>--%>

            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="10pt">
                {L-INA Dengan dikeluarkannya surat ini, saya/kami membebaskan Penanggung dari semua tindakan, tuntutan, gugatan serta beban apapun yang terkait dengan klaim tersebut.-L}
                {L-ENG By issuing this letter, I/We shall release the Insurer from any further actions, claims, suits and any further expenses related to the claim. -L}
            </fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="10pt">
                {L-INA Apabila di kemudian hari terbukti bahwa kerugian tersebut tidak terjamin oleh kondisi polis dan/atau ketentuan hukum yang berlaku, maka saya/kami akan mengembalikan sejumlah uang tersebut di atas kepada Penanggung. -L}
                {L-ENG Should there be any evidence in the future which proof that the above loss is excluded by police condition and/or governing laws, I/We will agree to return the above amount to the Insurer. -L}
            </fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="10pt">
                {L-INA Demikian Surat Pernyataan ini saya/kami buat untuk dapat dipergunakan sebagaimana mestinya.-L}
             </fo:block>

            <fo:block font-size="<%=fontsize%>" space-after.optimum="40pt"></fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="100mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block text-align="center" font-weight="bold">{L-INA TERTANGGUNG-L}{L-ENG THE INSURED-L}</fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>           

                        <fo:table-row>
                            <fo:table-cell ><fo:block number-column-display="2" space-after.optimum="30pt"></fo:block></fo:table-cell>             
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block number-column-display="2" space-after.optimum="30pt" text-align="center" font-size="6pt">{L-INA ttd &#x26; Materai-L}{L-ENG Signature &#x26; Stamp (if any)-L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block text-align="center" font-weight="bold"><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

        </fo:flow>
    </fo:page-sequence>
</fo:root>




