<%@ page import="com.webfin.ar.model.ARInvestmentDepositoView,
         com.webfin.ar.model.ARInvestmentBungaView,
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

                final boolean ismasterCurrency = CurrencyManager.getInstance().isMasterCurrency(deposito.getStCurrency());

                boolean effective = deposito.isEffective();

                boolean admin = true;
                if (SessionManager.getInstance().getSession().getStBranch() != null) {
                    admin = false;
                }

                boolean OnCall = true;
                if (deposito.getStKodedepo() != null) {
                    if (deposito.getStKodedepo().equalsIgnoreCase("1")) {
                        OnCall = false;
                    }
                }

                boolean bpdReadOnly = false;
                String bpd = "Y";
                if (deposito.getStReceiptClassID() != null) {
                    if (deposito.getStReceiptClassID().equalsIgnoreCase("3")) {
                        bpdReadOnly = true;
                        bpd = "N";
                    }
                }

                String method = "Y";

                int phase = 0;

                if (deposito.getStCostCenterCode() != null) {
                    phase = 1;
                }
                if (deposito.getStReceiptClassID() != null) {
                    phase = 2;
                }

                if (phase == 2 && deposito.getStReceiptClassID() != null) {
                    phase = 3;
                }

                if (phase == 2 && deposito.getStCostCenterCode() != null) {
                    phase = 3;
                }
    %>

    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field width="200" lov="LOV_CostCenter"
                                         caption="{L-ENGBranch-L}{L-INACabang-L}" name="deposito.stCostCenterCode" type="string"
                                mandatory="true" readonly="<%=deposito.getStCostCenterCode() != null%>" presentation="standard" changeaction="refresh"/>
                                <c:field lov="LOV_ReceiptClass" width="230" name="deposito.stReceiptClassID" mandatory="true" readonly="<%=deposito.getStReceiptClassID() != null%>" caption="{L-ENGMethod-L}{L-INAMetode-L}" changeaction="refresh"
                                         type="string" presentation="standard" >
                                    <c:param name="custcat" value="<%=method%>"/>
                                </c:field>
                                <c:evaluate when="<%=phase >= 3%>">
                                    <c:field name="deposito.stMonths" type="string" caption="{L-ENGMonth-L}{L-INABulan Transaksi-L}" lov="LOV_MONTH_Period" mandatory="true" presentation="standard" changeaction="selectMonth" readonly="true"/>
                                    <c:field name="deposito.stYears" type="string" caption="{L-ENGBulan-L}{L-INATahun Transaksi-L}" lov="LOV_GL_Years2" mandatory="true" presentation="standard" changeaction="setDate" readonly="true"/>
                                    <c:field width="200" caption="{L-ENGNo. Bukti-L}{L-INANo. Bukti-L}" name="deposito.stBuktiB" type="string"
                                             readonly="true" presentation="standard" readonly="true"/>
                                    <c:field width="200" caption="{L-ENGNo. Bilyet-L}{L-INANo. Bilyet-L}" name="deposito.stNodefo" type="string"
                                             mandatory="true" presentation="standard" readonly="true"/>
                                    <c:field width="200" caption="{L-ENGNo. Rekening Bilyet-L}{L-INANo. Rekening Bilyet (Sambung)-L}" name="deposito.stNoRekening" type="string" 
                                             mandatory="true" presentation="standard" readonly="true"/>
                                    <c:field lov="LOV_CompType" width="230" mandatory="true" name="deposito.stCompanyType" caption="{L-ENGCompany Type-L}{L-INAJenis Perusahaan-L}" type="string"  presentation="standard"
                                    readonly="<%=deposito.getStCompanyType() != null%>" changeaction="refresh" >
                                        <c:param name="custcatdep" value="<%=bpd%>"/>
                                    </c:field>
                                    <c:field width="200" lov="LOV_AccountInvestment" popuplov="true" clientchangeaction="selectEntity()" readonly="true"
                                             caption="{L-ENGAkun Deposito-L}{L-INAAkun Deposito-L}" name="deposito.stNoRekeningDeposito" descfield="deposito.stAccountDepo" type="string" mandatory="true" presentation="standard">
                                        <c:param name="param" value="<%="111|" + deposito.getStCostCenterCode() + "|" + deposito.getStCompanyType()%>"/>
                                    </c:field>
                                    <c:field caption="Keterangan" width="200" name="deposito.stDepoName" type="string" presentation="standard" readonly="true"/>
                                    <c:field width="200" lov="LOV_AccountInvestment" popuplov="true" clientchangeaction="selectEntity2()" readonly="true" 
                                             caption="{L-ENGAkun Bank-L}{L-INAAkun Bank-L}" name="deposito.stEntityID" descfield="deposito.stAccountBank" type="string" mandatory="true" presentation="standard">
                                        <c:param name="param" value="<%="122|" + deposito.getStCostCenterCode() + "|" + deposito.getStCompanyType()%>"/>
                                    </c:field>
                                    <c:field caption="Nama Bank" width="200" name="deposito.stBankName" type="string" presentation="standard" readonly="true"/>

                                    <%--
                                    <c:field width="200" lov="LOV_AccountInvestment" popuplov="true" clientchangeaction="selectEntity()" readonly="true"
                                             caption="{L-ENGAkun Deposito-L}{L-INAAkun Deposito-L}" name="deposito.stNoRekeningDeposito" descfield="deposito.stAccountDepo" type="string" mandatory="true" presentation="standard">
                                        <c:param name="depo" value="<%=deposito.getStCostCenterCode()%>"/>
                                    </c:field>                                
                                    <c:field caption="Keterangan" width="200" name="deposito.stDepoName" type="string" presentation="standard" readonly="true"/>
                                    <c:field lov="LOV_CompType" width="230" mandatory="true" name="deposito.stCompanyType" caption="{L-ENGCompany Type-L}{L-INAJenis Perusahaan-L}" type="string|255"  presentation="standard" readonly="true" >
                                        <c:param name="custcatdep" value="<%=bpd%>"/>
                                    </c:field>
                                    <c:field width="200" lov="LOV_AccountInvestment" popuplov="true" clientchangeaction="selectEntity2()" readonly="true"
                                             caption="{L-ENGAkun Bank-L}{L-INAAkun Bank-L}" name="deposito.stEntityID" descfield="deposito.stAccountBank" type="string" mandatory="true" presentation="standard">
                                        <c:param name="bank" value="<%=deposito.getStCostCenterCode()%>"/>
                                    </c:field>
                                    <c:field caption="Nama Bank" width="200" name="deposito.stBankName" type="string" presentation="standard" readonly="true"/>--%>
                                </c:evaluate>
                            </table>
                        </td>
                        <td>
                            <c:evaluate when="<%=phase >= 3%>">
                                <table cellpadding=2 cellspacing=1>
                                    <tr>
                                        <c:field lov="LOV_Currency" changeaction="onChgCurrency" caption="{L-ENGCurrency-L}{L-INAMata Uang-L}"
                                                 name="deposito.stCurrency" type="string" mandatory="true" presentation="standard" readonly="true"/>
                                        <c:field caption="Kurs" name="deposito.dbCurrencyRate" type="money16.2" mandatory="true"
                                        readonly="<%=ismasterCurrency%>" presentation="standard" readonly="true"/>
                                        <c:field caption="{L-ENG Bilyet Start Date-L}{L-INA Tanggal Awal Bilyet-L}" name="deposito.dtTglawal" type="date" presentation="standard" mandatory="true" readonly="true"/>
                                        <c:field caption="{L-ENG Bilyet End Date-L}{L-INA Tanggal Akhir Bilyet-L}" name="deposito.dtTglakhir" type="date" presentation="standard" mandatory="true" readonly="true"/>
                                        <c:field width="100" caption="Type" lov="LOV_OnCall" name="deposito.stKodedepo" type="string" mandatory="true" presentation="standard" changeaction="onChgCall" readonly="true"/>
                                        <c:field width="50" caption="{L-ENGDays-L}{L-INAHari-L}" name="deposito.stHari" type="string|3" readonly="<%=OnCall%>" presentation="standard" changeaction="calcDays" readonly="true"/>
                                        <c:field width="50" caption="{L-ENGMonths-L}{L-INABulan-L}" name="deposito.stBulan" type="string|2" readonly="<%=!OnCall%>" presentation="standard" changeaction="calcMonths" readonly="true"/>
                                        <c:field caption="{L-ENG Debet Date-L}{L-INA Tanggal Pendebetan-L}" name="deposito.dtTgldepo" type="date" presentation="standard" mandatory="true" readonly="true"/>
                                        <c:field caption="{L-ENG Mutate Date-L}{L-INA Neraca per Tanggal-L}" name="deposito.dtTglmuta" type="date" presentation="standard" mandatory="true" readonly="true"/>
                                        <c:field width="50" caption="{L-ENG Rate-L}{L-INA Bunga-L}" name="deposito.dbBunga" type="money16.2" mandatory="true" presentation="standard" readonly="true"/>
                                        <c:field width="50" caption="{L-ENG Tax-L}{L-INA Pajak-L}" name="deposito.dbPajak" type="money16.2" readonly="true" presentation="standard" readonly="true"/>
                                        <c:field width="150" caption="Deposito" name="deposito.dbNominalKurs" type="money16.2" mandatory="true" presentation="standard" changeaction="refresh" readonly="true"/>
                                        <c:field width="150" caption="Deposito (IDR)" name="deposito.dbNominal" type="money16.2" mandatory="true" presentation="standard" readonly="true" changeaction="generateDBNominal" readonly="true"/>
                                    </tr>
                                </table>
                            </c:evaluate>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td colspan=2>
                <c:evaluate when="<%=phase >= 3%>">
                    <c:field name="notesindex" type="string" hidden="true"/>
                    <c:listbox name="bunga">
                        <c:listcol title="" columnClass="header">
                            <c:button text="+" event="onNewBunga" validate="false" defaultRO="true"/>
                        </c:listcol>
                        <c:listcol title="" columnClass="detail">
                            <c:button text="-" event="onDeleteBunga" clientEvent="f.notesindex.value='$index$';"
                                      validate="false" defaultRO="true"/>
                        </c:listcol>

                        <c:listcol title=""><%=index.intValue() + 1%>
                        </c:listcol>

                        <c:listcol title="No Bukti Bunga" >
                            <c:field name="bunga.[$index$].stNoBuktiD" width="200" caption="Deskripsi" type="string" readonly="true"/>
                        </c:listcol>
                        <c:listcol title="Rekening" >
                            <%--<c:field width="200" lov="LOV_AccountInvestment" popuplov="true" clientchangeaction="selectEntity3($index$)"
                                     caption="{L-ENGAkun Bunga Bank-L}{L-INAAkun Bunga Bank-L}" name="bunga.[$index$].stEntityID" descfield="bunga.[$index$].stAccountBank" type="string" mandatory="true">
                                <c:param name="bank" value="<%=deposito.getStCostCenterCode()%>"/>
                            </c:field>--%>
                            <c:field width="200" lov="LOV_AccountInvestment" popuplov="true" clientchangeaction="selectEntity3($index$)"
                                     caption="{L-ENGAkun Bunga Bank-L}{L-INAAkun Bunga Bank-L}" name="bunga.[$index$].stEntityID" descfield="bunga.[$index$].stAccountBank" type="string" mandatory="true">
                                <c:param name="param" value="<%="122|" + deposito.getStCostCenterCode() + "|" + deposito.getStCompanyType()%>"/>
                            </c:field>
                        </c:listcol>

                        <c:listcol title="Keterangan" >
                            <c:field name="bunga.[$index$].stBankName" width="250" mandatory="true" caption="Deskripsi" type="string"/>
                        </c:listcol>
                        <c:listcol title="Tanggal" >
                            <c:field name="bunga.[$index$].dtTglBunga" width="130" mandatory="true" caption="Deskripsi" type="date"/>
                        </c:listcol>
                        <c:listcol title="Jumlah" >
                            <c:field name="bunga.[$index$].dbAngka" width="130" mandatory="true" caption="Deskripsi" type="money16.2"/>
                        </c:listcol>
                        <c:listcol title="Eff" >
                            <c:field name="bunga.[$index$].stEffectiveFlag" mandatory="true" caption="Deskripsi" type="check" readonly="true"/>
                        </c:listcol>
                    </c:listbox>
                </c:evaluate>
            </td>
        </tr>
        <tr>
            <td colspan=2>
                <c:evaluate when="<%=phase >= 3%>">
                    <table cellpadding=2 cellspacing=1>
                        <c:field width="550" rows="2" caption="{L-ENGDescription-L}{L-INAKeterangan-L}"
                                 name="deposito.stKeterangan" type="string" mandatory="false" presentation="standard"/>
                    </table>
                </c:evaluate>
            </td>
        </tr>
        <tr>
            <c:evaluate when="<%=phase >= 3%>">
                <td align=center>
                    <c:evaluate when="<%=!ro%>" >
                        <c:button text="{L-ENGRefresh-L}{L-INARefresh-L}" event="refresh" validate="true"/>
                        <c:button text="{L-ENGSave-L}{L-INASimpan-L} Bunga" event="saveBunga2" confirm="Yakin Mau Disimpan ?" validate="true"/>
                        <c:button text="{L-ENGCancel-L}{L-INABatal-L}" event="close" confirm="Yakin Mau Dibatalkan ?" validate="false"/>
                    </c:evaluate>
                    <c:evaluate when="<%=ro%>" >
                        <c:button text="{L-ENGClose-L}{L-INATutup-L}" event="close" validate="false"/>
                    </c:evaluate>
                </td> 
            </c:evaluate>     
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

    function selectEntity3(i) {
        var o = window.lovPopResult;
        document.getElementById('bunga.[' + i + '].stBankName').value = o.description;
    }
</script>