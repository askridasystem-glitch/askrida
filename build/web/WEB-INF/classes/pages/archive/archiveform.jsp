<%@ page import="com.webfin.archive.forms.ArchiveMasterForm,
com.webfin.archive.model.ArchiveView" %>
<%@ taglib prefix="c" uri="crux" %><c:frame title="ARSIP" >
    <%
    final ArchiveMasterForm form = (ArchiveMasterForm)request.getAttribute("FORM");
    
    final ArchiveView entity = form.getEntity();
    
    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:field name="entity.stArchiveID" caption="Archive ID" type="integer" hidden="true" readonly="true" presentation="standard" flags="auto3"/> 
                    <c:field width="200" name="entity.stDivision" lov="LOV_Division" mandatory="true" caption="{L-ENG Division-L}{L-INA Divisi-L}" type="string" presentation="standard" />
                    <c:field width="200" name="entity.stArchiveSubject" rows="4" mandatory="true" caption="{L-ENG Subject-L}{L-INA Judul-L}" type="string" presentation="standard" />
                    <c:field width="200" name="entity.dtPeriodStart" caption="Periode Awal" type="date" presentation="standard"  />
                    <c:field width="200" name="entity.dtPeriodEnd" caption="Periode Akhir" type="date" presentation="standard"  />
                    <c:field width="500" name="entity.stNote" rows="18" caption="{L-ENG Note-L}{L-INA Catatan-L}" type="string" presentation="standard"/>
                    <c:field width="200" name="entity.stFilePhysic" type="file" thumbnail="true" caption="{L-ENG File Attachment-L}{L-INA File Lampiran-L}" presentation="standard" />
                </table>
            </td>
        </tr>
        <tr>
            <td align=center>
                <c:button text="{L-ENG Save-L}{L-INA Simpan-L}"  event="doSave" show="<%=!form.isReadOnly()%>" validate="true" />
                <c:button text="{L-ENG Cancel-L}{L-INA Batal-L}"  event="doClose" show="<%=form.isReadOnly()%>"/>
               
            </td>
        </tr>
    </table>
</c:frame>
<script>
	function selectCustomer2() {
      var o= window.lovPopResult;
      document.getElementById('user.stUserID').value=o.value;
  	 }
</script>