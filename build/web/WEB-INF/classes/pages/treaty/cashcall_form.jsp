<%@ page import="com.webfin.master.claim.CashCallMasterForm,
                 com.webfin.insurance.model.InsuranceCashCallView,
                 com.webfin.insurance.model.InsuranceTreatyDetailView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame>
<%
   CashCallMasterForm form = (CashCallMasterForm) request.getAttribute("FORM");

   boolean policyTypeSelected = form.getStPolicyTypeID()!=null;

   InsuranceCashCallView tre = form.getTre();
   
   boolean enableEdit = form.isEnableEdit();
 
%>
<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <table cellpadding=2 cellspacing=1>
            <c:field width="300" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroup" readonly="<%=policyTypeSelected%>" caption="Policy Class" name="stPolicyTypeGroupID" type="string" mandatory="true" presentation="standard"/>
            <c:field width="300" include="<%=form.getStPolicyTypeGroupID()!=null%>" changeaction="onChangePolicyType" lov="LOV_PolicyType" readonly="<%=policyTypeSelected%>" caption="Policy Type" name="stPolicyTypeID" type="string" mandatory="true" presentation="standard">
               <c:lovLink name="polgroup" link="stPolicyTypeGroupID" clientLink="false" />
            </c:field>
         </table>
      </td> 
   </tr>
   <c:evaluate when="<%=policyTypeSelected%>" >
      <tr>
         <td>
             <table cellpadding=2 cellspacing=1>
               <tr>
                  <td>
                     <table cellpadding=2 cellspacing=1>
                        <c:field name="tre.stInsuranceTreatyID" caption="Cash Call ID" type="string" presentation="standard" readonly="true" show="<%=tre.getStInsuranceTreatyID()!=null%>" />
                        <%--<c:field name="tre.stTreatyClass" width="200" caption="Class" type="string" lov="LOV_TreatyClass" presentation="standard" readonly="<%=tre.getStTreatyClass()!=null%>" changeaction="changeClass" /> --%>
                        <c:field name="tre.stTreatyName" width="250" caption="Name" type="string" presentation="standard" readonly="false" mandatory="true" />
                         <c:field name="tre.dtTreatyPeriodStart" caption="Period Start" type="date" presentation="standard" readonly="false" mandatory="true" />
                        <c:field name="tre.dtTreatyPeriodEnd" caption="Period End" type="date" presentation="standard" readonly="false" mandatory="true" />
                        <%--<c:field name="tre.dbTreatyPriority" caption="Priority" type="money5.0" presentation="standard" readonly="false" /> --%>
                        <c:field name="tre.stActiveFlag" caption="Active" type="check" presentation="standard" readonly="false" />
                        
                     </table>
                  </td>
                  <td>
                     <table cellpadding=2 cellspacing=1>
                       
                       
                     </table>
                  </td>
                  
               </tr>
            </table>
         </td>
      </tr>

      <tr>
         <td>

         </td>
      </tr>
      <tr>
         <td>
            <c:field name="detailIndex" type="string" hidden="true" />

            <c:evaluate when="<%=!form.isDrillMode()%>" >
               <c:listbox name="details" paging="true" >
                  <c:listcol columnClass="header" ><c:button text="+" event="newDetail" /></c:listcol>
                  <c:listcol columnClass="detail" ><c:button text="-" event="delDetail" clientEvent="<%="f.detailIndex.value="+index%>" /></c:listcol>
                  
                  
                  <c:listcol title="Cash Call Limit" ><c:field width="150" name="details.[$index$].dbTreatyLimit" type="money16.2" caption="Cash Call Limit" /></c:listcol>
                  <%--<c:listcol title="Treaty Limit 2" ><c:field width="150" name="details.[$index$].dbTreatyLimit2" type="money16.2" caption="Treaty Limit 2" /></c:listcol>
                  <c:listcol title="Premi Rate" ><c:field width="60" name="details.[$index$].dbPremiRatePct" type="money16.5" suffix="%" caption="Premi Rate" /></c:listcol>
                  <c:listcol title="Comission Rate" ><c:field width="60" name="details.[$index$].dbCommissionRatePct" type="money16.2" suffix="%" caption="Comission Rate"  /></c:listcol>
                  <c:listcol title="Comission Captive Rate" ><c:field width="60" name="details.[$index$].dbCommissionRateCaptivePct" type="money16.2" suffix="%" caption="Comission Captive Rate"  /></c:listcol>
                  <c:listcol title="Quota Share" ><c:field width="60" name="details.[$index$].dbQuotaSharePct" type="money16.2" suffix="%" caption="Quota Share"  /></c:listcol>
                  <c:listcol title="Parent" ><c:field width="100" name="details.[$index$].stParentIndex" lov="lovDetailParent" type="string" caption="Parent"  /></c:listcol>
                  <c:listcol title="TSI Share" ><c:field width="60" name="details.[$index$].dbTSIPct" type="money16.2" caption="TSI Share" suffix="%" /></c:listcol>
                  <c:listcol title="AR Premi Line" ><c:field width="100" name="details.[$index$].stARTrxLineID" type="string" caption="AR Premi Line"  /></c:listcol>
                  <c:listcol title="AR Claim Line" ><c:field width="100" name="details.[$index$].stARTrxLineIDClaim" type="string" caption="AR Claim Line"  /></c:listcol>
                  <c:listcol title="XOL Low" ><c:field width="150" name="details.[$index$].dbXOLLower" type="money16.2" caption="XOL Low"  /></c:listcol>
                  <c:listcol title="XOL Up" ><c:field width="150" name="details.[$index$].dbXOLUpper" type="money16.2" caption="XOL Up"  /></c:listcol>
		  <c:listcol title="TSI Max" ><c:field width="150" name="details.[$index$].dbTSIMax" type="money16.2" caption="TSI Max" /></c:listcol>
                  <c:listcol title="Coverage" ><c:field width="200" lov="LOV_Coverage2" name="details.[$index$].stInsuranceCoverID" type="string" caption="Coverage"  /></c:listcol>
                  <c:listcol title="Divide By Years Flags" ><c:field width="150" name="details.[$index$].stDivideByYearsFlags" type="check" caption="Divide By Years Flags" /></c:listcol>
                  <c:listcol title="Comm Factors Flags" ><c:field width="150" name="details.[$index$].stPremiumCommFactorFlags" type="check" caption="Comm Factors Flags" /></c:listcol>
               	  <c:listcol title="Comm Factors Limit" ><c:field width="60" name="details.[$index$].dbPremiumCommFactorLimit" type="money16.2" suffix="%" caption="Comm Factors Limit"  /></c:listcol>
                  <c:listcol title="Inward Capacity" ><c:field width="60" name="details.[$index$].dbInwardCapacityPct" type="money16.2" suffix="%" caption="Inward Capacity" /></c:listcol>
                  
                  <c:listcol title="Bypass Validation RI" ><c:field width="150" name="details.[$index$].stBypassValidationRI" type="check" caption="Bypass Validation RI" /></c:listcol>
               	--%>
                </c:listbox>
            </c:evaluate>

            <c:evaluate when="<%=form.isDrillMode()%>" >
               <c:button text="Back to Treaty Details" event="drillToggle" />
               <c:field name="memberIndex" type="string" hidden="true" />
               <c:listbox name="members" paging="true" >
                  <c:listcol columnClass="header" ><c:button text="+" event="newMember" /></c:listcol>
                  <c:listcol columnClass="detail" ><c:button text="-" event="delMember" clientEvent="<%="f.memberIndex.value="+index%>" /></c:listcol>
                  <c:listcol title="Company" ><c:field width="200" name="members.[$index$].stMemberEntityID" type="string" caption="Company" lov="LOV_InsuranceCompany" popuplov="true" /></c:listcol>
                   <c:listcol title="Reinsurer" ><c:field width="200" name="members.[$index$].stReinsuranceEntityID" type="string" caption="Reinsurer" lov="LOV_EntityOnly" popuplov="true" /></c:listcol>
                  <c:listcol title="Share" ><c:field width="60" name="members.[$index$].dbSharePct" type="money16.2" caption="Share" suffix="%" /></c:listcol>
                  <c:listcol title="Premi" ><c:field width="60" name="members.[$index$].dbPremiRate" type="money16.5" caption="Premi" suffix="%" /></c:listcol>
                  <%--<c:listcol title="Company" ><c:field name="members.[$index$].dbPremiAmount" type="money16.2" caption="Share" /></c:listcol>--%>
                  <c:listcol title="Auto Rate" ><c:field width="80" name="members.[$index$].stAutoRateFlag" type="check" caption="Auto Rate" /></c:listcol>
                  <c:listcol title="Use Rate" ><c:field name="members.[$index$].stUseRateFlag" type="check" caption="Use Rate" /></c:listcol>
                  <c:listcol title="Commision" ><c:field width="60" name="members.[$index$].dbRICommRate" type="money16.2" caption="Commision" suffix="%" /></c:listcol>
                  <c:listcol title="Commision Captive" ><c:field width="60" name="members.[$index$].dbRICommCaptiveRate" type="money16.2" caption="Commision Captive" suffix="%" /></c:listcol>
                  <%--<c:listcol title="Company" ><c:field name="members.[$index$].dbRICommAmount" type="money16.2" caption="Share" /></c:listcol>--%>
                  <%--<c:listcol title="Notes" ><c:field name="members.[$index$].stNotes" type="money16.2" caption="Notes" /></c:listcol>--%>
                  <%--<c:listcol title="Company" ><c:field name="members.[$index$].dbTSIAmount" type="money16.2" caption="Share" /></c:listcol>--%>
                  <c:listcol title="Commision ONR" ><c:field width="60" name="members.[$index$].dbRICommONRRate" type="money16.2" caption="Commision ONR" suffix="%" /></c:listcol>
                  <c:listcol title="Format RI Slip" ><c:field width="200" name="members.[$index$].stRISlipNoFormat" type="string" caption="Format RI Slip" /></c:listcol>
                  <c:listcol title="Max Comm Flag" ><c:field name="members.[$index$].stMaxCommissionFlags" type="check" caption="Use Rate" /></c:listcol>
                  <c:listcol title="Max Total Comm" ><c:field width="60" name="members.[$index$].dbMaxCommissionPct" type="money16.2" suffix="%" caption="Max Total Comm" /></c:listcol>
                  <c:listcol title="Discount" ><c:field width="60" name="members.[$index$].dbDiscountPct" type="money16.2" suffix="%" caption="Discount" /></c:listcol>
                  <c:listcol title="Commision > Max Total Comm" ><c:field width="60" name="members.[$index$].dbRICommMoreThanMaxCommRate" type="money16.2" caption="Commision" suffix="%" /></c:listcol>
                  <c:listcol title="Premi Excess Flag" ><c:field name="members.[$index$].stPremiExcessFlag" type="check" caption="Premi Excess Flag" /></c:listcol>
                  <c:listcol title="Paid To" ><c:field width="200" name="members.[$index$].stPaidToEntityID" type="string" caption="Paid To" lov="LOV_EntityOnly" popuplov="true" /></c:listcol>
                  <c:listcol title="Flags" ><c:field width="150" name="members.[$index$].stControlFlags" type="check" caption="Flags" /></c:listcol>
               	  
               	  
               </c:listbox>
            </c:evaluate>
         </td>
      </tr>
      <tr>
         <td align=center>           
            <c:button show="<%=enableEdit%>" text="Save" event="save" />
            <c:button text="Cancel" event="close" />
         </td>
      </tr>
   </c:evaluate>
</table>
</c:frame>
