/**
 * 
 */
package com.webfin.utilities.dbf;

/**
 * @author joseph mcverry
 *
 */
import org.xBaseJ.*;
import org.xBaseJ.fields.CharField;
import org.xBaseJ.fields.LogicalField;

public class example3 {

	public static void main(String args[]){
	
		try{
			//Create a new dbf file
			DBF aDB=new DBF("e:\\teacher.dbf","N");
                        
                        Util.setxBaseJProperty("fieldFilledWithSpaces","true"); 
			Util.setxBaseJProperty("ignoreDBFLengthCheck","true"); 
			Util.setxBaseJProperty("trimFields","true");

                        /*
			//Define fields 
			CharField teacherId = new CharField("teacherId",9);
			CharField teacherNm = new CharField("teacherNm",25);
			CharField dept = new CharField("dept",4);
            LogicalField tenure = new LogicalField("tenure");
			
			//Add fields to database 
			aDB.addField(teacherId);
			aDB.addField(teacherNm);
			aDB.addField(dept);
            aDB.addField(tenure);
			
			aDB.createIndex("e:\\teacher.ndx","teacherID",true,true);     // true - delete NDX, false - unique ID
			System.out.println("index created");*/
			
			
            // fields are referenced through the database object
		/*	aDB.getField("teacherId").put("120120120");
            aDB.getField("teacherNm").put("Joanna Coffee");
			
            aDB.getField("dept").put("0800");
            ((LogicalField) aDB.getField("tenure")).put(true);
			
			aDB.write();

            // fields are referenced by their local variables
			teacherId.put("321123120");
            teacherNm.put("Juan Veldazou");
			
            dept.put("0810");
            tenure.put(true);
			
			aDB.write();
                        
                        teacherId.put("300020000");
            teacherNm.put("Exal De Cuau");
			
            dept.put("0830");
            tenure.put(false);

            // fields referenced both ways
			aDB.getField("teacherId").put("300020000");
            aDB.getField("teacherNm").put("Exal De' Cuau");
			
            dept.put("0810");
            tenure.put(false);
			
			aDB.write();*/
                        
                        //tes
                        aDB.getField("teacherId").put("120120120");
            aDB.getField("teacherNm").put("heru");

            aDB.getField("dept").put("0800");
            ((LogicalField) aDB.getField("tenure")).put(true);

			aDB.write();

            // fields are referenced by their local variables
			aDB.getField("teacherId").put("1234567");
            aDB.getField("teacherNm").put("prasetyo");

            aDB.getField("dept").put("0189");
            ((LogicalField) aDB.getField("tenure")).put(false);

			aDB.write();

			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
			
		
		
