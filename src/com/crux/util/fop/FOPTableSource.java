/***********************************************************************
 * Module:  com.crux.util.fop.FOPTableSource
 * Author:  Denny Mahendra
 * Created: Apr 28, 2006 5:41:44 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util.fop;

import com.crux.util.JSPUtil;

import javax.servlet.jsp.JspWriter;
import java.io.Writer;
import java.io.IOException;
import java.math.BigDecimal;

public abstract class FOPTableSource {
   private int columns;
   private int[] columnRatios;
   private double tableWidth;
   private String units;
   private int totalRatio;
   private double[] columnWidths;

   public abstract int getRowCount();

   public abstract Object getColumnValue(int rowNo, int columnNo);

   public abstract String getColumnHeader( int columnNo);

   public String getColumnAlign(int rowNo, int columnNo) {
      return "start";
   }

   protected FOPTableSource(int columns, int [] columnRatios, double tableWidth, String units) {
      this.columns = columns;
      this.columnRatios = columnRatios;
      this.tableWidth = tableWidth;
      this.units = units;

      totalRatio = 0;

      columnWidths = new double [columnRatios.length];

      for (int i = 0; i < columnRatios.length; i++) {
         int columnRatio = columnRatios[i];
         totalRatio+=columnRatio;
      }
      for (int i = 0; i < columnRatios.length; i++) {
         int columnRatio = columnRatios[i];
         columnWidths[i] = tableWidth * (double)columnRatio/(double)totalRatio;
      }
   }

   public void display(JspWriter out) {
      try {
         out.println("<fo:table table-layout=\"auto\" border-collapse=\"separate\">");

         for (int i = 0; i < columnWidths.length; i++) {
            double columnWidth = columnWidths[i];

            out.println("<fo:table-column column-width=\""+columnWidth+units+"\" />");
         }

/*
<fo:table-body   >
           <fo:table-row>
             <fo:table-cell border-width="0.5pt" border-style="solid" display-align="center"><fo:block>01</fo:block></fo:table-cell>
             <fo:table-cell border-width="0.5pt" border-style="solid" display-align="center"><fo:block>REINDO</fo:block></fo:table-cell>
             <fo:table-cell border-width="0.5pt" border-style="solid" display-align="center" ><fo:block start-indent="2pt" end-indent="2pt" space-after.optimum="2pt" space-before.optimum="2pt"   >12381283782 37231232323</fo:block></fo:table-cell>
             <fo:table-cell border-width="0.5pt" border-style="solid" display-align="center" ><fo:block start-indent="2pt" end-indent="2pt" space-after.optimum="2pt" space-before.optimum="2pt">TAG RA;l a;lk;er kqweioqi epoiqp eipqoi epoqiwpoei poipoiqpoei poqiwpe oiqwpoei19028 poqiwpe oiqwpei12p09</fo:block></fo:table-cell>
             <fo:table-cell border-width="0.5pt" border-style="solid" display-align="center"><fo:block>USD 2231112</fo:block></fo:table-cell>
             <fo:table-cell border-width="0.5pt" border-style="solid" display-align="center"><fo:block>IDR 123123123</fo:block></fo:table-cell>
             <fo:table-cell border-width="0.5pt" border-style="solid" display-align="center"><fo:block>askd jlqkjwe ui2i</fo:block></fo:table-cell>
           </fo:table-row>
         </fo:table-body>
*/
         final int rowCount = getRowCount();

         for (int i=-1;i<rowCount;i++) {

            if (i==-1) out.println("<fo:table-header>");
            if (i==0) out.println("<fo:table-body>");

            out.println("<fo:table-row>");
            for (int j=0;j<columnWidths.length;j++) {

               final Object o = getColumnValue2(i, j);
               out.println("<fo:table-cell border-width=\"0.5pt\" border-style=\"solid\" display-align=\"center\">");
               out.println("<fo:block  start-indent=\"2pt\" end-indent=\"2pt\" space-after.optimum=\"2pt\" space-before.optimum=\"2pt\" text-align=\""+getColumnAlign(i,j)+"\">");
               if (o instanceof BigDecimal)
                  out.println(JSPUtil.print((BigDecimal)o,2));
               else
                  out.println(JSPUtil.print(o));
               out.println("</fo:block>");
               out.println("</fo:table-cell>");
            }
            out.println("</fo:table-row>");

            if (i==-1) out.println("</fo:table-header>");
            
            if(rowCount==0&&i==rowCount-1) out.println("<fo:table-body>");
            
            if (i==rowCount-1) out.println("</fo:table-body>");
         }

         out.println("</fo:table>");
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   private Object getColumnValue2(int i, int j) {
      if (i<0) return getColumnHeader(j);
      return getColumnValue(i,j);
   }
}
