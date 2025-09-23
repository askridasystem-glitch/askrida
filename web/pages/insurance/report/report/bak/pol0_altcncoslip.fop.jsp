<%@ page import="com.webfin.insurance.model.*,
com.crux.ff.model.FlexFieldHeaderView,
com.crux.ff.model.FlexFieldDetailView,
com.crux.util.*,
com.crux.util.fop.FOPUtil,
com.webfin.entity.model.EntityView,
java.math.BigDecimal"%><?xml version="1.0" encoding="utf-8"?>
<%

final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
final String fontsize = (String) request.getAttribute("FONTSIZE");
final String attached = (String) request.getAttribute("attached");
boolean isAttached = attached.equalsIgnoreCase("1")?false:true;
boolean tanpaTanggal = attached.equalsIgnoreCase("3")?false:true;

boolean effective = pol.isEffective();

BigDecimal comm = new BigDecimal(0);
BigDecimal bfee = new BigDecimal(0);
BigDecimal hfee = new BigDecimal(0);
BigDecimal nominal = new BigDecimal(0);

%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->

        <fo:simple-page-master master-name="first"
                               page-height="15cm"
                               page-width="21cm"
                               margin-top="2.5cm"
                               margin-bottom="0.5cm"
                               margin-left="1.5cm"
                               margin-right="1.5cm">	
            
            <fo:region-body margin-top="1cm" margin-bottom="1cm"/>
            <fo:region-before extent="1.5cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>
        
    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="first" initial-page-number="1">
        
        <!-- usage of page layout -->
        <!-- HEADER -->
  
        <fo:static-content flow-name="xsl-region-after">
            <fo:block text-align="end"
                      font-size="6pt"
                      
                      line-height="12pt" >
                CNCOSLIP - PT. Asuransi Bangun Askrida 
            </fo:block>
            
        </fo:static-content>
        
        <fo:flow flow-name="xsl-region-body">
            
            <%
            int pn=0;
            
            /*      final DTOList objects = pol.getObjects();
 
            for (int i = 0; i < objects.size(); i++) {
            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
             */
            DTOList coinz = pol.getCoins2();
            
            for (int j = 0; j < coinz.size(); j++) {
                InsurancePolicyCoinsView ci = (InsurancePolicyCoinsView) coinz.get(j);
                
                if (ci.isHoldingCompany()) continue;
                
                EntityView reasuradur = ci.getEntity();
                
                String ciSlipNo = pol.getStPolicyNo()+j;
                
                if (ci.isCommission()) {
                    comm = ci.getDbCommissionAmount();
                }
                if (ci.isBrokerage()) {
                    bfee = ci.getDbBrokerageAmount();
                }
                if (ci.isHandlingFee()) {
                    hfee = ci.getDbHandlingFeeAmount();
                }
                
                nominal = BDUtil.add(comm, BDUtil.add(bfee,hfee));
                
                
            /*
            final DTOList treatyDetails = obj.getTreatyDetails();
 
            for (int j = 0; j < treatyDetails.size(); j++) {
            InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) treatyDetails.get(j);
 
            final boolean nonProportional = trd.getTreatyDetail().getTreatyType().isNonProportional();
 
            if (!nonProportional) continue;
 
            final DTOList shares = trd.getShares();
 
            for (int k = 0; k < shares.size(); k++) {
            InsurancePolicyReinsView ri = (InsurancePolicyReinsView) shares.get(k);
 
            final EntityView reasuradur = ri.getEntity();
             */
                
                pn++;
            
            %>
            <%if (pn>1) {%>
            <fo:block break-after="page"></fo:block>
            <% } %>
            
            <% if (!effective) {%>
            <fo:block font-size="14pt" 
                      line-height="16pt" space-after.optimum="10pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>
            
            <fo:block font-size="16pt"  line-height="12pt" space-after.optimum="5pt" color="black" >
                {L-ENG CREDIT NOTE-L}{L-INA NOTA KREDIT-L}
            </fo:block>
            
            <fo:block font-size="16pt"  line-height="12pt" color="black" space-after.optimum="20pt" >
                {L-ENG CO-INSURANCE-L}{L-INA KO-ASURANSI-L}
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" >
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG TO-L}{L-INA KEPADA-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= reasuradur.getStEntityName() %></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG ADDRESS-L}{L-INA ALAMAT-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block space-after.optimum="5pt"><%= reasuradur.getStAddress() %></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" >
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        
                        <!-- GARIS DALAM KOLOM -->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Type of Insurance-L}{L-INA Jenis Asuransi-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStPolicyTypeDesc2()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Policy Number-L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Address-L}{L-INA Alamat-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                            <% if (pol.getStCustomerAddress()!=null) { %><%=JSPUtil.printX(pol.getStCustomerAddress())%><% } else { %><% } %></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        
                        <%
                        final FlexFieldHeaderView objectMap = pol.getPolicyType().getObjectMap();
                        
                        final DTOList objectMapDetails = objectMap==null?null:objectMap.getDetails();
                        
                        //final InsurancePolicyObjDefaultView io = obj;
                        
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Period-L}{L-INA Jangka Waktu-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(DateUtil.getDateStr(pol.getDtPeriodStart(),"d ^^ yyyy")+" {L-ENG Up To-L}{L-INA Sampai-L} ")+DateUtil.getDateStr(pol.getDtPeriodEnd(),"d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Total Sum Insured-L}{L-INA Jumlah Pertanggungan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="40mm"/>
                                        <fo:table-column column-width="5mm"/>
                                        <fo:table-column column-width="10mm"/>
                                        <fo:table-column column-width="40mm"/>
                                        <fo:table-column column-width="64mm"/>
                                        
                                        
                                        <fo:table-column />
                                        <fo:table-body>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(),2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                        </fo:table-body>
                                    </fo:table>
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Your Share-L}{L-INA Saham Saudara-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="40mm"/>
                                        <fo:table-column column-width="5mm"/>
                                        <fo:table-column column-width="10mm"/>
                                        <fo:table-column column-width="40mm"/>
                                        <fo:table-column column-width="64mm"/>
                                        <fo:table-column />
                                        <fo:table-body>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(ci.getDbAmount(),2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                        </fo:table-body>
                                    </fo:table>
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        <%-- 
 <fo:table-row>
             <fo:table-cell ><fo:block>{L-ENG Conditions-L}{L-INA Kondisi-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>

<%
	final DTOList objects = pol.getObjects();

      for (int i = 0; i < objects.size(); i++) {
         InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);

   final DTOList covers = obj.getCoverage();

   for (int m = 0; m < covers.size(); m++) {
      InsurancePolicyCoverView cov = (InsurancePolicyCoverView) covers.get(m);
      
      if (m>0) out.print("; ");
      //out.print(JSPUtil.print(cov.getStInsCoverDesc()));
      	if (cov.getDbRatePct()!=null) {
      		out.print(JSPUtil.printX(cov.getStInsCoverDesc())+": "+JSPUtil.printX(cov.getDbRatePct(),4)+" %");
      	} else {
      		out.print(JSPUtil.printX(cov.getStInsCoverDesc()));
      	}
      %>
      
<% 		} 
	}
%>
                  
             </fo:block></fo:table-cell>
           </fo:table-row>           

           <fo:table-row>
             <fo:table-cell ><fo:block >{L-ENGHandling Fee-L}{L-INA Handling Fee-L}</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.print(ci.getDbHandlingFeeRate(),2)%>%</fo:block></fo:table-cell>
           </fo:table-row>

                   <fo:table-row>
                      <fo:table-cell number-columns-spanned="3" >
                           <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="5pt"></fo:block>
                      </fo:table-cell>
                    </fo:table-row>
 --%>
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <!-- defines text title level 1-->
      

            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="153mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        
                        <!-- GARIS DALAM KOLOM -->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="40mm"/>
                                        <fo:table-column column-width="2mm"/>
                                        <fo:table-column column-width="10mm"/>
                                        <fo:table-column column-width="30mm"/>
                                        
                                        
                                        <fo:table-column />
                                        <fo:table-body>
                                            
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Premium-L}{L-INA Premi-L}</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(ci.getDbPremiAmount(),2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                            
                                            
                                            <% if (ci.isCommission()) { %>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Commission -L}{L-INA Komisi-L} <%=JSPUtil.print(ci.getDbCommissionRate(),2)%>%</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(comm,2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                            
                                            <% } %>
                                            <% if (ci.isBrokerage()) { %>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Brokerage Fee-L}{L-INA Brokerage Fee-L} <%=JSPUtil.print(ci.getDbBrokerageRate(),2)%>%</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(bfee,2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                            
                                            <% } %>
                                            <% if (ci.isHandlingFee()) { %>
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Handling Fee-L}{L-INA Handling Fee-L} <%=JSPUtil.print(ci.getDbHandlingFeeRate(),2)%>%</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(hfee,2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                            <% } %>
                                            
                                            <fo:table-row>
                                                <fo:table-cell ></fo:table-cell>
                                                <fo:table-cell ></fo:table-cell>
                                                <fo:table-cell number-columns-spanned="2" >
                                                    <fo:block border-width="0.50pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="0.75pt"></fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>
                                            
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block>{L-ENG Nett Premium-L}{L-INA Premi Netto-L}</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>                                                
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.print(BDUtil.sub(ci.getDbPremiAmount(), nominal),2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                            
                                            <fo:table-row>
                                                <fo:table-cell ></fo:table-cell>
                                                <fo:table-cell ></fo:table-cell>
                                                <fo:table-cell number-columns-spanned="2" >
                                                    <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="0.75pt"></fo:block>
                                                </fo:table-cell>
                                            </fo:table-row>
                                            
                                            
                                        </fo:table-body>
                                    </fo:table>
                                    
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="150mm"/>
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center" space-before.optimum="10pt"><%= JSPUtil.printX(pol.getStCostCenterDesc())%>, <%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            
            <%
            //            }
            
            }
            
            %>
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>



