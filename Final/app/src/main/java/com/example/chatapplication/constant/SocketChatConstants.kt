package com.example.chatapplication.constant

/**
 * @AuthorUpdate: HO QUANG TUNG
 * @Date: 02/01/2023
 */
class SocketChatConstants {
    companion object {
        const val version = "-v2"
        const val JOIN_ROOM = "join-room"
        const val LEAVE_ROOM = "leave-room"

        //Cập nhật quyền
        const val ON_UPDATE_PERMISSION = "listen-update-permission-conversation$version"
        const val EMIT_UPDATE_PERMISSION = "update-permission-conversation$version"

        //Thêm thành viên
        const val ON_ADD_MEMBER = "listen-add-member-conversation$version"
        const val EMIT_ADD_MEMBER = "add-member-conversation$version"

        //change name group
        const val EMIT_CHANGE_NAME_GROUP = "update-name-conversation$version"
        const val ON_CHANGE_NAME_GROUP = "listen-update-name-conversation$version"

        //change avatar group
        const val EMIT_CHANGE_AVATAR_GROUP = "update-avatar-conversation$version"
        const val ON_CHANGE_AVATAR_GROUP = "listen-update-avatar-conversation$version"


        const val ON_DISBAND_GROUP = "listen-disband-conversation$version"
        const val EMIT_DISBAND_GROUP = "disband-conversation$version"

        const val ON_OUT_GROUP = "listen-out-conversation$version"
        const val EMIT_OUT_GROUP = "out-conversation$version"

        const val ON_REMOVE_MEMBER = "listen-remove-member-conversation$version"
        const val EMIT_REMOVE_MEMBER = "remove-member-conversation$version"

        //typing on
        const val ON_TYPING_ON = "listen-typing-on$version"
        const val EMIT_TYPING_ON = "typing-on$version"

        //typing off
        const val ON_TYPING_OFF = "listen-typing-off$version"
        const val EMIT_TYPING_OFF = "typing-off$version"

        //Chat text
        const val EMIT_CHAT_TEXT = "message-text$version"
        const val ON_CHAT_TEXT = "listen-message-text$version"

        //Chat image
        const val EMIT_CHAT_IMAGE = "message-image$version"
        const val ON_CHAT_IMAGE = "listen-message-image$version"

        // Chat video
        const val EMIT_CHAT_VIDEO = "message-video$version"
        const val ON_CHAT_VIDEO = "listen-message-video$version"

        // Chat file
        const val EMIT_CHAT_FILE = "message-file$version"
        const val ON_CHAT_FILE = "listen-message-file$version"

        // Chat audio
        const val EMIT_CHAT_AUDIO = "message-audio$version"
        const val ON_CHAT_AUDIO = "listen-message-audio$version"

        //Thu hồi
        const val EMIT_REVOKE = "message-revoke$version"
        const val ON_REVOKE = "listen-message-revoke$version"

        //ghim tin nhắn
        const val EMIT_PINNED = "message-pinned$version"
        const val ON_PINNED = "listen-message-pinned$version"

        //gỡ ghim tin nhắn
        const val EMIT_REMOVE_PINNED = "message-remove-pinned$version"
        const val ON_REMOVE_PINNED = "listen-message-remove-pinned$version"

        //đổi hình nền
        const val ON_CHANGE_BACKGROUND = "listen-update-background-conversation$version"

        //Trả lời
        const val EMIT_REPLY = "message-reply$version"
        const val ON_REPLY = "listen-message-reply$version"

        //Chat sticker
        const val EMIT_STICKER = "message-sticker$version"
        const val ON_STICKER = "listen-message-sticker$version"

        //socket nhận group mới
        const val LISTEN_NEW_CONVERSATION = "listen-new-conversation$version"

        //Reaction tin nhắn
        const val EMIT_REACTION_MESSAGE = "reaction-message$version"
        const val ON_REACTION_MESSAGE = "listen-reaction-message$version"

        //Thu hồi reaction tin nhắn
        const val EMIT_REMOVE_REACTION_MESSAGE = "remove-reaction-message$version"
        const val ON_REMOVE_REACTION_MESSAGE = "listen-remove-reaction-message$version"

        //chuyển quyên trưởng nhóm
        const val ON_UPDATE_OWNER_CONVERSATION = "listen-update-owner-conversation$version"

        //thêm quyên phó nhóm
        const val ON_UPDATE_ADD_DEPUTY_CONVERSATION = "listen-add-deputy-conversation$version"

        //thong bao bat/tat duyet thanh vien
        const val ON_OFF_IS_CONFIRM_CONVERSATION = "listen-confirm-member$version"

        //thong bao thanh vien vao nhóm can duyet
        const val ON_WAITING_CONFIRM_CONVERSATION = "listen-waiting-confirm-member$version"

        //xoá quyên phó nhóm
        const val ON_UPDATE_REMOVE_DEPUTY_CONVERSATION = "listen-remove-deputy-conversation$version"

        const val ON_UPDATE_PERMISSION_MESSAGE = "listen-update-permission-conversation$version"

        const val ON_CREATE_VOTE = "listen-message-create-vote$version"
        const val ON_MESSAGE_VOTE = "listen-message-vote$version"
        const val ON_CHANGE_VOTE = "listen-message-change-vote$version"
        const val ON_ADD_OPTION_VOTE = "listen-add-vote-option$version"
        const val ON_BLOCK_VOTE = "listen-message-block-vote$version"

        //Share tin nhắn
        const val EMIT_SHARE_SOCKET = "message-share$version"
        const val ON_SHARE_SOCKET = "listen-message-share$version"

        //Change Personal Chat Setting
        const val ON_MESSAGE_CONVERSATION_PERSONAL_SETTING = "listen-message-conversation-personal-setting$version"
        //Delete conversation history
        const val ON_DELETE_CONVERSATION_HISTORY = "listen-delete-conversation$version"

        //Socket Error
        const val ON_SOCKET_ERROR = "socket-error$version"

        //Nghe join room
        const val ON_JOIN_ROOM = "listen-join-room"

        //Nghe block tài khoản của người khác
        const val ON_USER_BLOCK = "listen-user-block$version"

        //Nghe unblock tài khoản của người khác
        const val ON_USER_UNBLOCK = "listen-user-unblock$version"
    }
}