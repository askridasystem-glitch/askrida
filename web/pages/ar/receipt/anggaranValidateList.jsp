<%@ page import="com.webfin.ar.model.ARRequestFee,
         com.crux.lov.LOVManager,
         com.crux.util.LOV,
         com.crux.util.*,
         com.crux.web.controller.SessionManager,
         com.crux.lang.LanguageManager,
         com.webfin.ar.forms.RequestFeeList"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="VALIDASI BIAYA" >
    <%
                final RequestFeeList form = (RequestFeeList) frame.getForm();

                final boolean canNavigateBranch = SessionManager.getInstance().getSession().getStBranch() != null ? false : true;

                final boolean canNavigateRegion = SessionManager.getInstance().getSession().getStRegion() != null ? false : true;

    %>

    <table cellpadding=2 cellspacing=1>     
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <c:field width="200" caption="{L-ENGBranch-L}{L-INACabang-L}" lov="LOV_Branch" name="stBranch" type="string" readonly="<%=!canNavigateBranch%>" presentation="standard" changeaction="refresh" />
                            <c:field show="<%=form.getStBranch() != null%>" width="200" caption="{L-ENGRegion-L}{L-INARegion-L}" lov="LOV_Region" name="stRegion" type="string" readonly="<%=!canNavigateRegion%>" presentation="standard" changeaction="refresh" >
                                <c:lovLink name="cc_code" link="stBranch" clientLink="false"/>
                            </c:field>
                        </td>
                    </tr>
                    <tr>
                        <td>Tanggal Permintaan</td>
                        <td>:</td>
                        <td>
                            <c:field name="dtReqDateFrom" type="date" />
                            {L-ENGTo-L}{L-INAS/D-L} <c:field name="dtReqDateTo" type="date" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <c:field caption="{L-ENGShow active &amp; Pending only-L}{L-INAPembentukan Aktif &amp; Pending-L} " name="stActiveFlag" type="check" presentation="standard" changeaction="refresh" />
                            <c:field caption="{L-ENGShow Pending only-L}{L-INAPembentukan Belum Disetujui-L} " name="stEffectiveFlag" type="check" presentation="standard" changeaction="refresh" />
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td> 
                <c:button text="{L-ENGRefresh-L}{L-INARefresh-L}" event="refresh" />    
            </td>
        </tr>                     
        <tr>
            <td>
                <c:listbox name="listval" autofilter="true" paging="true" selectable="true" view="com.webfin.ar.model.ARRequestFee" >
                    <c:listcol name="stARRequestID" title="" selectid="arreqid" />
                    <c:listcol name="stActiveFlag" title="Act" flag="true"/>
                    <c:listcol name="stCashierFlag" title="Paid" flag="true"/>
                    <c:listcol name="stPrintFlag" title="Prt" flag="true"/>
                    <c:listcol filterable="true" name="stTransactionNo" title="No. Bukti"/>
                    <c:listcol filterable="true" name="stRequestNo" title="No. Permintaan"/>
                    <c:listcol filterable="true" name="stARRequestID" title="ID"/>
                    <c:listcol name="dbNominalUsed" title="Jumlah" align="right"/>
                    <c:listcol name="stCashierName" title="Cashier"/>
                </c:listbox>
            </td>
        </tr>
        <tr>
            <td>
                <c:button text="Bayar" event="clickPaidValidate" />
                <c:button text="Lihat" event="clickViewValidate" />
            </td>
        </tr>
        <tr>
            <td>
                Print <c:field name="printLang" width="250" type="string" lov="LOV_REQUEST_PRINTING"></c:field> in <c:field name="stLang" type="string" value="<%=LanguageManager.getInstance().getActiveLang()%>" lov="LOV_LANG" />
                <c:button text="Print" name="bprintx" clientEvent="dynPrintClick();" />
            </td>
        </tr>
    </table>
    <iframe src="" id=frmx width=1 height=1></iframe>

</c:frame>
<script>
    var frmx = docEl('frmx');
    function dynPrintClick() {

        if (f.arreqid.value=='') {
            alert('Pilih ID');
            return;
        }

        if (true) {
            frmx.src='x.fpc?EVENT=REQUEST_PRINT&arreqid='+f.arreqid.value+'&alter='+f.printLang.value+'&xlang='+f.stLang.value;
            return;
        }
    }
</script>