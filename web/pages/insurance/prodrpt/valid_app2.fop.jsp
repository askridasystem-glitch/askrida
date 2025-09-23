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
      
      
    <%-- 
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
        rppre_cek_ri - PT. Asuransi Bangun Askrida
      </fo:block>
<fo:block text-align="end"
      font-size="8pt" font-family="serif" line-height="1em + 2pt">
      {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
ref-id="end-of-document"/>
      </fo:block>
       --%>  

    <fo:flow flow-name="xsl-region-body">
      	 
    
    <fo:block text-align="center"
            font-size="18pt"
            font-family="TAHOMA"
            line-height="12pt" 
            font-style="bold" space-after.optimum="50pt"><fo:inline text-decoration="underline">DATA YANG BELUM MENDAPAT PERSETUJUAN</fo:inline></fo:block>
            
     <fo:block font-size="12pt" text-align="start" space-after.optimum="20pt">Jakarta, <%=DateUtil.getDateStr(new Date(),"d ^^ yyyy")%></fo:block>
    
     <fo:block font-size="12pt" text-align="start" space-after.optimum="10pt">Kepada Yth.</fo:block>
     
     <fo:block font-size="12pt" text-align="start">Bp. Direktur Tehnik</fo:block>
     <fo:block font-size="12pt" text-align="start">Kepala Cabang / Perwakilan / Pemasar</fo:block>
     <fo:block font-size="12pt" text-align="start">Kadiv. Underwriting</fo:block>
     <fo:block font-size="12pt" text-align="start" space-after.optimum="20pt">Kadiv. Reasuransi</fo:block>
    
     <fo:block font-size="12pt" text-align="justify" space-after.optimum="15pt">
     Bersama ini kami sampaikan, bahwa polis-polis dibawah ini masih <fo:inline text-decoration="underline">belum disetujui dan /</fo:inline> atau dilakukan <fo:inline text-decoration="underline">Approval pada divisi terkait</fo:inline>.
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
     <% if (form.getPolicyDateFrom()!=null || form.getPolicyDateTo()!=null) {%> 
         Tanggal Polis : <%=JSPUtil.printX(form.getPolicyDateFrom())%> s/d <%=JSPUtil.printX(form.getPolicyDateTo())%>
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

   sqa.addSelect("	a.policy_date,a.pol_no,a.cust_name,a.status,a.effective_flag as eff_flag,coalesce(a.f_ri_finish,'N') as ri_flag,a.insured_amount*a.ccy_rate as insured_amount,a.premi_total*a.ccy_rate as premi_total" );

	sqa.addQuery(" from ins_policy a " );

	sqa.addClause(" a.status IN ('POLICY','ENDORSE','RENEWAL')");
	
	sqa.addClause(" (a.f_ri_finish is null or a.effective_flag = 'N')");
	
	sqa.addClause(" a.active_flag = 'Y'");

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
   
   String q = sqa.getSQL()+" order by a.pol_no";

   final DTOList r = ListUtil.getDTOListFromQuery(q,  sqa.getPar(), HashDTO.class);

   final FOPTableSource fopTableSource = new FOPTableSource(
           8,
           new int [] {1,2,3,6,1,1,3,3},
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
            case 1: return h.getFieldValueByFieldNameDT("policy_date");
            case 2: return h.getFieldValueByFieldNameST("pol_no");
            case 3: return JSPUtil.xmlEscape(h.getFieldValueByFieldNameST("cust_name"));  
            case 4: return h.getFieldValueByFieldNameST("eff_flag");
            case 5: return h.getFieldValueByFieldNameST("ri_flag");
            case 6: return h.getFieldValueByFieldNameBD("insured_amount");
            case 7: return h.getFieldValueByFieldNameBD("premi_total");
         }
         return "?";
      }

      public String getColumnHeader(int columnNo) {
         switch(columnNo) {
            case 0: return "No.";
            case 1: return "Policy Date";
            case 2: return "Policy No.";
            case 3: return "Cust. Name";
            case 4: return "U/W";
            case 5: return "R/I";
            case 6: return "TSI";
            case 7: return "Premi";
         }
         return "?";
      }

      public String getColumnAlign(int rowNo, int columnNo) {
         switch(columnNo) {
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
       
       <fo:block font-size="12pt" text-align="justify">
     Mohon dapat segera diselesaikan, atau segera menghubungi divisi terkait.
     </fo:block>
     
     <fo:block font-size="12pt" text-align="justify" space-after.optimum="30pt">
     Atas perhatian dan kerjasamanya diucapkan banyak terima kasih.
     </fo:block>
     
     
     <fo:block font-size="12pt" text-align="center">
     Mengetahui :
     </fo:block>
     
     <fo:block font-size="12pt" text-align="center">
     PT. ASURANSI BANGUN ASKRIDA
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
<%--     
       <fo:block id="end-of-document"><fo:marker
    marker-class-name="term">
<fo:instream-foreign-object>
<svg xmlns="http://www.w3.org/2000/svg" width="15cm" height="1cm" xml:space="preserve">     
<rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
</svg>
</fo:instream-foreign-object>
</fo:marker>
  </fo:block> 
   --%>        
    </fo:flow>
  </fo:page-sequence>
</fo:root>
