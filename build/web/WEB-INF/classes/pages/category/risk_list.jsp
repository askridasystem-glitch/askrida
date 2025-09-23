<%@ page import="com.webfin.insurance.model.InsuranceRiskCategoryView,
com.crux.lov.LOVManager,
com.crux.util.LOV,
com.crux.lang.LanguageManager,
com.webfin.master.category.form.RiskListForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="RISK CATEGORY LIST" >
    
    <%
    final RiskListForm form = (RiskListForm) frame.getForm();
    %>
    
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <c:field width="200" lov="LOV_PolicyType" caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" name="stPolicyTypeID" descfield="stPolicyTypeDesc" type="string" presentation="standard" changeaction="refresh"/>
                            
                            <table cellpadding=2 cellspacing=1>
                                <tr>
                                    <td>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <c:listbox name="list" autofilter="true" paging="true" selectable="true" view="com.webfin.insurance.model.InsuranceRiskCategoryView" >
                                            <c:listcol name="stInsuranceRiskCategoryID" title="" selectid="riskid" />
                                            <c:listcol filterable="true" name="stInsuranceRiskCategoryCode" title="Risk Code"/>
                                            <c:listcol filterable="true" name="stDescription" title="Description" />
                                            <c:listcol name="stPolicyTypeDesc" title="Policy Type" />
                                            <c:listcol name="dbTreatyLimit0" title="Limit 0"/>
                                            <c:listcol name="dbTreatyLimit1" title="Limit 1"/>
                                            <c:listcol name="dbTreatyLimit2" title="Limit 2"/>
                                            <c:listcol name="dbTreatyLimit3" title="Limit 3"/>
                                            <c:listcol name="dbRate1" title="Rate 1"/>
                                            <c:listcol name="dbRate2" title="Rate 2"/>
                                            <c:listcol name="dbRate3" title="Rate 3"/>
                                            <c:listcol name="dtPeriodStartDate" title="Period Start"/>
                                            <c:listcol name="dtPeriodEndDate" title="Period end"/>
                                        </c:listbox>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <c:button text="Create" event="clickCreate" />
                                        <c:button text="Edit" event="clickEdit" />
                                        <c:button text="View" event="clickView" />
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
    
</c:frame>
