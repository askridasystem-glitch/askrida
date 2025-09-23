<%@ page import="com.webfin.insurance.model.*, 
com.crux.ff.model.FlexFieldHeaderView, 
com.crux.ff.model.FlexFieldDetailView, 
com.crux.util.*, 
com.crux.util.Tools, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager, 
com.crux.common.parameter.Parameter,
com.webfin.insurance.form.ProductionClaimReportForm, 
com.webfin.gl.model.GLCostCenterView,
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 
<% 

final DTOList l = (DTOList)request.getAttribute("RPT");

final ProductionClaimReportForm form = (ProductionClaimReportForm)SessionManager.getInstance().getCurrentForm();

InsurancePolicyView policy = (InsurancePolicyView) l.get(0);

final GLCostCenterView cc_code = policy.getCostCenter(form.getStBranch());

String letter = policy.getStClaimLetter();

String ccy = policy.getStCurrencyCode();

String noUrut = policy.getStClaimLetter();

String itemID = null;
if (policy.getStParentID()!=null)
    itemID = policy.getStParentID();   

%> 

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->

        <fo:simple-page-master master-name="only"
                               page-height="29.5cm"
                               page-width="21cm"
                               margin-top="5cm"
                               margin-bottom="1cm"
                               margin-left="1cm"
                               margin-right="1cm">
            
            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>
        
    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">
        
        <!-- usage of page layout -->
        <!-- HEADER -->

        <fo:flow flow-name="xsl-region-body">
            
            <fo:block font-size="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="90mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block>Nomor : <%= JSPUtil.printX(letter) %></fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block>Jakarta, <%=DateUtil.getDateStr(new Date(),"d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="10pt" line-height="10pt" space-after.optimum="20pt" text-align="justify"> </fo:block>
            
            <!-- GARIS  -->
            <%--  <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="1pt"></fo:block> --%> 

            <!-- defines text title level 1-->
      
            <fo:block font-size="10pt">
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
                            <fo:table-cell><fo:block>Cab. <%= JSPUtil.printX(form.getStBranchDesc()) %></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block><%= JSPUtil.printX(cc_code.getStAddress()) %></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <!-- Normal text -->
            
            <!-- DYNAMIC HEADER WORDING -->

            <fo:block font-size="10pt" line-height="10pt" space-after.optimum="20pt" text-align="justify"> </fo:block>
            
            
            <!-- GARIS  -->
            <%--  <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="1pt"></fo:block> --%> 

            <!-- Normal text -->

            <fo:block font-size="10pt" line-height="12pt" space-after.optimum="10pt">
                Dengan hormat,
            </fo:block>
            
            <fo:block font-size="10pt" line-height="12pt" text-align="center" space-after.optimum="10pt">
                <%	if (policy.getStParentID()!=null) {
    if (itemID.equalsIgnoreCase("54")) { %>
                <fo:inline font-weight="bold">Hal : Konfirmasi Ex-Gratia Klaim <%= JSPUtil.printX(form.getStPolicyTypeDesc()) %></fo:inline>
                <% } else { %>
                <fo:inline font-weight="bold">Hal : Konfirmasi Adjuster-Fee Klaim <%= JSPUtil.printX(form.getStPolicyTypeDesc()) %></fo:inline>
                <% } %>
                <%	} else { %>
                <fo:inline font-weight="bold">Hal : Konfirmasi Teknis Klaim <%= JSPUtil.printX(form.getStPolicyTypeDesc()) %></fo:inline>
                <% } %>
            </fo:block>
            
            <fo:block font-size="10pt" line-height="12pt" space-after.optimum="20pt">
                Bersama ini kami sampaikan persetujuan teknis atas klaim tersebut dengan perincian  sbb:
            </fo:block>
            
            <fo:block font-size="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="center">No.</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">No. LKP</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">No. Polis</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">ID</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Tertanggung</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Klaim</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <% 
                        BigDecimal [] t = new BigDecimal[3];
                        
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            int n=0;
                            t[n] = BDUtil.add(t[n++], pol.getDbClaimAmount());
                            t[n] = BDUtil.add(t[n++], pol.getDbClaimCustAmount());
                            t[n] = BDUtil.add(t[n++], pol.getDbClaimDeductionCustAmount());
                        
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="center" font-size="9pt"><%=String.valueOf(i+1)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-size="9pt"><%= JSPUtil.printX(pol.getStDLANo()) %></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-size="9pt"><%= JSPUtil.printX(pol.getStPolicyNo()) %></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center" font-size="9pt"><%= JSPUtil.printX(pol.getStPolicyID()) %></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block font-size="9pt"><%= JSPUtil.printX(pol.getStReference1()) %></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(pol.getStCurrencyCode()) %></fo:block></fo:table-cell>
                            <%	if (policy.getStParentID()!=null) {
                                if (itemID.equalsIgnoreCase("50")) { %>
                            <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(pol.getDbClaimCustAmount(),2) %></fo:block></fo:table-cell>
                            <% } else { %>
                            <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(pol.getDbClaimAmount(),2) %></fo:block></fo:table-cell>
                            <% } %>
                            <%	} else { %>
                            <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(pol.getDbClaimAmount(),2) %></fo:block></fo:table-cell>
                            <% } %>
                        </fo:table-row>
                        
                        <% } %>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="6"></fo:table-cell>
                            <fo:table-cell >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block ></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block ></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block ></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block ></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end">Jumlah Klaim</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(ccy) %></fo:block></fo:table-cell>
                            <%	if (policy.getStParentID()!=null) {
                            if (itemID.equalsIgnoreCase("50")) { %>
                            <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(t[1],2) %></fo:block></fo:table-cell>
                            <% } else { %>
                            <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(t[0],2) %></fo:block></fo:table-cell>
                            <% } %>
                            <%	} else { %>
                            <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(t[0],2) %></fo:block></fo:table-cell>
                            <% } %>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block> 
            
            
            <!-- ROW -->
            <fo:block font-size="10pt" line-height="10pt" space-before.optimum="20pt" space-after.optimum="10pt" text-align="justify" > </fo:block>
            
            <fo:block font-size="10pt" line-height="12pt" text-align="justify" space-after.optimum="10pt">
                Berkenaan dengan persetujuan tersebut, perlu kami tegaskan bahwa apabila dikemudian hari terdapat indikasi bahwa kerugian tidak dijamin oleh kondisi polis dan bertentangan dengan ketentuan hukum yang berlaku, maka kami akan menarik kembali persetujuan teknis dan penyelesaian.
            </fo:block>
            
            <fo:block font-size="10pt" line-height="12pt" text-align="justify" space-after.optimum="10pt">
                Demikian agar maklum dan terima kasih atas perhatian serta kerjasama saudara.
            </fo:block>
            
            <fo:block font-size="10pt" space-after.optimum="20pt"></fo:block>
            
            <% if (form.getStAuthorized().equalsIgnoreCase("kasie")) { %> 
            
            <fo:block font-size="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="100mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(new Date(),"d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block text-align="center">DIVISI KLAIM,</fo:block>
                            </fo:table-cell>
                        </fo:table-row>           
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block number-column-display="2" space-after.optimum="60pt"></fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        <%       
                        if(SessionManager.getInstance().getSession().getStBranch()!=null){
                        %>  
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("BRANCH_SIGN_"+SessionManager.getInstance().getSession().getStBranch())%></fo:inline></fo:block>
                                <fo:block text-align="center">Pimpinan</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% } else { %>                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline">IMRAN UMAR</fo:inline></fo:block>
                                <fo:block text-align="center">Kasie. Klaim</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% } %>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            <% }%>
            
            <% if (form.getStAuthorized().equalsIgnoreCase("kabag")) {%> 
            
            <fo:block font-size="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="100mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(new Date(),"d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block text-align="center">DIVISI KLAIM,</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block number-column-display="2" space-after.optimum="60pt"></fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        <%       
                        if(SessionManager.getInstance().getSession().getStBranch()!=null){
                        %>  
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= Parameter.readString("BRANCH_SIGN_"+SessionManager.getInstance().getSession().getStBranch())%></fo:inline></fo:block>
                                <fo:block text-align="center">Pimpinan</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% } else { %>                        
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
            
            <% if (form.getStAuthorized().equalsIgnoreCase("kadiv")) {%> 
            
            <fo:block font-size="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="100mm"/>
                    <fo:table-column column-width="80mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(new Date(),"d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center">MENYETUJUI,</fo:block></fo:table-cell>             
                            <fo:table-cell ><fo:block text-align="center">DIVISI KLAIM,</fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block number-column-display="2" space-after.optimum="60pt"></fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline">HENRY ANANDA SIREGAR</fo:inline></fo:block>
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
            
            <% if (form.getStAuthorized().equalsIgnoreCase("dirtek")) {%> 
            
            <fo:block font-size="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="55mm"/>
                    <fo:table-column column-width="55mm"/>
                    <fo:table-column column-width="70mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(new Date(),"d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block text-align="center">MENGETAHUI,</fo:block></fo:table-cell>  
                            <fo:table-cell><fo:block text-align="center">MENYETUJUI,</fo:block></fo:table-cell>           
                            <fo:table-cell><fo:block text-align="center">DIVISI KLAIM,</fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-column-spanned="3"><fo:block space-after.optimum="60pt"></fo:block></fo:table-cell>             
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline">HENRY ANANDA SIREGAR</fo:inline></fo:block>
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
            
            <fo:block text-align="start">
                <fo:instream-foreign-object>
                    <barcode:barcode
                        xmlns:barcode="http://barcode4j.krysalis.org/ns"
                        message="<%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>" orientation="0">
                        <barcode:datamatrix> 
                            <barcode:height>40pt</barcode:height>
                            <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                        </barcode:datamatrix>
                    </barcode:barcode>
                </fo:instream-foreign-object>                                    
            </fo:block>
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>




