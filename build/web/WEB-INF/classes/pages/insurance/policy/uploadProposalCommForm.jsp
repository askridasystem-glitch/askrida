<%@ page import="com.webfin.gl.form.GLJournalForm,
         com.webfin.gl.model.JournalView,
         com.webfin.gl.model.JournalHeaderView,
         com.crux.util.validation.FieldValidator,
         com.crux.util.*,
         com.webfin.gl.ejb.CostCenterManager,
         com.webfin.ar.model.ARPaymentMethodView,
         com.webfin.ar.forms.ARTitipanPremiForm,
         com.webfin.insurance.form.InsuranceUploadForm,
         com.webfin.insurance.model.UploadHeaderProposalCommView,
         com.webfin.gl.ejb.CurrencyManager"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="UPLOAD PROPOSAL KOMISI" >
    <%

                final InsuranceUploadForm form = (InsuranceUploadForm) request.getAttribute("FORM");

                final UploadHeaderProposalCommView header = form.getHeaderProposal();

                boolean readOnly = form.isReadOnly();

    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="headerProposal.stInsuranceUploadID" width="200" caption="Upload ID" type="string" readonly="true" presentation="standard" />
                                <c:field width="200" lov="LOV_CostCenter" caption="{L-ENGBranch-L}{L-INACabang-L}" name="headerProposal.stCostCenterCode" type="string" mandatory="true" presentation="standard" readonly="<%=header.getStCostCenterCode() != null%>" changeaction="onChangeBranchGroup"/>
                                <tr>
                                    <td>
                                        Tanggal Premi Dibayar
                                    </td>
                                    <td>:</td>
                                    <td>
                                        <c:field width="120" caption="Tanggal Polis" name="headerProposal.dtPeriodeAwal" type="date" readonly="true" />
                                        s/d
                                        <c:field width="120" caption="Tanggal Polis Akhir" name="headerProposal.dtPeriodeAkhir" type="date" readonly="true" />
                                    </td>
                                </tr>
                                <c:field width="200" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroup" caption="{L-ENGPolicy Class-L}{L-INAKelas Polis-L}" name="headerProposal.stPolicyTypeGroupID" type="string" presentation="standard" />
                                <c:field width="200" show="<%=header.getStPolicyTypeGroupID() != null%>" lov="LOV_PolicyType" caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" name="headerProposal.stPolicyTypeID" type="string" presentation="standard" mandatory="true">
                                    <c:lovLink name="polgroup" link="headerProposal.stPolicyTypeGroupID" clientLink="false" />
                                </c:field>
                                <c:field name="headerProposal.dbAmountTotal" width="200" caption="Jumlah Proposal Komisi" type="money16.2" presentation="standard"  readonly="true"/>
                                <c:field name="headerProposal.stDataAmount" width="200" caption="Jumlah Polis" type="string" presentation="standard" readonly="true"/>
                                <c:field name="headerProposal.stNoSuratHutang" width="200" caption="No. SHK" type="string" presentation="standard" readonly="true"/>
                                <c:field name="headerProposal.stKeterangan" width="200" rows="3" caption="Keterangan" type="string" presentation="standard"/>
                                <tr>
                                    <td>
                                        Upload Excel
                                    </td>
                                    <td>:</td>
                                    <td>
                                        <c:field name="headerProposal.stFilePhysic" caption="Upload Excel" type="file" thumbnail="true"
                                                 readonly="false"/>
                                        <c:button text="Konversi" event="uploadExcelProposalComm" validate="false" enabled="true"/>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <td align=center>
                        <c:evaluate when="<%=!form.isViewMode()&&!form.isReverseMode()%>">
                            <c:button text="Del All" confirm="Yakin ingin dihapus semua?" event="onDeleteAll" validate="false" enabled="<%=!readOnly%>"/>
                            <%--<c:button show="<%=form.isCanCreate() && !form.isEditMode()%>" text="Retrieve" event="retrieveProposalComm"/>--%>
                            <c:button show="<%=form.isCanCreate() || form.isApprovalSie()%>" text="Hitung Ulang" event="recalculateProposalComm" validate="true" />
                            <c:button show="<%=form.isCanCreate() || form.isApprovalSie()%>" text="Simpan" event="doSaveProposalComm" validate="true" confirm="Do you want to save ?" />
                            <c:button show="<%=form.isCanCreate() || form.isApprovalSie()%>" text="Cancel" event="doCancel" confirm="Do you want to cancel ?" />
                            <c:button show="<%=form.isApprovalCab() || form.isApprovalSie() || form.isApprovalBag() || form.isApprovalDiv()%>" text="Setujui" event="approvedProposalComm" validate="true" confirm="Yakin ingin disetujui ?" />
                            <c:button show="<%=form.isApprovalCab() || form.isApprovalSie() || form.isApprovalBag() || form.isApprovalDiv()%>" text="Close" event="doClose"/>
                        </c:evaluate>
                        <c:evaluate when="<%=form.isViewMode()%>">
                            <c:button text="Close" event="doClose"/>
                        </c:evaluate>
                        <c:evaluate when="<%=form.isReverseMode()%>">
                            <c:button show="<%=form.isApprovalCab() || form.isApprovalSie()%>" text="Reverse" event="doReverseProposalComm" validate="true" confirm="Yakin ingin di-Reverse ?" />
                            <c:button text="Close" event="doClose"/>
                        </c:evaluate>
                    </td>

                    <tr>
                        <td>
                            <c:field name="notesindex" hidden="true" type="string"/>
                            <c:listbox name="detailsproposal"  paging="true" >
                                <%--
                                                                <c:listcol title="" columnClass="header">
                                                                    <c:button text="+" event="onNewProposal" validate="false" defaultRO="true"/>
                                                                </c:listcol>--%>
                                <%--
                                <c:listcol title="" columnClass="header" >
                                    <c:button text="+" clientEvent="addnewinvoice()" validate="false" enabled="true"/>
                                </c:listcol>--%>
                                <c:listcol title="" columnClass="header" >
                                </c:listcol>
                                <c:listcol title="" columnClass="detail" >
                                    <c:button text="-" event="delLine3" clientEvent="f.notesindex.value='$index$';" validate="false" enabled="<%=!readOnly%>"/>
                                </c:listcol>
                                <c:listcol title="KaOps" >
                                    <c:field name="detailsproposal.[$index$].stStatus1" width="140" caption="Deskripsi" type="check" readonly="true" />
                                </c:listcol>
                                <c:listcol title="Kasie" >
                                    <c:field name="detailsproposal.[$index$].stStatus2" width="140" caption="Deskripsi" type="check" readonly="true" />
                                </c:listcol>
                                <c:listcol title="Kabag" >
                                    <c:field name="detailsproposal.[$index$].stStatus3" width="140" caption="Deskripsi" type="check" readonly="true" />
                                </c:listcol>
                                <c:listcol title="Kadiv" >
                                    <c:field name="detailsproposal.[$index$].stStatus4" width="140" caption="Deskripsi" type="check" readonly="true" />
                                </c:listcol><%--
                                <c:listcol title="Nomor Polis" >
                                    <c:field name="detailsproposal.[$index$].stARInvoiceID" descfield="detailsproposal.[$index$].stPolicyNo" width="140" mandatory="true" caption="Deskripsi" type="string"
                                             lov="LOV_ProposalComm" popuplov="true" clientchangeaction="selectEntity($index$)">
                                        <c:param name="param" value="<%=header.getStCostCenterCode() + "|" + header.getStPolicyTypeID()%>"/>
                                    </c:field>
                                </c:listcol>
                                <c:listcol title="Nama" >
                                    <c:field name="detailsproposal.[$index$].stTertanggung" width="200" caption="Deskripsi" type="string" mandatory="true" />
                                </c:listcol>
                                <c:listcol title="TSI" >
                                    <c:field name="detailsproposal.[$index$].dbAmount" width="100" caption="Deskripsi" type="money16.2" mandatory="true" />
                                </c:listcol>--%>
                                <c:listcol title="ID Polis" >
                                    <c:field name="detailsproposal.[$index$].stARInvoiceID" width="60" mandatory="true" caption="Deskripsi" type="string" readonly="true"/>
                                </c:listcol>
                                <c:listcol title="Nomor Polis" >
                                    <c:field name="detailsproposal.[$index$].stPolicyNo" width="140" mandatory="true" caption="Deskripsi" type="string" readonly="true"/>
                                </c:listcol>
                                <c:listcol title="Keterangan" >
                                    <c:field name="detailsproposal.[$index$].stInvoiceNo" width="250" mandatory="true" caption="Deskripsi" type="string" readonly="true" />
                                </c:listcol>
                                <c:listcol title="Nama" >
                                    <c:field name="detailsproposal.[$index$].stTertanggung" width="200" caption="Deskripsi" type="string" readonly="true" />
                                </c:listcol>
                                <c:listcol title="TSI" >
                                    <c:field name="detailsproposal.[$index$].dbAmount" width="100" caption="Deskripsi" type="money16.2" readonly="true" />
                                </c:listcol>
                            </c:listbox>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    <iframe src="" id=frmx width=1 height=1></iframe>

</c:frame>
<script>
    function addnewinvoice() {
        openDialog('c.ctl?EVENT=AR_INVOICE_SEARCH_PROPOSAL'
            +'&cc_code='+docEl('headerProposal.stCostCenterCode').value
            +'&start_date='+docEl('headerProposal.dtPeriodeAwal').value
            +'&end_date='+docEl('headerProposal.dtPeriodeAkhir').value,
        600,400,
        function (o) {
            if (o!=null) {
                //alert('invoice_id'+o.INVOICE_ID_COMM);
                f.notesindex.value=o.INVOICE_ID_COMM;
                f.action_event.value='onNewInvoiceProposalComm';
                f.submit();
            }
        }
    );
    }
    <%--
        function selectEntity(i) {
            var o = window.lovPopResult;
            document.getElementById('detailsproposal.[' + i + '].stTertanggung').value = o.attr_pol_name;
            document.getElementById('detailsproposal.[' + i + '].dbAmount').value = o.claim_no;
        }
    --%>
</script>