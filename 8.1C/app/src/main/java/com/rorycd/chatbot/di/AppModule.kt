package com.rorycd.chatbot.di

import android.content.Context
import com.rorycd.chatbot.data.AppDatabase
import com.rorycd.chatbot.data.ConversationDao
import com.rorycd.chatbot.data.MessageDao
import com.rorycd.chatbot.data.PreferencesDataStore
import com.rorycd.chatbot.data.UserDao
import com.rorycd.chatbot.prompt.GemmaRepository
import com.rorycd.chatbot.prompt.PromptRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context) =
        PreferencesDataStore(context)
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    fun provideConversationDao(database: AppDatabase): ConversationDao {
        return database.conversationDao()
    }

    @Provides
    fun provideMessageDao(database: AppDatabase): MessageDao {
        return database.messageDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPromptRepository(
        impl: GemmaRepository
    ): PromptRepository
}
