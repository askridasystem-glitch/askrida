<%@ page import="com.webfin.insurance.model.*,
com.webfin.entity.model.EntityAddressView,
com.crux.ff.model.FlexFieldHeaderView,
com.crux.ff.model.FlexFieldDetailView,
com.crux.util.*,
com.crux.util.fop.FOPUtil,
com.crux.web.controller.SessionManager, 
com.crux.common.parameter.Parameter,
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
String objectID = null;
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
                            <% if(!effective) { %>
                            <fo:table-cell></fo:table-cell>
                            <% }else{ %>
                            <fo:table-cell><fo:block><%= JSPUtil.printX(pol.getStClaimLetter()) %></fo:block></fo:table-cell>
                            <% } %>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Jakarta, <%=DateUtil.getDateStr(new Date(),"d ^^ yyyy")%></fo:block></fo:table-cell>
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
                    <fo:table-column />
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block>{L-ENG To -L}{L-INA Kepada Yth. -L}</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block><fo:inline font-weight="bold">PT. ASURANSI BANGUN ASKRIDA</fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block><%= pol.getStCostCenterDesc() %></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block><%=pol.getStCostCenterAddress()%></fo:block></fo:table-cell>
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
            
            <fo:block font-size="<%=fontsize%>" line-height="12pt" text-align="center" space-after.optimum="10pt">
                <fo:inline font-weight="bold">Hal : Konfirmasi Teknis Klaim <%= JSPUtil.printX(pol.getStPolicyTypeDesc2()) %></fo:inline>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" line-height="12pt" space-after.optimum="20pt">
                Bersama ini kami sampaikan persetujuan teknis atas klaim tersebut dengan perincian  sbb:
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>
                        
                        <% 
                        final DTOList objects2 = pol.getObjectsClaim();
                        for(int j = 0;j<objects2.size();j++){
                            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects2.get(j);
                            
                            objectName = obj.getStReference1();
                            
                            objectID = obj.getStPolicyObjectID();
                        }
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="center">Nomor Polis</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">ID</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Tertanggung</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Klaim</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="center"><%= pol.getStPolicyNo() %></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%= objectID %></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%= objectName %></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%= pol.getStCurrencyCode() %></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(BDUtil.add(pol.getDbClaimAmount(), pol.getDbClaimAmountEndorse()),2) %></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block> 
            
            
            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>" line-height="10pt" space-before.optimum="20pt" space-after.optimum="10pt" text-align="justify" > </fo:block>
            
            <fo:block font-size="<%=fontsize%>" line-height="12pt" text-align="justify" space-after.optimum="10pt">
                Berkenaan dengan persetujuan tersebut, perlu kami tegaskan bahwa apabila dikemudian hari terdapat indikasi bahwa kerugian tidak dijamin oleh kondisi polis dan bertentangan dengan ketentuan hukum yang berlaku, maka kami akan menarik kembali persetujuan teknis dan penyelesaian.
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" line-height="12pt" text-align="justify" space-after.optimum="10pt">
                Demikian agar maklum dan terima kasih atas perhatian serta kerjasama saudara.
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" space-after.optimum="20pt"></fo:block>
            
            <% if (otorized.equalsIgnoreCase("kasie")) {%> 
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(pol.getDtPLADate(),"d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
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
                                <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(pol.getDtPLADate(),"d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
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
                            <fo:table-cell><fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(pol.getDtDLADate(),"d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">MENYETUJUI,</fo:block></fo:table-cell>             
                            <fo:table-cell ><fo:block text-align="center">BAGIAN KLAIM,</fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block number-column-display="2" space-after.optimum="60pt"></fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline">ANUGRAWATI PAKASI</fo:inline></fo:block>
                                <fo:block text-align="center">Kadiv. Klaim</fo:block>
                            </fo:table-cell>             
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline">JATMIKO</fo:inline></fo:block>
                                <fo:block text-align="center">Kabag. Klaim</fo:block>
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
                            <fo:table-cell><fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(pol.getDtDLADate(),"d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="center">MENGETAHUI,</fo:block></fo:table-cell>  
                            <fo:table-cell><fo:block text-align="center">MENYETUJUI,</fo:block></fo:table-cell>           
                            <fo:table-cell><fo:block text-align="center">BAGIAN KLAIM,</fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-after.optimum="60pt"></fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline">ANUGRAWATI PAKASI</fo:inline></fo:block>
                                <fo:block text-align="center">Kadiv. Klaim</fo:block>
                            </fo:table-cell>    
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline">POLLY POERDANADIREDJA</fo:inline></fo:block>
                                <fo:block text-align="center">Direktur Teknik</fo:block>
                            </fo:table-cell>        
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline">JATMIKO</fo:inline></fo:block>
                                <fo:block text-align="center">Kabag. Klaim</fo:block>
                            </fo:table-cell>             
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <% }%>
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>




