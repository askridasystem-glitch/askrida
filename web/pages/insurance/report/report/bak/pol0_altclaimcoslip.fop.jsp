<%@ page import="com.webfin.insurance.model.*,
com.crux.ff.model.FlexFieldHeaderView,
com.crux.ff.model.FlexFieldDetailView,
com.crux.util.*,
java.util.Date,
java.math.BigDecimal,
com.crux.util.fop.FOPUtil,
com.webfin.entity.model.EntityView"%><?xml version="1.0" encoding="utf-8"?>
<%
final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
final String fontsize = (String) request.getAttribute("FONTSIZE");
String objectName = null;
String objectID = null;
Date tglMulaiKreasi=null;
Date tglAkhirKreasi=null;
%>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->

        <fo:simple-page-master master-name="first"
                               page-height="29.5cm"
                               page-width="21.5cm"
                               margin-top="5.5cm"
                               margin-bottom="1cm"
                               margin-left="1cm"
                               margin-right="1cm">
            
            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
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
                      font-family="TAHOMA"
                      line-height="12pt" >
                CNCOSLIP - PT. Asuransi Bangun Askrida 
            </fo:block>      
        </fo:static-content>
        
        <fo:flow flow-name="xsl-region-body">
            
            
            <%
            int pn=0;
            
            final DTOList objects = pol.getObjects();
            
            for (int i = 0; i < objects.size(); i++) {
                InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
                
                DTOList coinz = pol.getCoins2();
                
                for (int j = 0; j < coinz.size(); j++) {
                    InsurancePolicyCoinsView ci = (InsurancePolicyCoinsView) coinz.get(j);
                    
                    if (ci.isHoldingCompany()) continue;
                    
                    EntityView reasuradur = ci.getEntity();
                    
                    pn++;
            
            %>
            <%if (pn>1) {%>
            <fo:block break-after="page"></fo:block>
            <% } %>
            
            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="10pt" space-after.optimum="5pt"></fo:block>
            
            <fo:block font-size="16pt" font-family="TAHOMA" line-height="16pt" color="black" text-align="center">
                {L-ENG DETAILS OF CO-INSURANCE CLAIM NOTE-L}{L-INA PERINCIAN NOTA KLAIM KO-ASURANSI-L}
            </fo:block>
            
            <fo:block font-size="12pt" line-height="10pt" space-before.optimum="5pt" space-after.optimum="10pt" color="black" text-align="center"> NO. <%=pol.getStDLANo()%></fo:block>
            
            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5pt" space-after.optimum="5pt"></fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="136mm"/>
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
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        
                        <!-- GARIS DALAM KOLOM -->
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <% 
                        final DTOList objects3 = pol.getObjectsClaim();
                        for(int k = 0;k < objects3.size(); k++){
                            InsurancePolicyObjDefaultView obj2 = (InsurancePolicyObjDefaultView) objects3.get(k);
                            
                            tglMulaiKreasi = obj2.getDtReference2();
                            
                            tglAkhirKreasi = obj2.getDtReference3();
                        }
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Policy Number-L}{L-INA Nomor Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%= pol.getStPolicyNo().substring(0,4)+"-"+pol.getStPolicyNo().substring(4,8)+"-"+pol.getStPolicyNo().substring(8,12)+"-"+pol.getStPolicyNo().substring(12,16)+"-"+pol.getStPolicyNo().substring(16,18)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Type of Insurance-L}{L-INA Jenis Asuransi-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStPolicyTypeDesc2()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Name of Insured-L}{L-INA Nama Tertanggung-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStCustomerName()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Address-L}{L-INA Alamat-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStCustomerAddress()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Period-L}{L-INA Jangka Waktu-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStPeriodLength()%> Hari</fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Inception Date-L}{L-INA Awal Pertanggungan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <%if(pol.getStPolicyTypeID().equalsIgnoreCase("21")){%>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(tglMulaiKreasi,"d ^^ yyyy")%></fo:block></fo:table-cell>
                            <% }else{%>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodStart(),"d ^^ yyyy")%></fo:block></fo:table-cell>
                            <% }%>           
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Expiry Date-L}{L-INA Akhir Pertanggungan -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <%if(pol.getStPolicyTypeID().equalsIgnoreCase("21")){%>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(tglAkhirKreasi,"d ^^ yyyy")%></fo:block></fo:table-cell>
                            <% }else{%>
                            <fo:table-cell ><fo:block><%=DateUtil.getDateStr(pol.getDtPeriodEnd(),"d ^^ yyyy")%></fo:block></fo:table-cell>
                            <% }%>           
                        </fo:table-row>
                        
                        <%--
<%
   final FlexFieldHeaderView polClaimMap = pol.getClaimFF();

      final DTOList polClaimMapDetails = polClaimMap==null?null:polClaimMap.getDetails();

   if (polClaimMapDetails!=null) {

      %>
      <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="35mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>
      <%


            for (int l = 0; l < polClaimMapDetails.size(); l++) {
               FlexFieldDetailView iomd = (FlexFieldDetailView) polClaimMapDetails.get(l);

               final Object desc = iomd.getDesc(pol);

%>
            <fo:table-row>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(iomd.getStFieldDesc())%></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
             <fo:table-cell ><fo:block><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
           </fo:table-row>
               <%
            }
%>
         </fo:table-body>
       </fo:table>
       </fo:block>
<% } %>  --%>

                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" space-before.optimum="10pt">
                {L-ENG INTEREST INSURED-L}{L-INA OBJEK PERTANGGUNGAN-L}
            </fo:block>
            
            <%
            final DTOList objects2 = pol.getObjectsClaim();
            
            for (int m = 0; m < objects2.size(); m++) {
                InsurancePolicyObjectView io = (InsurancePolicyObjectView) objects2.get(m);
                
                final FlexFieldHeaderView objectMap = io.getClaimFF();
                
                final DTOList objectMapDetails = objectMap==null?null:objectMap.getDetails();
            
            %>
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        <%
                        
                        if (objectMapDetails!=null)
                            for (int n = 0; n < objectMapDetails.size(); n++) {
                    FlexFieldDetailView iomd = (FlexFieldDetailView) objectMapDetails.get(n);
                    
                    final Object desc = iomd.getDesc(io);
                        
                        %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(iomd.getStFieldDesc())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(desc)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                            }
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block space-before.optimum="5pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Sum Insured-L}{L-INA Harga Pertanggungan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:block font-size="<%=fontsize%>">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="35mm"/>
                                            <fo:table-column column-width="2mm"/>
                                            <fo:table-column column-width="5mm"/>
                                            <fo:table-column column-width="30mm"/>
                                            <fo:table-body>
                                                <%
                                                final DTOList suminsureds = io.getSuminsureds();
                                                
                                                for (int p = 0; p < suminsureds.size(); p++) {
                                                    InsurancePolicyTSIView tsi = (InsurancePolicyTSIView) suminsureds.get(p);
                                                %>
                                                
                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printX(tsi.getStInsuranceTSIDesc())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(tsi.getDbInsuredAmount(),2)%></fo:block></fo:table-cell>
                                                </fo:table-row>
                                                <%
                                                
                                                }
                                                %>
                                                
                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Coverage -L}{L-INA Jaminan-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    
                                    <%
                                    final DTOList covers = obj.getCoverage();
                                    
                                    for (int o = 0; o < covers.size(); o++) {
                                        InsurancePolicyCoverView cov = (InsurancePolicyCoverView) covers.get(o);
                                        
                                        if (o>0) out.print("; ");
                                        //out.print(JSPUtil.print(cov.getStInsCoverDesc()));
                                        if (cov.getDbRatePct()!=null) {
                                            out.print(JSPUtil.printX(cov.getStInsCoverDesc())+": "+JSPUtil.printX(cov.getDbRatePct(),4)+" %");
                                        } else {
                                            out.print(JSPUtil.printX(cov.getStInsCoverDesc()));
                                        }
                                    %>
                                    
                                    <% } %>
                                    
                            </fo:block></fo:table-cell>
                        </fo:table-row>            
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Deductible-L}{L-INA Risiko Sendiri-L} </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:block font-size="<%=fontsize%>">
                                        <fo:table table-layout="fixed">
                                            <fo:table-column column-width="35mm"/>
                                            <fo:table-column column-width="2mm"/>
                                            <fo:table-column />
                                            <fo:table-body>
                                                
                                                <%
                                                final DTOList deductibles = io.getDeductibles();
                                                
                                                for (int u = 0; u < deductibles.size(); u++) {
                                                    InsurancePolicyDeductibleView ded = (InsurancePolicyDeductibleView) deductibles.get(u);
                                                
                                                %>
                                                
                                                <fo:table-row>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printX(ded.getStClaimCauseDesc())%></fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                                                    <fo:table-cell ><fo:block><%=JSPUtil.printX(ded.getStAutoDesc())%></fo:block></fo:table-cell>
                                                </fo:table-row>
                                                <%
                                                
                                                }
                                                %>
                                            </fo:table-body>
                                        </fo:table>
                                    </fo:block>
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Your Share-L}{L-INA Saham Saudara-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="5mm"/>
                                        <fo:table-column column-width="30mm"/>
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
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENG Claim-L}{L-INA Klaim-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>
                                    <fo:table table-layout="fixed">
                                        <fo:table-column column-width="5mm"/>
                                        <fo:table-column column-width="30mm"/>
                                        <fo:table-column />
                                        <fo:table-body>
                                            
                                            <fo:table-row>
                                                <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                                                <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(ci.getDbClaimAmount(),2)%></fo:block></fo:table-cell>
                                            </fo:table-row>
                                            
                                        </fo:table-body>
                                    </fo:table>
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block >{L-ENGHandling Fee-L}{L-INA Handling Fee-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.print(ci.getDbHandlingFeeRate(),2)%>%</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell  number-columns-spanned="3"><fo:block space-after.optimum="10pt"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <%} %>
            
            <%-- 
    <fo:block font-size="<%=fontsize%>">
        <fo:table table-layout="fixed">
         <fo:table-column column-width="30mm"/>
         <fo:table-column column-width="2mm"/>
         <fo:table-column />
         <fo:table-body>
         
            <fo:table-row>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block></fo:block></fo:table-cell>
             <fo:table-cell ><fo:block>
               <fo:table table-layout="fixed">
                  <fo:table-column column-width="-2mm"/>
                  <fo:table-column column-width="71mm"/>
                  <fo:table-column column-width="2mm"/>
                  <fo:table-column column-width="5mm"/>
                  <fo:table-column column-width="20mm"/>
                  <fo:table-body>
<%

      final DTOList details = pol.getDetails();

%>


<%

   for (int v = 0; v < details.size(); v++) {
      InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(v);
      if (!item.isDiscount()) continue;

      String desc = item.getStDescription2();

      if (item.isEntryByRate()) desc+=JSPUtil.printX(item.getDbRate()+" %");

%>
<% } %>



<%

   for (int b = 0; b < details.size(); b++) {
      InsurancePolicyItemsView item = (InsurancePolicyItemsView) details.get(b);

      if (!item.isFee()) continue;
%>
<% } %>




                 </fo:table-body>
                </fo:table>
             </fo:block></fo:table-cell>
           </fo:table-row>
        </fo:table-body>
       </fo:table>
       </fo:block>
       --%> 
            <!-- GARIS  -->
            <fo:block border-width="0.50pt" border-style="solid" border-before-precedence="0" line-height="0.50pt" space-after.optimum="10pt"></fo:block>
            
            
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column />
                    <fo:table-body>
                        
                        <% 
                        final DTOList objects4 = pol.getObjectsClaim();
                        for(int r = 0;r<objects4.size();r++){
                            InsurancePolicyObjDefaultView obj4 = (InsurancePolicyObjDefaultView) objects4.get(r);
                            
                            objectName = obj4.getStReference1();
                            
                            objectID = obj4.getStPolicyObjectID();
                        }
                        %>
                        <% if(pol.getStPolicyTypeID().equalsIgnoreCase("21")){%>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>Reinsurer</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStCoinsName()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>Credit Type</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=pol.getStKreasiTypeDesc()%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%}%>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>Object Claim</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=objectName%> Object ID : <%=objectID%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>Claim Date</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDtClaimDate())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <% DTOList cause = pol.getClaimCause();
                        for(int k = 0; k < cause.size(); k++) {
                            InsuranceClaimCauseView cau = (InsuranceClaimCauseView) cause.get(k);
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>Claim Cause</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(cau.getStDescription())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% } %>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>Claim Event Location</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimEventLocation())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>Claim Person Name</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimPersonName())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>Claim Person Address</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimPersonAddress())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>Estimated Amount</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getDbClaimAmountEstimate())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>Amount Currency</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimCurrency())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>Loss Status</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimLossStatusDesc())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>Claim Benefit</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimBenefitDesc())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>Claim Documents</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(pol.getStClaimDocuments())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <%
            final DTOList claimItems = pol.getClaimItems();
            
            
            %>
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="2mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-body>
                        <%
                        
                        
                        BigDecimal amt3 = new BigDecimal(0);
                        for (int g = 0; g < claimItems.size(); g++) {
                            InsurancePolicyItemsView ci2 = (InsurancePolicyItemsView) claimItems.get(g);
                            
                            final boolean negative = ci2.getInsItem().getARTrxLine().isNegative();
                            
                            if (negative) amt3 = BDUtil.sub(amt3,ci2.getDbAmount());
                            else amt3 = BDUtil.add(amt3,ci2.getDbAmount());
                            
                            
                            String amt = JSPUtil.printX(ci2.getDbAmount(),2);
                            
                            if (negative) amt="("+amt+")";
                        
                        
                        
                        
                        %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(ci2.getStDescription2())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=amt%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <%
                        }
                        %>
                        <fo:table-row>		
                            <fo:table-cell number-columns-spanned="4" >
                                <fo:block border-width="0.3pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="1pt"></fo:block>
                            </fo:table-cell>
                            <fo:table-cell ></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>Jumlah</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(amt3,2)%></fo:block></fo:table-cell>
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
                                <fo:block text-align="center" space-before.optimum="10pt">JAKARTA, <%=DateUtil.getDateStr(new Date(),"d ^^ yyyy")%></fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                
                            </fo:table-cell>
                        </fo:table-row>
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <%
                }
                
            }
            
            %>
            
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>



