/**
 *
 */
package com.webfin.insurance.form;

/**
 * @author joseph mcverry
 *
 */



import org.xBaseJ.*;
import org.xBaseJ.fields.CharField;
import org.xBaseJ.fields.LogicalField;
import org.xBaseJ.fields.NumField;
import org.xBaseJ.Util;

public class example1 {


	public static void main(String args[]){


		try{
			//Create a new dbf file
			DBF aDB=new DBF("e:\\teacher.dbf","N");

			//Define fields
			/*CharField teacherId = new CharField("teacherId",9);
			CharField teacherNm = new CharField("teacherNm",25);
			CharField dept = new CharField("dept",4);
            LogicalField tenure = new LogicalField("tenure");

			//Add fields to database
			aDB.addField(teacherId);
			aDB.addField(teacherNm);
			aDB.addField(dept);
            aDB.addField(tenure);
			*/
			//aDB.createIndex("teacher.ndx","teacherID",true,true);     // true - delete NDX, false - unique ID
			//System.out.println("index created");
			Util.setxBaseJProperty("fieldFilledWithSpaces","true"); 
			Util.setxBaseJProperty("ignoreDBFLengthCheck","true"); 
			Util.setxBaseJProperty("trimFields","true");
			
            // fields are referenced through the database object
			aDB.getField("teacherId").put("120120120");
            aDB.getField("teacherNm").put("Joanna Coffee");

            aDB.getField("dept").put("0800");
            ((LogicalField) aDB.getField("tenure")).put(true);

			aDB.write();

            // fields are referenced by their local variables
			aDB.getField("teacherId").put("1234567");
            aDB.getField("teacherNm").put("doni latupelisa");

            aDB.getField("dept").put("0189");
            ((LogicalField) aDB.getField("tenure")).put(false);

			aDB.write();

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
