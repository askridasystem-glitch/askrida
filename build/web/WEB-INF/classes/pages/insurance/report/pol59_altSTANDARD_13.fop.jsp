<%@ page import="com.webfin.insurance.model.*,
com.crux.ff.model.FlexFieldHeaderView,
com.crux.ff.model.FlexFieldDetailView,
com.crux.util.*, 
java.math.BigDecimal, 
com.crux.util.Tools,
java.util.Date,
com.crux.web.controller.SessionManager, 
com.crux.common.parameter.Parameter,
com.crux.util.fop.FOPUtil"%><?xml version="1.0" encoding="utf-8"?>
<%

final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
final String fontsize = (String) request.getAttribute("FONTSIZE");
final String attached = (String) request.getAttribute("attached");
boolean terlampir = attached.equalsIgnoreCase("2")?false:true;
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
String alamat = "";
String noKredit = "";
Date tglLahir=null;
String noKTP = "";
String kerja = "";
Date tglPK=null;

String gl_code = pol.getEntity().getStGLCode();

//String desc = pol.getStKreasiTypeDesc().replaceAll(pol.getStKreasiTypeDesc().substring(0,4)," ");

final DTOList objects = pol.getObjects();
for(int j = 0;j<objects.size();j++){
    InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(j);
    
    nama = obj.getStReference1();
    
    alamat = obj.getStReference6();
    
    noKredit = obj.getStReference4();
    
    noKTP = obj.getStReference3();
    
    kerja = obj.getStReference7Desc();
    
    tglLahir = obj.getDtReference1();
    
    tglPK = obj.getDtReference4();
    
}

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
                               margin-left="2cm"
                               margin-right="2cm">
            
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
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" text-align="justify" space-after.optimum="5pt"></fo:block>
            
            <fo:block font-size="16pt"
                      text-align="center">
                {L-ENG Policy No.-L}{L-INA No. Polis -L} <%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%>
            </fo:block>
            
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" text-align="justify" space-before.optimum="5pt" space-after.optimum="10pt"></fo:block>
            <% }%>
            
            <% if (isPreview) { %>
            <fo:block font-size="20pt" space-after.optimum="10pt" 
                      line-height="16pt"
                      color="blue"
                      text-align="center"
                      padding-top="10pt">
                PREVIEW MODE
            </fo:block>
            <% }%>            
            
            <% if (!effective) {%>
            <fo:block font-size="16pt" space-after.optimum="10pt" 
                      line-height="16pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>
     
            <!-- defines text title level 1-->

            <fo:block font-size="16pt" font-family="TAHOMA"
                      line-height="16pt" space-after.optimum="10pt"
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
                {L-INA ASURANSI KREDIT -L}{L-ENG CREDIT INSURANCE -L}
            </fo:block>
            
            <!-- GARIS  -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-after.optimum="10pt" text-align="justify"></fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align="center">
                {L-ENG Policy Number :  -L}{L-INA Nomor Polis :   -L}<%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%>
            </fo:block>
            
            <fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align="justify">
                PT. Asuransi Bangun Askrida berkedudukan di Pusat Niaga Cempaka Mas Blok M1/36 Jl. Letjen Soeprapto Jakarta 10640 yang selanjutnya disebut sebagai <fo:inline font-weight="bold">PENANGGUNG</fo:inline>, 
                atas dasar pembayaran premi dan keterangan-keterangan tertulis yang diberikan dengan ini menanggung :
            </fo:block>
            
            <fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align="justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="3mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG Name -L}{L-INA Nama -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-weight="bold"><%= JSPUtil.xmlEscape(pol.getStCustomerName()) %></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG Address -L}{L-INA Alamat -L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-weight="bold"><%= JSPUtil.xmlEscape(pol.getStCustomerAddress()) %></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align="justify">
                sebagai <fo:inline font-weight="bold">TERTANGGUNG</fo:inline>
            </fo:block>
            
            <fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align="justify">
                sejumlah uang sebesar-besarnya <fo:inline font-weight="bold"><%=pol.getStCurrencyCode()%> <%=JSPUtil.printX(pol.getDbInsuredAmount(),0)%> ( <%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbInsuredAmount(),0), pol.getStCurrencyCode())%>)</fo:inline> dalam hal terjadi wanprestasi atas 
                Perjanjian Kredit (PK)/<fo:inline font-style="italic">Offering Letter</fo:inline> nomor (<fo:inline font-weight="bold"><%=JSPUtil.printX(noKredit)%></fo:inline>) tanggal (<fo:inline font-weight="bold"><%=DateUtil.getDateStr(tglPK,"dd ^^ yyyy")%></fo:inline>) atas Debitur :
            </fo:block>
            
            <fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align="justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="3mm"/>
                    <fo:table-column />
                    <fo:table-body>          
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG Member-L}{L-INA Peserta-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <% if (objects.size()==1) { %>                                 
                            <fo:table-cell ><fo:block ><%= JSPUtil.printX(nama) %></fo:block></fo:table-cell>
                            <% } else { %>             
                            <fo:table-cell ><fo:block >{L-ENG See Attached-L}{L-INA Terlampir-L} </fo:block></fo:table-cell>
                            <% } %>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG Address-L}{L-INA Alamat-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <% if (objects.size()==1) { %>                                 
                            <fo:table-cell ><fo:block ><%=JSPUtil.printX(alamat)%></fo:block></fo:table-cell>
                            <% } else { %>             
                            <fo:table-cell ><fo:block >{L-ENG See Attached-L}{L-INA Terlampir-L} </fo:block></fo:table-cell>
                            <% } %>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG Birth of Date-L}{L-INA Tanggal Lahir-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <% if (objects.size()==1) { %>                                 
                            <fo:table-cell ><fo:block ><%=JSPUtil.printX(tglLahir)%></fo:block></fo:table-cell>
                            <% } else { %>             
                            <fo:table-cell ><fo:block >{L-ENG See Attached-L}{L-INA Terlampir-L} </fo:block></fo:table-cell>
                            <% } %>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG ID No.-L}{L-INA No. KTP-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <% if (objects.size()==1) { %>                                 
                            <fo:table-cell ><fo:block ><%=JSPUtil.printX(noKTP)%></fo:block></fo:table-cell>
                            <% } else { %>             
                            <fo:table-cell ><fo:block >{L-ENG See Attached-L}{L-INA Terlampir-L} </fo:block></fo:table-cell>
                            <% } %>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG Work-L}{L-INA Pekerjaan-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <% if (objects.size()==1) { %>                                 
                            <fo:table-cell ><fo:block ><%=JSPUtil.printX(kerja)%></fo:block></fo:table-cell>
                            <% } else { %>             
                            <fo:table-cell ><fo:block >{L-ENG See Attached-L}{L-INA Terlampir-L} </fo:block></fo:table-cell>
                            <% } %>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align="justify">
                Yang selanjutnya disebut sebagai <fo:inline font-weight="bold">PESERTA</fo:inline>
            </fo:block>
            
            <fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align="justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="3mm"/>
                    <fo:table-column />
                    <fo:table-body>       
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG Period-L}{L-INA Jangka Waktu Polis-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>      
                            <fo:table-cell ><fo:block ><%=DateUtil.getDateStr(pol.getDtPeriodStart(),"dd ^^ yyyy")%> s/d <%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"dd ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align="justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="3mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column />
                    <fo:table-body>     
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG Amount-L}{L-INA Nilai Pinjaman-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>      
                            <fo:table-cell><fo:block ><%= JSPUtil.printX(pol.getStCurrencyCode()) %></fo:block></fo:table-cell>      
                            <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(pol.getDbInsuredAmount(),0) %></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block ></fo:block></fo:table-cell>      
                        </fo:table-row>
                        <%--
                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG Premium-L}{L-INA Premi-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>      
                            <fo:table-cell><fo:block ><%= JSPUtil.printX(pol.getStCurrencyCode()) %></fo:block></fo:table-cell>      
                            <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(pol.getDbPremiTotal(),0) %></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block ></fo:block></fo:table-cell>      
                        </fo:table-row>
                        --%>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG Premium Calculate -L}{L-INA Perhitungan Premi-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>      
                            <fo:table-cell ><fo:block number-column-spanned="3">
                                    <fo:block font-size="<%=fontsize%>" text-align="justify">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="35mm"/>
                                            <fo:table-column column-width="3mm"/>
                                            <fo:table-column column-width="10mm"/>
                                            <fo:table-column column-width="25mm"/>
                                            <fo:table-body>   
                                                
                                                <fo:table-row>
                                                    <fo:table-cell><fo:block >- {L-ENG Premium-L}{L-INA Premi-L}</fo:block></fo:table-cell>
                                                    <fo:table-cell><fo:block >:</fo:block></fo:table-cell>      
                                                    <fo:table-cell><fo:block ><%= JSPUtil.printX(pol.getStCurrencyCode()) %></fo:block></fo:table-cell>      
                                                    <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(pol.getDbPremiTotal(),0) %></fo:block></fo:table-cell>
                                                </fo:table-row>
                                                
                                                <fo:table-row>
                                                    <fo:table-cell><fo:block >- {L-ENG Policy Cost-L}{L-INA Biaya Polis-L}</fo:block></fo:table-cell>
                                                    <fo:table-cell><fo:block >:</fo:block></fo:table-cell>      
                                                    <fo:table-cell><fo:block ><%= JSPUtil.printX(pol.getStCurrencyCode()) %></fo:block></fo:table-cell>      
                                                    <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(pol.getDbNDPCost(),0) %></fo:block></fo:table-cell>
                                                </fo:table-row>
                                                
                                                <fo:table-row>
                                                    <fo:table-cell><fo:block >- {L-ENG Stamp Fee-L}{L-INA Biaya Materai-L}</fo:block></fo:table-cell>
                                                    <fo:table-cell><fo:block >:</fo:block></fo:table-cell>      
                                                    <fo:table-cell><fo:block ><%= JSPUtil.printX(pol.getStCurrencyCode()) %></fo:block></fo:table-cell>      
                                                    <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(pol.getDbNDSFee(),0) %></fo:block></fo:table-cell>
                                                </fo:table-row>
                                                
                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                                                    <fo:table-cell >
                                                        <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>
                                                    </fo:table-cell>
                                                    <fo:table-cell >
                                                        <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>
                                                    </fo:table-cell>
                                                </fo:table-row>
                                                
                                                <fo:table-row>
                                                    <fo:table-cell><fo:block >- {L-ENG Total-L}{L-INA Total-L}</fo:block></fo:table-cell>
                                                    <fo:table-cell><fo:block >:</fo:block></fo:table-cell>      
                                                    <fo:table-cell><fo:block ><%= JSPUtil.printX(pol.getStCurrencyCode()) %></fo:block></fo:table-cell>      
                                                    <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(BDUtil.add(pol.getDbPremiTotal(), BDUtil.add(pol.getDbNDPCost(), pol.getDbNDSFee())),0) %></fo:block></fo:table-cell>
                                                </fo:table-row>
                                                
                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-column-spanned="5"><fo:block space-after.optimum="5pt" text-align="justify"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align="justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="3mm"/>
                    <fo:table-column />
                    <fo:table-body>  
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG Ruang Lingkup Jaminan-L}{L-INA Ruang Lingkup Jaminan-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>      
                            <fo:table-cell><fo:block text-align="justify">Menjamin resiko tidak kembalinya dana dari Peserta atau kredit yang disalurkan
                            oleh Tertanggung menjadi macet.</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-after.optimum="5pt" text-align="justify"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="3mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        <% if (terlampir) { %>
                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG Clauses Attached-L}{L-INA Klausula Tambahan-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>      
                            <fo:table-cell><fo:block >
                                    <% //if (objects.size()==1) {
                                    
                                    final DTOList clausules = pol.getReportClausules();
                                    for (int iii = 0; iii < clausules.size(); iii++) {
                                        InsurancePolicyClausulesView cl = (InsurancePolicyClausulesView) clausules.get(iii);
                                        
                                        if (!cl.isSelected()) continue;
                                        
                                         if (iii>0) out.print(" - ");
                                        String clau[] = cl.getStDescription().split("-");
                                        out.print(JSPUtil.print(clau[0]));
                                    } %>
                                    <%--<% } else { %>
                                    Terlampir
                                    <% } %>--%>
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        <% } %>
                        
                        <% if (!terlampir) { %>
                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG Clauses Attached-L}{L-INA Klausula Tambahan-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>  
                            <fo:table-cell><fo:block >Terlampir</fo:block></fo:table-cell>
                        </fo:table-row>
                        <% } %>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>                      
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="3mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        <% if (pol.getStWarranty()!=null) { %>          
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Warranty/Under Condition-L}{L-INA Syarat Tambahan/Kondisi Lainnya-L} </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block wrap-option="yes-wrap" linefeed-treatment="preserve"
                                                      white-space-treatment="preserve" white-space-collapse="false"><%= JSPUtil.printX(pol.getStWarranty()) %></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <% } %> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3"><fo:block space-after.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align="justify">
                <fo:table table-layout="fixed">
                    <fo:table-column />
                    <fo:table-body>  
                        
                        <fo:table-row>
                            <fo:table-cell number-column-spanned="5"><fo:block text-align="justify">Pertanggungan ini tunduk pada Syarat-syarat Umum Polis Asuransi Kredit dan ketentuan-ketentuan lain
                                    yang tercantum di dalam atau terlekat pada Polis ini yang dibuat atas dasar Surat Permintaan Asuransi yang diberikan oleh Tertanggung dan merupakan bagian yang tidak terpisahkan
                            dari perjanjian Asuransi.</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-column-spanned="5"><fo:block space-after.optimum="5pt" text-align="justify"></fo:block></fo:table-cell>
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
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(),"dd/MM/yyyy HH:mm:ss"))%> 
                
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
                                Jakarta<%if(!tanpaTanggal){%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%><%}%></fo:block>
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


