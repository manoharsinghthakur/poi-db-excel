package com.spring.SpringPOI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mysql.fabric.xmlrpc.base.Array;
import com.yash.data.connaction.JDBCConnaction;

public class POIExcelReadData {
	private static final String FILE_NAME = "ExcelFile.xlsx";
	JDBCConnaction connection = new JDBCConnaction();

	public void dbToExcel()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

		Connection con = connection.getConnection();
		Statement statement = con.createStatement();
		ResultSet resultSet = statement.executeQuery("select * from data_type");

		XSSFWorkbook workBook = new XSSFWorkbook();
		XSSFSheet sheet = workBook.createSheet("java Data types");

		System.out.println("creating excel");

		XSSFRow rowHead = sheet.createRow(0);
		rowHead.createCell(0).setCellValue("data type");
		rowHead.createCell(1).setCellValue("types");
		rowHead.createCell(2).setCellValue("size (in bytes)");

		int rowNumber = 1;

		while (resultSet.next()) {
			XSSFRow row = sheet.createRow(rowNumber);

			row.createCell(0).setCellValue(resultSet.getString(1));
			row.createCell(1).setCellValue(resultSet.getString(2));
			row.createCell(2).setCellValue(resultSet.getString(3));
			rowNumber++;
		}

		try {
			FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
			workBook.write(outputStream);
			workBook.close();
			con.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("done");
	}

	public void excelToDb() {
		try {

			FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet dataTypeSheet = workbook.getSheetAt(0);

			Iterator<Row> iterator = dataTypeSheet.iterator();

			while (iterator.hasNext()) {
				Row currentRow = iterator.next();
				if (currentRow.getRowNum() == 0) {
					continue;
				}

				Iterator<Cell> cellIterator = currentRow.iterator();
				ArrayList<Object> tempArrayList = new ArrayList<Object>();
				int i = 0;
				while (cellIterator.hasNext()) {
					Cell c = cellIterator.next();
					try {
						System.out.println("Cell value is =" + c.getStringCellValue());
						tempArrayList.add(c.getStringCellValue().trim());
					} catch (NullPointerException NE) {
						System.out.println("Cell is Null:" + i + " - " + NE);
						tempArrayList.add("");
					}

					i++;
				}
				
				String insertParam = new String();
				Iterator<Object> _iterator = tempArrayList.iterator();
				while (_iterator.hasNext()) {
					String ss = (String) _iterator.next();
					insertParam = insertParam + "," + "'" + ss + "'";
				}
				insertParam = insertParam.substring(1, insertParam.length());

				String sql = "INSERT INTO data_type VALUES(" + insertParam + ")";
				System.out.println("sql ::" + sql);

				try {
					try {
						Connection con = connection.getConnection();
						Statement st = con.createStatement();
						st.executeUpdate(sql);
						con.close();
					} catch (Exception e) {
						System.err.println("Got an exception! ");
						System.err.println(e.getMessage());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		POIExcelReadData object = new POIExcelReadData();
		object.excelToDb();
	}
}
