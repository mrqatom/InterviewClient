package com.example.interviewclient.main.ui

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.interviewclient.R
import com.example.interviewclient.app_info_detail.ui.AppInfoDetailActivity
import com.example.interviewclient.base.BaseFragment
import com.example.interviewclient.constant.IntentConstant
import com.example.interviewclient.main.adapter.AppInfoListAdapter
import com.example.interviewclient.main.view_model.MainViewModel
import com.example.interviewclient.util.PermissionUtil
import com.example.interviewclient.util.ToastUtil
import kotlinx.android.synthetic.main.fragment_app_info_list.*

class AppInfoListFragment(override val mViewModel: MainViewModel) : BaseFragment<MainViewModel>() {
    private lateinit var appInfoListAdapter: AppInfoListAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_app_info_list, container, false)
    }

    override fun initView() {
        app_list_swipe_refresh.setOnRefreshListener {
            mViewModel.getAppInfo(activity)
            app_list_swipe_refresh.isRefreshing = false
        }
        app_info_list_view.layoutManager = LinearLayoutManager(activity)
        appInfoListAdapter = AppInfoListAdapter(activity) {
            val intent = Intent(activity, AppInfoDetailActivity::class.java)
            intent.putExtra(IntentConstant.INTENT_KEY_APP_INFO,mViewModel.appInfo.value?.get(it))
            startActivity(intent)
        }
        app_info_list_view.adapter = appInfoListAdapter
    }

    override fun initViewObservable() {
        mViewModel.appInfo.observe(this, Observer {
            appInfoListAdapter.appInfo = it
            appInfoListAdapter.notifyDataSetChanged()
            ToastUtil.showShort(context, getString(R.string.app_list_update))
        })
    }

    override fun initData() {
        mViewModel.getAppInfo(activity)
    }

}