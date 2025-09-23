<%@ page import="com.webfin.insurance.model.*,
com.crux.ff.model.FlexFieldHeaderView,
com.crux.ff.model.FlexFieldDetailView,
com.crux.util.*,
java.util.Date,
com.crux.util.fop.FOPUtil"%><?xml version="1.0" encoding="utf-8"?>
<%

final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
final String fontsize = (String) request.getAttribute("FONTSIZE");

boolean effective = pol.isEffective();


//if (true) throw new NullPointerException();

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
        <%--
    <!-- usage of page layout -->
    <!-- header -->
    <fo:static-content flow-name="xsl-region-before">
<fo:block-container height="1cm" width="15cm" top="0cm" left="0cm" position="absolute">    <fo:block>
      <fo:retrieve-marker retrieve-class-name="message"
      retrieve-boundary="page"
      retrieve-position="first-starting-within-page"/>
      </fo:block>
</fo:block-container>
<fo:block-container height="1cm" width="15cm" top="0cm" left="0cm" position="absolute">    <fo:block>
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
    </fo:static-content> --%>

        <fo:flow flow-name="xsl-region-body">
            
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
                            <fo:table-cell><fo:block><%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align = "end">{L-ENG Value : -L}{L-INA Nilai : -L}<%=JSPUtil.print(pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getDbInsuredAmount(),2)%> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.print(pol.getStCurrencyCode())%><%=JSPUtil.printX(pol.getDbInsuredAmount(),2)%> </fo:block></fo:table-cell>
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
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Dengan ini dinyatakan bahwa kami : -L} <fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference1())%></fo:inline> {L-ENG 1. -L}{L-INA yang beralamat di -L} <%=JSPUtil.printX(obj.getStReference3())%>  {L-ENG 1. -L}{L-INA sebagai Kontraktor, selanjutnya disebut PRINCIPAL, dan -L} <fo:inline font-weight="bold"><%=JSPUtil.printX(obj.getStReference6Desc())%></fo:inline> {L-ENG 1. -L}{L-INA yang beralamat di -L}<%=JSPUtil.printX(obj.getStReference7())%>{L-ENG 1. -L}{L-INA sebagai PENJAMIN, selanjutnya disebut sebagai SURETY, bertanggung jawab dan dengan tegas terikat pada -L} <fo:inline font-weight="bold"><%=JSPUtil.xmlEscape(pol.getStCustomerName())%></fo:inline> {L-ENG 1. -L}{L-INA yang beralamat di -L} <%=pol.getStCustomerAddress()%>{L-ENG 1. -L}{L-INA sebagai Pemilik, selanjutnya disebut sebagai OBLIGEE atas uang sejumlah -L}<%=JSPUtil.print(pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getDbInsuredAmount(),2)%> {L-ENG 1. -L}{L-INA Terbilang ( -L}<%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbInsuredAmount(),2))%> <%=JSPUtil.printX(pol.getCurrency().getStCcyDescription())%> ) {L-ENG XXX -L}{L-INA yang harus dibayar kepada OBLIGEE -L}</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 2. -L}{L-INA 2. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Maka kami PRINCIPAL dan SURETY dengan ini mengikatkan diri untuk melakukan pembayaran jumlah tersebut diatas dengan baik dan benar. -L} </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 3. -L}{L-INA 3. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Bahwa PRINCIPAL dengan suatu perjanjian tertulis No. -L} <%=JSPUtil.printX(obj.getStReference10())%> tanggal <%=DateUtil.getDateStr(obj.getDtReference1(),"d ^^ yyyy")%> {L-ENG XXX -L}{L-INA telah mengadakan Kontrak dengan OBLIGEE untuk pekerjaan -L}<%=JSPUtil.printX(obj.getStReference11())%> {L-ENG XXX -L}{L-INA dengan Harga Kontrak yang telah disetujui sebesar -L}<%=JSPUtil.print(pol.getStCurrencyCode())%> <%=JSPUtil.printX(obj.getDbReference2(),2)%> {L-ENG 1. -L}{L-INA (Terbilang : -L}<%=NumberSpell.readNumber(JSPUtil.printX(obj.getDbReference2(),2),"",NumberSpell.INDONESIA)%> RUPIAH) {L-ENG XXX -L}{L-INA dan kontrak tersebut merupakan bagian yang tidak terpisahkan dari Jaminan ini. -L}</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 4. -L}{L-INA 4. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Bahwa untuk Kontrak tersebut diatas, OBLIGEE setuju membayar kepada PRINCIPAL uang sebesar -L} <%=JSPUtil.print(pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getDbInsuredAmount(),2)%> {L-ENG 1. -L}{L-INA (Terbilang : -L}<%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbInsuredAmount(),2),"",NumberSpell.INDONESIA)%> RUPIAH) {L-ENG XXX -L}{L-INA sebagai pembayaran uang muka sebelum Pekerjaan menurut Kontrak diatas dimulai. Sebagai jaminan terhadap pembayaran Uang Muka itu maka SURETY memberikan jaminan dengan ketentuan tersebut dibawah ini -L}</fo:block></fo:table-cell>
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
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Jika PRINCIPAL telah melakukan pembayaran kembali kepada OBLIGEE seluruh jumlah Uang Muka dimaksud (yang dinyatakan dalam surat tanda bukti penerimaan olehnya) atau sisa Uang Muka yang wajib dibayar menurut Kontrak tersebut, maka surat Jaminan ini menjadi batal dan tidak berlaku ; sebaliknya jika tidak maka Surat Jaminan ini tetap berlaku dan efektif mulai dari tanggal -L}<%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d ^^ yyyy")%> {L-ENG xxx -L}{L-INA sampai dengan tanggal-L} <%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"d ^^ yyyy")%> {L-ENG xxx -L}{L-INA (selama berlakunya kontrak atau sampai pada tanggal uang muka telah dibayar kembali seluruhnya).-L} </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 6. -L}{L-INA 6. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Tuntutan ganti rugi atas surat Jaminan ini dilaksanakan oleh OBLIGEE secara tertulis kepada SURETY segera setelah timbul cidera janji (Wanprestasi/default) oleh pihak PRINCIPAL, karena tidak dapat membayar kembali Uang Muka atau sisa Uang Muka tersebut sesuai dengan syarat Kontrak. -L} </fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 7. -L}{L-INA 7. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA SURETY akan membayar kepada OBLIGEE Uang Muka atau sisa Uang Muka yang berdasarkan kontrak belum dikembalikan oleh PRINCIPAL setelah menerima tuntutan penagihan (Klaim) dari OBLIGEE. -L} </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 8. -L}{L-INA 8. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Menunjuk pada Pasal 1832 KUH Perdata dengan ini ditegaskan kembali bahwa SURETY melepaskan hak-hak istimewanya untuk menuntut supaya harta benda pihak yang dijamin lebih dahulu disita dan dijual guna melunasi hutangnya sebagaimana dimaksud dalam Pasal 1831 KUH Perdata. -L} </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG 9. -L}{L-INA 9. -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Setiap pengajuan ganti rugi terhadap SURETY berdasarkan Jaminan ini harus sudah diajukan selambat-lambatnya dalam waktu 1 (satu) bulan sesudah berakhirnya masa laku jaminan ini. -L} </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>{L-ENG XXX -L}{L-INA Ditanda tangani serta dibubuhkan materai di -L} <%= JSPUtil.printX(pol.getStCostCenterDesc())%>{L-ENG XXX -L}{L-INA pada tanggal -L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%> </fo:block></fo:table-cell>
                        </fo:table-row>   
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <%-- mulai rincian --%> 
       
            <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="0pt" space-after.optimum="5pt"
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
                            <fo:table-cell><fo:block>Service Charge</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "end"><%=JSPUtil.printX(pol.getDbPremiTotal(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">{L-ENG XXX -L}{L-INA PRINCIPAL -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">{L-ENG XXX -L}{L-INA SURETY -L}</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block>Biaya Materai</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "end"><%=JSPUtil.printX(pol.getDbTotalItemAmount("SFEE"),2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=JSPUtil.printX(obj.getStReference1())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block>Administrasi</fo:block></fo:table-cell>                   
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "end"><%=JSPUtil.printX(pol.getDbTotalItemAmount("PCOST"),2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-before.optimum="10pt"></fo:block></fo:table-cell>
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
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "end"><%=JSPUtil.printX(pol.getDbTotalDue(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="1pt"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>  
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block space-after.optimum="40pt"></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block space-after.optimum="40pt"></fo:block></fo:table-cell>                            
                        </fo:table-row> 
                        
                    </fo:table-body>
                </fo:table>
                
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
                <% } %>      
            </fo:block>
            
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>
            
            
            <fo:block font-size="6pt"
                      font-family="sans-serif"
                      line-height="10pt"
                      space-after.optimum="10pt"
                      text-align="left" >
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(),"dd/MM/yyyy HH:mm:ss"))%> PrintStamp:<%=pol.getStPrintCode()%> PrintCode:<%=pol.getStPrintStamp()%> Page:<fo:page-number/> Terlampir : [<%=JSPUtil.print(request.getAttribute("DOCUMENT_NAME"))%>]
                
            </fo:block>
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>

