package com.rorycd.istream.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {

    private static UserRepository INSTANCE;
    private static final String USER_ID = "user_id";
    private final UserDao userDao;
    private final PlaylistDao playlistDao;
    private final SharedPreferences sharedPrefs;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public interface LoginCallback {
        void onResult(User user);
    }

    public interface SignupCallback {
        void onResult(int userId);
    }

    private UserRepository(Context context) {
        userDao = UserDatabase.getInstance(context).userDao();
        playlistDao = UserDatabase.getInstance(context).playlistDao();
        sharedPrefs = context.getSharedPreferences("com.rorycd.istream.data", Context.MODE_PRIVATE);
    }

    public static UserRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new UserRepository(context);
        }
        return INSTANCE;
    }

    public void registerUser(String username, String fullName, String password, SignupCallback callback) {
        User newUser = new User(username, fullName, password);
        // Run on a separate thread
        executor.execute(() -> {
            int userId = (int) userDao.registerUser(newUser);
            sharedPrefs.edit().putInt(USER_ID, userId).apply();

            // Execute the callback on the main thread
            new Handler(Looper.getMainLooper()).post(() -> callback.onResult(userId));
        });
    }

    public void login(String username, String password, LoginCallback callback) {
        // Run a separate thread
        executor.execute(() -> {
            User user = userDao.login(username, password);
            if (user != null) {
                sharedPrefs.edit().putInt(USER_ID, user.id).apply();
            }
            // Execute the callback on the main thread
            new Handler(Looper.getMainLooper()).post(() -> callback.onResult(user));
        });
    }

    public int getCurrentUserId() {
        return sharedPrefs.getInt(USER_ID, -1);
    }

    public void addUrlToPlaylist(String url) {
        PlaylistItem item = new PlaylistItem(getCurrentUserId(), url);

        // Run a separate thread
        executor.execute(() -> {
            playlistDao.addItem(item);
        });
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
