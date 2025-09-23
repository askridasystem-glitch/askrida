<%@ page import="com.webfin.ar.model.ARInvestmentDepositoView,
         com.crux.lov.LOVManager,
         com.webfin.gl.ejb.CurrencyManager,
         com.crux.util.LOV,
         com.crux.util.*,
         com.crux.lang.LanguageManager,
         com.crux.web.controller.SessionManager,
         com.webfin.ar.model.ARReceiptClassView,
         com.webfin.ar.forms.PembentukanForm"%>
<%@ taglib prefix="c" uri="crux" %>

<c:frame>
    <%
                PembentukanForm form = (PembentukanForm) request.getAttribute("FORM");

                boolean ro = form.isReadOnly();

                final ARInvestmentDepositoView deposito = form.getDeposito();

                boolean effective = deposito.isEffective();

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
                            <table cellpadding=2 cellspacing=1>
                                <%--<c:field name="deposito.stARDepoID" caption="Deposito ID" type="integer" readonly="true" presentation="standard" flags="auto3"/>--%>
                                <c:field width="200" lov="LOV_CostCenter" caption="{L-ENGBranch-L}{L-INACabang-L}" name="deposito.stCostCenterCode" type="string"
                                         readonly="true" presentation="standard" changeaction="refresh"/>
                                <c:field width="230" lov="LOV_ReceiptClass" name="deposito.stReceiptClassID"
                                         readonly="true" caption="{L-ENGMethod-L}{L-INAMetode-L}" changeaction="refresh" type="string" presentation="standard" />
                                <c:field name="deposito.stMonths" type="string" caption="{L-ENGMonth-L}{L-INABulan Transaksi-L}" lov="LOV_MONTH_Period" readonly="true" presentation="standard" changeaction="selectMonth"/>
                                <c:field name="deposito.stYears" type="string" caption="{L-ENGBulan-L}{L-INATahun Transaksi-L}" lov="LOV_GL_Years2" readonly="true" presentation="standard" changeaction="setDate"/>
                                <c:field width="200" caption="{L-ENGNo. Bukti-L}{L-INANo. Bukti-L}" name="deposito.stBuktiB" type="string"
                                         readonly="true" presentation="standard"/>
                                <c:field width="200" caption="{L-ENGNo. Bilyet-L}{L-INANo. Bilyet-L}" name="deposito.stNodefo" type="string" presentation="standard"/>
                                <c:field width="200" caption="{L-ENGNo. Rekening Bilyet-L}{L-INANo. Rekening Bilyet-L}" name="deposito.stNoRekening" type="string" presentation="standard"/>
                                <c:field width="230" lov="LOV_CompType" name="deposito.stCompanyType" caption="{L-ENGCompany Type-L}{L-INAJenis Perusahaan-L}" type="string" presentation="standard" readonly="true" changeaction="refresh" />
                                <c:field width="200" caption="{L-ENGAkun Deposito-L}{L-INAAkun Deposito-L}" name="deposito.stAccountDepo" readonly="true" type="string" presentation="standard"/>
                                <c:field caption="Keterangan" width="200" name="deposito.stDepoName" type="string" readonly="true" presentation="standard"/>
                                <c:field width="200" caption="{L-ENGAkun Bank-L}{L-INAAkun Bank-L}" name="deposito.stAccountBank" readonly="true" type="string" presentation="standard"/>
                                <c:field caption="Nama Bank" width="200" name="deposito.stBankName" type="string" readonly="true" presentation="standard"/>
                                <c:field caption="{L-ENGCurrency-L}{L-INAMata Uang-L}" name="deposito.stCurrency" type="string" readonly="true" presentation="standard"/>
                                <c:field caption="Kurs" name="deposito.dbCurrencyRate" type="money16.2" readonly="true" presentation="standard"/>
                            </table>
                        </td>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <tr>
                                    <c:field caption="{L-ENG Bilyet Start Date-L}{L-INA Tanggal Awal Bilyet-L}" name="deposito.dtTglawal" type="date" presentation="standard" readonly="true"/>
                                    <c:field caption="{L-ENG Bilyet End Date-L}{L-INA Tanggal Akhir Bilyet-L}" name="deposito.dtTglakhir" type="date" presentation="standard" readonly="true"/>
                                    <c:field width="100" caption="Type" lov="LOV_OnCall" name="deposito.stKodedepo" type="string" readonly="true" presentation="standard" changeaction="onChgCall"/>
                                    <c:field width="50" caption="{L-ENGDays-L}{L-INAHari-L}" name="deposito.stHari" type="string|3" readonly="true" presentation="standard" changeaction="calcDays"/>
                                    <c:field width="50" caption="{L-ENGMonths-L}{L-INABulan-L}" name="deposito.stBulan" type="string|2" readonly="true" presentation="standard" changeaction="calcMonths"/>
                                    <c:field caption="{L-ENG Debet Date-L}{L-INA Tanggal Pendebetan-L}" name="deposito.dtTgldepo" type="date" readonly="true" presentation="standard" />
                                    <c:field caption="{L-ENG Mutate Date-L}{L-INA Neraca per Tanggal-L}" name="deposito.dtTglmuta" type="date" readonly="true" presentation="standard" />
                                    <c:field width="50" caption="{L-ENG Rate-L}{L-INA Bunga-L}" name="deposito.dbBunga" type="money16.2" readonly="true" presentation="standard"/>
                                    <c:field width="50" caption="{L-ENG Tax-L}{L-INA Pajak-L}" name="deposito.dbPajak" type="money16.2" readonly="true" readonly="true" presentation="standard"/>
                                    <c:field width="150" caption="Deposito" name="deposito.dbNominalKurs" type="money16.2" readonly="true" presentation="standard" changeaction="refresh"/>
                                    <c:field width="150" caption="Deposito (IDR)" name="deposito.dbNominal" type="money16.2" readonly="true" presentation="standard" readonly="true" changeaction="generateDBNominal"/>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>

            </td>
        </tr>
        <tr>
            <td colspan=2>
                <table cellpadding=2 cellspacing=1>
                    <c:field width="550" rows="2" caption="{L-ENGDescription-L}{L-INAKeterangan-L}" readonly="true"
                             name="deposito.stKeterangan" type="string" mandatory="false" presentation="standard"/>
                </table>
            </td>
        </tr>
        <tr>
            <td align=center>
                <c:evaluate when="<%=!ro%>" >
                    <c:button text="Ubah Bilyet Rekening" event="editBilyet" confirm="Yakin Mau Disimpan ?" validate="true"/>
                    <c:button text="{L-ENGCancel-L}{L-INABatal-L}" event="close" confirm="Yakin Mau Dibatalkan ?" validate="false"/>
                </c:evaluate>
                <c:evaluate when="<%=ro%>" >
                    <c:button text="{L-ENGClose-L}{L-INATutup-L}" event="close" validate="false"/>
                </c:evaluate>
            </td>
        </tr>
    </table>
</c:frame>

<script>
    function selectEntity() {
        var o = window.lovPopResult;
        document.getElementById('deposito.stDepoName').value = o.description;
    }

    function selectEntity2() {
        var o = window.lovPopResult;
        document.getElementById('deposito.stBankName').value = o.description;
    }
</script>