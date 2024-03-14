package com.example.chatapplication.router


@Suppress("FunctionName")
object ApiRouterOauth {

    fun API_LOGIN(): String {
        return "auth/sign-in"
    }

    fun API_LOGOUT(): String {
        return "customers/push-token/logout"
    }

    fun API_VERIFY_CHANGE_PASSWORD(): String {
        return "customers/verify-change-password"
    }

    fun API_FORGOT_PASSWORD(): String {
        return "customers/forgot-password"
    }

    fun API_REGISTER(): String {
        return "customers/register"
    }

    fun API_REGISTER_CUSTOMER_DEVICE(): String {
        return "register-customer-device"
    }

    fun API_UPDATE_PROFILE_REGISTER(): String {
        return "customers/update-profile-register"
    }
    fun API_GET_LIST_BRANDS(): String {
        return "restaurant-brands/home "
    }
}