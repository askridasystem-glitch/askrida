/***********************************************************************
 * Module:  com.crux.common.model.DTO
 * Author:  Denny Mahendra
 * Created: Mar 9, 2004 11:10:45 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.common.model;

import com.crux.util.*;
import com.crux.base.BaseClass;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.lang.reflect.InvocationTargetException;

public abstract class DTO extends BaseClass implements Serializable, Cloneable {
   private int flags = 0;

   private UserSession userSession;

   private String stCreateWho;
   private Date dtCreateDate;
   private String stChangeWho;
   private Date dtChangeDate;
   private DTO old;

   public DTO getOld() {
      return old;
   }

   public void setOld(DTO old) {
      this.old = old;
   }

   private boolean enableStoreSync;
   private boolean autoLoad = true;
   private boolean enableLockProtect = true;

   public void enableAutoLoad(){
      autoLoad = true;
   }

   public void disableAutoLoad(){
      autoLoad = false;
   }

   public boolean isAutoLoadEnabled() {
      return autoLoad;
   }

   private HashMap attributes;

   public void setAttribute(String stKey, Object o) {
      if (attributes==null) attributes = new HashMap();
      attributes.put(stKey, o);
   }

   public Object getAttribute(String stKey) {
      if (attributes==null) attributes = new HashMap();
      return attributes.get(stKey);
   }

   public String getStCreateWho() {
      return stCreateWho;
   }

   public void setStCreateWho(String stCreateWho) {
      this.stCreateWho = stCreateWho;
   }

   public String getStChangeWho() {
      return stChangeWho;
   }

   public void setStChangeWho(String stChangeWho) {
      this.stChangeWho = stChangeWho;
   }

   public Date getDtCreateDate() {
      return dtCreateDate;
   }

   public void setDtCreateDate(Date dtCreateDate) {
      this.dtCreateDate = dtCreateDate;
   }

   public Date getDtChangeDate() {
      return dtChangeDate;
   }

   public void setDtChangeDate(Date dtChangeDate) {
      this.dtChangeDate = dtChangeDate;
   }

   public void startUpdate() {
   }

   public void endUpdate() {

   }

   public String toString() {
      return LogUtil.logGetterMethods(this);
   }

   public Object getFieldType(String stFieldName) throws Exception {
      HashMap fieldDesc = DTOCache.getInstance().getFields(this.getClass());

      final DTOField fld = (DTOField) fieldDesc.get(stFieldName);

      if (fld==null) return null;

      return fld.getFieldType();
   }

   public Object getFieldValueByFieldName(String stFieldName) {
      try {
         HashMap fieldDesc = DTOCache.getInstance().getFields(this.getClass());

         final DTOField fld = (DTOField) fieldDesc.get(stFieldName);

         if (fld == null) {
            throw new IllegalArgumentException("There is no such field : "+stFieldName+" in class "+this.getClass().getName());
         }

         return fld.getGetter().invoke(this,null);
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public Object setFieldValueByFieldName(String stFieldName, Object value) {
      try {
         HashMap fieldDesc = DTOCache.getInstance().getFields(this.getClass());

         final DTOField fld = (DTOField) fieldDesc.get(stFieldName);

         if (fld == null) {
            throw new IllegalArgumentException("There is no such field : "+stFieldName+" in class "+this.getClass().getName());
         }

         return fld.getSetter().invoke(this,new Object [] {value});
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public String getStMarks(){
      final StringBuffer sz = new StringBuffer();
      sz.append('[');
      if (isSelected()) sz.append('S');
      if (isUpdate()) sz.append('U');
      if (isNew()) sz.append('N');
      if (isDelete()) sz.append('D');
      if (isInsertOrUpdate()) sz.append('I');
      sz.append(']');
      return sz.toString();
   }

   public void select() {
      flags |= 8;
   }

   public void deSelect() {
      flags &= ~8;
   }

   public void markDelete() {
      //if (old==null) old = (DTO) ObjectCloner.deepCopy(this);
      flags = (flags & ~7) | 4;
   }

   public void markInsertOrUpdate() {
      flags = (flags & ~7) | 3;
   }

   public void markUpdate() {
      //if (old==null) old = (DTO) ObjectCloner.deepCopy(this);
      flags = (flags & ~7) | 2;
   }

   public void markUpdateO() {
      if (old==null) old = (DTO) ObjectCloner.deepCopy(this);
      flags = (flags & ~7) | 2;
   }

   public void markNew() {
      flags = (flags & ~7) | 1;
      old=null;
   }

   public void markUnmodified() {
      flags = (flags & ~7);
   }

   public boolean isDelete() {
      return (flags & 7) == 4;
   }

   public boolean isUpdate() {
      return (flags & 7) == 2;
   }

   public boolean isInsertOrUpdate() {
      return (flags & 7) == 3;
   }

   public boolean isSelected() {
      return (flags & 8) != 0;
   }

   public boolean isNew() {
      return (flags & 7) == 1;
   }

   public boolean isUnModified() {
      return (flags & 7) == 0;
   }

   public boolean isModified() {
      return (flags & 7) != 0;
   }

   public UserSession getUserSession() {
      return userSession;
   }

   public void setUserSession(UserSession userSession) {
      this.userSession = userSession;
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   public boolean isEnableStoreSync() {
      return enableStoreSync;
   }

   public void setEnableStoreSync(boolean enableStoreSync) {
      this.enableStoreSync = enableStoreSync;
   }

   public boolean isEnableLockProtect() {
      return enableLockProtect;
   }

   public void setEnableLockProtect(boolean enableLockProtect) {
      this.enableLockProtect = enableLockProtect;
   }

   public boolean isChanged(String field) {
      if (old==null) return true;

      return !Tools.isEqual((Comparable)getFieldValueByFieldName(field),(Comparable)old.getFieldValueByFieldName(field));
   }

   public void touch() {
      if (isUnModified()) markUpdate();
   }

   public void beforeInsert() {
   }
}
