/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceProdukView
 * Author:  Ahmad Rhodoni
 ***********************************************************************/

package com.webfin.h2h.model;

import com.webfin.insurance.model.*;
import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;
import com.crux.util.Tools;
import com.crux.util.BDUtil;
import com.crux.util.ConvertUtil;

import java.math.BigDecimal;
import java.util.Date;

public class WSDokumenFireView extends DTO {

   /*
  CREATE TABLE ws_dokumen_klaim
(
  id serial NOT NULL,
  nomor_loan character varying(128),
  no_pk character varying(32),
  no_polis character varying(20),
  no_urut bigint,
  kode_dokumen character varying(12),
  file_name character varying(255),
  file_type character varying(255),
  file_path character varying(255),
  file_size numeric,
  create_date timestamp without time zone,
  interkoneksi_file_path character varying(255),
  CONSTRAINT ws_dokumen_klaim_pkey PRIMARY KEY (id)
)
   */

   public static String tableName = "ws_dokumen_fire";

   public static String fieldMap[][] = {
   	   {"stID", "id*pk"},
           {"stNomorLoan", "nomor_loan"},
           {"stNomorPK", "no_pk"},
           {"stNomorPolis", "no_polis"},
           {"stNoUrut", "no_urut"},
           {"stKodeDokumen", "kode_dokumen"},
           {"stFileName", "file_name"},
           {"stFileType", "file_type"},
           {"stFilePath", "file_path"},
           {"dbFileSize", "file_size"},
           {"stInterkoneksiFilePath", "interkoneksi_file_path"},
           {"stKodeDokumenAskrida", "kode_dokumen_askrida"},
           {"stNoUrutPengajuan", "no_urut_pengajuan*n"},
           
   };

   private String stID;
   private String stNomorLoan;
   private String stNomorPK;
   private String stNomorPolis;
   private String stNoUrut;
   private String stKodeDokumen;
   private String stFileName;
   private String stFileType;
   private String stFilePath;
   private BigDecimal dbFileSize;
   private String stInterkoneksiFilePath;
   private String stKodeDokumenAskrida;
   private String stNoUrutPengajuan;

    public String getStNoUrutPengajuan() {
        return stNoUrutPengajuan;
    }

    public void setStNoUrutPengajuan(String stNoUrutPengajuan) {
        this.stNoUrutPengajuan = stNoUrutPengajuan;
    }

    public String getStKodeDokumenAskrida() {
        return stKodeDokumenAskrida;
    }

    public void setStKodeDokumenAskrida(String stKodeDokumenAskrida) {
        this.stKodeDokumenAskrida = stKodeDokumenAskrida;
    }

    public BigDecimal getDbFileSize() {
        return dbFileSize;
    }

    public void setDbFileSize(BigDecimal dbFileSize) {
        this.dbFileSize = dbFileSize;
    }

    public String getStFileName() {
        return stFileName;
    }

    public void setStFileName(String stFileName) {
        this.stFileName = stFileName;
    }

    public String getStFilePath() {
        return stFilePath;
    }

    public void setStFilePath(String stFilePath) {
        this.stFilePath = stFilePath;
    }

    public String getStFileType() {
        return stFileType;
    }

    public void setStFileType(String stFileType) {
        this.stFileType = stFileType;
    }

    public String getStID() {
        return stID;
    }

    public void setStID(String stID) {
        this.stID = stID;
    }

    public String getStInterkoneksiFilePath() {
        return stInterkoneksiFilePath;
    }

    public void setStInterkoneksiFilePath(String stInterkoneksiFilePath) {
        this.stInterkoneksiFilePath = stInterkoneksiFilePath;
    }

    public String getStKodeDokumen() {
        return stKodeDokumen;
    }

    public void setStKodeDokumen(String stKodeDokumen) {
        this.stKodeDokumen = stKodeDokumen;
    }

    public String getStNoUrut() {
        return stNoUrut;
    }

    public void setStNoUrut(String stNoUrut) {
        this.stNoUrut = stNoUrut;
    }

    public String getStNomorLoan() {
        return stNomorLoan;
    }

    public void setStNomorLoan(String stNomorLoan) {
        this.stNomorLoan = stNomorLoan;
    }

    public String getStNomorPK() {
        return stNomorPK;
    }

    public void setStNomorPK(String stNomorPK) {
        this.stNomorPK = stNomorPK;
    }

    public String getStNomorPolis() {
        return stNomorPolis;
    }

    public void setStNomorPolis(String stNomorPolis) {
        this.stNomorPolis = stNomorPolis;
    }

    public InsuranceDocumentTypeView getDocumentType() {
        return (InsuranceDocumentTypeView) DTOPool.getInstance().getDTO(InsuranceDocumentTypeView.class, stKodeDokumenAskrida);
    }

}