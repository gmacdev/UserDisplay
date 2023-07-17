package com.gmacv.userdisplay.data.api

import com.gmacv.userdisplay.data.model.AuthResponse
import com.gmacv.userdisplay.data.model.CreateUser
import com.gmacv.userdisplay.data.model.GetUsers
import com.gmacv.userdisplay.data.model.UserCredentials
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Apis {

    @GET("users?page=1")
    suspend fun getUsers(): Response<GetUsers>

    @POST("users")
    suspend fun createUser(@Body createUser: CreateUser): Response<CreateUser>

    @POST("register")
    suspend fun registerUser(@Body userCredentials: UserCredentials): Response<AuthResponse>

    @POST("login")
    suspend fun loginUser(@Body userCredentials: UserCredentials): Response<AuthResponse>

}