package com.rorycd.learningassistant.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

const val USER_ID_KEY = "current_user_id"
const val SHARED_PREFS_NAME = "prefs"

class UserRepository(private val context: Context) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    var userDao = UserDatabase.getDatabase(context).userDao()

    suspend fun login(username: String, password: String) : Boolean {
        // Check user exists
        val user = userDao.login(username, password)

        // Save user ID to shared prefs
        if (user != null)
            sharedPrefs.edit { putInt(USER_ID_KEY, user.id) }

        return user != null
    }

    fun logOut() {
        sharedPrefs.edit { putInt(USER_ID_KEY, -1) }
    }

    suspend fun register(
        username: String,
        password: String,
        email: String,
        imgUri: String?
    ) {
        val newUser = User(0, username, password, email, interests = null, imgUri = imgUri)
        userDao.insert(newUser)
        login(username, password)
    }

    fun currentUserId(): Int {
        return sharedPrefs.getInt(USER_ID_KEY, -1)
    }

    fun isLoggedIn() : Boolean {
        return currentUserId() != -1
    }

    suspend fun addInterests(
       interests: List<String>
    ) {
        userDao.updateInterests(currentUserId(), interests)
    }
}
