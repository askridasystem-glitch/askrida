<%@ page import="com.webfin.insurance.model.InsurancePolicyView,
com.crux.util.JSPUtil,
com.crux.lov.LOVManager,
com.crux.util.LOV,
java.util.Iterator,
com.crux.lang.LanguageManager,
com.webfin.insurance.form.PolicyListForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="Underwriting Monitoring (Auto Refresh Every 5 Minutes)">
    <%
    final PolicyListForm form = (PolicyListForm) frame.getForm();

    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>

            </td>
        </tr>
        
        <tr>
            <td>
                <c:listbox name="listUWMonitoring" autofilter="true" selectable="true" paging="true" view="com.webfin.insurance.model.InsurancePolicyView" >
                    <%
                    final InsurancePolicyView pol = (InsurancePolicyView) current;
                    
                    %>
                    <c:listcol name="stPolicyID" title="ID" selectid="policyID"/>
                    <c:listcol name="stEffectiveFlag" title="Eff" flag="true" />
                    <c:listcol filterable="true" name="stPolicyNo" title="{L-ENGPolicy No-L}{L-INANomor Polis-L}" />
                    <c:listcol filterable="true" name="stDescription" title="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" />
                    <c:listcol filterable="true" name="stCostCenterCode" title="{L-ENGBranch-L}{L-INACabang-L}" />
                    <c:listcol filterable="true" name="stStatus" title="Status" />
                    <c:listcol align="right" title="TSI" ><%=JSPUtil.print(pol.isStatusEndorse()?pol.getDbInsuredAmountEndorse():pol.getDbInsuredAmount(),2)%></c:listcol>
                    <c:listcol align="right" title="Premi" ><%=JSPUtil.print(pol.getDbPremiTotal(),2)%></c:listcol>
                    <c:listcol name="dtPolicyDate" title="Tanggal Polis" />   
                </c:listbox>
            </td>
        </tr>
        <tr>
            <td>                                                                 
                <c:button show="<%=form.isEnableView()%>"  text="{L-ENGView-L}{L-INALihat-L}" event="clickView" />
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

      if (f.stPrintForm.value=='') {
         alert('Please select a printing type');
         f.stPrintForm.focus();
         return;
      }

      if (f.policyID.value=='') {
         alert('Please select a policy');
         return;
      }
      
      if (f.stAttached.value=='') {
         alert('Please select a printing type');
         return;
      }

      var requireNom = (getSelectedAttr(f.stPrintForm,'ref2') == 'Y');

      if (!requireNom) {
         frmx.src='x.fpc?EVENT=INS_POL_PRT&policyid='+f.policyID.value+'&alter='+f.stPrintForm.value+'&xlang='+f.stLang.value+'&fontsize='+f.stFontSize.value+'&authorized='+f.stAuthorized.value+'&attached='+f.stAttached.value+'&antic='+(new Date().getTime());
         return;
      }

      openDialog('w.ctl?EVENT=INS_PRT_NOM',400,50,
         function (o) {
            if (o!=null) {
               //alert(o);               
            	frmx.src='x.fpc?EVENT=INS_POL_PRT&nom='+o+'&policyid='+f.policyID.value+'&alter='+f.stPrintForm.value+'&xlang='+f.stLang.value+'&fontsize='+f.stFontSize.value+'&authorized='+f.stAuthorized.value+'&attached='+f.stAttached.value+'&antic='+(new Date().getTime());
                    	
            }
         }
      );
   }
   
      function dynPreviewClick() {

      if (f.stPrintForm.value=='') {
         alert('Please select a printing type');
         f.stPrintForm.focus();
         return;
      }

      if (f.policyID.value=='') {
         alert('Please select a policy');
         return;
      }
      
      if (f.stAttached.value=='') {
         alert('Please select a printing type');
         return;
      }

      var requireNom = (getSelectedAttr(f.stPrintForm,'ref2') == 'Y');

      if (!requireNom) {
         frmx.src='x.fpc?EVENT=INS_POL_PRV&policyid='+f.policyID.value+'&alter='+f.stPrintForm.value+'&xlang='+f.stLang.value+'&fontsize='+f.stFontSize.value+'&authorized='+f.stAuthorized.value+'&attached='+f.stAttached.value+'&antic='+(new Date().getTime());
         return;
      }

      openDialog('w.ctl?EVENT=INS_PRT_NOM',400,50,
         function (o) {
            if (o!=null) {
               //alert(o);               
            	frmx.src='x.fpc?EVENT=INS_POL_PRV&nom='+o+'&policyid='+f.policyID.value+'&alter='+f.stPrintForm.value+'&xlang='+f.stLang.value+'&fontsize='+f.stFontSize.value+'&authorized='+f.stAuthorized.value+'&attached='+f.stAttached.value+'&antic='+(new Date().getTime());
                    	
            }
         }
      );
   }

</script>