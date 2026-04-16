package com.rorycd.istream.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, PlaylistItem.class}, version = 1, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract PlaylistDao playlistDao();

    private static volatile UserDatabase INSTANCE;

    public static UserDatabase getInstance (Context context) {
        if (INSTANCE == null) {
            synchronized (UserDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            UserDatabase.class,
                            "user_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
