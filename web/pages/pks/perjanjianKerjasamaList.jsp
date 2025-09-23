<%@ page import="com.webfin.pks.model.PerjanjianKerjasamaView,
         com.crux.lang.LanguageManager,
         com.webfin.pks.form.PerjanjianKerjasamaListForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="Perjanjian Kerjasama" >
    <%
                final PerjanjianKerjasamaListForm form = (PerjanjianKerjasamaListForm) frame.getForm();

                final boolean canEdit = form.isCanEdit();

                final boolean canNavigateBranch = form.isCanNavigateBranch();


    %>
    <table cellpadding=2 cellspacing=1>


        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field width="200" caption="{L-ENGBranch-L}{L-INACabang-L}" lov="LOV_Branch" name="stBranch" type="string" readonly="<%=!canNavigateBranch%>" presentation="standard" changeaction="refresh" />
                                <tr>
                                    <td>{L-ENGRemark Date-L}{L-INATanggal Penandatanganan-L}</td>
                                    <td>:</td>
                                    <td>
                                        {L-ENGFrom-L}{L-INADari-L} <c:field name="dtFilterPolicyDateFrom" caption="Policy Date From" type="date" />
                                        {L-ENGTo-L}{L-INAS/D-L} <c:field name="dtFilterPolicyDateTo" caption="Policy Date To" type="date" />
                                    </td>
                                </tr>
                                <tr>
                                    <td>{L-ENGExpire Date-L}{L-INATanggal Berakhir-L}</td>
                                    <td>:</td>
                                    <td>
                                        {L-ENGFrom-L}{L-INADari-L} <c:field name="dtFilterPolicyExpireFrom" caption="Expire Date From" type="date" />
                                        {L-ENGTo-L}{L-INAS/D-L} <c:field name="dtFilterPolicyExpireTo" caption="Expire Date To" type="date" />
                                    </td>
                                </tr>
                                <c:field width="200" changeaction="refresh" lov="LOV_PolicyTypeGroup" caption="{L-ENGPolicy Class-L}{L-INAKelas Polis-L}" name="stPolicyGroup" descfield="stPolicyTypeGroupDesc" type="string" presentation="standard" />
                                <c:field width="200" changeaction="refresh" show="<%=form.getStPolicyGroup() != null%>" lov="LOV_PolicyType" caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" name="stPolicyTypeID" descfield="stPolicyTypeDesc" type="string" presentation="standard">
                                    <c:lovLink name="polgroup" link="stPolicyGroup" clientLink="false" />
                                </c:field>
                                <c:field caption="PKS Awal" width="200" name="stPksAwal" type="string" presentation="standard" />
                                <c:field caption="{L-ENGShow All-L}{L-INATampilkan Semua-L} " name="stShowAll" type="check" presentation="standard" changeaction="refreshAll" />
                                <%--  <c:field caption="{L-ENGShow active &amp; Pending only-L}{L-INATampilkan polis aktif &amp; Pending-L} " name="stOutstandingFlag" type="check" presentation="standard" changeaction="refresh" />
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
                <c:listbox name="list" autofilter="true" selectable="true" paging="true" view="com.webfin.pks.model.PerjanjianKerjasamaView" >
                    <%
                                final PerjanjianKerjasamaView pol = (PerjanjianKerjasamaView) current;
                    %>
                    <c:listcol name="stPolicyID" title="ID" selectid="policyid"/>
                    <c:listcol title="Act" name="stActiveFlag" flag="true" />
                    <c:listcol title="Eff" name="stEffectiveFlag" flag="true" />
                    <c:listcol title="Status"name="stStatus" />
                    <c:listcol title="Cabang" name="stCostCenterCode" />
                    <c:listcol filterable="true" name="stPolicyNo" title="{L-ENGAskrida No-L}{L-INANomor PKS Askrida-L}" />
                    <c:listcol filterable="true" name="stBankNo" title="{L-ENGBank No-L}{L-INANomor PKS Bank-L}" />
                    <c:listcol title="Jenis" name="stReference1"/>
                    <c:listcol name="stPolicyTypeDesc" title="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" />
                    <c:listcol title="Customer" filterable="true" name="stCustomerName"/>
                </c:listbox>
            </td>
        </tr>
        <tr>
            <td>
                <c:button text="{L-ENGCreate-L}{L-INABuat-L} " event="clickCreate" />
                <c:button text="{L-ENGAdd-L}{L-INATambah-L}" event="clickCreateAdd"/>
                <c:button text="{L-ENGEdit-L}{L-INAUbah-L}" event="clickEdit" />
                <c:button text="{L-ENGView-L}{L-INALihat-L}" event="clickView" />
                <c:button text="{L-ENGApproval-L}{L-INASetujui-L}" event="clickApproval" />
            </td>
        </tr>
        <%--<tr>
         <td>
                Print <c:field name="stPrintForm" width="170" type="string" lov="LOV_POL_PRINTING" ><c:param name="vs" value="<%=form.getPrintingLOV()%>" /></c:field> in <c:field name="stLang" type="string" value="<%=LanguageManager.getInstance().getActiveLang()%>" lov="LOV_LANG" />

         	<c:button text="Print" name="bprintx"  clientEvent="dynPrintClick();" />
         </td>
        </tr>--%>
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