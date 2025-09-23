<%@ taglib prefix="c" uri="crux" %><c:frame title="ROUNDED JURNAL MUTASI">

<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <table cellpadding=2 cellspacing=1>
             <c:field name="example" caption="Contoh" width="500" rows="3" type="string" readonly="true" presentation="standard" />
            <c:field name="sql" caption="No Bukti" rows="10" width="500" type="string" presentation="standard" />
            <c:field name="status" caption="Status" width="500" type="string" readonly="true" presentation="standard" />
         </table>
      </td>
   </tr>
   <tr>
      <td>
         <c:button text="Proses" event="execute" confirm="Yakin ingin diproses ?" />
      </td>
   </tr>
</table>
</c:frame>