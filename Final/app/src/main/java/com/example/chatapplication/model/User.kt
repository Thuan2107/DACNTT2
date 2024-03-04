package com.example.chatapplication.model

import com.google.gson.annotations.SerializedName

class User {
    @SerializedName("id")
    var id = 0L

    @SerializedName("name")
    var name = ""

    @SerializedName("username")
    var username = ""

    @SerializedName("password")
    var password = ""

    @SerializedName("restaurant_name")
    var restaurantName = ""

    @SerializedName("domain_name")
    var domainName = ""

    @SerializedName("email")
    var email = ""

    @SerializedName("phone")
    var phone = ""

    @SerializedName("access_token")
    var accessToken = ""

    @SerializedName("jwt_token")
    var jwtToken = ""

    @SerializedName("avatar")
    var avatar = ""

    @SerializedName("address")
    var address = ""

    @SerializedName("branch_name")
    var branchName = ""

    @SerializedName("restaurant_id")
    var restaurantId = 0L

    @SerializedName("gender")
    var gender = 0

    @SerializedName("branch_id")
    var branchId = 0L

    @SerializedName("current_point")
    var currentPoint = 0L

    @SerializedName("permissions")
    var permissions = ArrayList<String>()

    @SerializedName("restaurant_brand_id")
    var restaurantBrandId = 0L

    @SerializedName("restaurant_brand_name")
    var restaurantBrandName = ""

    @SerializedName("brand_logo")
    var brandLogo = ""

    @SerializedName("employee_role_name")
    var employeeRoleName = ""

    @SerializedName("branch_address")
    var branchAddress = ""

    @SerializedName("is_show_dialog_choose_branch")
    var isShowDialogChooseBranch = true
}