<%@ page import="com.webfin.ar.model.*,  
com.webfin.ar.forms.FRRPTrptArAPDetailForm,
com.crux.ff.model.FlexFieldHeaderView, 
com.webfin.insurance.model.InsurancePolicyInwardView,
com.crux.ff.model.FlexFieldDetailView, 
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
                               page-height="21cm"
                               page-width="29.5cm"
                               margin-top="1cm"
                               margin-bottom="1cm"
                               margin-left="2.5cm"
                               margin-right="2.5cm">
            
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
                    <fo:table-column column-width="10mm"/><!-- Entry Date -->
                    <fo:table-column column-width="15mm"/><!-- Policy No -->
                    <fo:table-column column-width="50mm"/><!-- Premium --> 
                    <fo:table-column column-width="10mm"/><!-- Biaya Polis --> 
                    <fo:table-column column-width="30mm"/><!-- Biaya Polis --> 
                    <fo:table-column column-width="15mm"/><!-- Biaya Polis -->
                    <fo:table-column column-width="25mm"/><!-- Biaya Materai -->
                    <fo:table-column column-width="15mm"/><!-- Komisi-->
                    <fo:table-column column-width="15mm"/><!-- Komisi-->
                    <fo:table-column column-width="5mm"/><!-- Komisi-->
                    <fo:table-column column-width="50mm"/><!-- Pajak Komisi/Broker Fee -->
                    <fo:table-header>          
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block font-weight="bold">             
                                    REKAPITULASI PREMI REASURANSI OUTWARD EXCESS OF LOSS PER JENIS
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block  font-weight="bold">
                                    <% if (form.getPolicyDateFrom()!=null || form.getPolicyDateTo()!=null) {%> 
                                    BULAN : <%=DateUtil.getDateStr(form.getPolicyDateFrom(),"^^ yyyy")%> s/d <%=DateUtil.getDateStr(form.getPolicyDateTo(),"^^ yyyy")%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block  font-weight="bold">
                                    <% if (form.getPolicyDateFrom()!=null || form.getPolicyDateTo()!=null) {%> 
                                    TANGGAL : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12">
                                <fo:block  font-weight="bold">
                                    <% if (form.getStPolicyTypeName()!=null) {%> 
                                    JENIS PENUTUPAN : <%=JSPUtil.printX(form.getStPolicyTypeName().toUpperCase())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="12" >  
                                <fo:block border-width="0.25pt" border-style="solid" line-height="0.25pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Bussiness -L}{L-INA Bisnis -L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Date -L}{L-INA Tanggal -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Invoice -L}{L-INA Uraian -L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Kd. -L}{L-INA Kd. -L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Company -L}{L-INA Perusahaan -L}</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Premium -L}{L-INA Premi -L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Share -L}{L-INA Share -L}</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Kurs -L}{L-INA Kurs -L}</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Description -L}{L-INA Keterangan -L}</fo:block></fo:table-cell>
                        </fo:table-row>    
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="12" >  
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
                            }else {
                                nonIDRPremi = BDUtil.add(nonIDRPremi, invoic.getDbPremiEntered());
                            }
                            
                            if(i>0){
                                InsurancePolicyInwardView inv2 = (InsurancePolicyInwardView) l.get(i-1);
                                String inward = invoic.getStAttrPolicyTypeID();
                                String inward2 = inv2.getStAttrPolicyTypeID();
                                if(!inward.equalsIgnoreCase(inward2)){
                                    pn++;
                                    
                                    norut = 1;
                        
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6"><fo:block text-align="center">SUBTOTAL PREMI EXCESS OF LOSS : </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahIDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="4"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6"><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">non IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahnonIDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="4"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12" >
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
                            <fo:table-cell ><fo:block text-align="center"><%=String.valueOf(norut)%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStAttrPolicyTypeID())%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getDtInvoiceDate())%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStInvoiceNo())%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStARCustomerID())%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStCreateWho())%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStCurrencyCode())%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(invoic.getDbPremiEntered(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%= JSPUtil.printX(new BigDecimal(0),2) %> %</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(invoic.getDbCurrencyRate(),2)%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="start"><%=JSPUtil.printX(invoic.getStDescription())%></fo:block></fo:table-cell> 
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
                            <fo:table-cell number-columns-spanned="12" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6"><fo:block text-align="center">SUBTOTAL PREMI EXCESS OF LOSS : </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahIDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="4"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6"><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">non IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahnonIDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="4"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="12" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="12">  
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6"><fo:block text-align="center">TOTAL PREMI EXCESS OF LOSS : </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(IDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="4"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6"><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">non IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(nonIDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="4"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="12">  
                                <fo:block border-width="0.25pt" border-style="solid" line-height="0.25pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                    </fo:table-body>
                </fo:table>   
            </fo:block> 
            
            <fo:block font-size="6pt">
                {L-ENG Print Date-L}{L-INA Tanggal Cetak-L} : <%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>  
            </fo:block> 
            
            <fo:block font-size="8pt">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="100mm"/>
                    <fo:table-column column-width="50mm"/>
                    <fo:table-column column-width="100mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell >
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
                            </fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Jakarta, <%=DateUtil.getDateStr(new Date(),"d MMM yyyy")%></fo:block>
                                <fo:block text-align="center">S. E. &#x26; O</fo:block>
                                <fo:block text-align="center">Reinsurance Dept.,</fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block> 
            
        </fo:flow> 
    </fo:page-sequence>   
</fo:root>   