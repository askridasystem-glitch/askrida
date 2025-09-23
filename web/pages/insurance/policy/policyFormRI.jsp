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
com.crux.web.form.Form,
com.crux.common.config.Config" %>
<%@ page import="com.crux.util.DateUtil" %>
<%@ taglib prefix="c" uri="crux" %>

<%

final PolicyForm form = (PolicyForm) request.getAttribute("policyForm");

//final PolicyForm form = (PolicyForm) frame.getForm();
 
final InsurancePolicyView policy = form.getPolicy();

final InsurancePolicyObjectView selectedObject = form.getSelectedObject();

boolean directPolicy = false;
if (policy.getStCoverTypeCode() != null){
    if (policy.getStCoverTypeCode().equalsIgnoreCase("COINSIN"))

    if (policy.getStCoverTypeCode().equalsIgnoreCase("DIRECT"))
        directPolicy = true;
}

%>

                                    
                                   <c:fieldcontrol when="<%=true%>" readonly="true" >
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
                                                            <td>TSI</td>
                                                            <td>:</td>
                                                            <%if(!policy.isStatusEndorse()){%>
                                                            <td align=right><%=JSPUtil.print(selectedObject.getDbObjectInsuredAmountShare(), 2)%>
                                                            </td>
                                                            <%}%>
                                                            <%if(policy.isStatusEndorse()){%>
                                                            <td align=right><%=JSPUtil.print(selectedObject.getDbObjectInsuredAmountShareEndorse(), 2)%>
                                                            </td>
                                                            <%}%>
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
                                                        <%--if(policy.isManualReinsuranceFlag()){--%>
                                                        <%if(false){%>
                                                        <tr>
                                                            <td colspan="3">
                                                                <font color="red">Note : U/W Request Spreading Manual R/I</font>
                                                            </td>
                                                        </tr>

                                                        <%}%>

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
                                                            <td><%if(trd.getTreatyDetail().getStInsuranceCoverID()!=null){%> &nbsp; &nbsp; - <%}%><%=trd.getStTreatyClassDesc()%>
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
                                        <c:field name="idxTreatySharesInst" type="string" hidden="true"/>


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
                                                                
                                                                 <%
                                                                        String buttonPerils = form.isShowRIPerils()?"Hide Per Perils":"Show Per Perils";
                                                                        String buttonRIInst = form.isShowRIInstallment()?"Hide Cicilan":"Show Cicilan";
                                                                 %>
                                                                 <c:button text="<%=buttonPerils%>"
                                                                          confirm="Yakin ingin melihat per perils ?" event="showRIPerils" />
                                                                 <c:button text="<%=buttonRIInst%>"
                                                                          confirm="Yakin ingin melihat cicilan reas ?" event="showRIInstallment" />


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
                                                            final boolean useRISlip = Tools.isYes(treatyType.getStReinsuranceSlipFlag());

                                                            final boolean hasShares = tredet.getShares().size() > 0;

                                                            final boolean isOR = tdr.isOR();

                                                            final String style = isOR ? "row2" : "row0";

                                                            boolean canChangeMember = freeMembers;

                                                            boolean hasRISlip = nonProportional || useRISlip;

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
                                                                                        
                                                                                        <tr>
                                                                                            <td valign=top>
                                                                                                <table cellpadding=2 cellspacing=1>
                                                                                                    <%--
                                    <c:field name="<%="treaties.["+i+"].details.["+j+"].dbTreatyLimit"%>" width="200" readonly="<%=!freeTSUI||!special_treaty%>" caption="<%=JSPUtil.print(label)%>" type="money16.2" presentation="standard" />
                                    --%>
                                                                                                    <%--
                                                                                                    <c:field name="<%="treaties.["+i+"].details.["+j+"].dbBaseTSIAmount"%>" width="200"
                                                                                                             readonly="<%=!special_treaty%>" caption="{L-ENGBase TSI-L}{L-INABase TSI-L}"
                                                                                                             type="money16.2" presentation="standard"/>
                                                                                                    --%>

                                                                                                    <c:field name="<%="treaties.["+i+"].details.["+j+"].dbTreatyLimit"%>" width="200"
                                                                                                             readonly="true" caption="{L-ENGTreaty Limit-L}{L-INALimit Treaty-L}"
                                                                                                             type="money16.2" presentation="standard"/>
                                                                                                    <c:field name="<%="treaties.["+i+"].details.["+j+"].dbTSIPct"%>"
                                                                                                             readonly="true" caption="TSI Share" type="money16.2"
                                                                                                             presentation="standard" width="60" suffix=" %"/>

                                                                                                     <c:evaluate when="<%=policy.isStatusInward()%>">
                                                                                                            <tr>
                                                                                                                <td>{L-ENGInward Capacity-L}{L-INAKapasitas Inward-L}</td>
                                                                                                                <td>:</td>
                                                                                                                <td><%=JSPUtil.print(tdr.getDbInwardCapacityPct(), 0)%> %</td>
                                                                                                            </tr>
                                                                                                     </c:evaluate>

                                                                                                    <%--  <c:field name="<%="treaties.["+i+"].details.["+j+"].dbTreatyLimitRatio"%>" readonly="<%=!special_treaty%>" caption="Limit Ratio" type="money16.2" presentation="standard" width="60" suffix=" %" />
                                                                                 --%>
                                                                                                    <c:field name="<%="treaties.["+i+"].details.["+j+"].dbTSIAmount"%>"
                                                                                                             caption="{L-ENGTSI Amount-L}{L-INAJumlah TSI-L}" width="200" type="money16.2"
                                                                                                             presentation="standard" readonly="true"/>

                                                                                                    <%--
                                                                                                    <c:field name="<%="treaties.["+i+"].details.["+j+"].dbTSIPctCalc"%>"
                                                                                                             readonly="<%=!special_treaty%>" caption="TSI Share Calc" type="money16.2"
                                                                                                             presentation="standard" width="60" suffix=" %"/>

                                                                                                    <c:field name="<%="treaties.["+i+"].details.["+j+"].dbTSIAmountCalc"%>"
                                                                                                             caption="{L-ENGTSI Amount-L}{L-INAJumlah TSI-L} Calc" width="200" type="money16.2"
                                                                                                             presentation="standard" readonly="<%=!special_treaty%>"/> --%>

                                                                                                    <c:field name="<%="treaties.["+i+"].details.["+j+"].stEditFlag"%>" readonly="false"
                                                                                                             caption="{L-ENGManual TSI-L}{L-INAManual TSI-L}" type="check"
                                                                                                             presentation="standard" readonly="true"/>
                                                                                                    <c:evaluate when="<%=tdr.isFacultative() || tdr.isFacultativeObligatory1() || tdr.isFacultativeObligatory3()%>">
                                                                                                             <tr>
                                                                                                                 <td>Apply To All Objects</td>
                                                                                                                 <td>:</td>
                                                                                                                 <td>
                                                                                                                     <c:button text="Apply All Member Fac"  confirm="Yakin ingin di apply?" event="applyRIMemberToAllObjects" defaultRO="true"/>
                                                                                                                     <c:button text="Apply All Rate & RI Slip" confirm="Yakin ingin di apply?" event="applyRIRateAndSlipToAll" defaultRO="true"/>
                                                                                                                 </td>
                                                                                                             </tr>
                                                                                                    </c:evaluate>

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
                                                                                                    <c:field name="<%="treaties.["+i+"].details.["+j+"].dbComissionRate"%>" readonly="false"
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

                                                                                                            <td>Use<br>Rate<br>
                                                                                                                 <c:field
                                                                                                                    name="<%="treaties.["+i+"].details.["+j+"].stUseRateFlag"%>"
                                                                                                                    type="check" caption="cek all" clientchangeaction="<%="docEl('idxTreaty').value="+i+";docEl('idxTreatyDetail').value="+j+";"%>"
                                                                                                                        changeaction="cekAllShareUseRate" readonly="false"/>
                                                                                                            </td>
                                                                                                            <td>Auto<br>Rate<br>
                                                                                                                <c:field
                                                                                                                    name="<%="treaties.["+i+"].details.["+j+"].stAutoRateFlag"%>"
                                                                                                                    type="check" caption="cek all" clientchangeaction="<%="docEl('idxTreaty').value="+i+";docEl('idxTreatyDetail').value="+j+";"%>"
                                                                                                                        changeaction="cekAllShareAutoRate" readonly="false"/>
                                                                                                            </td>
                                                                                                            </td>
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
                                                                                                            <td>ACC<br>
                                                                                                                <c:field
                                                                                                                    name="<%="treaties.["+i+"].details.["+j+"].stApprovedFlag"%>"
                                                                                                                    type="check" caption="cek all" clientchangeaction="<%="docEl('idxTreaty').value="+i+";docEl('idxTreatyDetail').value="+j+";"%>"
                                                                                                                        changeaction="cekAllShareApproved" readonly="false"/>

                                                                                                            </td>

                                                                                                            <td>INST</td>
                                                                                                            <td>Pilihan</td>
                                                                                                            <td>Jumlah</td>
                                                                                                            <td>Tanggal Binding</td>

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
                                                                                                                readonly="false"/></td>
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
                                                                                                                    readonly="<%=!canChangeMember%>"/>
                                                                                                                    <c:button text="Apply All" event="applyRISlipToALLMember" defaultRO="true"/>

                                                                                                                    </td>
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


                                                                                                            <td><c:field
                                                                                                                    name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].stInstallmentFlag"%>"
                                                                                                                        type="check" caption="INST" readonly="false" changeaction="showCicilan" /></td>

                                                                                                            <td><c:field
                                                                                                                    name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].stInstallmentOption"%>"
                                                                                                                        lov="LOV_INSTALLMENT_OPTIONS"
                                                                                                                width="80" type="string" caption="Pilihan" readonly="false"/></td>
                                                                                                            <td><c:field
                                                                                                                    name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].stInstallmentCount"%>"
                                                                                                                width="40" type="string" caption="Jumlah" readonly="false"/></td>
                                                                                                            <td><c:field
                                                                                                                    name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].dtBindingDate"%>"
                                                                                                                type="date" caption="Tanggal Binding" readonly="false"/></td>

                                                                                                        </tr>
                                                                                                        <c:evaluate when="<%=form.isShowRIPerils()%>">
                                                                                                            <tr align="center" class=row0>
                                                                                                                <td colspan=13>
                                                                                                                    <table>
                                                                                                                        <tr class=header>
                                                                                                                            <td colspan="2">Detil Premi R/I Per Coverage <%=JSPUtil.print(tredet.getStTreatyClassDesc())%> - <%=JSPUtil.print(ri.getEntity().getStEntityName())%></td>
                                                                                                                        </tr>
                                                                                                                        <tr class=header>
                                                                                                                                <td> Coverage </td>
                                                                                                                                <td>&nbsp; Premi Per Coverage</td>
                                                                                                                        </tr>
                                                                                                                        <%
                                                                                                                            int n = 0;
                                                                                                                            for (int m = 0; m < selectedObject.getCoverage().size(); m++) {

                                                                                                                            n = n+1;
                                                                                                                        %>


                                                                                                                            <tr class=row0>
                                                                                                                                <td>
                                                                                                                                    <c:field
                                                                                                                                        name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].stInsuranceCoverID"+ n %>"
                                                                                                                                            lov="LOV_Coverage2" width="400" type="string" caption="Cover ID 1"
                                                                                                                                        readonly="true"/>
                                                                                                                                </td>

                                                                                                                                <td>
                                                                                                                                    <c:field
                                                                                                                                        name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].dbPremiumCover"+ n%>"
                                                                                                                                        width="120" type="money16.2" caption="Premi COVER 1"
                                                                                                                                        readonly="<%=!canChangeMember%>"/>
                                                                                                                                </td>

                                                                                                                            </tr>
                                                                                                                            <%
                                                                                                                                }
                                                                                                                            %>
                                                                                                                    </table>
                                                                                                                </td>
                                                                                                            </tr>
                                                                                                        </c:evaluate>
                                                                                                        <c:evaluate when="<%=ri.isInstallment() && form.isShowRIInstallment()%>">
                                                                                                            <tr align="right" class=row0>
                                                                                                                <td colspan=16>
                                                                                                                    <table>
                                                                                                                        <tr class=header>
                                                                                                                            <td colspan="16" align=center> {L-ENGSimulated Installment-L}{L-INASimulasi Cicilan-L} <%=JSPUtil.print(tredet.getStTreatyClassDesc())%> - <%=JSPUtil.print(ri.getEntity().getStEntityName())%></td>
                                                                                                                        </tr>
                                                                                                                        <tr class=header>
                                                                                                                            <td><c:button text="+" show="<%=canChangeMember%>" event="addReinsInst"
                                                                                                                                    clientEvent="<%="docEl('idxTreaty').value="+i+";docEl('idxTreatyDetail').value="+j+";docEl('idxTreatyShares').value="+k+";"%>"
                                                                                                                                    defaultRO="true"/></td>
                                                                                                                             <td>Manual</td>
                                                                                                                             <td>No</td>
                                                                                                                             <td>Tanggal Tagihan</td>
                                                                                                                             <td>Premi</td>
                                                                                                                             <td>Komisi</td>
                                                                                                                         </tr>

                                                                                                                        <%
                                                                                                                            final DTOList riInstallment = ri.getInstallment();

                                                                                                                            for (int l = 0; l < riInstallment.size(); l++) {
                                                                                                                                InsurancePolicyReinsInstallmentView riInst = (InsurancePolicyReinsInstallmentView) riInstallment.get(l);
                                                                                                                         %>

                                                                                                                             <tr class=row0>
                                                                                                                                <td><c:button text="-" show="<%=canChangeMember%>"
                                                                                                                                                  clientEvent="<%="docEl('idxTreaty').value="+i+";docEl('idxTreatyDetail').value="+j+";docEl('idxTreatyShares').value="+k+";docEl('idxTreatySharesInst').value="+l+";"%>"
                                                                                                                                              event="deleteReinsInst" defaultRO="true"/></td>
                                                                                                                                <td><c:field
                                                                                                                                        name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].installment.["+l+"].stManualFlag"%>"
                                                                                                                                        width="130"  type="check"
                                                                                                                                        caption="Komisi"
                                                                                                                                    readonly="false"/></td>
                                                                                                                                <td><c:field
                                                                                                                                        name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].installment.["+l+"].stInstallmentNumber"%>"
                                                                                                                                        width="40"  type="string"
                                                                                                                                        caption="Komisi"
                                                                                                                                    readonly="true"/></td>
                                                                                                                                <td><c:field
                                                                                                                                        name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].installment.["+l+"].dtDueDate"%>"
                                                                                                                                        width="130"  type="date"
                                                                                                                                        caption="Tanggal Tagihan"
                                                                                                                                    readonly="false"/></td>
                                                                                                                                <td><c:field
                                                                                                                                        name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].installment.["+l+"].dbPremiAmount"%>"
                                                                                                                                        width="150"  type="money16.2"
                                                                                                                                        caption="Premi"
                                                                                                                                    readonly="false"/></td>
                                                                                                                                <td><c:field
                                                                                                                                        name="<%="treaties.["+i+"].details.["+j+"].shares.["+k+"].installment.["+l+"].dbRICommAmount"%>"
                                                                                                                                        width="130"  type="money16.2"
                                                                                                                                        caption="Komisi"
                                                                                                                                    readonly="false"/></td>
                                                                                                                             </tr>


                                                                                                                     <%}%>
                                                                                                                     <tr><td><br></td></tr>
                                                                                                                   </table>
                                                                                                                </td>
                                                                                                            </tr>


                                                                                                        </c:evaluate>
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
                                        </c:fieldcontrol>
