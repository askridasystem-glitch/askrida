<%@ taglib prefix="c" uri="crux" %><c:frame>
   <table cellpadding=2 cellspacing=1>
      <tr>
         <td>
            <table cellpadding=2 cellspacing=1>
               <c:field name="periodFrom" type="date" caption="Period From" presentation="standard"  />
               <c:field name="periodTo" type="date" caption="Period From" presentation="standard"/>
            </table>
         </td>
      </tr>
      <tr>
         <td>
            <c:button event="btnPrint" text="Print"  />
         </td>
      </tr>
   </table>
<iframe src="" id=frmx width=1 height=1></iframe>
<script>
   function btnPrint() {
      frmx.src='x.fpc?EVENT=INS_POL_PRT&policyid='+f.policyID.value+'&alter='+f.stPrintForm.value+'&xlang='+f.stLang.value+'&antic='+(new Date().getTime());
   }
</script>
</c:frame>
