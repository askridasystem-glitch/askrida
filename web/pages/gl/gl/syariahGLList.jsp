<%@ page import="com.webfin.gl.model.JournalSyariahView,
                 com.crux.util.Tools,
                 com.webfin.gl.form.GLListForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="JURNAL SYARIAH">
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
            <table cellpadding=2 cellspacing=1>
               <tr>
                  <td>
                     <table cellpadding=2 cellspacing=1>
                        <c:field name="accountCode" type="string" caption="Account" presentation="standard" width="200" />
                        <c:field name="description" type="string" caption="Description" presentation="standard" width="200" />
                        <c:field name="branch" type="string" caption="Branch" lov="LOV_Branch" readonly="<%=cabang%>" presentation="standard" width="200" changeaction="btnSearch" />
                    </table>
                  </td>
                  <td>
                     <table cellpadding=2 cellspacing=1>
                        <tr>
                           <c:field name="transNumber" type="string" caption="Trans #" presentation="standard" width="200" />
                           <%--<c:field name="transdate" type="date" caption="Date" presentation="standard" width="200" />--%>
                           <c:field name="transdatefrom" type="date" caption="Date From" presentation="standard" width="200" />
                           <c:field name="transdateto" type="date" caption="Date To" presentation="standard" width="200" />
                           <c:field name="stSort" type="string" caption="Sort" lov="LOV_Sort" presentation="standard" width="200" changeaction="btnSearch" />

                        </tr>
                     </table>
                  </td>
                  <td><c:button text="Refresh" event="btnSyariahSearch" /></td>
               </tr>
            </table>
         </td>
      </tr>
      <tr>
         <td>
                <c:button text="Print" event="btnPrint" /> using template <c:field name="printForm" type="string" lov="LOV_PRT_SYGE" />: in <c:field name="printLang" lov="LOV_LANG" type="string" />
            </td>
      </tr>
      <tr>
         <td>
            <%
               JournalSyariahView lastjv = new JournalSyariahView();
            %>
            <c:field name="trxhdrid" type="string"  hidden="true" />
            <c:listbox name="syariahList" paging="true" >
               <%
                  final JournalSyariahView jv = (JournalSyariahView) current;

                  final boolean isdetail = jv!=null && lastjv!=null && Tools.isEqual(jv.getStTransactionHeaderID(), lastjv.getStTransactionHeaderID());

                  lastjv=jv;
               %>
               <c:evaluate when="<%=jv==null%>" >
                  <c:listcol title="" ></c:listcol>
                  <c:listcol title="EFF" ></c:listcol>
                  <c:listcol title="TRANS #" ></c:listcol>
                  <c:listcol title="YEAR" ></c:listcol>
                  <c:listcol title="MONTH" ></c:listcol>
                  <c:listcol title="DESC" ></c:listcol>
                  <c:listcol title="SALDO" align="right"></c:listcol>
               </c:evaluate>
               <c:evaluate when="<%=jv!=null%>" >
                  <c:listcol title="" ><% if (!isdetail) {%><input type=radio name=sel onclick="f.trxhdrid.value='<%=jspUtil.print(jv.getStTransactionHeaderID())%>'"><% } %></c:listcol>
                   <c:listcol name="stApproved" title="Eff" flag="true" />
                  <c:listcol title="TRANS #" ><%=jspUtil.print(!isdetail?jv.getStTransactionNo():null)%></c:listcol>
                  <c:listcol title="YEAR" ><%=jspUtil.print(!isdetail?String.valueOf(jv.getLgFiscalYear()):null)%></c:listcol>
                  <c:listcol title="MONTH" ><%=jspUtil.print(!isdetail?String.valueOf(jv.getLgPeriodNo()):null)%></c:listcol>
                  <c:listcol title="DESC" ><%=jspUtil.print(jv.getStDescription())%></c:listcol>
                  <c:listcol title="SALDO" align="right" ><%=jspUtil.print(jv.getDbDebit(),2)%></c:listcol> 
               </c:evaluate>
            </c:listbox>
         </td>
      </tr>
      <tr>
         <td>
            <%=jspUtil.getButtonNormal("crt","Create Neraca","openDialog('so.ctl?EVENT=SY_GL_ENTRY_ADD', 800,500,refreshcb);")%>
            <%=jspUtil.getButtonNormal("crt","Create Laba Rugi","openDialog('so.ctl?EVENT=SY_GL_ENTRY_ADD_LR', 800,500,refreshcb);")%>
            <%=jspUtil.getButtonNormal("crt", "Create RKAP", "openDialog('so.ctl?EVENT=SY_GL_ENTRY_ADD_RKAP', 600,500,refreshcb);")%>

             <%=jspUtil.getButtonNormal("edt","Edit","openDialog('so.ctl?EVENT=SY_GL_ENTRY_EDIT&trxhdrid='+f.trxhdrid.value, 800,500,refreshcb);")%>
            <%=jspUtil.getButtonNormal("v","View","openDialog('so.ctl?EVENT=SY_GL_ENTRY_VIEW&trxhdrid='+f.trxhdrid.value, 800,500,refreshcb);")%>
            <%=jspUtil.getButtonNormal("edt","Approve","openDialog('so.ctl?EVENT=SY_GL_ENTRY_VIEW_APPROVE&trxhdrid='+f.trxhdrid.value, 800,500,refreshcb);")%>
            <%=jspUtil.getButtonNormal("rev", "Reverse", "openDialog('so.ctl?EVENT=SY_ENTRY_REVERSE&trxhdrid='+f.trxhdrid.value, 800,500,refreshcb);")%>

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