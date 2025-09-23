/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceKoasurView
 * Author:  Ahmad Rhodoni
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;
import com.crux.util.Tools;
import com.crux.util.BDUtil;
import com.crux.util.ConvertUtil;

import java.math.BigDecimal;
import java.util.Date;

public class InsuranceKoasurView extends DTO{

   /*
  norek character varying(32),
  nopol character varying(17),
  nomreg character varying(32),
  urut bigint,
  kodeas bigint
   */

   public static String tableName = "aba_koasur";

   public static String fieldMap[][] = {
      {"stNorek","norek"},
      {"stNopol","nopol"},
      {"stNomreg","nomreg"},
      {"stUrut","urut"},
      {"stKodeas","kodeas"},
   };
   
    private String stNorek;
    private String stNopol;
    private String stNomreg;
    private String stUrut;
    private String stKodeas;   
   
    public String getStNorek() {
        return stNorek;
    }

    public void setStNorek(String stNorek) {
        this.stNorek = stNorek;
    }
    
    public String getStNopol() {
        return stNopol;
    }

    public void setStNopol(String stNopol) {
        this.stNopol = stNopol;
    }
    
    public String getStNomreg() {
        return stNomreg;
    }

    public void setStNomreg(String stNomreg) {
        this.stNomreg = stNomreg;
    }
    
    public String getStUrut() {
        return stUrut;
    }

    public void setStUrut(String stUrut) {
        this.stUrut = stUrut;
    }
    
    public String getStKodeas() {
        return stKodeas;
    }

    public void setStKodeas(String stKodeas) {
        this.stKodeas = stKodeas;
    }
}