<%@ page import="com.webfin.ar.model.ARInvestmentBungaView,
com.crux.lov.LOVManager,
com.crux.util.LOV,
com.crux.lang.LanguageManager,
com.crux.web.controller.SessionManager,
com.webfin.ar.forms.BungaList"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="BUNGA DEPOSITO" >
    
    <%
    final BungaList form = (BungaList) frame.getForm();
    
    final boolean canNavigateBranch = SessionManager.getInstance().getSession().getStBranch()!=null?false:true;
    %>
    <table cellpadding=2 cellspacing=1>  
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>  
                    <tr>
                        <td>
                            <c:field width="200" caption="{L-ENGBranch-L}{L-INACabang-L}" lov="LOV_Branch" name="stBranch" type="string" readonly="<%=!canNavigateBranch%>" presentation="standard" changeaction="refresh" />
                        </td>
                    </tr>
                    <tr>
                        <td>{L-ENGRate Date-L}{L-INATanggal Bunga-L}</td>
                        <td>:</td>
                        <td>
                            <c:field name="dtBungaDateFrom" caption="Bunga Date From" type="date" />
                            {L-ENGTo-L}{L-INAS/D-L} <c:field name="dtBungaDateTo" caption="Bunga Date To" type="date" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <c:field caption="{L-ENGShow active &amp; Pending only-L}{L-INABunga Aktif &amp; Pending-L} " name="stActiveFlag" type="check" presentation="standard" changeaction="refresh" />
                            <c:field caption="{L-ENGShow Pending only-L}{L-INABunga Belum Disetujui-L} " name="stEffectiveFlag" type="check" presentation="standard" changeaction="refresh" />
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
                <c:listbox name="list" autofilter="true" paging="true" selectable="true" view="com.webfin.ar.model.ARInvestmentBungaView" >
                    <c:listcol name="stARBungaID" title="" selectid="arbungaid" />
                    <c:listcol name="stActiveFlag" title="Act" flag="true"/>
                    <c:listcol name="stEffectiveFlag" title="Eff" flag="true"/>
                    <c:listcol filterable="true" name="stNodefo" title="No. Bilyet"/>
                    <c:listcol filterable="true" name="stNoRekening" title="No. Rekening Bilyet"/>
                    <c:listcol filterable="true" name="stNoBuktiD" title="No. Bukti Bunga"/>
                    <c:listcol filterable="true" name="stNoBuktiB" title="No. Bukti Pembentukan"/>
                    <c:listcol name="dtTglBunga" title="Tanggal Bunga" />
                    <c:listcol name="dtTglAwal" title="Tanggal Awal" />
                    <c:listcol name="dtTglAkhir" title="Tanggal Akhir" />
                    <c:listcol name="dtTglCair" title="Tanggal Cair" />
                    <c:listcol name="dtTglEntryCair" title="Tanggal Entry Cair" />
                    <c:listcol name="dbAngka" title="Nominal" />
                    <c:listcol filterable="true" name="stChangeWho" title="User ID" />
                    <c:listcol filterable="true" name="stChangeName" title="User Name" />
                    <c:listcol name="stDepoName" title="Deposito" />
                </c:listbox>
            </td>
        </tr>
        
        <tr>
            <td>
                <c:button text="{L-ENGCreate-L}{L-INABuat Bunga-L}" event="clickCreate" />
                <c:button text="{L-ENGEdit-L}{L-INAUbah-L}" event="clickEdit" />
                <c:button text="{L-ENGView-L}{L-INALihat-L}" event="clickView" />
            </td>
        </tr>
        <tr>
            <td>
                <c:button show="<%=form.isCanApprove()%>" text="{L-ENGApproval-L}{L-INASetujui-L}" event="clickApproval" />
                <c:button show="<%=form.isCanApprove()%>" text="Reverse" event="clickReverse" />
            </td>
        </tr>    
    </table>
    
</c:frame>