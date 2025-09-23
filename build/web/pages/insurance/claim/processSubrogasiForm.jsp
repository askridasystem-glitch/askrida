<%@page import="com.webfin.insurance.model.UploadHeaderView"%>
<%@ page import="com.webfin.gl.form.GLJournalForm,
         com.webfin.gl.model.JournalView,
         com.webfin.gl.model.JournalHeaderView,
         com.crux.util.validation.FieldValidator,
         com.crux.util.*,
         com.webfin.gl.ejb.CostCenterManager,
         com.webfin.ar.model.ARPaymentMethodView,
         com.webfin.ar.forms.ARTitipanPremiForm,
         com.webfin.insurance.form.ClaimUploadForm,
         com.webfin.insurance.model.UploadSubrogasiHeaderView,
         com.webfin.gl.ejb.CurrencyManager"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="PROSES SUBROGASI" >
    <%

                final ClaimUploadForm form = (ClaimUploadForm) request.getAttribute("FORM");

                final UploadSubrogasiHeaderView header = form.getSubrogasi();

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
                                            <c:field name="subrogasi.stInsuranceUploadID" width="150" caption="Upload Group ID" type="string" readonly="true" presentation="standard" />
                                            <c:evaluate when="<%=header.getStRegionID()!=null%>">
                                                <c:field name="subrogasi.stRecapNo" width="200" caption="Nomor Rekap" type="string" readonly="true" presentation="standard" />
                                            </c:evaluate>
                                            <c:field width="200" mandatory="true" readonly="<%=header.getStCostCenterCode()!=null%>" caption="Cabang" changeaction="onChangeBranch" name="subrogasi.stCostCenterCode" lov="LOV_CostCenter" type="string" presentation="standard" />
                                            <c:field width="200" show="<%=header.getStCostCenterCode()!=null%>" mandatory="true" readonly="<%=header.getStRegionID()!=null%>" caption="Region" changeaction="onChangeBranch" name="subrogasi.stRegionID" lov="LOV_Region" type="string" presentation="standard" >
                                                <c:lovLink name="cc_code" link="subrogasi.stCostCenterCode" clientLink="false"/>
                                            </c:field>
                                             
                                            <c:evaluate when="<%=header.getStRegionID()!=null%>">
                                            <c:field name="subrogasi.stFilePhysic" caption="File Excel" width="200" type="file" thumbnail="true"
                                                     readonly="false" presentation="standard"/>
                                        </table>
                                        
                                    </td>
                                    <td>
                                        <table cellpadding=2 cellspacing=1>
                                            <c:evaluate when="<%=header.getStRegionID()!=null%>">
                                               
                                                <c:field name="subrogasi.dbClaimTotal" readonly="true" width="200" caption="Total Klaim" type="money16.2" presentation="standard" />
                                                <c:field name="subrogasi.stDescription" width="400" rows="3" caption="Deskripsi" type="string" presentation="standard" />
                                                <%--
                                                <c:field name="titipan.stClaimDocument" caption="Dokumen Klaim (Zip File)" width="200" type="file" thumbnail="true"
                                                         readonly="false" presentation="standard" />

                                                <c:field name="titipan.stClaimAttachment" caption="Analisa Klaim (Zip File)" width="200" type="file" thumbnail="true"
                                                         readonly="false" presentation="standard" />--%>
                                            </c:evaluate>
                                        </table>
                                    </td>
                                </tr>
                                
                                <tr>
                                    <td>
                                        <c:button show="true" text="Del All" event="delAllSubrogasi"/>
                                        &nbsp;<c:button show="true" text="Konversi" event="btnUploadSubrogasi"/>
                                        <%--&nbsp;<c:button show="true" text="Load Dokumen Klaim" event="processZipFile"/> --%>
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
                                <c:listbox name="subrogasiDetails"  paging="true" >
                                    <c:listcol title="" columnClass="header" >
                                    </c:listcol>
                                    <c:listcol title="" columnClass="detail" >
                                        <c:button text="-" event="delLineSubrogasi" clientEvent="f.notesindex.value='$index$';" validate="false" enabled="<%=!readOnly%>"/>
                                    </c:listcol>

                                    <c:listcol title="Proses" >
                                        <c:field name="subrogasiDetails.[$index$].stStatus" width="150" caption="Proses" mandatory="true" type="check" readonly="true" />
                                    </c:listcol>
                                    <c:listcol title="Nomor Polis" >
                                        <c:field name="subrogasiDetails.[$index$].stPolicyNo" width="150" caption="Nomor Polis" mandatory="true" type="string" readonly="false" />
                                    </c:listcol>
                                    <%--
                                    <c:listcol title="Nomor Urut" >
                                        <c:field name="subrogasiDetails.[$index$].stOrderNo" width="50" mandatory="true" caption="Nomor Urut" type="string" readonly="false" />
                                    </c:listcol>
                                    --%>
                                    <c:listcol title="Nomor LKP" >
                                        <c:field name="subrogasiDetails.[$index$].stDLANo" width="150" caption="Nomor Polis" mandatory="true" type="string" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Deskripsi/Nama" >
                                        <c:field name="subrogasiDetails.[$index$].stDescription" width="180" caption="Deskripsi" mandatory="true" type="string" readonly="false" />
                                    </c:listcol>
                                     <%--<c:listcol title="Dokumen Klaim (Nopol_NoUrut_KodeDoc)" >
                                         <c:field name="details.[$index$].stFilePhysic" caption="Dokumen Klaim" width="200" type="file" thumbnail="true"
                                                     readonly="false" />
                                    </c:listcol>
                                    --%>
                                   
                                    <c:listcol title="Tanggal LKP" >
                                        <c:field name="subrogasiDetails.[$index$].dtTanggalLKP" width="130" mandatory="true" caption="Tanggal LKS" type="date" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Tgl Bayar Subrogasi" >
                                        <c:field name="subrogasiDetails.[$index$].dtTanggalBayarSubrogasi" width="130" mandatory="true" caption="Tanggal Byr Subrogasi" type="date" readonly="false" />
                                    </c:listcol>
                                   
                                    <c:listcol title="Mata Uang" >
                                        <c:field name="subrogasiDetails.[$index$].stClaimCurrency" lov="LOV_Currency" width="120" caption="Mata Uang" mandatory="true" type="string" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Kurs" >
                                        <c:field name="subrogasiDetails.[$index$].dbClaimCurrencyRate" width="60" caption="Kurs" mandatory="true" type="money16.2" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Nilai Subrogasi" >
                                        <c:field name="subrogasiDetails.[$index$].dbSubrogasiAmount" width="120" caption="Nilai Klaim" mandatory="true" type="money16.2" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Fee Recovery" >
                                        <c:field name="subrogasiDetails.[$index$].dbFeeRecovery" width="120" caption="Deductible" mandatory="true" type="money16.2" readonly="false" />
                                    </c:listcol>
                                   
                                    <c:listcol title="Approved By" >
                                        <c:field name="subrogasiDetails.[$index$].stApprovedWho" lov="LOV_User" width="180" caption="Approved" mandatory="false" type="string" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Jenis Proses" >
                                        <c:field name="subrogasiDetails.[$index$].stProcessType" width="150" caption="Proses" mandatory="true" type="string" readonly="true" />
                                    </c:listcol>
                                    <c:listcol title="Keterangan Endorse" >
                                        <c:field name="subrogasiDetails.[$index$].stEndorseNotes" width="250" caption="Tolak" mandatory="false" type="string" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Catatan Klaim" >
                                        <c:field name="subrogasiDetails.[$index$].stClaimNotes" width="250" caption="Tolak" mandatory="false" type="string" readonly="false" />
                                    </c:listcol>
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
                    
                    <c:button show="<%=!form.isApproveMode() && !form.isViewMode()%>" text="Hitung Ulang" event="recalculateSubrogasi" validate="true" />
                    <c:button show="<%=!form.isApproveMode() && !form.isViewMode()%>" text="Simpan" event="doSaveSubrogasi" validate="true" confirm="Do you want to save ?" />
                    <c:button show="<%=form.isApproveMode()%>" text="{L-ENGApprove-L}{L-INASetujui-L}" event="doApproveSubrogasi" confirm="Yakin ingin di setujui" validate="true"/>
                    <c:button text="Batal" event="doCancel" confirm="Do you want to cancel ?" />

            </td>
        </tr>
        <br><br>
    </table>

</c:frame>
