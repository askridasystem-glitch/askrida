<%@ page import="com.webfin.master.category.form.RiskMasterForm,
com.webfin.insurance.model.InsuranceRiskCategoryView"%>
<%@ taglib prefix="c" uri="crux" %>

<c:frame>
    <%
    RiskMasterForm form = (RiskMasterForm) request.getAttribute("FORM");
    
    boolean ro = form.isReadOnly();
    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:field width="200" readonly="true" caption="Risk ID" name="risk.stInsuranceRiskCategoryID" type="string" mandatory="true" presentation="standard"/>
                    <c:field width="200" readonly="false" caption="Risk Code" name="risk.stInsuranceRiskCategoryCode" type="string" mandatory="true" presentation="standard"/>
                    <c:field width="300" rows="3" readonly="false" caption="Description" name="risk.stDescription" type="string" mandatory="true" presentation="standard"/>
                    <c:field width="200" readonly="false" caption="Policy Type" name="risk.stPolicyTypeID" type="string" lov="LOV_PolicyType" mandatory="true" presentation="standard"/>
                    <c:field width="200" readonly="false" caption="Limit 0" name="risk.dbTreatyLimit0" type="money5.0" mandatory="true" presentation="standard"/>
                    <c:field width="200" readonly="false" caption="Limit 1" name="risk.dbTreatyLimit1" type="money5.0" mandatory="true" presentation="standard"/>
                    <c:field width="200" readonly="false" caption="Limit 2" name="risk.dbTreatyLimit2" type="money5.0" mandatory="true" presentation="standard"/>
                    <c:field width="200" readonly="false" caption="Limit 3" name="risk.dbTreatyLimit3" type="money5.0" mandatory="true" presentation="standard"/>
                    <c:field width="200" readonly="false" caption="Rate 1" name="risk.dbRate1" type="money5.0" mandatory="true" presentation="standard"/>
                    <c:field width="200" readonly="false" caption="Rate 2" name="risk.dbRate2" type="money5.0" mandatory="true" presentation="standard"/>
                    <c:field width="200" readonly="false" caption="Rate 3" name="risk.dbRate3" type="money5.0" mandatory="true" presentation="standard"/>
                    <c:field width="200" readonly="false" caption="Period Start" name="risk.dtPeriodStartDate" type="date" mandatory="true" presentation="standard"/>
                    <c:field width="200" readonly="false" caption="Period End" name="risk.dtPeriodEndDate" type="date" mandatory="true" presentation="standard"/>
                </table>
            </td>
        </tr>
        <tr>
            <td align=center>
                <c:evaluate when="<%=!ro%>" >
                    <c:button text="{L-ENGSave-L}{L-INASimpan-L}" event="save" validate="true"/>
                    <c:button text="{L-ENGCancel-L}{L-INABatal-L}" event="close" validate="false"/>
                </c:evaluate>
                <c:evaluate when="<%=ro%>" >
                    <c:button text="{L-ENGClose-L}{L-INATutup-L}" event="close" validate="false"/>
                </c:evaluate>
            </td>      
        </tr>
    </table>
</c:frame>
