<%@ page import="com.crux.util.JSPUtil,
                 com.ots.vendor.model.VendorView,
                 com.crux.util.DTOList,
                 java.util.Iterator,
                 com.ots.invoice.model.UOMView,
                 com.ots.invoice.model.CurrencyView,
                 com.ots.item.model.ItemView,
                 java.util.Date"%>
 <%
    JSPUtil jspUtil = new JSPUtil(request, response);
    response.setContentType("application/vnd.ms-excel");
    jspUtil.downloadMode("template-pl"+(new Date().getTime())+".xls");

    VendorView vendor = (VendorView) request.getAttribute("VENDOR");

    DTOList vendorList = new DTOList();
    vendorList.add(vendor);

    DTOList itemList = (DTOList) request.getAttribute("ITEM_LIST");
    DTOList uomList = (DTOList) request.getAttribute("UOM_LIST");
    DTOList ccyList = (DTOList) request.getAttribute("CCY_LIST");

%><?xml version="1.0"?>
<?mso-application progid="Excel.Sheet"?>
<Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet"
 xmlns:o="urn:schemas-microsoft-com:office:office"
 xmlns:x="urn:schemas-microsoft-com:office:excel"
 xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet"
 xmlns:html="http://www.w3.org/TR/REC-html40">
 <DocumentProperties xmlns="urn:schemas-microsoft-com:office:office">
  <Title>Packing List Import Template</Title>
  <Author>Denny Mahendra</Author>
  <LastAuthor>Denny</LastAuthor>
  <Created>2005-06-22T10:12:22Z</Created>
  <LastSaved>2005-06-27T04:37:24Z</LastSaved>
  <Company>Pt. Mitra Integrasi Informatika</Company>
  <Version>11.5606</Version>
 </DocumentProperties>
 <ExcelWorkbook xmlns="urn:schemas-microsoft-com:office:excel">
  <WindowHeight>6795</WindowHeight>
  <WindowWidth>7500</WindowWidth>
  <WindowTopX>120</WindowTopX>
  <WindowTopY>90</WindowTopY>
  <ProtectStructure>False</ProtectStructure>
  <ProtectWindows>False</ProtectWindows>
 </ExcelWorkbook>
 <Styles>
  <Style ss:ID="Default" ss:Name="Normal">
   <Alignment ss:Vertical="Bottom"/>
   <Borders/>
   <Font/>
   <Interior/>
   <NumberFormat/>
   <Protection/>
  </Style>
  <Style ss:ID="m23075228">
   <Alignment ss:Horizontal="Right" ss:Vertical="Bottom"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Interior ss:Color="#FFFF99" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="m23075238">
   <Alignment ss:Horizontal="Right" ss:Vertical="Bottom"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Interior ss:Color="#FFFF99" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="m23075248">
   <Alignment ss:Horizontal="Right" ss:Vertical="Bottom"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Interior ss:Color="#FFFF99" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="m23075258">
   <Alignment ss:Horizontal="Right" ss:Vertical="Bottom"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Interior ss:Color="#FFFF99" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="m23075076">
   <Alignment ss:Horizontal="Right" ss:Vertical="Bottom"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Interior ss:Color="#FFFF99" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="m23075086">
   <Alignment ss:Horizontal="Right" ss:Vertical="Bottom"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Interior ss:Color="#FFFF99" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="m23075096">
   <Alignment ss:Horizontal="Right" ss:Vertical="Bottom"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Interior ss:Color="#FFFF99" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="m23075106">
   <Alignment ss:Horizontal="Right" ss:Vertical="Bottom"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Interior ss:Color="#FFFF99" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="m23093084">
   <Alignment ss:Horizontal="Right" ss:Vertical="Bottom"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Interior ss:Color="#FFFF99" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="m23093094">
   <Alignment ss:Horizontal="Right" ss:Vertical="Bottom"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Interior ss:Color="#FFFF99" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="m23093104">
   <Alignment ss:Horizontal="Right" ss:Vertical="Bottom"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Interior ss:Color="#FFFF99" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="m23093114">
   <Alignment ss:Horizontal="Right" ss:Vertical="Bottom"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Interior ss:Color="#FFFF99" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="s21">
   <Protection ss:Protected="0"/>
  </Style>
  <Style ss:ID="s29">
   <Alignment ss:Horizontal="Right" ss:Vertical="Bottom"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Protection ss:Protected="0"/>
  </Style>
  <Style ss:ID="s32">
   <Alignment ss:Horizontal="Right" ss:Vertical="Bottom"/>
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <NumberFormat ss:Format="Short Date"/>
   <Protection ss:Protected="0"/>
  </Style>
  <Style ss:ID="s33">
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Interior ss:Color="#FFFF99" ss:Pattern="Solid"/>
  </Style>
  <Style ss:ID="s34">
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <Protection ss:Protected="0"/>
  </Style>
  <Style ss:ID="s35">
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <NumberFormat ss:Format="#,##0"/>
   <Protection ss:Protected="0"/>
  </Style>
  <Style ss:ID="s36">
   <Alignment ss:Horizontal="Right" ss:Vertical="Bottom"/>
   <Protection ss:Protected="0"/>
  </Style>
  <Style ss:ID="s37">
   <Borders>
    <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
    <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
   </Borders>
   <NumberFormat ss:Format="Standard"/>
   <Protection ss:Protected="0"/>
  </Style>
 </Styles>
 <Names>
  <NamedRange ss:Name="ccy_code" ss:RefersTo="=PL!R14C3"/>
  <NamedRange ss:Name="ccy_list" ss:RefersTo="=MASTER!R5C10:R<%=ccyList.size()+5%>C10"/>
  <NamedRange ss:Name="contract_number" ss:RefersTo="=PL!R3C3"/>
  <NamedRange ss:Name="invoice_amount" ss:RefersTo="=PL!R13C3"/>
  <NamedRange ss:Name="invoice_date" ss:RefersTo="=PL!R4C3"/>
  <NamedRange ss:Name="invoice_number" ss:RefersTo="=PL!R8C3"/>
  <NamedRange ss:Name="Item_Code" ss:RefersTo="=PL!R17C5:R2301C5"/>
  <NamedRange ss:Name="item_desc" ss:RefersTo="=PL!R17C6:R2301C6"/>
  <NamedRange ss:Name="item_desc_list" ss:RefersTo="=MASTER!R5C6:R<%=itemList.size()+5%>C6"/>
  <NamedRange ss:Name="item_price" ss:RefersTo="=PL!R17C9:R2301C9"/>
  <NamedRange ss:Name="item_qty" ss:RefersTo="=PL!R17C7:R2301C7"/>
  <NamedRange ss:Name="item_uom" ss:RefersTo="=PL!R17C8:R2301C8"/>
  <NamedRange ss:Name="number_of_package" ss:RefersTo="=PL!R10C3"/>
  <NamedRange ss:Name="pl_date" ss:RefersTo="=PL!R7C3"/>
  <NamedRange ss:Name="pl_number" ss:RefersTo="=PL!R6C3"/>
  <NamedRange ss:Name="receive_date" ss:RefersTo="=PL!R5C3"/>
  <NamedRange ss:Name="uom_list" ss:RefersTo="=MASTER!R5C8:R<%=uomList.size()+5%>C8"/>
  <NamedRange ss:Name="vendor_list" ss:RefersTo="=MASTER!R5C3:R12000C3"/>
  <NamedRange ss:Name="vendor_name" ss:RefersTo="=PL!R2C3"/>
  <NamedRange ss:Name="weight_gross" ss:RefersTo="=PL!R11C3"/>
  <NamedRange ss:Name="weight_net" ss:RefersTo="=PL!R12C3"/>
 </Names>
 <Worksheet ss:Name="PL">
  <Table ss:ExpandedColumnCount="9" ss:ExpandedRowCount="1000" x:FullColumns="1"
   x:FullRows="1">
   <Column ss:AutoFitWidth="0" ss:Width="57.75"/>
   <Column ss:StyleID="s21" ss:AutoFitWidth="0" ss:Width="51.75"/>
   <Column ss:StyleID="s21" ss:AutoFitWidth="0" ss:Width="60"/>
   <Column ss:StyleID="s21" ss:AutoFitWidth="0" ss:Width="8.25"/>
   <Column ss:StyleID="s21" ss:Width="49.5"/>
   <Column ss:StyleID="s21" ss:Width="54"/>
   <Row>
    <Cell ss:Index="2" ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
   </Row>
   <Row>
    <Cell ss:MergeAcross="1" ss:StyleID="m23093084"><Data ss:Type="String">Vendor :</Data></Cell>
    <Cell ss:StyleID="s29"><Data ss:Type="String"><%=jspUtil.print(vendor.getStVendorName())%></Data><NamedCell
      ss:Name="vendor_name"/></Cell>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
   </Row>
   <Row>
    <Cell ss:MergeAcross="1" ss:StyleID="m23093094"><Data ss:Type="String">Contract Number :</Data></Cell>
    <Cell ss:StyleID="s29"><NamedCell ss:Name="contract_number"/></Cell>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
   </Row>
   <Row>
    <Cell ss:MergeAcross="1" ss:StyleID="m23093104"><Data ss:Type="String">Invoice Date :</Data></Cell>
    <Cell ss:StyleID="s32"><NamedCell ss:Name="invoice_date"/></Cell>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
   </Row>
   <Row>
    <Cell ss:MergeAcross="1" ss:StyleID="m23093104"><Data ss:Type="String">Receive Date :</Data></Cell>
    <Cell ss:StyleID="s32"><NamedCell ss:Name="receive_date"/></Cell>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
   </Row>
   <Row>
    <Cell ss:MergeAcross="1" ss:StyleID="m23093114"><Data ss:Type="String">Packing List Number :</Data></Cell>
    <Cell ss:StyleID="s29"><NamedCell ss:Name="pl_number"/></Cell>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
   </Row>
   <Row>
    <Cell ss:MergeAcross="1" ss:StyleID="m23075076"><Data ss:Type="String">Packing List Date :</Data></Cell>
    <Cell ss:StyleID="s32"><NamedCell ss:Name="pl_date"/></Cell>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
   </Row>
   <Row>
    <Cell ss:MergeAcross="1" ss:StyleID="m23075086"><Data ss:Type="String">Invoice Number :</Data></Cell>
    <Cell ss:StyleID="s29"><NamedCell ss:Name="invoice_number"/></Cell>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
   </Row>
   <Row>
    <Cell ss:MergeAcross="1" ss:StyleID="m23075096"><Data ss:Type="String"></Data></Cell>
    <Cell ss:StyleID="s32"/>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
   </Row>
   <Row>
    <Cell ss:MergeAcross="1" ss:StyleID="m23075106"><Data ss:Type="String">Number of Package :</Data></Cell>
    <Cell ss:StyleID="s29"><NamedCell ss:Name="number_of_package"/></Cell>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
   </Row>
   <Row>
    <Cell ss:MergeAcross="1" ss:StyleID="m23075228"><Data ss:Type="String">Gross Weight :</Data></Cell>
    <Cell ss:StyleID="s29"><NamedCell ss:Name="weight_gross"/></Cell>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
   </Row>
   <Row>
    <Cell ss:MergeAcross="1" ss:StyleID="m23075238"><Data ss:Type="String">Net Weight :</Data></Cell>
    <Cell ss:StyleID="s29"><NamedCell ss:Name="weight_net"/></Cell>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
   </Row>
   <Row>
    <Cell ss:MergeAcross="1" ss:StyleID="m23075248"><Data ss:Type="String">Invoice Amount :</Data></Cell>
    <Cell ss:StyleID="s29"><NamedCell ss:Name="invoice_amount"/></Cell>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
   </Row>
   <Row>
    <Cell ss:MergeAcross="1" ss:StyleID="m23075258"><Data ss:Type="String">Currency :</Data></Cell>
    <Cell ss:StyleID="s29"><NamedCell ss:Name="ccy_code"/></Cell>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
   </Row>
   <Row>
    <Cell ss:Index="2" ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
    <Cell ss:StyleID="Default"/>
   </Row>
   <Row>
    <Cell ss:Index="5" ss:StyleID="s33"><Data ss:Type="String">Item Code</Data></Cell>
    <Cell ss:StyleID="s33"><Data ss:Type="String">Description</Data></Cell>
    <Cell ss:StyleID="s33"><Data ss:Type="String">Qty</Data></Cell>
    <Cell ss:StyleID="s33"><Data ss:Type="String">UOM</Data></Cell>
    <Cell ss:StyleID="s33"><Data ss:Type="String">Unit Price</Data></Cell>
   </Row>
   <Row>
    <Cell ss:Index="5" ss:StyleID="s29"
     ss:Formula="=IF(RC[1]=&quot;&quot;,&quot;&quot;,VLOOKUP(RC[1],MASTER!R5C6:R10001C7,2,FALSE))"><Data
      ss:Type="String"></Data><NamedCell ss:Name="item_uom"/></Cell>
    <Cell ss:StyleID="s34"><NamedCell ss:Name="item_price"/></Cell>
    <Cell ss:StyleID="s35"/>
    <Cell ss:StyleID="s34"/>
    <Cell ss:StyleID="s37"/>
   </Row>
   <Row>
    <Cell ss:Index="5" ss:StyleID="s29"
     ss:Formula="=IF(RC[1]=&quot;&quot;,&quot;&quot;,VLOOKUP(RC[1],MASTER!R5C6:R10001C7,2,FALSE))"><Data
      ss:Type="String"></Data><NamedCell ss:Name="item_uom"/></Cell>
    <Cell ss:StyleID="s34"><NamedCell ss:Name="item_price"/></Cell>
    <Cell ss:StyleID="s35"/>
    <Cell ss:StyleID="s34"/>
    <Cell ss:StyleID="s37"/>
   </Row>
   <Row>
    <Cell ss:Index="5" ss:StyleID="s29"
     ss:Formula="=IF(RC[1]=&quot;&quot;,&quot;&quot;,VLOOKUP(RC[1],MASTER!R5C6:R10001C7,2,FALSE))"><Data
      ss:Type="String"></Data><NamedCell ss:Name="item_uom"/></Cell>
    <Cell ss:StyleID="s34"><NamedCell ss:Name="item_price"/></Cell>
    <Cell ss:StyleID="s35"/>
    <Cell ss:StyleID="s34"/>
    <Cell ss:StyleID="s37"/>
   </Row>
   <Row>
    <Cell ss:Index="5" ss:StyleID="s29"
     ss:Formula="=IF(RC[1]=&quot;&quot;,&quot;&quot;,VLOOKUP(RC[1],MASTER!R5C6:R10001C7,2,FALSE))"><Data
      ss:Type="String"></Data><NamedCell ss:Name="item_uom"/></Cell>
    <Cell ss:StyleID="s34"><NamedCell ss:Name="item_price"/></Cell>
    <Cell ss:StyleID="s35"/>
    <Cell ss:StyleID="s34"/>
    <Cell ss:StyleID="s37"/>
   </Row>
   <Row>
    <Cell ss:Index="5" ss:StyleID="s29"
     ss:Formula="=IF(RC[1]=&quot;&quot;,&quot;&quot;,VLOOKUP(RC[1],MASTER!R5C6:R10001C7,2,FALSE))"><Data
      ss:Type="String"></Data><NamedCell ss:Name="item_uom"/></Cell>
    <Cell ss:StyleID="s34"><NamedCell ss:Name="item_price"/></Cell>
    <Cell ss:StyleID="s35"/>
    <Cell ss:StyleID="s34"/>
    <Cell ss:StyleID="s37"/>
   </Row>
   <Row>
    <Cell ss:Index="5" ss:StyleID="s29"
     ss:Formula="=IF(RC[1]=&quot;&quot;,&quot;&quot;,VLOOKUP(RC[1],MASTER!R5C6:R10001C7,2,FALSE))"><Data
      ss:Type="String"></Data><NamedCell ss:Name="item_uom"/></Cell>
    <Cell ss:StyleID="s34"><NamedCell ss:Name="item_price"/></Cell>
    <Cell ss:StyleID="s35"/>
    <Cell ss:StyleID="s34"/>
    <Cell ss:StyleID="s37"/>
   </Row>
   <Row>
    <Cell ss:Index="5" ss:StyleID="s29"
     ss:Formula="=IF(RC[1]=&quot;&quot;,&quot;&quot;,VLOOKUP(RC[1],MASTER!R5C6:R10001C7,2,FALSE))"><Data
      ss:Type="String"></Data><NamedCell ss:Name="item_uom"/></Cell>
    <Cell ss:StyleID="s34"><NamedCell ss:Name="item_price"/></Cell>
    <Cell ss:StyleID="s35"/>
    <Cell ss:StyleID="s34"/>
    <Cell ss:StyleID="s37"/>
   </Row>
   <Row>
    <Cell ss:Index="5" ss:StyleID="s29"
     ss:Formula="=IF(RC[1]=&quot;&quot;,&quot;&quot;,VLOOKUP(RC[1],MASTER!R5C6:R10001C7,2,FALSE))"><Data
      ss:Type="String"></Data><NamedCell ss:Name="item_uom"/></Cell>
    <Cell ss:StyleID="s34"><NamedCell ss:Name="item_price"/></Cell>
    <Cell ss:StyleID="s35"/>
    <Cell ss:StyleID="s34"/>
    <Cell ss:StyleID="s37"/>
   </Row>
   <Row>
    <Cell ss:Index="5" ss:StyleID="s29"
     ss:Formula="=IF(RC[1]=&quot;&quot;,&quot;&quot;,VLOOKUP(RC[1],MASTER!R5C6:R10001C7,2,FALSE))"><Data
      ss:Type="String"></Data><NamedCell ss:Name="item_uom"/></Cell>
    <Cell ss:StyleID="s34"><NamedCell ss:Name="item_price"/></Cell>
    <Cell ss:StyleID="s35"/>
    <Cell ss:StyleID="s34"/>
    <Cell ss:StyleID="s37"/>
   </Row>
   <Row>
    <Cell ss:Index="5" ss:StyleID="s29"
     ss:Formula="=IF(RC[1]=&quot;&quot;,&quot;&quot;,VLOOKUP(RC[1],MASTER!R5C6:R10001C7,2,FALSE))"><Data
      ss:Type="String"></Data><NamedCell ss:Name="item_uom"/></Cell>
    <Cell ss:StyleID="s34"><NamedCell ss:Name="item_price"/></Cell>
    <Cell ss:StyleID="s35"/>
    <Cell ss:StyleID="s34"/>
    <Cell ss:StyleID="s37"/>
   </Row>
   <Row>
    <Cell ss:Index="2" ss:StyleID="s36"><NamedCell ss:Name="Item_Code"/></Cell>
   </Row>
   <Row>
    <Cell ss:Index="2" ss:StyleID="s36"><NamedCell ss:Name="Item_Code"/></Cell>
   </Row>
  </Table>
  <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
   <Print>
    <ValidPrinterInfo/>
    <HorizontalResolution>600</HorizontalResolution>
    <VerticalResolution>600</VerticalResolution>
   </Print>
   <Zoom>85</Zoom>
   <Selected/>
   <Panes>
    <Pane>
     <Number>3</Number>
     <ActiveRow>4</ActiveRow>
     <ActiveCol>2</ActiveCol>
    </Pane>
   </Panes>
   <ProtectObjects>False</ProtectObjects>
   <ProtectScenarios>False</ProtectScenarios>
  </WorksheetOptions>
  <DataValidation xmlns="urn:schemas-microsoft-com:office:excel">
   <Range>R2C3</Range>
   <Type>List</Type>
   <Value>vendor_list</Value>
  </DataValidation>
  <DataValidation xmlns="urn:schemas-microsoft-com:office:excel">
   <Range>R26C2:R27C2</Range>
   <Type>List</Type>
   <Value>Item_list</Value>
  </DataValidation>
  <DataValidation xmlns="urn:schemas-microsoft-com:office:excel">
   <Range>R26C5:R27C5,R16C8:R25C8</Range>
   <Type>List</Type>
   <Value>uom_list</Value>
  </DataValidation>
  <DataValidation xmlns="urn:schemas-microsoft-com:office:excel">
   <Range>R14C3</Range>
   <Type>List</Type>
   <Value>ccy_list</Value>
  </DataValidation>
  <DataValidation xmlns="urn:schemas-microsoft-com:office:excel">
   <Range>R16C6:R25C6</Range>
   <Type>List</Type>
   <Value>item_desc_list</Value>
  </DataValidation>
  <DataValidation xmlns="urn:schemas-microsoft-com:office:excel">
   <Range>R16C7:R25C7</Range>
   <Type>Whole</Type>
   <Min>0</Min>
   <Max>100000000</Max>
  </DataValidation>
  <DataValidation xmlns="urn:schemas-microsoft-com:office:excel">
   <Range>R16C9:R25C9</Range>
   <Type>Decimal</Type>
   <Min>0</Min>
   <Max>1000000000000</Max>
  </DataValidation>
 </Worksheet>
 <Worksheet ss:Name="MASTER">
  <Table ss:ExpandedColumnCount="10" ss:ExpandedRowCount="5000" x:FullColumns="1"
   x:FullRows="1">
   <Column ss:Index="3" ss:Width="64.5"/>
   <Column ss:Index="10" ss:Width="70.5"/>
   <Row ss:Index="4">
    <Cell ss:Index="3"><Data ss:Type="String">Vendor Name</Data></Cell>
    <Cell ss:Index="6"><Data ss:Type="String">desc</Data></Cell>
    <Cell><Data ss:Type="String">Item</Data></Cell>
    <Cell><Data ss:Type="String">UOM</Data></Cell>
    <Cell ss:Index="10"><Data ss:Type="String">CCY</Data></Cell>
   </Row>
