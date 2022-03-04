package com.jokopriyono.belajarmvvm.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jokopriyono.belajarmvvm.data.model.Character

@Database(entities = [Character::class], version = 1)
abstract class MyDatabase : RoomDatabase() {

    abstract fun character(): CharactersDao

    companion object {
        private var database: MyDatabase? = null

        fun instance(context: Context): MyDatabase {
            if (database == null) {
                synchronized(MyDatabase::class) {
                    database = Room.databaseBuilder(
                        context.applicationContext,
                        MyDatabase::class.java,
                        "my_database.db"
                    ).build()
                }
            }
            return database!!
        }

        fun destroyInstance() {
            database = null
        }
    }
}