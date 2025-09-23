<%@ page import="com.webfin.ar.model.ARRequestFee,
         com.crux.lov.LOVManager,
         com.crux.util.LOV,
         com.crux.web.controller.SessionManager,
         com.crux.lang.LanguageManager,
         com.webfin.ar.forms.RequestFeeList"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="PERMINTAAN BIAYA" >

    <%
                final RequestFeeList form = (RequestFeeList) frame.getForm();

                final boolean canNavigateBranch = SessionManager.getInstance().getSession().getStBranch() != null ? false : true;

                final boolean canNavigateRegion = SessionManager.getInstance().getSession().getStRegion() != null ? false : true;
    %>
    <table cellpadding=2 cellspacing=1>     
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <c:field width="200" caption="{L-ENGBranch-L}{L-INACabang-L}" lov="LOV_Branch" name="stBranch" type="string" readonly="<%=!canNavigateBranch%>" presentation="standard" changeaction="refresh" />
                            <c:field show="<%=form.getStBranch() != null%>" width="200" caption="{L-ENGRegion-L}{L-INARegion-L}" lov="LOV_Region" name="stRegion" type="string" readonly="<%=!canNavigateRegion%>" presentation="standard" changeaction="refresh" >
                                <c:lovLink name="cc_code" link="stBranch" clientLink="false"/>
                            </c:field>
                        </td>
                    </tr>
                    <tr>
                        <td>Tanggal Permintaan</td>
                        <td>:</td>
                        <td>
                            <c:field name="dtReqDateFrom" type="date" />
                            {L-ENGTo-L}{L-INAS/D-L} <c:field name="dtReqDateTo" type="date" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <c:field caption="{L-ENGShow active &amp; Pending only-L}{L-INAPembentukan Aktif &amp; Pending-L} " name="stActiveFlag" type="check" presentation="standard" changeaction="refresh" />
                            <c:field caption="{L-ENGShow Pending only-L}{L-INAPembentukan Belum Disetujui-L} " name="stEffectiveFlag" type="check" presentation="standard" changeaction="refresh" />
                        </td>
                    </tr>
                </table>
            </td>
        </tr>

        <tr>
            <td> 
                <c:button text="{L-ENGRefresh-L}{L-INARefresh-L}" event="refresh" />    
            </td>
        </tr>                     
        <tr>
            <td>
                <c:listbox name="list" autofilter="true" paging="true" selectable="true" view="com.webfin.ar.model.ARRequestFee" >
                    <c:listcol name="stARRequestID" title="" selectid="arreqid" />
                    <c:listcol name="stActiveFlag" title="Act" flag="true"/>
                    <c:listcol name="stEffectiveFlag" title="Eff" flag="true"/>
                    <c:listcol name="stCashierFlag" title="Cash" flag="true"/>
                    <c:listcol filterable="true" name="stTransactionNo" title="No. Persetujuan"/>
                    <c:listcol filterable="true" name="stRequestNo" title="No. Permintaan"/>
                    <c:listcol filterable="true" name="stAccountNo" title="No. Akun"/>
                    <c:listcol name="dtTglRequest" title="Tanggal Request" />
                    <c:listcol name="dbNominal" title="Biaya Awal" />
                    <c:listcol name="dbNominalUsed" title="Penggunaan" />
                    <c:listcol filterable="true" name="stStatus" title="Status"/>
                    <c:listcol name="stCreateName" title="User Name"/>
                    <c:listcol name="dtCreateDate" title="Create Date" />
                    <c:listcol name="stApprovedName" title="Approved Name"/>
                    <c:listcol name="dtApprovedDate" title="Approved Date" />
                    <c:listcol name="stCashierName" title="Cashier"/>
                </c:listbox>
            </td>
        </tr>

        <tr>
            <td>
                <c:button text="{L-ENGCreate-L}{L-INABuat Permintaan-L}" event="clickCreateRequest" />
                <c:button show="<%=form.isCanApprove()%>" text="{L-ENGCreate-L}{L-INABuat Persetujuan-L}" event="clickCreateApproval" />
                <c:button text="{L-ENGEdit-L}{L-INAUbah-L}" event="clickEdit" />
                <c:button text="{L-ENGView-L}{L-INALihat-L}" event="clickView" />
            </td>
        </tr>
        <tr>
            <td>
                <c:button show="<%=form.isCanCashier()%>" text="{L-ENGCashier Validate-L}{L-INA Validasi Kasir-L}" event="clickCashier" />
                <c:button show="<%=form.isCanCashier()%>" text="{L-ENGCreate-L}{L-INABuat Pengembalian-L} Biaya" event="clickCreateReimburse" />
            </td>
        </tr>
        <tr>
            <td>
                <c:button show="<%=form.isCanApprove()%>" text="{L-ENGApproval-L}{L-INASetujui-L}" event="clickApproval" />
                <c:button show="<%=form.isCanApproveDireksi()%>" text="{L-ENGApproval-L}{L-INASetujui-L} Direksi" event="clickApprovalDireksi" />
                <c:button text="Print" name="bprint" clientEvent="dynPrintClick();" />
                <%--<c:button show="<%=form.isCanReverse()%>" text="Reverse" event="clickReverse" />--%>
            </td>
        </tr>
    </table>
    <iframe src="" id=frmx width=1 height=1></iframe>

</c:frame>
<script>
    var frmx = docEl('frmx');
    function dynPrintClick() {

        if (f.arreqid.value=='') {
            alert('Pilih ID');
            return;
        }

        if (true) {
            frmx.src='x.fpc?EVENT=REQUEST_PRINT&arreqid='+f.arreqid.value;
            return;
        }
    }
</script>