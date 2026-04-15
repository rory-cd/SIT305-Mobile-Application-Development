package com.rorycd.sportnewsfeed;

public class Article {
    private int id, imgResId;
    private String title, content;

    public Article(int id, int imgResId, String title, String content) {
        this.id = id;
        this.imgResId = imgResId;
        this.title = title;
        this.content = content;
    }

    public int getId() { return id; }
    public int setId(int id) { return this.id = id; }
    public int getImgResId() { return imgResId; }
    public int setImgResId(int id) { return this.imgResId = id; }
    public String getTitle() { return title; }
    public String setTitle(String title) { return this.title = title; }
    public String getContent() { return content; }
    public String setContent(String content) { return this.content = content; }
}
