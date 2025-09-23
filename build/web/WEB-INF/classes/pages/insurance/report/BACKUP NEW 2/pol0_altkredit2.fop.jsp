<%@ page import="com.webfin.insurance.model.*,
                 com.crux.ff.model.FlexFieldHeaderView,
                 com.crux.ff.model.FlexFieldDetailView,
                 com.crux.util.*,
                 com.crux.util.fop.FOPUtil,
                 com.webfin.entity.model.EntityView,
                 com.webfin.ar.model.ARTaxView,
                 com.crux.util.BDUtil,
                 java.math.BigDecimal"%><?xml version="1.0" encoding="utf-8"?>
<%

   final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
   final String fontsize = (String) request.getAttribute("FONTSIZE");
   
   String tax ="";
   String fee ="";
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
    <!-- komisi cuma 13.5cm -->

    <fo:simple-page-master master-name="first"
                  page-height="28cm"
                  page-width="22cm"
                  margin-top="2cm"
                  margin-bottom="0.5cm"
                  margin-left="1.5cm"
                  margin-right="1.5cm">

      <fo:region-body margin-top="3cm" margin-bottom="1.5cm"/>
      <fo:region-before extent="3cm"/>
      <fo:region-after extent="1.5cm"/>
    </fo:simple-page-master>

  </fo:layout-master-set>
  <!-- end: defines page layout -->

  <!-- actual layout -->
 

    <!-- usage of page layout -->
    <!-- HEADER -->
