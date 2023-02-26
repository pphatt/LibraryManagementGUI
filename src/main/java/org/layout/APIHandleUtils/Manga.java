package org.layout.APIHandleUtils;

public class Manga {
    private String uuid;
    private String title;
    private String description;
    private String cover;
    private Integer chapters;

    public Manga(String uuid, String title, String description, String cover, Integer chapters) {
        this.uuid = uuid;
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

    public String getDescription() {
        return description;
    }

    public String getCover() {
        return cover;
    }

    public Integer getChapters() {
        return chapters;
    }
}
