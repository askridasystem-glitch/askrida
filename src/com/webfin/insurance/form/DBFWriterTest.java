package com.webfin.insurance.form;

import com.linuxense.javadbf.*;
import java.io.*; 
import java.util.Date;

public class DBFWriterTest {

public static void main(String args[]) throws DBFException, IOException {

		System.setSecurityManager(null);
		
		//add records
		
		DBFWriter writer = new DBFWriter(new File("c:\\test.dbf"));
		/*
		Object rowData[] = new Object[3];
		//rowData[0] = "1000";
		//rowData[1] = "";
		rowData[2] = new Double(5000.00);
		
		writer.addRecord(rowData);
		writer.write();
		
		*/
		//create dbf file
		
		//DBFField fields[] = new DBFField[1];
		
		/*fields[0] = new DBFField();
		fields[0].setName("emp_code");
		fields[0].setDataType(DBFField.FIELD_TYPE_C);
		fields[0].setFieldLength(10);
		
		fields[1] = new DBFField();
		fields[1].setName("emp_name");
		fields[1].setDataType(DBFField.FIELD_TYPE_C);
		fields[1].setFieldLength(20);
		
		fields[2] = new DBFField();
		fields[2].setName("salary");
		fields[2].setDataType(DBFField.FIELD_TYPE_N);
		fields[2].setFieldLength(12);
		fields[2].setDecimalCount(2);
		
		DBFWriter writer = new DBFWriter();
		writer.setFields(fields);
		*/
		Object rowData[] = new Object[3];
		rowData[0] = new Date();
		rowData[1] = "040000";
		rowData[2] = new Double(5000.00);
		
		//writer.addRecord(rowData);
		
		
		
		writer.addRecord(rowData);
		writer.write();
		
		//FileOutputStream fos = new FileOutputStream("c:\\test.dbf");
		//writer.write(fos);
		//fos.close();
		}
}