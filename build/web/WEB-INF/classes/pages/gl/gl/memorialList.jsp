<%@ page import="com.webfin.gl.form.GLJournalList"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="UPLOAD JOURNAL MEMORIAL" >

    <%

   GLJournalList form  =(GLJournalList) request.getAttribute("FORM");

    %>

    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="description" type="string" caption="Description" presentation="standard" width="200" />
                                <c:field name="branch" type="string" caption="Branch" readonly="<%=form.getBranch() != null%>" lov="LOV_Branch" presentation="standard" width="200" />
                            </table>
                        </td>
                        <td><c:button text="Refresh" event="btnSearch" /></td>
                    </tr>
                </table>
            </td>
        </tr>

        <tr>
            <td>
                <c:field name="memorialID" hidden="true"/>
                <c:listbox name="list" selectable="true" paging="true" view="com.webfin.gl.model.JournalView" autofilter="true">
                    <c:listcol name="stTransactionID" title="ID"/>
                    <c:listcol name="stTransactionNo" title="No Bukti" filterable="true" />
                    <c:listcol name="stAccountNo" title="No Rekening" />
                    <c:listcol name="stDescription" title="Keterangan" filterable="true" />
                    <c:listcol name="stCostCenter" title="Cabang" />
                    <c:listcol name="dtApplyDate" title="Tanggal" />
                    <c:listcol name="dbDebit" title="Debit" />
                    <c:listcol name="dbCredit" title="Credit" />
                </c:listbox>
            </td>
        </tr>

        <tr>
            <td>
                <c:button  text="Upload Excel" event="clickUploadExcel" />
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