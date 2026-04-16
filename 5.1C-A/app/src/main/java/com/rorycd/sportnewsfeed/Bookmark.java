package com.rorycd.sportnewsfeed;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookmarks")
public class Bookmark {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int articleId;

    public Bookmark(int articleId) {
        this.articleId = articleId;
    }
}
