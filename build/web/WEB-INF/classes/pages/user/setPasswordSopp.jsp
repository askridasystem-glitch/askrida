<%@ taglib prefix="c" uri="crux" %><c:frame title="Set Password" >
<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <table cellpadding=2 cellspacing=1>
            <c:field width="100" name="user.stUserID" mandatory="true" caption="User Id" type="string" presentation="standard" readonly="true" />
            <c:field width="100" name="user.stUserName" mandatory="true" caption="User Name" type="string" presentation="standard" readonly="true" />
            <c:field width="100" name="user.stDivision" caption="Division" type="string" presentation="standard"  readonly="true" />
            <c:field width="100" name="user.stDepartment" caption="Departement" type="string" presentation="standard" readonly="true" />
            <c:field width="100" name="user.stNewPasswd" mandatory="true" caption="New Password" type="password" presentation="standard" />
            <c:field width="100" name="user.stTempPassword" mandatory="true" caption="Retype-Password" type="password" presentation="standard" />
         </table>
      </td>
   </tr>
   <tr>
      <td align=center>
         <c:button text="Submit"  event="doSave" />
         <c:button text="Cancel"  event="doCancel" />
      </td>
   </tr>
</table>
</c:frame>