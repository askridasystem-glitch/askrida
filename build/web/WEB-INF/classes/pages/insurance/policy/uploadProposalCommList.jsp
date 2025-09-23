<%@ page import="com.webfin.insurance.form.InsuranceUploadList,
         com.crux.util.Tools,
         com.crux.web.controller.SessionManager,
         com.webfin.insurance.model.uploadProposalCommView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="UPLOAD PROPOSAL KOMISI" >

    <%

                InsuranceUploadList form = (InsuranceUploadList) request.getAttribute("FORM");

                final boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("POL_PROP_NAVBR");
    %>

    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field width="200" caption="{L-ENGBranch-L}{L-INACabang-L}" lov="LOV_Branch" name="stBranch" type="string" readonly="<%=!canNavigateBranch%>" presentation="standard" changeaction="refresh" />
                                <c:field caption="Belum Di-Realisasi" name="stNotRealized" type="check" presentation="standard" changeaction="refresh" />
                                <c:field caption="Belum Di-Setujui KP" name="stNotApproved" type="check" presentation="standard" changeaction="refresh" />
                                <%--<c:field caption="Tampilkan Keseluruhan" name="stShowAll" type="check" presentation="standard" changeaction="refresh" />--%>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>

        <tr>
            <td>
                <%
                            uploadProposalCommView lastjv = new uploadProposalCommView();
                %>
                <%--<c:field name="memorialID" hidden="true"/>--%>
                <c:listbox name="listComm" autofilter="true" selectable="true" paging="true" view="com.webfin.insurance.model.uploadProposalCommView">
                    <%
                                final uploadProposalCommView jv = (uploadProposalCommView) current;

                                final boolean isdetail = jv != null && lastjv != null && Tools.isEqual(jv.getStInsuranceUploadID(), lastjv.getStInsuranceUploadID());

                                lastjv = jv;
                    %>                     

                    <% if (!isdetail) {%>
                    <c:listcol name="stInsuranceUploadID" title="ID" selectid="memorialID"/>
                    <c:listcol name="stStatus1" title="KaOps" flag="true" />
                    <c:listcol name="stStatus2" title="Kasie" flag="true" />
                    <c:listcol name="stStatus3" title="Kabag" flag="true" />
                    <c:listcol name="stStatus4" title="Kadiv" flag="true" />
                    <c:listcol name="stCostCenterCode" title="Cabang" />
                    <c:listcol name="dtPeriodeAwal" title="Tanggal" />
                    <c:listcol name="dtPeriodeAkhir" title="s/d Tanggal" />
                    <c:listcol name="stNoSuratHutang" title="No Surat Hutang" filterable="true" />
                    <%} else {%>
                    <c:listcol title="ID" selectid="memorialID"/>
                    <c:listcol title="KaOps" flag="true" />
                    <c:listcol title="Kasie" flag="true" />
                    <c:listcol title="Kabag" flag="true" />
                    <c:listcol title="Kadiv" flag="true" />
                    <c:listcol title="Cabang" />
                    <c:listcol title="Tanggal" />
                    <c:listcol title="s/d Tanggal" />
                    <c:listcol title="No Surat Hutang" filterable="true" />
                    <%}%>

                    <c:listcol name="dbAmountTotal" title="Total Komisi" filterable="true" />
                    <c:listcol name="stDataAmount" title="Jumlah Data" filterable="true" />
                    <c:listcol name="stCreateWho" title="User Entry" />
                    <c:listcol name="stReinsurerNote" title="No. Bukti Pemby." filterable="true" />
                </c:listbox>
            </td>
        </tr>
        <tr>
            <td>
                <c:button show="<%=form.isCanCreate() || form.isApprovalSie()%>" text="Buat" event="clickCreateProposalComm" />
                <c:button show="<%=form.isCanCreate()%>" text="Ubah" event="clickEditProposalComm" />
                <c:button text="Lihat" event="clickViewProposalComm" />
            </td>
        </tr>
        <tr>
            <td>
                <c:button show="<%=form.isApprovalCab()%>" text="Setujui Cabang" event="clickApprovalProposalComm1" />
                <c:button show="<%=form.isApprovalSie()%>" text="Setujui Kasie" event="clickApprovalProposalComm2" />
                <c:button show="<%=form.isApprovalBag()%>" text="Setujui Kabag" event="clickApprovalProposalComm3" />
                <c:button show="<%=form.isApprovalDiv()%>" text="Setujui Kadiv" event="clickApprovalProposalComm4" />
                <c:button show="<%=form.isApprovalCab()%>" text="Reverse" event="clickReverseProposalCommCab" />
                <c:button show="<%=form.isApprovalSie()%>" text="Reverse" event="clickReverseProposalCommSie" />
            </td>
        </tr>
        <tr>
            <td>
                <c:button text="Print (PDF)" name="bprintx" clientEvent="dynPrintClick();" />
                <c:button text="Print (Excel)" name="bprintxE" clientEvent="dynPrintClickExcel();" />
                <c:button text="Print Dokumen" name="bprintDoc" clientEvent="dynPrintClickDoc();" />
            </td>
        </tr>
    </table>
    <iframe src="" id=frmx width=1 height=1></iframe>

</c:frame>

<script>
    var frmx = docEl('frmx');
    function dynPrintClick() {

        if (f.memorialID.value=='') {
            alert('Pilih No. SHK');
            return;
        }

        if (true) {
            frmx.src='x.fpc?EVENT=INS_UPLOAD_PROPOSAL&receiptid='+f.memorialID.value;
            return;
        }
    }

    function dynPrintClickExcel() {

        if (f.memorialID.value=='') {
            alert('Pilih No. SHK');
            return;
        }

        if (true) {
            frmx.src='x.fpc?EVENT=INS_UPLOAD_PROPOSAL_EXCEL&receiptid='+f.memorialID.value;
            return;
        }
    }

    function dynPrintClickDoc() {

        if (f.memorialID.value=='') {
            alert('Pilih No. SHK');
            return;
        }

        if (true) {
            frmx.src='x.fpc?EVENT=INS_UPLOAD_PROPOSAL_DOC&receiptid='+f.memorialID.value;
            return;
        }
    }
</script>