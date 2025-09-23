<%@ page import="com.crux.web.controller.SessionManager"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="DATA AGEN" >
    <%            //boolean ENT_MASTER_NAV_BR = SessionManager.getInstance().hasResource("ENT_MASTER_NAV_BR");
%>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:field name="branch" caption="{L-ENGBranch-L}{L-INACabang-L}" type="string" lov="LOV_Branch" presentation="standard" changeaction="refresh" />
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <c:listbox autofilter="true" name="list" selectable="true" paging="true" view="com.webfin.entity.model.EntityView" >
                    <c:listcol name="stEntityID" title="ID" selectid="entityid"/>
                    <c:listcol filterable="true" name="stEntityName" title="{L-ENGName-L}{L-INANama-L}" />
                    <c:listcol filterable="true" name="stAddress" title="{L-ENGAddress-L}{L-INAAlamat-L}" />
                    <c:listcol filterable="true" name="stEntityID" title="{L-ENGEntity ID-L}{L-INAEntity ID-L}" />
                    <c:listcol filterable="true" name="stTaxCode" title="{L-ENGPajak-L}{L-INAPajak-L}" />
                    <c:listcol filterable="true" name="stTaxFile" title="{L-ENGNPWP-L}{L-INANPWP-L}" />
                </c:listbox>
            </td>
        </tr>
        <tr>
            <td>
                <c:button  text="{L-ENGCreate-L}{L-INABuat-L}" event="clickCreate" />
                <c:button  text="{L-ENGEdit-L}{L-INAUbah-L}" event="clickEdit" />
                <c:button  text="{L-ENGView-L}{L-INALihat-L}" event="clickView" />
            </td>
        </tr>
    </table>
</c:frame>