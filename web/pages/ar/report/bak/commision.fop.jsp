<%@ page import="com.webfin.ar.model.*,  
                 com.webfin.ar.forms.FRRPTrptArAPDetailForm,
                 com.crux.ff.model.FlexFieldHeaderView, 
                 com.crux.ff.model.FlexFieldDetailView, 
                 com.crux.util.*, 
                 com.crux.util.Tools, 
                 com.crux.util.fop.FOPUtil, 
                 java.math.BigDecimal, 
                 com.crux.web.form.FormManager, 
                 com.crux.web.controller.SessionManager,
                 java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 
<% 
 
	final DTOList l = (DTOList)request.getAttribute("RPT");
 
	final FRRPTrptArAPDetailForm form = (FRRPTrptArAPDetailForm)SessionManager.getInstance().getCurrentForm(); 
   
   BigDecimal IDRPremi = null;
   BigDecimal IDRComm = null;
   BigDecimal IDRClaim = null;
   BigDecimal IDRNetto = null;
   
   BigDecimal nonIDRPremi = null;
   BigDecimal nonIDRComm = null;
   BigDecimal nonIDRClaim = null;
   BigDecimal nonIDRNetto = null;
 
%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
 
  <!-- defines page layout --> 
  <fo:layout-master-set> 
 
    <!-- layout for the first page --> 
    <fo:simple-page-master master-name="first" 
                  page-height="21.5cm"
                  page-width="33cm"
                  margin-top="1cm"
                  margin-bottom="1cm"
                  margin-left="0.5cm"
                  margin-right="0.5cm">
 
      <fo:region-body margin-top="0cm" margin-bottom="0cm"/> 
      <fo:region-before extent="0cm"/> 
      <fo:region-after extent="0.5cm"/> 
    </fo:simple-page-master> 
 
  </fo:layout-master-set> 
  <!-- end: defines page layout --> 
 
  <!-- actual layout --> 
  <fo:page-sequence master-reference="first"> 
 
    <!-- usage of page layout --> 
    <fo:flow flow-name="xsl-region-body"> 
    
      <fo:block font-size="8pt"> 
        <fo:table table-layout="fixed">  
         <fo:table-column column-width="10mm"/><!-- No -->
         <fo:table-column column-width="10mm"/><!-- Entry Date -->
         <fo:table-column column-width="15mm"/><!-- Policy Date--> 
         <fo:table-column column-width="15mm"/><!-- Policy No -->
         <fo:table-column column-width="10mm"/><!-- The Insured -->
         <fo:table-column column-width="30mm"/><!-- Premium --> 
         <fo:table-column column-width="10mm"/><!-- Biaya Polis --> 
         <fo:table-column column-width="15mm"/><!-- Biaya Polis --> 
         <fo:table-column column-width="15mm"/><!-- Biaya Polis -->
         <fo:table-column column-width="25mm"/><!-- Biaya Materai -->
         <fo:table-column column-width="10mm"/><!-- Discount-->
         <fo:table-column column-width="25mm"/><!-- Komisi-->
         <fo:table-column column-width="25mm"/><!-- Komisi-->
         <fo:table-column column-width="25mm"/><!-- Handling Fee --> 
         <fo:table-column column-width="10mm"/><!-- Broker Fee -->
         <fo:table-column /><!-- Pajak Komisi/Broker Fee -->
         <fo:table-body>          
          
      <fo:table-row>
	  <fo:table-cell number-columns-spanned="16">
      <fo:block font-weight="bold">             
      LAPORAN PROFIT COMMISION REASURANSI TREATY INWARD
      </fo:block>
      </fo:table-cell>
      </fo:table-row>
          
      <fo:table-row>
	  <fo:table-cell number-columns-spanned="16">
      <fo:block  font-weight="bold">
       <% if (form.getPolicydatefrom()!=null || form.getPolicydateto()!=null) {%> 
         BULAN : <%=DateUtil.getDateStr(form.getPolicydatefrom(),"^^ yyyy")%> s/d <%=DateUtil.getDateStr(form.getPolicydateto(),"^^ yyyy")%>
       <% } %>
      </fo:block>
      </fo:table-cell>
      </fo:table-row>
         
         			<fo:table-row>   
                      <fo:table-cell number-columns-spanned="16" >  
                           <fo:block border-width="0.25pt" border-style="solid" line-height="0.25pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                      </fo:table-cell>   
                    </fo:table-row>  
         
            <fo:table-row>   
             <fo:table-cell ><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG Bussiness -L}{L-INA Bisnis -L}</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="center">{L-ENG Quartal -L}{L-INA Quartal -L}</fo:block></fo:table-cell>  
           	 <fo:table-cell ><fo:block text-align="center">{L-ENG Date -L}{L-INA Tanggal -L}</fo:block></fo:table-cell>
			 <fo:table-cell ><fo:block text-align="center">{L-ENG UW. Year -L}{L-INA Tahun UW. -L}</fo:block></fo:table-cell> 
			 <fo:table-cell ><fo:block text-align="center">{L-ENG Invoice -L}{L-INA Uraian -L}</fo:block></fo:table-cell>  
       	 	 <fo:table-cell ><fo:block text-align="center">{L-ENG Kd. -L}{L-INA Kd. -L}</fo:block></fo:table-cell>  
			 <fo:table-cell ><fo:block text-align="center">{L-ENG Company -L}{L-INA Perusahaan -L}</fo:block></fo:table-cell> 
       	 	 <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell>  
        	 <fo:table-cell ><fo:block text-align="center">{L-ENG Premium -L}{L-INA Premium -L}</fo:block></fo:table-cell>  
       	 	 <fo:table-cell ><fo:block text-align="center">{L-ENG Share -L}{L-INA Share -L}</fo:block></fo:table-cell> 
             <fo:table-cell ><fo:block text-align="center">{L-ENG Commission -L}{L-INA Komisi -L}</fo:block></fo:table-cell>  
   			 <fo:table-cell ><fo:block text-align="center">{L-ENG Claim -L}{L-INA Klaim -L}</fo:block></fo:table-cell>  
       	 	 <fo:table-cell ><fo:block text-align="center">{L-ENG Netto -L}{L-INA Netto -L}</fo:block></fo:table-cell> 
       	 	 <fo:table-cell ><fo:block text-align="center">{L-ENG Kurs -L}{L-INA Kurs -L}</fo:block></fo:table-cell> 
   	  		 <fo:table-cell ><fo:block text-align="center">{L-ENG Description -L}{L-INA Keterangan -L}</fo:block></fo:table-cell>
   			</fo:table-row>    
   			          
         			<fo:table-row>   
                      <fo:table-cell number-columns-spanned="16" >  
                           <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                      </fo:table-cell>   
                    </fo:table-row>   
