package com.mbhre.repository

import com.mbhre.network.RequestBodies
import com.mbhre.network.RetrofitInstance

class AppRepository {

    suspend fun getUsers() = RetrofitInstance.loginApi.getUsers()

    suspend fun loginUser(body: RequestBodies.LoginBody) = RetrofitInstance.loginApi.createUser(body)
}