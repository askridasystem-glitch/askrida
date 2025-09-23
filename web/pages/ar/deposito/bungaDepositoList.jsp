<%@ page import="com.webfin.ar.forms.BungaDepositoList,
         com.crux.lov.LOVManager,
         com.crux.util.LOV,
         com.crux.util.Tools,
         com.crux.lang.LanguageManager,
         com.crux.web.controller.SessionManager,
         com.webfin.gl.model.BungaDepositoView"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="SETTLEMENT" >

    <%

                BungaDepositoList form = (BungaDepositoList) request.getAttribute("FORM");

                final boolean canNavigateBranch = SessionManager.getInstance().getSession().getStBranch() != null ? false : true;

    %>

    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <c:field name="stEntityID" type="string" hidden="true"/>
            </td>
        </tr>
    </table>

    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="transNumber" type="string" caption="No Konversi" presentation="standard" width="200" />
                                <c:field name="branch" type="string" caption="Cabang" lov="LOV_Branch" readonly="<%=!canNavigateBranch%>" presentation="standard" width="200" />
                                <tr>
                                    <td>Bank</td>
                                    <td>:</td>
                                    <td>
                                        <c:field name="stEntityName" type="string" caption="Bank" readonly="true" width="200"/>
                                        <c:button text="..." clientEvent="selectAccountByBranch();" validate="false" enabled="true"/>
                                        <c:field name="stDescription" type="string" caption="Nama Bank" presentation="standard" width="200"/>
                                    </td>
                                </tr>
                                <c:field name="transBudep" type="string" caption="No Bukti Bunga" presentation="standard" width="200" />
                                <c:field name="transdatefrom" type="date" caption="Tanggal Mutasi dari" presentation="standard"/>
                                <c:field name="transdateto" type="date" caption="Tanggal Mutasi s/d" presentation="standard"/>
                            </table>
                        </td>

                        <td><c:button text="Refresh" event="btnSearch" /></td>
                    </tr>
                </table>
            </td>
        </tr>

        <tr>
            <td> 
                <%
                            BungaDepositoView lastjv = new BungaDepositoView();
                %>
                <%--<c:field name="arbudepid" type="string"  hidden="true" />--%>
                <c:listbox name="list" selectable="true" paging="true" view="com.webfin.gl.model.BungaDepositoView">
                    <%
                                final BungaDepositoView jv = (BungaDepositoView) current;

                                final boolean isdetail = jv != null && lastjv != null && Tools.isEqual(jv.getStTransactionHeaderID(), lastjv.getStTransactionHeaderID());
                                //final boolean isnodefo = jv.getBunga().getStNodefo()!=null;

                                lastjv = jv;
                    %>
                    <c:evaluate when="<%=jv == null%>" >
                        <c:listcol title="" ></c:listcol>
                        <c:listcol title="Act" ></c:listcol>
                        <c:listcol title="Eff" ></c:listcol>
                        <c:listcol title="No Konversi" ></c:listcol>
                        <c:listcol title="User" ></c:listcol>
                        <c:listcol title="No Bukti" ></c:listcol>
                        <c:listcol title="No Rekening" ></c:listcol>
                        <c:listcol title="Keterangan" ></c:listcol>
                        <c:listcol title="Tgl Bunga"></c:listcol>
                        <c:listcol title="Jumlah" align="right"></c:listcol>
                        <c:listcol title="Nodefo"></c:listcol>
                        <c:listcol title="Tgl Mutasi"></c:listcol>
                    </c:evaluate>
                    <c:evaluate when="<%=jv != null%>" >
                        <%--<c:listcol title="" selectid="ARBungaID"><% if (!isdetail) {%><input type=radio name=sel onclick="f.ARBungaID.value='<%=jspUtil.print(jv.getStTransactionHeaderID())%>'"><% }%></c:listcol>
                        <c:listcol title="" selectid="ARBungaID"><%=jspUtil.print(!isdetail ? jv.getStTransactionHeaderID() : null)%></c:listcol>--%>
                        <c:listcol name="stTransactionHeaderID" title="" selectid="ARBungaID" />
                        <c:listcol name="stActiveFlag" title="Act" flag="true" />
                        <c:listcol name="stApproved" title="Eff" flag="true" />                        
                        <c:listcol title="TRANS #" ><%=jspUtil.print(!isdetail ? jv.getStTransactionHeaderNo() : null)%></c:listcol>
                        <c:listcol title="User" ><%=jspUtil.print(!isdetail ? jv.getStCreateWho() : null)%></c:listcol>
                        <c:listcol name="stTransactionNo" title="No Bukti" /> 
                        <c:listcol title="No Rekening" ><%=jspUtil.print(jv.getAccount().getStAccountNo())%></c:listcol>
                        <c:listcol name="stDescription" title="Keterangan" />
                        <c:listcol name="dtApplyDate" title="Tanggal" />
                        <c:listcol name="dbAmount" title="Jumlah" />
                        <%--<c:listcol name="stARBungaID" title="Nodefo" />--%>
                        <c:listcol title="Nodefo" ><%=jspUtil.print(lastjv != null && lastjv.getStARBungaID() != null ? lastjv.getBunga().getStNodefo() : null)%></c:listcol>
                        <c:listcol title="Tgl Mutasi" ><%=jspUtil.print(lastjv != null && lastjv.getStARBungaID() != null ? lastjv.getBunga().getDtTglAkhir() : null)%></c:listcol>
                    </c:evaluate>
                </c:listbox>
            </td>

        </tr>
        <tr>
            <td>
                <c:button text="Upload Excel" event="clickUploadExcel" />
                <c:button text="{L-ENGEdit-L}{L-INA Ubah-L}" event="clickEdit" />
                <c:button text="{L-ENGView-L}{L-INA Lihat-L}" event="clickView" />
                <c:button text="{L-ENGInput No. Bilyet-L}{L-INA Input No. Bilyet-L}" event="clickInput" />
            </td>
        </tr>
        <tr>
            <td>
                <c:button show="<%=form.isCanApprove()%>"text="{L-ENGSave-L}{L-INA Setujui-L}" event="clickApprove" />
                <c:button show="<%=form.isCanApprove()%>"text="{L-ENGReverse-L}{L-INAReverse-L}" event="clickReverse" />
                <c:button show="<%=form.isCanApprove()%>"text="{L-ENGDelete Upload-L}{L-INAHapus Upload-L}" event="clickDelete" />
            </td>
        </tr>
    </table>

    <iframe src="" id=frmx width=1 height=1></iframe>
</c:frame>

<script>
    function refreshcb(o) {
        if (o!=null) f.submit();
    }

    function selectAccountByBranch(o){

        var cabang = document.getElementById('branch').value;

        openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT3&costcenter='+cabang+'',600,400,selectAccountsCashBank);

    }

    function selectAccountsCashBank(o) {
        if (o==null) return;
        document.getElementById('stEntityID').value=o.acid;
        document.getElementById('stEntityName').value=o.acno;
        document.getElementById('stDescription').value=o.desc;
    }
        
</script>