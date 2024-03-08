package com.example.chatapplication.constant

object AppConstants {
    const val KEY_CACHE_USER_INFO = "KEY_CACHE_USER_INFO"
    const val KEY_CACHE_CONFIG = "KEY_CACHE_CONFIG"

    const val HTTP_METHOD_POST = 1
    const val HTTP_METHOD_GET = 0

    const val CACHE_USER = "CACHE_USER"
    const val CACHE_CONFIG = "CACHE_CONFIG"
    const val PUSH_TOKEN = "PUSH_TOKEN"
    const val CACHE_HTTP_LOG = "CACHE_HTTP_LOG"
    const val ID_USER = "ID_USER"



    const val TYPE_SYSTEM = 0 // nhắn tin cho mình
    const val TYPE_GROUP = 1  // nhóm chat
    const val TYPE_PRIVATE = 2 // tin nhắn bạn bè

    //Contact_type
    const val ITS_ME = 0 // chính mình
    const val NOT_FRIEND = 1 // người khác
    const val WAITING_CONFIRM = 2 // họ gửi lời mời, đợi mình xác nhận
    const val WAITING_RESPONSE = 3 // mình gửi lời mời, đợi họ phản hồi
    const val FRIEND = 4//Bạn bè

    //User Status
    const val STATUS_NOT_ACTIVE = 0// Tạm ngưng(không dùng)
    const val STATUS_ACTIVE = 1// Đang hoạt động

    const val DATA_MEDIA = "DATA_MEDIA"
    const val POSITION_MEDIA = "POSITION_MEDIA"
    const val MEDIA_COUNT_VISIBLE = "MEDIA_COUNT_VISIBLE"
    const val GROUP_DATA = "GROUP_DATA"
    const val USER_TAG_ALL_ID = "USER_TAG_ALL_ID"








}