<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         com.crux.util.fop.FOPUtil,
         org.joda.time.Days,
         org.joda.time.DateTime,
         com.crux.common.parameter.Parameter,
         java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%

            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");
            final String attached = (String) request.getAttribute("attached");
            boolean withKurs = attached.equalsIgnoreCase("4") ? false : true;

            final String preview = (String) request.getAttribute("preview");
            boolean isPreview = false;
            if (preview != null) {
                if (preview.equalsIgnoreCase("Preview")) {
                    isPreview = true;
                }
            }

            boolean effective = pol.isEffective();

            DateTime startDate = new DateTime(pol.getDtPeriodStart());
            DateTime endDate = new DateTime(pol.getDtPeriodEnd());
            Days d = Days.daysBetween(startDate, endDate);
            int day = d.getDays();

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

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-height="30cm"
                               page-width="21cm"
                               margin-left="2cm"
                               margin-right="2cm">
            <fo:region-body margin-top="0cm" margin-bottom="0cm" background-image="file:///<%=watermarkPath%>"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>

    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only">

        <fo:flow flow-name="xsl-region-body">

            <!-- LOGO ASKRIDA -->
            <fo:block text-align = "center">
                <fo:external-graphic
                    content-height="scale-to-fit"
                    height="1.00in" content-width="1.00in"
                    scaling="non-uniform" src="url(file:///<%=logoAskridaPath%>)"  />
            </fo:block>

            <% if (isPreview) {%>
            <fo:block font-size="20pt"
                      line-height="16pt" space-after.optimum="0pt"
                      color="blue"
                      text-align="center"
                      padding-top="10pt">
                PREVIEW MODE
            </fo:block>
            <% }%>        

            <!-- defines text title level 1-->

            <% if (!effective) {%>
            <fo:block font-size="16pt" font-family="TAHOMA"
                      line-height="16pt" space-after.optimum="0pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>

            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-after.optimum="5pt"></fo:block>

            <fo:block font-size="14pt" font-family="TAHOMA" line-height="0pt" space-after.optimum="10pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                SERTIFIKAT PENJAMINAN BANK GARANSI
            </fo:block>

            <fo:block font-size="14pt" font-family="TAHOMA" line-height="0pt" space-after.optimum="5pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                <%=JSPUtil.printX(pol.getStPolicyTypeDesc2().substring(46, 65))%>
            </fo:block>

            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-before.optimum="5pt" space-after.optimum="10pt"></fo:block>

            <!-- defines text title level 1-->

            <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" space-after.optimum="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="100mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Nomor : <%= pol.getStPolicyNo().substring(0, 2)%> <%= pol.getStReference1()%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>Nilai   <%= pol.getStCurrencyCode()%> <%= JSPUtil.printX(pol.getDbInsuredAmount(), 0)%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <%
                                DTOList objects = pol.getObjects();

                                for (int i = 0; i < objects.size(); i++) {
                                    InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

            %>

            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="10pt"><fo:inline font-weight="bold">PT. ASURANSI BANGUN ASKRIDA</fo:inline> berkedudukan di <%=Parameter.readString("ASKRIDA_ADDRESS")%>, yang selanjutnya disebut <fo:inline font-weight="bold">PENJAMIN</fo:inline></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="10pt">Bahwa atas permintaan dari <%= JSPUtil.printX(obj.getStReference1())%> berkedudukan di <%= JSPUtil.printX(obj.getStReference3())%> yang selanjutnya disebut <fo:inline font-weight="bold">TERJAMIN</fo:inline> guna memberikan jaminan kepada <fo:inline font-weight="bold">PENERIMA JAMINAN</fo:inline> :</fo:block>


            <fo:block font-size="<%=fontsize%>" text-align = "justify" space-after.optimum="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >Nama </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >: <%=JSPUtil.xmlEscape(pol.getStCustomerName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >Alamat </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >: <%=JSPUtil.xmlEscape(pol.getStCustomerAddress())%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="10pt">Sertifikat Penjaminan Bank Garansi <%=JSPUtil.printX(pol.getStPolicyTypeDesc2().substring(46, 65))%> ini diterbitkan oleh PENJAMIN sehubungan dengan akan diterbitkannya BANK GARANSI oleh PENERIMA JAMINAN untuk kepentingan TERJAMIN guna keperluan
                <%= JSPUtil.xmlEscape(obj.getStReference6())%> berdasarkan Nomor <% if (obj.getStReference14() != null) {%><%=JSPUtil.printX(obj.getStReference14())%> tanggal <%=DateUtil.getDateStr(obj.getDtReference6(), "d ^^ yyyy")%> <% } else {%> <%=JSPUtil.printX(obj.getStReference7())%> tanggal <%=DateUtil.getDateStr(obj.getDtReference2(), "d ^^ yyyy")%> <% }%> dengan harga kontrak sebesar Rp. <%=JSPUtil.printX(obj.getDbReference3(), 0)%> (<%=NumberSpell.readNumber(JSPUtil.printX(obj.getDbReference3(), 0), "", NumberSpell.INDONESIA)%> RUPIAH)
                yang berlaku selama <%=String.valueOf(day + 1)%> hari kalender terhitung sejak <%=DateUtil.getDateStr(pol.getDtPeriodStart(), "d ^^ yyyy")%> sampai dengan <%=DateUtil.getDateStr(pol.getDtPeriodEnd(), "d ^^ yyyy")%> yang ditujukan kepada <%=JSPUtil.printX(obj.getStReference5())%></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="10pt">Bahwa apabila selama berlakunya Sertifikat Penjamin ini TERJAMIN telah lalai atau terjadi wanprestasi sebagaimana yang ditentukan di dalam BANK GARANSI dimaksud, sehingga terjadi pencairan BANK GARANSI, maka PENERIMA JAMINAN wajib terlebih dahulu memberitahukan kepada PENJAMIN secara tertulis dengan 
                disertai asli SERTIFIKAT PENJAMINAN BANK GARANSI dan bukti-bukti pencairan BANK GARANSI tersebut
                diatas dengan batas waktu pengajuan klaim selambat-lambatnya 45 (empat puluh lima) hari kalender sejak tanggal berakhirnya SERTIFIKAT PENJAMINAN BANK GARANSI. </fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="10pt">Pembayaran sejumlah uang tersebut diatas dilaksanakan selambat-lambatnya 14 (empat belas) hari kalender sejak tanggal diterimanya Surat Klaim Penjaminan Bank Garansi dari Pihak Penerima Jaminan.</fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify">Bahwa Sertifikat Penjamin ini dengan sendirinya tidak berlaku lagi apabila :</fo:block>

            <fo:block font-size="<%=fontsize%>" text-align = "justify" space-after.optimum="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >a. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >TERJAMIN telah memenuhi kewajibannya sebagaimana yang disebutkan dalam BANK GARANSI yang bersangkutan, walaupun jangka waktu berlakunya Sertifikat Penjaminan ini belum berakhir.</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >b. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >Jangka waktu untuk pengajuan klaim telah berakhir dan atau tidak adanya klaim dari PENERIMA JAMINAN.</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block >c. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >Adanya pernyataan dari PENERIMA JAMINAN dan TERJAMIN yang menyatakan teklah selesainya hal yang dijamin dalam BANK GARANSI tersebut yang dituangkan dalam Surat Pernyataan Bermaterai serta ditandatangani oleh kedua belah pihak.</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="10pt">Menunjuk Pasal 1832 KUH Perdata dengan ini ditegaskan kembali bahwa PENJAMIN melepaskan hak-hak istimewanya untuk menuntut supaya harta benda pihak yang dijamin lebih dahulu disita dan dijual guna melunasi hutangnya sebagaimana dimaksud dalam Pasal 1831 KUH Perdata.</fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="10pt">Sertifikat Penjaminan Bank Garansi ini merupakan bagian tidak terpisahkan dari Perjanjian Penjaminan Bank Garansi antara <fo:inline font-weight="bold"> PT. Asuransi Bangun Askrida </fo:inline> dengan 
                <fo:inline font-weight="bold"> 
                    <%= Parameter.readString("CERTIFICATE_" + pol.getStCostCenterCode())%>
                </fo:inline> 
                dan tidak dapat dipindah tangankan atau dijadikan jaminan kepada pihak lain.</fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify" space-after.optimum="10pt">Ditandatangani serta dibubuhi materai di <%=pol.getStCostCenterDesc()%> pada tanggal <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%>.</fo:block>


            <fo:block font-size="<%=fontsize%>" text-align = "justify">

                <fo:table table-layout="fixed">
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="4mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Jasa Jaminan</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "end"><%=JSPUtil.printX(pol.getDbPremiTotal(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Biaya Materai</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "end"><%=JSPUtil.printX(pol.getDbTotalItemAmount("SFEE"), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Administrasi</fo:block></fo:table-cell>                   
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "end"><%=JSPUtil.printX(pol.getDbTotalItemAmount("PCOST"), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="1pt"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>                        

                        <fo:table-row>
                            <fo:table-cell><fo:block>Jumlah</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "end"><%=JSPUtil.printX(pol.getDbTotalDue(), 0)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="1pt"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <%--
                        <% if (effective) { %>
                        <% if (pol.getUserApproved().getStSign()!=null) { %>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align = "center">  	
                                    <fo:external-graphic content-width="scale-to-fit"
                                                         content-height="70%"
                                                         width="70%"
                                                         scaling="non-uniform" src="url(<%=pol.getUserApproved().getFile().getStFilePath()%>)" />
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>                           
                        <% } else { %>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block space-after.optimum="40pt"></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block space-after.optimum="40pt"></fo:block></fo:table-cell>                            
                        </fo:table-row>  
                        <% } %>
                        <% } %>
                        --%>

                        <% if (usingDigitalSign) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" ><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getUserApproved().getStSign() != null) {%>
                            <fo:table-cell>
                                <fo:block text-align = "center">
                                    <%--<fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(D:\jboss-3.2.5\server\default\deploy\fin.ear\fin.war\pages\main\img\stepanus.jpg)"/>

                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(D:\fin-repository\00\20130221\1361441261089)"/>
                                    --%>
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(<%=pol.getUserApproved().getFile().getStFilePath()%>)"  />
                                </fo:block>
                            </fo:table-cell>
                            <% } else {%>
                            <fo:table-cell>
                                <fo:block text-align = "center" space-after.optimum="65pt"></fo:block>
                            </fo:table-cell>
                            <% }%>
                        </fo:table-row>  
                        <% } else {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" ><fo:block space-after.optimum="65pt"></fo:block></fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align = "center" space-after.optimum="65pt">
                                    <%--<fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(D:\jboss-3.2.5\server\default\deploy\fin.ear\fin.war\pages\main\img\stepanus.jpg)"/>

                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(D:\fin-repository\00\20130221\1361439311926.jpg)"/>--%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row> 
                        <% }%>  

                    </fo:table-body>
                </fo:table>

                <fo:table table-layout="fixed">
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="4mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=pol.getUserApproved().getStUserName().toUpperCase()%></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">TEPANUS HUTABARAT</fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" ></fo:table-cell>
                            <fo:table-cell >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0.2" line-height="0.15pt" space-after.optimum="0.5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center" ><%if (pol.getParaf() != null) {%><fo:leader leader-length="10mm"/><%}%><%=JSPUtil.printX(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%>                                     <%if (pol.getParaf() != null) {%>                                     <fo:external-graphic                                         content-height="scale-to-fit" height="0.3in"  content-width="0.3in" scaling="non-uniform"                                         overflow="hidden" src="url(<%=pol.getUser(pol.getParaf()).getParafFile().getStFilePath()%>)"  />                                     <%}%>                             </fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">KABAG. BONDING</fo:block></fo:table-cell>
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

            <fo:block font-size="6pt" space-after.optimum="85pt">
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "dd/MM/yyyy HH:mm:ss"))%>

                <% if (effective) {%>
                PrintStamp:<%=pol.getStPrintCode()%> PrintCode:<%=pol.getStPrintStamp()%> Page:<fo:page-number/> Cetakan : <%=pol.getStPrintCounter()%>
                <% }%>
                <% if (usingDigitalSign) {%>
                <%if (!isUsingBarcode) {%>  [<%=JSPUtil.print(pol.getStSignCode())%>] <%}%>
                <% }%>
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

</fo:root>