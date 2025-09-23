<%@ page import="com.webfin.ar.model.ARRequestFee,
         com.crux.lov.LOVManager,
         com.webfin.gl.ejb.CurrencyManager,
         com.crux.util.LOV,
         com.crux.util.*,
         com.crux.common.controller.FormTab,
         com.crux.lang.LanguageManager,
         com.crux.web.controller.SessionManager,
         com.webfin.ar.forms.RequestFeeForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="SETTLEMENT" >
    <%
                RequestFeeForm form = (RequestFeeForm) request.getAttribute("FORM");

                boolean ro = form.isReadOnly();

                final ARRequestFee arrequest = form.getArrequest();

                boolean effective = arrequest.isValidasiFlag();
                boolean cashier = arrequest.isCashierFlag();

                final boolean hasReceiptClass = arrequest.getStReceiptClassID() != null;

                int phase = 0;
                if (arrequest.getStRegionID() != null) {
                    phase = 1;
                }
                if (arrequest.getStMonths() != null) {
                    phase = 2;
                }
                final boolean canNavigateBranch = form.getStBranch() != null ? false : true;

                final boolean canNavigateRegion = SessionManager.getInstance().getSession().getStDivisionID() != null ? false : true;

                System.out.print("@@@@@@@@@@@@@@@@ " + form.isOwnerPms());
                System.out.print("@@@@@@@@@@@@@@@@ " + form.isOwnerUmum());
                System.out.print("@@@@@@@@@@@@@@@@ " + form.isOwnerAdm());
                System.out.print("@@@@@@@@@@@@@@@@ " + form.isApprovalMode());
                System.out.print("@@@@@@@@@@@@@@@@ " + form.isValidasiMode());
                System.out.print("@@@@@@@@@@@@@@@@ " + form.isApprovalByDireksiMode());
    %>

    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field width="200" lov="LOV_CostCenter" caption="{L-ENGBranch-L}{L-INACabang-L}" name="arrequest.stCostCenterCode" type="string"
                                readonly="<%=!canNavigateBranch%>" presentation="standard" changeaction="refresh"/>
                                <c:field show="<%=arrequest.getStCostCenterCode() != null%>" lov="LOV_RKAP_UnitKerja" mandatory="false" readonly="<%=!canNavigateRegion%>" changeaction="refresh"
                                         width="200" name="arrequest.stRegionID" caption="Unit Kerja" type="string" presentation="standard">
                                    <c:lovLink name="param" link="arrequest.stCostCenterCode" clientLink="false"/>
                                </c:field>
                                <c:evaluate when="<%=phase >= 1%>">
                                    <c:field name="arrequest.stMonths" type="string" caption="{L-ENGMonth-L}{L-INABulan Transaksi-L}" readonly="<%=arrequest.getStMonths() != null%>" lov="LOV_MONTH_Period" mandatory="true" presentation="standard" changeaction="setDate"/>
                                </c:evaluate>
                                <c:evaluate when="<%=phase >= 2%>">
                                    <c:field name="arrequest.stYears" type="string" caption="{L-ENGBulan-L}{L-INATahun Transaksi-L}" lov="LOV_GL_Years3"  readonly="<%=arrequest.getStYears() != null%>" presentation="standard" changeaction="setDate"/>
                                    <c:field lov="LOV_TYPE_ANGGARAN" width="150" name="arrequest.stAnggaranType" mandatory="true" caption="Tipe" readonly="<%=arrequest.getStAnggaranType() != null%>" type="string" presentation="standard" changeaction="refresh"/>
                                    <c:field lov="LOV_TYPE_REQUEST" width="230" name="arrequest.stPilihan" mandatory="true" caption="Pilihan" readonly="<%=arrequest.getStPilihan() != null%>" type="string" presentation="standard" changeaction="refresh"/>
                                    <c:field lov="LOV_Currency" readonly="false" changeaction="chgCurrency" name="arrequest.stCurrency" caption="{L-ENGCurrency-L}{L-INAMata Uang-L}" type="string" presentation="standard" readonly="true"/>
                                    <c:field readonly="true" mandatory="true" name="arrequest.dbCurrencyRate" caption="Rate" type="money16.2" presentation="standard"/>
                                </c:evaluate>
                            </table>
                        </td>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:evaluate when="<%=phase >= 2%>">
                                    <c:field width="120" caption="No. Pengajuan" name="arrequest.stRequestNo" type="string" presentation="standard" readonly="true"/>
                                    <c:field caption="Tanggal Pengajuan" name="arrequest.dtTglRequest" type="date" mandatory="true" presentation="standard"/>
                                    <c:field caption="Keterangan" width="300" rows="4" name="arrequest.stDescription" type="string" presentation="standard"/>
                                    <c:field caption="Pengajuan Anggaran" width="120" name="arrequest.dbNominalUsed" type="money16.2" presentation="standard" readonly="true"/>
                                    <%--<c:field caption="Pengembalian Biaya" width="120" name="arrequest.dbNominalBack" type="money16.2" presentation="standard" readonly="true"/>--%>
                                    <%--<c:evaluate when="<%=form.isCanApprove()%>">
                                        <c:field caption="Validasi Pimpinan" name="arrequest.stValidasiF" type="check" presentation="standard"/>
                                    </c:evaluate>--%>
                                </c:evaluate>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>

        <c:evaluate when="<%=phase >= 2%>">
            <tr>
                <td align=center>
                    <c:tab name="tabs">
                        <c:tabpage name="TAB_DETIL">
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="notesindex" type="string" hidden="true"/>
                                <c:listbox name="reqObject">
                                    <c:listcol title="" columnClass="header">
                                        <c:button text="+" event="onNewObj" validate="false" defaultRO="true"/>
                                    </c:listcol>
                                    <c:listcol title="" columnClass="detail">
                                        <c:button text="-" event="onDeleteObj" clientEvent="f.notesindex.value='$index$';"
                                                  validate="false" defaultRO="true"/>
                                    </c:listcol>
                                    <c:listcol title="" columnClass="header">
                                    </c:listcol>
                                    <c:listcol title="" columnClass="detail"><%=index.intValue() + 1%>
                                    </c:listcol>
                                    <c:listcol title="Account" >
                                        <c:field name="reqObject.[$index$].stAccountID" type="string" hidden="true"/>
                                        <c:field name="reqObject.[$index$].stAccountID2" type="string" hidden="true"/>
                                        <c:field width="100" caption="Akun" name="reqObject.[$index$].stAccountNo" type="string" mandatory="true" readonly="false"/>
                                        <c:evaluate when="<%=!ro%>" >
                                            <c:button text="..." clientEvent="selectKertasKerja($index$)" validate="false" enabled="true"/>
                                        </c:evaluate>
                                    </c:listcol>
                                    <c:listcol title="Kertas Kerja" >
                                        <c:field name="reqObject.[$index$].stAccountDesc" width="200" rows="2" mandatory="true" caption="Deskripsi" type="string"/>
                                    </c:listcol>
                                    <c:listcol title="Anggaran" >
                                        <c:field name="reqObject.[$index$].dbNominal" width="100" readonly="true" caption="Deskripsi" type="money16.2"/>
                                    </c:listcol>
                                    <c:listcol title="Keterangan" >
                                        <c:field name="reqObject.[$index$].stDescription" width="200" rows="2" mandatory="true" caption="Deskripsi" type="string"/>
                                    </c:listcol>
                                    <c:listcol title="Qty" >
                                        <c:field name="reqObject.[$index$].stQuantity" width="30" caption="Deskripsi" type="string"/>
                                    </c:listcol>
                                    <c:listcol title="" >
                                        <c:field name="reqObject.[$index$].stSatuanID" lov="LOV_QUANTITY" width="50" caption="Deskripsi" type="string"/>
                                    </c:listcol>
                                    <%--<c:listcol title="Tanggal<br>Pengajuan" >
                                        <c:field name="reqObject.[$index$].dtTglCashback" mandatory="true" caption="Deskripsi" type="date"/>
                                    </c:listcol>--%>
                                    <c:listcol title="Pengajuan" >
                                        <c:field name="reqObject.[$index$].dbHargaSatuan" width="80" mandatory="true" caption="Deskripsi" type="money16.2"/>
                                    </c:listcol>
                                    <c:listcol title="Total" >
                                        <c:field name="reqObject.[$index$].dbTotalNilai" width="100" readonly="true" caption="Deskripsi" type="money16.2"/>
                                    </c:listcol>
                                    <c:evaluate when="<%=!ro%>" >
                                        <c:listcol title="Hitung" >
                                            <c:button text="Hitung<br>Ulang" event="recalculatedet"/>
                                        </c:listcol>
                                    </c:evaluate>
                                </c:listbox>
                            </table>
                        </c:tabpage>
                        <c:tabpage name="TAB_LAMPIRAN">
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="instIndex" type="string" hidden="true"/>
                                <tr>
                                    <td>
                                        <c:listbox name="document">
                                            <c:listcol title="" columnClass="header">
                                                <c:button text="+" event="doNewDocument" validate="false" defaultRO="true"/>
                                            </c:listcol>
                                            <c:listcol title="" columnClass="detail">
                                                <c:button text="-" event="doDeleteDocument" clientEvent="f.instIndex.value='$index$';"
                                                          validate="false" defaultRO="true"/>
                                            </c:listcol>
                                            <c:listcol title="{L-ENGAttachment File-L}{L-INAFile Lampiran-L}">
                                                <c:field name="document.[$index$].stFilePhysic" caption="Lampiran Surat" type="file" thumbnail="true"
                                                         readonly="false"/>
                                            </c:listcol>
                                        </c:listbox>
                                    </td>
                                </tr>
                            </table>
                        </c:tabpage>
                        <c:tabpage name="TAB_APPROVAL">
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="appIndex" type="string" hidden="true"/>
                                <tr>
                                    <td>
                                        <c:listbox name="approval">
                                            <c:listcol title="" columnClass="header">
                                                <c:button text="+" event="doNewApproval" validate="false" defaultRO="true"/>
                                            </c:listcol>
                                            <c:listcol title="" columnClass="detail">
                                                <c:button text="-" event="doDeleteApproval" clientEvent="f.appIndex.value='$index$';"
                                                          validate="false" defaultRO="true"/>
                                            </c:listcol>
                                            <c:listcol title="NIRP">
                                                <%--<c:field name="approval.[$index$].stApprovalWho" readonly="false" caption="NIRP" type="string"/>--%>
                                                <c:field lov="LOV_Profil2" popuplov="true" mandatory="true" name="approval.[$index$].stApprovalWho"
                                                         caption="Nama Pegawai" type="string" />
                                                <%--<c:lovLink name="unitKerja" link="sppd.stUnitKerjaID" clientLink="false"/>
                                            </c:field>--%>
                                            </c:listcol>
                                            <c:listcol title="Tanggal Setujui">
                                                <c:field name="approval.[$index$].dtApprovalDate" readonly="true" caption="Deskripsi" type="date"/>
                                            </c:listcol>
                                            <c:listcol title="Eff">
                                                <c:field name="approval.[$index$].stEffectiveFlag" readonly="true" caption="Validasi Pimpinan" type="check"/>
                                            </c:listcol>
                                        </c:listbox>
                                    </td>
                                </tr>
                            </table>
                        </c:tabpage>
                        <%--
                        <c:tabpage name="TAB_POLICY_DOCUMENTS">
                            <table cellpadding=2 cellspacing=1 class=header width="100%">
                                <tr>
                                    <td>
                                        DOCUMENTS
                                    </td>
                                </tr>
                                <tr>
                                    <td class=row0>
                                        <c:listbox name="arrequest.policyDocuments">
                                            <c:listcol title="">
                                                <c:field name="arrequest.policyDocuments.[$index$].stSelectedFlag" type="check"
                                                         mandatory="false" readonly="false"/>
                                            </c:listcol>
                                            <c:listcol name="stDescription" title="DESCRIPTION">
                                            </c:listcol>
                                            <c:listcol title="Document">
                                                <c:field name="arrequest.policyDocuments.[$index$].stFilePhysic" type="file"
                                                         thumbnail="true" caption="File"/>
                                            </c:listcol>
                                        </c:listbox>
                                    </td>
                                </tr>
                            </table>
                        </c:tabpage>
                                                <c:evaluate when="<%=!effective%>">
                                                    <c:evaluate when="<%=form.isApprovalByDireksiMode()%>">
                                                        <c:tabpage name="TAB_BRANCH">

                                    <table cellpadding=2 cellspacing=1>
                                        <tr>
                                            <td>
                                                <c:field width="300" lov="LOV_BiaopTypeSgl" caption="RKAP Lainnya" name="arrequest.stAccountIDChoice" type="string"
                                                readonly="<%=arrequest.getStAccountIDChoice() != null%>" changeaction="select_rkap" presentation="standard" overrideRO="true"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <c:button text="Refresh" event="select_rkap" validate="true"/>
                                                <c:button text="Ubah Akun RKAP" event="changeRKAP" validate="true"/>
                                            </td>
                                        </tr>
                                    </table>
                                    <c:evaluate when="<%=form.getList() != null%>" >
                                        <table cellpadding=2 cellspacing=1 class=header width="100%">
                                            <tr>
                                                <td>
                                                    CABANG
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class=row0>
                                                    <c:listbox name="list">
                                                        <c:listcol title="">
                                                            <c:field name="list.[$index$].stAddFlag" type="check" readonly="false" overrideRO="true"/>
                                                        </c:listcol>
                                                        <c:listcol name="stDescriptionNew" title="DESCRIPTION">
                                                        </c:listcol>
                                                        <c:listcol title="RKAP">
                                                            <c:field name="list.[$index$].dbReference1" type="money16.2" readonly="true"/>
                                                        </c:listcol>
                                                        <c:listcol title="Used">
                                                            <c:field name="list.[$index$].dbReference2" type="money16.2" readonly="false" overrideRO="true"/>
                                                        </c:listcol>
                                                        <c:listcol title="Amount">
                                                            <c:field name="list.[$index$].dbReference3" type="money16.2" readonly="true"/>
                                                        </c:listcol>
                                                    </c:listbox>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <c:button text="{L-ENGRecalculate-L}{L-INAHitung Ulang-L}" event="recalculate" validate="true"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </c:evaluate>
                                </c:tabpage>
                            </c:evaluate>
                        </c:evaluate>
                        --%>
                    </c:tab>
                </td>
            </tr>
        </c:evaluate>
        <tr>
            <td align=center>
                <c:evaluate when="<%=!ro%>" >
                    <c:evaluate when="<%=!form.isApprovalMode() || !form.isApprovalByDireksiMode()%>">
                        <%--<c:button text="{L-ENGRefresh-L}{L-INARefresh-L}" event="refresh" validate="true"/>--%>
                        <c:button text="{L-ENGSave-L}{L-INASimpan-L}" event="doSave" confirm="Yakin Mau Disimpan ?" validate="true"/>
                    </c:evaluate>
                </c:evaluate>
                <c:evaluate when="<%=!effective%>">
                    <c:evaluate when="<%=form.isValidasiMode()%>">
                        <c:button show="true" text="{L-ENGApprove-L}{L-INASetujui-L}" event="doApproveVal" validate="true"
                                  confirm="Yakin Mau Disetujui ?"/>
                    </c:evaluate>
                    <c:evaluate when="<%=form.isApprovalMode()%>">
                        <c:button show="true" text="{L-ENGApprove-L}{L-INASetujui-L}" event="doApprove" validate="true"
                                  confirm="Yakin Mau Disetujui ?"/>
                    </c:evaluate>
                </c:evaluate>
                <%--<c:evaluate when="<%=!cashier && form.isCashierMode()%>">
                    <c:button show="true" text="{L-ENGApprove-L}{L-INASetujui-L} Kasir" event="doApproveCashier" validate="true"
                              confirm="Yakin Mau Disetujui ?"/>
                </c:evaluate>--%>
                <c:evaluate when="<%=form.isReverseMode()%>" >
                    <c:button text="Reverse" event="doReverse"/>
                </c:evaluate>
                <%--<c:button show="<%=form.isRePrintMode()%>"
                          text="{L-ENGSave-L}{L-INASimpan-L}" event="doSave"
                          confirm="Yakin mau disimpan ?" validate="true" />--%>
                <c:button text="{L-ENGClose-L}{L-INATutup-L}" event="close" validate="false"/>
            </td>
        </tr>
    </table>
