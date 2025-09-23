<%@ page import="com.crux.web.controller.SessionManager"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="PERSETUJUAN DIREKSI" >
    <%
    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table>
                    <tr>
                        <td>
                            <c:field caption="Tampilkan Surat Belum Dibaca" name="stUnreadFlag" type="check" presentation="standard" changeaction="refresh" />
                        </td>
                        <td>
                            <c:field caption="Tampilkan Surat Belum Disetujui" name="stUnapproveFlag" type="check" presentation="standard" changeaction="refresh" />
                        </td>
                        <td>
                            <c:button text="Refresh" event="refresh" />
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <c:listbox autofilter="true" name="list" selectable="true" paging="true" view="com.webfin.incoming.model.ApprovalBODView" >
                    <c:listcol name="stInID" title="" selectid="inid"/>
                    <c:listcol name="stReadFlag" title="Read" flag="true" />
                    <c:listcol filterable="true" name="stApprovalType" title="Status" />
                    <c:listcol filterable="true" name="stRefNo" title="Registrasi" />
                    <c:listcol filterable="true" name="stLetterNo" title="No. Surat" />
                    <c:listcol filterable="true" name="stSubject" title="Perihal" />
                    <c:listcol filterable="true" name="stSenderName" title="{L-ENGFrom-L}{L-INAPengirim-L}" />
                    <c:listcol filterable="true" name="stCC" title="{L-ENGTo-L}{L-INAPenerima-L}" />
                    <c:listcol name="dtCreateDate" title="{L-ENGLetter Date-L}{L-INATanggal Surat-L}" />
                    <c:listcol name="stJam" title="{L-ENGLetter Time-L}{L-INAJam Surat-L}" />
                </c:listbox>
            </td>
        </tr>

        <tr>
            <td>
                <c:button text="{L-ENGView-L}{L-INALihat-L}" event="clickView" />
                <c:button text="{L-ENGDelete-L}{L-INAHapus-L}" event="clickDelete" />
                <%--<c:button text="Print" name="bprintx" clientEvent="dynPrintClick();" />--%>
            </td>
        </tr>
    </table>
    <iframe src="" id=frmx width=1 height=1></iframe>
</c:frame>

<script>
    var frmx = docEl('frmx');

    function dynPrintClick() {

        if (f.inid.value=='') {
            alert('Pilih dulu suratnya');
            return;
        }

        if (true) {
            frmx.src='x.fpc?EVENT=INCOMING_PRINT&inid='+f.inid.value;
            return;
        }


    }

</script>