<%@ page import="com.webfin.pks.form.PerjanjianKerjasamaForm,
         com.webfin.gl.ejb.CurrencyManager,
         com.webfin.pks.model.*" %>
<%@ taglib prefix="c" uri="crux" %>
<c:frame title="Perjanjian Kerjasama">
    <%
                final PerjanjianKerjasamaForm form = (PerjanjianKerjasamaForm) frame.getForm();

                final PerjanjianKerjasamaView policy = form.getPolicy();

                final boolean ismasterCurrency = CurrencyManager.getInstance().isMasterCurrency(policy.getStCurrencyCode());

                final boolean hasPolicyType = policy.getStPolicyTypeID() != null;
                final boolean hasConditions = policy.getStConditionID() != null;
                final boolean hasRiskCategory = policy.getStRiskCategoryID() != null;

                final boolean ro = form.isReadOnly();

                boolean viewMode = ro;

                boolean showPolicyNo = false;
                if (policy.getStPolicyNo() != null) {
                    showPolicyNo = true;
                }

                showPolicyNo = true;

                final boolean effective = policy.isEffective();

                final boolean posted = policy.isPosted();

    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field width="200" readonly="<%=policy.getStReference1() != null%>" caption="Jenis" name="policy.stReference1" type="string"
                                         mandatory="true" presentation="standard" lov="LOV_PERJANJIAN"/>
                                <c:field width="200" changeaction="changeBranch" lov="LOV_CostCenter" readonly="<%=policy.getStCostCenterCode() != null%>"
                                         caption="{L-ENGBranch-L}{L-INACabang-L}" name="policy.stCostCenterCode" type="string"
                                         mandatory="true" presentation="standard"/>

                                <c:field show="<%=policy.getStCostCenterCode() != null%>" width="200" lov="LOV_Region" caption="{L-ENGRegion-L}{L-INADaerah-L}"
                                         name="policy.stRegionID" type="string" mandatory="true" readonly="<%=policy.getStRegionID() != null%>" presentation="standard">
                                    <c:lovLink name="cc_code" link="policy.stCostCenterCode" clientLink="false"/>
                                </c:field>

                                <c:field width="300" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroup"
                                         readonly="<%=policy.getStPolicyTypeGroupID() != null%>" caption="{L-ENGPolicy Class-L}{L-INAKelas Polis-L}"
                                         name="policy.stPolicyTypeGroupID" type="string" mandatory="false" presentation="standard"/>
                                <c:field show="<%=policy.getStPolicyTypeGroupID() != null%>" width="300" changeaction="onChangePolicyType" lov="LOV_PolicyType" readonly="<%=policy.getStPolicyTypeID() != null%>"
                                         caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" name="policy.stPolicyTypeID" type="string"
                                         mandatory="false" presentation="standard">
                                    <c:lovLink name="polgroup" link="policy.stPolicyTypeGroupID" clientLink="false"/>
                                </c:field>

                                <c:field width="100" show="false" caption="Policy ID" name="policy.stPolicyID" type="string"
                                         mandatory="false" readonly="true" presentation="standard"/>
                                <c:field width="100" show="false" caption="Parent Policy" name="policy.stParentID" type="string"
                                         mandatory="false" readonly="true" presentation="standard"/>


                                <c:field width="200" show="<%=showPolicyNo%>" caption="{L-ENGAskrida No-L}{L-INANomor PKS Askrida-L}"
                                         name="policy.stPolicyNo" type="string" mandatory="true" readonly="false" presentation="standard"/>
                                <c:field width="200" show="<%=showPolicyNo%>" caption="{L-ENGBank No-L}{L-INANomor PKS Bank-L}"
                                         name="policy.stBankNo" type="string" mandatory="true" readonly="false" presentation="standard"/>
                                <c:field caption="{L-ENGRemark Date-L}{L-INATanggal Penandatanganan-L}" name="policy.dtPolicyDate" type="date"
                                         mandatory="true" readonly="false" presentation="standard"/>
                                <c:field caption="{L-ENGReceive Date-L}{L-INATanggal Penerimaan-L}" name="policy.dtReceiveDate" type="date"
                                         mandatory="true" readonly="false" presentation="standard"/>
                                <c:field caption="{L-ENGPeriod Start-L}{L-INAPeriode Awal-L}" name="policy.dtPeriodStart" type="date"
                                         mandatory="true" readonly="false" presentation="standard"/>
                                <c:field caption="{L-ENGPeriod End-L}{L-INAPeriode Akhir-L}" name="policy.dtPeriodEnd" type="date"
                                         mandatory="true" readonly="false" presentation="standard"/>
                                <c:field name="policy.stFilePhysic" mandatory="true" type="file" thumbnail="true" caption="Lampiran PKS Awal" presentation="standard" />
                            </table>
                        </td>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field clientchangeaction="selectCustomer()" name="policy.stEntityID" type="string" width="200"
                                         popuplov="true" lov="LOV_Entity" mandatory="true"
                                         caption="Sumber Bisnis" presentation="standard"/>
                                <c:field width="200" rows="2" caption="Sumber Bisnis"
                                         name="policy.stCustomerName" type="string" mandatory="true" readonly="false"
                                         presentation="standard"/>
                                <c:field width="200" rows="2" caption="{L-ENGAddress-L}{L-INAAlamat-L}" name="policy.stCustomerAddress"
                                         type="string" mandatory="true" readonly="false" presentation="standard"/>

                                <c:field clientchangeaction="selectMarketer()" name="policy.stProducerID" type="string" width="200"
                                         popuplov="true" lov="LOV_Entity" mandatory="true"
                                         caption="Marketer" presentation="standard"/>
                                <c:field width="200" rows="2" caption="Marketer"
                                         name="policy.stProducerName" type="string" mandatory="true" readonly="false"
                                         presentation="standard"/>
                                <c:field width="200" rows="2" caption="{L-ENGAddress-L}{L-INAAlamat-L}" name="policy.stProducerAddress"
                                         type="string" mandatory="true" readonly="false" presentation="standard"/>


                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td colspan=2>
                            <table cellpadding=2 cellspacing=1>
                                <c:field width="550" rows="3" caption="{L-ENGNotes-L}{L-INACatatan-L}"
                                         name="policy.stDescription" type="string" mandatory="false" readonly="false"
                                         presentation="standard"/>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td align=center>
                <br>
                <br>
                <c:button show="<%=!viewMode%>"
                          text="{L-ENGSave-L}{L-INASimpan-L}" event="btnSave" validate="true"
                          confirm="Yakin mau disimpan ?"/>
                <c:button show="<%=!viewMode%>" text="{L-ENGCancel-L}{L-INABatal-L}" event="btnCancel" validate="false"
                          confirm="Yakin Mau Dibatalkan ?"/>
                <c:button show="<%=form.isApprovalMode()%>" text="Setujui" event="btnApproval" validate="false"
                          confirm="Yakin Mau Disetujui ?"/>
                <c:button show="<%=viewMode%>" text="{L-ENGClose-L}{L-INATutup-L}" event="btnCancel" validate="false"/>
            </td>
        </tr>
    </table>
</c:frame>
<script>    
    function selectCustomer() {
        var o = window.lovPopResult;
        document.getElementById('policy.stCustomerName').value = o.value;
        document.getElementById('policy.stCustomerAddress').value = o.address;
    }

    function selectMarketer() {
        var o = window.lovPopResult;
        document.getElementById('policy.stProducerName').value = o.value;
        document.getElementById('policy.stProducerAddress').value = o.address;
    }

    function refresh() {
        f.action_event.value = 'refresh';
        f.submit();
    }

    <%--document.body.onscroll=function() {alert(document.body.scrollTop);};--%>
    <%--document.body.onload=function() {document.body.scrollTop=200};--%>
</script>
