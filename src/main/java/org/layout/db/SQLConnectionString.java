package org.layout.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class SQLConnectionString {
    static Connection con = null;

    public static Connection getConnection() {
        if (con != null) return con;

        Properties prop = new Properties();
        String fileName = "app.properties";

        try (FileInputStream fis = new FileInputStream(fileName)) {
            prop.load(fis);
        } catch (IOException ignored) {
        }

        return getConnection(
                prop.getProperty("app.db_host"),
                prop.getProperty("app.port"),
                prop.getProperty("app.db_name"),
                prop.getProperty("app.db_username"),
                prop.getProperty("app.db_password")
        );
    }

    private static Connection getConnection(String db_host, String port, String db_name, String username, String password) {
        try {
            String connectionUrl =
                    "jdbc:sqlserver://" + db_host + ":" + port + ";"
                            + "databaseName=" + db_name + ";"
                            + "user=" + username + ";"
                            + "password=" + password + ";"
                            + "encrypt=true;"
                            + "trustServerCertificate=true;"
                            + "loginTimeout=30;";

            con = DriverManager.getConnection(connectionUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return con;
    }
}
