package com.android.trade.coinmonitor.di

import android.content.Context
import androidx.room.Room
import com.android.trade.data.local.dao.CoinDao
import com.android.trade.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Singleton
    @Provides
    fun provideNoteDatabase(@ApplicationContext context: Context) : AppDatabase {
        return Room
            .databaseBuilder(
                context,
                AppDatabase::class.java,
                "coin_database")
            .build()
    }

    @Singleton
    @Provides
    fun provideNoteDAO(noteDB: AppDatabase): CoinDao {
        return noteDB.coinDao()
    }
}