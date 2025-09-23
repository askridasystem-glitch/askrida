<%@ page import="com.crux.util.JSPUtil,
                 com.webfin.insurance.validator.InsurancePolicyValidator,
                 com.crux.util.LOV,
                 com.crux.util.DTOList,
                 com.crux.common.controller.FormTab,
                 com.crux.common.model.DTO,
                 com.webfin.insurance.model.*,
                 com.crux.util.BDUtil,
                 com.webfin.entity.model.EntityView,
                 com.webfin.entity.model.EntityAddressView,
                 com.webfin.gl.ejb.CostCenterManager"%>
<% final JSPUtil jspUtil = new JSPUtil(request, response,"INSURANCE POLICY");

   final InsurancePolicyView pol = (InsurancePolicyView)request.getAttribute("POLICY");
   final LOV ccyList = (LOV) request.getAttribute("CCY_LIST");
   final LOV itemLOV = (LOV) request.getAttribute("ITEM_LIST");
   final LOV busrcLOV = (LOV) request.getAttribute("BUSINESS_SOURCE");
   final LOV regLOV = (LOV) request.getAttribute("REGION");
   final DTOList details = (DTOList)pol.getDetails();
   final DTOList objects = pol.getObjects();

   final boolean roMode = pol.isUnModified();

   int cf = pol.isUnModified() ? JSPUtil.READONLY:0;

   final boolean posted = "Y".equalsIgnoreCase(pol.getStPostedFlag());

   final FormTab tab = pol.getTab();



%>
         <input type=hidden name=delindex value="">
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr><td>POLICY NO</td><td>:</td><td><%=jspUtil.getInputText("polno",InsurancePolicyValidator.policyNo,pol.getStPolicyNo(),200,JSPUtil.MANDATORY|cf )%></td></tr>
                     <tr><td>COST CENTER</td><td>:</td><td><%=jspUtil.getInputSelect("costcenter|Cost Center|string",null,CostCenterManager.getInstance().getCostCenterLOV().setLOValue(pol.getStCostCenterCode()),400,cf|JSPUtil.MANDATORY )%></td></tr>
                     <tr><td>POLICY DATE</td><td>:</td><td><%=jspUtil.getInputText("poldate|Policy Date|date",null,pol.getDtPolicyDate(),200,cf|JSPUtil.NOTEXTMODE|JSPUtil.MANDATORY )%></td></tr>
                     <tr><td>POLICY TYPE</td><td>:</td><td><%=jspUtil.print(pol.getStPolicyTypeDesc())%></td></tr>
                     <tr><td>DESCRIPTION</td><td>:</td><td><%=jspUtil.getInputText("desc",InsurancePolicyValidator.policyDesc,pol.getStDescription(),400,JSPUtil.MANDATORY|cf )%></td></tr>
                     <%--<tr><td>Amount</td><td>:</td><td><%=jspUtil.print(pol.getDbAmount())%></td></tr>--%>
                     <tr><td>CURRENCY</td><td>:</td><td>
                        <%=jspUtil.getInputSelect("ccy",InsurancePolicyValidator.policyCCy,ccyList.setLOValue(pol.getStCurrencyCode()),50,JSPUtil.MANDATORY|cf )%>
                        Rate : <%=jspUtil.getInputText("ccyrate|Currency Exchange Rate|money16.2",null,pol.getDbCurrencyRate(),60,cf | JSPUtil.MANDATORY)%> 
                     </td></tr>

                     <tr><td>BUSINESS SOURCE</td><td>:</td><td><%=jspUtil.getInputSelect("bussrc",null,busrcLOV.setLOValue(pol.getStBusinessSourceCode()),150,JSPUtil.MANDATORY|cf )%></td></tr>
                     <tr><td>REGION</td><td>:</td><td><%=jspUtil.getInputSelect("reg",null,regLOV.setLOValue(pol.getStRegionID()),150,JSPUtil.MANDATORY|cf )%></td></tr>
                     <tr><td>CAPTIVE</td><td>:</td><td><%=jspUtil.getInputCheck("captive",pol.getStCaptiveFlag(),"",JSPUtil.MANDATORY|cf )%></td></tr>
                     <tr><td>INWARD</td><td>:</td><td><%=jspUtil.getInputCheck("inward",pol.getStInwardFlag(),"",JSPUtil.MANDATORY|cf )%></td></tr>



                     <tr><td>INSURED AMOUNT</td><td>:</td><td><%=jspUtil.getInputText("insured_amt|Insured Amount|money16.2",null,pol.getDbInsuredAmount(),200,cf | JSPUtil.READONLY|JSPUtil.NOTEXTMODE)%></td></tr>
                     <tr><td>PREMI RATE</td><td>:</td><td><%=jspUtil.getInputText("premi_rate|Premi Rate|money16.2",null,pol.getDbPremiRate(),50,cf )%></td></tr>
                     <tr><td>PREMI (BASE)</td><td>:</td><td><%=jspUtil.getInputText("premibase|Base Premi|money16.2",null,pol.getDbPremiBase(),200,JSPUtil.MANDATORY|cf )%></td></tr>
                     <tr><td>PREMI (TOTAL)</td><td>:</td><td><%=jspUtil.getInputText("premitot|Total Premi|money16.2",null,pol.getDbPremiTotal(),200,JSPUtil.READONLY|cf|JSPUtil.NOTEXTMODE )%></td></tr>
                     <tr><td>PREMI (NETTO)</td><td>:</td><td><%=jspUtil.getInputText("preminet|Premi Netto|money16.2",null,pol.getDbPremiNetto(),200,JSPUtil.READONLY|cf|JSPUtil.NOTEXTMODE )%></td></tr>

                     <tr><td>PERIOD START</td><td>:</td><td><%=jspUtil.getInputText("perstart|Period Start|date",null,pol.getDtPeriodStart(),200,cf|JSPUtil.NOTEXTMODE )%></td></tr>
                     <tr><td>PERIOD STOP</td><td>:</td><td><%=jspUtil.getInputText("perend|Period End|date",null,pol.getDtPeriodEnd(),200,cf|JSPUtil.NOTEXTMODE )%></td></tr>
                     <tr><td>PERIOD LENGTH</td><td>:</td><td><%=jspUtil.getInputText("perlen|Period Length|string",null,pol.getStPeriodLength(),200,JSPUtil.READONLY|cf|JSPUtil.NOTEXTMODE )%></td></tr>
                     <tr><td>POST ALL</td><td>:</td><td><input type=checkbox name=posted <%=posted?"checked":""%> <%=posted?"disabled":""%>></td></tr>
                  </table>
               </td>
            </tr>

            <tr>
               <td>
                  <%=jspUtil.formTabOpen(tab,"INS_POL_CHG_TAB")%>


