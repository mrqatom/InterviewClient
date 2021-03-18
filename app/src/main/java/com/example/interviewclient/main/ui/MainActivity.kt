package com.example.interviewclient.main.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import com.example.interviewclient.R
import com.example.interviewclient.base.BaseActivity
import com.example.interviewclient.main.adapter.MViewPagerAdapter
import com.example.interviewclient.main.view_model.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<MainViewModel>() {

    override val mViewModel by lazy { initViewModel<MainViewModel>() }
    private val fragments = ArrayList<Fragment>()

    override fun onBindLayout(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        fragments.add(AppInfoListFragment(mViewModel))
        fragments.add(AppRecommendFragment(mViewModel))

        viewpager.adapter =
            MViewPagerAdapter(
                supportFragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                fragments
            )

        tablayout.setupWithViewPager(viewpager)
        tablayout.getTabAt(0)?.setText(R.string.app_list)
        tablayout.getTabAt(1)?.setText(R.string.recommend_list)
    }

    override fun enableLoadingView(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(packageReceiver, IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addDataScheme("package")
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(packageReceiver)
    }

    private val packageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.run {
                // 安装
                if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
                    dataString?.substringAfter(":")?.let {
                        mViewModel.installApp(
                            packageManager.getPackageInfo(it, 0),
                            this@MainActivity
                        )
                    }
                }
                // 移除
                if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
                    dataString?.substringAfter(":")?.let {
                        mViewModel.uninstallApp(it)
                    }
                }
            }
        }

    }
}