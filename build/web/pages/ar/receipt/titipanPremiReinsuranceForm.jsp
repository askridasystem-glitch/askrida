<%@ page import="com.webfin.gl.model.TitipanPremiReinsuranceView,
         com.webfin.gl.model.TitipanPremiReinsuranceHeaderView,
         com.crux.util.validation.FieldValidator,
         com.crux.util.*,
         com.webfin.gl.ejb.CostCenterManager,
         com.webfin.ar.model.ARPaymentMethodView,
         com.webfin.ar.forms.ARTitipanPremiForm,
         com.webfin.gl.ejb.CurrencyManager"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="SETTLEMENT" >
    <%

            final ARTitipanPremiForm form = (ARTitipanPremiForm) request.getAttribute("FORM");

            final TitipanPremiReinsuranceHeaderView header = form.getTitipanReinsurance();

            boolean readOnly = form.isReadOnly();

    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:field name="titipanReinsurance.stTransactionNo" width="200" caption="No Bukti" type="string" presentation="standard" readonly="true"/>
                                <c:field lov="LOV_CostCenter" readonly="<%=header.getStCostCenter()!=null%>" width="200" name="titipanReinsurance.stCostCenter" mandatory="true" caption="Cabang" type="string" presentation="standard"/>
                                 
                                
                                <c:field width="200" name="titipanReinsurance.stMonths" lov="LOV_MONTH_Period" mandatory="true" caption="Bulan" type="string" presentation="standard"/>
                                <c:field width="200" name="titipanReinsurance.stYears" lov="LOV_GL_Years2"  changeaction="selectYearReinsurance" mandatory="true" caption="Tahun" type="string" presentation="standard"/>
                                <c:field width="200" name="titipanReinsurance.stMethodCode" lov="LOV_ReceiptCashBank" mandatory="true" caption="Metode" type="string" presentation="standard"/>
                               
                                <tr>
                                    <td>Rekening</td>
                                    <td>:</td>
                                    <td>
                                        <c:field show="false" width="200" name="titipanReinsurance.stAccountIDMaster" mandatory="true" caption="Rekening" type="string"/>
                                        <c:field width="200" name="titipanReinsurance.stAccountNoMaster" mandatory="true" caption="Rekening" type="string"/>
                                        <c:button text="..." clientEvent="selectAccountByBranch();" validate="false" enabled="true"/>
                                    </td>
                                </tr>

                               <c:field width="400" name="titipanReinsurance.stDescriptionMaster" mandatory="true" caption="Nama Rekening" type="string" presentation="standard" />
                               <c:field name="titipanReinsurance.stFilePhysic" caption="Upload Excel" type="file" thumbnail="true"
                                                             readonly="false" presentation="standard"/>
                                 <tr>
                                    <td><c:button show="true" text="Konversi" event="uploadExcelReinsurance"/>
                                    </td>
                                 </tr>
                            </table>
                        </td>

                    </tr>
                    
                    <tr>
                        <td>
                            <c:field name="notesindex" hidden="true" type="string"/>
                            <c:listbox name="detailsReinsurance" >
                                <c:listcol title="" columnClass="header" >
                                </c:listcol>
                                <c:listcol title="" columnClass="detail" >
                                    <c:button text="-" event="delLine" clientEvent="f.notesindex.value='$index$';" validate="false" enabled="<%=!readOnly%>"/>
                                </c:listcol>
                                <c:listcol title="Counter" >
                                    <c:field name="detailsReinsurance.[$index$].stCounter" width="25" caption="No Bukti" type="string" readonly="true" />
                                </c:listcol>
                                <c:listcol title="Rekening" >
                                    <c:field show="false" name="detailsReinsurance.[$index$].stAccountID" width="130" mandatory="true" caption="Deskripsi" type="string" readonly="false" />
                                    <c:field name="detailsReinsurance.[$index$].stAccountNo" width="130" mandatory="true" caption="Deskripsi" type="string" readonly="false" />
                                        <c:button text="..." clientEvent="rnc='$index$';selectAccountByBranch2();" validate="false" enabled="true"/>
                                </c:listcol> 
                                
                                <c:listcol title="Keterangan" >
                                    <c:field name="detailsReinsurance.[$index$].stDescription" width="300" mandatory="true" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Penyebab" >
                                    <c:field name="detailsReinsurance.[$index$].stCause" lov="LOV_TitipanCause" width="160" mandatory="true" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Nomor Polis" >
                                    <c:field name="detailsReinsurance.[$index$].stPolicyID" lov="LOV_Policy" popuplov="true" width="130" caption="Deskripsi" type="string" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Tanggal" >
                                    <c:field name="detailsReinsurance.[$index$].dtApplyDate" width="130" mandatory="true" caption="Deskripsi" type="date" readonly="false" />
                                </c:listcol>
                                <c:listcol title="Jumlah" >
                                    <c:field name="detailsReinsurance.[$index$].dbAmount" width="130" mandatory="true" caption="Deskripsi" type="money16.2" readonly="false" />
                                </c:listcol>

                            </c:listbox>
                        </td>
                    </tr>

                    <%--
                    <tr>
                        <td align="right">
                            <c:button text="+ Titipan Premi" event="doNewTitipan" defaultRO="true"/>
                        </td>
                    </tr>
                    --%>
                     <td align="right">
                        Total : <c:field width="130" name="dbTotalTitipan" readonly="true" caption="Rekening" type="money16.2"/>
                    </td>

                </table>
            </td>
        </tr>
        


        <td align=center>
            <c:evaluate when="<%=!readOnly%>" >
                <c:button text="Hitung Ulang" event="doRecalculateReinsurance"/>

                <c:button text="Simpan" event="doSaveReinsurance" validate="true" confirm="Do you want to save ?" />
                <c:button text="Cancel" event="doCancel" confirm="Do you want to cancel ?" />
            </c:evaluate>
            <c:evaluate when="<%=readOnly%>" >
                <c:button text="Close" event="doClose"/>
            </c:evaluate>
            <%--<c:button text="Approve" event="doApprove" confirm="Do you want to approve ?" />
            --%>
        </td>

