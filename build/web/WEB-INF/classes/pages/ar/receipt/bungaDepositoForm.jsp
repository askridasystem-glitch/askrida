<%@ page import="com.webfin.gl.model.BungaDepositoView,
         com.webfin.gl.model.BungaDepositoHeaderView,
         com.crux.util.validation.FieldValidator,
         com.crux.util.*,
         com.webfin.gl.ejb.CostCenterManager,
         com.webfin.ar.model.ARPaymentMethodView,
         com.webfin.ar.forms.BungaDepositoForm,
         com.webfin.gl.ejb.CurrencyManager"%>
<%@ taglib prefix="c" uri="crux" %><c:frame title="SETTLEMENT" >
    <%

                final BungaDepositoForm form = (BungaDepositoForm) request.getAttribute("FORM");

                final BungaDepositoHeaderView header = form.getTitipan();

                boolean readOnly = form.isReadOnly();

    %>
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <tr>
                        <td>
                            <table cellpadding=2 cellspacing=1>
                                <c:fieldcontrol when="<%=!form.isSaveMode()%>" readonly="true">
                                    <c:field name="titipan.stTransactionHeaderNo" width="200" caption="No Konversi" type="string" presentation="standard" mandatory="true"/>
                                    <c:field lov="LOV_CostCenter" readonly="<%=header.getStCostCenter() != null%>" width="200" name="titipan.stCostCenter" mandatory="true" caption="Cabang" type="string" presentation="standard"/>
                                    <c:field width="200" name="titipan.stMonths" lov="LOV_MONTH_Period" mandatory="true" caption="Bulan" type="string" presentation="standard"/>
                                    <c:field width="200" name="titipan.stYears" lov="LOV_GL_Years2" mandatory="true" caption="Tahun" type="string" presentation="standard"/>
                                    <c:field width="200" name="titipan.stMethodCode" lov="LOV_ReceiptCashBank" mandatory="true" caption="Metode" type="string" presentation="standard"/>

                                    <tr>
                                        <td>Rekening</td>
                                        <td>:</td>
                                        <td>
                                            <c:field show="false" width="200" name="titipan.stHeaderAccountID" mandatory="true" caption="Rekening" type="string"/>
                                            <c:field width="200" name="titipan.stHeaderAccountNo" mandatory="true" caption="Rekening" type="string"/>
                                            <c:button text="..." clientEvent="selectAccountByBranch();" validate="false" enabled="true"/>
                                        </td>
                                    </tr>

                                    <c:field width="400" name="titipan.stHeaderAccountMaster" mandatory="true" caption="Nama Rekening" type="string" presentation="standard"/>
                                    <c:field name="titipan.stFilePhysic" caption="Upload Excel" type="file" thumbnail="true" presentation="standard"/>
                                    <tr>
                                        <td><c:button show="true" text="Konversi" event="uploadExcel"/>
                                        </td>
                                    </tr>
                                </c:fieldcontrol>
                            </table>
                        </td>

                    </tr>

                    <tr>
                        <td>
                            <c:field name="notesindex" hidden="true" type="string"/>
                            <c:listbox name="details" >
                                <c:listcol title="" columnClass="header" >
                                </c:listcol>
                                <c:listcol title="" columnClass="detail" >
                                    <c:button text="-" event="delLine" clientEvent="f.notesindex.value='$index$';" validate="false" enabled="<%=!readOnly%>"/>
                                </c:listcol>
                                <c:listcol title="No Bukti" >
                                    <c:field name="details.[$index$].stTransactionNo" width="200" caption="Deskripsi" type="string" readonly="true"/>
                                </c:listcol>
                                <c:listcol title="Rekening" >
                                    <c:field show="false" name="details.[$index$].stAccountID" width="130" mandatory="true" caption="Deskripsi" type="string" readonly="true"/>
                                    <c:field name="details.[$index$].stAccountNo" width="130" mandatory="true" caption="Deskripsi" type="string" readonly="true"/>
                                </c:listcol>
                                <c:listcol title="Keterangan" >
                                    <c:field name="details.[$index$].stDescription" width="250" mandatory="true" caption="Deskripsi" type="string" readonly="<%=!form.isEditMode()%>"/>
                                </c:listcol>
                                <c:listcol title="Tanggal" >
                                    <c:field name="details.[$index$].dtApplyDate" width="130" mandatory="true" caption="Deskripsi" type="date" readonly="<%=!form.isEditMode()%>"/>
                                </c:listcol>
                                <c:evaluate when="<%=form.isEditMode() || form.isInputMode()%>" >
                                    <c:listcol title="Jumlah" >
                                        <c:field name="details.[$index$].dbAmount" width="130" mandatory="true" caption="Deskripsi" type="money16.2" readonly="<%=!form.isEditMode()%>" />
                                    </c:listcol>
                                </c:evaluate>
                                <c:evaluate when="<%=form.isInputMode()%>" >
                                    <c:listcol title="Nodefo" >
                                        <c:field width="200" name="details.[$index$].stARBungaID" lov="LOV_BungaDeposito" popuplov="true" caption="Nodefo" type="string" >
                                            <c:param name="cc_code" value="<%=header.getStCostCenter()%>"/>
                                        </c:field>
                                    </c:listcol>
                                </c:evaluate>
                            </c:listbox>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>

        <td align=center>
            <c:evaluate when="<%=form.isSaveMode()%>" >
                <c:button text="Simpan" event="doSave" validate="true" confirm="Do you want to save ?" />
            </c:evaluate>
            <c:evaluate when="<%=form.isInputMode()%>" >
                <c:button text="Simpan Bilyet" event="doSaveInput" validate="true" confirm="Do you want to save ?" />
            </c:evaluate>
            <c:evaluate when="<%=form.isApproveMode()%>" >
                <c:button text="Setujui" event="doApprove" validate="true" confirm="Do you want to approve ?" />
            </c:evaluate>
            <c:evaluate when="<%=form.isReverseMode()%>" >
                <c:button text="Reverse" event="doReverse" validate="true" confirm="Do you want to reverse ?" />
            </c:evaluate>
            <c:evaluate when="<%=form.isDeleteMode()%>" >
                <c:button text="Hapus" event="doDelete" validate="true" confirm="Do you want to delete ?" />
            </c:evaluate>
            <c:button text="Cancel" event="doCancel" confirm="Do you want to cancel ?" />
        </td>

    </table>