<%  
   for (int i = 0; i < l.size(); i++) { 
      ARInvoiceView invoic = (ARInvoiceView) l.get(i);
	
	if (invoic.getStCurrencyCode().equalsIgnoreCase("IDR")){
		IDRPremi = BDUtil.add(IDRPremi, invoic.getDbPremi());
		IDRComm = BDUtil.add(IDRComm, invoic.getDbComm());
		IDRClaim = BDUtil.add(IDRClaim, invoic.getDbFee());
		IDRNetto = BDUtil.sub(IDRPremi, IDRClaim);
	}else {	
		nonIDRPremi = BDUtil.add(nonIDRPremi, invoic.getDbPremi());
		nonIDRComm = BDUtil.add(nonIDRComm, invoic.getDbComm());
		nonIDRClaim = BDUtil.add(nonIDRClaim, invoic.getDbFee());
		nonIDRNetto = BDUtil.sub(nonIDRPremi, nonIDRClaim);
	}
%>

			<fo:table-row>   
             <fo:table-cell ><fo:block text-align="center"><%=String.valueOf(i+1)%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
             <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStAttrPolicyTypeID())%></fo:block></fo:table-cell> 
             <fo:table-cell ><fo:block text-align="center">TW.<%=JSPUtil.printX(invoic.getStAttrQuartal())%>/<%=JSPUtil.printX(invoic.getStAttrUnderwriting())%></fo:block></fo:table-cell> 
             <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getDtInvoiceDate())%></fo:block></fo:table-cell> 
             <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell> 
             <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStInvoiceNo())%></fo:block></fo:table-cell> 
             <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStARCustomerID())%></fo:block></fo:table-cell> 
             <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStCreateWho())%></fo:block></fo:table-cell> 
             <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStCurrencyCode())%></fo:block></fo:table-cell> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(invoic.getDbPremi(),2)%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%= JSPUtil.printX(new BigDecimal(0),2) %> %</fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(invoic.getDbComm(),2)%></fo:block></fo:table-cell>  
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(invoic.getDbFee(),2)%></fo:block></fo:table-cell> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(invoic.getDbPremi(), invoic.getDbFee()),2)%></fo:block></fo:table-cell> 
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(invoic.getDbCurrencyRate(),2)%></fo:block></fo:table-cell> 
             <fo:table-cell ><fo:block></fo:block></fo:table-cell> 
			</fo:table-row>			
<% } %>   

				<fo:table-row>   
                      <fo:table-cell number-columns-spanned="16">  
                           <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                      </fo:table-cell>   
                    </fo:table-row>  
   
               <fo:table-row>
                <fo:table-cell number-columns-spanned="8"><fo:block text-align="center">TOTAL PROFIT COMMISION INWARD : </fo:block></fo:table-cell>
                <fo:table-cell ><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(IDRPremi,2)%></fo:block></fo:table-cell>
                <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell>
                <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(IDRComm,2)%></fo:block></fo:table-cell>
                <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(IDRClaim,2)%></fo:block></fo:table-cell>
                <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(IDRNetto,2)%></fo:block></fo:table-cell>
                <fo:table-cell number-columns-spanned="2"><fo:block></fo:block></fo:table-cell>
                </fo:table-row> 
                
               <fo:table-row>
                <fo:table-cell number-columns-spanned="8"><fo:block text-align="center"></fo:block></fo:table-cell>
                <fo:table-cell ><fo:block text-align="center">non IDR</fo:block></fo:table-cell>
                <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(nonIDRPremi,2)%></fo:block></fo:table-cell>
                <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell>
                <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(nonIDRComm,2)%></fo:block></fo:table-cell>
                <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(nonIDRClaim,2)%></fo:block></fo:table-cell>
                <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(nonIDRNetto,2)%></fo:block></fo:table-cell>
                <fo:table-cell number-columns-spanned="2"><fo:block></fo:block></fo:table-cell>
                </fo:table-row>  

				<fo:table-row>   
                      <fo:table-cell number-columns-spanned="16">  
                           <fo:block border-width="0.25pt" border-style="solid" line-height="0.25pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                      </fo:table-cell>   
                    </fo:table-row>  
   			        
		</fo:table-body>
       </fo:table>   
       </fo:block> 
       
       <fo:block font-size="8pt">
         {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
      </fo:block>    
  
    </fo:flow> 
  </fo:page-sequence>   
</fo:root>   