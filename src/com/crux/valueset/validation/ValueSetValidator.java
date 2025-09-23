/**
 * Created by IntelliJ IDEA.
 * User: Andi Adhyaksa
 * Date: Nov 23, 2004
 * Time: 10:32:21 AM
 * To change this template use Options | File Templates.
 */
package com.crux.valueset.validation;

import com.crux.util.validation.FieldValidator;
import com.crux.util.validation.Validator;

public class ValueSetValidator extends Validator{
    public static FieldValidator vfVsgroup =  new FieldValidator("groupname", "Group Name","string",32,MANDATORY);
    public static FieldValidator vfVscode =  new FieldValidator("code", "Code","string",32,MANDATORY);
    public static FieldValidator vfVsdesc =  new FieldValidator("description", "Description","string",255);
    public static FieldValidator vfVsorder =  new FieldValidator("order", "Order","integer",-1);
}
