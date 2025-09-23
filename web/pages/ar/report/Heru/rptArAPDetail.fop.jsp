<%@ page import="com.crux.util.fop.FOPTableSource,
                 com.crux.common.model.HashDTO,
                 com.crux.util.*,
                 java.util.Date,
                 com.webfin.ar.forms.FRRPTrptArAPDetailForm"%><?xml version="1.0" encoding="utf-8"?>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
<%
   final FRRPTrptArAPDetailForm form = (FRRPTrptArAPDetailForm) request.getAttribute("FORM");

   String policyno = (String) form.getAttribute("policyno");
   Date policydatefrom = (Date) form.getAttribute("policydatefrom");
   Date policydateto = (Date) form.getAttribute("policydateto");
   String entity = (String) form.getAttribute("entity");
   String customer = (String) form.getAttribute("customer");
   String customer_desc = (String) form.getAttribute("customer_desc");
   String entity_desc = (String) form.getAttribute("entity_desc");
   String account = (String) form.getAttribute("account");

%>

  <!-- defines page layout -->
  <fo:layout-master-set>

    <!-- layout for the first page -->
    <fo:simple-page-master master-name="only"
                  page-width="29.7cm"
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

      <fo:block font-size="12pt" line-height="12pt" text-align="center">Transaction Report</fo:block>
      <fo:block font-size="12pt" line-height="12pt" text-align="center">policyno = <%=JSPUtil.printNVL(policyno,"ALL")%></fo:block>
      <fo:block font-size="12pt" line-height="12pt" text-align="center">datefrom = <%=JSPUtil.printNVL(policydatefrom,"ALL")%></fo:block>
      <fo:block font-size="12pt" line-height="12pt" text-align="center">dateto = <%=JSPUtil.printNVL(policydateto,"ALL")%></fo:block>
      <fo:block font-size="12pt" line-height="12pt" text-align="center">Entity = <%=JSPUtil.printNVL(entity_desc,"ALL")%></fo:block>
      <fo:block font-size="12pt" line-height="12pt" text-align="center">Customer = <%=JSPUtil.printNVL(customer_desc,"ALL")%></fo:block>
      <fo:block font-size="12pt" line-height="12pt" text-align="center">Account = <%=JSPUtil.printNVL(account,"ALL")%></fo:block>

      <fo:block font-size="8pt" line-height="20pt" ></fo:block>

      <!-- defines text title level 1-->
      <fo:block font-size="4pt" line-height="4pt" >
<%

   SQLAssembler sqa = new SQLAssembler();

   sqa.addSelect(
           "a.attr_pol_no, b.pol_no,d.ent_name as cust_name, c.ent_name, a.invoice_type, a.invoice_date, a.due_date, a.amount, e.accountno, e.description, \n" +
           "(case when f.negative_flag='Y' and a.invoice_type='AR' then 0 else f.amount end) as amtcr, \n" +
           "(case when f.negative_flag='Y' and a.invoice_type='AR' then f.amount else 0 end) as amtdb");
   sqa.addQuery(
           "   from ar_invoice a\n" +
           "      inner join ar_invoice_details f on f.ar_invoice_id = a.ar_invoice_id\n" +
           "      left join ins_policy b on b.pol_id=a.attr_pol_id\n" +
           "      left join ent_master d on d.ent_id=b.entity_id\n" +
           "      left join ent_master c on c.ent_id = a.ent_id\n" +
           "      left join gl_accounts e on e.account_id = f.gl_account_id"
           );

   if (policyno!=null) {
      sqa.addClause("a.attr_pol_no like ?");
      sqa.addPar(policyno);
   }

   if (account!=null) {
      sqa.addClause("e.accountno like ?");
      sqa.addPar(account);
   }

   if (entity!=null) {
      sqa.addClause("a.ent_id=?");
      sqa.addPar(entity);
   }

   if (customer!=null) {
      sqa.addClause("b.entity_id=?");
      sqa.addPar(customer);
   }

   if (policydatefrom!=null) {
      sqa.addClause("f.policy_date>=?");
      sqa.addPar(DateUtil.dateBracketLow(policydatefrom));
   }

   if (policydateto!=null) {
      sqa.addClause("f.policy_date<?");
      sqa.addPar(DateUtil.dateBracketHigh(policydateto));
   }


   final DTOList r = ListUtil.getDTOListFromQuery(sqa.getSQL(),  sqa.getPar(), HashDTO.class);

   /*HashDTO tot = new HashDTO();

   tot.setFieldValueByFieldName("no", "");
   tot.setFieldValueByFieldName("premi_amount", r.getTotal("premi_amount"));
   tot.setFieldValueByFieldName("comission", r.getTotal("comission"));
   tot.setFieldValueByFieldName("netto", r.getTotal("netto"));
   tot.setFieldValueByFieldName("description", "TOTAL");

   r.add(tot);*/

   final String [] columnTitles={
      "No",
      "Policy No",
      "The Insured",
      "Entity",
      "Type",
      "Date",
      "Due",
      "Amount",
      "Account",
      "Description",
      "DB",
      "CR",
   };


   final FOPTableSource fopTableSource = new FOPTableSource(
           12,
           new int [] {4,20,40,40,3,10,10,10,15,30,10,10},
           26,
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
            case 2: return h.getFieldValueByFieldNameST("cust_name");
            case 3: return h.getFieldValueByFieldNameST("ent_name");
            case 4: return h.getFieldValueByFieldNameST("invoice_type");
            case 5: return h.getFieldValueByFieldNameDT("invoice_date");
            case 6: return h.getFieldValueByFieldNameDT("due_date");
            case 7: return h.getFieldValueByFieldNameBD("amount");
            case 8: return h.getFieldValueByFieldNameST("accountno");
            case 9: return h.getFieldValueByFieldNameST("description");
            case 10: return h.getFieldValueByFieldNameBD("amtcr");
            case 11: return h.getFieldValueByFieldNameBD("amtdb");
         }
         return "?";
      }

      public String getColumnHeader(int columnNo) {
         return columnTitles[columnNo];
      }

      public String getColumnAlign(int rowNo, int columnNo) {
         switch(columnNo) {
            case 7:
            case 10:
            case 11:
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
