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
boolean tanpaTanggal = attached.equalsIgnoreCase("3")?false:true;

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
    
}

%>  
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-height="33cm"
                               page-width="21.5cm"
                               margin-top="2cm"
                               margin-bottom="0.5cm"
                               margin-left="1.5cm"
                               margin-right="1.5cm">
            <fo:region-body margin-top="2cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>
        
    </fo:layout-master-set>
    <!-- end: defines page layout -->
    
    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">\
        
        <fo:flow flow-name="xsl-region-body">
            
            <% if (!effective) {%>
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" text-align="justify" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>
            
            <fo:block font-size="16pt"
                      text-align="center">
                {L-ENG Policy No.-L}{L-INA No. Polis -L} <%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%>
            </fo:block>
            
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.50pt" text-align="justify" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>
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
                {L-INA ASURANSI -L}<%=jenis_pol.replaceAll("PA"," ")%>{L-ENG INSURANCE -L}
            </fo:block>
            
            <!-- Normal text -->

            <!-- defines text title level 1-->


            <!-- Normal text -->
 
            <!-- GARIS  -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-before.optimum="1pt" space-after.optimum="10pt" text-align="justify"></fo:block>
            
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
                Perjanjian Kredit (PK)/<fo:inline font-style="italic">Offering Letter</fo:inline> nomor <fo:inline font-weight="bold"><%=JSPUtil.printX(noKredit)%></fo:inline> tanggal <fo:inline font-weight="bold"><%=DateUtil.getDateStr(pol.getDtPolicyDate(),"dd ^^ yyyy")%></fo:inline> atas Debitur :
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
                    <fo:table-column column-width="20mm"/>
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
                                            <fo:table-column column-width="25mm"/>
                                            <fo:table-column column-width="3mm"/>
                                            <fo:table-column column-width="10mm"/>
                                            <fo:table-column column-width="15mm"/>
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
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block >{L-ENG Clauses Attached-L}{L-INA Klausula Tambahan-L}</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>      
                            <fo:table-cell><fo:block >
                                    <% if (objects.size()==1) {
    
    final DTOList clausules = pol.getClausules();
    for (int ii = 0; ii < clausules.size(); ii++) {
        InsurancePolicyClausulesView cl = (InsurancePolicyClausulesView) clausules.get(ii);
        
        if (!cl.isSelected()) continue;
        
        if (ii>0) out.print(", ");
        out.print(JSPUtil.print(cl.getStDescription().substring(0,11)));
    }
                                    } else { %>
                                    Terlampir
                                    <% } %>
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-after.optimum="5pt" text-align="justify"></fo:block></fo:table-cell>
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
            
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>
            
            <fo:block font-size="6pt"
                      font-family="sans-serif"
                      line-height="10pt"
                      space-after.optimum="10pt"
                      text-align="left" >
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(),"dd/MM/yyyy HH:mm:ss"))%> 
                
                <% if (effective) {%>
                PrintCode:<%=pol.getStPrintCode()%> PrintStamp:<%=pol.getStPrintStamp()%> Page:<fo:page-number/> Terlampir : [<%=JSPUtil.print(request.getAttribute("DOCUMENT_NAME"))%>]
                <% } %>   
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" text-align="justify">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="7mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column />         
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell>
                                <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) { %>
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L} 
                                <%= JSPUtil.printX(pol.getStCostCenterDesc())%><%if(tanpaTanggal){%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%><%}%></fo:block>
                                <% } else { %>  
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L} 
                                Jakarta<%if(tanpaTanggal){%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%><%}%></fo:block>
                                <% } %>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block text-align="center">Tanda Tangan yang Berwenang</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>


