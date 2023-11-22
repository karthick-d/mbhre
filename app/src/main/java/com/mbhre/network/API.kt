package com.mbhre.network


import com.mbhre.model.UsersResponse
import com.mbhre.model.loginResponse.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Interface class for retrofit to call get and post
 *
 */
interface API {
    /**
     * List api call function
     *
     * @return
     */
    @GET("v2/users")
    suspend fun getUsers(): Response<UsersResponse>

    /**
     * create api call function
     *
     * @param body
     * @return
     */
    @POST("v2/users")
    suspend fun createUser(@Body body: RequestBodies.LoginBody): Response<LoginResponse>
}