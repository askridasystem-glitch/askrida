<%@ page import="com.webfin.insurance.model.InsurancePolicyView,
com.crux.util.JSPUtil,
com.crux.lov.LOVManager,
com.crux.util.LOV,
java.util.Iterator,
com.crux.lang.LanguageManager,
com.webfin.insurance.form.PolicyListForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame >
    <%
    final PolicyListForm form = (PolicyListForm) frame.getForm();
    
    final boolean canEdit = form.isCanEdit();
    
    final boolean canNavigateBranch = form.isCanNavigateBranch();
    
    final boolean isBondingViewOnly = form.isIsBondingDivision();
    
    boolean reas = form.isReas();
    
    boolean claim = form.isClaim();
    
    //final boolean showFilter = form.isShowFilter();
    
    final boolean approveUW = form.isApproveUW();
    
    final boolean policy = form.isPolicy();
    
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
                                            <c:field name="dtFilterPolicyDateFrom" caption="Policy Date From" type="date" />
                                            {L-ENGTo-L}{L-INAS/D-L} <c:field name="dtFilterPolicyDateTo" caption="Policy Date To" type="date" />
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>{L-ENGPolicy Expire-L}{L-INATanggal Berakhir-L}</td>
                                        <td>:</td>
                                        <td>
                                            <c:field name="dtFilterPolicyExpireFrom" caption="Policy Expire From" type="date" />
                                            {L-ENGTo-L}{L-INAS/D-L} <c:field name="dtFilterPolicyExpireTo" caption="Policy Expire To" type="date" />
                                        </td>
                                    </tr>
                                    <c:field width="200" caption="{L-ENGBranch-L}{L-INACabang-L}" lov="LOV_Branch" name="stBranch" type="string" readonly="false" presentation="standard" changeaction="refresh" />
                                    <c:evaluate when="<%=!isBondingViewOnly%>" >
                                        <%--<c:field width="200" caption="{L-ENGPolicy Class-L}{L-INAPolicy Class-L}" lov="LOV_PolicyTypeGroup" name="stPolicyGroup" type="string" presentation="standard" />
                                        <c:field width="200" caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" lov="LOV_PolicyType" name="stPolicyType" type="string" presentation="standard" changeaction="refresh" />
                                        --%>
                                        <c:field width="200" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroup" caption="{L-ENGPolicy Class-L}{L-INAKelas Polis-L}" name="stPolicyGroup" descfield="stPolicyTypeGroupDesc" type="string" presentation="standard" />
                                     <c:field width="200" show="<%=form.getStPolicyGroup()!=null%>" lov="LOV_PolicyType" caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" name="stPolicyTypeID" descfield="stPolicyTypeDesc" type="string" presentation="standard">
                                        <c:lovLink name="polgroup" link="stPolicyGroup" clientLink="false" />
                                     </c:field>
                                    </c:evaluate>
                                </table>
                            </td>
                            <td>
                                
                                <table>
                                    <c:field caption="{L-ENGShow active &amp; Pending only-L}{L-INAPolis Aktif &amp; Pending-L} " name="stOutstandingFlag" type="check" presentation="standard" changeaction="refresh" />
                                     <c:evaluate when="<%=policy%>" >
                                         <c:field caption="{L-ENGShow Pending only-L}{L-INAData Belum Disetujui-L} " name="stNotApprovedPolicy" type="check" presentation="standard" changeaction="refresh" />
                                     </c:evaluate>
                                         <c:evaluate when="<%=reas%>" >
                                        <c:field caption="{L-ENGShow Not Approved By R/I Dept.-L}{L-INABelum Disetujui R/I-L} " name="stShowReinsNotApproved" type="check" presentation="standard" changeaction="refresh" />
                                    </c:evaluate>
                                    <c:evaluate when="<%=claim%>" >
                                        <c:field caption="{L-ENGShow Claim Only-L}{L-INAData Klaim-L} " name="stShowClaimsOnly" type="check" presentation="standard" changeaction="refresh" />
                                    </c:evaluate>
                                    <c:evaluate when="<%=claim%>" >
                                        <c:field caption="{L-ENGShow Claim Not Approved-L}{L-INAKlaim Belum disetujui-L} " name="stShowClaimsNotApproved" type="check" presentation="standard" changeaction="refresh" />
                                    </c:evaluate>
                                    
                                   <c:evaluate when="<%=!claim&&!reas%>" >
                                         <c:field width="160" caption="{L-ENGPolicy No-L}{L-INANomor Polis-L} " name="stPolicyNo" type="string" presentation="standard" />
                                   </c:evaluate>
                                </table>
                               
                            </td>
                        </tr>
                        <tr>
                            <td><c:button text="Refresh" event="refresh" /></td>
                        </tr>
                    </table>
                </td>
            </tr>
        
        <tr>
            <td>
                <c:listbox name="listMandiri" autofilter="true" selectable="true" paging="true" view="com.webfin.insurance.model.InsurancePolicyView" >
                    <%
                    final InsurancePolicyView pol = (InsurancePolicyView) current;
                    %>
                    <c:listcol name="stPolicyID" title="ID" selectid="policyID"/>
                    <c:listcol title="Act" name="stActiveFlag" flag="true" />
                    <c:listcol name="stEffectiveFlag" title="Eff" flag="true" />
                    <%--<c:listcol name="stPrintFlag" title="Print" flag="true" />--%>
                   
                    <c:evaluate when="<%=claim%>" >
                        <c:listcol filterable="true" name="stClaimStatus" title="Status Klaim" />
                        <c:listcol filterable="true" name="stPLANo" title="{L-ENGPLA No-L}{L-INANomor LKS-L}" />
                        <c:listcol filterable="true" name="stDLANo" title="{L-ENGDLA No-L}{L-INANomor LKP-L}" />
                        <%--  <c:listcol align="right" name="dbClaimAmountEstimate" title="{L-ENGClaim Estimated-L}{L-INAKlaim Perkiraan-L}" />
               <c:listcol align="right" name="dbClaimAmountApproved" title="{L-ENGClaim Approved-L}{L-INAKlaim Disetujui-L}" />
                        --%><c:listcol align="right" title="Jumlah Klaim" ><%=JSPUtil.print(pol.isStatusClaimPLA()?pol.getDbClaimAmountEstimate():pol.getDbClaimAmountApproved(),2)%></c:listcol>
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
                    <c:listcol filterable="true" name="stStatus" title="Status" />
                    <c:listcol align="right" title="TSI" ><%=JSPUtil.print(pol.isStatusEndorse()?pol.getDbInsuredAmountEndorse():pol.getDbInsuredAmount(),2)%></c:listcol>
                    <c:listcol align="right" title="Premi" ><%=JSPUtil.print(pol.getDbPremiTotal(),2)%></c:listcol>
                    <c:listcol filterable="true" name="stPolicyID" title="ID" />
             
                    <c:listcol name="dtCreateDate" title="Create Date" />
                    
                </c:listbox>
            </td>
        </tr>
        <tr>
            <td> 
                <c:button show="true"  text="{L-ENGCreate-L}{L-INABuat-L}" event="clickCreateSPPAMandiri"/>
                <c:button show="true"  text="{L-ENGEdit-L}{L-INAUbah-L}" event="clickEditMandiri" />
                <c:button show="true"  text="{L-ENGView-L}{L-INALihat-L}" event="clickViewMandiri" />
                
                <c:button show="<%=form.isCanApprove()%>"  text="{L-ENGApproval-L}{L-INASetujui-L}" event="clickApproval" />
                
                <%--<c:button  text="Print" clientEvent="<%="document.location='ins_pol1.pdf.rpt?policyid='+f.policyID.value+'&antic='+(new Date().getTime());"%>" validate="true" />--%>
                <%--<c:button  text="Print1" clientEvent="<%="document.location='x.pdf.ctl?EVENT=INS_POL_PRT&policyid='+f.policyID.value+'&antic='+(new Date().getTime());"%>" validate="true" />--%>
                <%--<c:button show="<%=form.isEnablePrint()%>"  text="Print 1" clientEvent="<%="document.location='x.fpc?EVENT=INS_POL_PRT&policyid='+f.policyID.value+'&antic='+(new Date().getTime());"%>" validate="true" />--%>
                <%--<c:button show="<%=form.isEnablePrint()%>"  text="Print 2" clientEvent="<%="document.location='x.fpc?EVENT=INS_POL_PRT&policyid='+f.policyID.value+'&poltype=0&antic='+(new Date().getTime());"%>" validate="true" />--%>
                <%--<%
   final LOV polPrint = LOVManager.getInstance().getLOV("VS_POL_PRINTING",null);

   final Iterator it = polPrint.getCodeIterator();

   while (it.hasNext()) {
      String code = (String) it.next();

      final String text = polPrint.getComboDesc(code);
%>
         <c:button  text="<%="Print "+text%>" clientEvent="<%="document.location='x.fpc?EVENT=INS_POL_PRT&policyid='+f.policyID.value+'&poltype=0&alter="+code+"&antic='+(new Date().getTime());"%>" validate="true" />
<% } %>--%>
            </td>
        </tr>
        <tr>
            <td>
                <%if(form.isEnableEndorsement()){%>Endorse <%}%> <c:field name="stEndorseMode" width="180" type="string" show="<%=form.isEnableEndorsement()%>" lov="LOV_EndorseMode" />
                <c:button show="<%=form.isEnableEndorsement()%>"  text="{L-ENGCreate-L}{L-INABuat-L} Endorsemen" event="clickCreateEndorseByMode"/>
               <c:button show="<%=form.isEnableRenewal()%>"  text="{L-ENGCreate Renewal-L}{L-INABuat Perpanjangan-L}" event="clickCreateRenewal"/>
               <c:button show="<%=form.isEnableInputPaymentDate()%>"  text="{L-ENGPayment Input-L}{L-INAInput Tanggal Bayar-L}" event="clickEditInputPaymentDate" />
            </td>
        </tr>
        <tr>
            <td>
                <c:button show="<%=form.isCanCreateTemporaryPolicy()%>"  text="Buat Polis Sementara" event="clickCreateTemporaryPolicy"/>
                <c:button show="<%=form.IsCanInputManualPolicy() && form.isEnableCreatePolis()%>"  text="{L-ENGCreate-L}{L-INABuat-L} Polis No Manual" event="clickCreatePolisManual"/>
            
            </td>
        </tr>
        
        <c:evaluate when="<%=form.isEnableSuperEdit()%>" >
            <tr>
                <td>
                    Administrator Tools :
                </td>
            </tr>
            <tr>
                <td>
                    <c:button show="<%=form.isEnableSuperEdit()%>"  text="Super Edit" event="clickSuperEdit" />    
                    
                 </td>
            </tr>
        </c:evaluate>
       
     <%--
        <c:evaluate when="<%=form.isEnablePrint()%>" >
            <tr>
                <td>
                    Print <c:field name="stPrintForm" width="250" type="string" lov="LOV_POL_PRINTING" ><c:param name="vs" value="<%=form.getPrintingLOV()%>" /></c:field> in <c:field name="stLang" type="string" value="<%=LanguageManager.getInstance().getActiveLang()%>" lov="LOV_LANG" />
                    Font <c:field name="stFontSize" width="60" type="string" lov="LOV_FONTSIZE" />
                    Type <c:field name="stAttached" width="150" type="string" lov="LOV_POLATTACHED" />
                    <%if(claim){%>Authorized<%}%> <c:field show="<%=claim%>" name="stAuthorized" type="string" lov="LOV_AUTHORIZED" />
                </td>
            </tr>
            <tr>
                <td>
                     <c:button text="Preview" name="bpreviewx"  clientEvent="dynPreviewClick();" />
                    <c:button text="Print" name="bprintx"  clientEvent="dynPrintClick();" />
                </td>
            </tr>
        </c:evaluate>
        --%>
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