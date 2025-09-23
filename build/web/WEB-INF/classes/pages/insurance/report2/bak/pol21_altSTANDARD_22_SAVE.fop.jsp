<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         java.math.BigDecimal,
         com.webfin.entity.model.EntityView,
         com.webfin.ar.model.ARTaxView,
         java.util.Date,
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

            String jenis_pol = pol.getStPolicyTypeDesc2().toUpperCase();
            String nama = "";
            String usia = "";
            String lama = "";
            Date tglLahir = null;
            Date tglMulaiKreasi = null;
            Date tglAkhirKreasi = null;
            BigDecimal insured = null;

            String gl_code = pol.getEntity().getStGLCode();

            String desc = pol.getStKreasiTypeDesc().replaceAll(pol.getStKreasiTypeDesc().substring(0, 4), " ");

//if (true) throw new NullPointerException();
            final String digitalsign = (String) request.getAttribute("digitalsign");
            boolean usingDigitalSign = false;
            if (digitalsign != null) {
                if (digitalsign.equalsIgnoreCase("Y")) {
                    usingDigitalSign = true;
                }
            }

            boolean isUsingBarcode = Parameter.readString("REPORT_SIGN_TYPE").equalsIgnoreCase("BARCODE");
            String originalWatermarkPath = Parameter.readString("DIGITAL_POLIS_ORI_PIC");
            String duplicateWatermarkPath = Parameter.readString("DIGITAL_POLIS_DUPLICATE_PIC");
            String copyWatermarkPath = Parameter.readString("DIGITAL_POLIS_COPY_PIC");
            String askridaLogoPath = Parameter.readString("DIGITAL_POLIS_LOGO_PIC");
            String askridaLogoBlackWhitePath = Parameter.readString("DIGITAL_POLIS_LOGOBW_PIC");
            String alamatLogoPath = Parameter.readString("DIGITAL_POLIS_ALAMAT_PIC");
            String alamatLogoBlackWhitePath = Parameter.readString("DIGITAL_POLIS_ALAMATBW_PIC");

            //BUAT NOTA
            String tax = "";
            String fee = "";
            String fee_base = "";
            BigDecimal tax_amount = new BigDecimal(0);
            BigDecimal com_amount = new BigDecimal(0);
            boolean cek = false;
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
    <%-- DESIGN POLIS--%>
    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->

        <fo:simple-page-master master-name="only"
                               page-height="30cm"
                               page-width="21cm"
                               margin-top="1cm"
                               margin-left="2cm"
                               margin-right="2cm">

            <!-- WATERMARK BACKGROUND -->
            <fo:region-body margin-top="0cm" margin-bottom="0cm" background-image="file:///<%=watermarkPath%>"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1" force-page-count="no-force">

        <fo:static-content flow-name="xsl-region-after">
            <fo:block text-align="end"
                      font-size="6pt"
                      line-height="12pt" >
                Insurance Policy - PT. Asuransi Bangun Askrida NOMERATOR : <%=JSPUtil.printX(pol.getStNomerator())%> PrintCode:<%=pol.getStPrintCode()%> Page:<fo:page-number/>
            </fo:block>
        </fo:static-content>

        <fo:flow flow-name="xsl-region-body">

            <!-- LOGO ASKRIDA -->
            <fo:block text-align = "center">
                <fo:external-graphic
                    content-height="scale-to-fit"
                    height="1.00in" content-width="1.00in"
                    scaling="non-uniform" src="url(file:///<%=logoAskridaPath%>)"  />
            </fo:block>

            <% if (!effective) {%>
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" text-align="justify" space-before.optimum="2pt" space-after.optimum="2pt"></fo:block>

            <fo:block font-size="16pt"
                      text-align="center">
                {L-ENG Policy No.-L}{L-INA No. Polis -L} <%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%>
            </fo:block>

            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" text-align="justify" space-before.optimum="2pt" space-after.optimum="2pt"></fo:block>
            <% }%>

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
            <fo:block font-size="16pt"
                      line-height="16pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>

            <!-- GARIS  -->

            <!-- defines text title level 1-->

            <fo:block font-size="16pt" font-family="TAHOMA"
                      line-height="16pt" space-after.optimum="5pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                {L-INA IKHTISAR PERTANGGUNGAN -L}{L-ENG POLICY SCHEDULE -L}
            </fo:block>

            <fo:block font-size="16pt" font-family="TAHOMA"
                      line-height="16pt" space-after.optimum="10pt"
                      color="black"
                      text-align="center"
                      padding-top="0pt">
                {L-INA ASURANSI -L}<%=jenis_pol.replaceAll("PA", " ")%>{L-ENG INSURANCE -L}
            </fo:block>

            <!-- Normal text -->

            <!-- defines text title level 1-->


            <!-- Normal text -->

            <!-- GARIS  -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-before.optimum="1pt" space-after.optimum="10pt" text-align="justify"></fo:block>

            <!-- Normal text -->

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block font-weight="bold">{L-ENG Policy Number :  -L}{L-INA Nomor Polis :   -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-weight="bold"><%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end" font-weight="bold">{L-ENG Entity. No.   -L}{L-INA Entity. No.   -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-weight="bold"><%= pol.getStEntityID()%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end" font-weight="bold">{L-ENG GL Code No.   -L}{L-INA GL Code No.   -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-weight="bold"><%= JSPUtil.printX(gl_code)%></fo:block></fo:table-cell>
                        </fo:table-row>       

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block space-before.optimum="2pt" space-after.optimum="2pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify">
                Perseroan Terbatas ASURANSI BANGUN ASKRIDA, selanjutnya disebut ASKRIDA, menanggung atas dasar syarat-syarat seperti dalam polis ini :
            </fo:block>

            <fo:block space-before.optimum="2pt" space-after.optimum="2pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG NAME -L}{L-INA NAMA -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-weight="bold"><%= JSPUtil.xmlEscape(pol.getStCustomerName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-before.optimum="2pt" space-after.optimum="2pt" text-align="justify"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG ADDRESS -L}{L-INA ALAMAT -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-weight="bold"><%= JSPUtil.xmlEscape(pol.getStCustomerAddress())%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block space-before.optimum="2pt" space-after.optimum="2pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify">
                seterusnya dinyatakan sebagai Penanda Tangan perjanjian, untuk pembayaran jumlah-jumlah uang pertanggungan seperti tersebut dibawah ini dalam hal meninggal Dunia jika :
            </fo:block>

            <fo:block space-before.optimum="2pt" space-after.optimum="2pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <%
                                            final DTOList objects3 = pol.getObjects();
                                            for (int j = 0; j < objects3.size(); j++) {
                                                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects3.get(j);

                                                nama = obj.getStReference1();

                                                usia = obj.getStReference2();

                                                lama = obj.getStReference6();

                                                tglLahir = obj.getDtReference1();

                                                tglMulaiKreasi = obj.getDtReference2();

                                                tglAkhirKreasi = obj.getDtReference3();

                                                insured = obj.getDbObjectInsuredAmount();

                        %>   

                        <% }%>                 

                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG Member-L}{L-INA Peserta-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <% if (objects3.size() == 1) {%>
                            <fo:table-cell ><fo:block ><%= JSPUtil.printX(nama)%></fo:block></fo:table-cell>
                            <% } else {%>             
                            <fo:table-cell ><fo:block >{L-ENG See Attached-L}{L-INA Terlampir-L} </fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-after.optimum="5pt" text-align="justify"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG Birth Date-L}{L-INA Tanggal Lahir-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <% if (objects3.size() == 1) {%>
                            <fo:table-cell ><fo:block ><%=DateUtil.getDateStr(tglLahir, "d ^^ yyyy")%></fo:block></fo:table-cell>
                            <% } else {%>             
                            <fo:table-cell ><fo:block >{L-ENG See Attached-L}{L-INA Terlampir-L} </fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-after.optimum="5pt" text-align="justify"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG Age-L}{L-INA Usia-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <% if (objects3.size() == 1) {%>
                            <fo:table-cell ><fo:block ><%= JSPUtil.printX(usia)%></fo:block></fo:table-cell>
                            <% } else {%>             
                            <fo:table-cell ><fo:block >{L-ENG See Attached-L}{L-INA Terlampir-L} </fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-after.optimum="5pt" text-align="justify"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG Liquidity-L}{L-INA Pencairan-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <% if (objects3.size() == 1) {%>
                            <fo:table-cell ><fo:block ><%=DateUtil.getDateStr(tglMulaiKreasi, "d ^^ yyyy")%> s/d <%=DateUtil.getDateStr(tglAkhirKreasi, "d ^^ yyyy")%></fo:block></fo:table-cell>
                            <% } else {%>             
                            <fo:table-cell ><fo:block >{L-ENG See Attached-L}{L-INA Terlampir-L} </fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-after.optimum="5pt" text-align="justify"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Insured Amount-L}{L-INA Nilai Pertanggungan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell> 
                            <% if (objects3.size() == 1) {%>
                            <fo:table-cell ><fo:block ><%= JSPUtil.printX(pol.getStCurrencyCode())%> <%=JSPUtil.printX(insured, 0)%></fo:block></fo:table-cell>
                            <% } else {%>             
                            <fo:table-cell ><fo:block >{L-ENG See Attached (<%= JSPUtil.printX(pol.getStCurrencyCode())%> <%= JSPUtil.printX(pol.getDbInsuredAmount(), 2)%>) -L}{L-INA Terlampir (<%= JSPUtil.printX(pol.getStCurrencyCode())%> <%= JSPUtil.printX(pol.getDbInsuredAmount(), 2)%>)-L} </fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-after.optimum="5pt" text-align="justify"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENGType Of Credit-L}{L-INAKondisi Pertanggungan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell> 
                            <% if (objects3.size() == 1) {%>
                            <fo:table-cell ><fo:block ><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <% } else {%>             
                            <fo:table-cell ><fo:block >{L-ENG See Attached (<%= JSPUtil.printX(desc)%>)-L}{L-INA Terlampir (<%= JSPUtil.printX(desc)%>)-L} </fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-after.optimum="5pt" text-align="justify"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENGPeriod-L}{L-INAJangka Waktu-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell> 
                            <% if (objects3.size() == 1) {%>
                            <fo:table-cell ><fo:block ><%=JSPUtil.printX(lama)%> bulan</fo:block></fo:table-cell>
                            <% } else {%>             
                            <fo:table-cell ><fo:block >{L-ENG See Attached (<%= JSPUtil.printX(desc)%>)-L}{L-INA Terlampir (<%= JSPUtil.printX(desc)%>)-L} </fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>

                        <%
                                            InsurancePolicyObjectView obj2 = (InsurancePolicyObjectView) objects3.get(0);

                                            DTOList cover = obj2.getCoverage();

                                            for (int r = 0; r < cover.size(); r++) {

                                                InsurancePolicyCoverView cov = (InsurancePolicyCoverView) cover.get(r);

                                                if (!cov.getStInsuranceCoverID().equalsIgnoreCase("78")) {
                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <% if (objects3.size() == 1) {%>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(cov.getStInsuranceCoverDesc2())%></fo:block></fo:table-cell>
                            <% } else {%>             
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>
                        <% } else {%>
                        <fo:table-row>
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }
                                            }%>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block space-before.optimum="2pt" space-after.optimum="2pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify">
                selanjutnya dinyatakan sebagai Tertanggung, mengalami resiko kematian yang disebabkan oleh kecelakaan atau sebab-sebab lain, kecuali kematian karena :
            </fo:block>

            <fo:block space-before.optimum="2pt" space-after.optimum="2pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block >-</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >bunuh diri atau dihukum mati oleh Pengadilan / Pejabat yang berwenang.</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block >-</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >karena terlibat dalam perkelahian, kecuali sebagai seseorang yang mempertahankan diri.</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block >-</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >karena akibat kecelakaan pesawat penumpang pesawat udara yang tidak diselenggarakan oleh perusahaan dengan jadwal tetap dan teratur.</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block >-</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >karena akibat perang, perang saudara, perbuatan kekerasan dalam pemberontakan, huru-hara, pengacauan dan perbuatan teror lainnya.</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block >-</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >sebagai akibat perbuatan kejahatan yang dilakukan dengan sengaja ataupun kekhilafan besar oleh mereka yang berkepentingan dalam polis dan ini / atau ahli waris lainnya.</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block space-before.optimum="2pt" space-after.optimum="2pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify">
                Pembayaran ganti kerugian akan dilakukan kepada : 
            </fo:block>

            <fo:block space-before.optimum="2pt" space-after.optimum="2pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="center">
                <fo:inline font-weight="bold"><%= JSPUtil.printX(pol.getStCustomerName())%></fo:inline></fo:block>

            <fo:block space-before.optimum="2pt" space-after.optimum="2pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="center">
                QQ. NASABAH DAN / ATAU AHLI WARIS SAH YANG DITUNJUK 
            </fo:block>

            <fo:block space-before.optimum="2pt" space-after.optimum="2pt" text-align="justify"></fo:block>

            <% if (pol.getStCoverTypeCode().equalsIgnoreCase("COINSOUTSELF")) {%>	

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column />
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell >
                                <fo:list-block space-before.optimum="2pt" space-after.optimum="2pt">

                                    <fo:list-item>
                                        <fo:list-item-label end-indent="label-end()">
                                            <fo:block>&#x2022;</fo:block>
                                        </fo:list-item-label>
                                        <fo:list-item-body start-indent="body-start()">
                                            <fo:block font-size="<%=fontsize%>" font-family="TAHOMA">
                                                <fo:inline text-decoration="none">Klausula Ko-Asuransi</fo:inline>
                                            </fo:block>
                                        </fo:list-item-body>
                                    </fo:list-item>
                                </fo:list-block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify">
                Dicatat dan disepakati oleh anggota konsorsium, untuk penerimaan premi didistribusikan oleh Broker dan pembayaran klaim dilakukan oleh masing-masing anggota konsorsium sesuai dengan saham masing-masing anggota kepada Broker.
            </fo:block>

            <fo:block space-before.optimum="2pt" space-after.optimum="2pt" text-align="justify"></fo:block>


            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed" space-before.optimum="10pt" space-after.optimum="10pt">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>	

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center" >No</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center" >Nama</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center" >Share</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>	   

                        <%
                             final DTOList coins = pol.getCoins2();
                             for (int f = 0; f < coins.size(); f++) {
                                 InsurancePolicyCoinsView coi = (InsurancePolicyCoinsView) coins.get(f);
                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center" ><%= String.valueOf(f + 1)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block ><%= coi.getStEntityName()%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end" ><%= JSPUtil.printX(coi.getDbSharePct(), 2)%> %</fo:block></fo:table-cell>
                        </fo:table-row> 
                        <% }%>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>	  

                    </fo:table-body>
                </fo:table>
            </fo:block>


            <% } else {%>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column />
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell >
                                <fo:list-block space-after.optimum="5pt">

                                    <%
                                         final DTOList clausules = pol.getReportClausules();
                                         for (int i = 0; i < clausules.size(); i++) {
                                             InsurancePolicyClausulesView cl = (InsurancePolicyClausulesView) clausules.get(i);

                                             if (!cl.isSelected()) {
                                                 continue;
                                             }

                                    %>

                                    <fo:list-item>
                                        <fo:list-item-label end-indent="label-end()">
                                            <fo:block>&#x2022;</fo:block>
                                        </fo:list-item-label>
                                        <fo:list-item-body start-indent="body-start()">
                                            <fo:block font-size="<%=fontsize%>" font-family="TAHOMA">
                                                <fo:inline text-decoration="none"><%=cl.getStDescription()%></fo:inline>
                                            </fo:block>
                                        </fo:list-item-body>
                                    </fo:list-item>
                                    <%}%>
                                </fo:list-block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block>


            <%
                                }
            %> 

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <% if (pol.getStWarranty() != null) {%>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Warranty/Under Condition-L}{L-INA Syarat Tambahan/Kondisi Lainnya-L} </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block wrap-option="yes-wrap" linefeed-treatment="preserve"
                                                      white-space-treatment="preserve" white-space-collapse="false"><%= JSPUtil.printX(pol.getStWarranty())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%> 

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="7mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG Premium -L}{L-INA Premi -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%= JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(pol.getDbPremiTotalAfterDisc(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% if (pol.getDbNDPCost() != null) {%>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG Policy Cost -L}{L-INA Biaya Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%= JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(pol.getDbNDPCost(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>
                        <% if (pol.getDbNDSFee() != null) {%>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG Stamp Cost-L}{L-INA Biaya Materai -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%= JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(pol.getDbNDSFee(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>         

                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell>
                                <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell>
                                <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG Total -L}{L-INA Jumlah -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%= JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end" font-weight="bold"><%= JSPUtil.printX(pol.getDbTotalDue(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

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
<%--
            <!-- ALAMAT ASKRIDA -->
            <fo:block-container height="3.2cm" width="17cm" top="25.7cm" left="0cm" position="absolute">
                <fo:block text-align = "center">
                    <fo:external-graphic
                        scaling="non-uniform" src="url(file:///<%=alamatAskridaPath%>)"  />
                </fo:block>
            </fo:block-container>
--%>
        </fo:flow>
    </fo:page-sequence>
    <%-- END DESIGN HALAMAN 1 (ORIGINAL)--%>

    <%
                }
    %>

    <%-- DESIGN KLAUSULA--%>
    <%
                watermarkPath = null;
                logoAskridaPath = null;
                alamatAskridaPath = null;

                for (int x = 0; x < 1; x++) {
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
    <fo:layout-master-set>

        <!-- layout for the first page -->

        <fo:simple-page-master master-name="first"
                               page-height="30cm"
                               page-width="21cm"
                               margin-bottom="2cm"
                               margin-left="1cm"
                               margin-right="1cm">

            <fo:region-body margin-top="0cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="first">

        <!-- usage of page layout -->
        <!-- HEADER -->

        <fo:static-content flow-name="xsl-region-before">
            <fo:block-container height="1cm" width="0cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="message"
                                        retrieve-boundary="page"
                                        retrieve-position="first-starting-within-page"/>
                </fo:block>
            </fo:block-container>
            <fo:block-container height="1cm" width="0cm" top="0cm" left="0cm" position="absolute">    <fo:block>
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
            <fo:block text-align = "center">
                <fo:external-graphic
                    content-height="scale-to-fit"
                    height="1.00in" content-width="1.00in"
                    scaling="non-uniform" src="url(file:///<%=logoAskridaPath%>)"  />
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-header>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4">
                                <fo:block font-size="16pt"  line-height="16pt" space-after.optimum="5pt"
                                          text-align="center"
                                          padding-top="10pt">
                                    {L-ENG Clauses-L}{L-INA Klausula-Klausula-L}
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4">
                                <fo:block font-size="12pt" text-align="center" space-after.optimum="10pt">
                                    {L-ENG Policy Number-L}{L-INA Nomor Polis -L} : <%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%>
                                </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-header>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block>{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(pol.getStCustomerName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block>{L-ENG Inception Date-L}{L-INA Awal Pertanggungan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodStart(), "d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block>{L-ENG Expiry Date-L}{L-INA Akhir Pertanggungan -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodEnd(), "d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <%
                                            final DTOList clausules = pol.getReportClausules();
                                            for (int i = 0; i < clausules.size(); i++) {
                                                InsurancePolicyClausulesView cl = (InsurancePolicyClausulesView) clausules.get(i);

                                                if (!cl.isSelected()) {
                                                    continue;
                                                }

                                                //out.println(cl.getStDescription()+"\n");

                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="start">&#x2022; </fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block text-align="center"><%=cl.getStDescription()%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3">
                                <% if (cl.getStWordingNew() != null) {%>
                                <fo:block font-size="<%=fontsize%>" wrap-option="yes-wrap" linefeed-treatment="preserve" text-align="justify"
                                          white-space-treatment="preserve" white-space-collapse="false">
                                    <%=cl.getStWordingNew().replaceAll("%S%", pol.getEntity().getStEntityName())%>
                                </fo:block>
                                <% } else {%>
                                <fo:block font-size="<%=fontsize%>" wrap-option="yes-wrap" linefeed-treatment="preserve" text-align="justify"
                                          white-space-treatment="preserve" white-space-collapse="false">
                                    <%=cl.getStWording().replaceAll("%S%", pol.getEntity().getStEntityName())%>
                                </fo:block>
                                <% }%>

                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <% }%>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <!-- GARIS  -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>

            <fo:block id="end-of-document"><fo:marker
                    marker-class-name="term">
                    <fo:instream-foreign-object>
                        <svg xmlns="http://www.w3.org/2000/svg" width="0cm" height="1cm" xml:space="preserve">
                            <rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>

            <!-- ALAMAT ASKRIDA -->
            <fo:block-container height="3.2cm" width="17cm" top="25.7cm" left="0cm" position="absolute">
                <fo:block text-align = "center">
                    <fo:external-graphic
                        scaling="non-uniform" src="url(file:///<%=alamatAskridaPath%>)"  />
                </fo:block>
            </fo:block-container>

        </fo:flow>
    </fo:page-sequence>
    <%
                }
    %>
    <%-- END DESIGN KLAUSULA--%>

    <%-- DESIGN NOTA--%>
    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <!-- komisi cuma 13.5cm -->

        <fo:simple-page-master master-name="first"
                               page-height="14cm"
                               page-width="21.5cm"
                               margin-top="2.5cm"
                               margin-bottom="0.5cm"
                               margin-left="1cm"
                               margin-right="1cm">

            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>

    <fo:page-sequence master-reference="first" initial-page-number="1">

        <fo:flow flow-name="xsl-region-body">


            <%
                        final DTOList det = pol.getDetails();

                        int ln = 0;

                        for (int i = 0; i < det.size(); i++) {
                            InsurancePolicyItemsView dt = (InsurancePolicyItemsView) det.get(i);

                            if (!dt.isComission()) {
                                com_amount = BDUtil.zero;
                            } else {
                                com_amount = dt.getDbAmount();
                            }

                            if (!dt.isComission()) {
                                cek = true;
                                continue;
                            }

                            fee = dt.getStDescription();
                            fee_base = dt.getInsuranceItem().getStDescription();

                            final EntityView agent = dt.getEntity();

                            ln++;

            %>

            <%if (ln > 1) {%>

            <fo:block break-after="page"></fo:block>

            <%}%>


            <fo:block text-align="start"
                      font-size="16pt"
                      font-family="TAHOMA"
                      line-height="12pt"
                      font-weight="bold">
                {L-ENG CREDIT NOTE-L}{L-INA NOTA KREDIT-L}
            </fo:block>

            <fo:block space-after.optimum="10pt"></fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="100mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG TO-L}{L-INA KEPADA-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(agent.getStEntityName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block font-size="8pt"><%=JSPUtil.xmlEscape(agent.getStAddress())%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block-container height="5cm" width="2cm" top="0cm" left="13cm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="1.5cm"/>
                    <fo:table-column column-width="0.5cm"/>
                    <fo:table-column column-width="5cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="<%=fontsize%>">

                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="start">{L-ENG Number-L}{L-INA Nomor-L}</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="start"><%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="start">{L-ENG Dates-L}{L-INA Tanggal-L}</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="start"><%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="start">{L-ENG Branch-L}{L-INA Cabang-L}</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="start"><%= JSPUtil.printX(pol.getStCostCenterDesc())%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block-container>

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
                      line-height="16pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>

            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>


            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Type of Insurance-L}{L-INA Jenis Asuransi-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStPolicyTypeDesc2()%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Policy Number-L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG The Insured-L}{L-INA Nama Tertanggung -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(pol.getStCustomerName())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%--
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Address-L}{L-INA Alamat -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(pol.getStCustomerAddress())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        --%>
                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Sum Insured-L}{L-INA Harga Pertanggungan -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Premium Amount-L}{L-INA Jumlah Premi -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(), 2)%></fo:block></fo:table-cell>
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
                            <fo:table-cell ><fo:block>{L-ENG Period-L}{L-INA Jangka Waktu-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodStart(), "d ^^ yyyy")%> {L-ENG   up to   -L}{L-INA   s/d   -L}<%=DateUtil.getDateStr(pol.getDtPeriodEnd(), "d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <% if (fee != null) {%>
                            <fo:table-cell ><fo:block><%=fee%></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell ><fo:block><%=fee_base%></fo:block></fo:table-cell>
                            <% }%>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(com_amount, 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%
                                                    final ARTaxView tx = dt.getTax();

                                                    //if (tx!=null) {
                                                    if (tx == null) {
                                                        tax = "";
                                                        tax_amount = BDUtil.zero;
                                                    } else if (tx != null) {
                                                        tax = tx.getStDescription();
                                                        tax_amount = dt.getDbTaxAmount();
                                                    }
                        %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block><%=tax%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(tax_amount, 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% if (pol.getDbNDPPN() != null && dt.isBrokerFee()) {%>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG PPN-L}{L-INA PPN-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDPPN(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% }%>

                        <fo:table-row>
                            <% if (dt.getInsuranceItem().isFeeBase2()) {%>
                            <fo:table-cell ><fo:block>{L-ENG Due To You-L}{L-INA Untuk Bank -L}</fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell ><fo:block>{L-ENG Due To You-L}{L-INA Untuk Saudara -L}</fo:block></fo:table-cell>
                            <% }%>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <% if (pol.getDbNDPPN() != null && dt.isBrokerFee()) {%>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(dt.getDbNetAmount(), pol.getDbNDPPN()), 2)%></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(dt.getDbNetAmount(), 2)%></fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <!-- ROW -->

            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>

            <fo:block font-size="<%=fontsize%>"
                      font-weight="bold">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="180mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <% if (pol.getDbNDPPN() != null && dt.isBrokerFee()) {%>
                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="left">{L-ENG SAY :-L}{L-INA TERBILANG :-L} <%=NumberSpell.readNumber(JSPUtil.printX(BDUtil.sub(dt.getDbNetAmount(), pol.getDbNDPPN()), 2), pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% } else {%>
                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="left">{L-ENG SAY :-L}{L-INA TERBILANG :-L} <%=NumberSpell.readNumber(JSPUtil.printX(dt.getDbNetAmount(), 2), pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <!-- GARIS DALAM KOLOM -->

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

            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>

            <fo:block font-size="6pt" space-after.optimum="3pt">
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
                    <fo:table-column />
                    <fo:table-body>

                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L}
                                    <%= JSPUtil.printX(pol.getStCostCenterDesc())%><%if (tanpaTanggal) {%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%></fo:block>
                                    <% } else {%>
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L}
                                    Jakarta<%if (tanpaTanggal) {%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%></fo:block>
                                    <% }%>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>

                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block-container height="3cm" width="3cm" top="7cm" left="0cm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="3cm"/>
                    <fo:table-body>

                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="1pt"><fo:block>
                                    <% if (usingDigitalSign) {%>
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
                                    <%}%>
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
                            <fo:table-cell >
                                <% if (usingDigitalSign) {%>
                                <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="0.75in" content-width="0.75in"
                                        scaling="non-uniform" src="url(<%=pol.getUser(pol.getUserIDSign()).getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <% } else {%>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit" height="0.75in"
                                        content-width="0.75in" scaling="non-uniform"
                                        src="url(<%=pol.getUserApproved().getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <% }%>
                                <% }%>
                            </fo:table-cell>
                        </fo:table-row>

                        <% if (usingDigitalSign) {%>
                        <fo:table-row>

                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <%--<fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=pol.getUserApproved().getStUserName().toUpperCase()%></fo:block></fo:table-cell>--%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%= pol.getUserIDSignName()%></fo:inline></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%=JSPUtil.printX(pol.getUserApproved().getStUserName()).toUpperCase()%></fo:inline></fo:block></fo:table-cell>
                            <% }%>

                        </fo:table-row>
                        <%}%>

                        <fo:table-row>

                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
                            <% }%>

                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <% }%>


            <%if (ln < 1) {%>

            <fo:block text-align="start"
                      font-size="16pt"
                      font-family="TAHOMA"
                      line-height="12pt"
                      font-weight="bold">
                {L-ENG CREDIT NOTE-L}{L-INA NOTA KREDIT-L}
            </fo:block>

            <fo:block space-after.optimum="10pt"></fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="100mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG TO-L}{L-INA KEPADA-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block-container height="5cm" width="2cm" top="0cm" left="13cm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="1.5cm"/>
                    <fo:table-column column-width="0.5cm"/>
                    <fo:table-column column-width="5cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="<%=fontsize%>">

                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="start">{L-ENG Number-L}{L-INA Nomor-L}</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="start"><%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="start">{L-ENG Dates-L}{L-INA Tanggal-L}</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="start"><%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="start">{L-ENG Branch-L}{L-INA Cabang-L}</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="start"><%= JSPUtil.printX(pol.getStCostCenterDesc())%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block-container>

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
                      line-height="16pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>

            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>


            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Type of Insurance-L}{L-INA Jenis Asuransi-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStPolicyTypeDesc2()%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Policy Number-L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG The Insured-L}{L-INA Nama Tertanggung -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(pol.getStCustomerName())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%--
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Address-L}{L-INA Alamat -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(pol.getStCustomerAddress())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        --%>
                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Sum Insured-L}{L-INA Harga Pertanggungan -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Premium Amount-L}{L-INA Jumlah Premi -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(), 2)%></fo:block></fo:table-cell>
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
                            <fo:table-cell ><fo:block>{L-ENG Period-L}{L-INA Jangka Waktu-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodStart(), "d ^^ yyyy")%> {L-ENG   up to   -L}{L-INA   s/d   -L}<%=DateUtil.getDateStr(pol.getDtPeriodEnd(), "d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
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
                            <fo:table-cell ><fo:block><%=fee_base%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end">-</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <!-- ROW -->

            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>

            <fo:block font-size="<%=fontsize%>"
                      font-weight="bold">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="180mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="left">{L-ENG SAY :-L}{L-INA TERBILANG :-L} -</fo:block></fo:table-cell>
                        </fo:table-row>

                        <!-- GARIS DALAM KOLOM -->

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

            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>

            <fo:block font-size="6pt" space-after.optimum="3pt">
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
                    <fo:table-column />
                    <fo:table-body>

                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L}
                                    <%= JSPUtil.printX(pol.getStCostCenterDesc())%><%if (tanpaTanggal) {%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%></fo:block>
                                    <% } else {%>
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L}
                                    Jakarta<%if (tanpaTanggal) {%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%></fo:block>
                                    <% }%>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>

                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block-container height="3cm" width="3cm" top="7cm" left="0cm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="3cm"/>
                    <fo:table-body>

                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="1pt"><fo:block>
                                    <% if (usingDigitalSign) {%>
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
                                    <%}%>
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
                            <fo:table-cell >
                                <% if (usingDigitalSign) {%>
                                <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="0.75in" content-width="0.75in"
                                        scaling="non-uniform" src="url(<%=pol.getUser(pol.getUserIDSign()).getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <% } else {%>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit" height="0.75in"
                                        content-width="0.75in" scaling="non-uniform"
                                        src="url(<%=pol.getUserApproved().getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <% }%>
                                <% }%>
                            </fo:table-cell>
                        </fo:table-row>

                        <% if (usingDigitalSign) {%>
                        <fo:table-row>

                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <%--<fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=pol.getUserApproved().getStUserName().toUpperCase()%></fo:block></fo:table-cell>--%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%= pol.getUserIDSignName()%></fo:inline></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%=JSPUtil.printX(pol.getUserApproved().getStUserName()).toUpperCase()%></fo:inline></fo:block></fo:table-cell>
                            <% }%>

                        </fo:table-row>
                        <%}%>

                        <fo:table-row>

                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
                            <% }%>

                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <%}%>

        </fo:flow>
    </fo:page-sequence>

    <fo:page-sequence master-reference="first">


        <fo:flow flow-name="xsl-region-body">
            <!-- ROW -->

            <fo:block text-align="start"
                      font-size="16pt"
                      font-family="TAHOMA"
                      line-height="12pt"
                      font-weight="bold">
                {L-ENG DEBET NOTE-L}{L-INA NOTA DEBET-L}
            </fo:block>

            <fo:block space-after.optimum="10pt"></fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="100mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG TO-L}{L-INA KEPADA-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(pol.getStCustomerName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block font-size="8pt"><%=JSPUtil.xmlEscape(pol.getStCustomerAddress())%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block-container height="5cm" width="2cm" top="0cm" left="13cm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="1.5cm"/>
                    <fo:table-column column-width="0.5cm"/>
                    <fo:table-column column-width="5cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="<%=fontsize%>">

                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="start">{L-ENG Number-L}{L-INA Nomor-L}</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="start"><%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="start">{L-ENG Dates-L}{L-INA Tanggal-L}</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="start"><%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="start">{L-ENG Branch-L}{L-INA Cabang-L}</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="1pt">
                                <fo:block text-align="start"><%= JSPUtil.printX(pol.getStCostCenterDesc())%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block-container>

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
                      line-height="16pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>

            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Type of Insurance-L}{L-INA Jenis Asuransi-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStPolicyTypeDesc2()%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Policy Number-L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Period-L}{L-INA Jangka Waktu-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodStart(), "d ^^ yyyy")%> {L-ENG up to-L}{L-INA s/d-L} <%=DateUtil.getDateStr(pol.getDtPeriodEnd(), "d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Sum Insured-L}{L-INA Harga Pertanggungan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>

            <!-- ROW -->

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="37mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Details-L}{L-INA Detil-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Gross Premium-L}{L-INA Premi Bruto-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                                    final DTOList details = pol.getDetails();

                                    for (int i = 0; i < details.size(); i++) {
                                        InsurancePolicyItemsView dtu = (InsurancePolicyItemsView) details.get(i);

                                        if (!dtu.isDiscount()) {
                                            continue;
                                        }

                                        String rate = "";

                                        if (dtu.isEntryByRate()) {
                                            rate = JSPUtil.printX(dtu.getDbRate(), 2) + "%";
                                        }
                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Discount-L}{L-INA Diskon-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=rate%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(dtu.getDbAmount(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>
                        <!-- GARIS DALAM KOLOM -->

                        <%
                                    for (int i = 0; i < details.size(); i++) {
                                        InsurancePolicyItemsView dti = (InsurancePolicyItemsView) details.get(i);

                                        if (!dti.isFee() || dti.isPPN()) {
                                            continue;
                                        }
                                        /*
                                        String rate = "";

                                        rate = JSPUtil.printX(det.getDbRate(),2) + "%";
                                         */
                        %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(dti.getStDescription2())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(dti.getDbAmount(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" ><fo:block border-width="0.35pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Due To Us-L}{L-INA Total Tagihan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(pol.getDbTotalDue(), pol.getDbNDPPN()), 2)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <!-- GARIS DALAM KOLOM -->



                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" space-after.optimum="3pt" line-height="0.15pt"></fo:block>

            <fo:block font-size="<%=fontsize%>" font-weight="bold">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="180mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="left">{L-ENG SAY :-L}{L-INA TERBILANG :-L} <%=NumberSpell.readNumber(JSPUtil.printX(BDUtil.sub(pol.getDbTotalDue(), pol.getDbNDPPN()), 2), pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <!-- CICILAN -->

            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>" font-family="TAHOMA"
                      line-height="10pt" space-after.optimum="2pt"
                      text-align="justify" > </fo:block>

            <% //if(pol.getStInstallmentPeriods()>=2)
                        {

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

                                                        for (int i = 0; i < installment.size(); i++) {
                                                            InsurancePolicyInstallmentView ins = (InsurancePolicyInstallmentView) installment.get(i);

                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>Installment <%=(i + 1)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(ins.getDtDueDate())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(ins.getDbAmount(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>
                    </fo:table-body>
                </fo:table>
            </fo:block>
            <% }%>
            <% }%>
            <!-- ROW -->

            <% if (usingDigitalSign) {%>
            <fo:block font-size="6pt"
                      space-after.optimum="3pt"
                      text-align="justify" >
                Dokumen ini dicetak secara sistem dan dinyatakan sah.
            </fo:block>
            <% }%>

            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="3pt"></fo:block>

            <fo:block font-size="6pt" space-after.optimum="5pt">
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
                    <fo:table-column />
                    <fo:table-body>

                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L}
                                    <%= JSPUtil.printX(pol.getStCostCenterDesc())%><%if (tanpaTanggal) {%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%></fo:block>
                                    <% } else {%>
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L}
                                    Jakarta<%if (tanpaTanggal) {%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%></fo:block>
                                    <% }%>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>

                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block-container height="3cm" width="3cm" top="7cm" left="0cm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="3cm"/>
                    <fo:table-body>

                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="1pt"><fo:block>
                                    <% if (usingDigitalSign) {%>
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
                                    <%}%>
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
                            <fo:table-cell >
                                <% if (usingDigitalSign) {%>
                                <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="0.75in" content-width="0.75in"
                                        scaling="non-uniform" src="url(<%=pol.getUser(pol.getUserIDSign()).getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <% } else {%>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit" height="0.75in"
                                        content-width="0.75in" scaling="non-uniform"
                                        src="url(<%=pol.getUserApproved().getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <% }%>
                                <% }%>
                            </fo:table-cell>
                        </fo:table-row>

                        <% if (usingDigitalSign) {%>
                        <fo:table-row>

                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <%--<fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=pol.getUserApproved().getStUserName().toUpperCase()%></fo:block></fo:table-cell>--%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%= pol.getUserIDSignName()%></fo:inline></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%=JSPUtil.printX(pol.getUserApproved().getStUserName()).toUpperCase()%></fo:inline></fo:block></fo:table-cell>
                            <% }%>

                        </fo:table-row>
                        <%}%>

                        <fo:table-row>

                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
                            <% }%>

                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

        </fo:flow>
    </fo:page-sequence>

    <%--- END DESIGN NOTA--%>
</fo:root>