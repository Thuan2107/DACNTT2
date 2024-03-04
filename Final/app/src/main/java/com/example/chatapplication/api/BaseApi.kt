package com.example.chatapplication.api

import com.example.chatapplication.cache.CurrentUser
import com.example.chatapplication.constant.AppConstants
import com.google.gson.Gson
import com.hjq.http.annotation.HttpHeader
import com.hjq.http.annotation.HttpRename
import com.hjq.http.config.IRequestApi
import com.hjq.http.config.IRequestBodyStrategy
import com.hjq.http.config.IRequestInterceptor
import com.hjq.http.config.IRequestType
import com.hjq.http.model.HttpHeaders
import com.hjq.http.model.HttpParams
import com.hjq.http.model.RequestBodyType
import com.hjq.http.request.HttpRequest
import timber.log.Timber


open class BaseApi : IRequestApi, IRequestType, IRequestInterceptor {
    override fun getBodyType(): IRequestBodyStrategy {
        return RequestBodyType.JSON
    }


    @HttpHeader
    @HttpRename("Method")
    var method = AppConstants.HTTP_METHOD_GET

    @HttpHeader
    @HttpRename("Authorization")
    var authorization: String = CurrentUser().getToken()

    override fun getApi(): String {
        return ""
    }


    override fun interceptArguments(
        httpRequest: HttpRequest<*>,
        params: HttpParams,
        headers: HttpHeaders
    ) {
        super.interceptArguments(httpRequest, params, headers)
        Timber.e("EasyHttp RequestBody: ${Gson().toJson(params.params)}")
    }
}