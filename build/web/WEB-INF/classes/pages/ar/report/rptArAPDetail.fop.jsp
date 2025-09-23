<%@ page import="com.crux.util.fop.FOPTableSource,
                 com.crux.common.model.HashDTO,
                 com.crux.util.*,
                 java.util.Date,
                 com.webfin.ar.forms.FRRPTrptArAPDetailForm"%><?xml version="1.0" encoding="utf-8"?>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
<%
   /*final FRRPTrptArAPDetailForm form = (FRRPTrptArAPDetailForm) request.getAttribute("FORM");

   String policyno = (String) form.getAttribute("policyno");
   Date policydatefrom = (Date) form.getAttribute("policydatefrom");
   Date policydateto = (Date) form.getAttribute("policydateto");
   String entity = (String) form.getAttribute("entity");
   String customer = (String) form.getAttribute("customer");
   String customer_desc = (String) form.getAttribute("customer_desc");
   String entity_desc = (String) form.getAttribute("entity_desc");
   String account = (String) form.getAttribute("account");
   String trxtype = (String) form.getAttribute("trxtype");*/
   
   final FRRPTrptArAPDetailForm form = (FRRPTrptArAPDetailForm) request.getAttribute("FRX");
   
   final FOPTableSource source = (FOPTableSource) request.getAttribute("RPT");

%>

  <!-- defines page layout -->
  <fo:layout-master-set>

    <!-- layout for the first page -->
    <fo:simple-page-master master-name="only"
                  page-width="30cm"
                  page-height="21cm"
                  margin-top="0.5cm"
                  margin-bottom="0.5cm"                                                                                                                                                                           
                  margin-left="1cm"
                  margin-right="1cm">
      <fo:region-body margin-top="2cm" margin-bottom="0.5cm"/>
      <fo:region-before extent="3cm"/>
      <fo:region-after extent="1.5cm"/>
    </fo:simple-page-master>

  </fo:layout-master-set>
  <!-- end: defines page layout -->

  <!-- actual layout -->
  <fo:page-sequence master-reference="only" initial-page-number="1">

    <!-- usage of page layout -->
    <fo:static-content flow-name="xsl-region-before">
      <fo:block text-align="end"
            font-size="10pt"
            font-family="serif"
            line-height="14pt" >
        Page <fo:page-number/>
      </fo:block>
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">
		
	  
      <fo:block font-size="12pt" line-height="12pt" text-align="center">Transaction Report</fo:block>
     
      <% if(form.getPolicyno()!=null){%>
      <fo:block font-size="12pt" line-height="12pt" text-align="center">policyno = <%=JSPUtil.printNVL(form.getPolicyno(),"ALL")%></fo:block>
      <% } %>
      <% if(form.getPolicydatefrom()!=null){%>
      <fo:block font-size="12pt" line-height="12pt" text-align="center">datefrom = <%=JSPUtil.printNVL(form.getPolicydatefrom(),"ALL")%></fo:block>
      <% } %>
      <% if(form.getPolicydateto()!=null){%>
      <fo:block font-size="12pt" line-height="12pt" text-align="center">dateto = <%=JSPUtil.printNVL(form.getPolicydateto(),"ALL")%></fo:block>
      <% } %>
      <% if(form.getEntity_desc()!=null){%>
      <fo:block font-size="12pt" line-height="12pt" text-align="center">Entity = <%=JSPUtil.printNVL(form.getEntity_desc(),"ALL")%></fo:block>
      <% } %>
      <% if(form.getCustomer_desc()!=null){%>
      <fo:block font-size="12pt" line-height="12pt" text-align="center">Customer = <%=JSPUtil.printNVL(form.getCustomer_desc(),"ALL")%></fo:block>
      <% } %>
      <% if(form.getAccount()!=null){%>
      <fo:block font-size="12pt" line-height="12pt" text-align="center">Account = <%=JSPUtil.printNVL(form.getAccount(),"ALL")%></fo:block>
	  <% } %>
      <fo:block font-size="8pt" line-height="20pt" ></fo:block>

      <!-- defines text title level 1-->
      <fo:block font-size="6pt" line-height="8pt" >
<%

   final FOPTableSource fopTableSource = (FOPTableSource)request.getAttribute("RPT");
   fopTableSource.display(out);
%>
       </fo:block>
    </fo:flow>
  </fo:page-sequence>
</fo:root>
