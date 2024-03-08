package com.example.chatapplication.holder

import android.content.Intent
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.chatapplication.R
import com.example.chatapplication.adapter.MessageAdapter
import com.example.chatapplication.cache.UserCache
import com.example.chatapplication.databinding.ItemMessageNotificationBinding
import com.example.chatapplication.model.entity.Message
import com.example.chatapplication.utils.AppUtils
import com.example.chatapplication.utils.PhotoLoadUtils


class OutGroupHandle(
    private val binding: ItemMessageNotificationBinding,
    private val data: Message,
    private val position: Int,
    private var adapter: MessageAdapter, private val chatHandle: MessageAdapter.ChatHandle,
) {
    fun setData() {
        if (data.user.userId == UserCache.getUser().id) {
            binding.llAvatarOne.visibility = View.VISIBLE
            binding.ivAvatarOne.visibility = View.VISIBLE
            PhotoLoadUtils.loadImageAvatarByGlide(binding.ivAvatarOne, data.user.avatar)
            val nameText = adapter.getString(R.string.me_message)
            val nameTextView = String.format(
                    "%s %s",
                    nameText,
                    adapter.getString(R.string.has_left_the_group)
            )
            binding.tvNotificationAddUser.text = AppUtils.fromHtml(nameTextView)
        } else {
            binding.llAvatarOne.visibility = View.VISIBLE
            binding.ivAvatarOne.visibility = View.VISIBLE
            PhotoLoadUtils.loadImageAvatarByGlide(binding.ivAvatarOne, data.user.avatar)
            val nameText = data.user.fullName.trim()
            val nameTextView = String.format(
                    "%s %s",
                    "<b> $nameText" + "</b> ".trim(),
                    adapter.getString(R.string.has_left_the_group)
            )
            binding.tvNotificationAddUser.text = AppUtils.fromHtml(nameTextView)
        }

        val span = SpannableStringBuilder(binding.tvNotificationAddUser.text)
        if (data.user.userId != UserCache.getUser().id) {
            val text = span.toString()
            val startPosition = text.indexOf(data.user.fullName)
            if (startPosition != -1) {
                val clickableSpanSeeDetailUser: ClickableSpan = object : ClickableSpan() {
                    override fun onClick(textView: View) {
//                        val intent =
//                                Intent(
//                                        adapter.getContext(),
//                                        Class.forName(ModuleClassConstants.INFO_CUSTOMER)
//                                )
//                        intent.putExtra(AppConstants.ID_USER, data.user.userId)
//                        adapter.getContext().startActivity(intent)
                        Log.d("click view info customer", "click" )
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                        ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                        ds.color = adapter.getColor(R.color.gray_900)
                    }
                }
                span.setSpan(
                        clickableSpanSeeDetailUser,
                        startPosition,
                        startPosition + data.user.fullName.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        binding.tvNotificationAddUser.text = span
        binding.tvNotificationAddUser.movementMethod = LinkMovementMethod.getInstance()
    }


}