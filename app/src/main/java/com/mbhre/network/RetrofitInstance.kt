package com.mbhre.network


import com.mbhre.util.Constants
import com.mbhre.util.Constants.BASE_URL
import com.mbhre.util.OAuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit library integration with access token and base url
 *
 */
class RetrofitInstance {
    companion object {

        private val retrofitLogin by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(OAuthInterceptor("Bearer", Constants.Token))
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }


        /**
         * calling interface to api call
         */
        val loginApi by lazy {
            retrofitLogin.create(API::class.java)
        }


    }
}