<%@ page import="com.webfin.gl.form.GLJournalList"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="UPLOAD LAPORAN SYARIAH" >

    <%

                GLJournalList form = (GLJournalList) request.getAttribute("FORM");

    %>

    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="transNumber" type="string" caption="No Bukti" presentation="standard" width="200" />
                                <c:field name="transDate" type="date" caption="Tanggal Mutasi" presentation="standard" width="200" />
                            </table>
                        </td>
                        <td><c:button text="Refresh" event="btnSearchSyariah" /></td>
                    </tr>
                </table>
            </td>
        </tr>

        <tr>
            <td>
                <c:field name="syariahID" hidden="true"/>
                <c:listbox name="listSyariah" selectable="true" paging="true" view="com.webfin.gl.model.JournalSyariahView" autofilter="true">
                    <c:listcol name="stTransactionID" title="ID"/>
                    <c:listcol name="stTransactionNo" title="No Bukti" filterable="true" />
                    <c:listcol name="lgPeriodNo" title="Periode" />
                    <c:listcol name="lgFiscalYear" title="Tahun" />
                    <c:listcol name="stAccountNo" title="No Rekening" />
                    <c:listcol name="stDescription" title="Keterangan" filterable="true" />
                    <c:listcol name="stCostCenter" title="Cabang" />
                    <c:listcol name="dbDebit" title="Debit" />
                </c:listbox>
            </td>
        </tr>

        <tr>
            <td>
                <c:button  text="Upload Excel" event="clickUploadExcelSyariah" />
            </td>
        </tr>
    </table>

    <%--
       if (form.goPrint.equalsIgnoreCase("Y")) {
             out.print("<script>");
             out.print("window.open('pages/ar/report/rpt_ARAP_default.fop')");
             out.print("</script>");
             form.goPrint=null;
          }
    <iframe src="" id=frmx width=1 height=1></iframe>
    --%>
</c:frame>