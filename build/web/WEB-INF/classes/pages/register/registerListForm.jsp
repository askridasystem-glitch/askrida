<%@ page import="com.crux.web.controller.SessionManager,
                com.webfin.register.forms.RegisterListForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="WORKING TIMELINE" >
<%
    final RegisterListForm form = (RegisterListForm)request.getAttribute("FORM");
    
    final boolean canNavigateBranch = form.isCanNavigateBranch();
%>
<table cellpadding=2 cellspacing=1>
    <tr>
        <td>
            <table cellpadding=2 cellspacing=1>
                <tr>
                    <td>
                        <c:field width="160" lov="LOV_Division" name="division" caption="{L-ENGDivisi-L}{L-INA Divisi-L}" type="string" presentation="standard" readonly="<%=form.getDivision()!=null%>" changeaction="refresh"/>
                        <c:field width="200" lov="LOV_Branch" name="branch" caption="{L-ENGBranch-L}{L-INA Cabang-L}" type="string" presentation="standard" changeaction="refresh"/>
                    </td>
                </tr>
            </table>
        
        </td>
    </tr>
   <tr>
      <td>
         <c:listbox autofilter="true" name="list" selectable="true" paging="true" view="com.webfin.register.model.RegisterView" >
            <c:listcol name="stRegID" title="" selectid="outid"/>
            <c:listcol filterable="true" name="stRefNo" title="{L-ENGReference No-L}{L-INANo Surat-L}" />
            <c:listcol filterable="true" name="stSubject" title="{L-ENGSubject-L}{L-INAJudul-L}" />
            <c:listcol filterable="true" name="stStatus" title="Status" />
            <c:listcol name="dtReceiveDate" title="{L-ENGReceive Date-L}{L-INATanggal Permintaan<br>/Surat Masuk-L}" />
            <c:listcol name="dtDeadlineDate" title="{L-ENGDeadline Date-L}{L-INATanggal Deadline-L}" />
            <c:listcol filterable="true" name="stUserID" title="User ID" />
            <c:listcol name="stUserName" title="User" />
            
         </c:listbox>
      </td>
   </tr>
   <tr>
      <td>
         <c:button  text="{L-ENGCreate-L}{L-INABuat-L}" event="clickCreate" />
         <c:button  text="{L-ENGView-L}{L-INALihat-L}" event="clickView" />
         <c:button  text="{L-ENGEdit-L}{L-INAUbah-L}" event="clickEdit" />
      </td>
   </tr>
</table>
</c:frame>