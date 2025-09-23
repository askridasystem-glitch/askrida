<%@ page import="com.webfin.insurance.form.InsuranceUploadList,
         com.crux.util.Tools,
         com.webfin.insurance.model.uploadReinsuranceSpreadingView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="UPLOAD SPREADING RI" >

    <%

                InsuranceUploadList form = (InsuranceUploadList) request.getAttribute("FORM");

    %>

    <table cellpadding=2 cellspacing=1>


        <tr>
            <td>
                <%
                            uploadReinsuranceSpreadingView lastjv = new uploadReinsuranceSpreadingView();
                %>
                <c:field name="memorialID" hidden="true"/>
                <c:listbox name="listRI" selectable="true" paging="true" view="com.webfin.insurance.model.uploadReinsuranceSpreadingView" autofilter="true">
                    <%
                                final uploadReinsuranceSpreadingView jv = (uploadReinsuranceSpreadingView) current;

                                final boolean isdetail = jv != null && lastjv != null && Tools.isEqual(jv.getStInsuranceUploadID(), lastjv.getStInsuranceUploadID());

                                lastjv = jv;
                    %>                     

                    <% if (!isdetail) {%>
                    <c:listcol name="stInsuranceUploadID" title="ID" selectid="memorialID"/>
                    <c:listcol name="stStatus" title="Eff" flag="true" />
                    <c:listcol name="stRecapNo" title="No Rekap Upload" filterable="true" />
                    <%} else {%>
                    <c:listcol title="ID" selectid="memorialID"/>
                    <c:listcol name="stStatus" title="Eff" flag="true" />
                    <c:listcol title="No Rekap Upload" filterable="true" />
                    <%}%>

                    <c:listcol name="dbTSITotal" title="Total TSI" filterable="true" />
                    <c:listcol name="dbPremiTotal" title="Total Premi" filterable="true" />
                    <c:listcol name="stDataAmount" title="Jumlah Data" filterable="true" />
                    <c:listcol name="stCreateWho" title="User Entry" />
                </c:listbox>
            </td>
        </tr>

        <tr>
            <td>
                <c:button  text="Buat" event="clickUploadExcelRI" />
                <c:button  text="Ubah" event="clickEditRI" />
                <c:button  text="Lihat" event="clickViewRI" />
                <c:button  text="Proses Spreading" event="createSpreadingReinsByNoRecap" confirm="Yakin ingin diproses ?" />
            </td>
        </tr>
    </table>

</c:frame>