<%@page import="java.math.BigDecimal"%>
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

final boolean canCreatePolicyHistory = form.isCanCreatePolicyHistory();

final boolean enableNoPolicy = form.isEnableNoPolicy();

final boolean ismasterCurrency = CurrencyManager.getInstance().isMasterCurrency(policy.getStCurrencyCode());
final boolean hasPolicyType = policy.getStPolicyTypeID() != null;
final boolean isAddPeriodDesc = policy.isAddPeriodDesc();
final boolean isLampiran = policy.isLampiran();

final boolean phase2 = policy.getStRegionID()!=null;
final boolean phase1 = !phase2;

final String type_code = policy.getPolicyType() != null?policy.getPolicyType().getStPolicyTypeCode():"";
    
boolean hasCoIns = policy.hasCoIns();

final boolean headOfficeUser = SessionManager.getInstance().getSession().isHeadOfficeUser();

if (policy.getStCoverTypeCode() != null){
    if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN"))
        hasCoIns = false;
}

boolean canSeeCoins = policy.canSeeCoinsurance();
   
form.getTabs().enable("TAB_COINS", hasCoIns);
form.getTabs().enable("TAB_COINS_COVER", canSeeCoins);

final boolean ro = form.isReadOnly();
boolean viewMode = ro;

boolean statusDraft = FinCodec.PolicyStatus.DRAFT.equalsIgnoreCase(form.getStStatus());
boolean statusPolicy = FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(form.getStStatus());
boolean statusEndorse = FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(form.getStStatus());
boolean statusSPPA = FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(form.getStStatus());
boolean statusRenewal = FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(form.getStStatus());
boolean statusHistory = FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(form.getStStatus());

final boolean isInputPaymentDateMode = form.isInputPaymentDateMode();
boolean showPersetujuanPrinsipNo = policy.getStReference1()!=null && (statusSPPA || statusPolicy) && type_code.equalsIgnoreCase("OM_BG")? true:false;
final boolean showPolicyNo = policy.getStPolicyNo()!=null || (!statusDraft && !statusSPPA && canCreatePolicyHistory)? true:false;
final boolean effective = policy.isEffective();
final boolean posted = policy.isPosted();
final boolean canChangeCoverType = (statusDraft || statusSPPA || statusPolicy ||statusHistory || statusEndorse) && !posted && (policy.getStCoverTypeCode() == null);

boolean canChangeCoinsMember = (statusDraft || statusSPPA || statusPolicy ||statusRenewal || statusHistory || statusEndorse) && !posted;
boolean showCoinsurance = (statusHistory || statusEndorse );

final boolean rejectable = form.isRejectable();
final boolean isMember = policy.isMember();

boolean cROpolicyHeader = false;
boolean cROpolicyRiskDetail = false;
boolean cROpolicyClausules = false;
boolean cROpolicyPREMI = false;
boolean cROpolicyINSTALLMENT = false;
boolean cROendorsePeriodFactor = false;
boolean cROendorseObject = false;
boolean cROpolicySumInsured = false;
boolean cROpolicyCoverage = false;

final boolean cROReas = form.isReasMode();

final boolean isTotalEndorseMode = form.isTotalEndorseMode();
final boolean isPartialEndorseTSIMode = form.isPartialEndorseTSIMode();
final boolean isPartialEndorseRateMode = form.isPartialEndorseRateMode();
final boolean isDescriptionEndorseMode = form.isDescriptionEndorseMode();
final boolean isRestitutionEndorseMode = form.isRestitutionEndorseMode();
boolean isEndorseMode = (isTotalEndorseMode || isPartialEndorseTSIMode || isPartialEndorseRateMode || isDescriptionEndorseMode || isRestitutionEndorseMode);
boolean isRejectMode = form.isRejectMode();
/*
if(isEndorseMode){
    cROpolicyHeader = true;
    cROpolicyRiskDetail = true;
    cROpolicyClausules = true;
    cROpolicyPREMI = true;
    cROpolicyINSTALLMENT = true;
    viewMode = false;
}
    
if(isInputPaymentDateMode){
    cROpolicySumInsured = true;
    cROpolicyCoverage = true;
}

if(isTotalEndorseMode){ 
    cROendorsePeriodFactor = true;
}else if(isPartialEndorseTSIMode){
    cROpolicySumInsured = true;
    cROendorsePeriodFactor = true;
    if(policy.getStPolicyTypeID().equalsIgnoreCase("21")) cROendorseObject = true;
}else if(isPartialEndorseRateMode){
    cROpolicyCoverage = true;
    cROendorsePeriodFactor = true;
    cROpolicyPREMI = true;
    if(policy.getStPolicyTypeID().equalsIgnoreCase("21")) cROendorseObject = true;
}else if(isDescriptionEndorseMode){
    cROendorseObject = true;
}else if(isRestitutionEndorseMode){
    cROpolicyRiskDetail = false;
    cROpolicyCoverage = true;
    cROendorsePeriodFactor = true;
    cROendorseObject = true;
    cROpolicyPREMI = false;
}*/

boolean isPolisKhusus = policy.isPolisKhusus();

final boolean showRateFactor = form.showRateFactor();

request.setAttribute("policyForm",form);

