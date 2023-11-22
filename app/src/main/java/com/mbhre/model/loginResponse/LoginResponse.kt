package com.mbhre.model.loginResponse


import com.google.gson.annotations.SerializedName

/**
 * ouput response of user api
 *
 * @property id
 * @property name
 * @property email
 * @property gender
 * @property status
 */
data class LoginResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("status")
    val status: String
)