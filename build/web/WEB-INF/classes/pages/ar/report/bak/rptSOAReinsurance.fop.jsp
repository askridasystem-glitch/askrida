<%@ page import="com.crux.util.fop.FOPTableSource,
                 com.crux.common.model.HashDTO,
                 com.crux.util.*,
                 java.util.Date,
                 com.webfin.ar.forms.FRRPTrptSOAInsuranceForm"%><?xml version="1.0" encoding="utf-8"?>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
<%
   final FRRPTrptSOAInsuranceForm form = (FRRPTrptSOAInsuranceForm) request.getAttribute("FORM");

   //Date policydatefrom = (Date) form.getAttribute("policydatefrom");
   //Date policydateto = (Date) form.getAttribute("policydateto");
   String entity = (String) form.getAttribute("entity");
   String customer_desc = (String) form.getAttribute("customer_desc");
   String entity_desc = (String) form.getAttribute("entity_desc");
   String treatytype = (String) form.getAttribute("treatytype");
   String periodFrom = (String) form.getAttribute("periodFrom");
   String yearFrom = (String) form.getAttribute("yearFrom");
   String period_desc = (String) form.getAttribute("period_desc");

%>

  <!-- defines page layout -->
  <fo:layout-master-set>

    <!-- layout for the first page -->
    <fo:simple-page-master master-name="only"
                  page-width="25cm"
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
      <fo:block text-align="end"
            font-size="10pt"
            font-family="serif"
            line-height="14pt" >
        Page <fo:page-number/>
      </fo:block>
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">
		
	  
      <fo:block font-size="13pt" line-height="12pt" text-align="center">Technical Account Statement</fo:block>
     
     
       <% if(periodFrom!=null){%>
      <fo:block font-size="12pt" line-height="12pt" text-align="center">Account Period : <%=JSPUtil.printNVL(periodFrom,"ALL")%>/<%=JSPUtil.printNVL(yearFrom,"ALL")%></fo:block>
      <% } %>
      <% if(entity_desc!=null){%>
      <fo:block font-size="12pt" line-height="12pt" text-align="center">In Account With : <%=JSPUtil.printNVL(entity_desc,"ALL")%></fo:block>
      <% } %>
      <% if(customer_desc!=null){%>
      <fo:block font-size="12pt" line-height="12pt" text-align="center">{L-ENGCustomer-L}{L-INACustomer-L} : <%=JSPUtil.printNVL(customer_desc,"ALL")%></fo:block>
      <% } %>
       <% if(period_desc!=null){%>
      <fo:block font-size="12pt" line-height="12pt" text-align="center">{L-ENGMonth-L}{L-INABulan-L} : <%=JSPUtil.printNVL(period_desc,"ALL")%></fo:block>
      <% } %>
       <% if(yearFrom!=null){%>
      <fo:block font-size="12pt" line-height="12pt" text-align="center">{L-ENGYear-L}{L-INATahun-L} : <%=JSPUtil.printNVL(yearFrom,"ALL")%></fo:block>
      <% } %>
      <fo:block font-size="8pt" line-height="20pt" ></fo:block>

      <!-- defines text title level 1-->
      <fo:block font-size="7pt" line-height="8pt" >
