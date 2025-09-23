/***********************************************************************
 * Module:  com.crux.util.DTOList
 * Author:  Denny Mahendra
 * Created: Mar 9, 2004 11:09:28 AM
 * Purpose:
 ***********************************************************************/

package com.crux.util;

import com.crux.base.BaseClass;
import com.crux.common.config.Config;
import com.crux.common.exception.RTException;
import com.crux.common.model.DTO;
import com.crux.common.model.Filter;
import com.crux.common.model.HashDTO;

import java.io.Serializable;
import java.util.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;

public class DTOList extends BaseClass implements List, LOV, Comparator, Cloneable, Serializable {
   private final static transient LogManager logger = LogManager.getInstance(DTOList.class);

   private DTOList deleted = null;
   private transient ArrayList searchFields = null;
   private int searchpt = 0;
   private transient Object searchobj = null;

   private int rowPerPage = Config.ROW_PER_PAGE;
   private int currentPage = 0;
   private int totalRows = -1;
   private ArrayList l = new ArrayList();

   private String orderKey = null;
   private int orderDir = 1;
   private boolean comboNullable = true;

   private SQLUtil sqlUtil;
   private ResultSetIterator resultSetIterator;
   private int rsIndex = 0;
   public String comboValue;
   private Object currentObject;

   Filter filter;
   private HashMap mapOfMap;

   public DTOField pkField = null;
   private DTO selected = null;
   private int currentIndex;
   private String nullText;

   public HashMap getMapOf(KeyBuilder key) {
      final HashMap m = new HashMap();

      for (int i = 0; i < this.size(); i++) {
         DTO dto = (DTO) this.get(i);

         m.put(key.buildKey(dto),dto);
      }

      return m;
   }

   public Object getTotal(String s) {

      BigDecimal x = null;

      for (int i = 0; i < this.size(); i++) {
         DTO dto = (DTO) this.get(i);

         x = BDUtil.add((BigDecimal) dto.getFieldValueByFieldName(s),x);
      }

      return x;
   }

   public interface KeyBuilder {
      public String buildKey(DTO x);
   }

   public boolean addAll(Collection collection) {
      if (collection==null) return false;
      clearMaps();
      return l.addAll(collection);    //To change body of overridden methods use File | Settings | File Templates.
   }

   private Object getFieldValueByFieldName(DTO dto, int i, String k) {
      return "index".equalsIgnoreCase(k)?String.valueOf(i):dto.getFieldValueByFieldName(k);
   }

   public Filter getFilter() {
      if (filter==null) {
         filter = new Filter();
         filter.activate();
      }
      return filter;
   }

   public void setFilter(Filter filter) {
      this.filter = filter;
   }

   public void sort() {
      Collections.sort(this,this);
   }

   public int compare(Object o1, Object o2) {
      try {
         final DTO d1 = (DTO)o1;
         final DTO d2 = (DTO)o2;

         return
                 Tools.compare(
                         (Comparable)d1.getFieldValueByFieldName(orderKey),
                         (Comparable)d2.getFieldValueByFieldName(orderKey)
                 )*orderDir;

      } catch (Exception e) {
         throw new RTException(e);
      }
   }

   public void release() {
      try {

         if (resultSetIterator != null) {
            resultSetIterator.release();
            resultSetIterator = null;
         }

         if (sqlUtil != null) {
            sqlUtil.release();
            sqlUtil = null;
         }

      } catch (Exception e) {
         e.printStackTrace();
         logger.logError("error while releasing : " + e.getMessage() + " (ignoring)");
      }
   }

   public boolean isComboNullable() {
      return comboNullable;
   }

   public void setComboNullable(boolean comboNullable) {
      this.comboNullable = comboNullable;
   }

   public String getOrderKey() {
      return orderKey;
   }

   public void setOrderKey(String orderKey) {
      this.orderKey = orderKey;
   }

   public int getOrderDir() {
      return orderDir;
   }

   public void setOrderDir(int orderDir) {
      this.orderDir = orderDir;
   }

   private HashMap attributes;

   public HashMap getAttributes() {
      return attributes;
   }

   public void setAttributes(HashMap attributes) {
      this.attributes = attributes;
   }

   public void setAttribute(String key, Object o) {
      if (attributes == null) attributes = new HashMap();
      attributes.put(key, o);
   }

