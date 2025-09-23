/***********************************************************************
 * Module:  com.crux.login.validation.UserValidator
 * Author:  Denny Mahendra
 * Created: May 5, 2004 10:46:48 AM
 * Purpose: 
 ***********************************************************************/

package com.crux.login.validation;

import com.crux.util.validation.Validator;
import com.crux.util.validation.FieldValidator;

public class UserValidator extends Validator{
    public static FieldValidator vfUserID = new FieldValidator("userid","User ID","string",32, MANDATORY);
    public static FieldValidator vfUserType = new FieldValidator("usertype","User Type","string",5);
    public static FieldValidator vfUserName   = new FieldValidator("username","User Name","string",50);
    public static FieldValidator vfBranchID = new FieldValidator("branchid","Branch ID","string",4);
    public static FieldValidator vfDivision   = new FieldValidator("division","Division","string",128);
    public static FieldValidator vfDepartment   = new FieldValidator("department","Department","string",128);
    public static FieldValidator vfPhone   = new FieldValidator("phone","Phone","string",20);
    public static FieldValidator vfContact   = new FieldValidator("contactnum","Contact Number","string",20);
    public static FieldValidator vfEmail   = new FieldValidator("email","Email Address","email",100);
    public static FieldValidator vfPassword   = new FieldValidator("password","Password","password",100);
    public static FieldValidator vfRePassword   = new FieldValidator("repassword","Retype Password","password",100);
    public static FieldValidator vfOutletID = new FieldValidator("outletid","Outlet ID","string",32, MANDATORY);
    public static FieldValidator vfWarehouseID = new FieldValidator("warehouseid","Warehouse ID","string",20);
    public static FieldValidator vfDepoID = new FieldValidator("depoid","Depo ID","string",20);
    public static FieldValidator vfActiveDate = new FieldValidator("activedate","Active Date","date",-1);
    public static FieldValidator vfInActiveDate = new FieldValidator("inactivedate","Inactive Date","date",-1);
    public static FieldValidator vfTransDate = new FieldValidator("transdate","Transaction Date","date",-1);
    public static FieldValidator vfRoles   = new FieldValidator("roles","Roles","string",20);
}
