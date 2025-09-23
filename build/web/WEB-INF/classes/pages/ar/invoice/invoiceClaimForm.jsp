<%@ page import="com.webfin.insurance.form.InvoiceClaimForm,
         com.webfin.insurance.model.InsurancePolicyInwardView,
         com.webfin.ar.model.ARTransactionTypeView,
         com.crux.util.Tools,
         com.crux.util.BDUtil,
         com.webfin.gl.ejb.CurrencyManager,
         com.crux.web.controller.SessionManager,
         com.webfin.insurance.model.InsurancePolicyInwardDetailView,
         com.webfin.ar.model.ARTaxView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="TRANSACTION" >
    <%
                final InvoiceClaimForm form = (InvoiceClaimForm) request.getAttribute("FORM");

                final InsurancePolicyInwardView invoice = form.getInvoice();

                final boolean hasTRX = invoice.getStARTransactionTypeID() != null;

                final ARTransactionTypeView artrx = invoice.getARTrxType();

                final boolean superType = artrx != null && artrx.isSuperType();

                final boolean enablePolicyType = form.trxEnablePolType();
                final boolean enablePolis = form.trxEnablePolis();
                final boolean enableUWrit = form.trxEnableUWrit();
                final boolean enableFixedItem = form.trxEnableFixedItem();
                final boolean disableDetail = form.trxDisableDetail();
                final boolean custIns = artrx != null && artrx.trxCustIns();
                final boolean enableReins = form.trxEnableReins();
                final boolean enableXOL = form.trxEnableXOL();

                final boolean amountRO = !disableDetail;
                final boolean hasDetail = !disableDetail;

                final boolean hasAmountSettled = Tools.compare(invoice.getDbAmountSettled(), BDUtil.zero) > 0;

                final String masterCCY = CurrencyManager.getInstance().getMasterCurrency();

                final boolean forex = invoice.getStCurrencyCode() != null && !Tools.isEqual(masterCCY, invoice.getStCurrencyCode());

                final boolean isApprovedMode = form.isApprovedMode();
                final boolean enableAttachment = artrx.trxEnableAttachment();
                boolean isDLA = false;

                if (invoice.getStClaimStatus() != null) {
                    isDLA = invoice.getStClaimStatus().equalsIgnoreCase("DLA");
                }

                boolean canCreateExGratia = artrx.getStARTrxTypeID().equalsIgnoreCase("24");
    %>
    <c:evaluate when="<%=superType%>">
        <table cellpadding=2 cellspacing=1>
            <tr>
                <td>
                    <c:field changeaction="onChangeTRXType" readonly="false" lov="lovARTrxType" name="invoice.stARTransactionTypeID" width="200" caption="TRANSACTION TYPE" type="string" presentation="standard" mandatory="true" />
                </td>
            </tr>
        </table>
    </c:evaluate>
    <c:evaluate when="<%=!superType%>">
        <table cellpadding=2 cellspacing=1>
            <tr>
                <td>
                    <table cellpadding=2 cellspacing=1>

                        <tr>
                            <td valign=top>
                                <table cellpadding=2 cellspacing=1>
                                    <c:field changeaction="onChangeTRXType" readonly="<%=hasTRX%>" lov="LOV_ARTrxType" name="invoice.stARTransactionTypeID" width="400" caption="TRANSACTION TYPE" type="string" presentation="standard" mandatory="true" />
                                    <c:field name="invoice.stInvoiceType" caption="Invoice Type" type="string" presentation="standard" lov="LOV_InvoiceType" readonly="true" />
                                    <c:field name="invoice.stInvoiceNo" width="200" caption="NO BUKTI" type="string|64" presentation="standard" readonly="true" />

                                    <c:field name="invoice.stClaimStatus" width="200" caption="Status Claim" type="string|64" presentation="standard" mandatory="true" readonly="true" />
                                    <c:field name="invoice.stPLANo" width="200" caption="No. PLA" type="string|64" presentation="standard" mandatory="true" />
                                    <c:field name="invoice.dtPLADate" caption="Tanggal PLA" type="date" presentation="standard" mandatory="true" />
                                    <c:field include="<%=isDLA%>" name="invoice.stDLANo" width="200" caption="No. DLA" type="string|64" presentation="standard" mandatory="true" />
                                    <c:field include="<%=isDLA%>" name="invoice.dtDLADate" caption="Tanggal DLA" type="date" presentation="standard" mandatory="true" />

                                    <c:evaluate when="<%=hasTRX%>">

                                        <c:evaluate when="<%=!canCreateExGratia%>">
                                            <c:field lov="LOV_TreatyType" width="200" name="invoice.stRefID0" caption="Jenis Treaty" type="string" presentation="standard" mandatory="true" />
                                        </c:evaluate>

                                        <c:field include="<%=enablePolicyType%>" lov="LOV_PolicyType3" width="200" name="invoice.stAttrPolicyTypeID" caption="Jenis Polis" changeaction="changeBranch" type="string" presentation="standard" mandatory="true" />
                                        <c:field lov="LOV_CostCenter" name="invoice.stCostCenterCode" width="200" caption="Cabang" type="string|64" presentation="standard" mandatory="true" />

                                        <c:evaluate when="<%=!canCreateExGratia%>">
                                            <tr>
                                                <td>Reasuransi</td>
                                                <td>:</td>
                                                <td>
                                                    <c:field name="invoice.stEntityID" type="string" hidden="true" />
                                                    <c:field width="300" caption="Customer" name="invoice.stEntityName" type="string" mandatory="true" readonly="true" />
                                                    <c:button text="..." clientEvent="selectCustomer()" />
                                                </td>
                                            </tr>
                                        </c:evaluate>
                                        <%--
                                        <c:field width="350" caption="Tertanggung" name="invoice.stCustomerName" presentation="standard" type="string" mandatory="true" />
                                        --%>
                                        <c:field changeaction="onChangeCurrency" name="invoice.stCurrencyCode" caption="CURRENCY" type="string" presentation="standard" lov="LOV_Currency" mandatory="true" />
                                        <c:field readonly="<%=!forex%>" name="invoice.dbCurrencyRate" caption="Currency Rate" type="money16.2" presentation="standard" mandatory="true" />
                                        <%--<c:field width="200" name="invoice.stFilePhysic" type="file" thumbnail="true" caption="{L-ENG File Attachment-L}{L-INA File Lampiran-L}" presentation="standard" />--%>
                                        <c:field name="invoice.dtDueDate" caption="Due Date" type="date" presentation="standard" mandatory="true" />
                                       
                                    </c:evaluate>
                                </table>
                            </td>
                            <td>
                                <table cellpadding=2 cellspacing=1>
                                    <c:field name="invoice.stAttrPolicyNo" width="200" caption="No. Polis" type="string|64" presentation="standard" mandatory="true" />
                                    <c:field name="invoice.stClaimNo" width="200" caption="No. Konfirmasi Klaim" type="string|64" presentation="standard" mandatory="true" />
                                    <c:field name="invoice.stAttrUnderwriting" width="200" caption="U/W Year" type="string|64" presentation="standard" mandatory="true" />

                                    <c:field name="invoice.stAttrPolicyName" width="300" caption="Tertanggung" type="string|255" presentation="standard" mandatory="true" />
                                    <c:field name="invoice.stAttrPolicyAddress" width="300"  rows="2" caption="Alamat" type="string|255" presentation="standard" mandatory="true" />

                                    <c:field name="invoice.dtAttrPolicyPeriodStart" caption="Periode Awal" type="date" presentation="standard" mandatory="true" />
                                    <c:field name="invoice.dtAttrPolicyPeriodEnd" caption="Periode Akhir" type="date" presentation="standard" mandatory="true" />

                                    <c:field name="invoice.stPolicyCurrencyCode" caption="Currency Polis" width="150" type="string" presentation="standard" lov="LOV_Currency" mandatory="true" />
                                    <c:field name="invoice.dbAttrPolicyTSITotal" width="200" caption="Total Sum Insured" type="money16.2" presentation="standard" mandatory="true" />
                                    <c:field name="invoice.dbAttrPolicyTSI" width="200" caption="Sum Insured" type="money16.2" presentation="standard" mandatory="true" />
                                    <c:field name="invoice.dtReference2" caption="Date of Loss" type="date" presentation="standard" mandatory="true" />

                                    <c:field name="invoice.stReferenceX1" rows="3" width="400" caption="Cause Of Loss" type="string|64" presentation="standard" mandatory="true" />
                                    <c:field name="invoice.dbClaimAmountTotal" width="200" caption="Total Jumlah Klaim(100%)" type="money16.2" presentation="standard" mandatory="true" />
                                    <c:field width="200" name="invoice.stFilePhysic" type="file" thumbnail="true" caption="Dokumen" presentation="standard" />
                                    <c:field width="200" name="invoice.stFilePhysic2" type="file" thumbnail="true" caption="Persetujuan Divisi" presentation="standard" />
                                    <c:field width="200" name="invoice.stFilePhysic3" type="file" thumbnail="true" caption="Persetujuan Direksi" presentation="standard" />

                                    <%--
                                   <c:evaluate when="<%=hasTRX%>">
                                      <c:evaluate when="<%=enablePolis%>">
                                         <c:field name="invoice.stAttrPolicyNo" width="200" caption="Policy Number" type="string|32" presentation="standard" mandatory="true" />

                                         <c:field name="invoice.stAttrPolicyName" width="300" caption="Risk Name" type="string|255" presentation="standard" mandatory="true" />
                                         <c:field name="invoice.stAttrPolicyAddress" width="300"  rows="2" caption="Risk Address" type="string|255" presentation="standard" mandatory="true" />
                                         <c:field name="invoice.dbAttrPolicyTSITotal" caption="Total Sum Insured" type="money16.2" presentation="standard" mandatory="true" />
                                         <c:field name="invoice.dbAttrPolicyTSI" caption="Sum Insured" type="money16.2" presentation="standard" mandatory="true" />
                                      </c:evaluate>
                                      <c:evaluate when="<%=invoice.getARInvoiceFF()!=null%>">
                                           <c:flexfield ffid="<%=invoice.getARInvoiceFF().getStFlexFieldHeaderID()%>" prefix="invoice."/>
                                       </c:evaluate>
                                      <c:evaluate when="<%=enableUWrit%>">
                                         <c:field name="invoice.stAttrQuartal" caption="Quartal" type="string|32" presentation="standard" mandatory="true" />
                                         <c:field name="invoice.stAttrUnderwriting" caption="Underwriting Year" type="string|32" presentation="standard" mandatory="true" />
                                      </c:evaluate>
                                      <c:evaluate when="<%=enableReins%>">
                                         <c:field name="invoice.stRefID0" caption="Treaty" type="string|32" presentation="standard" lov="LOV_TreatyType" mandatory="true" />
                                      </c:evaluate>
                                      <c:evaluate when="<%=enableXOL%>">
                                          <c:field lov="LOV_XOLLayer" width="100" name="invoice.stReferenceA0" caption="XOL Layer" type="string" presentation="standard" mandatory="true" />
                                          <c:field lov="LOV_XOLType" width="150" name="invoice.stReferenceA1" caption="XOL Type" type="string" presentation="standard" mandatory="true" />
                                      </c:evaluate>
                                  </c:evaluate>
                                    --%>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <c:field name="itemIndex" type="string" hidden="true" />
                    <c:evaluate when="<%=hasTRX && hasDetail%>">
                        <c:listbox name="details">
                            <%
                                        final InsurancePolicyInwardDetailView ivx = (InsurancePolicyInwardDetailView) current;

                                        final boolean negative = ivx != null && ivx.getTrxLine() != null && ivx.getTrxLine().isNegative();
                            %>
                            <c:listcol title="" columnClass="header">
                                <c:evaluate when="<%=!enableFixedItem%>">
                                    <c:button text="+" event="onNewItem" validate="false" />
                                </c:evaluate>
                            </c:listcol>
                            <c:listcol title="" columnClass="detail">
                                <c:evaluate when="<%=!enableFixedItem%>">
                                    <c:button text="-" validate="false" event="onDeleteItem" clientEvent="f.itemIndex.value=$index$" />
                                              </c:evaluate>
                                              </c:listcol>
                                              <c:evaluate when="<%=!enableFixedItem%>">
                                              <c:listcol title="Account">
                                            <c:field name="details.[$index$].stGLAccountID" hidden="true"/>
                                            <c:field name="details.[$index$].stGLAccountDesc" caption="Account" type="string" readonly="true" width="300" />
                                            <c:button text="..." clientEvent="<%="pop('x.frx'," + index + ")"%>" validate="false" />
                                        </c:listcol>
                                </c:evaluate>
                                <c:evaluate when="<%=!enableXOL%>">
                                    <c:listcol title="Description">
                                        <c:field name="details.[$index$].stDescription" caption="Account" type="string" readonly="<%=enableFixedItem%>" />
                                    </c:listcol>
                                </c:evaluate>
                                <c:listcol title="+/-" align="center" >
                                    <%=negative ? "-" : "+"%>
                                </c:listcol>
                                <c:listcol title="<%="Amount(" + invoice.getStCurrencyCode() + ")"%>">
                                    <c:field name="details.[$index$].dbEnteredAmount" caption="Amount" type="money16.2" mandatory="true" />
                                </c:listcol>
                                <c:evaluate when="<%=forex%>">
                                    <c:listcol title="<%="Amount(" + masterCCY + ")"%>">
                                        <c:field name="details.[$index$].dbAmount" caption="Amount" type="money16.2" readonly="true" />
                                    </c:listcol>
                                </c:evaluate>
                                <c:evaluate when="<%=enableFixedItem%>">
                                    <c:listcol title="Account">
                                        <c:field name="details.[$index$].stGLAccountDesc" caption="Account" type="string" readonly="true" width="400" />
                                    </c:listcol>
                                </c:evaluate>
                                <%
                                            if (index.intValue() >= 0) {
                                                final InsurancePolicyInwardDetailView ivd = (InsurancePolicyInwardDetailView) form.getDetails().get(index.intValue());
                                %>
                                <c:evaluate when="<%=ivd.isTaxed()%>" >
                                    <%
                                                                                    final ARTaxView tax = ivd.getTax();
                                    %>
                            </tr>
                            <tr class=row0>
                                <td></td>
                                <c:evaluate when="<%=!enableFixedItem%>">
                                    <td></td>
                                    <td></td>
                                </c:evaluate>
                                <td><%=jspUtil.print(tax.getStDescription())%></td>
                                <td align=right>(<%=jspUtil.print(ivd.getDbTaxAmount(), 2)%>)</td>
                                <c:evaluate when="<%=forex%>">
                                    <td></td>
                                </c:evaluate>
                                <c:evaluate when="<%=enableFixedItem%>">
                                    <td></td>
                                </c:evaluate>
                            </c:evaluate>
                            <% }%>
                    </c:listbox>
                </c:evaluate>
                </td>
            </tr>
            <tr>
                <td>
                    <table cellpadding=2 cellspacing=1>
                        <tr>
                            <td>
                                <table cellpadding=2 cellspacing=1>
                                    <c:evaluate when="<%=hasTRX%>">
                                        <c:field name="invoice.dbEnteredAmount" mandatory="true" caption="<%="Amount(" + invoice.getStCurrencyCode() + ")"%>" type="money16.2" presentation="standard" readonly="<%=amountRO%>" />
                                        <c:evaluate when="<%=forex%>">
                                            <c:field name="invoice.dbAmount" caption="<%="Amount(" + masterCCY + ")"%>" type="money16.2" presentation="standard" readonly="true" />
                                        </c:evaluate>
                                        <c:field include="<%=hasAmountSettled%>" name="invoice.dbAmountSettled" caption="SETTLED" type="money16.2" presentation="standard" readonly="true" />
                                        <c:field name="invoice.stDescription" caption="Remarks" rows="3" type="string" presentation="standard" width="500" />
                                        <%--
                                        <c:field name="invoice.stPostedFlag" caption="POSTED" type="check" presentation="standard"/>
                                        <c:field name="invoice.stCancelFlag" caption="Cancelled" type="check" presentation="standard" readonly="true" />
                                        <c:field name="invoice.stCommitFlag" caption="Committed" type="check" presentation="standard" readonly="true" />
                                        <c:field name="invoice.stApprovedFlag" caption="Approved" type="check" presentation="standard" readonly="true" />
                                        --%>
                                    </c:evaluate>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    
                    <c:evaluate when="<%=hasTRX && !isApprovedMode && !form.isReverseMode()%>">
                        <c:button text="Hitung Ulang" event="recalculate" validate="false" />
                        <c:button text="Simpan" event="clickSave" validate="true" />
                    </c:evaluate>
                    <c:evaluate when="<%=isApprovedMode%>">
                        <c:button text="Setujui" event="clickSave" validate="true" />
                    </c:evaluate>
                    <c:evaluate when="<%=form.isReverseMode()%>">
                        <c:button text="Reverse" event="clickReverse" validate="true" />
                    </c:evaluate>
                    
                    <c:button text="Batal" event="clickCancel" validate="false" />
                </td>
            </tr>
        </table>
        <script>
            function pop2() {
                openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT', 400,400,
                function (o) {
                    if (o!=null) {
                        docEl('invoice.stGLARAccountID').value=o.acid;
                        docEl('invoice.stGLARAccountDesc').value=o.acdesc;
                    }
                }
            );
            }

            var idx;
            function pop(formName,i) {
                idx=i;
                openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT', 400,400,selectARAccountDet);
            }

            function selectARAccountDet(o) {
                if (o!=null) {
                    docEl('details.['+idx+'].stGLAccountID').value=o.acid;
                    docEl('details.['+idx+'].stGLAccountDesc').value=o.acdesc;
                }
            }
        </script>
    </c:evaluate>
</c:frame>
<script>
    function selectCustomer(i) {
        openDialog('entity_search.crux', 400,400,
        function (o) {
            if (o!=null) {
                document.getElementById('invoice.stEntityID').value=o.stEntityID;
                document.getElementById('invoice.stEntityName').value=o.stEntityName;
            }
        }
    );
    }

    function selectTertanggung(i) {
        openDialog('entity_search.crux', 400,400,
        function (o) {
            if (o!=null) {
                document.getElementById('invoice.stCustomerID').value=o.stEntityID;
                document.getElementById('invoice.stCustomerName').value=o.stEntityName;
            }
        }
    );
    }
</script>