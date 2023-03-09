package org.layout.db;

public class SQLConnectionString {
    public static String getConnectionString() {
        String connectionUrl =
                "jdbc:sqlserver://DESKTOP-0FQDJF2\\SQLEXPRESS:1433;"
                        + "databaseName=LibraryManagement;"
                        + "user=phat;"
                        + "password=nhattan1811090;"
                        + "encrypt=true;"
                        + "trustServerCertificate=true;"
                        + "loginTimeout=30;";

        return connectionUrl;
    }
}
