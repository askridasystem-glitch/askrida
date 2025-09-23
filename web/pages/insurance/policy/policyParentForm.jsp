<%@ page import="com.webfin.insurance.form.PolicyParentForm,
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
<c:frame title="Parent Policy">
<%
    final PolicyParentForm form = (PolicyParentForm) frame.getForm();

    final InsurancePolicyParentView policy = form.getPolicy();

    final InsurancePolicyObjectView selectedObject = form.getSelectedObject();
    
    final InsurancePolicyObjectView claimObject = form.getClaimObject();
    
    //final boolean canCreatePolicyHistory = form.isCanCreatePolicyHistory();
    
    //final boolean enableNoPolicy = form.isEnableNoPolicy();

    final boolean ismasterCurrency = CurrencyManager.getInstance().isMasterCurrency(policy.getStCurrencyCode());

    final boolean hasPolicyType = policy.getStPolicyTypeID() != null;
    final boolean hasConditions = policy.getStConditionID() != null;
    final boolean hasRiskCategory = policy.getStRiskCategoryID() != null;

    final boolean isAddPeriodDesc = policy.isAddPeriodDesc();
    final boolean isLampiran = policy.isLampiran();

    boolean special_treaty = false;
    boolean showPersetujuanPrinsipNo = false;

    //final boolean phase2 = hasPolicyType && hasConditions ;
    final boolean phase2 = hasPolicyType;
    final boolean phase1 = !phase2;
    
    boolean hasCoIns = policy.hasCoIns();
    boolean coLeaderPolicy = false;
    if (policy.getStCoverTypeCode() != null)
        if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN"))
            hasCoIns = false;

    boolean directPolicy = false;
    if (policy.getStCoverTypeCode() != null)
        if (policy.getStCoverTypeCode().equalsIgnoreCase("DIRECT"))
            directPolicy = true;

    boolean canSeeCoins = false;

    if (SessionManager.getInstance().getSession().getStBranch() != null)
        if (SessionManager.getInstance().getSession().getStBranch().equalsIgnoreCase("00"))
            canSeeCoins = true;

    if (SessionManager.getInstance().getSession().getStBranch() == null)
        canSeeCoins = true;

    if (policy.getPolicyType() != null)
        if (!policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_KREASI"))
            canSeeCoins = true;

    /*final boolean hasCoIns = FinCodec.InsuranceCoverType.COINSOUT.equalsIgnoreCase(policy.getStCoverTypeCode()) ||
    FinCodec.InsuranceCoverType.INWARD.equalsIgnoreCase(policy.getStCoverTypeCode());*/

    form.getTabs().enable("TAB_COINS", hasCoIns);

    if (policy.getPolicyType() != null)
        if (policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_KREASI"))
            form.getTabs().enable("TAB_COINS", canSeeCoins);

    final boolean ro = form.isReadOnly();

    boolean viewMode = ro;

    boolean statusDraft = FinCodec.PolicyStatus.DRAFT.equalsIgnoreCase(form.getStStatus());
    boolean statusPolicy = FinCodec.PolicyStatus.POLICY.equalsIgnoreCase(form.getStStatus());
    boolean statusEndorse = FinCodec.PolicyStatus.ENDORSE.equalsIgnoreCase(form.getStStatus());
    boolean statusClaim = FinCodec.PolicyStatus.CLAIM.equalsIgnoreCase(form.getStStatus());
    boolean statusCancel = FinCodec.PolicyStatus.CANCEL.equalsIgnoreCase(form.getStStatus());
    boolean statusSPPA = FinCodec.PolicyStatus.SPPA.equalsIgnoreCase(form.getStStatus());
    boolean statusRenewal = FinCodec.PolicyStatus.RENEWAL.equalsIgnoreCase(form.getStStatus());
    boolean statusClaimEndorse = FinCodec.PolicyStatus.ENDORSECLAIM.equalsIgnoreCase(form.getStStatus());
    boolean statusHistory = FinCodec.PolicyStatus.HISTORY.equalsIgnoreCase(form.getStStatus());
    boolean statusEndorseRI = FinCodec.PolicyStatus.ENDORSERI.equalsIgnoreCase(form.getStStatus());
    boolean statusParent = FinCodec.PolicyStatus.PARENT.equalsIgnoreCase(form.getStStatus());

    
    boolean statusPLA = FinCodec.ClaimStatus.PLA.equalsIgnoreCase(form.getStClaimStatus());
    boolean statusDLA = FinCodec.ClaimStatus.DLA.equalsIgnoreCase(form.getStClaimStatus());
    
    boolean showPolicyNo = false;
    if(policy.getStPolicyNo()!=null) showPolicyNo = true;
    
     showPolicyNo = true;
    
    if(policy.getStReference1()!=null && statusSPPA && policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_BG")) showPersetujuanPrinsipNo = true;

    final boolean effective = policy.isEffective();
    
    final boolean posted = policy.isPosted();

    final boolean canChangeCoverType = (statusDraft || statusSPPA || statusPolicy ||statusHistory ||statusParent) && !posted && (policy.getStCoverTypeCode() == null);

    final boolean canChangeCoinsMember = (statusDraft || statusSPPA || statusPolicy ||statusHistory) && !posted;

    final boolean rejectable = form.isRejectable();

    final boolean isMember = policy.isMember();

    boolean cROpolicyHeader = false;
    boolean cROpolicyRiskDetail = false;
    boolean cROpolicyClausules = false;
    boolean cROpolicyPREMI = false;
    boolean cROpolicyCOINS = false;

    /* if (statusClaim) {
          cROpolicyHeader = true;
          cROpolicyRiskDetail = true;
          cROpolicyClausules = true;
          cROpolicyPREMI = true;
          cROpolicyCOINS = true;
       }
    */

    if (statusPLA || statusDLA) {
        cROpolicyHeader = true;
        cROpolicyRiskDetail = true;
        cROpolicyClausules = true;
        cROpolicyPREMI = true;
        cROpolicyCOINS = true;
    }

    final boolean cROReas = form.isReasMode();

    if (form.isReasMode()&&!statusEndorseRI) {
        cROpolicyHeader = true;
        cROpolicyRiskDetail = true;
        cROpolicyClausules = true;
        cROpolicyPREMI = true;
        cROpolicyCOINS = true;
    }

    if (Tools.isYes(policy.getStLockCoinsFlag())) cROpolicyCOINS = true;

    boolean hasClaimCo = statusClaim && hasCoIns && policy.getClaimObject() != null;

    final boolean developmentMode = Config.isDevelopmentMode();
    
    String labelEntity = "{L-ENGBussiness Source-L}{L-INASumber Bisnis-L}";
    String labelEntity2 = "{L-ENGBussiness Source-L}{L-INASumber Bisnis-L}";
    if (policy.getPolicyType() != null)
        if (policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_BG"))
    		labelEntity = "Bank Penerbit";
    
    if (policy.getPolicyType() != null)
    	if(policy.getStPolicyTypeGroupID().equalsIgnoreCase("7")){
    		labelEntity = "Obligee";
    		labelEntity2 = "Instansi";
    	}
    	
%>
<script>
    function switchRates(i) {
        try {
            var autorate = docEl('selectedObject.coverage.[' + i + '].stAutoRateFlag').checked;

            var entryrate = docEl('selectedObject.coverage.[' + i + '].stEntryRateFlag').checked;

            VM_switchReadOnly('selectedObject.coverage.[' + i + '].dbRate', !autorate && entryrate);
            VM_switchReadOnly('selectedObject.coverage.[' + i + '].dbPremiNew', !entryrate);
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
                 name="policy.stPolicyNo" type="string|32" mandatory="false" readonly="true" presentation="standard"/>
        
        
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
        <c:field caption="{L-ENGPolicy Date-L}{L-INATanggal Polis-L}" name="policy.dtPolicyDate" type="date"
                 mandatory="true" readonly="false" presentation="standard"/>
        <c:field show="<%=statusEndorse||statusEndorseRI%>" caption="{L-ENGEndorsement Date-L}{L-INATanggal Endorsement-L}"
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
   
       
        <c:evaluate when="<%=isAddPeriodDesc%>">
            <c:field width="250" rows="3" caption="{L-ENGPeriod Desc-L}{L-INADeskripsi Periode-L}"
                     name="policy.stPeriodDesc" type="string" mandatory="<%=isAddPeriodDesc%>" readonly="false"
                     presentation="standard"/>
        </c:evaluate>
          </table>
</td>
<td>
    <table cellpadding=2 cellspacing=1>
        <c:field clientchangeaction="selectCustomer2()" name="policy.stEntityID" type="string" width="300"
                 popuplov="true" lov="LOV_Entity" mandatory="true"
                 caption="<%=labelEntity2%>" presentation="standard"/>
        <c:field width="300" rows="3" caption="<%=labelEntity%>"
                 name="policy.stCustomerName" type="string|255" mandatory="true" readonly="false"
                 presentation="standard"/>
        <c:field width="300" rows="3" caption="{L-ENGAddress-L}{L-INAAlamat-L}" name="policy.stCustomerAddress"
                 type="string|255" mandatory="true" readonly="false" presentation="standard"/>
        <c:field clientchangeaction="selectProducer2()" name="policy.stProducerID" type="string" width="300"
                 popuplov="true" lov="LOV_Entity" mandatory="true" caption="{L-ENGMarketer-L}{L-INAPemasar-L}"
                 readonly="false" presentation="standard"/>

        <c:field width="300" rows="3" caption="{L-ENGMarketer Name-L}{L-INANama Pemasar-L}" name="policy.stProducerName"
                 type="string|255" mandatory="true" readonly="false" presentation="standard"/>
        <c:field width="300" rows="3" caption="{L-ENGMarketer Address-L}{L-INAAlamat Pemasar-L}"
                 name="policy.stProducerAddress" type="string|255" mandatory="true" readonly="false"
                 presentation="standard"/>

         <c:field width="300" name="policy.stReference1" caption="{L-ENGDokumen Polis Induk-L}{L-INADokumen Polis Induk-L}"
                  type="file" thumbnail="true" presentation="standard" />

        <c:evaluate when="<%=policy.getSPPAFF()!=null%>">
            <c:flexfield ffid="<%=policy.getSPPAFF().getStFlexFieldHeaderID()%>" prefix="policy."/>
        </c:evaluate>
            <%--<c:field caption="Period Length" name="policy." type="string|64" mandatory="false" readonly="false" presentation="standard"/>--%>
        <c:field caption="{L-ENGPosted-L}{L-INAJurnal-L}" name="policy.stPostedFlag" type="check" mandatory="false"
                 overrideRO="true" show="false" readonly="true" presentation="standard"/>

        <c:evaluate when="<%=policy.getPolicyType().getStPolicyTypeCode().equalsIgnoreCase("OM_KREASI")%>">
            <c:field clientchangeaction="selectCreditType()" name="policy.stKreasiTypeID" type="string" width="200"
                     popuplov="true" lov="LOV_TypeOfCredit" mandatory="true"
                     caption="{L-ENGType Of Credit-L}{L-INAJenis Kredit-L}" presentation="standard">
            <c:lovLink name="cc_code" link="policy.stCostCenterCode" clientLink="false"/>
            </c:field>
            <c:field show="false" width="200" caption="{L-ENGKreasi Type Name-L}{L-INANama Jenis Kreasi-L}"
                     name="policy.stKreasiTypeDesc" type="string|255" mandatory="true" readonly="false"
                     presentation="standard"/>

            <c:field clientchangeaction="selectReinsKreasi()" name="policy.stCoinsID" type="string" width="200"
                     popuplov="true" lov="LOV_InsuranceCompany" mandatory="true"
                     caption="{L-ENGReinsurer-L}{L-INAReasuradur-L}" presentation="standard"/>
            <c:field show="false" width="200" caption="{L-ENGReinsurer Name-L}{L-INANama Reasuradur-L}"
                     name="policy.stCoinsName" type="string|255" mandatory="true" readonly="false"
                     presentation="standard"/>
        </c:evaluate>

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
</c:fieldcontrol>
</td>
</tr>
<tr>
<td>

</td>
</tr>
<tr>
    <td align=center>
        <br>
        <br>
   
               <c:button show="<%=!viewMode%>"
                  text="{L-ENGSave-L}{L-INASimpan-L}" event="btnSave" validate="true"
                  confirm="Yakin mau disimpan ?"/>
                <c:button show="<%=form.isApprovalMode()%>"
                  text="Setujui" event="btnApproveparent" validate="true"
                  confirm="Yakin mau disetujui ?"/>
       
        <c:button show="<%=!viewMode%>" text="{L-ENGCancel-L}{L-INABatal-L}" event="btnCancel" validate="false"
                  confirm="Yakin Mau Dibatalkan ?"/>
                    
            
            <c:button show="<%=policy.getStParentID()!=null%>" text="Reject" enabled="<%=rejectable%>" event="btnReject"
                      validate="true" confirm="Are you sure want to reject ?"/>

        <c:button show="<%=viewMode%>" text="{L-ENGClose-L}{L-INATutup-L}" event="btnCancel" validate="false"/>
       
        
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
