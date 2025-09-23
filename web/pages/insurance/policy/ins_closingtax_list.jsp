<%@ page import="com.webfin.insurance.model.InsuranceClosingView,
         com.crux.lov.LOVManager,
         com.crux.util.LOV,
         com.crux.lang.LanguageManager,
         com.webfin.insurance.form.InsuranceClosingList"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="CLOSING LIST" >

    <%
                final InsuranceClosingList form = (InsuranceClosingList) frame.getForm();

                final boolean isApproveClosing = form.isEnableApproveClosing();
    %>

    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>Tanggal Polis
                        </td>
                        <td>:</td>
                        <td>
                            <c:field name="dtPolicyDateStart" type="date" caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" />
                            S/D <c:field name="dtPolicyDateEnd" type="date" caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" />
                        </td>
                    </tr>
                    <tr>
                        <td>Tanggal Pembukuan
                        </td>
                        <td>:</td>
                        <td>
                            <c:field name="dtInvoiceDateStart" type="date" caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" />
                            S/D <c:field name="dtInvoiceDateEnd" type="date" caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" />
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <c:button text="Refresh" event="refresh" />
                <%--<c:button text="Cek Balance" event="clickExcelBalanceClaim" />--%>
            </td>
        </tr>

        <tr>
            <td colspan="3">
                <c:listbox name="listtax" autofilter="true" paging="true" selectable="true" view="com.webfin.insurance.model.InsuranceClosingView" >
                    <c:listcol name="stClosingID" title="" selectid="glpostingid" />
                    <c:listcol name="stReinsuranceClosingStatus" title="R/I" flag="true"/>
                    <c:listcol name="stFinanceClosingStatus" title="Keuangan" flag="true"/>
                    <c:listcol name="stClosingType" title="Jenis Closing" filterable="true"/>
                    <c:listcol name="dtPolicyDateStart" title="Tanggal Polis"/>
                    <c:listcol name="dtPolicyDateEnd" title="Tanggal Polis s/d"/>
                    <c:listcol name="dtInvoiceDate" title="Tanggal Pembukuan"/>
                    <c:listcol name="stNoSuratHutang" title="Rekap Surat Hutang" filterable="true"/>
                    <c:listcol name="dbTSITotal" title="Produksi Pajak pph21"/>
                    <c:listcol name="dbPremiReinsuranceTotal" title="OS Pajak pph21"/>
                    <c:listcol name="dbComissionReinsuranceTotal" title="Produksi Pajak pph23"/>
                    <c:listcol name="dbClaimReinsuranceTotal" title="OS Pajak pph23"/>
                    <c:listcol name="stCreateWho" title="User"/>
                    <c:listcol name="dtCreateDate" title="Tanggal Entry"/>
                </c:listbox>
            </td>
        </tr>

        <tr>
            <td colspan="3">
                <c:evaluate when="<%=isApproveClosing%>" >
                    <c:button text="Buat" event="clickCreateTax" />
                    <c:button text="Ubah" event="clickEditTax" />
                </c:evaluate>
                <c:button text="Lihat" event="clickViewTax" />
                <c:button show="<%=form.isEnableReverse()%>" text="Reverse" event="clickReverseTax" />
            </td>
        </tr>
        <%--
                <tr>
                    <td>
                        Print <c:field name="printLang" width="250" type="string" lov="LOV_CLOSING_PRINTING" ></c:field>
                        <c:button text="Print" name="bprintx" clientEvent="dynPrintClick();" />
                    </td>
                </tr>
        --%>
    </table>
    <iframe src="" id=frmx width=1 height=1></iframe>
</c:frame>
<script>
    var frmx = docEl('frmx');

    function dynPrintClick() {

        if (f.glpostingid.value=='') {
            alert('Please select a transaction');
            return;
        }

        frmx.src='x.fpc?EVENT=INS_CLOSING_RPT&closingid='+f.glpostingid.value+'&alter='+f.printLang.value;
        return;

    }

</script>