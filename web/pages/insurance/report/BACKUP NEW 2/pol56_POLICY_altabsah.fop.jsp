<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         java.util.Date,
         java.math.BigDecimal,
         com.crux.common.parameter.Parameter,
         com.crux.util.fop.FOPUtil"%><?xml version="1.0" encoding="utf-8"?>
<%

            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");
            final String attached = (String) request.getAttribute("attached");
            boolean tanpaTanggal = attached.equalsIgnoreCase("3") ? false : true;
            boolean tanpaNama = attached.equalsIgnoreCase("5") ? false : true;
            boolean tanpaNamaTanggal = attached.equalsIgnoreCase("6") ? false : true;

            final String preview = (String) request.getAttribute("preview");
            boolean isPreview = false;
            if (preview != null) {
                if (preview.equalsIgnoreCase("Preview")) {
                    isPreview = true;
                }
            }

            boolean effective = pol.isEffective();

            BigDecimal periodLength = new BigDecimal(pol.getStPeriodLength());

            final String digitalsign = (String) request.getAttribute("digitalsign");
            boolean usingDigitalSign = false;
            if (digitalsign != null) {
                if (digitalsign.equalsIgnoreCase("Y")) {
                    usingDigitalSign = true;
                }
            }

            boolean isUsingBarcode = Parameter.readString("REPORT_SIGN_TYPE").equalsIgnoreCase("BARCODE");

//if (true) throw new NullPointerException();

%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-height="30cm"
                               page-width="21cm"
                               margin-top="5cm"
                               margin-bottom="0.5cm"
                               margin-left="2.5cm"
                               margin-right="2.5cm">
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

            <fo:block space-before.optimum="10pt" space-after.optimum="5pt" font-size="<%=fontsize%>" font-weight="bold" text-align="center" text-decoration="underline">SURAT PERNYATAAN TENTANG KEASLIAN</fo:block>
            <fo:block space-after.optimum="5pt" font-size="<%=fontsize%>" font-weight="bold" text-align="center" text-decoration="underline">PERSETUJUAN PRINSIP JAMINAN KONTRA BANK GARANSI</fo:block>
            <fo:block space-after.optimum="10pt" font-size="<%=fontsize%>" font-weight="bold" text-align="center" text-decoration="underline"><%=JSPUtil.printX(pol.getStPolicyTypeDesc2().toUpperCase())%></fo:block>

            <!-- Normal text -->
            <%
                        DTOList objects = pol.getObjects();

                        for (int i = 0; i < objects.size(); i++) {
                            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

            %>

            <fo:block font-size="<%=fontsize%>" text-align="justify" line-space="1pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Yang Bertanda-tangan dibawah ini</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Nama</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%= pol.getUserIDSignName()%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Jabatan</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Perusahaan</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getStProducerName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3">
                                <fo:block space-before.optimum="10pt" space-after.optimum="10pt">
                                    Dengan ini menyatakan bahwa Persetujuan Prinsip Jaminan Kontra Bank Garansi (<%=JSPUtil.printX(pol.getStPolicyTypeDesc2())%>) :
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Nama Principal</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell>
                                <fo:block><%=JSPUtil.printX(obj.getStReference1())%></fo:block>
                                <fo:block><%=JSPUtil.xmlEscape(obj.getStReference3())%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>No. Persetujuan Prinsip</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><fo:inline font-weight="bold"><%=JSPUtil.printX(pol.getStReference1())%></fo:inline> Tanggal : <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "dd ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Nilai Jaminan</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><fo:inline font-weight="bold"><%=JSPUtil.printX(pol.getStCurrencyCode())%> <%=JSPUtil.printX(obj.getDbObjectInsuredAmount(), 2)%></fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Dikeluarkan Oleh</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(pol.getStProducerName())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>No. SPK</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell>
                                <fo:block><%=JSPUtil.printX(obj.getStReference7())%></fo:block>
                                <fo:block>Tanggal : <%=DateUtil.getDateStr(obj.getDtReference2(), "dd ^^ yyyy")%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>No. SPPBJ</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell>
                                <fo:block><%=JSPUtil.printX(obj.getStReference14())%></fo:block>
                                <fo:block>Tanggal : <%=DateUtil.getDateStr(obj.getDtReference6(), "dd ^^ yyyy")%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Pemilik Proyek</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference5())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Nama Proyek</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(obj.getStReference6())%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Masa Berlaku</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodStart(), "dd ^^ yyyy")%> s/d <%=DateUtil.getDateStr(pol.getDtPeriodEnd(), "dd ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <%}%>

            <fo:block font-size="<%=fontsize%>" space-before.optimum="10pt" space-after.optimum="5pt" text-align="justify">Adalah <fo:inline font-weight="bold" text-decoration="underline">BENAR-BENAR ASLI</fo:inline></fo:block>
            <fo:block font-size="<%=fontsize%>" space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify">Saya bersedia menanggung segala resiko apabila kemudian diketahui bahwa Persetujuan Prinsip Jaminan Kontra Bank Garansi (<%=JSPUtil.printX(pol.getStPolicyTypeDesc2())%>) tersebut diatas adalah <fo:inline font-weight="bold" text-decoration="underline">PALSU</fo:inline>.</fo:block>
            <fo:block font-size="<%=fontsize%>" space-before.optimum="5pt" space-after.optimum="10pt" text-align="justify">Demikian Surat Pernyataan ini saya buat dengan sesungguhnya.</fo:block>

            <%-- mulai rincian --%> 

            <fo:block font-size="<%=fontsize%>" line-height="0pt" space-after.optimum="20pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="104mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><%= JSPUtil.printX(pol.getStCostCenterDesc())%>, <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "dd ^^ yyyy")%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" text-align = "justify">                
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="4mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-body>

                        <% if (usingDigitalSign) {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" ><fo:block>
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
                            <fo:table-cell></fo:table-cell>
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
                        </fo:table-row>                          
                        <% } else {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block space-after.optimum="60pt"></fo:block></fo:table-cell>
                        </fo:table-row> 
                        <% }%>  

                        <% if (tanpaNama) {%>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <%--<fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=pol.getUserApproved().getStUserName().toUpperCase()%></fo:block></fo:table-cell>--%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%= pol.getUserIDSignName()%></fo:inline></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%=JSPUtil.printX(pol.getUserApproved().getStUserName()).toUpperCase()%></fo:inline></fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center" ><%if (pol.getParaf() != null) {%><fo:leader leader-length="10mm"/><%}%><%=JSPUtil.printX(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%>                                     <%if (pol.getParaf() != null) {%>                                     <fo:external-graphic                                         content-height="scale-to-fit" height="0.3in"  content-width="0.3in" scaling="non-uniform"                                         overflow="hidden" src="url(<%=pol.getUser(pol.getParaf()).getParafFile().getStFilePath()%>)"  />                                     <%}%>                             </fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>
                        <% }%>

                        <% if (!tanpaNama) {%>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
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

            <fo:block font-size="6pt"
                      font-family="sans-serif"
                      line-height="10pt"
                      space-after.optimum="10pt"
                      text-align="left" >
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "dd/MM/yyyy HH:mm:ss"))%>

                <% if (effective) {%>
                PrintStamp:<%=pol.getStPrintCode()%> PrintCode:<%=pol.getStPrintStamp()%> Page:<fo:page-number/> Terlampir : [<%=JSPUtil.print(request.getAttribute("DOCUMENT_NAME"))%>] <%if (usingDigitalSign) {%>Sign Code : <%=pol.getStSignCode()%> <%}%>
                <% }%>   
            </fo:block>

        </fo:flow>
    </fo:page-sequence>
</fo:root>


