/***********************************************************************
 * Module:  com.crux.pool.DTOPool
 * Author:  Denny Mahendra
 * Created: Oct 29, 2005 9:15:22 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.pool;

import com.crux.common.model.DTO;
import com.crux.util.DTOCache;
import com.crux.util.DTOField;
import com.crux.util.ListUtil;
import com.crux.util.ObjectCloner;

import java.util.HashMap;
import java.util.Iterator;

public class DTOPool {
   private static DTOPool staticinstance;
   private long lastExpireRun;

   public static DTOPool getInstance() {
      if (staticinstance == null) staticinstance = new DTOPool();
      return staticinstance;
   }

   private DTOPool() {
   }

   private HashMap dtoMap = new HashMap();

   public synchronized void clear() {
      dtoMap.clear();
   }

   public synchronized DTO getDTO(Class dtocl, String key) {

      expireDTOS();

      if (key==null) return null;

      try {
         HashMap m = (HashMap) dtoMap.get(dtocl.getName());

         if (m==null) {
            m=new HashMap();
            dtoMap.put(dtocl.getName(), m);
         }

         DTO dto = (DTO) m.get(key);

         if (dto==null) {
            dto= loadDTO(dtocl,key);
            if (dto==null) dto = new NonExistence();
            m.put(key,dto);
         }

         if (dto instanceof NonExistence) return null;

         dto.timeStamp = System.currentTimeMillis();

         dto = (DTO) ObjectCloner.deepCopy(dto);

         return dto;
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public DTO getDTORO(Class dtocl, String key) {

      if (key==null) return null;

      try {
         HashMap m = (HashMap) dtoMap.get(dtocl.getName());

         if (m==null) {
            m=new HashMap();
            dtoMap.put(dtocl.getName(), m);
         }

         DTO dto = (DTO) m.get(key);

         if (dto==null) {
            dto= loadDTO(dtocl,key);
            if (dto==null) dto = new NonExistence();
            m.put(key,dto);
         }

         if (dto instanceof NonExistence) return null;

         return dto;
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   private void expireDTOS() {
      if (System.currentTimeMillis()-lastExpireRun<5000) return; // only run after 5 seconds period

      lastExpireRun = System.currentTimeMillis();

      if (dtoMap==null) return;

      Iterator mit = dtoMap.values().iterator();

      long expireTime = System.currentTimeMillis()-10*60*1000;

      while (mit.hasNext()) {
         HashMap m = (HashMap) mit.next();

         Iterator dit = m.values().iterator();


         while (dit.hasNext()) {
            DTO dto = (DTO) dit.next();

            if (dto.timeStamp<expireTime) dit.remove();
         }
      }
   }

   private DTO loadDTO(Class dtocl, String key) throws Exception {
      final DTOField pkField = DTOCache.getInstance().getPkField(dtocl);
      if(pkField==null) throw new Exception("PK Field not found : "+dtocl);
      final String tableName = DTOCache.getTableName(dtocl);

      return ListUtil.getDTOListFromQuery(
              "select * from "+tableName+" where "+pkField.getStDatabaseFieldName()+" = ?",
              new Object [] {key},
              dtocl
      ).getDTO();
   }

   public synchronized void reset(DTO dto) throws Exception {

      if (dto==null) return;

      final HashMap map = (HashMap)dtoMap.get(dto.getClass().getName());
      if (map!=null) {
         final DTOField pkField = DTOCache.getInstance().getPkField(dto.getClass());

         final Object key = pkField.getGetter().invoke(dto,null);

         map.remove(key);
      }
   }

   public synchronized void reset(Class cls, Object key) throws Exception {
      final HashMap map = (HashMap)dtoMap.get(cls.getName());
      if (map!=null) {

         map.remove(key);
      }
   }

   public synchronized long getDTOCount() {
      expireDTOS();

      Iterator classit = dtoMap.values().iterator();

      long n=0;

      while (classit.hasNext()) {
         HashMap om = (HashMap) classit.next();
         n+=om.size();
      }

      return n;
   }

   public static class NonExistence extends DTO {};
}
