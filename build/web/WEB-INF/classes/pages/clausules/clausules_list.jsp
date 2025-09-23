<%@ page import="com.webfin.insurance.model.InsuranceClausulesView,
com.crux.lov.LOVManager,
com.crux.util.LOV,
com.crux.lang.LanguageManager,
com.webfin.master.clausules.form.ClausulesListForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="Clausules List" >
    
    <%
    final ClausulesListForm form = (ClausulesListForm) frame.getForm();
    final InsuranceClausulesView cl = form.getClausules();
    %>
    
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <c:field width="200" lov="LOV_Branch" caption="{L-ENGBranch-L}{L-INACabang-L}" name="stBranch" descfield="stBranchDesc" type="string" presentation="standard" changeaction="refresh"/>
                            <c:field width="200" lov="LOV_PolicyType" caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" name="stPolicyTypeID" descfield="stPolicyTypeDesc" type="string" presentation="standard" changeaction="refresh"/>
                            
                            <table cellpadding=2 cellspacing=1>
                                <tr>
                                    <td>

                                        <c:button text="Print" event="clickPrint" />

                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <c:listbox name="list" autofilter="true" paging="true" selectable="true" view="com.webfin.insurance.model.InsuranceClausulesView" >
                                            <c:listcol name="stInsuranceClauseID" title="" selectid="clausulesid" />
                                            <c:listcol name="stCostCenter" title="Cabang" />
                                            <c:listcol name="stPolicyTypeID" title="Policy Type" />
                                            <c:listcol filterable="true" name="stShortDescription" title="Description"/>
                                            <c:listcol filterable="true" name="stDescriptionNew" title="Wording"/>
                                            <c:listcol filterable="false" name="dtCreateDate" title="Tanggal Input"/>
                                            <c:listcol filterable="true" name="stCreateWho" title="User Input"/>
                                            <c:listcol filterable="false" name="stEntryUserName" title="User Input"/>
                                        </c:listbox>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <c:button text="Buat" event="clickCreate" />
                                        <c:button text="Ubah" event="clickEdit" />
                                        <c:button text="Lihat" event="clickView" />
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
