<%@ taglib prefix="c" uri="crux" %><c:frame title="Search Entity" >
   <table cellpadding=2 cellspacing=1>
      <tr>
         <td>
            <table cellpadding=2 cellspacing=1>
               <c:field name="doSearch" type="string" hidden="true" value="Y" />
               <c:field width="200" caption="Search" name="stKey" type="string" mandatory="true" readonly="false" presentation="standard"/>
            </table>
         </td>
      </tr>
      <tr>
         <td>
            <c:field name="entityid" type="string" hidden="true" />
            <c:listbox name="entities" selectable="true" >
               <c:listcol columnClass="header" ></c:listcol>
               <c:listcol columnClass="detail" >
                  <c:button text="*" clientEvent="sele($index$)" ></c:button>
               </c:listcol>
               <c:listcol name="stEntityID" title="ID"/>
               <c:listcol name="stEntityName" title="Name" />
               <c:listcol name="stAddress" title="Address" />
            </c:listbox>
         </td>
      </tr>
   </table>
</c:frame>
<script>

   function sele(x) {
      var tbl = docEl('entities');
      dialogReturn(
         {
          stEntityID:tbl.rows(x+1).cells(1).innerText,
          stEntityName:tbl.rows(x+1).cells(2).innerText,
          stAddress:tbl.rows(x+1).cells(3).innerText
         }
      );
      window.close();
   }
</script>