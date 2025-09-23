<%@ page import="com.webfin.insurance.form.InsuranceUploadForm,
         com.crux.util.Tools,
         com.crux.web.controller.SessionManager,
         com.webfin.gl.ejb.CurrencyManager,
         com.webfin.insurance.model.BiayaPemasaranView"%>
<%@ taglib prefix="c" uri="crux" %>

<c:frame>
    <%
                InsuranceUploadForm form = (InsuranceUploadForm) request.getAttribute("FORM");

                boolean ro = form.isReadOnly();

                final BiayaPemasaranView pemasaran = form.getPemasaran();

                final boolean ismasterCurrency = CurrencyManager.getInstance().isMasterCurrency(pemasaran.getStCurrency());

                boolean admin = true;
                if (SessionManager.getInstance().getSession().getStBranch() != null) {
                    admin = false;
                }
                int phase = 0;

                if (pemasaran.getStCostCenterCode() != null) {
                    phase = 1;
                }
                if (pemasaran.getStYears() != null) {
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
                                <c:field width="200" lov="LOV_CostCenter" caption="{L-ENGBranch-L}{L-INACabang-L}" name="pemasaran.stCostCenterCode" type="string"
                                mandatory="true" readonly="<%=pemasaran.getStCostCenterCode() != null%>" presentation="standard" changeaction="refresh"/>
                                <c:evaluate when="<%=phase >= 1%>">
                                    <c:field name="pemasaran.stMonths" type="string" caption="{L-ENGMonth-L}{L-INABulan Transaksi-L}" lov="LOV_MONTH_Period" mandatory="true" readonly="<%=pemasaran.getStMonths() != null%>" presentation="standard" />
                                    <c:field name="pemasaran.stYears" type="string" caption="{L-ENGBulan-L}{L-INATahun Transaksi-L}" lov="LOV_GL_Years2" mandatory="true" readonly="<%=pemasaran.getStYears() != null%>" presentation="standard" changeaction="setDate" />
                                </c:evaluate>
                                <c:evaluate when="<%=phase >= 2%>">
                                    <c:field lov="VS_TRANSAKSI" readonly="<%=pemasaran.getStKodeInput() != null%>" name="pemasaran.stKodeInput" width="150" mandatory="true" caption="Jenis Transaksi" type="string" presentation="standard"/>
                                    <c:field caption="Tanggal Biaya" name="pemasaran.dtEntryDate" type="date" readonly="true" presentation="standard"/>
                                    <c:field lov="LOV_ReceiptRequest" width="150" name="pemasaran.stReceiptClassID" mandatory="true" caption="{L-ENGMethod-L}{L-INAMetode-L}" type="string" presentation="standard" />
                                    <c:field name="pemasaran.stAccountID" type="string" hidden="true"/>
                                    <tr>
                                        <td>
                                            Bank
                                        </td>
                                        <td>
                                        </td>
                                        <td>
                                            <c:field name="pemasaran.stAccountNo" type="string" width="200"/><c:button text="..." clientEvent="selectAccountByBranch();" validate="false" enabled="true"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                        </td>
                                        <td>
                                        </td>
                                        <td>
                                            <c:field name="pemasaran.stAccountDesc" type="string" width="200"/>
                                        </td>
                                    </tr>
                                </c:evaluate>
                            </table>
                        </td>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:evaluate when="<%=phase >= 2%>">
                                    <c:field width="150" caption="No. SHM" name="pemasaran.stNoSPP" type="string" presentation="standard" readonly="true"/>
                                    <c:field lov="LOV_Currency" readonly="false" changeaction="chgCurrency" name="pemasaran.stCurrency" caption="{L-ENGCurrency-L}{L-INAMata Uang-L}" type="string" presentation="standard"/>
                                    <c:field readonly="false" mandatory="true" name="pemasaran.dbCurr" caption="Rate" type="money16.2" presentation="standard"/>
                                    <%--REVISI 2
<c:field width="100" caption="Biaya 1%" name="pemasaran.dbBiaya" type="money16.2" presentation="standard" readonly="true"/>
                                    <c:field width="100" caption="Saldo Biaya 1%" name="pemasaran.dbSaldoBiaya" type="money16.2" presentation="standard" readonly="true"/>--%>
                                    <c:field width="100" caption="Jumlah Data" name="pemasaran.stJumlahData" type="money16.0" presentation="standard" readonly="true"/>
                                    <c:field width="100" caption="Total Biaya" name="pemasaran.dbTotalBiaya" type="money16.2" presentation="standard" readonly="true"/>
                                    <c:field name="pemasaran.stKeterangan" width="200" rows="2" caption="No. Surat Pemasaran" type="string" presentation="standard" mandatory="true"/>
                                    <%--<c:field name="pemasaran.stFilePhysic" caption="Upload Bukti Pengajuan" type="file" thumbnail="true" readonly="false" presentation="standard"/>--%>
                                </c:evaluate>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <c:evaluate when="<%=phase >= 2%>">
            <tr>
                <td colspan=2>
                    <table cellpadding=2 cellspacing=1>
                        <c:tab name="tabs">
                            <c:tabpage name="TAB1">
                                <c:field name="notesindex" type="string" hidden="true"/>
                                <c:field name="pemasaranID" type="string" hidden="true"/>
                                <c:listbox name="pmsDetail">
                                    <c:listcol title="" columnClass="header">
                                        <c:button text="+" event="onNewDetail" validate="false" defaultRO="true"/>
                                    </c:listcol>
                                    <c:listcol title="" columnClass="detail">
                                        <c:button text="-" event="onDeleteDetail" clientEvent="f.notesindex.value='$index$';"
                                                  validate="false" defaultRO="true"/>
                                    </c:listcol>

                                    <c:listcol title=""><%=index.intValue() + 1%>
                                    </c:listcol>
                                    <c:listcol title="Mata Anggaran" >
                                        <c:field name="pmsDetail.[$index$].stJenisPemasaranID" lov="LOV_BiaopType3" width="200" caption="Deskripsi" type="string" mandatory="true">
                                            <c:lovLink name="polgroup" link="pemasaranID"/>
                                        </c:field>
                                    </c:listcol>
                                    <c:listcol title="No. Akun" >
                                        <c:field name="pmsDetail.[$index$].stAccountID" type="string" hidden="true"/>
                                        <c:field width="120" caption="Akun" name="pmsDetail.[$index$].stAccountNo" type="string" mandatory="true" readonly="false"/>
                                        <c:button text="..." clientEvent="selectAccountKas($index$)" validate="false" enabled="true"/>
                                    </c:listcol>
                                    <c:listcol title="Keterangan" >
                                        <c:field name="pmsDetail.[$index$].stKeterangan" width="250" mandatory="true" caption="Deskripsi" type="string"/>
                                    </c:listcol>
                                    <c:listcol title="Description" >
                                        <c:field name="pmsDetail.[$index$].stDescription" width="250" mandatory="true" caption="Deskripsi" type="string"/>
                                    </c:listcol>
                                    <%--REVISI 2
            <c:listcol title="Tanggal" >
                                        <c:field name="pmsDetail.[$index$].dtApplyDate" width="130" mandatory="true" caption="Deskripsi" type="date"/>
                                    </c:listcol>--%>
                                    <c:listcol title="Jumlah" >
                                        <c:field name="pmsDetail.[$index$].dbNilai" width="130" mandatory="true" caption="Deskripsi" type="money16.2"/>
                                    </c:listcol>
                                    <c:listcol title="Rate Pajak Pph23" >
                                        <c:field width="50" name="pmsDetail.[$index$].dbExcRatePajak" mandatory="true" caption="Excess" type="money16.2" />%
                                    </c:listcol>
                                    <c:listcol title="Jumlah Pajak" >
                                        <c:field name="pmsDetail.[$index$].dbExcAmount" width="130" readonly="true" caption="Deskripsi" type="money16.2"/>
                                    </c:listcol>
                                </c:listbox>
                            </c:tabpage>
                            <c:tabpage name="TAB2">
                                <table cellpadding=2 cellspacing=1>
                                    <c:field name="instIndex" type="string" hidden="true"/>
                                    <tr>
                                        <td>
                                            <c:listbox name="documents">
                                                <c:listcol title="" columnClass="header">
                                                    <c:button text="+" event="doNewDocument" validate="false" defaultRO="true"/>
                                                </c:listcol>
                                                <c:listcol title="" columnClass="detail">
                                                    <c:button text="-" event="doDeleteDocument" clientEvent="f.instIndex.value='$index$';"
                                                              validate="false" defaultRO="true"/>
                                                </c:listcol>
                                                <c:listcol title="{L-ENGAttachment File-L}{L-INAFile Lampiran-L}">
                                                    <c:field name="documents.[$index$].stFilePhysic" caption="Lampiran Surat" type="file" thumbnail="true"
                                                             readonly="false"/>
                                                </c:listcol>
                                                <c:listcol title="No. Surat" >
                                                    <c:field name="documents.[$index$].stKeterangan" width="250" mandatory="true" caption="No. Surat" type="string"/>
                                                </c:listcol>
                                            </c:listbox>
                                        </td>
                                    </tr>
                                </table>
                            </c:tabpage>
                        </c:tab>
                    </table>
                </td>
            </tr>
            <%--
            REVISI 1
            <tr>
                <td align=center>
                    <c:evaluate when="<%=!ro%>" >
                        <c:button text="{L-ENGRefresh-L}{L-INARefresh-L}" event="recalculatePemasaran" validate="true"/>
                        <c:button text="{L-ENGSave-L}{L-INASimpan-L}" event="doSavePms" confirm="Yakin Mau Disimpan ?" validate="true"/>
                        <c:button text="{L-ENGCancel-L}{L-INABatal-L}" event="close" confirm="Yakin Mau Dibatalkan ?" validate="false"/>
                    </c:evaluate>
                    <c:evaluate when="<%=ro%>" >
                        <c:button text="{L-ENGClose-L}{L-INATutup-L}" event="close" validate="false"/>
                    </c:evaluate>
                </td>
            </tr>
            --%>
            <td align=center>
                <c:evaluate when="<%=!form.isViewMode() && !form.isReverseMode() && !form.isReceiptMode()%>">
                    <c:button text="Del All" confirm="Yakin ingin dihapus semua?" event="onDeleteAllPms" validate="false" enabled="<%=!ro%>"/>
                    <c:button show="<%=form.isCanCreate()%>" text="Hitung Ulang" event="recalculatePemasaran" validate="true" />
                    <c:button show="<%=form.isCanCreate()%>" text="Simpan" event="doSavePms" validate="true" confirm="Do you want to save ?" />
                    <c:button show="<%=form.isCanCreate()%>" text="Cancel" event="doCancel" confirm="Do you want to cancel ?" />
                    <c:button show="<%=form.isApprovalCab() || form.isApprovalSie() || form.isApprovalBag() || form.isApprovalDiv()%>" text="Setujui" event="doApprovedPms" validate="true" confirm="Yakin ingin disetujui ?" />
                    <c:button show="<%=form.isApprovalCab() || form.isApprovalSie() || form.isApprovalBag() || form.isApprovalDiv()%>" text="Close" event="doClose"/>
                </c:evaluate>
                <c:evaluate when="<%=form.isViewMode()%>">
                    <c:button text="Close" event="doClose"/>
                </c:evaluate>
                <c:evaluate when="<%=form.isReceiptMode()%>">
                    <c:button text="Bayar" event="doBayarPms" validate="true" confirm="Yakin ingin dibayar ?" />
                    <c:button text="Close" event="doClose"/>
                </c:evaluate>
                <c:evaluate when="<%=form.isReverseMode()%>">
                    <c:button show="<%=form.isApprovalCab() || form.isApprovalSie()%>" text="Reverse" event="doReversePms" validate="true" confirm="Yakin ingin di-Reverse ?" />
                    <c:button text="Close" event="doClose"/>
                </c:evaluate>
            </td>
        </c:evaluate>
    </table>
</c:frame>
<script>
    function selectAccountByBranch(o){

        var cabang = document.getElementById('pemasaran.stCostCenterCode').value;
        var receiptclass = document.getElementById('pemasaran.stReceiptClassID').value;
        var month = document.getElementById('pemasaran.stMonths').value;
        var year = document.getElementById('pemasaran.stYears').value;

        var acccode;

        if(receiptclass=='A') acccode = '12100';
        else if(receiptclass=='B') acccode = '12110';
        else if(receiptclass=='C') acccode = '122';
        else if(receiptclass=='D') acccode = '122';

        openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'&month='+month+'&year='+year+'', 600,400,
        function (o) {
            if (o!=null) {
                document.getElementById('pemasaran.stAccountID').value=o.acid;
                document.getElementById('pemasaran.stAccountNo').value=o.acno;
                document.getElementById('pemasaran.stAccountDesc').value=o.desc;
            }
        }
    );
    }

    function selectAccountKas(i){

        var costcenter = document.getElementById('pemasaran.stCostCenterCode').value;
        var biaoptype = document.getElementById('pmsDetail.[' + i + '].stJenisPemasaranID').value;

        openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT2&costcenter='+costcenter+'&biaoptype='+biaoptype+'', 600,400,
        function (o) {
            if (o != null) {
                document.getElementById('pmsDetail.['+ i +'].stAccountID').value=o.acid;
                document.getElementById('pmsDetail.['+ i +'].stAccountNo').value=o.acno;
                document.getElementById('pmsDetail.['+ i +'].stKeterangan').value=o.desc;
            }
        }
    );
    }
</script>