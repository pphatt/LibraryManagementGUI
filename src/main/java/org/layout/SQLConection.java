package org.layout;

import java.sql.*;

class SQLConnection {
    public static void main(String[] args) {
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

        try (Connection connection = DriverManager.getConnection(connectionUrl)) {

            // Create and execute a SELECT SQL statement.
//            String selectSql = "SELECT * from Book";
//            resultSet = statement.executeQuery(selectSql);
//            PreparedStatement a = connection.prepareStatement("SELECT * FROM Author where Author.ID = ?");
//            a.setString(1, "1");
//            resultSet = a.executeQuery();
//
//            while (resultSet.next()) {
//                System.out.println(resultSet.getString(1));
//                System.out.println(resultSet.getString(2));
//                System.out.println(resultSet.getString(3));
//            }

            PreparedStatement authorQuery = connection.prepareStatement("Select * from Author where " + "Author.ID = ?");
            authorQuery.setString(1, "2");
            ResultSet authorRes = authorQuery.executeQuery();

            if (!authorRes.next()) {
                PreparedStatement a = connection.prepareStatement("Insert into Author (ID, Name, State) values ('2', 'Yamamoto', '0')");
                a.execute();
            }

            String uuid = "123";
            String title = "Chainsaw";
            String authorID = "2";
            String status = "og";
            String yearRelease = "12";
            String description = "not available";
            String coverPath = "not available";

            PreparedStatement insertBookQuery = connection.prepareStatement(
                    "Insert into Book (ID, Title, Author, Genre, Status, YearReleased, Description, Cover, Chapter, State) values " +
                            "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );

            insertBookQuery.setString(1, uuid);
            insertBookQuery.setString(2, title);
            insertBookQuery.setString(3, authorID);
            insertBookQuery.setString(4, "1");
            insertBookQuery.setString(5, status);
            insertBookQuery.setString(6, yearRelease);
            insertBookQuery.setString(7, description);
            insertBookQuery.setString(8, coverPath);
            insertBookQuery.setString(9, "12");
            insertBookQuery.setString(10, "0");
            insertBookQuery.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

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
