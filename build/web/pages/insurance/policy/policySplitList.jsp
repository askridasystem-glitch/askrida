<%@ page import="com.webfin.insurance.model.InsuranceSplitPolicyView,
com.crux.util.JSPUtil,
com.crux.lov.LOVManager,
com.crux.util.LOV, 
java.util.Iterator,
com.crux.lang.LanguageManager,
com.crux.web.controller.SessionManager,
com.webfin.insurance.form.PolicySplitListForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame >
    <%
    final PolicySplitListForm form = (PolicySplitListForm) frame.getForm();
    
    final boolean canEdit = form.isCanEdit();
    
    final boolean canNavigateBranch = form.isCanNavigateBranch();
    
    final boolean canNavigateRegion = form.isCanNavigateRegion();
    
    //final boolean isBondingViewOnly = form.isIsBondingDivision();
    
    boolean reas = form.isReas();
    
    boolean claim = form.isClaim();
    
    final boolean showFilter = form.isShowFilter();
    
    final boolean approveUW = form.isApproveUW();
    
    final boolean policy = form.isPolicy();
    
    final String ccCode = SessionManager.getInstance().getSession().getStBranch();
    
    boolean canSeeNoRekap = true;
     
    if(ccCode!=null){
        if(!ccCode.equalsIgnoreCase("00")) canSeeNoRekap = false;
    }
    %>
    <table cellpadding=2 cellspacing=1>
         <tr>
            <td>
                <c:evaluate when="<%=!showFilter%>" >
                    <img style="cursor:hand" src="/fin/images/show filter.png" onclick="mform.action_event.value='hideFilter';mform.submit();" />
                </c:evaluate>
                <c:evaluate when="<%=showFilter%>" >
                    <img style="cursor:hand" src="/fin/images/hide filter.png" onclick="mform.action_event.value='showFilter';mform.submit();" />
                </c:evaluate>
            </td>
        </tr>
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
                                    <c:evaluate when="<%=showFilter%>" > 
                                    <tr>
                                        <td>{L-ENGPolicy Expire-L}{L-INATanggal Berakhir-L}</td>
                                        <td>:</td>
                                        <td>
                                            <c:field name="dtFilterPolicyExpireFrom" caption="Policy Expire From" type="date" />
                                            {L-ENGTo-L}{L-INAS/D-L} <c:field name="dtFilterPolicyExpireTo" caption="Policy Expire To" type="date" />
                                        </td>
                                    </tr>
                                    </c:evaluate>
                                    <c:field width="200" caption="{L-ENGBranch-L}{L-INACabang-L}" lov="LOV_Branch" name="stBranch" type="string" readonly="<%=!canNavigateBranch%>" presentation="standard" changeaction="refresh" />
                                    
                                    <c:field show="<%=form.getStBranch()!=null%>" width="200" caption="{L-ENGRegion-L}{L-INARegion-L}" lov="LOV_Region" name="stRegion" type="string" readonly="<%=!canNavigateRegion%>" presentation="standard" changeaction="refresh" >
                                        <c:lovLink name="cc_code" link="stBranch" clientLink="false"/>
                                    </c:field>
                                        <c:field width="200" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroup" caption="{L-ENGPolicy Class-L}{L-INAKelas Polis-L}" name="stPolicyGroup" descfield="stPolicyTypeGroupDesc" type="string" presentation="standard" />
                                     <c:field width="200" show="<%=form.getStPolicyGroup()!=null%>" lov="LOV_PolicyType" caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" name="stPolicyTypeID" descfield="stPolicyTypeDesc" type="string" presentation="standard">
                                        <c:lovLink name="polgroup" link="stPolicyGroup" clientLink="false" />
                                     </c:field>
                                     <c:evaluate when="<%=showFilter%>" > 
                                         <c:field width="200" caption="{L-ENGStatus-L}{L-INAStatus-L} " lov="LOV_PolicyLevel" name="stPolicyStatus" type="string" presentation="standard" />
                                         <c:field width="200" caption="{L-ENGCustomer-L}{L-INACustomer-L} " name="stPolicyCustomer" type="string" presentation="standard" />
                                         <c:field width="200" caption="{L-ENGPrincipal-L}{L-INAPrincipal-L} " name="stPrincipal" type="string" presentation="standard" />
                                         <c:field caption="Belum Di print" name="stNotPrintedPolicy" type="check" presentation="standard" />
                                    </c:evaluate>
                                     
                                </table>
                            </td>
                            <td>
                                <table>
                                    <c:evaluate when="<%=showFilter%>" > 
                                        <c:field caption="{L-ENGShow active &amp; Pending only-L}{L-INAPolis Aktif &amp; Pending-L} " name="stOutstandingFlag" type="check" presentation="standard" changeaction="refresh" />
                                    </c:evaluate> 
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
                                              
                                    <c:evaluate when="<%=!claim && showFilter%>" >
                                    <c:field caption="{L-ENG TSI >= -L}{L-INA TSI >=-L} " name="dbOverLimit" width="200" type="money16.2" presentation="standard" />
                                    </c:evaluate>
                                    <c:evaluate when="<%=showFilter%>" >
                                        <c:field caption="{L-ENGOver Limit-L}{L-INAOver Limit-L} " name="stOverLimitPolicy" type="check" presentation="standard" changeaction="refresh" /> 
                                    </c:evaluate>
                                   <tr>
                                          <td> Filter : <c:field width="170" lov="VS_FILTER_UW" caption="Criteria" name="stCriteria" type="string"  /></td>
                                       <td> : </td>
                                       <td>
                                           <c:field width="200" caption="Key" name="stKey" type="string" />
                                       </td>
                                   </tr>                                   
                                 </table>
                            </td>
                            <td>
                                <table>
                                    <c:field caption="{L-ENGPages Limit-L}{L-INALimit Halaman-L}" name="stLimitFlag" width="80" type="string" presentation="standard" />
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td><c:button text="Refresh" event="refresh" /></td>
                        </tr>
                        <c:evaluate when="<%=reas%>">
                            <tr>
                                <td colspan="2">
                                    <c:button show="<%=form.isEnableEdit()%>"  text="{L-ENGEdit-L}{L-INAUbah-L}" event="clickEdit" />
                                    <c:button show="<%=form.isEnableView()%>"  text="{L-ENGView-L}{L-INALihat-L}" event="clickView" />
                                    <c:button show="<%=reas%>"  text="{L-ENGApproval Reins-L}{L-INASetujui Reas-L}" event="clickApprovalReins" />
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    R/I Edit Only : &nbsp;
                                    <c:button show="<%=reas || claim%>"  text="Reverse & Ubah (R/I)" event="clickEditReinsurance" />
                                    <c:button show="<%= (reas && form.isCanApproveReins()) || claim%>"  text="Setujui (R/I)" event="clickApprovalReinsOnly" />

                                </td>
                            </tr>
                        </c:evaluate>
                        
                    </table>
                </td>
            </tr>
        <tr>
            <td>
                <c:listbox name="list" autofilter="true" selectable="true" paging="true" view="com.webfin.insurance.model.InsuranceSplitPolicyView">
                    <%
                    final InsuranceSplitPolicyView pol = (InsuranceSplitPolicyView) current; 
                    %>
                    <c:listcol name="stPolicyID" title="ID" selectid="policyID"/>
                    <c:listcol title="Act" name="stActiveFlag" flag="true" />
                    <c:listcol name="stEffectiveFlag" title="Eff" flag="true" />
                    <%--<c:listcol name="stPrintFlag" title="Print" flag="true" />--%>
                    <b>
                        <c:listcol filterable="true" name="stPolicyNo" title="{L-ENGPolicy No-L}{L-INANomor Rekap-L}" />
                    </b>                   
                    <c:listcol filterable="true" name="stPolicyTypeDesc" title="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" />                    
                    
                    <c:listcol name="stJumlahObjek" title="Jumlah data" />
                    <c:listcol name="stJumlahObjekTerproses" title="Jumlah data Terproses" />
                    <c:listcol align="right" title="TSI" ><%=JSPUtil.print(pol.isStatusEndorse()?pol.getDbInsuredAmountEndorse():pol.getDbInsuredAmount(),2)%></c:listcol>
                    <c:listcol align="right" title="Premi" ><%=JSPUtil.print(pol.getDbPremiTotal(),2)%></c:listcol>
                    <c:listcol filterable="true" name="stPolicyID" title="ID" />
                    <c:listcol filterable="true" name="stApprovedWho" title="Approved By" />
                    <c:listcol name="stApprovedName" title="Approved By" />
                    <c:listcol name="dtApprovedDate" title="Approved Date" />
                   
                    <c:listcol filterable="true" name="stCreateWho" title="User ID" />
                    <c:listcol  name="stCreateName" title="User Name" />
                    <c:listcol name="dtCreateDate" title="Create Date" />
                   
                    

                      <%--
                    <c:evaluate when="<%=canSeeNoRekap&&!reas%>">
                        <c:listcol title="No Rekap" ><%=JSPUtil.print(pol.getStReference3())%></c:listcol>
                        <c:listcol title="No Rekap" ><%=JSPUtil.print(pol.getStReference4())%></c:listcol>
                    </c:evaluate>--%>
                </c:listbox>
            </td>
        </tr>
        <tr> 
            <td>                                                                 
                <c:button show="<%=form.isEnableCreateProposal()%>"  text="{L-ENGCreate-L}{L-INABuat-L}" event="clickCreate" />
                <c:button show="<%=form.isEnableCreateProduction()%>"  text="{L-ENGCreate-L}{L-INABuat-L} Production" event="clickCreateProd" />
                <c:button show="<%=form.isEnableEdit()%>"  text="{L-ENGEdit-L}{L-INAUbah-L}" event="clickEdit" />
                <c:button show="<%=form.isEnableView()%>"  text="{L-ENGView-L}{L-INALihat-L}" event="clickView" />
                
                
                <c:button show="<%=form.isCanApprove()%>"  text="{L-ENGApproval-L}{L-INASetujui-L}" event="clickApproval" />
                <c:button show="<%=form.isCanApprove()%>"  text="Proses Split" event="clickSplitProcess" confirm="Yakin ingin diproses ?" />
                <c:button show="true"  text="Print Excel" event="clickPrint" confirm="Yakin ingin print ?" />
                
                
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
         frmx.src='x.fpc?EVENT=INS_POL_PRT&policyid='+f.policyID.value+'&alter='+f.stPrintForm.value+'&vs='+f.vs.value+'&xlang='+f.stLang.value+'&fontsize='+f.stFontSize.value+'&authorized='+f.stAuthorized.value+'&attached='+f.stAttached.value+'&antic='+(new Date().getTime());
         return;
      }

      openDialog('w.ctl?EVENT=INS_PRT_NOM',400,50,
         function (o) {
            if (o!=null) {
               //alert(o);               
            	frmx.src='x.fpc?EVENT=INS_POL_PRT&nom='+o+'&policyid='+f.policyID.value+'&alter='+f.stPrintForm.value+'&vs='+f.vs.value+'&xlang='+f.stLang.value+'&fontsize='+f.stFontSize.value+'&authorized='+f.stAuthorized.value+'&attached='+f.stAttached.value+'&antic='+(new Date().getTime());
                    	
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
         frmx.src='x.fpc?EVENT=INS_POL_PRV&policyid='+f.policyID.value+'&alter='+f.stPrintForm.value+'&xlang='+f.stLang.value+'&fontsize='+f.stFontSize.value+'&authorized='+f.stAuthorized.value+'&attached='+f.stAttached.value+'&antic='+(new Date().getTime())+'&preview='+f.bpreviewx.value;
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

   function dynPrintDigitalSignClick() {

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

      var digitalsign = 'Y';

      if (!requireNom) {
         frmx.src='x.fpc?EVENT=INS_POL_PRT_DIGITALSIGN&policyid='+f.policyID.value+'&alter='+f.stPrintForm.value+'&vs='+f.vs.value+'&xlang='+f.stLang.value+'&fontsize='+f.stFontSize.value+'&authorized='+f.stAuthorized.value+'&attached='+f.stAttached.value+'&antic='+(new Date().getTime())+'&digitalsign='+ digitalsign;
         return;
      }

      openDialog('w.ctl?EVENT=INS_PRT_NOM',400,50,
         function (o) {
            if (o!=null) {
               //alert(o);
            	frmx.src='x.fpc?EVENT=INS_POL_PRT_DIGITALSIGN&nom='+o+'&policyid='+f.policyID.value+'&alter='+f.stPrintForm.value+'&vs='+f.vs.value+'&xlang='+f.stLang.value+'&fontsize='+f.stFontSize.value+'&authorized='+f.stAuthorized.value+'&attached='+f.stAttached.value+'&antic='+(new Date().getTime());

            }
         }
      );
   }

   function dynPrintDigitalPolicyClick() {

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

      var digitalsign = 'Y';

      if (!requireNom) {
         frmx.src='x.fpc?EVENT=INS_POL_PRT_DIGITALPOLICY&policyid='+f.policyID.value+'&alter='+f.stPrintForm.value+'&vs='+f.vs.value+'&xlang='+f.stLang.value+'&fontsize='+f.stFontSize.value+'&authorized='+f.stAuthorized.value+'&attached='+f.stAttached.value+'&antic='+(new Date().getTime())+'&digitalsign='+ digitalsign;
         return;
      }

      openDialog('w.ctl?EVENT=INS_PRT_NOM',400,50,
         function (o) {
            if (o!=null) {

            	frmx.src='x.fpc?EVENT=INS_POL_PRT_DIGITALPOLICY&nom='+o+'&policyid='+f.policyID.value+'&alter='+f.stPrintForm.value+'&vs='+f.vs.value+'&xlang='+f.stLang.value+'&fontsize='+f.stFontSize.value+'&authorized='+f.stAuthorized.value+'&attached='+f.stAttached.value+'&antic='+(new Date().getTime());

            }
         }
      );
   }

</script>