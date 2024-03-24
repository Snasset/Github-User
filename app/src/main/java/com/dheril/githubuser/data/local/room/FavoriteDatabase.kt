package com.dheril.githubuser.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dheril.githubuser.data.local.entity.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 2, exportSchema = false)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var instance: FavoriteDatabase? = null
        fun getInstance(context: Context): FavoriteDatabase {
            if (instance == null) {
                synchronized(FavoriteDatabase::class.java) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteDatabase::class.java, "favorite_database"
                    )
                        .build()
                }
            }
            return instance as FavoriteDatabase
        }
    }

}