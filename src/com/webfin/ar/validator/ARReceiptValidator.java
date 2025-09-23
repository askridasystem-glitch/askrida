/***********************************************************************
 * Module:  com.webfin.ar.validator.ARReceiptValidator
 * Author:  Denny Mahendra
 * Created: Oct 11, 2005 10:52:02 PM
 * Purpose: 
 ***********************************************************************/

package com.webfin.ar.validator;

import com.crux.util.validation.FieldValidator;

public class ARReceiptValidator {
   public static FieldValidator receiptNo = new FieldValidator("Receipt Number","string",64);
   public static FieldValidator lineAmount = new FieldValidator("Amount","money16.2",-1);
   public static FieldValidator currency = new FieldValidator("Currency","string",3);
   public static FieldValidator amount= new FieldValidator("Amount","money16.2",-1);
   public static FieldValidator rc= new FieldValidator("Method","string",-1);
   public static FieldValidator method= new FieldValidator("Account","string",-1);
}
