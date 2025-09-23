<%@ page import="com.crux.util.fop.FOPTableSource,
                 com.crux.common.model.HashDTO,
                 com.crux.util.*,
                 com.crux.web.controller.SessionManager, 
                 java.util.Date,
                 com.webfin.insurance.form.ProductionReportForm"%><?xml version="1.0" encoding="utf-8"?>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
<%
   final ProductionReportForm form = (ProductionReportForm)SessionManager.getInstance().getCurrentForm(); 

%>

  <!-- defines page layout -->
  <fo:layout-master-set>

    <!-- layout for the first page -->
    <fo:simple-page-master master-name="only"
                  page-width="27cm"
                  page-height="21cm"
                  margin-top="0.5cm"
                  margin-bottom="0.5cm"                                                                                                                                                                           
                  margin-left="1.5cm"
                  margin-right="1.5cm">
      <fo:region-body margin-top="2cm" margin-bottom="0.5cm"/>
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
    </fo:static-content>
    
    <fo:static-content flow-name="xsl-region-after">
    <fo:block text-align="end"
      font-size="8pt" font-family="serif" line-height="1em + 2pt">
      {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
ref-id="end-of-document"/>
      </fo:block>
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">
		
	  
      <fo:block font-size="13pt" line-height="12pt" text-align="center">{L-ENG Claim Coinsurance Report-L}{L-INA Laporan Produksi Klaim Koasuransi-L}</fo:block>
     
     
      <fo:block font-size="14pt" text-align="center">
       <% if (form.getPolicyDateFrom()!=null || form.getPolicyDateTo()!=null) {%> 
         Tanggal Polis : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
       <% } %>
      </fo:block> 

      <fo:block font-size="14pt" text-align="center">
       <% if (form.getPeriodFrom()!=null || form.getPeriodTo()!=null) {%> 
         Periode : <%=JSPUtil.printX(form.getPeriodFrom())%> S/D <%=JSPUtil.printX(form.getPeriodTo())%>
       <% } %>
      </fo:block>
      
      <fo:block font-size="14pt" text-align="center">
       <% if (form.getEntryDateFrom()!=null || form.getEntryDateTo()!=null) {%> 
         Tanggal Entry : <%=JSPUtil.printX(form.getEntryDateFrom())%> S/D <%=JSPUtil.printX(form.getEntryDateTo())%>
       <% } %>
      </fo:block>  
      
      <fo:block font-size="14pt">
       <% if (form.getStBranchDesc()!=null) {%> 
         Cabang : <%=JSPUtil.printX(form.getStBranchDesc())%>  
       <% } %>
      </fo:block>
  
      <fo:block font-size="14pt">
       <% if (form.getStPolicyTypeDesc()!=null) {%> 
         Jenis Polis : <%=JSPUtil.printX(form.getStPolicyTypeDesc())%>  
       <% } %>
      </fo:block>  

      <fo:block font-size="14pt">
       <% if (form.getStFltCoverTypeDesc()!=null) {%> 
         Jenis Penutupan  : <%=JSPUtil.printX(form.getStFltCoverTypeDesc())%>  
       <% } %>
      </fo:block>  
  
      <fo:block font-size="14pt">
       <% if (form.getStCustCategory1Desc()!=null) {%> 
         Sumber Bisnis : <%=JSPUtil.printX(form.getStCustCategory1Desc())%>  
       <% } %>
      </fo:block>  
  
      <fo:block font-size="14pt">
       <% if (form.getStRiskLocation()!=null) {%> 
         Risk Location :<%=JSPUtil.printX(form.getStRiskLocation())%>  
       <% } %>
      </fo:block>  

      <fo:block font-size="14pt">
       <% if (form.getStPostCode()!=null) {%> 
         Post Code :<%=JSPUtil.printX(form.getStPostCode())%>  
       <% } %>
      </fo:block>  

      <fo:block font-size="14pt">
       <% if (form.getStEntityName()!=null) {%> 
         Customer Name :<%=JSPUtil.printX(form.getStEntityName())%>  
       <% } %>
      </fo:block>  

      <fo:block font-size="14pt">
       <% if (form.getStRiskCardNo()!=null) {%> 
         Risk Card No. :<%=JSPUtil.printX(form.getStRiskCardNo())%>  
       <% } %>
      </fo:block>  

      <fo:block font-size="14pt">
       <% if (form.getStRiskCode()!=null) {%> 
         Risk Code :<%=JSPUtil.printX(form.getStRiskCode())%>  
       <% } %>
      </fo:block> 

      <!-- defines text title level 1-->
      <fo:block font-size="7pt" line-height="8pt" >
<%

/*
select l.group_name,a.pol_no,a.policy_date,b.ent_name,h.ent_name as asuradur,
g.share_pct,g.premi_amt,coalesce(g.comm_amount,0),a.create_who
from ins_policy a
left join ent_master b on b.ent_id = a.entity_id
inner join ins_pol_coins g on g.policy_id = a.pol_id
inner join ent_master h on h.ent_id = g.entity_id
inner join ins_pol_obj c on c.pol_id=a.pol_id
inner join ins_policy_types f on a.pol_type_id = f.pol_type_id 
inner join ins_policy_type_grp l on l.ins_policy_type_grp_id = f.ins_policy_type_grp_id
where a.status IN ('POLICY','RENEWAL','ENDORSE') and a.effective_flag = 'Y' 
and a.cover_type_code not in('DIRECT')
--and k.ent_name like 'REASURANSI INTERNASIONAL INDONESIA'
--and substr(a.policy_date,6,2) like '06' and substr(a.policy_date,1,4) like '2009'
order by a.pol_no asc,g.ins_pol_coins_id


*/


   SQLAssembler sqa = new SQLAssembler();

   sqa.addSelect(
           " l.group_name,a.pol_no,a.policy_date,b.ent_name,h.ent_name as asuradur,"+
           " g.share_pct,g.claim_amt,coalesce(g.comm_amount,0) as comm_amount,a.create_who");   
    sqa.addQuery(
           	" from ins_policy a"+
			" left join ent_master b on b.ent_id = a.entity_id"+
			"   inner join ins_pol_coins g on g.policy_id = a.pol_id"+
			"   inner join ent_master h on h.ent_id = g.entity_id"+
			"   inner join ins_pol_obj c on c.pol_id=a.pol_id"+
			"   inner join ins_policy_types f on a.pol_type_id = f.pol_type_id"+
		    "   inner join ins_policy_type_grp l on l.ins_policy_type_grp_id = f.ins_policy_type_grp_id");   
   sqa.addClause("a.status IN (?)");
   sqa.addPar("CLAIM");
   
   sqa.addClause("a.effective_flag = ?");
   sqa.addPar("Y");
   
   sqa.addClause("a.cover_type_code not in(?)");
   sqa.addPar("DIRECT");
   
   sqa.addOrder(" a.pol_no asc,g.ins_pol_coins_id");


   
   if (form.getPolicyDateFrom()!=null) {
      sqa.addClause("a.policy_date>=?");
      sqa.addPar(DateUtil.dateBracketLow(form.getPolicyDateFrom()));
   }

   if (form.getPolicyDateTo()!=null) {
      sqa.addClause("a.policy_date<?");
      sqa.addPar(DateUtil.dateBracketHigh(form.getPolicyDateTo()));
   }
   
      if (form.getPeriodFrom()!=null) {
         sqa.addClause("a.period_start>=?");
         sqa.addPar(form.getPeriodFrom());
      }

      if (form.getPeriodTo()!=null) {
         sqa.addClause("a.period_start<=?");
         sqa.addPar(form.getPeriodTo());
      }
      
      if (form.getEntryDateFrom()!=null) {
         sqa.addClause("a.create_date>=?");
         sqa.addPar(form.getEntryDateFrom());
      }

      if (form.getEntryDateTo()!=null) {
         sqa.addClause("a.create_date<=?");
         sqa.addPar(form.getEntryDateTo());
      }

      if (form.getExpirePeriodFrom()!=null) {
         sqa.addClause("a.period_end>=?");
         sqa.addPar(form.getExpirePeriodFrom());
      }

      if (form.getStPolicyTypeID()!=null) {
         sqa.addClause("a.pol_type_id=?");
         sqa.addPar(form.getStPolicyTypeID());
      }

      if (form.getExpirePeriodTo()!=null) {
         sqa.addClause("a.period_end<=?");
         sqa.addPar(form.getExpirePeriodTo());
      }

      if (form.getStBranch()!=null) {
         sqa.addClause("a.cc_code=?");
         sqa.addPar(form.getStBranch());
      }

      if(form.getStFltCoverType()!=null) {
         sqa.addClause("a.cover_type_code = ?");
         sqa.addPar(form.getStFltCoverType());
      }

      if (form.getStCustCategory1()!=null){
         sqa.addClause("c.category1 = ?");
         sqa.addPar(form.getStCustCategory1());
      }

      if (form.getStEntityID()!=null) {
         sqa.addClause("a.entity_id = ?");
         sqa.addPar(form.getStEntityID());
      }

      if (form.getStPolicyNo()!=null) {
         sqa.addClause("a.pol_no like ?");
         sqa.addPar('%'+form.getStPolicyNo()+'%');
      }


   final DTOList r = ListUtil.getDTOListFromQuery(sqa.getSQL(),  sqa.getPar(), HashDTO.class);

   HashDTO tot = new HashDTO();

   tot.setFieldValueByFieldName("no", "");
   tot.setFieldValueByFieldName("premi_amt", r.getTotal("premi_amt"));
   tot.setFieldValueByFieldName("comm_amount", r.getTotal("comm_amount"));
   
   r.add(tot);

   final String [] columnTitles={
      "No",
      "Policy Date",
      "Policy No",
      "Insurer ",
      "Coins Share ",
      "Claim Coins",
      "Commission Coins",
   };


   final FOPTableSource fopTableSource = new FOPTableSource(
           7,
           new int [] {3,5,8,13,8,8,10,},
           23,
           "cm"
   ) {
      public int getRowCount() {
         return r.size();
      }

      public Object getColumnValue(int rowNo, int columnNo) {
         HashDTO h = (HashDTO) r.get(rowNo);
         switch(columnNo) {
            case 0: return h.getFieldValueByFieldNameST("no")==null?String.valueOf(rowNo+1):"";
            case 1: return h.getFieldValueByFieldNameDT("policy_date");
            case 2: return JSPUtil.printX(h.getFieldValueByFieldNameST("pol_no"));
            case 3: return h.getFieldValueByFieldNameST("asuradur");
            case 4: return h.getFieldValueByFieldNameBD("share_pct");
            case 5: return h.getFieldValueByFieldNameBD("claim_amt");
            case 6: return h.getFieldValueByFieldNameBD("comm_amount");
         }
         return "?";
      }

      public String getColumnHeader(int columnNo) {
         return columnTitles[columnNo];
      }

      public String getColumnAlign(int rowNo, int columnNo) {
         switch(columnNo) {
            case 4:
            case 5:
            case 6:
               return "end";
         };

         return super.getColumnAlign(rowNo, columnNo);    //To change body of overridden methods use File | Settings | File Templates.
      }
   };

   fopTableSource.display(out);
%>
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
