package com.example.interviewclient.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.interviewclient.R
import com.example.interviewclient.base.BaseFragment
import com.example.interviewclient.main.adapter.AppRecommendAdapter
import com.example.interviewclient.main.view_model.MainViewModel
import kotlinx.android.synthetic.main.fragment_app_recommend.*

/**
 * 推荐列表页
 */
class AppRecommendFragment(override val mViewModel: MainViewModel) : BaseFragment<MainViewModel>() {

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
            installApp(mViewModel.appRecommend.value?.get(it)?.packageName)
        }
        app_recommend_list_view.adapter = appRecommendAdapter
    }

    override fun initViewObservable() {
        mViewModel.appRecommend.observe(this, Observer {
            appRecommendAdapter.appInfo = it
            appRecommendAdapter.notifyDataSetChanged()
        })
    }

    override fun initData() {
        activity?.run {
            mViewModel.getRecommendApp(this)
        }
    }

    /**
     * 去应用市场下载app
     */
    private fun installApp(packageName: String?) {
        packageName?.run {

        }
    }
}