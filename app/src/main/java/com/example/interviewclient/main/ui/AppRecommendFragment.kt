package com.example.interviewclient.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.interviewclient.R
import com.example.interviewclient.base.BaseFragment
import com.example.interviewclient.main.view_model.MainViewModel

class AppRecommendFragment(override val mViewModel: MainViewModel) : BaseFragment<MainViewModel>() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_app_recommend, container, false)
    }

    override fun initView() {
    }

    override fun initViewObservable() {
    }

    override fun initData() {

    }
}