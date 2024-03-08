package com.example.chatapplication.model.entity

import com.example.chatapplication.utils.AppUtils
import com.example.chatapplication.utils.TimeUtils
import com.google.gson.annotations.SerializedName


class MessageEmit {
    @SerializedName("message")
    var message = ""

    @SerializedName("message_reply_id")
    var messageReplyId = ""

    @SerializedName("message_vote_id")
    var messageVoteId = ""

    @SerializedName("sticker_id")
    var stickerId = ""


    @SerializedName("media")
    var media = ArrayList<String>()


    @SerializedName("thumb")
    var thumb = Thumbnail()

    @SerializedName("key_error")
    var keyError: String = AppUtils.getRandomString(10)

    @SerializedName("create_at")
    var createAt: String = createCurrentTime()


    constructor()

    fun createCurrentTime(): String {
        return TimeUtils.getCurrentTimeFormat("yyyy-MM-dd HH:mm:ss", isTimeZoneUTC = true)
    }
}