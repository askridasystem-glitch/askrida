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
         com.webfin.insurance.model.UploadClaimHeaderView,
         com.webfin.gl.ejb.CurrencyManager"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="UPLOAD KLAIM (LKS)" >
    <%

                final ClaimUploadForm form = (ClaimUploadForm) request.getAttribute("FORM");

                final UploadClaimHeaderView header = form.getTitipan();

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
                                            <c:evaluate when="<%=header.getStRegionID()!=null%>">
                                                <c:field name="titipan.stRecapNo" width="200" caption="Nomor Rekap" type="string" readonly="true" presentation="standard" />
                                            </c:evaluate>
                                            <c:field width="200" mandatory="true" readonly="<%=header.getStCostCenterCode()!=null%>" caption="Cabang" changeaction="onChangeBranch" name="titipan.stCostCenterCode" lov="LOV_CostCenter" type="string" presentation="standard" />
                                            <c:field width="200" show="<%=header.getStCostCenterCode()!=null%>" mandatory="true" readonly="<%=header.getStRegionID()!=null%>" caption="Region" changeaction="onChangeBranch" name="titipan.stRegionID" lov="LOV_Region" type="string" presentation="standard" >
                                                <c:lovLink name="cc_code" link="titipan.stCostCenterCode" clientLink="false"/>
                                            </c:field>
                                             
                                            <c:evaluate when="<%=header.getStRegionID()!=null%>">
                                            <c:field name="titipan.stFilePhysic" caption="File Excel" width="200" type="file" thumbnail="true"
                                                     readonly="false" presentation="standard"/>
                                        </table>
                                        
                                    </td>
                                    <td>
                                        <table cellpadding=2 cellspacing=1>
                                            <c:evaluate when="<%=header.getStRegionID()!=null%>">
                                               
                                                <c:field name="titipan.dbClaimTotal" readonly="true" width="200" caption="Total Klaim" type="money16.2" presentation="standard" />
                                                <c:field name="titipan.stDescription" width="400" rows="3" caption="Deskripsi" type="string" presentation="standard" />
                                                <c:field name="titipan.stClaimDocument" caption="Dokumen Klaim (Zip File)" width="200" type="file" thumbnail="true"
                                                         readonly="false" presentation="standard" />

                                                <c:field name="titipan.stClaimAttachment" caption="Analisa Klaim (Zip File)" width="200" type="file" thumbnail="true"
                                                         readonly="false" presentation="standard" />
                                            </c:evaluate>
                                        </table>
                                    </td>
                                </tr>
                                
                                <tr>
                                    <td>
                                        <c:button show="true" text="Del All" event="delAll"/>
                                        &nbsp;<c:button show="true" text="Konversi" event="btnUploadExcel"/>
                                        &nbsp;<c:button show="true" text="Load Dokumen Klaim" event="processZipFile"/>
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
                                        <c:field name="details.[$index$].stStatus" width="150" caption="Proses" mandatory="true" type="check" readonly="true" />
                                    </c:listcol>
                                    <c:listcol title="Nomor Polis" >
                                        <c:field name="details.[$index$].stPolicyNo" width="150" caption="Nomor Polis" mandatory="true" type="string" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Nomor Urut" >
                                        <c:field name="details.[$index$].stOrderNo" width="50" mandatory="true" caption="Nomor Urut" type="string" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Deskripsi/Nama" >
                                        <c:field name="details.[$index$].stDescription" width="180" caption="Deskripsi" mandatory="true" type="string" readonly="false" />
                                    </c:listcol>
                                     <c:listcol title="Dokumen Klaim (Nopol_NoUrut_KodeDoc)" >
                                         <c:field name="details.[$index$].stFilePhysic" caption="Dokumen Klaim" width="200" type="file" thumbnail="true"
                                                     readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Tanggal Pengajuan" >
                                        <c:field name="details.[$index$].dtTanggalPengajuanKlaim" width="130" mandatory="true" caption="Tanggal Pengajuan Klaim" type="date" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Tanggal Klaim" >
                                        <c:field name="details.[$index$].dtTanggalKlaim" width="130" mandatory="true" caption="Tanggal Klaim" type="date" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Tanggal LKS" >
                                        <c:field name="details.[$index$].dtTanggalLKS" width="130" mandatory="true" caption="Tanggal LKS" type="date" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Penyebab Klaim" >
                                        <c:field name="details.[$index$].stClaimCauseID" lov="LOV_ClaimCauseAll" width="220" caption="Penyebab Klaim" mandatory="true" type="string" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Penyebab Spesifik" >
                                        <c:field name="details.[$index$].stClaimSubCauseID" lov="LOV_ClaimSubCauseAll" width="220" caption="Penyebab Spesifik" mandatory="false" type="string" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Lokasi Klaim" >
                                        <c:field name="details.[$index$].stClaimEventLocation" width="180" caption="Lokasi Klaim" mandatory="true" type="string" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Pengaju Klaim" >
                                        <c:field name="details.[$index$].stClaimPersonName" width="180" caption="Pengaju Klaim" mandatory="true" type="string" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Alamat Pengaju Klaim" >
                                        <c:field name="details.[$index$].stClaimPersonAddress" width="180" caption="Alamat Pengaju Klaim" mandatory="true" type="string" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Status Pengaju Klaim" >
                                        <c:field name="details.[$index$].stClaimPersonStatus" width="180" caption="Status Pengaju Klaim" mandatory="true" type="string" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Status Kerugian" >
                                        <c:field name="details.[$index$].stClaimLossID" lov="LOV_ClaimLossAll" width="180" caption="Status Kerugian" mandatory="true" type="string" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Claim Benefit" >
                                        <c:field name="details.[$index$].stClaimBenefit" lov="VS_CLAIM_BENEFIT" width="150" caption="Claim benefit" mandatory="true" type="string" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Dokumen Klaim" >
                                        <c:field name="details.[$index$].stClaimDocument" width="180" caption="Dokumen Klaim" mandatory="true" type="string" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Kronologis" >
                                        <c:field name="details.[$index$].stClaimChronology" width="180" caption="Kronologis" mandatory="true" type="string" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Mata Uang" >
                                        <c:field name="details.[$index$].stClaimCurrency" lov="LOV_Currency" width="120" caption="Mata Uang" mandatory="true" type="string" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Kurs" >
                                        <c:field name="details.[$index$].dbClaimCurrencyRate" width="80" caption="Kurs" mandatory="true" type="money16.2" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Nilai Klaim Perkiraan" >
                                        <c:field name="details.[$index$].dbClaimAmountEstimate" width="150" caption="Nilai Klaim" mandatory="true" type="money16.2" readonly="false" />
                                    </c:listcol>
                                    <c:listcol title="Nilai Deductible" >
                                        <c:field name="details.[$index$].dbClaimDeductibleAmount" width="150" caption="Deductible" mandatory="true" type="money16.2" readonly="false" />
                                    </c:listcol>
                                   <c:listcol title="Potensi Subrogasi" >
                                        <c:field name="details.[$index$].stPotensiSubrogasi" width="150" caption="potensi" mandatory="true" type="check" readonly="false" />
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
                    
                    <c:button show="<%=!form.isApproveMode() && !form.isViewMode()%>" text="Hitung Ulang" event="recalculate" validate="true" />
                    <c:button show="<%=!form.isApproveMode() && !form.isViewMode()%>" text="Simpan" event="doSave" validate="true" confirm="Do you want to save ?" />
                    <c:button show="<%=form.isApproveMode()%>" text="{L-ENGApprove-L}{L-INASetujui-L}" event="doApprove" confirm="Yakin ingin di setujui" validate="true"/>
                    <c:button text="Batal" event="doCancel" confirm="Do you want to cancel ?" />

            </td>
        </tr>
        <br><br>
    </table>

</c:frame>
