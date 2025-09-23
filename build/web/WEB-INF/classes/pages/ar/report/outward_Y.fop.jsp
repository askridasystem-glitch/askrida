<%@ page import="com.webfin.ar.model.*,  
com.webfin.ar.forms.FRRPTrptArAPDetailForm,
com.crux.ff.model.FlexFieldHeaderView, 
com.crux.ff.model.FlexFieldDetailView, 
com.webfin.insurance.model.InsurancePolicyInwardView,
com.crux.util.*, 
com.crux.util.Tools, 
com.crux.util.fop.FOPUtil, 
java.math.BigDecimal, 
com.crux.web.form.FormManager, 
com.crux.web.controller.SessionManager,
java.util.Date"%><?xml version="1.0" encoding="utf-8"?> 
<% 

final DTOList l = (DTOList)request.getAttribute("RPT");

final FRRPTrptArAPDetailForm form = (FRRPTrptArAPDetailForm)SessionManager.getInstance().getCurrentForm();

BigDecimal IDRPremi = null;

BigDecimal nonIDRPremi = null;

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
    
    <!-- defines page layout --> 
    <fo:layout-master-set> 
        
        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="first" 
                               page-height="21.5cm"
                               page-width="35cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            
            <fo:region-body margin-top="0cm" margin-bottom="0cm"/> 
            <fo:region-before extent="0cm"/> 
            <fo:region-after extent="0.5cm"/> 
        </fo:simple-page-master> 
        
    </fo:layout-master-set> 
    <!-- end: defines page layout --> 
 
    <!-- actual layout --> 
    <fo:page-sequence master-reference="first"> 
        
        <!-- usage of page layout --> 
        <fo:flow flow-name="xsl-region-body"> 
            
            <fo:block font-size="8pt"> 
                <fo:table table-layout="fixed">  
                    <fo:table-column column-width="10mm"/><!-- No -->
                    <fo:table-column column-width="20mm"/><!-- Entry Date -->
                    <fo:table-column column-width="20mm"/><!-- Entry Date -->
                    <fo:table-column column-width="20mm"/><!-- Entry Date -->
                    <fo:table-column column-width="60mm"/><!-- Policy No -->
                    <fo:table-column column-width="10mm"/><!-- Policy Date--> 
                    <fo:table-column column-width="40mm"/><!-- Policy Date--> 
                    <fo:table-column column-width="10mm"/><!-- The Insured -->
                    <fo:table-column column-width="30mm"/><!-- Biaya Polis --> 
                    <fo:table-column column-width="15mm"/><!-- Broker Fee -->
                    <fo:table-column /><!-- Broker Fee -->
                    <fo:table-header>          
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block font-weight="bold">             
                                    LAPORAN PER REASURADUR PREMI REASURANSI OUTWARD XOL
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block  font-weight="bold">
                                    <% if (form.getPolicyDateFrom()!=null || form.getPolicyDateTo()!=null) {%> 
                                    BULAN : <%=DateUtil.getDateStr(form.getPolicyDateFrom(),"^^ yyyy")%> s/d <%=DateUtil.getDateStr(form.getPolicyDateTo(),"^^ yyyy")%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block  font-weight="bold">
                                    <% if (form.getPolicyDateFrom()!=null || form.getPolicyDateTo()!=null) {%> 
                                    TANGGAL : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block  font-weight="bold">
                                    <% if (form.getStPolicyTypeName()!=null) {%> 
                                    JENIS PENUTUPAN : <%=JSPUtil.printX(form.getStPolicyTypeName().toUpperCase())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block border-width="0.25pt" border-style="solid" line-height="0.25pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">{L-ENG Bussiness -L}{L-INA Bisnis -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">{L-ENG Due Date -L}{L-INA Due Date -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">{L-ENG Mutation Date -L}{L-INA Mutation Date -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">{L-ENG Invoice -L}{L-INA Uraian -L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">{L-ENG Kd. -L}{L-INA Kd. -L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">{L-ENG Company -L}{L-INA Perusahaan -L}</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold"><fo:inline color="grey">.</fo:inline></fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">{L-ENG Premium -L}{L-INA Premium -L}</fo:block></fo:table-cell>   
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">{L-ENG Kurs -L}{L-INA Kurs -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">{L-ENG Description -L}{L-INA Keterangan -L}</fo:block></fo:table-cell>
                        </fo:table-row>    
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row> 
                        
                    </fo:table-header>   
                    
                    <fo:table-body>   
                        
                        <% 
                        
                        int pn = 0;
                        int norut = 0;
                        
                        BigDecimal jumlahIDRPremi = null;
                        
                        BigDecimal jumlahnonIDRPremi = null;
                        
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyInwardView invoic = (InsurancePolicyInwardView) l.get(i);
                            
                            norut++;
                            
                            if (invoic.getStCurrencyCode().equalsIgnoreCase("IDR")){
                                IDRPremi = BDUtil.add(IDRPremi, invoic.getDbPremiEntered());
                            } else {
                                nonIDRPremi = BDUtil.add(nonIDRPremi, invoic.getDbPremiEntered());
                            }
                            
                            if(i>0){
                                InsurancePolicyInwardView inv2 = (InsurancePolicyInwardView) l.get(i-1);
                                String inward = invoic.getStEntityID();
                                String inward2 = inv2.getStEntityID();
                                if(!inward.equalsIgnoreCase(inward2)){
                                    pn++;
                                    
                                    norut = 1;
                        
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7"><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">IDR</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(jumlahIDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7"><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold"><fo:inline color="grey">.</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">non IDR</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(jumlahnonIDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <%--
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
                       --%>
                        <%
                        jumlahIDRPremi = null;
                        
                        jumlahnonIDRPremi = null;
                                }
                            }
                        %>
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(String.valueOf(norut))%></fo:block></fo:table-cell>    
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStAttrPolicyTypeID())%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getDtDueDate())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getDtMutationDate())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStInvoiceNo())%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStARCustomerID())%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStCreateWho())%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStCurrencyCode())%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(invoic.getDbPremiEntered(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getDbCurrencyRate(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStDescription())%></fo:block></fo:table-cell>
                        </fo:table-row>			
                        
                        <% 
                        
                        if (invoic.getStCurrencyCode().equalsIgnoreCase("IDR")){
                                jumlahIDRPremi = BDUtil.add(jumlahIDRPremi, invoic.getDbPremiEntered());
                        }else {
                                jumlahnonIDRPremi = BDUtil.add(jumlahnonIDRPremi, invoic.getDbPremiEntered());
                        }
                            
                        }
                        %>   
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7"><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">IDR</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(jumlahIDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold"></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7"><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold"><fo:inline color="grey">.</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">non IDR</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(jumlahnonIDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold"></fo:block></fo:table-cell>
                        </fo:table-row>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7"><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">IDR</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(IDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold"></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="7"><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold"><fo:inline color="grey">.</fo:inline></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold">non IDR</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block background-color="#C0C0C0" text-align="end" font-size="10pt" font-weight="bold"><%=JSPUtil.printX(nonIDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="2"><fo:block background-color="#C0C0C0" text-align="center" font-size="10pt" font-weight="bold"></fo:block></fo:table-cell>
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="11">
                                <fo:block border-width="0.25pt" border-style="solid" line-height="0.25pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
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
                        message="<%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(IDRPremi,0)%>" orientation="0">
                        <barcode:datamatrix> 
                            <barcode:height>40pt</barcode:height>
                            <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>22x22</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                        </barcode:datamatrix>
                    </barcode:barcode>
                </fo:instream-foreign-object>                                    
            </fo:block>
            
        </fo:flow> 
    </fo:page-sequence>   
</fo:root>   