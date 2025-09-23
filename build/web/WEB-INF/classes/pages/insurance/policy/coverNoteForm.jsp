<%@ page import="com.webfin.insurance.form.CoverNoteForm,
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
                 com.crux.common.config.Config"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="Cover Note" >
<%
   final CoverNoteForm form = (CoverNoteForm) frame.getForm(); 

   final CoverNoteView policy = form.getPolicy();

   final InsurancePolicyObjectView selectedObject = form.getSelectedObject();
   final InsurancePolicyObjectView claimObject = form.getClaimObject();

   //policy.getPolicyType().getStPolicyTypeCode();

   final boolean ismasterCurrency = CurrencyManager.getInstance().isMasterCurrency(policy.getStCurrencyCode());

   final boolean hasPolicyType = policy.getStPolicyTypeID()!=null;
   final boolean hasConditions = policy.getStConditionID()!=null;
   final boolean hasRiskCategory = policy.getStRiskCategoryID()!=null;
   
   boolean special_treaty = false;
   
   String risk_code2 = new String(""); 
   String pol_type = new String("");
   String label = new String("");

   //final boolean phase2 = hasPolicyType && hasConditions ;
   final boolean phase2 = true ;
   final boolean phase1 = !phase2;

   final boolean hasCoIns = policy.hasCoIns();

   /*final boolean hasCoIns = FinCodec.InsuranceCoverType.COINSOUT.equalsIgnoreCase(policy.getStCoverTypeCode()) ||
      FinCodec.InsuranceCoverType.INWARD.equalsIgnoreCase(policy.getStCoverTypeCode());*/

   form.getTabs().enable("TAB_COINS",hasCoIns);



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

   boolean statusPLA = FinCodec.ClaimStatus.PLA.equalsIgnoreCase(form.getStClaimStatus());
   boolean statusDLA = FinCodec.ClaimStatus.DLA.equalsIgnoreCase(form.getStClaimStatus());


   final boolean effective = policy.isEffective();

   final boolean posted = policy.isPosted();

   final boolean canChangeCoverType = (statusDraft||statusSPPA||statusPolicy) && !posted && (policy.getStCoverTypeCode()==null);

   final boolean canChangeCoinsMember = (statusDraft||statusSPPA||statusPolicy) && !posted;

   final boolean rejectable = form.isRejectable();

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

   if (form.isReasMode()) {
      cROpolicyHeader = true;
      cROpolicyRiskDetail = true;
      cROpolicyClausules = true;
      cROpolicyPREMI = true;
      cROpolicyCOINS = true;
   }

   if (Tools.isYes(policy.getStLockCoinsFlag())) cROpolicyCOINS=true;

   boolean hasClaimCo = statusClaim && hasCoIns && policy.getClaimObject()!=null;

   final boolean developmentMode = Config.isDevelopmentMode();
