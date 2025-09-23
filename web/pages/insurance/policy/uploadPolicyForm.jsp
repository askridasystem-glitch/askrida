<%@page import="com.webfin.insurance.model.UploadHeaderView"%>
<%@ page import="com.webfin.gl.form.GLJournalForm,
         com.webfin.gl.model.JournalView,
         com.webfin.gl.model.JournalHeaderView,
         com.crux.util.validation.FieldValidator,
         com.crux.util.*,
         com.webfin.gl.ejb.CostCenterManager,
         com.webfin.ar.model.ARPaymentMethodView,
         com.webfin.ar.forms.ARTitipanPremiForm,
         com.webfin.insurance.form.InsuranceUploadForm,
         com.webfin.insurance.model.UploadHeaderView,
         com.webfin.gl.ejb.CurrencyManager"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="UPLOAD ENDORSEMEN" >
    <%

                final InsuranceUploadForm form = (InsuranceUploadForm) request.getAttribute("FORM");

                final UploadHeaderView header = form.getTitipan();

                boolean readOnly = form.isReadOnly();

    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="titipan.stInsuranceUploadID" width="200" caption="Upload ID" type="string" readonly="true" presentation="standard" />
                                <c:field name="titipan.dtPolicyDate" width="200" caption="Tanggal Polis/Approval" mandatory="true" type="date" presentation="standard" />
                                <c:field name="titipan.stRecapNo" width="200" caption="No Rekap" type="string" presentation="standard" />
                                <c:field name="titipan.dbTSITotal" width="200" caption="Total TSI" type="money16.2" presentation="standard" />
                                <c:field name="titipan.dbPremiTotal" width="200" caption="Total Premi" type="money16.2" presentation="standard" />
                                 <c:field name="titipan.dbPremiKoasTotal" width="200" caption="Total Premi Koas" type="money16.2" presentation="standard" />
                                <c:field name="titipan.dbKomisiKoasTotal" width="200" caption="Total Komisi Koas" type="money16.2" presentation="standard" />
                                 <c:field name="titipan.stDataAmount" width="200" caption="Jumlah Data" type="string" presentation="standard" />
                                 <c:field name="titipan.stEndorseNote" width="400" rows="4" caption="Keterangan Endorse" type="string" presentation="standard" />
                                
                                <c:field name="titipan.stFilePhysic" caption="Upload Excel" type="file" thumbnail="true"
                                         readonly="false" presentation="standard"/>
                                <tr>
                                    <td><c:button show="true" text="Konversi" event="uploadExcel"/>
                                    </td>
                                </tr>
                                
                            </table>
                        </td>

                    </tr>

                    <tr>
                        <td>
                            <c:field name="notesindex" hidden="true" type="string"/>
                            <c:listbox name="details"  paging="true" >
                                <c:listcol title="" columnClass="header" >
                                </c:listcol>
                                <c:listcol title="" columnClass="detail" >
                                    <c:button text="-" event="delLine" clientEvent="f.notesindex.value='$index$';" validate="false" enabled="<%=!readOnly%>"/>
                                </c:listcol>
                                <c:listcol title="Status" >
                                    <c:field name="details.[$index$].stStatus" width="140" caption="Deskripsi" type="check" readonly="true" />
                                </c:listcol>
                                <c:listcol title="No Rekap Objek" >
                                    <c:field name="details.[$index$].stRecapNoObject" width="220" mandatory="false" caption="No Rekap Objek" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Nomor Polis" >
                                    <c:field name="details.[$index$].stPolicyNo" width="140" mandatory="true" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="No Urut" >
                                    <c:field name="details.[$index$].stOrderNo" width="60" mandatory="true" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Nama" >
                                    <c:field name="details.[$index$].stNama" width="200" mandatory="true" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="TSI" >
                                    <c:field name="details.[$index$].dbTSI" width="100" mandatory="true" caption="Deskripsi" type="money16.2" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Rate Jual" >
                                    <c:field name="details.[$index$].dbRateJual" width="100" mandatory="true" caption="Deskripsi" type="money16.2" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Premi" >
                                    <c:field name="details.[$index$].dbPremi" width="100" mandatory="true" caption="Deskripsi" type="money16.2" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Koas" >
                                    <c:field name="details.[$index$].stKoasuransi" width="200" popuplov="true" lov="LOV_ENTITY" mandatory="true" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Koas Menjadi" >
                                    <c:field name="details.[$index$].stKoasuransiMenjadi" width="200" popuplov="true" lov="LOV_ENTITY" mandatory="true" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Rate Koas" >
                                    <c:field name="details.[$index$].dbRateKoas" width="100" mandatory="true" caption="Deskripsi" type="money16.2" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Premi Koas" >
                                    <c:field name="details.[$index$].dbPremiKoas" width="100" mandatory="true" caption="Deskripsi" type="money16.2" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Rate Komisi Koas" >
                                    <c:field name="details.[$index$].dbRateKomisiKoas" width="100" mandatory="true" caption="Deskripsi" type="money16.2" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Komisi Koas" >
                                    <c:field name="details.[$index$].dbKomisiKoas" width="100" mandatory="true" caption="Deskripsi" type="money16.2" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Keterangan Endorse Objek" >
                                    <c:field name="details.[$index$].stEndorseNoteObject" width="400" caption="Keterangan Endorse Objek" type="string"  />
                                </c:listcol>
                                <c:listcol title="Auto Approve" >
                                    <c:field name="details.[$index$].stAutoApproveFlag" caption="Auto Approve" type="check"  />
                                </c:listcol>
                                <c:listcol title="User ID Approve" >
                                    <c:field name="details.[$index$].stApprovedWho" popuplov="true" lov="LOV_User" width="150" caption="User Approve" type="string"  />
                                </c:listcol>
 
                            </c:listbox>
                        </td>
                    </tr>
                    <%--
                    <td align="right">
                        Total : <c:field width="130" name="dbTotalTitipan" readonly="true" caption="Rekening" type="money16.2"/>
                    </td>
                    --%>
                </table>
            </td>
        </tr>



        <td align=center>
            <c:evaluate when="<%=!readOnly%>" >
                <%--<c:button text="Hitung Ulang" event="doRecalculate"/>--%>
                <c:button text="Hitung Ulang" event="recalculate" validate="true" />
                <c:button text="Simpan" event="doSave" validate="true" confirm="Do you want to save ?" />
                <c:button text="Cancel" event="doCancel" confirm="Do you want to cancel ?" />
            </c:evaluate>
            <c:evaluate when="<%=readOnly%>" >
                <c:button text="Close" event="doClose"/>
            </c:evaluate>
            <%--<c:button text="Approve" event="doApprove" confirm="Do you want to approve ?" />--%>
        </td>

    </table>

