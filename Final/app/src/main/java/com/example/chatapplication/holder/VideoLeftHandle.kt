package com.example.chatapplication.holder

import com.example.chatapplication.adapter.MessageAdapter
import com.example.chatapplication.databinding.ItemMessageVideoLeftBinding
import com.example.chatapplication.model.entity.Message
import com.example.chatapplication.utils.AppUtils

class VideoLeftHandle(
    private val binding: ItemMessageVideoLeftBinding,
    private val data: Message,
    private val position: Int,
    private val chatHandle: MessageAdapter.ChatHandle,
    private var adapter: MessageAdapter
) {

    fun setData() {

//        PhotoLoadUtils.resizeMediaWithCoil(
//            data.media[0].original.url,
//            binding.ivOneOne,
//            true
//        )

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

        binding.root.setOnLongClickListener {
            chatHandle.onRevoke(data, binding.root, screen[1], null)
            true
        }

        binding.root.setOnClickListener {
            AppUtils.disableClickAction(binding.root, 500)
        }
    }
}