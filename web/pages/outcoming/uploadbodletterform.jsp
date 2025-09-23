<%@ page import="com.webfin.outcoming.forms.UploadBODMasterForm,
         com.webfin.outcoming.model.UploadBODView" %>
<%@ taglib prefix="c" uri="crux" %><c:frame title="Upload Persetujuan" >
    <%
                final UploadBODMasterForm form = (UploadBODMasterForm) request.getAttribute("FORM");

                final UploadBODView entity = form.getEntity();

                boolean isReplyMode = form.isReplyMode();

    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>

                    <%--  <c:field hidden="true" name="userIndex"/> --%>
                    <c:tab name="tabs">
                        <c:tabpage name="TAB0">
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="entity.stOutID" caption="Outcoming Letter ID" type="integer" hidden="true" readonly="true" presentation="standard" flags="auto3"/>
                                <c:field width="200" name="entity.dtLetterDate" mandatory="true" caption="{L-ENG Letter Date-L}{L-INA Tanggal Surat-L}" type="date" presentation="standard"  />
                                <c:field width="500" name="entity.stLetterNo" mandatory="true" caption="No. Surat" type="string" presentation="standard"/>
                                <c:field width="500" name="entity.stSubject" mandatory="true" caption="Perihal" type="string" presentation="standard"/>
                                <c:field width="500" name="entity.stNote" rows="18" mandatory="true" caption="Isi Surat" type="string" presentation="standard"/>
                                <c:field width="200" name="entity.stPolicyID" mandatory="true" caption="No. Polis" type="string" presentation="standard" popuplov="true" lov="LOV_POLICY"/>
                            </table>
                        </c:tabpage>

                        <c:tabpage name="TAB1">
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="addressIndex" type="string" hidden="true"/>
                                <tr>
                                    <td>
                                        <c:listbox name="address">
                                            <c:listcol title="" columnClass="header">
                                                <c:button text="+" event="doNewAddress" validate="false" defaultRO="true"/>
                                            </c:listcol>
                                            <c:listcol title="" columnClass="detail">
                                                <c:button text="-" event="doDeleteAddress" clientEvent="f.addressIndex.value='$index$';"
                                                          validate="false" defaultRO="true"/>
                                            </c:listcol>
                                            <c:listcol title="{L-ENGReceiver-L}{L-INAPenerima-L}">
                                                <c:field name="address.[$index$].stReceiver" type="string" popuplov="true" lov="LOV_Profil2" mandatory="true" readonly="false"/>
                                            </c:listcol>
                                            <c:listcol title="Status">
                                                <c:field name="address.[$index$].stApprovalType" type="string" readonly="true"/>
                                            </c:listcol>
                                        </c:listbox>
                                    </td>
                                </tr>
                            </table>
                        </c:tabpage>
                        <c:tabpage name="TAB2">
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
                <c:button text="Preview" event="btnPrint"/>
                <c:button text="{L-ENG Send-L}{L-INA Kirim-L}"  event="doSave" show="<%=!form.isReadOnly() && !isReplyMode%>" />
                <c:button text="{L-ENG Cancel-L}{L-INA Batal-L}"  event="doClose" show="<%=!form.isReadOnly()%>"/>
                <c:button text="Back"  event="doClose" show="<%=form.isReadOnly()%>"/>
            </td>
        </tr>
    </table>
    <%
                if (form.goPrint.equalsIgnoreCase("Y")) {
                    out.print("<script>");
                    out.print("window.open('pages/outcoming/report/outpreview.fop')");
                    out.print("</script>");
                    form.goPrint = null;
                }
    %>
</c:frame>
<script>
    function selectCustomer2() {
        var o= window.lovPopResult;
        document.getElementById('address.stReceiver').value=o.code;
      
    }
  	 
    function selectCustomer3() {
        var o= window.lovPopResult;
        document.getElementById('entity.stSender').value=o.value;
      
    }
         
    function selectDocument() {
        //var o= window.lovPopResult;
        //document.getElementById('document.stFilePhysic').value=o.code;
      
    }
</script>