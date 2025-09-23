<%@page import="com.crux.util.DTOList"%>
<%@ page import="com.webfin.insurance.model.InsurancePolicyView,
com.crux.util.JSPUtil,
com.crux.lov.LOVManager,
com.crux.util.LOV,
java.util.Iterator,
com.crux.lang.LanguageManager,
com.crux.web.controller.SessionManager,
com.webfin.insurance.form.PolicyListForm"%>
<%@ taglib prefix="c" uri="crux" %>
<c:frame>
    <%
    final PolicyListForm form = (PolicyListForm) frame.getForm();

    DTOList list = form.getListPolicyChecking();

    final boolean showFilter = form.isShowFilter();

    %>
    <table style="background:#ffffff url(images/bg_blue.png) repeat" bgcolor="#176BB5" width="100%" height="100%">
        <tr valign="top">
            <td>
                <table  style="padding: 2em;" bgcolor="#ffffff" align="center" cellpadding=2 cellspacing=1>
                    <tr>
                        <td>

                                <img style="cursor:hand" src="/fin/images/top_img.png"  />

                        </td>
                    </tr>
                    <c:evaluate when="<%=!showFilter%>" >
                        <tr>
                            <td>
                                <table cellpadding=2 cellspacing=1>
                                    <tr>
                                        <td>
                                            <table cellpadding=2 cellspacing=1>
                                                <tr>
                                                    <td><b><p style="font-size:160%;">Input {L-ENGYour Policy Number-L}{L-INANomor Polis Anda-L}</p></b></td>
                                                    <td>:</td>
                                                    <td>
                                                        <c:field width="200" caption="Input {L-ENGYour Policy Number-L}{L-INANomor Polis Anda-L}" name="stPolicyNo" type="string" />
                                                        &nbsp;<c:button text="Cari Data" event="refreshCekPolis" />
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>

                                    </tr>
                                    
                                </table>
                            </td>
                        </tr>
                     </c:evaluate>

                    <%if(list.size()>0){
                        InsurancePolicyView policy = (InsurancePolicyView) list.get(0);
                    %>
                    <tr align="center">
                        <td align="center">

                            <table style="font-size:20px; font-style:bold; padding: 2em;" cellpadding=2 cellspacing=6 height="40%">
                                <th colspan="3" align="center" valign="middle"><u>INFORMASI POLIS <%=policy.getStPolicyTypeDesc().toUpperCase()%></u></th>
                                <tr>
                                    <td>
                                        <p style="font-size:120%;">Nomor Polis</p>
                                    </td>
                                    <td>:</td>
                                    <td><b><p style="font-size:120%;"><%=policy.getStPolicyNo()%></p></b></td>
                                </tr>
                                <tr>
                                    <td>
                                        <p style="font-size:120%;">Jenis Asuransi</p>
                                    </td>
                                    <td>:</td>
                                    <td><b><p style="font-size:120%;"><%=policy.getStPolicyTypeDesc()%></p></b></td>
                                </tr>
                                <tr>
                                    <td>
                                        <p style="font-size:120%;">Tanggal Polis</p>
                                    </td>
                                    <td>:</td>
                                    <td><b><p style="font-size:120%;"><%=jspUtil.print(policy.getDtPolicyDate())%></p></b></td>
                                </tr>
                                <tr>
                                    <td>
                                        <p style="font-size:120%;">Nama Principal</p>
                                    </td>
                                    <td>:</td>
                                    <td><b><p style="font-size:120%;"><%=policy.getStReference5()%></p></b></td>
                                </tr>
                                <tr>
                                    <td>
                                        <p style="font-size:120%;">Nama Obligee</p>
                                    </td>
                                    <td>:</td>
                                    <td><b><p style="font-size:120%;"><%=policy.getStCustomerName()%></p></b></td>
                                </tr>
                                <tr>
                                    <td>
                                        <p style="font-size:120%;">Nilai Jaminan</p>
                                    </td>
                                    <td>:</td>
                                    <td><b><p style="font-size:120%;"><%=policy.getStCurrencyCode()%> <%=jspUtil.printAutoPrec(policy.getDbInsuredAmount())%></p></b></td>
                                </tr>
                                <tr>
                                    <td>
                                        <p style="font-size:120%;">Tanggal Awal</p>
                                    </td>
                                    <td>:</td>
                                    <td><b><p style="font-size:120%;"><%=jspUtil.print(policy.getDtPeriodStart())%></p></b></td>
                                </tr>
                                <tr >
                                    <td>
                                        <p style="font-size:120%;">Tanggal Akhir</p>
                                    </td>
                                    <td>:</td>
                                    <td><b><p style="font-size:120%;"><%=jspUtil.print(policy.getDtPeriodEnd())%></p></b></td>
                                </tr>
                                <tr>
                                    <td>
                                        <p style="font-size:120%;">Pekerjaan</p>
                                    </td>
                                    <td>:</td>
                                    <td><b><p style="font-size:120%;"><%=policy.getStReference10()%></p></b></td>
                                </tr>
                                <tr>
                                    <td>
                                        <p style="font-size:120%;">Kantor Penerbit Polis</p>
                                    </td>
                                    <td>:</td>
                                    <td><b><p style="font-size:120%;"><%=policy.getStCostCenterDesc()%></p></b></td>
                                </tr>
                                <tr>
                                    <td colspan="3" align="center">
                                        <c:button text="Tutup" event="closeCekPolis" />
                                    </td>
                                </tr>

                                
                            </table>
                        </td>
                    </tr>
                    <br>
                    <br>
                    
                    <%--
                    <tr>
                        <td>
                            <c:listbox name="listPolicyChecking" selectable="true" view="com.webfin.insurance.model.InsurancePolicyView">
                                <%
                                final InsurancePolicyView pol = (InsurancePolicyView) current;
                                %>
                                <c:listcol name="stPolicyID" title="ID" selectid="policyID"/>
                                <c:listcol title="Download" >
                                    <c:button text="Download" name="bpreviewx" clientEvent="dynPreviewClick();" />
                                </c:listcol>
                                <b>
                                    <c:listcol filterable="true" name="stPolicyNo" title="{L-ENGPolicy No-L}{L-INANomor Polis-L}" />
                                </b>

                                <c:listcol filterable="true" name="stPolicyTypeDesc" title="{L-ENGPolicy Type-L}{L-INAJenis Polis-L}" />

                                <c:listcol filterable="true" name="stCustomerName" title="Customer">

                                </c:listcol>

                                <c:listcol filterable="true" name="stReference5" title="Principal"/>
                                <c:listcol filterable="true" name="stReference10" title="Pekerjaan"/>
                                <c:listcol align="right" title="TSI" ><%=JSPUtil.print(pol.isStatusEndorse()?pol.getDbInsuredAmountEndorse():pol.getDbInsuredAmount(),2)%></c:listcol>
                                <c:listcol align="right" title="Premi" ><%=JSPUtil.print(pol.getDbPremiTotal(),2)%></c:listcol>

                            </c:listbox>
                        </td>
                    </tr> --%>
                    <%}%>

                        <tr>
                            <td>
                                <input type="hidden" id="vs" value="UWRIT"/>
                                

                               <%if(false){%>Authorized<%}%> <c:field show="false" name="stAuthorized" type="string" lov="LOV_AUTHORIZED" />
                            </td>
                        </tr>

                        <tr bgcolor="#e2e2e7">
                            <td align="center">
                               <p>Copyright &#169; PT. Asuransi Bangun Askrida - 2016 </p>
                            </td>
                        </tr>
                </table>
            </td>
        </tr>
    </table>
    </body>
    <iframe src="" id=frmx width=1 height=1></iframe>

</c:frame>

<script>
   var frmx = docEl('frmx');

   function getSelectedAttr(c,ref) {
      return c.options[c.selectedIndex].getAttribute(ref);
   }

   

      function dynPreviewClick() {



      if (f.policyID.value=='') {
         alert('Please select a policy');
         return;
      }


      if (true) {
          alert('Data loading .....');
         frmx.src='x.fpc?EVENT=INS_POL_PRV&policyid='+f.policyID.value+'&alter=STANDARD&xlang=INA&fontsize=10pt&authorized='+f.stAuthorized.value+'&attached=1&antic='+(new Date().getTime())+'&preview='+f.bpreviewx.value;
         return;
      }

      openDialog('w.ctl?EVENT=INS_PRT_NOM',400,50,
         function (o) {
            if (o!=null) {
               //alert(o);
            	frmx.src='x.fpc?EVENT=INS_POL_PRV&nom='+o+'&policyid='+f.policyID.value+'&alter='+f.stPrintForm.value+'&xlang='+f.stLang.value+'&fontsize='+f.stFontSize.value+'&authorized='+f.stAuthorized.value+'&attached='+f.stAttached.value+'&antic='+(new Date().getTime());

            }
         }
      );
   }

   

</script>