<% if (jspUtil.tabStart(tab,InsurancePolicyView.TAB_OBJECTS)) {%>
<%
   final InsurancePolicyObjectView selected = (InsurancePolicyObjectView) objects.getSelected();
%>
   <table cellpadding=2 cellspacing=1>
      <tr>
         <td>Objects</td>
         <td>:</td>
         <td><%=jspUtil.getInputSelect("objsel|Object|string||onChange:INS_POL_CHG_OBJ",null,objects,400,0)%></td>
      </tr>
   </table>

   <table cellpadding=2 cellspacing=1>
      <tr>
         <td><%=jspUtil.getButtonNormal("b","Add","f.EVENT.value='INS_POL_ADD_OBJ';f.submit();")%></td>
         <td><%=jspUtil.getButtonNormal("b","Delete","f.EVENT.value='INS_POL_DEL_OBJ';f.submit();")%></td>
      </tr>
   </table>

<% if (selected!=null) {%>

<% if (selected instanceof InsurancePolicyVehicleView) {%>
<%
   final InsurancePolicyVehicleView veh = (InsurancePolicyVehicleView) selected;

%>
   <table cellpadding=2 cellspacing=1>
      <tr class=header><td>VEHICLE DATA</td></tr>
      <tr class=row0><td>
         <input type=hidden name=vehindex value=<%=objects.indexOf(selected)%>>
         <table cellpadding=2 cellspacing=1>
            <tr><td>Object ID</td><td>:</td><td><%=jspUtil.print(veh.getStPolicyObjectID())%></td></tr>
            <tr><td>Vehicle Type</td><td>:</td><td><%=jspUtil.getInputText("vehtype|Vehicle Type|string|128",null,veh.getStVehicleTypeDesc(), 300, cf|JSPUtil.MANDATORY)%></td></tr>
            <tr><td>Reg No</td><td>:</td><td><%=jspUtil.getInputText("vregno|Registration No|string|16",null,veh.getStPoliceRegNo(), 100, cf)%></td></tr>
            <tr><td>Year</td><td>:</td><td><%=jspUtil.getInputText("vyear|Year|integer|4",null,veh.getLgYearProduction(), 50, cf)%></td></tr>
            <tr><td>Chassis No</td><td>:</td><td><%=jspUtil.getInputText("vchassno|Chassis No|string|64",null,veh.getStChassisNo(), 200, cf)%></td></tr>
            <tr><td>Engine No</td><td>:</td><td><%=jspUtil.getInputText("vengno|Engine No|string|64",null,veh.getStEngineNo(), 200, cf)%></td></tr>
            <tr><td>Seat Num</td><td>:</td><td><%=jspUtil.getInputText("vseatnum|Seat Number|integer|4",null,veh.getLgYearProduction(), 50, cf)%></td></tr>
            <tr><td>Insured Amount</td><td>:</td><td><%=jspUtil.getInputText("vinsamount|Insured Amount|money16.2",null,veh.getDbInsuredAmount(), 200, cf|JSPUtil.MANDATORY)%></td></tr>
            <tr><td>Premi Rate</td><td>:</td><td><%=jspUtil.getInputText("vpremirate|Premi Rate|money16.2",null,veh.getDbPremiRate(), 100, cf)%></td></tr>
            <tr><td>Premi Amount</td><td>:</td><td><%=jspUtil.getInputText("vpremiamount|Premi Amount|money16.2",null,veh.getDbPremiAmount()==null?BDUtil.zero:veh.getDbPremiAmount(), 200, cf|JSPUtil.MANDATORY)%></td></tr>
            <tr><td>Premi Total</td><td>:</td><td><%=jspUtil.getInputText("vpremitotal|Premi Total|money16.2",null,veh.getDbPremiTotal(), 200, cf|JSPUtil.READONLY|JSPUtil.NOTEXTMODE )%></td></tr>
         </table>
      </td></tr>
   </table>


<% }%>

<%
   final DTOList clausules = selected.getClausules();
   if (clausules !=null) {
%>

   <table cellpadding=2 cellspacing=1>
      <tr class=header>
         <td>Clause</td>
         <td>Rate Type</td>
         <td>Rate</td>
         <td>Amount</td>
      </tr>
      <input type=hidden name=oclindex value=<%=objects.indexOf(selected)%>>

<%

   for (int i = 0; i < clausules.size(); i++) {
      InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);

%>
      <tr clas=row<%=i%2%>>
         <td><%=jspUtil.print(icl.getStShortDescription())%></td>
         <td><%=jspUtil.print(icl.getStRateType())%></td>
         <td><%=jspUtil.getInputText("oclrate"+i+"|Rate|money16.2",null,icl.getDbRate(),60,cf)%></td>
         <td><%=jspUtil.getInputText("oclrateamount"+i+"|Rate|money16.2",null,icl.getDbAmount(),120,cf|JSPUtil.NOTEXTMODE|JSPUtil.MANDATORY)%></td>
      </tr>
<% } %>
   </table>
<% } %>

<% } %>
   <%=jspUtil.tabStop(tab,InsurancePolicyView.TAB_OBJECTS)%>
