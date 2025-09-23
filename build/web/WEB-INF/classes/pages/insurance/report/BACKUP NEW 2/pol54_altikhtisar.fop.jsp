<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         java.util.Date,
         com.crux.util.Tools,
         com.crux.util.fop.FOPUtil,
         com.webfin.entity.model.EntityView,
         com.webfin.ar.model.ARTaxView,
         java.math.BigDecimal"%><?xml version="1.0" encoding="utf-8"?>
<%

            final InsurancePolicyView pol = (InsurancePolicyView) request.getAttribute("POLICY");
            final String fontsize = (String) request.getAttribute("FONTSIZE");
            final String attached = (String) request.getAttribute("attached");
            String param[] = attached.split("[\\|]");

            boolean isAttached = param[0].equalsIgnoreCase("2") ? true : false;
            boolean tanpaTanggal = param[0].equalsIgnoreCase("3") ? true : false;
            boolean tanpaRate = param[0].equalsIgnoreCase("7") ? true : false;
            //boolean klausulaTerlampir = param[0].equalsIgnoreCase("8") ? true : false;

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

            final String digitalsign = (String) request.getAttribute("digitalsign");
            boolean usingDigitalSign = false;
            if (digitalsign != null) {
                if (digitalsign.equalsIgnoreCase("Y")) {
                    usingDigitalSign = true;
                }
            }

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->

        <fo:simple-page-master master-name="first"
                               page-height="30cm"
                               page-width="21cm"
                               margin-top="5cm"
                               margin-left="1.5cm"
                               margin-right="1.5cm">

            <fo:region-body margin-top="0cm" margin-bottom="0cm"/> 
            <fo:region-before extent="0cm"/> 
            <fo:region-after extent="0cm"/> 
        </fo:simple-page-master>

    </fo:layout-master-set>

    <fo:page-sequence master-reference="first" initial-page-number="1">

        <fo:flow flow-name="xsl-region-body">            

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
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-after.optimum="1pt"></fo:block>

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
                (Indemnity System)
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
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG Bond No. : -L}{L-INA No. Bond : -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align = "end">{L-ENG Value : -L}{L-INA Nilai : -L}<%=JSPUtil.print(pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.print(pol.getStCurrencyCode())%><%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%> </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
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
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Dengan ini dinyatakan bahwa kami : -L} <fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference1())%></fo:inline> {L-ENG 1. -L}{L-INA yang beralamat di -L} <%=JSPUtil.printX(obj.getStReference3())%>  {L-ENG 1. -L}{L-INA sebagai Kontraktor, selanjutnya disebut PRINCIPAL, dan -L} <fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference6Desc())%></fo:inline> {L-ENG 1. -L}{L-INA yang beralamat di -L}<%=JSPUtil.printX(obj.getStReference7())%>{L-ENG 1. -L}{L-INA sebagai PENJAMIN, selanjutnya disebut sebagai SURETY, bertanggung jawab dan dengan tegas terikat pada -L} <fo:inline font-weight="bold"><%=JSPUtil.xmlEscape(pol.getStCustomerName())%></fo:inline> {L-ENG 1. -L}{L-INA yang beralamat di -L} <%=pol.getStCustomerAddress()%>{L-ENG 1. -L}{L-INA sebagai Pemilik, selanjutnya disebut sebagai OBLIGEE atas uang sejumlah -L}<%=JSPUtil.print(pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%> {L-ENG 1. -L}{L-INA Terbilang ( -L}<%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbInsuredAmount(), 2))%> <%=JSPUtil.printX(pol.getCurrency().getStCcyDescription())%> )</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 2. -L}{L-INA 2. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Maka kami PRINCIPAL dan SURETY dengan ini mengikatkan diri untuk melakukan pembayaran jumlah tersebut diatas dengan baik dan benar bilamana PRINCIPAL tidak memenuhi kewajiban sebagaimana ditetapkan dalam Instruksi kepada Peserta Lelang untuk Pekerjaan -L} <fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference11())%></fo:inline> {L-ENG 1. -L}{L-INA yang diselenggarakan oleh OBLIGEE pada tanggal -L} <%=DateUtil.getDateStr(pol.getDtPeriodStart(), "d ^^ yyyy")%> {L-ENG 1. -L}{L-INA (tanggal Pelelangan) -L} di  <%=JSPUtil.printX(obj.getStReference12())%> berdasarkan No. Surat Undangan <%=JSPUtil.printX(obj.getStReference10())%> pada tanggal <%=DateUtil.getDateStr(obj.getDtReference1(), "d ^^ yyyy")%>. </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 3. -L}{L-INA 3. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Adapun ketentuan dari Surat Jaminan ini adalah jika : -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" text-align = "justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="170mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG a. -L}{L-INA a. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA PRINCIPAL menarik kembali Penawarannya sebelum berakhirnya masa laku Penawaran yang dinyatakan dalam Formulir Penawaran -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG b. -L}{L-INA b. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Penawaran PRINCIPAL disetujui oleh OBLIGEE dalam masa laku Penawaran yang dinyatakan telah : -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" text-align = "justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="165mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG (i). -L}{L-INA (i) -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA menyerahkan jaminan Pelaksanaan yang diperlukan, -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG (ii). -L}{L-INA (ii) -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA menandatangani Kontrak, -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG (iii). -L}{L-INA (iii) -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA menandatangani dokumen perikatan lainnya sebagaimana yang diharuskan dalam Dokumen Lelang atau -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" text-align = "justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="170mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG c. -L}{L-INA c. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA PRINCIPAL gagal melaksanakan ketentuan seperti tersebut pada butir b diatas, dan telah membayar kepada OBLIGEE selisih (tidak melebihi nilai jaminan) antara perbedaan penawarannya dari yang lebih besar berikutnya, dimana OBLIGEE menunjuk kontraktor yang berikut itu untuk melaksanakan pekerjaan yang ditawarkannya, -L} {L-ENG XXX -L}{L-INA maka jaminan ini menjadi batal dan tidak berlaku; sebaliknya jika tidak terjadi hal-hal tersebut pada butir a, b, dan c diatas maka jaminan ini tetap berlaku dan efektif mulai dari tanggal -L} <fo:inline font-weight="bold"> <%=DateUtil.getDateStr(pol.getDtPeriodStart(), "d ^^ yyyy")%></fo:inline> {L-ENG XXX -L}{L-INA sampai dengan tanggal -L} <fo:inline font-weight="bold"><%=DateUtil.getDateStr(pol.getDtPeriodEnd(), "d ^^ yyyy")%></fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" text-align = "justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="175mm"/>
                    <fo:table-body>

                        <%--  
            <fo:table-row>
             <fo:table-cell><fo:block> </fo:block></fo:table-cell>
             <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA maka jaminan ini menjadi batal dan tidak berlaku; sebaliknya jika tidak terjadi hal-hal tersebut pada butir a, b, dan c diatas maka jaminan ini tetap berlaku dan efektif mulai dari tanggal -L}<%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d ^^ yyyy")%>{L-ENG XXX -L}{L-INA sampai dengan tanggal -L}<%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"d ^^ yyyy")%></fo:block></fo:table-cell>
           </fo:table-row>
			--%>
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 4. -L}{L-INA 4. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Tuntutan penagihan (klaim) atas surat Jaminan ini dilaksanakan oleh OBLIGEE secara tertulis kepada SURETY segera setelah timbul cidera janji (Wanprestasi/default) oleh pihak PRINCIPAL sesuai dengan ketentuan-ketentuan dalam Dokumen Lelang -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA SURETY akan membayar jumlah yang sesungguhnya diderita olehnya maksimum sebesar nilai jaminan tersebut diatas, selambat-lambatnya 30 (tiga puluh) hari kalender setelah menerima tuntutan penagihan dari pihak OBLIGEE berdasar Keputusan OBLIGEE mengenai pengenaan sanksi akibat tindakan cidera janji oleh pihak PRINCIPAL -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 5. -L}{L-INA 5. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Menunjuk pada pasal 1832 KUH Perdata dengan ini ditegaskan kembali bahwa SURETY melepaskan hak-hak istimewanya untuk menuntut supaya harta benda pihak yang dijamin lebih dahulu disita dan dijual guna melunasi hutangnya sebagaimana dimaksud dalam Pasal 1831 KUH Perdata. -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 6. -L}{L-INA 6. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Setiap pengajuan ganti rugi terhadap SURETY berdasarkan jaminan ini harus sudah diajukan selambat-lambatnya dalam waktu 3 (tiga) bulan sesudah berakhirnya masa laku Jaminan ini. -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell>
                                <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                                <fo:block >{L-ENG XXX -L}{L-INA Ditanda tangani serta dibubuhkan materai di -L} 
                                    <%= JSPUtil.printX(pol.getStCostCenterDesc())%><%if (tanpaTanggal) {%>, {L-ENG XXX -L}{L-INA pada tanggal -L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%>.</fo:block>
                                    <% } else {%>
                                <fo:block >{L-ENG XXX -L}{L-INA Ditanda tangani serta dibubuhkan materai di -L}
                                    Jakarta<%if (tanpaTanggal) {%>, {L-ENG XXX -L}{L-INA pada tanggal -L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(), "d ^^ yyyy")%><%}%>.</fo:block>
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
                        <% if (!tanpaRate) {%>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Service Charge</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "end"><%=JSPUtil.printX(pol.getDbPremiTotal(), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Biaya Materai</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "end"><%=JSPUtil.printX(pol.getDbTotalItemAmount("SFEE"), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>Administrasi</fo:block></fo:table-cell>                   
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "end"><%=JSPUtil.printX(pol.getDbTotalItemAmount("PCOST"), 2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">{L-ENG XXX -L}{L-INA PRINCIPAL -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">{L-ENG XXX -L}{L-INA SURETY -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="1pt"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>   

                        <fo:table-row>
                            <fo:table-cell><fo:block>Jumlah</fo:block></fo:table-cell>
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
                        <%--<% if (pol.getStProducerID().equalsIgnoreCase("983185") || pol.getStProducerID().equalsIgnoreCase("987345")) {%>--%>
                        <% if (tanpaRate) {%>

                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference1())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <%if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference6Desc())%></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                            <% }%>
                        </fo:table-row>

                        <% }%>

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
                            <fo:table-cell number-columns-spanned="5" ><fo:block></fo:block></fo:table-cell>
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
                                <fo:block text-align = "center" space-after.optimum="45pt"></fo:block>
                            </fo:table-cell>
                            <% }%>
                        </fo:table-row>  
                        <% } else {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" ><fo:block space-after.optimum="65pt"></fo:block></fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align = "center" space-after.optimum="45pt">
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
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference4())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference8())%></fo:block></fo:table-cell>
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
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference9())%></fo:block></fo:table-cell>
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

            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>


            <fo:block font-size="6pt"
                      font-family="sans-serif"
                      line-height="10pt"
                      text-align="left" >
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "dd/MM/yyyy HH:mm:ss"))%>

                <% if (effective) {%>
                PrintStamp:<%=pol.getStPrintCode()%> PrintCode:<%=pol.getStPrintStamp()%> Page:<fo:page-number/> Terlampir : [<%=JSPUtil.print(request.getAttribute("DOCUMENT_NAME"))%>] <%if (usingDigitalSign) {%>Sign Code : <%=pol.getStSignCode()%> <%}%>
                <% }%>   
            </fo:block>

        </fo:flow>
    </fo:page-sequence>
    <!-- end: defines page layout -->


    <!-- actual layout -->
    <fo:page-sequence master-reference="first">

        <fo:flow flow-name="xsl-region-body">

            <fo:block font-size="16pt"
                      line-height="16pt" space-after.optimum="20pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                {L-INA IKHTISAR -L}{L-ENG SCHEDULE -L}
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column />
                    <fo:table-body>

                        <%
                                    //final DTOList objects = pol.getObjects();
                                    for (int i = 0; i < objects.size(); i++) {
                                        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>1. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Type of Bond</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStPolicyTypeDesc())%></fo:block></fo:table-cell>             
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell ><fo:block>2. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Principal</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getStReference1().toUpperCase())%></fo:block></fo:table-cell>             
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell ><fo:block>3. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Obligee</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerName().toUpperCase())%></fo:block></fo:table-cell>             
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell ><fo:block>4. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Certificate (Bond) No</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell ><fo:block>5. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Contract No</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>             
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell ><fo:block>6. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Contract Value</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>             
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell ><fo:block>7. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Bond of Value</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell ><fo:block>8. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Type of Work</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>-</fo:block></fo:table-cell>             
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell ><fo:block>9. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Work</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(obj.getStReference11())%></fo:block></fo:table-cell>             
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell ><fo:block>10. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Rate</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>0.50%</fo:block></fo:table-cell>             
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell ><fo:block>11. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Period</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodStart(), "dd ^^ yyyy")%> {L-ENG up to-L}{L-INA s/d-L} <%=DateUtil.getDateStr(pol.getDtPeriodEnd(), "dd ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell ><fo:block>12. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Condition</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Sesuai Kebijakan PT. Asuransi Ekspor Indonesia</fo:block></fo:table-cell>             
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <fo:table-row>
                            <fo:table-cell ><fo:block>13. </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>Remarks</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>             
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row> 

                        <% }%>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Gross Premium-L}{L-INA Premi Bruto-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                                    final DTOList details = pol.getDetails();

                                    for (int i = 0; i < details.size(); i++) {
                                        InsurancePolicyItemsView det = (InsurancePolicyItemsView) details.get(i);

                                        if (!det.isDiscount()) {
                                            continue;
                                        }

                                        String rate = "";

                                        if (det.isEntryByRate()) {
                                            rate = JSPUtil.printX(det.getDbRate(), 2) + "%";
                                        }
                        %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Discount-L}{L-INA Diskon-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=rate%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(det.getDbAmount(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>
                        <!-- GARIS DALAM KOLOM -->

                        <%
                                    for (int i = 0; i < details.size(); i++) {
                                        InsurancePolicyItemsView det = (InsurancePolicyItemsView) details.get(i);

                                        if (!det.isFee() || det.isPPN()) {
                                            continue;
                                        }
                        %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(det.getStDescription2())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(det.getDbAmount(), 2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }%>

                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" ></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" ><fo:block border-width="0.35pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block> 
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Due To Us-L}{L-INA Total Tagihan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(pol.getDbTotalDue(), pol.getDbNDPPN()), 2)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row> 

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <fo:block font-size="<%=fontsize%>" text-align = "justify" space-after.optimum="10pt">Iktisar ini merupakan bagian yang tidak terpisah dari Sertifikat (Bond).</fo:block>

            <%
                        for (int j = 0; j < objects.size(); j++) {
                            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(j);
            %>

            <fo:block font-size="<%=fontsize%>">
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
                            <fo:table-cell number-columns-spanned="5" ><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getUserApproved().getStSign() != null) {%>
                            <fo:table-cell>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(<%=pol.getUserApproved().getFile().getStFilePath()%>)"  />
                                </fo:block>
                            </fo:table-cell>
                            <% } else {%>
                            <fo:table-cell>
                                <fo:block text-align = "center" space-after.optimum="45pt"></fo:block>
                            </fo:table-cell>
                            <% }%>
                        </fo:table-row>  
                        <% } else {%>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="5" ><fo:block space-after.optimum="65pt"></fo:block></fo:table-cell>
                            <fo:table-cell>
                                <fo:block text-align = "center" space-after.optimum="45pt">
                                </fo:block>
                            </fo:table-cell>
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
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference4())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference8())%></fo:block></fo:table-cell>
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
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference9())%></fo:block></fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table> 
            </fo:block>

            <% }%>

        </fo:flow>
    </fo:page-sequence>

</fo:root>