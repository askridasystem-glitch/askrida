<%@ page import="com.crux.util.fop.FOPTableSource,
                 com.webfin.insurance.form.ProductionClaimReportForm,
                 com.crux.common.model.HashDTO,
                 com.crux.util.*,
                 java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
<%
   final ProductionClaimReportForm form = (ProductionClaimReportForm) request.getAttribute("FORM");
%>

  <!-- defines page layout -->
  <fo:layout-master-set>

    <!-- layout for the first page -->
    <fo:simple-page-master master-name="only"
                  page-width="21cm"
                  page-height="30cm"
                  margin-top="0.5cm"
                  margin-bottom="0.5cm"
                  margin-left="2cm"
                  margin-right="1.5cm">
      <fo:region-body margin-top="2cm" margin-bottom="1cm"/>
      <fo:region-before extent="3cm"/>
      <fo:region-after extent="0.5cm"/>
    </fo:simple-page-master>

  </fo:layout-master-set>
  <!-- end: defines page layout -->

  <!-- actual layout -->
  <fo:page-sequence master-reference="only" initial-page-number="1">

    <!-- usage of page layout -->
    <fo:static-content flow-name="xsl-region-before">
    
    </fo:static-content>
    
    <fo:static-content flow-name="xsl-region-after">
       <fo:block font-size="6pt">
         {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
      </fo:block>
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">
    
    <fo:block text-align="center"
            font-size="18pt"
            font-family="TAHOMA"
            line-height="12pt" 
            font-style="bold" space-after.optimum="50pt"><fo:inline text-decoration="underline">DATA KLAIM YANG BELUM MENDAPAT PERSETUJUAN</fo:inline></fo:block>
            
     <fo:block font-size="12pt" text-align="start" space-after.optimum="20pt">Jakarta, <%=DateUtil.getDateStr(new Date(),"d ^^ yyyy")%></fo:block>
    
     <fo:block font-size="12pt" text-align="start" space-after.optimum="10pt">Kepada Yth.</fo:block>
     
     <fo:block font-size="12pt" text-align="start">Direktur Tehnik</fo:block>
     <fo:block font-size="12pt" text-align="start">Kepala Cabang / Perwakilan / Pemasar</fo:block>
     <fo:block font-size="12pt" text-align="start">Kadiv. Underwriting</fo:block>
     <fo:block font-size="12pt" text-align="start" space-after.optimum="20pt">Kadiv. Klaim</fo:block>
    
     <fo:block font-size="12pt" text-align="justify" space-after.optimum="15pt">
     Bersama ini kami sampaikan, bahwa polis-polis dibawah ini masih belum disetujui dan /atau dilakukan Approval pada divisi terkait.
     </fo:block>
    
    
     <fo:block >
        <fo:table table-layout="fixed">
      	 <fo:table-column column-width="90mm"/>
      	 <fo:table-column column-width="90mm"/>
      	 <fo:table-body>      	 
      
      <fo:table-row>
	  <fo:table-cell number-columns-spanned="2">
      <fo:block font-size="10pt" line-height="12pt" text-align="center" space-after.optimum="5pt">	 
      VALIDASI APPROVAL 
	  </fo:block>
      </fo:table-cell>
      </fo:table-row>
      
      <fo:table-row>
	  <fo:table-cell number-columns-spanned="2">
      <fo:block font-size="10pt" line-height="12pt" text-align="center" space-after.optimum="5pt">	 
     <% if (form.getClaimDateFrom()!=null || form.getClaimDateTo()!=null) {%> 
         Tanggal Klaim : <%=JSPUtil.printX(form.getClaimDateFrom())%> s/d <%=JSPUtil.printX(form.getClaimDateTo())%>
       <% } %>
	  </fo:block>
      </fo:table-cell>
      </fo:table-row>
      
      <fo:table-row>
	  <fo:table-cell>
      <fo:block font-size="10pt"><% if(form.getStBranchDesc()!=null) {%>	 
      Cabang : 
             <%= JSPUtil.printX(form.getStBranchDesc())%>
             <% }%>
	  </fo:block>
      </fo:table-cell>
      <fo:table-cell>
      <fo:block font-size="10pt" text-align="end">	 
      (dalam rupiah)
	  </fo:block>
      </fo:table-cell>
      </fo:table-row>
      
      </fo:table-body>
      </fo:table>
      </fo:block>

      <fo:block font-size="8pt" line-height="20pt"></fo:block>

      <!-- defines text title level 1-->
      <fo:block font-size="7pt" line-height="10pt" space-after.optimum="30pt">
<%

   SQLAssembler sqa = new SQLAssembler();

   sqa.addSelect("	a.claim_date,a.pol_no,b.short_desc,a.effective_flag,a.dla_no,a.cust_name,a.claim_amount_approved" );

	sqa.addQuery(" from ins_policy a "+
				" inner join ins_policy_types b on b.pol_type_id = a.pol_type_id");

	sqa.addClause(" a.status IN ('CLAIM','CLAIM ENDORSE')");
	
	sqa.addClause(" a.effective_flag = 'N'");
	
	sqa.addClause(" a.claim_status = 'DLA'");

   if (form.getClaimDateFrom()!=null) {
      sqa.addClause("date_trunc('day',a.claim_date) >= ?");
      sqa.addPar(form.getClaimDateFrom());
	}
	
	if (form.getClaimDateTo()!=null) {
      sqa.addClause("date_trunc('day',a.claim_date) <= ?");
      sqa.addPar(form.getClaimDateTo());
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
      
      if (form.getStNoUrut()!=null) {
            sqa.addClause("a.claim_amount_approved < ?");
            sqa.addPar(form.getStNoUrut());
        }
   
   String q = sqa.getSQL()+" order by a.pol_no";

   final DTOList r = ListUtil.getDTOListFromQuery(q,  sqa.getPar(), HashDTO.class);

   final FOPTableSource fopTableSource = new FOPTableSource(
           8,
           new int [] {1,2,3,1,1,3,3,3},
           18,
           "cm"
   ) {
      public int getRowCount() {
         return r.size();
      }

      public Object getColumnValue(int rowNo, int columnNo) {
         HashDTO h = (HashDTO) r.get(rowNo);
         switch(columnNo) {
            case 0: return h.getFieldValueByFieldNameST("no")==null?String.valueOf(rowNo+1):"";
            case 1: return h.getFieldValueByFieldNameDT("claim_date");
            case 2: return h.getFieldValueByFieldNameST("pol_no");
            case 3: return h.getFieldValueByFieldNameST("short_desc");  
            case 4: return h.getFieldValueByFieldNameST("effective_flag");
            case 5: return h.getFieldValueByFieldNameST("dla_no");
            case 6: return h.getFieldValueByFieldNameST("cust_name");
            case 7: return h.getFieldValueByFieldNameBD("claim_amount_approved");
         }
         return "?";
      }

      public String getColumnHeader(int columnNo) {
         switch(columnNo) {
            case 0: return "No.";
            case 1: return "Claim Date";
            case 2: return "Policy No.";
            case 3: return "Types";
            case 4: return "Eff";
            case 5: return "DLA No";
            case 6: return "Customer Name";
            case 7: return "Claim Amount";
         }
         return "?";
      }

      public String getColumnAlign(int rowNo, int columnNo) {
         switch(columnNo) {
            case 7:
               return "end";
         };

         return super.getColumnAlign(rowNo, columnNo);    //To change body of overridden methods use File | Settings | File Templates.
      }

   };

   fopTableSource.display(out);
%>
       </fo:block>
       
       <fo:block font-size="12pt" text-align="justify" space-after.optimum="15pt">
     Mohon dapat segera diselesaikan, atau segera menghubungi kantor bersangkutan.
     </fo:block>
     
     <fo:block font-size="12pt" text-align="justify">
     Terima Kasih,
     </fo:block>
     
     <fo:block font-size="12pt" text-align="justify" space-after.optimum="15pt">
     Divisi Perencanaan dan Pengembangan
     </fo:block>
     
    </fo:flow>
  </fo:page-sequence>
</fo:root>
