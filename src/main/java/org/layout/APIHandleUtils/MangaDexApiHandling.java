package org.layout.APIHandleUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;

public class MangaDexApiHandling {
    public static ArrayList<Manga> mangaArray = new ArrayList<>();
    public static ArrayList<Integer> chapterArray = new ArrayList<>();

    public MangaDexApiHandling(int amount) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.mangadex.org/manga?limit=" + amount +
                        "&includes[]=cover_art&includes[]=author&contentRating[]=safe&contentRating[]=suggestive"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(MangaDexApiHandling::getManga)
                .join();

        for (int i = 0; i < amount; i++) {
            System.out.println("Uuid: " + mangaArray.get(i).getUuid());
            System.out.println("Title: " + mangaArray.get(i).getTitle());
            System.out.println("Author: " + mangaArray.get(i).getAuthor());
            System.out.println("Genre: " + mangaArray.get(i).getGenre());
            System.out.println("Status: " + mangaArray.get(i).getStatus());
            System.out.println("Year Release: " + mangaArray.get(i).getYearRelease());
            System.out.println("Description: " + mangaArray.get(i).getDescription());
            System.out.println("Cover: " + mangaArray.get(i).getCover());
            System.out.println("Chapter: " + mangaArray.get(i).getChapters());
        }
    }

    public static void getManga(String responseBody) {
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

            mangaArray.add(new Manga(uuid, title, author, genre, status, yearRelease, description, coverPath, chapterArray.get(i)));
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
