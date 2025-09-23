<%@ page import="com.webfin.gl.report2.form.FinReportForm,
         java.util.HashMap,
         com.crux.util.JSPUtil,
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

                final boolean PFL_APPROVEDDATE = StringTools.indexOf(((String) rptRef.get("APPROVEDDATE")), "O") >= 0;
                final boolean PFL_ENTRYDATE = StringTools.indexOf(((String) rptRef.get("ENTRYDATE")), "O") >= 0;
                final boolean PFL_APPROVEDFROM_DATE = StringTools.indexOf(((String) rptRef.get("APPROVEDFROM_DATE")), "O") >= 0;
                final boolean PFL_APPROVEDTO_DATE = StringTools.indexOf(((String) rptRef.get("APPROVEDTO_DATE")), "O") >= 0;
                final boolean PFL_PERIOD_FROM = StringTools.indexOf(((String) rptRef.get("PERIOD_FROM")), "O") >= 0;
                final boolean PFL_PERIOD_TO = StringTools.indexOf(((String) rptRef.get("PERIOD_TO")), "O") >= 0;
                final boolean PFL_PERIOD = StringTools.indexOf(((String) rptRef.get("PERIOD")), "O") >= 0;
                final boolean PFL_YEAR_FROM = StringTools.indexOf(((String) rptRef.get("YEAR_FROM")), "O") >= 0;
                final boolean PFL_YEAR_TO = StringTools.indexOf(((String) rptRef.get("YEAR_TO")), "O") >= 0;
                final boolean PFL_YEAR = StringTools.indexOf(((String) rptRef.get("YEAR")), "O") >= 0;
                final boolean PFL_PRINTDATE = StringTools.indexOf(((String) rptRef.get("PRINTDATE")), "O") >= 0;
                final boolean PFL_BRANCH = StringTools.indexOf(((String) rptRef.get("BRANCH")), "O") >= 0;
                final boolean PFL_PERSON1 = StringTools.indexOf(((String) rptRef.get("PERSON1")), "O") >= 0;
                final boolean PFL_PERSON2 = StringTools.indexOf(((String) rptRef.get("PERSON2")), "O") >= 0;
                final boolean PFL_PER_DATE = StringTools.indexOf(((String) rptRef.get("PER_DATE")), "O") >= 0;
                final boolean PFL_FMT = StringTools.indexOf(((String) rptRef.get("FMT")), "O") >= 0;
                final boolean PFL_CUSTOMER = StringTools.indexOf(((String) rptRef.get("CUSTOMER")), "O") >= 0;
                final boolean PFL_CHECK = StringTools.indexOf(((String) rptRef.get("CHECK")), "O") >= 0;
                final boolean PFL_ACCOUNT = StringTools.indexOf(((String) rptRef.get("ACCOUNT")), "O") >= 0;
                final boolean PFL_SHOW = StringTools.indexOf(((String) rptRef.get("SHOW")), "O") >= 0;
                final boolean PFL_NR = StringTools.indexOf(((String) rptRef.get("NR")), "O") >= 0;
                final boolean PFL_LR = StringTools.indexOf(((String) rptRef.get("LR")), "O") >= 0;
                final boolean PFL_KAS = StringTools.indexOf(((String) rptRef.get("KAS")), "O") >= 0;
                final boolean PFL_NEW = StringTools.indexOf(((String) rptRef.get("NEW")), "O") >= 0;
                final boolean PFL_NR_BRANCH = StringTools.indexOf(((String) rptRef.get("NR_BRANCH")), "O") >= 0;
                final boolean PFL_KETERANGAN = StringTools.indexOf(((String) rptRef.get("KETERANGAN")), "O") >= 0;
                final boolean PFL_NR_BOD = StringTools.indexOf(((String) rptRef.get("NR_BOD")), "O") >= 0;

                final boolean PFM_APPROVEDDATE = StringTools.indexOf(((String) rptRef.get("APPROVEDDATE")), "M") >= 0;
                final boolean PFM_ENTRYDATE = StringTools.indexOf(((String) rptRef.get("ENTRYDATE")), "M") >= 0;
                final boolean PFM_APPROVEDFROM_DATE = StringTools.indexOf(((String) rptRef.get("APPROVEDFROM_DATE")), "M") >= 0;
                final boolean PFM_APPROVEDTO_DATE = StringTools.indexOf(((String) rptRef.get("APPROVEDTO_DATE")), "M") >= 0;
                final boolean PFM_PERIOD_FROM = StringTools.indexOf(((String) rptRef.get("PERIOD_FROM")), "M") >= 0;
                final boolean PFM_PERIOD_TO = StringTools.indexOf(((String) rptRef.get("PERIOD_TO")), "M") >= 0;
                final boolean PFM_PERIOD = StringTools.indexOf(((String) rptRef.get("PERIOD")), "M") >= 0;
                final boolean PFM_YEAR_FROM = StringTools.indexOf(((String) rptRef.get("YEAR_FROM")), "M") >= 0;
                final boolean PFM_YEAR_TO = StringTools.indexOf(((String) rptRef.get("YEAR_TO")), "M") >= 0;
                final boolean PFM_YEAR = StringTools.indexOf(((String) rptRef.get("YEAR")), "M") >= 0;
                final boolean PFM_PRINTDATE = StringTools.indexOf(((String) rptRef.get("PRINTDATE")), "M") >= 0;
                final boolean PFM_BRANCH = StringTools.indexOf(((String) rptRef.get("BRANCH")), "M") >= 0;
                final boolean PFM_PERSON1 = StringTools.indexOf(((String) rptRef.get("PERSON1")), "M") >= 0;
                final boolean PFM_PERSON2 = StringTools.indexOf(((String) rptRef.get("PERSON2")), "M") >= 0;
                final boolean PFM_FMT = StringTools.indexOf(((String) rptRef.get("FMT")), "M") >= 0;
                final boolean PFM_CUSTOMER = StringTools.indexOf(((String) rptRef.get("CUSTOMER")), "M") >= 0;
                final boolean PFM_CHECK = StringTools.indexOf(((String) rptRef.get("CHECK")), "M") >= 0;
                final boolean PFM_ACCOUNT = StringTools.indexOf(((String) rptRef.get("ACCOUNT")), "M") >= 0;
                final boolean PFM_SHOW = StringTools.indexOf(((String) rptRef.get("SHOW")), "M") >= 0;
                final boolean PFM_NR = StringTools.indexOf(((String) rptRef.get("NR")), "M") >= 0;
                final boolean PFM_LR = StringTools.indexOf(((String) rptRef.get("LR")), "M") >= 0;
                final boolean PFM_KAS = StringTools.indexOf(((String) rptRef.get("KAS")), "M") >= 0;
                final boolean PFM_NEW = StringTools.indexOf(((String) rptRef.get("NEW")), "M") >= 0;
                final boolean PFM_NR_BRANCH = StringTools.indexOf(((String) rptRef.get("NR_BRANCH")), "M") >= 0;
                final boolean PFM_NR_BOD = StringTools.indexOf(((String) rptRef.get("NR_BOD")), "M") >= 0;
                final boolean PFM_KETERANGAN = StringTools.indexOf(((String) rptRef.get("KETERANGAN")), "M") >= 0;
                final boolean PFM_PER_DATE = StringTools.indexOf(((String) rptRef.get("PER_DATE")), "M") >= 0;
                final boolean PFM_EXCEL_BUTTON = StringTools.indexOf(((String) rptRef.get("EXCEL_BUTTON")), "O") >= 0;
                final boolean PFM_EXCEL_ONLY = StringTools.indexOf(((String) rptRef.get("EXCEL_ONLY")), "O") >= 0;
    %>

    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <c:field name="stAccountID" type="string" hidden="true"/>
            </td>
        </tr>

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
                        <tr>
                            <td>
                                <table cellpadding=2 cellspacing=1>
                                    <c:evaluate when="<%=PFL_NR%>">
                                        <tr>
                                            <td>
                                                Report
                                            </td>
                                            <td>
                                                <c:field name="stNeraca" type="string" width="400" caption="Level" lov="LOV_NR" mandatory="true" />
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_KAS%>">
                                        <tr>
                                            <td>
                                                Report
                                            </td>
                                            <td>
                                                <c:field name="stKas" type="string" width="200" caption="Level" lov="LOV_KASBANK" mandatory="true" />
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_NEW%>">
                                        <tr>
                                            <td>
                                                Report
                                            </td>
                                            <td>
                                                <c:field name="stNeracaHarian" type="string" width="250" caption="Level" lov="LOV_FIN_NEW" mandatory="true" />
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_NR_BRANCH%>">
                                        <tr>
                                            <td>
                                                Report
                                            </td>
                                            <td>
                                                <c:field name="stNeraca" type="string" width="400" caption="Level" lov="LOV_NR_BRANCH" mandatory="true" />
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_NR_BOD%>">
                                        <tr>
                                            <td>
                                                Report
                                            </td>
                                            <td>
                                                <c:field name="stNeraca" type="string" width="450" caption="Level" lov="LOV_NR_BOD" mandatory="true" />
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_BRANCH%>">
                                        <tr>
                                            <td>
                                                Cabang
                                            </td>
                                            <td>
                                                <c:field name="branch" descfield="branchDesc" type="string" caption="Branch" lov="LOV_Branch" mandatory="false"/>
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_ACCOUNT%>">
                                        <tr>
                                            <td>
                                                Account
                                            </td>
                                            <td>
                                                <c:field name="stAccountNo" type="string" width="200" readonly="false" mandatory="true"/><c:button text="..." clientEvent="selectAccountByBranch();" validate="false" enabled="true"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                Bank
                                            </td>
                                            <td>
                                                <c:field name="stDescription" type="string" width="200" readonly="false" mandatory="true"/>
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_APPROVEDDATE%>">
                                        <tr>
                                            <td>Apply Date</td>
                                            <td>
                                                <c:field name="dtApplyDateFrom" type="date" mandatory="false" caption="Apply From"/>
                                                To <c:field name="dtApplyDateTo" type="date" mandatory="false" caption="Apply To" />
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_ENTRYDATE%>">
                                        <tr>
                                            <td>Entry Date</td>
                                            <td>
                                                <c:field name="dtEntryDateFrom" type="date" mandatory="false" caption="Entry From"/>
                                                To <c:field name="dtEntryDateTo" type="date" mandatory="false" caption="Entry To" />
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_APPROVEDFROM_DATE%>">
                                        <tr>
                                            <td>
                                                Start date
                                            </td>
                                            <td>
                                                <c:field name="appDateFrom" type="date" mandatory="false" caption="Entry date"/>
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_APPROVEDTO_DATE%>">
                                        <tr>
                                            <td>
                                                Entry date
                                            </td>
                                            <td>
                                                <c:field name="appDateTo" type="date" mandatory="false" caption="Entry date"/>
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_PERIOD_FROM%>">
                                        <tr>
                                            <td>
                                                Period From
                                            </td>
                                            <td>
                                                <c:field name="periodFrom" type="string" caption="Period From" lov="LOV_MONTH_Period" mandatory="true"/>
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_PERIOD_TO%>">
                                        <tr>
                                            <td>
                                                Period To
                                            </td>
                                            <td>
                                                <c:field name="periodTo" type="string" caption="Period To" lov="LOV_MONTH_Period" mandatory="true" changeaction="onChangeKeterangan"/>
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_YEAR_FROM%>">
                                        <tr>
                                            <td>
                                                Year From
                                            </td>
                                            <td>
                                                <c:field name="yearFrom" type="string" caption="Year From" lov="LOV_GL_Years2" mandatory="true"/>
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_YEAR_TO%>">
                                        <tr>
                                            <td>
                                                Year To
                                            </td>
                                            <td>
                                                <c:field name="yearTo" type="string" caption="Year To" lov="LOV_GL_Years2" />
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_PERIOD%>">
                                        <tr>
                                            <td>
                                                Period
                                            </td>
                                            <td>
                                                <c:field name="period" type="string" caption="Period" lov="LOV_GL_Period" />
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_PER_DATE%>" >
                                        <tr>
                                            <td>Per Tanggal</td>
                                            <td>
                                                <c:field name="perDateFrom" type="date" caption="Per From"  />
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_YEAR%>">
                                        <tr>
                                            <td>
                                                Year
                                            </td>
                                            <td>
                                                <c:field name="year" type="string" caption="Year" lov="LOV_GL_Years3" mandatory="true" changeaction="onChangeKeterangan"/>
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_PERSON1%>">
                                        <tr>
                                            <td>
                                                Person 1 Name
                                            </td>
                                            <td>
                                                <c:field name="person1Name" type="string" width="250" caption="Person 1 Name" readonly="false" mandatory="false" />
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_PERSON1%>">
                                        <tr>
                                            <td>
                                                Person 1 Title
                                            </td>
                                            <td>
                                                <c:field name="person1Title" type="string" width="250" caption="Person 1 Title" readonly="false" mandatory="false" />
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_PERSON2%>">
                                        <tr>
                                            <td>
                                                Person 2 Name
                                            </td>
                                            <td>
                                                <c:field name="person2Name" type="string" width="250" caption="Person 2 Name" readonly="false" mandatory="false" />
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_PERSON2%>">
                                        <tr>
                                            <td>
                                                Person 2 Title
                                            </td>
                                            <td>
                                                <c:field name="person2Title" type="string" width="250" caption="Person 2 Title" readonly="false" mandatory="false" />
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=form.getPeriodTo() != null%>">
                                        <c:evaluate when="<%=form.getPeriodTo().equalsIgnoreCase("12")%>">
                                            <c:evaluate when="<%=PFL_KETERANGAN%>">
                                                <tr>
                                                    <td>
                                                        Pilihan
                                                    </td>
                                                    <td>
                                                        <c:field name="stKeterangan" descfield="stKeteranganDesc" type="string" width="100" readonly="false" mandatory="false" lov="LOV_AkunDesc" />
                                                    </td>
                                                </tr>
                                            </c:evaluate>
                                        </c:evaluate>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_PRINTDATE%>">
                                        <tr>
                                            <td>
                                                Tanggal Cetak
                                            </td>
                                            <td>
                                                <c:field name="dtPrintDate" type="date" mandatory="true" caption="Print Date"/>
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_FMT%>">
                                        <tr>
                                            <td>
                                                Format
                                            </td>
                                            <td>
                                                <c:field name="rptfmt" type="string" width="250" caption="Format" mandatory="true" lov="VS_FINRPT_FMT" />
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_SHOW%>">
                                        <tr>
                                            <td>
                                                Account Rekap
                                            </td>
                                            <td>
                                                <c:field name="clAccDet" type="check" caption="Account Detail" />
                                                <%--<c:field name="clShowNo" type="check" presentation="standard" caption="Line Number"  />--%>
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                    <c:evaluate when="<%=PFL_CHECK%>">
                                        <tr>
                                            <td>
                                                Di urut berdasarkan
                                            </td>
                                            <td>
                                                Wajib dipilih salah satu
                                                <br>
                                                <br>
                                                <table cellpadding=2 cellspacing=1>
                                                    <c:field name="clAccNo" type="check" presentation="standard" caption="No Account"/>
                                                    <c:field name="clDate" type="check" presentation="standard" caption="Tanggal Mutasi"/>
                                                    <c:field name="clTrxNo" type="check" presentation="standard" caption="No Bukti"/>
                                                </table>
                                            </td>
                                        </tr>
                                    </c:evaluate>
                                </table>
                    </table>
                </td>
            </tr>
 <tr>
                <td align="center" >
                    <br>
                    <br>
                    <c:evaluate when="<%=PFL_NR%>">
                        <c:evaluate when="<%=form.getStNeraca() != null%>">
                            <c:evaluate when="<%=form.getStPrintForm().equalsIgnoreCase("account")%>">
                                <c:evaluate when="<%=form.getStNeraca().equalsIgnoreCase("1") || form.getStNeraca().equalsIgnoreCase("3")%>">
                                    <c:evaluate when="<%=form.getBranch() == null%>">
                                        <c:evaluate when="<%=form.isJournalSyariah()%>">
                                            Laporan Neraca dan RugiLaba Konsolidasi bulan <%=JSPUtil.printX(form.getPeriodTo())%> tahun <%=JSPUtil.printX(form.getYearFrom())%> bisa dicetak</fo:block>
                                        </c:evaluate>
                                    </c:evaluate>
                                </c:evaluate>
                            </c:evaluate>
                        </c:evaluate>
                    </c:evaluate>
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
    </c:frame>

    <script>
   
        function selectAccountByBranch(o){
   		
            var cabang = document.getElementById('branch').value;
        
            openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT3&costcenter='+cabang+'',600,400,selectAccountsCashBank);
   		
        }
   
        function selectAccountsCashBank(o) {
            if (o==null) return;
            document.getElementById('stAccountID').value=o.acid;
            document.getElementById('stAccountNo').value=o.acno;
            document.getElementById('stDescription').value=o.desc;
        }
    </script>