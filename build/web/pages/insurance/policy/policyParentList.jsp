<%@ page import="com.webfin.insurance.model.InsurancePolicyParentView,
                 com.crux.util.JSPUtil,
                 com.crux.lov.LOVManager,
                 com.crux.util.LOV,
                 java.util.Iterator,
                 com.crux.lang.LanguageManager,
                 com.webfin.insurance.form.PolicyParentListForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame >
<%
   final PolicyParentListForm form = (PolicyParentListForm) frame.getForm();

   final boolean canEdit = form.isCanEdit();

   final boolean canNavigateBranch = form.isCanNavigateBranch();

   boolean reas = form.isReas();
   
   boolean claim = form.isClaim();
   

%>
<table cellpadding=2 cellspacing=1>
  
   
   <tr>
      <td>
         <table cellpadding=2 cellspacing=1>
            <tr>
               <td>
                  <table cellpadding=2 cellspacing=1>
                     <tr>
                        <td>{L-ENGPolicy Date-L}{L-INATanggal Polis-L}</td>
                        <td>:</td>
                        <td>
                           {L-ENGFrom-L}{L-INADari-L} <c:field name="dtFilterPolicyDateFrom" caption="Policy Date From" type="date" />
                           {L-ENGTo-L}{L-INAS/D-L} <c:field name="dtFilterPolicyDateTo" caption="Policy Date To" type="date" />
                        </td>
                     </tr>
                     <tr>
                        <td>{L-ENGPolicy Expire-L}{L-INATanggal Polis Berakhir-L}</td>
                        <td>:</td>
                        <td>
                           {L-ENGFrom-L}{L-INADari-L} <c:field name="dtFilterPolicyExpireFrom" caption="Policy Expire From" type="date" />
                           {L-ENGTo-L}{L-INAS/D-L} <c:field name="dtFilterPolicyExpireTo" caption="Policy Expire To" type="date" />
                        </td>
                     </tr>
                     <c:field width="200" caption="{L-ENGBranch-L}{L-INACabang-L}" lov="LOV_Branch" name="stBranch" type="string" readonly="<%=!canNavigateBranch%>" presentation="standard" changeaction="refresh" />
                     <%--  <c:field caption="{L-ENGShow active &amp; Pending only-L}{L-INATampilkan polis aktif &amp; Pending-L} " name="stOutstandingFlag" type="check" presentation="standard" changeaction="refresh" />
                     <c:field caption="{L-ENGShow Pending only-L}{L-INATampilkan data belum disetujui-L} " name="stNotApprovedPolicy" type="check" presentation="standard" changeaction="refresh" />
                  	  --%>
                  </table>
               </td>
               <td>
                  <c:button text="Refresh" event="refresh" />
               </td>
            </tr>
         </table>
      </td>
   </tr>

   
   
   <tr>
      <td>
         <c:listbox name="list" autofilter="true" selectable="true" paging="true" view="com.webfin.insurance.model.InsurancePolicyView" >
         <%
            final InsurancePolicyParentView pol = (InsurancePolicyParentView) current;
         %>
            <c:listcol name="stPolicyID" title="ID" selectid="policyID"/>
            <c:listcol title="Act" name="stActiveFlag" flag="true" />
            <c:listcol name="stEffectiveFlag" title="Eff" flag="true" />
            <%--  <c:listcol name="stPrintFlag" title="Print" flag="true" />--%>
            <c:evaluate when="<%=reas%>" >
               <c:listcol name="stRIFinishFlag" title="RI" flag="true" />
            </c:evaluate>
            <c:listcol name="stPolicyID" title="ID" />
            <%--  
            <c:listcol name="stRootID" title="PID" />
            --%>
            <c:listcol filterable="true" name="stPolicyNo" title="{L-ENGPolicy No-L}{L-INANomor Polis-L}" />
            <c:evaluate when="<%=claim%>" >
               <c:listcol filterable="true" name="stPLANo" title="{L-ENGPLA No-L}{L-INANomor LKS-L}" />
               <c:listcol filterable="true" name="stDLANo" title="{L-ENGDLA No-L}{L-INANomor LKP-L}" />
               <c:listcol filterable="true" align="right" name="dbClaimAmountEstimate" title="{L-ENGClaim Estimated-L}{L-INAKlaim Perkiraan-L}" />
               <c:listcol filterable="true" align="right" name="dbClaimAmountApproved" title="{L-ENGClaim Approved-L}{L-INAKlaim Disetujui-L}" />
            </c:evaluate>
            <c:listcol filterable="true" name="stPolicyTypeDesc" title="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" />
            <c:listcol filterable="true" name="stCustomerName" title="Customer">
            
               <%--  <c:evaluate when="<%=pol!=null%>" >
                  <div style="width:120px;text-overflow:ellipsis;overflow:hidden">
                     <nobr><%=JSPUtil.print(pol.getStCustomerName())%></nobr>
                  </div>
               </c:evaluate>
               --%>
            </c:listcol>
            <c:listcol filterable="true" name="stStatusDesc" title="Status" />
            <%--  <c:listcol name="<%if(pol.isStatusEndorse()){%>dbInsuredAmount<%}else{%>dbInsuredAmountEndorse<%}%>" align="right" title="TSI" />
            --%><%-- <c:listcol align="right" title="TSI" ><%=JSPUtil.print(pol.isStatusEndorse()?pol.getDbInsuredAmountEndorse():pol.getDbInsuredAmount(),2)%></c:listcol>
            <c:listcol align="right" title="Premi" ><%=JSPUtil.print(pol.getDbPremiNetto(),2)%></c:listcol>
             <c:listcol title="User" ><%=JSPUtil.print(pol.getStCreateWho())%></c:listcol>--%>
         </c:listbox>
      </td>
   </tr>
   <tr>
      <td>                                                                 
         <c:button show="true"  text="{L-ENGCreate-L}{L-INABuat-L} Polis Induk" event="clickCreate" />
         
         
         <c:button show="true"  text="{L-ENGEdit-L}{L-INAUbah-L}" event="clickEdit" />
         
         <c:button show="true"  text="{L-ENGView-L}{L-INALihat-L}" event="clickView" />
         <c:button show="true"  text="{L-ENGApproval-L}{L-INASetujui-L}" event="clickApproval" />
         <%--  
         --%>

      </td>
    </tr>
    <tr>
     <td>
            Print <c:field name="stPrintForm" width="170" type="string" lov="LOV_POL_PRINTING" ><c:param name="vs" value="<%=form.getPrintingLOV()%>" /></c:field> in <c:field name="stLang" type="string" value="<%=LanguageManager.getInstance().getActiveLang()%>" lov="LOV_LANG" />
            
         	<c:button show="true"  text="Print" name="bprintx"  clientEvent="dynPrintClick();" />
     </td>
    </tr>
    <%--  
    <c:evaluate when="<%=form.isEnableSuperEdit()%>" >
    <tr>
    	<td>
    	Administrator Tools :
    	</td>
    </tr>
    <tr>
    	<td>
    	<c:button show="<%=form.isEnableSuperEdit()%>"  text="Super Edit" event="clickSuperEdit" />    
         <c:button show="<%=form.isEnableSuperEdit()%>"  text="{L-ENGEdit(Reverse)-L}{L-INAUbah(Reverse)-L}" event="clickEditViaReverse" />
      	 <c:button show="<%=form.isEnableSuperEdit()%>"  text="{L-ENGReverse-L}{L-INAReverse-L}" event="clickReverse" />
         <c:button show="<%=form.isEnableSuperEdit()%>"  text="{L-ENGApprove(Reverse)-L}{L-INASetujui(Reverse)-L}" event="clickApprovalViaReverse" />
    	<c:button show="<%=form.isEnableSuperEdit()%>"  text="{L-ENGReApprove-L}{L-INAApprove Ulang-L}" event="clickReApproval" />
    	</td>
    </tr>
    </c:evaluate>
   <c:evaluate when="<%=form.isEnablePrint()%>" >
      <tr>
         <td>
            Print <c:field name="stPrintForm" width="170" type="string" lov="LOV_POL_PRINTING" ><c:param name="vs" value="<%=form.getPrintingLOV()%>" /></c:field> in <c:field name="stLang" type="string" value="<%=LanguageManager.getInstance().getActiveLang()%>" lov="LOV_LANG" />
            Font <c:field name="stFontSize" width="60" type="string" lov="LOV_FONTSIZE" />
            Type <c:field name="stAttached" type="string" lov="LOV_POLATTACHED" />
            <c:button text="Print" name="bprintx"  clientEvent="dynPrintClick();" />
         </td>
      </tr>
   </c:evaluate>--%>
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

      var requireNom = (getSelectedAttr(f.stPrintForm,'ref2') == 'Y');

      if (!requireNom) {
         frmx.src='x.fpc?EVENT=INS_POL_PRT&policyid='+f.policyID.value+'&alter='+f.stPrintForm.value+'&xlang='+f.stLang.value+'&antic='+(new Date().getTime());
         return;
      }

      openDialog('w.ctl?EVENT=INS_PRT_NOM',400,50,
         function (o) {
            if (o!=null) {
               //alert(o);               
            	frmx.src='x.fpc?EVENT=INS_POL_PRT&nom='+o+'&policyid='+f.policyID.value+'&alter='+f.stPrintForm.value+'&xlang='+f.stLang.value+'&antic='+(new Date().getTime());
                    	
            }
         }
      );
   }

</script>