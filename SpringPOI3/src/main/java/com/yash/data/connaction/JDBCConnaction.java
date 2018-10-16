package com.yash.data.connaction;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCConnaction {
	public Connection getConnection() {
		Connection con = null;
		String ip = "localhost";
		String port = "3306";
		String dbName = "mydb";
		String username = "root";
		String password = "root";
		String DB_URL = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "";
		System.out.println("DB_URL == " + DB_URL);
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}

		try {
			con = DriverManager.getConnection(DB_URL, username, password);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return con;
	}

}
