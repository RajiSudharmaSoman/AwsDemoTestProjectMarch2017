package com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.property.PropertyHolder;
import com.utils.SystemVariables;

@Component
public class AmazonDaoImp implements AmazonDao {
	private static Logger LOGGER = Logger
			.getLogger(AmazonDaoImp.class);
	
	@Autowired
	PropertyHolder propertyHolder;
	
	@Autowired
	SystemVariables systemVar;
	
	public String saveJson(String s3Text) {
		// TODO Auto-generated method stub
		try {
			LOGGER.info("Loading driver...");
			Class.forName("com.mysql.jdbc.Driver");
			LOGGER.info("Driver loaded!");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Cannot find the driver in the classpath!", e);
		}

		Connection conn = null;
		Statement setupStatement = null;
	

		try {
			// Create connection to RDS DB instance
			conn = DriverManager.getConnection(systemVar.getJdbcUrl());			// Create a table and write two rows
			setupStatement = conn.createStatement();
			//String createTable = "CREATE TABLE S3DataTable (jsonString char(200));";
			
			String insertRow1 = "INSERT INTO S3DataTable (jsonString) VALUES ('"+s3Text+"')";
			//setupStatement.addBatch(createTable);
			setupStatement.addBatch(insertRow1);
			setupStatement.executeBatch();
			setupStatement.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
			// handle any errors
			LOGGER.info("SQLException: " + ex.getMessage());
			LOGGER.info("SQLState: " + ex.getSQLState());
			LOGGER.info("VendorError: " + ex.getErrorCode());
		} finally {
			System.out.println("Closing the connection.");
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ignore) {
				}
		}

		return s3Text;
	}

}
