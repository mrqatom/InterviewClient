package com.example.interviewclient.main.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
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
    companion object {
        private const val INSTALL_REQUEST_CODE = 0x1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_app_recommend, container, false)
    }

    private lateinit var appRecommendAdapter: AppRecommendAdapter

    override fun initView() {
        app_recommend_list_view.layoutManager = LinearLayoutManager(activity)
        appRecommendAdapter = AppRecommendAdapter(activity) {
            activity?.run {
                val packageName = mViewModel.appRecommend.value?.get(it)?.packageName ?: ""
                if (!PackageInfoUtil.isApplicationAvailable(this, packageName)) {
                    mViewModel.recommendInstallPosition = it
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
            when {
                it == null -> {
                    mViewModel.toastMsg.value = getString(R.string.cant_connect_service)
                }
                it.isEmpty() -> {
                    app_recommend_no_data.visibility = VISIBLE
                }
                else -> {
                    app_recommend_no_data.visibility = GONE
                    appRecommendAdapter.appInfo = it
                    appRecommendAdapter.notifyDataSetChanged()
                }
            }
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
            startActivityForResult(goToMarket, INSTALL_REQUEST_CODE)
        } catch (e: ActivityNotFoundException) {
            mViewModel.toastMsg.value = getString(R.string.cant_install)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            INSTALL_REQUEST_CODE ->
                context?.run {
                    mViewModel.checkInstallAndUpdate(this) {
                        appRecommendAdapter.notifyItemRemoved(it)
                    }
                }
            else -> {
            }
        }
    }
}