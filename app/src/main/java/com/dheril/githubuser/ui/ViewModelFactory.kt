package com.dheril.githubuser.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dheril.githubuser.ui.setting.SettingPreferences
import com.dheril.githubuser.ui.viewmodel.FavoriteUserViewModel
import com.dheril.githubuser.ui.viewmodel.FollowViewModel
import com.dheril.githubuser.ui.viewmodel.MainViewModel
import com.dheril.githubuser.ui.viewmodel.ThemeModeViewModel
import com.dheril.githubuser.ui.viewmodel.UserDetailViewModel

class ViewModelFactory private constructor(
    private val mApplication: Application,
    private val mPref: SettingPreferences
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserDetailViewModel::class.java)) {
            return UserDetailViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(FavoriteUserViewModel::class.java)) {
            return FavoriteUserViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(ThemeModeViewModel::class.java)) {
            return ThemeModeViewModel(mPref) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel() as T
        } else if (modelClass.isAssignableFrom(FollowViewModel::class.java)) {
            return FollowViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application, pref: SettingPreferences): ViewModelFactory {
            if (instance == null) {
                synchronized(ViewModelFactory::class.java) {
                    instance = ViewModelFactory(application, pref)
                }
            }
            return instance as ViewModelFactory
        }
    }
}