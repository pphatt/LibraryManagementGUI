package org.layout.APIHandleUtils;

public class Manga {
    private String uuid;
    private String title;
    private String author;
    private String genre;
    private String status;
    private String yearRelease;
    private String description;
    private String cover;
    private Integer chapters;

    public Manga(String uuid, String title, String author, String genre, String status, String yearRelease, String description, String cover, Integer chapters) {
        this.uuid = uuid;
        this.author = author;
        this.genre = genre;
        this.status = status;
        this.yearRelease = yearRelease;
        this.title = title;
        this.description = description;
        this.cover = cover;
        this.chapters = chapters;
    }

    public String getUuid() {
        return uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getYearRelease() {
        return yearRelease;
    }

    public void setYearRelease(String yearRelease) {
        this.yearRelease = yearRelease;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Integer getChapters() {
        return chapters;
    }

    public void setChapters(Integer chapters) {
        this.chapters = chapters;
    }
}
