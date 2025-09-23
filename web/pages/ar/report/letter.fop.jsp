<%@ page import="com.webfin.ar.forms.ReceiptForm,
                 com.crux.web.controller.SessionManager,
                 java.util.ArrayList,
                 com.crux.util.fop.FOPUtil,
                 com.crux.util.SQLAssembler,
                 com.webfin.ar.model.ARInvoiceView,
                 com.webfin.ar.model.ARInvoiceDetailView,
                 com.webfin.ar.model.ARReceiptView, 
                 com.webfin.ar.model.ARReceiptLinesView,              
                 com.crux.util.DTOList,
                 com.crux.util.BDUtil,
                 com.crux.util.JSPUtil,
                 java.math.BigDecimal,
                 com.crux.lang.LanguageManager,
                 com.webfin.insurance.model.*,
                 com.crux.util.*, 
                 java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <!-- defines the layout master -->
  <fo:layout-master-set>
    <fo:simple-page-master master-name="first"
                           page-height="29.5cm"
                           page-width="21cm"
                           margin-top="2.5cm"
                           margin-bottom="1cm"
                           margin-left="1cm"
                           margin-right="1cm">
      <fo:region-body margin-top="1cm" margin-bottom="1cm"/>
      <fo:region-before extent="0.5cm"/>
      <fo:region-after extent="0.5cm"/>
    </fo:simple-page-master>
  </fo:layout-master-set>

  <!-- starts actual layout -->
  <fo:page-sequence master-reference="first">
<%

   ReceiptForm form =  (ReceiptForm) request.getAttribute("FORM");
   
   DTOList line =  (DTOList) request.getAttribute("RPT");
   
   ArrayList colW = new ArrayList();

   colW.add(new Integer(2));
   colW.add(new Integer(7));
   
%>
<fo:flow flow-name="xsl-region-body">
<%
   String bw = "0.2pt";
%>
	
      <fo:block font-family="Helvetica" font-weight="bold" text-align="center" line-height="20mm">SURAT PENGANTAR</fo:block>
   <fo:block space-after.optimum="14pt"/>
  <fo:block font-family="Helvetica" font-size="6pt" display-align="center" border-width="0.1pt">
   <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>" >
   <fo:table-header>
   
	  <fo:table-row>   
          <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm"   text-align="center" font-size="10pt" >No</fo:block></fo:table-cell>
         <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="1pt"  border-left-style="solid"><fo:block line-height="6mm"  text-align="center" font-size="10pt" >No Polis</fo:block></fo:table-cell>
      </fo:table-row>
      
	</fo:table-header>
	
      <%=FOPUtil.printColumnWidth(colW,22,2,"cm")%>
      <fo:table-body>    
        
<%
   for(int j=0;j<line.size();j++){
      ARReceiptLinesView view = (ARReceiptLinesView) line.get(j);
%>
      <fo:table-row>
            <fo:table-cell padding="2pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="8pt"><%=String.valueOf(j+1)%>.</fo:block></fo:table-cell>
            <fo:table-cell padding="2pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="8pt"><%=JSPUtil.print(view.getInvoice().getStAttrPolicyNo().substring(0,4)+"-"+view.getInvoice().getStAttrPolicyNo().substring(4,8)+"-"+view.getInvoice().getStAttrPolicyNo().substring(8,12)+"-"+view.getInvoice().getStAttrPolicyNo().substring(12,16)+"-"+view.getInvoice().getStAttrPolicyNo().substring(16,18))%></fo:block></fo:table-cell>
      	</fo:table-row>
<%
   }
%> 
             <fo:table-row>   
              <fo:table-cell number-columns-spanned="2" >  
                   <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="2pt" space-after.optimum="2pt"></fo:block>   
              </fo:table-cell>   
            </fo:table-row> 
            
         <fo:table-row  >
            <fo:table-cell border-bottom-style="solid" padding="2pt" number-columns-spanned="4"><fo:block text-align="center" font-size="8pt" font-weight="bold">JUMLAH</fo:block></fo:table-cell>
			<fo:table-cell border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt" font-weight="bold"></fo:block></fo:table-cell>
		</fo:table-row  >
		
      </fo:table-body>      
    </fo:table>
   </fo:block>
 
</fo:flow>
</fo:page-sequence>
</fo:root>

