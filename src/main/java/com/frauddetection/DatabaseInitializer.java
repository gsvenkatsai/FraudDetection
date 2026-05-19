package com.frauddetection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseInitializer {

    public static void initialize() {
        try (Connection conn = DatabaseConnection.getConnection();
             InputStream is = DatabaseInitializer.class.getClassLoader().getResourceAsStream("schema.sql");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            if (is == null) {
                System.err.println("schema.sql not found!");
                return;
            }

            String sql = reader.lines().collect(Collectors.joining("\n"));
            String[] commands = sql.split(";");

            try (Statement stmt = conn.createStatement()) {
                // Ensure alerts are cleared before re-initialization
                try {
                    stmt.execute("DELETE FROM Alerts");
                } catch (Exception e) {
                    // Ignore if table doesn't exist yet
                }

                for (String command : commands) {
                    if (!command.trim().isEmpty()) {
                        stmt.execute(command);
                    }
                }
            }
            System.out.println("Database initialized successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        initialize();
    }
}
