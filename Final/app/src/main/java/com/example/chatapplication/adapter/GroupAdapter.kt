package com.example.chatapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.chatapplication.R
import com.example.chatapplication.cache.UserCache
import com.example.chatapplication.constant.AppConstants
import com.example.chatapplication.databinding.ItemGroupBinding
import com.example.chatapplication.model.entity.GroupChat
import com.example.chatapplication.model.entity.MediaList
import com.example.chatapplication.utils.AppUtils
import com.example.chatapplication.utils.AppUtils.hide
import com.example.chatapplication.utils.AppUtils.show
import com.example.chatapplication.utils.PhotoLoadUtils
import com.example.chatapplication.utils.TimeUtils
import vn.techres.line.app.AppAdapter

class GroupAdapter(context: Context) : AppAdapter<GroupChat>(context) {
    private var onClickGroupChat: GroupChatListener? = null


    override fun getItemId(position: Int): Long {
        return getItem(position).conversationId.hashCode().toLong()
    }

    fun setGroupChatListener(listener: GroupChatListener) {
        this.onClickGroupChat = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {

        val binding = ItemGroupBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemGroupBinding) :
            AppViewHolder(binding.root) {

        @SuppressLint("SetTextI18n", "ResourceAsColor")
        override fun onBindView(position: Int) {
            val item = getItem(position)

            when (item.type) {
//                AppConstants.TYPE_GROUP -> {
//                    binding.tvName.text = item.name.trim()
//                    PhotoLoadUtils.loadImageAvatarGroupByGlide(
//                            binding.imgAvatar,
//                            item.avatar.original.url
//                    )
//                    binding.imvOnline.hide()
//                    if (item.lastMessage.tag.size > 0) {
//                        item.lastMessage.tag.forEach {
//                            val tagName = if (it.user.userId == AppConstants.USER_TAG_ALL_ID) {
//                                "@All"
//                            } else {
//                                "@${it.user.fullName}"
//                            }
//                            item.lastMessage.message = item.lastMessage.message.replace(
//                                    it.key, tagName
//                            )
//                        }
//                    }
//
//                    if (item.lastMessage.user.userId == UserCache.getUser().id) {
//                        when (item.lastMessage.type) {
//                            MessageTypeChatConstants.NEW_GROUP, MessageTypeChatConstants.EMPTY -> {
//                                binding.tvContent.text =
//                                        getString(R.string.content_empty)
//                            }
//
//                            MessageTypeChatConstants.TEXT, MessageTypeChatConstants.REPLY -> {
//                                val content = if (item.lastMessage.thumb.isThumb == 0)
//                                    item.lastMessage.message
//                                else {
//                                    if (item.lastMessage.thumb.typeSystem == AppConstants.TYPE_SYSTEM_LINK_JOIN) {
//                                        "${getString(R.string.type_link)} ${ConfigCache.getConfig().apiJoinGroup}${item.lastMessage.thumb.objectId}"
//                                    } else {
//                                        "${getString(R.string.type_link)} ${item.lastMessage.thumb.url}"
//                                    }
//                                }
//                                binding.tvContent.text = String.format(
//                                        "%s: %s",
//                                        getString(R.string.me_message),
//                                        content
//                                )
//                            }
//
//                            MessageTypeChatConstants.IMAGE -> {
//                                binding.tvContent.text = String.format(
//                                        "%s: %s",
//                                        getString(R.string.me_message),
//                                        getString(R.string.type_image)
//                                )
//                            }
//
//                            MessageTypeChatConstants.VIDEO -> {
//                                binding.tvContent.text = String.format(
//                                        "%s: %s",
//                                        getString(R.string.me_message),
//                                        getString(R.string.type_video)
//                                )
//                            }
//
//                            MessageTypeChatConstants.AUDIO -> {
//                                binding.tvContent.text = String.format(
//                                        "%s: %s",
//                                        getString(R.string.me_message),
//                                        getString(R.string.type_audio)
//                                )
//                            }
//
//                            MessageTypeChatConstants.FILE -> {
//                                binding.tvContent.text = String.format(
//                                        "%s: %s",
//                                        getString(R.string.me_message),
//                                        getString(R.string.type_file)
//                                )
//                            }
//
//                            MessageTypeChatConstants.UPDATE_NAME -> {
//                                binding.tvContent.text = String.format(
//                                        "%s %s",
//                                        getString(R.string.me_message),
//                                        getString(R.string.update_name)
//                                )
//                            }
//
//                            MessageTypeChatConstants.UPDATE_AVATAR -> {
//                                binding.tvContent.text = String.format(
//                                        "%s %s",
//                                        getString(R.string.me_message),
//                                        getString(R.string.update_avt)
//                                )
//                            }
//
//                            MessageTypeChatConstants.UPDATE_BACKGROUND -> {
//                                binding.tvContent.text = getString(R.string.updated_avatar)
//                            }
//
//                            MessageTypeChatConstants.REMOVE_USER -> {
//                                val removedUser = if (item.lastMessage.userTarget.isNotEmpty()) {
//                                    item.lastMessage.userTarget[0].fullName
//                                } else {
//                                    ""
//                                }
//                                binding.tvContent.text = String.format(
//                                        "%s %s",
//                                        removedUser,
//                                        getString(R.string.me_remove_user)
//                                )
//                            }
//
//                            MessageTypeChatConstants.ADD_NEW_USER -> {
//                                if (item.lastMessage.userTarget.size == 1 && item.lastMessage.userTarget[0].userId == UserCache.getUser().id) {
//                                    binding.tvContent.text =
//                                            getString(R.string.you_join_conversation)
//                                } else {
//                                    val listNameUser = ArrayList<String>()
//                                    if (item.lastMessage.userTarget.isNotEmpty()) {
//                                        item.lastMessage.userTarget.forEach {
//                                            listNameUser.add(it.fullName)
//                                        }
//                                    }
//                                    val finalTextName = if (listNameUser.size == 1) {
//                                        listNameUser[0]
//                                    } else if (listNameUser.size >= 2) {
//                                        "${listNameUser[0]} và ${listNameUser.size - 1} người khác"
//                                    } else {
//                                        ""
//                                    }
//                                    binding.tvContent.text = String.format(
//                                            "%s được Bạn %s",
//                                            finalTextName,
//                                            getString(R.string.add_new_user)
//                                    )
//                                }
//                            }
//
//                            MessageTypeChatConstants.CHANGE_PERMISSION_USER -> {
//                                val finalTextName = if (item.lastMessage.userTarget.isNotEmpty()) {
//                                    item.lastMessage.userTarget[0].fullName
//                                } else {
//                                    ""
//                                }
//                                binding.tvContent.text = String.format(
//                                        "%s %s %s",
//                                        getString(R.string.you_have_appoint),
//                                        finalTextName,
//                                        getString(R.string.be_the_leader)
//                                )
//                            }
//
//                            MessageTypeChatConstants.REVOKE_MESSAGE -> {
//                                binding.tvContent.text = String.format(
//                                        "%s: %s",
//                                        getString(R.string.me_message),
//                                        getString(R.string.type_revoke_message)
//                                )
//                            }
//
//                            MessageTypeChatConstants.STICKER -> {
//                                binding.tvContent.text = String.format(
//                                        "%s: %s",
//                                        getString(R.string.me_message),
//                                        getString(R.string.type_sticker)
//                                )
//                            }
//
//                            MessageTypeChatConstants.CONFIRM_NEW_MEMBER -> {
//                                binding.tvContent.text =
//                                        String.format(
//                                                "%s %s",
//                                                getString(R.string.confirmed_member),
//                                                getString(R.string.need_browsing)
//                                        )
//                            }
//
//                            MessageTypeChatConstants.OFF_IS_CONFIRM_NEW_MEMBER -> {
//                                binding.tvContent.text =
//                                        String.format(
//                                                "%s %s",
//                                                getString(R.string.confirmed_member),
//                                                getString(R.string.no_browsing_required)
//                                        )
//                            }
//
//                            MessageTypeChatConstants.MESSAGE_PINNED -> {
//                                binding.tvContent.text =
//                                        String.format(
//                                                "%s %s",
//                                                getString(R.string.me_message),
//                                                getString(R.string.message_pinned)
//                                        )
//                            }
//
//                            MessageTypeChatConstants.MESSAGE_REMOVE_PINNED -> {
//                                binding.tvContent.text = String.format(
//                                        "%s: %s",
//                                        getString(R.string.me_message),
//                                        getString(R.string.message_remove_pinned)
//                                )
//                            }
//
//                            MessageTypeChatConstants.MESSAGE_ADD_DEPUTY_CONVERSATION -> {
//                                val listNameUser = ArrayList<String>()
//                                if (item.lastMessage.userTarget.isNotEmpty()) {
//                                    item.lastMessage.userTarget.forEach {
//                                        listNameUser.add(it.fullName)
//                                    }
//                                }
//                                val finalTextName = if (listNameUser.size == 1) {
//                                    listNameUser[0]
//                                } else if (listNameUser.size >= 2) {
//                                    "${listNameUser[0]} và ${listNameUser.size - 1} người khác"
//                                } else {
//                                    ""
//                                }
//                                binding.tvContent.text = String.format(
//                                        "%s %s %s",
//                                        getString(R.string.you_have_appoint),
//                                        finalTextName,
//                                        getString(R.string.be_the_deputy_team)
//                                )
//                            }
//
//                            MessageTypeChatConstants.MESSAGE_REMOVE_DEPUTY_CONVERSATION -> {
//                                val listNameUser = ArrayList<String>()
//                                if (item.lastMessage.userTarget.isNotEmpty()) {
//                                    item.lastMessage.userTarget.forEach {
//                                        listNameUser.add(it.fullName)
//                                    }
//                                }
//                                val finalTextName = if (listNameUser.size == 1) {
//                                    listNameUser[0]
//                                } else if (listNameUser.size >= 2) {
//                                    "${listNameUser[0]} và ${listNameUser.size - 1} người khác"
//                                } else {
//                                    ""
//                                }
//                                binding.tvContent.text = String.format(
//                                        "%s %s",
//                                        getString(R.string.you_delete_deputy_team),
//                                        finalTextName,
//                                )
//                            }
//
//                            MessageTypeChatConstants.MESSAGE_CREATE_VOTE -> {
//                                binding.tvContent.text = String.format(
//                                        "%s %s",
//                                        getString(R.string.me_message),
//                                        getString(R.string.created_new_vote)
//                                )
//                            }
//
//                            MessageTypeChatConstants.MESSAGE_VOTE -> {
//                                binding.tvContent.text = String.format(
//                                        "%s %s",
//                                        getString(R.string.me_message),
//                                        getString(R.string.joined_vote)
//                                )
//                            }
//
//                            MessageTypeChatConstants.MESSAGE_CHANGE_VOTE -> {
//                                binding.tvContent.text =
//                                        getString(R.string.my_change_options_vote)
//                            }
//
//                            MessageTypeChatConstants.ADD_OPTION_VOTE -> {
//                                binding.tvContent.text = String.format(
//                                        "%s %s",
//                                        getString(R.string.me_message),
//                                        getString(R.string.added_option_vote)
//                                )
//                            }
//
//                            MessageTypeChatConstants.MEMBERS_WAITING_GROUP -> {
//                                val listNameUser = ArrayList<String>()
//                                if (item.lastMessage.userTarget.isNotEmpty()) {
//                                    item.lastMessage.userTarget.forEach {
//                                        listNameUser.add(it.fullName)
//                                    }
//                                }
//                                val finalTextName = if (listNameUser.size == 1) {
//                                    listNameUser[0]
//                                } else if (listNameUser.size >= 2) {
//                                    "${listNameUser[0]} và ${listNameUser.size - 1} người khác"
//                                } else {
//                                    ""
//                                }
//                                when (item.myPermission) {
//                                    ConversationTypeConstants.OWNER, ConversationTypeConstants.DEPUTY -> {
//                                        binding.tvContent.text = String.format(
//                                                "%s %s",
//                                                finalTextName,
//                                                getString(R.string.i_add_new_user)
//                                        )
//                                    }
//
//                                    else -> {
//                                        binding.tvContent.text = String.format(
//                                                "%s %s",
//                                                finalTextName,
//                                                getString(R.string.leader_and_vice_president_to_join_the_group)
//                                        )
//                                    }
//                                }
//                            }
//
//                            MessageTypeChatConstants.BLOCK_VOTE -> {
//                                binding.tvContent.text = String.format(
//                                        "%s %s",
//                                        getString(R.string.me_message),
//                                        getString(R.string.block_vote)
//                                )
//                            }
//
//                            MessageTypeChatConstants.PINNED_VOTE -> {
//                                binding.tvContent.text = String.format(
//                                        "%s %s",
//                                        getString(R.string.me_message),
//                                        getString(R.string.vote_pinned)
//                                )
//                            }
//
//                            MessageTypeChatConstants.REMOVE_PINNED_VOTE -> {
//                                binding.tvContent.text = String.format(
//                                        "%s %s",
//                                        getString(R.string.me_message),
//                                        getString(R.string.vote_remove_pinned)
//                                )
//                            }
//
//                            else -> binding.tvContent.text =
//                                    getString(R.string.not_yet_assembled_sorry_for_the_inconvenience)
//                        }
//                    } else {
//                        when (item.lastMessage.type) {
//                            MessageTypeChatConstants.NEW_GROUP, MessageTypeChatConstants.EMPTY -> {
//                                binding.tvContent.text = getString(R.string.content_empty)
//                            }
//
//                            MessageTypeChatConstants.TEXT, MessageTypeChatConstants.REPLY -> {
//                                val content = if (item.lastMessage.thumb.isThumb == 0)
//                                    item.lastMessage.message
//                                else {
//                                    if (item.lastMessage.thumb.typeSystem == AppConstants.TYPE_SYSTEM_LINK_JOIN) {
//                                        "${getString(R.string.type_link)} ${ConfigCache.getConfig().apiJoinGroup}${item.lastMessage.thumb.objectId}"
//                                    } else {
//                                        "${getString(R.string.type_link)} ${item.lastMessage.thumb.url}"
//                                    }
//                                }
//                                binding.tvContent.text = String.format(
//                                        "%s: %s",
//                                        item.lastMessage.user.fullName,
//                                        content
//                                )
//                            }
//
//                            MessageTypeChatConstants.IMAGE -> {
//                                binding.tvContent.text = String.format(
//                                        "%s: %s",
//                                        item.lastMessage.user.fullName,
//                                        getString(R.string.type_image)
//                                )
//                            }
//
//                            MessageTypeChatConstants.VIDEO -> {
//                                binding.tvContent.text = String.format(
//                                        "%s: %s",
//                                        item.lastMessage.user.fullName,
//                                        getString(R.string.type_video)
//                                )
//                            }
//
//                            MessageTypeChatConstants.AUDIO -> {
//                                binding.tvContent.text = String.format(
//                                        "%s: %s",
//                                        item.lastMessage.user.fullName,
//                                        getString(R.string.type_audio)
//                                )
//                            }
//
//                            MessageTypeChatConstants.FILE -> {
//                                binding.tvContent.text = String.format(
//                                        "%s: %s",
//                                        item.lastMessage.user.fullName,
//                                        getString(R.string.type_file)
//                                )
//                            }
//
//                            MessageTypeChatConstants.UPDATE_NAME -> {
//                                binding.tvContent.text = String.format(
//                                        "%s %s",
//                                        item.lastMessage.user.fullName,
//                                        getString(R.string.update_name)
//                                )
//                            }
//
//                            MessageTypeChatConstants.UPDATE_AVATAR -> {
//                                binding.tvContent.text = String.format(
//                                        "%s %s",
//                                        item.lastMessage.user.fullName,
//                                        getString(R.string.update_avt)
//                                )
//                            }
//
//                            MessageTypeChatConstants.UPDATE_BACKGROUND -> {
//                                binding.tvContent.text = String.format(
//                                        "%s %s",
//                                        item.lastMessage.user.fullName,
//                                        getString(R.string.you_update_avt)
//                                )
//                            }
//
//                            MessageTypeChatConstants.REMOVE_USER -> {
//                                val removedUser = if (item.lastMessage.userTarget.isNotEmpty()) {
//                                    item.lastMessage.userTarget[0].fullName
//                                } else {
//                                    ""
//                                }
//                                binding.tvContent.text = String.format(
//                                        "%s được %s %s",
//                                        removedUser,
//                                        item.lastMessage.user.fullName,
//                                        getString(R.string.remove_from_group)
//                                )
//                            }
//
//                            MessageTypeChatConstants.ADD_NEW_USER -> {
//                                if (item.lastMessage.userTarget.size == 1 && item.lastMessage.userTarget[0].userId == item.lastMessage.user.userId) {
//                                    binding.tvContent.text = getString(R.string.they_join_conversation, item.lastMessage.user.fullName)
//                                } else {
//                                    val listNameUser = ArrayList<String>()
//                                    if (item.lastMessage.userTarget.isNotEmpty()) {
//                                        item.lastMessage.userTarget.forEach {
//                                            listNameUser.add(it.fullName)
//                                        }
//                                    }
//                                    val isIncludeYourself =
//                                            item.lastMessage.userTarget.any { it.userId == UserCache.getUser().id }
//                                    val finalTextName = if (listNameUser.size == 1) {
//                                        if (isIncludeYourself) {
//                                            getString(R.string.me_message)
//                                        } else {
//                                            listNameUser[0]
//                                        }
//                                    } else if (listNameUser.size >= 2) {
//                                        if (isIncludeYourself) {
//                                            "${getString(R.string.me_message)} và ${listNameUser.size - 1} người khác"
//                                        } else {
//                                            "${listNameUser[0]} và ${listNameUser.size - 1} người khác"
//                                        }
//                                    } else {
//                                        ""
//                                    }
//                                    binding.tvContent.text = String.format(
//                                            "%s %s %s %s",
//                                            finalTextName,
//                                            getString(R.string.okey),
//                                            item.lastMessage.user.fullName,
//                                            getString(R.string.add_new_user)
//                                    )
//                                }
//                            }
//
//                            MessageTypeChatConstants.CHANGE_PERMISSION_USER -> {
//                                val lastMessage = if (item.lastMessage.userTarget.isNotEmpty()) {
//                                    if (item.lastMessage.userTarget[0].userId != UserCache.getUser().id) {
//                                        String.format(
//                                                "%s %s %s",
//                                                item.lastMessage.user.fullName,
//                                                getString(R.string.appointed),
//                                                item.lastMessage.userTarget[0].fullName,
//                                                getString(R.string.be_the_leader)
//                                        )
//                                    } else {
//                                        "${item.lastMessage.user.fullName} đã bổ nhiệm bạn thành trưởng nhóm"
//                                    }
//                                } else {
//                                    "đã được bổ nhiệm làm trưởng nhóm"
//                                }
//
//                                binding.tvContent.text = lastMessage
//                            }
//
//                            MessageTypeChatConstants.USER_OUT_GROUP -> {
//                                binding.tvContent.text = String.format(
//                                        "%s %s",
//                                        item.lastMessage.user.fullName,
//                                        getString(R.string.out_group)
//                                )
//                            }
//
//                            MessageTypeChatConstants.REVOKE_MESSAGE -> {
//                                binding.tvContent.text = String.format(
//                                        "%s: %s",
//                                        item.lastMessage.user.fullName,
//                                        getString(R.string.type_revoke_message)
//                                )
//                            }
//
//                            MessageTypeChatConstants.STICKER -> {
//                                binding.tvContent.text = String.format(
//                                        "%s: %s",
//                                        item.lastMessage.user.fullName,
//                                        getString(R.string.type_sticker)
//                                )
//                            }
//
//                            MessageTypeChatConstants.CONFIRM_NEW_MEMBER -> {
//                                binding.tvContent.text = String.format(
//                                        "%s %s",
//                                        getString(R.string.confirmed_member),
//                                        getString(R.string.need_browsing)
//                                )
//                            }
//
//                            MessageTypeChatConstants.OFF_IS_CONFIRM_NEW_MEMBER -> {
//                                binding.tvContent.text = String.format(
//                                        "%s %s",
//                                        getString(R.string.confirmed_member),
//                                        getString(R.string.no_browsing_required)
//                                )
//                            }
//
//                            MessageTypeChatConstants.MESSAGE_PINNED -> {
//                                binding.tvContent.text = String.format(
//                                        "%s: %s",
//                                        item.lastMessage.user.fullName,
//                                        getString(R.string.message_pinned)
//                                )
//                            }
//
//                            MessageTypeChatConstants.MESSAGE_REMOVE_PINNED -> {
//                                binding.tvContent.text = String.format(
//                                        "%s: %s",
//                                        item.lastMessage.user.fullName,
//                                        getString(R.string.message_remove_pinned)
//                                )
//                            }
//
//                            MessageTypeChatConstants.MESSAGE_ADD_DEPUTY_CONVERSATION -> {
//                                val listNameUser = ArrayList<String>()
//                                if (item.lastMessage.userTarget.isNotEmpty()) {
//                                    item.lastMessage.userTarget.forEach {
//                                        listNameUser.add(it.fullName)
//                                    }
//                                }
//                                val isIncludeYourself =
//                                        item.lastMessage.userTarget.any { it.userId == UserCache.getUser().id }
//                                val finalTextName = if (listNameUser.size == 1) {
//                                    if (isIncludeYourself) {
//                                        getString(R.string.me_message)
//                                    } else {
//                                        listNameUser[0]
//                                    }
//                                } else if (listNameUser.size >= 2) {
//                                    if (isIncludeYourself) {
//                                        "${getString(R.string.me_message)} và ${listNameUser.size - 1} người khác"
//                                    } else {
//                                        "${listNameUser[0]} và ${listNameUser.size - 1} người khác"
//                                    }
//                                } else {
//                                    ""
//                                }
//                                binding.tvContent.text = String.format(
//                                        "%s %s %s %s",
//                                        item.lastMessage.user.fullName,
//                                        getString(R.string.appointed),
//                                        finalTextName,
//                                        getString(R.string.be_the_deputy_team)
//                                )
//                            }
//
//                            MessageTypeChatConstants.MESSAGE_REMOVE_DEPUTY_CONVERSATION -> {
//                                val listNameUser = ArrayList<String>()
//                                if (item.lastMessage.userTarget.isNotEmpty()) {
//                                    item.lastMessage.userTarget.forEach {
//                                        listNameUser.add(it.fullName)
//                                    }
//                                }
//                                val isIncludeYourself =
//                                        item.lastMessage.userTarget.any { it.userId == UserCache.getUser().id }
//                                val finalTextName = if (listNameUser.size == 1) {
//                                    if (isIncludeYourself) {
//                                        getString(R.string.me_message)
//                                    } else {
//                                        listNameUser[0]
//                                    }
//                                } else if (listNameUser.size >= 2) {
//                                    if (isIncludeYourself) {
//                                        "${getString(R.string.me_message)} và ${listNameUser.size - 1} người khác"
//                                    } else {
//                                        "${listNameUser[0]} và ${listNameUser.size - 1} người khác"
//                                    }
//                                } else {
//                                    ""
//                                }
//                                binding.tvContent.text = String.format(
//                                        "%s %s %s",
//                                        item.lastMessage.user.fullName,
//                                        getString(R.string.delete_deputy_team),
//                                        finalTextName
//                                )
//                            }
//
//                            MessageTypeChatConstants.MESSAGE_CREATE_VOTE -> {
//                                binding.tvContent.text = String.format(
//                                        "%s %s",
//                                        item.lastMessage.user.fullName,
//                                        getString(R.string.created_new_vote)
//                                )
//                            }
//
//                            MessageTypeChatConstants.MESSAGE_VOTE -> {
//                                binding.tvContent.text = String.format(
//                                        "%s %s",
//                                        item.lastMessage.user.fullName,
//                                        getString(R.string.joined_vote)
//                                )
//                            }
//
//                            MessageTypeChatConstants.MESSAGE_CHANGE_VOTE -> {
//                                binding.tvContent.text = String.format(
//                                        "%s %s",
//                                        item.lastMessage.user.fullName,
//                                        getString(R.string.change_options_vote)
//                                )
//                            }
//
//                            MessageTypeChatConstants.ADD_OPTION_VOTE -> {
//                                binding.tvContent.text = String.format(
//                                        "%s %s",
//                                        item.lastMessage.user.fullName,
//                                        getString(R.string.added_option_vote)
//                                )
//                            }
//
//                            MessageTypeChatConstants.MEMBERS_WAITING_GROUP -> {
//                                val listNameUser = ArrayList<String>()
//                                if (item.lastMessage.userTarget.isNotEmpty()) {
//                                    item.lastMessage.userTarget.forEach {
//                                        listNameUser.add(it.fullName)
//                                    }
//                                }
//                                val isIncludeYourself =
//                                        item.lastMessage.userTarget.any { it.userId == UserCache.getUser().id }
//                                val finalTextName = if (listNameUser.size == 1) {
//                                    if (isIncludeYourself) {
//                                        getString(R.string.me_message)
//                                    } else {
//                                        listNameUser[0]
//                                    }
//                                } else if (listNameUser.size >= 2) {
//                                    if (isIncludeYourself) {
//                                        "${getString(R.string.me_message)} và ${listNameUser.size - 1} người khác"
//                                    } else {
//                                        "${listNameUser[0]} và ${listNameUser.size - 1} người khác"
//                                    }
//                                } else {
//                                    ""
//                                }
//                                when (item.myPermission) {
//                                    ConversationTypeConstants.OWNER, ConversationTypeConstants.DEPUTY -> {
//                                        binding.tvContent.text = String.format(
//                                                "%s %s",
//                                                finalTextName,
//                                                getString(R.string.i_add_new_user)
//                                        )
//                                    }
//
//                                    else -> {
//                                        binding.tvContent.text = String.format(
//                                                "%s %s",
//                                                finalTextName,
//                                                getString(R.string.leader_and_vice_president_to_join_the_group)
//                                        )
//                                    }
//                                }
//                            }
//
//                            MessageTypeChatConstants.BLOCK_VOTE -> {
//                                binding.tvContent.text = String.format(
//                                        "%s %s",
//                                        item.lastMessage.user.fullName,
//                                        getString(R.string.block_vote)
//                                )
//                            }
//
//                            MessageTypeChatConstants.PINNED_VOTE -> {
//                                binding.tvContent.text = String.format(
//                                        "%s %s",
//                                        item.lastMessage.user.fullName,
//                                        getString(R.string.vote_pinned)
//                                )
//                            }
//
//                            MessageTypeChatConstants.REMOVE_PINNED_VOTE -> {
//                                binding.tvContent.text = String.format(
//                                        "%s %s",
//                                        item.lastMessage.user.fullName,
//                                        getString(R.string.vote_remove_pinned)
//                                )
//                            }
//
//                            else -> binding.tvContent.text =
//                                    getString(R.string.not_yet_assembled_sorry_for_the_inconvenience)
//                        }
//                    }
//                }

                AppConstants.TYPE_PRIVATE -> {
                    if (item.contactType == AppConstants.FRIEND)
                        binding.imvOnline.show()
                    else
                        binding.imvOnline.hide()

                    if (item.isOnline == 1) {
                        binding.imvOnline.setBackgroundResource(R.drawable.ic_online)
                    } else {
                        binding.imvOnline.setBackgroundResource(R.drawable.ic_off)
                    }
                    if (item.userStatus == AppConstants.STATUS_ACTIVE) {
                        binding.tvName.text = item.name.trim()
                        PhotoLoadUtils.loadImageAvatarByGlide(
                                binding.imgAvatar,
                                item.avatar
                        )
                    } else {
                        binding.tvName.text =
                                getString(R.string.undefine_account)
                        binding.imgAvatar.setImageResource(R.drawable.ic_user_default)
                    }

                    binding.tvContent.text = getString(R.string.content_empty)
                    

//                    when (item.lastMessage.type) {
//                        MessageTypeChatConstants.EMPTY, MessageTypeChatConstants.NEW_GROUP -> {
//                            binding.tvContent.text = getString(R.string.content_empty)
//                        }
//
//                        MessageTypeChatConstants.TEXT, MessageTypeChatConstants.REPLY -> {
//                            binding.tvContent.text = if (item.lastMessage.thumb.isThumb == 0)
//                                item.lastMessage.message
//                            else {
//                                if (item.lastMessage.thumb.typeSystem == AppConstants.TYPE_SYSTEM_LINK_JOIN) {
//                                    "${getString(R.string.type_link)} ${ConfigCache.getConfig().apiJoinGroup}${item.lastMessage.thumb.objectId}"
//                                } else {
//                                    "${getString(R.string.type_link)} ${item.lastMessage.thumb.url}"
//                                }
//                            }
//                        }
//
//                        MessageTypeChatConstants.IMAGE -> {
//                            binding.tvContent.text = getString(R.string.type_image)
//                        }
//
//                        MessageTypeChatConstants.VIDEO -> {
//                            binding.tvContent.text = getString(R.string.type_video)
//                        }
//
//                        MessageTypeChatConstants.AUDIO -> {
//                            binding.tvContent.text = getString(R.string.type_audio)
//                        }
//
//                        MessageTypeChatConstants.FILE -> {
//                            binding.tvContent.text = getString(R.string.type_file)
//                        }
//
//                        MessageTypeChatConstants.UPDATE_BACKGROUND -> {
//                            binding.tvContent.text = getString(R.string.me_update_avt)
//                        }
//
//                        MessageTypeChatConstants.REVOKE_MESSAGE -> {
//                            binding.tvContent.text = getString(R.string.type_revoke_message)
//                        }
//
//                        MessageTypeChatConstants.STICKER -> {
//                            binding.tvContent.text = getString(R.string.type_sticker)
//                        }
//
//                        MessageTypeChatConstants.MESSAGE_PINNED -> {
//                            binding.tvContent.text = String.format(
//                                    "%s: %s",
//                                    if (item.lastMessage.user.userId == UserCache.getUser().id) getString(
//                                            R.string.me_message
//                                    ) else item.lastMessage.user.fullName,
//                                    getString(R.string.message_pinned)
//                            )
//                        }
//
//                        MessageTypeChatConstants.MESSAGE_REMOVE_PINNED -> {
//                            binding.tvContent.text = String.format(
//                                    "%s: %s",
//                                    if (item.lastMessage.user.userId == UserCache.getUser().id) getString(
//                                            R.string.me_message
//                                    ) else item.lastMessage.user.fullName,
//                                    getString(R.string.message_remove_pinned)
//                            )
//                        }
//
//                        else -> binding.tvContent.text =
//                                getString(R.string.not_yet_assembled_sorry_for_the_inconvenience)
//                    }
                }
            }

            if (item.noOfNotSeen == 0) {
                binding.tvCountChat.hide()
                binding.tvContent.setTypeface(null, Typeface.NORMAL)
                binding.tvName.setTypeface(null, Typeface.NORMAL)
                binding.tvContent.isSelected = false
            } else {
                if (item.lastMessage.user.userId == UserCache.getUser().id) {
                    binding.tvCountChat.hide()
                    binding.tvContent.setTypeface(null, Typeface.NORMAL)
                    binding.tvName.setTypeface(null, Typeface.NORMAL)
                    binding.tvContent.isSelected = false
                } else {
                    if (item.noOfNotSeen > 99)
                        binding.tvCountChat.text = "99${getString(R.string.max)}"
                    else
                        binding.tvCountChat.text = item.noOfNotSeen.toString()

                    binding.tvCountChat.show()
                    binding.tvName.setTypeface(null, Typeface.BOLD)
                    binding.tvContent.setTypeface(null, Typeface.BOLD)
                    binding.tvContent.isSelected = true
                }
            }

            if (item.lastActivity.isNotEmpty() && item.lastActivity != "Invalid date") {
                binding.tvTime.text = TimeUtils.formatDatetimeChat(getContext(), item.lastActivity)
            } else {
                binding.tvTime.text = ""
            }

            if (item.isNotify != 1) {
                binding.imvNotify.show()
                binding.tvCountChat.setBackgroundResource(R.drawable.bg_no_of_not_seen_off)
            } else {
                binding.imvNotify.hide()
                binding.tvCountChat.setBackgroundResource(R.drawable.bg_no_of_not_seen_on)
            }

            if (item.isPinned == 1) {
                binding.imgPin.show()
                binding.lnItemChat.setBackgroundResource(R.drawable.ripple_animation_pin_conversation)
            } else {
                binding.imgPin.hide()
                binding.lnItemChat.setBackgroundResource(R.drawable.ripple_animation_conversation)
            }

//            binding.imgAvatar.setOnClickListener {
//                onClickGroupChat?.clickAvatar(position, item.avatar)
//            }

            binding.lnItemChat.setOnClickListener {
                onClickGroupChat?.clickGroup(item.conversationId)
            }
            binding.lnItemChat.setOnLongClickListener {
                onClickGroupChat?.onLongClickGroup(position, item)
                true
            }
        }
    }

    interface GroupChatListener {
        fun clickGroup(id: String)
//        fun clickAvatar(position: Int, avatar: MediaList)
        fun onLongClickGroup(position: Int, group: GroupChat)
    }
}