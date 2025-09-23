<%@page import="com.crux.util.BDUtil"%>
<%@ page import="com.webfin.gl.model.TitipanPremiView,
         com.crux.util.Tools,
         com.webfin.gl.form.GLListForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="TITIPAN PREMI POLIS KHUSUS">
    <%

                GLListForm form = (GLListForm) request.getAttribute("FORM");

                boolean cabang = true;

                if (form.getBranch() != null) {
                    if (form.getBranch().equalsIgnoreCase("00")) {
                        cabang = false;
                    }
                }

                if (form.getBranch() == null) {
                    cabang = false;
                }


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
                                <c:field name="transNumber" type="string" caption="No Bukti" presentation="standard" width="200" />
                                <c:field name="accountCode" type="string" caption="No Akun" presentation="standard" width="200" />
                                <c:field name="description" type="string" caption="Deskripsi" presentation="standard" width="200" />
                                <c:field name="branch" type="string" caption="Cabang" lov="LOV_Branch" readonly="<%=cabang%>" presentation="standard" width="200" />
                                <tr>
                                    <td>Bank</td>
                                    <td>:</td>
                                    <td>
                                        <c:field name="stEntityName" type="string" caption="Bank" readonly="false" width="200"/>
                                        <c:button text="..." clientEvent="selectAccountByBranch();" validate="false" enabled="true"/>
                                    </td>
                                </tr>
                                <c:field name="stDescription" type="string" caption="Nama Bank" presentation="standard" width="200"/>
                            </table>
                        </td>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <tr>
                                        <td>Tanggal Entry </td>
                                        <td>:</td>
                                        <td>
                                            <c:field name="entrydatefrom" type="date" caption="Tanggal Entry dari" />
                                            {L-ENGTo-L}{L-INAS/D -L}<c:field name="entrydateto" type="date" caption="Tanggal Entry s/d" />
                                        </td>
                                    </tr>
                                <tr>
                                <tr>
                                        <td>Tanggal Mutasi </td>
                                        <td>:</td>
                                        <td>
                                            <c:field name="transdatefrom" type="date" caption="Tanggal Entry dari" />
                                            {L-ENGTo-L}{L-INAS/D -L}<c:field name="transdateto" type="date" caption="Tanggal Entry s/d" />
                                        </td>
                                    </tr>
                                <tr>
                                <tr>
                                        <td>Tanggal Realisasi </td>
                                        <td>:</td>
                                        <td>
                                            <c:field name="realisasifrom" type="date" caption="Tanggal Entry dari" />
                                            {L-ENGTo-L}{L-INAS/D -L}<c:field name="realisasito" type="date" caption="Tanggal Entry s/d" />
                                        </td>
                                    </tr>
                                <tr>
                                    
                                   
                                    <c:field name="perdate" type="date" caption="Per Tanggal Neraca" presentation="standard"/>
                                    <c:field width="200" name="stCreateWho" type="string" caption="User Entry" presentation="standard" lov="LOV_Profil" popuplov="true" />
                                    <c:field caption="{L-ENGShow Pending only-L}{L-INABelum Disetujui-L} " name="stNotApproved" type="check" presentation="standard" />
                                </tr>
                            </table> 
                        </td>
                        <td><c:button text="Refresh" event="btnSearchTitipanExtracomptable" /></td> 
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>

            </td>
        </tr>
        <tr>
            <td>
                <c:button text="History Titipan - Realisasi" event="btnClickPrintExtracomptable" /> <c:button text="History Titipan - Realisasi (Excel)" event="printRealisasiTitipanExcelPolisKhusus" />
            </td>
        </tr>
        <tr>
            <td>
                <c:button text="Rincian Titipan Per Tanggal Neraca (Excel)" event="printRincianTitipanExcelPolisKhusus" />
                
            </td>
        </tr>
        <tr>
            <td>
                <c:field name="printType" type="string" lov="LOV_TITIPAN" /> in <c:field name="printLang" lov="LOV_LANG" type="string" /><c:button text="Rincian Titipan Per Hari Ini" event="printTitipanPolisKhusus" /> <c:button text="Rincian Titipan Per Hari Ini (Excel)" event="printTitipanExcelPolisKhusus" />
            
