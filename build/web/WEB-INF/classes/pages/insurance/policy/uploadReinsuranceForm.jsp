<%@ page import="com.webfin.gl.form.GLJournalForm,
         com.webfin.gl.model.JournalView,
         com.webfin.gl.model.JournalHeaderView,
         com.crux.util.validation.FieldValidator,
         com.crux.util.*,
         com.webfin.gl.ejb.CostCenterManager,
         com.webfin.ar.model.ARPaymentMethodView,
         com.webfin.ar.forms.ARTitipanPremiForm,
         com.webfin.insurance.form.InsuranceUploadForm,
         com.webfin.insurance.model.UploadHeaderReinsuranceView,
         com.webfin.gl.ejb.CurrencyManager"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="UPLOAD SPREADING" >
    <%

                final InsuranceUploadForm form = (InsuranceUploadForm) request.getAttribute("FORM");

                final UploadHeaderReinsuranceView header = form.getHeaderReins();

                boolean readOnly = form.isReadOnly();

    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="headerReins.stInsuranceUploadID" width="200" caption="Upload ID" type="string" readonly="true" presentation="standard" />
                                <c:field name="headerReins.stRecapNo" width="200" caption="No Rekap" type="string" presentation="standard" />
                                <c:field name="headerReins.dbTSITotal" width="200" caption="Total TSI" type="money16.2" presentation="standard" />
                                <c:field name="headerReins.dbPremiTotal" width="200" caption="Total Premi" type="money16.2" presentation="standard" />
                                <c:field name="headerReins.stDataAmount" width="200" caption="Jumlah Data" type="string" presentation="standard" />
                                <c:field name="headerReins.stReinsurerNote" width="400" rows="4" caption="Keterangan Endorse" type="string" presentation="standard" />

                                <c:field name="headerReins.stFilePhysic" caption="Upload Excel" type="file" thumbnail="true"
                                         readonly="false" presentation="standard"/>
                                <tr>
                                    <td><c:button show="true" text="Konversi" event="uploadExcelRI"/>
                                    </td>
                                </tr>

                            </table>
                        </td>

                    </tr>

                    <tr>
                        <td>
                            <c:field name="notesindex" hidden="true" type="string"/>
                            <c:listbox name="detailsreins"  paging="true" >
                                <c:listcol title="" columnClass="header" >
                                </c:listcol>
                                <c:listcol title="" columnClass="detail" >
                                    <c:button text="-" event="delLine2" clientEvent="f.notesindex.value='$index$';" validate="false" enabled="<%=!readOnly%>"/>
                                </c:listcol>
                                <c:listcol title="Status" >
                                    <c:field name="detailsreins.[$index$].stStatus" width="140" caption="Deskripsi" type="check" readonly="true" />
                                </c:listcol>
                                <c:listcol title="Nomor Polis" >
                                    <c:field name="detailsreins.[$index$].stPolicyNo" width="140" mandatory="true" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="No Urut" >
                                    <c:field name="detailsreins.[$index$].stOrderNo" width="60" mandatory="true" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Nama" >
                                    <c:field name="detailsreins.[$index$].stNama" width="200" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="TSI" >
                                    <c:field name="detailsreins.[$index$].dbTSI" width="100" caption="Deskripsi" type="money16.2" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Premi" >
                                    <c:field name="detailsreins.[$index$].dbPremi" width="100" caption="Deskripsi" type="money16.2" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Treaty" >
                                   <%-- <c:field name="detailsreins.[$index$].stTreaty" width="200" popuplov="true" lov="LOV_Treaty2" mandatory="true" caption="Deskripsi" type="string" readonly="false" />--%>
                                    <c:field name="detailsreins.[$index$].stTreaty" width="200" mandatory="true" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Keterangan Endorse Objek" >
                                    <c:field name="detailsreins.[$index$].stReinsurerNoteObject" width="400" caption="Keterangan Endorse Objek" type="string"  />
                                </c:listcol>

                            </c:listbox>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>

        <td align=center>
            <c:evaluate when="<%=!readOnly%>" >
                <c:button text="Hitung Ulang" event="recalculateRI" validate="true" />
                <c:button text="Simpan" event="doSaveRI" validate="true" confirm="Do you want to save ?" />
                <c:button text="Cancel" event="doCancel" confirm="Do you want to cancel ?" />
            </c:evaluate>
            <c:evaluate when="<%=readOnly%>" >
                <c:button text="Close" event="doClose"/>
            </c:evaluate>
        </td>

    </table>

</c:frame>