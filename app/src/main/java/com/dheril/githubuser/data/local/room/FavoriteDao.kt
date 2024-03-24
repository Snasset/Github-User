package com.dheril.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.*
import com.dheril.githubuser.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite: FavoriteEntity)

    @Update
    fun update(favorite: FavoriteEntity)

    @Delete
    fun delete(favorite: FavoriteEntity)

    @Query("SELECT * FROM FavoriteEntity WHERE username = :username")
    fun getFavoriteUserByUsername(username: String?): LiveData<FavoriteEntity>

    @Query("SELECT * from FavoriteEntity")
    fun getAllFavorites(): LiveData<List<FavoriteEntity>>
}