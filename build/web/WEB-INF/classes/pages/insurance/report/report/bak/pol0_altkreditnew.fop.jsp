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
   
   String tax ="";
   BigDecimal tax_amount = new BigDecimal(0);
   BigDecimal com_amount = new BigDecimal(0);
   boolean cek = false;

   //if (true) throw new NullPointerException();

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <!-- defines page layout -->
  <fo:layout-master-set>

    <!-- layout for the first page -->

    <fo:simple-page-master master-name="only"
                  page-height="14cm"
                  page-width="21cm"
                  margin-top="2cm"
                  margin-bottom="1cm"
                  margin-left="0.5cm"
                  margin-right="0.5cm">

      <fo:region-body margin-top="1cm" margin-bottom="1.5cm"/>
      <fo:region-before extent="3cm"/>
      <fo:region-after extent="1.5cm"/>
    </fo:simple-page-master>

  </fo:layout-master-set>
  <!-- end: defines page layout -->
  
 
  <!-- actual layout -->
  <fo:page-sequence master-reference="only" initial-page-number="1">
  
	<fo:static-content flow-name="xsl-region-before">
		<fo:block text-align="right"
            font-size="16pt"
            font-family="TAHOMA"
            line-height="12pt" 
            font-weight="bold">
        	{L-ENG CREDIT NOTE -L}{L-INA NOTA KREDIT -L}
      	</fo:block>
    </fo:static-content>  	

