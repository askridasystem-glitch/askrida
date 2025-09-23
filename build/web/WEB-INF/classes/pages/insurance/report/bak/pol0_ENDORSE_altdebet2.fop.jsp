<%@ page import="com.webfin.insurance.model.*,
com.crux.ff.model.FlexFieldHeaderView,
com.crux.ff.model.FlexFieldDetailView,
com.crux.util.*,
java.util.Date,
com.crux.util.Tools,
com.crux.util.fop.FOPUtil,
com.webfin.entity.model.EntityView,
com.webfin.ar.model.ARTaxView,
java.math.BigDecimal"%><?xml version="1.0" encoding="utf-8"?>
<%

final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
final String fontsize = (String) request.getAttribute("FONTSIZE");

boolean effective = pol.isEffective();

//final long n = pol.getStInstallmentPeriods();

//if (true) throw new NullPointerException();

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->

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
            <!-- ROW -->
    
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
                            <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(pol.getStCustomerName())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.xmlEscape(pol.getStCustomerAddress())%></fo:block></fo:table-cell>
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
            <fo:block font-size="16pt"
                      line-height="16pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>
            <fo:block space-after.optimum="10pt"></fo:block>
            
            <fo:block text-align="center"
                   font-size="16pt"
                  
                   line-height="12pt" 
                   font-weight="bold">
                {L-ENG DEBIT NOTE-L}{L-INA NOTA DEBET-L}
            </fo:block>
            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="15pt"></fo:block>
            
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="143mm"/>
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
                            <fo:table-cell ><fo:block>{L-ENG Period-L}{L-INA Jangka Waktu-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d ^^ yyyy")%> {L-ENG up to-L}{L-INA s/d-L} <%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Sum Insured-L}{L-INA Harga Pertanggungan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%> <%=JSPUtil.printX(pol.getDbInsuredAmount())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <%--         <fo:table-row>
                      <fo:table-cell number-columns-spanned="3" >
                           <fo:block border-width="0.10pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="15pt" space-after.optimum="5pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>--%>

                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="15pt" space-after.optimum="5pt"></fo:block>
            
            
            
            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>" line-height="10pt" space-after.optimum="5pt" text-align="justify" > </fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="37mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Details-L}{L-INA Detil-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Gross Premium-L}{L-INA Premi Bruto-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                        final DTOList details = pol.getDetails();
                        
                        for (int i = 0; i < details.size(); i++) {
                            InsurancePolicyItemsView det = (InsurancePolicyItemsView) details.get(i);
                            
                            if (!det.isDiscount()) continue;
                            
                            String rate = "";
                            
                            if (det.isEntryByRate()) rate = JSPUtil.printX(det.getDbRate(),2) + "%";
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Discount-L}{L-INA Diskon-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=rate%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(det.getDbAmount(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% } %>
                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotalAfterDisc(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <!-- ROW -->
                        <fo:table-row><fo:table-cell>
                                <fo:block space-after.optimum="5pt"></fo:block>
                        </fo:table-cell></fo:table-row>
                        
                        <%
                        for (int i = 0; i < details.size(); i++) {
                            InsurancePolicyItemsView det = (InsurancePolicyItemsView) details.get(i);
                            
                            if (!det.isFee()) continue;
                        /*
                        String rate = "";
 
                        rate = JSPUtil.printX(det.getDbRate(),2) + "%";
                         */
                        %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(det.getStDescription2())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(det.getDbAmount(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% } %>
                        
                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTotalFee(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <!-- ROW -->
                        <fo:table-row><fo:table-cell>
                                <fo:block space-after.optimum="5pt"></fo:block>
                        </fo:table-cell></fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>{L-ENG Due To Us-L}{L-INA Total Tagihan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(pol.getDbTotalDue(),pol.getDbNDPPN()),2)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <!-- GARIS DALAM KOLOM -->

                        <fo:table-row>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2" >
                                <fo:block border-width="0.35pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="0.15pt"></fo:block>
                                <fo:block font-size="<%=fontsize%>" font-family="SANS-SERIF" line-height="10pt" space-after.optimum="1pt" text-align="justify" > </fo:block>
                                <fo:block border-width="0.35pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="0.15pt"></fo:block> 
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" font-family="SANS-SERIF" line-height="10pt" space-after.optimum="10pt" text-align="justify" > </fo:block>
            
            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" space-after.optimum="5pt" line-height="0.15pt"></fo:block>
            
            <fo:block font-size="<%=fontsize%>" font-weight="bold">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="180mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="left">{L-ENG SAY :-L}{L-INA TERBILANG :-L} <%=NumberSpell.readNumber(JSPUtil.printX(BDUtil.sub(pol.getDbTotalDue(),pol.getDbNDPPN()),2), pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <!-- GARIS DALAM KOLOM -->

            
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>
            
            
            <fo:block font-size="6pt"
                      font-family="sans-serif"
                      line-height="10pt"
                      space-after.optimum="10pt"
                      text-align="left" >
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(),"dd/MM/yyyy HH:mm:ss"))%> PrintStamp:<%=pol.getStPrintCode()%> PrintCode:<%=pol.getStPrintStamp()%> Page:<fo:page-number/> Terlampir : [<%=JSPUtil.print(request.getAttribute("DOCUMENT_NAME"))%>]
                
            </fo:block>
            
            
            <!-- CICILAN -->

            <!-- ROW -->
            <fo:block font-size="<%=fontsize%>"
                      line-height="10pt" space-after.optimum="2pt"
                      text-align="justify" > </fo:block>
            
            <% //if(pol.getStInstallmentPeriods()>=2)
            {

            DTOList installment = pol.getInstallment();

            if (installment.size()>1) {
            
            %>
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-body>
                        
                        <!-- INTEREST START -->
       
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Tanggal</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">Jumlah</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-column-spanned="4"><fo:block font-size="<%=fontsize%>" space-after.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <%
                        
                        for (int i = 0; i < installment.size(); i++) {
                            InsurancePolicyInstallmentView ins = (InsurancePolicyInstallmentView) installment.get(i);
                        
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>Installment <%=(i+1)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(ins.getDtDueDate())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(ins.getDbAmount(),2)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% } %>
                    </fo:table-body>
                </fo:table>
            </fo:block>
            <% } %>
            <% } %>
            
            
            
            <%-- 

<% if (n>=2) { %>

 <fo:block font-size="<%=fontsize%>" line-height="10pt"  text-align="justify" > </fo:block>

     <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="37mm"/>
         <fo:table-column column-width="3mm"/>
         <fo:table-column column-width="30mm"/>
         <fo:table-column column-width="5mm"/>
         <fo:table-column />
         <fo:table-body>
         
         	<fo:table-row>
         	 <fo:table-cell></fo:table-cell>
         	 <fo:table-cell></fo:table-cell>
         	 <fo:table-cell><fo:block>{L-ENG Due Date-L}{L-INA Tanggal Tagihan-L}</fo:block></fo:table-cell>
         	 <fo:table-cell></fo:table-cell>
         	 <fo:table-cell><fo:block>{L-ENG Amount-L}{L-INA Jumlah-L}</fo:block></fo:table-cell>
         	</fo:table-row>
         
           <fo:table-row>
         	<fo:table-cell><fo:block>{L-ENG Installment Periods-L}{L-INA Jumlah Cicilan-L}</fo:block></fo:table-cell>
         	<fo:table-cell><fo:block>:</fo:block></fo:table-cell>
         	<fo:table-cell><fo:block>
			 <fo:block font-size="<%=fontsize%>">
        	  <fo:table table-layout="fixed">
        	   <fo:table-column column-width="30mm"/>
        	   <fo:table-column column-width="5mm"/>
         	   <fo:table-column column-width="30mm"/>
         	   <fo:table-body>
         	   
<%   final InsurancePeriodView instPeriod = pol.getInstallmentPeriod();

	  final BigDecimal periodAmount = BDUtil.div(pol.getDbPremiTotalAfterDisc(), new BigDecimal(n));

      final BigDecimal roundingErr = BDUtil.sub(pol.getDbPremiTotalAfterDisc(), BDUtil.mul(periodAmount, new BigDecimal(n)));
      
      Date perDate = pol.getDtPeriodStart();

      if (perDate==null) return;
 		
 		for (int i=1;i<=n;i++) {
         final InsurancePolicyInstallmentView inst = new InsurancePolicyInstallmentView();
         
         if (i==0)
            for (int j = 0; j < details.size(); j++) {
               InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(j);

               if (item.isFee()) inst.setDbAmount(BDUtil.add(inst.getDbAmount(), item.getDbAmount()));
            }

         inst.setDbAmount(periodAmount);
         inst.setDtDueDate(perDate);

         if (i==1) inst.setDbAmount(BDUtil.add(inst.getDbAmount(), roundingErr));
         if (i==1) inst.setDbAmount(BDUtil.add(inst.getDbAmount(), pol.getDbTotalFee()));
         
         if (instPeriod!=null)
            perDate = instPeriod.advance(perDate);
%>     
         	  
         	  <fo:table-row>
         	    <fo:table-cell><fo:block><%= JSPUtil.printX(inst.getDtDueDate()) %></fo:block></fo:table-cell>
         	    <fo:table-cell><fo:block><%= JSPUtil.printX(pol.getStCurrencyCode()) %></fo:block></fo:table-cell>
         	    <fo:table-cell><fo:block text-align = "end"><%= JSPUtil.printX(inst.getDbAmount(),2) %></fo:block></fo:table-cell>
         	  </fo:table-row>
         	  
 <% } %>          	  
          	
         	   
           	 </fo:table-body>
            </fo:table>
           </fo:block> 
           </fo:block></fo:table-cell>
 			</fo:table-row>	
           
         	
        </fo:table-body>
       </fo:table>
      </fo:block>  
      
<% }else{ %>

<% } %>      
 --%>
            <!-- ROW -->
   
            <fo:block font-size="<%=fontsize%>" line-height="10pt" space-after.optimum="20pt" text-align="justify" > </fo:block>
            
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



