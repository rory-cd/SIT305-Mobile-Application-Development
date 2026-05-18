package com.rorycd.bowerbird.di

import android.content.Context
import com.rorycd.bowerbird.data.AppDatabase
import com.rorycd.bowerbird.data.FolderDao
import com.rorycd.bowerbird.data.QueuedFileDao
import com.rorycd.bowerbird.data.RuleDao
import com.rorycd.bowerbird.data.ScannedFileDao
import com.rorycd.bowerbird.prompt.GemmaRepository
import com.rorycd.bowerbird.prompt.PromptRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideFolderDao(database: AppDatabase): FolderDao {
        return database.folderDao()
    }

    @Provides
    fun provideScannedFileDao(database: AppDatabase): ScannedFileDao {
        return database.scannedFileDao()
    }

    @Provides
    fun provideMessageDao(database: AppDatabase): QueuedFileDao {
        return database.queuedFileDao()
    }

    @Provides
    fun provideRuleDao(database: AppDatabase): RuleDao {
        return database.ruleDao()
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
