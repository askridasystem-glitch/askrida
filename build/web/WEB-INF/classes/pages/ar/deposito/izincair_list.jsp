<%@ page import="com.webfin.ar.model.ARInvestmentIzinPencairanView,
         com.crux.lov.LOVManager,
         com.crux.util.LOV,
         com.crux.web.controller.SessionManager,
         com.crux.lang.LanguageManager,
         com.webfin.ar.forms.PengajuanIzinList"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="IZIN PENCAIRAN" >

    <%
                final PengajuanIzinList form = (PengajuanIzinList) frame.getForm();

                final boolean canNavigateBranch = SessionManager.getInstance().getSession().getStBranch() != null ? false : true;

                boolean admin = true;

                if (SessionManager.getInstance().getSession().getStBranch() != null) {
                    admin = false;
                }
    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <c:field width="200" caption="{L-ENGBranch-L}{L-INACabang-L}" lov="LOV_Branch" name="stBranch" type="string" readonly="<%=!canNavigateBranch%>" presentation="standard" changeaction="refresh" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <c:field caption="{L-ENGShow Pending only-L}{L-INA Izin Pencairan Sudah Disetujui-L} " name="stEffectiveFlag" type="check" presentation="standard" changeaction="refresh" />
                        </td>
                    </tr>
                </table>
            </td>
        </tr>

        <tr>
            <td>
                <c:listbox name="izincairlist" autofilter="true" paging="true" selectable="true" view="com.webfin.ar.model.ARInvestmentIzinPencairanView" >
                    <c:listcol name="stARIzinCairID" title="" selectid="arizincairid" />
                    <c:listcol name="stActiveFlag" title="Act" flag="true"/>
                    <c:listcol name="stApprovedCabFlag" title="Cab" flag="true"/>
                    <c:listcol name="stApprovedPusFlag" title="Pus" flag="true"/>
                    <c:listcol name="stPrintFlag" title="Prt" flag="true"/>
                    <c:listcol filterable="true" name="stNoSurat" title="No. Surat"/>
                </c:listbox>
            </td>
        </tr>

        <tr>
            <td>
                <c:button text="Pengajuan Cair" event="clickCreateIzinCair" />
                <c:button text="Ubah" event="clickEditIzinCair" />
                <c:button text="Lihat" event="clickViewIzinCair" />
            </td>
        </tr>
        <tr>
            <td>
                <c:button show="<%=form.isIzinApprovedCab()%>" text="Setujui Cabang" event="clickAppIzinCairCab" />
                <c:button show="<%=form.isIzinApprovedPus()%>" text="Setujui Pusat" event="clickAppIzinCairPus" />
            </td>
        </tr>
        <tr>
            <td>
                Print <c:field name="stPrintForm" width="250" type="string" lov="LOV_DEPO_PRINTING" ><c:param name="vs" value="IZINCAIR" /></c:field>
                <c:button text="Print" name="bprintx" clientEvent="dynPrintClick();" />
            </td>
        </tr>
    </table>
    <iframe src="" id=frmx width=1 height=1></iframe>

</c:frame>
<script>
    var frmx = docEl('frmx');
    function dynPrintClick() {

        if (f.arizincairid.value=='') {
            alert('Pilih Data');
            return;
        }

        if (true) {
            frmx.src='x.fpc?EVENT=CAIR_PRINT&arizincairid='+f.arizincairid.value+'&alter='+f.stPrintForm.value;
            return;
        }
    }
</script>