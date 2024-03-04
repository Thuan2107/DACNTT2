package com.example.chatapplication.api

import com.example.chatapplication.router.APIRouter

class Test : BaseApi() {


    override fun getApi(): String {
        return APIRouter.API_TEST();
    }
}