<%@ page import="com.webfin.insurance.model.InsuranceKreasiView,
         com.webfin.insurance.form.PolicyListForm"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="Asuransi Kredit/PA Kreasi Data Searching" >

    <%
                final PolicyListForm form = (PolicyListForm) frame.getForm();

                final boolean canNavigateBranch = form.isKreasiNavigateBranch();

    %>

    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="stPolicy" type="string" caption="{L-ENGPolicy No.-L}{L-INANo. Polis-L}" presentation="standard" width="200" />
                                <c:field name="stEntityID" lov="LOV_EntityOnly" popuplov="true" type="string" caption="{L-ENGBusiness Source-L}{L-INASumber Bisnis-L}" presentation="standard" width="200" />
                                <c:field name="stName" type="string" caption="{L-ENGMember Name-L}{L-INANama Nasabah-L}" presentation="standard" width="200" />
                                <c:field name="dtBirthDate" type="date" caption="{L-ENGBirth Date-L}{L-INATanggal Lahir-L}" presentation="standard" width="200" />
                                <c:field name="stNoIdentitas" type="string" caption="Nomor Identitas" presentation="standard" width="200" />
                                <c:field name="stNoRekeningPinjaman" type="string" caption="No Rekening Pinjaman" presentation="standard" width="200" />
                                <c:field name="stNoPerjanjianKredit" type="string" caption="No Perjanjian Kredit" presentation="standard" width="200" />
                                 <c:field name="stFilePhysic" caption="File" type="file" thumbnail="true" readonly="false" presentation="standard" width="200" />
                            </table>

                        </td>
                        <td>
                            <table cellpadding=2 cellspacing=1> 
                                <tr>
                                    <td>{L-ENGPolicy Date-L}{L-INATanggal Polis-L}</td>
                                    <td>:</td>
                                    <td>
                                        <c:field name="dtPolicyDateFrom" type="date" caption="Policy From"  />
                                        To <c:field name="dtPolicyDateTo" type="date" caption="Policy To" />
                                    </td>
                                </tr>
                                <tr>
                                    <td>{L-ENGLiquidity Date-L}{L-INATanggal Pencairan-L}</td>
                                    <td>:</td>
                                    <td>
                                        <c:field name="dtLiquidityFrom" type="date" caption="Liquidity From"  />
                                        To <c:field name="dtLiquidityTo" type="date" caption="Liquidity To" />
                                    </td>
                                </tr>
                                <tr>
                                    <td>{L-ENGEnd Of Credit-L}{L-INAAkhir Kredit-L}</td>
                                    <td>:</td>
                                    <td>
                                        <c:field name="dtEndOfCreditFrom" type="date" caption="EndOfCredit From"  />
                                        To <c:field name="dtEndOfCreditTo" type="date" caption="EndOfCredit To" />
                                    </td>
                                </tr>
                                <tr>
                                    <c:field width="200" caption="{L-ENGBranch-L}{L-INACabang-L}" lov="LOV_Branch" name="stBranch" type="string" readonly="<%=!canNavigateBranch%>" presentation="standard" changeaction="btnRefresh" />
                                    <c:field width="200" caption="{L-ENGData-L}{L-INAData-L}" lov="LOV_KreasiData" name="stKreasi" type="string" presentation="standard" />
                                    <c:field name="stPolicyLevel" type="string" caption="Level" lov="LOV_PolicyLevel" presentation="standard" width="200" />
                                </tr>            
                </table>
            </td>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                                    <td>Jangka Waktu</td>
                                    <td>:</td>
                                    <td>
                                        <c:field width="200" lov="LOV_KreasiKredit" name="stKreasiKredit" type="string"  />
                                        <c:field name="stJangkaWaktu" type="string" width="50" />
                                 </td>

                              </tr>
                    <tr>
                        <c:field width="200" caption="Tanggal/Tahun" lov="LOV_KreasiTanggalTaon" name="stTglTahun" type="string" presentation="standard" />
                                  <c:field name="stAge" type="string" caption="Usia >=" presentation="standard" width="50"/>
                                 <c:field name="stUsia" type="string" caption="Usia <=" presentation="standard" width="50"/>
                                 <c:field width="200" caption="Kategori Debitur" lov="VS_INSOBJ_CREDIT_STATUS" name="stKategoriDeb" type="string" presentation="standard" />
                                <c:field width="200" caption="Pekerjaan Debitur" lov="VS_INSOBJ_PEKERJAAN_DEBITUR" name="stPekerjaanDeb" type="string" presentation="standard" />
                    </tr>
                </table>
            </td>

        </tr>
        <tr>
            <td>
                <c:button text="Refresh" event="btnRefresh" />
                <c:button text="Print Excel" event="EXCEL_PAKREASI" />
                <c:button text="Cek Debitur" event="clickCheck" />
                <c:button text="Get No Polis/PK" event="getDataDetailPolisToExcel" />
            </td>
        </tr>
    </table> 

</td>
</tr>

<tr>
    <td>
        <c:listbox name="pakreasilist" paging="true" selectable="true" view="com.webfin.insurance.model.InsuranceKreasiView">
            <c:listcol name="stInsuranceNoPolis" title="{L-ENGPolicy No-L}{L-INANo Polis-L}" />
            <c:listcol name="stEntityName" title="Sumber Bisnis" />
            <c:listcol name="stInsuranceNoUrut" title="{L-ENGNo-L}{L-INANo Urut-L}" />
            <c:listcol name="stInsuranceNama" title="{L-ENGMember Name-L}{L-INANama Nasabah-L}" />
            <c:listcol name="stInsuranceUmur" title="{L-ENGAge-L}{L-INAUsia-L}" />
            <c:listcol name="dtTanggalLahir" title="{L-ENGBirth Date-L}{L-INATanggal Lahir-L}" />
            <c:listcol name="stNoIdentitas" title="No Identitas" />
            <c:listcol name="stNoRekeningPinjaman" title="No Rekening Pinjaman" />
            <c:listcol name="stNoPerjanjianKredit" title="No Perjanjian Kredit" />
            <c:listcol name="dtTanggalCair" title="{L-ENGLiquidity Date-L}{L-INATanggal Pencairan-L}" />
            <c:listcol name="dtTanggalAkhir" title="{L-ENGEnd Of Credit-L}{L-INAAkhir Kredit-L}" />
            <c:listcol name="dtTanggalAwalPK" title="Tanggal Awal Sesuai PK" />
            <c:listcol name="dtTanggalAkhirPK" title="Tanggal Akhir Sesuai PK" />
            <c:listcol name="dbInsured" title="{L-ENGSum Insured-L}{L-INAHarga Pertanggungan-L}" align="right" />
            <%--<c:listcol name="dbRatePremi" title="{L-ENGPremium Rate-L}{L-INARate Premi-L}" align="right" />--%>
            <c:listcol name="dbPremi" title="{L-ENGPremium-L}{L-INAPremi-L}" align="right" />
            <c:listcol name="stPolicyID" title="{L-ENGPolicy ID-L}{L-INAPolis ID-L}" />
        </c:listbox>
    </td>
</tr>

</table>



</c:frame>