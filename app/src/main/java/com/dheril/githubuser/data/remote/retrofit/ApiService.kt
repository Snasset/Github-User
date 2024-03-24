package com.dheril.githubuser.data.remote.retrofit

import com.dheril.githubuser.data.remote.response.ItemsItem
import com.dheril.githubuser.data.remote.response.UserDetailResponse
import com.dheril.githubuser.data.remote.response.UserListResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getUserList(
        @Query("q") q: String,
        @Query("apiKey") apiKey: String
    ): Call<UserListResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String,
        @Query("apiKey") apiKey: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String,
        @Query("apiKey") apiKey: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String,
        @Query("apiKey") apiKey: String
    ): Call<List<ItemsItem>>

}
