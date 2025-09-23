<%@ page import="com.crux.web.controller.SessionManager"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="ARSIP" >
    <%
    %>
    <table cellpadding=2 cellspacing=1>

        <tr>
            <td>
                <c:listbox autofilter="true" name="list" selectable="true" paging="true" view="com.webfin.archive.model.ArchiveView" >
                    <c:listcol name="stArchiveID" title="" selectid="inid"/>
                    <c:listcol filterable="true" name="stDivision" title="Divisi" />
                    <c:listcol filterable="true" name="stArchiveSubject" title="Judul" />
                    <c:listcol filterable="true" name="dtPeriodStart" title="Tanggal Mulai" />
                    <c:listcol filterable="true" name="dtPeriodEnd" title="Tanggal Akhir" />
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