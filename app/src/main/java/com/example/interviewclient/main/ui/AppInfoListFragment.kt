package com.example.interviewclient.main.ui

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.interviewclient.R
import com.example.interviewclient.app_info_detail.ui.AppInfoDetailActivity
import com.example.interviewclient.base.BaseFragment
import com.example.interviewclient.constant.IntentConstant
import com.example.interviewclient.main.adapter.AppInfoListAdapter
import com.example.interviewclient.main.view_model.MainViewModel
import kotlinx.android.synthetic.main.fragment_app_info_list.*

/**
 * 已安装应用列表页
 */
class AppInfoListFragment(override val mViewModel: MainViewModel) : BaseFragment<MainViewModel>() {

    private lateinit var appInfoListAdapter: AppInfoListAdapter

    override fun onBindLayout(): Int = R.layout.fragment_app_info_list

    override fun initView() {
        app_list_swipe_refresh.setOnRefreshListener {
            mViewModel.getAppInfo(activity)
        }
        app_info_list_view.layoutManager = LinearLayoutManager(activity)
        appInfoListAdapter = AppInfoListAdapter(activity) {
            val intent = Intent(activity, AppInfoDetailActivity::class.java)
            intent.putExtra(IntentConstant.INTENT_KEY_APP_INFO, mViewModel.appInfo.value?.get(it))
            startActivity(intent)
        }
        app_info_list_view.adapter = appInfoListAdapter
    }

    override fun initViewObservable() {
        mViewModel.appInfo.observe(this, Observer {
            mViewModel.loadingViewVisible.value = false
            if (app_list_swipe_refresh.isRefreshing)
                app_list_swipe_refresh.isRefreshing = false
            if (it == null || it.isEmpty()) {
                app_list_no_data.visibility = View.VISIBLE
                appInfoListAdapter.appInfo = it
                appInfoListAdapter.notifyDataSetChanged()
            } else {
                app_list_no_data.visibility = View.GONE
                appInfoListAdapter.appInfo = it
                appInfoListAdapter.notifyDataSetChanged()
                mViewModel.toastMsg.value = getString(R.string.app_list_update)
            }
        })
        mViewModel.installRecommendPosition.observe(this, Observer {
            appInfoListAdapter.notifyItemInserted(0)
        })
        mViewModel.uninstallPosition.observe(this, Observer {
            appInfoListAdapter.notifyItemRemoved(it)
        })
    }

    override fun initData() {
        mViewModel.loadingViewVisible.value = true
        mViewModel.getAppInfo(activity)
    }

}