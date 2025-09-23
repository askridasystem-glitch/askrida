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
                    <tr>
                        <c:field name="stPolicyTypeGroupID" type="string" width="200" caption="{L-ENGMonth-L}{L-INAGroup Polis-L}" lov="LOV_PolicyTypeGroup" changeaction="refresh" presentation="standard" />
                        <c:field name="stPolicyTypeID" type="string" width="200" caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" lov="LOV_PolicyType" changeaction="refresh" presentation="standard"/>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td><c:button text="Refresh" event="refresh" />
            <c:button text="Cek Balance" event="clickExcelBalance" />
            </td>
        </tr>

        <tr>
            <td colspan="3">
                <c:listbox name="list" autofilter="true" paging="true" selectable="true" view="com.webfin.insurance.model.InsuranceClosingView" >
                    <c:listcol name="stClosingID" title="" selectid="glpostingid" />
                    <c:listcol name="stReinsuranceClosingStatus" title="R/I" flag="true"/>
                    <c:listcol name="stFinanceClosingStatus" title="Keuangan" flag="true"/>
                    <%--<c:listcol name="stPolicyTypeID" title="Jenis Polis" />--%>
                     <c:listcol name="stPolicyTypeDescription" title="Jenis Polis" />
                    <c:listcol name="stTreatyType" title="Treaty" filterable="true"/>

                    <c:listcol name="dtPolicyDateStart" title="Tanggal Polis"/>
                    <c:listcol name="dtPolicyDateEnd" title="Tanggal Polis s/d"/>
                    <c:listcol name="dtPeriodStartStart" title="Periode Awal"/>
                    <c:listcol name="dtPeriodStartEnd" title="Periode Awal s/d"/>
                    <c:listcol name="dtInvoiceDate" title="Tanggal Pembukuan"/>
                    <c:listcol name="dbPremiReinsuranceTotal" title="Total Premi"/>
                    <c:listcol name="dbComissionReinsuranceTotal" title="Total Komisi"/>
                    <c:listcol name="stDataAmount" title="Jumlah Data" filterable="true"/>
                    <c:listcol name="stDataProses" title="Jumlah Diproses" filterable="true"/>
                    <c:listcol name="stNoSuratHutang" title="No Surat Hutang" filterable="true"/>

                    <c:listcol name="stCreateWho" title="User"/>
                    <c:listcol name="dtCreateDate" title="Tanggal Entry"/>
                </c:listbox>
            </td>
        </tr>
        
        <tr>
            <td colspan="3">
                <c:evaluate when="<%=isApproveClosing%>" >
                    <c:button text="Buat" event="clickCreate" />
                    <c:button text="Ubah" event="clickEdit" />
                </c:evaluate>
                <c:button text="Lihat" event="clickView" />
                <c:button show="<%=form.isEnableReverse()%>" text="Reverse" event="clickReverse" />

                <c:button text="Buat SOA" event="clickCreateSOA" />
            </td>
        </tr>
        
        
    </table>
    
</c:frame>