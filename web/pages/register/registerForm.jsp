<%@ page import="com.webfin.register.forms.RegisterMasterForm,
com.webfin.register.model.RegisterView" %>
<%@ taglib prefix="c" uri="crux" %><c:frame title="WORKING TIMELINE" >
    <%     
    final RegisterMasterForm form = (RegisterMasterForm)request.getAttribute("FORM");
    
    final RegisterView entity = form.getEntity();
    
    boolean isReplyMode = form.isReplyMode();
    
    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    
                    <%--  <c:field hidden="true" name="userIndex"/> 
                    <c:field name="entity.stRegID" caption="Register ID" type="integer" hidden="true" readonly="true" presentation="standard" flags="auto3"/>
                    
                    <c:field width="300" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroup"
                         readonly="<%=entity.getStPolicyTypeGroupID()!=null%>" caption="{L-ENGPolicy Class-L}{L-INAKelas Polis-L}"
                         name="entity.stPolicyTypeGroupID" type="string" mandatory="true" presentation="standard"/>
                <c:field width="300" include="<%=entity.getStPolicyTypeGroupID()!=null%>"
                         changeaction="onChangePolicyType" lov="LOV_PolicyType" readonly="<%=entity.getStPolicyTypeID()!=null%>"
                         caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" name="entity.stPolicyTypeID" type="string"
                         mandatory="true" presentation="standard">
                    <c:lovLink name="polgroup" link="entity.stPolicyTypeGroupID" clientLink="false"/>
                </c:field>--%>
                        
                    <c:field width="160" readonly="<%=entity.getStDivision()!=null%>" lov="LOV_Division" name="entity.stDivision" caption="{L-ENGDivisi-L}{L-INA Divisi-L}" type="string" presentation="standard"/>
                    <c:field width="200" changeaction="changeBranch" lov="LOV_CostCenter"
                             caption="{L-ENGBranch-L}{L-INACabang-L}" name="entity.stCostCenterCode" type="string"
                             readonly="<%=entity.getStCostCenterCode()!=null%>"
                             presentation="standard"/>

                    <c:field show="<%=entity.getStCostCenterCode()!=null%>" width="200" lov="LOV_Region"
                             changeaction="onChangeRegion" caption="{L-ENGRegion-L}{L-INADaerah-L}"
                             name="entity.stRegionID" type="string" mandatory="false"
                             readonly="<%=entity.getStRegionID()!=null%>" presentation="standard">
                        <c:lovLink name="cc_code" link="entity.stCostCenterCode" clientLink="false"/>
                    </c:field>
                    <c:field width="160" lov="LOV_REGISTERSTATUS" name="entity.stStatus" caption="{L-ENGStatus-L}{L-INA Status-L}" type="string" presentation="standard"/>
                    <c:field width="200" name="entity.dtLetterDate" caption="{L-ENG Letter Date-L}{L-INA Tanggal Surat-L}" type="date" presentation="standard"  />
                    <c:field width="200" name="entity.dtDeadlineDate" caption="{L-ENG Deadline Date-L}{L-INA Tanggal Deadline-L}" type="date" presentation="standard"  />
                    <c:field width="200" name="entity.dtReceiveDate" caption="{L-ENG Receive Date-L}{L-INA Tanggal Permintaan/Surat Masuk-L}" type="date" presentation="standard"  />
                    <c:field width="200" name="entity.stUserID" popuplov="true" lov="LOV_Profil" caption="{L-ENGUser In Charge-L}{L-INAUser ID-L}" type="string" presentation="standard"/>
                    <c:field width="500" name="entity.stRefNo" caption="{L-ENG Ref No-L}{L-INA No Surat-L}" type="string" presentation="standard"/>
                    <c:field width="500" name="entity.stSubject" rows="5" caption="{L-ENG Subject-L}{L-INA Judul-L}" type="string" presentation="standard"/>
                    <c:field width="500" name="entity.stNote" rows="15" caption="{L-ENG Note-L}{L-INA Catatan-L}" type="string" presentation="standard"/>
                    <c:field name="entity.stFilePhysic" type="file" thumbnail="true" caption="{L-ENG File Attachment-L}{L-INA File Lampiran-L}" presentation="standard" />

                </table>
            </td>
        </tr>
        <tr>
            <td align=center>
                <c:button text="{L-ENG Save-L}{L-INA Simpan-L}"  event="doSave" show="<%=!form.isReadOnly()&&!isReplyMode%>" validate="true" />
                <c:button text="{L-ENG Save-L}{L-INA Simpan-L}"  event="doSave2" show="<%=!form.isReadOnly()&&isReplyMode%>" validate="true" />
                
                <c:button text="{L-ENG Cancel-L}{L-INA Batal-L}"  event="doClose" show="<%=!form.isReadOnly()%>"/>
                <c:button text="Back"  event="doClose" show="<%=form.isReadOnly()%>"/>
            </td>
        </tr>
    </table>
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
</script>