<% } %>

<% if (jspUtil.tabStart(tab,InsurancePolicyView.TAB_OWNER)) {%>
<%
   final EntityView owner = pol.getOwner().getEntity();

   if (owner!=null) {

   final DTOList addresses = owner.getAddresses();
%>
   <input type=hidden name=ownerinfo value="x">
   <table cellpadding=2 cellspacing=1>
      <tr class=header>
         <td>CUSTOMER INFORMATION</td>
      </tr>
      <tr class=row0>
         <td>
            <table cellpadding=2 cellspacing=1>
               <tr><td>Customer Number</td><td>:</td><td><%=jspUtil.getInputText("oxcustno|Customer Number|string",null,null,100,cf|JSPUtil.MANDATORY)%></td></tr>
               <tr><td>Full Name</td><td>:</td><td><%=jspUtil.getInputText("oxfullname|Full Name|string|255",null,owner.getStEntityName(),300,cf|JSPUtil.MANDATORY)%></td></tr>
               <tr><td>First Name</td><td>:</td><td><%=jspUtil.getInputText("oxfirstname|First Name|string|32",null,owner.getStFirstName(),100,cf|JSPUtil.MANDATORY)%></td></tr>
               <tr><td>Middle Name</td><td>:</td><td><%=jspUtil.getInputText("oxmidname|Middle Name|string|32",null,owner.getStMiddleName(),100,cf|JSPUtil.MANDATORY)%></td></tr>
               <tr><td>Last Name</td><td>:</td><td><%=jspUtil.getInputText("oxlastname|Last Name|string|32",null,owner.getStLastName(),100,cf|JSPUtil.MANDATORY)%></td></tr>
               <tr><td>Title</td><td>:</td><td><%=jspUtil.getInputText("oxtitle|Title|string|16",null,owner.getStTitle(),80,cf|JSPUtil.MANDATORY)%></td></tr>
               <tr><td>Sex</td><td>:</td><td><%=jspUtil.getInputText("oxsex_id|Sex|string",null,owner.getStSexID(),50,cf|JSPUtil.MANDATORY)%></td></tr>
               <tr><td>Birth Date</td><td>:</td><td><%=jspUtil.getInputText("oxbirth_date|Birth Date|date",null,owner.getDtBirthDate(),100,cf|JSPUtil.MANDATORY)%></td></tr>
            </table>
         </td>
      </tr>
   </table>

   <table cellpadding=2 cellspacing=1>
<%
   for (int i = 0; i < addresses.size(); i++) {
      EntityAddressView adr = (EntityAddressView) addresses.get(i);
%>
      <tr>
         <td>
            <table cellpadding=2 cellspacing=1>
               <tr class=header>
                  <td>ADDRESS <%=i+1%></td>
               </tr>
               <tr class=row0>
                  <td>
                     <table cellpadding=2 cellspacing=1>
                        <tr><td>Address</td><td>:</td><td><%=jspUtil.getInputText("axaddress|Address|string|255",null,adr.getStAddress(),400,cf|JSPUtil.MANDATORY)%></td></tr>
                     </table>
                  </td>
               </tr>
            </table>
         </td>
      </tr>
      <tr>
         <td>
            <%=jspUtil.getButtonEvent("Add Address","INS_POL_OW_ADD_ADDR")%>
         </td>
      </tr>
<% } %>
   </table>

   <% } %>

   <%=jspUtil.tabStop(tab,InsurancePolicyView.TAB_OWNER)%>
<% } %>

