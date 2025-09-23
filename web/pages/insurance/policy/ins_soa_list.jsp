<%@ page import="com.webfin.insurance.model.InsuranceClosingView,
com.crux.lov.LOVManager,
com.crux.util.LOV,
com.crux.lang.LanguageManager,
com.webfin.insurance.form.InsuranceClosingList"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="STATEMENT OF ACCOUNT LIST" >
    
    <%
    final InsuranceClosingList form = (InsuranceClosingList) frame.getForm();

    final boolean isApproveClosing = form.isEnableApproveClosing();
    %>
    
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <%--
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
                    </tr>--%>
                </table>
            </td>
        </tr>
        <tr>
            <td><c:button text="Refresh" event="refresh" />
            
            </td>
        </tr>

        <tr>
            <td colspan="3">
                <c:listbox name="listSOA" autofilter="true" paging="true" selectable="true" view="com.webfin.insurance.model.InsurancePolicySOAView" >
                    <c:listcol name="stSOAID" title="" selectid="glpostingid" />
                    <c:listcol name="stStatus" title="Status" filterable="true" flag="true"/>
                    <c:listcol name="stSOANo" title="Nomor SOA" filterable="true"/>
                    <c:listcol name="stTreatyType" title="Treaty" filterable="true"/>
                    <c:listcol name="stTriwulan" title="Triwulan" filterable="true"/>
                    <c:listcol name="stTahun" title="Tahun" filterable="true"/>
                    <c:listcol name="stDescription" title="Keterangan" />
                    <c:listcol name="stEntityID" title="Entity ID" filterable="true"/>
                    <c:listcol name="stEntityName" title="Reasuradur" filterable="true"/>
                    <c:listcol name="dbPremiReinsurance" title="Premi Reasuransi"/>
                    <c:listcol name="dbComissionReinsurance" title="Komisi Reasuransi"/>
                    <c:listcol name="dbClaimReinsurance" title="Klaim Reasuransi"/>
                </c:listbox>
            </td>
        </tr>
        
        <tr>
            <td colspan="3">
                <%--
                <c:evaluate when="<%=isApproveClosing%>" >
                    <c:button text="Buat" event="clickCreateSOA" />
                    <c:button text="Ubah" event="clickEditSOA" />
                </c:evaluate>
                --%>
                <c:button text="Lihat" event="clickViewSOA" />
                <%--<c:button show="<%=form.isEnableReverse()%>" text="Reverse" event="clickReverse" />--%>

                <c:button text="Proses SOA" event="clickProcessSOA" />
            </td>
        </tr>
        
        
    </table>
    
</c:frame>