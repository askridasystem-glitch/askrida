<%@ page import="com.webfin.insurance.model.*,
com.crux.ff.model.FlexFieldHeaderView,
com.crux.ff.model.FlexFieldDetailView,
com.crux.util.*,
com.crux.util.fop.FOPUtil,
java.math.BigDecimal,
com.crux.common.parameter.Parameter,
java.util.Date"%><?xml version="1.0" encoding="utf-8"?>
<%

final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
final String fontsize = (String) request.getAttribute("FONTSIZE");
final String attached = (String) request.getAttribute("attached");

String param[] = attached.split("[\\|]");

boolean isAttached = param[0].equalsIgnoreCase("2")?true:false;
boolean tanpaTanggal = param[0].equalsIgnoreCase("3")?true:false;
boolean tanpaNama = param[0].equalsIgnoreCase("5")?true:false;
boolean tanpaNamaTanggal = param[0].equalsIgnoreCase("6")?true:false;
boolean tanpaRate = param[0].equalsIgnoreCase("7")?true:false;
boolean klausulaTerlampir = param[0].equalsIgnoreCase("8")?true:false;

if(attached.length()>1){
    if(param[1]!=null){
        isAttached = !isAttached?param[1].equalsIgnoreCase("2")?true:false:isAttached;
        tanpaTanggal = !tanpaTanggal?param[1].equalsIgnoreCase("3")?true:false:tanpaTanggal;
        tanpaRate = !tanpaRate?param[1].equalsIgnoreCase("7")?true:false:tanpaRate;
    }
}

DTOList objects2 = pol.getObjects();

//InsurancePolicyObjDefaultView obj2 = (InsurancePolicyObjDefaultView) objects2.get(0);

String no_polis = pol.getStPolicyNo();

String no_polis_cetak = no_polis.substring(0,4)+"-"+no_polis.substring(4,8)+"-"+no_polis.substring(8,12)+"-"+no_polis.substring(12,16)+"-"+no_polis.substring(16,18);

boolean effective = pol.isEffective();

final String digitalsign = (String) request.getAttribute("digitalsign");
boolean usingDigitalSign = false;
if (digitalsign!=null)
    if (digitalsign.equalsIgnoreCase("Y")) usingDigitalSign = true;

boolean isUsingBarcode = Parameter.readString("REPORT_SIGN_TYPE").equalsIgnoreCase("BARCODE");

