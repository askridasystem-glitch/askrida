/***********************************************************************
 * Module:  com.crux.session.SessionImpl
 * Author:  Denny Mahendra
 * Created: May 25, 2005 1:35:47 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.session;

import com.crux.common.model.UserSession;
import com.crux.common.model.DTO;

import java.util.Date;

public class SessionImpl extends DTO implements Session, UserSession {
   private String stUserID;
   private Date dtTransactionDate;
   private String stUserName;
   private String stShortName;

   public String getStUserID() {
      return stUserID;
   }

   public void setStUserID(String stUserID) {
      this.stUserID = stUserID;
   }

   public Date getDtTransactionDate() {
      return dtTransactionDate;
   }

   public void setDtTransactionDate(Date dtTransactionDate) {
      this.dtTransactionDate = dtTransactionDate;
   }

   public void setDtTransactionDate(com.crux.data.Date dtTransactionDate) {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public String getStUserType() {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public String getStDivision() {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public void setStDivision(String stDivision) {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public String getStDepartment() {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public void setStDepartment(String stDepartment) {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public String getStPhone() {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public void setStPhone(String stPhone) {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public String getStContactNum() {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public void setStContactNum(String stContactNum) {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public String getStEmail() {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public void setStEmail(String stEmail) {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public String getStPasswd() {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public void setStPasswd(String stPasswd) {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public boolean isAdmin() {
      return false;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public String setStVendorID(String vendorId) {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }
   
   public String getStUserName() {
      return stUserName;
   }

   public void setStUserName(String stUserName) {
      this.stUserName = stUserName;
   }
   
   public String getStShortName(){
       return stShortName;
   }

   public void setStShortName(String stShortName){
       
   }

}
