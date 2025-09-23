/***********************************************************************
 * Module:  com.webfin.entity.model.EntityView
 * Author:  Denny Mahendra
 * Created: Oct 29, 2005 7:15:29 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.company.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.login.model.UserSessionView;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.web.controller.SessionManager;
import com.webfin.FinCodec;
import com.webfin.insurance.model.InsuranceEntityView;

import java.util.Date;

public class CompanyView extends DTO implements RecordAudit {
   /*
   CREATE TABLE ent_master
(
  CREATE TABLE s_company_group
(
  vs_group character varying(64) NOT NULL,
  vs_code character varying(64) NOT NULL,
  vs_description character varying(400),
  active_flag character varying(1),
  create_date timestamp without time zone NOT NULL,
  create_who character varying(32) NOT NULL,
  change_date timestamp without time zone,
  change_who character varying(32),
  ref1 character varying(400),
  default_flag character varying(1),
  orderseq numeric,
  ref2 character varying(1),
  division character varying(255),
  CONSTRAINT s_company_group_pkey PRIMARY KEY (vs_group, vs_code)
)

)
   */

   public static String tableName = "s_company_group";

   public static String comboFields[] = {"ent_id","ent_name","address","customer_status","tax_file","functionary_name","functionary_position","rc_no"};

   public static String fieldMap[][] = {
      {"stVSGroup","vs_group"},
      {"stVSCode","vs_code*pk"},
      {"stVSCompanyGroup","vs_description"},
      {"stActiveFlag","active_flag"},
   };

   /*
ALTER TABLE ent_master ADD COLUMN sharef0 varchar(32);
ALTER TABLE ent_master ADD COLUMN sharef1 varchar(32);
ALTER TABLE ent_master ADD COLUMN sharef2 varchar(32);

   */
   private String stVSGroup;
   private String stVSCode;
   private String stVSCompanyGroup;
   private String stActiveFlag;

    public String getStVSGroup() {
        return stVSGroup;
    }

    public void setStVSGroup(String stVSGroup) {
        this.stVSGroup = stVSGroup;
    }

    public String getStVSCode() {
        return stVSCode;
    }

    public void setStVSCode(String stVSCode) {
        this.stVSCode = stVSCode;
    }

    public String getStVSCompanyGroup() {
        return stVSCompanyGroup;
    }

    public void setStVSCompanyGroup(String stVSCompanyGroup) {
        this.stVSCompanyGroup = stVSCompanyGroup;
    }

    /**
     * @return the stActiveFlag
     */
    public String getStActiveFlag() {
        return stActiveFlag;
    }

    /**
     * @param stActiveFlag the stActiveFlag to set
     */
    public void setStActiveFlag(String stActiveFlag) {
        this.stActiveFlag = stActiveFlag;
    }

}
