<%@ page import="com.webfin.insurance.model.*, 
com.crux.ff.model.FlexFieldHeaderView, 
com.crux.ff.model.FlexFieldDetailView, 
com.crux.util.*, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal,
java.util.Date, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager, 
com.webfin.insurance.form.ProductionFinanceReportForm, 
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 
<% 

final DTOList l = (DTOList)request.getAttribute("RPT");

final ProductionFinanceReportForm form = (ProductionFinanceReportForm)SessionManager.getInstance().getCurrentForm();

BigDecimal TotalComm = new BigDecimal(0);
BigDecimal TotalTax = new BigDecimal(0);
BigDecimal TotalPremiBruto = new BigDecimal(0);
BigDecimal TotalPremiNetto = new BigDecimal(0);

String cabang = null;

if (SessionManager.getInstance().getSession().getStBranch()!=null) {
    cabang = form.getStBranchName();
} else {
    cabang = form.getStBranchDesc();
}

String region = null;

if (SessionManager.getInstance().getSession().getStRegion()!=null) {
    region = form.getStRegionName();
} else {
    region = form.getStRegionDesc();
}

boolean tanpaKomisi = true;

if(form.getStCustCategory1().equalsIgnoreCase("2") || form.getStCustCategory1().equalsIgnoreCase("3") || form.getStCustCategory1().equalsIgnoreCase("4"))
    tanpaKomisi = false;

