<%@page import="com.crux.util.BDUtil"%>
<%@ page import="com.webfin.insurance.form.PolicyForm,
com.crux.util.Tools,
com.webfin.gl.ejb.CurrencyManager,
com.crux.util.DTOList,
com.webfin.FinCodec,
com.webfin.insurance.model.*,
com.crux.util.JSPUtil,
com.crux.ff.model.FlexFieldHeaderView,
com.crux.ff.model.FlexFieldDetailView,
com.crux.common.controller.FormTab,
com.crux.web.controller.SessionManager,
com.crux.common.config.Config" %>
<%@ page import="com.crux.util.DateUtil" %>
<%@ taglib prefix="c" uri="crux" %>
<c:frame title="Insurance Policy">
<%
final PolicyForm form = (PolicyForm) frame.getForm();

final InsurancePolicyView policy = form.getPolicy();

final InsurancePolicyObjectView selectedObject = form.getSelectedObject();

final InsurancePolicyObjectView claimObject = form.getClaimObject();

final boolean canCreatePolicyHistory = form.isCanCreatePolicyHistory();

final boolean enableNoPolicy = form.isEnableNoPolicy();

final boolean ismasterCurrency = CurrencyManager.getInstance().isMasterCurrency(policy.getStCurrencyCode());
final boolean ismasterCurrencyClaim = CurrencyManager.getInstance().isMasterCurrency(policy.getStClaimCurrency());

final boolean hasPolicyType = policy.getStPolicyTypeID() != null;

final boolean isLampiran = policy.isLampiran();

boolean special_treaty = false;

final boolean phase2 = hasPolicyType;
final boolean phase1 = !phase2;

final String type_code = policy.getPolicyType() != null?policy.getPolicyType().getStPolicyTypeCode():"";
    
boolean hasCoIns = policy.hasCoIns();
boolean directPolicy = false;
if (policy.getStCoverTypeCode() != null){
    if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN"))
        hasCoIns = false;

    if (policy.getStCoverTypeCode().equalsIgnoreCase("DIRECT"))
        directPolicy = true;
}
    
boolean canSeeCoins = hasCoIns;
        
if (SessionManager.getInstance().getSession().getStBranch() != null){
     if (!SessionManager.getInstance().getSession().getStBranch().equalsIgnoreCase("00") && type_code.equalsIgnoreCase("OM_KREASI"))
        canSeeCoins = false;
        
     if (!SessionManager.getInstance().getSession().getStBranch().equalsIgnoreCase("00") && hasCoIns && !type_code.equalsIgnoreCase("OM_KREASI"))
        canSeeCoins = false;
}

final boolean ro = form.isReadOnly();
boolean viewMode = ro;

boolean statusDraft = FinCodec.PolicyStatus.DRAFT.equalsIgnoreCase(form.getStStatus());
boolean statusPolicy = FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(form.getStStatus());
boolean statusEndorse = FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(form.getStStatus());
boolean statusClaim = FinCodec.PolicyStatus.CLAIM.equalsIgnoreCase(form.getStStatus());
boolean statusSPPA = FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(form.getStStatus());
boolean statusClaimEndorse = FinCodec.PolicyStatus.ENDORSECLAIM.equalsIgnoreCase(form.getStStatus()) || FinCodec.PolicyStatus.ENDORSECLAIMINWARD.equalsIgnoreCase(form.getStStatus());
boolean statusHistory = FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(form.getStStatus());
boolean statusEndorseRI = FinCodec.PolicyStatus.ENDORSERI.equalsIgnoreCase(form.getStStatus());
boolean statusPLA = FinCodec.ClaimStatus.PLA.equalsIgnoreCase(form.getStClaimStatus());
boolean statusDLA = FinCodec.ClaimStatus.DLA.equalsIgnoreCase(form.getStClaimStatus());
boolean statusClaimInward = FinCodec.PolicyStatus.CLAIMINWARD.equalsIgnoreCase(form.getStStatus());

final boolean isInputPaymentDateMode = form.isInputPaymentDateMode();
final boolean showPersetujuanPrinsipNo = policy.getStReference1()!=null && statusSPPA && type_code.equalsIgnoreCase("OM_BG")? true:false;
final boolean showPolicyNo = policy.getStPolicyNo()!=null || (!statusDraft && !statusSPPA && canCreatePolicyHistory)? true:false;
final boolean effective = policy.isEffective();
final boolean posted = policy.isPosted();
final boolean canChangeCoverType = (statusDraft || statusSPPA || statusPolicy ||statusHistory) && !posted && (policy.getStCoverTypeCode() == null);

boolean canChangeCoinsMember = (statusDraft || statusSPPA || statusPolicy ||statusHistory || statusEndorse) && !posted;

final boolean rejectable = form.isRejectable();
final boolean isMember = policy.isMember();

boolean isRejectMode = form.isRejectMode();

boolean cROpolicyHeader = false;
boolean cROpolicyRiskDetail = false;
boolean cROpolicyClausules = false;
boolean cROpolicyPREMI = false;
boolean cROpolicyCOINS = false;
boolean cROpolicyINSTALLMENT = false;

if (statusPLA || statusDLA) {
    cROpolicyHeader = true;
    cROpolicyRiskDetail = true;
    cROpolicyClausules = true;
    cROpolicyPREMI = true;
    //cROpolicyCOINS = true;
}

final boolean cROReas = form.isReasMode();

if (form.isReasMode()&&!statusEndorseRI) {
    cROpolicyHeader = true;
    cROpolicyRiskDetail = true;
    cROpolicyClausules = true;
    cROpolicyPREMI = true;
}

if(isInputPaymentDateMode){
    cROpolicyHeader = true;
    cROpolicyRiskDetail = true;
    cROpolicyClausules = true;
    cROpolicyPREMI = true;
    cROpolicyINSTALLMENT = true;
}

if (Tools.isYes(policy.getStLockCoinsFlag())) cROpolicyCOINS = true;

boolean hasClaimCo = (statusClaim || statusClaimEndorse) && hasCoIns && policy.getClaimObject() != null;

String labelEntity = "{L-ENGBussiness Source-L}{L-INASumber Bisnis-L}";
String labelEntity2 = "{L-ENGBussiness Source-L}{L-INASumber Bisnis-L}";
if (policy.getPolicyType() != null){
    if(policy.getPolicyType().getStEntityMasterLabel()!=null)
        labelEntity2 = policy.getPolicyType().getStEntityMasterLabel();
    
    if(policy.getPolicyType().getStEntityMasterDescriptionLabel()!=null)
        labelEntity = policy.getPolicyType().getStEntityMasterDescriptionLabel();
}  

boolean canSeeDoc = SessionManager.getInstance().getSession().hasResource("CLAIM_APPROVE_BY_DIRECTOR");

boolean isSubrogasi = false;
if (statusClaimEndorse) {
    isSubrogasi = Tools.isYes(policy.getStReference13());
}

final boolean headOfficeUser = SessionManager.getInstance().getSession().isHeadOfficeUser();

%>
<script>
    function switchRates(i) {
        try {
            var autorate = docEl('selectedObject.coverage.[' + i + '].stAutoRateFlag').checked;
            var entryrate = docEl('selectedObject.coverage.[' + i + '].stEntryRateFlag').checked;
            var entrypremi = docEl('selectedObject.coverage.[' + i + '].stEntryPremiFlag').checked;

            VM_switchReadOnly('selectedObject.coverage.[' + i + '].dbRate', !autorate && entryrate);
            VM_switchReadOnly('selectedObject.coverage.[' + i + '].dbPremiNew', entrypremi);
            VM_switchReadOnly('selectedObject.coverage.[' + i + '].stCalculationDesc', !entryrate)
        } catch(e) {
        }
    }

    function switchRIRates(i, j, k) {
        //try {

        var pfx = "treaties.[" + i + "].details.[" + j + "].shares.[" + k + "]";
        var autorate = docEl(pfx + ".stAutoRateFlag").checked;
        var entryrate = docEl(pfx + ".stUseRateFlag").checked;

        VM_switchReadOnly(pfx + ".dbPremiRate", !autorate && entryrate);
        VM_switchReadOnly(pfx + ".dbPremiAmount", !entryrate);
        //} catch(e) {
        //}
    }

    function switchCoverRates(i, j, k, l) {
        //try {

        var pfx = "treaties.[" + i + "].details.[" + j + "].shares.[" + k + "].coverage.[" + l + "]";
        var autorate = docEl(pfx + ".stAutoRateFlag").checked;
        var entryrate = docEl(pfx + ".stEntryRateFlag").checked;
        var tes = docEl(pfx + ".stEntryInsuredAmountFlag").checked;
        //clientchangeaction="VM_switchReadOnly('"treaties.["+i+"].details.["+j+"].shares.["+k+"].coverage.["+l+"]"',this.checked)"

        VM_switchReadOnly(pfx + ".dbInsuredAmount", tes);
        VM_switchReadOnly(pfx + ".dbRate", !autorate && entryrate);


        //} catch(e) {
        //}
    }

</script>
<%--RISKCLASS = <%=policy.getStRiskClass()%>  LIMIT = <%=form.getTransactionLimit("ACCEPT", policy.getStRiskClass())%>--%>
<table cellpadding=2 cellspacing=1>
<c:evaluate when="<%=phase1%>">
    <tr>
        <td>
            <table cellpadding=2 cellspacing=1>
                <c:field width="300" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroup"
                         readonly="<%=hasPolicyType%>" caption="{L-ENGPolicy Class-L}{L-INAKelas Polis-L}"
                         name="policy.stPolicyTypeGroupID" type="string" mandatory="true" presentation="standard"/>
                <c:field width="300" include="<%=policy.getStPolicyTypeGroupID()!=null%>"
                         changeaction="onChangePolicyType" lov="LOV_PolicyType" readonly="<%=hasPolicyType%>"
                         caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" name="policy.stPolicyTypeID" type="string"
                         mandatory="true" presentation="standard">
                    <c:lovLink name="polgroup" link="policy.stPolicyTypeGroupID" clientLink="false"/>
                </c:field>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <c:button text="Cancel" event="btnCancel"/>
        </td>
    </tr>
</c:evaluate>
<%
boolean hasCoverType = policy.getStCoverTypeCode() != null;
%>
<c:evaluate when="<%=phase2 && !hasCoverType%>">
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <c:fieldcontrol when="<%=cROpolicyHeader%>" readonly="true">
                    <table cellpadding=2 cellspacing=1>
                        <c:field width="300" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroup"
                                 readonly="true" caption="{L-ENGPolicy Class-L}{L-INAKelas Polis-L}"
                                 name="policy.stPolicyTypeGroupID" type="string" mandatory="true"
                                 presentation="standard"/>
                        <c:field width="300" include="<%=policy.getStPolicyTypeGroupID()!=null%>"
                                 changeaction="onChangePolicyType" lov="LOV_PolicyType" readonly="true"
                                 caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" name="policy.stPolicyTypeID"
                                 type="string" mandatory="true" presentation="standard">
                            <c:lovLink name="polgroup" link="policy.stPolicyTypeGroupID" clientLink="false"/>
                            <c:lovLink name="pol_type_id" link="policy.stPolicyTypeID" clientLink="false"/>
                        </c:field>
                        
                        <c:field width="200" changeaction="changeBranch" lov="LOV_CostCenter"
                                 caption="{L-ENGBranch-L}{L-INACabang-L}" name="policy.stCostCenterCode" type="string"
                                 mandatory="true"
                                 readonly="<%=!canChangeCoverType || policy.getStCostCenterCode()!=null%>"
                                 presentation="standard"/>
                        
                        <c:field show="<%=policy.getStCostCenterCode()!=null%>" width="200" lov="LOV_Region"
                                 changeaction="onChangeRegion" caption="{L-ENGRegion-L}{L-INADaerah-L}"
                                 name="policy.stRegionID" type="string" mandatory="false"
                                 readonly="<%=!canChangeCoverType%>" presentation="standard">
                            <c:lovLink name="cc_code" link="policy.stCostCenterCode" clientLink="false"/>
                        </c:field>
                        <c:field show="<%=policy.getStRegionID()!=null%>" changeaction="chgCoverType" width="200"
                                 lov="LOV_InsuranceCoverType" caption="{L-ENGCover Type-L}{L-INAJenis Penutupan-L}"
                                 name="policy.stCoverTypeCode" type="string" mandatory="true"
                                 readonly="<%=!canChangeCoverType%>" presentation="standard"/>
                        
                    </table>
                </c:fieldcontrol>
            </td>
        </tr>
    </table>
