/***********************************************************************
 * Module:  com.crux.web.jstl.BaseTag
 * Author:  Denny Mahendra
 * Created: May 20, 2005 11:11:17 AM
 * Purpose:
 ***********************************************************************/

package com.crux.web.jstl;

import com.crux.util.JSPUtil;
import com.crux.util.LogManager;
import com.crux.util.DTOList;
import com.crux.web.form.Form;
import com.crux.base.BaseClass;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Stack;

public abstract class BaseTag extends BaseClass implements Tag,Serializable,Cloneable {
   protected transient PageContext pc;
   protected transient JspWriter out;
   protected transient com.crux.web.jstl.FrameTag parentFrame;
   protected transient Form form;
   protected transient static ThreadLocal tagStack = new ThreadLocal() {
      protected Object initialValue() {
         return new Stack();
      }
   };
   private transient Stack stack;
   protected DTOList childs;

   public DTOList getChilds() {
      if (childs==null) childs = new DTOList();
      return childs;
   }

   public void setChilds(DTOList childs) {
      this.childs = childs;
   }

   private final static transient LogManager logger = LogManager.getInstance(BaseTag.class);

   public void print(Object o) {
      try {
         out.print(JSPUtil.print(o));
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void setPageContext(PageContext pageContext) {
      stack = (Stack)tagStack.get();
      pc = pageContext;
      out = pc.getOut();
      parentFrame = (com.crux.web.jstl.FrameTag) findInStack(com.crux.web.jstl.FrameTag.class);
//      logger.logDebug("setPageContext: parentFrame = "+parentFrame);
      if (parentFrame != null)
         form = parentFrame.getForm();
      childs=null;
   }

   void push() {
//      logger.logDebug("push: "+this.getClass()+" ("+stack.size()+")");
      stack.push(this);
   }

   protected Object findInStack(Class aClass) {
      final Iterator it = stack.iterator();
      Object a = null;
      while (it.hasNext()) {
         Object o = (Object) it.next();
         if (aClass.isAssignableFrom(o.getClass())) a=o;
      }
      return a;
   }

   public void setParent(Tag tag) {

   }

   public Tag getParent() {
      return null;
   }

   public int startTag() throws JspException {
      return 0;
   }

   public final int doStartTag() throws JspException {
      push();
      return startTag();
   }

   public int endTag() throws JspException {
      return 0;
   }

   public final int doEndTag() throws JspException {
      int c=endTag();
      pop();
      return c;
   }

   public void release() {
   }

   private void pop() {
//      logger.logDebug("pop: ");
      stack.pop();
   }

   public void println(String x) {
      try {
         out.println(x);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public void print(String x) {
      try {
         out.print(x);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }


   public boolean hasChild(Class aClass) {
      if (childs==null) return false;

      for (int i = 0; i < childs.size(); i++) {
         final Object o = childs.get(i);

         if (o==null) continue;

         if (aClass.isAssignableFrom(o.getClass())) return true;
      }

      return false;
   }

   public String tlMacro(String text) {
      if (text==null) return null;
      if (text.indexOf('$')>=0) {

         final StringBuffer sz = new StringBuffer();

         final char[] nm = text.toCharArray();

         int p=-1,p0=-1,p1=-1;

         while (true) {

            p++;
            if (p>=nm.length) break;

            final char c = nm[p];

            switch (c) {
               case '$':
                  if (p0<0) p0=p;
                  else {
                     p1=p;
                     final String vrbl = new String(nm,p0+1,p1-p0-1);
                     //System.out.println("matched variable : "+);
                     p0=-1;p1=-1;
                     sz.append(pc.getAttribute(vrbl));
                  }
                  break;
               default: {
                  if (p0<0) sz.append(c);
               }
            }

         }

         text = sz.toString();
      }

      return text;
   }

   /*public static void main(String [] args) throws Exception {
      System.out.println(tlMacro("12345$gggghhsn.djhs$890"));
   }*/
   public void clearTagStack() {
      stack.clear();
   }

   protected FieldControl getFieldControl() {
      return (FieldControl) findInStack(FieldControl.class);
   }

   protected Boolean getDefaultRO() {
      final FieldControl fc = getFieldControl();

      if (fc!=null) {
         if (fc.isOVReadonly()) {
            return new Boolean(fc.isReadonly());
         }
      }

      if (parentFrame.getForm().isReadOnly())
         return new Boolean(true);

      return null;
   }
}
