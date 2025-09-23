<%@ page import="com.webfin.ar.model.*,  
com.webfin.ar.forms.FRRPTrptArAPDetailForm,
com.webfin.insurance.model.InsurancePolicyInwardView,
com.crux.ff.model.FlexFieldHeaderView, 
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

BigDecimal IDRTSI = null;
BigDecimal IDRPremi = null;
BigDecimal IDRComm = null;
BigDecimal IDRFee = null;
BigDecimal IDRNetto = null;

BigDecimal nonIDRTSI = null;
BigDecimal nonIDRPremi = null;
BigDecimal nonIDRComm = null;
BigDecimal nonIDRFee = null;
BigDecimal nonIDRNetto = null;

%> 
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format"> 
    
    <!-- defines page layout --> 
    <fo:layout-master-set> 
        
        <!-- layout for the first page --> 
        <fo:simple-page-master master-name="first" 
                               page-height="21.5cm"
                               page-width="35cm"
                               margin-top="1.5cm"
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
                    <fo:table-column column-width="25mm"/><!-- Entry Date -->
                    <fo:table-column column-width="10mm"/><!-- Policy Date--> 
                    <fo:table-column column-width="10mm"/><!-- Policy No -->
                    <fo:table-column column-width="40mm"/><!-- The Insured -->
                    <fo:table-column column-width="40mm"/><!-- Premium --> 
                    <fo:table-column column-width="10mm"/><!-- Biaya Polis --> 
                    <fo:table-column column-width="30mm"/><!-- Biaya Polis --> 
                    <fo:table-column column-width="20mm"/><!-- Biaya Polis -->
                    <fo:table-column column-width="25mm"/><!-- Biaya Materai -->
                    <fo:table-column column-width="25mm"/><!-- Discount-->
                    <fo:table-column column-width="25mm"/><!-- Komisi-->
                    <fo:table-column column-width="25mm"/><!-- Handling Fee --> 
                    <fo:table-column column-width="10mm"/><!-- Handling Fee --> 
                    <fo:table-column column-width="15mm"/><!-- Broker Fee -->
                    <fo:table-column /><!-- Pajak Komisi/Broker Fee -->
                    <fo:table-header>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block font-weight="bold">             
                                    DAFTAR PRODUKSI SLIP INWARD FACULTATIF
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block  font-weight="bold">
                                    <% if (form.getPolicyDateFrom()!=null || form.getPolicyDateTo()!=null) {%> 
                                    Tgl Mutasi Slip : <%=JSPUtil.printX(form.getPolicyDateFrom())%> S/D <%=JSPUtil.printX(form.getPolicyDateTo())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16">
                                <fo:block  font-weight="bold">
                                    <% if (form.getStPolicyTypeName()!=null) {%> 
                                    JENIS PENUTUPAN : <%=JSPUtil.printX(form.getStPolicyTypeName().toUpperCase())%>
                                    <% } %>
                                </fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="16" >  
                                <fo:block border-width="0.25pt" border-style="solid" line-height="0.25pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center">{L-ENG No.-L}{L-INA No.-L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Reinsured -L}{L-INA Reasuradur -L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Kd. -L}{L-INA Kd. -L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Bussiness -L}{L-INA Bisnis -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Slip No. -L}{L-INA No. Slip-L}</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Insured Date -L}{L-INA Tgl Pertanggungan -L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Insured Amount -L}{L-INA Jmlh Pertanggungan -L}</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Our Share -L}{L-INA Our Share -L}</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Premium -L}{L-INA Premium -L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Commission -L}{L-INA Komisi -L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Netto -L}{L-INA Netto -L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"></fo:block></fo:table-cell>    
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Kurs -L}{L-INA Kurs -L}</fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="center">{L-ENG Description -L}{L-INA Keterangan -L}</fo:block></fo:table-cell>
                        </fo:table-row>    
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="16" >  
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>   
                        
                    </fo:table-header>   
                    
                    <fo:table-body>   
                        
                        <%  
                        
                        int pn = 0;
                        int norut = 0;
                        
                        BigDecimal jumlahIDRTSI = null;
                        BigDecimal jumlahIDRPremi = null;
                        BigDecimal jumlahIDRComm = null;
                        BigDecimal jumlahIDRFee = null;
                        BigDecimal jumlahIDRNetto = null;
                        
                        BigDecimal jumlahnonIDRTSI = null;
                        BigDecimal jumlahnonIDRPremi = null;
                        BigDecimal jumlahnonIDRComm = null;
                        BigDecimal jumlahnonIDRFee = null;
                        BigDecimal jumlahnonIDRNetto = null;
                        
                        for (int i = 0; i < l.size(); i++) {
                            InsurancePolicyInwardView invoic = (InsurancePolicyInwardView) l.get(i);
                            
                            norut++;
                            
                            if (invoic.getStCurrencyCode().equalsIgnoreCase("IDR")){
                                IDRTSI = BDUtil.add(IDRTSI, invoic.getDbAttrPolicyTSI());
                                IDRPremi = BDUtil.add(IDRPremi, invoic.getDbPremiEntered());
                                IDRComm = BDUtil.add(IDRComm, invoic.getDbCommEntered());
                                IDRFee = BDUtil.sub(IDRFee, invoic.getDbFeeEntered());
                                IDRNetto = BDUtil.sub(IDRPremi, BDUtil.add(IDRComm, IDRFee));
                            }else {
                                nonIDRTSI = BDUtil.add(nonIDRTSI, invoic.getDbAttrPolicyTSI());
                                nonIDRPremi = BDUtil.add(nonIDRPremi, invoic.getDbPremiEntered());
                                nonIDRComm = BDUtil.add(nonIDRComm, invoic.getDbCommEntered());
                                nonIDRFee = BDUtil.add(nonIDRFee, invoic.getDbFeeEntered());
                                nonIDRNetto = BDUtil.sub(nonIDRPremi, BDUtil.add(nonIDRComm, nonIDRFee));
                            }
                            
                            if(i>0){
                                InsurancePolicyInwardView inv2 = (InsurancePolicyInwardView) l.get(i-1);
                                String inward = invoic.getStARCustomerID();
                                String inward2 = inv2.getStARCustomerID();
                                if(!inward.equalsIgnoreCase(inward2)){
                                    pn++;
                                    
                                    norut = 1;
                        
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8"><fo:block text-align="center">SUBTOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahIDRTSI,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahIDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahIDRComm,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahIDRNetto,2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8"><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">non IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahnonIDRTSI,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahnonIDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahnonIDRComm,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahnonIDRNetto,2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        <%--                        
                        <fo:table-row break-after="page">
                            <fo:table-cell />
                        </fo:table-row>
--%>
                        <%
                        jumlahIDRTSI = null;
                        jumlahIDRPremi = null;
                        jumlahIDRComm = null;
                        jumlahIDRFee = null;
                        jumlahIDRNetto = null;
                        
                        jumlahnonIDRTSI = null;
                        jumlahnonIDRPremi = null;
                        jumlahnonIDRComm = null;
                        jumlahnonIDRFee = null;
                        jumlahnonIDRNetto = null;
                                }
                            }
                        %>
                        
                        <fo:table-row>   
                            <fo:table-cell ><fo:block text-align="center"><%=String.valueOf(norut)%></fo:block></fo:table-cell>    <!-- No --><!-- No --> 
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(invoic.getStCreateWho())%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStARCustomerID())%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStAttrPolicyTypeID())%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(invoic.getStTransactionNoReference())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getDtAttrPolicyPeriodStart())%> - <%=JSPUtil.printX(invoic.getDtAttrPolicyPeriodEnd())%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStCurrencyCode())%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(invoic.getDbAttrPolicyTSITotal(),2)%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="center"><%=JSPUtil.printX(invoic.getStCurrencyCode())%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(invoic.getDbAttrPolicyTSI(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(invoic.getDbPremiEntered(),2)%></fo:block></fo:table-cell>  
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(invoic.getDbCommEntered(),2)%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(BDUtil.sub(invoic.getDbPremiEntered(), BDUtil.add(invoic.getDbCommEntered(), invoic.getDbFeeEntered())),2)%></fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%= JSPUtil.printX(new BigDecimal(0),2) %> %</fo:block></fo:table-cell> 
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(invoic.getDbCurrencyRate(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=JSPUtil.printX(invoic.getStDescription())%></fo:block></fo:table-cell> 
                        </fo:table-row>			
                        <% 
                        
                        if (invoic.getStCurrencyCode().equalsIgnoreCase("IDR")){
                                jumlahIDRTSI = BDUtil.add(jumlahIDRTSI, invoic.getDbAttrPolicyTSI());
                                jumlahIDRPremi = BDUtil.add(jumlahIDRPremi, invoic.getDbPremiEntered());
                                jumlahIDRComm = BDUtil.add(jumlahIDRComm, invoic.getDbCommEntered());
                                jumlahIDRFee = BDUtil.sub(jumlahIDRFee, invoic.getDbFeeEntered());
                                jumlahIDRNetto = BDUtil.sub(jumlahIDRPremi, BDUtil.add(jumlahIDRComm, jumlahIDRFee));
                        }else {
                                jumlahnonIDRTSI = BDUtil.add(jumlahnonIDRTSI, invoic.getDbAttrPolicyTSI());
                                jumlahnonIDRPremi = BDUtil.add(jumlahnonIDRPremi, invoic.getDbPremiEntered());
                                jumlahnonIDRComm = BDUtil.add(jumlahnonIDRComm, invoic.getDbCommEntered());
                                jumlahnonIDRFee = BDUtil.add(jumlahnonIDRFee, invoic.getDbFeeEntered());
                                jumlahnonIDRNetto = BDUtil.sub(jumlahnonIDRPremi, BDUtil.add(jumlahnonIDRComm, jumlahnonIDRFee));
                        }
                            
                        }
                        %>   
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8"><fo:block text-align="center">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahIDRTSI,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahIDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahIDRComm,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahIDRNetto,2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8"><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">non IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahnonIDRTSI,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahnonIDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahnonIDRComm,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(jumlahnonIDRNetto,2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="16" >
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="10pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="16">  
                                <fo:block border-width="0.15pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
                            </fo:table-cell>   
                        </fo:table-row>  
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8"><fo:block text-align="center">TOTAL</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(IDRTSI,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(IDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(IDRComm,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(IDRNetto,2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="8"><fo:block text-align="center"></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center">non IDR</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(nonIDRTSI,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(nonIDRPremi,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(nonIDRComm,2)%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(nonIDRNetto,2)%></fo:block></fo:table-cell>
                            <fo:table-cell number-columns-spanned="3"><fo:block></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>   
                            <fo:table-cell number-columns-spanned="16">  
                                <fo:block border-width="0.25pt" border-style="solid" line-height="0.15pt" space-before.optimum="3pt" space-after.optimum="3pt"></fo:block>   
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