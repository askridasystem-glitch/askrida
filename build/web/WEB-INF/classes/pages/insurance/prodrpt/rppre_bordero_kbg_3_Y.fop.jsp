<%@ page import="com.webfin.insurance.model.*,
com.crux.util.*, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager, 
com.webfin.insurance.form.ProductionReinsuranceReportForm, 
org.joda.time.DateTime,
org.joda.time.Months,
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 

<%
final DTOList l = (DTOList)request.getAttribute("RPT");

final ProductionReinsuranceReportForm form = (ProductionReinsuranceReportForm)SessionManager.getInstance().getCurrentForm();

InsurancePolicyView policy = (InsurancePolicyView) l.get(0);
String bordero = policy.getStCustomerAddress();

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-width="21cm"
                               page-height="30cm"
                               margin-top="4cm"
                               margin-bottom="0.5cm"                                                                                                                                                                           
                               margin-left="2cm"
                               margin-right="2cm">
            <fo:region-body margin-top="1cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="1cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>
        
    </fo:layout-master-set> 
    <!-- end: defines page layout --> 
    
    <!-- actual layout --> 
    <fo:page-sequence id="N2528" master-reference="only">         
        
        <fo:flow flow-name="xsl-region-body">
            
            <fo:block >
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>    
                        
                        <fo:table-row>
                            <fo:table-cell >
                                <fo:block font-size="12pt">  	 
                                    <%=form.getMarketer().getStEntityName().toUpperCase()%>
                                </fo:block>
                            </fo:table-cell >
                            <fo:table-cell ></fo:table-cell >
                            <fo:table-cell >
                                <fo:block font-weight="bold" font-size="12pt">  	 
                                    {L-ENG CREDIT NOTE -L}{L-INA NOTA KREDIT -L}
                                </fo:block>
                            </fo:table-cell >
                            <fo:table-cell ></fo:table-cell >
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell >
                            <fo:table-cell ></fo:table-cell >
                            <fo:table-cell >
                                <fo:block font-weight="bold" font-size="8pt">  	 
                                    Date
                                </fo:block>
                            </fo:table-cell >
                            <fo:table-cell > 
                                <fo:block font-size="8pt">  	 
                                    : {L-ENG <%=DateUtil.getDateStr(new Date(),"MMMM dd yyyy")%>-L}{L-INA <%=DateUtil.getDateStr(new Date(),"dd MMMM yyyy")%>-L}
                                </fo:block>
                            </fo:table-cell >
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>  
            
            <fo:block space-before.optimum="100pt"></fo:block>   
            
            <fo:block font-size="12pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column />
                    <fo:table-body>    
                        
                        <%
                        BigDecimal totalNETTO = null;
                        BigDecimal [] t = new BigDecimal[4];
                        
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            int n = 0;
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiNetto());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDComm1());
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiBase());
                            
                            totalNETTO = BDUtil.sub(pol.getDbPremiNetto(), pol.getDbNDComm1());
                            t[n] = BDUtil.add(t[n++], totalNETTO);
                            
                        }
                        %> 
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="start">{L-ENG BORDERAUX NO. -L}{L-INA NO. BORDERO -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start">: 
                                    <%=bordero%>
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="start">{L-ENG CESSION OF MONTH -L}{L-INA SESI BULAN -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start">: <%= DateUtil.getMonth(form.getPolicyDateTo())%> <%= DateUtil.getYear(form.getPolicyDateTo())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="start">{L-ENG CLASS OF BUSINESS -L}{L-INA JENIS POLIS -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start">: 
                                    <% if (form.getStPolicyTypeID()!=null) { %>
                                    <%=JSPUtil.printX(form.getStPolicyTypeDesc().toUpperCase())%>
                                    <% } else { %>
                                    <%=JSPUtil.printX(form.getStPolicyTypeGroupDesc().toUpperCase())%>
                                    <% } %>
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="start">{L-ENG REMARKS -L}{L-INA KETERANGAN -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start">: WPC 14 days from and of cession of month </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="start">{L-ENG CURRENCY -L}{L-INA KURS -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start">: <%=JSPUtil.printX(policy.getStCurrencyCode().toUpperCase())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>  
            
            <fo:block space-before.optimum="100pt"></fo:block>   
            
            <fo:block font-size="12pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="3mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>    
                        
                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="start">{L-ENG Gross Premium -L}{L-INA Premi Bruto -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start">:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0],2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="start">{L-ENG 30% R/I Comm -L}{L-INA Komisi 30% -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start">:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[1],2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2" ></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="start">{L-ENG Net Premium Due To You -L}{L-INA Premi Netto Untuk Saudara -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="start">:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[3],2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block space-after.optimum="150pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>  
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block text-align="start">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <fo:block text-align="start">{L-ENG REINSURANCE DEPARTMENT -L}{L-INA BAGIAN REASURANSI -L}</fo:block>
                            </fo:table-cell>                            
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>                   
            
            <fo:block text-align="start">
                <fo:instream-foreign-object>
                    <barcode:barcode
                        xmlns:barcode="http://barcode4j.krysalis.org/ns"
                        message="<%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(t[0],2)%>" orientation="0">
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
