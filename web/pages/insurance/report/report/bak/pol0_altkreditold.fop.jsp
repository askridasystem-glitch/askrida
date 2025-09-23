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
   
   boolean effective = pol.isEffective();

   //if (true) throw new NullPointerException();

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <!-- defines page layout -->
  <fo:layout-master-set>

    <!-- layout for the first page -->

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
       
       <fo:block-container height="1.5cm" width="14cm" top="3cm" left="1cm" padding="1mm" position="absolute">
                <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%=pol.getStCustomerName()%>
                </fo:block>
                <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%=pol.getStCustomerAddress()%>
                </fo:block>
            </fo:block-container>
            
            <fo:block-container height="1cm" width="5cm" top="1cm" left="15cm" padding="1mm" position="absolute">
                <fo:block text-align="right"
            font-size="16pt"
            font-family="TAHOMA"
            line-height="12pt" 
            font-weight="bold">
                    {L-ENG CREDIT NOTE -L}{L-INA NOTA KREDIT -L}
                </fo:block>
            </fo:block-container>

       <fo:block-container  height="1.5cm" width="3cm" top="3cm" left="15cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="1.5cm"/>
                    <fo:table-column column-width="0.7cm"/>
                    <fo:table-column column-width="3.5cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">    
   					
   					<fo:table-row >
                            <fo:table-cell padding="1pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt"><fo:block text-align="start">F<%=pol.getStPolicyNo().substring(2,4)+""+pol.getStPolicyNo().substring(10,12)+""+pol.getStPolicyNo().substring(0,2)+""+pol.getStPolicyNo().substring(4,6)+""+pol.getStPolicyNo().substring(12,16)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row >
                            <fo:table-cell padding="1pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt"><fo:block text-align="center">--</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row >
                            <fo:table-cell padding="2pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt"><fo:block text-align="start"><%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block-container>
            
            
            <fo:block-container height="0.5cm" width="4.5cm" top="5.5cm" left="1cm" padding="1mm" position="absolute">
                <fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%>
                </fo:block>
            </fo:block-container>
            
            <fo:block-container height="0.5cm" width="6cm" top="5.5cm" left="6cm" padding="1mm" position="absolute">
                <fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%=pol.getStPolicyTypeDesc2()%>
                </fo:block>
            </fo:block-container>
            
            <fo:block-container height="0.5cm" width="2.3cm" top="5.5cm" left="12cm" padding="1mm" position="absolute">
                <fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d-MM-yyyy")%>
                 </fo:block>
            </fo:block-container>
            
            <fo:block-container height="0.5cm" width="2.5cm" top="5.5cm" left="14.3cm" padding="1mm" position="absolute">
                <fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"d-MM-yyyy")%>
                 </fo:block>
            </fo:block-container>
             
            <fo:block-container height="0.5cm" width="4cm" top="5.5cm" left="17cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="1cm"/>
                    <fo:table-column column-width="0.5cm"/>
                    <fo:table-column column-width="2cm"/>
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
                         
            <fo:block-container height="0.5cm" width="4cm" top="6.5cm" left="17cm" padding="1mm" position="absolute">  
               <fo:block text-align="center" line-height="14pt" font-family="sans-serif" font-size="10pt">
               -
               </fo:block>
            </fo:block-container> 
            
             <fo:block-container height="0.5cm" width="4cm" top="7.5cm" left="17cm" padding="1mm" position="absolute">
            	<fo:block text-align="center" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    XXXXXXXXXX
                </fo:block>
            </fo:block-container>     
                    
            <fo:block-container height="1.3cm" width="8.5cm" top="8cm" left="12cm" padding="1mm" position="absolute">
                <fo:table>
                	<fo:table-column column-width="5cm"/>
                    <fo:table-column column-width="1cm"/>
                    <fo:table-column column-width="2.5cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     
   					
   					<fo:table-row line-height="12pt">
   							<fo:table-cell padding="2pt">
                                <fo:block text-align="start">Komisi</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%= JSPUtil.printX(pol.getStCurrencyCode()) %></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="end"><%= JSPUtil.printX(com_amount,2) %></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                     </fo:table-body>
                    </fo:table> 
            </fo:block-container>
            
            <fo:block-container height="0.5cm" width="4cm" top="8.7cm" left="17cm" padding="1mm" position="absolute">
            	<fo:block text-align="center" line-height="14pt" font-family="sans-serif" font-size="10pt">
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
           
            
            <fo:block-container height="1.3cm" width="8.5cm" top="9.3cm" left="12cm" padding="1mm" position="absolute">
                <fo:table>
                	<fo:table-column column-width="5cm"/>
                    <fo:table-column column-width="1cm"/>
                    <fo:table-column column-width="2.5cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     
   					
   					<fo:table-row line-height="12pt">
   							<fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%= tax %></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%= JSPUtil.printX(pol.getStCurrencyCode()) %></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="end"><%= JSPUtil.printX(tax_amount,2) %></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                     </fo:table-body>
                    </fo:table> 
            </fo:block-container>
            
            <fo:block-container height="0.5cm" width="4cm" top="10.7cm" left="17cm" padding="1mm" position="absolute">
            	<fo:block text-align="center" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    -
                </fo:block>
            </fo:block-container>   
            
            
            
            <fo:block-container height="0.5cm" width="15cm" top="12cm" left="1.5cm" padding="1mm" position="absolute">
            	<fo:block text-align="start" line-height="14pt" font-family="sans-serif" font-size="10pt">
                   - Untuk keuntungan Saudara, apabila pembayaran sudah kami terima
                </fo:block>
            </fo:block-container>
           
            <fo:block-container height="0.5cm" width="4cm" top="12cm" left="17cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="1cm"/>
                    <fo:table-column column-width="0.5cm"/>
                    <fo:table-column column-width="2cm"/>
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


      <fo:block-container height="1.5cm" width="14cm" top="3cm" left="1cm" padding="1mm" position="absolute">
                <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%=pol.getStCustomerName()%>
                </fo:block>
                <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%=pol.getStCustomerAddress()%>
                </fo:block>
            </fo:block-container>
            
            <fo:block-container height="1cm" width="5cm" top="1cm" left="15cm" padding="1mm" position="absolute">
                <fo:block text-align="right"
            font-size="16pt"
            font-family="TAHOMA"
            line-height="12pt" 
            font-weight="bold">
                    {L-ENG CREDIT NOTE -L}{L-INA NOTA KREDIT -L}
                </fo:block>
            </fo:block-container>

       <fo:block-container  height="1.5cm" width="3.5cm" top="2.5cm" left="15cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="1.5cm"/>
                    <fo:table-column column-width="0.5cm"/>
                    <fo:table-column column-width="3.5cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">    
   					
   					<fo:table-row >
                            <fo:table-cell padding="2pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt"><fo:block text-align="start">F<%=pol.getStPolicyNo().substring(2,4)+""+pol.getStPolicyNo().substring(10,12)+""+pol.getStPolicyNo().substring(0,2)+""+pol.getStPolicyNo().substring(4,6)+""+pol.getStPolicyNo().substring(12,16)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row >
                            <fo:table-cell padding="2pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt"><fo:block text-align="center">--</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row >
                            <fo:table-cell padding="2pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt"><fo:block text-align="start"></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt"><fo:block text-align="start"><%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block-container>
            
            
            <fo:block-container height="0.5cm" width="4.5cm" top="5.5cm" left="1cm" padding="1mm" position="absolute">
                <fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%>
                </fo:block>
            </fo:block-container>
            
            <fo:block-container height="0.5cm" width="6cm" top="5.5cm" left="6cm" padding="1mm" position="absolute">
                <fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%=pol.getStPolicyTypeDesc2()%>
                </fo:block>
            </fo:block-container>
            
            <fo:block-container height="0.5cm" width="2.3cm" top="5.5cm" left="12cm" padding="1mm" position="absolute">
                <fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d-MM-yyyy")%>
                 </fo:block>
            </fo:block-container>
            
            <fo:block-container height="0.5cm" width="2.5cm" top="5.5cm" left="14.3cm" padding="1mm" position="absolute">
                <fo:block text-align="center" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"d-MM-yyyy")%>
                 </fo:block>
            </fo:block-container>
             
            <fo:block-container height="0.5cm" width="4cm" top="5.5cm" left="17cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="1cm"/>
                    <fo:table-column column-width="0.5cm"/>
                    <fo:table-column column-width="2cm"/>
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
                         
            <fo:block-container height="0.5cm" width="4cm" top="6.5cm" left="16.5cm" padding="1mm" position="absolute">  
               <fo:block text-align="center" line-height="14pt" font-family="sans-serif" font-size="10pt">
               -
               </fo:block>
            </fo:block-container> 
            
             <fo:block-container height="0.5cm" width="4cm" top="7.5cm" left="17cm" padding="1mm" position="absolute">
            	<fo:block text-align="center" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    XXXXXXXXXX
                </fo:block>
            </fo:block-container>      
                    
            <fo:block-container height="1.3cm" width="8.5cm" top="8cm" left="12cm" padding="1mm" position="absolute">
                <fo:table>
                	<fo:table-column column-width="5cm"/>
                    <fo:table-column column-width="1cm"/>
                    <fo:table-column column-width="2.5cm"/>
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
            
            <fo:block-container height="0.5cm" width="4cm" top="8.9cm" left="17cm" padding="1mm" position="absolute">
            	<fo:block text-align="center" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    XXXXXXXXXX
                </fo:block>
            </fo:block-container> 
            
            <fo:block-container height="1.3cm" width="8.5cm" top="9.5cm" left="12cm" padding="1mm" position="absolute">
                <fo:table>
                	<fo:table-column column-width="5cm"/>
                    <fo:table-column column-width="1cm"/>
                    <fo:table-column column-width="2.5cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="8pt">     
   					
   					<fo:table-row line-height="12pt">
   							<fo:table-cell padding="2pt">
                                <fo:block text-align="start"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center">-</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                     </fo:table-body>
                    </fo:table> 
            </fo:block-container>
            
            <fo:block-container height="0.5cm" width="4cm" top="10.7cm" left="16.5cm" padding="1mm" position="absolute">
            	<fo:block text-align="center" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    -
                </fo:block>
            </fo:block-container>   
            
            
            
            <fo:block-container height="0.5cm" width="15cm" top="12cm" left="1cm" padding="1mm" position="absolute">
            	<fo:block text-align="start" line-height="14pt" font-family="sans-serif" font-size="10pt">
                   - Untuk keuntungan Saudara, apabila pembayaran sudah kami terima
                </fo:block>
            </fo:block-container>
           
            <fo:block-container height="0.5cm" width="4cm" top="12cm" left="17cm" padding="1mm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="1cm"/>
                    <fo:table-column column-width="0.5cm"/>
                    <fo:table-column column-width="2cm"/>
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