<%--    <fo:static-content flow-name="xsl-region-before">
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
      font-size="6pt" font-family="serif" line-height="1em + 2pt">
      {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
ref-id="end-of-document"/>
      </fo:block>
    </fo:static-content> --%>
       

    <fo:flow flow-name="xsl-region-body">
   <!-- ROW -->
   <%--    <fo:block 
      	font-size="10pt" 
      	font-family="TAHOMA" 
      	line-height="10pt" 
      	space-after.optimum="10pt" 
      	text-align="justify" > 
      	</fo:block> --%>
      	
<%
   final DTOList det = pol.getDetails();

   int ln=0;

   for (int i = 0; i < det.size(); i++) {
      InsurancePolicyItemsView dt = (InsurancePolicyItemsView) det.get(i);

	  if(!dt.isComission())
	  		com_amount=BDUtil.zero;
	  else
	  		com_amount = dt.getDbAmount();
	  
      if (!dt.isComission()){ 
      cek = true;
      continue;
      }

      final EntityView agent = dt.getEntity();

      ln++;

%>


   <%if (ln>1) {%>
   
   <fo:block break-after="page"></fo:block>
   
   <%}%>     	
      	
 
      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="100mm"/>
         <fo:table-column />
         <fo:table-body>

            <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="7pt" space-after.optimum="7pt"></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block><%=pol.getStCustomerName()%></fo:block></fo:table-cell>
           </fo:table-row>
           
           <fo:table-row>
             <fo:table-cell ><fo:block><%=pol.getStCustomerAddress()%></fo:block></fo:table-cell>
           </fo:table-row>

   <!-- ROW -->
                    <fo:table-row><fo:table-cell>
                           <fo:block space-after.optimum="10pt"></fo:block>
                    </fo:table-cell></fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

       <fo:block-container  height="1.75cm" width="5.15cm" top="0cm" left="14.5cm" padding="1mm" position="absolute">
            
      <!-- \<fo:block-container height="5cm" width="2cm" top="0cm" left="14.5cm" position="absolute"> -->
                <fo:table>
                    <fo:table-column column-width="1.5cm"/>
                    <fo:table-column column-width="0.5cm"/>
                    <fo:table-column column-width="5cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="<%=fontsize%>">     
   					
   					<fo:table-row line-height="10pt">
                            <fo:table-cell padding="2pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt"><fo:block text-align="start">xxx</fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row line-height="10pt">
                            <fo:table-cell padding="2pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt"><fo:block text-align="start">xxx</fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row line-height="10pt">
                            <fo:table-cell padding="2pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block-container>
            
            
            <fo:block-container height="0.5cm" width="4cm" top="2.9cm" left="0cm" padding="1mm" position="absolute">
                <fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%>
                </fo:block>
            </fo:block-container>
            
            <fo:block-container height="0.5cm" width="5cm" top="2.9cm" left="5cm" padding="1mm" position="absolute">
                <fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%=pol.getStPolicyTypeDesc2()%>
                </fo:block>
            </fo:block-container>
            
            <fo:block-container height="0.5cm" width="5cm" top="2.9cm" left="10.3cm" padding="1mm" position="absolute">
                <fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d-MM-yyyy")%>
                 </fo:block>
            </fo:block-container>
            
            <fo:block-container height="0.5cm" width="5cm" top="2.9cm" left="12.5cm" padding="1mm" position="absolute">
                <fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"d-MM-yyyy")%>
                 </fo:block>
            </fo:block-container>
             
            <fo:block-container height="0.5cm" width="3.8cm" top="2.9cm" left="16.3cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     
   					
   					<fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%= JSPUtil.printX(pol.getStCurrencyCode()) %></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="end"><%= JSPUtil.printX(pol.getDbInsuredAmount(),2) %></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                     </fo:table-body>
                    </fo:table> 
            </fo:block-container> 
                         
            <fo:block-container height="1.25cm" width="3.8cm" top="4.25cm" left="16.3cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     
   					
   					<fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center">-</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                     </fo:table-body>
                    </fo:table> 
            </fo:block-container> 
            
             <fo:block-container height="1.25cm" width="3.8cm" top="5cm" left="16.3cm" padding="1mm" position="absolute">
            	<fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    XXXXXXXXXX
                </fo:block>
            </fo:block-container>     
                    
            <fo:block-container height="1.25cm" width="3.8cm" top="5.5cm" left="13.3cm" padding="1mm" position="absolute">
                <fo:table>
                	<fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     
   					
   					<fo:table-row line-height="12pt">
   							<fo:table-cell padding="2pt">
                                <fo:block text-align="start">Komisi</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%= JSPUtil.printX(pol.getStCurrencyCode()) %></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="end"><%= JSPUtil.printX(com_amount,2) %></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                     </fo:table-body>
                    </fo:table> 
            </fo:block-container>
            
            <fo:block-container height="1.25cm" width="3.8cm" top="6.3cm" left="16.3cm" padding="1mm" position="absolute">
            	<fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    XXXXXXXXXX
                </fo:block>
            </fo:block-container>   
            
<%
   final ARTaxView tx = dt.getTax();
   
   //if (tx!=null) {
   if(tx==null){
   		tax="";
   		tax_amount = BDUtil.zero;
   }
   else if(tx!=null){
   		tax = tx.getStDescription();
   		tax_amount = dt.getDbTaxAmount();
   }

%>     
           
            
            <fo:block-container height="1.25cm" width="3.8cm" top="7cm" left="13.3cm" padding="1mm" position="absolute">
                <fo:table>
                	<fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     
   					
   					<fo:table-row line-height="12pt">
   							<fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%= tax %></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%= JSPUtil.printX(pol.getStCurrencyCode()) %></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="end"><%= JSPUtil.printX(tax_amount,2) %></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                     </fo:table-body>
                    </fo:table> 
            </fo:block-container>
            
            <fo:block-container height="1.25cm" width="3.8cm" top="8.2cm" left="16.3cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     
   					
   					<fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center">-</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                     </fo:table-body>
                    </fo:table> 
            </fo:block-container>
            
            
            
            <fo:block-container height="1.25cm" width="14.45cm" top="9.5cm" left="1cm" padding="1mm" position="absolute">
                 <fo:table>
                    <fo:table-column />
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     
   					
   				
                     <fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start">- Untuk keuntungan Saudara, apabila pembayaran sudah kami terima</fo:block>
                            </fo:table-cell>
                     </fo:table-row>
                     
                     </fo:table-body>
                    </fo:table> 
            </fo:block-container>
           
            <fo:block-container height="1.25cm" width="3.8cm" top="9.5cm" left="16.3cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     
   					
   					<fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%= JSPUtil.printX(pol.getStCurrencyCode()) %></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="end"><%= JSPUtil.printX(dt.getDbNetAmount(),2) %></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                     </fo:table-body>
                    </fo:table> 
             </fo:block-container>  
<% } %>

