<%@ page import="com.webfin.ar.model.ARRequestFee,
         com.crux.lov.LOVManager,
         com.crux.util.LOV,
         com.crux.util.*,
         com.crux.web.controller.SessionManager,
         com.crux.lang.LanguageManager,
         com.webfin.ar.forms.RequestFeeList"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="REALISASI ANGGARAN" >
    <%
                final RequestFeeList form = (RequestFeeList) frame.getForm();

                //final boolean canNavigateBranch = form.getStBranch() != null ? false : true;
                //final boolean canNavigateRegion = SessionManager.getInstance().getSession().getStDivisionID() != null ? false : true;

                //final boolean canNavigateStBranch = SessionManager.getInstance().getSession().getStBranch() != null ? false : true;

                /*boolean canNavigateBranch = true;
                boolean canNavigateRegion = true;
                if (form.isOwnerAdm() || form.isOwnerUmum() || form.isOwnerPms()) {
                canNavigateRegion = false;
                canNavigateBranch = false;
                }*/

                System.out.print("@@@@@@@@@@@@@@@@ " + form.isOwnerPms());
                System.out.print("@@@@@@@@@@@@@@@@ " + form.isOwnerUmum());
                System.out.print("@@@@@@@@@@@@@@@@ " + form.isOwnerAdm());
                System.out.print("@@@@@@@@@@@@@@@@ branch" + form.isCanNavigateBranch());
                System.out.print("@@@@@@@@@@@@@@@@ region" + form.isCanNavigateRegion());
                //System.out.print("@@@@@@@@@@@@@@@@ branch " + canNavigateBranch);
                //System.out.print("@@@@@@@@@@@@@@@@ region " + canNavigateRegion);
    %>

    <table cellpadding=2 cellspacing=1>     
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <c:field width="200" caption="{L-ENGBranch-L}{L-INACabang-L}" lov="LOV_Branch" name="stBranch" type="string" readonly="<%=!form.isCanNavigateBranch()%>" presentation="standard" changeaction="refresh" />
                            <%--<c:field show="<%=form.getStBranch() != null%>" width="200" caption="{L-ENGRegion-L}{L-INARegion-L}" lov="LOV_Region" name="stRegion" type="string" readonly="<%=!canNavigateRegion%>" presentation="standard" changeaction="refresh" >
                                <c:lovLink name="cc_code" link="stBranch" clientLink="false"/>
                            </c:field>--%>
                            <c:field show="<%=form.getStBranch() != null%>" lov="LOV_RKAP_UnitKerja" mandatory="false" readonly="<%=!form.isCanNavigateRegion()%>" changeaction="refresh"
                                     width="200" name="stRegion" caption="Unit Kerja" type="string" presentation="standard">
                                <c:lovLink name="param" link="stBranch" clientLink="false"/>
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
                    <%
                                final ARRequestFee req = (ARRequestFee) current;
                    %>
                    <c:listcol name="stARRequestID" title="" selectid="arreqid" />
                    <c:listcol name="stActiveFlag" title="Act" flag="true"/>
                    <c:listcol name="stEffectiveFlag" title="App1" flag="true"/>
                    <c:listcol name="stValidasiF" title="App2" flag="true"/>
                    <c:listcol name="stPrintFlag" title="Prt" flag="true"/>
                    <c:listcol filterable="true" name="stRequestNo" title="No. Pengajuan"/>
                    <c:listcol filterable="true" name="stARRequestID" title="ID"/>
                    <c:listcol name="dbNominalUsed" title="Jumlah" align="right"/>
                    <c:listcol name="stStatus" title="Status"/>
                    <%--<c:listcol title="Jumlah" align="right"><%=JSPUtil.print(req.isStatusApproval() ? req.getDbNominal() : req.isStatusCashback() ? req.getDbNominalUsed() : req.getDbNominalBack(), 2)%></c:listcol>
                    <c:listcol name="stCreateName" title="User Name"/>
                    <c:listcol name="stApprovedName" title="Approved Name"/>--%>
                    <c:listcol name="stApprovedName" title="Keterangan"/>
                </c:listbox>
            </td>
        </tr>
        <tr>
            <td>
                <c:button text="Realisasi Biaya<br>Program Kerja" event="clickCreateRealizedFromProposal" />
                <c:button text="Realisasi<br>Biaya Rutin" event="clickCreateRealized" />
            </td>
        </tr>
        <tr>
            <td>
                <c:button text="Ubah" event="clickEditRealized" />
                <c:button text="Lihat" event="clickViewRealized" />
                <%--<c:button show="<%=form.isCanApprove()%>" text="Setujui" event="clickAppRealized" />--%>
                <c:button show="<%=form.isCanApprove()%>" text="Validasi" event="clickValRealized" />
                <c:button show="<%=form.isOwnerAdm() || form.isOwnerPms() || form.isOwnerUmum()%>" text="Setujui Pemilik" event="clickAppRealisasi" />
            </td>
        </tr>
        <tr>
            <td>
                <c:button show="<%=form.isOwnerAdm() || form.isOwnerPms() || form.isOwnerUmum()%>" text="Persetujuan Direksi" event="clickEditAppProposal" />
                <%--<c:button show="<%=form.isCanApproveDireksi()%>" text="Setujui Direksi" event="clickAppProposalDireksi" />--%>
                <c:button show="<%=form.isCanApproveDireksi()%>" text="Setujui Direksi" event="clickAppRealisasi" />
            </td>
        </tr>
        <%--
        <tr>
            <td>
                <c:button show="<%=form.isCanReverse()%>" text="Reverse" event="clickReverse" />
                <c:button show="<%=form.isCanReprint()%>" text="Cetak Ulang" event="clickRePrint" />
            </td>
        </tr>
        --%>
        <tr>
            <td>
                Print <c:field name="printLang" width="250" type="string" lov="LOV_REQUEST_PRINTING"></c:field> in <c:field name="stLang" type="string" value="<%=LanguageManager.getInstance().getActiveLang()%>" lov="LOV_LANG" />
                <c:button text="Print" name="bprintx" clientEvent="dynPrintClick();" />
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
            frmx.src='x.fpc?EVENT=REQUEST_PRINT&arreqid='+f.arreqid.value+'&alter='+f.printLang.value+'&xlang='+f.stLang.value;
            return;
        }
    }
</script>