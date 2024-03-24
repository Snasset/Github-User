package com.dheril.githubuser.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dheril.githubuser.BuildConfig
import com.dheril.githubuser.data.local.entity.FavoriteEntity
import com.dheril.githubuser.data.remote.response.UserDetailResponse
import com.dheril.githubuser.data.remote.retrofit.ApiConfig
import com.dheril.githubuser.data.repository.FavoriteRepository
import com.dheril.githubuser.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun insert(favorite: FavoriteEntity) {
        mFavoriteRepository.insert(favorite)
    }

    fun delete(favorite: FavoriteEntity) {
        mFavoriteRepository.delete(favorite)
    }

    fun getFavoriteUserByUsername(username: String?) =
        mFavoriteRepository.getFavoriteUserByUsername(username)

    private val _userDetail = MutableLiveData<UserDetailResponse>()
    val userDetail: LiveData<UserDetailResponse> = _userDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    private val _query = MutableLiveData<String>()

    init {
        var query: String? = _query.value
        query = ""
        showUserDetail(query)
    }

    fun getDetail(): LiveData<UserDetailResponse> {
        return userDetail
    }

    fun showUserDetail(searchInput: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetail(searchInput, BuildConfig.API_KEY)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userDetail.value = response.body()
                } else {
                    val errorCode = response.code()
                    Log.d("UserDetailViewModel", "Error Code: $errorCode")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = Event("Error: ${t.message}")
            }
        })
    }


}