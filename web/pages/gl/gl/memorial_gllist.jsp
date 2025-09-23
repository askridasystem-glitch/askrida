<%@ page import="com.webfin.gl.model.JournalView,
com.crux.util.Tools,
com.webfin.gl.form.GLListForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="JURNAL MEMORIAL">
    <%
    
    GLListForm form  =(GLListForm) request.getAttribute("FORM");
    
    boolean cabang = false;
    
    if(form.getBranch()!=null){
        if(!form.getBranch().equalsIgnoreCase("00")){
            cabang = true;
        }
    }
    
    if(form.getBranch()==null){
        cabang = false;
    }
    
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
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="policyNo" type="string" caption="Nomor Polis" presentation="standard" width="200" />
                                <%--
                                <c:field name="accountCode" type="string" caption="Kode Akun" presentation="standard" width="200" lov="LOV_AccountInvestment" popuplov="true"/>
                                --%>
                                <tr>
                                    <td>
                                        Kode Akun
                                    </td>
                                    <td>
                                    </td>
                                    <td>
                                        <c:field name="accountCode" type="string" readonly="false" mandatory="false" width="200"/><c:button text="..." clientEvent="selectAccountByBranch();" validate="false" enabled="true"/>
                                    </td>
                                </tr>

                                <c:field name="description" type="string" caption="Keterangan" presentation="standard" width="200" />
                                <c:field name="branch" type="string" caption="Cabang" lov="LOV_Branch" readonly="<%=cabang%>" presentation="standard" width="200" />
                                <c:field name="method" type="string" caption="Metode" lov="LOV_JOURNAL_DESC" presentation="standard" width="200" />
                                <c:field name="stPayment" type="check" caption="Show Pembayaran" presentation="standard" width="200" />
                            </table>
                        </td>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <tr>
                                    <c:field name="transNumber" type="string" caption="Nomor Bukti" presentation="standard" width="200" />
                                    <%--<c:field name="transdate" type="date" caption="Date" presentation="standard" width="200" />--%>
                                    <c:field name="transdatefrom" type="date" caption="Tanggal Mulai" presentation="standard" width="200" />
                                    <c:field name="transdateto" type="date" caption="Tanggal Akhir" presentation="standard" width="200" />
                                    <c:field name="showReverse" type="check" caption="Show Reverse" presentation="standard" width="200" />
                                    <c:field name="stSort" type="string" caption="Urutkan" lov="LOV_Sort" presentation="standard" width="200" />
                                    
                                </tr>
                            </table>
                        </td>
                        <td><c:button text="Refresh" event="btnSearch" /></td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <c:button text="Print" event="btnPrint" /><c:button text="Print Excel" event="btnPrintExcel" /> using template <c:field name="printForm" type="string" lov="LOV_PRT_GLJE" />: in <c:field name="printLang" lov="LOV_LANG" type="string" />
            </td>
        </tr>
     
        <tr>
            <td>
                <%
                JournalView lastjv = new JournalView();
                %>
                <c:field name="trxhdrid" type="string"  hidden="true" />
                <c:listbox name="memorialList" paging="true" >
                    <%
                    final JournalView jv = (JournalView) current;
                    
                    final boolean isdetail = jv!=null && lastjv!=null && Tools.isEqual(jv.getStTransactionHeaderID(), lastjv.getStTransactionHeaderID());
                    
                    lastjv=jv;
                    %>
                    <c:evaluate when="<%=jv==null%>" >
                        <c:listcol title="" ></c:listcol>
                        <c:listcol title="Eff" ></c:listcol>
                        <c:listcol title="NO BUKTI" ></c:listcol>
                        <c:listcol title="TANGGAL" ></c:listcol>
                        <c:listcol title="NO POLIS" ></c:listcol>
                        <c:listcol title="KODE AKUN" ></c:listcol>
                        <c:listcol title="KETERANGAN" ></c:listcol>
                        <c:listcol title="JT" ></c:listcol>
                        <c:listcol title="DEBIT" align="right"></c:listcol>
                        <c:listcol title="KREDIT" align="right"></c:listcol>
                    </c:evaluate>
                    <c:evaluate when="<%=jv!=null%>" >
                        <c:listcol title="" ><% if (!isdetail) {%><input type=radio name=sel onclick="f.trxhdrid.value='<%=jspUtil.print(jv.getStTransactionHeaderID())%>'"><% } %></c:listcol>
                        <c:listcol name="stApproved" title="Eff" flag="true" />
                        <c:listcol title="TRANS #" ><%=jspUtil.print(!isdetail?jv.getStTransactionNo():null)%></c:listcol>
                        <c:listcol title="DATE" ><%=jspUtil.print(jv.getDtApplyDate())%></c:listcol>
                        <c:listcol title="POL #" ><%=jspUtil.print(jv.getStPolicyNo())%></c:listcol>
                        <c:listcol title="ACCOUNT #" ><div style="width:120"><%=jspUtil.print(jv.getStAccountNo())%></div></c:listcol>
                        <c:listcol title="KETERANGAN" ><%=jspUtil.print(jv.getStDescription())%></c:listcol>
                        <c:listcol title="JT" ><%=jspUtil.print(jv.getStJournalCode())%></c:listcol>
                        <c:listcol title="DEBIT" align="right" ><%=jspUtil.print(jv.getDbDebit(),2)%></c:listcol>
                        <c:listcol title="CREDIT" align="right" ><%=jspUtil.print(jv.getDbCredit(),2)%></c:listcol>
                    </c:evaluate>
                </c:listbox>
            </td>
        </tr>
        <tr>
            <td>
                 <%=jspUtil.getButtonNormal("crt","Buat","openDialog('so.ctl?EVENT=MEM_GL_ENTRY_ADD', 1000,500,refreshcb);")%>
            <%=jspUtil.getButtonNormal("edt","Ubah","openDialog('so.ctl?EVENT=MEM_GL_ENTRY_EDIT&trxhdrid='+f.trxhdrid.value, 1000,500,refreshcb);")%>
            <%=jspUtil.getButtonNormal("v","Lihat","openDialog('so.ctl?EVENT=MEM_GL_ENTRY_VIEW&trxhdrid='+f.trxhdrid.value, 1000,500,refreshcb);")%>
             <%=jspUtil.getButtonNormal("edt","Hapus","openDialog('so.ctl?EVENT=MEM_GL_ENTRY_DELETE&trxhdrid='+f.trxhdrid.value, 1000,550,refreshcb);")%>
             <%--
                <%=jspUtil.getButtonNormal("edt","Setujui","openDialog('so.ctl?EVENT=MEM_GL_ENTRY_VIEW&trxhdrid='+f.trxhdrid.value, 900,500,refreshcb);")%>
            <%=jspUtil.getButtonNormal("rev","Reverse","openDialog('so.ctl?EVENT=MEM_GL_ENTRY_REVERSE&trxhdrid='+f.trxhdrid.value, 900,500,refreshcb);")%>
            --%>
            </td>
        </tr>
    </table>
    <script>
   function refreshcb(o) {
    if (o!=null) f.submit();
   }
   
    function selectAccountByBranch(o){
   		
   	var cabang = document.getElementById('branch').value;
        var acccode = 'undefined';
        
   	openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'',600,400,selectAccount);
   		
   }
   
   function selectAccount(o) {
      if (o==null) return;
      document.getElementById('stAccountID').value=o.acid;
      document.getElementById('accountCode').value=o.acno;
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