%>
<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines page layout -->
    <fo:layout-master-set>
        
        <!-- layout for the first page -->
        <fo:simple-page-master master-name="only"
                               page-height="30cm"
                               page-width="21.5cm"
                               margin-top="4cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            <fo:region-body margin-top="1cm" margin-bottom="0.5cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="0.7cm"/>
        </fo:simple-page-master>
        
    </fo:layout-master-set>
    <!-- end: defines page layout -->

    <!-- actual layout -->
    <fo:page-sequence master-reference="only" initial-page-number="1">
        
        <!-- usage of page layout -->
        <!-- header -->
        <fo:static-content flow-name="xsl-region-before">
            <fo:block text-align="end"
                      font-size="6pt"
                      font-family="serif"
                      line-height="12pt" >
                PT. Asuransi Bangun Askrida Page:<fo:page-number/>
            </fo:block>
        </fo:static-content>
        
        <fo:static-content flow-name="xsl-region-after">
            <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="0pt" space-after.optimum="5pt"></fo:block>
        </fo:static-content>
        
        <fo:flow flow-name="xsl-region-body">
            
            <% if (!effective) {%>
            <fo:block font-size="16pt"
                      line-height="16pt" space-after.optimum="10pt"
                      color="red"
                      text-align="center"
                      padding-top="10pt">
                SPECIMEN
            </fo:block>
            <% }%>
            <!-- defines text title level 1-->

            <fo:block font-size="16pt" line-height="5pt" space-after.optimum="5pt"
                      color="black"
                      text-align="center"
                      padding-top="10pt">
                {L-ENG LIST OF ATTACHMENT PA KREASI-L}{L-INA LAMPIRAN DAFTAR PESERTA KREASI -L}
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" line-height="5pt" space-after.optimum="5pt"
                      color="black"
                      text-align="left"
                      padding-top="10pt">
            </fo:block>
            
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="10" line-height="0.75pt" space-after.optimum="10pt"></fo:block>
            
            <%
            String bw = "0.5pt";
            %> 
            <!-- Normal text -->

            <!-- defines text title level 1-->
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="4mm"/>
                    <fo:table-column />
                    <fo:table-body>         
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Policy No.-L}{L-INA Nomor Polis-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>  <%= no_polis_cetak%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Policy Date-L}{L-INA Tanggal Polis-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>  <%=JSPUtil.print(pol.getDtPolicyDate())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG On Behalf of-L}{L-INA Atas Nama-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>  <%=JSPUtil.xmlEscape(pol.getStCustomerName())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block>{L-ENG Credit Plafon-L}{L-INA Jenis Kredit-L}</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block>:</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.print(pol.getStKreasiTypeDesc())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <% 
                        InsurancePolicyObjectView obj2 = (InsurancePolicyObjectView) objects2.get(0);
                        
                        DTOList cover3 = obj2.getCoverage();
                        
                        for(int r = 0; r < cover3.size(); r++) {
                            
                            InsurancePolicyCoverView cov = (InsurancePolicyCoverView) cover3.get(r);
                            
                            if(!cov.getStInsuranceCoverID().equalsIgnoreCase("78")){
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block><%=JSPUtil.printX(cov.getStInsuranceCoverDesc2())%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }else{%>       
                        <fo:table-row>
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
                        </fo:table-row>
                        <% }}%>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block> </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>" line-height="5pt" space-after.optimum="5pt"
                      color="black"
                      text-align="left"
                      padding-top="10pt">
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="35mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="40mm"/>
                    <fo:table-column column-width="12mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column column-width="20mm"/>
                    <fo:table-column column-width="25mm"/>
                    <fo:table-column />
                    <fo:table-header>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="5t" space-after.optimum="0pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block padding-top="5pt" text-align="center" line-height="5mm" >{L-ENG No. -L}{L-INA No. -L}</fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block padding-top="5pt" text-align="center" line-height="5mm" >{L-ENG Name of Member -L}{L-INA Nama Peserta -L}</fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >{L-ENG Birth Date -L}{L-INA Tanggal Lahir -L}</fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block padding-top="5pt" text-align="center" line-height="5mm" >{L-ENG Year -L}{L-INA Usia -L}</fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block padding-top="5pt" text-align="center" line-height="5mm" >{L-ENG Time Length-L}{L-INA Jangka Waktu -L}</fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" >{L-ENG Time -L}{L-INA Lama (Bulan) -L}</fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block padding-top="5pt" text-align="center" line-height="5mm" >{L-ENG Sum Insured -L}{L-INA H. Pertanggungan -L}</fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block padding-top="5pt" text-align="center" line-height="5mm" >{L-ENG Premium -L}{L-INA Premi -L}</fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid" border-right-style="solid"><fo:block padding-top="5pt" text-align="center" line-height="5mm" >{L-ENG Coverage -L}{L-INA Jaminan -L}</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-header>   
                    
                    <fo:table-body>   
                        
                        
                        <%
                        DTOList objects = pol.getObjects();
                        
                        for (int i = 0; i < objects.size(); i++) {
                            InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(i);
                        
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" ><%=JSPUtil.printX(String.valueOf(i+1))%></fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block line-height="5mm" ><%=JSPUtil.printX(obj.getStReference1())%></fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" ><%=JSPUtil.printX(obj.getDtReference1())%></fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" ><%=JSPUtil.printX(obj.getStReference2())%></fo:block></fo:table-cell>
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" ><%=JSPUtil.printX(obj.getDtReference2())%> s/d <%=JSPUtil.printX(obj.getDtReference3())%></fo:block></fo:table-cell>           
                            <fo:table-cell  border-width="<%=bw%>" border-left-style="solid"><fo:block text-align="center" line-height="5mm" ><%=JSPUtil.printX(obj.getStReference6())%></fo:block></fo:table-cell>
                            
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid"><fo:block line-height="5mm" text-align="end"><%=JSPUtil.printX(obj.getDbObjectInsuredAmount(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid"><fo:block line-height="5mm" text-align="end"><%=JSPUtil.printX(obj.getDbReference6(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-left-style="solid" border-right-style="solid"><fo:block line-height="5mm" text-align="start">
                                    
                                    <% 
                                    DTOList cover = obj.getCoverage();
                                    
                                    for(int k = 0; k < cover.size(); k++) {
                                        InsurancePolicyCoverView cov = (InsurancePolicyCoverView) cover.get(k);
                                        
                                        if (cov.getStInsuranceCoverID().equalsIgnoreCase("78")) continue;
                                        
                                        if (k>0) out.print("; ");
                                        out.print(JSPUtil.print(cov.getStInsCoverDesc()));
                                    %>
                                    
                                    <% } %> 
                                    
                            </fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <%
                        }
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="6" border-width="0.5pt" border-left-style="solid"><fo:block text-align="center" line-height="5mm">TOTAL SELURUH</fo:block></fo:table-cell>
                            <fo:table-cell  border-width="0.5pt" border-left-style="solid"><fo:block text-align="end"  line-height="5mm"><%=JSPUtil.printX(pol.getDbInsuredAmount(),0)%></fo:block></fo:table-cell>
                            <fo:table-cell  border-width="0.5pt" border-left-style="solid"><fo:block text-align="end"  line-height="5mm"><%=JSPUtil.printX((BigDecimal) objects.getTotal("refn6"),0)%></fo:block></fo:table-cell>
                            <fo:table-cell  border-width="0.5pt" border-left-style="solid" border-right-style="solid"><fo:block text-align="end"  line-height="5mm"></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="9" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="65mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="45mm"/>
                    <fo:table-body>
                        <fo:table-row>
                            <fo:table-cell><fo:block>JUMLAH HARGA PERTANGGUNGAN</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>= <%=JSPUtil.print(pol.getStCurrencyCode())%>  </fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbInsuredAmount(),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        <fo:table-row>
                            <fo:table-cell><fo:block>JUMLAH PREMI</fo:block></fo:table-cell>
                            <fo:table-cell><fo:block>= <%=JSPUtil.print(pol.getStCurrencyCode())%></fo:block></fo:table-cell>
                            <fo:table-cell><fo:block text-align="end"><%=JSPUtil.printX(pol.getDbPremiTotal(),0)%></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <% if (pol.getStCoverTypeCode().equalsIgnoreCase("COINSOUTSELF")) { %>
            
            <fo:block space-before.optimum="5pt" space-after.optimum="5pt" text-align="justify"></fo:block>
            
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed" space-before.optimum="10pt" space-after.optimum="10pt">
                    <fo:table-column column-width="5mm"/>
                    <fo:table-column column-width="60mm"/>
                    <fo:table-column column-width="15mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>	
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center" >No</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center" >Nama</fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="center" >Share</fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="5pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>	   
                        
                        <% 
                        DTOList coins = pol.getCoins2();
                        for (int f = 0; f < coins.size(); f++) {
                            InsurancePolicyCoinsView coi = (InsurancePolicyCoinsView) coins.get(f);
                        %>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block text-align="center" ><%= String.valueOf(f+1) %></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block ><%= coi.getStEntityName() %></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end" ><%= JSPUtil.printX(coi.getDbSharePct(),2)%> %</fo:block></fo:table-cell>
                        </fo:table-row> 
                        <% } %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="3" >
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.75pt"></fo:block>
                            </fo:table-cell>
                        </fo:table-row>	  
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            
            <% } %> 
            
            <% if (usingDigitalSign) { %>
            <fo:block font-size="6pt"
                      space-before.optimum="3pt"
                      space-after.optimum="3pt"
                      text-align="justify" >
                Dokumen ini dicetak secara sistem dan dinyatakan sah.
            </fo:block>
            <% } %>
            
            <fo:block border-width="0.75pt" border-style="solid" border-before-precedence="0" line-height="0.75pt" space-after.optimum="3pt"></fo:block>
            
            
            <fo:block font-size="6pt" space-after.optimum="10pt">
                Print Date : <%=JSPUtil.print(DateUtil.getDateStr(new Date(),"dd/MM/yyyy HH:mm:ss"))%> 
                
                <% if (effective) {%>
                PrintStamp:<%=pol.getStPrintCode()%> PrintCode:<%=pol.getStPrintStamp()%> Page:<fo:page-number/> Cetakan : <%=pol.getStPrintCounter()%>
                <% } %>   
                <% if (usingDigitalSign) { %>
                <%if(!isUsingBarcode){%>  [<%=JSPUtil.print(pol.getStSignCode())%>] <%}%>
                <% } %>   
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="90mm"/>
                    <fo:table-body>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) { %>
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L} 
                                <%= JSPUtil.printX(pol.getStCostCenterDesc())%><%if(!tanpaTanggal) { %>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%><%}%></fo:block>
                                <% } else { %>  
                                <fo:block text-align="center">{L-ENG Signed at-L}{L-INA Dibuat di-L} 
                                <%= JSPUtil.printX(pol.getCostCenter(pol.getCostCenter(pol.getStCostCenterCode()).getStSubCostCenterCode()).getStDescription())%><%if(!tanpaTanggal){%>, {L-ENG On-L}{L-INA pada tanggal-L} <%=DateUtil.getDateStr(pol.getDtPolicyDate(),"d ^^ yyyy")%><%}%></fo:block>
                                <% } %>             
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <%if(!isUsingBarcode){%><fo:block text-align="center">{L-ENG Authorized-L}{L-INA Tanda Tangan Yang Berwenang-L}</fo:block><%}%>
                            </fo:table-cell>
                        </fo:table-row>
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-size="<%=fontsize%>">
                <fo:table table-layout="fixed">
                    <fo:table-column column-width="80mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-column column-width="70mm"/>
                    <fo:table-column column-width="10mm"/>
                    <fo:table-body>
                        
                        <% if (usingDigitalSign) { %>
                        <fo:table-row>
                            <fo:table-cell ><fo:block>
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="KANTOR_ASKRIDA_<%=pol.getCostCenter(pol.getStCostCenterCode()).getTypeDescription().toUpperCase()%>_<%=pol.getCostCenter(pol.getStCostCenterCode()).getStDescription().toUpperCase()%>_<%=pol.getStPolicyNo()%>" orientation="0">
                                            <barcode:datamatrix> 
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>24x24</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                                <%--<human-readable>none</human-readable>--%>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>
                                    
                            </fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <%--<% if (pol.getUserApproved().getFile().getStFilePath()!=null) { %>--%>
                            <fo:table-cell>
                                <%if(!isUsingBarcode){%>
                                <%if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) { %>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(<%=pol.getUser(pol.getUserIDSign()).getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <% } else { %>
                                <fo:block text-align = "center">
                                    <fo:external-graphic
                                        content-height="scale-to-fit"
                                        height="1.00in" content-width="1.00in"
                                        scaling="non-uniform" src="url(<%=pol.getUserApproved().getFile().getStFilePath()%>)"  />
                                </fo:block>
                                <% } %>
                                <% } %>
                                
                                <%if(isUsingBarcode){%>
                                <fo:block text-align = "center" space-before.optimum="5pt">
                                    <fo:instream-foreign-object>
                                        <barcode:barcode
                                            xmlns:barcode="http://barcode4j.krysalis.org/ns"
                                            message="<%=pol.getEncryptedApprovedWho()%>" orientation="0">
                                            <barcode:datamatrix>
                                                <barcode:height>40pt</barcode:height>
                                                <barcode:module-width>3pt</barcode:module-width> <barcode:min-symbol-size>24x24</barcode:min-symbol-size> <barcode:max-symbol-size>24x24</barcode:max-symbol-size>
                                                <%--<human-readable>none</human-readable>--%>
                                            </barcode:datamatrix>
                                        </barcode:barcode>
                                    </fo:instream-foreign-object>
                                    
                                </fo:block>
                                <fo:block font-size="6pt"
                                          font-family="sans-serif"
                                          line-height="10pt"
                                          space-after.optimum="10pt"
                                          text-align="center">
                                    <%=pol.getStSignCode()%>
                                </fo:block>
                                <%}%>
                                
                            </fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <%--
                            <% } else { %>
                            <fo:table-cell>
                                <fo:block text-align = "center" space-after.optimum="65pt"></fo:block>
                            </fo:table-cell>
                            <% } %>
                            --%>
                        </fo:table-row>                         
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) { %>
                            <%--<fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><%=pol.getUserApproved().getStUserName().toUpperCase()%></fo:block></fo:table-cell>--%>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%= pol.getUserIDSignName()%></fo:inline></fo:block></fo:table-cell>
                            <% } else { %>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"><fo:inline text-decoration="underline"><%=JSPUtil.printX(pol.getUserApproved().getStUserName()).toUpperCase()%></fo:inline></fo:block></fo:table-cell>
                            <% } %>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                            <% if (pol.getCostCenter(pol.getStCostCenterCode()).getStType().equalsIgnoreCase("1")) { %>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center" ><%if(pol.getParaf()!=null){%><fo:leader leader-length="10mm"/><%}%><%=JSPUtil.printX(pol.getUser(pol.getUserIDSign()).getStJobPosition()).toUpperCase()%>                                     <%if(pol.getParaf()!=null){%>                                     <fo:external-graphic                                         content-height="scale-to-fit" height="0.3in"  content-width="0.3in" scaling="non-uniform"                                         overflow="hidden" src="url(<%=pol.getUser(pol.getParaf()).getParafFile().getStFilePath()%>)"  />                                     <%}%>                             </fo:block></fo:table-cell> 
                            <% } else { %>
                            <fo:table-cell><fo:block font-size="<%=fontsize%>" text-align = "center"></fo:block></fo:table-cell>
                            <% } %>
                            <fo:table-cell ><fo:block></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <% } %> 
                        
                    </fo:table-body>
                </fo:table> 
            </fo:block>  
            
        </fo:flow>
    </fo:page-sequence>    
</fo:root>
