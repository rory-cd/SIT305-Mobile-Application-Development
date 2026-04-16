package com.rorycd.sportnewsfeed;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Bookmark bookmark);

    @Query("DELETE from bookmarks WHERE articleId = :articleId")
    void delete(int articleId);

    @Query("SELECT EXISTS(SELECT * from bookmarks WHERE articleId = :articleId)")
    boolean isBookmarked(int articleId);

    @Query("SELECT articleId from bookmarks")
    int[] getAllBookmarks();
}
