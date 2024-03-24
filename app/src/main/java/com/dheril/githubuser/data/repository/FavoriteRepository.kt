package com.dheril.githubuser.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dheril.githubuser.data.local.entity.FavoriteEntity
import com.dheril.githubuser.data.local.room.FavoriteDao
import com.dheril.githubuser.data.local.room.FavoriteDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteDatabase.getInstance(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllFavorites(): LiveData<List<FavoriteEntity>> = mFavoriteDao.getAllFavorites()

    fun insert(favorite: FavoriteEntity) {
        executorService.execute { mFavoriteDao.insert(favorite) }
    }

    fun getFavoriteUserByUsername(username: String?):
            LiveData<FavoriteEntity> = mFavoriteDao.getFavoriteUserByUsername(username)

    fun delete(favorite: FavoriteEntity) {
        executorService.execute { mFavoriteDao.delete(favorite) }
    }


}