package com.dheril.githubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dheril.githubuser.ui.setting.SettingPreferences
import kotlinx.coroutines.launch

class ThemeModeViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun getThemeSetting(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isThemeDarkMode: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isThemeDarkMode)
        }
    }

}