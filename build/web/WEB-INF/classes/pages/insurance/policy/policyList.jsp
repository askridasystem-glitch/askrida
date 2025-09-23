<%@ page import="com.webfin.insurance.model.InsurancePolicyView,
com.crux.util.JSPUtil,
com.crux.lov.LOVManager,
com.crux.util.LOV, 
java.util.Iterator,
com.crux.lang.LanguageManager,
com.crux.web.controller.SessionManager,
com.webfin.insurance.form.PolicyListForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame >
    <%
    final PolicyListForm form = (PolicyListForm) frame.getForm();
    
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
                                    <c:field width="200" caption="{L-ENGBranch-L}{L-INACabang-L} Penerbit" lov="LOV_Branch" name="stBranchSource" type="string" readonly="<%=!canNavigateBranch%>" presentation="standard" changeaction="refresh" />

                                        <c:field width="200" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroup" caption="{L-ENGPolicy Class-L}{L-INAKelas Polis-L}" name="stPolicyGroup" descfield="stPolicyTypeGroupDesc" type="string" presentation="standard" />
                                     <c:field width="200" show="<%=form.getStPolicyGroup()!=null%>" lov="LOV_PolicyType" caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" name="stPolicyTypeID" descfield="stPolicyTypeDesc" type="string" presentation="standard">
                                        <c:lovLink name="polgroup" link="stPolicyGroup" clientLink="false" />
                                     </c:field>
                                     <c:evaluate when="<%=showFilter%>" > 
                                         
                                         <c:field width="200" caption="{L-ENGCustomer-L}{L-INACustomer-L} " name="stPolicyCustomer" type="string" presentation="standard" />
                                         <c:field width="200" caption="{L-ENGPrincipal-L}{L-INAPrincipal-L} " name="stPrincipal" type="string" presentation="standard" />
                                         
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
                                    <c:field caption="{L-ENGReject Only-L}{L-INAData Ditolak-L} " name="stShowDataReject" type="check" presentation="standard" changeaction="refresh" />
                                    <c:field caption="Belum Di print" name="stNotPrintedPolicy" type="check" presentation="standard" />
                                    
                                   
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
                                    <c:field width="200" caption="{L-ENGStatus-L}{L-INAStatus-L} " lov="LOV_PolicyLevel" name="stPolicyStatus" type="string" presentation="standard" />
                                    <c:field caption="No Rekening Pinjaman" name="stNoRekeningPinjaman" width="200" type="string" presentation="standard" />
                                    <c:field caption="{L-ENGInterconnection Data-L}{L-INAData Interkoneksi-L} " name="stGatewayData" type="check" presentation="standard" changeaction="refresh" />
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
                <c:listbox name="list" autofilter="true" selectable="true" paging="true" view="com.webfin.insurance.model.InsurancePolicyView">
                    <%
                        final InsurancePolicyView pol = (InsurancePolicyView) current;
                    %>
                    <c:listcol name="stPolicyID" title="ID" selectid="policyID"/>
                    
                     
                    <c:listcol title="Act" name="stActiveFlag" flag="true" />
                    <c:listcol name="stEffectiveFlag" title="Eff" flag="true" />
                    <%--<c:listcol name="stPrintFlag" title="Print" flag="true" />--%>
                    <c:evaluate when="<%=!reas%>" >
                        <c:listcol name="stDocumentPrintFlag" title="Prt" flag="true" />
                        <c:listcol name="stReadyToApproveFlag" title="Vld" flag="true" />
                    </c:evaluate>
                    <c:listcol name="stRIFinishFlag" title="RI" flag="true" />
                    
                    <b>
                        <c:listcol filterable="true" name="stPolicyNo" title="{L-ENGPolicy No-L}{L-INANomor Polis-L}" />
                    </b>
                    <c:evaluate when="<%=!reas%>">
                        <c:listcol filterable="true" name="stReference1" title="No P. Prinsip" />
                    </c:evaluate>
                    
                    <c:evaluate when="<%=claim%>" >
                        <c:listcol filterable="true" name="stClaimStatus" title="Status Klaim" />
                        <c:listcol filterable="true" name="stPLANo" title="{L-ENGPLA No-L}{L-INANomor LKS-L}" />
                        <c:listcol filterable="true" name="stDLANo" title="{L-ENGDLA No-L}{L-INANomor LKP-L}" />
                        <%--  <c:listcol align="right" name="dbClaimAmountEstimate" title="{L-ENGClaim Estimated-L}{L-INAKlaim Perkiraan-L}" />
               <c:listcol align="right" name="dbClaimAmountApproved" title="{L-ENGClaim Approved-L}{L-INAKlaim Disetujui-L}" />
                        --%><c:listcol align="right" title="Jumlah Klaim" name="dbClaimAmount" />
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
                    
                    <c:listcol filterable="true" name="stReference5" title="Principal"/>
                    <c:listcol filterable="true" name="stReference10" title="Pekerjaan"/>
                    <c:listcol filterable="true" name="stStatus" title="Status" />
                    <c:listcol align="right" title="TSI" ><%=JSPUtil.print(pol.isStatusEndorse()?pol.getDbInsuredAmountEndorse():pol.getDbInsuredAmount(),2)%></c:listcol>
                    <c:listcol align="right" title="Premi" ><%=JSPUtil.print(pol.getDbPremiTotal(),2)%></c:listcol>
                    <c:listcol filterable="true" name="stPolicyID" title="ID" />
                    
                    <c:listcol filterable="true" name="stApprovedWho" title="Approved By" />
                    <c:listcol name="stApprovedName" title="Approved By" />
                    <c:listcol name="dtApprovedDate" title="Approved Date" />
                    <c:evaluate when="<%=!claim%>">
                        <c:listcol name="dtPaymentDate" title="Payment Date" />
                    </c:evaluate>
                    <c:evaluate when="<%=claim%>">
                        <c:listcol name="dtClaimPaymentDate2" title="Claim Payment Date" />
                    </c:evaluate>
                    <c:listcol filterable="true" name="stCreateWho" title="User ID" />
                    <c:listcol  name="stCreateName" title="User Name" />
                    <c:listcol name="dtCreateDate" title="Create Date" />
                    <c:evaluate when="<%=policy%>" >
                        <c:listcol title="" >
                            <c:button show="<%=pol.isStatusSPPA() && pol.isEffective()%>" confirm="Yakin mau buat polis ?" text="Buat Polis" event="clickCreatePolisButton" clientEvent="<%="docEl('policyID').value="+pol.getStPolicyID()+";"%>" />
                        </c:listcol>
                    </c:evaluate>
                    <c:listcol filterable="true" name="stCostCenterCodeSource" title="Cabang Penerbit" />
                    <c:evaluate when="<%=reas%>">
                        <c:listcol filterable="true" name="stReinsuranceApprovedWho" title="Approved R/I" />
                        <c:listcol name="stApprovedReinsName" title="Approved R/I" />
                    </c:evaluate>
                    <c:listcol filterable="true" name="stReference2Desc" title="Polis Induk" />
                     <c:listcol filterable="true" name="stParentPolicyNo" title="Polis Sebelumnya" />
                      <c:listcol filterable="true" name="stCoinsPolicyNo" title="Polis Rujukan" />
                      <c:listcol filterable="true" name="stReferenceNo" title="Nomor Referensi" />
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
                <c:button show="<%=form.isEnableCreateProposal()%>"  text="{L-ENGCreate-L}{L-INABuat-L} {L-ENGQuotation-L}{L-INAPenawaran-L}" event="clickCreate" clientEvent=""/>
                <c:button show="<%=form.isEnableCreateProduction()%>"  text="{L-ENGCreate-L}{L-INABuat-L} Production" event="clickCreateProd" />
                
                <c:evaluate when="<%=!reas%>">
                    <c:button show="<%=form.isEnableEdit()%>"  text="{L-ENGEdit-L}{L-INAUbah-L}" event="clickEdit" />
                </c:evaluate>
                <c:evaluate when="<%=reas%>">
                    <c:button show="<%=form.isEnableEdit()%>"  text="{L-ENGEdit-L}{L-INAUbah-L}" event="clickEditReinsurance2" />
                </c:evaluate>

                <c:button show="<%=form.isEnableView()%>"  text="{L-ENGView-L}{L-INALihat-L}" event="clickView" />
                <c:button show="<%=form.isEnableCreateSPPA()%>"  text="{L-ENGCreate-L}{L-INABuat-L} SPPA" event="clickCreateSPPA"/>
                
                <c:button show="<%=form.isCanApprove()%>"  text="{L-ENGApproval-L}{L-INASetujui-L}" event="clickApproval" />
                <c:button show="<%=form.isCanApprove() || form.isCanApproveReins()%>"  text="{L-ENGReject-L}{L-INATolak-L}" event="clickReject" />

                <c:button show="<%=form.IsCanReverse() && !reas %>"  text="{L-ENGReverse-L}{L-INAReverse-L}" event="clickReverseUW" />
                
                <c:button show="<%=form.isEnableCreatePolis()%>"  text="{L-ENGCreate-L}{L-INABuat-L} Polis" event="clickCreatePolis"/>
                <%--<c:button show="<%=form.isEnableCreatePolis()%>"  text="{L-ENGCreate-L}{L-INABentuk-L} Nomor Polis" event="clickCreatePolisTrial"/>--%>
                <c:button show="<%=form.isCanCreatePolicyHistory()%>"  text="{L-ENGCreate History Policy-L}{L-INABuat Polis History-L}" event="clickCreatePolicyHistory"/>
                <c:evaluate when="<%=policy%>" >
                    <c:button show="<%=form.isApprovalByDirector()%>"  text="{L-ENGApproval By Director-L}{L-INASetujui(Direksi)-L}" event="clickApprovalByDirector" />
                    <c:button show="<%=form.isApprovalByDivisi()%>"  text="{L-ENGApproval By Divisi-L}{L-INASetujui(Divisi)-L}" event="clickApprovalByDivisi" />
                </c:evaluate>
                <c:evaluate when="<%=reas%>">
                    <c:button show="<%=form.isReinsApprovalByDirector()%>"  text="{L-ENGApproval By Director-L}{L-INASetujui(Direksi)-L}" event="clickApprovalByDirector" />
                </c:evaluate>
                <c:button show="<%=form.isEnableClaimPLA() && form.isCanCreate()%>"  text="{L-ENGCreate Claim (PLA)-L}{L-INABuat Klaim (LKS)-L}"  event="clickCreateClaimPLA"/>
                <c:button show="<%=form.isEnableClaimDLA() && form.isCanCreate()%>"  text="{L-ENGCreate Claim (DLA)-L}{L-INABuat Klaim (LKP)-L}"  event="clickCreateClaimDLA"/>
                <c:button show="<%=form.isEnableClaimDLA() && form.isCanCreate()%>"  text="{L-ENGCreate Claim Endorsement-L}{L-INABuat Klaim Endorse-L}"  event="clickCreateClaimEndorse"/>
                <c:button show="<%=form.isEnableClaim2()%>"  text="{L-ENGEdit No-L}{L-INAUbah No Surat-L}" event="clickEditClaim" />
                
                <c:button show="<%=form.isCanCreateClaimInward() && form.isCanCreate()%>"  text="{L-ENGCreate Claim Inward-L}{L-INABuat Klaim Inward-L}"  event="clickCreateClaimInward"/>
                <c:button show="<%=form.isCanCreateClaimInward() && form.isCanCreate()%>"  text="{L-ENGCreate Endorse Claim Inward-L}{L-INABuat Endorse Klaim Inward-L}"  event="clickCreateEndorseClaimInward"/>

                <c:button show="<%=approveUW%>"  text="{L-ENGApproval U/W-L}{L-INASetujui U/W-L}" event="clickApprovalReins" />
                <c:button show="<%=approveUW%>"  text="{L-ENGApproval U/W-L}{L-INABuat Endorse INT-L}" event="clickCreateEndorseIntern" />

                <c:button show="<%=reas%>"  text="{L-ENGApproval Reins-L}{L-INASetujui Reas-L}" event="clickApprovalReins" />
                <c:button show="<%=reas%>"  text="{L-ENGCreate R/I Endorse-L}{L-INABuat Endorse R/I-L}" event="clickCreateEndorseRI" />
                <c:button show="<%=reas%>"  text="{L-ENGCreate Inward-L}{L-INABuat Inward-L}" event="clickCreateInward" />
                <c:button show="<%=reas%>"  text="Proses Update Kurs Inward To Outward" event="clickUpdateKursInwardBulanan" />

                <c:button show="<%=form.isCanApproveAnalisaResiko()%>"  text="{L-ENGRisk Analysis Approval-L}{L-INASetujui Analisa Resiko-L}" event="clickApprovalAnalisaResiko" />

                <%--<c:button show="<%=reas%>"  text="Export To Excel" event="clickExcelReas" />
                
                <c:button  text="Print" clientEvent="<%="document.location='ins_pol1.pdf.rpt?policyid='+f.policyID.value+'&antic='+(new Date().getTime());"%>" validate="true" />--%>
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
                <c:evaluate when="<%=claim%>" >
                    <c:button show="<%=form.isClaimApprovalByDirector()%>"  text="{L-ENGApproval By Director-L}{L-INASetujui Oleh Direksi-L}" event="clickApprovalByDirector" />
                    <c:button show="<%=form.isClaimApprovalByDirector()%>"  text="{L-ENGApproval By Division-L}{L-INASetujui Kadiv-L}" event="clickApprovalByDivision" />
                    <c:button show="<%=form.isClaimApprovalByDivisi()%>"  text="{L-ENGApproval By Division-L}{L-INASetujui Resiko Klaim-L}" event="clickApprovalClaimByDivision" />
                </c:evaluate>
            </td>
        </tr>
        <tr>
            <td>
               <%if(form.isEnableEndorsement()){%>Endorse <%}%> <c:field name="stEndorseMode" width="250" type="string" show="<%=form.isEnableEndorsement()%>" lov="LOV_EndorseMode" />
                <c:button show="<%=form.isEnableEndorsement()%>"  text="{L-ENGCreate-L}{L-INABuat-L}<br>Endorsemen" event="clickCreateEndorseByMode"/>
               <c:button show="<%=form.isEnableRenewal()%>"  text="{L-ENGCreate<br>Renewal-L}{L-INABuat<br>Perpanjangan-L}" event="clickCreateRenewal"/>
               <c:button show="<%=form.isEnableRenewal()%>"  text="Perpanjangan<br>Sisa Premi" event="createRenewalSerbaguna" confirm="Yakin ingin bentuk Polis perpanjangan atas sisa premi ?"/>
               <c:button show="<%=form.isEnableInputPaymentDate()%>"  text="{L-ENGPayment<br>Input-L}{L-INAInput<br>Tanggal Bayar-L}" event="clickEditInputPaymentDate" />
               <c:button show="<%=form.isCanDeleteSignCode() && !reas && !claim%>"  text="{L-ENGRe-Print<br>Policy-L}{L-INACetak<br>Ulang Polis-L}" event="clickDeleteSignCode" />
               <c:button show="<%=form.isEnableEndorsement()%>"  text="Endorse Pembatalan<br>& Polis Baru" confirm="Yakin ingin dibatalkan dan dibuatkan polis baru ?\nEndorsemen Akan Disetujui Secara Otomatis \nPolis baru nya harap diisi dengan benar" event="clickCreateEndorseBatalAndNewPolicy" />
               <c:button show="<%=form.isEnableRenewal()||form.isEnableCreateProposal()%>" confirm="Yakin ingin diubah menjadi produksi cabang ?" text="Ubah Polis Non AKS<br>ke Cabang" event="changePolisToAKS"/>

            </td>
        </tr>
        <tr>
            <td>
                 <%--<%if(form.isEnableRenewalNonAKS()){%><br> No Polis Sebelumnya <%}%>  <c:field name="stPolicyNoRenewal" width="170" type="string" show="<%=form.isEnableRenewalNonAKS()%>" />
                <%--<c:button show="<%=form.isEnableRenewalNonAKS()%>"  text="{L-ENGCreate<br>Renewal-L}{L-INAPerpanjangan-L} Non AKS" event="clickCreateRenewalNonAKS"/>--%>
                <%if(form.isEnableRenewal()){%><br> No Polis Sebelumnya <%}%>  <c:field name="stPolicyNoRenewal" width="170" type="string" show="<%=form.isEnableRenewal()%>" />
                <c:button show="<%=form.isEnableRenewal()%>"  text="{L-ENGCreate<br>Renewal-L}{L-INAPerpanjangan-L} ke Cabang" event="clickCreateRenewalToCabang"/>
                <%--
                    <c:button show="<%=form.isCanCreateTemporaryPolicy()%>"  text="Buat Polis Sementara" event="clickCreateTemporaryPolicy"/>
                    <c:button show="<%=form.IsCanInputManualPolicy() && form.isEnableCreatePolis()%>"  text="{L-ENGCreate-L}{L-INABuat-L} Polis No Manual" event="clickCreatePolisManual"/>
                --%>
            </td>
        </tr>
        <c:evaluate when="<%=reas%>" >
            <tr>
                <td>
                    Reinsurance Edit Only :
                </td> 
            </tr> 
            <tr>
                <td>
                    <c:button show="<%=reas || claim%>"  text="Reverse & Ubah (R/I)" event="clickEditReinsurance" />
                    <%--<c:button show="<%=(reas && form.isCanReverseReins()) || claim%>"  text="Reverse (R/I)" event="clickReverseReinsurance" />--%>
                    <c:button show="<%= (reas && form.isCanApproveReins()) || claim%>"  text="Setujui (R/I)" event="clickApprovalReinsOnly" />
                </td>
            </tr>
        </c:evaluate>

        <c:evaluate when="<%=form.isEnableSuperEdit()%>" >
        <%--<c:evaluate when="false" >--%>
            <tr>
                <td>
                    Administrator Only :
                </td> 
            </tr>  
            <tr>
                <td>
                    <%--<c:button show="<%=form.isEnableSuperEdit() && !reas%>"  text="Edit Keterangan" event="clickEditKeterangan" />
                    <c:button show="<%=form.isEnableSuperEdit() && reas%>"  text="Super Edit Reas" event="clickSuperEditReas" />
                    <c:button show="<%=form.isEnableSuperEdit()%>"  text="{L-ENGReverse-L}{L-INAReverse-L}" event="clickReverse" />
                   --%> <c:button show="<%=form.isEnableSuperEdit()%>"  text="{L-ENGReApprove-L}{L-INAApprove Ulang-L}" event="clickReApproval" />
                    <c:button show="<%=form.isEnableSuperEdit()%>"  text="Super Edit" event="clickSuperEdit" />
                    <c:button show="<%=form.isEnableSuperEdit()%>"  text="Mutakhir Spreading Klaim" event="clickChangeKlaimTreatyByDate" />
                     <c:button show="<%=form.isEnableSuperEdit()%>"  text="proses endorse brokerfee H2H mantap" event="createEndorseKomisiMantapBrokerPakarData" />
                     <c:button show="<%=form.isEnableSuperEdit()%>"  text="CREATE ENDORSE RI" event="createEndorsemenRIByDATA" />
                     <c:button show="<%=form.isEnableSuperEdit()%>"  text="Jurnal Ulang" event="clickJurnalUlangPerBulan" />
                     <c:button show="<%=form.isEnableSuperEdit()%>"  text="Proses Ulang Pajak" event="clickReApproveKumpulan" />
                    <%--<c:button show="<%=form.isEnableSuperEdit()%>"  text="Proses UbAH TREATY" event="clickChangeTreatyByDate3" />
                    <

                    <c:button show="<%=form.isEnableSuperEdit()%>"  text="Proses Deductible H2H Sumut" event="clickAddDeductibleH2HFire" />
                    

                    <c:button show="<%=form.isEnableSuperEdit()%>"  text="prose endorse H2H mantap" event="createEndorsemenKomisiByData" />
                    <c:button show="<%=form.isEnableSuperEdit()%>"  text="Ubah password non aks" event="clickChangePassword" />
                    <%--
                    
                    

                    
                    <%--<c:button show="<%=form.isEnableSuperEdit()%>"  text="prose endorse premi mantap" event="clickEditEndorseKumpulan" />
                   

                    <%-- <c:button show="<%=form.isEnableSuperEdit()%>"  text="EDIT KUMPULAN" event="clickEditPolisKumpulan" />
                     <c:button show="<%=form.isEnableSuperEdit()%>"  text="prose endorse tgl lahir" event="createEndorsemenByDATA" />
             
                     <%--<c:button show="<%=form.isEnableSuperEdit()%>"  text="Jurnal Ulang" event="clickJurnalUlangPerBulan" />
        
                     <c:button show="<%=form.isEnableSuperEdit()%>"  text="PROSES Penabung" event="clickEditPolisKumpulan" />
                    
                     <c:button show="<%=form.isEnableSuperEdit()%>"  text="Proses UbAH BPPDAN" event="clickChangeTreatyOnlyByDate" />
                     <c:button show="<%=form.isEnableSuperEdit()%>"  text="Jurnal Ulang" event="clickJurnalUlangPerBulan" />
                     <c:button show="<%=form.isEnableSuperEdit()%>"  text="MUtakhir Data Klaim" event="clickChangeKlaimTreatyByDate" />
                     <c:button show="<%=form.isEnableSuperEdit()%>"  text="PROSES HISTORY" event="clickEditOtomatis" />
                     <c:button show="<%=form.isEnableSuperEdit()%>"  text="PROSES ENDORSE PPN" event="createEndorsemenByDATA" />
                     <c:button show="<%=form.isEnableSuperEdit()%>"  text="PROSES APP PENABUNG" event="clickEditAndApproveKumpulan" />
                     <c:button show="<%=form.isEnableSuperEdit()%>"  text="PROSES EDIT KLAIM" event="clickEditKumpulan" />
                     <c:button show="<%=form.isEnableSuperEdit()%>"  text="PROSES TO INTERKONEKSI" event="clickProsesPolisToInterkoneksi" />
                     <%--<c:button show="<%=form.isEnableSuperEdit()%>"  text="REVERSE KUMP BANDUNG" event="clickReverseKumpulan" />
                     <c:button show="<%=form.isEnableSuperEdit()%>"  text="APPROVE KUMP BANDUNG" event="clickApproveKumpulan" />
                      --%>
                 </td>
            </tr>
        </c:evaluate>
       
     
        <c:evaluate when="<%=form.isEnablePrint()%>" >
            <tr>
                <td>
                    <input type="hidden" id="vs" value="<%=form.getPrintingLOV()%>"/>
                    Print <c:field name="stPrintForm" width="270" type="string" lov="LOV_POL_PRINTING" ><c:param name="vs" value="<%=form.getPrintingLOV()%>" /></c:field> in <c:field name="stLang" type="string" value="<%=LanguageManager.getInstance().getActiveLang()%>" lov="LOV_LANG" />
                    Font <c:field name="stFontSize" width="60" type="string" lov="LOV_FONTSIZE" />
                    Type <c:field name="stAttached" width="180" type="string" lov="LOV_POLATTACHED" /> 
                    <%if(claim){%>Authorized<%}%> <c:field show="<%=claim%>" name="stAuthorized" type="string" lov="LOV_AUTHORIZED" />
                </td>
            </tr>
            <tr>
                <td>
                    <%--<c:button text="Preview" name="bpreviewx"  event="previewPolicy" />--%>
                   
                    <c:button text="Preview" name="bpreviewx"  clientEvent="dynPreviewClick();" />
                    <c:button show="<%=form.isEnablePrintPreSign()&& !claim%>" text="Print" name="bprintx"  clientEvent="dynPrintClick();" />
                    <c:button show="<%=claim%>" text="Print" name="bprintx"  clientEvent="dynPrintClick();" />
                    <c:button show="<%=form.isEnablePrintDigitized()%>" text="Print Dengan Blangko" name="bprintdsx"  clientEvent="dynPrintDigitalSignClick();" />
                    <c:button show="<%=form.isEnablePrintDigitalPolicy()%>" text="Print Tanpa Blangko" name="bprintdpsx"  clientEvent="dynPrintDigitalPolicyClick();" />
                    <c:button show="<%=form.isEnableSuperEdit()%>" text="Polis Jiwa" name="bpreviewxx"  clientEvent="downloadEPolisPAJ();" />
                </td>
            </tr>
        </c:evaluate>
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

   var frmx = docEl('frmx');

   function dynPolisClick(clicked_id) {

    f.policyID.value = clicked_id;
    
      if(true){
            frmx.src='x.fpc?EVENT=INS_CREATE_POLIS&policyid='+f.policyID.value;
            return;
        }
   }

    function downloadEPolisPAJ() {

        //var nopolis = document.getElementById('selectedObject.stReference30').value;
        //var norek = document.getElementById('selectedObject.stReference16').value;
        //var entid = document.getElementById('policy.stEntityID').value;

        if(true){
            frmx.src='x.fpc?EVENT=INS_POLIS_JIWA&policyid='+f.policyID.value;
            return;
        }

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