<%@ page import="com.webfin.gl.accounts.forms.AccountForm,
         com.webfin.gl.model.AccountView2,
         com.webfin.gl.model.JournalHeaderView,
         com.crux.util.*"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="SETTLEMENT" >
    <%

                final AccountForm form = (AccountForm) request.getAttribute("FORM");

                final JournalHeaderView header = form.getTitipan();

                boolean readOnly = form.isReadOnly();

    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="titipan.stFilePhysic" caption="Upload Excel" type="file" thumbnail="true"
                                         readonly="false" presentation="standard"/>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <c:button show="true" text="Konversi" event="uploadExcelAccounts"/>
                        </td>
                    </tr>

                    <tr>
                        <td>
                            <c:field name="accountsindex" hidden="true" type="string"/>
                            <c:listbox name="details" >
                                <c:listcol title="" columnClass="header" >
                                </c:listcol>
                                <c:listcol title="" columnClass="detail" >
                                    <c:button text="-" event="delLine" clientEvent="f.accountsindex.value='$index$';" validate="false" enabled="<%=!readOnly%>"/>
                                </c:listcol>
                                <c:listcol title="Rekening" >
                                    <c:field name="details.[$index$].stAccountNo" width="130" mandatory="true" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Noper" >
                                    <c:field name="details.[$index$].stNoper" width="130" mandatory="true" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Keterangan" >
                                    <c:field name="details.[$index$].stDescription" width="300" mandatory="true" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                            </c:listbox>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>

        <td align=center>
            <c:evaluate when="<%=!readOnly%>" >
                <c:button text="Simpan" event="doSave" validate="true" confirm="Do you want to save ?" />
                <c:button text="Cancel" event="close" confirm="Do you want to cancel ?" />
            </c:evaluate>
            <c:evaluate when="<%=readOnly%>" >
                <c:button text="Close" event="close"/>
            </c:evaluate>
        </td>
    </table>

</c:frame>