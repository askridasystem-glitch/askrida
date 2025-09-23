<%@ page import="com.webfin.insurance.model.*,
com.crux.ff.model.FlexFieldHeaderView,
com.crux.ff.model.FlexFieldDetailView,
com.crux.util.*, 
java.math.BigDecimal, 
java.util.Date,
com.crux.web.controller.SessionManager, 
com.crux.common.parameter.Parameter,
com.crux.util.fop.FOPUtil"%><?xml version="1.0" encoding="utf-8"?>
<%

final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
final String fontsize = (String) request.getAttribute("FONTSIZE");
final String attached = (String) request.getAttribute("attached");

String param[] = attached.split("[\\|]");

boolean isAttached = param[0].equalsIgnoreCase("2")?true:false;
boolean tanpaTanggal = param[0].equalsIgnoreCase("3")?true:false;
boolean tanpaNama = param[0].equalsIgnoreCase("5")?true:false;
boolean tanpaNamaTanggal = param[0].equalsIgnoreCase("6")?true:false;
boolean tanpaRate = param[0].equalsIgnoreCase("7")?true:false;
boolean klausulaTerlampir = param[0].equalsIgnoreCase("8")?true:false;

if(attached.length()>1){
    if(param[1]!=null){
        isAttached = !isAttached?param[1].equalsIgnoreCase("2")?true:false:isAttached;
        tanpaTanggal = !tanpaTanggal?param[1].equalsIgnoreCase("3")?true:false:tanpaTanggal;
        tanpaRate = !tanpaRate?param[1].equalsIgnoreCase("7")?true:false:tanpaRate;
    }
}

final String preview = (String) request.getAttribute("preview");
boolean isPreview = false;
if (preview!=null)
    if (preview.equalsIgnoreCase("Preview")) isPreview = true;


boolean effective = pol.isEffective();

String jenis_pol = pol.getStPolicyTypeDesc2().toUpperCase();
String nama = "";
String usia = "";
String lama = "";
Date tglLahir=null;
Date tglMulaiKreasi=null;
Date tglAkhirKreasi=null;
BigDecimal insured=null;

String gl_code = pol.getEntity().getStGLCode();

//if (true) throw new NullPointerException();

String desc = pol.getStKreasiTypeDesc().replaceAll("20 -"," ");
final String digitalsign = (String) request.getAttribute("digitalsign");
boolean usingDigitalSign = false;
if (digitalsign!=null)
    if (digitalsign.equalsIgnoreCase("Y")) usingDigitalSign = true;

