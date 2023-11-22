package com.mbhre.network

object RequestBodies {

    data class LoginBody(
        val name:String,
        val gender:String,
        val email:String,
        val status:String
    )

}