<% if (jspUtil.tabStart(tab,InsurancePolicyView.TAB_PREMI)) {%>

<% if (objects!=null) {%>
Insured Objects :<br>
<table cellpadding=2 cellspacing=1>
   <tr class=header>
      <td>NO</td>
      <td>OBJECT</td>
      <td>INSURED</td>
      <td>RATE</td>
      <td>PREMI</td>
   </tr>
<%
   for (int i = 0; i < objects.size(); i++) {
      InsurancePolicyObjectView obj = (InsurancePolicyObjectView) objects.get(i);
%>
   <tr class=row<%=i%2%>>
      <td><%=i%></td>
      <td><%=jspUtil.print(obj.getStObjectDescription())%></td>
      <td><%=jspUtil.print(obj.getDbObjectInsuredAmount())%></td>
      <td><%=jspUtil.print(obj.getDbObjectPremiRate())%></td>
      <td><%=jspUtil.print(obj.getDbObjectPremiTotalAmount())%></td>
   </tr>
<% } %>
</table><br><br>

<% }%>
                  <table cellpadding=2 cellspacing=1>
                     <tr class=header>
                        <td></td>
                        <td>ITEMS</td>
                        <td>DESCRIPTION</td>
                        <td>AMOUNT</td>
                        <td></td>
                     </tr>
<%
   for (int i = 0; i < details.size(); i++) {
      InsurancePolicyItemsView pi = (InsurancePolicyItemsView) details.get(i);
      final boolean isComission = pi.isComission();
%>
                     <tr class=row<%=i%2%>>
                        <td><%=roMode?"":jspUtil. getButtonNormal("brecalc","-","f.delindex.value="+i+";f.EVENT.value='INS_POL_DEL_ITEM';f.submit();")%></td>
                        <td><%=jspUtil.getInputSelect("i_item"+i,InsurancePolicyValidator.policyItem,itemLOV.setLOValue(pi.getStInsItemID()),100,JSPUtil.READONLY|cf )%></td>
                        <td><%=jspUtil.getInputText("i_desc"+i,InsurancePolicyValidator.policyItemDesc,pi.getStDescription(),200,JSPUtil.MANDATORY|cf )%></td>
                        <td><%=jspUtil.getInputText("i_amount"+i,InsurancePolicyValidator.policyItemAmt,pi.getDbAmount(),100,JSPUtil.MANDATORY|cf )%></td>
                        <td>
                           <table cellpadding=2 cellspacing=1>
                              <tr>
                                 <td>
                                   <%if (isComission) {%>
                                    <div id=i_ent_name_cap<%=i%>><%=pi.getStEntityName()==null?"Select Agent":pi.getStEntityName()%></div>
                                    <input type=hidden name=i_ent_id<%=i%> value="<%=jspUtil.print(pi.getStEntityID())%>">
                                    <input type=hidden name=i_ent_name<%=i%> value="<%=jspUtil.print(pi.getStEntityName())%>">
                                    <% }%>
                                 </td>
                                 <td align=right><%if (isComission) {%><%=roMode?"":jspUtil. getButtonNormal("bsagent","...","selectAgent("+i+")")%><% }%></td>
                              </tr>
                           </table>
                        </td>
                     </tr>
<% } %>
                     <tr>
                        <td><%=roMode?"":jspUtil.getButtonNormal("brecalc","+","f.EVENT.value='INS_POL_NEW_ITEM';f.submit();")%></td>
                        <td><%=roMode?"":jspUtil.getInputSelect("i_item_n",InsurancePolicyValidator.policyItem,itemLOV.setLOValue(null),100,0 )%></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                     </tr>
                  </table>

<%=jspUtil.tabStop(tab,InsurancePolicyView.TAB_PREMI)%>
<%}%>

