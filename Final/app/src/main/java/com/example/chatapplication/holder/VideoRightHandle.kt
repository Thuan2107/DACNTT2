package com.example.chatapplication.holder

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import com.example.chatapplication.adapter.MessageAdapter
import com.example.chatapplication.constant.ChatConstants
import com.example.chatapplication.databinding.ItemMessageVideoRightBinding
import com.example.chatapplication.model.entity.Message
import com.example.chatapplication.utils.AppUtils
import com.example.chatapplication.utils.PhotoLoadUtils

/**
 * @Author: Bùi Hữu Thắng
 * @Date: 24/10/2022
 */
class VideoRightHandle(
    private val binding: ItemMessageVideoRightBinding,
    private val data: Message,
    private val position: Int,
    private val chatHandle: MessageAdapter.ChatHandle,
    private var adapter: MessageAdapter
) {

    @SuppressLint("ClickableViewAccessibility")
    fun setData() {


//        PhotoLoadUtils.resizeMediaWithCoil(
//            data.media[0].pathLocal.ifEmpty { data.media[0].original.url },
//            binding.ivOneOne,
//            true
//        )

        adapter.checkTimeMessages(
            data.isTimeline,
            data.createdAt,
            binding.time.llTimeHeader,
            binding.time.tvTimeHeader,
            binding.tvTime,
            position
        )

        adapter.setMarginStart(binding.root, binding.time.llTimeHeader, position)

        adapter.checkUserViewMessage(
            binding.rcvUserView,
            binding.send.llSendMessage,
            data,
            position,
            binding.llView,
            binding.llUserView,
            binding.tvMoreView
        )

        adapter.handleStatusMessage(binding.send.tvTextUserView, data)


        val screen = IntArray(2)
        binding.ctlContainer.getLocationOnScreen(screen)

        binding.root.setOnLongClickListener {
            chatHandle.onRevoke(data, binding.root, screen[1], null)
            true
        }
        binding.root.setOnClickListener {
            AppUtils.disableClickAction(binding.root, 500)
        }
    }
}