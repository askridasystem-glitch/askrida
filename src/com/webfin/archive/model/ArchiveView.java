/***********************************************************************
 * Module:  com.webfin.entity.model.EntityView
 * Author:  Denny Mahendra
 * Created: Oct 29, 2005 7:15:29 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.archive.model;

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

public class ArchiveView extends DTO implements RecordAudit {
   
   public static String tableName = "archive_doc";

  // public static String comboFields[] = {"in_id","sender","receiver"};

   public static String fieldMap[][] = {
      {"stArchiveID","archive_id*pk"},
      {"stArchiveSubject","archive_subject"},
      {"stNote","note"},
      {"stFilePhysic","file_physic"},
      {"stDeleteFlag","delete_flag"},
      {"dtPeriodStart","period_start"},
      {"dtPeriodEnd","period_end"},
      {"stDivision","division"},

   };

   private String stArchiveID;
   private String stArchiveSubject;
   private String stNote;
   private String stFilePhysic;
   private String stDeleteFlag;
   private Date dtPeriodStart;
   private Date dtPeriodEnd;
   private String stDivision;

    public String getStArchiveID() {
        return stArchiveID;
    }

    public void setStArchiveID(String stArchiveID) {
        this.stArchiveID = stArchiveID;
    }

    public String getStArchiveSubject() {
        return stArchiveSubject;
    }

    public void setStArchiveSubject(String stArchiveSubject) {
        this.stArchiveSubject = stArchiveSubject;
    }

    public String getStNote() {
        return stNote;
    }

    public void setStNote(String stNote) {
        this.stNote = stNote;
    }

    public String getStFilePhysic() {
        return stFilePhysic;
    }

    public void setStFilePhysic(String stFilePhysic) {
        this.stFilePhysic = stFilePhysic;
    }

    public String getStDeleteFlag() {
        return stDeleteFlag;
    }

    public void setStDeleteFlag(String stDeleteFlag) {
        this.stDeleteFlag = stDeleteFlag;
    }

    public Date getDtPeriodStart() {
        return dtPeriodStart;
    }

    public void setDtPeriodStart(Date dtPeriodStart) {
        this.dtPeriodStart = dtPeriodStart;
    }

    public Date getDtPeriodEnd() {
        return dtPeriodEnd;
    }

    public void setDtPeriodEnd(Date dtPeriodEnd) {
        this.dtPeriodEnd = dtPeriodEnd;
    }

    public String getStDivision() {
        return stDivision;
    }

    public void setStDivision(String stDivision) {
        this.stDivision = stDivision;
    }
   
   
}
