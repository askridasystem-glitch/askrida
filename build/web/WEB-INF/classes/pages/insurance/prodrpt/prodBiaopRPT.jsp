<%@ page import="com.webfin.insurance.form.ProductionFinanceReportForm,
         com.crux.lov.LOVManager,
         com.crux.util.Tools,
         java.util.HashMap,
         com.crux.lang.LanguageManager,
         com.crux.util.StringTools"%>
<%@ taglib prefix="c" uri="crux" %><c:frame>
    <%
                final ProductionFinanceReportForm f = (ProductionFinanceReportForm) frame.getForm();

                final String reporttype = f.getStReportType();

                final boolean enableRiskFilter = f.isEnableRiskFilter();

                final boolean rptSelected = f.getStReport() != null;

                final HashMap r1 = Tools.getPropMap(LOVManager.getInstance().getRef1("PROD_PRINTING", f.getStReport()));


                final boolean PFL_MUTATION_DATE = StringTools.indexOf(((String) r1.get("MUTATION_DATE")), "O") >= 0;
                final boolean PFL_OPERATIONAL = StringTools.indexOf(((String) r1.get("OPERATIONAL")), "O") >= 0;
                final boolean PFL_BRANCH = StringTools.indexOf(((String) r1.get("BRANCH")), "O") >= 0;
                final boolean PFM_OPERATIONAL = StringTools.indexOf(((String) r1.get("OPERATIONAL")), "M") >= 0;

                final boolean PFM_EXCEL_BUTTON = StringTools.indexOf(((String) r1.get("EXCEL_BUTTON")), "O") >= 0;
                final boolean PFM_EXCEL_ONLY = StringTools.indexOf(((String) r1.get("EXCEL_ONLY")), "O") >= 0;


                //final boolean canNavigateBranch = f.isCanNavigateBranch();

                //final boolean canNavigateRegion = f.isCanNavigateRegion();


                boolean cabang = true;
                if (f.getStBranch() != null) {
                    cabang = true;
                } else {
                    cabang = false;
                }

                boolean region = true;

                if (f.getStRegion() != null) {
                    if (f.getStRegion().equalsIgnoreCase("1")) {
                        region = false;
                    }
                }

                if (f.getStRegion() == null) {
                    region = false;
                }

    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                Print Report<c:field width="150" name="stPrintForm" type="string" lov="VS_PROD_PRINTING" readonly="<%=!f.isEnableSelectForm()%>" changeaction="chgform" /> in <c:field name="stLang" type="string" value="<%=LanguageManager.getInstance().getActiveLang()%>" lov="LOV_LANG" />
                Font <c:field width="60" name="stFontSize" type="string" lov="LOV_FONTSIZE" />
                <br>
                <br>
            </td>
        </tr>

        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:field name="stReportType" show="false" type="string" width="200" mandatory="false" caption="Report Type" presentation="standard" />
                    <c:field width="450" changeaction="onChangeReport" caption="Report" lov="LOV_MARKETINGREPORT" name="stReport" descfield="stReportDesc" type="string" presentation="standard" >
                        <c:lovLink name="reporttype" link="stReportType" clientLink="false" />
                    </c:field>
                    <c:evaluate when="<%=rptSelected%>" >
                        <c:evaluate when="<%=PFL_MUTATION_DATE%>" >
                            <tr>
                                <td>{L-ENGMutation Date-L}{L-INATanggal Mutasi-L}</td>
                                <td>:</td>
                                <td>
                                    <c:field name="policyDateFrom" type="date" caption="Period From" />
                                    To <c:field name="policyDateTo" type="date" caption="Period To" />
                                </td>
                            </tr>
                        </c:evaluate>
                        <c:evaluate when="<%=PFL_BRANCH%>" >
                            <c:field width="200" caption="Branch" lov="LOV_Branch" name="stBranch" descfield="stBranchDesc" type="string" readonly="<%=cabang%>" presentation="standard" changeaction="onChangeBranchGroup" />
                            <c:field show="<%=f.getStBranch() != null%>" width="200" caption="{L-ENGRegion-L}{L-INARegion-L}" lov="LOV_Region" name="stRegion" descfield="stRegionDesc" type="string" readonly="<%=region%>" presentation="standard" >
                                <c:lovLink name="cc_code" link="stBranch" clientLink="false"/>
                            </c:field>
                        </c:evaluate>
                        <c:evaluate when="<%=PFL_OPERATIONAL%>" >
                            <c:field width="300" show="<%=f.getStReport() != null%>" changeaction="onChangePolicyTypeGroup" lov="LOV_BiaopGroup" caption="{L-ENGOperational Group-L}{L-INAGrup Operasional-L}" name="stPolicyTypeGroupID" descfield="stPolicyTypeGroupDesc" type="string" mandatory="<%=PFM_OPERATIONAL%>" presentation="standard">
                                <c:lovLink name="groupbiaop" link="stReport" clientLink="false" />
                            </c:field>
                            <c:field width="300" show="<%=f.getStPolicyTypeGroupID() != null%>" lov="LOV_BiaopType" caption="{L-ENGOperational Detail-L}{L-INADetil Operasional-L}" name="stPolicyTypeID" descfield="stPolicyTypeDesc" type="string" mandatory="<%=PFM_OPERATIONAL%>" presentation="standard">
                                <c:lovLink name="polgroup" link="stPolicyTypeGroupID" clientLink="false" />
                            </c:field>
                        </c:evaluate>
                    </c:evaluate>
                </table>
            </td>
        </tr>

        <tr>
            <td align="center" >
                <br>
                <br>
                <c:evaluate when="<%=!PFM_EXCEL_ONLY%>" >
                    <c:button text="Print" name="bprintx" event="clickPrint" validate="true" />
                </c:evaluate>
                <c:evaluate when="<%=PFM_EXCEL_BUTTON%>" >
                    <c:button text="EXPORT TO Excel" name="excel" event="clickPrintExcel" />
                </c:evaluate>
            </td>
        </tr>
    </table>
    <iframe src="" id=frmx width=1 height=1></iframe>
</c:frame>

<script>
    function selectCoinsurer() {
        var o = window.lovPopResult;
        document.getElementById('stCoinsurerName').value = o.attr_pol_name;
    }
</script>