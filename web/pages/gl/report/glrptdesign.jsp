<%@ taglib prefix="c" uri="crux" %><c:frame title="GL Report Design" >
<%%>

<table cellpadding=2 cellspacing=1>
   <tr>
      <td>
         <table cellpadding=2 cellspacing=1>
            <c:field caption="Report ID" name="rpt.stReportID" type="string" readonly="true" presentation="standard" />
            <c:field mandatory="true" width="400" caption="Title" name="rpt.stReportTitle" type="string|255" presentation="standard" />
            <c:field mandatory="false" caption="Resource ID" name="rpt.stResourceID" type="string|32" presentation="standard" />
         </table>
      </td>
   </tr>
   <tr>
      <td>
         <c:tab name="tabs">
            <c:tabpage name="TAB_COLUMN">
               <c:listbox name="columns" >
                  <c:listcol title="" columnClass="header">
                     <c:button text="+" event="clickColNew" validate="false" />
                  </c:listcol>
                  <c:listcol title="" columnClass="detail">
                     <c:button text="-" event="clickColDelete" clientEvent="f.colIndex.value='$index$';" validate="false" />
                  </c:listcol>
                  <c:listcol title="No">
                     <c:field mandatory="true" width="50" caption="No" name="columns.[$index$].lgColumnNumber" type="integer|4"  />
                  </c:listcol>
                  <c:listcol title="Header">
                     <c:field width="150" mandatory="true" caption="Header" name="columns.[$index$].stColumnHeader" type="string|255"  />
                  </c:listcol>
                  <c:listcol title="Year">
                     <c:field width="50" caption="Year" name="columns.[$index$].lgYear" type="integer|4"  />
                  </c:listcol>
                  <c:listcol title="PStart">
                     <c:field width="40" caption="Start" name="columns.[$index$].lgPeriod" type="integer|3"  />
                  </c:listcol>
                  <c:listcol title="PEnd">
                     <c:field width="40" caption="End" name="columns.[$index$].lgPeriodTo" type="integer|3"  />
                  </c:listcol>
                  <c:listcol title="Value">
                     <c:field mandatory="true" width="100" lov="LOV_GLRptColType" caption="Value" name="columns.[$index$].stValue" type="string"  />
                  </c:listcol>
                  <c:listcol title="Pos">
                     <c:field width="40" caption="Position" name="columns.[$index$].lgColumnPosition" type="integer|3"  />
                  </c:listcol>
                  <c:listcol title="" columnClass="header">
                     Format (<a href="#" onclick="window.open('pages/help/DecimalFormat.jsp');return false;">help</a>)
                  </c:listcol>
                  <c:listcol title="" columnClass="detail" >
                     <c:field width="80" caption="Format" name="columns.[$index$].stColumnFormat" type="string|64"  />
                  </c:listcol>
               </c:listbox>
            </c:tabpage>
            <c:tabpage name="TAB_LINES">
               <c:field name="lineIndex" type="string" hidden="true"/>
               <c:listbox name="lines" paging="20">
                  <c:listcol title="" columnClass="header">
                     <c:button text="+" event="clickLineNew" validate="false" />
                  </c:listcol>
                  <c:listcol title="" columnClass="detail">
                     <c:button text="-" event="clickLineDelete" clientEvent="f.lineIndex.value='$index$';" validate="false" />
                  </c:listcol>
                  <c:listcol title="No">
                     <c:field mandatory="true" width="30" caption="No" name="lines.[$index$].lgLineNo" type="integer|4" />
                  </c:listcol>
                  <c:listcol title="Type">
                     <c:field mandatory="true" lov="VS_GLRPTLINETYPE" width="100" caption="Type" name="lines.[$index$].stLineType" type="string"/>
                  </c:listcol>
                  <c:listcol title="AC From">
                     <c:field width="130" caption="Account From" name="lines.[$index$].stAccountFrom" type="string|32"  />
                  </c:listcol>
                  <c:listcol title="AC To">
                     <c:field width="130" caption="Account To" name="lines.[$index$].stAccountTo" type="string|32"  />
                  </c:listcol>
                  <c:listcol title="Col No">
                     <c:field mandatory="true" width="40" caption="Column No" name="lines.[$index$].lgColumnNo" type="integer|3"  />
                  </c:listcol>
                  <c:listcol title="CR">
                     <c:field caption="Print CR" name="lines.[$index$].stPrintCRFlag" type="check"  />
                  </c:listcol>
                  <c:listcol title="Neg">
                     <c:field caption="Negate" name="lines.[$index$].stNegateFlag" type="check"  />
                  </c:listcol>
                  <c:listcol title="PRT">
                     <c:field caption="Print" name="lines.[$index$].stPrintFlag" type="check"  />
                  </c:listcol>
                  <c:listcol title="Sum">
                     <c:field caption="Summarize" name="lines.[$index$].stSummarizeFlag" type="check"  />
                  </c:listcol>
                  <c:listcol title="Bln">
                     <c:field width="20" caption="Blank Lines" name="lines.[$index$].lgBlankLine" type="integer|3"  />
                  </c:listcol>
                  <c:listcol title="In">
                     <c:field width="20"  caption="Indent" name="lines.[$index$].lgIndent" type="integer|3"  />
                  </c:listcol>
                  <c:listcol title="Desc">
                     <c:field width="150" caption="Description" name="lines.[$index$].stDescription" type="string|255" />
                  </c:listcol>
                  <c:listcol title="Variable">
                     <c:field width="70" caption="Variable" name="lines.[$index$].stVariable" type="string|128" />
                  </c:listcol>
                  <c:listcol title="Formula">
                     <c:field width="70" caption="Formula" name="lines.[$index$].stFormula" type="string|255" />
                  </c:listcol>
                  <c:listcol title="Format">
                     <c:field width="60" caption="Format" name="lines.[$index$].stFormat" type="string|128" />
                  </c:listcol>
               </c:listbox>
            </c:tabpage>
         </c:tab>
      </td>
   </tr>
   <tr>
      <td>
         <c:button text="Save" event="clickSave" validate="true" />
         <c:button text="Cancel" event="clickCancel"/>
         <c:button text="Back" event="clickCancel"/>
      </td>
   </tr>
</table>
</c:frame>