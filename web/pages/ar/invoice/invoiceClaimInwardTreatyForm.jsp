<%@ page import="com.webfin.insurance.form.InvoiceClaimInwardTreatyForm,
         com.webfin.insurance.model.InsurancePolicyInwardView,
         com.webfin.ar.model.ARTransactionTypeView,
         com.crux.util.Tools,
         com.webfin.FinCodec,
         com.crux.util.BDUtil,
         com.webfin.gl.ejb.CurrencyManager,
         com.webfin.insurance.model.InsurancePolicyInwardDetailView,
         com.webfin.ar.model.ARTaxView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="TRANSACTION" >
    <%
                final InvoiceClaimInwardTreatyForm form = (InvoiceClaimInwardTreatyForm) request.getAttribute("FORM");

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
                                    <c:field name="invoice.stClaimStatus" width="100" caption="Status" type="string|64" readonly="true" presentation="standard" />
                                    <c:evaluate when="<%=invoice.getStClaimStatus().equalsIgnoreCase(FinCodec.ClaimStatus.DLA)%>">
                                        <c:field name="invoice.stRefID0" width="150" caption="DLA Status" type="string|64" lov="LOV_ClaimInwardTreatyDLAStatus" readonly="false" presentation="standard" />
                                    </c:evaluate>
                                    <c:field changeaction="onChangeTRXType" readonly="<%=hasTRX%>" lov="LOV_ARTrxType" name="invoice.stARTransactionTypeID" width="400" caption="TRANSACTION TYPE" type="string" presentation="standard" mandatory="true" />
                                    <c:evaluate when="<%=hasTRX%>">
                                        <%--
                                        <c:field include="<%=enablePolicyType%>" lov="LOV_PolicyType" width="200" name="invoice.stAttrPolicyTypeID" caption="Policy Type" type="string" presentation="standard" mandatory="true" />
                                        --%>
                                        <c:field lov="LOV_CostCenter" name="invoice.stCostCenterCode" width="200" caption="Cabang" type="string|64" presentation="standard" mandatory="true" />
                                        <c:field name="invoice.stInvoiceNo" width="200" caption="PLA/DLA No" type="string|64" presentation="standard" mandatory="true" />
                                        <c:field name="invoice.dtInvoiceDate" caption="Entry Date" type="date" presentation="standard" mandatory="true" />

                                        <c:field name="invoice.dtMutationDate" caption="Approved Date" type="date" presentation="standard" mandatory="true" />
                                        <tr>
                                            <td>Ceding Company</td>
                                            <td>:</td>
                                            <td>
                                                <c:field name="invoice.stEntityID" type="string" hidden="true" />
                                                <c:field width="200" caption="Ceding Company" name="invoice.stEntityName" type="string" mandatory="true" readonly="true" />
                                                <c:button text="..." clientEvent="selectCustomer()" />
                                            </td>
                                        </tr>
                                        <%--  </c:evaluate> --%>

                                        <%--
                                        <c:evaluate when="<%=custIns%>">
                                           <c:field lov="LOV_Entity" name="invoice.stEntityID" caption="CUSTOMER tes" type="string" presentation="standard" width="200" mandatory="true" />
                                        </c:evaluate>
                                        --%>

                                        <c:field name="invoice.stInvoiceType" caption="INVOICE TYPE" type="string" presentation="standard" lov="LOV_InvoiceType" readonly="true" />
                                        <%--<c:field name="invoice.stGLAccountCodeTR" width="100" caption="ACCOUNT CODE" type="string" presentation="standard" readonly="true" />--%>

                                        <%--
                                        <c:field name="invoice.stGLARAccountDesc" rows="2" width="200" caption="ACCOUNT" type="string" presentation="standard" readonly="true" />
                                        --%>

                                        <c:field name="invoice.dtDueDate" caption="Due Date" type="date" presentation="standard" mandatory="true" />
                                        <c:field changeaction="onChangeCurrency" name="invoice.stCurrencyCode" caption="Currency" type="string" presentation="standard" lov="LOV_Currency" mandatory="true" />
                                        <c:field readonly="<%=!forex%>" name="invoice.dbCurrencyRate" caption="Currency Rate" type="money16.2" presentation="standard" mandatory="true" />
                                        <c:field name="invoice.stAttrQuartal" caption="Quartal" type="string|32" presentation="standard" mandatory="true" />
                                        <c:evaluate when="<%=enableAttachment%>">
                                            <c:field width="200" name="invoice.stFilePhysic" type="file" thumbnail="true" caption="{L-ENG File Attachment-L}{L-INA File Lampiran-L}" presentation="standard" />
                                        </c:evaluate>
                                        <c:field name="invoice.dtReference2" caption="Date of Loss" type="date" presentation="standard" mandatory="true" />
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
                                            <c:field name="invoice.stAttrPolicyName" width="300" caption="Risk Name" type="string|255" presentation="standard" mandatory="true" />
                                            <c:field name="invoice.stAttrPolicyAddress" width="300"  rows="2" caption="Risk Address" type="string|255" presentation="standard" mandatory="true" />
                                            <c:field name="invoice.dbAttrPolicyTSITotal" caption="Total Sum Insured" type="money16.2" presentation="standard" mandatory="true" />
                                            <c:field name="invoice.dbAttrPolicyTSI" caption="Sum Insured" type="money16.2" presentation="standard" mandatory="true" />
                                        </c:evaluate>
                                        <c:evaluate when="<%=invoice.getARInvoiceFF() != null%>">
                                            <c:flexfield ffid="<%=invoice.getARInvoiceFF().getStFlexFieldHeaderID()%>" prefix="invoice."/>
                                        </c:evaluate>
                                        <c:evaluate when="<%=enableUWrit%>">
                                            <%--
                                               <c:field name="invoice.stAttrUnderwriting" caption="Underwriting Year" type="string|32" presentation="standard" mandatory="true" />
                                            --%>
                                        </c:evaluate>
                                        <c:evaluate when="<%=enableReins%>">
                                            <c:field name="invoice.stRefID0" caption="Treaty" type="string|32" presentation="standard" lov="LOV_TreatyType" mandatory="true" />
                                        </c:evaluate>
                                    </c:evaluate>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td>
                    <%--<c:popselect form="xxx" title="xxx">
                       <param pass="invoice.stInvoiceNo"/>
                       <param pass="invoice.dtInvoiceDate"/>
                       <link name="value" field="details.currentObject.stGLAccountID"/>
                       <link name="text" field="details.currentObject.stGLAccountDesc"/>
                    </c:popselect>--%>
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
                                <c:listcol title="Policy Type">
                                    <c:field name="<%="details.[$index$].stAttrPolicyTypeID"%>" lov="LOV_PolicyType" caption="Policy Type" width="200" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="U/W Year">
                                    <c:field name="<%="details.[$index$].stAttrUnderwriting"%>"  caption="U/W Year" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Treaty">
                                    <c:field name="details.[$index$].stRefID0" lov="LOV_TreatyType" caption="Treaty" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Description">
                                    <c:field name="details.[$index$].stDescription" caption="Account" type="string" readonly="<%=enableFixedItem%>" />
                                </c:listcol>
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
                </c:evaluate>
                </td>
            </tr>
            <tr>
                <td>
                    <table cellpadding=2 cellspacing=1>
                        <tr>
                            <td>
                                <table cellpadding=2 cellspacing=1>
                                    <%--<tr>
                                       <td>ACCOUNT</td>
                                       <td>:</td>
                                       <td>
                                          <c:field name="invoice.stGLARAccountID" caption="ACCOUNT" type="string" hidden="true" mandatory="true" />
                                          <c:field name="invoice.stGLARAccountDesc" caption="ACCOUNT" type="string" readonly="true" width="200" />
                                          <c:button text="..." clientEvent="<%="pop2()"%>" validate="false" />
                                       </td>
                                    </tr>--%>
                                    <c:evaluate when="<%=hasTRX%>">
                                        <c:field name="invoice.dbEnteredAmount" mandatory="true" caption="<%="Amount(" + invoice.getStCurrencyCode() + ")"%>" type="money16.2" presentation="standard" readonly="<%=amountRO%>" />
                                        <c:evaluate when="<%=forex%>">
                                            <c:field name="invoice.dbAmount" caption="<%="Amount(" + masterCCY + ")"%>" type="money16.2" presentation="standard" readonly="true" />
                                        </c:evaluate>
                                        <c:field include="<%=hasAmountSettled%>" name="invoice.dbAmountSettled" caption="SETTLED" type="money16.2" presentation="standard" readonly="true" />
                                        <c:field name="invoice.stDescription" caption="DESCRIPTION" rows="3" type="string" presentation="standard" width="500" />
                                        <c:field name="invoice.stPostedFlag" caption="POSTED" type="check" presentation="standard"/>
                                        <c:field name="invoice.stCancelFlag" caption="Cancelled" type="check" presentation="standard" readonly="true" />
                                        <c:field name="invoice.stCommitFlag" caption="Committed" type="check" presentation="standard" readonly="true" />
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
                    <c:button text="Recalculate" event="recalculate" validate="false" />
                    <c:evaluate when="<%=hasTRX && !isApprovedMode%>">
                        <c:button text="Save" event="clickSave" validate="true" />
                    </c:evaluate>
                    <c:evaluate when="<%=isApprovedMode%>">
                        <c:button text="Approve" event="clickSave" validate="true" />
                    </c:evaluate>
                    <c:button text="Reverse" event="clickReverse" validate="true" />
                    <c:button text="Cancel" event="clickCancel" validate="false" />
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