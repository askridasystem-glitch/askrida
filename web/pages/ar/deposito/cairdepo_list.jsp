<%@ page import="com.webfin.ar.model.ARInvestmentPencairanView,
         com.crux.lov.LOVManager,
         com.crux.util.LOV,
         com.crux.lang.LanguageManager,
         com.crux.web.controller.SessionManager,
         com.webfin.ar.forms.PencairanList"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="PENCAIRAN DEPOSITO" >

    <%
                final PencairanList form = (PencairanList) frame.getForm();

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
                        <td>{L-ENGPencairan Date-L}{L-INATanggal Pencairan-L}</td>
                        <td>:</td>
                        <td>
                            <c:field name="dtCairDateFrom" caption="Pencairan Date From" type="date" />
                            {L-ENGTo-L}{L-INAS/D-L} <c:field name="dtCairDateTo" caption="Pencairan Date To" type="date" />
                        </td>
                    </tr>
                    --%>
                    <tr>
                        <td>
                            <c:field caption="{L-ENGShow active &amp; Pending only-L}{L-INAPencairan Aktif &amp; Pending-L} " name="stActiveFlag" type="check" presentation="standard" changeaction="refresh" />
                            <c:field caption="Pencairan Belum Direalisasi" name="stEffectiveFlag" type="check" presentation="standard" changeaction="refresh" />
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
                <c:listbox name="cairlist" autofilter="true" paging="true" selectable="true" view="com.webfin.ar.model.ARInvestmentPencairanView" >
                    <c:listcol name="stARCairID" title="" selectid="arcairid" />
                    <c:listcol name="stActiveFlag" title="Act" flag="true"/>
                    <c:listcol name="stEffectiveFlag" title="Eff" flag="true"/>
                    <c:listcol filterable="true" name="stNodefo" title="No. Bilyet"/>
                    <c:listcol filterable="true" name="stNoRekening" title="No. Rekening Bilyet"/>
                    <c:listcol filterable="true" name="stBuktiC" title="No. Bukti Pencairan"/>
                    <c:listcol filterable="true" name="stBuktiB" title="No. Bukti Pembentukan"/>
                    <c:listcol name="dtTglCair" title="Tanggal Cair" />
                    <c:listcol name="dbNominal" title="Nominal" />
                    <c:listcol name="stRealisasiNobuk" title="Realisasi Pemb." />
                </c:listbox>
            </td>
        </tr>

        <tr>
            <td>
                <c:button text="Realisasi" event="clickCreateIzin" />
                <c:button text="{L-ENGEdit-L}{L-INAUbah-L}" event="clickEditIzin" />
                <c:button text="{L-ENGView-L}{L-INALihat-L}" event="clickView" />
            </td>
        </tr>
        <tr>
            <td>
                <c:button show="<%=form.isCanApprove()%>" text="{L-ENGApproval-L}{L-INASetujui-L}" event="clickApproval" />
                <c:button text="Pembayaran" event="clickRealisasiIzin" />
            </td>
        </tr>
        <%--
        <tr>
            <td>
                <c:evaluate when="<%=admin%>" >
                    <c:button text="{L-ENGCreate-L}{L-INABuat Pencairan Tanpa Jurnal-L}" event="clickCreateTanpaJurnal" validate="true"/>
                </c:evaluate>
            </td>
        </tr>
        <tr>
            <td>
                <c:button show="<%=form.isCanApprove()%>" text="{L-ENGApproval-L}{L-INASetujui-L}" event="clickApproval" />
                <c:button show="<%=form.isCanApprove()%>" text="Reverse" event="clickReverse" />
            </td>
        </tr>
        --%>

    </table>

</c:frame>