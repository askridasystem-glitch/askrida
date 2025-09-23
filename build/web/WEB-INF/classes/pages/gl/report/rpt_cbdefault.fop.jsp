<%@ page import="com.webfin.gl.form.GLListForm,
com.crux.web.controller.SessionManager,
java.util.ArrayList,
com.crux.util.SQLAssembler,
com.webfin.gl.model.JournalView,
com.crux.util.fop.FOPUtil,
com.crux.util.DTOList,
com.crux.util.BDUtil,
com.crux.util.JSPUtil,
com.crux.util.Tools,
java.math.BigDecimal,
com.crux.util.ListUtil,
com.crux.common.parameter.Parameter,
com.crux.lang.LanguageManager,
java.util.Date"%><?xml version="1.0" ?>

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
    
    <!-- defines the layout master -->
    <fo:layout-master-set>
        <fo:simple-page-master master-name="first"
                               page-width="29.7cm"
                               page-height="21cm"
                               margin-top="1cm"
                               margin-bottom="2cm"
                               margin-left="0.7cm"
                               margin-right="1cm">
            <fo:region-body margin-top="3cm" margin-bottom="1.5cm"/>
            <fo:region-before extent="3cm"/>
            <fo:region-after extent="1.5cm"/>
        </fo:simple-page-master>
    </fo:layout-master-set>
    
    <!-- starts actual layout -->
    <fo:page-sequence master-reference="first">
        
        <%
        
        GLListForm form =  (GLListForm) SessionManager.getInstance().getForm(request.getParameter("formid"));
        
        ArrayList colW = new ArrayList();
        
        colW.add(new Integer(10));
        colW.add(new Integer(20));
        colW.add(new Integer(50));
        colW.add(new Integer(30));
        colW.add(new Integer(110));
        colW.add(new Integer(30));
        colW.add(new Integer(30));
        
        SQLAssembler sqa = form.getSQACashBankRECAP();
        
        sqa.addGroup("date_trunc('day',a.create_date),a.trx_no,substr(b.accountno,1,5),substr(b.accountno,13,3),d.value_string order by a.trx_no,substr(b.accountno,1,5) ");
        
        DTOList list = sqa.getList(JournalView.class);
        
        JournalView journal = (JournalView) list.get(0);
        %>
        
        
        <fo:flow flow-name="xsl-region-body">
            
            <%
            String bw = "0.5pt";
            %>
            
            <fo:block font-weight="bold" text-align="center" line-height="20mm">DAFTAR MUTASI SUMMARY</fo:block>
            <fo:block font-weight="bold" text-align="start" font-size="8pt">
                <% if (form.getTransdatefrom()!=null || form.getTransdateto()!=null) {%>
                <fo:block>
                    Date: <%=JSPUtil.printX(form.getTransdatefrom())%> to <%=JSPUtil.printX(form.getTransdateto())%>
                </fo:block>
                <% } %>
                <% if (form.getAccountCode()!=null) { %>
                <fo:block>
                    Account : <%=JSPUtil.printX(form.getAccountCode())%>
                </fo:block>
                <fo:block>
                    Account Description : <%=JSPUtil.printX(form.getAccountDescription())%>
                </fo:block>
                <% } %>
                <% if (form.getDescription()!=null) {%>
                <fo:block>
                    Description : <%=JSPUtil.printX(form.getDescription())%>
                </fo:block>
                <% } %>
                <% if (form.getTransNumber()!=null) {%>
                <fo:block>
                    Trans # : <%=JSPUtil.printX(form.getTransNumber())%>
                </fo:block>
                <% } %>
                <% if (form.getPolicyNo()!=null) {%>
                <fo:block>
                    Policy No. : <%=JSPUtil.printX(form.getPolicyNo())%>
                </fo:block>
                <% } %>
                <% if (form.getBranch()!=null) {%>
                <fo:block>
                    Branch : <%=JSPUtil.printX(form.getBranchDescription())%>
                </fo:block>
                <% } %>
                
                <fo:block>
                    Tanggal : <%=JSPUtil.printX(journal.getDtCreateDate())%>
                </fo:block>
                
            </fo:block>
            <fo:block space-after.optimum="14pt"/>
            
            <fo:block display-align="center" border-width="0.1pt">                
                <fo:table table-layout="fixed" border-style="solid" border-width="<%=bw%>">
                    <fo:table-header>
                        <fo:table-row>   
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-bottom-style="solid" border-left-style="solid"><fo:block text-align="center" font-size="10pt" font-weight="bold">NO</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-bottom-style="solid" border-left-style="solid"><fo:block text-align="center" font-size="10pt" font-weight="bold">TANGGAL</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-bottom-style="solid" border-left-style="solid"><fo:block text-align="center" font-size="10pt" font-weight="bold">NO. BUKTI</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-bottom-style="solid" border-left-style="solid"><fo:block text-align="center" font-size="10pt" font-weight="bold">NOREK</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-bottom-style="solid" border-left-style="solid"><fo:block text-align="center" font-size="10pt" font-weight="bold">KETERANGAN</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-bottom-style="solid" border-left-style="solid"><fo:block text-align="center" font-size="10pt" font-weight="bold">DEBET</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" border-bottom-style="solid" border-left-style="solid"><fo:block text-align="center" font-size="10pt" font-weight="bold">KREDIT</fo:block></fo:table-cell>
                        </fo:table-row>
                    </fo:table-header>
                    
                    <%=FOPUtil.printColumnWidth(colW,28,2,"cm")%>
                    
                    <fo:table-body>
                        <%          
                        String description = null;
                        BigDecimal amount = new BigDecimal(0);
                        BigDecimal debit = new BigDecimal(0);
                        BigDecimal credit = new BigDecimal(0);
                        BigDecimal totalDebit = new BigDecimal(0);
                        BigDecimal totalCredit = new BigDecimal(0);
                        for (int i = 0; i < list.size(); i++) {
                            JournalView jv = (JournalView) list.get(i);
                            
                            description = Parameter.readStringAccounts("ACCOUNT_"+jv.getStAccountNo().substring(0,5));
                            
                            amount = BDUtil.sub(jv.getDbDebit(), jv.getDbCredit());
                            
                            if (Tools.compare(amount, BDUtil.zero)<0) {
                                credit = BDUtil.negate(amount);
                                debit = BDUtil.zero;
                            } else if (Tools.compare(amount, BDUtil.zero)>0) {
                                credit = BDUtil.zero;
                                debit = amount;
                            }
                            
                            totalDebit = BDUtil.add(totalDebit, debit);
                            totalCredit = BDUtil.add(totalCredit, credit);
                        
                        %>
                        <fo:table-row  >
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.print(String.valueOf(i+1))%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.print(jv.getDtCreateDate())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.print(jv.getStTransactionNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="center" font-size="8pt"><%=JSPUtil.print(jv.getStAccountNo())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="start" font-size="8pt"><%=LanguageManager.getInstance().translate(description.toUpperCase())%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.print(jv.getDbDebit(),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" border-bottom-style="solid" padding="2pt"><fo:block text-align="end" font-size="8pt"><%=JSPUtil.print(jv.getDbCredit(),2)%></fo:block></fo:table-cell>
                        </fo:table-row  >
                        
                        <%
                        
                        }
                        %>
                        <fo:table-row  >
                            <fo:table-cell border-width="<%=bw%>" padding="2pt" number-columns-spanned="5"><fo:block text-align="center" font-size="8pt">TOTAL MUTASI</fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt" font-weight="bold"><%=JSPUtil.print((BigDecimal) list.getTotal("debit"),2)%></fo:block></fo:table-cell>
                            <fo:table-cell border-width="<%=bw%>" padding="2pt"><fo:block text-align="end" font-size="8pt" font-weight="bold"><%=JSPUtil.print((BigDecimal) list.getTotal("credit"),2)%></fo:block></fo:table-cell>
                        </fo:table-row  >
                        
                    </fo:table-body>
                </fo:table>
            </fo:block>
            
            <fo:block>
                Print Date: <%=JSPUtil.printDateTime(new Date())%>
            </fo:block>
            
        </fo:flow>
    </fo:page-sequence>
</fo:root>