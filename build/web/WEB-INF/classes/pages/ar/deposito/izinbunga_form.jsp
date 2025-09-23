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

                final ARInvestmentIzinBungaView izinbunga = form.getIzinbunga();

                boolean effectiveCab = izinbunga.isEffectiveCab();
                boolean effectivePus = izinbunga.isEffectivePus();

                boolean admin = true;
                if (SessionManager.getInstance().getSession().getStBranch() != null) {
                    admin = false;
                }

                String method = "Y";

                int phase = 0;

                if (izinbunga.getStCostCenterCode() != null) {
                    phase = 1;
                }

                if (izinbunga.getStYears() != null) {
                    phase = 2;
                }
    %>

    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:field width="200" lov="LOV_CostCenter" caption="{L-ENGBranch-L}{L-INACabang-L}" name="izinbunga.stCostCenterCode" type="string"
                    mandatory="true" readonly="<%=izinbunga.getStCostCenterCode() != null%>" presentation="standard" changeaction="refreshIzin"/>
                    <%--<c:field lov="LOV_ReceiptClass" width="230" name="izinbunga.stReceiptClassID" mandatory="true" readonly="<%=izinbunga.getStReceiptClassID() != null%>"
                             caption="{L-ENGMethod-L}{L-INAMetode-L}" changeaction="refreshIzin" type="string" presentation="standard" >
                        <c:param name="custcat" value="<%=method%>"/>
                    </c:field>--%>

                    <c:evaluate when="<%=phase >= 1%>">
                        <c:field name="izinbunga.stMonths" type="string" caption="{L-ENGMonth-L}{L-INABulan Transaksi-L}" lov="LOV_MONTH_Period"
                        mandatory="true" readonly="<%=izinbunga.getStMonths() != null%>" presentation="standard" changeaction="refreshIzin"/>
                        <c:field name="izinbunga.stYears" type="string" caption="{L-ENGBulan-L}{L-INATahun Transaksi-L}" lov="LOV_GL_Years2" mandatory="true"
                        readonly="<%=izinbunga.getStYears() != null%>" presentation="standard" changeaction="setDateIzinBunga"/>
                        <c:field caption="Tanggal Mutasi" name="izinbunga.dtMutationDate" type="date" presentation="standard" mandatory="true"/>
                        <%--<c:field lov="LOV_CompType" width="230" mandatory="true" name="izinbunga.stCompanyType" caption="{L-ENGCompany Type-L}{L-INAJenis Perusahaan-L}"
                                 type="string"  presentation="standard" readonly="<%=izinbunga.getStCompanyType() != null%>" changeaction="refreshIzin" >
                            <c:param name="custcatdep" value="<%=bpd%>"/>
                        </c:field>--%>
                        <c:field width="200" caption="No. Surat" name="izinbunga.stNoSurat" type="string"
                                 readonly="true" presentation="standard"/>
                    </c:evaluate>
                </table>
            </td>
        </tr>

        <c:evaluate when="<%=phase >= 2%>">
            <tr>
                <td colspan=2>
                    <c:field name="notesindex" type="string" hidden="true"/>
                    <c:listbox name="bungadet">
                        <c:listcol title="" columnClass="header">
                            <c:button text="+" event="onNewBungaDet" validate="false" defaultRO="true"/>
                        </c:listcol>
                        <c:listcol title="" columnClass="detail">
                            <c:button text="-" event="onDeleteBungaDet" clientEvent="f.notesindex.value='$index$';"
                                      validate="false" defaultRO="true"/>
                        </c:listcol>

                        <c:listcol title=""><%=index.intValue() + 1%>
                        </c:listcol>

                        <c:listcol title="No Bukti Bentuk" >
                            <c:field width="140" lov="LOV_Perpanjangan" popuplov="true" clientchangeaction="selectEntity3($index$)"
                                     caption="No.Bentuk" name="bungadet.[$index$].stARDepoID" descfield="bungadet.[$index$].stNoBuktiB" type="string" mandatory="false">
                                <c:param name="param" value="<%=izinbunga.getStCostCenterCode()%>"/>
                            </c:field>
                        </c:listcol>
                        <c:listcol title="Akun Deposito" >
                            <c:field width="100" lov="LOV_AccountInvestment" popuplov="true" caption="Akun Deposito" clientchangeaction="selectEntity($index$)"
                                     name="bungadet.[$index$].stNoRekeningDeposito" descfield="bungadet.[$index$].stAccountDepo" type="string" mandatory="true">
                                <c:param name="param" value="<%="111|" + izinbunga.getStCostCenterCode() + "|A"%>"/>
                            </c:field>
                        </c:listcol>
                        <c:listcol title="Bilyet" >
                            <c:field name="bungadet.[$index$].stNodefo" width="100" mandatory="true" caption="Nodefo" type="string"/>
                        </c:listcol>
                        <c:listcol title="Nominal" >
                            <c:field name="bungadet.[$index$].dbNominal" width="100" mandatory="true" caption="Nominal" type="money16.2"/>
                        </c:listcol>
                        <c:listcol title="Akun Spec.Budep" >
                            <c:field width="100" lov="LOV_AccountInvestment" popuplov="true" caption="Akun Bank" clientchangeaction="selectEntity2($index$)"
                                     name="bungadet.[$index$].stEntityID" descfield="bungadet.[$index$].stAccountBank" type="string" mandatory="true">
                                <c:param name="param" value="<%="122|" + izinbunga.getStCostCenterCode() + "|A"%>"/>
                            </c:field>
                        </c:listcol>
                        <c:listcol title="Nama Bank" >
                            <c:field name="bungadet.[$index$].stBankName" width="200" mandatory="true" caption="Nama Bank" type="string"/>
                        </c:listcol>
                        <c:listcol title="Tgl Bunga" >
                            <c:field name="bungadet.[$index$].dtTglBunga" mandatory="true" caption="Tanggal Bunga" type="date"/>
                        </c:listcol>
                        <c:listcol title="Bunga" >
                            <c:field name="bungadet.[$index$].dbAngka" width="100" mandatory="false" caption="Bunga" type="money16.2"/>
                        </c:listcol>
                        <c:listcol title="No Bukti Bunga" >
                            <c:field name="bungadet.[$index$].stNoBuktiD" width="140" readonly="true" caption="Nobuk Bunga" type="string"/>
                        </c:listcol>
                    </c:listbox>
                </td>
            </tr>
            <tr>
                <td align=center>
                    <c:evaluate when="<%=form.isEditMode()%>" >
                        <c:button text="Retrieve" event="retrieveBunga"/>
                        <c:button text="Del All" event="onDeleteBungaAll"/>
                        <c:button text="{L-ENGSave-L}{L-INASimpan-L}" event="saveIzinBunga" confirm="Yakin Mau Disimpan ?" validate="true"/>
                        <c:button text="{L-ENGCancel-L}{L-INABatal-L}" event="close" confirm="Yakin Mau Dibatalkan ?" validate="false"/>
                    </c:evaluate>
                    <c:evaluate when="<%=form.isApprovedMode()%>">
                        <c:button show="true" text="{L-ENGApprove-L}{L-INASetujui-L}" event="approveIzinBunga" validate="true"
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
    function selectEntity(i) {
        var o = window.lovPopResult;
        document.getElementById('bungadet.[' + i + '].stDepoName').value = o.description;
    }

    function selectEntity2(i) {
        var o = window.lovPopResult;
        document.getElementById('bungadet.[' + i + '].stBankName').value = o.description;
    }
    
    function selectEntity3(i) {
        var o = window.lovPopResult;
        document.getElementById('bungadet.[' + i + '].stNodefo').value = o.nodefo;
        document.getElementById('bungadet.[' + i + '].dbNominal').value = o.jumlah;
        document.getElementById('bungadet.[' + i + '].dbPersen').value = o.persen;
    }
</script>