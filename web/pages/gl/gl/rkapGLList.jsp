<%@ page import="com.webfin.gl.model.RKAPGroupView,
         com.crux.util.Tools,
         com.webfin.gl.form.GLListForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="JURNAL SYARIAH">
    <%

                GLListForm form = (GLListForm) request.getAttribute("FORM");

                boolean cabang = false;

                if (form.getBranch() != null) {
                    if (!form.getBranch().equalsIgnoreCase("00")) {
                        cabang = true;
                    }
                }

                if (form.getBranch() == null) {
                    cabang = false;
                }

    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="years" type="string" caption="Tahun" lov="LOV_GL_Years2" presentation="standard" />
                            </table>
                        </td>

                        <td><c:button text="Refresh" event="btnRKAPSearch" /></td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <c:button text="Print" event="btnPrint" />
            </td>
        </tr>
        <tr>
            <td>
                <%
                            RKAPGroupView lastjv = new RKAPGroupView();
                %>
                <c:field name="trxhdrid" type="string"  hidden="true" />
                <c:listbox name="rkapList" paging="true" >
                    <%
                                final RKAPGroupView jv = (RKAPGroupView) current;

                                final boolean isdetail = jv != null && lastjv != null && Tools.isEqual(jv.getStRKAPTransactionHeaderID(), lastjv.getStRKAPTransactionHeaderID());

                                lastjv = jv;
                    %>
                    <c:evaluate when="<%=jv == null%>" >
                        <c:listcol title="" ></c:listcol>
                        <c:listcol title="TRANS #" ></c:listcol>
                        <c:listcol title="YEAR" ></c:listcol>
                        <c:listcol title="DESC" ></c:listcol>
                        <c:listcol title="SALDO" align="right"></c:listcol>
                    </c:evaluate>
                    <c:evaluate when="<%=jv != null%>" >
                        <c:listcol title="" ><% if (!isdetail) {%><input type=radio name=sel onclick="f.trxhdrid.value='<%=jspUtil.print(jv.getStRKAPTransactionHeaderID())%>'"><% }%></c:listcol>
                        <c:listcol title="TRANS #" ><%=jspUtil.print(!isdetail ? jv.getStRKAPTransactionNo() : null)%></c:listcol>
                        <c:listcol title="YEAR" ><%=jspUtil.print(!isdetail ? jv.getStYears() : null)%></c:listcol>
                        <c:listcol title="DESC" ><%=jspUtil.print(jv.getStRKAPDescription())%></c:listcol>
                        <c:listcol title="SALDO" align="right" ><%=jspUtil.print(jv.getDbKonvensional(), 2)%></c:listcol>
                    </c:evaluate>
                </c:listbox>
            </td>
        </tr>
        <tr>
            <td>
                <%=jspUtil.getButtonNormal("crt", "Create RKAP", "openDialog('so.ctl?EVENT=REPORT_GL_ENTRY_ADD_RKAP', 600,500,refreshcb);")%>
                <%=jspUtil.getButtonNormal("edt", "Edit", "openDialog('so.ctl?EVENT=REPORT_GL_ENTRY_EDIT&trxhdrid='+f.trxhdrid.value, 800,500,refreshcb);")%>
                <%=jspUtil.getButtonNormal("v", "View", "openDialog('so.ctl?EVENT=REPORT_GL_ENTRY_VIEW&trxhdrid='+f.trxhdrid.value, 800,500,refreshcb);")%>
            </td>
        </tr>
    </table>
    <script>
        function refreshcb(o) {
            if (o!=null) f.submit();
        }


    </script>
    <%
                /*
                if (form.goPrint!=null) {
                out.print("<script>");
                out.print("window.open('pages/gl/report/rpt_"+form.goPrint+".fop?formid="+form.getFormID()+"')");
                out.print("</script>");
                form.goPrint=null;
                }*/


    %>
</c:frame>