</c:evaluate>
<c:evaluate when="<%=phase2 && hasCoverType%>">
<tr>
    <td>
        <c:fieldcontrol when="<%=cROpolicyHeader%>" readonly="true">
            <table cellpadding=2 cellspacing=1>
                <tr>
                    <td>
                        <table cellpadding=2 cellspacing=1>
                            <c:field width="100" show="false" caption="Policy ID" name="policy.stPolicyID" type="string|32"
                                     mandatory="false" readonly="true" presentation="standard"/>
                            <c:field width="100" show="false" caption="Parent Policy" name="policy.stParentID" type="string|32"
                                     mandatory="false" readonly="true" presentation="standard"/>
                            <c:field width="100" show="<%=policy.getStStatus()!=null%>" caption="Status" name="stStatus" type="string|32"
                                     mandatory="false" readonly="true" presentation="standard"/>
                            <c:field width="200" show="<%=policy.getStClaimStatus()!=null%>"
                                     caption="{L-ENGClaim Status-L}{L-INAStatus Klaim-L}" name="policy.stClaimStatusDesc" type="string"
                                     mandatory="false" readonly="true" presentation="standard"/>
                            <c:field width="200" show="<%=showPolicyNo%>" caption="{L-ENGPolicy No-L}{L-INANomor Polis-L}"
                                     name="policy.stPolicyNo" type="string|32" mandatory="true" readonly="<%=!enableNoPolicy%>" presentation="standard"/>
                            
                            
                            <c:field width="200" show="<%=showPersetujuanPrinsipNo%>" caption="{L-ENGNomor Persetujuan Prinsip-L}{L-INANomor Persetujuan Prinsip-L}"
                                     name="policy.stReference1" type="string|32" mandatory="false" readonly="true" presentation="standard"/>
                            <c:field width="200" show="<%=policy.getStSPPANo()!=null%>" caption="{L-ENGSPPA No-L}{L-INANomor SPPA-L}"
                                     name="policy.stSPPANo" type="string|32" mandatory="true" readonly="false" presentation="standard"/>
                            <c:field width="200" changeaction="changeBranch" lov="LOV_CostCenter" caption="{L-ENGBranch-L}{L-INACabang-L}"
                                     name="policy.stCostCenterCode" type="string" mandatory="true"
                                     readonly="<%=!canChangeCoverType || policy.getStCostCenterCode()!=null%>" presentation="standard"/>
                            <c:field show="<%=policy.getStCostCenterCode()!=null%>" width="200" lov="LOV_Region"
                                     changeaction="onChangeRegion" caption="{L-ENGRegion-L}{L-INADaerah-L}" name="policy.stRegionID"
                                     type="string" mandatory="false" readonly="<%=!canChangeCoverType%>" presentation="standard">
                                <c:lovLink name="cc_code" link="policy.stCostCenterCode" clientLink="false"/>
                            </c:field>
                            
                            <c:field show="<%=policy.getStRegionID()!=null%>" changeaction="chgCoverType" width="200"
                                     lov="LOV_InsuranceCoverType" caption="{L-ENGCover Type-L}{L-INAJenis Penutupan-L}"
                                     name="policy.stCoverTypeCode" type="string" mandatory="true" readonly="<%=!canChangeCoverType%>"
                                     presentation="standard"/>
                            <c:field caption="{L-ENGPolicy/Endorsement Date-L}{L-INATanggal Polis/Endorsement-L}" name="policy.dtPolicyDate" type="date"
                                     mandatory="true" readonly="false" presentation="standard"/>
                            <c:field show="<%=statusEndorse||statusEndorseRI%>" caption="{L-ENGChange Date-L}{L-INATanggal Perubahan-L}"
                                     name="policy.dtEndorseDate" type="date" mandatory="true" readonly="false" presentation="standard"/>
                            
                            <c:field width="300" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroup" readonly="true"
                                     caption="{L-ENGPolicy Class-L}{L-INAKelas Polis-L}" name="policy.stPolicyTypeGroupID" type="string"
                                     mandatory="true" presentation="standard"/>
                            <c:field width="300" include="<%=policy.getStPolicyTypeGroupID()!=null%>" changeaction="onChangePolicyType"
                                     lov="LOV_PolicyType" readonly="true" caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}"
                                     name="policy.stPolicyTypeID" type="string" mandatory="true" presentation="standard">
                                <c:lovLink name="polgroup" link="policy.stPolicyTypeGroupID" clientLink="false"/>
                                <c:lovLink name="pol_type_id" link="policy.stPolicyTypeID" clientLink="false"/>
                            </c:field>
                            <c:field lov="LOV_Currency" changeaction="onChgCurrency" caption="{L-ENGCurrency-L}{L-INAMata Uang-L}"
                                     name="policy.stCurrencyCode" type="string" mandatory="true" readonly="false" presentation="standard"/>
                            <c:field caption="Kurs" name="policy.dbCurrencyRate" type="money16.2" mandatory="true"
                                     readonly="<%=ismasterCurrency%>" presentation="standard"/>
                            <%--<c:field caption="Period" name="policy.stInsurancePeriodID" type="string" lov="LOV_InsurancePeriod" mandatory="true" readonly="false" presentation="standard"/>--%>

                            <%--  <c:field caption="{L-ENGPeriod End-L}{L-INAPeriode Akhir-L}" name="policy.dtPeriodEnd" type="date" mandatory="true" readonly="false" presentation="standard"/>
                               --%>
                            <tr>
                                <td>{L-ENGPeriod Start-L}{L-INAPeriode Awal-L}</td>
                                <td>:</td>
                                <td>
                                    <c:field caption="{L-ENGPeriod Start-L}{L-INAPeriode Awal-L}" name="policy.dtPeriodStart" type="date"
                                             mandatory="true" readonly="false"/>
                                </td>
                            </tr>
                            
                            <tr>
                                <td>{L-ENGPeriod End-L}{L-INAPeriode Akhir-L}</td>
                                <td>:</td>
                                <td>
                                    <c:field caption="{L-ENGPeriod End-L}{L-INAPeriode Akhir-L}" name="policy.dtPeriodEnd" type="date"
                                             mandatory="true" readonly="false"/>
                                    
                                </td>
                            </tr>
                            
                            
                            <c:field width="150" caption="{L-ENGInsured Amount-L}{L-INAHarga Pertanggungan-L}" name="policy.dbInsuredAmount"
                                     type="money16.2" mandatory="false" readonly="true" presentation="standard"/>
                            <c:field show="<%=statusEndorse||statusEndorseRI%>" width="150" caption="{L-ENGEndorse Amount-L}{L-INAHarga Endorse-L}"
                                     name="policy.dbInsuredAmountEndorse" type="money16.2" mandatory="false" readonly="true"
                                     presentation="standard"/>
                            <%--<c:field width="150" caption="Premium (BASE)" name="policy.dbPremiBase" type="money16.2" mandatory="false" readonly="true" presentation="standard"/>--%>
                            <c:evaluate when="<%=isMember%>">
                            </c:evaluate>
                            <c:field width="150" caption="{L-ENGPremium (Total)-L}{L-INAPremi Bruto-L}" name="policy.dbPremiTotal"
                                     type="money16.2" mandatory="false" readonly="true" presentation="standard"/>
                            <c:field width="150" caption="{L-ENGPremium (Net)-L}{L-INATagihan Netto-L}" name="policy.dbPremiNetto"
                                     type="money16.2" mandatory="false" readonly="true" presentation="standard"/>
                        </table>
                    </td>
                    <td>
                        <table cellpadding=2 cellspacing=1>
                            <c:field width="300" show="true" lov="LOV_ParentPolicy" popuplov="true" changeaction="changeParentPolicy" caption="{L-ENGParent Policy No-L}{L-INANomor Polis Induk-L}"
                                     name="policy.stReference2" type="string|32" mandatory="false" presentation="standard">
                                <c:lovLink name="cc_code" link="policy.stCostCenterCode" clientLink="false"/>
                            </c:field>     
                            <c:field clientchangeaction="selectCustomer2()" name="policy.stEntityID" type="string" width="300"
                                     popuplov="true" lov="LOV_Entity" mandatory="true"
                                     caption="<%=labelEntity2%>" presentation="standard"/>
                            <c:field width="300" rows="3" caption="<%=labelEntity%>"
                                     name="policy.stCustomerName" type="string|255" mandatory="true" readonly="false"
                                     presentation="standard"/>
                            <c:field width="300" rows="3" caption="{L-ENGAddress-L}{L-INAAlamat-L}" name="policy.stCustomerAddress"
                                     type="string|255" mandatory="true" readonly="false" presentation="standard"/>
                            <c:evaluate when="<%=policy.getSPPAFF()!=null%>">
                                <c:flexfield ffid="<%=policy.getSPPAFF().getStFlexFieldHeaderID()%>" prefix="policy."/>
                            </c:evaluate>
                            
                            <c:fieldcontrol when="<%=cROpolicyHeader%>" readonly="false">
                                <c:evaluate when="<%=policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_KREASI")||
                                                policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_CREDIT")||
                                                policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_CREDIT_MACET")||
                                                policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_CREDIT_SERBA")%>">
                                <c:field clientchangeaction="selectCreditType()" name="policy.stKreasiTypeID" type="string" width="200"
                                         popuplov="true" lov="LOV_TypeOfCredit" mandatory="true"
                                         caption="{L-ENGType Of Credit-L}{L-INAJenis Kredit-L}" presentation="standard">
                                    <c:lovLink name="cc_code" link="policy.stCostCenterCode" clientLink="false"/>
                                </c:field>
                                <c:field show="false" width="200" caption="{L-ENGKreasi Type Name-L}{L-INANama Jenis Kreasi-L}"
                                         name="policy.stKreasiTypeDesc" type="string|255" mandatory="true" readonly="false"
                                         presentation="standard"/>


                            </c:evaluate>
                            </c:fieldcontrol>
                            
                            
                            
                            <tr>
                                <td>
                                    <c:evaluate when="<%=isMember%>">
                                        <c:field width="200" caption="{L-ENGLeader Policy No-L}{L-INANo Polis Rujukan-L}"
                                                 name="policy.stCoinsPolicyNo" type="string" mandatory="false" readonly="false"
                                                 presentation="standard"/>
                                        <c:field caption="Share Askrida" width="50" name="policy.dbSharePct" type="money16.5"
                                                 mandatory="false" readonly="false" suffix="%" presentation="standard"/>
                                    </c:evaluate>
                                </td>
                            </tr>
                            
                        </table>
                    </td>
                </tr>
                <tr>
                    <td colspan=2>
                        <table cellpadding=2 cellspacing=1>
                            <c:field width="550" rows="3" caption="{L-ENGPolicy Notes-L}{L-INACatatan Polis-L}"
                                     name="policy.stDescription" type="string" mandatory="false" readonly="false"
                                     presentation="standard"/>
                        </table>
                    </td>
                </tr>
                <c:evaluate when="<%=form.isReverseMode()%>">
                    <tr>
                        <td colspan=2>
                            <table cellpadding=2 cellspacing=1>
                                <c:field width="550" rows="3" caption="{L-ENGReverse Notes-L}{L-INACatatan Reverse-L}"
                                         name="policy.stReverseNotes" overrideRO="true" type="string" mandatory="true" readonly="false"
                                         presentation="standard"/>
                            </table>
                        </td>
                    </tr>
                </c:evaluate>
            </table>
        </c:fieldcontrol>
    </td>
