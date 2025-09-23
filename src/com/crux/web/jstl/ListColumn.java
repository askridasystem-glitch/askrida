/***********************************************************************
 * Module:  com.crux.web.jstl.ListColumn
 * Author:  Denny Mahendra
 * Created: May 19, 2005 12:15:34 PM
 * Purpose:
 ***********************************************************************/

package com.crux.web.jstl;

import com.crux.util.*;
import com.crux.base.BaseClass;
import com.crux.common.model.Filter;

import javax.servlet.jsp.tagext.*;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.math.BigDecimal;

public class ListColumn extends BaseTag implements BodyTag {
   private String name;
   private String title;
   private String flags;
   private boolean flag;
   private String selectid;
   private String align;
   private String filterfield;
   private boolean filterable;
   private boolean sortable;
   private String columnClass;
   private Tag parent;
   private ListBox listBox;

   public boolean isFlag() {
      return flag;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public String getFilterfield() {
      return filterfield;
   }

   public void setFilterfield(String filterfield) {
      this.filterfield = filterfield;
   }

   public boolean isFilterable() {
      return filterable;
   }

   public void setFilterable(boolean filterable) {
      this.filterable = filterable;
   }

   public boolean isSortable() {
      return sortable;
   }

   public void setSortable(boolean sortable) {
      this.sortable = sortable;
   }

   public String getAlign() {
      return align;
   }

   public void setAlign(String align) {
      this.align = align;
   }

   public String getColumnClass() {
      return columnClass;
   }

   public void setColumnClass(String columnClass) {
      this.columnClass = columnClass;
   }

   public void setBodyContent(BodyContent bodyContent) {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public void doInitBody() throws JspException {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public int doAfterBody() throws JspException {
      return 0;  //To change body of implemented methods use File | Settings | File Templates.
   }


   public String getSelectid() {
      return selectid;
   }

   public void setSelectid(String selectid) {
      this.selectid = selectid;
   }

   public void setName(String name) {
      this.name = tlMacro(name);
      //if (filterfield==null) filterfield = this.name;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public void setFlags(String flags) {
      this.flags = flags;
   }

   public void setPageContext(PageContext pageContext) {
      super.setPageContext(pageContext);
      listBox = (ListBox) findInStack(ListBox.class);
   }

   public int startTag() throws JspException {
      try {
         final boolean inheader = listBox.inHeader();
         applyAutoFilter();

         if (columnClass!=null) {
            if ("header".equalsIgnoreCase(columnClass)) {
               if (!inheader) return 0;
            }
            else if ("detail".equalsIgnoreCase(columnClass)) {
               if (inheader) return 0;
            }

            out.print("<td>");
            return EVAL_BODY_INCLUDE;
         }

         out.print("<td"+getAlignScript()+">");

         if (inheader) {
            if (sortable)
               out.print(parentFrame.jspUtil.getSortHeader((DTOList)listBox.collection, title, filterfield));
            else
               out.print(title);
            return 0;
         } else {
            if (name!=null) {

               final BaseClass bc = (BaseClass)listBox.getCurrentObject();

               if (selectid!=null) {
                  if(!parentFrame.getForm().isPropertyEnabled(selectid)) {
                     parentFrame.getForm().enableProperty(selectid);
                     print("<input type=hidden id=\""+selectid+"\" name=\""+selectid+"\">");
                  }

                  print("<input type=radio name=n onclick=\"document.getElementById('"+selectid+"').value='"+bc.getProperty(name)+"';\">");
               } else {
                  if (flag) {
                     if (Tools.isYes((String) bc.getProperty(name)))
                        print("<img src=\""+parentFrame.jspUtil.getImagePath()+"/chk1.gif\">");
                  } else {
                     Object value = bc.getProperty(name);

                     if (value instanceof BigDecimal) {
                        BigDecimal v = (BigDecimal) value;
                        v=v.setScale(2, BigDecimal.ROUND_HALF_UP);
                        value=v;
                     }

                     print(value);
                  }
               }
            }
         }



         return EVAL_BODY_INCLUDE;  //To change body of implemented methods use File | Settings | File Templates.
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   private void applyAutoFilter() {
      final boolean inheader = listBox.inHeader();

      if (inheader)
         if (filterable)
            if (listBox.collection instanceof DTOList) {
               final DTOList l = (DTOList)listBox.collection;

               String ff = filterfield;

               if (ff==null) {
                  final DTOField f = DTOCache.getInstance().findField(listBox.getView(), name);

                  if (f==null) return;

                  ff=f.getStDatabaseFieldName();
               }

               if (ff==null) return;

               final String stPagerName = l.getName();

               final StringBuffer sz = new StringBuffer();

               sz.append("<script>addOpt(document.getElementById('affield" + stPagerName + "'),'" + ff + "','" + title + "');");

               final Filter f = l.getFilter();

               if (f != null && f.afField != null)
                  if (f.afField.equalsIgnoreCase(ff)) {
                     sz.append("\ndocument.getElementById('affield" + stPagerName + "').value='" + ff + "';");
                     sz.append("\ndocument.getElementById('afmode" + stPagerName + "').value='" + l.getFilter().afMode + "';");
                  }

               sz.append("</script>");

               print(sz.toString());
            }
   }

   private String getAlignScript() {

      return align==null?"":(" align="+align);
   }

   private boolean isSkip() {
      final boolean inheader = listBox.getIndex()<0;

      if (columnClass!=null) {
         if ("header".equalsIgnoreCase(columnClass)) {
            if (!inheader) return true;
         }
         else if ("detail".equalsIgnoreCase(columnClass)) {
            if (inheader) return true;
         }
      }

      return false;
   }

   /*public int endTag() throws JspException {
      try {
         listBox.add((ListColumn) this.clone());
         return 0;
      } catch (CloneNotSupportedException e) {
         throw new RuntimeException(e);
      }
   }*/

   public int endTag() throws JspException {

      final boolean inheader = listBox.getIndex()<0;

      if (columnClass!=null) {
         if ("header".equalsIgnoreCase(columnClass)) {
            if (!inheader) return 0;
         }
         else if ("detail".equalsIgnoreCase(columnClass)) {
            if (inheader) return 0;
         }
      }

      try {
         out.print("</td>");
         return 0;
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public void release() {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public String getName() {
      return name;
   }

   public String getTitle() {
      return title;
   }

   public String getFlags() {
      return flags;
   }
}
