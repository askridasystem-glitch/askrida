<%@ page import="com.webfin.ar.forms.ReceiptForm,
         com.crux.web.controller.SessionManager,
         java.util.ArrayList,
         com.crux.util.fop.FOPUtil,
         com.webfin.ar.model.*,
         com.crux.util.*,
         java.math.BigDecimal,
         com.crux.lang.LanguageManager,
         com.webfin.insurance.model.*,
         com.crux.util.*,
         com.crux.common.parameter.Parameter,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <%

                ReceiptForm form = (ReceiptForm) request.getAttribute("FORM");

                String askridaLogoPath = Parameter.readString("DIGITAL_POLIS_LOGO_PIC");
                String fontsize = "12pt";

                boolean isINST = false;
                if (form.getEntity().getStEntityClass().equalsIgnoreCase("INST")) {
                    isINST = true;
                }
    %>

    <!-- defines the layout master -->
    <fo:layout-master-set>
        <fo:simple-page-master master-name="first"
                               page-height="30cm"
                               page-width="21cm"
                               margin-left="2cm"
                               margin-right="2cm">

            <!-- WATERMARK BACKGROUND -->
            <fo:region-body margin-top="3cm" margin-bottom="3cm"/>
            <fo:region-before extent="3cm" />
            <fo:region-after extent="3.5cm" />
        </fo:simple-page-master>
    </fo:layout-master-set>

    <fo:page-sequence master-reference="first">

        <fo:static-content flow-name="xsl-region-before">

            <!-- LOGO ASKRIDA -->
            <fo:block text-align = "center">
                <fo:external-graphic
                    content-height="scale-to-fit"
                    height="1.00in" content-width="1.00in"
                    scaling="non-uniform" src="url(file:///<%=askridaLogoPath%>)"  />
            </fo:block>
        </fo:static-content>

        <!-- usage of page layout -->
        <!-- HEADER -->

        <fo:flow flow-name="xsl-region-body">

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="70mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="start">Nomor : <%= JSPUtil.printX(form.getClaimno())%></fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block text-align="right"><%=form.getCostCenter().getStDescription()%>, <%=DateUtil.getDateStr(new Date(), "d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" line-height="<%=fontsize%>" space-after.optimum="20pt" text-align="justify"> </fo:block>

            <!-- GARIS  -->
            <%--  <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="1pt"></fo:block> --%>

            <!-- defines text title level 1-->

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="100mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG To -L}{L-INA Kepada Yth. -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <%if (isINST) {%>
                        <fo:table-row>
                            <fo:table-cell><fo:block><fo:inline font-weight="bold">Pimpinan</fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <fo:table-row>
                            <fo:table-cell><fo:block><fo:inline font-weight="bold"><%= JSPUtil.printX(form.getStEntityName())%></fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block><%= JSPUtil.printX(form.getEntity().getStAddress())%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <!-- Normal text -->

            <!-- DYNAMIC HEADER WORDING -->

            <fo:block font-size="<%=fontsize%>" line-height="<%=fontsize%>" space-after.optimum="20pt" text-align="center"><fo:inline font-weight="bold" text-decoration="underline">Hal : Surat Tagihan Polis</fo:inline></fo:block>


            <!-- GARIS  -->
            <%--  <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="1pt"></fo:block> --%>

            <!-- Normal text -->

            <fo:block font-size="<%=fontsize%>" line-height="12pt" space-after.optimum="20pt">
                Dengan hormat,
            </fo:block>

            <fo:block font-size="<%=fontsize%>" line-height="12pt" space-after.optimum="20pt" text-align="justify">
                Pertama-tama kami mengucapkan terima kasih atas kepercayaan yang telah diberikan kepada PT. Asuransi Bangun Askrida atas penutupan Asuransi milik <%=form.getStEntityName()%>.
            </fo:block>

            <fo:block font-size="<%=fontsize%>" line-height="12pt" space-after.optimum="20pt" text-align="justify">
                Bersama ini terlampir kami sampaikan polis Asuransi sebagai berikut : (Terlampir)
            </fo:block>

            <fo:block font-size="<%=fontsize%>" line-height="12pt" space-after.optimum="20pt" text-align="justify">
                Jumlah tagihan tersebut kami mohon dapat dibayarkan melalui rekening PT Asuransi Bangun Askrida Cabang <%=form.getCostCenter().getStDescription()%>
                Nomor. <fo:inline font-weight="bold"><%=form.getEntity().getStRcNo()%></fo:inline> pada <%= JSPUtil.printX(form.getStEntityName())%>.
                Apabila telah melakukan pembayaran, mohon dapat diinformasikan melalui email <fo:inline font-weight="bold">buktibayar@askrida.co.id</fo:inline>.
            </fo:block>

            <fo:block font-size="<%=fontsize%>" line-height="12pt" space-after.optimum="20pt" text-align="justify">
                Demikian kami sampaikan, atas perhatian dan kerjasamanya diucapkan terima kasih.
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="70mm"/>
                    <fo:table-column column-width="70mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" >
                                <fo:block text-align="center">Hormat kami,</fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block text-align="center">Cabang <%=form.getCostCenter().getStDescription()%>,</fo:block>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" ><fo:block space-after.optimum="60pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("BRANCH_SIGN_" + form.getStBranch())%></fo:inline></fo:block>
                                <fo:block text-align="center">Pimpinan</fo:block>
                            </fo:table-cell>
                            <fo:table-cell >
                                <%if (form.getStName() != null) {%>
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= form.getStName()%></fo:inline></fo:block>
                                <fo:block text-align="center">Kasie. Keuangan</fo:block>
                                <% }%>
                            </fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>   

        </fo:flow>
    </fo:page-sequence>

    <!-- starts actual layout -->
    <fo:page-sequence master-reference="first">

        <fo:flow flow-name="xsl-region-body">
            <%

                        DTOList line = (DTOList) request.getAttribute("RPT");

                        ArrayList colW = new ArrayList();

                        colW.add(new Integer(5));
                        colW.add(new Integer(40));
                        colW.add(new Integer(20));
                        colW.add(new Integer(25));
                        colW.add(new Integer(15));
                        colW.add(new Integer(20));

                        String bw = "0.2pt";
            %>

            <fo:block font-family="Helvetica" font-weight="bold" text-align="start" font-size="12pt">PT. ASURANSI BANGUN ASKRIDA</fo:block>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="start" font-size="12pt">CABANG <%=form.getCostCenter().getStDescription().toUpperCase()%></fo:block>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="start" font-size="12pt">LAMPIRAN SURAT TAGIHAN POLIS</fo:block>
            <fo:block font-family="Helvetica" font-weight="bold" text-align="start" font-size="12pt">Nomor: <%=form.getClaimno()%></fo:block>
            <fo:block space-after.optimum="14pt"/>

            <fo:block font-family="Helvetica" display-align="center" border-width="0.1pt" font-size="10pt">
                <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>" >
                    <fo:table-header>

                        <fo:table-row>   
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">No</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">No. Polis</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Tanggal Polis</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Jenis Polis</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Premi</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center">Keterangan</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-header>

                    <%=FOPUtil.printColumnWidth(colW, 17, 2, "cm")%>
                    <fo:table-body>    

                        <%
                                    BigDecimal premiTotal = new BigDecimal(0);
                                    for (int j = 0; j < line.size(); j++) {
                                        ARReceiptLinesView view = (ARReceiptLinesView) line.get(j);

                                        premiTotal = BDUtil.add(premiTotal, view.getPolicy().getDbPremiTotal());
                        %>
                        <fo:table-row>
                            <fo:table-cell padding="2pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center"><%=String.valueOf(j + 1)%>.</fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center"><%=JSPUtil.print(view.getInvoice().getStAttrPolicyNo().substring(0, 4) + "-" + view.getInvoice().getStAttrPolicyNo().substring(4, 8) + "-" + view.getInvoice().getStAttrPolicyNo().substring(8, 12) + "-" + view.getInvoice().getStAttrPolicyNo().substring(12, 16) + "-" + view.getInvoice().getStAttrPolicyNo().substring(16, 18))%></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center"><%=DateUtil.getDateStr(view.getPolicy().getDtPolicyDate(), "dd ^ yyyy")%></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center"><%=JSPUtil.print(view.getPolicy().getPolicyType().getStDescription())%></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt" border-left-style="solid"><fo:block line-height="6mm" text-align="end"><%=JSPUtil.print(view.getPolicy().getDbPremiTotal(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center"></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                                    }
                        %> 
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="2pt" space-after.optimum="2pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 

                        <fo:table-row  >
                            <fo:table-cell border-bottom-style="solid" padding="2pt" number-columns-spanned="4"><fo:block text-align="center" font-weight="bold">JUMLAH</fo:block></fo:table-cell>
                            <fo:table-cell border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.print(premiTotal, 0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"></fo:block></fo:table-cell>
                        </fo:table-row  >

                    </fo:table-body>      
                </fo:table>
            </fo:block>

        </fo:flow>
    </fo:page-sequence>
</fo:root>

