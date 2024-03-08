package com.example.chatapplication.app

import android.annotation.SuppressLint
import android.app.Application
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.example.chatapplication.cache.HttpLoggerCache
import com.example.chatapplication.manager.ActivityManager
import com.example.chatapplication.model.RequestHandler
import com.example.chatapplication.model.RequestServer
import com.example.chatapplication.other.ToastStyle
import com.hjq.http.EasyConfig
import com.hjq.http.config.IRequestInterceptor
import com.hjq.http.model.HttpHeaders
import com.hjq.http.model.HttpParams
import com.hjq.http.request.HttpRequest
import com.hjq.toast.ToastLogInterceptor
import com.hjq.toast.ToastUtils
import com.tencent.mmkv.MMKV
import io.socket.client.Socket
import okhttp3.OkHttpClient



class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        initSdk(this)

        instance = this

    }

    companion object {
        var instance: AppApplication? = null

        fun applicationContext(): AppApplication {
            return instance as AppApplication
        }
        @SuppressLint("HardwareIds")
        fun initSdk(application: Application) {
            // Khởi tạo toast
            ToastUtils.init(application, ToastStyle())

            // cài đặt Toast
            ToastUtils.setInterceptor(ToastLogInterceptor())

            // cài đặt Crash
//            CrashHandler.register(application)

            //Lưu cache
            MMKV.initialize(application)

            // Activity
            ActivityManager.getInstance().init(application)
            // Create the Collector
            val chuckerCollector = ChuckerCollector(
                context = application,
                // Toggles visibility of the notification
                showNotification = true,
                // Allows to customize the retention period of collected data
                retentionPeriod = RetentionManager.Period.ONE_HOUR
            )
            val chuckerInterceptor = ChuckerInterceptor.Builder(application)
                // The previously created Collector
                .collector(chuckerCollector)
                // The max body content length in bytes, after this responses will be truncated.
                .maxContentLength(250_000L)
                // List of headers to replace with ** in the Chucker UI
                .redactHeaders("Auth-Token", "Bearer")
                // Read the whole response body even when the client does not consume the response completely.
                // This is useful in case of parsing errors or when the response body
                // is closed before being read like in Retrofit with Void and Unit types.
                .alwaysReadResponseBody(true)
                // Use decoder when processing request and response bodies. When multiple decoders are installed they
                // are applied in an order they were added.
                // Controls Android shortcut creation.
                .build()

            // Khởi tạo khung yêu cầu mạng
            val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                    .apply { if (HttpLoggerCache.getHttpLogEnable()) {
                        addInterceptor(chuckerInterceptor)
                    }}
                    .build()

            //Khởi tạo máy chủ java
            EasyConfig.with(okHttpClient)
                //Thiết lập cấu hình máy chủ
                .setServer(RequestServer())
                //Đặt chính sách xử lý yêu cầu
                .setHandler(RequestHandler(application))
                // Đặt bộ chặn tham số yêu cầu
                .setInterceptor(object : IRequestInterceptor {
                    override fun interceptArguments(
                        httpRequest: HttpRequest<*>, params: HttpParams, headers: HttpHeaders
                    ) {
                        headers.put("time", System.currentTimeMillis().toString())
                        headers.put("Content-Type", "application/json")
                        headers.put("Cache-Control", "no-store")
                        headers.put("Cache-Control", "no-cache")
                        headers.put(
                            "udid", Settings.Secure.getString(
                                application.contentResolver, Settings.Secure.ANDROID_ID
                            )
                        )
                        headers.put("os_name", "android")
                    }
                })
                // Đặt số lần yêu cầu thử lại
                .setRetryCount(1)
                // Đặt thời gian thử lại yêu cầu
                .setRetryTime(2000).into()


        }

        var socketChat: Socket? = null





    }
}