package com.example.chatapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatapplication.databinding.ActivityHomeBinding
import com.hjq.http.EasyHttp

class HomeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.text.text = "Home"


    }
}