<%
   Iterator itemit = itemList.iterator();
   Iterator uomIt = uomList.iterator();
   Iterator ccyIt = ccyList.iterator();
   Iterator vendorIt = vendorList.iterator();

   boolean a=true;

   while(a) {
      a=false;

%>
   <Row>
<% if (vendorIt.hasNext()) {
   VendorView vendorView = (VendorView) vendorIt.next();
   a=true;
%>
    <Cell ss:Index="3"><Data ss:Type="String"><%=jspUtil.print(vendorView.getStVendorName())%></Data><NamedCell
      ss:Name="vendor_list"/></Cell>
<% }%>
<% if (itemit.hasNext()) {
   ItemView item = (ItemView) itemit.next();
   a=true;
%>
    <Cell ss:Index="6"><Data ss:Type="String"><%=jspUtil.printesc(item.getStItemDesc())%></Data><NamedCell
      ss:Name="item_desc_list"/></Cell>
    <Cell ss:Index="7"><Data ss:Type="String"><%=jspUtil.printesc(item.getStItemCode())%></Data></Cell>
<% } %>

<%
   if (uomIt.hasNext()) {
      UOMView uomView = (UOMView) uomIt.next();
      a=true;
%>
    <Cell ss:Index="8"><Data ss:Type="String"><%=jspUtil.printesc(uomView.getStUOMCode())%></Data><NamedCell ss:Name="uom_list"/></Cell>
<% } %>

<%
   if (ccyIt.hasNext()) {
      CurrencyView currencyView = (CurrencyView) ccyIt.next();
      a=true;
%>
    <Cell ss:Index="10"><Data ss:Type="String"><%=jspUtil.printesc(currencyView.getStCcyCode())%></Data><NamedCell
      ss:Name="ccy_list"/></Cell>
<% } %>
   </Row>
<% out.flush();
   } %>
  </Table>
  <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
   <Panes>
    <Pane>
     <Number>3</Number>
     <ActiveRow>14</ActiveRow>
     <ActiveCol>8</ActiveCol>
    </Pane>
   </Panes>
   <ProtectObjects>False</ProtectObjects>
   <ProtectScenarios>False</ProtectScenarios>
  </WorksheetOptions>
 </Worksheet>
</Workbook>

