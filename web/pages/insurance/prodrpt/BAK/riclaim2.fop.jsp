<%@ page import="com.crux.util.fop.FOPTableSource,
                 com.webfin.insurance.form.ProductionReportForm,
                 com.crux.common.model.HashDTO,
                 com.crux.util.*,
                 java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
<%
   final ProductionReportForm form = (ProductionReportForm) request.getAttribute("FORM");
%>

  <!-- defines page layout -->
  <fo:layout-master-set>

    <!-- layout for the first page -->
    <fo:simple-page-master master-name="only"
                  page-width="21cm"
                  page-height="29.7cm"
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
<fo:block text-align="end"
      font-size="8pt" font-family="serif" line-height="1em + 2pt">
      {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
ref-id="end-of-document"/>
      </fo:block>
    </fo:static-content>
    
   


    <fo:flow flow-name="xsl-region-body">

      <fo:block font-size="12pt" line-height="12pt" text-align="center">Reinsurance Premium for <%=form.getStPolicyTypeID()==null?"ALL":form.getStPolicyTypeDesc()%> policy type</fo:block>
      <fo:block font-size="12pt" line-height="12pt" text-align="center">for <%=form.getStFltTreatyType()==null?"ALL":form.getStFltTreatyTypeDesc()%> Treaty</fo:block>
      <fo:block font-size="12pt" line-height="12pt" text-align="center">from <%=JSPUtil.printNVL(form.getPolicyDateFrom(),"prehistoric")%> up to <%=JSPUtil.printNVL(form.getPolicyDateTo(),new Date())%></fo:block>
      <fo:block font-size="12pt" line-height="12pt" text-align="center">for <%=JSPUtil.printNVL(form.getStEntityName(),"ALL")%> Reinsurance Company</fo:block>

      <fo:block font-size="8pt" line-height="20pt" ></fo:block>

      <!-- defines text title level 1-->
      <fo:block font-size="8pt" line-height="10pt" >
<%

   SQLAssembler sqa = new SQLAssembler();

   sqa.addSelect(
           "f.pol_no, coalesce(d.short_name, d.ent_name) as ent_name, a.claim_amount, a.ricomm_amt as comission, a.premi_amount-ricomm_amt as netto");
   sqa.addQuery(
           "   from ins_pol_ri a\n" +
           "      inner join ins_treaty_detail b on b.ins_treaty_detail_id=a.ins_treaty_detail_id\n" +
           "      inner join ins_pol_treaty e on e.ins_pol_treaty_id=a.ins_pol_treaty_id" +
           "      inner join ins_pol_obj g on g.ins_pol_obj_id=e.ins_pol_obj_id" +
           "      inner join ins_policy f on f.pol_id=g.pol_id and (f.status='CLAIM') and f.posted_flag='Y' " +
           "      inner join ins_policy_types c on c.pol_type_id=b.policy_type_id\n" +
           "      inner join ent_master d on d.ent_id = f.entity_id");

   if (form.getStPolicyTypeID()!=null) {
      sqa.addClause("b.policy_type_id=?");
      sqa.addPar(form.getStPolicyTypeID());
   }

   if (form.getPolicyDateFrom()!=null) {
      sqa.addClause("f.policy_date>=?");
      sqa.addPar(DateUtil.dateBracketLow(form.getPolicyDateFrom()));

      sqa.addClause("f.policy_date<?");
      sqa.addPar(DateUtil.dateBracketHigh(form.getPolicyDateTo()));
   }

   if (form.getStFltTreatyType()!=null) {
      sqa.addClause("b.treaty_type=?");
      sqa.addPar(form.getStFltTreatyType());
   }

   if (form.getStEntityID()!=null) {
      sqa.addClause("a.member_ent_id=?");
      sqa.addPar(form.getStEntityID());
   }

   String q =
           "select pol_no, ent_name, sum(claim_amount) as claim_amount, sum(comission) as comission, sum(netto) as netto from (" +
           sqa.getSQL()+
           ") a group by pol_no, ent_name";



   final DTOList r = ListUtil.getDTOListFromQuery(q,  sqa.getPar(), HashDTO.class);

   HashDTO tot = new HashDTO();

   tot.setFieldValueByFieldName("no", "");
   tot.setFieldValueByFieldName("claim_amount", r.getTotal("claim_amount"));
   tot.setFieldValueByFieldName("comission", r.getTotal("comission"));
   tot.setFieldValueByFieldName("netto", r.getTotal("netto"));
   tot.setFieldValueByFieldName("description", "TOTAL");

   r.add(tot);


   final FOPTableSource fopTableSource = new FOPTableSource(
           4,
           new int [] {4,10,30,10},
           16.7,
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
            case 2: return h.getFieldValueByFieldNameST("ent_name");
            case 3: return h.getFieldValueByFieldNameBD("claim_amount");
            case 4: return h.getFieldValueByFieldNameBD("comission");
            case 5: return h.getFieldValueByFieldNameBD("netto");
         }
         return "?";
      }

      public String getColumnHeader(int columnNo) {
         switch(columnNo) {
            case 0: return "No";
            case 1: return "Policy No";
            case 2: return "Name of Insured";
            case 3: return "Claim";
            case 4: return "Comission";
            case 5: return "Nett";
         }
         return "?";
      }

      public String getColumnAlign(int rowNo, int columnNo) {
         switch(columnNo) {
            case 3:
            case 4:
            case 5:
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
