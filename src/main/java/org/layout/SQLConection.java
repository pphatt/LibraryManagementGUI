package org.layout;

import org.layout.db.SQLConnectionString;

import java.sql.*;

class SQLConnection {
    public static void main(String[] args) throws SQLException {
        // create statement is for non-param queries
        // prepare statement is for param queries

        String connectionUrl =
                "jdbc:sqlserver://DESKTOP-0FQDJF2\\SQLEXPRESS:1433;"
                        + "databaseName=LibraryManagement;"
                        + "user=phat;"
                        + "password=nhattan1811090;"
                        + "encrypt=true;"
                        + "trustServerCertificate=true;"
                        + "loginTimeout=30;";

        ResultSet resultSet = null;

//        PreparedStatement insertBookQuery = SQLConnectionString.getConnection().prepareStatement(
//                "Insert into Author (ID, Name, State) values " +
//                        "(?, ?, ?)"
//        );
//
//        insertBookQuery.setString(1, "1");
//        insertBookQuery.setString(2, "1");
//        insertBookQuery.setString(3, "0");
//        insertBookQuery.executeUpdate();

        PreparedStatement insertBookQuery = SQLConnectionString.getConnection().prepareStatement(
                "Insert into Type (Name) values " +
                        "(?)"
        );

        insertBookQuery.setString(1, "manga");
        insertBookQuery.executeUpdate();

//        String insertSql = "INSERT INTO Book (ID, Title, Author, Genre, Status, YearReleased, Description, Cover, Chapter, State) VALUES "
//                + "('NewBike', 'BikeNew', 'Blue', 50, 120, '2016-01-01');";
//
//        ResultSet resultSet = null;
//
//        try (Connection connection = DriverManager.getConnection(connectionUrl);
//             PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);) {
//
//            prepsInsertProduct.execute();
//            // Retrieve the generated key from the insert.
//            resultSet = prepsInsertProduct.getGeneratedKeys();
//
//            // Print the ID of the inserted row.
//            while (resultSet.next()) {
//                System.out.println("Generated: " + resultSet.getString(1));
//            }
//        }
//        // Handle any errors that may have occurred.
//        catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
