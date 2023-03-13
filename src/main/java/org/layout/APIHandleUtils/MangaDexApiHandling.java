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
import java.util.Objects;

import org.layout.db.SQLConnectionString;

public class MangaDexApiHandling {
    public static ArrayList<Manga> mangaArray = new ArrayList<>();
    public static ArrayList<ArrayList<String>> tags = new ArrayList<>();
    public static ArrayList<Integer> chapterArray = new ArrayList<>();
    public static boolean state = false;

    /*
    * TODO:
    *  - Think about how to edit with this implementation of sql
    *  - Perf and refactor code
    *  - Think about a better approach about access data ? should we use sql query instead of arraylist ? something like that...
    * */

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
        DefaultTableModel model = (DefaultTableModel) MainLayoutGUI.contentTable.getTable().getModel();
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

            String type = "Not available";

            try {
                type = mangaObject.getString("type");
            } catch (JSONException ignored) {
            }

            JSONArray ab = attributes.getJSONArray("tags");
            ArrayList<ArrayList<String>> tag = new ArrayList<>();

            for (int j = 0; j < ab.length(); j++) {
                ArrayList<String> t = new ArrayList<>();

                JSONObject b = ab.getJSONObject(j);
                String tagID = b.getString("id");
                JSONObject tagAttr = b.getJSONObject("attributes");
                JSONObject tagNameObj = tagAttr.getJSONObject("name");
                String tagName = tagNameObj.getString("en");

                t.add(tagID);
                t.add(tagName);
                tag.add(t);
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

            try {
                if (Boolean.TRUE.equals(checkBookAlreadyExists(model, uuid, title, author, type, status, yearRelease,
                        description, coverPath, chapterArray.get(i)))) {
                    continue;
                }

                checkDBAuthor(authorID, author);
                checkDBType(type);

                addBookToDB(uuid, title, authorID, status, yearRelease, description, coverPath, chapterArray.get(i).toString());

                checkBookGenre(tag, uuid);
            } catch (Exception e) {
            }

            model.addRow(new Object[]{uuid, title, author, type, status, yearRelease, chapterArray.get(i)});
            MainLayoutGUI.contentTable.getTable().setModel(model);

            mangaArray.add(new Manga(uuid, title, author, type, status, yearRelease, description, coverPath, chapterArray.get(i)));
        }

        checkDBBook();
        state = true;
    }

    public static void checkDBType(String type) {
        try {
            PreparedStatement typeQuery = SQLConnectionString.getConnection().prepareStatement("Select * from Type where " + "Type.Name = ?");
            typeQuery.setString(1, type);
            ResultSet typeRes = typeQuery.executeQuery();

            if (!typeRes.next()) {
                PreparedStatement insertTypeQuery = SQLConnectionString.getConnection().prepareStatement("Insert into Type (Name) values (?)");
                insertTypeQuery.setString(1, type);
                insertTypeQuery.executeUpdate();
            }
        } catch (SQLException ignored) {
        }
    }

    public static void checkDBAuthor(String authorID, String authorName) {
        try {
            PreparedStatement authorQuery = SQLConnectionString.getConnection().prepareStatement("Select * from Author where " + "Author.ID = ?");
            authorQuery.setString(1, authorID);
            ResultSet authorRes = authorQuery.executeQuery();

            if (!authorRes.next()) {
                PreparedStatement insertAuthorQuery = SQLConnectionString.getConnection().prepareStatement("Insert into Author (ID, Name, State) values (?, ?, '0')");
                insertAuthorQuery.setString(1, authorID);
                insertAuthorQuery.setString(2, authorName);
                insertAuthorQuery.executeUpdate();
            }
        } catch (SQLException ignored) {
        }
    }

    public static void checkDBBook() {
        try {
            PreparedStatement retrieveBook = SQLConnectionString.getConnection().prepareStatement(
                    "Select Book.ID, Book.Title, Author.Name, Book.Type, Book.Status, Book.YearReleased, Book.Description, Book.Cover, Book.Chapter, Book.State from Book, Author where Book.Author = Author.ID");
            ResultSet retrieveBookRes = retrieveBook.executeQuery();

            while (retrieveBookRes.next()) {
                if (Objects.equals(retrieveBookRes.getString(10), "1")) {
                    continue;
                }

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
        } catch (Exception ignored) {
        }
    }

    public static void checkBookGenre(ArrayList<ArrayList<String>> tag, String uuid) {
        ArrayList<String> t = new ArrayList<>();

        try {
            for (ArrayList<String> strings : tag) {
                PreparedStatement checkGenre = SQLConnectionString.getConnection().prepareStatement("Select * from Genre where ID = ?");
                checkGenre.setString(1, strings.get(0));
                ResultSet checkGenreRes = checkGenre.executeQuery();

                if (!checkGenreRes.next()) {
                    PreparedStatement insertGenre = SQLConnectionString.getConnection().prepareStatement(
                            "Insert into Genre (ID, Name, State) values " +
                                    "(?, ?, ?)"
                    );

                    insertGenre.setString(1, strings.get(0));
                    insertGenre.setString(2, strings.get(1));
                    insertGenre.setString(3, "0");
                    insertGenre.executeUpdate();
                }

                PreparedStatement checkBookGenreExist = SQLConnectionString.getConnection().prepareStatement("Select * from BookGenre where BookID = ? and GenreID = ?");
                checkBookGenreExist.setString(1, uuid);
                checkBookGenreExist.setString(2, strings.get(0));
                ResultSet checkBookGenreExistRes = checkBookGenreExist.executeQuery();

                if (!checkBookGenreExistRes.next()) {
                    PreparedStatement insertBookGenre = SQLConnectionString.getConnection().prepareStatement(
                            "Insert into BookGenre (BookID, GenreID, State) values " +
                                    "(?, ?, ?)"
                    );

                    insertBookGenre.setString(1, uuid);
                    insertBookGenre.setString(2, strings.get(0));
                    insertBookGenre.setString(3, "0");
                    insertBookGenre.executeUpdate();
                }

                t.add(strings.get(1));
            }
        } catch (SQLException ignored) {
        }

        tags.add(t);
    }

    public static Boolean checkBookAlreadyExists(DefaultTableModel model, String uuid, String title, String author, String type, String status, String yearRelease, String description, String coverPath, Integer chapter) {
        try {
            PreparedStatement checkBookIDDup = SQLConnectionString.getConnection().prepareStatement("Select * from Book where " + "Book.ID = ?");
            checkBookIDDup.setString(1, uuid);
            ResultSet checkBookIDDupRes = checkBookIDDup.executeQuery();

            if (checkBookIDDupRes.next()) {
                if (Objects.equals(checkBookIDDupRes.getString(10), "1")) {
                    return true;
                }

                model.addRow(new Object[]{uuid, title, author, type, status, yearRelease, chapter});
                MainLayoutGUI.contentTable.getTable().setModel(model);

                mangaArray.add(new Manga(uuid, title, author, type, status, yearRelease, description, coverPath, chapter));
                return true;
            }

            return false;
        } catch (SQLException ignored) {
        }

        return null;
    }

    public static void addBookToDB(String uuid, String title, String authorID, String status, String yearRelease, String description, String coverPath, String chapter) {
        try {
            PreparedStatement insertBookQuery = SQLConnectionString.getConnection().prepareStatement(
                    "Insert into Book (ID, Title, Author, Type, Status, YearReleased, Description, Cover, Chapter, State) values " +
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
            insertBookQuery.setString(9, chapter);
            insertBookQuery.setString(10, "0");
            insertBookQuery.executeUpdate();
        } catch (SQLException ignored) {
        }
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
