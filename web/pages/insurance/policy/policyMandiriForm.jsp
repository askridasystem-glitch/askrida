<%@ page import="com.webfin.insurance.form.PolicyMandiriForm,
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
final PolicyMandiriForm form = (PolicyMandiriForm) frame.getForm();

final InsurancePolicyView policy = form.getPolicy();

final InsurancePolicyObjectView selectedObject = form.getSelectedObject();

final boolean canCreatePolicyHistory = form.isCanCreatePolicyHistory();

final boolean enableNoPolicy = form.isEnableNoPolicy();

final boolean ismasterCurrency = CurrencyManager.getInstance().isMasterCurrency(policy.getStCurrencyCode());
final boolean hasPolicyType = policy.getStPolicyTypeID() != null;
final boolean hasConditions = policy.getStConditionID() != null;
final boolean hasRiskCategory = policy.getStRiskCategoryID() != null;
final boolean isAddPeriodDesc = policy.isAddPeriodDesc();
final boolean isLampiran = policy.isLampiran();

//final boolean phase2 = hasPolicyType && hasConditions ;
final boolean phase2 = hasPolicyType;
final boolean phase1 = !phase2;

final String type_code = policy.getPolicyType() != null?policy.getPolicyType().getStPolicyTypeCode():"";
    
boolean hasCoIns = policy.hasCoIns();
boolean coLeaderPolicy = false;
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
}
   
form.getTabs().enable("TAB_COINS", hasCoIns);
form.getTabs().enable("TAB_COINS_COVER", canSeeCoins);
//form.getTabs().enable("TAB_COVER_REINS", canSeeCoins);

final boolean ro = form.isReadOnly();
boolean viewMode = ro;

boolean statusDraft = FinCodec.PolicyStatus.DRAFT.equalsIgnoreCase(form.getStStatus());
boolean statusPolicy = FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(form.getStStatus());
boolean statusEndorse = FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(form.getStStatus());
boolean statusCancel = FinCodec.PolicyStatus.CANCEL.equalsIgnoreCase(form.getStStatus());
boolean statusSPPA = FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(form.getStStatus());
boolean statusRenewal = FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(form.getStStatus());
boolean statusClaimEndorse = FinCodec.PolicyStatus.ENDORSECLAIM.equalsIgnoreCase(form.getStStatus());
boolean statusHistory = FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(form.getStStatus());
boolean statusTemporary = FinCodec.PolicyStatus.TEMPORARY.equalsIgnoreCase(form.getStStatus());
boolean statusEndorseTemporary = FinCodec.PolicyStatus.ENDORSETEMPORARY.equalsIgnoreCase(form.getStStatus());

final boolean isInputPaymentDateMode = form.isInputPaymentDateMode();
boolean showPersetujuanPrinsipNo = policy.getStReference1()!=null && (statusSPPA || statusPolicy) && type_code.equalsIgnoreCase("OM_BG")? true:false;
final boolean showPolicyNo = policy.getStPolicyNo()!=null || (!statusDraft && !statusSPPA && canCreatePolicyHistory)? true:false;
final boolean effective = policy.isEffective();
final boolean posted = policy.isPosted();
final boolean canChangeCoverType = (statusDraft || statusSPPA || statusPolicy ||statusHistory || statusTemporary) && !posted && (policy.getStCoverTypeCode() == null);

boolean canChangeCoinsMember = (statusDraft || statusSPPA || statusPolicy ||statusHistory || statusEndorse || statusTemporary || statusEndorseTemporary) && !posted;
boolean showCoinsurance = (statusHistory || statusEndorse || statusTemporary || statusEndorseTemporary);

final boolean rejectable = form.isRejectable();
final boolean isMember = policy.isMember();

boolean cROpolicyHeader = false;
boolean cROpolicyRiskDetail = false;
boolean cROpolicyClausules = false;
boolean cROpolicyPREMI = false;
boolean cROpolicyCOINS = false;
boolean cROpolicyINSTALLMENT = false;
boolean cROendorsePeriodFactor = false;
boolean cROendorseObject = false;
boolean cROpolicySumInsured = false;
boolean cROpolicyCoverage = false;
boolean cROendorseDescription = false;

final boolean cROReas = form.isReasMode();

