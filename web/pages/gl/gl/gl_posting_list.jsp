<%@ page import="com.webfin.gl.model.GLPostingView,
com.crux.lov.LOVManager,
com.crux.util.LOV,
com.crux.lang.LanguageManager,
com.webfin.gl.form.GLPostingList"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="GL POSTING LIST" >
    
    <%
    final GLPostingList form = (GLPostingList) frame.getForm();
    %>
    <font size="2">
        Posting hanya bisa dilakukan 1x setiap bulan, pastikan transaksi anda sudah selesai semua pada bulan & tahun yang akan diposting
    </font>
    
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>Cabang</td>
            <td>:</td>
            <td>
                <c:field name="branch" type="string" caption="Cabang" lov="LOV_Branch" changeaction="chgBranch"  width="200" />
            </td>
        </tr>
        <tr>
            <td>Bulan</td>
            <td>:</td>
            <td>
               <c:field name="stMonths" type="string" caption="{L-ENGMonth-L}{L-INABulan Transaksi-L}" lov="LOV_MONTH_Period" changeaction="refresh"/>
            </td>
        </tr>
        <tr>
            <td>Tahun</td>
            <td>:</td>
            <td>
                <c:field name="stYears" type="string" caption="{L-ENGBulan-L}{L-INATahun Transaksi-L}" lov="LOV_GL_Years2" changeaction="refresh"/>
            </td>
        </tr>
        <tr>
            <td colspan="3">
                <c:button text="Cek Balance" event="clickExcelBalance" />
                <c:button show="<%=form.isEnableReverse()%>" text="Migrasi To FoxPro" event="clickExcelMigrasi" />
                <c:button show="<%=form.isEnableReverse()%>" text="Migrasi To FoxPro (Resume)" event="clickExcelMigrasiResume" />
                <c:button show="<%=form.isEnableReverse()%>" text="Migrasi To FoxPro (File)" event="clickExcelMigrasiCsv" />
            </td>
        </tr>
        <tr>
            <td colspan="3">
                <c:listbox name="list" autofilter="true" paging="true" selectable="true" view="com.webfin.gl.model.GLPostingView" >
                    <c:listcol name="stGLPostingID" title="" selectid="glpostingid" />
                    <c:listcol name="stPostedFlag" title="Eff" flag="true"/>
                    <c:listcol name="stFinalFlag" title="Final" flag="true"/>
                    <%--<c:listcol name="stStatus" title="Status"/>--%>
                    <c:listcol filterable="true" name="stYears" title="Tahun" />
                    <c:listcol filterable="true" name="stMonths" title="Bulan"/>
                    <c:listcol name="stCostCenterCode" title="Cabang"/>
                    <c:listcol name="stCostCenter" title="Cabang"/>
                    <c:listcol name="stCreateWho" title="User Entry"/>
                    <c:listcol name="stUserName" title="User Entry"/>
                    <c:listcol name="dtCreateDate" title="Tanggal Entry"/>
                    <c:listcol name="stUserNameEdited" title="Last Edit"/>
                    <c:listcol name="stLastChanged" title="Last Edit Time"/>
                </c:listbox>
            </td>
        </tr>
        
        <tr>
            <td colspan="3">
                <c:button text="Buat" event="clickCreate" />
                <c:button text="Ubah" event="clickEdit" />
                <c:button text="Lihat" event="clickView" />
                <c:button show="<%=form.isEnableReverse()%>" text="Buka Kembali Posting" event="clickReopen" />
                <c:button show="<%=form.isEnableReverse()%>" text="Finalisasi Neraca" event="clickUpdateStatusNeraca" />
            	<%--<c:button show="<%=form.isEnableReverse()%>" text="Posting LabaRugi Berjalan" event="clickNeraca" />--%>
                </td>
        </tr>
        
        
    </table>
    
</c:frame>