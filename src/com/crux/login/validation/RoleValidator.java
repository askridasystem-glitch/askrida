/***********************************************************************
 * Module:  com.crux.login.validation.RoleValidator
 * Author:  Denny Mahendra
 * Created: Apr 27, 2004 2:53:13 PM
 * Purpose: 
 ***********************************************************************/

package com.crux.login.validation;

import com.crux.util.validation.Validator;
import com.crux.util.validation.FieldValidator;

public class RoleValidator extends Validator{
       public static FieldValidator vfRoleID = new FieldValidator("roleid","Role ID","string",32, MANDATORY);
       public static FieldValidator vfRoleName   = new FieldValidator("rolename","Role Name","string",50, MANDATORY);
       public static FieldValidator vfActiveDate = new FieldValidator("activedate","Active Date","date",-1);
       public static FieldValidator vfInActiveDate = new FieldValidator("inactivedate","Inactive Date","date",-1);
}
