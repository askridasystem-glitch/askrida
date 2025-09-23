/***********************************************************************
 * Module:  com.webfin.gl.model.AccountTypeView
 * Author:  Denny Mahendra
 * Created: Jul 18, 2005 5:44:27 AM
 * Purpose: 
 ***********************************************************************/

package com.webfin.gl.model;

import com.crux.common.model.DTO;

public class AccountTypeView extends DTO {

    /*

     varchar(5) NOT NULL,
   varchar(32) NOT NULL,
   varchar(5) NOT NULL,
    */

    private String stAccountType;
    private String stDescription;
    private String stBalanceType;

   public static String comboFields[] = {"accttype","description"};

    public static String fieldMap[][] = {
       {"stAccountType","accttype*pk"},
       {"stDescription","description"},
       {"stBalanceType","balancetype"},
    };

    public static String tableName = "gl_acct_type";

    public String getStAccountType() {
        return stAccountType;
    }

    public void setStAccountType(String stAccountType) {
        this.stAccountType = stAccountType;
    }

    public String getStDescription() {
        return stDescription;
    }

    public void setStDescription(String stDescription) {
        this.stDescription = stDescription;
    }

    public String getStBalanceType() {
        return stBalanceType;
    }

    public void setStBalanceType(String stBalanceType) {
        this.stBalanceType = stBalanceType;
    }
}