   public Object getAttribute(String stKey) {
      if (attributes == null) return null;
      return attributes.get(stKey);
   }

   private String Name;

   public int getTotalRows() {
      return totalRows;
   }

   public void setTotalRows(int totalRows) {
      this.totalRows = totalRows;
   }

   public int getRowPerPage() {
      if (getFilter()==null) return -1;
      return getFilter().rowPerPage;
   }

   public void setRowPerPage(int rowPerPage) {
      getFilter().rowPerPage = rowPerPage;
   }

   public int getCurrentPage() {
      if (getFilter()==null) return -1;
      return getFilter().getCurrentPage();
   }

   public void setCurrentPage(int currentPage) {
      getFilter().setCurrentPage(currentPage);
   }

   /*public void setCurrentRow(int row) {
      this.currentPage = row / rowPerPage;
   }*/

   public int getStartRow() {
      if (getFilter()==null) return -1;
      return getFilter().getStartRow();
   }

   public String getComboDesc(String stValue) {

      try {
         if (stValue==null) return null;

         String[] comboFields = null;

         for (int i = 0; i < this.size(); i++) {
            DTO dto = (DTO) this.get(i);

            if (comboFields == null)
               if (dto != null) {
                  comboFields = (String[]) dto.getClass().getField("comboFields").get(null);
               }

            final String comboID = (String) getFieldValueByFieldName(dto, i, comboFields[0]);

            if (Tools.isEqual(comboID, stValue)) {
               return (String) dto.getFieldValueByFieldName(comboFields[1]);
            }
         }

         return null;

         //throw new RuntimeException("Value not found : " + stValue);
      } catch (IllegalAccessException e) {
         throw new RuntimeException(e);
      } catch (NoSuchFieldException e) {
         throw new RuntimeException(e);
      }
   }

   public LOV setLOValue(String stValue) {
      comboValue = stValue;
      return this;
   }

   public String getLOValue() {
      return comboValue;
   }

   public String getComboContent() throws Exception {
      return getComboContent(comboValue);
   }

   public String getComboDesc() throws Exception {
      return getComboDesc(comboValue);
   }

   public void combineDeleted() {
      if (deleted!=null) {
         addAll(deleted);
         deleted=null;
      }
   }

   public LOV setNoNull() {
      comboNullable = false;
      return this;
   }

   public LOV setNullText(String s) {
      nullText = s;
      return this;
   }

   public void sort(Filter filter) {

      if (filter.orderDir==orderDir &&
              Tools.isEqual(filter.orderKey, orderKey)
              ) return;

      orderKey = filter.orderKey;
      orderDir = filter.orderDir;

      sort();
   }

   public HashDTO getHashDTO() {
      return (HashDTO) getDTO();
   }

   public class CodeIterator implements Iterator {
      private int index;
      private String[] comboFields;

      public CodeIterator() throws Exception {
         index = 0;

         if (size()<1) return;

         final DTO firstDTO = (DTO)get(0);

         if (firstDTO==null) return;

         final Field cf = firstDTO.getClass().getField("comboFields");

         comboFields = (String [] )cf.get(null);
      }

      public void remove() {
         //To change body of implemented methods use File | Settings | File Templates.
      }

      public boolean hasNext() {
         return index<=size()-1;
      }

      public Object next() {
         final DTO d = (DTO)get(index);
         index++;
         return (String) d.getFieldValueByFieldName(comboFields[0]);
      }
   }

