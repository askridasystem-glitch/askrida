<%@ page import="com.webfin.manual.form.PrintManualBook,
				com.crux.util.JSPUtil"%>
<%@ taglib prefix="c" uri="crux" %><c:frame>
    <%
                final PrintManualBook form = (PrintManualBook) frame.getForm();

                final boolean isDownloadMode = form.getStFormType().equalsIgnoreCase("DOWNLOAD");

                final boolean isManualMode = form.getStFormType().equalsIgnoreCase("MANUAL");

                final boolean isManualPDFMode = form.getStFormType().equalsIgnoreCase("MANUALPDF");

    %>
    <script language="JavaScript" src="script/validator.js">
    </script>

    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:evaluate when="<%=isDownloadMode%>">
                        <c:field width="400" lov="LOV_Download" caption="Download File" name="stManualBookTypeID" type="string" mandatory="true" presentation="standard"/>
                    </c:evaluate>
                    <c:evaluate when="<%=isManualMode%>">
                        <c:field width="400" lov="LOV_ManualGroup" caption="Kategori" name="stManualBookTypeID" type="string" mandatory="true" presentation="standard" changeaction="onChangeManualBook"/>
                        <%--<c:field width="400" lov="<%=form.getDocLOVName()%>" caption="File" name="stFileID" type="string" presentation="standard" show="<%=form.getStManualBookTypeID() != null%>" />--%>
                        <c:field width="400" show="<%=form.getStManualBookTypeID() != null%>" lov="LOV_ManualDetail" caption="File" name="stFileID" type="string" mandatory="true" presentation="standard">
                            <c:lovLink name="polgroup" link="stManualBookTypeID" clientLink="false" />
                        </c:field>
                    </c:evaluate>
                    <c:evaluate when="<%=isManualPDFMode%>">
                        <c:field width="400" lov="LOV_ManualPDFBook" caption="Manual PDF" name="stManualBookTypeID" type="string" mandatory="true" presentation="standard" changeaction="onChangeManualBook"/>
                        <%--<c:field width="400" lov="<%=form.getDocLOVName()%>" caption="File" name="stFileID" type="string" presentation="standard" show="<%=form.getStManualBookTypeID() != null%>" />--%>
                        <c:field width="400" show="<%=form.getStManualBookTypeID() != null%>" lov="LOV_ManualPDFBookDetail" caption="File" name="stFileID" type="string" mandatory="true" presentation="standard">
                            <c:lovLink name="polgroup" link="stManualBookTypeID" clientLink="false" />
                        </c:field>
                    </c:evaluate>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <c:evaluate when="<%=isDownloadMode || isManualMode%>">
                    <c:button show="true" text="Open" clientEvent="dynPrintClick();" />
                </c:evaluate>
                <c:evaluate when="<%=isManualPDFMode%>">
                    <c:button show="true" text="Open" clientEvent="dynPrintClick2();" />
                </c:evaluate>
            </td>
        </tr>
    </table>
    <iframe src="" id=frmx width=1 height=1></iframe>
</c:frame>

<script>
    var frmx = docEl('frmx');

    function getSelectedAttr(c,ref) {
        return c.options[c.selectedIndex].getAttribute(ref);
    }

    function dynPrintClick() {
   
        if (f.stManualBookTypeID.value=='') {
            alert('Jenis Manual Book Belum Dipilih!');
            f.stManualBookTypeID.focus();
            return;
        }
      
        frmx.src='x.fpc?EVENT=MANUAL_BOOK_PRINT&manualbook='+f.stFileID.value;
        return;
    }

    function dynPrintClick2() {

        if (f.stManualBookTypeID.value=='') {
            alert('Jenis Manual Book Belum Dipilih!');
            f.stManualBookTypeID.focus();
            return;
        }

        frmx.src='x.fpc?EVENT=MANUAL_BOOK_PRINT2&manualbook='+f.stFileID.value;
        return;
    }
   
         

</script>