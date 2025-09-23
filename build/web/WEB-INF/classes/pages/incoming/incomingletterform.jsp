<%@ page import="com.webfin.incoming.forms.IncomingMasterForm,
com.webfin.incoming.model.IncomingView" %>
<%@ taglib prefix="c" uri="crux" %><c:frame title="Surat Masuk" >
    <%
    final IncomingMasterForm form = (IncomingMasterForm)request.getAttribute("FORM");
    
    final IncomingView entity = form.getEntity();
    
    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:tab name="tabs">
                        <c:tabpage name="TAB0">
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="entity.stInID" caption="Incoming Letter ID" type="integer" hidden="true" readonly="true" presentation="standard" flags="auto3"/>
                                <c:field width="200" name="entity.stRefNo" mandatory="true" caption="{L-ENG Reference No-L}{L-INA Nomor Surat-L}" type="string" presentation="standard" />
                                <c:field width="200" name="entity.stSender" mandatory="true" caption="{L-ENG From-L}{L-INA ID Pengirim-L}" type="string" presentation="standard" />
                                <c:field width="200" name="entity.stSenderName" mandatory="true" caption="{L-ENG Sender Name-L}{L-INA Nama Pengirim-L}" type="string" presentation="standard" />
                                <c:field width="500" name="entity.stCC" mandatory="true" caption="{L-ENG Receiver-L}{L-INA Penerima-L}" type="string" presentation="standard" />
                                <c:field width="200" name="entity.dtLetterDate" caption="{L-ENG Letter Date-L}{L-INA Tanggal Surat-L}" type="date" presentation="standard"  />
                                <c:field width="500" name="entity.stSubject" rows="3" caption="{L-ENG Subject-L}{L-INA Judul-L}" type="string" presentation="standard"/>
                                <c:field width="500" name="entity.stNote" rows="18" caption="{L-ENG Message-L}{L-INA Pesan-L}" type="string" presentation="standard"/>
                                <c:field width="200" show="<%=entity.getStFilePhysic()!=null%>" name="entity.stFilePhysic" type="file" thumbnail="true" caption="{L-ENG File Attachment-L}{L-INA File Lampiran-L}" presentation="standard" />
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
                <c:button text="{L-ENG Submit-L}{L-INA Kirim-L}"  event="doSave" show="<%=!form.isReadOnly()%>" validate="true" />
                <c:button text="{L-ENG Cancel-L}{L-INA Batal-L}"  event="doClose" show="<%=!form.isReadOnly()%>"/>
                <c:button text="{L-ENG Reply-L}{L-INA Balas-L}"  event="doReply" show="<%=form.isReadOnly()%>"/>
                <c:button text="{L-ENG Forward-L}{L-INA Forward-L}"  event="forward" show="<%=form.isReadOnly()%>"/>
                <c:button text="{L-ENG Close-L}{L-INA Tutup-L}"  event="doClose" show="<%=form.isReadOnly()%>"/>
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