</table>

</c:frame>
<script>
    function addnewinvoice() {
        openDialog('c.ctl?EVENT=AR_titipan_SEARCH_INVOICE'
            +'&ccy='+docEl('titipanReinsurance.stCurrencyCode').value
            +'&cust='+docEl('titipanReinsurance.stEntityID').value
            +'&arsid='+docEl('titipanReinsurance.stARSettlementID').value
            +'&cc_code='+docEl('titipanReinsurance.stCostCenterCode').value
            +'&type='+docEl('titipanReinsurance.stInvoiceType').value,
        500,400,
        function (o) {
            if (o!=null) {
                f.parinvoiceid.value=o.INVOICE_ID;
                f.action_event.value='onNewInvoice';
                f.submit();
            }
        }
    );
    }

    function addnewnote() {
        openDialog('c.ctl?EVENT=AR_titipan_SEARCH_INVOICE'
            +'&ccy='+docEl('titipanReinsurance.stCurrencyCode').value
            +'&cust='+docEl('titipanReinsurance.stEntityID').value
            +'&type='+docEl('titipanReinsurance.stNoteType').value,
        500,400,
        function (o) {
            if (o!=null) {
                f.parinvoiceid.value=o.INVOICE_ID;
                f.action_event.value='onNewNote';
                f.submit();
            }
        }
    );
    }
   
    function addnewsurathutang() {
        openDialog('c.ctl?EVENT=AR_titipan_SEARCH_SURAT_HUTANG'
            +'&ccy='+docEl('titipanReinsurance.stCurrencyCode').value
            +'&cust='+docEl('titipanReinsurance.stEntityID').value
            +'&arsid='+docEl('titipanReinsurance.stARSettlementID').value
            +'&cc_code='+docEl('titipanReinsurance.stCostCenterCode').value
            +'&type='+docEl('titipanReinsurance.stInvoiceType').value,
        500,400,
        function (o) {
            if (o!=null) {
                f.nosurathutang.value=o.NO_SURAT_HUTANG;
                f.action_event.value='onNewSuratHutang';
                f.submit();
            }
        }
    );
    }
   
    function searchTitipan() {
        openDialog('c.ctl?EVENT=AR_titipan_SEARCH_TITIPAN'
            +'&ccy='+docEl('titipanReinsurance.stCurrencyCode').value
            +'&cust='+docEl('titipanReinsurance.stEntityID').value
            +'&paymentmethodid='+docEl('titipanReinsurance.stPaymentMethodID').value
            +'&arsid='+docEl('titipanReinsurance.stARSettlementID').value
            +'&cc_code='+docEl('titipanReinsurance.stCostCenterCode').value
            +'&type='+docEl('titipanReinsurance.stInvoiceType').value,
        500,400,
        function (o) {
            if (o!=null) {
                f.artitipanid.value=o.TITIPAN_ID;
                document.getElementById('titipanReinsurance.stARTitipanID').value=o.TITIPAN_ID;
                //f.action_event.value='onNewSuratHutang';
                //f.submit();
            }
        }
    );
    }
   
    /*
      function selectAccount(o) {
                openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT', 400,400,selectAccount2);
   }
   
      function selectAccount2(o) {
      if (o==null) return;
      document.getElementById('titipan.stAccountID').value=o.acid;
      document.getElementById('titipan.stAccountNo').value=o.acno;
      document.getElementById('titipan.stDescription').value=o.desc;
   }*/
   
    
    function pop(formName) {
        openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT', 400,400,selectARAccountDet);
    }

    function selectARAccountDet(o) {
        if (o!=null) {
            docEl('titipanReinsurance.stAccountID').value=o.acid;
            docEl('titipanReinsurance.stAccountNo').value=o.acno;
            docEl('titipanReinsurance.stDescription').value=o.desc;
        }
    }

    function selectAccountByBranch(){

        var cabang = document.getElementById('titipanReinsurance.stCostCenter').value;
        var receiptclass = document.getElementById('titipanReinsurance.stMethodCode').value;

        var acccode;

        if(receiptclass == ''){
            alert('Pilih dulu METODE titipan premi');
            return false;
        }

        var month = document.getElementById('titipanReinsurance.stMonths').value;
        var year = document.getElementById('titipanReinsurance.stYears').value;

        openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'&month='+month+'&year='+year+'', 600,400,selectAccount2);


        //openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'', 400,400,selectAccount2);

    }

    function selectAccount2(o) {
      if (o==null) return;
      document.getElementById('titipanReinsurance.stAccountIDMaster').value=o.acid;
      document.getElementById('titipanReinsurance.stAccountNoMaster').value=o.acno;
      document.getElementById('titipanReinsurance.stDescriptionMaster').value=o.desc;
   }

   function selectAccountByBranch2(o){

        var cabang = document.getElementById('titipanReinsurance.stCostCenter').value;
        var receiptclass = document.getElementById('titipanReinsurance.stMethodCode').value;
        var acccode;

        if(receiptclass=='A') acccode = '48920';
        else if(receiptclass=='B') acccode = '48920';
        else if(receiptclass=='C') acccode = '48921';
        else if(receiptclass=='D') acccode = '48922';

        var glcode = document.getElementById('titipanReinsurance.stAccountNoMaster').value.substring(5,10);
        if(glcode!='')
            acccode = acccode + glcode;

        var month = document.getElementById('titipanReinsurance.stMonths').value;
        var year = document.getElementById('titipanReinsurance.stYears').value;

        openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'&month='+month+'&year='+year+'', 600,400,selectAccount);


        //openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'', 400,400,selectAccount);
   }

var rnc;
   function selectAccount(o) {
      if (o==null) return;

      document.getElementById('details.['+ rnc +'].stAccountID').value=o.acid;
      document.getElementById('details.['+ rnc +'].stAccountNo').value=o.acno;
      document.getElementById('details.['+ rnc +'].stDescription').value=o.desc;

   }


</script>