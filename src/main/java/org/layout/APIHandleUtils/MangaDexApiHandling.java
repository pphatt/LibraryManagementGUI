package org.layout.APIHandleUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.layout.MainLayoutGUI;

import javax.swing.table.DefaultTableModel;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

import org.layout.db.SQLConnectionString;

public class MangaDexApiHandling {
    public static ArrayList<Manga> mangaArray = new ArrayList<>();
    public static ArrayList<Integer> chapterArray = new ArrayList<>();
    public static boolean state = false;

    public MangaDexApiHandling(int amount) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.mangadex.org/manga?limit=" + amount +
                        "&includes[]=cover_art&includes[]=author&contentRating[]=safe&contentRating[]=suggestive"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(MangaDexApiHandling::getManga);
    }

    public static void getManga(String responseBody) {
        DefaultTableModel a = (DefaultTableModel) MainLayoutGUI.contentTable.getTable().getModel();
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray mangas = new JSONArray(jsonObject.getJSONArray("data"));

        for (int i = 0; i < mangas.length(); i++) {
            JSONObject mangaObject = mangas.getJSONObject(i);
            String uuid = mangaObject.getString("id");

            JSONObject attributes = mangaObject.getJSONObject("attributes");
            JSONArray relationships = mangaObject.getJSONArray("relationships");

            String title = "Not available";

            try {
                title = attributes.getJSONObject("title")
                        .getString("en").trim();
            } catch (JSONException ignored) {
            }

            String author = "Not available";

            try {
                author = relationships.getJSONObject(0)
                        .getJSONObject("attributes")
                        .getString("name").trim();
            } catch (JSONException ignored) {
            }

            String authorID = "";

            try {
                authorID = relationships.getJSONObject(0)
                        .getString("id").trim();
            } catch (JSONException ignored) {
            }

            String genre = "Not available";

            try {
                genre = mangaObject.getString("type");
            } catch (JSONException ignored) {
            }

            String status = "Not available";

            try {
                status = attributes.getString("status");
            } catch (JSONException ignored) {
            }

            String yearRelease = "Not available";

            try {
                yearRelease = attributes.getNumber("year").toString();
            } catch (JSONException ignored) {
            }

            String description = "Not available";

            try {
                description = attributes.getJSONObject("description")
                        .getString("en")
                        .trim()
                        .replaceAll("(\n|\r\n|;)", " ");
            } catch (JSONException ignored) {
            }

            String coverPath = "Not available";

            try {
                coverPath = relationships.getJSONObject(relationships.length() - 1)
                        .getJSONObject("attributes")
                        .getString("fileName");
            } catch (JSONException ignored) {
            }

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.mangadex.org/manga/" + uuid + "/aggregate"))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(MangaDexApiHandling::getMangaChapter)
                    .join();

            try (Connection connection = DriverManager.getConnection(SQLConnectionString.getConnectionString())) {
                PreparedStatement checkBookIDDup = connection.prepareStatement("Select * from Book where " + "Book.ID = ?");
                checkBookIDDup.setString(1, uuid);
                ResultSet checkBookIDDupRes = checkBookIDDup.executeQuery();

                if (checkBookIDDupRes.next()) {
                    a.addRow(new Object[]{uuid, title, author, genre, status, yearRelease, chapterArray.get(i)});
                    MainLayoutGUI.contentTable.getTable().setModel(a);

                    mangaArray.add(new Manga(uuid, title, author, genre, status, yearRelease, description, coverPath, chapterArray.get(i)));
                    continue;
                }

                PreparedStatement authorQuery = connection.prepareStatement("Select * from Author where " + "Author.ID = ?");
                authorQuery.setString(1, authorID);
                ResultSet authorRes = authorQuery.executeQuery();

                if (!authorRes.next()) {
                    PreparedStatement insertAuthorQuery = connection.prepareStatement("Insert into Author (ID, Name, State) values (?, ?, '0')");
                    insertAuthorQuery.setString(1, authorID);
                    insertAuthorQuery.setString(2, author);
                    insertAuthorQuery.execute();
                }

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
                insertBookQuery.setString(9, chapterArray.get(i).toString());
                insertBookQuery.setString(10, "0");
                insertBookQuery.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            a.addRow(new Object[]{uuid, title, author, genre, status, yearRelease, chapterArray.get(i)});
            MainLayoutGUI.contentTable.getTable().setModel(a);

            mangaArray.add(new Manga(uuid, title, author, genre, status, yearRelease, description, coverPath, chapterArray.get(i)));
        }

        try (Connection connection = DriverManager.getConnection(SQLConnectionString.getConnectionString())) {
            PreparedStatement retrieveBook = connection.prepareStatement(
                    "Select Book.ID, Book.Title, Author.Name, Book.Genre, Book.Status, Book.YearReleased, Book.Description, Book.Cover, Book.Chapter from Book, Author where Book.Author = Author.ID");
            ResultSet retrieveBookRes = retrieveBook.executeQuery();

            while (retrieveBookRes.next()) {
                boolean c = false;
                for (int i = 0; i < mangaArray.size(); i++) {
                    if (mangaArray.get(i).getUuid().equals(retrieveBookRes.getString(1))) {
                        c = true;
                        break;
                    }
                }

                if (!c) {
                    mangaArray.add(new Manga(retrieveBookRes.getString(1),
                            retrieveBookRes.getString(2),
                            retrieveBookRes.getString(3),
                            retrieveBookRes.getString(4),
                            retrieveBookRes.getString(5),
                            retrieveBookRes.getString(6),
                            retrieveBookRes.getString(7),
                            retrieveBookRes.getString(8),
                            Integer.parseInt(retrieveBookRes.getString(9))));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        state = true;
    }

    public static void getMangaChapter(String responseBody) {
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject volumes = jsonObject.getJSONObject("volumes");
        int count = 0;

        Iterator<String> keys = volumes.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            String a = volumes.get(key).toString();
            JSONObject b = new JSONObject(a);
            count += b.getInt("count");
        }

        chapterArray.add(count);
    }
}
