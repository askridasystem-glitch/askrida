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
    
    final boolean hasPolicyType = policy.getStPolicyTypeID() != null;
    final boolean hasConditions = policy.getStConditionID() != null;
    final boolean hasRiskCategory = policy.getStRiskCategoryID() != null;
    
    final boolean isAddPeriodDesc = policy.isAddPeriodDesc();
    final boolean isLampiran = policy.isLampiran();
    
    boolean special_treaty = false;
    
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
    
    if (SessionManager.getInstance().getSession().getStBranch() != null)
    if (!SessionManager.getInstance().getSession().getStBranch().equalsIgnoreCase("00") && type_code.equalsIgnoreCase("OM_KREASI"))
    canSeeCoins = false;
    
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
    boolean statusPLA = FinCodec.ClaimStatus.PLA.equalsIgnoreCase(form.getStClaimStatus());
    boolean statusDLA = FinCodec.ClaimStatus.DLA.equalsIgnoreCase(form.getStClaimStatus());
    
    final boolean isInputPaymentDateMode = form.isInputPaymentDateMode();
    final boolean showPersetujuanPrinsipNo = policy.getStReference1()!=null && statusSPPA && type_code.equalsIgnoreCase("OM_BG")? true:false;
    final boolean showPolicyNo = policy.getStPolicyNo()!=null || (!statusDraft && !statusSPPA && canCreatePolicyHistory)? true:false;
    final boolean effective = policy.isEffective();
    final boolean posted = policy.isPosted();
    final boolean canChangeCoverType = (statusDraft || statusSPPA || statusPolicy ||statusHistory) && !posted && (policy.getStCoverTypeCode() == null);
    
    boolean canChangeCoinsMember = (statusDraft || statusSPPA || statusPolicy ||statusHistory || statusEndorse) && !posted;
    
    final boolean rejectable = form.isRejectable();
    final boolean isMember = policy.isMember();
    
    boolean cROpolicyHeader = false;
    boolean cROpolicyRiskDetail = false;
    boolean cROpolicyClausules = false;
    boolean cROpolicyPREMI = false;
    boolean cROpolicyCOINS = false;
    boolean cROpolicyINSTALLMENT = false;
    
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
    //cROpolicyCOINS = true;
    }
    
    final boolean cROReas = form.isReasMode();
    
    if (form.isReasMode()&&!statusEndorseRI) {
    cROpolicyHeader = true;
    cROpolicyRiskDetail = true;
    cROpolicyClausules = true;
    cROpolicyPREMI = true;
    //cROpolicyCOINS = true;
    }
    
    if(isInputPaymentDateMode){
    cROpolicyHeader = true;
    cROpolicyRiskDetail = true;
    cROpolicyClausules = true;
    cROpolicyPREMI = true;
    cROpolicyINSTALLMENT = true;
    }
    
    if (Tools.isYes(policy.getStLockCoinsFlag())) cROpolicyCOINS = true;
    
    boolean hasClaimCo = statusClaim && hasCoIns && policy.getClaimObject() != null;
    
    final boolean developmentMode = Config.isDevelopmentMode();
    
    String labelEntity = "{L-ENGBussiness Source-L}{L-INASumber Bisnis-L}";
    String labelEntity2 = "{L-ENGBussiness Source-L}{L-INASumber Bisnis-L}";
    if (type_code.equalsIgnoreCase("OM_BG"))
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
                                                 mandatory="true" overrideRO="true" readonly="false"/>
                                        <c:button text="+" event="addPeriodDesc" show="true" defaultRO="true"/>
                                        <c:button text="-" event="delPeriodDesc" show="true" defaultRO="true"/>
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
                                
                                <tr>
                                    <td colspan=3 class=header>
                                        <table cellpadding=2 cellspacing=1 class=row0 width="100%">
                                            <c:evaluate when="<%=statusEndorse||statusEndorseRI%>">
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
                                                             mandatory="true" readonly="false" lov="LOV_PeriodFactor"
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
                                <c:field width="200" show="true" lov="LOV_ParentPolicy" popuplov="true" changeaction="changeParentPolicy" caption="{L-ENGParent Policy No-L}{L-INANomor Polis Induk-L}"
                                         name="policy.stReference2" type="string|32" mandatory="false" presentation="standard">
                                    <c:lovLink name="cc_code" link="policy.stCostCenterCode" clientLink="false"/>
                                </c:field>     
                                <c:field clientchangeaction="selectCustomer2()" name="policy.stEntityID" type="string" width="200"
                                         popuplov="true" lov="LOV_Entity" mandatory="true"
                                         caption="<%=labelEntity2%>" presentation="standard"/>
                                <c:field width="200" rows="2" caption="<%=labelEntity%>"
                                         name="policy.stCustomerName" type="string|255" mandatory="true" readonly="false"
                                         presentation="standard"/>
                                <c:field width="200" rows="2" caption="{L-ENGAddress-L}{L-INAAlamat-L}" name="policy.stCustomerAddress"
                                         type="string|255" mandatory="true" readonly="false" presentation="standard"/>
                                <c:field clientchangeaction="selectProducer2()" name="policy.stProducerID" type="string" width="200"
                                         popuplov="true" lov="LOV_Entity" mandatory="true" caption="{L-ENGMarketer-L}{L-INAPemasar-L}"
                                         readonly="false" presentation="standard"/>
                                
                                <c:field width="200" rows="2" caption="{L-ENGMarketer Name-L}{L-INANama Pemasar-L}" name="policy.stProducerName"
                                         type="string|255" mandatory="true" readonly="false" presentation="standard"/>
                                <c:field width="200" rows="2" caption="{L-ENGMarketer Address-L}{L-INAAlamat Pemasar-L}"
                                         name="policy.stProducerAddress" type="string|255" mandatory="true" readonly="false"
                                         presentation="standard"/>
                                
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
                                
                                <tr>
                                    <td rowspan="2" valign="top">File Konversi</td>
                                    <td rowspan="2" valign="top">:</td>
                                    
                                    <c:evaluate when="<%=isLampiran%>">
                                        <td>
                                            <table width="200" border="1" cellspacing="0" cellpadding="2" bordercolor="#1a5591">
                                                <tr>
                                                    <td>
                                                        <table width="200">
                                                            <tr>
                                                                <td colspan="2">
                                                                    <c:field width="200" name="stFilePhysic" type="file" thumbnail="true"
                                                                             caption="{L-ENG File Attachment-L}{L-INA File Lampiran-L}"/>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td><c:button show="true" text="Konversi" event="uploadExcel"/>
                                                                </td>
                                                                <td><c:button show="true" text="Get History" event="btnKreasi"/></td>
                                                            </tr>
                                                        </table>
                                                    </td>
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
                                <c:field width="550" rows="3" caption="{L-ENGPolicy Notes-L}{L-INACatatan Polis-L}"
                                         name="policy.stDescription" type="string" mandatory="false" readonly="false"
                                         presentation="standard"/>
                            </table>
                        </td>
                    </tr>
                </table>
            </c:fieldcontrol>
        </td>
    </c:evaluate>
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