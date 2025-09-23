<%@ page import="com.webfin.insurance.model.*,
                 com.crux.ff.model.FlexFieldHeaderView,
                 com.crux.ff.model.FlexFieldDetailView,
                 com.crux.util.*,
                 com.crux.util.fop.FOPUtil,
                 com.webfin.entity.model.EntityView,
                 com.webfin.ar.model.ARTaxView,
                 java.math.BigDecimal,
                 java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
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

    <fo:simple-page-master master-name="first" 
                  page-height="16cm"
                  page-width="21.5cm">
 
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
   
 			<fo:block-container height="1cm" width="4cm" top="4cm" left="16.5cm" padding="1mm" position="absolute">
                <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="8pt">
                    <%=pol.getStPolicyTypeDesc2()%>
                </fo:block>
             </fo:block-container>
 
             <fo:block-container height="1cm" width="14cm" top="6cm" left="5.5cm" padding="1mm" position="absolute">
                <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    <%=pol.getStCustomerName()%>
                </fo:block>
             </fo:block-container>
            
            <fo:block-container height="1cm" width="14cm" top="7cm" left="5.5cm" padding="1mm" position="absolute">
                 <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    <%=pol.getStCustomerAddress()%>
                </fo:block>
            </fo:block-container>
            
            <fo:block-container height="1cm" width="14cm" top="8.2cm" left="5.5cm" padding="1mm" position="absolute">
                 <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt" font-weight="bold">
                    # <%=NumberSpell.readNumber(JSPUtil.printX(pol.getDbTotalDue(),2))%> RUPIAH #
                </fo:block>
            </fo:block-container>
            
            <fo:block-container height="1cm" width="11cm" top="9.5cm" left="9.5cm" padding="1mm" position="absolute">
                 <fo:table>
                    <fo:table-column column-width="7cm"/>
                    <fo:table-column column-width="4cm"/>
                    <fo:table-body font-family="sans-serif" font-weight="normal" font-size="10pt">
                    
                        <fo:table-row >
                            <fo:table-cell padding="1pt"><fo:block text-align="start">F<%=pol.getStPolicyNo().substring(2,4)+""+pol.getStPolicyNo().substring(10,12)+""+pol.getStPolicyNo().substring(0,2)+""+pol.getStPolicyNo().substring(4,6)+""+pol.getStPolicyNo().substring(12,16)%></fo:block></fo:table-cell>
                            <fo:table-cell padding="1pt"><fo:block text-align="start"><%=DateUtil.getDateStr(pol.getDtPolicyDate(),"dd ^^ yyyy")%></fo:block></fo:table-cell>
                         </fo:table-row>
                         
               </fo:table-body>
              </fo:table>           
            </fo:block-container>
            
             <fo:block-container height="1cm" width="14cm" top="10.5cm" left="3.5cm" padding="1mm" position="absolute">
                 <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    Pembayaran Polis No. <%=JSPUtil.printX(pol.getStPolicyNo().substring(0,4)+"."+pol.getStPolicyNo().substring(4,8)+"."+pol.getStPolicyNo().substring(8,12)+"."+pol.getStPolicyNo().substring(12,16)+"."+pol.getStPolicyNo().substring(16,18))%>
                </fo:block>
            </fo:block-container>
            
            <fo:block-container height="1cm" width="2.5cm" top="13cm" left="17cm" padding="1mm" position="absolute">
                 <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    <%=DateUtil.getDateStr(new Date(),"dd ^^")%>
                </fo:block>
            </fo:block-container>
             
            <fo:block-container height="1cm" width="0.8cm" top="13cm" left="20.5cm" padding="1mm" position="absolute">
                 <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt">
                    <%=DateUtil.getYear2Digit(new Date())%>
                </fo:block>
            </fo:block-container>
            
            <fo:block-container height="1cm" width="4cm" top="13.5cm" left="1.5cm" padding="1mm" position="absolute">
                 <fo:block text-align="start" space-after.optimum="3pt" line-height="14pt" font-family="sans-serif" font-size="10pt" font-weight="bold">
                    <%= JSPUtil.printX(pol.getStCurrencyCode()) %>.  <%=JSPUtil.printX(pol.getDbTotalDue(),2)%>
                </fo:block>
            </fo:block-container>
             
    </fo:flow>
  </fo:page-sequence>
</fo:root>



