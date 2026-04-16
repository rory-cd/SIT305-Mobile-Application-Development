package com.rorycd.istream.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "playlists",
    foreignKeys = @ForeignKey(
        entity = User.class,
        parentColumns = "id",
        childColumns = "userId",
        onDelete = ForeignKey.CASCADE
    )
)
public class PlaylistItem {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userId;

    @NonNull
    public String url;

    public PlaylistItem(int userId, @NonNull String url) {
        this.userId = userId;
        this.url = url;
    }
}
