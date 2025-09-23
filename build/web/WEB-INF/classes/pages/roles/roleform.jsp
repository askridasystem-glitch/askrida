<%@ page import="com.crux.login.form.RoleForm,
                 com.crux.login.form.RoleForm2,
                 com.crux.util.DTOList,
                 com.crux.login.model.FuncRoleView,
                 java.util.HashMap,
                 com.crux.login.model.RoleView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame>
<%
   RoleForm2 form = (RoleForm2)frame.getForm();

   RoleView r = form.getRole();
%>
   <table cellpadding=2 cellspacing=1>
      <tr>
         <td>
            <table cellpadding=2 cellspacing=1>
               <c:field width="200" name="role.stRoleID" mandatory="true" caption="Role Id" type="string" readonly="<%=!r.isNew()%>" presentation="standard" />
               <c:field width="200" name="role.stRoleName" mandatory="true" caption="Role Name" type="string" presentation="standard" />
               <c:field width="200" name="role.dtActiveDate" caption="Active Date" type="date" presentation="standard"  />
               <c:field width="200" name="role.dtInActiveDate" caption="Inactive Date" type="date" presentation="standard"/>
               <%--<c:field width="200" name="role.dbTransactionLimit" mandatory="true" caption="Transaction Limit" type ="money19.0" presentation="standard" />
               <c:field width="200" name="role.dbReference1" mandatory="true" caption="Limit Akseptasi" type ="money19.0" presentation="standard" />
               <c:field width="200" name="role.dbReference2" mandatory="true" caption="Limit Klaim" type ="money19.0" presentation="standard" />
               <c:field width="200" name="role.dbReference3" mandatory="true" caption="Limit Payment" type ="money19.0" presentation="standard" />--%>
            </table>
         </td>
      </tr>
      <tr>
         <td>
            <table cellpadding=2 cellspacing=1>
            <%
               DTOList rolef = form.getRolefunctions();

               boolean formRO = form.isReadOnly();

               String disabled = formRO ? "disabled":"";

               for (int i = 0; i < rolef.size(); i++) {
                  FuncRoleView fr = (FuncRoleView) rolef.get(i);
                  int il = fr.getIndentLevel();
                  boolean checked = fr.isNew();

            %><tr><td class=row<%=il%2%>><%
                  for (int j=0;j<il;j++) {
                     out.print("&nbsp;&nbsp;&nbsp;");
                  }
            %><input type=checkbox name="f<%=i%>" ilvl="<%=il%>" <%=checked?"checked":""%> onclick="autoCheck(this)" <%=disabled%>> <%=fr.getStFuncName()%></td></tr><%

               }
            %>

            </table>
         </td>
      </tr>
      <tr>
         <td>
            <c:evaluate when="<%=form.isReadOnly()%>" >
               <c:button event="clickCancel" text="Close" />
            </c:evaluate>
            <c:evaluate when="<%=!form.isReadOnly()%>" >
               <c:button event="clickSave" text="Save" />
               <c:button event="clickCancel" text="Cancel" />
            </c:evaluate>
         </td>
      </tr>
   </table>
<script>
   function autoCheck(c) {
      var idx = c.name.substring(1,c.name.length);


      idx++;

      while (true) {
         var d = docEl('f'+idx);
         if (d==null) break;

         if (d.ilvl<=c.ilvl) break;

         d.checked=c.checked;
         idx++;
      }

      var idx = c.name.substring(1,c.name.length);

      var n=c.ilvl-1;

      while (true) {

         idx--;

         var d = docEl('f'+idx);

         if (d==null) break;

         if (d.ilvl==n) {
            if (c.checked) d.checked=true;
            n--;
         }
      }
   }
</script>
</c:frame>