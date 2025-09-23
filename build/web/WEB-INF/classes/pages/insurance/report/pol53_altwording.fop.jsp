<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         java.util.Date,
         java.math.BigDecimal,
         com.crux.web.controller.SessionManager,
         com.crux.common.parameter.Parameter,
         com.crux.util.fop.FOPUtil"%><?xml version="1.0" encoding="utf-8"?>
<%

            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");
            final String attached = (String) request.getAttribute("attached");
            final String xlang = (String) request.getAttribute("xlang");
            String param[] = attached.split("[\\|]");

            System.out.print("%%%%%%%%%%%%%%%%%%%%%%%% " + xlang);

            boolean isAttached = param[0].equalsIgnoreCase("2") ? true : false;
            boolean tanpaTanggal = param[0].equalsIgnoreCase("3") ? true : false;
            boolean tanpaRate = param[0].equalsIgnoreCase("7") ? true : false;
            boolean tanpaPrincipal = param[0].equalsIgnoreCase("a") ? true : false;
            boolean versiLama = param[0].equalsIgnoreCase("b") ? true : false;

            if (attached.length() > 1) {
                if (param[1] != null) {
                    isAttached = !isAttached ? param[1].equalsIgnoreCase("2") ? true : false : isAttached;
                    tanpaTanggal = !tanpaTanggal ? param[1].equalsIgnoreCase("3") ? true : false : tanpaTanggal;
                    tanpaRate = !tanpaRate ? param[1].equalsIgnoreCase("7") ? true : false : tanpaRate;
                    tanpaPrincipal = !tanpaPrincipal ? param[1].equalsIgnoreCase("a") ? true : false : tanpaPrincipal;
                    versiLama = !versiLama ? param[1].equalsIgnoreCase("b") ? true : false : versiLama;
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

            BigDecimal periodLength = new BigDecimal(pol.getStPeriodLength());

            BigDecimal TotalPeriodLength = BDUtil.add(periodLength, new BigDecimal(1));

            final String digitalsign = (String) request.getAttribute("digitalsign");
            boolean usingDigitalSign = false;
            if (digitalsign != null) {
                if (digitalsign.equalsIgnoreCase("Y")) {
                    usingDigitalSign = true;
                }
            }

            boolean isUsingBarcode = Parameter.readString("REPORT_SIGN_TYPE").equalsIgnoreCase("BARCODE");

%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-height="30cm"
                               page-width="21cm"
                               margin-top="0.80cm"
                               margin-bottom="0.5cm"
                               margin-left="1.5cm"
                               margin-right="1.5cm">
            <fo:region-body margin-top="0.5cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">

        <fo:flow flow-name="xsl-region-body">

            <% if (isPreview) {%>
            <fo:block font-size="20pt"
                      line-height="16pt" space-after.optimum="0pt"
                      color="blue"
                      text-align="center"
                      padding-top="10pt">
                PREVIEW MODE
            </fo:block>
            <% }%>    

            <% if (!effective) {%>
            <fo:block font-size="16pt"
                      line-height="16pt" space-after.optimum="0pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>

            <!-- GARIS  -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-before.optimum="50pt" space-after.optimum="1pt"></fo:block>

            <!-- defines text title level 1-->

            <fo:block font-size="16pt" line-height="0pt" space-after.optimum="10pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                <%=pol.getStPolicyTypeDesc2()%>
            </fo:block>

            <!-- Normal text -->

            <!-- defines text title level 1-->

            <fo:block font-size="12pt" line-height="0pt" space-after.optimum="10pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                UNCONDITIONAL
            </fo:block>

            <!-- Normal text -->

            <!-- GARIS  -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-before.optimum="1pt" space-after.optimum="10pt" text-align="justify"></fo:block>

            <!-- Normal text -->

            <%if (xlang.equalsIgnoreCase("INA")) {%>

            <fo:block font-size="<%=fontsize%>" text-align="justify" line-space="1pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="145mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <%
                            DTOList objects = pol.getObjects();
                            String dtTanggal = null;

                            for (int i = 0; i < objects.size(); i++) {
                                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

                                if (pol.getStProducerID().equalsIgnoreCase("983185") || pol.getStProducerID().equalsIgnoreCase("987345")) {
                                    if (Tools.compare(obj.getDtReference1(), pol.getDtPolicyDate()) >= 0) {
                                        dtTanggal = "{L-ENG dated -L}{L-INA pada tanggal -L}" + DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy");
                                    } else if (Tools.compare(obj.getDtReference1(), pol.getDtPolicyDate()) <= 0) {
                                        dtTanggal = "{L-ENG dated -L}{L-INA pada tanggal -L}" + DateUtil.getDateStr(obj.getDtReference1(), "d ^^ yyyy");
                                    } else {
                                        dtTanggal = "{L-ENG dated -L}{L-INA pada tanggal -L}" + DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy");
                                    }
                                } else {
                                    dtTanggal = "{L-ENG dated -L}{L-INA pada tanggal -L}" + DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy");
                                }
                        %>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" text-align = "justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="85mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG Bond No. : -L}{L-INA No. Bond : -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG Value : -L}{L-INA Nilai : -L}<%=JSPUtil.print(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align = "end"><%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%> </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" text-align = "justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="175mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 1. -L}{L-INA 1. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Dengan ini dinyatakan bahwa kami : -L} <fo:inline font-weight="bold"><%=JSPUtil.xmlEscape(obj.getStReference1())%></fo:inline> {L-ENG XXX -L}{L-INA yang beralamat di -L} <%=JSPUtil.printX(obj.getStReference3())%>  {L-ENG XXX -L}{L-INA sebagai Pelaksana Pekerjaan, selanjutnya disebut TERJAMIN, dan -L} <fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference6Desc())%></fo:inline> {L-ENG XXX -L}{L-INA yang beralamat di -L}<%=JSPUtil.xmlEscape(obj.getStReference7())%>{L-ENG XXX -L}{L-INA sebagai Penjamin, selanjutnya disebut sebagai PENJAMIN, bertanggung jawab dan dengan tegas terikat pada -L} <fo:inline font-weight="bold"><%=JSPUtil.xmlEscape(pol.getStCustomerName())%></fo:inline> {L-ENG XXX -L}{L-INA yang beralamat di -L} <%=JSPUtil.xmlEscape(pol.getStCustomerAddress())%>{L-ENG XXX -L}{L-INA sebagai Pemilik Pekerjaan, selanjutnya disebut PENERIMA JAMINAN atas uang sejumlah -L}<%=JSPUtil.print(pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%> {L-ENG XXX -L}{L-INA Terbilang ( -L}<%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbInsuredAmount(), 2))%> <%=JSPUtil.printX(pol.getCurrency().getStCcyDescription())%> )</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 2. -L}{L-INA 2. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Maka kami TERJAMIN dan PENJAMIN dengan ini mengikatkan diri untuk melakukan pembayaran jumlah tersebut diatas dengan baik dan benar-L}. </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 3. -L}{L-INA 3. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Bahwa TERJAMIN dengan suatu perjanjian tertulis No. <%=JSPUtil.printX(obj.getStReference10())%> tanggal <%=DateUtil.getDateStr(obj.getDtReference1(), "d ^^ yyyy")%> telah mengadakan Kontrak dengan PENERIMA JAMINAN untuk pekerjaan <%=JSPUtil.printX(obj.getStReference11())%> dengan Harga Kontrak yang telah disetujui sebesar <%=JSPUtil.print(pol.getStCurrencyCode())%> <%=JSPUtil.printX(obj.getDbReference2(), 2)%> {L-ENG XXX -L}{L-INA Terbilang ( -L}<%=NumberSpell.readNumber(JSPUtil.printX(obj.getDbReference2(), 2))%> <%=JSPUtil.printX(pol.getCurrency().getStCcyDescription())%> )-L} dan Kontrak tersebut merupakan bagian yang tidak terpisahkan dari jaminan ini.</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 4. -L}{L-INA 4. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Bahwa untuk Kontrak tersebut di atas, PENERIMA JAMINAN setuju membayar kepada TERJAMIN uang sebesar <%=JSPUtil.print(pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%> {L-ENG XXX -L}{L-INA Terbilang ( -L}<%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbInsuredAmount(), 2))%> <%=JSPUtil.printX(pol.getCurrency().getStCcyDescription())%> )-L} sebagai pembayaran uang muka sebelum pekerjaan menurut Kontrak di atas dimulai. Sebagai Jaminan terhadap pembayaran Uang Muka itu maka PENJAMIN memberikan jaminannya dengan ketentuan tersebut di bawah ini.</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 5. -L}{L-INA 5. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Surat Jaminan ini berlaku selama <% if (pol.getStPeriodLength().equalsIgnoreCase("0")) {%> 1 <% } else {%> <%=JSPUtil.printX(TotalPeriodLength, 0)%> <% }%> (<% if (pol.getStPeriodLength().equalsIgnoreCase("0")) {%> Satu <% } else {%> <%=NumberSpell.readNumber(JSPUtil.printX(TotalPeriodLength, 0))%> <% }%>) hari kalender dan efektif mulai dari tanggal <%=DateUtil.getDateStr(pol.getDtPeriodStart(), "d ^^ yyyy")%> sampai dengan tanggal <%=DateUtil.getDateStr(pol.getDtPeriodEnd(), "d ^^ yyyy")%>. -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 6. -L}{L-INA 6. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Jaminan ini berlaku apabila TERJAMIN lalai/tidak memenuhi kewajibannya atau melakukan pembayaran kembali kepada PENERIMA JAMINAN senilai Uang Muka dimaksud (yang dinyatakan dalam surat bukti penerima olehnya) atau sisa Uang Muka yang wajib dibayar menurut Kontrak tersebut. -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 7. -L}{L-INA 7. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Tuntutan pencairan atas surat Jaminan ini dilaksanakan oleh PENERIMA JAMINAN secara tertulis kepada PENJAMIN segera setelah TERJAMIN cidera janji (Wanprestasi/default) karena tidak dapat memenuhi kewajibannya atau membayar kembali Uang Muka atas sisa Uang Muka tersebut sesuai dengan syarat kontrak.-L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 8. -L}{L-INA 8. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA PENJAMIN akan membayar kepada PENERIMA JAMINAN sejumlah nilai jaminan tersebut di atas dalam waktu paling lambat 14 (empat belas) hari kerja tanpa syarat (UNCONDITIONAL) setelah menerima tuntutan penagihan secara tertulis dari PENERIMA JAMINAN berdasar Keputusan PENERIMA JAMINAN mengenai pengenaan sanksi akibat TERJAMIN cidera/wanprestasi. -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                                if (versiLama) {
                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 9. -L}{L-INA 9. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Menunjuk pada pasal 1832 KUH Perdata dengan ini ditegaskan kembali bahwa PENJAMIN melepaskan hak-hak istimewa untuk menuntut supaya harta benda TERJAMIN lebih dahulu disita dan dijual guna dapat melunasi hutangnya sebagaimana dimaksud dalam Pasal 1831 KUH Perdata. -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 10. -L}{L-INA 10. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Tuntutan pencairan terhadap PENJAMIN berdasarkan Jaminan ini harus sudah diajukan selambat-lambatnya dalam waktu 30 (tiga puluh) hari kalender sesudah berakhirnya masa berlaku Jaminan ini. -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>

                        <%
                                                                if (!versiLama) {
                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 9. -L}{L-INA 9. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Kepala KPPN yang menerbitkan SP2D Uang Muka, berdasarkan surat kuasa pemegang jaminan atau obligee dapat mengajukan tuntutan/klaim penagihan kepada penjamin sampai dengan berakhirnya masa klaim Jaminan Uang Muka. -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 10. -L}{L-INA 10. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Menunjuk pada pasal 1832 KUH Perdata dengan ini ditegaskan kembali bahwa PENJAMIN melepaskan hak-hak istimewa untuk menuntut supaya harta benda TERJAMIN lebih dahulu disita dan dijual guna dapat melunasi hutangnya sebagaimana dimaksud dalam Pasal 1831 KUH Perdata. -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 11. -L}{L-INA 11. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Tuntutan pencairan terhadap PENJAMIN berdasarkan Jaminan ini harus sudah diajukan selambat-lambatnya dalam waktu 30 (tiga puluh) hari kalender sesudah berakhirnya masa berlaku Jaminan ini. -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell>
                                <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                                <fo:block >{L-ENG Signed, sealed, and stamped in -L}{L-INA Ditanda tangani serta dibubuhkan materai di -L}
                                    <%= JSPUtil.printX(pol.getStCostCenterDesc())%><%if (!tanpaTanggal) {%>, <%=dtTanggal%><%}%>.</fo:block>
                                    <% } else {%>
                                <fo:block >{L-ENG Signed, sealed, and stamped in -L}{L-INA Ditanda tangani serta dibubuhkan materai di -L}
                                    <%= JSPUtil.printX(pol.getCostCenter(pol.getCostCenter(pol.getStCostCenterCode()).getStSubCostCenterCode()).getStDescription())%><%if (!tanpaTanggal) {%>, <%=dtTanggal%><%}%>.</fo:block>
                                    <% }%>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <%-- mulai rincian --%>

            <fo:block font-size="<%=fontsize%>" line-height="0pt" space-after.optimum="5pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
            </fo:block>

            <fo:block font-size="<%=fontsize%>" text-align = "justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="4mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="3mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-body>

                        <%--<% if (pol.getStProducerID().equalsIgnoreCase("983185") || pol.getStProducerID().equalsIgnoreCase("987345")) {%>--%>
                        <% if (tanpaRate) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% if (tanpaPrincipal) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">{L-ENG SURETY -L}{L-INA SURETY -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <%if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference6Desc())%></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>
                        <% }%>

                        <% if (!tanpaPrincipal) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">{L-ENG PRINCIPAL -L}{L-INA PRINCIPAL -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">{L-ENG SURETY -L}{L-INA SURETY -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference1())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <%if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference6Desc())%></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>
                        <% }%>
                        <% }%>

                        <%--<% if (pol.getStProducerID().equalsIgnoreCase("983185") || pol.getStProducerID().equalsIgnoreCase("987345")) {%>--%>
                        <% if (!tanpaRate) {%>
                        <fo:table-row>
                            <fo:table-cell><fo:block>Service Charge</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "end"><%=JSPUtil.printX(pol.getDbPremiTotal(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG Stamp Fee -L}{L-INA Biaya Materai -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "end"><%=JSPUtil.printX(pol.getDbTotalItemAmount("SFEE"), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% if (tanpaPrincipal) {%>
                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG Policy Cost -L}{L-INA Administrasi -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "end"><%=JSPUtil.printX(pol.getDbTotalItemAmount("PCOST"), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">{L-ENG SURETY -L}{L-INA SURETY -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="1pt"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG Total -L}{L-INA Jumlah -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "end"><%=JSPUtil.printX(pol.getDbTotalDue(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <%if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference6Desc())%></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>
                        <% }%>

                        <% if (!tanpaPrincipal) {%>
                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG Policy Cost -L}{L-INA Administrasi -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "end"><%=JSPUtil.printX(pol.getDbTotalItemAmount("PCOST"), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">{L-ENG PRINCIPAL -L}{L-INA PRINCIPAL -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">{L-ENG SURETY -L}{L-INA SURETY -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="1pt"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG Total -L}{L-INA Jumlah -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "end"><%=JSPUtil.printX(pol.getDbTotalDue(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference1())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <%if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference6Desc())%></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>
                        <% }%>
                        <% }%>

                        <% if (usingDigitalSign) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block>
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="KANTOR_ASKRIDA_<%=pol.getCostCenter(pol.getStCostCenterCode()).getTypeDescription().toUpperCase()%>_<%=pol.getCostCenter(pol.getStCostCenterCode()).getStDescription().toUpperCase()%>_<%=pol.getStPolicyNo()%>" orientation="0">
                                            <barcode:datamatrix>
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>24x24</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                                <%--<human-readable>none</human-readable>--%>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>
                                </fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell>
                                <%if (!isUsingBarcode) {%>
                                <%if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(<%=pol.getUser(pol.getUserIDSign()).getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <% } else {%>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(<%=pol.getUserApproved().getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <% }%>
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
                        </fo:table-row>
                        <% } else {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6"><fo:block space-after.optimum="60pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" text-align = "justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="4mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="3mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-body>

                        <% if (tanpaPrincipal) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%= pol.getUserIDSignName()%></fo:inline></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%=JSPUtil.printX(pol.getUserApproved().getStUserName()).toUpperCase()%></fo:inline></fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"></fo:table-cell>
                            <fo:table-cell >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0.2" line-height="0.15pt" space-after.optimum="0.5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
<%--
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center" ><%if (pol.getParaf() != null) {%><fo:leader leader-length="10mm"/><%}%><%=JSPUtil.printX(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%>                                     <%if (pol.getParaf() != null) {%>                                     <fo:external-graphic                                         content-height="scale-to-fit" height="0.3in"  content-width="0.3in" scaling="non-uniform"                                         overflow="hidden" src="url(<%=pol.getUser(pol.getParaf()).getParafFile().getStFilePath()%>)"  />                                     <%}%>                             </fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>--%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center" ><%=JSPUtil.printX(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>
                        <% }%>

                        <% if (!tanpaPrincipal) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference4())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%= pol.getUserIDSignName()%></fo:inline></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%=JSPUtil.printX(pol.getUserApproved().getStUserName()).toUpperCase()%></fo:inline></fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"></fo:table-cell>
                            <fo:table-cell >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="1pt"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0.2" line-height="0.15pt" space-after.optimum="0.5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
<%--
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference5())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center" ><%if (pol.getParaf() != null) {%><fo:leader leader-length="10mm"/><%}%><%=JSPUtil.printX(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%>                                     <%if (pol.getParaf() != null) {%>                                     <fo:external-graphic                                         content-height="scale-to-fit" height="0.3in"  content-width="0.3in" scaling="non-uniform"                                         overflow="hidden" src="url(<%=pol.getUser(pol.getParaf()).getParafFile().getStFilePath()%>)"  />                                     <%}%>                             </fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>--%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference5())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center" ><%=JSPUtil.printX(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>
                        <% }%>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% }%>

            <%--batas bhs.indo--%>
            <% }%>

            <%if (xlang.equalsIgnoreCase("ENG")) {%>

            <fo:block font-size="<%=fontsize%>" text-align="justify" line-space="1pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="145mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <%
                            DTOList objects = pol.getObjects();

                            for (int i = 0; i < objects.size(); i++) {
                                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

                        %>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" text-align = "justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="85mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Bond No. : </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>Value : <%=JSPUtil.print(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align = "end"><%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%> </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" text-align = "justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="175mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block>1.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>KNOW ALL MEN BY THIS PRESENT, that we <fo:inline font-weight="bold"><%=JSPUtil.xmlEscape(obj.getStReference1())%></fo:inline> <%=JSPUtil.printX(obj.getStReference3())%> as Contractor, hereinafter called Principal and <fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference6Desc())%></fo:inline> <%=JSPUtil.printX(obj.getStReference7())%>, as Surety hereinafter called Surety, are held and firmly bound into <fo:inline font-weight="bold"><%=JSPUtil.xmlEscape(pol.getStCustomerName())%></fo:inline> <%=pol.getStCustomerAddress()%>, as Owner hereinafter called Obligee, in the amount of maximum <%=JSPUtil.print(pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%> in words: <%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbInsuredAmount(), 2), pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getCurrency().getStCcyDescription())%> is to be paid to the said Obligee, for which the payment to Principal and THE SURETY to bind themselves, firmly by this presents.</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>2.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>Whereas THE PRINCIPAL has by written <fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference10())%></fo:inline> entered into a certain contract with THE OBLIGEE for <fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference12())%></fo:inline> at an agreed contract price of <%=JSPUtil.print(pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%> in words: <%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbInsuredAmount(), 2), pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getCurrency().getStCcyDescription())%> which agreement is inherent within this bond.</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>3.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>Whereas THE OBLIGEE has agreed to advice to THE PRINCIPAL that certain sums or sums amounting to <%=JSPUtil.print(pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%> in words: <%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbInsuredAmount(), 2), pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getCurrency().getStCcyDescription())%> as an advance payment prior to the completion of the work under the said contract. As a guarantee to such advance payment, THE SURETY agreed to furnish an advance payment bond with the conditions as follow: If THE PRINCIPAL repays in any amount of such initial advance payment (completed with an authentic receipt) due under said agreement, then this bond shall be null and void; otherwise to remain in full force and effect as from <%=DateUtil.getDateStr(pol.getDtPeriodStart(), "d ^^ yyyy")%> up to <%=DateUtil.getDateStr(pol.getDtPeriodEnd(), "d ^^ yyyy")%>.</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>4.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>Any claim on this bond shall be made in written application by THE OBLIGEE to THE SURETY promptly after THE PRINCIPAL fails to repay the advance payment under the condition of said contract. </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>5.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>Surety shall pay THE OBLIGEE in accordance with the contract, the un-repaid advance payment by THE PRINCIPAL unconditionally at the latest 30 (thirty) calendar days after having received a written claim from THE OBLIGEE.</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>6.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>With reference to Section 1832 of the Book Civil Law (Kitab Undang-Undang Hukum Perdata) herewith we reaffirm that THE SURETY shall relinquish its special right of claim on assets belonging to THE PRINCIPAL and for the seizure and sale of such assets for the discharge of his debts as required in section 1831 of the Book of Civil Law.</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>7.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>Any proceedings against THE SURETY to recover any claim hereunder must be instituted within 1 (one) month after the bond expires. </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell>
                                <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                                <fo:block >Sign and sealed in
                                    <%= JSPUtil.printX(pol.getStCostCenterDesc())%><%if (!tanpaTanggal) {%>, on <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%>.</fo:block>
                                    <% } else {%>
                                <fo:block >Sign and sealed in
                                    <%= JSPUtil.printX(pol.getCostCenter(pol.getCostCenter(pol.getStCostCenterCode()).getStSubCostCenterCode()).getStDescription())%><%if (!tanpaTanggal) {%>, on <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%>.</fo:block>
                                    <% }%>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <%-- mulai rincian --%>

            <fo:block font-size="<%=fontsize%>" line-height="0pt" space-after.optimum="5pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
            </fo:block>

            <fo:block font-size="<%=fontsize%>" text-align = "justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="4mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="3mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">PRINCIPAL</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">SURETY</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference1())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <%if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference6Desc())%></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>

                        <% if (usingDigitalSign) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block>
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="KANTOR_ASKRIDA_<%=pol.getCostCenter(pol.getStCostCenterCode()).getTypeDescription().toUpperCase()%>_<%=pol.getCostCenter(pol.getStCostCenterCode()).getStDescription().toUpperCase()%>_<%=pol.getStPolicyNo()%>" orientation="0">
                                            <barcode:datamatrix>
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>24x24</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                                <%--<human-readable>none</human-readable>--%>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>
                                </fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell>
                                <%if (!isUsingBarcode) {%>
                                <%if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(<%=pol.getUser(pol.getUserIDSign()).getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <% } else {%>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(<%=pol.getUserApproved().getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <% }%>
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
                        </fo:table-row>
                        <% } else {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block space-after.optimum="60pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" text-align = "justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="4mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="3mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference4())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%= pol.getUserIDSignName()%></fo:inline></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%=JSPUtil.printX(pol.getUserApproved().getStUserName()).toUpperCase()%></fo:inline></fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"></fo:table-cell>
                            <fo:table-cell >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="1pt"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0.2" line-height="0.15pt" space-after.optimum="0.5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
<%--
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference5())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center" ><%if (pol.getParaf() != null) {%><fo:leader leader-length="10mm"/><%}%><%=JSPUtil.printX(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%>                                     <%if (pol.getParaf() != null) {%>                                     <fo:external-graphic                                         content-height="scale-to-fit" height="0.3in"  content-width="0.3in" scaling="non-uniform"                                         overflow="hidden" src="url(<%=pol.getUser(pol.getParaf()).getParafFile().getStFilePath()%>)"  />                                     <%}%>                             </fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>--%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference5())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center" ><%=JSPUtil.printX(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% }%>

            <%--batas bhs.eng--%>
            <% }%>

            <% if (usingDigitalSign) {%>
            <fo:block font-size="6pt"
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

        </fo:flow>
    </fo:page-sequence>
</fo:root>