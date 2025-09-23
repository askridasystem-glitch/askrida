<%@ page import="com.webfin.gl.model.GLCurrencyHistoryView,
         com.crux.lov.LOVManager,
         com.crux.util.LOV,
         com.crux.lang.LanguageManager,
         com.webfin.gl.form.CurrencyList"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="CURRENCY LIST" >

    <%
                final CurrencyList form = (CurrencyList) frame.getForm();
    %>

    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>Periode Awal
            </td>
            <td>:</td>
            <td>
                <c:field name="dtPeriodStart" type="date" caption="{L-ENGPeriod Start-L}{L-INATanggal Awal-L}" />
                S/D <c:field name="dtPeriodEnd" type="date" caption="{L-ENGPeriod End-L}{L-INATanggal Akhir-L}" />
            </td>
        </tr>

        <tr>
            <td>Show Active
            </td>
            <td>:</td>
            <td><c:field name="showDeleted" caption="Show Deleted" type="check" width="250" />
            </td>
        </tr>


        <tr>
            <td colspan="3">
                <c:button text="Refresh" event="refresh" />
                <c:button text="Export Excel" event="clickExcel" />
            </td>
        </tr>
        <tr>
            <td colspan="3">
                <c:listbox name="list" autofilter="true" paging="true" selectable="true" view="com.webfin.gl.model.GLCurrencyHistoryView" >
                    <c:listcol name="stCurrencyHistID" title="" selectid="currencyid" />
                    <c:listcol name="stActiveFlag" title="Act" flag="true" />
                    <c:listcol filterable="true" name="stCurrencyCode" title="Valuta"/>
                    <c:listcol filterable="true" name="stCurrencyDesc" title="Description" />
                    <c:listcol name="dbRate" title="Kurs" />
                    <c:listcol name="dtPeriodStart" title="Periode Awal" />
                    <c:listcol name="dtPeriodEnd" title="Periode Akhir" />
                    
                </c:listbox>
            </td>
        </tr>

        <tr>
            <td colspan="3">
                <c:button text="Buat" event="clickCreate" />
                <c:button text="Ubah" event="clickEdit" />
                <c:button text="Lihat" event="clickView" />
            </td>
        </tr>

    </table>

</c:frame>