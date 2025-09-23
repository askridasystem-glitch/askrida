/***********************************************************************
 * Module:  com.crux.common.model.UserSession
 * Author:  Denny Mahendra
 * Created: Mar 16, 2004 2:27:33 PM
 * Purpose: 
 ***********************************************************************/
package com.crux.common.model;

import com.crux.session.Session;

import java.util.Date;

public interface UserSession extends Session {

    Date getDtTransactionDate();

    void setDtTransactionDate(Date dtTransactionDate);

    String getStUserType();

    String getStUserID();

    String getStUserName();

    String getStDivision();

    void setStDivision(String stDivision);

    String getStDepartment();

    void setStDepartment(String stDepartment);

    String getStPhone();

    void setStPhone(String stPhone);

    String getStContactNum();

    void setStContactNum(String stContactNum);

    String getStEmail();

    void setStEmail(String stEmail);

    String getStPasswd();

    void setStPasswd(String stPasswd);

    boolean isAdmin();

    String setStVendorID(String vendorId);
}
