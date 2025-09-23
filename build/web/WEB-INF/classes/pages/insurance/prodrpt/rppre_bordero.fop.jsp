<%@ page import="com.webfin.insurance.model.*,
com.crux.util.*, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager, 
com.webfin.insurance.form.ProductionReinsuranceReportForm, 
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 
<% 

final DTOList l = (DTOList)request.getAttribute("RPT");

final ProductionReinsuranceReportForm form = (ProductionReinsuranceReportForm)SessionManager.getInstance().getCurrentForm();

BigDecimal SPL = new BigDecimal(0);
BigDecimal BPDAN = new BigDecimal(0);
BigDecimal FAC = new BigDecimal(0);
BigDecimal FACO = new BigDecimal(0);
BigDecimal QS = new BigDecimal(0);
BigDecimal PARK = new BigDecimal(0);
%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-height="21cm"
                               page-width="33cm"
                               margin-top="1cm"
                               margin-bottom="0.5cm"
                               margin-left="1cm"
                               margin-right="1cm">
            <fo:region-body margin-top="3cm" margin-bottom="1.5cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="0.5cm"/>
        </fo:simple-page-master>
        
    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">
        
        <!-- usage of page layout -->
        <!-- header -->
        <fo:static-content flow-name="xsl-region-before">
            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="95mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3">
                                <fo:block font-size="12pt" text-decoration="underline">
                                    BORDERO PREMI REASURANSI Bulan <%=DateUtil.getDateStr(form.getPolicyDateFrom(),"^^ yyyy")%>
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell><fo:block text-decoration="underline">Kepada</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>
                                    <% if(form.getStEntityName()!=null){ %>
                                    <%=JSPUtil.printX(form.getStEntityName())%>
                                    <% } %>
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3">
                                <fo:block font-size="12pt" space-before.optimum="2pt">
                                    Reinsurance Premium Bordereau for the month
                                </fo:block>
                            </fo:table-cell>
                            <fo:table-cell><fo:block space-before.optimum="1pt" space-after.optimum="3pt">To Messrs</fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                        </fo:table-row>      
                        
                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block text-decoration="underline">Tahun Underwriting</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>
                                    <% if(form.getPeriodFrom()!=null){ %>
                                    <%=DateUtil.getYear(form.getPeriodFrom())%>
                            <% } %></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block space-before.optimum="1pt" space-after.optimum="3pt">Underwriting Year</fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block text-decoration="underline">Jenis Pertanggungan</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><%=JSPUtil.printX(form.getStPolicyTypeGroupDesc())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell><fo:block text-decoration="underline">NOMOR BORDERO</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block><% if(form.getStPolicyTypeID()!=null) {%>
                                    <%= JSPUtil.printX(form.getStPolicyTypeGroupID())%>
                                    <% }else{ %>
                            <%= JSPUtil.printX(form.getStPolicyTypeGroupID())%><% } %>04R<%=DateUtil.getMonth2Digit(form.getPolicyDateFrom())%><%=DateUtil.getYear2Digit(form.getPolicyDateFrom())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block space-before.optimum="1pt" space-after.optimum="3pt">Class of Business</fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell><fo:block>PT. Asuransi Bangun Askrida</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block space-before.optimum="1pt">Bordereau Nr.</fo:block></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                            <fo:table-cell></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
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
                      font-size="6pt"
                      font-family="serif"
                      line-height="12pt" 
                      font-style="bold">
                rppre_bordero - PT. Asuransi Bangun Askrida
            </fo:block>
            <fo:block text-align="end"
                      font-size="6pt" font-family="serif" line-height="1em + 2pt">
                {L-ENG Page-L}{L-INA Halaman-L} <fo:page-number/> {L-ENG of-L}{L-INA dari-L} <fo:page-number-citation
                    ref-id="end-of-document"/>
            </fo:block>
        </fo:static-content>
        
        <fo:flow flow-name="xsl-region-body">
            
            <!-- Normal text -->
      
            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="65mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="30mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-header>
                        
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="12"><fo:block text-align="end" font-weight="bold">{L-ENG (In IDR)-L}{L-INA (dalam rupiah)-L}</fo:block></fo:table-cell>  
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="12" >  
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG No. -L}{L-INA No. -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Policy No. -L}{L-INA No. Polis -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Customer -L}{L-INA Nama Tertanggung -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Period Start -L}{L-INA Periode Awal -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Period End-L}{L-INA Periode Akhir -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Sum Insured -L}{L-INA Harga Pertanggungan -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Curr -L}{L-INA Kurs -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Reinsured -L}{L-INA Bag. Reasuransi -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Premium O.R -L}{L-INA Premi Ret. Sendiri -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Treaty Type -L}{L-INA Treaty Type -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" border-right-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Reinsured Premium -L}{L-INA Premi Reasuransi -L}</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" border-right-style="solid"><fo:block text-align="center" line-height="5mm">{L-ENG Note -L}{L-INA Ket. -L}</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12" >
                                <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-header>
                    <fo:table-body>
                        <% 
                        int counter = 40;
                        BigDecimal subTotalTSI = null;
                        BigDecimal subTotalPremiOr = null;
                        BigDecimal subTotalPremiReas = null;
                        
                        String nopol = null;
                        int no = 0;
                        BigDecimal [] t = new BigDecimal[3];
                        for (int i = 0; i < l.size(); i++){
                            InsurancePolicyView pol = (InsurancePolicyView) l.get(i);
                            
                            int n = 0;
                            t[n] = BDUtil.add(t[n++], pol.getDbTsiReas());
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiOR());
                            t[n] = BDUtil.add(t[n++], pol.getDbPremiReas());
                            
                            if(pol.getStTreatyType().equalsIgnoreCase("SPL")){
                                SPL = BDUtil.add(SPL, pol.getDbPremiReas());
                            }
                            
                            if(pol.getStTreatyType().equalsIgnoreCase("BPDAN")){
                                BPDAN = BDUtil.add(BPDAN, pol.getDbPremiReas());
                            }
                            
                            if(pol.getStTreatyType().equalsIgnoreCase("FAC")){
                                FAC = BDUtil.add(FAC, pol.getDbPremiReas());
                            }
                            
                            if(pol.getStTreatyType().equalsIgnoreCase("FACO")){
                                FACO = BDUtil.add(FACO, pol.getDbPremiReas());
                            }
                            
                            if(pol.getStTreatyType().equalsIgnoreCase("QS")){
                                QS = BDUtil.add(QS, pol.getDbPremiReas());
                            }
                            
                            if(pol.getStTreatyType().equalsIgnoreCase("PARK")){
                                PARK = BDUtil.add(PARK, pol.getDbPremiReas());
                            }
                            
                            if(!pol.getStPolicyNo().equalsIgnoreCase("")){
                                nopol = pol.getStPolicyNo().substring(0,4)+"."+pol.getStPolicyNo().substring(4,8)+"."+pol.getStPolicyNo().substring(8,12)+"."+pol.getStPolicyNo().substring(12,16)+"."+pol.getStPolicyNo().substring(16,18);
                            }else{
                                nopol = "";
                            }
                            
                            if(!pol.getStPolicyNo().equalsIgnoreCase("")){
                                no = no+1;
                            }
                            
                            if (i==counter) {
                                counter = counter + 40;
                        %>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row space-before.optimum="5pt">
                            <fo:table-cell number-columns-spanned="7" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalTSI,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiOr,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="end" line-height="5mm"></fo:block></fo:table-cell>
                            <fo:table-cell border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(subTotalPremiReas,0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="end" line-height="5mm"></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                        <%
                        subTotalTSI = null;
                        subTotalPremiOr = null;
                        subTotalPremiReas = null;
                            }%>
                        
                        <fo:table-row>
                            <%if(!pol.getStPolicyNo().equalsIgnoreCase("")){%>
                            <fo:table-cell><fo:block text-align="center"><%=no%></fo:block></fo:table-cell>
                            <% }else{ %>
                            <fo:table-cell></fo:table-cell>
                            <% 	} %>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(nopol)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block ><%=JSPUtil.printX(pol.getStCustomerName())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtPeriodStart())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(pol.getDtPeriodEnd())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbTsiReas(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiOR(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(pol.getStTreatyType())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiReas(),0)%> </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="center"><%=JSPUtil.printX(pol.getStStatus())%></fo:block></fo:table-cell>
                        </fo:table-row>	
                        
                        <% 		subTotalTSI = BDUtil.add(subTotalTSI,pol.getDbTsiReas());
                        subTotalPremiOr = BDUtil.add(subTotalPremiOr,pol.getDbPremiOR());
                        subTotalPremiReas = BDUtil.add(subTotalPremiReas,pol.getDbPremiReas());
                        } %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        
                        <fo:table-row>
                            <fo:table-cell border-left-style="solid" number-columns-spanned="7"><fo:block text-align="center" line-height="5mm">T O T A L</fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[0],0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[1],0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="end" line-height="5mm"></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid" border-right-style="solid"><fo:block text-align="end" line-height="5mm"><%=JSPUtil.printX(t[2],0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-left-style="solid"><fo:block text-align="end" line-height="5mm"></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <% if(Tools.compare(BPDAN,BDUtil.zero)!=0){ %>                                   
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8"></fo:table-cell>
                            <fo:table-cell ><fo:block >BPPDAN</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >: IDR</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%= JSPUtil.printX(BPDAN,0) %></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% } %>
                        
                        <% if(Tools.compare(PARK,BDUtil.zero)!=0){ %>      
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8"></fo:table-cell>
                            <fo:table-cell ><fo:block >MAIPARK</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >: IDR</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%= JSPUtil.printX(PARK,0) %></fo:block></fo:table-cell>
                        </fo:table-row>  
                        <% } %>
                        
                        <% if(Tools.compare(FAC,BDUtil.zero)!=0){ %> 
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8"></fo:table-cell>
                            <fo:table-cell ><fo:block >FACULTATIF</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >: IDR</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%= JSPUtil.printX(FAC,0) %></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% } %>
                        
                        <% if(Tools.compare(FACO,BDUtil.zero)!=0){ %>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8"></fo:table-cell>
                            <fo:table-cell ><fo:block >FACOBLIG</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >: IDR</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%= JSPUtil.printX(FACO,0) %></fo:block></fo:table-cell>
                        </fo:table-row>   
                        <% } %>
                        
                        <% if(Tools.compare(SPL,BDUtil.zero)!=0){ %>    
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8"></fo:table-cell>
                            <fo:table-cell ><fo:block >SURPLUS</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >: IDR</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%= JSPUtil.printX(SPL,0) %></fo:block></fo:table-cell>
                        </fo:table-row> 
                        <% } %>
                        
                        <% if(Tools.compare(QS,BDUtil.zero)!=0){ %>
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8"></fo:table-cell>
                            <fo:table-cell ><fo:block >QUOTA SHARE</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block >: IDR</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%= JSPUtil.printX(QS,0) %></fo:block></fo:table-cell>
                        </fo:table-row>  
                        <% } %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12" >
                                <fo:block space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9"></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block text-align="center">JAKARTA, <%=DateUtil.getDateStr(new Date(),"d MMM yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9"></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block text-align="center">BAGIAN REASURANSI</fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9"></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block text-align="center">S. E. &#x26; O.</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>  
            
            <fo:block font-size="8pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
            </fo:block>    
            
            <fo:block text-align="start">
                <fo:instream-foreign-object>
                    <barcode:barcode
                        xmlns:barcode="http://barcode4j.krysalis.org/ns"
                        message="<%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(t[0],0)%>" orientation="0">
                        <barcode:datamatrix> 
                            <barcode:height>40pt</barcode:height>
                            <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                        </barcode:datamatrix>
                    </barcode:barcode>
                </fo:instream-foreign-object>                                    
            </fo:block>
            
            <fo:block id="end-of-document"><fo:marker
                    marker-class-name="term">
                    <fo:instream-foreign-object>
                        <svg xmlns="http://www.w3.org/2000/svg" width="15cm" height="1cm" xml:space="preserve">     
                            <rect style="fill:white;stroke:white" x="0" y="0" width="15cm" height="1cm"/>
                        </svg>
                    </fo:instream-foreign-object>
                </fo:marker>
            </fo:block>
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>
