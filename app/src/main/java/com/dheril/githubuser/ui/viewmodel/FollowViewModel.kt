package com.dheril.githubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dheril.githubuser.BuildConfig
import com.dheril.githubuser.data.remote.response.ItemsItem
import com.dheril.githubuser.data.remote.retrofit.ApiConfig
import com.dheril.githubuser.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {
    private val _userFollow = MutableLiveData<List<ItemsItem>>()
    val userFollow: LiveData<List<ItemsItem>> = _userFollow

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    fun showUserFollow(searchInput: String, index: Int) {
        _isLoading.value = true
        val client = if (index == 1) {
            ApiConfig.getApiService().getFollowers(searchInput, BuildConfig.API_KEY)
        } else {
            ApiConfig.getApiService().getFollowing(searchInput, BuildConfig.API_KEY)
        }
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userFollow.value = response.body()
                } else {
                    _snackbarText.value = Event(response.message().toString())
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = Event("Error: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "FollowViewModel"
    }

}