boolean isUsingBarcode = Parameter.readString("REPORT_SIGN_TYPE").equalsIgnoreCase("BARCODE");
%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-height="30cm"
                               page-width="21cm"
                               margin-top="3cm"
                               margin-bottom="1cm"
                               margin-left="1.5cm"
                               margin-right="1.5cm">
            
            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>
        
    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">\
        
        <fo:flow flow-name="xsl-region-body">
            
            <% if (!effective) {%>
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" text-align="justify" space-before.optimum="2pt" space-after.optimum="2pt"></fo:block>
            
            <fo:block font-size="16pt"
                      text-align="center">
                {L-ENG Policy No.-L}{L-INA No. Polis -L} <%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%>
            </fo:block>
            
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" text-align="justify" space-before.optimum="2pt" space-after.optimum="2pt"></fo:block>
            <% }%>
            
            <% if (isPreview) { %>
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
                {L-INA ASURANSI -L}<%=jenis_pol.replaceAll("PA"," ")%>{L-ENG INSURANCE -L}
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
                            <fo:table-cell><fo:block font-weight="bold"><%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end" font-weight="bold">{L-ENG Entity. No.   -L}{L-INA Entity. No.   -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-weight="bold"><%= pol.getStEntityID() %></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end" font-weight="bold">{L-ENG GL Code No.   -L}{L-INA GL Code No.   -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-weight="bold"><%= JSPUtil.printX(gl_code) %></fo:block></fo:table-cell>
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
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG NAME -L}{L-INA NAMA -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-weight="bold"><%= JSPUtil.printX(pol.getStCustomerName()) %></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-before.optimum="2pt" space-after.optimum="2pt" text-align="justify"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG ADDRESS -L}{L-INA ALAMAT -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-weight="bold"><%= JSPUtil.printX(pol.getStCustomerAddress()) %></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block space-before.optimum="2pt" space-after.optimum="2pt" text-align="justify"></fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align="justify">
                seterusnya dinyatakan sebagai Penanda Tangan perjanjian, untuk pembayaran jumlah-jumlah uang pertanggungan seperti tersebut dibawah ini dalam hal :
            </fo:block>
            
            <fo:block space-before.optimum="2pt" space-after.optimum="2pt" text-align="justify"></fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align="justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        <% 
                        final DTOList objects3 = pol.getObjects();
                        for(int j = 0;j<objects3.size();j++){
                            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects3.get(j);
                            
                            nama = obj.getStReference1();
                            
                            usia = obj.getStReference2();
                            
                            lama = obj.getStReference6();
                            
                            tglLahir = obj.getDtReference1();
                            
                            tglMulaiKreasi = obj.getDtReference2();
                            
                            tglAkhirKreasi = obj.getDtReference3();
                            
                            insured = obj.getDbObjectInsuredAmount();
                        
                        %>   
                        
                        <% } %>                 
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG Member-L}{L-INA Peserta-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <% if (objects3.size()==1) { %>                                 
                            <fo:table-cell ><fo:block ><%= JSPUtil.printX(nama) %></fo:block></fo:table-cell>
                            <% } else { %>             
                            <fo:table-cell ><fo:block >{L-ENG See Attached-L}{L-INA Terlampir-L} </fo:block></fo:table-cell>
                            <% } %>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-after.optimum="5pt" text-align="justify"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG Birth Date-L}{L-INA Tanggal Lahir-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <% if (objects3.size()==1) { %>                                 
                            <fo:table-cell ><fo:block ><%=DateUtil.getDateStr(tglLahir,"d ^^ yyyy")%></fo:block></fo:table-cell>
                            <% } else { %>             
                            <fo:table-cell ><fo:block >{L-ENG See Attached-L}{L-INA Terlampir-L} </fo:block></fo:table-cell>
                            <% } %>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-after.optimum="5pt" text-align="justify"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG Age-L}{L-INA Usia-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <% if (objects3.size()==1) { %>                                 
                            <fo:table-cell ><fo:block ><%= JSPUtil.printX(usia) %></fo:block></fo:table-cell>
                            <% } else { %>             
                            <fo:table-cell ><fo:block >{L-ENG See Attached-L}{L-INA Terlampir-L} </fo:block></fo:table-cell>
                            <% } %>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-after.optimum="5pt" text-align="justify"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG Liquidity-L}{L-INA Pencairan-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <% if (objects3.size()==1) { %>                                 
                            <fo:table-cell ><fo:block ><%=DateUtil.getDateStr(tglMulaiKreasi,"d ^^ yyyy")%> s/d <%=DateUtil.getDateStr(tglAkhirKreasi,"d ^^ yyyy")%></fo:block></fo:table-cell>
                            <% } else { %>             
                            <fo:table-cell ><fo:block >{L-ENG See Attached-L}{L-INA Terlampir-L} </fo:block></fo:table-cell>
                            <% } %>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-after.optimum="5pt" text-align="justify"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Insured Amount-L}{L-INA Nilai Pertanggungan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell> 
                            <% if (objects3.size()==1) { %>                                 
                            <fo:table-cell ><fo:block ><%= JSPUtil.printX(pol.getStCurrencyCode()) %> <%=JSPUtil.printX(insured,0)%></fo:block></fo:table-cell>
                            <% } else { %>             
                            <fo:table-cell ><fo:block >{L-ENG <%= JSPUtil.printX(pol.getStCurrencyCode()) %> <%= JSPUtil.printX(pol.getDbInsuredAmount(),2) %> (See Attached) -L}{L-INA <%= JSPUtil.printX(pol.getStCurrencyCode()) %> <%= JSPUtil.printX(pol.getDbInsuredAmount(),2) %> (Terlampir)-L} </fo:block></fo:table-cell>
                            <% } %>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-after.optimum="5pt" text-align="justify"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENGType Of Credit-L}{L-INAKondisi Pertanggungan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell> 
                            <% if (objects3.size()==1) { %>                                 
                            <fo:table-cell ><fo:block ><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                            <% } else { %>             
                            <fo:table-cell ><fo:block >{L-ENG See Attached -L}{L-INA Terlampir -L} </fo:block></fo:table-cell>
                            <% } %>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-after.optimum="5pt" text-align="justify"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENGPeriod-L}{L-INAJangka Waktu-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell> 
                            <% if (objects3.size()==1) { %>                                 
                            <fo:table-cell ><fo:block ><%=JSPUtil.printX(lama)%> bulan</fo:block></fo:table-cell>
                            <% } else { %>             
                            <fo:table-cell ><fo:block >{L-ENG See Attached-L}{L-INA Terlampir-L} </fo:block></fo:table-cell>
                            <% } %>
                        </fo:table-row>
                        
                        <% 
                        InsurancePolicyObjectView obj2 = (InsurancePolicyObjectView) objects3.get(0);
                        
                        DTOList cover = obj2.getCoverage();
                        
                        for(int r = 0; r < cover.size(); r++) {
                            
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) cover.get(r);
                            
                            if(!cov.getStInsuranceCoverID().equalsIgnoreCase("78")){
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <% if (objects3.size()==1) { %>   
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(cov.getStInsuranceCoverDesc2())%></fo:block></fo:table-cell>
                            <% } else { %>             
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                            <% } %>
                        </fo:table-row>
                        <% }else{%>       
                        <fo:table-row>
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }}%>
                        
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
            <fo:inline font-weight="bold"><%= JSPUtil.printX(pol.getStCustomerName()) %></fo:inline></fo:block>
            
            <fo:block space-before.optimum="2pt" space-after.optimum="2pt" text-align="justify"></fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align="center">
                QQ. NASABAH DAN / ATAU AHLI WARIS SAH YANG DITUNJUK 
            </fo:block>
            
            <fo:block space-before.optimum="2pt" space-after.optimum="2pt" text-align="justify"></fo:block>
            
            <fo:block font-size="<%=fontsize%>">
            Jangka waktu pertanggungan : <fo:inline font-weight="bold"><%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d ^^ yyyy")%></fo:inline> sampai dengan <fo:inline font-weight="bold"><%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"d ^^ yyyy")%></fo:inline></fo:block>
            
            <fo:block space-before.optimum="2pt" space-after.optimum="2pt" text-align="justify"></fo:block>
            <%--
        <fo:block font-size="<%=fontsize%>" text-align="justify">
          Setelah berakhirnya jangka waktu tersebut, maka pertanggungan ini akan diperpanjang untuk jangka waktu yang sama, kecuali jika salah satu pihak sedikit-sedikitnya tiga bulan (3 bulan) sebelum berakhirnya tiap-tiap tanggal jangka waktu,
          membatalkan pertanggungan ini dengan pemberitahuan tertulis kepada pihak yang bersangkutan.
        </fo:block>
        
        <fo:block space-before.optimum="2pt" space-after.optimum="2pt" text-align="justify"></fo:block>
      
        <fo:block font-size="<%=fontsize%>" text-align="justify">
         Pertanggungan ini harus sudah berakhir pada hari terakhir dari jangka waktu yang bersangkutan jika Tertanggung telah mencapai usia 65 tahun.
        </fo:block>
            --%>  
            <% if (pol.getStCoverTypeCode().equalsIgnoreCase("COINSOUTSELF")) { %>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column />
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell >
                                <fo:list-block>
                                    
                                    <fo:list-item>
                                        <fo:list-item-label end-indent="label-end()">
                                            <fo:block>&#x2022;</fo:block>
                                        </fo:list-item-label>
                                        <fo:list-item-body start-indent="body-start()">
                                            <fo:block font-size="<%=fontsize%>">
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
            
            <% if (isAttached) { %>      
            <fo:block font-size="<%=fontsize%>" text-align="justify" space-before.optimum="2pt" space-after.optimum="2pt">
                Dicatat dan disepakati oleh anggota konsorsium, untuk penerimaan premi didistribusikan oleh Broker dan pembayaran klaim dilakukan oleh masing-masing anggota konsorsium sesuai dengan saham masing-masing anggota kepada Broker.
            </fo:block>
            <% } else { %>
            
            <% } %>
            
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed" space-after.optimum="10pt">
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center" ></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center" ></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center" ></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <% 
                        final DTOList coins = pol.getCoins2();
                        for (int f = 0; f < coins.size(); f++) {
                            InsurancePolicyCoinsView coi = (InsurancePolicyCoinsView) coins.get(f);
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center" ></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block ><%= coi.getStEntityName() %></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end" ><%= JSPUtil.printX(coi.getDbSharePct(),2)%> %</fo:block></fo:table-cell>
                        </fo:table-row> 
                        <% } %>	  
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            
            <% } else { %>
            
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
                                        
                                        if (!cl.isSelected()) continue;
                                    
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
                                    <%} %>
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

                        <% if (pol.getStWarranty()!=null) { %>

                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Warranty/Under Condition-L}{L-INA Syarat Tambahan/Kondisi Lainnya-L} </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block wrap-option="yes-wrap" linefeed-treatment="preserve"
                                                      white-space-treatment="preserve" white-space-collapse="false"><%= JSPUtil.printX(pol.getStWarranty()) %></fo:block></fo:table-cell>
                        </fo:table-row>

                        <% } %> 
                        
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
                            <fo:table-cell><fo:block><%= JSPUtil.printX(pol.getStCurrencyCode()) %></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(pol.getDbPremiTotalAfterDisc(),0) %></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <% if (pol.getDbNDPCost()!=null) { %>         
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG Policy Cost -L}{L-INA Biaya Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%= JSPUtil.printX(pol.getStCurrencyCode()) %></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(pol.getDbNDPCost(),0) %></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <% } %>
                        <% if (pol.getDbNDSFee()!=null) { %>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG Stamp Cost-L}{L-INA Biaya Materai -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%= JSPUtil.printX(pol.getStCurrencyCode()) %></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(pol.getDbNDSFee(),0) %></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <% } %>   
                        
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
                            <fo:table-cell><fo:block><%= JSPUtil.printX(pol.getStCurrencyCode()) %></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end" font-weight="bold"><%= JSPUtil.printX(pol.getDbTotalDue(),0) %></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>   
            
            <% if (usingDigitalSign) { %>
            <fo:block font-size="6pt"
                      space-after.optimum="3pt"
                      text-align="justify" >
                Dokumen ini dicetak secara sistem dan dinyatakan sah.
            </fo:block>
            <% } %>
            
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="3pt"></fo:block>
            
            
            <fo:block font-size="6pt" space-after.optimum="10pt">
                <%if (!tanpaTanggal) {%>
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(), "dd/MM/yyyy HH:mm:ss"))%>
                <% } %>
                <% if (effective) {%>
                PrintStamp:<%=pol.getStPrintCode()%> PrintCode:<%=pol.getStPrintStamp()%> Page:<fo:page-number/> Cetakan : <%=pol.getStPrintCounter()%>
                <% } %>   
                <% if (usingDigitalSign) { %>
                <%if(!isUsingBarcode){%>  [<%=JSPUtil.print(pol.getStSignCode())%>] <%}%>
                <% } %>   
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="90mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) { %>
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L} 
                                <%= JSPUtil.printX(pol.getStCostCenterDesc())%><%if(!tanpaTanggal) { %>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%><%}%></fo:block>
                                <% } else { %>  
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L} 
                                <%= JSPUtil.printX(pol.getCostCenter(pol.getCostCenter(pol.getStCostCenterCode()).getStSubCostCenterCode()).getStDescription())%><%if(!tanpaTanggal){%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%><%}%></fo:block>
                                <% } %>             
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <%if(!isUsingBarcode){%><fo:block text-align="center">{L-ENG Authorized-L}{L-INA Tanda Tangan Yang Berwenang-L}</fo:block><%}%>
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
                        
                        <% if (usingDigitalSign) { %>
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
                                <%if(!isUsingBarcode){%>
                                <%if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) { %>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(<%=pol.getUser(pol.getUserIDSign()).getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <% } else { %>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(<%=pol.getUserApproved().getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <% } %>
                                <% } %>
                                
                                <%if(isUsingBarcode){%>
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
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) { %>
                            <%--<fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=pol.getUserApproved().getStUserName().toUpperCase()%></fo:block></fo:table-cell>--%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%= pol.getUserIDSignName()%></fo:inline></fo:block></fo:table-cell>
                            <% } else { %>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%=JSPUtil.printX(pol.getUserApproved().getStUserName()).toUpperCase()%></fo:inline></fo:block></fo:table-cell>
                            <% } %>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) { %>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center" ><%if(pol.getParaf()!=null){%><fo:leader leader-length="10mm"/><%}%><%=JSPUtil.printX(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%>                                     <%if(pol.getParaf()!=null){%>                                     <fo:external-graphic                                         content-height="scale-to-fit" height="0.3in"  content-width="0.3in" scaling="non-uniform"                                         overflow="hidden" src="url(<%=pol.getUser(pol.getParaf()).getParafFile().getStFilePath()%>)"  />                                     <%}%>                             </fo:block></fo:table-cell> 
                            <% } else { %>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
                            <% } %>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <% } %> 
                        
                    </fo:table-body>
                </fo:table> 
            </fo:block>
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>


