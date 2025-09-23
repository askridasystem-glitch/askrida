<%@ page import="com.webfin.gl.model.ClosingHeaderView,
com.crux.lov.LOVManager,
com.crux.util.LOV,
com.crux.lang.LanguageManager,
com.webfin.gl.form.ClosingSettingList"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="CLOSING SETTING LIST" >
    
    <%
    final ClosingSettingList form = (ClosingSettingList) frame.getForm();
    %>

    
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>Cabang</td>
            <td>:</td>
            <td>
                <c:field name="branch" type="string" caption="Cabang" lov="LOV_Branch" changeaction="chgBranch"  width="200" />
            </td>
        </tr>
        <%--
        <tr>
            <td>Bulan</td>
            <td>:</td>
            <td>
               <c:field name="stMonths" type="string" caption="{L-ENGMonth-L}{L-INABulan Transaksi-L}" lov="LOV_MONTH_Period" changeaction="refresh"/>
            </td>
        </tr>--%>
        <tr>
            <td>Tahun</td>
            <td>:</td>
            <td>
                <c:field name="stYears" type="string" caption="{L-ENGBulan-L}{L-INATahun Transaksi-L}" lov="LOV_GL_Years2" changeaction="refresh"/>
            </td>
        </tr>

        <tr>
            <td colspan="3">
                <c:listbox name="list" autofilter="true" paging="true" selectable="true" view="com.webfin.gl.model.ClosingHeaderView" >
                    <c:listcol name="stClosingPeriodID" title="" selectid="glpostingid" />
                    <c:listcol name="stClosingPeriodID" title="ID" />
                    <c:listcol name="stActiveFlag" title="Act" flag="true"/>
                    <c:listcol name="stFiscalYear" title="Tahun"/>
                    <%--<c:listcol name="stPeriodNum" title="Bulan"/>--%>
                    <c:listcol filterable="true" name="stCostCenterCode" title="Cabang"/>
                    <c:listcol name="stCostCenter" title="Cabang"/>
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