final boolean isTotalEndorseMode = form.isTotalEndorseMode();
final boolean isPartialEndorseTSIMode = form.isPartialEndorseTSIMode();
final boolean isPartialEndorseRateMode = form.isPartialEndorseRateMode();
final boolean isDescriptionEndorseMode = form.isDescriptionEndorseMode();
final boolean isRestitutionEndorseMode = form.isRestitutionEndorseMode();
boolean isEndorseMode = (isTotalEndorseMode || isPartialEndorseTSIMode || isPartialEndorseRateMode || isDescriptionEndorseMode || isRestitutionEndorseMode);

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
    if(policy.getStPolicyTypeID().equalsIgnoreCase("21")) cROendorseObject = true;
}else if(isDescriptionEndorseMode){
    cROendorseObject = true;
    cROendorseDescription = true;
}else if(isRestitutionEndorseMode){
    cROpolicyRiskDetail = false;
    cROpolicyCoverage = true;
    cROendorsePeriodFactor = true;
    cROendorseObject = true;
    cROpolicyPREMI = false;
}

if (Tools.isYes(policy.getStLockCoinsFlag())) cROpolicyCOINS = true;

final boolean developmentMode = Config.isDevelopmentMode();

String labelEntity = "{L-ENGBussiness Source-L}{L-INASumber Bisnis-L}";
String labelEntity2 = "{L-ENGBussiness Source-L}{L-INASumber Bisnis-L}";
if (type_code.equalsIgnoreCase("OM_BG")){
    labelEntity = "Nama Bank Penerbit";
    labelEntity2 = "Bank Penerbit";
}
    