<%if (ln<1) {%>


      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="100mm"/>
         <fo:table-column />
         <fo:table-body>

            <fo:table-row>
             <fo:table-cell ><fo:block space-before.optimum="7pt" space-after.optimum="7pt"></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block><%=pol.getStCustomerName()%></fo:block></fo:table-cell>
           </fo:table-row>
           
           <fo:table-row>
             <fo:table-cell ><fo:block><%=pol.getStCustomerAddress()%></fo:block></fo:table-cell>
           </fo:table-row>

   <!-- ROW -->
                    <fo:table-row><fo:table-cell>
                           <fo:block space-after.optimum="10pt"></fo:block>
                    </fo:table-cell></fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

       <fo:block-container  height="1.75cm" width="5.15cm" top="0cm" left="14.5cm" padding="1mm" position="absolute">
            
      <!-- \<fo:block-container height="5cm" width="2cm" top="0cm" left="14.5cm" position="absolute"> -->
                <fo:table>
                    <fo:table-column column-width="1.5cm"/>
                    <fo:table-column column-width="0.5cm"/>
                    <fo:table-column column-width="5cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="<%=fontsize%>">     
   					
   					<fo:table-row line-height="10pt">
                            <fo:table-cell padding="2pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt"><fo:block text-align="start">xxx</fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row line-height="10pt">
                            <fo:table-cell padding="2pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt"><fo:block text-align="start">xxx</fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row line-height="10pt">
                            <fo:table-cell padding="2pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block-container>
            
            
            <fo:block-container height="0.5cm" width="4cm" top="2.9cm" left="0cm" padding="1mm" position="absolute">
                <fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%>
                </fo:block>
            </fo:block-container>
            
            <fo:block-container height="0.5cm" width="5cm" top="2.9cm" left="5cm" padding="1mm" position="absolute">
                <fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%=pol.getStPolicyTypeDesc2()%>
                </fo:block>
            </fo:block-container>
            
            <fo:block-container height="0.5cm" width="5cm" top="2.9cm" left="10.3cm" padding="1mm" position="absolute">
                <fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d-MM-yyyy")%>
                 </fo:block>
            </fo:block-container>
            
            <fo:block-container height="0.5cm" width="5cm" top="2.9cm" left="12.5cm" padding="1mm" position="absolute">
                <fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"d-MM-yyyy")%>
                 </fo:block>
            </fo:block-container>
             
            <fo:block-container height="0.5cm" width="3.8cm" top="2.9cm" left="16.3cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     
   					
   					<fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%= JSPUtil.printX(pol.getStCurrencyCode()) %></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="end"><%= JSPUtil.printX(pol.getDbInsuredAmount(),2) %></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                     </fo:table-body>
                    </fo:table> 
            </fo:block-container> 
                         
            <fo:block-container height="1.25cm" width="3.8cm" top="4.25cm" left="16.3cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     
   					
   					<fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center">-</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                     </fo:table-body>
                    </fo:table> 
            </fo:block-container> 
            
             <fo:block-container height="1.25cm" width="3.8cm" top="5cm" left="16.3cm" padding="1mm" position="absolute">
            	<fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    XXXXXXXXXX
                </fo:block>
            </fo:block-container>     
                    
            <fo:block-container height="1.25cm" width="3.8cm" top="5.5cm" left="13.3cm" padding="1mm" position="absolute">
                <fo:table>
                	<fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     
   					
   					<fo:table-row line-height="12pt">
   							<fo:table-cell padding="2pt">
                                <fo:block text-align="start">Komisi</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%= JSPUtil.printX(pol.getStCurrencyCode()) %></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="end"><%= JSPUtil.printX(pol.getDbNDComm1(),2) %></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                     </fo:table-body>
                    </fo:table> 
            </fo:block-container>
            
            <fo:block-container height="1.25cm" width="3.8cm" top="6.3cm" left="16.3cm" padding="1mm" position="absolute">
            	<fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    XXXXXXXXXX
                </fo:block>
            </fo:block-container>  
            
            <fo:block-container height="1.25cm" width="3.8cm" top="7cm" left="13.3cm" padding="1mm" position="absolute">
                <fo:table>
                	<fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     
   					
   					<fo:table-row line-height="12pt">
   							<fo:table-cell padding="2pt">
                                <fo:block text-align="start"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center">-</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                     </fo:table-body>
                    </fo:table> 
            </fo:block-container>
            
            <fo:block-container height="1.25cm" width="3.8cm" top="8.2cm" left="16.3cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     
   					
   					<fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center">-</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                     </fo:table-body>
                    </fo:table> 
            </fo:block-container>
            
            
            
            <fo:block-container height="1.25cm" width="14.45cm" top="9.5cm" left="1cm" padding="1mm" position="absolute">
                 <fo:table>
                    <fo:table-column />
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     
   					
   					<fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start">Untuk keuntungan Saudara, apabila pembayaran sudah kami terima</fo:block>
                            </fo:table-cell>
                     </fo:table-row>
                     
                     </fo:table-body>
                    </fo:table> 
            </fo:block-container>
           
            <fo:block-container height="1.25cm" width="3.8cm" top="9.5cm" left="16.3cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     
   					
   					<fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%= JSPUtil.printX(pol.getStCurrencyCode()) %></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="end"><%= JSPUtil.printX(pol.getDbNDComm1(),2) %></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                     </fo:table-body>
                    </fo:table> 
             </fo:block-container>
              <% } %>
    </fo:flow>
  </fo:page-sequence>
</fo:root>



