/***********************************************************************
 * Module:  com.webfin.entity.model.EntityView
 * Author:  Denny Mahendra
 * Created: Oct 29, 2005 7:15:29 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.outcoming.model;

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

public class OutcomingDocumentsView extends DTO implements RecordAudit {
  
    /*
    CREATE TABLE outcoming_documents
(
  out_id bigint NOT NULL,
  doc_out_id character varying(32) NOT NULL,
  create_who character varying(32) NOT NULL,
  create_date timestamp without time zone NOT NULL,
  change_who character varying(32),
  change_date timestamp without time zone,
  file_physic bigint
)
*/
   public static String tableName = "outcoming_documents";

   public static String comboFields[] = {"doc_out_id","file_physic"};

   public static String fieldMap[][] = {
      {"stDocumentOutID","doc_out_id*pk"},
      {"stOutID","out_id"},
      {"stFilePhysic","file_physic"},

      
   };
  
   private String stDocumentOutID;
   private String stOutID;
   private String stFilePhysic;
      
    public String getStDocumentOutID()
    {
        return stDocumentOutID;
    }

    public void setStDocumentOutID(String stDocumentOutID)
    {
        this.stDocumentOutID = stDocumentOutID;
    }

    public String getStOutID()
    {
        return stOutID;
    }

    public void setStOutID(String stOutID)
    {
        this.stOutID = stOutID;
    }

    public String getStFilePhysic()
    {
        return stFilePhysic;
    }

    public void setStFilePhysic(String stFilePhysic)
    {
        this.stFilePhysic = stFilePhysic;
    }


}
