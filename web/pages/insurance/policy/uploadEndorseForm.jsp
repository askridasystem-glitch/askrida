<%@page import="com.webfin.insurance.model.UploadHeaderView"%>
<%@ page import="com.webfin.gl.form.GLJournalForm,
         com.webfin.gl.model.JournalView,
         com.webfin.gl.model.JournalHeaderView,
         com.crux.util.validation.FieldValidator,
         com.crux.util.*,
         com.webfin.gl.ejb.CostCenterManager,
         com.webfin.ar.model.ARPaymentMethodView,
         com.webfin.ar.forms.ARTitipanPremiForm,
         com.webfin.insurance.form.EndorseUploadForm,
         com.webfin.insurance.model.UploadEndorseHeaderView,
         com.webfin.gl.ejb.CurrencyManager"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="UPLOAD DATA ENDORSEMEN" >
    <%

                final EndorseUploadForm form = (EndorseUploadForm) request.getAttribute("FORM");

                final UploadEndorseHeaderView header = form.getTitipan();

                boolean readOnly = form.isReadOnly();

    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <tr>
                                    <td>
                                        <table cellpadding=2 cellspacing=1>
                                            <c:field name="titipan.stInsuranceUploadID" width="150" caption="Upload Group ID" type="string" readonly="true" presentation="standard" />
                                            <c:field width="200" mandatory="true" readonly="<%=header.getStCostCenterCode()!=null%>" caption="Cabang" changeaction="onChangeBranch" name="titipan.stCostCenterCode" lov="LOV_CostCenter" type="string" presentation="standard" />
                                            <c:field width="200" show="<%=header.getStCostCenterCode()!=null%>" mandatory="true" readonly="<%=header.getStRegionID()!=null%>" caption="Region" changeaction="onChangeBranch" name="titipan.stRegionID" lov="LOV_Region" type="string" presentation="standard" >
                                                <c:lovLink name="cc_code" link="titipan.stCostCenterCode" clientLink="false"/>
                                            </c:field>
                                            <c:evaluate when="<%=header.getStRegionID()!=null%>">
                                                <c:field name="titipan.stRecapNo" width="200" caption="Nomor Rekap" type="string" readonly="true" presentation="standard" />
                                            </c:evaluate>

                                            <c:evaluate when="<%=header.getStRegionID()!=null%>">
                                            <c:field name="titipan.stFilePhysic" caption="File Excel" type="file" thumbnail="true"
                                                     readonly="false" presentation="standard"/>
                                        </table>
                                        
                                    </td>
                                    <td>
                                        <table cellpadding=2 cellspacing=1>
                                            <c:evaluate when="<%=header.getStRegionID()!=null%>">
                                                <c:field name="titipan.dbTSITotal" readonly="true" width="200" caption="Total TSI" type="money16.2" presentation="standard" />
                                                <c:field name="titipan.dbPremiTotal" readonly="true" width="200" caption="Total Premi" type="money16.2" presentation="standard" />
                                                <c:field name="titipan.stDescription" width="400" rows="3" caption="Deskripsi" type="string" presentation="standard" />
                                            </c:evaluate>
                                        </table>
                                    </td>
                                </tr>
                                
                                <tr>
                                    <td>
                                        <c:button show="true" text="Del All" event="delAll"/>
                                        &nbsp;<c:button show="true" text="Konversi" event="uploadExcel"/>
                                    </td>
                                </tr>
                                </c:evaluate>
                                
                            </table>
                        </td>



                    </tr>


                    <c:evaluate when="<%=header.getStRegionID()!=null%>">
                    <tr>
                        <td>
                            
                            <table>
                                <c:field name="notesindex" hidden="true" type="string"/>
                                <c:listbox name="details"  paging="true" >
                                    <c:listcol title="" columnClass="header" >
                                    </c:listcol>
                                    <c:listcol title="" columnClass="detail" >
                                        <c:button text="-" event="delLine" clientEvent="f.notesindex.value='$index$';" validate="false" enabled="<%=!readOnly%>"/>
                                    </c:listcol>

                                    <c:listcol title="Proses" >
                                        <c:field name="details.[$index$].stStatus" width="150" caption="Deskripsi" mandatory="true" type="check" readonly="true" />
                                    </c:listcol>
                                    <c:listcol title="Vld" >
                                        <c:field name="details.[$index$].stBypassValidasiFlag" width="150" caption="Deskripsi" mandatory="false" type="check" readonly="false" />
                                    </c:listcol>

                                    <c:listcol title="Nomor Polis" >
                                        <c:field name="details.[$index$].stPolicyNo" width="150" caption="Deskripsi" mandatory="true" type="string" readonly="false" />
                                    </c:listcol>

                                    <c:listcol title="Jenis Endorse" >
                                        <c:field name="details.[$index$].stJenisEndorse" width="150" caption="Deskripsi" type="string" readonly="true" />
                                    </c:listcol>

                                    <c:listcol title="Keterangan Endorse" >
                                        <c:field name="details.[$index$].stEndorseNotes" width="250" caption="Deskripsi" rows="3" type="string" readonly="false" />
                                    </c:listcol>

                                    <c:listcol title="Nama" >
                                        <c:field name="details.[$index$].stNama" width="180" caption="Deskripsi" mandatory="true" type="string" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Nomor Urut" >
                                        <c:field name="details.[$index$].stOrderNo" width="50" mandatory="true" caption="Deskripsi" type="string" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="TSI Awal" >
                                        <c:field name="details.[$index$].dbTSIAwal" width="130" mandatory="false" caption="Deskripsi" type="money16.2" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Premi Awal" >
                                        <c:field name="details.[$index$].dbPremiAwal" width="130" mandatory="false" caption="Deskripsi" type="money16.2" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Tanggal Lahir" >
                                        <c:field name="details.[$index$].dtTanggalLahir" width="130" mandatory="false" caption="Deskripsi" type="date" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Periode Awal" >
                                        <c:field name="details.[$index$].dtPeriodeAwal" width="130" mandatory="false" caption="Deskripsi" type="date" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Periode Akhir" >
                                        <c:field name="details.[$index$].dtPeriodeAkhir" width="130" mandatory="false" caption="Deskripsi" type="date" readonly="false" />
                                    </c:listcol>

                                    <c:listcol title="TSI" >
                                        <c:field name="details.[$index$].dbTSI" width="130" mandatory="false" caption="Deskripsi" type="money16.2" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Premi" >
                                        <c:field name="details.[$index$].dbPremi" width="130" mandatory="false" caption="Deskripsi" type="money16.2" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Tanggal Restitusi" >
                                        <c:field name="details.[$index$].dtRestitutionDate" width="130" mandatory="false" caption="Deskripsi" type="date" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Sisa Jangka Waktu" >
                                        <c:field name="details.[$index$].stSisaJangkaWaktu" width="60" caption="Deskripsi" type="string" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Pct Restitusi" >
                                        <c:field name="details.[$index$].dbRestitutionPct" width="60" mandatory="false" caption="Deskripsi" type="money16.2" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="No Referensi" >
                                        <c:field name="details.[$index$].stReferenceNo" width="180" caption="Deskripsi" type="string" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Fee Base" >
                                        <c:field name="details.[$index$].dbFeeBase" width="100" mandatory="false" caption="Deskripsi" type="money16.2" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Komisi" >
                                        <c:field name="details.[$index$].dbKomisi" width="100" mandatory="false" caption="Deskripsi" type="money16.2" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Brokerfee" >
                                        <c:field name="details.[$index$].dbBrokerFee" width="100" mandatory="false" caption="Deskripsi" type="money16.2" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Biaya Polis" >
                                        <c:field name="details.[$index$].dbBiayaPolis" width="100" mandatory="false" caption="Deskripsi" type="money16.2" readonly="false" />
                                    </c:listcol>

                                    <c:listcol title="Materai" >
                                        <c:field name="details.[$index$].dbMaterai" width="100" caption="Deskripsi" mandatory="false" type="money16.2" readonly="false" />
                                    </c:listcol>
                                    <%--
                                    <c:listcol title="Persetujuan" >
                                        <c:field name="details.[$index$].stApprovedWho" width="180" lov="LOV_Profil" popuplov="true" caption="Deskripsi" type="string" readonly="false" />
                                    </c:listcol>
                                    --%>
                                </c:listbox>
                            </table>
                            
                        </td>
                    </tr>
                    </c:evaluate>
                </table>
            </td>
        </tr>


        <tr>
            <td align=center>
                    
                    <c:button show="<%=!form.isApproveMode() && !form.isViewMode()%>" text="Hitung Ulang" event="recalculate" validate="true" />
                    <c:button show="<%=!form.isApproveMode() && !form.isViewMode()%>" text="Simpan" event="doSave" validate="true" confirm="Do you want to save ?" />
                    <c:button show="<%=form.isApproveMode()%>" text="{L-ENGApprove-L}{L-INASetujui-L}" event="doApprove" confirm="Yakin ingin di setujui" validate="true"/>
                    <c:button text="Batal" event="doCancel" confirm="Do you want to cancel ?" />

            </td>
        </tr>
        <br><br>
    </table>

</c:frame>
