/***********************************************************************
 * Module:  com.webfin.outcoming.model.UploadBODDocumentsView
 * Author:  Denny Mahendra
 * Created: Oct 29, 2005 7:15:29 AM
 * Purpose: 
 ***********************************************************************/
package com.webfin.outcoming.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

public class UploadBODDocumentsView extends DTO implements RecordAudit {

    public static String tableName = "uploadbod_documents";
    public static String comboFields[] = {"doc_out_id", "file_physic"};
    public static String fieldMap[][] = {
        {"stDocumentOutID", "doc_out_id*pk"},
        {"stOutID", "out_id"},
        {"stFilePhysic", "file_physic"},};
    private String stDocumentOutID;
    private String stOutID;
    private String stFilePhysic;

    public String getStDocumentOutID() {
        return stDocumentOutID;
    }

    public void setStDocumentOutID(String stDocumentOutID) {
        this.stDocumentOutID = stDocumentOutID;
    }

    public String getStOutID() {
        return stOutID;
    }

    public void setStOutID(String stOutID) {
        this.stOutID = stOutID;
    }

    public String getStFilePhysic() {
        return stFilePhysic;
    }

    public void setStFilePhysic(String stFilePhysic) {
        this.stFilePhysic = stFilePhysic;
    }
}