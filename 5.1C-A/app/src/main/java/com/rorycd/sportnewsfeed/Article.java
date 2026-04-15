package com.rorycd.sportnewsfeed;

public class Article {
    private int id, imgResId;
    private String title, category, content;

    public Article(int id, int imgResId, String title, String category, String content) {
        this.id = id;
        this.imgResId = imgResId;
        this.title = title;
        this.category = category;
        this.content = content;
    }

    public int getId() { return id; }
    public int setId(int id) { return this.id = id; }
    public int getImgResId() { return imgResId; }
    public int setImgResId(int id) { return this.imgResId = id; }
    public String getTitle() { return title; }
    public String setTitle(String title) { return this.title = title; }
    public String getCategory() { return category; }
    public String setCategory(String title) { return this.category = category; }
    public String getContent() { return content; }
    public String setContent(String content) { return this.content = content; }
}
