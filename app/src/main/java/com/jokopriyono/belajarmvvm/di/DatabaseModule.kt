package com.jokopriyono.belajarmvvm.di

import android.app.Application
import androidx.room.Room
import com.jokopriyono.belajarmvvm.data.local.CharactersDao
import com.jokopriyono.belajarmvvm.data.local.MyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): MyDatabase {
        return Room.databaseBuilder(
            application,
            MyDatabase::class.java,
            "my_database.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCharactersDao(myDatabase: MyDatabase): CharactersDao {
        return myDatabase.character()
    }

}