</tr>
<tr>
    <td>
    <c:tab name="tabs">
        <c:tabpage name="TAB_CLAIM">
            <c:tab name="claimtabs">
                                    <c:fieldcontrol when="<%=form.isEditClaimNoteMode()%>" readonly="true">
                                        <c:tabpage name="TAB_DOCUMENTS">
                                            <table cellpadding=2 cellspacing=1 class=header width="100%">
                                                <tr>
                                                    <td>
                                                        DOCUMENTS
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class=row0>
                                                        <c:listbox name="policy.claimDocuments">
                                                            <c:listcol title="">
                                                                <c:field name="policy.claimDocuments.[$index$].stSelectedFlag" type="check"
                                                                         mandatory="false" readonly="false"/>
                                                            </c:listcol>
                                                            <c:listcol name="stDescription" title="DESCRIPTION">
                                                            </c:listcol>
                                                           
                                                            <c:listcol title="Document">
                                                                <c:field name="policy.claimDocuments.[$index$].stFilePhysic" type="file"
                                                                         thumbnail="true" caption="File"/>
                                                            </c:listcol>
                                                            <c:listcol title="Status">
                                                                <c:field name="policy.claimDocuments.[$index$].stDocumentStatus" lov="VS_STATUS_DOCUMENT" type="string"
                                                                         caption="Status"/>
                                                            </c:listcol>
                                                            <c:listcol title="Keterangan">
                                                                <c:field name="policy.claimDocuments.[$index$].stDocumentNotes" type="string" width="200"
                                                                         caption="Keterangan"/>
                                                            </c:listcol>
                                                            <c:evaluate when="<%=policy.isDataGateway()%>">
                                                                <c:listcol title="Update Status">
                                                                    <c:field name="policy.claimDocuments.[$index$].stUpdateStatus" lov="VS_STATUS_DOCUMENT_UPDATE" type="string"
                                                                             caption="Status" readonly="true" />
                                                                </c:listcol>
                                                                <c:listcol title="Update Date">
                                                                    <c:field name="policy.claimDocuments.[$index$].dtUpdateDate" type="date"
                                                                             caption="Status" readonly="true" />
                                                                </c:listcol>
                                                            </c:evaluate>
                                                        </c:listbox>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <c:button text="Send Status To H2H" event="sendStatusDocumentToInterkoneksi" confirm="Ingin kirim status dokumen ke H2H ?"/>
                                                    </td> 
                                                </tr>
                                            </table>
                                        </c:tabpage>
                                        <c:tabpage name="TAB_PLA">
                                            <table cellpadding=2 cellspacing=1 class=header width="100%">
                                                <tr>
                                                    <td>
                                                        PLA/LKS
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class=row0>
                                                        <table cellpadding=2 cellspacing=1>
                                                            <c:field width="200" caption="{L-ENGPLA/LKS No-L}{L-INANo. PLA/LKS-L}" name="policy.stPLANo"
                                                                     type="string|32" mandatory="false" readonly="true" presentation="standard"/>
                                                            <%--<c:field width="200" caption="Claim No" name="policy.stClaimNo" type="string|128" mandatory="true" presentation="standard"/>--%>
                                                            <c:field caption="{L-ENGClaim Proposed Date-L}{L-INATanggal Pengajuan Klaim-L}" name="policy.dtClaimProposeDate"
                                                                     type="date" mandatory="true" presentation="standard"/>
                                                            <c:field caption="{L-ENGClaim Date-L}{L-INATanggal Klaim-L}" name="policy.dtClaimDate"
                                                                     type="date" mandatory="true" presentation="standard"/>
                                                            <c:field caption="{L-ENGPLA Date-L}{L-INATanggal LKS-L}" name="policy.dtPLADate" type="date" mandatory="true" presentation="standard"/>
                                                            
                                                            <c:field caption="{L-ENGClaim Cause-L}{L-INAPenyebab Klaim-L}" width="400"
                                                                     lov="LOV_ClaimCause" name="policy.stClaimCauseID"
                                                                     changeaction="validateClaimCause" type="string" mandatory="true"
                                                                     presentation="standard">
                                                                <c:lovLink name="poltype" link="policy.stPolicyTypeID" clientLink="false"/>
                                                            </c:field>
                                                            <c:field caption="{L-ENGSpesifik Claim Cause-L}{L-INAPenyebab Klaim Spesifik-L}" width="400"
                                                                     lov="LOV_ClaimSubCause" name="policy.stClaimSubCauseID"
                                                                     type="string" mandatory="false" changeaction="changeClaimSubCause"
                                                                     presentation="standard">
                                                                <c:lovLink name="causeID" link="policy.stClaimCauseID" clientLink="false"/>
                                                            </c:field>
                                                            <c:field caption="{L-ENGOther Claim Cause-L}{L-INAKeterangan Penyebab Lainnya-L}" width="400"
                                                                     name="policy.stClaimSubCauseOther"
                                                                     type="string" mandatory="false"
                                                                     presentation="standard">
                                                            </c:field>

                                                            <c:field width="400" 
                                                                     caption="{L-ENGClaim Event Location-L}{L-INALokasi Terjadi Klaim-L}"
                                                                     name="policy.stClaimEventLocation" rows="2" type="string" mandatory="true"
                                                                     presentation="standard"/>
                                                            <c:field width="400" caption="{L-ENGClaim Person Name-L}{L-INANama Pengaju Klaim-L}"
                                                                     name="policy.stClaimPersonName" type="string|255" mandatory="true"
                                                                     presentation="standard"/>
                                                            <c:field width="400"
                                                                     caption="{L-ENGClaim Person Address-L}{L-INAAlamat Pengaju Klaim-L}" rows="2"
                                                                     name="policy.stClaimPersonAddress" type="string|255" mandatory="true"
                                                                     presentation="standard"/>
                                                            <c:field width="400" caption="{L-ENGClaim Person Status-L}{L-INAStatus Pengaju Klaim-L}"
                                                                     name="policy.stClaimPersonStatus" type="string|255" mandatory="true"
                                                                     presentation="standard"/>

                                                           
                                                            <tr>
                                                                <td>Cari Objek</td>
                                                                <td>:</td>
                                                                <td>
                                                                     <c:field width="400" name="stKey" caption="Search"
                                                                             type="string" overrideRO="true" readonly="false"/>
                                                                     &nbsp;<c:button text="Cari" event="searchObject"/>
                                                                </td>
                                                            </tr>
                                                            <c:evaluate when="<%=form.getStKey()!=null%>">
                                                            <tr>
                                                                <td></td>
                                                                <td></td>
                                                                <td>Hasil Pencarian
                                                                    <c:field name="objIndex" type="string" hidden="true" overrideRO="true" />
                                                                    <c:listbox name="objectSearch" view="com.webfin.insurance.model.InsurancePolicyObjDefaultView">
                                                                         <%
                                                                                final InsurancePolicyObjDefaultView objx = (InsurancePolicyObjDefaultView) current;
                                                                         %>
                                                                         <c:listcol name="stOrderNo" title="No Urut" />
                                                                         <c:listcol name="stReference1" title="Nama" />
                                                                         <c:listcol name="dbObjectInsuredAmount" title="TSI" />
                                                                         <c:listcol name="dbObjectPremiTotalBeforeCoinsuranceAmount" title="Premi" />
                                                                         <c:listcol title="">
                                                                             <c:button text="Pilih" clientEvent="<%="docEl('objIndex').value="+objx.getStOrderNo()+";"%>" event="selectClaimObjectUseOrder" />
                                                                         </c:listcol>
     
                                                                     </c:listbox>
                                                                </td>
                                                            </tr>
                                                            </c:evaluate>

                                                            <c:field width="400" lov="lovObjects" presentation="standard" name="stClaimObject"
                                                                     caption="{L-ENGClaim Object-L}{L-INAObjek Klaim-L}" type="string"
                                                                     changeaction="selectClaimObject"/>
                                                            
                                                            <c:field width="250" caption="{L-ENGLoss Status-L}{L-INAStatus Kerugian-L}"
                                                                     lov="LOV_ClaimLoss" name="claimObject.stClaimLossID" type="string"
                                                                     mandatory="false" presentation="standard">
                                                                <c:lovLink name="poltype" link="policy.stPolicyTypeID" clientLink="false"/>
                                                            </c:field>
                                                            <c:field width="250" caption="Claim Benefit" lov="VS_CLAIM_BENEFIT"
                                                                     name="policy.stClaimBenefit" type="string" mandatory="true"
                                                                     presentation="standard"/>
                                                            <c:field width="400" caption="{L-ENGClaim Documents-L}{L-INADokumen Klaim-L}" rows="3"
                                                                     name="policy.stClaimDocuments" type="string" mandatory="true"
                                                                     presentation="standard"/>
                                                            <c:evaluate when="<%=policy.getClaimFF()!=null%>">
                                                                <c:flexfield ffid="<%=policy.getClaimFF().getStFlexFieldHeaderID()%>"
                                                                             prefix="policy."/>
                                                            </c:evaluate>
                                                            <%--<c:field caption="{L-ENGPremium Payment Date-L}{L-INATanggal Pembayaran Premi-L}"
                                             readonly="true" name="policy.dtPremiPayDate" type="date"
                                             presentation="standard"/>--%>
                                                            <c:field caption="{L-ENGPayment Date-L}{L-INATanggal Pembayaran-L}" name="policy.dtPaymentDate"
                                                                     type="date" readonly="true" presentation="standard"/>
                                                            <c:field width="400" caption="{L-ENGClaim Chronology-L}{L-INAKronologis Kejadian-L}" rows="3" mandatory="true"
                                                                     name="policy.stClaimChronology" type="string" 
                                                                     presentation="standard"/>
                                                            <c:field caption="{L-ENGCurrency-L}{L-INAMata Uang-L}" changeaction="onChgCurrencyClaim" lov="LOV_Currency"
                                                                     width="200" name="policy.stClaimCurrency" type="string" mandatory="true"
                                                                     presentation="standard"/>
                                                            <c:field caption="Kurs Klaim" name="policy.dbCurrencyRateClaim" type="money16.2" mandatory="true"
                                                            readonly="<%=ismasterCurrencyClaim%>" presentation="standard"/>

                                                            <c:field caption="{L-ENGEstimated Amount-L}{L-INAJumlah Perkiraan-L}" width="200"
                                                                     name="policy.dbClaimAmountEstimate" type="money16.2" mandatory="true"
                                                                     presentation="standard"/>

                                                            <c:evaluate when="<%=statusClaimInward%>">
                                                                <c:field caption="TSI 100%" width="200"
                                                                     name="policy.dbTSI100" type="money16.2" mandatory="true"
                                                                     presentation="standard"/>
                                                                <c:field caption="Klaim 100%" width="200"
                                                                         name="policy.dbClaim100" type="money16.2" mandatory="true"
                                                                         presentation="standard"/>
                                                            </c:evaluate>
                                                            
                                                            <c:field caption="Claim Deduction" name="policy.dbClaimDeductionAmount" type="money16.2"  width="200"
                                                                     presentation="standard" readonly="true"/>
                                                            <c:field caption="{L-ENGClaim Cost-L}{L-INABiaya Klaim-L}" name="policy.dbClaimAmount"  width="200"
                                                                     type="money16.2" presentation="standard" readonly="true"/>
                                                            <c:field caption="Deductible to Customer" name="policy.dbClaimDeductionCustAmount"  width="200"
                                                                     type="money16.2" presentation="standard" readonly="true"/>
                                                            <c:field caption="Payable to Customer" name="policy.dbClaimCustAmount" type="money16.2"  width="200"
                                                                     presentation="standard" readonly="true"/>
                                                            <c:field caption="Chargeable to Treaty" name="policy.dbClaimREAmount" type="money16.2"  width="200"
                                                                     presentation="standard" readonly="true"/>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                        </c:tabpage>
                                        </c:fieldcontrol>
                                        
                                        <c:tabpage name="TAB_DLA">
                                        <table cellpadding=2 cellspacing=1 class=header width="100%">
                                        <tr>
                                            <td>
                                                DLA/LKP
                                            </td>
                                        </tr>
                                        <tr>
                                        <td class=row0>
                                        <table cellpadding=2 cellspacing=1>
                                            <c:fieldcontrol when="<%=form.isEditClaimNoteMode()%>" readonly="true">
                                                <c:field width="200" caption="DLA/LKP No" name="policy.stDLANo" type="string|32"
                                                         mandatory="false" readonly="true" presentation="standard"/>
                                                <c:field caption="{L-ENGDLA Date-L}{L-INATanggal DLA/LKP-L}" name="policy.dtDLADate"
                                                         type="date" mandatory="false" presentation="standard"/>
                                                <c:field caption="{L-ENGClaim Amount Approved-L}{L-INAJumlah Klaim Disetujui-L}" name="policy.dbClaimAmountApproved"
                                                         type="money16.2" suffix=" *Sebelum Dipotong Item Klaim" presentation="standard"/>
                                                <c:evaluate when="<%=isSubrogasi%>">
                                                            <c:field caption="Pelunasan Subrogasi (%)" name="policy.dbClaimSubroPaid" width="50"
                                                                     type="money16.0" suffix="% Default 100%" presentation="standard"/>
                                                        </c:evaluate>

                                                <c:evaluate when="<%=statusClaimEndorse%>">
                                                    <c:field caption="{L-ENGClaim Amount Endorse-L}{L-INAJumlah Klaim Seharusnya-L}" name="policy.dbClaimAmountEndorse"
                                                             type="money16.2" presentation="standard"/>
                                                </c:evaluate>
                                                <c:field caption="{L-ENGReady To Approve-L}{L-INASiap Disetujui-L}" name="policy.stReadyToApproveFlag" type="check" mandatory="false"
                                     readonly="false" presentation="standard"/>
                                                <c:field width="400" caption="{L-ENGDLA Remark-L}{L-INACatatan-L}" rows="3" name="policy.stDLARemark"
                                                         type="string|255" mandatory="false" readonly="false" presentation="standard"/>
                                            </c:fieldcontrol>
                                    <c:field width="200" caption="{L-ENGConfirmation No-L}{L-INANo. Konfirmasi-L}" name="policy.stClaimLetter" type="string|30" mandatory="false" readonly="false" presentation="standard"/>
                                    <c:field caption="{L-ENGConfirmation Date-L}{L-INATanggal Konfirmasi-L}" name="policy.dtConfirmDate"
                             type="date" mandatory="false" presentation="standard"/>
                                    
                                </table>
                            </td>
                        </tr>
                    </table>
                </c:tabpage>
                
            </c:tab>
        </c:tabpage>
        
        <c:tabpage name="TAB_REJECT">
            <c:fieldcontrol when="<%=isRejectMode || policy.getStRejectNotes()!=null%>" readonly="false">
                <c:field width="750" rows="20" caption="Reject Notes" name="policy.stRejectNotes" type="string"
                         mandatory="true" readonly="false"/>
            </c:fieldcontrol>
        </c:tabpage>
        <c:tabpage name="TAB_ENDORSE">
            <c:field width="750" rows="20" caption="Endorsement Notes" name="policy.stEndorseNotes" type="string"
                     mandatory="true" readonly="false"/>
        </c:tabpage>
        <c:tabpage name="TAB_RISK_DET">
            <table cellpadding=2 cellspacing=1 width="100%">
                <tr>
                    <td>
                        <table cellpadding=2 cellspacing=1>
                            <tr>
                                <td>{L-ENGObject-L}{L-INAObjek Pertanggungan-L}</td>
                                <td>:</td>
                                <td>
                                    <c:field width="400" lov="lovObjects" name="stSelectedObject" caption="Selected Object"
                                             type="string" changeaction="selectObject" overrideRO="true" readonly="false"/>
                                    <c:fieldcontrol when="<%=cROpolicyRiskDetail%>" readonly="true">
                                        <c:button text="+" event="doNewObject" defaultRO="true"/>
                                        <c:button text="-" event="doDeleteObject" confirm="Yakin Ingin Menghapus Objek Ini ?" defaultRO="true"/>
                                        <c:button text="Del All" event="doDeleteAllObjects" confirm="Yakin Ingin Menghapus Seluruh Objek ?" defaultRO="true"/>
                                    </c:fieldcontrol>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <c:evaluate when="<%=form.getSelectedObject()!=null%>">
                            <c:tab name="rdtabs">
                                <c:tabpage name="TAB_DETAIL">
                                    <c:fieldcontrol when="<%=cROpolicyRiskDetail%>" readonly="true">
                                        <c:fieldcontrol when="<%=statusClaimEndorse%>" readonly="false">
                                        <table cellpadding=2 cellspacing=1 class=header>
                                            <tr>
                                                <td>
                                                    {L-ENGObject-L}{L-INAObjek Pertanggungan-L}
                                                </td>
                                            </tr>
                                            <tr class=row0>
                                                <td>
                                                    <table cellpadding=2 cellspacing=1>
                                                        <c:evaluate when="<%=form.getSelectedObject().getStVoidFlag()!=null%>">
                                                            <c:field caption="<b><font color=red>Sudah Klaim</font></b>" name="selectedObject.stVoidFlag" type="check"
                                                                     mandatory="false" readonly="true" presentation="standard"/>
                                                        </c:evaluate>
                                                        <c:field caption="Nomor Urut" name="selectedObject.stOrderNo" type="string"
                                                                 mandatory="false" readonly="true" presentation="standard"/>   
                                                        <c:evaluate when="<%=form.getSelectedObject() instanceof InsurancePolicyObjDefaultView%>">
                                                            <c:flexfield ffid="<%=policy.getPolicyType().getStObjectMapID()%>"
                                                                         prefix="selectedObject."/>
                                                        </c:evaluate>
                                                        <c:evaluate when="<%="OM_KREASI".equalsIgnoreCase(policy.getPolicyType().getStObjectMapID())%>">
                                                            <script>
                                document.getElementById('selectedObject.stReference9').onclick = function() {
                                    VM_switchReadOnly('selectedObject.dbReference5', this.checked);
                                    VM_switchReadOnly('selectedObject.dbReference6', !this.checked);
                                }
                                document.getElementById('selectedObject.stReference9').onclick();

                                document.getElementById('selectedObject.stReference10').onclick = function() {
                                    VM_switchReadOnly('selectedObject.dbReference1', this.checked);
                                    VM_switchReadOnly('selectedObject.dbReference2', !this.checked);
                                }
                                document.getElementById('selectedObject.stReference10').onclick();

                                document.getElementById('selectedObject.stReference11').onclick = function() {
                                    VM_switchReadOnly('selectedObject.dbReference7', this.checked);
                                    VM_switchReadOnly('selectedObject.dbReference9', !this.checked);
                                }
                                document.getElementById('selectedObject.stReference11').onclick();
                                                            </script>
                                                        </c:evaluate>
                                                        <c:field width="100" caption="Limit Of Liability (LOL)"
                                                                 name="selectedObject.dbLimitOfLiability" type="money16.2" mandatory="false"
                                                                 suffix=" %" presentation="standard"/>
                                                        <c:field lov="LOV_RiskCategory" caption="{L-ENGRisk Code / Category-L}{L-INAKode Resiko / Kategori-L}"
                                                                 name="selectedObject.stRiskCategoryID" type="string" mandatory="true" readonly="false"
                                                                 width="400" presentation="standard" popuplov="true" refresh="true">
                                                            <c:lovLink name="datestart" link="policy.dtPeriodStart" clientLink="false"/>                 
                                                            <c:param name="poltype" value="<%=policy.getStPolicyTypeID()%>"  />
                                                        </c:field>
                                                        <c:field caption="Object ID" name="selectedObject.stPolicyObjectID" type="string"
                                                                 mandatory="false" readonly="true" hidden="true" presentation="standard"/>
                                                        <%--
                            <c:field width="200" caption="{L-ENGPolicy Number-L}{L-INANomor Polis-L}" name="selectedObject.stSubPolicyNo" type="string|64" mandatory="false" readonly="true" presentation="standard"/>
                            --%>
                                                        <c:field width="150" caption="{L-ENGInsured Amount-L}{L-INAHarga Pertanggungan-L}"
                                                                 name="selectedObject.dbObjectInsuredAmount" type="money16.2" mandatory="false"
                                                                 readonly="true" hidden="true" presentation="standard"/>
                                                        
                                                        <c:field width="150" caption="{L-ENGPremi Total-L}{L-INATotal Premi-L}"
                                                                 name="selectedObject.dbObjectPremiTotalAmount" type="money16.2" mandatory="false"
                                                                 readonly="true" hidden="true" presentation="standard"/>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                        </c:fieldcontrol>
                                    </c:fieldcontrol>
                                </c:tabpage>
                               
                                <c:evaluate when="<%=form.getSelectedObject().getClaimFF()!=null%>">
                                    <c:tabpage name="TAB_RDCLM">
                                        <table cellpadding=2 cellspacing=1 class=header>
                                            <tr>
                                                <td>
                                                    {L-ENGCLAIM INFO-L}{L-INAINFO KLAIM-L}
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class=row0>
                                                    <table cellpadding=2 cellspacing=1>
                                                        <c:flexfield ffid="<%=form.getSelectedObject().getClaimFF().getStFlexFieldHeaderID()%>"
                                                                     prefix="selectedObject."/>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                    </c:tabpage>
                                </c:evaluate>
                                <c:tabpage name="TAB_SI">
                                    <c:fieldcontrol when="<%=cROpolicyRiskDetail%>" readonly="true">
                                        <table cellpadding=2 cellspacing=1 class=header>
                                            <tr>
                                                <td class=header>
                                                    {L-ENGSum Insured-L}{L-INAHarga Pertanggungan-L}
                                                </td>
                                            </tr>
                                            <%
                                            final boolean lockTSI = policy.isLockTSI();
                                            %>
                                            <tr class=row0>
                                                <td align=right>
                                                    <c:field name="tsiIndex" type="string" hidden="true"/>
                                                    <c:listbox name="selectedObject.suminsureds">
                                                        <%
                                                        final InsurancePolicyTSIView tsix = (InsurancePolicyTSIView) current;
                                                        
                                                        final boolean isVoid = tsix != null && tsix.isVoid();
                                                        
                                                        final boolean rotsi = isVoid;
                                                        
                                                        final boolean manualTSILock = tsix != null && Tools.isYes(tsix.getStManualTSILockFlag());
                                                        final boolean defaultTSI = tsix != null && Tools.isYes(tsix.getStDefaultTSIFlag());
                                                        
                                                        %>
                                                        <c:listcol title="">
                                                            <c:button text="-" clientEvent="f.tsiIndex.value=$index$;" event="onClickDeleteSumInsuredItem"
                                                                      show="<%=!rotsi && !lockTSI && !defaultTSI%>" defaultRO="true"/>
                                                        </c:listcol>
                                                        <c:listcol title="{L-ENGCategory-L}{L-INAKategori-L}"
                                                                   name="stInsuranceTSIDesc2"/>
                                                        <c:listcol title="{L-ENGExc-L}{L-INAExc-L}">
                                                            <%=JSPUtil.print(tsix.getStTSIExcluded())%>
                                                        </c:listcol>
                                                        <c:listcol title="{L-ENGDescription-L}{L-INAPenjelasan-L}">
                                                            <c:field caption="Description" width="400" rows="2"
                                                                     name="selectedObject.suminsureds.[$index$].stDescription" type="string" mandatory="false"
                                                                     readonly="false"/>
                                                        </c:listcol>
                                                        <c:listcol title="{L-ENGAmount-L}{L-INAJumlah-L}">
                                                            <c:field name="selectedObject.suminsureds.[$index$].stAutoFlag" type="check"
                                                                     clientchangeaction="VM_switchReadOnly('selectedObject.suminsureds.[$index$].dbInsuredAmount',!this.checked);"
                                                                     readonly="<%=manualTSILock%>"/>
                                                            <c:field caption="Amount" name="selectedObject.suminsureds.[$index$].dbInsuredAmount"
                                                                     type="money16.2" mandatory="true" readonly="<%=rotsi%>"/>
                                                            <script>docEl('selectedObject.suminsureds.[<%=index%>].stAutoFlag').onclick();</script>
                                                        </c:listcol>
                                                    </c:listbox>
                                                    
                                                    
                                                </td>
                                            </tr>
                                            <tr>
                                                <td align=right>
                                                    {L-ENGTotal Sum Insured-L}{L-INAJumlah Harga Pertanggungan-L}
                                                    : <%=jspUtil.print(selectedObject.getDbObjectInsuredAmount(), 2)%>
                                                </td>
                                            </tr>
                                            <c:evaluate when="<%=!lockTSI%>">
                                                <tr>
                                                    <td>
                                                        ADD : <c:field width="400" lov="sumInsuredAddLOV" caption="Sum Insured" name="sumInsuredAddID"
                                                                       type="string"/>
                                                        <c:button event="doAddSumInsured" text="+" defaultRO="true"/>
                                                    </td>
                                                </tr>
                                            </c:evaluate>
                                        </table>
                                        <br><br>
                                        
                                        <%--ENDORSED COVERAGE--%>
                                        <c:evaluate when="<%=true%>">
                                            <table cellpadding=2 cellspacing=1 class=header>
                                                <tr>
                                                    <td class=header>
                                                        Coverage
                                                    </td>
                                                </tr>
                                                <tr class=row0>
                                                    <td>
                                                        <c:field name="covIndex" type="integer" hidden="true"/>
                                                        <c:listbox name="selectedObject.coverage">
                                                            <%
                                                            final InsurancePolicyCoverView cover = (InsurancePolicyCoverView) current;
                                                            final boolean hasRef = cover != null && cover.getStInsurancePolicyCoverRefID() != null;
                                                            
                                                            final boolean isVoid = cover != null && cover.isVoid();
                                                            
                                                            final boolean rocov = isVoid;
                                                            
                                                            final boolean useRateLock = cover != null && Tools.isYes(cover.getCoverPolType().getStUseRateLockFlag());
                                                            final boolean autoRateLock = cover != null && Tools.isYes(cover.getCoverPolType().getStAutoRateLockFlag());
                                                            final boolean manualTSILock = cover != null && Tools.isYes(cover.getCoverPolType().getStManualTSILockFlag());
                                                            
                                                            final boolean defaultCover = cover != null && Tools.isYes(cover.getStDefaultCoverFlag());
                                                            
                                                            String rateScaleTxt = cover == null ? null : cover.getStRateScaleSymbol();
                                                            
                                                            %>
                                                            <c:listcol title="">
                                                                <c:evaluate when="<%=!((InsurancePolicyCoverView)current).isMainCover()%>">
                                                                    <%-- <c:button text="-" clientEvent="f.covIndex.value=$index$;" event="onClickDeleteCoverageItem" show="<%=!rocov && !defaultCover%>" defaultRO="true"/>--%>
                                                                    <c:button text="-" clientEvent="f.covIndex.value=$index$;" event="onClickDeleteCoverageItem"
                                                                              show="true" defaultRO="true"/>
                                                                    
                                                                </c:evaluate>
                                                            </c:listcol>
                                                            <c:listcol title="{L-ENGCoverage Category-L}{L-INAKategori Penutupan-L}"
                                                                       name="stInsuranceCoverDesc2"/>
                                                            <c:listcol title="{L-ENGAmount-L}{L-INAJumlah-L}">
                                                                <%-- <c:field name="selectedObject.coverage.[$index$].stEntryInsuredAmountFlag" clientchangeaction="VM_switchReadOnly('selectedObject.coverage.[$index$].dbInsuredAmount',this.checked)" type="check" readonly="<%=rocov || manualTSILock%>" />
                                                                --%>                 <c:field
                                                                    name="selectedObject.coverage.[$index$].stEntryInsuredAmountFlag"
                                                                    clientchangeaction="VM_switchReadOnly('selectedObject.coverage.[$index$].dbInsuredAmount',this.checked)"
                                                                    type="check" readonly="false"/>
                                                                
                                                                
                                                                <c:field caption="Amount" name="selectedObject.coverage.[$index$].dbInsuredAmount"
                                                                         type="money16.2" mandatory="false" readonly="<%=rocov%>"/>
                                                            </c:listcol>
                                                            
                                                            <c:listcol title="Use<br> Rate">
                                                                <%--  <c:field name="selectedObject.coverage.[$index$].stEntryRateFlag" clientchangeaction="switchRates($index$)" type="check" readonly="<%=rocov || useRateLock%>" />
                                                                --%>    <c:field name="selectedObject.coverage.[$index$].stEntryRateFlag"
                                                                                 clientchangeaction="switchRates($index$)" type="check" readonly="false"/>
                                                                
                                                            </c:listcol>
                                                            
                                                            <c:listcol title="Auto<br> Rate">
                                                                <%--  <c:field name="selectedObject.coverage.[$index$].stAutoRateFlag" clientchangeaction="switchRates($index$)" type="check" readonly="<%=rocov || autoRateLock%>" />
                                                                --%>    <c:field name="selectedObject.coverage.[$index$].stAutoRateFlag"
                                                                                 clientchangeaction="switchRates($index$)" type="check" readonly="false"/>
                                                                
                                                            </c:listcol>
                                                            
                                                            
                                                            <c:listcol title="Rate">
                                                                <%--<c:field name="selectedObject.coverage.[$index$].stEntryRateFlag" clientchangeaction="VM_switchReadOnly('selectedObject.coverage.[$index$].dbRate',this.checked);VM_switchReadOnly('selectedObject.coverage.[$index$].dbPremiNew',!this.checked);VM_switchReadOnly('selectedObject.coverage.[$index$].stCalculationDesc',!this.checked)" type="check" readonly="<%=rocov%>" />--%>
                                                                <c:field caption="Rate" width="50" name="selectedObject.coverage.[$index$].dbRate"
                                                                         type="money16.5" mandatory="false" readonly="<%=rocov%>"/>
                                                                

                                                                <c:button text="<%=policy.getStRateMethodDesc()%>" event="chgRateClass" size="30"
                                                                          clientEvent="f.covIndex.value=$index$;"/>
                                                                
                                                            </c:listcol>
                                                            
                                                            <c:listcol title="{L-ENGPremi-L}{L-INAPremi-L}">
                                                                <c:field
                                                                    name="selectedObject.coverage.[$index$].stEntryPremiFlag"
                                                                    clientchangeaction="switchRates($index$)"
                                                                    type="check" readonly="false"/>
                                                                <c:field caption="Premi New" name="selectedObject.coverage.[$index$].dbPremiNew"
                                                                         type="money16.2" mandatory="false" readonly="<%=rocov%>"/>
                                                            </c:listcol>
                                                            <c:listcol title="{L-ENGCalculation-L}{L-INAPerhitungan-L}">
                                                                <c:field name="selectedObject.coverage.[$index$].stCalculationDesc" width="180" type="string"/>
                                                            </c:listcol>
                                                            <c:listcol title="Premi Total">
                                                                <c:field caption="Premi" name="selectedObject.coverage.[$index$].dbPremi" type="money16.2"
                                                                         mandatory="false" readonly="true"/>
                                                                <script>docEl('selectedObject.coverage.[<%=index%>].stEntryInsuredAmountFlag').onclick();
                                                                docEl('selectedObject.coverage.[<%=index%>].stEntryRateFlag').onclick();</script>
                                                            </c:listcol>
                                                            
                                                        </c:listbox>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        ADD : <c:field width="400" lov="coverageAddLOV" caption="Coverage" name="coverageAddID" type="string"/>
                                                        <c:button event="doAddCoverage" text="+" defaultRO="true"/>
                                                    </td>
                                                </tr>
                                            </table>
                                        </c:evaluate>
                                        
                                        
                                    </c:fieldcontrol>
                                </c:tabpage>
                                <c:tabpage name="TAB_DED">
                                    <c:fieldcontrol when="<%=cROpolicyRiskDetail%>" readonly="true">
                                        <table cellpadding=2 cellspacing=1 class=header>
                                            <tr>
                                                <td class=header>
                                                    Deductibles
                                                </td>
                                            </tr>
                                            <tr class=row0>
                                                <td>
                                                    <c:field name="objDeductIndex" type="string" hidden="true"/>
                                                    <c:listbox name="selectedObject.deductibles">
                                                        <c:listcol title="" columnClass="header">
                                                            <c:button text="+" event="onNewObjDeductible" validate="false" defaultRO="true"/>
                                                        </c:listcol>
                                                        <c:listcol title="" columnClass="detail">
                                                            <c:button text="-" event="onDeleteObjDeductible"
                                                                      clientEvent="f.objDeductIndex.value='$index$';" validate="false"
                                                                      defaultRO="true"/>
                                                        </c:listcol>
                                                       
                                                        <c:listcol title="{L-ENGClaim Cause-L}{L-INAPenyebab Klaim-L}">
                                                            <c:field lov="LOV_ClaimCause" caption="{L-ENGClaim Cause-L}{L-INAPenyebab Klaim-L}"
                                                                     name="selectedObject.deductibles.[$index$].stInsuranceClaimCauseID"
                                                                     type="string|128" width="200" mandatory="true" readonly="false">
                                                                <c:lovLink name="poltype" link="policy.stPolicyTypeID" clientLink="false"/>
                                                            </c:field>
                                                        </c:listcol>
                                                        <c:listcol title="{L-ENGCurrency-L}{L-INAMata Uang-L}">
                                                            <c:field lov="LOV_Currency" name="selectedObject.deductibles.[$index$].stCurrencyCode"
                                                                     type="string" width="50" mandatory="false" readonly="false"/>
                                                        </c:listcol>
                                                        <c:listcol title="{L-ENGAmount-L}{L-INAJumlah-L}">
                                                            <c:field name="selectedObject.deductibles.[$index$].dbAmount" type="money16.2" width="100"
                                                                     mandatory="false" readonly="false"/>
                                                        </c:listcol>
                                                        <c:listcol title="PCT">
                                                            <c:field name="selectedObject.deductibles.[$index$].dbPct" type="money5.2" width="40"
                                                                     mandatory="false" readonly="false"/>%
                                                        </c:listcol>
                                                        <c:listcol title="{L-ENGType-L}{L-INAJenis-L}">
                                                            <c:field name="selectedObject.deductibles.[$index$].stDeductType" lov="VS_DEDUCT"
                                                                     type="string|128" width="150" mandatory="false" readonly="false"/>
                                                        </c:listcol>
                                                        <c:listcol title="Min">
                                                            <c:field name="selectedObject.deductibles.[$index$].dbAmountMin" type="money16.2"
                                                                     width="100" mandatory="false" readonly="false"/>
                                                        </c:listcol>
                                                        <c:listcol title="Max">
                                                            <c:field name="selectedObject.deductibles.[$index$].dbAmountMax" type="money16.2"
                                                                     width="100" mandatory="false" readonly="false"/>
                                                        </c:listcol>
                                                        <%--
                        <c:listcol title="{L-ENGType-L}{L-INAJenis-L}" >
                          <c:field name="selectedObject.deductibles.[$index$].stDeductTypePer" lov="VS_DEDUCT_TYPE" type="string|128" width="150" mandatory="false" readonly="false"/>
                        </c:listcol>
                        --%>
                                                    </c:listbox>
                                                </td>
                                            </tr>
                                        </table>
                                    </c:fieldcontrol>
                                </c:tabpage>
                                
                                <c:evaluate when="<%=policy.getStCoverTypeCode()!=null%>">
                                    <c:tabpage name="TAB_REINS">
                                        <%--  <c:fieldcontrol when="<%=!cROReas%>" readonly="true" >--%>
                                        <c:field name="treatiesIndex" type="string" hidden="true"/>
                                        <c:field name="treatyDetailIndex" type="string" hidden="true"/>
                                        
                                        <table cellpadding=2 cellspacing=1>
                                            <%--  <c:field name="policy.stInsuranceTreatyID" mandatory="true" width="200" changeaction="selectTreaty" readonly="<%=policy.getStInsuranceTreatyID()!=null%>" caption="Treaty" type="string" lov="lovTreaty" presentation="standard" />
                                                --%>
                                            <c:field name="selectedObject.stInsuranceTreatyID" mandatory="true" width="200" changeaction="selectTreaty"
                                                     readonly="<%=selectedObject.getStInsuranceTreatyID()!=null%>" caption="Treaty" type="string"
                                                     lov="lovTreaty" presentation="standard"/>
                                            
                                        </table>

                                        <table cellpadding=2 cellspacing=1 width="100%">
                                            <tr>
                                                <td>
                                                    <table cellpadding=2 cellspacing=1>
                                                        <tr>
                                                            <td>{L-ENGRisk Category-L}{L-INAKategori Resiko-L}</td>
                                                            <td>:</td>
                                                            <td><%=JSPUtil.print(selectedObject.getStRiskCategoryDesc())%>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>{L-ENGExc. Risk-L}{L-INAExc. Risk-L}</td>
                                                            <td>:</td>
                                                            <td><%=JSPUtil.print(selectedObject.getStRiskCategoryExcluded())%>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>{L-ENGRisk Class-L}{L-INAKelas Resiko-L}</td>
                                                            <td>:</td>
                                                            <td><%=JSPUtil.print(selectedObject.getStRiskClass())%>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                    
                                                    <table cellpadding=2 cellspacing=1>
                                                        <tr>
                                                            <td>{L-ENGTable Of Limit-L}{L-INATable Of Limit-L}</td>
                                                            <td>:</td>
                                                            <td align=right><%=JSPUtil.print(selectedObject.getDbTreatyLimitRatio(), 0)%>%</td>
                                                        </tr>
                                                        <% if (policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_EQUAKE")) {%>
                                                        <tr>
                                                            <td>{L-ENGTreaty Limit Ratio EQ-L}{L-INARasio Limit Treaty EQ-L}</td>
                                                            <td>:</td>
                                                            <td align=right><%=JSPUtil.print(selectedObject.getDbTreatyLimitRatioMaipark(), 0)%>%</td>
                                                        </tr>
                                                        <% } %>
                                                        <tr>
                                                            <td>Kurs Treaty</td>
                                                            <td>:</td>
                                                            <td align=right><%=JSPUtil.print(policy.getDbCurrencyRateTreaty(), 2)%>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>TSI</td>
                                                            <td>:</td>
                                                            <td align=right><%=JSPUtil.print(selectedObject.getDbObjectInsuredAmountShare(), 2)%>
                                                            </td>
                                                        </tr>
                                                        <% if (!directPolicy) {%>
                                                        <tr>
                                                            <td>Cession PCT</td>
                                                            <td>:</td>
                                                            <td align=right><%=JSPUtil.print(selectedObject.getDbCoinsSessionPct())%>%</td>
                                                        </tr>
                                                        <% } %>
                                                        
                                                        <tr>
                                                            <td>{L-ENGPremium-L}{L-INAPremi-L}</td>
                                                            <td>:</td>
                                                            <td align=right><%=JSPUtil.print(selectedObject.getDbObjectPremiTotalAmount(), 2)%>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                                <td align=center>
                                                    <table cellpadding=2 cellspacing=1 class=row0 width="100%">
                                                        <%
                                                        
                                                            {
                                                                final DTOList ctr1 = selectedObject.getTreaties();
                                                                
                                                                for (int i = 0; i < ctr1.size(); i++) {
                                                                    InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) ctr1.get(i);
                                                                    
                                                                    final DTOList ctr2 = tre.getDetails();
                                                                    
                                                                    for (int j = 0; j < ctr2.size(); j++) {
                                                                        InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) ctr2.get(j);
                                                        
                                                        %>
                                                        <tr class=row0>
                                                            <td><%=trd.getStTreatyClassDesc()%>
                                                            </td>
                                                            <td>:</td>
                                                            <td align=right><%=JSPUtil.print(trd.getDbTSIAmount(), 2)%>
                                                            </td>
                                                            
                                                        </tr>
                                                        <%
                                                                    }
                                                                }
                                                            }
                                                        %>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                        
                                        <c:field name="idxTreaty" type="string" hidden="true"/>
                                        <c:field name="idxTreatyDetail" type="string" hidden="true"/>
                                        <c:field name="idxTreatyShares" type="string" hidden="true"/>
                                        
                                        
                                        <table cellpadding=2 cellspacing=1>
                                            <%
                                            final DTOList treaties = selectedObject.getTreaties();
                                            
                                            for (int i = 0; i < treaties.size(); i++) {
                                                InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(i);
                                            %>
                                            <tr>
                                                <td>
                                                    <table cellpadding=2 cellspacing=1 class=header width="100%">
                                                        <tr>
                                                            <td>Treaty : <%=JSPUtil.print(tre.getStInsuranceTreatyDesc())%>
                                                            </td>
                                                            <td align=right>
                                                                <c:button text="Reset" confirm="Are you sure want to reset treaty ?" event="resetTreaty"
                                                                          defaultRO="true"/>
                                                                <c:button text="{L-ENGChange Treaty-L}{L-INAUbah Treaty-L}"
                                                                          confirm="Are you sure want to change treaty ?" event="changeTreaty" defaultRO="true"/>
                                                                <c:button text="{L-ENGChange Treaty/Object-L}{L-INAUbah Treaty/Objek-L}"
                                                                          confirm="Are you sure want to change treaty ?" event="changeTreatyPerObject"
                                                                          defaultRO="true"/>
                                                                
                                                                <%--  <c:button text="+" event="raiseTreatyLevel" />
                     <c:button text="-" event="lowerTreatyLevel" />--%>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <c:tab name="ritabs">
                                                        <%
                                                        final DTOList treDetails = tre.getDetails();
                                                        
                                                        
                                                        for (int j = 0; j < treDetails.size(); j++) {
                                                            InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) treDetails.get(j);
                                                            
                                                            final InsuranceTreatyDetailView tdr = tredet.getTreatyDetail();
                                                            
                                                            final InsuranceTreatyTypesView treatyType = tdr.getTreatyType();
                                                            
                                                            final boolean freeMembers = Tools.isYes(treatyType.getStFreeMembersFlag());
                                                            final boolean nonProportional = Tools.isYes(treatyType.getStNonProportionalFlag());
                                                            final boolean orShare = Tools.isYes(treatyType.getStORShareFlag());
                                                            final boolean freeTSUI = Tools.isYes(treatyType.getStFreeTSIFlag());
                                                            
                                                            final boolean hasShares = tredet.getShares().size() > 0;
                                                            
                                                            final boolean isOR = tdr.isOR();
                                                            
                                                            final String style = isOR ? "row2" : "row0";
                                                            
                                                            boolean canChangeMember = freeMembers;
                                                            
                                                            final boolean hasRISlip = nonProportional;
                                                            
                                                            final boolean isBppdan = tdr.isBPDAN();
                                                            
                                                            tdr.getDbXOLLower();
                                                            tdr.getDbXOLUpper();
                                                        
                                                        %>
                                                        <c:tabpage name="<%=tredet.getStInsuranceTreatyDetailID()%>">
                                                            <table cellpadding=2 cellspacing=1 class=row0 width="100%" height="100%">
                                                                <tr class=row0>
                                                                    <td>
                                                                        <table cellpadding=2 cellspacing=1 class=row0>
                                                                            <tr>
                                                                                <td>
                                                                                    <table cellpadding=2 cellspacing=1>
                                                                                        <tr>
                                                                                            <td colspan=2 class=header>
                                                                                                <%=JSPUtil.print(tredet.getStTreatyClassDesc())%>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <%
                                                                                        if (form.isManualTreaty()) {
                                                                                            special_treaty = true;
                                                                                            canChangeMember = special_treaty;
                                                                                        }
                                                                                        %>
                                                                                        <tr>
                                                                                            <td valign=top>
                                                                                                <table cellpadding=2 cellspacing=1>
                                                                                                    <%--
                                    <c:field name="<%="treaties.["+i+"].details.["+j+"].dbTreatyLimit"%>" width="200" readonly="<%=!freeTSUI||!special_treaty%>" caption="<%=JSPUtil.print(label)%>" type="money16.2" presentation="standard" />
                                    --%>
                                                                                                    <c:field name="<%="treaties.["+i+"].details.["+j+"].dbTreatyLimit"%>" width="200"
                                                                                                             readonly="<%=!special_treaty%>" caption="{L-ENGTreaty Limit-L}{L-INALimit Treaty-L}"
                                                                                                             type="money16.2" presentation="standard"/>
                                                                                                    <c:field name="<%="treaties.["+i+"].details.["+j+"].dbTSIPct"%>"
                                                                                                             readonly="<%=!special_treaty%>" caption="TSI Share" type="money16.2"
                                                                                                             presentation="standard" width="60" suffix=" %"/>
                                                                                                    <%--  <c:field name="<%="treaties.["+i+"].details.["+j+"].dbTreatyLimitRatio"%>" readonly="<%=!special_treaty%>" caption="Limit Ratio" type="money16.2" presentation="standard" width="60" suffix=" %" />
                                                                                 --%>
                                                                                                    <c:field name="<%="treaties.["+i+"].details.["+j+"].dbTSIAmount"%>"
                                                                                                             caption="{L-ENGTSI Amount-L}{L-INAJumlah TSI-L}" width="200" type="money16.2"
                                                                                                             presentation="standard" readonly="<%=!special_treaty%>"/>
                                                                                                    <c:field name="<%="treaties.["+i+"].details.["+j+"].stEditFlag"%>" readonly="false"
                                                                                                             caption="{L-ENGManual TSI-L}{L-INAManual TSI-L}" type="check"
                                                                                                             presentation="standard" readonly="<%=!special_treaty%>"/>
                                                                                                    
                                                                                                    <c:evaluate when="<%=orShare%>">
                                                                                                        <c:field name="<%="treaties.["+i+"].details.["+j+"].treatyDetail.dbXOLLower"%>"
                                                                                                                 width="200" caption="XOL Low" type="money16.2" presentation="standard"
                                                                                                                 readonly="true"/>
                                                                                                        <c:field name="<%="treaties.["+i+"].details.["+j+"].treatyDetail.dbXOLUpper"%>"
                                                                                                                 width="200" caption="XOL Up" type="money16.2" presentation="standard"
                                                                                                                 readonly="true"/>
                                                                                                    </c:evaluate>
                                                                                                </table>
                                                                                            </td>
                                                                                            <td valign=top>
                                                                                                <table cellpadding=2 cellspacing=1>
                                                                                                    <c:field name="<%="treaties.["+i+"].details.["+j+"].dbPremiRatePct"%>" readonly="true"
                                                                                                             caption="{L-ENGPremi Rate-L}{L-INARate Premi-L}" type="money16.2"
                                                                                                             presentation="standard" suffix=" %" width="60"/>
                                                                                                    <c:field name="<%="treaties.["+i+"].details.["+j+"].dbPremiAmount"%>" readonly="true"
                                                                                                             caption="{L-ENGPremium Amount-L}{L-INAJumlah Premi-L}" type="money16.2"
                                                                                                             presentation="standard"/>
                                                                                                    <c:field name="<%="treaties.["+i+"].details.["+j+"].dbComissionRate"%>" readonly="true"
                                                                                                             caption="{L-ENGComission Rate-L}{L-INARate Komisi-L}" type="money6.2"
                                                                                                             width="60" presentation="standard" suffix=" %"/>
                                                                                                    <c:field name="<%="treaties.["+i+"].details.["+j+"].dbComission"%>" readonly="true"
                                                                                                             caption="{L-ENGComission Amount-L}{L-INAJumlah Komisi-L}" type="money16.2"
                                                                                                             presentation="standard"/>
                                                                                                </table>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <c:evaluate when="<%=hasShares || canChangeMember%>">
                                                                                            <tr>
                                                                                                <td colspan=2>
                                                                                                    <table cellpadding=2 cellspacing=1 class=header>
                                                                                                        <tr class=header>
                                                                                                            <td><c:button text="+" show="<%=canChangeMember%>" event="addTreatyShare"
                                                                                                                              clientEvent="<%="docEl('idxTreaty').value="+i+";docEl('idxTreatyDetail').value="+j+";"%>"
                                                                                                                          defaultRO="true"/></td>
                                                                                                            <td>{L-ENGCompany-L}{L-INAPerusahaan-L}</td>
                                                                                                            <td>Share</td>
                                                                                                            <td>TSI</td>
                                                                                                            <td>Use<br>Rate</td>
                                                                                                            <td>Auto<br>Rate</td>
                                                                                                            <td>Premi Rate</td>
                                                                                                            <td>Premium</td>
                                                                                                            <td>{L-ENGCommission-L}{L-INAKomisi-L}<br>Rate</td>
                                                                                                            <td>{L-ENGCommission-L}{L-INAKomisi-L}</td>
                                                                                                            <c:evaluate when="<%=hasRISlip%>">
                                                                                                                <td>R/I Slip</td>
                                                                                                            </c:evaluate>
                                                                                                            <td>{L-ENGNotes-L}{L-INACatatan-L}</td>
                                                                                                            <c:evaluate when="<%=isBppdan%>">
                                                                                                            <td>{L-ENGR/I Date-L}{L-INATanggal R/I-L}</td>
                                                                                                            </c:evaluate>
                                                                                                            <td>ACC</td>
                                                                                                        </tr>
                                                                                                        <%
                                                                                                        final DTOList treShares = tredet.getShares();
                                                                                                        
                                                                                                        for (int k = 0; k < treShares.size(); k++) {
                                                                                                            InsurancePolicyReinsView ri = (InsurancePolicyReinsView) treShares.get(k);
                                                                                                        %>
                                                                                                        <tr class=row0>
                                                                                                            <td><c:button text="-" show="<%=canChangeMember%>"
                                                                                                                              clientEvent="<%="docEl('idxTreaty').value="+i+";docEl('idxTreatyDetail').value="+j+";docEl('idxTreatyShares').value="+k+";"%>"
                                                                                                                          event="deleteTreatyShare" defaultRO="true"/></td>
                                                                                                            <td><c:field
                                                                                                                    name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].stMemberEntityID"%>"
                                                                                                                    width="130" lov="LOV_InsuranceCompany" popuplov="true" type="string"
                                                                                                                    caption="{L-ENGCompany-L}{L-INAPerusahaan-L}"
                                                                                                                readonly="<%=!freeMembers%>"/></td>
                                                                                                            <td><c:field
                                                                                                                    name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].dbSharePct"%>"
                                                                                                                    width="40" type="money6.2" caption="Share Pct"
                                                                                                                    readonly="<%=!canChangeMember%>"/>%
                                                                                                            </td>
                                                                                                            <td><c:field
                                                                                                                    name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].dbTSIAmount"%>"
                                                                                                                    width="80" type="money16.2" caption="TSI"
                                                                                                                readonly="<%=!canChangeMember%>"/></td>
                                                                                                            
                                                                                                            <td><c:field
                                                                                                                    name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].stUseRateFlag"%>"
                                                                                                                    clientchangeaction="<%="switchRIRates("+i+","+j+","+k+")"%>"
                                                                                                                type="check" readonly="false"/></td>
                                                                                                            <td><c:field
                                                                                                                    name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].stAutoRateFlag"%>"
                                                                                                                    clientchangeaction="<%="switchRIRates("+i+","+j+","+k+")"%>"
                                                                                                                type="check" readonly="false"/></td>
                                                                                                            <td><c:field caption="Rate" width="40"
                                                                                                                             name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].dbPremiRate"%>"
                                                                                                                             precision="4" type="money16.5" mandatory="false"
                                                                                                                             readonly="false"/>%
                                                                                                            </td>
                                                                                                            <td><c:field
                                                                                                                    name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].dbPremiAmount"%>"
                                                                                                                    width="80" type="money16.2" caption="Premi"
                                                                                                                readonly="<%=!canChangeMember%>"/></td>
                                                                                                            <td><c:field
                                                                                                                    name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].dbRICommRate"%>"
                                                                                                                    width="40" type="money16.2" caption="Comm Rate"
                                                                                                                    readonly="<%=!canChangeMember%>"/>%
                                                                                                            </td>
                                                                                                            <td><c:field
                                                                                                                    name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].dbRICommAmount"%>"
                                                                                                                width="80" type="money16.2" caption="Comm" readonly="false" /></td>
                                                                                                            <c:evaluate when="<%=hasRISlip%>">
                                                                                                                <td><c:field
                                                                                                                        name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].stRISlipNo"%>"
                                                                                                                        width="80" type="string" caption="R/I Slip"
                                                                                                                    readonly="<%=!canChangeMember%>"/></td>
                                                                                                            </c:evaluate>
                                                                                                            <td><c:field
                                                                                                                    name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].stNotes"%>"
                                                                                                                    width="150" type="string" rows="2"
                                                                                                                caption="{L-ENGNotes-L}{L-INACatatan-L}" readonly="false"/></td>
                                                                                                                <c:evaluate when="<%=isBppdan%>">
                                                                                                                <td><c:field
                                                                                                                    name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].dtValidReinsuranceDate"%>"
                                                                                                                    type="date"
                                                                                                                caption="{L-ENGNotes-L}{L-INACatatan-L}" readonly="false"/></td>
                                                                                                                </c:evaluate>
                                                                                                            <td><c:field
                                                                                                                    name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].stApprovedFlag"%>"
                                                                                                                type="check" caption="ACC" readonly="false"/></td>
                                                                                                            <script><%="switchRIRates(" + i + "," + j + "," + k + ")"%>
                                                                                                            </script>
                                                                                                        </tr>
                                                                                                        <% } %>
                                                                                                    </table>
                                                                                                </td>
                                                                                            </tr>
                                                                                            
                                                                                        </c:evaluate>
                                                                                        <tr>
                                                                                        <td colspan=2>
                                                                                            <br><br>
                                                                                        </td>
                                                                                    </table>
                                                                                </td>
                                                                            </tr>
                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </c:tabpage>
                                                        <% } %>
                                                    </c:tab>
                                                </td>
                                            </tr>
                                            <%
                                            
                                            }
                                            %>
                                        </table>
                                        <%--  </c:fieldcontrol>--%>
                                    </c:tabpage>
                                </c:evaluate>
                            </c:tab>
                        </c:evaluate>
                    </td>
                </tr>
            </table>
        </c:tabpage>
        
        
        <%-- if(!policy.getStStatus().equalsIgnoreCase("PROPOSAL")){ --%>
        <c:tabpage name="TAB_CLAUSES">
            <c:fieldcontrol when="<%=cROpolicyClausules%>" readonly="true">
                <c:listbox name="clausules">
                    <c:listcol title="">
                        <c:field name="clausules.[$index$].stSelectedFlag" type="check" mandatory="false" readonly="false"/>
                    </c:listcol>
                    <c:listcol name="stDescription" title="{L-ENGDESCRIPTION-L}{L-INADESKRIPSI-L}">
                    </c:listcol>
                    <%--
              <c:listcol title="{L-ENGDESCRIPTION-L}{L-INADESKRIPSI-L}" >
              <c:field name="clausules.[$index$].stDescription2" type="string" width="200" mandatory="false" readonly="false"/>
            </c:listcol>--%>
                </c:listbox>
            </c:fieldcontrol>
        </c:tabpage>
        <%-- } --%>
        
        <c:tabpage name="TAB_DEDUCTIBLES">
            <c:field name="deductIndex" type="string" hidden="true"/>
            <c:listbox name="deductibles">
                <c:listcol title="" columnClass="header">
                    <c:button text="+" event="onNewDeductible" validate="false"/>
                </c:listcol>
                <c:listcol title="" columnClass="detail">
                    <c:button text="-" event="onDeleteDeductible" clientEvent="f.deductIndex.value='$index$';" validate="false"/>
                </c:listcol>
                <%--<c:listcol title="Description" >
          <c:field name="deductibles.[$index$].stDescription" type="string|128" width="200" mandatory="false" readonly="false"/>
        </c:listcol>--%>
                <c:listcol title="{L-ENGClaim Cause-L}{L-INAPenyebab Klaim-L}">
                    <c:field lov="LOV_ClaimCause" name="deductibles.[$index$].stInsuranceClaimCauseID" type="string|128"
                             width="200" mandatory="true" readonly="false"/>
                </c:listcol>
                <c:listcol title="{L-ENGCurrency-L}{L-INAMata Uang-L}">
                    <c:field lov="LOV_Currency" name="deductibles.[$index$].stCurrencyCode" type="string" width="50"
                             mandatory="false" readonly="false"/>
                </c:listcol>
                <c:listcol title="{L-ENGAmount-L}{L-INAJumlah-L}">
                    <c:field name="deductibles.[$index$].dbAmount" type="money16.2" width="100" mandatory="false"
                             readonly="false"/>
                </c:listcol>
                <c:listcol title="PCT">
                    <c:field name="deductibles.[$index$].dbPct" type="money5.2" width="40" mandatory="false" readonly="false"/>%
                </c:listcol>
                <c:listcol title="{L-ENGType-L}{L-INAJenis-L}">
                    <c:field name="deductibles.[$index$].stDeductType" lov="VS_DEDUCT" type="string|128" width="150"
                             mandatory="false" readonly="false"/>
                </c:listcol>
                <c:listcol title="Min">
                    <c:field name="deductibles.[$index$].dbAmountMin" type="money16.2" width="100" mandatory="false"
                             readonly="false"/>
                </c:listcol>
                <c:listcol title="Max">
                    <c:field name="deductibles.[$index$].dbAmountMax" type="money16.2" width="100" mandatory="false"
                             readonly="false"/>
                </c:listcol>
            </c:listbox>
        </c:tabpage>
        <c:tabpage name="TAB_CLAIM_ITEM">
            <c:fieldcontrol>
                <c:field name="claimItemindex" type="string" hidden="true"/>
                <c:listbox name="claimItems">
                    <%
                    final DTOList clmItems = form.getClaimItems();
                    final InsurancePolicyItemsView item = ((InsurancePolicyItemsView) clmItems.get(index.intValue()));
                    
                    final boolean deletable = item != null && item.isDeletable();
                    
                    
                    final boolean neg = item != null && item.getInsItem().getARTrxLine().isNegative();
                    
                    final boolean enableEntity = item != null && Tools.isYes(item.getInsItem().getStEntityFlag());

                    final boolean enableTax = item != null && item.getInsItem().isUseTax();
                    
                    %>
                    <c:listcol title="" columnClass="header">
                        
                    </c:listcol>
                    <c:listcol title="" columnClass="detail">
                        <c:button text="-" event="onDeleteClaimItem" clientEvent="f.claimItemindex.value='$index$';"
                                  validate="false" defaultRO="true" show="<%=!ro && deletable%>"/>
                    </c:listcol>
                    <c:listcol name="InsItem.stDescription" title="ITEM"/>
                    <c:listcol title="KETERANGAN">
                        <c:field caption="Description" name="claimItems.[$index$].stDescription" width="160" rows="2"
                                 type="string|255" mandatory="false" readonly="false"/>
                    </c:listcol>
                    <c:listcol title="RATE">
                        <c:evaluate when="<%=deletable%>">
                            <c:field name="claimItems.[$index$].stFlagEntryByRate"
                                     clientchangeaction="VM_switchReadOnly('claimItems.[$index$].dbRate',this.checked);VM_switchReadOnly('claimItems.[$index$].dbAmount',!this.checked)"
                                     type="check"/>
                            <c:field caption="Rate" name="claimItems.[$index$].dbRate" width="50" type="money4.1"
                                     mandatory="false" readonly="false"/> %
                        </c:evaluate>
                    </c:listcol>
                    <c:listcol title="+/-">
                        <%=neg ? "-" : "+"%>
                    </c:listcol>
                    <c:listcol title="JUMLAH">
                        <c:field caption="Amount" name="claimItems.[$index$].dbAmount" type="money16.2" mandatory="false"
                                 readonly="<%=!deletable%>"/>
                        <c:evaluate when="<%=!ro && deletable%>">
                            <script>docEl('claimItems.[<%=index%>].stFlagEntryByRate').onclick();</script>
                        </c:evaluate>
                    </c:listcol>
                    <c:listcol title="RI Chrg">
                        <c:field name="claimItems.[$index$].stChargableFlag" type="check" readonly="true"/>
                    </c:listcol>
                    <%--<c:listcol title="Payable/Receivable">
                        <c:field name="claimItems.[$index$].stEntityID" width="180"
                                 descfield="claimItems.[$index$].stEntityName" caption="Payable/Receivable" type="string"
                                 lov="LOV_Entity" readonly="<%=!enableEntity%>" popuplov="true"/>
                    </c:listcol>--%>

            <c:listcol title="Payable/Receivable" align="center">
                <c:evaluate when="<%=item!=null && enableEntity%>">
                    <c:field name="claimItems.[$index$].stEntityID" hidden="true" type="string"/>
                    <c:field caption="Payable/Receivable" name="claimItems.[$index$].stEntityName" width="190" type="string|255"
                             mandatory="false" clientchangeaction="f.claimItemindex.value='$index$'; setTaxCodeOnClaim();" />
                    <c:evaluate when="<%=deletable%>">
                        <c:button text="..." clientEvent="selectAgent($index$)" defaultRO="true"/>
                    </c:evaluate>
                </c:evaluate>
            </c:listcol>

            <c:listcol title="LKP" align="center">
                <c:evaluate when="<%=item.isInsentifSubrogasi()%>">
                    <c:field lov="LOV_CLAIM_DLA" name="claimItems.[$index$].stReferencePolicyID"
                             caption="LKP" type="string" width="150" popuplov="true" mandatory="false" readonly="false"/>
                </c:evaluate>
            </c:listcol>

            <c:listcol title="Metode" align="center">
                <c:evaluate when="<%=item.isInsentifSubrogasi()%>">
                    <c:field lov="VS_CALC_MODE" name="claimItems.[$index$].stCalculationMode"
                             caption="Metode" type="string" width="150" mandatory="false" readonly="false"/>
                </c:evaluate>
            </c:listcol>

            
            <c:evaluate when="<%=item!=null && enableTax %>">
    </tr>
    <tr class=row2>
    <td class=detail></td>
    <c:listcol title="ITEMS">{L-INAPAJAK-L}{L-ENGTAX-L}</c:listcol>
    <c:listcol title="DESCRIPTION">
        <c:field lov="LOV_ARTax" caption="TAX" width="160" name="claimItems.[$index$].stTaxCode" type="string"
                 mandatory="true" >
        </c:field>
    </c:listcol>
     <%
        final boolean readonlyTax = true;
        
        %>
    <c:listcol title="RATE">
        <c:field name="claimItems.[$index$].stTaxAutoRateFlag"
                 clientchangeaction="VM_switchReadOnly('claimItems.[$index$].dbTaxRatePct',this.checked);"
                 type="check" readonly="<%=readonlyTax%>" />
        <c:field caption="Rate" name="claimItems.[$index$].dbTaxRatePct" width="50" type="money16.3"
                 mandatory="false" readonly="<%=readonlyTax%>"/> %
    </c:listcol>
    <c:listcol title="RATE">
    </c:listcol>    
    <c:listcol title="AMOUNT">
        <c:field name="claimItems.[$index$].stTaxAutoAmountFlag"
                 clientchangeaction="VM_switchReadOnly('claimItems.[$index$].dbTaxAmount',this.checked);"
                 type="check" readonly="<%=!deletable%>"/>
        <c:field caption="Amount" name="claimItems.[$index$].dbTaxAmount" type="money16.2" mandatory="false"
                 readonly="false"/>
        <c:evaluate when="<%=!ro%>">
            <script>docEl('claimItems.[<%=index%>].stTaxAutoRateFlag').onclick();</script>
        </c:evaluate>
        <c:evaluate when="<%=!ro%>">
            <script>docEl('claimItems.[<%=index%>].stTaxAutoAmountFlag').onclick();</script>
        </c:evaluate>
    </c:listcol>
    <c:listcol title="RATE">
    </c:listcol> 
    <c:listcol title="AGENT & NPWP">
        
        <c:evaluate when="<%=item!=null && enableTax%>">
            NPWP <c:field caption="NPWP" name="claimItems.[$index$].stNPWP" width="180" type="string"
                          mandatory="false" />
        </c:evaluate>
    </c:listcol>
    </c:evaluate>
    
                </c:listbox>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            item :
                            <%--<c:field name="insItemID" type="string" lov="LOV_InsuranceItem">
                           <c:lovLink name="coversrc" link="policy.stCoverTypeCode"/>
                        </c:field>--%>
                            <c:field name="insItemID" type="string" lov="LOV_ClaimItem" width="250"/>
                            <c:button text="+" event="onNewClaimItem" validate="false" defaultRO="true"/>
                        </td>
                    </tr>
                </table>
            </c:fieldcontrol>
        </c:tabpage>
        <c:evaluate when="<%=policy.getClaimObject()!=null%>">
            <c:tabpage name="TAB_CLAIM_RE">
                <c:field name="treatiesIndex" type="string" hidden="true"/>
                <c:field name="treatyDetailIndex" type="string" hidden="true"/>

                <table cellpadding=2 cellspacing=1>

                    <c:field caption="Klaim Cash Call" name="policy.stClaimCashCallFlag" type="check" mandatory="false"
                                                                     readonly="false" presentation="standard"/>
                    <c:field name="claimObject.stInsuranceTreatyID" mandatory="true" width="200" changeaction="selectClaimTreaty"
                             readonly="<%=claimObject.getStInsuranceTreatyID()!=null%>" caption="Treaty" type="string"
                             lov="lovTreaty" presentation="standard"/>
                </table>

                <table cellpadding=2 cellspacing=1 width="100%">
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <tr>
                                    <td>{L-ENGRisk Category-L}{L-INAKategori Resiko-L}</td>
                                    <td>:</td>
                                    <td><%=JSPUtil.print(claimObject.getStRiskCategoryDesc())%>
                                    </td>
                                </tr>
                                <tr>
                                    <td>{L-ENGRisk Class-L}{L-INAKelas Resiko-L}</td>
                                    <td>:</td>
                                    <td><%=JSPUtil.print(claimObject.getStRiskClass())%>
                                    </td>
                                </tr>
                            </table>
                            
                            <table cellpadding=2 cellspacing=1>
                                <tr>
                                    <td>{L-ENGTreaty Limit Ratio-L}{L-INARasio Limit Treaty-L}</td>
                                    <td>:</td>
                                    <td align=right><%=JSPUtil.print(claimObject.getDbTreatyLimitRatio(), 0)%>%</td>
                                </tr>
                                <%if(claimObject.getDbLimitOfLiability()!=null){%>
                                    <tr>
                                        <td>Limit Of Liability (LOL)</td>
                                        <td>:</td>
                                        <td align=right><%=JSPUtil.print(claimObject.getDbLimitOfLiability(), 0)%>%</td>
                                    </tr>
                                <%}%>
                                <tr>
                                    <td>TSI</td>
                                    <td>:</td>
                                    <td align=right><%=JSPUtil.print(claimObject.getDbObjectInsuredAmountShare(), 2)%>
                                    </td>
                                </tr>
                                <%if(claimObject.getDbLimitOfLiability()!=null){%>
                                    <tr>
                                        <td>TSI (LOL)</td>
                                        <td>:</td>
                                        <td align=right><%=JSPUtil.print(BDUtil.mul(claimObject.getDbObjectInsuredAmountShare(),BDUtil.getRateFromPct(claimObject.getDbLimitOfLiability()), 2),2)%>
                                        </td>
                                    </tr>
                                <%}%>
                                <%--<tr><td>Premium</td><td>:</td><td align=right><%=JSPUtil.print(claimObject.getDbObjectPremiTotalAmount(), 2)%></td></tr>--%>
                            </table>
                        </td>
                        <td align=center>
                            <table cellpadding=2 cellspacing=1 class=row0 width="100%">
                                <%
                                
                                {
                        final DTOList ctr1 = claimObject.getTreaties();
                        
                        for (int i = 0; i < ctr1.size(); i++) {
                            InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) ctr1.get(i);
                            
                            final DTOList ctr2 = tre.getDetails();
                            
                            for (int j = 0; j < ctr2.size(); j++) {
                                InsurancePolicyTreatyDetailView trd = (InsurancePolicyTreatyDetailView) ctr2.get(j);
                                
                                %>
                                <tr class=row0>
                                    <td><%=trd.getStTreatyClassDesc()%>
                                    </td>
                                    <td>:</td>
                                    <td align=right><%=JSPUtil.print(trd.getDbClaimAmount(), 2)%>
                                    </td>
                                </tr>
                                <%
                                
                                }
                        }
                                }
                                %>
                            </table>
                        </td>
                    </tr>

                </table>
                
                <c:field name="idxTreaty" type="string" hidden="true"/>
                <c:field name="idxTreatyDetail" type="string" hidden="true"/>
                <c:field name="idxTreatyShares" type="string" hidden="true"/>
                
                
                <table cellpadding=2 cellspacing=1>
                    <%
                    final DTOList treaties = form.getClaimTreaties();
                    
                    for (int i = 0; i < treaties.size(); i++) {
                        InsurancePolicyTreatyView tre = (InsurancePolicyTreatyView) treaties.get(i);
                    %>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1 class=header width="100%">
                                <tr>
                                    <td>Treaty : <%=JSPUtil.print(tre.getStInsuranceTreatyDesc())%>
                                    </td>
                                    <td align="right">
                                        <c:button text="Update"
                                                  confirm="Are you sure want to update treaty ?" event="updateTreaty" defaultRO="true"/>
                                        <c:button text="Reset" confirm="Are you sure want to reset treaty ?" event="resetTreaty"
                                                                                      defaultRO="true"/>
                                        <c:button text="{L-ENGChange Treaty-L}{L-INAUbah Treaty-L}"
                                                  confirm="Are you sure want to change treaty ?" event="changeTreaty" defaultRO="true"/>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <c:tab name="claimritabs">
                                <%
                                final DTOList treDetails = tre.getDetails();
                                
                                
                                for (int j = 0; j < treDetails.size(); j++) {
                                    InsurancePolicyTreatyDetailView tredet = (InsurancePolicyTreatyDetailView) treDetails.get(j);
                                    
                                    final InsuranceTreatyDetailView tdr = tredet.getTreatyDetail();
                                    
                                    final InsuranceTreatyTypesView treatyType = tdr.getTreatyType();
                                    
                                    final boolean freeMembers = Tools.isYes(treatyType.getStFreeMembersFlag());
                                    final boolean nonProportional = Tools.isYes(treatyType.getStNonProportionalFlag());
                                    final boolean orShare = Tools.isYes(treatyType.getStORShareFlag());
                                    final boolean freeTSUI = Tools.isYes(treatyType.getStFreeTSIFlag());
                                    
                                    final boolean hasShares = tredet.getShares().size() > 0;
                                    
                                    final boolean isOR = tdr.isOR();
                                    
                                    final String style = isOR ? "row2" : "row0";
                                    
                                    boolean canChangeMember = freeMembers;
                                    
                                    final boolean hasRISlip = nonProportional;
                                
                                %>
                                <c:tabpage name="<%=tredet.getStInsuranceTreatyDetailID()%>">
                                    <table cellpadding=2 cellspacing=1 class=row0 width="100%" height="100%">
                                        <tr class=row0>
                                            <td>
                                                <table cellpadding=2 cellspacing=1 class=row0>
                                                    <tr>
                                                        <td>
                                                            <table cellpadding=2 cellspacing=1>
                                                                <tr>
                                                                    <td colspan=2 class=header>
                                                                        <%=JSPUtil.print(tredet.getStTreatyClassDesc())%>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td valign=top>
                                                                        <table cellpadding=2 cellspacing=1>
                                                                            <c:field
                                                                                name="<%="claimTreaties.["+i+"].details.["+j+"].dbTreatyLimit"%>"
                                                                                readonly="true" caption="Treaty Limit"
                                                                                type="money16.2" presentation="standard"/>
                                                                            <c:field
                                                                                name="<%="claimTreaties.["+i+"].details.["+j+"].dbTSIPct"%>"
                                                                                readonly="true" caption="TSI Share"
                                                                                type="money16.2" presentation="standard"
                                                                                width="60" suffix=" %"/>
                                                                            <c:field
                                                                                name="<%="claimTreaties.["+i+"].details.["+j+"].dbTSIAmount"%>"
                                                                                caption="TSI Amount" type="money16.2"
                                                                                presentation="standard" readonly="false"/>
                                                                            <c:field
                                                                                name="<%="claimTreaties.["+i+"].details.["+j+"].dbClaimAmount"%>"
                                                                                readonly="false" caption="Claim Amount"
                                                                                type="money16.2" presentation="standard"/>

                                                                            <c:field name="<%="claimTreaties.["+i+"].details.["+j+"].stManualClaimFlag"%>" readonly="false"
                                                                                                         caption="{L-ENGManual Spreading-L}{L-INAManual Spreading-L}" type="check"
                                                                                                         presentation="standard" readonly="false"/>
                                                                        </table>
                                                                    </td>
                                                                    <td valign=top>
                                                                        <table cellpadding=2 cellspacing=1>
                                                                            <c:field
                                                                                name="<%="claimTreaties.["+i+"].details.["+j+"].treatyDetail.dbXOLLower"%>"
                                                                                readonly="true" caption="XOL Lower"
                                                                                type="money16.2" presentation="standard"
                                                                                width="120"/>
                                                                            <c:field
                                                                                name="<%="claimTreaties.["+i+"].details.["+j+"].treatyDetail.dbXOLUpper"%>"
                                                                                readonly="true" caption="XOL Upper"
                                                                                type="money16.2" presentation="standard"
                                                                                width="120"/>
                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                                <c:evaluate when="<%=hasShares || canChangeMember%>">
                                                                    
                                                                    <tr>
                                                                        <td colspan=2>
                                                                            <table cellpadding=2 cellspacing=1 class=header>
                                                                                <tr class=header>
                                                                                    <td></td>
                                                                                    <td>{L-ENGCompany-L}{L-INAPerusahaan-L}</td>
                                                                                    <td>{L-ENGReinsurer-L}{L-INAReasuradur-L}</td>
                                                                                    <td>Share</td>
                                                                                    <td>TSI</td>
                                                                                    <td>Premi Rate</td>
                                                                                    <td>Premium</td>
                                                                                    <td>{L-ENGCommission-L}{L-INAKomisi-L}<br>Rate</td>
                                                                                    <td>{L-ENGCommission-L}{L-INAKomisi-L}</td>
                                                                                    <td>{L-ENGClaim-L}{L-INAKlaim-L}</td>
                                                                                    <c:evaluate when="<%=hasRISlip%>">
                                                                                        <td>R/I Slip</td>
                                                                                    </c:evaluate>
                                                                                    <td>Claim R/I Slip</td>
                                                                                    <td>{L-ENGNotes-L}{L-INACatatan-L}</td>
                                                                                </tr>
                                                                                <%
                                                                                final DTOList treShares = tredet.getShares();
                                                                                
                                                                                for (int k = 0; k < treShares.size(); k++) {
                                                                                InsurancePolicyReinsView ri = (InsurancePolicyReinsView) treShares.get(k);
                                                                                %>
                                                                                <tr class=row0>
                                                                                    <td></td>
                                                                                    <td><c:field
                                                                                            name="<%="claimTreaties.["+i+"].details.["+j+"].shares.["+k+"].stMemberEntityID"%>"
                                                                                            width="150"
                                                                                            lov="LOV_InsuranceCompany"
                                                                                            popuplov="true" type="string"
                                                                                            caption="{L-ENGCompany-L}{L-INAPerusahaan-L}"
                                                                                        readonly="<%=!freeMembers%>"/></td>
                                                                                    <td><c:field
                                                                                            name="<%="claimTreaties.["+i+"].details.["+j+"].shares.["+k+"].stReinsuranceEntityID"%>"
                                                                                            width="150"
                                                                                            lov="LOV_InsuranceCompany"
                                                                                            popuplov="true" type="string"
                                                                                            caption="{L-ENGReinsurer-L}{L-INAReasuradur-L}"
                                                                                        readonly="<%=!freeMembers%>"/></td>
                                                                                    <td><c:field
                                                                                            name="<%="claimTreaties.["+i+"].details.["+j+"].shares.["+k+"].dbSharePct"%>"
                                                                                            width="40" type="money6.2"
                                                                                            caption="Share Pct"
                                                                                            readonly="false"/>%
                                                                                    </td>
                                                                                    <td><c:field
                                                                                            name="<%="claimTreaties.["+i+"].details.["+j+"].shares.["+k+"].dbTSIAmount"%>"
                                                                                            width="80" type="money16.2"
                                                                                        caption="TSI" readonly="false"/></td>


                                                                                    <td><c:field caption="Rate" width="40"
                                                                                                     name="<%="claimTreaties.["+i+"].details.["+j+"].shares.["+k+"].dbPremiRate"%>"
                                                                                                     precision="4" type="money16.5" mandatory="false"
                                                                                                     readonly="true"/>%
                                                                                    </td>
                                                                                    <td><c:field
                                                                                            name="<%="claimTreaties.["+i+"].details.["+j+"].shares.["+k+"].dbPremiAmount"%>"
                                                                                            width="80" type="money16.2" caption="Premi"
                                                                                        readonly="true"/></td>
                                                                                    <td><c:field
                                                                                            name="<%="claimTreaties.["+i+"].details.["+j+"].shares.["+k+"].dbRICommRate"%>"
                                                                                            width="40" type="money16.2" caption="Comm Rate"
                                                                                            readonly="true"/>%
                                                                                    </td>
                                                                                    <td><c:field
                                                                                            name="<%="claimTreaties.["+i+"].details.["+j+"].shares.["+k+"].dbRICommAmount"%>"
                                                                                        width="80" type="money16.2" caption="Comm" readonly="true" /></td>
                                                                                    <td><c:field
                                                                                            name="<%="claimTreaties.["+i+"].details.["+j+"].shares.["+k+"].dbClaimAmount"%>"
                                                                                            width="150" type="money16.2"
                                                                                            caption="{L-ENGClaim Amount-L}{L-INAJumlah Klaim-L}"
                                                                                        readonly="false"/></td>
                                                                                    <c:evaluate when="<%=hasRISlip%>">
                                                                                        <td><c:field
                                                                                                name="<%="claimTreaties.["+i+"].details.["+j+"].shares.["+k+"].stRISlipNo"%>"
                                                                                                width="80" type="string"
                                                                                                caption="R/I Slip"
                                                                                            readonly="true"/></td>
                                                                                    </c:evaluate>
                                                                                        <td><c:field
                                                                                                name="<%="claimTreaties.["+i+"].details.["+j+"].shares.["+k+"].stClaimRISlipNo"%>"
                                                                                                width="80" type="string"
                                                                                                caption="Claim R/I Slip"
                                                                                            readonly="false"/></td>
                                                                                    <td><c:field
                                                                                            name="<%="claimTreaties.["+i+"].details.["+j+"].shares.["+k+"].stNotes"%>"
                                                                                            width="150" type="string" rows="2"
                                                                                            caption="{L-ENGNotes-L}{L-INACatatan-L}"
                                                                                        readonly="true"/></td>
                                                                                </tr>
                                                                                <% } %>
                                                                            </table>
                                                                        </td>
                                                                    </tr>
                                                                </c:evaluate>
                                                            </table>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                </c:tabpage>
                                <% } %>
                            </c:tab>
                        </td>
                    </tr>
                    <%
                    }
                    %>
                </table>
            </c:tabpage>
        </c:evaluate>
        <c:tabpage name="TAB_PREMI">
        <c:fieldcontrol when="<%=cROpolicyPREMI%>" readonly="true">
        <c:field name="detailindex" type="string" hidden="true"/>
        <c:listbox name="details">
        <%
        final DTOList details = form.getDetails();
        final InsurancePolicyItemsView item = ((InsurancePolicyItemsView) details.get(index.intValue()));
        
        final boolean deletable = item != null && item.isDeletable();

        boolean not_usetax = item != null && item.getInsItem().isNotUseTax();
        
        %>
        <c:listcol title="" columnClass="header">
            
        </c:listcol>
        <c:listcol title="" columnClass="detail">
            <c:evaluate when="<%=deletable%>">
                <c:button text="-" event="onDeleteDetail" clientEvent="f.detailindex.value='$index$';"
                          validate="false" defaultRO="true"/>
            </c:evaluate>
        </c:listcol>
        <c:listcol name="InsItem.stDescription" title="{L-ENGITEMS-L}{L-INAITEM-L}"/>
        
        
        <c:listcol title="{L-ENGDESCRIPTION-L}{L-INADESKRIPSI-L}">
            <c:field caption="Description" name="details.[$index$].stDescription" width="180" type="string|255"
                     mandatory="false" readonly="false"/>
        </c:listcol>
        <c:listcol title="RATE">
            <c:field name="details.[$index$].stFlagEntryByRate"
                     clientchangeaction="VM_switchReadOnly('details.[$index$].dbRate',this.checked);VM_switchReadOnly('details.[$index$].dbAmount',!this.checked)"
                     readonly="<%=!deletable%>" type="check" />
            <c:field caption="Rate" name="details.[$index$].dbRate" width="50" type="money16.2" mandatory="false"
                     readonly="<%=!deletable%>"/> %
        </c:listcol>
        <c:listcol title="{L-ENGAMOUNT-L}{L-INAJUMLAH-L}" align="right">
            <c:field caption="Amount" name="details.[$index$].dbAmount" type="money16.2" mandatory="false"
                     readonly="false"/>
            <c:evaluate when="<%=!ro%>">
                <script>docEl('details.[<%=index%>].stFlagEntryByRate').onclick();</script>
            </c:evaluate>
        </c:listcol>
        <c:listcol title="{L-ENGAGENT & NPWP-L}{L-INAAGEN & NPWP-L}" align="center">
            <c:evaluate when="<%=item!=null && item.isComission()%>">
                <c:field name="details.[$index$].stEntityID" hidden="true" type="string"/>
                <c:field caption="Description" name="details.[$index$].stEntityName" width="190" type="string|255"
                         mandatory="false" readonly="true"/>
                <c:evaluate when="<%=deletable%>">
                <c:button text="..." clientEvent="selectAgent($index$)" defaultRO="true"/>
                </c:evaluate>
            </c:evaluate>
        </c:listcol>
        
        
        <c:evaluate when="<%=item!=null && item.isComission()  && !not_usetax%>">
    </tr>
    <tr class=row2>
    <td class=detail></td>
    <c:listcol title="ITEMS">{L-INAPAJAK-L}{L-ENGTAX-L}</c:listcol>
    <c:listcol title="DESCRIPTION">
        <c:field lov="LOV_ARTax" caption="TAX" width="180" name="details.[$index$].stTaxCode" type="string"
                 mandatory="true" readonly="<%=!deletable%>">
        </c:field>
    </c:listcol>
    <c:listcol title="RATE">
        <c:field name="details.[$index$].stTaxAutoRateFlag"
                 clientchangeaction="VM_switchReadOnly('details.[$index$].dbTaxRatePct',this.checked);"
                 type="check" readonly="<%=!deletable%>" />
        <c:field caption="Rate" name="details.[$index$].dbTaxRatePct" width="50" type="money16.3"
                 mandatory="false" readonly="false"/> %
    </c:listcol>
    <c:listcol title="AMOUNT">
        <c:field name="details.[$index$].stTaxAutoAmountFlag"
                 clientchangeaction="VM_switchReadOnly('details.[$index$].dbTaxAmount',this.checked);"
                 type="check" readonly="<%=!deletable%>"/>
        <c:field caption="Amount" name="details.[$index$].dbTaxAmount" type="money16.2" mandatory="false"
                 readonly="false"/>
        <c:evaluate when="<%=!ro%>">
            <script>docEl('details.[<%=index%>].stTaxAutoRateFlag').onclick();</script>
        </c:evaluate>
        <c:evaluate when="<%=!ro%>">
            <script>docEl('details.[<%=index%>].stTaxAutoAmountFlag').onclick();</script>
        </c:evaluate>
    </c:listcol>
    <c:listcol title="AGENT & NPWP">
        
        <c:evaluate when="<%=item!=null && item.isComission()%>">
            NPWP <c:field caption="NPWP" name="details.[$index$].stNPWP" width="180" type="string|255"
                          mandatory="false" readonly="true"/>
        </c:evaluate>
    </c:listcol>
    </c:evaluate>
    </c:listbox>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                item :
                <%--<c:field name="insItemID" type="string" lov="LOV_InsuranceItem">
                           <c:lovLink name="coversrc" link="policy.stCoverTypeCode"/>
                        </c:field>--%>
                <c:field name="insItemID" type="string" lov="insuranceItemLOV" width="200"/>
                <c:button text="+" event="onNewDetail" validate="false" defaultRO="true"/>
            </td>
        </tr>
    </table>
    <table cellpadding=2 cellspacing=1>
        <c:field caption="{L-ENGGross Premium-L}{L-INAPremi Bruto-L}" name="policy.dbPremiTotal"
                 alias="policy.dbPremiTotal2" width="200" type="money16.2" mandatory="false" readonly="true"
                 precision="2" presentation="standard"/>
        
        <%--Discount--%>

        <%
        for (int i = 0; i < policy.getDetails().size(); i++) {
        InsurancePolicyItemsView it = (InsurancePolicyItemsView) policy.getDetails().get(i);
        
        if (it.isDiscount()) {
        %>
        <tr>
            <td><%=it.getInsItem().getStDescription()%>
            </td>
            <td>:</td>
            <td align=right><%=jspUtil.print(it.getDbAmount(), 2)%>
            </td>
            <td><%=jspUtil.print(it.getStGLAccount()) + " - " + jspUtil.print(it.getStAccountDesc())%>
            </td>
        </tr>
        <%
        }
        }
        %>
        
        <c:field caption="{L-ENGPremium-L}{L-INAPremi-L}" name="policy.dbPremiTotalAfterDisc" width="200"
                 type="money16.2" mandatory="false" precision="2" readonly="true" presentation="standard"/>
        
        <%--Fee--%>

        <%
        for (int i = 0; i < policy.getDetails().size(); i++) {
        InsurancePolicyItemsView it = (InsurancePolicyItemsView) policy.getDetails().get(i);
        
        if (it.isFee()) {
        %>
        <tr>
            <td><%=it.getInsItem().getStDescription()%>
            </td>
            <td>:</td>
            <td align=right><%=jspUtil.print(it.getDbAmount(), 2)%>
            </td>
            <td><%=jspUtil.print(it.getStGLAccount()) + " - " + jspUtil.print(it.getStAccountDesc())%>
            </td>
        </tr>
        <%
        }
        }
        %>
        
        <c:field caption="{L-ENGDue To Us-L}{L-INATagihan-L}" name="policy.dbTotalDue" width="200" type="money16.2"
                 mandatory="false" readonly="true" precision="2" presentation="standard"/>
        
        <%--Comission--%>

        <%
        for (int i = 0; i < policy.getDetails().size(); i++) {
        InsurancePolicyItemsView it = (InsurancePolicyItemsView) policy.getDetails().get(i);
        
        if (it.isComission()) {
        %>
        <tr>
            <td><%=it.getInsItem().getStDescription()%>
            </td>
            <td>:</td>
            <td align=right><%=jspUtil.print(it.getDbNetAmount(), 2)%>
            </td>
            <td><%=jspUtil.print(it.getStGLAccount()) + " - " + jspUtil.print(it.getStAccountDesc())%>
            </td>
        </tr>
        <tr>
            <td>{L-ENGTax-L}{L-INAPajak-L}</td>
            <td>:</td>
            <td align=right><%=jspUtil.print(it.getDbTaxAmount(), 2)%>
            </td>
            <td><%=jspUtil.print(it.getStTaxGLAccount()) + " - " + jspUtil.print(it.getStTaxGLAccountDesc())%>
            </td>
        </tr>
        <%
        }
        }
        %>
        
        <%--netto--%>

        <c:field caption="{L-ENGPremi Nett-L}{L-INATagihan Netto-L}" name="policy.dbPremiNetto"
                 alias="policy.dbPremiNetto2" width="200" type="money16.2" mandatory="false" precision="2"
                 readonly="true" presentation="standard"/>
    </table>
    </c:fieldcontrol>
    </c:tabpage>
    <c:tabpage name="TAB_INST">
        
        <table cellpadding=2 cellspacing=1>
            <c:fieldcontrol when="<%=cROpolicyINSTALLMENT%>" readonly="true">
            <c:field caption="{L-ENGInstallment Periods-L}{L-INAJumlah Cicilan-L}" name="policy.stInstallmentPeriods"
                     width="50" type="integer" readonly="true" mandatory="true" presentation="standard"/>
            <c:field caption="{L-ENGInstallment Step-L}{L-INAJarak Cicilan-L}" lov="LOV_InsurancePeriod"
                     name="policy.stInstallmentPeriodID" width="200" type="string" mandatory="true"
                     presentation="standard"/>
            <c:field caption="{L-ENGManual Installment-L}{L-INAManual Installment-L}" name="policy.stManualInstallmentFlag"
                     type="check" mandatory="false" presentation="standard"/>
            </c:fieldcontrol>         
            
           <tr>
                <td>{L-ENGPayment Date-L}{L-INATanggal Pembayaran-L}</td>
                <td>:</td>
                <td>
                    <c:field caption="{L-ENGPayment Date-L}{L-INATanggal Pembayaran-L}" name="policy.dtPaymentDate"
                                        readonly="<%=!isInputPaymentDateMode%>" type="date" />
                    <c:button show="true" text="Check" event="validateHistoryPolicy"/>
                </td>
            </tr>
            <c:field width="500" rows="3" caption="{L-ENGNotes-L}{L-INACatatan-L}" name="policy.stPaymentNotes" type="string"
                     readonly="<%=!isInputPaymentDateMode%>" presentation="standard"/>
        </table>
        <br>
        <br>
        
        <c:fieldcontrol when="<%=cROpolicyINSTALLMENT%>" readonly="true">
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
                    </c:listbox>
                </td>
            </tr>
        </table>
        </c:fieldcontrol> 
        
        <br>
        <br>
        
        <table cellpadding=2 cellspacing=1 class=header>
            <tr>
                <td align=center> {L-ENGActual AR Status-L}{L-INAStatus Pembayaran-L}</td>
            </tr>
            <tr>
                <td>
                    <c:listbox name="policy.arinvoices">
                        <c:listcol title=""><%=index.intValue() + 1%>
                        </c:listcol>
                        <c:listcol title="{L-ENGInvoice No-L}{L-INANomor Tagihan-L}" name="stInvoiceNo"/>
                        <c:listcol title="{L-ENGInvoice Date-L}{L-INATanggal Tagihan-L}" name="dtInvoiceDate"/>
                        <c:listcol title="{L-ENGDue Date-L}{L-INATanggal Tagihan-L}" name="dtDueDate"/>
                        <c:listcol title="{L-ENGAmount-L}{L-INAJumlah-L}" name="dbEnteredAmount"/>
                        <c:listcol title="{L-ENGSettled-L}{L-INADibayar-L}" name="dbAmountSettled"/>
                        <c:listcol title="{L-ENGPayment Date-L}{L-INATanggal Pembayaran-L}" name="dtPaymentDate"/>
                    </c:listbox>
                </td>
            </tr>
        </table>

        <br>
        <br>
        <table cellpadding=2 cellspacing=1 class=header>
            <tr>
                <td align=center> {L-ENGActual AR Status-L}{L-INAStatus Pembayaran-L} Sistem Care FA</td>
            </tr>
            <tr>
                <td>
                    <c:listbox name="policy.statusBayarKlaimCareList">
                        <c:listcol title=""><%=index.intValue() + 1%>
                        </c:listcol>
                        <c:listcol title="No Voucher" name="voucherNo"/>
                        <c:listcol title="Type" name="type"/>
                        <c:listcol title="Tagihan Net" name="net"/>
                        <c:listcol title="Payment" name="payment"/>
                        <c:listcol title="Outstanding" name="outstanding"/>
                        <c:listcol title="Tanggal Bayar" name="tglBayar"/>
                        <c:listcol title="Bukti Bayar" name="paymentReference"/>

                    </c:listbox>
                </td>
            </tr>
        </table>
        
    </c:tabpage>
    
    <c:evaluate when="<%=canSeeDoc%>">
        <c:tabpage name="TAB_POLICY_DOCUMENTS">
            <c:fieldcontrol when="<%=form.isApprovalByDirectorMode()%>" readonly="false">
                <table cellpadding=2 cellspacing=1 class=header width="100%">
                    <tr>
                        <td>
                            DOCUMENTS
                        </td>
                    </tr>
                    <tr>
                        <td class=row0>
                            <c:listbox name="policy.policyDocuments">
                                <c:listcol title="">
                                    <c:field name="policy.policyDocuments.[$index$].stSelectedFlag" type="check"
                                             mandatory="false" readonly="false"/>
                                </c:listcol>
                                <c:listcol name="stDescription" title="DESCRIPTION">
                                </c:listcol>
                                <c:listcol name="stDescription" title="DESCRIPTION">
                                </c:listcol>
                                <c:listcol title="Document">
                                    <c:field name="policy.policyDocuments.[$index$].stFilePhysic" type="file"
                                             thumbnail="true" caption="File"/>
                                </c:listcol>
                            </c:listbox>
                        </td>
                    </tr>
                </table>
            </c:fieldcontrol>
        </c:tabpage>
    </c:evaluate>

    <c:evaluate when="<%=hasClaimCo%>">
        <c:tabpage name="TAB_CLAIMCO">
            
            <c:field name="coinsIndex" type="string" hidden="true"/>
            <c:listbox name="coins">
                <%
                final InsurancePolicyCoinsView ccoins = (InsurancePolicyCoinsView) current;
                final boolean holdingComp = ccoins != null && ccoins.isHoldingCompany();
                
                %>
                <c:listcol title="" columnClass="header">
                    
                </c:listcol>
                <c:listcol title="" columnClass="detail">
                    <c:button text="-" event="onDeleteCoinsurance" enabled="<%=canChangeCoinsMember && !holdingComp%>"
                              clientEvent="f.coinsIndex.value='$index$';" validate="false" defaultRO="true"/>
                </c:listcol>
                <c:listcol title="{L-ENGCompany-L}{L-INAPerusahaan-L}">
                    <%--<c:field name="coins.[$index$].stEntityID" hidden="true" type="string" />--%>
                    <c:field caption="{L-ENGDescription-L}{L-INADeskripsi-L}" lov="LOV_InsuranceCompany" popuplov="true"
                             name="coins.[$index$].stEntityID" type="string|64" mandatory="true" readonly="true"
                             width="200"/>
                    <%--<c:button text="..." clientEvent="selectCoIns($index$)" />--%>
                </c:listcol>
                <c:listcol title="{L-ENGPosition-L}{L-INAPosisi-L}">
                    <c:field lov="VS_COINS_POS" caption="Position" name="coins.[$index$].stPositionCode" type="string"
                             mandatory="true" readonly="true"/>
                </c:listcol>
                <c:listcol title="{L-ENGShare-L}{L-INAShare-L}">
                    <c:field name="coins.[$index$].stFlagEntryByRate"
                             clientchangeaction="VM_switchReadOnly('coins.[$index$].dbSharePct',this.checked);VM_switchReadOnly('coins.[$index$].dbAmount',!this.checked)"
                             type="check" readonly="false"/>
                    <c:field caption="SharePercentage" name="coins.[$index$].dbSharePct" type="money16.2" mandatory="false"
                             readonly="false"/>
                </c:listcol>
                <c:listcol title="{L-ENGTSI-L}{L-INATSI-L}">
                    <c:field caption="Amount" name="coins.[$index$].dbAmount" width="120" type="money16.2" mandatory="false"
                             readonly="true"/>
                </c:listcol>
                <c:listcol title="{L-ENGManual-L}{L-INAManual-L}">
                    <c:field name="coins.[$index$].stManualClaimFlag"
                             clientchangeaction="VM_switchReadOnly('coins.[$index$].dbClaimAmount',this.checked);" type="check" readonly="false"/>
                </c:listcol>
                <c:listcol title="{L-ENGClaim Amt-L}{L-INAJumlah Klaim-L}">
                    <%--  <c:evaluate when="<%=!holdingComp%>" >--%>
                    <c:field name="coins.[$index$].stAutoClaimAmount"
                             clientchangeaction="VM_switchReadOnly('coins.[$index$].dbClaimAmount',this.checked);"
                             type="check" readonly="false"/>
                    <c:field caption="Amount" name="coins.[$index$].dbClaimAmount" type="money16.2" mandatory="true"
                             readonly="false"/>
                    <script>docEl('coins.[<%=index%>].stAutoClaimAmount').onclick();</script>
                    <%--  </c:evaluate>--%>
                </c:listcol>
            </c:listbox>
            
            <table cellpadding=2 cellspacing=1>
                <c:field caption="{L-ENGTotal Insured Amount-L}{L-INAJumlah Harga Pertanggungan-L}"
                         name="policy.dbCoinsCheckInsAmount" type="money16.2" mandatory="false" readonly="true"
                         presentation="standard"/>
                <c:field caption="{L-ENGTotal Premi Amount-L}{L-INAJumlah Premi-L}" name="policy.dbCoinsCheckPremiAmount"
                         type="money16.2" mandatory="false" readonly="true" presentation="standard"/>
            </table>
        </c:tabpage>
    </c:evaluate>
    
       <c:evaluate when="<%=hasClaimCo%>">
        <c:tabpage name="TAB_CLAIMCOINS_COVER">
            
            <c:field name="coinsCoverIndex" type="string" hidden="true"/>

            <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>{L-ENGManual Spreading-L}{L-INAManual Spreading-L} :
                        <c:field caption="{L-ENGManual Spreading-L}{L-INAManual Spreading-L}" name="policy.stManualCoinsFlag" type="check" mandatory="false"
                                readonly="false" />
                        </td>

                    </tr>
            </table>

            <c:listbox name="coinsCoverage">
                <%
                final InsurancePolicyCoinsView ccoins = (InsurancePolicyCoinsView) current;
                final boolean holdingComp = ccoins != null && ccoins.isHoldingCompany();
                
                %>
                <c:listcol title="" columnClass="header">
                    <c:button text="+" event="onNewClaimCoinsuranceCover" validate="false" />
                </c:listcol>
                <c:listcol title="" columnClass="detail">
                    <c:button text="-" event="onDeleteClaimCoinsuranceCover" enabled="<%=!holdingComp%>"
                              clientEvent="f.coinsCoverIndex.value='$index$';" validate="false" defaultRO="true"/>
                </c:listcol>
                <c:listcol title="{L-ENGCompany-L}{L-INAPerusahaan-L}">
                    <%--<c:field name="coins.[$index$].stEntityID" hidden="true" type="string" />--%>
                    <c:field caption="{L-ENGDescription-L}{L-INADeskripsi-L}" lov="LOV_InsuranceCompany" popuplov="true"
                             name="coinsCoverage.[$index$].stEntityID" type="string|64" mandatory="true" 
                             width="200"/>
                    <%--<c:button text="..." clientEvent="selectCoIns($index$)" />--%>
                </c:listcol>
                <c:listcol title="{L-ENGPosition-L}{L-INAPosisi-L}">
                    <c:field lov="VS_COINS_POS" caption="Position" name="coinsCoverage.[$index$].stPositionCode" type="string"
                             mandatory="true" readonly="true"/>
                </c:listcol>
                <c:listcol title="{L-ENGShare-L}{L-INAShare-L}">
                    <c:field name="coinsCoverage.[$index$].stFlagEntryByRate"
                             clientchangeaction="VM_switchReadOnly('coinsCoverage.[$index$].dbSharePct',this.checked);VM_switchReadOnly('coinsCoverage.[$index$].dbAmount',!this.checked)"
                             type="check" readonly="<%=!canChangeCoinsMember || holdingComp%>"/>
                    <c:field caption="SharePercentage" name="coinsCoverage.[$index$].dbSharePct" type="money16.2" mandatory="false"
                             readonly="true"/>
                </c:listcol>
                <c:listcol title="{L-ENGTSI-L}{L-INATSI-L}">
                    <c:field caption="Amount" name="coinsCoverage.[$index$].dbAmount" width="120" type="money16.2" mandatory="false"
                             readonly="true"/>
                </c:listcol>
                 <c:listcol title="{L-ENGManual-L}{L-INAManual-L}">
                    <c:field name="coinsCoverage.[$index$].stManualClaimFlag"
                             clientchangeaction="VM_switchReadOnly('coinsCoverage.[$index$].dbClaimAmount',this.checked);" type="check" readonly="false"/>
                </c:listcol>
                <c:listcol title="{L-ENGClaim Amt-L}{L-INAJumlah Klaim-L}">
                    <%--  <c:evaluate when="<%=!holdingComp%>" >--%>
                    <c:field name="coinsCoverage.[$index$].stAutoClaimAmount"
                             clientchangeaction="VM_switchReadOnly('coinsCoverage.[$index$].dbClaimAmount',this.checked);"
                             type="check" readonly="false"/>
                    <c:field caption="Amount" name="coinsCoverage.[$index$].dbClaimAmount" type="money16.2" mandatory="true"
                             readonly="false"/>
                    <script>docEl('coinsCoverage.[<%=index%>].stAutoClaimAmount').onclick();</script>
                    <%--  </c:evaluate>--%>
                </c:listcol>
            </c:listbox>
        </c:tabpage>
    </c:evaluate>
    <c:tabpage name="TAB_OPTIONS">
                      <table cellpadding=2 cellspacing=1 class=header>
                         <tr>
                            <td>
                               Pilihan
                            </td>
                         </tr>
                         <tr>
                            <td class=row0>
                               <table cellpadding=2 cellspacing=1>
                                   <c:field caption="Klaim Restrukturisasi" name="policy.stClaimRestructFlag" type="check" mandatory="false"
                                                                     readonly="false" presentation="standard"/>
                                   <c:evaluate when="<%=headOfficeUser%>">
                                       <%--<c:field name="policy.stRiskApproved" type="check" readonly="true" caption="Persetujuan Pusat" presentation="standard" />
                                       <c:field name="policy.stRiskApprovedWho" type="string" readonly="true" width="200" lov="LOV_User" popuplov="true" caption="User Setujui" presentation="standard" />--%>
                                       <c:field name="policy.stCheckingFlag" type="check" caption="Bypass Validasi" presentation="standard" />
                                   </c:evaluate>

                               </table>
                            </td>
                         </tr>

                      </table>       
        </c:tabpage>
