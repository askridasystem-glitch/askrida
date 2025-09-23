<%@ page import="com.webfin.insurance.form.InsuranceUploadList,
         com.crux.util.Tools,
         com.crux.web.controller.SessionManager,
         com.webfin.insurance.model.BiayaPemasaranView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="BIAYA PEMASARAN" >

    <%
                InsuranceUploadList form = (InsuranceUploadList) request.getAttribute("FORM");

                final boolean canNavigateBranch = SessionManager.getInstance().getSession().hasResource("POL_PROP_NAVBR");

                int phase = 0;

                if (form.getStBranch() != null) {
                    phase = 1;
                }

                if (form.getPeriod() != null) {
                    phase = 2;
                }
    %>

    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <tr>
                                    <td>
                                        <c:field width="200" caption="{L-ENGBranch-L}{L-INACabang-L}" lov="LOV_Branch" name="stBranch" type="string" readonly="<%=!canNavigateBranch%>" presentation="standard" changeaction="refresh" />
                                    </td>
                                </tr>
                                <c:evaluate when="<%=phase >= 1%>">
                                    <tr>
                                        <td>Period</td>
                                        <td>:</td>
                                        <td><c:field name="period" type="string" caption="Period" lov="LOV_MONTH_Period" changeaction="refresh" /></td>
                                    </tr>
                                </c:evaluate>
                                <c:evaluate when="<%=phase >= 2%>">
                                    <tr>
                                        <td>Year</td>
                                        <td>:</td>
                                        <td><c:field name="year" type="string" caption="Year" lov="LOV_GL_Years3" mandatory="true" changeaction="onChangeAmount"/></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <c:field width="200" caption="Nilai Produksi" name="dbAmount" type="money16.2" presentation="standard" readonly="true" />
                                            <c:field width="200" caption="Biaya 1%" name="dbAmountShare" type="money16.2" presentation="standard" readonly="true" />
                                        </td>
                                    </tr>
                                </c:evaluate>
                                <tr>
                                    <td>
                                        <c:field caption="Belum Di-Realisasi" name="stNotRealized" type="check" presentation="standard" changeaction="refresh" />
                                        <c:field caption="Belum Di-Setujui KP" name="stNotApproved" type="check" presentation="standard" changeaction="refresh" />

                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <c:button text="Refresh" event="onChangeAmount" />
            </td>
        </tr>

        <tr>
            <td>
                <c:listbox name="listPemasaran" autofilter="true" selectable="true" paging="true" view="com.webfin.insurance.model.BiayaPemasaranView">
                    <c:listcol name="stPemasaranID" title="ID" selectid="memorialID"/>
                    <c:listcol name="stStatus1" title="KaOps" flag="true" />
                    <c:listcol name="stStatus2" title="Kasie" flag="true" />
                    <c:listcol name="stStatus3" title="Kabag" flag="true" />
                    <c:listcol name="stStatus4" title="Kadiv" flag="true" />
                    <c:listcol name="stCostCenterCode" title="Cabang" />
                    <c:listcol name="stNoSPP" title="No Surat" filterable="true" />
                    <c:listcol name="stJumlahData" title="Jumlah Data" />
                    <c:listcol name="dbTotalBiaya" title="Total" />
                    <c:listcol name="stNoBukti" title="No Bukti" filterable="true" />
                    <c:listcol name="dtCreateDate" title="Tgl Entry" />
                    <c:listcol name="stCreateWho" title="User Entry" />
                </c:listbox>
            </td>
        </tr>
        <tr>
            <td>
                <c:button show="<%=form.isCanCreate() || form.isApprovalSie()%>" text="Buat" event="clickCreatePemasaran" />
                <c:button show="<%=form.isCanCreate()%>" text="Ubah" event="clickEditPemasaran" />
                <c:button text="Lihat" event="clickViewPemasaran" />
            </td>
        </tr>
        <tr>
            <td>
                <c:button show="<%=form.isApprovalCab()%>" text="Setujui Cabang" event="clickApprovalPms1" />
                <c:button show="<%=form.isApprovalSie()%>" text="Setujui Kasie" event="clickApprovalPms2" />
                <c:button show="<%=form.isApprovalBag()%>" text="Setujui Kabag" event="clickApprovalPms3" />
                <c:button show="<%=form.isApprovalDiv()%>" text="Setujui Kadiv" event="clickApprovalPms4" />
                <c:button show="<%=form.isApprovalCab()%>" text="Reverse" event="clickReversePmsCab" />
                <c:button show="<%=form.isApprovalSie()%>" text="Reverse" event="clickReversePmsSie" />
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
            frmx.src='x.fpc?EVENT=INS_BIAYA_PEMASARAN&receiptid='+f.memorialID.value;
            return;
        }
    }

    function dynPrintClickExcel() {

        if (f.memorialID.value=='') {
            alert('Pilih No. SHK');
            return;
        }

        if (true) {
            frmx.src='x.fpc?EVENT=INS_BIAYA_PEMASARAN_EXCEL&receiptid='+f.memorialID.value;
            return;
        }
    }

    function dynPrintClickDoc() {

        if (f.memorialID.value=='') {
            alert('Pilih No. SHK');
            return;
        }

        if (true) {
            frmx.src='x.fpc?EVENT=INS_BIAYA_PEMASARAN_DOC&receiptid='+f.memorialID.value;
            return;
        }
    }
</script>