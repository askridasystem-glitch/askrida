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
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            <fo:region-body margin-top="0cm" margin-bottom="0cm"/>
            <fo:region-before extent="0cm"/>
            <fo:region-after extent="0cm"/>
        </fo:simple-page-master>
    </fo:layout-master-set>
    
    <!-- starts actual layout -->
    <fo:page-sequence master-reference="first">
        <%
        
        ReceiptForm form = (ReceiptForm) request.getAttribute("FORM");
        
        DTOList line = (DTOList) request.getAttribute("RPT");
        
        ArrayList colW = new ArrayList();
        
        colW.add(new Integer(2));
        colW.add(new Integer(7));
        colW.add(new Integer(8));
        colW.add(new Integer(12));
        colW.add(new Integer(7));
        colW.add(new Integer(8));
        
        String pol_type = "";
        
        ARReceiptLinesView lines = (ARReceiptLinesView) line.get(0);
        
        final DTOList InvView = lines.getARInvoice();
        for (int w = 0; w < InvView.size(); w++) {
            ARInvoiceView invoicView = (ARInvoiceView) InvView.get(w);
            
            final DTOList insPol = invoicView.getInsPolicy();
            for (int z = 0; z < insPol.size(); z++) {
                InsurancePolicyView policy = (InsurancePolicyView) insPol.get(z);
                
                pol_type = policy.getStPolicyTypeDesc2();
            }
        }
        %>
        <fo:flow flow-name="xsl-region-body">
            
            <fo:block font-size="10pt"> 
                <fo:table table-layout="fixed"> 
                    <fo:table-column column-width="100mm" />
                    <fo:table-column />
                    <fo:table-body> 
                        
                        <fo:table-row> 
                            <fo:table-cell number-columns-spanned="2" > 
                                <fo:block space-after.optimum="20pt"></fo:block> 
                            </fo:table-cell> 
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block >Nomor : <%= JSPUtil.printX(form.getStReceiptNo()) %></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block text-align="end"><%=DateUtil.getDateStr(new Date(),"d ^^ yyyy")%></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row> 
                            <fo:table-cell number-columns-spanned="2" > 
                                <fo:block space-before.optimum="5pt" space-after.optimum="5pt"></fo:block> 
                            </fo:table-cell> 
                        </fo:table-row>
                        
                        <% if (form.getStEntityName()!=null) { %>     
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block >Kepada Yth.</fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block ><%= JSPUtil.printX(form.getStEntityName()) %></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block ><%=JSPUtil.printX(form.getStAddress())%></fo:block></fo:table-cell>
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell >
                        </fo:table-row> 
                        
                        <fo:table-row> 
                            <fo:table-cell number-columns-spanned="2" > 
                                <fo:block space-before.optimum="5pt" space-after.optimum="5pt"></fo:block> 
                            </fo:table-cell> 
                        </fo:table-row>
                        
                        <% } %>
                        
                        <% if (form.getStName()!=null) { %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block >Up. Yth, <%= JSPUtil.printX(form.getStName()) %></fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row> 
                            <fo:table-cell number-columns-spanned="2" > 
                                <fo:block space-before.optimum="5pt" space-after.optimum="5pt"></fo:block> 
                            </fo:table-cell> 
                        </fo:table-row>
                        
                        <% } %>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block text-align="center" >Perihal : <fo:inline text-decoration="underline">Pengajuan Klaim Asuransi <%= JSPUtil.printX(pol_type) %></fo:inline></fo:block></fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row> 
                            <fo:table-cell number-columns-spanned="2" > 
                                <fo:block space-before.optimum="5pt" space-after.optimum="5pt"></fo:block> 
                            </fo:table-cell> 
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block >Dengan Hormat,</fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row> 
                            <fo:table-cell number-columns-spanned="2" > 
                                <fo:block space-before.optimum="5pt" space-after.optimum="5pt"></fo:block> 
                            </fo:table-cell> 
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block space-after.optimum="10pt" text-align="justify">
                                    Bersama ini kami sampaikan kepada bapak LKP-LKP dan berkas-berkas pengajuan klaim <%= JSPUtil.printX(pol_type) %>
                                    dengan data-data sebagai berikut : 
                            </fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block font-family="Helvetica" font-size="8pt" display-align="center" border-width="0.1pt"> 
                <fo:table table-layout="fixed" border-style="solid" >
                    <fo:table-header>
                        <fo:table-row> 
                            <fo:table-cell border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >No</fo:block></fo:table-cell>
                            <fo:table-cell border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >No. LKP</fo:block></fo:table-cell>
                            <fo:table-cell border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >No Polis</fo:block></fo:table-cell>
                            <fo:table-cell border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Nama Tertanggung</fo:block></fo:table-cell>
                            <fo:table-cell border-bottom-style="solid" padding="1pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >Klaim</fo:block></fo:table-cell>
                            <fo:table-cell border-bottom-style="solid" padding="1pt" border-left-style="solid" border-right-style="solid"><fo:block line-height="6mm" text-align="center" font-size="10pt" >No Rekap</fo:block></fo:table-cell>
                        </fo:table-row>
                    </fo:table-header>
                    
                    <%=FOPUtil.printColumnWidth(colW,20,2,"cm")%>
                    <fo:table-body> 
                        
                        <%
                        BigDecimal claimGrossJumlah = null;
                        
                        String no_lkp = "";
                        String cust_name = "";
                        String name = "";
                        String noRekap = "";
                        String noRekap1 = "";
                        String coinsurer = "";
                        String noRekap3 = "";
                        // String nopol = "";
                        
                        for(int j=0;j<line.size();j++){
                            ARReceiptLinesView view = (ARReceiptLinesView) line.get(j);
                            
                            BigDecimal claimGrossTotal = null;
                            String noRekap2 = "";
                        /*
                        if (view.getInvoice().getStAttrPolicyNo().length()>16)
                        String nopol = view.getInvoice().getStAttrPolicyNo().substring(0,16);
                        else
                        String nopol = view.getInvoice().getStAttrPolicyNo().substring(0, view.getInvoice().getStAttrPolicyNo().length());
                         */
                            
                            final DTOList arInvoiceView = view.getARInvoice();
                            for (int l = 0; l < arInvoiceView.size(); l++) {
                                ARInvoiceView invoicView = (ARInvoiceView) arInvoiceView.get(l);
                                
                                claimGrossTotal = BDUtil.add(claimGrossTotal,invoicView.getDbAmount());
                                
                                final DTOList insPolicyView = invoicView.getInsPolicy();
                                for (int m = 0; m < insPolicyView.size(); m++) {
                                    InsurancePolicyView insPol = (InsurancePolicyView) insPolicyView.get(m);
                                    
                                    no_lkp = insPol.getStDLANo();
                                    
                                    cust_name = insPol.getStCustomerName();
                                    
                                    noRekap1 = insPol.getStReference3()!=null?insPol.getStReference3():insPol.getStReference4();
                                    
                                    final DTOList objects = insPol.getObjectsClaim();
                                    for (int n = 0; n < objects.size(); n++) {
                                        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) objects.get(n);
                                        
                                        name = obj.getStReference1();
                                        
                                        coinsurer = obj.getStReference8();
                                        
                                        noRekap3 = obj.getStRekapKreasi();
                                        
                                    }
                                    
                                    final DTOList koasur = insPol.getABAKoasur();
                                    for (int t = 0; t < koasur.size(); t++) {
                                        InsuranceKoasurView koa = (InsuranceKoasurView) koasur.get(t);
                                        
                                        if(koa.getStNorek()!=null)
                                            noRekap2 = koa.getStNorek();
                                        
                                    }
                                }
                                
                                if (coinsurer!=null) {
                                    if(noRekap3!=null){
                                        noRekap = noRekap3;
                                    }else if(noRekap3==null){
                                        noRekap = noRekap1;
                                    }else if(noRekap3==null&&noRekap2!=null){
                                        noRekap = noRekap2;
                                    }else if(noRekap3==null&&noRekap2==null){
                                        noRekap = noRekap2;
                                    }
                                } else if (coinsurer==null) {
                                    if(noRekap1!=null){
                                        noRekap = noRekap1;
                                    }else if(noRekap1==null&&noRekap2!=null){
                                        noRekap = noRekap2;
                                    }else if(noRekap1==null&&noRekap2==null) {
                                        noRekap = noRekap2;
                                    }
                                }
                                
                            }
                            
                            claimGrossJumlah = BDUtil.add(claimGrossJumlah,claimGrossTotal); 
                        %>
                        <fo:table-row>
                            <fo:table-cell padding="2pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center"><%=j+1%>.</fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center"><%=JSPUtil.printX(no_lkp)%></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt" border-left-style="solid"><fo:block line-height="6mm" text-align="center"><%=JSPUtil.print(view.getInvoice().getStAttrPolicyNo().substring(0,4)+"-"+view.getInvoice().getStAttrPolicyNo().substring(4,8)+"-"+view.getInvoice().getStAttrPolicyNo().substring(8,12)+"-"+view.getInvoice().getStAttrPolicyNo().substring(12,16)+"-"+view.getInvoice().getStAttrPolicyNo().substring(16,18))%></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt" border-left-style="solid"><fo:block line-height="6mm" text-align="start"><%=JSPUtil.printX(name)%></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt" border-left-style="solid"><fo:block line-height="6mm" text-align="end"><%=JSPUtil.printX(claimGrossTotal,2)%></fo:block></fo:table-cell>
                            <fo:table-cell padding="2pt" border-left-style="solid" border-right-style="solid"><fo:block line-height="6mm" text-align="center" font-size="6pt"><%=JSPUtil.printX(noRekap)%></fo:block></fo:table-cell>
                        </fo:table-row >
                        <%
                        }
                        %> 
                        <fo:table-row> 
                            <fo:table-cell number-columns-spanned="6" > 
                                <fo:block border-width="0.15pt" border-style="solid" border-before-precedence="0" line-height="0.15pt" space-before.optimum="2pt" space-after.optimum="2pt"></fo:block> 
                            </fo:table-cell> 
                        </fo:table-row> 
                        
                        <fo:table-row >
                            <fo:table-cell border-bottom-style="solid" border-left-style="solid" padding="2pt" number-columns-spanned="4"><fo:block text-align="center" font-weight="bold">JUMLAH</fo:block></fo:table-cell>
                            <fo:table-cell border-bottom-style="solid" border-left-style="solid" padding="2pt"><fo:block text-align="end" font-weight="bold"><%=JSPUtil.printX(claimGrossJumlah,2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-bottom-style="solid" border-left-style="solid" border-right-style="solid" padding="2pt" ></fo:table-cell>
                        </fo:table-row >
                        
                    </fo:table-body>  
                </fo:table>
            </fo:block>
            
            <fo:block font-size="10pt"> 
                <fo:table table-layout="fixed"> 
                    <fo:table-column column-width="100mm" />
                    <fo:table-column />
                    <fo:table-body> 
                        
                        <fo:table-row>
                            <fo:table-cell number-columns-spanned="2"><fo:block space-before.optimum="10pt" space-after.optimum="10pt" text-align="justify">
                                    Demikian pengajuan klaim ini kami sampaikan agar kiranya dapat diterima dengan baik, dan atas perhatian serta kerjasamanya
                                    yang baik selama ini kami ucapkan terima kasih.
                            </fo:block></fo:table-cell>
                        </fo:table-row> 
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center">Hormat Kami,</fo:block>
                                <fo:block text-align="center">PT. ASURANSI BANGUN ASKRIDA</fo:block>
                                <%-- <fo:block text-align="center">DIREKSI</fo:block> --%>
                            </fo:table-cell>
                        </fo:table-row>
                        
                        <fo:table-row> 
                            <fo:table-cell number-columns-spanned="2" > 
                                <fo:block space-before.optimum="30pt" space-after.optimum="30pt"></fo:block> 
                            </fo:table-cell> 
                        </fo:table-row>
                        
                        <fo:table-row>
                            <fo:table-cell ><fo:block ></fo:block></fo:table-cell>
                            <fo:table-cell >
                                <fo:block text-align="center"><fo:inline text-decoration="underline"><%= JSPUtil.printX(form.getStUserName()) %></fo:inline></fo:block>\
                                <fo:block text-align="center"><%= JSPUtil.printX(form.getStDivision()) %></fo:block>
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
                        message="<%=DateUtil.getDateStr(new Date(),"d-MMM-yyyy hh:mm:ss")%>_<%=JSPUtil.printX(claimGrossJumlah,0)%>" orientation="0">
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