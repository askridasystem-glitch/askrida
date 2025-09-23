<%@ page import="com.crux.web.controller.SessionManager"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="UPLOAD PERSETUJUAN DIREKSI" >
<%
%>
<table cellpadding=2 cellspacing=1>
   
   <tr>
      <td>
         <c:listbox autofilter="true" name="list" selectable="true" paging="true" view="com.webfin.outcoming.model.UploadBODView" >
            <c:listcol name="stOutID" title="" selectid="outid"/>
            <c:listcol filterable="true" name="stRefNo" title="Registrasi" />
            <c:listcol filterable="true" name="stLetterNo" title="No. Surat" />
            <c:listcol filterable="true" name="stSubject" title="Perihal" />
            <c:listcol filterable="true" name="stReceiver" title="{L-ENGReceiver-L}{L-INAPenerima-L}" />
            <c:listcol filterable="true" name="dtCreateDate" title="{L-ENGLetter Date-L}{L-INATanggal Surat-L}" />
            <c:listcol name="stJam" title="{L-ENGLetter Time-L}{L-INAJam Surat-L}" />
         </c:listbox>
      </td>
   </tr>
   <tr>
      <td>
         <c:button  text="{L-ENGCreate-L}{L-INABuat-L}" event="clickCreate" />
         <c:button  text="{L-ENGView-L}{L-INALihat-L}" event="clickView" />
         <c:button  text="{L-ENGForward-L}{L-INAForward-L}" event="clickForward" />
         <c:button  text="{L-ENGDelete-L}{L-INAHapus-L}" event="clickDelete" />
         <%--<c:button text="Print" name="bprintx" clientEvent="dynPrintClick();" />--%>
      </td>
   </tr>
</table>
<iframe src="" id=frmx width=1 height=1></iframe>
</c:frame>

<script>
   var frmx = docEl('frmx');

   function dynPrintClick() {

      if (f.outid.value=='') {
         alert('Pilih dulu suratnya');
         return;
      }

      if (true) {
         frmx.src='x.fpc?EVENT=OUTCOMING_PRINT&outid='+f.outid.value;
         return;
      }


   }

</script>