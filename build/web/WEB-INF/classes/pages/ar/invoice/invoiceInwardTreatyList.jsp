<%@ page import="com.crux.web.form.Form,
         com.webfin.insurance.model.InsurancePolicyInwardView,
com.crux.web.controller.SessionManager"%>
<%@ taglib prefix="c" uri="crux" %>
<%
final Form formx = (Form)request.getAttribute("FORM");

boolean hasNavigateBranch = SessionManager.getInstance().hasResource("ARINVOICE_NAVBR");

boolean canApproved1 = SessionManager.getInstance().getSession().hasResource("AR_TRX_1_APPROVE");

boolean canCreateDLA = SessionManager.getInstance().getSession().hasResource("AR_TRX_17_CREATE_DLA")||
        SessionManager.getInstance().getSession().hasResource("AR_TRX_18_CREATE_DLA")||
        SessionManager.getInstance().getSession().hasResource("AR_TRX_19_CREATE_DLA");

String trxID = (String) formx.getProperty("trxId");
boolean isClaim = trxID.equalsIgnoreCase("17")||trxID.equalsIgnoreCase("18")||trxID.equalsIgnoreCase("19");

boolean canProsesKursBulanan = SessionManager.getInstance().getSession().hasResource("CURRENCY_RI_INWARD_PROCESS");

%>
<c:frame title="<%="TRANSACTIONS"+formx.getProperty("trxId")%>" >
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="customer" caption="Customer" type="string" presentation="standard" width="200" />
                                <c:field name="invoiceno" caption="Nomor Transaksi" type="string" presentation="standard" width="200" />
                                <c:field name="trxno" caption="Nomor Bukti" type="string" presentation="standard" width="200" />
                                <%--<c:field name="branch" type="string" caption="Branch" lov="LOV_Branch" presentation="standard" width="200" changeaction="btnSearch" readonly="<%=!hasNavigateBranch%>" />  --%>              
                            </table>
                        </td>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="transdatefrom" type="date" caption="Tanggal Mutasi" presentation="standard" width="200" />
                                <c:field name="transdateto" type="date" caption="Tanggal Mutasi" presentation="standard" width="200" />
                                <c:field lov="LOV_Entity" width="200" name="stEntityID" descfield="stEntityName" caption="Customer" type="string" presentation="standard" popuplov="true" />
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <c:button text="Refresh" event="btnSearch" />
            </td>
        </tr>
        <tr>
            <td>
                <c:field name="invoiceid" hidden="true"/>
                <c:listbox name="list" selectable="true" paging="true" autofilter="true" view="com.webfin.insurance.model.InsurancePolicyInwardView">
                    <c:listcol name="stARInvoiceID" title="ID" selectid="invoiceid"/>
                    <c:listcol name="stApprovedFlag" title="Eff" flag="true"/>
                    <c:listcol name="stARInvoiceID" title="ID" />
                    <c:listcol name="stEntityName" title="Customer" />
                    <c:listcol name="stInvoiceType" title="Type" />
                    <c:listcol filterable="true" name="stInvoiceNo" title="Nomor Bukti" />
                    <c:listcol filterable="true" name="stTransactionNoReference" title="Nomor Transaksi" />
                    <%--<c:listcol name="dtInvoiceDate" title="Date" />
                    <c:listcol name="dtDueDate" title="Due Date" />--%>
                    <c:listcol name="dtMutationDate" title="Mutation Date" />
                    <c:listcol name="stCurrencyCode" title="Ccy" />
                    <c:listcol name="dbEnteredAmount" title="Amount" />
                    <%--<c:listcol name="dbAmountSettled" title="Settled" />--%>
                    <c:evaluate when="<%=isClaim%>">
                        <c:listcol name="stClaimStatus" title="Claim" />
                    </c:evaluate>
                    <c:listcol name="stUserInput" title="User Input" filterable="true" />
                   <c:listcol name="stCreateName" title="User Input Name" filterable="true" />
                </c:listbox>
            </td>
        </tr>
        <tr>
            <td>
                <c:button text="Buat" event="clickCreateInwardTreaty" />
                <c:button text="Upload" event="clickCreateInwardTreatyUpload" />
                <c:button text="Copy" event="clickCopyInwardTreaty" />
                 <c:button text="Endorse" event="clickEndorseInwardTreaty" />
                <c:evaluate when="<%=canCreateDLA%>">
                    <c:button text="Create DLA" event="clickCreateDLA" />
                </c:evaluate>
                <c:button text="Ubah" event="clickEditInwardTreaty" />
                <c:button text="Lihat" event="clickViewInwardTreaty" />
                <c:button text="Lihat Format Lama" event="clickView" />
                <c:evaluate when="<%=canApproved1%>">
                    <c:button text="Setujui" event="clickApproveInwardTreaty" />
                    <c:button text="Reverse" event="clickViewInwardTreaty" />
                </c:evaluate>
                <c:button text="{L-ENGReApprove-L}{L-INAApprove Ulang-L}" event="clickReApproval" />
                <c:evaluate when="<%=canProsesKursBulanan%>">
                    <c:button text="Proses Update Kurs" event="clickUpdateKursBulananTreaty" />
               </c:evaluate>
            </td>
        </tr>
    </table>
</c:frame>