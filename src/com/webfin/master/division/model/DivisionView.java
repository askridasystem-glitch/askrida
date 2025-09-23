/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.webfin.master.division.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.LogManager;

/**
 *
 * @author dhoni
 */
public class DivisionView  extends DTO implements RecordAudit {

    private final static transient LogManager logger = LogManager.getInstance(DivisionView.class);

   //MARK BUAT BERSIH2X
   //public static String tableName = "gl_je_detail_temp";
   public static String tableName = "s_division";

   public static String fieldMap[][] = {
      {"stDivisionCode","code*pk*nd"},
      {"stDescription","description"},
      {"stCostCenterCode","cc_code"},
      {"stRegionID","region_id"},
      {"stRkapID","rkap_id"},
      {"stHcisID","hcis_id"},
      {"stDirektoratID","direktorat"},

   };

   private String stDivisionCode;
   private String stDescription;
   private String stCostCenterCode;
   private String stRegionID;
   private String stRkapID;
   private String stHcisID;
   private String stDirektoratID;

    public String getStCostCenterCode() {
        return stCostCenterCode;
    }

    public void setStCostCenterCode(String stCostCenterCode) {
        this.stCostCenterCode = stCostCenterCode;
    }

    public String getStDescription() {
        return stDescription;
    }

    public void setStDescription(String stDescription) {
        this.stDescription = stDescription;
    }

    public String getStDivisionCode() {
        return stDivisionCode;
    }

    public void setStDivisionCode(String stDivisionCode) {
        this.stDivisionCode = stDivisionCode;
    }

    public String getStRegionID() {
        return stRegionID;
    }

    public void setStRegionID(String stRegionID) {
        this.stRegionID = stRegionID;
    }

    /**
     * @return the stRkapID
     */
    public String getStRkapID() {
        return stRkapID;
    }

    /**
     * @param stRkapID the stRkapID to set
     */
    public void setStRkapID(String stRkapID) {
        this.stRkapID = stRkapID;
    }

    /**
     * @return the stHcisID
     */
    public String getStHcisID() {
        return stHcisID;
    }

    /**
     * @param stHcisID the stHcisID to set
     */
    public void setStHcisID(String stHcisID) {
        this.stHcisID = stHcisID;
    }

    /**
     * @return the stDirektoratID
     */
    public String getStDirektoratID() {
        return stDirektoratID;
    }

    /**
     * @param stDirektoratID the stDirektoratID to set
     */
    public void setStDirektoratID(String stDirektoratID) {
        this.stDirektoratID = stDirektoratID;
    }

   
}
