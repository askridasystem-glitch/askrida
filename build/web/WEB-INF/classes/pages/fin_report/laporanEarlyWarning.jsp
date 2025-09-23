
<%@page import="java.math.RoundingMode"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="net.sf.jasperreports.engine.type.ModeEnum"%>
<%-- 
    Document   : laporanEarlyWarning
    Created on : 14 Apr 16, 10:50:11
    Author     : fahruzi
--%>

<%@page import="java.awt.Color"%>
<%@page import="com.crux.db.DTOMaps"%>
<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@ page  import="java.io.*"%>
<%@ page  import="java.sql.Connection"%>
<%@ page  import="java.sql.DriverManager"%>
<%@ page  import="java.util.HashMap"%>
<%@ page  import="java.util.Map"%>
<%@ page  import="net.sf.jasperreports.engine.*"%>
<%@page import="net.sf.jasperreports.engine.data.JRBeanCollectionDataSource"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="net.sf.jasperreports.engine.xml.JRXmlLoader"%>
<%@page import="net.sf.jasperreports.engine.design.JasperDesign"%>
<%@page import="net.sf.jasperreports.engine.design.*"%>

<%@page import="com.crux.db.*" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>

        <%
        DTOMaps dtoMaps = (DTOMaps)request.getAttribute("EARLY");
            System.out.print(dtoMaps.get("year") +" <--------------------------------------------- xxxx ");
            AnalisisLaporanKeuangan anal = new AnalisisLaporanKeuangan();
            List<DTOMaps> listKlaim = (List<DTOMaps>)   anal.getKlaim(dtoMaps.getString("year"),dtoMaps.getString("bulan"));
            List<DTOMaps> listDTos = (List<DTOMaps>)    anal.getDataAlasisis(dtoMaps.getString("year"),dtoMaps.getString("bulan"));
            List<DTOMaps> listDataDI = (List<DTOMaps>) anal.getDataDanaIdle(dtoMaps.getString("year"),dtoMaps.getString("bulan"));
            
             List<DTOMaps> listHasil = new ArrayList<DTOMaps>();
             for(DTOMaps dto : listDTos){
                DTOMaps dtoMap = new DTOMaps();
                
                if(!dto.get("id").equals("01")){


                BigDecimal beban_usaha = dto.getBigDecimal("beban_usaha")!=BigDecimal.ZERO?dto.getBigDecimal("beban_usaha"):BigDecimal.ONE;
                BigDecimal premi_bruto = dto.getBigDecimal("premi_bruto")!=BigDecimal.ZERO?dto.getBigDecimal("premi_bruto"):BigDecimal.ONE;

                // RBU
                 BigDecimal rbu = BigDecimal.ZERO;
                if(beban_usaha.doubleValue() != 0 && premi_bruto.doubleValue() != 0 ){
                   rbu = (beban_usaha.divide(premi_bruto , 2, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);
                   System.out.print("rbu "+beban_usaha+" / "+premi_bruto);

                }
                
                //RPP
                BigDecimal rpp1 = dto.getBigDecimal("naikturun_premi");

                //RPP2
                BigDecimal piutang_premi = dto.getBigDecimal("piutang_premi")!=BigDecimal.ZERO?dto.getBigDecimal("piutang_premi"):BigDecimal.ONE;

                 BigDecimal rpp2 = BigDecimal.ZERO;
                if(piutang_premi.doubleValue() != 0 && premi_bruto.doubleValue() != 0 ){
                     rpp2 = (piutang_premi.divide(premi_bruto , 2, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);
                    System.out.print("RPP2 "+piutang_premi+" / "+premi_bruto);
                }
                
                // RTP
                 
                BigDecimal titipan_premi = dto.getBigDecimal("titipan_premi")!=BigDecimal.ZERO?dto.getBigDecimal("titipan_premi"):BigDecimal.ONE;
                BigDecimal rtp = BigDecimal.ZERO;
                if(titipan_premi.doubleValue() != 0 && piutang_premi.doubleValue() != 0 ){
                     rtp = (titipan_premi.divide(piutang_premi , 2, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);
                     System.out.print("RTP "+titipan_premi+" / "+piutang_premi);
                }
                

                // RU
                BigDecimal hasilUw = dto.getBigDecimal("premi_reas").add(dto.getBigDecimal("naikturun_premi")).add(dto.getBigDecimal("premi_bruto")).add(dto.getBigDecimal("klaim_bruto")).add(dto.getBigDecimal("klaim_reas")).add(dto.getBigDecimal("kenaikan_klaim")).add(dto.getBigDecimal("beban_komisi_netto")).add(dto.getBigDecimal("beban_und_lain"));
                BigDecimal ru = BigDecimal.ZERO;
                if(hasilUw.doubleValue() != 0 && premi_bruto.doubleValue() != 0 ){
                  ru   = (hasilUw.divide(premi_bruto , 2, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);
                    System.out.print("RU "+hasilUw+" / "+premi_bruto);
                }
                // RL
                BigDecimal labaUsaha = hasilUw.add(dto.getBigDecimal("investasi")).add(dto.getBigDecimal("beban_usaha"));
                BigDecimal rl = BigDecimal.ZERO;
                if(labaUsaha.doubleValue() != 0 && premi_bruto.doubleValue() != 0 ){
                  rl  = (labaUsaha.divide(premi_bruto , 2, RoundingMode.HALF_UP )).setScale(2, RoundingMode.HALF_UP);
                    System.out.print("RL "+labaUsaha+" / "+premi_bruto);
                }

                //lost ratio
                BigDecimal klaim_bruto = dto.getBigDecimal("klaim_bruto");
                BigDecimal lr = BigDecimal.ZERO;
                if(klaim_bruto.doubleValue() != 0 && premi_bruto.doubleValue() != 0 ){
                    lr =(klaim_bruto.divide(premi_bruto , 2, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP);
                    System.out.print("lost ratio "+klaim_bruto+" / "+premi_bruto);
                }

                dtoMap.put("id", anal.getKdCabang(dto.getString("id")));
                dtoMap.put("rbu", (rbu.doubleValue()*100 ));
                dtoMap.put("rpp1", (rpp1.doubleValue()*100));
                dtoMap.put("rpp2", (rpp2.doubleValue()*100));
                dtoMap.put("rtp",  (rtp.doubleValue()*100));
                dtoMap.put("ru", (ru.doubleValue()*100));
                dtoMap.put("rl", (rl.doubleValue()*100));
                dtoMap.put("lr",(lr.doubleValue()*100));

                // klaim_bruto
                int pk = 0;
                for(DTOMaps dtos : listKlaim){
                    if(dtos.get("kddaerah").equals(dto.get("id"))){
                        pk = dtos.getInt("jmlHari");
                        break;
                    }
                 }
                dtoMap.put("pk", Double.valueOf(pk));

                BigDecimal di = BigDecimal.ZERO;
                 for(DTOMaps dtox : listDataDI){
                     if(dtox.get("kddaerah").equals(dto.get("id"))){
                        di = dtox.getBigDecimal("deposito");
                        break;
                    }
                }
                  System.out.print("Dana Idle "+ di.doubleValue()+" / ");
                 dtoMap.put("di",FunctionCollection.moneyToText(di));
                listHasil.add(dtoMap);
                    }
                 }




          HashMap map = new HashMap();
          
          map.put("p_bulan", FunctionCollection.getNamaBulan(Integer.valueOf(dtoMaps.getString("bulan")),false));
          map.put("p_taon", dtoMaps.getString("year"));

          
          InputStream IS = this.getClass().getClassLoader().getResourceAsStream("fin.war/images/rekapAnalisaKeuangan.jrxml");

          ByteArrayOutputStream baos =new ByteArrayOutputStream();
          response.setContentType("application/pdf");
          JasperDesign design = JRXmlLoader.load(IS);
          JasperReport report = JasperCompileManager.compileReport(design);
          JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listHasil);



          JasperPrint print = JasperFillManager.fillReport(report, map, ds);
          net.sf.jasperreports.engine.JasperExportManager.exportReportToPdfStream(print,baos);

             response.setContentLength(baos.size());
             ServletOutputStream out1 = response.getOutputStream();

            response.setHeader("Content-Disposition", "inline; filename=" +"laporanHarian.pdf");
            response.setHeader("Cache-Control", "cache, must-revalidate");
            response.setHeader("Pragma", "public");



          baos.writeTo(out1);
          out1.flush();



        
           
        %>
	
    </body>

</html>
