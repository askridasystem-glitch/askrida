<%@ page import="com.webfin.company.forms.CompanyMasterForm,
         com.crux.util.JSPUtil,
         com.webfin.company.model.CompanyView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="Company Form" >
    <%
                final CompanyMasterForm form = (CompanyMasterForm) request.getAttribute("FORM");
                final boolean entityMandatory = false;

                final CompanyView ent = form.getCompany();

                boolean ro = form.isReadOnly();

    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="company.stVSCode" caption="Code" type="integer" readonly="true" presentation="standard" />
                                <c:field name="company.stVSCompanyGroup" mandatory="true" caption="{L-ENGFull Name-L}{L-INANama Lengkap-L}" width="200" type="string|255"  presentation="standard"/>
                                <c:field name="company.stActiveFlag" caption="Centang utk Aktif" type="check"  presentation="standard" />
                            </table>
                        </td>
                    </tr>
                </table>

            </td>
        </tr>
        <tr>
            <td>
                <c:evaluate when="<%=!ro%>" >
                    <c:button text="{L-ENGSave-L}{L-INASimpan-L}" event="doSave" validate="true"/>
                    <c:button text="{L-ENGCancel-L}{L-INABatal-L}" event="doClose" validate="false"/>
                </c:evaluate>
                <c:evaluate when="<%=ro%>" >
                    <c:button text="{L-ENGClose-L}{L-INATutup-L}" event="doClose" validate="false"/>
                </c:evaluate>
            </td>
        </tr>
    </table>
</c:frame>
