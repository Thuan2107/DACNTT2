package com.example.chatapplication.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.chatapplication.R
import com.example.chatapplication.activity.ConversationDetailActivity
import com.example.chatapplication.activity.HomeActivity
import com.example.chatapplication.adapter.GroupAdapter
import com.example.chatapplication.api.DeleteGroupApi
import com.example.chatapplication.api.GroupApi
import com.example.chatapplication.app.AppApplication
import com.example.chatapplication.app.AppApplication.Companion.socketChat
import com.example.chatapplication.app.AppFragment
import com.example.chatapplication.cache.UserCache
import com.example.chatapplication.constant.AppConstants
import com.example.chatapplication.constant.ChatConstants
import com.example.chatapplication.constant.MessageTypeChatConstants
import com.example.chatapplication.constant.SocketChatConstants
import com.example.chatapplication.databinding.FragmentGroupBinding
import com.example.chatapplication.dialog.DialogOutGroup
import com.example.chatapplication.dialog.DialogSelectionChat
import com.example.chatapplication.model.HttpData
import com.example.chatapplication.model.entity.GroupChat
import com.example.chatapplication.model.entity.GroupId
import com.example.chatapplication.model.entity.GroupSocket
import com.example.chatapplication.model.entity.JoinAndLeaveRoom
import com.example.chatapplication.model.entity.ListenJoinRoomSocket
import com.example.chatapplication.model.entity.MediaList
import com.example.chatapplication.model.entity.Message
import com.example.chatapplication.other.CustomLoadingListItemCreator
import com.example.chatapplication.utils.AppUtils
import com.example.chatapplication.utils.AppUtils.hide
import com.example.chatapplication.utils.PictureThreadUtils.runOnUiThread
import com.google.gson.Gson
import com.hjq.http.EasyHttp
import com.hjq.http.listener.HttpCallbackProxy
import com.paginate.Paginate
import io.socket.emitter.Emitter
import timber.log.Timber
class GroupFragment : AppFragment<HomeActivity>(),
        GroupAdapter.GroupChatListener {
    private lateinit var binding: FragmentGroupBinding
    private var groupDataList: ArrayList<GroupChat> = ArrayList()
    private var adapterGroupChat: GroupAdapter? = null
    private var limitGroup: Int = 20
    private var isResume: Int = 0
    private var isLoadMore: Boolean = true
    private var loading: Boolean = false
    private var paginate: Paginate? = null
    private var position: String = ""
    private val groups: GroupChat = GroupChat("HeaderFriend")    //add vị trí đầu tiên của mảng
    private var dialogSelectionChat: DialogSelectionChat.Builder? = null

    companion object {
        fun newInstance(): GroupFragment {
            return GroupFragment()
        }
    }

    override fun getLayoutView(): View {
        binding = FragmentGroupBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() {
        adapterGroupChat = GroupAdapter(requireActivity())
        adapterGroupChat?.setData(groupDataList)
        adapterGroupChat?.setGroupChatListener(this)
        AppUtils.initRecyclerViewVertical(binding.rclGroupChat, adapterGroupChat)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {

        registrySocket()
        binding.rclGroupChat.suppressLayout(true) // không cho phép cuộn rcv

        isResume = 1
        groupDataList.clear()
        adapterGroupChat?.notifyDataSetChanged()
        position = ""
        getGroupChat(position)
        paginate()
    }



    /**
     * socket
     */
    private fun registrySocket() {
        /**
         * socket tạo group mới
         */
        socketChat?.on(
                "${SocketChatConstants.LISTEN_NEW_CONVERSATION}/${UserCache.getUser().id}",
                newGroup
        )

        socketChat?.on(
                SocketChatConstants.LISTEN_NEW_CONVERSATION,
                newGroup
        )

        /**
         * Socket khi có tin nhắn đến
         */
        socketChat?.on(SocketChatConstants.ON_CHAT_TEXT, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_CHAT_IMAGE, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_CHAT_VIDEO, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_CHAT_FILE, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_CHAT_AUDIO, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_STICKER, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_REPLY, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_PINNED, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_REMOVE_PINNED, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_ADD_MEMBER, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_REMOVE_MEMBER, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_CHANGE_NAME_GROUP, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_CHANGE_AVATAR_GROUP, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_OUT_GROUP, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_UPDATE_PERMISSION_MESSAGE, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_UPDATE_OWNER_CONVERSATION, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_UPDATE_ADD_DEPUTY_CONVERSATION, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_OFF_IS_CONFIRM_CONVERSATION, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_WAITING_CONFIRM_CONVERSATION, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_UPDATE_REMOVE_DEPUTY_CONVERSATION, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_CREATE_VOTE, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_MESSAGE_VOTE, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_CHANGE_VOTE, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_ADD_OPTION_VOTE, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_BLOCK_VOTE, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_REVOKE, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_CHANGE_BACKGROUND, onLastMessage)
        socketChat?.on(SocketChatConstants.ON_SHARE_SOCKET, onLastMessage)

        //Giải tán nhóm
        socketChat?.on(SocketChatConstants.ON_DISBAND_GROUP, onDisbandGroup)

        //Listen Socket Error
        socketChat?.on(SocketChatConstants.ON_SOCKET_ERROR, onSocketError)

        //Listen Socket Join Room
        socketChat?.on("${SocketChatConstants.ON_JOIN_ROOM}/${UserCache.getUser().id}", onJoinRoom)

        //Listen Socket Change Personal
        socketChat?.on(
                "${SocketChatConstants.ON_MESSAGE_CONVERSATION_PERSONAL_SETTING}/${UserCache.getUser().id}",
                onChangePersonalSettingGroup
        )

        //Listen delete conversation history
        socketChat?.on(
                "${SocketChatConstants.ON_DELETE_CONVERSATION_HISTORY}/${UserCache.getUser().id}",
                onDeleteConversation
        )

      
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegistrySocket()
    }

    private fun unRegistrySocket() {
        socketChat?.off(
                "${SocketChatConstants.LISTEN_NEW_CONVERSATION}/${UserCache.getUser().id}",
                newGroup
        )
        socketChat?.off(SocketChatConstants.LISTEN_NEW_CONVERSATION, newGroup)
        socketChat?.off(SocketChatConstants.ON_CHAT_TEXT, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_CHAT_IMAGE, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_CHAT_VIDEO, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_CHAT_FILE, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_CHAT_AUDIO, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_REVOKE, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_PINNED, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_CHANGE_BACKGROUND, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_REMOVE_MEMBER, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_REPLY, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_STICKER, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_CHANGE_NAME_GROUP)
        socketChat?.off(SocketChatConstants.ON_CHANGE_AVATAR_GROUP, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_OUT_GROUP, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_ADD_MEMBER, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_UPDATE_PERMISSION_MESSAGE, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_UPDATE_OWNER_CONVERSATION, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_UPDATE_ADD_DEPUTY_CONVERSATION, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_OFF_IS_CONFIRM_CONVERSATION, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_WAITING_CONFIRM_CONVERSATION, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_UPDATE_REMOVE_DEPUTY_CONVERSATION, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_REMOVE_PINNED, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_CREATE_VOTE, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_MESSAGE_VOTE, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_CHANGE_VOTE, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_ADD_OPTION_VOTE, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_BLOCK_VOTE, onLastMessage)
        socketChat?.off(SocketChatConstants.ON_SHARE_SOCKET, onLastMessage)

        socketChat?.off(SocketChatConstants.ON_DISBAND_GROUP, onDisbandGroup)

        socketChat?.off(SocketChatConstants.ON_SOCKET_ERROR, onSocketError)

        socketChat?.off(
                "${SocketChatConstants.ON_JOIN_ROOM}/${UserCache.getUser().id}", onJoinRoom
        )
        socketChat?.off(
                "${SocketChatConstants.ON_MESSAGE_CONVERSATION_PERSONAL_SETTING}/${UserCache.getUser().id}",
                onChangePersonalSettingGroup
        )
        socketChat?.off(
                "${SocketChatConstants.ON_DELETE_CONVERSATION_HISTORY}/${UserCache.getUser().id}",
                onDeleteConversation
        )
    }

    private fun paginate() {
        val callback: Paginate.Callbacks = object : Paginate.Callbacks {
            override fun onLoadMore() {
                loading = true
                postDelayed({
                    if (isLoadMore && groupDataList.size > countNumberOfOAGroup() + 1) {
                        getGroupChat(groupDataList[groupDataList.size - 1].position)
                    }
                }, 200)
            }

            override fun isLoading(): Boolean {
                return loading
            }

            override fun hasLoadedAllItems(): Boolean {
                return !isLoadMore

            }

        }

        paginate = Paginate.with(binding.rclGroupChat, callback).setLoadingTriggerThreshold(0)
                .addLoadingListItem(true).setLoadingListItemCreator(CustomLoadingListItemCreator())
                .build()
    }


    override fun clickGroup(id: String) {
        val clickItem = groupDataList.find { it.conversationId == id }
        if (clickItem != null) {
            try {
                val intent = Intent(
                        getAttachActivity(), ConversationDetailActivity::class.java
                )
                val bundle = Bundle()
                bundle.putString(
                    ChatConstants.GROUP_DATA, Gson().toJson(clickItem)
                )
                intent.putExtras(bundle)
                startActivity(intent)
            } catch (e: Exception) {
                Timber.e(e.message)
            }
        }
    }

    override fun onLongClickGroup(position: Int, group: GroupChat) {
        dialogSelectionChat = DialogSelectionChat.Builder(
                requireContext(), group
        ).setListener(object : DialogSelectionChat.OnListener {
            override fun onSelectionChat(
                    idGroup: String, type: Int, dialog: Dialog
            ) {
                when (type) {
                    ChatConstants.TYPE_PINNED -> {
//                        setPinned(idGroup, false)
                        dialog.dismiss()
                    }

                    ChatConstants.TYPE_UN_PINNED -> {
//                        setPinned(idGroup, true)
                        dialog.dismiss()
                    }


                    ChatConstants.TYPE_DELETE -> {
                        dialog.dismiss()
                        DialogOutGroup.Builder(
                                requireContext(),
                                getString(R.string.common_confirm),
                                getString(R.string.policy_delete_group),
                                getString(R.string.delete_the_group)
                        ).setListener(object : DialogOutGroup.OnListener {
                            override fun onPolicyConfirm(next: Int) {
                                deleteGroup(idGroup)
                            }
                        }).show()
                    }
//
//                    ChatConstants.TYPE_OUT_GROUP -> {
//                        dialog.dismiss()
//                        DialogOutGroup.Builder(
//                                requireContext(),
//                                getString(R.string.title_out_group),
//                                getString(R.string.policy_out_group),
//                                getString(R.string.leave_the_group)
//                        ).setListener(object : DialogOutGroup.OnListener {
//                            override fun onPolicyConfirm(next: Int) {
//                                if (group.myPermission == AppConstants.OWNER) {
//                                    DialogChooseLeaderBeforeOutGroup.Builder(
//                                            requireContext(),
//                                            this@GroupFragment,
//                                            idGroup
//                                    ).setOnChangeLeaderListener(object : DialogChooseLeaderBeforeOutGroup.OnChangeLeaderListener {
//                                        override fun onChangeLeader() {
//                                            setOutConversation(idGroup)
//                                        }
//                                    }).show()
//                                } else {
//                                    setOutConversation(idGroup)
//                                }
//                            }
//                        }).show()
//                    }
                    else -> {
                        dialog.dismiss()
                    }
                }
            }

        })
        dialogSelectionChat!!.show()
    }

    override fun clickAvatar(position: Int, avatar: String) {
        AppUtils.showMediaAvatar(requireActivity(), avatar, position)

    }


    private fun addNewGroupChat(group: GroupChat) {
        if (groupDataList.isNotEmpty()) {
            if (group.isPinned == 1) {//nếu cuộc trò chuyện đó có không có ghim thì nó nằm ở dưới danh sách group OA
                if (countNumberOfOAGroup() + 1 == groupDataList.size) {
                    groupDataList.add(group)
                } else {
                    groupDataList.add(countNumberOfOAGroup() + 1, group)
                }
            } else {//nếu có ghim thì cho dưới phần tử ghim cuối cùng
                if (countNumberOfOAGroup() + countNumberOfPinnedGroup() + 1 == groupDataList.size) {
                    groupDataList.add(group)
                } else {
                    groupDataList.add(
                            countNumberOfOAGroup() + countNumberOfPinnedGroup() + 1,
                            group
                    )
                }
            }
            adapterGroupChat?.notifyItemInserted(getPositionToInsertGroup(group))
        }
    }

    private fun getPositionToInsertGroup(group: GroupChat): Int {
        return if (group.isPinned == 1) {//nếu cuộc trò chuyện được ghim thì nó nằm ở dưới danh sách group OA
            countNumberOfOAGroup() + 1
        } else {//nếu có ghim thì cho dưới phần tử ghim cuối cùng
            countNumberOfOAGroup() + countNumberOfPinnedGroup() + 1
        }
    }

    private fun countNumberOfPinnedGroup(): Int {
        return groupDataList.count { it.conversationSystemId.isEmpty() && it.conversationId.isNotEmpty() && it.isPinned == 1 }
    }

    private fun countNumberOfOAGroup(): Int {
        return groupDataList.count { it.conversationSystemId.isNotEmpty() && it.conversationId.isEmpty() }
    }

    //---------------------------------------LISTEN SOCKET----------------------------------------//

    private val onSocketError = Emitter.Listener { args: Array<Any> ->
        Thread {
            runOnUiThread {
                Timber.e("ON SOCKET ERROR%s", args[0].toString())
            }
        }.start()
    }

    /**
     * Socket xóa lịch sử cuộc trò chuyện
     */
    private val onDeleteConversation = Emitter.Listener { args: Array<Any> ->
        Thread {
            runOnUiThread {
                Timber.d(SocketChatConstants.ON_DELETE_CONVERSATION_HISTORY + args[0].toString())
                val group = Gson().fromJson(args[0].toString(), GroupId::class.java)
                val index =
                        groupDataList.indexOfFirst { it.conversationId == group.conversationId && it.conversationSystemId.isEmpty() }
                if (index != -1) {
                    groupDataList.removeAt(index)
                    adapterGroupChat?.notifyItemRemoved(index)
                }
            }
        }.start()
    }

    /**
     * Socket khi ghim/gỡ ghim, ẩn/bỏ ẩn, tắt/bật thông báo
     */
    @SuppressLint("NotifyDataSetChanged")
    private val onChangePersonalSettingGroup = Emitter.Listener { args: Array<Any> ->
        Thread {
            runOnUiThread {
                Timber.d(SocketChatConstants.ON_MESSAGE_CONVERSATION_PERSONAL_SETTING + args[0].toString())
                val group = Gson().fromJson(args[0].toString(), GroupChat::class.java)

                val index =
                        groupDataList.indexOfFirst { it.conversationId == group.conversationId && it.conversationSystemId.isEmpty() }

                when (group.event) {
                    ChatConstants.EVENT_PIN_GROUP -> {
                        if (index != -1) {
                            groupDataList.removeAt(index)
                            adapterGroupChat?.notifyItemRemoved(index)
                        }
                        addNewGroupChat(group)
                    }

                    ChatConstants.EVENT_REMOVE_PIN_GROUP, ChatConstants.EVENT_SHOW_GROUP -> {
                        groupDataList.clear()
                        groupDataList.add(groups)
                        adapterGroupChat?.notifyDataSetChanged()
                        position = ""
//                        getGroupChat(position)
                    }

                    ChatConstants.EVENT_HIDE_GROUP -> {
                        if (index != -1) {
                            groupDataList.removeAt(index)
                            adapterGroupChat?.notifyItemRemoved(index)
                            
                        }
                    }

                    ChatConstants.EVENT_OFF_NOTIFY_GROUP, ChatConstants.EVENT_ON_NOTIFY_GROUP -> {
                        groupDataList[index] = group
                        adapterGroupChat?.notifyItemChanged(index)
                    }

                    else -> {
                        //Empty
                    }
                }
            }
        }.start()
    }

    /**
     * Socket khi tài khoản đăng nhập trên thiết bị khác join 1 room nào đó
     */
    @SuppressLint("NotifyDataSetChanged")
    private val onJoinRoom = Emitter.Listener { args: Array<Any> ->
        Thread {
            runOnUiThread {
                Timber.d(SocketChatConstants.ON_JOIN_ROOM + args[0].toString())
                val joinRoomSocket =
                        Gson().fromJson(args[0].toString(), ListenJoinRoomSocket::class.java)
                val index =
                        groupDataList.indexOfFirst { it.conversationId == joinRoomSocket.data.conversationId && it.conversationSystemId.isEmpty() }
                if (index != -1) {
                    groupDataList[index].noOfNotSeen = 0
                    adapterGroupChat?.notifyItemChanged(index)
                }
            }
        }.start()
    }

    @SuppressLint("NotifyDataSetChanged")
    private val newGroup = Emitter.Listener { args: Array<Any> ->
        Thread {
            runOnUiThread {
                Timber.d("NEW_GROUP" + args[0].toString())

                val group = GroupChat()
                val groupSK = Gson().fromJson(args[0].toString(), GroupSocket::class.java)

                binding.lnEmptyGroupChat.hide()

                group.conversationId = groupSK.conversationId
                group.name = groupSK.name
                group.noOfMember = groupSK.noOfMember
                group.lastMessage.message = groupSK.lastMessage.message
                group.isNotify = groupSK.isNotify
                group.isPinned = groupSK.isPinned
                group.isConfirmNewMember = groupSK.isConfirmNewMember
                group.type = groupSK.type
                group.lastMessage.type = MessageTypeChatConstants.NEW_GROUP
                group.noOfNotSeen = 1
                group.userStatus = AppConstants.STATUS_ACTIVE
                group.lastActivity = groupSK.lastActivity
                val indexItem =
                        groupDataList.indexOfFirst { it.conversationId == group.conversationId }
                if (indexItem != -1) {//Nếu tìm thấy group này trong list group chat
                    groupDataList.removeAt(indexItem)//Xóa item cũ ra khỏi list
                    adapterGroupChat?.notifyItemRemoved(indexItem)
                }
                addNewGroupChat(group)
            }
        }.start()
    }

    @SuppressLint("NewApi", "NotifyDataSetChanged")
    private val onLastMessage = Emitter.Listener { args: Array<Any> ->
        Thread {
            runOnUiThread {
                val lastMess = Gson().fromJson(args[0].toString(), Message::class.java)

                val group = GroupChat()
                group.conversationId = lastMess.conversation.conversationId
                group.name = if (lastMess.type == MessageTypeChatConstants.UPDATE_NAME) lastMess.message else lastMess.conversation.name
                group.avatar = lastMess.conversation.avatar
                group.isOnline = 1//Tin nhắn mới thì hẳn là đang on
                group.position = lastMess.conversation.position
                group.type = lastMess.conversation.type
                group.noOfMember = lastMess.conversation.noOfMember
                group.lastActivity = lastMess.conversation.lastActivity
                group.lastMessage.messageId = lastMess.messageId
                group.lastMessage.message = lastMess.message
                group.lastMessage.type = lastMess.type
                group.lastMessage.user = lastMess.user
                group.lastMessage.userTarget = lastMess.userTarget
                group.userStatus = AppConstants.STATUS_ACTIVE
                val indexItem =
                        groupDataList.indexOfFirst { it.conversationId == lastMess.conversation.conversationId }

                if (indexItem != -1) {//Nếu tìm thấy group này trong list group chat
                    val foundItem = groupDataList[indexItem]
                    groupDataList.removeAt(indexItem)//Xóa item cũ ra khỏi list
                    adapterGroupChat?.notifyDataSetChanged()
                    if (!(lastMess.type == MessageTypeChatConstants.REMOVE_USER && lastMess.userTarget.any { it.userId == UserCache.getUser().id })) {
                        group.noOfNotSeen = foundItem.noOfNotSeen + 1
                        group.isNotify = foundItem.isNotify
                        group.isPinned = foundItem.isPinned
                        group.myPermission = foundItem.myPermission
                        group.isConfirmNewMember = foundItem.isConfirmNewMember
                        group.isHidden = foundItem.isHidden
                        addNewGroupChat(group)
                    }
                } else {//Không có thì call api chi tiết để lấy
//                    getDetailGroup(group)
                }
            }
        }.start()
    }

    @SuppressLint("NotifyDataSetChanged")

    private val onDisbandGroup = Emitter.Listener { args: Array<Any> ->
        Thread {
            runOnUiThread {
                Timber.d("Socket DisbandGroup %s", args[0].toString())
                val conversation =
                        Gson().fromJson(args[0].toString(), JoinAndLeaveRoom::class.java)
                val index =
                        groupDataList.indexOfFirst { it.conversationId == conversation.conversationId }
                if (index != -1) {
                    groupDataList.removeAt(index)
                    adapterGroupChat?.notifyItemRemoved(index)
                }
            }
        }.start()
    }

    //--------------------------------------------------------------------------------------------//

    //--------------------------------------------API---------------------------------------------//

    /**
     * Out group
     */
//    private fun setOutConversation(idGroup: String) {
//        EasyHttp.post(this).api(OutGroupApi.params(idGroup))
//                .request(object : HttpCallbackProxy<HttpData<Any>>(this) {
//                    @SuppressLint("NotifyDataSetChanged")
//                    override fun onHttpSuccess(data: HttpData<Any>) {
//                        if (data.isRequestSucceed()) {
//                            val index = groupDataList.indexOfFirst { it.conversationId == idGroup }
//                            if (index != -1) {
//                                groupDataList.removeAt(index)
//                                adapterGroupChat?.notifyItemRemoved(index)
//                            }
//                        } else {
//                            toast(data.getData())
//                        }
//                    }
//
//                    override fun onHttpEnd(call: Call?) {
//                        //empty
//                    }
//
//                    override fun onHttpStart(call: Call?) {
//                        //empty
//                    }
//
//                })
//    }

    /**
     * api ghim cuộc trò chuyện
     */
//    private fun setPinned(idGroup: String, typeAll: Boolean) {
//        EasyHttp.post(this).api(PinnedGroupApi.params(idGroup))
//                .request(object : HttpCallbackProxy<HttpData<Any>>(this) {
//                    @SuppressLint("NotifyDataSetChanged")
//                    override fun onHttpSuccess(data: HttpData<Any>) {
//                        if (data.isRequestSucceed()) {
//                            if (typeAll) {
//                                toast(getString(R.string.unpin_1_chat))
//                            } else {
//                                toast(getString(R.string.pin_1_chat))
//                            }
//                        } else {
//                            if (data.isRequestError()) {
//                                toast(data.getMessage())
//                            }
//                            Timber.e(
//                                    "${
//                                        AppApplication.applicationContext()
//                                                .getString(vn.techres.line.R.string.error_message)
//                                    } ${data.getMessage()}"
//                            )
//                        }
//                    }
//
//                    override fun onHttpFail(e: java.lang.Exception?) {
//
//                        Timber.e(
//                                "${
//                                    AppApplication.applicationContext()
//                                            .getString(vn.techres.line.R.string.error_message)
//                                } ${e?.message}"
//                        )
//                    }
//
//                    override fun onHttpStart(call: Call?) {
//                        //empty
//                    }
//
//                    override fun onHttpEnd(call: Call?) {
//                        //empty
//                    }
//
//                })
//    }



    /**
     * api xoá lịch sử trò chuyện
     */
    private fun deleteGroup(idGroup: String) {
        EasyHttp.post(this).api(DeleteGroupApi.params(idGroup))
                .request(object : HttpCallbackProxy<HttpData<Any>>(this) {
                    @SuppressLint("NotifyDataSetChanged", "NewApi")
                    override fun onHttpSuccess(data: HttpData<Any>) {
                        if (data.isRequestSucceed()) {
                            val index = groupDataList.indexOfFirst { it.conversationId == idGroup }
                            if (index != -1) {
                                groupDataList.removeAt(index)
                                adapterGroupChat?.notifyDataSetChanged()
                            }
                        } else {
                            Timber.e(
                                    "${
                                        AppApplication.applicationContext()
                                                .getString(R.string.error_message)
                                    } ${data.getMessage()}"
                            )
                        }
                    }

                    override fun onHttpFail(throwable: Throwable?) {

                        Timber.e(
                                "${
                                    AppApplication.applicationContext()
                                            .getString(R.string.error_message)
                                } ${throwable}"
                        )
                    }
                })
    }

    /**
     * api tạo cuộc trò chuyện
     */
//    private fun createGroup(id: Int) {
//        EasyHttp.post(this).api(CreateConversationApi.params(id.toLong()))
//                .request(object : HttpCallbackProxy<HttpData<DataCreateGroup>>(this) {
//                    @SuppressLint("IntentWithNullActionLaunch")
//                    override fun onHttpSuccess(data: HttpData<DataCreateGroup>) {
//                        if (data.isRequestSucceed()) {
//                            try {
//                                val friend = friendDataList.find { it.userId == id }
//                                if (friend != null) {
//                                    val bundle = Bundle()
//                                    val intent = Intent(
//                                            getAttachActivity(),
//                                            Class.forName(ModuleClassConstants.CHAT_ACTIVITY)
//                                    )
//                                    val group = GroupChat()
//                                    with(group) {
//                                        this.conversationId = data.getData()!!.conversationId
//                                        type = AppConstants.TYPE_PRIVATE
//                                        name = friend.fullName
//                                        avatar.original.url = friend.avatar
//                                        isOnline = friend.isOnline
//                                        userBlockType = AppConstants.TYPE_NONE_BLOCK
//                                        userStatus = AppConstants.STATUS_ACTIVE
//                                    }
//                                    bundle.putString(AppConstants.GROUP_DATA, Gson().toJson(group))
//                                    intent.putExtras(bundle)
//                                    startActivity(intent)
//                                }
//                            } catch (e: Exception) {
//                                Timber.e(e.message)
//                            }
//                        } else {
//                            if (data.isRequestError()) {
//                                toast(data.getMessage())
//                            }
//                            Timber.e(
//                                    "${
//                                        AppApplication.applicationContext()
//                                                .getString(vn.techres.line.R.string.error_message)
//                                    } ${data.getMessage()}"
//                            )
//                        }
//                    }
//
//                    override fun onHttpStart(call: Call?) {
//                        //empty
//                    }
//                })
//    }



    /**
     * Gọi api lấy danh sách cuộc trò chuyện
     */
    @SuppressLint("HardwareIds")
    private fun getGroupChat(position: String) {
        EasyHttp.get(this).api(GroupApi.params(position, "", limitGroup))
                .request(/* listener = */ object : HttpCallbackProxy<HttpData<List<GroupChat>>>(this) {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onHttpSuccess(data: HttpData<List<GroupChat>>) {
                        if (data.isRequestSucceed()) {
                            loading = false
                            if (data.getData() == null) {
                                isLoadMore = false
                            } else {
                                isLoadMore = data.getData()!!.size >= limitGroup
                                paginate?.setHasMoreDataToLoad(isLoadMore)
                                val currentSize = groupDataList.size
                                groupDataList.addAll(data.getData()!!)
                                adapterGroupChat?.notifyItemRangeInserted(
                                        currentSize,
                                        data.getData()!!.size
                                )
                            }
                            isResume = 0
                            binding.rclGroupChat.suppressLayout(false)  // cho phép cuộn rcv
                        } else {
                            loading = false

                            Timber.e(
                                    "${
                                        AppApplication.applicationContext()
                                                .getString(R.string.error_message)
                                    } ${data.getMessage()}"
                            )
                            isResume = 0
                            binding.rclGroupChat.suppressLayout(false)  // cho phép cuộn rcv
                        }
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onHttpFail(throwable: Throwable?) {
                        loading = false
                        binding.rclGroupChat.suppressLayout(false)  // cho phép cuộn rcv
                        adapterGroupChat?.notifyDataSetChanged()
                    }

                    override fun onHttpStart(call: okhttp3.Call?) {
                        super.onHttpStart(call)
                    }

                    override fun onHttpEnd(call: okhttp3.Call?) {
                        super.onHttpEnd(call)
                    }

                })
    }





    //Api lấy detail group
//    private fun getDetailGroup(group: GroupChat) {
//        EasyHttp.get(this).api(DetailGroupApi.params(group.conversationId))
//                .request(object : HttpCallbackProxy<HttpData<GroupChat>>(this) {
//                    override fun onHttpEnd(call: Call?) {
//                        //empty
//                    }
//
//                    override fun onHttpStart(call: Call?) {
//                        //empty
//                    }
//
//                    @SuppressLint("IntentWithNullActionLaunch", "NotifyDataSetChanged")
//                    override fun onHttpSuccess(data: HttpData<GroupChat>) {
//                        if (data.isRequestSucceed()) {
//                            data.getData()?.let {
//                                val indexItem =
//                                        groupDataList.indexOfFirst { gr -> gr.conversationId == group.conversationId }
//                                if (indexItem != -1) {//Nếu tìm thấy group này trong list group chat
//                                    groupDataList.removeAt(indexItem)//Xóa item cũ ra khỏi list
//                                    adapterGroupChat?.notifyDataSetChanged()
//                                }
//                                if (!(group.lastMessage.type == MessageTypeChatConstants.REMOVE_USER &&
//                                                group.lastMessage.userTarget.any { lastMess -> lastMess.userId == UserCache.getUser().id })
//                                ) {
//                                    group.noOfNotSeen = it.noOfNotSeen + 1
//                                    group.isNotify = it.isNotify
//                                    group.isPinned = it.isPinned
//                                    group.myPermission = it.myPermission
//                                    group.isConfirmNewMember = it.isConfirmNewMember
//                                    group.isHidden = it.isHidden
//                                    addNewGroupChat(group)
//                                }
//                            }
//                        } else {
//                            if (data.isRequestError()) {
//                                toast(data.getMessage())
//                            }
//                            Timber.e(
//                                    "${
//                                        AppApplication.applicationContext()
//                                                .getString(vn.techres.line.R.string.error_message)
//                                    } ${data.getMessage()}"
//                            )
//                        }
//                    }
//
//                    override fun onHttpFail(e: java.lang.Exception?) {
//
//                        Timber.e(
//                                "${
//                                    AppApplication.applicationContext()
//                                            .getString(vn.techres.line.R.string.error_message)
//                                } ${e?.message}"
//                        )
//                    }
//                })
//    }

    //--------------------------------------------------------------------------------------------//

    //-----------------------------------------EVENT BUS------------------------------------------//

//    @SuppressLint("NotifyDataSetChanged")
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onOutGroup(event: OutGroupEventBus) {
//        val index = groupDataList.indexOfFirst { it.conversationId == event.groupId }
//        if (index != -1) {
//            groupDataList.removeAt(index)
//            adapterGroupChat?.notifyDataSetChanged()
//            if (groupDataList.size < 2) {
//                binding.lnEmptyGroupChat.show()
//            } else {
//                binding.lnEmptyGroupChat.hide()
//            }
//        }
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onUpdateNoOfNotSeen(event: UpdateNoOfNotSeenEventBus) {
//        val position = groupDataList.indexOfFirst { it.conversationId == event.groupId }
//        if (position != -1) {
//            groupDataList[position].noOfNotSeen = 0
//            adapterGroupChat?.notifyItemChanged(position)
//        }
//        EventBus.getDefault().post(EventBusUpdateNotifyCount())
//    }
//
//    //Ẩn cuộc trò chuyện
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onHideConversation(event: EventBusHideConversation) {
//        val position = groupDataList.indexOfFirst { it.conversationId == event.idGroup }
//        if (position != -1) {
//            groupDataList.removeAt(position)
//            adapterGroupChat?.notifyItemRemoved(position)
//            
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onScrollTopChat(event: EventBusClickScrollChat) {
//        binding.rclGroupChat.scrollToPosition(0)
//    }
//
//    @SuppressLint("NotifyDataSetChanged", "NewApi")
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onLastMessageJoinRoom(event: EventBusLastMessage) {
//        val indexItem =
//                groupDataList.indexOfFirst { it.conversationId == event.groupChat.conversationId }
//        if (indexItem != -1) {
//            groupDataList.removeAt(indexItem)//Xóa item cũ ra khỏi list
//            adapterGroupChat?.notifyDataSetChanged()
//        }
//
//        if (!(event.groupChat.type == MessageTypeChatConstants.REMOVE_USER && event.groupChat.lastMessage.userTarget.any { it.userId == UserCache.getUser().id })) {
//            addNewGroupChat(event.groupChat)
//        }
//        if (groupDataList.size < 2) {
//            binding.lnEmptyGroupChat.show()
//        } else {
//            binding.lnEmptyGroupChat.hide()
//        }
//    }
//
//    //Bật/tắt thông báo cuộc trò chuyện
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onUpdateGroupNotify(event: UpdateGroupNotify) {
//        val indexItem =
//                groupDataList.indexOfFirst { it.conversationId == event.idGroup }
//        if (indexItem != -1) {
//            groupDataList[indexItem].isNotify = if (event.isNotify) 1 else 0
//            adapterGroupChat?.notifyItemChanged(indexItem)
//        }
//    }
//
//    //Xóa lịch sử cuộc trò chuyện
//    @SuppressLint("NotifyDataSetChanged")
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onDeleteConversation(event: DeleteEventBus) {
//        val index = groupDataList.indexOfFirst { it.conversationId == event.idGroup }
//        if (index != -1) {
//            groupDataList.removeAt(index)
//            adapterGroupChat?.notifyDataSetChanged()
//            
//        }
//    }

   

    //--------------------------------------------------------------------------------------------//
}