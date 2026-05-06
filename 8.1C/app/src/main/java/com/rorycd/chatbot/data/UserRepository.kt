package com.rorycd.chatbot.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing [User] data
 */
@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val dataStore: PreferencesDataStore
) {

    fun currentUserId(): Flow<Int?> = dataStore.currentUserIdFlow()

    suspend fun login(username: String, password: String) : Boolean {
        // Check user exists
        val user = userDao.login(username, password)

        // Save user ID to shared prefs
        if (user != null)
            dataStore.setCurrentUserId(user.id)

        return user != null
    }

    suspend fun logOut() {
        dataStore.clearCurrentUser()
    }

    suspend fun register(
        username: String,
        password: String
    ): Boolean {
        val newUser = User(0, username, password)
        try {
            userDao.insert(newUser)
            login(username, password)
            return true
        } catch(e: Exception) {
            return false
        }
    }

    suspend fun getCurrentUser(): User? {
        val id = currentUserId().first() ?: return null
        val user = userDao.getUserById(id)
        return user
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getCurrentUserFlow(): Flow<User?> = currentUserId().flatMapLatest { id ->
        if (id == null) flowOf(null)
        else userDao.getUserFlowById(id)
    }

    suspend fun userExists(username: String): Boolean {
        val user = userDao.getUserByUsername(username)
        return user != null
    }

    fun isLoggedIn() : Flow<Boolean> {
        return currentUserId().map { it != null }
    }
}
