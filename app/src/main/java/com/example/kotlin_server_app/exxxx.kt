package com.example.kotlin_server_app

import retrofit2.Call
import retrofit2.http.*

interface exxxx {
    //@FormUrlEncoded
    @GET("/members/checktoken/")
    fun ex(
            @Header("Authorization") user_token : String?
    ): Call<ex_data>
}