<%@ page import="com.webfin.insurance.form.EndorseUploadList,
         com.crux.util.Tools,
         com.webfin.insurance.model.UploadEndorseFireHeaderView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="UPLOAD ENDORSEMEN POLIS" >

    <%
                EndorseUploadList form = (EndorseUploadList) request.getAttribute("FORM");

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
                <c:listbox name="fireList" selectable="true" paging="true" view="com.webfin.insurance.model.UploadEndorseFireHeaderView" autofilter="true">
                    <c:listcol name="stInsuranceUploadID" title="ID" selectid="memorialID"/>
                    <%--<c:listcol name="stInsuranceUploadID" title="ID" filterable="true" />--%>
                    <c:listcol name="stEffectiveFlag" title="Eff" flag="true" />
                    <c:listcol name="stPostedFlag" title="Proses" flag="true" />
   
                    <c:listcol name="stRecapNo" title="Nomor Rekap" filterable="true" />
                     <c:listcol name="dbTSITotal" title="Total TSI" />
                    <c:listcol name="dbPremiTotal" title="Total Premi" />
                    <c:listcol name="stJumlahData" title="Jumlah Data" />
                    <c:listcol name="stJumlahDataProses" title="Data Terproses" />
                    <c:listcol name="dtCreateDate" title="Tanggal Entry" />
                    <c:listcol name="stCreateWho" title="User Entry" filterable="true"  />
                    <c:listcol name="stCreateName" title="User Entry" />
                    <c:listcol name="stStatus" title="Status" />
                    
                </c:listbox>
            </td>
        </tr>
        <tr>
            <td>
                <c:button show="<%=form.isCanCreateFire()%>" text="Buat" event="clickCreateFire" />
                <c:button show="<%=form.isCanEditFire()%>" text="Ubah" event="clickEditFire" />
                <c:button text="Lihat" event="clickViewFire" />
                <c:button show="<%=form.isCanApproveFire()%>" text="Setujui" event="clickApprovalFire" />
                <c:button show="<%=form.isCanApproveFire()%>" text="Proses" event="createEndorsemenFireByNoRecap" confirm="Yakin ingin diproses ?" />
            </td> 
        </tr>
    </table>

</c:frame>