%>
<script>
   function switchRates(i) {
      try {
         var autorate = docEl('selectedObject.coverage.['+i+'].stAutoRateFlag').checked;

         var entryrate = docEl('selectedObject.coverage.['+i+'].stEntryRateFlag').checked;

         VM_switchReadOnly('selectedObject.coverage.['+i+'].dbRate',!autorate && entryrate);VM_switchReadOnly('selectedObject.coverage.['+i+'].dbPremiNew',!entryrate);VM_switchReadOnly('selectedObject.coverage.['+i+'].stCalculationDesc',!entryrate)
      } catch(e) {
      }
   }
   
   function switchRates2(i) {
      try {
         var autorate = docEl('selectedObject.coveragereins.['+i+'].stAutoRateFlag').checked;

         var entryrate = docEl('selectedObject.coveragereins.['+i+'].stEntryRateFlag').checked;

         VM_switchReadOnly('selectedObject.coveragereins.['+i+'].dbRate',!autorate && entryrate);VM_switchReadOnly('selectedObject.coveragereins.['+i+'].dbPremiNew',!entryrate);VM_switchReadOnly('selectedObject.coveragereins.['+i+'].stCalculationDesc',!entryrate)
      } catch(e) {
      }
   }

   function switchRIRates(i,j,k) {
      //try {

         var pfx="treaties.["+i+"].details.["+j+"].shares.["+k+"]";
         var autorate = docEl(pfx+".stAutoRateFlag").checked;
         var entryrate = docEl(pfx+".stUseRateFlag").checked;

         VM_switchReadOnly(pfx+".dbPremiRate",!autorate && entryrate);
         VM_switchReadOnly(pfx+".dbPremiAmount",!entryrate);
      //} catch(e) {
      //}
   }
   
      function switchCoverRates(i,j,k,l) {
      //try {

         var pfx="treaties.["+i+"].details.["+j+"].shares.["+k+"].coverage.["+l+"]";
         var autorate = docEl(pfx+".stAutoRateFlag").checked;
         var entryrate = docEl(pfx+".stEntryRateFlag").checked;
         var tes = docEl(pfx+".stEntryInsuredAmountFlag").checked;
         //clientchangeaction="VM_switchReadOnly('"treaties.["+i+"].details.["+j+"].shares.["+k+"].coverage.["+l+"]"',this.checked)"
		
		 VM_switchReadOnly(pfx+".dbInsuredAmount",tes);
         VM_switchReadOnly(pfx+".dbRate",!autorate && entryrate);
         
         

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
               
               <%--<c:field width="300"include="<%=hasPolicyType%>" changeaction="onChangePolicyConditions" lov="LOV_PolicyConditions" caption="Conditions" name="policy.stConditionID" type="string" mandatory="true" readonly="<%=hasConditions%>" presentation="standard">
                  <c:lovLink name="poltype" link="policy.stPolicyTypeID" clientLink="false" />
               </c:field>--%>
            </table>
         </td>
      </tr>
      <tr>
         <td>
            <c:button text="Cancel" event="btnCancel" />
         </td>                            
      </tr>
   </c:evaluate>
<%
   boolean hasCoverType = policy.getStCoverTypeCode()!=null;
%>
   <c:evaluate when="<%=phase2 && !hasCoverType%>">
      <table cellpadding=2 cellspacing=1>
         <tr>
            <td>
            <c:fieldcontrol when="<%=cROpolicyHeader%>" readonly="true" >
               <table cellpadding=2 cellspacing=1>
                          
                             
                           <c:field width="200" changeaction="changeBranch" lov="LOV_CostCenter" caption="{L-ENGBranch-L}{L-INACabang-L}" name="policy.stCostCenterCode" type="string" mandatory="true" readonly="<%=!canChangeCoverType || policy.getStCostCenterCode()!=null%>" presentation="standard"/>
                          
                           <c:field show="<%=policy.getStCostCenterCode()!=null%>" width="200" lov="LOV_Region" changeaction="onChangeRegion" caption="{L-ENGRegion-L}{L-INADaerah-L}" name="policy.stRegionID" type="string" mandatory="false" readonly="<%=!canChangeCoverType%>" presentation="standard">
                              <c:lovLink name="cc_code" link="policy.stCostCenterCode" clientLink="false" />
                           </c:field> 
                           <c:field show="<%=policy.getStRegionID()!=null%>" changeaction="chgCoverType" width="200" lov="LOV_InsuranceCoverType" caption="{L-ENGCover Type-L}{L-INAJenis Penutupan-L}" name="policy.stCoverTypeCode" type="string" mandatory="true" readonly="<%=!canChangeCoverType%>" presentation="standard"/>
               			
               </table>
            </c:fieldcontrol>
            </td>
         </tr>
      </table>
   </c:evaluate>
   <c:evaluate when="<%=phase2 && hasCoverType%>">
      <tr>
         <td>
            <c:fieldcontrol when="<%=cROpolicyHeader%>" readonly="true" >
               <table cellpadding=2 cellspacing=1>
                  <tr>
                     <td>
                        <table cellpadding=2 cellspacing=1>
                           <c:field width="100" show="false" caption="Policy ID" name="policy.stPolicyID" type="string|32" mandatory="false" readonly="true" presentation="standard"/>
                           <c:field width="100" show="false" caption="Parent Policy" name="policy.stParentID" type="string|32" mandatory="false" readonly="true" presentation="standard"/>
                           <c:field width="100" show="<%=policy.getStStatus()!=null%>" caption="Status" name="stStatus" type="string|32" mandatory="false" readonly="true" presentation="standard"/>
                           <c:field width="200" show="<%=policy.getStClaimStatus()!=null%>" caption="{L-ENGClaim Status-L}{L-INAStatus Klaim-L}" name="policy.stClaimStatusDesc" type="string" mandatory="false" readonly="true" presentation="standard"/>
                           <c:field width="200" show="<%=policy.getStPolicyNo()!=null%>" caption="{L-ENGPolicy No-L}{L-INANomor Polis-L}" name="policy.stPolicyNo" type="string|32" mandatory="true" readonly="true" presentation="standard"/>
                           <c:field width="200" show="<%=policy.getStSPPANo()!=null%>" caption="{L-ENGSPPA No-L}{L-INANomor SPPA-L}" name="policy.stSPPANo" type="string|32" mandatory="true" readonly="false" presentation="standard"/>
                           <c:field width="200" changeaction="changeBranch" lov="LOV_CostCenter" caption="{L-ENGBranch-L}{L-INACabang-L}" name="policy.stCostCenterCode" type="string" mandatory="true" readonly="<%=!canChangeCoverType || policy.getStCostCenterCode()!=null%>" presentation="standard"/>
                           <c:field show="<%=policy.getStCostCenterCode()!=null%>" width="200" lov="LOV_Region" changeaction="onChangeRegion" caption="{L-ENGRegion-L}{L-INADaerah-L}" name="policy.stRegionID" type="string" mandatory="false" readonly="<%=!canChangeCoverType%>" presentation="standard">
                              <c:lovLink name="cc_code" link="policy.stCostCenterCode" clientLink="false" />
                           </c:field>
                  
                           <c:field show="<%=policy.getStRegionID()!=null%>" changeaction="chgCoverType" width="200" lov="LOV_InsuranceCoverType" caption="{L-ENGCover Type-L}{L-INAJenis Penutupan-L}" name="policy.stCoverTypeCode" type="string" mandatory="true" readonly="<%=!canChangeCoverType%>" presentation="standard"/>
                           <c:field caption="{L-ENGPolicy Date-L}{L-INATanggal Polis-L}" name="policy.dtPolicyDate" type="date" mandatory="true" readonly="false" presentation="standard"/>
                           <c:field show="<%=statusEndorse%>" caption="{L-ENGEndorsement Date-L}{L-INATanggal Endorsement-L}" name="policy.dtEndorseDate" type="date" mandatory="true" readonly="false" presentation="standard"/>

                           <%--<c:field width="200" changeaction="onChangePolicyType" lov="LOV_PolicyType" readonly="<%=hasPolicyType%>" caption="Policy Type" name="policy.stPolicyTypeID" type="string" mandatory="true" presentation="standard"/>
                           <c:field width="200"include="<%=hasPolicyType%>" changeaction="onChangePolicyConditions" lov="LOV_PolicyConditions" caption="Conditions" name="policy.stConditionID" type="string" mandatory="true" readonly="<%=hasConditions%>" presentation="standard">
                              <c:lovLink name="poltype" link="policy.stPolicyTypeID" clientLink="false" />
                           </c:field>--%>
                           <%--<c:field width="200"include="<%=hasPolicyType%>" changeaction="onChangeRiskCategory" lov="LOV_RiskCategory" caption="Risk Category" name="policy.stRiskCategoryID" type="string" mandatory="true" readonly="<%=hasRiskCategory%>" presentation="standard">
                              <c:lovLink name="poltype" link="policy.stPolicyTypeID" clientLink="false" />
                           </c:field>--%>
                           <c:field lov="LOV_Currency" changeaction="onChgCurrency" caption="{L-ENGCurrency-L}{L-INAMata Uang-L}" name="policy.stCurrencyCode" type="string" mandatory="true" readonly="false" presentation="standard"/>
                           <c:field caption="Kurs" name="policy.dbCurrencyRate" type="money16.2" mandatory="true" readonly="<%=ismasterCurrency%>" presentation="standard"/>
                           <c:field caption="{L-ENGPeriod Start-L}{L-INAPeriode Awal-L}" name="policy.dtPeriodStart" type="date" mandatory="true" readonly="false" presentation="standard"/>
                           <%--<c:field caption="Period" name="policy.stInsurancePeriodID" type="string" lov="LOV_InsurancePeriod" mandatory="true" readonly="false" presentation="standard"/>--%>
                           <c:field caption="{L-ENGPeriod End-L}{L-INAPeriode Akhir-L}" name="policy.dtPeriodEnd" type="date" mandatory="true" readonly="false" presentation="standard"/>

                           <%--<c:field caption="Period Base" name="policy.stPeriodBaseID" type="string" mandatory="true" readonly="false" lov="LOV_PeriodBase" changeaction="changePeriodBase" presentation="standard"/>
                           <c:field caption="Period Rate" name="policy.dbPeriodRate" type="money16.2" mandatory="true" presentation="standard" />--%>
                            <%--<c:field width="150" caption="Premium (BASE)" name="policy.dbPremiBase" type="money16.2" mandatory="false" readonly="true" presentation="standard"/>--%>
                            <c:field width="150" caption="{L-ENGPremium (Net)-L}{L-INAPremi Netto-L}" name="policy.dbPremiNetto" type="money16.2" mandatory="false" readonly="false" presentation="standard"/>
                        </table>
                     </td>
                     <td>

                        <table cellpadding=2 cellspacing=1>
                           <c:field caption="{L-ENGActive-L}{L-INAAktif-L}" name="policy.stActiveFlag" type="check" mandatory="false" readonly="true" presentation="standard"/>
                           <c:field caption="{L-ENGEffective-L}{L-INAEfektif-L}" name="policy.stEffectiveFlag" type="check" mandatory="true" readonly="true" presentation="standard"/>
                           <%--<c:field name="policy.stEntityID" type="string" hidden="true" />
                           <tr>
                              <td>Customer</td>
                              <td>:</td>
                              <td>
                                 <c:field width="200" caption="Customer" name="policy.stEntityName" type="string" mandatory="false" readonly="true" />
                                 <c:button text="..." clientEvent="selectCustomer()" />
                              </td>
                           </tr>--%>
                           <c:field clientchangeaction="selectCustomer2()"  name="policy.stEntityID" type="string" width="200" popuplov="true"  lov="LOV_Entity" mandatory="true" caption="{L-ENGBussiness Source-L}{L-INASumber Bisnis-L}" presentation="standard" />

                           <c:field width="200" caption="{L-ENGBussiness Source Name-L}{L-INANama Sumber Bisnis-L}" name="policy.stCustomerName" type="string|255" mandatory="true" readonly="false" presentation="standard"/>
                          
                           <%--<c:field name="policy.stProducerID" type="string" hidden="true" />
                           <tr>
                              <td>Producer</td>
                              <td>:</td>
                              <td>
                                 <c:field width="200" caption="Producer" name="policy.stProducerEntName" type="string" mandatory="false" readonly="true" />
                                 <c:button text="..." clientEvent="selectProducer()" />
                              </td>
                           </tr>--%>

                           <%--<c:field caption="Period Length" name="policy." type="string|64" mandatory="false" readonly="false" presentation="standard"/>--%>
                           <c:field caption="{L-ENGPosted-L}{L-INAJurnal-L}" name="policy.stPostedFlag" type="check" mandatory="false" overrideRO="true" readonly="true" presentation="standard"/>
                        </table>
                     </td>
                  </tr>
                  <tr>
                     <td colspan=2>
                        <table cellpadding=2 cellspacing=1>
                           <c:field width="550" rows="3" caption="{L-ENGPolicy Notes-L}{L-INACatatan Polis-L}" name="policy.stDescription" type="string" mandatory="false" readonly="false" presentation="standard"/>
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
            <c:button show="<%=!viewMode%>" text="{L-ENGSave-L}{L-INASimpan-L}" event="btnSave" validate="true" confirm="Are you sure want to save ?"/>
            <c:button show="<%=!viewMode%>" text="{L-ENGCancel-L}{L-INABatal-L}" event="btnCancel" validate="false" confirm="Are you sure want to discard your changes ?"/>
            <c:evaluate when="<%=!effective && form.isApprovalMode()%>" >
               <c:button show="true"  text="{L-ENGApprove-L}{L-INASetujui-L}" event="btnApprove" validate="true" confirm="Are you sure want to approve ?" />
               <c:button show="<%=policy.getStParentID()!=null%>" text="Reject" enabled="<%=rejectable%>" event="btnReject" validate="true" confirm="Are you sure want to reject ?"/>
            </c:evaluate>
            <c:button show="<%=viewMode%>" text="{L-ENGClose-L}{L-INATutup-L}" event="btnCancel" validate="false" />
            <c:evaluate when="<%=developmentMode%>" >
               <c:button text="testGeneratePolicyNo" event="testGeneratePolicyNo" />
               <c:button text="testGenerateEndorseNo" event="testGenerateEndorseNo" />
               <c:button text="validate" event="validate" />
            </c:evaluate>
             <c:button show="<%=cROReas%>"  text="{L-ENGApprove Reins-L}{L-INASetujui Reas-L}" event="btnApproveReins" validate="true" confirm="Are you sure want to approve reins ?" />
 
         </td>
      </tr>
   </c:evaluate>
</table>

</c:frame>
<script>
   function selectAgent(i) {
      openDialog('so.ctl?EVENT=INS_AGENT_SELECT', 400,400,
         function (o) {
            if (o!=null) {
               document.getElementById('details.['+i+'].stEntityID').value=o.entid;
               document.getElementById('details.['+i+'].stEntityName').value=o.entname;
               document.getElementById('details.['+i+'].stNPWP').value=o.npwp;
               //document.getElementById('details.['+i+'].stEntityName').innerText=o.entname;
            }
         }
      );
   }

   function selectCoIns(i) {
      openDialog('so.ctl?EVENT=INS_AGENT_SELECT', 400,400,
         function (o) {
            if (o!=null) {
               document.getElementById('coins.['+i+'].stEntityID').value=o.entid;
               document.getElementById('coins.['+i+'].stEntityName').value=o.entname;
               //document.getElementById('coins.['+i+'].stEntityName').innerText=o.entname;
            }
         }
      );
   }

   function selectCustomer2() {
      var o= window.lovPopResult;
      document.getElementById('policy.stCustomerName').value=o.value;
   }

   function selectProducer2() {
      var o= window.lovPopResult;
      document.getElementById('policy.stProducerName').value=o.value;
      document.getElementById('policy.stProducerAddress').value=o.address;

   }

   function selectCustomer(i) {
      openDialog('entity_search.crux', 400,400,
         function (o) {
            if (o!=null) {
               document.getElementById('policy.stEntityID').value=o.stEntityID;
               document.getElementById('policy.stEntityName').value=o.stEntityName;
               document.getElementById('policy.stCustomerName').value=o.stEntityName;
               document.getElementById('policy.stCustomerAddress').value=o.stAddress;
            }
         }
      );
   }

   function selectProducer(i) {
      openDialog('entity_search.crux', 400,400,
         function (o) {
            if (o!=null) {
               document.getElementById('policy.stProducerID').value=o.stEntityID;
               document.getElementById('policy.stProducerEntName').value=o.stEntityName;
               document.getElementById('policy.stProducerName').value=o.stEntityName;
               document.getElementById('policy.stProducerAddress').value=o.stAddress;
            }
         }
      );
   }
   
   function selectPrincipal() {
      var o= window.lovPopResult;
      document.getElementById('selectedObject.stReference2').value=o.address;
      document.getElementById('selectedObject.stReference3').value=o.functionary_name;
	  document.getElementById('selectedObject.stReference4').value=o.functionary_position;
	  document.getElementById('selectedObject.stReference1').value=o.value;
   }
   
   function selectSurety() {
      var o= window.lovPopResult;
      document.getElementById('selectedObject.stReference6').value=o.address;
      document.getElementById('selectedObject.stReference7').value=o.functionary_name;
	  document.getElementById('selectedObject.stReference8').value=o.functionary_position;
   }
   
   function selectBank() {
      var o= window.lovPopResult;
      document.getElementById('selectedObject.stReference9').value=o.rc_no;
     }

   <%--document.body.onscroll=function() {alert(document.body.scrollTop);};--%>
   <%--document.body.onload=function() {document.body.scrollTop=200};--%>
</script>
