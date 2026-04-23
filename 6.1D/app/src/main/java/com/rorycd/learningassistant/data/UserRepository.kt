package com.rorycd.learningassistant.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.flow.Flow

const val USER_ID_KEY = "current_user_id"
const val SHARED_PREFS_NAME = "prefs"

class UserRepository(private val userDao: UserDao, private val context: Context) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

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
    ): Boolean {
        val newUser = User(0, username, password, email, interests = null, imgUri = imgUri)
        try {
            userDao.insert(newUser)
            login(username, password)
            return true
        } catch(e: Exception) {
            return false
        }
    }

    fun currentUserId(): Int {
        return sharedPrefs.getInt(USER_ID_KEY, -1)
    }

    suspend fun getCurrentUser(): User? {
        val id = currentUserId()
        val user = userDao.getUserById(id)
        return user
    }

    fun getCurrentUserFlow(): Flow<User?> {
        return userDao.getUserFlowById(currentUserId())
    }

    suspend fun userExists(username: String): Boolean {
        val user = userDao.getUserByUsername(username)
        return user != null
    }

    fun isLoggedIn() : Boolean {
        return currentUserId() != -1
    }

    suspend fun addInterests(interests: Set<String>) {
        val interestList = interests.toList()
        userDao.updateInterests(currentUserId(), interestList)
    }
}
