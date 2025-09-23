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
com.webfin.insurance.form.ProductionMarketingReportForm,
com.webfin.insurance.model.*,
java.util.Date,
java.util.HashMap"%><?xml version="1.0" encoding="utf-8"?> 
<% 

final DTOList l = (DTOList)request.getAttribute("RPT");

final LookUpUtil regions = (LookUpUtil) LOVManager.getInstance().getLOV("LOV_CostCenter", null);


final LookUpUtil poltypes = (LookUpUtil) LOVManager.getInstance().getLOV("LOV_PolicyType", null);

final LookUpUtil covertype = (LookUpUtil) LOVManager.getInstance().getLOV("LOV_InsuranceCoverType", null);

final LookUpUtil custcategory = (LookUpUtil) LOVManager.getInstance().getLOV("LOV_CustCategory1", null);

final HashMap amountMap = (HashMap)l.getAttribute("AMOUNT_MAP");

BigDecimal jumlah = new BigDecimal(0);

BigDecimal tot_jumlah = new BigDecimal(0);

BigDecimal persen = new BigDecimal(0);

BigDecimal tot_persen = new BigDecimal(0);

BigDecimal jml_inward = new BigDecimal(0);

final ProductionMarketingReportForm form = (ProductionMarketingReportForm)SessionManager.getInstance().getCurrentForm();


