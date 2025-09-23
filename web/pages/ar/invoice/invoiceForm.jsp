<%@ page import="com.webfin.insurance.form.InvoiceForm,
         com.webfin.insurance.model.InsurancePolicyInwardView,
         com.webfin.ar.model.ARTransactionTypeView,
         com.crux.util.Tools,
         com.crux.util.BDUtil,
         com.webfin.gl.ejb.CurrencyManager,
         com.webfin.insurance.model.InsurancePolicyInwardDetailView,
         com.webfin.ar.model.ARTaxView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="TRANSACTION" >
    <%
                final InvoiceForm form = (InvoiceForm) request.getAttribute("FORM");

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
                final boolean enableReinsured = form.trxEnableReinsured();

                final boolean amountRO = !disableDetail;
                final boolean hasDetail = !disableDetail;

                final boolean hasAmountSettled = Tools.compare(invoice.getDbAmountSettled(), BDUtil.zero) > 0;

                final String masterCCY = CurrencyManager.getInstance().getMasterCurrency();

                final boolean forex = invoice.getStCurrencyCode() != null && !Tools.isEqual(masterCCY, invoice.getStCurrencyCode());

                final boolean isApprovedMode = form.isApprovedMode();
                final boolean enableAttachment = artrx.trxEnableAttachment();
    %>
    <c:evaluate when="<%=superType%>">
        <table cellpadding=2 cellspacing=1>
            <tr>
                <td>
                    <c:field changeaction="onChangeTRXType" readonly="false" lov="lovARTrxType" name="invoice.stARTransactionTypeID" width="200" caption="Transaction Type" type="string" presentation="standard" mandatory="true" />
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
                                    <c:field changeaction="onChangeTRXType" readonly="<%=hasTRX%>" lov="LOV_ARTrxType" name="invoice.stARTransactionTypeID" width="400" caption="Transaction Type" type="string" presentation="standard" mandatory="true" />
                                    <c:evaluate when="<%=hasTRX%>">
                                        <c:field include="<%=enablePolicyType%>" lov="LOV_PolicyTypeInward" width="200" name="invoice.stAttrPolicyTypeID" caption="Policy Type" changeaction="changeBranch" type="string" presentation="standard" mandatory="true" />
                                        <c:field lov="LOV_CostCenter" name="invoice.stCostCenterCode" width="200" caption="Cost Center" type="string|64" presentation="standard" mandatory="true" />
                                        <c:field name="invoice.stInvoiceNo" width="200" caption="No Bukti" type="string|64" presentation="standard" readonly="true" />
                                        <c:field name="invoice.stTransactionNoReference" width="200" caption="Transaction No" type="string|64" presentation="standard" mandatory="true" />
                                        <c:field name="invoice.dtInvoiceDate" caption="Transaction Date" type="date" presentation="standard" mandatory="true" />
                                        <c:field name="invoice.dtMutationDate" caption="Mutation Date" type="date" presentation="standard" mandatory="true" />
                                        <tr>
                                            <td>Customer</td>
                                            <td>:</td>
                                            <td>
                                                <c:field name="invoice.stEntityID" type="string" hidden="true" />
                                                <c:field width="300" caption="Customer" name="invoice.stEntityName" type="string" mandatory="true" readonly="true" />
                                                <c:button text="..." clientEvent="selectCustomer()" />
                                            </td>
                                        </tr>
                                        <c:evaluate when="<%=enableReinsured%>">
                                            <c:field lov="LOV_EntityOnly" popuplov="true" name="invoice.stReinsuranceEntityID" caption="Reinsured" type="string" presentation="standard" width="300" mandatory="true" />
                                        </c:evaluate>
                                        <c:field name="invoice.stInvoiceType" caption="Invoice Type" type="string" presentation="standard" lov="LOV_InvoiceType" readonly="true" />
                                        <c:field name="invoice.dtDueDate" caption="Due Date" type="date" presentation="standard" mandatory="true" />
                                        <c:field changeaction="onChangeCurrency" width="150"name="invoice.stCurrencyCode" caption="Currency" type="string" presentation="standard" lov="LOV_Currency" mandatory="true" />
                                        <c:field readonly="<%=!forex%>" name="invoice.dbCurrencyRate" caption="Currency Rate" type="money16.2" presentation="standard" mandatory="true" />
                                        <c:evaluate when="<%=enableAttachment%>">
                                            <c:field width="200" name="invoice.stFilePhysic" type="file" thumbnail="true" caption="{L-ENG File Attachment-L}{L-INA File Lampiran-L}" presentation="standard" />
                                        </c:evaluate>

                                    </c:evaluate>
                                </table>
                            </td>
                            <td>
                                <table cellpadding=2 cellspacing=1>
                                    <c:evaluate when="<%=hasTRX%>">
                                        <c:evaluate when="<%=enablePolis%>">
                                            <c:field name="invoice.stAttrPolicyNo" width="200" caption="Policy Number" type="string|32" presentation="standard" mandatory="true" />
                                            <c:field name="invoice.dtAttrPolicyPeriodStart" caption="Period Start" type="date" presentation="standard" mandatory="true" />
                                            <c:field name="invoice.dtAttrPolicyPeriodEnd" caption="Period End" type="date" presentation="standard" mandatory="true" />
                                            <c:field name="invoice.stAttrPolicyName" width="300" rows="2" caption="Risk Name" type="string|255" presentation="standard" mandatory="true" />
                                            <c:field name="invoice.stAttrPolicyAddress" width="300"  rows="3" caption="Risk Address" type="string|255" presentation="standard" mandatory="true" />
                                            <c:field name="invoice.dbAttrPolicyTSITotal" width="200" caption="Total Sum Insured" type="money16.2" presentation="standard" mandatory="true" />
                                            <c:field name="invoice.dbAttrPolicyTSI" width="200" caption="Share Askrida" type="money16.2" presentation="standard" mandatory="true" />
                                        </c:evaluate>
                                        <c:evaluate when="<%=invoice.getARInvoiceFF() != null%>">
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
                                        <c:field width="200" name="invoice.stAttachment1" type="file" thumbnail="true" caption="Dokumen Email" presentation="standard" />
                                        <c:field width="200" name="invoice.stAttachment2" type="file" thumbnail="true" caption="{L-ENG File Attachment-L}{L-INA File Lampiran-L} 2" presentation="standard" />
                                        <c:field width="200" name="invoice.stAttachment3" type="file" thumbnail="true" caption="{L-ENG File Attachment-L}{L-INA File Lampiran-L} 3" presentation="standard" />

                                    </c:evaluate>
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


                    <c:tab name="tabs">
                        <c:tabpage name="TAB1">
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
                                                  <%--<c:evaluate when="<%=!enableFixedItem%>">
                                                  <c:listcol title="Account">
                                                <c:field name="details.[$index$].stGLAccountID" hidden="true"/>
                                                <c:field name="details.[$index$].stGLAccountDesc" caption="Account" type="string" readonly="true" width="300" />
                                                <c:button text="..." clientEvent="<%="pop('x.frx'," + index + ")"%>" validate="false" />
                                            </c:listcol>
                                    </c:evaluate> --%>
                                    <c:evaluate when="<%=!enableXOL%>">
                                        <c:listcol title="Description">
                                            <c:field name="details.[$index$].stDescription" caption="Account" type="string" width="200" readonly="<%=enableFixedItem%>" />
                                        </c:listcol>
                                    </c:evaluate>
                                    <c:listcol title="+/-" align="center" >
                                        <%=negative ? "-" : "+"%>
                                    </c:listcol>
                                    <c:listcol title="<%="Amount(" + invoice.getStCurrencyCode() + ")"%>">
                                        <c:field name="details.[$index$].dbEnteredAmount" width="200" caption="Amount" type="money16.2" mandatory="true" />
                                    </c:listcol>
                                    <c:evaluate when="<%=forex%>">
                                        <c:listcol title="<%="Amount(" + masterCCY + ")"%>">
                                            <c:field name="details.[$index$].dbAmount" width="200" caption="Amount" type="money16.2" readonly="true" />
                                        </c:listcol>
                                    </c:evaluate>
                                    <%--<c:listcol title="<%="Tax("+invoice.getStCurrencyCode()+")"%>">
                                             <c:field name="details.[$index$].dbTaxAmount" caption="Amount" type="money16.2" mandatory="true" />
                                          </c:listcol>--%>
                                    <c:evaluate when="<%=enableFixedItem%>">
                                        <%--<c:listcol title="Account Code">
                                                <c:field name="details.[$index$].stGLAccountCodeTR" caption="Account" type="string" readonly="true" width="100" />
                                             </c:listcol>--%>
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
                        </c:tabpage>
                        <c:tabpage name="TAB2">
                        <table cellpadding=2 cellspacing=1>
                            <c:field caption="Pilihan" lov="LOV_INSTALLMENT_OPTIONS"
                                         name="invoice.stInstallmentOptions" width="250" type="string" mandatory="false"
                                         presentation="standard"/>
                            <c:field caption="Jumlah Hari"
                                     name="invoice.stInstallmentDaysAmount" width="80" type="string" mandatory="false" suffix=" days"
                                         presentation="standard"/>
                            <c:field caption="{L-ENGInstallment Periods-L}{L-INAJumlah Cicilan-L}" name="invoice.stInstallmentPeriods"
                                         width="50" type="integer" readonly="true" mandatory="true" presentation="standard"/>
                             <c:field caption="{L-ENGInstallment Step-L}{L-INAJarak Cicilan-L}" lov="LOV_InsurancePeriod"
                                         name="invoice.stInstallmentPeriodID" width="200" type="string" mandatory="true"
                                         presentation="standard"/>
                             <c:field caption="Cicilan Manual"
                                         name="invoice.stInstallmentManualFlag" width="200" type="check" mandatory="false"
                                         presentation="standard"/>

                        </table>

                                <table cellpadding=2 cellspacing=1 class=header>
                                    <tr>
                                        <td align=center> {L-ENGSimulated Installment-L}{L-INASimulasi Cicilan-L}</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <c:field name="instIndex" type="string" hidden="true"/>
                                            <c:listbox name="installment">
                                                <c:listcol title="" columnClass="header">
                                                    <c:button text="+" event="onNewInstallment" validate="false" defaultRO="true"/>
                                                </c:listcol>
                                                <c:listcol title="" columnClass="detail">
                                                    <c:button text="-" event="onDeleteInstallment" clientEvent="f.instIndex.value='$index$';"
                                                              validate="false" defaultRO="true"/>
                                                </c:listcol>

                                                <c:listcol title=""><%=index.intValue() + 1%>
                                                </c:listcol>

                                                <c:listcol title="{L-ENGDue Date-L}{L-INATanggal Tagihan-L}">
                                                    <c:field name="installment.[$index$].dtDueDate" type="date" mandatory="false" readonly="false"/>
                                                </c:listcol>
                                                <c:listcol title="{L-ENGAmount-L}{L-INAJumlah-L}">
                                                    <c:field name="installment.[$index$].dbAmount" type="money16.2" mandatory="false"
                                                             readonly="false"/>
                                                </c:listcol>
                                                <c:listcol title="Premi Bruto">
                                                    <c:field name="installment.[$index$].dbPremiBruto" type="money16.2" mandatory="false"
                                                             readonly="false"/>
                                                </c:listcol>
                                                <c:listcol title="Komisi">
                                                    <c:field name="installment.[$index$].dbKomisi" type="money16.2" mandatory="false"
                                                             readonly="false"/>
                                                </c:listcol>
                                                <c:listcol title="Fee">
                                                    <c:field name="installment.[$index$].dbFee" type="money16.2" mandatory="false"
                                                             readonly="false"/>
                                                </c:listcol>
                                            </c:listbox>
                                        </td>
                                    </tr>
                                </table>
                                            <br><br>
                               <table cellpadding=2 cellspacing=1 class=header>
                                    <tr>
                                        <td align=center> {L-ENGPayment Status-L}{L-INAStatus Pembayaran-L}</td>
                                    </tr>
                                    <tr>
                                        <td>

                                            <c:listbox name="invoices">
                                                <c:listcol title=""><%=index.intValue() + 1%>
                                                </c:listcol>

                                                 <c:listcol title="{L-ENGInvoice No-L}{L-INANo Bukti -L}">
                                                    <c:field name="invoices.[$index$].stInvoiceNo" type="string" mandatory="false" readonly="true"/>
                                                </c:listcol>
                                                <c:listcol title="{L-ENGDue Date-L}{L-INATgl Jatuh Tempo-L}">
                                                    <c:field name="invoices.[$index$].dtDueDate" type="date" mandatory="false" readonly="false"/>
                                                </c:listcol>
                                                <c:listcol title="Jumlah Tagihan">
                                                    <c:field name="invoices.[$index$].dbAmount" type="money16.2" mandatory="false"
                                                             readonly="false"/>
                                                </c:listcol>
                                                <c:listcol title="{L-ENGReceipt No-L}{L-INANo Bukti Bayar-L}">
                                                    <c:field name="invoices.[$index$].stReceiptNo" type="string" width="200" mandatory="false" readonly="true"/>
                                                </c:listcol>
                                                <c:listcol title="{L-ENGPayment date-L}{L-INATanggal Bayar-L}">
                                                    <c:field name="invoices.[$index$].dtReceipt" type="date" mandatory="false"
                                                             readonly="false"/>
                                                </c:listcol>
                                                <c:listcol title="Jumlah Bayar">
                                                    <c:field name="invoices.[$index$].dbAmountSettled" type="money16.2" mandatory="false"
                                                             readonly="false"/>
                                                </c:listcol>
                                            </c:listbox>
                                        </td>
                                    </tr>
                                </table>
                                
                        </c:tabpage>
                      </c:tab>
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
                                        <c:field name="invoice.stDescription" caption="Description" rows="3" type="string" presentation="standard" width="500" />
                                        <%--<c:field name="invoice.stPostedFlag" caption="POSTED" type="check" presentation="standard"/>
                                        <c:field name="invoice.stCancelFlag" caption="Cancelled" type="check" presentation="standard" readonly="true" />
                                        <c:field name="invoice.stCommitFlag" caption="Committed" type="check" presentation="standard" readonly="true" />--%>
                                        <c:field name="invoice.stApprovedFlag" caption="Approved" type="check" presentation="standard" readonly="true" />
                                    </c:evaluate>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <c:button text="Hitung Ulang" event="recalculate" validate="false" />
                    <c:evaluate when="<%=hasTRX && !isApprovedMode%>">
                        <c:button text="Simpan" event="clickSave" validate="true" />
                    </c:evaluate>
                    <c:evaluate when="<%=isApprovedMode%>">
                        <c:button text="Setujui" event="clickSave" validate="true" />
                    </c:evaluate>
                    <c:evaluate when="<%=form.isReverseMode()%>">
                        <c:button text="Reverse" event="clickReverse" validate="true" />
                    </c:evaluate>
                    
                    <c:button text="Tutup" event="clickCancel" validate="false" />
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
</script>