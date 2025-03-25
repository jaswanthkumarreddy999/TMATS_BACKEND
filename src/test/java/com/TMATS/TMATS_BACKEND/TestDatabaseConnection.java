package com.TMATS.TMATS_BACKEND;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDatabaseConnection {

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://monorail.proxy.rlwy.net:51483/railway?allowPublicKeyRetrieval=true&useSSL=false";
        String username = "root";
        String password = "xRPDdaywuhHuYNTeMxBcinfatNyKJRhD";

        System.out.println("Testing connection to Railway MySQL database...");
        
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Attempt to establish a connection
            System.out.println("Connecting to: " + jdbcUrl);
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            
            System.out.println("Connection successful!");
            System.out.println("Database product: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("Database version: " + connection.getMetaData().getDatabaseProductVersion());
            
            // Close the connection
            connection.close();
            System.out.println("Connection closed.");
            
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Connection failed! Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 