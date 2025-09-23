<%@ page import="com.crux.login.form.UserForm,
                 com.crux.login.model.UserRoleView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="USER" >
<%
   UserForm form = (UserForm)frame.getForm();

   final boolean isNew = form.getUser().isNew();

%>
<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <table cellpadding=2 cellspacing=1>
            <c:field hidden="true" name="userIndex"/>
            <c:field width="200" name="user.stUserID" mandatory="true" caption="User Id" type="string" presentation="standard" />
            <c:field width="200" name="user.stUserName" mandatory="true" caption="{L-ENGUser Name-L}{L-INANama User-L}" type="string" presentation="standard" />
            <c:field width="200" name="user.stShortName" caption="{L-ENGShort Name-L}{L-INANama Panggilan-L}" type="string" presentation="standard" />
            <c:field width="200" name="user.stDivision" caption="{L-ENGDivision-L}{L-INADivisi-L}" lov="LOV_Division" type="string" presentation="standard"  />
            <c:field width="200" name="user.stDepartment" caption="Departement" type="string" presentation="standard"/>
            <c:field width="200" name="user.stEmail" caption="Email" type ="string" presentation="standard" />

            <c:evaluate when="<%=isNew%>" >
               <c:field width="200" name="stPasswd" mandatory="true" caption="Password" type="password" presentation="standard" />
               <c:field width="200" name="stPasswdConfirm" mandatory="true" caption="{L-ENGRe-Type Password-L}{L-INAUlangi Password-L}" type="password" presentation="standard" />
            </c:evaluate>
            <c:field width="200" name="user.stPhone" caption="{L-ENGPhone-L}{L-INATelepon-L}" type="string" presentation="standard" />
            <c:field width="200" name="user.stMobileNumber" caption="{L-ENGMobile Number-L}{L-INANo. Handphone-L}" type="string" presentation="standard" />
            <c:field width="200" name="user.stContactNum" caption="{L-ENGContact Number-L}{L-INANo. Kontak-L}" type="string" presentation="standard" />

            <c:field width="200" name="user.dtActiveDate" caption="{L-ENGActivate Date-L}{L-INATanggal Aktif-L}" type="date" presentation="standard" />
            <c:field width="200" name="user.dtInActiveDate" caption="{L-ENGExpire Date-L}{L-INATanggal Expire-L}" type="date" presentation="standard" />
            <c:field width="200" name="user.stJobPosition" caption="{L-ENGPosition-L}{L-INAJabatan-L}" type="string" presentation="standard" />
            
            
            <c:field width="200" name="user.stSign" caption="{L-ENGSign-L}{L-INATanda Tangan-L}" type="file" thumbnail="true" presentation="standard" />
            
            
            <c:field width="200" name="user.stParaf" caption="{L-ENGSign-L}{L-INAParaf-L}" type="file" thumbnail="true" presentation="standard" />
            
            <%--
            <tr><td>Paraf nik</td><td>:</td><td>
                    <div id=user.stParafdescdiv style="cursor:hand">
                        <img id=user.stParafdesc style="cursor:hand; border:solid 1px black" alt="File" src="thumb.ctl?EVENT=FILE&fileid=222&thumb=64">
                    </div>
                    <script>

                        
                        var p=docEl('user.stParafdescdiv');

                        p.onmousedown=function()
                        {
                            if (!this.readOnly && window.event.button==2)
                                uploadDoc({file_id:'',clearbutton:'Y',group:'',title:'Paraf',note:'Select a file for Paraf',validate_ext:'',upload_count:1,docName:'Paraf'},
                            function(a)
                            
                            {if (a!=null) {/*alert('berhasil upload id '+ a.id);docEl('user.stParaf').value=a.id;*/ireload2('user.stParaf',a,docEl('user.stParaf').value, 'Y', 'Paraf')} });

                            if (window.event.button==1 || window.event.button==0) {
                                window.open('file.cfl?EVENT=FILE&fileid='+docEl('user.stParaf').value,'_blank','scrollbars=yes,resizable=yes')}
                        
                        };

                        p.readOnly=false;
 
                     </script>
                    <input type=text id=user.stParaf name=user.stParaf value=>

                </td></tr>
            --%>

            <c:field width="200" name="user.stLetterOfAuthority" caption="{L-ENGLetter Of Authority-L}{L-INASurat Kuasa-L}" type="file" thumbnail="true" presentation="standard" />
            <c:field width="200" caption="{L-ENGBranch-L}{L-INACabang-L}" changeaction="changeBranch" lov="LOV_Branch" name="user.stBranch" type="string" presentation="standard" />
            <c:field width="200" caption="{L-ENGRegion-L}{L-INARegion-L}" lov="LOV_Region" name="user.stRegion" type="string" presentation="standard" >
                <c:lovLink name="cc_code" link="user.stBranch" clientLink="false"/>
            </c:field>
            <c:field lov="LOV_RKAP_UnitKerja" width="200" name="user.stDivisionID" caption="Unit Kerja" type="string" presentation="standard">
                <c:lovLink name="param" link="user.stBranch" clientLink="false"/>
            </c:field>
            <c:field width="200" name="user.stReferenceUserID" caption="Reference User Id" type="string" presentation="standard" />
            <c:field width="80" name="user.stSessionTimeout" caption="{L-ENGSession Timeout-L}{L-INASession Timeout-L}" type="string" presentation="standard" suffix=" minutes"/>

            <c:field width="200" caption="{L-ENGBranch-L}{L-INACabang-L} Penerbit" changeaction="changeBranch" lov="LOV_Branch" name="user.stCostCenterCodeSource" type="string" presentation="standard" />
            <c:field width="200" caption="{L-ENGRegion-L}{L-INARegion-L} Penerbit" lov="LOV_Region" name="user.stRegionIDSource" type="string" presentation="standard" >
                <c:lovLink name="cc_code" link="user.stCostCenterCodeSource" clientLink="false"/>
            </c:field>
            <tr>
                <td></td>
            </tr>
            <table cellpadding=2 cellspacing=1 class=header>
                     <tr>
                        <td>
                           Pilihan
                        </td>
                     </tr>
                     <tr>
                        <td class=row0>
                           <table cellpadding=2 cellspacing=1>
                              <c:field width="200" name="user.stSignAuthority" caption="{L-ENGSign Authority-L}{L-INAKewenangan Tanda Tangan Polis-L}" type="check" presentation="standard" />
                                <c:field width="200" name="user.stEntryBackdateFlag" caption="{L-ENGEntry Backdate-L}{L-INABisa Entry Backdate-L}" type="check" presentation="standard" />
                                <c:field width="200" name="user.stDeleteFlag" caption="{L-ENGDelete Flag-L}{L-INADelete Flag-L}" type="check" presentation="standard" />
                                <c:field width="200" lov="LOV_EntityOnly" popuplov="true" name="user.stMarketerID" caption="Data View Marketer ID" type="string" presentation="standard" />
                           </table>
                        </td>
                     </tr>
             </table>
             <br>
             
            <c:listbox name="user.userroles" >
            <%
               UserRoleView usr = (UserRoleView) current;
            %>
               <c:listcol title="" columnClass="header" ><c:button enabled="true" text="+" event="addRole"/></c:listcol>
               <c:listcol title="" columnClass="detail" ><c:button enabled="true" text="-" event="deleteRole" clientEvent="f.userIndex.value='$index$'"/></c:listcol>
               <c:listcol title="ID Grup Akses" >
                  <c:field name="user.userroles.[$index$].stRoleID" width="450" lov="LOV_Role" readonly="<%=!usr.isNew()%>" type="string" caption="ID Grup Akses" mandatory="true" />
               </c:listcol>
            </c:listbox>
         </table>
      </td>
   </tr>
   <tr>
      <td align=center>
         <c:button text="{L-ENGSubmit-L}{L-INASimpan-L}"  event="doSave" show="<%=!form.isReadOnly()%>" validate="true" />
         <c:button text="{L-ENGCancel-L}{L-INABatal-L}"  event="doCancel" show="<%=!form.isReadOnly()%>"/>
         <c:button text="{L-ENGBack-L}{L-INAKembali-L}"  event="doCancel" show="<%=form.isReadOnly()%>"/>
      </td>
   </tr>
</table>
</c:frame>