</c:tab>
</td>
</tr>
<tr>
    <td align=center>
        <br>
        <br>
        <c:button show="<%=!viewMode&&!form.isReApprovedMode()&&!form.isInputPaymentDateMode()%>"
                  text="{L-ENGRecalculate-L}{L-INAHitung Ulang-L}" event="btnRecalculateClaim" validate="true"/>
        <c:button show="<%=!viewMode&&!form.isReApprovedMode()&&!form.isSavePolicyHistoryMode()&&!form.isReverseMode()%>"
                  text="{L-ENGSave-L}{L-INASimpan-L}" event="btnSave" validate="true"
                  confirm="Yakin mau disimpan ? Sudah dihitung ulang?"/>
        <c:button show="<%=!viewMode%>" text="{L-ENGCancel-L}{L-INABatal-L}" event="btnCancel" validate="false"
                  confirm="Yakin Mau Dibatalkan ?"/>
        
        <c:evaluate
            when="<%=!effective && form.isApprovalMode()&&!form.isReverseMode()&&!form.isReApprovedMode()&&!form.isApprovedViaReverseMode()&&!form.isRejectMode()%>">
                <c:evaluate when="<%=form.getPolicy().isStatusPolicy() || form.getPolicy().isStatusRenewal() || form.getPolicy().isStatusClaimDLA() || policy.isStatusEndorse() || showPersetujuanPrinsipNo%>"><table class=row0>
                <tr class="header"><td colspan="3" align="center"><b>Password Validation</b></td></tr>
                    <tr><td><b>Password</b></td><td>:</td><td><c:field  width="200" caption="Password" name="policy.stPassword" type="password" mandatory="true" overrideRO="true" readonly="false"/></td></tr>
                </table><br><br>
                
                </c:evaluate>
                <c:button show="true" text="{L-ENGApprove-L}{L-INASetujui-L}" event="btnApprove" validate="true"
                      confirm="Yakin Mau Disetujui ?"/>
                <%--<c:button show="<%=policy.getStParentID()!=null%>" text="Reject" enabled="<%=rejectable%>" event="btnReject"
                      validate="true" confirm="Are you sure want to reject ?"/> --%>
        </c:evaluate>

        <c:evaluate when="<%=form.isRejectMode() && rejectable%>">
                <c:button show="true" text="Tolak" enabled="<%=form.isRejectMode() && rejectable%>" event="btnReject"
                      validate="true" confirm="Yakin ingin di tolak ?"/>
        </c:evaluate>
        
        <c:button show="<%=viewMode%>" text="{L-ENGClose-L}{L-INATutup-L}" event="btnCancel" validate="false"/>
        
        <c:button show="<%=form.isReverseMode()%>" text="Reverse" event="btnReverseJurnalBalikOnly"/>
        <c:button show="<%=form.isReApprovedMode()%>" text="ReApprove" event="btnReApprove" confirm="Yakin?Reas Juga di Reset nih" />
        <c:button show="<%=form.isApprovedViaReverseMode()%>"
                  text="{L-ENGApprove(Reverse)-L}{L-INASetujui(Reverse)-L}" event="btnApproveViaReverse"/>
        <c:button show="<%=cROReas&&form.isApprovedReasMode()%>" text="{L-ENGApprove-L}{L-INASetujui-L}" event="btnApproveReins"
                  validate="true" confirm="Yakin Mau Setujui ?"/>
    </td>
