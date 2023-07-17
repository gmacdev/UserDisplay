package com.gmacv.userdisplay.data.repository

import com.gmacv.userdisplay.data.api.Apis
import com.gmacv.userdisplay.data.model.CreateUser
import com.gmacv.userdisplay.data.model.UserCredentials
import javax.inject.Inject

class MainRepository @Inject constructor(private val apis: Apis) {

    suspend fun getUsers() = apis.getUsers()

    suspend fun createUser(createUser: CreateUser) = apis.createUser(createUser)

    suspend fun registerUser(userCredentials: UserCredentials) = apis.registerUser(userCredentials)

    suspend fun loginUser(userCredentials: UserCredentials) = apis.loginUser(userCredentials)

}