<% if (jspUtil.tabStart(tab, InsurancePolicyView.TAB_CLAUSULES)) {%>
   <table cellpadding=2 cellspacing=1>
      <tr class=header>
         <td></td>
         <td>Clause</td>
         <td>Rate Type</td>
         <td>Rate</td>
         <td>Amount</td>
      </tr>
<%
   final DTOList clausules = pol.getClausules();

   for (int i = 0; i < clausules.size(); i++) {
      InsurancePolicyClausulesView icl = (InsurancePolicyClausulesView) clausules.get(i);

      final boolean selected = icl.isSelected();

      int rf1 = selected?0:JSPUtil.READONLY;

      final boolean standardclause = icl.isStandard();

      if (standardclause) rf1=JSPUtil.READONLY;
%>
      <tr clas=row<%=i%2%>>
         <td><input name=clselect<%=i%> type=checkbox <%=selected?"checked":""%>></td>
         <td><%=jspUtil.print(icl.getStShortDescription())%></td>
         <td><%=jspUtil.print(icl.getStRateType())%></td>
         <td><%=jspUtil.getInputText("clrate"+i+"|Rate|money16.2",null,icl.getDbRate(),60,rf1|JSPUtil.NOTEXTMODE)%></td>
         <td><%=jspUtil.getInputText("clrateamount"+i+"|Rate|money16.2",null,icl.getDbAmount(),120,rf1|JSPUtil.NOTEXTMODE|JSPUtil.MANDATORY)%></td>
      </tr>
<% } %>
   </table>
<%=jspUtil.tabStop(tab,InsurancePolicyView.TAB_CLAUSULES)%>
<% } %>

<%=jspUtil.formTabClose(tab)%>
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
                  <%=roMode?"":jspUtil. getButtonSubmit("brecalc","Recalculate","f.EVENT.value='INS_POL_RECALCULATE'")%>
                  <%=roMode?"":jspUtil. getButtonSubmit("bs","Save","f.EVENT.value='INS_POL_SAVE'")%>
                  <%=jspUtil. getButtonSubmit("bc",roMode?"Back":"Cancel","f.EVENT.value='INS_POL'")%>
               </td>
            </tr>
         </table>
<script>
   function selectAgent(i) {
      openDialog('so.ctl?EVENT=INS_AGENT_SELECT', 400,400,
         function (o) {
            if (o!=null) {
               document.getElementById('i_ent_id'+i).value=o.entid;
               document.getElementById('i_ent_name'+i).value=o.entname;
               document.getElementById('i_ent_name_cap'+i).innerText=o.entname;
            }
         }
      );
   }
</script>
<%=jspUtil.release()%>