//package com.example.chatapplication.activity
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.os.Build
//import android.os.Bundle
//import android.provider.ContactsContract
//import android.view.View
//import androidx.appcompat.widget.SearchView
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.RecyclerView
//import com.google.gson.Gson
//import com.hjq.http.EasyHttp
//import com.hjq.http.listener.HttpCallbackProxy
//import okhttp3.Call
//import org.greenrobot.eventbus.EventBus
//import timber.log.Timber
//import vn.techres.base.BaseAdapter
//import vn.techres.line.app.AppActivity
//import vn.techres.line.app.AppApplication
//import vn.techres.line.cache.ContactCache
//import vn.techres.line.cache.UserCache
//import vn.techres.line.constants.AccountConstants
//import vn.techres.line.constants.AppConstants
//import vn.techres.line.constants.ModuleClassConstants
//import vn.techres.line.contact.R
//import vn.techres.line.contact.api.SyncApi
//import vn.techres.line.contact.constants.ContactConstants
//import vn.techres.line.contact.databinding.ActivityPhoneBookBinding
//import vn.techres.line.contact.ui.adapter.ContactAdapter
//import vn.techres.line.http.api.AddFriendApi
//import vn.techres.line.http.api.BlockUserApi
//import vn.techres.line.http.api.CreateConversationApi
//import vn.techres.line.http.api.NotAcceptFriendApi
//import vn.techres.line.http.api.RemoveRequestFriendApi
//import vn.techres.line.http.api.RequestFriendApi
//import vn.techres.line.http.api.UnFriendApi
//import vn.techres.line.http.model.HttpData
//import vn.techres.line.model.entity.ContactDevice
//import vn.techres.line.model.entity.DataCreateGroup
//import vn.techres.line.model.entity.Friend
//import vn.techres.line.model.entity.FriendContactFromData
//import vn.techres.line.model.entity.GroupChat
//import vn.techres.line.model.entity.MediaList
//import vn.techres.line.model.entity.Original
//import vn.techres.line.model.eventbus.BlockUserEventbus
//import vn.techres.line.model.eventbus.CurrentFragmentEventBus
//import vn.techres.line.model.eventbus.FragmentEventBus
//import vn.techres.line.ui.activity.HomeActivity
//import vn.techres.line.ui.dialog.DialogFeedBack
//import vn.techres.line.ui.dialog.MoreDialog
//import vn.techres.line.utils.AppUtils
//import vn.techres.line.utils.AppUtils.hide
//import vn.techres.line.utils.AppUtils.show
//import vn.techres.line.utils.TimeUtils
//import java.text.Normalizer
//import java.util.Locale
//import java.util.regex.Pattern
//
///**
// * @Author: NGUYEN THANH BINH
// * @Date: 12/2/22
// */
//class PhoneBookActivity : AppActivity(), BaseAdapter.OnItemClickListener,
//    ContactAdapter.MyFriendInterface,
//    ContactAdapter.AddFriendInterface {
//    private lateinit var binding: ActivityPhoneBookBinding
//    private var contactList: ArrayList<ContactDevice> = ArrayList()
//    private var dataList: ArrayList<Friend> = ArrayList()
//    private var db: ArrayList<Friend> = ArrayList()
//    private var adapterMyContact: ContactAdapter? = null
//    private var dialogMore: MoreDialog.Builder? = null
//    private var positionItem: Int = -1
//    private var keySearch: String = ""
//    private var btnAll: Int = 1
//    private var btnNew: Int = 0
//    private var dialogFeedBack: DialogFeedBack.Builder? = null
//
//    override fun getLayoutView(): View {
//        binding = ActivityPhoneBookBinding.inflate(layoutInflater)
//        return binding.root
//    }
//
//    override fun onResume() {
//        super.onResume()
//        checkContact()
//    }
//
//
//    override fun initView() {
//
//        setEnable(false)
//
//        if (UserCache.getUser().phoneBookUpdateTime != "") {
//            binding.tvUpdateAt.show()
//            binding.tvUpdateAt.text = String.format(
//                "%s %s",
//                getString(R.string.hint_phone_book),
//                UserCache.getUser().phoneBookUpdateTime
//            )
//        } else {
//            binding.tvUpdateAt.hide()
//        }
//
//        adapterMyContact = ContactAdapter(this)
//        adapterMyContact?.setMyFriendInterFace(this)
//        adapterMyContact?.setOnItemClickListener(this)
//        adapterMyContact?.setOnClickAddFriend(this)
//        adapterMyContact?.setOnClickMore(this)
//        adapterMyContact?.setData(dataList)
//
//        AppUtils.initRecyclerViewVertical(
//            binding.itemRcv.recyclerViewMyFriend, adapterMyContact
//        )
//        setOnClickListener(binding.itemRcv.tvAll, binding.itemRcv.tvNew)
//
//        binding.itemRcv.lnEmpty.hide()
//        binding.itemRcv.sflContact.show()
//        binding.itemRcv.recyclerViewMyFriend.hide()
//        binding.itemRcv.sflContact.startShimmer()
//    }
//
//    @SuppressLint("Range", "NotifyDataSetChanged")
//    override fun initData() {
//        binding.itemEmpty.btnSync.setOnClickListener {
//            dataList.clear()
//            if (applicationContext.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(
//                    this, arrayOf(Manifest.permission.READ_CONTACTS), ContactConstants.REQUEST_CODE
//                )
//            }
//        }
//
//        binding.btnUpdate.setOnClickListener {
//            setEnable(false)
//            showDialog()
//            requestPermissions(
//                arrayOf(Manifest.permission.READ_CONTACTS),
//                ContactConstants.REQUEST_CODE
//            )
//            binding.tvUpdateAt.show()
//
//        }
//
//        binding.lnUpdate.setOnClickListener {
//            setEnable(false)
//
//            showDialog()
//            requestPermissions(
//                arrayOf(Manifest.permission.READ_CONTACTS),
//                ContactConstants.REQUEST_CODE
//            )
//            binding.tvUpdateAt.show()
//
//
//        }
//
//        binding.svSaveBranch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(it: String?): Boolean {
//                keySearch = it.toString()
//
//                if (btnNew == 1)
//                    search(keySearch, filterNotFriend())
//                else
//                    search(keySearch, filterAll())
//                return true
//            }
//        })
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    override fun onClick(view: View) {
//        when (view) {
//            binding.itemRcv.tvAll -> {
//                if (btnAll == 0) {
//                    setEnable(false)
//                    btnAll = 1
//                    btnNew = 0
//                    selected(ContactConstants.ON_LISTENER, ContactConstants.OFF_LISTENER)
//                    keySearch = ""
//                    binding.svSaveBranch.setQuery("", false)
//                    binding.itemRcv.lnEmpty.hide()
//                    binding.itemRcv.sflContact.show()
//                    binding.itemRcv.recyclerViewMyFriend.hide()
//                    binding.itemRcv.sflContact.startShimmer()
//
//
//                    dataList.clear()
//                    setData(filterAll())
//                }
//            }
//
//            binding.itemRcv.tvNew -> {
//                if (btnNew == 0) {
//                    setEnable(false)
//                    selected(ContactConstants.OFF_LISTENER, ContactConstants.ON_LISTENER)
//                    btnNew = 1
//                    btnAll = 0
//                    keySearch = ""
//
//                    binding.svSaveBranch.setQuery("", false)
//
//
//
//                    binding.itemRcv.lnEmpty.hide()
//                    binding.itemRcv.sflContact.show()
//                    binding.itemRcv.recyclerViewMyFriend.hide()
//                    binding.itemRcv.sflContact.startShimmer()
//
//                    dataList.clear()
//                    setData(filterNotFriend())
//                }
//            }
//        }
//    }
//
//    /**
//     * Gọi api lấy danh sách danh bạ
//     */
//    @SuppressLint("HardwareIds")
//    private fun getContact(contact: ArrayList<ContactDevice>) {
//        binding.itemRcv.lnEmpty.hide()
//        binding.itemRcv.sflContact.show()
//        binding.itemRcv.recyclerViewMyFriend.hide()
//        binding.itemRcv.sflContact.startShimmer()
//
//        EasyHttp.post(this).api(SyncApi.params(contact))
//            .request(object : HttpCallbackProxy<HttpData<FriendContactFromData>>(this) {
//                @SuppressLint("NotifyDataSetChanged")
//                override fun onHttpStart(call: Call?) {
//                    //empty
//                }
//
//                override fun onHttpEnd(call: Call?) {
//                    //empty
//                }
//
//                @SuppressLint("NotifyDataSetChanged")
//                override fun onHttpSuccess(data: HttpData<FriendContactFromData>) {
//                    if (data.isRequestSucceed()) {
//                        dataList.clear()
//                        db.clear()
//
//                        binding.itemRcv.sflContact.hide()
//                        binding.itemRcv.recyclerViewMyFriend.show()
//                        binding.itemRcv.sflContact.stopShimmer()
//
//
//                        db.addAll(data.getData()!!.list)
//                        ContactCache.saveDb(db)
//
//
//
//                        binding.itemEmpty.lnEmpty.hide()
//                        binding.lnContact.show()
//
//
//
//                        if (btnNew == 1)
//                            setData(filterNotFriend())
//                        else
//                            setData(filterAll())
//                    }
//                }
//            })
//    }
//
//    @SuppressLint("Range")
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == ContactConstants.REQUEST_CODE) {
//            if (grantResults.isNotEmpty()) {
//                if (grantResults.first() == PackageManager.PERMISSION_GRANTED) {
//
//                    val contacts = contentResolver.query(
//                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                        null,
//                        null,
//                        null,
//                        null
//                    )
//
//                    contactList.clear()
//
//                    while (contacts!!.moveToNext()) {
//                        val ojb = ContactDevice()
//                        with(ojb) {
//                            name =
//                                contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
//                            phone = formatPhone(
//                                contacts.getString(
//                                    contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
//                                )
//                            )
//                        }
//
//                        contactList.add(ojb)
//
//
//                    }
//
//                    contacts.close()
//                    getContact(contactList)
//                    val user = UserCache.getUser()
//                    user.phoneBookUpdateTime =
//                        TimeUtils.getCurrentTimeFormat("HH:mm dd/MM/yyyy", isTimeZoneUTC = false)
//                    binding.tvUpdateAt.text = String.format(
//                        "%s %s",
//                        getString(R.string.hint_phone_book),
//                        user.phoneBookUpdateTime
//                    )
//                    UserCache.saveUser(user)
//                } else toast(getString(R.string.permission_contact))
//            }
//        }
//    }
//
//    /**
//     * kiểm tra đã bật quyền danh bạ chưa?
//     */
//    @SuppressLint("Range", "NotifyDataSetChanged", "ObsoleteSdkInt")
//    private fun checkContact() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.READ_CONTACTS
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            binding.lnContact.show()
//            binding.itemEmpty.lnEmpty.hide()
//
//            val contacts = contentResolver.query(
//                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                null,
//                null,
//                null,
//                null
//            )
//            contactList.clear()
//            while (contacts!!.moveToNext()) {
//                val ojb = ContactDevice()
//                ojb.name =
//                    contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
//                ojb.phone =
//                    formatPhone(contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))
//                contactList.add(ojb)
//            }
//
//            contacts.close()
//
//
//            getContact(contactList)
//
//
//        }
//    }
//
//    /**
//     * Gọi api thu hồi lời mời kết bạn
//     */
//    @SuppressLint("HardwareIds")
//    private fun removeRequestFriend(id: Int, position: Int) {
//        EasyHttp.post(this).api(RemoveRequestFriendApi.params(id))
//            .request(object : HttpCallbackProxy<HttpData<Any>>(this) {
//                @SuppressLint("NotifyDataSetChanged")
//                override fun onHttpSuccess(data: HttpData<Any>) {
//                    if (!data.isRequestSucceed()) {
//                        if (data.isRequestError()) {
//                            toast(data.getMessage())
//                        }
//                        Timber.e(
//                            "${
//                                AppApplication.applicationContext()
//                                    .getString(vn.techres.line.R.string.error_message)
//                            } ${data.getMessage()}"
//                        )
//                        hideDialog()
//                    } else {
//                        dataList[position].contactType = AppConstants.NOT_FRIEND
//                        adapterMyContact!!.notifyDataSetChanged()
//
//                    }
//                }
//
//                override fun onHttpFail(e: java.lang.Exception?) {
//
//                    Timber.e(
//                        "${
//                            AppApplication.applicationContext()
//                                .getString(vn.techres.line.R.string.error_message)
//                        } ${e?.message}"
//                    )
//                    hideDialog()
//                }
//
//                override fun onHttpEnd(call: Call?) {
//                    //empty
//                }
//
//                override fun onHttpStart(call: Call?) {
//                    //empty
//                }
//            })
//    }
//
//    /**
//     * gọi api block :vvvvvv
//     */
//    private fun blockUser(idUser: Int) {
//        EasyHttp.post(this).api(BlockUserApi.params(idUser))
//            .request(object : HttpCallbackProxy<HttpData<Any>>(this) {
//                override fun onHttpSuccess(data: HttpData<Any>) {
//                    if (data.isRequestSucceed()) {
//                        EventBus.getDefault().post(BlockUserEventbus(idUser))
//                    }
//                }
//
//                override fun onHttpEnd(call: Call?) {
//                    //empty
//                }
//
//                override fun onHttpStart(call: Call?) {
//                    //empty
//                }
//            })
//    }
//
//    /**
//     * gọi api huỷ kết bạn :vvvvvv
//     */
//    private fun unFriend(id: Int) {
//        EasyHttp.post(this).api(UnFriendApi.params(id))
//            .request(object : HttpCallbackProxy<HttpData<Any>>(this) {
//                override fun onHttpSuccess(data: HttpData<Any>) {
//                    if (!data.isRequestSucceed()) {
//                        if (data.isRequestError()) {
//                            toast(data.getMessage())
//                        }
//                        Timber.e(
//                            "${
//                                AppApplication.applicationContext()
//                                    .getString(vn.techres.line.R.string.error_message)
//                            } ${data.getMessage()}"
//                        )
//                        hideDialog()
//                    } else {
//                        //empty
//                    }
//                }
//
//                override fun onHttpFail(e: java.lang.Exception?) {
//
//                    Timber.e(
//                        "${
//                            AppApplication.applicationContext()
//                                .getString(vn.techres.line.R.string.error_message)
//                        } ${e?.message}"
//                    )
//                    hideDialog()
//                }
//
//                override fun onHttpStart(call: Call?) {
//                    //empty
//                }
//
//                override fun onHttpEnd(call: Call?) {
//                    //empty
//                }
//            })
//    }
//
//    /**
//     * gọi api kết bạn :vvvvvv
//     */
//    private fun callAddFriend(id: Int) {
//        EasyHttp.post(this).api(RequestFriendApi.params(id))
//            .request(object : HttpCallbackProxy<HttpData<Any>>(this) {
//                @SuppressLint("NotifyDataSetChanged")
//                override fun onHttpSuccess(data: HttpData<Any>) {
//                    if (!data.isRequestSucceed()) {
//                        if (data.isRequestError()) {
//                            toast(data.getMessage())
//                        }
//                        Timber.e(
//                            "${
//                                AppApplication.applicationContext()
//                                    .getString(vn.techres.line.R.string.error_message)
//                            } ${data.getMessage()}"
//                        )
//                        hideDialog()
//                    }
//                }
//
//                override fun onHttpFail(e: java.lang.Exception?) {
//
//                    Timber.e(
//                        "${
//                            AppApplication.applicationContext()
//                                .getString(vn.techres.line.R.string.error_message)
//                        } ${e?.message}"
//                    )
//                    hideDialog()
//                }
//
//                override fun onHttpEnd(call: Call?) {
//                    //empty
//                }
//
//                override fun onHttpStart(call: Call?) {
//                    //empty
//                }
//            })
//    }
//
//    /**
//     * Gọi api đồng ý kết bạn
//     */
//    @SuppressLint("HardwareIds")
//    private fun addFriend(id: Int) {
//        EasyHttp.post(this).api(AddFriendApi.params(id))
//            .request(object : HttpCallbackProxy<HttpData<Any>>(this) {
//                override fun onHttpEnd(call: Call?) {
//                    //empty
//                }
//
//                override fun onHttpStart(call: Call?) {
//                    //empty
//                }
//
//                override fun onHttpFail(e: java.lang.Exception?) {
//
//                    Timber.e(
//                        "${
//                            AppApplication.applicationContext()
//                                .getString(vn.techres.line.R.string.error_message)
//                        } ${e?.message}"
//                    )
//                    hideDialog()
//                }
//
//                override fun onHttpSuccess(data: HttpData<Any>) {
//                    if (!data.isRequestSucceed()) {
//                        if (data.isRequestError()) {
//                            toast(data.getMessage())
//                        }
//                        Timber.e(
//                            "${
//                                AppApplication.applicationContext()
//                                    .getString(vn.techres.line.R.string.error_message)
//                            } ${data.getMessage()}"
//                        )
//                        hideDialog()
//                    }
//                }
//            })
//    }
//
//    override fun clickAddFriend(id: Int, position: Int) {
//        callAddFriend(id)
//        dataList[position].contactType = AppConstants.WAITING_RESPONSE
//
//    }
//
//    override fun clickRecall(id: Int, position: Int) {
//        removeRequestFriend(id, position)
//    }
//
//    override fun clickFeedBack(id: Int, position: Int) {
//        positionItem = position
//        dialogFeedBack(id)
//    }
//
//    override fun clickMore(item: Friend, position: Int) {
//        dialogMore = MoreDialog.Builder(this, item, 1).setListener(object : MoreDialog.OnListener {
//            override fun onFollow(idUser: Int) {
//                //empty
//            }
//
//            override fun onUnFollow(idUser: Int) {
//                //empty
//            }
//
//            override fun onBlockUser(idUser: Int) {
//                blockUser(idUser)
//            }
//
//            @SuppressLint("NotifyDataSetChanged")
//            override fun onUnFriendUser(idUser: Int) {
//                unFriend(idUser)
//                dataList[position].contactType = AppConstants.NOT_FRIEND
//                adapterMyContact!!.notifyDataSetChanged()
//            }
//
//            override fun onContentReports(type: Int, contentReport: String) {
//                //empty
//            }
//
//
//        })
//        dialogMore!!.show()
//    }
//
//    override fun clickWall(position: Int) {
//        if (dataList[position].userId == UserCache.getUser().id.toLong()) {
//            EventBus.getDefault().post(CurrentFragmentEventBus(AppConstants.FRAGMENT_PROFILE))
//            EventBus.getDefault().post(FragmentEventBus(AppConstants.FRAGMENT_PROFILE))
//            startActivity(HomeActivity::class.java)
//        } else {
//            try {
//                val intent = Intent(
//                    this, Class.forName(ModuleClassConstants.INFO_CUSTOMER)
//                )
//                val bundle = Bundle()
//                bundle.putInt(
//                    AccountConstants.ID, dataList[position].userId.toInt()
//                )
//                keySearch = ""
//                intent.putExtras(bundle)
//                startActivity(intent)
//            } catch (e: Exception) {
//                Timber.e(e.message)
//            }
//        }
//    }
//
//    /**
//     * api tạo cuộc trò chuyện
//     */
//    private fun createGroup(position: Int) {
//        EasyHttp.post(this)
//            .api(CreateConversationApi.params(dataList[position].userId))
//            .request(object : HttpCallbackProxy<HttpData<DataCreateGroup>>(this) {
//                override fun onHttpSuccess(data: HttpData<DataCreateGroup>) {
//                    if (data.isRequestSucceed()) {
//                        try {
//                            val bundle = Bundle()
//                            val intent = Intent(
//                                this@PhoneBookActivity,
//                                Class.forName(ModuleClassConstants.CHAT_ACTIVITY)
//                            )
//                            val group = GroupChat()
//                            with(group) {
//                                conversationId = data.getData()!!.conversationId
//                                type = AppConstants.TYPE_PRIVATE
//                                name = dataList[position].fullName
//                                avatar.original.url = dataList[position].avatar
//                            }
//                            bundle.putString(AppConstants.GROUP_DATA, Gson().toJson(group))
//                            intent.putExtras(bundle)
//                            startActivity(intent)
//                            finish()
//                        } catch (e: Exception) {
//                            Timber.e(e.message)
//                        }
//                    }
//
//                }
//
//                override fun onHttpEnd(call: Call?) {
//                    //empty
//                }
//
//                override fun onHttpStart(call: Call?) {
//                    //empty
//                }
//            })
//    }
//
//    override fun clickMessage(position: Int) {
//        createGroup(position)
//    }
//
//    override fun clickAvatar(position: Int) {
//        val original = Original()
//        original.url = dataList[position].avatar
//        original.name = dataList[position].fullName
//        AppUtils.showMediaAvatar(this, MediaList(original, 0), 0)
//    }
//
//    override fun onItemClick(recyclerView: RecyclerView?, itemView: View?, position: Int) {
//        toast("aaa")
//
//
//    }
//
//    /**
//     * bỏ khoảng trắng trong số điện thoại
//     */
//    private fun formatPhone(text: String): String {
//        var strPhone = ""
//        val size = text.split(" ")
//        for (i in 0 until text.split(" ").size)
//            strPhone += size[i]
//
//        return strPhone
//    }
//
//
//    /**
//     * dialog phản hồi
//     */
//    private fun dialogFeedBack(id: Int) {
//        dialogFeedBack =
//            DialogFeedBack.Builder(this).setListener(object : DialogFeedBack.ClickAddFriend {
//                @SuppressLint("NotifyDataSetChanged")
//                override fun onClickAddFriend(type: Int) {
//                    if (type == 1) {
//                        addFriend(id)
//                        dataList[positionItem].contactType = AppConstants.FRIEND
//                        adapterMyContact!!.notifyDataSetChanged()
//                    } else {
//                        EasyHttp.post(this@PhoneBookActivity).api(NotAcceptFriendApi.params(id))
//                            .request(object : HttpCallbackProxy<HttpData<Any>>(this@PhoneBookActivity) {
//                                @SuppressLint("NotifyDataSetChanged")
//                                override fun onHttpSuccess(data: HttpData<Any>) {
//                                    if (data.isRequestSucceed()) {
//                                        dataList[positionItem].contactType = AppConstants.NOT_FRIEND
//                                        adapterMyContact!!.notifyDataSetChanged()
//                                    } else {
//                                        if (data.isRequestError()) {
//                                            toast(data.getMessage())
//                                        }
//                                        Timber.e(
//                                            "${
//                                                AppApplication.applicationContext()
//                                                    .getString(vn.techres.line.R.string.error_message)
//                                            } ${data.getMessage()}"
//                                        )
//                                        hideDialog()
//                                    }
//                                }
//
//                                override fun onHttpFail(e: java.lang.Exception?) {
//
//                                    Timber.e(
//                                        "${
//                                            AppApplication.applicationContext()
//                                                .getString(vn.techres.line.R.string.error_message)
//                                        } ${e?.message}"
//                                    )
//                                    hideDialog()
//                                }
//
//                                override fun onHttpEnd(call: Call?) {
//                                    //empty
//                                }
//
//                                override fun onHttpStart(call: Call?) {
//                                    //empty
//                                }
//                            })
//                    }
//
//                }
//            })
//        dialogFeedBack!!.show()
//    }
//
//    private fun selected(btnOne: Int, btnTwo: Int) {
//        if (btnOne == 1) {
//            binding.itemRcv.tvAll.setBackgroundResource(vn.techres.line.R.drawable.bg_form_input_on)
//            binding.itemRcv.tvAll.setTextColor(
//                resources.getColor(
//                    vn.techres.line.R.color.white, null
//                )
//            )
//        } else {
//            binding.itemRcv.tvAll.setBackgroundResource(R.drawable.bg_form_not_friend)
//            binding.itemRcv.tvAll.setTextColor(
//                resources.getColor(
//                    vn.techres.line.R.color.gray, null
//                )
//            )
//        }
//        if (btnTwo == 1) {
//            binding.itemRcv.tvNew.setBackgroundResource(vn.techres.line.R.drawable.bg_form_input_on)
//            binding.itemRcv.tvNew.setTextColor(
//                resources.getColor(
//                    vn.techres.line.R.color.white, null
//                )
//            )
//        } else {
//            binding.itemRcv.tvNew.setBackgroundResource(R.drawable.bg_form_not_friend)
//            binding.itemRcv.tvNew.setTextColor(
//                resources.getColor(
//                    vn.techres.line.R.color.gray, null
//                )
//            )
//        }
//    }
//
//
//    private fun checkEmpty(array: ArrayList<Friend>) {
//        if (array.isEmpty()) {
//            binding.itemRcv.recyclerViewMyFriend.hide()
//            binding.itemRcv.lnEmpty.show()
//        } else {
//            binding.itemRcv.recyclerViewMyFriend.show()
//            binding.itemRcv.lnEmpty.hide()
//        }
//    }
//
//
//    @SuppressLint("NotifyDataSetChanged")
//    private fun setData(data: ArrayList<Friend>) {
//
//        binding.itemRcv.tvNew.text = String.format(
//            "%s %s",
//            getString(R.string.not_friend),
//            filterNotFriend().size.toString()
//        )
//        binding.itemRcv.tvAll.text =
//            String.format("%s %s", getString(R.string.all), db.size.toString())
//
//
//        dataList.addAll(data)
//
//        adapterMyContact!!.notifyDataSetChanged()
//
//        binding.itemRcv.sflContact.hide()
//        binding.itemRcv.recyclerViewMyFriend.show()
//        binding.itemRcv.sflContact.stopShimmer()
//
//
//
//        checkEmpty(dataList)
//        setEnable(true)
//    }
//
//    private fun setEnable(isCheck: Boolean) {
//
//        binding.btnUpdate.isEnabled = isCheck
//        binding.lnUpdate.isEnabled = isCheck
//        binding.itemRcv.tvNew.isEnabled = isCheck
//        binding.itemRcv.tvAll.isEnabled = isCheck
//        hideDialog()
//
//    }
//
//    private fun filterNotFriend(): ArrayList<Friend> {
//        val data: ArrayList<Friend> = ArrayList()
//        db.forEach {
//            if (it.contactType != AppConstants.FRIEND && it.contactType != AppConstants.ITS_ME)
//                data.add(it)
//        }
//        binding.itemRcv.tvNew.text =
//            String.format("%s %s", getString(R.string.not_friend), data.size.toString())
//        return data
//    }
//
//    private fun filterAll(): ArrayList<Friend> {
//        binding.itemRcv.tvAll.text =
//            String.format("%s %s", getString(R.string.all), db.size.toString())
//        return db
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    private fun search(charText: String, data: ArrayList<Friend>) {
//        dataList.clear()
//        val finalCharText = charText.lowercase(Locale.getDefault())
//        if (charText != "") {
//            val tmpList: ArrayList<Friend> = ArrayList()
//            tmpList.addAll(data.filter {
//                it.fullName.lowercase()
//                    .contains(finalCharText) || removeVietnameseFromString(it.fullName).lowercase()
//                    .contains(finalCharText)
//                        || it.name.lowercase()
//                    .contains(finalCharText) || removeVietnameseFromString(it.name).lowercase()
//                    .contains(finalCharText)
//                        || it.phone.lowercase().contains(finalCharText)
//            })
//
//            setData(tmpList)
//
//
//            /**
//             * làm hài lòng qc thôi
//             * phần này sau này có db local xong thì xoá đi
//             */
//            binding.itemRcv.tvNew.text =
//                String.format("%s %s", getString(R.string.not_friend), filterNotFriend().filter {
//                    it.fullName.lowercase()
//                        .contains(finalCharText) || removeVietnameseFromString(it.fullName).lowercase()
//                        .contains(finalCharText)
//                            || it.name.lowercase()
//                        .contains(finalCharText) || removeVietnameseFromString(it.name).lowercase()
//                        .contains(finalCharText)
//                            || it.phone.lowercase().contains(finalCharText)
//                }.size.toString())
//            binding.itemRcv.tvAll.text = String.format("%s %s", getString(R.string.all), db.filter {
//                it.fullName.lowercase()
//                    .contains(finalCharText) || removeVietnameseFromString(it.fullName).lowercase()
//                    .contains(finalCharText)
//                        || it.name.lowercase()
//                    .contains(finalCharText) || removeVietnameseFromString(it.name).lowercase()
//                    .contains(finalCharText)
//                        || it.phone.lowercase().contains(finalCharText)
//            }.size.toString())
//
//        } else {
//            setData(data)
//        }
//    }
//
//    private fun removeVietnameseFromString(str: String): String {
//        val slug: String = try {
//            val temp: String = Normalizer.normalize(str, Normalizer.Form.NFD)
//            val pattern: Pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
//            pattern.matcher(temp).replaceAll("")
//        } catch (e: Exception) {
//            e.printStackTrace()
//            ""
//        }
//        return slug
//    }
//
//
//}