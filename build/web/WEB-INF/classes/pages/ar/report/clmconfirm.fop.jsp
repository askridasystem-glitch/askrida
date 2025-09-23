<%@ page import="com.webfin.ar.model.*,  
com.webfin.ar.forms.FRRPTrptArAPDetailForm,
com.webfin.insurance.model.InsurancePolicyInwardView,
com.crux.ff.model.FlexFieldHeaderView, 
com.crux.ff.model.FlexFieldDetailView, 
com.crux.util.*, 
com.crux.util.Tools, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager,
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 
<% 

final DTOList l = (DTOList)request.getAttribute("RPT");

final FRRPTrptArAPDetailForm form = (FRRPTrptArAPDetailForm)SessionManager.getInstance().getCurrentForm();

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
    
    <!-- defines page layout --> 
    <fo:layout-master-set> 
        
        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="first" 
                               page-height="30cm"
                               page-width="21cm"
                               margin-top="1.5cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            
            <fo:region-body margin-top="0cm" margin-bottom="0cm"/> 
            <fo:region-before extent="0cm"/> 
            <fo:region-after extent="0.5cm"/> 
        </fo:simple-page-master> 
        
    </fo:layout-master-set> 
    <!-- end: defines page layout --> 
    
    <!-- actual layout --> 
    <fo:page-sequence master-reference="first"> 
        
        <fo:flow flow-name="xsl-region-body">               
            
            <!-- usage of page layout --> 
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
            
            <fo:block space-after.optimum="10pt" text-align="justify"> </fo:block>
            
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
                            <fo:table-cell><fo:block>Cab. </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="10pt" line-height="12pt" text-align="justify" space-after.optimum="10pt">
                Dengan hormat,
            </fo:block>
            
            <fo:block font-size="10pt" line-height="12pt" text-align="center" space-after.optimum="10pt">
                <fo:inline font-weight="bold">Perihal : Konfirmasi Klaim </fo:inline>
            </fo:block>
            
            <fo:block font-size="10pt" line-height="12pt" text-align="justify" space-after.optimum="10pt">
                Bersama ini kami sampaikan persetujuan teknis atas klaim tersebut dengan perincian sbb:
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
                        <%--
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
                    <fo:table-cell><fo:block text-align="end"><%= JSPUtil.printX(pol.getDbClaimCustAmount(),2) %></fo:block></fo:table-cell>
                </fo:table-row>
                
                <% } %>       
                --%>
                
                    </fo:table-body>
                </fo:table>
            </fo:block> 
            
            <fo:block space-after.optimum="10pt" text-align="justify"> </fo:block>
            
            <fo:block font-size="10pt" line-height="12pt" text-align="justify" space-after.optimum="10pt">
                Berkenaan dengan persetujuan tersebut, perlu kami tegaskan bahwa apabila dikemudian hari terdapat indikasi bahwa 
                kerugian tidak dijamin oleh kondisi polis dan/atau perjanjian reasuransi dan/atau bertentangan 
                dengan ketentuan hukum yang berlaku, maka kami akan menarik kembali/membatalkan persetujuan tersebut.
            </fo:block>
            
            <fo:block font-size="10pt" line-height="12pt" text-align="justify" space-after.optimum="10pt">
                Demikian kami sampaikan. Atas perhatian dan kerjasamanya, kami ucapkan terima kasih.
            </fo:block>    
            
            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt"></fo:block>
            
            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
            </fo:block>          
            
            <fo:block text-align="start">
                <fo:instream-foreign-object>
                    <barcode:barcode
                        xmlns:barcode="http://barcode4j.krysalis.org/ns"
                        message="<%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(IDRPremi,0)%>" orientation="0">
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