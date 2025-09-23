<%@ page import="com.webfin.outcoming.forms.OutcomingMasterForm,
com.webfin.outcoming.model.OutcomingView" %>
<%@ taglib prefix="c" uri="crux" %><c:frame title="Surat Keluar" >
    <%     
    final OutcomingMasterForm form = (OutcomingMasterForm)request.getAttribute("FORM");
    
    final OutcomingView entity = form.getEntity();
    
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
                                <c:field width="500" name="entity.stSubject" rows="3" mandatory="true" caption="{L-ENG Subject-L}{L-INA Judul-L}" type="string" presentation="standard"/>
                                <c:field width="500" name="entity.stNote" rows="18" mandatory="true" caption="{L-ENG Message-L}{L-INA Pesan-L}" type="string" presentation="standard"/>
                                <c:field show="<%=entity.getStFilePhysic()!=null%>" name="entity.stFilePhysic" type="file" thumbnail="true" caption="{L-ENG File Attachment-L}{L-INA File Lampiran-L}" readonly="true" presentation="standard" />
                            </table>
                        </c:tabpage>
                        
                        <c:tabpage name="TAB1">
                            <table cellpadding=2 cellspacing=1>
                                <%--<tr>
                                    <td>{L-ENG Receiver-L}{L-INA Penerima-L}</td>
                                    <td>:</td>
                                    <td>
                                        <c:field width="300" lov="lovAddresses" name="stSelectedAddress" caption="Selected Address" type="string" changeaction="selectAddress" overrideRO="true" />
                                        
                                        <c:button text="+" event="doNewAddress" enabled="true"  />
                                        <c:button text="-" event="doDeleteAddress" enabled="true" />
                                    </td>
                                </tr>
                                <c:evaluate when="<%=form.getAddress()!=null%>">
                                    <tr>
                                        <td colspan=3 class=header>
                                            Penerima
                                            <table cellpadding=2 cellspacing=1 class=row0>
                                                <tr>
                                                    <td>
                                                        <table cellpadding=2 cellspacing=1>
                                                            <c:field clientchangeaction="selectCustomer2()"  name="address.stReceiver" type="string" width="300" popuplov="true"  lov="LOV_Profil" mandatory="true" caption="Penerima" presentation="standard" />
                                                        </table>
                                                    </td>
                                                    <td></td>
                                                    <td></td>
                                                    
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </c:evaluate>--%>
                                
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
                                                    <c:field name="address.[$index$].stReceiver" caption="Penerima" type="string" popuplov="true"  lov="LOV_Profil" mandatory="true" caption="Penerima" 
                                                             readonly="false"/>
                                                            
                                                </c:listcol>
                                            </c:listbox>
                                        </td>
                                    </tr>   
                            </table>
                        </c:tabpage>
                        <c:tabpage name="TAB2">
                            <table cellpadding=2 cellspacing=1>
                               <%-- <tr>
                                    <td>{L-ENG Attachment-L}{L-INA Lampiran-L}</td>
                                    <td>:</td>
                                    <td>
                                        <c:field width="300" lov="lovDocuments" name="stSelectedDocument" caption="Selected Document" type="string" changeaction="selectDocument" overrideRO="true" />
                                        
                                        <c:button text="+" event="doNewDocument" enabled="true"  />
                                        <c:button text="-" event="doDeleteDocument" enabled="true" />
                                    </td>
                                </tr>
                               
                                    <tr>
                                        <td colspan=3 class=header>
                                            Lampiran
                                            <table cellpadding=2 cellspacing=1 class=row0>
                                                <tr>
                                                    <td>
                                                        <table cellpadding=2 cellspacing=1>
                                                            <c:field name="document.stFilePhysic" type="string"  caption="{L-ENG File Attachment-L}{L-INA File Lampiran-L}" presentation="standard" />
                                                        </table>
                                                    </td>
                                                    <td></td>
                                                    <td></td>
                                                    
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>--%>
                                    
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
                <c:button text="{L-ENG Send-L}{L-INA Kirim-L}"  event="doSave" show="<%=!form.isReadOnly()&&!isReplyMode%>" />
                <c:button text="{L-ENG Send-L}{L-INA Kirim-L}"  event="doSave2" show="<%=!form.isReadOnly()&&isReplyMode%>"  />
                
                <c:button text="{L-ENG Cancel-L}{L-INA Batal-L}"  event="doClose" show="<%=!form.isReadOnly()%>"/>
                <c:button text="Back"  event="doClose" show="<%=form.isReadOnly()%>"/>
            </td>
        </tr>
    </table>
</c:frame>
<script>
   //document.getElementById('entity.stNote').focus();
</script>
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