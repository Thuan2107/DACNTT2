package com.example.chatapplication.holder

import com.example.chatapplication.adapter.MessageAdapter
import com.example.chatapplication.databinding.ItemMessageTextLeftBinding
import com.example.chatapplication.model.entity.Message

class TextLeftHandle(
    private val binding: ItemMessageTextLeftBinding,
    private val data: Message,
    private val position: Int,
    private val chatHandle: MessageAdapter.ChatHandle,
    private var adapter: MessageAdapter
) {
    fun setData() {

        adapter.checkTimeMessagesTheir(
            data.isTimeline,
            data.createdAt,
            binding.time.llTimeHeader,
            binding.time.tvTimeHeader,
            binding.message.tvTime,
            binding.tvNameMedia,
            binding.ivAvatar,
            position
        )

        adapter.setMarginStart(binding.root, binding.time.llTimeHeader, position)

        adapter.setProfilePersonChat(binding.tvNameMedia, binding.ivAvatar, data)

        binding.ctlText.setOnLongClickListener {
            chatHandle.onRevoke(
                data,
                binding.ctlMessage,
                binding.ctlMessage.y.toInt(),
                binding.message.tvMessage
            )
            true
        }

    }
}