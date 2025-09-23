/***********************************************************************
 * Module:  com.crux.web.jstl.ListBox
 * Author:  Denny Mahendra
 * Created: May 19, 2005 10:38:32 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.web.jstl;

import com.crux.util.DTOList;
import com.crux.util.LogManager;
import com.crux.common.model.Filter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;
import java.util.ArrayList;
import java.util.List;

public class ListBox extends BaseTag implements IterationTag {
   private ArrayList columns = new ArrayList();

   private String name;
   private String view;
   private boolean autofilter;
   private String paging;
   private boolean pagingEnabled = false;
   private int rows = -1;
   private String title;
   private String flags;
   private boolean selectable;
   private String filter;
   private int currentIndex=-1;
   public List collection;
   private Class viewClass;

   public String getView() {
      return view;
   }

   public void setView(String view) {
      this.view = view;
   }

   private final static transient LogManager logger = LogManager.getInstance(ListBox.class);
   private boolean inheader;
   private Filter listFilter;

   public Filter getListFilter() {
      return listFilter;
   }

   public void setListFilter(Filter listFilter) {
      this.listFilter = listFilter;
   }

   public boolean isAutofilter() {
      return autofilter;
   }

   public void setAutofilter(boolean autofilter) {
      this.autofilter = autofilter;
   }

   public void setFilter(String filter) {
      this.filter = filter;
   }

   public void setSelectable(boolean selectable) {
      this.selectable = selectable;
   }

   public int startTag() throws JspException {
      try {
         columns.clear();
         collection = (List)form.getProperty(name);
         DTOList l = null;
         if (collection instanceof DTOList)  {
            l = (DTOList) collection;
            //l.setName(name);
         }

         /*if (l!=null) {
            if (filter) {
               print(parentFrame.jspUtil.getAutoFilter(l));
            }

            if (l.getFilter()!=null) {
               print(parentFrame.jspUtil.getPager(l));
            }
         }*/

         ListColumn lcSelect=null;


         /*for (int i = 0; i < columns.size(); i++) {
            ListColumn lc = (ListColumn) columns.get(i);

            if (lc.getSelectid()!=null) {
               print("<input type=hidden name=\""+lc.getSelectid()+"\">");
               parentFrame.getForm().enableProperty(lc.getSelectid());
               lcSelect = lc;
               break;
            }
         }*/
         currentIndex=-1;

         if (pagingEnabled) {
            if (l.getFilter()==null) {
               final Filter f = new Filter();
               f.activate();
               l.setFilter(f);
            }
            l.getFilter().activate();
            if (autofilter) {
               out.print(parentFrame.jspUtil.getAutoFilter(l));
               out.print("<br>");
            }

            form.setAttribute("CachedList"+name,l);

            listFilter = l.getFilter();

            out.print(parentFrame.jspUtil.getPager(l));
            if (l.size()>l.getFilter().rowPerPage)
               currentIndex += l.getFilter().getStartRow();
            //logger.logDebug("startTag: currentIndex = "+currentIndex); mark logger
            rows = l.getFilter().rowPerPage+1;
            out.print("<script>");
            out.print("function changePage"+l.getName()+"() { mform.defaultArg1.value='"+(filter==null?name:filter)+"';mform.defaultArg2.value='"+l.getName()+"'; mform.action_event.value='sysFormChangeLiztPage'; mform.submit();}");
            out.print("</script>");
         }
         out.print("<table id="+name+" border=0 cellpadding=2 cellspacing=1>");
         out.print("<tr class=header>");
         /*if (selectable) {
            print("<td></td>");
         }
         for (int i = 0; i < columns.size(); i++) {
            ListColumn lc = (ListColumn) columns.get(i);
            out.print("<td>"+lc.getTitle()+"</td>");
         }*/

         inheader = true;

         pc.setAttribute("index",new Integer(currentIndex));
         pc.removeAttribute("current");

         /*if (collection instanceof DTOList)  {
            DTOList modelList = (DTOList) collection;

            for (int i = 0; i < modelList.size(); i++) {
               BaseClass bc = (BaseClass) modelList.get(i);
               modelList.setCurrentObject(bc);

               print("<tr class=row"+(i%2)+">");

               if (selectable) {
                  print("<td><input type=radio name=n onclick=\"document.getElementById('"+lcSelect.getSelectid()+"').value='"+bc.getProperty(lcSelect.getName())+"';\"></td>");
               }

               for (int j = 0; j < columns.size(); j++) {
                  ListColumn lc = (ListColumn) columns.get(j);
                  print("<td>");
                  print(bc.getProperty(lc.getName()));
                  print("</td>");
               }

               print("</tr>");
            }
         }*/

         //out.print("</tr>");
         return EVAL_BODY_INCLUDE;
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void release() {

   }

   public void add(ListColumn lc) {
      columns.add(lc);
   }

   public void setName(String name) {
      this.name = tlMacro(name);
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public void setFlags(String flags) {
      this.flags = flags;
   }

   public Object getCurrentObject() {
      return collection.get(currentIndex);
   }

   public int getIndex() {
      return currentIndex;
   }

   public boolean inHeader() {
      return inheader;
   }

   public Class getViewClass() {
      try {
         if (viewClass==null)
         viewClass = Class.forName(view);
         return viewClass;
      } catch (ClassNotFoundException e) {
         throw new RuntimeException(e);
      }
   }

   public static class ListBoxTEI extends TagExtraInfo {
      public VariableInfo[] getVariableInfo(TagData tagData) {
         return new VariableInfo [] {
            new VariableInfo("index","java.lang.Integer",true,VariableInfo.NESTED),
            new VariableInfo("current","java.lang.Object",true,VariableInfo.NESTED),
         };
      }
   }

   public int doAfterBody() throws JspException {
      try {
         inheader = false;
         out.print("</tr>");
         currentIndex++;
         pc.setAttribute("index",new Integer(currentIndex));
         if (currentIndex>=0)
            if (collection!=null) {
               final Object n = collection.get(currentIndex);
               if (n!=null)
                  pc.setAttribute("current",n); 
            }
         if(collection instanceof DTOList) {
            ((DTOList)collection).setCurrentIndex(currentIndex);
         }

         if (rows>0) rows--;

         if (rows!=0)
            if (currentIndex<collection.size()) {
               out.print("<tr class=\"row"+(currentIndex%2)+"\">");
               return EVAL_BODY_AGAIN;  //To change body of implemented methods use File | Settings | File Templates.
            }

         out.print("</table>");

         return 0;
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public String getPaging() {
      return paging;
   }

   public void setPaging(String paging) {
      this.paging = paging;
      pagingEnabled = true;
   }
}
