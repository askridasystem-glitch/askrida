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

boolean cROpolicyRiskDetail = false;

final boolean isTotalEndorseMode = form.isTotalEndorseMode();
final boolean isPartialEndorseTSIMode = form.isPartialEndorseTSIMode();
final boolean isPartialEndorseRateMode = form.isPartialEndorseRateMode();
final boolean isDescriptionEndorseMode = form.isDescriptionEndorseMode();
final boolean isRestitutionEndorseMode = form.isRestitutionEndorseMode();
boolean isEndorseMode = (isTotalEndorseMode || isPartialEndorseTSIMode || isPartialEndorseRateMode || isDescriptionEndorseMode || isRestitutionEndorseMode);
boolean isRejectMode = form.isRejectMode();

if(isEndorseMode){
    cROpolicyRiskDetail = true;
}

if(isRestitutionEndorseMode){
    cROpolicyRiskDetail = false;
}

%>

<table cellpadding=2 cellspacing=1 class=header>
                                                            <tr>
                                                                <td colspan=3 class=header>
                                                                    <table cellpadding=2 cellspacing=1 class=row0 width="100%">
                                                                        <tr>
                                                                            <td>Potensi Subrogasi</td>
                                                                            <td>:</td>
                                                                            <td>
                                                                                <c:field caption="Subrogasi" name="selectedObject.stSubrogasiID" lov="LOV_SUBROGASI" type="string" width="100" mandatory="true" changeaction="refresh"/>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </td>
                                                            </tr>
                                                        </table>
                                                        <br>
                                                        <c:evaluate when="<%=form.getSelectedObject().getStSubrogasiID() != null%>">
                                                            <c:evaluate when="<%=form.getSelectedObject().getStSubrogasiID().equalsIgnoreCase("1")%>">
                                                                <c:fieldcontrol when="<%=cROpolicyRiskDetail%>" readonly="true">
                                                                    <table cellpadding=2 cellspacing=1 class=header>
                                                                        <tr>
                                                                            <td class=header>
                                                                                SUBROGASI
                                                                            </td>
                                                                        </tr>
                                                                        <tr class=row0>
                                                                            <td>
                                                                                <c:field name="objSubroIndex" type="string" hidden="true"/>
                                                                                <c:listbox name="selectedObject.subrogasi">
                                                                                    <c:listcol title="" columnClass="header">
                                                                                        <%-- <c:button text="+" event="onNewObjDeductible" validate="false" defaultRO="true"/>
                                                                                        --%>
                                                                                    </c:listcol>
                                                                                    <c:listcol title="" columnClass="detail">
                                                                                        <c:button text="-" event="onDeleteObjSubrogasi"
                                                                                                  clientEvent="f.objSubroIndex.value='$index$';" validate="false"
                                                                                                  defaultRO="true"/>
                                                                                    </c:listcol>
                                                                                    <c:listcol title="{L-ENGCategory-L}{L-INAKategori-L}"
                                                                                               name="stClaimCauseDesc"/>
                                                                                    <%--
                                                                                    <c:listcol title="{L-ENGDescription-L}{L-INADeskripsi-L}">
                                                                                        <c:field name="selectedObject.subrogasi.[$index$].stDescription" type="string"
                                                                                                 rows ="2" width="200" mandatory="false" readonly="false"/>
                                                                                    </c:listcol>--%>
                                                                                    <c:listcol title="{L-ENGCurrency-L}{L-INAMata Uang-L}">
                                                                                        <c:field lov="LOV_Currency" name="selectedObject.subrogasi.[$index$].stCurrencyCode"
                                                                                                 type="string" width="50" mandatory="false" readonly="false"/>
                                                                                    </c:listcol>
                                                                                    <c:listcol title="Kurs">
                                                                                        <c:field name="selectedObject.subrogasi.[$index$].dbCurrencyRate" type="money16.2" width="50"
                                                                                                 mandatory="false" readonly="false"/>
                                                                                    </c:listcol>
                                                                                    <c:listcol title="Estimasi Nilai">
                                                                                        <c:field name="selectedObject.subrogasi.[$index$].dbAmountEstimated" type="money16.2" width="100"
                                                                                                 mandatory="false" readonly="false"/>
                                                                                    </c:listcol>
                                                                                    <%--<c:listcol title="PCT">
                                                                                        <c:field name="selectedObject.subrogasi.[$index$].dbPct" type="money5.2" width="40"
                                                                                                 mandatory="false" readonly="false"/>%
                                                                                    </c:listcol>
                                                                                    <c:listcol title="{L-ENGType-L}{L-INAJenis-L}">
                                                                                        <c:field name="selectedObject.subrogasi.[$index$].stDeductType" lov="VS_DEDUCT"
                                                                                                 type="string|128" width="150" mandatory="false" readonly="false"/>
                                                                                    </c:listcol>
                                                                                    <c:listcol title="Min">
                                                                                        <c:field name="selectedObject.subrogasi.[$index$].dbAmountMin" type="money16.2"
                                                                                                 width="100" mandatory="false" readonly="false"/>
                                                                                    </c:listcol>
                                                                                    <c:listcol title="Max">
                                                                                        <c:field name="selectedObject.subrogasi.[$index$].dbAmountMax" type="money16.2"
                                                                                                 width="100" mandatory="false" readonly="false"/>
                                                                                    </c:listcol>
                                                                                    <c:listcol title="Time Excess">
                                                                                        <c:field name="selectedObject.subrogasi.[$index$].dbTimeExcess" type="money16.2"
                                                                                                 width="50" mandatory="false" readonly="false"/>
                                                                                    </c:listcol>
                                                                                    <c:listcol title="Unit">
                                                                                        <c:field name="selectedObject.subrogasi.[$index$].stTimeExcessUnit" type="string" lov="LOV_PeriodUnit"
                                                                                                 width="80" mandatory="false" readonly="false"/>
                                                                                    </c:listcol>
                                                                                    <c:listcol title="Dokumen">
                                                                                        <c:field name="selectedObject.subrogasi.[$index$].stFilePhysic" type="file"
                                                                                                 thumbnail="true" caption="File"/>
                                                                                    </c:listcol>--%>
                                                                                </c:listbox>
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td>
                                                                                ADD : <c:field width="550" lov="subrogasiAddLOV" caption="Subrogasi" name="subrogasiAddID" type="string"/>
                                                                                <c:button event="onNewObjSubrogasi" text="+" defaultRO="true"/>
                                                                            </td>
                                                                        </tr>

                                                                        <tr>
                                                                            <td>
                                                                                <c:button text="Apply To All Objects" confirm="Yakin Ingin Menyalin Ke Seluruh Objek ?"event="applySubrogasiToAllObjects" validate="false" defaultRO="true"/>
                                                                            </td>
                                                                        </tr>
                                                                    </table>
                                                                </c:fieldcontrol>
                                                            </c:evaluate>
                                                        </c:evaluate>
                                    
                                   
