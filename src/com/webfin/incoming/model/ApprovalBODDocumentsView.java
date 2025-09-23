/***********************************************************************
 * Module:  com.webfin.entity.model.EntityView
 * Author:  Denny Mahendra
 * Created: Oct 29, 2005 7:15:29 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.incoming.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;


public class ApprovalBODDocumentsView extends DTO implements RecordAudit {
  
   public static String tableName = "approvalbod_documents";

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
