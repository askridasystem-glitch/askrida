/***********************************************************************
 * Module:  com.webfin.manual.form.PrintManualBook
 * Author:  Achmad Rhodoni
 * Created: 18 Mei 2009
 * Purpose: 
 ***********************************************************************/
package com.webfin.manual.form;

import com.crux.web.form.Form;
import com.crux.web.controller.SessionManager;

public class PrintManualBook extends Form {

    private String stManualBookTypeID;
    private String stFormType;
    private String stFileID;
    private boolean enableManualBook = SessionManager.getInstance().getSession().hasResource("USER_MANUAL_VIEW");

    public void setStManualBookTypeID(String stManualBookTypeID) {
        this.stManualBookTypeID = stManualBookTypeID;
    }

    public String getStManualBookTypeID() {
        return stManualBookTypeID;
    }

    public void initialize() {
        setTitle("DOWNLOAD FILE");
        setStFormType("MANUAL");
    }

    public void onChangeManualBookType() {
    }

    public void btnPrint() {
//      LOVManager.getInstance().getLOV(getDocLOVName(), null).getComboDesc(stFileID);
        if (stManualBookTypeID != null) {
            super.redirect("/pages/manual/" + stManualBookTypeID);
        } else {
            throw new RuntimeException("Jenis Manual Book Belum Dipilih");
        }
    }

    public void download() {
        setTitle("DOWNLOAD FILE");
        setStFormType("DOWNLOAD");
    }

    /**
     * @return the stFormType
     */
    public String getStFormType() {
        return stFormType;
    }

    /**
     * @param stFormType the stFormType to set
     */
    public void setStFormType(String stFormType) {
        this.stFormType = stFormType;
    }

    /**
     * @return the enableManualBook
     */
    public boolean isEnableManualBook() {
        return enableManualBook;
    }

    /**
     * @param enableManualBook the enableManualBook to set
     */
    public void setEnableManualBook(boolean enableManualBook) {
        this.enableManualBook = enableManualBook;
    }

    public void manual() {
        setTitle("DOWNLOAD FILE");
        setStFormType("MANUAL");
    }

    public void manualpdf() {
        setTitle("DOWNLOAD FILE");
        setStFormType("MANUALPDF");
    }

    public String getDocLOVName() {
        /*
        String cabang = UserManager.getInstance().getUser().getStBranch();
        String pattern = "VS_PWORD_"+getStPolicyTypeID();

        if(cabang!=null) pattern = pattern +"_"+cabang;

        String result = "";

        System.out.println("+++++++++++++ lov : " + LOVManager.getInstance().getLOV(pattern, null).getLOValue());
        if(LOVManager.getInstance().getLOV(pattern, null)==null)
        result = "VS_PWORD_"+getStPolicyTypeID();
        else
        result = pattern;

        return result;
         */

        return "VS_MANUAL_" + getStManualBookTypeID();
    }

    /**
     * @return the stFileID
     */
    public String getStFileID() {
        return stFileID;
    }

    /**
     * @param stFileID the stFileID to set
     */
    public void setStFileID(String stFileID) {
        this.stFileID = stFileID;
    }

    public void onChangeManualBook() {
    }

}
