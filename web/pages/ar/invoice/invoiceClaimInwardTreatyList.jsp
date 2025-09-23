<%@ page import="com.crux.web.form.Form,
         com.crux.web.controller.SessionManager,
         com.webfin.insurance.model.InsurancePolicyInwardView,
         com.crux.lang.LanguageManager"%>
<%@ taglib prefix="c" uri="crux" %>
<%
            final Form formx = (Form) request.getAttribute("FORM");

            boolean hasNavigateBranch = SessionManager.getInstance().hasResource("ARINVOICE_NAVBR");

            boolean canApproved1 = SessionManager.getInstance().getSession().hasResource("AR_TRX_1_APPROVE");

            boolean canCreateDLA = SessionManager.getInstance().getSession().hasResource("AR_TRX_17_CREATE_DLA")
                    || SessionManager.getInstance().getSession().hasResource("AR_TRX_18_CREATE_DLA")
                    || SessionManager.getInstance().getSession().hasResource("AR_TRX_19_CREATE_DLA");

            String trxID = (String) formx.getProperty("trxId");
            boolean isClaim = trxID.equalsIgnoreCase("17") || trxID.equalsIgnoreCase("18") || trxID.equalsIgnoreCase("19");

%>
<c:frame title="<%="TRANSACTIONS" + formx.getProperty("trxId")%>" >
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:field name="customer"  caption="Customer" type="string" presentation="standard" width="200" />
                    <c:field name="trxno"  caption="Transaction No" type="string" presentation="standard" width="200" />
                    <c:field name="branch" type="string" caption="Branch" lov="LOV_Branch" presentation="standard" width="200" changeaction="btnSearch" readonly="<%=!hasNavigateBranch%>" />
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <c:button text="Search" event="btnSearch" />
            </td>
        </tr>
        <tr>
            <td>
                <c:field name="invoiceid" hidden="true"/>
                <c:listbox name="list" selectable="true" paging="true">
                    <c:listcol name="stARInvoiceID" title="ID" selectid="invoiceid"/>
                    <c:listcol name="stApprovedFlag" title="Eff" flag="true"/>
                    <c:listcol name="stEntityName" title="Customer" />
                    <c:listcol name="stInvoiceType" title="Type" />
                    <c:listcol name="stInvoiceNo" title="Nomor Bukti" />
                    <c:listcol name="dtInvoiceDate" title="Date" />
                    <c:listcol name="dtDueDate" title="Due Date" />
                    <c:listcol name="stCurrencyCode" title="Ccy" />
                    <c:listcol name="dbEnteredAmount" title="Amount" />
                    <c:listcol name="dbAmountSettled" title="Settled" />
                    <c:evaluate when="<%=isClaim%>">
                        <c:listcol name="stClaimStatus" title="Claim" />
                    </c:evaluate>
                </c:listbox>
            </td>
        </tr>
        <tr>
            <td>
                <c:button text="Create" event="clickCreate" />
                <c:evaluate when="<%=canCreateDLA%>">
                    <c:button text="Create DLA" event="clickCreateDLA" />
                </c:evaluate>
                <c:button text="Create From Inward" event="clickCreateFromInward" />
                <c:button text="Edit" event="clickEdit" />
                <c:button text="View" event="clickView" />
                <c:evaluate when="<%=canApproved1%>">
                    <c:button text="Approve" event="clickApprove" />
                </c:evaluate>
                <c:button text="Reverse" event="clickView" />
            </td>
        </tr>
        <c:evaluate when="<%=isClaim%>">
            <tr>
                <td>
                    Print <c:field name="printLang" width="250" type="string" lov="LOV_CLMINWARD_PRINTING" ><c:param name="vs" value="<%=trxID%>" /></c:field> in <c:field name="stLang" type="string" value="<%=LanguageManager.getInstance().getActiveLang()%>" lov="LOV_LANG" /><c:button text="Print" name="bprintx" clientEvent="dynPrintClick();" />
                </td>
            </tr>
        </c:evaluate>
    </table>
    <iframe src="" id=frmx width=1 height=1></iframe>
</c:frame>
<script>
    var frmx = docEl('frmx');

    function getSelectedAttr(c,ref) {
        return c.options[c.selectedIndex].getAttribute(ref);
    }

    function dynPrintClick() {

        if (f.invoiceID.value=='') {
            alert('Please select a transaction');
            return;
        }

        if (true) {
            frmx.src='x.fpc?EVENT=AR_INVOICE_PRT&invoiceid='+f.invoiceID.value+'&alter='+f.printLang.value+'&xlang='+f.stLang.value+'&antic='+(new Date().getTime());
            return;
        }
    }
</script>