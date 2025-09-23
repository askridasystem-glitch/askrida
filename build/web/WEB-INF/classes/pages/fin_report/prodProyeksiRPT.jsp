<%@ page import="com.webfin.gl.report2.form.FinReportForm,
         java.util.HashMap,
         com.crux.lang.LanguageManager,
         com.crux.util.StringTools"%>
<%@ taglib prefix="c" uri="crux" %><c:frame>
    <%
                FinReportForm form = (FinReportForm) request.getAttribute("FORM");

                boolean cabang = true;

                if (form.getBranch() != null) {
                    if (form.getBranch().equalsIgnoreCase("00")) {
                        cabang = false;
                    }
                }

                if (form.getBranch() == null) {
                    cabang = false;
                }

                HashMap rptRef = form.getRptRef();

                final boolean PFL_YEAR = StringTools.indexOf(((String) rptRef.get("YEAR")), "O") >= 0;
                final boolean PFL_BRANCH = StringTools.indexOf(((String) rptRef.get("BRANCH")), "O") >= 0;
                final boolean PFL_REGION = StringTools.indexOf(((String) rptRef.get("REGION")), "O") >= 0;

                final boolean PFM_YEAR = StringTools.indexOf(((String) rptRef.get("YEAR")), "M") >= 0;
                final boolean PFM_BRANCH = StringTools.indexOf(((String) rptRef.get("BRANCH")), "M") >= 0;
                final boolean PFM_REGION = StringTools.indexOf(((String) rptRef.get("REGION")), "M") >= 0;
                final boolean PFM_EXCEL_BUTTON = StringTools.indexOf(((String) rptRef.get("EXCEL_BUTTON")), "O") >= 0;
                final boolean PFM_EXCEL_ONLY = StringTools.indexOf(((String) rptRef.get("EXCEL_ONLY")), "O") >= 0;
    %>

    <table cellpadding=2 cellspacing=1>

        <table cellpadding=2 cellspacing=1>
            <tr>
                <td>
                    Print <%=form.getTitle()%> in <c:field name="stLang" type="string" value="<%=LanguageManager.getInstance().getActiveLang()%>" lov="LOV_LANG" />
                    <br>
                    <br>
                </td>
            </tr>
            <tr>
                <td>
                    <table cellpadding=2 cellspacing=1>
                        <c:evaluate when="<%=PFL_BRANCH%>">
                            <tr>
                                <td>
                                    Cabang
                                </td>
                                <td><%-- readonly="<%=cabang%>" changeaction="onChangeBranchGroup"--%>
                                    <c:field name="branch" descfield="branchDesc" type="string" caption="Branch" lov="LOV_Branch" mandatory="false" changeaction="onChangeBranchGroup"/>
                                </td>
                            </tr>
                        </c:evaluate>
                        <c:evaluate when="<%=PFL_REGION%>">
                            <tr>
                                <td>
                                    Region
                                </td>
                                <td>
                                    <c:field caption="Region" lov="LOV_Region" name="region" descfield="regionDesc" type="string">
                                        <c:lovLink name="cc_code" link="branch" clientLink="false"/>
                                    </c:field>
                                </td>
                            </tr>
                        </c:evaluate>
                        <c:evaluate when="<%=PFL_YEAR%>">
                            <tr>
                                <td>
                                    Tahun
                                </td>
                                <td>
                                    <c:field name="year" type="string" caption="Year" lov="LOV_GL_Years2" mandatory="true"/>
                                </td>
                            </tr>
                        </c:evaluate>
                    </table>
                </td>
            </tr>
            <tr>
                <td align="center" >
                    <br>
                    <br>
                    <c:evaluate when="<%=!PFM_EXCEL_ONLY%>" >
                        <c:button text="Print" name="print"  event="clickPrint"  validate="true" />
                    </c:evaluate>
                    <c:evaluate when="<%=PFM_EXCEL_BUTTON%>" >
                        <c:button text="Print Excel" name="printx"  event="clickPrintExcel"  validate="true" />
                    </c:evaluate>
                </td>
            </tr>
        </table>
    </table>
</c:frame>