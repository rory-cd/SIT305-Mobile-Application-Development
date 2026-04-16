package com.rorycd.istream.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

public class UserRepository {

    private static final String USER_ID = "user_id";
    private final UserDao userDao;
    private final PlaylistDao playlistDao;
    private final SharedPreferences sharedPrefs;

    public UserRepository(Context context) {
        userDao = UserDatabase.getInstance(context).userDao();
        playlistDao = UserDatabase.getInstance(context).playlistDao();
        sharedPrefs = context.getSharedPreferences("com.rorycd.istream.data", Context.MODE_PRIVATE);
    }

    public void registerUser(String username, String fullName, String password) {
        User newUser = new User(username, fullName, password);
        int userId = (int)userDao.registerUser(newUser);
        sharedPrefs.edit().putInt(USER_ID, userId).apply();
    }

    public boolean login(String username, String password) {
        User user = userDao.login(username, password);
        if (user != null) {
            sharedPrefs.edit().putInt(USER_ID, user.id).apply();
            return true;
        }
        return false;
    }

    public int getCurrentUserId() {
        return sharedPrefs.getInt(USER_ID, -1);
    }

    public void addUrlToPlaylist(String url) {
        PlaylistItem item = new PlaylistItem(getCurrentUserId(), url);
        playlistDao.addItem(item);
    }

    public List<String> getPlaylist() {
        return playlistDao.getPlaylistForUser(getCurrentUserId());
    }

    public boolean isLoggedIn() {
        return getCurrentUserId() != -1;
    }

    public void logout() {
        sharedPrefs.edit().remove(USER_ID).apply();
    }
}
