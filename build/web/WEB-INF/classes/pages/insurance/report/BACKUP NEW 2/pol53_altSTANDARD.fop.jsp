<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         java.util.Date,
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

            boolean isUsingBarcode = Parameter.readString("REPORT_SIGN_TYPE").equalsIgnoreCase("BARCODE");

%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- defines page layout -->
    <fo:layout-master-set>

        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-height="30cm"
                               page-width="21cm"
                               margin-top="1cm"
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
            <fo:block font-size="16pt" font-family="TAHOMA"
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

            <fo:block font-size="16pt" font-family="TAHOMA" line-height="0pt" space-after.optimum="10pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                <%=pol.getStPolicyTypeDesc2()%>
            </fo:block>

            <!-- Normal text -->

            <!-- defines text title level 1-->

            <fo:block font-size="12pt" font-family="TAHOMA" line-height="0pt" space-after.optimum="10pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                (Advance Payment Bond)
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
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG Bond No. : -L}{L-INA No. Bond : -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align = "end">{L-ENG Amount : -L}{L-INA Nilai : -L}<%=JSPUtil.print(pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%> </fo:block></fo:table-cell>
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
                            <fo:table-cell><fo:block>{L-ENG KNOW ALL MEN BY THESE PRESENTS, that we -L}{L-INA Dengan ini dinyatakan bahwa kami : -L} <fo:inline font-weight="bold"><%=JSPUtil.xmlEscape(obj.getStReference1())%></fo:inline> {L-ENG address -L}{L-INA yang beralamat di -L} <%=JSPUtil.xmlEscape(obj.getStReference3())%>  {L-ENG as Contractor, hereinafter called the PRINCIPAL, and -L}{L-INA sebagai Kontraktor, selanjutnya disebut PRINCIPAL, dan -L} <fo:inline font-weight="bold"><%=JSPUtil.xmlEscape(obj.getStReference6Desc())%></fo:inline> {L-ENG address -L}{L-INA yang beralamat di -L}<%=JSPUtil.xmlEscape(obj.getStReference7())%>{L-ENG as Surety, hereinafter called the SURETY, -L}{L-INA sebagai PENJAMIN, selanjutnya disebut sebagai SURETY, bertanggung jawab dan dengan tegas terikat pada -L} <fo:inline font-weight="bold"><%=JSPUtil.xmlEscape(pol.getStCustomerName())%></fo:inline> {L-ENG address -L}{L-INA yang beralamat di -L} <%=JSPUtil.xmlEscape(pol.getStCustomerAddress())%>{L-ENG are held and firmly bound unto the Owner, hereinafter called the OBLIGEE in the amount of -L}{L-INA sebagai Pemilik, selanjutnya disebut sebagai OBLIGEE atas uang sejumlah -L}<%=JSPUtil.print(pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%> {L-ENG Say ( -L}{L-INA Terbilang ( -L}<%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbInsuredAmount(), 2), pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getCurrency().getStCcyDescription())%> ) {L-ENG is to be paid to the said OBLIGEE. -L}{L-INA yang harus dibayar kepada OBLIGEE. -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 2. -L}{L-INA 2. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG We the PRINCIPAL and the SURETY bind ourselves for the payment of which sum, well and truly to be made. -L}{L-INA Maka kami PRINCIPAL dan SURETY dengan ini mengikatkan diri untuk melakukan pembayaran jumlah tersebut diatas dengan baik dan benar. -L} </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 3. -L}{L-INA 3. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG WHEREAS, the PRINCIPAL has by written agreement Contract  -L}{L-INA Bahwa PRINCIPAL dengan suatu perjanjian tertulis No. -L} <%=JSPUtil.printX(obj.getStReference10())%> {L-ENG dated -L}{L-INA tanggal -L} <%=DateUtil.getDateStr(obj.getDtReference1(), "d ^^ yyyy")%> {L-ENG entered into a Contract with the OBLIGEE for the works of -L}{L-INA telah mengadakan Kontrak dengan OBLIGEE untuk pekerjaan -L}<%=JSPUtil.printX(obj.getStReference11())%> {L-ENG at and agreed Contract Price of -L}{L-INA dengan Harga Kontrak yang telah disetujui sebesar -L}<%=JSPUtil.print(pol.getStCurrencyCode())%> <%=JSPUtil.printX(obj.getDbReference2(), 2)%> {L-ENG Say : -L}{L-INA (Terbilang : -L}<%=NumberSpell.readNumber(JSPUtil.printX(obj.getDbReference2(), 2), pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getCurrency().getStCcyDescription())%>) {L-ENG which contract is inherent within this bond. -L}{L-INA dan kontrak tersebut merupakan bagian yang tidak terpisahkan dari Jaminan ini. -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 4. -L}{L-INA 4. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG WHEREAS the OBLIGEE has agreed the advance payment to the PRINCIPAL that sum amounting to -L}{L-INA Bahwa untuk Kontrak tersebut diatas, OBLIGEE setuju membayar kepada PRINCIPAL uang sebesar -L} <%=JSPUtil.print(pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getDbInsuredAmount(), 2)%> {L-ENG Say : -L}{L-INA (Terbilang : -L}<%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbInsuredAmount(), 2), pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getCurrency().getStCcyDescription())%> ) {L-ENG as an Advance Payment before the performance of the work under the said contract. As a guarantee of such Advance Payment, the SURETY agree to furnish an Advance Payment Bond with the condition as follows. -L}{L-INA sebagai pembayaran uang muka sebelum Pekerjaan menurut Kontrak diatas dimulai. Sebagai jaminan terhadap pembayaran Uang Muka itu maka SURETY memberikan jaminan dengan ketentuan tersebut dibawah ini -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-INA SURETY akan membayar jumlah yang sesungguhnya diderita olehnya maksimum sebesar nilai jaminan tersebut diatas, selambat-lambatnya 30 (tiga puluh) hari kalender setelah menerima tuntutan penagihan dari pihak OBLIGEE berdasar Keputusan OBLIGEE mengenai pengenaan sanksi akibat tindakan cidera janji oleh pihak PRINCIPAL -L}</fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 5. -L}{L-INA 5. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG If the PRINCIPAL repays in full amount of the Advance Payment or the remaining Advance Payment to the OBLIGEE  due under said contract, then this bond shall be null and void; otherwise to remain in full force and effect as from -L}{L-INA Jika PRINCIPAL telah melakukan pembayaran kembali kepada OBLIGEE seluruh jumlah Uang Muka dimaksud (yang dinyatakan dalam surat tanda bukti penerimaan olehnya) atau sisa Uang Muka yang wajib dibayar menurut Kontrak tersebut, maka surat Jaminan ini menjadi batal dan tidak berlaku ; sebaliknya jika tidak maka Surat Jaminan ini tetap berlaku dan efektif mulai dari tanggal -L}<%=DateUtil.getDateStr(pol.getDtPeriodStart(), "d ^^ yyyy")%> {L-ENG up to -L}{L-INA sampai dengan tanggal-L} <%=DateUtil.getDateStr(pol.getDtPeriodEnd(), "d ^^ yyyy")%> {L-ENG ( as for the duration of the contract or until the date the Advance Payment is fully repaid ). -L}{L-INA (selama berlakunya kontrak atau sampai pada tanggal uang muka telah dibayar kembali seluruhnya).-L} </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 6. -L}{L-INA 6. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG Any claim on this bond shall be made in written application by the OBLIGEE to the SURETY promply after the default arises caused by the PRINCIPAL fails to repay the Advance Payment or the remaining Advance Payment under the condition of said contract. -L}{L-INA Tuntutan ganti rugi atas surat Jaminan ini dilaksanakan oleh OBLIGEE secara tertulis kepada SURETY segera setelah timbul cidera janji (Wanprestasi/default) oleh pihak PRINCIPAL, karena tidak dapat membayar kembali Uang Muka atas sisa Uang Muka tersebut sesuai dengan syarat Kontrak. -L} </fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 7. -L}{L-INA 7. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG The SURETY shall pay to the OBLIGEE the unpaid Advance Payment by the PRINCIPAL after having received a written claim from the OBLIGEE. -L}{L-INA SURETY akan membayar kepada OBLIGEE Uang Muka atau sisa Uang Muka yang berdasarkan kontrak belum dikembalikan oleh PRINCIPAL setelah menerima tuntutan penagihan (Klaim) dari OBLIGEE. -L} </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 8. -L}{L-INA 8. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG With reference to article 1832 of the Indonesia Civil Law, we herewith reaffirm that the SURETY shall relinquish the special right of claim on assets  belonging to the PRINCIPAL and for seizure and sale of such assets for the discharge of his debts as required in article 1831 of the Indonesia Civil Law. -L}{L-INA Menunjuk pada Pasal 1832 KUH Perdata dengan ini ditegaskan kembali bahwa SURETY melepaskan hak-hak istimewanya untuk menuntut supaya harta benda pihak yang dijamin lebih dahulu disita dan dijual guna melunasi hutangnya sebagaimana dimaksud dalam Pasal 1831 KUH Perdata. -L} </fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 9. -L}{L-INA 9. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG Any proceeding against the SURETY to recover any claim hereunder must be received by the SURETY not later than 1 (one) month after the bond expires. -L}{L-INA Setiap pengajuan ganti rugi terhadap SURETY berdasarkan Jaminan ini harus sudah diajukan selambat-lambatnya dalam waktu 1 (satu) bulan sesudah berakhirnya masa laku jaminan ini. -L} </fo:block></fo:table-cell>
                        </fo:table-row>

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
                                    Jakarta<%if (!tanpaTanggal) {%>, <%=dtTanggal%><%}%>.</fo:block>
                                    <% }%>
                            </fo:table-cell>
                        </fo:table-row>

                    </fo:table-body>
                </fo:table>
            </fo:block>

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
                    <fo:table-column column-width="50mm"/>
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
                            <fo:table-cell>
                                <fo:block font-size="<%=fontsize%>" text-align = "center">
                                    PT. ASURANSI BANGUN ASKRIDA
                                </fo:block>
                                <% if (usingDigitalSign) {%>
                                <fo:block font-size="<%=fontsize%>" text-align = "center">
                                    <%if (pol.getParaf() != null) {%><fo:external-graphic content-height="scale-to-fit" height="0.3in"  content-width="0.3in" scaling="non-uniform" overflow="hidden" src="url(<%=pol.getUser(pol.getParaf()).getParafFile().getStFilePath()%>)"  /><%}%>CABANG <%=JSPUtil.printX(pol.getCostCenter3().getStDescription().toUpperCase())%>
                                </fo:block>
                                <%}%>
                            </fo:table-cell>
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

            <% if (!tanpaRate) {%>
            <fo:block font-size="<%=fontsize%>" text-align = "justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="4mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="3mm"/>
                    <fo:table-column column-width="50mm"/>
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
                            <fo:table-cell>
                                <fo:block font-size="<%=fontsize%>" text-align = "center">
                                    PT. ASURANSI BANGUN ASKRIDA
                                </fo:block>
                                <% if (usingDigitalSign) {%>
                                <fo:block font-size="<%=fontsize%>" text-align = "center">
                                    <%if (pol.getParaf() != null) {%><fo:external-graphic content-height="scale-to-fit" height="0.3in"  content-width="0.3in" scaling="non-uniform" overflow="hidden" src="url(<%=pol.getUser(pol.getParaf()).getParafFile().getStFilePath()%>)"  /><%}%>CABANG <%=JSPUtil.printX(pol.getCostCenter3().getStDescription().toUpperCase())%>
                                </fo:block>
                                <%}%>
                            </fo:table-cell>
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
                    <fo:table-column column-width="50mm"/>
                    <fo:table-body>

                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.xmlEscape(obj.getStReference4())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%= pol.getUserIDSignName()%></fo:inline></fo:block></fo:table-cell>
                            <% } else {%>
                            <% if (!isPreview) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%=JSPUtil.xmlEscape(pol.getUserApproved().getStUserName()).toUpperCase()%></fo:inline></fo:block></fo:table-cell>
                            <% }%>
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
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.xmlEscape(obj.getStReference5())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <% if (!isPreview) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center" ><%=JSPUtil.xmlEscape(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%></fo:block></fo:table-cell>
                            <% }%>
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

            <fo:block-container height="2cm" width="18cm" top="26cm" left="0cm" padding="1mm" position="absolute">
                <fo:block text-align="center" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    PT. ASURANSI BANGUN ASKRIDA <%=pol.getCostCenter3().getTypeDescription()%> <%=pol.getCostCenter3().getStDescription().toUpperCase()%>
                </fo:block>
            </fo:block-container>

            <fo:block-container height="2cm" width="18cm" top="26.3cm" left="0cm" padding="1mm" position="absolute">
                <fo:block text-align="center" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    Telp. <%=pol.getCostCenter3().getStPhoneCode()%>, Fax. <%=pol.getCostCenter3().getStFaxCode()%>
                </fo:block>
            </fo:block-container>

            <% }%>
        </fo:flow>
    </fo:page-sequence>
</fo:root>