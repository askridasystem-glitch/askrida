<%@ page import="com.webfin.insurance.model.InsurancePolicyView,
         com.crux.util.JSPUtil,
         com.crux.lov.LOVManager,
         com.crux.util.LOV,
         com.crux.util.DTOList,
         com.crux.web.controller.SessionManager,
         com.crux.ff.model.FlexFieldDetailView,
         com.crux.ff.model.FlexFieldHeaderView,
         com.webfin.insurance.model.InsurancePolicyObjDefaultView,
         java.util.Iterator,
         com.webfin.insurance.form.PolicySearchForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="Flexible Search">
    <%
                final PolicySearchForm form = (PolicySearchForm) frame.getForm();

                final InsurancePolicyObjDefaultView object = form.getPolicyObj();

                final boolean canEdit = form.isCanEdit();

                final boolean canNavigateBranch = SessionManager.getInstance().getSession().getStBranch() != null ? false : true;

                boolean reas = form.isReas();

                boolean claim = form.isClaim();

                final String header = form.getStPolicyHeaderID() != null ? form.getStPolicyHeaderID() : "OM_PROP";

    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <tr>
                                    <td colspan="2">
                                        <table>
                                            <c:field name="stPolicy" type="string" caption="{L-ENGPolicy No.-L}{L-INANo. Polis-L}" presentation="standard" width="200" />

                                            <c:field width="200" caption="{L-ENGBranch-L}{L-INACabang-L}" lov="LOV_Branch" name="stBranch" type="string" readonly="<%=!canNavigateBranch%>" presentation="standard" changeaction="refresh" />
                                            <c:field width="300" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroup" readonly="false" caption="{L-ENGPolicy Class-L}{L-INAKelas Polis-L}" name="stPolicyTypeGroupID" type="string" mandatory="true" presentation="standard"/>
                                            <c:field width="300" include="<%=form.getStPolicyTypeGroupID() != null%>" changeaction="onChangePolicyType" lov="LOV_PolicyType2" readonly="false" caption="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" name="stPolicyTypeID" type="string" mandatory="true" presentation="standard">
                                                <c:lovLink name="polgroup" link="stPolicyTypeGroupID" clientLink="false" />
                                            </c:field>
                                            <c:field width="100" show="false" caption="Header" name="stPolicyHeaderID" type="string|32" mandatory="false" readonly="false" presentation="standard"/>
                                            <c:field name="stEntityID" type="string" width="300" popuplov="true"  lov="LOV_Entity" mandatory="false" caption="Sumber Bisnis" presentation="standard" />
                                        </table>
                                    </td>
                                    <td>
                                        <table>
                                            <tr>
                                                <td>
                                                    Filter 1(Char) : <c:field width="100" caption="Filter(1)" name="stFilter1" type="string|32" mandatory="false" readonly="false" />
                                                </td>
                                                <td>
                                                    Keyword 1(Char) : <c:field width="300" caption="Keyword(1)" name="stKeyword1" type="string|32" mandatory="false" readonly="false" />
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    Filter 2(Char) : <c:field width="100" caption="Filter(2)" name="stFilter2" type="string|32" mandatory="false" readonly="false" />
                                                </td>
                                                <td>
                                                    Keyword 2(Char) : <c:field width="300" caption="Keyword(2)" name="stKeyword2" type="string|32" mandatory="false" readonly="false" />
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    Filter 3(Date) : <c:field width="100" caption="Filter(1)" name="stFilter3" type="string|32" mandatory="false" readonly="false" />
                                                </td>
                                                <td>
                                                    Keyword 3(Date) : <c:field caption="Keyword(1)" name="dtKeyword3" type="date" mandatory="false" readonly="false" />
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    Filter 4(Numeric) : <c:field width="100" caption="Filter(1)" name="stFilter4" type="string|32" mandatory="false" readonly="false" />
                                                </td>
                                                <td>
                                                    Keyword 4(Numeric) : <c:field caption="Keyword(1)" name="dbKeyword4" type="string" mandatory="false" readonly="false" />
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>


                                <tr>
                                    <td colspan="2">
                                        <c:button text="Search" event="refresh" />

                                        <c:button text="Print Excel" event="EXCEL_SEARCHING" />
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <table>
                    <tr class=header>
                        <% final FlexFieldHeaderView om = form.getFF(header);

                                    if (om != null) {
                                        final DTOList details = om.getDetails();
                        %>
                        <td>Nomor Polis</td>
                        <%
                                        for (int i = 0; i < details.size(); i++) {
                                            FlexFieldDetailView mapd = (FlexFieldDetailView) details.get(i);
                        %>
                        <td>
                            <%=mapd.getStFieldDesc()%> (<%=mapd.getStFieldRef()%>)
                        </td>

                        <%
                                        }
                                    }

                        %>
                    </tr>

        </tr>

        <% final DTOList object2 = form.getObject();

                    for (int p = 0; p < object2.size(); p++) {
                        InsurancePolicyObjDefaultView obj = (InsurancePolicyObjDefaultView) object2.get(p);
        %><tr class=row1>
            <td>
                <%=obj.getFieldValueByFieldName("pol_no")%>
            </td><%
                    final FlexFieldHeaderView om3 = form.getFF(header);

                    if (om3 != null) {
                        final DTOList details3 = om3.getDetails();

                        for (int i = 0; i < details3.size(); i++) {
                            FlexFieldDetailView mapd3 = (FlexFieldDetailView) details3.get(i);

                            String cari = mapd3.getStReference2();
            %>

            <td>
                <%if (obj.getProperty(mapd3.getStFieldRef()) != null && mapd3.getStHidden() == null) {%><%=obj.getProperty(mapd3.getStFieldRef())%><%}%>
            </td>
            <%
                        }
                    }
            %> </tr><%
                    }
            %>


    </table>
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
            frmx.src='x.fpc?EVENT=INS_POL_PRT&policyid='+f.policyID.value+'&alter='+f.stPrintForm.value+'&xlang='+f.stLang.value+'&fontsize='+f.stFontSize.value+'&attached='+f.stAttached.value+'&antic='+(new Date().getTime());
            return;
        }

        openDialog('w.ctl?EVENT=INS_PRT_NOM',400,50,
        function (o) {
            if (o!=null) {
                //alert(o);
                frmx.src='x.fpc?EVENT=INS_POL_PRT&nom='+o+'&policyid='+f.policyID.value+'&alter='+f.stPrintForm.value+'&xlang='+f.stLang.value+'&fontsize='+f.stFontSize.value+'&attached='+f.stAttached.value+'&antic='+(new Date().getTime());
            }
        }
    );
    }

</script>