<fo:page-sequence master-reference="first" initial-page-number="1">
<%--   
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
      font-size="6pt" font-family="serif" line-height="1em + 2pt">
      {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
ref-id="end-of-document"/>
      </fo:block>
    </fo:static-content>
 --%>    

    <fo:flow flow-name="xsl-region-body">
    
    
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
      
      fee = dt.getStDescription();

      final EntityView agent = dt.getEntity();

      ln++;

%>
   <%if (ln>1) {%>
   
   <fo:block break-after="page"></fo:block>
   
   <%}%>
   
   <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="15mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column column-width="50mm"/>
         <fo:table-column />
         <fo:table-body>

            <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG TO-L}{L-INA KEPADA-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(agent.getStEntityName())%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(agent.getStAddress())%></fo:block></fo:table-cell>
           </fo:table-row>

   <!-- ROW -->
                    <fo:table-row><fo:table-cell>
                           <fo:block space-after.optimum="10pt"></fo:block>
                    </fo:table-cell></fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>
       
       <fo:block-container height="5cm" width="2cm" top="0cm" left="13cm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="1.5cm"/>
                    <fo:table-column column-width="0.5cm"/>
                    <fo:table-column column-width="5cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="<%=fontsize%>">     
   					
   					<fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start">{L-ENG Number-L}{L-INA Nomor-L}</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%></fo:block>
                                </fo:table-cell>
           					</fo:table-row>
                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start">{L-ENG Dates-L}{L-INA Tanggal-L}</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start">{L-ENG Branch-L}{L-INA Cabang-L}</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%= JSPUtil.printX(pol.getStCostCenterDesc())%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block-container>

   <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="15pt" space-after.optimum="10pt"></fo:block>             
    	
    	<% if (!effective) {%>
      <fo:block font-size="16pt" font-family="TAHOMA"
            line-height="16pt" space-after.optimum="0pt"
            color="red"
            text-align="center"
            padding-top="10pt">
        SPECIMEN
      </fo:block>
<% }%>

<fo:block space-after.optimum="10pt"></fo:block>
    	
    	<fo:block text-align="center"
            font-size="16pt"
            font-family="TAHOMA"
            line-height="12pt" 
            font-weight="bold">
        	{L-ENG CREDIT NOTE-L}{L-INA NOTA KREDIT-L}
      	</fo:block>
      	<fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="15pt"></fo:block>

   
      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Type of Insurance-L}{L-INA Jenis Asuransi-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStPolicyTypeDesc2()%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Policy Number-L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG The Insured-L}{L-INA Nama Tertanggung -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStCustomerName()%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Address-L}{L-INA Alamat -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStCustomerAddress()%></fo:block></fo:table-cell>
           </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column column-width="10mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Sum Insured-L}{L-INA Harga Pertanggungan -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(),2)%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Premium Amount-L}{L-INA Jumlah Premi -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(),2)%></fo:block></fo:table-cell>
           </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Period-L}{L-INA Jangka Waktu-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d ^^ yyyy")%> {L-ENG   up to   -L}{L-INA   s/d   -L}<%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"d ^^ yyyy")%></fo:block></fo:table-cell>
           </fo:table-row>

   <!-- GARIS DALAM KOLOM -->

                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="3" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="15pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column column-width="10mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
<%
	if (fee!=null) {
%>
             <fo:table-cell ><fo:block><%=fee%></fo:block></fo:table-cell>
<% } else { %>
			 <fo:table-cell ><fo:block>{L-ENG Commission-L}{L-INA Komisi -L}</fo:block></fo:table-cell>
<% } %>
			 <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(com_amount,2)%></fo:block></fo:table-cell>
           </fo:table-row>

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
           <fo:table-row>
             <fo:table-cell ><fo:block><%=tax%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(tax_amount,2)%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Due To You-L}{L-INA Untuk Saudara -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(dt.getDbNetAmount(),2)%></fo:block></fo:table-cell>
           </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

   <!-- ROW -->
      <fo:block font-size="<%=fontsize%>" font-family="SANS-SERIF" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>
		
	  <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>

      <fo:block font-size="<%=fontsize%>"
      font-weight="bold">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="180mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="left">{L-ENG SAY :-L}{L-INA TERBILANG :-L} <%=NumberSpell.readNumber(JSPUtil.printX(dt.getDbNetAmount(),2), pol.getStCurrencyCode())%></fo:block></fo:table-cell>
           </fo:table-row>

   <!-- GARIS DALAM KOLOM -->

         </fo:table-body>
       </fo:table>
       </fo:block>
       
       <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" space-before.optimum="5pt" space-after.optimum="15pt" line-height="0.15pt"></fo:block>


   <!-- ROW -->
     

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="130mm"/>
         <fo:table-column />
         <fo:table-body>

   <!-- GARIS DALAM KOLOM -->
   
   		<fo:table-row>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block></fo:table-cell>
           </fo:table-row>
           
           			<fo:table-row><fo:table-cell>
                           <fo:block space-after.optimum="60pt"></fo:block>
                    </fo:table-cell></fo:table-row>
                    
                    
                    <fo:table-row>
                      <fo:table-cell ></fo:table-cell>
                      <fo:table-cell number-columns-spanned="1" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="0.15pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>
           
           <fo:table-row>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="center">{L-ENG AUTHORIZED SIGNATURE-L}{L-INA TANDA TANGAN-L}</fo:block></fo:table-cell>
           </fo:table-row>
           


         </fo:table-body>
       </fo:table>
       </fo:block>
       
 <%--      <fo:block-container height="3cm" width="10cm" top="22cm" left="4cm" position="absolute">
       <fo:block text-align="center" font-size="6pt">PT. ASURANSI BANGUN ASKRIDA</fo:block>
       <fo:block text-align="center" font-size="6pt">Pusat Niaga Cempaka Mas M.1/36 Jl. Letjen Soeprapto Jakarta 10640 </fo:block>
	   <fo:block text-align="center" font-size="6pt">Telp. 021-42877210 Fax. 021-42877215</fo:block>
	   </fo:block-container> --%> 

   <!-- ROW -->

<% } %>
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
   <%if (ln<1) {%>
   
   <fo:block font-size="<%=fontsize%>" font-family="SANS-SERIF" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="15mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column column-width="50mm"/>
         <fo:table-column />
         <fo:table-body>

            <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG TO-L}{L-INA KEPADA-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStCustomerAddress()%></fo:block></fo:table-cell>
           </fo:table-row>

   <!-- ROW -->
                    <fo:table-row><fo:table-cell>
                           <fo:block space-after.optimum="10pt"></fo:block>
                    </fo:table-cell></fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>
       
       <fo:block-container height="5cm" width="2cm" top="0cm" left="13cm" position="absolute">
                <fo:table>
                    <fo:table-column column-width="1.5cm"/>
                    <fo:table-column column-width="0.5cm"/>
                    <fo:table-column column-width="5cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="<%=fontsize%>">     
   					
   					<fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start">{L-ENG Number-L}{L-INA Nomor-L}</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%></fo:block>
                                </fo:table-cell>
           					</fo:table-row>
                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start">{L-ENG Dates-L}{L-INA Tanggal-L}</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row line-height="12pt">
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start">{L-ENG Branch-L}{L-INA Cabang-L}</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="center">:</fo:block>
                            </fo:table-cell>
                            <fo:table-cell padding="2pt">
                                <fo:block text-align="start"><%= JSPUtil.printX(pol.getStCostCenterDesc())%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block-container>
            
            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="15pt" space-after.optimum="10pt"></fo:block>             
    	
    		<% if (!effective) {%>
      <fo:block font-size="16pt" font-family="TAHOMA"
            line-height="16pt" space-after.optimum="0pt"
            color="red"
            text-align="center"
            padding-top="10pt">
        SPECIMEN
      </fo:block>
<% }%>

<fo:block space-after.optimum="10pt"></fo:block>
    	
    	<fo:block text-align="center"
            font-size="14pt"
            font-family="TAHOMA"
            line-height="12pt" 
            font-weight="bold">
        	{L-ENG CREDIT NOTE-L}{L-INA NOTA KREDIT-L}
      	</fo:block>
      	<fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="15pt"></fo:block>



      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Type of Insurance-L}{L-INA Jenis Asuransi-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStPolicyTypeDesc2()%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Policy Number-L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG The Insured-L}{L-INA Nama Tertanggung -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStCustomerName()%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Address-L}{L-INA Alamat -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=pol.getStCustomerAddress()%></fo:block></fo:table-cell>
           </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column column-width="10mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Sum Insured-L}{L-INA Harga Pertanggungan -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(),2)%></fo:block></fo:table-cell>
           </fo:table-row>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Premium Amount-L}{L-INA Jumlah Premi -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(),2)%></fo:block></fo:table-cell>
           </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>

      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Period-L}{L-INA Jangka Waktu-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d ^^ yyyy")%> {L-ENG   up to   -L}{L-INA   s/d   -L}<%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"d ^^ yyyy")%></fo:block></fo:table-cell>
           </fo:table-row>

   <!-- GARIS DALAM KOLOM -->

                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="3" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="5" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>
       
       
       <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Commission-L}{L-INA Komisi -L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
           </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>
       
       <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>

       
       <fo:block font-size="<%=fontsize%>"
       font-weight="bold">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="180mm"/>
         <fo:table-column />
         <fo:table-body>

           <fo:table-row>
             <fo:table-cell ><fo:block text-align="left">{L-ENG SAY :-L}{L-INA TERBILANG :-L}</fo:block></fo:table-cell>
           </fo:table-row>

   <!-- GARIS DALAM KOLOM -->

                    <fo:table-row>
                      <fo:table-cell number-columns-spanned="1" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>

         </fo:table-body>
       </fo:table>
       </fo:block>
       
   <%}%>
   
    </fo:flow>
  </fo:page-sequence>
</fo:root>



