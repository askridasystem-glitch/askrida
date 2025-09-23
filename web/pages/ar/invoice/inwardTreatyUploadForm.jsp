<%@ page import="com.webfin.insurance.form.InvoiceInwardTreatyForm,
com.webfin.insurance.model.InsurancePolicyInwardView,
com.webfin.ar.model.ARTransactionTypeView,
com.crux.util.Tools,
com.crux.util.BDUtil,
com.crux.util.DTOList,
com.webfin.gl.ejb.CurrencyManager,
com.webfin.insurance.model.InsurancePolicyInwardDetailView,
com.webfin.ar.model.ARTaxView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="UPLOAD INWARD" >
    <%
    final InvoiceInwardTreatyForm form = (InvoiceInwardTreatyForm)request.getAttribute("FORM");
    
    final InsurancePolicyInwardView invoice = form.getInvoice();
    
    final boolean hasTRX = invoice.getStARTransactionTypeID()!=null;
    
    final ARTransactionTypeView artrx = invoice.getARTrxType();
    
    final boolean superType = artrx!=null && artrx.isSuperType();
    
    final boolean enablePolicyType = form.trxEnablePolType();
    final boolean enablePolis = form.trxEnablePolis();
    final boolean enableUWrit = form.trxEnableUWrit();
    final boolean enableFixedItem = form.trxEnableFixedItem();
    final boolean disableDetail = form.trxDisableDetail();
    final boolean custIns = artrx!=null && artrx.trxCustIns();
    final boolean enableReins = form.trxEnableReins();
    
    final boolean amountRO = !disableDetail;
    final boolean hasDetail = !disableDetail;
    
    final boolean hasAmountSettled = Tools.compare(invoice.getDbAmountSettled(), BDUtil.zero)>0;
    
    final String masterCCY = CurrencyManager.getInstance().getMasterCurrency();
    
    final boolean forex = invoice.getStCurrencyCode()!=null && !Tools.isEqual(masterCCY,invoice.getStCurrencyCode());
    
    final boolean isApprovedMode = form.isApprovedMode();
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
                                <c:evaluate when="<%=hasTRX%>">
                                    <c:field lov="LOV_CostCenter" name="invoice.stCostCenterCode" width="200" caption="Cost Center" type="string|64" presentation="standard" mandatory="true" />
                                    <c:field name="invoice.dtInvoiceDate" caption="TRANSACTION DATE" type="date" presentation="standard" mandatory="true" />                  
                                    <c:field name="invoice.dtMutationDate" caption="Mutation Date" type="date" presentation="standard" mandatory="true" />
                                    <c:field name="invoice.stInvoiceType" caption="INVOICE TYPE" type="string" presentation="standard" lov="LOV_InvoiceType" readonly="true" />
                                    <c:field name="invoice.dtDueDate" caption="DUE DATE" type="date" presentation="standard" mandatory="true" />
                                   
                                    <c:field width="200" name="invoice.stFilePhysic" type="file" thumbnail="true" caption="{L-ENG File Attachment-L}{L-INA File Lampiran-L}" presentation="standard" />
                                    <tr>
                                        <td>
                                            <c:button text="Konversi" event="uploadExcel" />
                                        </td>
                                    </tr>

                                </c:evaluate>
                            </table>
                        </td>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:evaluate when="<%=hasTRX%>">
                                     <c:field width="200" name="invoice.stAttachment1" type="file" thumbnail="true" caption="Dokumen Email" presentation="standard" />
                                        <c:field width="200" name="invoice.stAttachment2" type="file" thumbnail="true" caption="{L-ENG File Attachment-L}{L-INA File Lampiran-L} 2" presentation="standard" />
                                        <c:field width="200" name="invoice.stAttachment3" type="file" thumbnail="true" caption="{L-ENG File Attachment-L}{L-INA File Lampiran-L} 3" presentation="standard" />
                                    <c:evaluate when="<%=false%>">
                                        <c:field name="invoice.stAttrPolicyNo" width="200" caption="Policy Number" type="string|32" presentation="standard" mandatory="true" />
                                        <c:field name="invoice.dtAttrPolicyPeriodStart" caption="Period Start" type="date" presentation="standard" mandatory="true" />
                                        <c:field name="invoice.dtAttrPolicyPeriodEnd" caption="Period End" type="date" presentation="standard" mandatory="true" />
                                        <c:field name="invoice.stAttrPolicyName" width="300" caption="Risk Name" type="string|255" presentation="standard" mandatory="true" />
                                        <c:field name="invoice.stAttrPolicyAddress" width="300"  rows="2" caption="Risk Address" type="string|255" presentation="standard" mandatory="true" />
                                        <c:field name="invoice.dbAttrPolicyTSITotal" caption="Total Sum Insured" type="money16.2" presentation="standard" mandatory="true" />
                                        <c:field name="invoice.dbAttrPolicyTSI" caption="Sum Insured" type="money16.2" presentation="standard" mandatory="true" />
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
            <c:field name="itemIndex" type="string" hidden="true" />
            <c:field name="detailindex" hidden="true" type="string"/>
            <c:evaluate when="<%=hasTRX && hasDetail%>">
                <c:listbox name="details" paging="true">
            <%
            final InsurancePolicyInwardDetailView ivx = (InsurancePolicyInwardDetailView) current;
            %>
            <c:listcol title="" columnClass="header">
                <c:evaluate when="<%=!enableFixedItem%>">
                    <c:button text="+" event="onNewDetails" validate="false" />
                </c:evaluate>
            </c:listcol>
            <c:listcol title="" columnClass="detail">
                <c:evaluate when="<%=!enableFixedItem%>">
                    <c:button text="-" validate="false" event="onDeleteItem" clientEvent="f.itemIndex.value=$index$" />
                </c:evaluate>
            </c:listcol>

            <c:listcol title="Ceding" />
            <c:listcol title="Reinsured"/>
            <c:listcol title="Nomor Transaksi"/>
            <c:listcol title="Policy Type">
                <c:field name="<%="details.[$index$].stAttrPolicyTypeID"%>" lov="LOV_PolicyType3" caption="Policy Type" width="200" type="string" readonly="true" />
            </c:listcol>

            <c:listcol title="U/W Year">
                <c:field name="<%="details.[$index$].stAttrUnderwriting"%>"  caption="U/W Year" type="string" readonly="true" />
            </c:listcol>
            <c:listcol title="Quartal"/>
            <c:listcol title="Quartal Year"/>
            <c:listcol title="Treaty">
                <c:field name="<%="details.[$index$].stRefID0"%>"  lov="LOV_TreatyType" caption="Treaty" type="string" readonly="true" />
            </c:listcol>
            
            <c:listcol title="Description" />
            <c:listcol title="Ccy" />
            <c:listcol title="Ccy Rate" />
            <c:listcol title="+/-" align="center" />

            <c:listcol title="<%="Amount("+invoice.getStCurrencyCode()+")"%>">
                <c:field name="<%="details.[$index$].dbEnteredAmount"%>" caption="Amount" type="money16.2" readonly="true" mandatory="true" />
            </c:listcol>
            <c:evaluate when="<%=forex%>">
                <c:listcol title="<%="Amount("+masterCCY+")"%>">
                    <c:field name="<%="details.[$index$].dbAmount"%>" caption="Amount" type="money16.2" readonly="true" />
                </c:listcol>
            </c:evaluate>
            <c:listcol title="Deskripsi" />

            <c:evaluate when="<%=enablePolis%>">
                <c:listcol title="Policy Number" />
                <c:listcol title="Period Start" />
                <c:listcol title="Period End" />
                <c:listcol title="Risk Name" />
                <c:listcol title="Risk Address" />
                <c:listcol title="Total Sum Insured" />
                <c:listcol title="Sum Insured" />
            </c:evaluate>

            
            
            <c:evaluate when="<%=ivx!=null && ivx.getDetails().size()>0%>" >
            
            <%
            final DTOList detail = ivx.getDetails();
            
            for (int i = 0; i < detail.size(); i++) {
                InsurancePolicyInwardDetailView arvdetail = (InsurancePolicyInwardDetailView) detail.get(i);
                
                final boolean negative = arvdetail!=null && arvdetail.getTrxLine() !=null && arvdetail.getTrxLine().isNegative();
            %>
        </tr><tr>
        <td class=row1></td>
        <%--
        <td class=row1>
            <c:field name="<%="details.[$index$].details.["+i+"].stGLAccountID"%>" hidden="true"/>
            <c:field name="<%="details.[$index$].details.["+i+"].stGLAccountDesc"%>" caption="Account" type="string" readonly="true" width="300" />
            <c:button text="..." clientEvent="<%="pop('x.frx',"+index+")"%>" validate="false" />
        </td>--%>
        <td class=row1>
                <c:field name="<%="details.[$index$].details.["+i+"].stEntityID"%>" lov="LOV_EntityOnly" popuplov="true" caption="Policy Type" width="200" type="string" readonly="false" />
        </td>
        <td class=row1>
                <c:field name="<%="details.[$index$].details.["+i+"].stReinsuranceEntityID"%>" lov="LOV_EntityOnly" popuplov="true" caption="Policy Type" width="200" type="string" readonly="false" />
        </td>
        <td class=row1>
            <c:field name="<%="details.[$index$].details.["+i+"].stTransactionNo"%>" caption="Trx No" width="200" type="string" readonly="false" />
        </td>
        <td class=row1>
            <c:field name="<%="details.[$index$].details.["+i+"].stAttrPolicyTypeID"%>" lov="LOV_PolicyType3" caption="Policy Type" width="200" type="string" readonly="false" />
        </td>
        
        <td class=row1>
            <c:field name="<%="details.[$index$].details.["+i+"].stAttrUnderwriting"%>"  width="80" caption="U/W Year" type="string" readonly="false" />
        </td>
        <td class=row1>
            <c:field name="<%="details.[$index$].details.["+i+"].stAttrQuartal"%>"  lov="LOV_Triwulan" caption="U/W Year" type="string" readonly="false" />
        </td>
        <td class=row1>
            <c:field name="<%="details.[$index$].details.["+i+"].stAttrQuartalYear"%>"  width="80" caption="U/W Year" type="string" readonly="false" />
        </td>
        <td class=row1>
            <c:field name="<%="details.[$index$].details.["+i+"].stRefID0"%>"  lov="LOV_TreatyType" caption="Treaty" type="string" readonly="false" />
        </td>
        <td class=row1>
            <c:field name="<%="details.[$index$].details.["+i+"].stDescription"%>" caption="Deskripsi" type="string" readonly="<%=enableFixedItem%>" />
        </td>
        <c:listcol title="Ccy">
            <c:field name="<%="details.[$index$].details.["+i+"].stCurrencyCode"%>" lov="LOV_Currency" caption="Policy Type" width="100" type="string" readonly="false" />
        </c:listcol>
        <c:listcol title="Ccy Rate">
            <c:field name="<%="details.[$index$].details.["+i+"].dbCurrencyRate"%>" caption="Policy Type" width="80" type="money16.2" readonly="false" />
        </c:listcol>
        <td  class=row1><%=negative?"-":"+"%></td>
        <td class=row1>
            <c:field name="<%="details.[$index$].details.["+i+"].dbEnteredAmount"%>" caption="Amount" type="money16.2" mandatory="true" />
        </td>
        <c:evaluate when="<%=forex%>">
            <td align="right" class=row1>
                <c:field name="<%="details.[$index$].details.["+i+"].dbAmount"%>" caption="Amount" type="money16.2" readonly="true" />
            </td>
        </c:evaluate>
        <td class=row1>
            <c:field name="<%="details.[$index$].details.["+i+"].stDeskripsi"%>" caption="Deskripsi" width="200" type="string" readonly="false" />
        </td>
        <c:evaluate when="<%=enablePolis%>">
            <c:field name="<%="details.[$index$].details.["+i+"].stAttrPolicyNo"%>" caption="Pol No" width="200" type="string" readonly="false" />
            <c:field name="<%="details.[$index$].details.["+i+"].dtAttrPolicyPeriodStart"%>" caption="Period Start" width="200" type="date" readonly="false" />
            <c:field name="<%="details.[$index$].details.["+i+"].dtAttrPolicyPeriodEnd"%>" caption="Period End" width="200" type="date" readonly="false" />
            <c:field name="<%="details.[$index$].details.["+i+"].stAttrPolicyName"%>" caption="Pol Name" width="200" type="string" readonly="false" />
            <c:field name="<%="details.[$index$].details.["+i+"].stAttrPolicyAddress"%>" caption="Address" width="200" type="string" readonly="false" />
            <c:field name="<%="details.[$index$].details.["+i+"].dbAttrPolicyTSITotal"%>" caption="TSI" type="money16.2" readonly="true" />
            <c:field name="<%="details.[$index$].details.["+i+"].dbAttrPolicyTSI"%>" caption="TSI 2" type="money16.2" readonly="true" />
        </c:evaluate>
        <%
            }
        %>
    </c:evaluate>
    </c:listbox>
    </c:evaluate>
    </td>
    </tr>
    <tr>
        <td>
            <br>
        </td>
    </tr>
    <tr>
        <td>
            <c:button text="Recalculate" event="recalculateInwardTreatyUpload" validate="false" />
            <c:evaluate when="<%=hasTRX&&!isApprovedMode%>">
                <c:button text="Save" event="clickSaveInwardTreatyUpload" confirm="Yakin ingin di simpan?" validate="true" />
            </c:evaluate>
            <c:evaluate when="<%=isApprovedMode%>">
                <c:button text="Approve" event="clickSaveInwardTreaty" validate="true" />
            </c:evaluate>

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