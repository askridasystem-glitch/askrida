/***********************************************************************
 * Module:  com.webfin.insurance.model.InsuranceTreatyView
 * Author:  Denny Mahendra
 * Created: Jul 2, 2006 12:47:07 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.util.Tools;

import java.util.Date;
import java.util.HashMap;
import java.math.BigDecimal;

public class InsuranceTreatyView extends DTO implements RecordAudit {
   /*

CREATE TABLE ins_treaty
(
  ins_treaty_id int8 NOT NULL,
  treaty_name  varchar(128),
  prop_tre_flag  varchar(1),
  retro_cess_flag  varchar(1),
  create_date timestamp NOT NULL,
  create_who varchar(32) NOT NULL,
  change_date timestamp,
  change_who varchar(32),
  CONSTRAINT ins_treaty_pk PRIMARY KEY (ins_treaty_id)
) without oids;


ALTER TABLE ins_treaty ADD COLUMN treaty_period_start timestamp;
ALTER TABLE ins_treaty ADD COLUMN treaty_period_end timestamp;
ALTER TABLE ins_treaty ADD COLUMN treaty_priority numeric;
ALTER TABLE ins_treaty ADD COLUMN active_flag varchar(1);

   */

   public static String tableName = "ins_treaty";

   public static String fieldMap[][] = {
      {"stInsuranceTreatyID","ins_treaty_id*pk"},
      {"stTreatyName","treaty_name"},
      {"stProportionalFlag","prop_tre_flag"},
      {"stRetrocessionFlag","retro_cess_flag"},
      {"dtTreatyPeriodStart","treaty_period_start"},
      {"dtTreatyPeriodEnd","treaty_period_end"},
      {"dbTreatyPriority","treaty_priority"},
      {"stActiveFlag","active_flag"},
      {"stTreatyClass","treaty_class"},
      {"stReference1","ref1"},
      {"stCompanyGroupID","company_group_id"},
      {"stInsuranceRiskCategoryCode","ins_risk_cat_code"},
      {"stApprovedFlag1","approved_flag1"},
      {"stApprovedWho1","approved_who1"},
      {"dtApprovedDate1","approved_date1"},
      {"stApprovedFlag2","approved_flag2"},
      {"stApprovedWho2","approved_who2"},
      {"dtApprovedDate2","approved_date2"},
      {"stApprovedFlag3","approved_flag3"},
      {"stApprovedWho3","approved_who3"},
      {"dtApprovedDate3","approved_date3"},
      {"stApprovedFlag4","approved_flag4"},
      {"stApprovedWho4","approved_who4"},
      {"dtApprovedDate4","approved_date4"},
      {"stEarthquakeZone","earthquake_zone"},


   };

   private Date dtTreatyPeriodStart;
   private Date dtTreatyPeriodEnd;
   private BigDecimal dbTreatyPriority;
   private String stActiveFlag;
   private String stTreatyClass;

   private String stInsuranceTreatyID;
   private String stTreatyName;
   private String stProportionalFlag;
   private String stRetrocessionFlag;
   private String stPolicyTypeID;
   private String stReference1;
   private DTOList details;
   private String stCompanyGroupID;
   private String stInsuranceRiskCategoryCode;
   
   private InsuranceTreatyDetailView tredet;

   private String stApprovedFlag1;
   private String stApprovedWho1;
   private Date dtApprovedDate1;

   private String stApprovedFlag2;
   private String stApprovedWho2;
   private Date dtApprovedDate2;

   private String stApprovedFlag3;
   private String stApprovedWho3;
   private Date dtApprovedDate3;

   private String stApprovedFlag4;
   private String stApprovedWho4;
   private Date dtApprovedDate4;

   private String stEarthquakeZone;

    public String getStEarthquakeZone() {
        return stEarthquakeZone;
    }

    public void setStEarthquakeZone(String stEarthquakeZone) {
        this.stEarthquakeZone = stEarthquakeZone;
    }

    public Date getDtApprovedDate1() {
        return dtApprovedDate1;
    }

    public void setDtApprovedDate1(Date dtApprovedDate1) {
        this.dtApprovedDate1 = dtApprovedDate1;
    }

    public Date getDtApprovedDate2() {
        return dtApprovedDate2;
    }

    public void setDtApprovedDate2(Date dtApprovedDate2) {
        this.dtApprovedDate2 = dtApprovedDate2;
    }

    public Date getDtApprovedDate3() {
        return dtApprovedDate3;
    }

    public void setDtApprovedDate3(Date dtApprovedDate3) {
        this.dtApprovedDate3 = dtApprovedDate3;
    }

    public Date getDtApprovedDate4() {
        return dtApprovedDate4;
    }

    public void setDtApprovedDate4(Date dtApprovedDate4) {
        this.dtApprovedDate4 = dtApprovedDate4;
    }

    public String getStApprovedFlag1() {
        return stApprovedFlag1;
    }

    public void setStApprovedFlag1(String stApprovedFlag1) {
        this.stApprovedFlag1 = stApprovedFlag1;
    }

    public String getStApprovedFlag2() {
        return stApprovedFlag2;
    }

    public void setStApprovedFlag2(String stApprovedFlag2) {
        this.stApprovedFlag2 = stApprovedFlag2;
    }

    public String getStApprovedFlag3() {
        return stApprovedFlag3;
    }

    public void setStApprovedFlag3(String stApprovedFlag3) {
        this.stApprovedFlag3 = stApprovedFlag3;
    }

    public String getStApprovedFlag4() {
        return stApprovedFlag4;
    }

    public void setStApprovedFlag4(String stApprovedFlag4) {
        this.stApprovedFlag4 = stApprovedFlag4;
    }

    public String getStApprovedWho1() {
        return stApprovedWho1;
    }

    public void setStApprovedWho1(String stApprovedWho1) {
        this.stApprovedWho1 = stApprovedWho1;
    }

    public String getStApprovedWho2() {
        return stApprovedWho2;
    }

    public void setStApprovedWho2(String stApprovedWho2) {
        this.stApprovedWho2 = stApprovedWho2;
    }

    public String getStApprovedWho3() {
        return stApprovedWho3;
    }

    public void setStApprovedWho3(String stApprovedWho3) {
        this.stApprovedWho3 = stApprovedWho3;
    }

    public String getStApprovedWho4() {
        return stApprovedWho4;
    }

    public void setStApprovedWho4(String stApprovedWho4) {
        this.stApprovedWho4 = stApprovedWho4;
    }

   
   public InsuranceTreatyDetailView getTredet() {
      return tredet;
   }
   
   public void setTredet(InsuranceTreatyDetailView tredet) {
      this.tredet = tredet;
   }

   public String getStTreatyClass() {
      return stTreatyClass;
   }

   public void setStTreatyClass(String stTreatyClass) {
      this.stTreatyClass = stTreatyClass;
   }

   public Date getDtTreatyPeriodStart() {
      return dtTreatyPeriodStart;
   }

   public void setDtTreatyPeriodStart(Date dtTreatyPeriodStart) {
      this.dtTreatyPeriodStart = dtTreatyPeriodStart;
   }

   public Date getDtTreatyPeriodEnd() {
      return dtTreatyPeriodEnd;
   }

   public void setDtTreatyPeriodEnd(Date dtTreatyPeriodEnd) {
      this.dtTreatyPeriodEnd = dtTreatyPeriodEnd;
   }

   public BigDecimal getDbTreatyPriority() {
      return dbTreatyPriority;
   }

   public void setDbTreatyPriority(BigDecimal dbTreatyPriority) {
      this.dbTreatyPriority = dbTreatyPriority;
   }

   public String getStActiveFlag() {
      return stActiveFlag;
   }

   public void setStActiveFlag(String stActiveFlag) {
      this.stActiveFlag = stActiveFlag;
   }

   public String getStInsuranceTreatyID() {
      return stInsuranceTreatyID;
   }

   public void setStInsuranceTreatyID(String stInsuranceTreatyID) {
      this.stInsuranceTreatyID = stInsuranceTreatyID;
   }

   public String getStTreatyName() {
      return stTreatyName;
   }

   public void setStTreatyName(String stTreatyName) {
      this.stTreatyName = stTreatyName;
   }

   public String getStProportionalFlag() {
      return stProportionalFlag;
   }

   public void setStProportionalFlag(String stProportionalFlag) {
      this.stProportionalFlag = stProportionalFlag;
   }

   public String getStRetrocessionFlag() {
      return stRetrocessionFlag;
   }

   public void setStRetrocessionFlag(String stRetrocessionFlag) {
      this.stRetrocessionFlag = stRetrocessionFlag;
   }

   public DTOList getDetails() {
      return details;
   }

   public DTOList getDetails(String poltype) {
      if (!Tools.isEqual(poltype,stPolicyTypeID)) details=null; // auto switch load
      stPolicyTypeID = poltype;
      loadDetails();
      return details;
   }

   private void loadDetails() {

      try {
         if (details == null) {
            details = ListUtil.getDTOListFromQuery(
                    "   select " +
                    "      a.* " +
                    "   from " +
                    "      ins_treaty_detail a" +
                    "         inner join ins_treaty_types b on b.ins_treaty_type_id = a.treaty_type" +
                    "   where " +
                    "      a.ins_treaty_id = ? and a.policy_type_id = ? and coalesce(a.delete_flag,'N') <> 'Y'" +
                    "   order by" +
                    "      b.treaty_type_level",
                    new Object [] {stInsuranceTreatyID, stPolicyTypeID},
                    InsuranceTreatyDetailView.class
            );


            HashMap mapByID = details.getMapOf("ins_treaty_detail_id");

            for (int i = 0; i < details.size(); i++) {
               InsuranceTreatyDetailView d = (InsuranceTreatyDetailView) details.get(i);

               d.setOwningList(details);

               if (d.getStParentID()!=null) {
                  InsuranceTreatyDetailView parent = (InsuranceTreatyDetailView) mapByID.get(d.getStParentID());

                  d.setParent(parent);
               }
            }
         }


      } catch (Exception e) {
         throw new RuntimeException(e);
      }

   }

   public void setDetails(DTOList details) {
      this.details = details;
   }
   
   public String getStReference1() {
      return stReference1;
   }

   public void setStReference1(String stReference1) {
      this.stReference1 = stReference1;
   }

    /**
     * @return the stCompanyGroupID
     */
    public String getStCompanyGroupID() {
        return stCompanyGroupID;
    }

    /**
     * @param stCompanyGroupID the stCompanyGroupID to set
     */
    public void setStCompanyGroupID(String stCompanyGroupID) {
        this.stCompanyGroupID = stCompanyGroupID;
    }

    /**
     * @return the stInsuranceRiskCategoryCode
     */
    public String getStInsuranceRiskCategoryCode() {
        return stInsuranceRiskCategoryCode;
    }

    /**
     * @param stInsuranceRiskCategoryCode the stInsuranceRiskCategoryCode to set
     */
    public void setStInsuranceRiskCategoryCode(String stInsuranceRiskCategoryCode) {
        this.stInsuranceRiskCategoryCode = stInsuranceRiskCategoryCode;
    }
}
