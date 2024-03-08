package com.example.chatapplication.holder

import android.view.View
import android.view.ViewGroup
import com.example.chatapplication.adapter.MessageAdapter
import com.example.chatapplication.databinding.ItemMessageImageRightBinding
import com.example.chatapplication.model.entity.Message

/**
 * @Author: Bùi Hữu Thắng
 * @Date: 24/10/2022
 */
class ImageRightHandle(
    private val binding: ItemMessageImageRightBinding,
    private val data: Message,
    private val position: Int,
    private val chatHandle: MessageAdapter.ChatHandle,
    private var adapter: MessageAdapter
) {
    fun setData() {

        adapter.checkTimeMessages(
            data.isTimeline,
            data.createdAt,
            binding.time.llTimeHeader,
            binding.time.tvTimeHeader,
            binding.tvTime,
            position
        )

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

        adapter.setMarginStart(binding.root, binding.time.llTimeHeader, position)


        val screen = IntArray(2)
        binding.ctlContainer.getLocationOnScreen(screen)

        binding.ivOneOne.setOnLongClickListener {
            chatHandle.onRevoke(data, binding.root, screen[1], null)
            true
        }

        binding.root.setOnLongClickListener {
            chatHandle.onRevoke(data, binding.root, screen[1], null)
            true
        }
    }
}