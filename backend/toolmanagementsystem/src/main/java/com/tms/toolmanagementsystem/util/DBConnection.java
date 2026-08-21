package com.tms.toolmanagementsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    public static void ensureToolInstanceSerialIndex() {
        try (Connection con = getConnection();
             Statement stmt = con.createStatement()) {

            String indexCheckSql = "SELECT index_name, GROUP_CONCAT(column_name ORDER BY seq_in_index) AS columns " +
                    "FROM information_schema.statistics " +
                    "WHERE table_schema = DATABASE() AND table_name = 'tool_instance' " +
                    "GROUP BY index_name";

            boolean hasLegacySerialIndex = false;
            boolean hasCompositeSerialIndex = false;

            try (ResultSet rs = stmt.executeQuery(indexCheckSql)) {
                while (rs.next()) {
                    String indexName = rs.getString("index_name");
                    String columns = rs.getString("columns");
                    if ("serial_number".equals(indexName) || "serial_number_2".equals(indexName)) {
                        hasLegacySerialIndex = true;
                    }
                    if ("tool_id,serial_number".equals(columns) || "serial_number,tool_id".equals(columns)) {
                        hasCompositeSerialIndex = true;
                    }
                }
            }

            if (hasLegacySerialIndex && !hasCompositeSerialIndex) {
                try (PreparedStatement dropLegacy = con.prepareStatement("ALTER TABLE tool_instance DROP INDEX serial_number")) {
                    dropLegacy.execute();
                } catch (SQLException ignored) {
                    // The legacy name may already be serial_number_2 or not present in this schema version.
                }

                try (PreparedStatement dropLegacy2 = con.prepareStatement("ALTER TABLE tool_instance DROP INDEX serial_number_2")) {
                    dropLegacy2.execute();
                } catch (SQLException ignored) {
                    // The second legacy index is optional depending on the schema.
                }

                try (PreparedStatement addComposite = con.prepareStatement(
                        "ALTER TABLE tool_instance ADD UNIQUE KEY uq_tool_serial (tool_id, serial_number)")) {
                    addComposite.execute();
                }
            } else if (!hasLegacySerialIndex && !hasCompositeSerialIndex) {
                try (PreparedStatement addComposite = con.prepareStatement(
                        "ALTER TABLE tool_instance ADD UNIQUE KEY uq_tool_serial (tool_id, serial_number)")) {
                    addComposite.execute();
                }
            }
        } catch (SQLException e) {
            // Ignore migration issues during startup; the app will still attempt the insert using the current schema.
            e.printStackTrace();
        }
    }

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
