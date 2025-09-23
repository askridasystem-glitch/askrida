<%@ page import="com.webfin.ar.model.*,
         com.crux.lov.LOVManager,
         com.crux.util.*,
         com.crux.lang.LanguageManager,
         com.crux.web.controller.SessionManager,
         com.webfin.ar.forms.PengajuanIzinForm"%>
<%@ taglib prefix="c" uri="crux" %>

<c:frame>
    <%
                PengajuanIzinForm form = (PengajuanIzinForm) request.getAttribute("FORM");

                boolean ro = form.isReadOnly();

                final ARInvestmentIzinPencairanView izinpencairan = form.getIzinpencairan();

                boolean effectiveCab = izinpencairan.isEffectiveCab();
                boolean effectivePus = izinpencairan.isEffectivePus();

                boolean admin = true;
                if (SessionManager.getInstance().getSession().getStBranch() != null) {
                    admin = false;
                }

                int phase = 0;

                if (izinpencairan.getStCostCenterCode() != null) {
                    phase = 1;
                }

                if (izinpencairan.getStYears() != null) {
                    phase = 2;
                }
    %>

    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:field width="200" lov="LOV_CostCenter" caption="{L-ENGBranch-L}{L-INACabang-L}" name="izinpencairan.stCostCenterCode" type="string"
                    mandatory="true" readonly="<%=izinpencairan.getStCostCenterCode() != null%>" presentation="standard" changeaction="refreshIzin"/>

                    <c:evaluate when="<%=phase >= 1%>">
                        <c:field name="izinpencairan.stMonths" type="string" caption="{L-ENGMonth-L}{L-INABulan Transaksi-L}" lov="LOV_MONTH_Period"
                        mandatory="true" readonly="<%=izinpencairan.getStMonths() != null%>" presentation="standard" changeaction="refreshIzin"/>
                        <c:field name="izinpencairan.stYears" type="string" caption="{L-ENGBulan-L}{L-INATahun Transaksi-L}" lov="LOV_GL_Years2" mandatory="true"
                        readonly="<%=izinpencairan.getStYears() != null%>" presentation="standard" changeaction="setDateIzinCair"/>
                        <c:field caption="Tanggal Mutasi" name="izinpencairan.dtMutationDate" type="date" presentation="standard" readonly="true"/>
                        <c:field width="200" caption="No. Surat" name="izinpencairan.stNoSurat" type="string"
                                 readonly="true" presentation="standard"/>
                    </c:evaluate>
                </table>
            </td>
        </tr>

        <c:evaluate when="<%=phase >= 2%>">
            <tr>
                <td colspan=2>
                    <c:field name="notesindex" type="string" hidden="true"/>
                    <c:listbox name="pencairandet">
                        <%
                                    final ARInvestmentIzinPencairanDetView pencairandet = (ARInvestmentIzinPencairanDetView) form.getPencairandet().get(index.intValue());

                                    final boolean expanded = pencairandet != null && pencairandet.getStJenisCair().equalsIgnoreCase("1");
                        %>
                        <c:listcol title="" columnClass="header">
                            <c:button text="+" event="onNewCairDet" validate="false" defaultRO="true"/>
                        </c:listcol>
                        <c:listcol title="" columnClass="detail">
                            <c:button text="-" event="onDeleteCairDet" clientEvent="f.notesindex.value='$index$';"
                                      validate="false" defaultRO="true"/>
                        </c:listcol>

                        <c:listcol title=""><%=index.intValue() + 1%>
                        </c:listcol>

                        <c:listcol title="No Bukti Bentuk" >
                            <c:field width="150" lov="LOV_DepositoIzin" popuplov="true" clientchangeaction="selectEntity3($index$)"
                                     caption="No.Bentuk" name="pencairandet.[$index$].stARDepoID" descfield="pencairandet.[$index$].stBuktiB" type="string" mandatory="true">
                                <c:param name="param" value="<%=izinpencairan.getStCostCenterCode()%>"/>
                            </c:field>
                        </c:listcol>
                        <c:listcol title="Nodefo" >
                            <c:field name="pencairandet.[$index$].stNodefo" width="100" mandatory="true" caption="Nodefo" type="string"/>
                        </c:listcol>
                        <c:listcol title="Bilyet" >
                            <c:field name="pencairandet.[$index$].dbBilyetAmount" width="100" mandatory="true" caption="Bilyet" type="money16.2"/>
                        </c:listcol>
                        <c:listcol title="Jenis Realisasi" >
                            <c:field name="pencairandet.[$index$].stJenisCair" width="100" caption="Realisasi" type="string" lov="LOV_IZIN_PENCAIRAN" changeaction="refreshIzin"/>
                        </c:listcol>
                        <c:listcol title="Klaim" >
                            <c:evaluate when="<%=expanded%>">
                                <c:field width="150" lov="LOV_CLAIM2" popuplov="true"  clientchangeaction="selectEntity4($index$)"
                                         caption="No.Bentuk" name="pencairandet.[$index$].stPolicyID" descfield="pencairandet.[$index$].stDLANo" type="string">
                                    <c:param name="param" value="<%=izinpencairan.getStCostCenterCode()%>"/>
                                </c:field>
                            </c:evaluate>
                        </c:listcol>
                        <c:listcol title="Untuk Realisasi" >
                            <c:field name="pencairandet.[$index$].stPencairanKet" width="200" mandatory="true" caption="Keterangan" type="string"/>
                        </c:listcol>
                        <c:listcol title="Nilai" >
                            <c:field name="pencairandet.[$index$].dbNilai" width="100" mandatory="true" caption="Keterangan" type="money16.2"/>
                        </c:listcol>
                    </c:listbox>
                </td>
            </tr>
            <tr>
                <td align=center>
                    <c:evaluate when="<%=form.isEditMode()%>" >
                        <c:button text="{L-ENGSave-L}{L-INASimpan-L}" event="saveIzinCair" confirm="Yakin Mau Disimpan ?" validate="true"/>
                        <c:button text="{L-ENGCancel-L}{L-INABatal-L}" event="close" confirm="Yakin Mau Dibatalkan ?" validate="false"/>
                    </c:evaluate>
                    <c:evaluate when="<%=form.isApprovedMode()%>">
                        <c:button show="true" text="{L-ENGApprove-L}{L-INASetujui-L}" event="approveIzinCair" validate="true"
                                  confirm="Yakin Mau Disetujui ?"/>
                        <c:button text="{L-ENGClose-L}{L-INATutup-L}" event="close" validate="false"/>
                    </c:evaluate>
                    <c:evaluate when="<%=form.isViewMode()%>">
                        <c:button text="{L-ENGClose-L}{L-INATutup-L}" event="close" validate="false"/>
                    </c:evaluate>
                </td>
            </tr>
        </c:evaluate>
    </table>
</c:frame>

<script>
    function selectEntity3(i) {
        var o = window.lovPopResult;
        document.getElementById('pencairandet.[' + i + '].stNodefo').value = o.nodefo;
        document.getElementById('pencairandet.[' + i + '].dbBilyetAmount').value = o.jumlah;
    }

    function selectEntity4(i) {
        var o = window.lovPopResult;
        document.getElementById('pencairandet.[' + i + '].stPencairanKet').value = o.dla_no;
        document.getElementById('pencairandet.[' + i + '].dbNilai').value = o.jumlah;
    }
</script>