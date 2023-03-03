package org.layout;

import org.layout.APIHandleUtils.Manga;
import org.layout.APIHandleUtils.MangaDexApiHandling;

import java.io.*;
import java.util.ArrayList;

import static org.layout.APIHandleUtils.MangaDexApiHandling.mangaArray;

public class Main {
//    public static void reWriteEntireData(ArrayList<Manga> array) {
//        try (FileWriter fstream = new FileWriter("book-data.txt")) {
//            BufferedWriter data = new BufferedWriter(fstream);
//
//            for (int i = 0; i < array.size(); i++) {
//                data.write(array.get(i).getUuid() + ";");
//                data.write(array.get(i).getTitle() + ";");
//                data.write(array.get(i).getAuthor() + ";");
//                data.write(array.get(i).getGenre() + ";");
//                data.write(array.get(i).getStatus() + ";");
//                data.write(array.get(i).getYearRelease() + ";");
//                data.write(array.get(i).getDescription() + ";");
//                data.write(array.get(i).getCover() + ";");
//                data.write(array.get(i).getChapters() + System.lineSeparator());
//            }
//
//            data.close();
//        } catch (IOException ignored) {
//        }
//    }
//
//    public static ArrayList<Manga> readData() {
//        try (BufferedReader br = new BufferedReader(new FileReader("book-data.txt"))) {
//            ArrayList<Manga> arr = new ArrayList<>();
//            String line = br.readLine();
//
//            while (line != null) {
//                String[] sb = line.split(";");
//                int chapter = Integer.parseInt(sb[8]);
//                arr.add(new Manga(sb[0], sb[1], sb[2], sb[3], sb[4], sb[5], sb[6], sb[7], chapter));
//
//                line = br.readLine();
//            }
//
//            return arr;
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//
//        return null;
//    }
//
//    public static void main(String[] args) {
//        MangaDexApiHandling mangaDexApiHandling = new MangaDexApiHandling(20);
//        reWriteEntireData(mangaArray);
//        ArrayList<Manga> arr = readData();
//
//        assert arr != null;
//        for (Manga m : arr) {
//            System.out.println(m.getTitle());
//        }
//    }
}