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
            String param[] = attached.split("[\\|]");

            boolean isAttached = param[0].equalsIgnoreCase("2") ? true : false;
            boolean tanpaTanggal = param[0].equalsIgnoreCase("3") ? true : false;
            boolean tanpaRate = param[0].equalsIgnoreCase("7") ? true : false;
            boolean klausulaTerlampir = param[0].equalsIgnoreCase("8") ? true : false;
            boolean versiLama = param[0].equalsIgnoreCase("b") ? true : false;

            if (attached.length() > 1) {
                if (param[1] != null) {
                    isAttached = !isAttached ? param[1].equalsIgnoreCase("2") ? true : false : isAttached;
                    tanpaTanggal = !tanpaTanggal ? param[1].equalsIgnoreCase("3") ? true : false : tanpaTanggal;
                    tanpaRate = !tanpaRate ? param[1].equalsIgnoreCase("7") ? true : false : tanpaRate;
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
            <fo:region-before extent="3cm"/>2
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
                            <fo:table-cell><fo:block>{L-ENG Bond No. : -L}{L-INA No. Bond : -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG Value : -L}{L-INA Nilai Bond : -L}</fo:block></fo:table-cell>
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
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Dengan ini dinyatakan, bahwa kami : -L} <fo:inline font-weight="bold"><%=JSPUtil.xmlEscape(obj.getStReference1().toUpperCase())%></fo:inline> {L-ENG XXX -L}{L-INA,-L} <fo:inline font-weight="bold"> <%=JSPUtil.printX(obj.getStReference3())%> </fo:inline> {L-ENG XXX -L}{L-INA sebagai Penyedia, selanjutnya disebut TERJAMIN, dan -L} <fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference6Desc())%></fo:inline> {L-ENG XXX -L}{L-INA, -L}<%=JSPUtil.printX(obj.getStReference7())%>{L-ENG XXX -L}{L-INA sebagai PENJAMIN, selanjutnya disebut sebagai PENJAMIN, bertanggung jawab dan dengan tegas terikat pada -L} <fo:inline font-weight="bold"><%=JSPUtil.xmlEscape(pol.getStCustomerName())%></fo:inline> {L-ENG XXX -L}{L-INA -L}{L-ENG XXX -L}{L-INA sebagai Pemilik Pekerjaan, selanjutnya disebut PENERIMA JAMINAN atas uang sejumlah -L}<%=JSPUtil.print(pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%> {L-ENG XXX -L}{L-INA ( -L}<%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbInsuredAmount(), 2))%> <%=JSPUtil.printX(pol.getCurrency().getStCcyDescription())%> )</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 2. -L}{L-INA 2. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Maka kami, TERJAMIN dan PENJAMIN dengan ini mengikatkan diri untuk melakukan pembayaran jumlah tersebut diatas dengan baik dan benar bilamana TERJAMIN tidak memenuhi kewajiban dalam melaksanakan Pekerjaan : -L} <fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference11())%> </fo:inline> {L-ENG XXX -L}{L-INA yang telah dipercayakan kepadanya atas dasar Surat Perjanjian / Kontrak dari Penerima Jaminan No.:-L} <fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference10())%> </fo:inline> tanggal <fo:inline font-weight="bold"> <%=DateUtil.getDateStr(obj.getDtReference1(), "d ^^ yyyy")%> </fo:inline>  </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 3. -L}{L-INA 3. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Surat Jaminan ini berlaku selama <% if (pol.getStPeriodLength().equalsIgnoreCase("0")) {%> 1 <% } else {%> <%=JSPUtil.printX(TotalPeriodLength, 0)%> <% }%> (<% if (pol.getStPeriodLength().equalsIgnoreCase("0")) {%> Satu <% } else {%> <%=NumberSpell.readNumber(JSPUtil.printX(TotalPeriodLength, 0))%> <% }%>) hari kalender dan efektif mulai dari tanggal <fo:inline font-weight="bold"> <%=DateUtil.getDateStr(pol.getDtPeriodStart(), "d ^^ yyyy")%> </fo:inline> sampai dengan tanggal <fo:inline font-weight="bold"> <%=DateUtil.getDateStr(pol.getDtPeriodEnd(), "d ^^ yyyy")%>. </fo:inline> -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 4. -L}{L-INA 4. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA PENJAMIN akan membayar kepada PENERIMA JAMINAN maksimal sejumlah nilai jaminan tersebut di atas atau sisa Uang Muka yang belum dikembalikan TERJAMIN dalam waktu paling lambat 14 (empat belas) hari kerja tanpa syarat (Unconditional) setelah menerima tuntutan pencairan secara tertulis dari PENERIMA JAMINAN berdasar Keputusan PENERIMA JAMINAN mengenai pengenaan sanksi akibat TERJAMIN cidera janji. -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 5. -L}{L-INA 5. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Menunjuk pada pasal 1832 KUH Perdata dengan ini ditegaskan kembali bahwa PENJAMIN melepaskan hak-hak istimewa untuk menuntut supaya harta benda TERJAMIN lebih dahulu disita dan dijual guna dapat melunasi hutangnya sebagaimana dimaksud dalam Pasal 1831 KUH Perdata. -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 6. -L}{L-INA 6. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Tuntutan pencairan terhadap PENJAMIN berdasarkan Jaminan ini harus sudah diajukan selambat-lambatnya dalam waktu 30 (tiga puluh) hari kalender sesudah berakhirnya masa berlaku Jaminan ini. -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell>
                                <fo:block > {L-ENG XXX -L}{L-INA Dikeluarkan di -L}  <%= JSPUtil.printX(pol.getStCostCenterDesc())%> {L-ENG XXX -L}{L-INA ditanda tangani serta dibubuhkan materai -L}
                                    <%if (!tanpaTanggal) {%>, {L-ENG XXX -L}{L-INA pada tanggal -L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%>.</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%--
                        <fo:table-row>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell>
                                <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) { %>
                                <fo:block > {L-ENG XXX -L}{L-INA Dikeluarkan di -L}  <%= JSPUtil.printX(pol.getStCostCenterDesc())%> {L-ENG XXX -L}{L-INA ditanda tangani serta dibubuhkan materai -L}
                                <%if(tanpaTanggal){%>, {L-ENG XXX -L}{L-INA pada tanggal -L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%><%}%>.</fo:block>
                                <% } else { %>
                                <fo:block > {L-ENG XXX -L}{L-INA Dikeluarkan di -L}Jakarta {L-ENG XXX -L}{L-INA ditanda tangani serta dibubuhkan materai -L}
                                <%if(tanpaTanggal){%>, {L-ENG XXX -L}{L-INA pada tanggal -L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%><%}%>.</fo:block>
                                <% } %>
                            </fo:table-cell>
                        </fo:table-row>
                        --%>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <%-- mulai rincian --%> 

            <fo:block font-size="<%=fontsize%>" line-height="0pt" space-after.optimum="5pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
            </fo:block>

            <% if (tanpaRate) {%>
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
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">{L-ENG TERJAMIN -L}{L-INA TERJAMIN -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">{L-ENG PENJAMIN -L}{L-INA PENJAMIN -L}</fo:block></fo:table-cell>
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
                            <fo:table-cell number-columns-spanned="6"><fo:block space-after.optimum="60pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>

                    </fo:table-body>
                </fo:table>
            </fo:block>
            <% }%>

            <% if (!tanpaRate) {%>
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

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG Policy Cost -L}{L-INA Administrasi -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "end"><%=JSPUtil.printX(pol.getDbTotalItemAmount("PCOST"), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">{L-ENG TERJAMIN -L}{L-INA TERJAMIN -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">{L-ENG PENJAMIN -L}{L-INA PENJAMIN -L}</fo:block></fo:table-cell>
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
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
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
                            <fo:table-cell number-columns-spanned="5" ><fo:block space-after.optimum="65pt"></fo:block></fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align = "center" space-after.optimum="45pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% }%>

                    </fo:table-body>
                </fo:table>
            </fo:block>
            <% }%>

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
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference4())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <%--<fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=pol.getUserApproved().getStUserName().toUpperCase()%></fo:block></fo:table-cell>--%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%= pol.getUserIDSignName()%></fo:inline></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%=JSPUtil.printX(pol.getUserApproved().getStUserName()).toUpperCase()%></fo:inline></fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell number-columns-spanned="1" >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="1pt"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="1" >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0.2" line-height="0.15pt" space-after.optimum="0.5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference5())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center" ><%if (pol.getParaf() != null) {%><fo:leader leader-length="10mm"/><%}%><%=JSPUtil.printX(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%>                                     <%if (pol.getParaf() != null) {%>                                     <fo:external-graphic                                         content-height="scale-to-fit" height="0.3in"  content-width="0.3in" scaling="non-uniform"                                         overflow="hidden" src="url(<%=pol.getUser(pol.getParaf()).getParafFile().getStFilePath()%>)"  />                                     <%}%>                             </fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <%--
            <% if (tanpaNama&&tanpaNamaTanggal) { %>
            <fo:block font-size="<%=fontsize%>" text-align = "justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"> PT. ASURANSI BANGUN ASKRIDA </fo:block></fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="70pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%  if(SessionManager.getInstance().getSession().getStBranch()!=null) {
                            if(!SessionManager.getInstance().getSession().getStBranch().equalsIgnoreCase("00")&&pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")){ %>

                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><%= Parameter.readString("BRANCH_SIGN_"+SessionManager.getInstance().getSession().getStBranch())%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <% }} %>

                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell number-columns-spanned="1" >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%  if(SessionManager.getInstance().getSession().getStBranch()!=null) {
                            if(!SessionManager.getInstance().getSession().getStBranch().equalsIgnoreCase("00")&&pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")){ %>

                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Pimpinan</fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <% }} %>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% } %>
            --%>

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


