<%@ page import="com.webfin.utilities.form.Utilities,
				com.crux.util.JSPUtil"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="Fix Data">
<%
   final Utilities form = (Utilities) frame.getForm();
   
%>
<script language="JavaScript" src="script/validator.js">
</script>
      
<table cellpadding=2 cellspacing=1>
	<tr>
      <td>
         <table cellpadding=2 cellspacing=1>
            <c:field width="200" caption="File Excel" name="stFilePhysic" type="file" thumbnail="true" presentation="standard"/>  
            <c:field width="200" caption="Method" name="stMethod" type="string" presentation="standard"/>  	
         </table>
      </td>
   </tr>
   <tr>
      <td>
         <c:button show="true" text="Execute" event="execute" />
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
      
      frmx.src='x.fpc?EVENT=GUIDANCE_PRINT&manualbook='+f.stManualBookTypeID.value;
      return;
   }
   
         

</script>