<%@ page import="com.webfin.gl.model.AccountView2,
com.crux.lov.LOVManager,
com.crux.util.LOV,
com.crux.lang.LanguageManager,
com.webfin.gl.accounts.forms.AccountForm"%>
<%@ taglib prefix="c" uri="crux" %>

<c:frame>
    <%
    AccountForm form = (AccountForm) request.getAttribute("FORM");
    
    boolean ro = form.isReadOnly();
    
    final AccountView2 account = form.getAccount();
    
    boolean cabang = true;
    if(form.getStBranch()!=null){
        if(form.getStBranch().equalsIgnoreCase("00")){
            cabang = false;
        }
    }
    
    if(form.getStBranch()==null){
        cabang = false;
    }
    
    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:evaluate when="<%=form.isEditMode()%>" >
                        <c:field width="70" caption="Account ID" name="account.stAccountID" type="string" readonly="true" presentation="standard"/>
                        <c:field lov="LOV_Branch" caption="Cabang" name="account.stCostCenterCode" type="string" mandatory="true" readonly="true" presentation="standard"/>
                        <c:field width="70"  caption="Account Header" name="stAccountHeader" type="string|5" readonly="true" presentation="standard"/>
                        <c:field width="70"  caption="GL Code" name="stGlCode" type="string" type="string|5" readonly="true" presentation="standard" />        
                        <c:field width="20"  caption="No.Urut/Jenpol" name="stTypeCode" type="string" type="string|2" readonly="true" presentation="standard" />        
                        <c:field width="200" caption="Nama Bank" name="stEntID" descfield="stEntName" type="string" popuplov="true" lov="LOV_Entity" readonly="true" presentation="standard" clientchangeaction="selectCustomer2()"/>
                        <c:field width="200" caption="Account No" name="account.stAccountNo" type="string" readonly="true" presentation="standard"/>
                    </c:evaluate>
                    <c:evaluate when="<%=!form.isEditMode()%>" >
                        <c:field width="70" caption="Account ID" name="account.stAccountID" type="string" readonly="true" presentation="standard"/>
                        <c:field lov="LOV_Branch" caption="Cabang" name="account.stCostCenterCode" type="string" mandatory="true" presentation="standard"/>
                        <c:field width="70"  caption="Account Header" name="stAccountHeader" type="string|5" presentation="standard"/>
                        <c:field width="70"  caption="GL Code" name="stGlCode" type="string" type="string|5" presentation="standard" />        
                        <c:field width="20"  caption="No.Urut/Jenpol" name="stTypeCode" type="string" type="string|2" presentation="standard" />        
                        <c:field width="200" caption="Nama Bank" name="stEntID" descfield="stEntName" type="string" popuplov="true" lov="LOV_Entity" presentation="standard" clientchangeaction="selectCustomer2()"/>
                        <c:field width="200" caption="Account No" name="account.stAccountNo" type="string" readonly="true" presentation="standard"/>
                    </c:evaluate>
                    <c:field width="300" rows="3" readonly="false" caption="Description" name="account.stDescription" type="string" presentation="standard"/>
                    <c:field width="200" caption="No. Giro" name="account.stRekeningNo" type="string" presentation="standard"/>
                    <c:field width="200" caption="Noper" name="account.stNoper" type="string" presentation="standard"/>
                    <c:field caption="Delete Flag" name="account.stDeleted" type="check" readonly="<%=cabang%>" presentation="standard" />
                    <c:field caption="Enable Flag" name="account.stEnabled" type="check" readonly="<%=cabang%>" presentation="standard" />
                </table>
            </td>
        </tr>
        <tr>
            <td align=center>
                <c:evaluate when="<%=!ro%>" >
                    <c:button text="{L-ENGRefresh-L}{L-INARefresh-L}" event="refresh" validate="true"/>
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

<script>
function selectCustomer2() {
        var o = window.lovPopResult;
        document.getElementById('stGlCode').value = o.gl_code;
    }
</script>

