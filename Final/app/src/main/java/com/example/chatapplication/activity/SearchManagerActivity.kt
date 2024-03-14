package com.example.chatapplication.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import com.example.chatapplication.R
import com.example.chatapplication.api.CreateConversationApi
import com.example.chatapplication.api.SearchUserApi
import com.example.chatapplication.app.AppActivity
import com.example.chatapplication.app.AppApplication
import com.example.chatapplication.constant.AppConstants
import com.example.chatapplication.databinding.ActivitySearchManagerBinding
import com.example.chatapplication.model.HttpData
import com.example.chatapplication.model.entity.GroupChat
import com.example.chatapplication.model.entity.GroupId
import com.example.chatapplication.model.entity.SearchUser
import com.example.chatapplication.model.entity.User
import com.example.chatapplication.utils.AppUtils.hide
import com.example.chatapplication.utils.AppUtils.show
import com.google.gson.Gson
import com.gyf.immersionbar.ImmersionBar
import com.hjq.http.EasyHttp
import com.hjq.http.listener.HttpCallbackProxy
import okhttp3.Call
import org.greenrobot.eventbus.EventBus
import timber.log.Timber

class SearchManagerActivity : AppActivity() {
    private lateinit var binding: ActivitySearchManagerBinding
    private var userFind = User()

    companion object {
        var keySearch: String = ""
    }

    override fun getLayoutView(): View {
        binding = ActivitySearchManagerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initView() {
        ImmersionBar.setTitleBar(this, binding.lnHeader)
    }

    override fun initData() {
        eventAction()
    }

    private fun eventAction() {

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.lnItemUser.show()
                searchUser(query!!)
                hideKeyboard(binding.svSearch)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.lnItemUser.hide()
                keySearch = newText ?: ""
                return true
            }
        })

        binding.btnAction.setOnClickListener {
            createGroup(userFind)
        }

        binding.lnItemUser.setOnClickListener {
            val bundle = Bundle()
            val intent = Intent(
                this@SearchManagerActivity,
                PeopleInformationActivity::class.java
            )
            bundle.putString(
                AppConstants.USER_DATA, Gson().toJson(userFind)
            )
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    //-------------------------------------------API----------------------------------------------//
    private fun searchUser(phone: String) {
        EasyHttp.get(this)
            .api(SearchUserApi.params(phone))
            .request(object : HttpCallbackProxy<HttpData<SearchUser>>(this) {
                override fun onHttpStart(call: Call?) {
                    //Empty
                }

                override fun onHttpFail(throwable: Throwable?) {
                    Timber.e(
                        "${
                            AppApplication.applicationContext()
                                .getString(R.string.error_message)
                        } ${throwable}"
                    )
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onHttpSuccess(data: HttpData<SearchUser>) {
                    if (data.isRequestSucceed()) {
                        if(data.getData()!!.user != null){
                            binding.lnItemUser.show()
                            binding.lnEmpty.hide()
                            userFind = data.getData()!!.user
                            binding.tvUserName.text = userFind.fullName
                        }else{
                            binding.lnItemUser.hide()
                            binding.lnEmpty.show()
                        }

                    } else {
                        binding.lnEmpty.show()
                        binding.lnItemUser.hide()
                        Timber.e(
                            "${
                                AppApplication.applicationContext()
                                    .getString(R.string.error_message)
                            } ${data.getMessage()}"
                        )
                    }
                }
            })
    }

    private fun createGroup(user: User) {
        EasyHttp.post(this).api(CreateConversationApi.params(user.id))
            .request(object : HttpCallbackProxy<HttpData<GroupId>>(this) {
                @SuppressLint("IntentWithNullActionLaunch")
                override fun onHttpSuccess(data: HttpData<GroupId>) {
                    if (data.isRequestSucceed()) {
                        val bundle = Bundle()
                        val intent = Intent(this@SearchManagerActivity, ConversationDetailActivity::class.java)
                        val group = GroupChat()
                        with(group) {
                            conversationId = data.getData()!!.conversationId
                            type = AppConstants.TYPE_PRIVATE
                            name = user.fullName
                            avatar = user.avatar
                        }
                        bundle.putString(AppConstants.GROUP_DATA, Gson().toJson(group))
                        intent.putExtras(bundle)
                        startActivity(intent)
                        finish()
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
                    hideDialog()
                    Timber.e(
                        "${
                            AppApplication.applicationContext()
                                .getString(R.string.error_message)
                        } ${throwable}"
                    )
                }

                override fun onHttpStart(call: Call?) {
                    showDialog()
                }

                override fun onHttpEnd(call: Call?) {
                    //empty
                }
            })
    }



}