boolean perorangan = false;

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
                        
                        <c:field width="200" changeaction="changeBranch" lov="LOV_CostCenter"
                                 caption="{L-ENGBranch-L}{L-INACabang-L}" name="policy.stCostCenterCode" type="string"
                                 mandatory="true"
                                 readonly="true"
                                 presentation="standard"/>
                        
                <c:field show="<%=policy.getStCostCenterCode()!=null%>" width="200" lov="LOV_Region"
                         changeaction="onChangeRegion" caption="{L-ENGRegion-L}{L-INADaerah-L}"
                         name="policy.stRegionID" type="string" mandatory="false"
                         readonly="true" presentation="standard">
                    <c:lovLink name="cc_code" link="policy.stCostCenterCode" clientLink="false"/>
                </c:field>
                
                       <c:field width="300" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroup"
                         readonly="<%=hasPolicyType%>" caption="{L-ENGPolicy Class-L}{L-INAKelas Polis-L}"
                         name="policy.stPolicyTypeGroupID" type="string" mandatory="true" presentation="standard"/>
                <c:field width="300" include="<%=policy.getStPolicyTypeGroupID()!=null%>"
                         changeaction="onChangePolicyType" lov="LOV_PolicyTypeInput" readonly="<%=hasPolicyType%>"
                         caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" name="policy.stPolicyTypeID" type="string"
                         mandatory="true" presentation="standard">
                    <c:lovLink name="polgroup" link="policy.stPolicyTypeGroupID" clientLink="false"/>
                    <c:param name="cc_code" value="<%=policy.getStCostCenterCode()%>"  />
                </c:field>
                <c:field show="<%=policy.getStPolicyTypeID()!=null%>" changeaction="chgCoverType" width="200"
                         lov="LOV_InsuranceCoverType" caption="{L-ENGCover Type-L}{L-INATipe Penutupan-L}"
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

                            <c:field width="200" show="true" show="<%=policy.isStatusEndorse()%>" caption="Jenis Endorse" name="policy.stEndorseMode" type="string|32"
                                     mandatory="false" readonly="true" presentation="standard"/>

                            <c:field width="200" show="<%=policy.getStClaimStatus()!=null%>"
                                     caption="{L-ENGClaim Status-L}{L-INAStatus Klaim-L}" name="policy.stClaimStatusDesc" type="string"
                                     mandatory="false" readonly="true" presentation="standard"/>
                           <c:field width="200" show="<%=showPolicyNo%>" caption="{L-ENGPolicy No-L}{L-INANomor Polis-L}"
                                     name="policy.stPolicyNo" type="string|32" mandatory="true" readonly="<%=!enableNoPolicy%>" presentation="standard"/>
                            <c:field width="200" show="<%=showPersetujuanPrinsipNo%>" caption="{L-ENGNomor Persetujuan Prinsip-L}{L-INANomor Persetujuan Prinsip-L}"
                                     name="policy.stReference1" type="string|32" mandatory="false" readonly="<%=!enableNoPolicy%>" presentation="standard"/>
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
                                     lov="LOV_InsuranceCoverType" caption="{L-ENGCover Type-L}{L-INATipe Penutupan-L}"
                                     name="policy.stCoverTypeCode" type="string" mandatory="true" readonly="<%=!canChangeCoverType%>"
                                     presentation="standard"/>
                            <c:field caption="{L-ENGPolicy/Endorsement Date-L}{L-INATanggal Polis/Endorsement-L}" name="policy.dtPolicyDate" type="date"
                                     mandatory="true" readonly="false" presentation="standard"/>
                            <c:field show="<%=statusEndorse%>" caption="{L-ENGChange Date-L}{L-INATanggal Perubahan-L}"
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
                                     width="200" name="policy.stCurrencyCode" type="string" mandatory="true" readonly="false" presentation="standard"/>
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
                                    <c:button text="Get Periode" event="applyDateHeader" show="true" defaultRO="true"/>
                                </td>
                            </tr>
                            
                            <tr>
                                <td>{L-ENGPeriod End-L}{L-INAPeriode Akhir-L}</td>
                                <td>:</td>
                                <td>
                                    <c:field caption="{L-ENGPeriod End-L}{L-INAPeriode Akhir-L}" name="policy.dtPeriodEnd" type="date"
                                             mandatory="true" readonly="false"/>
                                    <c:button text="+" event="addPeriodDesc" show="true" defaultRO="true"/>
                                    <c:button text="-" event="delPeriodDesc" show="true" defaultRO="true"/>
                                    <c:button text="Apply To All" event="applyPeriodToAll" show="true" defaultRO="true"/>
                                </td>
                            </tr>
                            <tr>
                                <td></td>
                                <td></td>
                                <td>         
                                </td>
                            </tr>
                            <tr>
                                <td>{L-ENGPeriod Add-L}{L-INATambah Periode-L}</td>
                                <td>:</td>
                                <td>
                                    <c:field caption="{L-ENGDays Length-L}{L-INAJumlah Hari-L}" name="policy.stDaysLength" type="string"
                                             width="70" readonly="false" />
                                    <c:field caption="{L-ENGUnits-L}{L-INASatuan-L}" name="policy.stUnits" lov="LOV_PeriodUnit" type="string"
                                             width="90" readonly="false" changeaction="calcDays" />           
                                </td>
                            </tr>
                            <c:evaluate when="<%=isAddPeriodDesc%>">
                                <c:field width="250" rows="3" caption="{L-ENGPeriod Desc-L}{L-INADeskripsi Periode-L}"
                                         name="policy.stPeriodDesc" type="string" mandatory="<%=isAddPeriodDesc%>" readonly="false"
                                         presentation="standard"/>
                            </c:evaluate>
                             <c:field caption="{L-ENGSame Period-L}{L-INAPeriode Pertanggungan Sama-L}" name="policy.stAllowSamePeriodFlag" type="check" mandatory="false"
                                     readonly="false" presentation="standard"/>
                            <tr>
                                <td colspan=3 class=header>
                                    <table cellpadding=2 cellspacing=1 class=row0 width="100%">
                                        <c:evaluate when="<%=statusEndorse%>">
                                            <c:fieldcontrol when="<%=cROendorsePeriodFactor%>" readonly="false">
                                            <tr>
                                                <td>Period Factor (Endorse)</td>
                                                <td>:</td>
                                                <td>
                                                    <c:field show="<%=policy.getStPeriodBaseBeforeID()!=null%>" caption="Period Rate Before"
                                                             name="policy.dbPeriodRateBefore" type="money16.2" mandatory="true" width="60"/>
                                                    <c:field caption="Period Base Before" name="policy.stPeriodBaseBeforeID" type="string"
                                                             mandatory="true" readonly="false" lov="LOV_PeriodBase"
                                                             changeaction="changePeriodBase" width="140"/>
                                                </td>
                                            </tr>
                                            </c:fieldcontrol>
                                        </c:evaluate>
                                        <tr>
                                            <td>Period Factor</td>
                                            <td>:</td>
                                            <td>
                                                <c:field caption="Period Rate " name="policy.dbPeriodRate" type="money16.2" mandatory="true"
                                                         readonly="false" width="60"/>
                                                <c:field caption="Period Base " width="140" name="policy.stPeriodBaseID" type="string"
                                                         mandatory="true" readonly="false" lov="LOV_PeriodBase"
                                                         changeaction="changePeriodBase"/>
                                            </td>
                                        </tr>
                                        
                                        <tr>
                                            <td>Rate Factor</td>
                                            <td>:</td>
                                            <td>
                                                <c:field caption="Rate" width="130" name="policy.stPremiumFactorID" type="string"
                                                         mandatory="true" readonly="<%=!showRateFactor%>" lov="LOV_PeriodFactor"
                                                         changeaction="changePeriodFactor"/>
                                                <%-- <c:button text="Calc" event="calcPeriods" show="<%=!viewMode%>" defaultRO="true" />--%>
                                                <c:button text="Default" event="defaultPeriod" show="<%=!viewMode%>" defaultRO="true"/>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <c:field width="150" caption="{L-ENGInsured Amount-L}{L-INAHarga Pertanggungan-L}" name="policy.dbInsuredAmount"
                                     type="money16.2" mandatory="false" readonly="true" presentation="standard"/>
                            <c:field show="<%=statusEndorse%>" width="150" caption="{L-ENGEndorse Amount-L}{L-INAHarga Endorse-L}"
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
                            <%--<c:field width="200" show="true" lov="LOV_TemporaryPolicy" popuplov="true" caption="{L-ENGTemporary Policy No-L}{L-INAPolis Sementara-L}"
                                     name="policy.stReference6" type="string|32" mandatory="false" presentation="standard">
                                <c:lovLink name="cc_code" link="policy.stCostCenterCode" clientLink="false"/>
                            </c:field>--%>
                            
                                
                            <c:field width="300" show="true" lov="LOV_ParentPolicy" popuplov="true" clientchangeaction="getPolicyHistory()" caption="{L-ENGParent Policy No-L}{L-INANomor Polis Induk-L}"
                                     name="policy.stReference2" type="string|32" mandatory="false" presentation="standard">
                                <c:lovLink name="cc_code" link="policy.stCostCenterCode" clientLink="false"/>
                            </c:field>   
                            <c:fieldcontrol when="<%=cROendorsePeriodFactor%>" readonly="false">
                            
                            <c:field clientchangeaction="selectCustomer2();setFeeBase();" name="policy.stEntityID" type="string" width="300"
                                     popuplov="true" lov="LOV_Entity" mandatory="true"
                                     caption="<%=policy.getPolicyType().getStEntityMasterLabel()%>" presentation="standard"/>
                                     
                                         <c:field width="300" rows="3" caption="<%=policy.getPolicyType().getStEntityMasterDescriptionLabel()%>"
                                                  name="policy.stCustomerName" type="string|512" mandatory="true" readonly="false"
                                                  presentation="standard"/>
                                         <c:field width="300" rows="3" caption="{L-ENGAddress-L}{L-INAAlamat-L}" name="policy.stCustomerAddress"
                                                  type="string|255" mandatory="true" readonly="false" presentation="standard"/>
                                     
                            <c:field clientchangeaction="selectProducer2()" name="policy.stProducerID" type="string" width="300"
                                     popuplov="true" lov="LOV_Entity" mandatory="true" caption="{L-ENGMarketer-L}{L-INASumber Bisnis-L}"
                                     readonly="false" presentation="standard"/>
                             
                            <c:field width="300" rows="3" caption="{L-ENGMarketer Name-L}{L-INANama Sumber Bisnis-L}" name="policy.stProducerName"
                                     type="string|255" mandatory="true" readonly="false" presentation="standard"/>
                            <c:field width="300" rows="3" caption="{L-ENGMarketer Address-L}{L-INAAlamat Sumber Bisnis-L}"
                                     name="policy.stProducerAddress" type="string|255" mandatory="true" readonly="false"
                                     presentation="standard"/>
                            <%--<c:field width="300" show="true" lov="LOV_EntityOnly" popuplov="true" caption="{L-ENGBusiness Source-L}{L-INASumber Bisnis-L}"
                                     name="policy.stBusinessSourceID" type="string" mandatory="false" presentation="standard" />
                            --%>
                            <c:field name="policy.stMarketingOfficerWho" type="string" width="300"
                                     popuplov="true" lov="LOV_EntityOnly" caption="{L-ENGMarketing Officer-L}{L-INAMarketing Officer-L}"
                                     readonly="false" presentation="standard" mandatory="true" />
                            </c:fieldcontrol>
                            <c:evaluate when="<%=policy.getSPPAFF()!=null%>"> 
                                <c:flexfield ffid="<%=policy.getSPPAFF().getStFlexFieldHeaderID()%>" prefix="policy."/>
                            </c:evaluate>
                            <%--<c:field caption="Period Length" name="policy." type="string|64" mandatory="false" readonly="false" presentation="standard"/>--%>
                            <c:field caption="{L-ENGPosted-L}{L-INAJurnal-L}" name="policy.stPostedFlag" type="check" mandatory="false"
                                     overrideRO="true" show="false" readonly="true" presentation="standard"/>
                            
                            <c:evaluate when="<%=policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_KREASI")||
                                                policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_CREDIT")||
                                                policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_CREDIT_MACET")||
                                                policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_CREDIT_SERBA")%>">
                                <%--<c:evaluate when="<%=policy.isStatusDraft()||policy.isStatusSPPA()||(policy.isStatusPolicy()&&policy.getStKreasiTypeID()==null)||policy.isStatusRenewal()%>">
                                    <c:field clientchangeaction="selectCreditType()" name="policy.stKreasiTypeID" type="string" width="200"
                                         popuplov="true" lov="LOV_TypeOfCredit2" 
                                         caption="{L-ENGType Of Credit-L}{L-INAJenis Kredit-L}" mandatory="true" presentation="standard">
                                        <c:lovLink name="cc_code" link="policy.stCostCenterCode" clientLink="false"/>
                                    </c:field>
                                </c:evaluate>
                                <c:evaluate when="<%=policy.isStatusEndorse()||policy.isStatusClaim()||policy.isStatusClaimEndorse()||policy.isStatusHistory()||(policy.isStatusPolicy()&&policy.getStKreasiTypeID()!=null)%>">
                                    <c:field clientchangeaction="selectCreditType()" name="policy.stKreasiTypeID" type="string" width="200"
                                         popuplov="true" lov="LOV_TypeOfCredit" 
                                         caption="{L-ENGType Of Credit-L}{L-INAJenis Kredit-L}" mandatory="true" presentation="standard">
                                        <c:lovLink name="cc_code" link="policy.stCostCenterCode" clientLink="false"/>
                                    </c:field>
                                </c:evaluate>--%>

                                <c:field clientchangeaction="selectCreditType()" name="policy.stKreasiTypeID" type="string" width="300"
                                         popuplov="true" lov="LOV_TypeOfCredit" changeaction="validateJenisKredit"
                                         caption="{L-ENGType Of Credit-L}{L-INAJenis Kredit-L}" mandatory="true" presentation="standard">
                                        <c:lovLink name="cc_code" link="policy.stCostCenterCode" clientLink="false"/>
                                </c:field>
                                
                                <c:field show="false" width="300" caption="{L-ENGKreasi Type Name-L}{L-INANama Jenis Kreasi-L}"
                                         name="policy.stKreasiTypeDesc" type="string" readonly="false"
                                         presentation="standard"/>

                            </c:evaluate>
                             <c:evaluate when="<%=policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_KREASI") && showCoinsurance%>">
                                <c:field clientchangeaction="selectReinsKreasi()" name="policy.stCoinsID" type="string" width="300"
                                         popuplov="true" lov="LOV_InsuranceCompany" 
                                         caption="{L-ENGReinsurer-L}{L-INAReasuradur-L}" presentation="standard"/>
                                <c:field show="false" width="300" caption="{L-ENGReinsurer Name-L}{L-INANama Reasuradur-L}"
                                         name="policy.stCoinsName" type="string" readonly="false"
                                         presentation="standard"/>
                            </c:evaluate>
                            
                            <tr>
                                <td rowspan="2" valign="top">File Konversi</td>
                                <td rowspan="2" valign="top">:</td>
                                
                                <c:evaluate when="<%=isLampiran%>">
                                    <td>
                                        <table width="300" border="1" cellspacing="0" cellpadding="2" bordercolor="#1a5591">
                                            <tr>
                                                <td  width="300" colspan="2" class=row0>
                                                    <c:listbox name="policy.konversiDocuments">
                                                        <c:listcol title="">
                                                            <c:field name="policy.konversiDocuments.[$index$].stSelectedFlag" type="check"
                                                                     mandatory="false" hidden="true" />
                                                        </c:listcol>
                                                        <c:listcol title=" Upload File Konversi Here">
                                                            <c:field width="180" name="policy.konversiDocuments.[$index$].stFilePhysic" type="file"
                                                                     thumbnail="true" caption="KONVERSI"/>
                                                        </c:listcol>
                                                    </c:listbox>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td><c:button show="true" text="Konversi" event="uploadExcel"/>
                                                </td>
                                                <td><c:button show="true" text="Get History" event="btnKreasi"/></td>
                                                <%--<td><c:button show="<%=form.isEnableSuperEdit()%>" text="Create Endorse" event="uploadExcelCreateEndorseKreasi"/></td>
                                                --%>
                                                </tr>
                                        </table>
                                    </td>
                                </c:evaluate>
                                
                                <td valign="top">
                                    <c:button text="+" event="addLampiran" show="true" defaultRO="true"/>
                                    <c:button text="-" event="delLampiran" show="true" defaultRO="true"/>
                                </td>
                            </tr>
                            
                            <tr>
                                <td>
                                    <c:evaluate when="<%=isMember%>">
                                        <c:field width="300" caption="{L-ENGLeader-L}{L-INALeader-L}"
                                                 name="policy.stCoLeaderID" popuplov="true" lov="LOV_Entity" type="string" mandatory="false" readonly="false"
                                                 presentation="standard"/>
                                        <c:field width="300" caption="{L-ENGLeader Policy No-L}{L-INANo Polis Rujukan-L}"
                                                 name="policy.stCoinsPolicyNo" type="string" mandatory="true" readonly="false"
                                                 presentation="standard"/>
                                        <c:field caption="Share Askrida" width="50" name="policy.dbSharePct" type="money16.5"
                                                 mandatory="true" readonly="false" suffix="%" presentation="standard"/>
                                    </c:evaluate>
                                </td>
                            </tr>
                            
                            <c:evaluate when="<%=form.isEnableSuperEdit()%>">
                                <c:field lov="LOV_User" caption="{L-ENGR/I Approved Who-L}{L-INAR/I Approved Who-L}"
                                         name="policy.stReinsuranceApprovedWho" type="string" mandatory="false" readonly="false"
                                         width="200" presentation="standard" show="<%=cROReas&&form.isApprovedReasMode()%>" popuplov="true" overrideRO="true" />
                            </c:evaluate> 

                            <c:field caption="{L-ENGReady To Approve-L}{L-INASiap Disetujui-L}" name="policy.stReadyToApproveFlag" type="check" mandatory="false"
                                     readonly="false" presentation="standard"/>

                            <c:field width="200" changeaction="changeBranch" lov="LOV_CostCenter" caption="{L-ENGBranch-L}{L-INACabang-L} Penerbit"
                                     name="policy.stCostCenterCodeSource" type="string" mandatory="true"
                                     readonly="<%=!canChangeCoverType || policy.getStCostCenterCodeSource()!=null%>" presentation="standard"/>
                                     
                            <c:field width="200" lov="LOV_Region"
                                     changeaction="onChangeRegion" caption="{L-ENGRegion-L}{L-INADaerah-L} Penerbit" name="policy.stRegionIDSource"
                                     type="string" mandatory="false" readonly="<%=!canChangeCoverType%>" presentation="standard">
                                <c:lovLink name="cc_code" link="policy.stCostCenterCodeSource" clientLink="false"/>
                            </c:field>
                            <c:field width="300" show="true" lov="LOV_EntityOnly" popuplov="true" caption="{L-ENGPayment To-L}{L-INATagihan Kepada-L}"
                                     name="policy.stPaymentCompanyID" type="string" mandatory="false" presentation="standard" />
                        </table>
                    </td>
                </tr>
                <tr>
                    <td colspan=2>
                        <table cellpadding=2 cellspacing=1>
                            <c:field width="550" rows="2" caption="{L-ENGPolicy Notes-L}{L-INACatatan Polis-L}"
                                     name="policy.stDescription" type="string" mandatory="false" readonly="false"
                                     presentation="standard"/>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td colspan=2>
                        <table cellpadding=2 cellspacing=1>
                            <c:field width="550" rows="2" caption="{L-ENGWarranty-L}{L-INAWarranty-L}&nbsp &nbsp &nbsp"
                                     name="policy.stWarranty" type="string" mandatory="false" readonly="false"
                                     presentation="standard"/>
                        </table>
                    </td>
                </tr>
                <c:evaluate when="<%=form.isReverseMode()%>">
                    <tr>
                        <td colspan=2>
                            <table cellpadding=2 cellspacing=1>
                                <c:field width="550" rows="3" caption="{L-ENGReverse Notes-L}{L-INACatatan Reverse-L}"
                                         name="policy.stReverseNotes" type="string" mandatory="true" 
                                         overrideRO="true" presentation="standard"/>
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
        
        <c:tabpage name="TAB_REJECT">
            <c:fieldcontrol when="<%=isRejectMode%>" readonly="false">
                <c:field width="750" rows="20" caption="Reject Notes" name="policy.stRejectNotes" type="string"
                         mandatory="true" readonly="false"/>
            </c:fieldcontrol>
        </c:tabpage>
        
        <c:tabpage name="TAB_ENDORSE">
            <c:fieldcontrol when="<%=isEndorseMode%>" readonly="false">
                <c:field width="750" rows="20" caption="Endorsement Notes" name="policy.stEndorseNotes" type="string"
                         mandatory="true" readonly="false"/>
            </c:fieldcontrol>
        </c:tabpage>
        <c:tabpage name="TAB_RISK_DET">
            <table cellpadding=2 cellspacing=1 width="100%">
                <tr>
                    <td>
                        <table cellpadding=2 cellspacing=1>
                            <tr>
                                <td>Cari Objek</td>
                                <td>:</td>
                                <td>
                                     <c:field width="400" name="stKey" caption="Search"
                                             type="string" overrideRO="true" readonly="false"/>
                                     &nbsp;<c:button text="Cari" event="searchObject"/>
                                     <c:button text="Max TSI" event="showTop5"/>
                                    
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
                                             <c:button text="Pilih" clientEvent="<%="docEl('objIndex').value="+objx.getStOrderNo()+";"%>" event="chooseObject" />
                                         </c:listcol>
                                     </c:listbox>
                                </td>
                            </tr>
                            </c:evaluate>
                            <c:evaluate when="<%=form.isShowTop5Object()%>">
                            <tr>
                                <td></td>
                                <td></td>
                                <td>Nilai TSI Tertinggi (Top 5)
                                    <c:field name="objIndex" type="string" hidden="true" overrideRO="true" />
                                    <c:listbox name="objectTop5" view="com.webfin.insurance.model.InsurancePolicyObjDefaultView">
                                         <%
                                                final InsurancePolicyObjDefaultView objx5 = (InsurancePolicyObjDefaultView) current;

                                         %>
                                         <c:listcol name="stOrderNo" title="No Urut" />
                                         <c:listcol name="stReference1" title="Nama" />
                                         <c:listcol name="dbObjectInsuredAmount" title="TSI" />
                                         <c:listcol name="dbObjectPremiTotalBeforeCoinsuranceAmount" title="Premi" />
                                         <c:listcol title="">
                                             <c:button text="Pilih" clientEvent="<%="docEl('objIndex').value="+objx5.getStOrderNo()+";"%>" event="chooseObject" />
                                         </c:listcol>
                                     </c:listbox>
                                </td>
                            </tr>
                            </c:evaluate>
                            <tr>
                                <td>{L-ENGObject-L}{L-INAObjek Pertanggungan-L}</td>
                                <td>:</td>
                                <td>
                                    <b><font color=red>
                                        <c:field width="400" lov="lovObjects" name="stSelectedObject" caption="Selected Object"
                                             type="string" changeaction="selectObject" overrideRO="true" readonly="false"/>
                                        </font>
                                    </b>
                                    <c:fieldcontrol when="<%=cROpolicyRiskDetail%>" readonly="true">
                                        <c:button text="+" event="doNewObject" defaultRO="true"/>
                                        
                                        <c:fieldcontrol when="<%=statusEndorse%>" readonly="true">
                                            <c:button text="-" event="doDeleteObject" confirm="Yakin Ingin Menghapus Objek Ini ?" defaultRO="true"/>
                                            <c:button text="Del All" event="doDeleteAllObjects" confirm="Yakin Ingin Menghapus Seluruh Objek ?" defaultRO="true"/>
                                        </c:fieldcontrol>
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
                                       <c:fieldcontrol when="<%=cROendorseObject%>" readonly="false">
                                        <table cellpadding=2 cellspacing=1 class=header>
                                            <tr>
                                                <td>
                                                    {L-ENGObject-L}{L-INAObjek Pertanggungan-L}
                                                </td>
                                            </tr>
                                            <tr class=row0>
                                                <td>
                                                    <table cellpadding=2 cellspacing=1>
                                                        <c:evaluate when="<%=form.getSelectedObject().isVoid()%>">
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

                                                        <%
                                                            if(selectedObject!=null){
                                                                if(selectedObject.getObjectDefault().getStReference76()!=null){
                                                                    if(selectedObject.getObjectDefault().getStReference76().equalsIgnoreCase("1")){
                                                                        perorangan = true;
                                                                    }else{
                                                                        perorangan = false;
                                                                    }
                                                                }
                                                            }
                                                            boolean isKredit = policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_KREASI")||
                                                                                policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_CREDIT");
                                                        %>
                                                        <c:field lov="VS_INSOBJ_NASABAH_TYPE" caption="Jenis Nasabah"
                                                                 name="selectedObject.stReference76" type="string" mandatory="false" readonly="false"
                                                                 width="200" presentation="standard" refresh="true" changeaction="refresh"/>

                                                        <c:evaluate when="<%=selectedObject.getObjectDefault().getStReference76()!=null%>">
                                                                <c:field show="<%=perorangan && !isKredit%>" caption="NIK"
                                                                    name="selectedObject.stReference77" type="string" mandatory="false" readonly="false"
                                                                    width="300" presentation="standard" />
                                                                <c:field  show="<%=!perorangan%>" caption="NPWP"
                                                                    name="selectedObject.stReference78" type="string" mandatory="false" readonly="false"
                                                                    width="300" presentation="standard" />
                                                                <c:field lov="LOV_Jobs" caption="Pekerjaan"
                                                                    name="selectedObject.stReference79" type="string" mandatory="false" readonly="false"
                                                                    width="400" presentation="standard" refresh="true">
                                                                    <c:lovLink name="jenisDebitur" link="selectedObject.stReference76" clientLink="false"/>
                                                                 </c:field>
                                                                 <c:field lov="LOV_Jobs_Detail" caption="Bidang Usaha"
                                                                         show="<%=!perorangan%>" name="selectedObject.stReference80" type="string" mandatory="false" readonly="false"
                                                                         width="400" presentation="standard" refresh="true"/>
                                                        </c:evaluate>
                                                        
                                                        <c:field width="100" caption="Limit Of Liability (LOL)"
                                                                 name="selectedObject.dbLimitOfLiability2" type="money16.2" mandatory="false"
                                                                 presentation="standard"/>
                                                        <c:field width="100" caption="Limit Of Liability (LOL) %"
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
                                            <c:evaluate when="<%="OM_CREDIT".equalsIgnoreCase(policy.getPolicyType().getStObjectMapID())%>">
                                                <tr>
                                                <td>
                                                    Utilities
                                                </td>
                                                </tr>
                                                <tr class=row0>
                                                    <td>
                                                        <c:button text="Tidak Double (All)" event="applyAllNotDouble" show="<%=!viewMode%>" defaultRO="true" />
                                                        <c:button show="true" text="Polis Jiwa" clientEvent="downloadEPolisPAJ();" />
                                                    </td>
                                                </tr>
                                            </c:evaluate>
                                            
                                        </table>
                                        </c:fieldcontrol>
                                    </c:fieldcontrol>
                                </c:tabpage>

                                <c:evaluate when="<%=form.getSelectedObject().getPolicyDetailFF()!=null%>">
                                    <c:tabpage name="TAB_DETAIL_POLICY">
                                        <c:fieldcontrol when="<%=cROpolicyRiskDetail%>" readonly="true">
                                            <table cellpadding=2 cellspacing=1 class=header>
                                                <tr>
                                                    <td>
                                                        DETIL POLIS
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class=row0>
                                                        <table cellpadding=2 cellspacing=1>
                                                            <c:flexfield ffid="<%=form.getSelectedObject().getPolicyDetailFF().getStFlexFieldHeaderID()%>"
                                                                         prefix="selectedObject."/>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                            <br>DETAIL OUTGO >><br><br>
                                            <c:listbox name="details">
                                                <%
                                                final DTOList details = form.getDetails();
                                                final InsurancePolicyItemsView item = ((InsurancePolicyItemsView) details.get(index.intValue()));

                                                boolean deletable = item != null && item.isDeletable();
                                                //boolean deletable = item != null;

                                                boolean not_usetax = item != null && item.getInsItem().isNotUseTax();

                                                //if(policy.isStatusEndorse() && item != null && !item.isDeletable())
                                                    //deletable = item != null;

                                                %>
                                                
                                                <c:listcol name="InsItem.stDescription" title="{L-ENGITEMS-L}{L-INAITEM-L}"/>


                                                <c:listcol title="{L-ENGDESCRIPTION-L}{L-INADESKRIPSI-L}">
                                                    <c:field caption="Description" name="details.[$index$].stDescription" width="200" type="string|255"
                                                             mandatory="false" readonly="false"/>
                                                </c:listcol>
                                                <c:listcol title="RATE">
                                                    <c:field name="details.[$index$].stFlagEntryByRate"
                                                             clientchangeaction="VM_switchReadOnly('details.[$index$].dbRate',this.checked);VM_switchReadOnly('details.[$index$].dbAmount',!this.checked)"
                                                             readonly="<%=!deletable%>" type="check" />
                                                    <c:field caption="Rate" name="details.[$index$].dbRate" width="70" type="money16.6" mandatory="false"
                                                             readonly="<%=!deletable%>"/> %
                                                </c:listcol>
                                                

                                                <c:listcol title="Jumlah 100%" align="right">
                                                    <c:field caption="Amount" name="details.[$index$].dbAmount100" type="money16.2" mandatory="false"
                                                             readonly="false"/>
                                                </c:listcol>
                                                <c:listcol title="Polis 1" align="right">
                                                    <c:field caption="Amount" name="details.[$index$].dbAmount1" type="money16.2" mandatory="false"
                                                             readonly="false"/>
                                                </c:listcol>
                                                <c:listcol title="Polis 2" align="right">
                                                    <c:field caption="Amount" name="details.[$index$].dbAmount2" type="money16.2" mandatory="false"
                                                             readonly="false"/>
                                                </c:listcol>
                                                <c:listcol title="Polis 3" align="right">
                                                    <c:field caption="Amount" name="details.[$index$].dbAmount3" type="money16.2" mandatory="false"
                                                             readonly="false"/>
                                                </c:listcol>
                                                <c:listcol title="Polis 4" align="right">
                                                    <c:field caption="Amount" name="details.[$index$].dbAmount4" type="money16.2" mandatory="false"
                                                             readonly="false"/>
                                                </c:listcol>
                                                <c:listcol title="Polis 5" align="right">
                                                    <c:field caption="Amount" name="details.[$index$].dbAmount5" type="money16.2" mandatory="false"
                                                             readonly="false"/>
                                                </c:listcol>
                                                <c:listcol title="Polis 6" align="right">
                                                    <c:field caption="Amount" name="details.[$index$].dbAmount6" type="money16.2" mandatory="false"
                                                             readonly="false"/>
                                                </c:listcol>

                                               
                                                <%--
                                                <c:listcol title="Akumulasi Sebelumnya" align="right">
                                                    <c:field caption="Amount" name="details.[$index$].dbTotalAmount" type="money16.2" mandatory="false"
                                                             readonly="true" width="150"/>
                                                </c:listcol>
                                                <c:listcol title="Akumulasi Hingga Bulan Ini" align="right">
                                                    <c:field caption="Amount" name="details.[$index$].dbTotalAmountUntilThisMonth" type="money16.2" mandatory="false"
                                                             readonly="true" width="150"/>
                                                </c:listcol>
                                                --%>

                                            <c:evaluate when="<%=item!=null && item.isComission() && !not_usetax%>">
                                            </tr>
                                            <tr class=row2>
                                            
                                            <c:listcol title="ITEMS">{L-INAPAJAK-L}{L-ENGTAX-L}</c:listcol>
                                            <c:listcol title="DESCRIPTION">
                                                <c:field lov="LOV_ARTax" caption="TAX" width="200" name="details.[$index$].stTaxCode" type="string"
                                                         mandatory="true" readonly="<%=!deletable%>">
                                                </c:field>
                                            </c:listcol>
                                             <%
                                                final boolean readonlyTax = true; //!policy.getStCostCenterCode().equalsIgnoreCase("20");

                                                %>
                                            <c:listcol title="RATE">
                                                <c:field name="details.[$index$].stTaxAutoRateFlag"
                                                         clientchangeaction="VM_switchReadOnly('details.[$index$].dbTaxRatePct',this.checked);"
                                                         type="check" readonly="<%=readonlyTax%>" />
                                                <c:field caption="Rate" name="details.[$index$].dbTaxRatePct" width="70" type="money16.3"
                                                         mandatory="false" readonly="<%=readonlyTax%>"/> %
                                            </c:listcol>
                                            
                                             <c:listcol title="{L-ENGAMOUNT-L}{L-INAJUMLAH-L} 100%" align="right">
                                                    <c:field caption="Amount" name="details.[$index$].dbTaxAmount100" type="money16.2" mandatory="false"
                                                             readonly="false"/>
                                                </c:listcol>
                                                <c:listcol title="Polis 1" align="right">
                                                    <c:field caption="Amount" name="details.[$index$].dbTaxAmount1" type="money16.2" mandatory="false"
                                                             readonly="false"/>
                                                </c:listcol>
                                                <c:listcol title="Polis 2" align="right">
                                                    <c:field caption="Amount" name="details.[$index$].dbTaxAmount2" type="money16.2" mandatory="false"
                                                             readonly="false"/>
                                                </c:listcol>
                                                <c:listcol title="Polis 3" align="right">
                                                    <c:field caption="Amount" name="details.[$index$].dbTaxAmount3" type="money16.2" mandatory="false"
                                                             readonly="false"/>
                                                </c:listcol>
                                                <c:listcol title="Polis 4" align="right">
                                                    <c:field caption="Amount" name="details.[$index$].dbTaxAmount4" type="money16.2" mandatory="false"
                                                             readonly="false"/>
                                                </c:listcol>
                                                <c:listcol title="Polis 5" align="right">
                                                    <c:field caption="Amount" name="details.[$index$].dbTaxAmount5" type="money16.2" mandatory="false"
                                                             readonly="false"/>
                                                </c:listcol>
                                                <c:listcol title="Polis 6" align="right">
                                                    <c:field caption="Amount" name="details.[$index$].dbTaxAmount6" type="money16.2" mandatory="false"
                                                             readonly="false"/>
                                                </c:listcol>
                                           
                                            <%--
                                            <c:listcol title=""/>
                                            <c:listcol title=""/>
                                            --%>
                                            </c:evaluate>
                                            </c:listbox>
                                        </c:fieldcontrol>
                                    </c:tabpage>
                                </c:evaluate>

                                <c:evaluate when="<%=form.getSelectedObject().getSPPAFF()!=null%>">
                                    <c:tabpage name="TAB_SPPA">
                                        <c:fieldcontrol when="<%=cROpolicyRiskDetail%>" readonly="true">
                                            <table cellpadding=2 cellspacing=1 class=header>
                                                <tr>
                                                    <td>
                                                        SPPA
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class=row0>
                                                        <table cellpadding=2 cellspacing=1>
                                                            <c:flexfield ffid="<%=form.getSelectedObject().getSPPAFF().getStFlexFieldHeaderID()%>"
                                                                         prefix="selectedObject."/>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                        </c:fieldcontrol>
                                    </c:tabpage>
                                </c:evaluate>

                                <c:tabpage name="TAB_SI">
                                    
                                    <table cellpadding=2 cellspacing=1 class=header>
                                        <tr>
                                            <td colspan=3 class=header>
                                                
                                                <table cellpadding=2 cellspacing=1 class=row0 >
                                                    
                                                    <tr>
                                                        <td>{L-ENGPer Object Period Factor-L}{L-INAPeriod Factor Per Objek-L}</td>
                                                        <td>:</td>
                                                        <td>
                                                            <c:field caption="{L-ENGPer Object Period Factor-L}{L-INAPeriod Factor Per Objek-L}" name="selectedObject.stPeriodFactorObjectFlag" type="check" mandatory="false"
                                                                readonly="false" />
                                                        </td>
                                                    </tr>
                                                    <c:evaluate when="<%=statusEndorse%>">
                                                        <c:fieldcontrol when="<%=cROendorsePeriodFactor%>" readonly="false">
                                                        <tr>
                                                            <td>Period Factor Objek(Endorse)</td>
                                                            <td>:</td>
                                                            <td>
                                                                <c:field show="<%=policy.getStPeriodBaseBeforeID()!=null%>" caption="Period Rate Before"
                                                                         name="selectedObject.dbPeriodRateBefore" type="money16.2" mandatory="false" width="60"/>
                                                                <c:field caption="Period Base Before" name="selectedObject.stPeriodBaseBeforeID" type="string"
                                                                         mandatory="false" readonly="false" lov="LOV_PeriodBase"
                                                                         changeaction="changePeriodBase" width="140"/>
                                                            </td>
                                                        </tr>
                                                        </c:fieldcontrol>
                                                    </c:evaluate>
                                                    <tr>
                                                        <td>Period Factor Objek</td>
                                                        <td>:</td>
                                                        <td>
                                                            <c:field caption="Period Rate " name="selectedObject.dbPeriodRate" type="money16.2" mandatory="false"
                                                                     readonly="false" width="60"/>
                                                            <c:field caption="Period Base " width="140" name="selectedObject.stPeriodBaseID" type="string"
                                                                     mandatory="false" readonly="false" lov="LOV_PeriodBase" />
                                                        </td>
                                                    </tr>

                                                    <tr>
                                                        <td>Rate Factor Objek</td>
                                                        <td>:</td>
                                                        <td>
                                                            <c:field caption="Rate" width="130" name="selectedObject.stPremiumFactorID" type="string"
                                                                     mandatory="false" readonly="<%=!showRateFactor%>" lov="LOV_PeriodFactor" />
                                                            <%-- <c:button text="Calc" event="calcPeriods" show="<%=!viewMode%>" defaultRO="true" />--%>
                                                            &nbsp;<c:button text="Default" event="defaultPeriodObject" show="<%=!viewMode%>" defaultRO="true"/>
                                                        </td>
                                                    </tr>
                                                    
                                                </table>
                                            </td>
                                        </tr>
                                    </table>
                                    <br>
                                    
                                    <c:fieldcontrol when="<%=cROpolicyRiskDetail%>" readonly="true">
                                        <c:fieldcontrol when="<%=cROpolicySumInsured%>" readonly="false">
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
                                                        final String labelTSI = policy.isStatusEndorse()?"{L-ENGAmount-L}{L-INAJumlah-L} (Nilai Menjadi)":"{L-ENGAmount-L}{L-INAJumlah-L}";
                                                        
                                                        %>
                                                        <c:listcol title="">
                                                            <c:fieldcontrol when="<%=statusEndorse%>" readonly="true">
                                                                <c:button text="-" clientEvent="f.tsiIndex.value=$index$;" event="onClickDeleteSumInsuredItem"
                                                                      show="<%=!rotsi && !lockTSI && !defaultTSI%>" defaultRO="true"/>
                                                            </c:fieldcontrol>
                                                        </c:listcol>
                                                        <c:listcol title="{L-ENGCategory-L}{L-INAKategori-L}"
                                                                   name="stInsuranceTSIDesc2"/>
                                                        <c:listcol title="{L-ENGCategory Description-L}{L-INADeskripsi Kategori-L}">
                                                            <c:field caption="Description" width="250" rows="2"
                                                                     name="selectedObject.suminsureds.[$index$].stTSICategoryDescription" type="string" mandatory="false"
                                                                     readonly="false"/>
                                                        </c:listcol>
                                                        <c:listcol title="{L-ENGExc-L}{L-INAExc-L}">
                                                            <%=JSPUtil.print(tsix.getStTSIExcluded())%>
                                                        </c:listcol>
                                                        <c:listcol title="{L-ENGDescription-L}{L-INAPenjelasan-L}">
                                                            <c:field caption="Description" width="350" rows="2"
                                                                     name="selectedObject.suminsureds.[$index$].stDescription" type="string" mandatory="false"
                                                                     readonly="false"/>
                                                        </c:listcol>
                                                        <c:evaluate when="<%=isMember%>">
                                                            <c:listcol title="Sum Insured 100%">
                                                                <c:field caption="Amount" name="selectedObject.suminsureds.[$index$].dbInsuredAmountFull"
                                                                         type="money16.2" mandatory="false" />
                                                            </c:listcol>
                                                        </c:evaluate>

                                                        <c:listcol title="<%=labelTSI%>">
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
                                        <br>
                                            </c:fieldcontrol>
                                        </c:fieldcontrol>

                                        
                                    <c:fieldcontrol when="<%=cROpolicyRiskDetail%>" readonly="true">
                                        <c:fieldcontrol when="<%=cROpolicyCoverage%>" readonly="false">
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
                                                            
                                                            //final boolean defaultCover = cover != null && Tools.isYes(cover.getStDefaultCoverFlag());
                                                            
                                                            //String rateScaleTxt = cover == null ? null : cover.getStRateScaleSymbol();
                                                            
                                                            %>
                                                            <c:listcol title="">
                                                                <c:evaluate when="<%=!((InsurancePolicyCoverView)current).isMainCover()%>">
                                                                    <%-- <c:button text="-" clientEvent="f.covIndex.value=$index$;" event="onClickDeleteCoverageItem" show="<%=!rocov && !defaultCover%>" defaultRO="true"/>--%>
                                                                    <c:button text="-" clientEvent="f.covIndex.value=$index$;" event="onClickDeleteCoverageItem"
                                                                              show="true" defaultRO="true"/>
                                                                    
                                                                </c:evaluate>
                                                            </c:listcol>
                                                            <c:listcol title="{L-ENG Category-L}{L-INAKategori-L}"
                                                                       name="stInsuranceCoverDesc2"/>
                                                            <c:listcol title="{L-ENGCategory Description-L}{L-INADeskripsi Kategori-L}">
                                                                <c:field name="selectedObject.coverage.[$index$].stDescription" rows="2" width="160" type="string"/>
                                                            </c:listcol>
                                                        
                                                            <c:listcol title="{L-ENGAmount-L}{L-INAJumlah-L}">
                                                                <%-- <c:field name="selectedObject.coverage.[$index$].stEntryInsuredAmountFlag" clientchangeaction="VM_switchReadOnly('selectedObject.coverage.[$index$].dbInsuredAmount',this.checked)" type="check" readonly="<%=rocov || manualTSILock%>" />
                                                                --%>                 <c:field
                                                                    name="selectedObject.coverage.[$index$].stEntryInsuredAmountFlag"
                                                                    clientchangeaction="VM_switchReadOnly('selectedObject.coverage.[$index$].dbInsuredAmount',this.checked)"
                                                                    type="check" readonly="<%=manualTSILock%>" />
                                                                
                                                                
                                                                <c:field caption="Amount" name="selectedObject.coverage.[$index$].dbInsuredAmount"
                                                                         type="money16.2" mandatory="false" readonly="<%=rocov%>"/>
                                                            </c:listcol>

                                                            <c:evaluate when="<%=form.getSelectedObject().isDepreciationMethod()%>">
                                                                <c:listcol title="Tahun<br>Depresiasi">
                                                                   <c:field name="selectedObject.coverage.[$index$].stDepreciationYear"
                                                                                     width="40" type="string" readonly="false"/>
                                                                </c:listcol>
                                                                <c:listcol title="PCT<br>Depresiasi">
                                                                   <c:field name="selectedObject.coverage.[$index$].dbDepreciationPct"
                                                                            width="60" type="money16.2" readonly="false" suffix=" %"/>
                                                                </c:listcol>
                                                            </c:evaluate>
           
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
                                                                         type="money16.6" mandatory="false" readonly="<%=rocov%>"/>
                                                                <%--<c:field caption="<%=policy.getStRateMethodDesc()%>" width="30" name="policy.stRateMethodDesc" type="string" mandatory="false" readonly="true"/>
                        <%=policy.getStRateMethodDesc()%>--%>
                                                                <%=policy.getStRateMethodDesc()%>
                                                                <%--<c:button text="<%=policy.getStRateMethodDesc()%>" event="chgRateClass" size="30"
                                                                          clientEvent="f.covIndex.value=$index$;"/>--%>
                                                                
                                                            </c:listcol>
                                                            <%--<c:listcol title="Period Rate" align="right" >
                       <%=JSPUtil.print(policy.getDbPeriodRateDesc())%>
                    </c:listcol>--%>
                                                            
                                                            

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
                                                            <%--
                                                            <c:listcol title="{L-ENGDiscount-L}{L-INADiskon-L} (%)">
                                                               <c:field name="selectedObject.coverage.[$index$].dbDiscountPct"
                                                                        width="60" type="money16.2" readonly="false" suffix=" %"/>
                                                            </c:listcol>
                                                            <c:listcol title="{L-ENGDiscount-L}{L-INADiskon-L}">
                                                               <c:field name="selectedObject.coverage.[$index$].dbDiscountAmount"
                                                                         type="money16.2" readonly="true" />
                                                            </c:listcol>--%>
                                                            
                                                        </c:listbox>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        ADD : <c:field width="450" lov="coverageAddLOV" caption="Coverage" name="coverageAddID" type="string"/>
                                                        
                                                        <c:button event="doAddCoverage" text="+" defaultRO="true"/>
                                                        LINE <c:field width="50" caption="Jumlah" name="coverageLine" type="string"/>
                                                        &nbsp;&nbsp;<c:button text="Apply To All Objects" confirm="Yakin Ingin Menyalin Ke Seluruh Objek ?"event="applyCoverageToAllObjects" validate="false" defaultRO="true"/>
                                                        <c:evaluate when="<%=form.getPolicy().getPolicyType().getStControlFlags()!=null%>">
                                                                    <c:button text="Rate Bawah" confirm="Yakin Ingin Cek Rate ?"event="getLowestRateOJK" validate="false" defaultRO="true"/>
                                                                    <c:button text="Rate Atas" confirm="Yakin Ingin Cek Rate ?"event="getHighestRateOJK" validate="false" defaultRO="true"/>
                                                        </c:evaluate>
                                                    </td>
                          
                                                </tr>
                                                
                                            </table>
                                        
                                        
                                        </c:fieldcontrol>
                                    </c:fieldcontrol>
                                </c:tabpage>
                                <c:tabpage name="TAB_DED">
                                    <c:fieldcontrol when="<%=cROpolicyRiskDetail%>" readonly="true">
                                        <table cellpadding=2 cellspacing=1 class=header>
                                            <tr>
                                                <td class=header> 
                                                    {L-ENGDeductibles-L}{L-INAResiko Sendiri-L}
                                                </td>
                                            </tr>
                                            <tr class=row0>
                                                <td>
                                                    <c:field name="objDeductIndex" type="string" hidden="true"/>
                                                    <c:listbox name="selectedObject.deductibles">
                                                        <c:listcol title="" columnClass="header">
                                                           <%-- <c:button text="+" event="onNewObjDeductible" validate="false" defaultRO="true"/>
                                                            --%>
                                                        </c:listcol>
                                                        <c:listcol title="" columnClass="detail">
                                                            <c:button text="-" event="onDeleteObjDeductible"
                                                                      clientEvent="f.objDeductIndex.value='$index$';" validate="false"
                                                                      defaultRO="true"/>
                                                        </c:listcol>
                                                        <%--<c:listcol title="Description" >
                          <c:field name="deductibles.[$index$].stDescription" type="string|128" width="200" mandatory="false" readonly="false"/>
                        </c:listcol>--%>                <%--
                                                        <c:listcol title="{L-ENGClaim Cause-L}{L-INAPenyebab Klaim-L}">
                                                            <c:field lov="LOV_ClaimCause" caption="{L-ENGClaim Cause-L}{L-INAPenyebab Klaim-L}"
                                                                     name="selectedObject.deductibles.[$index$].stInsuranceClaimCauseID"
                                                                     type="string|128" width="200" mandatory="true" readonly="false">
                                                                <c:lovLink name="poltype" link="policy.stPolicyTypeID" clientLink="false"/>
                                                            </c:field>
                                                            
                                                        </c:listcol>
                                                        --%>
                                                        <c:listcol title="{L-ENGCategory-L}{L-INAKategori-L}"
                                                                   name="stClaimCauseDesc"/>

                                                        <c:listcol title="{L-ENGDescription-L}{L-INADeskripsi-L}">
                                                            <c:field name="selectedObject.deductibles.[$index$].stDescription" type="string"
                                                                     rows ="2" width="200" mandatory="false" readonly="false"/>
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
                                                        <c:listcol title="Time Excess">
                                                            <c:field name="selectedObject.deductibles.[$index$].dbTimeExcess" type="money16.2"
                                                                     width="50" mandatory="false" readonly="false"/>
                                                        </c:listcol>
                                                        <c:listcol title="Unit">
                                                            <c:field name="selectedObject.deductibles.[$index$].stTimeExcessUnit" type="string" lov="LOV_PeriodUnit" 
                                                                     width="80" mandatory="false" readonly="false"/>
                                                        </c:listcol>
                                                        
                                                        <%--
                        <c:listcol title="{L-ENGType-L}{L-INAJenis-L}" >
                          <c:field name="selectedObject.deductibles.[$index$].stDeductTypePer" lov="VS_DEDUCT_TYPE" type="string|128" width="150" mandatory="false" readonly="false"/>
                        </c:listcol>
                        --%>
                                                    </c:listbox>
                                                </td>
                                            </tr>
                                            <tr>
                                                    <td>
                                                        ADD : <c:field width="550" lov="deductibleAddLOV" caption="Deductible" name="deductibleAddID" type="string"/>
                                                        <c:button event="onNewObjDeductible" text="+" defaultRO="true"/>
                                                    </td>
                                                </tr>

                                            <tr>
                                                <td>
                                                    <c:button text="Apply To All Objects" confirm="Yakin Ingin Menyalin Ke Seluruh Objek ?"event="applyDeductibleToAllObjects" validate="false" defaultRO="true"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </c:fieldcontrol>
                                </c:tabpage>

                                <c:evaluate when="<%=policy.getStCoverTypeCode()!=null%>">
                                    <c:tabpage name="TAB_REINS">
                                               <jsp:include page="policyFormRI.jsp" />
                                    </c:tabpage>
                                </c:evaluate>


                                 <c:evaluate when="<%=form.getSelectedObject().getAnalysisFF()!=null%>">
                                    <c:tabpage name="TAB_ANALYSIS">
                                        <c:fieldcontrol when="<%=cROpolicyRiskDetail%>" readonly="true">
                                            <table cellpadding=2 cellspacing=1 class=header>
                                                <tr>
                                                    <td>
                                                        ANALISA RESIKO
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class=row0>
                                                        <table cellpadding=2 cellspacing=1>
                                                            <c:flexfield ffid="<%=form.getSelectedObject().getAnalysisFF().getStFlexFieldHeaderID()%>"
                                                                         prefix="selectedObject."/>
                                                            <c:field width="180" name="selectedObject.stRiskFile" type="file"
                                                                     thumbnail="true" caption="File Pendukung" presentation="standard"/>

                                                            <c:field caption="{L-ENGRisk Approval-L}{L-INAValidasi Cabang-L}" name="selectedObject.stValidasiCabang" type="check" mandatory="false"
                                                                         readonly="<%=!form.isCanEditValidasiCabang()%>" presentation="standard"/>
                                                                <c:field caption="{L-ENGRisk Approval-L}{L-INAValidasi Cabang Induk-L}" name="selectedObject.stValidasiCabangInduk" type="check" mandatory="false"
                                                                         readonly="<%=!form.isCanEditValidasiInduk()%>" presentation="standard"/>
                                                                <c:field caption="{L-ENGRisk Approval-L}{L-INAValidasi Kantor Pusat-L}" name="selectedObject.stValidasiKantorPusat" type="check" mandatory="false"
                                                                         readonly="<%=!form.isCanEditValidasiPusat()%>" presentation="standard"/>

                                                            <c:evaluate when="<%=form.isRiskApprovalMode() || policy.isApprovedRisk()%>">
                                                                <c:field width="600" rows="3" caption="{L-ENGApproval Notes-L}{L-INACatatan Persetujuan-L}"
                                                                         name="selectedObject.stRiskNotes" type="string" overrideRO="true" mandatory="true" readonly="false"
                                                                         presentation="standard"/>
                                                                <c:field caption="{L-ENGRisk Approval-L}{L-INASetujui Resiko-L}" name="selectedObject.stRiskApproved" type="check" mandatory="false"
                                                                        overrideRO="true" show="true" readonly="false" presentation="standard"/>
                                                            </c:evaluate>
                                                        </table>
                                                    </td>
                                                </tr>

                                                <tr>
                                                    <td>
                                                        KIRIM NOTIFIKASI
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class=row0>
                                                        <table cellpadding=2 cellspacing=1>

                                                            <%--<c:field width="200" presentation="standard" name="policy.stNotificationUserID" caption="Penerima Notifikasi" type="string" popuplov="true" overrideRO="true" lov="LOV_Profil" mandatory="false" caption="Penerima" readonly="false"/>
                                                            --%>
                                                            <c:field caption="Kirim Notifikasi" name="kirimNotifikasi" type="check" mandatory="false"
                                                                        show="true" overrideRO="true" readonly="false" presentation="standard"/>
                                                            <%--
                                                            <tr>
                                                                <td colspan="3" align="right">
                                                                    <c:button show="true" text="{L-ENGSend Notif-L}{L-INAKirim Notifikasi-L}" event="sendApprovalLetters" validate="true"
                                                                            confirm="Yakin Mau Dikirim ?"/>
                                                                </td>
                                                            </tr>--%>

                                                        </table>
                                                    </td>
                                                </tr>

                                                <%--<c:evaluate when="<%=form.isShowNotifLetter()%>">--%>
                                                <c:evaluate when="false">
                                                    <tr>
                                                        <td>
                                                            <table class=row0>
                                                                <tr class="header"><td colspan="4" align="center"><b>Kirim Notifikasi</b></td></tr>
                                                                <tr><td><b>Penerima</b></td><td>:</td><td> <c:field name="policy.stNotificationUserID" caption="Penerima" type="string" popuplov="true" overrideRO="true" lov="LOV_Profil" mandatory="true" caption="Penerima" readonly="false"/></td>
                                                                    <td>
                                                                        <c:button show="true" text="{L-ENGSend Notif-L}{L-INAKirim Notifikasi-L}" event="sendApprovalLetters" validate="true"
                                                                            confirm="Yakin Mau Dikirim ?"/>
                                                                    </td>
                                                                </tr>
                                                            </table><br><br>
                                                        </td>
                                                    </tr>
                                                    
                                                </c:evaluate>
                                            </table>
                                        </c:fieldcontrol>
                                    </c:tabpage>
                                    
                                </c:evaluate>


                                        <c:tabpage name="TAB_DETAIL_DOCUMENTS">
                                            <table cellpadding=2 cellspacing=1 class=header width="100%">
                                                <tr>
                                                    <td>
                                                        DOKUMEN
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td class=row0>
                                                        <c:listbox name="selectedObject.detailDocuments">
                                                            <c:listcol title="">
                                                                <c:field name="selectedObject.detailDocuments.[$index$].stSelectedFlag" type="check"
                                                                         mandatory="false" readonly="false"/>
                                                            </c:listcol>
                                                            <c:listcol name="stDescription" title="Deskripsi">
                                                            </c:listcol>
                                                            <c:listcol name="stDescription" title="Deskripsi">
                                                            </c:listcol>
                                                            <c:listcol title="Dokumen">
                                                                <c:field name="selectedObject.detailDocuments.[$index$].stFilePhysic" type="file"
                                                                         thumbnail="true" caption="File"/>
                                                            </c:listcol>
                                                        </c:listbox>
                                                    </td>
                                                </tr>
                                            </table>
                                        </c:tabpage>

                                        <c:evaluate when="<%=policy.isEnabledSubrogasi()%>">
                                            <c:tabpage name="TAB_SUBROGASI">
                                                     <jsp:include page="policyFormSubrogasi.jsp" />
                                            </c:tabpage>
                                        </c:evaluate>
                                

                            </c:tab>
                        </c:evaluate>
                    </td>
                </tr>
            </table>
        </c:tabpage>
        
        <%--
        <c:tabpage name="TAB_CLAUSES">
            <c:fieldcontrol when="<%=cROpolicyClausules%>" readonly="true">
                <table cellpadding=2 cellspacing=1 class=header>
                                            <tr>
                                                <td class=header>
                                                    {L-ENGClausules-L}{L-INAKlausula-L}
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    ADD : <c:field width="550" lov="clausulesLOV" caption="Deductible" name="clausulesAddID" type="string"/>
                                                    <c:button event="onNewClausules" text="+" defaultRO="true"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <c:button event="applyDefaultClausules" text="Tambah Klausula Wajib" defaultRO="true"/>
                                                </td>
                                            </tr>
                                            <tr class=row0>
                                                <td>
                                                    <c:field name="clausulesIndex" type="string" hidden="true"/>
                                                    <c:listbox name="clausules">
                                                        <c:listcol title="" columnClass="header">
                                                        </c:listcol>
                                                        <c:listcol title="" columnClass="detail">
                                                            <c:button text="-" event="onDeleteClausules"
                                                                      clientEvent="f.clausulesIndex.value='$index$';" validate="false"
                                                                      defaultRO="true"/>
                                                        </c:listcol>
                                                        
                                                        <c:listcol name="stClausulesDesc" title="{L-ENGClausules-L}{L-INAKlausula-L}">
                                                             

                                                        </c:listcol>
                                                         <c:listcol title="Keterangan">
                                                             <c:field name="clausules.[$index$].stKeterangan" type="string" mandatory="false" readonly="false" width="250"/>
                                                        </c:listcol>
                                                    </c:listbox>
                                                </td>
                                            </tr>
                                            
                </table>
                
            </c:fieldcontrol>
        </c:tabpage>
        --%>

        <c:tabpage name="TAB_CLAUSES">
            <c:fieldcontrol when="<%=cROpolicyClausules%>" readonly="true">

                <c:listbox name="clausules">
                    <c:listcol title="">
                        <c:field name="clausules.[$index$].stSelectedFlag" type="check" mandatory="false" readonly="false"/>
                    </c:listcol>
                    <c:listcol name="stDescription" title="{L-ENGDESCRIPTION-L}{L-INADESKRIPSI-L}">
                    </c:listcol>
                     <c:listcol title="Description">
                         <c:field name="clausules.[$index$].stKeterangan" type="string" mandatory="false" readonly="false" width="250"/>
                    </c:listcol>
                    <%--
              <c:listcol title="{L-ENGDESCRIPTION-L}{L-INADESKRIPSI-L}" >
              <c:field name="clausules.[$index$].stDescription2" type="string" width="200" mandatory="false" readonly="false"/>
            </c:listcol>--%>
                </c:listbox>
            </c:fieldcontrol>
        </c:tabpage>

        <c:tabpage name="TAB_PREMI">
        <c:fieldcontrol when="<%=cROpolicyPREMI%>" readonly="true">
        <c:field name="detailindex" type="string" hidden="true"/>
        <c:listbox name="details">
        <%
        final DTOList details = form.getDetails();
        final InsurancePolicyItemsView item = ((InsurancePolicyItemsView) details.get(index.intValue()));
        
        boolean deletable = item != null && item.isDeletable();
	//boolean deletable = item != null;
        
        boolean not_usetax = item != null && item.getInsItem().isNotUseTax();

        //if(policy.isStatusEndorse() && item != null && !item.isDeletable())
            //deletable = item != null;
        
        %>
        <c:listcol title="" columnClass="header">
            
        </c:listcol>
        <c:listcol title="" columnClass="detail">
            <c:evaluate when="<%=true%>">
                <c:button text="-" event="onDeleteDetail" clientEvent="f.detailindex.value='$index$';"
                          validate="false" defaultRO="true"/>
            </c:evaluate>
        </c:listcol>
        <c:listcol name="InsItem.stDescription" title="{L-ENGITEMS-L}{L-INAITEM-L}"/>
        
        
        <c:listcol title="{L-ENGDESCRIPTION-L}{L-INADESKRIPSI-L}">
            <c:field caption="Description" name="details.[$index$].stDescription" width="200" type="string|255"
                     mandatory="false" readonly="false"/>
        </c:listcol>
        <c:listcol title="RATE">
            <c:field name="details.[$index$].stFlagEntryByRate"
                     clientchangeaction="VM_switchReadOnly('details.[$index$].dbRate',this.checked);VM_switchReadOnly('details.[$index$].dbAmount',!this.checked)"
                     readonly="<%=!deletable%>" type="check" />
            <c:field caption="Rate" name="details.[$index$].dbRate" width="70" type="money16.6" mandatory="false"
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
                <c:field name="details.[$index$].stEntityID" hidden="true" type="string" />
                <c:field caption="Description" name="details.[$index$].stEntityName" width="210" type="string|255"
                         mandatory="false" readonly="true" clientchangeaction="f.detailindex.value='$index$'; setTaxCode();" />
                <c:evaluate when="<%=deletable%>">
                <c:button text="..." clientEvent="selectAgent($index$)" defaultRO="true"/>
                </c:evaluate>
            </c:evaluate>
        </c:listcol>
        <c:listcol title="NO REKENING" align="center">
            <c:evaluate when="<%=item!=null && item.isComission()%>">
                <c:field caption="No Rekening" name="details.[$index$].stNoRekening" width="150" type="string"
                          mandatory="false" readonly="true" />
            </c:evaluate>
        </c:listcol>
        <%--
        <c:listcol title="Akumulasi Sebelumnya" align="right">
            <c:field caption="Amount" name="details.[$index$].dbTotalAmount" type="money16.2" mandatory="false"
                     readonly="true" width="150"/>
        </c:listcol>
        <c:listcol title="Akumulasi Hingga Bulan Ini" align="right">
            <c:field caption="Amount" name="details.[$index$].dbTotalAmountUntilThisMonth" type="money16.2" mandatory="false"
                     readonly="true" width="150"/>
        </c:listcol>
        --%>
        
    <c:evaluate when="<%=item!=null && item.isComission() && !not_usetax%>">
    </tr>
    <tr class=row2>
    <td class=detail></td>
    <c:listcol title="ITEMS">{L-INAPAJAK-L}{L-ENGTAX-L}</c:listcol>
    <c:listcol title="DESCRIPTION">
        <c:field lov="LOV_ARTax" caption="TAX" width="200" name="details.[$index$].stTaxCode" type="string"
                 mandatory="true" readonly="<%=!deletable%>">
        </c:field>
    </c:listcol>
     <%
        final boolean readonlyTax = true; //!policy.getStCostCenterCode().equalsIgnoreCase("20");
        
      %>
    <c:listcol title="RATE">
        <c:field name="details.[$index$].stTaxAutoRateFlag"
                 clientchangeaction="VM_switchReadOnly('details.[$index$].dbTaxRatePct',this.checked);"
                 type="check" readonly="<%=readonlyTax%>" />
        <c:field caption="Rate" name="details.[$index$].dbTaxRatePct" width="70" type="money16.3"
                 mandatory="false" readonly="<%=readonlyTax%>"/> %
    </c:listcol>
    <c:listcol title="AMOUNT2">
        <c:field name="details.[$index$].stTaxAutoAmountFlag"
                 clientchangeaction="VM_switchReadOnly('details.[$index$].dbTaxAmount',this.checked);"
                 type="check" readonly="false"/>
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
            NPWP <c:field caption="NPWP" name="details.[$index$].stNPWP" width="180" type="string"
                          mandatory="false" readonly="<%=readonlyTax%>" /><br>
            
        </c:evaluate>
    </c:listcol>
    <c:listcol title=""/>
    <%--
    <c:listcol title=""/>
    <c:listcol title=""/>
    --%>
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
        
        if (it.isFee() && !it.isPPN() && !it.isPPNFeeBase() && !it.isPPNComission()) {

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
        
        if (it.isComission() || it.isPPN() || it.isPPNFeeBase() || it.isPPNComission()) {
        %>
        <tr>
            <td><%=it.getInsItem().getStDescription()%>
            </td>
            <td>:</td>
            <td align=right><%=jspUtil.print(it.getDbNetAmount(), 2)%>
            </td>
            <td><%=jspUtil.print(it.getStGLAccount()) + " - " + jspUtil.print(it.getStGLAccountDesc())%>
            </td>
        </tr>
        <%if(it.getStTaxCode()!=null){%>
        <tr>
            <td><%=it.getInsItem().getStDescription()%>({L-ENGTax-L}{L-INAPajak-L})</td>
            <td>:</td>
            <td align=right><%=jspUtil.print(it.getDbTaxAmount(), 2)%>
            </td>
            <td><%=jspUtil.print(it.getStTaxGLAccount()) + " - " + jspUtil.print(it.getStTaxGLAccountDesc())%>
            </td>
        </tr>
        <%
           }
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
                    readonly="<%=!isInputPaymentDateMode%>" type="date" overrideRO="true" />
                    <c:button show="true" text="Check" event="validateHistoryPolicy"/>
                </td>
            </tr>
            <c:field width="500" rows="3" caption="{L-ENGNotes-L}{L-INACatatan-L}" name="policy.stPaymentNotes" type="string"
                     readonly="<%=!isInputPaymentDateMode%>" overrideRO="true" presentation="standard"/>
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
                    <c:listbox name="policy.statusBayarCareList">
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

    <c:evaluate when="<%=isPolisKhusus%>">
        <c:tabpage name="TAB_TITIPAN">

            <table cellpadding=2 cellspacing=1>
               <tr>
                    <td>No Bukti Realisasi</td>
                    <td>:</td>
                    <td>
                        <c:field caption="Nomor Bukti Realisasi" name="policy.stReceiptNoTitipan"
                         width="200" type="string" readonly="false" mandatory="false" />
                        <c:button show="true" text="+" event="selectRealisasiTitipanPremi"/>
                        &nbsp;<c:button show="true" text="Delete All" event="doDeleteAllTitipan" confirm="Yakin dihapus semua ?"/>
                    </td>
                </tr>
            </table>
            <br><br>
            <table cellpadding=2 cellspacing=1 class=header>
                <tr>
                    <td align=center> Titipan Premi Khusus</td>
                </tr>
                <tr>
                    <td>
                        <c:field name="titipanIndex" type="string" hidden="true"/>
                        <c:listbox name="titipan" paging="true">
                            <c:listcol title="" columnClass="header">

                            </c:listcol>
                            <c:listcol title="" columnClass="detail">
                                <c:button text="-" event="onDeleteTitipanPolisKhusus" clientEvent="f.titipanIndex.value='$index$';"
                                          validate="false" defaultRO="true"/>
                            </c:listcol>
                            <c:listcol title=""><%=index.intValue() + 1%>
                            </c:listcol>
                            <c:listcol title="No Bukti">
                                <c:field name="titipan.[$index$].stTransactionNo" type="string" width="150" mandatory="false" readonly="true"/>
                            </c:listcol>
                            <c:listcol title="Counter">
                                <c:field name="titipan.[$index$].stCounter" type="string" width="50"  mandatory="false" readonly="true"/>
                            </c:listcol>
                             <c:listcol title="Keterangan">
                                <c:field name="titipan.[$index$].stDescription" type="string" width="300" mandatory="false" readonly="true"/>
                            </c:listcol>
                            <c:listcol title="{L-ENGAmount-L}{L-INANilai Realisasi-L}">
                                <c:field name="titipan.[$index$].dbRealisasiAmount" type="money16.2" mandatory="false"  width="130"
                                         readonly="false"/>
                            </c:listcol>
                        </c:listbox>
                    </td>
                </tr>
                <tr>
                    <td align=right>
                        {L-ENGTotal-L}{L-INATotal-L}
                        : <%=jspUtil.print(policy.getDbRealisasiAmountTotal(), 2)%>
                    </td>
                </tr>
            </table>

            <br>
            <br>
        </c:tabpage>
    </c:evaluate>
    
    
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
                        <%
                            final InsurancePolicyDocumentView doc = (InsurancePolicyDocumentView) current;

                            boolean showPersetujuanDireksi = true;

                            if(doc != null && doc.getStInsuranceDocumentTypeID().equalsIgnoreCase("94") && !headOfficeUser)
                                showPersetujuanDireksi = false;
                        %>
                        
                        <c:listcol title="">
                            <c:field name="policy.policyDocuments.[$index$].stSelectedFlag" type="check"
                                     mandatory="false" readonly="false" show="<%=showPersetujuanDireksi%>"/>
                        </c:listcol>
                        <c:listcol name="stDescription" title="DESCRIPTION">
                        </c:listcol>
                       
                        <c:listcol title="Document">
                            <c:field name="policy.policyDocuments.[$index$].stFilePhysic" type="file"
                                     thumbnail="true" caption="File" show="<%=showPersetujuanDireksi%>" />
                        </c:listcol>
                        <c:listcol title="Nomor">
                            <c:field name="policy.policyDocuments.[$index$].stDocumentNumber" type="string" width="210"
                                     caption="Nomor" show="true" />
                        </c:listcol>
                        <c:listcol title="Tanggal">
                            <c:field name="policy.policyDocuments.[$index$].dtDocumentDate" type="date"
                                     caption="Tanggal" show="true" />
                        </c:listcol>
                    </c:listbox>
                </td>
            </tr>
        </table>
        </c:fieldcontrol>
    </c:tabpage>

    <c:evaluate when="<%=hasCoIns%>">
        
            <c:tabpage name="TAB_COINS">
                
               
                    <c:field name="coinsIndex" type="string" hidden="true"/>
                    <c:listbox name="coins">
                        <%
                        final InsurancePolicyCoinsView ccoins = (InsurancePolicyCoinsView) current;
                        
                        final boolean holdingComp = ccoins != null && ccoins.isHoldingCompany();
                        %>
                        <c:listcol title="" columnClass="header">
                            <c:button text="+" event="onNewCoinsurance" validate="false" enabled="<%=canChangeCoinsMember%>"
                                      />
                        </c:listcol>
                        <c:listcol title="" columnClass="detail">
                            <c:button text="-" event="onDeleteCoinsurance"
                                      enabled="<%=canChangeCoinsMember && !holdingComp%>"
                                      clientEvent="f.coinsIndex.value='$index$';" validate="false" />
                        </c:listcol>
                        <c:listcol title="{L-ENGCompany-L}{L-INAPerusahaan-L}">
                            <%--<c:field name="coins.[$index$].stEntityID" hidden="true" type="string" />--%>
                            <c:field caption="{L-ENGDescription-L}{L-INADeskripsi-L}" lov="LOV_Entity"
                                     popuplov="true" name="coins.[$index$].stEntityID" type="string|64" mandatory="true"
                                     readonly="<%=!(canChangeCoinsMember && !holdingComp)%>" width="200"/>
                            <%--<c:button text="..." clientEvent="selectCoIns($index$)" />--%>
                        </c:listcol>
                        <c:listcol title="{L-ENGPosition-L}{L-INAPosisi-L}">
                            <c:field lov="VS_COINS_POS" caption="Position" name="coins.[$index$].stPositionCode"
                                     type="string" mandatory="true" readonly="true"/>
                        </c:listcol>
                        <c:listcol title="Share">
                            <%--
                        <c:field name="coins.[$index$].stFlagEntryByRate" clientchangeaction="VM_switchReadOnly('coins.[$index$].dbSharePct',this.checked);VM_switchReadOnly('coins.[$index$].dbAmount',!this.checked)" type="check" readonly="<%=!canChangeCoinsMember || holdingComp%>" /> %
                        --%>
                            <%--
                        <c:field name="coins.[$index$].stFlagEntryByRate" clientchangeaction="VM_switchReadOnly('coins.[$index$].dbSharePct',this.checked);VM_switchReadOnly('coins.[$index$].dbAmount',!this.checked)" type="check" readonly="false" /> %
                        --%>
                            <c:field name="coins.[$index$].stFlagEntryByRate"
                                     clientchangeaction="VM_switchReadOnly('coins.[$index$].dbSharePct',this.checked)"
                                     type="check" readonly="false"/> 
                            <c:field caption="SharePercentage" name="coins.[$index$].dbSharePct" width="40" type="money16.2"
                                     mandatory="false" readonly="<%=holdingComp%>"/>%
                        </c:listcol>
                        <c:listcol title="{L-ENGAmount-L}{L-INAJumlah-L}">
                            <c:field caption="Amount" name="coins.[$index$].dbAmount" width="120" type="money16.2"
                                     mandatory="false" readonly="<%=!canChangeCoinsMember || holdingComp%>"/>
                        </c:listcol>
                        <c:listcol title="{L-ENGAuto-L}{L-INAAuto-L}">
                            <c:field name="coins.[$index$].stAutoPremiFlag"
                                     clientchangeaction="VM_switchReadOnly('coins.[$index$].dbPremiAmount',this.checked);"
                                     type="check" readonly="false"/>
                        </c:listcol>   
                         <c:listcol title="{L-ENGManual-L}{L-INAManual-L}">
                            <c:field name="coins.[$index$].stManualPremiFlag"
                                     type="check" readonly="false"/> 
                        </c:listcol> 
                        <c:listcol title="{L-ENGPremium-L}{L-INAPremi-L}">
                            <%--
                        <c:field name="coins.[$index$].stAutoPremiFlag" clientchangeaction="VM_switchReadOnly('coins.[$index$].dbPremiAmount',this.checked);" type="check" readonly="<%=!canChangeCoinsMember || holdingComp%>" /> %

                        <c:field caption="Amount" name="coins.[$index$].dbPremiAmount" type="money16.2" mandatory="false" readonly="true" />
                        <c:evaluate when="<%=!holdingComp%>" >
                        <script>docEl('coins.[<%=index%>].stFlagEntryByRate').onclick();</script>
                        </c:evaluate>
                        --%>
                            
                            
                            <c:field caption="Amount" name="coins.[$index$].dbPremiAmount" type="money16.2"
                                     mandatory="false" readonly="false"/>
                            
                        </c:listcol>
                        <c:listcol title="{L-ENGDiscount(%)-L}{L-INADiskon(%)-L}">
                            <c:field caption="Discount Rate" width="40" name="coins.[$index$].dbDiscountRate"
                                     type="money16.2" mandatory="false"
                                     readonly="<%=!canChangeCoinsMember%>"/>%
                        </c:listcol>
                        <c:listcol title="{L-ENGDiscount<br>Amt-L}{L-INAJumlah<br>Diskon-L}">
                            <c:field caption="Discount Fee" width="100" name="coins.[$index$].dbDiscountAmount"
                                     type="money16.2" mandatory="false" readonly="false"/>
                        </c:listcol>
                        <c:listcol title="{L-ENGComm(%)-L}{L-INAKomisi(%)-L}">
                            <c:field caption="Commission Rate" width="40" name="coins.[$index$].dbCommissionRate"
                                     type="money16.2" mandatory="false"
                                     readonly="<%=!canChangeCoinsMember%>"/>%
                        </c:listcol>
                        <c:listcol title="{L-ENGComm<br>Amt-L}{L-INAJumlah<br>Komisi-L}">
                            <c:field caption="Commission" width="100" name="coins.[$index$].dbCommissionAmount"
                                     type="money16.2" mandatory="false" readonly="false"/>
                        </c:listcol>
                        
                        <c:listcol title="{L-ENGBrokerage(%)-L}{L-INABrokerage(%)-L}">
                            <c:field caption="Brokerage Rate" width="40" name="coins.[$index$].dbBrokerageRate"
                                     type="money16.2" mandatory="false"
                                     readonly="<%=!canChangeCoinsMember%>"/>%
                        </c:listcol>
                        <c:listcol title="{L-ENGBroker<br>Amt-L}{L-INAJumlah<br>Brokerage-L}">
                            <c:field caption="Brokerage Fee" width="100" name="coins.[$index$].dbBrokerageAmount"
                                     type="money16.2" mandatory="false" readonly="false"/>
                        </c:listcol>
                        <c:listcol title="{L-ENGHFee(%)-L}{L-INAHFee(%)-L}">
                            <c:field caption="Handling Fee Rate" width="40" name="coins.[$index$].dbHandlingFeeRate"
                                     type="money16.2" mandatory="false"
                                     readonly="<%=!canChangeCoinsMember%>"/>%
                        </c:listcol>
                        <c:listcol title="{L-ENGHFee<br>Amt-L}{L-INAJumlah<br>HFee-L}">
                            <c:field caption="Handling Fee" width="100" name="coins.[$index$].dbHandlingFeeAmount"
                                     type="money16.2" mandatory="false" readonly="false"/>
                        </c:listcol>
                        
                    </c:listbox>           
            </c:tabpage>
    </c:evaluate>
    
    <c:evaluate when="<%=hasCoIns%>">
        <c:evaluate when="<%=canSeeCoins%>">
            <c:tabpage name="TAB_COINS_COVER">
                    <c:field name="coinsCoverIndex" type="string" hidden="true"/>
                    <c:listbox name="coinsCoverage">
                        <%
                        final InsurancePolicyCoinsView ccoins = (InsurancePolicyCoinsView) current;
                        
                        final boolean holdingComp = ccoins != null && ccoins.isHoldingCompany();
                        %>
                        <c:listcol title="" columnClass="header">
                            <c:button text="+" event="onNewCoinsuranceCoverage" validate="false" enabled="<%=canChangeCoinsMember%>"/>
                        </c:listcol>
                        <c:listcol title="" columnClass="detail">
                            <c:button text="-" event="onDeleteCoinsuranceCoverage"
                                      enabled="<%=canChangeCoinsMember && !holdingComp%>"
                                      clientEvent="f.coinsCoverIndex.value='$index$';" validate="false" />
                        </c:listcol>
                        <c:listcol title="{L-ENGCompany-L}{L-INAPerusahaan-L}">
                            <%--<c:field name="coins.[$index$].stEntityID" hidden="true" type="string" />--%>
                            <c:field caption="{L-ENGDescription-L}{L-INADeskripsi-L}" lov="LOV_InsuranceCompany"
                                     popuplov="true" name="coinsCoverage.[$index$].stEntityID" type="string|64" mandatory="true"
                                     readonly="<%=!(canChangeCoinsMember && !holdingComp)%>" width="200"/>
                            <%--<c:button text="..." clientEvent="selectCoIns($index$)" />--%>
                        </c:listcol>
                        <c:listcol title="{L-ENGPosition-L}{L-INAPosisi-L}">
                            <c:field lov="VS_COINS_POS" caption="Position" name="coinsCoverage.[$index$].stPositionCode"
                                     type="string" mandatory="true" readonly="true"/>
                        </c:listcol>
                        <c:listcol title="Share">
                            <%--
                        <c:field name="coins.[$index$].stFlagEntryByRate" clientchangeaction="VM_switchReadOnly('coins.[$index$].dbSharePct',this.checked);VM_switchReadOnly('coins.[$index$].dbAmount',!this.checked)" type="check" readonly="<%=!canChangeCoinsMember || holdingComp%>" /> %
                        --%>
                            <%--
                        <c:field name="coins.[$index$].stFlagEntryByRate" clientchangeaction="VM_switchReadOnly('coins.[$index$].dbSharePct',this.checked);VM_switchReadOnly('coins.[$index$].dbAmount',!this.checked)" type="check" readonly="false" /> %
                        --%>
                            <c:field name="coinsCoverage.[$index$].stFlagEntryByRate"
                                     clientchangeaction="VM_switchReadOnly('coinsCoverage.[$index$].dbSharePct',this.checked)"
                                     type="check" readonly="false"/> 
                            <c:field caption="SharePercentage" name="coinsCoverage.[$index$].dbSharePct" width="40" type="money16.2"
                                     mandatory="false" readonly="<%=holdingComp%>"/>%
                        </c:listcol>
                        <c:listcol title="{L-ENGAmount-L}{L-INAJumlah-L}">
                            <c:field caption="Amount" name="coinsCoverage.[$index$].dbAmount" width="120" type="money16.2"
                                     mandatory="false" readonly="<%=!canChangeCoinsMember || holdingComp%>"/>
                        </c:listcol>
                        <c:listcol title="{L-ENGPremium-L}{L-INAPremi-L}">
                            <%--
                        <c:field name="coins.[$index$].stAutoPremiFlag" clientchangeaction="VM_switchReadOnly('coins.[$index$].dbPremiAmount',this.checked);" type="check" readonly="<%=!canChangeCoinsMember || holdingComp%>" /> %

                        <c:field caption="Amount" name="coins.[$index$].dbPremiAmount" type="money16.2" mandatory="false" readonly="true" />
                        <c:evaluate when="<%=!holdingComp%>" >
                        <script>docEl('coins.[<%=index%>].stFlagEntryByRate').onclick();</script>
                        </c:evaluate>
                        --%>
                            <c:field name="coinsCoverage.[$index$].stAutoPremiFlag"
                                     clientchangeaction="VM_switchReadOnly('coinsCoverage.[$index$].dbPremiAmount',this.checked);"
                                     type="check" readonly="<%=holdingComp%>"/> %
                            
                            <c:field caption="Amount" name="coinsCoverage.[$index$].dbPremiAmount" type="money16.2"
                                     mandatory="false" readonly="true"/>
                            
                        </c:listcol>
                        <c:listcol title="{L-ENGDiscount<br>Rate-L}{L-INARate<br>Diskon-L}">
                            <c:field caption="Discount Rate" width="40" name="coinsCoverage.[$index$].dbDiscountRate"
                                     type="money16.2" mandatory="false"
                                     readonly="<%=!canChangeCoinsMember ||holdingComp%>"/>%
                        </c:listcol>
                        <c:listcol title="{L-ENGDiscount<br>Amt-L}{L-INAJumlah<br>Diskon-L}">
                            <c:field caption="Discount Fee" width="100" name="coinsCoverage.[$index$].dbDiscountAmount"
                                     type="money16.2" mandatory="false" readonly="true"/>
                        </c:listcol>
                        <c:listcol title="{L-ENGComm<br>Rate-L}{L-INARate<br>Komisi-L}">
                            <c:field caption="Commission Rate" width="40" name="coinsCoverage.[$index$].dbCommissionRate"
                                     type="money16.2" mandatory="false"
                                     readonly="<%=!canChangeCoinsMember ||holdingComp%>"/>%
                        </c:listcol>
                        <c:listcol title="{L-ENGComm<br>Amt-L}{L-INAJumlah<br>Komisi-L}">
                            <c:field caption="Commission" width="100" name="coinsCoverage.[$index$].dbCommissionAmount"
                                     type="money16.2" mandatory="false" readonly="true"/>
                        </c:listcol>
                        
                        <c:listcol title="{L-ENGBrokerage<br>Rate-L}{L-INARate<br>Brokerage-L}">
                            <c:field caption="Brokerage Rate" width="40" name="coinsCoverage.[$index$].dbBrokerageRate"
                                     type="money16.2" mandatory="false"
                                     readonly="<%=!canChangeCoinsMember ||holdingComp%>"/>%
                        </c:listcol>
                        <c:listcol title="{L-ENGBroker<br>Amt-L}{L-INAJumlah<br>Brokerage-L}">
                            <c:field caption="Brokerage Fee" width="100" name="coinsCoverage.[$index$].dbBrokerageAmount"
                                     type="money16.2" mandatory="false" readonly="true"/>
                        </c:listcol>
                        <c:listcol title="{L-ENGHFee<br>Rate-L}{L-INARate<br>HFee-L}">
                            <c:field caption="Handling Fee Rate" width="40" name="coinsCoverage.[$index$].dbHandlingFeeRate"
                                     type="money16.2" mandatory="false"
                                     readonly="<%=!canChangeCoinsMember ||holdingComp%>"/>%
                        </c:listcol>
                        <c:listcol title="{L-ENGHFee<br>Amt-L}{L-INAJumlah<br>HFee-L}">
                            <c:field caption="Handling Fee" width="100" name="coinsCoverage.[$index$].dbHandlingFeeAmount"
                                     type="money16.2" mandatory="false" readonly="true"/>
                        </c:listcol>
                        
                    </c:listbox>
            </c:tabpage>
        </c:evaluate>
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
                                   <c:field name="policy.stCacCbcFlag" type="string" width="150"
                                        lov="VS_CAC_CBC_TYPE" caption="Tipe Transaksi"
                                        readonly="false" presentation="standard"/>
                                   <c:field name="policy.stDataSourceID" type="string" width="150"
                                         lov="LOV_DataSourceType" caption="Sumber Data"
                                         readonly="false" presentation="standard"/>

                                   <c:evaluate when="<%=headOfficeUser%>">
                                       <c:field name="policy.stManualReinsuranceFlag" type="check" caption="Manual R/I Spreading" presentation="standard" />
                                       <c:field name="policy.stCheckingFlag" type="check" caption="Bypass Validasi" presentation="standard" />
                                   </c:evaluate>
                                   
                               </table>
                            </td>
                         </tr>

                      </table>
                      <br>
                      <table cellpadding=2 cellspacing=1 class=header>
                        <tr>
                            <td align=center> Log History </td>
                        </tr>
                        <tr>
                            <td>
                                <c:listbox name="reverse">
                                    <c:listcol title=""><%=index.intValue() + 1%>
                                    </c:listcol>
                                    <c:listcol title="User ID" name="stReverseWho"/>
                                    <c:listcol title="User Name" name="stReverseName"/>
                                    <c:listcol title="Tanggal" name="stReverseTime"/>
                                    <c:listcol title="Catatan" name="stReverseNotes"/>
                                </c:listbox>
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
        <c:button  show="<%=!viewMode && form.isCanApproveHeadOffice()%>" text="Persetujuan Pusat" event="setujuiPusatAllObject" defaultRO="true" />

        <c:button show="<%=!viewMode&&!form.isReApprovedMode()&&!form.isInputPaymentDateMode() && !form.isEditKeteranganMode() && !form.isReverseMode()%>"
                  text="{L-ENGRecalculate-L}{L-INAHitung Ulang-L}" event="btnRecalculate" validate="true" />
        <c:button show="<%=!viewMode&&!form.isReApprovedMode()&&!form.isReverseMode()&&!form.isSavePolicyHistoryMode()%>"
                  text="{L-ENGSave-L}{L-INASimpan-L}" event="btnSave"
                  confirm="Yakin mau disimpan ?" validate="true" />

        <c:button show="<%=viewMode && form.isBentukPolisMode()%>"
                  text="{L-ENGSave-L}{L-INASimpan-L}" event="btnSave"
                  confirm="Yakin mau disimpan ?" validate="true" />
        
        <%if(form.isInputPaymentDateMode()){%>
            <c:button show="true" text="{L-ENGSave Payment Date-L}{L-INASimpan-L}" event="btnSave"/>
        <%}%>

        <c:button show="<%=form.isSavePolicyHistoryMode()%>"
                  text="{L-ENGSave Policy History-L}{L-INASimpan Polis History-L}" event="btnSavePolicyHistory" validate="true"
                  confirm="Yakin mau disimpan ?"/>
        <c:button show="<%=!viewMode%>" text="{L-ENGCancel-L}{L-INABatal-L}" event="btnCancel" validate="false"
                  confirm="Yakin Mau Dibatalkan ?"/>
        <c:evaluate
            when="<%=!effective && form.isApprovalMode() && !form.isReverseMode() && !form.isRejectMode()%>">
                <c:evaluate when="<%=form.getPolicy().isStatusPolicy() || form.getPolicy().isStatusRenewal() || form.getPolicy().isStatusClaimDLA() || policy.isStatusEndorse() || showPersetujuanPrinsipNo%>">
                    <table class=row0>
                        <tr class="header"><td colspan="3" align="center"><b>Password Validation</b></td></tr>
                    <%if(policy.getStPolicyNo()!=null){
                        {%>
                        <tr><td><b>Nomor Polis</b></td><td>:</td><td><font style="font-weight:bold; font-size:14px;"><%= policy.getStPolicyNo()%></font></td></tr>
                    <%}}%>
                        <tr><td><b>Password</b></td><td>:</td><td><c:field  width="200" caption="Password" name="policy.stPassword" type="password" mandatory="true" overrideRO="true" readonly="false"/></td></tr>
                    </table><br><br>
                </c:evaluate>
                
                <c:button show="true" text="{L-ENGApprove-L}{L-INASetujui-L}" event="btnApprove" validate="true"
                      confirm="Yakin Mau Disetujui ?"/>
               <%-- <c:button show="true" text="{L-ENGApprove TRIAL-L}{L-INASetujui TRIAL-L}" event="btnApproveTrial" validate="true"
                    confirm="Yakin Mau Disetujui ?"/>--%>

        </c:evaluate>
        <c:evaluate when="<%=!effective && form.isRiskApprovalMode()%>">
            <c:button show="true" text="{L-ENGApprove Risk-L}{L-INASetujui Analisa Resiko-L}" event="btnApproveRisk" validate="true"
              confirm="Yakin Mau Setujui Analisa Resiko ?"/>
        </c:evaluate>
        
        

        <c:evaluate
            when="<%=!effective && form.isApprovalMode()&&!form.isReverseMode()&&!form.isReApprovedMode()&&!form.isApprovedViaReverseMode()&&form.isRejectMode()%>">
            <c:button show="true" text="Tolak" enabled="<%=rejectable%>" event="btnReject"
                      validate="true" confirm="Yakin ingin di tolak ?"/>
             <%--<c:button show="<%=policy.getStParentID()!=null%>" text="Tolak" enabled="<%=rejectable%>" event="btnTolak"
                      validate="true" confirm="Yakin ingin ditolak ?"/>--%>
        </c:evaluate>
        <c:button show="<%=viewMode%>" text="{L-ENGClose-L}{L-INATutup-L}" event="btnCancel" validate="false"/>
        <c:button show="<%=form.isReverseMode()%>" text="Reverse" event="btnReverseJurnalBalikOnly"/>
        <c:button show="<%=form.isReApprovedMode()%>" text="ReApprove" event="btnReApprove" confirm="Yakin?Reas Juga di Reset nih" />
        <c:button show="<%=form.isApprovedViaReverseMode()%>"
                  text="{L-ENGApprove(Reverse)-L}{L-INASetujui(Reverse)-L}" event="btnApproveViaReverse"/>
        <c:button show="<%=cROReas&&form.isApprovedReasMode()%>" text="{L-ENGApprove-L}{L-INASetujui-L}" event="btnApproveReins"
                  validate="true" confirm="Yakin Mau Setujui ?"/>
        <c:button show="<%=form.isEnableSwapPremiORKoas()%>" text="Swap Premi Koas > Premi OR" event="adjustPremiORWithKoas" confirm="Yakin ingin set premi OR dengan premi Koas?" validate="false"/>

    </td>
</tr>
<tr>
</tr>
</c:evaluate>
</table>
</c:frame>
<iframe src="" id=frmx width=1 height=1></iframe>
<script>

    var frmx = docEl('frmx');

    function downloadEPolisPAJ() {

        var nopolis = document.getElementById('selectedObject.stReference30').value;
        var norek = document.getElementById('selectedObject.stReference16').value;
        var entid = document.getElementById('policy.stEntityID').value;

        if(true){
            frmx.src='x.fpc?EVENT=INS_POLIS_JIWA_ONE&nopolis='+nopolis+'&norek='+norek+'&entid='+entid;
            return;
        }

    }

    function selectAgent(i) {
        openDialog('so.ctl?EVENT=INS_AGENT_SELECT', 400, 400,
                function (o) {
                    if (o != null) {
                        document.getElementById('details.[' + i + '].stEntityID').value = o.entid;
                        document.getElementById('details.[' + i + '].stEntityName').value = o.entname;
                        document.getElementById('details.[' + i + '].stNPWP').value = o.npwp;
                    }
                }
                );
    }
     
    function setTaxCode() {
        f.action_event.value = 'setTaxCode';
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
        var status =  o.customer_status;
        if(status=='BLACK LIST'){
            alert('Principal Di Black List');
            document.getElementById('selectedObject.stReference3').value = '';
            document.getElementById('selectedObject.stReference4').value = '';
            document.getElementById('selectedObject.stReference5').value = '';
            document.getElementById('selectedObject.stReference1').value = '';
            document.getElementById('selectedObject.stReference2').value = '';
            document.getElementById('selectedObject.stReference1Desc').value = '';
            return;
        }
            document.getElementById('selectedObject.stReference3').value = o.address;
            document.getElementById('selectedObject.stReference4').value = o.functionary_name;
            document.getElementById('selectedObject.stReference5').value = o.functionary_position;
            document.getElementById('selectedObject.stReference1').value = o.value;
    }
    
    function selectPrincipalBG() {
        var o = window.lovPopResult;
        var status =  o.customer_status;
        if(status=='BLACK LIST'){
            alert('Principal Di Black List');
            document.getElementById('selectedObject.stReference3').value = '';
            //document.getElementById('selectedObject.stReference4').value = '';
            //document.getElementById('selectedObject.stReference5').value = '';
            document.getElementById('selectedObject.stReference1').value = '';
            document.getElementById('selectedObject.stReference2').value = '';
            document.getElementById('selectedObject.stReference1Desc').value = '';
            return;
        }
            document.getElementById('selectedObject.stReference3').value = o.address;
            //document.getElementById('selectedObject.stReference4').value = o.functionary_name;
            //document.getElementById('selectedObject.stReference5').value = o.functionary_position;
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
    
    function getPolicyHistory() {
        f.action_event.value = 'changeParentPolicy';
        f.submit();
    }
    
    function selectZone() {
        var o = window.lovPopResult;
        var code = o.ref1; 
        var zone;
        
        if(code=='1') zone = 'ZONE1';
        else if(code=='2') zone = 'ZONE2';
        else if(code=='3') zone = 'ZONE3';
        else if(code=='4') zone = 'ZONE4';
        else if(code=='5') zone = 'ZONE5';

        document.getElementById('selectedObject.stReference6').value = zone;
        
    }
    
    function getPolicyHistory() {
        f.action_event.value = 'changeParentPolicy';
        f.submit();
    }
    
    function setFeeBase() {
        f.action_event.value = 'addFeeBaseAutomatically';
        f.submit();
    }

    function selectTertanggung() {
        var o = window.lovPopResult;
        document.getElementById('selectedObject.stReference3').value = o.address;
        document.getElementById('selectedObject.stReference4').value = o.functionary_name;
        document.getElementById('selectedObject.stReference5').value = o.functionary_position;
    }

    function selectPenanggung() {
        var o = window.lovPopResult;
        document.getElementById('selectedObject.stReference8').value = o.address;
        document.getElementById('selectedObject.stReference9').value = o.functionary_name;
        document.getElementById('selectedObject.stReference10').value = o.functionary_position;
    }

    function selectDebitur() {
        var o = window.lovPopResult;
        document.getElementById('selectedObject.stReference15').value = o.address;
        document.getElementById('selectedObject.stReference16').value = o.functionary_name;
        document.getElementById('selectedObject.stReference17').value = o.functionary_position;
    }

    <%--document.body.onscroll=function() {alert(document.body.scrollTop);};--%>
    <%--document.body.onload=function() {document.body.scrollTop=200};--%>
</script>
