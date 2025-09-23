/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceProdukView
 * Author:  ahmad rhodoni
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

public class InsuranceLunasView extends DTO {

   /*

CREATE TABLE aba_lunas
(
  nopol character varying(17),
  nobuk character varying(17),
  tglbay timestamp without time zone
)
WITH (
  OIDS=FALSE
);
ALTER TABLE aba_lunas OWNER TO postgres;
   */

   public static String tableName = "aba_lunas";

   public static String fieldMap[][] = {
   	   {"stInsuranceNoPolis", "nopol"},
           {"stInsuranceNoBukti", "nobuk"},
           {"dtTanggalBayar", "tglbay"},
           {"stFlag","flag"},
   };

    private String stInsuranceNoPolis;
    private String stInsuranceNoBukti;
   
    private Date dtTanggalBayar;
    private String stFlag;
    
    public String getStInsuranceNoPolis() {
        return stInsuranceNoPolis;
    }

    public void setStInsuranceNoPolis(String stInsuranceNoPolis) {
        this.stInsuranceNoPolis = stInsuranceNoPolis;
    }

    public String getStInsuranceNoBukti() {
        return stInsuranceNoBukti;
    }

    public void setStInsuranceNoBukti(String stInsuranceNoBukti) {
        this.stInsuranceNoBukti = stInsuranceNoBukti;
    }

    public Date getDtTanggalBayar() {
        return dtTanggalBayar;
    }

    public void setDtTanggalBayar(Date dtTanggalBayar) {
        this.dtTanggalBayar = dtTanggalBayar;
    }
    
    public String getStFlag() {
        return stFlag;
    }

    public void setStFlag(String stFlag) {
        this.stFlag = stFlag;
    }
    
     
}