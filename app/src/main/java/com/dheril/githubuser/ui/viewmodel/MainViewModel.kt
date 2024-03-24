package com.dheril.githubuser.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dheril.githubuser.BuildConfig
import com.dheril.githubuser.data.remote.response.ItemsItem
import com.dheril.githubuser.data.remote.response.UserListResponse
import com.dheril.githubuser.data.remote.retrofit.ApiConfig
import com.dheril.githubuser.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _userList = MutableLiveData<List<ItemsItem>>()
    val userList: LiveData<List<ItemsItem>> = _userList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    private val _query = MutableLiveData<String>()

    init {
        var query: String? = _query.value
        query = "snasset"
        showUserList(query)
    }

    fun showUserList(searchInput: String) {
        _isLoading.value = true
        val input = searchInput.ifBlank {
            _isLoading.value = false
            return
        }
        val client = ApiConfig.getApiService().getUserList(input, BuildConfig.API_KEY)
        client.enqueue(object : Callback<UserListResponse> {
            override fun onResponse(
                call: Call<UserListResponse>,
                response: Response<UserListResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userList.value = response.body()?.items
                } else {
                    _snackbarText.value = Event(response.message().toString())
                }
            }

            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = Event("Error: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }


}



