<%@ page import="com.webfin.gl.form.GLJournalForm,
         com.webfin.gl.model.JournalView,
         com.webfin.gl.model.JournalHeaderView,
         com.crux.util.validation.FieldValidator,
         com.crux.util.*,
         com.webfin.gl.ejb.CostCenterManager,
         com.webfin.ar.model.ARPaymentMethodView,
         com.webfin.ar.forms.ARTitipanPremiForm,
         com.webfin.gl.ejb.CurrencyManager"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="SETTLEMENT" >
    <%

                final GLJournalForm form = (GLJournalForm) request.getAttribute("FORM");

                final JournalHeaderView header = form.getTitipan();

                boolean readOnly = form.isReadOnly();

    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="titipan.stTransactionNo" width="200" caption="No Bukti" type="string" presentation="standard" readonly="true"/>
                                <c:field lov="LOV_CostCenter" readonly="<%=header.getStCostCenter() != null%>" width="200" name="titipan.stCostCenter" mandatory="true" caption="Cabang" type="string" presentation="standard"/>
                                <c:field width="200" name="titipan.stMonths" lov="LOV_MONTH_Period" mandatory="true" caption="Bulan" type="string" presentation="standard"/>
                                <c:field width="200" name="titipan.stYears" lov="LOV_GL_Years2" mandatory="true" caption="Tahun" type="string" presentation="standard"/>
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
                            <c:listbox name="details" paging="true" >
                                <c:listcol title="" columnClass="header" >
                                </c:listcol>
                                <c:listcol title="" columnClass="detail" >
                                    <c:button text="-" event="delLine" clientEvent="f.notesindex.value='$index$';" validate="false" enabled="<%=!readOnly%>"/>
                                </c:listcol>
                                <c:listcol title="Rekening" >
                                    <c:field show="false" name="details.[$index$].lgAccountID" width="130" mandatory="true" caption="Deskripsi" type="string" readonly="false" />
                                    <c:field name="details.[$index$].stAccountNo" width="130" mandatory="true" caption="Deskripsi" type="string" readonly="false" />
                                    <c:button text="..." clientEvent="rnc='$index$';selectAccountByBranch2();" validate="false" enabled="true"/>
                                </c:listcol>
                                <c:listcol title="Keterangan" >
                                    <c:field name="details.[$index$].stDescription" width="300" mandatory="true" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Polis" >
                                    <c:field name="details.[$index$].stPolicyNo" width="300" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Tanggal" >
                                    <c:field name="details.[$index$].dtApplyDate" width="130" mandatory="true" caption="Deskripsi" type="date" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Debit" >
                                    <c:field name="details.[$index$].dbDebit" width="130" mandatory="true" caption="Deskripsi" type="money16.2" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Credit" >
                                    <c:field name="details.[$index$].dbCredit" width="130" mandatory="true" caption="Deskripsi" type="money16.2" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Pemilik" >
                                    <c:field name="details.[$index$].stOwnerCode" width="130" mandatory="false" caption="Deskripsi" type="string" lov="LOV_OwnerDivision" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Pengguna" >
                                    <c:field name="details.[$index$].stUserCode" width="130" mandatory="false" caption="Deskripsi" type="string" lov="LOV_UserDivision" readonly="false" />
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