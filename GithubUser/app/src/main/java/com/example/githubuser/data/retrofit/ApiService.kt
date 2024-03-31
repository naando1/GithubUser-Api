package com.example.githubuser.data.retrofit

import com.example.githubuser.data.response.DetailUserResponse
import com.example.githubuser.data.response.GithubResponse
import com.example.githubuser.data.response.ItemsItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: ini token ")
    fun getGithub(
        @Query("q") query: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    @Headers("Authorization: ini token")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: ini token")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<ItemsItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: ini token")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<ItemsItem>>

}