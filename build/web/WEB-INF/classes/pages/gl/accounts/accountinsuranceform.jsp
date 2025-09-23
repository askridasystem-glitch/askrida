<%@ page import="com.webfin.gl.model.AccountInsuranceView,
com.crux.lov.LOVManager,
com.crux.util.LOV,
com.crux.lang.LanguageManager,
com.webfin.gl.accounts.forms.AccountInsuranceForm"%>
<%@ taglib prefix="c" uri="crux" %>

<c:frame>
    <%
    AccountInsuranceForm form = (AccountInsuranceForm) request.getAttribute("FORM");
    
    boolean ro = form.isReadOnly();
    
    final AccountInsuranceView account = form.getAccount();
    
    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:field width="50" caption="Account Header" name="account.stAccountNo" type="string|5" mandatory="true" presentation="standard"/>
                    <c:field width="300" caption="Description" name="account.stDescription" type="string" mandatory="true" presentation="standard"/>
                    <c:field width="300" caption="Cabang" name="account.stCostCenterCode" lov="LOV_CostCenter" type="string" presentation="standard"/>

                    <tr>
                        <td></td>
                        <td colspan="2">
                            <b>* Kosongkan Kode Cabang Untuk Membuat Akun Pada Semua Cabang</b>
                        </td>
                    </tr>
                    <c:field caption="Active" name="account.stActiveFlag" type="check" presentation="standard" />
                    <tr>
                        <td></td>
                        <td colspan="2">
                            <b>* Centang untuk mengaktifkan blocking account</b>
                        </td>
                    </tr>
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