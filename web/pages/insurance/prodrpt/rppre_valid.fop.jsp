<%@ page import="com.crux.util.fop.FOPTableSource,
com.webfin.insurance.form.ProductionReinsuranceReportForm,
com.crux.common.model.HashDTO,
com.crux.util.*,
java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <%
    final ProductionReinsuranceReportForm form = (ProductionReinsuranceReportForm) request.getAttribute("FORM");
    %>
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-width="35cm"
                               page-height="21cm"
                               margin-top="0.5cm"
                               margin-bottom="1cm"
                               margin-left="0.75cm"
                               margin-right="0.75cm">
            <fo:region-body margin-top="2cm" margin-bottom="1cm"/>
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
                      font-family="TAHOMA"
                      line-height="12pt" 
                      font-style="bold">
                rppre_valid - PT. Asuransi Bangun Askrida
            </fo:block>
            <fo:block text-align="end"
                      font-size="8pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>
        
        <fo:flow flow-name="xsl-region-body">
            
            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="330mm"/>
                    <fo:table-body>
                        
                        
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block font-size="12pt" line-height="12pt" text-align="center" space-after.optimum="5pt">	 
                                    VALIDASI SPREADING OF RISK 1
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
                                <fo:block font-size="12pt"><% if(form.getStPolicyTypeDesc()!=null) {%> 
                                    Jenis Pertanggungan : 
                                    <%= JSPUtil.printX(form.getStPolicyTypeGroupDesc())%>
                                    <% }else{ %>
                                    <%= JSPUtil.printX(form.getStPolicyTypeGroupDesc())%><% } %>
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
                
                sqa.addSelect("	substr(a.period_start::text,1,4) as u_year,a.pol_no,a.policy_date,a.cust_name,a.insured_amount,"+
                "	round(sum(checkreas(j.treaty_type='OR',i.tsi_amount)),2) as tsi_or,"+
                "	round(sum(checkreas(j.treaty_type='BPDAN',i.tsi_amount)),2) as tsi_bppdan,"+
                "	round(sum(checkreas(j.treaty_type='PARK',i.tsi_amount)),2) as tsi_maipark,"+
                "	round(sum(checkreas(j.treaty_type='FAC',i.tsi_amount)),2) as tsi_fac,"+
                "	a.insured_amount-(round(sum(checkreas(j.treaty_type='OR',i.tsi_amount)),2)+round(sum(checkreas(j.treaty_type='BPDAN',i.tsi_amount)),2)+round(sum(checkreas(j.treaty_type='QS',i.tsi_amount)),2)+round(sum(checkreas(j.treaty_type='SPL',i.tsi_amount)),2)+round(sum(checkreas(j.treaty_type='PARK',i.tsi_amount)),2)+round(sum(checkreas(j.treaty_type='FAC',i.tsi_amount)),2)+round(sum(checkreas(j.treaty_type='FACO',i.tsi_amount)),2)+round(sum(checkreas(j.treaty_type='FACP',i.tsi_amount)),2)) as valid_tsi"
                );
                
                sqa.addQuery("from ins_policy a "+
                "left join ent_master b on b.ent_id = a.entity_id "+
                "inner join ins_pol_obj c on c.pol_id = a.pol_id "+
                "inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id "+
                "inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id "+
                "inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id "+
                "inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id"
                );
                
                sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL')");
                
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
                
                if (form.getStPolicyTypeID()!=null) {
                sqa.addClause("a.pol_type_id = ?");
                sqa.addPar(form.getStPolicyTypeID());
                }
                
                if (form.getStPolicyTypeGroupID()!=null) {
                sqa.addClause("a.ins_policy_type_grp_id = ?");
                sqa.addPar(form.getStPolicyTypeGroupID());
                }   	
                
                if(form.getStBranch()!=null) {
                sqa.addClause("a.cc_code = ?");
                sqa.addPar(form.getStBranch());
                }
                
                String q = "select * from ( "+sqa.getSQL()+" group by substr(a.period_start::text,1,4),a.pol_no,a.policy_date,a.cust_name,a.insured_amount "+
                "order by a.pol_no ) x where valid_tsi <> 0";
                
                final DTOList r = ListUtil.getDTOListFromQuery(q,  sqa.getPar(), HashDTO.class);
                
                /*
                HashDTO tot = new HashDTO();
                
                tot.setFieldValueByFieldName("no", "");
                
                tot.setFieldValueByFieldName("insured_amount", r.getTotal("premi_amt"));
                tot.setFieldValueByFieldName("tsi_or", r.getTotal("tsi_or"));
                tot.setFieldValueByFieldName("tsi_bppdan", r.getTotal("tsi_bppdan"));
                tot.setFieldValueByFieldName("tsi_maipark", r.getTotal("tsi_maipark"));
                tot.setFieldValueByFieldName("tsi_fac", r.getTotal("tsi_fac"));
                tot.setFieldValueByFieldName("cust_name", "TOTAL");
                
                r.add(tot);
                */
                
                final FOPTableSource fopTableSource = new FOPTableSource(
                11,
                new int [] {3,5,11,6,20,13,13,13,13,13,13},
                33,
                "cm"
                ) {
                public int getRowCount() {
                return r.size();
                }
                
                public Object getColumnValue(int rowNo, int columnNo) {
                HashDTO h = (HashDTO) r.get(rowNo);
                switch(columnNo) {
                case 0: return h.getFieldValueByFieldNameST("no")==null?String.valueOf(rowNo+1):"";
                case 1: return h.getFieldValueByFieldNameST("u_year");
                case 2: return h.getFieldValueByFieldNameST("pol_no");
                case 3: return h.getFieldValueByFieldNameDT("policy_date");            
                case 4: return h.getFieldValueByFieldNameST("cust_name");
                case 5: return h.getFieldValueByFieldNameBD("insured_amount");
                case 6: return h.getFieldValueByFieldNameBD("tsi_or");
                case 7: return h.getFieldValueByFieldNameBD("tsi_bppdan");
                case 8: return h.getFieldValueByFieldNameBD("tsi_maipark");
                case 9: return h.getFieldValueByFieldNameBD("tsi_fac");
                case 10: return h.getFieldValueByFieldNameBD("valid_tsi");
                }
                return "?";
                }
                
                public String getColumnHeader(int columnNo) {
                switch(columnNo) {
                case 0: return "No";
                case 1: return "Tahun Under.";
                case 2: return "No. Polis";
                case 3: return "Tanggal Polis";
                case 4: return "Nama Tertanggung";
                case 5: return "TSI";
                case 6: return "TSI OR";
                case 7: return "TSI BPPDAN";
                case 8: return "TSI MAIPARK";
                case 9: return "TSI FAC";
                case 10: return "FULL PLACED";
                }
                return "?";
                }
                
                public String getColumnAlign(int rowNo, int columnNo) {
                switch(columnNo) {
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                return "end";
                };
                
                return super.getColumnAlign(rowNo, columnNo);    //To change body of overridden methods use File | Settings | File Templates.
                }
                };
                
                fopTableSource.display(out);
                %>
            </fo:block>
            
            <fo:block font-size="6pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
            </fo:block> 
            
            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="230mm"/>
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
