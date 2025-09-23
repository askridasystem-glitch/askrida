<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         com.crux.common.parameter.Parameter,
         com.crux.util.fop.FOPUtil,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%
            System.out.println("lewattttt siniiiiiiiiiiiiiiiiiiiiiiiiiiiix");
            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");
            final String attached = (String) request.getAttribute("attached");
            String param[] = attached.split("[\\|]");

            boolean isAttached = param[0].equalsIgnoreCase("2") ? true : false;
            boolean tanpaTanggal = param[0].equalsIgnoreCase("3") ? true : false;
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
            boolean coMember = pol.isCoMember();

            String nopol = pol.getStPolicyNo();

            String jenis_pol = pol.getStPolicyTypeDesc2().toUpperCase();
            System.out.println("lewattttt siniiiiiiiiiiiiiiiiiiiiiiiiiiiix-------------");


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
                               page-height="30.5cm"
                               page-width="21cm"
                               margin-top="2.5cm"
                               margin-bottom="1cm"
                               margin-left="2cm"
                               margin-right="2cm">

            <fo:region-body margin-top="2cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="1.5cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">


        <fo:flow flow-name="xsl-region-body">

            <%
                        System.out.println("lewattttt sini lagiiiiiiiiiiix-------------1");

                        if (!effective) {%>
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" text-align="justify" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>

            <fo:block font-size="16pt"
                      text-align="center">

            </fo:block>

            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" text-align="justify" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>
            <% }%>

            <%

                        if (isPreview) {%>
            <fo:block font-size="20pt"
                      line-height="16pt"
                      color="blue"
                      text-align="center"
                      padding-top="10pt">
                PREVIEW MODE
            </fo:block>
            <% }%>            

            <% if (!effective) {%>
            <fo:block font-size="14pt"
                      line-height="14pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>

            <% if (coMember) {%>

            <fo:block font-size="14pt"
                      line-height="14pt" space-after.optimum="5pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                {L-INA CO-MEMBER -L}{L-ENG CO-MEMBER -L}
            </fo:block>

            <% } else {%>

            <fo:block font-size="14pt"
                      line-height="14pt" space-after.optimum="5pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                {L-INA IKHTISAR PERTANGGUNGAN -L}{L-ENG POLICY SCHEDULE -L}
            </fo:block>

            <% }%>

            <fo:block font-size="14pt"
                      line-height="14pt" space-after.optimum="10pt"
                      color="black"
                      text-align="center"
                      padding-top="0pt">
                ASURANSI KREDIT KONSTRUKSI DAN PENGADAAN BARANG/JASA
                <!--                {L-INA ASURANSI -L}<%=jenis_pol%>{L-ENG INSURANCE -L}-->
            </fo:block>


            <!-- GARIS -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" text-align="justify"></fo:block>

            <!-- defines text title level 1-->
            <% if (coMember) {%>

            <fo:block font-size="<%=fontsize%>"
                      text-align="center">

            </fo:block>

            <!-- GARIS  -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" text-align="justify"></fo:block>
            <% }%>
            <!-- Normal text -->

            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>"
                      line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify">





                <fo:table table-layout="fixed">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="55mm"/>
                    <fo:table-column column-width="4mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="8mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="4mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column />
                    <fo:table-body>


                        <%

                                    final DTOList objects = pol.getObjects();

                                    for (int i = 0; i < objects.size(); i++) {
                                        InsurancePolicyObjDefaultView inspolobjct = (InsurancePolicyObjDefaultView) objects.get(i);

                        %>


                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block 
                                    text-align="start">
                                    {L-ENG Policy No.-L}{L-INA No. Polis -L} <%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>



                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" space-before.optimum="5pt">
                                    PT ASURANSI BANGUN ASKRIDA, berkedudukan di <%=Parameter.readString("ASKRIDA_ADDRESS")%> yang selanjutnya disebut sebagai PENANGGUNG, dengan ini menanggung :
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>




                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block space-after.optimum="5pt">{L-ENG To of Insured-L}{L-INA Kepada Tertanggung-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="5"><fo:block space-after.optimum="5pt"><%=JSPUtil.printX(inspolobjct.getStReference1Desc())%></fo:block></fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="5"><fo:block space-after.optimum="5pt"><%=JSPUtil.printX(inspolobjct.getStReference3())%></fo:block></fo:table-cell>
                        </fo:table-row>


                        <!--ini yang ke 1-->
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">1.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="7">
                                <fo:block  space-after.optimum="5pt" >
                                    Beradasarkan Surat Permohonan Penutupan Asuransi Kredit Konstruksi dan Pengadaan Barang/Jasa No. <%=JSPUtil.printX(inspolobjct.getStReference30())%><%--tanggal, <%=JSPUtil.print(DateUtil.getDateStr(pol.getParentPolicy().getDtPolicyDate(), "dd/MM/yyyy"))%>--%>  untuk dan atas nama pertanggungan kredit DEBITUR
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>



                        <fo:table-row>
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >Nama </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="5"><fo:block ><%=JSPUtil.printX(inspolobjct.getStReference3Desc())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"> Alamat</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="5"><fo:block space-after.optimum="5pt"><%=JSPUtil.printX(inspolobjct.getStReference15())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <!--ini yang ke 2-->

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">2.</fo:block></fo:table-cell>

                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    Sesuai Surat Konfirmasi Persetujuan dari PENANGGUNG Nomor, <%=JSPUtil.printX(inspolobjct.getStReference28())%>  Tanggal, <%=JSPUtil.print(DateUtil.getDateStr(inspolobjct.getDtReference7(), "dd/MM/yyyy"))%> dengan data sebagai berikut  :
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>



                        <fo:table-row>
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >a.	Nomor Perjanjian Kredit </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="6"><fo:block ><%=JSPUtil.printX(inspolobjct.getStReference11())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >b.	Plafond Kredit/Nilai Pertanggungan </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="6"><fo:block ><%=JSPUtil.printX(inspolobjct.getDbReference1(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >c.	Jangka Waktu Pertanggungan </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="6"><fo:block ><%=JSPUtil.print(DateUtil.getDateStr(inspolobjct.getDtReference4(), "dd/MM/yyyy"))%> - <%=JSPUtil.print(DateUtil.getDateStr(inspolobjct.getDtReference5(), "dd/MM/yyyy"))%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>d.	Penggunaan Plafon                   </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="6"><fo:block><%=JSPUtil.xmlEscape(inspolobjct.getStReference19())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">e.	Jangka Waktu Pekerjaan                   </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">:</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="6"><fo:block space-after.optimum="5pt"><%=JSPUtil.print(DateUtil.getDateStr(inspolobjct.getDtReference3(), "dd/MM/yyyy"))%> - <%=JSPUtil.print(DateUtil.getDateStr(inspolobjct.getDtReference6(), "dd/MM/yyyy"))%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <!--ini yang ke 3-->

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">3.</fo:block></fo:table-cell>

                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    Perhitungan Biaya Pertanggungan :
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >a.	Premi Pertanggungan </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >b.	Bea Meterai </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDSFee(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">c.	Biaya Administrasi  </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt" text-align="end"><%=JSPUtil.printX(pol.getDbNDPCost(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2">
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end">{L-ENG Sum Insured-L}{L-INA Jumlah Biaya Pertanggungan -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(pol.getDbPremiTotal(), BDUtil.add(pol.getDbNDPCost(), pol.getDbNDSFee())), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>



                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">4.</fo:block></fo:table-cell>

                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    Ruang Lingkup Jaminan sebagaimana tersebut dalam Perjanjian Kerjasama (PKS) Polis Asuransi Kredit Konstruksi dan Pengadaan Barang/Jasa terlampir.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <% }%>
                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% if (usingDigitalSign) {%>
            <fo:block font-size="6pt"
                      space-after.optimum="3pt"
                      text-align="justify" >
                Dokumen ini dicetak secara sistem dan dinyatakan sah.
            </fo:block>
            <% }%>

            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="3pt"></fo:block>


            <fo:block font-size="6pt" space-after.optimum="10pt">
                <%if (!tanpaTanggal) {%>
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "dd/MM/yyyy HH:mm:ss"))%>
                <% } %>
                <% if (effective) {%>
                PrintStamp:<%=pol.getStPrintCode()%> PrintCode:<%=pol.getStPrintStamp()%> Page:<fo:page-number/> Cetakan : <%=pol.getStPrintCounter()%>
                <% }%>
                <% if (usingDigitalSign) {%>
                <%if (!isUsingBarcode) {%>  [<%=JSPUtil.print(pol.getStSignCode())%>] <%}%>
                <% }%>
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="90mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L}
                                    <%= JSPUtil.printX(pol.getStCostCenterDesc())%><%if (!tanpaTanggal) {%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%></fo:block>
                                    <% } else {%>
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L}
                                    Jakarta<%if (!tanpaTanggal) {%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%></fo:block>
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
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="70mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-body>

                        <% if (usingDigitalSign) {%>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>
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
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <%--<% if (pol.getUserApproved().getFile().getStFilePath()!=null) { %>--%>
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
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <%--
                            <% } else { %>
                            <fo:table-cell>
                                <fo:block text-align = "center" space-after.optimum="65pt"></fo:block>
                            </fo:table-cell>
                            <% } %>
                            --%>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <%--<fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=pol.getUserApproved().getStUserName().toUpperCase()%></fo:block></fo:table-cell>--%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%= pol.getUserIDSignName()%></fo:inline></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%=JSPUtil.printX(pol.getUserApproved().getStUserName()).toUpperCase()%></fo:inline></fo:block></fo:table-cell>
                            <% }%>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center" ><%if (pol.getParaf() != null) {%><fo:leader leader-length="10mm"/><%}%><%=JSPUtil.printX(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%>                                     <%if (pol.getParaf() != null) {%>                                     <fo:external-graphic                                         content-height="scale-to-fit" height="0.3in"  content-width="0.3in" scaling="non-uniform"                                         overflow="hidden" src="url(<%=pol.getUser(pol.getParaf()).getParafFile().getStFilePath()%>)"  />                                     <%}%>                             </fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
                            <% }%>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>

                    </fo:table-body>
                </fo:table>
            </fo:block>

        </fo:flow>
    </fo:page-sequence>
</fo:root>