//final ProductionRecapReportForm pprc = (ProductionRecapReportForm)SessionManager.getInstance().getCurrentForm();

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
    
    <!-- defines page layout --> 
    <fo:layout-master-set> 
        
        <fo:simple-page-master master-name="only" 
                               page-height="30cm"
                               page-width="25cm"
                               margin-top="2cm"
                               margin-bottom="2cm"
                               margin-left="2cm"
                               margin-right="2.5cm">
            <fo:region-body margin-top="2cm" margin-bottom="1.5cm"/> 
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
            
            <fo:block text-align="center" font-size="16pt" line-height="16pt" > 
                {L-ENG RECAPITULATION PRODUCTION PREMIUM PER BRANCH -L}{L-INA REKAPITULASI PRODUKSI PREMI PER CABANG NON KOMISI-L}
            </fo:block> 
            
            
            <fo:block text-align="center" font-size="9pt" line-height="16pt" > 
                {L-ENG (After Disc + Comm + Bfee + Hfee)-L}{L-INA (Setelah Diskon + Komisi + Bfee + Hfee)-L}
            </fo:block> 
            
            
        </fo:static-content> 
        
        
        
        
        <fo:flow flow-name="xsl-region-body">
            
            <fo:block font-size="9pt" text-align="center">
                <% if (form.getPeriodFrom()!=null || form.getPeriodTo()!=null) {%> 
                Periode : <%=JSPUtil.printX(form.getPeriodFrom())%> S/D <%=JSPUtil.printX(form.getPeriodTo())%>
                <% } %>
            </fo:block> 
            
            <fo:block font-size="9pt" text-align="center">
                <% if (form.getPolicyDateFrom()!=null || form.getPolicyDateTo()!=null) {%> 
                Tanggal Polis : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
                <% } %>
            </fo:block> 
            
            <fo:block font-size="9pt">
                <% if (form.getStBranchName()!=null) { %>
                Cabang : <%=JSPUtil.printX(form.getStBranchName())%>
                <% } %>
                <% if (form.getStBranchDesc()!=null) { %>
                Cabang : <%=JSPUtil.printX(form.getStBranchDesc())%>
                <% } %>
            </fo:block>
            
            <fo:block font-size="9pt">
                <% if (form.getStFltCoverTypeDesc()!=null) {%> 
                Jenis Penutupan  : <%=JSPUtil.printX(form.getStFltCoverTypeDesc())%>  
                <% } %>
            </fo:block>
            
            <fo:block font-size="9pt">
                <% if (form.getStMarketerName()!=null) {%> 
                Marketer  : <%=JSPUtil.printX(form.getStMarketerName())%>  
                <% } %>
            </fo:block>
            
            
            <fo:block font-size="9pt"> 
                <fo:table table-layout="fixed"> 
                    <fo:table-column column-width="15mm"/> 
                    <fo:table-column column-width="46mm"/> 
                    <% 
                    {final Iterator it = custcategory.getIterator();
                     while (it.hasNext()) {
                         String cod = (String) it.next(); 
                    
                    %> 
                    <fo:table-column column-width="30mm"/> 
                    <% }} %>
                    
                    
                    <fo:table-column column-width="30mm"/> 
                    
                    
                    <fo:table-body> 
                        
                        <!-- GARIS DALAM KOLOM --> 
 
                        <fo:table-row> 
                            <fo:table-cell number-columns-spanned="7" > 
                                <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block> 
                            </fo:table-cell> 
                        </fo:table-row> 
                        
                        <fo:table-row> 
                            
                            <fo:table-cell number-rows-spanned="2" display-align="center"><fo:block text-align="center">NO</fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" display-align="center"><fo:block>DAERAH</fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="<%=custcategory.size()%>" ><fo:block text-align="center">SUMBER BISNIS</fo:block></fo:table-cell>
                            <fo:table-cell number-rows-spanned="2" display-align="center"><fo:block text-align="center">JUMLAH</fo:block></fo:table-cell>
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
                            <% }
                             
                            }%> 
                            
                            <%--  
            
			--%>
                        </fo:table-row> 
                        
                        
                        
                        <!-- GARIS DALAM KOLOM --> 
 
                        <fo:table-row> 
                            <fo:table-cell number-columns-spanned="7" > 
                                <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block> 
                            </fo:table-cell> 
                        </fo:table-row> 
                        
                        
                        <% 
                        
                        {
                        final Iterator regit = regions.getIterator();
                        
                        int n=0;
                        
                        
                        while (regit.hasNext()) {
                            String regCode = (String) regit.next();
                            n++;
                            
                            String kode = new String("");
                            
                            if(regCode.length() == 1) kode = "0"+regCode;
                            else kode = regCode;
                            
                            {final Iterator it = custcategory.getIterator();
                             while (it.hasNext()) {
                                 String cod = (String) it.next();
                                 
                                 final InsurancePolicyView vpol = (InsurancePolicyView)amountMap.get(regCode+"/"+cod);
                                 
                                 BigDecimal amt = vpol==null?null:vpol.getDbPremiTotalAfterDisc();
                                 
                                 if(amt==null) amt=BDUtil.zero;
                                 
                                 tot_jumlah = tot_jumlah.add(amt);
                                 
                             }}
                        }
                        }
                        
                        
                        %>       
                        
                        
                        <% 
                        final HashMap totals = new HashMap();
                        
                        {
                            final Iterator regit = regions.getIterator();
                            
                            int n=0;
                            
                            
                            while (regit.hasNext()) {
                                String regCode = (String) regit.next();
                                n++;
                                
                                String kode = new String("");
                                
                                if(regCode.length() == 1) kode = "0"+regCode;
                                else kode = regCode;
                        
                        %> 
                        
                        <fo:table-row> 
                            
                            
                            
                            <fo:table-cell ><fo:block text-align="center"><%=n%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block><%=regions.getDescription(regCode)%></fo:block></fo:table-cell> 
                            <% 
                                {final Iterator it = custcategory.getIterator();
                                 while (it.hasNext()) {
                                     String cod = (String) it.next();
                                     
                                     //BigDecimal amt = pprc.getAmount(regCode,cod);
                                     
                                     final InsurancePolicyView vpol = (InsurancePolicyView)amountMap.get(regCode+"/"+cod);
                                     
                                     BigDecimal amt = vpol==null?null:vpol.getDbPremiTotalAfterDisc();
                                     
                                     if(amt==null) amt=BDUtil.zero;
                                     
                                     final String key = "O"+cod;
                                     
                                     totals.put(
                                             key,
                                             BDUtil.add(
                                             (BigDecimal) totals.get(key),
                                             amt
                                             )
                                             );
                                     
                                     jumlah = jumlah.add(amt);
                                     
                                     //tot_jumlah = tot_jumlah.add(jumlah);
                                     
                                     if (jumlah.equals(BDUtil.zero)) persen = BDUtil.zero;
                                     
                                     else
                                         persen = (jumlah.divide(tot_jumlah,BigDecimal.ROUND_CEILING)).multiply(new BigDecimal(100));
                                     
                                     tot_persen = tot_persen.add(persen);
                            
                            
                            %> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(amt,0)%></fo:block></fo:table-cell>
                            <% }} %> 
                            
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(jumlah,0)%></fo:block></fo:table-cell>
                            
                            <% 
                            
                            jumlah = new BigDecimal(0);
                            
                            %>
                            
                            
                        </fo:table-row> 
                        <% }}%>
                        
                        
                        
                        <!-- GARIS DALAM KOLOM --> 
 
                        <fo:table-row> 
                            <fo:table-cell number-columns-spanned="7" > 
                                <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block> 
                            </fo:table-cell> 
                        </fo:table-row> 
                        
                        <fo:table-row> 
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block>SUB-TOTAL</fo:block></fo:table-cell> 
                            <% 
                            {final Iterator it = custcategory.getIterator();
                             while (it.hasNext()) {
                                 String cod = (String) it.next();
                                 
                                 final String key = "O"+cod;
                                 
                                 final Object tot = totals.get(key); 
                            
                            
                            
                            %> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX((BigDecimal) tot,0)%></fo:block></fo:table-cell> 
                            <% }} %> 
                            
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX((BigDecimal) tot_jumlah,0)%></fo:block></fo:table-cell> 
                            
                        </fo:table-row> 
                        
                        <% 
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            if (!pol.isInward()) continue;
                            
                            final String key = "I"+pol.getStBusinessSourceCode();
                            
                            BigDecimal amt = pol.getDbPremiTotalAfterDisc();
                            
                            if (amt==null) amt=BDUtil.zero;
                            
                            totals.put(
                                    key,
                                    BDUtil.add(
                                    (BigDecimal) totals.get(key),
                                    amt
                                    )
                                    );
                        } 
                        
                        %> 
                        
                        <!-- GARIS DALAM KOLOM --> 
 
                        <fo:table-row> 
                            <fo:table-cell number-columns-spanned="7" > 
                                <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block> 
                            </fo:table-cell> 
                        </fo:table-row> 
                        <%--
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
                  
                  
                  
                  jml_inward = jml_inward.add((BigDecimal)tot) ;
 
             %> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX((BigDecimal) tot,0)%></fo:block></fo:table-cell> 
             
            <% }} %> 
            
              
            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX((BigDecimal) jml_inward,0)%></fo:block></fo:table-cell> 
            </fo:table-row> 
 
   <!-- GARIS DALAM KOLOM --> 
 
                    <fo:table-row> 
                      <fo:table-cell number-columns-spanned="7" > 
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
              
             
            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX((BigDecimal) tot_jumlah,0)%></fo:block></fo:table-cell> 
            </fo:table-row> 
 
   <!-- GARIS DALAM KOLOM --> 
 
                    <fo:table-row> 
                      <fo:table-cell number-columns-spanned="7" > 
                           <fo:block border-width="0.70pt" border-style="solid" border-before-precedence="5" line-height="0.15pt"></fo:block> 
                      </fo:table-cell> 
                    </fo:table-row> 
 --%>
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
                        message="<%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(tot_jumlah,0)%>" orientation="0">
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
