/***********************************************************************
 * Module:  com.webfin.insurance.validator.InsurancePolicyValidator
 * Author:  Denny Mahendra
 * Created: Oct 9, 2005 10:15:22 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.insurance.validator;

import com.crux.util.validation.FieldValidator;

public class InsurancePolicyValidator {
   public static FieldValidator policyNo = new FieldValidator("Policy Number","string",20);
   public static FieldValidator policyItem = new FieldValidator("Policy Item","string",-1);
   public static FieldValidator policyItemDesc= new FieldValidator("Description","string",255);
   public static FieldValidator policyItemAmt= new FieldValidator("Amount","money16.2",-1);
   public static FieldValidator policyCCy= new FieldValidator("Currency","string",3);
   public static FieldValidator policyDesc= new FieldValidator("Description","string",255);
}
