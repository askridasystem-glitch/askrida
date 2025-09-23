/***********************************************************************
 * Module:  com.crux.util.SQLAssembler
 * Author:  Denny Mahendra
 * Created: Jul 2, 2004 3:27:28 PM
 * Purpose:
 ***********************************************************************/

package com.crux.util;

import com.crux.common.model.Filter;

import java.util.ArrayList;

public class SQLAssembler {
   private StringBuffer szSelect;
   private StringBuffer szQuery;
   private StringBuffer szClause;
   private StringBuffer szOrder;
   private StringBuffer szGroup;
   private ArrayList par;
   private Filter f;
   public String orderExpression;
   private int limit=-1;
   private String stDS;
   private String stQuery;

   public void addPar(Object x) {
      if (par==null) par = new ArrayList(10);
      par.add(x);
   }

   public ArrayList getPar() {
      if (par==null) par = new ArrayList(10);
      return par;
   }

   public void setPar(ArrayList par) {
      this.par = par;
   }

   public void addSelect(String stmt) {
      if (szSelect == null)
         szSelect = new StringBuffer(64);
      if (szSelect.length() > 0) szSelect.append(",");
      szSelect.append(stmt);
   }

   public void addQuery(String stmt) {
      if (szQuery == null)
         szQuery = new StringBuffer(64);
      szQuery.append(stmt);
   }

   public void addClause(String stmt) {
      if (szClause == null)
         szClause = new StringBuffer(64);
      if (szClause.length()>0) szClause.append(" and ");
      szClause.append(stmt);
   }

   public String getSQL() {
      final StringBuffer sz = new StringBuffer();

      if (stDS != null) sz.append(stDS);

      sz.append(" select ");
      sz.append(szSelect.toString());
      sz.append(' ').append(szQuery.toString());

      if (szClause!=null && szClause.length()>0) sz.append(" where ").append(szClause);

      if (orderExpression != null) sz.append(' ').append(orderExpression);
      
      if (szGroup != null) sz.append(" group by ").append(szGroup.toString());
      if (szOrder != null) sz.append(" order by ").append(szOrder.toString());

      if(limit>=0) sz.append(" limit ").append(limit);

      return sz.toString();
   }

   public void appendOrder(Filter f) {
      orderExpression = ListUtil.getOrderExpression(f);
   }

   public void addOrder(String s) {
      if (szOrder==null)
         szOrder = new StringBuffer();

      if (szOrder.length()>0) szOrder.append(",");

      szOrder.append(s);
   }

   public void addGroup(String s) {
      if (szGroup==null)
         szGroup = new StringBuffer();

      if (szGroup.length()>0) szGroup.append(",");

      szGroup.append(s);
   }

   public void addFilter(Filter f) {
      this.f=f;
      if (szClause==null) szClause=new StringBuffer();
      if (par==null) par=new ArrayList();
      ListUtil.appendAutoFilter(f,szClause,par);
      appendOrder(f);
   }

   public LOV getLOV() throws Exception {
      return ListUtil.getLookUpFromQuery(getSQL(),getPar());
   }

   public DTOList getList(Class dtoClass) throws Exception {
      return ListUtil.getDTOListFromQuery(getSQL(),getPar(),dtoClass,f);
   }

   public LOV getLookUp() throws Exception {
      return ListUtil.getLookUpFromQuery(getSQL(), getPar());

   }

   public boolean hasClause() {
      return szClause!=null && szClause.length()>0;
   }

   public void addParKeyword(Object o) {
      o=o==null?"":o;
      String p = ((String)o).toUpperCase();
      addPar("%"+p+"%");

   }

   public void setLimit(int i) {
      limit = i;
   }

   public void addClauseIN(String fld, String[] values) {

      StringBuffer sz = new StringBuffer();

      sz.append(fld).append(" in (");

      for (int i = 0; i < values.length; i++) {
         String s1 = values[i];
         if (i>0) sz.append(',');
         sz.append("?");
         addPar(s1);
      }

      sz.append(")");

      addClause(sz.toString());
   }

   public void addLike(String str) {
      addPar("%"+str.toUpperCase()+"%");
   }

   public void clearOrder() {
      szOrder.setLength(0);
      orderExpression=null;
   }

    /**
     * @return the stDS
     */
    public String getStDS() {
        return stDS;
    }

    /**
     * @param stDS the stDS to set
     */
    public void setStDS(String stDS) {
        this.stDS = stDS;
    }

    public void setQuery(String stQuery){
        this.stQuery = stQuery;
    }

    public String getSQL2() {
      final StringBuffer sz = new StringBuffer();

      if (stDS != null) sz.append(stDS);

      sz.append(' ').append(stQuery);

      return sz.toString();
   }

    public DTOList getList2(Class dtoClass) throws Exception {
      return ListUtil.getDTOListFromQuery(getSQL2(),getPar(),dtoClass,f);
   }

}

