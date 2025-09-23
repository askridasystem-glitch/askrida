<%@ page import="com.webfin.gl.model.AccountView2,
com.crux.lov.LOVManager,
com.crux.util.LOV,
com.crux.lang.LanguageManager,
com.webfin.gl.accounts.forms.AccountList"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="ACCOUNT LIST" >
    
    <%
    final AccountList form = (AccountList) frame.getForm();
    
    boolean cabang = true;
    
    if(form.getBranch()!=null){
        if(form.getBranch().equalsIgnoreCase("00")){
            cabang = false;
        }
    }
    
    if(form.getBranch()==null){
        cabang = false;
    }
    %>
    
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <c:field name="branch" type="string" caption="Branch" lov="LOV_Branch" presentation="standard" width="200" changeaction="refresh"/>
                            <table cellpadding=2 cellspacing=1>                                
                                <tr>
                                    <td>
                                        <c:listbox name="list" autofilter="true" paging="true" selectable="true" view="com.webfin.gl.model.AccountView2" >
                                            <c:listcol name="stAccountID" title="" selectid="accountid" />
                                            <c:listcol filterable="true" name="stAccountNo" title="Account No"/>
                                            <c:listcol filterable="true" name="stDescription" title="Description" />
                                            <c:listcol filterable="true" name="stNoper" title="Noper" />
                                            <c:listcol filterable="true" name="stRekeningNo" title="Rekno" />
                                            <c:listcol name="stAccountID" title="Account ID" />
                                        </c:listbox>
                                    </td>
                                </tr>
                                
                                <tr>
                                    <td>
                                        <c:evaluate when="<%=!cabang%>" >
                                            <c:button text="Create" event="clickCreate" />
                                        </c:evaluate>
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
