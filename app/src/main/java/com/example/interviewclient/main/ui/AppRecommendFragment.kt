package com.example.interviewclient.main.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.interviewclient.R
import com.example.interviewclient.base.BaseFragment
import com.example.interviewclient.main.adapter.AppRecommendAdapter
import com.example.interviewclient.main.view_model.MainViewModel
import com.example.interviewclient.util.PackageInfoUtil
import kotlinx.android.synthetic.main.fragment_app_recommend.*

/**
 * 推荐列表页
 */
class AppRecommendFragment(override val mViewModel: MainViewModel) : BaseFragment<MainViewModel>() {
    private lateinit var appRecommendAdapter: AppRecommendAdapter

    override fun onBindLayout(): Int = R.layout.fragment_app_recommend

    override fun initView() {
        app_recommend_swipe_refresh.setOnRefreshListener {
            activity?.run {
                mViewModel.getRecommendApp(this)
            }
        }
        app_recommend_list_view.layoutManager = LinearLayoutManager(activity)
        appRecommendAdapter = AppRecommendAdapter(activity) {
            activity?.run {
                val packageName = mViewModel.appRecommend.value?.get(it)?.packageName ?: ""
                if (!PackageInfoUtil.isApplicationAvailable(this, packageName)) {
                    installApp(packageName)
                } else {
                    mViewModel.toastMsg.value = getString(R.string.has_install)
                }
            }
        }
        app_recommend_list_view.adapter = appRecommendAdapter
    }

    override fun initViewObservable() {
        mViewModel.appRecommend.observe(this, Observer {
            mViewModel.loadingViewVisible.value = false
            if (app_recommend_swipe_refresh.isRefreshing)
                app_recommend_swipe_refresh.isRefreshing = false
            when {
                it == null -> {
                    mViewModel.toastMsg.value = getString(R.string.cant_connect_service)
                }
                it.isEmpty() -> {
                    app_recommend_no_data.visibility = VISIBLE
                    appRecommendAdapter.appInfo = it
                    appRecommendAdapter.notifyDataSetChanged()
                }
                else -> {
                    app_recommend_no_data.visibility = GONE
                    appRecommendAdapter.appInfo = it
                    appRecommendAdapter.notifyDataSetChanged()
                }
            }
        })
        mViewModel.installRecommendPosition.observe(this, Observer {
            appRecommendAdapter.notifyItemRemoved(it)
        })
    }

    override fun initData() {
        activity?.run {
            mViewModel.loadingViewVisible.value = true
            mViewModel.getRecommendApp(this)
        }
    }


    /**
     * 去应用市场下载app
     */
    private fun installApp(packageName: String) {
        val uri: Uri = Uri.parse("market://details?id=$packageName")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            mViewModel.toastMsg.value = getString(R.string.cant_install)
        }
    }
}