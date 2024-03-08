package com.example.chatapplication.model.request

import com.google.gson.annotations.SerializedName

class LoginRequest {
    @SerializedName("password")
    var password = ""

    @SerializedName("phone")
    var phone = ""
}