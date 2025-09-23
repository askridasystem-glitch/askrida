<%@page import="com.webfin.acceptance.form.AcceptanceForm"%>
<%@ page import="com.webfin.acceptance.form.AcceptanceForm,
com.crux.util.Tools,
com.webfin.gl.ejb.CurrencyManager,
com.crux.util.DTOList,
com.webfin.FinCodec,
com.webfin.acceptance.model.*,
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
final AcceptanceForm form = (AcceptanceForm) frame.getForm();

final AcceptanceView policy = form.getPolicy();

final AcceptanceObjectView selectedObject = form.getSelectedObject();

final boolean canCreatePolicyHistory = form.isCanCreatePolicyHistory();

boolean special_treaty = false;

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
   
final boolean ro = form.isReadOnly();
boolean viewMode = ro;

boolean statusDraft = FinCodec.PolicyStatus.DRAFT.equalsIgnoreCase(form.getStStatus());
boolean statusPolicy = FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(form.getStStatus());
boolean statusEndorse = FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(form.getStStatus());
boolean statusSPPA = FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(form.getStStatus());
boolean statusRenewal = FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(form.getStStatus());
boolean statusHistory = FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(form.getStStatus());
boolean statusAcceptance = FinCodec.AcceptanceStatus.ACCEPTANCE.equalsIgnoreCase(form.getStStatus());

final boolean isInputPaymentDateMode = form.isInputPaymentDateMode();
boolean showPersetujuanPrinsipNo = policy.getStReference1()!=null && (statusSPPA || statusPolicy) && type_code.equalsIgnoreCase("OM_BG")? true:false;
final boolean showPolicyNo = policy.getStPolicyNo()!=null || (!statusDraft && !statusSPPA && canCreatePolicyHistory)? true:false;
final boolean effective = policy.isEffective();
final boolean posted = policy.isPosted();
final boolean canChangeCoverType = (statusAcceptance || statusDraft || statusSPPA || statusPolicy ||statusHistory || statusEndorse) && !posted && (policy.getStCoverTypeCode() == null);

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
}else if(isRestitutionEndorseMode){
    cROpolicyRiskDetail = false;
    cROpolicyCoverage = true;
    cROendorsePeriodFactor = true;
    cROendorseObject = true;
    cROpolicyPREMI = false;
}

