/***********************************************************************
 * Module:  com.webfin.entity.model.EntityView
 * Author:  Denny Mahendra
 * Created: Oct 29, 2005 7:15:29 AM
 * Purpose: 
 ***********************************************************************/
package com.webfin.insurance.model;

import com.crux.common.model.DTO;
import com.crux.common.model.RecordAudit;

public class BiayaPemasaranDocumentsView extends DTO implements RecordAudit {

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
    public static String tableName = "biaya_pemasaran_doc";
    public static String comboFields[] = {"doc_pms_id", "file_physic", "keterangan"};
    public static String fieldMap[][] = {
        {"stDocumentPmsID", "doc_pms_id*pk"},
        {"stPemasaranID", "pms_id"},
        {"stFilePhysic", "file_physic"},
        {"stKeterangan", "keterangan"},};
    private String stDocumentPmsID;
    private String stPemasaranID;
    private String stFilePhysic;
    private String stKeterangan;

    public String getStFilePhysic() {
        return stFilePhysic;
    }

    public void setStFilePhysic(String stFilePhysic) {
        this.stFilePhysic = stFilePhysic;
    }

    /**
     * @return the stDocumentPmsID
     */
    public String getStDocumentPmsID() {
        return stDocumentPmsID;
    }

    /**
     * @param stDocumentPmsID the stDocumentPmsID to set
     */
    public void setStDocumentPmsID(String stDocumentPmsID) {
        this.stDocumentPmsID = stDocumentPmsID;
    }

    /**
     * @return the stPemasaranID
     */
    public String getStPemasaranID() {
        return stPemasaranID;
    }

    /**
     * @param stPemasaranID the stPemasaranID to set
     */
    public void setStPemasaranID(String stPemasaranID) {
        this.stPemasaranID = stPemasaranID;
    }

    /**
     * @return the stKeterangan
     */
    public String getStKeterangan() {
        return stKeterangan;
    }

    /**
     * @param stKeterangan the stKeterangan to set
     */
    public void setStKeterangan(String stKeterangan) {
        this.stKeterangan = stKeterangan;
    }
}