if (policy.getPolicyType() != null)
    if(policy.getStPolicyTypeGroupID().equalsIgnoreCase("7")){
        labelEntity = "Nama Obligee";
        labelEntity2 = "Obligee";
    }

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
                <c:field width="300" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroupSome"
                         readonly="<%=hasPolicyType%>" caption="{L-ENGPolicy Class-L}{L-INAKelas Polis-L}"
                         name="policy.stPolicyTypeGroupID" type="string" mandatory="true" presentation="standard"/>
                <c:field width="300" include="<%=policy.getStPolicyTypeGroupID()!=null%>"
                         changeaction="onChangePolicyType" lov="LOV_PolicyTypeSome" readonly="<%=hasPolicyType%>"
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
                        
                        <c:field width="200" changeaction="changeBranchMandiri" lov="LOV_CostCenter"
                                 caption="{L-ENGBranch-L}{L-INACabang-L}" name="policy.stCostCenterCode" type="string"
                                 mandatory="true"
                                 readonly="<%=!canChangeCoverType || policy.getStCostCenterCode()!=null%>"
                                 presentation="standard"/>
                        
                        <c:field show="<%=policy.getStCostCenterCode()!=null%>" width="200" lov="LOV_Region"
                                 changeaction="onChangeRegionMandiri" caption="{L-ENGRegion-L}{L-INADaerah-L}"
                                 name="policy.stRegionID" type="string" mandatory="false"
                                 readonly="<%=!canChangeCoverType%>" presentation="standard">
                            <c:lovLink name="cc_code" link="policy.stCostCenterCode" clientLink="false"/>
                        </c:field>
                        
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
                            
     
                            <c:field caption="{L-ENGPolicy Date-L}{L-INATanggal Polis-L}" name="policy.dtPolicyDate" type="date"
                                     mandatory="true" readonly="false" presentation="standard"/>
                            <c:field show="<%=statusEndorse||statusEndorseTemporary%>" caption="{L-ENGChange Date-L}{L-INATanggal Perubahan-L}"
                                     name="policy.dtEndorseDate" type="date" mandatory="true" readonly="false" presentation="standard"/>
                            
                            <c:field width="300" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroupSome" readonly="true"
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
                                             mandatory="true" overrideRO="true" readonly="false"/>
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
                            
                            <c:field width="150" caption="{L-ENGInsured Amount-L}{L-INAHarga Pertanggungan-L}" name="policy.dbInsuredAmount"
                                     type="money16.2" mandatory="false" readonly="true" presentation="standard"/>
                            <c:field show="<%=statusEndorse||statusEndorseTemporary%>" width="150" caption="{L-ENGEndorse Amount-L}{L-INAHarga Endorse-L}"
                                     name="policy.dbInsuredAmountEndorse" type="money16.2" mandatory="false" readonly="true"
                                     presentation="standard"/>
                            <%--<c:field width="150" caption="Premium (BASE)" name="policy.dbPremiBase" type="money16.2" mandatory="false" readonly="true" presentation="standard"/>--%>
                            <c:evaluate when="<%=isMember%>">
                            </c:evaluate>
                            <c:field width="150" caption="{L-ENGPremium (Total)-L}{L-INAPremi Bruto-L}" name="policy.dbPremiTotal"
                                     type="money16.2" mandatory="false" readonly="true" presentation="standard"/>
                            
                        </table>
                    </td>
                    <td>
                        <table cellpadding=2 cellspacing=1>
                           
                            <c:fieldcontrol when="<%=cROendorsePeriodFactor%>" readonly="false">
                            <c:field clientchangeaction="selectCustomer2()" name="policy.stEntityID" type="string" width="200"
                                     popuplov="true" lov="LOV_Entity" mandatory="true"
                                     caption="<%=labelEntity2%>" presentation="standard"/>
                                     
                                         <c:field width="200" rows="3" caption="<%=labelEntity%>"
                                                  name="policy.stCustomerName" type="string|255" mandatory="true" readonly="false"
                                                  presentation="standard"/>
                                         <c:field width="200" rows="3" caption="{L-ENGAddress-L}{L-INAAlamat-L}" name="policy.stCustomerAddress"
                                                  type="string|255" mandatory="true" readonly="false" presentation="standard"/>
                                     
                           
                            </c:fieldcontrol>
                            <c:evaluate when="<%=policy.getSPPAFF()!=null%>">
                                <c:flexfield ffid="<%=policy.getSPPAFF().getStFlexFieldHeaderID()%>" prefix="policy."/>
                            </c:evaluate>
                            <%--<c:field caption="Period Length" name="policy." type="string|64" mandatory="false" readonly="false" presentation="standard"/>--%>
                            <c:field caption="{L-ENGPosted-L}{L-INAJurnal-L}" name="policy.stPostedFlag" type="check" mandatory="false"
                                     overrideRO="true" show="false" readonly="true" presentation="standard"/>
                            
                            <c:evaluate when="<%=policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_KREASI")%>">
                                <c:field clientchangeaction="selectCreditType()" name="policy.stKreasiTypeID" type="string" width="200"
                                         popuplov="true" lov="LOV_TypeOfCredit" 
                                         caption="{L-ENGType Of Credit-L}{L-INAJenis Kredit-L}" mandatory="true" presentation="standard">
                                    <c:lovLink name="cc_code" link="policy.stCostCenterCode" clientLink="false"/>
                                </c:field>
                                <c:field show="false" width="200" caption="{L-ENGKreasi Type Name-L}{L-INANama Jenis Kreasi-L}"
                                         name="policy.stKreasiTypeDesc" type="string|255" readonly="false"
                                         presentation="standard"/>
                            </c:evaluate>
                             <c:evaluate when="<%=policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_KREASI") && showCoinsurance%>">
                                <c:field clientchangeaction="selectReinsKreasi()" name="policy.stCoinsID" type="string" width="200"
                                         popuplov="true" lov="LOV_InsuranceCompany" 
                                         caption="{L-ENGReinsurer-L}{L-INAReasuradur-L}" presentation="standard"/>
                                <c:field show="false" width="200" caption="{L-ENGReinsurer Name-L}{L-INANama Reasuradur-L}"
                                         name="policy.stCoinsName" type="string|255" readonly="false"
                                         presentation="standard"/>
                            </c:evaluate>
             
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
                            <c:evaluate when="<%=form.isEnableSuperEdit()%>">
                                <c:field lov="LOV_User" caption="{L-ENGR/I Approved Who-L}{L-INAR/I Approved Who-L}"
                                         name="policy.stReinsuranceApprovedWho" type="string" mandatory="false" readonly="false"
                                         width="200" presentation="standard" show="<%=cROReas&&form.isApprovedReasMode()%>" popuplov="true" overrideRO="true" />
                            </c:evaluate>
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
               
            </table>
        </c:fieldcontrol>
    </td>
