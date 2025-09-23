<%@ page import="com.webfin.insurance.model.*,
                 com.crux.ff.model.FlexFieldHeaderView,
                 com.crux.ff.model.FlexFieldDetailView,
                 com.crux.util.*,
                 com.crux.util.fop.FOPUtil,
                 com.webfin.entity.model.EntityView,
                 com.webfin.ar.model.ARTaxView,
                 java.math.BigDecimal"%><?xml version="1.0" encoding="utf-8"?>
<%

   final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
   final String fontsize = (String) request.getAttribute("FONTSIZE");
   pol.loadClausules();
   
   boolean effective = pol.isEffective();

   //if (true) throw new NullPointerException();

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <!-- defines page layout -->
  <fo:layout-master-set>

    <!-- layout for the first page -->
<%--
    <fo:simple-page-master master-name="first"
                  page-height="28cm"
                  page-width="21.5cm"
                  margin-top="1cm"
                  margin-bottom="0.5cm"
                  margin-left="1cm"
                  margin-right="1cm">

      <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
      <fo:region-before extent="2cm"/>
      <fo:region-after extent="0.5cm"/>
    </fo:simple-page-master>
    --%>
    <fo:simple-page-master master-name="first" 
                  page-height="14cm"
                  page-width="21cm">
 
      <fo:region-body margin-top="0cm" margin-bottom="0cm"/> 
      <fo:region-before extent="0cm"/> 
      <fo:region-after extent="0cm"/> 
    </fo:simple-page-master>

  </fo:layout-master-set>
  <!-- end: defines page layout -->
  
 
  <!-- actual layout -->
  <fo:page-sequence master-reference="first"> 
  
<%-- 
	<fo:static-content flow-name="xsl-region-before">
		<fo:block text-align="right"
            font-size="16pt"
            font-family="TAHOMA"
            line-height="12pt" 
            font-weight="bold">
        	{L-ENG DETAILS OF CREDIT NOTE -L}{L-INA PERINCIAN NOTA KREDIT -L}
      	</fo:block>
    </fo:static-content>  	
 --%>  
 
 
    <fo:flow flow-name="xsl-region-body">
   <!-- ROW -->
   <% if (!effective) {%>
      <fo:block font-size="16pt" font-family="TAHOMA"
            line-height="16pt" space-after.optimum="0pt"
            color="red"
            text-align="center"
            padding-top="10pt">
        SPECIMEN
      </fo:block>
<% }%>