</td>
        </tr>
        <tr>
            <td>
                <%
                            TitipanPremiView lastjv = new TitipanPremiView();
                %>
                <c:field name="trxhdrid" type="string"  hidden="true" />
                <c:listbox name="titipanPremiExtracomptableList" paging="true" view="com.webfin.gl.model.TitipanPremiView">
                    <%
                                final TitipanPremiView jv = (TitipanPremiView) current;

                                final boolean isdetail = jv != null && lastjv != null && Tools.isEqual(jv.getStTransactionHeaderID(), lastjv.getStTransactionHeaderID());

                                lastjv = jv;
                    %>
                    <c:evaluate when="<%=jv == null%>" >
                        <c:listcol title="" ></c:listcol>
                        <c:listcol title="Act" ></c:listcol>
                        <c:listcol title="Eff" ></c:listcol>
                        <c:listcol title="No Bukti" ></c:listcol>
                        <c:listcol title="Counter" ></c:listcol>
                        <c:listcol title="Tanggal" ></c:listcol>
                        <c:listcol title="No Akun" ></c:listcol>
                        <c:listcol title="Deskripsi" ></c:listcol>
                        <c:listcol title="Nilai" align="right"></c:listcol>
                        <c:listcol title="Sisa" align="right"></c:listcol>
                        <c:listcol title="User" ></c:listcol>
                        <c:listcol title="User" ></c:listcol>
                    </c:evaluate>
                    <c:evaluate when="<%=jv != null%>" >
                        <c:listcol title="" ><% if (!isdetail) {%><input type=radio name=sel onclick="f.trxhdrid.value='<%=jspUtil.print(jv.getStTransactionHeaderID())%>'"><% }%></c:listcol>
                        <c:listcol name="stActiveFlag" title="Act" flag="true" />
                        <c:listcol name="stApproved" title="Eff" flag="true" />
                        <c:listcol title="TRANS #" ><%=jspUtil.print(!isdetail ? jv.getStTransactionNo() : null)%></c:listcol>
                        <c:listcol title="Counter" ><%=jspUtil.print(jv.getStCounter())%></c:listcol>
                        <c:listcol title="DATE" name="dtApplyDate" />
                        <c:listcol title="ACCOUNT #" ><div style="width:120"><%=jspUtil.print(jv.getStAccountNo())%></div></c:listcol>
                        <c:listcol title="DESC" ><%=jspUtil.print(jv.getStDescription())%></c:listcol>
                        <c:listcol title="Nilai" align="right" ><%=jspUtil.print(jv.getDbAmount(), 2)%></c:listcol>
                        <c:listcol title="Sisa" align="right" ><%=jspUtil.print(BDUtil.sub(jv.getDbAmount(), jv.getDbRealisasiUsed()), 2)%></c:listcol>
                        <c:listcol title="User" ><%=jspUtil.print(jv.getStCreateWho())%></c:listcol>
                        <c:listcol title="User" ><%=jspUtil.print(jv.getStUserName())%></c:listcol>
                    </c:evaluate>
                </c:listbox>
            </td>
        </tr>
        <tr>
            <td>
                <%--<%=jspUtil.getButtonNormal("edt", "Aktivasi Titipan", "openDialog('so.ctl?EVENT=TP_GL_ACTIVATE_TRX&trxhdrid='+f.trxhdrid.value, 980,550,refreshcb);")%>--%>
                <%=jspUtil.getButtonNormal("v", "Lihat", "openDialog('so.ctl?EVENT=TP_GL_EXTRACOMPTABLE_VIEW&trxhdrid='+f.trxhdrid.value, 980,550,refreshcb);")%>
            </td>
        </tr>
    </table>
    <%--
    <%
    if (form.goPrint.equalsIgnoreCase("Y")) {
                        out.print("<script>");
                        out.print("window.open('pages/gl/report/rpt_realisasititipan.fop?formid="+form.getFormID()+"')");
                        out.print("</script>");
                        form.goPrint=null;
    }
    %>
    --%>
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