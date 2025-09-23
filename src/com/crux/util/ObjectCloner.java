/***********************************************************************
 * Module:  com.crux.util.ObjectCloner
 * Author:  Denny Mahendra
 * Created: Mar 16, 2004 2:35:20 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.util;

import java.io.*;

public class ObjectCloner {
   /**
    * so that nobody can accidentally create an ObjectCloner object
    */
   private ObjectCloner() {
   }

   /**
    * returns a deep copy of an object
    * @param oldObj
    * @return
    */
   static public Object deepCopy(Object oldObj) {
      ObjectOutputStream oos = null;
      ObjectInputStream ois = null;
      try {
         ByteArrayOutputStream bos =
               new ByteArrayOutputStream();
         oos = new ObjectOutputStream(bos);
         oos.writeObject(oldObj);
         oos.flush();
         ByteArrayInputStream bin =
               new ByteArrayInputStream(bos.toByteArray());
         ois = new ObjectInputStream(bin);

         final Object o = ois.readObject();

         oos.close();
         ois.close();

         return o; // G
      }
      catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public static byte [] serialize(Object o) {
      try {
         ObjectOutputStream oos = null;
         ByteArrayOutputStream bos =
                  new ByteArrayOutputStream();
         oos = new ObjectOutputStream(bos);
         oos.writeObject(o);
         oos.flush();
         final byte[] ba = bos.toByteArray();

         oos.close();

         return ba;
      }
      catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public static Object deSerialize(byte [] o) {
      ObjectInputStream ois = null;
      try {
         ByteArrayInputStream bin =
               new ByteArrayInputStream(o);
         ois = new ObjectInputStream(bin);

         final Object z = ois.readObject();

         ois.close();

         return z;
      }
      catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
}
