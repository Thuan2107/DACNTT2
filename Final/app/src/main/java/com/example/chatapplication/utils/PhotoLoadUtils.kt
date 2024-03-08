package com.example.chatapplication.utils

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.chatapplication.R
import com.example.chatapplication.app.AppApplication
import com.example.chatapplication.cache.ConfigCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



object PhotoLoadUtils {
    //Custom link url để load hình
    fun getLinkPhoto(photo: String?): String {
//        return String.format("%s%s", ConfigCache.getConfig().apiUploadShort, photo)
        return ""
    }
    fun loadImageByGlide(view: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) {
            // Start a new coroutine to load the image asynchronously
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            val job = coroutineScope.launch {
                Glide.with(AppApplication.applicationContext())
                    .load(if (!url.contains("/")) getLinkPhoto(url) else url)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .placeholder(R.drawable.ic_default)
                    .error(R.drawable.ic_default)
                    .into(view)
            }
            // Store the coroutine job as a tag to the view
            view.tag = job
        } else {
            view.setImageResource(R.drawable.ic_default)
        }
    }



    fun loadImageBackgroundByGlide(view: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) {
            // Start a new coroutine to load the image asynchronously
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            val job = coroutineScope.launch {
                Glide.with(AppApplication.applicationContext())
                    .load(if (!url.contains("/")) getLinkPhoto(url) else url)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .placeholder(R.drawable.bg_white_default)
                    .error(R.drawable.bg_white_default)
                    .into(view)
            }
            // Store the coroutine job as a tag to the view
            view.tag = job
        } else {
            view.setImageResource(R.drawable.bg_white_default)
        }
    }



    fun loadImageBackgroundBrandByGlide(view: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) {
            // Start a new coroutine to load the image asynchronously
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            val job = coroutineScope.launch {
                Glide.with(AppApplication.applicationContext())
                    .load(if (!url.contains("/")) getLinkPhoto(url) else url)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .placeholder(R.drawable.ic_default)
                    .error(R.drawable.ic_default)
                    .transform(
                        MultiTransformation(
                            RoundedCorners(
                                AppApplication.applicationContext().resources.getDimension(R.dimen.dp_8).toInt()
                            )
                        )
                    )
                    .into(view)
            }
            // Store the coroutine job as a tag to the view
            view.tag = job
        } else {
            view.setImageResource(R.drawable.ic_default)
        }
    }



    fun loadImageAvatarByGlide(view: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) {
            // Start a new coroutine to load the image asynchronously
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            val job = coroutineScope.launch {
                Glide.with(AppApplication.applicationContext())
                    .load(if (!url.contains("/")) getLinkPhoto(url) else url)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .placeholder(R.drawable.ic_user_default)
                    .error(R.drawable.ic_user_default)
                    .transform(MultiTransformation(CircleCrop()))
                    .into(view)
            }
            // Store the coroutine job as a tag to the view
            view.tag = job
        } else {
            view.setImageResource(R.drawable.ic_user_default)
        }
    }

    fun loadImageAvatarGroupByGlide(view: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) {
            // Start a new coroutine to load the image asynchronously
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            val job = coroutineScope.launch {
                Glide.with(AppApplication.applicationContext())
                    .load(if (!url.contains("/")) getLinkPhoto(url) else url)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .placeholder(R.drawable.ic_empty_group)
                    .error(R.drawable.ic_empty_group)
                    .transform(MultiTransformation(CircleCrop()))
                    .into(view)
            }
            // Store the coroutine job as a tag to the view
            view.tag = job
        } else {
            view.setImageResource(R.drawable.ic_empty_group)
        }
    }

    fun loadImageLinkJoinGroupByGlide(view: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) {
            // Start a new coroutine to load the image asynchronously
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            val job = coroutineScope.launch {
                Glide.with(AppApplication.applicationContext())
                    .load(if (!url.contains("/")) getLinkPhoto(url) else url)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .transform(
                        MultiTransformation(
                            RoundedCorners(
                                AppApplication.applicationContext().resources.getDimension(
                                    R.dimen.dp_8
                                ).toInt()
                            )
                        )
                    )
                    .into(view)
            }
            // Store the coroutine job as a tag to the view
            view.tag = job
        } else {
            view.setImageResource(R.mipmap.ic_launcher)
        }
    }



    //--------------------------------------------Chat--------------------------------------------//

    @SuppressLint("ResourceType")
    fun loadPhoto(view: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) {
            // Start a new coroutine to load the image asynchronously
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            val job = coroutineScope.launch {
                Glide.with(view)
                    .load(if (!url.contains("/")) getLinkPhoto(url) else url)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .placeholder(R.drawable.ic_empty_media)
                    .error(R.drawable.ic_empty_media)
                    .into(view)
            }
            // Store the coroutine job as a tag to the view
            view.tag = job
        } else {
            view.setImageDrawable(
                AppCompatResources.getDrawable(
                    AppApplication.applicationContext(),
                    R.drawable.ic_empty_media
                )
            )
        }
    }

    private fun resizeBitmapChat(source: Bitmap, maxLength: Int): Bitmap? {
        return try {
            if (source.height > source.width) {
                val aspectRatio = source.width.toDouble() / source.height.toDouble()
                var targetWidth = (maxLength * aspectRatio).toInt()
                if (targetWidth > maxLength) {
                    targetWidth = maxLength
                }
                Bitmap.createScaledBitmap(source, targetWidth, maxLength, false)
            } else if (source.height == source.width) {
                Bitmap.createScaledBitmap(source, maxLength, maxLength, false)
            } else {
                val aspectRatio = source.height.toDouble() / source.width.toDouble()
                val targetHeight = (maxLength * aspectRatio).toInt()
                Bitmap.createScaledBitmap(source, maxLength, targetHeight, false)
            }
        } catch (e: java.lang.Exception) {
            source
        }
    }



//--------------------------------------------------------------------------------------------//



}