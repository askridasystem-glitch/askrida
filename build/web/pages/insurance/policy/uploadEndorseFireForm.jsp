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
         com.webfin.insurance.model.UploadEndorseFireHeaderView,
         com.webfin.gl.ejb.CurrencyManager"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="UPLOAD DATA ENDORSEMEN POLIS" >
    <%

                final EndorseUploadForm form = (EndorseUploadForm) request.getAttribute("FORM");

                final UploadEndorseFireHeaderView header = form.getTitipanFire();

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
                                            <c:field name="titipanFire.stInsuranceUploadID" width="150" caption="Upload Group ID" type="string" readonly="true" presentation="standard" />
                                            <c:field width="200" mandatory="true" readonly="<%=header.getStCostCenterCode()!=null%>" caption="Cabang" changeaction="onChangeBranch" name="titipanFire.stCostCenterCode" lov="LOV_CostCenter" type="string" presentation="standard" />
                                            <c:field width="200" show="<%=header.getStCostCenterCode()!=null%>" mandatory="true" readonly="<%=header.getStRegionID()!=null%>" caption="Region" changeaction="onChangeBranch" name="titipanFire.stRegionID" lov="LOV_Region" type="string" presentation="standard" >
                                                <c:lovLink name="cc_code" link="titipanFire.stCostCenterCode" clientLink="false"/>
                                            </c:field>
                                            <c:evaluate when="<%=header.getStRegionID()!=null%>">
                                                <c:field name="titipanFire.stRecapNo" width="200" caption="Nomor Rekap" type="string" readonly="true" presentation="standard" />
                                            </c:evaluate>

                                            <c:evaluate when="<%=header.getStRegionID()!=null%>">
                                            <c:field name="titipanFire.stFilePhysic" caption="File Excel" type="file" thumbnail="true"
                                                     readonly="false" presentation="standard"/>
                                        </table>
                                        
                                    </td>
                                    <td>
                                        <table cellpadding=2 cellspacing=1>
                                            <c:evaluate when="<%=header.getStRegionID()!=null%>">
                                                <c:field name="titipanFire.dbTSITotal" readonly="true" width="200" caption="Total TSI" type="money16.2" presentation="standard" />
                                                <c:field name="titipanFire.dbPremiTotal" readonly="true" width="200" caption="Total Premi" type="money16.2" presentation="standard" />
                                                <c:field name="titipanFire.stDescription" width="400" rows="3" caption="Deskripsi" type="string" presentation="standard" />
                                            </c:evaluate>
                                        </table>
                                    </td>
                                </tr>
                                
                                <tr>
                                    <td>
                                        <c:button show="true" text="Del All" event="delAllFire"/>
                                        &nbsp;<c:button show="true" text="Konversi" event="uploadExcelFire"/>
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
                                <c:listbox name="detailsFire"  paging="true" >
                                    <c:listcol title="" columnClass="header" >
                                    </c:listcol>
                                    <c:listcol title="" columnClass="detail" >
                                        <c:button text="-" event="delLine" clientEvent="f.notesindex.value='$index$';" validate="false" enabled="<%=!readOnly%>"/>
                                    </c:listcol>

                                    <c:listcol title="Proses" >
                                        <c:field name="detailsFire.[$index$].stStatus" width="150" caption="Proses" mandatory="true" type="check" readonly="true" />
                                    </c:listcol>
                                    <c:listcol title="Vld" >
                                        <c:field name="detailsFire.[$index$].stBypassValidasiFlag" width="150" caption="Vld" mandatory="false" type="check" readonly="false" />
                                    </c:listcol>

                                    <c:listcol title="Nomor Polis" >
                                        <c:field name="detailsFire.[$index$].stPolicyNo" width="150" caption="Nomor Polis" mandatory="true" type="string" readonly="false" />
                                    </c:listcol>

                                    <c:listcol title="Jenis Endorse" >
                                        <c:field name="detailsFire.[$index$].stJenisEndorse" width="150" caption="Jenis Endorse" type="string" readonly="true" />
                                    </c:listcol>

                                    <c:listcol title="Keterangan Endorse" >
                                        <c:field name="detailsFire.[$index$].stEndorseNotes" width="250" caption="Keterangan Endorse" rows="3" type="string" readonly="false" />
                                    </c:listcol>

                                    <c:listcol title="Nama" >
                                        <c:field name="detailsFire.[$index$].stNama" width="180" caption="Nama" mandatory="true" type="string" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Nomor Urut" >
                                        <c:field name="detailsFire.[$index$].stOrderNo" width="50" mandatory="true" caption="Nomor Urut" type="string" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="TSI Awal" >
                                        <c:field name="detailsFire.[$index$].dbTSIAwal" width="130" mandatory="false" caption="TSI Awal" type="money16.2" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Premi Awal" >
                                        <c:field name="detailsFire.[$index$].dbPremiAwal" width="130" mandatory="false" caption="Premi Awal" type="money16.2" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Periode Awal" >
                                        <c:field name="detailsFire.[$index$].dtPeriodeAwal" width="130" mandatory="false" caption="Periode Awal" type="date" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Periode Akhir" >
                                        <c:field name="detailsFire.[$index$].dtPeriodeAkhir" width="130" mandatory="false" caption="Periode Akhir" type="date" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="TSI" >
                                        <c:field name="detailsFire.[$index$].dbTSI" width="130" mandatory="false" caption="TSI" type="money16.2" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Premi" >
                                        <c:field name="detailsFire.[$index$].dbPremi" width="130" mandatory="false" caption="Premi" type="money16.2" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Fee Base Pct" >
                                        <c:field name="detailsFire.[$index$].dbFeeBasePct" width="100" mandatory="false" caption="Fee Base Pct" type="money16.2" suffix="%" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Fee Base (Amount)" >
                                        <c:field name="detailsFire.[$index$].dbFeeBase" width="100" mandatory="false" caption="Fee Base (Amount)" type="money16.2" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Komisi Pct" >
                                        <c:field name="detailsFire.[$index$].dbKomisiPct" width="100" mandatory="false" caption="Komisi Pct" type="money16.2" suffix="%" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Komisi (Amount)" >
                                        <c:field name="detailsFire.[$index$].dbKomisi" width="100" mandatory="false" caption="Komisi (Amount)" type="money16.2" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Brokerfee Pct" >
                                        <c:field name="detailsFire.[$index$].dbBrokerFeePct" width="100" mandatory="false" caption="Brokerfee Pct" type="money16.2" suffix="%" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Brokerfee (Amount)" >
                                        <c:field name="detailsFire.[$index$].dbBrokerFee" width="100" mandatory="false" caption="Brokerfee (Amount)" type="money16.2" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Biaya Polis" >
                                        <c:field name="detailsFire.[$index$].dbBiayaPolis" width="100" mandatory="false" caption="Biaya Polis" type="money16.2" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Materai" >
                                        <c:field name="detailsFire.[$index$].dbMaterai" width="100" caption="Materai" mandatory="false" type="money16.2" readonly="false" />
                                    </c:listcol>
                                     <c:listcol title="Alamat Risiko" >
                                        <c:field name="detailsFire.[$index$].stAlamatRisiko" width="150" mandatory="false" caption="Alamat Risiko" type="string" readonly="false" />
                                    </c:listcol>
                                    <%--
                                    <c:listcol title="Persetujuan" >
                                        <c:field name="detailsFire.[$index$].stApprovedWho" width="180" lov="LOV_Profil" popuplov="true" caption="Deskripsi" type="string" readonly="false" />
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
                    
                    <c:button show="<%=!form.isApproveMode() && !form.isViewMode()%>" text="Hitung Ulang" event="recalculateFire" validate="true" />
                    <c:button show="<%=!form.isApproveMode() && !form.isViewMode()%>" text="Simpan" event="doSaveFire" validate="true" confirm="Do you want to save ?" />
                    <c:button show="<%=form.isApproveMode()%>" text="{L-ENGApprove-L}{L-INASetujui-L}" event="doApproveFire" confirm="Yakin ingin di setujui" validate="true"/>
                    <c:button text="Batal" event="doCancel" confirm="Do you want to cancel ?" />

            </td>
        </tr>
        <br><br>
    </table>

</c:frame>