</c:frame>

<script>    
    function selectAccountByBranch(o){

        var cabang = document.getElementById('arrequest.stCostCenterCode').value;
        var receiptclass = document.getElementById('arrequest.stReceiptClassID').value;
        var month = document.getElementById('arrequest.stMonths').value;
        var year = document.getElementById('arrequest.stYears').value;

        var acccode;

        if(receiptclass=='A') acccode = '12100';
        else if(receiptclass=='B') acccode = '12110';
        else if(receiptclass=='C') acccode = '122';
        else if(receiptclass=='D') acccode = '122';

        openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'&month='+month+'&year='+year+'', 600,400,
        function (o) {
            if (o!=null) {
                document.getElementById('arrequest.stAccountID').value=o.acid;
                document.getElementById('arrequest.stAccountNo').value=o.acno;
                document.getElementById('arrequest.stAccountDesc').value=o.desc;
            }
        }
    );
    }

    function selectAccountKas(i){
        
        var costcenter = document.getElementById('arrequest.stCostCenterCode').value;
        var biaoptype = document.getElementById('reqObject.[' + i + '].stBiaopTypeID').value;

        openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT2&costcenter='+costcenter+'&biaoptype='+biaoptype+'', 600,400,
        function (o) {
            if (o != null) {
                document.getElementById('reqObject.['+ i +'].stAccountID').value=o.acid;
                document.getElementById('reqObject.['+ i +'].stAccountNo').value=o.acno;
                document.getElementById('reqObject.['+ i +'].stAccountDesc').value=o.desc;
            }
        }
    );
    }

    function selectKertasKerja(i){

        var costcenter = document.getElementById('arrequest.stCostCenterCode').value;
        var unit = document.getElementById('arrequest.stRegionID').value;
        var years = document.getElementById('arrequest.stYears').value;
        var akun = document.getElementById('arrequest.stPilihan').value;
        var type = document.getElementById('arrequest.stAnggaranType').value;

        openDialog('so.ctl?EVENT=GL_ACCOUNTS_RKAP&costcenter='+costcenter+'&unit='+unit+'&years='+years+'&akun='+akun+'&type='+type+'', 600,400,
        function (o) {
            if (o != null) {
                document.getElementById('reqObject.['+ i +'].stAccountID').value=o.acid;
                document.getElementById('reqObject.['+ i +'].stAccountNo').value=o.acno;
                document.getElementById('reqObject.['+ i +'].stAccountDesc').value=o.desc;
                document.getElementById('reqObject.['+ i +'].dbNominal').value=o.vang;
                document.getElementById('reqObject.['+ i +'].stAccountID2').value=o.acid2;
            }
        }
    );
    }
</script>