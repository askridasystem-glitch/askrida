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

                final ARInvestmentIzinDepositoView izindeposito = form.getIzindeposito();
                final ARInvestmentIzinDepositoDetailView depodetail = form.getDepodetail();

                boolean effectiveCab = izindeposito.isEffectiveCab();
                boolean effectivePus = izindeposito.isEffectivePus();

                boolean admin = true;
                if (SessionManager.getInstance().getSession().getStBranch() != null) {
                    admin = false;
                }

                boolean bpdReadOnly = false;
                String bpd = "Y";
                if (izindeposito.getStReceiptClassID() != null) {
                    if (izindeposito.getStReceiptClassID().equalsIgnoreCase("3")) {
                        bpdReadOnly = true;
                        bpd = "N";
                    }
                }

                String method = "Y";

                int phase = 0;

                if (izindeposito.getStReceiptClassID() != null) {
                    phase = 1;
                }

                if (izindeposito.getStCompanyType() != null) {
                    phase = 2;
                }
    %>

    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:field width="200" lov="LOV_CostCenter" caption="{L-ENGBranch-L}{L-INACabang-L}" name="izindeposito.stCostCenterCode" type="string"
                    mandatory="true" readonly="<%=izindeposito.getStCostCenterCode() != null%>" presentation="standard" changeaction="refreshIzin"/>
                    <c:field lov="LOV_ReceiptClass" width="230" name="izindeposito.stReceiptClassID" mandatory="true" readonly="<%=izindeposito.getStReceiptClassID() != null%>" caption="{L-ENGMethod-L}{L-INAMetode-L}" changeaction="refreshIzin"
                             type="string" presentation="standard" >
                        <c:param name="custcat" value="<%=method%>"/>
                    </c:field>

                    <c:evaluate when="<%=phase >= 1%>">
                        <c:field name="izindeposito.stMonths" type="string" caption="{L-ENGMonth-L}{L-INABulan Transaksi-L}" lov="LOV_MONTH_Period"
                        mandatory="true" readonly="<%=izindeposito.getStMonths() != null%>" presentation="standard" changeaction="refreshIzin"/>
                        <c:field name="izindeposito.stYears" type="string" caption="{L-ENGBulan-L}{L-INATahun Transaksi-L}" lov="LOV_GL_Years2" mandatory="true"
                        readonly="<%=izindeposito.getStYears() != null%>" presentation="standard" changeaction="setDateIzin"/>
                        <c:field caption="Tanggal Mutasi" name="izindeposito.dtMutationDate" type="date" presentation="standard" readonly="true"/>
                        <c:field lov="LOV_CompType" width="230" mandatory="true" name="izindeposito.stCompanyType" caption="{L-ENGCompany Type-L}{L-INAJenis Perusahaan-L}" type="string"  presentation="standard"
                        readonly="<%=izindeposito.getStCompanyType() != null%>" changeaction="refreshIzin" >
                            <c:param name="custcatdep" value="<%=bpd%>"/>
                        </c:field>
                        <c:field width="200" caption="No. Surat" name="izindeposito.stNoSurat" type="string"
                                 readonly="true" presentation="standard"/>
                    </c:evaluate>
                </table>
            </td>
        </tr>

        <c:evaluate when="<%=phase >= 2%>">
            <tr>
                <td colspan=2>
                    <table cellpadding=2 cellspacing=1>
                        <c:tab name="tabdeposito">
                            <c:tabpage name="TAB1">
                                <table cellpadding=2 cellspacing=1>
                                    <tr>
                                        <td>Deposito</td>
                                        <td>:</td>
                                        <td>
                                            <c:field width="400" lov="lovObjects" name="stSelectedObject" caption="Selected" type="string" changeaction="selectObject" overrideRO="true" />
                                            <c:button text="+" event="doNewObject" enabled="<%=!ro%>"  />
                                            <c:button text="-" event="doDeleteObject" enabled="<%=!ro%>"/>
                                        </td>
                                    </tr>
                                    <c:evaluate when="<%=form.getDepodetail() != null%>">
                                        <tr>
                                            <td colspan=3 class=header>
                                                DEPOSITO
                                                <table cellpadding=2 cellspacing=1 class=row0>
                                                    <tr>
                                                        <td>
                                                            <table cellpadding=2 cellspacing=1>
                                                                <c:field width="100" caption="No. Surat" name="depodetail.stNoSurat" type="string" mandatory="true" presentation="standard"/>
                                                                <c:field width="100" caption="No. Bilyet Sementara" name="depodetail.stNodefoSementara" type="string"
                                                                         mandatory="true" presentation="standard"/>
                                                                <c:field width="100" caption="Nominal Deposito" name="depodetail.dbNominalKurs" type="money16.2" mandatory="true" presentation="standard" />
                                                                <c:field width="50" caption="{L-ENG Rate-L}{L-INA Bunga-L}" name="depodetail.dbBunga" type="money16.2" mandatory="true" presentation="standard"/>
                                                                <c:field caption="{L-ENG Bilyet Start Date-L}{L-INA Tanggal Awal Bilyet-L}" name="depodetail.dtTglawal" type="date" presentation="standard" mandatory="true"/>
                                                                <c:field caption="{L-ENG Bilyet End Date-L}{L-INA Tanggal Akhir Bilyet-L}" name="depodetail.dtTglakhir" type="date" presentation="standard" mandatory="true" readonly="true"/>
                                                                <c:field width="100" caption="Type" lov="LOV_OnCall" name="depodetail.stKodedepo" type="string" mandatory="true" presentation="standard" changeaction="onChgCall"/>
                                                                <%
                                                                            boolean OnCall = true;
                                                                            if (depodetail.getStKodedepo() != null) {
                                                                                if (depodetail.getStKodedepo().equalsIgnoreCase("1")) {
                                                                                    OnCall = false;
                                                                                }
                                                                            }
                                                                %>
                                                                <c:field width="50" caption="{L-ENGDays-L}{L-INAHari-L}" name="depodetail.stHari" type="string|3" readonly="<%=OnCall%>" presentation="standard" changeaction="calcDays"/>
                                                                <c:field width="50" caption="{L-ENGMonths-L}{L-INABulan-L}" name="depodetail.stBulan" type="string|2" readonly="<%=!OnCall%>" presentation="standard" changeaction="calcMonths"/>
                                                                <c:field width="100" caption="Unit" lov="LOV_DEPOSITO_UNIT" name="depodetail.stUnit" type="string" mandatory="true" presentation="standard" />
                                                            </table>
                                                        </td>
                                                        <td>
                                                            <table cellpadding=2 cellspacing=1>
                                                                <c:field width="400" lov="LOV_AccountInvestment" popuplov="true" clientchangeaction="selectEntity()"
                                                                         caption="{L-ENGAkun Deposito-L}{L-INAAkun Deposito-L}" name="depodetail.stNoRekeningDeposito" descfield="depodetail.stAccountDepo" type="string" mandatory="true" presentation="standard">
                                                                    <c:param name="param" value="<%="111|" + izindeposito.getStCostCenterCode() + "|" + izindeposito.getStCompanyType()%>"/>
                                                                </c:field>
                                                                <c:field caption="Keterangan" width="400" name="depodetail.stDepoName" type="string" presentation="standard"/>

                                                                <c:field width="400" caption="Sumber Dana" lov="LOV_DEPOSUMBER" name="depodetail.stSumberID" type="string" mandatory="true" presentation="standard" changeaction="refreshIzin"/>

                                                                <c:evaluate when="<%=depodetail.getStSumberID() != null%>">
                                                                    <c:evaluate when="<%=depodetail.isDebet()%>">
                                                                        <c:field width="400" lov="LOV_AccountInvestment" popuplov="true" clientchangeaction="selectEntity2()"
                                                                                 caption="{L-ENGAkun Bank-L}{L-INAAkun Bank-L}" name="depodetail.stEntityID" descfield="depodetail.stAccountBank" type="string" mandatory="true" presentation="standard">
                                                                            <c:param name="param" value="<%="122|" + izindeposito.getStCostCenterCode() + "|" + izindeposito.getStCompanyType()%>"/>
                                                                        </c:field>
                                                                        <c:field caption="Nama Bank" width="400" name="depodetail.stBankName" type="string" presentation="standard"/>
                                                                    </c:evaluate>

                                                                    <c:evaluate when="<%=!depodetail.isDebet()%>">
                                                                        <c:field width="400" lov="LOV_AccountInvestment" popuplov="true" clientchangeaction="selectEntity3()"
                                                                                 caption="Akun Bank (Asal)" name="depodetail.stSumberBank" descfield="depodetail.stAccountSumberBank" type="string" mandatory="true" presentation="standard">
                                                                            <c:param name="param" value="<%="122|" + izindeposito.getStCostCenterCode() + "|" + izindeposito.getStCompanyType()%>"/>
                                                                        </c:field>
                                                                        <c:field caption="Nama Bank" width="400" name="depodetail.stNamaSumberBank" type="string" presentation="standard"/>

                                                                        <c:field width="400" caption="Rekening Tujuan" lov="LOV_DEPOTUJUAN" name="depodetail.stTujuanID" type="string" mandatory="true" presentation="standard" changeaction="refreshIzin"/>

                                                                        <c:evaluate when="<%=depodetail.getStTujuanID() != null%>">
                                                                            <c:evaluate when="<%=depodetail.isAskrida()%>">
                                                                                <c:field width="400" lov="LOV_AccountInvestment" popuplov="true" clientchangeaction="selectEntity2()"
                                                                                         caption="Akun Bank (Tujuan)" name="depodetail.stEntityID" descfield="depodetail.stAccountBank" type="string" mandatory="true" presentation="standard">
                                                                                    <c:param name="param" value="<%="122|" + izindeposito.getStCostCenterCode() + "|" + izindeposito.getStCompanyType()%>"/>
                                                                                </c:field>
                                                                                <c:field caption="Nama Bank" width="400" name="depodetail.stBankName" type="string" presentation="standard"/>
                                                                                <c:field caption="Atas Nama" width="400" name="depodetail.stAtasNama" type="string" presentation="standard"/>
                                                                            </c:evaluate>

                                                                            <c:evaluate when="<%=!depodetail.isAskrida()%>">
                                                                                <c:field caption="No.Rekeking" width="400" name="depodetail.stAccountBank" type="string" presentation="standard"/>
                                                                                <c:field caption="Nama Bank" width="400" name="depodetail.stBankName" type="string" presentation="standard"/>
                                                                                <c:field caption="Atas Nama" width="400" name="depodetail.stAtasNama" type="string" presentation="standard"/>
                                                                            </c:evaluate>
                                                                        </c:evaluate>
                                                                    </c:evaluate>
                                                                </c:evaluate>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                </table>
                            </c:tabpage>
                        </c:tab>
                    </table>
                </td>
            </tr>
            <tr>
                <td align=center>
                    <c:evaluate when="<%=form.isEditMode()%>" >
                        <c:button text="{L-ENGSave-L}{L-INASimpan-L}" event="saveIzin" confirm="Yakin Mau Disimpan ?" validate="true"/>
                        <c:button text="{L-ENGCancel-L}{L-INABatal-L}" event="close" confirm="Yakin Mau Dibatalkan ?" validate="false"/>
                    </c:evaluate>
                    <c:evaluate when="<%=form.isApprovedMode()%>">
                        <c:button show="true" text="{L-ENGApprove-L}{L-INASetujui-L}" event="approveIzin" validate="true" confirm="Yakin Mau Disetujui ?"/>
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
    function selectEntity() {
        var o = window.lovPopResult;
        document.getElementById('depodetail.stDepoName').value = o.description;
    }

    function selectEntity2() {
        var o = window.lovPopResult;
        document.getElementById('depodetail.stBankName').value = o.description;
    }

    function selectEntity3() {
        var o = window.lovPopResult;
        document.getElementById('depodetail.stNamaSumberBank').value = o.description;
    }
</script>