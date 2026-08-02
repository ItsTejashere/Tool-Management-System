package com.tms.toolmanagementsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    public static Connection getConnection() throws SQLException {
        // 1. Try to read the Railway Environment Variables first
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        // 2. If they are null (because you are running it locally on your laptop), use your local XAMPP credentials
        if (url == null || url.trim().isEmpty()) {
            url = "jdbc:mysql://localhost:3306/tool_management";
            user = "root";
            password = "admin"; 
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
    }
}