</tr>
<tr>
</tr>
</c:evaluate>
</table>
</c:frame>
<script>
    function selectAgent(i) {
        openDialog('so.ctl?EVENT=INS_AGENT_SELECT', 400, 400,
                function (o) {
                    if (o != null) {
                        document.getElementById('claimItems.[' + i + '].stEntityID').value = o.entid;
                        document.getElementById('claimItems.[' + i + '].stEntityName').value = o.entname;
                        document.getElementById('claimItems.[' + i + '].stNPWP').value = o.npwp;
                    }
                }
                );
    }

function setTaxCodeOnClaim() {
        f.action_event.value = 'setTaxCodeOnClaim';
        f.submit();
    }

    function selectCoIns(i) {
        openDialog('so.ctl?EVENT=INS_AGENT_SELECT', 400, 400,
                function (o) {
                    if (o != null) {
                        document.getElementById('coins.[' + i + '].stEntityID').value = o.entid;
                        document.getElementById('coins.[' + i + '].stEntityName').value = o.entname;
                        //document.getElementById('coins.['+i+'].stEntityName').innerText=o.entname;
                    }
                }
                );
    }

    function selectCustomer2() {
        var o = window.lovPopResult;
        document.getElementById('policy.stCustomerName').value = o.value;
        document.getElementById('policy.stCustomerAddress').value = o.address;
    }

    function selectProducer2() {
        var o = window.lovPopResult;
        document.getElementById('policy.stProducerName').value = o.value;
        document.getElementById('policy.stProducerAddress').value = o.address;

    }

    function selectCustomer(i) {
        openDialog('entity_search.crux', 400, 400,
                function (o) {
                    if (o != null) {
                        document.getElementById('policy.stEntityID').value = o.stEntityID;
                        document.getElementById('policy.stEntityName').value = o.stEntityName;
                        document.getElementById('policy.stCustomerName').value = o.stEntityName;
                        document.getElementById('policy.stCustomerAddress').value = o.stAddress;
                    }
                }
                );
    }

    function selectProducer(i) {
        openDialog('entity_search.crux', 400, 400,
                function (o) {
                    if (o != null) {
                        document.getElementById('policy.stProducerID').value = o.stEntityID;
                        document.getElementById('policy.stProducerEntName').value = o.stEntityName;
                        document.getElementById('policy.stProducerName').value = o.stEntityName;
                        document.getElementById('policy.stProducerAddress').value = o.stAddress;
                    }
                }
                );
    }

    function selectPrincipal2() {
        var o = window.lovPopResult;
        document.getElementById('selectedObject.stReference3').value = o.address;
        document.getElementById('selectedObject.stReference4').value = o.functionary_name;
        document.getElementById('selectedObject.stReference5').value = o.functionary_position;
        document.getElementById('selectedObject.stReference2').value = o.code;
    }

    function selectPrincipal() {
        var o = window.lovPopResult;
        document.getElementById('selectedObject.stReference3').value = o.address;
        document.getElementById('selectedObject.stReference4').value = o.functionary_name;
        document.getElementById('selectedObject.stReference5').value = o.functionary_position;
        document.getElementById('selectedObject.stReference1').value = o.value;
    }

    function selectSurety() {
        var o = window.lovPopResult;
        document.getElementById('selectedObject.stReference7').value = o.address;
        document.getElementById('selectedObject.stReference8').value = o.functionary_name;
        document.getElementById('selectedObject.stReference9').value = o.functionary_position;
        document.getElementById('selectedObject.stReference12').value = o.value;
    }

    function selectBank() {
        var o = window.lovPopResult;
        document.getElementById('selectedObject.stReference10').value = o.rc_no;
    }

    function selectObligee() {
        var o = window.lovPopResult;
        document.getElementById('selectedObject.stReference5').value = o.code;
        //document.getElementById('selectedObject.stReference2').value=o.address;
    }

    function selectZone(i) {
        openDialog('so.ctl?EVENT=ZONE_SELECT', 400, 400,
                function (o) {
                    if (o != null) {
                        document.getElementById('selectedObject.coverage.[' + i + '].stZoneID').value = o.zoneid;
                        document.getElementById('selectedObject.coverage.[' + i + '].stDescription').value = o.desc;
                    }
                }
                );
    }

    function selectCreditType() {
        var o = window.lovPopResult;
        document.getElementById('policy.stKreasiTypeDesc').value = o.desc;
    }

    function selectReinsKreasi() {
        var o = window.lovPopResult;
        document.getElementById('policy.stCoinsName').value = o.value;
    }

    function refresh() {
        f.action_event.value = 'refresh';
        f.submit();
    }

    <%--document.body.onscroll=function() {alert(document.body.scrollTop);};--%>
    <%--document.body.onload=function() {document.body.scrollTop=200};--%>
</script>
