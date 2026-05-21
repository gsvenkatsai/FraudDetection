package com.frauddetection;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@WebListener
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Checking database initialization...");
        
        int retries = 0;
        boolean connected = false;
        
        while (retries < 10 && !connected) {
            try (Connection conn = DatabaseConnection.getConnection();
                 Statement stmt = conn.createStatement()) {
                
                connected = true;
                // Check if Users table exists
                ResultSet rs = conn.getMetaData().getTables(null, null, "users", null);
                if (!rs.next()) {
                    System.out.println("Tables not found. Initializing database...");
                    DatabaseInitializer.initialize();
                } else {
                    System.out.println("Database already initialized.");
                }
            } catch (Exception e) {
                retries++;
                System.out.println("Database not ready yet (attempt " + retries + "/10). Waiting 3 seconds...");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        
        if (!connected) {
            System.err.println("Could not connect to database after several attempts. Initialization failed.");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
