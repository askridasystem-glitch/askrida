<%@ page import="com.webfin.insurance.model.*,
com.webfin.entity.model.EntityAddressView,
com.crux.ff.model.FlexFieldHeaderView,
com.crux.ff.model.FlexFieldDetailView,
com.crux.util.*,
com.crux.util.fop.FOPUtil,
java.util.Date,
com.crux.web.controller.SessionManager, 
com.crux.common.parameter.Parameter,
java.math.BigDecimal,
java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%

final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
final String fontsize = (String) request.getAttribute("FONTSIZE");
final String attached = (String) request.getAttribute("attached");
final String otorized = (String) request.getAttribute("authorized");
boolean isAttached = attached.equalsIgnoreCase("1")?false:true;

boolean effective = pol.isEffective();

String nopol = pol.getStPolicyNo();

String jenis_pol = pol.getStPolicyTypeDesc2().toUpperCase();

String objectName = null;
String rekapCoins = null;
String coinsName = null;
String coinsAddress = null;

final DTOList objects2 = pol.getObjectsClaim();

for(int j = 0;j < objects2.size(); j++){
    InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects2.get(j);
    
    objectName = obj.getStReference1();
    rekapCoins = obj.getStRekapKreasi();
    coinsName = obj.getEntityCoins(obj.getStReference8()).getStEntityName().toUpperCase();
    coinsAddress = obj.getEntityCoins(obj.getStReference8()).getStAddress();
}

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->

        <fo:simple-page-master master-name="only"
                               page-height="30cm"
                               page-width="21cm"
                               margin-top="5cm"
                               margin-bottom="1cm"
                               margin-left="2.5cm"
                               margin-right="2.5cm">
            
            <fo:region-body margin-top="1.5cm" margin-bottom="1.5cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="1.5cm"/>
        </fo:simple-page-master>
        
    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">
        
        <!-- usage of page layout -->
        <!-- HEADER -->

        <fo:flow flow-name="xsl-region-body">
            
            <% if (!effective) {%>
            <fo:block font-size="16pt" font-family="TAHOMA"
                      line-height="16pt" space-after.optimum="10pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="90mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block>Nomor : <%= JSPUtil.printX(rekapCoins) %></fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Jakarta, <%=DateUtil.getDateStr(new Date(),"dd ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" line-height="10pt" space-after.optimum="20pt" text-align="justify"> </fo:block>
            
            <!-- GARIS  -->
            <%--  <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="1pt"></fo:block> --%> 

            <!-- defines text title level 1-->
      
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="100mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block>{L-ENG To -L}{L-INA Kepada Yth. -L}</fo:block>                            
                                <fo:block><fo:inline font-weight="bold"><%= JSPUtil.printX(coinsName) %></fo:inline></fo:block>
                                <fo:block><%= JSPUtil.printX(coinsAddress) %></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" line-height="10pt" space-after.optimum="10pt" text-align="justify"> </fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block>Up. Yth</fo:block></fo:table-cell>
                            <fo:table-cell>         
                                <fo:block><fo:inline font-weight="bold">dr. Yuliana Tauran</fo:inline></fo:block>
                                <fo:block><fo:inline font-weight="bold">dr. Rovie Meilani</fo:inline></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <!-- Normal text -->
            
            <!-- DYNAMIC HEADER WORDING -->

            <fo:block font-size="<%=fontsize%>" line-height="10pt" space-after.optimum="20pt" text-align="justify"> </fo:block>
            
            
            <!-- GARIS  -->
            <%--  <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="1pt"></fo:block> --%> 

            <!-- Normal text -->

            <fo:block font-size="<%=fontsize%>" line-height="12pt" space-after.optimum="10pt">
                Dengan hormat,
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" line-height="12pt" space-after.optimum="10pt">
                Bersama ini kami menyampaikan terima kasih atas konfirmasi persetujuan klaim meninggal sebagai berikut :
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block >Nama Tertanggung</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block ><%=JSPUtil.printX(objectName)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block >No. Polis</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block ><%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block >Nilai Klaim</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block >:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block ><%=JSPUtil.printX(pol.getDbClaimAmount(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block> 
            
            <fo:block font-size="<%=fontsize%>" line-height="12pt" text-align="justify" space-before.optimum="10pt" space-after.optimum="10pt">
                Adapun untuk pembayaran klaim tersebut dapat dikirimkan melalui rekening a/n <fo:inline font-weight="bold">PT. Asuransi Bangun Askrida (Kantor Pusat)</fo:inline> sebagai berikut :
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block ><fo:inline font-weight="bold">Rupiah (IDR)</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block ><fo:inline font-weight="bold">:</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell>
                                <fo:block ><fo:inline font-weight="bold">Bank Mandiri</fo:inline></fo:block>
                                <fo:block ><fo:inline font-weight="bold">Branch Cempaka Mas, Jakarta</fo:inline></fo:block>
                                <fo:block ><fo:inline font-weight="bold">a/c no. 120 00000 51099</fo:inline></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block> 
            
            <fo:block font-size="<%=fontsize%>" line-height="12pt" text-align="justify" space-before.optimum="10pt" space-after.optimum="10pt">
                Demikian kami sampaikan, atas perhatian dan kerjasamanya kami ucapkan terima kasih.
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" space-after.optimum="10pt"></fo:block>
            
            <% if (otorized.equalsIgnoreCase("kasie")) {%> 
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Hormat kami,</fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block text-align="center">BAGIAN KLAIM,</fo:block>
                            </fo:table-cell>
                        </fo:table-row>           
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block number-column-display="2" space-after.optimum="60pt"></fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline">IMRAN UMAR</fo:inline></fo:block>
                                <fo:block text-align="center">Kasie. Klaim</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            <% }%>
            
            <% if (otorized.equalsIgnoreCase("kabag")) {%> 
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Hormat kami,</fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7")||pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) { %>
                                <fo:block text-align="center">BAGIAN BONDING,</fo:block>
                                <% } else { %>
                                <fo:block text-align="center">BAGIAN KLAIM,</fo:block>
                                <% } %>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block number-column-display="2" space-after.optimum="60pt"></fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7")||pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) { %>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">TEPANUS HUTABARAT</fo:inline></fo:block>
                                <fo:block text-align="center">Kabag. Bonding</fo:block> 
                                <% } else { %>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">JATMIKO</fo:inline></fo:block>
                                <fo:block text-align="center">Kabag. Klaim</fo:block> 
                                <% } %>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <% }%>
            
            <% if (otorized.equalsIgnoreCase("kadiv")) {%> 
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Hormat kami,</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">MENYETUJUI,</fo:block></fo:table-cell> 
                            <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7")||pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) { %>
                            <fo:table-cell ><fo:block text-align="center">BAGIAN BONDING,</fo:block></fo:table-cell> 
                            <% } else { %>
                            <fo:table-cell ><fo:block text-align="center">BAGIAN KLAIM,</fo:block></fo:table-cell> 
                            <% } %>        
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block number-column-display="2" space-after.optimum="60pt"></fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell >
                                <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7")||pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) { %>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">BUDI PRANOTO</fo:inline></fo:block>
                                <fo:block text-align="center">Kadiv. UnderWriting dan Reas Facultatif</fo:block>
                                <% } else { %>
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("KADIV_KLAIM")%></fo:inline></fo:block>
                                <fo:block text-align="center">Kadiv. Klaim</fo:block>
                                <% } %>
                            </fo:table-cell>             
                            <fo:table-cell >
                                <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7")||pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) { %>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">TEPANUS HUTABARAT</fo:inline></fo:block>
                                <fo:block text-align="center">Kabag. Bonding</fo:block> 
                                <% } else { %>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">JATMIKO</fo:inline></fo:block>
                                <fo:block text-align="center">Kabag. Klaim</fo:block> 
                                <% } %>
                            </fo:table-cell>             
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <% }%>
            
            <% if (otorized.equalsIgnoreCase("dirtek")) {%> 
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="55mm"/>
                    <fo:table-column column-width="55mm"/>
                    <fo:table-column column-width="70mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Hormat kami,</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="center">MENGETAHUI,</fo:block></fo:table-cell>  
                            <fo:table-cell><fo:block text-align="center">MENYETUJUI,</fo:block></fo:table-cell>           
                            <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7")||pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) { %>
                            <fo:table-cell ><fo:block text-align="center">BAGIAN BONDING,</fo:block></fo:table-cell> 
                            <% } else { %>
                            <fo:table-cell ><fo:block text-align="center">BAGIAN KLAIM,</fo:block></fo:table-cell> 
                            <% } %>                   
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-after.optimum="60pt"></fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell >
                                <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7")||pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) { %>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">BUDI PRANOTO</fo:inline></fo:block>
                                <fo:block text-align="center">Kadiv. UnderWriting dan Reas Facultatif</fo:block>
                                <% } else { %>
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("KADIV_KLAIM")%></fo:inline></fo:block>
                                <fo:block text-align="center">Kadiv. Klaim</fo:block>
                                <% } %>
                            </fo:table-cell>    
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline">RIA GAYATRI</fo:inline></fo:block>
                                <fo:block text-align="center">Direktur Teknik</fo:block>
                            </fo:table-cell>        
                            <fo:table-cell >
                                <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("7")||pol.getStPolicyTypeGroupID().equalsIgnoreCase("8")) { %>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">TEPANUS HUTABARAT</fo:inline></fo:block>
                                <fo:block text-align="center">Kabag. Bonding</fo:block> 
                                <% } else { %>
                                <fo:block text-align="center"><fo:inline text-decoration="underline">JATMIKO</fo:inline></fo:block>
                                <fo:block text-align="center">Kabag. Klaim</fo:block> 
                                <% } %>
                            </fo:table-cell>             
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <% }%>
            
            
            <fo:block font-size="8pt" space-before.optimum="40pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
            </fo:block>   
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>




