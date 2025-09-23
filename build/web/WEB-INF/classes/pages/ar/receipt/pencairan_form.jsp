<%@ page import="com.webfin.ar.model.ARInvestmentPencairanView,
com.crux.lov.LOVManager,
com.webfin.gl.ejb.CurrencyManager,
com.crux.util.LOV,
com.crux.util.*,
java.util.Date,
com.crux.lang.LanguageManager,
com.crux.web.controller.SessionManager,
com.webfin.ar.model.ARReceiptClassView,
com.webfin.ar.forms.PencairanForm"%>
<%@ taglib prefix="c" uri="crux" %>

<c:frame>
    <%
    PencairanForm form = (PencairanForm) request.getAttribute("FORM");
    
    boolean ro = form.isReadOnly();
    
    boolean journalMode = form.isJournalMode();
    
    final ARInvestmentPencairanView pencairan = form.getPencairan();
    boolean effective = pencairan.isEffective();
    final boolean ismasterCurrency = CurrencyManager.getInstance().isMasterCurrency(pencairan.getStCurrency());
    
    boolean admin = true;
    if (SessionManager.getInstance().getSession().getStBranch()!=null){
        admin = false;
    }
    
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
    
    boolean bpdReadOnly = false;
    String bpd = "Y";
    String stReceiptClassID = null;
    if(pencairan.getStReceiptClassID()!=null)
        if(pencairan.getStReceiptClassID().equalsIgnoreCase("3")){
        bpdReadOnly = true;
        bpd = "N";
        }
    
    String method = "Y";
    
    if (Tools.compare(pencairan.getDtTglCair(), pencairan.getDtTglakhir()) < 0) {
        pencairan.setStType("1");
    } else {
        pencairan.setStType("2");
    }
    
    boolean OnCall = true;
    if(pencairan.getStKodedepo()!=null) {
        if (pencairan.getStKodedepo().equalsIgnoreCase("1"))
            OnCall = false;
    }
    
    %>
    
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <c:evaluate when="<%=!journalMode%>" >
                    <table cellpadding=2 cellspacing=1>
                        <tr>
                            <td>
                                <table cellpadding=2 cellspacing=1>
                                    <c:field width="200" lov="LOV_CostCenter"
                                             caption="{L-ENGBranch-L}{L-INACabang-L}" name="pencairan.stCostCenterCode" type="string"
                                             mandatory="true" readonly="<%=pencairan.getStCostCenterCode()!=null%>" presentation="standard" changeaction="refresh"/>
                                    <c:field name="pencairan.stMonths" type="string" caption="{L-ENGMonth-L}{L-INABulan Transaksi-L}" lov="LOV_MONTH_Period" mandatory="true" presentation="standard" changeaction="selectMonth"/>
                                    <c:field name="pencairan.stYears" type="string" caption="{L-ENGBulan-L}{L-INATahun Transaksi-L}" lov="LOV_GL_Years2" mandatory="true" presentation="standard" changeaction="setDate"/>
                                    <c:field width="200" caption="{L-ENGNo. Bukti C-L}{L-INANo. Bukti C-L}" name="pencairan.stBuktiC" type="string"
                                             readonly="true" presentation="standard"/>
                                    <c:field width="200" caption="{L-ENGNo. Bukti B-L}{L-INANo. Bukti B-L}" name="pencairan.stBuktiB" type="string"
                                             readonly="true" presentation="standard"/>
                                    <c:field width="200" caption="{L-ENGNo. Bilyet-L}{L-INANo. Bilyet-L}" name="pencairan.stNodefo" type="string" 
                                             readonly="true" presentation="standard"/>
                                    <c:field width="200" caption="{L-ENGNo. Rekening Bilyet-L}{L-INANo. Rekening Bilyet-L}" name="pencairan.stNoRekening" type="string" 
                                             readonly="true" presentation="standard"/>
                                    <c:field width="200" caption="{L-ENGAkun Deposito-L}{L-INAAkun Deposito-L}" name="pencairan.stAccountDepo" readonly="true" type="string" mandatory="true" presentation="standard"/>
                                    <c:field caption="Keterangan" width="200" name="pencairan.stDepoName" type="string" presentation="standard" readonly="true"/>
                                    <c:field lov="LOV_ReceiptClass" width="230" name="pencairan.stReceiptClassID" readonly="<%=pencairan.getStReceiptClassID()!=null%>" caption="{L-ENGMethod-L}{L-INAMetode-L}" type="string" presentation="standard" mandatory="true"/>
                                    <c:field lov="LOV_CompType" width="230" readonly="<%=pencairan.getStCompanyType()!=null%>" name="pencairan.stCompanyType" caption="{L-ENGCompany Type-L}{L-INAJenis Perusahaan-L}" type="string|255" presentation="standard" mandatory="true"/>
                                    <c:field width="200" lov="LOV_AccountInvestment" popuplov="true" clientchangeaction="selectEntity2()"
                                             caption="{L-ENGAkun Bank-L}{L-INAAkun Bank-L}" name="pencairan.stEntityID" type="string" mandatory="true" presentation="standard">
                                        <c:param name="bank" value="<%=pencairan.getStCostCenterCode()%>"/>
                                    </c:field>
                                    <c:field caption="Nama Bank" width="200" name="pencairan.stBankName" type="string" presentation="standard"/>
                                    <c:field caption="{L-ENGCurrency-L}{L-INAMata Uang-L}" name="pencairan.stCurrency" type="string" presentation="standard" readonly="true"/>
                                </table>
                            </td>
                            
                            <td>
                                <table cellpadding=2 cellspacing=1>
                                    <tr>
                                        <c:field caption="Kurs" name="pencairan.dbCurrencyRate" type="money16.2" readonly="true" presentation="standard"/>
                                        <c:field width="100" caption="Reg. Bentuk" name="pencairan.stRegisterBentuk" type="string" readonly="true" presentation="standard"/>
                                        <c:field caption="{L-ENG Start Date-L}{L-INA Tanggal Awal Bilyet-L}" name="pencairan.dtTglawal" type="date" presentation="standard" readonly="true"/>
                                        <c:field caption="{L-ENG End Date-L}{L-INA Tanggal Akhir Bilyet-L}" name="pencairan.dtTglakhir" type="date" presentation="standard" readonly="true"/>
                                        <c:field width="100" caption="Type" lov="LOV_OnCall" name="pencairan.stKodedepo" type="string" readonly="true" mandatory="true" presentation="standard"/>
                                        <c:field width="50" caption="{L-ENGDays-L}{L-INAHari-L}" name="pencairan.stHari" type="string|3" presentation="standard" readonly="true"/>
                                        <c:field width="50" caption="{L-ENGMonths-L}{L-INABulan-L}" name="pencairan.stBulan" type="string|2" presentation="standard" readonly="true"/>
                                        <c:field caption="{L-ENG Debet Date-L}{L-INA Tanggal Pendebetan-L}" name="pencairan.dtTgldepo" type="date" presentation="standard" readonly="true"/>
                                        <c:field caption="{L-ENG Mutate Date-L}{L-INA Neraca per Tanggal-L}" name="pencairan.dtTglmuta" type="date" presentation="standard" readonly="true"/>
                                        <c:field caption="{L-ENG Tanggal Cair-L}{L-INA Tanggal Cair-L}" name="pencairan.dtTglCair" type="date" presentation="standard" mandatory="true"/>
                                        <c:field width="50" caption="{L-ENG Rate-L}{L-INA Bunga-L}" name="pencairan.dbBunga" type="money16.2" readonly="true" presentation="standard"/>
                                        <c:field width="50" caption="{L-ENG Tax-L}{L-INA Pajak-L}" name="pencairan.dbPajak" type="money16.2" readonly="true" presentation="standard"/>
                                        <c:field width="150" caption="Deposito" name="pencairan.dbNominalKurs" type="money16.2" readonly="true" presentation="standard"/>
                                        <c:field width="150" caption="Deposito (IDR)" name="pencairan.dbNominal" type="money16.2" readonly="true" presentation="standard"/>
                                        <c:field lov="LOV_INVPENCAIRAN" width="150" caption="Jenis" name="pencairan.stType" type="string" presentation="standard" readonly="true"/>
                                        <c:field width="150" caption="Nominal" name="pencairan.dbPinalty" type="money16.2" presentation="standard"/>
                                    </tr>
                                </table>                
                            </td>
                        </tr>
                    </table>
                </c:evaluate>
                <c:evaluate when="<%=journalMode%>" >
                    <table cellpadding=2 cellspacing=1>
                        <tr>
                            <td>
                                <table cellpadding=2 cellspacing=1>
                                    <c:field width="200" lov="LOV_CostCenter"
                                             caption="{L-ENGBranch-L}{L-INACabang-L}" name="pencairan.stCostCenterCode" type="string"
                                             mandatory="true" readonly="<%=pencairan.getStCostCenterCode()!=null%>" presentation="standard" changeaction="refresh"/>
                                    <c:field width="200" caption="{L-ENGNo. Bukti C-L}{L-INANo. Bukti C-L}" name="pencairan.stBuktiC" type="string" readonly="true" presentation="standard"/>
                                    <c:field width="200" caption="{L-ENGNo. Bukti B-L}{L-INANo. Bukti B-L}" name="pencairan.stBuktiB" type="string" readonly="true" presentation="standard"/>
                                    <c:field width="200" caption="{L-ENGNo. Bilyet-L}{L-INANo. Bilyet-L}" name="pencairan.stNodefo" type="string" mandatory="true" presentation="standard"/>
                                    <c:field width="200" caption="{L-ENGNo. Rekening Bilyet-L}{L-INANo. Rekening Bilyet-L}" name="pencairan.stNoRekening" type="string" mandatory="true" presentation="standard"/>
                                    <c:evaluate when="<%=pencairan.getStCostCenterCode()!=null%>">
                                        <c:field width="200" lov="LOV_AccountInvestment" popuplov="true" clientchangeaction="selectEntity()"
                                                 caption="{L-ENGAkun Deposito-L}{L-INAAkun Deposito-L}" name="pencairan.stNoRekeningDeposito" descfield="pencairan.stAccountDepo" type="string" mandatory="true" presentation="standard">
                                            <c:param name="depo" value="<%=pencairan.getStCostCenterCode()%>"/>
                                        </c:field>                                
                                    </c:evaluate>    
                                    <c:field caption="Keterangan" width="200" name="pencairan.stDepoName" type="string" presentation="standard"/>
                                    <c:field lov="LOV_ReceiptClass" width="230" name="pencairan.stReceiptClassID" mandatory="true" caption="{L-ENGMethod-L}{L-INAMetode-L}" changeaction="refresh"
                                             type="string" presentation="standard" >
                                        <c:param name="custcat" value="<%=method%>"/>
                                    </c:field>
                                    <c:evaluate when="<%=pencairan.getStReceiptClassID()!=null%>">
                                        <c:field lov="LOV_CompType" width="230" mandatory="true" name="pencairan.stCompanyType" caption="{L-ENGCompany Type-L}{L-INAJenis Perusahaan-L}" type="string|255"  presentation="standard" >
                                            <c:param name="custcatdep" value="<%=bpd%>"/>
                                        </c:field>
                                    </c:evaluate>    
                                    <c:evaluate when="<%=pencairan.getStCostCenterCode()!=null%>">
                                        <c:field width="200" lov="LOV_AccountInvestment" popuplov="true" clientchangeaction="selectEntity2()"
                                                 caption="{L-ENGAkun Bank-L}{L-INAAkun Bank-L}" name="pencairan.stEntityID" descfield="pencairan.stAccountBank" type="string" mandatory="true" presentation="standard">
                                            <c:param name="bank" value="<%=pencairan.getStCostCenterCode()%>"/>
                                        </c:field>
                                    </c:evaluate>   
                                    <c:field caption="Nama Bank" width="200" name="pencairan.stBankName" type="string" presentation="standard"/>                    
                                    <c:field lov="LOV_Currency" changeaction="onChgCurrency" caption="{L-ENGCurrency-L}{L-INAMata Uang-L}"
                                             name="pencairan.stCurrency" type="string" mandatory="true" presentation="standard"/>
                                    <c:field caption="Kurs" name="pencairan.dbCurrencyRate" type="money16.2" mandatory="true"
                                             readonly="<%=ismasterCurrency%>" presentation="standard"/>
                                    <c:field width="100" caption="Reg. Bentuk" name="pencairan.stRegisterBentuk" type="string" readonly="true" presentation="standard"/>
                                </table>
                            </td>
                            
                            <td>
                                <table cellpadding=2 cellspacing=1>
                                    <tr>
                                        <c:field caption="{L-ENG Start Date-L}{L-INA Tanggal Awal Bilyet-L}" name="pencairan.dtTglawal" type="date" presentation="standard"/>
                                        <c:field caption="{L-ENG End Date-L}{L-INA Tanggal Akhir Bilyet-L}" name="pencairan.dtTglakhir" type="date" presentation="standard" readonly="true"/>
                                        <c:field width="100" caption="Type" lov="LOV_OnCall" name="pencairan.stKodedepo" type="string" mandatory="true" presentation="standard" changeaction="onChgCall"/>
                                        <c:field width="50" caption="{L-ENGDays-L}{L-INAHari-L}" name="pencairan.stHari" type="string|3" readonly="<%=OnCall%>" presentation="standard" changeaction="calcDays"/>
                                        <c:field width="50" caption="{L-ENGMonths-L}{L-INABulan-L}" name="pencairan.stBulan" type="string|2" readonly="<%=!OnCall%>" presentation="standard" changeaction="calcMonths"/>
                                        <c:field caption="{L-ENG Debet Date-L}{L-INA Tanggal Pendebetan-L}" name="pencairan.dtTgldepo" type="date" presentation="standard" readonly="true"/>
                                        <c:field caption="{L-ENG Mutate Date-L}{L-INA Neraca per Tanggal-L}" name="pencairan.dtTglmuta" type="date" presentation="standard" readonly="true"/>
                                        <c:field caption="{L-ENG Tanggal Cair-L}{L-INA Tanggal Cair-L}" name="pencairan.dtTglCair" type="date" presentation="standard" mandatory="true"/>
                                        <c:field width="50" caption="{L-ENG Rate-L}{L-INA Bunga-L}" name="pencairan.dbBunga" type="money16.2" presentation="standard"/>
                                        <c:field width="50" caption="{L-ENG Tax-L}{L-INA Pajak-L}" name="pencairan.dbPajak" type="money16.2" readonly="true" presentation="standard"/>
                                        <c:field width="150" caption="Deposito" name="pencairan.dbNominalKurs" type="money16.2" mandatory="true" presentation="standard" changeaction="refresh"/>
                                        <c:field width="150" caption="Deposito (IDR)" name="pencairan.dbNominal" type="money16.2" mandatory="true" presentation="standard" readonly="true" changeaction="generateDBNominal"/>
                                        <c:field lov="LOV_INVPENCAIRAN" width="150" caption="Jenis" name="pencairan.stType" type="string" presentation="standard" readonly="true"/>
                                        <c:field width="150" caption="Nominal" name="pencairan.dbPinalty" type="money16.2" presentation="standard"/>
                                    </tr>
                                </table>                
                            </td>
                        </tr>
                    </table>
                </c:evaluate>
            </td>
        </tr>
        <tr>
            <td colspan=2>
                <table cellpadding=2 cellspacing=1>
                    <c:field width="550" rows="2" caption="{L-ENGDescription-L}{L-INAKeterangan-L}"
                             name="pencairan.stKeterangan" type="string" mandatory="false" readonly="false"
                             presentation="standard"/>
                </table>
            </td>
        </tr>
        <tr>
            <td align=center>
                <c:evaluate when="<%=!ro&&!journalMode%>" >
                    <c:button text="{L-ENGRefresh-L}{L-INARefresh-L}" event="refresh" validate="true"/>
                    <c:button text="{L-ENGSave-L}{L-INASimpan-L}" event="save" confirm="Yakin Mau Disimpan ?" validate="true"/>
                    <c:evaluate when="<%=admin%>" >
                        <c:button text="Simpan Tanpa Jurnal" event="saveWithoutJurnal" confirm="Yakin Mau Disimpan ?" validate="true"
                                  confirm="Yakin Mau Disetujui ?"/>
                    </c:evaluate>
                    <c:button text="{L-ENGCancel-L}{L-INABatal-L}" event="close" confirm="Yakin Mau Dibatalkan ?" validate="false"/>
                </c:evaluate>
                <c:evaluate when="<%=journalMode%>" >
                    <c:button text="{L-ENGRefresh-L}{L-INARefresh-L}" event="refresh" validate="true"/>
                    <c:button text="Simpan Tanpa Jurnal" event="saveWithoutJurnal" confirm="Yakin Mau Disimpan ?" validate="true"
                              confirm="Yakin Mau Disetujui ?"/>
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
   function selectEntity() {
        var o = window.lovPopResult;
        document.getElementById('pencairan.stDepoName').value = o.description;
    }
    
   function selectEntity2() {
        var o = window.lovPopResult;
        document.getElementById('pencairan.stBankName').value = o.description;
    }
</script>