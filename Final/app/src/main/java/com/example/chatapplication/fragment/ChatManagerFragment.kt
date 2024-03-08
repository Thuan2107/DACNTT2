package com.example.chatapplication.fragment

import android.util.Log
import android.view.View
import com.example.chatapplication.activity.HomeActivity
import com.example.chatapplication.app.AppFragment
import com.example.chatapplication.base.BaseFragment
import com.example.chatapplication.databinding.FragmentChatManagerBinding

class ChatManagerFragment : AppFragment<HomeActivity>() {

    private lateinit var binding: FragmentChatManagerBinding

    override fun getLayoutView(): View {
        binding = FragmentChatManagerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initData() {
        Log.d("gooooooo","chat fragment")
    }
}