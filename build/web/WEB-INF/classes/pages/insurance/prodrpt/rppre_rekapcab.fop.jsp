<%@ page import="com.webfin.insurance.model.*, 
com.crux.ff.model.FlexFieldHeaderView, 
com.crux.ff.model.FlexFieldDetailView, 
com.crux.util.*, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal, 
com.crux.lov.LOVManager, 
java.util.Iterator, 
com.crux.web.controller.SessionManager, 
com.webfin.insurance.form.ProductionRecapReportForm,
com.webfin.insurance.form.ProductionReinsuranceReportForm,
com.webfin.insurance.model.*,
java.util.Date,
java.util.HashMap"%><?xml version="1.0" encoding="utf-8"?> 
<%

final DTOList l = (DTOList)request.getAttribute("RPT");

final LookUpUtil regions = (LookUpUtil) LOVManager.getInstance().getLOV("LOV_CostCenter", null);
final LookUpUtil custcategory = (LookUpUtil) LOVManager.getInstance().getLOV("LOV_CustCategory1", null);

final HashMap amountMap = (HashMap)l.getAttribute("AMOUNT_MAP");

final ProductionReinsuranceReportForm form = (ProductionReinsuranceReportForm)SessionManager.getInstance().getCurrentForm();


//final ProductionRecapReportForm pprc = (ProductionRecapReportForm)SessionManager.getInstance().getCurrentForm();

%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-height="29.7cm"
                               page-width="24cm"
                               margin-top="1cm"
                               margin-bottom="2cm"
                               margin-left="2.5cm"
                               margin-right="2.5cm">
            <fo:region-body margin-top="1cm" margin-bottom="1.5cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="1.5cm"/>
        </fo:simple-page-master>
        
    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">
        
        <!-- usage of page layout -->
        <!-- header -->
        <fo:static-content flow-name="xsl-region-before">
            
            <fo:block text-align="center" font-size="16pt" font-family="tahoma" line-height="16pt" >
                {L-ENG Register of Premium List R/I Recapitulation per Branch-L}{L-INA Register Produksi Premi Reasuransi Rekap per Cabang-L}
            </fo:block>
            
            <%--     <fo:block text-align="center" font-size="12pt" font-family="tahoma" line-height="16pt" >
        {L-ENG PER BRANCH-L}{L-INA PER CABANG -L}
      </fo:block> --%>
 

        </fo:static-content>
        
        <fo:flow flow-name="xsl-region-body">
            
            <fo:block font-size="9pt" text-align="center">
                <% if (form.getPeriodFrom()!=null || form.getPeriodTo()!=null) {%> 
                Periode : <%=JSPUtil.printX(form.getPeriodFrom())%> S/D <%=JSPUtil.printX(form.getPeriodTo())%>
                <% } %>
            </fo:block> 
            
            <fo:block font-size="9pt">
                <% if (form.getStBranchDesc()!=null) {%> 
                Cabang : <%=JSPUtil.printX(form.getStBranchDesc())%>  
                <% } %>
            </fo:block>
            
            <fo:block font-size="9pt">
                <% if (form.getStFltCoverTypeDesc()!=null) {%> 
                Jenis Penutupan  : <%=JSPUtil.printX(form.getStFltCoverTypeDesc())%>  
                <% } %>
            </fo:block>
            
            <fo:block font-size="10pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="40mm"/>
                    <%
                    {final Iterator it = custcategory.getIterator();
                     while (it.hasNext()) {
                         String cod = (String) it.next();
                    
                    %>
                    <fo:table-column column-width="30mm"/>
                    <% }} %>
                    <fo:table-body>
                        
                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-rows-spanned="2" display-align="center"><fo:block>No</fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" display-align="center"><fo:block>DAERAH</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="<%=custcategory.size()%>" ><fo:block text-align="center">SUMBER BISNIS</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <%--<fo:table-cell ><fo:block>No</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>DAERAH</fo:block></fo:table-cell>--%>
                            <%
                            {final Iterator it = custcategory.getIterator();
                             while (it.hasNext()) {
                                 String cod = (String) it.next();
                            
                            %>
                            <fo:table-cell ><fo:block text-align="end"><%=custcategory.getDescription(cod)%></fo:block></fo:table-cell>
                            <% }} %>
                            
                        </fo:table-row>
                        
                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <%
                        final HashMap totals = new HashMap();
                        
                        {
                            final Iterator regit = regions.getIterator();
                            
                            int n=0;
                            
                            
                            while (regit.hasNext()) {
                                String regCode = (String) regit.next();
                                n++;
                        
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block><%=n%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=regions.getDescription(regCode)%></fo:block></fo:table-cell>
                            <%
                                {final Iterator it = custcategory.getIterator();
                                 while (it.hasNext()) {
                                     String cod = (String) it.next();
                                     
                                     //BigDecimal amt = pprc.getAmount(regCode,cod);
                                     
                                     final InsurancePolicyView vpol = (InsurancePolicyView)amountMap.get(regCode+"/"+cod);
                                     
                                     BigDecimal amt = vpol==null?null:vpol.getDbPremiTotal();
                                     
                                     if(amt==null) amt=BDUtil.zero;
                                     
                                     final String key = "O"+cod;
                                     
                                     totals.put(
                                             key,
                                             BDUtil.add(
                                             (BigDecimal) totals.get(key),
                                             amt
                                             )
                                             );
                            
                            
                            
                            %>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(amt,0)%></fo:block></fo:table-cell>
                            <% }} %>
                        </fo:table-row>
                        <% }}%>
                        
                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <%
                            {final Iterator it = custcategory.getIterator();
                             while (it.hasNext()) {
                                 String cod = (String) it.next();
                                 
                                 final String key = "O"+cod;
                                 
                                 final Object tot = totals.get(key);
                            
                            
                            
                            %>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX((BigDecimal) tot,0)%></fo:block></fo:table-cell>
                            <% }} %>
                        </fo:table-row>
                        
                        <%
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            if (!pol.isInward()) continue;
                            
                            final String key = "I"+pol.getStBusinessSourceCode();
                            
                            BigDecimal amt = BDUtil.add(
                                    (BigDecimal) totals.get(key),
                                    pol.getDbPremiTotal()
                                    );
                            
                            if (amt==null) amt=BDUtil.zero;
                            
                            totals.put(
                                    key,
                                    amt
                                    );
                        }
                        
                        %>
                        
                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>INWARD</fo:block></fo:table-cell>
                            <%
                            {final Iterator it = custcategory.getIterator();
                            while (it.hasNext()) {
                            String cod = (String) it.next();
                            
                            final String key = "I"+cod;
                            
                            Object tot = totals.get(key);
                            
                            if (tot==null) tot=BDUtil.zero;
                            
                            %>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX((BigDecimal) tot,0)%></fo:block></fo:table-cell>
                            <% }} %>
                        </fo:table-row>
                        
                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>TOTAL</fo:block></fo:table-cell>
                            <%
                            {final Iterator it = custcategory.getIterator();
                            while (it.hasNext()) {
                            String cod = (String) it.next();
                            
                            BigDecimal tot = (BigDecimal) totals.get("I"+cod);
                            
                            tot = BDUtil.add(tot, (BigDecimal) totals.get("O"+cod));
                            
                            if (tot==null) tot=BDUtil.zero;
                            
                            %>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX((BigDecimal) tot,0)%></fo:block></fo:table-cell>
                            <% }} %>
                        </fo:table-row>
                        
                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" >
                                <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
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
                        <svg xmlns="http://www.w3.org/2000/svg" width="15cm" height="1cm" xml:space="preserve">     
                            <rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>