</tr>
<tr>
    <td>
    <c:tab name="tabs">
        
        
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
                                <td>{L-ENGObject-L}{L-INAObjek Pertanggungan-L}</td>
                                <td>:</td>
                                <td>
                                    <c:field width="400" lov="lovObjects" name="stSelectedObject" caption="Selected Object"
                                             type="string" changeaction="selectObject" overrideRO="true" readonly="false"/>
                                    <c:fieldcontrol when="<%=cROpolicyRiskDetail%>" readonly="true">
                                        <c:button text="+" event="doNewObjectMandiri" defaultRO="true"/>
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
                                                       <%-- <c:field lov="LOV_RiskCategory" caption="{L-ENGRisk Code / Category-L}{L-INAKode Resiko / Kategori-L}"
                                                                 name="selectedObject.stRiskCategoryID" type="string" mandatory="true" readonly="false"
                                                                 width="400" presentation="standard" popuplov="true" refresh="true">
                                                            <c:lovLink name="datestart" link="policy.dtPeriodStart" clientLink="false"/>                 
                                                            <c:param name="poltype" value="<%=policy.getStPolicyTypeID()%>"  />
                                                        </c:field>
                                                        --%>
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
                               
                                <c:tabpage name="TAB_SI">
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
                                                        <c:listcol title="<%=labelTSI%>">
                                                            <c:field name="selectedObject.suminsureds.[$index$].stAutoFlag" type="check"
                                                                     clientchangeaction="VM_switchReadOnly('selectedObject.suminsureds.[$index$].dbInsuredAmount',!this.checked);"
                                                                     readonly="<%=manualTSILock%>"/>
                                                            <c:field caption="Amount" name="selectedObject.suminsureds.[$index$].dbInsuredAmount"
                                                                     type="money16.2" mandatory="false" readonly="<%=rotsi%>"/>
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
                                            </c:fieldcontrol>
                                        </c:fieldcontrol>
                                    <c:fieldcontrol when="<%=cROpolicyRiskDetail%>" readonly="true">
                                        <c:fieldcontrol when="<%=cROpolicyCoverage%>" readonly="false">
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
                                                                <%--<c:field caption="<%=policy.getStRateMethodDesc()%>" width="30" name="policy.stRateMethodDesc" type="string" mandatory="false" readonly="true"/>
                        <%=policy.getStRateMethodDesc()%>--%>

                                                                <c:button text="<%=policy.getStRateMethodDesc()%>" event="chgRateClass" size="30"
                                                                          clientEvent="f.covIndex.value=$index$;"/>
                                                                
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
                                                            <c:button text="+" event="onNewObjDeductible" validate="false" defaultRO="true"/>
                                                            
                                                        </c:listcol>
                                                        <c:listcol title="" columnClass="detail">
                                                            <c:button text="-" event="onDeleteObjDeductible"
                                                                      clientEvent="f.objDeductIndex.value='$index$';" validate="false"
                                                                      defaultRO="true"/>
                                                        </c:listcol>
                                                        <%--<c:listcol title="Description" >
                          <c:field name="deductibles.[$index$].stDescription" type="string|128" width="200" mandatory="false" readonly="false"/>
                        </c:listcol>--%>
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
        
    </c:tabpage>
    
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
                            <c:field caption="{L-ENGDescription-L}{L-INADeskripsi-L}" lov="LOV_InsuranceCompany"
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
                        <c:listcol title="{L-ENGPremium-L}{L-INAPremi-L}">
                            <%--
                        <c:field name="coins.[$index$].stAutoPremiFlag" clientchangeaction="VM_switchReadOnly('coins.[$index$].dbPremiAmount',this.checked);" type="check" readonly="<%=!canChangeCoinsMember || holdingComp%>" /> %

                        <c:field caption="Amount" name="coins.[$index$].dbPremiAmount" type="money16.2" mandatory="false" readonly="true" />
                        <c:evaluate when="<%=!holdingComp%>" >
                        <script>docEl('coins.[<%=index%>].stFlagEntryByRate').onclick();</script>
                        </c:evaluate>
                        --%>
                            <c:field name="coins.[$index$].stAutoPremiFlag"
                                     clientchangeaction="VM_switchReadOnly('coins.[$index$].dbPremiAmount',this.checked);"
                                     type="check" readonly="<%=holdingComp%>"/> %
                            
                            <c:field caption="Amount" name="coins.[$index$].dbPremiAmount" type="money16.2"
                                     mandatory="false" readonly="<%=holdingComp%>"/>
                            
                        </c:listcol>
                        <c:listcol title="{L-ENGDiscount(%)-L}{L-INADiskon(%)-L}">
                            <c:field caption="Discount Rate" width="40" name="coins.[$index$].dbDiscountRate"
                                     type="money16.2" mandatory="false"
                                     readonly="<%=!canChangeCoinsMember%>"/>%
                        </c:listcol>
                        <c:listcol title="{L-ENGDiscount<br>Amt-L}{L-INAJumlah<br>Diskon-L}">
                            <c:field caption="Discount Fee" width="100" name="coins.[$index$].dbDiscountAmount"
                                     type="money16.2" mandatory="false" readonly="true"/>
                        </c:listcol>
                        <c:listcol title="{L-ENGComm(%)-L}{L-INAKomisi(%)-L}">
                            <c:field caption="Commission Rate" width="40" name="coins.[$index$].dbCommissionRate"
                                     type="money16.2" mandatory="false"
                                     readonly="<%=!canChangeCoinsMember%>"/>%
                        </c:listcol>
                        <c:listcol title="{L-ENGComm<br>Amt-L}{L-INAJumlah<br>Komisi-L}">
                            <c:field caption="Commission" width="100" name="coins.[$index$].dbCommissionAmount"
                                     type="money16.2" mandatory="false" readonly="true"/>
                        </c:listcol>
                        
                        <c:listcol title="{L-ENGBrokerage(%)-L}{L-INABrokerage(%)-L}">
                            <c:field caption="Brokerage Rate" width="40" name="coins.[$index$].dbBrokerageRate"
                                     type="money16.2" mandatory="false"
                                     readonly="<%=!canChangeCoinsMember%>"/>%
                        </c:listcol>
                        <c:listcol title="{L-ENGBroker<br>Amt-L}{L-INAJumlah<br>Brokerage-L}">
                            <c:field caption="Brokerage Fee" width="100" name="coins.[$index$].dbBrokerageAmount"
                                     type="money16.2" mandatory="false" readonly="true"/>
                        </c:listcol>
                        <c:listcol title="{L-ENGHFee(%)-L}{L-INAHFee(%)-L}">
                            <c:field caption="Handling Fee Rate" width="40" name="coins.[$index$].dbHandlingFeeRate"
                                     type="money16.2" mandatory="false"
                                     readonly="<%=!canChangeCoinsMember%>"/>%
                        </c:listcol>
                        <c:listcol title="{L-ENGHFee<br>Amt-L}{L-INAJumlah<br>HFee-L}">
                            <c:field caption="Handling Fee" width="100" name="coins.[$index$].dbHandlingFeeAmount"
                                     type="money16.2" mandatory="false" readonly="true"/>
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
</c:tab>
</td>
</tr>
<tr>
    <td align=center>
        <br>
        <br>
        
        <c:button show="<%=!viewMode&&!form.isReApprovedMode()&&!form.isInputPaymentDateMode()%>"
                  text="{L-ENGRecalculate-L}{L-INAHitung Ulang-L}" event="btnRecalculate" validate="true"/>
        <c:button show="<%=!viewMode&&!form.isReApprovedMode()&&!form.isSavePolicyHistoryMode()%>"
                  text="{L-ENGSave-L}{L-INASimpan-L}" event="btnSave" validate="true"
                  confirm="Yakin mau disimpan ?"/>
        <c:button show="<%=form.isSavePolicyHistoryMode()%>"
                  text="{L-ENGSave Policy History-L}{L-INASimpan Polis History-L}" event="btnSavePolicyHistory" validate="true"
                  confirm="Yakin mau disimpan ?"/>
        <c:button show="<%=!viewMode%>" text="{L-ENGCancel-L}{L-INABatal-L}" event="btnCancel" validate="false"
                  confirm="Yakin Mau Dibatalkan ?"/>
        <c:evaluate
            when="<%=!effective && form.isApprovalMode()&&!form.isReverseMode()&&!form.isReApprovedMode()&&!form.isApprovedViaReverseMode()%>">
            <c:button show="true" text="{L-ENGApprove-L}{L-INASetujui-L}" event="btnApprove" validate="true"
                      confirm="Data yang disetujui tidak bisa dibuka kembali, yakin mau disetujui ? "/>
            
            <c:button show="<%=policy.getStParentID()!=null%>" text="Reject" enabled="<%=rejectable%>" event="btnReject"
                      validate="true" confirm="Are you sure want to reject ?"/>
             <%--<c:button show="<%=policy.getStParentID()!=null%>" text="Tolak" enabled="<%=rejectable%>" event="btnTolak"
                      validate="true" confirm="Yakin ingin ditolak ?"/>--%>
        </c:evaluate>
        <c:button show="<%=viewMode%>" text="{L-ENGClose-L}{L-INATutup-L}" event="btnCancel" validate="false"/>
        <c:button show="<%=form.isReverseMode()%>" text="Reverse" event="btnReverse"/>
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
                        document.getElementById('details.[' + i + '].stEntityID').value = o.entid;
                        document.getElementById('details.[' + i + '].stEntityName').value = o.entname;
                        document.getElementById('details.[' + i + '].stNPWP').value = o.npwp;
                        //document.getElementById('details.['+i+'].stEntityName').innerText=o.entname;
                    }
                }
                );
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

    <%--document.body.onscroll=function() {alert(document.body.scrollTop);};--%>
    <%--document.body.onload=function() {document.body.scrollTop=200};--%>
</script>
