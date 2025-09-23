/***********************************************************************
 * Module:  com.crux.file.FileView
 * Author:  Denny Mahendra
 * Created: Jul 28, 2006 12:09:08 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.file;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.common.parameter.Parameter;
import com.crux.util.BDUtil;
import com.crux.util.IDFactory;
import com.crux.util.SQLUtil;
import com.crux.util.LogManager;
import com.crux.util.jai.ThumbnailGenerator;

import java.math.BigDecimal;
import java.util.Date;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileView extends DTO implements RecordAudit {
   /*
   CREATE TABLE S_FILES
(
  FILE_ID          NUMBER,
  ORIG_NAME        VARCHAR2(255 BYTE),
  FILE_PATH        VARCHAR2(255 BYTE),
  FILE_SIZE        NUMBER,
  FILE_DATE        DATE,
  FILE_TYPE        VARCHAR2(64 BYTE),
  MIME_TYPE        VARCHAR2(64 BYTE),
  COMPRESSED_FLAG  VARCHAR2(1 BYTE),
  ORIG_SIZE        NUMBER,
  CREATE_WHO       VARCHAR2(32 BYTE),
  CREATE_DATE      DATE,
  CHANGE_WHO       VARCHAR2(32 BYTE),
  CHANGE_DATE      DATE,
  PARENT_ID        NUMBER,
  IMAGE_WIDTH      NUMBER,
  IMAGE_HEIGHT     NUMBER,
  IMAGE_COLORS     NUMBER,
  FOLDER_FLAG      VARCHAR2(1 BYTE),
  THUMB_FLAG       VARCHAR2(1 BYTE),
  FILE_GROUP       VARCHAR2(256 BYTE),
  DESCRIPTION      VARCHAR2(255 BYTE),
  CONSTRAINT PK_S_FILES PRIMARY KEY (FILE_ID)
)
   */
    
   private final static transient LogManager logger = LogManager.getInstance(FileView.class);

   public static String tableName = "S_FILES";

   public static String fieldMap[][] = {
      {"stFileID","FILE_ID*pk"},
      {"stOriginalName","ORIG_NAME"},
      {"stFilePath","FILE_PATH"},
      {"dbFileSize","FILE_SIZE"},
      {"dtFileDate","FILE_DATE"},
      {"stFileType","FILE_TYPE"},
      {"stMimeType","MIME_TYPE"},
      {"stCompressedFlag","COMPRESSED_FLAG"},
      {"dbOriginalSize","ORIG_SIZE"},
      {"stParentID","PARENT_ID"},
      {"dbImageWidth","IMAGE_WIDTH"},
      {"dbImageHeight","IMAGE_HEIGHT"},
      {"stImageColors","IMAGE_COLORS"},
      {"stFolderFlag","FOLDER_FLAG"},
      {"stThumbFlag","THUMB_FLAG"},
      {"stFileGroup","FILE_GROUP"},
      {"stDescription","DESCRIPTION"},
      {"stImageFlag","IMAGE_FLAG"},
   };

   private String stFileID;
   private String stOriginalName;
   private String stFilePath;
   private BigDecimal dbFileSize;
   private Date dtFileDate;
   private String stFileType;
   private String stMimeType;
   private String stCompressedFlag;
   private BigDecimal dbOriginalSize;
   private String stParentID;
   private BigDecimal dbImageWidth;
   private BigDecimal dbImageHeight;
   private String stImageColors;
   private String stFolderFlag;
   private String stThumbFlag;
   private String stFileGroup;
   private String stDescription;
   private String stImageFlag;


   public String getStFileSize() {
      if (dbFileSize==null) return null;
      long fz = dbFileSize.longValue();

      if (fz>1*1024*1024) {

         BigDecimal b = dbFileSize.divide(new BigDecimal(1024*1024),1, BigDecimal.ROUND_HALF_DOWN);

         return b+"M";
      }
      if (fz>1024) {

         BigDecimal b = dbFileSize.divide(new BigDecimal(1024), 1, BigDecimal.ROUND_HALF_DOWN);

         return b+"K";
      }
      else
         return String.valueOf(fz)+"b";
   }

   public String getStFileLink() {
      return stDescription==null?stOriginalName:stDescription;
   }

   public String getStImageFlag() {
      return stImageFlag;
   }

   public void setStImageFlag(String stImageFlag) {
      this.stImageFlag = stImageFlag;
   }

   public String getStFileID() {
      return stFileID;
   }

   public void setStFileID(String stFileID) {
      this.stFileID = stFileID;
   }

   public String getStOriginalName() {
      return stOriginalName;
   }

   public void setStOriginalName(String stOriginalName) {
      this.stOriginalName = stOriginalName;
   }

   public String getStFilePath() {
      return stFilePath;
   }

   public void setStFilePath(String stFilePath) {
      this.stFilePath = stFilePath;
   }

   public BigDecimal getDbFileSize() {
      return dbFileSize;
   }

   public void setDbFileSize(BigDecimal dbFileSize) {
      this.dbFileSize = dbFileSize;
   }

   public Date getDtFileDate() {
      return dtFileDate;
   }

   public void setDtFileDate(Date dtFileDate) {
      this.dtFileDate = dtFileDate;
   }

   public String getStFileType() {
      return stFileType;
   }

   public void setStFileType(String stFileType) {
      this.stFileType = stFileType;
   }

   public String getStMimeType() {
      return stMimeType;
   }

   public void setStMimeType(String stMimeType) {
      this.stMimeType = stMimeType;
   }

   public String getStCompressedFlag() {
      return stCompressedFlag;
   }

   public void setStCompressedFlag(String stCompressedFlag) {
      this.stCompressedFlag = stCompressedFlag;
   }

   public BigDecimal getDbOriginalSize() {
      return dbOriginalSize;
   }

   public void setDbOriginalSize(BigDecimal dbOriginalSize) {
      this.dbOriginalSize = dbOriginalSize;
   }

   public String getStParentID() {
      return stParentID;
   }

   public void setStParentID(String stParentID) {
      this.stParentID = stParentID;
   }

   public BigDecimal getDbImageWidth() {
      return dbImageWidth;
   }

   public void setDbImageWidth(BigDecimal dbImageWidth) {
      this.dbImageWidth = dbImageWidth;
   }

   public BigDecimal getDbImageHeight() {
      return dbImageHeight;
   }

   public void setDbImageHeight(BigDecimal dbImageHeight) {
      this.dbImageHeight = dbImageHeight;
   }

   public String getStImageColors() {
      return stImageColors;
   }

   public void setStImageColors(String stImageColors) {
      this.stImageColors = stImageColors;
   }

   public String getStFolderFlag() {
      return stFolderFlag;
   }

   public void setStFolderFlag(String stFolderFlag) {
      this.stFolderFlag = stFolderFlag;
   }

   public String getStThumbFlag() {
      return stThumbFlag;
   }

   public void setStThumbFlag(String stThumbFlag) {
      this.stThumbFlag = stThumbFlag;
   }

   public String getStFileGroup() {
      return stFileGroup;
   }

   public void setStFileGroup(String stFileGroup) {
      this.stFileGroup = stFileGroup;
   }

   public String getStDescription() {
      return stDescription;
   }

   public void setStDescription(String stDescription) {
      this.stDescription = stDescription;
   }

   public void determineFileType() {
      if (getStOriginalName()==null) return ;

      int dot = getStOriginalName().lastIndexOf('.');

      if (dot<0) return;

      String ext = getStOriginalName().substring(dot+1,getStOriginalName().length());

      ext = ext.trim().toUpperCase();

      stFileType=ext;


      boolean isImage =
              "JPG".equalsIgnoreCase(ext) ||
              "JPEG".equalsIgnoreCase(ext) ||
              "GIF".equalsIgnoreCase(ext) ||
              "PNG".equalsIgnoreCase(ext);

      stImageFlag = isImage ? "Y":"N";

   }

   public boolean isImage() {
      return "Y".equalsIgnoreCase(stImageFlag);
   }

   public void store() throws Exception {
      if (isNew())
         if (stFileID==null)  stFileID = String.valueOf(IDFactory.createNumericID("FILE"));
  
      validateAttachmentSize();
              
      if (isImage()) {
         createThumb(64);
      }

      SQLUtil S = new SQLUtil();

      try {
         S.store(this);
      } finally {
         S.release();
      }
   }

   private void createThumb(int i) throws Exception {

      FileInputStream fis=null;
      FileOutputStream fos=null;

      try {
         fis = new FileInputStream(stFilePath);
         fos = new FileOutputStream(stFilePath+"_thumb_"+i);

         ThumbnailGenerator.createThumbnail(fis,fos,i,stOriginalName);
      } finally {
         if (fis!=null) fis.close();
         if (fos!=null) fos.close();
      }

   }
   
   private void validateAttachmentSize() throws Exception{
       final BigDecimal fileSize = getDbOriginalSize();
       final BigDecimal maxFileSize = Parameter.readNum("MAXIMUM_FILE_SIZE");
       final BigDecimal maxImageSize = Parameter.readNum("MAXIMUM_IMAGE_SIZE");
       if(isImage()){
           if(BDUtil.biggerThan(fileSize, maxImageSize))
               throw new RuntimeException("Ukuran maksimum file gambar adalah "+ BDUtil.div(maxImageSize, BDUtil.thousand) +" KB, Compress file gambar anda");
       }
       
       if(!isImage()){
           if(BDUtil.biggerThan(fileSize, maxFileSize))
               throw new RuntimeException("Ukuran maksimum file adalah "+ BDUtil.div(maxFileSize, BDUtil.thousand) +" KB");
       }
   }
}
