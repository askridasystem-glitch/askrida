/***********************************************************************
 * Module:  com.webfin.gl.model.GLCostCenterView3
 * Author:  Denny Mahendra
 * Created: Jul 17, 2005 10:57:56 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.pool.DTOPool;
import com.webfin.entity.model.EntityView;
import java.util.Date;

public class GLCostCenterView3 extends DTO implements RecordAudit {
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

   public static String tableName = "gl_cost_center3";

   private String stCostCenterCode;
   private String stDescription;
   private String stAddress;
   private String stType;
   private Date dtValidDate;
   private String stPhoneCode;
   private String stFaxCode;
   private String stEntityID;
   private String stSubCode;
   private String stShortname;

   public static String fieldMap[][] = {
      {"stCostCenterCode","cc_code*pk"},
      {"stDescription","description"},
      {"stAddress","address"},
      {"stType","type"},
      {"dtValidDate","valid_date"},
      {"stPhoneCode","phone"},
      {"stFaxCode","fax"},
      {"stEntityID","ent_id"},
      {"stSubCode","sub_code"},
      {"stShortname","shortname"},
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
     * @return the stPhoneCode
     */
    public String getStPhoneCode() {
        return stPhoneCode;
    }

    /**
     * @param stPhoneCode the stPhoneCode to set
     */
    public void setStPhoneCode(String stPhoneCode) {
        this.stPhoneCode = stPhoneCode;
    }

    /**
     * @return the stFaxCode
     */
    public String getStFaxCode() {
        return stFaxCode;
    }

    /**
     * @param stFaxCode the stFaxCode to set
     */
    public void setStFaxCode(String stFaxCode) {
        this.stFaxCode = stFaxCode;
    }



    /**
     * @return the stEntityID
     */
    public String getStEntityID() {
        return stEntityID;
    }

    /**
     * @param stEntityID the stEntityID to set
     */
    public void setStEntityID(String stEntityID) {
        this.stEntityID = stEntityID;
    }

    public EntityView getEntity() {

        final EntityView gcc = (EntityView) DTOPool.getInstance().getDTO(EntityView.class, stEntityID);

        return gcc;

    }



    /**
     * @return the stSubCode
     */
    public String getStSubCode() {
        return stSubCode;
    }

    /**
     * @param stSubCode the stSubCode to set
     */
    public void setStSubCode(String stSubCode) {
        this.stSubCode = stSubCode;
    }

    /**
     * @return the stShortname
     */
    public String getStShortname() {
        return stShortname;
    }

    /**
     * @param stShortname the stShortname to set
     */
    public void setStShortname(String stShortname) {
        this.stShortname = stShortname;
    }

}