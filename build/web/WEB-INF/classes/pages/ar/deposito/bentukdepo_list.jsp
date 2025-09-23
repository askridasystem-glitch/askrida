<%@ page import="com.webfin.ar.model.ARInvestmentDepositoView,
         com.crux.lov.LOVManager,
         com.crux.util.LOV,
         com.crux.web.controller.SessionManager,
         com.crux.lang.LanguageManager,
         com.webfin.ar.forms.PembentukanList"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="PEMBENTUKAN DEPOSITO" >

    <%
                final PembentukanList form = (PembentukanList) frame.getForm();

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
                    <%--
                    <tr>
                        <td>{L-ENGDeposito Date-L}{L-INATanggal Pendebetan-L}</td>
                        <td>:</td>
                        <td>
                            <c:field name="dtDepoDateFrom" caption="Deposito Date From" type="date" />
                            {L-ENGTo-L}{L-INAS/D-L} <c:field name="dtDepoDateTo" caption="Deposito Date To" type="date" />
                        </td>
                    </tr>
                    <tr>
                        <td>{L-ENGEnd Date Bilyet-L}{L-INATanggal Akhir Bilyet-L}</td>
                        <td>:</td>
                        <td>
                            <c:field name="dtEndDate" caption="End Date" type="date" />
                        </td>
                    </tr>--%>
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
                <c:listbox name="bentuklist" autofilter="true" paging="true" selectable="true" view="com.webfin.ar.model.ARInvestmentDepositoView" >
                    <%
                                ARInvestmentDepositoView lastjv = new ARInvestmentDepositoView();
                                final boolean isUpload = lastjv != null && lastjv.getStUpload() != null;
                    %>
                    <c:listcol name="stARDepoID" title="" selectid="ardepoid" />
                    <c:listcol name="stActiveFlag" title="Act" flag="true"/>
                    <c:listcol name="stEffectiveFlag" title="Eff" flag="true"/>
                    <c:listcol filterable="true" name="stNodefo" title="No. Bilyet"/>
                    <c:listcol filterable="true" name="stNoRekening" title="No. Rekening Bilyet"/>
                    <c:listcol filterable="true" name="stBuktiB" title="No. Bukti Pembentukan"/>
                    <c:listcol name="dtTglawal" title="Tanggal Awal" />
                    <c:listcol name="dtTglakhir" title="Tanggal Akhir" />
                    <c:listcol name="dbNominal" title="Nominal" />
                    <c:listcol name="stUpload" title="Bukti" flag="<%=isUpload%>" />
                </c:listbox>
            </td>
        </tr>

        <tr>
            <td>
                <%--
                <c:button text="{L-ENGCreate-L}{L-INABuat Pembentukan-L}" event="clickCreate"/>
                <c:button show="<%=form.isCanApprove()%>" text="{L-ENGRenewal-L}{L-INAPerpanjangan-L}" event="createRenewal" />--%>
                <c:button text="Realisasi" event="clickCreateIzin" />
                <c:button text="Ubah" event="clickEditIzin" />
                <c:button text="{L-ENGView-L}{L-INALihat-L}" event="clickViewIzin" />
            </td>
        </tr>
        <tr>
            <td>
                <c:button text="Input Bukti" event="clickInputIzinTiket" />
                <c:button show="<%=form.isCanApprove()%>" text="{L-ENGApproval-L}{L-INASetujui-L}" event="clickApproval" />
            </td>
        </tr>
    </table>

</c:frame>