</c:frame>
<script>
    function selectAccountByBranch(){

        var cabang = document.getElementById('titipan.stCostCenter').value;
        var receiptclass = document.getElementById('titipan.stMethodCode').value;

        var acccode;



        if(receiptclass == ''){
            alert('Pilih dulu METODE titipan premi');
            return false;
        }

        var month = document.getElementById('titipan.stMonths').value;
        var year = document.getElementById('titipan.stYears').value;

        openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'&month='+month+'&year='+year+'', 600,400,selectAccount2);


        //openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'', 400,400,selectAccount2);

    }

    function selectAccount2(o) {
        if (o==null) return;
        document.getElementById('titipan.stAccountIDMaster').value=o.acid;
        document.getElementById('titipan.stAccountNoMaster').value=o.acno;
        document.getElementById('titipan.stDescriptionMaster').value=o.desc;
    }

    function selectAccountByBranch2(o){

        var cabang = document.getElementById('titipan.stCostCenter').value;
        var receiptclass = document.getElementById('titipan.stMethodCode').value;
        var acccode;

        if(receiptclass=='A') acccode = '48920';
        else if(receiptclass=='B') acccode = '48920';
        else if(receiptclass=='C') acccode = '48921';
        else if(receiptclass=='D') acccode = '48922';

        var glcode = document.getElementById('titipan.stAccountNoMaster').value.substring(5,10);
        if(glcode!='')
            acccode = acccode + glcode;

        var month = document.getElementById('titipan.stMonths').value;
        var year = document.getElementById('titipan.stYears').value;

        openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'&month='+month+'&year='+year+'', 600,400,selectAccount);


        //openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'', 400,400,selectAccount);
    }

    var rnc;
    function selectAccount(o) {
        if (o==null) return;

        document.getElementById('details.['+ rnc +'].stAccountID').value=o.acid;
        document.getElementById('details.['+ rnc +'].stAccountNo').value=o.acno;
        document.getElementById('details.['+ rnc +'].stDescription').value=o.desc;

    }


</script>