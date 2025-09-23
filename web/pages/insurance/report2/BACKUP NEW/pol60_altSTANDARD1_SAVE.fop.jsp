<%@ page import="com.webfin.insurance.model.*,
         com.crux.ff.model.FlexFieldHeaderView,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.util.*,
         com.crux.common.parameter.Parameter,
         com.crux.util.fop.FOPUtil,
         java.math.BigDecimal,
         com.webfin.entity.model.EntityView,
         com.webfin.ar.model.ARTaxView,
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


        <fo:flow flow-name="xsl-region-body">

            <!-- LOGO ASKRIDA -->
            <fo:block text-align = "center">
                <fo:external-graphic
                    content-height="scale-to-fit"
                    height="1.00in" content-width="1.00in"
                    scaling="non-uniform" src="url(file:///<%=logoAskridaPath%>)"  />
            </fo:block>

            <% if (!effective) {%>
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" text-align="justify" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>

            <fo:block font-size="16pt"
                      text-align="center">
                {L-ENG Policy No.-L}{L-INA No. Polis -L} <%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%>
            </fo:block>

            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" text-align="justify" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>
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

            <fo:block font-size="12pt"
                      line-height="12pt" space-after.optimum="10pt"
                      color="black"
                      text-align="center"
                      padding-top="0pt">
                POLIS ASURANSI KREDIT KONSTRUKSI DAN PENGADAAN BARANG/JASA
            </fo:block>

            <fo:block font-size="12pt"
                      line-height="12pt" space-after.optimum="10pt"
                      color="black"
                      text-align="center"
                      padding-top="0pt">
                NO: <%= pol.getStPolicyNo().substring(0, 4) + "-" + pol.getStPolicyNo().substring(4, 8) + "-" + pol.getStPolicyNo().substring(8, 12) + "-" + pol.getStPolicyNo().substring(12, 16) + "-" + pol.getStPolicyNo().substring(16, 18)%>
            </fo:block>
            <!-- GARIS -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" text-align="justify"></fo:block>

            <fo:block font-size="<%=fontsize%>" text-align="justify">
                <fo:table table-layout="fixed">

                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="8mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="10mm"/>

                    <fo:table-column />
                    <fo:table-body>

                        <%

                                            final DTOList objects = pol.getObjects();

                                            for (int i = 0; i < objects.size(); i++) {
                                                InsurancePolicyObjDefaultView inspolobjct = (InsurancePolicyObjDefaultView) objects.get(i);

                        %>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block  space-before.optimum="5pt" space-after.optimum="5pt">
                                    SYARAT-SYARAT UMUM POLIS ASURANSI KREDIT KONSTRUKSI dan PENGADAAN BARANG/JASA
                                </fo:block>
                                <fo:block space-before.optimum="5pt">
                                    Yang bertanda tangan di bawah ini selanjutnya disebut PENANGGUNG, akan membayar Ganti Rugi kepada TERTANGGUNG sebagaimana disebutkan dalam Ikhtisar Pertanggungan atas dasar Permohonan Asuransi secara tertulis yang dilengkapi dengan keterangan tertulis lainnya yang diberikan oleh TERTANGGUNG merupakan bagian yang tidak terpisahkan dari Polis ini dan dengan syarat TERTANGGUNG telah membayar Premi secara benar sebagaimana disebutkan dalam Ikhtisar Pertanggungan dan tunduk pada ketentuan dan persyaratan sebagai berikut :
                                </fo:block>

                                <fo:block space-before.optimum="5pt" text-align="center">
                                    Pasal 1
                                </fo:block>
                                <fo:block text-align="center" space-after.optimum="5pt">
                                    OBYEK ASURANSI KREDIT
                                </fo:block>
                            </fo:table-cell>

                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">1.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    Kredit Konstruksi dan Pengadaan Barang/Jasa yang ditanggung oleh PENANGGUNG adalah Kredit Modal Kerja Konstruksi dan Pengadaan Barang/Jasa dalam rangka pembangunan proyek dan/atau Pengadaan Barang/Jasa yang dibiayai berdasarkan Anggaran Daerah (dana APBD) dan dana BUMN yang disalurkan melalui TERTANGGUNG
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">2.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    Kredit Konstruksi dan Pengadaan Barang/Jasa yang diberikan TERTANGGUNG kepada DEBITUR adalah maksimal sebesar total nilai proyek sebagaimana yang tersebut dalam Surat Perintah Mulai Kerja (SPMK) / Surat Perintah Kerja (SPK) / Kontrak yang dimiliki oleh DEBITUR.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">3.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    DEBITUR sebagai penerima Kredit Konstruksi/Pengadaan Barang/Jasa harus memenuhi syarat sebagai berikut :
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">a.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Memiliki izin-izin usaha di bidang jasa konstruksi dan/atau Pengadaan Barang dan Jasa, klasifikasi, dan kualifikasi perusahaan;
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">b.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                       	Memiliki SPMK/SPK/Kontrak asli atas suatu proyek yang akan dibangunnya dan/atau pengadaan barang/jasa berdasarkan hasil penunjukan langsung atau melalui lelang yang telah dilaksanakan oleh Pengguna Jasa Konstruksi dan Pengadaan Barang/Jasa;
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">c.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Menyerahkan copy SPK dan/atau kontrak yang telah dilengkapi dengan mata anggaran atau yang telah tertuang dalam dokumen anggaran seperti DIPA (Daftar Isian Pelaksanaan Anggaran) atau yang lainnya dan telah diverifikasi oleh TERTANGGUNG kepada Pengguna Jasa Konstruksi dan Pengadaan Barang/Jasa;
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">d.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Dalam hal proyek tidak diperoleh langsung oleh DEBITUR, maka DEBITUR wajib menyerahkan Surat Perjanjian Kerjasama atau Surat Kuasa Direksi perusahaan pemenang tender secara notariil atau di bawah tangan;
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">e.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Memiliki Standing Instruction (SI) atau dokumen pendukung lain yang dipersamakan untuk mendukung kepastian pembayaran tagihan proyek disertai dengan surat konfirmasi dari Obligee bahwa SI dan dokumen pendukung lainnya adalah benar dan sah serta rekening yang ditunjuk pada TERTANGGUNG tidak dapat diubah tanpa persetujuan PENANGGUNG.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">f.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Memiliki Standing Instruction (SI) atau dokumen pendukung lain yang dipersamakan untuk mendukung kepastian pembayaran tagihan proyek disertai dengan surat konfirmasi dari Obligee bahwa SI dan dokumen pendukung lainnya adalah benar dan sah serta rekening yang ditunjuk pada TERTANGGUNG tidak dapat diubah tanpa persetujuan PENANGGUNG.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block space-before.optimum="5pt" text-align="center">
                                    Pasal 2
                                </fo:block>
                                <fo:block text-align="center" space-after.optimum="5pt">
                                    KETENTUAN UMUM
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block space-before.optimum="5pt" >
                                    Dalam Polis ini yang dimaksud dengan :
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">1.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    PENANGGUNG adalah Perusahaan Asuransi yang memberikan jasa dalam penutupan risiko atas kerugian yang timbul akibat tidak diterimanya pelunasan Kredit Modal Kerja Konstruksi dan Pengadaan Barang/Jasa dari DEBITUR kepada TERTANGGUNG;
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">2.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    TERTANGGUNG adalah PT. Bank NTT yang menyalurkan Kredit Modal Kerja Konstruksi dan Pengadaan Barang/Jasa kepada DEBITUR dan berhak menerima ganti rugi (klaim asuransi) dari PENANGGUNG apabila DEBITUR :
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">a.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Tidak dapat memenuhi kewajiban perikatannya pada saat kredit jatuh tempo dan telah terjadi tunggakan pokok dan/atau bunga dan kredit tidak dapat diperpanjang.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">b.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Tidak dapat memenuhi kewajiban perikatannya sebelum kredit jatuh tempo dan dinyatakan macet sesuai dengan ketentuan Bank Indonesia.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>



                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">3.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    DEBITUR adalah adalah Badan Hukum yang bergerak dibidang Usaha Konstruksi dan Pengadaan Barang /Jasa yang melaksanakan pekerjaan dari pengguna Jasa Konstruksi dan Pengadaan Barang/Jasa sebagaimana dimaksud dalam yang dituangkan dalam SPMK/SPK/ Kontrak baik untuk atas namanya sendiri maupun atas Surat Kuasa pemilik SPMK/SPK/ Kontrak.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">4.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    Kredit Konstruksi dan Pengadaan Barang/Jasa adalah Kredit Modal Kerja yang diberikan oleh TERTANGGUNG kepada DEBITUR dalam rangka pembangunan proyek dan/atau Pengadaan Barang/Jasa yang dibiayai berdasarkan  dana APBD, BUMN, BUMD.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">5.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    Pekerjaan Konstruksi adalah keseluruhan atau sebagian dari rangkaian kegiatan perencanaan dan/atau pelaksanaan beserta pengawasan yang mencakup pekerjaan arsitektural, sipil, mekanikal, elektrikal, dan tata lingkungan masing-masing beserta kelengkapannya untuk mewujudkan suatu bangunan atau bentuk fisik lain.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">6.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    Pengadaan Barang/Jasa adalah sebagaimana dimaksud dalam Perpres No.54 tahun 2010 dan No. 70 tahun 2012 tentang Tata Cara Pengadaan Barang/Jasa Pemerintah termasuk perubahannya.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>




                        <!--                         break-->

                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>


                    </fo:table-body>
                </fo:table>
            </fo:block>


            <!--                       halaman 1-->



            <fo:block font-size="<%=fontsize%>" text-align="justify">
                <fo:table table-layout="fixed">

                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="8mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="10mm"/>

                    <fo:table-column />
                    <fo:table-body>



                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">7.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    Pengguna Jasa Konstruksi dan Pengadaan Barang/Jasa (Bowheer/Obligee) adalah Badan/Instansi Pemerintah sebagai pemberi tugas atau pemilik pekerjaan/proyek yang memerlukan layanan jasa konstruksi dan Pengadaan Barang/Jasa dari DEBITUR.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">8.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    Nilai Proyek adalah nilai kontrak dikurangi pajak (PPN).
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">9.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    Surat Perintah Mulai Kerja (SPMK)/Surat Perintah Kerja (SPK)/Kontrak adalah Polis yang mengatur hubungan hukum antara pengguna jasa konstruksi dan pengadaan barang/jasa dengan DEBITUR dalam penyelenggaraan pekerjaan konstruksi dan pengadaan barang/jasa yang telah didukung dengan dokumen anggaran seperti DIPA/DIPDA (Daftar Isian Pelaksanaan Anggaran/Daftar Isian Pelaksanaan Anggaran Daerah) atau dokumen sejenis lainnya dan telah diverifikasi oleh TERTANGGUNG kepada Pengguna Jasa Konstruksi dan Pengadaan Barang/Jasa.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">10.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    Polis Asuransi Kredit adalah bukti persetujuan Asuransi dari PENANGGUNG kepada TERTANGGUNG.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">11.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    Premi Asuransi Kredit adalah sejumlah uang yang diterima oleh PENANGGUNG atas Asuransi yang diberikan oleh PENANGGUNG kepada TERTANGGUNG.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">12.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    Risiko yang ditanggung adalah kegagalan DEBITUR sebagai penerima Kredit Konstruksi dan Pengadaan Barang/Jasa dalam memenuhi kewajiban angsuran kredit sesuai yang diperjanjikan.                                 </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">12.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    Risiko yang ditanggung adalah kegagalan DEBITUR sebagai penerima Kredit Konstruksi dan Pengadaan Barang/Jasa dalam memenuhi kewajiban angsuran kredit sesuai yang diperjanjikan.                                 </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">13.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    Jumlah Kerugian adalah kewajiban hutang yang terdiri dari hutang pokok, hutang bunga dan denda yang tidak dapat dipenuhi oleh DEBITUR sewaktu Polis Kredit tersebut jatuh tempo dan kredit tidak dapat diperpanjang atau sebelum kredit jatuh tempo dan DEBITUR tidak dapat memenuhi kewajiban perikatannya.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">14.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    Ganti Rugi Asuransi (Klaim) adalah sejumlah uang yang harus dibayar oleh PENANGGUNG sebagai ganti rugi atas kerugian yang diderita oleh TERTANGGUNG.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">15.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    Piutang Subrogasi adalah hak tagih berupa uang dari DEBITUR yang beralih dari TERTANGGUNG kepada PENANGGUNG sehubungan dengan telah dibayarnya ganti rugi Asuransi Kredit oleh PENANGGUNG.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">16.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    Agunan adalah jaminan kredit yang disediakan oleh DEBITUR, baik berupa jaminan harta benda maupun jaminan uang tunai.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">17.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                    Hari adalah berdasarkan pada hari kerja, yaitu jumlah hari setelah dikurangi hari Sabtu, Minggu dan Libur Nasional
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block space-before.optimum="5pt" text-align="center">
                                    Pasal 3
                                </fo:block>
                                <fo:block text-align="center" space-after.optimum="5pt">
                                    PROSENTASE GANTI RUGI
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block space-before.optimum="5pt" >
                                    Prosentase jumlah Ganti Rugi yang ditanggung oleh PENANGGUNG untuk setiap kredit yang ditanggung atas nama DEBITUR maksimal 75% (tujuh puluh lima persen) dari realisasi kredit.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block space-before.optimum="5pt" text-align="center">
                                    Pasal 4
                                </fo:block>
                                <fo:block text-align="center" space-after.optimum="5pt">
                                    PENCAIRAN, PERPANJANGAN ASURANSI
                                </fo:block>

                                <fo:block text-align="center" space-after.optimum="5pt">
                                    DAN SUPLESI (TAMBAHAN) KREDIT KONSTRUKSI DAN
                                </fo:block>

                                <fo:block text-align="center" space-after.optimum="5pt">
                                    PENGADAAN BARANG/JASA
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">1.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Pencairan Kredit Konstruksi dan Pengadaan Barang/Jasa ditetapkan sebagai berikut :
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">a.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    TERTANGGUNG bertanggung jawab sepenuhnya bahwa pencairan dan penggunaan kredit dilakukan sesuai :
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">1.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Surat Pemberitahuan Persetujuan Pemberian Kredit yang dikeluarkan oleh TERTANGGUNG dan telah disetujui oleh DEBITUR
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">2.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Perjanjian Kredit beserta Addendum dan/atau perubahannya yang telah ditandatangani oleh TERTANGGUNG dan DEBITUR;
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">3.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Surat konfirmasi persetujuan yang merupakan kesepakatan akhir persetujuan antara PENANGGUNG, TERTANGGUNG, dan DEBITUR, dalam hal Asuransi Kredit jadi diterbitkan;
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">4.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Ketentuan umum kredit yang berlaku pada TERTANGGUNG.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">b.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Perubahan pencairan dan penggunaan kredit sebagaimana dimaksud pada ayat (1) huruf a Pasal ini dapat dilakukan dengan memberitahukan secara tertulis kepada PENANGGUNG.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">2.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   	Perpanjangan Asuransi Kredit Konstruksi dan Pengadaan Barang/Jasa ditetapkan sebagai berikut   : 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">a.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    DEBITUR harus mengajukan permohonan perpanjangan kredit kepada TERTANGGUNG dan selanjutnya TERTANGGUNG mengajukan permohonan perpanjangan Asuransi Kredit kepada PENANGGUNG.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">b.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Pengajuan permohonan perpanjangan jangka waktu kredit sebagaimana dimaksud pada ayat (2) huruf a Pasal ini wajib dilampiri copy dokumen yang menyebabkan kredit diperpanjang.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">c.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Dalam waktu selambat-lambatnya 15 (lima belas) hari sejak diterimanya surat permintaan penegasan perpanjangan Asuransi Kredit dari TERTANGGUNG, PENANGGUNG harus sudah memberikan surat persetujuan perpanjangan Asuransi kredit. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">3.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   	Suplesi (Tambahan) Kredit Konstruksi dan Pengadaan Barang/Jasa dilaksanakan apabila sebelum kredit jatuh tempo, DEBITUR memerlukan suplesi (tambahan) kredit, maka TERTANGGUNG harus mengajukan permohonan Asuransi suplesi (tambahan) kredit kepada PENANGGUNG dengan melampirkan data antara lain namun tidak terbatas pada  :
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <!--                        break-->
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>


                    </fo:table-body>
                </fo:table>
            </fo:block>


            <!-- halaman 2-->


            <fo:block font-size="<%=fontsize%>" text-align="justify">
                <fo:table table-layout="fixed">

                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="8mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="10mm"/>

                    <fo:table-column />
                    <fo:table-body>



                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">a.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    DEBITUR harus mengajukan permohonan perpanjangan kredit kepada TERTANGGUNG dan selanjutnya TERTANGGUNG mengajukan permohonan perpanjangan Asuransi Kredit kepada PENANGGUNG.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">b.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Rencana penyelesaian proyek dan pelunasan kredit setelah suplesi (tambahan) kredit.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block space-before.optimum="5pt" text-align="center">
                                    Pasal 5
                                </fo:block>
                                <fo:block text-align="center" space-after.optimum="5pt">
                                    PREMI ASURANSI
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">1.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   	PENANGGUNG menetapkan tarif Premi Asuransi yang termuat dalam surat tersendiri yang tidak terpisahkan dan merupakan satu kesatuan dengan Polis ini. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">2.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   	Premi Asuransi wajib dibayar lunas sekaligus di muka dan disetorkan oleh TERTANGGUNG ke rekening PENANGGUNG di BPD. Cabang. No.Rekening. selambat-lambatnya ...
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">3.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   	Atas adanya perpanjangan Kredit Konstruksi dan Pengadaan Barang/Jasa, maka Premi Asuransi dibayarkan sebagaimana dimaksud dalam ayat (2) Pasal ini.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">4.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   	Atas adanya tambahan (suplesi/tambahan) Kredit Konstruksi dan Pengadaan Barang/Jasa, maka Premi Asuransi dibayarkan kepada PENANGGUNG dengan besaran yang dihitung dari tambahan (suplesi/tambahan) kredit. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">5.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   	PENANGGUNG memberitahukan secara tertulis kepada TERTANGGUNG dalam hal terdapat perubahan terhadap tarif Premi Asuransi .
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block space-before.optimum="5pt" text-align="center">
                                    Pasal 6
                                </fo:block>
                                <fo:block text-align="center" space-after.optimum="5pt">
                                    TATA CARA PENGAJUAN KLAIM
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">1.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		TERTANGGUNG berhak untuk mengajukan klaim kepada PENANGGUNG dalam hal kredit Konstruksi dan Pengadaan Barang/Jasa telah jatuh tempo, atau Kredit Konstruksi dan Pengadaan Barang/Jasa sebelum jatuh tempo apabila kredit telah dinyatakan macet sesuai dengan ketentuan Bank Indonesia.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">2.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Surat Pengajuan Klaim dilakukan oleh TERTANGGUNG secara tertulis selambat-lambatnya 2 (dua) bulan sejak timbulnya hak klaim sebagaimana dimaksud dalam ayat (1) Pasal ini yang dilengkapi dengan : 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">a.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Copy IKHTISAR PERTANGGUNGAN;
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">b.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Copy Bukti pelunasan Premi Asuransi;
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">c.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Copy Perjanjian Kredit;
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">d.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Berita Acara Klaim yang memuat hal-hal sebagai berikut;
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">-</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Data DEBITUR
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">-</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Penyebab macetnya kredit
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">-</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Pernyataan kredit macet dari TERTANGGUNG
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">-</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Besarnya tuntutan ganti rugi
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">e.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Copy kartu pinjaman/rekening koran DEBITUR tahun terakhir.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>



                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">3.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   	PENANGGUNG wajib menginformasikan secara tertulis kepada TERTANGGUNG apabila berkas pengajuan klaim belum diterima secara lengkap selambat-lambatnya 7 (tujuh) hari sejak pengajuan klaim diterima.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">4.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Atas pengajuan klaim yang tidak lengkap, TERTANGGUNG wajib untuk melengkapinya dalam waktu 2 (dua) bulan sejak pemberitahuan dari PENANGGUNG. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">5.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Klaim yang dapat diajukan oleh TERTANGGUNG sebesar jumlah kerugian (pokok + bunga) dikalikan dengan prosentase Jumlah Pertanggungan, dengan batas setinggi-tingginya sebesar Jumlah Pertanggungan yang direalisasikan dikalikan dengan prosentase Jumlah Pertanggungan .
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">6.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Dasar perhitungan jumlah kredit yang diklaim, dituangkan dalam Berita Acara Klaim yang ditandatangani TERTANGGUNG dan DEBITUR. Apabila DEBITUR tidak dapat menandatangani Berita Acara Klaim, maka TERTANGGUNG wajib untuk menjelaskan alasan-alasan penyebab DEBITUR tidak dapat menandatangani Berita Acara Klaim dimaksud. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>




                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block space-before.optimum="5pt" text-align="center">
                                    Pasal 7
                                </fo:block>
                                <fo:block text-align="center" space-after.optimum="5pt">
                                    KEPUTUSAN KLAIM
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>



                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">1.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		PENANGGUNG wajib memberikan Keputusan atas klaim yang diajukan TERTANGGUNG selambat-lambatnya 2 (dua) bulan terhitung sejak berkas pengajuan klaim dari TERTANGGUNG diterima lengkap oleh PENANGGUNG. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>



                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">2.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Atas klaim yang dapat disetujui PENANGGUNG, PENANGGUNG akan menyampaikan persetujuan klaim tersebut dalam bentuk Surat Keputusan Klaim kepada TERTANGGUNG. Surat tersebut memuat data tentang: 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>




                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">a.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Jumlah klaim yang diajukan oleh TERTANGGUNG;
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">b.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Jumlah klaim yang akan dibayar oleh PENANGGUNG;
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">c.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Jumlah kewajiban/risiko yang ditanggung sendiri oleh TERTANGGUNG;
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>



                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">3.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Apabila jangka waktu pemberian keputusan klaim sebagaimana tersebut dalam ayat (1) Pasal ini telah berakhir dan PENANGGUNG ternyata belum memberikan keputusan klaim tersebut, maka klaim tersebut dianggap disetujui oleh PENANGGUNG, dan PENANGGUNG wajib untuk memenuhi semua kewajibannya/melaksanakan pembayaran klaim tersebut kepada TERTANGGUNG. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>



                        <!--                        break-->
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>


                    </fo:table-body>
                </fo:table>
            </fo:block>


            <!--                       halaman 3-->


            <fo:block font-size="<%=fontsize%>" text-align="justify">
                <fo:table table-layout="fixed">

                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="8mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="10mm"/>

                    <fo:table-column />
                    <fo:table-body>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">4.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Dalam waktu 15 (lima belas) hari dihitung sejak PENANGGUNG menyampaikan Surat Persetujuan Klaim tersebut kepada TERTANGGUNG sebagaimana tersebut dalam ayat (2) Pasal ini PENANGGUNG wajib untuk mentransfer atau memindahbukukan dana kepada TERTANGGUNG sebesar jumlah klaim yang dibayar sebagaimana tersebut dalam Surat Persetujuan Klaim.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">5.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Hak TERTANGGUNG untuk mengajukan klaim menjadi hapus dengan sendirinya (daluwar-sa) apabila : 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>




                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">a.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    TERTANGGUNG tidak mengajukan kepada PENANGGUNG setelah lewat 2 (dua) bulan sejak timbulnya hak untuk mengajukan klaim sebagaimana yang diatur pada pasal 7 ayat (1).
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">b.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    TERTANGGUNG tidak melengkapi berkas pengajuan klaim yang belum lengkap sebagaimana diatur Pasal 7 ayat (2) dan (4) Polis ini.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block space-before.optimum="5pt" text-align="center">
                                    Pasal 8
                                </fo:block>
                                <fo:block text-align="center" space-after.optimum="5pt">
                                    PENGAWASAN, PEMBINAAN DAN PELAPORAN
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">1.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		TERTANGGUNG berkewajiban melakukan pengawasan dan pembinaan terhadap DEBITUR. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">2.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		PENANGGUNG untuk dan atas nama TERTANGGUNG dapat memeriksa pembukuan DEBITUR, aset-aset DEBITUR yang dijadikan Agunan, kegiatan usaha DEBITUR dan kegiatan-kegiatan lain yang dianggap penting oleh PENANGGUNG. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">3.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		TERTANGGUNG berkewajiban melakukan tindakan yang diperlukan guna pengamanan kredit menurut cara yang lazim dilakukan oleh TERTANGGUNG apabila ditemukan indikasi kredit akan bermasalah.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>



                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">4.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Kantor Cabang TERTANGGUNG wajib menyampaikan kepada PENANGGUNG laporan bulanan atas penyaluran kredit yang dijamin oleh PENANGGUNG dalam bentuk laporan nominatif yang memuat antara lain jumlah DEBITUR, jumlah kredit, kolektibilitas kredit, sisa kredit, tunggakan pokok kredit, tunggakan bunga kredit, dan jangka waktu kredit. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">5.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Terhadap DEBITUR yang kreditnya bermasalah, dibuatkan laporan atas kredit bermasalah dimaksud secara terperinci yang merupakan lampiran laporan nominatif sebagaimana dimaksud pada ayat (4) Pasal ini. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">6.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   			Laporan Nominatif sebagaimana dimaksud pada ayat (4) dan (5) Pasal ini selambat-lambatnya sudah harus diterima oleh PENANGGUNG setiap minggu kedua bulan berikutnya. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>



                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block space-before.optimum="5pt" text-align="center">
                                    Pasal 9
                                </fo:block>
                                <fo:block text-align="center" >
                                    PENAGIHAN PIUTANG SUBROGASI
                                </fo:block>
                                <fo:block text-align="center" space-after.optimum="5pt">
                                    SETELAH PEMBAYARAN KLAIM
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">1.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   			Laporan Nominatif sebagaimana dimaksud pada ayat (4) dan (5) Pasal ini selambat-lambatnya sudah harus diterima oleh PENANGGUNG setiap minggu kedua bulan berikutnya.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">2.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		PENANGGUNG dan TERTANGGUNG dapat melakukan upaya penagihan baik bersama-sama maupun sendiri-sendiri.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">3.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Apabila PENANGGUNG dan/atau TERTANGGUNG berhasil melakukan penagihan sebagaimana dimaksud pada ayat (1) dan ayat (2) Pasal ini, maka PENANGGUNG dan/atau TERTANGGUNG harus memberitahukan secara tertulis kepada PIHAK lainnya.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">4.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Apabila upaya penagihan Piutang Subrogasi sebagaimana dimaksud pada ayat (1) Pasal ini telah dilakukan dengan optimal, namun DEBITUR tetap menunggak pembayaran angsuran 3 (tiga) kali berturut-turut, maka PENANGGUNG dan/atau TERTANGGUNG melakukan penjualan Agunan kredit, baik melalui di bawah tangan maupun melalui eksekusi.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">5.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Hasil penagihan dan/atau penjualan sebagaimana dimaksud pada ayat (1), ayat (3) dan ayat (4) Pasal ini sesuai dengan haknya wajib dibayarkan kepada PARA PIHAK dalam waktu paling lambat 2 (dua) bulan sejak pembayaran diterima di rekening/kas PARA PIHAK, dan apabila salah satu PIHAK tidak melaksanakannya, maka PIHAK tersebut akan dikenakan denda sebesar tingkat suku bunga deposito yang berlaku di TERTANGGUNG.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">6.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		TERTANGGUNG wajib menyampaikan laporan triwulan secara tertulis tentang perkembangan hasil penagihan Piutang Subrogasi kepada PENANGGUNG dan PENANGGUNG wajib menyampaikan laporan triwulan secara tertulis tentang perkembangan hasil penagihan Piutang Subrogasi kepada TERTANGGUNG. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">7.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Apabila dalam jangka waktu 4 (empat) bulan sejak pembayaran klaim upaya penagihan sebagaimana dimaksud pada ayat (1) sampai dengan ayat (6) Pasal ini tidak memberikan hasil yang maksimal, maka TERTANGGUNG wajib menyerahkan penanganan penagihan kredit dimaksud kepada Pengadilan/pihak yang berwenang setempat sesuai dengan Peraturan Perundang-undangan yang berlaku. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>



                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block space-before.optimum="5pt" text-align="center">
                                    Pasal 10
                                </fo:block>
                                <fo:block text-align="center" >
                                    GUGURNYA HAK KLAIM
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9">
                                <fo:block  space-after.optimum="5pt" >
                                   		Hak klaim TERTANGGUNG menjadi gugur apabila memenuhi salah satu atau lebih kriteria sebagai berikut : 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>



                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">1.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Polis kredit beserta Akta Perubahan dan/atau Addendum yang menyertainya dibatalkan oleh Pengadilan; 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">1.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Polis kredit beserta Akta Perubahan dan/atau Addendum yang menyertainya dibatalkan oleh Pengadilan;
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">2.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Premi Asuransi sesuai dengan ketentuan pada Pasal 6 Polis ini tidak dibayarkan kepada PENANGGUNG.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">3.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		TERTANGGUNG tidak mengajukan klaim kepada PENANGGUNG setelah lewat 2 (dua) bulan sejak timbulnya hak mengajukan klaim sebagaimana dimaksud pada Pasal 7 ayat (2) Polis ini. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">4.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		TERTANGGUNG tidak melengkapi berkas pengajuan klaim sebagaimana dimaksud dalam Pasal 7 ayat (2) Polis ini. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">5.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Adanya keputusan pengadilan yang mempunyai kekuatan hukum tetap yang memutuskan adanya tindak pidana/perdata yang merugikan PENANGGUNG yang dilakukan oleh TERTANGGUNG dengan DEBITUR. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">6.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Legalitas usaha DEBITUR yang dibiayai TERTANGGUNG dan ditanggung oleh PENANGGUNG tidak sesuai dengan ketentuan Perpres No.54 tahun 2010 tentang Tata Cara Penga-daan Barang/Jasa Pemerintah berikut perubahannya.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">7.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		TERTANGGUNG tidak memenuhi ketentuan pencairan, perpanjangan Asuransi dan suplesi (tambahan) kredit dan pembayaran tagihan proyek sebagaimana diatur dalam Pasal 5 Polis ini. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">8.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		TERTANGGUNG mengembalikan, mengalihkan dan/atau mencairkan Agunan tanpa terlebih dahulu mendapat persetujuan secara tertulis dari PENANGGUNG.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block space-before.optimum="5pt" text-align="center">
                                    Pasal 11
                                </fo:block>
                                <fo:block text-align="center" >
                                    KERUGIAN YANG TIDAK DITANGGUNG
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>

                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   			PENANGGUNG tidak diwajibkan membayar ganti-rugi atau klaim dalam hal kerugian tersebut disebabkan oleh :
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">1.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Surat Perintah Kerja/Kontrak adalah palsu. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">2.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Pengguna Jasa atau Obligee telah melakukan pembayaran namun pembayaran tersebut tidak digunakan sebagai sumber pengembalian kredit.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">3.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Pekerjaan konstruksi dan/atau pengadaan barang/jasa yang tercantum dalam Kontrak tidak terdapat dalam mata anggaran APBD, BUMN dan BUMD. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>



                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">4.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Pembayaran termijn telah dilakukan oleh pemilik proyek melalui rekening DEBITUR di TERTANGGUNG, namun dicairkan kembali sebagian atau seluruhnya oleh DEBITUR untuk pendanaan proyek-proyek lainnya maupun untuk keperluan lainnya. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">5.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Pembayaran termijn telah dilakukan oleh pemilik proyek melalui rekening DEBITUR di TERTANGGUNG, namun dicairkan kembali sebagian atau seluruhnya oleh DEBITUR untuk pendanaan proyek-proyek lainnya maupun untuk keperluan lainnya.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>



                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">6.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Terdapat tambahan pekerjaan yang tidak didukung dengan Addendum Kontrak atau dokumen yang sejenis yang sekurang-kurangnya memuat jenis dan nilai tambahan pekerjaan, waktu penyelesaian dan sumber pembayaran terkait. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">7.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Reaksi nuklir, kontaminasi radioaktif, radiasi reaksi inti atom yang secara langsung maupun tidak langsung mengakibatkan kegagalan usaha DEBITUR untuk melunasi kredit tanpa memandang bagaimana dan di mana terjadinya; 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">8.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Terjadinya peperangan baik dinyatakan maupun tidak atau sebagian wilayah Indonesia dinyatakan dalam keadaan bahaya atau dalam keadaan darurat perang; 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">9.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Terjadinya huru-hara yang berkaitan dengan gerakan politik yang langsung mengakibatkan kegagalan DEBITUR untuk melunasi kreditnya;
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">10.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Tindakan hukum yang dilakukan oleh Pemerintah Republik Indonesia terhadap DEBITUR dan/atau TERTANGGUNG; 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">11.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Terjadinya bencana alam yang mengakibatkan kerugian langsung kepada usaha DEBITUR.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">12.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Adanya pemufakatan jahat yang dilakukan oleh pihak-pihak yang terlibat dalam Polis Kredit. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">13.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Kelalaian atau kesalahan yang dilakukan oleh pihak TERTANGGUNG.

                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>



                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block space-before.optimum="5pt" text-align="center">
                                    Pasal 12
                                </fo:block>
                                <fo:block text-align="center" >
                                    AGUNAN
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>



                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">1.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		TERTANGGUNG dalam memberikan kredit kepada DEBITUR menerima Agunan (jaminan kebendaan dan/atau bukan kebendaan) dari dan milik DEBITUR dan Agunan dimaksud telah diikat secara hukum sesuai dengan ketentuan yang berlaku pada TERTANGGUNG. 

                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">2.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		TERTANGGUNG tidak dibenarkan mengembalikan Agunan milik DEBITUR, kepada DEBITUR /pihak lain, kecuali dengan persetujuan tertulis dari PENANGGUNG, atau DEBITUR telah melunasi seluruh kreditnya kepada TERTANGGUNG. Apabila dalam waktu selambat-lambatnya 15 (lima belas) hari sejak diterimanya permohonan pengembalian Agunan milik DEBITUR oleh PENANGGUNG dan PENANGGUNG tidak memberikan jawaban tertulis kepada TERTANGGUNG, maka dinyatakan telah disetujui oleh PENANGGUNG. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>



                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">3.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Dalam hal TERTANGGUNG bermaksud untuk melakukan penjualan dan atau mencairkan Agunan milik DEBITUR dalam rangka melunasi seluruh kredit sebelum atau setelah kredit jatuh tempo, maka TERTANGGUNG dapat melakukan penjualan Agunan milik DEBITUR tanpa terlebih dahulu meminta persetujuan dari PENANGGUNG.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">4.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Dalam hal terdapat Agunan likuid (berupa deposito/cash collateral), Agunan dimaksud dicairkan sesuai ketentuan dan persyaratan TERTANGGUNG dalam waktu 15 (lima belas) hari sejak pembayaran klaim. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">5.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Hasil pencairan Agunan likuid sebagaimana dimaksud pada ayat (4) Pasal ini dipergunakan untuk pembayaran tunggakan kredit dan Piutang Subrogasi secara proporsional sebesar prosentase Jumlah Ganti Rugi sebagaimana dimaksud pada Pasal 3 Polis ini. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">6.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		Dalam hal pencairan Agunan dilakukan melalui Pengadilan/pihak yang berwenang setempat yang sesuai dengan peraturan perundang-undangan yang berlaku, TERTANGGUNG harus memberitahukan secara tertulis kepada PENANGGUNG hasil keputusan pengadilan/pihak yang berwenang dimaksud disertai salinan keputusannya dalam waktu paling lambat 15 (lima belas) hari sejak tanggal dikeluarkannya keputusan pengadilan/pihak yang berwenang dimaksud. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>



                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block space-before.optimum="5pt" text-align="center">
                                    Pasal 13
                                </fo:block>
                                <fo:block text-align="center" >
                                    RAHASIA BANK
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">1.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   		PARA PIHAK sepakat dan setuju bahwa segala informasi dan keterangan baik tertulis maupun tidak tertulis yang diketahui dan / atau timbul berdasarkan Polis ini yang diterima oleh dan/atau dari salah satu pihak baik seluruh maupun sebagian memiliki sifat rahasia tidak boleh diberitahukan kepada pihak ketiga atau badan atau orang yang tidak berkepen-tingan dengan alasan apapun juga selama dan sesudah berakhirnya Polis ini, kecuali: 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">a.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Diperintahkan oleh badan peradilan atau instansi pemerintah lainnya yang berhubungan dengan penegakan hukum secara tertulis atau resmi;
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">b.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Menurut peraturan perundang-undangan yang berlaku di Indonesia, informasi tersebut harus diberikan kepada pihak lain yang disebut secara jelas dalam peraturan perundang-undangan tersebut;
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">c.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Yang telah disepakati bersama dan tidak bersifat rahasia. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <!--                        break-->
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        <% }%>

                    </fo:table-body>
                </fo:table>
            </fo:block>

            <!--                       halaman 4-->

            <fo:block font-size="<%=fontsize%>" text-align="justify">
                <fo:table table-layout="fixed">

                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="8mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="10mm"/>

                    <fo:table-column />
                    <fo:table-body>



                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block space-before.optimum="5pt" text-align="center">
                                    Pasal 14
                                </fo:block>
                                <fo:block text-align="center" >
                                    PERSELISIHAN
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">1.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    	Apabila terjadi perselisihan antara PENANGGUNG dan TERTANGGUNG, maka PENANGGUNG dan TERTANGGUNG sepakat untuk menyelesaikan secara musyawarah dan mufakat. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">2.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                    Apabila cara-cara penyelesaian sebagaimana dimaksud pada ayat (1) Pasal ini tidak dapat ditempuh, maka PENANGGUNG dan TERTANGGUNG sepakat untuk menyerahkan kepada Pengadilan Negeri Republik Indonesia
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="10">
                                <fo:block space-before.optimum="5pt" text-align="center">
                                    Pasal 15
                                </fo:block>
                                <fo:block text-align="center" >
                                    PENUTUP
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">1.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   	Penyimpangan-penyimpangan dari Polis ini hanya dapat dilakukan atas persetujuan bersama antara PENANGGUNG dan TERTANGGUNG. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>





                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">2.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   	Surat-surat dan lampiran-lampiran yang berhubungan dengan Polis ini merupakan satu kesatuan yang tidak dapat dipisahkan dari Polis ini sehingga Polis ini tidak akan dibuat tanpa adanya surat-surat dan lampiran-lampiran tersebut.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">3.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   	Dalam hal hari dan/atau tanggal yang dimaksud dalam Polis ini jatuh pada hari libur, maka PARA PIHAK sepakat untuk menyelesaikan pada hari berikutnya. 
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>


                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">4.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   	PENANGGUNG dan TERTANGGUNG menyatakan bahwa pihak-pihak yang menandatangani Polis ini dan surat-surat lainnya/lampirannya adalah pihak-pihak yang berhak dan berwenang mewakili masing-masing PENANGGUNG dan TERTANGGUNG sesuai dengan ketentuan dalam anggaran dasar dan/atau keputusan/ketentuan yang berlaku pada masing-masing PENANGGUNG dan TERTANGGUNG.  
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="5pt">5.</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="8">
                                <fo:block  space-after.optimum="5pt" >
                                   	Segala ketentuan dan syarat-syarat dalam Polis ini berlaku serta mengikat PENANGGUNG dan TERTANGGUNG yang menandatangani dan pengganti-penggantinya.
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>




                    </fo:table-body>
                </fo:table>
            </fo:block>





            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">

                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="100mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Dibuat di, <%=JSPUtil.printX(pol.getCostCenter(pol.getStCostCenterCode()).getStDescription())%> , <%=JSPUtil.print(DateUtil.getDateStr(pol.getDtPolicyDate(), "dd-MM-yyyy"))%></fo:block>
                                <fo:block text-align="center">PENANGGUNG</fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block text-align="center"> Kantor Cabang,<%=JSPUtil.printX(pol.getCostCenter(pol.getStCostCenterCode()).getStDescription())%> </fo:block>

                            </fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
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
                                        content-height="60%"
                                        width="60%"
                                        scaling="uniform" src="url(<%=pol.getUser(Parameter.readString("BRANCH_NAME_" + pol.getStCostCenterCode())).getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <% } else {%>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="60%"
                                        width="60%"
                                        scaling="uniform" src="url(<%=pol.getUserApproved().getFile().getStFilePath()%>)"  />
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
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%= Parameter.readString("BRANCH_SIGN_" + pol.getStCostCenterCode())%></fo:inline></fo:block></fo:table-cell>
                            <% } else {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%=JSPUtil.printX(pol.getUserApproved().getStUserName()).toUpperCase()%></fo:inline></fo:block></fo:table-cell>
                            <% }%>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>

                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) {%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center" ><%if (pol.getParaf() != null) {%><fo:leader leader-length="10mm"/><%}%><%=JSPUtil.printX(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%>                                     <%if (pol.getParaf() != null) {%>                                     <fo:external-graphic                                         content-height="scale-to-fit" height="0.3in"  content-width="0.3in" scaling="non-uniform"                                         overflow="hidden" src="url(<%=pol.getUser(pol.getParaf()).getFile().getStFilePath()%>)"  />                                     <%}%>                             </fo:block></fo:table-cell>
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
                               margin-top="1cm"
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