package com.example.chatapplication.constant


class ChatConstants {
    companion object {
        const val DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss"
        const val DATE_TIME_MINUS_FORMAT = "dd/MM/yyyy HH:mm"
        const val TIME_MINUS_FORMAT = "HH:mm"
        const val DATE_FORMAT = "dd/MM/yyyy"

        const val GROUP_DATA = "GROUP_DATA"
        const val USER_DATA = "USER_DATA"
        const val IS_CONFIRM_NEW_MEMBER = "IS_CONFIRM_NEW_MEMBER"

        //property animation
        const val ALPHA = "alpha"
        const val TRANSLATION_X = "translationX"

        //Mine type
        const val TYPE_JPG = "jpg"
        const val TYPE_JPEG = "jpeg"
        const val TYPE_PNG = "png"
        const val TYPE_SVG = "svg"
        const val TYPE_DOCX = "docx"
        const val TYPE_AI = "ai"
        const val TYPE_DMG = "dmg"
        const val TYPE_XLSX = "xlsx"
        const val TYPE_HTML = "html"
        const val TYPE_MP3 = "mp3"
        const val TYPE_PDF = "pdf"
        const val TYPE_PPTX = "pptx"
        const val TYPE_PSD = "psd"
        const val TYPE_REC = "rec"
        const val TYPE_EXE = "exe"
        const val TYPE_SKETCH = "sketch"
        const val TYPE_TXT = "txt"
        const val TYPE_MP4 = "mp4"
        const val TYPE_XML = "xml"
        const val TYPE_ZIP = "zip"


        const val EMIT_UPLOAD = "EMIT_UPLOAD"
        const val CHANGE_BACKGROUND = "CHANGE_BACKGROUND"

        //Group
        const val TYPE_PINNED = 0
        const val TYPE_UN_PINNED = 1
        const val TYPE_HIDE = 2
        const val TYPE_DELETE = 3
        const val TYPE_REPORT = 4
        const val TYPE_ON_NOTIFICATION = 5
        const val TYPE_OFF_NOTIFICATION = 6
        const val TYPE_OUT_GROUP = 7
        const val TYPE_BLOCK = 8
        const val TYPE_UNLOCK = 9


        //vote
        const val LOCK_VOTE = 0
        const val UNLOCK_VOTE = 1
        const val CHAT_FLOW = 1
        const val NEWS_FLOW = 0

        //get list chat constants
        const val SCROLL_TO_TOP = 1
        const val SCROLL_TO_BOTTOM = 2
        const val SCROLL_TO_REPLY = 3

        //DirectionChat
        const val DIRECTION_RIGHT = 0
        const val DIRECTION_LEFT = 1

        //Direction Search Mess
        const val DIRECTION_SEARCH_UP = 0
        const val DIRECTION_SEARCH_DOWN = 1

        //Enum duyệt thành viên group chat
        const val ALL_MEMBER = -1
        const val APPROVE_MEMBER = 1
        const val DENY_MEMBER = 0

        //Attack_Type
        const val ATTACK_TYPE_GIFT = 1
        const val ATTACK_TYPE_URL_LINK = 2
        const val ATTACK_TYPE_NO_GIFT = 3

        const val DEFAULT_BACKGROUND = "Default Background"

        const val TIME_HEADER = "TIME_HEADER"

        //Personal Setting Event
        const val EVENT_PIN_GROUP = 1
        const val EVENT_REMOVE_PIN_GROUP = 2
        const val EVENT_HIDE_GROUP = 3
        const val EVENT_SHOW_GROUP = 4
        const val EVENT_OFF_NOTIFY_GROUP = 5
        const val EVENT_ON_NOTIFY_GROUP = 6
    }
}