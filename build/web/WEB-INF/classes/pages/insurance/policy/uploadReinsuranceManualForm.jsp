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
<%@ taglib prefix="c" uri="crux" %><c:frame title="UPLOAD SPREADING RI MANUAL" >
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
                                
                                <c:field name="headerReins.stFilePhysic" caption="File Excel Spreading" width="200" type="file" thumbnail="true"
                                         readonly="false" presentation="standard"/>
                                <c:field name="headerReins.stStatus" caption="Keterangan" type="string" width="500"
                                         rows="2" readonly="true" presentation="standard"/>
                                

                            </table>
                        </td>

                    </tr>

                    <%--
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
                                   
                                    <c:field name="detailsreins.[$index$].stTreaty" width="200" mandatory="true" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Keterangan Endorse Objek" >
                                    <c:field name="detailsreins.[$index$].stReinsurerNoteObject" width="400" caption="Keterangan Endorse Objek" type="string"  />
                                </c:listcol>

                            </c:listbox>
                        </td>
                    </tr>
                    --%>
                </table>
            </td>
        </tr>

        <td align=center>
            
                <c:button text="Proses Upload" event="prosesRISpreadingManual" confirm="Yakin ingin di proses ?"/>
                <c:button text="Clear" event="doClose"/>
        </td>

    </table>

</c:frame>