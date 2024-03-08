package com.example.chatapplication.model

/**
 * @Author: Bùi Hửu Thắng
 * @Date: 03/10/2022
 */
open class HttpData<T> {

    private val status: Int = 0

    private val message: String? = null

    private val data: T? = null

    fun getCode(): Int {
        return status
    }

    fun getMessage(): String? {
        return message
    }

    fun getData(): T? {
        return data
    }


    fun isRequestSucceed(): Boolean {
        return status == 200
    }
    fun isRequestRestoreAccount(): Boolean {
        return status == 300
    }

    fun isRequestError(): Boolean {
        return status == 400
    }

    fun isTokenFailure(): Boolean {
        return status == 410
    }
}