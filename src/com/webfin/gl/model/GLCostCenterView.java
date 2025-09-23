/***********************************************************************
 * Module:  com.webfin.gl.model.GLCostCenterView
 * Author:  Denny Mahendra
 * Created: Jul 17, 2005 10:57:56 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.Tools;
import java.util.Date;

public class GLCostCenterView extends DTO implements RecordAudit {
   /*
   CREATE TABLE gl_dept
(
   varchar(8) NOT NULL,
   varchar(128) NOT NULL,
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT dept_pk PRIMARY KEY (dept_code)
)
   */

   public static String tableName = "gl_cost_center";

   private String stCostCenterCode;
   private String stDescription;
   private String stAddress;
   private String stType;
   private Date dtValidDate;
   private String stMachineVehicleZone;
   private String stSubCostCenterCode;
   private String stEmail;
   private String stEmail2;

   public static String fieldMap[][] = {
      {"stCostCenterCode","cc_code*pk"},
      {"stDescription","description"},
      {"stAddress","address"},
      {"stType","type"},
      {"dtValidDate","valid_date"},
      {"stMachineVehicleZone","mv_zone"},
      {"stSubCostCenterCode","sub_code"},
      {"stEmail","email"},
      {"stEmail2","email2"},
   };

   public static String comboFields[] = {"cc_code","description","address"};

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
   
   public String getStAddress() {
      return stAddress;
   }

   public void setStAddress(String stAddress) {
      this.stAddress = stAddress;
   }
   
   public String getStType() {
      return stType;
   }

   public void setStType(String stType) {
      this.stType = stType;
   }

    public Date getDtValidDate() {
        return dtValidDate;
    }

    public void setDtValidDate(Date dtValidDate) {
        this.dtValidDate = dtValidDate;
    }

    public String getTypeDescription(){
        String typeDesc = "";
        int type = Integer.parseInt(stType);
        switch (type) {
            case 1:
                typeDesc = "CABANG";
                break;
            case 2:
                typeDesc = "PERWAKILAN";
                break;
            case 3:
                typeDesc = "PEMASAR";
                break;
            case 4:
                typeDesc = "KANTOR PUSAT";
                break;
            case 5:
                typeDesc = "OUTLET";
                break;
            default:
                typeDesc = "OPERASIONAL";
        }

        return typeDesc;
    }

    /**
     * @return the stMachineVehicleZone
     */
    public String getStMachineVehicleZone() {
        return stMachineVehicleZone;
    }

    /**
     * @param stMachineVehicleZone the stMachineVehicleZone to set
     */
    public void setStMachineVehicleZone(String stMachineVehicleZone) {
        this.stMachineVehicleZone = stMachineVehicleZone;
    }

    /**
     * @return the stSubCostCenterCode
     */
    public String getStSubCostCenterCode() {
        return stSubCostCenterCode;
    }

    /**
     * @param stSubCostCenterCode the stSubCostCenterCode to set
     */
    public void setStSubCostCenterCode(String stSubCostCenterCode) {
        this.stSubCostCenterCode = stSubCostCenterCode;
    }

    /**
     * @return the stEmail
     */
    public String getStEmail() {
        return stEmail;
    }

    /**
     * @param stEmail the stEmail to set
     */
    public void setStEmail(String stEmail) {
        this.stEmail = stEmail;
    }

    public String getStEmail2() {
        return stEmail2;
    }

    /**
     * @param stEmail2 the stEmail2 to set
     */
    public void setStEmail2(String stEmail2) {
        this.stEmail2 = stEmail2;
    }
    
}