</c:frame>
<script>
    function addnewinvoice() {
        openDialog('c.ctl?EVENT=AR_titipan_SEARCH_INVOICE'
            +'&ccy='+docEl('titipan.stCurrencyCode').value
            +'&cust='+docEl('titipan.stEntityID').value
            +'&arsid='+docEl('titipan.stARSettlementID').value
            +'&cc_code='+docEl('titipan.stCostCenterCode').value
            +'&type='+docEl('titipan.stInvoiceType').value,
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
            +'&ccy='+docEl('titipan.stCurrencyCode').value
            +'&cust='+docEl('titipan.stEntityID').value
            +'&type='+docEl('titipan.stNoteType').value,
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
            +'&ccy='+docEl('titipan.stCurrencyCode').value
            +'&cust='+docEl('titipan.stEntityID').value
            +'&arsid='+docEl('titipan.stARSettlementID').value
            +'&cc_code='+docEl('titipan.stCostCenterCode').value
            +'&type='+docEl('titipan.stInvoiceType').value,
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
            +'&ccy='+docEl('titipan.stCurrencyCode').value
            +'&cust='+docEl('titipan.stEntityID').value
            +'&paymentmethodid='+docEl('titipan.stPaymentMethodID').value
            +'&arsid='+docEl('titipan.stARSettlementID').value
            +'&cc_code='+docEl('titipan.stCostCenterCode').value
            +'&type='+docEl('titipan.stInvoiceType').value,
        500,400,
        function (o) {
            if (o!=null) {
                f.artitipanid.value=o.TITIPAN_ID;
                document.getElementById('titipan.stARTitipanID').value=o.TITIPAN_ID;
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
            docEl('titipan.stAccountID').value=o.acid;
            docEl('titipan.stAccountNo').value=o.acno;
            docEl('titipan.stDescription').value=o.desc;
        }
    }

    function selectAccountByBranch(){

        var cabang = document.getElementById('titipan.stCostCenter').value;
        var receiptclass = document.getElementById('titipan.stMethodCode').value;

        var acccode;

        

        if(receiptclass == ''){
            alert('Pilih dulu METODE titipan premi');
            return false;
        }

        var month = document.getElementById('titipan.stMonths').value;
        var year = document.getElementById('titipan.stYears').value;

        openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'&month='+month+'&year='+year+'', 600,400,selectAccount2);


        //openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT&acccode='+acccode+'&costcenter='+cabang+'', 400,400,selectAccount2);

    }

    function selectAccount2(o) {
        if (o==null) return;
        document.getElementById('titipan.stHeaderAccountID').value=o.acid;
        document.getElementById('titipan.stHeaderAccountNo').value=o.acno;
        document.getElementById('titipan.stHeaderAccountMaster').value=o.desc;
    }

    function selectAccountByBranch2(o){

        var cabang = document.getElementById('titipan.stCostCenter').value;
        var receiptclass = document.getElementById('titipan.stMethodCode').value;
        var acccode;

        if(receiptclass=='A') acccode = '48920';
        else if(receiptclass=='B') acccode = '48920';
        else if(receiptclass=='C') acccode = '48921';
        else if(receiptclass=='D') acccode = '48922';

        var glcode = document.getElementById('titipan.stHeaderAccountMaster').value.substring(5,10);
        if(glcode!='')
            acccode = acccode + glcode;

        var month = document.getElementById('titipan.stMonths').value;
        var year = document.getElementById('titipan.stYears').value;

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
   
    function selectBungaDeposito(){

        var cabang = document.getElementById('titipan.stCostCenter').value;
        var month = document.getElementById('titipan.stMonths').value;
        var year = document.getElementById('titipan.stYears').value;

        openDialog('so.ctl?EVENT=AR_RECEIPT_SELECT_BUDEP&costcenter='+cabang+'&month='+month+'&year='+year+'', 600,400,selectBudep);

    } 
    
    function selectBudep(o) {
        if (o==null) return;
        document.getElementById('titipan.stHeaderAccountID').value=o.acid;
        document.getElementById('titipan.stHeaderAccountNo').value=o.acno;
        document.getElementById('titipan.stHeaderAccountMaster').value=o.desc;
    }
</script>