String width = tanpaKomisi?"35cm":"28cm";

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
    
    <!-- defines page layout --> 
    <fo:layout-master-set> 
        
        <!-- layout for the first page -->
        <fo:simple-page-master master-name="first"
                               page-height="21.5cm"
                               page-width="<%=width%>"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            
            <fo:region-body margin-top="0cm" margin-bottom="0cm"/> 
            <fo:region-before extent="0.5cm"/> 
            <fo:region-after extent="0.5cm"/> 
        </fo:simple-page-master> 
        
    </fo:layout-master-set> 
    <!-- end: defines page layout --> 
 
    <!-- actual layout --> 
    <fo:page-sequence master-reference="first">  
        
        <!-- usage of page layout --> 
        <!-- header --> 
        <fo:static-content flow-name="xsl-region-before"> 
            
            <fo:block-container height="1cm" width="5cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="message"
                                        retrieve-boundary="page"
                                        retrieve-position="first-starting-within-page"/>
                </fo:block>
            </fo:block-container>
            <fo:block-container height="1cm" width="5cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="term"
                                        retrieve-boundary="page"
                                        retrieve-position="last-ending-within-page"/>
                </fo:block>
            </fo:block-container>
        </fo:static-content>
        
        <fo:static-content flow-name="xsl-region-after"> 
            <fo:block text-align="end"
                      font-size="8pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>
        
        <fo:flow flow-name="xsl-region-body"> 
            
            <!-- defines text title level 1--> 
  
            <!-- GARIS  -->  
       
            <fo:block font-size="8pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="25mm"/>

                    <%if(tanpaKomisi){%>
                        <fo:table-column column-width="25mm"/> <%--komisi --%>
                    <%}%>

                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="25mm"/>
                    <%if(tanpaKomisi){%>
                        <fo:table-column column-width="25mm"/>
                        <fo:table-column column-width="25mm"/>
                    <%}%>
                    <fo:table-header> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block font-family="serif" font-weight="bold" font-size="20pt" text-align="center">             
                                    LAPORAN OUTSTANDING PREMI   
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block font-family="serif" font-weight="bold" font-size="16pt" text-align="center">
                                    <% if (form.getPolicyDateFrom()!=null || form.getPolicyDateTo()!=null) {%> 
                                    Tanggal Polis : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block font-family="serif" font-weight="bold" font-size="16pt" text-align="center">
                                    <% if (form.getPerDateFrom()!=null) {%> 
                                    Per Tanggal : <%=JSPUtil.printX(form.getPerDateFrom())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <% if (cabang!=null) { %>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block font-weight="bold">
                                    Cabang : <%=JSPUtil.printX(cabang)%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% } %>
                        
                        <% if (form.getStRegion()!=null) { %>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block font-weight="bold">
                                    Outlet : <%=JSPUtil.printX(region)%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% } %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block font-weight="bold">
                                    <% if (form.getStPolicyTypeDesc()!=null) {%> 
                                    Jenis Polis : <%=JSPUtil.printX(form.getStPolicyTypeDesc())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block font-weight="bold">
                                    <% if (form.getStFltCoverTypeDesc()!=null) {%> 
                                    Jenis Penutupan  : <%=JSPUtil.printX(form.getStFltCoverTypeDesc())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block font-weight="bold">
                                    <% if (form.getStEntityName()!=null) {%> 
                                    Customer Name :<%=JSPUtil.printX(form.getStEntityName())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13">
                                <fo:block font-weight="bold">
                                    <% if (form.getStMarketerName()!=null) {%> 
                                    Marketer Name :<%=JSPUtil.printX(form.getStMarketerName())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="13" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Policy Date-L}{L-INA Tgl. Polis-L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG No. Policy.-L}{L-INA No. Polis -L}</fo:block></fo:table-cell> 
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Kodeko-L}{L-INA Kodeko -L}</fo:block></fo:table-cell>   
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Name of Insured -L}{L-INA Nama Tertanggung -L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Gross Balance -L}{L-INA Premi Bruto -L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Fee Base -L}{L-INA Fee Base -L}</fo:block></fo:table-cell>  
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Discount -L}{L-INA Discount -L}</fo:block></fo:table-cell>
                            <%if(tanpaKomisi){%>
                                <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Comm -L}{L-INA Komisi -L}</fo:block></fo:table-cell>
                            <%} %>
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Bfee -L}{L-INA Bfee -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Hfee -L}{L-INA Hfee -L}</fo:block></fo:table-cell>  
                            <%if(tanpaKomisi){%>
                                <fo:table-cell border-left-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Tax -L}{L-INA Pajak -L}</fo:block></fo:table-cell>
                                <fo:table-cell border-left-style="solid" border-right-style="solid" padding="2pt" ><fo:block text-align="center">{L-ENG Nett Balance-L}{L-INA Premi Netto-L}</fo:block></fo:table-cell>
                            <%} %>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="13">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>    
                        
                    </fo:table-header>             
                    <fo:table-body>      
                        <!-- GARIS DALAM KOLOM -->  
                        <%   
                        BigDecimal [] t = new BigDecimal[10];
                        
                        int no = 0;
                        int counter = 45;
                        BigDecimal subTotalPremiBruto = null;
                        BigDecimal subTotalFeeBase = null;
                        BigDecimal subTotalDisc = null;
                        BigDecimal subTotalComm = null;
                        BigDecimal subTotalBfee = null;
                        BigDecimal subTotalHfee = null;
                        BigDecimal subTotalTax = null;
                        BigDecimal subTotalPremiNetto = null;
                        String kodeko = null;
                        
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            //if (pol.getStMasterPolicyID().equalsIgnoreCase("10")&&pol.getStPolicyTypeID().equalsIgnoreCase("21")) continue;
                            
                            int n=0;
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiTotal());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDFeeBase1());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDDisc1());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDComm1());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDBrok1());
                            t[n] = BDUtil.add(t[n++], pol.getDbNDHFee());
                            
                            TotalTax = BDUtil.add(pol.getDbNDTaxComm1(), pol.getDbNDTaxBrok1());
                            TotalTax = BDUtil.add(TotalTax, pol.getDbNDTaxHFee());
                            t[n] = BDUtil.add(t[n++], TotalTax); //total tagihan komisi
                            
                            TotalComm = BDUtil.add(pol.getDbNDComm1(), pol.getDbNDBrok1());
                            TotalComm = BDUtil.add(TotalComm, pol.getDbNDHFee());
                            TotalComm = BDUtil.add(TotalComm, TotalTax);
                            t[n] = BDUtil.add(t[n++], TotalComm); //total tagihan komisi
                            
                            TotalPremiBruto = BDUtil.add(pol.getDbNDPCost(), pol.getDbNDSFee());
                            TotalPremiBruto = BDUtil.add(TotalPremiBruto, pol.getDbPremiTotal());
                            t[n] = BDUtil.add(t[n++], TotalPremiBruto); //total tagihan komisi
                            
                            TotalPremiNetto = BDUtil.sub(TotalPremiBruto, TotalComm);
                            TotalPremiNetto = BDUtil.add(TotalPremiNetto, TotalTax);
                            TotalPremiNetto = BDUtil.sub(TotalPremiNetto, pol.getDbNDDisc1());
                            TotalPremiNetto = BDUtil.sub(TotalPremiNetto, pol.getDbNDFeeBase1());
                            t[n] = BDUtil.add(t[n++], TotalPremiNetto); //total tagihan komisi                            
                                                        
                            String custName = "";
                            if (pol.getStCustomerName()!=null) {
                                if (pol.getStCustomerName().length()>30)
                                    custName = pol.getStCustomerName().substring(0,30);
                                else
                                    custName = pol.getStCustomerName().substring(0, pol.getStCustomerName().length());
                            }
                                    
                            String no_polis_cetak = "";
                            String no_polis = pol.getStPolicyNo();
                            if(no_polis.length()==18)
                                no_polis_cetak = no_polis.substring(0,4)+"-"+no_polis.substring(4,8)+"-"+no_polis.substring(8,12)+"-"+no_polis.substring(12,16)+"-"+no_polis.substring(16,18);
                            else
                                no_polis_cetak = no_polis.substring(0,4)+"-"+no_polis.substring(4,8)+"-"+no_polis.substring(8,12)+"-"+no_polis.substring(12,16);
                            
                            no++;
                            
                            if (i==counter) {
                                counter = counter + 45;
                        %>   
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="13" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>

                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        <%
                        subTotalPremiBruto 	= null;
                        subTotalFeeBase		= null;
                        subTotalDisc 		= null;
                        subTotalComm 		= null;
                        subTotalBfee 		= null;
                        subTotalHfee 		= null;
                        subTotalTax      	= null;
                        subTotalPremiNetto 	= null;
                        
                            }%>
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center"><%=no%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtPolicyDate())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(no_polis_cetak)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStEntityID())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(custName)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(TotalPremiBruto,0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDFeeBase1(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDDisc1(),0)%></fo:block></fo:table-cell>
                            <%if(tanpaKomisi){%>
                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.add(pol.getDbNDComm1(), TotalTax),0)%></fo:block></fo:table-cell>
                            <%} %>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDBrok1(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbNDHFee(),0)%></fo:block></fo:table-cell>
                            <%if(tanpaKomisi){%>
                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(TotalTax,0)%></fo:block></fo:table-cell>
                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(TotalPremiNetto,0)%></fo:block></fo:table-cell>
                            <%} %>
                       </fo:table-row>
                        
                        <% 
                        subTotalPremiBruto = BDUtil.add(subTotalPremiBruto,TotalPremiBruto);
                        subTotalFeeBase = BDUtil.add(subTotalFeeBase,pol.getDbNDFeeBase1());
                        subTotalDisc 	= BDUtil.add(subTotalDisc,pol.getDbNDDisc1());
                        subTotalComm 	= BDUtil.add(subTotalComm,pol.getDbNDComm1());
                        subTotalBfee 	= BDUtil.add(subTotalBfee,pol.getDbNDBrok1());
                        subTotalHfee 	= BDUtil.add(subTotalHfee,pol.getDbNDHFee());
                        subTotalTax	= BDUtil.add(subTotalTax,TotalTax);
                        subTotalPremiNetto 	= BDUtil.add(subTotalPremiNetto,TotalPremiNetto);
                        
                        } %>    
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="13">  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>
                       
                        
                    </fo:table-body>   
                </fo:table>   
            </fo:block>
            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
            </fo:block>   
            <fo:block font-size="8pt">
                {L-ENG Print User-L}{L-INA Print User-L} : <%=SessionManager.getInstance().getCreateUser().getStShortName()%>  
            </fo:block>          
            
            <fo:block text-align="start">
                <fo:instream-foreign-object>
                    <barcode:barcode
                        xmlns:barcode="http://barcode4j.krysalis.org/ns"
                        message="<%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(t[8],0)%>" orientation="0">
                        <barcode:datamatrix> 
                            <barcode:height>40pt</barcode:height>
                            <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                        </barcode:datamatrix>
                    </barcode:barcode>
                </fo:instream-foreign-object>                                    
            </fo:block>
            
            <fo:block id="end-of-document"><fo:marker
                    marker-class-name="term">
                    <fo:instream-foreign-object>
                        <svg xmlns="http://www.w3.org/2000/svg" width="5cm" height="1cm" xml:space="preserve">     
                            <rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>
            
        </fo:flow>   
    </fo:page-sequence>   
</fo:root> 