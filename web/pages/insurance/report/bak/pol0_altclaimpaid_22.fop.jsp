<%@ page import="com.webfin.insurance.model.*,
com.crux.ff.model.FlexFieldHeaderView,
com.crux.ff.model.FlexFieldDetailView,
com.crux.util.*,
java.util.Date,
com.crux.web.controller.SessionManager, 
com.crux.common.parameter.Parameter,
com.crux.util.fop.FOPUtil,
java.math.BigDecimal"%><?xml version="1.0" encoding="utf-8"?>
<%

final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
final String fontsize = (String) request.getAttribute("FONTSIZE");
final String otorized = (String) request.getAttribute("authorized");
//  boolean isOtorized = otorized.equalsIgnoreCase("kasie");
String objectName = null;
String objectID = null;
BigDecimal amount = null;

boolean effective = pol.isEffective();
boolean itemID = false;

//if (true) throw new NullPointerException();

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->

        <fo:simple-page-master master-name="only"
                               page-height="30cm"
                               page-width="21cm"
                               margin-top="5.5cm"
                               margin-bottom="0.5cm"
                               margin-left="2.5cm"
                               margin-right="2.5cm">
            
            <fo:region-body margin-top="1cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="1.5cm"/>
        </fo:simple-page-master>
        
    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">
        
        <!-- usage of page layout -->
        <!-- HEADER -->

        <fo:static-content flow-name="xsl-region-before">
            <fo:block text-align="end"
                      font-size="6pt"
                      font-family="TAHOMA"
                      line-height="12pt" >
                Claim Document PrintCode:<%=pol.getStPrintCode()%> Page:<fo:page-number/>
            </fo:block>
        </fo:static-content>
        
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
            
            <!-- GARIS  -->
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="10pt"></fo:block>
            
            <!-- defines text title level 1-->

            <fo:block font-size="16pt" font-family="TAHOMA" line-height="16pt" color="black" text-align="center" space-after.optimum="10pt">
                {L-INA Surat Permintaan Pembayaran-L}{L-ENG Payment Request-L}
            </fo:block>
            
            
            <!-- Normal text -->
            
            <!-- DYNAMIC HEADER WORDING -->


            <!-- GARIS -->
            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
            
            <!-- Normal text -->

            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Date-L}{L-INA Tanggal -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%=DateUtil.getDateStr(pol.getDtDLADate(),"d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Number-L}{L-INA Nomor-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%=pol.getStDLANo()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG To-L}{L-INA Kepada -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG FINANCE DEPARTMENT-L}{L-INA BAGIAN KEUANGAN -L}</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <!-- GARIS  -->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.50pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="162mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">{L-ENG We, herewith request to pay -L}{L-INA Dengan ini kami minta agar Saudara membayarkan -L}</fo:block></fo:table-cell>
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
                        <%  
                        final DTOList objects2 = pol.getObjectsClaim();
                        for(int j = 0;j<objects2.size();j++){
                            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects2.get(j);
                            
                            if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("10"))
                                objectName = pol.getStClaimClientName();
                            else
                                objectName = obj.getStReference1();
                            
                            objectID = obj.getStPolicyObjectID();
                        }
                        
                        final DTOList items = pol.getClaimItems();
                        for(int j = 0;j<items.size();j++){
                            InsurancePolicyItemsView item = (InsurancePolicyItemsView) items.get(j);
                            
                            if (item.isAFee())
                                amount = pol.getDbClaimCustAmount();
                            else
                                amount = pol.getDbClaimAmount();
                        }
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG To-L}{L-INA Kepada-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%= JSPUtil.printX(pol.getStClaimPersonName()) %></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Amount of-L}{L-INA Sebesar-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%= JSPUtil.printX(pol.getStCurrencyCode()) %>. <%= JSPUtil.printX(amount,2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Say :-L}{L-INA Terbilang :-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%=NumberSpell.readNumber(JSPUtil.printX(amount,2), pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Remark-L}{L-INA Keterangan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">Pembayaran Klaim Asuransi a/n <%=objectName%> </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <% if (pol.getStPolicyTypeGroupID().equalsIgnoreCase("10")) { %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">No. Rekening : <%=pol.getStClaimAccountNo()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% } %>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt">Tanggal Kejadian : <%=JSPUtil.printX(pol.getDtClaimDate())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Policy No.-L}{L-INA No. Polis-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="10pt"><%= JSPUtil.printX(pol.getStPolicyNo())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            
            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>" font-family="TAHOMA" line-height="10pt" space-after.optimum="2pt" text-align="justify" > </fo:block>
            
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-body>
                                            <%
                                            
                                            final DTOList details = pol.getDetails();
                                            
                                            %>
                                            
                                            
                                            <%
                                            
                                            for (int i = 0; i < details.size(); i++) {
                                                InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);
                                                if (!item.isDiscount()) continue;
                                                
                                                String desc = item.getStDescription2();
                                                
                                                if (item.isEntryByRate()) desc+=JSPUtil.printX(item.getDbRate()+" %");
                                            
                                            %>
                                            <% } %>
                                            
                                            
                                            
                                            <%
                                            
                                            for (int i = 0; i < details.size(); i++) {
                                                InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(i);
                                                
                                                if (!item.isFee()) continue;
                                            %>
                                            <% } %>
                                            
                                            
                                        </fo:table-body>
                                    </fo:table>
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            
            <fo:block font-size="<%=fontsize%>" space-after.optimum="15pt"></fo:block>
            
            <% if (otorized.equalsIgnoreCase("kasie")) {%> 
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>
                        
                        <%       
                        if (SessionManager.getInstance().getSession().getStBranch()!=null){
                        %>     
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><%= JSPUtil.printX(pol.getStCostCenterDesc())%>, <%=DateUtil.getDateStr(pol.getDtDLADate(),"d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                            </fo:table-cell>
                        </fo:table-row>       
                        
                        <%       
                        } else { 
                        %>     
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(pol.getDtDLADate(),"d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block text-align="center">BAGIAN KLAIM</fo:block>
                            </fo:table-cell>
                        </fo:table-row>           
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block number-column-display="2" space-after.optimum="60pt"></fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline">NOVIA</fo:inline></fo:block>
                                <fo:block text-align="center">Kasie. Klaim</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <% } %>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            <% } %>
            
            <% if (otorized.equalsIgnoreCase("kabag")) {%> 
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>
                        
                        <%       
                        if (SessionManager.getInstance().getSession().getStBranch()!=null){
                        %>     
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><%= JSPUtil.printX(pol.getStCostCenterDesc())%>, <%=DateUtil.getDateStr(pol.getDtDLADate(),"d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                            </fo:table-cell>
                        </fo:table-row>    
                        
                        <%       
                        } else { 
                        %>     
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(pol.getDtDLADate(),"d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block text-align="center">BAGIAN KLAIM</fo:block>
                            </fo:table-cell>
                        </fo:table-row>           
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block number-column-display="2" space-after.optimum="60pt"></fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline">JATMIKO</fo:inline></fo:block>
                                <fo:block text-align="center">Kabag. Klaim</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <% } %>
                        
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
                            <fo:table-cell><fo:block text-align="center">JAKARTA, <%=DateUtil.getDateStr(pol.getDtDLADate(),"d ^^ yyyy")%></fo:block></fo:table-cell>
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
                                <fo:block text-align="center"><fo:inline text-decoration="underline">HENRY A. SIREGAR</fo:inline></fo:block>
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
                            <fo:table-cell><fo:block text-align="center">JAKARTA, <%=DateUtil.getDateStr(pol.getDtDLADate(),"d ^^ yyyy")%></fo:block></fo:table-cell>
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
                                <fo:block text-align="center"><fo:inline text-decoration="underline">HENRY A. SIREGAR</fo:inline></fo:block>
                                <fo:block text-align="center">Kadiv. Klaim</fo:block>
                            </fo:table-cell>    
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline">RIA GAYATRI</fo:inline></fo:block>
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
            
            
            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
            </fo:block>   
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>



