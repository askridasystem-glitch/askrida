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

                boolean effective = arrequest.isEffective();
                boolean cashier = arrequest.isCashierFlag();

                int phase = 0;
                if (arrequest.getStRegionID() != null) {
                    phase = 1;
                }
                if (arrequest.getStMonths() != null) {
                    phase = 2;
                }

    %>

    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field width="200" lov="LOV_CostCenter" caption="{L-ENGBranch-L}{L-INACabang-L}" name="arrequest.stCostCenterCode" type="string"
                                readonly="<%=arrequest.getStCostCenterCode() != null%>" presentation="standard" changeaction="refresh"/>
                                <c:field show="<%=arrequest.getStCostCenterCode() != null%>" lov="LOV_RKAP_UnitKerja" mandatory="false" readonly="<%=arrequest.getStRegionID() != null%>" changeaction="refresh"
                                         width="200" name="arrequest.stRegionID" caption="Unit Kerja" type="string" presentation="standard">
                                    <c:lovLink name="param" link="arrequest.stCostCenterCode" clientLink="false"/>
                                </c:field>
                                <c:evaluate when="<%=phase >= 1%>">
                                    <c:field name="arrequest.stMonths" type="string" caption="{L-ENGMonth-L}{L-INABulan Transaksi-L}" readonly="<%=arrequest.getStMonths() != null%>" lov="LOV_MONTH_Period" mandatory="true" presentation="standard" changeaction="setDate"/>
                                </c:evaluate>
                                <c:evaluate when="<%=phase >= 2%>">
                                    <c:field name="arrequest.stYears" type="string" caption="{L-ENGBulan-L}{L-INATahun Transaksi-L}" lov="LOV_GL_Years3"  readonly="<%=arrequest.getStYears() != null%>" presentation="standard" changeaction="setDate"/>
                                    <c:field lov="LOV_TYPE_REQUEST" width="230" name="arrequest.stPilihan" mandatory="true" caption="Pilihan" readonly="<%=arrequest.getStPilihan() != null%>" type="string" presentation="standard"/>
                                    <c:field lov="LOV_ReceiptRequest" width="230" name="arrequest.stReceiptClassID" mandatory="true" caption="{L-ENGMethod-L}{L-INAMetode-L}" readonly="<%=arrequest.getStReceiptClassID() != null%>" type="string" presentation="standard"/>
                                    <c:field name="arrequest.stAccountID" type="string" hidden="true"/>
                                    <tr>
                                        <td>
                                            Bank
                                        </td>
                                        <td>
                                        </td>
                                        <td>
                                            <c:field name="arrequest.stAccountNo" type="string" width="200"/>
                                            <c:evaluate when="<%=form.isReceiptMode()%>">
                                                <c:button text="..." clientEvent="selectAccountByBranch();" validate="false" enabled="true"/>
                                            </c:evaluate>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                        </td>
                                        <td>
                                        </td>
                                        <td>
                                            <c:field name="arrequest.stAccountDesc" type="string" width="200"/>
                                        </td>
                                    </tr>
                                </c:evaluate>
                            </table>
                        </td>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:evaluate when="<%=phase >= 2%>">
                                    <c:field lov="LOV_Currency" readonly="false" changeaction="chgCurrency" name="arrequest.stCurrency" caption="{L-ENGCurrency-L}{L-INAMata Uang-L}" type="string" presentation="standard" readonly="true"/>
                                    <c:field readonly="true" mandatory="true" name="arrequest.dbCurrencyRate" caption="Rate" type="money16.2" presentation="standard"/>
                                    <c:field width="120" caption="No. Permintaan" name="arrequest.stRequestNo" type="string" presentation="standard" readonly="true"/>
                                    <c:field caption="Tanggal Permintaan" name="arrequest.dtTglRequest" type="date" mandatory="true" presentation="standard" readonly="true"/>
                                    <c:field caption="Keterangan Biaya" width="300" rows="4" name="arrequest.stDescription" type="string" presentation="standard" readonly="true"/>
                                    <c:field caption="Realisasi Biaya" width="120" name="arrequest.dbNominalUsed" type="money16.2" presentation="standard" readonly="true"/>
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
                                    <c:listcol title="Account" >
                                        <c:field name="reqObject.[$index$].stAccountID" type="string" hidden="true"/>
                                        <c:field name="reqObject.[$index$].stAccountID2" type="string" hidden="true"/>
                                        <c:field width="100" caption="Akun" name="reqObject.[$index$].stAccountNo" type="string" mandatory="true" readonly="true"/>
                                    </c:listcol>
                                    <c:listcol title="Kertas Kerja" >
                                        <c:field name="reqObject.[$index$].stAccountDesc" width="200" rows="2" readonly="true" caption="Deskripsi" type="string"/>
                                    </c:listcol>
                                    <c:listcol title="Anggaran" >
                                        <c:field name="reqObject.[$index$].dbNominal" width="100" readonly="true" caption="Deskripsi" type="money16.2"/>
                                    </c:listcol>
                                    <c:listcol title="Keterangan" >
                                        <c:field name="reqObject.[$index$].stDescription" width="200" rows="2" readonly="true" caption="Deskripsi" type="string"/>
                                    </c:listcol>
                                    <c:listcol title="Qty" >
                                        <c:field name="reqObject.[$index$].stQuantity" width="30" caption="Deskripsi" type="string" readonly="true"/>
                                    </c:listcol>
                                    <c:listcol title="" >
                                        <c:field name="reqObject.[$index$].stSatuanID" lov="LOV_QUANTITY" width="50" caption="Deskripsi" type="string" readonly="true"/>
                                    </c:listcol>
                                    <c:listcol title="Tanggal<br>Realisasi" >
                                        <c:field name="reqObject.[$index$].dtTglCashback" readonly="true" caption="Deskripsi" type="date"/>
                                    </c:listcol>
                                    <c:listcol title="Biaya" >
                                        <c:field name="reqObject.[$index$].dbHargaSatuan" width="80" readonly="true" caption="Deskripsi" type="money16.2"/>
                                    </c:listcol>
                                    <c:listcol title="Total" >
                                        <c:field name="reqObject.[$index$].dbNominalRealisasi" width="100" readonly="true" caption="Deskripsi" type="money16.2"/>
                                    </c:listcol>
                                    <c:listcol title="Kwitansi" >
                                        <c:field name="reqObject.[$index$].stKwitansi" width="50" mandatory="true" caption="Lampiran Surat" type="file" thumbnail="true"/>
                                    </c:listcol>
                                </c:listbox>
                            </table>
                        </c:tabpage>
                        <c:tabpage name="TAB_LAMPIRAN">
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="instIndex" type="string" hidden="true"/>
                                <tr>
                                    <td>
                                        <c:listbox name="document">                                            
                                            <c:listcol title="{L-ENGAttachment File-L}{L-INAFile Lampiran-L}">
                                                <c:field name="document.[$index$].stFilePhysic" caption="Lampiran Surat" type="file" thumbnail="true"
                                                         readonly="false"/>
                                            </c:listcol>
                                        </c:listbox>
                                    </td>
                                </tr>
                            </table>
                        </c:tabpage>
                        <c:tabpage name="TAB_POLICY_DOCUMENTS">
                            <c:fieldcontrol when="<%=form.isApprovalByDireksiMode()%>" readonly="false">
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
                            </c:fieldcontrol>
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
                    </c:tab>
                </td>
            </tr>
        </c:evaluate>
        <tr>
            <td align=center>
                <c:evaluate when="<%=form.isReceiptMode()%>">
                    <c:button show="true" text="Bayar" event="doApproveCashier" validate="true"
                              confirm="Yakin Mau Disetujui ?"/>
                </c:evaluate>
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
        else if(receiptclass=='C') acccode = '1221';
        else if(receiptclass=='D') acccode = '1222';

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

        openDialog('so.ctl?EVENT=GL_ACCOUNTS_RKAP&costcenter='+costcenter+'&unit='+unit+'&years='+years+'&akun='+akun+'', 600,400,
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