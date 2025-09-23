<%@ page import="com.webfin.ar.forms.DepositoReportForm,
com.crux.lov.LOVManager,
com.crux.util.Tools,
java.util.HashMap,
com.crux.lang.LanguageManager,
com.crux.util.StringTools"%>
<%@ taglib prefix="c" uri="crux" %><c:frame>
    <%
    final DepositoReportForm f = (DepositoReportForm) frame.getForm();
    
    final String reporttype = f.getStReportType();
    
    final boolean rptSelected = f.getStReport()!=null;
    
    final HashMap r1 = Tools.getPropMap(LOVManager.getInstance().getRef1("PROD_DEPOSITO",f.getStReport()));
    
    final boolean PFL_DEPOSITO_DATE = StringTools.indexOf(((String) r1.get("DEPOSITO_DATE")),"O")>=0;
    final boolean PFL_CAIR_DATE = StringTools.indexOf(((String) r1.get("CAIR_DATE")),"O")>=0;
    final boolean PFL_BUNGA_DATE = StringTools.indexOf(((String) r1.get("BUNGA_DATE")),"O")>=0;
    final boolean PFL_AWAL_DATE = StringTools.indexOf(((String) r1.get("AWAL_DATE")),"O")>=0;
    final boolean PFL_AKHIR_DATE = StringTools.indexOf(((String) r1.get("AKHIR_DATE")),"O")>=0;
    final boolean PFL_BRANCH = StringTools.indexOf(((String) r1.get("BRANCH")),"O")>=0;
    final boolean PFL_COMP_TYPE = StringTools.indexOf(((String) r1.get("COMP_TYPE")),"O")>=0;
    final boolean PFL_COMP_TYPE2 = StringTools.indexOf(((String) r1.get("COMP_TYPE2")),"O")>=0;
    final boolean PFL_CUSTOMER = StringTools.indexOf(((String) r1.get("CUSTOMER")),"O")>=0;
    final boolean PFL_ACTIVE = StringTools.indexOf(((String) r1.get("ACTIVE")),"O")>=0;
    final boolean PFL_DEPOSITO = StringTools.indexOf(((String) r1.get("DEPOSITO")),"O")>=0;
    
    final boolean PFM_DEPOSITO_DATE = StringTools.indexOf(((String) r1.get("DEPOSITO_DATE")),"M")>=0;
    final boolean PFM_CAIR_DATE = StringTools.indexOf(((String) r1.get("CAIR_DATE")),"M")>=0;
    final boolean PFM_BUNGA_DATE = StringTools.indexOf(((String) r1.get("BUNGA_DATE")),"M")>=0;
    final boolean PFM_AWAL_DATE = StringTools.indexOf(((String) r1.get("AWAL_DATE")),"M")>=0;
    final boolean PFM_AKHIR_DATE = StringTools.indexOf(((String) r1.get("AKHIR_DATE")),"M")>=0;
    final boolean PFM_BRANCH = StringTools.indexOf(((String) r1.get("BRANCH")),"M")>=0;
    final boolean PFM_COMP_TYPE = StringTools.indexOf(((String) r1.get("COMP_TYPE")),"M")>=0;
    final boolean PFM_COMP_TYPE2 = StringTools.indexOf(((String) r1.get("COMP_TYPE2")),"M")>=0;
    final boolean PFM_CUSTOMER = StringTools.indexOf(((String) r1.get("CUSTOMER")),"M")>=0;
    final boolean PFM_ACTIVE = StringTools.indexOf(((String) r1.get("ACTIVE")),"M")>=0;
    final boolean PFM_DEPOSITO = StringTools.indexOf(((String) r1.get("DEPOSITO")),"M")>=0;
    
    final boolean PFM_EXCEL_BUTTON = StringTools.indexOf(((String) r1.get("EXCEL_BUTTON")),"O")>=0;
    final boolean PFM_EXCEL_ONLY = StringTools.indexOf(((String) r1.get("EXCEL_ONLY")),"O")>=0;
    
    boolean cabang = true;
    
    if(f.getStBranch()!=null){
        if(f.getStBranch().equalsIgnoreCase("00")){
            cabang = false;
        }
    }
    
    if(f.getStBranch()==null){
        cabang = false;
    }
    
    final boolean canNavigateBranch = f.isCanNavigateBranch();
    
    final boolean canNavigateRegion = f.isCanNavigateRegion();
    
    boolean bpdReadOnly = false;
    String bpd = "Y";
    if(f.getStReceiptClassID()!=null)
        if(f.getStReceiptClassID().equalsIgnoreCase("3")){
        bpdReadOnly = true;
        bpd = "N";
        }
    
    String method = "Y";
    
    %>
    
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                <c:field name="stEntityID" type="string" hidden="true"/>
            </td>
        </tr>
    </table>
    
    <table cellpadding=2 cellspacing=1>
        <tr>
            <td>
                Print Report<c:field width="150" name="stPrintForm" type="string" lov="VS_PROD_DEPOSITO" readonly="<%=!f.isEnableSelectForm()%>"  changeaction="chgform" /> in <c:field name="stLang" type="string" value="<%=LanguageManager.getInstance().getActiveLang()%>" lov="LOV_LANG" />
                <br>
                <br>
            </td>
        </tr>
        
        <tr>
            <td>
                <table cellpadding=2 cellspacing=1>
                    <c:field name="stReportType" show="false" type="string" width="200" mandatory="false" caption="Report Type" presentation="standard" />
                    <c:field width="450" changeaction="onChangeReport" caption="Report" lov="LOV_DEPOSITOREPORT" name="stReport"  descfield="stReportDesc" type="string" presentation="standard" >
                        <c:lovLink name="reporttype" link="stReportType" clientLink="false" />
                    </c:field>
                    <c:evaluate when="<%=rptSelected%>" >
                        <c:evaluate when="<%=PFL_DEPOSITO_DATE%>" >      
                            <tr>
                                <td>Tanggal Pendebetan</td>
                                <td>:</td>
                                <td>
                                    <c:field name="DepoDateFrom" type="date" caption="Pembentukan From"  />
                                    To <c:field name="DepoDateTo" type="date" caption="Pembentukan To" />
                                </td>
                            </tr>
                        </c:evaluate>    
                        <c:evaluate when="<%=PFL_CAIR_DATE%>" >      
                            <tr>
                                <td>Tanggal Pencairan</td>
                                <td>:</td>
                                <td>
                                    <c:field name="CairDateFrom" type="date" caption="Pencairan From"  />
                                    To <c:field name="CairDateTo" type="date" caption="Pencairan To" />
                                </td>
                            </tr>
                        </c:evaluate>    
                        <c:evaluate when="<%=PFL_BUNGA_DATE%>" >      
                            <tr>
                                <td>Tanggal Bunga</td>
                                <td>:</td>
                                <td>
                                    <c:field name="BungaDateFrom" type="date" caption="Bunga From"  />
                                    To <c:field name="BungaDateTo" type="date" caption="Bunga To" />
                                </td>
                            </tr>
                        </c:evaluate>
                        <c:evaluate when="<%=PFL_AWAL_DATE%>" >      
                            <tr>
                                <td>Deposito s/d Tanggal</td>
                                <td>:</td>
                                <td>
                                    <c:field name="dtTglAwalFrom" type="date" caption="Deposito s/d Tanggal"  />
                                </td>
                            </tr>
                        </c:evaluate>    
                        <c:evaluate when="<%=PFL_AKHIR_DATE%>" >      
                            <tr>
                                <td>Jatuh Tempo per Bulan</td>
                                <td>:</td>
                                <td>
                                    <c:field name="dtTglAkhirFrom" type="date" caption="Jatuh Tempo per Bulan"  />
                                </td>
                            </tr>
                        </c:evaluate>        
                        <c:evaluate when="<%=PFL_BRANCH%>" >   
                            <tr>
                                <td>Cabang</td>
                                <td>:</td>
                                <td>
                                    <%--<c:field width="200" caption="Branch" lov="LOV_Branch" name="stBranch" descfield="stBranchDesc" readonly="<%=!canNavigateBranch%>" type="string" presentation="standard" loveall="true"/>--%>
                                    <c:field width="200" caption="Branch" lov="LOV_Branch" name="stBranch" descfield="stBranchDesc" readonly="<%=cabang%>" type="string" mandatory="false"/>
                                </td>
                            </tr>
                        </c:evaluate>  
                        <c:evaluate when="<%=PFL_COMP_TYPE%>" >   
                            <tr>
                                <td>
                                    <c:field type="string" name="stCompTypeID" descfield="stCompTypeName" width="200" popuplov="true"  lov="LOV_CompType" mandatory="false" caption="Company Type" presentation="standard" />
                                </td>
                            </tr>
                        </c:evaluate>   
                        <c:evaluate when="<%=PFL_DEPOSITO%>" >   
                            <tr>
                                <td>
                                    <c:field type="string" name="stStatusID" descfield="stStatusName" width="200" lov="LOV_DEPOSITO2" mandatory="false" caption="Status" presentation="standard" />
                                </td>
                            </tr>
                        </c:evaluate>   
                        <c:evaluate when="<%=PFL_COMP_TYPE2%>" >
                            <tr>
                                <td>
                                    <c:field lov="LOV_ReceiptClass" width="230" name="stReceiptClassID" caption="{L-ENGMethod-L}{L-INAMetode-L}" changeaction="refresh"
                                             type="string" presentation="standard" >
                                        <c:param name="custcat" value="<%=method%>"  />
                                    </c:field>
                                </td>
                            </tr>
                            <c:evaluate when="<%=f.getStReceiptClassID()!=null%>">
                                <tr>
                                    <td>
                                        <c:field lov="LOV_CompType" width="230" name="stCompTypeID" descfield="stCompTypeName" caption="{L-ENGCompany Type-L}{L-INAJenis Perusahaan-L}" type="string" presentation="standard" >
                                            <c:param name="custcatdep" value="<%=bpd%>"  />
                                        </c:field>
                                    </td>
                                </tr>
                            </c:evaluate> 
                        </c:evaluate>  
                        <%--
                        <c:evaluate when="<%=PFL_CUSTOMER%>" >
                            <c:field lov="LOV_AccountInvestment" width="230" name="stEntityID" descfield="stEntityName" caption="Bank" type="string" presentation="standard" popuplov="true" >
                                <c:param name="bank" value="<%=f.getStBranch()%>"/>
                            </c:field>
                        </c:evaluate>  
                        --%>
                        <c:evaluate when="<%=PFL_CUSTOMER%>">
                            <tr>
                                <td>Account</td>
                                <td>:</td>
                                <td>
                                    <c:field name="stEntityName" type="string" width="200"/><c:button text="..." clientEvent="selectAccountByBranch();" validate="false" enabled="true"/>
                                </td>
                            </tr>
                            <tr>
                                <td>Bank</td>
                                <td>:</td>
                                <td>
                                    <c:field name="stDescription" type="string" width="200"/>
                                </td>
                            </tr>
                        </c:evaluate>
                        <c:evaluate when="<%=PFL_ACTIVE%>" >
                            <tr>
                                <td>
                                    <c:field caption="Deposito Active" name="stActiveFlag" type="check" presentation="standard" />
                                </td>
                            </tr>
                        </c:evaluate>
                    </c:evaluate>
                </table>
            </td>
        </tr>
        
        <tr>
            <td align="center" >
                <br>
                <br>
                <c:evaluate when="<%=!PFM_EXCEL_ONLY%>" >
                    <c:button text="Print" name="bprintx"  event="clickPrint"  validate="true" />
                </c:evaluate>
                <c:evaluate when="<%=PFM_EXCEL_BUTTON%>" >
                    <c:button text="EXPORT TO Excel" name="excel"  event="clickPrintExcel" />
                </c:evaluate>
            </td>
        </tr>
    </table>
    <iframe src="" id=frmx width=1 height=1></iframe>
</c:frame>

<script>
   
function selectAccountByBranch(o){
   		
   	var cabang = document.getElementById('stBranch').value;
        
   	openDialog('so.ctl?EVENT=GL_ACCOUNTS_SELECT3&costcenter='+cabang+'',600,400,selectAccountsCashBank);
   		
   }
   
   function selectAccountsCashBank(o) {
      if (o==null) return;
      document.getElementById('stEntityID').value=o.acid;
      document.getElementById('stEntityName').value=o.acno;
      document.getElementById('stDescription').value=o.desc;
   }
</script>