final boolean showRateFactor = form.showRateFactor();

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
                <%--
                <c:field show="<%=policy.getStPolicyTypeID()!=null%>" changeaction="chgCoverType" width="200"
                         lov="LOV_InsuranceCoverType" caption="{L-ENGCover Type-L}{L-INAJenis Penutupan-L}"
                         name="policy.stCoverTypeCode" type="string" mandatory="true"
                         readonly="<%=!canChangeCoverType%>" presentation="standard"/>--%>
                        
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
                           <c:field width="250" show="true" caption="No Underwriting Sheet"
                                     name="policy.stUnderwritingSheetNo" type="string|32" mandatory="true" readonly="false" presentation="standard"/>
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
                            <%--
                            <c:field show="<%=policy.getStRegionID()!=null%>" changeaction="chgCoverType" width="200"
                                     lov="LOV_InsuranceCoverType" caption="{L-ENGCover Type-L}{L-INAJenis Penutupan-L}"
                                     name="policy.stCoverTypeCode" type="string" mandatory="true" readonly="<%=!canChangeCoverType%>"
                                     presentation="standard"/>--%>
                            <c:field caption="{L-ENGPolicy/Endorsement Date-L}{L-INATanggal Pengajuan-L}" name="policy.dtPolicyDate" type="date"
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
                            
                            
                            <tr>
                                <td></td>
                                <td></td>
                                <td>
                                    
                                    
                                </td>
                            </tr>
                            
                            <c:evaluate when="<%=isAddPeriodDesc%>">
                                <c:field width="250" rows="3" caption="{L-ENGPeriod Desc-L}{L-INADeskripsi Periode-L}"
                                         name="policy.stPeriodDesc" type="string" mandatory="<%=isAddPeriodDesc%>" readonly="false"
                                         presentation="standard"/>
                            </c:evaluate>
                      
                        </table>
                    </td>
                    <td>
                        <table cellpadding=2 cellspacing=1>
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
                            
                            <c:fieldcontrol when="<%=cROendorsePeriodFactor%>" readonly="false">
                            <c:field clientchangeaction="selectCustomer2();" name="policy.stEntityID" type="string" width="300"
                                     popuplov="true" lov="LOV_Entity" mandatory="true"
                                     caption="<%=policy.getPolicyType().getStEntityMasterLabel()%>" presentation="standard"/>
                                     
                                         <c:field width="300" rows="3" caption="<%=policy.getPolicyType().getStEntityMasterDescriptionLabel()%>"
                                                  name="policy.stCustomerName" type="string|255" mandatory="true" readonly="false"
                                                  presentation="standard"/>
                                         <c:field width="300" rows="3" caption="{L-ENGAddress-L}{L-INAAlamat-L}" name="policy.stCustomerAddress"
                                                  type="string|255" mandatory="true" readonly="false" presentation="standard"/>
                                     
                            
   
                            </c:fieldcontrol>

                            <c:evaluate when="<%=form.isAssignMode()%>">
                                <c:field name="policy.stMarketingOfficerWho" type="string" width="300"
                                     popuplov="true" lov="LOV_Profil" caption="Person In Charge (PIC)"
                                     readonly="false" presentation="standard"/>
                            </c:evaluate>
                            

                            <c:field caption="{L-ENGApprove-L}{L-INASetujui Kantor Operasional-L}" name="policy.stReadyToApproveFlag" type="check" mandatory="false"
                                     readonly="true" presentation="standard"/>

                            <c:evaluate when="<%=form.isConfirmMode()%>">
                                <c:field caption="{L-ENGNeed Confirmation-L}{L-INAButuh Konfirmasi-L}" name="policy.stCheckingFlag" type="check" mandatory="false"
                                     readonly="true" presentation="standard"/>
                            </c:evaluate>
                            

                           
                           
                            <c:field caption="{L-ENGPosted-L}{L-INAJurnal-L}" name="policy.stPostedFlag" type="check" mandatory="false"
                                     overrideRO="true" show="false" readonly="true" presentation="standard"/>
                            
                            
                             <c:evaluate when="<%=policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_KREASI") && showCoinsurance%>">
                                <c:field clientchangeaction="selectReinsKreasi()" name="policy.stCoinsID" type="string" width="300"
                                         popuplov="true" lov="LOV_InsuranceCompany" 
                                         caption="{L-ENGReinsurer-L}{L-INAReasuradur-L}" presentation="standard"/>
                                <c:field show="false" width="300" caption="{L-ENGReinsurer Name-L}{L-INANama Reasuradur-L}"
                                         name="policy.stCoinsName" type="string" readonly="false"
                                         presentation="standard"/>
                            </c:evaluate>
                            
                            <tr>
                                <td>
                                    <c:evaluate when="<%=isMember%>">
                                        <c:field width="300" caption="{L-ENGLeader-L}{L-INALeader-L}"
                                                 name="policy.stCoLeaderID" popuplov="true" lov="LOV_Entity" type="string" mandatory="true" readonly="false"
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
                            
                        </table>
                    </td>
                </tr>
                <tr>
                    <td colspan=2>
                        <table cellpadding=2 cellspacing=1>
                            <c:field width="550" rows="2" caption="{L-ENG Notes-L}{L-INACatatan-L}"
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
                                <td>{L-ENGObject-L}{L-INAInformasi Pertanggungan-L}</td>
                                <td>:</td>
                                <td>
                                    <c:field width="400" lov="lovObjects" name="stSelectedObject" caption="Selected Object"
                                             type="string" changeaction="selectObject" overrideRO="true" readonly="false"/>
                                    <c:fieldcontrol when="<%=cROpolicyRiskDetail%>" readonly="true">
                                        <c:button text="+" event="doNewObject" defaultRO="true"/>
                                        <c:button text="-" event="doDeleteObject" confirm="Yakin Ingin Menghapus Objek Ini ?" defaultRO="true"/>
                                        
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
                                                    {L-ENGObject-L}{L-INAInformasi Pertanggungan-L}
                                                </td>
                                            </tr>
                                            <tr class=row0>
                                                <td>
                                                    <table cellpadding=2 cellspacing=1>
                                                        
                                                           
                                                        <c:evaluate when="<%=form.getSelectedObject() instanceof AcceptanceObjDefaultView%>">
                                                            <c:flexfield ffid="<%=policy.getPolicyType().getStObjectMapID()+"_ACC"%>" prefix="selectedObject."/>
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
                                                        
                                                        <%--
                                                        <c:field lov="LOV_RiskCategory" caption="{L-ENGRisk Code / Category-L}{L-INAKode Resiko / Kategori-L}"
                                                                 name="selectedObject.stRiskCategoryID" type="string" mandatory="true" readonly="false"
                                                                 width="400" presentation="standard" popuplov="true" refresh="true">
                                                            <c:lovLink name="datestart" link="policy.dtPeriodStart" clientLink="false"/>                 
                                                            <c:param name="poltype" value="<%=policy.getStPolicyTypeID()%>"  />
                                                        </c:field> --%>
                                                        <c:field caption="Object ID" name="selectedObject.stPolicyObjectID" type="string"
                                                                 mandatory="false" readonly="true" hidden="true" presentation="standard"/>
                                                                     
                                                        <c:field width="200" caption="{L-ENGInsured Amount-L}{L-INAHarga Pertanggungan-L}"
                                                                 name="selectedObject.dbObjectInsuredAmount" type="money16.2" mandatory="false"
                                                                 readonly="false" presentation="standard"/>
                                                        
                                                        <c:field width="200" caption="{L-ENGPremi Total-L}{L-INA Premi-L}"
                                                                 name="selectedObject.dbObjectPremiTotalAmount" type="money16.2" mandatory="false"
                                                                 readonly="false" presentation="standard"/>
                                                    </table>
                                                </td>
                                            </tr>
              
                                            
                                        </table>
                                        </c:fieldcontrol>
                                    </c:fieldcontrol>
                                </c:tabpage>
                                

                                        <c:tabpage name="TAB_DETAIL_DOCUMENTS">
                                            <table cellpadding=2 cellspacing=1 class=header>
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
                                                            
                                                            <c:listcol title="Dokumen">
                                                                <c:field name="selectedObject.detailDocuments.[$index$].stFilePhysic" type="file"
                                                                         thumbnail="true" caption="File"/>
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
                                  
                                   <c:evaluate when="<%=headOfficeUser%>">
                                       <c:field name="policy.stManualReinsuranceFlag" type="check" caption="Manual R/I Spreading" presentation="standard" />
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

        <c:button show="<%=!viewMode&&!form.isReApprovedMode()&&!form.isReverseMode()&&!form.isSavePolicyHistoryMode()%>"
                  text="Hitung Ulang" event="btnRecalculate"
                   validate="true" />

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
                    <%if(policy.getStPolicyNo()!=null){%><tr><td><b>Nomor Polis</b></td><td>:</td><td><font style="font-weight:bold; font-size:14px;"><%= policy.getStPolicyNo().substring(0,4)+"-"+policy.getStPolicyNo().substring(4,8)+"-"+policy.getStPolicyNo().substring(8,12)+"-"+policy.getStPolicyNo().substring(12,16)+"-"+policy.getStPolicyNo().substring(16,18)%></font></td></tr><%}%>
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
<script>
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
