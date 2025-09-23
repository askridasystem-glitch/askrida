<%@ page import="com.crux.util.fop.FOPTableSource,
com.webfin.insurance.form.ProductionMarketingReportForm,
com.crux.common.model.HashDTO,
com.crux.util.*,
java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <%
    final ProductionMarketingReportForm form = (ProductionMarketingReportForm) request.getAttribute("FORM");
    %>
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-width="30cm"
                               page-height="21cm"
                               margin-top="0.5cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            <fo:region-body margin-top="4cm" margin-bottom="1cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="1.5cm"/>
        </fo:simple-page-master>
        
    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">
        
        <!-- usage of page layout -->
        <fo:static-content flow-name="xsl-region-before">
            <fo:block-container height="1cm" width="15cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="message"
                                        retrieve-boundary="page"
                                        retrieve-position="first-starting-within-page"/>
                </fo:block>
            </fo:block-container>
            <fo:block-container height="1cm" width="15cm" top="0cm" left="0cm" position="absolute">    <fo:block>
                    <fo:retrieve-marker retrieve-class-name="term"
                                        retrieve-boundary="page"
                                        retrieve-position="last-ending-within-page"/>
                </fo:block>
            </fo:block-container>
            <fo:block text-align="end"
                      font-size="6pt"
                      line-height="12pt" 
                      font-style="bold">
                rpp_occupation - PT. Asuransi Bangun Askrida
            </fo:block>
            <fo:block text-align="end"
                      font-size="8pt" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>
        
        <fo:flow flow-name="xsl-region-body">
            
            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="290mm"/>
                    <fo:table-body>
                        
                        
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block font-size="12pt" line-height="12pt" text-align="center" space-after.optimum="5pt">	 
                                    RISK ASSESSMENT
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block font-size="12pt" line-height="12pt" text-align="center" space-after.optimum="5pt">	 
                                    <% if (form.getPolicyDateFrom()!=null || form.getPolicyDateTo()!=null) {%> 
                                    Tanggal Polis : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block font-size="12pt"><% if(form.getStBranchDesc()!=null) {%>	 
                                    Cabang : 
                                    <%= JSPUtil.printX(form.getStBranchDesc())%>
                                    <% }%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block font-size="12pt" line-height="12pt" text-align="end" space-before.optimum="10pt">	 
                                    (dalam rupiah)
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="8pt" line-height="20pt"></fo:block>
            
            <!-- defines text title level 1-->
            <fo:block font-size="8pt" line-height="10pt" space-after.optimum="10pt">
                <%
                
                SQLAssembler sqa = new SQLAssembler();
                
                sqa.addSelect(" a.pol_no,a.cust_name,b.ref1,b.ref9d,b.risk_class,b.ref3,c.ins_risk_cat_code,c.description ");
                
                sqa.addQuery("   from ins_policy a "+
                        "	inner join ins_pol_obj b on b.pol_id = a.pol_id "+
                        "	left join ins_risk_cat c on b.ins_risk_cat_id = c.ins_risk_cat_id "
                        );
                
                sqa.addClause("a.status in ('ENDORSE','POLICY','RENEWAL')");
                
                sqa.addClause("a.pol_type_id in (1,2,81,19)");
                
                sqa.addClause("a.effective_flag='Y'");
                
                
                if (form.getPolicyDateFrom()!=null) {
                    sqa.addClause("date_trunc('day',a.policy_date) >= ?");
                    sqa.addPar(form.getPolicyDateFrom());
                }
                
                if (form.getPolicyDateTo()!=null) {
                    sqa.addClause("date_trunc('day',a.policy_date) <= ?");
                    sqa.addPar(form.getPolicyDateTo());
                }
                
                if(form.getPeriodFrom()!=null) {
                    sqa.addClause("date_trunc('day',a.period_start) >= ?");
                    sqa.addPar(form.getPeriodFrom());
                }
                
                if(form.getPeriodTo()!=null) {
                    sqa.addClause("date_trunc('day',a.period_start) <= ?");
                    sqa.addPar(form.getPeriodTo());
                }
                
                if(form.getStBranch()!=null) {
                    sqa.addClause("a.cc_code = ?");
                    sqa.addPar(form.getStBranch());
                }
                
                String q = sqa.getSQL()+" order by a.pol_no" ;
                
                
                
                
                final DTOList r = ListUtil.getDTOListFromQuery(q,  sqa.getPar(), HashDTO.class);
                /*
                HashDTO tot = new HashDTO();
 
                tot.setFieldValueByFieldName("no", "");
                tot.setFieldValueByFieldName("premi_amt", r.getTotal("premi_amt"));
                tot.setFieldValueByFieldName("komisi", r.getTotal("komisi"));
                tot.setFieldValueByFieldName("netto", r.getTotal("netto"));
                tot.setFieldValueByFieldName("cust_name", "TOTAL");
 
                r.add(tot);
                 */
                
                final FOPTableSource fopTableSource = new FOPTableSource(
                        9,
                        new int [] {1,2,5,3,1,1,1,1,5},
                        29,
                        "cm"
                        ) {
                    public int getRowCount() {
                        return r.size();
                    }
                    
                    public Object getColumnValue(int rowNo, int columnNo) {
                        HashDTO h = (HashDTO) r.get(rowNo);
                        switch(columnNo) {
                            case 0: return h.getFieldValueByFieldNameST("no")==null?String.valueOf(rowNo+1):"";
                            case 1: return h.getFieldValueByFieldNameST("pol_no");
                            case 2: return h.getFieldValueByFieldNameST("cust_name").replaceAll("&","{L-ENG And-L}{L-INA Dan-L}");
                            case 3: return h.getFieldValueByFieldNameST("ref1").replaceAll("&","{L-ENG And-L}{L-INA Dan-L}");
                            case 4: return h.getFieldValueByFieldNameST("ref9d");
                            case 5: return h.getFieldValueByFieldNameST("risk_class");
                            case 6: return h.getFieldValueByFieldNameST("ref3");
                            case 7: return h.getFieldValueByFieldNameST("ins_risk_cat_code");
                            case 8: return h.getFieldValueByFieldNameST("description");
                        }
                        return "?";
                    }
                    
                    public String getColumnHeader(int columnNo) {
                        switch(columnNo) {
                            case 0: return "NO";
                            case 1: return "POLICY NO.";
                            case 2: return "CUSTOMER NAME";
                            case 3: return "OCCUPATION";
                            case 4: return "POST CODE";
                            case 5: return "CLASS";
                            case 6: return "CODE";
                            case 7: return "CODE2";
                            case 8: return "DESCRIPTION";
                        }
                        return "?";
                    }
                /*
                public String getColumnAlign(int rowNo, int columnNo) {
                switch(columnNo) {
                case 4:
                case 6:
                case 7:
                return "end";
                };
 
                return super.getColumnAlign(rowNo, columnNo);    //To change body of overridden methods use File | Settings | File Templates.
                }*/
                };
                
                fopTableSource.display(out);
                %>
            </fo:block>
            
            <fo:block font-size="6pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
            </fo:block> 
            <%-- 
       <fo:block font-size="8pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="100mm"/>
         <fo:table-column column-width="100mm"/>
         <fo:table-body>
         <fo:table-row>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell >
               <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(new Date(),"d MMM yyyy")%></fo:block>
             </fo:table-cell>
           </fo:table-row>
           <fo:table-row>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell >
               <fo:block text-align="center">BAGIAN REASURANSI</fo:block>
             </fo:table-cell>
           </fo:table-row>   
           <fo:table-row>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell >
               <fo:block text-align="center">S. E. &#x26; O.</fo:block>
             </fo:table-cell>
           </fo:table-row>                    
        </fo:table-body>
       </fo:table>
       </fo:block>
        --%>
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