<%

   SQLAssembler sqa = new SQLAssembler();

   sqa.addSelect(
           "l.group_name,a.pol_no,a.policy_date,b.ent_name,j.treaty_type,k.ent_name as reasuradur,e.description,"+
			"d.cover_category,d.ins_cover_id,a.ccy,coalesce(((d.premi_new/a.premi_total)*i.premi_amount),0) as premi_share,"+
			"coalesce(((d.premi_new/a.premi_total)*i.ricomm_amt),0) as ricomm_share,"+
			"coalesce(((d.premi_new/a.premi_total)*i.claim_amount),0) as riclaim_share,"+
			"(((coalesce(((d.premi_new/a.premi_total)*i.premi_amount),0)) - (coalesce(((d.premi_new/a.premi_total)*i.ricomm_amt),0)))-coalesce((coalesce(((d.premi_new/a.premi_total)*i.claim_amount),0)),0)) as balance");
   sqa.addQuery(
           "   from ins_policy a"+
			"	left join ent_master b on b.ent_id = a.entity_id"+
			"	inner join ins_pol_obj c on c.pol_id=a.pol_id"+
			"	inner join ins_pol_cover d on a.pol_id = d.pol_id"+
			"	inner join ins_cover e on d.ins_cover_id = e.ins_cover_id"+
			"	inner join ins_policy_types f on a.pol_type_id = f.pol_type_id"+
			"	inner join ins_pol_treaty g on g.ins_pol_obj_id = c.ins_pol_obj_id"+
			"	inner join ins_pol_treaty_detail h on g.ins_pol_treaty_id = h.ins_pol_treaty_id"+
			"	inner join ins_pol_ri i on i.ins_pol_tre_det_id = h.ins_pol_tre_det_id"+
			"	inner join ins_treaty_detail j on j.ins_treaty_detail_id = h.ins_treaty_detail_id"+
			"	inner join ent_master k on k.ent_id = i.member_ent_id"+
			"	inner join ins_policy_type_grp l on l.ins_policy_type_grp_id = f.ins_policy_type_grp_id");
   sqa.addClause("a.status IN (?,?,?)");
   sqa.addPar("POLICY");
   sqa.addPar("CLAIM");
   sqa.addPar("ENDORSE");
   
   sqa.addClause("a.effective_flag = ?");
   sqa.addPar("Y");
   
   sqa.addOrder(" a.pol_no asc,d.ins_pol_cover_id");


   if (treatytype!=null) {
      sqa.addClause("j.treaty_type=?");
      sqa.addPar(treatytype);
   }

   if (entity!=null) {
      sqa.addClause("k.ent_id=?");
      sqa.addPar(entity);
   }
   
   if (periodFrom!=null) {
      sqa.addClause(" substr(a.policy_date,6,2) like ?");
      sqa.addPar(periodFrom);
   }
   
   if (yearFrom!=null) {
      sqa.addClause(" substr(a.policy_date,1,4) like ?");
      sqa.addPar(yearFrom);
   }

   /*
   if (policydatefrom!=null) {
      sqa.addClause("a.policy_date>=?");
      sqa.addPar(DateUtil.dateBracketLow(policydatefrom));
   }

   if (policydateto!=null) {
      sqa.addClause("a.policy_date<?");
      sqa.addPar(DateUtil.dateBracketHigh(policydateto));
   }*/


   final DTOList r = ListUtil.getDTOListFromQuery(sqa.getSQL(),  sqa.getPar(), HashDTO.class);

   HashDTO tot = new HashDTO();

   tot.setFieldValueByFieldName("no", "");
   tot.setFieldValueByFieldName("premi_share", r.getTotal("premi_share"));
   tot.setFieldValueByFieldName("ricomm_share", r.getTotal("ricomm_share"));
   tot.setFieldValueByFieldName("riclaim_share", r.getTotal("riclaim_share"));
   tot.setFieldValueByFieldName("balance",r.getTotal("balance"));
   tot.setFieldValueByFieldName("group_name", "TOTAL");

   r.add(tot);

   final String [] columnTitles={
      "No",
      "Class Of Bussiness",
      "CCY",
      "Tanggal",
      "Tipe Treaty",
      "Premi R/I",
      "Komisi R/I",
      "Klaim",
      "Balance",
   };


   final FOPTableSource fopTableSource = new FOPTableSource(
           9,
           new int [] {3,10,2,5,5,8,8,8,8},
           20,
           "cm"
   ) {
      public int getRowCount() {
         return r.size();
      }

      public Object getColumnValue(int rowNo, int columnNo) {
         HashDTO h = (HashDTO) r.get(rowNo);
         switch(columnNo) {
            case 0: return h.getFieldValueByFieldNameST("no")==null?String.valueOf(rowNo+1):"";
            case 1: return h.getFieldValueByFieldNameST("group_name");
            case 2: return JSPUtil.printX(h.getFieldValueByFieldNameST("ccy"));
            case 3: return h.getFieldValueByFieldNameDT("policy_date");
            case 4: return h.getFieldValueByFieldNameST("treaty_type");
            case 5: return h.getFieldValueByFieldNameBD("premi_share");
            case 6: return h.getFieldValueByFieldNameBD("ricomm_share");
            case 7: return h.getFieldValueByFieldNameBD("riclaim_share");
            case 8: return h.getFieldValueByFieldNameBD("balance");
         }
         return "?";
      }

      public String getColumnHeader(int columnNo) {
         return columnTitles[columnNo];
      }

      public String getColumnAlign(int rowNo, int columnNo) {
         switch(columnNo) {
            case 5:
            case 6:
            case 7:
            case 8:
               return "end";
         };

         return super.getColumnAlign(rowNo, columnNo);    //To change body of overridden methods use File | Settings | File Templates.
      }
   };

   fopTableSource.display(out);
%>
       </fo:block>
    </fo:flow>
  </fo:page-sequence>
</fo:root>