<%
   int pn=0;

      final DTOList objects = pol.getObjects();

      for (int i = 0; i < objects.size(); i++) {
         InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

         final DTOList treatyDetails = obj.getTreatyDetails();

         for (int j = 0; j < treatyDetails.size(); j++) {
            InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);

            final boolean nonProportional = trd.getTreatyDetail().getTreatyType().isNonProportional();

            if (!nonProportional) continue;

            final DTOList shares = trd.getShares();

            for (int k = 0; k < shares.size(); k++) {
               InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);

               final EntityView reasuradur = ri.getEntity();

               pn++;

%>
 
<%if (pn>1) {%>
         <fo:block break-after="page"></fo:block>
<% } %>
 
<fo:block text-align="end"
            font-size="16pt"
            font-family="TAHOMA"
            line-height="12pt" 
            font-weight="bold">
        	{L-ENG CREDIT NOTE-L}{L-INA NOTA KREDIT-L}
      	</fo:block>
      	
      	<fo:block space-after.optimum="45pt"></fo:block>
   
   <fo:block font-size="8pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="13cm"/>
         <fo:table-column column-width="3cm"/>
         <fo:table-column />
         <fo:table-body>

            <fo:table-row>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><fo:page-number/>/<%= String.valueOf(i+1) %>/<%=JSPUtil.printX(ri.getStRISlipNo())%></fo:block></fo:table-cell>
           </fo:table-row>
           
            <fo:table-row>
                      <fo:table-cell number-columns-spanned="3">
                           <fo:block space-after.optimum="5pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block><%=pol.getStCustomerName()%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStProducerID()%></fo:block></fo:table-cell>
           </fo:table-row>

<%      
     String address = "";
      if(pol.getStCustomerAddress().length()>80)
      		address = pol.getStCustomerAddress().substring(0,80);
      else
      		address = pol.getStCustomerAddress().substring(0, pol.getStCustomerAddress().length());
 %>
            
           <fo:table-row>
             <fo:table-cell ><fo:block><%=address%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%></fo:block></fo:table-cell>
           </fo:table-row>
           
           <fo:table-row>
             <fo:table-cell ><fo:block><%=reasuradur.getStEntityName()%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
           </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>
    	
    	<fo:block space-after.optimum="45pt"></fo:block>
   
   <fo:block font-size="8pt">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="4.1cm"/>
         <fo:table-column column-width="6.2cm"/>
         <fo:table-column column-width="2.4cm"/>
         <fo:table-column column-width="3cm"/>
         <fo:table-column column-width="1cm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="start"><%=JSPUtil.printX(pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18))%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStPolicyTypeDesc2())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="center"><%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d-MM-yyyy")%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="center"><%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"d-MM-yyyy")%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="start"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(),2)%></fo:block></fo:table-cell>
           </fo:table-row>
           
                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="6">
                           <fo:block space-after.optimum="20pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>
                    
           <fo:table-row>
             <fo:table-cell number-columns-spanned="4"><fo:block></fo:block></fo:table-cell>
             <fo:table-cell number-columns-spanned="2"><fo:block text-align="center">-</fo:block></fo:table-cell>
           </fo:table-row>
            
           <fo:table-row>
                      <fo:table-cell number-columns-spanned="6">
                           <fo:block space-after.optimum="18pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>
                    
           <fo:table-row>
             <fo:table-cell number-columns-spanned="4"><fo:block></fo:block></fo:table-cell>
             <fo:table-cell number-columns-spanned="2"><fo:block text-align="end">XXXXXXXXXXXXXXX</fo:block></fo:table-cell>
           </fo:table-row>
           
           <fo:table-row>
                      <fo:table-cell number-columns-spanned="6">
                           <fo:block space-after.optimum="13pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>
                    
           <fo:table-row>
             <fo:table-cell number-columns-spanned="2"><fo:block></fo:block></fo:table-cell>
             <fo:table-cell number-columns-spanned="2"><fo:block text-align="start">{L-ENG Premium-L}{L-INA Premi-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="start"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(ri.getDbPremiAmount(),2)%></fo:block></fo:table-cell>
           </fo:table-row>
           
           <fo:table-row>
                      <fo:table-cell number-columns-spanned="6">
                           <fo:block space-after.optimum="7pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>
                    
           <fo:table-row>
             <fo:table-cell number-columns-spanned="4"><fo:block></fo:block></fo:table-cell>
             <fo:table-cell number-columns-spanned="2"><fo:block text-align="end">XXXXXXXXXXXXXXX</fo:block></fo:table-cell>
           </fo:table-row>
           
           <fo:table-row>
                      <fo:table-cell number-columns-spanned="6">
                           <fo:block space-after.optimum="15pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>
                    
           <fo:table-row>
             <fo:table-cell number-columns-spanned="2"><fo:block></fo:block></fo:table-cell>
             <fo:table-cell number-columns-spanned="2"><fo:block text-align="start">{L-ENG Commission-L}{L-INA Komisi-L} <%=JSPUtil.print(ri.getDbRICommRate(),2)%>% </fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="start"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(ri.getDbRICommAmount(),2)%></fo:block></fo:table-cell>
           </fo:table-row>
           
           <fo:table-row>
                      <fo:table-cell number-columns-spanned="6">
                           <fo:block space-after.optimum="17pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>
                    
           <fo:table-row>
             <fo:table-cell number-columns-spanned="4"><fo:block></fo:block></fo:table-cell>
             <fo:table-cell number-columns-spanned="2"><fo:block text-align="center">-</fo:block></fo:table-cell>
           </fo:table-row>
           
           <fo:table-row>
                      <fo:table-cell number-columns-spanned="6">
                           <fo:block space-after.optimum="30pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>
                    
           <fo:table-row>
             <fo:table-cell number-columns-spanned="4"><fo:block>{L-ENG - Due To You-L}{L-INA - Untuk keuntungan Saudara, apabila pembayaran sudah kami terima -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="start"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(ri.getDbPremiNet(),2)%></fo:block></fo:table-cell>
           </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>
       
       <fo:block space-after.optimum="65pt"></fo:block>
             
<% 		} 
	}
}
%>

    </fo:flow>
  </fo:page-sequence>
</fo:root>