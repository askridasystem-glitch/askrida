<%@ page import="com.webfin.incoming.forms.ApprovalBODMasterForm,
         com.crux.util.Tools,
         com.webfin.incoming.model.ApprovalBODView" %>
<%@ taglib prefix="c" uri="crux" %><c:frame title="Surat Masuk" >
    <%
                final ApprovalBODMasterForm form = (ApprovalBODMasterForm) request.getAttribute("FORM");

                final ApprovalBODView entity = form.getEntity();

    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:tab name="tabs">
                        <c:tabpage name="TAB0">
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="entity.stInID" caption="Incoming Letter ID" type="integer" hidden="true" readonly="true" presentation="standard" flags="auto3"/>
                                <c:field width="100" name="entity.stRefNo" mandatory="true" caption="{L-ENG Reference No-L}{L-INA Nomor Surat-L}" type="string" presentation="standard" readonly="true"/>
                                <c:field width="100" name="entity.stLetterNo" mandatory="true" caption="Perihal" type="string" presentation="standard" readonly="true"/>
                                <c:field width="200" name="entity.stSender" mandatory="true" caption="{L-ENG From-L}{L-INA ID Pengirim-L}" type="string" presentation="standard" readonly="true"/>
                                <c:field width="200" name="entity.stSenderName" mandatory="true" caption="{L-ENG Sender Name-L}{L-INA Nama Pengirim-L}" type="string" presentation="standard" readonly="true"/>
                                <c:field width="500" name="entity.stCC" mandatory="true" caption="{L-ENG Receiver-L}{L-INA Penerima-L}" type="string" presentation="standard" readonly="true"/>
                                <c:field width="200" name="entity.dtLetterDate" caption="{L-ENG Letter Date-L}{L-INA Tanggal Surat-L}" type="date" presentation="standard" readonly="true"/>
                                <c:field width="500" name="entity.stSubject" rows="3" caption="No. Surat" type="string" presentation="standard" readonly="true"/>
                                <c:field width="200" name="entity.stPolicyID" mandatory="true" caption="No. Polis" type="string" presentation="standard" popuplov="true" lov="LOV_POLICY" readonly="true"/>
                                <c:field width="500" name="entity.stNote" rows="10" caption="Isi Surat" type="string" presentation="standard" readonly="true"/>
                                <c:field width="500" name="entity.stReplyNote" rows="10" caption="Catatan" type="string" presentation="standard" readonly="<%=Tools.isYes(entity.getStApproveFlag())%>"/>
                                <c:field width="250" name="entity.stApprovalType" caption="Status" type="string" presentation="standard" lov="LOV_UPLOADSTATUS" readonly="<%=Tools.isYes(entity.getStApproveFlag())%>"/>
                               </table>
                        </c:tabpage>
                        <c:tabpage name="TAB1">
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="instIndex" type="string" hidden="true"/>
                                <tr>
                                    <td>
                                        <c:listbox name="document">
                                            <c:listcol title="" columnClass="header">
                                                <c:button text="+" event="doNewDocument" validate="false" defaultRO="true"/>
                                            </c:listcol>
                                            <c:listcol title="" columnClass="detail">
                                                <c:button text="-" event="doDeleteDocument" clientEvent="f.instIndex.value='$index$';"
                                                          validate="false" defaultRO="true"/>
                                            </c:listcol>
                                            <c:evaluate when="<%=false%>">
                                                <c:listcol title="{L-ENGID-L}{L-INAID-L}">
                                                    <c:field name="document.[$index$].stDocumentOutID" type="string" readonly="true"/>
                                                </c:listcol>
                                            </c:evaluate>
                                            <c:listcol title="{L-ENGAttachment File-L}{L-INAFile Lampiran-L}">
                                                <c:field name="document.[$index$].stFilePhysic" caption="Lampiran Surat" type="file" thumbnail="true"
                                                         readonly="false"/>
                                            </c:listcol>
                                        </c:listbox>
                                    </td>
                                </tr>
                            </table>
                        </c:tabpage>
                    </c:tab>
                </table>
            </td>
        </tr>
        <tr>
            <td align=center>
                <%--<c:button text="{L-ENG Submit-L}{L-INA Kirim-L}" event="doSave" show="<%=!form.isReadOnly()%>" validate="true" />
                <c:button text="{L-ENG Cancel-L}{L-INA Batal-L}" event="doClose" show="<%=!form.isReadOnly()%>"/>
                <c:button text="{L-ENG Forward-L}{L-INA Forward-L}" event="forward" show="<%=form.isReadOnly()%>"/>
                <c:button text="{L-ENG Reply-L}{L-INA Balas-L}" event="doReply"/>
                <c:button text="Preview" event="doPreview"/>--%>
                <c:button text="Preview" event="btnPrint"/>
                <c:button text="{L-ENG Save-L}{L-INA Simpan-L}" event="doSave2"/>
                <c:button text="{L-ENG Close-L}{L-INA Tutup-L}" event="doClose"/>
            </td>
        </tr>
    </table>
      <%
   if (form.goPrint.equalsIgnoreCase("Y")) {
         out.print("<script>");
         out.print("window.open('pages/incoming/report/inpreview.fop')");
         out.print("</script>");
         form.goPrint=null;
      }
      %>
</c:frame>