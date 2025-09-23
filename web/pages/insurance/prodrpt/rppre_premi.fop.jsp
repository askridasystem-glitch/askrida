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
                               page-width="21cm"
                               page-height="29.7cm"
                               margin-top="0.5cm"
                               margin-bottom="1cm"
                               margin-left="1.5cm"
                               margin-right="1.5cm">
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
                      font-family="TAHOMA"
                      line-height="12pt" 
                      font-style="bold">
                rppre_premi - PT. Asuransi Bangun Askrida
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
                    <fo:table-column column-width="180mm"/>
                    <fo:table-body>
                        
                        
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block font-size="12pt" line-height="12pt" text-align="center" space-after.optimum="5pt">	 
                                    PEMBAGIAN PREMI KEPADA REINSURER BULAN <%=DateUtil.getDateStr(form.getPolicyDateFrom(),"^^ ").toUpperCase()%> <%=DateUtil.getYear(form.getPolicyDateFrom())%>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block font-size="12pt" line-height="12pt" text-align="center" space-after.optimum="10pt">	 
                                    PERIODE : <% if(form.getPeriodFrom()!=null) { %>
                                    <%=DateUtil.getYear(form.getPeriodFrom())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block>	 
                                    Jenis Pertanggungan : <% if(form.getStPolicyTypeDesc()!=null) {%>
                                    <%= JSPUtil.printX(form.getStPolicyTypeGroupDesc())%>
                                    <% }else{ %>
                                    <%= JSPUtil.printX(form.getStPolicyTypeGroupDesc())%><% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block>	 
                                    Treaty : <% if(form.getStFltTreatyTypeDesc()!=null) { %>
                                    <%=JSPUtil.printX(form.getStFltTreatyTypeDesc())%><% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell>
                                <fo:block>	 
                                    <% if(form.getStCustCategory1Desc()!=null) { %>
                                    <%=JSPUtil.printX(form.getStCustCategory1Desc())%><% } %>
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
                
                sqa.addSelect("f.ent_name as cust_name,substr(a.period_start::text,1,4) as years,substr(i.treaty_name,1,11) as treaty_name,e.sharepct as saham,sum(e.premi_amount*a.ccy_rate) as premi_amt,e.ricomm_rate as persen, "+
                        "sum(e.ricomm_amt*a.ccy_rate) as komisi,sum((e.premi_amount*a.ccy_rate)-(e.ricomm_amt*a.ccy_rate)) as netto ");
                
                sqa.addQuery("   from ins_policy a "+
                        "	inner join ins_pol_obj b on b.pol_id = a.pol_id "+
                        "	inner join ins_pol_treaty c on c.ins_pol_obj_id = b.ins_pol_obj_id "+
                        "	inner join ins_pol_treaty_detail d on d.ins_pol_treaty_id = c.ins_pol_treaty_id "+
                        "	inner join ins_pol_ri e on e.ins_pol_tre_det_id = d.ins_pol_tre_det_id "+
                        "	inner join ins_policy_types h on h.pol_type_id = a.pol_type_id "+
                        "	inner join ent_master f on f.ent_id = e.member_ent_id "+
                        "	inner join ins_treaty_detail g on g.ins_treaty_detail_id = e.ins_treaty_detail_id "+
                        "	inner join ins_treaty i on i.ins_treaty_id = c.ins_treaty_id "+
                        "	left join ent_master j on j.ent_id = a.entity_id"
                        );
                
                sqa.addClause("a.status in ('POLICY','ENDORSE','RENEWAL','ENDORSE RI')");
                
                sqa.addClause("g.treaty_type not in ('XOL1','XOL2','XOL3','XOL4','XOL5','OR')");
                
                sqa.addClause("a.effective_flag='Y'");
                
                
                if (form.getStPolicyTypeID()!=null) {
                    sqa.addClause("h.pol_type_id = ?");
                    sqa.addPar(form.getStPolicyTypeID());
                }
                
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
                
                if (form.getStPolicyTypeGroupID()!=null) {
                    sqa.addClause("h.ins_policy_type_grp_id = ?");
                    sqa.addPar(form.getStPolicyTypeGroupID());
                }
                
                if(form.getStBranch()!=null) {
                    sqa.addClause("a.cc_code = ?");
                    sqa.addPar(form.getStBranch());
                }
                
                if(form.getStFltTreatyType()!=null) {
                    sqa.addClause("g.treaty_type = ?");
                    sqa.addPar(form.getStFltTreatyType());
                }
                
                if(form.getStCustCategory1()!=null) {
                    sqa.addClause("j.category1 = ?");
                    sqa.addPar(form.getStCustCategory1());
                }
                
                String q = "select years,cust_name,saham,sum(premi_amt) as premi_amt, persen,sum(komisi) as komisi,sum(netto) as netto from ( "+
                        sqa.getSQL()+" group by substr(a.period_start::text,1,4),f.ent_name,substr(i.treaty_name,1,11),e.sharepct,e.ricomm_rate "+
                        ") x group by years,cust_name,saham,persen" ;
                
                final DTOList r = ListUtil.getDTOListFromQuery(q, sqa.getPar(), HashDTO.class);
                
                HashDTO tot = new HashDTO();
                
                tot.setFieldValueByFieldName("no", "");
                tot.setFieldValueByFieldName("premi_amt", r.getTotal("premi_amt"));
                tot.setFieldValueByFieldName("komisi", r.getTotal("komisi"));
                tot.setFieldValueByFieldName("netto", r.getTotal("netto"));
                tot.setFieldValueByFieldName("cust_name", "TOTAL");
                tot.setFieldValueByFieldName("years", " ");
                
                r.add(tot);
                
                
                final FOPTableSource fopTableSource = new FOPTableSource(
                        8,
                        new int [] {4,20,10,5,12,5,12,12},
                        17.3,
                        "cm"
                        ) {
                    public int getRowCount() {
                        return r.size();
                    }
                    
                    public Object getColumnValue(int rowNo, int columnNo) {
                        HashDTO h = (HashDTO) r.get(rowNo);
                        switch(columnNo) {
                            case 0: return h.getFieldValueByFieldNameST("no")==null?String.valueOf(rowNo+1):"";
                            case 1: return h.getFieldValueByFieldNameST("cust_name");
                            case 2: return "Treaty "+h.getFieldValueByFieldNameST("years");
                            case 3: return h.getFieldValueByFieldNameBD("saham");
                            case 4: return h.getFieldValueByFieldNameBD("premi_amt");
                            case 5: return h.getFieldValueByFieldNameBD("persen");
                            case 6: return h.getFieldValueByFieldNameBD("komisi");
                            case 7: return h.getFieldValueByFieldNameBD("netto");
                        }
                        return "?";
                    }
                    
                    public String getColumnHeader(int columnNo) {
                        switch(columnNo) {
                            case 0: return "No";
                            case 1: return "Reinsurer";
                            case 2: return "Tahun Under.";
                            case 3: return "Saham";
                            case 4: return "Premi Reasuransi";
                            case 5: return "Komisi Reas (%)";
                            case 6: return "Jumlah Komisi";
                            case 7: return "Netto";
                        }
                        return "?";
                    }
                    
                    public String getColumnAlign(int rowNo, int columnNo) {
                        switch(columnNo) {
                            case 4:
                            case 6:
                            case 7:
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
