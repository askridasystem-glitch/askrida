<%@ page import="com.webfin.insurance.model.*, 
com.crux.ff.model.FlexFieldHeaderView, 
com.crux.ff.model.FlexFieldDetailView, 
com.crux.util.*, 
com.crux.util.Tools, 
com.crux.util.fop.FOPUtil, 
com.crux.util.BDUtil,
java.math.BigDecimal, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager, 
com.webfin.insurance.form.ProductionMarketingReportForm, 
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 
<% 

final DTOList l = (DTOList)request.getAttribute("RPT");

final ProductionMarketingReportForm form = (ProductionMarketingReportForm)SessionManager.getInstance().getCurrentForm();


String label1="";
String label2="";
String label3="";
String label4="";

if(form.getStPolicyTypeID()!=null){
    if(form.getStPolicyTypeID().equalsIgnoreCase("14")){
        label1 = "Berangkat Dari";
        label2 = "Berangkat Menuju";
        label3 = "Waktu Berangkat";
        label4 = "Diangkut Dengan";

    }

    if(form.getStPolicyTypeID().equalsIgnoreCase("5")){
        label1 = "Bisnis";
        label2 = "Buatan";
        label3 = "Alamat";
        label4 = "Kondisi";

    }
}

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
    
    <!-- defines page layout --> 
    <fo:layout-master-set> 
        
        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="first" 
                               page-height="28cm"
                               page-width="35cm"
                               margin-top="1cm"
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
        
        <!-- usage of page layout --> 
        <!-- header --> 
        <fo:static-content flow-name="xsl-region-before"> 
            
            <fo:block-container height="0cm" width="0cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="message"
                                        retrieve-boundary="page"
                                        retrieve-position="first-starting-within-page"/>
                </fo:block>
            </fo:block-container>
            <fo:block-container height="0cm" width="0cm" top="0cm" left="0cm" position="absolute">    <fo:block>
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
            
            <fo:block font-size="8pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="10mm"/><!-- No -->
                    <fo:table-column column-width="20mm"/><!-- Policy Date-->
                    <fo:table-column column-width="25mm"/><!-- Policy No -->
                    <fo:table-column column-width="10mm"/><!-- Policy No -->
                    <fo:table-column column-width="60mm"/><!-- The Insured -->
                    <fo:table-column column-width="25mm"/><!-- Premium -->
                    <fo:table-column column-width="25mm"/><!-- Biaya Polis -->
                    <fo:table-column column-width="40mm"/><!-- Biaya Materai -->
                    <fo:table-column column-width="40mm"/><!-- feebase -->
                    <fo:table-column column-width="40mm"/><!-- ppn -->
                    <fo:table-column column-width="40mm"/><!-- Kode Relasi -->

                    <fo:table-header>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-family="serif" font-weight="bold" font-size="20pt" text-align="center">             
                                    LAPORAN DEKLARASI
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block  font-weight="bold" font-size="18pt" text-align="center">
                                    <% if (form.getPolicyDateFrom()!=null || form.getPolicyDateTo()!=null) {%> 
                                    Tanggal Polis : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-weight="bold" text-align="center">
                                    <% if (form.getPeriodFrom()!=null || form.getPeriodTo()!=null) {%> 
                                    Periode : <%=JSPUtil.printX(form.getPeriodFrom())%> S/D <%=JSPUtil.printX(form.getPeriodTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <% if (form.getStBranch()!=null) { %>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-weight="bold">
                                    Cabang : <%=JSPUtil.printX(form.getCostCenter().getStDescription())%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% } %>
                        
                        <% if (form.getStRegion()!=null) { %>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-weight="bold">
                                    Outlet : <%=JSPUtil.printX(form.getRegion().getStDescription())%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <% } %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-weight="bold">
                                    <% if (form.getStPolicyTypeGroupID()!=null) {%> 
                                    Jenis Group : <%=JSPUtil.printX(form.getStPolicyTypeGroupDesc())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-weight="bold">
                                    <% if (form.getStPolicyTypeID()!=null) {%> 
                                    Jenis Polis : <%=JSPUtil.printX(form.getStPolicyTypeDesc())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>                        
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-weight="bold">
                                    <% if (form.getStFltCoverType()!=null) {%> 
                                    Jenis Penutupan  : <%=JSPUtil.printX(form.getStFltCoverTypeDesc())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-weight="bold">
                                    <% if (form.getStCustCategory1()!=null) {%> 
                                    Sumber Bisnis : <%=JSPUtil.printX(form.getStCustCategory1Desc())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-weight="bold">
                                    <% if (form.getStEntityID()!=null) {%> 
                                    Customer Name :<%=JSPUtil.printX(form.getStEntityName())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-weight="bold">
                                    <% if (form.getStMarketerID()!=null) {%> 
                                    Marketer Name :<%=JSPUtil.printX(form.getStMarketerName())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-weight="bold">
                                    <% if (form.getStCompanyID()!=null) {%> 
                                    Company Name :<%=JSPUtil.printX(form.getStCompanyName())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-weight="bold">
                                    <% if (form.getStCoinsurerID()!=null) {%> 
                                    Coinsurer Name :<%=JSPUtil.printX(form.getStCoinsurerName())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-weight="bold">
                                    <% if (form.getStCustStatus()!=null) {%> 
                                    Customer Status :<%=JSPUtil.printX(form.getStCustStatus())%>  
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="11"><fo:block text-align="end" font-weight="bold">{L-ENG (In IDR)-L}{L-INA (dalam rupiah)-L}</fo:block></fo:table-cell>
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="11" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Policy Date-L}{L-INA Tgl. Polis-L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Policy Number -L}{L-INA Nomor Deklarasi -L}</fo:block></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">{L-ENG TSI-L}{L-INA Sum Insured-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Bruto Premium-L}{L-INA Premi Bruto-L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center"><%=label1%></fo:block></fo:table-cell>

           
                            <fo:table-cell ><fo:block text-align="center"><%=label2%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=label3%></fo:block></fo:table-cell>

                            <fo:table-cell ><fo:block text-align="center"><%=label4%></fo:block></fo:table-cell>
                            
                        </fo:table-row>  
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="11" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                    </fo:table-header>
                    
                    <fo:table-body>
                        
                        <!-- GARIS  -->  
                        <!--   \<fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-before.optimum="1pt" space-after.optimum="10pt"></fo:block> -->  
     
                        
                        <%   
                        int counter = 60;
                        BigDecimal subTotalPremi = new BigDecimal(0);
                        BigDecimal subTotalTSI = new BigDecimal(0);
                        
                        
                        BigDecimal [] t = new BigDecimal[10];
                        
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);

                            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) pol.getObjects().get(0);
                            
                            int n=0;
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiTotal());
                            t[n] = BDUtil.add(t[n++], pol.getDbInsuredAmount());
                          
                            String custName = "";
                            if(pol.getStCustomerName().length()>80)
                                custName = pol.getStCustomerName().substring(0,80);
                            else
                                custName = pol.getStCustomerName().substring(0, pol.getStCustomerName().length());
                            
                            
                            if (i==counter) {
                                counter = counter + 60;
                                
                        /*   	  int p=-1;
 
                        final DTOList objects = pol.getObjects();
 
                        while (true) {
                        p++;
                        if (p>=objects.size()) break;
 
                        final InsurancePolicyObjectView obj = (InsurancePolicyObjectView)objects.get(p);
                         */
                        %>   
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row space-before.optimum="5pt">
                            <fo:table-cell number-columns-spanned="5"><fo:block text-align="center">SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalPremi,0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(subTotalTSI,0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell>
                            
                            <fo:table-cell number-columns-spanned="2"></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        <%
                        subTotalPremi = null;
                        subTotalTSI = null;
  
                            }%>
                        
                        
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center"><%=String.valueOf(i+1)%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPolicyDate())%></fo:block></fo:table-cell>    <!-- No --><!-- Period --> 
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStSPPANo())%></fo:block></fo:table-cell>    <!-- No --><!-- Policy No -->
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStEntityID())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(custName)%></fo:block></fo:table-cell>    <!-- No --><!-- The Insured -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(),0)%></fo:block></fo:table-cell>    <!-- No --><!-- Premium -->
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(),0)%></fo:block></fo:table-cell>    <!-- No --><!-- Premium --> 
                            
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(obj.getStReference1())%></fo:block></fo:table-cell>    <!-- No --><!-- Discount -->
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(obj.getStReference2())%></fo:block></fo:table-cell>    <!-- No --><!-- Discount -->
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(obj.getStReference3())%></fo:block></fo:table-cell>    <!-- No --><!-- Discount -->
                           
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(obj.getStReference4())%></fo:block></fo:table-cell>    <!-- No --><!-- Broker 1 -->
                            
                        </fo:table-row>
                        
                        <% 
                        subTotalPremi = BDUtil.add(subTotalPremi,pol.getDbPremiTotal());
                        subTotalTSI = BDUtil.add(subTotalTSI,pol.getDbInsuredAmount());

                        
                        
                        } %>   
                        
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="5"><fo:block> TOTAL : </fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[0],0)%></fo:block></fo:table-cell><!-- Total Premi --> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(t[1],0)%></fo:block></fo:table-cell><!-- Total Premi --> 
                            <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell><!-- Total Fee --> 
                            <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell><!-- Total Fee -->
                            <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell><!-- Total Fee -->
                            <fo:table-cell ><fo:block text-align="end"></fo:block></fo:table-cell><!-- Total Fee -->
                            
                            <fo:table-cell ></fo:table-cell>    
                            
                        </fo:table-row>   
                        
                        <!-- GARIS DALAM KOLOM -->   
   
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="11" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 
                        
                    </fo:table-body>   
                </fo:table>   
            </fo:block> 
            
            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
            </fo:block>    
            
            <fo:block text-align="start">
                <fo:instream-foreign-object>
                    <barcode:barcode
                        xmlns:barcode="http://barcode4j.krysalis.org/ns"
                        message="<%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(t[0],0)%>" orientation="0">
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
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>
            
            
        </fo:flow> 
    </fo:page-sequence>   
</fo:root>   