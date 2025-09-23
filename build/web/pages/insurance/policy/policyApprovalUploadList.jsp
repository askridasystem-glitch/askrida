<%@ page import="com.webfin.insurance.form.PolicyApprovalUploadList,
         com.crux.util.Tools,
         com.webfin.insurance.model.PolicyApprovalHeaderView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="UPLOAD APPROVAL POLIS" >

    <%
                PolicyApprovalUploadList form = (PolicyApprovalUploadList) request.getAttribute("FORM");

                final boolean canNavigateBranch = form.isCanNavigateBranch();
    %>

    <table cellpadding=2 cellspacing=1>
        
        <tr>
            <td>
                <table  cellpadding=2 cellspacing=1>
                    <tr>
                        <td>Cabang</td>
                            <td>:</td>
                            <td>
                                <c:field width="200" caption="{L-ENGBranch-L}{L-INACabang-L}" lov="LOV_Branch" name="branch" type="string" readonly="<%=!canNavigateBranch%>" changeaction="clickRefresh" />
                            </td>
                    </tr>
                    <tr>
                        <td>{L-ENGEntry Date-L}{L-INATanggal Entry-L}</td>
                            <td>:</td>
                            <td>
                                <c:field name="dtFilterEntryDateFrom" caption="Policy Date From" type="date" />
                                {L-ENGTo-L}{L-INAS/D-L} <c:field name="dtFilterEntryDateTo" caption="Policy Date To" type="date" />
                            </td>
                    </tr>
                </table>

            </td>
            
        </tr>
        
        <tr>
            <td>
                <c:button text="Refresh" event="refresh" />
            </td>
        </tr>
        <br><br>
        <tr>
            <td>
                <c:field name="memorialID" hidden="true"/>
                <c:listbox name="list" selectable="true" paging="true" view="com.webfin.insurance.model.PolicyApprovalHeaderView" autofilter="true">
                    <c:listcol name="stInsurancePolicyApprovalUploadID" title="ID" selectid="memorialID"/>
                    <%--<c:listcol name="stInsuranceUploadID" title="ID" filterable="true" />--%>
                    <c:listcol name="stEffectiveFlag" title="Eff" flag="true" />
                    <c:listcol name="stPostedFlag" title="Proses" flag="true" />
   
                    <c:listcol name="stRecapNo" title="Nomor Rekap" filterable="true" />
                    <c:listcol name="stJumlahData" title="Jumlah Polis" />
                    <c:listcol name="stJumlahDataProses" title="Polis Terproses" />
                    <c:listcol name="dtCreateDate" title="Tanggal Entry" />
                    <c:listcol name="stCreateWho" title="User Entry" filterable="true" />
                    <c:listcol name="stCreateName" title="User Entry" />
                    <c:listcol name="dtApproveDate" title="Tanggal Setujui" />
                    <c:listcol name="stApprovedWho" title="User Setujui" filterable="true"  />
                    <c:listcol name="stApprovedName" title="User Setujui"   />
                    <c:listcol name="stStatus" title="Status" />
                    
                </c:listbox>
            </td>
        </tr>
        <tr>
            <td>
                <c:button show="<%=form.isCanCreate()%>" text="Buat" event="clickCreate" />
                <c:button show="<%=form.isCanEdit()%>" text="Ubah" event="clickEdit" />
                <c:button text="Lihat" event="clickView" />
                <c:button show="<%=form.isCanApprove()%>" text="Setujui" event="clickApproval" />
                <c:button show="<%=form.isCanProcess()%>" text="Proses" event="prosesApprovalPolicyByNoRecap" confirm="Yakin ingin diproses ?" />
            </td> 
        </tr>
    </table>

</c:frame>