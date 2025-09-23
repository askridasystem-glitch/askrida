<%@ page import="com.webfin.ar.model.ARInvestmentBungaView,
com.crux.lov.LOVManager,
com.webfin.gl.ejb.CurrencyManager,
com.crux.util.LOV,
com.crux.util.*,
com.crux.lang.LanguageManager,
com.webfin.ar.forms.BungaForm"%>
<%@ taglib prefix="c" uri="crux" %>

<c:frame>
    <%
    BungaForm form = (BungaForm) request.getAttribute("FORM");
    
    boolean ro = form.isReadOnly();
    
    final ARInvestmentBungaView bunga = form.getBunga();
    final boolean ismasterCurrency = CurrencyManager.getInstance().isMasterCurrency(bunga.getStCurrency());
    final boolean effective = bunga.isEffective();
    
    /*
    boolean cabang = true;
 
    if(form.getStBranch()!=null){
    if(form.getStBranch().equalsIgnoreCase("00")){
    cabang = false;
    }
    }
 
    if(form.getStBranch()==null){
    cabang = false;
    }
     */
    
    String method = "Y";
    %>
    
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field width="200" lov="LOV_CostCenter"
                                         caption="{L-ENGBranch-L}{L-INACabang-L}" name="bunga.stCostCenterCode" type="string"
                                         mandatory="true" readonly="<%=bunga.getStCostCenterCode()!=null%>" presentation="standard" changeaction="refresh"/>
                                <c:field name="bunga.stMonths" type="string" caption="{L-ENGMonth-L}{L-INABulan Transaksi-L}" lov="LOV_MONTH_Period" mandatory="true" presentation="standard" changeaction="selectMonth"/>
                                <c:field name="bunga.stYears" type="string" caption="{L-ENGBulan-L}{L-INATahun Transaksi-L}" lov="LOV_GL_Years2" mandatory="true" presentation="standard" changeaction="setDate"/>
                                <c:field width="200" caption="{L-ENGNo. Bilyet-L}{L-INANo. Bilyet-L}" name="bunga.stNodefo" type="string" 
                                         readonly="true" presentation="standard"/>
                                <c:field width="200" caption="{L-ENGNo. Rekening Bilyet-L}{L-INANo. Rekening Bilyet-L}" name="bunga.stNoRekening" type="string" 
                                         readonly="true" presentation="standard"/>
                                <c:field width="200" caption="{L-ENGNo. Bukti D-L}{L-INANo. Bukti D-L}" name="bunga.stNoBuktiD" type="string"
                                         readonly="true" presentation="standard"/>
                                <c:field width="200" caption="{L-ENGNo. Bukti B-L}{L-INANo. Bukti B-L}" name="bunga.stNoBuktiB" type="string"
                                         readonly="true" presentation="standard"/>
                                <c:field width="200" caption="{L-ENGAkun Deposito-L}{L-INAAkun Deposito-L}" name="bunga.stAccountDepo" readonly="true" type="string" mandatory="true" presentation="standard"/>
                                <c:field caption="Keterangan" width="200" name="bunga.stDepoName" type="string" presentation="standard" readonly="true"/>
                                <c:field lov="LOV_ReceiptClass" width="230" name="bunga.stReceiptClassID" readonly="<%=bunga.getStReceiptClassID()!=null%>" caption="{L-ENGMethod-L}{L-INAMetode-L}" changeaction="refresh"
                                         type="string" presentation="standard" >
                                    <c:param name="custcat" value="<%=method%>"/>
                                </c:field>
                                <c:field lov="LOV_CompType" width="230" readonly="<%=bunga.getStCompanyType()!=null%>" name="bunga.stCompanyType" caption="{L-ENGCompany Type-L}{L-INAJenis Perusahaan-L}" type="string|255" presentation="standard"/>
                                <c:field width="200" lov="LOV_AccountInvestment" popuplov="true" clientchangeaction="selectEntity2()"
                                         caption="{L-ENGAkun Bank-L}{L-INAAkun Bank-L}" name="bunga.stEntityID" type="string" mandatory="true" presentation="standard">
                                    <c:param name="bank" value="<%=bunga.getStCostCenterCode()%>"/>
                                </c:field>
                                <c:field caption="Nama Bank" width="200" name="bunga.stBankName" type="string" presentation="standard"/>                    
                            </table>
                        </td>
                        
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <tr>
                                    <c:field width="200" caption="No. Register" name="bunga.stRegister" type="string" readonly="true" presentation="standard"/>
                                    <c:field width="100" caption="Reg. Bentuk" name="bunga.stRegisterBentuk" type="string" readonly="true" presentation="standard"/>
                                    <c:field caption="{L-ENG Bilyet Start Date-L}{L-INA Tanggal Awal Bilyet-L}" name="bunga.dtTglAwal" type="date" presentation="standard" readonly="true"/>
                                    <c:field caption="{L-ENG Bilyet End Date-L}{L-INA Tanggal Akhir Bilyet-L}" name="bunga.dtTglAkhir" type="date" presentation="standard" readonly="true"/>
                                    <c:field caption="{L-ENGCurrency-L}{L-INAMata Uang-L}" name="bunga.stCurrency" type="string" presentation="standard" readonly="true"/>
                                    <c:field caption="Kurs" name="bunga.dbCurrencyRate" type="money16.2" readonly="true" presentation="standard"/>
                                    <c:field width="100" caption="Type" lov="LOV_OnCall" name="bunga.stKodedepo" type="string" mandatory="true" readonly="true" presentation="standard"/>
                                    <c:field width="50" caption="{L-ENGDays-L}{L-INAHari-L}" name="bunga.stHari" type="string|5" readonly="true" presentation="standard"/>
                                    <c:field width="50" caption="{L-ENGTax-L}{L-INAPajak-L}" name="bunga.dbPajak" type="money16.2" readonly="true" presentation="standard"/>
                                    <c:field width="50" caption="{L-ENGRate-L}{L-INAPersentase Bunga-L}" name="bunga.dbPersen" mandatory="true" readonly="true" type="money16.2" presentation="standard"/>
                                    <c:field caption="{L-ENG Tanggal Cair-L}{L-INA Tanggal Cair-L}" name="bunga.dtTglCair" type="date" presentation="standard" readonly="true"/>
                                    <c:field caption="{L-ENG Rate Date-L}{L-INA Tanggal Bunga-L}" name="bunga.dtTglBunga" type="date" presentation="standard" mandatory="true"/>
                                    <c:field width="150" caption="Bunga (IDR)" name="bunga.dbAngka" type="money16.2" mandatory="true" presentation="standard"/>
                                    <c:field width="150" caption="Bunga History" name="bunga.dbAngka1" type="money16.2" readonly="true" presentation="standard"/>
                                    <c:field width="150" caption="Deposito (IDR)" name="bunga.dbNominal" type="money16.2" readonly="true" presentation="standard"/>
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
                    <c:field width="550" rows="2" caption="{L-ENGDescription-L}{L-INAKeterangan-L}"
                             name="bunga.stKeterangan" type="string" mandatory="false" presentation="standard"/>
                </table>
            </td>
        </tr>
        <tr>
            <td align=center>
                <c:evaluate when="<%=!ro%>" >
                    <c:button text="{L-ENGRefresh-L}{L-INARefresh-L}" event="refresh" validate="true"/>
                    <c:button text="{L-ENGSave-L}{L-INASimpan-L}" event="save" confirm="Yakin Mau Disimpan ?" validate="true"/>
                    <c:button text="{L-ENGCancel-L}{L-INABatal-L}" event="close" confirm="Yakin Mau Dibatalkan ?" validate="false"/>
                </c:evaluate>
                <c:evaluate
                    when="<%=!effective && form.isApprovalMode()%>">
                    <c:button show="true" text="{L-ENGApprove-L}{L-INASetujui-L}" event="approve" validate="true"
                              confirm="Yakin Mau Disetujui ?"/>
                </c:evaluate>
                <c:evaluate when="<%=form.isReverseMode()%>" >
                    <c:button text="Reverse" event="reverse"/>
                </c:evaluate>
                <c:evaluate when="<%=ro%>" >
                    <c:button text="{L-ENGClose-L}{L-INATutup-L}" event="close" validate="false"/>
                </c:evaluate>
            </td>      
        </tr>
    </table>
</c:frame>

<script>   
   function selectEntity2() {
        var o = window.lovPopResult;
        document.getElementById('bunga.stBankName').value = o.description;
    }
</script>