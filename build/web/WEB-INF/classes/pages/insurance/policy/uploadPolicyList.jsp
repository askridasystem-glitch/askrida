<%@ page import="com.webfin.insurance.form.InsuranceUploadList,
                com.crux.util.Tools,
                com.webfin.insurance.model.uploadEndorsemenView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="UPLOAD ENDORSE PA KREASI" >

    <%

   InsuranceUploadList form  =(InsuranceUploadList) request.getAttribute("FORM");

    %>

    <table cellpadding=2 cellspacing=1>
        

        <tr>
            <td>
                <%
                   uploadEndorsemenView lastjv = new uploadEndorsemenView();
                %>
                <c:field name="memorialID" hidden="true"/>
                <c:listbox name="list" selectable="true" paging="true" view="com.webfin.insurance.model.uploadEndorsemenView" autofilter="true">
                    <%
                          final uploadEndorsemenView jv = (uploadEndorsemenView) current;

                          final boolean isdetail = jv!=null && lastjv!=null && Tools.isEqual(jv.getStInsuranceUploadID(), lastjv.getStInsuranceUploadID());

                          lastjv=jv;
                    %>

                    <%--
                    <% if (!isdetail){%> 
                        <c:listcol name="stInsuranceUploadID" title="ID" selectid="memorialID"/>
                        <c:listcol name="stStatus" title="Eff" flag="true" />
                        <c:listcol name="stRecapNo" title="No Rekap Upload" filterable="true" />
                        <c:listcol name="dbPremiKoasTotal" title="Total Premi Koas" filterable="true" />
                        <c:listcol name="dbKomisiKoasTotal" title="Total Komisi Koas" filterable="true" />
                        <c:listcol name="stDataAmount" title="Jumlah Data" filterable="true" />
                    <%}else{%>
                        <c:listcol title="ID"/>
                        <c:listcol name="stStatus" title="Eff" flag="true" />
                        <c:listcol title="No Rekap Upload" filterable="true" />
                        <c:listcol title="Total Premi Koas" filterable="true" />
                        <c:listcol title="Jumlah Data" filterable="true" />
                    <%}%>
                    --%>

                     

                     <% if (!isdetail){%>
                        <c:listcol name="stInsuranceUploadID" title="ID" selectid="memorialID"/>
                        <c:listcol name="stStatus" title="Eff" flag="true" />
                        <c:listcol name="stRecapNo" title="No Rekap Upload" filterable="true" />
                    <%}else{%>
                        <c:listcol title="ID" selectid="memorialID"/>
                        <c:listcol title="Eff" flag="true" />
                        <c:listcol title="No Rekap Upload" filterable="true" />
                    <%}%>

                        <c:listcol name="dbPremiKoasTotal" title="Total Premi Koas" filterable="true" />
                        <c:listcol name="dbKomisiKoasTotal" title="Total Komisi Koas" filterable="true" />
                        <c:listcol name="stDataAmount" title="Jumlah Data" filterable="true" />
                   <%--
                    <c:listcol name="stPolicyNo" title="No Polis" filterable="true" />
                    <c:listcol name="stOrderNo" title="No Urut" />
                    <c:listcol name="stNama" title="Nama" filterable="true" />
                    <c:listcol name="dbTSI" title="TSI" filterable="true" />
                    <c:listcol name="dbPremi" title="Premi" filterable="true" />--%>
                    <c:listcol name="stCreateWho" title="User Entry" />
                </c:listbox>
            </td>
        </tr>

        <tr>
            <td>
                <c:button  text="Buat" event="clickUploadExcel" />
                <c:button  text="Ubah" event="clickEdit" />
                <c:button  text="Lihat" event="clickView" />
                <c:button  text="Proses Endorsemen" event="createEndorsemenByNoRecap" confirm="Yakin ingin diproses ?" />
            </td>
        </tr>
    </table>

</c:frame>