/***********************************************************************
 * Module:  com.webfin.entity.model.EntityView
 * Author:  Denny Mahendra
 * Created: Oct 29, 2005 7:15:29 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.approval.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;
import com.crux.login.model.UserSessionView;
import com.crux.pool.DTOPool;
import com.crux.util.DTOList;
import com.crux.util.ListUtil;
import com.crux.web.controller.SessionManager;
import com.webfin.FinCodec;

import java.util.Date;

public class IncomingDocumentsView extends DTO implements RecordAudit {
  
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
   public static String tableName = "incoming_documents";

   public static String comboFields[] = {"doc_out_id","file_physic"};

   public static String fieldMap[][] = {
      {"stDocumentInID","doc_in_id*pk"},
      {"stInID","in_id"},
      {"stFilePhysic","file_physic"},

      
   };
  
   private String stDocumentInID;
   private String stInID;
   private String stFilePhysic;

    public String getStDocumentInID()
    {
        return stDocumentInID;
    }

    public void setStDocumentInID(String stDocumentInID)
    {
        this.stDocumentInID = stDocumentInID;
    }

    public String getStInID()
    {
        return stInID;
    }

    public void setStInID(String stInID)
    {
        this.stInID = stInID;
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
