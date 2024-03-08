package com.example.chatapplication.model.response

import com.example.chatapplication.model.entity.Conversation
import com.example.chatapplication.model.entity.GroupChat

class ListConversationResponse {
    val status = 1
    val message = ""
    val data= ArrayList<GroupChat>()
}