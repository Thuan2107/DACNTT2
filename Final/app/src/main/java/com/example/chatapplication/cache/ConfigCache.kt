package com.example.chatapplication.cache

import com.example.chatapplication.constant.AppConstants
import com.example.chatapplication.helper.PrefUtils.Companion.getString
import com.example.chatapplication.helper.PrefUtils.Companion.putString
import com.example.chatapplication.model.Config
import com.google.gson.Gson

class ConfigCache {
    private var mConfig: Config = Config()

    fun getConfig(): Config {
        try {
            mConfig = Gson().fromJson(getString(AppConstants.KEY_CACHE_CONFIG), Config::class.java)
            return mConfig
        } catch (e: Exception) {
            e.stackTrace
        }
        return mConfig
    }

    fun saveConfig(config: Config?) {
        var congfig = config
        try {
            if (congfig == null) {
                congfig = Config()
            }
            putString(AppConstants.KEY_CACHE_CONFIG, Gson().toJson(congfig))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}