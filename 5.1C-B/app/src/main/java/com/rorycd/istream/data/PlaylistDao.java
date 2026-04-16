package com.rorycd.istream.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addItem(PlaylistItem item);

    @Query("SELECT url FROM playlists WHERE userId = :userId")
    List<String> getPlaylistForUser(int userId);
}
