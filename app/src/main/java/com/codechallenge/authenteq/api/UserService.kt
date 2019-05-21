package com.codechallenge.authenteq.api

import com.codechallenge.authenteq.BuildConfig
import com.codechallenge.authenteq.model.Repository
import com.codechallenge.authenteq.model.Result
import com.codechallenge.authenteq.model.User
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {

    @GET("search/users")
    fun searchAsync(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("sort") sort: String,
        @Query("client_id") clientId: String = BuildConfig.GithubClientId,
        @Query("client_secret") clientSecret: String = BuildConfig.GithubClientSecret
    ): Deferred<Result>

    @GET("users/{username}")
    fun getDetailAsync(
        @Path("username") username: String,
        @Query("client_id") clientId: String = BuildConfig.GithubClientId,
        @Query("client_secret") clientSecret: String = BuildConfig.GithubClientSecret
    ): Deferred<User>

    @GET("users/{username}/repos")
    fun getReposAsync(
        @Path("username") username: String,
        @Query("client_id") clientId: String = BuildConfig.GithubClientId,
        @Query("client_secret") clientSecret: String = BuildConfig.GithubClientSecret
    ): Deferred<List<Repository>>

    @GET("users/{username}/followers")
    fun getFollowersAsync(
        @Path("username") username: String,
        @Query("per_page") perPage: Int = 2,
        @Query("client_id") clientId: String = BuildConfig.GithubClientId,
        @Query("client_secret") clientSecret: String = BuildConfig.GithubClientSecret
    ): Deferred<List<User>>
}