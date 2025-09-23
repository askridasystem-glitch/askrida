<%@ page import="com.webfin.gl.form.GLListForm,
com.webfin.gl.model.TitipanPremiView,
com.crux.web.controller.SessionManager,
java.util.ArrayList,
com.crux.util.fop.FOPUtil,
com.crux.util.DTOList,
com.crux.util.BDUtil,
com.crux.util.JSPUtil,
java.math.BigDecimal,
com.crux.util.SQLAssembler,
com.crux.lang.LanguageManager,
com.webfin.gl.model.*,
java.util.Date"%><?xml version="1.0" ?>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines the layout master -->
    <fo:layout-master-set>
        <fo:simple-page-master master-name="first"
                               page-height="21cm"
                               page-width="33cm"
                               margin-top="0.75cm"
                               margin-bottom="1cm"
                               margin-left="0.5cm"
                               margin-right="0.5cm">
            <fo:region-body margin-top="1cm" margin-bottom="1.5cm"/>
            <fo:region-before extent="1cm"/>
            <fo:region-after extent="1.5cm"/>
        </fo:simple-page-master>
    </fo:layout-master-set>
    
    <!-- starts actual layout -->
    <fo:page-sequence master-reference="first">
        
        
        <%
        
        GLListForm form =  (GLListForm) SessionManager.getInstance().getForm(request.getParameter("formid"));
        
        ArrayList colW = new ArrayList();
        
        colW.add(new Integer(10));
        colW.add(new Integer(40));
        colW.add(new Integer(40));
        colW.add(new Integer(85));
        colW.add(new Integer(10));
        colW.add(new Integer(15));
        colW.add(new Integer(30));
        colW.add(new Integer(30));
        colW.add(new Integer(30));
        colW.add(new Integer(25));
        colW.add(new Integer(25));
        
        //colW=FOPUtil.computeColumnWidth(colW,16,0," cm");
        
        SQLAssembler sqa = form.getSQATitipanPremiReport();
        
        sqa.clearOrder();
        sqa.addOrder("a.trx_id desc,a.trx_no");
        
        DTOList list = sqa.getList(TitipanPremiView.class);
        
        TitipanPremiView receipt = (TitipanPremiView) list.get(0);
        boolean isPosted = receipt.isApproved();
        %>
        
        <fo:flow flow-name="xsl-region-body">
            
            <%
            String bw = "0.5pt";
            %>
            
            <% if (!isPosted) { %>
            <fo:block font-weight="bold" text-align="center" line-height="20mm" color="red">SPECIMEN</fo:block>
            <% } %>            
            <fo:block font-weight="bold" text-align="center" line-height="20mm">TITIPAN PREMI</fo:block>
            <fo:block font-weight="bold" text-align="start" font-size="8pt">
                <% if (form.getTransdatefrom()!=null || form.getTransdateto()!=null) {%>
                <fo:block>
                    Date: <%=JSPUtil.printX(form.getTransdatefrom())%> to <%=JSPUtil.printX(form.getTransdateto())%>
                </fo:block>
                <% } %>
                <% if (form.getTransNumber()!=null) {%>
                <fo:block>
                    Trans # : <%=JSPUtil.printX(form.getTransNumber())%>
                </fo:block>
                <% } %>
                <% if (form.getAccountCode()!=null) {%>
                <fo:block>
                    Account : <%=JSPUtil.printX(form.getAccountCode())%>
                </fo:block>
                <% } %>
                <% if (form.getBranch()!=null) {%>
                <fo:block>
                    Branch : <%=JSPUtil.printX(form.getBranchDescription())%>
                </fo:block>
                <% } %>
                <% if (form.getStDescription()!=null) {%>
                <fo:block>
                    Bank : <%=JSPUtil.printX(form.getStDescription())%>
                </fo:block>
                <% } %>
                
                <fo:block>
                    Print Date: <%=JSPUtil.printDateTime(new Date())%>
                </fo:block>
                
            </fo:block>
            <fo:block space-after.optimum="14pt"/>
            
            <fo:block display-align="center" border-width="0.1pt">                
                <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>">
                    <fo:table-header>
                        <fo:table-row>   
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-bottom-style="solid" border-left-style="solid"><fo:block text-align="center" font-size="10pt" font-weight="bold">NO</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-bottom-style="solid" border-left-style="solid"><fo:block text-align="center" font-size="10pt" font-weight="bold">NO. BUKTI</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-bottom-style="solid" border-left-style="solid"><fo:block text-align="center" font-size="10pt" font-weight="bold">NO. AKUN</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-bottom-style="solid" border-left-style="solid"><fo:block text-align="center" font-size="10pt" font-weight="bold">KETERANGAN</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-bottom-style="solid" border-left-style="solid"><fo:block text-align="center" font-size="10pt" font-weight="bold">KTR</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-bottom-style="solid" border-left-style="solid"><fo:block text-align="center" font-size="10pt" font-weight="bold">KODA</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-bottom-style="solid" border-left-style="solid"><fo:block text-align="center" font-size="10pt" font-weight="bold">NO. POLIS</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-bottom-style="solid" border-left-style="solid"><fo:block text-align="center" font-size="10pt" font-weight="bold">TGL TITIP</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-bottom-style="solid" border-left-style="solid"><fo:block text-align="center" font-size="10pt" font-weight="bold">TGL ENTRY</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-bottom-style="solid" border-left-style="solid"><fo:block text-align="center" font-size="10pt" font-weight="bold">NILAI</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-bottom-style="solid" border-left-style="solid" border-right-style="solid"><fo:block text-align="center" font-size="10pt" font-weight="bold">SISA</fo:block></fo:table-cell>
                        </fo:table-row>
                    </fo:table-header>                 
                    
                    <%=FOPUtil.printColumnWidth(colW,31,2,"cm")%>
                    <fo:table-body>
                        
                        <%
                        BigDecimal [] t = new BigDecimal[2];
                        for (int i = 0; i < list.size(); i++) {
                            TitipanPremiView jv = (TitipanPremiView) list.get(i);
                            
                            int n=0;
                            t[n] = BDUtil.add(t[n++], jv.getDbAmount());
                            t[n] = BDUtil.add(t[n++], jv.getDbBalance());
                        %>
                        
                        <fo:table-row  >
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.printX(String.valueOf(i+1))%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.printX(jv.getStTransactionNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.printX(jv.getStHeaderAccountNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="start" font-size="8pt"><%=JSPUtil.printX(LanguageManager.getInstance().translate(jv.getStDescription()))%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.printX(jv.getStCounter())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.printX(jv.getStCostCenter())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.printX(jv.getStPolicyNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.printX(jv.getDtApplyDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.printX(jv.getDtCreateDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(jv.getDbAmount(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.printX(jv.getDbBalance(),2)%></fo:block></fo:table-cell>
                        </fo:table-row  >
                        
                        <%                        
                        }
                        %>
                        <fo:table-row  >
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" number-columns-spanned="9"><fo:block text-align="center" font-size="8pt" font-weight="bold">TOTAL MUTASI</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="8pt" font-weight="bold"><%=JSPUtil.printX(t[0],2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-left-style="solid"><fo:block text-align="end" font-size="8pt" font-weight="bold"><%=JSPUtil.printX(t[1],2)%></fo:block></fo:table-cell>
                        </fo:table-row  >
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>