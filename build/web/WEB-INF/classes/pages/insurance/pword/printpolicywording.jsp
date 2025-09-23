<%@ page import="com.webfin.insurance.form.PrintPolicyWordingForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame>
<%
   final PrintPolicyWordingForm form = (PrintPolicyWordingForm) frame.getForm();
%>
<script language="JavaScript" src="script/validator.js">
</script>
<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <table cellpadding=2 cellspacing=1>
            <c:field width="300" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroup" caption="Policy Class" name="stPolicyTypeGroupID" type="string" mandatory="true" presentation="standard"/>
            <c:field width="300" show="<%=form.getStPolicyTypeGroupID()!=null%>" changeaction="onChangePolicyType" lov="LOV_PolicyType" caption="Policy Type" name="stPolicyTypeID" type="string" mandatory="true" presentation="standard">
               <c:lovLink name="polgroup" link="stPolicyTypeGroupID" clientLink="false" />
            </c:field>
            <c:field width="300" name="stFileID" caption="File"  type="string" lov="<%=form.getDocLOVName()%>" presentation="standard" show="<%=form.getStPolicyTypeID()!=null%>" />
         </table>
      </td>
   </tr>
   <tr>
       <%--
      <td>
         <c:button show="<%=form.getStPolicyTypeID()!=null%>" event="btnPrint" text="Print / Download"  />
      </td>
       --%>
      <td>
         <c:button show="<%=form.getStPolicyTypeID()!=null%>" text="Open" clientEvent="dynPrintClick();" />
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

   	  if (f.stFileID.value=='') {
             alert('Jenis Insurance Tools Belum Dipilih!');
             f.stFileID.focus();
             return;
          }

        frmx.src='x.fpc?EVENT=INSURANCE_TOOLS_PRINT&manualbook='+f.stFileID.value;
        return;
   }



</script>