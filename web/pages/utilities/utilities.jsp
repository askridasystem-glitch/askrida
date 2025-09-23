<%@ page import="com.webfin.utilities.form.Utilities,
				com.crux.util.JSPUtil"%>
<%@ taglib prefix="c" uri="crux" %><c:frame>
<%
   final Utilities form = (Utilities) frame.getForm();
   
%>
<script language="JavaScript" src="script/validator.js">
</script>
<%--
<table cellpadding=2 cellspacing=1>
	<tr>
      <td>
         <table cellpadding=2 cellspacing=1>
            <c:field width="200" lov="LOV_Guidance" caption="Jenis File" name="stManualBookTypeID" type="string" mandatory="true" presentation="standard"/>  	
         </table>
      </td>
   </tr>
   <tr>
      <td>
         <c:button show="true" text="Open" clientEvent="dynPrintClick();" />
      </td>
   </tr>
</table>
--%>
<table cellpadding=2 cellspacing=1>
	<tr>
      <td>
         <table cellpadding=2 cellspacing=1>

            <c:field width="300" caption="Kode" name="stEncryptedCode" type="string" mandatory="true" presentation="standard"/>
            <c:field width="300" caption="Kode Enkripsi Perbandingan" name="stDecryptedCode2" type="string" mandatory="true" presentation="standard"/>
            <c:field width="300" caption="Kode Enkripsi" name="stDecryptedCode" type="string" readonly="true" mandatory="true" presentation="standard"/>
            <tr>
                   <td>Keterangan</td>
                   <td>:</td>


            <%if(form.getStDecryptedCode()!=null && form.getStDecryptedCode2()!=null){
                   if(form.getStDecryptedCode().toString().equalsIgnoreCase(form.getStDecryptedCode2().toString())){%>
                   
                       <td><b>KODE SAMA</b></td>
                   
                   <%}else{
                        %>
                            <td><b>KODE TIDAK SAMA</b></td>
                        <%
                       }

                    }%>
            </tr>
         </table>
      </td>
   </tr>
   <tr>
       <td colspan="3" align="center">
          <c:button show="true" text="Cek Kode Enkripsi" event="decrypt" />
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