<%@ page import="com.webfin.insurance.form.ProductionRecapReportForm,
         com.crux.lov.LOVManager,
         com.crux.util.Tools,
         java.util.HashMap,
         com.crux.lang.LanguageManager,
         com.crux.util.StringTools"%>
<%@ taglib prefix="c" uri="crux" %><c:frame>
    <%
                final ProductionRecapReportForm f = (ProductionRecapReportForm) frame.getForm();

                final String reporttype = f.getStReportType();

                final boolean enableRiskFilter = f.isEnableRiskFilter();

                final boolean rptSelected = f.getStReport() != null;

                final HashMap r1 = Tools.getPropMap(LOVManager.getInstance().getRef1("PROD_PRINTING_REKAP", f.getStReport()));

                final boolean PFL_POLICY_DATE = StringTools.indexOf(((String) r1.get("POLICY_DATE")), "O") >= 0;
                final boolean PFL_MUTATION_DATE = StringTools.indexOf(((String) r1.get("MUTATION_DATE")), "O") >= 0;
                final boolean PFL_PERIOD_FROM = StringTools.indexOf(((String) r1.get("PERIOD_FROM")), "O") >= 0;
                final boolean PFL_PERIOD_TO = StringTools.indexOf(((String) r1.get("PERIOD_TO")), "O") >= 0;
                final boolean PFL_YEAR = StringTools.indexOf(((String) r1.get("YEAR")), "O") >= 0;
                final boolean PFL_POLICY_CLASS = StringTools.indexOf(((String) r1.get("POLICY_CLASS")), "O") >= 0;
                final boolean PFL_BRANCH = StringTools.indexOf(((String) r1.get("BRANCH")), "O") >= 0;

                final boolean PFM_POLICY_DATE = StringTools.indexOf(((String) r1.get("POLICY_DATE")), "M") >= 0;
                final boolean PFM_MUTATION_DATE = StringTools.indexOf(((String) r1.get("MUTATION_DATE")), "M") >= 0;
                final boolean PFM_PERIOD_FROM = StringTools.indexOf(((String) r1.get("PERIOD_FROM")), "M") >= 0;
                final boolean PFM_PERIOD_TO = StringTools.indexOf(((String) r1.get("PERIOD_TO")), "M") >= 0;
                final boolean PFM_YEAR = StringTools.indexOf(((String) r1.get("YEAR")), "M") >= 0;
                final boolean PFM_POLICY_CLASS = StringTools.indexOf(((String) r1.get("POLICY_CLASS")), "M") >= 0;
                final boolean PFM_BRANCH = StringTools.indexOf(((String) r1.get("BRANCH")), "M") >= 0;

                final boolean PFM_EXCEL_BUTTON = StringTools.indexOf(((String) r1.get("EXCEL_BUTTON")), "O") >= 0;
                final boolean PFM_EXCEL_ONLY = StringTools.indexOf(((String) r1.get("EXCEL_ONLY")), "O") >= 0;

                final boolean canNavigateBranch = f.isCanNavigateBranch();
                final boolean canNavigateRegion = f.isCanNavigateRegion();

    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                Print Report<c:field width="150" name="stPrintForm" type="string" lov="VS_PROD_PRINTING_REKAP" readonly="<%=!f.isEnableSelectForm()%>"  changeaction="chgform" /> in <c:field name="stLang" type="string" value="<%=LanguageManager.getInstance().getActiveLang()%>" lov="LOV_LANG" />
                Font <c:field width="60" name="stFontSize" type="string" lov="LOV_FONTSIZE" />
                <br>
                <br>
            </td>
        </tr>

        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:field name="stReportType" show="false" type="string" width="200" mandatory="false" caption="Report Type" presentation="standard" />
                    <c:field width="450" changeaction="onChangeReport" caption="Report" lov="LOV_MARKETINGREPORTVLD" name="stReport"  descfield="stReportDesc" type="string" presentation="standard" >
                        <c:lovLink name="reporttype" link="stReportType" clientLink="false" />
                    </c:field>
                    <c:evaluate when="<%=rptSelected%>" >
                        <c:evaluate when="<%=PFL_POLICY_DATE%>" >
                            <tr>
                                <td>Policy Date</td>
                                <td>:</td>
                                <td>
                                    <c:field name="policyDateFrom" type="date" caption="Period From"  />
                                    To <c:field name="policyDateTo" type="date" caption="Period To" />
                                </td>
                            </tr>
                        </c:evaluate>
                        <c:evaluate when="<%=PFL_MUTATION_DATE%>" >
                            <tr>
                                <td>Mutation Date</td>
                                <td>:</td>
                                <td>
                                    <c:field name="mutationFrom" type="date" caption="Mutation From"  />
                                    To <c:field name="mutationTo" type="date" caption="Mutation To" />
                                </td>
                            </tr>
                        </c:evaluate>
                        <c:evaluate when="<%=PFM_PERIOD_FROM%>" >
                            <tr>
                                <td>Bulan</td>
                                <td>:</td>
                                <td>
                                    <c:field name="periodFrom" descfield="periodFromName" type="string" caption="Bulan dari" lov="LOV_MONTH_Period" mandatory="<%=PFM_PERIOD_FROM%>" include="<%=PFL_PERIOD_FROM%>" />
                                    sd <c:field name="periodTo" descfield="periodToName" type="string" caption="Bulan ke" lov="LOV_MONTH_Period" mandatory="<%=PFM_PERIOD_TO%>" include="<%=PFL_PERIOD_TO%>" />
                                </td>
                            </tr>
                        </c:evaluate>
                        <%--<c:field name="periodFrom" type="string" caption="Bulan dari" lov="LOV_GL_Period" mandatory="<%=PFM_PERIOD_FROM%>" include="<%=PFL_PERIOD_FROM%>" presentation="standard" />
                        <c:field name="periodTo" type="string" caption="Bulan ke" lov="LOV_GL_Period" mandatory="<%=PFM_PERIOD_TO%>" include="<%=PFL_PERIOD_TO%>" presentation="standard"/>--%>
                        <c:field name="year" type="string" caption="Tahun" lov="LOV_GL_Years3" mandatory="<%=PFM_YEAR%>" include="<%=PFL_YEAR%>" presentation="standard"/>



                        <c:evaluate when="<%=PFL_POLICY_CLASS%>" >
                            <c:field width="300" changeaction="onChangePolicyTypeGroup" lov="LOV_PolicyTypeGroup" caption="Policy Class" name="stPolicyTypeGroupID" descfield="stPolicyTypeGroupDesc" type="string" mandatory="<%=PFM_POLICY_CLASS%>" presentation="standard" loveall="true"/>
                            <c:field width="300" show="<%=f.getStPolicyTypeGroupID() != null%>" lov="LOV_PolicyType" caption="Policy Type" name="stPolicyTypeID" descfield="stPolicyTypeDesc" type="string" mandatory="<%=PFM_POLICY_CLASS%>" presentation="standard">
                                <c:lovLink name="polgroup" link="stPolicyTypeGroupID" clientLink="false" />
                            </c:field>
                        </c:evaluate>

                        <c:evaluate when="<%=PFL_BRANCH%>" >
                            <c:field width="200" caption="Branch" lov="LOV_Branch" name="stBranch" descfield="stBranchDesc" type="string" presentation="standard" changeaction="onChangeBranchGroup"/>
                            <c:field show="<%=f.getStBranch() != null%>" width="200" caption="Region" lov="LOV_Region" name="stRegion" descfield="stRegionDesc" type="string" presentation="standard" >
                                <c:lovLink name="cc_code" link="stBranch" clientLink="false"/>
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
                <%--<c:button text="Print Validasi" name="bprintvld" event="clickPrintValidate" />--%>
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