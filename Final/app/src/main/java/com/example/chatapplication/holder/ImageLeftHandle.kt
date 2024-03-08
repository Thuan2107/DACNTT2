package com.example.chatapplication.holder

import com.example.chatapplication.adapter.MessageAdapter
import com.example.chatapplication.databinding.ItemMessageImageLeftBinding
import com.example.chatapplication.model.entity.Message


class ImageLeftHandle(
    private val binding: ItemMessageImageLeftBinding,
    private val data: Message,
    private val position: Int,
    private val chatHandle: MessageAdapter.ChatHandle,
    private var adapter: MessageAdapter
) {
    fun setData() {
        if (data.highlight == 1) {
            data.highlight = 0
        }
        adapter.checkTimeMessagesTheir(
            data.isTimeline,
            data.createdAt,
            binding.time.llTimeHeader,
            binding.time.tvTimeHeader,
            binding.tvTime,
            binding.tvNameMedia,
            binding.ivAvatar,
            position
        )

        adapter.setMarginStart(binding.root, binding.time.llTimeHeader, position)

        adapter.setProfilePersonChat(binding.tvNameMedia, binding.ivAvatar, data)


        val screen = IntArray(2)
        binding.ctlContainer.getLocationOnScreen(screen)

        binding.ivOneOne.setOnLongClickListener {
            chatHandle.onRevoke(data, binding.root, screen[1], null)
            true
        }
    }

}