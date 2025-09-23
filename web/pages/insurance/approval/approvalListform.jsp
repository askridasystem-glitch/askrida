<%@page import="com.webfin.approval.forms.ApprovalListForm"%>
<%@ page import="com.crux.web.controller.SessionManager,
                com.webfin.approval.forms.ApprovalListForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="NOTIFIKASI APPROVAL" >
    <%
        final ApprovalListForm form = (ApprovalListForm) frame.getForm();
    %>
    <table cellpadding=2 cellspacing=1>
        
        <tr>
            <td>
                <table>
                    <tr>
                        <td>
                            <c:field width="200" caption="{L-ENGBranch-L}{L-INACabang-L}" lov="LOV_Branch" name="stBranch" type="string" readonly="false" presentation="standard" changeaction="refresh" />

                            <c:field show="<%=form.getStBranch()!=null%>" width="200" caption="{L-ENGRegion-L}{L-INARegion-L}" lov="LOV_Region" name="stRegion" type="string" readonly="false" presentation="standard" changeaction="refresh" >
                                <c:lovLink name="cc_code" link="stBranch" clientLink="false"/>
                            </c:field>

                            <c:field width="200" caption="Kewenangan" lov="VS_LIMIT_RISK" name="stLimit" type="string" readonly="false" presentation="standard" changeaction="refresh" />
                        </td>
                        <td>
                            <c:button text="Refresh" event="refresh" />
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <c:listbox autofilter="true" name="list" selectable="true" paging="true" view="com.webfin.approval.model.ApprovalView" >
                    <c:listcol name="stInID" title="" selectid="inid"/>
                    <c:listcol name="stRiskAnalysisEffectiveFlag" title="A.Resiko" flag="true" />

                    <c:listcol name="stValidasiCabang" title="Val. Cabang" flag="true" />
                    <c:listcol name="stValidasiCabangInduk" title="Val. Induk" flag="true" />
                    <c:listcol name="stValidasiKantorPusat" title="Val. HO" flag="true" />

                    <%--<c:listcol filterable="true" name="stRefNo" title="{L-ENGReference No-L}{L-INANo Surat-L}" />--%>
                    <c:listcol filterable="true" name="stLimit" title="Kewenangan" />
                    <%--<c:listcol filterable="true" name="stStatus" title="Status" />--%>
                    <c:listcol filterable="true" name="stPolicyID" title="Pol ID" />
                    <c:listcol filterable="true" name="stSubject" title="{L-ENGSubject-L}{L-INAJudul-L}" />
                    <c:listcol filterable="true" name="stSenderName" title="{L-ENGFrom-L}{L-INAPengirim-L}" />
                    <c:listcol filterable="true" name="stCostCenterCode" title="Cabang" />
                   <%-- <c:listcol filterable="true" name="stCC" title="{L-ENGTo-L}{L-INAPenerima-L}" />--%>
                    <c:listcol name="dtCreateDate" title="{L-ENGSend Date-L}{L-INATanggal Kirim-L}" />
                    <%--<c:listcol name="stJam" title="{L-ENGLetter Time-L}{L-INAJam Surat-L}" />--%>
                </c:listbox>
            </td>
        </tr>
        
        <tr>
            <td>
                <c:button  text="{L-ENGView-L}{L-INALihat-L}" event="clickView" />
                <%--<c:button  text="Create" event="clickCreate" />
           <c:button  text="Edit" event="clickEdit" />
                <c:button  text="{L-ENGDelete-L}{L-INAHapus-L}" event="clickDelete" />
                
                <c:button text="Print" name="bprintx" clientEvent="dynPrintClick();" />--%>
            </td>
        </tr>
    </table>
    <iframe src="" id=frmx width=1 height=1></iframe>
</c:frame>

<script>
   var frmx = docEl('frmx');

   function dynPrintClick() {

      if (f.inid.value=='') {
         alert('Pilih dulu suratnya');
         return;
      }

      if (true) {
         frmx.src='x.fpc?EVENT=INCOMING_PRINT&inid='+f.inid.value;
         return;
      }


   }

</script>