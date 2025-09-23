<%@ page import="com.webfin.gl.model.AccountView2,
         com.webfin.gl.accounts.forms.AccountList"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="UPLOAD ACCOUNT" >

    <%

                AccountList form = (AccountList) request.getAttribute("FORM");

    %>

    <table cellpadding=2 cellspacing=1>

        <tr>
            <td>
                <c:field name="accountsID" hidden="true"/>
                <c:listbox name="list" selectable="true" paging="true" view="com.webfin.gl.model.AccountView2" autofilter="true">
                    <c:listcol filterable="true" name="stAccountID" title="ID"/>
                    <c:listcol filterable="true" name="stAccountNo" title="Account No"/>
                    <c:listcol filterable="true" name="stDescription" title="Description" />
                    <c:listcol filterable="true" name="stNoper" title="Noper" />
                    <c:listcol filterable="true" name="stRekeningNo" title="Rekno" />
                </c:listbox>
            </td>
        </tr>

        <tr>
            <td>
                <c:button  text="Upload Excel" event="clickUploadExcel" />
                <c:button  text="Upload Noper" event="clickUploadNoper" />
            </td>
        </tr>
    </table>

</c:frame>