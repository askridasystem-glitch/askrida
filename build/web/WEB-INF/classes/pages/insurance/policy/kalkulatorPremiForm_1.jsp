<%@ page import="com.webfin.gl.form.GLJournalForm,
         com.webfin.gl.model.JournalView,
         com.webfin.gl.model.JournalHeaderView,
         com.crux.util.validation.FieldValidator,
         com.crux.util.*,
         com.webfin.gl.ejb.CostCenterManager,
         com.webfin.ar.model.ARPaymentMethodView,
         com.webfin.ar.forms.ARTitipanPremiForm,
         com.webfin.insurance.form.InsuranceUploadForm,
         com.webfin.insurance.model.kalkulatorPremiHeaderView,
         com.webfin.gl.ejb.CurrencyManager"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="UPLOAD SPREADING" >
    <%

                final InsuranceUploadForm form = (InsuranceUploadForm) request.getAttribute("FORM");

                //final kalkulatorPremiHeaderView header = form.getHeaderKalkulator();

                boolean readOnly = form.isReadOnly();

    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr align="center">
                        <td>
                            <table cellpadding=2 cellspacing=1>
 
                                <tr>
                                    <td>Upload File Excel</td>
                                    <td>:</td>
                                    <td>
                                        <c:field name="kalkulatorReins.stFilePhysic" width="200" caption="Upload File Excel" type="file" thumbnail="true"
                                         readonly="false" />
                                    </td>
                                </tr>
                                <tr align="center">
                                    <td colspan="3"><c:button show="true" text="Proses File" event="uploadExcelKalkulator"/>
                                        <c:evaluate when="<%=!readOnly%>" >
                                            <c:button enabled="<%=form.getDetails2().size()>1%>" text="Hitung Premi" event="recalculatePremi" validate="true" />
                                            <c:button enabled="<%=form.getDetails2().size()>1%>" text="Export to Excel" event="exportToExcelKalkulator" validate="true" />
                                            <c:button text="Download Format Excel" event="downloadFormat" confirm="Do you want to cancel ?" />
                                            <c:button text="Batal" event="doCancel" confirm="Do you want to cancel ?" />
                                        </c:evaluate>
                                    </td>
                                </tr>

                                

                            </table>
                        </td>

                    </tr>

                    <tr>
                        <td>
                            <c:field name="notesindex" hidden="true" type="string"/>
                            <c:listbox name="details2">
                                <c:listcol title="" columnClass="header" >
                                </c:listcol>
                                <c:listcol title="" columnClass="detail" >
                                    
                                </c:listcol>                            
                                <c:listcol title="No" >
                                    <c:field name="details2.[$index$].stOrderNo" width="50" caption="Deskripsi" type="string" readonly="true" />
                                </c:listcol>
                                <c:listcol title="Nama" >
                                    <c:field name="details2.[$index$].stNama" width="200" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Tanggal Lahir" >
                                    <c:field name="details2.[$index$].dtTanggalLahir" width="200" caption="Deskripsi" type="date" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Tanggal Mulai" >
                                    <c:field name="details2.[$index$].dtPeriodeAwal" width="200" caption="Deskripsi" type="date" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Tanggal Akhir" >
                                    <c:field name="details2.[$index$].dtPeriodeAkhir" width="200" caption="Deskripsi" type="date" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Usia" >
                                    <c:field name="details2.[$index$].stUsia" width="50" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                                
                                <c:listcol title="Bulan" >
                                    <c:field name="details2.[$index$].stBulan" width="50" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Lama" >
                                    <c:field name="details2.[$index$].stLama" width="50" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Kategori" >
                                    <c:field name="details2.[$index$].stKategori" lov="VS_KATEGORI_KREDIT" width="150" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Rate (%o)" >
                                    <c:field name="details2.[$index$].dbRate" width="70" caption="Deskripsi" type="money16.2" readonly="false" />
                                </c:listcol>
                                <c:listcol title="TSI" >
                                    <c:field name="details2.[$index$].dbTSI" width="100" caption="Deskripsi" type="money16.2" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Premi" >
                                    <c:field name="details2.[$index$].dbPremi" width="100" caption="Deskripsi" type="money16.2" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Perluasan" >
                                    <c:field name="details2.[$index$].stPerluasan" lov="VS_PERLUASAN KREDIT" width="100" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Premi Perluasan" >
                                    <c:field name="details2.[$index$].dbPremiPerluasan" width="100" caption="Deskripsi" type="money16.2" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Premi Total" >
                                    <c:field name="details2.[$index$].dbPremiTotal" width="100" caption="Deskripsi" type="money16.2" readonly="false" />
                                </c:listcol>
                                

                            </c:listbox>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>

    </table>

</c:frame>