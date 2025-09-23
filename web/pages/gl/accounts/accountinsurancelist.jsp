<%@ page import="com.webfin.gl.model.AccountInsuranceView,
com.crux.lov.LOVManager,
com.crux.util.LOV,
com.crux.lang.LanguageManager,
com.webfin.gl.accounts.forms.AccountInsuranceList"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="ACCOUNT LIST" >
    
    <%
    final AccountInsuranceList form = (AccountInsuranceList) frame.getForm();
    %>
    
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>                                
                                <tr>
                                    <td>
                                        <c:listbox name="list" autofilter="true" paging="true" selectable="true" view="com.webfin.gl.model.AccountInsuranceView" >
                                            <c:listcol name="stGLInsuranceID" title="" selectid="glinsid" />
                                             <c:listcol filterable="true" name="stCostCenterCode" title="Cabang" />
                                            <c:listcol filterable="true" name="stAccountNo" title="Account No"/>
                                            <c:listcol filterable="true" name="stDescription" title="Description" />
                                            <c:listcol name="stActiveFlag" title="Act" flag="true"/>
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
