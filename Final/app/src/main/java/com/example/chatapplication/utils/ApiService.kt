package com.example.chatapplication.utils

import com.example.chatapplication.model.request.LoginRequest
import com.example.chatapplication.model.response.ListConversationResponse
import com.example.chatapplication.model.response.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {
    @POST("auth/sign-in")
    fun login(
        @Body request: LoginRequest
    ): Call<UserResponse>

    @GET("conversation")
    fun getListConversation(
        @Query("limit") limit: Int,
        @Query("position") position: String,
        @Header("Authorization") authorization: String
    ): Call<ListConversationResponse>

}