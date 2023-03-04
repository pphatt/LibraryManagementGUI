package org.layout.APIHandleUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.layout.ContentScroll;
import org.layout.MainLayoutGUI;

import javax.swing.table.DefaultTableModel;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class MangaDexApiHandling {
    public static ArrayList<Manga> mangaArray = new ArrayList<>();
    public static ArrayList<Integer> chapterArray = new ArrayList<>();
    public static ContentScroll scrollPaneContent = new ContentScroll(mangaArray);

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
        DefaultTableModel a = (DefaultTableModel) scrollPaneContent.getTable().getModel();
        ArrayList<Manga> ab = MainLayoutGUI.readData();
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

            a.addRow(new Object[]{title, author, genre, status, yearRelease, chapterArray.get(i)});
            scrollPaneContent.getTable().setModel(a);

            mangaArray.add(new Manga(uuid, title, author, genre, status, yearRelease, description, coverPath, chapterArray.get(i)));
        }

        if (ab != null) {
            for (Manga value : ab) {
                boolean c = true;

                for (Manga manga : mangaArray) {
                    if (manga.getUuid().equals(value.getUuid())) {
                        c = false;
                        break;
                    }
                }

                if (c) {
                    mangaArray.add(value);
                    a.addRow(new Object[]{value.getTitle(), value.getAuthor(), value.getGenre(), value.getStatus(), value.getYearRelease(), value.getChapters()});
                    scrollPaneContent.getTable().setModel(a);
                }
            }
        }

        MainLayoutGUI.reWriteEntireData(mangaArray);
    }

    public ContentScroll getUpdateScrollPane() {
        return scrollPaneContent;
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