   public Iterator getCodeIterator() {
      try {
         return new CodeIterator();
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public Iterator getIterator() {
      return iterator();
   }

   public String getJSObject() throws Exception {
      String[] comboFields = null;

      final StringBuffer sb = new StringBuffer();

      sb.append('[');

      if (comboNullable)
         sb.append("{text:\"[select one]\",value:\"\"}");

      for (int i = 0; i < this.size(); i++) {
         DTO dto = (DTO) this.get(i);
         if (comboFields == null)
            if (dto != null) {
               comboFields = (String[]) dto.getClass().getField("comboFields").get(null);
            }

         final String comboID = (String) dto.getFieldValueByFieldName(comboFields[0]);
         final String comboValue = (String) dto.getFieldValueByFieldName(comboFields[1]);

         if (sb.length()>1) sb.append(",\n");

         sb.append("{text:'"+JSPUtil.jsEscape(comboID)+"',value:'"+JSPUtil.jsEscape(comboValue)+"'");

         for (int j = 2; j < comboFields.length; j++) {
            sb.append(',');
            sb.append(comboFields[j]).append(":\'");
            final Object p = dto.getFieldValueByFieldName(comboFields[j]);
            sb.append(p==null?"":JSPUtil.jsEscape((String)p));
            sb.append('\'');
         }

         sb.append('}');
      }

      sb.append(']');

      return sb.toString();
   }

   public String[] getAttributeNames() {
      try {
         if (size()<1) return new String [] {};
         final DTO dto = (DTO)this.get(0);
         String[] comboFields = (String[]) dto.getClass().getField("comboFields").get(null);

         return comboFields;

         /*if (comboFields.length>2) {
            final String[] names = new String [comboFields.length-2];

            System.arraycopy(comboFields,2,names,0,names.length);

            return names;
         }

         return new String [] {};*/
      } catch (IllegalAccessException e) {
         throw new RuntimeException(e);
      } catch (NoSuchFieldException e) {
         throw new RuntimeException(e);
      }
   }

   public String getComboContent(String stDefaultValue) throws Exception {
      String [] comboFields = null;

      final StringBuffer sb = new StringBuffer();

      if (stDefaultValue == null) {
         if (selected!=null) {
            if (comboFields == null)
               if (selected != null) {
                  comboFields = (String []) selected.getClass().getField("comboFields").get(null);
               }
            int n = indexOf(selected);
            stDefaultValue = String.valueOf(getFieldValueByFieldName(selected, n, comboFields[0]));
         }
      }

      if (comboNullable)
         sb.append("<OPTION VALUE=\"\">[").append(nullText==null?"select one":nullText).append("]</OPTION>");

      for (int i = 0; i < this.size(); i++) {
         DTO dto = (DTO) this.get(i);
         if (comboFields == null)
            if (dto != null) {
               comboFields = (String []) dto.getClass().getField("comboFields").get(null);
            }

         final String comboID = (String) getFieldValueByFieldName(dto, i, comboFields[0]);
         String comboValue = (String) getFieldValueByFieldName(dto, i, comboFields[1]);
         if (comboValue==null) comboValue="";

         sb.append("<OPTION VALUE=\"").append(comboID+"\"").append(Tools.isEqual(comboID,stDefaultValue)?" SELECTED":"");

         for (int j=2;j<comboFields.length;j++) {
            final String k = comboFields[j];
            sb.append(' ').append(k).append("=\"");

            sb.append(getFieldValueByFieldName(dto, i,k));

            sb.append('"');
         }

         sb.append(">"+comboValue+"</OPTION>");
      }

      return sb.toString();
   }

   public void delete(int n) {
      final DTO dto = (DTO) get(n);

      if (dto.isDelete()) return;

      if (dto.isNew()) {
         remove(n);
         return;
      }

      remove(n);

      dto.markDelete();

      if (deleted == null) deleted = new DTOList();

      deleted.add(dto);
   }

   public DTOList getDeleted() {
      return deleted;
   }

   public void keepSelected() {
      for (int i = this.size() - 1; i >= 0; i--) {
         DTO o = (DTO) this.get(i);
         if (!o.isSelected())
            remove(i);
      }
   }

   public int find(String stFieldName, Object o) throws Exception {
      if (size() < 1) return -1;
      final DTO key = (DTO) (get(0)).getClass().newInstance();
      final HashMap fields = DTOCache.getInstance().getFields(key.getClass());
      final DTOField df = (DTOField) fields.get(stFieldName.toLowerCase());
      df.getSetter().invoke(key, new Object[]{o});
      return find(key);
   }

   public int find(DTO key) throws Exception {
      if (searchFields == null) searchFields = new ArrayList();
      searchFields.clear();
      searchpt = 0;

      final HashMap fields = DTOCache.getInstance().getFields(key.getClass());

      final Iterator keys = fields.values().iterator();

      while (keys.hasNext()) {
         DTOField df = (DTOField) keys.next();

         final Object o = df.getGetter().invoke(key, null);

         if (o != null) { // a key is active
            searchFields.add(df);
         }
      }

      searchobj = key;

      return findNext();
   }

   public void findReset() {
      searchpt = 0;
   }

   public int findNext() throws Exception {
      if (searchpt >= this.size()) return -1;

      boolean found = false;

      for (int i = searchpt; i < this.size(); i++) {
         DTO dto = (DTO) this.get(i);

         found = true;

         for (int j = 0; j < searchFields.size(); j++) {
            DTOField df = (DTOField) searchFields.get(j);

            if (!Tools.isEqual((Comparable) df.getGetter().invoke(dto, null), (Comparable) df.getGetter().invoke(searchobj, null))) {
               found = false;
               break;
            }
         }

         if (found) {
            searchpt = i + 1;
            return i;
         }
      }

      searchpt = this.size();
      return -1;
   }

   public int getEndRow() {
      if (getStartRow() + rowPerPage > size()) return size();
      return getStartRow() + rowPerPage;
   }

   public String getName() {
      return Name;
   }

   public void setName(String name) {
      Name = name;
   }

   public DTOList(String name) {
      Name = name;
   }

   public DTOList() {
      this("List");
   }

   /*public Object get(int index) {
      if (resultSetIterator==null) return super.get(index);

      if (index<rsIndex) return super.get(index);

      if (index>rsIndex) throw new RuntimeException("random access read not yet supported");

      if (resultSetIterator.hasNext())
         add(resultSetIterator.next());
      else {
         release();
      }

      return super.get(index);
   }*/

   public boolean add(Object o) {
      if (o == null) throw new RuntimeException("null values are not accepted");
      if (resultSetIterator != null) throw new RuntimeException("Operation not supported");
      clearMaps();
      return l.add(o);
   }

   private void clearMaps() {
      mapOfMap=null;
   }

   public Object get(int index) {
      if (index<0) return null;

      if (index>=size()) return null;

      if (resultSetIterator == null) return l.get(index);

      if (index < rsIndex) throw new RuntimeException("random access read not yet supported");

      if (index > rsIndex) throw new RuntimeException("random access read not yet supported");

      if (resultSetIterator.hasNext()) {
         rsIndex++;
         return resultSetIterator.next();
      } else {
         release();
         throw new RuntimeException("no more records");
      }
   }

   public ResultSetIterator getResultSetIterator() {
      return resultSetIterator;
   }

   public void setResultSetIterator(ResultSetIterator resultSetIterator) {
      this.resultSetIterator = resultSetIterator;
   }

   public SQLUtil getSqlUtil() {
      return sqlUtil;
   }

   public void setSqlUtil(SQLUtil sqlUtil) {
      this.sqlUtil = sqlUtil;
   }

   public static DTOList getDTOListFromProxy(DTOList x) throws Exception {
      if (x instanceof QueryProxy) return ((QueryProxy) x).getDTOList();
      return x;
   }

   public Iterator iterator() {
      if (resultSetIterator != null) return resultSetIterator.newIterator();
      return l.iterator();
   }

   public DTO getDTO() {
      if (size() < 1) return null;
      return (DTO) get(0);
   }

   public HashMap getMapOf(String key) {
      return getMapOf(key, false);
   }

   public HashMap getMapOf(String key, boolean reload) {
      if (mapOfMap==null)
         mapOfMap = new HashMap();

      HashMap m = (HashMap)mapOfMap.get(key);

      if (reload) m=null;

      if (m!=null) return m;

      try {
         final HashMap map = new HashMap();

         if (size()>0) {
            final DTO first = (DTO)get(0);

            logger.logDebug("getMapOf: "+key+" from "+first.getClass());

            final DTOField fld = (DTOField)DTOCache.getInstance().getFields(first.getClass()).get(key.toLowerCase());

            if (fld==null) throw new RuntimeException("Unknown field : "+key);


            for (int i = 0; i < this.size(); i++) {
               DTO dto = (DTO) this.get(i);

               final Object k = fld.getGetter().invoke(dto,null);

               map.put(k,dto);
            }
         }

         mapOfMap.put(key,map);

         return map;
      } catch (Exception e) {
         throw new RTException(e);
      }
   }

   public DTO findByPK(Object objsel) {
      try {
         loadPKField();

         if (pkField==null) return null;

         final int i = find(pkField.getStFieldName(), objsel);

         if (i>=0) return (DTO) get(i);

         return null;
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   private void loadPKField() throws Exception {
      if (pkField==null) {
         if (size()<1) return;
         final DTO dto = (DTO)get(0);
         if (dto==null) return;
         pkField = DTOCache.getInstance().getPkField(dto.getClass());
      }
   }

   public DTO select(DTO dto) {
      if (selected!=null) {
         selected.deSelect();
         selected = null;
      }

      if (dto==null) return null;

      final Iterator it = iterator();
      while (it.hasNext()) {
         DTO dz = (DTO) it.next();

         if (dz==dto) {
            dto.select();
            selected = dto;
            return selected;
         }

      }

      throw new RuntimeException("the DTO is not a member of this list");
   }

   public DTO getSelected() {
      return selected;
   }

   public Object getCurrentObject() {
      return currentObject;
   }

   public void setCurrentObject(Object currentObject) {
      this.currentObject = currentObject;
   }

   public int size() {
      return l.size();
   }

   public void clear() {
      clearMaps();
      l.clear();
   }

   public boolean isEmpty() {
      return l.isEmpty();
   }

   public Object[] toArray() {
      return l.toArray();
   }

   public Object remove(int index) {
      clearMaps();
      return l.remove(index);
   }

   public void add(int index, Object element) {
      clearMaps();
      l.add(index, element);
   }

   public int indexOf(Object o) {
      return l.indexOf(o);
   }

   public int lastIndexOf(Object o) {
      return l.lastIndexOf(o);
   }

   public boolean contains(Object o) {
      return l.contains(o);
   }

   public boolean remove(Object o) {
      clearMaps();
      return l.remove(o);
   }

   public boolean addAll(int index, Collection c) {
      clearMaps();
      return l.addAll(index, c);
   }

   public boolean containsAll(Collection c) {
      return l.containsAll(c);
   }

   public boolean removeAll(Collection c) {
      clearMaps();
      return l.removeAll(c);
   }

   public boolean retainAll(Collection c) {
      clearMaps();
      return l.retainAll(c);
   }

   public List subList(int fromIndex, int toIndex) {
      return l.subList(fromIndex, toIndex);
   }

   public ListIterator listIterator() {
      return l.listIterator();
   }

   public ListIterator listIterator(int index) {
      return l.listIterator(index);
   }

   public Object set(int index, Object element) {
      return l.set(index, element);
   }

   public Object[] toArray(Object a[]) {
      return l.toArray(a);
   }

   public Object clone() throws CloneNotSupportedException {
      final DTOList cloned = (DTOList) super.clone();
      cloned.l=(ArrayList) l.clone();
      return cloned;
   }

   public void setCurrentIndex(int currentIndex) {
      this.currentIndex = currentIndex;
      setCurrentObject(get(currentIndex));
   }

   public int getCurrentIndex() {
      return currentIndex;
   }

   public void markAllUpdate() {
      for (int i = 0; i < this.size(); i++) {
         DTO dto = (DTO) this.get(i);
         dto.markUpdate();
      }
   }

   public void markAllNew() {
      for (int i = 0; i < this.size(); i++) {
         DTO dto = (DTO) this.get(i);
         dto.markNew();
      }
   }

   public void activateFilter() {
      getFilter().activate();
   }

   public void convertAllToNew() {
      if (deleted!=null)
         deleted.clear();

      for (int i = 0; i < this.size(); i++) {
         DTO dto = (DTO) this.get(i);

         dto.markNew();
      }
   }

   public void deleteAll() {

      final int n = size();

      for (int i=0;i<n;i++) {
         delete(0);
      }
   }
   
   public void deleteAllNormal() {

      final int n = size();

      for (int i=0;i<n;i++) {
         delete(0);
      }
   }

   public String toString() {
      StringBuffer sz = new StringBuffer();

      sz.append('[');

      for (int i = 0; i < this.size(); i++) {
         Object o = (Object) this.get(i);

         if (i>0) sz.append(",");

         sz.append(String.valueOf(o));
      }

      sz.append(']');

      return sz.toString();

   }
   
   public Object getTotal2(String s) {

      BigDecimal x = null;

      for (int i = 0; i < this.size(); i++) {
         DTO dto = (DTO) this.get(i);

         x = BDUtil.add((BigDecimal) dto.getFieldValueByFieldName(s),x);
      }

      return x;
   }

}

